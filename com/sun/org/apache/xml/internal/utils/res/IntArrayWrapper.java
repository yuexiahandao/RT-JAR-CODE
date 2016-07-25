/*    */ package com.sun.org.apache.xml.internal.utils.res;
/*    */ 
/*    */ public class IntArrayWrapper
/*    */ {
/*    */   private int[] m_int;
/*    */ 
/*    */   public IntArrayWrapper(int[] arg)
/*    */   {
/* 34 */     this.m_int = arg;
/*    */   }
/*    */ 
/*    */   public int getInt(int index) {
/* 38 */     return this.m_int[index];
/*    */   }
/*    */ 
/*    */   public int getLength() {
/* 42 */     return this.m_int.length;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.utils.res.IntArrayWrapper
 * JD-Core Version:    0.6.2
 */