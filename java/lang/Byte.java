/*     */ package java.lang;
/*     */ 
/*     */ public final class Byte extends Number
/*     */   implements Comparable<Byte>
/*     */ {
/*     */   public static final byte MIN_VALUE = -128;
/*     */   public static final byte MAX_VALUE = 127;
/*  62 */   public static final Class<Byte> TYPE = Class.getPrimitiveClass("byte");
/*     */   private final byte value;
/*     */   public static final int SIZE = 8;
/*     */   private static final long serialVersionUID = -7183698231559129828L;
/*     */ 
/*     */   public static String toString(byte paramByte)
/*     */   {
/*  73 */     return Integer.toString(paramByte, 10);
/*     */   }
/*     */ 
/*     */   public static Byte valueOf(byte paramByte)
/*     */   {
/* 102 */     return ByteCache.cache[(paramByte + 128)];
/*     */   }
/*     */ 
/*     */   public static byte parseByte(String paramString, int paramInt)
/*     */     throws NumberFormatException
/*     */   {
/* 148 */     int i = Integer.parseInt(paramString, paramInt);
/* 149 */     if ((i < -128) || (i > 127)) {
/* 150 */       throw new NumberFormatException("Value out of range. Value:\"" + paramString + "\" Radix:" + paramInt);
/*     */     }
/* 152 */     return (byte)i;
/*     */   }
/*     */ 
/*     */   public static byte parseByte(String paramString)
/*     */     throws NumberFormatException
/*     */   {
/* 174 */     return parseByte(paramString, 10);
/*     */   }
/*     */ 
/*     */   public static Byte valueOf(String paramString, int paramInt)
/*     */     throws NumberFormatException
/*     */   {
/* 204 */     return valueOf(parseByte(paramString, paramInt));
/*     */   }
/*     */ 
/*     */   public static Byte valueOf(String paramString)
/*     */     throws NumberFormatException
/*     */   {
/* 230 */     return valueOf(paramString, 10);
/*     */   }
/*     */ 
/*     */   public static Byte decode(String paramString)
/*     */     throws NumberFormatException
/*     */   {
/* 276 */     int i = Integer.decode(paramString).intValue();
/* 277 */     if ((i < -128) || (i > 127)) {
/* 278 */       throw new NumberFormatException("Value " + i + " out of range from input " + paramString);
/*     */     }
/* 280 */     return valueOf((byte)i);
/*     */   }
/*     */ 
/*     */   public Byte(byte paramByte)
/*     */   {
/* 298 */     this.value = paramByte;
/*     */   }
/*     */ 
/*     */   public Byte(String paramString)
/*     */     throws NumberFormatException
/*     */   {
/* 315 */     this.value = parseByte(paramString, 10);
/*     */   }
/*     */ 
/*     */   public byte byteValue()
/*     */   {
/* 323 */     return this.value;
/*     */   }
/*     */ 
/*     */   public short shortValue()
/*     */   {
/* 331 */     return (short)this.value;
/*     */   }
/*     */ 
/*     */   public int intValue()
/*     */   {
/* 339 */     return this.value;
/*     */   }
/*     */ 
/*     */   public long longValue()
/*     */   {
/* 347 */     return this.value;
/*     */   }
/*     */ 
/*     */   public float floatValue()
/*     */   {
/* 355 */     return this.value;
/*     */   }
/*     */ 
/*     */   public double doubleValue()
/*     */   {
/* 363 */     return this.value;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 377 */     return Integer.toString(this.value);
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 387 */     return this.value;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 401 */     if ((paramObject instanceof Byte)) {
/* 402 */       return this.value == ((Byte)paramObject).byteValue();
/*     */     }
/* 404 */     return false;
/*     */   }
/*     */ 
/*     */   public int compareTo(Byte paramByte)
/*     */   {
/* 421 */     return compare(this.value, paramByte.value);
/*     */   }
/*     */ 
/*     */   public static int compare(byte paramByte1, byte paramByte2)
/*     */   {
/* 439 */     return paramByte1 - paramByte2;
/*     */   }
/*     */ 
/*     */   private static class ByteCache
/*     */   {
/*  79 */     static final Byte[] cache = new Byte[256];
/*     */ 
/*     */     static {
/*  82 */       for (int i = 0; i < cache.length; i++)
/*  83 */         cache[i] = new Byte((byte)(i - 128));
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.Byte
 * JD-Core Version:    0.6.2
 */