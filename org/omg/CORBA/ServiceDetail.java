/*    */ package org.omg.CORBA;
/*    */ 
/*    */ import org.omg.CORBA.portable.IDLEntity;
/*    */ 
/*    */ public final class ServiceDetail
/*    */   implements IDLEntity
/*    */ {
/*    */   public int service_detail_type;
/*    */   public byte[] service_detail;
/*    */ 
/*    */   public ServiceDetail()
/*    */   {
/*    */   }
/*    */ 
/*    */   public ServiceDetail(int paramInt, byte[] paramArrayOfByte)
/*    */   {
/* 65 */     this.service_detail_type = paramInt;
/* 66 */     this.service_detail = paramArrayOfByte;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.ServiceDetail
 * JD-Core Version:    0.6.2
 */