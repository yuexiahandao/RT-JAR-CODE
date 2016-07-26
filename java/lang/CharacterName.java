/*     */
package java.lang;
/*     */ 
/*     */

import java.io.DataInputStream;
/*     */ import java.io.InputStream;
/*     */ import java.lang.ref.SoftReference;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.zip.InflaterInputStream;

/*     */
/*     */ class CharacterName
/*     */ {
    /*     */   private static SoftReference<byte[]> refStrPool;
    /*     */   private static int[][] lookup;

    /*     */
/*     */
    private static synchronized byte[] initNamePool()
/*     */ {
/*  42 */
        byte[] arrayOfByte1 = null;
/*  43 */
        if ((refStrPool != null) && ((arrayOfByte1 = (byte[]) refStrPool.get()) != null))
/*  44 */ return arrayOfByte1;
/*  45 */
        DataInputStream localDataInputStream = null;
/*     */
        try {
/*  47 */
            localDataInputStream = new DataInputStream(new InflaterInputStream((InputStream) AccessController.doPrivileged(new PrivilegedAction()
/*     */ {
                /*     */
                public InputStream run()
/*     */ {
/*  51 */
                    return getClass().getResourceAsStream("uniName.dat");
/*     */
                }
/*     */
            })));
/*  55 */
            lookup = new int[4352][];
/*  56 */
            int i = localDataInputStream.readInt();
/*  57 */
            int j = localDataInputStream.readInt();
/*  58 */
            byte[] arrayOfByte2 = new byte[j];
/*  59 */
            localDataInputStream.readFully(arrayOfByte2);
/*     */ 
/*  61 */
            int k = 0;
/*  62 */
            int m = 0;
/*  63 */
            int n = 0;
/*     */
            do {
/*  65 */
                int i1 = arrayOfByte2[(m++)] & 0xFF;
/*  66 */
                if (i1 == 0) {
/*  67 */
                    i1 = arrayOfByte2[(m++)] & 0xFF;
/*     */ 
/*  69 */
                    n = (arrayOfByte2[(m++)] & 0xFF) << 16 | (arrayOfByte2[(m++)] & 0xFF) << 8 | arrayOfByte2[(m++)] & 0xFF;
/*     */
                }
/*     */
                else
/*     */ {
/*  73 */
                    n++;
/*     */
                }
/*  75 */
                int i2 = n >> 8;
/*  76 */
                if (lookup[i2] == null) {
/*  77 */
                    lookup[i2] = new int[256];
/*     */
                }
/*  79 */
                lookup[i2][(n & 0xFF)] = (k << 8 | i1);
/*  80 */
                k += i1;
/*  81 */
            } while (m < j);
/*  82 */
            arrayOfByte1 = new byte[i - j];
/*  83 */
            localDataInputStream.readFully(arrayOfByte1);
/*  84 */
            refStrPool = new SoftReference(arrayOfByte1);
/*     */
        } catch (Exception localException2) {
/*  86 */
            throw new InternalError(localException2.getMessage());
/*     */
        } finally {
/*     */
            try {
/*  89 */
                if (localDataInputStream != null)
/*  90 */ localDataInputStream.close();
/*     */
            } catch (Exception localException3) {
            }
/*     */ 
/*     */
        }
/*  93 */
        return arrayOfByte1;
/*     */
    }

    /*     */
/*     */
    public static String get(int paramInt) {
/*  97 */
        byte[] arrayOfByte = null;
/*  98 */
        if ((refStrPool == null) || ((arrayOfByte = (byte[]) refStrPool.get()) == null))
/*  99 */ arrayOfByte = initNamePool();
/* 100 */
        int i = 0;
/* 101 */
        if ((lookup[(paramInt >> 8)] == null) || ((i = lookup[(paramInt >> 8)][(paramInt & 0xFF)]) == 0))
/*     */ {
/* 103 */
            return null;
/* 104 */
        }
        return new String(arrayOfByte, 0, i >>> 8, i & 0xFF);
/*     */
    }
/*     */
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.CharacterName
 * JD-Core Version:    0.6.2
 */