/*      */ package sun.security.x509;
/*      */ 
/*      */ import java.io.BufferedInputStream;
/*      */ import java.io.BufferedReader;
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.InputStreamReader;
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
/*      */ import java.security.cert.Certificate;
/*      */ import java.security.cert.CertificateEncodingException;
/*      */ import java.security.cert.CertificateException;
/*      */ import java.security.cert.CertificateExpiredException;
/*      */ import java.security.cert.CertificateNotYetValidException;
/*      */ import java.security.cert.CertificateParsingException;
/*      */ import java.security.cert.X509Certificate;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Date;
/*      */ import java.util.Enumeration;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.TreeSet;
/*      */ import javax.security.auth.x500.X500Principal;
/*      */ import sun.misc.BASE64Decoder;
/*      */ import sun.misc.HexDumpEncoder;
/*      */ import sun.security.provider.X509Factory;
/*      */ import sun.security.util.DerEncoder;
/*      */ import sun.security.util.DerInputStream;
/*      */ import sun.security.util.DerOutputStream;
/*      */ import sun.security.util.DerValue;
/*      */ import sun.security.util.ObjectIdentifier;
/*      */ 
/*      */ public class X509CertImpl extends X509Certificate
/*      */   implements DerEncoder
/*      */ {
/*      */   private static final long serialVersionUID = -3457612960190864406L;
/*      */   private static final String DOT = ".";
/*      */   public static final String NAME = "x509";
/*      */   public static final String INFO = "info";
/*      */   public static final String ALG_ID = "algorithm";
/*      */   public static final String SIGNATURE = "signature";
/*      */   public static final String SIGNED_CERT = "signed_cert";
/*      */   public static final String SUBJECT_DN = "x509.info.subject.dname";
/*      */   public static final String ISSUER_DN = "x509.info.issuer.dname";
/*      */   public static final String SERIAL_ID = "x509.info.serialNumber.number";
/*      */   public static final String PUBLIC_KEY = "x509.info.key.value";
/*      */   public static final String VERSION = "x509.info.version.number";
/*      */   public static final String SIG_ALG = "x509.algorithm";
/*      */   public static final String SIG = "x509.signature";
/*  127 */   private boolean readOnly = false;
/*      */ 
/*  130 */   private byte[] signedCert = null;
/*  131 */   protected X509CertInfo info = null;
/*  132 */   protected AlgorithmId algId = null;
/*  133 */   protected byte[] signature = null;
/*      */   private static final String KEY_USAGE_OID = "2.5.29.15";
/*      */   private static final String EXTENDED_KEY_USAGE_OID = "2.5.29.37";
/*      */   private static final String BASIC_CONSTRAINT_OID = "2.5.29.19";
/*      */   private static final String SUBJECT_ALT_NAME_OID = "2.5.29.17";
/*      */   private static final String ISSUER_ALT_NAME_OID = "2.5.29.18";
/*      */   private static final String AUTH_INFO_ACCESS_OID = "1.3.6.1.5.5.7.1.1";
/*      */   private static final int NUM_STANDARD_KEY_USAGE = 9;
/*      */   private Collection<List<?>> subjectAlternativeNames;
/*      */   private Collection<List<?>> issuerAlternativeNames;
/*      */   private List<String> extKeyUsage;
/*      */   private Set<AccessDescription> authInfoAccess;
/*      */   private PublicKey verifiedPublicKey;
/*      */   private String verifiedProvider;
/*      */   private boolean verificationResult;
/*      */ 
/*      */   public X509CertImpl()
/*      */   {
/*      */   }
/*      */ 
/*      */   public X509CertImpl(byte[] paramArrayOfByte)
/*      */     throws CertificateException
/*      */   {
/*      */     try
/*      */     {
/*  196 */       parse(new DerValue(paramArrayOfByte));
/*      */     } catch (IOException localIOException) {
/*  198 */       this.signedCert = null;
/*  199 */       throw new CertificateException("Unable to initialize, " + localIOException, localIOException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public X509CertImpl(InputStream paramInputStream)
/*      */     throws CertificateException
/*      */   {
/*  216 */     DerValue localDerValue = null;
/*      */ 
/*  218 */     BufferedInputStream localBufferedInputStream = new BufferedInputStream(paramInputStream);
/*      */     try
/*      */     {
/*  223 */       localBufferedInputStream.mark(2147483647);
/*  224 */       localDerValue = readRFC1421Cert(localBufferedInputStream);
/*      */     }
/*      */     catch (IOException localIOException1) {
/*      */       try {
/*  228 */         localBufferedInputStream.reset();
/*  229 */         localDerValue = new DerValue(localBufferedInputStream);
/*      */       } catch (IOException localIOException3) {
/*  231 */         throw new CertificateException("Input stream must be either DER-encoded bytes or RFC1421 hex-encoded DER-encoded bytes: " + localIOException3.getMessage(), localIOException3);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/*  239 */       parse(localDerValue);
/*      */     } catch (IOException localIOException2) {
/*  241 */       this.signedCert = null;
/*  242 */       throw new CertificateException("Unable to parse DER value of certificate, " + localIOException2, localIOException2);
/*      */     }
/*      */   }
/*      */ 
/*      */   private DerValue readRFC1421Cert(InputStream paramInputStream)
/*      */     throws IOException
/*      */   {
/*  256 */     DerValue localDerValue = null;
/*  257 */     String str = null;
/*  258 */     BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(paramInputStream, "ASCII"));
/*      */     try
/*      */     {
/*  261 */       str = localBufferedReader.readLine();
/*      */     } catch (IOException localIOException1) {
/*  263 */       throw new IOException("Unable to read InputStream: " + localIOException1.getMessage());
/*      */     }
/*      */ 
/*  266 */     if (str.equals("-----BEGIN CERTIFICATE-----"))
/*      */     {
/*  268 */       BASE64Decoder localBASE64Decoder = new BASE64Decoder();
/*  269 */       ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
/*      */       try {
/*  271 */         while ((str = localBufferedReader.readLine()) != null) {
/*  272 */           if (str.equals("-----END CERTIFICATE-----")) {
/*  273 */             localDerValue = new DerValue(localByteArrayOutputStream.toByteArray());
/*  274 */             break;
/*      */           }
/*  276 */           localByteArrayOutputStream.write(localBASE64Decoder.decodeBuffer(str));
/*      */         }
/*      */       }
/*      */       catch (IOException localIOException2) {
/*  280 */         throw new IOException("Unable to read InputStream: " + localIOException2.getMessage());
/*      */       }
/*      */     }
/*      */     else {
/*  284 */       throw new IOException("InputStream is not RFC1421 hex-encoded DER bytes");
/*      */     }
/*      */ 
/*  287 */     return localDerValue;
/*      */   }
/*      */ 
/*      */   public X509CertImpl(X509CertInfo paramX509CertInfo)
/*      */   {
/*  298 */     this.info = paramX509CertInfo;
/*      */   }
/*      */ 
/*      */   public X509CertImpl(DerValue paramDerValue)
/*      */     throws CertificateException
/*      */   {
/*      */     try
/*      */     {
/*  311 */       parse(paramDerValue);
/*      */     } catch (IOException localIOException) {
/*  313 */       this.signedCert = null;
/*  314 */       throw new CertificateException("Unable to initialize, " + localIOException, localIOException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void encode(OutputStream paramOutputStream)
/*      */     throws CertificateEncodingException
/*      */   {
/*  326 */     if (this.signedCert == null)
/*  327 */       throw new CertificateEncodingException("Null certificate to encode");
/*      */     try
/*      */     {
/*  330 */       paramOutputStream.write((byte[])this.signedCert.clone());
/*      */     } catch (IOException localIOException) {
/*  332 */       throw new CertificateEncodingException(localIOException.toString());
/*      */     }
/*      */   }
/*      */ 
/*      */   public void derEncode(OutputStream paramOutputStream)
/*      */     throws IOException
/*      */   {
/*  345 */     if (this.signedCert == null)
/*  346 */       throw new IOException("Null certificate to encode");
/*  347 */     paramOutputStream.write((byte[])this.signedCert.clone());
/*      */   }
/*      */ 
/*      */   public byte[] getEncoded()
/*      */     throws CertificateEncodingException
/*      */   {
/*  359 */     return (byte[])getEncodedInternal().clone();
/*      */   }
/*      */ 
/*      */   public byte[] getEncodedInternal()
/*      */     throws CertificateEncodingException
/*      */   {
/*  368 */     if (this.signedCert == null) {
/*  369 */       throw new CertificateEncodingException("Null certificate to encode");
/*      */     }
/*      */ 
/*  372 */     return this.signedCert;
/*      */   }
/*      */ 
/*      */   public void verify(PublicKey paramPublicKey)
/*      */     throws CertificateException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, SignatureException
/*      */   {
/*  394 */     verify(paramPublicKey, "");
/*      */   }
/*      */ 
/*      */   public synchronized void verify(PublicKey paramPublicKey, String paramString)
/*      */     throws CertificateException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, SignatureException
/*      */   {
/*  416 */     if (paramString == null) {
/*  417 */       paramString = "";
/*      */     }
/*  419 */     if ((this.verifiedPublicKey != null) && (this.verifiedPublicKey.equals(paramPublicKey)))
/*      */     {
/*  422 */       if (paramString.equals(this.verifiedProvider)) {
/*  423 */         if (this.verificationResult) {
/*  424 */           return;
/*      */         }
/*  426 */         throw new SignatureException("Signature does not match.");
/*      */       }
/*      */     }
/*      */ 
/*  430 */     if (this.signedCert == null) {
/*  431 */       throw new CertificateEncodingException("Uninitialized certificate");
/*      */     }
/*      */ 
/*  434 */     Signature localSignature = null;
/*  435 */     if (paramString.length() == 0)
/*  436 */       localSignature = Signature.getInstance(this.algId.getName());
/*      */     else {
/*  438 */       localSignature = Signature.getInstance(this.algId.getName(), paramString);
/*      */     }
/*  440 */     localSignature.initVerify(paramPublicKey);
/*      */ 
/*  442 */     byte[] arrayOfByte = this.info.getEncodedInfo();
/*  443 */     localSignature.update(arrayOfByte, 0, arrayOfByte.length);
/*      */ 
/*  446 */     this.verificationResult = localSignature.verify(this.signature);
/*  447 */     this.verifiedPublicKey = paramPublicKey;
/*  448 */     this.verifiedProvider = paramString;
/*      */ 
/*  450 */     if (!this.verificationResult)
/*  451 */       throw new SignatureException("Signature does not match.");
/*      */   }
/*      */ 
/*      */   public void sign(PrivateKey paramPrivateKey, String paramString)
/*      */     throws CertificateException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, SignatureException
/*      */   {
/*  474 */     sign(paramPrivateKey, paramString, null);
/*      */   }
/*      */ 
/*      */   public void sign(PrivateKey paramPrivateKey, String paramString1, String paramString2)
/*      */     throws CertificateException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, SignatureException
/*      */   {
/*      */     try
/*      */     {
/*  498 */       if (this.readOnly) {
/*  499 */         throw new CertificateEncodingException("cannot over-write existing certificate");
/*      */       }
/*  501 */       Signature localSignature = null;
/*  502 */       if ((paramString2 == null) || (paramString2.length() == 0))
/*  503 */         localSignature = Signature.getInstance(paramString1);
/*      */       else {
/*  505 */         localSignature = Signature.getInstance(paramString1, paramString2);
/*      */       }
/*  507 */       localSignature.initSign(paramPrivateKey);
/*      */ 
/*  510 */       this.algId = AlgorithmId.get(localSignature.getAlgorithm());
/*      */ 
/*  512 */       DerOutputStream localDerOutputStream1 = new DerOutputStream();
/*  513 */       DerOutputStream localDerOutputStream2 = new DerOutputStream();
/*      */ 
/*  516 */       this.info.encode(localDerOutputStream2);
/*  517 */       byte[] arrayOfByte = localDerOutputStream2.toByteArray();
/*      */ 
/*  520 */       this.algId.encode(localDerOutputStream2);
/*      */ 
/*  523 */       localSignature.update(arrayOfByte, 0, arrayOfByte.length);
/*  524 */       this.signature = localSignature.sign();
/*  525 */       localDerOutputStream2.putBitString(this.signature);
/*      */ 
/*  528 */       localDerOutputStream1.write((byte)48, localDerOutputStream2);
/*  529 */       this.signedCert = localDerOutputStream1.toByteArray();
/*  530 */       this.readOnly = true;
/*      */     }
/*      */     catch (IOException localIOException) {
/*  533 */       throw new CertificateEncodingException(localIOException.toString());
/*      */     }
/*      */   }
/*      */ 
/*      */   public void checkValidity()
/*      */     throws CertificateExpiredException, CertificateNotYetValidException
/*      */   {
/*  547 */     Date localDate = new Date();
/*  548 */     checkValidity(localDate);
/*      */   }
/*      */ 
/*      */   public void checkValidity(Date paramDate)
/*      */     throws CertificateExpiredException, CertificateNotYetValidException
/*      */   {
/*  567 */     CertificateValidity localCertificateValidity = null;
/*      */     try {
/*  569 */       localCertificateValidity = (CertificateValidity)this.info.get("validity");
/*      */     } catch (Exception localException) {
/*  571 */       throw new CertificateNotYetValidException("Incorrect validity period");
/*      */     }
/*  573 */     if (localCertificateValidity == null)
/*  574 */       throw new CertificateNotYetValidException("Null validity period");
/*  575 */     localCertificateValidity.valid(paramDate);
/*      */   }
/*      */ 
/*      */   public Object get(String paramString)
/*      */     throws CertificateParsingException
/*      */   {
/*  590 */     X509AttributeName localX509AttributeName = new X509AttributeName(paramString);
/*  591 */     String str = localX509AttributeName.getPrefix();
/*  592 */     if (!str.equalsIgnoreCase("x509")) {
/*  593 */       throw new CertificateParsingException("Invalid root of attribute name, expected [x509], received [" + str + "]");
/*      */     }
/*      */ 
/*  597 */     localX509AttributeName = new X509AttributeName(localX509AttributeName.getSuffix());
/*  598 */     str = localX509AttributeName.getPrefix();
/*      */ 
/*  600 */     if (str.equalsIgnoreCase("info")) {
/*  601 */       if (this.info == null) {
/*  602 */         return null;
/*      */       }
/*  604 */       if (localX509AttributeName.getSuffix() != null) {
/*      */         try {
/*  606 */           return this.info.get(localX509AttributeName.getSuffix());
/*      */         } catch (IOException localIOException) {
/*  608 */           throw new CertificateParsingException(localIOException.toString());
/*      */         } catch (CertificateException localCertificateException) {
/*  610 */           throw new CertificateParsingException(localCertificateException.toString());
/*      */         }
/*      */       }
/*  613 */       return this.info;
/*      */     }
/*  615 */     if (str.equalsIgnoreCase("algorithm"))
/*  616 */       return this.algId;
/*  617 */     if (str.equalsIgnoreCase("signature")) {
/*  618 */       if (this.signature != null) {
/*  619 */         return this.signature.clone();
/*      */       }
/*  621 */       return null;
/*  622 */     }if (str.equalsIgnoreCase("signed_cert")) {
/*  623 */       if (this.signedCert != null) {
/*  624 */         return this.signedCert.clone();
/*      */       }
/*  626 */       return null;
/*      */     }
/*  628 */     throw new CertificateParsingException("Attribute name not recognized or get() not allowed for the same: " + str);
/*      */   }
/*      */ 
/*      */   public void set(String paramString, Object paramObject)
/*      */     throws CertificateException, IOException
/*      */   {
/*  644 */     if (this.readOnly) {
/*  645 */       throw new CertificateException("cannot over-write existing certificate");
/*      */     }
/*      */ 
/*  648 */     X509AttributeName localX509AttributeName = new X509AttributeName(paramString);
/*  649 */     String str = localX509AttributeName.getPrefix();
/*  650 */     if (!str.equalsIgnoreCase("x509")) {
/*  651 */       throw new CertificateException("Invalid root of attribute name, expected [x509], received " + str);
/*      */     }
/*      */ 
/*  654 */     localX509AttributeName = new X509AttributeName(localX509AttributeName.getSuffix());
/*  655 */     str = localX509AttributeName.getPrefix();
/*      */ 
/*  657 */     if (str.equalsIgnoreCase("info")) {
/*  658 */       if (localX509AttributeName.getSuffix() == null) {
/*  659 */         if (!(paramObject instanceof X509CertInfo)) {
/*  660 */           throw new CertificateException("Attribute value should be of type X509CertInfo.");
/*      */         }
/*      */ 
/*  663 */         this.info = ((X509CertInfo)paramObject);
/*  664 */         this.signedCert = null;
/*      */       } else {
/*  666 */         this.info.set(localX509AttributeName.getSuffix(), paramObject);
/*  667 */         this.signedCert = null;
/*      */       }
/*      */     }
/*  670 */     else throw new CertificateException("Attribute name not recognized or set() not allowed for the same: " + str);
/*      */   }
/*      */ 
/*      */   public void delete(String paramString)
/*      */     throws CertificateException, IOException
/*      */   {
/*  685 */     if (this.readOnly) {
/*  686 */       throw new CertificateException("cannot over-write existing certificate");
/*      */     }
/*      */ 
/*  689 */     X509AttributeName localX509AttributeName = new X509AttributeName(paramString);
/*  690 */     String str = localX509AttributeName.getPrefix();
/*  691 */     if (!str.equalsIgnoreCase("x509")) {
/*  692 */       throw new CertificateException("Invalid root of attribute name, expected [x509], received " + str);
/*      */     }
/*      */ 
/*  696 */     localX509AttributeName = new X509AttributeName(localX509AttributeName.getSuffix());
/*  697 */     str = localX509AttributeName.getPrefix();
/*      */ 
/*  699 */     if (str.equalsIgnoreCase("info")) {
/*  700 */       if (localX509AttributeName.getSuffix() != null)
/*  701 */         this.info = null;
/*      */       else
/*  703 */         this.info.delete(localX509AttributeName.getSuffix());
/*      */     }
/*  705 */     else if (str.equalsIgnoreCase("algorithm"))
/*  706 */       this.algId = null;
/*  707 */     else if (str.equalsIgnoreCase("signature"))
/*  708 */       this.signature = null;
/*  709 */     else if (str.equalsIgnoreCase("signed_cert"))
/*  710 */       this.signedCert = null;
/*      */     else
/*  712 */       throw new CertificateException("Attribute name not recognized or delete() not allowed for the same: " + str);
/*      */   }
/*      */ 
/*      */   public Enumeration<String> getElements()
/*      */   {
/*  722 */     AttributeNameEnumeration localAttributeNameEnumeration = new AttributeNameEnumeration();
/*  723 */     localAttributeNameEnumeration.addElement("x509.info");
/*  724 */     localAttributeNameEnumeration.addElement("x509.algorithm");
/*  725 */     localAttributeNameEnumeration.addElement("x509.signature");
/*  726 */     localAttributeNameEnumeration.addElement("x509.signed_cert");
/*      */ 
/*  728 */     return localAttributeNameEnumeration.elements();
/*      */   }
/*      */ 
/*      */   public String getName()
/*      */   {
/*  735 */     return "x509";
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/*  745 */     if ((this.info == null) || (this.algId == null) || (this.signature == null)) {
/*  746 */       return "";
/*      */     }
/*  748 */     StringBuilder localStringBuilder = new StringBuilder();
/*      */ 
/*  750 */     localStringBuilder.append("[\n");
/*  751 */     localStringBuilder.append(this.info.toString() + "\n");
/*  752 */     localStringBuilder.append("  Algorithm: [" + this.algId.toString() + "]\n");
/*      */ 
/*  754 */     HexDumpEncoder localHexDumpEncoder = new HexDumpEncoder();
/*  755 */     localStringBuilder.append("  Signature:\n" + localHexDumpEncoder.encodeBuffer(this.signature));
/*  756 */     localStringBuilder.append("\n]");
/*      */ 
/*  758 */     return localStringBuilder.toString();
/*      */   }
/*      */ 
/*      */   public PublicKey getPublicKey()
/*      */   {
/*  769 */     if (this.info == null)
/*  770 */       return null;
/*      */     try {
/*  772 */       return (PublicKey)this.info.get("key.value");
/*      */     }
/*      */     catch (Exception localException) {
/*      */     }
/*  776 */     return null;
/*      */   }
/*      */ 
/*      */   public int getVersion()
/*      */   {
/*  786 */     if (this.info == null)
/*  787 */       return -1;
/*      */     try {
/*  789 */       int i = ((Integer)this.info.get("version.number")).intValue();
/*      */ 
/*  791 */       return i + 1; } catch (Exception localException) {
/*      */     }
/*  793 */     return -1;
/*      */   }
/*      */ 
/*      */   public BigInteger getSerialNumber()
/*      */   {
/*  803 */     SerialNumber localSerialNumber = getSerialNumberObject();
/*      */ 
/*  805 */     return localSerialNumber != null ? localSerialNumber.getNumber() : null;
/*      */   }
/*      */ 
/*      */   public SerialNumber getSerialNumberObject()
/*      */   {
/*  815 */     if (this.info == null)
/*  816 */       return null;
/*      */     try {
/*  818 */       return (SerialNumber)this.info.get("serialNumber.number");
/*      */     }
/*      */     catch (Exception localException)
/*      */     {
/*      */     }
/*  823 */     return null;
/*      */   }
/*      */ 
/*      */   public Principal getSubjectDN()
/*      */   {
/*  834 */     if (this.info == null)
/*  835 */       return null;
/*      */     try {
/*  837 */       return (Principal)this.info.get("subject.dname");
/*      */     }
/*      */     catch (Exception localException)
/*      */     {
/*      */     }
/*  842 */     return null;
/*      */   }
/*      */ 
/*      */   public X500Principal getSubjectX500Principal()
/*      */   {
/*  852 */     if (this.info == null)
/*  853 */       return null;
/*      */     try
/*      */     {
/*  856 */       return (X500Principal)this.info.get("subject.x500principal");
/*      */     }
/*      */     catch (Exception localException)
/*      */     {
/*      */     }
/*  861 */     return null;
/*      */   }
/*      */ 
/*      */   public Principal getIssuerDN()
/*      */   {
/*  871 */     if (this.info == null)
/*  872 */       return null;
/*      */     try {
/*  874 */       return (Principal)this.info.get("issuer.dname");
/*      */     }
/*      */     catch (Exception localException)
/*      */     {
/*      */     }
/*  879 */     return null;
/*      */   }
/*      */ 
/*      */   public X500Principal getIssuerX500Principal()
/*      */   {
/*  889 */     if (this.info == null)
/*  890 */       return null;
/*      */     try
/*      */     {
/*  893 */       return (X500Principal)this.info.get("issuer.x500principal");
/*      */     }
/*      */     catch (Exception localException)
/*      */     {
/*      */     }
/*  898 */     return null;
/*      */   }
/*      */ 
/*      */   public Date getNotBefore()
/*      */   {
/*  908 */     if (this.info == null)
/*  909 */       return null;
/*      */     try {
/*  911 */       return (Date)this.info.get("validity.notBefore");
/*      */     }
/*      */     catch (Exception localException) {
/*      */     }
/*  915 */     return null;
/*      */   }
/*      */ 
/*      */   public Date getNotAfter()
/*      */   {
/*  925 */     if (this.info == null)
/*  926 */       return null;
/*      */     try {
/*  928 */       return (Date)this.info.get("validity.notAfter");
/*      */     }
/*      */     catch (Exception localException) {
/*      */     }
/*  932 */     return null;
/*      */   }
/*      */ 
/*      */   public byte[] getTBSCertificate()
/*      */     throws CertificateEncodingException
/*      */   {
/*  945 */     if (this.info != null) {
/*  946 */       return this.info.getEncodedInfo();
/*      */     }
/*  948 */     throw new CertificateEncodingException("Uninitialized certificate");
/*      */   }
/*      */ 
/*      */   public byte[] getSignature()
/*      */   {
/*  957 */     if (this.signature == null)
/*  958 */       return null;
/*  959 */     byte[] arrayOfByte = new byte[this.signature.length];
/*  960 */     System.arraycopy(this.signature, 0, arrayOfByte, 0, arrayOfByte.length);
/*  961 */     return arrayOfByte;
/*      */   }
/*      */ 
/*      */   public String getSigAlgName()
/*      */   {
/*  972 */     if (this.algId == null)
/*  973 */       return null;
/*  974 */     return this.algId.getName();
/*      */   }
/*      */ 
/*      */   public String getSigAlgOID()
/*      */   {
/*  984 */     if (this.algId == null)
/*  985 */       return null;
/*  986 */     ObjectIdentifier localObjectIdentifier = this.algId.getOID();
/*  987 */     return localObjectIdentifier.toString();
/*      */   }
/*      */ 
/*      */   public byte[] getSigAlgParams()
/*      */   {
/*  998 */     if (this.algId == null)
/*  999 */       return null;
/*      */     try {
/* 1001 */       return this.algId.getEncodedParams(); } catch (IOException localIOException) {
/*      */     }
/* 1003 */     return null;
/*      */   }
/*      */ 
/*      */   public boolean[] getIssuerUniqueID()
/*      */   {
/* 1013 */     if (this.info == null)
/* 1014 */       return null;
/*      */     try {
/* 1016 */       UniqueIdentity localUniqueIdentity = (UniqueIdentity)this.info.get("issuerID.id");
/*      */ 
/* 1019 */       if (localUniqueIdentity == null) {
/* 1020 */         return null;
/*      */       }
/* 1022 */       return localUniqueIdentity.getId(); } catch (Exception localException) {
/*      */     }
/* 1024 */     return null;
/*      */   }
/*      */ 
/*      */   public boolean[] getSubjectUniqueID()
/*      */   {
/* 1034 */     if (this.info == null)
/* 1035 */       return null;
/*      */     try {
/* 1037 */       UniqueIdentity localUniqueIdentity = (UniqueIdentity)this.info.get("subjectID.id");
/*      */ 
/* 1040 */       if (localUniqueIdentity == null) {
/* 1041 */         return null;
/*      */       }
/* 1043 */       return localUniqueIdentity.getId(); } catch (Exception localException) {
/*      */     }
/* 1045 */     return null;
/*      */   }
/*      */ 
/*      */   public AuthorityKeyIdentifierExtension getAuthorityKeyIdentifierExtension()
/*      */   {
/* 1056 */     return (AuthorityKeyIdentifierExtension)getExtension(PKIXExtensions.AuthorityKey_Id);
/*      */   }
/*      */ 
/*      */   public byte[] getIssuerKeyIdentifier()
/*      */   {
/* 1065 */     byte[] arrayOfByte = null;
/* 1066 */     AuthorityKeyIdentifierExtension localAuthorityKeyIdentifierExtension = getAuthorityKeyIdentifierExtension();
/*      */ 
/* 1069 */     if (localAuthorityKeyIdentifierExtension != null) {
/*      */       try
/*      */       {
/* 1072 */         KeyIdentifier localKeyIdentifier = (KeyIdentifier)localAuthorityKeyIdentifierExtension.get("key_id");
/*      */ 
/* 1075 */         if (localKeyIdentifier != null) {
/* 1076 */           arrayOfByte = localKeyIdentifier.getIdentifier();
/*      */         }
/*      */       }
/*      */       catch (IOException localIOException)
/*      */       {
/*      */       }
/*      */     }
/* 1083 */     return arrayOfByte;
/*      */   }
/*      */ 
/*      */   public BasicConstraintsExtension getBasicConstraintsExtension()
/*      */   {
/* 1092 */     return (BasicConstraintsExtension)getExtension(PKIXExtensions.BasicConstraints_Id);
/*      */   }
/*      */ 
/*      */   public CertificatePoliciesExtension getCertificatePoliciesExtension()
/*      */   {
/* 1102 */     return (CertificatePoliciesExtension)getExtension(PKIXExtensions.CertificatePolicies_Id);
/*      */   }
/*      */ 
/*      */   public ExtendedKeyUsageExtension getExtendedKeyUsageExtension()
/*      */   {
/* 1112 */     return (ExtendedKeyUsageExtension)getExtension(PKIXExtensions.ExtendedKeyUsage_Id);
/*      */   }
/*      */ 
/*      */   public IssuerAlternativeNameExtension getIssuerAlternativeNameExtension()
/*      */   {
/* 1122 */     return (IssuerAlternativeNameExtension)getExtension(PKIXExtensions.IssuerAlternativeName_Id);
/*      */   }
/*      */ 
/*      */   public NameConstraintsExtension getNameConstraintsExtension()
/*      */   {
/* 1131 */     return (NameConstraintsExtension)getExtension(PKIXExtensions.NameConstraints_Id);
/*      */   }
/*      */ 
/*      */   public PolicyConstraintsExtension getPolicyConstraintsExtension()
/*      */   {
/* 1141 */     return (PolicyConstraintsExtension)getExtension(PKIXExtensions.PolicyConstraints_Id);
/*      */   }
/*      */ 
/*      */   public PolicyMappingsExtension getPolicyMappingsExtension()
/*      */   {
/* 1151 */     return (PolicyMappingsExtension)getExtension(PKIXExtensions.PolicyMappings_Id);
/*      */   }
/*      */ 
/*      */   public PrivateKeyUsageExtension getPrivateKeyUsageExtension()
/*      */   {
/* 1160 */     return (PrivateKeyUsageExtension)getExtension(PKIXExtensions.PrivateKeyUsage_Id);
/*      */   }
/*      */ 
/*      */   public SubjectAlternativeNameExtension getSubjectAlternativeNameExtension()
/*      */   {
/* 1171 */     return (SubjectAlternativeNameExtension)getExtension(PKIXExtensions.SubjectAlternativeName_Id);
/*      */   }
/*      */ 
/*      */   public SubjectKeyIdentifierExtension getSubjectKeyIdentifierExtension()
/*      */   {
/* 1181 */     return (SubjectKeyIdentifierExtension)getExtension(PKIXExtensions.SubjectKey_Id);
/*      */   }
/*      */ 
/*      */   public byte[] getSubjectKeyIdentifier()
/*      */   {
/* 1190 */     byte[] arrayOfByte = null;
/* 1191 */     SubjectKeyIdentifierExtension localSubjectKeyIdentifierExtension = getSubjectKeyIdentifierExtension();
/*      */ 
/* 1193 */     if (localSubjectKeyIdentifierExtension != null) {
/*      */       try
/*      */       {
/* 1196 */         KeyIdentifier localKeyIdentifier = (KeyIdentifier)localSubjectKeyIdentifierExtension.get("key_id");
/*      */ 
/* 1199 */         if (localKeyIdentifier != null) {
/* 1200 */           arrayOfByte = localKeyIdentifier.getIdentifier();
/*      */         }
/*      */       }
/*      */       catch (IOException localIOException)
/*      */       {
/*      */       }
/*      */     }
/* 1207 */     return arrayOfByte;
/*      */   }
/*      */ 
/*      */   public CRLDistributionPointsExtension getCRLDistributionPointsExtension()
/*      */   {
/* 1216 */     return (CRLDistributionPointsExtension)getExtension(PKIXExtensions.CRLDistributionPoints_Id);
/*      */   }
/*      */ 
/*      */   public boolean hasUnsupportedCriticalExtension()
/*      */   {
/* 1225 */     if (this.info == null)
/* 1226 */       return false;
/*      */     try {
/* 1228 */       CertificateExtensions localCertificateExtensions = (CertificateExtensions)this.info.get("extensions");
/*      */ 
/* 1230 */       if (localCertificateExtensions == null)
/* 1231 */         return false;
/* 1232 */       return localCertificateExtensions.hasUnsupportedCriticalExtension(); } catch (Exception localException) {
/*      */     }
/* 1234 */     return false;
/*      */   }
/*      */ 
/*      */   public Set<String> getCriticalExtensionOIDs()
/*      */   {
/* 1247 */     if (this.info == null)
/* 1248 */       return null;
/*      */     try
/*      */     {
/* 1251 */       CertificateExtensions localCertificateExtensions = (CertificateExtensions)this.info.get("extensions");
/*      */ 
/* 1253 */       if (localCertificateExtensions == null) {
/* 1254 */         return null;
/*      */       }
/* 1256 */       TreeSet localTreeSet = new TreeSet();
/* 1257 */       for (Extension localExtension : localCertificateExtensions.getAllExtensions()) {
/* 1258 */         if (localExtension.isCritical()) {
/* 1259 */           localTreeSet.add(localExtension.getExtensionId().toString());
/*      */         }
/*      */       }
/* 1262 */       return localTreeSet; } catch (Exception localException) {
/*      */     }
/* 1264 */     return null;
/*      */   }
/*      */ 
/*      */   public Set<String> getNonCriticalExtensionOIDs()
/*      */   {
/* 1277 */     if (this.info == null)
/* 1278 */       return null;
/*      */     try
/*      */     {
/* 1281 */       CertificateExtensions localCertificateExtensions = (CertificateExtensions)this.info.get("extensions");
/*      */ 
/* 1283 */       if (localCertificateExtensions == null) {
/* 1284 */         return null;
/*      */       }
/* 1286 */       TreeSet localTreeSet = new TreeSet();
/* 1287 */       for (Extension localExtension : localCertificateExtensions.getAllExtensions()) {
/* 1288 */         if (!localExtension.isCritical()) {
/* 1289 */           localTreeSet.add(localExtension.getExtensionId().toString());
/*      */         }
/*      */       }
/* 1292 */       localTreeSet.addAll(localCertificateExtensions.getUnparseableExtensions().keySet());
/* 1293 */       return localTreeSet; } catch (Exception localException) {
/*      */     }
/* 1295 */     return null;
/*      */   }
/*      */ 
/*      */   public Extension getExtension(ObjectIdentifier paramObjectIdentifier)
/*      */   {
/* 1307 */     if (this.info == null)
/* 1308 */       return null;
/*      */     try
/*      */     {
/*      */       CertificateExtensions localCertificateExtensions;
/*      */       try {
/* 1313 */         localCertificateExtensions = (CertificateExtensions)this.info.get("extensions");
/*      */       } catch (CertificateException localCertificateException) {
/* 1315 */         return null;
/*      */       }
/* 1317 */       if (localCertificateExtensions == null) {
/* 1318 */         return null;
/*      */       }
/* 1320 */       Extension localExtension1 = localCertificateExtensions.getExtension(paramObjectIdentifier.toString());
/* 1321 */       if (localExtension1 != null) {
/* 1322 */         return localExtension1;
/*      */       }
/* 1324 */       for (Extension localExtension2 : localCertificateExtensions.getAllExtensions()) {
/* 1325 */         if (localExtension2.getExtensionId().equals(paramObjectIdentifier))
/*      */         {
/* 1327 */           return localExtension2;
/*      */         }
/*      */       }
/*      */ 
/* 1331 */       return null;
/*      */     } catch (IOException localIOException) {
/*      */     }
/* 1334 */     return null;
/*      */   }
/*      */ 
/*      */   public Extension getUnparseableExtension(ObjectIdentifier paramObjectIdentifier)
/*      */   {
/* 1339 */     if (this.info == null)
/* 1340 */       return null;
/*      */     try
/*      */     {
/*      */       CertificateExtensions localCertificateExtensions;
/*      */       try {
/* 1345 */         localCertificateExtensions = (CertificateExtensions)this.info.get("extensions");
/*      */       } catch (CertificateException localCertificateException) {
/* 1347 */         return null;
/*      */       }
/* 1349 */       if (localCertificateExtensions == null) {
/* 1350 */         return null;
/*      */       }
/* 1352 */       return (Extension)localCertificateExtensions.getUnparseableExtensions().get(paramObjectIdentifier.toString());
/*      */     } catch (IOException localIOException) {
/*      */     }
/* 1355 */     return null;
/*      */   }
/*      */ 
/*      */   public byte[] getExtensionValue(String paramString)
/*      */   {
/*      */     try
/*      */     {
/* 1367 */       ObjectIdentifier localObjectIdentifier1 = new ObjectIdentifier(paramString);
/* 1368 */       String str = OIDMap.getName(localObjectIdentifier1);
/* 1369 */       Object localObject1 = null;
/* 1370 */       CertificateExtensions localCertificateExtensions = (CertificateExtensions)this.info.get("extensions");
/*      */       Iterator localIterator;
/* 1373 */       if (str == null)
/*      */       {
/* 1375 */         if (localCertificateExtensions == null) {
/* 1376 */           return null;
/*      */         }
/*      */ 
/* 1379 */         for (localIterator = localCertificateExtensions.getAllExtensions().iterator(); localIterator.hasNext(); ) { localObject2 = (Extension)localIterator.next();
/* 1380 */           ObjectIdentifier localObjectIdentifier2 = ((Extension)localObject2).getExtensionId();
/* 1381 */           if (localObjectIdentifier2.equals(localObjectIdentifier1)) {
/* 1382 */             localObject1 = localObject2;
/* 1383 */             break;
/*      */           } }
/*      */       }
/*      */       else {
/*      */         try {
/* 1388 */           localObject1 = (Extension)get(str);
/*      */         }
/*      */         catch (CertificateException localCertificateException) {
/*      */         }
/*      */       }
/* 1393 */       if (localObject1 == null) {
/* 1394 */         if (localCertificateExtensions != null) {
/* 1395 */           localObject1 = (Extension)localCertificateExtensions.getUnparseableExtensions().get(paramString);
/*      */         }
/* 1397 */         if (localObject1 == null) {
/* 1398 */           return null;
/*      */         }
/*      */       }
/* 1401 */       byte[] arrayOfByte = ((Extension)localObject1).getExtensionValue();
/* 1402 */       if (arrayOfByte == null) {
/* 1403 */         return null;
/*      */       }
/* 1405 */       Object localObject2 = new DerOutputStream();
/* 1406 */       ((DerOutputStream)localObject2).putOctetString(arrayOfByte);
/* 1407 */       return ((DerOutputStream)localObject2).toByteArray(); } catch (Exception localException) {
/*      */     }
/* 1409 */     return null;
/*      */   }
/*      */ 
/*      */   public boolean[] getKeyUsage()
/*      */   {
/*      */     try
/*      */     {
/* 1420 */       String str = OIDMap.getName(PKIXExtensions.KeyUsage_Id);
/* 1421 */       if (str == null) {
/* 1422 */         return null;
/*      */       }
/* 1424 */       KeyUsageExtension localKeyUsageExtension = (KeyUsageExtension)get(str);
/* 1425 */       if (localKeyUsageExtension == null) {
/* 1426 */         return null;
/*      */       }
/* 1428 */       Object localObject = localKeyUsageExtension.getBits();
/*      */       boolean[] arrayOfBoolean;
/* 1429 */       if (localObject.length < 9) {
/* 1430 */         arrayOfBoolean = new boolean[9];
/* 1431 */         System.arraycopy(localObject, 0, arrayOfBoolean, 0, localObject.length);
/* 1432 */       }return arrayOfBoolean;
/*      */     }
/*      */     catch (Exception localException) {
/*      */     }
/* 1436 */     return null;
/*      */   }
/*      */ 
/*      */   public synchronized List<String> getExtendedKeyUsage()
/*      */     throws CertificateParsingException
/*      */   {
/* 1448 */     if ((this.readOnly) && (this.extKeyUsage != null)) {
/* 1449 */       return this.extKeyUsage;
/*      */     }
/* 1451 */     ExtendedKeyUsageExtension localExtendedKeyUsageExtension = getExtendedKeyUsageExtension();
/* 1452 */     if (localExtendedKeyUsageExtension == null) {
/* 1453 */       return null;
/*      */     }
/* 1455 */     this.extKeyUsage = Collections.unmodifiableList(localExtendedKeyUsageExtension.getExtendedKeyUsage());
/*      */ 
/* 1457 */     return this.extKeyUsage;
/*      */   }
/*      */ 
/*      */   public static List<String> getExtendedKeyUsage(X509Certificate paramX509Certificate)
/*      */     throws CertificateParsingException
/*      */   {
/*      */     try
/*      */     {
/* 1470 */       byte[] arrayOfByte1 = paramX509Certificate.getExtensionValue("2.5.29.37");
/* 1471 */       if (arrayOfByte1 == null)
/* 1472 */         return null;
/* 1473 */       DerValue localDerValue = new DerValue(arrayOfByte1);
/* 1474 */       byte[] arrayOfByte2 = localDerValue.getOctetString();
/*      */ 
/* 1476 */       ExtendedKeyUsageExtension localExtendedKeyUsageExtension = new ExtendedKeyUsageExtension(Boolean.FALSE, arrayOfByte2);
/*      */ 
/* 1478 */       return Collections.unmodifiableList(localExtendedKeyUsageExtension.getExtendedKeyUsage());
/*      */     } catch (IOException localIOException) {
/* 1480 */       throw new CertificateParsingException(localIOException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public int getBasicConstraints()
/*      */   {
/*      */     try
/*      */     {
/* 1491 */       String str = OIDMap.getName(PKIXExtensions.BasicConstraints_Id);
/* 1492 */       if (str == null)
/* 1493 */         return -1;
/* 1494 */       BasicConstraintsExtension localBasicConstraintsExtension = (BasicConstraintsExtension)get(str);
/*      */ 
/* 1496 */       if (localBasicConstraintsExtension == null) {
/* 1497 */         return -1;
/*      */       }
/* 1499 */       if (((Boolean)localBasicConstraintsExtension.get("is_ca")).booleanValue() == true)
/*      */       {
/* 1501 */         return ((Integer)localBasicConstraintsExtension.get("path_len")).intValue();
/*      */       }
/*      */ 
/* 1504 */       return -1; } catch (Exception localException) {
/*      */     }
/* 1506 */     return -1;
/*      */   }
/*      */ 
/*      */   private static Collection<List<?>> makeAltNames(GeneralNames paramGeneralNames)
/*      */   {
/* 1520 */     if (paramGeneralNames.isEmpty()) {
/* 1521 */       return Collections.emptySet();
/*      */     }
/* 1523 */     ArrayList localArrayList1 = new ArrayList();
/* 1524 */     for (GeneralName localGeneralName : paramGeneralNames.names()) {
/* 1525 */       GeneralNameInterface localGeneralNameInterface = localGeneralName.getName();
/* 1526 */       ArrayList localArrayList2 = new ArrayList(2);
/* 1527 */       localArrayList2.add(Integer.valueOf(localGeneralNameInterface.getType()));
/* 1528 */       switch (localGeneralNameInterface.getType()) {
/*      */       case 1:
/* 1530 */         localArrayList2.add(((RFC822Name)localGeneralNameInterface).getName());
/* 1531 */         break;
/*      */       case 2:
/* 1533 */         localArrayList2.add(((DNSName)localGeneralNameInterface).getName());
/* 1534 */         break;
/*      */       case 4:
/* 1536 */         localArrayList2.add(((X500Name)localGeneralNameInterface).getRFC2253Name());
/* 1537 */         break;
/*      */       case 6:
/* 1539 */         localArrayList2.add(((URIName)localGeneralNameInterface).getName());
/* 1540 */         break;
/*      */       case 7:
/*      */         try {
/* 1543 */           localArrayList2.add(((IPAddressName)localGeneralNameInterface).getName());
/*      */         }
/*      */         catch (IOException localIOException1) {
/* 1546 */           throw new RuntimeException("IPAddress cannot be parsed", localIOException1);
/*      */         }
/*      */ 
/*      */       case 8:
/* 1551 */         localArrayList2.add(((OIDName)localGeneralNameInterface).getOID().toString());
/* 1552 */         break;
/*      */       case 3:
/*      */       case 5:
/*      */       default:
/* 1555 */         DerOutputStream localDerOutputStream = new DerOutputStream();
/*      */         try {
/* 1557 */           localGeneralNameInterface.encode(localDerOutputStream);
/*      */         }
/*      */         catch (IOException localIOException2)
/*      */         {
/* 1561 */           throw new RuntimeException("name cannot be encoded", localIOException2);
/*      */         }
/* 1563 */         localArrayList2.add(localDerOutputStream.toByteArray());
/*      */       }
/*      */ 
/* 1566 */       localArrayList1.add(Collections.unmodifiableList(localArrayList2));
/*      */     }
/* 1568 */     return Collections.unmodifiableCollection(localArrayList1);
/*      */   }
/*      */ 
/*      */   private static Collection<List<?>> cloneAltNames(Collection<List<?>> paramCollection)
/*      */   {
/* 1576 */     int i = 0;
/* 1577 */     for (Object localObject1 = paramCollection.iterator(); ((Iterator)localObject1).hasNext(); ) { localObject2 = (List)((Iterator)localObject1).next();
/* 1578 */       if ((((List)localObject2).get(1) instanceof byte[]))
/*      */       {
/* 1580 */         i = 1;
/*      */       }
/*      */     }
/*      */     Object localObject2;
/* 1583 */     if (i != 0) {
/* 1584 */       localObject1 = new ArrayList();
/* 1585 */       for (localObject2 = paramCollection.iterator(); ((Iterator)localObject2).hasNext(); ) { List localList = (List)((Iterator)localObject2).next();
/* 1586 */         Object localObject3 = localList.get(1);
/* 1587 */         if ((localObject3 instanceof byte[])) {
/* 1588 */           ArrayList localArrayList = new ArrayList(localList);
/*      */ 
/* 1590 */           localArrayList.set(1, ((byte[])localObject3).clone());
/* 1591 */           ((List)localObject1).add(Collections.unmodifiableList(localArrayList));
/*      */         } else {
/* 1593 */           ((List)localObject1).add(localList);
/*      */         }
/*      */       }
/* 1596 */       return Collections.unmodifiableCollection((Collection)localObject1);
/*      */     }
/* 1598 */     return paramCollection;
/*      */   }
/*      */ 
/*      */   public synchronized Collection<List<?>> getSubjectAlternativeNames()
/*      */     throws CertificateParsingException
/*      */   {
/* 1611 */     if ((this.readOnly) && (this.subjectAlternativeNames != null)) {
/* 1612 */       return cloneAltNames(this.subjectAlternativeNames);
/*      */     }
/* 1614 */     SubjectAlternativeNameExtension localSubjectAlternativeNameExtension = getSubjectAlternativeNameExtension();
/*      */ 
/* 1616 */     if (localSubjectAlternativeNameExtension == null)
/* 1617 */       return null;
/*      */     GeneralNames localGeneralNames;
/*      */     try
/*      */     {
/* 1621 */       localGeneralNames = (GeneralNames)localSubjectAlternativeNameExtension.get("subject_name");
/*      */     }
/*      */     catch (IOException localIOException)
/*      */     {
/* 1625 */       return Collections.emptySet();
/*      */     }
/* 1627 */     this.subjectAlternativeNames = makeAltNames(localGeneralNames);
/* 1628 */     return this.subjectAlternativeNames;
/*      */   }
/*      */ 
/*      */   public static Collection<List<?>> getSubjectAlternativeNames(X509Certificate paramX509Certificate)
/*      */     throws CertificateParsingException
/*      */   {
/*      */     try
/*      */     {
/* 1640 */       byte[] arrayOfByte1 = paramX509Certificate.getExtensionValue("2.5.29.17");
/* 1641 */       if (arrayOfByte1 == null) {
/* 1642 */         return null;
/*      */       }
/* 1644 */       DerValue localDerValue = new DerValue(arrayOfByte1);
/* 1645 */       byte[] arrayOfByte2 = localDerValue.getOctetString();
/*      */ 
/* 1647 */       SubjectAlternativeNameExtension localSubjectAlternativeNameExtension = new SubjectAlternativeNameExtension(Boolean.FALSE, arrayOfByte2);
/*      */       GeneralNames localGeneralNames;
/*      */       try {
/* 1653 */         localGeneralNames = (GeneralNames)localSubjectAlternativeNameExtension.get("subject_name");
/*      */       }
/*      */       catch (IOException localIOException2)
/*      */       {
/* 1657 */         return Collections.emptySet();
/*      */       }
/* 1659 */       return makeAltNames(localGeneralNames);
/*      */     } catch (IOException localIOException1) {
/* 1661 */       throw new CertificateParsingException(localIOException1);
/*      */     }
/*      */   }
/*      */ 
/*      */   public synchronized Collection<List<?>> getIssuerAlternativeNames()
/*      */     throws CertificateParsingException
/*      */   {
/* 1674 */     if ((this.readOnly) && (this.issuerAlternativeNames != null)) {
/* 1675 */       return cloneAltNames(this.issuerAlternativeNames);
/*      */     }
/* 1677 */     IssuerAlternativeNameExtension localIssuerAlternativeNameExtension = getIssuerAlternativeNameExtension();
/*      */ 
/* 1679 */     if (localIssuerAlternativeNameExtension == null)
/* 1680 */       return null;
/*      */     GeneralNames localGeneralNames;
/*      */     try
/*      */     {
/* 1684 */       localGeneralNames = (GeneralNames)localIssuerAlternativeNameExtension.get("issuer_name");
/*      */     }
/*      */     catch (IOException localIOException)
/*      */     {
/* 1688 */       return Collections.emptySet();
/*      */     }
/* 1690 */     this.issuerAlternativeNames = makeAltNames(localGeneralNames);
/* 1691 */     return this.issuerAlternativeNames;
/*      */   }
/*      */ 
/*      */   public static Collection<List<?>> getIssuerAlternativeNames(X509Certificate paramX509Certificate)
/*      */     throws CertificateParsingException
/*      */   {
/*      */     try
/*      */     {
/* 1703 */       byte[] arrayOfByte1 = paramX509Certificate.getExtensionValue("2.5.29.18");
/* 1704 */       if (arrayOfByte1 == null) {
/* 1705 */         return null;
/* 1708 */       }
/*      */ DerValue localDerValue = new DerValue(arrayOfByte1);
/* 1709 */       byte[] arrayOfByte2 = localDerValue.getOctetString();
/*      */ 
/* 1711 */       IssuerAlternativeNameExtension localIssuerAlternativeNameExtension = new IssuerAlternativeNameExtension(Boolean.FALSE, arrayOfByte2);
/*      */       GeneralNames localGeneralNames;
/*      */       try {
/* 1716 */         localGeneralNames = (GeneralNames)localIssuerAlternativeNameExtension.get("issuer_name");
/*      */       }
/*      */       catch (IOException localIOException2)
/*      */       {
/* 1720 */         return Collections.emptySet();
/*      */       }
/* 1722 */       return makeAltNames(localGeneralNames);
/*      */     } catch (IOException localIOException1) {
/* 1724 */       throw new CertificateParsingException(localIOException1);
/*      */     }
/*      */   }
/*      */ 
/*      */   public AuthorityInfoAccessExtension getAuthorityInfoAccessExtension() {
/* 1729 */     return (AuthorityInfoAccessExtension)getExtension(PKIXExtensions.AuthInfoAccess_Id);
/*      */   }
/*      */ 
/*      */   private void parse(DerValue paramDerValue)
/*      */     throws CertificateException, IOException
/*      */   {
/* 1748 */     if (this.readOnly) {
/* 1749 */       throw new CertificateParsingException("cannot over-write existing certificate");
/*      */     }
/*      */ 
/* 1752 */     if ((paramDerValue.data == null) || (paramDerValue.tag != 48)) {
/* 1753 */       throw new CertificateParsingException("invalid DER-encoded certificate data");
/*      */     }
/*      */ 
/* 1756 */     this.signedCert = paramDerValue.toByteArray();
/* 1757 */     DerValue[] arrayOfDerValue = new DerValue[3];
/*      */ 
/* 1759 */     arrayOfDerValue[0] = paramDerValue.data.getDerValue();
/* 1760 */     arrayOfDerValue[1] = paramDerValue.data.getDerValue();
/* 1761 */     arrayOfDerValue[2] = paramDerValue.data.getDerValue();
/*      */ 
/* 1763 */     if (paramDerValue.data.available() != 0) {
/* 1764 */       throw new CertificateParsingException("signed overrun, bytes = " + paramDerValue.data.available());
/*      */     }
/*      */ 
/* 1767 */     if (arrayOfDerValue[0].tag != 48) {
/* 1768 */       throw new CertificateParsingException("signed fields invalid");
/*      */     }
/*      */ 
/* 1771 */     this.algId = AlgorithmId.parse(arrayOfDerValue[1]);
/* 1772 */     this.signature = arrayOfDerValue[2].getBitString();
/*      */ 
/* 1774 */     if (arrayOfDerValue[1].data.available() != 0) {
/* 1775 */       throw new CertificateParsingException("algid field overrun");
/*      */     }
/* 1777 */     if (arrayOfDerValue[2].data.available() != 0) {
/* 1778 */       throw new CertificateParsingException("signed fields overrun");
/*      */     }
/*      */ 
/* 1781 */     this.info = new X509CertInfo(arrayOfDerValue[0]);
/*      */ 
/* 1784 */     AlgorithmId localAlgorithmId = (AlgorithmId)this.info.get("algorithmID.algorithm");
/*      */ 
/* 1788 */     if (!this.algId.equals(localAlgorithmId))
/* 1789 */       throw new CertificateException("Signature algorithm mismatch");
/* 1790 */     this.readOnly = true;
/*      */   }
/*      */ 
/*      */   private static X500Principal getX500Principal(X509Certificate paramX509Certificate, boolean paramBoolean)
/*      */     throws Exception
/*      */   {
/* 1800 */     byte[] arrayOfByte1 = paramX509Certificate.getEncoded();
/* 1801 */     DerInputStream localDerInputStream1 = new DerInputStream(arrayOfByte1);
/* 1802 */     DerValue localDerValue1 = localDerInputStream1.getSequence(3)[0];
/* 1803 */     DerInputStream localDerInputStream2 = localDerValue1.data;
/*      */ 
/* 1805 */     DerValue localDerValue2 = localDerInputStream2.getDerValue();
/*      */ 
/* 1807 */     if (localDerValue2.isContextSpecific((byte)0)) {
/* 1808 */       localDerValue2 = localDerInputStream2.getDerValue();
/*      */     }
/*      */ 
/* 1811 */     localDerValue2 = localDerInputStream2.getDerValue();
/* 1812 */     localDerValue2 = localDerInputStream2.getDerValue();
/* 1813 */     if (!paramBoolean) {
/* 1814 */       localDerValue2 = localDerInputStream2.getDerValue();
/* 1815 */       localDerValue2 = localDerInputStream2.getDerValue();
/*      */     }
/* 1817 */     byte[] arrayOfByte2 = localDerValue2.toByteArray();
/* 1818 */     return new X500Principal(arrayOfByte2);
/*      */   }
/*      */ 
/*      */   public static X500Principal getSubjectX500Principal(X509Certificate paramX509Certificate)
/*      */   {
/*      */     try
/*      */     {
/* 1827 */       return getX500Principal(paramX509Certificate, false);
/*      */     } catch (Exception localException) {
/* 1829 */       throw new RuntimeException("Could not parse subject", localException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public static X500Principal getIssuerX500Principal(X509Certificate paramX509Certificate)
/*      */   {
/*      */     try
/*      */     {
/* 1839 */       return getX500Principal(paramX509Certificate, true);
/*      */     } catch (Exception localException) {
/* 1841 */       throw new RuntimeException("Could not parse issuer", localException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public static byte[] getEncodedInternal(Certificate paramCertificate)
/*      */     throws CertificateEncodingException
/*      */   {
/* 1853 */     if ((paramCertificate instanceof X509CertImpl)) {
/* 1854 */       return ((X509CertImpl)paramCertificate).getEncodedInternal();
/*      */     }
/* 1856 */     return paramCertificate.getEncoded();
/*      */   }
/*      */ 
/*      */   public static X509CertImpl toImpl(X509Certificate paramX509Certificate)
/*      */     throws CertificateException
/*      */   {
/* 1867 */     if ((paramX509Certificate instanceof X509CertImpl)) {
/* 1868 */       return (X509CertImpl)paramX509Certificate;
/*      */     }
/* 1870 */     return X509Factory.intern(paramX509Certificate);
/*      */   }
/*      */ 
/*      */   public static boolean isSelfIssued(X509Certificate paramX509Certificate)
/*      */   {
/* 1879 */     X500Principal localX500Principal1 = paramX509Certificate.getSubjectX500Principal();
/* 1880 */     X500Principal localX500Principal2 = paramX509Certificate.getIssuerX500Principal();
/* 1881 */     return localX500Principal1.equals(localX500Principal2);
/*      */   }
/*      */ 
/*      */   public static boolean isSelfSigned(X509Certificate paramX509Certificate, String paramString)
/*      */   {
/* 1892 */     if (isSelfIssued(paramX509Certificate))
/*      */       try {
/* 1894 */         if (paramString == null)
/* 1895 */           paramX509Certificate.verify(paramX509Certificate.getPublicKey());
/*      */         else {
/* 1897 */           paramX509Certificate.verify(paramX509Certificate.getPublicKey(), paramString);
/*      */         }
/* 1899 */         return true;
/*      */       }
/*      */       catch (Exception localException)
/*      */       {
/*      */       }
/* 1904 */     return false;
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.x509.X509CertImpl
 * JD-Core Version:    0.6.2
 */