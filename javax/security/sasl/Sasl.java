/*     */ package javax.security.sasl;
/*     */ 
/*     */ import java.security.Provider;
/*     */ import java.security.Security;
/*     */ import java.util.Collections;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.security.auth.callback.CallbackHandler;
/*     */ 
/*     */ public class Sasl
/*     */ {
/*     */   public static final String QOP = "javax.security.sasl.qop";
/*     */   public static final String STRENGTH = "javax.security.sasl.strength";
/*     */   public static final String SERVER_AUTH = "javax.security.sasl.server.authentication";
/*     */   public static final String MAX_BUFFER = "javax.security.sasl.maxbuffer";
/*     */   public static final String RAW_SEND_SIZE = "javax.security.sasl.rawsendsize";
/*     */   public static final String REUSE = "javax.security.sasl.reuse";
/*     */   public static final String POLICY_NOPLAINTEXT = "javax.security.sasl.policy.noplaintext";
/*     */   public static final String POLICY_NOACTIVE = "javax.security.sasl.policy.noactive";
/*     */   public static final String POLICY_NODICTIONARY = "javax.security.sasl.policy.nodictionary";
/*     */   public static final String POLICY_NOANONYMOUS = "javax.security.sasl.policy.noanonymous";
/*     */   public static final String POLICY_FORWARD_SECRECY = "javax.security.sasl.policy.forward";
/*     */   public static final String POLICY_PASS_CREDENTIALS = "javax.security.sasl.policy.credentials";
/*     */   public static final String CREDENTIALS = "javax.security.sasl.credentials";
/*     */ 
/*     */   public static SaslClient createSaslClient(String[] paramArrayOfString, String paramString1, String paramString2, String paramString3, Map<String, ?> paramMap, CallbackHandler paramCallbackHandler)
/*     */     throws SaslException
/*     */   {
/* 349 */     SaslClient localSaslClient = null;
/*     */ 
/* 354 */     for (int i = 0; i < paramArrayOfString.length; i++)
/*     */     {
/*     */       String str2;
/* 355 */       if ((str2 = paramArrayOfString[i]) == null) {
/* 356 */         throw new NullPointerException("Mechanism name cannot be null");
/*     */       }
/* 358 */       if (str2.length() != 0)
/*     */       {
/* 361 */         String str3 = "SaslClientFactory." + str2;
/* 362 */         Provider[] arrayOfProvider = Security.getProviders(str3);
/* 363 */         for (int j = 0; (arrayOfProvider != null) && (j < arrayOfProvider.length); j++) {
/* 364 */           String str1 = arrayOfProvider[j].getProperty(str3);
/* 365 */           if (str1 != null)
/*     */           {
/* 370 */             SaslClientFactory localSaslClientFactory = (SaslClientFactory)loadFactory(arrayOfProvider[j], str1);
/* 371 */             if (localSaslClientFactory != null) {
/* 372 */               localSaslClient = localSaslClientFactory.createSaslClient(new String[] { paramArrayOfString[i] }, paramString1, paramString2, paramString3, paramMap, paramCallbackHandler);
/*     */ 
/* 375 */               if (localSaslClient != null)
/* 376 */                 return localSaslClient;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 382 */     return null;
/*     */   }
/*     */ 
/*     */   private static Object loadFactory(Provider paramProvider, String paramString)
/*     */     throws SaslException
/*     */   {
/*     */     try
/*     */     {
/* 397 */       ClassLoader localClassLoader = paramProvider.getClass().getClassLoader();
/*     */ 
/* 399 */       Class localClass = Class.forName(paramString, true, localClassLoader);
/* 400 */       return localClass.newInstance();
/*     */     } catch (ClassNotFoundException localClassNotFoundException) {
/* 402 */       throw new SaslException("Cannot load class " + paramString, localClassNotFoundException);
/*     */     } catch (InstantiationException localInstantiationException) {
/* 404 */       throw new SaslException("Cannot instantiate class " + paramString, localInstantiationException);
/*     */     } catch (IllegalAccessException localIllegalAccessException) {
/* 406 */       throw new SaslException("Cannot access class " + paramString, localIllegalAccessException);
/*     */     } catch (SecurityException localSecurityException) {
/* 408 */       throw new SaslException("Cannot access class " + paramString, localSecurityException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static SaslServer createSaslServer(String paramString1, String paramString2, String paramString3, Map<String, ?> paramMap, CallbackHandler paramCallbackHandler)
/*     */     throws SaslException
/*     */   {
/* 489 */     SaslServer localSaslServer = null;
/*     */ 
/* 493 */     if (paramString1 == null)
/* 494 */       throw new NullPointerException("Mechanism name cannot be null");
/* 495 */     if (paramString1.length() == 0) {
/* 496 */       return null;
/*     */     }
/*     */ 
/* 499 */     String str2 = "SaslServerFactory." + paramString1;
/* 500 */     Provider[] arrayOfProvider = Security.getProviders(str2);
/* 501 */     for (int i = 0; (arrayOfProvider != null) && (i < arrayOfProvider.length); i++) {
/* 502 */       String str1 = arrayOfProvider[i].getProperty(str2);
/* 503 */       if (str1 == null) {
/* 504 */         throw new SaslException("Provider does not support " + str2);
/*     */       }
/*     */ 
/* 507 */       SaslServerFactory localSaslServerFactory = (SaslServerFactory)loadFactory(arrayOfProvider[i], str1);
/* 508 */       if (localSaslServerFactory != null) {
/* 509 */         localSaslServer = localSaslServerFactory.createSaslServer(paramString1, paramString2, paramString3, paramMap, paramCallbackHandler);
/*     */ 
/* 511 */         if (localSaslServer != null) {
/* 512 */           return localSaslServer;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 517 */     return null;
/*     */   }
/*     */ 
/*     */   public static Enumeration<SaslClientFactory> getSaslClientFactories()
/*     */   {
/* 529 */     Set localSet = getFactories("SaslClientFactory");
/* 530 */     Iterator localIterator = localSet.iterator();
/* 531 */     return new Enumeration() {
/*     */       public boolean hasMoreElements() {
/* 533 */         return this.val$iter.hasNext();
/*     */       }
/*     */       public SaslClientFactory nextElement() {
/* 536 */         return (SaslClientFactory)this.val$iter.next();
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   public static Enumeration<SaslServerFactory> getSaslServerFactories()
/*     */   {
/* 550 */     Set localSet = getFactories("SaslServerFactory");
/* 551 */     Iterator localIterator = localSet.iterator();
/* 552 */     return new Enumeration() {
/*     */       public boolean hasMoreElements() {
/* 554 */         return this.val$iter.hasNext();
/*     */       }
/*     */       public SaslServerFactory nextElement() {
/* 557 */         return (SaslServerFactory)this.val$iter.next();
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   private static Set<Object> getFactories(String paramString) {
/* 563 */     HashSet localHashSet1 = new HashSet();
/*     */ 
/* 565 */     if ((paramString == null) || (paramString.length() == 0) || (paramString.endsWith(".")))
/*     */     {
/* 567 */       return localHashSet1;
/*     */     }
/*     */ 
/* 571 */     Provider[] arrayOfProvider = Security.getProviders();
/* 572 */     HashSet localHashSet2 = new HashSet();
/*     */     Enumeration localEnumeration;
/* 575 */     for (int i = 0; i < arrayOfProvider.length; i++) {
/* 576 */       localHashSet2.clear();
/*     */ 
/* 579 */       for (localEnumeration = arrayOfProvider[i].keys(); localEnumeration.hasMoreElements(); ) {
/* 580 */         String str1 = (String)localEnumeration.nextElement();
/* 581 */         if (str1.startsWith(paramString))
/*     */         {
/* 588 */           if (str1.indexOf(" ") < 0) {
/* 589 */             String str2 = arrayOfProvider[i].getProperty(str1);
/* 590 */             if (!localHashSet2.contains(str2)) {
/* 591 */               localHashSet2.add(str2);
/*     */               try {
/* 593 */                 Object localObject = loadFactory(arrayOfProvider[i], str2);
/* 594 */                 if (localObject != null)
/* 595 */                   localHashSet1.add(localObject);
/*     */               }
/*     */               catch (Exception localException) {
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 604 */     return Collections.unmodifiableSet(localHashSet1);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.security.sasl.Sasl
 * JD-Core Version:    0.6.2
 */