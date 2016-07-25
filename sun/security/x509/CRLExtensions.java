/*     */ package sun.security.x509;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.security.cert.CRLException;
/*     */ import java.security.cert.CertificateException;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Map;
/*     */ import java.util.TreeMap;
/*     */ import sun.security.util.DerInputStream;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.DerValue;
/*     */ import sun.security.util.ObjectIdentifier;
/*     */ 
/*     */ public class CRLExtensions
/*     */ {
/*  67 */   private Map<String, Extension> map = Collections.synchronizedMap(new TreeMap());
/*     */ 
/*  69 */   private boolean unsupportedCritExt = false;
/*     */ 
/* 110 */   private static final Class[] PARAMS = { Boolean.class, Object.class };
/*     */ 
/*     */   public CRLExtensions()
/*     */   {
/*     */   }
/*     */ 
/*     */   public CRLExtensions(DerInputStream paramDerInputStream)
/*     */     throws CRLException
/*     */   {
/*  84 */     init(paramDerInputStream);
/*     */   }
/*     */ 
/*     */   private void init(DerInputStream paramDerInputStream) throws CRLException
/*     */   {
/*     */     try {
/*  90 */       DerInputStream localDerInputStream = paramDerInputStream;
/*     */ 
/*  92 */       int i = (byte)paramDerInputStream.peekByte();
/*     */ 
/*  94 */       if (((i & 0xC0) == 128) && ((i & 0x1F) == 0))
/*     */       {
/*  96 */         localObject = localDerInputStream.getDerValue();
/*  97 */         localDerInputStream = ((DerValue)localObject).data;
/*     */       }
/*     */ 
/* 100 */       Object localObject = localDerInputStream.getSequence(5);
/* 101 */       for (int j = 0; j < localObject.length; j++) {
/* 102 */         Extension localExtension = new Extension(localObject[j]);
/* 103 */         parseExtension(localExtension);
/*     */       }
/*     */     } catch (IOException localIOException) {
/* 106 */       throw new CRLException("Parsing error: " + localIOException.toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   private void parseExtension(Extension paramExtension)
/*     */     throws CRLException
/*     */   {
/*     */     try
/*     */     {
/* 115 */       Class localClass = OIDMap.getClass(paramExtension.getExtensionId());
/* 116 */       if (localClass == null) {
/* 117 */         if (paramExtension.isCritical())
/* 118 */           this.unsupportedCritExt = true;
/* 119 */         if (this.map.put(paramExtension.getExtensionId().toString(), paramExtension) != null)
/* 120 */           throw new CRLException("Duplicate extensions not allowed");
/* 121 */         return;
/*     */       }
/* 123 */       Constructor localConstructor = localClass.getConstructor(PARAMS);
/* 124 */       Object[] arrayOfObject = { Boolean.valueOf(paramExtension.isCritical()), paramExtension.getExtensionValue() };
/*     */ 
/* 126 */       CertAttrSet localCertAttrSet = (CertAttrSet)localConstructor.newInstance(arrayOfObject);
/* 127 */       if (this.map.put(localCertAttrSet.getName(), (Extension)localCertAttrSet) != null)
/* 128 */         throw new CRLException("Duplicate extensions not allowed");
/*     */     }
/*     */     catch (InvocationTargetException localInvocationTargetException) {
/* 131 */       throw new CRLException(localInvocationTargetException.getTargetException().getMessage());
/*     */     } catch (Exception localException) {
/* 133 */       throw new CRLException(localException.toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   public void encode(OutputStream paramOutputStream, boolean paramBoolean)
/*     */     throws CRLException
/*     */   {
/*     */     try
/*     */     {
/* 148 */       DerOutputStream localDerOutputStream1 = new DerOutputStream();
/* 149 */       Collection localCollection = this.map.values();
/* 150 */       Object[] arrayOfObject = localCollection.toArray();
/*     */ 
/* 152 */       for (int i = 0; i < arrayOfObject.length; i++) {
/* 153 */         if ((arrayOfObject[i] instanceof CertAttrSet))
/* 154 */           ((CertAttrSet)arrayOfObject[i]).encode(localDerOutputStream1);
/* 155 */         else if ((arrayOfObject[i] instanceof Extension))
/* 156 */           ((Extension)arrayOfObject[i]).encode(localDerOutputStream1);
/*     */         else {
/* 158 */           throw new CRLException("Illegal extension object");
/*     */         }
/*     */       }
/* 161 */       DerOutputStream localDerOutputStream2 = new DerOutputStream();
/* 162 */       localDerOutputStream2.write((byte)48, localDerOutputStream1);
/*     */ 
/* 164 */       DerOutputStream localDerOutputStream3 = new DerOutputStream();
/* 165 */       if (paramBoolean) {
/* 166 */         localDerOutputStream3.write(DerValue.createTag((byte)-128, true, (byte)0), localDerOutputStream2);
/*     */       }
/*     */       else {
/* 169 */         localDerOutputStream3 = localDerOutputStream2;
/*     */       }
/* 171 */       paramOutputStream.write(localDerOutputStream3.toByteArray());
/*     */     } catch (IOException localIOException) {
/* 173 */       throw new CRLException("Encoding error: " + localIOException.toString());
/*     */     } catch (CertificateException localCertificateException) {
/* 175 */       throw new CRLException("Encoding error: " + localCertificateException.toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   public Extension get(String paramString)
/*     */   {
/* 185 */     X509AttributeName localX509AttributeName = new X509AttributeName(paramString);
/*     */ 
/* 187 */     String str2 = localX509AttributeName.getPrefix();
/*     */     String str1;
/* 188 */     if (str2.equalsIgnoreCase("x509")) {
/* 189 */       int i = paramString.lastIndexOf(".");
/* 190 */       str1 = paramString.substring(i + 1);
/*     */     } else {
/* 192 */       str1 = paramString;
/* 193 */     }return (Extension)this.map.get(str1);
/*     */   }
/*     */ 
/*     */   public void set(String paramString, Object paramObject)
/*     */   {
/* 204 */     this.map.put(paramString, (Extension)paramObject);
/*     */   }
/*     */ 
/*     */   public void delete(String paramString)
/*     */   {
/* 213 */     this.map.remove(paramString);
/*     */   }
/*     */ 
/*     */   public Enumeration<Extension> getElements()
/*     */   {
/* 221 */     return Collections.enumeration(this.map.values());
/*     */   }
/*     */ 
/*     */   public Collection<Extension> getAllExtensions()
/*     */   {
/* 229 */     return this.map.values();
/*     */   }
/*     */ 
/*     */   public boolean hasUnsupportedCriticalExtension()
/*     */   {
/* 237 */     return this.unsupportedCritExt;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 251 */     if (this == paramObject)
/* 252 */       return true;
/* 253 */     if (!(paramObject instanceof CRLExtensions))
/* 254 */       return false;
/* 255 */     Collection localCollection = ((CRLExtensions)paramObject).getAllExtensions();
/*     */ 
/* 257 */     Object[] arrayOfObject = localCollection.toArray();
/*     */ 
/* 259 */     int i = arrayOfObject.length;
/* 260 */     if (i != this.map.size()) {
/* 261 */       return false;
/*     */     }
/*     */ 
/* 264 */     String str = null;
/* 265 */     for (int j = 0; j < i; j++) {
/* 266 */       if ((arrayOfObject[j] instanceof CertAttrSet))
/* 267 */         str = ((CertAttrSet)arrayOfObject[j]).getName();
/* 268 */       Extension localExtension1 = (Extension)arrayOfObject[j];
/* 269 */       if (str == null)
/* 270 */         str = localExtension1.getExtensionId().toString();
/* 271 */       Extension localExtension2 = (Extension)this.map.get(str);
/* 272 */       if (localExtension2 == null)
/* 273 */         return false;
/* 274 */       if (!localExtension2.equals(localExtension1))
/* 275 */         return false;
/*     */     }
/* 277 */     return true;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 286 */     return this.map.hashCode();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 298 */     return this.map.toString();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.x509.CRLExtensions
 * JD-Core Version:    0.6.2
 */