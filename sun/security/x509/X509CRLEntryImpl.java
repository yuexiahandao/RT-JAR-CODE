/*     */ package sun.security.x509;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.math.BigInteger;
/*     */ import java.security.cert.CRLException;
/*     */ import java.security.cert.CRLReason;
/*     */ import java.security.cert.X509CRLEntry;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Date;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.TreeMap;
/*     */ import java.util.TreeSet;
/*     */ import javax.security.auth.x500.X500Principal;
/*     */ import sun.misc.HexDumpEncoder;
/*     */ import sun.security.util.DerInputStream;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.DerValue;
/*     */ import sun.security.util.ObjectIdentifier;
/*     */ 
/*     */ public class X509CRLEntryImpl extends X509CRLEntry
/*     */   implements Comparable<X509CRLEntryImpl>
/*     */ {
/*  75 */   private SerialNumber serialNumber = null;
/*  76 */   private Date revocationDate = null;
/*  77 */   private CRLExtensions extensions = null;
/*  78 */   private byte[] revokedCert = null;
/*     */   private X500Principal certIssuer;
/*     */   private static final boolean isExplicit = false;
/*     */   private static final long YR_2050 = 2524636800000L;
/*     */ 
/*     */   public X509CRLEntryImpl(BigInteger paramBigInteger, Date paramDate)
/*     */   {
/*  92 */     this.serialNumber = new SerialNumber(paramBigInteger);
/*  93 */     this.revocationDate = paramDate;
/*     */   }
/*     */ 
/*     */   public X509CRLEntryImpl(BigInteger paramBigInteger, Date paramDate, CRLExtensions paramCRLExtensions)
/*     */   {
/* 107 */     this.serialNumber = new SerialNumber(paramBigInteger);
/* 108 */     this.revocationDate = paramDate;
/* 109 */     this.extensions = paramCRLExtensions;
/*     */   }
/*     */ 
/*     */   public X509CRLEntryImpl(byte[] paramArrayOfByte)
/*     */     throws CRLException
/*     */   {
/*     */     try
/*     */     {
/* 120 */       parse(new DerValue(paramArrayOfByte));
/*     */     } catch (IOException localIOException) {
/* 122 */       this.revokedCert = null;
/* 123 */       throw new CRLException("Parsing error: " + localIOException.toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   public X509CRLEntryImpl(DerValue paramDerValue)
/*     */     throws CRLException
/*     */   {
/*     */     try
/*     */     {
/* 135 */       parse(paramDerValue);
/*     */     } catch (IOException localIOException) {
/* 137 */       this.revokedCert = null;
/* 138 */       throw new CRLException("Parsing error: " + localIOException.toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean hasExtensions()
/*     */   {
/* 150 */     return this.extensions != null;
/*     */   }
/*     */ 
/*     */   public void encode(DerOutputStream paramDerOutputStream)
/*     */     throws CRLException
/*     */   {
/*     */     try
/*     */     {
/* 162 */       if (this.revokedCert == null) {
/* 163 */         DerOutputStream localDerOutputStream1 = new DerOutputStream();
/*     */ 
/* 165 */         this.serialNumber.encode(localDerOutputStream1);
/*     */ 
/* 167 */         if (this.revocationDate.getTime() < 2524636800000L)
/* 168 */           localDerOutputStream1.putUTCTime(this.revocationDate);
/*     */         else {
/* 170 */           localDerOutputStream1.putGeneralizedTime(this.revocationDate);
/*     */         }
/*     */ 
/* 173 */         if (this.extensions != null) {
/* 174 */           this.extensions.encode(localDerOutputStream1, false);
/*     */         }
/* 176 */         DerOutputStream localDerOutputStream2 = new DerOutputStream();
/* 177 */         localDerOutputStream2.write((byte)48, localDerOutputStream1);
/*     */ 
/* 179 */         this.revokedCert = localDerOutputStream2.toByteArray();
/*     */       }
/* 181 */       paramDerOutputStream.write(this.revokedCert);
/*     */     } catch (IOException localIOException) {
/* 183 */       throw new CRLException("Encoding error: " + localIOException.toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   public byte[] getEncoded()
/*     */     throws CRLException
/*     */   {
/* 194 */     return (byte[])getEncoded0().clone();
/*     */   }
/*     */ 
/*     */   private byte[] getEncoded0() throws CRLException
/*     */   {
/* 199 */     if (this.revokedCert == null)
/* 200 */       encode(new DerOutputStream());
/* 201 */     return this.revokedCert;
/*     */   }
/*     */ 
/*     */   public X500Principal getCertificateIssuer()
/*     */   {
/* 206 */     return this.certIssuer;
/*     */   }
/*     */ 
/*     */   void setCertificateIssuer(X500Principal paramX500Principal1, X500Principal paramX500Principal2) {
/* 210 */     if (paramX500Principal1.equals(paramX500Principal2))
/* 211 */       this.certIssuer = null;
/*     */     else
/* 213 */       this.certIssuer = paramX500Principal2;
/*     */   }
/*     */ 
/*     */   public BigInteger getSerialNumber()
/*     */   {
/* 224 */     return this.serialNumber.getNumber();
/*     */   }
/*     */ 
/*     */   public Date getRevocationDate()
/*     */   {
/* 234 */     return new Date(this.revocationDate.getTime());
/*     */   }
/*     */ 
/*     */   public CRLReason getRevocationReason()
/*     */   {
/* 244 */     Extension localExtension = getExtension(PKIXExtensions.ReasonCode_Id);
/* 245 */     if (localExtension == null) {
/* 246 */       return null;
/*     */     }
/* 248 */     CRLReasonCodeExtension localCRLReasonCodeExtension = (CRLReasonCodeExtension)localExtension;
/* 249 */     return localCRLReasonCodeExtension.getReasonCode();
/*     */   }
/*     */ 
/*     */   public static CRLReason getRevocationReason(X509CRLEntry paramX509CRLEntry)
/*     */   {
/*     */     try
/*     */     {
/* 258 */       byte[] arrayOfByte1 = paramX509CRLEntry.getExtensionValue("2.5.29.21");
/* 259 */       if (arrayOfByte1 == null) {
/* 260 */         return null;
/*     */       }
/* 262 */       DerValue localDerValue = new DerValue(arrayOfByte1);
/* 263 */       byte[] arrayOfByte2 = localDerValue.getOctetString();
/*     */ 
/* 265 */       CRLReasonCodeExtension localCRLReasonCodeExtension = new CRLReasonCodeExtension(Boolean.FALSE, arrayOfByte2);
/*     */ 
/* 267 */       return localCRLReasonCodeExtension.getReasonCode(); } catch (IOException localIOException) {
/*     */     }
/* 269 */     return null;
/*     */   }
/*     */ 
/*     */   public Integer getReasonCode()
/*     */     throws IOException
/*     */   {
/* 280 */     Extension localExtension = getExtension(PKIXExtensions.ReasonCode_Id);
/* 281 */     if (localExtension == null)
/* 282 */       return null;
/* 283 */     CRLReasonCodeExtension localCRLReasonCodeExtension = (CRLReasonCodeExtension)localExtension;
/* 284 */     return (Integer)localCRLReasonCodeExtension.get("reason");
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 294 */     StringBuilder localStringBuilder = new StringBuilder();
/*     */ 
/* 296 */     localStringBuilder.append(this.serialNumber.toString());
/* 297 */     localStringBuilder.append("  On: " + this.revocationDate.toString());
/* 298 */     if (this.certIssuer != null) {
/* 299 */       localStringBuilder.append("\n    Certificate issuer: " + this.certIssuer);
/*     */     }
/* 301 */     if (this.extensions != null) {
/* 302 */       Collection localCollection = this.extensions.getAllExtensions();
/* 303 */       Object[] arrayOfObject = localCollection.toArray();
/*     */ 
/* 305 */       localStringBuilder.append("\n    CRL Entry Extensions: " + arrayOfObject.length);
/* 306 */       for (int i = 0; i < arrayOfObject.length; i++) {
/* 307 */         localStringBuilder.append("\n    [" + (i + 1) + "]: ");
/* 308 */         Extension localExtension = (Extension)arrayOfObject[i];
/*     */         try {
/* 310 */           if (OIDMap.getClass(localExtension.getExtensionId()) == null) {
/* 311 */             localStringBuilder.append(localExtension.toString());
/* 312 */             byte[] arrayOfByte = localExtension.getExtensionValue();
/* 313 */             if (arrayOfByte != null) {
/* 314 */               DerOutputStream localDerOutputStream = new DerOutputStream();
/* 315 */               localDerOutputStream.putOctetString(arrayOfByte);
/* 316 */               arrayOfByte = localDerOutputStream.toByteArray();
/* 317 */               HexDumpEncoder localHexDumpEncoder = new HexDumpEncoder();
/* 318 */               localStringBuilder.append("Extension unknown: DER encoded OCTET string =\n" + localHexDumpEncoder.encodeBuffer(arrayOfByte) + "\n");
/*     */             }
/*     */           }
/*     */           else
/*     */           {
/* 323 */             localStringBuilder.append(localExtension.toString());
/*     */           }
/*     */         } catch (Exception localException) { localStringBuilder.append(", Error parsing this extension"); }
/*     */ 
/*     */       }
/*     */     }
/* 329 */     localStringBuilder.append("\n");
/* 330 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   public boolean hasUnsupportedCriticalExtension()
/*     */   {
/* 338 */     if (this.extensions == null)
/* 339 */       return false;
/* 340 */     return this.extensions.hasUnsupportedCriticalExtension();
/*     */   }
/*     */ 
/*     */   public Set<String> getCriticalExtensionOIDs()
/*     */   {
/* 352 */     if (this.extensions == null) {
/* 353 */       return null;
/*     */     }
/* 355 */     TreeSet localTreeSet = new TreeSet();
/* 356 */     for (Extension localExtension : this.extensions.getAllExtensions()) {
/* 357 */       if (localExtension.isCritical()) {
/* 358 */         localTreeSet.add(localExtension.getExtensionId().toString());
/*     */       }
/*     */     }
/* 361 */     return localTreeSet;
/*     */   }
/*     */ 
/*     */   public Set<String> getNonCriticalExtensionOIDs()
/*     */   {
/* 373 */     if (this.extensions == null) {
/* 374 */       return null;
/*     */     }
/* 376 */     TreeSet localTreeSet = new TreeSet();
/* 377 */     for (Extension localExtension : this.extensions.getAllExtensions()) {
/* 378 */       if (!localExtension.isCritical()) {
/* 379 */         localTreeSet.add(localExtension.getExtensionId().toString());
/*     */       }
/*     */     }
/* 382 */     return localTreeSet;
/*     */   }
/*     */ 
/*     */   public byte[] getExtensionValue(String paramString)
/*     */   {
/* 398 */     if (this.extensions == null)
/* 399 */       return null;
/*     */     try {
/* 401 */       String str = OIDMap.getName(new ObjectIdentifier(paramString));
/* 402 */       Object localObject1 = null;
/*     */ 
/* 404 */       if (str == null) {
/* 405 */         localObject2 = new ObjectIdentifier(paramString);
/* 406 */         localObject3 = null;
/*     */ 
/* 408 */         Enumeration localEnumeration = this.extensions.getElements();
/* 409 */         while (localEnumeration.hasMoreElements()) {
/* 410 */           localObject3 = (Extension)localEnumeration.nextElement();
/* 411 */           ObjectIdentifier localObjectIdentifier = ((Extension)localObject3).getExtensionId();
/* 412 */           if (localObjectIdentifier.equals((ObjectIdentifier)localObject2))
/* 413 */             localObject1 = localObject3;
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 418 */         localObject1 = this.extensions.get(str);
/* 419 */       }if (localObject1 == null)
/* 420 */         return null;
/* 421 */       Object localObject2 = ((Extension)localObject1).getExtensionValue();
/* 422 */       if (localObject2 == null) {
/* 423 */         return null;
/*     */       }
/* 425 */       Object localObject3 = new DerOutputStream();
/* 426 */       ((DerOutputStream)localObject3).putOctetString((byte[])localObject2);
/* 427 */       return ((DerOutputStream)localObject3).toByteArray(); } catch (Exception localException) {
/*     */     }
/* 429 */     return null;
/*     */   }
/*     */ 
/*     */   public Extension getExtension(ObjectIdentifier paramObjectIdentifier)
/*     */   {
/* 440 */     if (this.extensions == null) {
/* 441 */       return null;
/*     */     }
/*     */ 
/* 445 */     return this.extensions.get(OIDMap.getName(paramObjectIdentifier));
/*     */   }
/*     */ 
/*     */   private void parse(DerValue paramDerValue)
/*     */     throws CRLException, IOException
/*     */   {
/* 451 */     if (paramDerValue.tag != 48) {
/* 452 */       throw new CRLException("Invalid encoded RevokedCertificate, starting sequence tag missing.");
/*     */     }
/*     */ 
/* 455 */     if (paramDerValue.data.available() == 0) {
/* 456 */       throw new CRLException("No data encoded for RevokedCertificates");
/*     */     }
/* 458 */     this.revokedCert = paramDerValue.toByteArray();
/*     */ 
/* 460 */     DerInputStream localDerInputStream = paramDerValue.toDerInputStream();
/* 461 */     DerValue localDerValue = localDerInputStream.getDerValue();
/* 462 */     this.serialNumber = new SerialNumber(localDerValue);
/*     */ 
/* 465 */     int i = paramDerValue.data.peekByte();
/* 466 */     if ((byte)i == 23)
/* 467 */       this.revocationDate = paramDerValue.data.getUTCTime();
/* 468 */     else if ((byte)i == 24)
/* 469 */       this.revocationDate = paramDerValue.data.getGeneralizedTime();
/*     */     else {
/* 471 */       throw new CRLException("Invalid encoding for revocation date");
/*     */     }
/* 473 */     if (paramDerValue.data.available() == 0) {
/* 474 */       return;
/*     */     }
/*     */ 
/* 477 */     this.extensions = new CRLExtensions(paramDerValue.toDerInputStream());
/*     */   }
/*     */ 
/*     */   public static X509CRLEntryImpl toImpl(X509CRLEntry paramX509CRLEntry)
/*     */     throws CRLException
/*     */   {
/* 487 */     if ((paramX509CRLEntry instanceof X509CRLEntryImpl)) {
/* 488 */       return (X509CRLEntryImpl)paramX509CRLEntry;
/*     */     }
/* 490 */     return new X509CRLEntryImpl(paramX509CRLEntry.getEncoded());
/*     */   }
/*     */ 
/*     */   CertificateIssuerExtension getCertificateIssuerExtension()
/*     */   {
/* 500 */     return (CertificateIssuerExtension)getExtension(PKIXExtensions.CertificateIssuer_Id);
/*     */   }
/*     */ 
/*     */   public Map<String, java.security.cert.Extension> getExtensions()
/*     */   {
/* 509 */     if (this.extensions == null) {
/* 510 */       return Collections.emptyMap();
/*     */     }
/* 512 */     Collection localCollection = this.extensions.getAllExtensions();
/* 513 */     TreeMap localTreeMap = new TreeMap();
/* 514 */     for (Extension localExtension : localCollection) {
/* 515 */       localTreeMap.put(localExtension.getId(), localExtension);
/*     */     }
/* 517 */     return localTreeMap;
/*     */   }
/*     */ 
/*     */   public int compareTo(X509CRLEntryImpl paramX509CRLEntryImpl)
/*     */   {
/* 522 */     int i = getSerialNumber().compareTo(paramX509CRLEntryImpl.getSerialNumber());
/* 523 */     if (i != 0)
/* 524 */       return i;
/*     */     try
/*     */     {
/* 527 */       byte[] arrayOfByte1 = getEncoded0();
/* 528 */       byte[] arrayOfByte2 = paramX509CRLEntryImpl.getEncoded0();
/* 529 */       for (int j = 0; (j < arrayOfByte1.length) && (j < arrayOfByte2.length); j++) {
/* 530 */         int k = arrayOfByte1[j] & 0xFF;
/* 531 */         int m = arrayOfByte2[j] & 0xFF;
/* 532 */         if (k != m) return k - m;
/*     */       }
/* 534 */       return arrayOfByte1.length - arrayOfByte2.length; } catch (CRLException localCRLException) {
/*     */     }
/* 536 */     return -1;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.x509.X509CRLEntryImpl
 * JD-Core Version:    0.6.2
 */