package com.art.huakai.artshow.entity;

import java.io.Serializable;

/**
 * Created by lidongliang on 2017/11/13.
 */

public class ClientBean implements Serializable {
    //     "clientVersion": {
//        "id": "2c91faca5f8f5475015f9e89c70a0066",
//                "versionCode": 101,
//                "versionName": "1.0.1",
//                "versionDescpt": "发现新版本",
//                "downloadUrl": "http://www.baidu.com",
//                "isMandatory": 1,
//                "createTime": 1510386440000,
//                "system": "android",
//                "enabled": 1
//    }
//}
    public ClientVersion clientVersion;

    public class ClientVersion implements Serializable{
        public String id;
        public int versionCode;
        public String versionName;
        public String versionDescpt;
        public String downloadUrl;
        //isMandatory表示是否强制升级，1是;0否
        public int isMandatory;
        public long createTime;
        public String system;
        public String enabled;
    }
}
