/*      */ package com.sun.corba.se.impl.orb;
/*      */ 
/*      */ class SynchVariable
/*      */ {
/*      */   public boolean _flag;
/*      */ 
/*      */   SynchVariable()
/*      */   {
/* 2106 */     this._flag = false;
/*      */   }
/*      */ 
/*      */   public void set()
/*      */   {
/* 2112 */     this._flag = true;
/*      */   }
/*      */ 
/*      */   public boolean value()
/*      */   {
/* 2118 */     return this._flag;
/*      */   }
/*      */ 
/*      */   public void reset()
/*      */   {
/* 2124 */     this._flag = false;
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.orb.SynchVariable
 * JD-Core Version:    0.6.2
 */