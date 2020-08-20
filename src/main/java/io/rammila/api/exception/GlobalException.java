package io.rammila.api.exception;

public class GlobalException extends RuntimeException{
    private static final long serialVersionUID = 2771174581631905388L;


    public GlobalException() {
    }

    public GlobalException(String message) {
        super(message);
    }

    public GlobalException(Throwable cause) {
        super(cause);
    }

    public GlobalException(String message, Throwable cause) {
        super(message, cause);
    }


}
