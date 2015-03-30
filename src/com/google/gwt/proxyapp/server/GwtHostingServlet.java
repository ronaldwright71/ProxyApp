package com.google.gwt.proxyapp.server;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class GwtHostingServlet extends HttpServlet {

	@Override
	 protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

	   resp.setContentType("text/html");
	   resp.setCharacterEncoding("UTF-8");
	   String ip = getClientIpAddress(req);
	   // Print a simple HTML page including a <script> tag referencing your GWT module as the response
	   PrintWriter writer = resp.getWriter();
	   doHosting(writer, ip);
	  }
	 
	public void doHosting(PrintWriter writer, String ip) {
		writer.append("<html><head>")
		       .append("<script type=\"text/javascript\" src=\"/proxyapp/proxyapp.nocache.js\"></script>")
		       .append("</head><body><p>Hello, world!</p><p>" + ip + "</p></body></html>");
	}
	
	private static final String[] HEADERS_TO_TRY = { 
	    "X-Forwarded-For",
	    "Proxy-Client-IP",
	    "WL-Proxy-Client-IP",
	    "HTTP_X_FORWARDED_FOR",
	    "HTTP_X_FORWARDED",
	    "HTTP_X_CLUSTER_CLIENT_IP",
	    "HTTP_CLIENT_IP",
	    "HTTP_FORWARDED_FOR",
	    "HTTP_FORWARDED",
	    "HTTP_VIA",
	    "REMOTE_ADDR" };
	
	public static String getClientIpAddress(HttpServletRequest request) {
	    for (String header : HEADERS_TO_TRY) {
	        String ip = request.getHeader(header);
	        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
	            return ip;
	        }
	    }	    
	    return request.getRemoteAddr();
	}

}