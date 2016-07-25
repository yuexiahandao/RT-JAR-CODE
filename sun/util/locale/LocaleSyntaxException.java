/*    */ package sun.util.locale;
/*    */ 
/*    */ public class LocaleSyntaxException extends Exception
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/* 38 */   private int index = -1;
/*    */ 
/*    */   public LocaleSyntaxException(String paramString) {
/* 41 */     this(paramString, 0);
/*    */   }
/*    */ 
/*    */   public LocaleSyntaxException(String paramString, int paramInt) {
/* 45 */     super(paramString);
/* 46 */     this.index = paramInt;
/*    */   }
/*    */ 
/*    */   public int getErrorIndex() {
/* 50 */     return this.index;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.util.locale.LocaleSyntaxException
 * JD-Core Version:    0.6.2
 */