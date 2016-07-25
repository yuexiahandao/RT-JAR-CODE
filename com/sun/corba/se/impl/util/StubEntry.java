/*      */ package com.sun.corba.se.impl.util;
/*      */ 
/*      */ class StubEntry
/*      */ {
/*      */   org.omg.CORBA.Object stub;
/*      */   boolean mostDerived;
/*      */ 
/*      */   StubEntry(org.omg.CORBA.Object paramObject, boolean paramBoolean)
/*      */   {
/*  999 */     this.stub = paramObject;
/* 1000 */     this.mostDerived = paramBoolean;
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.util.StubEntry
 * JD-Core Version:    0.6.2
 */