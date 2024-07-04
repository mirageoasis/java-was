package http.startline;

import http.Header;
import http.Http;

public class ResponseLine extends StartLine{

    // http 응답 구현에 따라서 상태 코드와 상태 메시지를 저장하는 클래스
    private int statusCode;
    private String statusMessage;

    public ResponseLine() {
        super();
    }

    public ResponseLine(String version) {
        super(version);
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public void success() {
        setStatusCode(200);
        setStatusMessage("OK");
        setVersion("HTTP/1.1");
    }

    @Override
    public String toString() {
        return getVersion() + " " + statusCode + " " + statusMessage + "\r\n";
    }

    public void redirect(Http httpRequest, Http httpResponse, String urlPath) {
        ResponseLine responseLine = (ResponseLine) httpResponse.getStartLine();
        responseLine.setStatusCode(302);
        responseLine.setStatusMessage("Found");
        responseLine.setVersion("HTTP/1.1");

        Header responseHeader = httpResponse.getHeader();
        responseHeader.addHeader("Location", urlPath);

        httpResponse.setStartLine(responseLine);
    }
}
