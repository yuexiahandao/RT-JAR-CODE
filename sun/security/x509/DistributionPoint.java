/*     */ package sun.security.x509;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Arrays;
/*     */ import sun.security.util.BitArray;
/*     */ import sun.security.util.DerInputStream;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.DerValue;
/*     */ 
/*     */ public class DistributionPoint
/*     */ {
/*     */   public static final int KEY_COMPROMISE = 1;
/*     */   public static final int CA_COMPROMISE = 2;
/*     */   public static final int AFFILIATION_CHANGED = 3;
/*     */   public static final int SUPERSEDED = 4;
/*     */   public static final int CESSATION_OF_OPERATION = 5;
/*     */   public static final int CERTIFICATE_HOLD = 6;
/*     */   public static final int PRIVILEGE_WITHDRAWN = 7;
/*     */   public static final int AA_COMPROMISE = 8;
/* 109 */   private static final String[] REASON_STRINGS = { null, "key compromise", "CA compromise", "affiliation changed", "superseded", "cessation of operation", "certificate hold", "privilege withdrawn", "AA compromise" };
/*     */   private static final byte TAG_DIST_PT = 0;
/*     */   private static final byte TAG_REASONS = 1;
/*     */   private static final byte TAG_ISSUER = 2;
/*     */   private static final byte TAG_FULL_NAME = 0;
/*     */   private static final byte TAG_REL_NAME = 1;
/*     */   private GeneralNames fullName;
/*     */   private RDN relativeName;
/*     */   private boolean[] reasonFlags;
/*     */   private GeneralNames crlIssuer;
/*     */   private volatile int hashCode;
/*     */ 
/*     */   public DistributionPoint(GeneralNames paramGeneralNames1, boolean[] paramArrayOfBoolean, GeneralNames paramGeneralNames2)
/*     */   {
/* 153 */     if ((paramGeneralNames1 == null) && (paramGeneralNames2 == null)) {
/* 154 */       throw new IllegalArgumentException("fullName and crlIssuer may not both be null");
/*     */     }
/*     */ 
/* 157 */     this.fullName = paramGeneralNames1;
/* 158 */     this.reasonFlags = paramArrayOfBoolean;
/* 159 */     this.crlIssuer = paramGeneralNames2;
/*     */   }
/*     */ 
/*     */   public DistributionPoint(RDN paramRDN, boolean[] paramArrayOfBoolean, GeneralNames paramGeneralNames)
/*     */   {
/* 175 */     if ((paramRDN == null) && (paramGeneralNames == null)) {
/* 176 */       throw new IllegalArgumentException("relativeName and crlIssuer may not both be null");
/*     */     }
/*     */ 
/* 179 */     this.relativeName = paramRDN;
/* 180 */     this.reasonFlags = paramArrayOfBoolean;
/* 181 */     this.crlIssuer = paramGeneralNames;
/*     */   }
/*     */ 
/*     */   public DistributionPoint(DerValue paramDerValue)
/*     */     throws IOException
/*     */   {
/* 191 */     if (paramDerValue.tag != 48) {
/* 192 */       throw new IOException("Invalid encoding of DistributionPoint.");
/*     */     }
/*     */ 
/* 198 */     while ((paramDerValue.data != null) && (paramDerValue.data.available() != 0)) {
/* 199 */       DerValue localDerValue1 = paramDerValue.data.getDerValue();
/*     */ 
/* 201 */       if ((localDerValue1.isContextSpecific((byte)0)) && (localDerValue1.isConstructed())) {
/* 202 */         if ((this.fullName != null) || (this.relativeName != null)) {
/* 203 */           throw new IOException("Duplicate DistributionPointName in DistributionPoint.");
/*     */         }
/*     */ 
/* 206 */         DerValue localDerValue2 = localDerValue1.data.getDerValue();
/* 207 */         if ((localDerValue2.isContextSpecific((byte)0)) && (localDerValue2.isConstructed()))
/*     */         {
/* 209 */           localDerValue2.resetTag((byte)48);
/* 210 */           this.fullName = new GeneralNames(localDerValue2);
/* 211 */         } else if ((localDerValue2.isContextSpecific((byte)1)) && (localDerValue2.isConstructed()))
/*     */         {
/* 213 */           localDerValue2.resetTag((byte)49);
/* 214 */           this.relativeName = new RDN(localDerValue2);
/*     */         } else {
/* 216 */           throw new IOException("Invalid DistributionPointName in DistributionPoint");
/*     */         }
/*     */       }
/* 219 */       else if ((localDerValue1.isContextSpecific((byte)1)) && (!localDerValue1.isConstructed()))
/*     */       {
/* 221 */         if (this.reasonFlags != null) {
/* 222 */           throw new IOException("Duplicate Reasons in DistributionPoint.");
/*     */         }
/*     */ 
/* 225 */         localDerValue1.resetTag((byte)3);
/* 226 */         this.reasonFlags = localDerValue1.getUnalignedBitString().toBooleanArray();
/* 227 */       } else if ((localDerValue1.isContextSpecific((byte)2)) && (localDerValue1.isConstructed()))
/*     */       {
/* 229 */         if (this.crlIssuer != null) {
/* 230 */           throw new IOException("Duplicate CRLIssuer in DistributionPoint.");
/*     */         }
/*     */ 
/* 233 */         localDerValue1.resetTag((byte)48);
/* 234 */         this.crlIssuer = new GeneralNames(localDerValue1);
/*     */       } else {
/* 236 */         throw new IOException("Invalid encoding of DistributionPoint.");
/*     */       }
/*     */     }
/*     */ 
/* 240 */     if ((this.crlIssuer == null) && (this.fullName == null) && (this.relativeName == null))
/* 241 */       throw new IOException("One of fullName, relativeName,  and crlIssuer has to be set");
/*     */   }
/*     */ 
/*     */   public GeneralNames getFullName()
/*     */   {
/* 250 */     return this.fullName;
/*     */   }
/*     */ 
/*     */   public RDN getRelativeName()
/*     */   {
/* 257 */     return this.relativeName;
/*     */   }
/*     */ 
/*     */   public boolean[] getReasonFlags()
/*     */   {
/* 264 */     return this.reasonFlags;
/*     */   }
/*     */ 
/*     */   public GeneralNames getCRLIssuer()
/*     */   {
/* 271 */     return this.crlIssuer;
/*     */   }
/*     */ 
/*     */   public void encode(DerOutputStream paramDerOutputStream)
/*     */     throws IOException
/*     */   {
/* 281 */     DerOutputStream localDerOutputStream1 = new DerOutputStream();
/*     */     DerOutputStream localDerOutputStream2;
/*     */     Object localObject;
/* 284 */     if ((this.fullName != null) || (this.relativeName != null)) {
/* 285 */       localDerOutputStream2 = new DerOutputStream();
/* 286 */       if (this.fullName != null) {
/* 287 */         localObject = new DerOutputStream();
/* 288 */         this.fullName.encode((DerOutputStream)localObject);
/* 289 */         localDerOutputStream2.writeImplicit(DerValue.createTag((byte)-128, true, (byte)0), (DerOutputStream)localObject);
/*     */       }
/* 292 */       else if (this.relativeName != null) {
/* 293 */         localObject = new DerOutputStream();
/* 294 */         this.relativeName.encode((DerOutputStream)localObject);
/* 295 */         localDerOutputStream2.writeImplicit(DerValue.createTag((byte)-128, true, (byte)1), (DerOutputStream)localObject);
/*     */       }
/*     */ 
/* 299 */       localDerOutputStream1.write(DerValue.createTag((byte)-128, true, (byte)0), localDerOutputStream2);
/*     */     }
/*     */ 
/* 303 */     if (this.reasonFlags != null) {
/* 304 */       localDerOutputStream2 = new DerOutputStream();
/* 305 */       localObject = new BitArray(this.reasonFlags);
/* 306 */       localDerOutputStream2.putTruncatedUnalignedBitString((BitArray)localObject);
/* 307 */       localDerOutputStream1.writeImplicit(DerValue.createTag((byte)-128, false, (byte)1), localDerOutputStream2);
/*     */     }
/*     */ 
/* 311 */     if (this.crlIssuer != null) {
/* 312 */       localDerOutputStream2 = new DerOutputStream();
/* 313 */       this.crlIssuer.encode(localDerOutputStream2);
/* 314 */       localDerOutputStream1.writeImplicit(DerValue.createTag((byte)-128, true, (byte)2), localDerOutputStream2);
/*     */     }
/*     */ 
/* 318 */     paramDerOutputStream.write((byte)48, localDerOutputStream1);
/*     */   }
/*     */ 
/*     */   private static boolean equals(Object paramObject1, Object paramObject2)
/*     */   {
/* 325 */     return paramObject1 == null ? false : paramObject2 == null ? true : paramObject1.equals(paramObject2);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 335 */     if (this == paramObject) {
/* 336 */       return true;
/*     */     }
/* 338 */     if (!(paramObject instanceof DistributionPoint)) {
/* 339 */       return false;
/*     */     }
/* 341 */     DistributionPoint localDistributionPoint = (DistributionPoint)paramObject;
/*     */ 
/* 343 */     boolean bool = (equals(this.fullName, localDistributionPoint.fullName)) && (equals(this.relativeName, localDistributionPoint.relativeName)) && (equals(this.crlIssuer, localDistributionPoint.crlIssuer)) && (Arrays.equals(this.reasonFlags, localDistributionPoint.reasonFlags));
/*     */ 
/* 347 */     return bool;
/*     */   }
/*     */ 
/*     */   public int hashCode() {
/* 351 */     int i = this.hashCode;
/* 352 */     if (i == 0) {
/* 353 */       i = 1;
/* 354 */       if (this.fullName != null) {
/* 355 */         i += this.fullName.hashCode();
/*     */       }
/* 357 */       if (this.relativeName != null) {
/* 358 */         i += this.relativeName.hashCode();
/*     */       }
/* 360 */       if (this.crlIssuer != null) {
/* 361 */         i += this.crlIssuer.hashCode();
/*     */       }
/* 363 */       if (this.reasonFlags != null) {
/* 364 */         for (int j = 0; j < this.reasonFlags.length; j++) {
/* 365 */           if (this.reasonFlags[j] != 0) {
/* 366 */             i += j;
/*     */           }
/*     */         }
/*     */       }
/* 370 */       this.hashCode = i;
/*     */     }
/* 372 */     return i;
/*     */   }
/*     */ 
/*     */   private static String reasonToString(int paramInt)
/*     */   {
/* 379 */     if ((paramInt > 0) && (paramInt < REASON_STRINGS.length)) {
/* 380 */       return REASON_STRINGS[paramInt];
/*     */     }
/* 382 */     return "Unknown reason " + paramInt;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 389 */     StringBuilder localStringBuilder = new StringBuilder();
/* 390 */     if (this.fullName != null) {
/* 391 */       localStringBuilder.append("DistributionPoint:\n     " + this.fullName + "\n");
/*     */     }
/* 393 */     if (this.relativeName != null) {
/* 394 */       localStringBuilder.append("DistributionPoint:\n     " + this.relativeName + "\n");
/*     */     }
/*     */ 
/* 397 */     if (this.reasonFlags != null) {
/* 398 */       localStringBuilder.append("   ReasonFlags:\n");
/* 399 */       for (int i = 0; i < this.reasonFlags.length; i++) {
/* 400 */         if (this.reasonFlags[i] != 0) {
/* 401 */           localStringBuilder.append("    " + reasonToString(i) + "\n");
/*     */         }
/*     */       }
/*     */     }
/* 405 */     if (this.crlIssuer != null) {
/* 406 */       localStringBuilder.append("   CRLIssuer:" + this.crlIssuer + "\n");
/*     */     }
/* 408 */     return localStringBuilder.toString();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.x509.DistributionPoint
 * JD-Core Version:    0.6.2
 */