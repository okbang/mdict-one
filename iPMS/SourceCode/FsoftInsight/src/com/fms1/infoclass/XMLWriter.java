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
 
 package com.fms1.infoclass;

import java.sql.*;
import java.io.*;
import java.util.Vector;
public class XMLWriter{
	public String generateXML(final Vector vtProjectInfo){
		try{
			final StringBuffer buffer = new StringBuffer(1024 * 4);
			if (vtProjectInfo == null){
				return "";
			}
			if (vtProjectInfo.size() == 0){
				return "";
			}
			buffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
			buffer.append("<ProjectList>\n");
			for(int id = 0; id < vtProjectInfo.size(); id++){
				XMLGenerateBean xmlBean = (XMLGenerateBean)vtProjectInfo.get(id);
				buffer.append("<project>\n");

				buffer.append("\t\t<" + xmlBean.getTagProjectName() + ">");
				buffer.append(xmlBean.getProjectName());
				buffer.append("</" + xmlBean.getTagProjectName() + ">\n");

				buffer.append("\t\t<" + xmlBean.getTagCustomer() + ">");
				buffer.append(xmlBean.getCustomer());
				buffer.append("</" + xmlBean.getTagCustomer() + ">\n");

				buffer.append("\t\t<" + xmlBean.getTagProjectManager() + ">");
				buffer.append(xmlBean.getProjectManager());
				buffer.append("</" + xmlBean.getTagProjectManager() + ">\n");

				buffer.append("\t\t<" + xmlBean.getTagProjectPoint() + ">");
				if (xmlBean.getProjectPoint() != 0){
					buffer.append(xmlBean.getProjectPoint());
				}
				else{
					buffer.append("N/A");
				}
				buffer.append("</" + xmlBean.getTagProjectPoint() + ">\n");

				buffer.append("</project>\n");
			}
			buffer.append("</ProjectList>\n");
			return buffer.toString();
		}
		catch (Exception ex){
			ex.printStackTrace();
			return "";
		}
	}
}