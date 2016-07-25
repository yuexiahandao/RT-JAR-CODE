/*     */ package com.sun.jmx.snmp;
/*     */ 
/*     */ public class SnmpStringFixed extends SnmpString
/*     */ {
/*     */   private static final long serialVersionUID = -9120939046874646063L;
/*     */ 
/*     */   public SnmpStringFixed(byte[] paramArrayOfByte)
/*     */   {
/*  54 */     super(paramArrayOfByte);
/*     */   }
/*     */ 
/*     */   public SnmpStringFixed(Byte[] paramArrayOfByte)
/*     */   {
/*  62 */     super(paramArrayOfByte);
/*     */   }
/*     */ 
/*     */   public SnmpStringFixed(String paramString)
/*     */   {
/*  70 */     super(paramString);
/*     */   }
/*     */ 
/*     */   public SnmpStringFixed(int paramInt, byte[] paramArrayOfByte)
/*     */     throws IllegalArgumentException
/*     */   {
/*  81 */     if ((paramInt <= 0) || (paramArrayOfByte == null)) {
/*  82 */       throw new IllegalArgumentException();
/*     */     }
/*  84 */     int i = Math.min(paramInt, paramArrayOfByte.length);
/*  85 */     this.value = new byte[paramInt];
/*  86 */     for (int j = 0; j < i; j++) {
/*  87 */       this.value[j] = paramArrayOfByte[j];
/*     */     }
/*  89 */     for (j = i; j < paramInt; j++)
/*  90 */       this.value[j] = 0;
/*     */   }
/*     */ 
/*     */   public SnmpStringFixed(int paramInt, Byte[] paramArrayOfByte)
/*     */     throws IllegalArgumentException
/*     */   {
/* 102 */     if ((paramInt <= 0) || (paramArrayOfByte == null)) {
/* 103 */       throw new IllegalArgumentException();
/*     */     }
/* 105 */     int i = Math.min(paramInt, paramArrayOfByte.length);
/* 106 */     this.value = new byte[paramInt];
/* 107 */     for (int j = 0; j < i; j++) {
/* 108 */       this.value[j] = paramArrayOfByte[j].byteValue();
/*     */     }
/* 110 */     for (j = i; j < paramInt; j++)
/* 111 */       this.value[j] = 0;
/*     */   }
/*     */ 
/*     */   public SnmpStringFixed(int paramInt, String paramString)
/*     */     throws IllegalArgumentException
/*     */   {
/* 123 */     if ((paramInt <= 0) || (paramString == null)) {
/* 124 */       throw new IllegalArgumentException();
/*     */     }
/* 126 */     byte[] arrayOfByte = paramString.getBytes();
/* 127 */     int i = Math.min(paramInt, arrayOfByte.length);
/* 128 */     this.value = new byte[paramInt];
/* 129 */     for (int j = 0; j < i; j++) {
/* 130 */       this.value[j] = arrayOfByte[j];
/*     */     }
/* 132 */     for (j = i; j < paramInt; j++)
/* 133 */       this.value[j] = 0;
/*     */   }
/*     */ 
/*     */   public static SnmpOid toOid(int paramInt1, long[] paramArrayOfLong, int paramInt2)
/*     */     throws SnmpStatusException
/*     */   {
/*     */     try
/*     */     {
/* 153 */       long[] arrayOfLong = new long[paramInt1];
/* 154 */       for (int i = 0; i < paramInt1; i++) {
/* 155 */         arrayOfLong[i] = paramArrayOfLong[(paramInt2 + i)];
/*     */       }
/* 157 */       return new SnmpOid(arrayOfLong);
/*     */     } catch (IndexOutOfBoundsException localIndexOutOfBoundsException) {
/*     */     }
/* 160 */     throw new SnmpStatusException(2);
/*     */   }
/*     */ 
/*     */   public static int nextOid(int paramInt1, long[] paramArrayOfLong, int paramInt2)
/*     */     throws SnmpStatusException
/*     */   {
/* 177 */     int i = paramInt2 + paramInt1;
/* 178 */     if (i > paramArrayOfLong.length) {
/* 179 */       throw new SnmpStatusException(2);
/*     */     }
/* 181 */     return i;
/*     */   }
/*     */ 
/*     */   public static void appendToOid(int paramInt, SnmpOid paramSnmpOid1, SnmpOid paramSnmpOid2)
/*     */   {
/* 191 */     paramSnmpOid2.append(paramSnmpOid1);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.SnmpStringFixed
 * JD-Core Version:    0.6.2
 */