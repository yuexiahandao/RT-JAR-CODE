/*    */ package com.sun.org.apache.bcel.internal.util;
/*    */ 
/*    */ public final class Objects
/*    */ {
/*    */   private Objects()
/*    */   {
/* 34 */     throw new IllegalAccessError();
/*    */   }
/*    */ 
/*    */   public static int hashCode(Object o) {
/* 38 */     return o == null ? 0 : o.hashCode();
/*    */   }
/*    */ 
/*    */   public static boolean equals(Object one, Object two) {
/* 42 */     return (one == two) || ((one != null) && (one.equals(two)));
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.util.Objects
 * JD-Core Version:    0.6.2
 */