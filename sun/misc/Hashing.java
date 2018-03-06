package sun.misc;

// 基于ThreadLocal实现的随机数
import java.util.concurrent.ThreadLocalRandom;

/**
 * 关于Hashing的类
 */
public class Hashing {
    private Hashing() {
        // 不允许实例化此类
        throw new Error("No instances");
    }

    public static int murmur3_32(byte[] paramArrayOfByte) {
        return murmur3_32(0, paramArrayOfByte, 0, paramArrayOfByte.length);
    }

    public static int murmur3_32(int paramInt, byte[] paramArrayOfByte) {
        return murmur3_32(paramInt, paramArrayOfByte, 0, paramArrayOfByte.length);
    }

    public static int murmur3_32(int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3) {
        int i = paramInt1;
        int j = paramInt3;
        int k;
        while (j >= 4) {
            k = paramArrayOfByte[paramInt2] & 0xFF | (paramArrayOfByte[(paramInt2 + 1)] & 0xFF) << 8 | (paramArrayOfByte[(paramInt2 + 2)] & 0xFF) << 16 | paramArrayOfByte[(paramInt2 + 3)] << 24;

            j -= 4;
            paramInt2 += 4;

            k *= -862048943;
            k = Integer.rotateLeft(k, 15);
            k *= 461845907;

            i ^= k;
            i = Integer.rotateLeft(i, 13);
            i = i * 5 + -430675100;
        }

        if (j > 0) {
            k = 0;

            switch (j) {
                case 3:
                    k ^= (paramArrayOfByte[(paramInt2 + 2)] & 0xFF) << 16;
                case 2:
                    k ^= (paramArrayOfByte[(paramInt2 + 1)] & 0xFF) << 8;
                case 1:
                    k ^= paramArrayOfByte[paramInt2] & 0xFF;
            }

            k *= -862048943;
            k = Integer.rotateLeft(k, 15);
            k *= 461845907;
            i ^= k;
        }

        i ^= paramInt3;

        i ^= i >>> 16;
        i *= -2048144789;
        i ^= i >>> 13;
        i *= -1028477387;
        i ^= i >>> 16;

        return i;
    }

    public static int murmur3_32(char[] paramArrayOfChar) {
        return murmur3_32(0, paramArrayOfChar, 0, paramArrayOfChar.length);
    }

    public static int murmur3_32(int paramInt, char[] paramArrayOfChar) {
        return murmur3_32(paramInt, paramArrayOfChar, 0, paramArrayOfChar.length);
    }

    public static int murmur3_32(int paramInt1, char[] paramArrayOfChar, int paramInt2, int paramInt3) {
        int i = paramInt1;

        int j = paramInt2;
        int k = paramInt3;
        int m;
        while (k >= 2) {
            m = paramArrayOfChar[(j++)] & 0xFFFF | paramArrayOfChar[(j++)] << '\020';

            k -= 2;

            m *= -862048943;
            m = Integer.rotateLeft(m, 15);
            m *= 461845907;

            i ^= m;
            i = Integer.rotateLeft(i, 13);
            i = i * 5 + -430675100;
        }

        if (k > 0) {
            m = paramArrayOfChar[j];

            m *= -862048943;
            m = Integer.rotateLeft(m, 15);
            m *= 461845907;
            i ^= m;
        }

        i ^= paramInt3 * 2;

        i ^= i >>> 16;
        i *= -2048144789;
        i ^= i >>> 13;
        i *= -1028477387;
        i ^= i >>> 16;

        return i;
    }

    public static int murmur3_32(int[] paramArrayOfInt) {
        return murmur3_32(0, paramArrayOfInt, 0, paramArrayOfInt.length);
    }

    public static int murmur3_32(int paramInt, int[] paramArrayOfInt) {
        return murmur3_32(paramInt, paramArrayOfInt, 0, paramArrayOfInt.length);
    }

    public static int murmur3_32(int paramInt1, int[] paramArrayOfInt, int paramInt2, int paramInt3) {
        int i = paramInt1;

        int j = paramInt2;
        int k = paramInt2 + paramInt3;

        while (j < k) {
            int m = paramArrayOfInt[(j++)];

            m *= -862048943;
            m = Integer.rotateLeft(m, 15);
            m *= 461845907;

            i ^= m;
            i = Integer.rotateLeft(i, 13);
            i = i * 5 + -430675100;
        }

        i ^= paramInt3 * 4;

        i ^= i >>> 16;
        i *= -2048144789;
        i ^= i >>> 13;
        i *= -1028477387;
        i ^= i >>> 16;

        return i;
    }

    public static int stringHash32(String paramString) {
        return Holder.LANG_ACCESS.getStringHash32(paramString);
    }

    /**
     * 生成随机的Hash种子
     * @param paramObject
     * @return
     */
    public static int randomHashSeed(Object paramObject) {
        int i;
        if (VM.isBooted()) {
            // 如果VM已经启动，返回一个int即可
            i = ThreadLocalRandom.current().nextInt();
        } else {
            int[] arrayOfInt = {
                    System.identityHashCode(Hashing.class),
                    System.identityHashCode(paramObject),
                    System.identityHashCode(Thread.currentThread()),
                    (int) Thread.currentThread().getId(),
                    (int) (System.currentTimeMillis() >>> 2),
                    (int) (System.nanoTime() >>> 5),
                    (int) (Runtime.getRuntime().freeMemory() >>> 4)
            };

            i = murmur3_32(arrayOfInt);
        }

        return 0 != i ? i : 1;
    }

    private static class Holder {
        static final JavaLangAccess LANG_ACCESS = SharedSecrets.getJavaLangAccess();

        static {
            if (null == LANG_ACCESS)
                throw new Error("Shared secrets not initialized");
        }
    }
}
