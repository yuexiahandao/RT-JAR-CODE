/*     */ package sun.security.x509;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.util.Enumeration;
/*     */ import sun.security.util.DerInputStream;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.DerValue;
/*     */ 
/*     */ public class IssuingDistributionPointExtension extends Extension
/*     */   implements CertAttrSet<String>
/*     */ {
/*     */   public static final String IDENT = "x509.info.extensions.IssuingDistributionPoint";
/*     */   public static final String NAME = "IssuingDistributionPoint";
/*     */   public static final String POINT = "point";
/*     */   public static final String REASONS = "reasons";
/*     */   public static final String ONLY_USER_CERTS = "only_user_certs";
/*     */   public static final String ONLY_CA_CERTS = "only_ca_certs";
/*     */   public static final String ONLY_ATTRIBUTE_CERTS = "only_attribute_certs";
/*     */   public static final String INDIRECT_CRL = "indirect_crl";
/*  93 */   private DistributionPointName distributionPoint = null;
/*     */ 
/*  98 */   private ReasonFlags revocationReasons = null;
/*  99 */   private boolean hasOnlyUserCerts = false;
/* 100 */   private boolean hasOnlyCACerts = false;
/* 101 */   private boolean hasOnlyAttributeCerts = false;
/* 102 */   private boolean isIndirectCRL = false;
/*     */   private static final byte TAG_DISTRIBUTION_POINT = 0;
/*     */   private static final byte TAG_ONLY_USER_CERTS = 1;
/*     */   private static final byte TAG_ONLY_CA_CERTS = 2;
/*     */   private static final byte TAG_ONLY_SOME_REASONS = 3;
/*     */   private static final byte TAG_INDIRECT_CRL = 4;
/*     */   private static final byte TAG_ONLY_ATTRIBUTE_CERTS = 5;
/*     */ 
/*     */   public IssuingDistributionPointExtension(DistributionPointName paramDistributionPointName, ReasonFlags paramReasonFlags, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4)
/*     */     throws IOException
/*     */   {
/* 142 */     if (((paramBoolean1) && ((paramBoolean2) || (paramBoolean3))) || ((paramBoolean2) && ((paramBoolean1) || (paramBoolean3))) || ((paramBoolean3) && ((paramBoolean1) || (paramBoolean2))))
/*     */     {
/* 145 */       throw new IllegalArgumentException("Only one of hasOnlyUserCerts, hasOnlyCACerts, hasOnlyAttributeCerts may be set to true");
/*     */     }
/*     */ 
/* 149 */     this.extensionId = PKIXExtensions.IssuingDistributionPoint_Id;
/* 150 */     this.critical = true;
/* 151 */     this.distributionPoint = paramDistributionPointName;
/* 152 */     this.revocationReasons = paramReasonFlags;
/* 153 */     this.hasOnlyUserCerts = paramBoolean1;
/* 154 */     this.hasOnlyCACerts = paramBoolean2;
/* 155 */     this.hasOnlyAttributeCerts = paramBoolean3;
/* 156 */     this.isIndirectCRL = paramBoolean4;
/* 157 */     encodeThis();
/*     */   }
/*     */ 
/*     */   public IssuingDistributionPointExtension(Boolean paramBoolean, Object paramObject)
/*     */     throws IOException
/*     */   {
/* 170 */     this.extensionId = PKIXExtensions.IssuingDistributionPoint_Id;
/* 171 */     this.critical = paramBoolean.booleanValue();
/*     */ 
/* 173 */     if (!(paramObject instanceof byte[])) {
/* 174 */       throw new IOException("Illegal argument type");
/*     */     }
/*     */ 
/* 177 */     this.extensionValue = ((byte[])paramObject);
/* 178 */     DerValue localDerValue1 = new DerValue(this.extensionValue);
/* 179 */     if (localDerValue1.tag != 48) {
/* 180 */       throw new IOException("Invalid encoding for IssuingDistributionPointExtension.");
/*     */     }
/*     */ 
/* 185 */     if ((localDerValue1.data == null) || (localDerValue1.data.available() == 0)) {
/* 186 */       return;
/*     */     }
/*     */ 
/* 189 */     DerInputStream localDerInputStream = localDerValue1.data;
/* 190 */     while ((localDerInputStream != null) && (localDerInputStream.available() != 0)) {
/* 191 */       DerValue localDerValue2 = localDerInputStream.getDerValue();
/*     */ 
/* 193 */       if ((localDerValue2.isContextSpecific((byte)0)) && (localDerValue2.isConstructed()))
/*     */       {
/* 195 */         this.distributionPoint = new DistributionPointName(localDerValue2.data.getDerValue());
/*     */       }
/* 197 */       else if ((localDerValue2.isContextSpecific((byte)1)) && (!localDerValue2.isConstructed()))
/*     */       {
/* 199 */         localDerValue2.resetTag((byte)1);
/* 200 */         this.hasOnlyUserCerts = localDerValue2.getBoolean();
/* 201 */       } else if ((localDerValue2.isContextSpecific((byte)2)) && (!localDerValue2.isConstructed()))
/*     */       {
/* 203 */         localDerValue2.resetTag((byte)1);
/* 204 */         this.hasOnlyCACerts = localDerValue2.getBoolean();
/* 205 */       } else if ((localDerValue2.isContextSpecific((byte)3)) && (!localDerValue2.isConstructed()))
/*     */       {
/* 207 */         this.revocationReasons = new ReasonFlags(localDerValue2);
/* 208 */       } else if ((localDerValue2.isContextSpecific((byte)4)) && (!localDerValue2.isConstructed()))
/*     */       {
/* 210 */         localDerValue2.resetTag((byte)1);
/* 211 */         this.isIndirectCRL = localDerValue2.getBoolean();
/* 212 */       } else if ((localDerValue2.isContextSpecific((byte)5)) && (!localDerValue2.isConstructed()))
/*     */       {
/* 214 */         localDerValue2.resetTag((byte)1);
/* 215 */         this.hasOnlyAttributeCerts = localDerValue2.getBoolean();
/*     */       } else {
/* 217 */         throw new IOException("Invalid encoding of IssuingDistributionPoint");
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 227 */     return "IssuingDistributionPoint";
/*     */   }
/*     */ 
/*     */   public void encode(OutputStream paramOutputStream)
/*     */     throws IOException
/*     */   {
/* 238 */     DerOutputStream localDerOutputStream = new DerOutputStream();
/* 239 */     if (this.extensionValue == null) {
/* 240 */       this.extensionId = PKIXExtensions.IssuingDistributionPoint_Id;
/* 241 */       this.critical = false;
/* 242 */       encodeThis();
/*     */     }
/* 244 */     super.encode(localDerOutputStream);
/* 245 */     paramOutputStream.write(localDerOutputStream.toByteArray());
/*     */   }
/*     */ 
/*     */   public void set(String paramString, Object paramObject)
/*     */     throws IOException
/*     */   {
/* 252 */     if (paramString.equalsIgnoreCase("point")) {
/* 253 */       if (!(paramObject instanceof DistributionPointName)) {
/* 254 */         throw new IOException("Attribute value should be of type DistributionPointName.");
/*     */       }
/*     */ 
/* 257 */       this.distributionPoint = ((DistributionPointName)paramObject);
/*     */     }
/* 259 */     else if (paramString.equalsIgnoreCase("reasons")) {
/* 260 */       if (!(paramObject instanceof ReasonFlags)) {
/* 261 */         throw new IOException("Attribute value should be of type ReasonFlags.");
/*     */       }
/*     */ 
/*     */     }
/* 265 */     else if (paramString.equalsIgnoreCase("indirect_crl")) {
/* 266 */       if (!(paramObject instanceof Boolean)) {
/* 267 */         throw new IOException("Attribute value should be of type Boolean.");
/*     */       }
/*     */ 
/* 270 */       this.isIndirectCRL = ((Boolean)paramObject).booleanValue();
/*     */     }
/* 272 */     else if (paramString.equalsIgnoreCase("only_user_certs")) {
/* 273 */       if (!(paramObject instanceof Boolean)) {
/* 274 */         throw new IOException("Attribute value should be of type Boolean.");
/*     */       }
/*     */ 
/* 277 */       this.hasOnlyUserCerts = ((Boolean)paramObject).booleanValue();
/*     */     }
/* 279 */     else if (paramString.equalsIgnoreCase("only_ca_certs")) {
/* 280 */       if (!(paramObject instanceof Boolean)) {
/* 281 */         throw new IOException("Attribute value should be of type Boolean.");
/*     */       }
/*     */ 
/* 284 */       this.hasOnlyCACerts = ((Boolean)paramObject).booleanValue();
/*     */     }
/* 286 */     else if (paramString.equalsIgnoreCase("only_attribute_certs")) {
/* 287 */       if (!(paramObject instanceof Boolean)) {
/* 288 */         throw new IOException("Attribute value should be of type Boolean.");
/*     */       }
/*     */ 
/* 291 */       this.hasOnlyAttributeCerts = ((Boolean)paramObject).booleanValue();
/*     */     }
/*     */     else
/*     */     {
/* 295 */       throw new IOException("Attribute name [" + paramString + "] not recognized by " + "CertAttrSet:IssuingDistributionPointExtension.");
/*     */     }
/*     */ 
/* 299 */     encodeThis();
/*     */   }
/*     */ 
/*     */   public Object get(String paramString)
/*     */     throws IOException
/*     */   {
/* 306 */     if (paramString.equalsIgnoreCase("point")) {
/* 307 */       return this.distributionPoint;
/*     */     }
/* 309 */     if (paramString.equalsIgnoreCase("indirect_crl")) {
/* 310 */       return Boolean.valueOf(this.isIndirectCRL);
/*     */     }
/* 312 */     if (paramString.equalsIgnoreCase("reasons")) {
/* 313 */       return this.revocationReasons;
/*     */     }
/* 315 */     if (paramString.equalsIgnoreCase("only_user_certs")) {
/* 316 */       return Boolean.valueOf(this.hasOnlyUserCerts);
/*     */     }
/* 318 */     if (paramString.equalsIgnoreCase("only_ca_certs")) {
/* 319 */       return Boolean.valueOf(this.hasOnlyCACerts);
/*     */     }
/* 321 */     if (paramString.equalsIgnoreCase("only_attribute_certs")) {
/* 322 */       return Boolean.valueOf(this.hasOnlyAttributeCerts);
/*     */     }
/*     */ 
/* 325 */     throw new IOException("Attribute name [" + paramString + "] not recognized by " + "CertAttrSet:IssuingDistributionPointExtension.");
/*     */   }
/*     */ 
/*     */   public void delete(String paramString)
/*     */     throws IOException
/*     */   {
/* 335 */     if (paramString.equalsIgnoreCase("point")) {
/* 336 */       this.distributionPoint = null;
/*     */     }
/* 338 */     else if (paramString.equalsIgnoreCase("indirect_crl")) {
/* 339 */       this.isIndirectCRL = false;
/*     */     }
/* 341 */     else if (paramString.equalsIgnoreCase("reasons")) {
/* 342 */       this.revocationReasons = null;
/*     */     }
/* 344 */     else if (paramString.equalsIgnoreCase("only_user_certs")) {
/* 345 */       this.hasOnlyUserCerts = false;
/*     */     }
/* 347 */     else if (paramString.equalsIgnoreCase("only_ca_certs")) {
/* 348 */       this.hasOnlyCACerts = false;
/*     */     }
/* 350 */     else if (paramString.equalsIgnoreCase("only_attribute_certs")) {
/* 351 */       this.hasOnlyAttributeCerts = false;
/*     */     }
/*     */     else {
/* 354 */       throw new IOException("Attribute name [" + paramString + "] not recognized by " + "CertAttrSet:IssuingDistributionPointExtension.");
/*     */     }
/*     */ 
/* 358 */     encodeThis();
/*     */   }
/*     */ 
/*     */   public Enumeration<String> getElements()
/*     */   {
/* 366 */     AttributeNameEnumeration localAttributeNameEnumeration = new AttributeNameEnumeration();
/* 367 */     localAttributeNameEnumeration.addElement("point");
/* 368 */     localAttributeNameEnumeration.addElement("reasons");
/* 369 */     localAttributeNameEnumeration.addElement("only_user_certs");
/* 370 */     localAttributeNameEnumeration.addElement("only_ca_certs");
/* 371 */     localAttributeNameEnumeration.addElement("only_attribute_certs");
/* 372 */     localAttributeNameEnumeration.addElement("indirect_crl");
/* 373 */     return localAttributeNameEnumeration.elements();
/*     */   }
/*     */ 
/*     */   private void encodeThis()
/*     */     throws IOException
/*     */   {
/* 379 */     if ((this.distributionPoint == null) && (this.revocationReasons == null) && (!this.hasOnlyUserCerts) && (!this.hasOnlyCACerts) && (!this.hasOnlyAttributeCerts) && (!this.isIndirectCRL))
/*     */     {
/* 386 */       this.extensionValue = null;
/* 387 */       return;
/*     */     }
/*     */ 
/* 391 */     DerOutputStream localDerOutputStream1 = new DerOutputStream();
/*     */ 
/* 393 */     if (this.distributionPoint != null) {
/* 394 */       localDerOutputStream2 = new DerOutputStream();
/* 395 */       this.distributionPoint.encode(localDerOutputStream2);
/* 396 */       localDerOutputStream1.writeImplicit(DerValue.createTag((byte)-128, true, (byte)0), localDerOutputStream2);
/*     */     }
/*     */ 
/* 400 */     if (this.hasOnlyUserCerts) {
/* 401 */       localDerOutputStream2 = new DerOutputStream();
/* 402 */       localDerOutputStream2.putBoolean(this.hasOnlyUserCerts);
/* 403 */       localDerOutputStream1.writeImplicit(DerValue.createTag((byte)-128, false, (byte)1), localDerOutputStream2);
/*     */     }
/*     */ 
/* 407 */     if (this.hasOnlyCACerts) {
/* 408 */       localDerOutputStream2 = new DerOutputStream();
/* 409 */       localDerOutputStream2.putBoolean(this.hasOnlyCACerts);
/* 410 */       localDerOutputStream1.writeImplicit(DerValue.createTag((byte)-128, false, (byte)2), localDerOutputStream2);
/*     */     }
/*     */ 
/* 414 */     if (this.revocationReasons != null) {
/* 415 */       localDerOutputStream2 = new DerOutputStream();
/* 416 */       this.revocationReasons.encode(localDerOutputStream2);
/* 417 */       localDerOutputStream1.writeImplicit(DerValue.createTag((byte)-128, false, (byte)3), localDerOutputStream2);
/*     */     }
/*     */ 
/* 421 */     if (this.isIndirectCRL) {
/* 422 */       localDerOutputStream2 = new DerOutputStream();
/* 423 */       localDerOutputStream2.putBoolean(this.isIndirectCRL);
/* 424 */       localDerOutputStream1.writeImplicit(DerValue.createTag((byte)-128, false, (byte)4), localDerOutputStream2);
/*     */     }
/*     */ 
/* 428 */     if (this.hasOnlyAttributeCerts) {
/* 429 */       localDerOutputStream2 = new DerOutputStream();
/* 430 */       localDerOutputStream2.putBoolean(this.hasOnlyAttributeCerts);
/* 431 */       localDerOutputStream1.writeImplicit(DerValue.createTag((byte)-128, false, (byte)5), localDerOutputStream2);
/*     */     }
/*     */ 
/* 435 */     DerOutputStream localDerOutputStream2 = new DerOutputStream();
/* 436 */     localDerOutputStream2.write((byte)48, localDerOutputStream1);
/* 437 */     this.extensionValue = localDerOutputStream2.toByteArray();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 445 */     StringBuilder localStringBuilder = new StringBuilder(super.toString());
/* 446 */     localStringBuilder.append("IssuingDistributionPoint [\n  ");
/*     */ 
/* 448 */     if (this.distributionPoint != null) {
/* 449 */       localStringBuilder.append(this.distributionPoint);
/*     */     }
/*     */ 
/* 452 */     if (this.revocationReasons != null) {
/* 453 */       localStringBuilder.append(this.revocationReasons);
/*     */     }
/*     */ 
/* 456 */     localStringBuilder.append(this.hasOnlyUserCerts ? "  Only contains user certs: true" : "  Only contains user certs: false").append("\n");
/*     */ 
/* 460 */     localStringBuilder.append(this.hasOnlyCACerts ? "  Only contains CA certs: true" : "  Only contains CA certs: false").append("\n");
/*     */ 
/* 464 */     localStringBuilder.append(this.hasOnlyAttributeCerts ? "  Only contains attribute certs: true" : "  Only contains attribute certs: false").append("\n");
/*     */ 
/* 468 */     localStringBuilder.append(this.isIndirectCRL ? "  Indirect CRL: true" : "  Indirect CRL: false").append("\n");
/*     */ 
/* 472 */     localStringBuilder.append("]\n");
/*     */ 
/* 474 */     return localStringBuilder.toString();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.x509.IssuingDistributionPointExtension
 * JD-Core Version:    0.6.2
 */