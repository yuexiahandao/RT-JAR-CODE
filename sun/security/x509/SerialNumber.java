/*     */ package sun.security.x509;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.math.BigInteger;
/*     */ import sun.security.util.Debug;
/*     */ import sun.security.util.DerInputStream;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.DerValue;
/*     */ 
/*     */ public class SerialNumber
/*     */ {
/*     */   private BigInteger serialNum;
/*     */ 
/*     */   private void construct(DerValue paramDerValue)
/*     */     throws IOException
/*     */   {
/*  44 */     this.serialNum = paramDerValue.getBigInteger();
/*  45 */     if (paramDerValue.data.available() != 0)
/*  46 */       throw new IOException("Excess SerialNumber data");
/*     */   }
/*     */ 
/*     */   public SerialNumber(BigInteger paramBigInteger)
/*     */   {
/*  56 */     this.serialNum = paramBigInteger;
/*     */   }
/*     */ 
/*     */   public SerialNumber(int paramInt)
/*     */   {
/*  65 */     this.serialNum = BigInteger.valueOf(paramInt);
/*     */   }
/*     */ 
/*     */   public SerialNumber(DerInputStream paramDerInputStream)
/*     */     throws IOException
/*     */   {
/*  75 */     DerValue localDerValue = paramDerInputStream.getDerValue();
/*  76 */     construct(localDerValue);
/*     */   }
/*     */ 
/*     */   public SerialNumber(DerValue paramDerValue)
/*     */     throws IOException
/*     */   {
/*  86 */     construct(paramDerValue);
/*     */   }
/*     */ 
/*     */   public SerialNumber(InputStream paramInputStream)
/*     */     throws IOException
/*     */   {
/*  96 */     DerValue localDerValue = new DerValue(paramInputStream);
/*  97 */     construct(localDerValue);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 104 */     return "SerialNumber: [" + Debug.toHexString(this.serialNum) + "]";
/*     */   }
/*     */ 
/*     */   public void encode(DerOutputStream paramDerOutputStream)
/*     */     throws IOException
/*     */   {
/* 114 */     paramDerOutputStream.putInteger(this.serialNum);
/*     */   }
/*     */ 
/*     */   public BigInteger getNumber()
/*     */   {
/* 121 */     return this.serialNum;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.x509.SerialNumber
 * JD-Core Version:    0.6.2
 */