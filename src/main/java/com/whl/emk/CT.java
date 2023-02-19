package com.whl.emk;

import it.unisa.dia.gas.jpbc.Element;

import java.util.Map;

/**
 * 作者：whl
 * 日期：2023-01-15 15:54
 * 描述：
 */
public class CT {

    Element c;//GT
    Element c1;
    Map<Integer,Element> ci;
    Map<Integer,Element> di;

    //访问结构
    AA aa;

    public CT() {
    }

    public CT(Element c, Element c1, Map<Integer, Element> ci, Map<Integer, Element> di, AA aa) {
        this.c = c;
        this.c1 = c1;
        this.ci = ci;
        this.di = di;
        this.aa = aa;
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

    public Map<Integer, Element> getCi() {
        return ci;
    }

    public void setCi(Map<Integer, Element> ci) {
        this.ci = ci;
    }

    public Map<Integer, Element> getDi() {
        return di;
    }

    public void setDi(Map<Integer, Element> di) {
        this.di = di;
    }

    public AA getAa() {
        return aa;
    }

    public void setAa(AA aa) {
        this.aa = aa;
    }
}
