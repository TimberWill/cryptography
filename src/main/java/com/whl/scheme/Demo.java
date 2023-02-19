package com.whl.scheme;

import com.whl.policy.AA;
import it.unisa.dia.gas.jpbc.Element;
import jdk.nashorn.internal.parser.Token;

import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 作者：whl
 * 日期：2023-01-05 12:28
 * 描述：
 */
public class Demo {
    //大属性域
    final static String[] u={"ECNU","teacher", "doctor","master","bachelor","2016","2015","2014"};
    //用户属性
    final static String[] s={"ECNU","teacher","2015"};
    //用户访问策略
    final static String accessPolicy = "ECNU and teacher";
    //用户关键字集合
    final static Map<String,String> kw = new HashMap<String,String>(){
        {
            put("1","162");
            put("2","110");
            put("3","112");
        }
    };
    final static W k_w = new W(kw);
    //关键词搜索策略
    final static String searchPolicy = "1 and 2 or 3";


    public static void main(String[] args){
        //初始化参数
        MyabsePar par = new MyabsePar();
        MyabseMsk msk = new MyabseMsk();

        //1.setup-------------------------------------------------------------------
        System.out.println("-----start to setup-----");
        Myabse.setup(par,msk);
        System.out.println("-----end to setup-----");

        //2.keygen---------------------------------------------------------------------------
        System.out.println("-----start to keygen-----");
        MyabseSK sk = Myabse.keyGen(par, msk, s);
        System.out.println("-----end to keygen-----");

        //3.offlineEnc-----中间密文----------------------------------------------------
        System.out.println("-----start to offlineEnc-----");
        MyabseIC ic = Myabse.offlineEnc(par,u);
        System.out.println("-----end to offlineEnc-----");

        /////////////////////调用python/////////////////////////////////
        //生成用户访问策略 (A,rho)
        AA aa = Myabse.pythonTest(accessPolicy, s);
        Integer[][] M = aa.getMatrix();//矩阵M
        String[] rho = aa.getRho();

        //生成关键词访问策略 (N,fi)
        Object[] objects = k_w.getKW().keySet().toArray();//用户关键字名集合
        String[] names = new String[objects.length];
        System.arraycopy(objects,0,names,0,objects.length);

//        String[] names = kw.keySet().toArray(new String[1000]);

//        String[] values = k_w.getKW().values().toArray();
        Object[] objects1 = k_w.getKW().values().toArray();
        String[] values = new String[objects1.length];
        System.arraycopy(objects1,0,values,0,objects1.length);

        AA bb = Myabse.pythonTest(searchPolicy, names);
        /////////////////////调用python/////////////////////////////////

        //4.onlineEnc------------------------------------------------------------------
        System.out.println("-----start to onlineEnc-----");
        //待加密的文件
        Element file = par.p.getGT().newRandomElement().getImmutable();
        System.out.println("生成的文件明文：" + file);
        //计算在线加密开始时间
        long startTime = System.currentTimeMillis();
        MyabseCipher ct = Myabse.onlineEnc(par, ic, bb, file, k_w);
        //计算在线加密结束时间
        long endTime = System.currentTimeMillis();
        System.out.println("在线加密运行时间：" + (endTime-startTime) + "ms");
        CTf cTf = ct.getcTf();//加密原始文件
        Iw iw = ct.getIw();//加密的索引
        System.out.println("-----end to onlineEnc-----");

        //5.trapdoor------------------------------------------------------------------
        System.out.println("-----start to trapdoor-----");
        //计算陷门生成开始时间
        long startTime1 = System.currentTimeMillis();
        MyabseToken token = Myabse.trapdoor(par, msk, bb, values);
        //计算陷门生成结束时间
        long endTime1 = System.currentTimeMillis();
        System.out.println("陷门生成运行时间：" + (endTime1-startTime1) + "ms");
        System.out.println("-----end to trapdoor-----");

        //6.search----------------------------------------------------------------------
        System.out.println("-----start to search-----");
        Boolean search = Myabse.search(par, token, iw, searchPolicy);
        if (search){
            System.out.println("-----搜索成功--------");
        }else {
            System.out.println("-----搜索失败--------");
//            return;
        }
        System.out.println("-----end to search-----");

        //7.transform-------------------------------------------------------------------
        System.out.println("-----start to transform-----");
        //生成转换因子
        Element tau = par.p.getZr().newRandomElement();
        MyabseKfog kfog = Myabse.transform(par, sk, tau);
        System.out.println("-----end to transform-----");

        //8.fnDec-------------------------------------------------------------------
        System.out.println("-----start to fnDec-----");
        MyabsePCT pct = Myabse.fnDec(par, kfog, cTf, s, accessPolicy);
        System.out.println("-----end to fnDec-----");

        //9.userDec-------------------------------------------------------------------
        System.out.println("-----start to userDec-----");
        //计算用户解密开始时间
        long startTime2 = System.currentTimeMillis();
        Element plaintext = Myabse.userDec(par, sk, pct, ct, tau);
        //计算用户解密结束时间
        long endTime2 = System.currentTimeMillis();
        System.out.println("用户解密运行时间：" + (endTime2-startTime2) + "ms");
        System.out.println("解密得到的明文：" + plaintext);
        System.out.println("-----end to userDec-----");
    }
}
