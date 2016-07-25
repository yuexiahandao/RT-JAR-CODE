/*    */ package com.sun.corba.se.impl.orbutil;
/*    */ 
/*    */ import java.lang.reflect.Array;
/*    */ 
/*    */ public final class ObjectUtility
/*    */ {
/*    */   public static Object concatenateArrays(Object paramObject1, Object paramObject2)
/*    */   {
/* 63 */     Class localClass1 = paramObject1.getClass().getComponentType();
/* 64 */     Class localClass2 = paramObject2.getClass().getComponentType();
/* 65 */     int i = Array.getLength(paramObject1);
/* 66 */     int j = Array.getLength(paramObject2);
/*    */ 
/* 68 */     if ((localClass1 == null) || (localClass2 == null))
/* 69 */       throw new IllegalStateException("Arguments must be arrays");
/* 70 */     if (!localClass1.equals(localClass2)) {
/* 71 */       throw new IllegalStateException("Arguments must be arrays with the same component type");
/*    */     }
/*    */ 
/* 74 */     Object localObject = Array.newInstance(localClass1, i + j);
/*    */ 
/* 76 */     int k = 0;
/*    */ 
/* 78 */     for (int m = 0; m < i; m++) {
/* 79 */       Array.set(localObject, k++, Array.get(paramObject1, m));
/*    */     }
/* 81 */     for (m = 0; m < j; m++) {
/* 82 */       Array.set(localObject, k++, Array.get(paramObject2, m));
/*    */     }
/* 84 */     return localObject;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.orbutil.ObjectUtility
 * JD-Core Version:    0.6.2
 */