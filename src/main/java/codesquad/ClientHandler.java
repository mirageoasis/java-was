package codesquad;

import http.Http;
import java.io.BufferedReader;
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
        try (OutputStream clientOutput = clientSocket.getOutputStream();
            InputStream inputStream = clientSocket.getInputStream()
        ) {
            // HTTP 응답을 생성합니다.
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            Http http = Http.generate(bufferedReader);
            logger.info("Request: {}", http);

            clientOutput.write("HTTP/1.1 200 OK\r\n".getBytes());
            clientOutput.write("Content-Type: text/html\r\n".getBytes());
            clientOutput.write("Content-Length: 14\r\n".getBytes());
            clientOutput.write("\r\n".getBytes());
            clientOutput.write("<h1>Hello</h1>".getBytes()); // 응답 본문으로 "Hello"를 보냅니다.
            clientOutput.flush();
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
