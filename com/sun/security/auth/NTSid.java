/*     */ package com.sun.security.auth;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.security.Principal;
/*     */ import java.text.MessageFormat;
/*     */ import sun.security.util.ResourcesMgr;
/*     */ 
/*     */ public class NTSid
/*     */   implements Principal, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 4412290580770249885L;
/*     */   private String sid;
/*     */ 
/*     */   public NTSid(String paramString)
/*     */   {
/*  73 */     if (paramString == null) {
/*  74 */       MessageFormat localMessageFormat = new MessageFormat(ResourcesMgr.getString("invalid.null.input.value", "sun.security.util.AuthResources"));
/*     */ 
/*  78 */       Object[] arrayOfObject = { "stringSid" };
/*  79 */       throw new NullPointerException(localMessageFormat.format(arrayOfObject));
/*     */     }
/*  81 */     if (paramString.length() == 0) {
/*  82 */       throw new IllegalArgumentException(ResourcesMgr.getString("Invalid.NTSid.value", "sun.security.util.AuthResources"));
/*     */     }
/*     */ 
/*  87 */     this.sid = new String(paramString);
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/*  98 */     return this.sid;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 109 */     MessageFormat localMessageFormat = new MessageFormat(ResourcesMgr.getString("NTSid.name", "sun.security.util.AuthResources"));
/*     */ 
/* 113 */     Object[] arrayOfObject = { this.sid };
/* 114 */     return localMessageFormat.format(arrayOfObject);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 132 */     if (paramObject == null) {
/* 133 */       return false;
/*     */     }
/* 135 */     if (this == paramObject) {
/* 136 */       return true;
/*     */     }
/* 138 */     if (!(paramObject instanceof NTSid))
/* 139 */       return false;
/* 140 */     NTSid localNTSid = (NTSid)paramObject;
/*     */ 
/* 142 */     if (this.sid.equals(localNTSid.sid)) {
/* 143 */       return true;
/*     */     }
/* 145 */     return false;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 156 */     return this.sid.hashCode();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.security.auth.NTSid
 * JD-Core Version:    0.6.2
 */