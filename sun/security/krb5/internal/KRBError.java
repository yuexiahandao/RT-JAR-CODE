/*     */ package sun.security.krb5.internal;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.io.Serializable;
/*     */ import java.math.BigInteger;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import sun.misc.HexDumpEncoder;
/*     */ import sun.security.krb5.Asn1Exception;
/*     */ import sun.security.krb5.Checksum;
/*     */ import sun.security.krb5.PrincipalName;
/*     */ import sun.security.krb5.Realm;
/*     */ import sun.security.krb5.RealmException;
/*     */ import sun.security.krb5.internal.util.KerberosString;
/*     */ import sun.security.util.DerInputStream;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.DerValue;
/*     */ 
/*     */ public class KRBError
/*     */   implements Serializable
/*     */ {
/*     */   static final long serialVersionUID = 3643809337475284503L;
/*     */   private int pvno;
/*     */   private int msgType;
/*     */   private KerberosTime cTime;
/*     */   private Integer cuSec;
/*     */   private KerberosTime sTime;
/*     */   private Integer suSec;
/*     */   private int errorCode;
/*     */   private Realm crealm;
/*     */   private PrincipalName cname;
/*     */   private Realm realm;
/*     */   private PrincipalName sname;
/*     */   private String eText;
/*     */   private byte[] eData;
/*     */   private Checksum eCksum;
/*     */   private PAData[] pa;
/* 103 */   private static boolean DEBUG = Krb5.DEBUG;
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream) throws IOException, ClassNotFoundException
/*     */   {
/*     */     try {
/* 108 */       init(new DerValue((byte[])paramObjectInputStream.readObject()));
/* 109 */       parseEData(this.eData);
/*     */     } catch (Exception localException) {
/* 111 */       throw new IOException(localException);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException
/*     */   {
/*     */     try {
/* 118 */       paramObjectOutputStream.writeObject(asn1Encode());
/*     */     } catch (Exception localException) {
/* 120 */       throw new IOException(localException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public KRBError(APOptions paramAPOptions, KerberosTime paramKerberosTime1, Integer paramInteger1, KerberosTime paramKerberosTime2, Integer paramInteger2, int paramInt, Realm paramRealm1, PrincipalName paramPrincipalName1, Realm paramRealm2, PrincipalName paramPrincipalName2, String paramString, byte[] paramArrayOfByte)
/*     */     throws IOException, Asn1Exception
/*     */   {
/* 138 */     this.pvno = 5;
/* 139 */     this.msgType = 30;
/* 140 */     this.cTime = paramKerberosTime1;
/* 141 */     this.cuSec = paramInteger1;
/* 142 */     this.sTime = paramKerberosTime2;
/* 143 */     this.suSec = paramInteger2;
/* 144 */     this.errorCode = paramInt;
/* 145 */     this.crealm = paramRealm1;
/* 146 */     this.cname = paramPrincipalName1;
/* 147 */     this.realm = paramRealm2;
/* 148 */     this.sname = paramPrincipalName2;
/* 149 */     this.eText = paramString;
/* 150 */     this.eData = paramArrayOfByte;
/*     */ 
/* 152 */     parseEData(this.eData);
/*     */   }
/*     */ 
/*     */   public KRBError(APOptions paramAPOptions, KerberosTime paramKerberosTime1, Integer paramInteger1, KerberosTime paramKerberosTime2, Integer paramInteger2, int paramInt, Realm paramRealm1, PrincipalName paramPrincipalName1, Realm paramRealm2, PrincipalName paramPrincipalName2, String paramString, byte[] paramArrayOfByte, Checksum paramChecksum)
/*     */     throws IOException, Asn1Exception
/*     */   {
/* 170 */     this.pvno = 5;
/* 171 */     this.msgType = 30;
/* 172 */     this.cTime = paramKerberosTime1;
/* 173 */     this.cuSec = paramInteger1;
/* 174 */     this.sTime = paramKerberosTime2;
/* 175 */     this.suSec = paramInteger2;
/* 176 */     this.errorCode = paramInt;
/* 177 */     this.crealm = paramRealm1;
/* 178 */     this.cname = paramPrincipalName1;
/* 179 */     this.realm = paramRealm2;
/* 180 */     this.sname = paramPrincipalName2;
/* 181 */     this.eText = paramString;
/* 182 */     this.eData = paramArrayOfByte;
/* 183 */     this.eCksum = paramChecksum;
/*     */ 
/* 185 */     parseEData(this.eData);
/*     */   }
/*     */ 
/*     */   public KRBError(byte[] paramArrayOfByte) throws Asn1Exception, RealmException, KrbApErrException, IOException
/*     */   {
/* 190 */     init(new DerValue(paramArrayOfByte));
/* 191 */     parseEData(this.eData);
/*     */   }
/*     */ 
/*     */   public KRBError(DerValue paramDerValue) throws Asn1Exception, RealmException, KrbApErrException, IOException
/*     */   {
/* 196 */     init(paramDerValue);
/* 197 */     showDebug();
/* 198 */     parseEData(this.eData);
/*     */   }
/*     */ 
/*     */   private void parseEData(byte[] paramArrayOfByte)
/*     */     throws IOException
/*     */   {
/* 225 */     if (paramArrayOfByte == null) {
/* 226 */       return;
/*     */     }
/*     */ 
/* 230 */     if ((this.errorCode == 25) || (this.errorCode == 24))
/*     */     {
/*     */       try
/*     */       {
/* 236 */         parsePAData(paramArrayOfByte);
/*     */       } catch (Exception localException) {
/* 238 */         if (DEBUG) {
/* 239 */           System.out.println("Unable to parse eData field of KRB-ERROR:\n" + new HexDumpEncoder().encodeBuffer(paramArrayOfByte));
/*     */         }
/*     */ 
/* 242 */         IOException localIOException = new IOException("Unable to parse eData field of KRB-ERROR");
/*     */ 
/* 244 */         localIOException.initCause(localException);
/* 245 */         throw localIOException;
/*     */       }
/*     */     }
/* 248 */     else if (DEBUG)
/* 249 */       System.out.println("Unknown eData field of KRB-ERROR:\n" + new HexDumpEncoder().encodeBuffer(paramArrayOfByte));
/*     */   }
/*     */ 
/*     */   private void parsePAData(byte[] paramArrayOfByte)
/*     */     throws IOException, Asn1Exception
/*     */   {
/* 261 */     DerValue localDerValue1 = new DerValue(paramArrayOfByte);
/* 262 */     ArrayList localArrayList = new ArrayList();
/* 263 */     while (localDerValue1.data.available() > 0)
/*     */     {
/* 265 */       DerValue localDerValue2 = localDerValue1.data.getDerValue();
/* 266 */       PAData localPAData = new PAData(localDerValue2);
/* 267 */       localArrayList.add(localPAData);
/* 268 */       if (DEBUG) {
/* 269 */         System.out.println(localPAData);
/*     */       }
/*     */     }
/* 272 */     this.pa = ((PAData[])localArrayList.toArray(new PAData[localArrayList.size()]));
/*     */   }
/*     */ 
/*     */   public final KerberosTime getServerTime() {
/* 276 */     return this.sTime;
/*     */   }
/*     */ 
/*     */   public final KerberosTime getClientTime() {
/* 280 */     return this.cTime;
/*     */   }
/*     */ 
/*     */   public final Integer getServerMicroSeconds() {
/* 284 */     return this.suSec;
/*     */   }
/*     */ 
/*     */   public final Integer getClientMicroSeconds() {
/* 288 */     return this.cuSec;
/*     */   }
/*     */ 
/*     */   public final int getErrorCode() {
/* 292 */     return this.errorCode;
/*     */   }
/*     */ 
/*     */   public final PAData[] getPA()
/*     */   {
/* 297 */     return this.pa;
/*     */   }
/*     */ 
/*     */   public final String getErrorString() {
/* 301 */     return this.eText;
/*     */   }
/*     */ 
/*     */   private void init(DerValue paramDerValue)
/*     */     throws Asn1Exception, RealmException, KrbApErrException, IOException
/*     */   {
/* 316 */     if (((paramDerValue.getTag() & 0x1F) != 30) || (paramDerValue.isApplication() != true) || (paramDerValue.isConstructed() != true))
/*     */     {
/* 319 */       throw new Asn1Exception(906);
/*     */     }
/* 321 */     DerValue localDerValue1 = paramDerValue.getData().getDerValue();
/* 322 */     if (localDerValue1.getTag() != 48) {
/* 323 */       throw new Asn1Exception(906);
/*     */     }
/* 325 */     DerValue localDerValue2 = localDerValue1.getData().getDerValue();
/* 326 */     if ((localDerValue2.getTag() & 0x1F) == 0)
/*     */     {
/* 328 */       this.pvno = localDerValue2.getData().getBigInteger().intValue();
/* 329 */       if (this.pvno != 5)
/* 330 */         throw new KrbApErrException(39);
/*     */     } else {
/* 332 */       throw new Asn1Exception(906);
/*     */     }
/*     */ 
/* 335 */     localDerValue2 = localDerValue1.getData().getDerValue();
/* 336 */     if ((localDerValue2.getTag() & 0x1F) == 1) {
/* 337 */       this.msgType = localDerValue2.getData().getBigInteger().intValue();
/* 338 */       if (this.msgType != 30)
/* 339 */         throw new KrbApErrException(40);
/*     */     }
/*     */     else {
/* 342 */       throw new Asn1Exception(906);
/*     */     }
/*     */ 
/* 345 */     this.cTime = KerberosTime.parse(localDerValue1.getData(), (byte)2, true);
/* 346 */     if ((localDerValue1.getData().peekByte() & 0x1F) == 3) {
/* 347 */       localDerValue2 = localDerValue1.getData().getDerValue();
/* 348 */       this.cuSec = new Integer(localDerValue2.getData().getBigInteger().intValue());
/*     */     } else {
/* 350 */       this.cuSec = null;
/* 351 */     }this.sTime = KerberosTime.parse(localDerValue1.getData(), (byte)4, false);
/* 352 */     localDerValue2 = localDerValue1.getData().getDerValue();
/* 353 */     if ((localDerValue2.getTag() & 0x1F) == 5)
/* 354 */       this.suSec = new Integer(localDerValue2.getData().getBigInteger().intValue());
/*     */     else
/* 356 */       throw new Asn1Exception(906);
/* 357 */     localDerValue2 = localDerValue1.getData().getDerValue();
/* 358 */     if ((localDerValue2.getTag() & 0x1F) == 6)
/* 359 */       this.errorCode = localDerValue2.getData().getBigInteger().intValue();
/*     */     else
/* 361 */       throw new Asn1Exception(906);
/* 362 */     this.crealm = Realm.parse(localDerValue1.getData(), (byte)7, true);
/* 363 */     this.cname = PrincipalName.parse(localDerValue1.getData(), (byte)8, true);
/* 364 */     this.realm = Realm.parse(localDerValue1.getData(), (byte)9, false);
/* 365 */     this.sname = PrincipalName.parse(localDerValue1.getData(), (byte)10, false);
/* 366 */     this.eText = null;
/* 367 */     this.eData = null;
/* 368 */     this.eCksum = null;
/* 369 */     if ((localDerValue1.getData().available() > 0) && 
/* 370 */       ((localDerValue1.getData().peekByte() & 0x1F) == 11)) {
/* 371 */       localDerValue2 = localDerValue1.getData().getDerValue();
/* 372 */       this.eText = new KerberosString(localDerValue2.getData().getDerValue()).toString();
/*     */     }
/*     */ 
/* 376 */     if ((localDerValue1.getData().available() > 0) && 
/* 377 */       ((localDerValue1.getData().peekByte() & 0x1F) == 12)) {
/* 378 */       localDerValue2 = localDerValue1.getData().getDerValue();
/* 379 */       this.eData = localDerValue2.getData().getOctetString();
/*     */     }
/*     */ 
/* 382 */     if (localDerValue1.getData().available() > 0) {
/* 383 */       this.eCksum = Checksum.parse(localDerValue1.getData(), (byte)13, true);
/*     */     }
/* 385 */     if (localDerValue1.getData().available() > 0)
/* 386 */       throw new Asn1Exception(906);
/*     */   }
/*     */ 
/*     */   private void showDebug()
/*     */   {
/* 393 */     if (DEBUG) {
/* 394 */       System.out.println(">>>KRBError:");
/* 395 */       if (this.cTime != null)
/* 396 */         System.out.println("\t cTime is " + this.cTime.toDate().toString() + " " + this.cTime.toDate().getTime());
/* 397 */       if (this.cuSec != null) {
/* 398 */         System.out.println("\t cuSec is " + this.cuSec.intValue());
/*     */       }
/*     */ 
/* 401 */       System.out.println("\t sTime is " + this.sTime.toDate().toString() + " " + this.sTime.toDate().getTime());
/*     */ 
/* 403 */       System.out.println("\t suSec is " + this.suSec);
/* 404 */       System.out.println("\t error code is " + this.errorCode);
/* 405 */       System.out.println("\t error Message is " + Krb5.getErrorMessage(this.errorCode));
/* 406 */       if (this.crealm != null) {
/* 407 */         System.out.println("\t crealm is " + this.crealm.toString());
/*     */       }
/* 409 */       if (this.cname != null) {
/* 410 */         System.out.println("\t cname is " + this.cname.toString());
/*     */       }
/* 412 */       if (this.realm != null) {
/* 413 */         System.out.println("\t realm is " + this.realm.toString());
/*     */       }
/* 415 */       if (this.sname != null) {
/* 416 */         System.out.println("\t sname is " + this.sname.toString());
/*     */       }
/* 418 */       if (this.eData != null) {
/* 419 */         System.out.println("\t eData provided.");
/*     */       }
/* 421 */       if (this.eCksum != null) {
/* 422 */         System.out.println("\t checksum provided.");
/*     */       }
/* 424 */       System.out.println("\t msgType is " + this.msgType);
/*     */     }
/*     */   }
/*     */ 
/*     */   public byte[] asn1Encode()
/*     */     throws Asn1Exception, IOException
/*     */   {
/* 435 */     DerOutputStream localDerOutputStream1 = new DerOutputStream();
/* 436 */     DerOutputStream localDerOutputStream2 = new DerOutputStream();
/*     */ 
/* 438 */     localDerOutputStream1.putInteger(BigInteger.valueOf(this.pvno));
/* 439 */     localDerOutputStream2.write(DerValue.createTag((byte)-128, true, (byte)0), localDerOutputStream1);
/* 440 */     localDerOutputStream1 = new DerOutputStream();
/* 441 */     localDerOutputStream1.putInteger(BigInteger.valueOf(this.msgType));
/* 442 */     localDerOutputStream2.write(DerValue.createTag((byte)-128, true, (byte)1), localDerOutputStream1);
/*     */ 
/* 444 */     if (this.cTime != null) {
/* 445 */       localDerOutputStream2.write(DerValue.createTag((byte)-128, true, (byte)2), this.cTime.asn1Encode());
/*     */     }
/* 447 */     if (this.cuSec != null) {
/* 448 */       localDerOutputStream1 = new DerOutputStream();
/* 449 */       localDerOutputStream1.putInteger(BigInteger.valueOf(this.cuSec.intValue()));
/* 450 */       localDerOutputStream2.write(DerValue.createTag((byte)-128, true, (byte)3), localDerOutputStream1);
/*     */     }
/*     */ 
/* 453 */     localDerOutputStream2.write(DerValue.createTag((byte)-128, true, (byte)4), this.sTime.asn1Encode());
/* 454 */     localDerOutputStream1 = new DerOutputStream();
/* 455 */     localDerOutputStream1.putInteger(BigInteger.valueOf(this.suSec.intValue()));
/* 456 */     localDerOutputStream2.write(DerValue.createTag((byte)-128, true, (byte)5), localDerOutputStream1);
/* 457 */     localDerOutputStream1 = new DerOutputStream();
/* 458 */     localDerOutputStream1.putInteger(BigInteger.valueOf(this.errorCode));
/* 459 */     localDerOutputStream2.write(DerValue.createTag((byte)-128, true, (byte)6), localDerOutputStream1);
/*     */ 
/* 461 */     if (this.crealm != null) {
/* 462 */       localDerOutputStream2.write(DerValue.createTag((byte)-128, true, (byte)7), this.crealm.asn1Encode());
/*     */     }
/* 464 */     if (this.cname != null) {
/* 465 */       localDerOutputStream2.write(DerValue.createTag((byte)-128, true, (byte)8), this.cname.asn1Encode());
/*     */     }
/*     */ 
/* 468 */     localDerOutputStream2.write(DerValue.createTag((byte)-128, true, (byte)9), this.realm.asn1Encode());
/* 469 */     localDerOutputStream2.write(DerValue.createTag((byte)-128, true, (byte)10), this.sname.asn1Encode());
/*     */ 
/* 471 */     if (this.eText != null) {
/* 472 */       localDerOutputStream1 = new DerOutputStream();
/* 473 */       localDerOutputStream1.putDerValue(new KerberosString(this.eText).toDerValue());
/* 474 */       localDerOutputStream2.write(DerValue.createTag((byte)-128, true, (byte)11), localDerOutputStream1);
/*     */     }
/* 476 */     if (this.eData != null) {
/* 477 */       localDerOutputStream1 = new DerOutputStream();
/* 478 */       localDerOutputStream1.putOctetString(this.eData);
/* 479 */       localDerOutputStream2.write(DerValue.createTag((byte)-128, true, (byte)12), localDerOutputStream1);
/*     */     }
/* 481 */     if (this.eCksum != null) {
/* 482 */       localDerOutputStream2.write(DerValue.createTag((byte)-128, true, (byte)13), this.eCksum.asn1Encode());
/*     */     }
/*     */ 
/* 485 */     localDerOutputStream1 = new DerOutputStream();
/* 486 */     localDerOutputStream1.write((byte)48, localDerOutputStream2);
/* 487 */     localDerOutputStream2 = new DerOutputStream();
/* 488 */     localDerOutputStream2.write(DerValue.createTag((byte)64, true, (byte)30), localDerOutputStream1);
/* 489 */     return localDerOutputStream2.toByteArray();
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject) {
/* 493 */     if (this == paramObject) {
/* 494 */       return true;
/*     */     }
/*     */ 
/* 497 */     if (!(paramObject instanceof KRBError)) {
/* 498 */       return false;
/*     */     }
/*     */ 
/* 501 */     KRBError localKRBError = (KRBError)paramObject;
/* 502 */     return (this.pvno == localKRBError.pvno) && (this.msgType == localKRBError.msgType) && (isEqual(this.cTime, localKRBError.cTime)) && (isEqual(this.cuSec, localKRBError.cuSec)) && (isEqual(this.sTime, localKRBError.sTime)) && (isEqual(this.suSec, localKRBError.suSec)) && (this.errorCode == localKRBError.errorCode) && (isEqual(this.crealm, localKRBError.crealm)) && (isEqual(this.cname, localKRBError.cname)) && (isEqual(this.realm, localKRBError.realm)) && (isEqual(this.sname, localKRBError.sname)) && (isEqual(this.eText, localKRBError.eText)) && (Arrays.equals(this.eData, localKRBError.eData)) && (isEqual(this.eCksum, localKRBError.eCksum));
/*     */   }
/*     */ 
/*     */   private static boolean isEqual(Object paramObject1, Object paramObject2)
/*     */   {
/* 519 */     return paramObject1 == null ? false : paramObject2 == null ? true : paramObject1.equals(paramObject2);
/*     */   }
/*     */ 
/*     */   public int hashCode() {
/* 523 */     int i = 17;
/* 524 */     i = 37 * i + this.pvno;
/* 525 */     i = 37 * i + this.msgType;
/* 526 */     if (this.cTime != null) i = 37 * i + this.cTime.hashCode();
/* 527 */     if (this.cuSec != null) i = 37 * i + this.cuSec.hashCode();
/* 528 */     if (this.sTime != null) i = 37 * i + this.sTime.hashCode();
/* 529 */     if (this.suSec != null) i = 37 * i + this.suSec.hashCode();
/* 530 */     i = 37 * i + this.errorCode;
/* 531 */     if (this.crealm != null) i = 37 * i + this.crealm.hashCode();
/* 532 */     if (this.cname != null) i = 37 * i + this.cname.hashCode();
/* 533 */     if (this.realm != null) i = 37 * i + this.realm.hashCode();
/* 534 */     if (this.sname != null) i = 37 * i + this.sname.hashCode();
/* 535 */     if (this.eText != null) i = 37 * i + this.eText.hashCode();
/* 536 */     i = 37 * i + Arrays.hashCode(this.eData);
/* 537 */     if (this.eCksum != null) i = 37 * i + this.eCksum.hashCode();
/* 538 */     return i;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.internal.KRBError
 * JD-Core Version:    0.6.2
 */