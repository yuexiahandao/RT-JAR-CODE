/*     */ package sun.security.x509;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.math.BigInteger;
/*     */ import java.security.ProviderException;
/*     */ import java.security.interfaces.DSAParams;
/*     */ import sun.security.util.Debug;
/*     */ import sun.security.util.DerInputStream;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.DerValue;
/*     */ 
/*     */ public final class AlgIdDSA extends AlgorithmId
/*     */   implements DSAParams
/*     */ {
/*     */   private static final long serialVersionUID = 3437177836797504046L;
/*     */   private BigInteger p;
/*     */   private BigInteger q;
/*     */   private BigInteger g;
/*     */ 
/*     */   public BigInteger getP()
/*     */   {
/*  87 */     return this.p;
/*     */   }
/*     */   public BigInteger getQ() {
/*  90 */     return this.q;
/*     */   }
/*     */   public BigInteger getG() {
/*  93 */     return this.g;
/*     */   }
/*     */ 
/*     */   public AlgIdDSA()
/*     */   {
/*     */   }
/*     */ 
/*     */   AlgIdDSA(DerValue paramDerValue)
/*     */     throws IOException
/*     */   {
/* 103 */     super(paramDerValue.getOID());
/*     */   }
/*     */ 
/*     */   public AlgIdDSA(byte[] paramArrayOfByte)
/*     */     throws IOException
/*     */   {
/* 109 */     super(new DerValue(paramArrayOfByte).getOID());
/*     */   }
/*     */ 
/*     */   public AlgIdDSA(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3)
/*     */     throws IOException
/*     */   {
/* 123 */     this(new BigInteger(1, paramArrayOfByte1), new BigInteger(1, paramArrayOfByte2), new BigInteger(1, paramArrayOfByte3));
/*     */   }
/*     */ 
/*     */   public AlgIdDSA(BigInteger paramBigInteger1, BigInteger paramBigInteger2, BigInteger paramBigInteger3)
/*     */   {
/* 139 */     super(DSA_oid);
/*     */ 
/* 141 */     if ((paramBigInteger1 != null) || (paramBigInteger2 != null) || (paramBigInteger3 != null)) {
/* 142 */       if ((paramBigInteger1 == null) || (paramBigInteger2 == null) || (paramBigInteger3 == null))
/* 143 */         throw new ProviderException("Invalid parameters for DSS/DSA Algorithm ID");
/*     */       try
/*     */       {
/* 146 */         this.p = paramBigInteger1;
/* 147 */         this.q = paramBigInteger2;
/* 148 */         this.g = paramBigInteger3;
/* 149 */         initializeParams();
/*     */       }
/*     */       catch (IOException localIOException)
/*     */       {
/* 153 */         throw new ProviderException("Construct DSS/DSA Algorithm ID");
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 163 */     return "DSA";
/*     */   }
/*     */ 
/*     */   private void initializeParams()
/*     */     throws IOException
/*     */   {
/* 173 */     DerOutputStream localDerOutputStream = new DerOutputStream();
/*     */ 
/* 175 */     localDerOutputStream.putInteger(this.p);
/* 176 */     localDerOutputStream.putInteger(this.q);
/* 177 */     localDerOutputStream.putInteger(this.g);
/* 178 */     this.params = new DerValue((byte)48, localDerOutputStream.toByteArray());
/*     */   }
/*     */ 
/*     */   protected void decodeParams()
/*     */     throws IOException
/*     */   {
/* 188 */     if (this.params == null)
/* 189 */       throw new IOException("DSA alg params are null");
/* 190 */     if (this.params.tag != 48) {
/* 191 */       throw new IOException("DSA alg parsing error");
/*     */     }
/* 193 */     this.params.data.reset();
/*     */ 
/* 195 */     this.p = this.params.data.getBigInteger();
/* 196 */     this.q = this.params.data.getBigInteger();
/* 197 */     this.g = this.params.data.getBigInteger();
/*     */ 
/* 199 */     if (this.params.data.available() != 0)
/* 200 */       throw new IOException("AlgIdDSA params, extra=" + this.params.data.available());
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 209 */     return paramsToString();
/*     */   }
/*     */ 
/*     */   protected String paramsToString()
/*     */   {
/* 216 */     if (this.params == null) {
/* 217 */       return " null\n";
/*     */     }
/* 219 */     return "\n    p:\n" + Debug.toHexString(this.p) + "\n    q:\n" + Debug.toHexString(this.q) + "\n    g:\n" + Debug.toHexString(this.g) + "\n";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.x509.AlgIdDSA
 * JD-Core Version:    0.6.2
 */