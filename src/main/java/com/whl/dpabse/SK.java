package com.whl.dpabse;

import it.unisa.dia.gas.jpbc.Element;

import java.util.Map;

/**
 * 作者：whl
 * 日期：2023-01-28 11:42
 * 描述：
 */
public class SK {
    Element k1;
    Element k2;
    Map<String,Element> k_att;

    public SK(Element k1, Element k2, Map<String, Element> k_att) {
        this.k1 = k1;
        this.k2 = k2;
        this.k_att = k_att;
    }

    public SK() {
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

    public Map<String, Element> getK_att() {
        return k_att;
    }

    public void setK_att(Map<String, Element> k_att) {
        this.k_att = k_att;
    }
}
