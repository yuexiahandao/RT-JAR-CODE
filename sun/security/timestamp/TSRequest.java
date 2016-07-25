/*     */ package sun.security.timestamp;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.math.BigInteger;
/*     */ import java.security.cert.X509Extension;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.ObjectIdentifier;
/*     */ 
/*     */ public class TSRequest
/*     */ {
/*  78 */   private static final ObjectIdentifier SHA1_OID = localObjectIdentifier1;
/*  79 */   private static final ObjectIdentifier MD5_OID = localObjectIdentifier2;
/*     */ 
/*  82 */   private int version = 1;
/*     */ 
/*  84 */   private ObjectIdentifier hashAlgorithmId = null;
/*     */   private byte[] hashValue;
/*  88 */   private String policyId = null;
/*     */ 
/*  90 */   private BigInteger nonce = null;
/*     */ 
/*  92 */   private boolean returnCertificate = false;
/*     */ 
/*  94 */   private X509Extension[] extensions = null;
/*     */ 
/*     */   public TSRequest(byte[] paramArrayOfByte, String paramString)
/*     */   {
/* 105 */     if ("MD5".equalsIgnoreCase(paramString)) {
/* 106 */       this.hashAlgorithmId = MD5_OID;
/*     */ 
/* 108 */       if ((!$assertionsDisabled) && (paramArrayOfByte.length != 16)) throw new AssertionError();
/*     */     }
/* 110 */     else if (("SHA-1".equalsIgnoreCase(paramString)) || ("SHA".equalsIgnoreCase(paramString)) || ("SHA1".equalsIgnoreCase(paramString)))
/*     */     {
/* 113 */       this.hashAlgorithmId = SHA1_OID;
/*     */ 
/* 115 */       assert (paramArrayOfByte.length == 20);
/*     */     }
/*     */ 
/* 119 */     this.hashValue = new byte[paramArrayOfByte.length];
/* 120 */     System.arraycopy(paramArrayOfByte, 0, this.hashValue, 0, paramArrayOfByte.length);
/*     */   }
/*     */ 
/*     */   public void setVersion(int paramInt)
/*     */   {
/* 129 */     this.version = paramInt;
/*     */   }
/*     */ 
/*     */   public void setPolicyId(String paramString)
/*     */   {
/* 138 */     this.policyId = paramString;
/*     */   }
/*     */ 
/*     */   public void setNonce(BigInteger paramBigInteger)
/*     */   {
/* 148 */     this.nonce = paramBigInteger;
/*     */   }
/*     */ 
/*     */   public void requestCertificate(boolean paramBoolean)
/*     */   {
/* 158 */     this.returnCertificate = paramBoolean;
/*     */   }
/*     */ 
/*     */   public void setExtensions(X509Extension[] paramArrayOfX509Extension)
/*     */   {
/* 167 */     this.extensions = paramArrayOfX509Extension;
/*     */   }
/*     */ 
/*     */   public byte[] encode() throws IOException
/*     */   {
/* 172 */     DerOutputStream localDerOutputStream1 = new DerOutputStream();
/*     */ 
/* 175 */     localDerOutputStream1.putInteger(this.version);
/*     */ 
/* 178 */     DerOutputStream localDerOutputStream2 = new DerOutputStream();
/* 179 */     DerOutputStream localDerOutputStream3 = new DerOutputStream();
/* 180 */     localDerOutputStream3.putOID(this.hashAlgorithmId);
/* 181 */     localDerOutputStream2.write((byte)48, localDerOutputStream3);
/* 182 */     localDerOutputStream2.putOctetString(this.hashValue);
/* 183 */     localDerOutputStream1.write((byte)48, localDerOutputStream2);
/*     */ 
/* 187 */     if (this.policyId != null) {
/* 188 */       localDerOutputStream1.putOID(new ObjectIdentifier(this.policyId));
/*     */     }
/* 190 */     if (this.nonce != null) {
/* 191 */       localDerOutputStream1.putInteger(this.nonce);
/*     */     }
/* 193 */     if (this.returnCertificate) {
/* 194 */       localDerOutputStream1.putBoolean(true);
/*     */     }
/*     */ 
/* 197 */     DerOutputStream localDerOutputStream4 = new DerOutputStream();
/* 198 */     localDerOutputStream4.write((byte)48, localDerOutputStream1);
/* 199 */     return localDerOutputStream4.toByteArray();
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  70 */     ObjectIdentifier localObjectIdentifier1 = null;
/*  71 */     ObjectIdentifier localObjectIdentifier2 = null;
/*     */     try {
/*  73 */       localObjectIdentifier1 = new ObjectIdentifier("1.3.14.3.2.26");
/*  74 */       localObjectIdentifier2 = new ObjectIdentifier("1.2.840.113549.2.5");
/*     */     }
/*     */     catch (IOException localIOException)
/*     */     {
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.timestamp.TSRequest
 * JD-Core Version:    0.6.2
 */