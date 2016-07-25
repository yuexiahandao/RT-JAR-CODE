/*     */ package com.sun.naming.internal;
/*     */ 
/*     */ import java.util.List;
/*     */ import javax.naming.NamingException;
/*     */ 
/*     */ public final class FactoryEnumeration
/*     */ {
/*     */   private List factories;
/*  41 */   private int posn = 0;
/*     */   private ClassLoader loader;
/*     */ 
/*     */   FactoryEnumeration(List paramList, ClassLoader paramClassLoader)
/*     */   {
/*  66 */     this.factories = paramList;
/*  67 */     this.loader = paramClassLoader;
/*     */   }
/*     */ 
/*     */   public Object next() throws NamingException {
/*  71 */     synchronized (this.factories)
/*     */     {
/*  73 */       NamedWeakReference localNamedWeakReference = (NamedWeakReference)this.factories.get(this.posn++);
/*  74 */       Object localObject1 = localNamedWeakReference.get();
/*  75 */       if ((localObject1 != null) && (!(localObject1 instanceof Class))) {
/*  76 */         return localObject1;
/*     */       }
/*     */ 
/*  79 */       String str = localNamedWeakReference.getName();
/*     */       try
/*     */       {
/*  82 */         if (localObject1 == null) {
/*  83 */           Class localClass = Class.forName(str, true, this.loader);
/*  84 */           localObject1 = localClass;
/*     */         }
/*     */ 
/*  87 */         localObject1 = ((Class)localObject1).newInstance();
/*  88 */         localNamedWeakReference = new NamedWeakReference(localObject1, str);
/*  89 */         this.factories.set(this.posn - 1, localNamedWeakReference);
/*  90 */         return localObject1;
/*     */       } catch (ClassNotFoundException localClassNotFoundException) {
/*  92 */         localNamingException = new NamingException("No longer able to load " + str);
/*     */ 
/*  94 */         localNamingException.setRootCause(localClassNotFoundException);
/*  95 */         throw localNamingException;
/*     */       } catch (InstantiationException localInstantiationException) {
/*  97 */         localNamingException = new NamingException("Cannot instantiate " + localObject1);
/*     */ 
/*  99 */         localNamingException.setRootCause(localInstantiationException);
/* 100 */         throw localNamingException;
/*     */       } catch (IllegalAccessException localIllegalAccessException) {
/* 102 */         NamingException localNamingException = new NamingException("Cannot access " + localObject1);
/* 103 */         localNamingException.setRootCause(localIllegalAccessException);
/* 104 */         throw localNamingException;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean hasMore() {
/* 110 */     synchronized (this.factories) {
/* 111 */       return this.posn < this.factories.size();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.naming.internal.FactoryEnumeration
 * JD-Core Version:    0.6.2
 */