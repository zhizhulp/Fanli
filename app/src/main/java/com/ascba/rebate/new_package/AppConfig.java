package com.ascba.rebate.new_package;

/**
 * Created by 李平 on 2017/8/10.
 * shareprefrence 管理类
 */

public class AppConfig {
    private static AppConfig appConfig= new AppConfig();
    private AppConfig(){
    }

    public static AppConfig getInstance(){
        return appConfig;
    }

}
