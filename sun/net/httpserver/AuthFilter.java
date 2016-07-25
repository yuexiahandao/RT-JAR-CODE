/*    */ package sun.net.httpserver;
/*    */ 
/*    */ import com.sun.net.httpserver.Authenticator;
/*    */ import com.sun.net.httpserver.Authenticator.Failure;
/*    */ import com.sun.net.httpserver.Authenticator.Result;
/*    */ import com.sun.net.httpserver.Authenticator.Retry;
/*    */ import com.sun.net.httpserver.Authenticator.Success;
/*    */ import com.sun.net.httpserver.Filter;
/*    */ import com.sun.net.httpserver.Filter.Chain;
/*    */ import com.sun.net.httpserver.HttpExchange;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ 
/*    */ public class AuthFilter extends Filter
/*    */ {
/*    */   private Authenticator authenticator;
/*    */ 
/*    */   public AuthFilter(Authenticator paramAuthenticator)
/*    */   {
/* 43 */     this.authenticator = paramAuthenticator;
/*    */   }
/*    */ 
/*    */   public String description() {
/* 47 */     return "Authentication filter";
/*    */   }
/*    */ 
/*    */   public void setAuthenticator(Authenticator paramAuthenticator) {
/* 51 */     this.authenticator = paramAuthenticator;
/*    */   }
/*    */ 
/*    */   public void consumeInput(HttpExchange paramHttpExchange) throws IOException {
/* 55 */     InputStream localInputStream = paramHttpExchange.getRequestBody();
/* 56 */     byte[] arrayOfByte = new byte[4096];
/* 57 */     while (localInputStream.read(arrayOfByte) != -1);
/* 58 */     localInputStream.close();
/*    */   }
/*    */ 
/*    */   public void doFilter(HttpExchange paramHttpExchange, Filter.Chain paramChain)
/*    */     throws IOException
/*    */   {
/* 66 */     if (this.authenticator != null) {
/* 67 */       Authenticator.Result localResult = this.authenticator.authenticate(paramHttpExchange);
/*    */       Object localObject;
/* 68 */       if ((localResult instanceof Authenticator.Success)) {
/* 69 */         localObject = (Authenticator.Success)localResult;
/* 70 */         ExchangeImpl localExchangeImpl = ExchangeImpl.get(paramHttpExchange);
/* 71 */         localExchangeImpl.setPrincipal(((Authenticator.Success)localObject).getPrincipal());
/* 72 */         paramChain.doFilter(paramHttpExchange);
/* 73 */       } else if ((localResult instanceof Authenticator.Retry)) {
/* 74 */         localObject = (Authenticator.Retry)localResult;
/* 75 */         consumeInput(paramHttpExchange);
/* 76 */         paramHttpExchange.sendResponseHeaders(((Authenticator.Retry)localObject).getResponseCode(), -1L);
/* 77 */       } else if ((localResult instanceof Authenticator.Failure)) {
/* 78 */         localObject = (Authenticator.Failure)localResult;
/* 79 */         consumeInput(paramHttpExchange);
/* 80 */         paramHttpExchange.sendResponseHeaders(((Authenticator.Failure)localObject).getResponseCode(), -1L);
/*    */       }
/*    */     } else {
/* 83 */       paramChain.doFilter(paramHttpExchange);
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.httpserver.AuthFilter
 * JD-Core Version:    0.6.2
 */