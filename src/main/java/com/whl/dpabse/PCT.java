package com.whl.dpabse;

import it.unisa.dia.gas.jpbc.Element;

/**
 * 作者：whl
 * 日期：2023-01-28 11:57
 * 描述：
 */
public class PCT {
    Element pct;
    Element c;
    Element c1;

    public PCT() {
    }

    public PCT(Element pct, Element c, Element c1) {
        this.pct = pct;
        this.c = c;
        this.c1 = c1;
    }

    public Element getPct() {
        return pct;
    }

    public void setPct(Element pct) {
        this.pct = pct;
    }

    public Element getC() {
        return c;
    }

    public void setC(Element c) {
        this.c = c;
    }

    public Element getC1() {
        return c1;
    }

    public void setC1(Element c1) {
        this.c1 = c1;
    }
}
