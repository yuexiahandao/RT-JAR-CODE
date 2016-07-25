/*      */ package com.sun.rowset.internal;
/*      */ 
/*      */ import com.sun.rowset.CachedRowSetImpl;
/*      */ import com.sun.rowset.JdbcRowSetResourceBundle;
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.Serializable;
/*      */ import java.sql.Array;
/*      */ import java.sql.Blob;
/*      */ import java.sql.Clob;
/*      */ import java.sql.Connection;
/*      */ import java.sql.DatabaseMetaData;
/*      */ import java.sql.PreparedStatement;
/*      */ import java.sql.ResultSet;
/*      */ import java.sql.ResultSetMetaData;
/*      */ import java.sql.SQLData;
/*      */ import java.sql.SQLException;
/*      */ import java.sql.Savepoint;
/*      */ import java.sql.Struct;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Map;
/*      */ import java.util.Vector;
/*      */ import javax.sql.RowSetInternal;
/*      */ import javax.sql.rowset.CachedRowSet;
/*      */ import javax.sql.rowset.RowSetMetaDataImpl;
/*      */ import javax.sql.rowset.serial.SQLInputImpl;
/*      */ import javax.sql.rowset.serial.SerialArray;
/*      */ import javax.sql.rowset.serial.SerialBlob;
/*      */ import javax.sql.rowset.serial.SerialClob;
/*      */ import javax.sql.rowset.serial.SerialStruct;
/*      */ import javax.sql.rowset.spi.SyncProviderException;
/*      */ import javax.sql.rowset.spi.TransactionalWriter;
/*      */ import sun.reflect.misc.ReflectUtil;
/*      */ 
/*      */ public class CachedRowSetWriter
/*      */   implements TransactionalWriter, Serializable
/*      */ {
/*      */   private transient Connection con;
/*      */   private String selectCmd;
/*      */   private String updateCmd;
/*      */   private String updateWhere;
/*      */   private String deleteCmd;
/*      */   private String deleteWhere;
/*      */   private String insertCmd;
/*      */   private int[] keyCols;
/*      */   private Object[] params;
/*      */   private CachedRowSetReader reader;
/*      */   private ResultSetMetaData callerMd;
/*      */   private int callerColumnCount;
/*      */   private CachedRowSetImpl crsResolve;
/*      */   private ArrayList status;
/*      */   private int iChangedValsInDbAndCRS;
/*      */   private int iChangedValsinDbOnly;
/*      */   private JdbcRowSetResourceBundle resBundle;
/*      */   static final long serialVersionUID = -8506030970299413976L;
/*      */ 
/*      */   public CachedRowSetWriter()
/*      */   {
/*      */     try
/*      */     {
/*  205 */       this.resBundle = JdbcRowSetResourceBundle.getJdbcRowSetResourceBundle();
/*      */     } catch (IOException localIOException) {
/*  207 */       throw new RuntimeException(localIOException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean writeData(RowSetInternal paramRowSetInternal)
/*      */     throws SQLException
/*      */   {
/*  268 */     boolean bool1 = false;
/*  269 */     boolean bool2 = false;
/*  270 */     PreparedStatement localPreparedStatement = null;
/*  271 */     this.iChangedValsInDbAndCRS = 0;
/*  272 */     this.iChangedValsinDbOnly = 0;
/*      */ 
/*  275 */     CachedRowSetImpl localCachedRowSetImpl = (CachedRowSetImpl)paramRowSetInternal;
/*      */ 
/*  277 */     this.crsResolve = new CachedRowSetImpl();
/*      */ 
/*  283 */     this.con = this.reader.connect(paramRowSetInternal);
/*      */ 
/*  286 */     if (this.con == null) {
/*  287 */       throw new SQLException(this.resBundle.handleGetObject("crswriter.connect").toString());
/*      */     }
/*      */ 
/*  301 */     initSQLStatements(localCachedRowSetImpl);
/*      */ 
/*  304 */     RowSetMetaDataImpl localRowSetMetaDataImpl1 = (RowSetMetaDataImpl)localCachedRowSetImpl.getMetaData();
/*  305 */     RowSetMetaDataImpl localRowSetMetaDataImpl2 = new RowSetMetaDataImpl();
/*      */ 
/*  307 */     int i = localRowSetMetaDataImpl1.getColumnCount();
/*  308 */     int j = localCachedRowSetImpl.size() + 1;
/*  309 */     this.status = new ArrayList(j);
/*      */ 
/*  311 */     this.status.add(0, null);
/*  312 */     localRowSetMetaDataImpl2.setColumnCount(i);
/*      */ 
/*  314 */     for (int k = 1; k <= i; k++) {
/*  315 */       localRowSetMetaDataImpl2.setColumnType(k, localRowSetMetaDataImpl1.getColumnType(k));
/*  316 */       localRowSetMetaDataImpl2.setColumnName(k, localRowSetMetaDataImpl1.getColumnName(k));
/*  317 */       localRowSetMetaDataImpl2.setNullable(k, 2);
/*      */     }
/*  319 */     this.crsResolve.setMetaData(localRowSetMetaDataImpl2);
/*      */ 
/*  324 */     if (this.callerColumnCount < 1)
/*      */     {
/*  326 */       if (this.reader.getCloseConnection() == true)
/*  327 */         this.con.close();
/*  328 */       return true;
/*      */     }
/*      */ 
/*  331 */     bool2 = localCachedRowSetImpl.getShowDeleted();
/*  332 */     localCachedRowSetImpl.setShowDeleted(true);
/*      */ 
/*  335 */     localCachedRowSetImpl.beforeFirst();
/*      */ 
/*  337 */     k = 1;
/*  338 */     while (localCachedRowSetImpl.next()) {
/*  339 */       if (localCachedRowSetImpl.rowDeleted())
/*      */       {
/*  341 */         if ((bool1 = deleteOriginalRow(localCachedRowSetImpl, this.crsResolve) == true ? 1 : 0) != 0) {
/*  342 */           this.status.add(k, Integer.valueOf(1));
/*      */         }
/*      */         else
/*      */         {
/*  346 */           this.status.add(k, Integer.valueOf(3));
/*      */         }
/*      */       }
/*  349 */       else if (localCachedRowSetImpl.rowInserted())
/*      */       {
/*  352 */         localPreparedStatement = this.con.prepareStatement(this.insertCmd);
/*  353 */         if ((bool1 = insertNewRow(localCachedRowSetImpl, localPreparedStatement, this.crsResolve)) == true) {
/*  354 */           this.status.add(k, Integer.valueOf(2));
/*      */         }
/*      */         else
/*      */         {
/*  358 */           this.status.add(k, Integer.valueOf(3));
/*      */         }
/*  360 */       } else if (localCachedRowSetImpl.rowUpdated())
/*      */       {
/*  362 */         if ((bool1 = updateOriginalRow(localCachedRowSetImpl) == true ? 1 : 0) != 0) {
/*  363 */           this.status.add(k, Integer.valueOf(0));
/*      */         }
/*      */         else
/*      */         {
/*  367 */           this.status.add(k, Integer.valueOf(3));
/*      */         }
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*  378 */         m = localCachedRowSetImpl.getMetaData().getColumnCount();
/*  379 */         this.status.add(k, Integer.valueOf(3));
/*      */ 
/*  381 */         this.crsResolve.moveToInsertRow();
/*  382 */         for (n = 0; n < i; n++) {
/*  383 */           this.crsResolve.updateNull(n + 1);
/*      */         }
/*      */ 
/*  386 */         this.crsResolve.insertRow();
/*  387 */         this.crsResolve.moveToCurrentRow();
/*      */       }
/*      */ 
/*  390 */       k++;
/*      */     }
/*      */ 
/*  394 */     if (localPreparedStatement != null) {
/*  395 */       localPreparedStatement.close();
/*      */     }
/*  397 */     localCachedRowSetImpl.setShowDeleted(bool2);
/*      */ 
/*  399 */     int m = 0;
/*  400 */     for (int n = 1; n < this.status.size(); n++)
/*      */     {
/*  402 */       if (!this.status.get(n).equals(Integer.valueOf(3)))
/*      */       {
/*  404 */         m = 1;
/*  405 */         break;
/*      */       }
/*      */     }
/*      */ 
/*  409 */     localCachedRowSetImpl.beforeFirst();
/*  410 */     this.crsResolve.beforeFirst();
/*      */ 
/*  412 */     if (m != 0) {
/*  413 */       SyncProviderException localSyncProviderException = new SyncProviderException(this.status.size() - 1 + this.resBundle.handleGetObject("crswriter.conflictsno").toString());
/*      */ 
/*  416 */       SyncResolverImpl localSyncResolverImpl = (SyncResolverImpl)localSyncProviderException.getSyncResolver();
/*      */ 
/*  418 */       localSyncResolverImpl.setCachedRowSet(localCachedRowSetImpl);
/*  419 */       localSyncResolverImpl.setCachedRowSetResolver(this.crsResolve);
/*      */ 
/*  421 */       localSyncResolverImpl.setStatus(this.status);
/*  422 */       localSyncResolverImpl.setCachedRowSetWriter(this);
/*      */ 
/*  424 */       throw localSyncProviderException;
/*      */     }
/*  426 */     return true;
/*      */   }
/*      */ 
/*      */   private boolean updateOriginalRow(CachedRowSet paramCachedRowSet)
/*      */     throws SQLException
/*      */   {
/*  465 */     int i = 0;
/*  466 */     int j = 0;
/*      */ 
/*  469 */     ResultSet localResultSet1 = paramCachedRowSet.getOriginalRow();
/*  470 */     localResultSet1.next();
/*      */     try
/*      */     {
/*  473 */       this.updateWhere = buildWhereClause(this.updateWhere, localResultSet1);
/*      */ 
/*  487 */       String str1 = this.selectCmd.toLowerCase();
/*      */ 
/*  489 */       int k = str1.indexOf("where");
/*      */ 
/*  491 */       if (k != -1)
/*      */       {
/*  493 */         String str2 = this.selectCmd.substring(0, k);
/*  494 */         this.selectCmd = str2;
/*      */       }
/*      */ 
/*  497 */       PreparedStatement localPreparedStatement = this.con.prepareStatement(this.selectCmd + this.updateWhere, 1005, 1007);
/*      */ 
/*  500 */       for (i = 0; i < this.keyCols.length; i++) {
/*  501 */         if (this.params[i] != null) {
/*  502 */           localPreparedStatement.setObject(++j, this.params[i]);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */       try
/*      */       {
/*  509 */         localPreparedStatement.setMaxRows(paramCachedRowSet.getMaxRows());
/*  510 */         localPreparedStatement.setMaxFieldSize(paramCachedRowSet.getMaxFieldSize());
/*  511 */         localPreparedStatement.setEscapeProcessing(paramCachedRowSet.getEscapeProcessing());
/*  512 */         localPreparedStatement.setQueryTimeout(paramCachedRowSet.getQueryTimeout());
/*      */       }
/*      */       catch (Exception localException1)
/*      */       {
/*      */       }
/*  517 */       ResultSet localResultSet2 = null;
/*  518 */       localResultSet2 = localPreparedStatement.executeQuery();
/*  519 */       ResultSetMetaData localResultSetMetaData = localResultSet2.getMetaData();
/*      */ 
/*  521 */       if (localResultSet2.next()) {
/*  522 */         if (localResultSet2.next())
/*      */         {
/*  533 */           return true;
/*      */         }
/*      */ 
/*  540 */         localResultSet2.first();
/*      */ 
/*  543 */         int m = 0;
/*  544 */         Vector localVector = new Vector();
/*  545 */         String str3 = this.updateCmd;
/*      */ 
/*  549 */         int n = 1;
/*  550 */         Object localObject4 = null;
/*      */ 
/*  555 */         int i1 = 1;
/*  556 */         int i2 = 1;
/*      */ 
/*  558 */         this.crsResolve.moveToInsertRow();
/*      */         Object localObject5;
/*  560 */         for (i = 1; i <= this.callerColumnCount; i++) {
/*  561 */           Object localObject1 = localResultSet1.getObject(i);
/*  562 */           Object localObject2 = paramCachedRowSet.getObject(i);
/*  563 */           Object localObject3 = localResultSet2.getObject(i);
/*      */ 
/*  570 */           localObject5 = paramCachedRowSet.getTypeMap() == null ? this.con.getTypeMap() : paramCachedRowSet.getTypeMap();
/*  571 */           if ((localObject3 instanceof Struct))
/*      */           {
/*  573 */             Struct localStruct = (Struct)localObject3;
/*      */ 
/*  576 */             Class localClass = null;
/*  577 */             localClass = (Class)((Map)localObject5).get(localStruct.getSQLTypeName());
/*  578 */             if (localClass != null)
/*      */             {
/*  580 */               SQLData localSQLData = null;
/*      */               try {
/*  582 */                 localSQLData = (SQLData)ReflectUtil.newInstance(localClass);
/*      */               } catch (Exception localException2) {
/*  584 */                 throw new SQLException("Unable to Instantiate: ", localException2);
/*      */               }
/*      */ 
/*  587 */               Object[] arrayOfObject = localStruct.getAttributes((Map)localObject5);
/*      */ 
/*  589 */               SQLInputImpl localSQLInputImpl = new SQLInputImpl(arrayOfObject, (Map)localObject5);
/*      */ 
/*  591 */               localSQLData.readSQL(localSQLInputImpl, localStruct.getSQLTypeName());
/*  592 */               localObject3 = localSQLData;
/*      */             }
/*  594 */           } else if ((localObject3 instanceof SQLData)) {
/*  595 */             localObject3 = new SerialStruct((SQLData)localObject3, (Map)localObject5);
/*  596 */           } else if ((localObject3 instanceof Blob)) {
/*  597 */             localObject3 = new SerialBlob((Blob)localObject3);
/*  598 */           } else if ((localObject3 instanceof Clob)) {
/*  599 */             localObject3 = new SerialClob((Clob)localObject3);
/*  600 */           } else if ((localObject3 instanceof Array)) {
/*  601 */             localObject3 = new SerialArray((Array)localObject3, (Map)localObject5);
/*      */           }
/*      */ 
/*  605 */           n = 1;
/*      */ 
/*  612 */           if ((localObject3 == null) && (localObject1 != null))
/*      */           {
/*  617 */             this.iChangedValsinDbOnly += 1;
/*      */ 
/*  620 */             n = 0;
/*  621 */             localObject4 = localObject3;
/*      */           }
/*  629 */           else if ((localObject3 != null) && (!localObject3.equals(localObject1)))
/*      */           {
/*  635 */             this.iChangedValsinDbOnly += 1;
/*      */ 
/*  638 */             n = 0;
/*  639 */             localObject4 = localObject3;
/*  640 */           } else if ((localObject1 == null) || (localObject2 == null))
/*      */           {
/*  648 */             if ((i1 == 0) || (i2 == 0)) {
/*  649 */               str3 = str3 + ", ";
/*      */             }
/*  651 */             str3 = str3 + paramCachedRowSet.getMetaData().getColumnName(i);
/*  652 */             localVector.add(Integer.valueOf(i));
/*  653 */             str3 = str3 + " = ? ";
/*  654 */             i1 = 0;
/*      */           }
/*  662 */           else if (localObject1.equals(localObject2)) {
/*  663 */             m++;
/*      */           }
/*  672 */           else if (!localObject1.equals(localObject2))
/*      */           {
/*  689 */             if (paramCachedRowSet.columnUpdated(i)) {
/*  690 */               if (localObject3.equals(localObject1))
/*      */               {
/*  694 */                 if ((i2 == 0) || (i1 == 0)) {
/*  695 */                   str3 = str3 + ", ";
/*      */                 }
/*  697 */                 str3 = str3 + paramCachedRowSet.getMetaData().getColumnName(i);
/*  698 */                 localVector.add(Integer.valueOf(i));
/*  699 */                 str3 = str3 + " = ? ";
/*  700 */                 i2 = 0;
/*      */               }
/*      */               else
/*      */               {
/*  706 */                 n = 0;
/*  707 */                 localObject4 = localObject3;
/*  708 */                 this.iChangedValsInDbAndCRS += 1;
/*      */               }
/*      */             }
/*      */           }
/*      */ 
/*  713 */           if (n == 0)
/*  714 */             this.crsResolve.updateObject(i, localObject4);
/*      */           else {
/*  716 */             this.crsResolve.updateNull(i);
/*      */           }
/*      */         }
/*      */ 
/*  720 */         localResultSet2.close();
/*  721 */         localPreparedStatement.close();
/*      */ 
/*  723 */         this.crsResolve.insertRow();
/*  724 */         this.crsResolve.moveToCurrentRow();
/*      */ 
/*  733 */         if (((i1 == 0) && (localVector.size() == 0)) || (m == this.callerColumnCount))
/*      */         {
/*  735 */           return false;
/*      */         }
/*      */ 
/*  738 */         if ((this.iChangedValsInDbAndCRS != 0) || (this.iChangedValsinDbOnly != 0)) {
/*  739 */           return true;
/*      */         }
/*      */ 
/*  743 */         str3 = str3 + this.updateWhere;
/*      */ 
/*  745 */         localPreparedStatement = this.con.prepareStatement(str3);
/*      */ 
/*  748 */         for (i = 0; i < localVector.size(); i++) {
/*  749 */           localObject5 = paramCachedRowSet.getObject(((Integer)localVector.get(i)).intValue());
/*  750 */           if (localObject5 != null)
/*  751 */             localPreparedStatement.setObject(i + 1, localObject5);
/*      */           else
/*  753 */             localPreparedStatement.setNull(i + 1, paramCachedRowSet.getMetaData().getColumnType(i + 1));
/*      */         }
/*  755 */         j = i;
/*      */ 
/*  758 */         for (i = 0; i < this.keyCols.length; i++) {
/*  759 */           if (this.params[i] != null) {
/*  760 */             localPreparedStatement.setObject(++j, this.params[i]);
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*  766 */         i = localPreparedStatement.executeUpdate();
/*      */ 
/*  776 */         return false;
/*      */       }
/*      */ 
/*  793 */       return true;
/*      */     }
/*      */     catch (SQLException localSQLException) {
/*  796 */       localSQLException.printStackTrace();
/*      */ 
/*  799 */       this.crsResolve.moveToInsertRow();
/*      */ 
/*  801 */       for (i = 1; i <= this.callerColumnCount; i++) {
/*  802 */         this.crsResolve.updateNull(i);
/*      */       }
/*      */ 
/*  805 */       this.crsResolve.insertRow();
/*  806 */       this.crsResolve.moveToCurrentRow();
/*      */     }
/*  808 */     return true;
/*      */   }
/*      */ 
/*      */   private boolean insertNewRow(CachedRowSet paramCachedRowSet, PreparedStatement paramPreparedStatement, CachedRowSetImpl paramCachedRowSetImpl)
/*      */     throws SQLException
/*      */   {
/*  828 */     int i = 0;
/*  829 */     int j = paramCachedRowSet.getMetaData().getColumnCount();
/*      */ 
/*  831 */     boolean bool = false;
/*  832 */     PreparedStatement localPreparedStatement = this.con.prepareStatement(this.selectCmd, 1005, 1007);
/*      */ 
/*  834 */     ResultSet localResultSet2 = null;
/*  835 */     DatabaseMetaData localDatabaseMetaData = this.con.getMetaData();
/*  836 */     ResultSet localResultSet1 = localPreparedStatement.executeQuery();
/*  837 */     String str1 = paramCachedRowSet.getTableName();
/*  838 */     localResultSet2 = localDatabaseMetaData.getPrimaryKeys(null, null, str1);
/*  839 */     String[] arrayOfString = new String[j];
/*  840 */     int k = 0;
/*  841 */     while (localResultSet2.next()) {
/*  842 */       String str2 = localResultSet2.getString("COLUMN_NAME");
/*  843 */       arrayOfString[k] = str2;
/*  844 */       k++;
/*      */     }
/*      */ 
/*  847 */     if (localResultSet1.next()) {
/*  848 */       for (int m = 0; m < arrayOfString.length; m++) {
/*  849 */         if (arrayOfString[m] != null) {
/*  850 */           if (paramCachedRowSet.getObject(arrayOfString[m]) == null) {
/*      */             break;
/*      */           }
/*  853 */           String str3 = paramCachedRowSet.getObject(arrayOfString[m]).toString();
/*  854 */           String str4 = localResultSet1.getObject(arrayOfString[m]).toString();
/*  855 */           if (str3.equals(str4)) {
/*  856 */             bool = true;
/*  857 */             this.crsResolve.moveToInsertRow();
/*  858 */             for (i = 1; i <= j; i++) {
/*  859 */               String str5 = localResultSet1.getMetaData().getColumnName(i);
/*  860 */               if (str5.equals(arrayOfString[m]))
/*  861 */                 this.crsResolve.updateObject(i, str4);
/*      */               else
/*  863 */                 this.crsResolve.updateNull(i);
/*      */             }
/*  865 */             this.crsResolve.insertRow();
/*  866 */             this.crsResolve.moveToCurrentRow();
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  871 */     if (bool)
/*  872 */       return bool;
/*      */     try
/*      */     {
/*  875 */       for (i = 1; i <= j; i++) {
/*  876 */         Object localObject = paramCachedRowSet.getObject(i);
/*  877 */         if (localObject != null)
/*  878 */           paramPreparedStatement.setObject(i, localObject);
/*      */         else {
/*  880 */           paramPreparedStatement.setNull(i, paramCachedRowSet.getMetaData().getColumnType(i));
/*      */         }
/*      */       }
/*      */ 
/*  884 */       i = paramPreparedStatement.executeUpdate();
/*  885 */       return false;
/*      */     }
/*      */     catch (SQLException localSQLException)
/*      */     {
/*  895 */       this.crsResolve.moveToInsertRow();
/*      */ 
/*  897 */       for (i = 1; i <= j; i++) {
/*  898 */         this.crsResolve.updateNull(i);
/*      */       }
/*      */ 
/*  901 */       this.crsResolve.insertRow();
/*  902 */       this.crsResolve.moveToCurrentRow();
/*      */     }
/*  904 */     return true;
/*      */   }
/*      */ 
/*      */   private boolean deleteOriginalRow(CachedRowSet paramCachedRowSet, CachedRowSetImpl paramCachedRowSetImpl)
/*      */     throws SQLException
/*      */   {
/*  929 */     int j = 0;
/*      */ 
/*  932 */     ResultSet localResultSet1 = paramCachedRowSet.getOriginalRow();
/*  933 */     localResultSet1.next();
/*      */ 
/*  935 */     this.deleteWhere = buildWhereClause(this.deleteWhere, localResultSet1);
/*  936 */     PreparedStatement localPreparedStatement = this.con.prepareStatement(this.selectCmd + this.deleteWhere, 1005, 1007);
/*      */ 
/*  939 */     for (int i = 0; i < this.keyCols.length; i++) {
/*  940 */       if (this.params[i] != null) {
/*  941 */         localPreparedStatement.setObject(++j, this.params[i]);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/*  948 */       localPreparedStatement.setMaxRows(paramCachedRowSet.getMaxRows());
/*  949 */       localPreparedStatement.setMaxFieldSize(paramCachedRowSet.getMaxFieldSize());
/*  950 */       localPreparedStatement.setEscapeProcessing(paramCachedRowSet.getEscapeProcessing());
/*  951 */       localPreparedStatement.setQueryTimeout(paramCachedRowSet.getQueryTimeout());
/*      */     }
/*      */     catch (Exception localException)
/*      */     {
/*      */     }
/*      */ 
/*  959 */     ResultSet localResultSet2 = localPreparedStatement.executeQuery();
/*      */ 
/*  961 */     if (localResultSet2.next() == true) {
/*  962 */       if (localResultSet2.next())
/*      */       {
/*  964 */         return true;
/*      */       }
/*  966 */       localResultSet2.first();
/*      */ 
/*  970 */       int k = 0;
/*      */ 
/*  972 */       paramCachedRowSetImpl.moveToInsertRow();
/*      */ 
/*  974 */       for (i = 1; i <= paramCachedRowSet.getMetaData().getColumnCount(); i++)
/*      */       {
/*  976 */         localObject1 = localResultSet1.getObject(i);
/*  977 */         Object localObject2 = localResultSet2.getObject(i);
/*      */ 
/*  979 */         if ((localObject1 != null) && (localObject2 != null)) {
/*  980 */           if (!localObject1.toString().equals(localObject2.toString())) {
/*  981 */             k = 1;
/*  982 */             paramCachedRowSetImpl.updateObject(i, localResultSet1.getObject(i));
/*      */           }
/*      */         }
/*  985 */         else paramCachedRowSetImpl.updateNull(i);
/*      */ 
/*      */       }
/*      */ 
/*  989 */       paramCachedRowSetImpl.insertRow();
/*  990 */       paramCachedRowSetImpl.moveToCurrentRow();
/*      */ 
/*  992 */       if (k != 0)
/*      */       {
/*  996 */         return true;
/*      */       }
/*      */ 
/* 1003 */       Object localObject1 = this.deleteCmd + this.deleteWhere;
/* 1004 */       localPreparedStatement = this.con.prepareStatement((String)localObject1);
/*      */ 
/* 1006 */       j = 0;
/* 1007 */       for (i = 0; i < this.keyCols.length; i++) {
/* 1008 */         if (this.params[i] != null) {
/* 1009 */           localPreparedStatement.setObject(++j, this.params[i]);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1015 */       if (localPreparedStatement.executeUpdate() != 1) {
/* 1016 */         return true;
/*      */       }
/* 1018 */       localPreparedStatement.close();
/*      */     }
/*      */     else {
/* 1021 */       return true;
/*      */     }
/*      */ 
/* 1025 */     return false;
/*      */   }
/*      */ 
/*      */   public void setReader(CachedRowSetReader paramCachedRowSetReader)
/*      */     throws SQLException
/*      */   {
/* 1034 */     this.reader = paramCachedRowSetReader;
/*      */   }
/*      */ 
/*      */   public CachedRowSetReader getReader()
/*      */     throws SQLException
/*      */   {
/* 1043 */     return this.reader;
/*      */   }
/*      */ 
/*      */   private void initSQLStatements(CachedRowSet paramCachedRowSet)
/*      */     throws SQLException
/*      */   {
/* 1060 */     this.callerMd = paramCachedRowSet.getMetaData();
/* 1061 */     this.callerColumnCount = this.callerMd.getColumnCount();
/* 1062 */     if (this.callerColumnCount < 1)
/*      */     {
/* 1064 */       return;
/*      */     }
/*      */ 
/* 1071 */     String str1 = paramCachedRowSet.getTableName();
/* 1072 */     if (str1 == null)
/*      */     {
/* 1078 */       str1 = this.callerMd.getTableName(1);
/* 1079 */       if ((str1 == null) || (str1.length() == 0)) {
/* 1080 */         throw new SQLException(this.resBundle.handleGetObject("crswriter.tname").toString());
/*      */       }
/*      */     }
/* 1083 */     String str2 = this.callerMd.getCatalogName(1);
/* 1084 */     String str3 = this.callerMd.getSchemaName(1);
/* 1085 */     DatabaseMetaData localDatabaseMetaData = this.con.getMetaData();
/*      */ 
/* 1092 */     this.selectCmd = "SELECT ";
/* 1093 */     for (int i = 1; i <= this.callerColumnCount; i++) {
/* 1094 */       this.selectCmd += this.callerMd.getColumnName(i);
/* 1095 */       if (i < this.callerMd.getColumnCount())
/* 1096 */         this.selectCmd += ", ";
/*      */       else {
/* 1098 */         this.selectCmd += " ";
/*      */       }
/*      */     }
/*      */ 
/* 1102 */     this.selectCmd = (this.selectCmd + "FROM " + buildTableName(localDatabaseMetaData, str2, str3, str1));
/*      */ 
/* 1107 */     this.updateCmd = ("UPDATE " + buildTableName(localDatabaseMetaData, str2, str3, str1));
/*      */ 
/* 1120 */     String str4 = this.updateCmd.toLowerCase();
/*      */ 
/* 1122 */     int j = str4.indexOf("where");
/*      */ 
/* 1124 */     if (j != -1)
/*      */     {
/* 1126 */       this.updateCmd = this.updateCmd.substring(0, j);
/*      */     }
/* 1128 */     this.updateCmd += "SET ";
/*      */ 
/* 1133 */     this.insertCmd = ("INSERT INTO " + buildTableName(localDatabaseMetaData, str2, str3, str1));
/*      */ 
/* 1135 */     this.insertCmd += "(";
/* 1136 */     for (i = 1; i <= this.callerColumnCount; i++) {
/* 1137 */       this.insertCmd += this.callerMd.getColumnName(i);
/* 1138 */       if (i < this.callerMd.getColumnCount())
/* 1139 */         this.insertCmd += ", ";
/*      */       else
/* 1141 */         this.insertCmd += ") VALUES (";
/*      */     }
/* 1143 */     for (i = 1; i <= this.callerColumnCount; i++) {
/* 1144 */       this.insertCmd += "?";
/* 1145 */       if (i < this.callerColumnCount)
/* 1146 */         this.insertCmd += ", ";
/*      */       else {
/* 1148 */         this.insertCmd += ")";
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1154 */     this.deleteCmd = ("DELETE FROM " + buildTableName(localDatabaseMetaData, str2, str3, str1));
/*      */ 
/* 1160 */     buildKeyDesc(paramCachedRowSet);
/*      */   }
/*      */ 
/*      */   private String buildTableName(DatabaseMetaData paramDatabaseMetaData, String paramString1, String paramString2, String paramString3)
/*      */     throws SQLException
/*      */   {
/* 1184 */     String str = "";
/*      */ 
/* 1186 */     paramString1 = paramString1.trim();
/* 1187 */     paramString2 = paramString2.trim();
/* 1188 */     paramString3 = paramString3.trim();
/*      */ 
/* 1190 */     if (paramDatabaseMetaData.isCatalogAtStart() == true) {
/* 1191 */       if ((paramString1 != null) && (paramString1.length() > 0)) {
/* 1192 */         str = str + paramString1 + paramDatabaseMetaData.getCatalogSeparator();
/*      */       }
/* 1194 */       if ((paramString2 != null) && (paramString2.length() > 0)) {
/* 1195 */         str = str + paramString2 + ".";
/*      */       }
/* 1197 */       str = str + paramString3;
/*      */     } else {
/* 1199 */       if ((paramString2 != null) && (paramString2.length() > 0)) {
/* 1200 */         str = str + paramString2 + ".";
/*      */       }
/* 1202 */       str = str + paramString3;
/* 1203 */       if ((paramString1 != null) && (paramString1.length() > 0)) {
/* 1204 */         str = str + paramDatabaseMetaData.getCatalogSeparator() + paramString1;
/*      */       }
/*      */     }
/* 1207 */     str = str + " ";
/* 1208 */     return str;
/*      */   }
/*      */ 
/*      */   private void buildKeyDesc(CachedRowSet paramCachedRowSet)
/*      */     throws SQLException
/*      */   {
/* 1232 */     this.keyCols = paramCachedRowSet.getKeyColumns();
/* 1233 */     ResultSetMetaData localResultSetMetaData = paramCachedRowSet.getMetaData();
/* 1234 */     if ((this.keyCols == null) || (this.keyCols.length == 0)) {
/* 1235 */       ArrayList localArrayList = new ArrayList();
/*      */ 
/* 1237 */       for (int i = 0; i < this.callerColumnCount; i++)
/* 1238 */         if ((localResultSetMetaData.getColumnType(i + 1) != 2005) && (localResultSetMetaData.getColumnType(i + 1) != 2002) && (localResultSetMetaData.getColumnType(i + 1) != 2009) && (localResultSetMetaData.getColumnType(i + 1) != 2004) && (localResultSetMetaData.getColumnType(i + 1) != 2003) && (localResultSetMetaData.getColumnType(i + 1) != 1111))
/*      */         {
/* 1244 */           localArrayList.add(Integer.valueOf(i + 1));
/*      */         }
/* 1246 */       this.keyCols = new int[localArrayList.size()];
/* 1247 */       for (i = 0; i < localArrayList.size(); i++)
/* 1248 */         this.keyCols[i] = ((Integer)localArrayList.get(i)).intValue();
/*      */     }
/* 1250 */     this.params = new Object[this.keyCols.length];
/*      */   }
/*      */ 
/*      */   private String buildWhereClause(String paramString, ResultSet paramResultSet)
/*      */     throws SQLException
/*      */   {
/* 1279 */     paramString = "WHERE ";
/*      */ 
/* 1281 */     for (int i = 0; i < this.keyCols.length; i++) {
/* 1282 */       if (i > 0) {
/* 1283 */         paramString = paramString + "AND ";
/*      */       }
/* 1285 */       paramString = paramString + this.callerMd.getColumnName(this.keyCols[i]);
/* 1286 */       this.params[i] = paramResultSet.getObject(this.keyCols[i]);
/* 1287 */       if (paramResultSet.wasNull() == true)
/* 1288 */         paramString = paramString + " IS NULL ";
/*      */       else {
/* 1290 */         paramString = paramString + " = ? ";
/*      */       }
/*      */     }
/* 1293 */     return paramString;
/*      */   }
/*      */ 
/*      */   void updateResolvedConflictToDB(CachedRowSet paramCachedRowSet, Connection paramConnection)
/*      */     throws SQLException
/*      */   {
/* 1299 */     String str1 = "WHERE ";
/* 1300 */     String str2 = " ";
/* 1301 */     String str3 = "UPDATE ";
/* 1302 */     int i = paramCachedRowSet.getMetaData().getColumnCount();
/* 1303 */     int[] arrayOfInt = paramCachedRowSet.getKeyColumns();
/*      */ 
/* 1305 */     String str4 = "";
/*      */ 
/* 1307 */     str1 = buildWhereClause(str1, paramCachedRowSet);
/*      */ 
/* 1309 */     if ((arrayOfInt == null) || (arrayOfInt.length == 0)) {
/* 1310 */       arrayOfInt = new int[i];
/* 1311 */       for (j = 0; j < arrayOfInt.length; ) {
/* 1312 */         arrayOfInt[(j++)] = j;
/*      */       }
/*      */     }
/* 1315 */     Object[] arrayOfObject = new Object[arrayOfInt.length];
/*      */ 
/* 1317 */     str3 = "UPDATE " + buildTableName(paramConnection.getMetaData(), paramCachedRowSet.getMetaData().getCatalogName(1), paramCachedRowSet.getMetaData().getSchemaName(1), paramCachedRowSet.getTableName());
/*      */ 
/* 1324 */     str3 = str3 + "SET ";
/*      */ 
/* 1326 */     int j = 1;
/*      */ 
/* 1328 */     for (int k = 1; k <= i; k++) {
/* 1329 */       if (paramCachedRowSet.columnUpdated(k)) {
/* 1330 */         if (j == 0) {
/* 1331 */           str4 = str4 + ", ";
/*      */         }
/* 1333 */         str4 = str4 + paramCachedRowSet.getMetaData().getColumnName(k);
/* 1334 */         str4 = str4 + " = ? ";
/* 1335 */         j = 0;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1340 */     str3 = str3 + str4;
/* 1341 */     str1 = "WHERE ";
/*      */ 
/* 1343 */     for (k = 0; k < arrayOfInt.length; k++) {
/* 1344 */       if (k > 0) {
/* 1345 */         str1 = str1 + "AND ";
/*      */       }
/* 1347 */       str1 = str1 + paramCachedRowSet.getMetaData().getColumnName(arrayOfInt[k]);
/* 1348 */       arrayOfObject[k] = paramCachedRowSet.getObject(arrayOfInt[k]);
/* 1349 */       if (paramCachedRowSet.wasNull() == true)
/* 1350 */         str1 = str1 + " IS NULL ";
/*      */       else {
/* 1352 */         str1 = str1 + " = ? ";
/*      */       }
/*      */     }
/* 1355 */     str3 = str3 + str1;
/*      */ 
/* 1357 */     PreparedStatement localPreparedStatement = paramConnection.prepareStatement(str3);
/*      */ 
/* 1359 */     k = 0;
/* 1360 */     for (int m = 0; m < i; m++) {
/* 1361 */       if (paramCachedRowSet.columnUpdated(m + 1)) {
/* 1362 */         Object localObject = paramCachedRowSet.getObject(m + 1);
/* 1363 */         if (localObject != null)
/* 1364 */           localPreparedStatement.setObject(++k, localObject);
/*      */         else {
/* 1366 */           localPreparedStatement.setNull(m + 1, paramCachedRowSet.getMetaData().getColumnType(m + 1));
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1372 */     for (m = 0; m < arrayOfInt.length; m++) {
/* 1373 */       if (arrayOfObject[m] != null) {
/* 1374 */         localPreparedStatement.setObject(++k, arrayOfObject[m]);
/*      */       }
/*      */     }
/*      */ 
/* 1378 */     m = localPreparedStatement.executeUpdate();
/*      */   }
/*      */ 
/*      */   public void commit()
/*      */     throws SQLException
/*      */   {
/* 1386 */     this.con.commit();
/* 1387 */     if (this.reader.getCloseConnection() == true)
/* 1388 */       this.con.close();
/*      */   }
/*      */ 
/*      */   public void commit(CachedRowSetImpl paramCachedRowSetImpl, boolean paramBoolean) throws SQLException
/*      */   {
/* 1393 */     this.con.commit();
/* 1394 */     if ((paramBoolean) && 
/* 1395 */       (paramCachedRowSetImpl.getCommand() != null)) {
/* 1396 */       paramCachedRowSetImpl.execute(this.con);
/*      */     }
/*      */ 
/* 1399 */     if (this.reader.getCloseConnection() == true)
/* 1400 */       this.con.close();
/*      */   }
/*      */ 
/*      */   public void rollback()
/*      */     throws SQLException
/*      */   {
/* 1408 */     this.con.rollback();
/* 1409 */     if (this.reader.getCloseConnection() == true)
/* 1410 */       this.con.close();
/*      */   }
/*      */ 
/*      */   public void rollback(Savepoint paramSavepoint)
/*      */     throws SQLException
/*      */   {
/* 1418 */     this.con.rollback(paramSavepoint);
/* 1419 */     if (this.reader.getCloseConnection() == true)
/* 1420 */       this.con.close();
/*      */   }
/*      */ 
/*      */   private void readObject(ObjectInputStream paramObjectInputStream)
/*      */     throws IOException, ClassNotFoundException
/*      */   {
/* 1426 */     paramObjectInputStream.defaultReadObject();
/*      */     try
/*      */     {
/* 1429 */       this.resBundle = JdbcRowSetResourceBundle.getJdbcRowSetResourceBundle();
/*      */     } catch (IOException localIOException) {
/* 1431 */       throw new RuntimeException(localIOException);
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.rowset.internal.CachedRowSetWriter
 * JD-Core Version:    0.6.2
 */