/*     */ package sun.security.provider;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.math.BigInteger;
/*     */ import java.security.AlgorithmParametersSpi;
/*     */ import java.security.spec.AlgorithmParameterSpec;
/*     */ import java.security.spec.DSAParameterSpec;
/*     */ import java.security.spec.InvalidParameterSpecException;
/*     */ import sun.security.util.Debug;
/*     */ import sun.security.util.DerInputStream;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.DerValue;
/*     */ 
/*     */ public class DSAParameters extends AlgorithmParametersSpi
/*     */ {
/*     */   protected BigInteger p;
/*     */   protected BigInteger q;
/*     */   protected BigInteger g;
/*     */ 
/*     */   protected void engineInit(AlgorithmParameterSpec paramAlgorithmParameterSpec)
/*     */     throws InvalidParameterSpecException
/*     */   {
/*  64 */     if (!(paramAlgorithmParameterSpec instanceof DSAParameterSpec)) {
/*  65 */       throw new InvalidParameterSpecException("Inappropriate parameter specification");
/*     */     }
/*     */ 
/*  68 */     this.p = ((DSAParameterSpec)paramAlgorithmParameterSpec).getP();
/*  69 */     this.q = ((DSAParameterSpec)paramAlgorithmParameterSpec).getQ();
/*  70 */     this.g = ((DSAParameterSpec)paramAlgorithmParameterSpec).getG();
/*     */   }
/*     */ 
/*     */   protected void engineInit(byte[] paramArrayOfByte) throws IOException {
/*  74 */     DerValue localDerValue = new DerValue(paramArrayOfByte);
/*     */ 
/*  76 */     if (localDerValue.tag != 48) {
/*  77 */       throw new IOException("DSA params parsing error");
/*     */     }
/*     */ 
/*  80 */     localDerValue.data.reset();
/*     */ 
/*  82 */     this.p = localDerValue.data.getBigInteger();
/*  83 */     this.q = localDerValue.data.getBigInteger();
/*  84 */     this.g = localDerValue.data.getBigInteger();
/*     */ 
/*  86 */     if (localDerValue.data.available() != 0)
/*  87 */       throw new IOException("encoded params have " + localDerValue.data.available() + " extra bytes");
/*     */   }
/*     */ 
/*     */   protected void engineInit(byte[] paramArrayOfByte, String paramString)
/*     */     throws IOException
/*     */   {
/*  95 */     engineInit(paramArrayOfByte);
/*     */   }
/*     */ 
/*     */   protected <T extends AlgorithmParameterSpec> T engineGetParameterSpec(Class<T> paramClass)
/*     */     throws InvalidParameterSpecException
/*     */   {
/*     */     try
/*     */     {
/* 103 */       Class localClass = Class.forName("java.security.spec.DSAParameterSpec");
/*     */ 
/* 105 */       if (localClass.isAssignableFrom(paramClass)) {
/* 106 */         return new DSAParameterSpec(this.p, this.q, this.g);
/*     */       }
/* 108 */       throw new InvalidParameterSpecException("Inappropriate parameter Specification");
/*     */     }
/*     */     catch (ClassNotFoundException localClassNotFoundException)
/*     */     {
/* 112 */       throw new InvalidParameterSpecException("Unsupported parameter specification: " + localClassNotFoundException.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   protected byte[] engineGetEncoded() throws IOException
/*     */   {
/* 118 */     DerOutputStream localDerOutputStream1 = new DerOutputStream();
/* 119 */     DerOutputStream localDerOutputStream2 = new DerOutputStream();
/*     */ 
/* 121 */     localDerOutputStream2.putInteger(this.p);
/* 122 */     localDerOutputStream2.putInteger(this.q);
/* 123 */     localDerOutputStream2.putInteger(this.g);
/* 124 */     localDerOutputStream1.write((byte)48, localDerOutputStream2);
/* 125 */     return localDerOutputStream1.toByteArray();
/*     */   }
/*     */ 
/*     */   protected byte[] engineGetEncoded(String paramString) throws IOException
/*     */   {
/* 130 */     return engineGetEncoded();
/*     */   }
/*     */ 
/*     */   protected String engineToString()
/*     */   {
/* 137 */     return "\n\tp: " + Debug.toHexString(this.p) + "\n\tq: " + Debug.toHexString(this.q) + "\n\tg: " + Debug.toHexString(this.g) + "\n";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.provider.DSAParameters
 * JD-Core Version:    0.6.2
 */