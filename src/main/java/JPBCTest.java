import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;



/**
 * 作者：whl
 * 日期：2023-01-15 16:51
 * 描述：
 */
public class JPBCTest {
    public static void main(String[] args) {
        //BLS签名
        Pairing bp = PairingFactory.getPairing("a.properties");
        //域
        Field G1 = bp.getG1();
        Field Zr = bp.getZr();
        Field GT = bp.getGT();

        Element x = Zr.newRandomElement().getImmutable();
        Element g = G1.newRandomElement().getImmutable();
        Element g_x = g.powZn(x);

        String m = "message";
        //这里m的hash值是int类型的
        byte[] bytes = Integer.toString(m.hashCode()).getBytes();
        Element h = G1.newElementFromHash(bytes, 0, bytes.length);
        Element sigma = h.duplicate().powZn(x);//注意要用duplicate()

        Element left = bp.pairing(sigma, g);
        Element right = bp.pairing(h, g_x);
        if (left.isEqual(right)){
            System.out.println("yes");
        }else {
            System.out.println("no");
        }
//        Element element = bp.getGT().newElement();
//        Element element1 = bp.getGT().newOneElement();
//        Element element2 = bp.getGT().newZeroElement();
//        Element element3 = bp.getGT().newRandomElement();
//        System.out.println(element);
//        System.out.println(element1);
//        System.out.println(element2);
//        System.out.println(element3);
    }
}
