/*     */ package com.sun.org.apache.xml.internal.utils;
/*     */ 
/*     */ import com.sun.org.apache.xalan.internal.utils.ObjectFactory;
/*     */ import com.sun.org.apache.xml.internal.res.XMLMessages;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ public class ObjectPool
/*     */   implements Serializable
/*     */ {
/*     */   static final long serialVersionUID = -8519013691660936643L;
/*     */   private final Class objectType;
/*     */   private final ArrayList freeStack;
/*     */ 
/*     */   public ObjectPool(Class type)
/*     */   {
/*  55 */     this.objectType = type;
/*  56 */     this.freeStack = new ArrayList();
/*     */   }
/*     */ 
/*     */   public ObjectPool(String className)
/*     */   {
/*     */     try
/*     */     {
/*  68 */       this.objectType = ObjectFactory.findProviderClass(className, true);
/*     */     }
/*     */     catch (ClassNotFoundException cnfe)
/*     */     {
/*  72 */       throw new WrappedRuntimeException(cnfe);
/*     */     }
/*  74 */     this.freeStack = new ArrayList();
/*     */   }
/*     */ 
/*     */   public ObjectPool(Class type, int size)
/*     */   {
/*  87 */     this.objectType = type;
/*  88 */     this.freeStack = new ArrayList(size);
/*     */   }
/*     */ 
/*     */   public ObjectPool()
/*     */   {
/*  97 */     this.objectType = null;
/*  98 */     this.freeStack = new ArrayList();
/*     */   }
/*     */ 
/*     */   public synchronized Object getInstanceIfFree()
/*     */   {
/* 111 */     if (!this.freeStack.isEmpty())
/*     */     {
/* 115 */       Object result = this.freeStack.remove(this.freeStack.size() - 1);
/* 116 */       return result;
/*     */     }
/*     */ 
/* 119 */     return null;
/*     */   }
/*     */ 
/*     */   public synchronized Object getInstance()
/*     */   {
/* 132 */     if (this.freeStack.isEmpty())
/*     */     {
/*     */       try
/*     */       {
/* 138 */         return this.objectType.newInstance();
/*     */       }
/*     */       catch (InstantiationException ex) {
/*     */       }
/*     */       catch (IllegalAccessException ex) {
/*     */       }
/* 144 */       throw new RuntimeException(XMLMessages.createXMLMessage("ER_EXCEPTION_CREATING_POOL", null));
/*     */     }
/*     */ 
/* 150 */     Object result = this.freeStack.remove(this.freeStack.size() - 1);
/* 151 */     return result;
/*     */   }
/*     */ 
/*     */   public synchronized void freeInstance(Object obj)
/*     */   {
/* 168 */     this.freeStack.add(obj);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.utils.ObjectPool
 * JD-Core Version:    0.6.2
 */