/*    */ package org.omg.CORBA_2_3;
/*    */ 
/*    */ import org.omg.CORBA.BAD_PARAM;
/*    */ import org.omg.CORBA.NO_IMPLEMENT;
/*    */ import org.omg.CORBA.portable.ValueFactory;
/*    */ 
/*    */ public abstract class ORB extends org.omg.CORBA.ORB
/*    */ {
/*    */   public ValueFactory register_value_factory(String paramString, ValueFactory paramValueFactory)
/*    */   {
/* 46 */     throw new NO_IMPLEMENT();
/*    */   }
/*    */ 
/*    */   public void unregister_value_factory(String paramString)
/*    */   {
/* 55 */     throw new NO_IMPLEMENT();
/*    */   }
/*    */ 
/*    */   public ValueFactory lookup_value_factory(String paramString)
/*    */   {
/* 64 */     throw new NO_IMPLEMENT();
/*    */   }
/*    */ 
/*    */   public org.omg.CORBA.Object get_value_def(String paramString)
/*    */     throws BAD_PARAM
/*    */   {
/* 76 */     throw new NO_IMPLEMENT();
/*    */   }
/*    */ 
/*    */   public void set_delegate(java.lang.Object paramObject)
/*    */   {
/* 85 */     throw new NO_IMPLEMENT();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA_2_3.ORB
 * JD-Core Version:    0.6.2
 */