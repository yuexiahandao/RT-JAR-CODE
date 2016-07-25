/*     */ package javax.xml.crypto.dsig;
/*     */ 
/*     */ import java.security.InvalidAlgorithmParameterException;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.NoSuchProviderException;
/*     */ import java.security.Provider;
/*     */ import java.security.Provider.Service;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map.Entry;
/*     */ import javax.xml.crypto.MarshalException;
/*     */ import javax.xml.crypto.XMLCryptoContext;
/*     */ import javax.xml.crypto.XMLStructure;
/*     */ import javax.xml.crypto.dsig.spec.TransformParameterSpec;
/*     */ import sun.security.jca.GetInstance;
/*     */ import sun.security.jca.GetInstance.Instance;
/*     */ 
/*     */ public abstract class TransformService
/*     */   implements Transform
/*     */ {
/*     */   private String algorithm;
/*     */   private String mechanism;
/*     */   private Provider provider;
/*     */ 
/*     */   public static TransformService getInstance(String paramString1, String paramString2)
/*     */     throws NoSuchAlgorithmException
/*     */   {
/* 153 */     if ((paramString2 == null) || (paramString1 == null)) {
/* 154 */       throw new NullPointerException();
/*     */     }
/* 156 */     int i = 0;
/* 157 */     if (paramString2.equals("DOM")) {
/* 158 */       i = 1;
/*     */     }
/* 160 */     List localList = GetInstance.getServices("TransformService", paramString1);
/* 161 */     for (Iterator localIterator = localList.iterator(); localIterator.hasNext(); ) {
/* 162 */       Provider.Service localService = (Provider.Service)localIterator.next();
/* 163 */       String str = localService.getAttribute("MechanismType");
/* 164 */       if (((str == null) && (i != 0)) || ((str != null) && (str.equals(paramString2))))
/*     */       {
/* 166 */         GetInstance.Instance localInstance = GetInstance.getInstance(localService, null);
/* 167 */         TransformService localTransformService = (TransformService)localInstance.impl;
/* 168 */         localTransformService.algorithm = paramString1;
/* 169 */         localTransformService.mechanism = paramString2;
/* 170 */         localTransformService.provider = localInstance.provider;
/* 171 */         return localTransformService;
/*     */       }
/*     */     }
/* 174 */     throw new NoSuchAlgorithmException(paramString1 + " algorithm and " + paramString2 + " mechanism not available");
/*     */   }
/*     */ 
/*     */   public static TransformService getInstance(String paramString1, String paramString2, Provider paramProvider)
/*     */     throws NoSuchAlgorithmException
/*     */   {
/* 202 */     if ((paramString2 == null) || (paramString1 == null) || (paramProvider == null)) {
/* 203 */       throw new NullPointerException();
/*     */     }
/*     */ 
/* 206 */     int i = 0;
/* 207 */     if (paramString2.equals("DOM")) {
/* 208 */       i = 1;
/*     */     }
/* 210 */     Provider.Service localService = GetInstance.getService("TransformService", paramString1, paramProvider);
/*     */ 
/* 212 */     String str = localService.getAttribute("MechanismType");
/* 213 */     if (((str == null) && (i != 0)) || ((str != null) && (str.equals(paramString2))))
/*     */     {
/* 215 */       GetInstance.Instance localInstance = GetInstance.getInstance(localService, null);
/* 216 */       TransformService localTransformService = (TransformService)localInstance.impl;
/* 217 */       localTransformService.algorithm = paramString1;
/* 218 */       localTransformService.mechanism = paramString2;
/* 219 */       localTransformService.provider = localInstance.provider;
/* 220 */       return localTransformService;
/*     */     }
/* 222 */     throw new NoSuchAlgorithmException(paramString1 + " algorithm and " + paramString2 + " mechanism not available");
/*     */   }
/*     */ 
/*     */   public static TransformService getInstance(String paramString1, String paramString2, String paramString3)
/*     */     throws NoSuchAlgorithmException, NoSuchProviderException
/*     */   {
/* 254 */     if ((paramString2 == null) || (paramString1 == null) || (paramString3 == null))
/* 255 */       throw new NullPointerException();
/* 256 */     if (paramString3.length() == 0) {
/* 257 */       throw new NoSuchProviderException();
/*     */     }
/* 259 */     int i = 0;
/* 260 */     if (paramString2.equals("DOM")) {
/* 261 */       i = 1;
/*     */     }
/* 263 */     Provider.Service localService = GetInstance.getService("TransformService", paramString1, paramString3);
/*     */ 
/* 265 */     String str = localService.getAttribute("MechanismType");
/* 266 */     if (((str == null) && (i != 0)) || ((str != null) && (str.equals(paramString2))))
/*     */     {
/* 268 */       GetInstance.Instance localInstance = GetInstance.getInstance(localService, null);
/* 269 */       TransformService localTransformService = (TransformService)localInstance.impl;
/* 270 */       localTransformService.algorithm = paramString1;
/* 271 */       localTransformService.mechanism = paramString2;
/* 272 */       localTransformService.provider = localInstance.provider;
/* 273 */       return localTransformService;
/*     */     }
/* 275 */     throw new NoSuchAlgorithmException(paramString1 + " algorithm and " + paramString2 + " mechanism not available");
/*     */   }
/*     */ 
/*     */   public final String getMechanismType()
/*     */   {
/* 320 */     return this.mechanism;
/*     */   }
/*     */ 
/*     */   public final String getAlgorithm()
/*     */   {
/* 330 */     return this.algorithm;
/*     */   }
/*     */ 
/*     */   public final Provider getProvider()
/*     */   {
/* 339 */     return this.provider;
/*     */   }
/*     */ 
/*     */   public abstract void init(TransformParameterSpec paramTransformParameterSpec)
/*     */     throws InvalidAlgorithmParameterException;
/*     */ 
/*     */   public abstract void marshalParams(XMLStructure paramXMLStructure, XMLCryptoContext paramXMLCryptoContext)
/*     */     throws MarshalException;
/*     */ 
/*     */   public abstract void init(XMLStructure paramXMLStructure, XMLCryptoContext paramXMLCryptoContext)
/*     */     throws InvalidAlgorithmParameterException;
/*     */ 
/*     */   private static class MechanismMapEntry
/*     */     implements Map.Entry
/*     */   {
/*     */     private final String mechanism;
/*     */     private final String algorithm;
/*     */     private final String key;
/*     */ 
/*     */     MechanismMapEntry(String paramString1, String paramString2)
/*     */     {
/* 285 */       this.algorithm = paramString1;
/* 286 */       this.mechanism = paramString2;
/* 287 */       this.key = ("TransformService." + paramString1 + " MechanismType");
/*     */     }
/*     */     public boolean equals(Object paramObject) {
/* 290 */       if (!(paramObject instanceof Map.Entry)) {
/* 291 */         return false;
/*     */       }
/* 293 */       Map.Entry localEntry = (Map.Entry)paramObject;
/* 294 */       return (getKey() == null ? localEntry.getKey() == null : getKey().equals(localEntry.getKey())) && (getValue() == null ? localEntry.getValue() == null : getValue().equals(localEntry.getValue()));
/*     */     }
/*     */ 
/*     */     public Object getKey()
/*     */     {
/* 300 */       return this.key;
/*     */     }
/*     */     public Object getValue() {
/* 303 */       return this.mechanism;
/*     */     }
/*     */     public Object setValue(Object paramObject) {
/* 306 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     public int hashCode() {
/* 309 */       return (getKey() == null ? 0 : getKey().hashCode()) ^ (getValue() == null ? 0 : getValue().hashCode());
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.crypto.dsig.TransformService
 * JD-Core Version:    0.6.2
 */