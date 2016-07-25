/*    */ package sun.net.www.protocol.http.ntlm;
/*    */ 
/*    */ import java.net.URL;
/*    */ 
/*    */ public abstract class NTLMAuthenticationCallback
/*    */ {
/* 36 */   private static volatile NTLMAuthenticationCallback callback = new DefaultNTLMAuthenticationCallback();
/*    */ 
/*    */   public static void setNTLMAuthenticationCallback(NTLMAuthenticationCallback paramNTLMAuthenticationCallback)
/*    */   {
/* 41 */     callback = paramNTLMAuthenticationCallback;
/*    */   }
/*    */ 
/*    */   public static NTLMAuthenticationCallback getNTLMAuthenticationCallback() {
/* 45 */     return callback;
/*    */   }
/*    */ 
/*    */   public abstract boolean isTrustedSite(URL paramURL);
/*    */ 
/*    */   static class DefaultNTLMAuthenticationCallback extends NTLMAuthenticationCallback
/*    */   {
/*    */     public boolean isTrustedSite(URL paramURL)
/*    */     {
/* 56 */       return true;
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.www.protocol.http.ntlm.NTLMAuthenticationCallback
 * JD-Core Version:    0.6.2
 */