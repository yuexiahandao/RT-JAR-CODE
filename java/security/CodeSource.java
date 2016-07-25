/*     */ package java.security;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.net.SocketPermission;
/*     */ import java.net.URL;
/*     */ import java.security.cert.CertPath;
/*     */ import java.security.cert.Certificate;
/*     */ import java.security.cert.CertificateEncodingException;
/*     */ import java.security.cert.CertificateException;
/*     */ import java.security.cert.CertificateFactory;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Hashtable;
/*     */ import java.util.List;
/*     */ 
/*     */ public class CodeSource
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 4977541819976013951L;
/*     */   private URL location;
/*  62 */   private transient CodeSigner[] signers = null;
/*     */ 
/*  67 */   private transient Certificate[] certs = null;
/*     */   private transient SocketPermission sp;
/*  73 */   private transient CertificateFactory factory = null;
/*     */ 
/*     */   public CodeSource(URL paramURL, Certificate[] paramArrayOfCertificate)
/*     */   {
/*  85 */     this.location = paramURL;
/*     */ 
/*  88 */     if (paramArrayOfCertificate != null)
/*  89 */       this.certs = ((Certificate[])paramArrayOfCertificate.clone());
/*     */   }
/*     */ 
/*     */   public CodeSource(URL paramURL, CodeSigner[] paramArrayOfCodeSigner)
/*     */   {
/* 104 */     this.location = paramURL;
/*     */ 
/* 107 */     if (paramArrayOfCodeSigner != null)
/* 108 */       this.signers = ((CodeSigner[])paramArrayOfCodeSigner.clone());
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 119 */     if (this.location != null) {
/* 120 */       return this.location.hashCode();
/*     */     }
/* 122 */     return 0;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 137 */     if (paramObject == this) {
/* 138 */       return true;
/*     */     }
/*     */ 
/* 141 */     if (!(paramObject instanceof CodeSource)) {
/* 142 */       return false;
/*     */     }
/* 144 */     CodeSource localCodeSource = (CodeSource)paramObject;
/*     */ 
/* 147 */     if (this.location == null)
/*     */     {
/* 149 */       if (localCodeSource.location != null) return false;
/*     */ 
/*     */     }
/* 152 */     else if (!this.location.equals(localCodeSource.location)) return false;
/*     */ 
/* 156 */     return matchCerts(localCodeSource, true);
/*     */   }
/*     */ 
/*     */   public final URL getLocation()
/*     */   {
/* 167 */     return this.location;
/*     */   }
/*     */ 
/*     */   public final Certificate[] getCertificates()
/*     */   {
/* 185 */     if (this.certs != null) {
/* 186 */       return (Certificate[])this.certs.clone();
/*     */     }
/* 188 */     if (this.signers != null)
/*     */     {
/* 190 */       ArrayList localArrayList = new ArrayList();
/*     */ 
/* 192 */       for (int i = 0; i < this.signers.length; i++) {
/* 193 */         localArrayList.addAll(this.signers[i].getSignerCertPath().getCertificates());
/*     */       }
/*     */ 
/* 196 */       this.certs = ((Certificate[])localArrayList.toArray(new Certificate[localArrayList.size()]));
/*     */ 
/* 198 */       return (Certificate[])this.certs.clone();
/*     */     }
/*     */ 
/* 201 */     return null;
/*     */   }
/*     */ 
/*     */   public final CodeSigner[] getCodeSigners()
/*     */   {
/* 219 */     if (this.signers != null) {
/* 220 */       return (CodeSigner[])this.signers.clone();
/*     */     }
/* 222 */     if (this.certs != null)
/*     */     {
/* 224 */       this.signers = convertCertArrayToSignerArray(this.certs);
/* 225 */       return (CodeSigner[])this.signers.clone();
/*     */     }
/*     */ 
/* 228 */     return null;
/*     */   }
/*     */ 
/*     */   public boolean implies(CodeSource paramCodeSource)
/*     */   {
/* 303 */     if (paramCodeSource == null) {
/* 304 */       return false;
/*     */     }
/* 306 */     return (matchCerts(paramCodeSource, false)) && (matchLocation(paramCodeSource));
/*     */   }
/*     */ 
/*     */   private boolean matchCerts(CodeSource paramCodeSource, boolean paramBoolean)
/*     */   {
/* 322 */     if ((this.certs == null) && (this.signers == null)) {
/* 323 */       if (paramBoolean) {
/* 324 */         return (paramCodeSource.certs == null) && (paramCodeSource.signers == null);
/*     */       }
/* 326 */       return true;
/*     */     }
/*     */     int j;
/*     */     int i;
/*     */     int k;
/* 329 */     if ((this.signers != null) && (paramCodeSource.signers != null)) {
/* 330 */       if ((paramBoolean) && (this.signers.length != paramCodeSource.signers.length)) {
/* 331 */         return false;
/*     */       }
/* 333 */       for (j = 0; j < this.signers.length; j++) {
/* 334 */         i = 0;
/* 335 */         for (k = 0; k < paramCodeSource.signers.length; k++) {
/* 336 */           if (this.signers[j].equals(paramCodeSource.signers[k])) {
/* 337 */             i = 1;
/* 338 */             break;
/*     */           }
/*     */         }
/* 341 */         if (i == 0) return false;
/*     */       }
/* 343 */       return true;
/*     */     }
/*     */ 
/* 346 */     if ((this.certs != null) && (paramCodeSource.certs != null)) {
/* 347 */       if ((paramBoolean) && (this.certs.length != paramCodeSource.certs.length)) {
/* 348 */         return false;
/*     */       }
/* 350 */       for (j = 0; j < this.certs.length; j++) {
/* 351 */         i = 0;
/* 352 */         for (k = 0; k < paramCodeSource.certs.length; k++) {
/* 353 */           if (this.certs[j].equals(paramCodeSource.certs[k])) {
/* 354 */             i = 1;
/* 355 */             break;
/*     */           }
/*     */         }
/* 358 */         if (i == 0) return false;
/*     */       }
/* 360 */       return true;
/*     */     }
/*     */ 
/* 363 */     return false;
/*     */   }
/*     */ 
/*     */   private boolean matchLocation(CodeSource paramCodeSource)
/*     */   {
/* 374 */     if (this.location == null) {
/* 375 */       return true;
/*     */     }
/* 377 */     if ((paramCodeSource == null) || (paramCodeSource.location == null)) {
/* 378 */       return false;
/*     */     }
/* 380 */     if (this.location.equals(paramCodeSource.location)) {
/* 381 */       return true;
/*     */     }
/* 383 */     if (!this.location.getProtocol().equalsIgnoreCase(paramCodeSource.location.getProtocol())) {
/* 384 */       return false;
/*     */     }
/* 386 */     if ((this.location.getPort() != -1) && 
/* 387 */       (this.location.getPort() != paramCodeSource.location.getPort())) {
/* 388 */       return false;
/*     */     }
/*     */ 
/* 391 */     if (this.location.getFile().endsWith("/-"))
/*     */     {
/* 396 */       String str1 = this.location.getFile().substring(0, this.location.getFile().length() - 1);
/*     */ 
/* 398 */       if (!paramCodeSource.location.getFile().startsWith(str1))
/* 399 */         return false;
/* 400 */     } else if (this.location.getFile().endsWith("/*"))
/*     */     {
/* 405 */       int i = paramCodeSource.location.getFile().lastIndexOf('/');
/* 406 */       if (i == -1)
/* 407 */         return false;
/* 408 */       str3 = this.location.getFile().substring(0, this.location.getFile().length() - 1);
/*     */ 
/* 410 */       String str4 = paramCodeSource.location.getFile().substring(0, i + 1);
/* 411 */       if (!str4.equals(str3)) {
/* 412 */         return false;
/*     */       }
/*     */ 
/*     */     }
/* 416 */     else if ((!paramCodeSource.location.getFile().equals(this.location.getFile())) && (!paramCodeSource.location.getFile().equals(this.location.getFile() + "/")))
/*     */     {
/* 418 */       return false;
/*     */     }
/*     */ 
/* 422 */     if ((this.location.getRef() != null) && 
/* 423 */       (!this.location.getRef().equals(paramCodeSource.location.getRef()))) {
/* 424 */       return false;
/*     */     }
/*     */ 
/* 427 */     String str2 = this.location.getHost();
/* 428 */     String str3 = paramCodeSource.location.getHost();
/* 429 */     if ((str2 != null) && (
/* 430 */       ((!"".equals(str2)) && (!"localhost".equals(str2))) || ((!"".equals(str3)) && (!"localhost".equals(str3)))))
/*     */     {
/* 433 */       if (!str2.equalsIgnoreCase(str3)) {
/* 434 */         if (str3 == null) {
/* 435 */           return false;
/*     */         }
/* 437 */         if (this.sp == null) {
/* 438 */           this.sp = new SocketPermission(str2, "resolve");
/*     */         }
/* 440 */         if (paramCodeSource.sp == null) {
/* 441 */           paramCodeSource.sp = new SocketPermission(str3, "resolve");
/*     */         }
/* 443 */         if (!this.sp.implies(paramCodeSource.sp)) {
/* 444 */           return false;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 449 */     return true;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 459 */     StringBuilder localStringBuilder = new StringBuilder();
/* 460 */     localStringBuilder.append("(");
/* 461 */     localStringBuilder.append(this.location);
/*     */     int i;
/* 463 */     if ((this.certs != null) && (this.certs.length > 0)) {
/* 464 */       for (i = 0; i < this.certs.length; i++) {
/* 465 */         localStringBuilder.append(" " + this.certs[i]);
/*     */       }
/*     */     }
/* 468 */     else if ((this.signers != null) && (this.signers.length > 0)) {
/* 469 */       for (i = 0; i < this.signers.length; i++)
/* 470 */         localStringBuilder.append(" " + this.signers[i]);
/*     */     }
/*     */     else {
/* 473 */       localStringBuilder.append(" <no signer certificates>");
/*     */     }
/* 475 */     localStringBuilder.append(")");
/* 476 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*     */     throws IOException
/*     */   {
/* 496 */     paramObjectOutputStream.defaultWriteObject();
/*     */ 
/* 499 */     if ((this.certs == null) || (this.certs.length == 0)) {
/* 500 */       paramObjectOutputStream.writeInt(0);
/*     */     }
/*     */     else {
/* 503 */       paramObjectOutputStream.writeInt(this.certs.length);
/*     */ 
/* 505 */       for (int i = 0; i < this.certs.length; i++) {
/* 506 */         Certificate localCertificate = this.certs[i];
/*     */         try {
/* 508 */           paramObjectOutputStream.writeUTF(localCertificate.getType());
/* 509 */           byte[] arrayOfByte = localCertificate.getEncoded();
/* 510 */           paramObjectOutputStream.writeInt(arrayOfByte.length);
/* 511 */           paramObjectOutputStream.write(arrayOfByte);
/*     */         } catch (CertificateEncodingException localCertificateEncodingException) {
/* 513 */           throw new IOException(localCertificateEncodingException.getMessage());
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 519 */     if ((this.signers != null) && (this.signers.length > 0))
/* 520 */       paramObjectOutputStream.writeObject(this.signers);
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 531 */     Hashtable localHashtable = null;
/*     */ 
/* 533 */     paramObjectInputStream.defaultReadObject();
/*     */ 
/* 536 */     int i = paramObjectInputStream.readInt();
/* 537 */     if (i > 0)
/*     */     {
/* 540 */       localHashtable = new Hashtable(3);
/* 541 */       this.certs = new Certificate[i];
/*     */     }
/*     */ 
/* 544 */     for (int j = 0; j < i; j++)
/*     */     {
/* 547 */       String str = paramObjectInputStream.readUTF();
/*     */       CertificateFactory localCertificateFactory;
/* 548 */       if (localHashtable.containsKey(str))
/*     */       {
/* 550 */         localCertificateFactory = (CertificateFactory)localHashtable.get(str);
/*     */       }
/*     */       else {
/*     */         try {
/* 554 */           localCertificateFactory = CertificateFactory.getInstance(str);
/*     */         } catch (CertificateException localCertificateException1) {
/* 556 */           throw new ClassNotFoundException("Certificate factory for " + str + " not found");
/*     */         }
/*     */ 
/* 560 */         localHashtable.put(str, localCertificateFactory);
/*     */       }
/*     */ 
/* 563 */       byte[] arrayOfByte = null;
/*     */       try {
/* 565 */         arrayOfByte = new byte[paramObjectInputStream.readInt()];
/*     */       } catch (OutOfMemoryError localOutOfMemoryError) {
/* 567 */         throw new IOException("Certificate too big");
/*     */       }
/* 569 */       paramObjectInputStream.readFully(arrayOfByte);
/* 570 */       ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(arrayOfByte);
/*     */       try {
/* 572 */         this.certs[j] = localCertificateFactory.generateCertificate(localByteArrayInputStream);
/*     */       } catch (CertificateException localCertificateException2) {
/* 574 */         throw new IOException(localCertificateException2.getMessage());
/*     */       }
/* 576 */       localByteArrayInputStream.close();
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 581 */       this.signers = ((CodeSigner[])((CodeSigner[])paramObjectInputStream.readObject()).clone());
/*     */     }
/*     */     catch (IOException localIOException)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   private CodeSigner[] convertCertArrayToSignerArray(Certificate[] paramArrayOfCertificate)
/*     */   {
/* 597 */     if (paramArrayOfCertificate == null) {
/* 598 */       return null;
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 603 */       if (this.factory == null) {
/* 604 */         this.factory = CertificateFactory.getInstance("X.509");
/*     */       }
/*     */ 
/* 608 */       int i = 0;
/* 609 */       ArrayList localArrayList1 = new ArrayList();
/* 610 */       while (i < paramArrayOfCertificate.length) {
/* 611 */         ArrayList localArrayList2 = new ArrayList();
/*     */ 
/* 613 */         localArrayList2.add(paramArrayOfCertificate[(i++)]);
/* 614 */         int j = i;
/*     */ 
/* 619 */         while ((j < paramArrayOfCertificate.length) && ((paramArrayOfCertificate[j] instanceof X509Certificate)) && (((X509Certificate)paramArrayOfCertificate[j]).getBasicConstraints() != -1))
/*     */         {
/* 621 */           localArrayList2.add(paramArrayOfCertificate[j]);
/* 622 */           j++;
/*     */         }
/* 624 */         i = j;
/* 625 */         CertPath localCertPath = this.factory.generateCertPath(localArrayList2);
/* 626 */         localArrayList1.add(new CodeSigner(localCertPath, null));
/*     */       }
/*     */ 
/* 629 */       if (localArrayList1.isEmpty()) {
/* 630 */         return null;
/*     */       }
/* 632 */       return (CodeSigner[])localArrayList1.toArray(new CodeSigner[localArrayList1.size()]);
/*     */     }
/*     */     catch (CertificateException localCertificateException) {
/*     */     }
/* 636 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.CodeSource
 * JD-Core Version:    0.6.2
 */