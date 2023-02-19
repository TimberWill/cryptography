package com.whl.dpabse;

import it.unisa.dia.gas.jpbc.Element;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者：whl
 * 日期：2023-01-29 23:33
 * 描述：6关键词 4属性
 */
public class Test6 {
    final static String[] userList = {"ECNU","post_graduate","information_security","doctor","AI","FL","FuDan","TongJi","SJTU"};//大属性域

    final static String[] attributeList = {"ECNU","post_graduate","information_security","doctor"};//用户属性域
    final static String accessPolicy = "ECNU and (post_graduate or doctor) and information_security";//访问策略表达式

    final static String[] keywordValue_u = {"female","162","55","cs","ECNU","SH"};//大关键字值域

    final static String keyword_accessPolicy = "sex and height and weight and major and school and location";//访问策略表达式
    final static String[] keywordList = {"sex","height","weight","major","school","location"};//用户关键字集
    final static String[] keywordValue_s = {"female","162","55","cs","ECNU","SH"};//用户关键字域

    static long oe_time = 0;
    static long token_time = 0;
    static long ud_time = 0;

    public static void main(String[] args) {
        for (int i = 0; i < 1000; i++) {
            Par par = new Par();
            Msk msk = new Msk();

            AA aa = pythonTest(accessPolicy, attributeList);//用户访问结构

            AA bb = pythonTest(keyword_accessPolicy, keywordList);//关键字访问结构

            System.out.println("-----------开始 set up-------------------");
            DPABSE.setup(par,msk);
            System.out.println("-----------结束 set up-------------------");

            System.out.println("-----------开始 key gen-------------------");
            SK sk = DPABSE.keyGen(par, msk, attributeList);
            System.out.println("-----------结束 key gen-------------------");

            System.out.println("-----------开始 offline enc-------------------");
            IC ic = DPABSE.offlineEnc(par,userList);
            System.out.println("-----------结束 offline enc-------------------");


            System.out.println("-----------开始 online enc-------------------");
            Element file = par.getP().getGT().newRandomElement().getImmutable();//待加密文件
            long starttime1 = System.currentTimeMillis();
            CTIW ctiw = DPABSE.onlineEnc(par, ic, aa, file, keywordValue_u);
            long endtime1 = System.currentTimeMillis();
            oe_time = oe_time + (endtime1-starttime1);
            System.out.println("在线加密运行时间："+ (endtime1-starttime1) + "ms");
            CTf ctf = ctiw.getCtf();//文件密文
            Iw iw = ctiw.getIw();//关键字索引密文
            System.out.println("-----------结束 online enc-------------------");

            System.out.println("-----------开始 trapdoor-------------------");
            long starttime2 = System.currentTimeMillis();
            Token token = DPABSE.trapdoor(par, msk, sk, bb, keywordValue_s);
            long endtime2 = System.currentTimeMillis();
            token_time = token_time + (endtime2-starttime2);
            System.out.println("陷门生成运行时间："+ (endtime2-starttime2) + "ms");
            System.out.println("-----------结束 trapdoor-------------------");

            System.out.println("-----------开始 search-------------------");
            CTf search = DPABSE.search(par, token, iw);
            System.out.println("-----------结束 search-------------------");

            System.out.println("-----------开始 transform-------------------");
            Element tau = par.getP().getZr().newRandomElement().getImmutable();
            Kfog kfog = DPABSE.transform(par, sk, tau);
            System.out.println("-----------结束 transform-------------------");

            System.out.println("-----------开始 FN dec-------------------");
            PCT pct = DPABSE.fnDec(par, kfog, ctf);
            System.out.println("-----------结束 FN dec-------------------");

            System.out.println("-----------开始 user dec-------------------");
            long starttime3 = System.currentTimeMillis();
            Element element = DPABSE.userDec(tau, sk, pct);
            long endtime3 = System.currentTimeMillis();
            ud_time = ud_time + (endtime3-starttime3);
            System.out.println("用户解密运行时间："+ (endtime3-starttime3) + "ms");
            //判断
            if (element.isEqual(file)){
                System.out.println("找到文件");
            }else {
                System.out.println("没有找到文件");
            }
            System.out.println("-----------结束 user dec-------------------");
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
