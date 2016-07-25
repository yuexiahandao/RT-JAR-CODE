/*    */ package com.sun.org.apache.xml.internal.security.utils;
/*    */ 
/*    */ import com.sun.org.apache.xml.internal.security.algorithms.MessageDigestAlgorithm;
/*    */ import java.io.ByteArrayOutputStream;
/*    */ import java.util.logging.Level;
/*    */ import java.util.logging.Logger;
/*    */ 
/*    */ public class DigesterOutputStream extends ByteArrayOutputStream
/*    */ {
/*    */   final MessageDigestAlgorithm mda;
/* 33 */   static Logger log = Logger.getLogger(DigesterOutputStream.class.getName());
/*    */ 
/*    */   public DigesterOutputStream(MessageDigestAlgorithm paramMessageDigestAlgorithm)
/*    */   {
/* 41 */     this.mda = paramMessageDigestAlgorithm;
/*    */   }
/*    */ 
/*    */   public void write(byte[] paramArrayOfByte)
/*    */   {
/* 46 */     write(paramArrayOfByte, 0, paramArrayOfByte.length);
/*    */   }
/*    */ 
/*    */   public void write(int paramInt)
/*    */   {
/* 51 */     this.mda.update((byte)paramInt);
/*    */   }
/*    */ 
/*    */   public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*    */   {
/* 56 */     if (log.isLoggable(Level.FINE)) {
/* 57 */       log.log(Level.FINE, "Pre-digested input:");
/* 58 */       StringBuffer localStringBuffer = new StringBuffer(paramInt2);
/* 59 */       for (int i = paramInt1; i < paramInt1 + paramInt2; i++) {
/* 60 */         localStringBuffer.append((char)paramArrayOfByte[i]);
/*    */       }
/* 62 */       log.log(Level.FINE, localStringBuffer.toString());
/*    */     }
/* 64 */     this.mda.update(paramArrayOfByte, paramInt1, paramInt2);
/*    */   }
/*    */ 
/*    */   public byte[] getDigestValue()
/*    */   {
/* 71 */     return this.mda.digest();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.utils.DigesterOutputStream
 * JD-Core Version:    0.6.2
 */