/*    */ package sun.org.mozilla.javascript.internal;
/*    */ 
/*    */ public abstract class Ref
/*    */ {
/*    */   public boolean has(Context paramContext)
/*    */   {
/* 49 */     return true;
/*    */   }
/*    */ 
/*    */   public abstract Object get(Context paramContext);
/*    */ 
/*    */   public abstract Object set(Context paramContext, Object paramObject);
/*    */ 
/*    */   public boolean delete(Context paramContext)
/*    */   {
/* 58 */     return false;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.Ref
 * JD-Core Version:    0.6.2
 */