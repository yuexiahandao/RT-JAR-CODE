/*    */ package org.omg.PortableInterceptor;
/*    */ 
/*    */ import org.omg.CORBA.Object;
/*    */ import org.omg.CORBA.UserException;
/*    */ 
/*    */ public final class ForwardRequest extends UserException
/*    */ {
/* 17 */   public Object forward = null;
/*    */ 
/*    */   public ForwardRequest()
/*    */   {
/* 21 */     super(ForwardRequestHelper.id());
/*    */   }
/*    */ 
/*    */   public ForwardRequest(Object paramObject)
/*    */   {
/* 26 */     super(ForwardRequestHelper.id());
/* 27 */     this.forward = paramObject;
/*    */   }
/*    */ 
/*    */   public ForwardRequest(String paramString, Object paramObject)
/*    */   {
/* 33 */     super(ForwardRequestHelper.id() + "  " + paramString);
/* 34 */     this.forward = paramObject;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.PortableInterceptor.ForwardRequest
 * JD-Core Version:    0.6.2
 */