/*     */ package java.security;
/*     */ 
/*     */ import java.security.spec.AlgorithmParameterSpec;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import sun.security.jca.GetInstance;
/*     */ import sun.security.jca.GetInstance.Instance;
/*     */ import sun.security.jca.JCAUtil;
/*     */ 
/*     */ public abstract class KeyPairGenerator extends KeyPairGeneratorSpi
/*     */ {
/*     */   private final String algorithm;
/*     */   Provider provider;
/*     */ 
/*     */   protected KeyPairGenerator(String paramString)
/*     */   {
/* 145 */     this.algorithm = paramString;
/*     */   }
/*     */ 
/*     */   public String getAlgorithm()
/*     */   {
/* 158 */     return this.algorithm;
/*     */   }
/*     */ 
/*     */   private static KeyPairGenerator getInstance(GetInstance.Instance paramInstance, String paramString)
/*     */   {
/*     */     Object localObject;
/* 164 */     if ((paramInstance.impl instanceof KeyPairGenerator)) {
/* 165 */       localObject = (KeyPairGenerator)paramInstance.impl;
/*     */     } else {
/* 167 */       KeyPairGeneratorSpi localKeyPairGeneratorSpi = (KeyPairGeneratorSpi)paramInstance.impl;
/* 168 */       localObject = new Delegate(localKeyPairGeneratorSpi, paramString);
/*     */     }
/* 170 */     ((KeyPairGenerator)localObject).provider = paramInstance.provider;
/* 171 */     return localObject;
/*     */   }
/*     */ 
/*     */   public static KeyPairGenerator getInstance(String paramString)
/*     */     throws NoSuchAlgorithmException
/*     */   {
/* 203 */     List localList = GetInstance.getServices("KeyPairGenerator", paramString);
/*     */ 
/* 205 */     Iterator localIterator = localList.iterator();
/* 206 */     if (!localIterator.hasNext()) {
/* 207 */       throw new NoSuchAlgorithmException(paramString + " KeyPairGenerator not available");
/*     */     }
/*     */ 
/* 211 */     Object localObject = null;
/*     */     do {
/* 213 */       Provider.Service localService = (Provider.Service)localIterator.next();
/*     */       try {
/* 215 */         GetInstance.Instance localInstance = GetInstance.getInstance(localService, KeyPairGeneratorSpi.class);
/*     */ 
/* 217 */         if ((localInstance.impl instanceof KeyPairGenerator)) {
/* 218 */           return getInstance(localInstance, paramString);
/*     */         }
/* 220 */         return new Delegate(localInstance, localIterator, paramString);
/*     */       }
/*     */       catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
/* 223 */         if (localObject == null)
/* 224 */           localObject = localNoSuchAlgorithmException;
/*     */       }
/*     */     }
/* 227 */     while (localIterator.hasNext());
/* 228 */     throw localObject;
/*     */   }
/*     */ 
/*     */   public static KeyPairGenerator getInstance(String paramString1, String paramString2)
/*     */     throws NoSuchAlgorithmException, NoSuchProviderException
/*     */   {
/* 268 */     GetInstance.Instance localInstance = GetInstance.getInstance("KeyPairGenerator", KeyPairGeneratorSpi.class, paramString1, paramString2);
/*     */ 
/* 270 */     return getInstance(localInstance, paramString1);
/*     */   }
/*     */ 
/*     */   public static KeyPairGenerator getInstance(String paramString, Provider paramProvider)
/*     */     throws NoSuchAlgorithmException
/*     */   {
/* 304 */     GetInstance.Instance localInstance = GetInstance.getInstance("KeyPairGenerator", KeyPairGeneratorSpi.class, paramString, paramProvider);
/*     */ 
/* 306 */     return getInstance(localInstance, paramString);
/*     */   }
/*     */ 
/*     */   public final Provider getProvider()
/*     */   {
/* 315 */     disableFailover();
/* 316 */     return this.provider;
/*     */   }
/*     */ 
/*     */   void disableFailover()
/*     */   {
/*     */   }
/*     */ 
/*     */   public void initialize(int paramInt)
/*     */   {
/* 340 */     initialize(paramInt, JCAUtil.getSecureRandom());
/*     */   }
/*     */ 
/*     */   public void initialize(int paramInt, SecureRandom paramSecureRandom)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void initialize(AlgorithmParameterSpec paramAlgorithmParameterSpec)
/*     */     throws InvalidAlgorithmParameterException
/*     */   {
/* 400 */     initialize(paramAlgorithmParameterSpec, JCAUtil.getSecureRandom());
/*     */   }
/*     */ 
/*     */   public void initialize(AlgorithmParameterSpec paramAlgorithmParameterSpec, SecureRandom paramSecureRandom)
/*     */     throws InvalidAlgorithmParameterException
/*     */   {
/*     */   }
/*     */ 
/*     */   public final KeyPair genKeyPair()
/*     */   {
/* 459 */     return generateKeyPair();
/*     */   }
/*     */ 
/*     */   public KeyPair generateKeyPair()
/*     */   {
/* 490 */     return null;
/*     */   }
/*     */ 
/*     */   private static final class Delegate extends KeyPairGenerator
/*     */   {
/*     */     private volatile KeyPairGeneratorSpi spi;
/* 535 */     private final Object lock = new Object();
/*     */     private Iterator<Provider.Service> serviceIterator;
/*     */     private static final int I_NONE = 1;
/*     */     private static final int I_SIZE = 2;
/*     */     private static final int I_PARAMS = 3;
/*     */     private int initType;
/*     */     private int initKeySize;
/*     */     private AlgorithmParameterSpec initParams;
/*     */     private SecureRandom initRandom;
/*     */ 
/*     */     Delegate(KeyPairGeneratorSpi paramKeyPairGeneratorSpi, String paramString)
/*     */     {
/* 550 */       super();
/* 551 */       this.spi = paramKeyPairGeneratorSpi;
/*     */     }
/*     */ 
/*     */     Delegate(GetInstance.Instance paramInstance, Iterator<Provider.Service> paramIterator, String paramString)
/*     */     {
/* 556 */       super();
/* 557 */       this.spi = ((KeyPairGeneratorSpi)paramInstance.impl);
/* 558 */       this.provider = paramInstance.provider;
/* 559 */       this.serviceIterator = paramIterator;
/* 560 */       this.initType = 1;
/*     */     }
/*     */ 
/*     */     private KeyPairGeneratorSpi nextSpi(KeyPairGeneratorSpi paramKeyPairGeneratorSpi, boolean paramBoolean)
/*     */     {
/* 571 */       synchronized (this.lock)
/*     */       {
/* 574 */         if ((paramKeyPairGeneratorSpi != null) && (paramKeyPairGeneratorSpi != this.spi)) {
/* 575 */           return this.spi;
/*     */         }
/* 577 */         if (this.serviceIterator == null) {
/* 578 */           return null;
/*     */         }
/* 580 */         while (this.serviceIterator.hasNext()) {
/* 581 */           Provider.Service localService = (Provider.Service)this.serviceIterator.next();
/*     */           try {
/* 583 */             Object localObject1 = localService.newInstance(null);
/*     */ 
/* 585 */             if (((localObject1 instanceof KeyPairGeneratorSpi)) && 
/* 588 */               (!(localObject1 instanceof KeyPairGenerator)))
/*     */             {
/* 591 */               KeyPairGeneratorSpi localKeyPairGeneratorSpi = (KeyPairGeneratorSpi)localObject1;
/* 592 */               if (paramBoolean) {
/* 593 */                 if (this.initType == 2)
/* 594 */                   localKeyPairGeneratorSpi.initialize(this.initKeySize, this.initRandom);
/* 595 */                 else if (this.initType == 3)
/* 596 */                   localKeyPairGeneratorSpi.initialize(this.initParams, this.initRandom);
/* 597 */                 else if (this.initType != 1) {
/* 598 */                   throw new AssertionError("KeyPairGenerator initType: " + this.initType);
/*     */                 }
/*     */               }
/*     */ 
/* 602 */               this.provider = localService.getProvider();
/* 603 */               this.spi = localKeyPairGeneratorSpi;
/* 604 */               return localKeyPairGeneratorSpi;
/*     */             }
/*     */           } catch (Exception localException) {
/*     */           }
/*     */         }
/* 609 */         disableFailover();
/* 610 */         return null;
/*     */       }
/*     */     }
/*     */ 
/*     */     void disableFailover() {
/* 615 */       this.serviceIterator = null;
/* 616 */       this.initType = 0;
/* 617 */       this.initParams = null;
/* 618 */       this.initRandom = null;
/*     */     }
/*     */ 
/*     */     public void initialize(int paramInt, SecureRandom paramSecureRandom)
/*     */     {
/* 623 */       if (this.serviceIterator == null) {
/* 624 */         this.spi.initialize(paramInt, paramSecureRandom);
/* 625 */         return;
/*     */       }
/* 627 */       Object localObject = null;
/* 628 */       KeyPairGeneratorSpi localKeyPairGeneratorSpi = this.spi;
/*     */       do
/*     */         try {
/* 631 */           localKeyPairGeneratorSpi.initialize(paramInt, paramSecureRandom);
/* 632 */           this.initType = 2;
/* 633 */           this.initKeySize = paramInt;
/* 634 */           this.initParams = null;
/* 635 */           this.initRandom = paramSecureRandom;
/* 636 */           return;
/*     */         } catch (RuntimeException localRuntimeException) {
/* 638 */           if (localObject == null) {
/* 639 */             localObject = localRuntimeException;
/*     */           }
/* 641 */           localKeyPairGeneratorSpi = nextSpi(localKeyPairGeneratorSpi, false);
/*     */         }
/* 643 */       while (localKeyPairGeneratorSpi != null);
/* 644 */       throw localObject;
/*     */     }
/*     */ 
/*     */     public void initialize(AlgorithmParameterSpec paramAlgorithmParameterSpec, SecureRandom paramSecureRandom)
/*     */       throws InvalidAlgorithmParameterException
/*     */     {
/* 650 */       if (this.serviceIterator == null) {
/* 651 */         this.spi.initialize(paramAlgorithmParameterSpec, paramSecureRandom);
/* 652 */         return;
/*     */       }
/* 654 */       Object localObject = null;
/* 655 */       KeyPairGeneratorSpi localKeyPairGeneratorSpi = this.spi;
/*     */       do
/*     */         try {
/* 658 */           localKeyPairGeneratorSpi.initialize(paramAlgorithmParameterSpec, paramSecureRandom);
/* 659 */           this.initType = 3;
/* 660 */           this.initKeySize = 0;
/* 661 */           this.initParams = paramAlgorithmParameterSpec;
/* 662 */           this.initRandom = paramSecureRandom;
/* 663 */           return;
/*     */         } catch (Exception localException) {
/* 665 */           if (localObject == null) {
/* 666 */             localObject = localException;
/*     */           }
/* 668 */           localKeyPairGeneratorSpi = nextSpi(localKeyPairGeneratorSpi, false);
/*     */         }
/* 670 */       while (localKeyPairGeneratorSpi != null);
/* 671 */       if ((localObject instanceof RuntimeException)) {
/* 672 */         throw ((RuntimeException)localObject);
/*     */       }
/*     */ 
/* 675 */       throw ((InvalidAlgorithmParameterException)localObject);
/*     */     }
/*     */ 
/*     */     public KeyPair generateKeyPair()
/*     */     {
/* 680 */       if (this.serviceIterator == null) {
/* 681 */         return this.spi.generateKeyPair();
/*     */       }
/* 683 */       Object localObject = null;
/* 684 */       KeyPairGeneratorSpi localKeyPairGeneratorSpi = this.spi;
/*     */       do
/*     */         try {
/* 687 */           return localKeyPairGeneratorSpi.generateKeyPair();
/*     */         } catch (RuntimeException localRuntimeException) {
/* 689 */           if (localObject == null) {
/* 690 */             localObject = localRuntimeException;
/*     */           }
/* 692 */           localKeyPairGeneratorSpi = nextSpi(localKeyPairGeneratorSpi, true);
/*     */         }
/* 694 */       while (localKeyPairGeneratorSpi != null);
/* 695 */       throw localObject;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.KeyPairGenerator
 * JD-Core Version:    0.6.2
 */