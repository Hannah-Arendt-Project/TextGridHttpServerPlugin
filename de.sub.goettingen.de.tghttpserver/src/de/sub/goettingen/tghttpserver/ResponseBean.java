package de.sub.goettingen.tghttpserver;

/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

/**
 * Helper bean to create the HTTP response.
 * 
 * @author Johannes Biermann, SUB Göttingen
 * @version 1.0.1
 *
 */
public class ResponseBean {
	
	private byte[] response;
	private String contentType;
	private String lastModified;
	public byte[] getResponse() {
		return response;
	}
	public void setResponse(byte[] response) {
		this.response = response;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public String getLastModified() {
		return lastModified;
	}
	public void setLastModified(String lastModified) {
		this.lastModified = lastModified;
	}

	
	
}
