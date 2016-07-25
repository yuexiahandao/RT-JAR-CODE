/*    */ package java.rmi;
/*    */ 
/*    */ public class MarshalException extends RemoteException
/*    */ {
/*    */   private static final long serialVersionUID = 6223554758134037936L;
/*    */ 
/*    */   public MarshalException(String paramString)
/*    */   {
/* 57 */     super(paramString);
/*    */   }
/*    */ 
/*    */   public MarshalException(String paramString, Exception paramException)
/*    */   {
/* 69 */     super(paramString, paramException);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.rmi.MarshalException
 * JD-Core Version:    0.6.2
 */