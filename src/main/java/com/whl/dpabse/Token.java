package com.whl.dpabse;

import it.unisa.dia.gas.jpbc.Element;

import java.util.Map;

/**
 * 作者：whl
 * 日期：2023-01-28 11:52
 * 描述：
 */
public class Token {
    AA aa;
    Element T;
    Map<Integer,Element> ti1;
    Map<Integer,Element> ti2;
    Element t3;
    Element t4;

    public Token() {
    }

    public Token(AA aa, Element t, Map<Integer, Element> ti1, Map<Integer, Element> ti2, Element t3, Element t4) {
        this.aa = aa;
        T = t;
        this.ti1 = ti1;
        this.ti2 = ti2;
        this.t3 = t3;
        this.t4 = t4;
    }

    public AA getAa() {
        return aa;
    }

    public void setAa(AA aa) {
        this.aa = aa;
    }

    public Element getT() {
        return T;
    }

    public void setT(Element t) {
        T = t;
    }

    public Map<Integer, Element> getTi1() {
        return ti1;
    }

    public void setTi1(Map<Integer, Element> ti1) {
        this.ti1 = ti1;
    }

    public Map<Integer, Element> getTi2() {
        return ti2;
    }

    public void setTi2(Map<Integer, Element> ti2) {
        this.ti2 = ti2;
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
