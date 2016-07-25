/*    */ package java.security;
/*    */ 
/*    */ import java.security.spec.AlgorithmParameterSpec;
/*    */ 
/*    */ public abstract class KeyPairGeneratorSpi
/*    */ {
/*    */   public abstract void initialize(int paramInt, SecureRandom paramSecureRandom);
/*    */ 
/*    */   public void initialize(AlgorithmParameterSpec paramAlgorithmParameterSpec, SecureRandom paramSecureRandom)
/*    */     throws InvalidAlgorithmParameterException
/*    */   {
/* 94 */     throw new UnsupportedOperationException();
/*    */   }
/*    */ 
/*    */   public abstract KeyPair generateKeyPair();
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.KeyPairGeneratorSpi
 * JD-Core Version:    0.6.2
 */