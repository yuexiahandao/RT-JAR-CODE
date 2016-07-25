/*     */ package sun.security.pkcs;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.security.cert.CertificateException;
/*     */ import java.util.Date;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Locale;
/*     */ import sun.misc.HexDumpEncoder;
/*     */ import sun.security.util.Debug;
/*     */ import sun.security.util.DerEncoder;
/*     */ import sun.security.util.DerInputStream;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.DerValue;
/*     */ import sun.security.util.ObjectIdentifier;
/*     */ import sun.security.x509.CertificateExtensions;
/*     */ 
/*     */ public class PKCS9Attribute
/*     */   implements DerEncoder
/*     */ {
/* 183 */   private static final Debug debug = Debug.getInstance("jar");
/*     */ 
/* 188 */   static final ObjectIdentifier[] PKCS9_OIDS = new ObjectIdentifier[18];
/*     */   private static final Class<?> BYTE_ARRAY_CLASS;
/*     */   public static final ObjectIdentifier EMAIL_ADDRESS_OID;
/*     */   public static final ObjectIdentifier UNSTRUCTURED_NAME_OID;
/*     */   public static final ObjectIdentifier CONTENT_TYPE_OID;
/*     */   public static final ObjectIdentifier MESSAGE_DIGEST_OID;
/*     */   public static final ObjectIdentifier SIGNING_TIME_OID;
/*     */   public static final ObjectIdentifier COUNTERSIGNATURE_OID;
/*     */   public static final ObjectIdentifier CHALLENGE_PASSWORD_OID;
/*     */   public static final ObjectIdentifier UNSTRUCTURED_ADDRESS_OID;
/*     */   public static final ObjectIdentifier EXTENDED_CERTIFICATE_ATTRIBUTES_OID;
/*     */   public static final ObjectIdentifier ISSUER_SERIALNUMBER_OID;
/*     */   public static final ObjectIdentifier EXTENSION_REQUEST_OID;
/*     */   public static final ObjectIdentifier SMIME_CAPABILITY_OID;
/*     */   public static final ObjectIdentifier SIGNING_CERTIFICATE_OID;
/*     */   public static final ObjectIdentifier SIGNATURE_TIMESTAMP_TOKEN_OID;
/*     */   public static final String EMAIL_ADDRESS_STR = "EmailAddress";
/*     */   public static final String UNSTRUCTURED_NAME_STR = "UnstructuredName";
/*     */   public static final String CONTENT_TYPE_STR = "ContentType";
/*     */   public static final String MESSAGE_DIGEST_STR = "MessageDigest";
/*     */   public static final String SIGNING_TIME_STR = "SigningTime";
/*     */   public static final String COUNTERSIGNATURE_STR = "Countersignature";
/*     */   public static final String CHALLENGE_PASSWORD_STR = "ChallengePassword";
/*     */   public static final String UNSTRUCTURED_ADDRESS_STR = "UnstructuredAddress";
/*     */   public static final String EXTENDED_CERTIFICATE_ATTRIBUTES_STR = "ExtendedCertificateAttributes";
/*     */   public static final String ISSUER_SERIALNUMBER_STR = "IssuerAndSerialNumber";
/*     */   private static final String RSA_PROPRIETARY_STR = "RSAProprietary";
/*     */   private static final String SMIME_SIGNING_DESC_STR = "SMIMESigningDesc";
/*     */   public static final String EXTENSION_REQUEST_STR = "ExtensionRequest";
/*     */   public static final String SMIME_CAPABILITY_STR = "SMIMECapability";
/*     */   public static final String SIGNING_CERTIFICATE_STR = "SigningCertificate";
/*     */   public static final String SIGNATURE_TIMESTAMP_TOKEN_STR = "SignatureTimestampToken";
/*     */   private static final Hashtable<String, ObjectIdentifier> NAME_OID_TABLE;
/*     */   private static final Hashtable<ObjectIdentifier, String> OID_NAME_TABLE;
/*     */   private static final Byte[][] PKCS9_VALUE_TAGS;
/*     */   private static final Class[] VALUE_CLASSES;
/* 370 */   private static final boolean[] SINGLE_VALUED = { false, false, false, true, true, true, false, true, false, false, true, false, false, false, true, true, true, true };
/*     */   private ObjectIdentifier oid;
/*     */   private int index;
/*     */   private Object value;
/*     */ 
/*     */   public PKCS9Attribute(ObjectIdentifier paramObjectIdentifier, Object paramObject)
/*     */     throws IllegalArgumentException
/*     */   {
/* 425 */     init(paramObjectIdentifier, paramObject);
/*     */   }
/*     */ 
/*     */   public PKCS9Attribute(String paramString, Object paramObject)
/*     */     throws IllegalArgumentException
/*     */   {
/* 446 */     ObjectIdentifier localObjectIdentifier = getOID(paramString);
/*     */ 
/* 448 */     if (localObjectIdentifier == null) {
/* 449 */       throw new IllegalArgumentException("Unrecognized attribute name " + paramString + " constructing PKCS9Attribute.");
/*     */     }
/*     */ 
/* 453 */     init(localObjectIdentifier, paramObject);
/*     */   }
/*     */ 
/*     */   private void init(ObjectIdentifier paramObjectIdentifier, Object paramObject)
/*     */     throws IllegalArgumentException
/*     */   {
/* 459 */     this.oid = paramObjectIdentifier;
/* 460 */     this.index = indexOf(paramObjectIdentifier, PKCS9_OIDS, 1);
/* 461 */     Class localClass = this.index == -1 ? BYTE_ARRAY_CLASS : VALUE_CLASSES[this.index];
/* 462 */     if (!localClass.isInstance(paramObject)) {
/* 463 */       throw new IllegalArgumentException("Wrong value class  for attribute " + paramObjectIdentifier + " constructing PKCS9Attribute; was " + paramObject.getClass().toString() + ", should be " + localClass.toString());
/*     */     }
/*     */ 
/* 470 */     this.value = paramObject;
/*     */   }
/*     */ 
/*     */   public PKCS9Attribute(DerValue paramDerValue)
/*     */     throws IOException
/*     */   {
/* 483 */     DerInputStream localDerInputStream = new DerInputStream(paramDerValue.toByteArray());
/* 484 */     DerValue[] arrayOfDerValue1 = localDerInputStream.getSequence(2);
/*     */ 
/* 486 */     if (localDerInputStream.available() != 0) {
/* 487 */       throw new IOException("Excess data parsing PKCS9Attribute");
/*     */     }
/* 489 */     if (arrayOfDerValue1.length != 2) {
/* 490 */       throw new IOException("PKCS9Attribute doesn't have two components");
/*     */     }
/*     */ 
/* 493 */     this.oid = arrayOfDerValue1[0].getOID();
/* 494 */     byte[] arrayOfByte = arrayOfDerValue1[1].toByteArray();
/* 495 */     DerValue[] arrayOfDerValue2 = new DerInputStream(arrayOfByte).getSet(1);
/*     */ 
/* 497 */     this.index = indexOf(this.oid, PKCS9_OIDS, 1);
/* 498 */     if (this.index == -1) {
/* 499 */       if (debug != null) {
/* 500 */         debug.println("Unsupported signer attribute: " + this.oid);
/*     */       }
/* 502 */       this.value = arrayOfByte;
/* 503 */       return;
/*     */     }
/*     */ 
/* 507 */     if ((SINGLE_VALUED[this.index] != 0) && (arrayOfDerValue2.length > 1)) {
/* 508 */       throwSingleValuedException();
/*     */     }
/*     */ 
/* 512 */     for (int i = 0; i < arrayOfDerValue2.length; i++) {
/* 513 */       Byte localByte = new Byte(arrayOfDerValue2[i].tag);
/*     */ 
/* 515 */       if (indexOf(localByte, PKCS9_VALUE_TAGS[this.index], 0) == -1)
/* 516 */         throwTagException(localByte);
/*     */     }
/*     */     Object localObject;
/*     */     int j;
/* 519 */     switch (this.index)
/*     */     {
/*     */     case 1:
/*     */     case 2:
/*     */     case 8:
/* 524 */       localObject = new String[arrayOfDerValue2.length];
/*     */ 
/* 526 */       for (j = 0; j < arrayOfDerValue2.length; j++)
/* 527 */         localObject[j] = arrayOfDerValue2[j].getAsString();
/* 528 */       this.value = localObject;
/*     */ 
/* 530 */       break;
/*     */     case 3:
/* 533 */       this.value = arrayOfDerValue2[0].getOID();
/* 534 */       break;
/*     */     case 4:
/* 537 */       this.value = arrayOfDerValue2[0].getOctetString();
/* 538 */       break;
/*     */     case 5:
/* 541 */       this.value = new DerInputStream(arrayOfDerValue2[0].toByteArray()).getUTCTime();
/* 542 */       break;
/*     */     case 6:
/* 546 */       localObject = new SignerInfo[arrayOfDerValue2.length];
/* 547 */       for (j = 0; j < arrayOfDerValue2.length; j++) {
/* 548 */         localObject[j] = new SignerInfo(arrayOfDerValue2[j].toDerInputStream());
/*     */       }
/* 550 */       this.value = localObject;
/*     */ 
/* 552 */       break;
/*     */     case 7:
/* 555 */       this.value = arrayOfDerValue2[0].getAsString();
/* 556 */       break;
/*     */     case 9:
/* 559 */       throw new IOException("PKCS9 extended-certificate attribute not supported.");
/*     */     case 10:
/* 563 */       throw new IOException("PKCS9 IssuerAndSerialNumberattribute not supported.");
/*     */     case 11:
/*     */     case 12:
/* 568 */       throw new IOException("PKCS9 RSA DSI attributes11 and 12, not supported.");
/*     */     case 13:
/* 572 */       throw new IOException("PKCS9 attribute #13 not supported.");
/*     */     case 14:
/* 576 */       this.value = new CertificateExtensions(new DerInputStream(arrayOfDerValue2[0].toByteArray()));
/*     */ 
/* 578 */       break;
/*     */     case 15:
/* 581 */       throw new IOException("PKCS9 SMIMECapability attribute not supported.");
/*     */     case 16:
/* 585 */       this.value = new SigningCertificateInfo(arrayOfDerValue2[0].toByteArray());
/* 586 */       break;
/*     */     case 17:
/* 589 */       this.value = arrayOfDerValue2[0].toByteArray();
/* 590 */       break;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void derEncode(OutputStream paramOutputStream)
/*     */     throws IOException
/*     */   {
/* 604 */     DerOutputStream localDerOutputStream = new DerOutputStream();
/* 605 */     localDerOutputStream.putOID(this.oid);
/*     */     Object localObject2;
/*     */     int i;
/* 606 */     switch (this.index) {
/*     */     case -1:
/* 608 */       localDerOutputStream.write((byte[])this.value);
/* 609 */       break;
/*     */     case 1:
/*     */     case 2:
/* 613 */       localObject1 = (String[])this.value;
/* 614 */       localObject2 = new DerOutputStream[localObject1.length];
/*     */ 
/* 617 */       for (i = 0; i < localObject1.length; i++) {
/* 618 */         localObject2[i] = new DerOutputStream();
/* 619 */         localObject2[i].putIA5String(localObject1[i]);
/*     */       }
/* 621 */       localDerOutputStream.putOrderedSetOf((byte)49, (DerEncoder[])localObject2);
/*     */ 
/* 623 */       break;
/*     */     case 3:
/* 627 */       localObject1 = new DerOutputStream();
/* 628 */       ((DerOutputStream)localObject1).putOID((ObjectIdentifier)this.value);
/* 629 */       localDerOutputStream.write((byte)49, ((DerOutputStream)localObject1).toByteArray());
/*     */ 
/* 631 */       break;
/*     */     case 4:
/* 635 */       localObject1 = new DerOutputStream();
/* 636 */       ((DerOutputStream)localObject1).putOctetString((byte[])this.value);
/* 637 */       localDerOutputStream.write((byte)49, ((DerOutputStream)localObject1).toByteArray());
/*     */ 
/* 639 */       break;
/*     */     case 5:
/* 643 */       localObject1 = new DerOutputStream();
/* 644 */       ((DerOutputStream)localObject1).putUTCTime((Date)this.value);
/* 645 */       localDerOutputStream.write((byte)49, ((DerOutputStream)localObject1).toByteArray());
/*     */ 
/* 647 */       break;
/*     */     case 6:
/* 650 */       localDerOutputStream.putOrderedSetOf((byte)49, (DerEncoder[])this.value);
/* 651 */       break;
/*     */     case 7:
/* 655 */       localObject1 = new DerOutputStream();
/* 656 */       ((DerOutputStream)localObject1).putPrintableString((String)this.value);
/* 657 */       localDerOutputStream.write((byte)49, ((DerOutputStream)localObject1).toByteArray());
/*     */ 
/* 659 */       break;
/*     */     case 8:
/* 663 */       localObject1 = (String[])this.value;
/* 664 */       localObject2 = new DerOutputStream[localObject1.length];
/*     */ 
/* 667 */       for (i = 0; i < localObject1.length; i++) {
/* 668 */         localObject2[i] = new DerOutputStream();
/* 669 */         localObject2[i].putPrintableString(localObject1[i]);
/*     */       }
/* 671 */       localDerOutputStream.putOrderedSetOf((byte)49, (DerEncoder[])localObject2);
/*     */ 
/* 673 */       break;
/*     */     case 9:
/* 676 */       throw new IOException("PKCS9 extended-certificate attribute not supported.");
/*     */     case 10:
/* 680 */       throw new IOException("PKCS9 IssuerAndSerialNumberattribute not supported.");
/*     */     case 11:
/*     */     case 12:
/* 685 */       throw new IOException("PKCS9 RSA DSI attributes11 and 12, not supported.");
/*     */     case 13:
/* 689 */       throw new IOException("PKCS9 attribute #13 not supported.");
/*     */     case 14:
/* 694 */       localObject1 = new DerOutputStream();
/* 695 */       localObject2 = (CertificateExtensions)this.value;
/*     */       try {
/* 697 */         ((CertificateExtensions)localObject2).encode((OutputStream)localObject1, true);
/*     */       } catch (CertificateException localCertificateException) {
/* 699 */         throw new IOException(localCertificateException.toString());
/*     */       }
/* 701 */       localDerOutputStream.write((byte)49, ((DerOutputStream)localObject1).toByteArray());
/*     */ 
/* 703 */       break;
/*     */     case 15:
/* 705 */       throw new IOException("PKCS9 attribute #15 not supported.");
/*     */     case 16:
/* 709 */       throw new IOException("PKCS9 SigningCertificate attribute not supported.");
/*     */     case 17:
/* 714 */       localDerOutputStream.write((byte)49, (byte[])this.value);
/* 715 */       break;
/*     */     case 0:
/*     */     }
/*     */ 
/* 720 */     Object localObject1 = new DerOutputStream();
/* 721 */     ((DerOutputStream)localObject1).write((byte)48, localDerOutputStream.toByteArray());
/*     */ 
/* 723 */     paramOutputStream.write(((DerOutputStream)localObject1).toByteArray());
/*     */   }
/*     */ 
/*     */   public boolean isKnown()
/*     */   {
/* 732 */     return this.index != -1;
/*     */   }
/*     */ 
/*     */   public Object getValue()
/*     */   {
/* 746 */     return this.value;
/*     */   }
/*     */ 
/*     */   public boolean isSingleValued()
/*     */   {
/* 753 */     return (this.index == -1) || (SINGLE_VALUED[this.index] != 0);
/*     */   }
/*     */ 
/*     */   public ObjectIdentifier getOID()
/*     */   {
/* 760 */     return this.oid;
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 767 */     return this.index == -1 ? this.oid.toString() : (String)OID_NAME_TABLE.get(PKCS9_OIDS[this.index]);
/*     */   }
/*     */ 
/*     */   public static ObjectIdentifier getOID(String paramString)
/*     */   {
/* 777 */     return (ObjectIdentifier)NAME_OID_TABLE.get(paramString.toLowerCase(Locale.ENGLISH));
/*     */   }
/*     */ 
/*     */   public static String getName(ObjectIdentifier paramObjectIdentifier)
/*     */   {
/* 785 */     return (String)OID_NAME_TABLE.get(paramObjectIdentifier);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 792 */     StringBuffer localStringBuffer = new StringBuffer(100);
/*     */ 
/* 794 */     localStringBuffer.append("[");
/*     */ 
/* 796 */     if (this.index == -1)
/* 797 */       localStringBuffer.append(this.oid.toString());
/*     */     else {
/* 799 */       localStringBuffer.append((String)OID_NAME_TABLE.get(PKCS9_OIDS[this.index]));
/*     */     }
/* 801 */     localStringBuffer.append(": ");
/*     */ 
/* 803 */     if ((this.index == -1) || (SINGLE_VALUED[this.index] != 0)) {
/* 804 */       if ((this.value instanceof byte[])) {
/* 805 */         HexDumpEncoder localHexDumpEncoder = new HexDumpEncoder();
/* 806 */         localStringBuffer.append(localHexDumpEncoder.encodeBuffer((byte[])this.value));
/*     */       } else {
/* 808 */         localStringBuffer.append(this.value.toString());
/*     */       }
/* 810 */       localStringBuffer.append("]");
/* 811 */       return localStringBuffer.toString();
/*     */     }
/* 813 */     int i = 1;
/* 814 */     Object[] arrayOfObject = (Object[])this.value;
/*     */ 
/* 816 */     for (int j = 0; j < arrayOfObject.length; j++) {
/* 817 */       if (i != 0)
/* 818 */         i = 0;
/*     */       else {
/* 820 */         localStringBuffer.append(", ");
/*     */       }
/* 822 */       localStringBuffer.append(arrayOfObject[j].toString());
/*     */     }
/* 824 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   static int indexOf(Object paramObject, Object[] paramArrayOfObject, int paramInt)
/*     */   {
/* 835 */     for (int i = paramInt; i < paramArrayOfObject.length; i++) {
/* 836 */       if (paramObject.equals(paramArrayOfObject[i])) return i;
/*     */     }
/* 838 */     return -1;
/*     */   }
/*     */ 
/*     */   private void throwSingleValuedException()
/*     */     throws IOException
/*     */   {
/* 846 */     throw new IOException("Single-value attribute " + this.oid + " (" + getName() + ")" + " has multiple values.");
/*     */   }
/*     */ 
/*     */   private void throwTagException(Byte paramByte)
/*     */     throws IOException
/*     */   {
/* 858 */     Byte[] arrayOfByte = PKCS9_VALUE_TAGS[this.index];
/* 859 */     StringBuffer localStringBuffer = new StringBuffer(100);
/* 860 */     localStringBuffer.append("Value of attribute ");
/* 861 */     localStringBuffer.append(this.oid.toString());
/* 862 */     localStringBuffer.append(" (");
/* 863 */     localStringBuffer.append(getName());
/* 864 */     localStringBuffer.append(") has wrong tag: ");
/* 865 */     localStringBuffer.append(paramByte.toString());
/* 866 */     localStringBuffer.append(".  Expected tags: ");
/*     */ 
/* 868 */     localStringBuffer.append(arrayOfByte[0].toString());
/*     */ 
/* 870 */     for (int i = 1; i < arrayOfByte.length; i++) {
/* 871 */       localStringBuffer.append(", ");
/* 872 */       localStringBuffer.append(arrayOfByte[i].toString());
/*     */     }
/* 874 */     localStringBuffer.append(".");
/* 875 */     throw new IOException(localStringBuffer.toString());
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 193 */     for (int i = 1; i < PKCS9_OIDS.length - 2; i++) {
/* 194 */       PKCS9_OIDS[i] = ObjectIdentifier.newInternal(new int[] { 1, 2, 840, 113549, 1, 9, i });
/*     */     }
/*     */ 
/* 199 */     PKCS9_OIDS[(PKCS9_OIDS.length - 2)] = ObjectIdentifier.newInternal(new int[] { 1, 2, 840, 113549, 1, 9, 16, 2, 12 });
/*     */ 
/* 201 */     PKCS9_OIDS[(PKCS9_OIDS.length - 1)] = ObjectIdentifier.newInternal(new int[] { 1, 2, 840, 113549, 1, 9, 16, 2, 14 });
/*     */     try
/*     */     {
/* 205 */       BYTE_ARRAY_CLASS = Class.forName("[B");
/*     */     } catch (ClassNotFoundException localClassNotFoundException1) {
/* 207 */       throw new ExceptionInInitializerError(localClassNotFoundException1.toString());
/*     */     }
/*     */ 
/* 212 */     EMAIL_ADDRESS_OID = PKCS9_OIDS[1];
/* 213 */     UNSTRUCTURED_NAME_OID = PKCS9_OIDS[2];
/* 214 */     CONTENT_TYPE_OID = PKCS9_OIDS[3];
/* 215 */     MESSAGE_DIGEST_OID = PKCS9_OIDS[4];
/* 216 */     SIGNING_TIME_OID = PKCS9_OIDS[5];
/* 217 */     COUNTERSIGNATURE_OID = PKCS9_OIDS[6];
/* 218 */     CHALLENGE_PASSWORD_OID = PKCS9_OIDS[7];
/* 219 */     UNSTRUCTURED_ADDRESS_OID = PKCS9_OIDS[8];
/* 220 */     EXTENDED_CERTIFICATE_ATTRIBUTES_OID = PKCS9_OIDS[9];
/*     */ 
/* 222 */     ISSUER_SERIALNUMBER_OID = PKCS9_OIDS[10];
/*     */ 
/* 225 */     EXTENSION_REQUEST_OID = PKCS9_OIDS[14];
/* 226 */     SMIME_CAPABILITY_OID = PKCS9_OIDS[15];
/* 227 */     SIGNING_CERTIFICATE_OID = PKCS9_OIDS[16];
/* 228 */     SIGNATURE_TIMESTAMP_TOKEN_OID = PKCS9_OIDS[17];
/*     */ 
/* 256 */     NAME_OID_TABLE = new Hashtable(18);
/*     */ 
/* 260 */     NAME_OID_TABLE.put("emailaddress", PKCS9_OIDS[1]);
/* 261 */     NAME_OID_TABLE.put("unstructuredname", PKCS9_OIDS[2]);
/* 262 */     NAME_OID_TABLE.put("contenttype", PKCS9_OIDS[3]);
/* 263 */     NAME_OID_TABLE.put("messagedigest", PKCS9_OIDS[4]);
/* 264 */     NAME_OID_TABLE.put("signingtime", PKCS9_OIDS[5]);
/* 265 */     NAME_OID_TABLE.put("countersignature", PKCS9_OIDS[6]);
/* 266 */     NAME_OID_TABLE.put("challengepassword", PKCS9_OIDS[7]);
/* 267 */     NAME_OID_TABLE.put("unstructuredaddress", PKCS9_OIDS[8]);
/* 268 */     NAME_OID_TABLE.put("extendedcertificateattributes", PKCS9_OIDS[9]);
/* 269 */     NAME_OID_TABLE.put("issuerandserialnumber", PKCS9_OIDS[10]);
/* 270 */     NAME_OID_TABLE.put("rsaproprietary", PKCS9_OIDS[11]);
/* 271 */     NAME_OID_TABLE.put("rsaproprietary", PKCS9_OIDS[12]);
/* 272 */     NAME_OID_TABLE.put("signingdescription", PKCS9_OIDS[13]);
/* 273 */     NAME_OID_TABLE.put("extensionrequest", PKCS9_OIDS[14]);
/* 274 */     NAME_OID_TABLE.put("smimecapability", PKCS9_OIDS[15]);
/* 275 */     NAME_OID_TABLE.put("signingcertificate", PKCS9_OIDS[16]);
/* 276 */     NAME_OID_TABLE.put("signaturetimestamptoken", PKCS9_OIDS[17]);
/*     */ 
/* 283 */     OID_NAME_TABLE = new Hashtable(16);
/*     */ 
/* 286 */     OID_NAME_TABLE.put(PKCS9_OIDS[1], "EmailAddress");
/* 287 */     OID_NAME_TABLE.put(PKCS9_OIDS[2], "UnstructuredName");
/* 288 */     OID_NAME_TABLE.put(PKCS9_OIDS[3], "ContentType");
/* 289 */     OID_NAME_TABLE.put(PKCS9_OIDS[4], "MessageDigest");
/* 290 */     OID_NAME_TABLE.put(PKCS9_OIDS[5], "SigningTime");
/* 291 */     OID_NAME_TABLE.put(PKCS9_OIDS[6], "Countersignature");
/* 292 */     OID_NAME_TABLE.put(PKCS9_OIDS[7], "ChallengePassword");
/* 293 */     OID_NAME_TABLE.put(PKCS9_OIDS[8], "UnstructuredAddress");
/* 294 */     OID_NAME_TABLE.put(PKCS9_OIDS[9], "ExtendedCertificateAttributes");
/* 295 */     OID_NAME_TABLE.put(PKCS9_OIDS[10], "IssuerAndSerialNumber");
/* 296 */     OID_NAME_TABLE.put(PKCS9_OIDS[11], "RSAProprietary");
/* 297 */     OID_NAME_TABLE.put(PKCS9_OIDS[12], "RSAProprietary");
/* 298 */     OID_NAME_TABLE.put(PKCS9_OIDS[13], "SMIMESigningDesc");
/* 299 */     OID_NAME_TABLE.put(PKCS9_OIDS[14], "ExtensionRequest");
/* 300 */     OID_NAME_TABLE.put(PKCS9_OIDS[15], "SMIMECapability");
/* 301 */     OID_NAME_TABLE.put(PKCS9_OIDS[16], "SigningCertificate");
/* 302 */     OID_NAME_TABLE.put(PKCS9_OIDS[17], "SignatureTimestampToken");
/*     */ 
/* 310 */     PKCS9_VALUE_TAGS = new Byte[][] { null, { new Byte(22) }, { new Byte(22) }, { new Byte(6) }, { new Byte(4) }, { new Byte(23) }, { new Byte(48) }, { new Byte(19), new Byte(20) }, { new Byte(19), new Byte(20) }, { new Byte(49) }, { new Byte(48) }, null, null, null, { new Byte(48) }, { new Byte(48) }, { new Byte(48) }, { new Byte(48) } };
/*     */ 
/* 333 */     VALUE_CLASSES = new Class[18];
/*     */     try
/*     */     {
/* 337 */       Class localClass = Class.forName("[Ljava.lang.String;");
/*     */ 
/* 339 */       VALUE_CLASSES[0] = null;
/* 340 */       VALUE_CLASSES[1] = localClass;
/* 341 */       VALUE_CLASSES[2] = localClass;
/* 342 */       VALUE_CLASSES[3] = Class.forName("sun.security.util.ObjectIdentifier");
/*     */ 
/* 344 */       VALUE_CLASSES[4] = BYTE_ARRAY_CLASS;
/* 345 */       VALUE_CLASSES[5] = Class.forName("java.util.Date");
/* 346 */       VALUE_CLASSES[6] = Class.forName("[Lsun.security.pkcs.SignerInfo;");
/*     */ 
/* 348 */       VALUE_CLASSES[7] = Class.forName("java.lang.String");
/*     */ 
/* 350 */       VALUE_CLASSES[8] = localClass;
/* 351 */       VALUE_CLASSES[9] = null;
/* 352 */       VALUE_CLASSES[10] = null;
/* 353 */       VALUE_CLASSES[11] = null;
/* 354 */       VALUE_CLASSES[12] = null;
/* 355 */       VALUE_CLASSES[13] = null;
/* 356 */       VALUE_CLASSES[14] = Class.forName("sun.security.x509.CertificateExtensions");
/*     */ 
/* 358 */       VALUE_CLASSES[15] = null;
/* 359 */       VALUE_CLASSES[16] = null;
/* 360 */       VALUE_CLASSES[17] = BYTE_ARRAY_CLASS;
/*     */     } catch (ClassNotFoundException localClassNotFoundException2) {
/* 362 */       throw new ExceptionInInitializerError(localClassNotFoundException2.toString());
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.pkcs.PKCS9Attribute
 * JD-Core Version:    0.6.2
 */