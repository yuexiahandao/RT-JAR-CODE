/*    */ package com.sun.xml.internal.ws.util.xml;
/*    */ 
/*    */ import java.security.AccessController;
/*    */ import java.security.PrivilegedAction;
/*    */ import java.text.MessageFormat;
/*    */ import java.util.ResourceBundle;
/*    */ import java.util.WeakHashMap;
/*    */ 
/*    */ abstract class ContextClassloaderLocal<V>
/*    */ {
/*    */   private static final String FAILED_TO_CREATE_NEW_INSTANCE = "FAILED_TO_CREATE_NEW_INSTANCE";
/* 41 */   private WeakHashMap<ClassLoader, V> CACHE = new WeakHashMap();
/*    */ 
/*    */   public V get() throws Error {
/* 44 */     ClassLoader tccl = getContextClassLoader();
/* 45 */     Object instance = this.CACHE.get(tccl);
/* 46 */     if (instance == null) {
/* 47 */       instance = createNewInstance();
/* 48 */       this.CACHE.put(tccl, instance);
/*    */     }
/* 50 */     return instance;
/*    */   }
/*    */ 
/*    */   public void set(V instance) {
/* 54 */     this.CACHE.put(getContextClassLoader(), instance);
/*    */   }
/*    */ 
/*    */   protected abstract V initialValue() throws Exception;
/*    */ 
/*    */   private V createNewInstance() {
/*    */     try {
/* 61 */       return initialValue();
/*    */     } catch (Exception e) {
/* 63 */       throw new Error(format("FAILED_TO_CREATE_NEW_INSTANCE", new Object[] { getClass().getName() }), e);
/*    */     }
/*    */   }
/*    */ 
/*    */   private static String format(String property, Object[] args) {
/* 68 */     String text = ResourceBundle.getBundle(ContextClassloaderLocal.class.getName()).getString(property);
/* 69 */     return MessageFormat.format(text, args);
/*    */   }
/*    */ 
/*    */   private static ClassLoader getContextClassLoader() {
/* 73 */     return (ClassLoader)AccessController.doPrivileged(new PrivilegedAction()
/*    */     {
/*    */       public Object run() {
/* 76 */         ClassLoader cl = null;
/*    */         try {
/* 78 */           cl = Thread.currentThread().getContextClassLoader();
/*    */         } catch (SecurityException ex) {
/*    */         }
/* 81 */         return cl;
/*    */       }
/*    */     });
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.util.xml.ContextClassloaderLocal
 * JD-Core Version:    0.6.2
 */