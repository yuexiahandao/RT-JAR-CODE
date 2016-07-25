/*     */ package javax.swing.event;
/*     */ 
/*     */ import java.util.EventObject;
/*     */ import javax.swing.table.TableModel;
/*     */ 
/*     */ public class TableModelEvent extends EventObject
/*     */ {
/*     */   public static final int INSERT = 1;
/*     */   public static final int UPDATE = 0;
/*     */   public static final int DELETE = -1;
/*     */   public static final int HEADER_ROW = -1;
/*     */   public static final int ALL_COLUMNS = -1;
/*     */   protected int type;
/*     */   protected int firstRow;
/*     */   protected int lastRow;
/*     */   protected int column;
/*     */ 
/*     */   public TableModelEvent(TableModel paramTableModel)
/*     */   {
/* 107 */     this(paramTableModel, 0, 2147483647, -1, 0);
/*     */   }
/*     */ 
/*     */   public TableModelEvent(TableModel paramTableModel, int paramInt)
/*     */   {
/* 121 */     this(paramTableModel, paramInt, paramInt, -1, 0);
/*     */   }
/*     */ 
/*     */   public TableModelEvent(TableModel paramTableModel, int paramInt1, int paramInt2)
/*     */   {
/* 128 */     this(paramTableModel, paramInt1, paramInt2, -1, 0);
/*     */   }
/*     */ 
/*     */   public TableModelEvent(TableModel paramTableModel, int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/* 136 */     this(paramTableModel, paramInt1, paramInt2, paramInt3, 0);
/*     */   }
/*     */ 
/*     */   public TableModelEvent(TableModel paramTableModel, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 148 */     super(paramTableModel);
/* 149 */     this.firstRow = paramInt1;
/* 150 */     this.lastRow = paramInt2;
/* 151 */     this.column = paramInt3;
/* 152 */     this.type = paramInt4;
/*     */   }
/*     */ 
/*     */   public int getFirstRow()
/*     */   {
/* 162 */     return this.firstRow;
/*     */   }
/*     */   public int getLastRow() {
/* 165 */     return this.lastRow;
/*     */   }
/*     */ 
/*     */   public int getColumn()
/*     */   {
/* 172 */     return this.column;
/*     */   }
/*     */ 
/*     */   public int getType()
/*     */   {
/* 177 */     return this.type;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.event.TableModelEvent
 * JD-Core Version:    0.6.2
 */