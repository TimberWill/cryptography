package com.whl.emk;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者：whl
 * 日期：2023-01-29 22:57
 * 描述：3个关键字、4个属性
 */
public class Test2 {
    final static String[] userList = {"ECNU","post_graduate","information_security","doctor","AI","FL","FuDan","TongJi","SJTU"};//大属性域
    final static String[] attributeList = {"ECNU","post_graduate","information_security","doctor"};//用户属性域
    final static String[] w = {"sex","height","weight","major","school","location"};//大关键字集合
    final static String[] w0 = {"sex","height","weight"};//用户搜索关键字集合
    final static String accessPolicy = "ECNU and (post_graduate or doctor) and information_security";//访问策略表达式
    static long oe_time = 0;
    static long token_time = 0;
    static long ud_time = 0;

    public static void main(String[] args) {
        for (int i = 0; i < 1000; i++) {
            GP gp = new GP();
            MSK msk = new MSK();
            Pairing bp = PairingFactory.getPairing("a.properties");

            //待加密明文
            Element file = bp.getGT().newRandomElement();
            //调用python生成访问策略
            AA aa = pythonTest(accessPolicy, attributeList);

            System.out.println("-----------start set up 初始化---------------");
            EMKABSE.setup(gp,msk,userList);
            System.out.println("-----------end set up 初始化---------------");

            System.out.println("-----------start key gen 密钥生成---------------");
            SK sk = EMKABSE.kenGen(gp, msk, attributeList);
            System.out.println("-----------end key gen 密钥生成---------------");

            System.out.println("-----------start offline enc 离线加密---------------");
            IC ic = EMKABSE.offlineEnc(gp);
            System.out.println("-----------end offline enc 离线加密---------------");

            System.out.println("-----------start online enc 在线加密---------------");
            long starttime1 = System.currentTimeMillis();//在线加密开始时间
            IWCT iwct = EMKABSE.onlineEnc(gp, ic, aa, file, w);
            long endtime1 = System.currentTimeMillis();//在线加密结束时间
            oe_time = oe_time + (endtime1-starttime1);
            System.out.println("在线加密运行时间："+ (endtime1-starttime1) + "ms");

            CT ct = iwct.getCt();//文件加密的密文
            IW iw = iwct.getIw();//关键字索引加密的密文
            System.out.println("-----------end online enc 在线加密---------------");

            System.out.println("-----------start trapdoor 陷门生成---------------");
            long starttime2 = System.currentTimeMillis();//陷门生成开始时间
            Tq tq = EMKABSE.trapDoor(gp, sk, w0);
            long endtime2 = System.currentTimeMillis();//陷门生成结束时间
            token_time = token_time + (endtime2-starttime2);
            System.out.println("陷门生成运行时间：" + (endtime2-starttime2) + "ms");
            System.out.println("-----------end trapdoor 陷门生成---------------");

            System.out.println("-----------start search 搜索---------------");
            CT search = EMKABSE.search(gp, tq, iw);
            System.out.println("-----------end search 搜索---------------");

            System.out.println("-----------start transform 转换密钥---------------");
            //随机数tau
            Element tau = bp.getZr().newRandomElement();
            //k_tau
            Element k_tau = gp.getG().powZn(tau);
            KI ki = EMKABSE.transform(gp, sk,k_tau);
            System.out.println("-----------end transform 转换密钥---------------");

            System.out.println("-----------start en dec 边缘解密---------------");
            PCT pct = EMKABSE.enDec(gp, ki, ct);
            System.out.println("-----------end en dec 边缘解密---------------");

            System.out.println("-----------start user dec 用户解密---------------");
            long starttime3 = System.currentTimeMillis();//用户解密开始时间
            Element f = EMKABSE.userDec(sk, pct, k_tau);
            long endtime3 = System.currentTimeMillis();//用户解密结束时间
            ud_time = ud_time + (endtime3-starttime3);
            System.out.println("用户解密运行时间："+ (endtime3-starttime3) + "ms");
            System.out.println("-----------end user dec 用户解密---------------");
            if (file.isEqual(f)){
                System.out.println("找到文件");
            }else{
                System.out.println("没找到文件");
            }
        }

        System.out.println("平均在线加密时间：" + (oe_time/1000));
        System.out.println("平均陷门生成时间：" + (token_time/1000));
        System.out.println("平均用户解密时间：" + (ud_time/1000));
    }

    private static String arr2Str(String[] origin){
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < origin.length; i++) {
            if(i != origin.length - 1)
                sb.append(origin[i]).append(",");
            else
                sb.append(origin[i]);
        }
        return sb.toString();
    }

    /**
     * 调用python方法
     */
    public static AA pythonTest(String policy, String[] userAttributes){
        AA aa = new AA();
        Process proc;
        try {

            String s = arr2Str(userAttributes);
            String[] args1 = new String[] {"D:\\software\\py3.11.0\\python.exe", "D:\\ECNU\\2022_draft\\code\\Python\\Linear-Secret-Sharing-Scheme-In-Python-main\\lsss.py", policy, s};

            proc = Runtime.getRuntime().exec(args1);
            int res = proc.waitFor();
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line = null;
            //回参数组
            ArrayList<String> strL = new ArrayList<>();
            while ((line = in.readLine()) != null) {
                strL.add(line);
//                System.out.println(line);
//                System.out.println("--------------------------");
            }
            Object[] objects = strL.toArray();
            String temp = null;
            List<String> tempMatrixArr = new ArrayList<>();
            List<String> tempRhoArr = new ArrayList<>();
            List<String> tempAttr_pathArr = new ArrayList<>();
            List<String> tempOmegaArr = new ArrayList<>();
            for (int i = 0; i < objects.length; i++) {
                if("matrix: ".equals(objects[i])){
                    temp = "matrix: ";
                    continue;
                }
                if("matrix: ".equals(temp) && !"rho: ".equals(objects[i])){
                    tempMatrixArr.add((String) objects[i]);
                }
                if("rho: ".equals(objects[i])){
                    temp = "rho: ";
                    continue;
                }
                if("rho: ".equals(temp) && !"attr_path: ".equals(objects[i])){
                    tempRhoArr.add((String) objects[i]);
                }
                if("attr_path: ".equals(objects[i])){
                    temp = "attr_path: ";
                    continue;
                }
                if("attr_path: ".equals(temp) && !"omega: ".equals(objects[i])){
                    tempAttr_pathArr.add((String) objects[i]);
                }
                if("omega: ".equals(objects[i])){
                    temp = "omega: ";
                    continue;
                }
                if("omega: ".equals(temp)){
                    tempOmegaArr.add((String) objects[i]);
                }
            }

            Integer[][] matrixIntDlArr = strArr2IntDlArr(tempMatrixArr.toArray());
            String[] rhoArr = tempRhoArr.toArray(new String[0]);
            Integer[] attr_pathIntArr = strArr2IntArr(tempAttr_pathArr);
            Integer[] omegaIntArr = strArr2IntArr(tempOmegaArr);

            //包装aa
            aa.setMatrix(matrixIntDlArr);
            aa.setRho(rhoArr);
            aa.setAttr_path(attr_pathIntArr);
            aa.setOmega(omegaIntArr);


        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return aa;
    }

    private static Integer[] strArr2IntArr(List<String> strArr) {
        Integer[] res = new Integer[strArr.size()];
        int idx = 0;
        for (String s : strArr) {
            res[idx] = Integer.valueOf(s);
            idx++;
        }
        return res;
    }


    public static Integer[][]  strArr2IntDlArr(Object[] matrixStrArr){
        int i = 0;
        int length = matrixStrArr[0].toString().substring(1, getStrLen(matrixStrArr[0].toString()) - 1).split(",").length;
        Integer[][] matrixIntArr = new Integer[matrixStrArr.length][length];
        for (Object s : matrixStrArr) {
            String substring = s.toString().substring(1, getStrLen(s.toString()) - 1);
            String[] split = substring.split(", ");
            ArrayList<String> strings = new ArrayList<>();
            for (String s1 : split) {
                String substring1 = s1.substring(1, getStrLen(s1) - 1);
                strings.add(substring1);
            }

            Object[] objects = strings.toArray();
            for (int j = 0; j < objects.length; j++) {
                matrixIntArr[i][j] = Integer.parseInt((String) objects[j]);
            }
            i++;

        }

        return matrixIntArr;
    }

    public static int getStrLen(String str){
        char[] chars = str.toCharArray();
        return chars.length;
    }

}
