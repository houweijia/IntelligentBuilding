package com.example.veigar.intelligentbuilding.bean;

import java.util.List;

/**
 * Created by veigar on 2017/3/23.
 */

public class PersonAttendanceData {
    private String resultcode;
    private String reason;
    private List<Result> result;

    private String errorCode;
    public void setResultcode(String resultcode) {
        this.resultcode = resultcode;
    }
    public String getResultcode() {
        return resultcode;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
    public String getReason() {
        return reason;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }
    public List<Result> getResult() {
        return result;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
    public String getErrorCode() {
        return errorCode;
    }
}
