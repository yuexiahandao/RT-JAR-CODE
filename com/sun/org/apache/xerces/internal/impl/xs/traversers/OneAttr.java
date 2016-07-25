/*      */ package com.sun.org.apache.xerces.internal.impl.xs.traversers;
/*      */ 
/*      */ class OneAttr
/*      */ {
/*      */   public String name;
/*      */   public int dvIndex;
/*      */   public int valueIndex;
/*      */   public Object dfltValue;
/*      */ 
/*      */   public OneAttr(String name, int dvIndex, int valueIndex, Object dfltValue)
/*      */   {
/* 1770 */     this.name = name;
/* 1771 */     this.dvIndex = dvIndex;
/* 1772 */     this.valueIndex = valueIndex;
/* 1773 */     this.dfltValue = dfltValue;
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.xs.traversers.OneAttr
 * JD-Core Version:    0.6.2
 */