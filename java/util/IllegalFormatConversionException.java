/*    */ package java.util;
/*    */ 
/*    */ public class IllegalFormatConversionException extends IllegalFormatException
/*    */ {
/*    */   private static final long serialVersionUID = 17000126L;
/*    */   private char c;
/*    */   private Class arg;
/*    */ 
/*    */   public IllegalFormatConversionException(char paramChar, Class<?> paramClass)
/*    */   {
/* 56 */     if (paramClass == null)
/* 57 */       throw new NullPointerException();
/* 58 */     this.c = paramChar;
/* 59 */     this.arg = paramClass;
/*    */   }
/*    */ 
/*    */   public char getConversion()
/*    */   {
/* 68 */     return this.c;
/*    */   }
/*    */ 
/*    */   public Class<?> getArgumentClass()
/*    */   {
/* 77 */     return this.arg;
/*    */   }
/*    */ 
/*    */   public String getMessage()
/*    */   {
/* 82 */     return String.format("%c != %s", new Object[] { Character.valueOf(this.c), this.arg.getName() });
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.IllegalFormatConversionException
 * JD-Core Version:    0.6.2
 */