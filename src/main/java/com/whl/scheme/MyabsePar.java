package com.whl.scheme;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;

/**
 * 作者：whl
 * 日期：2022-12-15 15:21
 * 描述：
 */
public class MyabsePar {
    //公共参数
    public String paringDesc;
    public Pairing p;
    public Element g_alpha; //G1
    public Element g_beta;  //G1
    public Element g_d1;    //G1
    public Element g_d2;    //G1
    public Element g_d3;    //G1
    public Element g_d4;    //G1
    //e(g,g)^beta
    //e(g,g)^d1d2alpha
    public Element g1;       //G1
    public Element g2;       //G2

    public Element e_gg_beta ;//GT
    public Element e_gg_d1d2alpha ;//GT

    public String getParingDesc() {
        return paringDesc;
    }

    public void setParingDesc(String paringDesc) {
        this.paringDesc = paringDesc;
    }

    public Pairing getP() {
        return p;
    }

    public void setP(Pairing p) {
        this.p = p;
    }

    public Element getG_alpha() {
        return g_alpha;
    }

    public void setG_alpha(Element g_alpha) {
        this.g_alpha = g_alpha;
    }

    public Element getG_beta() {
        return g_beta;
    }

    public void setG_beta(Element g_beta) {
        this.g_beta = g_beta;
    }

    public Element getG_d1() {
        return g_d1;
    }

    public void setG_d1(Element g_d1) {
        this.g_d1 = g_d1;
    }

    public Element getG_d2() {
        return g_d2;
    }

    public void setG_d2(Element g_d2) {
        this.g_d2 = g_d2;
    }

    public Element getG_d3() {
        return g_d3;
    }

    public void setG_d3(Element g_d3) {
        this.g_d3 = g_d3;
    }

    public Element getG_d4() {
        return g_d4;
    }

    public void setG_d4(Element g_d4) {
        this.g_d4 = g_d4;
    }

    public Element getG1() {
        return g1;
    }

    public void setG1(Element g1) {
        this.g1 = g1;
    }

    public Element getG2() {
        return g2;
    }

    public void setG2(Element g2) {
        this.g2 = g2;
    }

    public Element getE_gg_beta() {
        return e_gg_beta;
    }

    public void setE_gg_beta(Element e_gg_beta) {
        this.e_gg_beta = e_gg_beta;
    }

    public Element getE_gg_d1d2alpha() {
        return e_gg_d1d2alpha;
    }

    public void setE_gg_d1d2alpha(Element e_gg_d1d2alpha) {
        this.e_gg_d1d2alpha = e_gg_d1d2alpha;
    }
}
