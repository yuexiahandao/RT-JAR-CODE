/*    */ package java.beans;
/*    */ 
/*    */ public class IndexedPropertyChangeEvent extends PropertyChangeEvent
/*    */ {
/*    */   private static final long serialVersionUID = -320227448495806870L;
/*    */   private int index;
/*    */ 
/*    */   public IndexedPropertyChangeEvent(Object paramObject1, String paramString, Object paramObject2, Object paramObject3, int paramInt)
/*    */   {
/* 61 */     super(paramObject1, paramString, paramObject2, paramObject3);
/* 62 */     this.index = paramInt;
/*    */   }
/*    */ 
/*    */   public int getIndex()
/*    */   {
/* 72 */     return this.index;
/*    */   }
/*    */ 
/*    */   void appendTo(StringBuilder paramStringBuilder) {
/* 76 */     paramStringBuilder.append("; index=").append(getIndex());
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.beans.IndexedPropertyChangeEvent
 * JD-Core Version:    0.6.2
 */