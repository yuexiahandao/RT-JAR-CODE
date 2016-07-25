/*     */ package sun.security.timestamp;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.math.BigInteger;
/*     */ import java.util.Date;
/*     */ import sun.security.util.DerInputStream;
/*     */ import sun.security.util.DerValue;
/*     */ import sun.security.util.ObjectIdentifier;
/*     */ import sun.security.x509.AlgorithmId;
/*     */ 
/*     */ public class TimestampToken
/*     */ {
/*     */   private int version;
/*     */   private ObjectIdentifier policy;
/*     */   private BigInteger serialNumber;
/*     */   private AlgorithmId hashAlgorithm;
/*     */   private byte[] hashedMessage;
/*     */   private Date genTime;
/*     */   private BigInteger nonce;
/*     */ 
/*     */   public TimestampToken(byte[] paramArrayOfByte)
/*     */     throws IOException
/*     */   {
/*  90 */     if (paramArrayOfByte == null) {
/*  91 */       throw new IOException("No timestamp token info");
/*     */     }
/*  93 */     parse(paramArrayOfByte);
/*     */   }
/*     */ 
/*     */   public Date getDate()
/*     */   {
/* 102 */     return this.genTime;
/*     */   }
/*     */ 
/*     */   public AlgorithmId getHashAlgorithm() {
/* 106 */     return this.hashAlgorithm;
/*     */   }
/*     */ 
/*     */   public byte[] getHashedMessage()
/*     */   {
/* 111 */     return this.hashedMessage;
/*     */   }
/*     */ 
/*     */   public BigInteger getNonce() {
/* 115 */     return this.nonce;
/*     */   }
/*     */ 
/*     */   public BigInteger getSerialNumber() {
/* 119 */     return this.serialNumber;
/*     */   }
/*     */ 
/*     */   private void parse(byte[] paramArrayOfByte)
/*     */     throws IOException
/*     */   {
/* 132 */     DerValue localDerValue1 = new DerValue(paramArrayOfByte);
/* 133 */     if (localDerValue1.tag != 48) {
/* 134 */       throw new IOException("Bad encoding for timestamp token info");
/*     */     }
/*     */ 
/* 137 */     this.version = localDerValue1.data.getInteger();
/*     */ 
/* 140 */     this.policy = localDerValue1.data.getOID();
/*     */ 
/* 143 */     DerValue localDerValue2 = localDerValue1.data.getDerValue();
/* 144 */     this.hashAlgorithm = AlgorithmId.parse(localDerValue2.data.getDerValue());
/* 145 */     this.hashedMessage = localDerValue2.data.getOctetString();
/*     */ 
/* 148 */     this.serialNumber = localDerValue1.data.getBigInteger();
/*     */ 
/* 151 */     this.genTime = localDerValue1.data.getGeneralizedTime();
/*     */ 
/* 154 */     while (localDerValue1.data.available() > 0) {
/* 155 */       DerValue localDerValue3 = localDerValue1.data.getDerValue();
/* 156 */       if (localDerValue3.tag == 2) {
/* 157 */         this.nonce = localDerValue3.getBigInteger();
/* 158 */         break;
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.timestamp.TimestampToken
 * JD-Core Version:    0.6.2
 */