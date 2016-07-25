/*     */ package javax.security.auth.x500;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.NotActiveException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.security.Principal;
/*     */ import java.util.Collections;
/*     */ import java.util.Map;
/*     */ import sun.security.util.DerValue;
/*     */ import sun.security.util.ResourcesMgr;
/*     */ import sun.security.x509.X500Name;
/*     */ 
/*     */ public final class X500Principal
/*     */   implements Principal, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -500463348111345721L;
/*     */   public static final String RFC1779 = "RFC1779";
/*     */   public static final String RFC2253 = "RFC2253";
/*     */   public static final String CANONICAL = "CANONICAL";
/*     */   private transient X500Name thisX500Name;
/*     */ 
/*     */   X500Principal(X500Name paramX500Name)
/*     */   {
/*  96 */     this.thisX500Name = paramX500Name;
/*     */   }
/*     */ 
/*     */   public X500Principal(String paramString)
/*     */   {
/* 121 */     this(paramString, Collections.EMPTY_MAP);
/*     */   }
/*     */ 
/*     */   public X500Principal(String paramString, Map<String, String> paramMap)
/*     */   {
/* 155 */     if (paramString == null) {
/* 156 */       throw new NullPointerException(ResourcesMgr.getString("provided.null.name"));
/*     */     }
/*     */ 
/* 160 */     if (paramMap == null) {
/* 161 */       throw new NullPointerException(ResourcesMgr.getString("provided.null.keyword.map"));
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 167 */       this.thisX500Name = new X500Name(paramString, paramMap);
/*     */     } catch (Exception localException) {
/* 169 */       IllegalArgumentException localIllegalArgumentException = new IllegalArgumentException("improperly specified input name: " + paramString);
/*     */ 
/* 171 */       localIllegalArgumentException.initCause(localException);
/* 172 */       throw localIllegalArgumentException;
/*     */     }
/*     */   }
/*     */ 
/*     */   public X500Principal(byte[] paramArrayOfByte)
/*     */   {
/*     */     try
/*     */     {
/* 212 */       this.thisX500Name = new X500Name(paramArrayOfByte);
/*     */     } catch (Exception localException) {
/* 214 */       IllegalArgumentException localIllegalArgumentException = new IllegalArgumentException("improperly specified input name");
/*     */ 
/* 216 */       localIllegalArgumentException.initCause(localException);
/* 217 */       throw localIllegalArgumentException;
/*     */     }
/*     */   }
/*     */ 
/*     */   public X500Principal(InputStream paramInputStream)
/*     */   {
/* 240 */     if (paramInputStream == null) {
/* 241 */       throw new NullPointerException("provided null input stream");
/*     */     }
/*     */     try
/*     */     {
/* 245 */       if (paramInputStream.markSupported())
/* 246 */         paramInputStream.mark(paramInputStream.available() + 1);
/* 247 */       DerValue localDerValue = new DerValue(paramInputStream);
/* 248 */       this.thisX500Name = new X500Name(localDerValue.data);
/*     */     } catch (Exception localException) {
/* 250 */       if (paramInputStream.markSupported()) {
/*     */         try {
/* 252 */           paramInputStream.reset();
/*     */         } catch (IOException localIOException) {
/* 254 */           IllegalArgumentException localIllegalArgumentException2 = new IllegalArgumentException("improperly specified input stream and unable to reset input stream");
/*     */ 
/* 257 */           localIllegalArgumentException2.initCause(localException);
/* 258 */           throw localIllegalArgumentException2;
/*     */         }
/*     */       }
/* 261 */       IllegalArgumentException localIllegalArgumentException1 = new IllegalArgumentException("improperly specified input stream");
/*     */ 
/* 263 */       localIllegalArgumentException1.initCause(localException);
/* 264 */       throw localIllegalArgumentException1;
/*     */     }
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 278 */     return getName("RFC2253");
/*     */   }
/*     */ 
/*     */   public String getName(String paramString)
/*     */   {
/* 344 */     if (paramString != null) {
/* 345 */       if (paramString.equalsIgnoreCase("RFC1779"))
/* 346 */         return this.thisX500Name.getRFC1779Name();
/* 347 */       if (paramString.equalsIgnoreCase("RFC2253"))
/* 348 */         return this.thisX500Name.getRFC2253Name();
/* 349 */       if (paramString.equalsIgnoreCase("CANONICAL")) {
/* 350 */         return this.thisX500Name.getRFC2253CanonicalName();
/*     */       }
/*     */     }
/* 353 */     throw new IllegalArgumentException("invalid format specified");
/*     */   }
/*     */ 
/*     */   public String getName(String paramString, Map<String, String> paramMap)
/*     */   {
/* 391 */     if (paramMap == null) {
/* 392 */       throw new NullPointerException(ResourcesMgr.getString("provided.null.OID.map"));
/*     */     }
/*     */ 
/* 396 */     if (paramString != null) {
/* 397 */       if (paramString.equalsIgnoreCase("RFC1779"))
/* 398 */         return this.thisX500Name.getRFC1779Name(paramMap);
/* 399 */       if (paramString.equalsIgnoreCase("RFC2253")) {
/* 400 */         return this.thisX500Name.getRFC2253Name(paramMap);
/*     */       }
/*     */     }
/* 403 */     throw new IllegalArgumentException("invalid format specified");
/*     */   }
/*     */ 
/*     */   public byte[] getEncoded()
/*     */   {
/*     */     try
/*     */     {
/* 419 */       return this.thisX500Name.getEncoded();
/*     */     } catch (IOException localIOException) {
/* 421 */       throw new RuntimeException("unable to get encoding", localIOException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 432 */     return this.thisX500Name.toString();
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 454 */     if (this == paramObject) {
/* 455 */       return true;
/*     */     }
/* 457 */     if (!(paramObject instanceof X500Principal)) {
/* 458 */       return false;
/*     */     }
/* 460 */     X500Principal localX500Principal = (X500Principal)paramObject;
/* 461 */     return this.thisX500Name.equals(localX500Principal.thisX500Name);
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 473 */     return this.thisX500Name.hashCode();
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*     */     throws IOException
/*     */   {
/* 485 */     paramObjectOutputStream.writeObject(this.thisX500Name.getEncodedInternal());
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws IOException, NotActiveException, ClassNotFoundException
/*     */   {
/* 497 */     this.thisX500Name = new X500Name((byte[])paramObjectInputStream.readObject());
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.security.auth.x500.X500Principal
 * JD-Core Version:    0.6.2
 */