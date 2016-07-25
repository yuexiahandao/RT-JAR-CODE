/*    */ package java.rmi.dgc;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ 
/*    */ public final class Lease
/*    */   implements Serializable
/*    */ {
/*    */   private VMID vmid;
/*    */   private long value;
/*    */   private static final long serialVersionUID = -5713411624328831948L;
/*    */ 
/*    */   public Lease(VMID paramVMID, long paramLong)
/*    */   {
/* 56 */     this.vmid = paramVMID;
/* 57 */     this.value = paramLong;
/*    */   }
/*    */ 
/*    */   public VMID getVMID()
/*    */   {
/* 66 */     return this.vmid;
/*    */   }
/*    */ 
/*    */   public long getValue()
/*    */   {
/* 75 */     return this.value;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.rmi.dgc.Lease
 * JD-Core Version:    0.6.2
 */