package handler;

import http.HttpRequest;
import http.HttpResponse;
import http.ResponseWriter;
import java.io.IOException;

public class RegistrationHandler extends MyHandler{

    @Override
    void doGet(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        // TODO: registration.html으로 redirect
        ResponseWriter.redirect(httpRequest, httpResponse, "/registration/index.html");
    }

    @Override
    void doPost(HttpRequest httpRequest, HttpResponse httpResponse) {

    }
}
