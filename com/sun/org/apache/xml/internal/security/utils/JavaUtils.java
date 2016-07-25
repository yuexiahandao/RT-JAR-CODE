/*     */ package com.sun.org.apache.xml.internal.security.utils;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.security.SecurityPermission;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ public class JavaUtils
/*     */ {
/*  38 */   static Logger log = Logger.getLogger(JavaUtils.class.getName());
/*     */ 
/*  41 */   private static final SecurityPermission REGISTER_PERMISSION = new SecurityPermission("com.sun.org.apache.xml.internal.security.register");
/*     */ 
/*     */   public static byte[] getBytesFromFile(String paramString)
/*     */     throws FileNotFoundException, IOException
/*     */   {
/*  61 */     byte[] arrayOfByte1 = null;
/*     */ 
/*  63 */     FileInputStream localFileInputStream = new FileInputStream(paramString);
/*     */     try {
/*  65 */       UnsyncByteArrayOutputStream localUnsyncByteArrayOutputStream = new UnsyncByteArrayOutputStream();
/*  66 */       byte[] arrayOfByte2 = new byte[1024];
/*     */       int i;
/*  69 */       while ((i = localFileInputStream.read(arrayOfByte2)) > 0) {
/*  70 */         localUnsyncByteArrayOutputStream.write(arrayOfByte2, 0, i);
/*     */       }
/*     */ 
/*  73 */       arrayOfByte1 = localUnsyncByteArrayOutputStream.toByteArray();
/*     */     } finally {
/*  75 */       localFileInputStream.close();
/*     */     }
/*     */ 
/*  78 */     return arrayOfByte1;
/*     */   }
/*     */ 
/*     */   public static void writeBytesToFilename(String paramString, byte[] paramArrayOfByte)
/*     */   {
/*  89 */     FileOutputStream localFileOutputStream = null;
/*     */     try {
/*  91 */       if ((paramString != null) && (paramArrayOfByte != null)) {
/*  92 */         File localFile = new File(paramString);
/*     */ 
/*  94 */         localFileOutputStream = new FileOutputStream(localFile);
/*     */ 
/*  96 */         localFileOutputStream.write(paramArrayOfByte);
/*  97 */         localFileOutputStream.close();
/*     */       } else {
/*  99 */         log.log(Level.FINE, "writeBytesToFilename got null byte[] pointed");
/*     */       }
/*     */     } catch (IOException localIOException1) {
/* 102 */       if (localFileOutputStream != null)
/*     */         try {
/* 104 */           localFileOutputStream.close();
/*     */         }
/*     */         catch (IOException localIOException2)
/*     */         {
/*     */         }
/*     */     }
/*     */   }
/*     */ 
/*     */   public static byte[] getBytesFromStream(InputStream paramInputStream)
/*     */     throws IOException
/*     */   {
/* 123 */     byte[] arrayOfByte1 = null;
/*     */ 
/* 125 */     UnsyncByteArrayOutputStream localUnsyncByteArrayOutputStream = new UnsyncByteArrayOutputStream();
/* 126 */     byte[] arrayOfByte2 = new byte[1024];
/*     */     int i;
/* 129 */     while ((i = paramInputStream.read(arrayOfByte2)) > 0) {
/* 130 */       localUnsyncByteArrayOutputStream.write(arrayOfByte2, 0, i);
/*     */     }
/*     */ 
/* 133 */     arrayOfByte1 = localUnsyncByteArrayOutputStream.toByteArray();
/* 134 */     return arrayOfByte1;
/*     */   }
/*     */ 
/*     */   public static void checkRegisterPermission()
/*     */   {
/* 148 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 149 */     if (localSecurityManager != null)
/* 150 */       localSecurityManager.checkPermission(REGISTER_PERMISSION);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.utils.JavaUtils
 * JD-Core Version:    0.6.2
 */