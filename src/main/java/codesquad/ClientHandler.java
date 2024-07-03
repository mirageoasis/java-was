package codesquad;

import handler.MyHandler;
import handler.MyHandlerMapper;
import http.Http;
import http.startline.RequestLine;
import http.startline.ResponseLine;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import org.slf4j.Logger;
import util.LoggerUtil;

public class ClientHandler implements Runnable {

    private final Socket clientSocket;
    private final Logger logger = LoggerUtil.getLogger();

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try (
            OutputStream clientOutput = clientSocket.getOutputStream();
            InputStream inputStream = clientSocket.getInputStream()
        ) {
            // HTTP 응답을 생성합니다.
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            Http httpRequest = Http.generateHttpRequest(bufferedReader);
            Http httpResponse = Http.generateHttpResponse();
            RequestLine requestLine = (RequestLine) httpRequest.getStartLine();
            logger.info("Request: {}", httpRequest);
            // resource/static/index.html 파일을 읽어서 보내기
            // 핸들러는 매칭되는 것만 찾고 파일은 핸들러와 무관하다. 일단은 핸들러가 작동하지 않는다고 가정하고 코드를 짠다.

            MyHandlerMapper handlerMapper = MyHandlerMapper.getInstance();

            MyHandler temp = handlerMapper.findHandler(requestLine.getUrlPath().getPath());
            if (temp == null) {
                logger.error("Handler not found");
                return;
            }

            temp.handle(httpRequest, httpResponse);

            // response 기준으로 다시 쓴다.
            // HTTP 응답 헤더 생성
            /*
            String httpResponseHeader = "HTTP/1.1 200 OK\r\n" +
            "Content-Type: " + contentType + "\r\n" +
            "Content-Length: " + fileContent.length + "\r\n" +
            "\r\n";
            * */
            String responseLine = ((ResponseLine) httpResponse.getStartLine()).toString();
            String responseHeaderLine = httpResponse.getHeader().toString();

            logger.info("ResponseLine\n{}", responseLine);
            logger.info("Response Header\n{}", responseHeaderLine);

            clientOutput.write(responseLine.getBytes());
            clientOutput.write(responseHeaderLine.getBytes());
            clientOutput.write("\r\n".getBytes());
            clientOutput.write(httpResponse.getBody());
            clientOutput.flush();

        } catch (FileNotFoundException e) {
            logger.error("File not found: {}", e.getMessage());
        } catch (IOException e) {
            logger.error("Error handling client: {}", e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                logger.error("Error closing client socket: {}", e.getMessage());
            }
        }
    }
}
