/*     */ package sun.security.x509;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.security.MessageDigest;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.PublicKey;
/*     */ import java.util.Arrays;
/*     */ import sun.misc.HexDumpEncoder;
/*     */ import sun.security.util.BitArray;
/*     */ import sun.security.util.DerInputStream;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.DerValue;
/*     */ 
/*     */ public class KeyIdentifier
/*     */ {
/*     */   private byte[] octetString;
/*     */ 
/*     */   public KeyIdentifier(byte[] paramArrayOfByte)
/*     */   {
/*  51 */     this.octetString = ((byte[])paramArrayOfByte.clone());
/*     */   }
/*     */ 
/*     */   public KeyIdentifier(DerValue paramDerValue)
/*     */     throws IOException
/*     */   {
/*  60 */     this.octetString = paramDerValue.getOctetString();
/*     */   }
/*     */ 
/*     */   public KeyIdentifier(PublicKey paramPublicKey)
/*     */     throws IOException
/*     */   {
/*  85 */     DerValue localDerValue = new DerValue(paramPublicKey.getEncoded());
/*  86 */     if (localDerValue.tag != 48) {
/*  87 */       throw new IOException("PublicKey value is not a valid X.509 public key");
/*     */     }
/*     */ 
/*  90 */     AlgorithmId localAlgorithmId = AlgorithmId.parse(localDerValue.data.getDerValue());
/*  91 */     byte[] arrayOfByte = localDerValue.data.getUnalignedBitString().toByteArray();
/*     */ 
/*  93 */     MessageDigest localMessageDigest = null;
/*     */     try {
/*  95 */       localMessageDigest = MessageDigest.getInstance("SHA1");
/*     */     } catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
/*  97 */       throw new IOException("SHA1 not supported");
/*     */     }
/*  99 */     localMessageDigest.update(arrayOfByte);
/* 100 */     this.octetString = localMessageDigest.digest();
/*     */   }
/*     */ 
/*     */   public byte[] getIdentifier()
/*     */   {
/* 107 */     return (byte[])this.octetString.clone();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 114 */     String str = "KeyIdentifier [\n";
/*     */ 
/* 116 */     HexDumpEncoder localHexDumpEncoder = new HexDumpEncoder();
/* 117 */     str = str + localHexDumpEncoder.encodeBuffer(this.octetString);
/* 118 */     str = str + "]\n";
/* 119 */     return str;
/*     */   }
/*     */ 
/*     */   void encode(DerOutputStream paramDerOutputStream)
/*     */     throws IOException
/*     */   {
/* 129 */     paramDerOutputStream.putOctetString(this.octetString);
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 137 */     int i = 0;
/* 138 */     for (int j = 0; j < this.octetString.length; j++)
/* 139 */       i += this.octetString[j] * j;
/* 140 */     return i;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 147 */     if (this == paramObject)
/* 148 */       return true;
/* 149 */     if (!(paramObject instanceof KeyIdentifier))
/* 150 */       return false;
/* 151 */     return Arrays.equals(this.octetString, ((KeyIdentifier)paramObject).getIdentifier());
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.x509.KeyIdentifier
 * JD-Core Version:    0.6.2
 */