package com.ascba.rebate.beans.sweep;

import java.util.List;

/**
 * Created by lenovo on 2017/7/5.
 */

public class ToBeSuredOrdersEntity {


    /**
     * uuid : 85
     * token : a3935dbfb41685a378eceb9a1a5df9e0
     * expiring_time : 1502002631
     * update_status : 0
     * tokenFail : false
     * isLogin : true
     * isExpiringTime : false
     * count : 0
     * iden_info : {"title":"待确认总额","total_money":114,"total":4,"tip":"仅统计待确认交易账单，其它请查看流水记录"}
     * data_list : [{"fivepercent_log_id":589,"avatar":"/public/base/images/default.jpg","money":"+1.00","pay_type_text":"现金付款","order_status_text":"待确认","create_time":1499398972},{"fivepercent_log_id":590,"avatar":"/public/base/images/default.jpg","money":"+1.00","pay_type_text":"现金付款","order_status_text":"待确认","create_time":1499399086},{"fivepercent_log_id":612,"avatar":"/public/base/images/default.jpg","money":"+111.00","pay_type_text":"现金付款","order_status_text":"待确认","create_time":1499409210},{"fivepercent_log_id":618,"avatar":"/public/base/images/default.jpg","money":"+1.00","pay_type_text":"现金付款","order_status_text":"待确认","create_time":1499411801}]
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
    private IdenInfoBean iden_info;
    private int now_page;
    private int total_page;
    private List<DataListBean> data_list;

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

    public IdenInfoBean getIden_info() {
        return iden_info;
    }

    public void setIden_info(IdenInfoBean iden_info) {
        this.iden_info = iden_info;
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

    public List<DataListBean> getData_list() {
        return data_list;
    }

    public void setData_list(List<DataListBean> data_list) {
        this.data_list = data_list;
    }

    public static class IdenInfoBean {
        /**
         * title : 待确认总额
         * total_money : 114
         * total : 4
         * tip : 仅统计待确认交易账单，其它请查看流水记录
         */

        private String title;
        private int total_money;
        private int total;
        private String tip;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getTotal_money() {
            return total_money;
        }

        public void setTotal_money(int total_money) {
            this.total_money = total_money;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public String getTip() {
            return tip;
        }

        public void setTip(String tip) {
            this.tip = tip;
        }
    }

    public static class DataListBean {
        /**
         * fivepercent_log_id : 589
         * avatar : /public/base/images/default.jpg
         * money : +1.00
         * pay_type_text : 现金付款
         * order_status_text : 待确认
         * create_time : 1499398972
         */

        private int fivepercent_log_id;
        private String avatar;
        private String money;
        private String pay_type_text;
        private String order_status_text;
        private long create_time;

        public int getFivepercent_log_id() {
            return fivepercent_log_id;
        }

        public void setFivepercent_log_id(int fivepercent_log_id) {
            this.fivepercent_log_id = fivepercent_log_id;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getPay_type_text() {
            return pay_type_text;
        }

        public void setPay_type_text(String pay_type_text) {
            this.pay_type_text = pay_type_text;
        }

        public String getOrder_status_text() {
            return order_status_text;
        }

        public void setOrder_status_text(String order_status_text) {
            this.order_status_text = order_status_text;
        }

        public long getCreate_time() {
            return create_time;
        }

        public void setCreate_time(long create_time) {
            this.create_time = create_time;
        }
    }

}
