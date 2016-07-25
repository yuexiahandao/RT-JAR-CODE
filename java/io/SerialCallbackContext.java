/*    */ package java.io;
/*    */ 
/*    */ final class SerialCallbackContext
/*    */ {
/*    */   private final Object obj;
/*    */   private final ObjectStreamClass desc;
/*    */   private Thread thread;
/*    */ 
/*    */   public SerialCallbackContext(Object paramObject, ObjectStreamClass paramObjectStreamClass)
/*    */   {
/* 49 */     this.obj = paramObject;
/* 50 */     this.desc = paramObjectStreamClass;
/* 51 */     this.thread = Thread.currentThread();
/*    */   }
/*    */ 
/*    */   public Object getObj() throws NotActiveException {
/* 55 */     checkAndSetUsed();
/* 56 */     return this.obj;
/*    */   }
/*    */ 
/*    */   public ObjectStreamClass getDesc() {
/* 60 */     return this.desc;
/*    */   }
/*    */ 
/*    */   private void checkAndSetUsed() throws NotActiveException {
/* 64 */     if (this.thread != Thread.currentThread()) {
/* 65 */       throw new NotActiveException("not in readObject invocation or fields already read");
/*    */     }
/*    */ 
/* 68 */     this.thread = null;
/*    */   }
/*    */ 
/*    */   public void setUsed() {
/* 72 */     this.thread = null;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.io.SerialCallbackContext
 * JD-Core Version:    0.6.2
 */