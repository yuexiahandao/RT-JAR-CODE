/*      */ package com.sun.rowset;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.OutputStream;
/*      */ import java.io.Reader;
/*      */ import java.io.Writer;
/*      */ import java.math.BigDecimal;
/*      */ import java.net.URL;
/*      */ import java.sql.Array;
/*      */ import java.sql.Blob;
/*      */ import java.sql.Clob;
/*      */ import java.sql.Connection;
/*      */ import java.sql.Date;
/*      */ import java.sql.Ref;
/*      */ import java.sql.ResultSet;
/*      */ import java.sql.ResultSetMetaData;
/*      */ import java.sql.SQLException;
/*      */ import java.sql.SQLWarning;
/*      */ import java.sql.Statement;
/*      */ import java.sql.Time;
/*      */ import java.sql.Timestamp;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Calendar;
/*      */ import java.util.Collection;
/*      */ import java.util.Map;
/*      */ import java.util.Vector;
/*      */ import javax.sql.RowSet;
/*      */ import javax.sql.RowSetListener;
/*      */ import javax.sql.RowSetMetaData;
/*      */ import javax.sql.rowset.CachedRowSet;
/*      */ import javax.sql.rowset.JoinRowSet;
/*      */ import javax.sql.rowset.Joinable;
/*      */ import javax.sql.rowset.RowSetMetaDataImpl;
/*      */ import javax.sql.rowset.WebRowSet;
/*      */ import javax.sql.rowset.spi.SyncProvider;
/*      */ import javax.sql.rowset.spi.SyncProviderException;
/*      */ 
/*      */ public class JoinRowSetImpl extends WebRowSetImpl
/*      */   implements JoinRowSet
/*      */ {
/*      */   private Vector<CachedRowSetImpl> vecRowSetsInJOIN;
/*      */   private CachedRowSetImpl crsInternal;
/*      */   private Vector<Integer> vecJoinType;
/*      */   private Vector<String> vecTableNames;
/*      */   private int iMatchKey;
/*      */   private String strMatchKey;
/*      */   boolean[] supportedJOINs;
/*      */   private WebRowSet wrs;
/*      */   static final long serialVersionUID = -5590501621560008453L;
/*      */ 
/*      */   public JoinRowSetImpl()
/*      */     throws SQLException
/*      */   {
/*  124 */     this.vecRowSetsInJOIN = new Vector();
/*  125 */     this.crsInternal = new CachedRowSetImpl();
/*  126 */     this.vecJoinType = new Vector();
/*  127 */     this.vecTableNames = new Vector();
/*  128 */     this.iMatchKey = -1;
/*  129 */     this.strMatchKey = null;
/*  130 */     this.supportedJOINs = new boolean[] { false, true, false, false, false };
/*      */     try
/*      */     {
/*  133 */       this.resBundle = JdbcRowSetResourceBundle.getJdbcRowSetResourceBundle();
/*      */     } catch (IOException localIOException) {
/*  135 */       throw new RuntimeException(localIOException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void addRowSet(Joinable paramJoinable)
/*      */     throws SQLException
/*      */   {
/*  161 */     int i = 0;
/*  162 */     int j = 0;
/*      */ 
/*  165 */     if (!(paramJoinable instanceof RowSet))
/*  166 */       throw new SQLException(this.resBundle.handleGetObject("joinrowsetimpl.notinstance").toString());
/*      */     CachedRowSetImpl localCachedRowSetImpl;
/*      */     int i1;
/*  169 */     if ((paramJoinable instanceof JdbcRowSetImpl)) {
/*  170 */       localCachedRowSetImpl = new CachedRowSetImpl();
/*  171 */       localCachedRowSetImpl.populate((RowSet)paramJoinable);
/*  172 */       if (localCachedRowSetImpl.size() == 0) {
/*  173 */         throw new SQLException(this.resBundle.handleGetObject("joinrowsetimpl.emptyrowset").toString());
/*      */       }
/*      */ 
/*      */       try
/*      */       {
/*  178 */         int k = 0;
/*  179 */         for (int m = 0; (m < paramJoinable.getMatchColumnIndexes().length) && 
/*  180 */           (paramJoinable.getMatchColumnIndexes()[m] != -1); m++)
/*      */         {
/*  181 */           k++;
/*      */         }
/*      */ 
/*  185 */         int[] arrayOfInt1 = new int[k];
/*  186 */         for (i1 = 0; i1 < k; i1++)
/*  187 */           arrayOfInt1[i1] = paramJoinable.getMatchColumnIndexes()[i1];
/*  188 */         localCachedRowSetImpl.setMatchColumn(arrayOfInt1);
/*      */       }
/*      */       catch (SQLException localSQLException1) {
/*      */       }
/*      */     }
/*      */     else {
/*  194 */       localCachedRowSetImpl = (CachedRowSetImpl)paramJoinable;
/*  195 */       if (localCachedRowSetImpl.size() == 0) {
/*  196 */         throw new SQLException(this.resBundle.handleGetObject("joinrowsetimpl.emptyrowset").toString());
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/*  204 */       this.iMatchKey = localCachedRowSetImpl.getMatchColumnIndexes()[0];
/*      */     }
/*      */     catch (SQLException localSQLException2) {
/*  207 */       i = 1;
/*      */     }
/*      */     try
/*      */     {
/*  211 */       this.strMatchKey = localCachedRowSetImpl.getMatchColumnNames()[0];
/*      */     }
/*      */     catch (SQLException localSQLException3) {
/*  214 */       j = 1;
/*      */     }
/*      */ 
/*  217 */     if ((i != 0) && (j != 0))
/*      */     {
/*  219 */       throw new SQLException(this.resBundle.handleGetObject("joinrowsetimpl.matchnotset").toString());
/*      */     }
/*      */ 
/*  223 */     if (i != 0)
/*      */     {
/*  225 */       ArrayList localArrayList = new ArrayList();
/*  226 */       for (int n = 0; (n < localCachedRowSetImpl.getMatchColumnNames().length) && 
/*  227 */         ((this.strMatchKey = localCachedRowSetImpl.getMatchColumnNames()[n]) != null); n++)
/*      */       {
/*  228 */         this.iMatchKey = localCachedRowSetImpl.findColumn(this.strMatchKey);
/*  229 */         localArrayList.add(Integer.valueOf(this.iMatchKey));
/*      */       }
/*      */ 
/*  234 */       int[] arrayOfInt2 = new int[localArrayList.size()];
/*  235 */       for (i1 = 0; i1 < localArrayList.size(); i1++)
/*  236 */         arrayOfInt2[i1] = ((Integer)localArrayList.get(i1)).intValue();
/*  237 */       localCachedRowSetImpl.setMatchColumn(arrayOfInt2);
/*      */     }
/*      */ 
/*  255 */     initJOIN(localCachedRowSetImpl);
/*      */   }
/*      */ 
/*      */   public void addRowSet(RowSet paramRowSet, int paramInt)
/*      */     throws SQLException
/*      */   {
/*  283 */     ((CachedRowSetImpl)paramRowSet).setMatchColumn(paramInt);
/*      */ 
/*  285 */     addRowSet((Joinable)paramRowSet);
/*      */   }
/*      */ 
/*      */   public void addRowSet(RowSet paramRowSet, String paramString)
/*      */     throws SQLException
/*      */   {
/*  311 */     ((CachedRowSetImpl)paramRowSet).setMatchColumn(paramString);
/*  312 */     addRowSet((Joinable)paramRowSet);
/*      */   }
/*      */ 
/*      */   public void addRowSet(RowSet[] paramArrayOfRowSet, int[] paramArrayOfInt)
/*      */     throws SQLException
/*      */   {
/*  345 */     if (paramArrayOfRowSet.length != paramArrayOfInt.length) {
/*  346 */       throw new SQLException(this.resBundle.handleGetObject("joinrowsetimpl.numnotequal").toString());
/*      */     }
/*      */ 
/*  349 */     for (int i = 0; i < paramArrayOfRowSet.length; i++) {
/*  350 */       ((CachedRowSetImpl)paramArrayOfRowSet[i]).setMatchColumn(paramArrayOfInt[i]);
/*  351 */       addRowSet((Joinable)paramArrayOfRowSet[i]);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void addRowSet(RowSet[] paramArrayOfRowSet, String[] paramArrayOfString)
/*      */     throws SQLException
/*      */   {
/*  392 */     if (paramArrayOfRowSet.length != paramArrayOfString.length) {
/*  393 */       throw new SQLException(this.resBundle.handleGetObject("joinrowsetimpl.numnotequal").toString());
/*      */     }
/*      */ 
/*  396 */     for (int i = 0; i < paramArrayOfRowSet.length; i++) {
/*  397 */       ((CachedRowSetImpl)paramArrayOfRowSet[i]).setMatchColumn(paramArrayOfString[i]);
/*  398 */       addRowSet((Joinable)paramArrayOfRowSet[i]);
/*      */     }
/*      */   }
/*      */ 
/*      */   public Collection getRowSets()
/*      */     throws SQLException
/*      */   {
/*  417 */     return this.vecRowSetsInJOIN;
/*      */   }
/*      */ 
/*      */   public String[] getRowSetNames()
/*      */     throws SQLException
/*      */   {
/*  429 */     Object[] arrayOfObject = this.vecTableNames.toArray();
/*  430 */     String[] arrayOfString = new String[arrayOfObject.length];
/*      */ 
/*  432 */     for (int i = 0; i < arrayOfObject.length; i++) {
/*  433 */       arrayOfString[i] = arrayOfObject[i].toString();
/*      */     }
/*      */ 
/*  436 */     return arrayOfString;
/*      */   }
/*      */ 
/*      */   public CachedRowSet toCachedRowSet()
/*      */     throws SQLException
/*      */   {
/*  470 */     return this.crsInternal;
/*      */   }
/*      */ 
/*      */   public boolean supportsCrossJoin()
/*      */   {
/*  481 */     return this.supportedJOINs[0];
/*      */   }
/*      */ 
/*      */   public boolean supportsInnerJoin()
/*      */   {
/*  491 */     return this.supportedJOINs[1];
/*      */   }
/*      */ 
/*      */   public boolean supportsLeftOuterJoin()
/*      */   {
/*  501 */     return this.supportedJOINs[2];
/*      */   }
/*      */ 
/*      */   public boolean supportsRightOuterJoin()
/*      */   {
/*  511 */     return this.supportedJOINs[3];
/*      */   }
/*      */ 
/*      */   public boolean supportsFullJoin()
/*      */   {
/*  521 */     return this.supportedJOINs[4];
/*      */   }
/*      */ 
/*      */   public void setJoinType(int paramInt)
/*      */     throws SQLException
/*      */   {
/*  550 */     if ((paramInt >= 0) && (paramInt <= 4)) {
/*  551 */       if (paramInt != 1)
/*      */       {
/*  553 */         throw new SQLException(this.resBundle.handleGetObject("joinrowsetimpl.notsupported").toString());
/*      */       }
/*  555 */       Integer localInteger = Integer.valueOf(1);
/*  556 */       this.vecJoinType.add(localInteger);
/*      */     }
/*      */     else {
/*  559 */       throw new SQLException(this.resBundle.handleGetObject("joinrowsetimpl.notdefined").toString());
/*      */     }
/*      */   }
/*      */ 
/*      */   private boolean checkforMatchColumn(Joinable paramJoinable)
/*      */     throws SQLException
/*      */   {
/*  572 */     int[] arrayOfInt = paramJoinable.getMatchColumnIndexes();
/*  573 */     if (arrayOfInt.length <= 0) {
/*  574 */       return false;
/*      */     }
/*  576 */     return true;
/*      */   }
/*      */ 
/*      */   private void initJOIN(CachedRowSet paramCachedRowSet)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/*  585 */       CachedRowSetImpl localCachedRowSetImpl1 = (CachedRowSetImpl)paramCachedRowSet;
/*      */ 
/*  587 */       CachedRowSetImpl localCachedRowSetImpl2 = new CachedRowSetImpl();
/*  588 */       RowSetMetaDataImpl localRowSetMetaDataImpl = new RowSetMetaDataImpl();
/*      */ 
/*  599 */       if (this.vecRowSetsInJOIN.isEmpty())
/*      */       {
/*  605 */         this.crsInternal = ((CachedRowSetImpl)paramCachedRowSet.createCopy());
/*  606 */         this.crsInternal.setMetaData((RowSetMetaDataImpl)localCachedRowSetImpl1.getMetaData());
/*      */ 
/*  609 */         this.vecRowSetsInJOIN.add(localCachedRowSetImpl1);
/*      */       }
/*      */       else
/*      */       {
/*  624 */         if (this.vecRowSetsInJOIN.size() - this.vecJoinType.size() == 2)
/*      */         {
/*  629 */           setJoinType(1);
/*  630 */         } else if (this.vecRowSetsInJOIN.size() - this.vecJoinType.size() != 1);
/*  635 */         this.vecTableNames.add(this.crsInternal.getTableName());
/*  636 */         this.vecTableNames.add(localCachedRowSetImpl1.getTableName());
/*      */ 
/*  647 */         int i = localCachedRowSetImpl1.size();
/*  648 */         int j = this.crsInternal.size();
/*      */ 
/*  652 */         int k = 0;
/*  653 */         for (int m = 0; (m < this.crsInternal.getMatchColumnIndexes().length) && 
/*  654 */           (this.crsInternal.getMatchColumnIndexes()[m] != -1); m++)
/*      */         {
/*  655 */           k++;
/*      */         }
/*      */ 
/*  660 */         localRowSetMetaDataImpl.setColumnCount(this.crsInternal.getMetaData().getColumnCount() + localCachedRowSetImpl1.getMetaData().getColumnCount() - k);
/*      */ 
/*  664 */         localCachedRowSetImpl2.setMetaData(localRowSetMetaDataImpl);
/*  665 */         this.crsInternal.beforeFirst();
/*  666 */         localCachedRowSetImpl1.beforeFirst();
/*  667 */         for (m = 1; (m <= j) && 
/*  668 */           (!this.crsInternal.isAfterLast()); m++)
/*      */         {
/*  671 */           if (this.crsInternal.next()) {
/*  672 */             localCachedRowSetImpl1.beforeFirst();
/*  673 */             for (n = 1; (n <= i) && 
/*  674 */               (!localCachedRowSetImpl1.isAfterLast()); n++)
/*      */             {
/*  677 */               if (localCachedRowSetImpl1.next()) {
/*  678 */                 int i1 = 1;
/*  679 */                 for (int i2 = 0; i2 < k; i2++) {
/*  680 */                   if (!this.crsInternal.getObject(this.crsInternal.getMatchColumnIndexes()[i2]).equals(localCachedRowSetImpl1.getObject(localCachedRowSetImpl1.getMatchColumnIndexes()[i2])))
/*      */                   {
/*  682 */                     i1 = 0;
/*  683 */                     break;
/*      */                   }
/*      */                 }
/*  686 */                 if (i1 != 0)
/*      */                 {
/*  689 */                   int i3 = 0;
/*      */ 
/*  691 */                   localCachedRowSetImpl2.moveToInsertRow();
/*      */ 
/*  694 */                   for (i2 = 1; 
/*  695 */                     i2 <= this.crsInternal.getMetaData().getColumnCount(); i2++)
/*      */                   {
/*  697 */                     i1 = 0;
/*  698 */                     for (i4 = 0; i4 < k; i4++) {
/*  699 */                       if (i2 == this.crsInternal.getMatchColumnIndexes()[i4]) {
/*  700 */                         i1 = 1;
/*  701 */                         break;
/*      */                       }
/*      */                     }
/*  704 */                     if (i1 == 0)
/*      */                     {
/*  706 */                       localCachedRowSetImpl2.updateObject(++i3, this.crsInternal.getObject(i2));
/*      */ 
/*  709 */                       localRowSetMetaDataImpl.setColumnName(i3, this.crsInternal.getMetaData().getColumnName(i2));
/*      */ 
/*  711 */                       localRowSetMetaDataImpl.setTableName(i3, this.crsInternal.getTableName());
/*      */ 
/*  713 */                       localRowSetMetaDataImpl.setColumnType(i2, this.crsInternal.getMetaData().getColumnType(i2));
/*  714 */                       localRowSetMetaDataImpl.setAutoIncrement(i2, this.crsInternal.getMetaData().isAutoIncrement(i2));
/*  715 */                       localRowSetMetaDataImpl.setCaseSensitive(i2, this.crsInternal.getMetaData().isCaseSensitive(i2));
/*  716 */                       localRowSetMetaDataImpl.setCatalogName(i2, this.crsInternal.getMetaData().getCatalogName(i2));
/*  717 */                       localRowSetMetaDataImpl.setColumnDisplaySize(i2, this.crsInternal.getMetaData().getColumnDisplaySize(i2));
/*  718 */                       localRowSetMetaDataImpl.setColumnLabel(i2, this.crsInternal.getMetaData().getColumnLabel(i2));
/*  719 */                       localRowSetMetaDataImpl.setColumnType(i2, this.crsInternal.getMetaData().getColumnType(i2));
/*  720 */                       localRowSetMetaDataImpl.setColumnTypeName(i2, this.crsInternal.getMetaData().getColumnTypeName(i2));
/*  721 */                       localRowSetMetaDataImpl.setCurrency(i2, this.crsInternal.getMetaData().isCurrency(i2));
/*  722 */                       localRowSetMetaDataImpl.setNullable(i2, this.crsInternal.getMetaData().isNullable(i2));
/*  723 */                       localRowSetMetaDataImpl.setPrecision(i2, this.crsInternal.getMetaData().getPrecision(i2));
/*  724 */                       localRowSetMetaDataImpl.setScale(i2, this.crsInternal.getMetaData().getScale(i2));
/*  725 */                       localRowSetMetaDataImpl.setSchemaName(i2, this.crsInternal.getMetaData().getSchemaName(i2));
/*  726 */                       localRowSetMetaDataImpl.setSearchable(i2, this.crsInternal.getMetaData().isSearchable(i2));
/*  727 */                       localRowSetMetaDataImpl.setSigned(i2, this.crsInternal.getMetaData().isSigned(i2));
/*      */                     }
/*      */                     else
/*      */                     {
/*  733 */                       localCachedRowSetImpl2.updateObject(++i3, this.crsInternal.getObject(i2));
/*      */ 
/*  735 */                       localRowSetMetaDataImpl.setColumnName(i3, this.crsInternal.getMetaData().getColumnName(i2));
/*  736 */                       localRowSetMetaDataImpl.setTableName(i3, this.crsInternal.getTableName() + "#" + localCachedRowSetImpl1.getTableName());
/*      */ 
/*  742 */                       localRowSetMetaDataImpl.setColumnType(i2, this.crsInternal.getMetaData().getColumnType(i2));
/*  743 */                       localRowSetMetaDataImpl.setAutoIncrement(i2, this.crsInternal.getMetaData().isAutoIncrement(i2));
/*  744 */                       localRowSetMetaDataImpl.setCaseSensitive(i2, this.crsInternal.getMetaData().isCaseSensitive(i2));
/*  745 */                       localRowSetMetaDataImpl.setCatalogName(i2, this.crsInternal.getMetaData().getCatalogName(i2));
/*  746 */                       localRowSetMetaDataImpl.setColumnDisplaySize(i2, this.crsInternal.getMetaData().getColumnDisplaySize(i2));
/*  747 */                       localRowSetMetaDataImpl.setColumnLabel(i2, this.crsInternal.getMetaData().getColumnLabel(i2));
/*  748 */                       localRowSetMetaDataImpl.setColumnType(i2, this.crsInternal.getMetaData().getColumnType(i2));
/*  749 */                       localRowSetMetaDataImpl.setColumnTypeName(i2, this.crsInternal.getMetaData().getColumnTypeName(i2));
/*  750 */                       localRowSetMetaDataImpl.setCurrency(i2, this.crsInternal.getMetaData().isCurrency(i2));
/*  751 */                       localRowSetMetaDataImpl.setNullable(i2, this.crsInternal.getMetaData().isNullable(i2));
/*  752 */                       localRowSetMetaDataImpl.setPrecision(i2, this.crsInternal.getMetaData().getPrecision(i2));
/*  753 */                       localRowSetMetaDataImpl.setScale(i2, this.crsInternal.getMetaData().getScale(i2));
/*  754 */                       localRowSetMetaDataImpl.setSchemaName(i2, this.crsInternal.getMetaData().getSchemaName(i2));
/*  755 */                       localRowSetMetaDataImpl.setSearchable(i2, this.crsInternal.getMetaData().isSearchable(i2));
/*  756 */                       localRowSetMetaDataImpl.setSigned(i2, this.crsInternal.getMetaData().isSigned(i2));
/*      */                     }
/*      */ 
/*      */                   }
/*      */ 
/*  764 */                   for (int i4 = 1; 
/*  765 */                     i4 <= localCachedRowSetImpl1.getMetaData().getColumnCount(); i4++)
/*      */                   {
/*  767 */                     i1 = 0;
/*  768 */                     for (int i5 = 0; i5 < k; i5++) {
/*  769 */                       if (i4 == localCachedRowSetImpl1.getMatchColumnIndexes()[i5]) {
/*  770 */                         i1 = 1;
/*  771 */                         break;
/*      */                       }
/*      */                     }
/*  774 */                     if (i1 == 0)
/*      */                     {
/*  776 */                       localCachedRowSetImpl2.updateObject(++i3, localCachedRowSetImpl1.getObject(i4));
/*      */ 
/*  778 */                       localRowSetMetaDataImpl.setColumnName(i3, localCachedRowSetImpl1.getMetaData().getColumnName(i4));
/*      */ 
/*  780 */                       localRowSetMetaDataImpl.setTableName(i3, localCachedRowSetImpl1.getTableName());
/*      */ 
/*  794 */                       localRowSetMetaDataImpl.setColumnType(i2 + i4 - 1, localCachedRowSetImpl1.getMetaData().getColumnType(i4));
/*  795 */                       localRowSetMetaDataImpl.setAutoIncrement(i2 + i4 - 1, localCachedRowSetImpl1.getMetaData().isAutoIncrement(i4));
/*  796 */                       localRowSetMetaDataImpl.setCaseSensitive(i2 + i4 - 1, localCachedRowSetImpl1.getMetaData().isCaseSensitive(i4));
/*  797 */                       localRowSetMetaDataImpl.setCatalogName(i2 + i4 - 1, localCachedRowSetImpl1.getMetaData().getCatalogName(i4));
/*  798 */                       localRowSetMetaDataImpl.setColumnDisplaySize(i2 + i4 - 1, localCachedRowSetImpl1.getMetaData().getColumnDisplaySize(i4));
/*  799 */                       localRowSetMetaDataImpl.setColumnLabel(i2 + i4 - 1, localCachedRowSetImpl1.getMetaData().getColumnLabel(i4));
/*  800 */                       localRowSetMetaDataImpl.setColumnType(i2 + i4 - 1, localCachedRowSetImpl1.getMetaData().getColumnType(i4));
/*  801 */                       localRowSetMetaDataImpl.setColumnTypeName(i2 + i4 - 1, localCachedRowSetImpl1.getMetaData().getColumnTypeName(i4));
/*  802 */                       localRowSetMetaDataImpl.setCurrency(i2 + i4 - 1, localCachedRowSetImpl1.getMetaData().isCurrency(i4));
/*  803 */                       localRowSetMetaDataImpl.setNullable(i2 + i4 - 1, localCachedRowSetImpl1.getMetaData().isNullable(i4));
/*  804 */                       localRowSetMetaDataImpl.setPrecision(i2 + i4 - 1, localCachedRowSetImpl1.getMetaData().getPrecision(i4));
/*  805 */                       localRowSetMetaDataImpl.setScale(i2 + i4 - 1, localCachedRowSetImpl1.getMetaData().getScale(i4));
/*  806 */                       localRowSetMetaDataImpl.setSchemaName(i2 + i4 - 1, localCachedRowSetImpl1.getMetaData().getSchemaName(i4));
/*  807 */                       localRowSetMetaDataImpl.setSearchable(i2 + i4 - 1, localCachedRowSetImpl1.getMetaData().isSearchable(i4));
/*  808 */                       localRowSetMetaDataImpl.setSigned(i2 + i4 - 1, localCachedRowSetImpl1.getMetaData().isSigned(i4));
/*      */                     }
/*      */                     else {
/*  811 */                       i2--;
/*      */                     }
/*      */                   }
/*  814 */                   localCachedRowSetImpl2.insertRow();
/*  815 */                   localCachedRowSetImpl2.moveToCurrentRow();
/*      */                 }
/*      */ 
/*      */               }
/*      */ 
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*  829 */         localCachedRowSetImpl2.setMetaData(localRowSetMetaDataImpl);
/*  830 */         localCachedRowSetImpl2.setOriginal();
/*      */ 
/*  835 */         int[] arrayOfInt = new int[k];
/*  836 */         for (int n = 0; n < k; n++) {
/*  837 */           arrayOfInt[n] = this.crsInternal.getMatchColumnIndexes()[n];
/*      */         }
/*  839 */         this.crsInternal = ((CachedRowSetImpl)localCachedRowSetImpl2.createCopy());
/*      */ 
/*  848 */         this.crsInternal.setMatchColumn(arrayOfInt);
/*      */ 
/*  850 */         this.crsInternal.setMetaData(localRowSetMetaDataImpl);
/*  851 */         this.vecRowSetsInJOIN.add(localCachedRowSetImpl1);
/*      */       }
/*      */     }
/*      */     catch (SQLException localSQLException) {
/*  855 */       localSQLException.printStackTrace();
/*  856 */       throw new SQLException(this.resBundle.handleGetObject("joinrowsetimpl.initerror").toString() + localSQLException);
/*      */     } catch (Exception localException) {
/*  858 */       localException.printStackTrace();
/*  859 */       throw new SQLException(this.resBundle.handleGetObject("joinrowsetimpl.genericerr").toString() + localException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public String getWhereClause()
/*      */     throws SQLException
/*      */   {
/*  877 */     String str1 = "Select ";
/*      */ 
/*  879 */     String str2 = "";
/*  880 */     String str3 = "";
/*      */ 
/*  894 */     int i = this.vecRowSetsInJOIN.size();
/*  895 */     for (int m = 0; m < i; m++) {
/*  896 */       CachedRowSetImpl localCachedRowSetImpl = (CachedRowSetImpl)this.vecRowSetsInJOIN.get(m);
/*  897 */       int j = localCachedRowSetImpl.getMetaData().getColumnCount();
/*  898 */       str2 = str2.concat(localCachedRowSetImpl.getTableName());
/*  899 */       str3 = str3.concat(str2 + ", ");
/*  900 */       int k = 1;
/*  901 */       while (k < j)
/*      */       {
/*  903 */         str1 = str1.concat(str2 + "." + localCachedRowSetImpl.getMetaData().getColumnName(k++));
/*      */ 
/*  905 */         str1 = str1.concat(", ");
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  911 */     str1 = str1.substring(0, str1.lastIndexOf(","));
/*      */ 
/*  915 */     str1 = str1.concat(" from ");
/*      */ 
/*  918 */     str1 = str1.concat(str3);
/*      */ 
/*  921 */     str1 = str1.substring(0, str1.lastIndexOf(","));
/*      */ 
/*  925 */     str1 = str1.concat(" where ");
/*      */ 
/*  929 */     for (m = 0; m < i; m++) {
/*  930 */       str1 = str1.concat(((CachedRowSetImpl)this.vecRowSetsInJOIN.get(m)).getMatchColumnNames()[0]);
/*      */ 
/*  932 */       if (m % 2 != 0)
/*  933 */         str1 = str1.concat("=");
/*      */       else {
/*  935 */         str1 = str1.concat(" and");
/*      */       }
/*  937 */       str1 = str1.concat(" ");
/*      */     }
/*      */ 
/*  940 */     return str1;
/*      */   }
/*      */ 
/*      */   public boolean next()
/*      */     throws SQLException
/*      */   {
/*  965 */     return this.crsInternal.next();
/*      */   }
/*      */ 
/*      */   public void close()
/*      */     throws SQLException
/*      */   {
/*  979 */     this.crsInternal.close();
/*      */   }
/*      */ 
/*      */   public boolean wasNull()
/*      */     throws SQLException
/*      */   {
/*  995 */     return this.crsInternal.wasNull();
/*      */   }
/*      */ 
/*      */   public String getString(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 1012 */     return this.crsInternal.getString(paramInt);
/*      */   }
/*      */ 
/*      */   public boolean getBoolean(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 1029 */     return this.crsInternal.getBoolean(paramInt);
/*      */   }
/*      */ 
/*      */   public byte getByte(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 1046 */     return this.crsInternal.getByte(paramInt);
/*      */   }
/*      */ 
/*      */   public short getShort(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 1063 */     return this.crsInternal.getShort(paramInt);
/*      */   }
/*      */ 
/*      */   public int getInt(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 1080 */     return this.crsInternal.getInt(paramInt);
/*      */   }
/*      */ 
/*      */   public long getLong(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 1097 */     return this.crsInternal.getLong(paramInt);
/*      */   }
/*      */ 
/*      */   public float getFloat(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 1114 */     return this.crsInternal.getFloat(paramInt);
/*      */   }
/*      */ 
/*      */   public double getDouble(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 1131 */     return this.crsInternal.getDouble(paramInt);
/*      */   }
/*      */ 
/*      */   /** @deprecated */
/*      */   public BigDecimal getBigDecimal(int paramInt1, int paramInt2)
/*      */     throws SQLException
/*      */   {
/* 1156 */     return this.crsInternal.getBigDecimal(paramInt1);
/*      */   }
/*      */ 
/*      */   public byte[] getBytes(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 1174 */     return this.crsInternal.getBytes(paramInt);
/*      */   }
/*      */ 
/*      */   public Date getDate(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 1191 */     return this.crsInternal.getDate(paramInt);
/*      */   }
/*      */ 
/*      */   public Time getTime(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 1208 */     return this.crsInternal.getTime(paramInt);
/*      */   }
/*      */ 
/*      */   public Timestamp getTimestamp(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 1225 */     return this.crsInternal.getTimestamp(paramInt);
/*      */   }
/*      */ 
/*      */   public InputStream getAsciiStream(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 1242 */     return this.crsInternal.getAsciiStream(paramInt);
/*      */   }
/*      */ 
/*      */   /** @deprecated */
/*      */   public InputStream getUnicodeStream(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 1267 */     return this.crsInternal.getUnicodeStream(paramInt);
/*      */   }
/*      */ 
/*      */   public InputStream getBinaryStream(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 1290 */     return this.crsInternal.getBinaryStream(paramInt);
/*      */   }
/*      */ 
/*      */   public String getString(String paramString)
/*      */     throws SQLException
/*      */   {
/* 1308 */     return this.crsInternal.getString(paramString);
/*      */   }
/*      */ 
/*      */   public boolean getBoolean(String paramString)
/*      */     throws SQLException
/*      */   {
/* 1324 */     return this.crsInternal.getBoolean(paramString);
/*      */   }
/*      */ 
/*      */   public byte getByte(String paramString)
/*      */     throws SQLException
/*      */   {
/* 1340 */     return this.crsInternal.getByte(paramString);
/*      */   }
/*      */ 
/*      */   public short getShort(String paramString)
/*      */     throws SQLException
/*      */   {
/* 1356 */     return this.crsInternal.getShort(paramString);
/*      */   }
/*      */ 
/*      */   public int getInt(String paramString)
/*      */     throws SQLException
/*      */   {
/* 1372 */     return this.crsInternal.getInt(paramString);
/*      */   }
/*      */ 
/*      */   public long getLong(String paramString)
/*      */     throws SQLException
/*      */   {
/* 1388 */     return this.crsInternal.getLong(paramString);
/*      */   }
/*      */ 
/*      */   public float getFloat(String paramString)
/*      */     throws SQLException
/*      */   {
/* 1404 */     return this.crsInternal.getFloat(paramString);
/*      */   }
/*      */ 
/*      */   public double getDouble(String paramString)
/*      */     throws SQLException
/*      */   {
/* 1420 */     return this.crsInternal.getDouble(paramString);
/*      */   }
/*      */ 
/*      */   /** @deprecated */
/*      */   public BigDecimal getBigDecimal(String paramString, int paramInt)
/*      */     throws SQLException
/*      */   {
/* 1439 */     return this.crsInternal.getBigDecimal(paramString);
/*      */   }
/*      */ 
/*      */   public byte[] getBytes(String paramString)
/*      */     throws SQLException
/*      */   {
/* 1456 */     return this.crsInternal.getBytes(paramString);
/*      */   }
/*      */ 
/*      */   public Date getDate(String paramString)
/*      */     throws SQLException
/*      */   {
/* 1472 */     return this.crsInternal.getDate(paramString);
/*      */   }
/*      */ 
/*      */   public Time getTime(String paramString)
/*      */     throws SQLException
/*      */   {
/* 1488 */     return this.crsInternal.getTime(paramString);
/*      */   }
/*      */ 
/*      */   public Timestamp getTimestamp(String paramString)
/*      */     throws SQLException
/*      */   {
/* 1504 */     return this.crsInternal.getTimestamp(paramString);
/*      */   }
/*      */ 
/*      */   public InputStream getAsciiStream(String paramString)
/*      */     throws SQLException
/*      */   {
/* 1528 */     return this.crsInternal.getAsciiStream(paramString);
/*      */   }
/*      */ 
/*      */   /** @deprecated */
/*      */   public InputStream getUnicodeStream(String paramString)
/*      */     throws SQLException
/*      */   {
/* 1555 */     return this.crsInternal.getUnicodeStream(paramString);
/*      */   }
/*      */ 
/*      */   public InputStream getBinaryStream(String paramString)
/*      */     throws SQLException
/*      */   {
/* 1579 */     return this.crsInternal.getBinaryStream(paramString);
/*      */   }
/*      */ 
/*      */   public SQLWarning getWarnings()
/*      */   {
/* 1598 */     return this.crsInternal.getWarnings();
/*      */   }
/*      */ 
/*      */   public void clearWarnings()
/*      */   {
/* 1611 */     this.crsInternal.clearWarnings();
/*      */   }
/*      */ 
/*      */   public String getCursorName()
/*      */     throws SQLException
/*      */   {
/* 1638 */     return this.crsInternal.getCursorName();
/*      */   }
/*      */ 
/*      */   public ResultSetMetaData getMetaData()
/*      */     throws SQLException
/*      */   {
/* 1652 */     return this.crsInternal.getMetaData();
/*      */   }
/*      */ 
/*      */   public Object getObject(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 1688 */     return this.crsInternal.getObject(paramInt);
/*      */   }
/*      */ 
/*      */   public Object getObject(int paramInt, Map<String, Class<?>> paramMap)
/*      */     throws SQLException
/*      */   {
/* 1730 */     return this.crsInternal.getObject(paramInt, paramMap);
/*      */   }
/*      */ 
/*      */   public Object getObject(String paramString)
/*      */     throws SQLException
/*      */   {
/* 1766 */     return this.crsInternal.getObject(paramString);
/*      */   }
/*      */ 
/*      */   public Object getObject(String paramString, Map<String, Class<?>> paramMap)
/*      */     throws SQLException
/*      */   {
/* 1789 */     return this.crsInternal.getObject(paramString, paramMap);
/*      */   }
/*      */ 
/*      */   public Reader getCharacterStream(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 1810 */     return this.crsInternal.getCharacterStream(paramInt);
/*      */   }
/*      */ 
/*      */   public Reader getCharacterStream(String paramString)
/*      */     throws SQLException
/*      */   {
/* 1830 */     return this.crsInternal.getCharacterStream(paramString);
/*      */   }
/*      */ 
/*      */   public BigDecimal getBigDecimal(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 1847 */     return this.crsInternal.getBigDecimal(paramInt);
/*      */   }
/*      */ 
/*      */   public BigDecimal getBigDecimal(String paramString)
/*      */     throws SQLException
/*      */   {
/* 1863 */     return this.crsInternal.getBigDecimal(paramString);
/*      */   }
/*      */ 
/*      */   public int size()
/*      */   {
/* 1872 */     return this.crsInternal.size();
/*      */   }
/*      */ 
/*      */   public boolean isBeforeFirst()
/*      */     throws SQLException
/*      */   {
/* 1884 */     return this.crsInternal.isBeforeFirst();
/*      */   }
/*      */ 
/*      */   public boolean isAfterLast()
/*      */     throws SQLException
/*      */   {
/* 1896 */     return this.crsInternal.isAfterLast();
/*      */   }
/*      */ 
/*      */   public boolean isFirst()
/*      */     throws SQLException
/*      */   {
/* 1908 */     return this.crsInternal.isFirst();
/*      */   }
/*      */ 
/*      */   public boolean isLast()
/*      */     throws SQLException
/*      */   {
/* 1924 */     return this.crsInternal.isLast();
/*      */   }
/*      */ 
/*      */   public void beforeFirst()
/*      */     throws SQLException
/*      */   {
/* 1936 */     this.crsInternal.beforeFirst();
/*      */   }
/*      */ 
/*      */   public void afterLast()
/*      */     throws SQLException
/*      */   {
/* 1947 */     this.crsInternal.afterLast();
/*      */   }
/*      */ 
/*      */   public boolean first()
/*      */     throws SQLException
/*      */   {
/* 1962 */     return this.crsInternal.first();
/*      */   }
/*      */ 
/*      */   public boolean last()
/*      */     throws SQLException
/*      */   {
/* 1978 */     return this.crsInternal.last();
/*      */   }
/*      */ 
/*      */   public int getRow()
/*      */     throws SQLException
/*      */   {
/* 1990 */     return this.crsInternal.getRow();
/*      */   }
/*      */ 
/*      */   public boolean absolute(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 2041 */     return this.crsInternal.absolute(paramInt);
/*      */   }
/*      */ 
/*      */   public boolean relative(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 2100 */     return this.crsInternal.relative(paramInt);
/*      */   }
/*      */ 
/*      */   public boolean previous()
/*      */     throws SQLException
/*      */   {
/* 2146 */     return this.crsInternal.previous();
/*      */   }
/*      */ 
/*      */   public int findColumn(String paramString)
/*      */     throws SQLException
/*      */   {
/* 2160 */     return this.crsInternal.findColumn(paramString);
/*      */   }
/*      */ 
/*      */   public boolean rowUpdated()
/*      */     throws SQLException
/*      */   {
/* 2178 */     return this.crsInternal.rowUpdated();
/*      */   }
/*      */ 
/*      */   public boolean columnUpdated(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 2194 */     return this.crsInternal.columnUpdated(paramInt);
/*      */   }
/*      */ 
/*      */   public boolean rowInserted()
/*      */     throws SQLException
/*      */   {
/* 2209 */     return this.crsInternal.rowInserted();
/*      */   }
/*      */ 
/*      */   public boolean rowDeleted()
/*      */     throws SQLException
/*      */   {
/* 2226 */     return this.crsInternal.rowDeleted();
/*      */   }
/*      */ 
/*      */   public void updateNull(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 2256 */     this.crsInternal.updateNull(paramInt);
/*      */   }
/*      */ 
/*      */   public void updateBoolean(int paramInt, boolean paramBoolean)
/*      */     throws SQLException
/*      */   {
/* 2283 */     this.crsInternal.updateBoolean(paramInt, paramBoolean);
/*      */   }
/*      */ 
/*      */   public void updateByte(int paramInt, byte paramByte)
/*      */     throws SQLException
/*      */   {
/* 2310 */     this.crsInternal.updateByte(paramInt, paramByte);
/*      */   }
/*      */ 
/*      */   public void updateShort(int paramInt, short paramShort)
/*      */     throws SQLException
/*      */   {
/* 2337 */     this.crsInternal.updateShort(paramInt, paramShort);
/*      */   }
/*      */ 
/*      */   public void updateInt(int paramInt1, int paramInt2)
/*      */     throws SQLException
/*      */   {
/* 2364 */     this.crsInternal.updateInt(paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   public void updateLong(int paramInt, long paramLong)
/*      */     throws SQLException
/*      */   {
/* 2391 */     this.crsInternal.updateLong(paramInt, paramLong);
/*      */   }
/*      */ 
/*      */   public void updateFloat(int paramInt, float paramFloat)
/*      */     throws SQLException
/*      */   {
/* 2418 */     this.crsInternal.updateFloat(paramInt, paramFloat);
/*      */   }
/*      */ 
/*      */   public void updateDouble(int paramInt, double paramDouble)
/*      */     throws SQLException
/*      */   {
/* 2445 */     this.crsInternal.updateDouble(paramInt, paramDouble);
/*      */   }
/*      */ 
/*      */   public void updateBigDecimal(int paramInt, BigDecimal paramBigDecimal)
/*      */     throws SQLException
/*      */   {
/* 2472 */     this.crsInternal.updateBigDecimal(paramInt, paramBigDecimal);
/*      */   }
/*      */ 
/*      */   public void updateString(int paramInt, String paramString)
/*      */     throws SQLException
/*      */   {
/* 2502 */     this.crsInternal.updateString(paramInt, paramString);
/*      */   }
/*      */ 
/*      */   public void updateBytes(int paramInt, byte[] paramArrayOfByte)
/*      */     throws SQLException
/*      */   {
/* 2529 */     this.crsInternal.updateBytes(paramInt, paramArrayOfByte);
/*      */   }
/*      */ 
/*      */   public void updateDate(int paramInt, Date paramDate)
/*      */     throws SQLException
/*      */   {
/* 2557 */     this.crsInternal.updateDate(paramInt, paramDate);
/*      */   }
/*      */ 
/*      */   public void updateTime(int paramInt, Time paramTime)
/*      */     throws SQLException
/*      */   {
/* 2585 */     this.crsInternal.updateTime(paramInt, paramTime);
/*      */   }
/*      */ 
/*      */   public void updateTimestamp(int paramInt, Timestamp paramTimestamp)
/*      */     throws SQLException
/*      */   {
/* 2614 */     this.crsInternal.updateTimestamp(paramInt, paramTimestamp);
/*      */   }
/*      */ 
/*      */   public void updateAsciiStream(int paramInt1, InputStream paramInputStream, int paramInt2)
/*      */     throws SQLException
/*      */   {
/* 2639 */     this.crsInternal.updateAsciiStream(paramInt1, paramInputStream, paramInt2);
/*      */   }
/*      */ 
/*      */   public void updateBinaryStream(int paramInt1, InputStream paramInputStream, int paramInt2)
/*      */     throws SQLException
/*      */   {
/* 2669 */     this.crsInternal.updateBinaryStream(paramInt1, paramInputStream, paramInt2);
/*      */   }
/*      */ 
/*      */   public void updateCharacterStream(int paramInt1, Reader paramReader, int paramInt2)
/*      */     throws SQLException
/*      */   {
/* 2701 */     this.crsInternal.updateCharacterStream(paramInt1, paramReader, paramInt2);
/*      */   }
/*      */ 
/*      */   public void updateObject(int paramInt1, Object paramObject, int paramInt2)
/*      */     throws SQLException
/*      */   {
/* 2733 */     this.crsInternal.updateObject(paramInt1, paramObject, paramInt2);
/*      */   }
/*      */ 
/*      */   public void updateObject(int paramInt, Object paramObject)
/*      */     throws SQLException
/*      */   {
/* 2760 */     this.crsInternal.updateObject(paramInt, paramObject);
/*      */   }
/*      */ 
/*      */   public void updateNull(String paramString)
/*      */     throws SQLException
/*      */   {
/* 2786 */     this.crsInternal.updateNull(paramString);
/*      */   }
/*      */ 
/*      */   public void updateBoolean(String paramString, boolean paramBoolean)
/*      */     throws SQLException
/*      */   {
/* 2812 */     this.crsInternal.updateBoolean(paramString, paramBoolean);
/*      */   }
/*      */ 
/*      */   public void updateByte(String paramString, byte paramByte)
/*      */     throws SQLException
/*      */   {
/* 2838 */     this.crsInternal.updateByte(paramString, paramByte);
/*      */   }
/*      */ 
/*      */   public void updateShort(String paramString, short paramShort)
/*      */     throws SQLException
/*      */   {
/* 2864 */     this.crsInternal.updateShort(paramString, paramShort);
/*      */   }
/*      */ 
/*      */   public void updateInt(String paramString, int paramInt)
/*      */     throws SQLException
/*      */   {
/* 2890 */     this.crsInternal.updateInt(paramString, paramInt);
/*      */   }
/*      */ 
/*      */   public void updateLong(String paramString, long paramLong)
/*      */     throws SQLException
/*      */   {
/* 2916 */     this.crsInternal.updateLong(paramString, paramLong);
/*      */   }
/*      */ 
/*      */   public void updateFloat(String paramString, float paramFloat)
/*      */     throws SQLException
/*      */   {
/* 2942 */     this.crsInternal.updateFloat(paramString, paramFloat);
/*      */   }
/*      */ 
/*      */   public void updateDouble(String paramString, double paramDouble)
/*      */     throws SQLException
/*      */   {
/* 2968 */     this.crsInternal.updateDouble(paramString, paramDouble);
/*      */   }
/*      */ 
/*      */   public void updateBigDecimal(String paramString, BigDecimal paramBigDecimal)
/*      */     throws SQLException
/*      */   {
/* 2994 */     this.crsInternal.updateBigDecimal(paramString, paramBigDecimal);
/*      */   }
/*      */ 
/*      */   public void updateString(String paramString1, String paramString2)
/*      */     throws SQLException
/*      */   {
/* 3020 */     this.crsInternal.updateString(paramString1, paramString2);
/*      */   }
/*      */ 
/*      */   public void updateBytes(String paramString, byte[] paramArrayOfByte)
/*      */     throws SQLException
/*      */   {
/* 3046 */     this.crsInternal.updateBytes(paramString, paramArrayOfByte);
/*      */   }
/*      */ 
/*      */   public void updateDate(String paramString, Date paramDate)
/*      */     throws SQLException
/*      */   {
/* 3074 */     this.crsInternal.updateDate(paramString, paramDate);
/*      */   }
/*      */ 
/*      */   public void updateTime(String paramString, Time paramTime)
/*      */     throws SQLException
/*      */   {
/* 3102 */     this.crsInternal.updateTime(paramString, paramTime);
/*      */   }
/*      */ 
/*      */   public void updateTimestamp(String paramString, Timestamp paramTimestamp)
/*      */     throws SQLException
/*      */   {
/* 3133 */     this.crsInternal.updateTimestamp(paramString, paramTimestamp);
/*      */   }
/*      */ 
/*      */   public void updateAsciiStream(String paramString, InputStream paramInputStream, int paramInt)
/*      */     throws SQLException
/*      */   {
/* 3160 */     this.crsInternal.updateAsciiStream(paramString, paramInputStream, paramInt);
/*      */   }
/*      */ 
/*      */   public void updateBinaryStream(String paramString, InputStream paramInputStream, int paramInt)
/*      */     throws SQLException
/*      */   {
/* 3190 */     this.crsInternal.updateBinaryStream(paramString, paramInputStream, paramInt);
/*      */   }
/*      */ 
/*      */   public void updateCharacterStream(String paramString, Reader paramReader, int paramInt)
/*      */     throws SQLException
/*      */   {
/* 3221 */     this.crsInternal.updateCharacterStream(paramString, paramReader, paramInt);
/*      */   }
/*      */ 
/*      */   public void updateObject(String paramString, Object paramObject, int paramInt)
/*      */     throws SQLException
/*      */   {
/* 3255 */     this.crsInternal.updateObject(paramString, paramObject, paramInt);
/*      */   }
/*      */ 
/*      */   public void updateObject(String paramString, Object paramObject)
/*      */     throws SQLException
/*      */   {
/* 3281 */     this.crsInternal.updateObject(paramString, paramObject);
/*      */   }
/*      */ 
/*      */   public void insertRow()
/*      */     throws SQLException
/*      */   {
/* 3300 */     this.crsInternal.insertRow();
/*      */   }
/*      */ 
/*      */   public void updateRow()
/*      */     throws SQLException
/*      */   {
/* 3317 */     this.crsInternal.updateRow();
/*      */   }
/*      */ 
/*      */   public void deleteRow()
/*      */     throws SQLException
/*      */   {
/* 3336 */     this.crsInternal.deleteRow();
/*      */   }
/*      */ 
/*      */   public void refreshRow()
/*      */     throws SQLException
/*      */   {
/* 3350 */     this.crsInternal.refreshRow();
/*      */   }
/*      */ 
/*      */   public void cancelRowUpdates()
/*      */     throws SQLException
/*      */   {
/* 3371 */     this.crsInternal.cancelRowUpdates();
/*      */   }
/*      */ 
/*      */   public void moveToInsertRow()
/*      */     throws SQLException
/*      */   {
/* 3401 */     this.crsInternal.moveToInsertRow();
/*      */   }
/*      */ 
/*      */   public void moveToCurrentRow()
/*      */     throws SQLException
/*      */   {
/* 3415 */     this.crsInternal.moveToCurrentRow();
/*      */   }
/*      */ 
/*      */   public Statement getStatement()
/*      */     throws SQLException
/*      */   {
/* 3425 */     return this.crsInternal.getStatement();
/*      */   }
/*      */ 
/*      */   public Ref getRef(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 3443 */     return this.crsInternal.getRef(paramInt);
/*      */   }
/*      */ 
/*      */   public Blob getBlob(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 3461 */     return this.crsInternal.getBlob(paramInt);
/*      */   }
/*      */ 
/*      */   public Clob getClob(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 3479 */     return this.crsInternal.getClob(paramInt);
/*      */   }
/*      */ 
/*      */   public Array getArray(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 3498 */     return this.crsInternal.getArray(paramInt);
/*      */   }
/*      */ 
/*      */   public Ref getRef(String paramString)
/*      */     throws SQLException
/*      */   {
/* 3517 */     return this.crsInternal.getRef(paramString);
/*      */   }
/*      */ 
/*      */   public Blob getBlob(String paramString)
/*      */     throws SQLException
/*      */   {
/* 3535 */     return this.crsInternal.getBlob(paramString);
/*      */   }
/*      */ 
/*      */   public Clob getClob(String paramString)
/*      */     throws SQLException
/*      */   {
/* 3553 */     return this.crsInternal.getClob(paramString);
/*      */   }
/*      */ 
/*      */   public Array getArray(String paramString)
/*      */     throws SQLException
/*      */   {
/* 3571 */     return this.crsInternal.getArray(paramString);
/*      */   }
/*      */ 
/*      */   public Date getDate(int paramInt, Calendar paramCalendar)
/*      */     throws SQLException
/*      */   {
/* 3594 */     return this.crsInternal.getDate(paramInt, paramCalendar);
/*      */   }
/*      */ 
/*      */   public Date getDate(String paramString, Calendar paramCalendar)
/*      */     throws SQLException
/*      */   {
/* 3616 */     return this.crsInternal.getDate(paramString, paramCalendar);
/*      */   }
/*      */ 
/*      */   public Time getTime(int paramInt, Calendar paramCalendar)
/*      */     throws SQLException
/*      */   {
/* 3639 */     return this.crsInternal.getTime(paramInt, paramCalendar);
/*      */   }
/*      */ 
/*      */   public Time getTime(String paramString, Calendar paramCalendar)
/*      */     throws SQLException
/*      */   {
/* 3661 */     return this.crsInternal.getTime(paramString, paramCalendar);
/*      */   }
/*      */ 
/*      */   public Timestamp getTimestamp(int paramInt, Calendar paramCalendar)
/*      */     throws SQLException
/*      */   {
/* 3684 */     return this.crsInternal.getTimestamp(paramInt, paramCalendar);
/*      */   }
/*      */ 
/*      */   public Timestamp getTimestamp(String paramString, Calendar paramCalendar)
/*      */     throws SQLException
/*      */   {
/* 3707 */     return this.crsInternal.getTimestamp(paramString, paramCalendar);
/*      */   }
/*      */ 
/*      */   public void setMetaData(RowSetMetaData paramRowSetMetaData)
/*      */     throws SQLException
/*      */   {
/* 3720 */     this.crsInternal.setMetaData(paramRowSetMetaData);
/*      */   }
/*      */ 
/*      */   public ResultSet getOriginal() throws SQLException {
/* 3724 */     return this.crsInternal.getOriginal();
/*      */   }
/*      */ 
/*      */   public ResultSet getOriginalRow()
/*      */     throws SQLException
/*      */   {
/* 3738 */     return this.crsInternal.getOriginalRow();
/*      */   }
/*      */ 
/*      */   public void setOriginalRow()
/*      */     throws SQLException
/*      */   {
/* 3749 */     this.crsInternal.setOriginalRow();
/*      */   }
/*      */ 
/*      */   public int[] getKeyColumns()
/*      */     throws SQLException
/*      */   {
/* 3764 */     return this.crsInternal.getKeyColumns();
/*      */   }
/*      */ 
/*      */   public void setKeyColumns(int[] paramArrayOfInt)
/*      */     throws SQLException
/*      */   {
/* 3784 */     this.crsInternal.setKeyColumns(paramArrayOfInt);
/*      */   }
/*      */ 
/*      */   public void updateRef(int paramInt, Ref paramRef)
/*      */     throws SQLException
/*      */   {
/* 3812 */     this.crsInternal.updateRef(paramInt, paramRef);
/*      */   }
/*      */ 
/*      */   public void updateRef(String paramString, Ref paramRef)
/*      */     throws SQLException
/*      */   {
/* 3840 */     this.crsInternal.updateRef(paramString, paramRef);
/*      */   }
/*      */ 
/*      */   public void updateClob(int paramInt, Clob paramClob)
/*      */     throws SQLException
/*      */   {
/* 3868 */     this.crsInternal.updateClob(paramInt, paramClob);
/*      */   }
/*      */ 
/*      */   public void updateClob(String paramString, Clob paramClob)
/*      */     throws SQLException
/*      */   {
/* 3896 */     this.crsInternal.updateClob(paramString, paramClob);
/*      */   }
/*      */ 
/*      */   public void updateBlob(int paramInt, Blob paramBlob)
/*      */     throws SQLException
/*      */   {
/* 3924 */     this.crsInternal.updateBlob(paramInt, paramBlob);
/*      */   }
/*      */ 
/*      */   public void updateBlob(String paramString, Blob paramBlob)
/*      */     throws SQLException
/*      */   {
/* 3952 */     this.crsInternal.updateBlob(paramString, paramBlob);
/*      */   }
/*      */ 
/*      */   public void updateArray(int paramInt, Array paramArray)
/*      */     throws SQLException
/*      */   {
/* 3980 */     this.crsInternal.updateArray(paramInt, paramArray);
/*      */   }
/*      */ 
/*      */   public void updateArray(String paramString, Array paramArray)
/*      */     throws SQLException
/*      */   {
/* 4008 */     this.crsInternal.updateArray(paramString, paramArray);
/*      */   }
/*      */ 
/*      */   public void execute()
/*      */     throws SQLException
/*      */   {
/* 4035 */     this.crsInternal.execute();
/*      */   }
/*      */ 
/*      */   public void execute(Connection paramConnection)
/*      */     throws SQLException
/*      */   {
/* 4057 */     this.crsInternal.execute(paramConnection);
/*      */   }
/*      */ 
/*      */   public URL getURL(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 4064 */     return this.crsInternal.getURL(paramInt);
/*      */   }
/*      */ 
/*      */   public URL getURL(String paramString)
/*      */     throws SQLException
/*      */   {
/* 4071 */     return this.crsInternal.getURL(paramString);
/*      */   }
/*      */ 
/*      */   public void writeXml(ResultSet paramResultSet, Writer paramWriter)
/*      */     throws SQLException
/*      */   {
/* 4084 */     this.wrs = new WebRowSetImpl();
/* 4085 */     this.wrs.populate(paramResultSet);
/* 4086 */     this.wrs.writeXml(paramWriter);
/*      */   }
/*      */ 
/*      */   public void writeXml(Writer paramWriter)
/*      */     throws SQLException
/*      */   {
/* 4099 */     createWebRowSet().writeXml(paramWriter);
/*      */   }
/*      */ 
/*      */   public void readXml(Reader paramReader)
/*      */     throws SQLException
/*      */   {
/* 4108 */     this.wrs = new WebRowSetImpl();
/* 4109 */     this.wrs.readXml(paramReader);
/* 4110 */     this.crsInternal = ((CachedRowSetImpl)this.wrs);
/*      */   }
/*      */ 
/*      */   public void readXml(InputStream paramInputStream)
/*      */     throws SQLException, IOException
/*      */   {
/* 4121 */     this.wrs = new WebRowSetImpl();
/* 4122 */     this.wrs.readXml(paramInputStream);
/* 4123 */     this.crsInternal = ((CachedRowSetImpl)this.wrs);
/*      */   }
/*      */ 
/*      */   public void writeXml(OutputStream paramOutputStream)
/*      */     throws SQLException, IOException
/*      */   {
/* 4134 */     createWebRowSet().writeXml(paramOutputStream);
/*      */   }
/*      */ 
/*      */   public void writeXml(ResultSet paramResultSet, OutputStream paramOutputStream)
/*      */     throws SQLException, IOException
/*      */   {
/* 4146 */     this.wrs = new WebRowSetImpl();
/* 4147 */     this.wrs.populate(paramResultSet);
/* 4148 */     this.wrs.writeXml(paramOutputStream);
/*      */   }
/*      */ 
/*      */   private WebRowSet createWebRowSet()
/*      */     throws SQLException
/*      */   {
/* 4155 */     if (this.wrs != null)
/*      */     {
/* 4157 */       return this.wrs;
/*      */     }
/* 4159 */     this.wrs = new WebRowSetImpl();
/* 4160 */     this.crsInternal.beforeFirst();
/* 4161 */     this.wrs.populate(this.crsInternal);
/* 4162 */     return this.wrs;
/*      */   }
/*      */ 
/*      */   public int getJoinType()
/*      */     throws SQLException
/*      */   {
/* 4174 */     if (this.vecJoinType == null)
/*      */     {
/* 4176 */       setJoinType(1);
/*      */     }
/* 4178 */     Integer localInteger = (Integer)this.vecJoinType.get(this.vecJoinType.size() - 1);
/* 4179 */     return localInteger.intValue();
/*      */   }
/*      */ 
/*      */   public void addRowSetListener(RowSetListener paramRowSetListener)
/*      */   {
/* 4205 */     this.crsInternal.addRowSetListener(paramRowSetListener);
/*      */   }
/*      */ 
/*      */   public void removeRowSetListener(RowSetListener paramRowSetListener)
/*      */   {
/* 4222 */     this.crsInternal.removeRowSetListener(paramRowSetListener);
/*      */   }
/*      */ 
/*      */   public Collection<?> toCollection()
/*      */     throws SQLException
/*      */   {
/* 4241 */     return this.crsInternal.toCollection();
/*      */   }
/*      */ 
/*      */   public Collection<?> toCollection(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 4263 */     return this.crsInternal.toCollection(paramInt);
/*      */   }
/*      */ 
/*      */   public Collection<?> toCollection(String paramString)
/*      */     throws SQLException
/*      */   {
/* 4285 */     return this.crsInternal.toCollection(paramString);
/*      */   }
/*      */ 
/*      */   public CachedRowSet createCopySchema()
/*      */     throws SQLException
/*      */   {
/* 4311 */     return this.crsInternal.createCopySchema();
/*      */   }
/*      */ 
/*      */   public void setSyncProvider(String paramString)
/*      */     throws SQLException
/*      */   {
/* 4318 */     this.crsInternal.setSyncProvider(paramString);
/*      */   }
/*      */ 
/*      */   public void acceptChanges()
/*      */     throws SyncProviderException
/*      */   {
/* 4325 */     this.crsInternal.acceptChanges();
/*      */   }
/*      */ 
/*      */   public SyncProvider getSyncProvider()
/*      */     throws SQLException
/*      */   {
/* 4332 */     return this.crsInternal.getSyncProvider();
/*      */   }
/*      */ 
/*      */   private void readObject(ObjectInputStream paramObjectInputStream)
/*      */     throws IOException, ClassNotFoundException
/*      */   {
/* 4342 */     paramObjectInputStream.defaultReadObject();
/*      */     try
/*      */     {
/* 4345 */       this.resBundle = JdbcRowSetResourceBundle.getJdbcRowSetResourceBundle();
/*      */     } catch (IOException localIOException) {
/* 4347 */       throw new RuntimeException(localIOException);
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.rowset.JoinRowSetImpl
 * JD-Core Version:    0.6.2
 */