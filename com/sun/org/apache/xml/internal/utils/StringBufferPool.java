/*    */ package com.sun.org.apache.xml.internal.utils;
/*    */ 
/*    */ public class StringBufferPool
/*    */ {
/* 35 */   private static ObjectPool m_stringBufPool = new ObjectPool(FastStringBuffer.class);
/*    */ 
/*    */   public static synchronized FastStringBuffer get()
/*    */   {
/* 46 */     return (FastStringBuffer)m_stringBufPool.getInstance();
/*    */   }
/*    */ 
/*    */   public static synchronized void free(FastStringBuffer sb)
/*    */   {
/* 59 */     sb.setLength(0);
/* 60 */     m_stringBufPool.freeInstance(sb);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.utils.StringBufferPool
 * JD-Core Version:    0.6.2
 */