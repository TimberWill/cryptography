package com.whl.dpabse;

import it.unisa.dia.gas.jpbc.Element;

import java.util.Map;

/**
 * 作者：whl
 * 日期：2023-01-28 11:46
 * 描述：
 */
public class IC {
    Element w;//随机数
    Element c0;//GT
    Element c1;//G1
    Map<String,Element> ci1;
    Map<String,Element> ci2;

    public IC(Element w, Element c0, Element c1, Map<String, Element> ci1, Map<String, Element> ci2) {
        this.w = w;
        this.c0 = c0;
        this.c1 = c1;
        this.ci1 = ci1;
        this.ci2 = ci2;
    }

    public IC() {
    }

    public Element getW() {
        return w;
    }

    public void setW(Element w) {
        this.w = w;
    }

    public Element getC0() {
        return c0;
    }

    public void setC0(Element c0) {
        this.c0 = c0;
    }

    public Element getC1() {
        return c1;
    }

    public void setC1(Element c1) {
        this.c1 = c1;
    }

    public Map<String, Element> getCi1() {
        return ci1;
    }

    public void setCi1(Map<String, Element> ci1) {
        this.ci1 = ci1;
    }

    public Map<String, Element> getCi2() {
        return ci2;
    }

    public void setCi2(Map<String, Element> ci2) {
        this.ci2 = ci2;
    }
}
