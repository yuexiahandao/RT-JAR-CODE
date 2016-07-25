/*     */ package javax.swing.table;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Vector;
/*     */ import javax.swing.event.TableModelEvent;
/*     */ 
/*     */ public class DefaultTableModel extends AbstractTableModel
/*     */   implements Serializable
/*     */ {
/*     */   protected Vector dataVector;
/*     */   protected Vector columnIdentifiers;
/*     */ 
/*     */   public DefaultTableModel()
/*     */   {
/*  86 */     this(0, 0);
/*     */   }
/*     */ 
/*     */   private static Vector newVector(int paramInt) {
/*  90 */     Vector localVector = new Vector(paramInt);
/*  91 */     localVector.setSize(paramInt);
/*  92 */     return localVector;
/*     */   }
/*     */ 
/*     */   public DefaultTableModel(int paramInt1, int paramInt2)
/*     */   {
/* 106 */     this(newVector(paramInt2), paramInt1);
/*     */   }
/*     */ 
/*     */   public DefaultTableModel(Vector paramVector, int paramInt)
/*     */   {
/* 124 */     setDataVector(newVector(paramInt), paramVector);
/*     */   }
/*     */ 
/*     */   public DefaultTableModel(Object[] paramArrayOfObject, int paramInt)
/*     */   {
/* 142 */     this(convertToVector(paramArrayOfObject), paramInt);
/*     */   }
/*     */ 
/*     */   public DefaultTableModel(Vector paramVector1, Vector paramVector2)
/*     */   {
/* 159 */     setDataVector(paramVector1, paramVector2);
/*     */   }
/*     */ 
/*     */   public DefaultTableModel(Object[][] paramArrayOfObject, Object[] paramArrayOfObject1)
/*     */   {
/* 175 */     setDataVector(paramArrayOfObject, paramArrayOfObject1);
/*     */   }
/*     */ 
/*     */   public Vector getDataVector()
/*     */   {
/* 194 */     return this.dataVector;
/*     */   }
/*     */ 
/*     */   private static Vector nonNullVector(Vector paramVector) {
/* 198 */     return paramVector != null ? paramVector : new Vector();
/*     */   }
/*     */ 
/*     */   public void setDataVector(Vector paramVector1, Vector paramVector2)
/*     */   {
/* 222 */     this.dataVector = nonNullVector(paramVector1);
/* 223 */     this.columnIdentifiers = nonNullVector(paramVector2);
/* 224 */     justifyRows(0, getRowCount());
/* 225 */     fireTableStructureChanged();
/*     */   }
/*     */ 
/*     */   public void setDataVector(Object[][] paramArrayOfObject, Object[] paramArrayOfObject1)
/*     */   {
/* 240 */     setDataVector(convertToVector(paramArrayOfObject), convertToVector(paramArrayOfObject1));
/*     */   }
/*     */ 
/*     */   public void newDataAvailable(TableModelEvent paramTableModelEvent)
/*     */   {
/* 250 */     fireTableChanged(paramTableModelEvent);
/*     */   }
/*     */ 
/*     */   private void justifyRows(int paramInt1, int paramInt2)
/*     */   {
/* 262 */     this.dataVector.setSize(getRowCount());
/*     */ 
/* 264 */     for (int i = paramInt1; i < paramInt2; i++) {
/* 265 */       if (this.dataVector.elementAt(i) == null) {
/* 266 */         this.dataVector.setElementAt(new Vector(), i);
/*     */       }
/* 268 */       ((Vector)this.dataVector.elementAt(i)).setSize(getColumnCount());
/*     */     }
/*     */   }
/*     */ 
/*     */   public void newRowsAdded(TableModelEvent paramTableModelEvent)
/*     */   {
/* 288 */     justifyRows(paramTableModelEvent.getFirstRow(), paramTableModelEvent.getLastRow() + 1);
/* 289 */     fireTableChanged(paramTableModelEvent);
/*     */   }
/*     */ 
/*     */   public void rowsRemoved(TableModelEvent paramTableModelEvent)
/*     */   {
/* 299 */     fireTableChanged(paramTableModelEvent);
/*     */   }
/*     */ 
/*     */   public void setNumRows(int paramInt)
/*     */   {
/* 315 */     int i = getRowCount();
/* 316 */     if (i == paramInt) {
/* 317 */       return;
/*     */     }
/* 319 */     this.dataVector.setSize(paramInt);
/* 320 */     if (paramInt <= i) {
/* 321 */       fireTableRowsDeleted(paramInt, i - 1);
/*     */     }
/*     */     else {
/* 324 */       justifyRows(i, paramInt);
/* 325 */       fireTableRowsInserted(i, paramInt - 1);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setRowCount(int paramInt)
/*     */   {
/* 339 */     setNumRows(paramInt);
/*     */   }
/*     */ 
/*     */   public void addRow(Vector paramVector)
/*     */   {
/* 350 */     insertRow(getRowCount(), paramVector);
/*     */   }
/*     */ 
/*     */   public void addRow(Object[] paramArrayOfObject)
/*     */   {
/* 361 */     addRow(convertToVector(paramArrayOfObject));
/*     */   }
/*     */ 
/*     */   public void insertRow(int paramInt, Vector paramVector)
/*     */   {
/* 374 */     this.dataVector.insertElementAt(paramVector, paramInt);
/* 375 */     justifyRows(paramInt, paramInt + 1);
/* 376 */     fireTableRowsInserted(paramInt, paramInt);
/*     */   }
/*     */ 
/*     */   public void insertRow(int paramInt, Object[] paramArrayOfObject)
/*     */   {
/* 389 */     insertRow(paramInt, convertToVector(paramArrayOfObject));
/*     */   }
/*     */ 
/*     */   private static int gcd(int paramInt1, int paramInt2) {
/* 393 */     return paramInt2 == 0 ? paramInt1 : gcd(paramInt2, paramInt1 % paramInt2);
/*     */   }
/*     */ 
/*     */   private static void rotate(Vector paramVector, int paramInt1, int paramInt2, int paramInt3) {
/* 397 */     int i = paramInt2 - paramInt1;
/* 398 */     int j = i - paramInt3;
/* 399 */     int k = gcd(i, j);
/* 400 */     for (int m = 0; m < k; m++) {
/* 401 */       int n = m;
/* 402 */       Object localObject = paramVector.elementAt(paramInt1 + n);
/* 403 */       for (int i1 = (n + j) % i; i1 != m; i1 = (n + j) % i) {
/* 404 */         paramVector.setElementAt(paramVector.elementAt(paramInt1 + i1), paramInt1 + n);
/* 405 */         n = i1;
/*     */       }
/* 407 */       paramVector.setElementAt(localObject, paramInt1 + n);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void moveRow(int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/* 440 */     int i = paramInt3 - paramInt1;
/*     */     int j;
/*     */     int k;
/* 442 */     if (i < 0) {
/* 443 */       j = paramInt3;
/* 444 */       k = paramInt2;
/*     */     }
/*     */     else {
/* 447 */       j = paramInt1;
/* 448 */       k = paramInt3 + paramInt2 - paramInt1;
/*     */     }
/* 450 */     rotate(this.dataVector, j, k + 1, i);
/*     */ 
/* 452 */     fireTableRowsUpdated(j, k);
/*     */   }
/*     */ 
/*     */   public void removeRow(int paramInt)
/*     */   {
/* 463 */     this.dataVector.removeElementAt(paramInt);
/* 464 */     fireTableRowsDeleted(paramInt, paramInt);
/*     */   }
/*     */ 
/*     */   public void setColumnIdentifiers(Vector paramVector)
/*     */   {
/* 485 */     setDataVector(this.dataVector, paramVector);
/*     */   }
/*     */ 
/*     */   public void setColumnIdentifiers(Object[] paramArrayOfObject)
/*     */   {
/* 502 */     setColumnIdentifiers(convertToVector(paramArrayOfObject));
/*     */   }
/*     */ 
/*     */   public void setColumnCount(int paramInt)
/*     */   {
/* 518 */     this.columnIdentifiers.setSize(paramInt);
/* 519 */     justifyRows(0, getRowCount());
/* 520 */     fireTableStructureChanged();
/*     */   }
/*     */ 
/*     */   public void addColumn(Object paramObject)
/*     */   {
/* 534 */     addColumn(paramObject, (Vector)null);
/*     */   }
/*     */ 
/*     */   public void addColumn(Object paramObject, Vector paramVector)
/*     */   {
/* 551 */     this.columnIdentifiers.addElement(paramObject);
/* 552 */     if (paramVector != null) {
/* 553 */       int i = paramVector.size();
/* 554 */       if (i > getRowCount()) {
/* 555 */         this.dataVector.setSize(i);
/*     */       }
/* 557 */       justifyRows(0, getRowCount());
/* 558 */       int j = getColumnCount() - 1;
/* 559 */       for (int k = 0; k < i; k++) {
/* 560 */         Vector localVector = (Vector)this.dataVector.elementAt(k);
/* 561 */         localVector.setElementAt(paramVector.elementAt(k), j);
/*     */       }
/*     */     }
/*     */     else {
/* 565 */       justifyRows(0, getRowCount());
/*     */     }
/*     */ 
/* 568 */     fireTableStructureChanged();
/*     */   }
/*     */ 
/*     */   public void addColumn(Object paramObject, Object[] paramArrayOfObject)
/*     */   {
/* 583 */     addColumn(paramObject, convertToVector(paramArrayOfObject));
/*     */   }
/*     */ 
/*     */   public int getRowCount()
/*     */   {
/* 595 */     return this.dataVector.size();
/*     */   }
/*     */ 
/*     */   public int getColumnCount()
/*     */   {
/* 603 */     return this.columnIdentifiers.size();
/*     */   }
/*     */ 
/*     */   public String getColumnName(int paramInt)
/*     */   {
/* 616 */     Object localObject = null;
/*     */ 
/* 619 */     if ((paramInt < this.columnIdentifiers.size()) && (paramInt >= 0)) {
/* 620 */       localObject = this.columnIdentifiers.elementAt(paramInt);
/*     */     }
/* 622 */     return localObject == null ? super.getColumnName(paramInt) : localObject.toString();
/*     */   }
/*     */ 
/*     */   public boolean isCellEditable(int paramInt1, int paramInt2)
/*     */   {
/* 635 */     return true;
/*     */   }
/*     */ 
/*     */   public Object getValueAt(int paramInt1, int paramInt2)
/*     */   {
/* 649 */     Vector localVector = (Vector)this.dataVector.elementAt(paramInt1);
/* 650 */     return localVector.elementAt(paramInt2);
/*     */   }
/*     */ 
/*     */   public void setValueAt(Object paramObject, int paramInt1, int paramInt2)
/*     */   {
/* 665 */     Vector localVector = (Vector)this.dataVector.elementAt(paramInt1);
/* 666 */     localVector.setElementAt(paramObject, paramInt2);
/* 667 */     fireTableCellUpdated(paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   protected static Vector convertToVector(Object[] paramArrayOfObject)
/*     */   {
/* 681 */     if (paramArrayOfObject == null) {
/* 682 */       return null;
/*     */     }
/* 684 */     Vector localVector = new Vector(paramArrayOfObject.length);
/* 685 */     for (Object localObject : paramArrayOfObject) {
/* 686 */       localVector.addElement(localObject);
/*     */     }
/* 688 */     return localVector;
/*     */   }
/*     */ 
/*     */   protected static Vector convertToVector(Object[][] paramArrayOfObject)
/*     */   {
/* 698 */     if (paramArrayOfObject == null) {
/* 699 */       return null;
/*     */     }
/* 701 */     Vector localVector = new Vector(paramArrayOfObject.length);
/* 702 */     for (Object[] arrayOfObject1 : paramArrayOfObject) {
/* 703 */       localVector.addElement(convertToVector(arrayOfObject1));
/*     */     }
/* 705 */     return localVector;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.table.DefaultTableModel
 * JD-Core Version:    0.6.2
 */