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
/*     */ public class ETypeInfo
/*     */ {
/*     */   private int etype;
/*  47 */   private String salt = null;
/*     */   private static final byte TAG_TYPE = 0;
/*     */   private static final byte TAG_VALUE = 1;
/*     */ 
/*     */   private ETypeInfo()
/*     */   {
/*     */   }
/*     */ 
/*     */   public ETypeInfo(int paramInt, String paramString)
/*     */   {
/*  56 */     this.etype = paramInt;
/*  57 */     this.salt = paramString;
/*     */   }
/*     */ 
/*     */   public Object clone() {
/*  61 */     return new ETypeInfo(this.etype, this.salt);
/*     */   }
/*     */ 
/*     */   public ETypeInfo(DerValue paramDerValue)
/*     */     throws Asn1Exception, IOException
/*     */   {
/*  72 */     DerValue localDerValue = null;
/*     */ 
/*  74 */     if (paramDerValue.getTag() != 48) {
/*  75 */       throw new Asn1Exception(906);
/*     */     }
/*     */ 
/*  79 */     localDerValue = paramDerValue.getData().getDerValue();
/*  80 */     if ((localDerValue.getTag() & 0x1F) == 0) {
/*  81 */       this.etype = localDerValue.getData().getBigInteger().intValue();
/*     */     }
/*     */     else {
/*  84 */       throw new Asn1Exception(906);
/*     */     }
/*     */ 
/*  87 */     if (paramDerValue.getData().available() > 0) {
/*  88 */       localDerValue = paramDerValue.getData().getDerValue();
/*  89 */       if ((localDerValue.getTag() & 0x1F) == 1) {
/*  90 */         byte[] arrayOfByte = localDerValue.getData().getOctetString();
/*     */ 
/* 101 */         if (KerberosString.MSNAME)
/* 102 */           this.salt = new String(arrayOfByte, "UTF8");
/*     */         else {
/* 104 */           this.salt = new String(arrayOfByte);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 109 */     if (paramDerValue.getData().available() > 0)
/* 110 */       throw new Asn1Exception(906);
/*     */   }
/*     */ 
/*     */   public byte[] asn1Encode()
/*     */     throws Asn1Exception, IOException
/*     */   {
/* 122 */     DerOutputStream localDerOutputStream1 = new DerOutputStream();
/* 123 */     DerOutputStream localDerOutputStream2 = new DerOutputStream();
/*     */ 
/* 125 */     localDerOutputStream2.putInteger(this.etype);
/* 126 */     localDerOutputStream1.write(DerValue.createTag((byte)-128, true, (byte)0), localDerOutputStream2);
/*     */ 
/* 129 */     if (this.salt != null) {
/* 130 */       localDerOutputStream2 = new DerOutputStream();
/* 131 */       if (KerberosString.MSNAME)
/* 132 */         localDerOutputStream2.putOctetString(this.salt.getBytes("UTF8"));
/*     */       else {
/* 134 */         localDerOutputStream2.putOctetString(this.salt.getBytes());
/*     */       }
/* 136 */       localDerOutputStream1.write(DerValue.createTag((byte)-128, true, (byte)1), localDerOutputStream2);
/*     */     }
/*     */ 
/* 140 */     localDerOutputStream2 = new DerOutputStream();
/* 141 */     localDerOutputStream2.write((byte)48, localDerOutputStream1);
/* 142 */     return localDerOutputStream2.toByteArray();
/*     */   }
/*     */ 
/*     */   public int getEType()
/*     */   {
/* 147 */     return this.etype;
/*     */   }
/*     */ 
/*     */   public String getSalt() {
/* 151 */     return this.salt;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.internal.ETypeInfo
 * JD-Core Version:    0.6.2
 */