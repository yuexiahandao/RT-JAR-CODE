/*     */ package sun.security.util;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.Console;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.PushbackInputStream;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.CharsetEncoder;
/*     */ import java.nio.charset.CodingErrorAction;
/*     */ import java.util.Arrays;
/*     */ import sun.misc.JavaIOAccess;
/*     */ import sun.misc.SharedSecrets;
/*     */ 
/*     */ public class Password
/*     */ {
/*     */   private static volatile CharsetEncoder enc;
/*     */ 
/*     */   public static char[] readPassword(InputStream paramInputStream)
/*     */     throws IOException
/*     */   {
/*  40 */     return readPassword(paramInputStream, false);
/*     */   }
/*     */ 
/*     */   public static char[] readPassword(InputStream paramInputStream, boolean paramBoolean)
/*     */     throws IOException
/*     */   {
/*  49 */     char[] arrayOfChar1 = null;
/*  50 */     byte[] arrayOfByte = null;
/*     */     try
/*     */     {
/*  54 */       Console localConsole = null;
/*     */       Object localObject1;
/*  55 */       if ((!paramBoolean) && (paramInputStream == System.in) && ((localConsole = System.console()) != null)) {
/*  56 */         arrayOfChar1 = localConsole.readPassword();
/*     */ 
/*  59 */         if ((arrayOfChar1 != null) && (arrayOfChar1.length == 0)) {
/*  60 */           return null;
/*     */         }
/*  62 */         arrayOfByte = convertToBytes(arrayOfChar1);
/*  63 */         paramInputStream = new ByteArrayInputStream(arrayOfByte);
/*     */       }
/*     */ 
/*  73 */       char[] arrayOfChar2 = localObject1 = new char[''];
/*     */ 
/*  75 */       int i = arrayOfChar2.length;
/*  76 */       int j = 0;
/*     */ 
/*  79 */       int m = 0;
/*  80 */       while (m == 0)
/*     */       {
/*     */         int k;
/*  81 */         switch (k = paramInputStream.read()) {
/*     */         case -1:
/*     */         case 10:
/*  84 */           m = 1;
/*  85 */           break;
/*     */         case 13:
/*  88 */           int n = paramInputStream.read();
/*  89 */           if ((n != 10) && (n != -1)) {
/*  90 */             if (!(paramInputStream instanceof PushbackInputStream)) {
/*  91 */               paramInputStream = new PushbackInputStream(paramInputStream);
/*     */             }
/*  93 */             ((PushbackInputStream)paramInputStream).unread(n);
/*     */           } else {
/*  95 */             m = 1;
/*  96 */           }break;
/*     */         }
/*     */ 
/* 100 */         i--; if (i < 0) {
/* 101 */           arrayOfChar2 = new char[j + 128];
/* 102 */           i = arrayOfChar2.length - j - 1;
/* 103 */           System.arraycopy(localObject1, 0, arrayOfChar2, 0, j);
/* 104 */           Arrays.fill((char[])localObject1, ' ');
/* 105 */           localObject1 = arrayOfChar2;
/*     */         }
/* 107 */         arrayOfChar2[(j++)] = ((char)k);
/*     */       }
/*     */ 
/* 112 */       if (j == 0) {
/* 113 */         return null;
/*     */       }
/*     */ 
/* 116 */       char[] arrayOfChar3 = new char[j];
/* 117 */       System.arraycopy(arrayOfChar2, 0, arrayOfChar3, 0, j);
/* 118 */       Arrays.fill(arrayOfChar2, ' ');
/*     */ 
/* 120 */       return arrayOfChar3;
/*     */     } finally {
/* 122 */       if (arrayOfChar1 != null) {
/* 123 */         Arrays.fill(arrayOfChar1, ' ');
/*     */       }
/* 125 */       if (arrayOfByte != null)
/* 126 */         Arrays.fill(arrayOfByte, (byte)0);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static byte[] convertToBytes(char[] paramArrayOfChar)
/*     */   {
/* 139 */     if (enc == null) {
/* 140 */       synchronized (Password.class) {
/* 141 */         enc = SharedSecrets.getJavaIOAccess().charset().newEncoder().onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 148 */     ??? = new byte[(int)(enc.maxBytesPerChar() * paramArrayOfChar.length)];
/* 149 */     ByteBuffer localByteBuffer = ByteBuffer.wrap((byte[])???);
/* 150 */     synchronized (enc) {
/* 151 */       enc.reset().encode(CharBuffer.wrap(paramArrayOfChar), localByteBuffer, true);
/*     */     }
/* 153 */     if (localByteBuffer.position() < ???.length) {
/* 154 */       ???[localByteBuffer.position()] = 10;
/*     */     }
/* 156 */     return ???;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.util.Password
 * JD-Core Version:    0.6.2
 */