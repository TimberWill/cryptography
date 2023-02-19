package com.whl.emk;

import it.unisa.dia.gas.jpbc.Element;

import java.util.Map;

/**
 * 作者：whl
 * 日期：2023-01-15 15:46
 * 描述：
 */
public class SK {
    Element k;//g^(x beta)
    Element k1;
    Element k2;
    Element k3;
    Element k4;
    Element k5;

    Map<String,Element> ki;

    public SK() {
    }

    public SK(Element k, Element k1, Element k2, Element k3, Element k4, Element k5, Map<String, Element> ki) {
        this.k = k;
        this.k1 = k1;
        this.k2 = k2;
        this.k3 = k3;
        this.k4 = k4;
        this.k5 = k5;
        this.ki = ki;
    }

    public Element getK() {
        return k;
    }

    public void setK(Element k) {
        this.k = k;
    }

    public Element getK1() {
        return k1;
    }

    public void setK1(Element k1) {
        this.k1 = k1;
    }

    public Element getK2() {
        return k2;
    }

    public void setK2(Element k2) {
        this.k2 = k2;
    }

    public Element getK3() {
        return k3;
    }

    public void setK3(Element k3) {
        this.k3 = k3;
    }

    public Element getK4() {
        return k4;
    }

    public void setK4(Element k4) {
        this.k4 = k4;
    }

    public Element getK5() {
        return k5;
    }

    public void setK5(Element k5) {
        this.k5 = k5;
    }

    public Map<String, Element> getKi() {
        return ki;
    }

    public void setKi(Map<String, Element> ki) {
        this.ki = ki;
    }
}
