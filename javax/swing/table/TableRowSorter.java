/*     */ package javax.swing.table;
/*     */ 
/*     */ import java.text.Collator;
/*     */ import java.util.Comparator;
/*     */ import javax.swing.DefaultRowSorter;
/*     */ import javax.swing.DefaultRowSorter.ModelWrapper;
/*     */ 
/*     */ public class TableRowSorter<M extends TableModel> extends DefaultRowSorter<M, Integer>
/*     */ {
/* 134 */   private static final Comparator COMPARABLE_COMPARATOR = new ComparableComparator(null);
/*     */   private M tableModel;
/*     */   private TableStringConverter stringConverter;
/*     */ 
/*     */   public TableRowSorter()
/*     */   {
/* 152 */     this(null);
/*     */   }
/*     */ 
/*     */   public TableRowSorter(M paramM)
/*     */   {
/* 163 */     setModel(paramM);
/*     */   }
/*     */ 
/*     */   public void setModel(M paramM)
/*     */   {
/* 174 */     this.tableModel = paramM;
/* 175 */     setModelWrapper(new TableRowSorterModelWrapper(null));
/*     */   }
/*     */ 
/*     */   public void setStringConverter(TableStringConverter paramTableStringConverter)
/*     */   {
/* 188 */     this.stringConverter = paramTableStringConverter;
/*     */   }
/*     */ 
/*     */   public TableStringConverter getStringConverter()
/*     */   {
/* 198 */     return this.stringConverter;
/*     */   }
/*     */ 
/*     */   public Comparator<?> getComparator(int paramInt)
/*     */   {
/* 217 */     Comparator localComparator = super.getComparator(paramInt);
/* 218 */     if (localComparator != null) {
/* 219 */       return localComparator;
/*     */     }
/* 221 */     Class localClass = ((TableModel)getModel()).getColumnClass(paramInt);
/* 222 */     if (localClass == String.class) {
/* 223 */       return Collator.getInstance();
/*     */     }
/* 225 */     if (Comparable.class.isAssignableFrom(localClass)) {
/* 226 */       return COMPARABLE_COMPARATOR;
/*     */     }
/* 228 */     return Collator.getInstance();
/*     */   }
/*     */ 
/*     */   protected boolean useToString(int paramInt)
/*     */   {
/* 237 */     Comparator localComparator = super.getComparator(paramInt);
/* 238 */     if (localComparator != null) {
/* 239 */       return false;
/*     */     }
/* 241 */     Class localClass = ((TableModel)getModel()).getColumnClass(paramInt);
/* 242 */     if (localClass == String.class) {
/* 243 */       return false;
/*     */     }
/* 245 */     if (Comparable.class.isAssignableFrom(localClass)) {
/* 246 */       return false;
/*     */     }
/* 248 */     return true;
/*     */   }
/*     */ 
/*     */   private static class ComparableComparator
/*     */     implements Comparator
/*     */   {
/*     */     public int compare(Object paramObject1, Object paramObject2)
/*     */     {
/* 305 */       return ((Comparable)paramObject1).compareTo(paramObject2);
/*     */     }
/*     */   }
/*     */ 
/*     */   private class TableRowSorterModelWrapper extends DefaultRowSorter.ModelWrapper<M, Integer>
/*     */   {
/*     */     private TableRowSorterModelWrapper()
/*     */     {
/*     */     }
/*     */ 
/*     */     public M getModel()
/*     */     {
/* 257 */       return TableRowSorter.this.tableModel;
/*     */     }
/*     */ 
/*     */     public int getColumnCount() {
/* 261 */       return TableRowSorter.this.tableModel == null ? 0 : TableRowSorter.this.tableModel.getColumnCount();
/*     */     }
/*     */ 
/*     */     public int getRowCount() {
/* 265 */       return TableRowSorter.this.tableModel == null ? 0 : TableRowSorter.this.tableModel.getRowCount();
/*     */     }
/*     */ 
/*     */     public Object getValueAt(int paramInt1, int paramInt2) {
/* 269 */       return TableRowSorter.this.tableModel.getValueAt(paramInt1, paramInt2);
/*     */     }
/*     */ 
/*     */     public String getStringValueAt(int paramInt1, int paramInt2) {
/* 273 */       TableStringConverter localTableStringConverter = TableRowSorter.this.getStringConverter();
/* 274 */       if (localTableStringConverter != null)
/*     */       {
/* 276 */         localObject = localTableStringConverter.toString(TableRowSorter.this.tableModel, paramInt1, paramInt2);
/*     */ 
/* 278 */         if (localObject != null) {
/* 279 */           return localObject;
/*     */         }
/* 281 */         return "";
/*     */       }
/*     */ 
/* 285 */       Object localObject = getValueAt(paramInt1, paramInt2);
/* 286 */       if (localObject == null) {
/* 287 */         return "";
/*     */       }
/* 289 */       String str = localObject.toString();
/* 290 */       if (str == null) {
/* 291 */         return "";
/*     */       }
/* 293 */       return str;
/*     */     }
/*     */ 
/*     */     public Integer getIdentifier(int paramInt) {
/* 297 */       return Integer.valueOf(paramInt);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.table.TableRowSorter
 * JD-Core Version:    0.6.2
 */