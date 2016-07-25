/*     */ package sun.security.krb5.internal.crypto;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.security.DigestException;
/*     */ import java.security.MessageDigestSpi;
/*     */ import sun.security.krb5.internal.Krb5;
/*     */ 
/*     */ public final class crc32 extends MessageDigestSpi
/*     */   implements Cloneable
/*     */ {
/*     */   private static final int CRC32_LENGTH = 4;
/*     */   private int seed;
/*  39 */   private static boolean DEBUG = Krb5.DEBUG;
/*     */ 
/* 119 */   private static int[] crc32Table = { 0, 1996959894, -301047508, -1727442502, 124634137, 1886057615, -379345611, -1637575261, 249268274, 2044508324, -522852066, -1747789432, 162941995, 2125561021, -407360249, -1866523247, 498536548, 1789927666, -205950648, -2067906082, 450548861, 1843258603, -187386543, -2083289657, 325883990, 1684777152, -43845254, -1973040660, 335633487, 1661365465, -99664541, -1928851979, 997073096, 1281953886, -715111964, -1570279054, 1006888145, 1258607687, -770865667, -1526024853, 901097722, 1119000684, -608450090, -1396901568, 853044451, 1172266101, -589951537, -1412350631, 651767980, 1373503546, -925412992, -1076862698, 565507253, 1454621731, -809855591, -1195530993, 671266974, 1594198024, -972236366, -1324619484, 795835527, 1483230225, -1050600021, -1234817731, 1994146192, 31158534, -1731059524, -271249366, 1907459465, 112637215, -1614814043, -390540237, 2013776290, 251722036, -1777751922, -519137256, 2137656763, 141376813, -1855689577, -429695999, 1802195444, 476864866, -2056965928, -228458418, 1812370925, 453092731, -2113342271, -183516073, 1706088902, 314042704, -1950435094, -54949764, 1658658271, 366619977, -1932296973, -69972891, 1303535960, 984961486, -1547960204, -725929758, 1256170817, 1037604311, -1529756563, -740887301, 1131014506, 879679996, -1385723834, -631195440, 1141124467, 855842277, -1442165665, -586318647, 1342533948, 654459306, -1106571248, -921952122, 1466479909, 544179635, -1184443383, -832445281, 1591671054, 702138776, -1328506846, -942167884, 1504918807, 783551873, -1212326853, -1061524307, -306674912, -1698712650, 62317068, 1957810842, -355121351, -1647151185, 81470997, 1943803523, -480048366, -1805370492, 225274430, 2053790376, -468791541, -1828061283, 167816743, 2097651377, -267414716, -2029476910, 503444072, 1762050814, -144550051, -2140837941, 426522225, 1852507879, -19653770, -1982649376, 282753626, 1742555852, -105259153, -1900089351, 397917763, 1622183637, -690576408, -1580100738, 953729732, 1340076626, -776247311, -1497606297, 1068828381, 1219638859, -670225446, -1358292148, 906185462, 1090812512, -547295293, -1469587627, 829329135, 1181335161, -882789492, -1134132454, 628085408, 1382605366, -871598187, -1156888829, 570562233, 1426400815, -977650754, -1296233688, 733239954, 1555261956, -1026031705, -1244606671, 752459403, 1541320221, -1687895376, -328994266, 1969922972, 40735498, -1677130071, -351390145, 1913087877, 83908371, -1782625662, -491226604, 2075208622, 213261112, -1831694693, -438977011, 2094854071, 198958881, -2032938284, -237706686, 1759359992, 534414190, -2118248755, -155638181, 1873836001, 414664567, -2012718362, -15766928, 1711684554, 285281116, -1889165569, -127750551, 1634467795, 376229701, -1609899400, -686959890, 1308918612, 956543938, -1486412191, -799009033, 1231636301, 1047427035, -1362007478, -640263460, 1088359270, 936918000, -1447252397, -558129467, 1202900863, 817233897, -1111625188, -893730166, 1404277552, 615818150, -1160759803, -841546093, 1423857449, 601450431, -1285129682, -1000256840, 1567103746, 711928724, -1274298825, -1022587231, 1510334235, 755167117 };
/*     */ 
/*     */   public crc32()
/*     */   {
/*  44 */     init();
/*     */   }
/*     */ 
/*     */   public Object clone() {
/*     */     try {
/*  49 */       crc32 localcrc32 = (crc32)super.clone();
/*  50 */       localcrc32.init();
/*  51 */       return localcrc32;
/*     */     }
/*     */     catch (CloneNotSupportedException localCloneNotSupportedException) {
/*     */     }
/*  55 */     return null;
/*     */   }
/*     */ 
/*     */   protected int engineGetDigestLength()
/*     */   {
/*  62 */     return 4;
/*     */   }
/*     */ 
/*     */   protected byte[] engineDigest()
/*     */   {
/*  68 */     byte[] arrayOfByte = new byte[4];
/*  69 */     arrayOfByte = int2quad(this.seed);
/*     */ 
/*  71 */     init();
/*  72 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   protected int engineDigest(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws DigestException
/*     */   {
/*  78 */     byte[] arrayOfByte = new byte[4];
/*  79 */     arrayOfByte = int2quad(this.seed);
/*  80 */     if (paramInt2 < 4) {
/*  81 */       throw new DigestException("partial digests not returned");
/*     */     }
/*  83 */     if (paramArrayOfByte.length - paramInt1 < 4) {
/*  84 */       throw new DigestException("insufficient space in the output buffer to store the digest");
/*     */     }
/*     */ 
/*  87 */     System.arraycopy(arrayOfByte, 0, paramArrayOfByte, paramInt1, 4);
/*     */ 
/*  97 */     init();
/*  98 */     return 4;
/*     */   }
/*     */ 
/*     */   protected synchronized void engineUpdate(byte paramByte)
/*     */   {
/* 104 */     byte[] arrayOfByte = new byte[1];
/* 105 */     arrayOfByte[0] = paramByte;
/*     */ 
/* 107 */     engineUpdate(arrayOfByte, this.seed, 1);
/*     */   }
/*     */ 
/*     */   protected synchronized void engineUpdate(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */   {
/* 117 */     processData(paramArrayOfByte, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   protected void engineReset()
/*     */   {
/* 187 */     init();
/*     */   }
/*     */ 
/*     */   public void init()
/*     */   {
/* 194 */     this.seed = 0;
/*     */   }
/*     */ 
/*     */   private void processData(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {
/* 198 */     int i = this.seed;
/* 199 */     for (int j = 0; j < paramInt2; j++)
/* 200 */       i = i >>> 8 ^ crc32Table[((i ^ paramArrayOfByte[j]) & 0xFF)];
/* 201 */     this.seed = i;
/*     */   }
/*     */ 
/*     */   public static int int2crc32(int paramInt) {
/* 205 */     int i = paramInt;
/*     */ 
/* 207 */     for (int j = 8; j > 0; j--) {
/* 208 */       if ((i & 0x1) != 0)
/* 209 */         i = i >>> 1 ^ 0xEDB88320;
/*     */       else
/* 211 */         i >>>= 1;
/*     */     }
/* 213 */     return i;
/*     */   }
/*     */ 
/*     */   public static void printcrc32Table()
/*     */   {
/* 218 */     String str2 = "00000000";
/*     */ 
/* 220 */     System.out.print("\tpublic static int[] crc32Table = {");
/* 221 */     for (int i = 0; i < 256; i++) {
/* 222 */       if (i % 4 == 0)
/* 223 */         System.out.print("\n\t\t");
/* 224 */       String str1 = Integer.toHexString(int2crc32(i));
/* 225 */       System.out.print("0x" + str2.substring(str1.length()) + str1);
/*     */ 
/* 227 */       if (i != 255)
/* 228 */         System.out.print(", ");
/*     */     }
/* 230 */     System.out.println("\n\t};");
/*     */   }
/*     */ 
/*     */   public static int byte2crc32sum(int paramInt1, byte[] paramArrayOfByte, int paramInt2) {
/* 234 */     int i = paramInt1;
/*     */ 
/* 236 */     for (int j = 0; j < paramInt2; j++)
/* 237 */       i = i >>> 8 ^ crc32Table[((i ^ paramArrayOfByte[j]) & 0xFF)];
/* 238 */     return i;
/*     */   }
/*     */ 
/*     */   public static int byte2crc32sum(int paramInt, byte[] paramArrayOfByte) {
/* 242 */     return byte2crc32sum(paramInt, paramArrayOfByte, paramArrayOfByte.length);
/*     */   }
/*     */ 
/*     */   public static int byte2crc32sum(byte[] paramArrayOfByte)
/*     */   {
/* 247 */     return byte2crc32sum(0, paramArrayOfByte);
/*     */   }
/*     */ 
/*     */   public static int byte2crc32(byte[] paramArrayOfByte)
/*     */   {
/* 252 */     return byte2crc32sum(-1, paramArrayOfByte) ^ 0xFFFFFFFF;
/*     */   }
/*     */ 
/*     */   public static byte[] byte2crc32sum_bytes(byte[] paramArrayOfByte) {
/* 256 */     int i = byte2crc32sum(paramArrayOfByte);
/* 257 */     return int2quad(i);
/*     */   }
/*     */ 
/*     */   public static byte[] byte2crc32sum_bytes(byte[] paramArrayOfByte, int paramInt) {
/* 261 */     int i = byte2crc32sum(0, paramArrayOfByte, paramInt);
/* 262 */     if (DEBUG) {
/* 263 */       System.out.println(">>>crc32: " + Integer.toHexString(i));
/* 264 */       System.out.println(">>>crc32: " + Integer.toBinaryString(i));
/*     */     }
/* 266 */     return int2quad(i);
/*     */   }
/*     */ 
/*     */   public static byte[] int2quad(long paramLong) {
/* 270 */     byte[] arrayOfByte = new byte[4];
/* 271 */     for (int i = 0; i < 4; i++) {
/* 272 */       arrayOfByte[i] = ((byte)(int)(paramLong >>> i * 8 & 0xFF));
/*     */     }
/* 274 */     return arrayOfByte;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.internal.crypto.crc32
 * JD-Core Version:    0.6.2
 */