package de.sub.goettingen.tghttpserver;

import info.textgrid.lab.authn.RBACSession;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Logger;

import org.apache.http.Header;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.eclipse.ui.IStartup;
import org.nanohttpd.protocols.http.IHTTPSession;
import org.nanohttpd.protocols.http.NanoHTTPD;
import org.nanohttpd.protocols.http.request.Method;
import org.nanohttpd.protocols.http.response.Response;
import org.nanohttpd.protocols.http.response.Status;
import org.nanohttpd.util.ServerRunner;

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
 * This class starts the NannoHTTP Server on port 9090.
 * the incoming request is redirected to the TextGrid WebService 
 * The result is then served locally.
 * Why don't directly put the URL of the TextGrid Service into your documents?
 * You need to be authenticated to retrieve data. This plugin takes the login
 * Information from the TG Lab to ask the TG Service for the object.
 * 
 * @author Johannes Biermann, SUB Göttingen
 * @version 1.0.1
 *
 */
public class TGHTTPServer extends NanoHTTPD implements IStartup {

    private static final Logger LOG = Logger.getLogger(TGHTTPServer.class.getName());
    private static final String version = "1.0.1";

	@Override
	public void earlyStartup() {
		TGHTTPServer.LOG.info("Starting TG HTTP localhost server");
		ServerRunner.run(TGHTTPServer.class);
	}    
    
    
    public TGHTTPServer() {
        super("localhost", 9090);
        TGHTTPServer.LOG.info("listen on http://"+this.getHostname()+":"+this.myPort);
    }

    @Override
    public Response serve(IHTTPSession session) {
        Method method = session.getMethod();
        String uri = session.getUri();
        TGHTTPServer.LOG.info(method + " '" + uri + "' ");
        
        ResponseBean response = null;
        
        if (!uri.equals("/")) {     
	        try {
	        	response = retrieveTGObjectHC(uri.substring(1));
	        }catch (Exception e) {
	        	StringWriter sw = new StringWriter();
	        	e.printStackTrace(new PrintWriter(sw));
	        	String stacktrace = sw.toString();
	        	response = createMessage("Error fetching object from TextGrid", "Got an exception calling TextGrid-Repository: "+e.getMessage()+"<pre>"+stacktrace+"</pre>");
	        }
        }else{
        	response = createMessage("Welcome to the TextGrid Repository localhost proxy server!", 
           			"Usage: add the desired TextGrid URI at the end of the URL to retrieve a specific object "
                			+ "(e.g. <a href=\"http://"+this.getHostname()+":"+this.getListeningPort()+"/textgrid:vqmz.0\">http://"+this.getHostname()+":"+this.getListeningPort()+"/textgrid:vqmz.0</a> will serve the TextGrid Object 'textgrid:vqmz.0' via HTTP.)."
               );
        }
        return Response.newFixedLengthResponse(Status.OK, response.getContentType(), response.getResponse());
    }
    
    private ResponseBean retrieveTGObjectHC(String tgurl) throws Exception {
    	
    	ResponseBean responseBean = new ResponseBean();
    	
    	CloseableHttpClient client = HttpClients.createDefault();
    	
    	// Fetch TextGrid session ID
    	String sid = RBACSession.getInstance().getSID(false);
    	// Create HTTP Request
    	HttpGet httpget = new HttpGet("https://textgridlab.org/1.0/tgcrud/rest/"+tgurl+"/data?sessionId="+sid);
    	
    	CloseableHttpResponse response = client.execute(httpget);
        int statusCode = response.getStatusLine().getStatusCode();

        if (statusCode != HttpStatus.SC_OK) {
        	throw new Exception("HTTP Retrieve from TG repository failed: " + response.getStatusLine().getReasonPhrase() + " HTTP Code "+response.getStatusLine().getStatusCode());
        }
        ByteArrayOutputStream data = new ByteArrayOutputStream();
        response.getEntity().writeTo(data);
        // Read the response body.
        responseBean.setResponse(data.toByteArray()); 
        data.close();

        Header contentType = response.getFirstHeader("Content-Type");
        Header lastModified = response.getFirstHeader("Last-Modified");
        
        responseBean.setContentType(contentType.getValue());
        responseBean.setLastModified(lastModified.getValue());
    	
        client.close();
    	
		return responseBean;
    	
    }
    
    private ResponseBean createMessage(String headline, String message) {
    	ResponseBean responseBean = new ResponseBean();
    	responseBean.setContentType("text/html");
    	
    	String html ="<!DOCTYPE html>\n" +
    			"<html>\n" +
    			"<head>\n" +
    			"<meta charset=\"UTF-8\">\n" +
    			"<title>TextGrid HTTP Server</title>\n" +
    			"\n" +
    			"<style>\n" +
    			"body { background-color: #7292b4; }\n" +
    			".outer { padding: 30px; }\n" +
    			".logo   { margin:0 auto; width: 95%; background-color: #f3f3f3; padding:10px; box-sizing: border-box;}\n" +
    			".text    {margin:0 auto; width: 95%; background-color: #fff; padding:10px; box-sizing: border-box; font-family: OpenSans,Helvetica,Arial,sans-serif; font-size: 16px; line-height: 26px; font-weight: normal;}\n" +
    			".footer {margin:0 auto; width: 95%; background-color: #fff; padding:10px; box-sizing: border-box; font-family: OpenSans,Helvetica,Arial,sans-serif; font-size: 12px }\n" +
    			"h1 { font-family:SancoaleSoftenedRegular,Helvetica,Arial,sans-serif; font-size: 46px; line-height: 52px; font-weight: normal; margin:0px; }\n" +
    			"</style>\n" +
    			"</head>\n" +
    			"\n" +
    			"<body>\n" +
    			"<div class=\"outer\">\n" +
    			"\n" +
    			"<div class=\"logo\"><img src=\"data:image/png;base64,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\"></div>\n" +
    			"<div class=\"text\"><h1>"+headline+"</h1><p>"+message+"</p></div>\n" +
    			"<div class=\"footer\">Version "+version+", additional information can be found on the <a href=\"https://github.com/Hannah-Arendt-Project/TextGridHttpServerPlugin\">GitHub page</a></div>\n" +
    			"\n" +
    			"</div>\n" +
    			"</body>\n" +
    			"</html> ";	
    	
    	responseBean.setResponse(html.getBytes());
    	
    	return responseBean;
    }

	
}
