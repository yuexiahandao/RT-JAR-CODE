/*     */ package java.security.cert;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import sun.misc.HexDumpEncoder;
/*     */ import sun.security.util.DerInputStream;
/*     */ import sun.security.util.DerValue;
/*     */ import sun.security.util.ObjectIdentifier;
/*     */ 
/*     */ public class PolicyQualifierInfo
/*     */ {
/*     */   private byte[] mEncoded;
/*     */   private String mId;
/*     */   private byte[] mData;
/*     */   private String pqiString;
/*     */ 
/*     */   public PolicyQualifierInfo(byte[] paramArrayOfByte)
/*     */     throws IOException
/*     */   {
/* 101 */     this.mEncoded = ((byte[])paramArrayOfByte.clone());
/*     */ 
/* 103 */     DerValue localDerValue = new DerValue(this.mEncoded);
/* 104 */     if (localDerValue.tag != 48) {
/* 105 */       throw new IOException("Invalid encoding for PolicyQualifierInfo");
/*     */     }
/* 107 */     this.mId = localDerValue.data.getDerValue().getOID().toString();
/* 108 */     byte[] arrayOfByte = localDerValue.data.toByteArray();
/* 109 */     if (arrayOfByte == null) {
/* 110 */       this.mData = null;
/*     */     } else {
/* 112 */       this.mData = new byte[arrayOfByte.length];
/* 113 */       System.arraycopy(arrayOfByte, 0, this.mData, 0, arrayOfByte.length);
/*     */     }
/*     */   }
/*     */ 
/*     */   public final String getPolicyQualifierId()
/*     */   {
/* 126 */     return this.mId;
/*     */   }
/*     */ 
/*     */   public final byte[] getEncoded()
/*     */   {
/* 138 */     return (byte[])this.mEncoded.clone();
/*     */   }
/*     */ 
/*     */   public final byte[] getPolicyQualifier()
/*     */   {
/* 150 */     return this.mData == null ? null : (byte[])this.mData.clone();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 161 */     if (this.pqiString != null)
/* 162 */       return this.pqiString;
/* 163 */     HexDumpEncoder localHexDumpEncoder = new HexDumpEncoder();
/* 164 */     StringBuffer localStringBuffer = new StringBuffer();
/* 165 */     localStringBuffer.append("PolicyQualifierInfo: [\n");
/* 166 */     localStringBuffer.append("  qualifierID: " + this.mId + "\n");
/* 167 */     localStringBuffer.append("  qualifier: " + (this.mData == null ? "null" : localHexDumpEncoder.encodeBuffer(this.mData)) + "\n");
/*     */ 
/* 169 */     localStringBuffer.append("]");
/* 170 */     this.pqiString = localStringBuffer.toString();
/* 171 */     return this.pqiString;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.cert.PolicyQualifierInfo
 * JD-Core Version:    0.6.2
 */