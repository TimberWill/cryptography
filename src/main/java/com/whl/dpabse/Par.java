package com.whl.dpabse;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;

/**
 * 作者：whl
 * 日期：2023-01-28 11:44
 * 描述：
 */
public class Par {
    String pairingDec;
    Pairing p;
    Field G1;
    Field GT;
    Field Zr;

    Element g;
    Element g_alpha;
    Element g_d1;
    Element g_d2;
    Element g_d1d2;

    Element egg_beta;
    Element egg_d1d2_alpha;

    public Par() {
    }

    public Par(String pairingDec, Pairing p, Field g1, Field GT, Field zr, Element g, Element g_alpha, Element g_d1, Element g_d2, Element g_d1d2, Element egg_beta, Element egg_d1d2_alpha) {
        this.pairingDec = pairingDec;
        this.p = p;
        G1 = g1;
        this.GT = GT;
        Zr = zr;
        this.g = g;
        this.g_alpha = g_alpha;
        this.g_d1 = g_d1;
        this.g_d2 = g_d2;
        this.g_d1d2 = g_d1d2;
        this.egg_beta = egg_beta;
        this.egg_d1d2_alpha = egg_d1d2_alpha;
    }

    public String getPairingDec() {
        return pairingDec;
    }

    public void setPairingDec(String pairingDec) {
        this.pairingDec = pairingDec;
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

    public Field getZr() {
        return Zr;
    }

    public void setZr(Field zr) {
        Zr = zr;
    }

    public Element getG() {
        return g;
    }

    public void setG(Element g) {
        this.g = g;
    }

    public Element getG_alpha() {
        return g_alpha;
    }

    public void setG_alpha(Element g_alpha) {
        this.g_alpha = g_alpha;
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

    public Element getG_d1d2() {
        return g_d1d2;
    }

    public void setG_d1d2(Element g_d1d2) {
        this.g_d1d2 = g_d1d2;
    }

    public Element getEgg_beta() {
        return egg_beta;
    }

    public void setEgg_beta(Element egg_beta) {
        this.egg_beta = egg_beta;
    }

    public Element getEgg_d1d2_alpha() {
        return egg_d1d2_alpha;
    }

    public void setEgg_d1d2_alpha(Element egg_d1d2_alpha) {
        this.egg_d1d2_alpha = egg_d1d2_alpha;
    }
}
