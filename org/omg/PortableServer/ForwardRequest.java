/*    */ package org.omg.PortableServer;
/*    */ 
/*    */ import org.omg.CORBA.Object;
/*    */ import org.omg.CORBA.UserException;
/*    */ 
/*    */ public final class ForwardRequest extends UserException
/*    */ {
/* 13 */   public Object forward_reference = null;
/*    */ 
/*    */   public ForwardRequest()
/*    */   {
/* 17 */     super(ForwardRequestHelper.id());
/*    */   }
/*    */ 
/*    */   public ForwardRequest(Object paramObject)
/*    */   {
/* 22 */     super(ForwardRequestHelper.id());
/* 23 */     this.forward_reference = paramObject;
/*    */   }
/*    */ 
/*    */   public ForwardRequest(String paramString, Object paramObject)
/*    */   {
/* 29 */     super(ForwardRequestHelper.id() + "  " + paramString);
/* 30 */     this.forward_reference = paramObject;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.PortableServer.ForwardRequest
 * JD-Core Version:    0.6.2
 */