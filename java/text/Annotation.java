/*    */ package java.text;
/*    */ 
/*    */ public class Annotation
/*    */ {
/*    */   private Object value;
/*    */ 
/*    */   public Annotation(Object paramObject)
/*    */   {
/* 65 */     this.value = paramObject;
/*    */   }
/*    */ 
/*    */   public Object getValue()
/*    */   {
/* 72 */     return this.value;
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 79 */     return getClass().getName() + "[value=" + this.value + "]";
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.text.Annotation
 * JD-Core Version:    0.6.2
 */