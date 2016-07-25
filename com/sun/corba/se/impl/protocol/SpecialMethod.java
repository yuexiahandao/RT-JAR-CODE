/*    */ package com.sun.corba.se.impl.protocol;
/*    */ 
/*    */ import com.sun.corba.se.spi.oa.ObjectAdapter;
/*    */ import com.sun.corba.se.spi.protocol.CorbaMessageMediator;
/*    */ 
/*    */ public abstract class SpecialMethod
/*    */ {
/* 63 */   static SpecialMethod[] methods = { new IsA(), new GetInterface(), new NonExistent(), new NotExistent() };
/*    */ 
/*    */   public abstract boolean isNonExistentMethod();
/*    */ 
/*    */   public abstract String getName();
/*    */ 
/*    */   public abstract CorbaMessageMediator invoke(Object paramObject, CorbaMessageMediator paramCorbaMessageMediator, byte[] paramArrayOfByte, ObjectAdapter paramObjectAdapter);
/*    */ 
/*    */   public static final SpecialMethod getSpecialMethod(String paramString)
/*    */   {
/* 57 */     for (int i = 0; i < methods.length; i++)
/* 58 */       if (methods[i].getName().equals(paramString))
/* 59 */         return methods[i];
/* 60 */     return null;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.protocol.SpecialMethod
 * JD-Core Version:    0.6.2
 */