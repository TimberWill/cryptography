package com.whl.emk;

import it.unisa.dia.gas.jpbc.Element;

import java.util.Map;

/**
 * 作者：whl
 * 日期：2023-01-15 15:55
 * 描述：
 */
public class IW {
    Map<Integer, Element> cwj;
    Element cw1;
    Element cw2;
    Element cw3;

    public IW() {
    }

    public IW(Map<Integer, Element> cwj, Element cw1, Element cw2, Element cw3) {
        this.cwj = cwj;
        this.cw1 = cw1;
        this.cw2 = cw2;
        this.cw3 = cw3;
    }

    public Map<Integer, Element> getCwj() {
        return cwj;
    }

    public void setCwj(Map<Integer, Element> cwj) {
        this.cwj = cwj;
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
}
