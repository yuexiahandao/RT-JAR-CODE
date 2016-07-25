/*     */ package com.sun.jmx.snmp.internal;
/*     */ 
/*     */ import com.sun.jmx.snmp.SnmpDefinitions;
/*     */ 
/*     */ public class SnmpTools
/*     */   implements SnmpDefinitions
/*     */ {
/*     */   public static String binary2ascii(byte[] paramArrayOfByte, int paramInt)
/*     */   {
/*  43 */     if (paramArrayOfByte == null) return null;
/*  44 */     int i = paramInt * 2 + 2;
/*  45 */     byte[] arrayOfByte = new byte[i];
/*  46 */     arrayOfByte[0] = 48;
/*  47 */     arrayOfByte[1] = 120;
/*  48 */     for (int j = 0; j < paramInt; j++) {
/*  49 */       int k = j * 2;
/*  50 */       int m = paramArrayOfByte[j] & 0xF0;
/*  51 */       m >>= 4;
/*  52 */       if (m < 10)
/*  53 */         arrayOfByte[(k + 2)] = ((byte)(48 + m));
/*     */       else
/*  55 */         arrayOfByte[(k + 2)] = ((byte)(65 + (m - 10)));
/*  56 */       m = paramArrayOfByte[j] & 0xF;
/*  57 */       if (m < 10)
/*  58 */         arrayOfByte[(k + 1 + 2)] = ((byte)(48 + m));
/*     */       else
/*  60 */         arrayOfByte[(k + 1 + 2)] = ((byte)(65 + (m - 10)));
/*     */     }
/*  62 */     return new String(arrayOfByte);
/*     */   }
/*     */ 
/*     */   public static String binary2ascii(byte[] paramArrayOfByte)
/*     */   {
/*  72 */     return binary2ascii(paramArrayOfByte, paramArrayOfByte.length);
/*     */   }
/*     */ 
/*     */   public static byte[] ascii2binary(String paramString)
/*     */   {
/*  80 */     if (paramString == null) return null;
/*  81 */     String str = paramString.substring(2);
/*     */ 
/*  83 */     int i = str.length();
/*  84 */     byte[] arrayOfByte1 = new byte[i / 2];
/*  85 */     byte[] arrayOfByte2 = str.getBytes();
/*     */ 
/*  87 */     for (int j = 0; j < i / 2; j++)
/*     */     {
/*  89 */       int k = j * 2;
/*  90 */       int m = 0;
/*  91 */       if ((arrayOfByte2[k] >= 48) && (arrayOfByte2[k] <= 57)) {
/*  92 */         m = (byte)(arrayOfByte2[k] - 48 << 4);
/*     */       }
/*  94 */       else if ((arrayOfByte2[k] >= 97) && (arrayOfByte2[k] <= 102)) {
/*  95 */         m = (byte)(arrayOfByte2[k] - 97 + 10 << 4);
/*     */       }
/*  97 */       else if ((arrayOfByte2[k] >= 65) && (arrayOfByte2[k] <= 70)) {
/*  98 */         m = (byte)(arrayOfByte2[k] - 65 + 10 << 4);
/*     */       }
/*     */       else {
/* 101 */         throw new Error("BAD format :" + paramString);
/*     */       }
/* 103 */       if ((arrayOfByte2[(k + 1)] >= 48) && (arrayOfByte2[(k + 1)] <= 57))
/*     */       {
/* 105 */         m = (byte)(m + (arrayOfByte2[(k + 1)] - 48));
/*     */       }
/* 108 */       else if ((arrayOfByte2[(k + 1)] >= 97) && (arrayOfByte2[(k + 1)] <= 102))
/*     */       {
/* 110 */         m = (byte)(m + (arrayOfByte2[(k + 1)] - 97 + 10));
/*     */       }
/* 113 */       else if ((arrayOfByte2[(k + 1)] >= 65) && (arrayOfByte2[(k + 1)] <= 70))
/*     */       {
/* 115 */         m = (byte)(m + (arrayOfByte2[(k + 1)] - 65 + 10));
/*     */       }
/*     */       else
/*     */       {
/* 119 */         throw new Error("BAD format :" + paramString);
/*     */       }
/* 121 */       arrayOfByte1[j] = m;
/*     */     }
/* 123 */     return arrayOfByte1;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.internal.SnmpTools
 * JD-Core Version:    0.6.2
 */