/*     */ package com.sun.corba.se.impl.resolver;
/*     */ 
/*     */ import com.sun.corba.se.impl.encoding.EncapsInputStream;
/*     */ import com.sun.corba.se.impl.logging.OMGSystemException;
/*     */ import com.sun.corba.se.impl.logging.ORBUtilSystemException;
/*     */ import com.sun.corba.se.impl.naming.namingutil.CorbalocURL;
/*     */ import com.sun.corba.se.impl.naming.namingutil.CorbanameURL;
/*     */ import com.sun.corba.se.impl.naming.namingutil.IIOPEndpointInfo;
/*     */ import com.sun.corba.se.impl.naming.namingutil.INSURL;
/*     */ import com.sun.corba.se.impl.naming.namingutil.INSURLHandler;
/*     */ import com.sun.corba.se.impl.orbutil.ORBUtility;
/*     */ import com.sun.corba.se.spi.ior.IOR;
/*     */ import com.sun.corba.se.spi.ior.IORFactories;
/*     */ import com.sun.corba.se.spi.ior.IORTemplate;
/*     */ import com.sun.corba.se.spi.ior.ObjectKey;
/*     */ import com.sun.corba.se.spi.ior.ObjectKeyFactory;
/*     */ import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
/*     */ import com.sun.corba.se.spi.ior.iiop.IIOPAddress;
/*     */ import com.sun.corba.se.spi.ior.iiop.IIOPFactories;
/*     */ import com.sun.corba.se.spi.ior.iiop.IIOPProfileTemplate;
/*     */ import com.sun.corba.se.spi.orb.ORB;
/*     */ import com.sun.corba.se.spi.orb.ORBData;
/*     */ import com.sun.corba.se.spi.orb.Operation;
/*     */ import com.sun.corba.se.spi.resolver.LocalResolver;
/*     */ import com.sun.corba.se.spi.resolver.Resolver;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.omg.CosNaming.NamingContextExt;
/*     */ import org.omg.CosNaming.NamingContextExtHelper;
/*     */ import sun.corba.EncapsInputStreamFactory;
/*     */ 
/*     */ public class INSURLOperationImpl
/*     */   implements Operation
/*     */ {
/*     */   ORB orb;
/*     */   ORBUtilSystemException wrapper;
/*     */   OMGSystemException omgWrapper;
/*     */   Resolver bootstrapResolver;
/*     */   private NamingContextExt rootNamingContextExt;
/*  87 */   private java.lang.Object rootContextCacheLock = new java.lang.Object();
/*     */ 
/*  90 */   private INSURLHandler insURLHandler = INSURLHandler.getINSURLHandler();
/*     */   private static final int NIBBLES_PER_BYTE = 2;
/*     */   private static final int UN_SHIFT = 4;
/*     */ 
/*     */   public INSURLOperationImpl(ORB paramORB, Resolver paramResolver)
/*     */   {
/*  94 */     this.orb = paramORB;
/*  95 */     this.wrapper = ORBUtilSystemException.get(paramORB, "orb.resolver");
/*     */ 
/*  97 */     this.omgWrapper = OMGSystemException.get(paramORB, "orb.resolver");
/*     */ 
/*  99 */     this.bootstrapResolver = paramResolver;
/*     */   }
/*     */ 
/*     */   private org.omg.CORBA.Object getIORFromString(String paramString)
/*     */   {
/* 111 */     if ((paramString.length() & 0x1) == 1) {
/* 112 */       throw this.wrapper.badStringifiedIorLen();
/*     */     }
/* 114 */     byte[] arrayOfByte = new byte[(paramString.length() - "IOR:".length()) / 2];
/* 115 */     int i = "IOR:".length(); for (int j = 0; i < paramString.length(); j++) {
/* 116 */       arrayOfByte[j] = ((byte)(ORBUtility.hexOf(paramString.charAt(i)) << 4 & 0xF0));
/*     */       int tmp72_70 = j;
/*     */       byte[] tmp72_69 = arrayOfByte; tmp72_69[tmp72_70] = ((byte)(tmp72_69[tmp72_70] | (byte)(ORBUtility.hexOf(paramString.charAt(i + 1)) & 0xF)));
/*     */ 
/* 115 */       i += 2;
/*     */     }
/*     */ 
/* 119 */     EncapsInputStream localEncapsInputStream = EncapsInputStreamFactory.newEncapsInputStream(this.orb, arrayOfByte, arrayOfByte.length, this.orb.getORBData().getGIOPVersion());
/*     */ 
/* 121 */     localEncapsInputStream.consumeEndian();
/* 122 */     return localEncapsInputStream.read_Object();
/*     */   }
/*     */ 
/*     */   public java.lang.Object operate(java.lang.Object paramObject)
/*     */   {
/* 127 */     if ((paramObject instanceof String)) {
/* 128 */       String str = (String)paramObject;
/*     */ 
/* 130 */       if (str.startsWith("IOR:"))
/*     */       {
/* 132 */         return getIORFromString(str);
/*     */       }
/* 134 */       INSURL localINSURL = this.insURLHandler.parseURL(str);
/* 135 */       if (localINSURL == null)
/* 136 */         throw this.omgWrapper.soBadSchemeName();
/* 137 */       return resolveINSURL(localINSURL);
/*     */     }
/*     */ 
/* 141 */     throw this.wrapper.stringExpected();
/*     */   }
/*     */ 
/*     */   private org.omg.CORBA.Object resolveINSURL(INSURL paramINSURL)
/*     */   {
/* 146 */     if (paramINSURL.isCorbanameURL()) {
/* 147 */       return resolveCorbaname((CorbanameURL)paramINSURL);
/*     */     }
/* 149 */     return resolveCorbaloc((CorbalocURL)paramINSURL);
/*     */   }
/*     */ 
/*     */   private org.omg.CORBA.Object resolveCorbaloc(CorbalocURL paramCorbalocURL)
/*     */   {
/* 161 */     org.omg.CORBA.Object localObject = null;
/*     */ 
/* 163 */     if (paramCorbalocURL.getRIRFlag())
/* 164 */       localObject = this.bootstrapResolver.resolve(paramCorbalocURL.getKeyString());
/*     */     else {
/* 166 */       localObject = getIORUsingCorbaloc(paramCorbalocURL);
/*     */     }
/*     */ 
/* 169 */     return localObject;
/*     */   }
/*     */ 
/*     */   private org.omg.CORBA.Object resolveCorbaname(CorbanameURL paramCorbanameURL)
/*     */   {
/* 178 */     java.lang.Object localObject1 = null;
/*     */     try
/*     */     {
/* 181 */       NamingContextExt localNamingContextExt = null;
/*     */ 
/* 183 */       if (paramCorbanameURL.getRIRFlag())
/*     */       {
/* 185 */         localNamingContextExt = getDefaultRootNamingContext();
/*     */       }
/*     */       else {
/* 188 */         localObject2 = getIORUsingCorbaloc(paramCorbanameURL);
/*     */ 
/* 190 */         if (localObject2 == null) {
/* 191 */           return null;
/*     */         }
/*     */ 
/* 194 */         localNamingContextExt = NamingContextExtHelper.narrow((org.omg.CORBA.Object)localObject2);
/*     */       }
/*     */ 
/* 198 */       java.lang.Object localObject2 = paramCorbanameURL.getStringifiedName();
/*     */ 
/* 200 */       if (localObject2 == null)
/*     */       {
/* 202 */         return localNamingContextExt;
/*     */       }
/* 204 */       return localNamingContextExt.resolve_str((String)localObject2);
/*     */     }
/*     */     catch (Exception localException) {
/* 207 */       clearRootNamingContextCache();
/* 208 */     }return null;
/*     */   }
/*     */ 
/*     */   private org.omg.CORBA.Object getIORUsingCorbaloc(INSURL paramINSURL)
/*     */   {
/* 219 */     HashMap localHashMap = new HashMap();
/* 220 */     ArrayList localArrayList = new ArrayList();
/*     */ 
/* 224 */     List localList = paramINSURL.getEndpointInfo();
/* 225 */     String str = paramINSURL.getKeyString();
/*     */ 
/* 227 */     if (str == null) {
/* 228 */       return null;
/*     */     }
/*     */ 
/* 231 */     ObjectKey localObjectKey = this.orb.getObjectKeyFactory().create(str.getBytes());
/*     */ 
/* 233 */     IORTemplate localIORTemplate = IORFactories.makeIORTemplate(localObjectKey.getTemplate());
/* 234 */     Iterator localIterator = localList.iterator();
/* 235 */     while (localIterator.hasNext()) {
/* 236 */       localObject1 = (IIOPEndpointInfo)localIterator.next();
/*     */ 
/* 238 */       localObject2 = IIOPFactories.makeIIOPAddress(this.orb, ((IIOPEndpointInfo)localObject1).getHost(), ((IIOPEndpointInfo)localObject1).getPort());
/*     */ 
/* 240 */       localObject3 = GIOPVersion.getInstance((byte)((IIOPEndpointInfo)localObject1).getMajor(), (byte)((IIOPEndpointInfo)localObject1).getMinor());
/*     */ 
/* 242 */       localObject4 = null;
/* 243 */       if (((GIOPVersion)localObject3).equals(GIOPVersion.V1_0)) {
/* 244 */         localObject4 = IIOPFactories.makeIIOPProfileTemplate(this.orb, (GIOPVersion)localObject3, (IIOPAddress)localObject2);
/*     */ 
/* 246 */         localArrayList.add(localObject4);
/*     */       }
/* 248 */       else if (localHashMap.get(localObject3) == null) {
/* 249 */         localObject4 = IIOPFactories.makeIIOPProfileTemplate(this.orb, (GIOPVersion)localObject3, (IIOPAddress)localObject2);
/*     */ 
/* 251 */         localHashMap.put(localObject3, localObject4);
/*     */       } else {
/* 253 */         localObject4 = (IIOPProfileTemplate)localHashMap.get(localObject3);
/* 254 */         localObject5 = IIOPFactories.makeAlternateIIOPAddressComponent((IIOPAddress)localObject2);
/*     */ 
/* 256 */         ((IIOPProfileTemplate)localObject4).add(localObject5);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 261 */     java.lang.Object localObject1 = this.orb.getORBData().getGIOPVersion();
/* 262 */     java.lang.Object localObject2 = (IIOPProfileTemplate)localHashMap.get(localObject1);
/* 263 */     if (localObject2 != null) {
/* 264 */       localIORTemplate.add(localObject2);
/* 265 */       localHashMap.remove(localObject1);
/*     */     }
/*     */ 
/* 269 */     java.lang.Object localObject3 = new Comparator() {
/*     */       public int compare(java.lang.Object paramAnonymousObject1, java.lang.Object paramAnonymousObject2) {
/* 271 */         GIOPVersion localGIOPVersion1 = (GIOPVersion)paramAnonymousObject1;
/* 272 */         GIOPVersion localGIOPVersion2 = (GIOPVersion)paramAnonymousObject2;
/* 273 */         return localGIOPVersion1.equals(localGIOPVersion2) ? 0 : localGIOPVersion1.lessThan(localGIOPVersion2) ? 1 : -1;
/*     */       }
/*     */     };
/* 278 */     java.lang.Object localObject4 = new ArrayList(localHashMap.keySet());
/* 279 */     Collections.sort((List)localObject4, (Comparator)localObject3);
/*     */ 
/* 282 */     java.lang.Object localObject5 = ((List)localObject4).iterator();
/* 283 */     while (((Iterator)localObject5).hasNext()) {
/* 284 */       localObject6 = (IIOPProfileTemplate)localHashMap.get(((Iterator)localObject5).next());
/* 285 */       localIORTemplate.add(localObject6);
/*     */     }
/*     */ 
/* 289 */     localIORTemplate.addAll(localArrayList);
/*     */ 
/* 291 */     java.lang.Object localObject6 = localIORTemplate.makeIOR(this.orb, "", localObjectKey.getId());
/* 292 */     return ORBUtility.makeObjectReference((IOR)localObject6);
/*     */   }
/*     */ 
/*     */   private NamingContextExt getDefaultRootNamingContext()
/*     */   {
/* 306 */     synchronized (this.rootContextCacheLock) {
/* 307 */       if (this.rootNamingContextExt == null) {
/*     */         try {
/* 309 */           this.rootNamingContextExt = NamingContextExtHelper.narrow(this.orb.getLocalResolver().resolve("NameService"));
/*     */         }
/*     */         catch (Exception localException)
/*     */         {
/* 313 */           this.rootNamingContextExt = null;
/*     */         }
/*     */       }
/*     */     }
/* 317 */     return this.rootNamingContextExt;
/*     */   }
/*     */ 
/*     */   private void clearRootNamingContextCache()
/*     */   {
/* 325 */     synchronized (this.rootContextCacheLock) {
/* 326 */       this.rootNamingContextExt = null;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.resolver.INSURLOperationImpl
 * JD-Core Version:    0.6.2
 */