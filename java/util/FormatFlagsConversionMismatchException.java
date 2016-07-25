/*    */ package java.util;
/*    */ 
/*    */ public class FormatFlagsConversionMismatchException extends IllegalFormatException
/*    */ {
/*    */   private static final long serialVersionUID = 19120414L;
/*    */   private String f;
/*    */   private char c;
/*    */ 
/*    */   public FormatFlagsConversionMismatchException(String paramString, char paramChar)
/*    */   {
/* 57 */     if (paramString == null)
/* 58 */       throw new NullPointerException();
/* 59 */     this.f = paramString;
/* 60 */     this.c = paramChar;
/*    */   }
/*    */ 
/*    */   public String getFlags()
/*    */   {
/* 69 */     return this.f;
/*    */   }
/*    */ 
/*    */   public char getConversion()
/*    */   {
/* 78 */     return this.c;
/*    */   }
/*    */ 
/*    */   public String getMessage() {
/* 82 */     return "Conversion = " + this.c + ", Flags = " + this.f;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.FormatFlagsConversionMismatchException
 * JD-Core Version:    0.6.2
 */