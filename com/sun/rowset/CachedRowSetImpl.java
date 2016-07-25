/*       */ package com.sun.rowset;
/*       */ 
/*       */ import com.sun.rowset.internal.BaseRow;
/*       */ import com.sun.rowset.internal.CachedRowSetReader;
/*       */ import com.sun.rowset.internal.CachedRowSetWriter;
/*       */ import com.sun.rowset.internal.InsertRow;
/*       */ import com.sun.rowset.internal.Row;
/*       */ import com.sun.rowset.providers.RIOptimisticProvider;
/*       */ import java.io.ByteArrayInputStream;
/*       */ import java.io.ByteArrayOutputStream;
/*       */ import java.io.IOException;
/*       */ import java.io.InputStream;
/*       */ import java.io.InputStreamReader;
/*       */ import java.io.ObjectInputStream;
/*       */ import java.io.ObjectOutputStream;
/*       */ import java.io.OptionalDataException;
/*       */ import java.io.PrintStream;
/*       */ import java.io.Reader;
/*       */ import java.io.Serializable;
/*       */ import java.io.StreamCorruptedException;
/*       */ import java.io.StringBufferInputStream;
/*       */ import java.io.StringReader;
/*       */ import java.io.UnsupportedEncodingException;
/*       */ import java.math.BigDecimal;
/*       */ import java.net.URL;
/*       */ import java.sql.Array;
/*       */ import java.sql.Blob;
/*       */ import java.sql.Clob;
/*       */ import java.sql.Connection;
/*       */ import java.sql.DatabaseMetaData;
/*       */ import java.sql.NClob;
/*       */ import java.sql.Ref;
/*       */ import java.sql.ResultSet;
/*       */ import java.sql.ResultSetMetaData;
/*       */ import java.sql.RowId;
/*       */ import java.sql.SQLData;
/*       */ import java.sql.SQLException;
/*       */ import java.sql.SQLFeatureNotSupportedException;
/*       */ import java.sql.SQLWarning;
/*       */ import java.sql.SQLXML;
/*       */ import java.sql.Savepoint;
/*       */ import java.sql.Statement;
/*       */ import java.sql.Struct;
/*       */ import java.sql.Time;
/*       */ import java.sql.Timestamp;
/*       */ import java.text.DateFormat;
/*       */ import java.text.MessageFormat;
/*       */ import java.text.ParseException;
/*       */ import java.util.Calendar;
/*       */ import java.util.Collection;
/*       */ import java.util.Hashtable;
/*       */ import java.util.Iterator;
/*       */ import java.util.Map;
/*       */ import java.util.TreeMap;
/*       */ import java.util.Vector;
/*       */ import javax.sql.RowSet;
/*       */ import javax.sql.RowSetEvent;
/*       */ import javax.sql.RowSetInternal;
/*       */ import javax.sql.RowSetMetaData;
/*       */ import javax.sql.RowSetReader;
/*       */ import javax.sql.RowSetWriter;
/*       */ import javax.sql.rowset.BaseRowSet;
/*       */ import javax.sql.rowset.CachedRowSet;
/*       */ import javax.sql.rowset.RowSetMetaDataImpl;
/*       */ import javax.sql.rowset.RowSetWarning;
/*       */ import javax.sql.rowset.serial.SQLInputImpl;
/*       */ import javax.sql.rowset.serial.SerialArray;
/*       */ import javax.sql.rowset.serial.SerialBlob;
/*       */ import javax.sql.rowset.serial.SerialClob;
/*       */ import javax.sql.rowset.serial.SerialRef;
/*       */ import javax.sql.rowset.serial.SerialStruct;
/*       */ import javax.sql.rowset.spi.SyncFactory;
/*       */ import javax.sql.rowset.spi.SyncProvider;
/*       */ import javax.sql.rowset.spi.SyncProviderException;
/*       */ import javax.sql.rowset.spi.TransactionalWriter;
/*       */ import sun.reflect.misc.ReflectUtil;
/*       */ 
/*       */ public class CachedRowSetImpl extends BaseRowSet
/*       */   implements RowSet, RowSetInternal, Serializable, Cloneable, CachedRowSet
/*       */ {
/*       */   private SyncProvider provider;
/*       */   private RowSetReader rowSetReader;
/*       */   private RowSetWriter rowSetWriter;
/*       */   private transient Connection conn;
/*       */   private transient ResultSetMetaData RSMD;
/*       */   private RowSetMetaDataImpl RowSetMD;
/*       */   private int[] keyCols;
/*       */   private String tableName;
/*       */   private Vector<Object> rvh;
/*       */   private int cursorPos;
/*       */   private int absolutePos;
/*       */   private int numDeleted;
/*       */   private int numRows;
/*       */   private InsertRow insertRow;
/*       */   private boolean onInsertRow;
/*       */   private int currentRow;
/*       */   private boolean lastValueNull;
/*       */   private SQLWarning sqlwarn;
/*   193 */   private String strMatchColumn = "";
/*       */ 
/*   198 */   private int iMatchColumn = -1;
/*       */   private RowSetWarning rowsetWarning;
/*   208 */   private String DEFAULT_SYNC_PROVIDER = "com.sun.rowset.providers.RIOptimisticProvider";
/*       */   private boolean dbmslocatorsUpdateCopy;
/*       */   private transient ResultSet resultSet;
/*       */   private int endPos;
/*       */   private int prevEndPos;
/*       */   private int startPos;
/*       */   private int startPrev;
/*       */   private int pageSize;
/*       */   private int maxRowsreached;
/*   259 */   private boolean pagenotend = true;
/*       */   private boolean onFirstPage;
/*       */   private boolean onLastPage;
/*       */   private int populatecallcount;
/*       */   private int totalRows;
/*       */   private boolean callWithCon;
/*       */   private CachedRowSetReader crsReader;
/*       */   private Vector<Integer> iMatchColumns;
/*       */   private Vector<String> strMatchColumns;
/*   308 */   private boolean tXWriter = false;
/*       */ 
/*   313 */   private TransactionalWriter tWriter = null;
/*       */   protected transient JdbcRowSetResourceBundle resBundle;
/*       */   private boolean updateOnInsert;
/*       */   static final long serialVersionUID = 1884577171200622428L;
/*       */ 
/*       */   public CachedRowSetImpl()
/*       */     throws SQLException
/*       */   {
/*       */     try
/*       */     {
/*   354 */       this.resBundle = JdbcRowSetResourceBundle.getJdbcRowSetResourceBundle();
/*       */     } catch (IOException localIOException) {
/*   356 */       throw new RuntimeException(localIOException);
/*       */     }
/*       */ 
/*   360 */     this.provider = SyncFactory.getInstance(this.DEFAULT_SYNC_PROVIDER);
/*       */ 
/*   363 */     if (!(this.provider instanceof RIOptimisticProvider)) {
/*   364 */       throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.invalidp").toString());
/*       */     }
/*       */ 
/*   367 */     this.rowSetReader = ((CachedRowSetReader)this.provider.getRowSetReader());
/*   368 */     this.rowSetWriter = ((CachedRowSetWriter)this.provider.getRowSetWriter());
/*       */ 
/*   371 */     initParams();
/*       */ 
/*   373 */     initContainer();
/*       */ 
/*   376 */     initProperties();
/*       */ 
/*   379 */     this.onInsertRow = false;
/*   380 */     this.insertRow = null;
/*       */ 
/*   383 */     this.sqlwarn = new SQLWarning();
/*   384 */     this.rowsetWarning = new RowSetWarning();
/*       */   }
/*       */ 
/*       */   public CachedRowSetImpl(Hashtable paramHashtable)
/*       */     throws SQLException
/*       */   {
/*       */     try
/*       */     {
/*   457 */       this.resBundle = JdbcRowSetResourceBundle.getJdbcRowSetResourceBundle();
/*       */     } catch (IOException localIOException) {
/*   459 */       throw new RuntimeException(localIOException);
/*       */     }
/*       */ 
/*   462 */     if (paramHashtable == null) {
/*   463 */       throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.nullhash").toString());
/*       */     }
/*       */ 
/*   466 */     String str = (String)paramHashtable.get("rowset.provider.classname");
/*       */ 
/*   470 */     this.provider = SyncFactory.getInstance(str);
/*       */ 
/*   473 */     this.rowSetReader = this.provider.getRowSetReader();
/*   474 */     this.rowSetWriter = this.provider.getRowSetWriter();
/*       */ 
/*   476 */     initParams();
/*   477 */     initContainer();
/*   478 */     initProperties();
/*       */   }
/*       */ 
/*       */   private void initContainer()
/*       */   {
/*   488 */     this.rvh = new Vector(100);
/*   489 */     this.cursorPos = 0;
/*   490 */     this.absolutePos = 0;
/*   491 */     this.numRows = 0;
/*   492 */     this.numDeleted = 0;
/*       */   }
/*       */ 
/*       */   private void initProperties()
/*       */     throws SQLException
/*       */   {
/*   503 */     if (this.resBundle == null) {
/*       */       try {
/*   505 */         this.resBundle = JdbcRowSetResourceBundle.getJdbcRowSetResourceBundle();
/*       */       } catch (IOException localIOException) {
/*   507 */         throw new RuntimeException(localIOException);
/*       */       }
/*       */     }
/*   510 */     setShowDeleted(false);
/*   511 */     setQueryTimeout(0);
/*   512 */     setMaxRows(0);
/*   513 */     setMaxFieldSize(0);
/*   514 */     setType(1004);
/*   515 */     setConcurrency(1008);
/*   516 */     if ((this.rvh.size() > 0) && (!isReadOnly()))
/*   517 */       setReadOnly(false);
/*       */     else
/*   519 */       setReadOnly(true);
/*   520 */     setTransactionIsolation(2);
/*   521 */     setEscapeProcessing(true);
/*       */ 
/*   523 */     checkTransactionalWriter();
/*       */ 
/*   527 */     this.iMatchColumns = new Vector(10);
/*   528 */     for (int i = 0; i < 10; i++) {
/*   529 */       this.iMatchColumns.add(i, Integer.valueOf(-1));
/*       */     }
/*       */ 
/*   532 */     this.strMatchColumns = new Vector(10);
/*   533 */     for (i = 0; i < 10; i++)
/*   534 */       this.strMatchColumns.add(i, null);
/*       */   }
/*       */ 
/*       */   private void checkTransactionalWriter()
/*       */   {
/*   543 */     if (this.rowSetWriter != null) {
/*   544 */       Class localClass = this.rowSetWriter.getClass();
/*   545 */       if (localClass != null) {
/*   546 */         Class[] arrayOfClass = localClass.getInterfaces();
/*   547 */         for (int i = 0; i < arrayOfClass.length; i++)
/*   548 */           if (arrayOfClass[i].getName().indexOf("TransactionalWriter") > 0) {
/*   549 */             this.tXWriter = true;
/*   550 */             establishTransactionalWriter();
/*       */           }
/*       */       }
/*       */     }
/*       */   }
/*       */ 
/*       */   private void establishTransactionalWriter()
/*       */   {
/*   561 */     this.tWriter = ((TransactionalWriter)this.provider.getRowSetWriter());
/*       */   }
/*       */ 
/*       */   public void setCommand(String paramString)
/*       */     throws SQLException
/*       */   {
/*   584 */     super.setCommand(paramString);
/*       */ 
/*   586 */     if (!buildTableName(paramString).equals(""))
/*   587 */       setTableName(buildTableName(paramString));
/*       */   }
/*       */ 
/*       */   public void populate(ResultSet paramResultSet)
/*       */     throws SQLException
/*       */   {
/*   626 */     Map localMap = getTypeMap();
/*       */ 
/*   630 */     if (paramResultSet == null) {
/*   631 */       throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.populate").toString());
/*       */     }
/*   633 */     this.resultSet = paramResultSet;
/*       */ 
/*   636 */     this.RSMD = paramResultSet.getMetaData();
/*       */ 
/*   639 */     this.RowSetMD = new RowSetMetaDataImpl();
/*   640 */     initMetaData(this.RowSetMD, this.RSMD);
/*       */ 
/*   643 */     this.RSMD = null;
/*   644 */     int j = this.RowSetMD.getColumnCount();
/*   645 */     int m = getMaxRows();
/*   646 */     int i = 0;
/*   647 */     Row localRow = null;
/*       */ 
/*   649 */     while (paramResultSet.next())
/*       */     {
/*   651 */       localRow = new Row(j);
/*       */ 
/*   653 */       if ((i > m) && (m > 0)) {
/*   654 */         this.rowsetWarning.setNextWarning(new RowSetWarning("Populating rows setting has exceeded max row setting"));
/*       */       }
/*       */ 
/*   657 */       for (int k = 1; k <= j; k++)
/*       */       {
/*       */         Object localObject;
/*   664 */         if (localMap == null)
/*   665 */           localObject = paramResultSet.getObject(k);
/*       */         else {
/*   667 */           localObject = paramResultSet.getObject(k, localMap);
/*       */         }
/*       */ 
/*   674 */         if ((localObject instanceof Struct))
/*   675 */           localObject = new SerialStruct((Struct)localObject, localMap);
/*   676 */         else if ((localObject instanceof SQLData))
/*   677 */           localObject = new SerialStruct((SQLData)localObject, localMap);
/*   678 */         else if ((localObject instanceof Blob))
/*   679 */           localObject = new SerialBlob((Blob)localObject);
/*   680 */         else if ((localObject instanceof Clob))
/*   681 */           localObject = new SerialClob((Clob)localObject);
/*   682 */         else if ((localObject instanceof Array)) {
/*   683 */           if (localMap != null)
/*   684 */             localObject = new SerialArray((Array)localObject, localMap);
/*       */           else {
/*   686 */             localObject = new SerialArray((Array)localObject);
/*       */           }
/*       */         }
/*   689 */         localRow.initColumnObject(k, localObject);
/*       */       }
/*   691 */       i++;
/*   692 */       this.rvh.add(localRow);
/*       */     }
/*       */ 
/*   695 */     this.numRows = i;
/*       */ 
/*   699 */     notifyRowSetChanged();
/*       */   }
/*       */ 
/*       */   private void initMetaData(RowSetMetaDataImpl paramRowSetMetaDataImpl, ResultSetMetaData paramResultSetMetaData)
/*       */     throws SQLException
/*       */   {
/*   716 */     int i = paramResultSetMetaData.getColumnCount();
/*       */ 
/*   718 */     paramRowSetMetaDataImpl.setColumnCount(i);
/*   719 */     for (int j = 1; j <= i; j++) {
/*   720 */       paramRowSetMetaDataImpl.setAutoIncrement(j, paramResultSetMetaData.isAutoIncrement(j));
/*   721 */       if (paramResultSetMetaData.isAutoIncrement(j))
/*   722 */         this.updateOnInsert = true;
/*   723 */       paramRowSetMetaDataImpl.setCaseSensitive(j, paramResultSetMetaData.isCaseSensitive(j));
/*   724 */       paramRowSetMetaDataImpl.setCurrency(j, paramResultSetMetaData.isCurrency(j));
/*   725 */       paramRowSetMetaDataImpl.setNullable(j, paramResultSetMetaData.isNullable(j));
/*   726 */       paramRowSetMetaDataImpl.setSigned(j, paramResultSetMetaData.isSigned(j));
/*   727 */       paramRowSetMetaDataImpl.setSearchable(j, paramResultSetMetaData.isSearchable(j));
/*       */ 
/*   732 */       int k = paramResultSetMetaData.getColumnDisplaySize(j);
/*   733 */       if (k < 0) {
/*   734 */         k = 0;
/*       */       }
/*   736 */       paramRowSetMetaDataImpl.setColumnDisplaySize(j, k);
/*   737 */       paramRowSetMetaDataImpl.setColumnLabel(j, paramResultSetMetaData.getColumnLabel(j));
/*   738 */       paramRowSetMetaDataImpl.setColumnName(j, paramResultSetMetaData.getColumnName(j));
/*   739 */       paramRowSetMetaDataImpl.setSchemaName(j, paramResultSetMetaData.getSchemaName(j));
/*       */ 
/*   744 */       int m = paramResultSetMetaData.getPrecision(j);
/*   745 */       if (m < 0) {
/*   746 */         m = 0;
/*       */       }
/*   748 */       paramRowSetMetaDataImpl.setPrecision(j, m);
/*       */ 
/*   755 */       int n = paramResultSetMetaData.getScale(j);
/*   756 */       if (n < 0) {
/*   757 */         n = 0;
/*       */       }
/*   759 */       paramRowSetMetaDataImpl.setScale(j, n);
/*   760 */       paramRowSetMetaDataImpl.setTableName(j, paramResultSetMetaData.getTableName(j));
/*   761 */       paramRowSetMetaDataImpl.setCatalogName(j, paramResultSetMetaData.getCatalogName(j));
/*   762 */       paramRowSetMetaDataImpl.setColumnType(j, paramResultSetMetaData.getColumnType(j));
/*   763 */       paramRowSetMetaDataImpl.setColumnTypeName(j, paramResultSetMetaData.getColumnTypeName(j));
/*       */     }
/*       */ 
/*   766 */     if (this.conn != null)
/*       */     {
/*   769 */       this.dbmslocatorsUpdateCopy = this.conn.getMetaData().locatorsUpdateCopy();
/*       */     }
/*       */   }
/*       */ 
/*       */   public void execute(Connection paramConnection)
/*       */     throws SQLException
/*       */   {
/*   793 */     setConnection(paramConnection);
/*       */ 
/*   795 */     if (getPageSize() != 0) {
/*   796 */       this.crsReader = ((CachedRowSetReader)this.provider.getRowSetReader());
/*   797 */       this.crsReader.setStartPosition(1);
/*   798 */       this.callWithCon = true;
/*   799 */       this.crsReader.readData(this);
/*       */     }
/*       */     else
/*       */     {
/*   804 */       this.rowSetReader.readData(this);
/*       */     }
/*   806 */     this.RowSetMD = ((RowSetMetaDataImpl)getMetaData());
/*       */ 
/*   808 */     if (paramConnection != null)
/*       */     {
/*   811 */       this.dbmslocatorsUpdateCopy = paramConnection.getMetaData().locatorsUpdateCopy();
/*       */     }
/*       */   }
/*       */ 
/*       */   private void setConnection(Connection paramConnection)
/*       */   {
/*   831 */     this.conn = paramConnection;
/*       */   }
/*       */ 
/*       */   public void acceptChanges()
/*       */     throws SyncProviderException
/*       */   {
/*   870 */     if (this.onInsertRow == true) {
/*   871 */       throw new SyncProviderException(this.resBundle.handleGetObject("cachedrowsetimpl.invalidop").toString());
/*       */     }
/*       */ 
/*   874 */     int i = this.cursorPos;
/*   875 */     int j = 0;
/*   876 */     boolean bool = false;
/*       */     try
/*       */     {
/*   879 */       if (this.rowSetWriter != null) {
/*   880 */         i = this.cursorPos;
/*   881 */         bool = this.rowSetWriter.writeData(this);
/*   882 */         this.cursorPos = i;
/*       */       }
/*       */ 
/*   885 */       if (this.tXWriter)
/*       */       {
/*   887 */         if (!bool) {
/*   888 */           this.tWriter = ((TransactionalWriter)this.rowSetWriter);
/*   889 */           this.tWriter.rollback();
/*   890 */           j = 0;
/*       */         } else {
/*   892 */           this.tWriter = ((TransactionalWriter)this.rowSetWriter);
/*   893 */           if ((this.tWriter instanceof CachedRowSetWriter))
/*   894 */             ((CachedRowSetWriter)this.tWriter).commit(this, this.updateOnInsert);
/*       */           else {
/*   896 */             this.tWriter.commit();
/*       */           }
/*       */ 
/*   899 */           j = 1;
/*       */         }
/*       */       }
/*       */ 
/*   903 */       if (j == 1) {
/*   904 */         setOriginal();
/*       */       }
/*   905 */       else if (j != 0);
/*       */     }
/*       */     catch (SyncProviderException localSyncProviderException)
/*       */     {
/*   910 */       throw localSyncProviderException;
/*       */     } catch (SQLException localSQLException) {
/*   912 */       localSQLException.printStackTrace();
/*   913 */       throw new SyncProviderException(localSQLException.getMessage());
/*       */     } catch (SecurityException localSecurityException) {
/*   915 */       throw new SyncProviderException(localSecurityException.getMessage());
/*       */     }
/*       */   }
/*       */ 
/*       */   public void acceptChanges(Connection paramConnection)
/*       */     throws SyncProviderException
/*       */   {
/*   943 */     setConnection(paramConnection);
/*   944 */     acceptChanges();
/*       */   }
/*       */ 
/*       */   public void restoreOriginal()
/*       */     throws SQLException
/*       */   {
/*   961 */     for (Iterator localIterator = this.rvh.iterator(); localIterator.hasNext(); ) {
/*   962 */       Row localRow = (Row)localIterator.next();
/*   963 */       if (localRow.getInserted() == true) {
/*   964 */         localIterator.remove();
/*   965 */         this.numRows -= 1;
/*       */       } else {
/*   967 */         if (localRow.getDeleted() == true) {
/*   968 */           localRow.clearDeleted();
/*       */         }
/*   970 */         if (localRow.getUpdated() == true) {
/*   971 */           localRow.clearUpdated();
/*       */         }
/*       */       }
/*       */     }
/*       */ 
/*   976 */     this.cursorPos = 0;
/*       */ 
/*   979 */     notifyRowSetChanged();
/*       */   }
/*       */ 
/*       */   public void release()
/*       */     throws SQLException
/*       */   {
/*   992 */     initContainer();
/*   993 */     notifyRowSetChanged();
/*       */   }
/*       */ 
/*       */   public void undoDelete()
/*       */     throws SQLException
/*       */   {
/*  1006 */     if (!getShowDeleted()) {
/*  1007 */       return;
/*       */     }
/*       */ 
/*  1010 */     checkCursor();
/*       */ 
/*  1013 */     if (this.onInsertRow == true) {
/*  1014 */       throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.invalidcp").toString());
/*       */     }
/*       */ 
/*  1017 */     Row localRow = (Row)getCurrentRow();
/*  1018 */     if (localRow.getDeleted() == true) {
/*  1019 */       localRow.clearDeleted();
/*  1020 */       this.numDeleted -= 1;
/*  1021 */       notifyRowChanged();
/*       */     }
/*       */   }
/*       */ 
/*       */   public void undoInsert()
/*       */     throws SQLException
/*       */   {
/*  1040 */     checkCursor();
/*       */ 
/*  1043 */     if (this.onInsertRow == true) {
/*  1044 */       throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.invalidcp").toString());
/*       */     }
/*       */ 
/*  1047 */     Row localRow = (Row)getCurrentRow();
/*  1048 */     if (localRow.getInserted() == true) {
/*  1049 */       this.rvh.remove(this.cursorPos - 1);
/*  1050 */       this.numRows -= 1;
/*  1051 */       notifyRowChanged();
/*       */     } else {
/*  1053 */       throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.illegalop").toString());
/*       */     }
/*       */   }
/*       */ 
/*       */   public void undoUpdate()
/*       */     throws SQLException
/*       */   {
/*  1079 */     moveToCurrentRow();
/*       */ 
/*  1083 */     undoDelete();
/*       */ 
/*  1085 */     undoInsert();
/*       */   }
/*       */ 
/*       */   public RowSet createShared()
/*       */     throws SQLException
/*       */   {
/*       */     RowSet localRowSet;
/*       */     try
/*       */     {
/*  1109 */       localRowSet = (CachedRowSet)clone();
/*       */     } catch (CloneNotSupportedException localCloneNotSupportedException) {
/*  1111 */       throw new SQLException(localCloneNotSupportedException.getMessage());
/*       */     }
/*  1113 */     return localRowSet;
/*       */   }
/*       */ 
/*       */   protected Object clone()
/*       */     throws CloneNotSupportedException
/*       */   {
/*  1133 */     return super.clone(); } 
/*       */   public CachedRowSet createCopy() throws SQLException { // Byte code:
/*       */     //   0: new 673	java/io/ByteArrayOutputStream
/*       */     //   3: dup
/*       */     //   4: invokespecial 1502	java/io/ByteArrayOutputStream:<init>	()V
/*       */     //   7: astore_2
/*       */     //   8: new 678	java/io/ObjectOutputStream
/*       */     //   11: dup
/*       */     //   12: aload_2
/*       */     //   13: invokespecial 1510	java/io/ObjectOutputStream:<init>	(Ljava/io/OutputStream;)V
/*       */     //   16: astore_1
/*       */     //   17: aload_1
/*       */     //   18: aload_0
/*       */     //   19: invokevirtual 1511	java/io/ObjectOutputStream:writeObject	(Ljava/lang/Object;)V
/*       */     //   22: goto +38 -> 60
/*       */     //   25: astore_3
/*       */     //   26: new 718	java/sql/SQLException
/*       */     //   29: dup
/*       */     //   30: aload_0
/*       */     //   31: getfield 1324	com/sun/rowset/CachedRowSetImpl:resBundle	Lcom/sun/rowset/JdbcRowSetResourceBundle;
/*       */     //   34: ldc 4
/*       */     //   36: invokevirtual 1474	com/sun/rowset/JdbcRowSetResourceBundle:handleGetObject	(Ljava/lang/String;)Ljava/lang/Object;
/*       */     //   39: invokevirtual 1547	java/lang/Object:toString	()Ljava/lang/String;
/*       */     //   42: iconst_1
/*       */     //   43: anewarray 699	java/lang/Object
/*       */     //   46: dup
/*       */     //   47: iconst_0
/*       */     //   48: aload_3
/*       */     //   49: invokevirtual 1504	java/io/IOException:getMessage	()Ljava/lang/String;
/*       */     //   52: aastore
/*       */     //   53: invokestatic 1587	java/text/MessageFormat:format	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
/*       */     //   56: invokespecial 1576	java/sql/SQLException:<init>	(Ljava/lang/String;)V
/*       */     //   59: athrow
/*       */     //   60: new 672	java/io/ByteArrayInputStream
/*       */     //   63: dup
/*       */     //   64: aload_2
/*       */     //   65: invokevirtual 1503	java/io/ByteArrayOutputStream:toByteArray	()[B
/*       */     //   68: invokespecial 1501	java/io/ByteArrayInputStream:<init>	([B)V
/*       */     //   71: astore 4
/*       */     //   73: new 677	java/io/ObjectInputStream
/*       */     //   76: dup
/*       */     //   77: aload 4
/*       */     //   79: invokespecial 1508	java/io/ObjectInputStream:<init>	(Ljava/io/InputStream;)V
/*       */     //   82: astore_3
/*       */     //   83: goto +77 -> 160
/*       */     //   86: astore 4
/*       */     //   88: new 718	java/sql/SQLException
/*       */     //   91: dup
/*       */     //   92: aload_0
/*       */     //   93: getfield 1324	com/sun/rowset/CachedRowSetImpl:resBundle	Lcom/sun/rowset/JdbcRowSetResourceBundle;
/*       */     //   96: ldc 4
/*       */     //   98: invokevirtual 1474	com/sun/rowset/JdbcRowSetResourceBundle:handleGetObject	(Ljava/lang/String;)Ljava/lang/Object;
/*       */     //   101: invokevirtual 1547	java/lang/Object:toString	()Ljava/lang/String;
/*       */     //   104: iconst_1
/*       */     //   105: anewarray 699	java/lang/Object
/*       */     //   108: dup
/*       */     //   109: iconst_0
/*       */     //   110: aload 4
/*       */     //   112: invokevirtual 1515	java/io/StreamCorruptedException:getMessage	()Ljava/lang/String;
/*       */     //   115: aastore
/*       */     //   116: invokestatic 1587	java/text/MessageFormat:format	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
/*       */     //   119: invokespecial 1576	java/sql/SQLException:<init>	(Ljava/lang/String;)V
/*       */     //   122: athrow
/*       */     //   123: astore 4
/*       */     //   125: new 718	java/sql/SQLException
/*       */     //   128: dup
/*       */     //   129: aload_0
/*       */     //   130: getfield 1324	com/sun/rowset/CachedRowSetImpl:resBundle	Lcom/sun/rowset/JdbcRowSetResourceBundle;
/*       */     //   133: ldc 4
/*       */     //   135: invokevirtual 1474	com/sun/rowset/JdbcRowSetResourceBundle:handleGetObject	(Ljava/lang/String;)Ljava/lang/Object;
/*       */     //   138: invokevirtual 1547	java/lang/Object:toString	()Ljava/lang/String;
/*       */     //   141: iconst_1
/*       */     //   142: anewarray 699	java/lang/Object
/*       */     //   145: dup
/*       */     //   146: iconst_0
/*       */     //   147: aload 4
/*       */     //   149: invokevirtual 1504	java/io/IOException:getMessage	()Ljava/lang/String;
/*       */     //   152: aastore
/*       */     //   153: invokestatic 1587	java/text/MessageFormat:format	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
/*       */     //   156: invokespecial 1576	java/sql/SQLException:<init>	(Ljava/lang/String;)V
/*       */     //   159: athrow
/*       */     //   160: aload_3
/*       */     //   161: invokevirtual 1509	java/io/ObjectInputStream:readObject	()Ljava/lang/Object;
/*       */     //   164: checkcast 664	com/sun/rowset/CachedRowSetImpl
/*       */     //   167: astore 4
/*       */     //   169: aload 4
/*       */     //   171: aload_0
/*       */     //   172: getfield 1324	com/sun/rowset/CachedRowSetImpl:resBundle	Lcom/sun/rowset/JdbcRowSetResourceBundle;
/*       */     //   175: putfield 1324	com/sun/rowset/CachedRowSetImpl:resBundle	Lcom/sun/rowset/JdbcRowSetResourceBundle;
/*       */     //   178: aload 4
/*       */     //   180: areturn
/*       */     //   181: astore 4
/*       */     //   183: new 718	java/sql/SQLException
/*       */     //   186: dup
/*       */     //   187: aload_0
/*       */     //   188: getfield 1324	com/sun/rowset/CachedRowSetImpl:resBundle	Lcom/sun/rowset/JdbcRowSetResourceBundle;
/*       */     //   191: ldc 4
/*       */     //   193: invokevirtual 1474	com/sun/rowset/JdbcRowSetResourceBundle:handleGetObject	(Ljava/lang/String;)Ljava/lang/Object;
/*       */     //   196: invokevirtual 1547	java/lang/Object:toString	()Ljava/lang/String;
/*       */     //   199: iconst_1
/*       */     //   200: anewarray 699	java/lang/Object
/*       */     //   203: dup
/*       */     //   204: iconst_0
/*       */     //   205: aload 4
/*       */     //   207: invokevirtual 1527	java/lang/ClassNotFoundException:getMessage	()Ljava/lang/String;
/*       */     //   210: aastore
/*       */     //   211: invokestatic 1587	java/text/MessageFormat:format	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
/*       */     //   214: invokespecial 1576	java/sql/SQLException:<init>	(Ljava/lang/String;)V
/*       */     //   217: athrow
/*       */     //   218: astore 4
/*       */     //   220: new 718	java/sql/SQLException
/*       */     //   223: dup
/*       */     //   224: aload_0
/*       */     //   225: getfield 1324	com/sun/rowset/CachedRowSetImpl:resBundle	Lcom/sun/rowset/JdbcRowSetResourceBundle;
/*       */     //   228: ldc 4
/*       */     //   230: invokevirtual 1474	com/sun/rowset/JdbcRowSetResourceBundle:handleGetObject	(Ljava/lang/String;)Ljava/lang/Object;
/*       */     //   233: invokevirtual 1547	java/lang/Object:toString	()Ljava/lang/String;
/*       */     //   236: iconst_1
/*       */     //   237: anewarray 699	java/lang/Object
/*       */     //   240: dup
/*       */     //   241: iconst_0
/*       */     //   242: aload 4
/*       */     //   244: invokevirtual 1512	java/io/OptionalDataException:getMessage	()Ljava/lang/String;
/*       */     //   247: aastore
/*       */     //   248: invokestatic 1587	java/text/MessageFormat:format	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
/*       */     //   251: invokespecial 1576	java/sql/SQLException:<init>	(Ljava/lang/String;)V
/*       */     //   254: athrow
/*       */     //   255: astore 4
/*       */     //   257: new 718	java/sql/SQLException
/*       */     //   260: dup
/*       */     //   261: aload_0
/*       */     //   262: getfield 1324	com/sun/rowset/CachedRowSetImpl:resBundle	Lcom/sun/rowset/JdbcRowSetResourceBundle;
/*       */     //   265: ldc 4
/*       */     //   267: invokevirtual 1474	com/sun/rowset/JdbcRowSetResourceBundle:handleGetObject	(Ljava/lang/String;)Ljava/lang/Object;
/*       */     //   270: invokevirtual 1547	java/lang/Object:toString	()Ljava/lang/String;
/*       */     //   273: iconst_1
/*       */     //   274: anewarray 699	java/lang/Object
/*       */     //   277: dup
/*       */     //   278: iconst_0
/*       */     //   279: aload 4
/*       */     //   281: invokevirtual 1504	java/io/IOException:getMessage	()Ljava/lang/String;
/*       */     //   284: aastore
/*       */     //   285: invokestatic 1587	java/text/MessageFormat:format	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
/*       */     //   288: invokespecial 1576	java/sql/SQLException:<init>	(Ljava/lang/String;)V
/*       */     //   291: athrow
/*       */     //
/*       */     // Exception table:
/*       */     //   from	to	target	type
/*       */     //   8	22	25	java/io/IOException
/*       */     //   60	83	86	java/io/StreamCorruptedException
/*       */     //   60	83	123	java/io/IOException
/*       */     //   160	180	181	java/lang/ClassNotFoundException
/*       */     //   160	180	218	java/io/OptionalDataException
/*       */     //   160	180	255	java/io/IOException } 
/*  1221 */   public CachedRowSet createCopySchema() throws SQLException { int i = this.numRows;
/*  1222 */     this.numRows = 0;
/*       */ 
/*  1224 */     CachedRowSet localCachedRowSet = createCopy();
/*       */ 
/*  1227 */     this.numRows = i;
/*       */ 
/*  1229 */     return localCachedRowSet;
/*       */   }
/*       */ 
/*       */   public CachedRowSet createCopyNoConstraints()
/*       */     throws SQLException
/*       */   {
/*  1253 */     CachedRowSetImpl localCachedRowSetImpl = (CachedRowSetImpl)createCopy();
/*       */ 
/*  1255 */     localCachedRowSetImpl.initProperties();
/*       */     try {
/*  1257 */       localCachedRowSetImpl.unsetMatchColumn(localCachedRowSetImpl.getMatchColumnIndexes());
/*       */     }
/*       */     catch (SQLException localSQLException1)
/*       */     {
/*       */     }
/*       */     try {
/*  1263 */       localCachedRowSetImpl.unsetMatchColumn(localCachedRowSetImpl.getMatchColumnNames());
/*       */     }
/*       */     catch (SQLException localSQLException2)
/*       */     {
/*       */     }
/*  1268 */     return localCachedRowSetImpl;
/*       */   }
/*       */ 
/*       */   public Collection<?> toCollection()
/*       */     throws SQLException
/*       */   {
/*  1288 */     TreeMap localTreeMap = new TreeMap();
/*       */ 
/*  1290 */     for (int i = 0; i < this.numRows; i++) {
/*  1291 */       localTreeMap.put(Integer.valueOf(i), this.rvh.get(i));
/*       */     }
/*       */ 
/*  1294 */     return localTreeMap.values();
/*       */   }
/*       */ 
/*       */   public Collection<?> toCollection(int paramInt)
/*       */     throws SQLException
/*       */   {
/*  1317 */     int i = this.numRows;
/*  1318 */     Vector localVector = new Vector(i);
/*       */ 
/*  1322 */     CachedRowSetImpl localCachedRowSetImpl = (CachedRowSetImpl)createCopy();
/*       */ 
/*  1324 */     while (i != 0) {
/*  1325 */       localCachedRowSetImpl.next();
/*  1326 */       localVector.add(localCachedRowSetImpl.getObject(paramInt));
/*  1327 */       i--;
/*       */     }
/*       */ 
/*  1330 */     return localVector;
/*       */   }
/*       */ 
/*       */   public Collection<?> toCollection(String paramString)
/*       */     throws SQLException
/*       */   {
/*  1352 */     return toCollection(getColIdxByName(paramString));
/*       */   }
/*       */ 
/*       */   public SyncProvider getSyncProvider()
/*       */     throws SQLException
/*       */   {
/*  1371 */     return this.provider;
/*       */   }
/*       */ 
/*       */   public void setSyncProvider(String paramString)
/*       */     throws SQLException
/*       */   {
/*  1382 */     this.provider = SyncFactory.getInstance(paramString);
/*       */ 
/*  1385 */     this.rowSetReader = this.provider.getRowSetReader();
/*  1386 */     this.rowSetWriter = this.provider.getRowSetWriter();
/*       */   }
/*       */ 
/*       */   public void execute()
/*       */     throws SQLException
/*       */   {
/*  1427 */     execute(null);
/*       */   }
/*       */ 
/*       */   public boolean next()
/*       */     throws SQLException
/*       */   {
/*  1462 */     if ((this.cursorPos < 0) || (this.cursorPos >= this.numRows + 1)) {
/*  1463 */       throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.invalidcp").toString());
/*       */     }
/*       */ 
/*  1466 */     boolean bool = internalNext();
/*  1467 */     notifyCursorMoved();
/*       */ 
/*  1469 */     return bool;
/*       */   }
/*       */ 
/*       */   protected boolean internalNext()
/*       */     throws SQLException
/*       */   {
/*  1496 */     boolean bool = false;
/*       */     do
/*       */     {
/*  1499 */       if (this.cursorPos < this.numRows) {
/*  1500 */         this.cursorPos += 1;
/*  1501 */         bool = true;
/*  1502 */       } else if (this.cursorPos == this.numRows)
/*       */       {
/*  1504 */         this.cursorPos += 1;
/*  1505 */         bool = false;
/*  1506 */         break;
/*       */       }
/*       */     }
/*  1508 */     while ((!getShowDeleted()) && (rowDeleted() == true));
/*       */ 
/*  1513 */     if (bool == true)
/*  1514 */       this.absolutePos += 1;
/*       */     else {
/*  1516 */       this.absolutePos = 0;
/*       */     }
/*  1518 */     return bool;
/*       */   }
/*       */ 
/*       */   public void close()
/*       */     throws SQLException
/*       */   {
/*  1533 */     this.cursorPos = 0;
/*  1534 */     this.absolutePos = 0;
/*  1535 */     this.numRows = 0;
/*  1536 */     this.numDeleted = 0;
/*       */ 
/*  1540 */     initProperties();
/*       */ 
/*  1543 */     this.rvh.clear();
/*       */   }
/*       */ 
/*       */   public boolean wasNull()
/*       */     throws SQLException
/*       */   {
/*  1561 */     return this.lastValueNull;
/*       */   }
/*       */ 
/*       */   private void setLastValueNull(boolean paramBoolean)
/*       */   {
/*  1573 */     this.lastValueNull = paramBoolean;
/*       */   }
/*       */ 
/*       */   private void checkIndex(int paramInt)
/*       */     throws SQLException
/*       */   {
/*  1594 */     if ((paramInt < 1) || (paramInt > this.RowSetMD.getColumnCount()))
/*  1595 */       throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.invalidcol").toString());
/*       */   }
/*       */ 
/*       */   private void checkCursor()
/*       */     throws SQLException
/*       */   {
/*  1612 */     if ((isAfterLast() == true) || (isBeforeFirst() == true))
/*  1613 */       throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.invalidcp").toString());
/*       */   }
/*       */ 
/*       */   private int getColIdxByName(String paramString)
/*       */     throws SQLException
/*       */   {
/*  1629 */     this.RowSetMD = ((RowSetMetaDataImpl)getMetaData());
/*  1630 */     int i = this.RowSetMD.getColumnCount();
/*       */ 
/*  1632 */     for (int j = 1; j <= i; j++) {
/*  1633 */       String str = this.RowSetMD.getColumnName(j);
/*  1634 */       if ((str != null) && 
/*  1635 */         (paramString.equalsIgnoreCase(str))) {
/*  1636 */         return j;
/*       */       }
/*       */     }
/*       */ 
/*  1640 */     throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.invalcolnm").toString());
/*       */   }
/*       */ 
/*       */   protected BaseRow getCurrentRow()
/*       */   {
/*  1652 */     if (this.onInsertRow == true) {
/*  1653 */       return this.insertRow;
/*       */     }
/*  1655 */     return (BaseRow)this.rvh.get(this.cursorPos - 1);
/*       */   }
/*       */ 
/*       */   protected void removeCurrentRow()
/*       */   {
/*  1669 */     ((Row)getCurrentRow()).setDeleted();
/*  1670 */     this.rvh.remove(this.cursorPos - 1);
/*  1671 */     this.numRows -= 1;
/*       */   }
/*       */ 
/*       */   public String getString(int paramInt)
/*       */     throws SQLException
/*       */   {
/*  1697 */     checkIndex(paramInt);
/*       */ 
/*  1699 */     checkCursor();
/*       */ 
/*  1701 */     setLastValueNull(false);
/*  1702 */     Object localObject = getCurrentRow().getColumnObject(paramInt);
/*       */ 
/*  1705 */     if (localObject == null) {
/*  1706 */       setLastValueNull(true);
/*  1707 */       return null;
/*       */     }
/*       */ 
/*  1710 */     return localObject.toString(); } 
/*       */   public boolean getBoolean(int paramInt) throws SQLException { // Byte code:
/*       */     //   0: aload_0
/*       */     //   1: iload_1
/*       */     //   2: invokespecial 1389	com/sun/rowset/CachedRowSetImpl:checkIndex	(I)V
/*       */     //   5: aload_0
/*       */     //   6: invokespecial 1358	com/sun/rowset/CachedRowSetImpl:checkCursor	()V
/*       */     //   9: aload_0
/*       */     //   10: iconst_0
/*       */     //   11: invokespecial 1414	com/sun/rowset/CachedRowSetImpl:setLastValueNull	(Z)V
/*       */     //   14: aload_0
/*       */     //   15: invokevirtual 1418	com/sun/rowset/CachedRowSetImpl:getCurrentRow	()Lcom/sun/rowset/internal/BaseRow;
/*       */     //   18: iload_1
/*       */     //   19: invokevirtual 1476	com/sun/rowset/internal/BaseRow:getColumnObject	(I)Ljava/lang/Object;
/*       */     //   22: astore_2
/*       */     //   23: aload_2
/*       */     //   24: ifnonnull +10 -> 34
/*       */     //   27: aload_0
/*       */     //   28: iconst_1
/*       */     //   29: invokespecial 1414	com/sun/rowset/CachedRowSetImpl:setLastValueNull	(Z)V
/*       */     //   32: iconst_0
/*       */     //   33: ireturn
/*       */     //   34: aload_2
/*       */     //   35: instanceof 687
/*       */     //   38: ifeq +11 -> 49
/*       */     //   41: aload_2
/*       */     //   42: checkcast 687	java/lang/Boolean
/*       */     //   45: invokevirtual 1519	java/lang/Boolean:booleanValue	()Z
/*       */     //   48: ireturn
/*       */     //   49: new 693	java/lang/Double
/*       */     //   52: dup
/*       */     //   53: aload_2
/*       */     //   54: invokevirtual 1547	java/lang/Object:toString	()Ljava/lang/String;
/*       */     //   57: invokespecial 1532	java/lang/Double:<init>	(Ljava/lang/String;)V
/*       */     //   60: astore_3
/*       */     //   61: aload_3
/*       */     //   62: new 693	java/lang/Double
/*       */     //   65: dup
/*       */     //   66: dconst_0
/*       */     //   67: invokespecial 1530	java/lang/Double:<init>	(D)V
/*       */     //   70: invokevirtual 1531	java/lang/Double:compareTo	(Ljava/lang/Double;)I
/*       */     //   73: ifne +5 -> 78
/*       */     //   76: iconst_0
/*       */     //   77: ireturn
/*       */     //   78: iconst_1
/*       */     //   79: ireturn
/*       */     //   80: astore_3
/*       */     //   81: new 718	java/sql/SQLException
/*       */     //   84: dup
/*       */     //   85: aload_0
/*       */     //   86: getfield 1324	com/sun/rowset/CachedRowSetImpl:resBundle	Lcom/sun/rowset/JdbcRowSetResourceBundle;
/*       */     //   89: ldc_w 614
/*       */     //   92: invokevirtual 1474	com/sun/rowset/JdbcRowSetResourceBundle:handleGetObject	(Ljava/lang/String;)Ljava/lang/Object;
/*       */     //   95: invokevirtual 1547	java/lang/Object:toString	()Ljava/lang/String;
/*       */     //   98: iconst_2
/*       */     //   99: anewarray 699	java/lang/Object
/*       */     //   102: dup
/*       */     //   103: iconst_0
/*       */     //   104: aload_2
/*       */     //   105: invokevirtual 1547	java/lang/Object:toString	()Ljava/lang/String;
/*       */     //   108: invokevirtual 1558	java/lang/String:trim	()Ljava/lang/String;
/*       */     //   111: aastore
/*       */     //   112: dup
/*       */     //   113: iconst_1
/*       */     //   114: iload_1
/*       */     //   115: invokestatic 1537	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
/*       */     //   118: aastore
/*       */     //   119: invokestatic 1587	java/text/MessageFormat:format	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
/*       */     //   122: invokespecial 1576	java/sql/SQLException:<init>	(Ljava/lang/String;)V
/*       */     //   125: athrow
/*       */     //
/*       */     // Exception table:
/*       */     //   from	to	target	type
/*       */     //   49	77	80	java/lang/NumberFormatException
/*       */     //   78	79	80	java/lang/NumberFormatException } 
/*       */   public byte getByte(int paramInt) throws SQLException { // Byte code:
/*       */     //   0: aload_0
/*       */     //   1: iload_1
/*       */     //   2: invokespecial 1389	com/sun/rowset/CachedRowSetImpl:checkIndex	(I)V
/*       */     //   5: aload_0
/*       */     //   6: invokespecial 1358	com/sun/rowset/CachedRowSetImpl:checkCursor	()V
/*       */     //   9: aload_0
/*       */     //   10: iconst_0
/*       */     //   11: invokespecial 1414	com/sun/rowset/CachedRowSetImpl:setLastValueNull	(Z)V
/*       */     //   14: aload_0
/*       */     //   15: invokevirtual 1418	com/sun/rowset/CachedRowSetImpl:getCurrentRow	()Lcom/sun/rowset/internal/BaseRow;
/*       */     //   18: iload_1
/*       */     //   19: invokevirtual 1476	com/sun/rowset/internal/BaseRow:getColumnObject	(I)Ljava/lang/Object;
/*       */     //   22: astore_2
/*       */     //   23: aload_2
/*       */     //   24: ifnonnull +10 -> 34
/*       */     //   27: aload_0
/*       */     //   28: iconst_1
/*       */     //   29: invokespecial 1414	com/sun/rowset/CachedRowSetImpl:setLastValueNull	(Z)V
/*       */     //   32: iconst_0
/*       */     //   33: ireturn
/*       */     //   34: aload_2
/*       */     //   35: invokevirtual 1547	java/lang/Object:toString	()Ljava/lang/String;
/*       */     //   38: invokestatic 1524	java/lang/Byte:valueOf	(Ljava/lang/String;)Ljava/lang/Byte;
/*       */     //   41: invokevirtual 1522	java/lang/Byte:byteValue	()B
/*       */     //   44: ireturn
/*       */     //   45: astore_3
/*       */     //   46: new 718	java/sql/SQLException
/*       */     //   49: dup
/*       */     //   50: aload_0
/*       */     //   51: getfield 1324	com/sun/rowset/CachedRowSetImpl:resBundle	Lcom/sun/rowset/JdbcRowSetResourceBundle;
/*       */     //   54: ldc_w 615
/*       */     //   57: invokevirtual 1474	com/sun/rowset/JdbcRowSetResourceBundle:handleGetObject	(Ljava/lang/String;)Ljava/lang/Object;
/*       */     //   60: invokevirtual 1547	java/lang/Object:toString	()Ljava/lang/String;
/*       */     //   63: iconst_2
/*       */     //   64: anewarray 699	java/lang/Object
/*       */     //   67: dup
/*       */     //   68: iconst_0
/*       */     //   69: aload_2
/*       */     //   70: invokevirtual 1547	java/lang/Object:toString	()Ljava/lang/String;
/*       */     //   73: invokevirtual 1558	java/lang/String:trim	()Ljava/lang/String;
/*       */     //   76: aastore
/*       */     //   77: dup
/*       */     //   78: iconst_1
/*       */     //   79: iload_1
/*       */     //   80: invokestatic 1537	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
/*       */     //   83: aastore
/*       */     //   84: invokestatic 1587	java/text/MessageFormat:format	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
/*       */     //   87: invokespecial 1576	java/sql/SQLException:<init>	(Ljava/lang/String;)V
/*       */     //   90: athrow
/*       */     //
/*       */     // Exception table:
/*       */     //   from	to	target	type
/*       */     //   34	44	45	java/lang/NumberFormatException } 
/*       */   public short getShort(int paramInt) throws SQLException { // Byte code:
/*       */     //   0: aload_0
/*       */     //   1: iload_1
/*       */     //   2: invokespecial 1389	com/sun/rowset/CachedRowSetImpl:checkIndex	(I)V
/*       */     //   5: aload_0
/*       */     //   6: invokespecial 1358	com/sun/rowset/CachedRowSetImpl:checkCursor	()V
/*       */     //   9: aload_0
/*       */     //   10: iconst_0
/*       */     //   11: invokespecial 1414	com/sun/rowset/CachedRowSetImpl:setLastValueNull	(Z)V
/*       */     //   14: aload_0
/*       */     //   15: invokevirtual 1418	com/sun/rowset/CachedRowSetImpl:getCurrentRow	()Lcom/sun/rowset/internal/BaseRow;
/*       */     //   18: iload_1
/*       */     //   19: invokevirtual 1476	com/sun/rowset/internal/BaseRow:getColumnObject	(I)Ljava/lang/Object;
/*       */     //   22: astore_2
/*       */     //   23: aload_2
/*       */     //   24: ifnonnull +10 -> 34
/*       */     //   27: aload_0
/*       */     //   28: iconst_1
/*       */     //   29: invokespecial 1414	com/sun/rowset/CachedRowSetImpl:setLastValueNull	(Z)V
/*       */     //   32: iconst_0
/*       */     //   33: ireturn
/*       */     //   34: aload_2
/*       */     //   35: invokevirtual 1547	java/lang/Object:toString	()Ljava/lang/String;
/*       */     //   38: invokevirtual 1558	java/lang/String:trim	()Ljava/lang/String;
/*       */     //   41: invokestatic 1552	java/lang/Short:valueOf	(Ljava/lang/String;)Ljava/lang/Short;
/*       */     //   44: invokevirtual 1550	java/lang/Short:shortValue	()S
/*       */     //   47: ireturn
/*       */     //   48: astore_3
/*       */     //   49: new 718	java/sql/SQLException
/*       */     //   52: dup
/*       */     //   53: aload_0
/*       */     //   54: getfield 1324	com/sun/rowset/CachedRowSetImpl:resBundle	Lcom/sun/rowset/JdbcRowSetResourceBundle;
/*       */     //   57: ldc_w 643
/*       */     //   60: invokevirtual 1474	com/sun/rowset/JdbcRowSetResourceBundle:handleGetObject	(Ljava/lang/String;)Ljava/lang/Object;
/*       */     //   63: invokevirtual 1547	java/lang/Object:toString	()Ljava/lang/String;
/*       */     //   66: iconst_2
/*       */     //   67: anewarray 699	java/lang/Object
/*       */     //   70: dup
/*       */     //   71: iconst_0
/*       */     //   72: aload_2
/*       */     //   73: invokevirtual 1547	java/lang/Object:toString	()Ljava/lang/String;
/*       */     //   76: invokevirtual 1558	java/lang/String:trim	()Ljava/lang/String;
/*       */     //   79: aastore
/*       */     //   80: dup
/*       */     //   81: iconst_1
/*       */     //   82: iload_1
/*       */     //   83: invokestatic 1537	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
/*       */     //   86: aastore
/*       */     //   87: invokestatic 1587	java/text/MessageFormat:format	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
/*       */     //   90: invokespecial 1576	java/sql/SQLException:<init>	(Ljava/lang/String;)V
/*       */     //   93: athrow
/*       */     //
/*       */     // Exception table:
/*       */     //   from	to	target	type
/*       */     //   34	47	48	java/lang/NumberFormatException } 
/*       */   public int getInt(int paramInt) throws SQLException { // Byte code:
/*       */     //   0: aload_0
/*       */     //   1: iload_1
/*       */     //   2: invokespecial 1389	com/sun/rowset/CachedRowSetImpl:checkIndex	(I)V
/*       */     //   5: aload_0
/*       */     //   6: invokespecial 1358	com/sun/rowset/CachedRowSetImpl:checkCursor	()V
/*       */     //   9: aload_0
/*       */     //   10: iconst_0
/*       */     //   11: invokespecial 1414	com/sun/rowset/CachedRowSetImpl:setLastValueNull	(Z)V
/*       */     //   14: aload_0
/*       */     //   15: invokevirtual 1418	com/sun/rowset/CachedRowSetImpl:getCurrentRow	()Lcom/sun/rowset/internal/BaseRow;
/*       */     //   18: iload_1
/*       */     //   19: invokevirtual 1476	com/sun/rowset/internal/BaseRow:getColumnObject	(I)Ljava/lang/Object;
/*       */     //   22: astore_2
/*       */     //   23: aload_2
/*       */     //   24: ifnonnull +10 -> 34
/*       */     //   27: aload_0
/*       */     //   28: iconst_1
/*       */     //   29: invokespecial 1414	com/sun/rowset/CachedRowSetImpl:setLastValueNull	(Z)V
/*       */     //   32: iconst_0
/*       */     //   33: ireturn
/*       */     //   34: aload_2
/*       */     //   35: invokevirtual 1547	java/lang/Object:toString	()Ljava/lang/String;
/*       */     //   38: invokevirtual 1558	java/lang/String:trim	()Ljava/lang/String;
/*       */     //   41: invokestatic 1541	java/lang/Integer:valueOf	(Ljava/lang/String;)Ljava/lang/Integer;
/*       */     //   44: invokevirtual 1536	java/lang/Integer:intValue	()I
/*       */     //   47: ireturn
/*       */     //   48: astore_3
/*       */     //   49: new 718	java/sql/SQLException
/*       */     //   52: dup
/*       */     //   53: aload_0
/*       */     //   54: getfield 1324	com/sun/rowset/CachedRowSetImpl:resBundle	Lcom/sun/rowset/JdbcRowSetResourceBundle;
/*       */     //   57: ldc_w 624
/*       */     //   60: invokevirtual 1474	com/sun/rowset/JdbcRowSetResourceBundle:handleGetObject	(Ljava/lang/String;)Ljava/lang/Object;
/*       */     //   63: invokevirtual 1547	java/lang/Object:toString	()Ljava/lang/String;
/*       */     //   66: iconst_2
/*       */     //   67: anewarray 699	java/lang/Object
/*       */     //   70: dup
/*       */     //   71: iconst_0
/*       */     //   72: aload_2
/*       */     //   73: invokevirtual 1547	java/lang/Object:toString	()Ljava/lang/String;
/*       */     //   76: invokevirtual 1558	java/lang/String:trim	()Ljava/lang/String;
/*       */     //   79: aastore
/*       */     //   80: dup
/*       */     //   81: iconst_1
/*       */     //   82: iload_1
/*       */     //   83: invokestatic 1537	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
/*       */     //   86: aastore
/*       */     //   87: invokestatic 1587	java/text/MessageFormat:format	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
/*       */     //   90: invokespecial 1576	java/sql/SQLException:<init>	(Ljava/lang/String;)V
/*       */     //   93: athrow
/*       */     //
/*       */     // Exception table:
/*       */     //   from	to	target	type
/*       */     //   34	47	48	java/lang/NumberFormatException } 
/*       */   public long getLong(int paramInt) throws SQLException { // Byte code:
/*       */     //   0: aload_0
/*       */     //   1: iload_1
/*       */     //   2: invokespecial 1389	com/sun/rowset/CachedRowSetImpl:checkIndex	(I)V
/*       */     //   5: aload_0
/*       */     //   6: invokespecial 1358	com/sun/rowset/CachedRowSetImpl:checkCursor	()V
/*       */     //   9: aload_0
/*       */     //   10: iconst_0
/*       */     //   11: invokespecial 1414	com/sun/rowset/CachedRowSetImpl:setLastValueNull	(Z)V
/*       */     //   14: aload_0
/*       */     //   15: invokevirtual 1418	com/sun/rowset/CachedRowSetImpl:getCurrentRow	()Lcom/sun/rowset/internal/BaseRow;
/*       */     //   18: iload_1
/*       */     //   19: invokevirtual 1476	com/sun/rowset/internal/BaseRow:getColumnObject	(I)Ljava/lang/Object;
/*       */     //   22: astore_2
/*       */     //   23: aload_2
/*       */     //   24: ifnonnull +10 -> 34
/*       */     //   27: aload_0
/*       */     //   28: iconst_1
/*       */     //   29: invokespecial 1414	com/sun/rowset/CachedRowSetImpl:setLastValueNull	(Z)V
/*       */     //   32: lconst_0
/*       */     //   33: lreturn
/*       */     //   34: aload_2
/*       */     //   35: invokevirtual 1547	java/lang/Object:toString	()Ljava/lang/String;
/*       */     //   38: invokevirtual 1558	java/lang/String:trim	()Ljava/lang/String;
/*       */     //   41: invokestatic 1544	java/lang/Long:valueOf	(Ljava/lang/String;)Ljava/lang/Long;
/*       */     //   44: invokevirtual 1542	java/lang/Long:longValue	()J
/*       */     //   47: lreturn
/*       */     //   48: astore_3
/*       */     //   49: new 718	java/sql/SQLException
/*       */     //   52: dup
/*       */     //   53: aload_0
/*       */     //   54: getfield 1324	com/sun/rowset/CachedRowSetImpl:resBundle	Lcom/sun/rowset/JdbcRowSetResourceBundle;
/*       */     //   57: ldc_w 627
/*       */     //   60: invokevirtual 1474	com/sun/rowset/JdbcRowSetResourceBundle:handleGetObject	(Ljava/lang/String;)Ljava/lang/Object;
/*       */     //   63: invokevirtual 1547	java/lang/Object:toString	()Ljava/lang/String;
/*       */     //   66: iconst_2
/*       */     //   67: anewarray 699	java/lang/Object
/*       */     //   70: dup
/*       */     //   71: iconst_0
/*       */     //   72: aload_2
/*       */     //   73: invokevirtual 1547	java/lang/Object:toString	()Ljava/lang/String;
/*       */     //   76: invokevirtual 1558	java/lang/String:trim	()Ljava/lang/String;
/*       */     //   79: aastore
/*       */     //   80: dup
/*       */     //   81: iconst_1
/*       */     //   82: iload_1
/*       */     //   83: invokestatic 1537	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
/*       */     //   86: aastore
/*       */     //   87: invokestatic 1587	java/text/MessageFormat:format	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
/*       */     //   90: invokespecial 1576	java/sql/SQLException:<init>	(Ljava/lang/String;)V
/*       */     //   93: athrow
/*       */     //
/*       */     // Exception table:
/*       */     //   from	to	target	type
/*       */     //   34	47	48	java/lang/NumberFormatException } 
/*       */   public float getFloat(int paramInt) throws SQLException { // Byte code:
/*       */     //   0: aload_0
/*       */     //   1: iload_1
/*       */     //   2: invokespecial 1389	com/sun/rowset/CachedRowSetImpl:checkIndex	(I)V
/*       */     //   5: aload_0
/*       */     //   6: invokespecial 1358	com/sun/rowset/CachedRowSetImpl:checkCursor	()V
/*       */     //   9: aload_0
/*       */     //   10: iconst_0
/*       */     //   11: invokespecial 1414	com/sun/rowset/CachedRowSetImpl:setLastValueNull	(Z)V
/*       */     //   14: aload_0
/*       */     //   15: invokevirtual 1418	com/sun/rowset/CachedRowSetImpl:getCurrentRow	()Lcom/sun/rowset/internal/BaseRow;
/*       */     //   18: iload_1
/*       */     //   19: invokevirtual 1476	com/sun/rowset/internal/BaseRow:getColumnObject	(I)Ljava/lang/Object;
/*       */     //   22: astore_2
/*       */     //   23: aload_2
/*       */     //   24: ifnonnull +10 -> 34
/*       */     //   27: aload_0
/*       */     //   28: iconst_1
/*       */     //   29: invokespecial 1414	com/sun/rowset/CachedRowSetImpl:setLastValueNull	(Z)V
/*       */     //   32: fconst_0
/*       */     //   33: freturn
/*       */     //   34: new 695	java/lang/Float
/*       */     //   37: dup
/*       */     //   38: aload_2
/*       */     //   39: invokevirtual 1547	java/lang/Object:toString	()Ljava/lang/String;
/*       */     //   42: invokespecial 1535	java/lang/Float:<init>	(Ljava/lang/String;)V
/*       */     //   45: invokevirtual 1533	java/lang/Float:floatValue	()F
/*       */     //   48: freturn
/*       */     //   49: astore_3
/*       */     //   50: new 718	java/sql/SQLException
/*       */     //   53: dup
/*       */     //   54: aload_0
/*       */     //   55: getfield 1324	com/sun/rowset/CachedRowSetImpl:resBundle	Lcom/sun/rowset/JdbcRowSetResourceBundle;
/*       */     //   58: ldc_w 622
/*       */     //   61: invokevirtual 1474	com/sun/rowset/JdbcRowSetResourceBundle:handleGetObject	(Ljava/lang/String;)Ljava/lang/Object;
/*       */     //   64: invokevirtual 1547	java/lang/Object:toString	()Ljava/lang/String;
/*       */     //   67: iconst_2
/*       */     //   68: anewarray 699	java/lang/Object
/*       */     //   71: dup
/*       */     //   72: iconst_0
/*       */     //   73: aload_2
/*       */     //   74: invokevirtual 1547	java/lang/Object:toString	()Ljava/lang/String;
/*       */     //   77: invokevirtual 1558	java/lang/String:trim	()Ljava/lang/String;
/*       */     //   80: aastore
/*       */     //   81: dup
/*       */     //   82: iconst_1
/*       */     //   83: iload_1
/*       */     //   84: invokestatic 1537	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
/*       */     //   87: aastore
/*       */     //   88: invokestatic 1587	java/text/MessageFormat:format	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
/*       */     //   91: invokespecial 1576	java/sql/SQLException:<init>	(Ljava/lang/String;)V
/*       */     //   94: athrow
/*       */     //
/*       */     // Exception table:
/*       */     //   from	to	target	type
/*       */     //   34	48	49	java/lang/NumberFormatException } 
/*       */   public double getDouble(int paramInt) throws SQLException { // Byte code:
/*       */     //   0: aload_0
/*       */     //   1: iload_1
/*       */     //   2: invokespecial 1389	com/sun/rowset/CachedRowSetImpl:checkIndex	(I)V
/*       */     //   5: aload_0
/*       */     //   6: invokespecial 1358	com/sun/rowset/CachedRowSetImpl:checkCursor	()V
/*       */     //   9: aload_0
/*       */     //   10: iconst_0
/*       */     //   11: invokespecial 1414	com/sun/rowset/CachedRowSetImpl:setLastValueNull	(Z)V
/*       */     //   14: aload_0
/*       */     //   15: invokevirtual 1418	com/sun/rowset/CachedRowSetImpl:getCurrentRow	()Lcom/sun/rowset/internal/BaseRow;
/*       */     //   18: iload_1
/*       */     //   19: invokevirtual 1476	com/sun/rowset/internal/BaseRow:getColumnObject	(I)Ljava/lang/Object;
/*       */     //   22: astore_2
/*       */     //   23: aload_2
/*       */     //   24: ifnonnull +10 -> 34
/*       */     //   27: aload_0
/*       */     //   28: iconst_1
/*       */     //   29: invokespecial 1414	com/sun/rowset/CachedRowSetImpl:setLastValueNull	(Z)V
/*       */     //   32: dconst_0
/*       */     //   33: dreturn
/*       */     //   34: new 693	java/lang/Double
/*       */     //   37: dup
/*       */     //   38: aload_2
/*       */     //   39: invokevirtual 1547	java/lang/Object:toString	()Ljava/lang/String;
/*       */     //   42: invokevirtual 1558	java/lang/String:trim	()Ljava/lang/String;
/*       */     //   45: invokespecial 1532	java/lang/Double:<init>	(Ljava/lang/String;)V
/*       */     //   48: invokevirtual 1529	java/lang/Double:doubleValue	()D
/*       */     //   51: dreturn
/*       */     //   52: astore_3
/*       */     //   53: new 718	java/sql/SQLException
/*       */     //   56: dup
/*       */     //   57: aload_0
/*       */     //   58: getfield 1324	com/sun/rowset/CachedRowSetImpl:resBundle	Lcom/sun/rowset/JdbcRowSetResourceBundle;
/*       */     //   61: ldc_w 617
/*       */     //   64: invokevirtual 1474	com/sun/rowset/JdbcRowSetResourceBundle:handleGetObject	(Ljava/lang/String;)Ljava/lang/Object;
/*       */     //   67: invokevirtual 1547	java/lang/Object:toString	()Ljava/lang/String;
/*       */     //   70: iconst_2
/*       */     //   71: anewarray 699	java/lang/Object
/*       */     //   74: dup
/*       */     //   75: iconst_0
/*       */     //   76: aload_2
/*       */     //   77: invokevirtual 1547	java/lang/Object:toString	()Ljava/lang/String;
/*       */     //   80: invokevirtual 1558	java/lang/String:trim	()Ljava/lang/String;
/*       */     //   83: aastore
/*       */     //   84: dup
/*       */     //   85: iconst_1
/*       */     //   86: iload_1
/*       */     //   87: invokestatic 1537	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
/*       */     //   90: aastore
/*       */     //   91: invokestatic 1587	java/text/MessageFormat:format	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
/*       */     //   94: invokespecial 1576	java/sql/SQLException:<init>	(Ljava/lang/String;)V
/*       */     //   97: athrow
/*       */     //
/*       */     // Exception table:
/*       */     //   from	to	target	type
/*       */     //   34	51	52	java/lang/NumberFormatException } 
/*  2051 */   /** @deprecated */
/*       */   public BigDecimal getBigDecimal(int paramInt1, int paramInt2) throws SQLException { checkIndex(paramInt1);
/*       */ 
/*  2053 */     checkCursor();
/*       */ 
/*  2055 */     setLastValueNull(false);
/*  2056 */     Object localObject = getCurrentRow().getColumnObject(paramInt1);
/*       */ 
/*  2059 */     if (localObject == null) {
/*  2060 */       setLastValueNull(true);
/*  2061 */       return new BigDecimal(0);
/*       */     }
/*       */ 
/*  2064 */     BigDecimal localBigDecimal1 = getBigDecimal(paramInt1);
/*       */ 
/*  2066 */     BigDecimal localBigDecimal2 = localBigDecimal1.setScale(paramInt2);
/*       */ 
/*  2068 */     return localBigDecimal2;
/*       */   }
/*       */ 
/*       */   public byte[] getBytes(int paramInt)
/*       */     throws SQLException
/*       */   {
/*  2093 */     checkIndex(paramInt);
/*       */ 
/*  2095 */     checkCursor();
/*       */ 
/*  2097 */     if (!isBinary(this.RowSetMD.getColumnType(paramInt))) {
/*  2098 */       throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.dtypemismt").toString());
/*       */     }
/*       */ 
/*  2101 */     return (byte[])getCurrentRow().getColumnObject(paramInt);
/*       */   }
/*       */ 
/*       */   public java.sql.Date getDate(int paramInt)
/*       */     throws SQLException
/*       */   {
/*  2122 */     checkIndex(paramInt);
/*       */ 
/*  2124 */     checkCursor();
/*       */ 
/*  2126 */     setLastValueNull(false);
/*  2127 */     Object localObject = getCurrentRow().getColumnObject(paramInt);
/*       */ 
/*  2130 */     if (localObject == null) {
/*  2131 */       setLastValueNull(true);
/*  2132 */       return null;
/*       */     }
/*       */     long l;
/*  2142 */     switch (this.RowSetMD.getColumnType(paramInt)) {
/*       */     case 91:
/*  2144 */       l = ((java.sql.Date)localObject).getTime();
/*  2145 */       return new java.sql.Date(l);
/*       */     case 93:
/*  2148 */       l = ((Timestamp)localObject).getTime();
/*  2149 */       return new java.sql.Date(l);
/*       */     case -1:
/*       */     case 1:
/*       */     case 12:
/*       */       try
/*       */       {
/*  2155 */         DateFormat localDateFormat = DateFormat.getDateInstance();
/*  2156 */         return (java.sql.Date)localDateFormat.parse(localObject.toString());
/*       */       } catch (ParseException localParseException) {
/*  2158 */         throw new SQLException(MessageFormat.format(this.resBundle.handleGetObject("cachedrowsetimpl.datefail").toString(), new Object[] { localObject.toString().trim(), Integer.valueOf(paramInt) }));
/*       */       }
/*       */ 
/*       */     }
/*       */ 
/*  2163 */     throw new SQLException(MessageFormat.format(this.resBundle.handleGetObject("cachedrowsetimpl.datefail").toString(), new Object[] { localObject.toString().trim(), Integer.valueOf(paramInt) }));
/*       */   }
/*       */ 
/*       */   public Time getTime(int paramInt)
/*       */     throws SQLException
/*       */   {
/*  2186 */     checkIndex(paramInt);
/*       */ 
/*  2188 */     checkCursor();
/*       */ 
/*  2190 */     setLastValueNull(false);
/*  2191 */     Object localObject = getCurrentRow().getColumnObject(paramInt);
/*       */ 
/*  2194 */     if (localObject == null) {
/*  2195 */       setLastValueNull(true);
/*  2196 */       return null;
/*       */     }
/*       */ 
/*  2206 */     switch (this.RowSetMD.getColumnType(paramInt)) {
/*       */     case 92:
/*  2208 */       return (Time)localObject;
/*       */     case 93:
/*  2211 */       long l = ((Timestamp)localObject).getTime();
/*  2212 */       return new Time(l);
/*       */     case -1:
/*       */     case 1:
/*       */     case 12:
/*       */       try
/*       */       {
/*  2218 */         DateFormat localDateFormat = DateFormat.getTimeInstance();
/*  2219 */         return (Time)localDateFormat.parse(localObject.toString());
/*       */       } catch (ParseException localParseException) {
/*  2221 */         throw new SQLException(MessageFormat.format(this.resBundle.handleGetObject("cachedrowsetimpl.timefail").toString(), new Object[] { localObject.toString().trim(), Integer.valueOf(paramInt) }));
/*       */       }
/*       */ 
/*       */     }
/*       */ 
/*  2226 */     throw new SQLException(MessageFormat.format(this.resBundle.handleGetObject("cachedrowsetimpl.timefail").toString(), new Object[] { localObject.toString().trim(), Integer.valueOf(paramInt) }));
/*       */   }
/*       */ 
/*       */   public Timestamp getTimestamp(int paramInt)
/*       */     throws SQLException
/*       */   {
/*  2249 */     checkIndex(paramInt);
/*       */ 
/*  2251 */     checkCursor();
/*       */ 
/*  2253 */     setLastValueNull(false);
/*  2254 */     Object localObject = getCurrentRow().getColumnObject(paramInt);
/*       */ 
/*  2257 */     if (localObject == null) {
/*  2258 */       setLastValueNull(true);
/*  2259 */       return null;
/*       */     }
/*       */     long l;
/*  2269 */     switch (this.RowSetMD.getColumnType(paramInt)) {
/*       */     case 93:
/*  2271 */       return (Timestamp)localObject;
/*       */     case 92:
/*  2274 */       l = ((Time)localObject).getTime();
/*  2275 */       return new Timestamp(l);
/*       */     case 91:
/*  2278 */       l = ((java.sql.Date)localObject).getTime();
/*  2279 */       return new Timestamp(l);
/*       */     case -1:
/*       */     case 1:
/*       */     case 12:
/*       */       try
/*       */       {
/*  2285 */         DateFormat localDateFormat = DateFormat.getTimeInstance();
/*  2286 */         return (Timestamp)localDateFormat.parse(localObject.toString());
/*       */       } catch (ParseException localParseException) {
/*  2288 */         throw new SQLException(MessageFormat.format(this.resBundle.handleGetObject("cachedrowsetimpl.timefail").toString(), new Object[] { localObject.toString().trim(), Integer.valueOf(paramInt) }));
/*       */       }
/*       */ 
/*       */     }
/*       */ 
/*  2293 */     throw new SQLException(MessageFormat.format(this.resBundle.handleGetObject("cachedrowsetimpl.timefail").toString(), new Object[] { localObject.toString().trim(), Integer.valueOf(paramInt) }));
/*       */   }
/*       */ 
/*       */   public InputStream getAsciiStream(int paramInt)
/*       */     throws SQLException
/*       */   {
/*  2334 */     this.asciiStream = null;
/*       */ 
/*  2337 */     checkIndex(paramInt);
/*       */ 
/*  2339 */     checkCursor();
/*       */ 
/*  2341 */     Object localObject = getCurrentRow().getColumnObject(paramInt);
/*  2342 */     if (localObject == null) {
/*  2343 */       this.lastValueNull = true;
/*  2344 */       return null;
/*       */     }
/*       */     try
/*       */     {
/*  2348 */       if (isString(this.RowSetMD.getColumnType(paramInt)))
/*  2349 */         this.asciiStream = new ByteArrayInputStream(((String)localObject).getBytes("ASCII"));
/*       */       else
/*  2351 */         throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.dtypemismt").toString());
/*       */     }
/*       */     catch (UnsupportedEncodingException localUnsupportedEncodingException) {
/*  2354 */       throw new SQLException(localUnsupportedEncodingException.getMessage());
/*       */     }
/*       */ 
/*  2357 */     return this.asciiStream;
/*       */   }
/*       */ 
/*       */   /** @deprecated */
/*       */   public InputStream getUnicodeStream(int paramInt)
/*       */     throws SQLException
/*       */   {
/*  2383 */     this.unicodeStream = null;
/*       */ 
/*  2386 */     checkIndex(paramInt);
/*       */ 
/*  2388 */     checkCursor();
/*       */ 
/*  2390 */     if ((!isBinary(this.RowSetMD.getColumnType(paramInt))) && (!isString(this.RowSetMD.getColumnType(paramInt))))
/*       */     {
/*  2392 */       throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.dtypemismt").toString());
/*       */     }
/*       */ 
/*  2395 */     Object localObject = getCurrentRow().getColumnObject(paramInt);
/*  2396 */     if (localObject == null) {
/*  2397 */       this.lastValueNull = true;
/*  2398 */       return null;
/*       */     }
/*       */ 
/*  2401 */     this.unicodeStream = new StringBufferInputStream(localObject.toString());
/*       */ 
/*  2403 */     return this.unicodeStream;
/*       */   }
/*       */ 
/*       */   public InputStream getBinaryStream(int paramInt)
/*       */     throws SQLException
/*       */   {
/*  2439 */     this.binaryStream = null;
/*       */ 
/*  2442 */     checkIndex(paramInt);
/*       */ 
/*  2444 */     checkCursor();
/*       */ 
/*  2446 */     if (!isBinary(this.RowSetMD.getColumnType(paramInt))) {
/*  2447 */       throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.dtypemismt").toString());
/*       */     }
/*       */ 
/*  2450 */     Object localObject = getCurrentRow().getColumnObject(paramInt);
/*  2451 */     if (localObject == null) {
/*  2452 */       this.lastValueNull = true;
/*  2453 */       return null;
/*       */     }
/*       */ 
/*  2456 */     this.binaryStream = new ByteArrayInputStream((byte[])localObject);
/*       */ 
/*  2458 */     return this.binaryStream;
/*       */   }
/*       */ 
/*       */   public String getString(String paramString)
/*       */     throws SQLException
/*       */   {
/*  2482 */     return getString(getColIdxByName(paramString));
/*       */   }
/*       */ 
/*       */   public boolean getBoolean(String paramString)
/*       */     throws SQLException
/*       */   {
/*  2501 */     return getBoolean(getColIdxByName(paramString));
/*       */   }
/*       */ 
/*       */   public byte getByte(String paramString)
/*       */     throws SQLException
/*       */   {
/*  2521 */     return getByte(getColIdxByName(paramString));
/*       */   }
/*       */ 
/*       */   public short getShort(String paramString)
/*       */     throws SQLException
/*       */   {
/*  2542 */     return getShort(getColIdxByName(paramString));
/*       */   }
/*       */ 
/*       */   public int getInt(String paramString)
/*       */     throws SQLException
/*       */   {
/*  2563 */     return getInt(getColIdxByName(paramString));
/*       */   }
/*       */ 
/*       */   public long getLong(String paramString)
/*       */     throws SQLException
/*       */   {
/*  2584 */     return getLong(getColIdxByName(paramString));
/*       */   }
/*       */ 
/*       */   public float getFloat(String paramString)
/*       */     throws SQLException
/*       */   {
/*  2605 */     return getFloat(getColIdxByName(paramString));
/*       */   }
/*       */ 
/*       */   public double getDouble(String paramString)
/*       */     throws SQLException
/*       */   {
/*  2627 */     return getDouble(getColIdxByName(paramString));
/*       */   }
/*       */ 
/*       */   /** @deprecated */
/*       */   public BigDecimal getBigDecimal(String paramString, int paramInt)
/*       */     throws SQLException
/*       */   {
/*  2651 */     return getBigDecimal(getColIdxByName(paramString), paramInt);
/*       */   }
/*       */ 
/*       */   public byte[] getBytes(String paramString)
/*       */     throws SQLException
/*       */   {
/*  2672 */     return getBytes(getColIdxByName(paramString));
/*       */   }
/*       */ 
/*       */   public java.sql.Date getDate(String paramString)
/*       */     throws SQLException
/*       */   {
/*  2690 */     return getDate(getColIdxByName(paramString));
/*       */   }
/*       */ 
/*       */   public Time getTime(String paramString)
/*       */     throws SQLException
/*       */   {
/*  2706 */     return getTime(getColIdxByName(paramString));
/*       */   }
/*       */ 
/*       */   public Timestamp getTimestamp(String paramString)
/*       */     throws SQLException
/*       */   {
/*  2722 */     return getTimestamp(getColIdxByName(paramString));
/*       */   }
/*       */ 
/*       */   public InputStream getAsciiStream(String paramString)
/*       */     throws SQLException
/*       */   {
/*  2756 */     return getAsciiStream(getColIdxByName(paramString));
/*       */   }
/*       */ 
/*       */   /** @deprecated */
/*       */   public InputStream getUnicodeStream(String paramString)
/*       */     throws SQLException
/*       */   {
/*  2782 */     return getUnicodeStream(getColIdxByName(paramString));
/*       */   }
/*       */ 
/*       */   public InputStream getBinaryStream(String paramString)
/*       */     throws SQLException
/*       */   {
/*  2815 */     return getBinaryStream(getColIdxByName(paramString));
/*       */   }
/*       */ 
/*       */   public SQLWarning getWarnings()
/*       */   {
/*  2837 */     return this.sqlwarn;
/*       */   }
/*       */ 
/*       */   public void clearWarnings()
/*       */   {
/*  2847 */     this.sqlwarn = null;
/*       */   }
/*       */ 
/*       */   public String getCursorName()
/*       */     throws SQLException
/*       */   {
/*  2875 */     throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.posupdate").toString());
/*       */   }
/*       */ 
/*       */   public ResultSetMetaData getMetaData()
/*       */     throws SQLException
/*       */   {
/*  2905 */     return this.RowSetMD;
/*       */   }
/*       */ 
/*       */   public Object getObject(int paramInt)
/*       */     throws SQLException
/*       */   {
/*  2946 */     checkIndex(paramInt);
/*       */ 
/*  2948 */     checkCursor();
/*       */ 
/*  2950 */     setLastValueNull(false);
/*  2951 */     Object localObject = getCurrentRow().getColumnObject(paramInt);
/*       */ 
/*  2954 */     if (localObject == null) {
/*  2955 */       setLastValueNull(true);
/*  2956 */       return null;
/*       */     }
/*  2958 */     if ((localObject instanceof Struct)) {
/*  2959 */       Struct localStruct = (Struct)localObject;
/*  2960 */       Map localMap = getTypeMap();
/*       */ 
/*  2962 */       Class localClass = (Class)localMap.get(localStruct.getSQLTypeName());
/*  2963 */       if (localClass != null)
/*       */       {
/*  2965 */         SQLData localSQLData = null;
/*       */         try {
/*  2967 */           localSQLData = (SQLData)ReflectUtil.newInstance(localClass);
/*       */         } catch (Exception localException) {
/*  2969 */           throw new SQLException("Unable to Instantiate: ", localException);
/*       */         }
/*       */ 
/*  2972 */         Object[] arrayOfObject = localStruct.getAttributes(localMap);
/*       */ 
/*  2974 */         SQLInputImpl localSQLInputImpl = new SQLInputImpl(arrayOfObject, localMap);
/*       */ 
/*  2976 */         localSQLData.readSQL(localSQLInputImpl, localStruct.getSQLTypeName());
/*  2977 */         return localSQLData;
/*       */       }
/*       */     }
/*  2980 */     return localObject;
/*       */   }
/*       */ 
/*       */   public Object getObject(String paramString)
/*       */     throws SQLException
/*       */   {
/*  3016 */     return getObject(getColIdxByName(paramString));
/*       */   }
/*       */ 
/*       */   public int findColumn(String paramString)
/*       */     throws SQLException
/*       */   {
/*  3032 */     return getColIdxByName(paramString);
/*       */   }
/*       */ 
/*       */   public Reader getCharacterStream(int paramInt)
/*       */     throws SQLException
/*       */   {
/*  3068 */     checkIndex(paramInt);
/*       */ 
/*  3070 */     checkCursor();
/*       */     Object localObject;
/*  3072 */     if (isBinary(this.RowSetMD.getColumnType(paramInt))) {
/*  3073 */       localObject = getCurrentRow().getColumnObject(paramInt);
/*  3074 */       if (localObject == null) {
/*  3075 */         this.lastValueNull = true;
/*  3076 */         return null;
/*       */       }
/*  3078 */       this.charStream = new InputStreamReader(new ByteArrayInputStream((byte[])localObject));
/*       */     }
/*  3080 */     else if (isString(this.RowSetMD.getColumnType(paramInt))) {
/*  3081 */       localObject = getCurrentRow().getColumnObject(paramInt);
/*  3082 */       if (localObject == null) {
/*  3083 */         this.lastValueNull = true;
/*  3084 */         return null;
/*       */       }
/*  3086 */       this.charStream = new StringReader(localObject.toString());
/*       */     } else {
/*  3088 */       throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.dtypemismt").toString());
/*       */     }
/*       */ 
/*  3091 */     return this.charStream;
/*       */   }
/*       */ 
/*       */   public Reader getCharacterStream(String paramString)
/*       */     throws SQLException
/*       */   {
/*  3115 */     return getCharacterStream(getColIdxByName(paramString)); } 
/*       */   public BigDecimal getBigDecimal(int paramInt) throws SQLException { // Byte code:
/*       */     //   0: aload_0
/*       */     //   1: iload_1
/*       */     //   2: invokespecial 1389	com/sun/rowset/CachedRowSetImpl:checkIndex	(I)V
/*       */     //   5: aload_0
/*       */     //   6: invokespecial 1358	com/sun/rowset/CachedRowSetImpl:checkCursor	()V
/*       */     //   9: aload_0
/*       */     //   10: iconst_0
/*       */     //   11: invokespecial 1414	com/sun/rowset/CachedRowSetImpl:setLastValueNull	(Z)V
/*       */     //   14: aload_0
/*       */     //   15: invokevirtual 1418	com/sun/rowset/CachedRowSetImpl:getCurrentRow	()Lcom/sun/rowset/internal/BaseRow;
/*       */     //   18: iload_1
/*       */     //   19: invokevirtual 1476	com/sun/rowset/internal/BaseRow:getColumnObject	(I)Ljava/lang/Object;
/*       */     //   22: astore_2
/*       */     //   23: aload_2
/*       */     //   24: ifnonnull +10 -> 34
/*       */     //   27: aload_0
/*       */     //   28: iconst_1
/*       */     //   29: invokespecial 1414	com/sun/rowset/CachedRowSetImpl:setLastValueNull	(Z)V
/*       */     //   32: aconst_null
/*       */     //   33: areturn
/*       */     //   34: new 706	java/math/BigDecimal
/*       */     //   37: dup
/*       */     //   38: aload_2
/*       */     //   39: invokevirtual 1547	java/lang/Object:toString	()Ljava/lang/String;
/*       */     //   42: invokevirtual 1558	java/lang/String:trim	()Ljava/lang/String;
/*       */     //   45: invokespecial 1570	java/math/BigDecimal:<init>	(Ljava/lang/String;)V
/*       */     //   48: areturn
/*       */     //   49: astore_3
/*       */     //   50: new 718	java/sql/SQLException
/*       */     //   53: dup
/*       */     //   54: aload_0
/*       */     //   55: getfield 1324	com/sun/rowset/CachedRowSetImpl:resBundle	Lcom/sun/rowset/JdbcRowSetResourceBundle;
/*       */     //   58: ldc_w 617
/*       */     //   61: invokevirtual 1474	com/sun/rowset/JdbcRowSetResourceBundle:handleGetObject	(Ljava/lang/String;)Ljava/lang/Object;
/*       */     //   64: invokevirtual 1547	java/lang/Object:toString	()Ljava/lang/String;
/*       */     //   67: iconst_2
/*       */     //   68: anewarray 699	java/lang/Object
/*       */     //   71: dup
/*       */     //   72: iconst_0
/*       */     //   73: aload_2
/*       */     //   74: invokevirtual 1547	java/lang/Object:toString	()Ljava/lang/String;
/*       */     //   77: invokevirtual 1558	java/lang/String:trim	()Ljava/lang/String;
/*       */     //   80: aastore
/*       */     //   81: dup
/*       */     //   82: iconst_1
/*       */     //   83: iload_1
/*       */     //   84: invokestatic 1537	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
/*       */     //   87: aastore
/*       */     //   88: invokestatic 1587	java/text/MessageFormat:format	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
/*       */     //   91: invokespecial 1576	java/sql/SQLException:<init>	(Ljava/lang/String;)V
/*       */     //   94: athrow
/*       */     //
/*       */     // Exception table:
/*       */     //   from	to	target	type
/*       */     //   34	48	49	java/lang/NumberFormatException } 
/*  3181 */   public BigDecimal getBigDecimal(String paramString) throws SQLException { return getBigDecimal(getColIdxByName(paramString)); }
/*       */ 
/*       */ 
/*       */   public int size()
/*       */   {
/*  3194 */     return this.numRows;
/*       */   }
/*       */ 
/*       */   public boolean isBeforeFirst()
/*       */     throws SQLException
/*       */   {
/*  3206 */     if ((this.cursorPos == 0) && (this.numRows > 0)) {
/*  3207 */       return true;
/*       */     }
/*  3209 */     return false;
/*       */   }
/*       */ 
/*       */   public boolean isAfterLast()
/*       */     throws SQLException
/*       */   {
/*  3222 */     if ((this.cursorPos == this.numRows + 1) && (this.numRows > 0)) {
/*  3223 */       return true;
/*       */     }
/*  3225 */     return false;
/*       */   }
/*       */ 
/*       */   public boolean isFirst()
/*       */     throws SQLException
/*       */   {
/*  3239 */     int i = this.cursorPos;
/*  3240 */     int j = this.absolutePos;
/*  3241 */     internalFirst();
/*  3242 */     if (this.cursorPos == i) {
/*  3243 */       return true;
/*       */     }
/*  3245 */     this.cursorPos = i;
/*  3246 */     this.absolutePos = j;
/*  3247 */     return false;
/*       */   }
/*       */ 
/*       */   public boolean isLast()
/*       */     throws SQLException
/*       */   {
/*  3264 */     int i = this.cursorPos;
/*  3265 */     int j = this.absolutePos;
/*  3266 */     boolean bool = getShowDeleted();
/*  3267 */     setShowDeleted(true);
/*  3268 */     internalLast();
/*  3269 */     if (this.cursorPos == i) {
/*  3270 */       setShowDeleted(bool);
/*  3271 */       return true;
/*       */     }
/*  3273 */     setShowDeleted(bool);
/*  3274 */     this.cursorPos = i;
/*  3275 */     this.absolutePos = j;
/*  3276 */     return false;
/*       */   }
/*       */ 
/*       */   public void beforeFirst()
/*       */     throws SQLException
/*       */   {
/*  3289 */     if (getType() == 1003) {
/*  3290 */       throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.beforefirst").toString());
/*       */     }
/*  3292 */     this.cursorPos = 0;
/*  3293 */     this.absolutePos = 0;
/*  3294 */     notifyCursorMoved();
/*       */   }
/*       */ 
/*       */   public void afterLast()
/*       */     throws SQLException
/*       */   {
/*  3305 */     if (this.numRows > 0) {
/*  3306 */       this.cursorPos = (this.numRows + 1);
/*  3307 */       this.absolutePos = 0;
/*  3308 */       notifyCursorMoved();
/*       */     }
/*       */   }
/*       */ 
/*       */   public boolean first()
/*       */     throws SQLException
/*       */   {
/*  3324 */     if (getType() == 1003) {
/*  3325 */       throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.first").toString());
/*       */     }
/*       */ 
/*  3329 */     boolean bool = internalFirst();
/*  3330 */     notifyCursorMoved();
/*       */ 
/*  3332 */     return bool;
/*       */   }
/*       */ 
/*       */   protected boolean internalFirst()
/*       */     throws SQLException
/*       */   {
/*  3352 */     boolean bool = false;
/*       */ 
/*  3354 */     if (this.numRows > 0) {
/*  3355 */       this.cursorPos = 1;
/*  3356 */       if ((!getShowDeleted()) && (rowDeleted() == true))
/*  3357 */         bool = internalNext();
/*       */       else {
/*  3359 */         bool = true;
/*       */       }
/*       */     }
/*       */ 
/*  3363 */     if (bool == true)
/*  3364 */       this.absolutePos = 1;
/*       */     else {
/*  3366 */       this.absolutePos = 0;
/*       */     }
/*  3368 */     return bool;
/*       */   }
/*       */ 
/*       */   public boolean last()
/*       */     throws SQLException
/*       */   {
/*  3383 */     if (getType() == 1003) {
/*  3384 */       throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.last").toString());
/*       */     }
/*       */ 
/*  3388 */     boolean bool = internalLast();
/*  3389 */     notifyCursorMoved();
/*       */ 
/*  3391 */     return bool;
/*       */   }
/*       */ 
/*       */   protected boolean internalLast()
/*       */     throws SQLException
/*       */   {
/*  3412 */     boolean bool = false;
/*       */ 
/*  3414 */     if (this.numRows > 0) {
/*  3415 */       this.cursorPos = this.numRows;
/*  3416 */       if ((!getShowDeleted()) && (rowDeleted() == true))
/*  3417 */         bool = internalPrevious();
/*       */       else {
/*  3419 */         bool = true;
/*       */       }
/*       */     }
/*  3422 */     if (bool == true)
/*  3423 */       this.absolutePos = (this.numRows - this.numDeleted);
/*       */     else
/*  3425 */       this.absolutePos = 0;
/*  3426 */     return bool;
/*       */   }
/*       */ 
/*       */   public int getRow()
/*       */     throws SQLException
/*       */   {
/*  3440 */     if ((this.numRows > 0) && (this.cursorPos > 0) && (this.cursorPos < this.numRows + 1) && (!getShowDeleted()) && (!rowDeleted()))
/*       */     {
/*  3444 */       return this.absolutePos;
/*  3445 */     }if (getShowDeleted() == true) {
/*  3446 */       return this.cursorPos;
/*       */     }
/*  3448 */     return 0;
/*       */   }
/*       */ 
/*       */   public boolean absolute(int paramInt)
/*       */     throws SQLException
/*       */   {
/*  3500 */     if ((paramInt == 0) || (getType() == 1003)) {
/*  3501 */       throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.absolute").toString());
/*       */     }
/*       */ 
/*  3504 */     if (paramInt > 0) {
/*  3505 */       if (paramInt > this.numRows)
/*       */       {
/*  3507 */         afterLast();
/*  3508 */         return false;
/*       */       }
/*  3510 */       if (this.absolutePos <= 0)
/*  3511 */         internalFirst();
/*       */     }
/*       */     else {
/*  3514 */       if (this.cursorPos + paramInt < 0)
/*       */       {
/*  3516 */         beforeFirst();
/*  3517 */         return false;
/*       */       }
/*  3519 */       if (this.absolutePos >= 0) {
/*  3520 */         internalLast();
/*       */       }
/*       */ 
/*       */     }
/*       */ 
/*  3525 */     while (this.absolutePos != paramInt) {
/*  3526 */       if (this.absolutePos < paramInt) {
/*  3527 */         if (!internalNext()) {
/*  3528 */           break;
/*       */         }
/*       */       }
/*  3531 */       else if (!internalPrevious()) {
/*  3532 */         break;
/*       */       }
/*       */     }
/*       */ 
/*  3536 */     notifyCursorMoved();
/*       */ 
/*  3538 */     if ((isAfterLast()) || (isBeforeFirst())) {
/*  3539 */       return false;
/*       */     }
/*  3541 */     return true;
/*       */   }
/*       */ 
/*       */   public boolean relative(int paramInt)
/*       */     throws SQLException
/*       */   {
/*  3601 */     if ((this.numRows == 0) || (isBeforeFirst()) || (isAfterLast()) || (getType() == 1003))
/*       */     {
/*  3603 */       throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.relative").toString());
/*       */     }
/*       */ 
/*  3606 */     if (paramInt == 0)
/*  3607 */       return true;
/*       */     int i;
/*  3610 */     if (paramInt > 0) {
/*  3611 */       if (this.cursorPos + paramInt > this.numRows)
/*       */       {
/*  3613 */         afterLast();
/*       */       }
/*       */       else {
/*  3615 */         for (i = 0; (i < paramInt) && 
/*  3616 */           (internalNext()); i++);
/*       */       }
/*       */ 
/*       */     }
/*  3621 */     else if (this.cursorPos + paramInt < 0)
/*       */     {
/*  3623 */       beforeFirst();
/*       */     }
/*       */     else
/*       */     {
/*  3625 */       for (i = paramInt; (i < 0) && 
/*  3626 */         (internalPrevious()); i++);
/*       */     }
/*       */ 
/*  3631 */     notifyCursorMoved();
/*       */ 
/*  3633 */     if ((isAfterLast()) || (isBeforeFirst())) {
/*  3634 */       return false;
/*       */     }
/*  3636 */     return true;
/*       */   }
/*       */ 
/*       */   public boolean previous()
/*       */     throws SQLException
/*       */   {
/*  3683 */     if (getType() == 1003) {
/*  3684 */       throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.last").toString());
/*       */     }
/*       */ 
/*  3691 */     if ((this.cursorPos < 0) || (this.cursorPos > this.numRows + 1)) {
/*  3692 */       throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.invalidcp").toString());
/*       */     }
/*       */ 
/*  3695 */     boolean bool = internalPrevious();
/*  3696 */     notifyCursorMoved();
/*       */ 
/*  3698 */     return bool;
/*       */   }
/*       */ 
/*       */   protected boolean internalPrevious()
/*       */     throws SQLException
/*       */   {
/*  3718 */     boolean bool = false;
/*       */     do
/*       */     {
/*  3721 */       if (this.cursorPos > 1) {
/*  3722 */         this.cursorPos -= 1;
/*  3723 */         bool = true;
/*  3724 */       } else if (this.cursorPos == 1)
/*       */       {
/*  3726 */         this.cursorPos -= 1;
/*  3727 */         bool = false;
/*  3728 */         break;
/*       */       }
/*       */     }
/*  3730 */     while ((!getShowDeleted()) && (rowDeleted() == true));
/*       */ 
/*  3736 */     if (bool == true)
/*  3737 */       this.absolutePos -= 1;
/*       */     else {
/*  3739 */       this.absolutePos = 0;
/*       */     }
/*  3741 */     return bool;
/*       */   }
/*       */ 
/*       */   public boolean rowUpdated()
/*       */     throws SQLException
/*       */   {
/*  3765 */     checkCursor();
/*  3766 */     if (this.onInsertRow == true) {
/*  3767 */       throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.invalidop").toString());
/*       */     }
/*  3769 */     return ((Row)getCurrentRow()).getUpdated();
/*       */   }
/*       */ 
/*       */   public boolean columnUpdated(int paramInt)
/*       */     throws SQLException
/*       */   {
/*  3788 */     checkCursor();
/*  3789 */     if (this.onInsertRow == true) {
/*  3790 */       throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.invalidop").toString());
/*       */     }
/*  3792 */     return ((Row)getCurrentRow()).getColUpdated(paramInt - 1);
/*       */   }
/*       */ 
/*       */   public boolean columnUpdated(String paramString)
/*       */     throws SQLException
/*       */   {
/*  3811 */     return columnUpdated(getColIdxByName(paramString));
/*       */   }
/*       */ 
/*       */   public boolean rowInserted()
/*       */     throws SQLException
/*       */   {
/*  3827 */     checkCursor();
/*  3828 */     if (this.onInsertRow == true) {
/*  3829 */       throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.invalidop").toString());
/*       */     }
/*  3831 */     return ((Row)getCurrentRow()).getInserted();
/*       */   }
/*       */ 
/*       */   public boolean rowDeleted()
/*       */     throws SQLException
/*       */   {
/*  3850 */     if ((isAfterLast() == true) || (isBeforeFirst() == true) || (this.onInsertRow == true))
/*       */     {
/*  3854 */       throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.invalidcp").toString());
/*       */     }
/*  3856 */     return ((Row)getCurrentRow()).getDeleted();
/*       */   }
/*       */ 
/*       */   private boolean isNumeric(int paramInt)
/*       */   {
/*  3870 */     switch (paramInt) {
/*       */     case -7:
/*       */     case -6:
/*       */     case -5:
/*       */     case 2:
/*       */     case 3:
/*       */     case 4:
/*       */     case 5:
/*       */     case 6:
/*       */     case 7:
/*       */     case 8:
/*  3881 */       return true;
/*       */     case -4:
/*       */     case -3:
/*       */     case -2:
/*       */     case -1:
/*       */     case 0:
/*  3883 */     case 1: } return false;
/*       */   }
/*       */ 
/*       */   private boolean isString(int paramInt)
/*       */   {
/*  3896 */     switch (paramInt) {
/*       */     case -1:
/*       */     case 1:
/*       */     case 12:
/*  3900 */       return true;
/*       */     }
/*  3902 */     return false;
/*       */   }
/*       */ 
/*       */   private boolean isBinary(int paramInt)
/*       */   {
/*  3915 */     switch (paramInt) {
/*       */     case -4:
/*       */     case -3:
/*       */     case -2:
/*  3919 */       return true;
/*       */     }
/*  3921 */     return false;
/*       */   }
/*       */ 
/*       */   private boolean isTemporal(int paramInt)
/*       */   {
/*  3936 */     switch (paramInt) {
/*       */     case 91:
/*       */     case 92:
/*       */     case 93:
/*  3940 */       return true;
/*       */     }
/*  3942 */     return false;
/*       */   }
/*       */ 
/*       */   private boolean isBoolean(int paramInt)
/*       */   {
/*  3957 */     switch (paramInt) {
/*       */     case -7:
/*       */     case 16:
/*  3960 */       return true;
/*       */     }
/*  3962 */     return false;
/*       */   }
/*       */ 
/*       */   private Object convertNumeric(Object paramObject, int paramInt1, int paramInt2)
/*       */     throws SQLException
/*       */   {
/*  3994 */     if (paramInt1 == paramInt2) {
/*  3995 */       return paramObject;
/*       */     }
/*       */ 
/*  3998 */     if ((!isNumeric(paramInt2)) && (!isString(paramInt2))) {
/*  3999 */       throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.dtypemismt").toString() + paramInt2);
/*       */     }
/*       */     try
/*       */     {
/*  4003 */       switch (paramInt2) {
/*       */       case -7:
/*  4005 */         Integer localInteger = Integer.valueOf(paramObject.toString().trim());
/*  4006 */         return localInteger.equals(Integer.valueOf(0)) ? Boolean.valueOf(false) : Boolean.valueOf(true);
/*       */       case -6:
/*  4010 */         return Byte.valueOf(paramObject.toString().trim());
/*       */       case 5:
/*  4012 */         return Short.valueOf(paramObject.toString().trim());
/*       */       case 4:
/*  4014 */         return Integer.valueOf(paramObject.toString().trim());
/*       */       case -5:
/*  4016 */         return Long.valueOf(paramObject.toString().trim());
/*       */       case 2:
/*       */       case 3:
/*  4019 */         return new BigDecimal(paramObject.toString().trim());
/*       */       case 6:
/*       */       case 7:
/*  4022 */         return new Float(paramObject.toString().trim());
/*       */       case 8:
/*  4024 */         return new Double(paramObject.toString().trim());
/*       */       case -1:
/*       */       case 1:
/*       */       case 12:
/*  4028 */         return paramObject.toString();
/*       */       case -4:
/*       */       case -3:
/*       */       case -2:
/*       */       case 0:
/*       */       case 9:
/*       */       case 10:
/*  4030 */       case 11: } throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.dtypemismt").toString() + paramInt2);
/*       */     } catch (NumberFormatException localNumberFormatException) {
/*       */     }
/*  4033 */     throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.dtypemismt").toString() + paramInt2);
/*       */   }
/*       */ 
/*       */   private Object convertTemporal(Object paramObject, int paramInt1, int paramInt2)
/*       */     throws SQLException
/*       */   {
/*  4093 */     if (paramInt1 == paramInt2) {
/*  4094 */       return paramObject;
/*       */     }
/*       */ 
/*  4097 */     if ((isNumeric(paramInt2) == true) || ((!isString(paramInt2)) && (!isTemporal(paramInt2))))
/*       */     {
/*  4099 */       throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.dtypemismt").toString());
/*       */     }
/*       */     try
/*       */     {
/*  4103 */       switch (paramInt2) {
/*       */       case 91:
/*  4105 */         if (paramInt1 == 93) {
/*  4106 */           return new java.sql.Date(((Timestamp)paramObject).getTime());
/*       */         }
/*  4108 */         throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.dtypemismt").toString());
/*       */       case 93:
/*  4111 */         if (paramInt1 == 92) {
/*  4112 */           return new Timestamp(((Time)paramObject).getTime());
/*       */         }
/*  4114 */         return new Timestamp(((java.sql.Date)paramObject).getTime());
/*       */       case 92:
/*  4117 */         if (paramInt1 == 93) {
/*  4118 */           return new Time(((Timestamp)paramObject).getTime());
/*       */         }
/*  4120 */         throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.dtypemismt").toString());
/*       */       case -1:
/*       */       case 1:
/*       */       case 12:
/*  4125 */         return paramObject.toString();
/*       */       }
/*  4127 */       throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.dtypemismt").toString());
/*       */     } catch (NumberFormatException localNumberFormatException) {
/*       */     }
/*  4130 */     throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.dtypemismt").toString());
/*       */   }
/*       */ 
/*       */   private Object convertBoolean(Object paramObject, int paramInt1, int paramInt2)
/*       */     throws SQLException
/*       */   {
/*  4159 */     if (paramInt1 == paramInt2) {
/*  4160 */       return paramObject;
/*       */     }
/*       */ 
/*  4163 */     if ((isNumeric(paramInt2) == true) || ((!isString(paramInt2)) && (!isBoolean(paramInt2))))
/*       */     {
/*  4165 */       throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.dtypemismt").toString());
/*       */     }
/*       */ 
/*       */     try
/*       */     {
/*  4170 */       switch (paramInt2) {
/*       */       case -7:
/*  4172 */         Integer localInteger = Integer.valueOf(paramObject.toString().trim());
/*  4173 */         return localInteger.equals(Integer.valueOf(0)) ? Boolean.valueOf(false) : Boolean.valueOf(true);
/*       */       case 16:
/*  4177 */         return Boolean.valueOf(paramObject.toString().trim());
/*       */       }
/*  4179 */       throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.dtypemismt").toString() + paramInt2);
/*       */     } catch (NumberFormatException localNumberFormatException) {
/*       */     }
/*  4182 */     throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.dtypemismt").toString() + paramInt2);
/*       */   }
/*       */ 
/*       */   public void updateNull(int paramInt)
/*       */     throws SQLException
/*       */   {
/*  4214 */     checkIndex(paramInt);
/*       */ 
/*  4216 */     checkCursor();
/*       */ 
/*  4218 */     BaseRow localBaseRow = getCurrentRow();
/*  4219 */     localBaseRow.setColumnObject(paramInt, null);
/*       */   }
/*       */ 
/*       */   public void updateBoolean(int paramInt, boolean paramBoolean)
/*       */     throws SQLException
/*       */   {
/*  4248 */     checkIndex(paramInt);
/*       */ 
/*  4250 */     checkCursor();
/*  4251 */     Object localObject = convertBoolean(Boolean.valueOf(paramBoolean), -7, this.RowSetMD.getColumnType(paramInt));
/*       */ 
/*  4255 */     getCurrentRow().setColumnObject(paramInt, localObject);
/*       */   }
/*       */ 
/*       */   public void updateByte(int paramInt, byte paramByte)
/*       */     throws SQLException
/*       */   {
/*  4283 */     checkIndex(paramInt);
/*       */ 
/*  4285 */     checkCursor();
/*       */ 
/*  4287 */     Object localObject = convertNumeric(Byte.valueOf(paramByte), -6, this.RowSetMD.getColumnType(paramInt));
/*       */ 
/*  4291 */     getCurrentRow().setColumnObject(paramInt, localObject);
/*       */   }
/*       */ 
/*       */   public void updateShort(int paramInt, short paramShort)
/*       */     throws SQLException
/*       */   {
/*  4319 */     checkIndex(paramInt);
/*       */ 
/*  4321 */     checkCursor();
/*       */ 
/*  4323 */     Object localObject = convertNumeric(Short.valueOf(paramShort), 5, this.RowSetMD.getColumnType(paramInt));
/*       */ 
/*  4327 */     getCurrentRow().setColumnObject(paramInt, localObject);
/*       */   }
/*       */ 
/*       */   public void updateInt(int paramInt1, int paramInt2)
/*       */     throws SQLException
/*       */   {
/*  4355 */     checkIndex(paramInt1);
/*       */ 
/*  4357 */     checkCursor();
/*  4358 */     Object localObject = convertNumeric(Integer.valueOf(paramInt2), 4, this.RowSetMD.getColumnType(paramInt1));
/*       */ 
/*  4362 */     getCurrentRow().setColumnObject(paramInt1, localObject);
/*       */   }
/*       */ 
/*       */   public void updateLong(int paramInt, long paramLong)
/*       */     throws SQLException
/*       */   {
/*  4390 */     checkIndex(paramInt);
/*       */ 
/*  4392 */     checkCursor();
/*       */ 
/*  4394 */     Object localObject = convertNumeric(Long.valueOf(paramLong), -5, this.RowSetMD.getColumnType(paramInt));
/*       */ 
/*  4398 */     getCurrentRow().setColumnObject(paramInt, localObject);
/*       */   }
/*       */ 
/*       */   public void updateFloat(int paramInt, float paramFloat)
/*       */     throws SQLException
/*       */   {
/*  4427 */     checkIndex(paramInt);
/*       */ 
/*  4429 */     checkCursor();
/*       */ 
/*  4431 */     Object localObject = convertNumeric(new Float(paramFloat), 7, this.RowSetMD.getColumnType(paramInt));
/*       */ 
/*  4435 */     getCurrentRow().setColumnObject(paramInt, localObject);
/*       */   }
/*       */ 
/*       */   public void updateDouble(int paramInt, double paramDouble)
/*       */     throws SQLException
/*       */   {
/*  4463 */     checkIndex(paramInt);
/*       */ 
/*  4465 */     checkCursor();
/*  4466 */     Object localObject = convertNumeric(new Double(paramDouble), 8, this.RowSetMD.getColumnType(paramInt));
/*       */ 
/*  4470 */     getCurrentRow().setColumnObject(paramInt, localObject);
/*       */   }
/*       */ 
/*       */   public void updateBigDecimal(int paramInt, BigDecimal paramBigDecimal)
/*       */     throws SQLException
/*       */   {
/*  4498 */     checkIndex(paramInt);
/*       */ 
/*  4500 */     checkCursor();
/*       */ 
/*  4502 */     Object localObject = convertNumeric(paramBigDecimal, 2, this.RowSetMD.getColumnType(paramInt));
/*       */ 
/*  4506 */     getCurrentRow().setColumnObject(paramInt, localObject);
/*       */   }
/*       */ 
/*       */   public void updateString(int paramInt, String paramString)
/*       */     throws SQLException
/*       */   {
/*  4537 */     checkIndex(paramInt);
/*       */ 
/*  4539 */     checkCursor();
/*       */ 
/*  4541 */     getCurrentRow().setColumnObject(paramInt, paramString);
/*       */   }
/*       */ 
/*       */   public void updateBytes(int paramInt, byte[] paramArrayOfByte)
/*       */     throws SQLException
/*       */   {
/*  4569 */     checkIndex(paramInt);
/*       */ 
/*  4571 */     checkCursor();
/*       */ 
/*  4573 */     if (!isBinary(this.RowSetMD.getColumnType(paramInt))) {
/*  4574 */       throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.dtypemismt").toString());
/*       */     }
/*       */ 
/*  4577 */     getCurrentRow().setColumnObject(paramInt, paramArrayOfByte);
/*       */   }
/*       */ 
/*       */   public void updateDate(int paramInt, java.sql.Date paramDate)
/*       */     throws SQLException
/*       */   {
/*  4606 */     checkIndex(paramInt);
/*       */ 
/*  4608 */     checkCursor();
/*       */ 
/*  4610 */     Object localObject = convertTemporal(paramDate, 91, this.RowSetMD.getColumnType(paramInt));
/*       */ 
/*  4614 */     getCurrentRow().setColumnObject(paramInt, localObject);
/*       */   }
/*       */ 
/*       */   public void updateTime(int paramInt, Time paramTime)
/*       */     throws SQLException
/*       */   {
/*  4643 */     checkIndex(paramInt);
/*       */ 
/*  4645 */     checkCursor();
/*       */ 
/*  4647 */     Object localObject = convertTemporal(paramTime, 92, this.RowSetMD.getColumnType(paramInt));
/*       */ 
/*  4651 */     getCurrentRow().setColumnObject(paramInt, localObject);
/*       */   }
/*       */ 
/*       */   public void updateTimestamp(int paramInt, Timestamp paramTimestamp)
/*       */     throws SQLException
/*       */   {
/*  4681 */     checkIndex(paramInt);
/*       */ 
/*  4683 */     checkCursor();
/*       */ 
/*  4685 */     Object localObject = convertTemporal(paramTimestamp, 93, this.RowSetMD.getColumnType(paramInt));
/*       */ 
/*  4689 */     getCurrentRow().setColumnObject(paramInt, localObject);
/*       */   }
/*       */ 
/*       */   public void updateAsciiStream(int paramInt1, InputStream paramInputStream, int paramInt2)
/*       */     throws SQLException
/*       */   {
/*  4715 */     checkIndex(paramInt1);
/*       */ 
/*  4717 */     checkCursor();
/*       */ 
/*  4720 */     if ((!isString(this.RowSetMD.getColumnType(paramInt1))) && (!isBinary(this.RowSetMD.getColumnType(paramInt1))))
/*       */     {
/*  4722 */       throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.dtypemismt").toString());
/*       */     }
/*       */ 
/*  4725 */     byte[] arrayOfByte = new byte[paramInt2];
/*       */     try {
/*  4727 */       int i = 0;
/*       */       do
/*  4729 */         i += paramInputStream.read(arrayOfByte, i, paramInt2 - i);
/*  4730 */       while (i != paramInt2);
/*       */     }
/*       */     catch (IOException localIOException) {
/*  4733 */       throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.asciistream").toString());
/*       */     }
/*  4735 */     String str = new String(arrayOfByte);
/*       */ 
/*  4737 */     getCurrentRow().setColumnObject(paramInt1, str);
/*       */   }
/*       */ 
/*       */   public void updateBinaryStream(int paramInt1, InputStream paramInputStream, int paramInt2)
/*       */     throws SQLException
/*       */   {
/*  4769 */     checkIndex(paramInt1);
/*       */ 
/*  4771 */     checkCursor();
/*       */ 
/*  4773 */     if (!isBinary(this.RowSetMD.getColumnType(paramInt1))) {
/*  4774 */       throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.dtypemismt").toString());
/*       */     }
/*       */ 
/*  4777 */     byte[] arrayOfByte = new byte[paramInt2];
/*       */     try {
/*  4779 */       int i = 0;
/*       */       do
/*  4781 */         i += paramInputStream.read(arrayOfByte, i, paramInt2 - i);
/*  4782 */       while (i != -1);
/*       */     } catch (IOException localIOException) {
/*  4784 */       throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.binstream").toString());
/*       */     }
/*       */ 
/*  4787 */     getCurrentRow().setColumnObject(paramInt1, arrayOfByte);
/*       */   }
/*       */ 
/*       */   public void updateCharacterStream(int paramInt1, Reader paramReader, int paramInt2)
/*       */     throws SQLException
/*       */   {
/*  4820 */     checkIndex(paramInt1);
/*       */ 
/*  4822 */     checkCursor();
/*       */ 
/*  4824 */     if ((!isString(this.RowSetMD.getColumnType(paramInt1))) && (!isBinary(this.RowSetMD.getColumnType(paramInt1))))
/*       */     {
/*  4826 */       throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.dtypemismt").toString());
/*       */     }
/*       */ 
/*  4829 */     char[] arrayOfChar = new char[paramInt2];
/*       */     try {
/*  4831 */       int i = 0;
/*       */       do
/*  4833 */         i += paramReader.read(arrayOfChar, i, paramInt2 - i);
/*  4834 */       while (i != paramInt2);
/*       */     }
/*       */     catch (IOException localIOException) {
/*  4837 */       throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.binstream").toString());
/*       */     }
/*  4839 */     String str = new String(arrayOfChar);
/*       */ 
/*  4841 */     getCurrentRow().setColumnObject(paramInt1, str);
/*       */   }
/*       */ 
/*       */   public void updateObject(int paramInt1, Object paramObject, int paramInt2)
/*       */     throws SQLException
/*       */   {
/*  4874 */     checkIndex(paramInt1);
/*       */ 
/*  4876 */     checkCursor();
/*       */ 
/*  4878 */     int i = this.RowSetMD.getColumnType(paramInt1);
/*  4879 */     if ((i == 3) || (i == 2)) {
/*  4880 */       ((BigDecimal)paramObject).setScale(paramInt2);
/*       */     }
/*  4882 */     getCurrentRow().setColumnObject(paramInt1, paramObject);
/*       */   }
/*       */ 
/*       */   public void updateObject(int paramInt, Object paramObject)
/*       */     throws SQLException
/*       */   {
/*  4910 */     checkIndex(paramInt);
/*       */ 
/*  4912 */     checkCursor();
/*       */ 
/*  4914 */     getCurrentRow().setColumnObject(paramInt, paramObject);
/*       */   }
/*       */ 
/*       */   public void updateNull(String paramString)
/*       */     throws SQLException
/*       */   {
/*  4938 */     updateNull(getColIdxByName(paramString));
/*       */   }
/*       */ 
/*       */   public void updateBoolean(String paramString, boolean paramBoolean)
/*       */     throws SQLException
/*       */   {
/*  4964 */     updateBoolean(getColIdxByName(paramString), paramBoolean);
/*       */   }
/*       */ 
/*       */   public void updateByte(String paramString, byte paramByte)
/*       */     throws SQLException
/*       */   {
/*  4990 */     updateByte(getColIdxByName(paramString), paramByte);
/*       */   }
/*       */ 
/*       */   public void updateShort(String paramString, short paramShort)
/*       */     throws SQLException
/*       */   {
/*  5016 */     updateShort(getColIdxByName(paramString), paramShort);
/*       */   }
/*       */ 
/*       */   public void updateInt(String paramString, int paramInt)
/*       */     throws SQLException
/*       */   {
/*  5042 */     updateInt(getColIdxByName(paramString), paramInt);
/*       */   }
/*       */ 
/*       */   public void updateLong(String paramString, long paramLong)
/*       */     throws SQLException
/*       */   {
/*  5068 */     updateLong(getColIdxByName(paramString), paramLong);
/*       */   }
/*       */ 
/*       */   public void updateFloat(String paramString, float paramFloat)
/*       */     throws SQLException
/*       */   {
/*  5094 */     updateFloat(getColIdxByName(paramString), paramFloat);
/*       */   }
/*       */ 
/*       */   public void updateDouble(String paramString, double paramDouble)
/*       */     throws SQLException
/*       */   {
/*  5120 */     updateDouble(getColIdxByName(paramString), paramDouble);
/*       */   }
/*       */ 
/*       */   public void updateBigDecimal(String paramString, BigDecimal paramBigDecimal)
/*       */     throws SQLException
/*       */   {
/*  5146 */     updateBigDecimal(getColIdxByName(paramString), paramBigDecimal);
/*       */   }
/*       */ 
/*       */   public void updateString(String paramString1, String paramString2)
/*       */     throws SQLException
/*       */   {
/*  5172 */     updateString(getColIdxByName(paramString1), paramString2);
/*       */   }
/*       */ 
/*       */   public void updateBytes(String paramString, byte[] paramArrayOfByte)
/*       */     throws SQLException
/*       */   {
/*  5198 */     updateBytes(getColIdxByName(paramString), paramArrayOfByte);
/*       */   }
/*       */ 
/*       */   public void updateDate(String paramString, java.sql.Date paramDate)
/*       */     throws SQLException
/*       */   {
/*  5226 */     updateDate(getColIdxByName(paramString), paramDate);
/*       */   }
/*       */ 
/*       */   public void updateTime(String paramString, Time paramTime)
/*       */     throws SQLException
/*       */   {
/*  5254 */     updateTime(getColIdxByName(paramString), paramTime);
/*       */   }
/*       */ 
/*       */   public void updateTimestamp(String paramString, Timestamp paramTimestamp)
/*       */     throws SQLException
/*       */   {
/*  5285 */     updateTimestamp(getColIdxByName(paramString), paramTimestamp);
/*       */   }
/*       */ 
/*       */   public void updateAsciiStream(String paramString, InputStream paramInputStream, int paramInt)
/*       */     throws SQLException
/*       */   {
/*  5310 */     updateAsciiStream(getColIdxByName(paramString), paramInputStream, paramInt);
/*       */   }
/*       */ 
/*       */   public void updateBinaryStream(String paramString, InputStream paramInputStream, int paramInt)
/*       */     throws SQLException
/*       */   {
/*  5340 */     updateBinaryStream(getColIdxByName(paramString), paramInputStream, paramInt);
/*       */   }
/*       */ 
/*       */   public void updateCharacterStream(String paramString, Reader paramReader, int paramInt)
/*       */     throws SQLException
/*       */   {
/*  5373 */     updateCharacterStream(getColIdxByName(paramString), paramReader, paramInt);
/*       */   }
/*       */ 
/*       */   public void updateObject(String paramString, Object paramObject, int paramInt)
/*       */     throws SQLException
/*       */   {
/*  5404 */     updateObject(getColIdxByName(paramString), paramObject, paramInt);
/*       */   }
/*       */ 
/*       */   public void updateObject(String paramString, Object paramObject)
/*       */     throws SQLException
/*       */   {
/*  5430 */     updateObject(getColIdxByName(paramString), paramObject);
/*       */   }
/*       */ 
/*       */   public void insertRow()
/*       */     throws SQLException
/*       */   {
/*  5451 */     if ((!this.onInsertRow) || (!this.insertRow.isCompleteRow(this.RowSetMD)))
/*       */     {
/*  5453 */       throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.failedins").toString());
/*       */     }
/*       */ 
/*  5458 */     Object[] arrayOfObject = getParams();
/*       */ 
/*  5460 */     for (int j = 0; j < arrayOfObject.length; j++) {
/*  5461 */       this.insertRow.setColumnObject(j + 1, arrayOfObject[j]);
/*       */     }
/*       */ 
/*  5464 */     Row localRow = new Row(this.RowSetMD.getColumnCount(), this.insertRow.getOrigRow());
/*       */ 
/*  5466 */     localRow.setInserted();
/*       */     int i;
/*  5474 */     if ((this.currentRow >= this.numRows) || (this.currentRow < 0))
/*  5475 */       i = this.numRows;
/*       */     else {
/*  5477 */       i = this.currentRow;
/*       */     }
/*       */ 
/*  5480 */     this.rvh.add(i, localRow);
/*  5481 */     this.numRows += 1;
/*       */ 
/*  5483 */     notifyRowChanged();
/*       */   }
/*       */ 
/*       */   public void updateRow()
/*       */     throws SQLException
/*       */   {
/*  5501 */     if (this.onInsertRow == true) {
/*  5502 */       throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.updateins").toString());
/*       */     }
/*       */ 
/*  5505 */     ((Row)getCurrentRow()).setUpdated();
/*       */ 
/*  5508 */     notifyRowChanged();
/*       */   }
/*       */ 
/*       */   public void deleteRow()
/*       */     throws SQLException
/*       */   {
/*  5528 */     checkCursor();
/*       */ 
/*  5530 */     ((Row)getCurrentRow()).setDeleted();
/*  5531 */     this.numDeleted += 1;
/*       */ 
/*  5534 */     notifyRowChanged();
/*       */   }
/*       */ 
/*       */   public void refreshRow()
/*       */     throws SQLException
/*       */   {
/*  5549 */     checkCursor();
/*       */ 
/*  5552 */     if (this.onInsertRow == true) {
/*  5553 */       throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.invalidcp").toString());
/*       */     }
/*       */ 
/*  5556 */     Row localRow = (Row)getCurrentRow();
/*       */ 
/*  5558 */     localRow.clearUpdated();
/*       */   }
/*       */ 
/*       */   public void cancelRowUpdates()
/*       */     throws SQLException
/*       */   {
/*  5576 */     checkCursor();
/*       */ 
/*  5579 */     if (this.onInsertRow == true) {
/*  5580 */       throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.invalidcp").toString());
/*       */     }
/*       */ 
/*  5583 */     Row localRow = (Row)getCurrentRow();
/*  5584 */     if (localRow.getUpdated() == true) {
/*  5585 */       localRow.clearUpdated();
/*  5586 */       notifyRowChanged();
/*       */     }
/*       */   }
/*       */ 
/*       */   public void moveToInsertRow()
/*       */     throws SQLException
/*       */   {
/*  5617 */     if (getConcurrency() == 1007) {
/*  5618 */       throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.movetoins").toString());
/*       */     }
/*  5620 */     if (this.insertRow == null) {
/*  5621 */       if (this.RowSetMD == null)
/*  5622 */         throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.movetoins1").toString());
/*  5623 */       int i = this.RowSetMD.getColumnCount();
/*  5624 */       if (i > 0)
/*  5625 */         this.insertRow = new InsertRow(i);
/*       */       else {
/*  5627 */         throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.movetoins2").toString());
/*       */       }
/*       */     }
/*  5630 */     this.onInsertRow = true;
/*       */ 
/*  5633 */     this.currentRow = this.cursorPos;
/*  5634 */     this.cursorPos = -1;
/*       */ 
/*  5636 */     this.insertRow.initInsertRow();
/*       */   }
/*       */ 
/*       */   public void moveToCurrentRow()
/*       */     throws SQLException
/*       */   {
/*  5650 */     if (!this.onInsertRow) {
/*  5651 */       return;
/*       */     }
/*  5653 */     this.cursorPos = this.currentRow;
/*  5654 */     this.onInsertRow = false;
/*       */   }
/*       */ 
/*       */   public Statement getStatement()
/*       */     throws SQLException
/*       */   {
/*  5665 */     return null;
/*       */   }
/*       */ 
/*       */   public Object getObject(int paramInt, Map<String, Class<?>> paramMap)
/*       */     throws SQLException
/*       */   {
/*  5693 */     checkIndex(paramInt);
/*       */ 
/*  5695 */     checkCursor();
/*       */ 
/*  5697 */     setLastValueNull(false);
/*  5698 */     Object localObject = getCurrentRow().getColumnObject(paramInt);
/*       */ 
/*  5701 */     if (localObject == null) {
/*  5702 */       setLastValueNull(true);
/*  5703 */       return null;
/*       */     }
/*  5705 */     if ((localObject instanceof Struct)) {
/*  5706 */       Struct localStruct = (Struct)localObject;
/*       */ 
/*  5709 */       Class localClass = (Class)paramMap.get(localStruct.getSQLTypeName());
/*  5710 */       if (localClass != null)
/*       */       {
/*  5712 */         SQLData localSQLData = null;
/*       */         try {
/*  5714 */           localSQLData = (SQLData)ReflectUtil.newInstance(localClass);
/*       */         } catch (Exception localException) {
/*  5716 */           throw new SQLException("Unable to Instantiate: ", localException);
/*       */         }
/*       */ 
/*  5719 */         Object[] arrayOfObject = localStruct.getAttributes(paramMap);
/*       */ 
/*  5721 */         SQLInputImpl localSQLInputImpl = new SQLInputImpl(arrayOfObject, paramMap);
/*       */ 
/*  5723 */         localSQLData.readSQL(localSQLInputImpl, localStruct.getSQLTypeName());
/*  5724 */         return localSQLData;
/*       */       }
/*       */     }
/*  5727 */     return localObject;
/*       */   }
/*       */ 
/*       */   public Ref getRef(int paramInt)
/*       */     throws SQLException
/*       */   {
/*  5749 */     checkIndex(paramInt);
/*       */ 
/*  5751 */     checkCursor();
/*       */ 
/*  5753 */     if (this.RowSetMD.getColumnType(paramInt) != 2006) {
/*  5754 */       throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.dtypemismt").toString());
/*       */     }
/*       */ 
/*  5757 */     setLastValueNull(false);
/*  5758 */     Ref localRef = (Ref)getCurrentRow().getColumnObject(paramInt);
/*       */ 
/*  5761 */     if (localRef == null) {
/*  5762 */       setLastValueNull(true);
/*  5763 */       return null;
/*       */     }
/*       */ 
/*  5766 */     return localRef;
/*       */   }
/*       */ 
/*       */   public Blob getBlob(int paramInt)
/*       */     throws SQLException
/*       */   {
/*  5788 */     checkIndex(paramInt);
/*       */ 
/*  5790 */     checkCursor();
/*       */ 
/*  5792 */     if (this.RowSetMD.getColumnType(paramInt) != 2004) {
/*  5793 */       System.out.println(MessageFormat.format(this.resBundle.handleGetObject("cachedrowsetimpl.type").toString(), new Object[] { Integer.valueOf(this.RowSetMD.getColumnType(paramInt)) }));
/*  5794 */       throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.dtypemismt").toString());
/*       */     }
/*       */ 
/*  5797 */     setLastValueNull(false);
/*  5798 */     Blob localBlob = (Blob)getCurrentRow().getColumnObject(paramInt);
/*       */ 
/*  5801 */     if (localBlob == null) {
/*  5802 */       setLastValueNull(true);
/*  5803 */       return null;
/*       */     }
/*       */ 
/*  5806 */     return localBlob;
/*       */   }
/*       */ 
/*       */   public Clob getClob(int paramInt)
/*       */     throws SQLException
/*       */   {
/*  5828 */     checkIndex(paramInt);
/*       */ 
/*  5830 */     checkCursor();
/*       */ 
/*  5832 */     if (this.RowSetMD.getColumnType(paramInt) != 2005) {
/*  5833 */       System.out.println(MessageFormat.format(this.resBundle.handleGetObject("cachedrowsetimpl.type").toString(), new Object[] { Integer.valueOf(this.RowSetMD.getColumnType(paramInt)) }));
/*  5834 */       throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.dtypemismt").toString());
/*       */     }
/*       */ 
/*  5837 */     setLastValueNull(false);
/*  5838 */     Clob localClob = (Clob)getCurrentRow().getColumnObject(paramInt);
/*       */ 
/*  5841 */     if (localClob == null) {
/*  5842 */       setLastValueNull(true);
/*  5843 */       return null;
/*       */     }
/*       */ 
/*  5846 */     return localClob;
/*       */   }
/*       */ 
/*       */   public Array getArray(int paramInt)
/*       */     throws SQLException
/*       */   {
/*  5869 */     checkIndex(paramInt);
/*       */ 
/*  5871 */     checkCursor();
/*       */ 
/*  5873 */     if (this.RowSetMD.getColumnType(paramInt) != 2003) {
/*  5874 */       throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.dtypemismt").toString());
/*       */     }
/*       */ 
/*  5877 */     setLastValueNull(false);
/*  5878 */     Array localArray = (Array)getCurrentRow().getColumnObject(paramInt);
/*       */ 
/*  5881 */     if (localArray == null) {
/*  5882 */       setLastValueNull(true);
/*  5883 */       return null;
/*       */     }
/*       */ 
/*  5886 */     return localArray;
/*       */   }
/*       */ 
/*       */   public Object getObject(String paramString, Map<String, Class<?>> paramMap)
/*       */     throws SQLException
/*       */   {
/*  5909 */     return getObject(getColIdxByName(paramString), paramMap);
/*       */   }
/*       */ 
/*       */   public Ref getRef(String paramString)
/*       */     throws SQLException
/*       */   {
/*  5927 */     return getRef(getColIdxByName(paramString));
/*       */   }
/*       */ 
/*       */   public Blob getBlob(String paramString)
/*       */     throws SQLException
/*       */   {
/*  5945 */     return getBlob(getColIdxByName(paramString));
/*       */   }
/*       */ 
/*       */   public Clob getClob(String paramString)
/*       */     throws SQLException
/*       */   {
/*  5964 */     return getClob(getColIdxByName(paramString));
/*       */   }
/*       */ 
/*       */   public Array getArray(String paramString)
/*       */     throws SQLException
/*       */   {
/*  5983 */     return getArray(getColIdxByName(paramString));
/*       */   }
/*       */ 
/*       */   public java.sql.Date getDate(int paramInt, Calendar paramCalendar)
/*       */     throws SQLException
/*       */   {
/*  6009 */     checkIndex(paramInt);
/*       */ 
/*  6011 */     checkCursor();
/*       */ 
/*  6013 */     setLastValueNull(false);
/*  6014 */     Object localObject = getCurrentRow().getColumnObject(paramInt);
/*       */ 
/*  6017 */     if (localObject == null) {
/*  6018 */       setLastValueNull(true);
/*  6019 */       return null;
/*       */     }
/*       */ 
/*  6022 */     localObject = convertTemporal(localObject, this.RowSetMD.getColumnType(paramInt), 91);
/*       */ 
/*  6027 */     Calendar localCalendar = Calendar.getInstance();
/*       */ 
/*  6029 */     localCalendar.setTime((java.util.Date)localObject);
/*       */ 
/*  6036 */     paramCalendar.set(1, localCalendar.get(1));
/*  6037 */     paramCalendar.set(2, localCalendar.get(2));
/*  6038 */     paramCalendar.set(5, localCalendar.get(5));
/*       */ 
/*  6044 */     return new java.sql.Date(paramCalendar.getTime().getTime());
/*       */   }
/*       */ 
/*       */   public java.sql.Date getDate(String paramString, Calendar paramCalendar)
/*       */     throws SQLException
/*       */   {
/*  6066 */     return getDate(getColIdxByName(paramString), paramCalendar);
/*       */   }
/*       */ 
/*       */   public Time getTime(int paramInt, Calendar paramCalendar)
/*       */     throws SQLException
/*       */   {
/*  6092 */     checkIndex(paramInt);
/*       */ 
/*  6094 */     checkCursor();
/*       */ 
/*  6096 */     setLastValueNull(false);
/*  6097 */     Object localObject = getCurrentRow().getColumnObject(paramInt);
/*       */ 
/*  6100 */     if (localObject == null) {
/*  6101 */       setLastValueNull(true);
/*  6102 */       return null;
/*       */     }
/*       */ 
/*  6105 */     localObject = convertTemporal(localObject, this.RowSetMD.getColumnType(paramInt), 92);
/*       */ 
/*  6110 */     Calendar localCalendar = Calendar.getInstance();
/*       */ 
/*  6112 */     localCalendar.setTime((java.util.Date)localObject);
/*       */ 
/*  6119 */     paramCalendar.set(11, localCalendar.get(11));
/*  6120 */     paramCalendar.set(12, localCalendar.get(12));
/*  6121 */     paramCalendar.set(13, localCalendar.get(13));
/*       */ 
/*  6123 */     return new Time(paramCalendar.getTime().getTime());
/*       */   }
/*       */ 
/*       */   public Time getTime(String paramString, Calendar paramCalendar)
/*       */     throws SQLException
/*       */   {
/*  6145 */     return getTime(getColIdxByName(paramString), paramCalendar);
/*       */   }
/*       */ 
/*       */   public Timestamp getTimestamp(int paramInt, Calendar paramCalendar)
/*       */     throws SQLException
/*       */   {
/*  6171 */     checkIndex(paramInt);
/*       */ 
/*  6173 */     checkCursor();
/*       */ 
/*  6175 */     setLastValueNull(false);
/*  6176 */     Object localObject = getCurrentRow().getColumnObject(paramInt);
/*       */ 
/*  6179 */     if (localObject == null) {
/*  6180 */       setLastValueNull(true);
/*  6181 */       return null;
/*       */     }
/*       */ 
/*  6184 */     localObject = convertTemporal(localObject, this.RowSetMD.getColumnType(paramInt), 93);
/*       */ 
/*  6189 */     Calendar localCalendar = Calendar.getInstance();
/*       */ 
/*  6191 */     localCalendar.setTime((java.util.Date)localObject);
/*       */ 
/*  6198 */     paramCalendar.set(1, localCalendar.get(1));
/*  6199 */     paramCalendar.set(2, localCalendar.get(2));
/*  6200 */     paramCalendar.set(5, localCalendar.get(5));
/*  6201 */     paramCalendar.set(11, localCalendar.get(11));
/*  6202 */     paramCalendar.set(12, localCalendar.get(12));
/*  6203 */     paramCalendar.set(13, localCalendar.get(13));
/*       */ 
/*  6205 */     return new Timestamp(paramCalendar.getTime().getTime());
/*       */   }
/*       */ 
/*       */   public Timestamp getTimestamp(String paramString, Calendar paramCalendar)
/*       */     throws SQLException
/*       */   {
/*  6228 */     return getTimestamp(getColIdxByName(paramString), paramCalendar);
/*       */   }
/*       */ 
/*       */   public Connection getConnection()
/*       */     throws SQLException
/*       */   {
/*  6246 */     return this.conn;
/*       */   }
/*       */ 
/*       */   public void setMetaData(RowSetMetaData paramRowSetMetaData)
/*       */     throws SQLException
/*       */   {
/*  6259 */     this.RowSetMD = ((RowSetMetaDataImpl)paramRowSetMetaData);
/*       */   }
/*       */ 
/*       */   public ResultSet getOriginal()
/*       */     throws SQLException
/*       */   {
/*  6277 */     CachedRowSetImpl localCachedRowSetImpl = new CachedRowSetImpl();
/*  6278 */     localCachedRowSetImpl.RowSetMD = this.RowSetMD;
/*  6279 */     localCachedRowSetImpl.numRows = this.numRows;
/*  6280 */     localCachedRowSetImpl.cursorPos = 0;
/*       */ 
/*  6286 */     int i = this.RowSetMD.getColumnCount();
/*       */ 
/*  6289 */     for (Iterator localIterator = this.rvh.iterator(); localIterator.hasNext(); ) {
/*  6290 */       Row localRow = new Row(i, ((Row)localIterator.next()).getOrigRow());
/*  6291 */       localCachedRowSetImpl.rvh.add(localRow);
/*       */     }
/*  6293 */     return localCachedRowSetImpl;
/*       */   }
/*       */ 
/*       */   public ResultSet getOriginalRow()
/*       */     throws SQLException
/*       */   {
/*  6308 */     CachedRowSetImpl localCachedRowSetImpl = new CachedRowSetImpl();
/*  6309 */     localCachedRowSetImpl.RowSetMD = this.RowSetMD;
/*  6310 */     localCachedRowSetImpl.numRows = 1;
/*  6311 */     localCachedRowSetImpl.cursorPos = 0;
/*  6312 */     localCachedRowSetImpl.setTypeMap(getTypeMap());
/*       */ 
/*  6319 */     Row localRow = new Row(this.RowSetMD.getColumnCount(), getCurrentRow().getOrigRow());
/*       */ 
/*  6322 */     localCachedRowSetImpl.rvh.add(localRow);
/*       */ 
/*  6324 */     return localCachedRowSetImpl;
/*       */   }
/*       */ 
/*       */   public void setOriginalRow()
/*       */     throws SQLException
/*       */   {
/*  6335 */     if (this.onInsertRow == true) {
/*  6336 */       throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.invalidop").toString());
/*       */     }
/*       */ 
/*  6339 */     Row localRow = (Row)getCurrentRow();
/*  6340 */     makeRowOriginal(localRow);
/*       */ 
/*  6343 */     if (localRow.getDeleted() == true)
/*  6344 */       removeCurrentRow();
/*       */   }
/*       */ 
/*       */   private void makeRowOriginal(Row paramRow)
/*       */   {
/*  6358 */     if (paramRow.getInserted() == true) {
/*  6359 */       paramRow.clearInserted();
/*       */     }
/*       */ 
/*  6362 */     if (paramRow.getUpdated() == true)
/*  6363 */       paramRow.moveCurrentToOrig();
/*       */   }
/*       */ 
/*       */   public void setOriginal()
/*       */     throws SQLException
/*       */   {
/*  6375 */     for (Iterator localIterator = this.rvh.iterator(); localIterator.hasNext(); ) {
/*  6376 */       Row localRow = (Row)localIterator.next();
/*  6377 */       makeRowOriginal(localRow);
/*       */ 
/*  6379 */       if (localRow.getDeleted() == true) {
/*  6380 */         localIterator.remove();
/*  6381 */         this.numRows -= 1;
/*       */       }
/*       */     }
/*  6384 */     this.numDeleted = 0;
/*       */ 
/*  6387 */     notifyRowSetChanged();
/*       */   }
/*       */ 
/*       */   public String getTableName()
/*       */     throws SQLException
/*       */   {
/*  6399 */     return this.tableName;
/*       */   }
/*       */ 
/*       */   public void setTableName(String paramString)
/*       */     throws SQLException
/*       */   {
/*  6412 */     if (paramString == null) {
/*  6413 */       throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.tablename").toString());
/*       */     }
/*  6415 */     this.tableName = paramString;
/*       */   }
/*       */ 
/*       */   public int[] getKeyColumns()
/*       */     throws SQLException
/*       */   {
/*  6430 */     return this.keyCols;
/*       */   }
/*       */ 
/*       */   public void setKeyColumns(int[] paramArrayOfInt)
/*       */     throws SQLException
/*       */   {
/*  6451 */     int i = 0;
/*  6452 */     if (this.RowSetMD != null) {
/*  6453 */       i = this.RowSetMD.getColumnCount();
/*  6454 */       if (paramArrayOfInt.length > i)
/*  6455 */         throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.keycols").toString());
/*       */     }
/*  6457 */     this.keyCols = new int[paramArrayOfInt.length];
/*  6458 */     for (int j = 0; j < paramArrayOfInt.length; j++) {
/*  6459 */       if ((this.RowSetMD != null) && ((paramArrayOfInt[j] <= 0) || (paramArrayOfInt[j] > i)))
/*       */       {
/*  6461 */         throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.invalidcol").toString() + paramArrayOfInt[j]);
/*       */       }
/*       */ 
/*  6464 */       this.keyCols[j] = paramArrayOfInt[j];
/*       */     }
/*       */   }
/*       */ 
/*       */   public void updateRef(int paramInt, Ref paramRef)
/*       */     throws SQLException
/*       */   {
/*  6493 */     checkIndex(paramInt);
/*       */ 
/*  6495 */     checkCursor();
/*       */ 
/*  6500 */     getCurrentRow().setColumnObject(paramInt, new SerialRef(paramRef));
/*       */   }
/*       */ 
/*       */   public void updateRef(String paramString, Ref paramRef)
/*       */     throws SQLException
/*       */   {
/*  6526 */     updateRef(getColIdxByName(paramString), paramRef);
/*       */   }
/*       */ 
/*       */   public void updateClob(int paramInt, Clob paramClob)
/*       */     throws SQLException
/*       */   {
/*  6554 */     checkIndex(paramInt);
/*       */ 
/*  6556 */     checkCursor();
/*       */ 
/*  6562 */     if (this.dbmslocatorsUpdateCopy) {
/*  6563 */       getCurrentRow().setColumnObject(paramInt, new SerialClob(paramClob));
/*       */     }
/*       */     else
/*  6566 */       throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.opnotsupp").toString());
/*       */   }
/*       */ 
/*       */   public void updateClob(String paramString, Clob paramClob)
/*       */     throws SQLException
/*       */   {
/*  6593 */     updateClob(getColIdxByName(paramString), paramClob);
/*       */   }
/*       */ 
/*       */   public void updateBlob(int paramInt, Blob paramBlob)
/*       */     throws SQLException
/*       */   {
/*  6621 */     checkIndex(paramInt);
/*       */ 
/*  6623 */     checkCursor();
/*       */ 
/*  6629 */     if (this.dbmslocatorsUpdateCopy) {
/*  6630 */       getCurrentRow().setColumnObject(paramInt, new SerialBlob(paramBlob));
/*       */     }
/*       */     else
/*  6633 */       throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.opnotsupp").toString());
/*       */   }
/*       */ 
/*       */   public void updateBlob(String paramString, Blob paramBlob)
/*       */     throws SQLException
/*       */   {
/*  6660 */     updateBlob(getColIdxByName(paramString), paramBlob);
/*       */   }
/*       */ 
/*       */   public void updateArray(int paramInt, Array paramArray)
/*       */     throws SQLException
/*       */   {
/*  6688 */     checkIndex(paramInt);
/*       */ 
/*  6690 */     checkCursor();
/*       */ 
/*  6695 */     getCurrentRow().setColumnObject(paramInt, new SerialArray(paramArray));
/*       */   }
/*       */ 
/*       */   public void updateArray(String paramString, Array paramArray)
/*       */     throws SQLException
/*       */   {
/*  6721 */     updateArray(getColIdxByName(paramString), paramArray);
/*       */   }
/*       */ 
/*       */   public URL getURL(int paramInt)
/*       */     throws SQLException
/*       */   {
/*  6744 */     checkIndex(paramInt);
/*       */ 
/*  6746 */     checkCursor();
/*       */ 
/*  6748 */     if (this.RowSetMD.getColumnType(paramInt) != 70) {
/*  6749 */       throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.dtypemismt").toString());
/*       */     }
/*       */ 
/*  6752 */     setLastValueNull(false);
/*  6753 */     URL localURL = (URL)getCurrentRow().getColumnObject(paramInt);
/*       */ 
/*  6756 */     if (localURL == null) {
/*  6757 */       setLastValueNull(true);
/*  6758 */       return null;
/*       */     }
/*       */ 
/*  6761 */     return localURL;
/*       */   }
/*       */ 
/*       */   public URL getURL(String paramString)
/*       */     throws SQLException
/*       */   {
/*  6779 */     return getURL(getColIdxByName(paramString));
/*       */   }
/*       */ 
/*       */   public RowSetWarning getRowSetWarnings()
/*       */   {
/*       */     try
/*       */     {
/*  6804 */       notifyCursorMoved(); } catch (SQLException localSQLException) {
/*       */     }
/*  6806 */     return this.rowsetWarning;
/*       */   }
/*       */ 
/*       */   private String buildTableName(String paramString)
/*       */     throws SQLException
/*       */   {
/*  6826 */     Object localObject1 = "";
/*  6827 */     paramString = paramString.trim();
/*       */ 
/*  6831 */     if (paramString.toLowerCase().startsWith("select"))
/*       */     {
/*  6836 */       int i = paramString.toLowerCase().indexOf("from");
/*  6837 */       int j = paramString.indexOf(",", i);
/*       */ 
/*  6839 */       if (j == -1)
/*       */       {
/*  6841 */         localObject1 = paramString.substring(i + "from".length(), paramString.length()).trim();
/*       */ 
/*  6843 */         Object localObject2 = localObject1;
/*       */ 
/*  6845 */         int k = ((String)localObject2).toLowerCase().indexOf("where");
/*       */ 
/*  6852 */         if (k != -1)
/*       */         {
/*  6854 */           localObject2 = ((String)localObject2).substring(0, k).trim();
/*       */         }
/*       */ 
/*  6857 */         localObject1 = localObject2;
/*       */       }
/*       */ 
/*       */     }
/*  6863 */     else if (!paramString.toLowerCase().startsWith("insert"))
/*       */     {
/*  6865 */       if (!paramString.toLowerCase().startsWith("update"));
/*       */     }
/*       */ 
/*  6868 */     return localObject1;
/*       */   }
/*       */ 
/*       */   public void commit()
/*       */     throws SQLException
/*       */   {
/*  6878 */     this.conn.commit();
/*       */   }
/*       */ 
/*       */   public void rollback()
/*       */     throws SQLException
/*       */   {
/*  6888 */     this.conn.rollback();
/*       */   }
/*       */ 
/*       */   public void rollback(Savepoint paramSavepoint)
/*       */     throws SQLException
/*       */   {
/*  6898 */     this.conn.rollback(paramSavepoint);
/*       */   }
/*       */ 
/*       */   public void unsetMatchColumn(int[] paramArrayOfInt)
/*       */     throws SQLException
/*       */   {
/*  6918 */     for (int j = 0; j < paramArrayOfInt.length; j++) {
/*  6919 */       int i = Integer.parseInt(((Integer)this.iMatchColumns.get(j)).toString());
/*  6920 */       if (paramArrayOfInt[j] != i) {
/*  6921 */         throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.matchcols").toString());
/*       */       }
/*       */     }
/*       */ 
/*  6925 */     for (j = 0; j < paramArrayOfInt.length; j++)
/*  6926 */       this.iMatchColumns.set(j, Integer.valueOf(-1));
/*       */   }
/*       */ 
/*       */   public void unsetMatchColumn(String[] paramArrayOfString)
/*       */     throws SQLException
/*       */   {
/*  6946 */     for (int i = 0; i < paramArrayOfString.length; i++) {
/*  6947 */       if (!paramArrayOfString[i].equals(this.strMatchColumns.get(i))) {
/*  6948 */         throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.matchcols").toString());
/*       */       }
/*       */     }
/*       */ 
/*  6952 */     for (i = 0; i < paramArrayOfString.length; i++)
/*  6953 */       this.strMatchColumns.set(i, null);
/*       */   }
/*       */ 
/*       */   public String[] getMatchColumnNames()
/*       */     throws SQLException
/*       */   {
/*  6969 */     String[] arrayOfString = new String[this.strMatchColumns.size()];
/*       */ 
/*  6971 */     if (this.strMatchColumns.get(0) == null) {
/*  6972 */       throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.setmatchcols").toString());
/*       */     }
/*       */ 
/*  6975 */     this.strMatchColumns.copyInto(arrayOfString);
/*  6976 */     return arrayOfString;
/*       */   }
/*       */ 
/*       */   public int[] getMatchColumnIndexes()
/*       */     throws SQLException
/*       */   {
/*  6990 */     Integer[] arrayOfInteger = new Integer[this.iMatchColumns.size()];
/*  6991 */     int[] arrayOfInt = new int[this.iMatchColumns.size()];
/*       */ 
/*  6994 */     int i = ((Integer)this.iMatchColumns.get(0)).intValue();
/*       */ 
/*  6996 */     if (i == -1) {
/*  6997 */       throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.setmatchcols").toString());
/*       */     }
/*       */ 
/*  7001 */     this.iMatchColumns.copyInto(arrayOfInteger);
/*       */ 
/*  7003 */     for (int j = 0; j < arrayOfInteger.length; j++) {
/*  7004 */       arrayOfInt[j] = arrayOfInteger[j].intValue();
/*       */     }
/*       */ 
/*  7007 */     return arrayOfInt;
/*       */   }
/*       */ 
/*       */   public void setMatchColumn(int[] paramArrayOfInt)
/*       */     throws SQLException
/*       */   {
/*  7029 */     for (int i = 0; i < paramArrayOfInt.length; i++) {
/*  7030 */       if (paramArrayOfInt[i] < 0) {
/*  7031 */         throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.matchcols1").toString());
/*       */       }
/*       */     }
/*  7034 */     for (i = 0; i < paramArrayOfInt.length; i++)
/*  7035 */       this.iMatchColumns.add(i, Integer.valueOf(paramArrayOfInt[i]));
/*       */   }
/*       */ 
/*       */   public void setMatchColumn(String[] paramArrayOfString)
/*       */     throws SQLException
/*       */   {
/*  7056 */     for (int i = 0; i < paramArrayOfString.length; i++) {
/*  7057 */       if ((paramArrayOfString[i] == null) || (paramArrayOfString[i].equals(""))) {
/*  7058 */         throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.matchcols2").toString());
/*       */       }
/*       */     }
/*  7061 */     for (i = 0; i < paramArrayOfString.length; i++)
/*  7062 */       this.strMatchColumns.add(i, paramArrayOfString[i]);
/*       */   }
/*       */ 
/*       */   public void setMatchColumn(int paramInt)
/*       */     throws SQLException
/*       */   {
/*  7086 */     if (paramInt < 0) {
/*  7087 */       throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.matchcols1").toString());
/*       */     }
/*       */ 
/*  7090 */     this.iMatchColumns.set(0, Integer.valueOf(paramInt));
/*       */   }
/*       */ 
/*       */   public void setMatchColumn(String paramString)
/*       */     throws SQLException
/*       */   {
/*  7112 */     if ((paramString == null) || ((paramString = paramString.trim()).equals(""))) {
/*  7113 */       throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.matchcols2").toString());
/*       */     }
/*       */ 
/*  7116 */     this.strMatchColumns.set(0, paramString);
/*       */   }
/*       */ 
/*       */   public void unsetMatchColumn(int paramInt)
/*       */     throws SQLException
/*       */   {
/*  7137 */     if (!((Integer)this.iMatchColumns.get(0)).equals(Integer.valueOf(paramInt)))
/*  7138 */       throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.unsetmatch").toString());
/*  7139 */     if (this.strMatchColumns.get(0) != null) {
/*  7140 */       throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.unsetmatch1").toString());
/*       */     }
/*       */ 
/*  7143 */     this.iMatchColumns.set(0, Integer.valueOf(-1));
/*       */   }
/*       */ 
/*       */   public void unsetMatchColumn(String paramString)
/*       */     throws SQLException
/*       */   {
/*  7163 */     paramString = paramString.trim();
/*       */ 
/*  7165 */     if (!((String)this.strMatchColumns.get(0)).equals(paramString))
/*  7166 */       throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.unsetmatch").toString());
/*  7167 */     if (((Integer)this.iMatchColumns.get(0)).intValue() > 0) {
/*  7168 */       throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.unsetmatch2").toString());
/*       */     }
/*  7170 */     this.strMatchColumns.set(0, null);
/*       */   }
/*       */ 
/*       */   public void rowSetPopulated(RowSetEvent paramRowSetEvent, int paramInt)
/*       */     throws SQLException
/*       */   {
/*  7189 */     if ((paramInt < 0) || (paramInt < getFetchSize())) {
/*  7190 */       throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.numrows").toString());
/*       */     }
/*       */ 
/*  7193 */     if (size() % paramInt == 0) {
/*  7194 */       RowSetEvent localRowSetEvent = new RowSetEvent(this);
/*  7195 */       paramRowSetEvent = localRowSetEvent;
/*  7196 */       notifyRowSetChanged();
/*       */     }
/*       */   }
/*       */ 
/*       */   public void populate(ResultSet paramResultSet, int paramInt)
/*       */     throws SQLException
/*       */   {
/*  7238 */     Map localMap = getTypeMap();
/*       */ 
/*  7242 */     this.cursorPos = 0;
/*  7243 */     if (this.populatecallcount == 0) {
/*  7244 */       if (paramInt < 0) {
/*  7245 */         throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.startpos").toString());
/*       */       }
/*  7247 */       if (getMaxRows() == 0) {
/*  7248 */         paramResultSet.absolute(paramInt);
/*  7249 */         while (paramResultSet.next()) {
/*  7250 */           this.totalRows += 1;
/*       */         }
/*  7252 */         this.totalRows += 1;
/*       */       }
/*  7254 */       this.startPos = paramInt;
/*       */     }
/*  7256 */     this.populatecallcount += 1;
/*  7257 */     this.resultSet = paramResultSet;
/*  7258 */     if ((this.endPos - this.startPos >= getMaxRows()) && (getMaxRows() > 0)) {
/*  7259 */       this.endPos = this.prevEndPos;
/*  7260 */       this.pagenotend = false;
/*  7261 */       return;
/*       */     }
/*       */ 
/*  7264 */     if (((this.maxRowsreached != getMaxRows()) || (this.maxRowsreached != this.totalRows)) && (this.pagenotend)) {
/*  7265 */       this.startPrev = (paramInt - getPageSize());
/*       */     }
/*       */ 
/*  7268 */     if (this.pageSize == 0) {
/*  7269 */       this.prevEndPos = this.endPos;
/*  7270 */       this.endPos = (paramInt + getMaxRows());
/*       */     }
/*       */     else {
/*  7273 */       this.prevEndPos = this.endPos;
/*  7274 */       this.endPos = (paramInt + getPageSize());
/*       */     }
/*       */ 
/*  7278 */     if (paramInt == 1) {
/*  7279 */       this.resultSet.beforeFirst();
/*       */     }
/*       */     else {
/*  7282 */       this.resultSet.absolute(paramInt - 1);
/*       */     }
/*  7284 */     if (this.pageSize == 0) {
/*  7285 */       this.rvh = new Vector(getMaxRows());
/*       */     }
/*       */     else
/*       */     {
/*  7289 */       this.rvh = new Vector(getPageSize());
/*       */     }
/*       */ 
/*  7292 */     if (paramResultSet == null) {
/*  7293 */       throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.populate").toString());
/*       */     }
/*       */ 
/*  7297 */     this.RSMD = paramResultSet.getMetaData();
/*       */ 
/*  7300 */     this.RowSetMD = new RowSetMetaDataImpl();
/*  7301 */     initMetaData(this.RowSetMD, this.RSMD);
/*       */ 
/*  7304 */     this.RSMD = null;
/*  7305 */     int j = this.RowSetMD.getColumnCount();
/*  7306 */     int m = getMaxRows();
/*  7307 */     int i = 0;
/*  7308 */     Row localRow = null;
/*       */ 
/*  7310 */     if ((!paramResultSet.next()) && (m == 0)) {
/*  7311 */       this.endPos = this.prevEndPos;
/*  7312 */       this.pagenotend = false;
/*  7313 */       return;
/*       */     }
/*       */ 
/*  7316 */     paramResultSet.previous();
/*       */ 
/*  7318 */     while (paramResultSet.next())
/*       */     {
/*  7320 */       localRow = new Row(j);
/*  7321 */       if (this.pageSize == 0) {
/*  7322 */         if ((i >= m) && (m > 0)) {
/*  7323 */           this.rowsetWarning.setNextException(new SQLException("Populating rows setting has exceeded max row setting"));
/*       */ 
/*  7325 */           break;
/*       */         }
/*       */ 
/*       */       }
/*  7329 */       else if ((i >= this.pageSize) || ((this.maxRowsreached >= m) && (m > 0))) {
/*  7330 */         this.rowsetWarning.setNextException(new SQLException("Populating rows setting has exceeded max row setting"));
/*       */ 
/*  7332 */         break;
/*       */       }
/*       */ 
/*  7336 */       for (int k = 1; k <= j; k++)
/*       */       {
/*       */         Object localObject;
/*  7343 */         if (localMap == null)
/*  7344 */           localObject = paramResultSet.getObject(k);
/*       */         else {
/*  7346 */           localObject = paramResultSet.getObject(k, localMap);
/*       */         }
/*       */ 
/*  7353 */         if ((localObject instanceof Struct))
/*  7354 */           localObject = new SerialStruct((Struct)localObject, localMap);
/*  7355 */         else if ((localObject instanceof SQLData))
/*  7356 */           localObject = new SerialStruct((SQLData)localObject, localMap);
/*  7357 */         else if ((localObject instanceof Blob))
/*  7358 */           localObject = new SerialBlob((Blob)localObject);
/*  7359 */         else if ((localObject instanceof Clob))
/*  7360 */           localObject = new SerialClob((Clob)localObject);
/*  7361 */         else if ((localObject instanceof Array)) {
/*  7362 */           localObject = new SerialArray((Array)localObject, localMap);
/*       */         }
/*       */ 
/*  7365 */         localRow.initColumnObject(k, localObject);
/*       */       }
/*  7367 */       i++;
/*  7368 */       this.maxRowsreached += 1;
/*  7369 */       this.rvh.add(localRow);
/*       */     }
/*  7371 */     this.numRows = i;
/*       */ 
/*  7374 */     notifyRowSetChanged();
/*       */   }
/*       */ 
/*       */   public boolean nextPage()
/*       */     throws SQLException
/*       */   {
/*  7387 */     if (this.populatecallcount == 0) {
/*  7388 */       throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.nextpage").toString());
/*       */     }
/*       */ 
/*  7391 */     this.onFirstPage = false;
/*  7392 */     if (this.callWithCon) {
/*  7393 */       this.crsReader.setStartPosition(this.endPos);
/*  7394 */       this.crsReader.readData(this);
/*  7395 */       this.resultSet = null;
/*       */     }
/*       */     else {
/*  7398 */       populate(this.resultSet, this.endPos);
/*       */     }
/*  7400 */     return this.pagenotend;
/*       */   }
/*       */ 
/*       */   public void setPageSize(int paramInt)
/*       */     throws SQLException
/*       */   {
/*  7411 */     if (paramInt < 0) {
/*  7412 */       throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.pagesize").toString());
/*       */     }
/*  7414 */     if ((paramInt > getMaxRows()) && (getMaxRows() != 0)) {
/*  7415 */       throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.pagesize1").toString());
/*       */     }
/*  7417 */     this.pageSize = paramInt;
/*       */   }
/*       */ 
/*       */   public int getPageSize()
/*       */   {
/*  7426 */     return this.pageSize;
/*       */   }
/*       */ 
/*       */   public boolean previousPage()
/*       */     throws SQLException
/*       */   {
/*  7444 */     int i = getPageSize();
/*  7445 */     int j = this.maxRowsreached;
/*       */ 
/*  7447 */     if (this.populatecallcount == 0) {
/*  7448 */       throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.nextpage").toString());
/*       */     }
/*       */ 
/*  7451 */     if ((!this.callWithCon) && 
/*  7452 */       (this.resultSet.getType() == 1003)) {
/*  7453 */       throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.fwdonly").toString());
/*       */     }
/*       */ 
/*  7457 */     this.pagenotend = true;
/*       */ 
/*  7459 */     if (this.startPrev < this.startPos) {
/*  7460 */       this.onFirstPage = true;
/*  7461 */       return false;
/*       */     }
/*       */ 
/*  7464 */     if (this.onFirstPage) {
/*  7465 */       return false;
/*       */     }
/*       */ 
/*  7468 */     int k = j % i;
/*       */ 
/*  7470 */     if (k == 0) {
/*  7471 */       this.maxRowsreached -= 2 * i;
/*  7472 */       if (this.callWithCon) {
/*  7473 */         this.crsReader.setStartPosition(this.startPrev);
/*  7474 */         this.crsReader.readData(this);
/*  7475 */         this.resultSet = null;
/*       */       }
/*       */       else {
/*  7478 */         populate(this.resultSet, this.startPrev);
/*       */       }
/*  7480 */       return true;
/*       */     }
/*       */ 
/*  7484 */     this.maxRowsreached -= i + k;
/*  7485 */     if (this.callWithCon) {
/*  7486 */       this.crsReader.setStartPosition(this.startPrev);
/*  7487 */       this.crsReader.readData(this);
/*  7488 */       this.resultSet = null;
/*       */     }
/*       */     else {
/*  7491 */       populate(this.resultSet, this.startPrev);
/*       */     }
/*  7493 */     return true;
/*       */   }
/*       */ 
/*       */   public void setRowInserted(boolean paramBoolean)
/*       */     throws SQLException
/*       */   {
/*  7685 */     checkCursor();
/*       */ 
/*  7687 */     if (this.onInsertRow == true) {
/*  7688 */       throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.invalidop").toString());
/*       */     }
/*  7690 */     if (paramBoolean)
/*  7691 */       ((Row)getCurrentRow()).setInserted();
/*       */     else
/*  7693 */       ((Row)getCurrentRow()).clearInserted();
/*       */   }
/*       */ 
/*       */   public SQLXML getSQLXML(int paramInt)
/*       */     throws SQLException
/*       */   {
/*  7706 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.opnotysupp").toString());
/*       */   }
/*       */ 
/*       */   public SQLXML getSQLXML(String paramString)
/*       */     throws SQLException
/*       */   {
/*  7717 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.opnotysupp").toString());
/*       */   }
/*       */ 
/*       */   public RowId getRowId(int paramInt)
/*       */     throws SQLException
/*       */   {
/*  7732 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.opnotysupp").toString());
/*       */   }
/*       */ 
/*       */   public RowId getRowId(String paramString)
/*       */     throws SQLException
/*       */   {
/*  7747 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.opnotysupp").toString());
/*       */   }
/*       */ 
/*       */   public void updateRowId(int paramInt, RowId paramRowId)
/*       */     throws SQLException
/*       */   {
/*  7763 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.opnotysupp").toString());
/*       */   }
/*       */ 
/*       */   public void updateRowId(String paramString, RowId paramRowId)
/*       */     throws SQLException
/*       */   {
/*  7779 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.opnotysupp").toString());
/*       */   }
/*       */ 
/*       */   public int getHoldability()
/*       */     throws SQLException
/*       */   {
/*  7789 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.opnotysupp").toString());
/*       */   }
/*       */ 
/*       */   public boolean isClosed()
/*       */     throws SQLException
/*       */   {
/*  7800 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.opnotysupp").toString());
/*       */   }
/*       */ 
/*       */   public void updateNString(int paramInt, String paramString)
/*       */     throws SQLException
/*       */   {
/*  7812 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.opnotysupp").toString());
/*       */   }
/*       */ 
/*       */   public void updateNString(String paramString1, String paramString2)
/*       */     throws SQLException
/*       */   {
/*  7824 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.opnotysupp").toString());
/*       */   }
/*       */ 
/*       */   public void updateNClob(int paramInt, NClob paramNClob)
/*       */     throws SQLException
/*       */   {
/*  7837 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.opnotysupp").toString());
/*       */   }
/*       */ 
/*       */   public void updateNClob(String paramString, NClob paramNClob)
/*       */     throws SQLException
/*       */   {
/*  7849 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.opnotysupp").toString());
/*       */   }
/*       */ 
/*       */   public NClob getNClob(int paramInt)
/*       */     throws SQLException
/*       */   {
/*  7864 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.opnotysupp").toString());
/*       */   }
/*       */ 
/*       */   public NClob getNClob(String paramString)
/*       */     throws SQLException
/*       */   {
/*  7880 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.opnotysupp").toString());
/*       */   }
/*       */ 
/*       */   public <T> T unwrap(Class<T> paramClass) throws SQLException {
/*  7884 */     return null;
/*       */   }
/*       */ 
/*       */   public boolean isWrapperFor(Class<?> paramClass) throws SQLException {
/*  7888 */     return false;
/*       */   }
/*       */ 
/*       */   public void setSQLXML(int paramInt, SQLXML paramSQLXML)
/*       */     throws SQLException
/*       */   {
/*  7901 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.opnotysupp").toString());
/*       */   }
/*       */ 
/*       */   public void setSQLXML(String paramString, SQLXML paramSQLXML)
/*       */     throws SQLException
/*       */   {
/*  7913 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.opnotysupp").toString());
/*       */   }
/*       */ 
/*       */   public void setRowId(int paramInt, RowId paramRowId)
/*       */     throws SQLException
/*       */   {
/*  7929 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.opnotysupp").toString());
/*       */   }
/*       */ 
/*       */   public void setRowId(String paramString, RowId paramRowId)
/*       */     throws SQLException
/*       */   {
/*  7944 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.opnotysupp").toString());
/*       */   }
/*       */ 
/*       */   public void setNCharacterStream(int paramInt, Reader paramReader)
/*       */     throws SQLException
/*       */   {
/*  7971 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
/*       */   }
/*       */ 
/*       */   public void setNClob(String paramString, NClob paramNClob)
/*       */     throws SQLException
/*       */   {
/*  7987 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.opnotysupp").toString());
/*       */   }
/*       */ 
/*       */   public Reader getNCharacterStream(int paramInt)
/*       */     throws SQLException
/*       */   {
/*  8007 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.opnotysupp").toString());
/*       */   }
/*       */ 
/*       */   public Reader getNCharacterStream(String paramString)
/*       */     throws SQLException
/*       */   {
/*  8027 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.opnotysupp").toString());
/*       */   }
/*       */ 
/*       */   public void updateSQLXML(int paramInt, SQLXML paramSQLXML)
/*       */     throws SQLException
/*       */   {
/*  8044 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.opnotysupp").toString());
/*       */   }
/*       */ 
/*       */   public void updateSQLXML(String paramString, SQLXML paramSQLXML)
/*       */     throws SQLException
/*       */   {
/*  8061 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.opnotysupp").toString());
/*       */   }
/*       */ 
/*       */   public String getNString(int paramInt)
/*       */     throws SQLException
/*       */   {
/*  8079 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.opnotysupp").toString());
/*       */   }
/*       */ 
/*       */   public String getNString(String paramString)
/*       */     throws SQLException
/*       */   {
/*  8097 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.opnotysupp").toString());
/*       */   }
/*       */ 
/*       */   public void updateNCharacterStream(int paramInt, Reader paramReader, long paramLong)
/*       */     throws SQLException
/*       */   {
/*  8119 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.opnotysupp").toString());
/*       */   }
/*       */ 
/*       */   public void updateNCharacterStream(String paramString, Reader paramReader, long paramLong)
/*       */     throws SQLException
/*       */   {
/*  8141 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.opnotysupp").toString());
/*       */   }
/*       */ 
/*       */   public void updateNCharacterStream(int paramInt, Reader paramReader)
/*       */     throws SQLException
/*       */   {
/*  8171 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
/*       */   }
/*       */ 
/*       */   public void updateNCharacterStream(String paramString, Reader paramReader)
/*       */     throws SQLException
/*       */   {
/*  8203 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
/*       */   }
/*       */ 
/*       */   public void updateBlob(int paramInt, InputStream paramInputStream, long paramLong)
/*       */     throws SQLException
/*       */   {
/*  8238 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
/*       */   }
/*       */ 
/*       */   public void updateBlob(String paramString, InputStream paramInputStream, long paramLong)
/*       */     throws SQLException
/*       */   {
/*  8271 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
/*       */   }
/*       */ 
/*       */   public void updateBlob(int paramInt, InputStream paramInputStream)
/*       */     throws SQLException
/*       */   {
/*  8306 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
/*       */   }
/*       */ 
/*       */   public void updateBlob(String paramString, InputStream paramInputStream)
/*       */     throws SQLException
/*       */   {
/*  8341 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
/*       */   }
/*       */ 
/*       */   public void updateClob(int paramInt, Reader paramReader, long paramLong)
/*       */     throws SQLException
/*       */   {
/*  8373 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
/*       */   }
/*       */ 
/*       */   public void updateClob(String paramString, Reader paramReader, long paramLong)
/*       */     throws SQLException
/*       */   {
/*  8405 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
/*       */   }
/*       */ 
/*       */   public void updateClob(int paramInt, Reader paramReader)
/*       */     throws SQLException
/*       */   {
/*  8439 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
/*       */   }
/*       */ 
/*       */   public void updateClob(String paramString, Reader paramReader)
/*       */     throws SQLException
/*       */   {
/*  8474 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
/*       */   }
/*       */ 
/*       */   public void updateNClob(int paramInt, Reader paramReader, long paramLong)
/*       */     throws SQLException
/*       */   {
/*  8508 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
/*       */   }
/*       */ 
/*       */   public void updateNClob(String paramString, Reader paramReader, long paramLong)
/*       */     throws SQLException
/*       */   {
/*  8542 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
/*       */   }
/*       */ 
/*       */   public void updateNClob(int paramInt, Reader paramReader)
/*       */     throws SQLException
/*       */   {
/*  8578 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
/*       */   }
/*       */ 
/*       */   public void updateNClob(String paramString, Reader paramReader)
/*       */     throws SQLException
/*       */   {
/*  8615 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
/*       */   }
/*       */ 
/*       */   public void updateAsciiStream(int paramInt, InputStream paramInputStream, long paramLong)
/*       */     throws SQLException
/*       */   {
/*       */   }
/*       */ 
/*       */   public void updateBinaryStream(int paramInt, InputStream paramInputStream, long paramLong)
/*       */     throws SQLException
/*       */   {
/*       */   }
/*       */ 
/*       */   public void updateCharacterStream(int paramInt, Reader paramReader, long paramLong)
/*       */     throws SQLException
/*       */   {
/*  8686 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
/*       */   }
/*       */ 
/*       */   public void updateCharacterStream(String paramString, Reader paramReader, long paramLong)
/*       */     throws SQLException
/*       */   {
/*  8712 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
/*       */   }
/*       */ 
/*       */   public void updateAsciiStream(String paramString, InputStream paramInputStream, long paramLong)
/*       */     throws SQLException
/*       */   {
/*       */   }
/*       */ 
/*       */   public void updateBinaryStream(String paramString, InputStream paramInputStream, long paramLong)
/*       */     throws SQLException
/*       */   {
/*       */   }
/*       */ 
/*       */   public void updateBinaryStream(int paramInt, InputStream paramInputStream)
/*       */     throws SQLException
/*       */   {
/*  8782 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
/*       */   }
/*       */ 
/*       */   public void updateBinaryStream(String paramString, InputStream paramInputStream)
/*       */     throws SQLException
/*       */   {
/*  8809 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
/*       */   }
/*       */ 
/*       */   public void updateCharacterStream(int paramInt, Reader paramReader)
/*       */     throws SQLException
/*       */   {
/*  8834 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
/*       */   }
/*       */ 
/*       */   public void updateCharacterStream(String paramString, Reader paramReader)
/*       */     throws SQLException
/*       */   {
/*  8861 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
/*       */   }
/*       */ 
/*       */   public void updateAsciiStream(int paramInt, InputStream paramInputStream)
/*       */     throws SQLException
/*       */   {
/*  8886 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
/*       */   }
/*       */ 
/*       */   public void updateAsciiStream(String paramString, InputStream paramInputStream)
/*       */     throws SQLException
/*       */   {
/*       */   }
/*       */ 
/*       */   public void setURL(int paramInt, URL paramURL)
/*       */     throws SQLException
/*       */   {
/*  8928 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
/*       */   }
/*       */ 
/*       */   public void setNClob(int paramInt, Reader paramReader)
/*       */     throws SQLException
/*       */   {
/*  8956 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
/*       */   }
/*       */ 
/*       */   public void setNClob(String paramString, Reader paramReader, long paramLong)
/*       */     throws SQLException
/*       */   {
/*  8984 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
/*       */   }
/*       */ 
/*       */   public void setNClob(String paramString, Reader paramReader)
/*       */     throws SQLException
/*       */   {
/*  9011 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
/*       */   }
/*       */ 
/*       */   public void setNClob(int paramInt, Reader paramReader, long paramLong)
/*       */     throws SQLException
/*       */   {
/*  9039 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
/*       */   }
/*       */ 
/*       */   public void setNClob(int paramInt, NClob paramNClob)
/*       */     throws SQLException
/*       */   {
/*  9055 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
/*       */   }
/*       */ 
/*       */   public void setNString(int paramInt, String paramString)
/*       */     throws SQLException
/*       */   {
/*  9075 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
/*       */   }
/*       */ 
/*       */   public void setNString(String paramString1, String paramString2)
/*       */     throws SQLException
/*       */   {
/*  9092 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
/*       */   }
/*       */ 
/*       */   public void setNCharacterStream(int paramInt, Reader paramReader, long paramLong)
/*       */     throws SQLException
/*       */   {
/*  9110 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
/*       */   }
/*       */ 
/*       */   public void setNCharacterStream(String paramString, Reader paramReader, long paramLong)
/*       */     throws SQLException
/*       */   {
/*  9129 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
/*       */   }
/*       */ 
/*       */   public void setNCharacterStream(String paramString, Reader paramReader)
/*       */     throws SQLException
/*       */   {
/*  9155 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
/*       */   }
/*       */ 
/*       */   public void setTimestamp(String paramString, Timestamp paramTimestamp, Calendar paramCalendar)
/*       */     throws SQLException
/*       */   {
/*  9181 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
/*       */   }
/*       */ 
/*       */   public void setClob(String paramString, Reader paramReader, long paramLong)
/*       */     throws SQLException
/*       */   {
/*  9207 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
/*       */   }
/*       */ 
/*       */   public void setClob(String paramString, Clob paramClob)
/*       */     throws SQLException
/*       */   {
/*  9225 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
/*       */   }
/*       */ 
/*       */   public void setClob(String paramString, Reader paramReader)
/*       */     throws SQLException
/*       */   {
/*  9251 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
/*       */   }
/*       */ 
/*       */   public void setDate(String paramString, java.sql.Date paramDate)
/*       */     throws SQLException
/*       */   {
/*  9273 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
/*       */   }
/*       */ 
/*       */   public void setDate(String paramString, java.sql.Date paramDate, Calendar paramCalendar)
/*       */     throws SQLException
/*       */   {
/*  9300 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
/*       */   }
/*       */ 
/*       */   public void setTime(String paramString, Time paramTime)
/*       */     throws SQLException
/*       */   {
/*  9320 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
/*       */   }
/*       */ 
/*       */   public void setTime(String paramString, Time paramTime, Calendar paramCalendar)
/*       */     throws SQLException
/*       */   {
/*  9347 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
/*       */   }
/*       */ 
/*       */   public void setClob(int paramInt, Reader paramReader)
/*       */     throws SQLException
/*       */   {
/*  9373 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
/*       */   }
/*       */ 
/*       */   public void setClob(int paramInt, Reader paramReader, long paramLong)
/*       */     throws SQLException
/*       */   {
/*  9397 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
/*       */   }
/*       */ 
/*       */   public void setBlob(int paramInt, InputStream paramInputStream, long paramLong)
/*       */     throws SQLException
/*       */   {
/*  9427 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
/*       */   }
/*       */ 
/*       */   public void setBlob(int paramInt, InputStream paramInputStream)
/*       */     throws SQLException
/*       */   {
/*  9457 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
/*       */   }
/*       */ 
/*       */   public void setBlob(String paramString, InputStream paramInputStream, long paramLong)
/*       */     throws SQLException
/*       */   {
/*  9489 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
/*       */   }
/*       */ 
/*       */   public void setBlob(String paramString, Blob paramBlob)
/*       */     throws SQLException
/*       */   {
/*  9507 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
/*       */   }
/*       */ 
/*       */   public void setBlob(String paramString, InputStream paramInputStream)
/*       */     throws SQLException
/*       */   {
/*  9534 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
/*       */   }
/*       */ 
/*       */   public void setObject(String paramString, Object paramObject, int paramInt1, int paramInt2)
/*       */     throws SQLException
/*       */   {
/*  9580 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
/*       */   }
/*       */ 
/*       */   public void setObject(String paramString, Object paramObject, int paramInt)
/*       */     throws SQLException
/*       */   {
/*  9608 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
/*       */   }
/*       */ 
/*       */   public void setObject(String paramString, Object paramObject)
/*       */     throws SQLException
/*       */   {
/*  9649 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
/*       */   }
/*       */ 
/*       */   public void setAsciiStream(String paramString, InputStream paramInputStream, int paramInt)
/*       */     throws SQLException
/*       */   {
/*  9676 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
/*       */   }
/*       */ 
/*       */   public void setBinaryStream(String paramString, InputStream paramInputStream, int paramInt)
/*       */     throws SQLException
/*       */   {
/*  9703 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
/*       */   }
/*       */ 
/*       */   public void setCharacterStream(String paramString, Reader paramReader, int paramInt)
/*       */     throws SQLException
/*       */   {
/*  9733 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
/*       */   }
/*       */ 
/*       */   public void setAsciiStream(String paramString, InputStream paramInputStream)
/*       */     throws SQLException
/*       */   {
/*  9761 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
/*       */   }
/*       */ 
/*       */   public void setBinaryStream(String paramString, InputStream paramInputStream)
/*       */     throws SQLException
/*       */   {
/*  9788 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
/*       */   }
/*       */ 
/*       */   public void setCharacterStream(String paramString, Reader paramReader)
/*       */     throws SQLException
/*       */   {
/*  9819 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
/*       */   }
/*       */ 
/*       */   public void setBigDecimal(String paramString, BigDecimal paramBigDecimal)
/*       */     throws SQLException
/*       */   {
/*  9838 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
/*       */   }
/*       */ 
/*       */   public void setString(String paramString1, String paramString2)
/*       */     throws SQLException
/*       */   {
/*  9861 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
/*       */   }
/*       */ 
/*       */   public void setBytes(String paramString, byte[] paramArrayOfByte)
/*       */     throws SQLException
/*       */   {
/*  9883 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
/*       */   }
/*       */ 
/*       */   public void setTimestamp(String paramString, Timestamp paramTimestamp)
/*       */     throws SQLException
/*       */   {
/*  9905 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
/*       */   }
/*       */ 
/*       */   public void setNull(String paramString, int paramInt)
/*       */     throws SQLException
/*       */   {
/*  9922 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
/*       */   }
/*       */ 
/*       */   public void setNull(String paramString1, int paramInt, String paramString2)
/*       */     throws SQLException
/*       */   {
/*  9960 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
/*       */   }
/*       */ 
/*       */   public void setBoolean(String paramString, boolean paramBoolean)
/*       */     throws SQLException
/*       */   {
/*  9980 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
/*       */   }
/*       */ 
/*       */   public void setByte(String paramString, byte paramByte)
/*       */     throws SQLException
/*       */   {
/* 10000 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
/*       */   }
/*       */ 
/*       */   public void setShort(String paramString, short paramShort)
/*       */     throws SQLException
/*       */   {
/* 10020 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
/*       */   }
/*       */ 
/*       */   public void setInt(String paramString, int paramInt)
/*       */     throws SQLException
/*       */   {
/* 10039 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
/*       */   }
/*       */ 
/*       */   public void setLong(String paramString, long paramLong)
/*       */     throws SQLException
/*       */   {
/* 10058 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
/*       */   }
/*       */ 
/*       */   public void setFloat(String paramString, float paramFloat)
/*       */     throws SQLException
/*       */   {
/* 10077 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
/*       */   }
/*       */ 
/*       */   public void setDouble(String paramString, double paramDouble)
/*       */     throws SQLException
/*       */   {
/* 10096 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
/*       */   }
/*       */ 
/*       */   private void readObject(ObjectInputStream paramObjectInputStream)
/*       */     throws IOException, ClassNotFoundException
/*       */   {
/* 10106 */     paramObjectInputStream.defaultReadObject();
/*       */     try
/*       */     {
/* 10109 */       this.resBundle = JdbcRowSetResourceBundle.getJdbcRowSetResourceBundle();
/*       */     } catch (IOException localIOException) {
/* 10111 */       throw new RuntimeException(localIOException);
/*       */     }
/*       */   }
/*       */ 
/*       */   public <T> T getObject(int paramInt, Class<T> paramClass)
/*       */     throws SQLException
/*       */   {
/* 10118 */     throw new SQLFeatureNotSupportedException("Not supported yet.");
/*       */   }
/*       */ 
/*       */   public <T> T getObject(String paramString, Class<T> paramClass) throws SQLException {
/* 10122 */     throw new SQLFeatureNotSupportedException("Not supported yet.");
/*       */   }
/*       */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.rowset.CachedRowSetImpl
 * JD-Core Version:    0.6.2
 */