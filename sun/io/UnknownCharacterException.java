/*    */ package sun.io;
/*    */ 
/*    */ import java.io.CharConversionException;
/*    */ 
/*    */ @Deprecated
/*    */ public class UnknownCharacterException extends CharConversionException
/*    */ {
/*    */   private static final long serialVersionUID = -8563196502398436986L;
/*    */ 
/*    */   public UnknownCharacterException()
/*    */   {
/*    */   }
/*    */ 
/*    */   public UnknownCharacterException(String paramString)
/*    */   {
/* 58 */     super(paramString);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.io.UnknownCharacterException
 * JD-Core Version:    0.6.2
 */