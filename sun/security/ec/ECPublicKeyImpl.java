/*     */ package sun.security.ec;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectStreamException;
/*     */ import java.security.AlgorithmParameters;
/*     */ import java.security.InvalidKeyException;
/*     */ import java.security.KeyRep;
/*     */ import java.security.KeyRep.Type;
/*     */ import java.security.interfaces.ECPublicKey;
/*     */ import java.security.spec.ECField;
/*     */ import java.security.spec.ECParameterSpec;
/*     */ import java.security.spec.ECPoint;
/*     */ import java.security.spec.EllipticCurve;
/*     */ import java.security.spec.InvalidParameterSpecException;
/*     */ import sun.security.x509.AlgorithmId;
/*     */ import sun.security.x509.X509Key;
/*     */ 
/*     */ public final class ECPublicKeyImpl extends X509Key
/*     */   implements ECPublicKey
/*     */ {
/*     */   private static final long serialVersionUID = -2462037275160462289L;
/*     */   private ECPoint w;
/*     */   private ECParameterSpec params;
/*     */ 
/*     */   public ECPublicKeyImpl(ECPoint paramECPoint, ECParameterSpec paramECParameterSpec)
/*     */     throws InvalidKeyException
/*     */   {
/*  56 */     this.w = paramECPoint;
/*  57 */     this.params = paramECParameterSpec;
/*     */ 
/*  59 */     this.algid = new AlgorithmId(AlgorithmId.EC_oid, ECParameters.getAlgorithmParameters(paramECParameterSpec));
/*     */ 
/*  61 */     this.key = ECParameters.encodePoint(paramECPoint, paramECParameterSpec.getCurve());
/*     */   }
/*     */ 
/*     */   public ECPublicKeyImpl(byte[] paramArrayOfByte)
/*     */     throws InvalidKeyException
/*     */   {
/*  68 */     decode(paramArrayOfByte);
/*     */   }
/*     */ 
/*     */   public String getAlgorithm()
/*     */   {
/*  73 */     return "EC";
/*     */   }
/*     */ 
/*     */   public ECPoint getW()
/*     */   {
/*  78 */     return this.w;
/*     */   }
/*     */ 
/*     */   public ECParameterSpec getParams()
/*     */   {
/*  83 */     return this.params;
/*     */   }
/*     */ 
/*     */   public byte[] getEncodedPublicValue()
/*     */   {
/*  89 */     return (byte[])this.key.clone();
/*     */   }
/*     */ 
/*     */   protected void parseKeyBits()
/*     */     throws InvalidKeyException
/*     */   {
/*     */     try
/*     */     {
/*  97 */       AlgorithmParameters localAlgorithmParameters = this.algid.getParameters();
/*  98 */       this.params = ((ECParameterSpec)localAlgorithmParameters.getParameterSpec(ECParameterSpec.class));
/*  99 */       this.w = ECParameters.decodePoint(this.key, this.params.getCurve());
/*     */     } catch (IOException localIOException) {
/* 101 */       throw new InvalidKeyException("Invalid EC key", localIOException);
/*     */     } catch (InvalidParameterSpecException localInvalidParameterSpecException) {
/* 103 */       throw new InvalidKeyException("Invalid EC key", localInvalidParameterSpecException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 109 */     return "Sun EC public key, " + this.params.getCurve().getField().getFieldSize() + " bits\n  public x coord: " + this.w.getAffineX() + "\n  public y coord: " + this.w.getAffineY() + "\n  parameters: " + this.params;
/*     */   }
/*     */ 
/*     */   protected Object writeReplace()
/*     */     throws ObjectStreamException
/*     */   {
/* 116 */     return new KeyRep(KeyRep.Type.PUBLIC, getAlgorithm(), getFormat(), getEncoded());
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.ec.ECPublicKeyImpl
 * JD-Core Version:    0.6.2
 */