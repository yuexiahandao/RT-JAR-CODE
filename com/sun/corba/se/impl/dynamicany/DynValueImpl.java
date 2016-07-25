/*    */ package com.sun.corba.se.impl.dynamicany;
/*    */ 
/*    */ import com.sun.corba.se.spi.orb.ORB;
/*    */ import org.omg.CORBA.Any;
/*    */ import org.omg.CORBA.TypeCode;
/*    */ import org.omg.DynamicAny.DynValue;
/*    */ 
/*    */ public class DynValueImpl extends DynValueCommonImpl
/*    */   implements DynValue
/*    */ {
/*    */   private DynValueImpl()
/*    */   {
/* 49 */     this(null, (Any)null, false);
/*    */   }
/*    */ 
/*    */   protected DynValueImpl(ORB paramORB, Any paramAny, boolean paramBoolean) {
/* 53 */     super(paramORB, paramAny, paramBoolean);
/*    */   }
/*    */ 
/*    */   protected DynValueImpl(ORB paramORB, TypeCode paramTypeCode) {
/* 57 */     super(paramORB, paramTypeCode);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.dynamicany.DynValueImpl
 * JD-Core Version:    0.6.2
 */