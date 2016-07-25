/*     */ package sun.security.krb5.internal;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.math.BigInteger;
/*     */ import sun.security.krb5.Asn1Exception;
/*     */ import sun.security.krb5.RealmException;
/*     */ import sun.security.util.DerInputStream;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.DerValue;
/*     */ 
/*     */ public class EncKrbCredPart
/*     */ {
/*  62 */   public KrbCredInfo[] ticketInfo = null;
/*     */   public KerberosTime timeStamp;
/*     */   private Integer nonce;
/*     */   private Integer usec;
/*     */   private HostAddress sAddress;
/*     */   private HostAddresses rAddress;
/*     */ 
/*     */   public EncKrbCredPart(KrbCredInfo[] paramArrayOfKrbCredInfo, KerberosTime paramKerberosTime, Integer paramInteger1, Integer paramInteger2, HostAddress paramHostAddress, HostAddresses paramHostAddresses)
/*     */     throws IOException
/*     */   {
/*  76 */     if (paramArrayOfKrbCredInfo != null) {
/*  77 */       this.ticketInfo = new KrbCredInfo[paramArrayOfKrbCredInfo.length];
/*  78 */       for (int i = 0; i < paramArrayOfKrbCredInfo.length; i++) {
/*  79 */         if (paramArrayOfKrbCredInfo[i] == null) {
/*  80 */           throw new IOException("Cannot create a EncKrbCredPart");
/*     */         }
/*  82 */         this.ticketInfo[i] = ((KrbCredInfo)paramArrayOfKrbCredInfo[i].clone());
/*     */       }
/*     */     }
/*     */ 
/*  86 */     this.timeStamp = paramKerberosTime;
/*  87 */     this.usec = paramInteger1;
/*  88 */     this.nonce = paramInteger2;
/*  89 */     this.sAddress = paramHostAddress;
/*  90 */     this.rAddress = paramHostAddresses;
/*     */   }
/*     */ 
/*     */   public EncKrbCredPart(byte[] paramArrayOfByte) throws Asn1Exception, IOException, RealmException
/*     */   {
/*  95 */     init(new DerValue(paramArrayOfByte));
/*     */   }
/*     */ 
/*     */   public EncKrbCredPart(DerValue paramDerValue) throws Asn1Exception, IOException, RealmException
/*     */   {
/* 100 */     init(paramDerValue);
/*     */   }
/*     */ 
/*     */   private void init(DerValue paramDerValue)
/*     */     throws Asn1Exception, IOException, RealmException
/*     */   {
/* 115 */     this.nonce = null;
/* 116 */     this.timeStamp = null;
/* 117 */     this.usec = null;
/* 118 */     this.sAddress = null;
/* 119 */     this.rAddress = null;
/* 120 */     if (((paramDerValue.getTag() & 0x1F) != 29) || (paramDerValue.isApplication() != true) || (paramDerValue.isConstructed() != true))
/*     */     {
/* 123 */       throw new Asn1Exception(906);
/*     */     }
/* 125 */     DerValue localDerValue1 = paramDerValue.getData().getDerValue();
/* 126 */     if (localDerValue1.getTag() != 48) {
/* 127 */       throw new Asn1Exception(906);
/*     */     }
/*     */ 
/* 130 */     DerValue localDerValue2 = localDerValue1.getData().getDerValue();
/* 131 */     if ((localDerValue2.getTag() & 0x1F) == 0) {
/* 132 */       DerValue[] arrayOfDerValue = localDerValue2.getData().getSequence(1);
/* 133 */       this.ticketInfo = new KrbCredInfo[arrayOfDerValue.length];
/* 134 */       for (int i = 0; i < arrayOfDerValue.length; i++)
/* 135 */         this.ticketInfo[i] = new KrbCredInfo(arrayOfDerValue[i]);
/*     */     }
/*     */     else {
/* 138 */       throw new Asn1Exception(906);
/*     */     }
/* 140 */     if ((localDerValue1.getData().available() > 0) && 
/* 141 */       (((byte)localDerValue1.getData().peekByte() & 0x1F) == 1)) {
/* 142 */       localDerValue2 = localDerValue1.getData().getDerValue();
/* 143 */       this.nonce = new Integer(localDerValue2.getData().getBigInteger().intValue());
/*     */     }
/*     */ 
/* 146 */     if (localDerValue1.getData().available() > 0) {
/* 147 */       this.timeStamp = KerberosTime.parse(localDerValue1.getData(), (byte)2, true);
/*     */     }
/* 149 */     if ((localDerValue1.getData().available() > 0) && 
/* 150 */       (((byte)localDerValue1.getData().peekByte() & 0x1F) == 3)) {
/* 151 */       localDerValue2 = localDerValue1.getData().getDerValue();
/* 152 */       this.usec = new Integer(localDerValue2.getData().getBigInteger().intValue());
/*     */     }
/*     */ 
/* 155 */     if (localDerValue1.getData().available() > 0) {
/* 156 */       this.sAddress = HostAddress.parse(localDerValue1.getData(), (byte)4, true);
/*     */     }
/* 158 */     if (localDerValue1.getData().available() > 0) {
/* 159 */       this.rAddress = HostAddresses.parse(localDerValue1.getData(), (byte)5, true);
/*     */     }
/* 161 */     if (localDerValue1.getData().available() > 0)
/* 162 */       throw new Asn1Exception(906);
/*     */   }
/*     */ 
/*     */   public byte[] asn1Encode()
/*     */     throws Asn1Exception, IOException
/*     */   {
/* 174 */     DerOutputStream localDerOutputStream1 = new DerOutputStream();
/* 175 */     DerOutputStream localDerOutputStream2 = new DerOutputStream();
/* 176 */     DerValue[] arrayOfDerValue = new DerValue[this.ticketInfo.length];
/* 177 */     for (int i = 0; i < this.ticketInfo.length; i++) {
/* 178 */       arrayOfDerValue[i] = new DerValue(this.ticketInfo[i].asn1Encode());
/*     */     }
/* 180 */     localDerOutputStream2.putSequence(arrayOfDerValue);
/* 181 */     localDerOutputStream1.write(DerValue.createTag((byte)-128, true, (byte)0), localDerOutputStream2);
/*     */ 
/* 184 */     if (this.nonce != null) {
/* 185 */       localDerOutputStream2 = new DerOutputStream();
/* 186 */       localDerOutputStream2.putInteger(BigInteger.valueOf(this.nonce.intValue()));
/* 187 */       localDerOutputStream1.write(DerValue.createTag((byte)-128, true, (byte)1), localDerOutputStream2);
/*     */     }
/*     */ 
/* 190 */     if (this.timeStamp != null) {
/* 191 */       localDerOutputStream1.write(DerValue.createTag((byte)-128, true, (byte)2), this.timeStamp.asn1Encode());
/*     */     }
/*     */ 
/* 194 */     if (this.usec != null) {
/* 195 */       localDerOutputStream2 = new DerOutputStream();
/* 196 */       localDerOutputStream2.putInteger(BigInteger.valueOf(this.usec.intValue()));
/* 197 */       localDerOutputStream1.write(DerValue.createTag((byte)-128, true, (byte)3), localDerOutputStream2);
/*     */     }
/*     */ 
/* 200 */     if (this.sAddress != null) {
/* 201 */       localDerOutputStream1.write(DerValue.createTag((byte)-128, true, (byte)4), this.sAddress.asn1Encode());
/*     */     }
/*     */ 
/* 204 */     if (this.rAddress != null) {
/* 205 */       localDerOutputStream1.write(DerValue.createTag((byte)-128, true, (byte)5), this.rAddress.asn1Encode());
/*     */     }
/*     */ 
/* 208 */     localDerOutputStream2 = new DerOutputStream();
/* 209 */     localDerOutputStream2.write((byte)48, localDerOutputStream1);
/* 210 */     localDerOutputStream1 = new DerOutputStream();
/* 211 */     localDerOutputStream1.write(DerValue.createTag((byte)64, true, (byte)29), localDerOutputStream2);
/*     */ 
/* 213 */     return localDerOutputStream1.toByteArray();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.internal.EncKrbCredPart
 * JD-Core Version:    0.6.2
 */