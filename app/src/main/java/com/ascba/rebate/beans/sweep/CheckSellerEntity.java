package com.ascba.rebate.beans.sweep;

/**
 * Created by lenovo on 2017/6/30.
 */

public class CheckSellerEntity {


    /**
     * uuid : 2919
     * token : 44cf5560f82a83743a1d20b1bc215b10
     * expiring_time : 1501380027
     * update_status : 0
     * tokenFail : false
     * isLogin : true
     * isExpiringTime : false
     * count : 0
     * info : {"self_money":"0.00","seller":2562,"seller_cover_logo":"/public/uploads/seller/logo/2562/2562.png","seller_name":"固安县广泰汽车贸易有限公司"}
     */

    private int uuid;
    private String token;
    private int expiring_time;
    private int update_status;
    private boolean tokenFail;
    private boolean isLogin;
    private boolean isExpiringTime;
    private int count;
    private InfoBean info;

    public int getUuid() {
        return uuid;
    }

    public void setUuid(int uuid) {
        this.uuid = uuid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getExpiring_time() {
        return expiring_time;
    }

    public void setExpiring_time(int expiring_time) {
        this.expiring_time = expiring_time;
    }

    public int getUpdate_status() {
        return update_status;
    }

    public void setUpdate_status(int update_status) {
        this.update_status = update_status;
    }

    public boolean isTokenFail() {
        return tokenFail;
    }

    public void setTokenFail(boolean tokenFail) {
        this.tokenFail = tokenFail;
    }

    public boolean isIsLogin() {
        return isLogin;
    }

    public void setIsLogin(boolean isLogin) {
        this.isLogin = isLogin;
    }

    public boolean isIsExpiringTime() {
        return isExpiringTime;
    }

    public void setIsExpiringTime(boolean isExpiringTime) {
        this.isExpiringTime = isExpiringTime;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public InfoBean getInfo() {
        return info;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }

    public static class InfoBean {
        /**
         * self_money : 0.00
         * seller : 2562    http://home.qlqwp2p.com/public/uploads/seller/image/2562/2562.png
         * seller_cover_logo : http://home.qlqwp2p.com/public/uploads/seller/logo/2562/2562.png
         * seller_name : 固安县广泰汽车贸易有限公司
         */

        private String self_money;
        private int seller;
        private String seller_cover_logo;
        private String seller_name;

        public String getSelf_money() {
            return self_money;
        }

        public void setSelf_money(String self_money) {
            this.self_money = self_money;
        }

        public int getSeller() {
            return seller;
        }

        public void setSeller(int seller) {
            this.seller = seller;
        }

        public String getSeller_cover_logo() {
            return seller_cover_logo;
        }

        public void setSeller_cover_logo(String seller_cover_logo) {
            this.seller_cover_logo = seller_cover_logo;
        }

        public String getSeller_name() {
            return seller_name;
        }

        public void setSeller_name(String seller_name) {
            this.seller_name = seller_name;
        }
    }
}
