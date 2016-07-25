/*     */ package java.beans;
/*     */ 
/*     */ public abstract class PersistenceDelegate
/*     */ {
/*     */   public void writeObject(Object paramObject, Encoder paramEncoder)
/*     */   {
/* 112 */     Object localObject = paramEncoder.get(paramObject);
/* 113 */     if (!mutatesTo(paramObject, localObject)) {
/* 114 */       paramEncoder.remove(paramObject);
/* 115 */       paramEncoder.writeExpression(instantiate(paramObject, paramEncoder));
/*     */     }
/*     */     else {
/* 118 */       initialize(paramObject.getClass(), paramObject, localObject, paramEncoder);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected boolean mutatesTo(Object paramObject1, Object paramObject2)
/*     */   {
/* 142 */     return (paramObject2 != null) && (paramObject1 != null) && (paramObject1.getClass() == paramObject2.getClass());
/*     */   }
/*     */ 
/*     */   protected abstract Expression instantiate(Object paramObject, Encoder paramEncoder);
/*     */ 
/*     */   protected void initialize(Class<?> paramClass, Object paramObject1, Object paramObject2, Encoder paramEncoder)
/*     */   {
/* 210 */     Class localClass = paramClass.getSuperclass();
/* 211 */     PersistenceDelegate localPersistenceDelegate = paramEncoder.getPersistenceDelegate(localClass);
/* 212 */     localPersistenceDelegate.initialize(localClass, paramObject1, paramObject2, paramEncoder);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.beans.PersistenceDelegate
 * JD-Core Version:    0.6.2
 */