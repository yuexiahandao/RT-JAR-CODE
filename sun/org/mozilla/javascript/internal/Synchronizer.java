/*    */ package sun.org.mozilla.javascript.internal;
/*    */ 
/*    */ public class Synchronizer extends Delegator
/*    */ {
/*    */   private Object syncObject;
/*    */ 
/*    */   public Synchronizer(Scriptable paramScriptable)
/*    */   {
/* 70 */     super(paramScriptable);
/*    */   }
/*    */ 
/*    */   public Synchronizer(Scriptable paramScriptable, Object paramObject)
/*    */   {
/* 81 */     super(paramScriptable);
/* 82 */     this.syncObject = paramObject;
/*    */   }
/*    */ 
/*    */   public Object call(Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject)
/*    */   {
/* 92 */     Scriptable localScriptable = this.syncObject != null ? this.syncObject : paramScriptable2;
/* 93 */     synchronized ((localScriptable instanceof Wrapper) ? ((Wrapper)localScriptable).unwrap() : localScriptable) {
/* 94 */       return ((Function)this.obj).call(paramContext, paramScriptable1, paramScriptable2, paramArrayOfObject);
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.Synchronizer
 * JD-Core Version:    0.6.2
 */