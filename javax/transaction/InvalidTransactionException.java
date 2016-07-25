/*    */ package javax.transaction;
/*    */ 
/*    */ import java.rmi.RemoteException;
/*    */ 
/*    */ public class InvalidTransactionException extends RemoteException
/*    */ {
/*    */   public InvalidTransactionException()
/*    */   {
/*    */   }
/*    */ 
/*    */   public InvalidTransactionException(String paramString)
/*    */   {
/* 48 */     super(paramString);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.transaction.InvalidTransactionException
 * JD-Core Version:    0.6.2
 */