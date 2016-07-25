/*     */ package javax.security.sasl;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import javax.security.auth.callback.Callback;
/*     */ 
/*     */ public class AuthorizeCallback
/*     */   implements Callback, Serializable
/*     */ {
/*     */   private String authenticationID;
/*     */   private String authorizationID;
/*     */   private String authorizedID;
/*     */   private boolean authorized;
/*     */   private static final long serialVersionUID = -2353344186490470805L;
/*     */ 
/*     */   public AuthorizeCallback(String paramString1, String paramString2)
/*     */   {
/*  75 */     this.authenticationID = paramString1;
/*  76 */     this.authorizationID = paramString2;
/*     */   }
/*     */ 
/*     */   public String getAuthenticationID()
/*     */   {
/*  84 */     return this.authenticationID;
/*     */   }
/*     */ 
/*     */   public String getAuthorizationID()
/*     */   {
/*  92 */     return this.authorizationID;
/*     */   }
/*     */ 
/*     */   public boolean isAuthorized()
/*     */   {
/* 104 */     return this.authorized;
/*     */   }
/*     */ 
/*     */   public void setAuthorized(boolean paramBoolean)
/*     */   {
/* 114 */     this.authorized = paramBoolean;
/*     */   }
/*     */ 
/*     */   public String getAuthorizedID()
/*     */   {
/* 125 */     if (!this.authorized) {
/* 126 */       return null;
/*     */     }
/* 128 */     return this.authorizedID == null ? this.authorizationID : this.authorizedID;
/*     */   }
/*     */ 
/*     */   public void setAuthorizedID(String paramString)
/*     */   {
/* 141 */     this.authorizedID = paramString;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.security.sasl.AuthorizeCallback
 * JD-Core Version:    0.6.2
 */