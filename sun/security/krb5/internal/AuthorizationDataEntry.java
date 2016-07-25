/*     */ package sun.security.krb5.internal;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.math.BigInteger;
/*     */ import sun.security.krb5.Asn1Exception;
/*     */ import sun.security.krb5.internal.ccache.CCacheOutputStream;
/*     */ import sun.security.util.DerInputStream;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.DerValue;
/*     */ 
/*     */ public class AuthorizationDataEntry
/*     */   implements Cloneable
/*     */ {
/*     */   public int adType;
/*     */   public byte[] adData;
/*     */ 
/*     */   private AuthorizationDataEntry()
/*     */   {
/*     */   }
/*     */ 
/*     */   public AuthorizationDataEntry(int paramInt, byte[] paramArrayOfByte)
/*     */   {
/*  48 */     this.adType = paramInt;
/*  49 */     this.adData = paramArrayOfByte;
/*     */   }
/*     */ 
/*     */   public Object clone() {
/*  53 */     AuthorizationDataEntry localAuthorizationDataEntry = new AuthorizationDataEntry();
/*     */ 
/*  55 */     localAuthorizationDataEntry.adType = this.adType;
/*  56 */     if (this.adData != null) {
/*  57 */       localAuthorizationDataEntry.adData = new byte[this.adData.length];
/*  58 */       System.arraycopy(this.adData, 0, localAuthorizationDataEntry.adData, 0, this.adData.length);
/*     */     }
/*     */ 
/*  61 */     return localAuthorizationDataEntry;
/*     */   }
/*     */ 
/*     */   public AuthorizationDataEntry(DerValue paramDerValue)
/*     */     throws Asn1Exception, IOException
/*     */   {
/*  70 */     if (paramDerValue.getTag() != 48) {
/*  71 */       throw new Asn1Exception(906);
/*     */     }
/*  73 */     DerValue localDerValue = paramDerValue.getData().getDerValue();
/*  74 */     if ((localDerValue.getTag() & 0x1F) == 0)
/*  75 */       this.adType = localDerValue.getData().getBigInteger().intValue();
/*     */     else {
/*  77 */       throw new Asn1Exception(906);
/*     */     }
/*  79 */     localDerValue = paramDerValue.getData().getDerValue();
/*  80 */     if ((localDerValue.getTag() & 0x1F) == 1)
/*  81 */       this.adData = localDerValue.getData().getOctetString();
/*     */     else {
/*  83 */       throw new Asn1Exception(906);
/*     */     }
/*  85 */     if (paramDerValue.getData().available() > 0)
/*  86 */       throw new Asn1Exception(906);
/*     */   }
/*     */ 
/*     */   public byte[] asn1Encode()
/*     */     throws Asn1Exception, IOException
/*     */   {
/*  97 */     DerOutputStream localDerOutputStream1 = new DerOutputStream();
/*  98 */     DerOutputStream localDerOutputStream2 = new DerOutputStream();
/*  99 */     localDerOutputStream2.putInteger(this.adType);
/* 100 */     localDerOutputStream1.write(DerValue.createTag((byte)-128, true, (byte)0), localDerOutputStream2);
/* 101 */     localDerOutputStream2 = new DerOutputStream();
/* 102 */     localDerOutputStream2.putOctetString(this.adData);
/* 103 */     localDerOutputStream1.write(DerValue.createTag((byte)-128, true, (byte)1), localDerOutputStream2);
/* 104 */     localDerOutputStream2 = new DerOutputStream();
/* 105 */     localDerOutputStream2.write((byte)48, localDerOutputStream1);
/* 106 */     return localDerOutputStream2.toByteArray();
/*     */   }
/*     */ 
/*     */   public void writeEntry(CCacheOutputStream paramCCacheOutputStream)
/*     */     throws IOException
/*     */   {
/* 116 */     paramCCacheOutputStream.write16(this.adType);
/* 117 */     paramCCacheOutputStream.write32(this.adData.length);
/* 118 */     paramCCacheOutputStream.write(this.adData, 0, this.adData.length);
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 122 */     return "adType=" + this.adType + " adData.length=" + this.adData.length;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.internal.AuthorizationDataEntry
 * JD-Core Version:    0.6.2
 */