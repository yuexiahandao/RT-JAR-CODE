/*     */ package sun.security.krb5.internal;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.math.BigInteger;
/*     */ import sun.security.krb5.Asn1Exception;
/*     */ import sun.security.krb5.EncryptedData;
/*     */ import sun.security.krb5.PrincipalName;
/*     */ import sun.security.krb5.Realm;
/*     */ import sun.security.krb5.RealmException;
/*     */ import sun.security.util.DerInputStream;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.DerValue;
/*     */ 
/*     */ public class KDCRep
/*     */ {
/*     */   public Realm crealm;
/*     */   public PrincipalName cname;
/*     */   public Ticket ticket;
/*     */   public EncryptedData encPart;
/*     */   public EncKDCRepPart encKDCRepPart;
/*     */   private int pvno;
/*     */   private int msgType;
/*  71 */   public PAData[] pAData = null;
/*  72 */   private boolean DEBUG = Krb5.DEBUG;
/*     */ 
/*     */   public KDCRep(PAData[] paramArrayOfPAData, Realm paramRealm, PrincipalName paramPrincipalName, Ticket paramTicket, EncryptedData paramEncryptedData, int paramInt)
/*     */     throws IOException
/*     */   {
/*  81 */     this.pvno = 5;
/*  82 */     this.msgType = paramInt;
/*  83 */     if (paramArrayOfPAData != null) {
/*  84 */       this.pAData = new PAData[paramArrayOfPAData.length];
/*  85 */       for (int i = 0; i < paramArrayOfPAData.length; i++) {
/*  86 */         if (paramArrayOfPAData[i] == null) {
/*  87 */           throw new IOException("Cannot create a KDCRep");
/*     */         }
/*  89 */         this.pAData[i] = ((PAData)paramArrayOfPAData[i].clone());
/*     */       }
/*     */     }
/*     */ 
/*  93 */     this.crealm = paramRealm;
/*  94 */     this.cname = paramPrincipalName;
/*  95 */     this.ticket = paramTicket;
/*  96 */     this.encPart = paramEncryptedData;
/*     */   }
/*     */ 
/*     */   public KDCRep()
/*     */   {
/*     */   }
/*     */ 
/*     */   public KDCRep(byte[] paramArrayOfByte, int paramInt) throws Asn1Exception, KrbApErrException, RealmException, IOException {
/* 104 */     init(new DerValue(paramArrayOfByte), paramInt);
/*     */   }
/*     */ 
/*     */   public KDCRep(DerValue paramDerValue, int paramInt) throws Asn1Exception, RealmException, KrbApErrException, IOException
/*     */   {
/* 109 */     init(paramDerValue, paramInt);
/*     */   }
/*     */ 
/*     */   protected void init(DerValue paramDerValue, int paramInt)
/*     */     throws Asn1Exception, RealmException, IOException, KrbApErrException
/*     */   {
/* 136 */     if ((paramDerValue.getTag() & 0x1F) != paramInt) {
/* 137 */       if (this.DEBUG) {
/* 138 */         System.out.println(">>> KDCRep: init() encoding tag is " + paramDerValue.getTag() + " req type is " + paramInt);
/*     */       }
/*     */ 
/* 143 */       throw new Asn1Exception(906);
/*     */     }
/* 145 */     DerValue localDerValue1 = paramDerValue.getData().getDerValue();
/* 146 */     if (localDerValue1.getTag() != 48) {
/* 147 */       throw new Asn1Exception(906);
/*     */     }
/* 149 */     DerValue localDerValue2 = localDerValue1.getData().getDerValue();
/* 150 */     if ((localDerValue2.getTag() & 0x1F) == 0) {
/* 151 */       this.pvno = localDerValue2.getData().getBigInteger().intValue();
/* 152 */       if (this.pvno != 5)
/* 153 */         throw new KrbApErrException(39);
/*     */     }
/*     */     else {
/* 156 */       throw new Asn1Exception(906);
/*     */     }
/* 158 */     localDerValue2 = localDerValue1.getData().getDerValue();
/* 159 */     if ((localDerValue2.getTag() & 0x1F) == 1) {
/* 160 */       this.msgType = localDerValue2.getData().getBigInteger().intValue();
/* 161 */       if (this.msgType != paramInt)
/* 162 */         throw new KrbApErrException(40);
/*     */     }
/*     */     else {
/* 165 */       throw new Asn1Exception(906);
/*     */     }
/* 167 */     if ((localDerValue1.getData().peekByte() & 0x1F) == 2) {
/* 168 */       localDerValue2 = localDerValue1.getData().getDerValue();
/* 169 */       DerValue[] arrayOfDerValue = localDerValue2.getData().getSequence(1);
/* 170 */       this.pAData = new PAData[arrayOfDerValue.length];
/* 171 */       for (int i = 0; i < arrayOfDerValue.length; i++)
/* 172 */         this.pAData[i] = new PAData(arrayOfDerValue[i]);
/*     */     }
/*     */     else {
/* 175 */       this.pAData = null;
/*     */     }
/* 177 */     this.crealm = Realm.parse(localDerValue1.getData(), (byte)3, false);
/* 178 */     this.cname = PrincipalName.parse(localDerValue1.getData(), (byte)4, false);
/* 179 */     this.ticket = Ticket.parse(localDerValue1.getData(), (byte)5, false);
/* 180 */     this.encPart = EncryptedData.parse(localDerValue1.getData(), (byte)6, false);
/* 181 */     if (localDerValue1.getData().available() > 0)
/* 182 */       throw new Asn1Exception(906);
/*     */   }
/*     */ 
/*     */   public byte[] asn1Encode()
/*     */     throws Asn1Exception, IOException
/*     */   {
/* 195 */     DerOutputStream localDerOutputStream1 = new DerOutputStream();
/* 196 */     DerOutputStream localDerOutputStream2 = new DerOutputStream();
/* 197 */     localDerOutputStream2.putInteger(BigInteger.valueOf(this.pvno));
/* 198 */     localDerOutputStream1.write(DerValue.createTag((byte)-128, true, (byte)0), localDerOutputStream2);
/*     */ 
/* 200 */     localDerOutputStream2 = new DerOutputStream();
/* 201 */     localDerOutputStream2.putInteger(BigInteger.valueOf(this.msgType));
/* 202 */     localDerOutputStream1.write(DerValue.createTag((byte)-128, true, (byte)1), localDerOutputStream2);
/*     */ 
/* 204 */     if ((this.pAData != null) && (this.pAData.length > 0)) {
/* 205 */       DerOutputStream localDerOutputStream3 = new DerOutputStream();
/* 206 */       for (int i = 0; i < this.pAData.length; i++) {
/* 207 */         localDerOutputStream3.write(this.pAData[i].asn1Encode());
/*     */       }
/* 209 */       localDerOutputStream2 = new DerOutputStream();
/* 210 */       localDerOutputStream2.write((byte)48, localDerOutputStream3);
/* 211 */       localDerOutputStream1.write(DerValue.createTag((byte)-128, true, (byte)2), localDerOutputStream2);
/*     */     }
/*     */ 
/* 214 */     localDerOutputStream1.write(DerValue.createTag((byte)-128, true, (byte)3), this.crealm.asn1Encode());
/*     */ 
/* 216 */     localDerOutputStream1.write(DerValue.createTag((byte)-128, true, (byte)4), this.cname.asn1Encode());
/*     */ 
/* 218 */     localDerOutputStream1.write(DerValue.createTag((byte)-128, true, (byte)5), this.ticket.asn1Encode());
/*     */ 
/* 220 */     localDerOutputStream1.write(DerValue.createTag((byte)-128, true, (byte)6), this.encPart.asn1Encode());
/*     */ 
/* 222 */     localDerOutputStream2 = new DerOutputStream();
/* 223 */     localDerOutputStream2.write((byte)48, localDerOutputStream1);
/* 224 */     return localDerOutputStream2.toByteArray();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.internal.KDCRep
 * JD-Core Version:    0.6.2
 */