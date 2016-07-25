/*     */ package com.sun.rowset.internal;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.sql.SQLException;
/*     */ import java.util.BitSet;
/*     */ 
/*     */ public class Row extends BaseRow
/*     */   implements Serializable, Cloneable
/*     */ {
/*     */   private Object[] currentVals;
/*     */   private BitSet colsChanged;
/*     */   private boolean deleted;
/*     */   private boolean updated;
/*     */   private boolean inserted;
/*     */   private int numCols;
/*     */ 
/*     */   public Row(int paramInt)
/*     */   {
/* 103 */     this.origVals = new Object[paramInt];
/* 104 */     this.currentVals = new Object[paramInt];
/* 105 */     this.colsChanged = new BitSet(paramInt);
/* 106 */     this.numCols = paramInt;
/*     */   }
/*     */ 
/*     */   public Row(int paramInt, Object[] paramArrayOfObject)
/*     */   {
/* 117 */     this.origVals = new Object[paramInt];
/* 118 */     for (int i = 0; i < paramInt; i++) {
/* 119 */       this.origVals[i] = paramArrayOfObject[i];
/*     */     }
/* 121 */     this.currentVals = new Object[paramInt];
/* 122 */     this.colsChanged = new BitSet(paramInt);
/* 123 */     this.numCols = paramInt;
/*     */   }
/*     */ 
/*     */   public void initColumnObject(int paramInt, Object paramObject)
/*     */   {
/* 137 */     this.origVals[(paramInt - 1)] = paramObject;
/*     */   }
/*     */ 
/*     */   public void setColumnObject(int paramInt, Object paramObject)
/*     */   {
/* 152 */     this.currentVals[(paramInt - 1)] = paramObject;
/* 153 */     setColUpdated(paramInt - 1);
/*     */   }
/*     */ 
/*     */   public Object getColumnObject(int paramInt)
/*     */     throws SQLException
/*     */   {
/* 167 */     if (getColUpdated(paramInt - 1)) {
/* 168 */       return this.currentVals[(paramInt - 1)];
/*     */     }
/* 170 */     return this.origVals[(paramInt - 1)];
/*     */   }
/*     */ 
/*     */   public boolean getColUpdated(int paramInt)
/*     */   {
/* 186 */     return this.colsChanged.get(paramInt);
/*     */   }
/*     */ 
/*     */   public void setDeleted()
/*     */   {
/* 196 */     this.deleted = true;
/*     */   }
/*     */ 
/*     */   public boolean getDeleted()
/*     */   {
/* 210 */     return this.deleted;
/*     */   }
/*     */ 
/*     */   public void clearDeleted()
/*     */   {
/* 218 */     this.deleted = false;
/*     */   }
/*     */ 
/*     */   public void setInserted()
/*     */   {
/* 229 */     this.inserted = true;
/*     */   }
/*     */ 
/*     */   public boolean getInserted()
/*     */   {
/* 242 */     return this.inserted;
/*     */   }
/*     */ 
/*     */   public void clearInserted()
/*     */   {
/* 251 */     this.inserted = false;
/*     */   }
/*     */ 
/*     */   public boolean getUpdated()
/*     */   {
/* 263 */     return this.updated;
/*     */   }
/*     */ 
/*     */   public void setUpdated()
/*     */   {
/* 275 */     for (int i = 0; i < this.numCols; i++)
/* 276 */       if (getColUpdated(i) == true) {
/* 277 */         this.updated = true;
/* 278 */         return;
/*     */       }
/*     */   }
/*     */ 
/*     */   private void setColUpdated(int paramInt)
/*     */   {
/* 294 */     this.colsChanged.set(paramInt);
/*     */   }
/*     */ 
/*     */   public void clearUpdated()
/*     */   {
/* 305 */     this.updated = false;
/* 306 */     for (int i = 0; i < this.numCols; i++) {
/* 307 */       this.currentVals[i] = null;
/* 308 */       this.colsChanged.clear(i);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void moveCurrentToOrig()
/*     */   {
/* 324 */     for (int i = 0; i < this.numCols; i++) {
/* 325 */       if (getColUpdated(i) == true) {
/* 326 */         this.origVals[i] = this.currentVals[i];
/* 327 */         this.currentVals[i] = null;
/* 328 */         this.colsChanged.clear(i);
/*     */       }
/*     */     }
/* 331 */     this.updated = false;
/*     */   }
/*     */ 
/*     */   public BaseRow getCurrentRow()
/*     */   {
/* 341 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.rowset.internal.Row
 * JD-Core Version:    0.6.2
 */