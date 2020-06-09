package com.cnqisoft.advice;

public class Response {

    private int status;

    private String message;

    private Object data;

    public Response(String message) {
        this.message = message;
    }

    public Response(Object data) {
        this(1, null, data);
    }

    public Response(int status, Object data) {
        this(status, null, data);
    }

    public Response(int status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public static Response success(Object data) {
        return success(null, data);
    }

    public static Response success(String message, Object data) {
        return new Response(1, message, data);
    }

    public static Response error(String message) {
        return new Response(0, message);
    }

    public static Response error(Exception e) {
        return error(e.getMessage());
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}
