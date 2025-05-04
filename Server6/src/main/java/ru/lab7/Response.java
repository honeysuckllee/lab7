package ru.lab7;

import java.io.Serializable;

public class Response implements Serializable {
    private static final long serialVersionUID = 1L;
    private String responseInfo;
    private boolean isEndCommand;

    public Response(String responseInfo) {
        this.responseInfo = responseInfo;
        isEndCommand = false;
    }

    public String getResponseInfo() {
        return responseInfo;
    }

    public Response(String responseInfo, boolean isEndCommand) {
        this.responseInfo = responseInfo;
        this.isEndCommand = isEndCommand;
    }

    public boolean isEmpty() {
        return responseInfo == null;
    }
}
