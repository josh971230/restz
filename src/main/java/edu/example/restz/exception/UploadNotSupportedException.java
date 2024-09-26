package edu.example.restz.exception;

public class UploadNotSupportedException extends RuntimeException {
    public UploadNotSupportedException(String message) {
        super(message);
    }
}
