/*     */ package com.sun.org.apache.xml.internal.security;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.security.algorithms.JCEMapper;
/*     */ import com.sun.org.apache.xml.internal.security.algorithms.JCEMapper.Algorithm;
/*     */ import com.sun.org.apache.xml.internal.security.algorithms.SignatureAlgorithm;
/*     */ import com.sun.org.apache.xml.internal.security.c14n.Canonicalizer;
/*     */ import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
/*     */ import com.sun.org.apache.xml.internal.security.keys.keyresolver.KeyResolver;
/*     */ import com.sun.org.apache.xml.internal.security.transforms.Transform;
/*     */ import com.sun.org.apache.xml.internal.security.utils.ElementProxy;
/*     */ import com.sun.org.apache.xml.internal.security.utils.I18n;
/*     */ import com.sun.org.apache.xml.internal.security.utils.XMLUtils;
/*     */ import com.sun.org.apache.xml.internal.security.utils.resolver.ResourceResolver;
/*     */ import java.io.InputStream;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import org.w3c.dom.Attr;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ 
/*     */ public class Init
/*     */ {
/*     */   public static final String CONF_NS = "http://www.xmlsecurity.org/NS/#configuration";
/*  65 */   private static Logger log = Logger.getLogger(Init.class.getName());
/*     */ 
/*  69 */   private static boolean alreadyInitialized = false;
/*     */ 
/*     */   public static final synchronized boolean isInitialized()
/*     */   {
/*  76 */     return alreadyInitialized;
/*     */   }
/*     */ 
/*     */   public static synchronized void init()
/*     */   {
/*  84 */     if (alreadyInitialized) {
/*  85 */       return;
/*     */     }
/*     */ 
/*  88 */     InputStream localInputStream = (InputStream)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public InputStream run()
/*     */       {
/*  92 */         String str = System.getProperty("com.sun.org.apache.xml.internal.security.resource.config");
/*     */ 
/*  94 */         if (str == null) {
/*  95 */           return null;
/*     */         }
/*  97 */         return getClass().getResourceAsStream(str);
/*     */       }
/*     */     });
/* 100 */     if (localInputStream == null)
/* 101 */       dynamicInit();
/*     */     else {
/* 103 */       fileInit(localInputStream);
/*     */     }
/*     */ 
/* 106 */     alreadyInitialized = true;
/*     */   }
/*     */ 
/*     */   private static void dynamicInit()
/*     */   {
/* 118 */     I18n.init("en", "US");
/*     */ 
/* 120 */     if (log.isLoggable(Level.FINE))
/* 121 */       log.log(Level.FINE, "Registering default algorithms");
/*     */     try
/*     */     {
/* 124 */       AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */       {
/*     */         public Void run()
/*     */           throws XMLSecurityException
/*     */         {
/* 129 */           ElementProxy.registerDefaultPrefixes();
/*     */ 
/* 134 */           Transform.registerDefaultAlgorithms();
/*     */ 
/* 139 */           SignatureAlgorithm.registerDefaultAlgorithms();
/*     */ 
/* 144 */           JCEMapper.registerDefaultAlgorithms();
/*     */ 
/* 149 */           Canonicalizer.registerDefaultAlgorithms();
/*     */ 
/* 154 */           ResourceResolver.registerDefaultResolvers();
/*     */ 
/* 159 */           KeyResolver.registerDefaultResolvers();
/*     */ 
/* 161 */           return null;
/*     */         } } );
/*     */     }
/*     */     catch (PrivilegedActionException localPrivilegedActionException) {
/* 165 */       XMLSecurityException localXMLSecurityException = (XMLSecurityException)localPrivilegedActionException.getException();
/* 166 */       log.log(Level.SEVERE, localXMLSecurityException.getMessage(), localXMLSecurityException);
/* 167 */       localXMLSecurityException.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   private static void fileInit(InputStream paramInputStream)
/*     */   {
/*     */     try
/*     */     {
/* 177 */       DocumentBuilderFactory localDocumentBuilderFactory = DocumentBuilderFactory.newInstance();
/* 178 */       localDocumentBuilderFactory.setFeature("http://javax.xml.XMLConstants/feature/secure-processing", Boolean.TRUE.booleanValue());
/*     */ 
/* 180 */       localDocumentBuilderFactory.setNamespaceAware(true);
/* 181 */       localDocumentBuilderFactory.setValidating(false);
/*     */ 
/* 183 */       DocumentBuilder localDocumentBuilder = localDocumentBuilderFactory.newDocumentBuilder();
/* 184 */       Document localDocument = localDocumentBuilder.parse(paramInputStream);
/* 185 */       Node localNode1 = localDocument.getFirstChild();
/* 186 */       while ((localNode1 != null) && 
/* 187 */         (!"Configuration".equals(localNode1.getLocalName()))) {
/* 186 */         localNode1 = localNode1.getNextSibling();
/*     */       }
/*     */ 
/* 191 */       if (localNode1 == null) {
/* 192 */         log.log(Level.SEVERE, "Error in reading configuration file - Configuration element not found");
/* 193 */         return;
/*     */       }
/* 195 */       for (Node localNode2 = localNode1.getFirstChild(); localNode2 != null; localNode2 = localNode2.getNextSibling())
/* 196 */         if (1 == localNode2.getNodeType())
/*     */         {
/* 199 */           String str1 = localNode2.getLocalName();
/*     */           Object localObject1;
/*     */           Object localObject2;
/*     */           Object localObject3;
/* 200 */           if (str1.equals("ResourceBundles")) {
/* 201 */             localObject1 = (Element)localNode2;
/*     */ 
/* 203 */             Attr localAttr = ((Element)localObject1).getAttributeNode("defaultLanguageCode");
/* 204 */             localObject2 = ((Element)localObject1).getAttributeNode("defaultCountryCode");
/* 205 */             localObject3 = localAttr == null ? null : localAttr.getNodeValue();
/*     */ 
/* 207 */             String str4 = localObject2 == null ? null : ((Attr)localObject2).getNodeValue();
/*     */ 
/* 209 */             I18n.init((String)localObject3, str4);
/*     */           }
/*     */           int i;
/*     */           Object[] arrayOfObject;
/* 212 */           if (str1.equals("CanonicalizationMethods")) {
/* 213 */             localObject1 = XMLUtils.selectNodes(localNode2.getFirstChild(), "http://www.xmlsecurity.org/NS/#configuration", "CanonicalizationMethod");
/*     */ 
/* 216 */             for (i = 0; i < localObject1.length; i++) {
/* 217 */               localObject2 = localObject1[i].getAttributeNS(null, "URI");
/* 218 */               localObject3 = localObject1[i].getAttributeNS(null, "JAVACLASS");
/*     */               try
/*     */               {
/* 221 */                 Canonicalizer.register((String)localObject2, (String)localObject3);
/* 222 */                 if (log.isLoggable(Level.FINE))
/* 223 */                   log.log(Level.FINE, "Canonicalizer.register(" + (String)localObject2 + ", " + (String)localObject3 + ")");
/*     */               }
/*     */               catch (ClassNotFoundException localClassNotFoundException1) {
/* 226 */                 arrayOfObject = new Object[] { localObject2, localObject3 };
/* 227 */                 log.log(Level.SEVERE, I18n.translate("algorithm.classDoesNotExist", arrayOfObject));
/*     */               }
/*     */             }
/*     */           }
/*     */ 
/* 232 */           if (str1.equals("TransformAlgorithms")) {
/* 233 */             localObject1 = XMLUtils.selectNodes(localNode2.getFirstChild(), "http://www.xmlsecurity.org/NS/#configuration", "TransformAlgorithm");
/*     */ 
/* 236 */             for (i = 0; i < localObject1.length; i++) {
/* 237 */               localObject2 = localObject1[i].getAttributeNS(null, "URI");
/* 238 */               localObject3 = localObject1[i].getAttributeNS(null, "JAVACLASS");
/*     */               try
/*     */               {
/* 241 */                 Transform.register((String)localObject2, (String)localObject3);
/* 242 */                 if (log.isLoggable(Level.FINE))
/* 243 */                   log.log(Level.FINE, "Transform.register(" + (String)localObject2 + ", " + (String)localObject3 + ")");
/*     */               }
/*     */               catch (ClassNotFoundException localClassNotFoundException2) {
/* 246 */                 arrayOfObject = new Object[] { localObject2, localObject3 };
/*     */ 
/* 248 */                 log.log(Level.SEVERE, I18n.translate("algorithm.classDoesNotExist", arrayOfObject));
/*     */               } catch (NoClassDefFoundError localNoClassDefFoundError) {
/* 250 */                 log.log(Level.WARNING, "Not able to found dependencies for algorithm, I'll keep working.");
/*     */               }
/*     */             }
/*     */           }
/*     */ 
/* 255 */           if ("JCEAlgorithmMappings".equals(str1)) {
/* 256 */             localObject1 = ((Element)localNode2).getElementsByTagName("Algorithms").item(0);
/* 257 */             if (localObject1 != null) {
/* 258 */               Element[] arrayOfElement = XMLUtils.selectNodes(((Node)localObject1).getFirstChild(), "http://www.xmlsecurity.org/NS/#configuration", "Algorithm");
/*     */ 
/* 260 */               for (int m = 0; m < arrayOfElement.length; m++) {
/* 261 */                 localObject3 = arrayOfElement[m];
/* 262 */                 String str5 = ((Element)localObject3).getAttribute("URI");
/* 263 */                 JCEMapper.register(str5, new JCEMapper.Algorithm((Element)localObject3));
/*     */               }
/*     */             }
/*     */           }
/*     */           int j;
/*     */           String str2;
/* 268 */           if (str1.equals("SignatureAlgorithms")) {
/* 269 */             localObject1 = XMLUtils.selectNodes(localNode2.getFirstChild(), "http://www.xmlsecurity.org/NS/#configuration", "SignatureAlgorithm");
/*     */ 
/* 272 */             for (j = 0; j < localObject1.length; j++) {
/* 273 */               str2 = localObject1[j].getAttributeNS(null, "URI");
/* 274 */               localObject3 = localObject1[j].getAttributeNS(null, "JAVACLASS");
/*     */               try
/*     */               {
/* 280 */                 SignatureAlgorithm.register(str2, (String)localObject3);
/* 281 */                 if (log.isLoggable(Level.FINE))
/* 282 */                   log.log(Level.FINE, "SignatureAlgorithm.register(" + str2 + ", " + (String)localObject3 + ")");
/*     */               }
/*     */               catch (ClassNotFoundException localClassNotFoundException3)
/*     */               {
/* 286 */                 arrayOfObject = new Object[] { str2, localObject3 };
/*     */ 
/* 288 */                 log.log(Level.SEVERE, I18n.translate("algorithm.classDoesNotExist", arrayOfObject));
/*     */               }
/*     */             }
/*     */           }
/*     */ 
/* 293 */           if (str1.equals("ResourceResolvers")) {
/* 294 */             localObject1 = XMLUtils.selectNodes(localNode2.getFirstChild(), "http://www.xmlsecurity.org/NS/#configuration", "Resolver");
/*     */ 
/* 297 */             for (j = 0; j < localObject1.length; j++) {
/* 298 */               str2 = localObject1[j].getAttributeNS(null, "JAVACLASS");
/*     */ 
/* 300 */               localObject3 = localObject1[j].getAttributeNS(null, "DESCRIPTION");
/*     */ 
/* 303 */               if ((localObject3 != null) && (((String)localObject3).length() > 0)) {
/* 304 */                 if (log.isLoggable(Level.FINE)) {
/* 305 */                   log.log(Level.FINE, "Register Resolver: " + str2 + ": " + (String)localObject3);
/*     */                 }
/*     */ 
/*     */               }
/* 309 */               else if (log.isLoggable(Level.FINE)) {
/* 310 */                 log.log(Level.FINE, "Register Resolver: " + str2 + ": For unknown purposes");
/*     */               }
/*     */ 
/*     */               try
/*     */               {
/* 315 */                 ResourceResolver.register(str2);
/*     */               } catch (Throwable localThrowable) {
/* 317 */                 log.log(Level.WARNING, "Cannot register:" + str2 + " perhaps some needed jars are not installed", localThrowable);
/*     */               }
/*     */ 
/*     */             }
/*     */ 
/*     */           }
/*     */ 
/* 326 */           if (str1.equals("KeyResolver")) {
/* 327 */             localObject1 = XMLUtils.selectNodes(localNode2.getFirstChild(), "http://www.xmlsecurity.org/NS/#configuration", "Resolver");
/*     */ 
/* 329 */             ArrayList localArrayList = new ArrayList(localObject1.length);
/* 330 */             for (int n = 0; n < localObject1.length; n++) {
/* 331 */               localObject3 = localObject1[n].getAttributeNS(null, "JAVACLASS");
/*     */ 
/* 333 */               String str6 = localObject1[n].getAttributeNS(null, "DESCRIPTION");
/*     */ 
/* 336 */               if ((str6 != null) && (str6.length() > 0)) {
/* 337 */                 if (log.isLoggable(Level.FINE)) {
/* 338 */                   log.log(Level.FINE, "Register Resolver: " + (String)localObject3 + ": " + str6);
/*     */                 }
/*     */ 
/*     */               }
/* 342 */               else if (log.isLoggable(Level.FINE)) {
/* 343 */                 log.log(Level.FINE, "Register Resolver: " + (String)localObject3 + ": For unknown purposes");
/*     */               }
/*     */ 
/* 347 */               localArrayList.add(localObject3);
/*     */             }
/* 349 */             KeyResolver.registerClassNames(localArrayList);
/*     */           }
/*     */ 
/* 353 */           if (str1.equals("PrefixMappings")) {
/* 354 */             if (log.isLoggable(Level.FINE)) {
/* 355 */               log.log(Level.FINE, "Now I try to bind prefixes:");
/*     */             }
/*     */ 
/* 358 */             localObject1 = XMLUtils.selectNodes(localNode2.getFirstChild(), "http://www.xmlsecurity.org/NS/#configuration", "PrefixMapping");
/*     */ 
/* 361 */             for (int k = 0; k < localObject1.length; k++) {
/* 362 */               String str3 = localObject1[k].getAttributeNS(null, "namespace");
/* 363 */               localObject3 = localObject1[k].getAttributeNS(null, "prefix");
/* 364 */               if (log.isLoggable(Level.FINE)) {
/* 365 */                 log.log(Level.FINE, "Now I try to bind " + (String)localObject3 + " to " + str3);
/*     */               }
/* 367 */               ElementProxy.setDefaultPrefix(str3, (String)localObject3);
/*     */             }
/*     */           }
/*     */         }
/*     */     } catch (Exception localException) {
/* 372 */       log.log(Level.SEVERE, "Bad: ", localException);
/* 373 */       localException.printStackTrace();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.Init
 * JD-Core Version:    0.6.2
 */