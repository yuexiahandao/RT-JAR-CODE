/*    */ package java.security;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ 
/*    */ public final class KeyPair
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = -7565189502268009837L;
/*    */   private PrivateKey privateKey;
/*    */   private PublicKey publicKey;
/*    */ 
/*    */   public KeyPair(PublicKey paramPublicKey, PrivateKey paramPrivateKey)
/*    */   {
/* 60 */     this.publicKey = paramPublicKey;
/* 61 */     this.privateKey = paramPrivateKey;
/*    */   }
/*    */ 
/*    */   public PublicKey getPublic()
/*    */   {
/* 70 */     return this.publicKey;
/*    */   }
/*    */ 
/*    */   public PrivateKey getPrivate()
/*    */   {
/* 79 */     return this.privateKey;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.KeyPair
 * JD-Core Version:    0.6.2
 */