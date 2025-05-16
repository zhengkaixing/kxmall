package com.kxmall.common.utils;

import com.kxmall.common.core.domain.PrintBean;
import com.kxmall.common.core.domain.PrintItemBean;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

/**
 * @author kaixin
 * @version 1.0
 * @date 2023/8/5
 */
public class FeieyunPrint {
    public static final String URL = "http://api.feieyun.cn/Api/Open/";//不需要修改

    public static final String USER = "xxxxxxxx";//*必填*：账号名
    public static final String UKEY = "xxxxxx";//*必填*: 飞鹅云后台注册账号后生成的UKEY 【备注：这不是填打印机的KEY】
    public static final String SN = "xxxx";//*必填*：打印机编号，必须要在管理后台里添加打印机或调用API接口添加之后，才能调用API


    //**********测试时，打开下面注释掉方法的即可,更多接口文档信息,请访问官网开放平台查看**********
    public static void main(String[] args) throws Exception {
        //==================添加打印机接口（支持批量）==================
        //***返回值JSON字符串***
        //正确例子：{"msg":"ok","ret":0,"data":{"ok":["sn#key#remark#carnum","316500011#abcdefgh#快餐前台"],"no":["316500012#abcdefgh#快餐前台#13688889999  （错误：识别码不正确）"]},"serverExecutedTime":3}
        //错误：{"msg":"参数错误 : 该帐号未注册.","ret":-2,"data":null,"serverExecutedTime":37}

        //提示：打印机编号(必填) # 打印机识别码(必填) # 备注名称(选填) # 流量卡号码(选填)，多台打印机请换行（\n）添加新打印机信息，每次最多100行(台)。
//			String snlist = "sn1#key1#remark1#carnum1\nsn2#key2#remark2#carnum2";
//			String method = addprinter(snlist);
//			System.out.println(method);


        //==================方法1.小票机打印订单接口==================
        //***返回值JSON字符串***
        //成功：{"msg":"ok","ret":0,"data":"xxxxxxx_xxxxxxxx_xxxxxxxx","serverExecutedTime":5}
        //失败：{"msg":"错误描述","ret":非0,"data":"null","serverExecutedTime":5}

//        String method1 = printTset(SN,UKEY,USER);//该接口只能是小票机使用,如购买的是标签机请使用下面方法2，调用打印
//        System.out.println(method1);


        String line = String.format("%-8s%-7s%-4s%-4s<BR>", "小白安保",
                "10.0", 1, "10.0");
        String line2 = String.format("%-8s%-7s%-4s%-4s<BR>", "小黑",
                "10.0", 1, "10.0");
        String line3 = String.format("%-8s%-7s%-4s%-4s<BR>", "小黑",
                "10.0", 1, "10.0");
        System.out.println(line);
        System.out.println(line2);
        System.out.println(line3);

        //==================方法2.标签机专用打印订单接口==================
        //***返回值JSON字符串***
        //成功：{"msg":"ok","ret":0,"data":"xxxxxxx_xxxxxxxx_xxxxxxxx","serverExecutedTime":5}
        //失败：{"msg":"错误描述","ret":非0,"data":"null","serverExecutedTime":5}

//			String method2 = printLabelMsg(SN);//打开注释调用标签机打印接口进行打印,该接口只能是标签机使用，其它型号打印机请勿使用该接口
//			System.out.println(method2);


        //===========方法3.查询某订单是否打印成功=============
        //***返回值JSON字符串***
        //成功：{"msg":"ok","ret":0,"data":true,"serverExecutedTime":2}//data:true为已打印,false为未打印
        //失败：{"msg":"错误描述","ret":非0, "data":null,"serverExecutedTime":7}

//			String orderid = "xxxxxxx_xxxxxxxx_xxxxxxxx";//订单ID，从方法1返回值data获取
//			String method3 = queryOrderState(orderid);
//			System.out.println(method3);


        //===========方法4.查询指定打印机某天的订单详情============
        //***返回值JSON字符串***
        //成功：{"msg":"ok","ret":0,"data":{"print":6,"waiting":1},"serverExecutedTime":9}//print已打印，waiting为打印
        //失败：{"msg":"错误描述","ret":非0,"data":"null","serverExecutedTime":5}

//			String strdate = "2016-11-12";//注意时间格式为"yyyy-MM-dd"
//			String method4 = queryOrderInfoByDate(SN,strdate);
//			System.out.println(method4);


        //===========方法5.查询打印机的状态==========================
        //***返回值JSON字符串***
        //成功：{"msg":"ok","ret":0,"data":"状态","serverExecutedTime":4}
        //失败：{"msg":"错误描述","ret":非0,"data":"null","serverExecutedTime":5}

//			String method5 = queryPrinterStatus(SN);
//			System.out.println(method5);

    }


    //=====================以下是函数实现部分================================================

    private static String addprinter(String snlist) {

        //通过POST请求，发送打印信息到服务器
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(30000)//读取超时
                .setConnectTimeout(30000)//连接超时
                .build();

        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .build();

        HttpPost post = new HttpPost(URL);
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("user", USER));
        String STIME = String.valueOf(System.currentTimeMillis() / 1000);
        nvps.add(new BasicNameValuePair("stime", STIME));
        nvps.add(new BasicNameValuePair("sig", signature(USER, UKEY, STIME)));
        nvps.add(new BasicNameValuePair("apiname", "Open_printerAddlist"));//固定值,不需要修改
        nvps.add(new BasicNameValuePair("printerContent", snlist));

        CloseableHttpResponse response = null;
        String result = null;
        try {
            post.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
            response = httpClient.execute(post);
            int statecode = response.getStatusLine().getStatusCode();
            if (statecode == 200) {
                HttpEntity httpentity = response.getEntity();
                if (httpentity != null) {
                    result = EntityUtils.toString(httpentity);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                post.abort();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;

    }

    //方法1
    public static String print(String sn, String Ukey, String user, PrintBean printBean) {
        //标签说明：
        //单标签:
        //"<BR>"为换行,"<CUT>"为切刀指令(主动切纸,仅限切刀打印机使用才有效果)
        //"<LOGO>"为打印LOGO指令(前提是预先在机器内置LOGO图片),"<PLUGIN>"为钱箱或者外置音响指令
        //成对标签：
        //"<CB></CB>"为居中放大一倍,"<B></B>"为放大一倍,"<C></C>"为居中,<L></L>字体变高一倍
        //<W></W>字体变宽一倍,"<QR></QR>"为二维码,"<BOLD></BOLD>"为字体加粗,"<RIGHT></RIGHT>"为右对齐
        //拼凑订单内容时可参考如下格式
        //根据打印纸张的宽度，自行调整内容的格式，可参考下面的样例格式
        String orderType = printBean.getOrderType() == 1 ? "配送单" : "自提单";
        String content;
        content = "<CB>" + printBean.getStorageName() + "</CB><BR>";
        content += "<CB>" + orderType + "</CB><BR>";
        content += "<BOLD>订单号：" + printBean.getOrderNo() + "</BOLD><BR>";
        content += "名称　　　　　 单价  数量 金额<BR>";
        content += "--------------------------------<BR>";

        List<PrintItemBean> orderParamList = printBean.getItemBeans();

        double totals = 0.0;
        int b1 = 14;
        int b2 = 6;
        int b3 = 3;
        int b4 = 6;
        for (int i = 0; i < orderParamList.size(); i++) {
            String title = orderParamList.get(i).getName();
            String price = orderParamList.get(i).getPrice().toString();
            String num = orderParamList.get(i).getNum().toString();
            String total = "" + Double.parseDouble(price) * Integer.parseInt(num);
            totals += Double.parseDouble(total);
            price = addSpace(price, b2);
            num = addSpace(num, b3);
            total = addSpace(total, b4);
            String otherStr = " " + price + num + " " + total;

            int tl = 0;
            try {
                tl = title.getBytes("GBK").length;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            int spaceNum = (tl / b1 + 1) * b1 - tl;
            if (tl < b1) {
                for (int k = 0; k < spaceNum; k++) {
                    title += " ";
                }
                title += otherStr;
            } else if (tl == b1) {
                title += otherStr;
            } else {
                List<String> list = null;
                if (isEn(title)) {
                    list = getStrList(title, b1);
                } else {
                    list = getStrList(title, b1 / 2);
                }
                String s0 = titleAddSpace(list.get(0));
                title = s0 + otherStr + "<BR>";// 添加 单价 数量 总额
                String s = "";
                for (int k = 1; k < list.size(); k++) {
                    s += list.get(k);
                }
                try {
                    s = getStringByEnter(b1, s);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                title += s;
            }
            content += title + "<BR>";
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));

        content += "--------------------------------<BR>";
        content += "合计：" + printBean.getTotalMoney().toString() + "元<BR>";
        content += "--------------------------------<BR>";
        content += "送货地点：" + printBean.getAddress() + "<BR>";
        content += "联系姓名：" + chineseName(printBean.getName()) + "<BR>";
        content += "联系电话：" + printBean.getPhone() + "<BR>";
        content += "订单时间：" + sdf.format(printBean.getOrderTime()) + "<BR>";
        String contentTemp = "送达时间：" + printBean.getPredictTime() + "<BR>";
        if (printBean.getOrderType() == 2) {
            contentTemp = contentTemp.replaceAll("送达", "自提");
        }
        content += contentTemp;
        content += "--------------------------------<BR>";
        content += "备注：" + printBean.getRemark() + "<BR>";
        //通过POST请求，发送打印信息到服务器
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(30000)//读取超时
                .setConnectTimeout(30000)//连接超时
                .build();

        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .build();

        HttpPost post = new HttpPost(URL);
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("user", user));
        String STIME = String.valueOf(System.currentTimeMillis() / 1000);
        nvps.add(new BasicNameValuePair("stime", STIME));
        nvps.add(new BasicNameValuePair("sig", signature(user, Ukey, STIME)));
        nvps.add(new BasicNameValuePair("apiname", "Open_printMsg"));//固定值,不需要修改
        nvps.add(new BasicNameValuePair("sn", sn));
        nvps.add(new BasicNameValuePair("content", content));
        nvps.add(new BasicNameValuePair("times", "1"));//打印联数

        CloseableHttpResponse response = null;
        String result = null;
        try {
            post.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
            response = httpClient.execute(post);
            int statecode = response.getStatusLine().getStatusCode();
            if (statecode == 200) {
                HttpEntity httpentity = response.getEntity();
                if (httpentity != null) {
                    //服务器返回的JSON字符串，建议要当做日志记录起来
                    result = EntityUtils.toString(httpentity);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                post.abort();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;

    }


    public static String getStringByEnter(int length, String string) throws Exception {
        for (int i = 1; i <= string.length(); i++) {
            if (string.substring(0, i).getBytes("GBK").length > length) {
                return string.substring(0, i - 1) + "<BR>" + getStringByEnter(length, string.substring(i - 1));
            }
        }
        return string;
    }

    public static String addSpace(String str, int size) {
        int len = str.length();
        if (len < size) {
            for (int i = 0; i < size - len; i++) {
                str += " ";
            }
        }
        return str;
    }

    public static Boolean isEn(String str) {
        Boolean b = false;
        try {
            b = str.getBytes("GBK").length == str.length();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return b;
    }

    public static List<String> getStrList(String inputString, int length) {
        int size = inputString.length() / length;
        if (inputString.length() % length != 0) {
            size += 1;
        }
        return getStrList(inputString, length, size);
    }

    public static List<String> getStrList(String inputString, int length, int size) {
        List<String> list = new ArrayList<String>();
        for (int index = 0; index < size; index++) {
            String childStr = substring(inputString, index * length, (index + 1) * length);
            list.add(childStr);
        }
        return list;
    }

    public static String substring(String str, int f, int t) {
        if (f > str.length()) {
            return null;
        }
        if (t > str.length()) {
            return str.substring(f, str.length());
        } else {
            return str.substring(f, t);
        }
    }

    public static String titleAddSpace(String str) {
        int k = 0;
        int b = 14;
        try {
            k = str.getBytes("GBK").length;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < b - k; i++) {
            str += " ";
        }
        return str;
    }

    /**
     * [中文姓名] 只显示第一个汉字，其他隐藏为星号<例子：李**>
     *
     * @param fullName 姓名
     * @return
     */
    public static String chineseName(String fullName) {
        if (StringUtils.isBlank(fullName)) {
            return "";
        }
        String name = StringUtils.left(fullName, 1);
        return StringUtils.rightPad(name, StringUtils.length(fullName), "*");
    }

    //方法1
    public static String printTset(String sn, String Ukey, String user) {
        //标签说明：
        //单标签:
        //"<BR>"为换行,"<CUT>"为切刀指令(主动切纸,仅限切刀打印机使用才有效果)
        //"<LOGO>"为打印LOGO指令(前提是预先在机器内置LOGO图片),"<PLUGIN>"为钱箱或者外置音响指令
        //成对标签：
        //"<CB></CB>"为居中放大一倍,"<B></B>"为放大一倍,"<C></C>"为居中,<L></L>字体变高一倍
        //<W></W>字体变宽一倍,"<QR></QR>"为二维码,"<BOLD></BOLD>"为字体加粗,"<RIGHT></RIGHT>"为右对齐
        //拼凑订单内容时可参考如下格式
        //根据打印纸张的宽度，自行调整内容的格式，可参考下面的样例格式

        String content;
        content = "<CB>测试打印</CB><BR>";
        content += "名称　　　　　 单价  数量 金额<BR>";
        content += "--------------------------------<BR>";
        content += "饭　　　　　　 1.0    1   1.0<BR>";
        content += "炒饭　　　　　 10.0   10  10.0<BR>";
        content += "蛋炒饭　　　　 10.0   10  100.0<BR>";
        content += "鸡蛋炒饭　　　 100.0  1   100.0<BR>";
        content += "番茄蛋炒饭　　 1000.0 1   100.0<BR>";
        content += "西红柿蛋炒饭　 1000.0 1   100.0<BR>";
        content += "西红柿鸡蛋炒饭 100.0  10  100.0<BR>";
        content += "西红柿鸡蛋炒饭饭饭饭饭饭饭饭饭饭饭饭饭饭饭饭饭饭饭饭饭饭饭饭饭饭饭饭饭 100.0  10  100.0<BR>";
        content += "备注：加辣<BR>";
        content += "--------------------------------<BR>";
        content += "合计：xx.0元<BR>";
        content += "送货地点：广州市南沙区xx路xx号<BR>";
        content += "联系电话：13888888888888<BR>";
        content += "订餐时间：2016-08-08 08:08:08<BR>";

        //通过POST请求，发送打印信息到服务器
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(30000)//读取超时
                .setConnectTimeout(30000)//连接超时
                .build();

        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .build();

        HttpPost post = new HttpPost(URL);
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("user", user));
        String STIME = String.valueOf(System.currentTimeMillis() / 1000);
        nvps.add(new BasicNameValuePair("stime", STIME));
        nvps.add(new BasicNameValuePair("sig", signature(user, Ukey, STIME)));
        nvps.add(new BasicNameValuePair("apiname", "Open_printMsg"));//固定值,不需要修改
        nvps.add(new BasicNameValuePair("sn", sn));
        nvps.add(new BasicNameValuePair("content", content));
        nvps.add(new BasicNameValuePair("times", "1"));//打印联数

        CloseableHttpResponse response = null;
        String result = null;
        try {
            post.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
            response = httpClient.execute(post);
            int statecode = response.getStatusLine().getStatusCode();
            if (statecode == 200) {
                HttpEntity httpentity = response.getEntity();
                if (httpentity != null) {
                    //服务器返回的JSON字符串，建议要当做日志记录起来
                    result = EntityUtils.toString(httpentity);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                post.abort();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;

    }


    //方法2
    private static String printLabelMsg(String sn) {

        String content;
        content = "<DIRECTION>1</DIRECTION>";//设定打印时出纸和打印字体的方向，n 0 或 1，每次设备重启后都会初始化为 0 值设置，1：正向出纸，0：反向出纸，
        content += "<TEXT x='9' y='10' font='12' w='1' h='2' r='0'>#001       五号桌      1/3</TEXT><TEXT x='80' y='80' font='12' w='2' h='2' r='0'>可乐鸡翅</TEXT><TEXT x='9' y='180' font='12' w='1' h='1' r='0'>张三先生       13800138000</TEXT>";//40mm宽度标签纸打印例子，打开注释调用标签打印接口打印

        //通过POST请求，发送打印信息到服务器
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(30000)//读取超时
                .setConnectTimeout(30000)//连接超时
                .build();

        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .build();

        HttpPost post = new HttpPost(URL);
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("user", USER));
        String STIME = String.valueOf(System.currentTimeMillis() / 1000);
        nvps.add(new BasicNameValuePair("stime", STIME));
        nvps.add(new BasicNameValuePair("sig", signature(USER, UKEY, STIME)));
        nvps.add(new BasicNameValuePair("apiname", "Open_printLabelMsg"));//固定值,不需要修改
        nvps.add(new BasicNameValuePair("sn", sn));
        nvps.add(new BasicNameValuePair("content", content));
        nvps.add(new BasicNameValuePair("times", "1"));//打印联数

        CloseableHttpResponse response = null;
        String result = null;
        try {
            post.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
            response = httpClient.execute(post);
            int statecode = response.getStatusLine().getStatusCode();
            if (statecode == 200) {
                HttpEntity httpentity = response.getEntity();
                if (httpentity != null) {
                    //服务器返回的JSON字符串，建议要当做日志记录起来
                    result = EntityUtils.toString(httpentity);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                post.abort();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;

    }


    //方法3
    private static String queryOrderState(String orderid) {

        //通过POST请求，发送打印信息到服务器
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(30000)//读取超时
                .setConnectTimeout(30000)//连接超时
                .build();

        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .build();

        HttpPost post = new HttpPost(URL);
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("user", USER));
        String STIME = String.valueOf(System.currentTimeMillis() / 1000);
        nvps.add(new BasicNameValuePair("stime", STIME));
        nvps.add(new BasicNameValuePair("sig", signature(USER, UKEY, STIME)));
        nvps.add(new BasicNameValuePair("apiname", "Open_queryOrderState"));//固定值,不需要修改
        nvps.add(new BasicNameValuePair("orderid", orderid));

        CloseableHttpResponse response = null;
        String result = null;
        try {
            post.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
            response = httpClient.execute(post);
            int statecode = response.getStatusLine().getStatusCode();
            if (statecode == 200) {
                HttpEntity httpentity = response.getEntity();
                if (httpentity != null) {
                    //服务器返回
                    result = EntityUtils.toString(httpentity);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                post.abort();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;

    }


    //方法4
    private static String queryOrderInfoByDate(String sn, String strdate) {

        //通过POST请求，发送打印信息到服务器
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(30000)//读取超时
                .setConnectTimeout(30000)//连接超时
                .build();

        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .build();

        HttpPost post = new HttpPost(URL);
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("user", USER));
        String STIME = String.valueOf(System.currentTimeMillis() / 1000);
        nvps.add(new BasicNameValuePair("stime", STIME));
        nvps.add(new BasicNameValuePair("sig", signature(USER, UKEY, STIME)));
        nvps.add(new BasicNameValuePair("apiname", "Open_queryOrderInfoByDate"));//固定值,不需要修改
        nvps.add(new BasicNameValuePair("sn", sn));
        nvps.add(new BasicNameValuePair("date", strdate));//yyyy-MM-dd格式

        CloseableHttpResponse response = null;
        String result = null;
        try {
            post.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
            response = httpClient.execute(post);
            int statecode = response.getStatusLine().getStatusCode();
            if (statecode == 200) {
                HttpEntity httpentity = response.getEntity();
                if (httpentity != null) {
                    //服务器返回
                    result = EntityUtils.toString(httpentity);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                post.abort();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;

    }


    //方法5
    private static String queryPrinterStatus(String sn) {

        //通过POST请求，发送打印信息到服务器
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(30000)//读取超时
                .setConnectTimeout(30000)//连接超时
                .build();

        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .build();

        HttpPost post = new HttpPost(URL);
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("user", USER));
        String STIME = String.valueOf(System.currentTimeMillis() / 1000);
        nvps.add(new BasicNameValuePair("stime", STIME));
        nvps.add(new BasicNameValuePair("sig", signature(USER, UKEY, STIME)));
        nvps.add(new BasicNameValuePair("apiname", "Open_queryPrinterStatus"));//固定值,不需要修改
        nvps.add(new BasicNameValuePair("sn", sn));

        CloseableHttpResponse response = null;
        String result = null;
        try {
            post.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
            response = httpClient.execute(post);
            int statecode = response.getStatusLine().getStatusCode();
            if (statecode == 200) {
                HttpEntity httpentity = response.getEntity();
                if (httpentity != null) {
                    //服务器返回
                    result = EntityUtils.toString(httpentity);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                post.abort();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;

    }

    //生成签名字符串
    private static String signature(String USER, String UKEY, String STIME) {
        String s = DigestUtils.sha1Hex(USER + UKEY + STIME);
        return s;
    }
}
