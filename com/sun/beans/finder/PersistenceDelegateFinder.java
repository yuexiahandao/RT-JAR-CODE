/*    */ package com.sun.beans.finder;
/*    */ 
/*    */ import java.beans.PersistenceDelegate;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ 
/*    */ public final class PersistenceDelegateFinder extends InstanceFinder<PersistenceDelegate>
/*    */ {
/*    */   private final Map<Class<?>, PersistenceDelegate> registry;
/*    */ 
/*    */   public PersistenceDelegateFinder()
/*    */   {
/* 45 */     super(PersistenceDelegate.class, true, "PersistenceDelegate", new String[0]);
/* 46 */     this.registry = new HashMap();
/*    */   }
/*    */ 
/*    */   public void register(Class<?> paramClass, PersistenceDelegate paramPersistenceDelegate) {
/* 50 */     synchronized (this.registry) {
/* 51 */       if (paramPersistenceDelegate != null) {
/* 52 */         this.registry.put(paramClass, paramPersistenceDelegate);
/*    */       }
/*    */       else
/* 55 */         this.registry.remove(paramClass);
/*    */     }
/*    */   }
/*    */ 
/*    */   public PersistenceDelegate find(Class<?> paramClass)
/*    */   {
/*    */     PersistenceDelegate localPersistenceDelegate;
/* 63 */     synchronized (this.registry) {
/* 64 */       localPersistenceDelegate = (PersistenceDelegate)this.registry.get(paramClass);
/*    */     }
/* 66 */     return localPersistenceDelegate != null ? localPersistenceDelegate : (PersistenceDelegate)super.find(paramClass);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.beans.finder.PersistenceDelegateFinder
 * JD-Core Version:    0.6.2
 */