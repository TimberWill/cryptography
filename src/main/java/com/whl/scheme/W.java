package com.whl.scheme;

import java.util.Map;

/**
 * 作者：whl
 * 日期：2023-01-09 14:28
 * 描述：
 */
public class W {
    public Map<String,String> KW;

    public W(Map<String, String> KW) {
        this.KW = KW;
    }

    public Map<String, String> getKW() {
        return KW;
    }

    public void setKW(Map<String, String> KW) {
        this.KW = KW;
    }
}
