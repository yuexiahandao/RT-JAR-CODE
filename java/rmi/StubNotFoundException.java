/*    */ package java.rmi;
/*    */ 
/*    */ public class StubNotFoundException extends RemoteException
/*    */ {
/*    */   private static final long serialVersionUID = -7088199405468872373L;
/*    */ 
/*    */   public StubNotFoundException(String paramString)
/*    */   {
/* 53 */     super(paramString);
/*    */   }
/*    */ 
/*    */   public StubNotFoundException(String paramString, Exception paramException)
/*    */   {
/* 65 */     super(paramString, paramException);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.rmi.StubNotFoundException
 * JD-Core Version:    0.6.2
 */