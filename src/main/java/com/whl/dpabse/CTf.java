package com.whl.dpabse;

import it.unisa.dia.gas.jpbc.Element;

import java.util.Map;

/**
 * 作者：whl
 * 日期：2023-01-28 11:48
 * 描述：
 */
public class CTf {
    AA aa;
    Element c;
    Element c1;
    Map<Integer,Element> ci1;
    Map<Integer,Element> ci2;

    public CTf() {
    }

    public CTf(AA aa, Element c, Map<Integer, Element> ci1, Map<Integer, Element> ci2) {
        this.aa = aa;
        this.c = c;
        this.ci1 = ci1;
        this.ci2 = ci2;
    }

    public Element getC1() {
        return c1;
    }

    public void setC1(Element c1) {
        this.c1 = c1;
    }

    public AA getAa() {
        return aa;
    }

    public void setAa(AA aa) {
        this.aa = aa;
    }

    public Element getC() {
        return c;
    }

    public void setC(Element c) {
        this.c = c;
    }

    public Map<Integer, Element> getCi1() {
        return ci1;
    }

    public void setCi1(Map<Integer, Element> ci1) {
        this.ci1 = ci1;
    }

    public Map<Integer, Element> getCi2() {
        return ci2;
    }

    public void setCi2(Map<Integer, Element> ci2) {
        this.ci2 = ci2;
    }
}
