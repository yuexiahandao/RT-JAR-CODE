/*     */ package sun.security.krb5.internal;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.math.BigInteger;
/*     */ import java.util.Vector;
/*     */ import sun.security.krb5.Asn1Exception;
/*     */ import sun.security.krb5.EncryptedData;
/*     */ import sun.security.krb5.RealmException;
/*     */ import sun.security.util.DerInputStream;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.DerValue;
/*     */ 
/*     */ public class KRBCred
/*     */ {
/*  61 */   public Ticket[] tickets = null;
/*     */   public EncryptedData encPart;
/*     */   private int pvno;
/*     */   private int msgType;
/*     */ 
/*     */   public KRBCred(Ticket[] paramArrayOfTicket, EncryptedData paramEncryptedData)
/*     */     throws IOException
/*     */   {
/*  67 */     this.pvno = 5;
/*  68 */     this.msgType = 22;
/*  69 */     if (paramArrayOfTicket != null) {
/*  70 */       this.tickets = new Ticket[paramArrayOfTicket.length];
/*  71 */       for (int i = 0; i < paramArrayOfTicket.length; i++) {
/*  72 */         if (paramArrayOfTicket[i] == null) {
/*  73 */           throw new IOException("Cannot create a KRBCred");
/*     */         }
/*  75 */         this.tickets[i] = ((Ticket)paramArrayOfTicket[i].clone());
/*     */       }
/*     */     }
/*     */ 
/*  79 */     this.encPart = paramEncryptedData;
/*     */   }
/*     */ 
/*     */   public KRBCred(byte[] paramArrayOfByte) throws Asn1Exception, RealmException, KrbApErrException, IOException
/*     */   {
/*  84 */     init(new DerValue(paramArrayOfByte));
/*     */   }
/*     */ 
/*     */   public KRBCred(DerValue paramDerValue) throws Asn1Exception, RealmException, KrbApErrException, IOException
/*     */   {
/*  89 */     init(paramDerValue);
/*     */   }
/*     */ 
/*     */   private void init(DerValue paramDerValue)
/*     */     throws Asn1Exception, RealmException, KrbApErrException, IOException
/*     */   {
/* 103 */     if (((paramDerValue.getTag() & 0x1F) != 22) || (paramDerValue.isApplication() != true) || (paramDerValue.isConstructed() != true))
/*     */     {
/* 106 */       throw new Asn1Exception(906);
/*     */     }
/*     */ 
/* 109 */     DerValue localDerValue1 = paramDerValue.getData().getDerValue();
/* 110 */     if (localDerValue1.getTag() != 48) {
/* 111 */       throw new Asn1Exception(906);
/*     */     }
/* 113 */     DerValue localDerValue2 = localDerValue1.getData().getDerValue();
/* 114 */     if ((localDerValue2.getTag() & 0x1F) == 0) {
/* 115 */       this.pvno = localDerValue2.getData().getBigInteger().intValue();
/* 116 */       if (this.pvno != 5)
/* 117 */         throw new KrbApErrException(39);
/*     */     }
/*     */     else {
/* 120 */       throw new Asn1Exception(906);
/*     */     }
/* 122 */     localDerValue2 = localDerValue1.getData().getDerValue();
/* 123 */     if ((localDerValue2.getTag() & 0x1F) == 1) {
/* 124 */       this.msgType = localDerValue2.getData().getBigInteger().intValue();
/* 125 */       if (this.msgType != 22)
/* 126 */         throw new KrbApErrException(40);
/*     */     }
/*     */     else {
/* 129 */       throw new Asn1Exception(906);
/*     */     }
/* 131 */     localDerValue2 = localDerValue1.getData().getDerValue();
/* 132 */     if ((localDerValue2.getTag() & 0x1F) == 2) {
/* 133 */       DerValue localDerValue3 = localDerValue2.getData().getDerValue();
/* 134 */       if (localDerValue3.getTag() != 48) {
/* 135 */         throw new Asn1Exception(906);
/*     */       }
/* 137 */       Vector localVector = new Vector();
/* 138 */       while (localDerValue3.getData().available() > 0) {
/* 139 */         localVector.addElement(new Ticket(localDerValue3.getData().getDerValue()));
/*     */       }
/* 141 */       if (localVector.size() > 0) {
/* 142 */         this.tickets = new Ticket[localVector.size()];
/* 143 */         localVector.copyInto(this.tickets);
/*     */       }
/*     */     } else {
/* 146 */       throw new Asn1Exception(906);
/*     */     }
/* 148 */     this.encPart = EncryptedData.parse(localDerValue1.getData(), (byte)3, false);
/*     */ 
/* 150 */     if (localDerValue1.getData().available() > 0)
/* 151 */       throw new Asn1Exception(906);
/*     */   }
/*     */ 
/*     */   public byte[] asn1Encode()
/*     */     throws Asn1Exception, IOException
/*     */   {
/* 163 */     DerOutputStream localDerOutputStream1 = new DerOutputStream();
/* 164 */     localDerOutputStream1.putInteger(BigInteger.valueOf(this.pvno));
/* 165 */     DerOutputStream localDerOutputStream3 = new DerOutputStream();
/* 166 */     localDerOutputStream3.write(DerValue.createTag((byte)-128, true, (byte)0), localDerOutputStream1);
/*     */ 
/* 168 */     localDerOutputStream1 = new DerOutputStream();
/* 169 */     localDerOutputStream1.putInteger(BigInteger.valueOf(this.msgType));
/* 170 */     localDerOutputStream3.write(DerValue.createTag((byte)-128, true, (byte)1), localDerOutputStream1);
/*     */ 
/* 172 */     localDerOutputStream1 = new DerOutputStream();
/* 173 */     for (int i = 0; i < this.tickets.length; i++) {
/* 174 */       localDerOutputStream1.write(this.tickets[i].asn1Encode());
/*     */     }
/* 176 */     DerOutputStream localDerOutputStream2 = new DerOutputStream();
/* 177 */     localDerOutputStream2.write((byte)48, localDerOutputStream1);
/* 178 */     localDerOutputStream3.write(DerValue.createTag((byte)-128, true, (byte)2), localDerOutputStream2);
/*     */ 
/* 180 */     localDerOutputStream3.write(DerValue.createTag((byte)-128, true, (byte)3), this.encPart.asn1Encode());
/*     */ 
/* 182 */     localDerOutputStream2 = new DerOutputStream();
/* 183 */     localDerOutputStream2.write((byte)48, localDerOutputStream3);
/* 184 */     localDerOutputStream3 = new DerOutputStream();
/* 185 */     localDerOutputStream3.write(DerValue.createTag((byte)64, true, (byte)22), localDerOutputStream2);
/*     */ 
/* 187 */     return localDerOutputStream3.toByteArray();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.internal.KRBCred
 * JD-Core Version:    0.6.2
 */