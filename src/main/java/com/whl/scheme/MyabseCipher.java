package com.whl.scheme;

import com.sun.javafx.font.coretext.CTFactory;
import com.whl.policy.AccessStructure;
import com.whl.policy.Policy;
import it.unisa.dia.gas.jpbc.Element;

/**
 * 作者：whl
 * 日期：2022-12-28 17:29
 * 描述：
 */
public class MyabseCipher {
    private CTf cTf;
    private Iw iw;
    byte[] enFile;//加密文件的密文


    public MyabseCipher() {
    }

    public byte[] getEnFile() {
        return enFile;
    }

    public void setEnFile(byte[] enFile) {
        this.enFile = enFile;
    }

    public MyabseCipher(CTf cTf, Iw iw) {
        this.cTf = cTf;
        this.iw = iw;
    }

    public CTf getcTf() {
        return cTf;
    }

    public void setcTf(CTf cTf) {
        this.cTf = cTf;
    }

    public Iw getIw() {
        return iw;
    }

    public void setIw(Iw iw) {
        this.iw = iw;
    }
}
