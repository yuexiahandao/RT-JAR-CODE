/*      */ package com.sun.org.apache.xerces.internal.impl.xs.traversers;
/*      */ 
/*      */ class SmallContainer extends Container
/*      */ {
/*      */   String[] keys;
/*      */ 
/*      */   SmallContainer(int size)
/*      */   {
/* 1795 */     this.keys = new String[size];
/* 1796 */     this.values = new OneAttr[size];
/*      */   }
/*      */   void put(String key, OneAttr value) {
/* 1799 */     this.keys[this.pos] = key;
/* 1800 */     this.values[(this.pos++)] = value;
/*      */   }
/*      */   OneAttr get(String key) {
/* 1803 */     for (int i = 0; i < this.pos; i++) {
/* 1804 */       if (this.keys[i].equals(key)) {
/* 1805 */         return this.values[i];
/*      */       }
/*      */     }
/* 1808 */     return null;
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.xs.traversers.SmallContainer
 * JD-Core Version:    0.6.2
 */