/*    */ package org.omg.PortableServer.CurrentPackage;
/*    */ 
/*    */ import org.omg.CORBA.UserException;
/*    */ 
/*    */ public final class NoContext extends UserException
/*    */ {
/*    */   public NoContext()
/*    */   {
/* 16 */     super(NoContextHelper.id());
/*    */   }
/*    */ 
/*    */   public NoContext(String paramString)
/*    */   {
/* 22 */     super(NoContextHelper.id() + "  " + paramString);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.PortableServer.CurrentPackage.NoContext
 * JD-Core Version:    0.6.2
 */