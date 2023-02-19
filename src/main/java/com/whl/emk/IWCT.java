package com.whl.emk;

/**
 * 作者：whl
 * 日期：2023-01-15 15:55
 * 描述：
 */
public class IWCT {
    CT ct;
    IW iw;

    public IWCT() {
    }

    public IWCT(CT ct, IW iw) {
        this.ct = ct;
        this.iw = iw;
    }

    public CT getCt() {
        return ct;
    }

    public void setCt(CT ct) {
        this.ct = ct;
    }

    public IW getIw() {
        return iw;
    }

    public void setIw(IW iw) {
        this.iw = iw;
    }
}
