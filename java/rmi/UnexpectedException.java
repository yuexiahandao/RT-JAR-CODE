/*    */ package java.rmi;
/*    */ 
/*    */ public class UnexpectedException extends RemoteException
/*    */ {
/*    */   private static final long serialVersionUID = 1800467484195073863L;
/*    */ 
/*    */   public UnexpectedException(String paramString)
/*    */   {
/* 50 */     super(paramString);
/*    */   }
/*    */ 
/*    */   public UnexpectedException(String paramString, Exception paramException)
/*    */   {
/* 62 */     super(paramString, paramException);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.rmi.UnexpectedException
 * JD-Core Version:    0.6.2
 */