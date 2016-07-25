/*    */ package com.sun.corba.se.impl.protocol;
/*    */ 
/*    */ import com.sun.corba.se.spi.oa.NullServant;
/*    */ import com.sun.corba.se.spi.oa.ObjectAdapter;
/*    */ import com.sun.corba.se.spi.protocol.CorbaMessageMediator;
/*    */ import com.sun.corba.se.spi.protocol.CorbaProtocolHandler;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ 
/*    */ class NonExistent extends SpecialMethod
/*    */ {
/*    */   public boolean isNonExistentMethod()
/*    */   {
/* 74 */     return true;
/*    */   }
/*    */ 
/*    */   public String getName() {
/* 78 */     return "_non_existent";
/*    */   }
/*    */ 
/*    */   public CorbaMessageMediator invoke(Object paramObject, CorbaMessageMediator paramCorbaMessageMediator, byte[] paramArrayOfByte, ObjectAdapter paramObjectAdapter)
/*    */   {
/* 86 */     boolean bool = (paramObject == null) || ((paramObject instanceof NullServant));
/* 87 */     CorbaMessageMediator localCorbaMessageMediator = paramCorbaMessageMediator.getProtocolHandler().createResponse(paramCorbaMessageMediator, null);
/*    */ 
/* 89 */     ((OutputStream)localCorbaMessageMediator.getOutputObject()).write_boolean(bool);
/* 90 */     return localCorbaMessageMediator;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.protocol.NonExistent
 * JD-Core Version:    0.6.2
 */