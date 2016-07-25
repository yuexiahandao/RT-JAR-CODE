/*     */ package com.sun.jndi.ldap;
/*     */ 
/*     */ import java.io.OutputStream;
/*     */ import java.util.Arrays;
/*     */ import java.util.Hashtable;
/*     */ import javax.naming.ldap.Control;
/*     */ 
/*     */ class DigestClientId extends SimpleClientId
/*     */ {
/*  45 */   private static final String[] SASL_PROPS = { "java.naming.security.sasl.authorizationId", "java.naming.security.sasl.realm", "javax.security.sasl.qop", "javax.security.sasl.strength", "javax.security.sasl.reuse", "javax.security.sasl.server.authentication", "javax.security.sasl.maxbuffer", "javax.security.sasl.policy.noplaintext", "javax.security.sasl.policy.noactive", "javax.security.sasl.policy.nodictionary", "javax.security.sasl.policy.noanonymous", "javax.security.sasl.policy.forward", "javax.security.sasl.policy.credentials" };
/*     */   private final String[] propvals;
/*     */   private final int myHash;
/*  63 */   private int pHash = 0;
/*     */ 
/*     */   DigestClientId(int paramInt1, String paramString1, int paramInt2, String paramString2, Control[] paramArrayOfControl, OutputStream paramOutputStream, String paramString3, String paramString4, Object paramObject, Hashtable paramHashtable)
/*     */   {
/*  70 */     super(paramInt1, paramString1, paramInt2, paramString2, paramArrayOfControl, paramOutputStream, paramString3, paramString4, paramObject);
/*     */ 
/*  73 */     if (paramHashtable == null) {
/*  74 */       this.propvals = null;
/*     */     }
/*     */     else
/*     */     {
/*  78 */       this.propvals = new String[SASL_PROPS.length];
/*  79 */       for (int i = 0; i < SASL_PROPS.length; i++) {
/*  80 */         this.propvals[i] = ((String)paramHashtable.get(SASL_PROPS[i]));
/*  81 */         if (this.propvals[i] != null) {
/*  82 */           this.pHash = (this.pHash * 31 + this.propvals[i].hashCode());
/*     */         }
/*     */       }
/*     */     }
/*  86 */     this.myHash = (super.hashCode() + this.pHash);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject) {
/*  90 */     if (!(paramObject instanceof DigestClientId)) {
/*  91 */       return false;
/*     */     }
/*  93 */     DigestClientId localDigestClientId = (DigestClientId)paramObject;
/*  94 */     return (this.myHash == localDigestClientId.myHash) && (this.pHash == localDigestClientId.pHash) && (super.equals(paramObject)) && (Arrays.equals(this.propvals, localDigestClientId.propvals));
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 101 */     return this.myHash;
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 105 */     if (this.propvals != null) {
/* 106 */       StringBuffer localStringBuffer = new StringBuffer();
/* 107 */       for (int i = 0; i < this.propvals.length; i++) {
/* 108 */         localStringBuffer.append(':');
/* 109 */         if (this.propvals[i] != null) {
/* 110 */           localStringBuffer.append(this.propvals[i]);
/*     */         }
/*     */       }
/* 113 */       return super.toString() + localStringBuffer.toString();
/*     */     }
/* 115 */     return super.toString();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.ldap.DigestClientId
 * JD-Core Version:    0.6.2
 */