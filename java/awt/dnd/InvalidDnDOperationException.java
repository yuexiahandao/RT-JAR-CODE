/*    */ package java.awt.dnd;
/*    */ 
/*    */ public class InvalidDnDOperationException extends IllegalStateException
/*    */ {
/* 39 */   private static String dft_msg = "The operation requested cannot be performed by the DnD system since it is not in the appropriate state";
/*    */ 
/*    */   public InvalidDnDOperationException()
/*    */   {
/* 45 */     super(dft_msg);
/*    */   }
/*    */ 
/*    */   public InvalidDnDOperationException(String paramString)
/*    */   {
/* 53 */     super(paramString);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.dnd.InvalidDnDOperationException
 * JD-Core Version:    0.6.2
 */