/*     */ package javax.security.auth.callback;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public class PasswordCallback
/*     */   implements Callback, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 2267422647454909926L;
/*     */   private String prompt;
/*     */   private boolean echoOn;
/*     */   private char[] inputPassword;
/*     */ 
/*     */   public PasswordCallback(String paramString, boolean paramBoolean)
/*     */   {
/*  71 */     if ((paramString == null) || (paramString.length() == 0)) {
/*  72 */       throw new IllegalArgumentException();
/*     */     }
/*  74 */     this.prompt = paramString;
/*  75 */     this.echoOn = paramBoolean;
/*     */   }
/*     */ 
/*     */   public String getPrompt()
/*     */   {
/*  86 */     return this.prompt;
/*     */   }
/*     */ 
/*     */   public boolean isEchoOn()
/*     */   {
/*  99 */     return this.echoOn;
/*     */   }
/*     */ 
/*     */   public void setPassword(char[] paramArrayOfChar)
/*     */   {
/* 115 */     this.inputPassword = (paramArrayOfChar == null ? null : (char[])paramArrayOfChar.clone());
/*     */   }
/*     */ 
/*     */   public char[] getPassword()
/*     */   {
/* 130 */     return this.inputPassword == null ? null : (char[])this.inputPassword.clone();
/*     */   }
/*     */ 
/*     */   public void clearPassword()
/*     */   {
/* 137 */     if (this.inputPassword != null)
/* 138 */       for (int i = 0; i < this.inputPassword.length; i++)
/* 139 */         this.inputPassword[i] = ' ';
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.security.auth.callback.PasswordCallback
 * JD-Core Version:    0.6.2
 */