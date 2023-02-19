package com.whl.emk;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者：whl
 * 日期：2023-01-14 16:26
 * 描述：
 */
public class EMKABSE {

    /**
     * 初始化阶段
     * @param gp
     * @param msk
     */
    public static void setup(GP gp, MSK msk, String[] userList){
        System.out.println("-----------setup phase----------");

        Pairing bp = PairingFactory.getPairing("a.properties");
        gp.setP(bp);
//        System.out.println("set up bp:" + bp);

        //GP参数构造
        Field G = bp.getG1();
        Field Zr = bp.getZr();
        Field GT = bp.getGT();

        //随机数
        Element alpha = Zr.newRandomElement().getImmutable();//α
        Element beta = Zr.newRandomElement().getImmutable();//beta
        Element mu = Zr.newRandomElement().getImmutable();//mu

        Element g = G.newRandomElement().getImmutable();//G
        //e(g,g)^alpha
        Element egg = bp.pairing(g, g).getImmutable();
        Element egg_alpha = egg.powZn(alpha).getImmutable();

        //g^beta
        Element g_beta = g.powZn(beta);
        //g^mu
        Element g_mu = g.powZn(mu);

        //根据属性域生成
        Map<String, Element> Ti = new HashMap<>();
        for (int i = 0; i < userList.length; i++) {
            Element ai = Zr.newRandomElement().getImmutable();//ai

            Element ti = g.powZn(ai);//Ti
            Ti.put(userList[i],ti);
        }

        //set方法
        gp.setTi(Ti);
        gp.setG_mu(g_mu);
        gp.setG(g);
        gp.setG_beta(g_beta);
        gp.setEgg_alpha(egg_alpha);
        gp.setG1(G);
        gp.setGT(GT);

        msk.setBeta(beta);
        msk.setAlpha(alpha);

    }

    /**
     * 密钥生成
     * @param gp
     * @param msk
     * @param s
     * @return
     */
    public static SK kenGen(GP gp, MSK msk,String[] s){
        SK sk = new SK();
        //随机数x
        Element x = gp.p.getZr().newRandomElement().getImmutable();
        //密钥K
        Element k = gp.getG_beta().powZn(x).getImmutable();
        //k1
        Element k1 = gp.getG().powZn(x).getImmutable();
        //k2
        Element k2 = msk.beta.mul(x.invert()).getImmutable();
        //k3
        Element k3 = gp.getG().powZn(msk.getAlpha()).getImmutable();
        //随机数y
        Element y = gp.p.getZr().newRandomElement().getImmutable();
        //k4
        Element k4 = k3.duplicate().mul(gp.getG_mu().powZn(y)).powZn(k2.invert()).getImmutable();
        //k5
        Element k50 = gp.getG().powZn(y);
        Element k5 = k50.powZn(k2.invert()).getImmutable();
        //ki
        HashMap<String, Element> ki = new HashMap<>();
        for (int i = 0; i < s.length; i++) {
            for (String s1 : gp.getTi().keySet()) {
                if (s[i].equals(s1)){
                    Element element = gp.getTi().get(s1).powZn(y);
                    Element kii = element.duplicate().powZn(k2.invert());
                    ki.put(s[i],kii);
                }

            }
        }

        sk.setKi(ki);
        sk.setK(k);
        sk.setK1(k1);
        sk.setK2(k2);
        sk.setK3(k3);
        sk.setK4(k4);
        sk.setK5(k5);

        return sk;
    }

    /**
     * 离线加密
     * @param gp
     * @return
     */
    public static IC offlineEnc(GP gp){
        IC ic = new IC();

        //随机数s
        Element s = gp.p.getZr().newRandomElement().getImmutable();
        //c0
        Element c0 = gp.getEgg_alpha().powZn(s).getImmutable();
        //c1
        Element c1 = gp.getG().powZn(s).getImmutable();
        //ci di
        Map<String, Element> ti = gp.getTi();
        Map<String, Element> ci = new HashMap<>();//ci
        HashMap<String, Element> di = new HashMap<>();//di
        for (Map.Entry<String, Element> stringElementEntry : ti.entrySet()) {
            //随机数ri
            Element ri = gp.p.getZr().newRandomElement().getImmutable();

            Element value = stringElementEntry.getValue();
            Element tii = value.powZn(ri.negate());
            ci.put(stringElementEntry.getKey(),tii);
            di.put(stringElementEntry.getKey(),gp.getG().powZn(ri));
        }

        ic.setS(s);
        ic.setC0(c0);
        ic.setC1(c1);
        ic.setCi(ci);
        ic.setDi(di);

        return ic;
    }

    /**
     * 在线加密
     * @param gp
     * @param ic
     * @param aa
     * @param f
     * @param w
     * @return
     */
    public static IWCT onlineEnc(GP gp,IC ic,AA aa, Element f, String[] w){
        Pairing pairing = gp.p;
        IWCT iwct = new IWCT();
        //ct
        CT ct = new CT();
        //iw
        IW iw = new IW();

        //向量v
        List<Element> v = new ArrayList<>();
        v.add(ic.s);
        for (int i = 1; i < aa.matrixGetN(); i++) {
            v.add(gp.p.getZr().newRandomElement().getImmutable());
        }

        //c
        Element c = ic.c0.duplicate().mul(f);
        //ci,di,ci'
        Map<Integer, Element> ci = new HashMap<>();
        Map<Integer, Element> di = new HashMap<>();
        Map<Integer, Element> ci_ = new HashMap<>();
        for (int i = 0; i < aa.matrixGetL(); i++) {
            //λ
            Element lambda_i = lambdaOrSigma_i(aa.matrixRow_i(i),v,pairing.getZr().newZeroElement(),pairing);
            Element cii = gp.getG_mu().duplicate().powZn(lambda_i);
            ci.put(i+1,cii);
            //ci'
            for (String s : ic.getCi().keySet()) {
                if (aa.getRho()[i].equals(s)){
                    ci_.put(i+1,ic.getCi().get(s).duplicate().mul(cii));
                    di.put(i+1,ic.getDi().get(s));
                }
            }
        }

        ct.setAa(aa);
        ct.setC(c);
        ct.setC1(ic.getC1());
        ct.setCi(ci_);
        ct.setDi(di);

        //iw
        int d = w.length;
        //随机数r r'
        Element r = pairing.getZr().newRandomElement().getImmutable();
        Element r_ = pairing.getZr().newRandomElement().getImmutable();

        //cwj
        Map<Integer, Element> cwj = new HashMap<>();
        int count = 1;//计数器
        for (String s : w) {
            byte[] bytes = s.getBytes();
            Element h = pairing.getZr().newElementFromHash(bytes, 0, bytes.length).getImmutable();
            Element cwji = gp.getG_beta().duplicate().powZn(h.duplicate().mul(r)).getImmutable();
            cwj.put(count,cwji);
            count++;
        }
        //cw1
        Element cw1 = gp.getG().duplicate().powZn(r_).getImmutable();
        //cw2
        Element cw2 = gp.getG_beta().duplicate().powZn(r_).getImmutable();
        //cw3
        Element cw3 = gp.getG_mu().duplicate().powZn(r).getImmutable();

        iw.setCwj(cwj);
        iw.setCw1(cw1);
        iw.setCw2(cw2);
        iw.setCw3(cw3);

        iwct.setIw(iw);
        iwct.setCt(ct);

        return iwct;
    }

    /**
     * 陷门生成
     * @param gp
     * @param sk
     * @param w_
     * @return
     */
    public static Tq trapDoor(GP gp, SK sk, String[] w_){
        Tq tq = new Tq();
        Pairing pairing = gp.p;
        int d_ = w_.length;
        //随机数q
        Element q = pairing.getZr().newRandomElement().getImmutable();
        //T1
        Element t1 = sk.getK().duplicate().powZn(q).getImmutable();
        //T2
        Element t2 = sk.getK1().duplicate().powZn(q).getImmutable();
        //T3
        Element t3 = pairing.getG1().newOneElement();
        for (String s : w_) {
            byte[] bytes = s.getBytes();
            Element h = pairing.getZr().newElementFromHash(bytes, 0, bytes.length).getImmutable();
            Element t3i = gp.getG_beta().duplicate().powZn(h);
            t3.mul(t3i);
        }
        //T4
        Element t4 = gp.getG_mu().duplicate().getImmutable();

        tq.setT1(t1);
        tq.setT2(t2);
        tq.setT3(t3);
        tq.setT4(t4);
        tq.setD_(d_);

        return tq;
    }

    /**
     * 搜索
     * @param gp
     * @param tq
     * @param iw
     * @return
     */
    public static CT search(GP gp, Tq tq, IW iw){
        CT ct = new CT();
        Pairing pairing = gp.p;

        Element pairing1 = pairing.pairing(tq.getT2(), iw.cw2).getImmutable();
        Element element2 = pairing.getG1().newOneElement();
        int count = 0;
        for (Integer key : iw.getCwj().keySet()) {
            Element val = iw.getCwj().get(key);
            element2.mul(val);
            count++;
            if (count == tq.d_){
                break;
            }
        }
        Element pairing2 = pairing.pairing(element2, tq.getT4());
        Element left = pairing1.mul(pairing2).getImmutable();
        Element pairing3 = pairing.pairing(tq.t1, iw.cw1).getImmutable();
        Element pairing4 = pairing.pairing(tq.t3, iw.cw3).getImmutable();
        Element right = pairing3.mul(pairing4).getImmutable();

        if (left.isEqual(right)){
            System.out.println("yes");
        }else {
            System.out.println("no");

        }

        return ct;
    }

    /**
     * 转换密钥
     * @param gp
     * @param sk
     * @return
     */
    public static KI transform(GP gp, SK sk,Element k_tau){
        KI ki = new KI();
        Pairing pairing = gp.p;
//        //随机数tau
//        Element tau = pairing.getZr().newRandomElement();
//        //k_tau
//        Element k_tau = gp.getG().powZn(tau);
        //k6
        Element k6 = sk.getK4().duplicate().mul(k_tau);
        //k5
        Element k5 = sk.getK5();
        //ki
        Map<String, Element> kii = sk.getKi();

        ki.setK5(k5);
        ki.setK6(k6);
        ki.setKi(kii);

        return ki;
    }

    /**
     * 边缘解密
     * @param gp
     * @param ki
     * @param ct
     * @return
     */
    public static PCT enDec(GP gp,KI ki,CT ct){
        PCT pct = new PCT();
        Pairing pairing = gp.p;

        Element top = pairing.pairing(ct.getC1(), ki.getK6());
        Integer[] omega = ct.getAa().getOmega();//wi
        Integer[] attr_path = ct.getAa().getAttr_path();//att path
        Element[] wi = new Element[omega.length];//转成element类型

        for (int i = 0; i < omega.length; i++) {
            Element e = pairing.getZr().newElement();
            switch (omega[i]){
                case 1:
                    e.setToOne();
                    break;
                case 0:
                    e.setToZero();
                    break;
                case -1:
                    e.setToOne().negate();
                    break;

            }
            wi[i] = e;
        }
        Element[] k_rho = new Element[attr_path.length];//kρ(i)
        String[] rho = ct.getAa().getRho();
        for (String s : ki.getKi().keySet()) {
            for (int i = 0; i < attr_path.length; i++) {
                if (rho[attr_path[i]].equals(s)){
                    k_rho[i] = ki.getKi().get(s).duplicate();
                }
            }
        }
        Element[] di = new Element[attr_path.length];

        Element down = pairing.getGT().newOneElement();
        Element[] ci = new Element[attr_path.length];//ci
        for (int i = 0; i < attr_path.length; i++) {
            Element cii = ct.getCi().get(attr_path[i] + 1);
            ci[i] = cii.duplicate();
            Element dii = ct.getDi().get(attr_path[i] + 1);
            di[i] = dii.duplicate();

            Element pairing1 = pairing.pairing(cii, ki.getK5());
            Element pairing2 = pairing.pairing(k_rho[i], dii);
            Element mul = pairing1.duplicate().mul(pairing2.duplicate());
            Element element = mul.duplicate().powZn(wi[i]);

            down.mul(element);

        }

        pct.setPct(top.duplicate().div(down));
        pct.setC(ct.getC());
        pct.setC1(ct.getC1());

        return pct;
    }

    /**
     * 用户解密
     * @param sk
     * @param pct
     * @param k_tau
     * @return
     */
    public static Element userDec(SK sk, PCT pct, Element k_tau){
        Pairing bp = PairingFactory.getPairing("a.properties");

        Element element1 = bp.pairing(pct.getC1(), k_tau).duplicate().powZn(sk.getK2());
        Element top = pct.getC().duplicate().mul(element1);

        Element down = pct.getPct().duplicate().powZn(sk.getK2());

        Element div = top.duplicate().div(down);

        return div;
    }

    //构造lamdai
    private static Element lambdaOrSigma_i(Integer[] M,List<Element> v,Element lambda_i,Pairing pairing){
        //健壮性 抛异常
        if (M.length != v.size()) throw new IllegalArgumentException("different length");
        if (lambda_i.isImmutable()) throw new IllegalArgumentException("immutable");
        //lambda_i初始化
        if (!lambda_i.isZero())
            lambda_i.setToZero();

        for (int i = 0; i < M.length; i++) {
            //将矩阵行中的元素转为element类型
            Element e = pairing.getZr().newElement();
            switch (M[i]){
                case 1:
                    e.setToOne();
                    break;
                case 0:
                    e.setToZero();
                    break;
            }
            lambda_i.add(e.duplicate().mul(v.get(i).getImmutable()));
        }
        return lambda_i.getImmutable();
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
