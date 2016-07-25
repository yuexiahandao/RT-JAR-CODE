/*    */ package sun.net.www.protocol.http.spnego;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.net.Authenticator;
/*    */ import java.net.PasswordAuthentication;
/*    */ import java.util.Arrays;
/*    */ import javax.security.auth.callback.Callback;
/*    */ import javax.security.auth.callback.CallbackHandler;
/*    */ import javax.security.auth.callback.NameCallback;
/*    */ import javax.security.auth.callback.PasswordCallback;
/*    */ import javax.security.auth.callback.UnsupportedCallbackException;
/*    */ import sun.net.www.protocol.http.HttpCallerInfo;
/*    */ 
/*    */ public class NegotiateCallbackHandler
/*    */   implements CallbackHandler
/*    */ {
/*    */   private String username;
/*    */   private char[] password;
/*    */   private boolean answered;
/*    */   private final HttpCallerInfo hci;
/*    */ 
/*    */   public NegotiateCallbackHandler(HttpCallerInfo paramHttpCallerInfo)
/*    */   {
/* 58 */     this.hci = paramHttpCallerInfo;
/*    */   }
/*    */ 
/*    */   private void getAnswer() {
/* 62 */     if (!this.answered) {
/* 63 */       this.answered = true;
/* 64 */       PasswordAuthentication localPasswordAuthentication = Authenticator.requestPasswordAuthentication(this.hci.host, this.hci.addr, this.hci.port, this.hci.protocol, this.hci.prompt, this.hci.scheme, this.hci.url, this.hci.authType);
/*    */ 
/* 73 */       if (localPasswordAuthentication != null) {
/* 74 */         this.username = localPasswordAuthentication.getUserName();
/* 75 */         this.password = localPasswordAuthentication.getPassword();
/*    */       }
/*    */     }
/*    */   }
/*    */ 
/*    */   public void handle(Callback[] paramArrayOfCallback) throws UnsupportedCallbackException, IOException
/*    */   {
/* 82 */     for (int i = 0; i < paramArrayOfCallback.length; i++) {
/* 83 */       Callback localCallback = paramArrayOfCallback[i];
/*    */ 
/* 85 */       if ((localCallback instanceof NameCallback)) {
/* 86 */         getAnswer();
/* 87 */         ((NameCallback)localCallback).setName(this.username);
/* 88 */       } else if ((localCallback instanceof PasswordCallback)) {
/* 89 */         getAnswer();
/* 90 */         ((PasswordCallback)localCallback).setPassword(this.password);
/* 91 */         if (this.password != null) Arrays.fill(this.password, ' '); 
/*    */       }
/* 93 */       else { throw new UnsupportedCallbackException(localCallback, "Call back not supported"); }
/*    */ 
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.www.protocol.http.spnego.NegotiateCallbackHandler
 * JD-Core Version:    0.6.2
 */