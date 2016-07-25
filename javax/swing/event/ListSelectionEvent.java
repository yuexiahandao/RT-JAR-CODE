/*     */ package javax.swing.event;
/*     */ 
/*     */ import java.util.EventObject;
/*     */ 
/*     */ public class ListSelectionEvent extends EventObject
/*     */ {
/*     */   private int firstIndex;
/*     */   private int lastIndex;
/*     */   private boolean isAdjusting;
/*     */ 
/*     */   public ListSelectionEvent(Object paramObject, int paramInt1, int paramInt2, boolean paramBoolean)
/*     */   {
/*  73 */     super(paramObject);
/*  74 */     this.firstIndex = paramInt1;
/*  75 */     this.lastIndex = paramInt2;
/*  76 */     this.isAdjusting = paramBoolean;
/*     */   }
/*     */ 
/*     */   public int getFirstIndex()
/*     */   {
/*  86 */     return this.firstIndex;
/*     */   }
/*     */ 
/*     */   public int getLastIndex()
/*     */   {
/*  95 */     return this.lastIndex;
/*     */   }
/*     */ 
/*     */   public boolean getValueIsAdjusting()
/*     */   {
/* 106 */     return this.isAdjusting;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 115 */     String str = " source=" + getSource() + " firstIndex= " + this.firstIndex + " lastIndex= " + this.lastIndex + " isAdjusting= " + this.isAdjusting + " ";
/*     */ 
/* 121 */     return getClass().getName() + "[" + str + "]";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.event.ListSelectionEvent
 * JD-Core Version:    0.6.2
 */