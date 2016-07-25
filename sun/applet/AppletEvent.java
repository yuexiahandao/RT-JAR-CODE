/*    */ package sun.applet;
/*    */ 
/*    */ import java.util.EventObject;
/*    */ 
/*    */ public class AppletEvent extends EventObject
/*    */ {
/*    */   private Object arg;
/*    */   private int id;
/*    */ 
/*    */   public AppletEvent(Object paramObject1, int paramInt, Object paramObject2)
/*    */   {
/* 44 */     super(paramObject1);
/* 45 */     this.arg = paramObject2;
/* 46 */     this.id = paramInt;
/*    */   }
/*    */ 
/*    */   public int getID() {
/* 50 */     return this.id;
/*    */   }
/*    */ 
/*    */   public Object getArgument() {
/* 54 */     return this.arg;
/*    */   }
/*    */ 
/*    */   public String toString() {
/* 58 */     String str = getClass().getName() + "[source=" + this.source + " + id=" + this.id;
/* 59 */     if (this.arg != null) {
/* 60 */       str = str + " + arg=" + this.arg;
/*    */     }
/* 62 */     str = str + " ]";
/* 63 */     return str;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.applet.AppletEvent
 * JD-Core Version:    0.6.2
 */