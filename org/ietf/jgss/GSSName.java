/*     */ package org.ietf.jgss;
/*     */ 
/*     */ public abstract interface GSSName
/*     */ {
/* 110 */   public static final Oid NT_HOSTBASED_SERVICE = Oid.getInstance("1.2.840.113554.1.2.1.4");
/*     */ 
/* 120 */   public static final Oid NT_USER_NAME = Oid.getInstance("1.2.840.113554.1.2.1.1");
/*     */ 
/* 131 */   public static final Oid NT_MACHINE_UID_NAME = Oid.getInstance("1.2.840.113554.1.2.1.2");
/*     */ 
/* 143 */   public static final Oid NT_STRING_UID_NAME = Oid.getInstance("1.2.840.113554.1.2.1.3");
/*     */ 
/* 152 */   public static final Oid NT_ANONYMOUS = Oid.getInstance("1.3.6.1.5.6.3");
/*     */ 
/* 163 */   public static final Oid NT_EXPORT_NAME = Oid.getInstance("1.3.6.1.5.6.4");
/*     */ 
/*     */   public abstract boolean equals(GSSName paramGSSName)
/*     */     throws GSSException;
/*     */ 
/*     */   public abstract boolean equals(Object paramObject);
/*     */ 
/*     */   public abstract int hashCode();
/*     */ 
/*     */   public abstract GSSName canonicalize(Oid paramOid)
/*     */     throws GSSException;
/*     */ 
/*     */   public abstract byte[] export()
/*     */     throws GSSException;
/*     */ 
/*     */   public abstract String toString();
/*     */ 
/*     */   public abstract Oid getStringNameType()
/*     */     throws GSSException;
/*     */ 
/*     */   public abstract boolean isAnonymous();
/*     */ 
/*     */   public abstract boolean isMN();
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.ietf.jgss.GSSName
 * JD-Core Version:    0.6.2
 */