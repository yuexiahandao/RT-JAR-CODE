/*     */ package sun.security.x509;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.security.cert.CertificateEncodingException;
/*     */ import java.security.cert.CertificateException;
/*     */ import java.security.cert.CertificateParsingException;
/*     */ import java.util.Collection;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import sun.misc.HexDumpEncoder;
/*     */ import sun.security.util.DerInputStream;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.DerValue;
/*     */ 
/*     */ public class X509CertInfo
/*     */   implements CertAttrSet<String>
/*     */ {
/*     */   public static final String IDENT = "x509.info";
/*     */   public static final String NAME = "info";
/*     */   public static final String VERSION = "version";
/*     */   public static final String SERIAL_NUMBER = "serialNumber";
/*     */   public static final String ALGORITHM_ID = "algorithmID";
/*     */   public static final String ISSUER = "issuer";
/*     */   public static final String VALIDITY = "validity";
/*     */   public static final String SUBJECT = "subject";
/*     */   public static final String KEY = "key";
/*     */   public static final String ISSUER_ID = "issuerID";
/*     */   public static final String SUBJECT_ID = "subjectID";
/*     */   public static final String EXTENSIONS = "extensions";
/*  83 */   protected CertificateVersion version = new CertificateVersion();
/*  84 */   protected CertificateSerialNumber serialNum = null;
/*  85 */   protected CertificateAlgorithmId algId = null;
/*  86 */   protected CertificateIssuerName issuer = null;
/*  87 */   protected CertificateValidity interval = null;
/*  88 */   protected CertificateSubjectName subject = null;
/*  89 */   protected CertificateX509Key pubKey = null;
/*     */ 
/*  92 */   protected CertificateIssuerUniqueIdentity issuerUniqueId = null;
/*  93 */   protected CertificateSubjectUniqueIdentity subjectUniqueId = null;
/*     */ 
/*  96 */   protected CertificateExtensions extensions = null;
/*     */   private static final int ATTR_VERSION = 1;
/*     */   private static final int ATTR_SERIAL = 2;
/*     */   private static final int ATTR_ALGORITHM = 3;
/*     */   private static final int ATTR_ISSUER = 4;
/*     */   private static final int ATTR_VALIDITY = 5;
/*     */   private static final int ATTR_SUBJECT = 6;
/*     */   private static final int ATTR_KEY = 7;
/*     */   private static final int ATTR_ISSUER_ID = 8;
/*     */   private static final int ATTR_SUBJECT_ID = 9;
/*     */   private static final int ATTR_EXTENSIONS = 10;
/* 111 */   private byte[] rawCertInfo = null;
/*     */ 
/* 114 */   private static final Map<String, Integer> map = new HashMap();
/*     */ 
/*     */   public X509CertInfo()
/*     */   {
/*     */   }
/*     */ 
/*     */   public X509CertInfo(byte[] paramArrayOfByte)
/*     */     throws CertificateParsingException
/*     */   {
/*     */     try
/*     */     {
/* 148 */       DerValue localDerValue = new DerValue(paramArrayOfByte);
/*     */ 
/* 150 */       parse(localDerValue);
/*     */     } catch (IOException localIOException) {
/* 152 */       CertificateParsingException localCertificateParsingException = new CertificateParsingException(localIOException.toString());
/*     */ 
/* 154 */       localCertificateParsingException.initCause(localIOException);
/* 155 */       throw localCertificateParsingException;
/*     */     }
/*     */   }
/*     */ 
/*     */   public X509CertInfo(DerValue paramDerValue)
/*     */     throws CertificateParsingException
/*     */   {
/*     */     try
/*     */     {
/* 169 */       parse(paramDerValue);
/*     */     } catch (IOException localIOException) {
/* 171 */       CertificateParsingException localCertificateParsingException = new CertificateParsingException(localIOException.toString());
/*     */ 
/* 173 */       localCertificateParsingException.initCause(localIOException);
/* 174 */       throw localCertificateParsingException;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void encode(OutputStream paramOutputStream)
/*     */     throws CertificateException, IOException
/*     */   {
/* 187 */     if (this.rawCertInfo == null) {
/* 188 */       DerOutputStream localDerOutputStream = new DerOutputStream();
/* 189 */       emit(localDerOutputStream);
/* 190 */       this.rawCertInfo = localDerOutputStream.toByteArray();
/*     */     }
/* 192 */     paramOutputStream.write((byte[])this.rawCertInfo.clone());
/*     */   }
/*     */ 
/*     */   public Enumeration<String> getElements()
/*     */   {
/* 200 */     AttributeNameEnumeration localAttributeNameEnumeration = new AttributeNameEnumeration();
/* 201 */     localAttributeNameEnumeration.addElement("version");
/* 202 */     localAttributeNameEnumeration.addElement("serialNumber");
/* 203 */     localAttributeNameEnumeration.addElement("algorithmID");
/* 204 */     localAttributeNameEnumeration.addElement("issuer");
/* 205 */     localAttributeNameEnumeration.addElement("validity");
/* 206 */     localAttributeNameEnumeration.addElement("subject");
/* 207 */     localAttributeNameEnumeration.addElement("key");
/* 208 */     localAttributeNameEnumeration.addElement("issuerID");
/* 209 */     localAttributeNameEnumeration.addElement("subjectID");
/* 210 */     localAttributeNameEnumeration.addElement("extensions");
/*     */ 
/* 212 */     return localAttributeNameEnumeration.elements();
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 219 */     return "info";
/*     */   }
/*     */ 
/*     */   public byte[] getEncodedInfo()
/*     */     throws CertificateEncodingException
/*     */   {
/*     */     try
/*     */     {
/* 229 */       if (this.rawCertInfo == null) {
/* 230 */         DerOutputStream localDerOutputStream = new DerOutputStream();
/* 231 */         emit(localDerOutputStream);
/* 232 */         this.rawCertInfo = localDerOutputStream.toByteArray();
/*     */       }
/* 234 */       return (byte[])this.rawCertInfo.clone();
/*     */     } catch (IOException localIOException) {
/* 236 */       throw new CertificateEncodingException(localIOException.toString());
/*     */     } catch (CertificateException localCertificateException) {
/* 238 */       throw new CertificateEncodingException(localCertificateException.toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 251 */     if ((paramObject instanceof X509CertInfo)) {
/* 252 */       return equals((X509CertInfo)paramObject);
/*     */     }
/* 254 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean equals(X509CertInfo paramX509CertInfo)
/*     */   {
/* 266 */     if (this == paramX509CertInfo)
/* 267 */       return true;
/* 268 */     if ((this.rawCertInfo == null) || (paramX509CertInfo.rawCertInfo == null))
/* 269 */       return false;
/* 270 */     if (this.rawCertInfo.length != paramX509CertInfo.rawCertInfo.length) {
/* 271 */       return false;
/*     */     }
/* 273 */     for (int i = 0; i < this.rawCertInfo.length; i++) {
/* 274 */       if (this.rawCertInfo[i] != paramX509CertInfo.rawCertInfo[i]) {
/* 275 */         return false;
/*     */       }
/*     */     }
/* 278 */     return true;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 286 */     int i = 0;
/*     */ 
/* 288 */     for (int j = 1; j < this.rawCertInfo.length; j++) {
/* 289 */       i += this.rawCertInfo[j] * j;
/*     */     }
/* 291 */     return i;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 299 */     if ((this.subject == null) || (this.pubKey == null) || (this.interval == null) || (this.issuer == null) || (this.algId == null) || (this.serialNum == null))
/*     */     {
/* 301 */       throw new NullPointerException("X.509 cert is incomplete");
/*     */     }
/* 303 */     StringBuilder localStringBuilder = new StringBuilder();
/*     */ 
/* 305 */     localStringBuilder.append("[\n");
/* 306 */     localStringBuilder.append("  " + this.version.toString() + "\n");
/* 307 */     localStringBuilder.append("  Subject: " + this.subject.toString() + "\n");
/* 308 */     localStringBuilder.append("  Signature Algorithm: " + this.algId.toString() + "\n");
/* 309 */     localStringBuilder.append("  Key:  " + this.pubKey.toString() + "\n");
/* 310 */     localStringBuilder.append("  " + this.interval.toString() + "\n");
/* 311 */     localStringBuilder.append("  Issuer: " + this.issuer.toString() + "\n");
/* 312 */     localStringBuilder.append("  " + this.serialNum.toString() + "\n");
/*     */ 
/* 315 */     if (this.issuerUniqueId != null) {
/* 316 */       localStringBuilder.append("  Issuer Id:\n" + this.issuerUniqueId.toString() + "\n");
/*     */     }
/* 318 */     if (this.subjectUniqueId != null)
/* 319 */       localStringBuilder.append("  Subject Id:\n" + this.subjectUniqueId.toString() + "\n");
/*     */     Object localObject;
/*     */     int j;
/*     */     Iterator localIterator;
/* 321 */     if (this.extensions != null) {
/* 322 */       Collection localCollection = this.extensions.getAllExtensions();
/* 323 */       Object[] arrayOfObject = localCollection.toArray();
/* 324 */       localStringBuilder.append("\nCertificate Extensions: " + arrayOfObject.length);
/* 325 */       for (int i = 0; i < arrayOfObject.length; i++) {
/* 326 */         localStringBuilder.append("\n[" + (i + 1) + "]: ");
/* 327 */         Extension localExtension = (Extension)arrayOfObject[i];
/*     */         try {
/* 329 */           if (OIDMap.getClass(localExtension.getExtensionId()) == null) {
/* 330 */             localStringBuilder.append(localExtension.toString());
/* 331 */             byte[] arrayOfByte = localExtension.getExtensionValue();
/* 332 */             if (arrayOfByte != null) {
/* 333 */               localObject = new DerOutputStream();
/* 334 */               ((DerOutputStream)localObject).putOctetString(arrayOfByte);
/* 335 */               arrayOfByte = ((DerOutputStream)localObject).toByteArray();
/* 336 */               HexDumpEncoder localHexDumpEncoder = new HexDumpEncoder();
/* 337 */               localStringBuilder.append("Extension unknown: DER encoded OCTET string =\n" + localHexDumpEncoder.encodeBuffer(arrayOfByte) + "\n");
/*     */             }
/*     */           }
/*     */           else
/*     */           {
/* 342 */             localStringBuilder.append(localExtension.toString());
/*     */           }
/*     */         } catch (Exception localException) { localStringBuilder.append(", Error parsing this extension"); }
/*     */ 
/*     */       }
/* 347 */       Map localMap = this.extensions.getUnparseableExtensions();
/* 348 */       if (!localMap.isEmpty()) {
/* 349 */         localStringBuilder.append("\nUnparseable certificate extensions: " + localMap.size());
/* 350 */         j = 1;
/* 351 */         for (localIterator = localMap.values().iterator(); localIterator.hasNext(); ) { localObject = (Extension)localIterator.next();
/* 352 */           localStringBuilder.append("\n[" + j++ + "]: ");
/* 353 */           localStringBuilder.append(localObject);
/*     */         }
/*     */       }
/*     */     }
/* 357 */     localStringBuilder.append("\n]");
/* 358 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   public void set(String paramString, Object paramObject)
/*     */     throws CertificateException, IOException
/*     */   {
/* 371 */     X509AttributeName localX509AttributeName = new X509AttributeName(paramString);
/*     */ 
/* 373 */     int i = attributeMap(localX509AttributeName.getPrefix());
/* 374 */     if (i == 0) {
/* 375 */       throw new CertificateException("Attribute name not recognized: " + paramString);
/*     */     }
/*     */ 
/* 379 */     this.rawCertInfo = null;
/* 380 */     String str = localX509AttributeName.getSuffix();
/*     */ 
/* 382 */     switch (i) {
/*     */     case 1:
/* 384 */       if (str == null)
/* 385 */         setVersion(paramObject);
/*     */       else {
/* 387 */         this.version.set(str, paramObject);
/*     */       }
/* 389 */       break;
/*     */     case 2:
/* 392 */       if (str == null)
/* 393 */         setSerialNumber(paramObject);
/*     */       else {
/* 395 */         this.serialNum.set(str, paramObject);
/*     */       }
/* 397 */       break;
/*     */     case 3:
/* 400 */       if (str == null)
/* 401 */         setAlgorithmId(paramObject);
/*     */       else {
/* 403 */         this.algId.set(str, paramObject);
/*     */       }
/* 405 */       break;
/*     */     case 4:
/* 408 */       if (str == null)
/* 409 */         setIssuer(paramObject);
/*     */       else {
/* 411 */         this.issuer.set(str, paramObject);
/*     */       }
/* 413 */       break;
/*     */     case 5:
/* 416 */       if (str == null)
/* 417 */         setValidity(paramObject);
/*     */       else {
/* 419 */         this.interval.set(str, paramObject);
/*     */       }
/* 421 */       break;
/*     */     case 6:
/* 424 */       if (str == null)
/* 425 */         setSubject(paramObject);
/*     */       else {
/* 427 */         this.subject.set(str, paramObject);
/*     */       }
/* 429 */       break;
/*     */     case 7:
/* 432 */       if (str == null)
/* 433 */         setKey(paramObject);
/*     */       else {
/* 435 */         this.pubKey.set(str, paramObject);
/*     */       }
/* 437 */       break;
/*     */     case 8:
/* 440 */       if (str == null)
/* 441 */         setIssuerUniqueId(paramObject);
/*     */       else {
/* 443 */         this.issuerUniqueId.set(str, paramObject);
/*     */       }
/* 445 */       break;
/*     */     case 9:
/* 448 */       if (str == null)
/* 449 */         setSubjectUniqueId(paramObject);
/*     */       else {
/* 451 */         this.subjectUniqueId.set(str, paramObject);
/*     */       }
/* 453 */       break;
/*     */     case 10:
/* 456 */       if (str == null) {
/* 457 */         setExtensions(paramObject);
/*     */       } else {
/* 459 */         if (this.extensions == null)
/* 460 */           this.extensions = new CertificateExtensions();
/* 461 */         this.extensions.set(str, paramObject);
/*     */       }
/*     */       break;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void delete(String paramString)
/*     */     throws CertificateException, IOException
/*     */   {
/* 476 */     X509AttributeName localX509AttributeName = new X509AttributeName(paramString);
/*     */ 
/* 478 */     int i = attributeMap(localX509AttributeName.getPrefix());
/* 479 */     if (i == 0) {
/* 480 */       throw new CertificateException("Attribute name not recognized: " + paramString);
/*     */     }
/*     */ 
/* 484 */     this.rawCertInfo = null;
/* 485 */     String str = localX509AttributeName.getSuffix();
/*     */ 
/* 487 */     switch (i) {
/*     */     case 1:
/* 489 */       if (str == null)
/* 490 */         this.version = null;
/*     */       else {
/* 492 */         this.version.delete(str);
/*     */       }
/* 494 */       break;
/*     */     case 2:
/* 496 */       if (str == null)
/* 497 */         this.serialNum = null;
/*     */       else {
/* 499 */         this.serialNum.delete(str);
/*     */       }
/* 501 */       break;
/*     */     case 3:
/* 503 */       if (str == null)
/* 504 */         this.algId = null;
/*     */       else {
/* 506 */         this.algId.delete(str);
/*     */       }
/* 508 */       break;
/*     */     case 4:
/* 510 */       if (str == null)
/* 511 */         this.issuer = null;
/*     */       else {
/* 513 */         this.issuer.delete(str);
/*     */       }
/* 515 */       break;
/*     */     case 5:
/* 517 */       if (str == null)
/* 518 */         this.interval = null;
/*     */       else {
/* 520 */         this.interval.delete(str);
/*     */       }
/* 522 */       break;
/*     */     case 6:
/* 524 */       if (str == null)
/* 525 */         this.subject = null;
/*     */       else {
/* 527 */         this.subject.delete(str);
/*     */       }
/* 529 */       break;
/*     */     case 7:
/* 531 */       if (str == null)
/* 532 */         this.pubKey = null;
/*     */       else {
/* 534 */         this.pubKey.delete(str);
/*     */       }
/* 536 */       break;
/*     */     case 8:
/* 538 */       if (str == null)
/* 539 */         this.issuerUniqueId = null;
/*     */       else {
/* 541 */         this.issuerUniqueId.delete(str);
/*     */       }
/* 543 */       break;
/*     */     case 9:
/* 545 */       if (str == null)
/* 546 */         this.subjectUniqueId = null;
/*     */       else {
/* 548 */         this.subjectUniqueId.delete(str);
/*     */       }
/* 550 */       break;
/*     */     case 10:
/* 552 */       if (str == null) {
/* 553 */         this.extensions = null;
/*     */       }
/* 555 */       else if (this.extensions != null)
/* 556 */         this.extensions.delete(str);
/*     */       break;
/*     */     }
/*     */   }
/*     */ 
/*     */   public Object get(String paramString)
/*     */     throws CertificateException, IOException
/*     */   {
/* 572 */     X509AttributeName localX509AttributeName = new X509AttributeName(paramString);
/*     */ 
/* 574 */     int i = attributeMap(localX509AttributeName.getPrefix());
/* 575 */     if (i == 0) {
/* 576 */       throw new CertificateParsingException("Attribute name not recognized: " + paramString);
/*     */     }
/*     */ 
/* 579 */     String str = localX509AttributeName.getSuffix();
/*     */ 
/* 581 */     switch (i) {
/*     */     case 10:
/* 583 */       if (str == null) {
/* 584 */         return this.extensions;
/*     */       }
/* 586 */       if (this.extensions == null) {
/* 587 */         return null;
/*     */       }
/* 589 */       return this.extensions.get(str);
/*     */     case 6:
/* 593 */       if (str == null) {
/* 594 */         return this.subject;
/*     */       }
/* 596 */       return this.subject.get(str);
/*     */     case 4:
/* 599 */       if (str == null) {
/* 600 */         return this.issuer;
/*     */       }
/* 602 */       return this.issuer.get(str);
/*     */     case 7:
/* 605 */       if (str == null) {
/* 606 */         return this.pubKey;
/*     */       }
/* 608 */       return this.pubKey.get(str);
/*     */     case 3:
/* 611 */       if (str == null) {
/* 612 */         return this.algId;
/*     */       }
/* 614 */       return this.algId.get(str);
/*     */     case 5:
/* 617 */       if (str == null) {
/* 618 */         return this.interval;
/*     */       }
/* 620 */       return this.interval.get(str);
/*     */     case 1:
/* 623 */       if (str == null) {
/* 624 */         return this.version;
/*     */       }
/* 626 */       return this.version.get(str);
/*     */     case 2:
/* 629 */       if (str == null) {
/* 630 */         return this.serialNum;
/*     */       }
/* 632 */       return this.serialNum.get(str);
/*     */     case 8:
/* 635 */       if (str == null) {
/* 636 */         return this.issuerUniqueId;
/*     */       }
/* 638 */       if (this.issuerUniqueId == null) {
/* 639 */         return null;
/*     */       }
/* 641 */       return this.issuerUniqueId.get(str);
/*     */     case 9:
/* 644 */       if (str == null) {
/* 645 */         return this.subjectUniqueId;
/*     */       }
/* 647 */       if (this.subjectUniqueId == null) {
/* 648 */         return null;
/*     */       }
/* 650 */       return this.subjectUniqueId.get(str);
/*     */     }
/*     */ 
/* 653 */     return null;
/*     */   }
/*     */ 
/*     */   private void parse(DerValue paramDerValue)
/*     */     throws CertificateParsingException, IOException
/*     */   {
/* 664 */     if (paramDerValue.tag != 48) {
/* 665 */       throw new CertificateParsingException("signed fields invalid");
/*     */     }
/* 667 */     this.rawCertInfo = paramDerValue.toByteArray();
/*     */ 
/* 669 */     DerInputStream localDerInputStream = paramDerValue.data;
/*     */ 
/* 672 */     DerValue localDerValue = localDerInputStream.getDerValue();
/* 673 */     if (localDerValue.isContextSpecific((byte)0)) {
/* 674 */       this.version = new CertificateVersion(localDerValue);
/* 675 */       localDerValue = localDerInputStream.getDerValue();
/*     */     }
/*     */ 
/* 679 */     this.serialNum = new CertificateSerialNumber(localDerValue);
/*     */ 
/* 682 */     this.algId = new CertificateAlgorithmId(localDerInputStream);
/*     */ 
/* 685 */     this.issuer = new CertificateIssuerName(localDerInputStream);
/* 686 */     X500Name localX500Name1 = (X500Name)this.issuer.get("dname");
/* 687 */     if (localX500Name1.isEmpty()) {
/* 688 */       throw new CertificateParsingException("Empty issuer DN not allowed in X509Certificates");
/*     */     }
/*     */ 
/* 693 */     this.interval = new CertificateValidity(localDerInputStream);
/*     */ 
/* 696 */     this.subject = new CertificateSubjectName(localDerInputStream);
/* 697 */     X500Name localX500Name2 = (X500Name)this.subject.get("dname");
/* 698 */     if ((this.version.compare(0) == 0) && (localX500Name2.isEmpty()))
/*     */     {
/* 700 */       throw new CertificateParsingException("Empty subject DN not allowed in v1 certificate");
/*     */     }
/*     */ 
/* 705 */     this.pubKey = new CertificateX509Key(localDerInputStream);
/*     */ 
/* 708 */     if (localDerInputStream.available() != 0) {
/* 709 */       if (this.version.compare(0) == 0) {
/* 710 */         throw new CertificateParsingException("no more data allowed for version 1 certificate");
/*     */       }
/*     */     }
/*     */     else {
/* 714 */       return;
/*     */     }
/*     */ 
/* 718 */     localDerValue = localDerInputStream.getDerValue();
/* 719 */     if (localDerValue.isContextSpecific((byte)1)) {
/* 720 */       this.issuerUniqueId = new CertificateIssuerUniqueIdentity(localDerValue);
/* 721 */       if (localDerInputStream.available() == 0)
/* 722 */         return;
/* 723 */       localDerValue = localDerInputStream.getDerValue();
/*     */     }
/*     */ 
/* 727 */     if (localDerValue.isContextSpecific((byte)2)) {
/* 728 */       this.subjectUniqueId = new CertificateSubjectUniqueIdentity(localDerValue);
/* 729 */       if (localDerInputStream.available() == 0)
/* 730 */         return;
/* 731 */       localDerValue = localDerInputStream.getDerValue();
/*     */     }
/*     */ 
/* 735 */     if (this.version.compare(2) != 0) {
/* 736 */       throw new CertificateParsingException("Extensions not allowed in v2 certificate");
/*     */     }
/*     */ 
/* 739 */     if ((localDerValue.isConstructed()) && (localDerValue.isContextSpecific((byte)3))) {
/* 740 */       this.extensions = new CertificateExtensions(localDerValue.data);
/*     */     }
/*     */ 
/* 744 */     verifyCert(this.subject, this.extensions);
/*     */   }
/*     */ 
/*     */   private void verifyCert(CertificateSubjectName paramCertificateSubjectName, CertificateExtensions paramCertificateExtensions)
/*     */     throws CertificateParsingException, IOException
/*     */   {
/* 756 */     X500Name localX500Name = (X500Name)paramCertificateSubjectName.get("dname");
/* 757 */     if (localX500Name.isEmpty()) {
/* 758 */       if (paramCertificateExtensions == null) {
/* 759 */         throw new CertificateParsingException("X.509 Certificate is incomplete: subject field is empty, and certificate has no extensions");
/*     */       }
/*     */ 
/* 763 */       SubjectAlternativeNameExtension localSubjectAlternativeNameExtension = null;
/* 764 */       Object localObject = null;
/* 765 */       GeneralNames localGeneralNames = null;
/*     */       try {
/* 767 */         localSubjectAlternativeNameExtension = (SubjectAlternativeNameExtension)paramCertificateExtensions.get("SubjectAlternativeName");
/*     */ 
/* 769 */         localGeneralNames = (GeneralNames)localSubjectAlternativeNameExtension.get("subject_name");
/*     */       }
/*     */       catch (IOException localIOException) {
/* 772 */         throw new CertificateParsingException("X.509 Certificate is incomplete: subject field is empty, and SubjectAlternativeName extension is absent");
/*     */       }
/*     */ 
/* 778 */       if ((localGeneralNames == null) || (localGeneralNames.isEmpty())) {
/* 779 */         throw new CertificateParsingException("X.509 Certificate is incomplete: subject field is empty, and SubjectAlternativeName extension is empty");
/*     */       }
/*     */ 
/* 782 */       if (!localSubjectAlternativeNameExtension.isCritical())
/* 783 */         throw new CertificateParsingException("X.509 Certificate is incomplete: SubjectAlternativeName extension MUST be marked critical when subject field is empty");
/*     */     }
/*     */   }
/*     */ 
/*     */   private void emit(DerOutputStream paramDerOutputStream)
/*     */     throws CertificateException, IOException
/*     */   {
/* 795 */     DerOutputStream localDerOutputStream = new DerOutputStream();
/*     */ 
/* 798 */     this.version.encode(localDerOutputStream);
/*     */ 
/* 802 */     this.serialNum.encode(localDerOutputStream);
/* 803 */     this.algId.encode(localDerOutputStream);
/*     */ 
/* 805 */     if ((this.version.compare(0) == 0) && (this.issuer.toString() == null))
/*     */     {
/* 807 */       throw new CertificateParsingException("Null issuer DN not allowed in v1 certificate");
/*     */     }
/*     */ 
/* 810 */     this.issuer.encode(localDerOutputStream);
/* 811 */     this.interval.encode(localDerOutputStream);
/*     */ 
/* 814 */     if ((this.version.compare(0) == 0) && (this.subject.toString() == null))
/*     */     {
/* 816 */       throw new CertificateParsingException("Null subject DN not allowed in v1 certificate");
/*     */     }
/* 818 */     this.subject.encode(localDerOutputStream);
/* 819 */     this.pubKey.encode(localDerOutputStream);
/*     */ 
/* 822 */     if (this.issuerUniqueId != null) {
/* 823 */       this.issuerUniqueId.encode(localDerOutputStream);
/*     */     }
/* 825 */     if (this.subjectUniqueId != null) {
/* 826 */       this.subjectUniqueId.encode(localDerOutputStream);
/*     */     }
/*     */ 
/* 830 */     if (this.extensions != null) {
/* 831 */       this.extensions.encode(localDerOutputStream);
/*     */     }
/*     */ 
/* 835 */     paramDerOutputStream.write((byte)48, localDerOutputStream);
/*     */   }
/*     */ 
/*     */   private int attributeMap(String paramString)
/*     */   {
/* 842 */     Integer localInteger = (Integer)map.get(paramString);
/* 843 */     if (localInteger == null) {
/* 844 */       return 0;
/*     */     }
/* 846 */     return localInteger.intValue();
/*     */   }
/*     */ 
/*     */   private void setVersion(Object paramObject)
/*     */     throws CertificateException
/*     */   {
/* 856 */     if (!(paramObject instanceof CertificateVersion)) {
/* 857 */       throw new CertificateException("Version class type invalid.");
/*     */     }
/* 859 */     this.version = ((CertificateVersion)paramObject);
/*     */   }
/*     */ 
/*     */   private void setSerialNumber(Object paramObject)
/*     */     throws CertificateException
/*     */   {
/* 869 */     if (!(paramObject instanceof CertificateSerialNumber)) {
/* 870 */       throw new CertificateException("SerialNumber class type invalid.");
/*     */     }
/* 872 */     this.serialNum = ((CertificateSerialNumber)paramObject);
/*     */   }
/*     */ 
/*     */   private void setAlgorithmId(Object paramObject)
/*     */     throws CertificateException
/*     */   {
/* 882 */     if (!(paramObject instanceof CertificateAlgorithmId)) {
/* 883 */       throw new CertificateException("AlgorithmId class type invalid.");
/*     */     }
/*     */ 
/* 886 */     this.algId = ((CertificateAlgorithmId)paramObject);
/*     */   }
/*     */ 
/*     */   private void setIssuer(Object paramObject)
/*     */     throws CertificateException
/*     */   {
/* 896 */     if (!(paramObject instanceof CertificateIssuerName)) {
/* 897 */       throw new CertificateException("Issuer class type invalid.");
/*     */     }
/*     */ 
/* 900 */     this.issuer = ((CertificateIssuerName)paramObject);
/*     */   }
/*     */ 
/*     */   private void setValidity(Object paramObject)
/*     */     throws CertificateException
/*     */   {
/* 910 */     if (!(paramObject instanceof CertificateValidity)) {
/* 911 */       throw new CertificateException("CertificateValidity class type invalid.");
/*     */     }
/*     */ 
/* 914 */     this.interval = ((CertificateValidity)paramObject);
/*     */   }
/*     */ 
/*     */   private void setSubject(Object paramObject)
/*     */     throws CertificateException
/*     */   {
/* 924 */     if (!(paramObject instanceof CertificateSubjectName)) {
/* 925 */       throw new CertificateException("Subject class type invalid.");
/*     */     }
/*     */ 
/* 928 */     this.subject = ((CertificateSubjectName)paramObject);
/*     */   }
/*     */ 
/*     */   private void setKey(Object paramObject)
/*     */     throws CertificateException
/*     */   {
/* 938 */     if (!(paramObject instanceof CertificateX509Key)) {
/* 939 */       throw new CertificateException("Key class type invalid.");
/*     */     }
/*     */ 
/* 942 */     this.pubKey = ((CertificateX509Key)paramObject);
/*     */   }
/*     */ 
/*     */   private void setIssuerUniqueId(Object paramObject)
/*     */     throws CertificateException
/*     */   {
/* 952 */     if (this.version.compare(1) < 0) {
/* 953 */       throw new CertificateException("Invalid version");
/*     */     }
/* 955 */     if (!(paramObject instanceof CertificateIssuerUniqueIdentity)) {
/* 956 */       throw new CertificateException("IssuerUniqueId class type invalid.");
/*     */     }
/*     */ 
/* 959 */     this.issuerUniqueId = ((CertificateIssuerUniqueIdentity)paramObject);
/*     */   }
/*     */ 
/*     */   private void setSubjectUniqueId(Object paramObject)
/*     */     throws CertificateException
/*     */   {
/* 969 */     if (this.version.compare(1) < 0) {
/* 970 */       throw new CertificateException("Invalid version");
/*     */     }
/* 972 */     if (!(paramObject instanceof CertificateSubjectUniqueIdentity)) {
/* 973 */       throw new CertificateException("SubjectUniqueId class type invalid.");
/*     */     }
/*     */ 
/* 976 */     this.subjectUniqueId = ((CertificateSubjectUniqueIdentity)paramObject);
/*     */   }
/*     */ 
/*     */   private void setExtensions(Object paramObject)
/*     */     throws CertificateException
/*     */   {
/* 986 */     if (this.version.compare(2) < 0) {
/* 987 */       throw new CertificateException("Invalid version");
/*     */     }
/* 989 */     if (!(paramObject instanceof CertificateExtensions)) {
/* 990 */       throw new CertificateException("Extensions class type invalid.");
/*     */     }
/*     */ 
/* 993 */     this.extensions = ((CertificateExtensions)paramObject);
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 116 */     map.put("version", Integer.valueOf(1));
/* 117 */     map.put("serialNumber", Integer.valueOf(2));
/* 118 */     map.put("algorithmID", Integer.valueOf(3));
/* 119 */     map.put("issuer", Integer.valueOf(4));
/* 120 */     map.put("validity", Integer.valueOf(5));
/* 121 */     map.put("subject", Integer.valueOf(6));
/* 122 */     map.put("key", Integer.valueOf(7));
/* 123 */     map.put("issuerID", Integer.valueOf(8));
/* 124 */     map.put("subjectID", Integer.valueOf(9));
/* 125 */     map.put("extensions", Integer.valueOf(10));
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.x509.X509CertInfo
 * JD-Core Version:    0.6.2
 */