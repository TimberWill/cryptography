package com.whl.emk;

import it.unisa.dia.gas.jpbc.Element;

import java.util.Map;

/**
 * 作者：whl
 * 日期：2023-01-15 16:06
 * 描述：
 */
public class KI {
    Element k6;
    Element k5;
    Map<String,Element> ki;

    public KI() {
    }

    public KI(Element k6, Element k5, Map<String, Element> ki) {
        this.k6 = k6;
        this.k5 = k5;
        this.ki = ki;
    }

    public Element getK6() {
        return k6;
    }

    public void setK6(Element k6) {
        this.k6 = k6;
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
