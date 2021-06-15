package cn.zzq0324.feature.flag.algorithm;

import java.math.BigInteger;

/**
 * description: FNV算法 <br>
 * date: 2021/6/10 1:37 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
public class FNV {
    private static final BigInteger INIT32 = new BigInteger("811c9dc5", 16);
    private static final BigInteger PRIME32 = new BigInteger("01000193", 16);
    private static final BigInteger MOD32 = new BigInteger("2").pow(32);

    /**
     * fnv1a_32算法计算hash值
     *
     * @param text 计算hash值的原始数据
     * @return 返回哈希值
     * @see <a href="https://github.com/Killeroid/JavaFNV/blob/master/src/main/java/Hash/FNV.java">JavaFNV</a>
     */
    public static long fnv1a_32(String text) {
        BigInteger hash = INIT32;

        for (byte b : text.getBytes()) {
            hash = hash.xor(BigInteger.valueOf((int)b & 0xff));
            hash = hash.multiply(PRIME32).mod(MOD32);
        }

        return hash.longValue();
    }
}
