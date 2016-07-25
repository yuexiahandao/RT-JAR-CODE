/*      */ package sun.security.x509;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.OutputStream;
/*      */ import java.math.BigInteger;
/*      */ import java.security.InvalidKeyException;
/*      */ import java.security.NoSuchAlgorithmException;
/*      */ import java.security.NoSuchProviderException;
/*      */ import java.security.Principal;
/*      */ import java.security.PrivateKey;
/*      */ import java.security.PublicKey;
/*      */ import java.security.Signature;
/*      */ import java.security.SignatureException;
/*      */ import java.security.cert.CRLException;
/*      */ import java.security.cert.Certificate;
/*      */ import java.security.cert.X509CRL;
/*      */ import java.security.cert.X509CRLEntry;
/*      */ import java.security.cert.X509Certificate;
/*      */ import java.util.Collection;
/*      */ import java.util.Date;
/*      */ import java.util.Enumeration;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.TreeMap;
/*      */ import java.util.TreeSet;
/*      */ import javax.security.auth.x500.X500Principal;
/*      */ import sun.misc.HexDumpEncoder;
/*      */ import sun.security.provider.X509Factory;
/*      */ import sun.security.util.DerEncoder;
/*      */ import sun.security.util.DerInputStream;
/*      */ import sun.security.util.DerOutputStream;
/*      */ import sun.security.util.DerValue;
/*      */ import sun.security.util.ObjectIdentifier;
/*      */ 
/*      */ public class X509CRLImpl extends X509CRL
/*      */   implements DerEncoder
/*      */ {
/*   95 */   private byte[] signedCRL = null;
/*   96 */   private byte[] signature = null;
/*   97 */   private byte[] tbsCertList = null;
/*   98 */   private AlgorithmId sigAlgId = null;
/*      */   private int version;
/*      */   private AlgorithmId infoSigAlgId;
/*  103 */   private X500Name issuer = null;
/*  104 */   private X500Principal issuerPrincipal = null;
/*  105 */   private Date thisUpdate = null;
/*  106 */   private Date nextUpdate = null;
/*  107 */   private Map<X509IssuerSerial, X509CRLEntry> revokedMap = new TreeMap();
/*  108 */   private List<X509CRLEntry> revokedList = new LinkedList();
/*  109 */   private CRLExtensions extensions = null;
/*      */   private static final boolean isExplicit = true;
/*      */   private static final long YR_2050 = 2524636800000L;
/*  113 */   private boolean readOnly = false;
/*      */   private PublicKey verifiedPublicKey;
/*      */   private String verifiedProvider;
/*      */ 
/*      */   private X509CRLImpl()
/*      */   {
/*      */   }
/*      */ 
/*      */   public X509CRLImpl(byte[] paramArrayOfByte)
/*      */     throws CRLException
/*      */   {
/*      */     try
/*      */     {
/*  146 */       parse(new DerValue(paramArrayOfByte));
/*      */     } catch (IOException localIOException) {
/*  148 */       this.signedCRL = null;
/*  149 */       throw new CRLException("Parsing error: " + localIOException.getMessage());
/*      */     }
/*      */   }
/*      */ 
/*      */   public X509CRLImpl(DerValue paramDerValue)
/*      */     throws CRLException
/*      */   {
/*      */     try
/*      */     {
/*  161 */       parse(paramDerValue);
/*      */     } catch (IOException localIOException) {
/*  163 */       this.signedCRL = null;
/*  164 */       throw new CRLException("Parsing error: " + localIOException.getMessage());
/*      */     }
/*      */   }
/*      */ 
/*      */   public X509CRLImpl(InputStream paramInputStream)
/*      */     throws CRLException
/*      */   {
/*      */     try
/*      */     {
/*  177 */       parse(new DerValue(paramInputStream));
/*      */     } catch (IOException localIOException) {
/*  179 */       this.signedCRL = null;
/*  180 */       throw new CRLException("Parsing error: " + localIOException.getMessage());
/*      */     }
/*      */   }
/*      */ 
/*      */   public X509CRLImpl(X500Name paramX500Name, Date paramDate1, Date paramDate2)
/*      */   {
/*  192 */     this.issuer = paramX500Name;
/*  193 */     this.thisUpdate = paramDate1;
/*  194 */     this.nextUpdate = paramDate2;
/*      */   }
/*      */ 
/*      */   public X509CRLImpl(X500Name paramX500Name, Date paramDate1, Date paramDate2, X509CRLEntry[] paramArrayOfX509CRLEntry)
/*      */     throws CRLException
/*      */   {
/*  211 */     this.issuer = paramX500Name;
/*  212 */     this.thisUpdate = paramDate1;
/*  213 */     this.nextUpdate = paramDate2;
/*  214 */     if (paramArrayOfX509CRLEntry != null) {
/*  215 */       X500Principal localX500Principal1 = getIssuerX500Principal();
/*  216 */       X500Principal localX500Principal2 = localX500Principal1;
/*  217 */       for (int i = 0; i < paramArrayOfX509CRLEntry.length; i++) {
/*  218 */         X509CRLEntryImpl localX509CRLEntryImpl = (X509CRLEntryImpl)paramArrayOfX509CRLEntry[i];
/*      */         try {
/*  220 */           localX500Principal2 = getCertIssuer(localX509CRLEntryImpl, localX500Principal2);
/*      */         } catch (IOException localIOException) {
/*  222 */           throw new CRLException(localIOException);
/*      */         }
/*  224 */         localX509CRLEntryImpl.setCertificateIssuer(localX500Principal1, localX500Principal2);
/*  225 */         X509IssuerSerial localX509IssuerSerial = new X509IssuerSerial(localX500Principal2, localX509CRLEntryImpl.getSerialNumber());
/*      */ 
/*  227 */         this.revokedMap.put(localX509IssuerSerial, localX509CRLEntryImpl);
/*  228 */         this.revokedList.add(localX509CRLEntryImpl);
/*  229 */         if (localX509CRLEntryImpl.hasExtensions())
/*  230 */           this.version = 1;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public X509CRLImpl(X500Name paramX500Name, Date paramDate1, Date paramDate2, X509CRLEntry[] paramArrayOfX509CRLEntry, CRLExtensions paramCRLExtensions)
/*      */     throws CRLException
/*      */   {
/*  251 */     this(paramX500Name, paramDate1, paramDate2, paramArrayOfX509CRLEntry);
/*  252 */     if (paramCRLExtensions != null) {
/*  253 */       this.extensions = paramCRLExtensions;
/*  254 */       this.version = 1;
/*      */     }
/*      */   }
/*      */ 
/*      */   public byte[] getEncodedInternal()
/*      */     throws CRLException
/*      */   {
/*  264 */     if (this.signedCRL == null) {
/*  265 */       throw new CRLException("Null CRL to encode");
/*      */     }
/*  267 */     return this.signedCRL;
/*      */   }
/*      */ 
/*      */   public byte[] getEncoded()
/*      */     throws CRLException
/*      */   {
/*  276 */     return (byte[])getEncodedInternal().clone();
/*      */   }
/*      */ 
/*      */   public void encodeInfo(OutputStream paramOutputStream)
/*      */     throws CRLException
/*      */   {
/*      */     try
/*      */     {
/*  287 */       DerOutputStream localDerOutputStream1 = new DerOutputStream();
/*  288 */       DerOutputStream localDerOutputStream2 = new DerOutputStream();
/*  289 */       DerOutputStream localDerOutputStream3 = new DerOutputStream();
/*      */ 
/*  291 */       if (this.version != 0)
/*  292 */         localDerOutputStream1.putInteger(this.version);
/*  293 */       this.infoSigAlgId.encode(localDerOutputStream1);
/*  294 */       if ((this.version == 0) && (this.issuer.toString() == null))
/*  295 */         throw new CRLException("Null Issuer DN not allowed in v1 CRL");
/*  296 */       this.issuer.encode(localDerOutputStream1);
/*      */ 
/*  298 */       if (this.thisUpdate.getTime() < 2524636800000L)
/*  299 */         localDerOutputStream1.putUTCTime(this.thisUpdate);
/*      */       else {
/*  301 */         localDerOutputStream1.putGeneralizedTime(this.thisUpdate);
/*      */       }
/*  303 */       if (this.nextUpdate != null) {
/*  304 */         if (this.nextUpdate.getTime() < 2524636800000L)
/*  305 */           localDerOutputStream1.putUTCTime(this.nextUpdate);
/*      */         else {
/*  307 */           localDerOutputStream1.putGeneralizedTime(this.nextUpdate);
/*      */         }
/*      */       }
/*  310 */       if (!this.revokedList.isEmpty()) {
/*  311 */         for (X509CRLEntry localX509CRLEntry : this.revokedList) {
/*  312 */           ((X509CRLEntryImpl)localX509CRLEntry).encode(localDerOutputStream2);
/*      */         }
/*  314 */         localDerOutputStream1.write((byte)48, localDerOutputStream2);
/*      */       }
/*      */ 
/*  317 */       if (this.extensions != null) {
/*  318 */         this.extensions.encode(localDerOutputStream1, true);
/*      */       }
/*  320 */       localDerOutputStream3.write((byte)48, localDerOutputStream1);
/*      */ 
/*  322 */       this.tbsCertList = localDerOutputStream3.toByteArray();
/*  323 */       paramOutputStream.write(this.tbsCertList);
/*      */     } catch (IOException localIOException) {
/*  325 */       throw new CRLException("Encoding error: " + localIOException.getMessage());
/*      */     }
/*      */   }
/*      */ 
/*      */   public void verify(PublicKey paramPublicKey)
/*      */     throws CRLException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, SignatureException
/*      */   {
/*  345 */     verify(paramPublicKey, "");
/*      */   }
/*      */ 
/*      */   public synchronized void verify(PublicKey paramPublicKey, String paramString)
/*      */     throws CRLException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, SignatureException
/*      */   {
/*  368 */     if (paramString == null) {
/*  369 */       paramString = "";
/*      */     }
/*  371 */     if ((this.verifiedPublicKey != null) && (this.verifiedPublicKey.equals(paramPublicKey)))
/*      */     {
/*  374 */       if (paramString.equals(this.verifiedProvider)) {
/*  375 */         return;
/*      */       }
/*      */     }
/*  378 */     if (this.signedCRL == null) {
/*  379 */       throw new CRLException("Uninitialized CRL");
/*      */     }
/*  381 */     Signature localSignature = null;
/*  382 */     if (paramString.length() == 0)
/*  383 */       localSignature = Signature.getInstance(this.sigAlgId.getName());
/*      */     else {
/*  385 */       localSignature = Signature.getInstance(this.sigAlgId.getName(), paramString);
/*      */     }
/*  387 */     localSignature.initVerify(paramPublicKey);
/*      */ 
/*  389 */     if (this.tbsCertList == null) {
/*  390 */       throw new CRLException("Uninitialized CRL");
/*      */     }
/*      */ 
/*  393 */     localSignature.update(this.tbsCertList, 0, this.tbsCertList.length);
/*      */ 
/*  395 */     if (!localSignature.verify(this.signature)) {
/*  396 */       throw new SignatureException("Signature does not match.");
/*      */     }
/*  398 */     this.verifiedPublicKey = paramPublicKey;
/*  399 */     this.verifiedProvider = paramString;
/*      */   }
/*      */ 
/*      */   public void sign(PrivateKey paramPrivateKey, String paramString)
/*      */     throws CRLException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, SignatureException
/*      */   {
/*  418 */     sign(paramPrivateKey, paramString, null);
/*      */   }
/*      */ 
/*      */   public void sign(PrivateKey paramPrivateKey, String paramString1, String paramString2)
/*      */     throws CRLException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, SignatureException
/*      */   {
/*      */     try
/*      */     {
/*  439 */       if (this.readOnly)
/*  440 */         throw new CRLException("cannot over-write existing CRL");
/*  441 */       Signature localSignature = null;
/*  442 */       if ((paramString2 == null) || (paramString2.length() == 0))
/*  443 */         localSignature = Signature.getInstance(paramString1);
/*      */       else {
/*  445 */         localSignature = Signature.getInstance(paramString1, paramString2);
/*      */       }
/*  447 */       localSignature.initSign(paramPrivateKey);
/*      */ 
/*  450 */       this.sigAlgId = AlgorithmId.get(localSignature.getAlgorithm());
/*  451 */       this.infoSigAlgId = this.sigAlgId;
/*      */ 
/*  453 */       DerOutputStream localDerOutputStream1 = new DerOutputStream();
/*  454 */       DerOutputStream localDerOutputStream2 = new DerOutputStream();
/*      */ 
/*  457 */       encodeInfo(localDerOutputStream2);
/*      */ 
/*  460 */       this.sigAlgId.encode(localDerOutputStream2);
/*      */ 
/*  463 */       localSignature.update(this.tbsCertList, 0, this.tbsCertList.length);
/*  464 */       this.signature = localSignature.sign();
/*  465 */       localDerOutputStream2.putBitString(this.signature);
/*      */ 
/*  468 */       localDerOutputStream1.write((byte)48, localDerOutputStream2);
/*  469 */       this.signedCRL = localDerOutputStream1.toByteArray();
/*  470 */       this.readOnly = true;
/*      */     }
/*      */     catch (IOException localIOException) {
/*  473 */       throw new CRLException("Error while encoding data: " + localIOException.getMessage());
/*      */     }
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/*  484 */     StringBuffer localStringBuffer = new StringBuffer();
/*  485 */     localStringBuffer.append("X.509 CRL v" + (this.version + 1) + "\n");
/*  486 */     if (this.sigAlgId != null) {
/*  487 */       localStringBuffer.append("Signature Algorithm: " + this.sigAlgId.toString() + ", OID=" + this.sigAlgId.getOID().toString() + "\n");
/*      */     }
/*  489 */     if (this.issuer != null)
/*  490 */       localStringBuffer.append("Issuer: " + this.issuer.toString() + "\n");
/*  491 */     if (this.thisUpdate != null)
/*  492 */       localStringBuffer.append("\nThis Update: " + this.thisUpdate.toString() + "\n");
/*  493 */     if (this.nextUpdate != null)
/*  494 */       localStringBuffer.append("Next Update: " + this.nextUpdate.toString() + "\n");
/*      */     int i;
/*      */     Object localObject2;
/*  495 */     if (this.revokedList.isEmpty()) {
/*  496 */       localStringBuffer.append("\nNO certificates have been revoked\n");
/*      */     } else {
/*  498 */       localStringBuffer.append("\nRevoked Certificates: " + this.revokedList.size());
/*  499 */       i = 1;
/*  500 */       for (localObject2 = this.revokedList.iterator(); ((Iterator)localObject2).hasNext(); ) { X509CRLEntry localX509CRLEntry = (X509CRLEntry)((Iterator)localObject2).next();
/*  501 */         localStringBuffer.append("\n[" + i++ + "] " + localX509CRLEntry.toString());
/*      */       }
/*      */     }
/*      */     Object localObject1;
/*  504 */     if (this.extensions != null) {
/*  505 */       localObject1 = this.extensions.getAllExtensions();
/*  506 */       localObject2 = ((Collection)localObject1).toArray();
/*  507 */       localStringBuffer.append("\nCRL Extensions: " + localObject2.length);
/*  508 */       for (int j = 0; j < localObject2.length; j++) {
/*  509 */         localStringBuffer.append("\n[" + (j + 1) + "]: ");
/*  510 */         Extension localExtension = (Extension)localObject2[j];
/*      */         try {
/*  512 */           if (OIDMap.getClass(localExtension.getExtensionId()) == null) {
/*  513 */             localStringBuffer.append(localExtension.toString());
/*  514 */             byte[] arrayOfByte = localExtension.getExtensionValue();
/*  515 */             if (arrayOfByte != null) {
/*  516 */               DerOutputStream localDerOutputStream = new DerOutputStream();
/*  517 */               localDerOutputStream.putOctetString(arrayOfByte);
/*  518 */               arrayOfByte = localDerOutputStream.toByteArray();
/*  519 */               HexDumpEncoder localHexDumpEncoder = new HexDumpEncoder();
/*  520 */               localStringBuffer.append("Extension unknown: DER encoded OCTET string =\n" + localHexDumpEncoder.encodeBuffer(arrayOfByte) + "\n");
/*      */             }
/*      */           }
/*      */           else
/*      */           {
/*  525 */             localStringBuffer.append(localExtension.toString());
/*      */           }
/*      */         } catch (Exception localException) { localStringBuffer.append(", Error parsing this extension"); }
/*      */ 
/*      */       }
/*      */     }
/*  531 */     if (this.signature != null) {
/*  532 */       localObject1 = new HexDumpEncoder();
/*  533 */       localStringBuffer.append("\nSignature:\n" + ((HexDumpEncoder)localObject1).encodeBuffer(this.signature) + "\n");
/*      */     }
/*      */     else {
/*  536 */       localStringBuffer.append("NOT signed yet\n");
/*  537 */     }return localStringBuffer.toString();
/*      */   }
/*      */ 
/*      */   public boolean isRevoked(Certificate paramCertificate)
/*      */   {
/*  548 */     if ((this.revokedMap.isEmpty()) || (!(paramCertificate instanceof X509Certificate))) {
/*  549 */       return false;
/*      */     }
/*  551 */     X509Certificate localX509Certificate = (X509Certificate)paramCertificate;
/*  552 */     X509IssuerSerial localX509IssuerSerial = new X509IssuerSerial(localX509Certificate);
/*  553 */     return this.revokedMap.containsKey(localX509IssuerSerial);
/*      */   }
/*      */ 
/*      */   public int getVersion()
/*      */   {
/*  567 */     return this.version + 1;
/*      */   }
/*      */ 
/*      */   public Principal getIssuerDN()
/*      */   {
/*  599 */     return this.issuer;
/*      */   }
/*      */ 
/*      */   public X500Principal getIssuerX500Principal()
/*      */   {
/*  607 */     if (this.issuerPrincipal == null) {
/*  608 */       this.issuerPrincipal = this.issuer.asX500Principal();
/*      */     }
/*  610 */     return this.issuerPrincipal;
/*      */   }
/*      */ 
/*      */   public Date getThisUpdate()
/*      */   {
/*  620 */     return new Date(this.thisUpdate.getTime());
/*      */   }
/*      */ 
/*      */   public Date getNextUpdate()
/*      */   {
/*  630 */     if (this.nextUpdate == null)
/*  631 */       return null;
/*  632 */     return new Date(this.nextUpdate.getTime());
/*      */   }
/*      */ 
/*      */   public X509CRLEntry getRevokedCertificate(BigInteger paramBigInteger)
/*      */   {
/*  643 */     if (this.revokedMap.isEmpty()) {
/*  644 */       return null;
/*      */     }
/*      */ 
/*  647 */     X509IssuerSerial localX509IssuerSerial = new X509IssuerSerial(getIssuerX500Principal(), paramBigInteger);
/*      */ 
/*  649 */     return (X509CRLEntry)this.revokedMap.get(localX509IssuerSerial);
/*      */   }
/*      */ 
/*      */   public X509CRLEntry getRevokedCertificate(X509Certificate paramX509Certificate)
/*      */   {
/*  656 */     if (this.revokedMap.isEmpty()) {
/*  657 */       return null;
/*      */     }
/*  659 */     X509IssuerSerial localX509IssuerSerial = new X509IssuerSerial(paramX509Certificate);
/*  660 */     return (X509CRLEntry)this.revokedMap.get(localX509IssuerSerial);
/*      */   }
/*      */ 
/*      */   public Set<X509CRLEntry> getRevokedCertificates()
/*      */   {
/*  672 */     if (this.revokedList.isEmpty()) {
/*  673 */       return null;
/*      */     }
/*  675 */     return new TreeSet(this.revokedList);
/*      */   }
/*      */ 
/*      */   public byte[] getTBSCertList()
/*      */     throws CRLException
/*      */   {
/*  688 */     if (this.tbsCertList == null)
/*  689 */       throw new CRLException("Uninitialized CRL");
/*  690 */     byte[] arrayOfByte = new byte[this.tbsCertList.length];
/*  691 */     System.arraycopy(this.tbsCertList, 0, arrayOfByte, 0, arrayOfByte.length);
/*  692 */     return arrayOfByte;
/*      */   }
/*      */ 
/*      */   public byte[] getSignature()
/*      */   {
/*  701 */     if (this.signature == null)
/*  702 */       return null;
/*  703 */     byte[] arrayOfByte = new byte[this.signature.length];
/*  704 */     System.arraycopy(this.signature, 0, arrayOfByte, 0, arrayOfByte.length);
/*  705 */     return arrayOfByte;
/*      */   }
/*      */ 
/*      */   public String getSigAlgName()
/*      */   {
/*  724 */     if (this.sigAlgId == null)
/*  725 */       return null;
/*  726 */     return this.sigAlgId.getName();
/*      */   }
/*      */ 
/*      */   public String getSigAlgOID()
/*      */   {
/*  743 */     if (this.sigAlgId == null)
/*  744 */       return null;
/*  745 */     ObjectIdentifier localObjectIdentifier = this.sigAlgId.getOID();
/*  746 */     return localObjectIdentifier.toString();
/*      */   }
/*      */ 
/*      */   public byte[] getSigAlgParams()
/*      */   {
/*  759 */     if (this.sigAlgId == null)
/*  760 */       return null;
/*      */     try {
/*  762 */       return this.sigAlgId.getEncodedParams(); } catch (IOException localIOException) {
/*      */     }
/*  764 */     return null;
/*      */   }
/*      */ 
/*      */   public AlgorithmId getSigAlgId()
/*      */   {
/*  774 */     return this.sigAlgId;
/*      */   }
/*      */ 
/*      */   public KeyIdentifier getAuthKeyId()
/*      */     throws IOException
/*      */   {
/*  785 */     AuthorityKeyIdentifierExtension localAuthorityKeyIdentifierExtension = getAuthKeyIdExtension();
/*  786 */     if (localAuthorityKeyIdentifierExtension != null) {
/*  787 */       KeyIdentifier localKeyIdentifier = (KeyIdentifier)localAuthorityKeyIdentifierExtension.get("key_id");
/*  788 */       return localKeyIdentifier;
/*      */     }
/*  790 */     return null;
/*      */   }
/*      */ 
/*      */   public AuthorityKeyIdentifierExtension getAuthKeyIdExtension()
/*      */     throws IOException
/*      */   {
/*  802 */     Object localObject = getExtension(PKIXExtensions.AuthorityKey_Id);
/*  803 */     return (AuthorityKeyIdentifierExtension)localObject;
/*      */   }
/*      */ 
/*      */   public CRLNumberExtension getCRLNumberExtension()
/*      */     throws IOException
/*      */   {
/*  813 */     Object localObject = getExtension(PKIXExtensions.CRLNumber_Id);
/*  814 */     return (CRLNumberExtension)localObject;
/*      */   }
/*      */ 
/*      */   public BigInteger getCRLNumber()
/*      */     throws IOException
/*      */   {
/*  824 */     CRLNumberExtension localCRLNumberExtension = getCRLNumberExtension();
/*  825 */     if (localCRLNumberExtension != null) {
/*  826 */       BigInteger localBigInteger = (BigInteger)localCRLNumberExtension.get("value");
/*  827 */       return localBigInteger;
/*      */     }
/*  829 */     return null;
/*      */   }
/*      */ 
/*      */   public DeltaCRLIndicatorExtension getDeltaCRLIndicatorExtension()
/*      */     throws IOException
/*      */   {
/*  842 */     Object localObject = getExtension(PKIXExtensions.DeltaCRLIndicator_Id);
/*  843 */     return (DeltaCRLIndicatorExtension)localObject;
/*      */   }
/*      */ 
/*      */   public BigInteger getBaseCRLNumber()
/*      */     throws IOException
/*      */   {
/*  853 */     DeltaCRLIndicatorExtension localDeltaCRLIndicatorExtension = getDeltaCRLIndicatorExtension();
/*  854 */     if (localDeltaCRLIndicatorExtension != null) {
/*  855 */       BigInteger localBigInteger = (BigInteger)localDeltaCRLIndicatorExtension.get("value");
/*  856 */       return localBigInteger;
/*      */     }
/*  858 */     return null;
/*      */   }
/*      */ 
/*      */   public IssuerAlternativeNameExtension getIssuerAltNameExtension()
/*      */     throws IOException
/*      */   {
/*  870 */     Object localObject = getExtension(PKIXExtensions.IssuerAlternativeName_Id);
/*  871 */     return (IssuerAlternativeNameExtension)localObject;
/*      */   }
/*      */ 
/*      */   public IssuingDistributionPointExtension getIssuingDistributionPointExtension()
/*      */     throws IOException
/*      */   {
/*  884 */     Object localObject = getExtension(PKIXExtensions.IssuingDistributionPoint_Id);
/*  885 */     return (IssuingDistributionPointExtension)localObject;
/*      */   }
/*      */ 
/*      */   public boolean hasUnsupportedCriticalExtension()
/*      */   {
/*  893 */     if (this.extensions == null)
/*  894 */       return false;
/*  895 */     return this.extensions.hasUnsupportedCriticalExtension();
/*      */   }
/*      */ 
/*      */   public Set<String> getCriticalExtensionOIDs()
/*      */   {
/*  907 */     if (this.extensions == null) {
/*  908 */       return null;
/*      */     }
/*  910 */     TreeSet localTreeSet = new TreeSet();
/*  911 */     for (Extension localExtension : this.extensions.getAllExtensions()) {
/*  912 */       if (localExtension.isCritical()) {
/*  913 */         localTreeSet.add(localExtension.getExtensionId().toString());
/*      */       }
/*      */     }
/*  916 */     return localTreeSet;
/*      */   }
/*      */ 
/*      */   public Set<String> getNonCriticalExtensionOIDs()
/*      */   {
/*  928 */     if (this.extensions == null) {
/*  929 */       return null;
/*      */     }
/*  931 */     TreeSet localTreeSet = new TreeSet();
/*  932 */     for (Extension localExtension : this.extensions.getAllExtensions()) {
/*  933 */       if (!localExtension.isCritical()) {
/*  934 */         localTreeSet.add(localExtension.getExtensionId().toString());
/*      */       }
/*      */     }
/*  937 */     return localTreeSet;
/*      */   }
/*      */ 
/*      */   public byte[] getExtensionValue(String paramString)
/*      */   {
/*  952 */     if (this.extensions == null)
/*  953 */       return null;
/*      */     try {
/*  955 */       String str = OIDMap.getName(new ObjectIdentifier(paramString));
/*  956 */       Object localObject1 = null;
/*      */ 
/*  958 */       if (str == null) {
/*  959 */         localObject2 = new ObjectIdentifier(paramString);
/*  960 */         localObject3 = null;
/*      */ 
/*  962 */         Enumeration localEnumeration = this.extensions.getElements();
/*  963 */         while (localEnumeration.hasMoreElements()) {
/*  964 */           localObject3 = (Extension)localEnumeration.nextElement();
/*  965 */           ObjectIdentifier localObjectIdentifier = ((Extension)localObject3).getExtensionId();
/*  966 */           if (localObjectIdentifier.equals((ObjectIdentifier)localObject2))
/*  967 */             localObject1 = localObject3;
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/*  972 */         localObject1 = this.extensions.get(str);
/*  973 */       }if (localObject1 == null)
/*  974 */         return null;
/*  975 */       Object localObject2 = ((Extension)localObject1).getExtensionValue();
/*  976 */       if (localObject2 == null)
/*  977 */         return null;
/*  978 */       Object localObject3 = new DerOutputStream();
/*  979 */       ((DerOutputStream)localObject3).putOctetString((byte[])localObject2);
/*  980 */       return ((DerOutputStream)localObject3).toByteArray(); } catch (Exception localException) {
/*      */     }
/*  982 */     return null;
/*      */   }
/*      */ 
/*      */   public Object getExtension(ObjectIdentifier paramObjectIdentifier)
/*      */   {
/*  994 */     if (this.extensions == null) {
/*  995 */       return null;
/*      */     }
/*      */ 
/*  998 */     return this.extensions.get(OIDMap.getName(paramObjectIdentifier));
/*      */   }
/*      */ 
/*      */   private void parse(DerValue paramDerValue)
/*      */     throws CRLException, IOException
/*      */   {
/* 1006 */     if (this.readOnly) {
/* 1007 */       throw new CRLException("cannot over-write existing CRL");
/*      */     }
/* 1009 */     if ((paramDerValue.getData() == null) || (paramDerValue.tag != 48)) {
/* 1010 */       throw new CRLException("Invalid DER-encoded CRL data");
/*      */     }
/* 1012 */     this.signedCRL = paramDerValue.toByteArray();
/* 1013 */     DerValue[] arrayOfDerValue1 = new DerValue[3];
/*      */ 
/* 1015 */     arrayOfDerValue1[0] = paramDerValue.data.getDerValue();
/* 1016 */     arrayOfDerValue1[1] = paramDerValue.data.getDerValue();
/* 1017 */     arrayOfDerValue1[2] = paramDerValue.data.getDerValue();
/*      */ 
/* 1019 */     if (paramDerValue.data.available() != 0) {
/* 1020 */       throw new CRLException("signed overrun, bytes = " + paramDerValue.data.available());
/*      */     }
/*      */ 
/* 1023 */     if (arrayOfDerValue1[0].tag != 48) {
/* 1024 */       throw new CRLException("signed CRL fields invalid");
/*      */     }
/* 1026 */     this.sigAlgId = AlgorithmId.parse(arrayOfDerValue1[1]);
/* 1027 */     this.signature = arrayOfDerValue1[2].getBitString();
/*      */ 
/* 1029 */     if (arrayOfDerValue1[1].data.available() != 0) {
/* 1030 */       throw new CRLException("AlgorithmId field overrun");
/*      */     }
/* 1032 */     if (arrayOfDerValue1[2].data.available() != 0) {
/* 1033 */       throw new CRLException("Signature field overrun");
/*      */     }
/*      */ 
/* 1036 */     this.tbsCertList = arrayOfDerValue1[0].toByteArray();
/*      */ 
/* 1039 */     DerInputStream localDerInputStream = arrayOfDerValue1[0].data;
/*      */ 
/* 1044 */     this.version = 0;
/* 1045 */     int i = (byte)localDerInputStream.peekByte();
/* 1046 */     if (i == 2) {
/* 1047 */       this.version = localDerInputStream.getInteger();
/* 1048 */       if (this.version != 1)
/* 1049 */         throw new CRLException("Invalid version");
/*      */     }
/* 1051 */     DerValue localDerValue = localDerInputStream.getDerValue();
/*      */ 
/* 1054 */     AlgorithmId localAlgorithmId = AlgorithmId.parse(localDerValue);
/*      */ 
/* 1057 */     if (!localAlgorithmId.equals(this.sigAlgId))
/* 1058 */       throw new CRLException("Signature algorithm mismatch");
/* 1059 */     this.infoSigAlgId = localAlgorithmId;
/*      */ 
/* 1062 */     this.issuer = new X500Name(localDerInputStream);
/* 1063 */     if (this.issuer.isEmpty()) {
/* 1064 */       throw new CRLException("Empty issuer DN not allowed in X509CRLs");
/*      */     }
/*      */ 
/* 1070 */     i = (byte)localDerInputStream.peekByte();
/* 1071 */     if (i == 23)
/* 1072 */       this.thisUpdate = localDerInputStream.getUTCTime();
/* 1073 */     else if (i == 24)
/* 1074 */       this.thisUpdate = localDerInputStream.getGeneralizedTime();
/*      */     else {
/* 1076 */       throw new CRLException("Invalid encoding for thisUpdate (tag=" + i + ")");
/*      */     }
/*      */ 
/* 1080 */     if (localDerInputStream.available() == 0) {
/* 1081 */       return;
/*      */     }
/*      */ 
/* 1084 */     i = (byte)localDerInputStream.peekByte();
/* 1085 */     if (i == 23)
/* 1086 */       this.nextUpdate = localDerInputStream.getUTCTime();
/* 1087 */     else if (i == 24) {
/* 1088 */       this.nextUpdate = localDerInputStream.getGeneralizedTime();
/*      */     }
/*      */ 
/* 1091 */     if (localDerInputStream.available() == 0) {
/* 1092 */       return;
/*      */     }
/*      */ 
/* 1095 */     i = (byte)localDerInputStream.peekByte();
/* 1096 */     if ((i == 48) && ((i & 0xC0) != 128))
/*      */     {
/* 1098 */       DerValue[] arrayOfDerValue2 = localDerInputStream.getSequence(4);
/*      */ 
/* 1100 */       X500Principal localX500Principal1 = getIssuerX500Principal();
/* 1101 */       X500Principal localX500Principal2 = localX500Principal1;
/* 1102 */       for (int j = 0; j < arrayOfDerValue2.length; j++) {
/* 1103 */         X509CRLEntryImpl localX509CRLEntryImpl = new X509CRLEntryImpl(arrayOfDerValue2[j]);
/* 1104 */         localX500Principal2 = getCertIssuer(localX509CRLEntryImpl, localX500Principal2);
/* 1105 */         localX509CRLEntryImpl.setCertificateIssuer(localX500Principal1, localX500Principal2);
/* 1106 */         X509IssuerSerial localX509IssuerSerial = new X509IssuerSerial(localX500Principal2, localX509CRLEntryImpl.getSerialNumber());
/*      */ 
/* 1108 */         this.revokedMap.put(localX509IssuerSerial, localX509CRLEntryImpl);
/* 1109 */         this.revokedList.add(localX509CRLEntryImpl);
/*      */       }
/*      */     }
/*      */ 
/* 1113 */     if (localDerInputStream.available() == 0) {
/* 1114 */       return;
/*      */     }
/*      */ 
/* 1117 */     localDerValue = localDerInputStream.getDerValue();
/* 1118 */     if ((localDerValue.isConstructed()) && (localDerValue.isContextSpecific((byte)0))) {
/* 1119 */       this.extensions = new CRLExtensions(localDerValue.data);
/*      */     }
/* 1121 */     this.readOnly = true;
/*      */   }
/*      */ 
/*      */   public static X500Principal getIssuerX500Principal(X509CRL paramX509CRL)
/*      */   {
/*      */     try
/*      */     {
/* 1132 */       byte[] arrayOfByte1 = paramX509CRL.getEncoded();
/* 1133 */       DerInputStream localDerInputStream1 = new DerInputStream(arrayOfByte1);
/* 1134 */       DerValue localDerValue1 = localDerInputStream1.getSequence(3)[0];
/* 1135 */       DerInputStream localDerInputStream2 = localDerValue1.data;
/*      */ 
/* 1139 */       int i = (byte)localDerInputStream2.peekByte();
/* 1140 */       if (i == 2) {
/* 1141 */         localDerValue2 = localDerInputStream2.getDerValue();
/*      */       }
/*      */ 
/* 1144 */       DerValue localDerValue2 = localDerInputStream2.getDerValue();
/* 1145 */       localDerValue2 = localDerInputStream2.getDerValue();
/* 1146 */       byte[] arrayOfByte2 = localDerValue2.toByteArray();
/* 1147 */       return new X500Principal(arrayOfByte2);
/*      */     } catch (Exception localException) {
/* 1149 */       throw new RuntimeException("Could not parse issuer", localException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public static byte[] getEncodedInternal(X509CRL paramX509CRL)
/*      */     throws CRLException
/*      */   {
/* 1160 */     if ((paramX509CRL instanceof X509CRLImpl)) {
/* 1161 */       return ((X509CRLImpl)paramX509CRL).getEncodedInternal();
/*      */     }
/* 1163 */     return paramX509CRL.getEncoded();
/*      */   }
/*      */ 
/*      */   public static X509CRLImpl toImpl(X509CRL paramX509CRL)
/*      */     throws CRLException
/*      */   {
/* 1174 */     if ((paramX509CRL instanceof X509CRLImpl)) {
/* 1175 */       return (X509CRLImpl)paramX509CRL;
/*      */     }
/* 1177 */     return X509Factory.intern(paramX509CRL);
/*      */   }
/*      */ 
/*      */   private X500Principal getCertIssuer(X509CRLEntryImpl paramX509CRLEntryImpl, X500Principal paramX500Principal)
/*      */     throws IOException
/*      */   {
/* 1192 */     CertificateIssuerExtension localCertificateIssuerExtension = paramX509CRLEntryImpl.getCertificateIssuerExtension();
/*      */ 
/* 1194 */     if (localCertificateIssuerExtension != null) {
/* 1195 */       GeneralNames localGeneralNames = (GeneralNames)localCertificateIssuerExtension.get("issuer");
/*      */ 
/* 1197 */       X500Name localX500Name = (X500Name)localGeneralNames.get(0).getName();
/* 1198 */       return localX500Name.asX500Principal();
/*      */     }
/* 1200 */     return paramX500Principal;
/*      */   }
/*      */ 
/*      */   public void derEncode(OutputStream paramOutputStream)
/*      */     throws IOException
/*      */   {
/* 1206 */     if (this.signedCRL == null)
/* 1207 */       throw new IOException("Null CRL to encode");
/* 1208 */     paramOutputStream.write((byte[])this.signedCRL.clone());
/*      */   }
/*      */ 
/*      */   private static final class X509IssuerSerial
/*      */     implements Comparable<X509IssuerSerial>
/*      */   {
/*      */     final X500Principal issuer;
/*      */     final BigInteger serial;
/* 1218 */     volatile int hashcode = 0;
/*      */ 
/*      */     X509IssuerSerial(X500Principal paramX500Principal, BigInteger paramBigInteger)
/*      */     {
/* 1227 */       this.issuer = paramX500Principal;
/* 1228 */       this.serial = paramBigInteger;
/*      */     }
/*      */ 
/*      */     X509IssuerSerial(X509Certificate paramX509Certificate)
/*      */     {
/* 1235 */       this(paramX509Certificate.getIssuerX500Principal(), paramX509Certificate.getSerialNumber());
/*      */     }
/*      */ 
/*      */     X500Principal getIssuer()
/*      */     {
/* 1244 */       return this.issuer;
/*      */     }
/*      */ 
/*      */     BigInteger getSerial()
/*      */     {
/* 1253 */       return this.serial;
/*      */     }
/*      */ 
/*      */     public boolean equals(Object paramObject)
/*      */     {
/* 1264 */       if (paramObject == this) {
/* 1265 */         return true;
/*      */       }
/*      */ 
/* 1268 */       if (!(paramObject instanceof X509IssuerSerial)) {
/* 1269 */         return false;
/*      */       }
/*      */ 
/* 1272 */       X509IssuerSerial localX509IssuerSerial = (X509IssuerSerial)paramObject;
/* 1273 */       if ((this.serial.equals(localX509IssuerSerial.getSerial())) && (this.issuer.equals(localX509IssuerSerial.getIssuer())))
/*      */       {
/* 1275 */         return true;
/*      */       }
/* 1277 */       return false;
/*      */     }
/*      */ 
/*      */     public int hashCode()
/*      */     {
/* 1286 */       if (this.hashcode == 0) {
/* 1287 */         int i = 17;
/* 1288 */         i = 37 * i + this.issuer.hashCode();
/* 1289 */         i = 37 * i + this.serial.hashCode();
/* 1290 */         this.hashcode = i;
/*      */       }
/* 1292 */       return this.hashcode;
/*      */     }
/*      */ 
/*      */     public int compareTo(X509IssuerSerial paramX509IssuerSerial)
/*      */     {
/* 1297 */       int i = this.issuer.toString().compareTo(paramX509IssuerSerial.issuer.toString());
/*      */ 
/* 1299 */       if (i != 0) return i;
/* 1300 */       return this.serial.compareTo(paramX509IssuerSerial.serial);
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.x509.X509CRLImpl
 * JD-Core Version:    0.6.2
 */