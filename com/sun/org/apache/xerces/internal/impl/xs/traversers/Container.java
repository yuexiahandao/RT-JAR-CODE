/*      */ package com.sun.org.apache.xerces.internal.impl.xs.traversers;
/*      */ 
/*      */ abstract class Container
/*      */ {
/*      */   static final int THRESHOLD = 5;
/*      */   OneAttr[] values;
/* 1789 */   int pos = 0;
/*      */ 
/*      */   static Container getContainer(int size)
/*      */   {
/* 1780 */     if (size > 5) {
/* 1781 */       return new LargeContainer(size);
/*      */     }
/* 1783 */     return new SmallContainer(size);
/*      */   }
/*      */ 
/*      */   abstract void put(String paramString, OneAttr paramOneAttr);
/*      */ 
/*      */   abstract OneAttr get(String paramString);
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.xs.traversers.Container
 * JD-Core Version:    0.6.2
 */