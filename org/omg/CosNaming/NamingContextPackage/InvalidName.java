/*    */ package org.omg.CosNaming.NamingContextPackage;
/*    */ 
/*    */ import org.omg.CORBA.UserException;
/*    */ 
/*    */ public final class InvalidName extends UserException
/*    */ {
/*    */   public InvalidName()
/*    */   {
/* 16 */     super(InvalidNameHelper.id());
/*    */   }
/*    */ 
/*    */   public InvalidName(String paramString)
/*    */   {
/* 22 */     super(InvalidNameHelper.id() + "  " + paramString);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CosNaming.NamingContextPackage.InvalidName
 * JD-Core Version:    0.6.2
 */