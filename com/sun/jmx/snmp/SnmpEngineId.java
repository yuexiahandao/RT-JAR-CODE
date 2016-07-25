/*     */ package com.sun.jmx.snmp;
/*     */ 
/*     */ import com.sun.jmx.snmp.internal.SnmpTools;
/*     */ import java.io.Serializable;
/*     */ import java.net.InetAddress;
/*     */ import java.net.UnknownHostException;
/*     */ import java.util.Arrays;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.StringTokenizer;
/*     */ 
/*     */ public class SnmpEngineId
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 5434729655830763317L;
/*  45 */   byte[] engineId = null;
/*  46 */   String hexString = null;
/*  47 */   String humanString = null;
/*     */ 
/*     */   SnmpEngineId(String paramString)
/*     */   {
/*  53 */     this.engineId = SnmpTools.ascii2binary(paramString);
/*  54 */     this.hexString = paramString.toLowerCase();
/*     */   }
/*     */ 
/*     */   SnmpEngineId(byte[] paramArrayOfByte)
/*     */   {
/*  61 */     this.engineId = paramArrayOfByte;
/*  62 */     this.hexString = SnmpTools.binary2ascii(paramArrayOfByte).toLowerCase();
/*     */   }
/*     */ 
/*     */   public String getReadableId()
/*     */   {
/*  70 */     return this.humanString;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/*  78 */     return this.hexString;
/*     */   }
/*     */ 
/*     */   public byte[] getBytes()
/*     */   {
/*  85 */     return this.engineId;
/*     */   }
/*     */ 
/*     */   void setStringValue(String paramString)
/*     */   {
/*  92 */     this.humanString = paramString;
/*     */   }
/*     */ 
/*     */   static void validateId(String paramString) throws IllegalArgumentException {
/*  96 */     byte[] arrayOfByte = SnmpTools.ascii2binary(paramString);
/*  97 */     validateId(arrayOfByte);
/*     */   }
/*     */ 
/*     */   static void validateId(byte[] paramArrayOfByte) throws IllegalArgumentException
/*     */   {
/* 102 */     if (paramArrayOfByte.length < 5) throw new IllegalArgumentException("Id size lower than 5 bytes.");
/* 103 */     if (paramArrayOfByte.length > 32) throw new IllegalArgumentException("Id size greater than 32 bytes.");
/*     */ 
/* 106 */     if (((paramArrayOfByte[0] & 0x80) == 0) && (paramArrayOfByte.length != 12)) {
/* 107 */       throw new IllegalArgumentException("Very first bit = 0 and length != 12 octets");
/*     */     }
/* 109 */     byte[] arrayOfByte1 = new byte[paramArrayOfByte.length];
/* 110 */     if (Arrays.equals(arrayOfByte1, paramArrayOfByte)) throw new IllegalArgumentException("Zeroed Id.");
/* 111 */     byte[] arrayOfByte2 = new byte[paramArrayOfByte.length];
/* 112 */     Arrays.fill(arrayOfByte2, (byte)-1);
/* 113 */     if (Arrays.equals(arrayOfByte2, paramArrayOfByte)) throw new IllegalArgumentException("0xFF Id.");
/*     */   }
/*     */ 
/*     */   public static SnmpEngineId createEngineId(byte[] paramArrayOfByte)
/*     */     throws IllegalArgumentException
/*     */   {
/* 130 */     if ((paramArrayOfByte == null) || (paramArrayOfByte.length == 0)) return null;
/* 131 */     validateId(paramArrayOfByte);
/* 132 */     return new SnmpEngineId(paramArrayOfByte);
/*     */   }
/*     */ 
/*     */   public static SnmpEngineId createEngineId()
/*     */   {
/* 140 */     Object localObject = null;
/* 141 */     byte[] arrayOfByte = new byte[13];
/* 142 */     int i = 42;
/* 143 */     long l1 = 255L;
/* 144 */     long l2 = System.currentTimeMillis();
/*     */ 
/* 146 */     arrayOfByte[0] = ((byte)((i & 0xFF000000) >> 24));
/*     */     int tmp32_31 = 0;
/*     */     byte[] tmp32_30 = arrayOfByte; tmp32_30[tmp32_31] = ((byte)(tmp32_30[tmp32_31] | 0x80));
/* 148 */     arrayOfByte[1] = ((byte)((i & 0xFF0000) >> 16));
/* 149 */     arrayOfByte[2] = ((byte)((i & 0xFF00) >> 8));
/* 150 */     arrayOfByte[3] = ((byte)(i & 0xFF));
/* 151 */     arrayOfByte[4] = 5;
/*     */ 
/* 153 */     arrayOfByte[5] = ((byte)(int)((tmp32_31 & l1 << 56) >>> 56));
/* 154 */     arrayOfByte[6] = ((byte)(int)((tmp32_31 & l1 << 48) >>> 48));
/* 155 */     arrayOfByte[7] = ((byte)(int)((tmp32_31 & l1 << 40) >>> 40));
/* 156 */     arrayOfByte[8] = ((byte)(int)((tmp32_31 & l1 << 32) >>> 32));
/* 157 */     arrayOfByte[9] = ((byte)(int)((tmp32_31 & l1 << 24) >>> 24));
/* 158 */     arrayOfByte[10] = ((byte)(int)((tmp32_31 & l1 << 16) >>> 16));
/* 159 */     arrayOfByte[11] = ((byte)(int)((tmp32_31 & l1 << 8) >>> 8));
/* 160 */     arrayOfByte[12] = ((byte)(int)(tmp32_31 & l1));
/*     */ 
/* 162 */     return new SnmpEngineId(arrayOfByte);
/*     */   }
/*     */ 
/*     */   public SnmpOid toOid()
/*     */   {
/* 173 */     long[] arrayOfLong = new long[this.engineId.length + 1];
/* 174 */     arrayOfLong[0] = this.engineId.length;
/* 175 */     for (int i = 1; i <= this.engineId.length; i++)
/* 176 */       arrayOfLong[i] = (this.engineId[(i - 1)] & 0xFF);
/* 177 */     return new SnmpOid(arrayOfLong);
/*     */   }
/*     */ 
/*     */   public static SnmpEngineId createEngineId(String paramString)
/*     */     throws IllegalArgumentException, UnknownHostException
/*     */   {
/* 218 */     return createEngineId(paramString, null);
/*     */   }
/*     */ 
/*     */   public static SnmpEngineId createEngineId(String paramString1, String paramString2)
/*     */     throws IllegalArgumentException, UnknownHostException
/*     */   {
/* 246 */     if (paramString1 == null) return null;
/*     */ 
/* 248 */     if ((paramString1.startsWith("0x")) || (paramString1.startsWith("0X"))) {
/* 249 */       validateId(paramString1);
/* 250 */       return new SnmpEngineId(paramString1);
/*     */     }
/* 252 */     paramString2 = paramString2 == null ? ":" : paramString2;
/* 253 */     StringTokenizer localStringTokenizer = new StringTokenizer(paramString1, paramString2, true);
/*     */ 
/* 257 */     String str1 = null;
/* 258 */     String str2 = null;
/* 259 */     String str3 = null;
/* 260 */     int i = 161;
/* 261 */     int j = 42;
/* 262 */     InetAddress localInetAddress = null;
/* 263 */     SnmpEngineId localSnmpEngineId = null;
/*     */     try
/*     */     {
/*     */       try {
/* 267 */         str1 = localStringTokenizer.nextToken();
/*     */       } catch (NoSuchElementException localNoSuchElementException1) {
/* 269 */         throw new IllegalArgumentException("Passed string is invalid : [" + paramString1 + "]");
/*     */       }
/* 271 */       if (!str1.equals(paramString2)) {
/* 272 */         localInetAddress = InetAddress.getByName(str1);
/*     */         try {
/* 274 */           localStringTokenizer.nextToken();
/*     */         }
/*     */         catch (NoSuchElementException localNoSuchElementException2) {
/* 277 */           localSnmpEngineId = createEngineId(localInetAddress, i, j);
/*     */ 
/* 280 */           localSnmpEngineId.setStringValue(paramString1);
/* 281 */           return localSnmpEngineId;
/*     */         }
/*     */       }
/*     */       else {
/* 285 */         localInetAddress = InetAddress.getLocalHost();
/*     */       }
/*     */       try
/*     */       {
/* 289 */         str2 = localStringTokenizer.nextToken();
/*     */       }
/*     */       catch (NoSuchElementException localNoSuchElementException3) {
/* 292 */         localSnmpEngineId = createEngineId(localInetAddress, i, j);
/*     */ 
/* 295 */         localSnmpEngineId.setStringValue(paramString1);
/* 296 */         return localSnmpEngineId;
/*     */       }
/*     */ 
/* 299 */       if (!str2.equals(paramString2)) {
/* 300 */         i = Integer.parseInt(str2);
/*     */         try {
/* 302 */           localStringTokenizer.nextToken();
/*     */         }
/*     */         catch (NoSuchElementException localNoSuchElementException4) {
/* 305 */           localSnmpEngineId = createEngineId(localInetAddress, i, j);
/*     */ 
/* 308 */           localSnmpEngineId.setStringValue(paramString1);
/* 309 */           return localSnmpEngineId;
/*     */         }
/*     */       }
/*     */ 
/*     */       try
/*     */       {
/* 315 */         str3 = localStringTokenizer.nextToken();
/*     */       }
/*     */       catch (NoSuchElementException localNoSuchElementException5) {
/* 318 */         localSnmpEngineId = createEngineId(localInetAddress, i, j);
/*     */ 
/* 321 */         localSnmpEngineId.setStringValue(paramString1);
/* 322 */         return localSnmpEngineId;
/*     */       }
/*     */ 
/* 325 */       if (!str3.equals(paramString2)) {
/* 326 */         j = Integer.parseInt(str3);
/*     */       }
/* 328 */       localSnmpEngineId = createEngineId(localInetAddress, i, j);
/*     */ 
/* 331 */       localSnmpEngineId.setStringValue(paramString1);
/*     */ 
/* 333 */       return localSnmpEngineId;
/*     */     } catch (Exception localException) {
/*     */     }
/* 336 */     throw new IllegalArgumentException("Passed string is invalid : [" + paramString1 + "]. Check that the used separator [" + paramString2 + "] is compatible with IPv6 address format.");
/*     */   }
/*     */ 
/*     */   public static SnmpEngineId createEngineId(int paramInt)
/*     */     throws UnknownHostException
/*     */   {
/* 353 */     int i = 42;
/* 354 */     InetAddress localInetAddress = null;
/* 355 */     localInetAddress = InetAddress.getLocalHost();
/* 356 */     return createEngineId(localInetAddress, paramInt, i);
/*     */   }
/*     */ 
/*     */   public static SnmpEngineId createEngineId(InetAddress paramInetAddress, int paramInt)
/*     */     throws IllegalArgumentException
/*     */   {
/* 370 */     int i = 42;
/* 371 */     if (paramInetAddress == null)
/* 372 */       throw new IllegalArgumentException("InetAddress is null.");
/* 373 */     return createEngineId(paramInetAddress, paramInt, i);
/*     */   }
/*     */ 
/*     */   public static SnmpEngineId createEngineId(int paramInt1, int paramInt2)
/*     */     throws UnknownHostException
/*     */   {
/* 386 */     InetAddress localInetAddress = null;
/* 387 */     localInetAddress = InetAddress.getLocalHost();
/* 388 */     return createEngineId(localInetAddress, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public static SnmpEngineId createEngineId(InetAddress paramInetAddress, int paramInt1, int paramInt2)
/*     */   {
/* 402 */     if (paramInetAddress == null) throw new IllegalArgumentException("InetAddress is null.");
/* 403 */     byte[] arrayOfByte1 = paramInetAddress.getAddress();
/* 404 */     byte[] arrayOfByte2 = new byte[9 + arrayOfByte1.length];
/* 405 */     arrayOfByte2[0] = ((byte)((paramInt2 & 0xFF000000) >> 24));
/*     */     int tmp43_42 = 0;
/*     */     byte[] tmp43_40 = arrayOfByte2; tmp43_40[tmp43_42] = ((byte)(tmp43_40[tmp43_42] | 0x80));
/* 407 */     arrayOfByte2[1] = ((byte)((paramInt2 & 0xFF0000) >> 16));
/* 408 */     arrayOfByte2[2] = ((byte)((paramInt2 & 0xFF00) >> 8));
/*     */ 
/* 410 */     arrayOfByte2[3] = ((byte)(paramInt2 & 0xFF));
/* 411 */     arrayOfByte2[4] = 5;
/*     */ 
/* 413 */     if (arrayOfByte1.length == 4) {
/* 414 */       arrayOfByte2[4] = 1;
/*     */     }
/* 416 */     if (arrayOfByte1.length == 16) {
/* 417 */       arrayOfByte2[4] = 2;
/*     */     }
/* 419 */     for (int i = 0; i < arrayOfByte1.length; i++) {
/* 420 */       arrayOfByte2[(i + 5)] = arrayOfByte1[i];
/*     */     }
/*     */ 
/* 423 */     arrayOfByte2[(5 + arrayOfByte1.length)] = ((byte)((paramInt1 & 0xFF000000) >> 24));
/* 424 */     arrayOfByte2[(6 + arrayOfByte1.length)] = ((byte)((paramInt1 & 0xFF0000) >> 16));
/* 425 */     arrayOfByte2[(7 + arrayOfByte1.length)] = ((byte)((paramInt1 & 0xFF00) >> 8));
/* 426 */     arrayOfByte2[(8 + arrayOfByte1.length)] = ((byte)(paramInt1 & 0xFF));
/*     */ 
/* 428 */     return new SnmpEngineId(arrayOfByte2);
/*     */   }
/*     */ 
/*     */   public static SnmpEngineId createEngineId(int paramInt, InetAddress paramInetAddress)
/*     */   {
/* 441 */     if (paramInetAddress == null) throw new IllegalArgumentException("InetAddress is null.");
/* 442 */     byte[] arrayOfByte1 = paramInetAddress.getAddress();
/* 443 */     byte[] arrayOfByte2 = new byte[5 + arrayOfByte1.length];
/* 444 */     arrayOfByte2[0] = ((byte)((paramInt & 0xFF000000) >> 24));
/*     */     int tmp39_38 = 0;
/*     */     byte[] tmp39_37 = arrayOfByte2; tmp39_37[tmp39_38] = ((byte)(tmp39_37[tmp39_38] | 0x80));
/* 446 */     arrayOfByte2[1] = ((byte)((paramInt & 0xFF0000) >> 16));
/* 447 */     arrayOfByte2[2] = ((byte)((paramInt & 0xFF00) >> 8));
/*     */ 
/* 449 */     arrayOfByte2[3] = ((byte)(paramInt & 0xFF));
/* 450 */     if (arrayOfByte1.length == 4) {
/* 451 */       arrayOfByte2[4] = 1;
/*     */     }
/* 453 */     if (arrayOfByte1.length == 16) {
/* 454 */       arrayOfByte2[4] = 2;
/*     */     }
/* 456 */     for (int i = 0; i < arrayOfByte1.length; i++) {
/* 457 */       arrayOfByte2[(i + 5)] = arrayOfByte1[i];
/*     */     }
/*     */ 
/* 460 */     return new SnmpEngineId(arrayOfByte2);
/*     */   }
/*     */ 
/*     */   public static SnmpEngineId createEngineId(InetAddress paramInetAddress)
/*     */   {
/* 473 */     return createEngineId(42, paramInetAddress);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 482 */     if (!(paramObject instanceof SnmpEngineId)) return false;
/* 483 */     return this.hexString.equals(((SnmpEngineId)paramObject).toString());
/*     */   }
/*     */ 
/*     */   public int hashCode() {
/* 487 */     return this.hexString.hashCode();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.SnmpEngineId
 * JD-Core Version:    0.6.2
 */