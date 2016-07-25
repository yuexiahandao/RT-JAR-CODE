/*      */ package sun.security.x509;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.lang.reflect.Field;
/*      */ import java.security.AccessController;
/*      */ import java.security.Principal;
/*      */ import java.security.PrivilegedExceptionAction;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collections;
/*      */ import java.util.HashMap;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import javax.security.auth.x500.X500Principal;
/*      */ import sun.security.util.DerInputStream;
/*      */ import sun.security.util.DerOutputStream;
/*      */ import sun.security.util.DerValue;
/*      */ import sun.security.util.ObjectIdentifier;
/*      */ 
/*      */ public class X500Name
/*      */   implements GeneralNameInterface, Principal
/*      */ {
/*      */   private String dn;
/*      */   private String rfc1779Dn;
/*      */   private String rfc2253Dn;
/*      */   private String canonicalDn;
/*      */   private RDN[] names;
/*      */   private X500Principal x500Principal;
/*      */   private byte[] encoded;
/*      */   private volatile List<RDN> rdnList;
/*      */   private volatile List<AVA> allAvaList;
/* 1122 */   private static final Map<ObjectIdentifier, ObjectIdentifier> internedOIDs = new HashMap();
/*      */ 
/* 1130 */   private static final int[] commonName_data = { 2, 5, 4, 3 };
/* 1131 */   private static final int[] SURNAME_DATA = { 2, 5, 4, 4 };
/* 1132 */   private static final int[] SERIALNUMBER_DATA = { 2, 5, 4, 5 };
/* 1133 */   private static final int[] countryName_data = { 2, 5, 4, 6 };
/* 1134 */   private static final int[] localityName_data = { 2, 5, 4, 7 };
/* 1135 */   private static final int[] stateName_data = { 2, 5, 4, 8 };
/* 1136 */   private static final int[] streetAddress_data = { 2, 5, 4, 9 };
/* 1137 */   private static final int[] orgName_data = { 2, 5, 4, 10 };
/* 1138 */   private static final int[] orgUnitName_data = { 2, 5, 4, 11 };
/* 1139 */   private static final int[] title_data = { 2, 5, 4, 12 };
/* 1140 */   private static final int[] GIVENNAME_DATA = { 2, 5, 4, 42 };
/* 1141 */   private static final int[] INITIALS_DATA = { 2, 5, 4, 43 };
/* 1142 */   private static final int[] GENERATIONQUALIFIER_DATA = { 2, 5, 4, 44 };
/* 1143 */   private static final int[] DNQUALIFIER_DATA = { 2, 5, 4, 46 };
/*      */ 
/* 1145 */   private static final int[] ipAddress_data = { 1, 3, 6, 1, 4, 1, 42, 2, 11, 2, 1 };
/* 1146 */   private static final int[] DOMAIN_COMPONENT_DATA = { 0, 9, 2342, 19200300, 100, 1, 25 };
/*      */ 
/* 1148 */   private static final int[] userid_data = { 0, 9, 2342, 19200300, 100, 1, 1 };
/*      */ 
/* 1172 */   public static final ObjectIdentifier commonName_oid = intern(ObjectIdentifier.newInternal(commonName_data));
/*      */   public static final ObjectIdentifier countryName_oid;
/*      */   public static final ObjectIdentifier localityName_oid;
/*      */   public static final ObjectIdentifier orgName_oid;
/*      */   public static final ObjectIdentifier orgUnitName_oid;
/*      */   public static final ObjectIdentifier stateName_oid;
/*      */   public static final ObjectIdentifier streetAddress_oid;
/*      */   public static final ObjectIdentifier title_oid;
/*      */   public static final ObjectIdentifier DNQUALIFIER_OID;
/*      */   public static final ObjectIdentifier SURNAME_OID;
/*      */   public static final ObjectIdentifier GIVENNAME_OID;
/*      */   public static final ObjectIdentifier INITIALS_OID;
/*      */   public static final ObjectIdentifier GENERATIONQUALIFIER_OID;
/*      */   public static final ObjectIdentifier ipAddress_oid;
/*      */   public static final ObjectIdentifier DOMAIN_COMPONENT_OID;
/*      */   public static final ObjectIdentifier userid_oid;
/* 1177 */   public static final ObjectIdentifier SERIALNUMBER_OID = intern(ObjectIdentifier.newInternal(SERIALNUMBER_DATA));
/*      */   private static final Constructor principalConstructor;
/*      */   private static final Field principalField;
/*      */ 
/*      */   public X500Name(String paramString)
/*      */     throws IOException
/*      */   {
/*  152 */     this(paramString, Collections.emptyMap());
/*      */   }
/*      */ 
/*      */   public X500Name(String paramString, Map<String, String> paramMap)
/*      */     throws IOException
/*      */   {
/*  165 */     parseDN(paramString, paramMap);
/*      */   }
/*      */ 
/*      */   public X500Name(String paramString1, String paramString2)
/*      */     throws IOException
/*      */   {
/*  178 */     if (paramString1 == null) {
/*  179 */       throw new NullPointerException("Name must not be null");
/*      */     }
/*  181 */     if (paramString2.equalsIgnoreCase("RFC2253"))
/*  182 */       parseRFC2253DN(paramString1);
/*  183 */     else if (paramString2.equalsIgnoreCase("DEFAULT"))
/*  184 */       parseDN(paramString1, Collections.emptyMap());
/*      */     else
/*  186 */       throw new IOException("Unsupported format " + paramString2);
/*      */   }
/*      */ 
/*      */   public X500Name(String paramString1, String paramString2, String paramString3, String paramString4)
/*      */     throws IOException
/*      */   {
/*  206 */     this.names = new RDN[4];
/*      */ 
/*  211 */     this.names[3] = new RDN(1);
/*  212 */     this.names[3].assertion[0] = new AVA(commonName_oid, new DerValue(paramString1));
/*      */ 
/*  214 */     this.names[2] = new RDN(1);
/*  215 */     this.names[2].assertion[0] = new AVA(orgUnitName_oid, new DerValue(paramString2));
/*      */ 
/*  217 */     this.names[1] = new RDN(1);
/*  218 */     this.names[1].assertion[0] = new AVA(orgName_oid, new DerValue(paramString3));
/*      */ 
/*  220 */     this.names[0] = new RDN(1);
/*  221 */     this.names[0].assertion[0] = new AVA(countryName_oid, new DerValue(paramString4));
/*      */   }
/*      */ 
/*      */   public X500Name(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6)
/*      */     throws IOException
/*      */   {
/*  244 */     this.names = new RDN[6];
/*      */ 
/*  249 */     this.names[5] = new RDN(1);
/*  250 */     this.names[5].assertion[0] = new AVA(commonName_oid, new DerValue(paramString1));
/*      */ 
/*  252 */     this.names[4] = new RDN(1);
/*  253 */     this.names[4].assertion[0] = new AVA(orgUnitName_oid, new DerValue(paramString2));
/*      */ 
/*  255 */     this.names[3] = new RDN(1);
/*  256 */     this.names[3].assertion[0] = new AVA(orgName_oid, new DerValue(paramString3));
/*      */ 
/*  258 */     this.names[2] = new RDN(1);
/*  259 */     this.names[2].assertion[0] = new AVA(localityName_oid, new DerValue(paramString4));
/*      */ 
/*  261 */     this.names[1] = new RDN(1);
/*  262 */     this.names[1].assertion[0] = new AVA(stateName_oid, new DerValue(paramString5));
/*      */ 
/*  264 */     this.names[0] = new RDN(1);
/*  265 */     this.names[0].assertion[0] = new AVA(countryName_oid, new DerValue(paramString6));
/*      */   }
/*      */ 
/*      */   public X500Name(RDN[] paramArrayOfRDN)
/*      */     throws IOException
/*      */   {
/*  276 */     if (paramArrayOfRDN == null) {
/*  277 */       this.names = new RDN[0];
/*      */     } else {
/*  279 */       this.names = ((RDN[])paramArrayOfRDN.clone());
/*  280 */       for (int i = 0; i < this.names.length; i++)
/*  281 */         if (this.names[i] == null)
/*  282 */           throw new IOException("Cannot create an X500Name");
/*      */     }
/*      */   }
/*      */ 
/*      */   public X500Name(DerValue paramDerValue)
/*      */     throws IOException
/*      */   {
/*  297 */     this(paramDerValue.toDerInputStream());
/*      */   }
/*      */ 
/*      */   public X500Name(DerInputStream paramDerInputStream)
/*      */     throws IOException
/*      */   {
/*  307 */     parseDER(paramDerInputStream);
/*      */   }
/*      */ 
/*      */   public X500Name(byte[] paramArrayOfByte)
/*      */     throws IOException
/*      */   {
/*  316 */     DerInputStream localDerInputStream = new DerInputStream(paramArrayOfByte);
/*  317 */     parseDER(localDerInputStream);
/*      */   }
/*      */ 
/*      */   public List<RDN> rdns()
/*      */   {
/*  324 */     List localList = this.rdnList;
/*  325 */     if (localList == null) {
/*  326 */       localList = Collections.unmodifiableList(Arrays.asList(this.names));
/*  327 */       this.rdnList = localList;
/*      */     }
/*  329 */     return localList;
/*      */   }
/*      */ 
/*      */   public int size()
/*      */   {
/*  336 */     return this.names.length;
/*      */   }
/*      */ 
/*      */   public List<AVA> allAvas()
/*      */   {
/*  344 */     Object localObject = this.allAvaList;
/*  345 */     if (localObject == null) {
/*  346 */       localObject = new ArrayList();
/*  347 */       for (int i = 0; i < this.names.length; i++) {
/*  348 */         ((List)localObject).addAll(this.names[i].avas());
/*      */       }
/*      */     }
/*  351 */     return localObject;
/*      */   }
/*      */ 
/*      */   public int avaSize()
/*      */   {
/*  359 */     return allAvas().size();
/*      */   }
/*      */ 
/*      */   public boolean isEmpty()
/*      */   {
/*  367 */     int i = this.names.length;
/*  368 */     if (i == 0) {
/*  369 */       return true;
/*      */     }
/*  371 */     for (int j = 0; j < i; j++) {
/*  372 */       if (this.names[j].assertion.length != 0) {
/*  373 */         return false;
/*      */       }
/*      */     }
/*  376 */     return true;
/*      */   }
/*      */ 
/*      */   public int hashCode()
/*      */   {
/*  384 */     return getRFC2253CanonicalName().hashCode();
/*      */   }
/*      */ 
/*      */   public boolean equals(Object paramObject)
/*      */   {
/*  393 */     if (this == paramObject) {
/*  394 */       return true;
/*      */     }
/*  396 */     if (!(paramObject instanceof X500Name)) {
/*  397 */       return false;
/*      */     }
/*  399 */     X500Name localX500Name = (X500Name)paramObject;
/*      */ 
/*  401 */     if ((this.canonicalDn != null) && (localX500Name.canonicalDn != null)) {
/*  402 */       return this.canonicalDn.equals(localX500Name.canonicalDn);
/*      */     }
/*      */ 
/*  405 */     int i = this.names.length;
/*  406 */     if (i != localX500Name.names.length) {
/*  407 */       return false;
/*      */     }
/*  409 */     for (int j = 0; j < i; j++) {
/*  410 */       localObject = this.names[j];
/*  411 */       RDN localRDN = localX500Name.names[j];
/*  412 */       if (((RDN)localObject).assertion.length != localRDN.assertion.length) {
/*  413 */         return false;
/*      */       }
/*      */     }
/*      */ 
/*  417 */     String str = getRFC2253CanonicalName();
/*  418 */     Object localObject = localX500Name.getRFC2253CanonicalName();
/*  419 */     return str.equals(localObject);
/*      */   }
/*      */ 
/*      */   private String getString(DerValue paramDerValue)
/*      */     throws IOException
/*      */   {
/*  427 */     if (paramDerValue == null)
/*  428 */       return null;
/*  429 */     String str = paramDerValue.getAsString();
/*      */ 
/*  431 */     if (str == null) {
/*  432 */       throw new IOException("not a DER string encoding, " + paramDerValue.tag);
/*      */     }
/*      */ 
/*  435 */     return str;
/*      */   }
/*      */ 
/*      */   public int getType()
/*      */   {
/*  442 */     return 4;
/*      */   }
/*      */ 
/*      */   public String getCountry()
/*      */     throws IOException
/*      */   {
/*  452 */     DerValue localDerValue = findAttribute(countryName_oid);
/*      */ 
/*  454 */     return getString(localDerValue);
/*      */   }
/*      */ 
/*      */   public String getOrganization()
/*      */     throws IOException
/*      */   {
/*  465 */     DerValue localDerValue = findAttribute(orgName_oid);
/*      */ 
/*  467 */     return getString(localDerValue);
/*      */   }
/*      */ 
/*      */   public String getOrganizationalUnit()
/*      */     throws IOException
/*      */   {
/*  478 */     DerValue localDerValue = findAttribute(orgUnitName_oid);
/*      */ 
/*  480 */     return getString(localDerValue);
/*      */   }
/*      */ 
/*      */   public String getCommonName()
/*      */     throws IOException
/*      */   {
/*  491 */     DerValue localDerValue = findAttribute(commonName_oid);
/*      */ 
/*  493 */     return getString(localDerValue);
/*      */   }
/*      */ 
/*      */   public String getLocality()
/*      */     throws IOException
/*      */   {
/*  504 */     DerValue localDerValue = findAttribute(localityName_oid);
/*      */ 
/*  506 */     return getString(localDerValue);
/*      */   }
/*      */ 
/*      */   public String getState()
/*      */     throws IOException
/*      */   {
/*  516 */     DerValue localDerValue = findAttribute(stateName_oid);
/*      */ 
/*  518 */     return getString(localDerValue);
/*      */   }
/*      */ 
/*      */   public String getDomain()
/*      */     throws IOException
/*      */   {
/*  528 */     DerValue localDerValue = findAttribute(DOMAIN_COMPONENT_OID);
/*      */ 
/*  530 */     return getString(localDerValue);
/*      */   }
/*      */ 
/*      */   public String getDNQualifier()
/*      */     throws IOException
/*      */   {
/*  540 */     DerValue localDerValue = findAttribute(DNQUALIFIER_OID);
/*      */ 
/*  542 */     return getString(localDerValue);
/*      */   }
/*      */ 
/*      */   public String getSurname()
/*      */     throws IOException
/*      */   {
/*  552 */     DerValue localDerValue = findAttribute(SURNAME_OID);
/*      */ 
/*  554 */     return getString(localDerValue);
/*      */   }
/*      */ 
/*      */   public String getGivenName()
/*      */     throws IOException
/*      */   {
/*  564 */     DerValue localDerValue = findAttribute(GIVENNAME_OID);
/*      */ 
/*  566 */     return getString(localDerValue);
/*      */   }
/*      */ 
/*      */   public String getInitials()
/*      */     throws IOException
/*      */   {
/*  576 */     DerValue localDerValue = findAttribute(INITIALS_OID);
/*      */ 
/*  578 */     return getString(localDerValue);
/*      */   }
/*      */ 
/*      */   public String getGeneration()
/*      */     throws IOException
/*      */   {
/*  588 */     DerValue localDerValue = findAttribute(GENERATIONQUALIFIER_OID);
/*      */ 
/*  590 */     return getString(localDerValue);
/*      */   }
/*      */ 
/*      */   public String getIP()
/*      */     throws IOException
/*      */   {
/*  600 */     DerValue localDerValue = findAttribute(ipAddress_oid);
/*      */ 
/*  602 */     return getString(localDerValue);
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/*  612 */     if (this.dn == null) {
/*  613 */       generateDN();
/*      */     }
/*  615 */     return this.dn;
/*      */   }
/*      */ 
/*      */   public String getRFC1779Name()
/*      */   {
/*  624 */     return getRFC1779Name(Collections.emptyMap());
/*      */   }
/*      */ 
/*      */   public String getRFC1779Name(Map<String, String> paramMap)
/*      */     throws IllegalArgumentException
/*      */   {
/*  635 */     if (paramMap.isEmpty())
/*      */     {
/*  637 */       if (this.rfc1779Dn != null) {
/*  638 */         return this.rfc1779Dn;
/*      */       }
/*  640 */       this.rfc1779Dn = generateRFC1779DN(paramMap);
/*  641 */       return this.rfc1779Dn;
/*      */     }
/*      */ 
/*  644 */     return generateRFC1779DN(paramMap);
/*      */   }
/*      */ 
/*      */   public String getRFC2253Name()
/*      */   {
/*  653 */     return getRFC2253Name(Collections.emptyMap());
/*      */   }
/*      */ 
/*      */   public String getRFC2253Name(Map<String, String> paramMap)
/*      */   {
/*  664 */     if (paramMap.isEmpty()) {
/*  665 */       if (this.rfc2253Dn != null) {
/*  666 */         return this.rfc2253Dn;
/*      */       }
/*  668 */       this.rfc2253Dn = generateRFC2253DN(paramMap);
/*  669 */       return this.rfc2253Dn;
/*      */     }
/*      */ 
/*  672 */     return generateRFC2253DN(paramMap);
/*      */   }
/*      */ 
/*      */   private String generateRFC2253DN(Map<String, String> paramMap)
/*      */   {
/*  680 */     if (this.names.length == 0) {
/*  681 */       return "";
/*      */     }
/*      */ 
/*  693 */     StringBuilder localStringBuilder = new StringBuilder(48);
/*  694 */     for (int i = this.names.length - 1; i >= 0; i--) {
/*  695 */       if (i < this.names.length - 1) {
/*  696 */         localStringBuilder.append(',');
/*      */       }
/*  698 */       localStringBuilder.append(this.names[i].toRFC2253String(paramMap));
/*      */     }
/*  700 */     return localStringBuilder.toString();
/*      */   }
/*      */ 
/*      */   public String getRFC2253CanonicalName()
/*      */   {
/*  705 */     if (this.canonicalDn != null) {
/*  706 */       return this.canonicalDn;
/*      */     }
/*      */ 
/*  712 */     if (this.names.length == 0) {
/*  713 */       this.canonicalDn = "";
/*  714 */       return this.canonicalDn;
/*      */     }
/*      */ 
/*  726 */     StringBuilder localStringBuilder = new StringBuilder(48);
/*  727 */     for (int i = this.names.length - 1; i >= 0; i--) {
/*  728 */       if (i < this.names.length - 1) {
/*  729 */         localStringBuilder.append(',');
/*      */       }
/*  731 */       localStringBuilder.append(this.names[i].toRFC2253String(true));
/*      */     }
/*  733 */     this.canonicalDn = localStringBuilder.toString();
/*  734 */     return this.canonicalDn;
/*      */   }
/*      */ 
/*      */   public String getName()
/*      */   {
/*  741 */     return toString();
/*      */   }
/*      */ 
/*      */   private DerValue findAttribute(ObjectIdentifier paramObjectIdentifier)
/*      */   {
/*  748 */     if (this.names != null) {
/*  749 */       for (int i = 0; i < this.names.length; i++) {
/*  750 */         DerValue localDerValue = this.names[i].findAttribute(paramObjectIdentifier);
/*  751 */         if (localDerValue != null) {
/*  752 */           return localDerValue;
/*      */         }
/*      */       }
/*      */     }
/*  756 */     return null;
/*      */   }
/*      */ 
/*      */   public DerValue findMostSpecificAttribute(ObjectIdentifier paramObjectIdentifier)
/*      */   {
/*  764 */     if (this.names != null) {
/*  765 */       for (int i = this.names.length - 1; i >= 0; i--) {
/*  766 */         DerValue localDerValue = this.names[i].findAttribute(paramObjectIdentifier);
/*  767 */         if (localDerValue != null) {
/*  768 */           return localDerValue;
/*      */         }
/*      */       }
/*      */     }
/*  772 */     return null;
/*      */   }
/*      */ 
/*      */   private void parseDER(DerInputStream paramDerInputStream)
/*      */     throws IOException
/*      */   {
/*  783 */     DerValue[] arrayOfDerValue = null;
/*  784 */     byte[] arrayOfByte = paramDerInputStream.toByteArray();
/*      */     try
/*      */     {
/*  787 */       arrayOfDerValue = paramDerInputStream.getSequence(5);
/*      */     } catch (IOException localIOException) {
/*  789 */       if (arrayOfByte == null) {
/*  790 */         arrayOfDerValue = null;
/*      */       } else {
/*  792 */         DerValue localDerValue = new DerValue((byte)48, arrayOfByte);
/*      */ 
/*  794 */         arrayOfByte = localDerValue.toByteArray();
/*  795 */         arrayOfDerValue = new DerInputStream(arrayOfByte).getSequence(5);
/*      */       }
/*      */     }
/*      */ 
/*  799 */     if (arrayOfDerValue == null) {
/*  800 */       this.names = new RDN[0];
/*      */     } else {
/*  802 */       this.names = new RDN[arrayOfDerValue.length];
/*  803 */       for (int i = 0; i < arrayOfDerValue.length; i++)
/*  804 */         this.names[i] = new RDN(arrayOfDerValue[i]);
/*      */     }
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public void emit(DerOutputStream paramDerOutputStream)
/*      */     throws IOException
/*      */   {
/*  817 */     encode(paramDerOutputStream);
/*      */   }
/*      */ 
/*      */   public void encode(DerOutputStream paramDerOutputStream)
/*      */     throws IOException
/*      */   {
/*  826 */     DerOutputStream localDerOutputStream = new DerOutputStream();
/*  827 */     for (int i = 0; i < this.names.length; i++) {
/*  828 */       this.names[i].encode(localDerOutputStream);
/*      */     }
/*  830 */     paramDerOutputStream.write((byte)48, localDerOutputStream);
/*      */   }
/*      */ 
/*      */   public byte[] getEncodedInternal()
/*      */     throws IOException
/*      */   {
/*  839 */     if (this.encoded == null) {
/*  840 */       DerOutputStream localDerOutputStream1 = new DerOutputStream();
/*  841 */       DerOutputStream localDerOutputStream2 = new DerOutputStream();
/*  842 */       for (int i = 0; i < this.names.length; i++) {
/*  843 */         this.names[i].encode(localDerOutputStream2);
/*      */       }
/*  845 */       localDerOutputStream1.write((byte)48, localDerOutputStream2);
/*  846 */       this.encoded = localDerOutputStream1.toByteArray();
/*      */     }
/*  848 */     return this.encoded;
/*      */   }
/*      */ 
/*      */   public byte[] getEncoded()
/*      */     throws IOException
/*      */   {
/*  857 */     return (byte[])getEncodedInternal().clone();
/*      */   }
/*      */ 
/*      */   private void parseDN(String paramString, Map<String, String> paramMap)
/*      */     throws IOException
/*      */   {
/*  875 */     if ((paramString == null) || (paramString.length() == 0)) {
/*  876 */       this.names = new RDN[0];
/*  877 */       return;
/*      */     }
/*      */ 
/*  880 */     ArrayList localArrayList = new ArrayList();
/*  881 */     int i = 0;
/*      */ 
/*  884 */     int k = 0;
/*      */ 
/*  886 */     String str2 = paramString;
/*      */ 
/*  888 */     int m = 0;
/*  889 */     int n = str2.indexOf(',');
/*  890 */     int i1 = str2.indexOf(';');
/*  891 */     while ((n >= 0) || (i1 >= 0))
/*      */     {
/*      */       int j;
/*  893 */       if (i1 < 0)
/*  894 */         j = n;
/*  895 */       else if (n < 0)
/*  896 */         j = i1;
/*      */       else {
/*  898 */         j = Math.min(n, i1);
/*      */       }
/*  900 */       k += countQuotes(str2, m, j);
/*      */ 
/*  909 */       if ((j >= 0) && (k != 1) && (!escaped(j, m, str2)))
/*      */       {
/*  915 */         str1 = str2.substring(i, j);
/*      */ 
/*  918 */         localRDN = new RDN(str1, paramMap);
/*  919 */         localArrayList.add(localRDN);
/*      */ 
/*  922 */         i = j + 1;
/*      */ 
/*  925 */         k = 0;
/*      */       }
/*      */ 
/*  928 */       m = j + 1;
/*  929 */       n = str2.indexOf(',', m);
/*  930 */       i1 = str2.indexOf(';', m);
/*      */     }
/*      */ 
/*  934 */     String str1 = str2.substring(i);
/*  935 */     RDN localRDN = new RDN(str1, paramMap);
/*  936 */     localArrayList.add(localRDN);
/*      */ 
/*  942 */     Collections.reverse(localArrayList);
/*  943 */     this.names = ((RDN[])localArrayList.toArray(new RDN[localArrayList.size()]));
/*      */   }
/*      */ 
/*      */   private void parseRFC2253DN(String paramString) throws IOException {
/*  947 */     if (paramString.length() == 0) {
/*  948 */       this.names = new RDN[0];
/*  949 */       return;
/*      */     }
/*      */ 
/*  952 */     ArrayList localArrayList = new ArrayList();
/*  953 */     int i = 0;
/*      */ 
/*  956 */     int j = 0;
/*  957 */     int k = paramString.indexOf(',');
/*  958 */     while (k >= 0)
/*      */     {
/*  966 */       if ((k > 0) && (!escaped(k, j, paramString)))
/*      */       {
/*  971 */         str = paramString.substring(i, k);
/*      */ 
/*  974 */         localRDN = new RDN(str, "RFC2253");
/*  975 */         localArrayList.add(localRDN);
/*      */ 
/*  978 */         i = k + 1;
/*      */       }
/*      */ 
/*  981 */       j = k + 1;
/*  982 */       k = paramString.indexOf(',', j);
/*      */     }
/*      */ 
/*  986 */     String str = paramString.substring(i);
/*  987 */     RDN localRDN = new RDN(str, "RFC2253");
/*  988 */     localArrayList.add(localRDN);
/*      */ 
/*  994 */     Collections.reverse(localArrayList);
/*  995 */     this.names = ((RDN[])localArrayList.toArray(new RDN[localArrayList.size()]));
/*      */   }
/*      */ 
/*      */   static int countQuotes(String paramString, int paramInt1, int paramInt2)
/*      */   {
/* 1003 */     int i = 0;
/*      */ 
/* 1005 */     for (int j = paramInt1; j < paramInt2; j++) {
/* 1006 */       if (((paramString.charAt(j) == '"') && (j == paramInt1)) || ((paramString.charAt(j) == '"') && (paramString.charAt(j - 1) != '\\')))
/*      */       {
/* 1008 */         i++;
/*      */       }
/*      */     }
/*      */ 
/* 1012 */     return i;
/*      */   }
/*      */ 
/*      */   private static boolean escaped(int paramInt1, int paramInt2, String paramString)
/*      */   {
/* 1018 */     if ((paramInt1 == 1) && (paramString.charAt(paramInt1 - 1) == '\\'))
/*      */     {
/* 1023 */       return true;
/*      */     }
/* 1025 */     if ((paramInt1 > 1) && (paramString.charAt(paramInt1 - 1) == '\\') && (paramString.charAt(paramInt1 - 2) != '\\'))
/*      */     {
/* 1031 */       return true;
/*      */     }
/* 1033 */     if ((paramInt1 > 1) && (paramString.charAt(paramInt1 - 1) == '\\') && (paramString.charAt(paramInt1 - 2) == '\\'))
/*      */     {
/* 1039 */       int i = 0;
/* 1040 */       paramInt1--;
/* 1041 */       while (paramInt1 >= paramInt2) {
/* 1042 */         if (paramString.charAt(paramInt1) == '\\') {
/* 1043 */           i++;
/*      */         }
/* 1045 */         paramInt1--;
/*      */       }
/*      */ 
/* 1049 */       return i % 2 != 0;
/*      */     }
/*      */ 
/* 1052 */     return false;
/*      */   }
/*      */ 
/*      */   private void generateDN()
/*      */   {
/* 1064 */     if (this.names.length == 1) {
/* 1065 */       this.dn = this.names[0].toString();
/* 1066 */       return;
/*      */     }
/*      */ 
/* 1069 */     StringBuilder localStringBuilder = new StringBuilder(48);
/* 1070 */     if (this.names != null) {
/* 1071 */       for (int i = this.names.length - 1; i >= 0; i--) {
/* 1072 */         if (i != this.names.length - 1) {
/* 1073 */           localStringBuilder.append(", ");
/*      */         }
/* 1075 */         localStringBuilder.append(this.names[i].toString());
/*      */       }
/*      */     }
/* 1078 */     this.dn = localStringBuilder.toString();
/*      */   }
/*      */ 
/*      */   private String generateRFC1779DN(Map<String, String> paramMap)
/*      */   {
/* 1091 */     if (this.names.length == 1) {
/* 1092 */       return this.names[0].toRFC1779String(paramMap);
/*      */     }
/*      */ 
/* 1095 */     StringBuilder localStringBuilder = new StringBuilder(48);
/* 1096 */     if (this.names != null) {
/* 1097 */       for (int i = this.names.length - 1; i >= 0; i--) {
/* 1098 */         if (i != this.names.length - 1) {
/* 1099 */           localStringBuilder.append(", ");
/*      */         }
/* 1101 */         localStringBuilder.append(this.names[i].toRFC1779String(paramMap));
/*      */       }
/*      */     }
/* 1104 */     return localStringBuilder.toString();
/*      */   }
/*      */ 
/*      */   static ObjectIdentifier intern(ObjectIdentifier paramObjectIdentifier)
/*      */   {
/* 1114 */     ObjectIdentifier localObjectIdentifier = (ObjectIdentifier)internedOIDs.get(paramObjectIdentifier);
/* 1115 */     if (localObjectIdentifier != null) {
/* 1116 */       return localObjectIdentifier;
/*      */     }
/* 1118 */     internedOIDs.put(paramObjectIdentifier, paramObjectIdentifier);
/* 1119 */     return paramObjectIdentifier;
/*      */   }
/*      */ 
/*      */   public int constrains(GeneralNameInterface paramGeneralNameInterface)
/*      */     throws UnsupportedOperationException
/*      */   {
/*      */     int i;
/* 1259 */     if (paramGeneralNameInterface == null) {
/* 1260 */       i = -1;
/* 1261 */     } else if (paramGeneralNameInterface.getType() != 4) {
/* 1262 */       i = -1;
/*      */     } else {
/* 1264 */       X500Name localX500Name = (X500Name)paramGeneralNameInterface;
/* 1265 */       if (localX500Name.equals(this))
/* 1266 */         i = 0;
/* 1267 */       else if (localX500Name.names.length == 0)
/* 1268 */         i = 2;
/* 1269 */       else if (this.names.length == 0)
/* 1270 */         i = 1;
/* 1271 */       else if (localX500Name.isWithinSubtree(this))
/* 1272 */         i = 1;
/* 1273 */       else if (isWithinSubtree(localX500Name))
/* 1274 */         i = 2;
/*      */       else {
/* 1276 */         i = 3;
/*      */       }
/*      */     }
/* 1279 */     return i;
/*      */   }
/*      */ 
/*      */   private boolean isWithinSubtree(X500Name paramX500Name)
/*      */   {
/* 1290 */     if (this == paramX500Name) {
/* 1291 */       return true;
/*      */     }
/* 1293 */     if (paramX500Name == null) {
/* 1294 */       return false;
/*      */     }
/* 1296 */     if (paramX500Name.names.length == 0) {
/* 1297 */       return true;
/*      */     }
/* 1299 */     if (this.names.length == 0) {
/* 1300 */       return false;
/*      */     }
/* 1302 */     if (this.names.length < paramX500Name.names.length) {
/* 1303 */       return false;
/*      */     }
/* 1305 */     for (int i = 0; i < paramX500Name.names.length; i++) {
/* 1306 */       if (!this.names[i].equals(paramX500Name.names[i])) {
/* 1307 */         return false;
/*      */       }
/*      */     }
/* 1310 */     return true;
/*      */   }
/*      */ 
/*      */   public int subtreeDepth()
/*      */     throws UnsupportedOperationException
/*      */   {
/* 1322 */     return this.names.length;
/*      */   }
/*      */ 
/*      */   public X500Name commonAncestor(X500Name paramX500Name)
/*      */   {
/* 1333 */     if (paramX500Name == null) {
/* 1334 */       return null;
/*      */     }
/* 1336 */     int i = paramX500Name.names.length;
/* 1337 */     int j = this.names.length;
/* 1338 */     if ((j == 0) || (i == 0)) {
/* 1339 */       return null;
/*      */     }
/* 1341 */     int k = j < i ? j : i;
/*      */ 
/* 1345 */     for (int m = 0; 
/* 1346 */       m < k; m++) {
/* 1347 */       if (!this.names[m].equals(paramX500Name.names[m])) {
/* 1348 */         if (m != 0) break;
/* 1349 */         return null;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1357 */     RDN[] arrayOfRDN = new RDN[m];
/* 1358 */     for (int n = 0; n < m; n++) {
/* 1359 */       arrayOfRDN[n] = this.names[n];
/*      */     }
/*      */ 
/* 1362 */     X500Name localX500Name = null;
/*      */     try {
/* 1364 */       localX500Name = new X500Name(arrayOfRDN);
/*      */     } catch (IOException localIOException) {
/* 1366 */       return null;
/*      */     }
/* 1368 */     return localX500Name;
/*      */   }
/*      */ 
/*      */   public X500Principal asX500Principal()
/*      */   {
/* 1415 */     if (this.x500Principal == null) {
/*      */       try {
/* 1417 */         Object[] arrayOfObject = { this };
/* 1418 */         this.x500Principal = ((X500Principal)principalConstructor.newInstance(arrayOfObject));
/*      */       }
/*      */       catch (Exception localException) {
/* 1421 */         throw new RuntimeException("Unexpected exception", localException);
/*      */       }
/*      */     }
/* 1424 */     return this.x500Principal;
/*      */   }
/*      */ 
/*      */   public static X500Name asX500Name(X500Principal paramX500Principal)
/*      */   {
/*      */     try
/*      */     {
/* 1434 */       X500Name localX500Name = (X500Name)principalField.get(paramX500Principal);
/* 1435 */       localX500Name.x500Principal = paramX500Principal;
/* 1436 */       return localX500Name;
/*      */     } catch (Exception localException) {
/* 1438 */       throw new RuntimeException("Unexpected exception", localException);
/*      */     }
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/* 1180 */     countryName_oid = intern(ObjectIdentifier.newInternal(countryName_data));
/*      */ 
/* 1183 */     localityName_oid = intern(ObjectIdentifier.newInternal(localityName_data));
/*      */ 
/* 1186 */     orgName_oid = intern(ObjectIdentifier.newInternal(orgName_data));
/*      */ 
/* 1189 */     orgUnitName_oid = intern(ObjectIdentifier.newInternal(orgUnitName_data));
/*      */ 
/* 1192 */     stateName_oid = intern(ObjectIdentifier.newInternal(stateName_data));
/*      */ 
/* 1195 */     streetAddress_oid = intern(ObjectIdentifier.newInternal(streetAddress_data));
/*      */ 
/* 1198 */     title_oid = intern(ObjectIdentifier.newInternal(title_data));
/*      */ 
/* 1202 */     DNQUALIFIER_OID = intern(ObjectIdentifier.newInternal(DNQUALIFIER_DATA));
/*      */ 
/* 1205 */     SURNAME_OID = intern(ObjectIdentifier.newInternal(SURNAME_DATA));
/*      */ 
/* 1208 */     GIVENNAME_OID = intern(ObjectIdentifier.newInternal(GIVENNAME_DATA));
/*      */ 
/* 1211 */     INITIALS_OID = intern(ObjectIdentifier.newInternal(INITIALS_DATA));
/*      */ 
/* 1214 */     GENERATIONQUALIFIER_OID = intern(ObjectIdentifier.newInternal(GENERATIONQUALIFIER_DATA));
/*      */ 
/* 1222 */     ipAddress_oid = intern(ObjectIdentifier.newInternal(ipAddress_data));
/*      */ 
/* 1232 */     DOMAIN_COMPONENT_OID = intern(ObjectIdentifier.newInternal(DOMAIN_COMPONENT_DATA));
/*      */ 
/* 1236 */     userid_oid = intern(ObjectIdentifier.newInternal(userid_data));
/*      */ 
/* 1386 */     PrivilegedExceptionAction local1 = new PrivilegedExceptionAction()
/*      */     {
/*      */       public Object[] run() throws Exception {
/* 1389 */         X500Principal localX500Principal = X500Principal.class;
/* 1390 */         Class[] arrayOfClass = { X500Name.class };
/* 1391 */         Constructor localConstructor = localX500Principal.getDeclaredConstructor(arrayOfClass);
/* 1392 */         localConstructor.setAccessible(true);
/* 1393 */         Field localField = localX500Principal.getDeclaredField("thisX500Name");
/* 1394 */         localField.setAccessible(true);
/* 1395 */         return new Object[] { localConstructor, localField };
/*      */       }
/*      */     };
/*      */     try {
/* 1399 */       Object[] arrayOfObject = (Object[])AccessController.doPrivileged(local1);
/* 1400 */       principalConstructor = (Constructor)arrayOfObject[0];
/* 1401 */       principalField = (Field)arrayOfObject[1];
/*      */     } catch (Exception localException) {
/* 1403 */       throw ((InternalError)new InternalError("Could not obtain X500Principal access").initCause(localException));
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.x509.X500Name
 * JD-Core Version:    0.6.2
 */