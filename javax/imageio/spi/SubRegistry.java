/*     */ package javax.imageio.spi;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ 
/*     */ class SubRegistry
/*     */ {
/*     */   ServiceRegistry registry;
/*     */   Class category;
/* 694 */   PartiallyOrderedSet poset = new PartiallyOrderedSet();
/*     */ 
/* 697 */   Map<Class<?>, Object> map = new HashMap();
/*     */ 
/*     */   public SubRegistry(ServiceRegistry paramServiceRegistry, Class paramClass) {
/* 700 */     this.registry = paramServiceRegistry;
/* 701 */     this.category = paramClass;
/*     */   }
/*     */ 
/*     */   public boolean registerServiceProvider(Object paramObject) {
/* 705 */     Object localObject = this.map.get(paramObject.getClass());
/* 706 */     int i = localObject != null ? 1 : 0;
/*     */ 
/* 708 */     if (i != 0) {
/* 709 */       deregisterServiceProvider(localObject);
/*     */     }
/* 711 */     this.map.put(paramObject.getClass(), paramObject);
/* 712 */     this.poset.add(paramObject);
/* 713 */     if ((paramObject instanceof RegisterableService)) {
/* 714 */       RegisterableService localRegisterableService = (RegisterableService)paramObject;
/* 715 */       localRegisterableService.onRegistration(this.registry, this.category);
/*     */     }
/*     */ 
/* 718 */     return i == 0;
/*     */   }
/*     */ 
/*     */   public boolean deregisterServiceProvider(Object paramObject)
/*     */   {
/* 727 */     Object localObject = this.map.get(paramObject.getClass());
/*     */ 
/* 729 */     if (paramObject == localObject) {
/* 730 */       this.map.remove(paramObject.getClass());
/* 731 */       this.poset.remove(paramObject);
/* 732 */       if ((paramObject instanceof RegisterableService)) {
/* 733 */         RegisterableService localRegisterableService = (RegisterableService)paramObject;
/* 734 */         localRegisterableService.onDeregistration(this.registry, this.category);
/*     */       }
/*     */ 
/* 737 */       return true;
/*     */     }
/* 739 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean contains(Object paramObject) {
/* 743 */     Object localObject = this.map.get(paramObject.getClass());
/* 744 */     return localObject == paramObject;
/*     */   }
/*     */ 
/*     */   public boolean setOrdering(Object paramObject1, Object paramObject2)
/*     */   {
/* 749 */     return this.poset.setOrdering(paramObject1, paramObject2);
/*     */   }
/*     */ 
/*     */   public boolean unsetOrdering(Object paramObject1, Object paramObject2)
/*     */   {
/* 754 */     return this.poset.unsetOrdering(paramObject1, paramObject2);
/*     */   }
/*     */ 
/*     */   public Iterator getServiceProviders(boolean paramBoolean) {
/* 758 */     if (paramBoolean) {
/* 759 */       return this.poset.iterator();
/*     */     }
/* 761 */     return this.map.values().iterator();
/*     */   }
/*     */ 
/*     */   public <T> T getServiceProviderByClass(Class<T> paramClass)
/*     */   {
/* 766 */     return this.map.get(paramClass);
/*     */   }
/*     */ 
/*     */   public void clear() {
/* 770 */     Iterator localIterator = this.map.values().iterator();
/* 771 */     while (localIterator.hasNext()) {
/* 772 */       Object localObject = localIterator.next();
/* 773 */       localIterator.remove();
/*     */ 
/* 775 */       if ((localObject instanceof RegisterableService)) {
/* 776 */         RegisterableService localRegisterableService = (RegisterableService)localObject;
/* 777 */         localRegisterableService.onDeregistration(this.registry, this.category);
/*     */       }
/*     */     }
/* 780 */     this.poset.clear();
/*     */   }
/*     */ 
/*     */   public void finalize() {
/* 784 */     clear();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.imageio.spi.SubRegistry
 * JD-Core Version:    0.6.2
 */