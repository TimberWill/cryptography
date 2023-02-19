package com.whl;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

import java.io.ByteArrayInputStream;

/**
 * 作者：whl
 * 日期：2023-01-12 20:46
 * 描述：
 */
public class Test {

    public static void main(String[] args) {
        Pairing pairing = PairingFactory.getPairing("a.properties");

        Field G1 = pairing.getG1();
        Field Zr = pairing.getZr();

        Element g = G1.newRandomElement().getImmutable();
        Element a = Zr.newRandomElement().getImmutable();
        Element b = Zr.newRandomElement().getImmutable();

        //等式左半边
        Element g_a = g.powZn(a);
        Element g_b = g.powZn(b);
        Element egg_ab = pairing.pairing(g_a, g_b);
        //等式右半边
        Element egg = pairing.pairing(g, g).getImmutable();
        Element ab = a.mul(b);
        Element egg_ab_p = egg.powZn(ab);

//        System.out.println("------------");
//        Element element = Zr.newOneElement();
//        Element pow = element.duplicate().div(ab);
//        Element first = egg_ab.duplicate().powZn(pow);
//        if (first.isEqual(egg)){
//            System.out.println("对");
//        }else {
//            System.out.println("错");
//        }

        //验证
        if (egg_ab.isEqual(egg_ab_p)){
            System.out.println("yes");
        }else {
            System.out.println("no");
        }
        //
        if (egg_ab.equals(egg_ab_p)){
            System.out.println("yes");
        }else {
            System.out.println("no");
        }
    }
}
