/*     */ package com.sun.corba.se.impl.io;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.rmi.Remote;
/*     */ import java.rmi.RemoteException;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ 
/*     */ class ObjectStreamClassCorbaExt
/*     */ {
/*     */   static final boolean isAbstractInterface(Class paramClass)
/*     */   {
/*  65 */     if ((!paramClass.isInterface()) || (Remote.class.isAssignableFrom(paramClass)))
/*     */     {
/*  67 */       return false;
/*     */     }
/*  69 */     Method[] arrayOfMethod = paramClass.getMethods();
/*  70 */     for (int i = 0; i < arrayOfMethod.length; i++) {
/*  71 */       Class[] arrayOfClass = arrayOfMethod[i].getExceptionTypes();
/*  72 */       int j = 0;
/*  73 */       for (int k = 0; (k < arrayOfClass.length) && (j == 0); k++) {
/*  74 */         if ((RemoteException.class == arrayOfClass[k]) || (Throwable.class == arrayOfClass[k]) || (Exception.class == arrayOfClass[k]) || (IOException.class == arrayOfClass[k]))
/*     */         {
/*  78 */           j = 1;
/*     */         }
/*     */       }
/*  81 */       if (j == 0) {
/*  82 */         return false;
/*     */       }
/*     */     }
/*  85 */     return true;
/*     */   }
/*     */ 
/*     */   static final boolean isAny(String paramString)
/*     */   {
/*  93 */     int i = 0;
/*     */ 
/*  95 */     if ((paramString != null) && ((paramString.equals("Ljava/lang/Object;")) || (paramString.equals("Ljava/io/Serializable;")) || (paramString.equals("Ljava/io/Externalizable;"))))
/*     */     {
/*  99 */       i = 1;
/*     */     }
/* 101 */     return i == 1;
/*     */   }
/*     */ 
/*     */   private static final Method[] getDeclaredMethods(Class paramClass) {
/* 105 */     return (Method[])AccessController.doPrivileged(new PrivilegedAction() {
/*     */       public Object run() {
/* 107 */         return this.val$clz.getDeclaredMethods();
/*     */       }
/*     */     });
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.io.ObjectStreamClassCorbaExt
 * JD-Core Version:    0.6.2
 */