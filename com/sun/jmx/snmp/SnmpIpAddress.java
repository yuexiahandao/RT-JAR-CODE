/*     */ package com.sun.jmx.snmp;
/*     */ 
/*     */ public class SnmpIpAddress extends SnmpOid
/*     */ {
/*     */   private static final long serialVersionUID = 7204629998270874474L;
/*     */   static final String name = "IpAddress";
/*     */ 
/*     */   public SnmpIpAddress(byte[] paramArrayOfByte)
/*     */     throws IllegalArgumentException
/*     */   {
/*  50 */     buildFromByteArray(paramArrayOfByte);
/*     */   }
/*     */ 
/*     */   public SnmpIpAddress(long paramLong)
/*     */   {
/*  58 */     int i = (int)paramLong;
/*  59 */     byte[] arrayOfByte = new byte[4];
/*     */ 
/*  61 */     arrayOfByte[0] = ((byte)(i >>> 24 & 0xFF));
/*  62 */     arrayOfByte[1] = ((byte)(i >>> 16 & 0xFF));
/*  63 */     arrayOfByte[2] = ((byte)(i >>> 8 & 0xFF));
/*  64 */     arrayOfByte[3] = ((byte)(i & 0xFF));
/*     */ 
/*  66 */     buildFromByteArray(arrayOfByte);
/*     */   }
/*     */ 
/*     */   public SnmpIpAddress(String paramString)
/*     */     throws IllegalArgumentException
/*     */   {
/*  76 */     super(paramString);
/*  77 */     if ((this.componentCount > 4) || (this.components[0] > 255L) || (this.components[1] > 255L) || (this.components[2] > 255L) || (this.components[3] > 255L))
/*     */     {
/*  82 */       throw new IllegalArgumentException(paramString);
/*     */     }
/*     */   }
/*     */ 
/*     */   public SnmpIpAddress(long paramLong1, long paramLong2, long paramLong3, long paramLong4)
/*     */   {
/*  95 */     super(paramLong1, paramLong2, paramLong3, paramLong4);
/*  96 */     if ((this.components[0] > 255L) || (this.components[1] > 255L) || (this.components[2] > 255L) || (this.components[3] > 255L))
/*     */     {
/* 100 */       throw new IllegalArgumentException();
/*     */     }
/*     */   }
/*     */ 
/*     */   public byte[] byteValue()
/*     */   {
/* 111 */     byte[] arrayOfByte = new byte[4];
/* 112 */     arrayOfByte[0] = ((byte)(int)this.components[0]);
/* 113 */     arrayOfByte[1] = ((byte)(int)this.components[1]);
/* 114 */     arrayOfByte[2] = ((byte)(int)this.components[2]);
/* 115 */     arrayOfByte[3] = ((byte)(int)this.components[3]);
/*     */ 
/* 117 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   public String stringValue()
/*     */   {
/* 126 */     return toString();
/*     */   }
/*     */ 
/*     */   public static SnmpOid toOid(long[] paramArrayOfLong, int paramInt)
/*     */     throws SnmpStatusException
/*     */   {
/* 139 */     if (paramInt + 4 <= paramArrayOfLong.length) {
/*     */       try {
/* 141 */         return new SnmpOid(paramArrayOfLong[paramInt], paramArrayOfLong[(paramInt + 1)], paramArrayOfLong[(paramInt + 2)], paramArrayOfLong[(paramInt + 3)]);
/*     */       }
/*     */       catch (IllegalArgumentException localIllegalArgumentException)
/*     */       {
/* 148 */         throw new SnmpStatusException(2);
/*     */       }
/*     */     }
/*     */ 
/* 152 */     throw new SnmpStatusException(2);
/*     */   }
/*     */ 
/*     */   public static int nextOid(long[] paramArrayOfLong, int paramInt)
/*     */     throws SnmpStatusException
/*     */   {
/* 166 */     if (paramInt + 4 <= paramArrayOfLong.length) {
/* 167 */       return paramInt + 4;
/*     */     }
/*     */ 
/* 170 */     throw new SnmpStatusException(2);
/*     */   }
/*     */ 
/*     */   public static void appendToOid(SnmpOid paramSnmpOid1, SnmpOid paramSnmpOid2)
/*     */   {
/* 180 */     if (paramSnmpOid1.getLength() != 4) {
/* 181 */       throw new IllegalArgumentException();
/*     */     }
/* 183 */     paramSnmpOid2.append(paramSnmpOid1);
/*     */   }
/*     */ 
/*     */   public final String getTypeName()
/*     */   {
/* 191 */     return "IpAddress";
/*     */   }
/*     */ 
/*     */   private void buildFromByteArray(byte[] paramArrayOfByte)
/*     */   {
/* 200 */     if (paramArrayOfByte.length != 4) {
/* 201 */       throw new IllegalArgumentException();
/*     */     }
/* 203 */     this.components = new long[4];
/* 204 */     this.componentCount = 4;
/* 205 */     this.components[0] = (paramArrayOfByte[0] >= 0 ? paramArrayOfByte[0] : paramArrayOfByte[0] + 256);
/* 206 */     this.components[1] = (paramArrayOfByte[1] >= 0 ? paramArrayOfByte[1] : paramArrayOfByte[1] + 256);
/* 207 */     this.components[2] = (paramArrayOfByte[2] >= 0 ? paramArrayOfByte[2] : paramArrayOfByte[2] + 256);
/* 208 */     this.components[3] = (paramArrayOfByte[3] >= 0 ? paramArrayOfByte[3] : paramArrayOfByte[3] + 256);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.SnmpIpAddress
 * JD-Core Version:    0.6.2
 */