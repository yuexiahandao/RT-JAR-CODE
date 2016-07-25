/*     */ package sun.security.x509;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.security.cert.CertificateException;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import javax.security.auth.x500.X500Principal;
/*     */ import sun.security.pkcs.PKCS9Attribute;
/*     */ import sun.security.util.DerInputStream;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.DerValue;
/*     */ import sun.security.util.ObjectIdentifier;
/*     */ 
/*     */ public class NameConstraintsExtension extends Extension
/*     */   implements CertAttrSet<String>, Cloneable
/*     */ {
/*     */   public static final String IDENT = "x509.info.extensions.NameConstraints";
/*     */   public static final String NAME = "NameConstraints";
/*     */   public static final String PERMITTED_SUBTREES = "permitted_subtrees";
/*     */   public static final String EXCLUDED_SUBTREES = "excluded_subtrees";
/*     */   private static final byte TAG_PERMITTED = 0;
/*     */   private static final byte TAG_EXCLUDED = 1;
/*  84 */   private GeneralSubtrees permitted = null;
/*  85 */   private GeneralSubtrees excluded = null;
/*     */   private boolean hasMin;
/*     */   private boolean hasMax;
/*  89 */   private boolean minMaxValid = false;
/*     */ 
/*     */   private void calcMinMax() throws IOException
/*     */   {
/*  93 */     this.hasMin = false;
/*  94 */     this.hasMax = false;
/*     */     int i;
/*     */     GeneralSubtree localGeneralSubtree;
/*  95 */     if (this.excluded != null) {
/*  96 */       for (i = 0; i < this.excluded.size(); i++) {
/*  97 */         localGeneralSubtree = this.excluded.get(i);
/*  98 */         if (localGeneralSubtree.getMinimum() != 0)
/*  99 */           this.hasMin = true;
/* 100 */         if (localGeneralSubtree.getMaximum() != -1) {
/* 101 */           this.hasMax = true;
/*     */         }
/*     */       }
/*     */     }
/* 105 */     if (this.permitted != null) {
/* 106 */       for (i = 0; i < this.permitted.size(); i++) {
/* 107 */         localGeneralSubtree = this.permitted.get(i);
/* 108 */         if (localGeneralSubtree.getMinimum() != 0)
/* 109 */           this.hasMin = true;
/* 110 */         if (localGeneralSubtree.getMaximum() != -1)
/* 111 */           this.hasMax = true;
/*     */       }
/*     */     }
/* 114 */     this.minMaxValid = true;
/*     */   }
/*     */ 
/*     */   private void encodeThis() throws IOException
/*     */   {
/* 119 */     this.minMaxValid = false;
/* 120 */     if ((this.permitted == null) && (this.excluded == null)) {
/* 121 */       this.extensionValue = null;
/* 122 */       return;
/*     */     }
/* 124 */     DerOutputStream localDerOutputStream1 = new DerOutputStream();
/*     */ 
/* 126 */     DerOutputStream localDerOutputStream2 = new DerOutputStream();
/*     */     DerOutputStream localDerOutputStream3;
/* 127 */     if (this.permitted != null) {
/* 128 */       localDerOutputStream3 = new DerOutputStream();
/* 129 */       this.permitted.encode(localDerOutputStream3);
/* 130 */       localDerOutputStream2.writeImplicit(DerValue.createTag((byte)-128, true, (byte)0), localDerOutputStream3);
/*     */     }
/*     */ 
/* 133 */     if (this.excluded != null) {
/* 134 */       localDerOutputStream3 = new DerOutputStream();
/* 135 */       this.excluded.encode(localDerOutputStream3);
/* 136 */       localDerOutputStream2.writeImplicit(DerValue.createTag((byte)-128, true, (byte)1), localDerOutputStream3);
/*     */     }
/*     */ 
/* 139 */     localDerOutputStream1.write((byte)48, localDerOutputStream2);
/* 140 */     this.extensionValue = localDerOutputStream1.toByteArray();
/*     */   }
/*     */ 
/*     */   public NameConstraintsExtension(GeneralSubtrees paramGeneralSubtrees1, GeneralSubtrees paramGeneralSubtrees2)
/*     */     throws IOException
/*     */   {
/* 154 */     this.permitted = paramGeneralSubtrees1;
/* 155 */     this.excluded = paramGeneralSubtrees2;
/*     */ 
/* 157 */     this.extensionId = PKIXExtensions.NameConstraints_Id;
/* 158 */     this.critical = true;
/* 159 */     encodeThis();
/*     */   }
/*     */ 
/*     */   public NameConstraintsExtension(Boolean paramBoolean, Object paramObject)
/*     */     throws IOException
/*     */   {
/* 172 */     this.extensionId = PKIXExtensions.NameConstraints_Id;
/* 173 */     this.critical = paramBoolean.booleanValue();
/*     */ 
/* 175 */     this.extensionValue = ((byte[])paramObject);
/* 176 */     DerValue localDerValue1 = new DerValue(this.extensionValue);
/* 177 */     if (localDerValue1.tag != 48) {
/* 178 */       throw new IOException("Invalid encoding for NameConstraintsExtension.");
/*     */     }
/*     */ 
/* 188 */     if (localDerValue1.data == null)
/* 189 */       return;
/* 190 */     while (localDerValue1.data.available() != 0) {
/* 191 */       DerValue localDerValue2 = localDerValue1.data.getDerValue();
/*     */ 
/* 193 */       if ((localDerValue2.isContextSpecific((byte)0)) && (localDerValue2.isConstructed())) {
/* 194 */         if (this.permitted != null) {
/* 195 */           throw new IOException("Duplicate permitted GeneralSubtrees in NameConstraintsExtension.");
/*     */         }
/*     */ 
/* 198 */         localDerValue2.resetTag((byte)48);
/* 199 */         this.permitted = new GeneralSubtrees(localDerValue2);
/*     */       }
/* 201 */       else if ((localDerValue2.isContextSpecific((byte)1)) && (localDerValue2.isConstructed()))
/*     */       {
/* 203 */         if (this.excluded != null) {
/* 204 */           throw new IOException("Duplicate excluded GeneralSubtrees in NameConstraintsExtension.");
/*     */         }
/*     */ 
/* 207 */         localDerValue2.resetTag((byte)48);
/* 208 */         this.excluded = new GeneralSubtrees(localDerValue2);
/*     */       } else {
/* 210 */         throw new IOException("Invalid encoding of NameConstraintsExtension.");
/*     */       }
/*     */     }
/* 213 */     this.minMaxValid = false;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 220 */     return super.toString() + "NameConstraints: [" + (this.permitted == null ? "" : new StringBuilder().append("\n    Permitted:").append(this.permitted.toString()).toString()) + (this.excluded == null ? "" : new StringBuilder().append("\n    Excluded:").append(this.excluded.toString()).toString()) + "   ]\n";
/*     */   }
/*     */ 
/*     */   public void encode(OutputStream paramOutputStream)
/*     */     throws IOException
/*     */   {
/* 235 */     DerOutputStream localDerOutputStream = new DerOutputStream();
/* 236 */     if (this.extensionValue == null) {
/* 237 */       this.extensionId = PKIXExtensions.NameConstraints_Id;
/* 238 */       this.critical = true;
/* 239 */       encodeThis();
/*     */     }
/* 241 */     super.encode(localDerOutputStream);
/* 242 */     paramOutputStream.write(localDerOutputStream.toByteArray());
/*     */   }
/*     */ 
/*     */   public void set(String paramString, Object paramObject)
/*     */     throws IOException
/*     */   {
/* 249 */     if (paramString.equalsIgnoreCase("permitted_subtrees")) {
/* 250 */       if (!(paramObject instanceof GeneralSubtrees)) {
/* 251 */         throw new IOException("Attribute value should be of type GeneralSubtrees.");
/*     */       }
/*     */ 
/* 254 */       this.permitted = ((GeneralSubtrees)paramObject);
/* 255 */     } else if (paramString.equalsIgnoreCase("excluded_subtrees")) {
/* 256 */       if (!(paramObject instanceof GeneralSubtrees)) {
/* 257 */         throw new IOException("Attribute value should be of type GeneralSubtrees.");
/*     */       }
/*     */ 
/* 260 */       this.excluded = ((GeneralSubtrees)paramObject);
/*     */     } else {
/* 262 */       throw new IOException("Attribute name not recognized by CertAttrSet:NameConstraintsExtension.");
/*     */     }
/*     */ 
/* 265 */     encodeThis();
/*     */   }
/*     */ 
/*     */   public Object get(String paramString)
/*     */     throws IOException
/*     */   {
/* 272 */     if (paramString.equalsIgnoreCase("permitted_subtrees"))
/* 273 */       return this.permitted;
/* 274 */     if (paramString.equalsIgnoreCase("excluded_subtrees")) {
/* 275 */       return this.excluded;
/*     */     }
/* 277 */     throw new IOException("Attribute name not recognized by CertAttrSet:NameConstraintsExtension.");
/*     */   }
/*     */ 
/*     */   public void delete(String paramString)
/*     */     throws IOException
/*     */   {
/* 286 */     if (paramString.equalsIgnoreCase("permitted_subtrees"))
/* 287 */       this.permitted = null;
/* 288 */     else if (paramString.equalsIgnoreCase("excluded_subtrees"))
/* 289 */       this.excluded = null;
/*     */     else {
/* 291 */       throw new IOException("Attribute name not recognized by CertAttrSet:NameConstraintsExtension.");
/*     */     }
/*     */ 
/* 294 */     encodeThis();
/*     */   }
/*     */ 
/*     */   public Enumeration<String> getElements()
/*     */   {
/* 302 */     AttributeNameEnumeration localAttributeNameEnumeration = new AttributeNameEnumeration();
/* 303 */     localAttributeNameEnumeration.addElement("permitted_subtrees");
/* 304 */     localAttributeNameEnumeration.addElement("excluded_subtrees");
/*     */ 
/* 306 */     return localAttributeNameEnumeration.elements();
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 313 */     return "NameConstraints";
/*     */   }
/*     */ 
/*     */   public void merge(NameConstraintsExtension paramNameConstraintsExtension)
/*     */     throws IOException
/*     */   {
/* 341 */     if (paramNameConstraintsExtension == null)
/*     */     {
/* 343 */       return;
/*     */     }
/*     */ 
/* 352 */     GeneralSubtrees localGeneralSubtrees1 = (GeneralSubtrees)paramNameConstraintsExtension.get("excluded_subtrees");
/*     */ 
/* 354 */     if (this.excluded == null) {
/* 355 */       this.excluded = (localGeneralSubtrees1 != null ? (GeneralSubtrees)localGeneralSubtrees1.clone() : null);
/*     */     }
/* 358 */     else if (localGeneralSubtrees1 != null)
/*     */     {
/* 360 */       this.excluded.union(localGeneralSubtrees1);
/*     */     }
/*     */ 
/* 370 */     GeneralSubtrees localGeneralSubtrees2 = (GeneralSubtrees)paramNameConstraintsExtension.get("permitted_subtrees");
/*     */ 
/* 372 */     if (this.permitted == null) {
/* 373 */       this.permitted = (localGeneralSubtrees2 != null ? (GeneralSubtrees)localGeneralSubtrees2.clone() : null);
/*     */     }
/* 376 */     else if (localGeneralSubtrees2 != null)
/*     */     {
/* 378 */       localGeneralSubtrees1 = this.permitted.intersect(localGeneralSubtrees2);
/*     */ 
/* 381 */       if (localGeneralSubtrees1 != null) {
/* 382 */         if (this.excluded != null)
/* 383 */           this.excluded.union(localGeneralSubtrees1);
/*     */         else {
/* 385 */           this.excluded = ((GeneralSubtrees)localGeneralSubtrees1.clone());
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 395 */     if (this.permitted != null) {
/* 396 */       this.permitted.reduce(this.excluded);
/*     */     }
/*     */ 
/* 401 */     encodeThis();
/*     */   }
/*     */ 
/*     */   public boolean verify(X509Certificate paramX509Certificate)
/*     */     throws IOException
/*     */   {
/* 419 */     if (paramX509Certificate == null) {
/* 420 */       throw new IOException("Certificate is null");
/*     */     }
/*     */ 
/* 424 */     if (!this.minMaxValid) {
/* 425 */       calcMinMax();
/*     */     }
/*     */ 
/* 428 */     if (this.hasMin) {
/* 429 */       throw new IOException("Non-zero minimum BaseDistance in name constraints not supported");
/*     */     }
/*     */ 
/* 433 */     if (this.hasMax) {
/* 434 */       throw new IOException("Maximum BaseDistance in name constraints not supported");
/*     */     }
/*     */ 
/* 438 */     X500Principal localX500Principal = paramX509Certificate.getSubjectX500Principal();
/* 439 */     X500Name localX500Name = X500Name.asX500Name(localX500Principal);
/*     */ 
/* 441 */     if ((!localX500Name.isEmpty()) && 
/* 442 */       (!verify(localX500Name))) {
/* 443 */       return false;
/*     */     }
/*     */ 
/* 447 */     GeneralNames localGeneralNames = null;
/*     */     Object localObject;
/*     */     try
/*     */     {
/* 452 */       X509CertImpl localX509CertImpl = X509CertImpl.toImpl(paramX509Certificate);
/* 453 */       localObject = localX509CertImpl.getSubjectAlternativeNameExtension();
/*     */ 
/* 455 */       if (localObject != null)
/*     */       {
/* 458 */         localGeneralNames = (GeneralNames)((SubjectAlternativeNameExtension)localObject).get("subject_name");
/*     */       }
/*     */     }
/*     */     catch (CertificateException localCertificateException) {
/* 462 */       throw new IOException("Unable to extract extensions from certificate: " + localCertificateException.getMessage());
/*     */     }
/*     */ 
/* 470 */     if (localGeneralNames == null) {
/* 471 */       return verifyRFC822SpecialCase(localX500Name);
/*     */     }
/*     */ 
/* 475 */     for (int i = 0; i < localGeneralNames.size(); i++) {
/* 476 */       localObject = localGeneralNames.get(i).getName();
/* 477 */       if (!verify((GeneralNameInterface)localObject)) {
/* 478 */         return false;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 483 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean verify(GeneralNameInterface paramGeneralNameInterface)
/*     */     throws IOException
/*     */   {
/* 496 */     if (paramGeneralNameInterface == null)
/* 497 */       throw new IOException("name is null");
/*     */     int i;
/*     */     Object localObject1;
/*     */     Object localObject2;
/* 501 */     if ((this.excluded != null) && (this.excluded.size() > 0))
/*     */     {
/* 503 */       for (i = 0; i < this.excluded.size(); i++) {
/* 504 */         GeneralSubtree localGeneralSubtree = this.excluded.get(i);
/* 505 */         if (localGeneralSubtree != null)
/*     */         {
/* 507 */           localObject1 = localGeneralSubtree.getName();
/* 508 */           if (localObject1 != null)
/*     */           {
/* 510 */             localObject2 = ((GeneralName)localObject1).getName();
/* 511 */             if (localObject2 != null)
/*     */             {
/* 516 */               switch (((GeneralNameInterface)localObject2).constrains(paramGeneralNameInterface)) {
/*     */               case -1:
/*     */               case 2:
/*     */               case 3:
/* 520 */                 break;
/*     */               case 0:
/*     */               case 1:
/* 523 */                 return false;
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 529 */     if ((this.permitted != null) && (this.permitted.size() > 0))
/*     */     {
/* 531 */       i = 0;
/*     */ 
/* 533 */       for (int j = 0; j < this.permitted.size(); j++) {
/* 534 */         localObject1 = this.permitted.get(j);
/* 535 */         if (localObject1 != null)
/*     */         {
/* 537 */           localObject2 = ((GeneralSubtree)localObject1).getName();
/* 538 */           if (localObject2 != null)
/*     */           {
/* 540 */             GeneralNameInterface localGeneralNameInterface = ((GeneralName)localObject2).getName();
/* 541 */             if (localGeneralNameInterface != null)
/*     */             {
/* 547 */               switch (localGeneralNameInterface.constrains(paramGeneralNameInterface)) {
/*     */               case -1:
/* 549 */                 break;
/*     */               case 2:
/*     */               case 3:
/* 552 */                 i = 1;
/* 553 */                 break;
/*     */               case 0:
/*     */               case 1:
/* 557 */                 return true;
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/* 560 */       if (i != 0) {
/* 561 */         return false;
/*     */       }
/*     */     }
/* 564 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean verifyRFC822SpecialCase(X500Name paramX500Name)
/*     */     throws IOException
/*     */   {
/* 578 */     for (Iterator localIterator = paramX500Name.allAvas().iterator(); localIterator.hasNext(); ) {
/* 579 */       AVA localAVA = (AVA)localIterator.next();
/* 580 */       ObjectIdentifier localObjectIdentifier = localAVA.getObjectIdentifier();
/* 581 */       if (localObjectIdentifier.equals(PKCS9Attribute.EMAIL_ADDRESS_OID)) {
/* 582 */         String str = localAVA.getValueString();
/* 583 */         if (str != null) {
/*     */           RFC822Name localRFC822Name;
/*     */           try {
/* 586 */             localRFC822Name = new RFC822Name(str); } catch (IOException localIOException) {
/*     */           }
/* 588 */           continue;
/*     */ 
/* 590 */           if (!verify(localRFC822Name)) {
/* 591 */             return false;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 596 */     return true;
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/*     */     try
/*     */     {
/* 604 */       NameConstraintsExtension localNameConstraintsExtension = (NameConstraintsExtension)super.clone();
/*     */ 
/* 607 */       if (this.permitted != null) {
/* 608 */         localNameConstraintsExtension.permitted = ((GeneralSubtrees)this.permitted.clone());
/*     */       }
/* 610 */       if (this.excluded != null) {
/* 611 */         localNameConstraintsExtension.excluded = ((GeneralSubtrees)this.excluded.clone());
/*     */       }
/* 613 */       return localNameConstraintsExtension; } catch (CloneNotSupportedException localCloneNotSupportedException) {
/*     */     }
/* 615 */     throw new RuntimeException("CloneNotSupportedException while cloning NameConstraintsException. This should never happen.");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.x509.NameConstraintsExtension
 * JD-Core Version:    0.6.2
 */