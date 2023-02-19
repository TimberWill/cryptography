package com.whl.emk;

import it.unisa.dia.gas.jpbc.Element;

/**
 * 作者：whl
 * 日期：2023-01-14 16:50
 * 描述：
 */
public class MSK {
    //主密钥
    Element alpha; //α
    Element beta;  //β

    public MSK() {
    }

    public MSK(Element alpha, Element beta) {
        this.alpha = alpha;
        this.beta = beta;
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
}
