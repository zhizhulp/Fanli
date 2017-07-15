package com.ascba.rebate.beans.sweep.promotion_ceremony;

import java.util.List;

/**
 * Created by lenovo on 2017/7/12.
 */

public class PromotionCeremoneyEntity {

    /**
     * uuid : 81
     * token : 0741ddf48013e3604b8e530932d92010
     * expiring_time : 1502606423
     * update_status : 0
     * tokenFail : false
     * isLogin : true
     * isExpiringTime : false
     * count : 0
     * courtesy : {"image":"/public/base/images/courtesy.jpg","title":"哇！花多少赚多少，京东都怕了。","subtitle":"商品多，配送快，品质好，天天省钱，消费能赚钱，快快体验吧！","courtesy_id":"9810150984","courtesy_img":"/public/base/images/zhuan.jpg","courtesy_url":"http://home.qlqwshop.com/reg/9810150984"}
     * total_money : 5
     * people_num : 2
     * referee_list : [{"member_id":273,"mobile":"159****6066","avatar":"/public/base/images/default.jpg","remarks":"未完成首单","money":0},{"member_id":82,"mobile":"158****2251","avatar":"/public/base/images/default.jpg","remarks":"已完成首单","money":"5.00"}]
     * now_page : 1
     * total_page : 1
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
    private String total_money;
    private int people_num;
    private int now_page;
    private int total_page;
    private List<RefereeListBean> referee_list;

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

    public String getTotal_money() {
        return total_money;
    }

    public void setTotal_money(String total_money) {
        this.total_money = total_money;
    }

    public int getPeople_num() {
        return people_num;
    }

    public void setPeople_num(int people_num) {
        this.people_num = people_num;
    }

    public int getNow_page() {
        return now_page;
    }

    public void setNow_page(int now_page) {
        this.now_page = now_page;
    }

    public int getTotal_page() {
        return total_page;
    }

    public void setTotal_page(int total_page) {
        this.total_page = total_page;
    }

    public List<RefereeListBean> getReferee_list() {
        return referee_list;
    }

    public void setReferee_list(List<RefereeListBean> referee_list) {
        this.referee_list = referee_list;
    }

    public static class CourtesyBean {
        /**
         * image : /public/base/images/courtesy.jpg
         * title : 哇！花多少赚多少，京东都怕了。
         * subtitle : 商品多，配送快，品质好，天天省钱，消费能赚钱，快快体验吧！
         * courtesy_id : 9810150984
         * courtesy_img : /public/base/images/zhuan.jpg
         * courtesy_url : http://home.qlqwshop.com/reg/9810150984
         */

        private String image;
        private String title;
        private String subtitle;
        private String courtesy_id;
        private String courtesy_img;
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

        public String getCourtesy_img() {
            return courtesy_img;
        }

        public void setCourtesy_img(String courtesy_img) {
            this.courtesy_img = courtesy_img;
        }

        public String getCourtesy_url() {
            return courtesy_url;
        }

        public void setCourtesy_url(String courtesy_url) {
            this.courtesy_url = courtesy_url;
        }
    }

    public static class RefereeListBean {
        /**
         * member_id : 273
         * mobile : 159****6066
         * avatar : /public/base/images/default.jpg
         * remarks : 未完成首单
         * money : 0
         */

        private int member_id;
        private String mobile;
        private String avatar;
        private String remarks;
        private double money;

        public int getMember_id() {
            return member_id;
        }

        public void setMember_id(int member_id) {
            this.member_id = member_id;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }

        public double getMoney() {
            return money;
        }

        public void setMoney(double money) {
            this.money = money;
        }
    }
}
