package com.whl.scheme;

import com.whl.policy.AA;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import java.io.*;
import java.util.*;


/**
 * 作者：whl
 * 日期：2022-12-11 18:27
 * 描述：
 */
public class Myabse {

    /**
     * 初始化阶段
     * @param par
     * @param msk
     */
    public static void setup(MyabsePar par, MyabseMsk msk){
        System.out.println("-----------setup phase----------");

        Pairing pairing = PairingFactory.getPairing("a.properties");


        par.p = pairing;

        //公共参数构造
        Element alpha,beta,d1,d2,d3,d4;

        //g1 g2定值
        par.g1 = pairing.getG1().newRandomElement().getImmutable();//g1
        par.g2 = pairing.getG2().newRandomElement();//g2
        par.g2.set(par.g1).getImmutable();//G2=G1=G
        //随机数定值（α β d1 d2 d3 d4）
        alpha = pairing.getZr().newRandomElement().getImmutable();//随机数α
        beta = pairing.getZr().newRandomElement().getImmutable();//随机数β
        d1 = pairing.getZr().newRandomElement().getImmutable();//随机数d1
        d2 = pairing.getZr().newRandomElement().getImmutable();//随机数d2
        d3 = pairing.getZr().newRandomElement().getImmutable();//随机数d3
        d4 = pairing.getZr().newRandomElement().getImmutable();//随机数d4

        //msk定值（α β d1 d2 d3 d4）
        msk.alpha = alpha.duplicate().getImmutable();
        msk.beta = beta.duplicate().getImmutable();
        msk.d1 = d1.duplicate().getImmutable();
        msk.d2 = d2.duplicate().getImmutable();
        msk.d3 = d3.duplicate().getImmutable();
        msk.d4 = d4.duplicate().getImmutable();

        //初始化（par的g_alpha g_beta g_d1 g_d2 g_d3 g_d4 ）
        par.g_alpha = pairing.getG1().newRandomElement();
        par.g_beta = pairing.getG1().newRandomElement();
        par.g_d1 = pairing.getG1().newRandomElement();
        par.g_d2 = pairing.getG1().newRandomElement();
        par.g_d3 = pairing.getG1().newRandomElement();
        par.g_d4 = pairing.getG1().newRandomElement();

        par.g_alpha = par.g1.duplicate();
        par.g_alpha = par.g_alpha.powZn(msk.alpha).getImmutable();//g^alpha定值
        par.g_beta = par.g1.duplicate();
        par.g_beta = par.g_beta.powZn(msk.beta).getImmutable();//g^beta定值

        par.g_d1 = par.g1.duplicate();
        par.g_d1 = par.g_d1.powZn(msk.d1).getImmutable();//g^d1定值
        par.g_d2 = par.g1.duplicate();
        par.g_d2 = par.g_d2.powZn(msk.d2).getImmutable();//g^d2定值
        par.g_d3 = par.g1.duplicate();
        par.g_d3 = par.g_d3.powZn(msk.d3).getImmutable();//g^d3定值
        par.g_d4 = par.g1.duplicate();
        par.g_d4 = par.g_d4.powZn(msk.d4).getImmutable();//g^d4定值

        par.e_gg_beta = pairing.getGT().newRandomElement();
        par.e_gg_beta = pairing.pairing(par.g1,par.g2).powZn(msk.beta).getImmutable();//e(g,g)^beta
        par.e_gg_d1d2alpha = pairing.getGT().newRandomElement();
        //did2alpha
        par.e_gg_d1d2alpha = pairing.pairing(par.g1,par.g2).powZn(msk.d1.mul(msk.d2).mul(msk.alpha)).getImmutable();//e(g,g)^d1d2alpha

    }

    /**
     * 密钥生成阶段
     * @param par
     * @param msk
     * @param S
     * @return
     */
    public static MyabseSK keyGen(MyabsePar par, MyabseMsk msk, String[] S) {
        System.out.println("------------key gen phase-----------------");
        MyabseSK sk = new MyabseSK();
        Pairing pairing = par.p;
        int n = S.length;

        Element[] elements = new Element[n];

        //随机数r 定值
        Element r =pairing.getZr().newRandomElement().getImmutable();

        //构造k1
        Element k11 = par.g_alpha.duplicate().powZn(r).getImmutable();
        Element k12 = pairing.getG1().newRandomElement().powZn(msk.beta).getImmutable();
        sk.setK1(k11.duplicate().mul(k12));

        //构造k2
        sk.setK2(par.g1.duplicate().powZn(r).getImmutable());

        //构造katt
        for (int i = 0; i < S.length; i++) {
            Element H = pairing.getG1().newElement();
            byte[] bytes = S[i].getBytes();
            H = H.setFromHash(bytes,0,bytes.length);
            elements[i] = H.duplicate().powZn(r).getImmutable();
        }
        sk.setKatt(elements);
        System.out.println("密钥生成后par msk不变");

        return sk;

    }

    /**
     * 离线加密阶段
     * @param par
     * @return
     */
    public static MyabseIC offlineEnc(MyabsePar par,String[] u){
        System.out.println("-----------offline enc phase--------------");
        MyabseIC IC = new MyabseIC();

        Pairing pairing = par.p;
        //随机数 秘密wo
        Element w0 = pairing.getZr().newRandomElement().getImmutable();
        IC.setWo(w0);

        //C0
        Element c0 = par.e_gg_beta.duplicate().powZn(IC.wo).getImmutable();
        IC.setC0(c0);
        //C1
        Element c1 = par.g1.duplicate().powZn(IC.wo).getImmutable();
        IC.setC1(c1);

        //Ci1 Ci2
        Map<String, Element> tempMap = new HashMap<>();
        Element[] elements1 = new Element[u.length];//ci1
        Element[] elements2 = new Element[u.length];//ci2
        for (int i = 0; i < u.length; i++) {
            //随机数ti
            Element ti = pairing.getZr().newRandomElement().getImmutable();
            //ci1
            Element m = pairing.getG1().newRandomElement();
            byte[] ind = u[i].getBytes();
            m = m.setFromHash(ind,0,ind.length);
            elements1[i] = m.duplicate().powZn(ti.duplicate().negate()).getImmutable();
            //ci2
            tempMap.put(u[i], elements1[i]);
            elements2[i] = pairing.getG1().newRandomElement().powZn(ti).getImmutable();
        }
        IC.setTemp(tempMap);
        IC.setCi1(elements1);
        IC.setCi2(elements2);


        return IC;

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
    public static AA pythonTest(String policy,String[] userAttributes){
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


    /**
     * 在线加密阶段
     * @param par
     * @param IC
     * @param as
     * @param file
     * @param kw
     * @return
     */
    public static MyabseCipher onlineEnc(MyabsePar par, MyabseIC IC, AA as, Element file, W kw) {
        System.out.println("------------online enc phase--------------");
        MyabseCipher cipher = new MyabseCipher();
        CTf cTf = new CTf();
        Iw iw = new Iw();
        //1.文件加密-------------------------------------------------------------------------------
        Pairing pairing = par.p;

        //构造列向量v
        //取随机数v2,...,vn
        List<Element> v = new ArrayList<>();
        v.add(IC.wo);
        for (int i = 1; i < as.matrixGetN(); i++) {
            v.add(pairing.getZr().newRandomElement().getImmutable());
        }
        //密文构造(C,Ci1,Ci2)
        Element c = file.duplicate().mul(IC.getC0()).getImmutable();
        cTf.setC(c);
        //构造Ci1
        Element[] temp = new Element[as.matrixGetL()];
        Integer[][] M = as.getMatrix();
        cTf.setM(M);
        cTf.setRho(as.getRho());
        for (int i = 0; i < as.matrixGetL(); i++) {
            //得到λi
            Element lambda_i = lambdaOrSigma_i(as.matrixRow_i(i), v, pairing.getZr().newZeroElement(), pairing).getImmutable();

            Element ci1c1 = par.g_alpha.duplicate().powZn(lambda_i).getImmutable();

//            Element[] res = new Element[as.matrixGetL()];
            Map<String, Element> tempMap = IC.getTemp();
//            Element element = tempMap.get(as.getRho()[i]);
            Element element = pairing.getG1().newElement();//获取ci1的hash部分
            //判断key是否相等，再取对应value
            for (String key : tempMap.keySet()) {
                if (key.equals(as.getRho()[i])) {
                    element = tempMap.get(key).getImmutable();
                }
            }
//            res[i] = element.getImmutable();

            Element element1 = ci1c1.mul(element);
            temp[i] = element1.duplicate().getImmutable();
        }

        cTf.setCi1(temp);
        cTf.setCi2(IC.getCi2());
        cTf.setC1(IC.c1);

        //2.关键字加密-------------------------------------------------------------------------
        Set<String> strings = kw.getKW().keySet();//获取kw中的所有key
        Object[] objects = strings.toArray();//所有的关键字名称转为数组存储
        String[] names = new String[objects.length];
        System.arraycopy(objects,0,names,0,objects.length);
        iw.setKeyname(names);//关键字名集合

        Collection<String> values = kw.getKW().values();//获取kw中的所有val
        Object[] objects1 = values.toArray();//所有的关键字值转为数组存储
        String[] vals = new String[objects1.length];
        System.arraycopy(objects1,0,vals,0,objects1.length);

        int d = values.size();;
        //随机数r、r1、r2
        Element r =pairing.getZr().newRandomElement().getImmutable();
        Element r1 =pairing.getZr().newRandomElement().getImmutable();
        Element r2 =pairing.getZr().newRandomElement().getImmutable();

        //Cwj
        Element[] temp2 = new Element[d];
        for (int i = 0; i < d; i++) {
            //构造cwj
            byte[] bytes = vals[i].getBytes();
            Element w = pairing.getG1().newRandomElement();
            w = w.setFromHash(bytes,0,bytes.length);

            temp2[i] = w.duplicate().powZn(r).getImmutable();
        }
        iw.setCwj(temp2);

        //构造cw0
        iw.setCw0(par.e_gg_d1d2alpha.duplicate().powZn(r));

        //构造cw1
        iw.setCw1(par.g_d1.duplicate().powZn(r.duplicate().add(r1.duplicate().negate())));

        //构造cw2
        iw.setCw2(par.g_d2.duplicate().powZn(r1));

        //构造cw3
        iw.setCw3(par.g_d3.duplicate().powZn(r.duplicate().add(r2.negate())));

        //构造cw4
        iw.setCw4(par.g_d4.duplicate().powZn(r2));

        cipher.setcTf(cTf);
        cipher.setIw(iw);
        return cipher;
    }



    /**
     * 陷门生成阶段
     * @param par
     * @param msk
     * @param as
     * @param w-----关键字值
     * @return
     */
    public static MyabseToken trapdoor(MyabsePar par,MyabseMsk msk, AA as, String[] w){
        System.out.println("-------------trapdoor gen phase------------");
        MyabseToken token = new MyabseToken();
        Pairing pairing = par.p;
        //向量u
        List<Element> u = new ArrayList<>();
        u.add(msk.alpha);
        for (int i = 1; i < as.matrixGetN(); i++) {
            u.add(pairing.getZr().newRandomElement().getImmutable());
        }

        //构造随机数r' r''
        Element r_ = pairing.getZr().newRandomElement().getImmutable();
        Element r00 = pairing.getZr().newRandomElement().getImmutable();

        //构造陷门组件 T
        Element add1 = r_.mul(msk.d1.mul(msk.d2));
        Element add2 = r00.mul(msk.d3.mul(msk.d4));
        Element t = par.g1.powZn(add1.duplicate().add(add2.duplicate())).getImmutable();

        token.setT(t);
        //构造sigma_i------policy.getN()与w的长度相等
        Element[] ti1 = new Element[as.matrixGetL()];//ti1
        Element[] ti2 = new Element[as.matrixGetL()];//ti2
        Element[] ti3 = new Element[as.matrixGetL()];//ti3
        Element[] ti4 = new Element[as.matrixGetL()];//ti4
        for (int i = 0; i < as.matrixGetL(); i++){
            Element sigma_i = lambdaOrSigma_i(as.matrixRow_i(i), u, pairing.getZr().newZeroElement(), pairing).getImmutable();
            //构造hash
            Element h2 = pairing.getG1().newElement();
            byte[] bytes = w[i].getBytes();
            h2 = h2.setFromHash(bytes,0,bytes.length);
            //构造陷门组件Ti1 Ti2 Ti3 Ti4
            //Ti1
            Element element = par.g_d2.powZn(sigma_i).mul(h2.duplicate().powZn(msk.d2.mul(r_.duplicate().negate()))).getImmutable();
            ti1[i] = element.duplicate().getImmutable();
            //Ti2
            Element element1 = par.g_d1.powZn(sigma_i).mul(h2.powZn(msk.d1.mul(r_.duplicate().negate()))).getImmutable();
            ti2[i] = element1.duplicate().getImmutable();
            //Ti3
            Element element2 = h2.powZn(msk.d4.mul(r00.duplicate().negate())).getImmutable();
            ti3[i] = element2.duplicate().getImmutable();
            //Ti4
            Element element3 = h2.powZn(msk.d3.mul(r00.duplicate().negate())).getImmutable();
            ti4[i] = element3.duplicate().getImmutable();
        }

        //访问结构(N,fi)
        token.setN(as.getMatrix());
        token.setFi(as.getRho());
        token.setTi1(ti1);
        token.setTi2(ti2);
        token.setTi3(ti3);
        token.setTi4(ti4);

        return token;
    }

    /**
     * 搜索阶段
     * @param par
     * @param token
     * @param iw
     * @return
     */
    public static Boolean search(MyabsePar par,MyabseToken token,Iw iw,String policyFormula){
        System.out.println("--------------search phase---------------");
        boolean result = false;
        Pairing pairing = par.p;

        //定义等式左边、右边
        Element left = pairing.getGT().newElement();
        Element right = pairing.getGT().newElement();

        //调用python方法得到Omega
        //token中的（N,fi）
        Integer[][] N = token.getN();//矩阵N
        String[] fi = token.getFi();//映射fi
        String[] keyname = iw.getKeyname();//关键字名集合

        AA aa = pythonTest(policyFormula, keyname);
        //attr_path和omega的大小相等
        Integer[] attr_path = aa.getAttr_path();
        Integer[] omega = aa.getOmega();
        for (int i = 0; i < attr_path.length; i++) {
            //对整数型omega_i进行处理
            //todo
            Element omega_i = pairing.getZr().newRandomElement();
            if (omega[i] == 1){
                omega_i.setToOne().getImmutable();
            }else if (omega[i] == -1){
                omega_i.setToOne().negate().getImmutable();
            }else if (omega[i] == 0){
                omega_i.setToZero().getImmutable();
            }else {
                System.out.println("omega_i处理有误");
            }
            System.out.println("omega转成element类型后的值：" + omega_i);
            Element left_temp = pairing.pairing(token.t, iw.cwj[attr_path[i]]).mul(pairing.pairing(token.ti1[attr_path[i]], iw.cw1)).mul(pairing.pairing(token.ti2[attr_path[i]], iw.cw2)).mul(pairing.pairing(token.ti3[attr_path[i]], iw.cw3)).mul(pairing.pairing(token.ti4[attr_path[i]], iw.cw4)).duplicate();
            left.mul(left_temp).powZn(omega_i);
        }

        right = iw.cw0.duplicate();
        System.out.println("左边" + left);
        System.out.println("右边" + right);
        //判断等式是否相等
        if (left.equals(right)){
            result = true;
        }

        return result;
    }




    /**
     * 转换密钥阶段
     * @param par
     * @param sk
     * @return
     */
    public static MyabseKfog transform(MyabsePar par,MyabseSK sk,Element tau){
        System.out.println("--------------transform phase---------------");
        MyabseKfog kfog = new MyabseKfog();
        int n = sk.katt.length;
        kfog.katt0 = new Element[n];

        //随机数t
        Pairing pairing = par.p;
//        Element t = pairing.getZr().newRandomElement();
        TransformFactor transformFactor = new TransformFactor();
        transformFactor.setTau(tau);//转换因子tau

        kfog.k0 = pairing.getG1().newOneElement();
        kfog.k1 = pairing.getG1().newOneElement();
        //构造k'、k1
        kfog.k0 = sk.k2.duplicate();
        kfog.k0.powZn(tau);
        kfog.k1 = sk.k1.duplicate();
        //构造katt'
        for (int i = 0; i < n; i++) {
            kfog.katt0[i] = pairing.getG1().newRandomElement();
            kfog.katt0[i] = sk.katt[i].duplicate();
            kfog.katt0[i].powZn(tau);
        }

        return kfog;
    }

    /**
     * 雾阶段部分解密阶段
     * @param par
     * @param kfog
     * @param cTf
     * @return
     */
    public static MyabsePCT fnDec(MyabsePar par,MyabseKfog kfog,CTf cTf,String[] userAttrs,String accessFormula){
        System.out.println("---------------fog node partially dec phase---------------");
        MyabsePCT pct = new MyabsePCT();
        Pairing pairing = par.p;

        //定义等式左边
        Element left = pairing.getGT().newOneElement();

        //调用python方法找omega
        AA aa = pythonTest(accessFormula, userAttrs);
        Integer[] omega = aa.getOmega();//所有的omega
        Integer[] attr_path = aa.getAttr_path();//所有的attr_path

        for (int i = 0; i < omega.length; i++) {
            //将omega_i的类型处理为element类
            Element omega_i = pairing.getZr().newElement();
//            Element left_temp = pairing.getGT().newElement();
            if (omega[i] == 1){
                omega_i.setToOne();
            }else if (omega[i] == -1){
                omega_i.setToOne().negate();
            }else if (omega[i] == 0){
                omega_i.setToZero();
            }else {
                System.out.println("omega_i处理有误");
            }

            Element mul = pairing.pairing(cTf.Ci1[attr_path[i]], kfog.k0).mul(pairing.pairing(cTf.Ci2[attr_path[i]], kfog.katt0[attr_path[i]])).duplicate();
//            Element e1 = pairing.pairing(cTf.Ci1[attr_path[i]], kfog.k0);
//            Element e2 = pairing.pairing(cTf.Ci2[attr_path[i]], kfog.katt0[attr_path[i]]);
            left.mul(mul).powZn(omega_i);
        }

        pct.setPct(left);

        return pct;
    }

    /**
     * 用户最终解密
     * @param sk
     * @param pct
     * @return
     */
    public static Element userDec(MyabsePar par,MyabseSK sk,MyabsePCT pct,MyabseCipher ct,Element tau){
        System.out.println("-------------user dec phase--------------");
        Pairing pairing = par.p;

        //定义等式上下
        Element top = pairing.getGT().newOneElement();
        Element down = pairing.getGT().newOneElement();

        Element one = pairing.getG1().newElement();
        one.setToOne();
//        Element one = pairing.getG1().newRandomElement().set(1);
        Element pow = tau.invert();//k_tau分之一
        Element top_right = pairing.getGT().newElement();
        top_right = pct.getPct().duplicate();
        top_right.powZn(pow);
        top = ct.getcTf().getC().mul(top_right).duplicate().getImmutable();
//        top = ct.getcTf().getC().mul(pct.getPct().powZn(one.div(tau))).duplicate();

        down = pairing.pairing(ct.getcTf().C1,sk.getK1()).duplicate();

        Element file = pairing.getG1().newElement();
        file = top.div(down).duplicate();


        return file;

    }

    /**
     * 判断包含关系
     * @param attrs
     * @param u
     * @return
     */
    public static boolean isContain(String []attrs,String u){
        boolean ret = false;
        for(int i=0;i<attrs.length;i++){
            if(u.equals(attrs[i])){
                ret = true;
                break;
            }
        }
        return ret;
    }

//    private static Element dotProduct(List<AccessStructure.MatrixElement> v1, List<Element> v2, Element element, Pairing pairing) {
//        if (v1.size() != v2.size()) throw new IllegalArgumentException("different length");
//        if (element.isImmutable()) throw new IllegalArgumentException("immutable");
//
//        if (!element.isZero())
//            element.setToZero();
//
//        for (int i = 0; i < v1.size(); i++) {
//            Element e = pairing.getZr().newElement();
//            switch (v1.get(i)) {
//                case MINUS_ONE:
//                    e.setToOne().negate();
//                    break;
//                case ONE:
//                    e.setToOne();
//                    break;
//                case ZERO:
//                    e.setToZero();
//                    break;
//            }
//            element.add(e.mul(v2.get(i).getImmutable()));
//        }
//
//        return element.getImmutable();
//    }

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
}
