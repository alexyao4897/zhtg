package com.money.deep.tstock.model;

/**
 * Created by fengxg on 2016/9/12.
 */
public class StatusInfo {
    private String Version;
    private String ReturnCode;
    private String Status;
    private String StatusCode;
    private String ErrorDetailCode;

    public String getVersion() {
        return Version;
    }

    public String getStatusCode() {
        return StatusCode;
    }

    public void setStatusCode(String statusCode) {
        StatusCode = statusCode;
    }

    public void setVersion(String version) {
        Version = version;
    }

    public String getReturnCode() {
        return ReturnCode;
    }

    public void setReturnCode(String returnCode) {
        ReturnCode = returnCode;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getErrorDetailCode() {
        return ErrorDetailCode;
    }

    public void setErrorDetailCode(String errorDetailCode) {
        ErrorDetailCode = errorDetailCode;
    }
}
