/*    */ package java.util;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ 
/*    */ public class EventObject
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 5516075349620653480L;
/*    */   protected transient Object source;
/*    */ 
/*    */   public EventObject(Object paramObject)
/*    */   {
/* 55 */     if (paramObject == null) {
/* 56 */       throw new IllegalArgumentException("null source");
/*    */     }
/* 58 */     this.source = paramObject;
/*    */   }
/*    */ 
/*    */   public Object getSource()
/*    */   {
/* 67 */     return this.source;
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 76 */     return getClass().getName() + "[source=" + this.source + "]";
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.EventObject
 * JD-Core Version:    0.6.2
 */