package com.whl.emk;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;

import java.util.Map;

/**
 * 作者：whl
 * 日期：2023-01-14 16:49
 * 描述：
 */
public class GP {
    //公共参数
    String paringDesc;
    Pairing p;

    Field G1;
    Field GT;

    Element g;//G1
    Element egg_alpha;//GT
    Element g_beta;//
    Element g_mu;//G1
    Map<String,Element> Ti;

    public GP(String paringDesc, Pairing p, Field g1, Field GT, Element g, Element egg_alpha, Element g_beta, Element g_mu, Map<String, Element> ti) {
        this.paringDesc = paringDesc;
        this.p = p;
        G1 = g1;
        this.GT = GT;
        this.g = g;
        this.egg_alpha = egg_alpha;
        this.g_beta = g_beta;
        this.g_mu = g_mu;
        Ti = ti;
    }

    public GP() {
    }

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

    public Field getG1() {
        return G1;
    }

    public void setG1(Field g1) {
        G1 = g1;
    }

    public Field getGT() {
        return GT;
    }

    public void setGT(Field GT) {
        this.GT = GT;
    }

    public Element getG() {
        return g;
    }

    public void setG(Element g) {
        this.g = g;
    }

    public Element getEgg_alpha() {
        return egg_alpha;
    }

    public void setEgg_alpha(Element egg_alpha) {
        this.egg_alpha = egg_alpha;
    }

    public Element getG_beta() {
        return g_beta;
    }

    public void setG_beta(Element g_beta) {
        this.g_beta = g_beta;
    }

    public Element getG_mu() {
        return g_mu;
    }

    public void setG_mu(Element g_mu) {
        this.g_mu = g_mu;
    }

    public Map<String, Element> getTi() {
        return Ti;
    }

    public void setTi(Map<String, Element> ti) {
        Ti = ti;
    }
}
