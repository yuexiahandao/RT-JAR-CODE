/*    */ package org.omg.CORBA;
/*    */ 
/*    */ import org.omg.CORBA.portable.IDLEntity;
/*    */ 
/*    */ public final class ServiceInformation
/*    */   implements IDLEntity
/*    */ {
/*    */   public int[] service_options;
/*    */   public ServiceDetail[] service_details;
/*    */ 
/*    */   public ServiceInformation()
/*    */   {
/*    */   }
/*    */ 
/*    */   public ServiceInformation(int[] paramArrayOfInt, ServiceDetail[] paramArrayOfServiceDetail)
/*    */   {
/* 58 */     this.service_options = paramArrayOfInt;
/* 59 */     this.service_details = paramArrayOfServiceDetail;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.ServiceInformation
 * JD-Core Version:    0.6.2
 */