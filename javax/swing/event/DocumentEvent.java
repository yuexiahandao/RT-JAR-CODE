/*     */ package javax.swing.event;
/*     */ 
/*     */ import javax.swing.text.Document;
/*     */ import javax.swing.text.Element;
/*     */ 
/*     */ public abstract interface DocumentEvent
/*     */ {
/*     */   public abstract int getOffset();
/*     */ 
/*     */   public abstract int getLength();
/*     */ 
/*     */   public abstract Document getDocument();
/*     */ 
/*     */   public abstract EventType getType();
/*     */ 
/*     */   public abstract ElementChange getChange(Element paramElement);
/*     */ 
/*     */   public static abstract interface ElementChange
/*     */   {
/*     */     public abstract Element getElement();
/*     */ 
/*     */     public abstract int getIndex();
/*     */ 
/*     */     public abstract Element[] getChildrenRemoved();
/*     */ 
/*     */     public abstract Element[] getChildrenAdded();
/*     */   }
/*     */ 
/*     */   public static final class EventType
/*     */   {
/* 116 */     public static final EventType INSERT = new EventType("INSERT");
/*     */ 
/* 121 */     public static final EventType REMOVE = new EventType("REMOVE");
/*     */ 
/* 126 */     public static final EventType CHANGE = new EventType("CHANGE");
/*     */     private String typeString;
/*     */ 
/*     */     private EventType(String paramString)
/*     */     {
/* 110 */       this.typeString = paramString;
/*     */     }
/*     */ 
/*     */     public String toString()
/*     */     {
/* 134 */       return this.typeString;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.event.DocumentEvent
 * JD-Core Version:    0.6.2
 */