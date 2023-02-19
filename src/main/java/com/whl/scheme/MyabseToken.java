package com.whl.scheme;

import com.whl.policy.AA;
import com.whl.policy.AccessStructure;
import com.whl.policy.Policy;
import it.unisa.dia.gas.jpbc.Element;

/**
 * 作者：whl
 * 日期：2022-12-15 15:25
 * 描述：
 */
public class MyabseToken {
    public Element t;    //T---G1
    public Element[] ti1;//Ti1---G1
    public Element[] ti2;//Ti2---G1
    public Element[] ti3;//Ti3---Zr
    public Element[] ti4;//Ti4---Zr

    public Integer[][] N;//访问结构（N，fi）
    public String[] fi;

    public Element getT() {
        return t;
    }

    public void setT(Element t) {
        this.t = t;
    }

    public Element[] getTi1() {
        return ti1;
    }

    public void setTi1(Element[] ti1) {
        this.ti1 = ti1;
    }

    public Element[] getTi2() {
        return ti2;
    }

    public void setTi2(Element[] ti2) {
        this.ti2 = ti2;
    }

    public Element[] getTi3() {
        return ti3;
    }

    public void setTi3(Element[] ti3) {
        this.ti3 = ti3;
    }

    public Element[] getTi4() {
        return ti4;
    }

    public void setTi4(Element[] ti4) {
        this.ti4 = ti4;
    }

    public Integer[][] getN() {
        return N;
    }

    public void setN(Integer[][] n) {
        N = n;
    }

    public String[] getFi() {
        return fi;
    }

    public void setFi(String[] fi) {
        this.fi = fi;
    }
}
