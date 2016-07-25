/*     */ package sun.jdbc.odbc.ee;
/*     */ 
/*     */ import java.util.Properties;
/*     */ 
/*     */ public class ConnectionAttributes
/*     */ {
/*  22 */   private String url = null;
/*     */ 
/*  25 */   private String user = null;
/*     */ 
/*  28 */   private String password = null;
/*     */ 
/*  31 */   private String charSet = null;
/*     */ 
/*  34 */   private int loginTimeout = 0;
/*     */ 
/*     */   public ConnectionAttributes(String paramString1, String paramString2, String paramString3, String paramString4, int paramInt)
/*     */   {
/*  46 */     this.url = ("jdbc:odbc:" + paramString1);
/*  47 */     this.user = paramString2;
/*  48 */     this.password = paramString3;
/*  49 */     this.charSet = paramString4;
/*  50 */     this.loginTimeout = paramInt;
/*     */   }
/*     */ 
/*     */   public String getUser()
/*     */   {
/*  59 */     return this.user;
/*     */   }
/*     */ 
/*     */   public String getPassword()
/*     */   {
/*  68 */     return this.password;
/*     */   }
/*     */ 
/*     */   public String getUrl()
/*     */   {
/*  77 */     return this.url;
/*     */   }
/*     */ 
/*     */   public String getCharSet()
/*     */   {
/*  86 */     return this.charSet;
/*     */   }
/*     */ 
/*     */   public int getLoginTimeout()
/*     */   {
/*  95 */     return this.loginTimeout;
/*     */   }
/*     */ 
/*     */   public Properties getProperties()
/*     */   {
/* 105 */     Properties localProperties = new Properties();
/* 106 */     if (this.charSet != null) {
/* 107 */       localProperties.put("charSet", this.charSet);
/*     */     }
/*     */ 
/* 110 */     if (this.user != null) {
/* 111 */       localProperties.put("user", this.user);
/*     */     }
/*     */ 
/* 114 */     if (this.password != null) {
/* 115 */       localProperties.put("password", this.password);
/*     */     }
/*     */ 
/* 118 */     if (this.url != null) {
/* 119 */       localProperties.put("url", this.url);
/*     */     }
/* 121 */     localProperties.put("loginTimeout", "" + this.loginTimeout);
/* 122 */     return localProperties;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.jdbc.odbc.ee.ConnectionAttributes
 * JD-Core Version:    0.6.2
 */