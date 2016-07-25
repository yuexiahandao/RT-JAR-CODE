/*    */ package sun.security.rsa;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.math.BigInteger;
/*    */ import java.security.InvalidKeyException;
/*    */ import java.security.interfaces.RSAPrivateKey;
/*    */ import sun.security.pkcs.PKCS8Key;
/*    */ import sun.security.util.DerOutputStream;
/*    */ import sun.security.util.DerValue;
/*    */ 
/*    */ public final class RSAPrivateKeyImpl extends PKCS8Key
/*    */   implements RSAPrivateKey
/*    */ {
/*    */   private static final long serialVersionUID = -33106691987952810L;
/*    */   private final BigInteger n;
/*    */   private final BigInteger d;
/*    */ 
/*    */   RSAPrivateKeyImpl(BigInteger paramBigInteger1, BigInteger paramBigInteger2)
/*    */     throws InvalidKeyException
/*    */   {
/* 62 */     this.n = paramBigInteger1;
/* 63 */     this.d = paramBigInteger2;
/* 64 */     RSAKeyFactory.checkRSAProviderKeyLengths(paramBigInteger1.bitLength(), null);
/*    */ 
/* 66 */     this.algid = RSAPrivateCrtKeyImpl.rsaId;
/*    */     try {
/* 68 */       DerOutputStream localDerOutputStream = new DerOutputStream();
/* 69 */       localDerOutputStream.putInteger(0);
/* 70 */       localDerOutputStream.putInteger(paramBigInteger1);
/* 71 */       localDerOutputStream.putInteger(0);
/* 72 */       localDerOutputStream.putInteger(paramBigInteger2);
/* 73 */       localDerOutputStream.putInteger(0);
/* 74 */       localDerOutputStream.putInteger(0);
/* 75 */       localDerOutputStream.putInteger(0);
/* 76 */       localDerOutputStream.putInteger(0);
/* 77 */       localDerOutputStream.putInteger(0);
/* 78 */       DerValue localDerValue = new DerValue((byte)48, localDerOutputStream.toByteArray());
/*    */ 
/* 80 */       this.key = localDerValue.toByteArray();
/*    */     }
/*    */     catch (IOException localIOException) {
/* 83 */       throw new InvalidKeyException(localIOException);
/*    */     }
/*    */   }
/*    */ 
/*    */   public String getAlgorithm()
/*    */   {
/* 89 */     return "RSA";
/*    */   }
/*    */ 
/*    */   public BigInteger getModulus()
/*    */   {
/* 94 */     return this.n;
/*    */   }
/*    */ 
/*    */   public BigInteger getPrivateExponent()
/*    */   {
/* 99 */     return this.d;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.rsa.RSAPrivateKeyImpl
 * JD-Core Version:    0.6.2
 */