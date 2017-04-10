package com.ascba.rebate.utils;

/**
 * 存储所有网址
 */

public class UrlUtils {
    public static final String baseWebsite = "http://api.qlqwp2p.com";
    public static final String baseWebsiteChange = "http://home.qlqwp2p.com";
    public static final String webVersion = "v1";
    public static final String urlPrefix = baseWebsite + "/" + webVersion + "/";

    //基本操作
    public static final String sendMsg = baseWebsite + "/" + webVersion + "/" + "sendMsg";//短信验证
    public static final String register = baseWebsite + "/" + webVersion + "/" + "register";//注册
    public static final String login = baseWebsite + "/" + webVersion + "/" + "login";//登录
    public static final String getBackPwd = baseWebsite + "/" + webVersion + "/" + "getBackPwd";//密码找回
    //首页尾页数据
    public static final String index = baseWebsite + "/" + webVersion + "/" + "index";//首页
    public static final String user = baseWebsite + "/" + webVersion + "/" + "user";//尾页
    //设置
    public static final String userSet = baseWebsite + "/" + webVersion + "/" + "userSet";//个人资料
    public static final String updateSet = baseWebsite + "/" + webVersion + "/" + "updateSet";//修改个人资料
    public static final String findCardInfo = baseWebsite + "/" + webVersion + "/" + "findCardInfo";//身份证查询
    public static final String verifyCard = baseWebsite + "/" + webVersion + "/" + "verifyCard";//实名认证
    public static final String checkCardId = baseWebsite + "/" + webVersion + "/" + "checkCardId";//检查实名认证
    public static final String changePwd = baseWebsite + "/" + webVersion + "/" + "changePwd";//密码修改
    //银行卡
    public static final String getBankList = baseWebsite + "/" + webVersion + "/" + "getBankList";//银行卡列表数据
    public static final String getBankCard = baseWebsite + "/" + webVersion + "/" + "getBankCard";//获取要添加的银行卡的信息
    public static final String verifyBankCard = baseWebsite + "/" + webVersion + "/" + "verifyBankCard";//银行卡实名认证
    public static final String delBanks = baseWebsite + "/" + webVersion + "/" + "delBanks";//删除银行卡
    //代金券
    public static final String getVoucherList = baseWebsite + "/" + webVersion + "/" + "getVoucherList";//代金券
    //兑现券列表
    public static final String getCashingList = baseWebsite + "/" + webVersion + "/" + "getCashingList";

    //商户申请
    public static final String addCompany = baseWebsite + "/" + webVersion + "/" + "addCompany";//提交商家的公司审核资料
    public static final String getCompany = baseWebsite + "/" + webVersion + "/" + "getCompany";//获取商户申请状态（0,1,2,3）
    //public static final String setCompany=baseWebsite+"/"+webVersion+"/"+"setCompany";//设置商家信息(旧)
    public static final String setTenants = baseWebsite + "/" + webVersion + "/" + "setTenants";//设置商家信息
    public static final String getBusinesses = baseWebsite + "/" + webVersion + "/" + "getBusinesses";//获取商家详情

    public static final String payment = baseWebsite + "/" + webVersion + "/" + "payment";//支付宝支付

    public static final String partook = baseWebsite + "/" + webVersion + "/" + "partook";//二维码生成

    //public static final String getCashingMoney=baseWebsite+"/"+webVersion+"/"+"getCashingMoney";//推荐列表
    public static final String getMyReferee = baseWebsite + "/" + webVersion + "/" + "getMyReferee";//推荐列表

    //代理
    public static final String upgradedList = baseWebsite + "/" + webVersion + "/" + "upgradedList";//特权升级列表
    public static final String getUpgraded = baseWebsite + "/" + webVersion + "/" + "getUpgraded";//代理开通数据
    public static final String getJoinArea = baseWebsite + "/" + webVersion + "/" + "getJoinArea";//地区查询
    public static final String backJoinArea = baseWebsite + "/" + webVersion + "/" + "backJoinArea";//
    public static final String memberUpgraded = baseWebsite + "/" + webVersion + "/" + "memberUpgraded";//

    //推送
    public static final String checkMember = baseWebsite + "/" + webVersion + "/" + "checkMember";
    public static final String confirmOrder = baseWebsite + "/" + webVersion + "/" + "confirmOrder";
    public static final String addTransaction = baseWebsite + "/" + webVersion + "/" + "addTransaction";
    //红积分
    public static final String getRedScore = baseWebsite + "/" + webVersion + "/" + "getRedScore";
    public static final String redIntegration = baseWebsite + "/" + webVersion + "/" + "redIntegration";//兑换红积分

    public static final String getCashingInfo = baseWebsite + "/" + webVersion + "/" + "getCashingInfo";
    public static final String billList = baseWebsite + "/" + webVersion + "/" + "billList";//获取现金账单列表
    public static final String getTillsMoney = baseWebsite + "/" + webVersion + "/" + "getTillsMoney";//获取提现信息
    public static final String cashingMoney = baseWebsite + "/" + webVersion + "/" + "cashingMoney";
    public static final String tillsMoney = baseWebsite + "/" + webVersion + "/" + "tillsMoney";//提现结果
    //协议
    public static final String service = baseWebsiteChange + "/" + "service";//银行卡服务协议
    public static final String regAgree = baseWebsiteChange + "/" + "regAgree";//注册协议
    public static final String explain = baseWebsiteChange + "/" + "explain";//商家入驻流程
    public static final String shop = urlPrefix + "shop";//商城


    //public static final String getCompanyInfo=baseWebsite+"/"+webVersion+"/"+"getCompanyInfo";
    public static final String findCompany = baseWebsite + "/" + webVersion + "/" + "findCompany";//获取公司信息
    public static final String resubmitCompany = baseWebsite + "/" + webVersion + "/" + "resubmitCompany";//资料有误重复提交


    public static final String getMyPReferee = baseWebsite + "/" + webVersion + "/" + "getMyPReferee";//二级推荐
    public static final String wxpay = baseWebsite + "/" + webVersion + "/" + "wxpay";//微信支付
    public static final String getCharging = baseWebsite + "/" + webVersion + "/" + "getCharging";//商家扣费比率

    public static final String wealth = baseWebsite + "/" + webVersion + "/" + "wealth";//财富

    public static final String getMyPspread = baseWebsite + "/" + webVersion + "/" + "getMyPspread";//筛选列表;
    public static final String getSearchPspread = baseWebsite + "/" + webVersion + "/" + "getSearchPspread";//筛选出的一级推广;
    public static final String getGroupPspread = baseWebsite + "/" + webVersion + "/" + "getGroupPspread";//一级推广

    public static final String getGroupPpspread = baseWebsite + "/" + webVersion + "/" + "getGroupPpspread";//二级筛选列表
    public static final String getSearchPpspread = baseWebsite + "/" + webVersion + "/" + "getSearchPpspread";//二级筛选结果

    //商品详情
    public static final String getGoodsArticle = urlPrefix + "goodsArticle";

    //周边
    public static final String getNearBy = urlPrefix + "nearby";

    //商城分类
    public static final String category = urlPrefix + "category";

    //收款
    public static final String receivables = urlPrefix + "receivables";

    //商家店铺
    public static final String getStore = urlPrefix + "getStore";

    //购物车
    public static final String shoppingCart = urlPrefix + "shoppingCart";

    //收货地址
    public static final String getMemberAddress = urlPrefix + "getMemberAddress";

    //添加收货地址
    public static final String memberAddressAdd = urlPrefix + "memberAddressAdd";

    //修改收货地址
    public static final String memberAddressEdit = urlPrefix + "memberAddressEdit";

    //删除收货地址
    public static final String memberAddressDel = urlPrefix + "memberAddressDel";

    //设置默认收货地址
    public static final String memberAddressSetDefault = urlPrefix + "memberAddressSetDefault";

    //购物车-选中商品
    public static final String cartSelectdGoods = urlPrefix + "cartSelectdGoods";

    //确认订单
    public static final String createOrder = urlPrefix + "createOrder";

    //购物车-加减商品
    public static final String cartChangenumGoods = urlPrefix + "cartChangenumGoods";

    //购物车-删除商品
    public static final String cartDeleteGoods = urlPrefix + "cartDeleteGoods";

    //购物车-结算
    public static final String cartAccount = urlPrefix + "cartAccount";

    //获取商品规格
    public static final String getGoodsSpec = urlPrefix + "getGoodsSpec";

    //首页-创业扶持
    public static final String getDataUrl = urlPrefix + "getDataUrl";

    //商城——我
    public static final String myPageInfo = urlPrefix + "myPageInfo";
    //添加商品到购物车
    public static final String cartAddGoods=urlPrefix + "cartAddGoods";
}
