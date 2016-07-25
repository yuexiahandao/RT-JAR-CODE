/*     */ package org.ietf.jgss;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.DerValue;
/*     */ import sun.security.util.ObjectIdentifier;
/*     */ 
/*     */ public class Oid
/*     */ {
/*     */   private ObjectIdentifier oid;
/*     */   private byte[] derEncoding;
/*     */ 
/*     */   public Oid(String paramString)
/*     */     throws GSSException
/*     */   {
/*     */     try
/*     */     {
/*  68 */       this.oid = new ObjectIdentifier(paramString);
/*  69 */       this.derEncoding = null;
/*     */     } catch (Exception localException) {
/*  71 */       throw new GSSException(11, "Improperly formatted Object Identifier String - " + paramString);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Oid(InputStream paramInputStream)
/*     */     throws GSSException
/*     */   {
/*     */     try
/*     */     {
/*  89 */       DerValue localDerValue = new DerValue(paramInputStream);
/*  90 */       this.derEncoding = localDerValue.toByteArray();
/*  91 */       this.oid = localDerValue.getOID();
/*     */     } catch (IOException localIOException) {
/*  93 */       throw new GSSException(11, "Improperly formatted ASN.1 DER encoding for Oid");
/*     */     }
/*     */   }
/*     */ 
/*     */   public Oid(byte[] paramArrayOfByte)
/*     */     throws GSSException
/*     */   {
/*     */     try
/*     */     {
/* 111 */       DerValue localDerValue = new DerValue(paramArrayOfByte);
/* 112 */       this.derEncoding = localDerValue.toByteArray();
/* 113 */       this.oid = localDerValue.getOID();
/*     */     } catch (IOException localIOException) {
/* 115 */       throw new GSSException(11, "Improperly formatted ASN.1 DER encoding for Oid");
/*     */     }
/*     */   }
/*     */ 
/*     */   static Oid getInstance(String paramString)
/*     */   {
/* 126 */     Oid localOid = null;
/*     */     try {
/* 128 */       localOid = new Oid(paramString);
/*     */     }
/*     */     catch (GSSException localGSSException) {
/*     */     }
/* 132 */     return localOid;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 142 */     return this.oid.toString();
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 156 */     if (this == paramObject) {
/* 157 */       return true;
/*     */     }
/* 159 */     if ((paramObject instanceof Oid))
/* 160 */       return this.oid.equals(((Oid)paramObject).oid);
/* 161 */     if ((paramObject instanceof ObjectIdentifier)) {
/* 162 */       return this.oid.equals(paramObject);
/*     */     }
/* 164 */     return false;
/*     */   }
/*     */ 
/*     */   public byte[] getDER()
/*     */     throws GSSException
/*     */   {
/* 177 */     if (this.derEncoding == null) {
/* 178 */       DerOutputStream localDerOutputStream = new DerOutputStream();
/*     */       try {
/* 180 */         localDerOutputStream.putOID(this.oid);
/*     */       } catch (IOException localIOException) {
/* 182 */         throw new GSSException(11, localIOException.getMessage());
/*     */       }
/* 184 */       this.derEncoding = localDerOutputStream.toByteArray();
/*     */     }
/*     */ 
/* 187 */     return (byte[])this.derEncoding.clone();
/*     */   }
/*     */ 
/*     */   public boolean containedIn(Oid[] paramArrayOfOid)
/*     */   {
/* 199 */     for (int i = 0; i < paramArrayOfOid.length; i++) {
/* 200 */       if (paramArrayOfOid[i].equals(this)) {
/* 201 */         return true;
/*     */       }
/*     */     }
/* 204 */     return false;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 214 */     return this.oid.hashCode();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.ietf.jgss.Oid
 * JD-Core Version:    0.6.2
 */