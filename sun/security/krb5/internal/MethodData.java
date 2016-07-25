/*     */ package sun.security.krb5.internal;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.math.BigInteger;
/*     */ import sun.security.krb5.Asn1Exception;
/*     */ import sun.security.util.DerInputStream;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.DerValue;
/*     */ 
/*     */ public class MethodData
/*     */ {
/*     */   private int methodType;
/*  50 */   private byte[] methodData = null;
/*     */ 
/*     */   public MethodData(int paramInt, byte[] paramArrayOfByte) {
/*  53 */     this.methodType = paramInt;
/*  54 */     if (paramArrayOfByte != null)
/*  55 */       this.methodData = ((byte[])paramArrayOfByte.clone());
/*     */   }
/*     */ 
/*     */   public MethodData(DerValue paramDerValue)
/*     */     throws Asn1Exception, IOException
/*     */   {
/*  67 */     if (paramDerValue.getTag() != 48) {
/*  68 */       throw new Asn1Exception(906);
/*     */     }
/*  70 */     DerValue localDerValue = paramDerValue.getData().getDerValue();
/*  71 */     if ((localDerValue.getTag() & 0x1F) == 0) {
/*  72 */       BigInteger localBigInteger = localDerValue.getData().getBigInteger();
/*  73 */       this.methodType = localBigInteger.intValue();
/*     */     }
/*     */     else {
/*  76 */       throw new Asn1Exception(906);
/*  77 */     }if (paramDerValue.getData().available() > 0) {
/*  78 */       localDerValue = paramDerValue.getData().getDerValue();
/*  79 */       if ((localDerValue.getTag() & 0x1F) == 1)
/*  80 */         this.methodData = localDerValue.getData().getOctetString();
/*     */       else
/*  82 */         throw new Asn1Exception(906);
/*     */     }
/*  84 */     if (paramDerValue.getData().available() > 0)
/*  85 */       throw new Asn1Exception(906);
/*     */   }
/*     */ 
/*     */   public byte[] asn1Encode()
/*     */     throws Asn1Exception, IOException
/*     */   {
/*  96 */     DerOutputStream localDerOutputStream1 = new DerOutputStream();
/*  97 */     DerOutputStream localDerOutputStream2 = new DerOutputStream();
/*  98 */     localDerOutputStream2.putInteger(BigInteger.valueOf(this.methodType));
/*  99 */     localDerOutputStream1.write(DerValue.createTag((byte)-128, true, (byte)0), localDerOutputStream2);
/* 100 */     if (this.methodData != null) {
/* 101 */       localDerOutputStream2 = new DerOutputStream();
/* 102 */       localDerOutputStream2.putOctetString(this.methodData);
/* 103 */       localDerOutputStream1.write(DerValue.createTag((byte)-128, true, (byte)1), localDerOutputStream2);
/*     */     }
/*     */ 
/* 106 */     localDerOutputStream2 = new DerOutputStream();
/* 107 */     localDerOutputStream2.write((byte)48, localDerOutputStream1);
/* 108 */     return localDerOutputStream2.toByteArray();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.internal.MethodData
 * JD-Core Version:    0.6.2
 */