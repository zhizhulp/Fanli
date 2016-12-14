package com.ascba.rebate.utils;

/**
 * 存储所有网址
 */

public class UrlUtils {
    public static final String baseWebsite="http://api.qlqwgw.com";
    public static final String baseWebsiteChange="http://api.qlqwgw.com";
    public static final String webVersion="v1";
    public static final String webVersionChange="v2";

    //基本操作
    public static final String sendMsg=baseWebsite+"/"+webVersion+"/"+"sendMsg";//短信验证
    public static final String register=baseWebsite+"/"+webVersion+"/"+"register";//注册
    public static final String login=baseWebsite+"/"+webVersion+"/"+"login";//登录
    public static final String getBackPwd=baseWebsite+"/"+webVersion+"/"+"getBackPwd";//密码找回
    //首页尾页数据
    public static final String index=baseWebsite+"/"+webVersion+"/"+"index";//首页
    public static final String user=baseWebsite+"/"+webVersion+"/"+"user";//尾页
    //设置
    public static final String userSet=baseWebsite+"/"+webVersion+"/"+"userSet";//个人资料
    public static final String updateSet=baseWebsite+"/"+webVersion+"/"+"updateSet";//修改个人资料
    public static final String findCardInfo=baseWebsite+"/"+webVersion+"/"+"findCardInfo";//身份证查询
    public static final String verifyCard=baseWebsite+"/"+webVersion+"/"+"verifyCard";//实名认证
    public static final String checkCardId=baseWebsite+"/"+webVersion+"/"+"checkCardId";//检查实名认证
    public static final String changePwd=baseWebsite+"/"+webVersion+"/"+"changePwd";//密码修改
    //银行卡
    public static final String getBankList=baseWebsite+"/"+webVersion+"/"+"getBankList";//银行卡列表数据????????????
    public static final String getBankCard=baseWebsite+"/"+webVersion+"/"+"getBankCard";//获取要添加的银行卡的信息
    public static final String verifyBankCard=baseWebsite+"/"+webVersion+"/"+"verifyBankCard";//银行卡实名认证
    public static final String delBanks=baseWebsite+"/"+webVersion+"/"+"delBanks";//删除银行卡???????????????????
    //代金券
    public static final String getVoucherList=baseWebsite+"/"+webVersion+"/"+"getVoucherList";//代金券
    //商户申请
    public static final String addCompany=baseWebsite+"/"+webVersion+"/"+"addCompany";//公司设置(商户申请前要进行提交公司信息)
    public static final String getCompany=baseWebsite+"/"+webVersion+"/"+"getCompany";//获取商户申请状态（0,1,2,3）
    public static final String setCompany=baseWebsite+"/"+webVersion+"/"+"setCompany";//设置商家信息
    public static final String getBusinesses=baseWebsite+"/"+webVersion+"/"+"getBusinesses";//获取商家详情

    public static final String payment=baseWebsite+"/"+webVersion+"/"+"payment";//支付宝支付

    public static final String partook=baseWebsite+"/"+webVersion+"/"+"partook";//二维码生成

    public static final String getReferee=baseWebsite+"/"+webVersion+"/"+"getReferee";//推荐?????????
    //代理
    public static final String upgradedList=baseWebsite+"/"+webVersion+"/"+"upgradedList";//特权升级列表
    public static final String getUpgraded=baseWebsite+"/"+webVersion+"/"+"getUpgraded";//代理开通数据？？？？？？？？
    public static final String getJoinArea=baseWebsite+"/"+webVersion+"/"+"getJoinArea";//地区查询？？？？？？？
    public static final String backJoinArea=baseWebsite+"/"+webVersion+"/"+"backJoinArea";//????????
    public static final String memberUpgraded=baseWebsite+"/"+webVersion+"/"+"memberUpgraded";//??????????????

    //推送
    public static final String checkMember=baseWebsite+"/"+webVersion+"/"+"checkMember";
    public static final String confirmOrder=baseWebsite+"/"+webVersion+"/"+"confirmOrder";
    public static final String addTransaction=baseWebsite+"/"+webVersion+"/"+"addTransaction";
    //红积分
    public static final String getRedScore=baseWebsite+"/"+webVersion+"/"+"getRedScore";
}
