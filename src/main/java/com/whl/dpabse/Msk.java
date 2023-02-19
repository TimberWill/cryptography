package com.whl.dpabse;

import it.unisa.dia.gas.jpbc.Element;

/**
 * 作者：whl
 * 日期：2023-01-28 11:44
 * 描述：
 */
public class Msk {
    Element alpha;//α
    Element beta;//β
    Element d1;//d1
    Element d2;//d2

    public Msk() {
    }

    public Msk(Element alpha, Element beta, Element d1, Element d2) {
        this.alpha = alpha;
        this.beta = beta;
        this.d1 = d1;
        this.d2 = d2;
    }

    public Element getAlpha() {
        return alpha;
    }

    public void setAlpha(Element alpha) {
        this.alpha = alpha;
    }

    public Element getBeta() {
        return beta;
    }

    public void setBeta(Element beta) {
        this.beta = beta;
    }

    public Element getD1() {
        return d1;
    }

    public void setD1(Element d1) {
        this.d1 = d1;
    }

    public Element getD2() {
        return d2;
    }

    public void setD2(Element d2) {
        this.d2 = d2;
    }
}
