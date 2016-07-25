/*      */ package sun.jdbc.odbc;
/*      */ 
/*      */ import java.io.InputStream;
/*      */ import java.io.Reader;
/*      */ import java.math.BigDecimal;
/*      */ import java.sql.BatchUpdateException;
/*      */ import java.sql.Connection;
/*      */ import java.sql.Date;
/*      */ import java.sql.ResultSet;
/*      */ import java.sql.SQLException;
/*      */ import java.sql.SQLWarning;
/*      */ import java.sql.Statement;
/*      */ import java.sql.Time;
/*      */ import java.sql.Timestamp;
/*      */ import java.util.Enumeration;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Vector;
/*      */ 
/*      */ public class JdbcOdbcStatement extends JdbcOdbcObject
/*      */   implements Statement
/*      */ {
/*      */   protected JdbcOdbc OdbcApi;
/*      */   protected long hDbc;
/*      */   protected long hStmt;
/*      */   protected SQLWarning lastWarning;
/*      */   protected Hashtable typeInfo;
/*      */   protected ResultSet myResultSet;
/*      */   protected JdbcOdbcConnectionInterface myConnection;
/*      */   protected int rsType;
/*      */   protected int rsConcurrency;
/*      */   protected int fetchDirection;
/*      */   protected int fetchSize;
/*      */   protected Vector batchSqlVec;
/*      */   protected boolean batchSupport;
/*      */   protected int batchRCFlag;
/*      */   protected String mySql;
/*      */   protected boolean batchOn;
/*      */   protected int rsBlockSize;
/*      */   protected int moreResults;
/*      */   protected boolean closeCalledFromFinalize;
/*      */ 
/*      */   public JdbcOdbcStatement(JdbcOdbcConnectionInterface paramJdbcOdbcConnectionInterface)
/*      */   {
/*   46 */     this.OdbcApi = null;
/*   47 */     this.hDbc = 0L;
/*   48 */     this.hStmt = 0L;
/*   49 */     this.lastWarning = null;
/*   50 */     this.myConnection = paramJdbcOdbcConnectionInterface;
/*   51 */     this.rsType = 1003;
/*   52 */     this.rsConcurrency = 1007;
/*   53 */     this.fetchDirection = 1000;
/*   54 */     this.fetchSize = 1;
/*      */ 
/*   56 */     this.batchRCFlag = -1;
/*   57 */     this.batchSupport = false;
/*      */ 
/*   59 */     this.moreResults = 1;
/*      */   }
/*      */ 
/*      */   protected void finalize()
/*      */   {
/*   69 */     if (this.OdbcApi.getTracer().isTracing()) {
/*   70 */       this.OdbcApi.getTracer().trace("Statement.finalize " + this);
/*      */     }
/*      */     try
/*      */     {
/*   74 */       this.closeCalledFromFinalize = true;
/*   75 */       close();
/*      */     }
/*      */     catch (SQLException localSQLException)
/*      */     {
/*      */     }
/*      */   }
/*      */ 
/*      */   public void initialize(JdbcOdbc paramJdbcOdbc, long paramLong1, long paramLong2, Hashtable paramHashtable, int paramInt1, int paramInt2)
/*      */     throws SQLException
/*      */   {
/*   98 */     this.OdbcApi = paramJdbcOdbc;
/*   99 */     this.hDbc = paramLong1;
/*  100 */     this.hStmt = paramLong2;
/*  101 */     this.rsType = paramInt1;
/*  102 */     this.rsConcurrency = paramInt2;
/*  103 */     this.typeInfo = paramHashtable;
/*      */ 
/*  110 */     this.batchRCFlag = this.myConnection.getBatchRowCountFlag(1);
/*      */ 
/*  113 */     if ((this.batchRCFlag > 0) && (this.batchRCFlag == 2))
/*      */     {
/*  115 */       this.batchSupport = true;
/*      */     }
/*  117 */     else this.batchSupport = false;
/*      */ 
/*  121 */     if ((this.rsType == 1003) || (this.rsType == 1004) || (this.rsType == 1005))
/*      */     {
/*  125 */       if ((this.rsConcurrency != 1007) && (this.rsConcurrency != 1008))
/*      */       {
/*  132 */         close();
/*  133 */         throw new SQLException("Invalid Concurrency Type.");
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/*  138 */       close();
/*  139 */       throw new SQLException("Invalid Cursor Type.");
/*      */     }
/*      */ 
/*  146 */     int i = this.myConnection.getOdbcCursorType(this.rsType);
/*      */ 
/*  148 */     if (i == -1)
/*      */     {
/*  150 */       i = this.myConnection.getBestOdbcCursorType();
/*      */ 
/*  152 */       if (i == -1) {
/*  153 */         throw new SQLException("The result set type is not supported.");
/*      */       }
/*      */ 
/*  157 */       setWarning(new SQLWarning("The result set type has been downgraded and changed."));
/*      */ 
/*  166 */       switch (i)
/*      */       {
/*      */       case 0:
/*  169 */         this.rsType = 1003;
/*  170 */         break;
/*      */       case 1:
/*      */       case 3:
/*  173 */         this.rsType = 1004;
/*      */       case 2:
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  181 */     if (this.rsConcurrency == 1008) {
/*  182 */       i = 2;
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/*  188 */       this.OdbcApi.SQLSetStmtOption(this.hStmt, (short)6, i);
/*      */     }
/*      */     catch (SQLWarning localSQLWarning1)
/*      */     {
/*  193 */       setWarning(localSQLWarning1);
/*      */     }
/*      */     catch (SQLException localSQLException1)
/*      */     {
/*  198 */       if (i != 0)
/*      */       {
/*  200 */         localSQLException1.fillInStackTrace();
/*  201 */         throw localSQLException1;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  208 */     int j = this.myConnection.getOdbcConcurrency(this.rsConcurrency);
/*      */     try
/*      */     {
/*  212 */       this.OdbcApi.SQLSetStmtOption(this.hStmt, (short)7, j);
/*      */     }
/*      */     catch (SQLWarning localSQLWarning2)
/*      */     {
/*  217 */       setWarning(localSQLWarning2);
/*      */     }
/*      */     catch (SQLException localSQLException2)
/*      */     {
/*  222 */       if (j != 1)
/*      */       {
/*  224 */         localSQLException2.fillInStackTrace();
/*  225 */         throw localSQLException2;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public ResultSet executeQuery(String paramString)
/*      */     throws SQLException
/*      */   {
/*  244 */     if (this.OdbcApi.getTracer().isTracing()) {
/*  245 */       this.OdbcApi.getTracer().trace("*Statement.executeQuery (" + paramString + ")");
/*      */     }
/*  247 */     ResultSet localResultSet = null;
/*      */ 
/*  252 */     if (execute(paramString)) {
/*  253 */       localResultSet = getResultSet(false);
/*      */     }
/*      */     else
/*      */     {
/*  258 */       throw new SQLException("No ResultSet was produced");
/*      */     }
/*      */ 
/*  261 */     if (this.batchOn) {
/*  262 */       clearBatch();
/*      */     }
/*  264 */     return localResultSet;
/*      */   }
/*      */ 
/*      */   public int executeUpdate(String paramString)
/*      */     throws SQLException
/*      */   {
/*  279 */     if (this.OdbcApi.getTracer().isTracing()) {
/*  280 */       this.OdbcApi.getTracer().trace("*Statement.executeUpdate (" + paramString + ")");
/*      */     }
/*  282 */     int i = -1;
/*      */ 
/*  287 */     if (!execute(paramString)) {
/*  288 */       i = getUpdateCount();
/*      */     }
/*      */     else
/*      */     {
/*  295 */       throw new SQLException("No row count was produced");
/*      */     }
/*      */ 
/*  298 */     if (this.batchOn) {
/*  299 */       clearBatch();
/*      */     }
/*  301 */     return i;
/*      */   }
/*      */ 
/*      */   public synchronized boolean execute(String paramString)
/*      */     throws SQLException
/*      */   {
/*  316 */     if (this.OdbcApi.getTracer().isTracing()) {
/*  317 */       this.OdbcApi.getTracer().trace("*Statement.execute (" + paramString + ")");
/*      */     }
/*  319 */     boolean bool = false;
/*  320 */     Object localObject = null;
/*      */ 
/*  323 */     setSql(paramString);
/*      */ 
/*  327 */     reset();
/*      */ 
/*  332 */     lockIfNecessary(paramString);
/*      */     try
/*      */     {
/*  337 */       this.OdbcApi.SQLExecDirect(this.hStmt, paramString);
/*      */     }
/*      */     catch (SQLWarning localSQLWarning)
/*      */     {
/*  344 */       localObject = localSQLWarning;
/*      */     }
/*      */ 
/*  351 */     if (getColumnCount() > 0) {
/*  352 */       bool = true;
/*      */     }
/*      */ 
/*  355 */     if (this.batchOn) {
/*  356 */       clearBatch();
/*      */     }
/*  358 */     return bool;
/*      */   }
/*      */ 
/*      */   public ResultSet getResultSet()
/*      */     throws SQLException
/*      */   {
/*  369 */     if (this.OdbcApi.getTracer().isTracing()) {
/*  370 */       this.OdbcApi.getTracer().trace("*Statement.getResultSet");
/*      */     }
/*      */ 
/*  373 */     if (this.myResultSet != null)
/*  374 */       return this.myResultSet;
/*  375 */     this.myResultSet = getResultSet(true);
/*      */ 
/*  377 */     return this.myResultSet;
/*      */   }
/*      */ 
/*      */   public synchronized ResultSet getResultSet(boolean paramBoolean)
/*      */     throws SQLException
/*      */   {
/*  392 */     if (this.myResultSet != null)
/*      */     {
/*  397 */       throw new SQLException("Invalid state for getResultSet");
/*      */     }
/*      */ 
/*  401 */     JdbcOdbcResultSet localJdbcOdbcResultSet = null;
/*  402 */     int i = 1;
/*      */ 
/*  408 */     if (paramBoolean) {
/*  409 */       i = getColumnCount();
/*      */     }
/*      */ 
/*  414 */     if (i > 0)
/*      */     {
/*  419 */       if (this.rsType != 1003) {
/*  420 */         checkCursorDowngrade();
/*      */       }
/*  422 */       localJdbcOdbcResultSet = new JdbcOdbcResultSet();
/*  423 */       localJdbcOdbcResultSet.initialize(this.OdbcApi, this.hDbc, this.hStmt, true, this);
/*      */ 
/*  427 */       this.myResultSet = localJdbcOdbcResultSet;
/*      */     }
/*      */     else {
/*  430 */       clearMyResultSet();
/*      */     }
/*  432 */     return localJdbcOdbcResultSet;
/*      */   }
/*      */ 
/*      */   public int getUpdateCount()
/*      */     throws SQLException
/*      */   {
/*  444 */     if (this.OdbcApi.getTracer().isTracing()) {
/*  445 */       this.OdbcApi.getTracer().trace("*Statement.getUpdateCount");
/*      */     }
/*  447 */     int i = -1;
/*      */ 
/*  450 */     if (this.moreResults == 3) {
/*  451 */       return i;
/*      */     }
/*      */ 
/*  456 */     if (getColumnCount() == 0) {
/*  457 */       i = getRowCount();
/*      */     }
/*  459 */     return i;
/*      */   }
/*      */ 
/*      */   public synchronized void close()
/*      */     throws SQLException
/*      */   {
/*  474 */     if (this.OdbcApi.getTracer().isTracing()) {
/*  475 */       this.OdbcApi.getTracer().trace("*Statement.close");
/*      */     }
/*      */ 
/*  481 */     clearMyResultSet();
/*      */     try
/*      */     {
/*  486 */       clearWarnings();
/*  487 */       if (this.hStmt != 0L)
/*      */       {
/*  489 */         if (this.closeCalledFromFinalize == true) {
/*  490 */           if (!this.myConnection.isFreeStmtsFromConnectionOnly()) {
/*  491 */             this.OdbcApi.SQLFreeStmt(this.hStmt, 1);
/*      */           }
/*      */         }
/*      */         else {
/*  495 */           this.OdbcApi.SQLFreeStmt(this.hStmt, 1);
/*      */         }
/*  497 */         this.hStmt = 0L;
/*      */       }
/*      */     }
/*      */     catch (SQLException localSQLException) {
/*  501 */       localSQLException.printStackTrace();
/*      */     }
/*      */ 
/*  508 */     this.myConnection.deregisterStatement(this);
/*      */   }
/*      */ 
/*      */   protected void reset()
/*      */     throws SQLException
/*      */   {
/*  520 */     clearWarnings();
/*      */ 
/*  522 */     clearMyResultSet();
/*  523 */     if (this.hStmt != 0L)
/*      */     {
/*  525 */       this.OdbcApi.SQLFreeStmt(this.hStmt, 0);
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean getMoreResults()
/*      */     throws SQLException
/*      */   {
/*  541 */     if (this.OdbcApi.getTracer().isTracing()) {
/*  542 */       this.OdbcApi.getTracer().trace("*Statement.getMoreResults");
/*      */     }
/*      */ 
/*  545 */     Object localObject = null;
/*  546 */     boolean bool = false;
/*      */ 
/*  548 */     if (this.moreResults == 1) {
/*  549 */       this.moreResults = 3;
/*      */     }
/*      */ 
/*  553 */     clearWarnings();
/*      */     try
/*      */     {
/*  558 */       if (this.OdbcApi.SQLMoreResults(this.hStmt))
/*  559 */         this.moreResults = 2;
/*      */       else {
/*  561 */         this.moreResults = 3;
/*      */       }
/*      */ 
/*      */     }
/*      */     catch (SQLWarning localSQLWarning)
/*      */     {
/*  568 */       localObject = localSQLWarning;
/*      */     }
/*      */ 
/*  573 */     if (this.moreResults == 2)
/*      */     {
/*  580 */       if (getColumnCount() != 0) {
/*  581 */         bool = true;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  587 */     setWarning(localObject);
/*      */ 
/*  591 */     return bool;
/*      */   }
/*      */ 
/*      */   public int getMaxFieldSize()
/*      */     throws SQLException
/*      */   {
/*  607 */     if (this.OdbcApi.getTracer().isTracing()) {
/*  608 */       this.OdbcApi.getTracer().trace("*Statement.getMaxFieldSize");
/*      */     }
/*  610 */     return getStmtOption((short)3);
/*      */   }
/*      */ 
/*      */   public void setMaxFieldSize(int paramInt)
/*      */     throws SQLException
/*      */   {
/*  621 */     if (this.OdbcApi.getTracer().isTracing()) {
/*  622 */       this.OdbcApi.getTracer().trace("*Statement.setMaxFieldSize (" + paramInt + ")");
/*      */     }
/*  624 */     this.OdbcApi.SQLSetStmtOption(this.hStmt, (short)3, paramInt);
/*      */   }
/*      */ 
/*      */   public int getMaxRows()
/*      */     throws SQLException
/*      */   {
/*  638 */     if (this.OdbcApi.getTracer().isTracing()) {
/*  639 */       this.OdbcApi.getTracer().trace("*Statement.getMaxRows");
/*      */     }
/*  641 */     return getStmtOption((short)1);
/*      */   }
/*      */ 
/*      */   public void setMaxRows(int paramInt)
/*      */     throws SQLException
/*      */   {
/*  652 */     if (this.OdbcApi.getTracer().isTracing()) {
/*  653 */       this.OdbcApi.getTracer().trace("*Statement.setMaxRows (" + paramInt + ")");
/*      */     }
/*  655 */     if (paramInt < 0) {
/*  656 */       throw new SQLException("Invalid new max row limit");
/*      */     }
/*      */ 
/*  659 */     this.OdbcApi.SQLSetStmtOption(this.hStmt, (short)1, paramInt);
/*      */   }
/*      */ 
/*      */   public void setEscapeProcessing(boolean paramBoolean)
/*      */     throws SQLException
/*      */   {
/*  673 */     if (this.OdbcApi.getTracer().isTracing()) {
/*  674 */       this.OdbcApi.getTracer().trace("*Statement.setEscapeProcessing (" + paramBoolean + ")");
/*      */     }
/*      */ 
/*  677 */     int i = 0;
/*  678 */     if (!paramBoolean) {
/*  679 */       i = 1;
/*      */     }
/*  681 */     this.OdbcApi.SQLSetStmtOption(this.hStmt, (short)2, i);
/*      */   }
/*      */ 
/*      */   public int getQueryTimeout()
/*      */     throws SQLException
/*      */   {
/*  693 */     if (this.OdbcApi.getTracer().isTracing()) {
/*  694 */       this.OdbcApi.getTracer().trace("*Statement.getQueryTimeout");
/*      */     }
/*  696 */     return getStmtOption((short)0);
/*      */   }
/*      */ 
/*      */   public void setQueryTimeout(int paramInt)
/*      */     throws SQLException
/*      */   {
/*  707 */     if (this.OdbcApi.getTracer().isTracing()) {
/*  708 */       this.OdbcApi.getTracer().trace("*Statement.setQueryTimeout (" + paramInt + ")");
/*      */     }
/*      */ 
/*  711 */     this.OdbcApi.SQLSetStmtOption(this.hStmt, (short)0, paramInt);
/*      */   }
/*      */ 
/*      */   public void cancel()
/*      */     throws SQLException
/*      */   {
/*  725 */     if (this.OdbcApi.getTracer().isTracing()) {
/*  726 */       this.OdbcApi.getTracer().trace("*Statement.cancel");
/*      */     }
/*      */ 
/*  730 */     clearWarnings();
/*      */     try
/*      */     {
/*  735 */       this.OdbcApi.SQLCancel(this.hStmt);
/*      */     }
/*      */     catch (SQLWarning localSQLWarning)
/*      */     {
/*  741 */       setWarning(localSQLWarning);
/*      */     }
/*      */   }
/*      */ 
/*      */   public SQLWarning getWarnings()
/*      */     throws SQLException
/*      */   {
/*  755 */     if (this.OdbcApi.getTracer().isTracing()) {
/*  756 */       this.OdbcApi.getTracer().trace("*Statement.getWarnings");
/*      */     }
/*  758 */     return this.lastWarning;
/*      */   }
/*      */ 
/*      */   public void clearWarnings()
/*      */     throws SQLException
/*      */   {
/*  764 */     this.lastWarning = null;
/*      */   }
/*      */ 
/*      */   public void setWarning(SQLWarning paramSQLWarning)
/*      */   {
/*  774 */     this.lastWarning = paramSQLWarning;
/*      */   }
/*      */ 
/*      */   public void setCursorName(String paramString)
/*      */     throws SQLException
/*      */   {
/*  787 */     if (this.OdbcApi.getTracer().isTracing()) {
/*  788 */       this.OdbcApi.getTracer().trace("*Statement.setCursorName " + paramString + ")");
/*      */     }
/*  790 */     this.OdbcApi.SQLSetCursorName(this.hStmt, paramString);
/*      */   }
/*      */ 
/*      */   public void setFetchDirection(int paramInt)
/*      */     throws SQLException
/*      */   {
/*  801 */     if ((paramInt == 1000) || (paramInt == 1001) || (paramInt == 1002))
/*      */     {
/*  804 */       this.fetchDirection = paramInt;
/*      */     }
/*      */     else
/*  807 */       throw new SQLException("Invalid fetch direction");
/*      */   }
/*      */ 
/*      */   public int getFetchDirection()
/*      */     throws SQLException
/*      */   {
/*  814 */     return this.fetchDirection;
/*      */   }
/*      */ 
/*      */   public void setFetchSize(int paramInt)
/*      */     throws SQLException
/*      */   {
/*  821 */     if ((0 <= paramInt) && (paramInt <= getMaxRows())) {
/*  822 */       this.fetchSize = paramInt;
/*      */     }
/*      */     else
/*  825 */       throw new SQLException("Invalid Fetch Size");
/*      */   }
/*      */ 
/*      */   public int getFetchSize()
/*      */     throws SQLException
/*      */   {
/*  832 */     return this.fetchSize;
/*      */   }
/*      */ 
/*      */   public int getResultSetConcurrency()
/*      */     throws SQLException
/*      */   {
/*  838 */     return this.rsConcurrency;
/*      */   }
/*      */ 
/*      */   public int getResultSetType()
/*      */     throws SQLException
/*      */   {
/*  844 */     return this.rsType;
/*      */   }
/*      */ 
/*      */   public void addBatch(String paramString)
/*      */     throws SQLException
/*      */   {
/*  851 */     if (this.OdbcApi.getTracer().isTracing()) {
/*  852 */       this.OdbcApi.getTracer().trace("*Statement.addBatch (" + paramString + ")");
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/*  858 */       if (paramString != null)
/*      */       {
/*  861 */         this.batchSqlVec = this.myConnection.getBatchVector(this);
/*      */ 
/*  863 */         if (this.batchSqlVec == null)
/*      */         {
/*  865 */           this.batchSqlVec = new Vector(5, 10);
/*      */         }
/*      */ 
/*  868 */         this.batchSqlVec.addElement(paramString);
/*      */ 
/*  870 */         this.myConnection.setBatchVector(this.batchSqlVec, this);
/*      */ 
/*  872 */         this.batchOn = true;
/*      */       }
/*      */ 
/*      */     }
/*      */     catch (Exception localException)
/*      */     {
/*  878 */       localException.printStackTrace();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void clearBatch()
/*      */   {
/*  886 */     if (this.OdbcApi.getTracer().isTracing())
/*      */     {
/*  888 */       this.OdbcApi.getTracer().trace("*Statement.clearBatch");
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/*  893 */       if (this.batchSqlVec != null)
/*      */       {
/*  896 */         this.myConnection.removeBatchVector(this);
/*  897 */         this.batchSqlVec = null;
/*  898 */         this.batchOn = false;
/*      */       }
/*      */     }
/*      */     catch (Exception localException)
/*      */     {
/*  903 */       localException.printStackTrace();
/*      */     }
/*      */   }
/*      */ 
/*      */   public int[] executeBatch()
/*      */     throws BatchUpdateException
/*      */   {
/*  911 */     return executeBatchUpdate();
/*      */   }
/*      */ 
/*      */   protected int[] executeBatchUpdate()
/*      */     throws BatchUpdateException
/*      */   {
/*  918 */     int[] arrayOfInt1 = new int[0];
/*  919 */     int[] arrayOfInt2 = null;
/*  920 */     int i = 0;
/*      */ 
/*  922 */     if (this.OdbcApi.getTracer().isTracing())
/*      */     {
/*  924 */       this.OdbcApi.getTracer().trace("*Statement.executeBatch");
/*      */     }
/*      */ 
/*  927 */     if (!this.batchSupport)
/*      */     {
/*  929 */       return emulateBatchUpdate();
/*      */     }
/*      */ 
/*  934 */     this.batchSqlVec = this.myConnection.getBatchVector(this);
/*      */ 
/*  936 */     if (this.batchSqlVec != null)
/*      */     {
/*  939 */       Enumeration localEnumeration = this.batchSqlVec.elements();
/*      */ 
/*  941 */       arrayOfInt1 = new int[this.batchSqlVec.size()];
/*      */ 
/*  943 */       int j = arrayOfInt1.length;
/*      */ 
/*  945 */       StringBuffer localStringBuffer = new StringBuffer();
/*      */ 
/*  948 */       for (int k = 0; k < j; k++)
/*      */       {
/*  950 */         if (localEnumeration.hasMoreElements())
/*      */         {
/*  952 */           String str = (String)localEnumeration.nextElement();
/*      */ 
/*  966 */           localStringBuffer.append(str + "\n");
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */       try
/*      */       {
/*  975 */         if (!execute(localStringBuffer.toString()))
/*      */         {
/*      */           while (true)
/*      */           {
/*  980 */             k = getUpdateCount();
/*      */ 
/*  982 */             if (k == -1)
/*      */             {
/*      */               break;
/*      */             }
/*      */ 
/*  988 */             arrayOfInt1[(i++)] = k;
/*  989 */             getMoreResults();
/*      */           }
/*      */ 
/*  993 */           if (i < j)
/*      */           {
/*  995 */             arrayOfInt2 = new int[i];
/*  996 */             for (k = 0; k < i; k++)
/*      */             {
/*  998 */               arrayOfInt2[k] = arrayOfInt1[k];
/*      */             }
/*      */ 
/* 1001 */             clearBatch();
/* 1002 */             throw new JdbcOdbcBatchUpdateException("SQL Attempt to produce a ResultSet from executeBatch", arrayOfInt2);
/*      */           }
/*      */         }
/*      */         else
/*      */         {
/* 1007 */           clearBatch();
/*      */ 
/* 1009 */           throw new JdbcOdbcBatchUpdateException("SQL Attempt to produce a ResultSet from executeBatch", null);
/*      */         }
/*      */ 
/*      */       }
/*      */       catch (SQLException localSQLException)
/*      */       {
/* 1015 */         clearBatch();
/*      */ 
/* 1017 */         throw new JdbcOdbcBatchUpdateException(localSQLException.getMessage(), localSQLException.getSQLState(), arrayOfInt2);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1022 */     clearBatch();
/*      */ 
/* 1028 */     return arrayOfInt1;
/*      */   }
/*      */ 
/*      */   protected int[] emulateBatchUpdate()
/*      */     throws BatchUpdateException
/*      */   {
/* 1042 */     int[] arrayOfInt1 = new int[0];
/*      */ 
/* 1045 */     this.batchSqlVec = this.myConnection.getBatchVector(this);
/*      */ 
/* 1047 */     if (this.batchSqlVec != null)
/*      */     {
/* 1049 */       int[] arrayOfInt2 = new int[0];
/* 1050 */       int i = 0;
/*      */ 
/* 1052 */       Enumeration localEnumeration = this.batchSqlVec.elements();
/*      */ 
/* 1054 */       arrayOfInt1 = new int[this.batchSqlVec.size()];
/*      */ 
/* 1057 */       for (int j = 0; j < arrayOfInt1.length; j++)
/*      */       {
/* 1060 */         if (localEnumeration.hasMoreElements())
/*      */         {
/* 1063 */           String str = (String)localEnumeration.nextElement();
/*      */           try
/*      */           {
/* 1068 */             if (!execute(str))
/*      */             {
/* 1070 */               arrayOfInt1[j] = getUpdateCount();
/* 1071 */               i++;
/*      */             }
/*      */             else
/*      */             {
/* 1076 */               arrayOfInt2 = new int[i];
/* 1077 */               for (int k = 0; k <= j - 1; k++)
/*      */               {
/* 1079 */                 arrayOfInt2[k] = arrayOfInt1[k];
/*      */               }
/* 1081 */               clearBatch();
/*      */ 
/* 1083 */               throw new JdbcOdbcBatchUpdateException("No row count was produced from executeBatch", arrayOfInt2);
/*      */             }
/*      */ 
/*      */           }
/*      */           catch (SQLException localSQLException)
/*      */           {
/* 1089 */             arrayOfInt2 = new int[i];
/* 1090 */             for (int m = 0; m <= j - 1; m++)
/*      */             {
/* 1092 */               arrayOfInt2[m] = arrayOfInt1[m];
/*      */             }
/* 1094 */             clearBatch();
/*      */ 
/* 1096 */             throw new JdbcOdbcBatchUpdateException(localSQLException.getMessage(), localSQLException.getSQLState(), arrayOfInt2);
/*      */           }
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1102 */       clearBatch();
/*      */     }
/*      */ 
/* 1107 */     return arrayOfInt1;
/*      */   }
/*      */ 
/*      */   public Connection getConnection()
/*      */     throws SQLException
/*      */   {
/* 1115 */     return this.myConnection;
/*      */   }
/*      */ 
/*      */   public String getSql()
/*      */   {
/* 1120 */     return this.mySql;
/*      */   }
/*      */ 
/*      */   public void setSql(String paramString)
/*      */   {
/* 1130 */     this.mySql = paramString.toUpperCase();
/*      */   }
/*      */ 
/*      */   public Object[] getObjects()
/*      */   {
/* 1140 */     Object[] arrayOfObject = new Object[0];
/*      */ 
/* 1142 */     return arrayOfObject;
/*      */   }
/*      */ 
/*      */   public int[] getObjectTypes()
/*      */   {
/* 1152 */     int[] arrayOfInt = new int[0];
/*      */ 
/* 1154 */     return arrayOfInt;
/*      */   }
/*      */ 
/*      */   public int getParamCount()
/*      */   {
/* 1164 */     return 0;
/*      */   }
/*      */ 
/*      */   public int getBlockCursorSize()
/*      */   {
/* 1174 */     return this.rsBlockSize;
/*      */   }
/*      */ 
/*      */   public void setBlockCursorSize(int paramInt)
/*      */   {
/* 1185 */     this.rsBlockSize = paramInt;
/*      */   }
/*      */ 
/*      */   protected int getStmtOption(short paramShort)
/*      */     throws SQLException
/*      */   {
/* 1200 */     int i = 0;
/*      */ 
/* 1204 */     clearWarnings();
/*      */     try
/*      */     {
/* 1207 */       i = (int)this.OdbcApi.SQLGetStmtOption(this.hStmt, paramShort);
/*      */     }
/*      */     catch (JdbcOdbcSQLWarning localJdbcOdbcSQLWarning)
/*      */     {
/* 1215 */       BigDecimal localBigDecimal = (BigDecimal)localJdbcOdbcSQLWarning.value;
/* 1216 */       i = localBigDecimal.intValue();
/*      */ 
/* 1218 */       setWarning(JdbcOdbc.convertWarning(localJdbcOdbcSQLWarning));
/*      */     }
/* 1220 */     return i;
/*      */   }
/*      */ 
/*      */   protected int getColumnCount()
/*      */     throws SQLException
/*      */   {
/* 1231 */     int i = 0;
/*      */     try
/*      */     {
/* 1234 */       i = this.OdbcApi.SQLNumResultCols(this.hStmt);
/*      */     }
/*      */     catch (JdbcOdbcSQLWarning localJdbcOdbcSQLWarning)
/*      */     {
/* 1242 */       BigDecimal localBigDecimal = (BigDecimal)localJdbcOdbcSQLWarning.value;
/* 1243 */       i = localBigDecimal.intValue();
/*      */     }
/* 1245 */     return i;
/*      */   }
/*      */ 
/*      */   protected int getRowCount()
/*      */     throws SQLException
/*      */   {
/* 1257 */     int i = 0;
/*      */     try
/*      */     {
/* 1260 */       i = this.OdbcApi.SQLRowCount(this.hStmt);
/*      */     }
/*      */     catch (JdbcOdbcSQLWarning localJdbcOdbcSQLWarning)
/*      */     {
/* 1268 */       BigDecimal localBigDecimal = (BigDecimal)localJdbcOdbcSQLWarning.value;
/* 1269 */       i = localBigDecimal.intValue();
/*      */     }
/* 1271 */     return i;
/*      */   }
/*      */ 
/*      */   protected boolean lockIfNecessary(String paramString)
/*      */     throws SQLException
/*      */   {
/* 1286 */     boolean bool = false;
/*      */ 
/* 1290 */     String str = paramString.toUpperCase();
/*      */ 
/* 1295 */     int i = str.indexOf(" FOR UPDATE");
/*      */ 
/* 1300 */     if (i > 0) {
/* 1301 */       if (this.OdbcApi.getTracer().isTracing()) {
/* 1302 */         this.OdbcApi.getTracer().trace("Setting concurrency for update");
/*      */       }
/*      */       try
/*      */       {
/* 1306 */         this.OdbcApi.SQLSetStmtOption(this.hStmt, (short)7, 2);
/*      */       }
/*      */       catch (SQLWarning localSQLWarning)
/*      */       {
/* 1312 */         setWarning(localSQLWarning);
/*      */       }
/* 1314 */       bool = true;
/*      */     }
/*      */ 
/* 1317 */     return bool;
/*      */   }
/*      */ 
/*      */   protected int getPrecision(int paramInt)
/*      */   {
/* 1329 */     int i = -1;
/*      */ 
/* 1332 */     if (this.typeInfo != null) {
/* 1333 */       JdbcOdbcTypeInfo localJdbcOdbcTypeInfo = (JdbcOdbcTypeInfo)this.typeInfo.get(new Integer(paramInt));
/*      */ 
/* 1336 */       if (localJdbcOdbcTypeInfo != null) {
/* 1337 */         i = localJdbcOdbcTypeInfo.getPrec();
/*      */       }
/*      */     }
/*      */ 
/* 1341 */     if ((paramInt == -2) && (i == -1)) {
/* 1342 */       i = getPrecision(-3);
/*      */     }
/* 1344 */     return i;
/*      */   }
/*      */ 
/*      */   protected synchronized void clearMyResultSet()
/*      */     throws SQLException
/*      */   {
/* 1355 */     if (this.myResultSet != null)
/*      */     {
/* 1358 */       if (this.hStmt != 0L)
/*      */       {
/* 1360 */         this.myResultSet.close();
/*      */       }
/* 1362 */       this.myResultSet = null;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void checkCursorDowngrade()
/*      */     throws SQLException
/*      */   {
/* 1377 */     int i = (int)this.OdbcApi.SQLGetStmtOption(this.hStmt, (short)6);
/*      */ 
/* 1380 */     if (i != this.myConnection.getOdbcCursorType(this.rsType))
/*      */     {
/* 1382 */       if (i == 0)
/* 1383 */         this.rsType = 1003;
/*      */       else
/* 1385 */         this.rsType = 1004;
/*      */     }
/* 1387 */     setWarning(new SQLWarning("Result set type has been changed."));
/*      */   }
/*      */ 
/*      */   public static int getTypeFromObject(Object paramObject)
/*      */   {
/* 1400 */     if (paramObject == null) {
/* 1401 */       return 0;
/*      */     }
/* 1403 */     if ((paramObject instanceof String)) {
/* 1404 */       return 1;
/*      */     }
/* 1406 */     if ((paramObject instanceof BigDecimal)) {
/* 1407 */       return 2;
/*      */     }
/* 1409 */     if ((paramObject instanceof Boolean)) {
/* 1410 */       return -7;
/*      */     }
/* 1412 */     if ((paramObject instanceof Byte)) {
/* 1413 */       return -6;
/*      */     }
/* 1415 */     if ((paramObject instanceof Short)) {
/* 1416 */       return 5;
/*      */     }
/* 1418 */     if ((paramObject instanceof Integer)) {
/* 1419 */       return 4;
/*      */     }
/* 1421 */     if ((paramObject instanceof Long)) {
/* 1422 */       return -5;
/*      */     }
/* 1424 */     if ((paramObject instanceof Float)) {
/* 1425 */       return 6;
/*      */     }
/* 1427 */     if ((paramObject instanceof Double)) {
/* 1428 */       return 8;
/*      */     }
/* 1430 */     if ((paramObject instanceof byte[])) {
/* 1431 */       return -3;
/*      */     }
/* 1433 */     if ((paramObject instanceof InputStream)) {
/* 1434 */       return -4;
/*      */     }
/* 1436 */     if ((paramObject instanceof Reader)) {
/* 1437 */       return -1;
/*      */     }
/* 1439 */     if ((paramObject instanceof Date)) {
/* 1440 */       return 91;
/*      */     }
/* 1442 */     if ((paramObject instanceof Time)) {
/* 1443 */       return 92;
/*      */     }
/* 1445 */     if ((paramObject instanceof Timestamp))
/* 1446 */       return 93;
/* 1447 */     return 1111;
/*      */   }
/*      */ 
/*      */   public boolean getMoreResults(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 1455 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public ResultSet getGeneratedKeys() throws SQLException {
/* 1459 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public int executeUpdate(String paramString, int paramInt) throws SQLException {
/* 1463 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public int executeUpdate(String paramString, int[] paramArrayOfInt) throws SQLException {
/* 1467 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public int executeUpdate(String paramString, String[] paramArrayOfString) throws SQLException {
/* 1471 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public boolean execute(String paramString, int paramInt) throws SQLException {
/* 1475 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public boolean execute(String paramString, int[] paramArrayOfInt) throws SQLException {
/* 1479 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public boolean execute(String paramString, String[] paramArrayOfString) throws SQLException {
/* 1483 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public int getResultSetHoldability() throws SQLException {
/* 1487 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public boolean isClosed()
/*      */     throws SQLException
/*      */   {
/* 1498 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public <T> T unwrap(Class<T> paramClass)
/*      */     throws SQLException
/*      */   {
/* 1515 */     return null;
/*      */   }
/*      */ 
/*      */   public boolean isWrapperFor(Class<?> paramClass)
/*      */     throws SQLException
/*      */   {
/* 1534 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean isPoolable()
/*      */     throws SQLException
/*      */   {
/* 1553 */     return false;
/*      */   }
/*      */ 
/*      */   public void setPoolable(boolean paramBoolean)
/*      */     throws SQLException
/*      */   {
/*      */   }
/*      */ 
/*      */   public void closeOnCompletion()
/*      */     throws SQLException
/*      */   {
/*      */   }
/*      */ 
/*      */   public boolean isCloseOnCompletion()
/*      */     throws SQLException
/*      */   {
/* 1608 */     return false;
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.jdbc.odbc.JdbcOdbcStatement
 * JD-Core Version:    0.6.2
 */