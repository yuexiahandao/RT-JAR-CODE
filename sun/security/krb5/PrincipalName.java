/*     */ package sun.security.krb5;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.math.BigInteger;
/*     */ import java.net.InetAddress;
/*     */ import java.net.UnknownHostException;
/*     */ import java.util.Vector;
/*     */ import sun.security.krb5.internal.ccache.CCacheOutputStream;
/*     */ import sun.security.krb5.internal.util.KerberosString;
/*     */ import sun.security.util.DerInputStream;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.DerValue;
/*     */ 
/*     */ public class PrincipalName
/*     */   implements Cloneable
/*     */ {
/*     */   public static final int KRB_NT_UNKNOWN = 0;
/*     */   public static final int KRB_NT_PRINCIPAL = 1;
/*     */   public static final int KRB_NT_SRV_INST = 2;
/*     */   public static final int KRB_NT_SRV_HST = 3;
/*     */   public static final int KRB_NT_SRV_XHST = 4;
/*     */   public static final int KRB_NT_UID = 5;
/*     */   public static final String TGS_DEFAULT_SRV_NAME = "krbtgt";
/*     */   public static final int TGS_DEFAULT_NT = 2;
/*     */   public static final char NAME_COMPONENT_SEPARATOR = '/';
/*     */   public static final char NAME_REALM_SEPARATOR = '@';
/*     */   public static final char REALM_COMPONENT_SEPARATOR = '.';
/*     */   public static final String NAME_COMPONENT_SEPARATOR_STR = "/";
/*     */   public static final String NAME_REALM_SEPARATOR_STR = "@";
/*     */   public static final String REALM_COMPONENT_SEPARATOR_STR = ".";
/*     */   private int nameType;
/*     */   private String[] nameStrings;
/*     */   private Realm nameRealm;
/* 105 */   private String salt = null;
/*     */ 
/*     */   protected PrincipalName()
/*     */   {
/*     */   }
/*     */ 
/*     */   public PrincipalName(String[] paramArrayOfString, int paramInt) throws IllegalArgumentException, IOException {
/* 112 */     if (paramArrayOfString == null) {
/* 113 */       throw new IllegalArgumentException("Null input not allowed");
/*     */     }
/* 115 */     this.nameStrings = new String[paramArrayOfString.length];
/* 116 */     System.arraycopy(paramArrayOfString, 0, this.nameStrings, 0, paramArrayOfString.length);
/* 117 */     this.nameType = paramInt;
/* 118 */     this.nameRealm = null;
/*     */   }
/*     */ 
/*     */   public PrincipalName(String[] paramArrayOfString) throws IOException {
/* 122 */     this(paramArrayOfString, 0);
/*     */   }
/*     */ 
/*     */   public Object clone() {
/*     */     try {
/* 127 */       PrincipalName localPrincipalName = (PrincipalName)super.clone();
/*     */ 
/* 129 */       if (this.nameStrings != null) {
/* 130 */         localPrincipalName.nameStrings = ((String[])this.nameStrings.clone());
/*     */       }
/* 132 */       if (this.nameRealm != null) {
/* 133 */         localPrincipalName.nameRealm = ((Realm)this.nameRealm.clone());
/*     */       }
/* 135 */       return localPrincipalName; } catch (CloneNotSupportedException localCloneNotSupportedException) {
/*     */     }
/* 137 */     throw new AssertionError("Should never happen");
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 147 */     if ((paramObject instanceof PrincipalName)) {
/* 148 */       return equals((PrincipalName)paramObject);
/*     */     }
/* 150 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean equals(PrincipalName paramPrincipalName)
/*     */   {
/* 156 */     if (!equalsWithoutRealm(paramPrincipalName)) {
/* 157 */       return false;
/*     */     }
/*     */ 
/* 160 */     if (((this.nameRealm != null) && (paramPrincipalName.nameRealm == null)) || ((this.nameRealm == null) && (paramPrincipalName.nameRealm != null)))
/*     */     {
/* 162 */       return false;
/*     */     }
/*     */ 
/* 165 */     if ((this.nameRealm != null) && (paramPrincipalName.nameRealm != null) && 
/* 166 */       (!this.nameRealm.equals(paramPrincipalName.nameRealm))) {
/* 167 */       return false;
/*     */     }
/*     */ 
/* 171 */     return true;
/*     */   }
/*     */ 
/*     */   boolean equalsWithoutRealm(PrincipalName paramPrincipalName)
/*     */   {
/* 176 */     if (((this.nameStrings != null) && (paramPrincipalName.nameStrings == null)) || ((this.nameStrings == null) && (paramPrincipalName.nameStrings != null)))
/*     */     {
/* 178 */       return false;
/*     */     }
/* 180 */     if ((this.nameStrings != null) && (paramPrincipalName.nameStrings != null)) {
/* 181 */       if (this.nameStrings.length != paramPrincipalName.nameStrings.length)
/* 182 */         return false;
/* 183 */       for (int i = 0; i < this.nameStrings.length; i++) {
/* 184 */         if (!this.nameStrings[i].equals(paramPrincipalName.nameStrings[i]))
/* 185 */           return false;
/*     */       }
/*     */     }
/* 188 */     return true;
/*     */   }
/*     */ 
/*     */   public PrincipalName(DerValue paramDerValue)
/*     */     throws Asn1Exception, IOException
/*     */   {
/* 220 */     this.nameRealm = null;
/*     */ 
/* 222 */     if (paramDerValue == null) {
/* 223 */       throw new IllegalArgumentException("Null input not allowed");
/*     */     }
/* 225 */     if (paramDerValue.getTag() != 48) {
/* 226 */       throw new Asn1Exception(906);
/*     */     }
/* 228 */     DerValue localDerValue1 = paramDerValue.getData().getDerValue();
/*     */     Object localObject;
/* 229 */     if ((localDerValue1.getTag() & 0x1F) == 0) {
/* 230 */       localObject = localDerValue1.getData().getBigInteger();
/* 231 */       this.nameType = ((BigInteger)localObject).intValue();
/*     */     } else {
/* 233 */       throw new Asn1Exception(906);
/*     */     }
/* 235 */     localDerValue1 = paramDerValue.getData().getDerValue();
/* 236 */     if ((localDerValue1.getTag() & 0x1F) == 1) {
/* 237 */       localObject = localDerValue1.getData().getDerValue();
/* 238 */       if (((DerValue)localObject).getTag() != 48) {
/* 239 */         throw new Asn1Exception(906);
/*     */       }
/* 241 */       Vector localVector = new Vector();
/*     */ 
/* 243 */       while (((DerValue)localObject).getData().available() > 0) {
/* 244 */         DerValue localDerValue2 = ((DerValue)localObject).getData().getDerValue();
/* 245 */         localVector.addElement(new KerberosString(localDerValue2).toString());
/*     */       }
/* 247 */       if (localVector.size() > 0) {
/* 248 */         this.nameStrings = new String[localVector.size()];
/* 249 */         localVector.copyInto(this.nameStrings);
/*     */       } else {
/* 251 */         this.nameStrings = new String[] { "" };
/*     */       }
/*     */     } else {
/* 254 */       throw new Asn1Exception(906);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static PrincipalName parse(DerInputStream paramDerInputStream, byte paramByte, boolean paramBoolean)
/*     */     throws Asn1Exception, IOException
/*     */   {
/* 277 */     if ((paramBoolean) && (((byte)paramDerInputStream.peekByte() & 0x1F) != paramByte))
/*     */     {
/* 279 */       return null;
/* 280 */     }DerValue localDerValue1 = paramDerInputStream.getDerValue();
/* 281 */     if (paramByte != (localDerValue1.getTag() & 0x1F)) {
/* 282 */       throw new Asn1Exception(906);
/*     */     }
/* 284 */     DerValue localDerValue2 = localDerValue1.getData().getDerValue();
/* 285 */     return new PrincipalName(localDerValue2);
/*     */   }
/*     */ 
/*     */   protected static String[] parseName(String paramString)
/*     */   {
/* 296 */     Vector localVector = new Vector();
/* 297 */     String str1 = paramString;
/* 298 */     int i = 0;
/* 299 */     int j = 0;
/*     */     String str2;
/* 302 */     while (i < str1.length()) {
/* 303 */       if (str1.charAt(i) == '/')
/*     */       {
/* 308 */         if ((i > 0) && (str1.charAt(i - 1) == '\\')) {
/* 309 */           str1 = str1.substring(0, i - 1) + str1.substring(i, str1.length());
/*     */         }
/*     */         else
/*     */         {
/* 314 */           if (j < i) {
/* 315 */             str2 = str1.substring(j, i);
/* 316 */             localVector.addElement(str2);
/*     */           }
/* 318 */           j = i + 1;
/*     */         }
/*     */       }
/* 321 */       else if (str1.charAt(i) == '@')
/*     */       {
/* 326 */         if ((i > 0) && (str1.charAt(i - 1) == '\\')) {
/* 327 */           str1 = str1.substring(0, i - 1) + str1.substring(i, str1.length());
/*     */         }
/*     */         else
/*     */         {
/* 331 */           if (j < i) {
/* 332 */             str2 = str1.substring(j, i);
/* 333 */             localVector.addElement(str2);
/*     */           }
/* 335 */           j = i + 1;
/* 336 */           break;
/*     */         }
/*     */       }
/* 339 */       else i++;
/*     */     }
/*     */ 
/* 342 */     if ((i == str1.length()) && 
/* 343 */       (j < i)) {
/* 344 */       str2 = str1.substring(j, i);
/* 345 */       localVector.addElement(str2);
/*     */     }
/*     */ 
/* 348 */     String[] arrayOfString = new String[localVector.size()];
/* 349 */     localVector.copyInto(arrayOfString);
/* 350 */     return arrayOfString;
/*     */   }
/*     */ 
/*     */   public PrincipalName(String paramString, int paramInt) throws RealmException
/*     */   {
/* 355 */     if (paramString == null) {
/* 356 */       throw new IllegalArgumentException("Null name not allowed");
/*     */     }
/* 358 */     String[] arrayOfString = parseName(paramString);
/* 359 */     Realm localRealm = null;
/* 360 */     String str = Realm.parseRealmAtSeparator(paramString);
/*     */     Object localObject2;
/* 362 */     if (str == null) {
/*     */       try {
/* 364 */         Config localConfig = Config.getInstance();
/* 365 */         str = localConfig.getDefaultRealm();
/*     */       } catch (KrbException localKrbException) {
/* 367 */         localObject2 = new RealmException(localKrbException.getMessage());
/*     */ 
/* 369 */         ((RealmException)localObject2).initCause(localKrbException);
/* 370 */         throw ((Throwable)localObject2);
/*     */       }
/*     */     }
/*     */ 
/* 374 */     if (str != null) {
/* 375 */       localRealm = new Realm(str);
/*     */     }
/* 377 */     switch (paramInt) {
/*     */     case 3:
/* 379 */       if (arrayOfString.length >= 2) {
/* 380 */         localObject1 = arrayOfString[1];
/*     */         try
/*     */         {
/* 386 */           localObject2 = InetAddress.getByName((String)localObject1).getCanonicalHostName();
/*     */ 
/* 392 */           if (((String)localObject2).toLowerCase().startsWith(((String)localObject1).toLowerCase() + "."))
/*     */           {
/* 394 */             localObject1 = localObject2;
/*     */           }
/*     */         }
/*     */         catch (UnknownHostException localUnknownHostException) {
/*     */         }
/* 399 */         arrayOfString[1] = ((String)localObject1).toLowerCase();
/*     */       }
/* 401 */       this.nameStrings = arrayOfString;
/* 402 */       this.nameType = paramInt;
/*     */ 
/* 410 */       Object localObject1 = mapHostToRealm(arrayOfString[1]);
/* 411 */       if (localObject1 != null)
/* 412 */         this.nameRealm = new Realm((String)localObject1);
/*     */       else {
/* 414 */         this.nameRealm = localRealm;
/*     */       }
/* 416 */       break;
/*     */     case 0:
/*     */     case 1:
/*     */     case 2:
/*     */     case 4:
/*     */     case 5:
/* 422 */       this.nameStrings = arrayOfString;
/* 423 */       this.nameType = paramInt;
/* 424 */       this.nameRealm = localRealm;
/* 425 */       break;
/*     */     default:
/* 427 */       throw new IllegalArgumentException("Illegal name type");
/*     */     }
/*     */   }
/*     */ 
/*     */   public PrincipalName(String paramString) throws RealmException {
/* 432 */     this(paramString, 0);
/*     */   }
/*     */ 
/*     */   public PrincipalName(String paramString1, String paramString2) throws RealmException {
/* 436 */     this(paramString1, 0);
/* 437 */     this.nameRealm = new Realm(paramString2);
/*     */   }
/*     */ 
/*     */   public String getRealmAsString() {
/* 441 */     return getRealmString();
/*     */   }
/*     */ 
/*     */   public String getPrincipalNameAsString() {
/* 445 */     StringBuffer localStringBuffer = new StringBuffer(this.nameStrings[0]);
/* 446 */     for (int i = 1; i < this.nameStrings.length; i++)
/* 447 */       localStringBuffer.append(this.nameStrings[i]);
/* 448 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   public int hashCode() {
/* 452 */     return toString().hashCode();
/*     */   }
/*     */ 
/*     */   public String getName() {
/* 456 */     return toString();
/*     */   }
/*     */ 
/*     */   public int getNameType() {
/* 460 */     return this.nameType;
/*     */   }
/*     */ 
/*     */   public String[] getNameStrings() {
/* 464 */     return (String[])this.nameStrings.clone();
/*     */   }
/*     */ 
/*     */   public byte[][] toByteArray() {
/* 468 */     byte[][] arrayOfByte = new byte[this.nameStrings.length][];
/* 469 */     for (int i = 0; i < this.nameStrings.length; i++) {
/* 470 */       arrayOfByte[i] = new byte[this.nameStrings[i].length()];
/* 471 */       arrayOfByte[i] = this.nameStrings[i].getBytes();
/*     */     }
/* 473 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   public String getRealmString() {
/* 477 */     if (this.nameRealm != null)
/* 478 */       return this.nameRealm.toString();
/* 479 */     return null;
/*     */   }
/*     */ 
/*     */   public Realm getRealm() {
/* 483 */     return this.nameRealm;
/*     */   }
/*     */ 
/*     */   public void setRealm(Realm paramRealm) throws RealmException {
/* 487 */     this.nameRealm = paramRealm;
/*     */   }
/*     */ 
/*     */   public void setRealm(String paramString) throws RealmException {
/* 491 */     this.nameRealm = new Realm(paramString);
/*     */   }
/*     */ 
/*     */   public String getSalt() {
/* 495 */     if (this.salt == null) {
/* 496 */       StringBuffer localStringBuffer = new StringBuffer();
/* 497 */       if (this.nameRealm != null) {
/* 498 */         localStringBuffer.append(this.nameRealm.toString());
/*     */       }
/* 500 */       for (int i = 0; i < this.nameStrings.length; i++) {
/* 501 */         localStringBuffer.append(this.nameStrings[i]);
/*     */       }
/* 503 */       return localStringBuffer.toString();
/*     */     }
/* 505 */     return this.salt;
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 509 */     StringBuffer localStringBuffer = new StringBuffer();
/* 510 */     for (int i = 0; i < this.nameStrings.length; i++) {
/* 511 */       if (i > 0)
/* 512 */         localStringBuffer.append("/");
/* 513 */       localStringBuffer.append(this.nameStrings[i]);
/*     */     }
/* 515 */     if (this.nameRealm != null) {
/* 516 */       localStringBuffer.append("@");
/* 517 */       localStringBuffer.append(this.nameRealm.toString());
/*     */     }
/*     */ 
/* 520 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   public String getNameString() {
/* 524 */     StringBuffer localStringBuffer = new StringBuffer();
/* 525 */     for (int i = 0; i < this.nameStrings.length; i++) {
/* 526 */       if (i > 0)
/* 527 */         localStringBuffer.append("/");
/* 528 */       localStringBuffer.append(this.nameStrings[i]);
/*     */     }
/* 530 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   public byte[] asn1Encode()
/*     */     throws Asn1Exception, IOException
/*     */   {
/* 541 */     DerOutputStream localDerOutputStream1 = new DerOutputStream();
/* 542 */     DerOutputStream localDerOutputStream2 = new DerOutputStream();
/* 543 */     BigInteger localBigInteger = BigInteger.valueOf(this.nameType);
/* 544 */     localDerOutputStream2.putInteger(localBigInteger);
/* 545 */     localDerOutputStream1.write(DerValue.createTag((byte)-128, true, (byte)0), localDerOutputStream2);
/* 546 */     localDerOutputStream2 = new DerOutputStream();
/* 547 */     DerValue[] arrayOfDerValue = new DerValue[this.nameStrings.length];
/* 548 */     for (int i = 0; i < this.nameStrings.length; i++) {
/* 549 */       arrayOfDerValue[i] = new KerberosString(this.nameStrings[i]).toDerValue();
/*     */     }
/* 551 */     localDerOutputStream2.putSequence(arrayOfDerValue);
/* 552 */     localDerOutputStream1.write(DerValue.createTag((byte)-128, true, (byte)1), localDerOutputStream2);
/* 553 */     localDerOutputStream2 = new DerOutputStream();
/* 554 */     localDerOutputStream2.write((byte)48, localDerOutputStream1);
/* 555 */     return localDerOutputStream2.toByteArray();
/*     */   }
/*     */ 
/*     */   public boolean match(PrincipalName paramPrincipalName)
/*     */   {
/* 567 */     boolean bool = true;
/*     */ 
/* 572 */     if ((this.nameRealm != null) && (paramPrincipalName.nameRealm != null) && 
/* 573 */       (!this.nameRealm.toString().equalsIgnoreCase(paramPrincipalName.nameRealm.toString()))) {
/* 574 */       bool = false;
/*     */     }
/*     */ 
/* 577 */     if (this.nameStrings.length != paramPrincipalName.nameStrings.length)
/* 578 */       bool = false;
/*     */     else {
/* 580 */       for (int i = 0; i < this.nameStrings.length; i++) {
/* 581 */         if (!this.nameStrings[i].equalsIgnoreCase(paramPrincipalName.nameStrings[i])) {
/* 582 */           bool = false;
/*     */         }
/*     */       }
/*     */     }
/* 586 */     return bool;
/*     */   }
/*     */ 
/*     */   public void writePrincipal(CCacheOutputStream paramCCacheOutputStream)
/*     */     throws IOException
/*     */   {
/* 597 */     paramCCacheOutputStream.write32(this.nameType);
/* 598 */     paramCCacheOutputStream.write32(this.nameStrings.length);
/* 599 */     if (this.nameRealm != null) {
/* 600 */       arrayOfByte = null;
/* 601 */       arrayOfByte = this.nameRealm.toString().getBytes();
/* 602 */       paramCCacheOutputStream.write32(arrayOfByte.length);
/* 603 */       paramCCacheOutputStream.write(arrayOfByte, 0, arrayOfByte.length);
/*     */     }
/* 605 */     byte[] arrayOfByte = null;
/* 606 */     for (int i = 0; i < this.nameStrings.length; i++) {
/* 607 */       arrayOfByte = this.nameStrings[i].getBytes();
/* 608 */       paramCCacheOutputStream.write32(arrayOfByte.length);
/* 609 */       paramCCacheOutputStream.write(arrayOfByte, 0, arrayOfByte.length);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected PrincipalName(String paramString1, String paramString2, String paramString3, int paramInt)
/*     */     throws KrbException
/*     */   {
/* 625 */     if (paramInt != 2) {
/* 626 */       throw new KrbException(60, "Bad name type");
/*     */     }
/*     */ 
/* 629 */     String[] arrayOfString = new String[2];
/* 630 */     arrayOfString[0] = paramString1;
/* 631 */     arrayOfString[1] = paramString2;
/*     */ 
/* 633 */     this.nameStrings = arrayOfString;
/* 634 */     this.nameRealm = new Realm(paramString3);
/* 635 */     this.nameType = paramInt;
/*     */   }
/*     */ 
/*     */   public String getInstanceComponent()
/*     */   {
/* 648 */     if ((this.nameStrings != null) && (this.nameStrings.length >= 2))
/*     */     {
/* 650 */       return new String(this.nameStrings[1]);
/*     */     }
/*     */ 
/* 653 */     return null;
/*     */   }
/*     */ 
/*     */   static String mapHostToRealm(String paramString) {
/* 657 */     String str1 = null;
/*     */     try {
/* 659 */       String str2 = null;
/* 660 */       Config localConfig = Config.getInstance();
/* 661 */       if ((str1 = localConfig.getDefault(paramString, "domain_realm")) != null) {
/* 662 */         return str1;
/*     */       }
/* 664 */       for (int i = 1; i < paramString.length(); i++) {
/* 665 */         if ((paramString.charAt(i) == '.') && (i != paramString.length() - 1)) {
/* 666 */           str2 = paramString.substring(i);
/* 667 */           str1 = localConfig.getDefault(str2, "domain_realm");
/* 668 */           if (str1 != null)
/*     */           {
/*     */             break;
/*     */           }
/* 672 */           str2 = paramString.substring(i + 1);
/* 673 */           str1 = localConfig.getDefault(str2, "domain_realm");
/* 674 */           if (str1 != null) {
/*     */             break;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (KrbException localKrbException)
/*     */     {
/*     */     }
/* 683 */     return str1;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.PrincipalName
 * JD-Core Version:    0.6.2
 */