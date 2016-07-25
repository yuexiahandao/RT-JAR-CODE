/*    */ package com.sun.org.apache.xml.internal.security.utils;
/*    */ 
/*    */ import com.sun.org.apache.xml.internal.security.algorithms.SignatureAlgorithm;
/*    */ import com.sun.org.apache.xml.internal.security.signature.XMLSignatureException;
/*    */ import java.io.ByteArrayOutputStream;
/*    */ import java.util.logging.Level;
/*    */ import java.util.logging.Logger;
/*    */ 
/*    */ public class SignerOutputStream extends ByteArrayOutputStream
/*    */ {
/*    */   final SignatureAlgorithm sa;
/* 34 */   static Logger log = Logger.getLogger(SignerOutputStream.class.getName());
/*    */ 
/*    */   public SignerOutputStream(SignatureAlgorithm paramSignatureAlgorithm)
/*    */   {
/* 42 */     this.sa = paramSignatureAlgorithm;
/*    */   }
/*    */ 
/*    */   public void write(byte[] paramArrayOfByte)
/*    */   {
/* 47 */     super.write(paramArrayOfByte, 0, paramArrayOfByte.length);
/*    */     try {
/* 49 */       this.sa.update(paramArrayOfByte);
/*    */     } catch (XMLSignatureException localXMLSignatureException) {
/* 51 */       throw new RuntimeException("" + localXMLSignatureException);
/*    */     }
/*    */   }
/*    */ 
/*    */   public void write(int paramInt)
/*    */   {
/* 57 */     super.write(paramInt);
/*    */     try {
/* 59 */       this.sa.update((byte)paramInt);
/*    */     } catch (XMLSignatureException localXMLSignatureException) {
/* 61 */       throw new RuntimeException("" + localXMLSignatureException);
/*    */     }
/*    */   }
/*    */ 
/*    */   public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*    */   {
/* 67 */     super.write(paramArrayOfByte, paramInt1, paramInt2);
/* 68 */     if (log.isLoggable(Level.FINE)) {
/* 69 */       log.log(Level.FINE, "Canonicalized SignedInfo:");
/* 70 */       StringBuffer localStringBuffer = new StringBuffer(paramInt2);
/* 71 */       for (int i = paramInt1; i < paramInt1 + paramInt2; i++) {
/* 72 */         localStringBuffer.append((char)paramArrayOfByte[i]);
/*    */       }
/* 74 */       log.log(Level.FINE, localStringBuffer.toString());
/*    */     }
/*    */     try {
/* 77 */       this.sa.update(paramArrayOfByte, paramInt1, paramInt2);
/*    */     } catch (XMLSignatureException localXMLSignatureException) {
/* 79 */       throw new RuntimeException("" + localXMLSignatureException);
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.utils.SignerOutputStream
 * JD-Core Version:    0.6.2
 */