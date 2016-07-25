/*     */ package sun.security.krb5.internal;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.math.BigInteger;
/*     */ import java.util.Vector;
/*     */ import sun.security.krb5.Asn1Exception;
/*     */ import sun.security.krb5.KrbException;
/*     */ import sun.security.util.DerInputStream;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.DerValue;
/*     */ 
/*     */ public class KDCReq
/*     */ {
/*     */   public KDCReqBody reqBody;
/*     */   private int pvno;
/*     */   private int msgType;
/*  64 */   private PAData[] pAData = null;
/*     */ 
/*     */   public KDCReq(PAData[] paramArrayOfPAData, KDCReqBody paramKDCReqBody, int paramInt) throws IOException
/*     */   {
/*  68 */     this.pvno = 5;
/*  69 */     this.msgType = paramInt;
/*  70 */     if (paramArrayOfPAData != null) {
/*  71 */       this.pAData = new PAData[paramArrayOfPAData.length];
/*  72 */       for (int i = 0; i < paramArrayOfPAData.length; i++) {
/*  73 */         if (paramArrayOfPAData[i] == null) {
/*  74 */           throw new IOException("Cannot create a KDCRep");
/*     */         }
/*  76 */         this.pAData[i] = ((PAData)paramArrayOfPAData[i].clone());
/*     */       }
/*     */     }
/*     */ 
/*  80 */     this.reqBody = paramKDCReqBody;
/*     */   }
/*     */ 
/*     */   public KDCReq()
/*     */   {
/*     */   }
/*     */ 
/*     */   public KDCReq(byte[] paramArrayOfByte, int paramInt) throws Asn1Exception, IOException, KrbException {
/*  88 */     init(new DerValue(paramArrayOfByte), paramInt);
/*     */   }
/*     */ 
/*     */   public KDCReq(DerValue paramDerValue, int paramInt)
/*     */     throws Asn1Exception, IOException, KrbException
/*     */   {
/* 102 */     init(paramDerValue, paramInt);
/*     */   }
/*     */ 
/*     */   protected void init(DerValue paramDerValue, int paramInt)
/*     */     throws Asn1Exception, IOException, KrbException
/*     */   {
/* 120 */     if ((paramDerValue.getTag() & 0x1F) != paramInt) {
/* 121 */       throw new Asn1Exception(906);
/*     */     }
/* 123 */     DerValue localDerValue1 = paramDerValue.getData().getDerValue();
/* 124 */     if (localDerValue1.getTag() != 48) {
/* 125 */       throw new Asn1Exception(906);
/*     */     }
/* 127 */     DerValue localDerValue2 = localDerValue1.getData().getDerValue();
/*     */     BigInteger localBigInteger;
/* 128 */     if ((localDerValue2.getTag() & 0x1F) == 1) {
/* 129 */       localBigInteger = localDerValue2.getData().getBigInteger();
/* 130 */       this.pvno = localBigInteger.intValue();
/* 131 */       if (this.pvno != 5)
/* 132 */         throw new KrbApErrException(39);
/*     */     }
/*     */     else {
/* 135 */       throw new Asn1Exception(906);
/*     */     }
/* 137 */     localDerValue2 = localDerValue1.getData().getDerValue();
/* 138 */     if ((localDerValue2.getTag() & 0x1F) == 2) {
/* 139 */       localBigInteger = localDerValue2.getData().getBigInteger();
/* 140 */       this.msgType = localBigInteger.intValue();
/* 141 */       if (this.msgType != paramInt)
/* 142 */         throw new KrbApErrException(40);
/*     */     }
/*     */     else {
/* 145 */       throw new Asn1Exception(906);
/*     */     }
/*     */     DerValue localDerValue3;
/* 147 */     if ((localDerValue1.getData().peekByte() & 0x1F) == 3) {
/* 148 */       localDerValue2 = localDerValue1.getData().getDerValue();
/* 149 */       localDerValue3 = localDerValue2.getData().getDerValue();
/* 150 */       if (localDerValue3.getTag() != 48) {
/* 151 */         throw new Asn1Exception(906);
/*     */       }
/* 153 */       Vector localVector = new Vector();
/* 154 */       while (localDerValue3.getData().available() > 0) {
/* 155 */         localVector.addElement(new PAData(localDerValue3.getData().getDerValue()));
/*     */       }
/* 157 */       if (localVector.size() > 0) {
/* 158 */         this.pAData = new PAData[localVector.size()];
/* 159 */         localVector.copyInto(this.pAData);
/*     */       }
/*     */     } else {
/* 162 */       this.pAData = null;
/*     */     }
/* 164 */     localDerValue2 = localDerValue1.getData().getDerValue();
/* 165 */     if ((localDerValue2.getTag() & 0x1F) == 4) {
/* 166 */       localDerValue3 = localDerValue2.getData().getDerValue();
/* 167 */       this.reqBody = new KDCReqBody(localDerValue3, this.msgType);
/*     */     } else {
/* 169 */       throw new Asn1Exception(906);
/*     */     }
/*     */   }
/*     */ 
/*     */   public byte[] asn1Encode()
/*     */     throws Asn1Exception, IOException
/*     */   {
/* 183 */     DerOutputStream localDerOutputStream1 = new DerOutputStream();
/* 184 */     localDerOutputStream1.putInteger(BigInteger.valueOf(this.pvno));
/* 185 */     DerOutputStream localDerOutputStream3 = new DerOutputStream();
/* 186 */     localDerOutputStream3.write(DerValue.createTag((byte)-128, true, (byte)1), localDerOutputStream1);
/*     */ 
/* 188 */     localDerOutputStream1 = new DerOutputStream();
/* 189 */     localDerOutputStream1.putInteger(BigInteger.valueOf(this.msgType));
/* 190 */     localDerOutputStream3.write(DerValue.createTag((byte)-128, true, (byte)2), localDerOutputStream1);
/*     */ 
/* 192 */     if ((this.pAData != null) && (this.pAData.length > 0)) {
/* 193 */       localDerOutputStream1 = new DerOutputStream();
/* 194 */       for (int i = 0; i < this.pAData.length; i++) {
/* 195 */         localDerOutputStream1.write(this.pAData[i].asn1Encode());
/*     */       }
/* 197 */       localDerOutputStream2 = new DerOutputStream();
/* 198 */       localDerOutputStream2.write((byte)48, localDerOutputStream1);
/* 199 */       localDerOutputStream3.write(DerValue.createTag((byte)-128, true, (byte)3), localDerOutputStream2);
/*     */     }
/*     */ 
/* 202 */     localDerOutputStream3.write(DerValue.createTag((byte)-128, true, (byte)4), this.reqBody.asn1Encode(this.msgType));
/*     */ 
/* 204 */     DerOutputStream localDerOutputStream2 = new DerOutputStream();
/* 205 */     localDerOutputStream2.write((byte)48, localDerOutputStream3);
/* 206 */     localDerOutputStream3 = new DerOutputStream();
/* 207 */     localDerOutputStream3.write(DerValue.createTag((byte)64, true, (byte)this.msgType), localDerOutputStream2);
/*     */ 
/* 209 */     return localDerOutputStream3.toByteArray();
/*     */   }
/*     */ 
/*     */   public byte[] asn1EncodeReqBody() throws Asn1Exception, IOException {
/* 213 */     return this.reqBody.asn1Encode(this.msgType);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.internal.KDCReq
 * JD-Core Version:    0.6.2
 */