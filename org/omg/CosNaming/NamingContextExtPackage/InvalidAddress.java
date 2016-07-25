/*    */ package org.omg.CosNaming.NamingContextExtPackage;
/*    */ 
/*    */ import org.omg.CORBA.UserException;
/*    */ 
/*    */ public final class InvalidAddress extends UserException
/*    */ {
/*    */   public InvalidAddress()
/*    */   {
/* 16 */     super(InvalidAddressHelper.id());
/*    */   }
/*    */ 
/*    */   public InvalidAddress(String paramString)
/*    */   {
/* 22 */     super(InvalidAddressHelper.id() + "  " + paramString);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CosNaming.NamingContextExtPackage.InvalidAddress
 * JD-Core Version:    0.6.2
 */