package com.whl.scheme;


import it.unisa.dia.gas.jpbc.Element;

/**
 * 作者：whl
 * 日期：2022-12-15 15:25
 * 描述：
 */
public class MyabseSK {
    //用户私钥
    public Element k1;  //G2
    public Element k2;  //G2
    public Element[] katt;//Zr

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

    public Element[] getKatt() {
        return katt;
    }

    public void setKatt(Element[] katt) {
        this.katt = katt;
    }
}
