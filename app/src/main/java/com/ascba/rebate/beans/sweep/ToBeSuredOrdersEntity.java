package com.ascba.rebate.beans.sweep;

import java.util.List;

/**
 * Created by lenovo on 2017/7/5.
 */

public class ToBeSuredOrdersEntity {


    /**
     * uuid : 85
     * token : 67f4ccb11c1f55ce9e90d9e3a8ec55a9
     * expiring_time : 1501837572
     * update_status : 0
     * tokenFail : false
     * isLogin : true
     * isExpiringTime : false
     * count : 0
     * iden_info : {"title":"待确认总额","total_money":"10000元","total":10,"tip":"仅统计待确认交易账单，其它请查看流水记录"}
     * data_list : [{"order_id":571,"avatar":null,"money":"+356.70","pay_type_text":"现金付款","order_status_text":"待商家确认"},{"order_id":572,"avatar":null,"money":"+356.70","pay_type_text":"现金付款","order_status_text":"待商家确认"},{"order_id":573,"avatar":null,"money":"+356.70","pay_type_text":"现金付款","order_status_text":"待商家确认"},{"order_id":574,"avatar":null,"money":"+356.70","pay_type_text":"现金付款","order_status_text":"待商家确认"},{"order_id":575,"avatar":null,"money":"+356.70","pay_type_text":"现金付款","order_status_text":"待商家确认"},{"order_id":578,"avatar":null,"money":"+356.70","pay_type_text":"现金付款","order_status_text":"待商家确认"},{"order_id":579,"avatar":null,"money":"+356.70","pay_type_text":"现金付款","order_status_text":"待商家确认"},{"order_id":580,"avatar":null,"money":"+356.70","pay_type_text":"现金付款","order_status_text":"待商家确认"},{"order_id":581,"avatar":null,"money":"+356.70","pay_type_text":"现金付款","order_status_text":"待商家确认"},{"order_id":582,"avatar":null,"money":"+356.70","pay_type_text":"现金付款","order_status_text":"待商家确认"},{"order_id":583,"avatar":null,"money":"+356.70","pay_type_text":"现金付款","order_status_text":"待商家确认"},{"order_id":584,"avatar":null,"money":"+356.70","pay_type_text":"现金付款","order_status_text":"待商家确认"},{"order_id":585,"avatar":null,"money":"+356.70","pay_type_text":"现金付款","order_status_text":"待商家确认"},{"order_id":586,"avatar":null,"money":"+356.70","pay_type_text":"现金付款","order_status_text":"待商家确认"},{"order_id":587,"avatar":null,"money":"+356.70","pay_type_text":"现金付款","order_status_text":"待商家确认"}]
     * paged : 1
     * page_total : 2
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
    private int paged;
    private int page_total;
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

    public int getPaged() {
        return paged;
    }

    public void setPaged(int paged) {
        this.paged = paged;
    }

    public int getPage_total() {
        return page_total;
    }

    public void setPage_total(int page_total) {
        this.page_total = page_total;
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
         * total_money : 10000元
         * total : 10
         * tip : 仅统计待确认交易账单，其它请查看流水记录
         */

        private String title;
        private String total_money;
        private int total;
        private String tip;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTotal_money() {
            return total_money;
        }

        public void setTotal_money(String total_money) {
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
         * order_id : 571
         * avatar : null
         * money : +356.70
         * pay_type_text : 现金付款
         * order_status_text : 待商家确认
         */

        private int order_id;
        private Object avatar;
        private String money;
        private String pay_type_text;
        private String order_status_text;

        public int getOrder_id() {
            return order_id;
        }

        public void setOrder_id(int order_id) {
            this.order_id = order_id;
        }

        public Object getAvatar() {
            return avatar;
        }

        public void setAvatar(Object avatar) {
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
    }
}
