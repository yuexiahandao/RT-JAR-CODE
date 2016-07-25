/*    */ package org.omg.CosNaming.NamingContextPackage;
/*    */ 
/*    */ import org.omg.CORBA.UserException;
/*    */ import org.omg.CosNaming.NameComponent;
/*    */ 
/*    */ public final class NotFound extends UserException
/*    */ {
/* 13 */   public NotFoundReason why = null;
/* 14 */   public NameComponent[] rest_of_name = null;
/*    */ 
/*    */   public NotFound()
/*    */   {
/* 18 */     super(NotFoundHelper.id());
/*    */   }
/*    */ 
/*    */   public NotFound(NotFoundReason paramNotFoundReason, NameComponent[] paramArrayOfNameComponent)
/*    */   {
/* 23 */     super(NotFoundHelper.id());
/* 24 */     this.why = paramNotFoundReason;
/* 25 */     this.rest_of_name = paramArrayOfNameComponent;
/*    */   }
/*    */ 
/*    */   public NotFound(String paramString, NotFoundReason paramNotFoundReason, NameComponent[] paramArrayOfNameComponent)
/*    */   {
/* 31 */     super(NotFoundHelper.id() + "  " + paramString);
/* 32 */     this.why = paramNotFoundReason;
/* 33 */     this.rest_of_name = paramArrayOfNameComponent;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CosNaming.NamingContextPackage.NotFound
 * JD-Core Version:    0.6.2
 */