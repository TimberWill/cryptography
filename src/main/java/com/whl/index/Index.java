package com.whl.index;

/**
 * 作者：whl
 * 日期：2022-12-11 18:28
 * 描述：
 */
public class Index {
    public String keyword;
    public String[] file;
    //无参构造
    public Index() {
    }
    //有参构造
    public Index(String keyword, String[] file) {
        this.keyword = keyword;
        this.file = file;
    }
}
