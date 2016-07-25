/*     */ package java.security.cert;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.math.BigInteger;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Date;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import javax.security.auth.x500.X500Principal;
/*     */ import sun.security.util.Debug;
/*     */ import sun.security.util.DerInputStream;
/*     */ import sun.security.x509.CRLNumberExtension;
/*     */ import sun.security.x509.X500Name;
/*     */ 
/*     */ public class X509CRLSelector
/*     */   implements CRLSelector
/*     */ {
/*  79 */   private static final Debug debug = Debug.getInstance("certpath");
/*     */   private HashSet<Object> issuerNames;
/*     */   private HashSet<X500Principal> issuerX500Principals;
/*     */   private BigInteger minCRL;
/*     */   private BigInteger maxCRL;
/*     */   private Date dateAndTime;
/*     */   private X509Certificate certChecking;
/*  86 */   private long skew = 0L;
/*     */ 
/*     */   public void setIssuers(Collection<X500Principal> paramCollection)
/*     */   {
/* 122 */     if ((paramCollection == null) || (paramCollection.isEmpty())) {
/* 123 */       this.issuerNames = null;
/* 124 */       this.issuerX500Principals = null;
/*     */     }
/*     */     else {
/* 127 */       this.issuerX500Principals = new HashSet(paramCollection);
/* 128 */       this.issuerNames = new HashSet();
/* 129 */       for (X500Principal localX500Principal : this.issuerX500Principals)
/* 130 */         this.issuerNames.add(localX500Principal.getEncoded());
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setIssuerNames(Collection<?> paramCollection)
/*     */     throws IOException
/*     */   {
/* 198 */     if ((paramCollection == null) || (paramCollection.size() == 0)) {
/* 199 */       this.issuerNames = null;
/* 200 */       this.issuerX500Principals = null;
/*     */     } else {
/* 202 */       HashSet localHashSet = cloneAndCheckIssuerNames(paramCollection);
/*     */ 
/* 204 */       this.issuerX500Principals = parseIssuerNames(localHashSet);
/* 205 */       this.issuerNames = localHashSet;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void addIssuer(X500Principal paramX500Principal)
/*     */   {
/* 223 */     addIssuerNameInternal(paramX500Principal.getEncoded(), paramX500Principal);
/*     */   }
/*     */ 
/*     */   public void addIssuerName(String paramString)
/*     */     throws IOException
/*     */   {
/* 247 */     addIssuerNameInternal(paramString, new X500Name(paramString).asX500Principal());
/*     */   }
/*     */ 
/*     */   public void addIssuerName(byte[] paramArrayOfByte)
/*     */     throws IOException
/*     */   {
/* 276 */     addIssuerNameInternal(paramArrayOfByte.clone(), new X500Name(paramArrayOfByte).asX500Principal());
/*     */   }
/*     */ 
/*     */   private void addIssuerNameInternal(Object paramObject, X500Principal paramX500Principal)
/*     */   {
/* 290 */     if (this.issuerNames == null) {
/* 291 */       this.issuerNames = new HashSet();
/*     */     }
/* 293 */     if (this.issuerX500Principals == null) {
/* 294 */       this.issuerX500Principals = new HashSet();
/*     */     }
/* 296 */     this.issuerNames.add(paramObject);
/* 297 */     this.issuerX500Principals.add(paramX500Principal);
/*     */   }
/*     */ 
/*     */   private static HashSet<Object> cloneAndCheckIssuerNames(Collection<?> paramCollection)
/*     */     throws IOException
/*     */   {
/* 314 */     HashSet localHashSet = new HashSet();
/* 315 */     Iterator localIterator = paramCollection.iterator();
/* 316 */     while (localIterator.hasNext()) {
/* 317 */       Object localObject = localIterator.next();
/* 318 */       if ((!(localObject instanceof byte[])) && (!(localObject instanceof String)))
/*     */       {
/* 320 */         throw new IOException("name not byte array or String");
/* 321 */       }if ((localObject instanceof byte[]))
/* 322 */         localHashSet.add(((byte[])localObject).clone());
/*     */       else
/* 324 */         localHashSet.add(localObject);
/*     */     }
/* 326 */     return localHashSet;
/*     */   }
/*     */ 
/*     */   private static HashSet<Object> cloneIssuerNames(Collection<Object> paramCollection)
/*     */   {
/*     */     try
/*     */     {
/* 346 */       return cloneAndCheckIssuerNames(paramCollection);
/*     */     } catch (IOException localIOException) {
/* 348 */       throw new RuntimeException(localIOException);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static HashSet<X500Principal> parseIssuerNames(Collection<Object> paramCollection)
/*     */     throws IOException
/*     */   {
/* 366 */     HashSet localHashSet = new HashSet();
/* 367 */     for (Iterator localIterator = paramCollection.iterator(); localIterator.hasNext(); ) {
/* 368 */       Object localObject = localIterator.next();
/* 369 */       if ((localObject instanceof String))
/* 370 */         localHashSet.add(new X500Name((String)localObject).asX500Principal());
/*     */       else {
/*     */         try {
/* 373 */           localHashSet.add(new X500Principal((byte[])localObject));
/*     */         } catch (IllegalArgumentException localIllegalArgumentException) {
/* 375 */           throw ((IOException)new IOException("Invalid name").initCause(localIllegalArgumentException));
/*     */         }
/*     */       }
/*     */     }
/* 379 */     return localHashSet;
/*     */   }
/*     */ 
/*     */   public void setMinCRLNumber(BigInteger paramBigInteger)
/*     */   {
/* 391 */     this.minCRL = paramBigInteger;
/*     */   }
/*     */ 
/*     */   public void setMaxCRLNumber(BigInteger paramBigInteger)
/*     */   {
/* 403 */     this.maxCRL = paramBigInteger;
/*     */   }
/*     */ 
/*     */   public void setDateAndTime(Date paramDate)
/*     */   {
/* 422 */     if (paramDate == null)
/* 423 */       this.dateAndTime = null;
/*     */     else
/* 425 */       this.dateAndTime = new Date(paramDate.getTime());
/* 426 */     this.skew = 0L;
/*     */   }
/*     */ 
/*     */   void setDateAndTime(Date paramDate, long paramLong)
/*     */   {
/* 434 */     this.dateAndTime = (paramDate == null ? null : new Date(paramDate.getTime()));
/*     */ 
/* 436 */     this.skew = paramLong;
/*     */   }
/*     */ 
/*     */   public void setCertificateChecking(X509Certificate paramX509Certificate)
/*     */   {
/* 451 */     this.certChecking = paramX509Certificate;
/*     */   }
/*     */ 
/*     */   public Collection<X500Principal> getIssuers()
/*     */   {
/* 469 */     if (this.issuerX500Principals == null) {
/* 470 */       return null;
/*     */     }
/* 472 */     return Collections.unmodifiableCollection(this.issuerX500Principals);
/*     */   }
/*     */ 
/*     */   public Collection<Object> getIssuerNames()
/*     */   {
/* 499 */     if (this.issuerNames == null) {
/* 500 */       return null;
/*     */     }
/* 502 */     return cloneIssuerNames(this.issuerNames);
/*     */   }
/*     */ 
/*     */   public BigInteger getMinCRL()
/*     */   {
/* 513 */     return this.minCRL;
/*     */   }
/*     */ 
/*     */   public BigInteger getMaxCRL()
/*     */   {
/* 525 */     return this.maxCRL;
/*     */   }
/*     */ 
/*     */   public Date getDateAndTime()
/*     */   {
/* 543 */     if (this.dateAndTime == null)
/* 544 */       return null;
/* 545 */     return (Date)this.dateAndTime.clone();
/*     */   }
/*     */ 
/*     */   public X509Certificate getCertificateChecking()
/*     */   {
/* 559 */     return this.certChecking;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 569 */     StringBuffer localStringBuffer = new StringBuffer();
/* 570 */     localStringBuffer.append("X509CRLSelector: [\n");
/* 571 */     if (this.issuerNames != null) {
/* 572 */       localStringBuffer.append("  IssuerNames:\n");
/* 573 */       Iterator localIterator = this.issuerNames.iterator();
/* 574 */       while (localIterator.hasNext())
/* 575 */         localStringBuffer.append("    " + localIterator.next() + "\n");
/*     */     }
/* 577 */     if (this.minCRL != null)
/* 578 */       localStringBuffer.append("  minCRLNumber: " + this.minCRL + "\n");
/* 579 */     if (this.maxCRL != null)
/* 580 */       localStringBuffer.append("  maxCRLNumber: " + this.maxCRL + "\n");
/* 581 */     if (this.dateAndTime != null)
/* 582 */       localStringBuffer.append("  dateAndTime: " + this.dateAndTime + "\n");
/* 583 */     if (this.certChecking != null)
/* 584 */       localStringBuffer.append("  Certificate being checked: " + this.certChecking + "\n");
/* 585 */     localStringBuffer.append("]");
/* 586 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   public boolean match(CRL paramCRL)
/*     */   {
/* 597 */     if (!(paramCRL instanceof X509CRL)) {
/* 598 */       return false;
/*     */     }
/* 600 */     X509CRL localX509CRL = (X509CRL)paramCRL;
/*     */     Object localObject1;
/*     */     Object localObject2;
/* 603 */     if (this.issuerNames != null) {
/* 604 */       localObject1 = localX509CRL.getIssuerX500Principal();
/* 605 */       localObject2 = this.issuerX500Principals.iterator();
/* 606 */       int i = 0;
/* 607 */       while ((i == 0) && (((Iterator)localObject2).hasNext())) {
/* 608 */         if (((X500Principal)((Iterator)localObject2).next()).equals(localObject1)) {
/* 609 */           i = 1;
/*     */         }
/*     */       }
/* 612 */       if (i == 0) {
/* 613 */         if (debug != null) {
/* 614 */           debug.println("X509CRLSelector.match: issuer DNs don't match");
/*     */         }
/*     */ 
/* 617 */         return false;
/*     */       }
/*     */     }
/*     */     Object localObject3;
/* 621 */     if ((this.minCRL != null) || (this.maxCRL != null))
/*     */     {
/* 623 */       localObject1 = localX509CRL.getExtensionValue("2.5.29.20");
/* 624 */       if ((localObject1 == null) && 
/* 625 */         (debug != null)) {
/* 626 */         debug.println("X509CRLSelector.match: no CRLNumber");
/*     */       }
/*     */ 
/*     */       try
/*     */       {
/* 631 */         DerInputStream localDerInputStream = new DerInputStream((byte[])localObject1);
/* 632 */         localObject3 = localDerInputStream.getOctetString();
/* 633 */         CRLNumberExtension localCRLNumberExtension = new CRLNumberExtension(Boolean.FALSE, localObject3);
/*     */ 
/* 635 */         localObject2 = (BigInteger)localCRLNumberExtension.get("value");
/*     */       } catch (IOException localIOException) {
/* 637 */         if (debug != null) {
/* 638 */           debug.println("X509CRLSelector.match: exception in decoding CRL number");
/*     */         }
/*     */ 
/* 641 */         return false;
/*     */       }
/*     */ 
/* 645 */       if ((this.minCRL != null) && 
/* 646 */         (((BigInteger)localObject2).compareTo(this.minCRL) < 0)) {
/* 647 */         if (debug != null) {
/* 648 */           debug.println("X509CRLSelector.match: CRLNumber too small");
/*     */         }
/* 650 */         return false;
/*     */       }
/*     */ 
/* 655 */       if ((this.maxCRL != null) && 
/* 656 */         (((BigInteger)localObject2).compareTo(this.maxCRL) > 0)) {
/* 657 */         if (debug != null) {
/* 658 */           debug.println("X509CRLSelector.match: CRLNumber too large");
/*     */         }
/* 660 */         return false;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 667 */     if (this.dateAndTime != null) {
/* 668 */       localObject1 = localX509CRL.getThisUpdate();
/* 669 */       localObject2 = localX509CRL.getNextUpdate();
/* 670 */       if (localObject2 == null) {
/* 671 */         if (debug != null) {
/* 672 */           debug.println("X509CRLSelector.match: nextUpdate null");
/*     */         }
/* 674 */         return false;
/*     */       }
/* 676 */       Date localDate = this.dateAndTime;
/* 677 */       localObject3 = this.dateAndTime;
/* 678 */       if (this.skew > 0L) {
/* 679 */         localDate = new Date(this.dateAndTime.getTime() + this.skew);
/* 680 */         localObject3 = new Date(this.dateAndTime.getTime() - this.skew);
/*     */       }
/* 682 */       if ((((Date)localObject3).after((Date)localObject2)) || (localDate.before((Date)localObject1)))
/*     */       {
/* 684 */         if (debug != null) {
/* 685 */           debug.println("X509CRLSelector.match: update out of range");
/*     */         }
/* 687 */         return false;
/*     */       }
/*     */     }
/*     */ 
/* 691 */     return true;
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/*     */     try
/*     */     {
/* 701 */       X509CRLSelector localX509CRLSelector = (X509CRLSelector)super.clone();
/* 702 */       if (this.issuerNames != null) {
/* 703 */         localX509CRLSelector.issuerNames = new HashSet(this.issuerNames);
/*     */ 
/* 705 */         localX509CRLSelector.issuerX500Principals = new HashSet(this.issuerX500Principals);
/*     */       }
/*     */ 
/* 708 */       return localX509CRLSelector;
/*     */     }
/*     */     catch (CloneNotSupportedException localCloneNotSupportedException) {
/* 711 */       throw new InternalError(localCloneNotSupportedException.toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  76 */     CertPathHelperImpl.initialize();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.cert.X509CRLSelector
 * JD-Core Version:    0.6.2
 */