/*    */ package java.rmi;
/*    */ 
/*    */ public class UnknownHostException extends RemoteException
/*    */ {
/*    */   private static final long serialVersionUID = -8152710247442114228L;
/*    */ 
/*    */   public UnknownHostException(String paramString)
/*    */   {
/* 48 */     super(paramString);
/*    */   }
/*    */ 
/*    */   public UnknownHostException(String paramString, Exception paramException)
/*    */   {
/* 60 */     super(paramString, paramException);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.rmi.UnknownHostException
 * JD-Core Version:    0.6.2
 */