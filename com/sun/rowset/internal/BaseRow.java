/*    */ package com.sun.rowset.internal;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import java.sql.SQLException;
/*    */ 
/*    */ public abstract class BaseRow
/*    */   implements Serializable, Cloneable
/*    */ {
/*    */   protected Object[] origVals;
/*    */ 
/*    */   public Object[] getOrigRow()
/*    */   {
/* 68 */     return this.origVals;
/*    */   }
/*    */ 
/*    */   public abstract Object getColumnObject(int paramInt)
/*    */     throws SQLException;
/*    */ 
/*    */   public abstract void setColumnObject(int paramInt, Object paramObject)
/*    */     throws SQLException;
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.rowset.internal.BaseRow
 * JD-Core Version:    0.6.2
 */