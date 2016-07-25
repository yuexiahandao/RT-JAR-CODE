/*    */ package javax.tools;
/*    */ 
/*    */ import java.util.Locale;
/*    */ 
/*    */ public abstract interface Diagnostic<S>
/*    */ {
/*    */   public static final long NOPOS = -1L;
/*    */ 
/*    */   public abstract Kind getKind();
/*    */ 
/*    */   public abstract S getSource();
/*    */ 
/*    */   public abstract long getPosition();
/*    */ 
/*    */   public abstract long getStartPosition();
/*    */ 
/*    */   public abstract long getEndPosition();
/*    */ 
/*    */   public abstract long getLineNumber();
/*    */ 
/*    */   public abstract long getColumnNumber();
/*    */ 
/*    */   public abstract String getCode();
/*    */ 
/*    */   public abstract String getMessage(Locale paramLocale);
/*    */ 
/*    */   public static enum Kind
/*    */   {
/* 57 */     ERROR, 
/*    */ 
/* 62 */     WARNING, 
/*    */ 
/* 69 */     MANDATORY_WARNING, 
/*    */ 
/* 73 */     NOTE, 
/*    */ 
/* 77 */     OTHER;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.tools.Diagnostic
 * JD-Core Version:    0.6.2
 */