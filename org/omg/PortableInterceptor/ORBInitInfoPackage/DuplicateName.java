/*    */ package org.omg.PortableInterceptor.ORBInitInfoPackage;
/*    */ 
/*    */ import org.omg.CORBA.UserException;
/*    */ 
/*    */ public final class DuplicateName extends UserException
/*    */ {
/* 17 */   public String name = null;
/*    */ 
/*    */   public DuplicateName()
/*    */   {
/* 21 */     super(DuplicateNameHelper.id());
/*    */   }
/*    */ 
/*    */   public DuplicateName(String paramString)
/*    */   {
/* 26 */     super(DuplicateNameHelper.id());
/* 27 */     this.name = paramString;
/*    */   }
/*    */ 
/*    */   public DuplicateName(String paramString1, String paramString2)
/*    */   {
/* 33 */     super(DuplicateNameHelper.id() + "  " + paramString1);
/* 34 */     this.name = paramString2;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.PortableInterceptor.ORBInitInfoPackage.DuplicateName
 * JD-Core Version:    0.6.2
 */