/*     */ package sun.security.krb5.internal;
/*     */ 
/*     */ import java.io.IOException;
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
/*     */ public class Ticket
/*     */   implements Cloneable
/*     */ {
/*     */   public int tkt_vno;
/*     */   public Realm realm;
/*     */   public PrincipalName sname;
/*     */   public EncryptedData encPart;
/*     */ 
/*     */   private Ticket()
/*     */   {
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/*  71 */     Ticket localTicket = new Ticket();
/*  72 */     localTicket.realm = ((Realm)this.realm.clone());
/*  73 */     localTicket.sname = ((PrincipalName)this.sname.clone());
/*  74 */     localTicket.encPart = ((EncryptedData)this.encPart.clone());
/*  75 */     localTicket.tkt_vno = this.tkt_vno;
/*  76 */     return localTicket;
/*     */   }
/*     */ 
/*     */   public Ticket(Realm paramRealm, PrincipalName paramPrincipalName, EncryptedData paramEncryptedData)
/*     */   {
/*  84 */     this.tkt_vno = 5;
/*  85 */     this.realm = paramRealm;
/*  86 */     this.sname = paramPrincipalName;
/*  87 */     this.encPart = paramEncryptedData;
/*     */   }
/*     */ 
/*     */   public Ticket(byte[] paramArrayOfByte) throws Asn1Exception, RealmException, KrbApErrException, IOException
/*     */   {
/*  92 */     init(new DerValue(paramArrayOfByte));
/*     */   }
/*     */ 
/*     */   public Ticket(DerValue paramDerValue) throws Asn1Exception, RealmException, KrbApErrException, IOException
/*     */   {
/*  97 */     init(paramDerValue);
/*     */   }
/*     */ 
/*     */   private void init(DerValue paramDerValue)
/*     */     throws Asn1Exception, RealmException, KrbApErrException, IOException
/*     */   {
/* 113 */     if (((paramDerValue.getTag() & 0x1F) != 1) || (paramDerValue.isApplication() != true) || (paramDerValue.isConstructed() != true))
/*     */     {
/* 116 */       throw new Asn1Exception(906);
/* 117 */     }DerValue localDerValue1 = paramDerValue.getData().getDerValue();
/* 118 */     if (localDerValue1.getTag() != 48)
/* 119 */       throw new Asn1Exception(906);
/* 120 */     DerValue localDerValue2 = localDerValue1.getData().getDerValue();
/* 121 */     if ((localDerValue2.getTag() & 0x1F) != 0)
/* 122 */       throw new Asn1Exception(906);
/* 123 */     this.tkt_vno = localDerValue2.getData().getBigInteger().intValue();
/* 124 */     if (this.tkt_vno != 5)
/* 125 */       throw new KrbApErrException(39);
/* 126 */     this.realm = Realm.parse(localDerValue1.getData(), (byte)1, false);
/* 127 */     this.sname = PrincipalName.parse(localDerValue1.getData(), (byte)2, false);
/* 128 */     this.encPart = EncryptedData.parse(localDerValue1.getData(), (byte)3, false);
/* 129 */     if (localDerValue1.getData().available() > 0)
/* 130 */       throw new Asn1Exception(906);
/*     */   }
/*     */ 
/*     */   public byte[] asn1Encode()
/*     */     throws Asn1Exception, IOException
/*     */   {
/* 140 */     DerOutputStream localDerOutputStream1 = new DerOutputStream();
/* 141 */     DerOutputStream localDerOutputStream2 = new DerOutputStream();
/* 142 */     DerValue[] arrayOfDerValue = new DerValue[4];
/* 143 */     localDerOutputStream2.putInteger(BigInteger.valueOf(this.tkt_vno));
/* 144 */     localDerOutputStream1.write(DerValue.createTag((byte)-128, true, (byte)0), localDerOutputStream2);
/* 145 */     localDerOutputStream1.write(DerValue.createTag((byte)-128, true, (byte)1), this.realm.asn1Encode());
/* 146 */     localDerOutputStream1.write(DerValue.createTag((byte)-128, true, (byte)2), this.sname.asn1Encode());
/* 147 */     localDerOutputStream1.write(DerValue.createTag((byte)-128, true, (byte)3), this.encPart.asn1Encode());
/* 148 */     localDerOutputStream2 = new DerOutputStream();
/* 149 */     localDerOutputStream2.write((byte)48, localDerOutputStream1);
/* 150 */     DerOutputStream localDerOutputStream3 = new DerOutputStream();
/* 151 */     localDerOutputStream3.write(DerValue.createTag((byte)64, true, (byte)1), localDerOutputStream2);
/* 152 */     return localDerOutputStream3.toByteArray();
/*     */   }
/*     */ 
/*     */   public static Ticket parse(DerInputStream paramDerInputStream, byte paramByte, boolean paramBoolean)
/*     */     throws Asn1Exception, IOException, RealmException, KrbApErrException
/*     */   {
/* 167 */     if ((paramBoolean) && (((byte)paramDerInputStream.peekByte() & 0x1F) != paramByte))
/* 168 */       return null;
/* 169 */     DerValue localDerValue1 = paramDerInputStream.getDerValue();
/* 170 */     if (paramByte != (localDerValue1.getTag() & 0x1F)) {
/* 171 */       throw new Asn1Exception(906);
/*     */     }
/*     */ 
/* 174 */     DerValue localDerValue2 = localDerValue1.getData().getDerValue();
/* 175 */     return new Ticket(localDerValue2);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.internal.Ticket
 * JD-Core Version:    0.6.2
 */