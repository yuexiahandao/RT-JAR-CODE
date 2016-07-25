/*     */ package com.sun.jndi.ldap;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import sun.misc.HexDumpEncoder;
/*     */ 
/*     */ public abstract class Ber
/*     */ {
/*     */   protected byte[] buf;
/*     */   protected int offset;
/*     */   protected int bufsize;
/*     */   public static final int ASN_BOOLEAN = 1;
/*     */   public static final int ASN_INTEGER = 2;
/*     */   public static final int ASN_BIT_STRING = 3;
/*     */   public static final int ASN_SIMPLE_STRING = 4;
/*     */   public static final int ASN_OCTET_STR = 4;
/*     */   public static final int ASN_NULL = 5;
/*     */   public static final int ASN_OBJECT_ID = 6;
/*     */   public static final int ASN_SEQUENCE = 16;
/*     */   public static final int ASN_SET = 17;
/*     */   public static final int ASN_PRIMITIVE = 0;
/*     */   public static final int ASN_UNIVERSAL = 0;
/*     */   public static final int ASN_CONSTRUCTOR = 32;
/*     */   public static final int ASN_APPLICATION = 64;
/*     */   public static final int ASN_CONTEXT = 128;
/*     */   public static final int ASN_PRIVATE = 192;
/*     */   public static final int ASN_ENUMERATED = 10;
/*     */ 
/*     */   public static void dumpBER(OutputStream paramOutputStream, String paramString, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */   {
/*     */     try
/*     */     {
/*  52 */       paramOutputStream.write(10);
/*  53 */       paramOutputStream.write(paramString.getBytes("UTF8"));
/*     */ 
/*  55 */       new HexDumpEncoder().encodeBuffer(new ByteArrayInputStream(paramArrayOfByte, paramInt1, paramInt2), paramOutputStream);
/*     */ 
/*  59 */       paramOutputStream.write(10);
/*     */     } catch (IOException localIOException1) {
/*     */       try {
/*  62 */         paramOutputStream.write("Ber.dumpBER(): error encountered\n".getBytes("UTF8"));
/*     */       }
/*     */       catch (IOException localIOException2)
/*     */       {
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   static final class DecodeException extends IOException
/*     */   {
/*     */     DecodeException(String paramString)
/*     */     {
/* 104 */       super();
/*     */     }
/*     */   }
/*     */ 
/*     */   static final class EncodeException extends IOException
/*     */   {
/*     */     EncodeException(String paramString)
/*     */     {
/*  98 */       super();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.ldap.Ber
 * JD-Core Version:    0.6.2
 */