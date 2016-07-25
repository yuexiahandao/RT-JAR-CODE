/*    */ package com.sun.rowset;
/*    */ 
/*    */ import java.sql.SQLException;
/*    */ import javax.sql.rowset.CachedRowSet;
/*    */ import javax.sql.rowset.FilteredRowSet;
/*    */ import javax.sql.rowset.JdbcRowSet;
/*    */ import javax.sql.rowset.JoinRowSet;
/*    */ import javax.sql.rowset.RowSetFactory;
/*    */ import javax.sql.rowset.WebRowSet;
/*    */ 
/*    */ public final class RowSetFactoryImpl
/*    */   implements RowSetFactory
/*    */ {
/*    */   public CachedRowSet createCachedRowSet()
/*    */     throws SQLException
/*    */   {
/* 49 */     return new CachedRowSetImpl();
/*    */   }
/*    */ 
/*    */   public FilteredRowSet createFilteredRowSet() throws SQLException {
/* 53 */     return new FilteredRowSetImpl();
/*    */   }
/*    */ 
/*    */   public JdbcRowSet createJdbcRowSet() throws SQLException
/*    */   {
/* 58 */     return new JdbcRowSetImpl();
/*    */   }
/*    */ 
/*    */   public JoinRowSet createJoinRowSet() throws SQLException {
/* 62 */     return new JoinRowSetImpl();
/*    */   }
/*    */ 
/*    */   public WebRowSet createWebRowSet() throws SQLException {
/* 66 */     return new WebRowSetImpl();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.rowset.RowSetFactoryImpl
 * JD-Core Version:    0.6.2
 */