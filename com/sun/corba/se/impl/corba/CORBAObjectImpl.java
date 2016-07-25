/*    */ package com.sun.corba.se.impl.corba;
/*    */ 
/*    */ import org.omg.CORBA_2_3.portable.ObjectImpl;
/*    */ 
/*    */ public class CORBAObjectImpl extends ObjectImpl
/*    */ {
/*    */   public String[] _ids()
/*    */   {
/* 39 */     String[] arrayOfString = new String[1];
/* 40 */     arrayOfString[0] = "IDL:omg.org/CORBA/Object:1.0";
/* 41 */     return arrayOfString;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.corba.CORBAObjectImpl
 * JD-Core Version:    0.6.2
 */