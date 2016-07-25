/*     */ package sun.security.x509;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.util.Enumeration;
/*     */ import sun.security.util.DerInputStream;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.DerValue;
/*     */ 
/*     */ public class AuthorityKeyIdentifierExtension extends Extension
/*     */   implements CertAttrSet<String>
/*     */ {
/*     */   public static final String IDENT = "x509.info.extensions.AuthorityKeyIdentifier";
/*     */   public static final String NAME = "AuthorityKeyIdentifier";
/*     */   public static final String KEY_ID = "key_id";
/*     */   public static final String AUTH_NAME = "auth_name";
/*     */   public static final String SERIAL_NUMBER = "serial_number";
/*     */   private static final byte TAG_ID = 0;
/*     */   private static final byte TAG_NAMES = 1;
/*     */   private static final byte TAG_SERIAL_NUM = 2;
/*  78 */   private KeyIdentifier id = null;
/*  79 */   private GeneralNames names = null;
/*  80 */   private SerialNumber serialNum = null;
/*     */ 
/*     */   private void encodeThis() throws IOException
/*     */   {
/*  84 */     if ((this.id == null) && (this.names == null) && (this.serialNum == null)) {
/*  85 */       this.extensionValue = null;
/*  86 */       return;
/*     */     }
/*  88 */     DerOutputStream localDerOutputStream1 = new DerOutputStream();
/*  89 */     DerOutputStream localDerOutputStream2 = new DerOutputStream();
/*     */     DerOutputStream localDerOutputStream3;
/*  90 */     if (this.id != null) {
/*  91 */       localDerOutputStream3 = new DerOutputStream();
/*  92 */       this.id.encode(localDerOutputStream3);
/*  93 */       localDerOutputStream2.writeImplicit(DerValue.createTag((byte)-128, false, (byte)0), localDerOutputStream3);
/*     */     }
/*     */     try
/*     */     {
/*  97 */       if (this.names != null) {
/*  98 */         localDerOutputStream3 = new DerOutputStream();
/*  99 */         this.names.encode(localDerOutputStream3);
/* 100 */         localDerOutputStream2.writeImplicit(DerValue.createTag((byte)-128, true, (byte)1), localDerOutputStream3);
/*     */       }
/*     */     }
/*     */     catch (Exception localException) {
/* 104 */       throw new IOException(localException.toString());
/*     */     }
/* 106 */     if (this.serialNum != null) {
/* 107 */       DerOutputStream localDerOutputStream4 = new DerOutputStream();
/* 108 */       this.serialNum.encode(localDerOutputStream4);
/* 109 */       localDerOutputStream2.writeImplicit(DerValue.createTag((byte)-128, false, (byte)2), localDerOutputStream4);
/*     */     }
/*     */ 
/* 112 */     localDerOutputStream1.write((byte)48, localDerOutputStream2);
/* 113 */     this.extensionValue = localDerOutputStream1.toByteArray();
/*     */   }
/*     */ 
/*     */   public AuthorityKeyIdentifierExtension(KeyIdentifier paramKeyIdentifier, GeneralNames paramGeneralNames, SerialNumber paramSerialNumber)
/*     */     throws IOException
/*     */   {
/* 129 */     this.id = paramKeyIdentifier;
/* 130 */     this.names = paramGeneralNames;
/* 131 */     this.serialNum = paramSerialNumber;
/*     */ 
/* 133 */     this.extensionId = PKIXExtensions.AuthorityKey_Id;
/* 134 */     this.critical = false;
/* 135 */     encodeThis();
/*     */   }
/*     */ 
/*     */   public AuthorityKeyIdentifierExtension(Boolean paramBoolean, Object paramObject)
/*     */     throws IOException
/*     */   {
/* 148 */     this.extensionId = PKIXExtensions.AuthorityKey_Id;
/* 149 */     this.critical = paramBoolean.booleanValue();
/*     */ 
/* 151 */     this.extensionValue = ((byte[])paramObject);
/* 152 */     DerValue localDerValue1 = new DerValue(this.extensionValue);
/* 153 */     if (localDerValue1.tag != 48) {
/* 154 */       throw new IOException("Invalid encoding for AuthorityKeyIdentifierExtension.");
/*     */     }
/*     */ 
/* 161 */     while ((localDerValue1.data != null) && (localDerValue1.data.available() != 0)) {
/* 162 */       DerValue localDerValue2 = localDerValue1.data.getDerValue();
/*     */ 
/* 167 */       if ((localDerValue2.isContextSpecific((byte)0)) && (!localDerValue2.isConstructed())) {
/* 168 */         if (this.id != null) {
/* 169 */           throw new IOException("Duplicate KeyIdentifier in AuthorityKeyIdentifier.");
/*     */         }
/* 171 */         localDerValue2.resetTag((byte)4);
/* 172 */         this.id = new KeyIdentifier(localDerValue2);
/*     */       }
/* 174 */       else if ((localDerValue2.isContextSpecific((byte)1)) && (localDerValue2.isConstructed()))
/*     */       {
/* 176 */         if (this.names != null) {
/* 177 */           throw new IOException("Duplicate GeneralNames in AuthorityKeyIdentifier.");
/*     */         }
/* 179 */         localDerValue2.resetTag((byte)48);
/* 180 */         this.names = new GeneralNames(localDerValue2);
/*     */       }
/* 182 */       else if ((localDerValue2.isContextSpecific((byte)2)) && (!localDerValue2.isConstructed()))
/*     */       {
/* 184 */         if (this.serialNum != null) {
/* 185 */           throw new IOException("Duplicate SerialNumber in AuthorityKeyIdentifier.");
/*     */         }
/* 187 */         localDerValue2.resetTag((byte)2);
/* 188 */         this.serialNum = new SerialNumber(localDerValue2);
/*     */       } else {
/* 190 */         throw new IOException("Invalid encoding of AuthorityKeyIdentifierExtension.");
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 199 */     String str = super.toString() + "AuthorityKeyIdentifier [\n";
/* 200 */     if (this.id != null) {
/* 201 */       str = str + this.id.toString();
/*     */     }
/* 203 */     if (this.names != null) {
/* 204 */       str = str + this.names.toString() + "\n";
/*     */     }
/* 206 */     if (this.serialNum != null) {
/* 207 */       str = str + this.serialNum.toString() + "\n";
/*     */     }
/* 209 */     return str + "]\n";
/*     */   }
/*     */ 
/*     */   public void encode(OutputStream paramOutputStream)
/*     */     throws IOException
/*     */   {
/* 219 */     DerOutputStream localDerOutputStream = new DerOutputStream();
/* 220 */     if (this.extensionValue == null) {
/* 221 */       this.extensionId = PKIXExtensions.AuthorityKey_Id;
/* 222 */       this.critical = false;
/* 223 */       encodeThis();
/*     */     }
/* 225 */     super.encode(localDerOutputStream);
/* 226 */     paramOutputStream.write(localDerOutputStream.toByteArray());
/*     */   }
/*     */ 
/*     */   public void set(String paramString, Object paramObject)
/*     */     throws IOException
/*     */   {
/* 233 */     if (paramString.equalsIgnoreCase("key_id")) {
/* 234 */       if (!(paramObject instanceof KeyIdentifier)) {
/* 235 */         throw new IOException("Attribute value should be of type KeyIdentifier.");
/*     */       }
/*     */ 
/* 238 */       this.id = ((KeyIdentifier)paramObject);
/* 239 */     } else if (paramString.equalsIgnoreCase("auth_name")) {
/* 240 */       if (!(paramObject instanceof GeneralNames)) {
/* 241 */         throw new IOException("Attribute value should be of type GeneralNames.");
/*     */       }
/*     */ 
/* 244 */       this.names = ((GeneralNames)paramObject);
/* 245 */     } else if (paramString.equalsIgnoreCase("serial_number")) {
/* 246 */       if (!(paramObject instanceof SerialNumber)) {
/* 247 */         throw new IOException("Attribute value should be of type SerialNumber.");
/*     */       }
/*     */ 
/* 250 */       this.serialNum = ((SerialNumber)paramObject);
/*     */     } else {
/* 252 */       throw new IOException("Attribute name not recognized by CertAttrSet:AuthorityKeyIdentifier.");
/*     */     }
/*     */ 
/* 255 */     encodeThis();
/*     */   }
/*     */ 
/*     */   public Object get(String paramString)
/*     */     throws IOException
/*     */   {
/* 262 */     if (paramString.equalsIgnoreCase("key_id"))
/* 263 */       return this.id;
/* 264 */     if (paramString.equalsIgnoreCase("auth_name"))
/* 265 */       return this.names;
/* 266 */     if (paramString.equalsIgnoreCase("serial_number")) {
/* 267 */       return this.serialNum;
/*     */     }
/* 269 */     throw new IOException("Attribute name not recognized by CertAttrSet:AuthorityKeyIdentifier.");
/*     */   }
/*     */ 
/*     */   public void delete(String paramString)
/*     */     throws IOException
/*     */   {
/* 278 */     if (paramString.equalsIgnoreCase("key_id"))
/* 279 */       this.id = null;
/* 280 */     else if (paramString.equalsIgnoreCase("auth_name"))
/* 281 */       this.names = null;
/* 282 */     else if (paramString.equalsIgnoreCase("serial_number"))
/* 283 */       this.serialNum = null;
/*     */     else {
/* 285 */       throw new IOException("Attribute name not recognized by CertAttrSet:AuthorityKeyIdentifier.");
/*     */     }
/*     */ 
/* 288 */     encodeThis();
/*     */   }
/*     */ 
/*     */   public Enumeration<String> getElements()
/*     */   {
/* 296 */     AttributeNameEnumeration localAttributeNameEnumeration = new AttributeNameEnumeration();
/* 297 */     localAttributeNameEnumeration.addElement("key_id");
/* 298 */     localAttributeNameEnumeration.addElement("auth_name");
/* 299 */     localAttributeNameEnumeration.addElement("serial_number");
/*     */ 
/* 301 */     return localAttributeNameEnumeration.elements();
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 308 */     return "AuthorityKeyIdentifier";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.x509.AuthorityKeyIdentifierExtension
 * JD-Core Version:    0.6.2
 */