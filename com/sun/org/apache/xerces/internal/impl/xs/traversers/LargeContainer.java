/*      */ package com.sun.org.apache.xerces.internal.impl.xs.traversers;
/*      */ 
/*      */ import java.util.HashMap;
/*      */ import java.util.Map;
/*      */ 
/*      */ class LargeContainer extends Container
/*      */ {
/*      */   Map items;
/*      */ 
/*      */   LargeContainer(int size)
/*      */   {
/* 1815 */     this.items = new HashMap(size * 2 + 1);
/* 1816 */     this.values = new OneAttr[size];
/*      */   }
/*      */   void put(String key, OneAttr value) {
/* 1819 */     this.items.put(key, value);
/* 1820 */     this.values[(this.pos++)] = value;
/*      */   }
/*      */   OneAttr get(String key) {
/* 1823 */     OneAttr ret = (OneAttr)this.items.get(key);
/* 1824 */     return ret;
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.xs.traversers.LargeContainer
 * JD-Core Version:    0.6.2
 */