/*    */ package com.sun.net.httpserver;
/*    */ 
/*    */ public abstract class BasicAuthenticator extends Authenticator
/*    */ {
/*    */   protected String realm;
/*    */ 
/*    */   public BasicAuthenticator(String paramString)
/*    */   {
/* 44 */     this.realm = paramString;
/*    */   }
/*    */ 
/*    */   public String getRealm()
/*    */   {
/* 52 */     return this.realm;
/*    */   }
/*    */ 
/*    */   public Authenticator.Result authenticate(HttpExchange paramHttpExchange)
/*    */   {
/* 57 */     Headers localHeaders1 = paramHttpExchange.getRequestHeaders();
/*    */ 
/* 61 */     String str1 = localHeaders1.getFirst("Authorization");
/* 62 */     if (str1 == null) {
/* 63 */       Headers localHeaders2 = paramHttpExchange.getResponseHeaders();
/* 64 */       localHeaders2.set("WWW-Authenticate", "Basic realm=\"" + this.realm + "\"");
/* 65 */       return new Authenticator.Retry(401);
/*    */     }
/* 67 */     int i = str1.indexOf(' ');
/* 68 */     if ((i == -1) || (!str1.substring(0, i).equals("Basic"))) {
/* 69 */       return new Authenticator.Failure(401);
/*    */     }
/* 71 */     byte[] arrayOfByte = Base64.base64ToByteArray(str1.substring(i + 1));
/* 72 */     String str2 = new String(arrayOfByte);
/* 73 */     int j = str2.indexOf(':');
/* 74 */     String str3 = str2.substring(0, j);
/* 75 */     String str4 = str2.substring(j + 1);
/*    */ 
/* 77 */     if (checkCredentials(str3, str4)) {
/* 78 */       return new Authenticator.Success(new HttpPrincipal(str3, this.realm));
/*    */     }
/*    */ 
/* 86 */     Headers localHeaders3 = paramHttpExchange.getResponseHeaders();
/* 87 */     localHeaders3.set("WWW-Authenticate", "Basic realm=\"" + this.realm + "\"");
/* 88 */     return new Authenticator.Failure(401);
/*    */   }
/*    */ 
/*    */   public abstract boolean checkCredentials(String paramString1, String paramString2);
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.net.httpserver.BasicAuthenticator
 * JD-Core Version:    0.6.2
 */