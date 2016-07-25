/*    */ package javax.transaction;
/*    */ 
/*    */ import java.rmi.RemoteException;
/*    */ 
/*    */ public class TransactionRequiredException extends RemoteException
/*    */ {
/*    */   public TransactionRequiredException()
/*    */   {
/*    */   }
/*    */ 
/*    */   public TransactionRequiredException(String paramString)
/*    */   {
/* 47 */     super(paramString);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.transaction.TransactionRequiredException
 * JD-Core Version:    0.6.2
 */