/*     */ package javax.swing.event;
/*     */ 
/*     */ import java.util.EventObject;
/*     */ import javax.swing.RowSorter;
/*     */ 
/*     */ public class RowSorterEvent extends EventObject
/*     */ {
/*     */   private Type type;
/*     */   private int[] oldViewToModel;
/*     */ 
/*     */   public RowSorterEvent(RowSorter paramRowSorter)
/*     */   {
/*  74 */     this(paramRowSorter, Type.SORT_ORDER_CHANGED, null);
/*     */   }
/*     */ 
/*     */   public RowSorterEvent(RowSorter paramRowSorter, Type paramType, int[] paramArrayOfInt)
/*     */   {
/*  89 */     super(paramRowSorter);
/*  90 */     if (paramType == null) {
/*  91 */       throw new IllegalArgumentException("type must be non-null");
/*     */     }
/*  93 */     this.type = paramType;
/*  94 */     this.oldViewToModel = paramArrayOfInt;
/*     */   }
/*     */ 
/*     */   public RowSorter getSource()
/*     */   {
/* 103 */     return (RowSorter)super.getSource();
/*     */   }
/*     */ 
/*     */   public Type getType()
/*     */   {
/* 112 */     return this.type;
/*     */   }
/*     */ 
/*     */   public int convertPreviousRowIndexToModel(int paramInt)
/*     */   {
/* 127 */     if ((this.oldViewToModel != null) && (paramInt >= 0) && (paramInt < this.oldViewToModel.length))
/*     */     {
/* 129 */       return this.oldViewToModel[paramInt];
/*     */     }
/* 131 */     return -1;
/*     */   }
/*     */ 
/*     */   public int getPreviousRowCount()
/*     */   {
/* 142 */     return this.oldViewToModel == null ? 0 : this.oldViewToModel.length;
/*     */   }
/*     */ 
/*     */   public static enum Type
/*     */   {
/*  56 */     SORT_ORDER_CHANGED, 
/*     */ 
/*  62 */     SORTED;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.event.RowSorterEvent
 * JD-Core Version:    0.6.2
 */