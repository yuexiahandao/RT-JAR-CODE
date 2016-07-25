/*    */ package java.beans;
/*    */ 
/*    */ import java.lang.ref.SoftReference;
/*    */ import java.lang.ref.WeakReference;
/*    */ import java.lang.reflect.Method;
/*    */ import sun.reflect.misc.ReflectUtil;
/*    */ 
/*    */ final class MethodRef
/*    */ {
/*    */   private String signature;
/*    */   private SoftReference<Method> methodRef;
/*    */   private WeakReference<Class<?>> typeRef;
/*    */ 
/*    */   void set(Method paramMethod)
/*    */   {
/* 40 */     if (paramMethod == null) {
/* 41 */       this.signature = null;
/* 42 */       this.methodRef = null;
/* 43 */       this.typeRef = null;
/*    */     }
/*    */     else {
/* 46 */       this.signature = paramMethod.toGenericString();
/* 47 */       this.methodRef = new SoftReference(paramMethod);
/* 48 */       this.typeRef = new WeakReference(paramMethod.getDeclaringClass());
/*    */     }
/*    */   }
/*    */ 
/*    */   boolean isSet() {
/* 53 */     return this.methodRef != null;
/*    */   }
/*    */ 
/*    */   Method get() {
/* 57 */     if (this.methodRef == null) {
/* 58 */       return null;
/*    */     }
/* 60 */     Method localMethod = (Method)this.methodRef.get();
/* 61 */     if (localMethod == null) {
/* 62 */       localMethod = find((Class)this.typeRef.get(), this.signature);
/* 63 */       if (localMethod == null) {
/* 64 */         this.signature = null;
/* 65 */         this.methodRef = null;
/* 66 */         this.typeRef = null;
/*    */       }
/*    */       else {
/* 69 */         this.methodRef = new SoftReference(localMethod);
/*    */       }
/*    */     }
/* 72 */     return ReflectUtil.isPackageAccessible(localMethod.getDeclaringClass()) ? localMethod : null;
/*    */   }
/*    */ 
/*    */   private static Method find(Class<?> paramClass, String paramString) {
/* 76 */     if (paramClass != null) {
/* 77 */       for (Method localMethod : paramClass.getMethods()) {
/* 78 */         if ((paramClass.equals(localMethod.getDeclaringClass())) && 
/* 79 */           (localMethod.toGenericString().equals(paramString))) {
/* 80 */           return localMethod;
/*    */         }
/*    */       }
/*    */     }
/*    */ 
/* 85 */     return null;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.beans.MethodRef
 * JD-Core Version:    0.6.2
 */