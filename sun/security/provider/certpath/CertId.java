/*     */ package sun.security.provider.certpath;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.math.BigInteger;
/*     */ import java.security.MessageDigest;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.PublicKey;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.Arrays;
/*     */ import javax.security.auth.x500.X500Principal;
/*     */ import sun.misc.HexDumpEncoder;
/*     */ import sun.security.util.DerInputStream;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.DerValue;
/*     */ import sun.security.x509.AlgorithmId;
/*     */ import sun.security.x509.SerialNumber;
/*     */ 
/*     */ public class CertId
/*     */ {
/*     */   private static final boolean debug = false;
/*  59 */   private static final AlgorithmId SHA1_ALGID = new AlgorithmId(AlgorithmId.SHA_oid);
/*     */   private final AlgorithmId hashAlgId;
/*     */   private final byte[] issuerNameHash;
/*     */   private final byte[] issuerKeyHash;
/*     */   private final SerialNumber certSerialNumber;
/*  65 */   private int myhash = -1;
/*     */ 
/*     */   public CertId(X509Certificate paramX509Certificate, SerialNumber paramSerialNumber)
/*     */     throws IOException
/*     */   {
/*  74 */     MessageDigest localMessageDigest = null;
/*     */     try {
/*  76 */       localMessageDigest = MessageDigest.getInstance("SHA1");
/*     */     } catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
/*  78 */       throw new IOException("Unable to create CertId", localNoSuchAlgorithmException);
/*     */     }
/*  80 */     this.hashAlgId = SHA1_ALGID;
/*  81 */     localMessageDigest.update(paramX509Certificate.getSubjectX500Principal().getEncoded());
/*  82 */     this.issuerNameHash = localMessageDigest.digest();
/*     */ 
/*  85 */     byte[] arrayOfByte1 = paramX509Certificate.getPublicKey().getEncoded();
/*  86 */     DerValue localDerValue = new DerValue(arrayOfByte1);
/*  87 */     DerValue[] arrayOfDerValue = new DerValue[2];
/*  88 */     arrayOfDerValue[0] = localDerValue.data.getDerValue();
/*  89 */     arrayOfDerValue[1] = localDerValue.data.getDerValue();
/*  90 */     byte[] arrayOfByte2 = arrayOfDerValue[1].getBitString();
/*  91 */     localMessageDigest.update(arrayOfByte2);
/*  92 */     this.issuerKeyHash = localMessageDigest.digest();
/*  93 */     this.certSerialNumber = paramSerialNumber;
/*     */   }
/*     */ 
/*     */   public CertId(DerInputStream paramDerInputStream)
/*     */     throws IOException
/*     */   {
/* 110 */     this.hashAlgId = AlgorithmId.parse(paramDerInputStream.getDerValue());
/* 111 */     this.issuerNameHash = paramDerInputStream.getOctetString();
/* 112 */     this.issuerKeyHash = paramDerInputStream.getOctetString();
/* 113 */     this.certSerialNumber = new SerialNumber(paramDerInputStream);
/*     */   }
/*     */ 
/*     */   public AlgorithmId getHashAlgorithm()
/*     */   {
/* 120 */     return this.hashAlgId;
/*     */   }
/*     */ 
/*     */   public byte[] getIssuerNameHash()
/*     */   {
/* 127 */     return this.issuerNameHash;
/*     */   }
/*     */ 
/*     */   public byte[] getIssuerKeyHash()
/*     */   {
/* 134 */     return this.issuerKeyHash;
/*     */   }
/*     */ 
/*     */   public BigInteger getSerialNumber()
/*     */   {
/* 141 */     return this.certSerialNumber.getNumber();
/*     */   }
/*     */ 
/*     */   public void encode(DerOutputStream paramDerOutputStream)
/*     */     throws IOException
/*     */   {
/* 150 */     DerOutputStream localDerOutputStream = new DerOutputStream();
/* 151 */     this.hashAlgId.encode(localDerOutputStream);
/* 152 */     localDerOutputStream.putOctetString(this.issuerNameHash);
/* 153 */     localDerOutputStream.putOctetString(this.issuerKeyHash);
/* 154 */     this.certSerialNumber.encode(localDerOutputStream);
/* 155 */     paramDerOutputStream.write((byte)48, localDerOutputStream);
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 170 */     if (this.myhash == -1) {
/* 171 */       this.myhash = this.hashAlgId.hashCode();
/* 172 */       for (int i = 0; i < this.issuerNameHash.length; i++) {
/* 173 */         this.myhash += this.issuerNameHash[i] * i;
/*     */       }
/* 175 */       for (i = 0; i < this.issuerKeyHash.length; i++) {
/* 176 */         this.myhash += this.issuerKeyHash[i] * i;
/*     */       }
/* 178 */       this.myhash += this.certSerialNumber.getNumber().hashCode();
/*     */     }
/* 180 */     return this.myhash;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 193 */     if (this == paramObject) {
/* 194 */       return true;
/*     */     }
/* 196 */     if ((paramObject == null) || (!(paramObject instanceof CertId))) {
/* 197 */       return false;
/*     */     }
/*     */ 
/* 200 */     CertId localCertId = (CertId)paramObject;
/* 201 */     if ((this.hashAlgId.equals(localCertId.getHashAlgorithm())) && (Arrays.equals(this.issuerNameHash, localCertId.getIssuerNameHash())) && (Arrays.equals(this.issuerKeyHash, localCertId.getIssuerKeyHash())) && (this.certSerialNumber.getNumber().equals(localCertId.getSerialNumber())))
/*     */     {
/* 205 */       return true;
/*     */     }
/* 207 */     return false;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 215 */     StringBuilder localStringBuilder = new StringBuilder();
/* 216 */     localStringBuilder.append("CertId \n");
/* 217 */     localStringBuilder.append("Algorithm: " + this.hashAlgId.toString() + "\n");
/* 218 */     localStringBuilder.append("issuerNameHash \n");
/* 219 */     HexDumpEncoder localHexDumpEncoder = new HexDumpEncoder();
/* 220 */     localStringBuilder.append(localHexDumpEncoder.encode(this.issuerNameHash));
/* 221 */     localStringBuilder.append("\nissuerKeyHash: \n");
/* 222 */     localStringBuilder.append(localHexDumpEncoder.encode(this.issuerKeyHash));
/* 223 */     localStringBuilder.append("\n" + this.certSerialNumber.toString());
/* 224 */     return localStringBuilder.toString();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.provider.certpath.CertId
 * JD-Core Version:    0.6.2
 */