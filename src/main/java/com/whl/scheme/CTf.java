package com.whl.scheme;

import com.whl.policy.AA;
import com.whl.policy.AccessStructure;
import it.unisa.dia.gas.jpbc.Element;

/**
 * 作者：whl
 * 日期：2023-01-09 19:08
 * 描述：
 */
public class CTf {
    public Element C;     //GT
    public Element C1;    //G1
    public Element[] Ci1; //G1
    public Element[] Ci2; //G1
    public Integer[][] M; //矩阵M
    public String[] rho;//映射ρ

    String[] userAttris; //用户属性集合


    public CTf() {
    }

    public Integer[][] getM() {
        return M;
    }

    public void setM(Integer[][] m) {
        M = m;
    }

    public String[] getRho() {
        return rho;
    }

    public void setRho(String[] rho) {
        this.rho = rho;
    }

    public CTf(Element c, Element c1, Element[] ci1, Element[] ci2, Integer[][] m, String[] rho, String[] userAttris) {
        C = c;
        C1 = c1;
        Ci1 = ci1;
        Ci2 = ci2;
        M = m;
        this.rho = rho;
        this.userAttris = userAttris;
    }

    public Element getC() {
        return C;
    }

    public void setC(Element c) {
        C = c;
    }

    public Element getC1() {
        return C1;
    }

    public void setC1(Element c1) {
        C1 = c1;
    }

    public Element[] getCi1() {
        return Ci1;
    }

    public void setCi1(Element[] ci1) {
        Ci1 = ci1;
    }

    public Element[] getCi2() {
        return Ci2;
    }

    public void setCi2(Element[] ci2) {
        Ci2 = ci2;
    }


    public String[] getUserAttris() {
        return userAttris;
    }

    public void setUserAttris(String[] userAttris) {
        this.userAttris = userAttris;
    }
}
