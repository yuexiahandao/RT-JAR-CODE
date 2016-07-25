/*     */ package javax.imageio.spi;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.ServiceLoader;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class ServiceRegistry
/*     */ {
/* 102 */   private Map categoryMap = new HashMap();
/*     */ 
/*     */   public ServiceRegistry(Iterator<Class<?>> paramIterator)
/*     */   {
/* 116 */     if (paramIterator == null) {
/* 117 */       throw new IllegalArgumentException("categories == null!");
/*     */     }
/* 119 */     while (paramIterator.hasNext()) {
/* 120 */       Class localClass = (Class)paramIterator.next();
/* 121 */       SubRegistry localSubRegistry = new SubRegistry(this, localClass);
/* 122 */       this.categoryMap.put(localClass, localSubRegistry);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static <T> Iterator<T> lookupProviders(Class<T> paramClass, ClassLoader paramClassLoader)
/*     */   {
/* 172 */     if (paramClass == null) {
/* 173 */       throw new IllegalArgumentException("providerClass == null!");
/*     */     }
/* 175 */     return ServiceLoader.load(paramClass, paramClassLoader).iterator();
/*     */   }
/*     */ 
/*     */   public static <T> Iterator<T> lookupProviders(Class<T> paramClass)
/*     */   {
/* 201 */     if (paramClass == null) {
/* 202 */       throw new IllegalArgumentException("providerClass == null!");
/*     */     }
/* 204 */     return ServiceLoader.load(paramClass).iterator();
/*     */   }
/*     */ 
/*     */   public Iterator<Class<?>> getCategories()
/*     */   {
/* 216 */     Set localSet = this.categoryMap.keySet();
/* 217 */     return localSet.iterator();
/*     */   }
/*     */ 
/*     */   private Iterator getSubRegistries(Object paramObject)
/*     */   {
/* 225 */     ArrayList localArrayList = new ArrayList();
/* 226 */     Iterator localIterator = this.categoryMap.keySet().iterator();
/* 227 */     while (localIterator.hasNext()) {
/* 228 */       Class localClass = (Class)localIterator.next();
/* 229 */       if (localClass.isAssignableFrom(paramObject.getClass())) {
/* 230 */         localArrayList.add((SubRegistry)this.categoryMap.get(localClass));
/*     */       }
/*     */     }
/* 233 */     return localArrayList.iterator();
/*     */   }
/*     */ 
/*     */   public <T> boolean registerServiceProvider(T paramT, Class<T> paramClass)
/*     */   {
/* 263 */     if (paramT == null) {
/* 264 */       throw new IllegalArgumentException("provider == null!");
/*     */     }
/* 266 */     SubRegistry localSubRegistry = (SubRegistry)this.categoryMap.get(paramClass);
/* 267 */     if (localSubRegistry == null) {
/* 268 */       throw new IllegalArgumentException("category unknown!");
/*     */     }
/* 270 */     if (!paramClass.isAssignableFrom(paramT.getClass())) {
/* 271 */       throw new ClassCastException();
/*     */     }
/*     */ 
/* 274 */     return localSubRegistry.registerServiceProvider(paramT);
/*     */   }
/*     */ 
/*     */   public void registerServiceProvider(Object paramObject)
/*     */   {
/* 296 */     if (paramObject == null) {
/* 297 */       throw new IllegalArgumentException("provider == null!");
/*     */     }
/* 299 */     Iterator localIterator = getSubRegistries(paramObject);
/* 300 */     while (localIterator.hasNext()) {
/* 301 */       SubRegistry localSubRegistry = (SubRegistry)localIterator.next();
/* 302 */       localSubRegistry.registerServiceProvider(paramObject);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void registerServiceProviders(Iterator<?> paramIterator)
/*     */   {
/* 327 */     if (paramIterator == null) {
/* 328 */       throw new IllegalArgumentException("provider == null!");
/*     */     }
/* 330 */     while (paramIterator.hasNext())
/* 331 */       registerServiceProvider(paramIterator.next());
/*     */   }
/*     */ 
/*     */   public <T> boolean deregisterServiceProvider(T paramT, Class<T> paramClass)
/*     */   {
/* 365 */     if (paramT == null) {
/* 366 */       throw new IllegalArgumentException("provider == null!");
/*     */     }
/* 368 */     SubRegistry localSubRegistry = (SubRegistry)this.categoryMap.get(paramClass);
/* 369 */     if (localSubRegistry == null) {
/* 370 */       throw new IllegalArgumentException("category unknown!");
/*     */     }
/* 372 */     if (!paramClass.isAssignableFrom(paramT.getClass())) {
/* 373 */       throw new ClassCastException();
/*     */     }
/* 375 */     return localSubRegistry.deregisterServiceProvider(paramT);
/*     */   }
/*     */ 
/*     */   public void deregisterServiceProvider(Object paramObject)
/*     */   {
/* 388 */     if (paramObject == null) {
/* 389 */       throw new IllegalArgumentException("provider == null!");
/*     */     }
/* 391 */     Iterator localIterator = getSubRegistries(paramObject);
/* 392 */     while (localIterator.hasNext()) {
/* 393 */       SubRegistry localSubRegistry = (SubRegistry)localIterator.next();
/* 394 */       localSubRegistry.deregisterServiceProvider(paramObject);
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean contains(Object paramObject)
/*     */   {
/* 411 */     if (paramObject == null) {
/* 412 */       throw new IllegalArgumentException("provider == null!");
/*     */     }
/* 414 */     Iterator localIterator = getSubRegistries(paramObject);
/* 415 */     while (localIterator.hasNext()) {
/* 416 */       SubRegistry localSubRegistry = (SubRegistry)localIterator.next();
/* 417 */       if (localSubRegistry.contains(paramObject)) {
/* 418 */         return true;
/*     */       }
/*     */     }
/*     */ 
/* 422 */     return false;
/*     */   }
/*     */ 
/*     */   public <T> Iterator<T> getServiceProviders(Class<T> paramClass, boolean paramBoolean)
/*     */   {
/* 447 */     SubRegistry localSubRegistry = (SubRegistry)this.categoryMap.get(paramClass);
/* 448 */     if (localSubRegistry == null) {
/* 449 */       throw new IllegalArgumentException("category unknown!");
/*     */     }
/* 451 */     return localSubRegistry.getServiceProviders(paramBoolean);
/*     */   }
/*     */ 
/*     */   public <T> Iterator<T> getServiceProviders(Class<T> paramClass, Filter paramFilter, boolean paramBoolean)
/*     */   {
/* 503 */     SubRegistry localSubRegistry = (SubRegistry)this.categoryMap.get(paramClass);
/* 504 */     if (localSubRegistry == null) {
/* 505 */       throw new IllegalArgumentException("category unknown!");
/*     */     }
/* 507 */     Iterator localIterator = getServiceProviders(paramClass, paramBoolean);
/* 508 */     return new FilterIterator(localIterator, paramFilter);
/*     */   }
/*     */ 
/*     */   public <T> T getServiceProviderByClass(Class<T> paramClass)
/*     */   {
/* 529 */     if (paramClass == null) {
/* 530 */       throw new IllegalArgumentException("providerClass == null!");
/*     */     }
/* 532 */     Iterator localIterator = this.categoryMap.keySet().iterator();
/* 533 */     while (localIterator.hasNext()) {
/* 534 */       Class localClass = (Class)localIterator.next();
/* 535 */       if (localClass.isAssignableFrom(paramClass)) {
/* 536 */         SubRegistry localSubRegistry = (SubRegistry)this.categoryMap.get(localClass);
/* 537 */         Object localObject = localSubRegistry.getServiceProviderByClass(paramClass);
/* 538 */         if (localObject != null) {
/* 539 */           return localObject;
/*     */         }
/*     */       }
/*     */     }
/* 543 */     return null;
/*     */   }
/*     */ 
/*     */   public <T> boolean setOrdering(Class<T> paramClass, T paramT1, T paramT2)
/*     */   {
/* 576 */     if ((paramT1 == null) || (paramT2 == null)) {
/* 577 */       throw new IllegalArgumentException("provider is null!");
/*     */     }
/* 579 */     if (paramT1 == paramT2) {
/* 580 */       throw new IllegalArgumentException("providers are the same!");
/*     */     }
/* 582 */     SubRegistry localSubRegistry = (SubRegistry)this.categoryMap.get(paramClass);
/* 583 */     if (localSubRegistry == null) {
/* 584 */       throw new IllegalArgumentException("category unknown!");
/*     */     }
/* 586 */     if ((localSubRegistry.contains(paramT1)) && (localSubRegistry.contains(paramT2)))
/*     */     {
/* 588 */       return localSubRegistry.setOrdering(paramT1, paramT2);
/*     */     }
/* 590 */     return false;
/*     */   }
/*     */ 
/*     */   public <T> boolean unsetOrdering(Class<T> paramClass, T paramT1, T paramT2)
/*     */   {
/* 621 */     if ((paramT1 == null) || (paramT2 == null)) {
/* 622 */       throw new IllegalArgumentException("provider is null!");
/*     */     }
/* 624 */     if (paramT1 == paramT2) {
/* 625 */       throw new IllegalArgumentException("providers are the same!");
/*     */     }
/* 627 */     SubRegistry localSubRegistry = (SubRegistry)this.categoryMap.get(paramClass);
/* 628 */     if (localSubRegistry == null) {
/* 629 */       throw new IllegalArgumentException("category unknown!");
/*     */     }
/* 631 */     if ((localSubRegistry.contains(paramT1)) && (localSubRegistry.contains(paramT2)))
/*     */     {
/* 633 */       return localSubRegistry.unsetOrdering(paramT1, paramT2);
/*     */     }
/* 635 */     return false;
/*     */   }
/*     */ 
/*     */   public void deregisterAll(Class<?> paramClass)
/*     */   {
/* 648 */     SubRegistry localSubRegistry = (SubRegistry)this.categoryMap.get(paramClass);
/* 649 */     if (localSubRegistry == null) {
/* 650 */       throw new IllegalArgumentException("category unknown!");
/*     */     }
/* 652 */     localSubRegistry.clear();
/*     */   }
/*     */ 
/*     */   public void deregisterAll()
/*     */   {
/* 660 */     Iterator localIterator = this.categoryMap.values().iterator();
/* 661 */     while (localIterator.hasNext()) {
/* 662 */       SubRegistry localSubRegistry = (SubRegistry)localIterator.next();
/* 663 */       localSubRegistry.clear();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void finalize()
/*     */     throws Throwable
/*     */   {
/* 677 */     deregisterAll();
/* 678 */     super.finalize();
/*     */   }
/*     */ 
/*     */   public static abstract interface Filter
/*     */   {
/*     */     public abstract boolean filter(Object paramObject);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.imageio.spi.ServiceRegistry
 * JD-Core Version:    0.6.2
 */