package handler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import http.HttpRequest;
import http.HttpResponse;
import http.startline.RequestLine;
import http.startline.ResponseLine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserListHandlerTest {

    private UserListHandler userListHandler;
    private HttpRequest httpRequest;
    private HttpResponse httpResponse;

    @BeforeEach
    void setUp() {
        userListHandler = new UserListHandler();
        RequestLine requestLine = RequestLine.fromString("GET /user/list HTTP/1.1");
        httpRequest = new HttpRequest(requestLine, null, null);
        httpResponse = HttpResponse.generateHttpResponse();
    }

    @Test
    void testUserListRetrievalSuccess() {
        // Simulate a GET request for retrieving the user list
        userListHandler.doGet(httpRequest, httpResponse);

        // Assuming the success is indicated by a specific status code or the presence of user data in the body
        ResponseLine responseLine = (ResponseLine) httpResponse.getStartLine();
        assertEquals(200, responseLine.getStatusCode());
        assertTrue(new String(httpResponse.getBody()).contains("userList"), "Response body should contain 'userList'");
    }

    @Test
    void testUserListRetrievalWithNoUsers() {
        // Ensure no users are available for this test case

        // Simulate a GET request for retrieving the user list
        userListHandler.doGet(httpRequest, httpResponse);

        // Assuming the appropriate handling is indicated by a specific status code or message
        ResponseLine responseLine = (ResponseLine) httpResponse.getStartLine();
        assertEquals(200, responseLine.getStatusCode());
        assertTrue(new String(httpResponse.getBody()).contains("No users available"), "Response body should indicate no users are available");
    }
}