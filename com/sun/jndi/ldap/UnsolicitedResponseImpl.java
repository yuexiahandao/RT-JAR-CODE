/*     */ package com.sun.jndi.ldap;
/*     */ 
/*     */ import java.util.Vector;
/*     */ import javax.naming.NamingException;
/*     */ import javax.naming.ldap.Control;
/*     */ import javax.naming.ldap.UnsolicitedNotification;
/*     */ 
/*     */ final class UnsolicitedResponseImpl
/*     */   implements UnsolicitedNotification
/*     */ {
/*     */   private String oid;
/*     */   private String[] referrals;
/*     */   private byte[] extensionValue;
/*     */   private NamingException exception;
/*     */   private Control[] controls;
/*     */   private static final long serialVersionUID = 5913778898401784775L;
/*     */ 
/*     */   UnsolicitedResponseImpl(String paramString1, byte[] paramArrayOfByte, Vector paramVector, int paramInt, String paramString2, String paramString3, Control[] paramArrayOfControl)
/*     */   {
/*  46 */     this.oid = paramString1;
/*  47 */     this.extensionValue = paramArrayOfByte;
/*     */ 
/*  49 */     if ((paramVector != null) && (paramVector.size() > 0)) {
/*  50 */       int i = paramVector.size();
/*  51 */       this.referrals = new String[i];
/*  52 */       for (int j = 0; j < i; j++) {
/*  53 */         this.referrals[j] = ((String)paramVector.elementAt(j));
/*     */       }
/*     */     }
/*  56 */     this.exception = LdapCtx.mapErrorCode(paramInt, paramString2);
/*     */ 
/*  60 */     this.controls = paramArrayOfControl;
/*     */   }
/*     */ 
/*     */   public String getID()
/*     */   {
/*  70 */     return this.oid;
/*     */   }
/*     */ 
/*     */   public byte[] getEncodedValue()
/*     */   {
/*  85 */     return this.extensionValue;
/*     */   }
/*     */ 
/*     */   public String[] getReferrals()
/*     */   {
/*  95 */     return this.referrals;
/*     */   }
/*     */ 
/*     */   public NamingException getException()
/*     */   {
/* 106 */     return this.exception;
/*     */   }
/*     */ 
/*     */   public Control[] getControls() throws NamingException {
/* 110 */     return this.controls;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.ldap.UnsolicitedResponseImpl
 * JD-Core Version:    0.6.2
 */