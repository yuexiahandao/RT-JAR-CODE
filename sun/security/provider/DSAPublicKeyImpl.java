/*    */ package sun.security.provider;
/*    */ 
/*    */ import java.io.ObjectStreamException;
/*    */ import java.math.BigInteger;
/*    */ import java.security.InvalidKeyException;
/*    */ import java.security.KeyRep;
/*    */ import java.security.KeyRep.Type;
/*    */ 
/*    */ public final class DSAPublicKeyImpl extends DSAPublicKey
/*    */ {
/*    */   private static final long serialVersionUID = 7819830118247182730L;
/*    */ 
/*    */   public DSAPublicKeyImpl(BigInteger paramBigInteger1, BigInteger paramBigInteger2, BigInteger paramBigInteger3, BigInteger paramBigInteger4)
/*    */     throws InvalidKeyException
/*    */   {
/* 63 */     super(paramBigInteger1, paramBigInteger2, paramBigInteger3, paramBigInteger4);
/*    */   }
/*    */ 
/*    */   public DSAPublicKeyImpl(byte[] paramArrayOfByte)
/*    */     throws InvalidKeyException
/*    */   {
/* 70 */     super(paramArrayOfByte);
/*    */   }
/*    */ 
/*    */   protected Object writeReplace() throws ObjectStreamException {
/* 74 */     return new KeyRep(KeyRep.Type.PUBLIC, getAlgorithm(), getFormat(), getEncoded());
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.provider.DSAPublicKeyImpl
 * JD-Core Version:    0.6.2
 */