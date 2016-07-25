/*     */ package sun.security.krb5.internal;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.math.BigInteger;
/*     */ import sun.security.krb5.Asn1Exception;
/*     */ import sun.security.krb5.internal.util.KerberosString;
/*     */ import sun.security.util.DerInputStream;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.DerValue;
/*     */ 
/*     */ public class ETypeInfo2
/*     */ {
/*     */   private int etype;
/*  48 */   private String saltStr = null;
/*  49 */   private byte[] s2kparams = null;
/*     */   private static final byte TAG_TYPE = 0;
/*     */   private static final byte TAG_VALUE1 = 1;
/*     */   private static final byte TAG_VALUE2 = 2;
/*     */ 
/*     */   private ETypeInfo2()
/*     */   {
/*     */   }
/*     */ 
/*     */   public ETypeInfo2(int paramInt, String paramString, byte[] paramArrayOfByte)
/*     */   {
/*  59 */     this.etype = paramInt;
/*  60 */     this.saltStr = paramString;
/*  61 */     if (paramArrayOfByte != null)
/*  62 */       this.s2kparams = ((byte[])paramArrayOfByte.clone());
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/*  67 */     ETypeInfo2 localETypeInfo2 = new ETypeInfo2();
/*  68 */     localETypeInfo2.etype = this.etype;
/*  69 */     localETypeInfo2.saltStr = this.saltStr;
/*  70 */     if (this.s2kparams != null) {
/*  71 */       localETypeInfo2.s2kparams = new byte[this.s2kparams.length];
/*  72 */       System.arraycopy(this.s2kparams, 0, localETypeInfo2.s2kparams, 0, this.s2kparams.length);
/*     */     }
/*     */ 
/*  75 */     return localETypeInfo2;
/*     */   }
/*     */ 
/*     */   public ETypeInfo2(DerValue paramDerValue)
/*     */     throws Asn1Exception, IOException
/*     */   {
/*  86 */     DerValue localDerValue = null;
/*     */ 
/*  88 */     if (paramDerValue.getTag() != 48) {
/*  89 */       throw new Asn1Exception(906);
/*     */     }
/*     */ 
/*  93 */     localDerValue = paramDerValue.getData().getDerValue();
/*  94 */     if ((localDerValue.getTag() & 0x1F) == 0) {
/*  95 */       this.etype = localDerValue.getData().getBigInteger().intValue();
/*     */     }
/*     */     else {
/*  98 */       throw new Asn1Exception(906);
/*     */     }
/*     */ 
/* 101 */     if ((paramDerValue.getData().available() > 0) && 
/* 102 */       ((paramDerValue.getData().peekByte() & 0x1F) == 1)) {
/* 103 */       localDerValue = paramDerValue.getData().getDerValue();
/* 104 */       this.saltStr = new KerberosString(localDerValue.getData().getDerValue()).toString();
/*     */     }
/*     */ 
/* 110 */     if ((paramDerValue.getData().available() > 0) && 
/* 111 */       ((paramDerValue.getData().peekByte() & 0x1F) == 2)) {
/* 112 */       localDerValue = paramDerValue.getData().getDerValue();
/* 113 */       this.s2kparams = localDerValue.getData().getOctetString();
/*     */     }
/*     */ 
/* 117 */     if (paramDerValue.getData().available() > 0)
/* 118 */       throw new Asn1Exception(906);
/*     */   }
/*     */ 
/*     */   public byte[] asn1Encode()
/*     */     throws Asn1Exception, IOException
/*     */   {
/* 130 */     DerOutputStream localDerOutputStream1 = new DerOutputStream();
/* 131 */     DerOutputStream localDerOutputStream2 = new DerOutputStream();
/*     */ 
/* 133 */     localDerOutputStream2.putInteger(this.etype);
/* 134 */     localDerOutputStream1.write(DerValue.createTag((byte)-128, true, (byte)0), localDerOutputStream2);
/*     */ 
/* 137 */     if (this.saltStr != null) {
/* 138 */       localDerOutputStream2 = new DerOutputStream();
/* 139 */       localDerOutputStream2.putDerValue(new KerberosString(this.saltStr).toDerValue());
/* 140 */       localDerOutputStream1.write(DerValue.createTag((byte)-128, true, (byte)1), localDerOutputStream2);
/*     */     }
/*     */ 
/* 143 */     if (this.s2kparams != null) {
/* 144 */       localDerOutputStream2 = new DerOutputStream();
/* 145 */       localDerOutputStream2.putOctetString(this.s2kparams);
/* 146 */       localDerOutputStream1.write(DerValue.createTag((byte)-128, true, (byte)2), localDerOutputStream2);
/*     */     }
/*     */ 
/* 150 */     localDerOutputStream2 = new DerOutputStream();
/* 151 */     localDerOutputStream2.write((byte)48, localDerOutputStream1);
/* 152 */     return localDerOutputStream2.toByteArray();
/*     */   }
/*     */ 
/*     */   public int getEType()
/*     */   {
/* 157 */     return this.etype;
/*     */   }
/*     */ 
/*     */   public String getSalt() {
/* 161 */     return this.saltStr;
/*     */   }
/*     */ 
/*     */   public byte[] getParams() {
/* 165 */     return this.s2kparams == null ? null : (byte[])this.s2kparams.clone();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.internal.ETypeInfo2
 * JD-Core Version:    0.6.2
 */