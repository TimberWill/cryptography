package com.whl.scheme;


import it.unisa.dia.gas.jpbc.Element;

/**
 * 作者：whl
 * 日期：2023-01-10 23:56
 * 描述：
 */
public class TransformFactor {
    Element tau;//转换因子tau

    public TransformFactor() {
    }

    public TransformFactor(Element tau) {
        this.tau = tau;
    }

    public Element getTau() {
        return tau;
    }

    public void setTau(Element tau) {
        this.tau = tau;
    }
}
