package com.whl.dpabse;

import it.unisa.dia.gas.jpbc.Element;

import java.util.Map;

/**
 * 作者：whl
 * 日期：2023-01-28 11:55
 * 描述：
 */
public class Kfog {
    Element k_;
    Element k1;
    Map<String,Element> k_att_;

    public Kfog() {
    }

    public Kfog(Element k_, Element k1, Map<String, Element> k_att_) {
        this.k_ = k_;
        this.k1 = k1;
        this.k_att_ = k_att_;
    }

    public Element getK_() {
        return k_;
    }

    public void setK_(Element k_) {
        this.k_ = k_;
    }

    public Element getK1() {
        return k1;
    }

    public void setK1(Element k1) {
        this.k1 = k1;
    }

    public Map<String, Element> getK_att_() {
        return k_att_;
    }

    public void setK_att_(Map<String, Element> k_att_) {
        this.k_att_ = k_att_;
    }
}
