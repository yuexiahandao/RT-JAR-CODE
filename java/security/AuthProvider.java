/*    */ package java.security;
/*    */ 
/*    */ import javax.security.auth.Subject;
/*    */ import javax.security.auth.callback.CallbackHandler;
/*    */ import javax.security.auth.login.LoginException;
/*    */ 
/*    */ public abstract class AuthProvider extends Provider
/*    */ {
/*    */   protected AuthProvider(String paramString1, double paramDouble, String paramString2)
/*    */   {
/* 53 */     super(paramString1, paramDouble, paramString2);
/*    */   }
/*    */ 
/*    */   public abstract void login(Subject paramSubject, CallbackHandler paramCallbackHandler)
/*    */     throws LoginException;
/*    */ 
/*    */   public abstract void logout()
/*    */     throws LoginException;
/*    */ 
/*    */   public abstract void setCallbackHandler(CallbackHandler paramCallbackHandler);
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.AuthProvider
 * JD-Core Version:    0.6.2
 */