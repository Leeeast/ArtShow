package com.art.huakai.artshow.entity;


import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;

import java.io.Serializable;

public class InfoBaseResp implements Serializable {
    private int errCode;
    private String code;
    private String state;
    private String lang;
    private String country;

    public InfoBaseResp(BaseResp baseResp) {
        this.errCode = baseResp.errCode;

        if (baseResp instanceof SendAuth.Resp) {
            code = ((SendAuth.Resp) baseResp).code;
            state = ((SendAuth.Resp) baseResp).state;
        }
    }

    public int getErrCode() {
        return errCode;
    }

    public String getCode() {
        return code;
    }

    public String getState() {
        return state;
    }

}
