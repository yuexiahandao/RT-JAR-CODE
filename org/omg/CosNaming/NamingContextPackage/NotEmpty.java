/*    */ package org.omg.CosNaming.NamingContextPackage;
/*    */ 
/*    */ import org.omg.CORBA.UserException;
/*    */ 
/*    */ public final class NotEmpty extends UserException
/*    */ {
/*    */   public NotEmpty()
/*    */   {
/* 16 */     super(NotEmptyHelper.id());
/*    */   }
/*    */ 
/*    */   public NotEmpty(String paramString)
/*    */   {
/* 22 */     super(NotEmptyHelper.id() + "  " + paramString);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CosNaming.NamingContextPackage.NotEmpty
 * JD-Core Version:    0.6.2
 */