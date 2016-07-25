/*     */ package sun.security.krb5.internal;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.math.BigInteger;
/*     */ import sun.security.krb5.Asn1Exception;
/*     */ import sun.security.util.DerInputStream;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.DerValue;
/*     */ 
/*     */ public class TransitedEncoding
/*     */ {
/*     */   public int trType;
/*     */   public byte[] contents;
/*     */ 
/*     */   public TransitedEncoding(int paramInt, byte[] paramArrayOfByte)
/*     */   {
/*  60 */     this.trType = paramInt;
/*  61 */     this.contents = paramArrayOfByte;
/*     */   }
/*     */ 
/*     */   public TransitedEncoding(DerValue paramDerValue)
/*     */     throws Asn1Exception, IOException
/*     */   {
/*  72 */     if (paramDerValue.getTag() != 48) {
/*  73 */       throw new Asn1Exception(906);
/*     */     }
/*     */ 
/*  76 */     DerValue localDerValue = paramDerValue.getData().getDerValue();
/*  77 */     if ((localDerValue.getTag() & 0x1F) == 0) {
/*  78 */       this.trType = localDerValue.getData().getBigInteger().intValue();
/*     */     }
/*     */     else
/*  81 */       throw new Asn1Exception(906);
/*  82 */     localDerValue = paramDerValue.getData().getDerValue();
/*     */ 
/*  84 */     if ((localDerValue.getTag() & 0x1F) == 1) {
/*  85 */       this.contents = localDerValue.getData().getOctetString();
/*     */     }
/*     */     else
/*  88 */       throw new Asn1Exception(906);
/*  89 */     if (localDerValue.getData().available() > 0)
/*  90 */       throw new Asn1Exception(906);
/*     */   }
/*     */ 
/*     */   public byte[] asn1Encode()
/*     */     throws Asn1Exception, IOException
/*     */   {
/* 100 */     DerOutputStream localDerOutputStream1 = new DerOutputStream();
/* 101 */     DerOutputStream localDerOutputStream2 = new DerOutputStream();
/* 102 */     localDerOutputStream2.putInteger(BigInteger.valueOf(this.trType));
/* 103 */     localDerOutputStream1.write(DerValue.createTag((byte)-128, true, (byte)0), localDerOutputStream2);
/* 104 */     localDerOutputStream2 = new DerOutputStream();
/* 105 */     localDerOutputStream2.putOctetString(this.contents);
/* 106 */     localDerOutputStream1.write(DerValue.createTag((byte)-128, true, (byte)1), localDerOutputStream2);
/* 107 */     localDerOutputStream2 = new DerOutputStream();
/* 108 */     localDerOutputStream2.write((byte)48, localDerOutputStream1);
/* 109 */     return localDerOutputStream2.toByteArray();
/*     */   }
/*     */ 
/*     */   public static TransitedEncoding parse(DerInputStream paramDerInputStream, byte paramByte, boolean paramBoolean)
/*     */     throws Asn1Exception, IOException
/*     */   {
/* 125 */     if ((paramBoolean) && (((byte)paramDerInputStream.peekByte() & 0x1F) != paramByte))
/* 126 */       return null;
/* 127 */     DerValue localDerValue1 = paramDerInputStream.getDerValue();
/* 128 */     if (paramByte != (localDerValue1.getTag() & 0x1F)) {
/* 129 */       throw new Asn1Exception(906);
/*     */     }
/*     */ 
/* 132 */     DerValue localDerValue2 = localDerValue1.getData().getDerValue();
/* 133 */     return new TransitedEncoding(localDerValue2);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.internal.TransitedEncoding
 * JD-Core Version:    0.6.2
 */