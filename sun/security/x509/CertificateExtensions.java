/*     */ package sun.security.x509;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.security.cert.CertificateException;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Map;
/*     */ import java.util.TreeMap;
/*     */ import sun.misc.HexDumpEncoder;
/*     */ import sun.security.util.Debug;
/*     */ import sun.security.util.DerInputStream;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.DerValue;
/*     */ import sun.security.util.ObjectIdentifier;
/*     */ 
/*     */ public class CertificateExtensions
/*     */   implements CertAttrSet<Extension>
/*     */ {
/*     */   public static final String IDENT = "x509.info.extensions";
/*     */   public static final String NAME = "extensions";
/*  58 */   private static final Debug debug = Debug.getInstance("x509");
/*     */ 
/*  60 */   private Map<String, Extension> map = Collections.synchronizedMap(new TreeMap());
/*     */ 
/*  62 */   private boolean unsupportedCritExt = false;
/*     */   private Map<String, Extension> unparseableExtensions;
/*  92 */   private static Class[] PARAMS = { Boolean.class, Object.class };
/*     */ 
/*     */   public CertificateExtensions()
/*     */   {
/*     */   }
/*     */ 
/*     */   public CertificateExtensions(DerInputStream paramDerInputStream)
/*     */     throws IOException
/*     */   {
/*  78 */     init(paramDerInputStream);
/*     */   }
/*     */ 
/*     */   private void init(DerInputStream paramDerInputStream)
/*     */     throws IOException
/*     */   {
/*  84 */     DerValue[] arrayOfDerValue = paramDerInputStream.getSequence(5);
/*     */ 
/*  86 */     for (int i = 0; i < arrayOfDerValue.length; i++) {
/*  87 */       Extension localExtension = new Extension(arrayOfDerValue[i]);
/*  88 */       parseExtension(localExtension);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void parseExtension(Extension paramExtension)
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/*  97 */       Class localClass = OIDMap.getClass(paramExtension.getExtensionId());
/*  98 */       if (localClass == null) {
/*  99 */         if (paramExtension.isCritical()) {
/* 100 */           this.unsupportedCritExt = true;
/*     */         }
/* 102 */         if (this.map.put(paramExtension.getExtensionId().toString(), paramExtension) == null) {
/* 103 */           return;
/*     */         }
/* 105 */         throw new IOException("Duplicate extensions not allowed");
/*     */       }
/*     */ 
/* 108 */       localObject1 = localClass.getConstructor(PARAMS);
/*     */ 
/* 110 */       localObject2 = new Object[] { Boolean.valueOf(paramExtension.isCritical()), paramExtension.getExtensionValue() };
/*     */ 
/* 112 */       CertAttrSet localCertAttrSet = (CertAttrSet)((Constructor)localObject1).newInstance((Object[])localObject2);
/* 113 */       if (this.map.put(localCertAttrSet.getName(), (Extension)localCertAttrSet) != null)
/* 114 */         throw new IOException("Duplicate extensions not allowed");
/*     */     }
/*     */     catch (InvocationTargetException localInvocationTargetException)
/*     */     {
/*     */       Object localObject2;
/* 117 */       Object localObject1 = localInvocationTargetException.getTargetException();
/* 118 */       if (!paramExtension.isCritical())
/*     */       {
/* 120 */         if (this.unparseableExtensions == null) {
/* 121 */           this.unparseableExtensions = new TreeMap();
/*     */         }
/* 123 */         this.unparseableExtensions.put(paramExtension.getExtensionId().toString(), new UnparseableExtension(paramExtension, (Throwable)localObject1));
/*     */ 
/* 125 */         if (debug != null) {
/* 126 */           debug.println("Error parsing extension: " + paramExtension);
/* 127 */           ((Throwable)localObject1).printStackTrace();
/* 128 */           localObject2 = new HexDumpEncoder();
/* 129 */           System.err.println(((HexDumpEncoder)localObject2).encodeBuffer(paramExtension.getExtensionValue()));
/*     */         }
/* 131 */         return;
/*     */       }
/* 133 */       if ((localObject1 instanceof IOException)) {
/* 134 */         throw ((IOException)localObject1);
/*     */       }
/* 136 */       throw ((IOException)new IOException(((Throwable)localObject1).toString()).initCause((Throwable)localObject1));
/*     */     }
/*     */     catch (IOException localIOException) {
/* 139 */       throw localIOException;
/*     */     } catch (Exception localException) {
/* 141 */       throw ((IOException)new IOException(localException.toString()).initCause(localException));
/*     */     }
/*     */   }
/*     */ 
/*     */   public void encode(OutputStream paramOutputStream)
/*     */     throws CertificateException, IOException
/*     */   {
/* 155 */     encode(paramOutputStream, false);
/*     */   }
/*     */ 
/*     */   public void encode(OutputStream paramOutputStream, boolean paramBoolean)
/*     */     throws CertificateException, IOException
/*     */   {
/* 168 */     DerOutputStream localDerOutputStream1 = new DerOutputStream();
/* 169 */     Collection localCollection = this.map.values();
/* 170 */     Object[] arrayOfObject = localCollection.toArray();
/*     */ 
/* 172 */     for (int i = 0; i < arrayOfObject.length; i++) {
/* 173 */       if ((arrayOfObject[i] instanceof CertAttrSet))
/* 174 */         ((CertAttrSet)arrayOfObject[i]).encode(localDerOutputStream1);
/* 175 */       else if ((arrayOfObject[i] instanceof Extension))
/* 176 */         ((Extension)arrayOfObject[i]).encode(localDerOutputStream1);
/*     */       else {
/* 178 */         throw new CertificateException("Illegal extension object");
/*     */       }
/*     */     }
/* 181 */     DerOutputStream localDerOutputStream2 = new DerOutputStream();
/* 182 */     localDerOutputStream2.write((byte)48, localDerOutputStream1);
/*     */     DerOutputStream localDerOutputStream3;
/* 185 */     if (!paramBoolean) {
/* 186 */       localDerOutputStream3 = new DerOutputStream();
/* 187 */       localDerOutputStream3.write(DerValue.createTag((byte)-128, true, (byte)3), localDerOutputStream2);
/*     */     }
/*     */     else {
/* 190 */       localDerOutputStream3 = localDerOutputStream2;
/*     */     }
/* 192 */     paramOutputStream.write(localDerOutputStream3.toByteArray());
/*     */   }
/*     */ 
/*     */   public void set(String paramString, Object paramObject)
/*     */     throws IOException
/*     */   {
/* 202 */     if ((paramObject instanceof Extension))
/* 203 */       this.map.put(paramString, (Extension)paramObject);
/*     */     else
/* 205 */       throw new IOException("Unknown extension type.");
/*     */   }
/*     */ 
/*     */   public Object get(String paramString)
/*     */     throws IOException
/*     */   {
/* 215 */     Object localObject = this.map.get(paramString);
/* 216 */     if (localObject == null) {
/* 217 */       throw new IOException("No extension found with name " + paramString);
/*     */     }
/* 219 */     return localObject;
/*     */   }
/*     */ 
/*     */   Extension getExtension(String paramString)
/*     */   {
/* 225 */     return (Extension)this.map.get(paramString);
/*     */   }
/*     */ 
/*     */   public void delete(String paramString)
/*     */     throws IOException
/*     */   {
/* 234 */     Object localObject = this.map.get(paramString);
/* 235 */     if (localObject == null) {
/* 236 */       throw new IOException("No extension found with name " + paramString);
/*     */     }
/* 238 */     this.map.remove(paramString);
/*     */   }
/*     */ 
/*     */   public String getNameByOid(ObjectIdentifier paramObjectIdentifier) throws IOException {
/* 242 */     for (String str : this.map.keySet()) {
/* 243 */       if (((Extension)this.map.get(str)).getExtensionId().equals(paramObjectIdentifier)) {
/* 244 */         return str;
/*     */       }
/*     */     }
/* 247 */     return null;
/*     */   }
/*     */ 
/*     */   public Enumeration<Extension> getElements()
/*     */   {
/* 255 */     return Collections.enumeration(this.map.values());
/*     */   }
/*     */ 
/*     */   public Collection<Extension> getAllExtensions()
/*     */   {
/* 263 */     return this.map.values();
/*     */   }
/*     */ 
/*     */   public Map<String, Extension> getUnparseableExtensions() {
/* 267 */     if (this.unparseableExtensions == null) {
/* 268 */       return Collections.emptyMap();
/*     */     }
/* 270 */     return this.unparseableExtensions;
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 278 */     return "extensions";
/*     */   }
/*     */ 
/*     */   public boolean hasUnsupportedCriticalExtension()
/*     */   {
/* 286 */     return this.unsupportedCritExt;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 301 */     if (this == paramObject)
/* 302 */       return true;
/* 303 */     if (!(paramObject instanceof CertificateExtensions))
/* 304 */       return false;
/* 305 */     Collection localCollection = ((CertificateExtensions)paramObject).getAllExtensions();
/*     */ 
/* 307 */     Object[] arrayOfObject = localCollection.toArray();
/*     */ 
/* 309 */     int i = arrayOfObject.length;
/* 310 */     if (i != this.map.size()) {
/* 311 */       return false;
/*     */     }
/*     */ 
/* 314 */     String str = null;
/* 315 */     for (int j = 0; j < i; j++) {
/* 316 */       if ((arrayOfObject[j] instanceof CertAttrSet))
/* 317 */         str = ((CertAttrSet)arrayOfObject[j]).getName();
/* 318 */       Extension localExtension1 = (Extension)arrayOfObject[j];
/* 319 */       if (str == null)
/* 320 */         str = localExtension1.getExtensionId().toString();
/* 321 */       Extension localExtension2 = (Extension)this.map.get(str);
/* 322 */       if (localExtension2 == null)
/* 323 */         return false;
/* 324 */       if (!localExtension2.equals(localExtension1))
/* 325 */         return false;
/*     */     }
/* 327 */     return getUnparseableExtensions().equals(((CertificateExtensions)paramObject).getUnparseableExtensions());
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 337 */     return this.map.hashCode() + getUnparseableExtensions().hashCode();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 349 */     return this.map.toString();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.x509.CertificateExtensions
 * JD-Core Version:    0.6.2
 */