/*    */ package com.sun.xml.internal.stream.util;
/*    */ 
/*    */ import java.lang.ref.SoftReference;
/*    */ 
/*    */ public class ThreadLocalBufferAllocator
/*    */ {
/* 42 */   private static ThreadLocal tlba = new ThreadLocal();
/*    */ 
/*    */   public static BufferAllocator getBufferAllocator() {
/* 45 */     SoftReference bAllocatorRef = (SoftReference)tlba.get();
/* 46 */     if ((bAllocatorRef == null) || (bAllocatorRef.get() == null)) {
/* 47 */       bAllocatorRef = new SoftReference(new BufferAllocator());
/* 48 */       tlba.set(bAllocatorRef);
/*    */     }
/*    */ 
/* 51 */     return (BufferAllocator)bAllocatorRef.get();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.stream.util.ThreadLocalBufferAllocator
 * JD-Core Version:    0.6.2
 */