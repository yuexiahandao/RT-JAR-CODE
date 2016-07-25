/*     */ package sun.security.pkcs;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.util.Collection;
/*     */ import java.util.Hashtable;
/*     */ import sun.security.util.DerEncoder;
/*     */ import sun.security.util.DerInputStream;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.DerValue;
/*     */ import sun.security.util.ObjectIdentifier;
/*     */ 
/*     */ public class PKCS9Attributes
/*     */ {
/*  46 */   private final Hashtable<ObjectIdentifier, PKCS9Attribute> attributes = new Hashtable(3);
/*     */   private final Hashtable<ObjectIdentifier, ObjectIdentifier> permittedAttributes;
/*     */   private final byte[] derEncoding;
/*  64 */   private boolean ignoreUnsupportedAttributes = false;
/*     */ 
/*     */   public PKCS9Attributes(ObjectIdentifier[] paramArrayOfObjectIdentifier, DerInputStream paramDerInputStream)
/*     */     throws IOException
/*     */   {
/*  86 */     if (paramArrayOfObjectIdentifier != null) {
/*  87 */       this.permittedAttributes = new Hashtable(paramArrayOfObjectIdentifier.length);
/*     */ 
/*  91 */       for (int i = 0; i < paramArrayOfObjectIdentifier.length; i++)
/*  92 */         this.permittedAttributes.put(paramArrayOfObjectIdentifier[i], paramArrayOfObjectIdentifier[i]);
/*     */     }
/*     */     else {
/*  95 */       this.permittedAttributes = null;
/*     */     }
/*     */ 
/*  99 */     this.derEncoding = decode(paramDerInputStream);
/*     */   }
/*     */ 
/*     */   public PKCS9Attributes(DerInputStream paramDerInputStream)
/*     */     throws IOException
/*     */   {
/* 116 */     this(paramDerInputStream, false);
/*     */   }
/*     */ 
/*     */   public PKCS9Attributes(DerInputStream paramDerInputStream, boolean paramBoolean)
/*     */     throws IOException
/*     */   {
/* 138 */     this.ignoreUnsupportedAttributes = paramBoolean;
/*     */ 
/* 140 */     this.derEncoding = decode(paramDerInputStream);
/* 141 */     this.permittedAttributes = null;
/*     */   }
/*     */ 
/*     */   public PKCS9Attributes(PKCS9Attribute[] paramArrayOfPKCS9Attribute)
/*     */     throws IllegalArgumentException, IOException
/*     */   {
/* 160 */     for (int i = 0; i < paramArrayOfPKCS9Attribute.length; i++) {
/* 161 */       ObjectIdentifier localObjectIdentifier = paramArrayOfPKCS9Attribute[i].getOID();
/* 162 */       if (this.attributes.containsKey(localObjectIdentifier)) {
/* 163 */         throw new IllegalArgumentException("PKCSAttribute " + paramArrayOfPKCS9Attribute[i].getOID() + " duplicated while constructing " + "PKCS9Attributes.");
/*     */       }
/*     */ 
/* 168 */       this.attributes.put(localObjectIdentifier, paramArrayOfPKCS9Attribute[i]);
/*     */     }
/* 170 */     this.derEncoding = generateDerEncoding();
/* 171 */     this.permittedAttributes = null;
/*     */   }
/*     */ 
/*     */   private byte[] decode(DerInputStream paramDerInputStream)
/*     */     throws IOException
/*     */   {
/* 188 */     DerValue localDerValue = paramDerInputStream.getDerValue();
/*     */ 
/* 191 */     byte[] arrayOfByte = localDerValue.toByteArray();
/* 192 */     arrayOfByte[0] = 49;
/*     */ 
/* 194 */     DerInputStream localDerInputStream = new DerInputStream(arrayOfByte);
/* 195 */     DerValue[] arrayOfDerValue = localDerInputStream.getSet(3, true);
/*     */ 
/* 199 */     int i = 1;
/*     */ 
/* 201 */     for (int j = 0; j < arrayOfDerValue.length; j++) {
/*     */       PKCS9Attribute localPKCS9Attribute;
/*     */       try {
/* 204 */         localPKCS9Attribute = new PKCS9Attribute(arrayOfDerValue[j]);
/*     */       }
/*     */       catch (ParsingException localParsingException) {
/* 207 */         if (this.ignoreUnsupportedAttributes) {
/* 208 */           i = 0;
/* 209 */           continue;
/*     */         }
/* 211 */         throw localParsingException;
/*     */       }
/*     */ 
/* 214 */       ObjectIdentifier localObjectIdentifier = localPKCS9Attribute.getOID();
/*     */ 
/* 216 */       if (this.attributes.get(localObjectIdentifier) != null) {
/* 217 */         throw new IOException("Duplicate PKCS9 attribute: " + localObjectIdentifier);
/*     */       }
/* 219 */       if ((this.permittedAttributes != null) && (!this.permittedAttributes.containsKey(localObjectIdentifier)))
/*     */       {
/* 221 */         throw new IOException("Attribute " + localObjectIdentifier + " not permitted in this attribute set");
/*     */       }
/*     */ 
/* 224 */       this.attributes.put(localObjectIdentifier, localPKCS9Attribute);
/*     */     }
/* 226 */     return i != 0 ? arrayOfByte : generateDerEncoding();
/*     */   }
/*     */ 
/*     */   public void encode(byte paramByte, OutputStream paramOutputStream)
/*     */     throws IOException
/*     */   {
/* 239 */     paramOutputStream.write(paramByte);
/* 240 */     paramOutputStream.write(this.derEncoding, 1, this.derEncoding.length - 1);
/*     */   }
/*     */ 
/*     */   private byte[] generateDerEncoding() throws IOException {
/* 244 */     DerOutputStream localDerOutputStream = new DerOutputStream();
/* 245 */     Object[] arrayOfObject = this.attributes.values().toArray();
/*     */ 
/* 247 */     localDerOutputStream.putOrderedSetOf((byte)49, castToDerEncoder(arrayOfObject));
/*     */ 
/* 249 */     return localDerOutputStream.toByteArray();
/*     */   }
/*     */ 
/*     */   public byte[] getDerEncoding()
/*     */     throws IOException
/*     */   {
/* 257 */     return (byte[])this.derEncoding.clone();
/*     */   }
/*     */ 
/*     */   public PKCS9Attribute getAttribute(ObjectIdentifier paramObjectIdentifier)
/*     */   {
/* 265 */     return (PKCS9Attribute)this.attributes.get(paramObjectIdentifier);
/*     */   }
/*     */ 
/*     */   public PKCS9Attribute getAttribute(String paramString)
/*     */   {
/* 272 */     return (PKCS9Attribute)this.attributes.get(PKCS9Attribute.getOID(paramString));
/*     */   }
/*     */ 
/*     */   public PKCS9Attribute[] getAttributes()
/*     */   {
/* 280 */     PKCS9Attribute[] arrayOfPKCS9Attribute = new PKCS9Attribute[this.attributes.size()];
/*     */ 
/* 283 */     int i = 0;
/* 284 */     for (int j = 1; (j < PKCS9Attribute.PKCS9_OIDS.length) && (i < arrayOfPKCS9Attribute.length); 
/* 285 */       j++) {
/* 286 */       arrayOfPKCS9Attribute[i] = getAttribute(PKCS9Attribute.PKCS9_OIDS[j]);
/*     */ 
/* 288 */       if (arrayOfPKCS9Attribute[i] != null)
/* 289 */         i++;
/*     */     }
/* 291 */     return arrayOfPKCS9Attribute;
/*     */   }
/*     */ 
/*     */   public Object getAttributeValue(ObjectIdentifier paramObjectIdentifier)
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 300 */       return getAttribute(paramObjectIdentifier).getValue();
/*     */     } catch (NullPointerException localNullPointerException) {
/*     */     }
/* 303 */     throw new IOException("No value found for attribute " + paramObjectIdentifier);
/*     */   }
/*     */ 
/*     */   public Object getAttributeValue(String paramString)
/*     */     throws IOException
/*     */   {
/* 312 */     ObjectIdentifier localObjectIdentifier = PKCS9Attribute.getOID(paramString);
/*     */ 
/* 314 */     if (localObjectIdentifier == null) {
/* 315 */       throw new IOException("Attribute name " + paramString + " not recognized or not supported.");
/*     */     }
/*     */ 
/* 318 */     return getAttributeValue(localObjectIdentifier);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 326 */     StringBuffer localStringBuffer = new StringBuffer(200);
/* 327 */     localStringBuffer.append("PKCS9 Attributes: [\n\t");
/*     */ 
/* 332 */     int i = 1;
/* 333 */     for (int j = 1; j < PKCS9Attribute.PKCS9_OIDS.length; j++) {
/* 334 */       PKCS9Attribute localPKCS9Attribute = getAttribute(PKCS9Attribute.PKCS9_OIDS[j]);
/*     */ 
/* 336 */       if (localPKCS9Attribute != null)
/*     */       {
/* 339 */         if (i != 0)
/* 340 */           i = 0;
/*     */         else {
/* 342 */           localStringBuffer.append(";\n\t");
/*     */         }
/* 344 */         localStringBuffer.append(localPKCS9Attribute.toString());
/*     */       }
/*     */     }
/* 347 */     localStringBuffer.append("\n\t] (end PKCS9 Attributes)");
/*     */ 
/* 349 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   static DerEncoder[] castToDerEncoder(Object[] paramArrayOfObject)
/*     */   {
/* 358 */     DerEncoder[] arrayOfDerEncoder = new DerEncoder[paramArrayOfObject.length];
/*     */ 
/* 360 */     for (int i = 0; i < arrayOfDerEncoder.length; i++) {
/* 361 */       arrayOfDerEncoder[i] = ((DerEncoder)paramArrayOfObject[i]);
/*     */     }
/* 363 */     return arrayOfDerEncoder;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.pkcs.PKCS9Attributes
 * JD-Core Version:    0.6.2
 */