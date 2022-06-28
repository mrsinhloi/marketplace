package com.ehubstar.marketplace.retrofit;

import android.content.Context;

import com.ehubstar.marketplace.utils.MyUtils;

import java.io.Serializable;

public class ReturnResult implements Serializable {
    public static final int SUCCESS = 0;
    public static final int TOKEN_EXPIRED = 101;

    public static final String ERROR_CODE_TAG = "errorCode";
    public static final String ERROR_MESSAGE_TAG = "errorMessage";
    public static final String DATA_TAG = "data";


    private int errorCode = -1;
    private String errorMessage = "";
    private Object data;//parse json return

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public static void controlError(ReturnResult result, Context context) {
        if (result != null && context != null) {
            if (result.errorCode != SUCCESS) {
                /*if (result.ResultCode == TOKEN_EXPIRED) {
                    //neu het han thi logout va login lai
                    MH02_MainMenuActivity.Companion.logout();
                } else {*/
                    MyUtils.showAlertDialog(context, result.getErrorMessage());
                //}
            }
        }
    }
}
