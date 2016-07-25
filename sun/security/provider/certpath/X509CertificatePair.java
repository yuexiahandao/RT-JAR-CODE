/*     */ package sun.security.provider.certpath;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.security.GeneralSecurityException;
/*     */ import java.security.PublicKey;
/*     */ import java.security.cert.CertificateEncodingException;
/*     */ import java.security.cert.CertificateException;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.security.interfaces.DSAPublicKey;
/*     */ import javax.security.auth.x500.X500Principal;
/*     */ import sun.security.provider.X509Factory;
/*     */ import sun.security.util.Cache;
/*     */ import sun.security.util.Cache.EqualByteArray;
/*     */ import sun.security.util.DerInputStream;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.DerValue;
/*     */ import sun.security.x509.X509CertImpl;
/*     */ 
/*     */ public class X509CertificatePair
/*     */ {
/*     */   private static final byte TAG_FORWARD = 0;
/*     */   private static final byte TAG_REVERSE = 1;
/*     */   private X509Certificate forward;
/*     */   private X509Certificate reverse;
/*     */   private byte[] encoded;
/*  82 */   private static final Cache cache = Cache.newSoftMemoryCache(750);
/*     */ 
/*     */   public X509CertificatePair()
/*     */   {
/*     */   }
/*     */ 
/*     */   public X509CertificatePair(X509Certificate paramX509Certificate1, X509Certificate paramX509Certificate2)
/*     */     throws CertificateException
/*     */   {
/* 101 */     if ((paramX509Certificate1 == null) && (paramX509Certificate2 == null)) {
/* 102 */       throw new CertificateException("at least one of certificate pair must be non-null");
/*     */     }
/*     */ 
/* 106 */     this.forward = paramX509Certificate1;
/* 107 */     this.reverse = paramX509Certificate2;
/*     */ 
/* 109 */     checkPair();
/*     */   }
/*     */ 
/*     */   private X509CertificatePair(byte[] paramArrayOfByte)
/*     */     throws CertificateException
/*     */   {
/*     */     try
/*     */     {
/* 119 */       parse(new DerValue(paramArrayOfByte));
/* 120 */       this.encoded = paramArrayOfByte;
/*     */     } catch (IOException localIOException) {
/* 122 */       throw new CertificateException(localIOException.toString());
/*     */     }
/* 124 */     checkPair();
/*     */   }
/*     */ 
/*     */   public static synchronized void clearCache()
/*     */   {
/* 131 */     cache.clear();
/*     */   }
/*     */ 
/*     */   public static synchronized X509CertificatePair generateCertificatePair(byte[] paramArrayOfByte)
/*     */     throws CertificateException
/*     */   {
/* 140 */     Cache.EqualByteArray localEqualByteArray = new Cache.EqualByteArray(paramArrayOfByte);
/* 141 */     X509CertificatePair localX509CertificatePair = (X509CertificatePair)cache.get(localEqualByteArray);
/* 142 */     if (localX509CertificatePair != null) {
/* 143 */       return localX509CertificatePair;
/*     */     }
/* 145 */     localX509CertificatePair = new X509CertificatePair(paramArrayOfByte);
/* 146 */     localEqualByteArray = new Cache.EqualByteArray(localX509CertificatePair.encoded);
/* 147 */     cache.put(localEqualByteArray, localX509CertificatePair);
/* 148 */     return localX509CertificatePair;
/*     */   }
/*     */ 
/*     */   public void setForward(X509Certificate paramX509Certificate)
/*     */     throws CertificateException
/*     */   {
/* 155 */     checkPair();
/* 156 */     this.forward = paramX509Certificate;
/*     */   }
/*     */ 
/*     */   public void setReverse(X509Certificate paramX509Certificate)
/*     */     throws CertificateException
/*     */   {
/* 163 */     checkPair();
/* 164 */     this.reverse = paramX509Certificate;
/*     */   }
/*     */ 
/*     */   public X509Certificate getForward()
/*     */   {
/* 173 */     return this.forward;
/*     */   }
/*     */ 
/*     */   public X509Certificate getReverse()
/*     */   {
/* 182 */     return this.reverse;
/*     */   }
/*     */ 
/*     */   public byte[] getEncoded()
/*     */     throws CertificateEncodingException
/*     */   {
/*     */     try
/*     */     {
/* 193 */       if (this.encoded == null) {
/* 194 */         DerOutputStream localDerOutputStream = new DerOutputStream();
/* 195 */         emit(localDerOutputStream);
/* 196 */         this.encoded = localDerOutputStream.toByteArray();
/*     */       }
/*     */     } catch (IOException localIOException) {
/* 199 */       throw new CertificateEncodingException(localIOException.toString());
/*     */     }
/* 201 */     return this.encoded;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 210 */     StringBuffer localStringBuffer = new StringBuffer();
/* 211 */     localStringBuffer.append("X.509 Certificate Pair: [\n");
/* 212 */     if (this.forward != null)
/* 213 */       localStringBuffer.append("  Forward: " + this.forward + "\n");
/* 214 */     if (this.reverse != null)
/* 215 */       localStringBuffer.append("  Reverse: " + this.reverse + "\n");
/* 216 */     localStringBuffer.append("]");
/* 217 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   private void parse(DerValue paramDerValue)
/*     */     throws IOException, CertificateException
/*     */   {
/* 224 */     if (paramDerValue.tag != 48) {
/* 225 */       throw new IOException("Sequence tag missing for X509CertificatePair");
/*     */     }
/*     */ 
/* 229 */     while ((paramDerValue.data != null) && (paramDerValue.data.available() != 0)) {
/* 230 */       DerValue localDerValue = paramDerValue.data.getDerValue();
/* 231 */       int i = (short)(byte)(localDerValue.tag & 0x1F);
/* 232 */       switch (i) {
/*     */       case 0:
/* 234 */         if ((localDerValue.isContextSpecific()) && (localDerValue.isConstructed())) {
/* 235 */           if (this.forward != null) {
/* 236 */             throw new IOException("Duplicate forward certificate in X509CertificatePair");
/*     */           }
/*     */ 
/* 239 */           localDerValue = localDerValue.data.getDerValue();
/* 240 */           this.forward = X509Factory.intern(new X509CertImpl(localDerValue.toByteArray())); } break;
/*     */       case 1:
/* 245 */         if ((localDerValue.isContextSpecific()) && (localDerValue.isConstructed())) {
/* 246 */           if (this.reverse != null) {
/* 247 */             throw new IOException("Duplicate reverse certificate in X509CertificatePair");
/*     */           }
/*     */ 
/* 250 */           localDerValue = localDerValue.data.getDerValue();
/* 251 */           this.reverse = X509Factory.intern(new X509CertImpl(localDerValue.toByteArray())); } break;
/*     */       default:
/* 256 */         throw new IOException("Invalid encoding of X509CertificatePair");
/*     */       }
/*     */     }
/*     */ 
/* 260 */     if ((this.forward == null) && (this.reverse == null))
/* 261 */       throw new CertificateException("at least one of certificate pair must be non-null");
/*     */   }
/*     */ 
/*     */   private void emit(DerOutputStream paramDerOutputStream)
/*     */     throws IOException, CertificateEncodingException
/*     */   {
/* 270 */     DerOutputStream localDerOutputStream1 = new DerOutputStream();
/*     */     DerOutputStream localDerOutputStream2;
/* 272 */     if (this.forward != null) {
/* 273 */       localDerOutputStream2 = new DerOutputStream();
/* 274 */       localDerOutputStream2.putDerValue(new DerValue(this.forward.getEncoded()));
/* 275 */       localDerOutputStream1.write(DerValue.createTag((byte)-128, true, (byte)0), localDerOutputStream2);
/*     */     }
/*     */ 
/* 279 */     if (this.reverse != null) {
/* 280 */       localDerOutputStream2 = new DerOutputStream();
/* 281 */       localDerOutputStream2.putDerValue(new DerValue(this.reverse.getEncoded()));
/* 282 */       localDerOutputStream1.write(DerValue.createTag((byte)-128, true, (byte)1), localDerOutputStream2);
/*     */     }
/*     */ 
/* 286 */     paramDerOutputStream.write((byte)48, localDerOutputStream1);
/*     */   }
/*     */ 
/*     */   private void checkPair()
/*     */     throws CertificateException
/*     */   {
/* 295 */     if ((this.forward == null) || (this.reverse == null)) {
/* 296 */       return;
/*     */     }
/*     */ 
/* 302 */     X500Principal localX500Principal1 = this.forward.getSubjectX500Principal();
/* 303 */     X500Principal localX500Principal2 = this.forward.getIssuerX500Principal();
/* 304 */     X500Principal localX500Principal3 = this.reverse.getSubjectX500Principal();
/* 305 */     X500Principal localX500Principal4 = this.reverse.getIssuerX500Principal();
/* 306 */     if ((!localX500Principal2.equals(localX500Principal3)) || (!localX500Principal4.equals(localX500Principal1))) {
/* 307 */       throw new CertificateException("subject and issuer names in forward and reverse certificates do not match");
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 313 */       PublicKey localPublicKey = this.reverse.getPublicKey();
/* 314 */       if ((!(localPublicKey instanceof DSAPublicKey)) || (((DSAPublicKey)localPublicKey).getParams() != null))
/*     */       {
/* 316 */         this.forward.verify(localPublicKey);
/*     */       }
/* 318 */       localPublicKey = this.forward.getPublicKey();
/* 319 */       if ((!(localPublicKey instanceof DSAPublicKey)) || (((DSAPublicKey)localPublicKey).getParams() != null))
/*     */       {
/* 321 */         this.reverse.verify(localPublicKey);
/*     */       }
/*     */     } catch (GeneralSecurityException localGeneralSecurityException) {
/* 324 */       throw new CertificateException("invalid signature: " + localGeneralSecurityException.getMessage());
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.provider.certpath.X509CertificatePair
 * JD-Core Version:    0.6.2
 */