/*    */ package com.sun.corba.se.impl.ior.iiop;
/*    */ 
/*    */ import com.sun.corba.se.impl.logging.ORBUtilSystemException;
/*    */ import com.sun.corba.se.spi.ior.TaggedComponentBase;
/*    */ import com.sun.corba.se.spi.ior.iiop.RequestPartitioningComponent;
/*    */ import org.omg.CORBA_2_3.portable.OutputStream;
/*    */ 
/*    */ public class RequestPartitioningComponentImpl extends TaggedComponentBase
/*    */   implements RequestPartitioningComponent
/*    */ {
/* 43 */   private static ORBUtilSystemException wrapper = ORBUtilSystemException.get("oa.ior");
/*    */   private int partitionToUse;
/*    */ 
/*    */   public boolean equals(Object paramObject)
/*    */   {
/* 50 */     if (!(paramObject instanceof RequestPartitioningComponentImpl)) {
/* 51 */       return false;
/*    */     }
/* 53 */     RequestPartitioningComponentImpl localRequestPartitioningComponentImpl = (RequestPartitioningComponentImpl)paramObject;
/*    */ 
/* 56 */     return this.partitionToUse == localRequestPartitioningComponentImpl.partitionToUse;
/*    */   }
/*    */ 
/*    */   public int hashCode()
/*    */   {
/* 61 */     return this.partitionToUse;
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 66 */     return "RequestPartitioningComponentImpl[partitionToUse=" + this.partitionToUse + "]";
/*    */   }
/*    */ 
/*    */   public RequestPartitioningComponentImpl()
/*    */   {
/* 71 */     this.partitionToUse = 0;
/*    */   }
/*    */ 
/*    */   public RequestPartitioningComponentImpl(int paramInt) {
/* 75 */     if ((paramInt < 0) || (paramInt > 63))
/*    */     {
/* 77 */       throw wrapper.invalidRequestPartitioningComponentValue(new Integer(paramInt), new Integer(0), new Integer(63));
/*    */     }
/*    */ 
/* 82 */     this.partitionToUse = paramInt;
/*    */   }
/*    */ 
/*    */   public int getRequestPartitioningId()
/*    */   {
/* 87 */     return this.partitionToUse;
/*    */   }
/*    */ 
/*    */   public void writeContents(OutputStream paramOutputStream)
/*    */   {
/* 92 */     paramOutputStream.write_ulong(this.partitionToUse);
/*    */   }
/*    */ 
/*    */   public int getId()
/*    */   {
/* 97 */     return 1398099457;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.ior.iiop.RequestPartitioningComponentImpl
 * JD-Core Version:    0.6.2
 */