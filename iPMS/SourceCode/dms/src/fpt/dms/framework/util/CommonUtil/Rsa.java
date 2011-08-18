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
 
 package fpt.dms.framework.util.CommonUtil;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import com.fms1.infoclass.LinkInfo;
/**
 * MANU:13-feb-04
 * the Rsa algo is adapted from :
 * http://pajhome.org.uk/crypt/rsa/implementation.html
 * paj@pajhome.org.uk
 */
public class Rsa {
	public BigInteger publicKey, modulus;
	private BigInteger privateKey; //private key
	private static Vector rsas = new Vector();
	private String ip;
	private String id;
	//must be even number do not change, the size must be known by the client
	private static final int IDSIZE = 6;
	private static String separator = "_";
	public Rsa(int bitlen) {
		SecureRandom r = new SecureRandom();
		BigInteger p = new BigInteger(bitlen / 2, 100, r);
		BigInteger q = new BigInteger(bitlen / 2, 100, r);
		modulus = p.multiply(q);
		
		BigInteger m = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
		publicKey = new BigInteger("23227");
		while (m.gcd(publicKey).intValue() > 1) {
			publicKey = publicKey.add(new BigInteger("2"));
        }
		privateKey = publicKey.modInverse(m);
	}

	public BigInteger encrypt(BigInteger message) {
		return message.modPow(publicKey, modulus);
	}

	public static String encrypt(String strmessage, String strmodulus, String strpublicKey) {
		BigInteger message = new BigInteger(strmessage.getBytes());
		BigInteger thepublicKey = new BigInteger(strpublicKey, 16);
		BigInteger modulus = new BigInteger(strmodulus, 16);
		return message.modPow(thepublicKey, modulus).toString(16);
	}

    public String decrypt(String code) {
  		BigInteger decode;
		String decodeStr = "";
		//the code is a string representing the hexa value of the crypted logon
		StringTokenizer st = new StringTokenizer(code, separator);
		while (st.hasMoreTokens()) {
			decode = new BigInteger(st.nextToken(), 16);
			decode = decode.modPow(privateKey, modulus);
			decodeStr = decodeStr + new String(decode.toByteArray());
		}
		return decodeStr;
	}
	/**
	 * sends public key to other tools
	 */
	public static final void getAuthenticate(HttpServletRequest request, HttpServletResponse response) {
		try {
			//System.out.println("getRSA");
			Rsa rsa = new Rsa(160);
			rsa.ip = request.getRemoteAddr();
			String idTemp;
			//make sure this ID is not already register
			while (true) {
				idTemp = getRandomString(IDSIZE);
				//System.out.println("id=" + idTemp);
				for (int i = 0; i < rsas.size(); i++)
					if (((Rsa) rsas.elementAt(i)).id.equals(idTemp))
						continue;
				break;
			}
			rsa.id = idTemp;
			rsas.add(rsa);
			PrintWriter writer = response.getWriter();
			writer.print(rsa.id + separator + rsa.modulus.toString(16) + separator + rsa.publicKey.toString(16) + "\n");
			writer.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * see above function
	 */
	public static final Rsa getRsaFromPool(String theID) {
		Rsa rsa = null;
		try {
			for (int i = 0; i < rsas.size(); i++) {
				rsa = (Rsa) rsas.elementAt(i);
				if (rsa.id.equals(theID)) {
					rsas.remove(i);
					break;
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			return rsa;
		}
	}
	/*public static final LinkInfo getRemoteLink(String HTTPURL) {
		String raw = Http.getURL(HTTPURL);
		return parseKey(raw);
	}
	public static final LinkInfo parseKey(String hash) {
		LinkInfo link = null;
		String[] split = new String[3];
		try {
			final StringTokenizer strToken = new StringTokenizer(hash, separator);
			int i = 0;
			while (strToken.hasMoreElements()) {
				split[i] = strToken.nextToken();
				System.out.println(i + "/" + split[i]);
				i++;
			}
			link = new LinkInfo();
			link.id=split[0];
			link.modulus=split[1];
			link.publicKey=split[2];
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			return link;
		}
	}*/
	/**
	* idsize must be an even number
	*/
	public static final String getRandomString(int idSize) {
		if (idSize % 2 != 0) {
			System.out.println("Rsa: id size must be even number");
        }
		SecureRandom r = new SecureRandom();
		byte[] buf = new byte[idSize / 2];
		r.nextBytes(buf);
		BigInteger bi = new BigInteger(buf);
		return bi.toString(16);
	}
}
