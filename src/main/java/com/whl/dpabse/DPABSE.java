package com.whl.dpabse;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者：whl
 * 日期：2023-01-28 11:40
 * 描述：
 */
public class DPABSE {

    /**
     * 初始化阶段
     * @param par
     * @param msk
     */
    public static void setup(Par par,Msk msk){

        Pairing bp = PairingFactory.getPairing("a.properties");
        par.setP(bp);

        Field G1 = bp.getG1();
        Field Zr = bp.getZr();
        Field GT = bp.getGT();

        //随机数
        Element alpha = Zr.newRandomElement().getImmutable();//α
        Element beta = Zr.newRandomElement().getImmutable();//β
        Element d1 = Zr.newRandomElement().getImmutable();//d1
        Element d2 = Zr.newRandomElement().getImmutable();//d2

        //g
        Element g = G1.newRandomElement().getImmutable();
        //g_alpha
        Element g_alpha = g.powZn(alpha).getImmutable();
        //g_d1
        Element g_d1 = g.powZn(d1).getImmutable();
        //g_d2
        Element g_d2 = g.powZn(d2).getImmutable();
        //g_d1d2
        Element g_d1d2 = g.powZn(d1.mul(d2)).getImmutable();
        //egg_beta
        Element egg_beta = bp.pairing(g, g).powZn(beta).getImmutable();
        //egg_d1d2_alpha
        Element egg_d1d2_alpha = bp.pairing(g, g).powZn(d1.mul(d2.mul(alpha))).getImmutable();

        par.setG(g);
        par.setG1(G1);
        par.setGT(GT);
        par.setZr(Zr);
        par.setG_alpha(g_alpha);
        par.setG_d1(g_d1);
        par.setG_d2(g_d2);
        par.setG_d1d2(g_d1d2);
        par.setEgg_beta(egg_beta);
        par.setEgg_d1d2_alpha(egg_d1d2_alpha);

        msk.setAlpha(alpha);
        msk.setBeta(beta);
        msk.setD1(d1);
        msk.setD2(d2);
    }

    /**
     * 密钥生成阶段
     * @param par
     * @param msk
     * @param s
     * @return
     */
    public static SK keyGen(Par par, Msk msk, String[] s){
        SK sk = new SK();
        //随机数r
        Element r = par.p.getZr().newRandomElement().getImmutable();
        //k1
        Element k11 = par.getG_alpha().duplicate().powZn(r);
        Element k12 = par.getG().duplicate().powZn(msk.getBeta());
        Element k1 = k11.duplicate().mul(k12).getImmutable();
        //k2
        Element k2 = par.getG().duplicate().powZn(r).getImmutable();
        //katt
        Map<String, Element> k_att = new HashMap<>();
        for (String s1 : s) {
            byte[] bytes = s1.getBytes();
            Element hash = par.p.getZr().newElementFromHash(bytes, 0, bytes.length).getImmutable();
            Element ki = par.getG().powZn(hash.duplicate().mul(r)).getImmutable();
            k_att.put(s1,ki);
        }

        sk.setK1(k1);
        sk.setK2(k2);
        sk.setK_att(k_att);

        return sk;
    }

    /**
     * 离线加密
     * @param par
     * @return
     */
    public static IC offlineEnc(Par par, String[] u){
        IC ic = new IC();
        Pairing pairing = par.getP();
        //随机数w
        Element w = par.getZr().newRandomElement().getImmutable();
        //c0
        Element c0 = par.getEgg_beta().duplicate().powZn(w).getImmutable();
        //c1
        Element c1 = par.getG().powZn(w).getImmutable();
        //ci1,ci2
        Map<String, Element> ci1 = new HashMap<>();
        Map<String, Element> ci2 = new HashMap<>();
        for (String s : u) {
            //随机数ti
            Element ti = par.getZr().newRandomElement().getImmutable();
            //hash
            byte[] bytes = s.getBytes();
            Element hash = par.getZr().newElementFromHash(bytes, 0, bytes.length).getImmutable();
            Element ci1i = par.getG().powZn(hash.mul(ti.negate())).getImmutable();
            ci1.put(s,ci1i);//ci1

            //ci2
            Element ci2i = par.getG().powZn(ti).getImmutable();
            ci2.put(s,ci2i);
        }

        ic.setC0(c0);
        ic.setC1(c1);
        ic.setCi1(ci1);
        ic.setCi2(ci2);
        ic.setW(w);
        return ic;
    }

    /**
     * 在线加密
     * @param par 公共参数
     * @param ic 中间密文
     * @param aa 访问结构
     * @param f 文件
     * @param w 关键字集合
     * @return 密文
     */
    public static CTIW onlineEnc(Par par, IC ic, AA aa, Element f, String[] w){
        CTIW ctiw = new CTIW();
        CTf cTf = new CTf();
        Iw iw = new Iw();
        Pairing pairing = par.getP();
        ///////////////////////////文件加密//////////////////////////////////////
        //向量v
        List<Element> v = new ArrayList<>();
        v.add(ic.w);
        for (int i = 1; i < aa.matrixGetN(); i++) {
            v.add(pairing.getZr().newRandomElement().getImmutable());
        }

        //c
        Element c = f.duplicate().mul(ic.getC0()).getImmutable();
        //ci1,ci2
        Map<Integer, Element> ci1 = new HashMap<>();
        Map<Integer, Element> ci2 = new HashMap<>();
        for (int i = 0; i < aa.matrixGetL(); i++) {
            //λi
            Element lambda_i = lambdaOrSigma_i(aa.matrixRow_i(i),v,pairing.getZr().newZeroElement(),pairing);
            //ci1
            Element ci11 = par.getG_alpha().duplicate().powZn(lambda_i).getImmutable();
            for (String s : ic.getCi1().keySet()) {
                if (aa.getRho()[i].equals(s)){
                    Element ci12 = ic.getCi1().get(s).getImmutable();
                    ci1.put(i+1,ci11.duplicate().mul(ci12).getImmutable());//ci1
                    ci2.put(i+1,ic.getCi2().get(s));//ci2
                }
            }
        }

        cTf.setAa(aa);
        cTf.setC(c);
        cTf.setC1(ic.getC1());
        cTf.setCi1(ci1);
        cTf.setCi2(ci2);

        ///////////////////////关键字加密///////////////////////////////////////
        //随机r1,r2
        Element r1 = par.getZr().newRandomElement().getImmutable();
        Element r2 = par.getZr().newRandomElement().getImmutable();
        //cwj
        Map<Integer, Element> cwj = new HashMap<>();
        int count = 1; //计数器
        for (String s : w) {
            byte[] bytes = s.getBytes();
            Element hash = par.getZr().newElementFromHash(bytes, 0, bytes.length).getImmutable();
            Element cwj_i = par.getG().powZn(hash.mul(r2)).getImmutable();
            cwj.put(count,cwj_i);
            count++;
        }
        //cw0
        Element cw0 = par.getEgg_d1d2_alpha().duplicate().powZn(r2).getImmutable();
        //cw1
        Element cw1 = par.getG_d1().duplicate().powZn(r2).getImmutable();
        //cw2
        Element cw2 = par.getG().duplicate().powZn(r2).getImmutable();
        //cw3
        Element cw3 = par.getG_d2().duplicate().powZn(r1).getImmutable();
        //cw4
        Element cw4 = par.getG_d1d2().duplicate().powZn(r1).getImmutable();

        iw.setCwj(cwj);
        iw.setCw0(cw0);
        iw.setCw1(cw1);
        iw.setCw2(cw2);
        iw.setCw3(cw3);
        iw.setCw4(cw4);

        ctiw.setCtf(cTf);
        ctiw.setIw(iw);

        return ctiw;
    }

    /**
     * 陷门生成
     * @param par 公共参数
     * @param sk 私钥
     * @param aa 访问结构
     * @param wi 关键字值集合
     * @return 陷门
     */
    public static Token trapdoor(Par par,Msk msk, SK sk, AA aa, String[] wi){
        Token token = new Token();
        Pairing pairing = par.getP();
        //向量u
        List<Element> u = new ArrayList<>();
        u.add(msk.getAlpha());
        for (int i = 1; i < aa.matrixGetN(); i++) {
            u.add(pairing.getZr().newRandomElement().getImmutable());
        }
        //随机数r'
        Element r_ = par.getZr().newRandomElement().getImmutable();
        //T
        Element t = par.getG_d1d2().duplicate().powZn(r_).getImmutable();
        //ti1
        Map<Integer, Element> ti1 = new HashMap<>();
        int count = 0;
        for (String s : wi) {
            byte[] bytes = s.getBytes();
            //hash
            Element hash = par.getZr().newElementFromHash(bytes, 0, bytes.length).getImmutable();
            Element ti11 = par.getG_d2().powZn(hash.mul(r_.negate())).getImmutable();
            ti1.put(++count,ti11);
        }
        //ti2 sigma_i
        HashMap<Integer, Element> ti2 = new HashMap<>();
        count = 0;
        for (int i = 0; i < aa.matrixGetL(); i++) {
            Element sigma_i = lambdaOrSigma_i(aa.matrixRow_i(i),u,pairing.getZr().newZeroElement(),pairing);
            Element ti22 = par.getG_d1d2().duplicate().powZn(sigma_i).getImmutable();
            ti2.put(++count,ti22);
        }
        //t3
        Element t3 = par.getG_d1().duplicate().powZn(r_.negate()).getImmutable();
        //t4
        Element t4 = par.getG().duplicate().powZn(r_).getImmutable();

        token.setAa(aa);
        token.setT(t);
        token.setTi1(ti1);
        token.setTi2(ti2);
        token.setT3(t3);
        token.setT4(t4);

        return token;
    }

    /**
     * 查询阶段
     * @param par
     * @param token
     * @param iw
     * @return
     */
    public static CTf search(Par par, Token token, Iw iw){
        CTf cTf = new CTf();
        Pairing pairing = par.getP();

        //获取omega相关信息
        String[] rho = token.getAa().getRho();
        Integer[] omega = token.getAa().getOmega();
        Integer[] attr_path = token.getAa().getAttr_path();

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

        //p4
        Element p4 = pairing.pairing(token.getT3(), iw.getCw3()).getImmutable();
        //p5
        Element p5 = pairing.pairing(token.getT4(), iw.getCw4()).getImmutable();

        Element sum = pairing.getGT().newOneElement();
        for (int i = 0; i < attr_path.length; i++) {
            Element cwj_i = iw.getCwj().get(attr_path[i] + 1).getImmutable();
            Element ti1_i = token.getTi1().get(attr_path[i] + 1).getImmutable();
            Element ti2_i = token.getTi2().get(attr_path[i] + 1).getImmutable();
            //p1
            Element p1 = pairing.pairing(token.getT(), cwj_i).getImmutable();
            //p2
            Element p2 = pairing.pairing(ti1_i, iw.getCw1()).getImmutable();
            //p3
            Element p3 = pairing.pairing(ti2_i, iw.getCw2()).getImmutable();
            Element mul = p4.mul(p5.mul(p1.mul(p2.mul(p3)))).getImmutable();
            Element element = mul.powZn(wi[i]).getImmutable();
            sum.mul(element);
        }

        //判断
        if (sum.isEqual(iw.getCw0())){
            System.out.println("成立");
        }else{
            System.out.println("不成立");
        }

        return cTf;
    }

    /**
     * 转换密钥阶段
     * @param par
     * @param sk
     * @return
     */
    public static Kfog transform(Par par, SK sk, Element tau){
        Kfog kfog = new Kfog();
        //k'
        Element k_ = sk.getK2().duplicate().powZn(tau).getImmutable();
        //k'att
        Map<String, Element> k_att_ = new HashMap<>();//k'att
        Map<String, Element> k_att = sk.getK_att();//k_att
        for (String s : k_att.keySet()) {
            Element k_att_i = k_att.get(s).duplicate().powZn(tau).getImmutable();
            k_att_.put(s,k_att_i);
        }

        kfog.setK_(k_);
        kfog.setK1(sk.getK1());
        kfog.setK_att_(k_att_);

        return kfog;
    }

    /**
     * 雾节点解密阶段
     * @param par
     * @param kfog
     * @param cTf
     * @return
     */
    public static PCT fnDec(Par par, Kfog kfog, CTf cTf){
        PCT pct = new PCT();
        Pairing pairing = par.getP();
        //获取访问结构中的omega信息
        Integer[] attr_path = cTf.getAa().getAttr_path();
        Integer[] omega = cTf.getAa().getOmega();
        String[] rho = cTf.getAa().getRho();

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
        //连乘
        Element p = pairing.getGT().newOneElement();
        for (int i = 0; i < attr_path.length; i++) {
            Element ci11 = cTf.getCi1().get(attr_path[i] + 1).getImmutable();
            Element ci22 = cTf.getCi2().get(attr_path[i] + 1).getImmutable();
            Element k_rho_i = kfog.getK_att_().get(rho[attr_path[i]]).getImmutable();
            //pp
            Element pairing1 = pairing.pairing(ci11, kfog.getK_()).getImmutable();
            Element pairing2 = pairing.pairing(ci22, k_rho_i).getImmutable();
            Element mul = pairing1.mul(pairing2).getImmutable();
            Element pp = mul.powZn(wi[i]);

            p.mul(pp);
        }

        pct.setPct(p);
        pct.setC(cTf.getC());
        pct.setC1(cTf.getC1());
        return pct;
    }

    /**
     * 用户解密阶段
     * @param k_tau
     * @param sk
     * @param pct
     * @return
     */
    public static Element userDec(Element k_tau, SK sk, PCT pct){
        Pairing bp = PairingFactory.getPairing("a.properties");

        Element element1 = bp.getZr().newOneElement();
        Element pow = element1.duplicate().div(k_tau).getImmutable();

        Element top = pct.getC().duplicate().mul(pct.getPct().powZn(pow)).getImmutable();
        Element down = bp.pairing(pct.getC1(), sk.getK1()).getImmutable();

        Element f = top.div(down);

        return f;
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
}
