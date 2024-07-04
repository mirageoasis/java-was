package http;

import http.startline.ResponseLine;

public class ResponseWriter {

    public static final String CRLF = "\r\n";

    public static void success(HttpResponse httpResponse, byte[] body){
        ResponseLine responseLine = (ResponseLine)httpResponse.getStartLine();
        Header responseHeader = httpResponse.getHeader();

        // start line
        responseLine.setVersion("HTTP/1.1");
        responseLine.setStatusCode(200);
        responseLine.setStatusMessage("OK");

        // header
        responseHeader.addHeader("Content-Length", String.valueOf(body.length));


        // body
        httpResponse.setBody(body);
    }

}
