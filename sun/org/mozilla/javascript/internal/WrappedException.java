/*    */ package sun.org.mozilla.javascript.internal;
/*    */ 
/*    */ public class WrappedException extends EvaluatorException
/*    */ {
/*    */   static final long serialVersionUID = -1551979216966520648L;
/*    */   private Throwable exception;
/*    */ 
/*    */   public WrappedException(Throwable paramThrowable)
/*    */   {
/* 58 */     super("Wrapped " + paramThrowable.toString());
/* 59 */     this.exception = paramThrowable;
/* 60 */     Kit.initCause(this, paramThrowable);
/*    */ 
/* 62 */     int[] arrayOfInt = { 0 };
/* 63 */     String str = Context.getSourcePositionFromStack(arrayOfInt);
/* 64 */     int i = arrayOfInt[0];
/* 65 */     if (str != null) {
/* 66 */       initSourceName(str);
/*    */     }
/* 68 */     if (i != 0)
/* 69 */       initLineNumber(i);
/*    */   }
/*    */ 
/*    */   public Throwable getWrappedException()
/*    */   {
/* 81 */     return this.exception;
/*    */   }
/*    */ 
/*    */   /** @deprecated */
/*    */   public Object unwrap()
/*    */   {
/* 89 */     return getWrappedException();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.WrappedException
 * JD-Core Version:    0.6.2
 */