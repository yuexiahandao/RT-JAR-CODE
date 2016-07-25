/*    */ package com.sun.corba.se.impl.copyobject;
/*    */ 
/*    */ import com.sun.corba.se.spi.copyobject.ObjectCopier;
/*    */ import com.sun.corba.se.spi.copyobject.ReflectiveCopyException;
/*    */ 
/*    */ public class FallbackObjectCopierImpl
/*    */   implements ObjectCopier
/*    */ {
/*    */   private ObjectCopier first;
/*    */   private ObjectCopier second;
/*    */ 
/*    */   public FallbackObjectCopierImpl(ObjectCopier paramObjectCopier1, ObjectCopier paramObjectCopier2)
/*    */   {
/* 42 */     this.first = paramObjectCopier1;
/* 43 */     this.second = paramObjectCopier2;
/*    */   }
/*    */ 
/*    */   public Object copy(Object paramObject) throws ReflectiveCopyException
/*    */   {
/*    */     try {
/* 49 */       return this.first.copy(paramObject);
/*    */     } catch (ReflectiveCopyException localReflectiveCopyException) {
/*    */     }
/* 52 */     return this.second.copy(paramObject);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.copyobject.FallbackObjectCopierImpl
 * JD-Core Version:    0.6.2
 */