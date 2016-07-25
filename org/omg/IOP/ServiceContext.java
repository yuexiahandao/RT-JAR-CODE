/*    */ package org.omg.IOP;
/*    */ 
/*    */ import org.omg.CORBA.portable.IDLEntity;
/*    */ 
/*    */ public final class ServiceContext
/*    */   implements IDLEntity
/*    */ {
/* 15 */   public int context_id = 0;
/*    */ 
/* 18 */   public byte[] context_data = null;
/*    */ 
/*    */   public ServiceContext()
/*    */   {
/*    */   }
/*    */ 
/*    */   public ServiceContext(int paramInt, byte[] paramArrayOfByte)
/*    */   {
/* 26 */     this.context_id = paramInt;
/* 27 */     this.context_data = paramArrayOfByte;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.IOP.ServiceContext
 * JD-Core Version:    0.6.2
 */