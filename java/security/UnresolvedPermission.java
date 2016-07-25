/*     */ package java.security;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.security.cert.Certificate;
/*     */ import java.security.cert.CertificateEncodingException;
/*     */ import java.security.cert.CertificateException;
/*     */ import java.security.cert.CertificateFactory;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Hashtable;
/*     */ import sun.security.util.Debug;
/*     */ 
/*     */ public final class UnresolvedPermission extends Permission
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -4821973115467008846L;
/* 109 */   private static final Debug debug = Debug.getInstance("policy,access", "UnresolvedPermission");
/*     */   private String type;
/*     */   private String name;
/*     */   private String actions;
/*     */   private transient Certificate[] certs;
/* 221 */   private static final Class[] PARAMS0 = new Class[0];
/* 222 */   private static final Class[] PARAMS1 = { String.class };
/* 223 */   private static final Class[] PARAMS2 = { String.class, String.class };
/*     */ 
/*     */   public UnresolvedPermission(String paramString1, String paramString2, String paramString3, Certificate[] paramArrayOfCertificate)
/*     */   {
/* 159 */     super(paramString1);
/*     */ 
/* 161 */     if (paramString1 == null) {
/* 162 */       throw new NullPointerException("type can't be null");
/*     */     }
/* 164 */     this.type = paramString1;
/* 165 */     this.name = paramString2;
/* 166 */     this.actions = paramString3;
/* 167 */     if (paramArrayOfCertificate != null)
/*     */     {
/* 169 */       for (int i = 0; i < paramArrayOfCertificate.length; i++) {
/* 170 */         if (!(paramArrayOfCertificate[i] instanceof X509Certificate))
/*     */         {
/* 173 */           this.certs = ((Certificate[])paramArrayOfCertificate.clone());
/* 174 */           break;
/*     */         }
/*     */       }
/*     */ 
/* 178 */       if (this.certs == null)
/*     */       {
/* 181 */         i = 0;
/* 182 */         int j = 0;
/* 183 */         while (i < paramArrayOfCertificate.length) {
/* 184 */           j++;
/* 185 */           while ((i + 1 < paramArrayOfCertificate.length) && (((X509Certificate)paramArrayOfCertificate[i]).getIssuerDN().equals(((X509Certificate)paramArrayOfCertificate[(i + 1)]).getSubjectDN())))
/*     */           {
/* 188 */             i++;
/*     */           }
/* 190 */           i++;
/*     */         }
/* 192 */         if (j == paramArrayOfCertificate.length)
/*     */         {
/* 195 */           this.certs = ((Certificate[])paramArrayOfCertificate.clone());
/*     */         }
/*     */ 
/* 198 */         if (this.certs == null)
/*     */         {
/* 200 */           ArrayList localArrayList = new ArrayList();
/*     */ 
/* 202 */           i = 0;
/* 203 */           while (i < paramArrayOfCertificate.length) {
/* 204 */             localArrayList.add(paramArrayOfCertificate[i]);
/* 205 */             while ((i + 1 < paramArrayOfCertificate.length) && (((X509Certificate)paramArrayOfCertificate[i]).getIssuerDN().equals(((X509Certificate)paramArrayOfCertificate[(i + 1)]).getSubjectDN())))
/*     */             {
/* 208 */               i++;
/*     */             }
/* 210 */             i++;
/*     */           }
/* 212 */           this.certs = new Certificate[localArrayList.size()];
/*     */ 
/* 214 */           localArrayList.toArray(this.certs);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   Permission resolve(Permission paramPermission, Certificate[] paramArrayOfCertificate)
/*     */   {
/* 230 */     if (this.certs != null)
/*     */     {
/* 232 */       if (paramArrayOfCertificate == null) {
/* 233 */         return null;
/*     */       }
/*     */ 
/* 238 */       for (int j = 0; j < this.certs.length; j++) {
/* 239 */         int i = 0;
/* 240 */         for (int k = 0; k < paramArrayOfCertificate.length; k++) {
/* 241 */           if (this.certs[j].equals(paramArrayOfCertificate[k])) {
/* 242 */             i = 1;
/* 243 */             break;
/*     */           }
/*     */         }
/* 246 */         if (i == 0) return null; 
/*     */       }
/*     */     }
/*     */     try
/*     */     {
/* 250 */       Class localClass = paramPermission.getClass();
/*     */ 
/* 252 */       if ((this.name == null) && (this.actions == null)) {
/*     */         try {
/* 254 */           Constructor localConstructor1 = localClass.getConstructor(PARAMS0);
/* 255 */           return (Permission)localConstructor1.newInstance(new Object[0]);
/*     */         } catch (NoSuchMethodException localNoSuchMethodException2) {
/*     */           try {
/* 258 */             Constructor localConstructor4 = localClass.getConstructor(PARAMS1);
/* 259 */             return (Permission)localConstructor4.newInstance(new Object[] { this.name });
/*     */           }
/*     */           catch (NoSuchMethodException localNoSuchMethodException4) {
/* 262 */             Constructor localConstructor6 = localClass.getConstructor(PARAMS2);
/* 263 */             return (Permission)localConstructor6.newInstance(new Object[] { this.name, this.actions });
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/* 268 */       if ((this.name != null) && (this.actions == null)) {
/*     */         try {
/* 270 */           Constructor localConstructor2 = localClass.getConstructor(PARAMS1);
/* 271 */           return (Permission)localConstructor2.newInstance(new Object[] { this.name });
/*     */         }
/*     */         catch (NoSuchMethodException localNoSuchMethodException3) {
/* 274 */           Constructor localConstructor5 = localClass.getConstructor(PARAMS2);
/* 275 */           return (Permission)localConstructor5.newInstance(new Object[] { this.name, this.actions });
/*     */         }
/*     */       }
/*     */ 
/* 279 */       Constructor localConstructor3 = localClass.getConstructor(PARAMS2);
/* 280 */       return (Permission)localConstructor3.newInstance(new Object[] { this.name, this.actions });
/*     */     }
/*     */     catch (NoSuchMethodException localNoSuchMethodException1)
/*     */     {
/* 285 */       if (debug != null) {
/* 286 */         debug.println("NoSuchMethodException:\n  could not find proper constructor for " + this.type);
/*     */ 
/* 288 */         localNoSuchMethodException1.printStackTrace();
/*     */       }
/* 290 */       return null;
/*     */     } catch (Exception localException) {
/* 292 */       if (debug != null) {
/* 293 */         debug.println("unable to instantiate " + this.name);
/* 294 */         localException.printStackTrace();
/*     */       }
/*     */     }
/* 296 */     return null;
/*     */   }
/*     */ 
/*     */   public boolean implies(Permission paramPermission)
/*     */   {
/* 310 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 330 */     if (paramObject == this) {
/* 331 */       return true;
/*     */     }
/* 333 */     if (!(paramObject instanceof UnresolvedPermission))
/* 334 */       return false;
/* 335 */     UnresolvedPermission localUnresolvedPermission = (UnresolvedPermission)paramObject;
/*     */ 
/* 338 */     if (!this.type.equals(localUnresolvedPermission.type)) {
/* 339 */       return false;
/*     */     }
/*     */ 
/* 343 */     if (this.name == null) {
/* 344 */       if (localUnresolvedPermission.name != null)
/* 345 */         return false;
/*     */     }
/* 347 */     else if (!this.name.equals(localUnresolvedPermission.name)) {
/* 348 */       return false;
/*     */     }
/*     */ 
/* 352 */     if (this.actions == null) {
/* 353 */       if (localUnresolvedPermission.actions != null) {
/* 354 */         return false;
/*     */       }
/*     */     }
/* 357 */     else if (!this.actions.equals(localUnresolvedPermission.actions)) {
/* 358 */       return false;
/*     */     }
/*     */ 
/* 363 */     if (((this.certs == null) && (localUnresolvedPermission.certs != null)) || ((this.certs != null) && (localUnresolvedPermission.certs == null)) || ((this.certs != null) && (localUnresolvedPermission.certs != null) && (this.certs.length != localUnresolvedPermission.certs.length)))
/*     */     {
/* 367 */       return false;
/*     */     }
/*     */     int k;
/*     */     int j;
/* 373 */     for (int i = 0; (this.certs != null) && (i < this.certs.length); i++) {
/* 374 */       k = 0;
/* 375 */       for (j = 0; j < localUnresolvedPermission.certs.length; j++) {
/* 376 */         if (this.certs[i].equals(localUnresolvedPermission.certs[j])) {
/* 377 */           k = 1;
/* 378 */           break;
/*     */         }
/*     */       }
/* 381 */       if (k == 0) return false;
/*     */     }
/*     */ 
/* 384 */     for (i = 0; (localUnresolvedPermission.certs != null) && (i < localUnresolvedPermission.certs.length); i++) {
/* 385 */       k = 0;
/* 386 */       for (j = 0; j < this.certs.length; j++) {
/* 387 */         if (localUnresolvedPermission.certs[i].equals(this.certs[j])) {
/* 388 */           k = 1;
/* 389 */           break;
/*     */         }
/*     */       }
/* 392 */       if (k == 0) return false;
/*     */     }
/* 394 */     return true;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 404 */     int i = this.type.hashCode();
/* 405 */     if (this.name != null)
/* 406 */       i ^= this.name.hashCode();
/* 407 */     if (this.actions != null)
/* 408 */       i ^= this.actions.hashCode();
/* 409 */     return i;
/*     */   }
/*     */ 
/*     */   public String getActions()
/*     */   {
/* 424 */     return "";
/*     */   }
/*     */ 
/*     */   public String getUnresolvedType()
/*     */   {
/* 437 */     return this.type;
/*     */   }
/*     */ 
/*     */   public String getUnresolvedName()
/*     */   {
/* 451 */     return this.name;
/*     */   }
/*     */ 
/*     */   public String getUnresolvedActions()
/*     */   {
/* 465 */     return this.actions;
/*     */   }
/*     */ 
/*     */   public Certificate[] getUnresolvedCerts()
/*     */   {
/* 479 */     return this.certs == null ? null : (Certificate[])this.certs.clone();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 490 */     return "(unresolved " + this.type + " " + this.name + " " + this.actions + ")";
/*     */   }
/*     */ 
/*     */   public PermissionCollection newPermissionCollection()
/*     */   {
/* 502 */     return new UnresolvedPermissionCollection();
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*     */     throws IOException
/*     */   {
/* 524 */     paramObjectOutputStream.defaultWriteObject();
/*     */ 
/* 526 */     if ((this.certs == null) || (this.certs.length == 0)) {
/* 527 */       paramObjectOutputStream.writeInt(0);
/*     */     }
/*     */     else {
/* 530 */       paramObjectOutputStream.writeInt(this.certs.length);
/*     */ 
/* 532 */       for (int i = 0; i < this.certs.length; i++) {
/* 533 */         Certificate localCertificate = this.certs[i];
/*     */         try {
/* 535 */           paramObjectOutputStream.writeUTF(localCertificate.getType());
/* 536 */           byte[] arrayOfByte = localCertificate.getEncoded();
/* 537 */           paramObjectOutputStream.writeInt(arrayOfByte.length);
/* 538 */           paramObjectOutputStream.write(arrayOfByte);
/*     */         } catch (CertificateEncodingException localCertificateEncodingException) {
/* 540 */           throw new IOException(localCertificateEncodingException.getMessage());
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 553 */     Hashtable localHashtable = null;
/*     */ 
/* 555 */     paramObjectInputStream.defaultReadObject();
/*     */ 
/* 557 */     if (this.type == null) {
/* 558 */       throw new NullPointerException("type can't be null");
/*     */     }
/*     */ 
/* 561 */     int i = paramObjectInputStream.readInt();
/* 562 */     if (i > 0)
/*     */     {
/* 565 */       localHashtable = new Hashtable(3);
/* 566 */       this.certs = new Certificate[i];
/*     */     }
/*     */ 
/* 569 */     for (int j = 0; j < i; j++)
/*     */     {
/* 572 */       String str = paramObjectInputStream.readUTF();
/*     */       CertificateFactory localCertificateFactory;
/* 573 */       if (localHashtable.containsKey(str))
/*     */       {
/* 575 */         localCertificateFactory = (CertificateFactory)localHashtable.get(str);
/*     */       }
/*     */       else {
/*     */         try {
/* 579 */           localCertificateFactory = CertificateFactory.getInstance(str);
/*     */         } catch (CertificateException localCertificateException1) {
/* 581 */           throw new ClassNotFoundException("Certificate factory for " + str + " not found");
/*     */         }
/*     */ 
/* 585 */         localHashtable.put(str, localCertificateFactory);
/*     */       }
/*     */ 
/* 588 */       byte[] arrayOfByte = null;
/*     */       try {
/* 590 */         arrayOfByte = new byte[paramObjectInputStream.readInt()];
/*     */       } catch (OutOfMemoryError localOutOfMemoryError) {
/* 592 */         throw new IOException("Certificate too big");
/*     */       }
/* 594 */       paramObjectInputStream.readFully(arrayOfByte);
/* 595 */       ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(arrayOfByte);
/*     */       try {
/* 597 */         this.certs[j] = localCertificateFactory.generateCertificate(localByteArrayInputStream);
/*     */       } catch (CertificateException localCertificateException2) {
/* 599 */         throw new IOException(localCertificateException2.getMessage());
/*     */       }
/* 601 */       localByteArrayInputStream.close();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.UnresolvedPermission
 * JD-Core Version:    0.6.2
 */