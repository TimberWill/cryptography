package com.whl.dpabse;

import it.unisa.dia.gas.jpbc.Element;

import java.util.Map;

/**
 * 作者：whl
 * 日期：2023-01-28 11:48
 * 描述：
 */
public class Iw {
    Map<Integer, Element> cwj;
    Element cw0;//GT
    Element cw1;//G1
    Element cw2;//G1
    Element cw3;//G1
    Element cw4;//G1

    public Iw() {
    }

    public Iw(Map<Integer, Element> cwj, Element cw0, Element cw1, Element cw2, Element cw3, Element cw4) {
        this.cwj = cwj;
        this.cw0 = cw0;
        this.cw1 = cw1;
        this.cw2 = cw2;
        this.cw3 = cw3;
        this.cw4 = cw4;
    }

    public Map<Integer, Element> getCwj() {
        return cwj;
    }

    public void setCwj(Map<Integer, Element> cwj) {
        this.cwj = cwj;
    }

    public Element getCw0() {
        return cw0;
    }

    public void setCw0(Element cw0) {
        this.cw0 = cw0;
    }

    public Element getCw1() {
        return cw1;
    }

    public void setCw1(Element cw1) {
        this.cw1 = cw1;
    }

    public Element getCw2() {
        return cw2;
    }

    public void setCw2(Element cw2) {
        this.cw2 = cw2;
    }

    public Element getCw3() {
        return cw3;
    }

    public void setCw3(Element cw3) {
        this.cw3 = cw3;
    }

    public Element getCw4() {
        return cw4;
    }

    public void setCw4(Element cw4) {
        this.cw4 = cw4;
    }
}
