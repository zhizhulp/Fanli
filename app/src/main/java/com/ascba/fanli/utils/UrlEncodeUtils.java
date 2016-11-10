package com.ascba.fanli.utils;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * url加密
 */

public class UrlEncodeUtils {
    public static String createSign(String baseUrl){
        String secret_key="46c229d744bc3a013332aff722d32c23";
        String currentDate = TimeUtils.milliseconds2String(System.currentTimeMillis(),new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()));
        LogUtils.PrintLog("456",EncryptUtils.MD5(baseUrl + currentDate+ secret_key,false));
        return EncryptUtils.MD5(baseUrl + currentDate+ secret_key,false);
    }
}
