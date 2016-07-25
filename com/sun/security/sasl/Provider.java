/*    */ package com.sun.security.sasl;
/*    */ 
/*    */ import java.security.AccessController;
/*    */ import java.security.PrivilegedAction;
/*    */ 
/*    */ public final class Provider extends java.security.Provider
/*    */ {
/*    */   private static final long serialVersionUID = 8622598936488630849L;
/*    */   private static final String info = "Sun SASL provider(implements client mechanisms for: DIGEST-MD5, GSSAPI, EXTERNAL, PLAIN, CRAM-MD5, NTLM; server mechanisms for: DIGEST-MD5, GSSAPI, CRAM-MD5, NTLM)";
/*    */ 
/*    */   public Provider()
/*    */   {
/* 56 */     super("SunSASL", 1.7D, "Sun SASL provider(implements client mechanisms for: DIGEST-MD5, GSSAPI, EXTERNAL, PLAIN, CRAM-MD5, NTLM; server mechanisms for: DIGEST-MD5, GSSAPI, CRAM-MD5, NTLM)");
/*    */ 
/* 58 */     AccessController.doPrivileged(new PrivilegedAction()
/*    */     {
/*    */       public Void run() {
/* 61 */         Provider.this.put("SaslClientFactory.DIGEST-MD5", "com.sun.security.sasl.digest.FactoryImpl");
/*    */ 
/* 63 */         Provider.this.put("SaslClientFactory.NTLM", "com.sun.security.sasl.ntlm.FactoryImpl");
/*    */ 
/* 65 */         Provider.this.put("SaslClientFactory.GSSAPI", "com.sun.security.sasl.gsskerb.FactoryImpl");
/*    */ 
/* 68 */         Provider.this.put("SaslClientFactory.EXTERNAL", "com.sun.security.sasl.ClientFactoryImpl");
/*    */ 
/* 70 */         Provider.this.put("SaslClientFactory.PLAIN", "com.sun.security.sasl.ClientFactoryImpl");
/*    */ 
/* 72 */         Provider.this.put("SaslClientFactory.CRAM-MD5", "com.sun.security.sasl.ClientFactoryImpl");
/*    */ 
/* 76 */         Provider.this.put("SaslServerFactory.CRAM-MD5", "com.sun.security.sasl.ServerFactoryImpl");
/*    */ 
/* 78 */         Provider.this.put("SaslServerFactory.GSSAPI", "com.sun.security.sasl.gsskerb.FactoryImpl");
/*    */ 
/* 80 */         Provider.this.put("SaslServerFactory.DIGEST-MD5", "com.sun.security.sasl.digest.FactoryImpl");
/*    */ 
/* 82 */         Provider.this.put("SaslServerFactory.NTLM", "com.sun.security.sasl.ntlm.FactoryImpl");
/*    */ 
/* 84 */         return null;
/*    */       }
/*    */     });
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.security.sasl.Provider
 * JD-Core Version:    0.6.2
 */