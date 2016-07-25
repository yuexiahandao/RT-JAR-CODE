/*     */ package sun.security.x509;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.util.Arrays;
/*     */ import sun.security.util.DerInputStream;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.DerValue;
/*     */ import sun.security.util.ObjectIdentifier;
/*     */ 
/*     */ public class Extension
/*     */   implements java.security.cert.Extension
/*     */ {
/*  63 */   protected ObjectIdentifier extensionId = null;
/*  64 */   protected boolean critical = false;
/*  65 */   protected byte[] extensionValue = null;
/*     */   private static final int hashMagic = 31;
/*     */ 
/*     */   public Extension()
/*     */   {
/*     */   }
/*     */ 
/*     */   public Extension(DerValue paramDerValue)
/*     */     throws IOException
/*     */   {
/*  77 */     DerInputStream localDerInputStream = paramDerValue.toDerInputStream();
/*     */ 
/*  80 */     this.extensionId = localDerInputStream.getOID();
/*     */ 
/*  83 */     DerValue localDerValue = localDerInputStream.getDerValue();
/*  84 */     if (localDerValue.tag == 1) {
/*  85 */       this.critical = localDerValue.getBoolean();
/*     */ 
/*  88 */       localDerValue = localDerInputStream.getDerValue();
/*  89 */       this.extensionValue = localDerValue.getOctetString();
/*     */     } else {
/*  91 */       this.critical = false;
/*  92 */       this.extensionValue = localDerValue.getOctetString();
/*     */     }
/*     */   }
/*     */ 
/*     */   public Extension(ObjectIdentifier paramObjectIdentifier, boolean paramBoolean, byte[] paramArrayOfByte)
/*     */     throws IOException
/*     */   {
/* 106 */     this.extensionId = paramObjectIdentifier;
/* 107 */     this.critical = paramBoolean;
/*     */ 
/* 110 */     DerValue localDerValue = new DerValue(paramArrayOfByte);
/* 111 */     this.extensionValue = localDerValue.getOctetString();
/*     */   }
/*     */ 
/*     */   public Extension(Extension paramExtension)
/*     */   {
/* 121 */     this.extensionId = paramExtension.extensionId;
/* 122 */     this.critical = paramExtension.critical;
/* 123 */     this.extensionValue = paramExtension.extensionValue;
/*     */   }
/*     */ 
/*     */   public static Extension newExtension(ObjectIdentifier paramObjectIdentifier, boolean paramBoolean, byte[] paramArrayOfByte)
/*     */     throws IOException
/*     */   {
/* 137 */     Extension localExtension = new Extension();
/* 138 */     localExtension.extensionId = paramObjectIdentifier;
/* 139 */     localExtension.critical = paramBoolean;
/* 140 */     localExtension.extensionValue = paramArrayOfByte;
/* 141 */     return localExtension;
/*     */   }
/*     */ 
/*     */   public void encode(OutputStream paramOutputStream) throws IOException {
/* 145 */     if (paramOutputStream == null) {
/* 146 */       throw new NullPointerException();
/*     */     }
/*     */ 
/* 149 */     DerOutputStream localDerOutputStream1 = new DerOutputStream();
/* 150 */     DerOutputStream localDerOutputStream2 = new DerOutputStream();
/*     */ 
/* 152 */     localDerOutputStream1.putOID(this.extensionId);
/* 153 */     if (this.critical) {
/* 154 */       localDerOutputStream1.putBoolean(this.critical);
/*     */     }
/* 156 */     localDerOutputStream1.putOctetString(this.extensionValue);
/*     */ 
/* 158 */     localDerOutputStream2.write((byte)48, localDerOutputStream1);
/* 159 */     paramOutputStream.write(localDerOutputStream2.toByteArray());
/*     */   }
/*     */ 
/*     */   public void encode(DerOutputStream paramDerOutputStream)
/*     */     throws IOException
/*     */   {
/* 170 */     if (this.extensionId == null)
/* 171 */       throw new IOException("Null OID to encode for the extension!");
/* 172 */     if (this.extensionValue == null) {
/* 173 */       throw new IOException("No value to encode for the extension!");
/*     */     }
/* 175 */     DerOutputStream localDerOutputStream = new DerOutputStream();
/*     */ 
/* 177 */     localDerOutputStream.putOID(this.extensionId);
/* 178 */     if (this.critical)
/* 179 */       localDerOutputStream.putBoolean(this.critical);
/* 180 */     localDerOutputStream.putOctetString(this.extensionValue);
/*     */ 
/* 182 */     paramDerOutputStream.write((byte)48, localDerOutputStream);
/*     */   }
/*     */ 
/*     */   public boolean isCritical()
/*     */   {
/* 189 */     return this.critical;
/*     */   }
/*     */ 
/*     */   public ObjectIdentifier getExtensionId()
/*     */   {
/* 196 */     return this.extensionId;
/*     */   }
/*     */ 
/*     */   public byte[] getValue() {
/* 200 */     return (byte[])this.extensionValue.clone();
/*     */   }
/*     */ 
/*     */   public byte[] getExtensionValue()
/*     */   {
/* 211 */     return this.extensionValue;
/*     */   }
/*     */ 
/*     */   public String getId() {
/* 215 */     return this.extensionId.toString();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 222 */     String str = "ObjectId: " + this.extensionId.toString();
/* 223 */     if (this.critical)
/* 224 */       str = str + " Criticality=true\n";
/*     */     else {
/* 226 */       str = str + " Criticality=false\n";
/*     */     }
/* 228 */     return str;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 240 */     int i = 0;
/* 241 */     if (this.extensionValue != null) {
/* 242 */       byte[] arrayOfByte = this.extensionValue;
/* 243 */       int j = arrayOfByte.length;
/* 244 */       while (j > 0)
/* 245 */         i += j * arrayOfByte[(--j)];
/*     */     }
/* 247 */     i = i * 31 + this.extensionId.hashCode();
/* 248 */     i = i * 31 + (this.critical ? 1231 : 1237);
/* 249 */     return i;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 265 */     if (this == paramObject)
/* 266 */       return true;
/* 267 */     if (!(paramObject instanceof Extension))
/* 268 */       return false;
/* 269 */     Extension localExtension = (Extension)paramObject;
/* 270 */     if (this.critical != localExtension.critical)
/* 271 */       return false;
/* 272 */     if (!this.extensionId.equals(localExtension.extensionId))
/* 273 */       return false;
/* 274 */     return Arrays.equals(this.extensionValue, localExtension.extensionValue);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.x509.Extension
 * JD-Core Version:    0.6.2
 */