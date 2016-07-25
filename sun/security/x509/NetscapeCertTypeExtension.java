/*     */ package sun.security.x509;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Vector;
/*     */ import sun.security.util.BitArray;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.DerValue;
/*     */ import sun.security.util.ObjectIdentifier;
/*     */ 
/*     */ public class NetscapeCertTypeExtension extends Extension
/*     */   implements CertAttrSet<String>
/*     */ {
/*     */   public static final String IDENT = "x509.info.extensions.NetscapeCertType";
/*     */   public static final String NAME = "NetscapeCertType";
/*     */   public static final String SSL_CLIENT = "ssl_client";
/*     */   public static final String SSL_SERVER = "ssl_server";
/*     */   public static final String S_MIME = "s_mime";
/*     */   public static final String OBJECT_SIGNING = "object_signing";
/*     */   public static final String SSL_CA = "ssl_ca";
/*     */   public static final String S_MIME_CA = "s_mime_ca";
/*     */   public static final String OBJECT_SIGNING_CA = "object_signing_ca";
/*  72 */   private static final int[] CertType_data = { 2, 16, 840, 1, 113730, 1, 1 };
/*     */   public static ObjectIdentifier NetscapeCertType_Id;
/*     */   private boolean[] bitString;
/*     */   private static MapEntry[] mMapData;
/*     */   private static final Vector<String> mAttributeNames;
/*     */ 
/*     */   private static int getPosition(String paramString)
/*     */     throws IOException
/*     */   {
/* 118 */     for (int i = 0; i < mMapData.length; i++) {
/* 119 */       if (paramString.equalsIgnoreCase(mMapData[i].mName))
/* 120 */         return mMapData[i].mPosition;
/*     */     }
/* 122 */     throw new IOException("Attribute name [" + paramString + "] not recognized by CertAttrSet:NetscapeCertType.");
/*     */   }
/*     */ 
/*     */   private void encodeThis()
/*     */     throws IOException
/*     */   {
/* 128 */     DerOutputStream localDerOutputStream = new DerOutputStream();
/* 129 */     localDerOutputStream.putTruncatedUnalignedBitString(new BitArray(this.bitString));
/* 130 */     this.extensionValue = localDerOutputStream.toByteArray();
/*     */   }
/*     */ 
/*     */   private boolean isSet(int paramInt)
/*     */   {
/* 139 */     return (paramInt < this.bitString.length) && (this.bitString[paramInt] != 0);
/*     */   }
/*     */ 
/*     */   private void set(int paramInt, boolean paramBoolean)
/*     */   {
/* 148 */     if (paramInt >= this.bitString.length) {
/* 149 */       boolean[] arrayOfBoolean = new boolean[paramInt + 1];
/* 150 */       System.arraycopy(this.bitString, 0, arrayOfBoolean, 0, this.bitString.length);
/* 151 */       this.bitString = arrayOfBoolean;
/*     */     }
/* 153 */     this.bitString[paramInt] = paramBoolean;
/*     */   }
/*     */ 
/*     */   public NetscapeCertTypeExtension(byte[] paramArrayOfByte)
/*     */     throws IOException
/*     */   {
/* 163 */     this.bitString = new BitArray(paramArrayOfByte.length * 8, paramArrayOfByte).toBooleanArray();
/*     */ 
/* 165 */     this.extensionId = NetscapeCertType_Id;
/* 166 */     this.critical = true;
/* 167 */     encodeThis();
/*     */   }
/*     */ 
/*     */   public NetscapeCertTypeExtension(boolean[] paramArrayOfBoolean)
/*     */     throws IOException
/*     */   {
/* 177 */     this.bitString = paramArrayOfBoolean;
/* 178 */     this.extensionId = NetscapeCertType_Id;
/* 179 */     this.critical = true;
/* 180 */     encodeThis();
/*     */   }
/*     */ 
/*     */   public NetscapeCertTypeExtension(Boolean paramBoolean, Object paramObject)
/*     */     throws IOException
/*     */   {
/* 193 */     this.extensionId = NetscapeCertType_Id;
/* 194 */     this.critical = paramBoolean.booleanValue();
/* 195 */     this.extensionValue = ((byte[])paramObject);
/* 196 */     DerValue localDerValue = new DerValue(this.extensionValue);
/* 197 */     this.bitString = localDerValue.getUnalignedBitString().toBooleanArray();
/*     */   }
/*     */ 
/*     */   public NetscapeCertTypeExtension()
/*     */   {
/* 204 */     this.extensionId = NetscapeCertType_Id;
/* 205 */     this.critical = true;
/* 206 */     this.bitString = new boolean[0];
/*     */   }
/*     */ 
/*     */   public void set(String paramString, Object paramObject)
/*     */     throws IOException
/*     */   {
/* 213 */     if (!(paramObject instanceof Boolean)) {
/* 214 */       throw new IOException("Attribute must be of type Boolean.");
/*     */     }
/* 216 */     boolean bool = ((Boolean)paramObject).booleanValue();
/* 217 */     set(getPosition(paramString), bool);
/* 218 */     encodeThis();
/*     */   }
/*     */ 
/*     */   public Object get(String paramString)
/*     */     throws IOException
/*     */   {
/* 225 */     return Boolean.valueOf(isSet(getPosition(paramString)));
/*     */   }
/*     */ 
/*     */   public void delete(String paramString)
/*     */     throws IOException
/*     */   {
/* 232 */     set(getPosition(paramString), false);
/* 233 */     encodeThis();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 240 */     StringBuilder localStringBuilder = new StringBuilder();
/* 241 */     localStringBuilder.append(super.toString());
/* 242 */     localStringBuilder.append("NetscapeCertType [\n");
/*     */ 
/* 244 */     if (isSet(0)) {
/* 245 */       localStringBuilder.append("   SSL client\n");
/*     */     }
/* 247 */     if (isSet(1)) {
/* 248 */       localStringBuilder.append("   SSL server\n");
/*     */     }
/* 250 */     if (isSet(2)) {
/* 251 */       localStringBuilder.append("   S/MIME\n");
/*     */     }
/* 253 */     if (isSet(3)) {
/* 254 */       localStringBuilder.append("   Object Signing\n");
/*     */     }
/* 256 */     if (isSet(5)) {
/* 257 */       localStringBuilder.append("   SSL CA\n");
/*     */     }
/* 259 */     if (isSet(6)) {
/* 260 */       localStringBuilder.append("   S/MIME CA\n");
/*     */     }
/* 262 */     if (isSet(7)) {
/* 263 */       localStringBuilder.append("   Object Signing CA");
/*     */     }
/*     */ 
/* 266 */     localStringBuilder.append("]\n");
/* 267 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   public void encode(OutputStream paramOutputStream)
/*     */     throws IOException
/*     */   {
/* 277 */     DerOutputStream localDerOutputStream = new DerOutputStream();
/*     */ 
/* 279 */     if (this.extensionValue == null) {
/* 280 */       this.extensionId = NetscapeCertType_Id;
/* 281 */       this.critical = true;
/* 282 */       encodeThis();
/*     */     }
/* 284 */     super.encode(localDerOutputStream);
/* 285 */     paramOutputStream.write(localDerOutputStream.toByteArray());
/*     */   }
/*     */ 
/*     */   public Enumeration<String> getElements()
/*     */   {
/* 293 */     return mAttributeNames.elements();
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 300 */     return "NetscapeCertType";
/*     */   }
/*     */ 
/*     */   public boolean[] getKeyUsageMappedBits()
/*     */   {
/* 310 */     KeyUsageExtension localKeyUsageExtension = new KeyUsageExtension();
/* 311 */     Boolean localBoolean = Boolean.TRUE;
/*     */     try
/*     */     {
/* 314 */       if ((isSet(getPosition("ssl_client"))) || (isSet(getPosition("s_mime"))) || (isSet(getPosition("object_signing"))))
/*     */       {
/* 317 */         localKeyUsageExtension.set("digital_signature", localBoolean);
/*     */       }
/* 319 */       if (isSet(getPosition("ssl_server"))) {
/* 320 */         localKeyUsageExtension.set("key_encipherment", localBoolean);
/*     */       }
/* 322 */       if ((isSet(getPosition("ssl_ca"))) || (isSet(getPosition("s_mime_ca"))) || (isSet(getPosition("object_signing_ca"))))
/*     */       {
/* 325 */         localKeyUsageExtension.set("key_certsign", localBoolean);
/*     */       } } catch (IOException localIOException) {  }
/*     */ 
/* 327 */     return localKeyUsageExtension.getBits();
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*     */     try
/*     */     {
/*  81 */       NetscapeCertType_Id = new ObjectIdentifier(CertType_data);
/*     */     }
/*     */     catch (IOException localIOException)
/*     */     {
/*     */     }
/*     */ 
/*  99 */     mMapData = new MapEntry[] { new MapEntry("ssl_client", 0), new MapEntry("ssl_server", 1), new MapEntry("s_mime", 2), new MapEntry("object_signing", 3), new MapEntry("ssl_ca", 5), new MapEntry("s_mime_ca", 6), new MapEntry("object_signing_ca", 7) };
/*     */ 
/* 110 */     mAttributeNames = new Vector();
/*     */ 
/* 112 */     for (MapEntry localMapEntry : mMapData)
/* 113 */       mAttributeNames.add(localMapEntry.mName);
/*     */   }
/*     */ 
/*     */   private static class MapEntry
/*     */   {
/*     */     String mName;
/*     */     int mPosition;
/*     */ 
/*     */     MapEntry(String paramString, int paramInt)
/*     */     {
/*  94 */       this.mName = paramString;
/*  95 */       this.mPosition = paramInt;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.x509.NetscapeCertTypeExtension
 * JD-Core Version:    0.6.2
 */