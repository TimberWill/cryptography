package com.whl.emk;



import it.unisa.dia.gas.jpbc.Element;

import java.util.Map;

/**
 * 作者：whl
 * 日期：2023-01-15 15:52
 * 描述：
 */
public class IC {
    Element s;
    Element c0;//GT
    Element c1;
    Map<String,Element> ci;
    Map<String,Element> di;

    public IC() {
    }

    public IC(Element s, Element c0, Element c1, Map<String, Element> ci, Map<String, Element> di) {
        this.s = s;
        this.c0 = c0;
        this.c1 = c1;
        this.ci = ci;
        this.di = di;
    }

    public Element getS() {
        return s;
    }

    public void setS(Element s) {
        this.s = s;
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

    public Map<String, Element> getCi() {
        return ci;
    }

    public void setCi(Map<String, Element> ci) {
        this.ci = ci;
    }

    public Map<String, Element> getDi() {
        return di;
    }

    public void setDi(Map<String, Element> di) {
        this.di = di;
    }
}
