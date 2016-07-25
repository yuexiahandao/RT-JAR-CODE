/*    */ package org.jcp.xml.dsig.internal;
/*    */ 
/*    */ import java.io.ByteArrayOutputStream;
/*    */ import java.security.Signature;
/*    */ import java.security.SignatureException;
/*    */ 
/*    */ public class SignerOutputStream extends ByteArrayOutputStream
/*    */ {
/*    */   private final Signature sig;
/*    */ 
/*    */   public SignerOutputStream(Signature paramSignature)
/*    */   {
/* 45 */     this.sig = paramSignature;
/*    */   }
/*    */ 
/*    */   public void write(byte[] paramArrayOfByte)
/*    */   {
/* 50 */     super.write(paramArrayOfByte, 0, paramArrayOfByte.length);
/*    */     try {
/* 52 */       this.sig.update(paramArrayOfByte);
/*    */     } catch (SignatureException localSignatureException) {
/* 54 */       throw new RuntimeException("" + localSignatureException);
/*    */     }
/*    */   }
/*    */ 
/*    */   public void write(int paramInt)
/*    */   {
/* 60 */     super.write(paramInt);
/*    */     try {
/* 62 */       this.sig.update((byte)paramInt);
/*    */     } catch (SignatureException localSignatureException) {
/* 64 */       throw new RuntimeException("" + localSignatureException);
/*    */     }
/*    */   }
/*    */ 
/*    */   public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*    */   {
/* 70 */     super.write(paramArrayOfByte, paramInt1, paramInt2);
/*    */     try {
/* 72 */       this.sig.update(paramArrayOfByte, paramInt1, paramInt2);
/*    */     } catch (SignatureException localSignatureException) {
/* 74 */       throw new RuntimeException("" + localSignatureException);
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.jcp.xml.dsig.internal.SignerOutputStream
 * JD-Core Version:    0.6.2
 */