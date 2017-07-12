package com.ascba.rebate.beans.sweep.promotion_ceremony;

import java.util.List;

/**
 * Created by lenovo on 2017/7/12.
 */

public class PromotionCeremoneyEntity {

    /**
     * uuid : 85
     * token : f9943b7a0dc135098923bbd7fefbb16f
     * expiring_time : 1502422120
     * update_status : 0
     * tokenFail : false
     * isLogin : true
     * isExpiringTime : false
     * count : 0
     * courtesy : {"image":"/public/base/images/courtesy.jpg","title":"哇！花多少赚多少，京东都怕了。","subtitle":"商品多，配送快，品质好，天天省钱，消费能赚钱，快快体验吧！","courtesy_id":"5710053535","courtesy_url":"http://home.qlqwshop.com/reg/5710053535"}
     * total_money : 0
     * people_num : 0
     * referee_list : []
     */

    private int uuid;
    private String token;
    private int expiring_time;
    private int update_status;
    private boolean tokenFail;
    private boolean isLogin;
    private boolean isExpiringTime;
    private int count;
    private CourtesyBean courtesy;
    private int total_money;
    private int people_num;
    private List<?> referee_list;

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

    public CourtesyBean getCourtesy() {
        return courtesy;
    }

    public void setCourtesy(CourtesyBean courtesy) {
        this.courtesy = courtesy;
    }

    public int getTotal_money() {
        return total_money;
    }

    public void setTotal_money(int total_money) {
        this.total_money = total_money;
    }

    public int getPeople_num() {
        return people_num;
    }

    public void setPeople_num(int people_num) {
        this.people_num = people_num;
    }

    public List<?> getReferee_list() {
        return referee_list;
    }

    public void setReferee_list(List<?> referee_list) {
        this.referee_list = referee_list;
    }

    public static class CourtesyBean {
        /**
         * image : /public/base/images/courtesy.jpg
         * title : 哇！花多少赚多少，京东都怕了。
         * subtitle : 商品多，配送快，品质好，天天省钱，消费能赚钱，快快体验吧！
         * courtesy_id : 5710053535
         * courtesy_url : http://home.qlqwshop.com/reg/5710053535
         */

        private String image;
        private String title;
        private String subtitle;
        private String courtesy_id;
        private String courtesy_url;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSubtitle() {
            return subtitle;
        }

        public void setSubtitle(String subtitle) {
            this.subtitle = subtitle;
        }

        public String getCourtesy_id() {
            return courtesy_id;
        }

        public void setCourtesy_id(String courtesy_id) {
            this.courtesy_id = courtesy_id;
        }

        public String getCourtesy_url() {
            return courtesy_url;
        }

        public void setCourtesy_url(String courtesy_url) {
            this.courtesy_url = courtesy_url;
        }
    }
}
