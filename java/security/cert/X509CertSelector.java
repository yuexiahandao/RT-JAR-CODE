/*      */ package java.security.cert;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.math.BigInteger;
/*      */ import java.security.PublicKey;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Date;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Set;
/*      */ import java.util.Vector;
/*      */ import javax.security.auth.x500.X500Principal;
/*      */ import sun.misc.HexDumpEncoder;
/*      */ import sun.security.util.Debug;
/*      */ import sun.security.util.DerInputStream;
/*      */ import sun.security.util.DerValue;
/*      */ import sun.security.util.ObjectIdentifier;
/*      */ import sun.security.x509.AlgorithmId;
/*      */ import sun.security.x509.CertificatePoliciesExtension;
/*      */ import sun.security.x509.CertificatePolicyId;
/*      */ import sun.security.x509.CertificatePolicySet;
/*      */ import sun.security.x509.DNSName;
/*      */ import sun.security.x509.EDIPartyName;
/*      */ import sun.security.x509.ExtendedKeyUsageExtension;
/*      */ import sun.security.x509.GeneralName;
/*      */ import sun.security.x509.GeneralNameInterface;
/*      */ import sun.security.x509.GeneralNames;
/*      */ import sun.security.x509.GeneralSubtree;
/*      */ import sun.security.x509.GeneralSubtrees;
/*      */ import sun.security.x509.IPAddressName;
/*      */ import sun.security.x509.NameConstraintsExtension;
/*      */ import sun.security.x509.OIDName;
/*      */ import sun.security.x509.OtherName;
/*      */ import sun.security.x509.PolicyInformation;
/*      */ import sun.security.x509.PrivateKeyUsageExtension;
/*      */ import sun.security.x509.RFC822Name;
/*      */ import sun.security.x509.SubjectAlternativeNameExtension;
/*      */ import sun.security.x509.URIName;
/*      */ import sun.security.x509.X400Address;
/*      */ import sun.security.x509.X500Name;
/*      */ import sun.security.x509.X509CertImpl;
/*      */ import sun.security.x509.X509Key;
/*      */ 
/*      */ public class X509CertSelector
/*      */   implements CertSelector
/*      */ {
/*   88 */   private static final Debug debug = Debug.getInstance("certpath");
/*      */ 
/*   90 */   private static final ObjectIdentifier ANY_EXTENDED_KEY_USAGE = ObjectIdentifier.newInternal(new int[] { 2, 5, 29, 37, 0 });
/*      */   private BigInteger serialNumber;
/*      */   private X500Principal issuer;
/*      */   private X500Principal subject;
/*      */   private byte[] subjectKeyID;
/*      */   private byte[] authorityKeyID;
/*      */   private Date certificateValid;
/*      */   private Date privateKeyValid;
/*      */   private ObjectIdentifier subjectPublicKeyAlgID;
/*      */   private PublicKey subjectPublicKey;
/*      */   private byte[] subjectPublicKeyBytes;
/*      */   private boolean[] keyUsage;
/*      */   private Set<String> keyPurposeSet;
/*      */   private Set<ObjectIdentifier> keyPurposeOIDSet;
/*      */   private Set<List<?>> subjectAlternativeNames;
/*      */   private Set<GeneralNameInterface> subjectAlternativeGeneralNames;
/*      */   private CertificatePolicySet policy;
/*      */   private Set<String> policySet;
/*      */   private Set<List<?>> pathToNames;
/*      */   private Set<GeneralNameInterface> pathToGeneralNames;
/*      */   private NameConstraintsExtension nc;
/*      */   private byte[] ncBytes;
/*  118 */   private int basicConstraints = -1;
/*      */   private X509Certificate x509Cert;
/*  120 */   private boolean matchAllSubjectAltNames = true;
/*      */   private static final Boolean FALSE;
/*      */   private static final int PRIVATE_KEY_USAGE_ID = 0;
/*      */   private static final int SUBJECT_ALT_NAME_ID = 1;
/*      */   private static final int NAME_CONSTRAINTS_ID = 2;
/*      */   private static final int CERT_POLICIES_ID = 3;
/*      */   private static final int EXTENDED_KEY_USAGE_ID = 4;
/*      */   private static final int NUM_OF_EXTENSIONS = 5;
/*      */   private static final String[] EXTENSION_OIDS;
/*      */   static final int NAME_ANY = 0;
/*      */   static final int NAME_RFC822 = 1;
/*      */   static final int NAME_DNS = 2;
/*      */   static final int NAME_X400 = 3;
/*      */   static final int NAME_DIRECTORY = 4;
/*      */   static final int NAME_EDI = 5;
/*      */   static final int NAME_URI = 6;
/*      */   static final int NAME_IP = 7;
/*      */   static final int NAME_OID = 8;
/*      */ 
/*      */   public void setCertificate(X509Certificate paramX509Certificate)
/*      */   {
/*  175 */     this.x509Cert = paramX509Certificate;
/*      */   }
/*      */ 
/*      */   public void setSerialNumber(BigInteger paramBigInteger)
/*      */   {
/*  189 */     this.serialNumber = paramBigInteger;
/*      */   }
/*      */ 
/*      */   public void setIssuer(X500Principal paramX500Principal)
/*      */   {
/*  203 */     this.issuer = paramX500Principal;
/*      */   }
/*      */ 
/*      */   public void setIssuer(String paramString)
/*      */     throws IOException
/*      */   {
/*  227 */     if (paramString == null)
/*  228 */       this.issuer = null;
/*      */     else
/*  230 */       this.issuer = new X500Name(paramString).asX500Principal();
/*      */   }
/*      */ 
/*      */   public void setIssuer(byte[] paramArrayOfByte)
/*      */     throws IOException
/*      */   {
/*      */     try
/*      */     {
/*  278 */       this.issuer = (paramArrayOfByte == null ? null : new X500Principal(paramArrayOfByte));
/*      */     } catch (IllegalArgumentException localIllegalArgumentException) {
/*  280 */       throw ((IOException)new IOException("Invalid name").initCause(localIllegalArgumentException));
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setSubject(X500Principal paramX500Principal)
/*      */   {
/*  295 */     this.subject = paramX500Principal;
/*      */   }
/*      */ 
/*      */   public void setSubject(String paramString)
/*      */     throws IOException
/*      */   {
/*  318 */     if (paramString == null)
/*  319 */       this.subject = null;
/*      */     else
/*  321 */       this.subject = new X500Name(paramString).asX500Principal();
/*      */   }
/*      */ 
/*      */   public void setSubject(byte[] paramArrayOfByte)
/*      */     throws IOException
/*      */   {
/*      */     try
/*      */     {
/*  342 */       this.subject = (paramArrayOfByte == null ? null : new X500Principal(paramArrayOfByte));
/*      */     } catch (IllegalArgumentException localIllegalArgumentException) {
/*  344 */       throw ((IOException)new IOException("Invalid name").initCause(localIllegalArgumentException));
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setSubjectKeyIdentifier(byte[] paramArrayOfByte)
/*      */   {
/*  381 */     if (paramArrayOfByte == null)
/*  382 */       this.subjectKeyID = null;
/*      */     else
/*  384 */       this.subjectKeyID = ((byte[])paramArrayOfByte.clone());
/*      */   }
/*      */ 
/*      */   public void setAuthorityKeyIdentifier(byte[] paramArrayOfByte)
/*      */   {
/*  442 */     if (paramArrayOfByte == null)
/*  443 */       this.authorityKeyID = null;
/*      */     else
/*  445 */       this.authorityKeyID = ((byte[])paramArrayOfByte.clone());
/*      */   }
/*      */ 
/*      */   public void setCertificateValid(Date paramDate)
/*      */   {
/*  462 */     if (paramDate == null)
/*  463 */       this.certificateValid = null;
/*      */     else
/*  465 */       this.certificateValid = ((Date)paramDate.clone());
/*      */   }
/*      */ 
/*      */   public void setPrivateKeyValid(Date paramDate)
/*      */   {
/*  483 */     if (paramDate == null)
/*  484 */       this.privateKeyValid = null;
/*      */     else
/*  486 */       this.privateKeyValid = ((Date)paramDate.clone());
/*      */   }
/*      */ 
/*      */   public void setSubjectPublicKeyAlgID(String paramString)
/*      */     throws IOException
/*      */   {
/*  506 */     if (paramString == null)
/*  507 */       this.subjectPublicKeyAlgID = null;
/*      */     else
/*  509 */       this.subjectPublicKeyAlgID = new ObjectIdentifier(paramString);
/*      */   }
/*      */ 
/*      */   public void setSubjectPublicKey(PublicKey paramPublicKey)
/*      */   {
/*  522 */     if (paramPublicKey == null) {
/*  523 */       this.subjectPublicKey = null;
/*  524 */       this.subjectPublicKeyBytes = null;
/*      */     } else {
/*  526 */       this.subjectPublicKey = paramPublicKey;
/*  527 */       this.subjectPublicKeyBytes = paramPublicKey.getEncoded();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setSubjectPublicKey(byte[] paramArrayOfByte)
/*      */     throws IOException
/*      */   {
/*  565 */     if (paramArrayOfByte == null) {
/*  566 */       this.subjectPublicKey = null;
/*  567 */       this.subjectPublicKeyBytes = null;
/*      */     } else {
/*  569 */       this.subjectPublicKeyBytes = ((byte[])paramArrayOfByte.clone());
/*  570 */       this.subjectPublicKey = X509Key.parse(new DerValue(this.subjectPublicKeyBytes));
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setKeyUsage(boolean[] paramArrayOfBoolean)
/*      */   {
/*  590 */     if (paramArrayOfBoolean == null)
/*  591 */       this.keyUsage = null;
/*      */     else
/*  593 */       this.keyUsage = ((boolean[])paramArrayOfBoolean.clone());
/*      */   }
/*      */ 
/*      */   public void setExtendedKeyUsage(Set<String> paramSet)
/*      */     throws IOException
/*      */   {
/*  617 */     if ((paramSet == null) || (paramSet.isEmpty())) {
/*  618 */       this.keyPurposeSet = null;
/*  619 */       this.keyPurposeOIDSet = null;
/*      */     } else {
/*  621 */       this.keyPurposeSet = Collections.unmodifiableSet(new HashSet(paramSet));
/*      */ 
/*  623 */       this.keyPurposeOIDSet = new HashSet();
/*  624 */       for (String str : this.keyPurposeSet)
/*  625 */         this.keyPurposeOIDSet.add(new ObjectIdentifier(str));
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setMatchAllSubjectAltNames(boolean paramBoolean)
/*      */   {
/*  647 */     this.matchAllSubjectAltNames = paramBoolean;
/*      */   }
/*      */ 
/*      */   public void setSubjectAlternativeNames(Collection<List<?>> paramCollection)
/*      */     throws IOException
/*      */   {
/*  699 */     if (paramCollection == null) {
/*  700 */       this.subjectAlternativeNames = null;
/*  701 */       this.subjectAlternativeGeneralNames = null;
/*      */     } else {
/*  703 */       if (paramCollection.isEmpty()) {
/*  704 */         this.subjectAlternativeNames = null;
/*  705 */         this.subjectAlternativeGeneralNames = null;
/*  706 */         return;
/*      */       }
/*  708 */       Set localSet = cloneAndCheckNames(paramCollection);
/*      */ 
/*  710 */       this.subjectAlternativeGeneralNames = parseNames(localSet);
/*  711 */       this.subjectAlternativeNames = localSet;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void addSubjectAlternativeName(int paramInt, String paramString)
/*      */     throws IOException
/*      */   {
/*  755 */     addSubjectAlternativeNameInternal(paramInt, paramString);
/*      */   }
/*      */ 
/*      */   public void addSubjectAlternativeName(int paramInt, byte[] paramArrayOfByte)
/*      */     throws IOException
/*      */   {
/*  800 */     addSubjectAlternativeNameInternal(paramInt, paramArrayOfByte.clone());
/*      */   }
/*      */ 
/*      */   private void addSubjectAlternativeNameInternal(int paramInt, Object paramObject)
/*      */     throws IOException
/*      */   {
/*  816 */     GeneralNameInterface localGeneralNameInterface = makeGeneralNameInterface(paramInt, paramObject);
/*  817 */     if (this.subjectAlternativeNames == null) {
/*  818 */       this.subjectAlternativeNames = new HashSet();
/*      */     }
/*  820 */     if (this.subjectAlternativeGeneralNames == null) {
/*  821 */       this.subjectAlternativeGeneralNames = new HashSet();
/*      */     }
/*  823 */     ArrayList localArrayList = new ArrayList(2);
/*  824 */     localArrayList.add(Integer.valueOf(paramInt));
/*  825 */     localArrayList.add(paramObject);
/*  826 */     this.subjectAlternativeNames.add(localArrayList);
/*  827 */     this.subjectAlternativeGeneralNames.add(localGeneralNameInterface);
/*      */   }
/*      */ 
/*      */   private static Set<GeneralNameInterface> parseNames(Collection<List<?>> paramCollection)
/*      */     throws IOException
/*      */   {
/*  848 */     HashSet localHashSet = new HashSet();
/*  849 */     for (List localList : paramCollection) {
/*  850 */       if (localList.size() != 2) {
/*  851 */         throw new IOException("name list size not 2");
/*      */       }
/*  853 */       Object localObject = localList.get(0);
/*  854 */       if (!(localObject instanceof Integer)) {
/*  855 */         throw new IOException("expected an Integer");
/*      */       }
/*  857 */       int i = ((Integer)localObject).intValue();
/*  858 */       localObject = localList.get(1);
/*  859 */       localHashSet.add(makeGeneralNameInterface(i, localObject));
/*      */     }
/*      */ 
/*  862 */     return localHashSet;
/*      */   }
/*      */ 
/*      */   static boolean equalNames(Collection paramCollection1, Collection paramCollection2)
/*      */   {
/*  876 */     if ((paramCollection1 == null) || (paramCollection2 == null)) {
/*  877 */       return paramCollection1 == paramCollection2;
/*      */     }
/*  879 */     return paramCollection1.equals(paramCollection2);
/*      */   }
/*      */ 
/*      */   static GeneralNameInterface makeGeneralNameInterface(int paramInt, Object paramObject)
/*      */     throws IOException
/*      */   {
/*  900 */     if (debug != null)
/*  901 */       debug.println("X509CertSelector.makeGeneralNameInterface(" + paramInt + ")...");
/*      */     Object localObject;
/*  905 */     if ((paramObject instanceof String)) {
/*  906 */       if (debug != null) {
/*  907 */         debug.println("X509CertSelector.makeGeneralNameInterface() name is String: " + paramObject);
/*      */       }
/*      */ 
/*  910 */       switch (paramInt) {
/*      */       case 1:
/*  912 */         localObject = new RFC822Name((String)paramObject);
/*  913 */         break;
/*      */       case 2:
/*  915 */         localObject = new DNSName((String)paramObject);
/*  916 */         break;
/*      */       case 4:
/*  918 */         localObject = new X500Name((String)paramObject);
/*  919 */         break;
/*      */       case 6:
/*  921 */         localObject = new URIName((String)paramObject);
/*  922 */         break;
/*      */       case 7:
/*  924 */         localObject = new IPAddressName((String)paramObject);
/*  925 */         break;
/*      */       case 8:
/*  927 */         localObject = new OIDName((String)paramObject);
/*  928 */         break;
/*      */       case 3:
/*      */       case 5:
/*      */       default:
/*  930 */         throw new IOException("unable to parse String names of type " + paramInt);
/*      */       }
/*      */ 
/*  933 */       if (debug != null) {
/*  934 */         debug.println("X509CertSelector.makeGeneralNameInterface() result: " + localObject.toString());
/*      */       }
/*      */     }
/*  937 */     else if ((paramObject instanceof byte[])) {
/*  938 */       DerValue localDerValue = new DerValue((byte[])paramObject);
/*  939 */       if (debug != null) {
/*  940 */         debug.println("X509CertSelector.makeGeneralNameInterface() is byte[]");
/*      */       }
/*      */ 
/*  944 */       switch (paramInt) {
/*      */       case 0:
/*  946 */         localObject = new OtherName(localDerValue);
/*  947 */         break;
/*      */       case 1:
/*  949 */         localObject = new RFC822Name(localDerValue);
/*  950 */         break;
/*      */       case 2:
/*  952 */         localObject = new DNSName(localDerValue);
/*  953 */         break;
/*      */       case 3:
/*  955 */         localObject = new X400Address(localDerValue);
/*  956 */         break;
/*      */       case 4:
/*  958 */         localObject = new X500Name(localDerValue);
/*  959 */         break;
/*      */       case 5:
/*  961 */         localObject = new EDIPartyName(localDerValue);
/*  962 */         break;
/*      */       case 6:
/*  964 */         localObject = new URIName(localDerValue);
/*  965 */         break;
/*      */       case 7:
/*  967 */         localObject = new IPAddressName(localDerValue);
/*  968 */         break;
/*      */       case 8:
/*  970 */         localObject = new OIDName(localDerValue);
/*  971 */         break;
/*      */       default:
/*  973 */         throw new IOException("unable to parse byte array names of type " + paramInt);
/*      */       }
/*      */ 
/*  976 */       if (debug != null)
/*  977 */         debug.println("X509CertSelector.makeGeneralNameInterface() result: " + localObject.toString());
/*      */     }
/*      */     else
/*      */     {
/*  981 */       if (debug != null) {
/*  982 */         debug.println("X509CertSelector.makeGeneralName() input name not String or byte array");
/*      */       }
/*      */ 
/*  985 */       throw new IOException("name not String or byte array");
/*      */     }
/*  987 */     return localObject;
/*      */   }
/*      */ 
/*      */   public void setNameConstraints(byte[] paramArrayOfByte)
/*      */     throws IOException
/*      */   {
/* 1040 */     if (paramArrayOfByte == null) {
/* 1041 */       this.ncBytes = null;
/* 1042 */       this.nc = null;
/*      */     } else {
/* 1044 */       this.ncBytes = ((byte[])paramArrayOfByte.clone());
/* 1045 */       this.nc = new NameConstraintsExtension(FALSE, paramArrayOfByte);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setBasicConstraints(int paramInt)
/*      */   {
/* 1066 */     if (paramInt < -2) {
/* 1067 */       throw new IllegalArgumentException("basic constraints less than -2");
/*      */     }
/* 1069 */     this.basicConstraints = paramInt;
/*      */   }
/*      */ 
/*      */   public void setPolicy(Set<String> paramSet)
/*      */     throws IOException
/*      */   {
/* 1093 */     if (paramSet == null) {
/* 1094 */       this.policySet = null;
/* 1095 */       this.policy = null;
/*      */     }
/*      */     else {
/* 1098 */       Set localSet = Collections.unmodifiableSet(new HashSet(paramSet));
/*      */ 
/* 1101 */       Iterator localIterator = localSet.iterator();
/* 1102 */       Vector localVector = new Vector();
/* 1103 */       while (localIterator.hasNext()) {
/* 1104 */         Object localObject = localIterator.next();
/* 1105 */         if (!(localObject instanceof String)) {
/* 1106 */           throw new IOException("non String in certPolicySet");
/*      */         }
/* 1108 */         localVector.add(new CertificatePolicyId(new ObjectIdentifier((String)localObject)));
/*      */       }
/*      */ 
/* 1112 */       this.policySet = localSet;
/* 1113 */       this.policy = new CertificatePolicySet(localVector);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setPathToNames(Collection<List<?>> paramCollection)
/*      */     throws IOException
/*      */   {
/* 1169 */     if ((paramCollection == null) || (paramCollection.isEmpty())) {
/* 1170 */       this.pathToNames = null;
/* 1171 */       this.pathToGeneralNames = null;
/*      */     } else {
/* 1173 */       Set localSet = cloneAndCheckNames(paramCollection);
/* 1174 */       this.pathToGeneralNames = parseNames(localSet);
/*      */ 
/* 1176 */       this.pathToNames = localSet;
/*      */     }
/*      */   }
/*      */ 
/*      */   void setPathToNamesInternal(Set<GeneralNameInterface> paramSet)
/*      */   {
/* 1184 */     this.pathToNames = Collections.emptySet();
/* 1185 */     this.pathToGeneralNames = paramSet;
/*      */   }
/*      */ 
/*      */   public void addPathToName(int paramInt, String paramString)
/*      */     throws IOException
/*      */   {
/* 1222 */     addPathToNameInternal(paramInt, paramString);
/*      */   }
/*      */ 
/*      */   public void addPathToName(int paramInt, byte[] paramArrayOfByte)
/*      */     throws IOException
/*      */   {
/* 1252 */     addPathToNameInternal(paramInt, paramArrayOfByte.clone());
/*      */   }
/*      */ 
/*      */   private void addPathToNameInternal(int paramInt, Object paramObject)
/*      */     throws IOException
/*      */   {
/* 1268 */     GeneralNameInterface localGeneralNameInterface = makeGeneralNameInterface(paramInt, paramObject);
/* 1269 */     if (this.pathToGeneralNames == null) {
/* 1270 */       this.pathToNames = new HashSet();
/* 1271 */       this.pathToGeneralNames = new HashSet();
/*      */     }
/* 1273 */     ArrayList localArrayList = new ArrayList(2);
/* 1274 */     localArrayList.add(Integer.valueOf(paramInt));
/* 1275 */     localArrayList.add(paramObject);
/* 1276 */     this.pathToNames.add(localArrayList);
/* 1277 */     this.pathToGeneralNames.add(localGeneralNameInterface);
/*      */   }
/*      */ 
/*      */   public X509Certificate getCertificate()
/*      */   {
/* 1290 */     return this.x509Cert;
/*      */   }
/*      */ 
/*      */   public BigInteger getSerialNumber()
/*      */   {
/* 1304 */     return this.serialNumber;
/*      */   }
/*      */ 
/*      */   public X500Principal getIssuer()
/*      */   {
/* 1318 */     return this.issuer;
/*      */   }
/*      */ 
/*      */   public String getIssuerAsString()
/*      */   {
/* 1340 */     return this.issuer == null ? null : this.issuer.getName();
/*      */   }
/*      */ 
/*      */   public byte[] getIssuerAsBytes()
/*      */     throws IOException
/*      */   {
/* 1363 */     return this.issuer == null ? null : this.issuer.getEncoded();
/*      */   }
/*      */ 
/*      */   public X500Principal getSubject()
/*      */   {
/* 1377 */     return this.subject;
/*      */   }
/*      */ 
/*      */   public String getSubjectAsString()
/*      */   {
/* 1399 */     return this.subject == null ? null : this.subject.getName();
/*      */   }
/*      */ 
/*      */   public byte[] getSubjectAsBytes()
/*      */     throws IOException
/*      */   {
/* 1422 */     return this.subject == null ? null : this.subject.getEncoded();
/*      */   }
/*      */ 
/*      */   public byte[] getSubjectKeyIdentifier()
/*      */   {
/* 1438 */     if (this.subjectKeyID == null) {
/* 1439 */       return null;
/*      */     }
/* 1441 */     return (byte[])this.subjectKeyID.clone();
/*      */   }
/*      */ 
/*      */   public byte[] getAuthorityKeyIdentifier()
/*      */   {
/* 1457 */     if (this.authorityKeyID == null) {
/* 1458 */       return null;
/*      */     }
/* 1460 */     return (byte[])this.authorityKeyID.clone();
/*      */   }
/*      */ 
/*      */   public Date getCertificateValid()
/*      */   {
/* 1476 */     if (this.certificateValid == null) {
/* 1477 */       return null;
/*      */     }
/* 1479 */     return (Date)this.certificateValid.clone();
/*      */   }
/*      */ 
/*      */   public Date getPrivateKeyValid()
/*      */   {
/* 1495 */     if (this.privateKeyValid == null) {
/* 1496 */       return null;
/*      */     }
/* 1498 */     return (Date)this.privateKeyValid.clone();
/*      */   }
/*      */ 
/*      */   public String getSubjectPublicKeyAlgID()
/*      */   {
/* 1513 */     if (this.subjectPublicKeyAlgID == null) {
/* 1514 */       return null;
/*      */     }
/* 1516 */     return this.subjectPublicKeyAlgID.toString();
/*      */   }
/*      */ 
/*      */   public PublicKey getSubjectPublicKey()
/*      */   {
/* 1528 */     return this.subjectPublicKey;
/*      */   }
/*      */ 
/*      */   public boolean[] getKeyUsage()
/*      */   {
/* 1546 */     if (this.keyUsage == null) {
/* 1547 */       return null;
/*      */     }
/* 1549 */     return (boolean[])this.keyUsage.clone();
/*      */   }
/*      */ 
/*      */   public Set<String> getExtendedKeyUsage()
/*      */   {
/* 1565 */     return this.keyPurposeSet;
/*      */   }
/*      */ 
/*      */   public boolean getMatchAllSubjectAltNames()
/*      */   {
/* 1585 */     return this.matchAllSubjectAltNames;
/*      */   }
/*      */ 
/*      */   public Collection<List<?>> getSubjectAlternativeNames()
/*      */   {
/* 1622 */     if (this.subjectAlternativeNames == null) {
/* 1623 */       return null;
/*      */     }
/* 1625 */     return cloneNames(this.subjectAlternativeNames);
/*      */   }
/*      */ 
/*      */   private static Set<List<?>> cloneNames(Collection<List<?>> paramCollection)
/*      */   {
/*      */     try
/*      */     {
/* 1650 */       return cloneAndCheckNames(paramCollection);
/*      */     } catch (IOException localIOException) {
/* 1652 */       throw new RuntimeException("cloneNames encountered IOException: " + localIOException.getMessage());
/*      */     }
/*      */   }
/*      */ 
/*      */   private static Set<List<?>> cloneAndCheckNames(Collection<List<?>> paramCollection)
/*      */     throws IOException
/*      */   {
/* 1674 */     HashSet localHashSet = new HashSet();
/* 1675 */     Iterator localIterator = paramCollection.iterator();
/*      */     Object localObject1;
/* 1676 */     while (localIterator.hasNext()) {
/* 1677 */       localObject1 = localIterator.next();
/* 1678 */       if (!(localObject1 instanceof List)) {
/* 1679 */         throw new IOException("expected a List");
/*      */       }
/* 1681 */       localHashSet.add(new ArrayList((List)localObject1));
/*      */     }
/*      */ 
/* 1685 */     localIterator = localHashSet.iterator();
/* 1686 */     while (localIterator.hasNext()) {
/* 1687 */       localObject1 = (List)localIterator.next();
/* 1688 */       if (((List)localObject1).size() != 2) {
/* 1689 */         throw new IOException("name list size not 2");
/*      */       }
/* 1691 */       Object localObject2 = ((List)localObject1).get(0);
/* 1692 */       if (!(localObject2 instanceof Integer)) {
/* 1693 */         throw new IOException("expected an Integer");
/*      */       }
/* 1695 */       int i = ((Integer)localObject2).intValue();
/* 1696 */       if ((i < 0) || (i > 8)) {
/* 1697 */         throw new IOException("name type not 0-8");
/*      */       }
/* 1699 */       Object localObject3 = ((List)localObject1).get(1);
/* 1700 */       if ((!(localObject3 instanceof byte[])) && (!(localObject3 instanceof String)))
/*      */       {
/* 1702 */         if (debug != null) {
/* 1703 */           debug.println("X509CertSelector.cloneAndCheckNames() name not byte array");
/*      */         }
/*      */ 
/* 1706 */         throw new IOException("name not byte array or String");
/*      */       }
/* 1708 */       if ((localObject3 instanceof byte[])) {
/* 1709 */         ((List)localObject1).set(1, ((byte[])localObject3).clone());
/*      */       }
/*      */     }
/* 1712 */     return localHashSet;
/*      */   }
/*      */ 
/*      */   public byte[] getNameConstraints()
/*      */   {
/* 1736 */     if (this.ncBytes == null) {
/* 1737 */       return null;
/*      */     }
/* 1739 */     return (byte[])this.ncBytes.clone();
/*      */   }
/*      */ 
/*      */   public int getBasicConstraints()
/*      */   {
/* 1754 */     return this.basicConstraints;
/*      */   }
/*      */ 
/*      */   public Set<String> getPolicy()
/*      */   {
/* 1770 */     return this.policySet;
/*      */   }
/*      */ 
/*      */   public Collection<List<?>> getPathToNames()
/*      */   {
/* 1805 */     if (this.pathToNames == null) {
/* 1806 */       return null;
/*      */     }
/* 1808 */     return cloneNames(this.pathToNames);
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/* 1818 */     StringBuffer localStringBuffer = new StringBuffer();
/* 1819 */     localStringBuffer.append("X509CertSelector: [\n");
/* 1820 */     if (this.x509Cert != null) {
/* 1821 */       localStringBuffer.append("  Certificate: " + this.x509Cert.toString() + "\n");
/*      */     }
/* 1823 */     if (this.serialNumber != null) {
/* 1824 */       localStringBuffer.append("  Serial Number: " + this.serialNumber.toString() + "\n");
/*      */     }
/* 1826 */     if (this.issuer != null) {
/* 1827 */       localStringBuffer.append("  Issuer: " + getIssuerAsString() + "\n");
/*      */     }
/* 1829 */     if (this.subject != null) {
/* 1830 */       localStringBuffer.append("  Subject: " + getSubjectAsString() + "\n");
/*      */     }
/* 1832 */     localStringBuffer.append("  matchAllSubjectAltNames flag: " + String.valueOf(this.matchAllSubjectAltNames) + "\n");
/*      */     Object localObject;
/* 1834 */     if (this.subjectAlternativeNames != null) {
/* 1835 */       localStringBuffer.append("  SubjectAlternativeNames:\n");
/* 1836 */       localObject = this.subjectAlternativeNames.iterator();
/* 1837 */       while (((Iterator)localObject).hasNext()) {
/* 1838 */         List localList = (List)((Iterator)localObject).next();
/* 1839 */         localStringBuffer.append("    type " + localList.get(0) + ", name " + localList.get(1) + "\n");
/*      */       }
/*      */     }
/*      */ 
/* 1843 */     if (this.subjectKeyID != null) {
/* 1844 */       localObject = new HexDumpEncoder();
/* 1845 */       localStringBuffer.append("  Subject Key Identifier: " + ((HexDumpEncoder)localObject).encodeBuffer(this.subjectKeyID) + "\n");
/*      */     }
/*      */ 
/* 1848 */     if (this.authorityKeyID != null) {
/* 1849 */       localObject = new HexDumpEncoder();
/* 1850 */       localStringBuffer.append("  Authority Key Identifier: " + ((HexDumpEncoder)localObject).encodeBuffer(this.authorityKeyID) + "\n");
/*      */     }
/*      */ 
/* 1853 */     if (this.certificateValid != null) {
/* 1854 */       localStringBuffer.append("  Certificate Valid: " + this.certificateValid.toString() + "\n");
/*      */     }
/*      */ 
/* 1857 */     if (this.privateKeyValid != null) {
/* 1858 */       localStringBuffer.append("  Private Key Valid: " + this.privateKeyValid.toString() + "\n");
/*      */     }
/*      */ 
/* 1861 */     if (this.subjectPublicKeyAlgID != null) {
/* 1862 */       localStringBuffer.append("  Subject Public Key AlgID: " + this.subjectPublicKeyAlgID.toString() + "\n");
/*      */     }
/*      */ 
/* 1865 */     if (this.subjectPublicKey != null) {
/* 1866 */       localStringBuffer.append("  Subject Public Key: " + this.subjectPublicKey.toString() + "\n");
/*      */     }
/*      */ 
/* 1869 */     if (this.keyUsage != null) {
/* 1870 */       localStringBuffer.append("  Key Usage: " + keyUsageToString(this.keyUsage) + "\n");
/*      */     }
/* 1872 */     if (this.keyPurposeSet != null) {
/* 1873 */       localStringBuffer.append("  Extended Key Usage: " + this.keyPurposeSet.toString() + "\n");
/*      */     }
/*      */ 
/* 1876 */     if (this.policy != null) {
/* 1877 */       localStringBuffer.append("  Policy: " + this.policy.toString() + "\n");
/*      */     }
/* 1879 */     if (this.pathToGeneralNames != null) {
/* 1880 */       localStringBuffer.append("  Path to names:\n");
/* 1881 */       localObject = this.pathToGeneralNames.iterator();
/* 1882 */       while (((Iterator)localObject).hasNext()) {
/* 1883 */         localStringBuffer.append("    " + ((Iterator)localObject).next() + "\n");
/*      */       }
/*      */     }
/* 1886 */     localStringBuffer.append("]");
/* 1887 */     return localStringBuffer.toString();
/*      */   }
/*      */ 
/*      */   private static String keyUsageToString(boolean[] paramArrayOfBoolean)
/*      */   {
/* 1896 */     String str = "KeyUsage [\n";
/*      */     try {
/* 1898 */       if (paramArrayOfBoolean[0] != 0) {
/* 1899 */         str = str + "  DigitalSignature\n";
/*      */       }
/* 1901 */       if (paramArrayOfBoolean[1] != 0) {
/* 1902 */         str = str + "  Non_repudiation\n";
/*      */       }
/* 1904 */       if (paramArrayOfBoolean[2] != 0) {
/* 1905 */         str = str + "  Key_Encipherment\n";
/*      */       }
/* 1907 */       if (paramArrayOfBoolean[3] != 0) {
/* 1908 */         str = str + "  Data_Encipherment\n";
/*      */       }
/* 1910 */       if (paramArrayOfBoolean[4] != 0) {
/* 1911 */         str = str + "  Key_Agreement\n";
/*      */       }
/* 1913 */       if (paramArrayOfBoolean[5] != 0) {
/* 1914 */         str = str + "  Key_CertSign\n";
/*      */       }
/* 1916 */       if (paramArrayOfBoolean[6] != 0) {
/* 1917 */         str = str + "  Crl_Sign\n";
/*      */       }
/* 1919 */       if (paramArrayOfBoolean[7] != 0) {
/* 1920 */         str = str + "  Encipher_Only\n";
/*      */       }
/* 1922 */       if (paramArrayOfBoolean[8] != 0)
/* 1923 */         str = str + "  Decipher_Only\n";
/*      */     }
/*      */     catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException) {
/*      */     }
/* 1927 */     str = str + "]\n";
/*      */ 
/* 1929 */     return str;
/*      */   }
/*      */ 
/*      */   private static Extension getExtensionObject(X509Certificate paramX509Certificate, int paramInt)
/*      */     throws IOException
/*      */   {
/* 1953 */     if ((paramX509Certificate instanceof X509CertImpl)) {
/* 1954 */       localObject = (X509CertImpl)paramX509Certificate;
/* 1955 */       switch (paramInt) {
/*      */       case 0:
/* 1957 */         return ((X509CertImpl)localObject).getPrivateKeyUsageExtension();
/*      */       case 1:
/* 1959 */         return ((X509CertImpl)localObject).getSubjectAlternativeNameExtension();
/*      */       case 2:
/* 1961 */         return ((X509CertImpl)localObject).getNameConstraintsExtension();
/*      */       case 3:
/* 1963 */         return ((X509CertImpl)localObject).getCertificatePoliciesExtension();
/*      */       case 4:
/* 1965 */         return ((X509CertImpl)localObject).getExtendedKeyUsageExtension();
/*      */       }
/* 1967 */       return null;
/*      */     }
/*      */ 
/* 1970 */     Object localObject = paramX509Certificate.getExtensionValue(EXTENSION_OIDS[paramInt]);
/* 1971 */     if (localObject == null) {
/* 1972 */       return null;
/*      */     }
/* 1974 */     DerInputStream localDerInputStream = new DerInputStream((byte[])localObject);
/* 1975 */     byte[] arrayOfByte = localDerInputStream.getOctetString();
/* 1976 */     switch (paramInt) {
/*      */     case 0:
/*      */       try {
/* 1979 */         return new PrivateKeyUsageExtension(FALSE, arrayOfByte);
/*      */       } catch (CertificateException localCertificateException) {
/* 1981 */         throw new IOException(localCertificateException.getMessage());
/*      */       }
/*      */     case 1:
/* 1984 */       return new SubjectAlternativeNameExtension(FALSE, arrayOfByte);
/*      */     case 2:
/* 1986 */       return new NameConstraintsExtension(FALSE, arrayOfByte);
/*      */     case 3:
/* 1988 */       return new CertificatePoliciesExtension(FALSE, arrayOfByte);
/*      */     case 4:
/* 1990 */       return new ExtendedKeyUsageExtension(FALSE, arrayOfByte);
/*      */     }
/* 1992 */     return null;
/*      */   }
/*      */ 
/*      */   public boolean match(Certificate paramCertificate)
/*      */   {
/* 2004 */     if (!(paramCertificate instanceof X509Certificate)) {
/* 2005 */       return false;
/*      */     }
/* 2007 */     X509Certificate localX509Certificate = (X509Certificate)paramCertificate;
/*      */ 
/* 2009 */     if (debug != null) {
/* 2010 */       debug.println("X509CertSelector.match(SN: " + localX509Certificate.getSerialNumber().toString(16) + "\n  Issuer: " + localX509Certificate.getIssuerDN() + "\n  Subject: " + localX509Certificate.getSubjectDN() + ")");
/*      */     }
/*      */ 
/* 2017 */     if ((this.x509Cert != null) && 
/* 2018 */       (!this.x509Cert.equals(localX509Certificate))) {
/* 2019 */       if (debug != null) {
/* 2020 */         debug.println("X509CertSelector.match: certs don't match");
/*      */       }
/*      */ 
/* 2023 */       return false;
/*      */     }
/*      */ 
/* 2028 */     if ((this.serialNumber != null) && 
/* 2029 */       (!this.serialNumber.equals(localX509Certificate.getSerialNumber()))) {
/* 2030 */       if (debug != null) {
/* 2031 */         debug.println("X509CertSelector.match: serial numbers don't match");
/*      */       }
/*      */ 
/* 2034 */       return false;
/*      */     }
/*      */ 
/* 2039 */     if ((this.issuer != null) && 
/* 2040 */       (!this.issuer.equals(localX509Certificate.getIssuerX500Principal()))) {
/* 2041 */       if (debug != null) {
/* 2042 */         debug.println("X509CertSelector.match: issuer DNs don't match");
/*      */       }
/*      */ 
/* 2045 */       return false;
/*      */     }
/*      */ 
/* 2050 */     if ((this.subject != null) && 
/* 2051 */       (!this.subject.equals(localX509Certificate.getSubjectX500Principal()))) {
/* 2052 */       if (debug != null) {
/* 2053 */         debug.println("X509CertSelector.match: subject DNs don't match");
/*      */       }
/*      */ 
/* 2056 */       return false;
/*      */     }
/*      */ 
/* 2061 */     if (this.certificateValid != null) {
/*      */       try {
/* 2063 */         localX509Certificate.checkValidity(this.certificateValid);
/*      */       } catch (CertificateException localCertificateException) {
/* 2065 */         if (debug != null) {
/* 2066 */           debug.println("X509CertSelector.match: certificate not within validity period");
/*      */         }
/*      */ 
/* 2069 */         return false;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2074 */     if (this.subjectPublicKeyBytes != null) {
/* 2075 */       byte[] arrayOfByte = localX509Certificate.getPublicKey().getEncoded();
/* 2076 */       if (!Arrays.equals(this.subjectPublicKeyBytes, arrayOfByte)) {
/* 2077 */         if (debug != null) {
/* 2078 */           debug.println("X509CertSelector.match: subject public keys don't match");
/*      */         }
/*      */ 
/* 2081 */         return false;
/*      */       }
/*      */     }
/*      */ 
/* 2085 */     boolean bool = (matchBasicConstraints(localX509Certificate)) && (matchKeyUsage(localX509Certificate)) && (matchExtendedKeyUsage(localX509Certificate)) && (matchSubjectKeyID(localX509Certificate)) && (matchAuthorityKeyID(localX509Certificate)) && (matchPrivateKeyValid(localX509Certificate)) && (matchSubjectPublicKeyAlgID(localX509Certificate)) && (matchPolicy(localX509Certificate)) && (matchSubjectAlternativeNames(localX509Certificate)) && (matchPathToNames(localX509Certificate)) && (matchNameConstraints(localX509Certificate));
/*      */ 
/* 2097 */     if ((bool) && (debug != null)) {
/* 2098 */       debug.println("X509CertSelector.match returning: true");
/*      */     }
/* 2100 */     return bool;
/*      */   }
/*      */ 
/*      */   private boolean matchSubjectKeyID(X509Certificate paramX509Certificate)
/*      */   {
/* 2105 */     if (this.subjectKeyID == null)
/* 2106 */       return true;
/*      */     try
/*      */     {
/* 2109 */       byte[] arrayOfByte1 = paramX509Certificate.getExtensionValue("2.5.29.14");
/* 2110 */       if (arrayOfByte1 == null) {
/* 2111 */         if (debug != null) {
/* 2112 */           debug.println("X509CertSelector.match: no subject key ID extension");
/*      */         }
/*      */ 
/* 2115 */         return false;
/*      */       }
/* 2117 */       DerInputStream localDerInputStream = new DerInputStream(arrayOfByte1);
/* 2118 */       byte[] arrayOfByte2 = localDerInputStream.getOctetString();
/* 2119 */       if ((arrayOfByte2 == null) || (!Arrays.equals(this.subjectKeyID, arrayOfByte2)))
/*      */       {
/* 2121 */         if (debug != null) {
/* 2122 */           debug.println("X509CertSelector.match: subject key IDs don't match");
/*      */         }
/*      */ 
/* 2125 */         return false;
/*      */       }
/*      */     } catch (IOException localIOException) {
/* 2128 */       if (debug != null) {
/* 2129 */         debug.println("X509CertSelector.match: exception in subject key ID check");
/*      */       }
/*      */ 
/* 2132 */       return false;
/*      */     }
/* 2134 */     return true;
/*      */   }
/*      */ 
/*      */   private boolean matchAuthorityKeyID(X509Certificate paramX509Certificate)
/*      */   {
/* 2139 */     if (this.authorityKeyID == null)
/* 2140 */       return true;
/*      */     try
/*      */     {
/* 2143 */       byte[] arrayOfByte1 = paramX509Certificate.getExtensionValue("2.5.29.35");
/* 2144 */       if (arrayOfByte1 == null) {
/* 2145 */         if (debug != null) {
/* 2146 */           debug.println("X509CertSelector.match: no authority key ID extension");
/*      */         }
/*      */ 
/* 2149 */         return false;
/*      */       }
/* 2151 */       DerInputStream localDerInputStream = new DerInputStream(arrayOfByte1);
/* 2152 */       byte[] arrayOfByte2 = localDerInputStream.getOctetString();
/* 2153 */       if ((arrayOfByte2 == null) || (!Arrays.equals(this.authorityKeyID, arrayOfByte2)))
/*      */       {
/* 2155 */         if (debug != null) {
/* 2156 */           debug.println("X509CertSelector.match: authority key IDs don't match");
/*      */         }
/*      */ 
/* 2159 */         return false;
/*      */       }
/*      */     } catch (IOException localIOException) {
/* 2162 */       if (debug != null) {
/* 2163 */         debug.println("X509CertSelector.match: exception in authority key ID check");
/*      */       }
/*      */ 
/* 2166 */       return false;
/*      */     }
/* 2168 */     return true;
/*      */   }
/*      */ 
/*      */   private boolean matchPrivateKeyValid(X509Certificate paramX509Certificate)
/*      */   {
/* 2173 */     if (this.privateKeyValid == null) {
/* 2174 */       return true;
/*      */     }
/* 2176 */     PrivateKeyUsageExtension localPrivateKeyUsageExtension = null;
/*      */     try {
/* 2178 */       localPrivateKeyUsageExtension = (PrivateKeyUsageExtension)getExtensionObject(paramX509Certificate, 0);
/*      */ 
/* 2180 */       if (localPrivateKeyUsageExtension != null)
/* 2181 */         localPrivateKeyUsageExtension.valid(this.privateKeyValid);
/*      */     }
/*      */     catch (CertificateExpiredException localCertificateExpiredException) {
/* 2184 */       if (debug != null) {
/* 2185 */         str = "n/a";
/*      */         try {
/* 2187 */           Date localDate1 = (Date)localPrivateKeyUsageExtension.get("not_after");
/*      */ 
/* 2189 */           str = localDate1.toString();
/*      */         }
/*      */         catch (CertificateException localCertificateException2) {
/*      */         }
/* 2193 */         debug.println("X509CertSelector.match: private key usage not within validity date; ext.NOT_After: " + str + "; X509CertSelector: " + toString());
/*      */ 
/* 2197 */         localCertificateExpiredException.printStackTrace();
/*      */       }
/* 2199 */       return false;
/*      */     }
/*      */     catch (CertificateNotYetValidException localCertificateNotYetValidException)
/*      */     {
/*      */       String str;
/* 2201 */       if (debug != null) {
/* 2202 */         str = "n/a";
/*      */         try {
/* 2204 */           Date localDate2 = (Date)localPrivateKeyUsageExtension.get("not_before");
/*      */ 
/* 2206 */           str = localDate2.toString();
/*      */         }
/*      */         catch (CertificateException localCertificateException3) {
/*      */         }
/* 2210 */         debug.println("X509CertSelector.match: private key usage not within validity date; ext.NOT_BEFORE: " + str + "; X509CertSelector: " + toString());
/*      */ 
/* 2214 */         localCertificateNotYetValidException.printStackTrace();
/*      */       }
/* 2216 */       return false;
/*      */     } catch (CertificateException localCertificateException1) {
/* 2218 */       if (debug != null) {
/* 2219 */         debug.println("X509CertSelector.match: CertificateException in private key usage check; X509CertSelector: " + toString());
/*      */ 
/* 2222 */         localCertificateException1.printStackTrace();
/*      */       }
/* 2224 */       return false;
/*      */     } catch (IOException localIOException) {
/* 2226 */       if (debug != null) {
/* 2227 */         debug.println("X509CertSelector.match: IOException in private key usage check; X509CertSelector: " + toString());
/*      */ 
/* 2230 */         localIOException.printStackTrace();
/*      */       }
/* 2232 */       return false;
/*      */     }
/* 2234 */     return true;
/*      */   }
/*      */ 
/*      */   private boolean matchSubjectPublicKeyAlgID(X509Certificate paramX509Certificate)
/*      */   {
/* 2239 */     if (this.subjectPublicKeyAlgID == null)
/* 2240 */       return true;
/*      */     try
/*      */     {
/* 2243 */       byte[] arrayOfByte = paramX509Certificate.getPublicKey().getEncoded();
/* 2244 */       DerValue localDerValue = new DerValue(arrayOfByte);
/* 2245 */       if (localDerValue.tag != 48) {
/* 2246 */         throw new IOException("invalid key format");
/*      */       }
/*      */ 
/* 2249 */       AlgorithmId localAlgorithmId = AlgorithmId.parse(localDerValue.data.getDerValue());
/* 2250 */       if (debug != null) {
/* 2251 */         debug.println("X509CertSelector.match: subjectPublicKeyAlgID = " + this.subjectPublicKeyAlgID + ", xcert subjectPublicKeyAlgID = " + localAlgorithmId.getOID());
/*      */       }
/*      */ 
/* 2255 */       if (!this.subjectPublicKeyAlgID.equals(localAlgorithmId.getOID())) {
/* 2256 */         if (debug != null) {
/* 2257 */           debug.println("X509CertSelector.match: subject public key alg IDs don't match");
/*      */         }
/*      */ 
/* 2260 */         return false;
/*      */       }
/*      */     } catch (IOException localIOException) {
/* 2263 */       if (debug != null) {
/* 2264 */         debug.println("X509CertSelector.match: IOException in subject public key algorithm OID check");
/*      */       }
/*      */ 
/* 2267 */       return false;
/*      */     }
/* 2269 */     return true;
/*      */   }
/*      */ 
/*      */   private boolean matchKeyUsage(X509Certificate paramX509Certificate)
/*      */   {
/* 2274 */     if (this.keyUsage == null) {
/* 2275 */       return true;
/*      */     }
/* 2277 */     boolean[] arrayOfBoolean = paramX509Certificate.getKeyUsage();
/* 2278 */     if (arrayOfBoolean != null) {
/* 2279 */       for (int i = 0; i < this.keyUsage.length; i++) {
/* 2280 */         if ((this.keyUsage[i] != 0) && ((i >= arrayOfBoolean.length) || (arrayOfBoolean[i] == 0)))
/*      */         {
/* 2282 */           if (debug != null) {
/* 2283 */             debug.println("X509CertSelector.match: key usage bits don't match");
/*      */           }
/*      */ 
/* 2286 */           return false;
/*      */         }
/*      */       }
/*      */     }
/* 2290 */     return true;
/*      */   }
/*      */ 
/*      */   private boolean matchExtendedKeyUsage(X509Certificate paramX509Certificate)
/*      */   {
/* 2295 */     if ((this.keyPurposeSet == null) || (this.keyPurposeSet.isEmpty()))
/* 2296 */       return true;
/*      */     try
/*      */     {
/* 2299 */       ExtendedKeyUsageExtension localExtendedKeyUsageExtension = (ExtendedKeyUsageExtension)getExtensionObject(paramX509Certificate, 4);
/*      */ 
/* 2302 */       if (localExtendedKeyUsageExtension != null) {
/* 2303 */         Vector localVector = (Vector)localExtendedKeyUsageExtension.get("usages");
/*      */ 
/* 2305 */         if ((!localVector.contains(ANY_EXTENDED_KEY_USAGE)) && (!localVector.containsAll(this.keyPurposeOIDSet)))
/*      */         {
/* 2307 */           if (debug != null) {
/* 2308 */             debug.println("X509CertSelector.match: cert failed extendedKeyUsage criterion");
/*      */           }
/*      */ 
/* 2311 */           return false;
/*      */         }
/*      */       }
/*      */     } catch (IOException localIOException) {
/* 2315 */       if (debug != null) {
/* 2316 */         debug.println("X509CertSelector.match: IOException in extended key usage check");
/*      */       }
/*      */ 
/* 2319 */       return false;
/*      */     }
/* 2321 */     return true;
/*      */   }
/*      */ 
/*      */   private boolean matchSubjectAlternativeNames(X509Certificate paramX509Certificate)
/*      */   {
/* 2326 */     if ((this.subjectAlternativeNames == null) || (this.subjectAlternativeNames.isEmpty()))
/* 2327 */       return true;
/*      */     try
/*      */     {
/* 2330 */       SubjectAlternativeNameExtension localSubjectAlternativeNameExtension = (SubjectAlternativeNameExtension)getExtensionObject(paramX509Certificate, 1);
/*      */ 
/* 2333 */       if (localSubjectAlternativeNameExtension == null) {
/* 2334 */         if (debug != null) {
/* 2335 */           debug.println("X509CertSelector.match: no subject alternative name extension");
/*      */         }
/*      */ 
/* 2338 */         return false;
/*      */       }
/* 2340 */       GeneralNames localGeneralNames = (GeneralNames)localSubjectAlternativeNameExtension.get("subject_name");
/*      */ 
/* 2342 */       Iterator localIterator1 = this.subjectAlternativeGeneralNames.iterator();
/*      */ 
/* 2344 */       while (localIterator1.hasNext()) {
/* 2345 */         GeneralNameInterface localGeneralNameInterface1 = (GeneralNameInterface)localIterator1.next();
/* 2346 */         boolean bool = false;
/* 2347 */         Iterator localIterator2 = localGeneralNames.iterator();
/* 2348 */         while ((localIterator2.hasNext()) && (!bool)) {
/* 2349 */           GeneralNameInterface localGeneralNameInterface2 = ((GeneralName)localIterator2.next()).getName();
/* 2350 */           bool = localGeneralNameInterface2.equals(localGeneralNameInterface1);
/*      */         }
/* 2352 */         if ((!bool) && ((this.matchAllSubjectAltNames) || (!localIterator1.hasNext()))) {
/* 2353 */           if (debug != null) {
/* 2354 */             debug.println("X509CertSelector.match: subject alternative name " + localGeneralNameInterface1 + " not found");
/*      */           }
/*      */ 
/* 2357 */           return false;
/* 2358 */         }if ((bool) && (!this.matchAllSubjectAltNames))
/*      */           break;
/*      */       }
/*      */     }
/*      */     catch (IOException localIOException) {
/* 2363 */       if (debug != null) {
/* 2364 */         debug.println("X509CertSelector.match: IOException in subject alternative name check");
/*      */       }
/* 2366 */       return false;
/*      */     }
/* 2368 */     return true;
/*      */   }
/*      */ 
/*      */   private boolean matchNameConstraints(X509Certificate paramX509Certificate)
/*      */   {
/* 2373 */     if (this.nc == null)
/* 2374 */       return true;
/*      */     try
/*      */     {
/* 2377 */       if (!this.nc.verify(paramX509Certificate)) {
/* 2378 */         if (debug != null) {
/* 2379 */           debug.println("X509CertSelector.match: name constraints not satisfied");
/*      */         }
/*      */ 
/* 2382 */         return false;
/*      */       }
/*      */     } catch (IOException localIOException) {
/* 2385 */       if (debug != null) {
/* 2386 */         debug.println("X509CertSelector.match: IOException in name constraints check");
/*      */       }
/*      */ 
/* 2389 */       return false;
/*      */     }
/* 2391 */     return true;
/*      */   }
/*      */ 
/*      */   private boolean matchPolicy(X509Certificate paramX509Certificate)
/*      */   {
/* 2396 */     if (this.policy == null)
/* 2397 */       return true;
/*      */     try
/*      */     {
/* 2400 */       CertificatePoliciesExtension localCertificatePoliciesExtension = (CertificatePoliciesExtension)getExtensionObject(paramX509Certificate, 3);
/*      */ 
/* 2402 */       if (localCertificatePoliciesExtension == null) {
/* 2403 */         if (debug != null) {
/* 2404 */           debug.println("X509CertSelector.match: no certificate policy extension");
/*      */         }
/*      */ 
/* 2407 */         return false;
/*      */       }
/* 2409 */       List localList = (List)localCertificatePoliciesExtension.get("policies");
/*      */ 
/* 2414 */       ArrayList localArrayList = new ArrayList(localList.size());
/* 2415 */       for (Iterator localIterator = localList.iterator(); localIterator.hasNext(); ) { localObject = (PolicyInformation)localIterator.next();
/* 2416 */         localArrayList.add(((PolicyInformation)localObject).getPolicyIdentifier());
/*      */       }
/*      */       Object localObject;
/* 2418 */       if (this.policy != null) {
/* 2419 */         int i = 0;
/*      */ 
/* 2425 */         if (this.policy.getCertPolicyIds().isEmpty()) {
/* 2426 */           if (localArrayList.isEmpty()) {
/* 2427 */             if (debug != null) {
/* 2428 */               debug.println("X509CertSelector.match: cert failed policyAny criterion");
/*      */             }
/*      */ 
/* 2431 */             return false;
/*      */           }
/*      */         } else {
/* 2434 */           for (localObject = this.policy.getCertPolicyIds().iterator(); ((Iterator)localObject).hasNext(); ) { CertificatePolicyId localCertificatePolicyId = (CertificatePolicyId)((Iterator)localObject).next();
/* 2435 */             if (localArrayList.contains(localCertificatePolicyId)) {
/* 2436 */               i = 1;
/* 2437 */               break;
/*      */             }
/*      */           }
/* 2440 */           if (i == 0) {
/* 2441 */             if (debug != null) {
/* 2442 */               debug.println("X509CertSelector.match: cert failed policyAny criterion");
/*      */             }
/*      */ 
/* 2445 */             return false;
/*      */           }
/*      */         }
/*      */       }
/*      */     } catch (IOException localIOException) {
/* 2450 */       if (debug != null) {
/* 2451 */         debug.println("X509CertSelector.match: IOException in certificate policy ID check");
/*      */       }
/*      */ 
/* 2454 */       return false;
/*      */     }
/* 2456 */     return true;
/*      */   }
/*      */ 
/*      */   private boolean matchPathToNames(X509Certificate paramX509Certificate)
/*      */   {
/* 2461 */     if (this.pathToGeneralNames == null)
/* 2462 */       return true;
/*      */     try
/*      */     {
/* 2465 */       NameConstraintsExtension localNameConstraintsExtension = (NameConstraintsExtension)getExtensionObject(paramX509Certificate, 2);
/*      */ 
/* 2467 */       if (localNameConstraintsExtension == null) {
/* 2468 */         return true;
/*      */       }
/* 2470 */       if ((debug != null) && (Debug.isOn("certpath"))) {
/* 2471 */         debug.println("X509CertSelector.match pathToNames:\n");
/* 2472 */         localObject = this.pathToGeneralNames.iterator();
/*      */ 
/* 2474 */         while (((Iterator)localObject).hasNext()) {
/* 2475 */           debug.println("    " + ((Iterator)localObject).next() + "\n");
/*      */         }
/*      */       }
/*      */ 
/* 2479 */       Object localObject = (GeneralSubtrees)localNameConstraintsExtension.get("permitted_subtrees");
/*      */ 
/* 2481 */       GeneralSubtrees localGeneralSubtrees = (GeneralSubtrees)localNameConstraintsExtension.get("excluded_subtrees");
/*      */ 
/* 2483 */       if ((localGeneralSubtrees != null) && 
/* 2484 */         (!matchExcluded(localGeneralSubtrees))) {
/* 2485 */         return false;
/*      */       }
/*      */ 
/* 2488 */       if ((localObject != null) && 
/* 2489 */         (!matchPermitted((GeneralSubtrees)localObject)))
/* 2490 */         return false;
/*      */     }
/*      */     catch (IOException localIOException)
/*      */     {
/* 2494 */       if (debug != null) {
/* 2495 */         debug.println("X509CertSelector.match: IOException in name constraints check");
/*      */       }
/*      */ 
/* 2498 */       return false;
/*      */     }
/* 2500 */     return true;
/*      */   }
/*      */ 
/*      */   private boolean matchExcluded(GeneralSubtrees paramGeneralSubtrees)
/*      */   {
/* 2509 */     for (Iterator localIterator1 = paramGeneralSubtrees.iterator(); localIterator1.hasNext(); ) {
/* 2510 */       GeneralSubtree localGeneralSubtree = (GeneralSubtree)localIterator1.next();
/* 2511 */       GeneralNameInterface localGeneralNameInterface1 = localGeneralSubtree.getName().getName();
/* 2512 */       Iterator localIterator2 = this.pathToGeneralNames.iterator();
/* 2513 */       while (localIterator2.hasNext()) {
/* 2514 */         GeneralNameInterface localGeneralNameInterface2 = (GeneralNameInterface)localIterator2.next();
/* 2515 */         if (localGeneralNameInterface1.getType() == localGeneralNameInterface2.getType()) {
/* 2516 */           switch (localGeneralNameInterface2.constrains(localGeneralNameInterface1)) {
/*      */           case 0:
/*      */           case 2:
/* 2519 */             if (debug != null) {
/* 2520 */               debug.println("X509CertSelector.match: name constraints inhibit path to specified name");
/*      */ 
/* 2522 */               debug.println("X509CertSelector.match: excluded name: " + localGeneralNameInterface2);
/*      */             }
/*      */ 
/* 2525 */             return false;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 2531 */     return true;
/*      */   }
/*      */ 
/*      */   private boolean matchPermitted(GeneralSubtrees paramGeneralSubtrees)
/*      */   {
/* 2541 */     Iterator localIterator1 = this.pathToGeneralNames.iterator();
/* 2542 */     while (localIterator1.hasNext()) {
/* 2543 */       GeneralNameInterface localGeneralNameInterface1 = (GeneralNameInterface)localIterator1.next();
/* 2544 */       Iterator localIterator2 = paramGeneralSubtrees.iterator();
/* 2545 */       int i = 0;
/* 2546 */       int j = 0;
/* 2547 */       String str = "";
/* 2548 */       while ((localIterator2.hasNext()) && (i == 0)) {
/* 2549 */         GeneralSubtree localGeneralSubtree = (GeneralSubtree)localIterator2.next();
/* 2550 */         GeneralNameInterface localGeneralNameInterface2 = localGeneralSubtree.getName().getName();
/* 2551 */         if (localGeneralNameInterface2.getType() == localGeneralNameInterface1.getType()) {
/* 2552 */           j = 1;
/* 2553 */           str = str + "  " + localGeneralNameInterface2;
/* 2554 */           switch (localGeneralNameInterface1.constrains(localGeneralNameInterface2)) {
/*      */           case 0:
/*      */           case 2:
/* 2557 */             i = 1;
/*      */           }
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 2563 */       if ((i == 0) && (j != 0)) {
/* 2564 */         if (debug != null) {
/* 2565 */           debug.println("X509CertSelector.match: name constraints inhibit path to specified name; permitted names of type " + localGeneralNameInterface1.getType() + ": " + str);
/*      */         }
/*      */ 
/* 2569 */         return false;
/*      */       }
/*      */     }
/* 2572 */     return true;
/*      */   }
/*      */ 
/*      */   private boolean matchBasicConstraints(X509Certificate paramX509Certificate)
/*      */   {
/* 2577 */     if (this.basicConstraints == -1) {
/* 2578 */       return true;
/*      */     }
/* 2580 */     int i = paramX509Certificate.getBasicConstraints();
/* 2581 */     if (this.basicConstraints == -2) {
/* 2582 */       if (i != -1) {
/* 2583 */         if (debug != null) {
/* 2584 */           debug.println("X509CertSelector.match: not an EE cert");
/*      */         }
/* 2586 */         return false;
/*      */       }
/*      */     }
/* 2589 */     else if (i < this.basicConstraints) {
/* 2590 */       if (debug != null) {
/* 2591 */         debug.println("X509CertSelector.match: maxPathLen too small (" + i + " < " + this.basicConstraints + ")");
/*      */       }
/*      */ 
/* 2594 */       return false;
/*      */     }
/*      */ 
/* 2597 */     return true;
/*      */   }
/*      */ 
/*      */   private static Set<?> cloneSet(Set<?> paramSet) {
/* 2601 */     if ((paramSet instanceof HashSet)) {
/* 2602 */       Object localObject = ((HashSet)paramSet).clone();
/* 2603 */       return (Set)localObject;
/*      */     }
/* 2605 */     return new HashSet(paramSet);
/*      */   }
/*      */ 
/*      */   public Object clone()
/*      */   {
/*      */     try
/*      */     {
/* 2616 */       X509CertSelector localX509CertSelector = (X509CertSelector)super.clone();
/*      */ 
/* 2618 */       if (this.subjectAlternativeNames != null) {
/* 2619 */         localX509CertSelector.subjectAlternativeNames = cloneSet(this.subjectAlternativeNames);
/*      */ 
/* 2621 */         localX509CertSelector.subjectAlternativeGeneralNames = cloneSet(this.subjectAlternativeGeneralNames);
/*      */       }
/*      */ 
/* 2625 */       if (this.pathToGeneralNames != null) {
/* 2626 */         localX509CertSelector.pathToNames = cloneSet(this.pathToNames);
/*      */ 
/* 2628 */         localX509CertSelector.pathToGeneralNames = cloneSet(this.pathToGeneralNames);
/*      */       }
/*      */ 
/* 2632 */       return localX509CertSelector;
/*      */     }
/*      */     catch (CloneNotSupportedException localCloneNotSupportedException) {
/* 2635 */       throw new InternalError(localCloneNotSupportedException.toString());
/*      */     }
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*   94 */     CertPathHelperImpl.initialize();
/*      */ 
/*  122 */     FALSE = Boolean.FALSE;
/*      */ 
/*  130 */     EXTENSION_OIDS = new String[5];
/*      */ 
/*  133 */     EXTENSION_OIDS[0] = "2.5.29.16";
/*  134 */     EXTENSION_OIDS[1] = "2.5.29.17";
/*  135 */     EXTENSION_OIDS[2] = "2.5.29.30";
/*  136 */     EXTENSION_OIDS[3] = "2.5.29.32";
/*  137 */     EXTENSION_OIDS[4] = "2.5.29.37";
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.cert.X509CertSelector
 * JD-Core Version:    0.6.2
 */