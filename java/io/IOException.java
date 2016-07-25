/*    */ package java.io;
/*    */ 
/*    */ public class IOException extends Exception
/*    */ {
/*    */   static final long serialVersionUID = 7818375828146090155L;
/*    */ 
/*    */   public IOException()
/*    */   {
/*    */   }
/*    */ 
/*    */   public IOException(String paramString)
/*    */   {
/* 58 */     super(paramString);
/*    */   }
/*    */ 
/*    */   public IOException(String paramString, Throwable paramThrowable)
/*    */   {
/* 81 */     super(paramString, paramThrowable);
/*    */   }
/*    */ 
/*    */   public IOException(Throwable paramThrowable)
/*    */   {
/* 99 */     super(paramThrowable);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.io.IOException
 * JD-Core Version:    0.6.2
 */