/*     */ package javax.print;
/*     */ 
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.ServiceConfigurationError;
/*     */ import java.util.ServiceLoader;
/*     */ import javax.print.attribute.AttributeSet;
/*     */ import sun.awt.AppContext;
/*     */ 
/*     */ public abstract class PrintServiceLookup
/*     */ {
/*     */   private static Services getServicesForContext()
/*     */   {
/*  77 */     Services localServices = (Services)AppContext.getAppContext().get(Services.class);
/*     */ 
/*  79 */     if (localServices == null) {
/*  80 */       localServices = new Services();
/*  81 */       AppContext.getAppContext().put(Services.class, localServices);
/*     */     }
/*  83 */     return localServices;
/*     */   }
/*     */ 
/*     */   private static ArrayList getListOfLookupServices() {
/*  87 */     return getServicesForContext().listOfLookupServices;
/*     */   }
/*     */ 
/*     */   private static ArrayList initListOfLookupServices() {
/*  91 */     ArrayList localArrayList = new ArrayList();
/*  92 */     getServicesForContext().listOfLookupServices = localArrayList;
/*  93 */     return localArrayList;
/*     */   }
/*     */ 
/*     */   private static ArrayList getRegisteredServices()
/*     */   {
/*  98 */     return getServicesForContext().registeredServices;
/*     */   }
/*     */ 
/*     */   private static ArrayList initRegisteredServices() {
/* 102 */     ArrayList localArrayList = new ArrayList();
/* 103 */     getServicesForContext().registeredServices = localArrayList;
/* 104 */     return localArrayList;
/*     */   }
/*     */ 
/*     */   public static final PrintService[] lookupPrintServices(DocFlavor paramDocFlavor, AttributeSet paramAttributeSet)
/*     */   {
/* 123 */     ArrayList localArrayList = getServices(paramDocFlavor, paramAttributeSet);
/* 124 */     return (PrintService[])localArrayList.toArray(new PrintService[localArrayList.size()]);
/*     */   }
/*     */ 
/*     */   public static final MultiDocPrintService[] lookupMultiDocPrintServices(DocFlavor[] paramArrayOfDocFlavor, AttributeSet paramAttributeSet)
/*     */   {
/* 151 */     ArrayList localArrayList = getMultiDocServices(paramArrayOfDocFlavor, paramAttributeSet);
/* 152 */     return (MultiDocPrintService[])localArrayList.toArray(new MultiDocPrintService[localArrayList.size()]);
/*     */   }
/*     */ 
/*     */   public static final PrintService lookupDefaultPrintService()
/*     */   {
/* 180 */     Iterator localIterator = getAllLookupServices().iterator();
/* 181 */     while (localIterator.hasNext())
/*     */       try {
/* 183 */         PrintServiceLookup localPrintServiceLookup = (PrintServiceLookup)localIterator.next();
/* 184 */         PrintService localPrintService = localPrintServiceLookup.getDefaultPrintService();
/* 185 */         if (localPrintService != null)
/* 186 */           return localPrintService;
/*     */       }
/*     */       catch (Exception localException)
/*     */       {
/*     */       }
/* 191 */     return null;
/*     */   }
/*     */ 
/*     */   public static boolean registerServiceProvider(PrintServiceLookup paramPrintServiceLookup)
/*     */   {
/* 210 */     synchronized (PrintServiceLookup.class) {
/* 211 */       Iterator localIterator = getAllLookupServices().iterator();
/* 212 */       while (localIterator.hasNext())
/*     */         try {
/* 214 */           Object localObject1 = localIterator.next();
/* 215 */           if (localObject1.getClass() == paramPrintServiceLookup.getClass())
/* 216 */             return false;
/*     */         }
/*     */         catch (Exception localException)
/*     */         {
/*     */         }
/* 221 */       getListOfLookupServices().add(paramPrintServiceLookup);
/* 222 */       return true;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static boolean registerService(PrintService paramPrintService)
/*     */   {
/* 248 */     synchronized (PrintServiceLookup.class) {
/* 249 */       if ((paramPrintService instanceof StreamPrintService)) {
/* 250 */         return false;
/*     */       }
/* 252 */       ArrayList localArrayList = getRegisteredServices();
/* 253 */       if (localArrayList == null) {
/* 254 */         localArrayList = initRegisteredServices();
/*     */       }
/* 257 */       else if (localArrayList.contains(paramPrintService)) {
/* 258 */         return false;
/*     */       }
/*     */ 
/* 261 */       localArrayList.add(paramPrintService);
/* 262 */       return true;
/*     */     }
/*     */   }
/*     */ 
/*     */   public abstract PrintService[] getPrintServices(DocFlavor paramDocFlavor, AttributeSet paramAttributeSet);
/*     */ 
/*     */   public abstract PrintService[] getPrintServices();
/*     */ 
/*     */   public abstract MultiDocPrintService[] getMultiDocPrintServices(DocFlavor[] paramArrayOfDocFlavor, AttributeSet paramAttributeSet);
/*     */ 
/*     */   public abstract PrintService getDefaultPrintService();
/*     */ 
/*     */   private static ArrayList getAllLookupServices()
/*     */   {
/* 330 */     synchronized (PrintServiceLookup.class) {
/* 331 */       ArrayList localArrayList = getListOfLookupServices();
/* 332 */       if (localArrayList != null) {
/* 333 */         return localArrayList;
/*     */       }
/* 335 */       localArrayList = initListOfLookupServices();
/*     */       try
/*     */       {
/* 338 */         AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */         {
/*     */           public Object run() {
/* 341 */             Iterator localIterator = ServiceLoader.load(PrintServiceLookup.class).iterator();
/*     */ 
/* 344 */             ArrayList localArrayList = PrintServiceLookup.access$200();
/* 345 */             while (localIterator.hasNext()) {
/*     */               try {
/* 347 */                 localArrayList.add(localIterator.next());
/*     */               }
/*     */               catch (ServiceConfigurationError localServiceConfigurationError) {
/* 350 */                 if (System.getSecurityManager() != null)
/* 351 */                   localServiceConfigurationError.printStackTrace();
/*     */                 else {
/* 353 */                   throw localServiceConfigurationError;
/*     */                 }
/*     */               }
/*     */             }
/* 357 */             return null;
/*     */           }
/*     */         });
/*     */       }
/*     */       catch (PrivilegedActionException localPrivilegedActionException) {
/*     */       }
/* 363 */       return localArrayList;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static ArrayList getServices(DocFlavor paramDocFlavor, AttributeSet paramAttributeSet)
/*     */   {
/* 370 */     ArrayList localArrayList1 = new ArrayList();
/* 371 */     Iterator localIterator = getAllLookupServices().iterator();
/*     */     Object localObject;
/*     */     int i;
/* 372 */     while (localIterator.hasNext())
/*     */       try {
/* 374 */         PrintServiceLookup localPrintServiceLookup = (PrintServiceLookup)localIterator.next();
/* 375 */         localObject = null;
/* 376 */         if ((paramDocFlavor == null) && (paramAttributeSet == null))
/*     */           try {
/* 378 */             localObject = localPrintServiceLookup.getPrintServices();
/*     */           }
/*     */           catch (Throwable localThrowable) {
/*     */           }
/* 382 */         else localObject = localPrintServiceLookup.getPrintServices(paramDocFlavor, paramAttributeSet);
/*     */ 
/* 384 */         if (localObject != null)
/*     */         {
/* 387 */           for (i = 0; i < localObject.length; i++)
/* 388 */             localArrayList1.add(localObject[i]);
/*     */         }
/*     */       }
/*     */       catch (Exception localException)
/*     */       {
/*     */       }
/* 394 */     ArrayList localArrayList2 = null;
/*     */     try {
/* 396 */       localObject = System.getSecurityManager();
/* 397 */       if (localObject != null) {
/* 398 */         ((SecurityManager)localObject).checkPrintJobAccess();
/*     */       }
/* 400 */       localArrayList2 = getRegisteredServices();
/*     */     } catch (SecurityException localSecurityException) {
/*     */     }
/* 403 */     if (localArrayList2 != null) {
/* 404 */       PrintService[] arrayOfPrintService = (PrintService[])localArrayList2.toArray(new PrintService[localArrayList2.size()]);
/*     */ 
/* 407 */       for (i = 0; i < arrayOfPrintService.length; i++) {
/* 408 */         if (!localArrayList1.contains(arrayOfPrintService[i])) {
/* 409 */           if ((paramDocFlavor == null) && (paramAttributeSet == null))
/* 410 */             localArrayList1.add(arrayOfPrintService[i]);
/* 411 */           else if (((paramDocFlavor != null) && (arrayOfPrintService[i].isDocFlavorSupported(paramDocFlavor))) || ((paramDocFlavor == null) && (null == arrayOfPrintService[i].getUnsupportedAttributes(paramDocFlavor, paramAttributeSet))))
/*     */           {
/* 416 */             localArrayList1.add(arrayOfPrintService[i]);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 421 */     return localArrayList1;
/*     */   }
/*     */ 
/*     */   private static ArrayList getMultiDocServices(DocFlavor[] paramArrayOfDocFlavor, AttributeSet paramAttributeSet)
/*     */   {
/* 428 */     ArrayList localArrayList1 = new ArrayList();
/* 429 */     Iterator localIterator = getAllLookupServices().iterator();
/*     */     Object localObject;
/*     */     int i;
/* 430 */     while (localIterator.hasNext())
/*     */       try {
/* 432 */         PrintServiceLookup localPrintServiceLookup = (PrintServiceLookup)localIterator.next();
/* 433 */         localObject = localPrintServiceLookup.getMultiDocPrintServices(paramArrayOfDocFlavor, paramAttributeSet);
/*     */ 
/* 435 */         if (localObject != null)
/*     */         {
/* 438 */           for (i = 0; i < localObject.length; i++)
/* 439 */             localArrayList1.add(localObject[i]);
/*     */         }
/*     */       }
/*     */       catch (Exception localException1)
/*     */       {
/*     */       }
/* 445 */     ArrayList localArrayList2 = null;
/*     */     try {
/* 447 */       localObject = System.getSecurityManager();
/* 448 */       if (localObject != null) {
/* 449 */         ((SecurityManager)localObject).checkPrintJobAccess();
/*     */       }
/* 451 */       localArrayList2 = getRegisteredServices();
/*     */     } catch (Exception localException2) {
/*     */     }
/* 454 */     if (localArrayList2 != null) {
/* 455 */       PrintService[] arrayOfPrintService = (PrintService[])localArrayList2.toArray(new PrintService[localArrayList2.size()]);
/*     */ 
/* 458 */       for (i = 0; i < arrayOfPrintService.length; i++) {
/* 459 */         if (((arrayOfPrintService[i] instanceof MultiDocPrintService)) && (!localArrayList1.contains(arrayOfPrintService[i])))
/*     */         {
/* 461 */           if ((paramArrayOfDocFlavor == null) || (paramArrayOfDocFlavor.length == 0)) {
/* 462 */             localArrayList1.add(arrayOfPrintService[i]);
/*     */           } else {
/* 464 */             int j = 1;
/* 465 */             for (int k = 0; k < paramArrayOfDocFlavor.length; k++) {
/* 466 */               if (arrayOfPrintService[i].isDocFlavorSupported(paramArrayOfDocFlavor[k]))
/*     */               {
/* 468 */                 if (arrayOfPrintService[i].getUnsupportedAttributes(paramArrayOfDocFlavor[k], paramAttributeSet) != null)
/*     */                 {
/* 470 */                   j = 0;
/* 471 */                   break;
/*     */                 }
/*     */               } else {
/* 474 */                 j = 0;
/* 475 */                 break;
/*     */               }
/*     */             }
/* 478 */             if (j != 0) {
/* 479 */               localArrayList1.add(arrayOfPrintService[i]);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 485 */     return localArrayList1;
/*     */   }
/*     */ 
/*     */   static class Services
/*     */   {
/*  72 */     private ArrayList listOfLookupServices = null;
/*  73 */     private ArrayList registeredServices = null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.print.PrintServiceLookup
 * JD-Core Version:    0.6.2
 */