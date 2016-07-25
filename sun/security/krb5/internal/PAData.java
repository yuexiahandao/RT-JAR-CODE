/*     */ package sun.security.krb5.internal;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.math.BigInteger;
/*     */ import sun.misc.HexDumpEncoder;
/*     */ import sun.security.krb5.Asn1Exception;
/*     */ import sun.security.krb5.internal.util.KerberosString;
/*     */ import sun.security.util.DerInputStream;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.DerValue;
/*     */ 
/*     */ public class PAData
/*     */ {
/*     */   private int pADataType;
/*  59 */   private byte[] pADataValue = null;
/*     */   private static final byte TAG_PATYPE = 1;
/*     */   private static final byte TAG_PAVALUE = 2;
/*     */ 
/*     */   private PAData()
/*     */   {
/*     */   }
/*     */ 
/*     */   public PAData(int paramInt, byte[] paramArrayOfByte)
/*     */   {
/*  67 */     this.pADataType = paramInt;
/*  68 */     if (paramArrayOfByte != null)
/*  69 */       this.pADataValue = ((byte[])paramArrayOfByte.clone());
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/*  74 */     PAData localPAData = new PAData();
/*  75 */     localPAData.pADataType = this.pADataType;
/*  76 */     if (this.pADataValue != null) {
/*  77 */       localPAData.pADataValue = new byte[this.pADataValue.length];
/*  78 */       System.arraycopy(this.pADataValue, 0, localPAData.pADataValue, 0, this.pADataValue.length);
/*     */     }
/*     */ 
/*  81 */     return localPAData;
/*     */   }
/*     */ 
/*     */   public PAData(DerValue paramDerValue)
/*     */     throws Asn1Exception, IOException
/*     */   {
/*  91 */     DerValue localDerValue = null;
/*  92 */     if (paramDerValue.getTag() != 48) {
/*  93 */       throw new Asn1Exception(906);
/*     */     }
/*  95 */     localDerValue = paramDerValue.getData().getDerValue();
/*  96 */     if ((localDerValue.getTag() & 0x1F) == 1) {
/*  97 */       this.pADataType = localDerValue.getData().getBigInteger().intValue();
/*     */     }
/*     */     else
/* 100 */       throw new Asn1Exception(906);
/* 101 */     localDerValue = paramDerValue.getData().getDerValue();
/* 102 */     if ((localDerValue.getTag() & 0x1F) == 2) {
/* 103 */       this.pADataValue = localDerValue.getData().getOctetString();
/*     */     }
/* 105 */     if (paramDerValue.getData().available() > 0)
/* 106 */       throw new Asn1Exception(906);
/*     */   }
/*     */ 
/*     */   public byte[] asn1Encode()
/*     */     throws Asn1Exception, IOException
/*     */   {
/* 118 */     DerOutputStream localDerOutputStream1 = new DerOutputStream();
/* 119 */     DerOutputStream localDerOutputStream2 = new DerOutputStream();
/*     */ 
/* 121 */     localDerOutputStream2.putInteger(this.pADataType);
/* 122 */     localDerOutputStream1.write(DerValue.createTag((byte)-128, true, (byte)1), localDerOutputStream2);
/* 123 */     localDerOutputStream2 = new DerOutputStream();
/* 124 */     localDerOutputStream2.putOctetString(this.pADataValue);
/* 125 */     localDerOutputStream1.write(DerValue.createTag((byte)-128, true, (byte)2), localDerOutputStream2);
/*     */ 
/* 127 */     localDerOutputStream2 = new DerOutputStream();
/* 128 */     localDerOutputStream2.write((byte)48, localDerOutputStream1);
/* 129 */     return localDerOutputStream2.toByteArray();
/*     */   }
/*     */ 
/*     */   public int getType()
/*     */   {
/* 134 */     return this.pADataType;
/*     */   }
/*     */ 
/*     */   public byte[] getValue() {
/* 138 */     return this.pADataValue == null ? null : (byte[])this.pADataValue.clone();
/*     */   }
/*     */ 
/*     */   public static int getPreferredEType(PAData[] paramArrayOfPAData, int paramInt)
/*     */     throws IOException, Asn1Exception
/*     */   {
/* 154 */     if (paramArrayOfPAData == null) return paramInt;
/*     */ 
/* 156 */     DerValue localDerValue1 = null; DerValue localDerValue2 = null;
/* 157 */     for (Object localObject3 : paramArrayOfPAData)
/* 158 */       if (localObject3.getValue() != null)
/* 159 */         switch (localObject3.getType()) {
/*     */         case 11:
/* 161 */           localDerValue1 = new DerValue(localObject3.getValue());
/* 162 */           break;
/*     */         case 19:
/* 164 */           localDerValue2 = new DerValue(localObject3.getValue());
/*     */         }
/*     */     Object localObject2;
/* 168 */     if (localDerValue2 != null) {
/* 169 */       while (localDerValue2.data.available() > 0) {
/* 170 */         ??? = localDerValue2.data.getDerValue();
/* 171 */         localObject2 = new ETypeInfo2((DerValue)???);
/* 172 */         if (((ETypeInfo2)localObject2).getParams() == null)
/*     */         {
/* 174 */           return ((ETypeInfo2)localObject2).getEType();
/*     */         }
/*     */       }
/*     */     }
/* 178 */     if ((localDerValue1 != null) && 
/* 179 */       (localDerValue1.data.available() > 0)) {
/* 180 */       ??? = localDerValue1.data.getDerValue();
/* 181 */       localObject2 = new ETypeInfo((DerValue)???);
/* 182 */       return ((ETypeInfo)localObject2).getEType();
/*     */     }
/*     */ 
/* 185 */     return paramInt;
/*     */   }
/*     */ 
/*     */   public static SaltAndParams getSaltAndParams(int paramInt, PAData[] paramArrayOfPAData)
/*     */     throws Asn1Exception, IOException
/*     */   {
/* 215 */     if (paramArrayOfPAData == null) return null;
/*     */ 
/* 217 */     DerValue localDerValue1 = null; DerValue localDerValue2 = null;
/* 218 */     String str = null;
/*     */ 
/* 220 */     for (Object localObject3 : paramArrayOfPAData)
/* 221 */       if (localObject3.getValue() != null)
/* 222 */         switch (localObject3.getType()) {
/*     */         case 3:
/* 224 */           str = new String(localObject3.getValue(), KerberosString.MSNAME ? "UTF8" : "8859_1");
/*     */ 
/* 226 */           break;
/*     */         case 11:
/* 228 */           localDerValue1 = new DerValue(localObject3.getValue());
/* 229 */           break;
/*     */         case 19:
/* 231 */           localDerValue2 = new DerValue(localObject3.getValue());
/*     */         }
/*     */     Object localObject2;
/* 235 */     if (localDerValue2 != null) {
/* 236 */       while (localDerValue2.data.available() > 0) {
/* 237 */         ??? = localDerValue2.data.getDerValue();
/* 238 */         localObject2 = new ETypeInfo2((DerValue)???);
/* 239 */         if ((((ETypeInfo2)localObject2).getParams() == null) && (((ETypeInfo2)localObject2).getEType() == paramInt))
/*     */         {
/* 241 */           return new SaltAndParams(((ETypeInfo2)localObject2).getSalt(), ((ETypeInfo2)localObject2).getParams());
/*     */         }
/*     */       }
/*     */     }
/* 245 */     if (localDerValue1 != null) {
/* 246 */       while (localDerValue1.data.available() > 0) {
/* 247 */         ??? = localDerValue1.data.getDerValue();
/* 248 */         localObject2 = new ETypeInfo((DerValue)???);
/* 249 */         if (((ETypeInfo)localObject2).getEType() == paramInt) {
/* 250 */           return new SaltAndParams(((ETypeInfo)localObject2).getSalt(), null);
/*     */         }
/*     */       }
/*     */     }
/* 254 */     if (str != null) {
/* 255 */       return new SaltAndParams(str, null);
/*     */     }
/* 257 */     return null;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 262 */     StringBuilder localStringBuilder = new StringBuilder();
/* 263 */     localStringBuilder.append(">>>Pre-Authentication Data:\n\t PA-DATA type = ").append(this.pADataType).append('\n');
/*     */     DerValue localDerValue3;
/*     */     Object localObject;
/* 266 */     switch (this.pADataType) {
/*     */     case 2:
/* 268 */       localStringBuilder.append("\t PA-ENC-TIMESTAMP");
/* 269 */       break;
/*     */     case 11:
/* 271 */       if (this.pADataValue != null) {
/*     */         try {
/* 273 */           DerValue localDerValue1 = new DerValue(this.pADataValue);
/* 274 */           while (localDerValue1.data.available() > 0) {
/* 275 */             localDerValue3 = localDerValue1.data.getDerValue();
/* 276 */             localObject = new ETypeInfo(localDerValue3);
/* 277 */             localStringBuilder.append("\t PA-ETYPE-INFO etype = ").append(((ETypeInfo)localObject).getEType()).append(", salt = ").append(((ETypeInfo)localObject).getSalt()).append('\n');
/*     */           }
/*     */ 
/*     */         }
/*     */         catch (IOException|Asn1Exception localIOException1)
/*     */         {
/* 284 */           localStringBuilder.append("\t <Unparseable PA-ETYPE-INFO>\n");
/*     */         }
/*     */       }
/*     */       break;
/*     */     case 19:
/* 289 */       if (this.pADataValue != null) {
/*     */         try {
/* 291 */           DerValue localDerValue2 = new DerValue(this.pADataValue);
/* 292 */           while (localDerValue2.data.available() > 0) {
/* 293 */             localDerValue3 = localDerValue2.data.getDerValue();
/* 294 */             localObject = new ETypeInfo2(localDerValue3);
/* 295 */             localStringBuilder.append("\t PA-ETYPE-INFO2 etype = ").append(((ETypeInfo2)localObject).getEType()).append(", salt = ").append(((ETypeInfo2)localObject).getSalt()).append(", s2kparams = ");
/*     */ 
/* 300 */             byte[] arrayOfByte = ((ETypeInfo2)localObject).getParams();
/* 301 */             if (arrayOfByte == null)
/* 302 */               localStringBuilder.append("null\n");
/* 303 */             else if (arrayOfByte.length == 0)
/* 304 */               localStringBuilder.append("empty\n");
/*     */             else
/* 306 */               localStringBuilder.append(new HexDumpEncoder().encodeBuffer(arrayOfByte));
/*     */           }
/*     */         }
/*     */         catch (IOException|Asn1Exception localIOException2)
/*     */         {
/* 311 */           localStringBuilder.append("\t <Unparseable PA-ETYPE-INFO>\n");
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */       break;
/*     */     }
/*     */ 
/* 319 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   public static class SaltAndParams
/*     */   {
/*     */     public final String salt;
/*     */     public final byte[] params;
/*     */ 
/*     */     public SaltAndParams(String paramString, byte[] paramArrayOfByte)
/*     */     {
/* 197 */       if ((paramString != null) && (paramString.isEmpty())) paramString = null;
/* 198 */       this.salt = paramString;
/* 199 */       this.params = paramArrayOfByte;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.internal.PAData
 * JD-Core Version:    0.6.2
 */