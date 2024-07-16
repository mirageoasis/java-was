package exception;

import static util.HttpStatusCode.METHOD_NOT_ALLOWED;

public class ServerError extends GeneralException {

    public ServerError() {
        super(METHOD_NOT_ALLOWED.getStatusMessage(), METHOD_NOT_ALLOWED.getStatusCode());
    }
}
