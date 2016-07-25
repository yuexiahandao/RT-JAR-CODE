/*      */ package com.sun.rowset.internal;
/*      */ 
/*      */ import com.sun.rowset.CachedRowSetImpl;
/*      */ import com.sun.rowset.JdbcRowSetResourceBundle;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.Reader;
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
/*      */ import java.sql.Savepoint;
/*      */ import java.sql.Statement;
/*      */ import java.sql.Time;
/*      */ import java.sql.Timestamp;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Calendar;
/*      */ import java.util.Collection;
/*      */ import java.util.Map;
/*      */ import javax.sql.RowSet;
/*      */ import javax.sql.RowSetEvent;
/*      */ import javax.sql.RowSetMetaData;
/*      */ import javax.sql.rowset.CachedRowSet;
/*      */ import javax.sql.rowset.RowSetMetaDataImpl;
/*      */ import javax.sql.rowset.RowSetWarning;
/*      */ import javax.sql.rowset.spi.SyncProvider;
/*      */ import javax.sql.rowset.spi.SyncProviderException;
/*      */ import javax.sql.rowset.spi.SyncResolver;
/*      */ 
/*      */ public class SyncResolverImpl extends CachedRowSetImpl
/*      */   implements SyncResolver
/*      */ {
/*      */   private CachedRowSetImpl crsRes;
/*      */   private CachedRowSetImpl crsSync;
/*      */   private ArrayList stats;
/*      */   private CachedRowSetWriter crw;
/*      */   private int rowStatus;
/*      */   private int sz;
/*      */   private transient Connection con;
/*      */   private CachedRowSet row;
/*      */   private JdbcRowSetResourceBundle resBundle;
/*      */   static final long serialVersionUID = -3345004441725080251L;
/*      */ 
/*      */   public SyncResolverImpl()
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/*  108 */       this.crsSync = new CachedRowSetImpl();
/*  109 */       this.crsRes = new CachedRowSetImpl();
/*  110 */       this.crw = new CachedRowSetWriter();
/*  111 */       this.row = new CachedRowSetImpl();
/*  112 */       this.rowStatus = 1;
/*      */       try {
/*  114 */         this.resBundle = JdbcRowSetResourceBundle.getJdbcRowSetResourceBundle();
/*      */       } catch (IOException localIOException) {
/*  116 */         throw new RuntimeException(localIOException);
/*      */       }
/*      */     }
/*      */     catch (SQLException localSQLException)
/*      */     {
/*      */     }
/*      */   }
/*      */ 
/*      */   public int getStatus()
/*      */   {
/*  135 */     return ((Integer)this.stats.get(this.rowStatus - 1)).intValue();
/*      */   }
/*      */ 
/*      */   public Object getConflictValue(int paramInt)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/*  148 */       return this.crsRes.getObject(paramInt);
/*      */     } catch (SQLException localSQLException) {
/*  150 */       throw new SQLException(localSQLException.getMessage());
/*      */     }
/*      */   }
/*      */ 
/*      */   public Object getConflictValue(String paramString)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/*  164 */       return this.crsRes.getObject(paramString);
/*      */     } catch (SQLException localSQLException) {
/*  166 */       throw new SQLException(localSQLException.getMessage());
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setResolvedValue(int paramInt, Object paramObject)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/*  193 */       if ((paramInt <= 0) || (paramInt > this.crsSync.getMetaData().getColumnCount())) {
/*  194 */         throw new SQLException(this.resBundle.handleGetObject("syncrsimpl.indexval").toString() + paramInt);
/*      */       }
/*      */ 
/*  197 */       if (this.crsRes.getObject(paramInt) == null)
/*  198 */         throw new SQLException(this.resBundle.handleGetObject("syncrsimpl.noconflict").toString());
/*      */     }
/*      */     catch (SQLException localSQLException1)
/*      */     {
/*  202 */       throw new SQLException(localSQLException1.getMessage());
/*      */     }
/*      */     try {
/*  205 */       int i = 1;
/*      */ 
/*  212 */       if ((this.crsSync.getObject(paramInt).toString().equals(paramObject.toString())) || (this.crsRes.getObject(paramInt).toString().equals(paramObject.toString())))
/*      */       {
/*  225 */         this.crsRes.updateNull(paramInt);
/*  226 */         this.crsRes.updateRow();
/*      */ 
/*  233 */         if (this.row.size() != 1) {
/*  234 */           this.row = buildCachedRow();
/*      */         }
/*      */ 
/*  237 */         this.row.updateObject(paramInt, paramObject);
/*  238 */         this.row.updateRow();
/*      */ 
/*  240 */         for (int j = 1; j < this.crsRes.getMetaData().getColumnCount(); j++) {
/*  241 */           if (this.crsRes.getObject(j) != null) {
/*  242 */             i = 0;
/*  243 */             break;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*  250 */         if (i != 0)
/*      */         {
/*      */           try
/*      */           {
/*  264 */             writeData(this.row);
/*      */           }
/*      */           catch (SyncProviderException localSyncProviderException)
/*      */           {
/*  276 */             throw new SQLException(this.resBundle.handleGetObject("syncrsimpl.syncnotpos").toString());
/*      */           }
/*      */         }
/*      */       }
/*      */       else {
/*  281 */         throw new SQLException(this.resBundle.handleGetObject("syncrsimpl.valtores").toString());
/*      */       }
/*      */     }
/*      */     catch (SQLException localSQLException2)
/*      */     {
/*  286 */       throw new SQLException(localSQLException2.getMessage());
/*      */     }
/*      */   }
/*      */ 
/*      */   private void writeData(CachedRowSet paramCachedRowSet)
/*      */     throws SQLException
/*      */   {
/*  301 */     this.crw.updateResolvedConflictToDB(paramCachedRowSet, this.crw.getReader().connect(this.crsSync));
/*      */   }
/*      */ 
/*      */   private CachedRowSet buildCachedRow()
/*      */     throws SQLException
/*      */   {
/*  313 */     CachedRowSetImpl localCachedRowSetImpl = new CachedRowSetImpl();
/*      */ 
/*  315 */     RowSetMetaDataImpl localRowSetMetaDataImpl1 = new RowSetMetaDataImpl();
/*  316 */     RowSetMetaDataImpl localRowSetMetaDataImpl2 = (RowSetMetaDataImpl)this.crsSync.getMetaData();
/*  317 */     RowSetMetaDataImpl localRowSetMetaDataImpl3 = new RowSetMetaDataImpl();
/*      */ 
/*  319 */     int i = localRowSetMetaDataImpl2.getColumnCount();
/*  320 */     localRowSetMetaDataImpl3.setColumnCount(i);
/*      */ 
/*  322 */     for (int j = 1; j <= i; j++) {
/*  323 */       localRowSetMetaDataImpl3.setColumnType(j, localRowSetMetaDataImpl2.getColumnType(j));
/*  324 */       localRowSetMetaDataImpl3.setColumnName(j, localRowSetMetaDataImpl2.getColumnName(j));
/*  325 */       localRowSetMetaDataImpl3.setNullable(j, 2);
/*      */       try
/*      */       {
/*  328 */         localRowSetMetaDataImpl3.setCatalogName(j, localRowSetMetaDataImpl2.getCatalogName(j));
/*  329 */         localRowSetMetaDataImpl3.setSchemaName(j, localRowSetMetaDataImpl2.getSchemaName(j));
/*      */       } catch (SQLException localSQLException6) {
/*  331 */         localSQLException6.printStackTrace();
/*      */       }
/*      */     }
/*      */ 
/*  335 */     localCachedRowSetImpl.setMetaData(localRowSetMetaDataImpl3);
/*      */ 
/*  337 */     localCachedRowSetImpl.moveToInsertRow();
/*      */ 
/*  339 */     for (j = 1; j <= this.crsSync.getMetaData().getColumnCount(); j++) {
/*  340 */       localCachedRowSetImpl.updateObject(j, this.crsSync.getObject(j));
/*      */     }
/*      */ 
/*  343 */     localCachedRowSetImpl.insertRow();
/*  344 */     localCachedRowSetImpl.moveToCurrentRow();
/*      */ 
/*  346 */     localCachedRowSetImpl.absolute(1);
/*  347 */     localCachedRowSetImpl.setOriginalRow();
/*      */     try
/*      */     {
/*  350 */       localCachedRowSetImpl.setUrl(this.crsSync.getUrl());
/*      */     }
/*      */     catch (SQLException localSQLException1)
/*      */     {
/*      */     }
/*      */     try {
/*  356 */       localCachedRowSetImpl.setDataSourceName(this.crsSync.getCommand());
/*      */     }
/*      */     catch (SQLException localSQLException2)
/*      */     {
/*      */     }
/*      */     try {
/*  362 */       if (this.crsSync.getTableName() != null)
/*  363 */         localCachedRowSetImpl.setTableName(this.crsSync.getTableName());
/*      */     }
/*      */     catch (SQLException localSQLException3)
/*      */     {
/*      */     }
/*      */     try
/*      */     {
/*  370 */       if (this.crsSync.getCommand() != null)
/*  371 */         localCachedRowSetImpl.setCommand(this.crsSync.getCommand());
/*      */     }
/*      */     catch (SQLException localSQLException4)
/*      */     {
/*      */     }
/*      */     try {
/*  377 */       localCachedRowSetImpl.setKeyColumns(this.crsSync.getKeyColumns());
/*      */     }
/*      */     catch (SQLException localSQLException5) {
/*      */     }
/*  381 */     return localCachedRowSetImpl;
/*      */   }
/*      */ 
/*      */   public void setResolvedValue(String paramString, Object paramObject)
/*      */     throws SQLException
/*      */   {
/*      */   }
/*      */ 
/*      */   void setCachedRowSet(CachedRowSet paramCachedRowSet)
/*      */   {
/*  407 */     this.crsSync = ((CachedRowSetImpl)paramCachedRowSet);
/*      */   }
/*      */ 
/*      */   void setCachedRowSetResolver(CachedRowSet paramCachedRowSet)
/*      */   {
/*      */     try
/*      */     {
/*  418 */       this.crsRes = ((CachedRowSetImpl)paramCachedRowSet);
/*  419 */       this.crsRes.afterLast();
/*  420 */       this.sz = this.crsRes.size();
/*      */     }
/*      */     catch (SQLException localSQLException)
/*      */     {
/*      */     }
/*      */   }
/*      */ 
/*      */   void setStatus(ArrayList paramArrayList)
/*      */   {
/*  433 */     this.stats = paramArrayList;
/*      */   }
/*      */ 
/*      */   void setCachedRowSetWriter(CachedRowSetWriter paramCachedRowSetWriter)
/*      */   {
/*  443 */     this.crw = paramCachedRowSetWriter;
/*      */   }
/*      */ 
/*      */   public boolean nextConflict()
/*      */     throws SQLException
/*      */   {
/*  468 */     boolean bool = false;
/*      */ 
/*  470 */     this.crsSync.setShowDeleted(true);
/*  471 */     while (this.crsSync.next()) {
/*  472 */       this.crsRes.previous();
/*  473 */       this.rowStatus += 1;
/*      */ 
/*  475 */       if (this.rowStatus - 1 >= this.stats.size()) {
/*  476 */         bool = false;
/*      */       }
/*  480 */       else if (((Integer)this.stats.get(this.rowStatus - 1)).intValue() != 3)
/*      */       {
/*  485 */         bool = true;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  491 */     this.crsSync.setShowDeleted(false);
/*  492 */     return bool;
/*      */   }
/*      */ 
/*      */   public boolean previousConflict()
/*      */     throws SQLException
/*      */   {
/*  505 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void setCommand(String paramString)
/*      */     throws SQLException
/*      */   {
/*  527 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void populate(ResultSet paramResultSet)
/*      */     throws SQLException
/*      */   {
/*  560 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void execute(Connection paramConnection)
/*      */     throws SQLException
/*      */   {
/*  582 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void acceptChanges()
/*      */     throws SyncProviderException
/*      */   {
/*  620 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void acceptChanges(Connection paramConnection)
/*      */     throws SyncProviderException
/*      */   {
/*  646 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void restoreOriginal()
/*      */     throws SQLException
/*      */   {
/*  661 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void release()
/*      */     throws SQLException
/*      */   {
/*  674 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void undoDelete()
/*      */     throws SQLException
/*      */   {
/*  687 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void undoInsert()
/*      */     throws SQLException
/*      */   {
/*  704 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void undoUpdate()
/*      */     throws SQLException
/*      */   {
/*  726 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public RowSet createShared()
/*      */     throws SQLException
/*      */   {
/*  748 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   protected Object clone()
/*      */     throws CloneNotSupportedException
/*      */   {
/*  768 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public CachedRowSet createCopy()
/*      */     throws SQLException
/*      */   {
/*  793 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public CachedRowSet createCopySchema()
/*      */     throws SQLException
/*      */   {
/*  819 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public CachedRowSet createCopyNoConstraints()
/*      */     throws SQLException
/*      */   {
/*  841 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public Collection toCollection()
/*      */     throws SQLException
/*      */   {
/*  860 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public Collection toCollection(int paramInt)
/*      */     throws SQLException
/*      */   {
/*  882 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public Collection toCollection(String paramString)
/*      */     throws SQLException
/*      */   {
/*  904 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public SyncProvider getSyncProvider()
/*      */     throws SQLException
/*      */   {
/*  923 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void setSyncProvider(String paramString)
/*      */     throws SQLException
/*      */   {
/*  934 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void execute()
/*      */     throws SQLException
/*      */   {
/*  975 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public boolean next()
/*      */     throws SQLException
/*      */   {
/* 1005 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   protected boolean internalNext()
/*      */     throws SQLException
/*      */   {
/* 1032 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void close()
/*      */     throws SQLException
/*      */   {
/* 1043 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public boolean wasNull()
/*      */     throws SQLException
/*      */   {
/* 1058 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   protected BaseRow getCurrentRow()
/*      */   {
/* 1069 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   protected void removeCurrentRow()
/*      */   {
/* 1082 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public String getString(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 1105 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public boolean getBoolean(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 1125 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public byte getByte(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 1148 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public short getShort(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 1171 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public int getInt(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 1193 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public long getLong(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 1216 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public float getFloat(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 1239 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public double getDouble(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 1263 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   /** @deprecated */
/*      */   public BigDecimal getBigDecimal(int paramInt1, int paramInt2)
/*      */     throws SQLException
/*      */   {
/* 1288 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public byte[] getBytes(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 1312 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public Date getDate(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 1330 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public Time getTime(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 1347 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public Timestamp getTimestamp(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 1364 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public InputStream getAsciiStream(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 1399 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   /** @deprecated */
/*      */   public InputStream getUnicodeStream(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 1424 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public InputStream getBinaryStream(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 1458 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public String getString(String paramString)
/*      */     throws SQLException
/*      */   {
/* 1484 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public boolean getBoolean(String paramString)
/*      */     throws SQLException
/*      */   {
/* 1503 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public byte getByte(String paramString)
/*      */     throws SQLException
/*      */   {
/* 1523 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public short getShort(String paramString)
/*      */     throws SQLException
/*      */   {
/* 1544 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public int getInt(String paramString)
/*      */     throws SQLException
/*      */   {
/* 1565 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public long getLong(String paramString)
/*      */     throws SQLException
/*      */   {
/* 1586 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public float getFloat(String paramString)
/*      */     throws SQLException
/*      */   {
/* 1607 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public double getDouble(String paramString)
/*      */     throws SQLException
/*      */   {
/* 1629 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   /** @deprecated */
/*      */   public BigDecimal getBigDecimal(String paramString, int paramInt)
/*      */     throws SQLException
/*      */   {
/* 1653 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public byte[] getBytes(String paramString)
/*      */     throws SQLException
/*      */   {
/* 1674 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public Date getDate(String paramString)
/*      */     throws SQLException
/*      */   {
/* 1692 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public Time getTime(String paramString)
/*      */     throws SQLException
/*      */   {
/* 1708 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public Timestamp getTimestamp(String paramString)
/*      */     throws SQLException
/*      */   {
/* 1724 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public InputStream getAsciiStream(String paramString)
/*      */     throws SQLException
/*      */   {
/* 1758 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   /** @deprecated */
/*      */   public InputStream getUnicodeStream(String paramString)
/*      */     throws SQLException
/*      */   {
/* 1784 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public InputStream getBinaryStream(String paramString)
/*      */     throws SQLException
/*      */   {
/* 1817 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public SQLWarning getWarnings()
/*      */   {
/* 1841 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void clearWarnings()
/*      */   {
/* 1851 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public String getCursorName()
/*      */     throws SQLException
/*      */   {
/* 1879 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public ResultSetMetaData getMetaData()
/*      */     throws SQLException
/*      */   {
/* 1909 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public Object getObject(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 1946 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public Object getObject(String paramString)
/*      */     throws SQLException
/*      */   {
/* 1982 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public int findColumn(String paramString)
/*      */     throws SQLException
/*      */   {
/* 1998 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public Reader getCharacterStream(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 2031 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public Reader getCharacterStream(String paramString)
/*      */     throws SQLException
/*      */   {
/* 2055 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public BigDecimal getBigDecimal(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 2078 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public BigDecimal getBigDecimal(String paramString)
/*      */     throws SQLException
/*      */   {
/* 2101 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public int size()
/*      */   {
/* 2114 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public boolean isBeforeFirst()
/*      */     throws SQLException
/*      */   {
/* 2126 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public boolean isAfterLast()
/*      */     throws SQLException
/*      */   {
/* 2138 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public boolean isFirst()
/*      */     throws SQLException
/*      */   {
/* 2150 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public boolean isLast()
/*      */     throws SQLException
/*      */   {
/* 2166 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void beforeFirst()
/*      */     throws SQLException
/*      */   {
/* 2178 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void afterLast()
/*      */     throws SQLException
/*      */   {
/* 2189 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public boolean first()
/*      */     throws SQLException
/*      */   {
/* 2204 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   protected boolean internalFirst()
/*      */     throws SQLException
/*      */   {
/* 2224 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public boolean last()
/*      */     throws SQLException
/*      */   {
/* 2239 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   protected boolean internalLast()
/*      */     throws SQLException
/*      */   {
/* 2260 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public int getRow()
/*      */     throws SQLException
/*      */   {
/* 2273 */     return this.crsSync.getRow();
/*      */   }
/*      */ 
/*      */   public boolean absolute(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 2324 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public boolean relative(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 2383 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public boolean previous()
/*      */     throws SQLException
/*      */   {
/* 2429 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   protected boolean internalPrevious()
/*      */     throws SQLException
/*      */   {
/* 2449 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public boolean rowUpdated()
/*      */     throws SQLException
/*      */   {
/* 2472 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public boolean columnUpdated(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 2490 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public boolean columnUpdated(String paramString)
/*      */     throws SQLException
/*      */   {
/* 2509 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public boolean rowInserted()
/*      */     throws SQLException
/*      */   {
/* 2524 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public boolean rowDeleted()
/*      */     throws SQLException
/*      */   {
/* 2541 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void updateNull(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 2571 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void updateBoolean(int paramInt, boolean paramBoolean)
/*      */     throws SQLException
/*      */   {
/* 2598 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void updateByte(int paramInt, byte paramByte)
/*      */     throws SQLException
/*      */   {
/* 2625 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void updateShort(int paramInt, short paramShort)
/*      */     throws SQLException
/*      */   {
/* 2652 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void updateInt(int paramInt1, int paramInt2)
/*      */     throws SQLException
/*      */   {
/* 2679 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void updateLong(int paramInt, long paramLong)
/*      */     throws SQLException
/*      */   {
/* 2706 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void updateFloat(int paramInt, float paramFloat)
/*      */     throws SQLException
/*      */   {
/* 2734 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void updateDouble(int paramInt, double paramDouble)
/*      */     throws SQLException
/*      */   {
/* 2761 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void updateBigDecimal(int paramInt, BigDecimal paramBigDecimal)
/*      */     throws SQLException
/*      */   {
/* 2788 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void updateString(int paramInt, String paramString)
/*      */     throws SQLException
/*      */   {
/* 2818 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void updateBytes(int paramInt, byte[] paramArrayOfByte)
/*      */     throws SQLException
/*      */   {
/* 2845 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void updateDate(int paramInt, Date paramDate)
/*      */     throws SQLException
/*      */   {
/* 2873 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void updateTime(int paramInt, Time paramTime)
/*      */     throws SQLException
/*      */   {
/* 2901 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void updateTimestamp(int paramInt, Timestamp paramTimestamp)
/*      */     throws SQLException
/*      */   {
/* 2930 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void updateAsciiStream(int paramInt1, InputStream paramInputStream, int paramInt2)
/*      */     throws SQLException
/*      */   {
/* 2955 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void updateBinaryStream(int paramInt1, InputStream paramInputStream, int paramInt2)
/*      */     throws SQLException
/*      */   {
/* 2985 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void updateCharacterStream(int paramInt1, Reader paramReader, int paramInt2)
/*      */     throws SQLException
/*      */   {
/* 3017 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void updateObject(int paramInt1, Object paramObject, int paramInt2)
/*      */     throws SQLException
/*      */   {
/* 3049 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void updateObject(int paramInt, Object paramObject)
/*      */     throws SQLException
/*      */   {
/* 3076 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void updateNull(String paramString)
/*      */     throws SQLException
/*      */   {
/* 3101 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void updateBoolean(String paramString, boolean paramBoolean)
/*      */     throws SQLException
/*      */   {
/* 3127 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void updateByte(String paramString, byte paramByte)
/*      */     throws SQLException
/*      */   {
/* 3153 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void updateShort(String paramString, short paramShort)
/*      */     throws SQLException
/*      */   {
/* 3179 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void updateInt(String paramString, int paramInt)
/*      */     throws SQLException
/*      */   {
/* 3205 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void updateLong(String paramString, long paramLong)
/*      */     throws SQLException
/*      */   {
/* 3231 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void updateFloat(String paramString, float paramFloat)
/*      */     throws SQLException
/*      */   {
/* 3257 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void updateDouble(String paramString, double paramDouble)
/*      */     throws SQLException
/*      */   {
/* 3283 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void updateBigDecimal(String paramString, BigDecimal paramBigDecimal)
/*      */     throws SQLException
/*      */   {
/* 3309 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void updateString(String paramString1, String paramString2)
/*      */     throws SQLException
/*      */   {
/* 3335 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void updateBytes(String paramString, byte[] paramArrayOfByte)
/*      */     throws SQLException
/*      */   {
/* 3361 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void updateDate(String paramString, Date paramDate)
/*      */     throws SQLException
/*      */   {
/* 3389 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void updateTime(String paramString, Time paramTime)
/*      */     throws SQLException
/*      */   {
/* 3417 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void updateTimestamp(String paramString, Timestamp paramTimestamp)
/*      */     throws SQLException
/*      */   {
/* 3448 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void updateAsciiStream(String paramString, InputStream paramInputStream, int paramInt)
/*      */     throws SQLException
/*      */   {
/* 3473 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void updateBinaryStream(String paramString, InputStream paramInputStream, int paramInt)
/*      */     throws SQLException
/*      */   {
/* 3503 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void updateCharacterStream(String paramString, Reader paramReader, int paramInt)
/*      */     throws SQLException
/*      */   {
/* 3536 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void updateObject(String paramString, Object paramObject, int paramInt)
/*      */     throws SQLException
/*      */   {
/* 3567 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void updateObject(String paramString, Object paramObject)
/*      */     throws SQLException
/*      */   {
/* 3593 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void insertRow()
/*      */     throws SQLException
/*      */   {
/* 3612 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void updateRow()
/*      */     throws SQLException
/*      */   {
/* 3629 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void deleteRow()
/*      */     throws SQLException
/*      */   {
/* 3648 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void refreshRow()
/*      */     throws SQLException
/*      */   {
/* 3662 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void cancelRowUpdates()
/*      */     throws SQLException
/*      */   {
/* 3678 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void moveToInsertRow()
/*      */     throws SQLException
/*      */   {
/* 3708 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void moveToCurrentRow()
/*      */     throws SQLException
/*      */   {
/* 3722 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public Statement getStatement()
/*      */     throws SQLException
/*      */   {
/* 3732 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public Object getObject(int paramInt, Map<String, Class<?>> paramMap)
/*      */     throws SQLException
/*      */   {
/* 3757 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public Ref getRef(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 3776 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public Blob getBlob(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 3795 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public Clob getClob(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 3814 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public Array getArray(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 3834 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public Object getObject(String paramString, Map<String, Class<?>> paramMap)
/*      */     throws SQLException
/*      */   {
/* 3857 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public Ref getRef(String paramString)
/*      */     throws SQLException
/*      */   {
/* 3875 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public Blob getBlob(String paramString)
/*      */     throws SQLException
/*      */   {
/* 3893 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public Clob getClob(String paramString)
/*      */     throws SQLException
/*      */   {
/* 3912 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public Array getArray(String paramString)
/*      */     throws SQLException
/*      */   {
/* 3931 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public Date getDate(int paramInt, Calendar paramCalendar)
/*      */     throws SQLException
/*      */   {
/* 3954 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public Date getDate(String paramString, Calendar paramCalendar)
/*      */     throws SQLException
/*      */   {
/* 3976 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public Time getTime(int paramInt, Calendar paramCalendar)
/*      */     throws SQLException
/*      */   {
/* 3999 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public Time getTime(String paramString, Calendar paramCalendar)
/*      */     throws SQLException
/*      */   {
/* 4021 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public Timestamp getTimestamp(int paramInt, Calendar paramCalendar)
/*      */     throws SQLException
/*      */   {
/* 4044 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public Timestamp getTimestamp(String paramString, Calendar paramCalendar)
/*      */     throws SQLException
/*      */   {
/* 4067 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public Connection getConnection()
/*      */     throws SQLException
/*      */   {
/* 4085 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void setMetaData(RowSetMetaData paramRowSetMetaData)
/*      */     throws SQLException
/*      */   {
/* 4098 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public ResultSet getOriginal()
/*      */     throws SQLException
/*      */   {
/* 4116 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public ResultSet getOriginalRow()
/*      */     throws SQLException
/*      */   {
/* 4131 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void setOriginalRow()
/*      */     throws SQLException
/*      */   {
/* 4142 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void setOriginal()
/*      */     throws SQLException
/*      */   {
/* 4153 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public String getTableName()
/*      */     throws SQLException
/*      */   {
/* 4165 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void setTableName(String paramString)
/*      */     throws SQLException
/*      */   {
/* 4178 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public int[] getKeyColumns()
/*      */     throws SQLException
/*      */   {
/* 4193 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void setKeyColumns(int[] paramArrayOfInt)
/*      */     throws SQLException
/*      */   {
/* 4214 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void updateRef(int paramInt, Ref paramRef)
/*      */     throws SQLException
/*      */   {
/* 4241 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void updateRef(String paramString, Ref paramRef)
/*      */     throws SQLException
/*      */   {
/* 4267 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void updateClob(int paramInt, Clob paramClob)
/*      */     throws SQLException
/*      */   {
/* 4294 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void updateClob(String paramString, Clob paramClob)
/*      */     throws SQLException
/*      */   {
/* 4320 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void updateBlob(int paramInt, Blob paramBlob)
/*      */     throws SQLException
/*      */   {
/* 4347 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void updateBlob(String paramString, Blob paramBlob)
/*      */     throws SQLException
/*      */   {
/* 4373 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void updateArray(int paramInt, Array paramArray)
/*      */     throws SQLException
/*      */   {
/* 4400 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void updateArray(String paramString, Array paramArray)
/*      */     throws SQLException
/*      */   {
/* 4426 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public URL getURL(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 4444 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public URL getURL(String paramString)
/*      */     throws SQLException
/*      */   {
/* 4462 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public RowSetWarning getRowSetWarnings()
/*      */   {
/* 4486 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void commit()
/*      */     throws SQLException
/*      */   {
/* 4496 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void rollback()
/*      */     throws SQLException
/*      */   {
/* 4506 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void rollback(Savepoint paramSavepoint)
/*      */     throws SQLException
/*      */   {
/* 4516 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void unsetMatchColumn(int[] paramArrayOfInt)
/*      */     throws SQLException
/*      */   {
/* 4534 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void unsetMatchColumn(String[] paramArrayOfString)
/*      */     throws SQLException
/*      */   {
/* 4552 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public String[] getMatchColumnNames()
/*      */     throws SQLException
/*      */   {
/* 4566 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public int[] getMatchColumnIndexes()
/*      */     throws SQLException
/*      */   {
/* 4579 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void setMatchColumn(int[] paramArrayOfInt)
/*      */     throws SQLException
/*      */   {
/* 4600 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void setMatchColumn(String[] paramArrayOfString)
/*      */     throws SQLException
/*      */   {
/* 4619 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void setMatchColumn(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 4641 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void setMatchColumn(String paramString)
/*      */     throws SQLException
/*      */   {
/* 4660 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void unsetMatchColumn(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 4678 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void unsetMatchColumn(String paramString)
/*      */     throws SQLException
/*      */   {
/* 4696 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void rowSetPopulated(RowSetEvent paramRowSetEvent, int paramInt)
/*      */     throws SQLException
/*      */   {
/* 4713 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void populate(ResultSet paramResultSet, int paramInt)
/*      */     throws SQLException
/*      */   {
/* 4749 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public boolean nextPage()
/*      */     throws SQLException
/*      */   {
/* 4761 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void setPageSize(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 4772 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public int getPageSize()
/*      */   {
/* 4781 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public boolean previousPage()
/*      */     throws SQLException
/*      */   {
/* 4795 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void updateNCharacterStream(int paramInt1, Reader paramReader, int paramInt2)
/*      */     throws SQLException
/*      */   {
/* 4817 */     throw new UnsupportedOperationException("Operation not yet supported");
/*      */   }
/*      */ 
/*      */   public void updateNCharacterStream(String paramString, Reader paramReader, int paramInt)
/*      */     throws SQLException
/*      */   {
/* 4839 */     throw new UnsupportedOperationException("Operation not yet supported");
/*      */   }
/*      */ 
/*      */   private void readObject(ObjectInputStream paramObjectInputStream)
/*      */     throws IOException, ClassNotFoundException
/*      */   {
/* 4849 */     paramObjectInputStream.defaultReadObject();
/*      */     try
/*      */     {
/* 4852 */       this.resBundle = JdbcRowSetResourceBundle.getJdbcRowSetResourceBundle();
/*      */     } catch (IOException localIOException) {
/* 4854 */       throw new RuntimeException(localIOException);
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.rowset.internal.SyncResolverImpl
 * JD-Core Version:    0.6.2
 */