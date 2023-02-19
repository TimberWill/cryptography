package com.whl.policy;

import it.unisa.dia.gas.jpbc.Element;

/**
 * 作者：whl
 * 日期：2023-01-05 11:34
 * 描述：
 */
public class Policy {
    Element[][] M;
    Element[] p;
    Element[] W;

    public Policy(Element[][] m, Element[] p) {
        M = m;
        this.p = p;
    }

    public Policy(Element[][] m, Element[] p, Element[] w) {
        M = m;
        this.p = p;
        W = w;
    }

    public Element[][] getM() {
        return M;
    }

    public Element[] getP() {
        return p;
    }

    public void setW(Element[] w) {
        W = w;
    }

    public Element[] getW() {
        return W;
    }

    public void setM(Element[][] m) {
        M = m;
    }

    public void setP(Element[] p) {
        this.p = p;
    }
}
