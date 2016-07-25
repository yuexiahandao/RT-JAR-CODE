/*    */ package javax.net.ssl;
/*    */ 
/*    */ import java.security.Principal;
/*    */ 
/*    */ public abstract class X509ExtendedKeyManager
/*    */   implements X509KeyManager
/*    */ {
/*    */   public String chooseEngineClientAlias(String[] paramArrayOfString, Principal[] paramArrayOfPrincipal, SSLEngine paramSSLEngine)
/*    */   {
/* 69 */     return null;
/*    */   }
/*    */ 
/*    */   public String chooseEngineServerAlias(String paramString, Principal[] paramArrayOfPrincipal, SSLEngine paramSSLEngine)
/*    */   {
/* 92 */     return null;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.net.ssl.X509ExtendedKeyManager
 * JD-Core Version:    0.6.2
 */