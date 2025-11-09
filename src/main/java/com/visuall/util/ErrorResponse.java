package com.visuall.util;

public class ErrorResponse {
    public String error;

    public ErrorResponse() {}

    public ErrorResponse(String error) {
        this.error = error;
    }

    public String getError() { return error; }
    public void setError(String error) { this.error = error; }
}
