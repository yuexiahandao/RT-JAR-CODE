/*    */ package com.sun.naming.internal;
/*    */ 
/*    */ import java.lang.ref.WeakReference;
/*    */ 
/*    */ class NamedWeakReference extends WeakReference
/*    */ {
/*    */   private final String name;
/*    */ 
/*    */   NamedWeakReference(Object paramObject, String paramString)
/*    */   {
/* 41 */     super(paramObject);
/* 42 */     this.name = paramString;
/*    */   }
/*    */ 
/*    */   String getName() {
/* 46 */     return this.name;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.naming.internal.NamedWeakReference
 * JD-Core Version:    0.6.2
 */