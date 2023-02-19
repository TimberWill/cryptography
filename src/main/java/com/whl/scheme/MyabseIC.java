package com.whl.scheme;

import it.unisa.dia.gas.jpbc.Element;

import java.util.List;
import java.util.Map;


/**
 * 作者：whl
 * 日期：2022-12-28 17:23
 * 描述：
 */
public class MyabseIC {
    public Element wo;  //Zr
    public Element c0;  //GT
    public Element c1;  //G1
    public Element[] ci1; //G1
    public Element[] ci2; //G1
    public Map<String, Element> temp;

    public Map<String, Element> getTemp() {
        return temp;
    }

    public void setTemp(Map<String, Element> temp) {
        this.temp = temp;
    }

    public Element[] getCi1() {
        return ci1;
    }

    public Element[] getCi2() {
        return ci2;
    }

    public Element getWo() {
        return wo;
    }

    public void setWo(Element wo) {
        this.wo = wo;
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

    public void setCi1(Element[] ci1) {
        this.ci1 = ci1;
    }

    public void setCi2(Element[] ci2) {
        this.ci2 = ci2;
    }
}
