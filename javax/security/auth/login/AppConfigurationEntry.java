/*     */ package javax.security.auth.login;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.Map;
/*     */ import sun.security.util.ResourcesMgr;
/*     */ 
/*     */ public class AppConfigurationEntry
/*     */ {
/*     */   private String loginModuleName;
/*     */   private LoginModuleControlFlag controlFlag;
/*     */   private Map<String, ?> options;
/*     */ 
/*     */   public AppConfigurationEntry(String paramString, LoginModuleControlFlag paramLoginModuleControlFlag, Map<String, ?> paramMap)
/*     */   {
/*  77 */     if ((paramString == null) || (paramString.length() == 0) || ((paramLoginModuleControlFlag != LoginModuleControlFlag.REQUIRED) && (paramLoginModuleControlFlag != LoginModuleControlFlag.REQUISITE) && (paramLoginModuleControlFlag != LoginModuleControlFlag.SUFFICIENT) && (paramLoginModuleControlFlag != LoginModuleControlFlag.OPTIONAL)) || (paramMap == null))
/*     */     {
/*  83 */       throw new IllegalArgumentException();
/*     */     }
/*  85 */     this.loginModuleName = paramString;
/*  86 */     this.controlFlag = paramLoginModuleControlFlag;
/*  87 */     this.options = Collections.unmodifiableMap(paramMap);
/*     */   }
/*     */ 
/*     */   public String getLoginModuleName()
/*     */   {
/*  97 */     return this.loginModuleName;
/*     */   }
/*     */ 
/*     */   public LoginModuleControlFlag getControlFlag()
/*     */   {
/* 110 */     return this.controlFlag;
/*     */   }
/*     */ 
/*     */   public Map<String, ?> getOptions()
/*     */   {
/* 120 */     return this.options;
/*     */   }
/*     */ 
/*     */   public static class LoginModuleControlFlag
/*     */   {
/*     */     private String controlFlag;
/* 134 */     public static final LoginModuleControlFlag REQUIRED = new LoginModuleControlFlag("required");
/*     */ 
/* 140 */     public static final LoginModuleControlFlag REQUISITE = new LoginModuleControlFlag("requisite");
/*     */ 
/* 146 */     public static final LoginModuleControlFlag SUFFICIENT = new LoginModuleControlFlag("sufficient");
/*     */ 
/* 152 */     public static final LoginModuleControlFlag OPTIONAL = new LoginModuleControlFlag("optional");
/*     */ 
/*     */     private LoginModuleControlFlag(String paramString)
/*     */     {
/* 156 */       this.controlFlag = paramString;
/*     */     }
/*     */ 
/*     */     public String toString()
/*     */     {
/* 169 */       return ResourcesMgr.getString("LoginModuleControlFlag.") + this.controlFlag;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.security.auth.login.AppConfigurationEntry
 * JD-Core Version:    0.6.2
 */