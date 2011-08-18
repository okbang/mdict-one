/*
 * Copyright (c) 2009, FPT Software JSC
 * All rights reserved.
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *	- Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 *	- Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 *	- Neither the name of the FPT Software JSC nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, 
 * BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. 
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, 
 * OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; 
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, 
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
 
 package com.fms1.web;
import java.io.*;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fms1.infoclass.UploadInfo;
import java.net.*;

/**
 * HTTP protocol, used for single logon feature
 * @author manu
 * @date Feb 19, 2004
 */
public class Http {
	/**
	 * the url must start by http://
	 */
	public static String getURL(String urlStr) {
		String response = "error";
		try {
			URL url = new URL(urlStr);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.connect();
			//response = conn.getResponseMessage();
			InputStreamReader reader = new InputStreamReader(conn.getInputStream());
			char[] buff = new char[256];
			reader.read(buff);
			reader.close();
			conn.disconnect();
			int i;
			for (i = 0; i < buff.length; i++) {
				if (buff[i] == '\n')
					break;
			}
			response = new String(buff, 0, i);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			return response;
		}
	}
	public static UploadInfo doUpload(HttpServletRequest request, int maxSize) {
		UploadInfo retval = new UploadInfo();
		try {
			int uploadSize = request.getContentLength();
			if (uploadSize > maxSize) {
				retval.responseType = UploadInfo.TOO_BIG;
				return retval;
			}
			if (uploadSize == 0) {
				retval.responseType = UploadInfo.EMPTY_FILE;
				return retval;
			}
			byte[] headerCrap = new byte[2000];
			ServletInputStream in = request.getInputStream();
			int iterator = 0;
			int headerSize = 0;
			int byteRead = 0;
			int maxHeaders = 20;
			int delimiterSize=0;//used to remove the last delimiter
			String headers;
			int fileNamePos;
			String fileName="";
			String filenameMarker="filename=\"";
			while (iterator <= maxHeaders) {
				iterator++;
				byteRead = in.readLine(headerCrap, 0, headerCrap.length);
				if (byteRead == -1) {
					retval.responseType = UploadInfo.DISCONNECT;
					return retval;
				}
				if (iterator==1)
					delimiterSize=byteRead;
				headerSize += byteRead;
				headers=new String(headerCrap,0,byteRead);
				//System.out.println(headers);
				fileNamePos=headers.indexOf(filenameMarker);
				if (fileNamePos != -1) {
					fileNamePos+=filenameMarker.length();
					fileName=headers.substring(fileNamePos,headers.length()-3);
				}
				if (byteRead == 2 ) //first empty line, beginning of content
					break;
			}
			if (iterator == maxHeaders) {
				retval.responseType = UploadInfo.BAD_HEADERS;
				return retval;
			}
			
			int footerSize=delimiterSize+4;//(1 carriage return (2bytes)before the footer,+2more dashes)
			int fileSize=uploadSize-headerSize-footerSize;
			retval.file = new byte[fileSize];
			int j=0;
			int i = in.read();
			while (i != -1 && j < fileSize) {
				retval.file[j++] =(byte)i;
				i = in.read();
			}
			in.close();
			File file=new File(fileName);
			retval.fileName=file.getName();
			retval.responseType = UploadInfo.SUCCESS;
		}
		catch (Exception e) {
			e.printStackTrace();
			retval.responseType = UploadInfo.UNKNOWN_ERROR;
		}
		finally {
			return retval;
		}
	}
	public static void httpSendFile(HttpServletResponse response,byte [] file){
		try {
		//response.setStatus(HttpServletResponse.SC_CREATED);	
		response.setHeader("Referer","caca.doc");
		response.setContentType(Fms1Servlet.CONTENT_TYPE_BINARY);
		ServletOutputStream out= response.getOutputStream();
		out.write(file);
		out.flush();
		}
		catch (Exception e) {}
	}
}
