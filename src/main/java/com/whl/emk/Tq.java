package com.whl.emk;

import it.unisa.dia.gas.jpbc.Element;

/**
 * 作者：whl
 * 日期：2023-01-15 16:01
 * 描述：
 */
public class Tq {
    Element t1;
    Element t2;
    Element t3;
    Element t4;
    int d_;

    public Tq() {
    }

    public Tq(Element t1, Element t2, Element t3, Element t4, int d_) {
        this.t1 = t1;
        this.t2 = t2;
        this.t3 = t3;
        this.t4 = t4;
        this.d_ = d_;
    }

    public Tq(Element t1, Element t2, Element t3, Element t4) {
        this.t1 = t1;
        this.t2 = t2;
        this.t3 = t3;
        this.t4 = t4;
    }

    public int getD_() {
        return d_;
    }

    public void setD_(int d_) {
        this.d_ = d_;
    }

    public Element getT1() {
        return t1;
    }

    public void setT1(Element t1) {
        this.t1 = t1;
    }

    public Element getT2() {
        return t2;
    }

    public void setT2(Element t2) {
        this.t2 = t2;
    }

    public Element getT3() {
        return t3;
    }

    public void setT3(Element t3) {
        this.t3 = t3;
    }

    public Element getT4() {
        return t4;
    }

    public void setT4(Element t4) {
        this.t4 = t4;
    }
}
