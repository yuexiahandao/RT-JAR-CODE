/*      */ package sun.jdbc.odbc;
/*      */ 
/*      */ import java.sql.Array;
/*      */ import java.sql.Blob;
/*      */ import java.sql.CallableStatement;
/*      */ import java.sql.Clob;
/*      */ import java.sql.DatabaseMetaData;
/*      */ import java.sql.NClob;
/*      */ import java.sql.PreparedStatement;
/*      */ import java.sql.ResultSet;
/*      */ import java.sql.SQLClientInfoException;
/*      */ import java.sql.SQLException;
/*      */ import java.sql.SQLFeatureNotSupportedException;
/*      */ import java.sql.SQLWarning;
/*      */ import java.sql.SQLXML;
/*      */ import java.sql.Savepoint;
/*      */ import java.sql.Statement;
/*      */ import java.sql.Struct;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Iterator;
/*      */ import java.util.Map;
/*      */ import java.util.Properties;
/*      */ import java.util.Set;
/*      */ import java.util.StringTokenizer;
/*      */ import java.util.Vector;
/*      */ import java.util.WeakHashMap;
/*      */ import java.util.concurrent.Executor;
/*      */ 
/*      */ public class JdbcOdbcConnection extends JdbcOdbcObject
/*      */   implements JdbcOdbcConnectionInterface
/*      */ {
/*      */   protected JdbcOdbc OdbcApi;
/*      */   protected JdbcOdbcDriverInterface myDriver;
/*      */   protected long hEnv;
/*      */   protected long hDbc;
/*      */   protected SQLWarning lastWarning;
/*      */   protected boolean closed;
/*      */   protected String URL;
/*      */   protected int odbcVer;
/*      */   protected Hashtable typeInfo;
/*      */   public WeakHashMap statements;
/*      */   protected Hashtable batchStatements;
/*      */   protected short rsTypeFO;
/*      */   protected short rsTypeSI;
/*      */   protected short rsTypeSS;
/*      */   protected short rsTypeBest;
/*      */   protected int rsBlockSize;
/*      */   protected int batchInStatements;
/*      */   protected int batchInProcedures;
/*      */   protected int batchInPrepares;
/*      */   private boolean freeStmtsFromConnectionOnly;
/* 2302 */   protected JdbcOdbcTracer tracer = new JdbcOdbcTracer();
/*      */ 
/*      */   public JdbcOdbcConnection(JdbcOdbc paramJdbcOdbc, long paramLong, JdbcOdbcDriverInterface paramJdbcOdbcDriverInterface)
/*      */   {
/*   52 */     this.OdbcApi = paramJdbcOdbc;
/*      */ 
/*   55 */     this.tracer = this.OdbcApi.getTracer();
/*      */ 
/*   57 */     this.myDriver = paramJdbcOdbcDriverInterface;
/*   58 */     this.hEnv = paramLong;
/*      */ 
/*   62 */     this.hDbc = 0L;
/*   63 */     this.URL = null;
/*      */ 
/*   67 */     this.lastWarning = null;
/*      */ 
/*   71 */     this.closed = true;
/*   72 */     this.freeStmtsFromConnectionOnly = false;
/*      */   }
/*      */ 
/*      */   protected void finalize()
/*      */   {
/*   82 */     if (this.tracer.isTracing()) {
/*   83 */       this.tracer.trace("Connection.finalize " + this);
/*      */     }
/*      */     try
/*      */     {
/*   87 */       close();
/*      */     }
/*      */     catch (SQLException localSQLException)
/*      */     {
/*      */     }
/*      */   }
/*      */ 
/*      */   public void initialize(String paramString, Properties paramProperties, int paramInt)
/*      */     throws SQLException
/*      */   {
/*  108 */     Object localObject1 = "";
/*      */ 
/*  113 */     Object localObject2 = "";
/*  114 */     Object localObject3 = null;
/*  115 */     Object localObject4 = null;
/*  116 */     Object localObject5 = null;
/*  117 */     Object localObject6 = "";
/*      */ 
/*  125 */     if (this.closed) {
/*  126 */       this.hDbc = this.myDriver.allocConnection(this.hEnv);
/*      */     }
/*      */ 
/*  131 */     if (paramInt > 0) {
/*  132 */       setLoginTimeout(paramInt);
/*      */     }
/*      */ 
/*  137 */     String str3 = paramProperties.getProperty("odbcRowSetSize");
/*  138 */     if (str3 != null) {
/*  139 */       setResultSetBlockSize(str3);
/*      */     }
/*      */ 
/*  142 */     this.OdbcApi.charSet = paramProperties.getProperty("charSet", System.getProperty("file.encoding"));
/*      */ 
/*  145 */     String str4 = paramProperties.getProperty("licfile", "");
/*  146 */     String str5 = paramProperties.getProperty("licpwd", "");
/*      */ 
/*  151 */     String str1 = paramProperties.getProperty("user", "");
/*  152 */     String str2 = paramProperties.getProperty("password", "");
/*      */ 
/*  158 */     String str6 = null;
/*  159 */     if ((paramString.indexOf("DRIVER") != -1) || (paramString.indexOf("Driver") != -1) || (paramString.indexOf("driver") != -1))
/*      */     {
/*  161 */       str6 = paramString;
/*      */     }
/*  163 */     else str6 = "DSN=" + paramString;
/*      */ 
/*  169 */     StringTokenizer localStringTokenizer = new StringTokenizer(str6, ";", false);
/*      */ 
/*  171 */     if (localStringTokenizer.countTokens() > 1)
/*      */     {
/*  173 */       int i = 0;
/*      */ 
/*  175 */       while (localStringTokenizer.hasMoreTokens())
/*      */       {
/*  177 */         i++;
/*  178 */         String str10 = localStringTokenizer.nextToken();
/*      */ 
/*  180 */         if (str10.startsWith("user"))
/*      */         {
/*  182 */           localObject3 = str10;
/*      */         }
/*  184 */         else if (str10.startsWith("password"))
/*      */         {
/*  186 */           localObject4 = str10;
/*      */         }
/*  188 */         else if (str10.startsWith("odbcRowSetSize"))
/*      */         {
/*  190 */           localObject5 = str10;
/*      */         }
/*  194 */         else if (i > 1)
/*      */         {
/*  196 */           localObject2 = (String)localObject2 + ";" + str10;
/*      */         }
/*  198 */         else localObject2 = (String)localObject2 + str10;
/*      */       }
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*  204 */       localObject2 = str6;
/*      */     }
/*      */ 
/*  210 */     localObject1 = localObject2;
/*      */     try
/*      */     {
/*      */       int m;
/*      */       StringBuffer localStringBuffer3;
/*  218 */       if ((str1.equals("")) && (localObject3 != null))
/*      */       {
/*  221 */         localObject6 = localObject3;
/*      */ 
/*  223 */         String str7 = localObject3.substring(4);
/*  224 */         if (!str7.equals(""))
/*      */         {
/*  228 */           if (((String)localObject1).indexOf("UID=") == -1) {
/*  229 */             localObject1 = (String)localObject1 + ";UID" + str7;
/*      */           }
/*      */           else {
/*  232 */             m = ((String)localObject1).indexOf("UID=");
/*  233 */             int n = ((String)localObject1).indexOf(";", m);
/*  234 */             localStringBuffer3 = new StringBuffer((String)localObject1);
/*  235 */             localStringBuffer3.replace(m, n, "UID=" + str7);
/*  236 */             localObject1 = localStringBuffer3.toString();
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*  242 */       else if (!str1.equals(""))
/*      */       {
/*  246 */         if (((String)localObject1).indexOf("UID=") == -1) {
/*  247 */           localObject1 = (String)localObject1 + ";UID=" + str1;
/*      */         }
/*      */         else {
/*  250 */           int j = ((String)localObject1).indexOf("UID=");
/*  251 */           m = ((String)localObject1).indexOf(";", j);
/*  252 */           StringBuffer localStringBuffer1 = new StringBuffer((String)localObject1);
/*  253 */           localStringBuffer1.replace(j, m, "UID=" + str1);
/*  254 */           localObject1 = localStringBuffer1.toString();
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  260 */       if ((str2.equals("")) && (localObject4 != null))
/*      */       {
/*  263 */         localObject6 = localObject4;
/*      */ 
/*  265 */         String str8 = localObject4.substring(8);
/*      */ 
/*  268 */         if (((String)localObject1).indexOf("UID=") != -1)
/*      */         {
/*  270 */           if (((String)localObject1).indexOf("PWD=") == -1) {
/*  271 */             localObject1 = (String)localObject1 + ";PWD" + str8;
/*      */           }
/*      */           else {
/*  274 */             m = ((String)localObject1).indexOf("PWD=");
/*  275 */             int i1 = ((String)localObject1).indexOf(";", m);
/*  276 */             localStringBuffer3 = new StringBuffer((String)localObject1);
/*  277 */             localStringBuffer3.replace(m, i1, "PWD=" + str8);
/*  278 */             localObject1 = localStringBuffer3.toString();
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*  284 */       else if (!str2.equals(""))
/*      */       {
/*  288 */         if (((String)localObject1).indexOf("UID=") != -1)
/*      */         {
/*  290 */           if (((String)localObject1).indexOf("PWD=") == -1) {
/*  291 */             localObject1 = (String)localObject1 + ";PWD=" + str2;
/*      */           }
/*      */           else {
/*  294 */             int k = ((String)localObject1).indexOf("PWD=");
/*  295 */             m = ((String)localObject1).indexOf(";", k);
/*  296 */             StringBuffer localStringBuffer2 = new StringBuffer((String)localObject1);
/*  297 */             localStringBuffer2.replace(k, m, "PWD=" + str2);
/*  298 */             localObject1 = localStringBuffer2.toString();
/*      */           }
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  304 */       if ((str3 == null) && (localObject5 != null))
/*      */       {
/*  306 */         localObject6 = localObject5;
/*      */ 
/*  308 */         String str9 = localObject5.substring(15);
/*      */ 
/*  310 */         setResultSetBlockSize(str9);
/*      */       }
/*      */     }
/*      */     catch (StringIndexOutOfBoundsException localStringIndexOutOfBoundsException)
/*      */     {
/*  315 */       throw new SQLException("invalid property values [" + (String)localObject6 + "]");
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/*  323 */       this.OdbcApi.SQLDriverConnect(this.hDbc, (String)localObject1);
/*      */     }
/*      */     catch (SQLWarning localSQLWarning) {
/*  326 */       this.lastWarning = localSQLWarning;
/*      */     }
/*      */     catch (SQLException localSQLException)
/*      */     {
/*  334 */       this.myDriver.closeConnection(this.hDbc);
/*      */ 
/*  338 */       throw localSQLException;
/*      */     }
/*      */ 
/*  343 */     this.closed = false;
/*      */ 
/*  347 */     if ((str4 == null) || 
/*  351 */       (str5 != null));
/*  357 */     this.statements = new WeakHashMap();
/*      */ 
/*  361 */     this.batchStatements = new Hashtable();
/*      */ 
/*  363 */     DatabaseMetaData localDatabaseMetaData = getMetaData();
/*      */ 
/*  365 */     this.OdbcApi.odbcDriverName = (localDatabaseMetaData.getDriverName() + " " + localDatabaseMetaData.getDriverVersion());
/*      */ 
/*  368 */     if (this.tracer.isTracing())
/*      */     {
/*  371 */       this.tracer.trace("Driver name:    " + localDatabaseMetaData.getDriverName());
/*  372 */       this.tracer.trace("Driver version: " + localDatabaseMetaData.getDriverVersion());
/*      */     }
/*      */     else {
/*  375 */       localDatabaseMetaData = null;
/*      */     }
/*      */ 
/*  381 */     buildTypeInfo();
/*      */ 
/*  383 */     checkScrollCursorSupport();
/*      */ 
/*  385 */     checkBatchUpdateSupport();
/*      */   }
/*      */ 
/*      */   public Statement createStatement()
/*      */     throws SQLException
/*      */   {
/*  396 */     return createStatement(1003, 1007);
/*      */   }
/*      */ 
/*      */   public Statement createStatement(int paramInt1, int paramInt2)
/*      */     throws SQLException
/*      */   {
/*  407 */     if (this.tracer.isTracing()) {
/*  408 */       this.tracer.trace("*Connection.createStatement");
/*      */     }
/*      */ 
/*  413 */     long l = this.OdbcApi.SQLAllocStmt(this.hDbc);
/*      */ 
/*  417 */     JdbcOdbcStatement localJdbcOdbcStatement = new JdbcOdbcStatement(this);
/*      */ 
/*  422 */     localJdbcOdbcStatement.initialize(this.OdbcApi, this.hDbc, l, null, paramInt1, paramInt2);
/*      */ 
/*  425 */     localJdbcOdbcStatement.setBlockCursorSize(this.rsBlockSize);
/*      */ 
/*  429 */     registerStatement(localJdbcOdbcStatement);
/*      */ 
/*  431 */     return localJdbcOdbcStatement;
/*      */   }
/*      */ 
/*      */   public PreparedStatement prepareStatement(String paramString)
/*      */     throws SQLException
/*      */   {
/*  443 */     return prepareStatement(paramString, 1003, 1007);
/*      */   }
/*      */ 
/*      */   public PreparedStatement prepareStatement(String paramString, int paramInt1, int paramInt2)
/*      */     throws SQLException
/*      */   {
/*  454 */     if (this.tracer.isTracing()) {
/*  455 */       this.tracer.trace("*Connection.prepareStatement (" + paramString + ")");
/*      */     }
/*      */ 
/*  459 */     JdbcOdbcPreparedStatement localJdbcOdbcPreparedStatement = null;
/*  460 */     Object localObject = null;
/*      */ 
/*  465 */     long l = this.OdbcApi.SQLAllocStmt(this.hDbc);
/*      */ 
/*  469 */     localJdbcOdbcPreparedStatement = new JdbcOdbcPreparedStatement(this);
/*  470 */     localJdbcOdbcPreparedStatement.initialize(this.OdbcApi, this.hDbc, l, this.typeInfo, paramInt1, paramInt2);
/*      */     try
/*      */     {
/*  475 */       this.OdbcApi.SQLPrepare(l, paramString);
/*      */     }
/*      */     catch (SQLWarning localSQLWarning)
/*      */     {
/*  482 */       localObject = localSQLWarning;
/*      */     }
/*      */     catch (SQLException localSQLException)
/*      */     {
/*  491 */       localJdbcOdbcPreparedStatement.close();
/*  492 */       throw localSQLException;
/*      */     }
/*      */ 
/*  495 */     localJdbcOdbcPreparedStatement.initBoundParam();
/*      */ 
/*  497 */     localJdbcOdbcPreparedStatement.setWarning(localObject);
/*      */ 
/*  499 */     localJdbcOdbcPreparedStatement.setBlockCursorSize(this.rsBlockSize);
/*      */ 
/*  503 */     localJdbcOdbcPreparedStatement.setSql(paramString);
/*      */ 
/*  507 */     registerStatement(localJdbcOdbcPreparedStatement);
/*      */ 
/*  511 */     return localJdbcOdbcPreparedStatement;
/*      */   }
/*      */ 
/*      */   public CallableStatement prepareCall(String paramString)
/*      */     throws SQLException
/*      */   {
/*  524 */     return prepareCall(paramString, 1003, 1007);
/*      */   }
/*      */ 
/*      */   public CallableStatement prepareCall(String paramString, int paramInt1, int paramInt2)
/*      */     throws SQLException
/*      */   {
/*  535 */     if (this.tracer.isTracing()) {
/*  536 */       this.tracer.trace("*Connection.prepareCall (" + paramString + ")");
/*      */     }
/*      */ 
/*  540 */     JdbcOdbcCallableStatement localJdbcOdbcCallableStatement = null;
/*  541 */     Object localObject = null;
/*      */ 
/*  545 */     long l = this.OdbcApi.SQLAllocStmt(this.hDbc);
/*      */ 
/*  549 */     localJdbcOdbcCallableStatement = new JdbcOdbcCallableStatement(this);
/*  550 */     localJdbcOdbcCallableStatement.initialize(this.OdbcApi, this.hDbc, l, this.typeInfo, paramInt1, paramInt2);
/*      */     try
/*      */     {
/*  555 */       this.OdbcApi.SQLPrepare(l, paramString);
/*      */     }
/*      */     catch (SQLWarning localSQLWarning)
/*      */     {
/*  562 */       localObject = localSQLWarning;
/*      */     }
/*      */     catch (SQLException localSQLException)
/*      */     {
/*  571 */       localJdbcOdbcCallableStatement.close();
/*  572 */       throw localSQLException;
/*      */     }
/*      */ 
/*  575 */     localJdbcOdbcCallableStatement.initBoundParam();
/*      */ 
/*  577 */     localJdbcOdbcCallableStatement.setWarning(localObject);
/*      */ 
/*  579 */     localJdbcOdbcCallableStatement.setBlockCursorSize(this.rsBlockSize);
/*      */ 
/*  583 */     localJdbcOdbcCallableStatement.setSql(paramString);
/*      */ 
/*  587 */     registerStatement(localJdbcOdbcCallableStatement);
/*      */ 
/*  591 */     return localJdbcOdbcCallableStatement;
/*      */   }
/*      */ 
/*      */   public String nativeSQL(String paramString)
/*      */     throws SQLException
/*      */   {
/*  604 */     if (this.tracer.isTracing()) {
/*  605 */       this.tracer.trace("*Connection.nativeSQL (" + paramString + ")");
/*      */     }
/*      */ 
/*      */     String str;
/*      */     try
/*      */     {
/*  611 */       str = this.OdbcApi.SQLNativeSql(this.hDbc, paramString);
/*      */     }
/*      */     catch (SQLException localSQLException)
/*      */     {
/*  617 */       str = paramString;
/*      */     }
/*  619 */     return str;
/*      */   }
/*      */ 
/*      */   public void setAutoCommit(boolean paramBoolean)
/*      */     throws SQLException
/*      */   {
/*  636 */     if (this.tracer.isTracing()) {
/*  637 */       this.tracer.trace("*Connection.setAutoCommit (" + paramBoolean + ")");
/*      */     }
/*      */ 
/*  641 */     int i = 1;
/*      */ 
/*  645 */     validateConnection();
/*      */ 
/*  647 */     if (!paramBoolean) {
/*  648 */       i = 0;
/*      */     }
/*      */ 
/*  651 */     this.OdbcApi.SQLSetConnectOption(this.hDbc, (short)102, i);
/*      */   }
/*      */ 
/*      */   public boolean getAutoCommit()
/*      */     throws SQLException
/*      */   {
/*  663 */     if (this.tracer.isTracing()) {
/*  664 */       this.tracer.trace("*Connection.getAutoCommit");
/*      */     }
/*      */ 
/*  668 */     boolean bool = false;
/*      */ 
/*  672 */     validateConnection();
/*      */ 
/*  674 */     int i = (int)this.OdbcApi.SQLGetConnectOption(this.hDbc, (short)102);
/*      */ 
/*  677 */     if (i == 1) {
/*  678 */       bool = true;
/*      */     }
/*  680 */     return bool;
/*      */   }
/*      */ 
/*      */   public void commit()
/*      */     throws SQLException
/*      */   {
/*  695 */     if (this.tracer.isTracing()) {
/*  696 */       this.tracer.trace("*Connection.commit");
/*      */     }
/*      */ 
/*  699 */     validateConnection();
/*  700 */     this.OdbcApi.SQLTransact(this.hEnv, this.hDbc, (short)0);
/*      */   }
/*      */ 
/*      */   public void rollback()
/*      */     throws SQLException
/*      */   {
/*  710 */     if (this.tracer.isTracing()) {
/*  711 */       this.tracer.trace("*Connection.rollback");
/*      */     }
/*      */ 
/*  714 */     validateConnection();
/*  715 */     this.OdbcApi.SQLTransact(this.hEnv, this.hDbc, (short)1);
/*      */   }
/*      */ 
/*      */   public void close()
/*      */     throws SQLException
/*      */   {
/*  727 */     if (this.tracer.isTracing()) {
/*  728 */       this.tracer.trace("*Connection.close");
/*      */     }
/*      */ 
/*  733 */     setFreeStmtsFromConnectionOnly();
/*  734 */     closeAllStatements();
/*      */ 
/*  738 */     if (!this.closed) {
/*  739 */       this.myDriver.disconnect(this.hDbc);
/*  740 */       this.myDriver.closeConnection(this.hDbc);
/*      */     }
/*      */ 
/*  743 */     this.closed = true;
/*  744 */     this.URL = null;
/*      */   }
/*      */ 
/*      */   public boolean isFreeStmtsFromConnectionOnly()
/*      */   {
/*  749 */     return this.freeStmtsFromConnectionOnly;
/*      */   }
/*      */ 
/*      */   public void setFreeStmtsFromConnectionOnly()
/*      */   {
/*  759 */     this.freeStmtsFromConnectionOnly = true;
/*      */   }
/*      */ 
/*      */   public void setFreeStmtsFromAnyWhere()
/*      */   {
/*  769 */     this.freeStmtsFromConnectionOnly = false;
/*      */   }
/*      */ 
/*      */   public boolean isClosed()
/*      */     throws SQLException
/*      */   {
/*  780 */     return this.closed;
/*      */   }
/*      */ 
/*      */   public DatabaseMetaData getMetaData()
/*      */     throws SQLException
/*      */   {
/*  789 */     if (this.tracer.isTracing()) {
/*  790 */       this.tracer.trace("*Connection.getMetaData");
/*      */     }
/*      */ 
/*  797 */     validateConnection();
/*      */ 
/*  801 */     JdbcOdbcDatabaseMetaData localJdbcOdbcDatabaseMetaData = new JdbcOdbcDatabaseMetaData(this.OdbcApi, this);
/*      */ 
/*  803 */     return localJdbcOdbcDatabaseMetaData;
/*      */   }
/*      */ 
/*      */   public void setReadOnly(boolean paramBoolean)
/*      */     throws SQLException
/*      */   {
/*  817 */     if (this.tracer.isTracing()) {
/*  818 */       this.tracer.trace("*Connection.setReadOnly (" + paramBoolean + ")");
/*      */     }
/*      */ 
/*  821 */     int i = 0;
/*      */ 
/*  825 */     validateConnection();
/*      */ 
/*  827 */     if (paramBoolean) {
/*  828 */       i = 1;
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/*  834 */       this.OdbcApi.SQLSetConnectOption(this.hDbc, (short)101, i);
/*      */     }
/*      */     catch (SQLException localSQLException)
/*      */     {
/*  838 */       if (this.tracer.isTracing())
/*  839 */         this.tracer.trace("setReadOnly exception ignored");
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean isReadOnly()
/*      */     throws SQLException
/*      */   {
/*  852 */     if (this.tracer.isTracing()) {
/*  853 */       this.tracer.trace("*Connection.isReadOnly");
/*      */     }
/*      */ 
/*  857 */     boolean bool = false;
/*      */ 
/*  861 */     validateConnection();
/*      */ 
/*  863 */     int i = (int)this.OdbcApi.SQLGetConnectOption(this.hDbc, (short)101);
/*      */ 
/*  866 */     if (i == 1) {
/*  867 */       bool = true;
/*      */     }
/*  869 */     return bool;
/*      */   }
/*      */ 
/*      */   public void setCatalog(String paramString)
/*      */     throws SQLException
/*      */   {
/*  880 */     if (this.tracer.isTracing()) {
/*  881 */       this.tracer.trace("*Connection.setCatalog (" + paramString + ")");
/*      */     }
/*      */ 
/*  886 */     validateConnection();
/*      */ 
/*  888 */     this.OdbcApi.SQLSetConnectOption(this.hDbc, (short)109, paramString);
/*      */   }
/*      */ 
/*      */   public String getCatalog()
/*      */     throws SQLException
/*      */   {
/*  900 */     if (this.tracer.isTracing()) {
/*  901 */       this.tracer.trace("*Connection.getCatalog");
/*      */     }
/*      */ 
/*  906 */     validateConnection();
/*      */ 
/*  908 */     return this.OdbcApi.SQLGetInfoString(this.hDbc, (short)16);
/*      */   }
/*      */ 
/*      */   public void setTransactionIsolation(int paramInt)
/*      */     throws SQLException
/*      */   {
/*  926 */     if (this.tracer.isTracing()) {
/*  927 */       this.tracer.trace("*Connection.setTransactionIsolation (" + paramInt + ")");
/*      */     }
/*      */ 
/*  933 */     validateConnection();
/*      */ 
/*  935 */     switch (paramInt)
/*      */     {
/*      */     case 0:
/*  940 */       setAutoCommit(true);
/*  941 */       break;
/*      */     case 1:
/*  943 */       setAutoCommit(false);
/*  944 */       this.OdbcApi.SQLSetConnectOption(this.hDbc, (short)108, 1);
/*      */ 
/*  947 */       break;
/*      */     case 2:
/*  949 */       setAutoCommit(false);
/*  950 */       this.OdbcApi.SQLSetConnectOption(this.hDbc, (short)108, 2);
/*      */ 
/*  953 */       break;
/*      */     case 4:
/*  955 */       setAutoCommit(false);
/*  956 */       this.OdbcApi.SQLSetConnectOption(this.hDbc, (short)108, 4);
/*      */ 
/*  959 */       break;
/*      */     case 8:
/*  961 */       setAutoCommit(false);
/*  962 */       this.OdbcApi.SQLSetConnectOption(this.hDbc, (short)108, 8);
/*      */ 
/*  965 */       break;
/*      */     case 3:
/*      */     case 5:
/*      */     case 6:
/*      */     case 7:
/*      */     default:
/*  971 */       setAutoCommit(false);
/*  972 */       this.OdbcApi.SQLSetConnectOption(this.hDbc, (short)108, paramInt);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setLicenseFile(String paramString)
/*      */     throws SQLException
/*      */   {
/*  988 */     if (this.tracer.isTracing()) {
/*  989 */       this.tracer.trace("*Connection.setLicenseFile (" + paramString + ")");
/*      */     }
/*      */ 
/*  997 */     this.OdbcApi.SQLSetConnectOption(this.hDbc, (short)1041, paramString);
/*      */   }
/*      */ 
/*      */   public void setLicensePassword(String paramString)
/*      */     throws SQLException
/*      */   {
/* 1013 */     if (this.tracer.isTracing()) {
/* 1014 */       this.tracer.trace("*Connection.setPassword (" + paramString + ")");
/*      */     }
/*      */ 
/* 1022 */     this.OdbcApi.SQLSetConnectOption(this.hDbc, (short)1042, paramString);
/*      */   }
/*      */ 
/*      */   public int getTransactionIsolation()
/*      */     throws SQLException
/*      */   {
/* 1035 */     if (this.tracer.isTracing()) {
/* 1036 */       this.tracer.trace("*Connection.getTransactionIsolation");
/*      */     }
/*      */ 
/* 1039 */     int i = 0;
/*      */ 
/* 1044 */     validateConnection();
/*      */ 
/* 1046 */     int j = (int)this.OdbcApi.SQLGetConnectOption(this.hDbc, (short)108);
/*      */ 
/* 1052 */     switch (j) {
/*      */     case 1:
/* 1054 */       i = 1;
/* 1055 */       break;
/*      */     case 2:
/* 1057 */       i = 2;
/* 1058 */       break;
/*      */     case 4:
/* 1060 */       i = 4;
/* 1061 */       break;
/*      */     case 8:
/* 1063 */       i = 8;
/* 1064 */       break;
/*      */     case 3:
/*      */     case 5:
/*      */     case 6:
/*      */     case 7:
/*      */     default:
/* 1066 */       i = j;
/*      */     }
/* 1068 */     return i;
/*      */   }
/*      */ 
/*      */   public SQLWarning getWarnings()
/*      */     throws SQLException
/*      */   {
/* 1081 */     if (this.tracer.isTracing()) {
/* 1082 */       this.tracer.trace("*Connection.getWarnings");
/*      */     }
/* 1084 */     return this.lastWarning;
/*      */   }
/*      */ 
/*      */   public void clearWarnings()
/*      */     throws SQLException
/*      */   {
/* 1095 */     this.lastWarning = null;
/*      */   }
/*      */ 
/*      */   public void validateConnection()
/*      */     throws SQLException
/*      */   {
/* 1108 */     if (this.closed)
/* 1109 */       throw new SQLException("Connection is closed");
/*      */   }
/*      */ 
/*      */   public long getHDBC()
/*      */   {
/* 1119 */     return this.hDbc;
/*      */   }
/*      */ 
/*      */   public void setURL(String paramString)
/*      */   {
/* 1129 */     this.URL = paramString;
/*      */   }
/*      */ 
/*      */   public String getURL()
/*      */   {
/* 1138 */     return this.URL;
/*      */   }
/*      */ 
/*      */   protected void setLoginTimeout(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 1150 */     this.OdbcApi.SQLSetConnectOption(this.hDbc, (short)103, paramInt);
/*      */   }
/*      */ 
/*      */   public int getODBCVer()
/*      */   {
/* 1163 */     if (this.odbcVer == 0)
/*      */     {
/*      */       String str;
/*      */       try
/*      */       {
/* 1168 */         str = this.OdbcApi.SQLGetInfoString(this.hDbc, (short)10);
/*      */       }
/*      */       catch (SQLException localSQLException)
/*      */       {
/* 1172 */         str = "-1";
/*      */       }
/*      */ 
/* 1178 */       Integer localInteger = new Integer(str.substring(0, 2));
/*      */ 
/* 1182 */       this.odbcVer = localInteger.intValue();
/*      */     }
/*      */ 
/* 1185 */     return this.odbcVer;
/*      */   }
/*      */ 
/*      */   protected void checkBatchUpdateSupport()
/*      */   {
/* 1195 */     this.batchInStatements = -1;
/* 1196 */     this.batchInProcedures = -1;
/* 1197 */     this.batchInPrepares = -1;
/*      */ 
/* 1200 */     int i = -1;
/* 1201 */     int j = -1;
/*      */ 
/* 1204 */     int k = 0;
/* 1205 */     int m = 0;
/* 1206 */     int n = 0;
/*      */     try
/*      */     {
/* 1211 */       i = this.OdbcApi.SQLGetInfo(this.hDbc, (short)121);
/*      */ 
/* 1213 */       if ((i & 0x2) > 0)
/*      */       {
/* 1215 */         k = 1;
/*      */       }
/*      */ 
/* 1218 */       if ((i & 0x8) > 0)
/*      */       {
/* 1220 */         m = 1;
/*      */       }
/*      */ 
/* 1225 */       j = this.OdbcApi.SQLGetInfo(this.hDbc, (short)120);
/*      */ 
/* 1230 */       if ((j & 0x4) > 0)
/*      */       {
/* 1232 */         this.batchInStatements = 4;
/* 1233 */         this.batchInProcedures = 4;
/*      */       }
/*      */       else
/*      */       {
/* 1238 */         if (k != 0)
/*      */         {
/* 1240 */           if ((j & 0x2) > 0)
/*      */           {
/* 1242 */             this.batchInStatements = 2;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/* 1247 */         if (m != 0)
/*      */         {
/* 1249 */           if ((j & 0x1) > 0)
/*      */           {
/* 1251 */             this.batchInProcedures = 1;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1259 */       i = this.OdbcApi.SQLGetInfo(this.hDbc, (short)153);
/*      */ 
/* 1261 */       if ((i & 0x1) > 0)
/*      */       {
/* 1263 */         n = 1;
/*      */ 
/* 1265 */         this.batchInPrepares = 1;
/*      */       }
/*      */ 
/*      */     }
/*      */     catch (SQLException localSQLException)
/*      */     {
/* 1273 */       this.batchInStatements = -1;
/* 1274 */       this.batchInProcedures = -1;
/* 1275 */       this.batchInPrepares = -1;
/*      */     }
/*      */   }
/*      */ 
/*      */   public int getBatchRowCountFlag(int paramInt)
/*      */   {
/* 1287 */     switch (paramInt)
/*      */     {
/*      */     case 1:
/* 1290 */       return this.batchInStatements;
/*      */     case 2:
/* 1292 */       return this.batchInPrepares;
/*      */     case 3:
/* 1294 */       return this.batchInProcedures;
/*      */     }
/*      */ 
/* 1297 */     return -1;
/*      */   }
/*      */ 
/*      */   public void checkScrollCursorSupport()
/*      */     throws SQLException
/*      */   {
/* 1310 */     short s = -1;
/* 1311 */     int i = 0;
/*      */ 
/* 1314 */     int j = this.OdbcApi.SQLGetInfo(this.hDbc, (short)44);
/*      */ 
/* 1319 */     this.rsTypeFO = -1;
/* 1320 */     this.rsTypeSI = -1;
/* 1321 */     this.rsTypeSS = -1;
/*      */ 
/* 1324 */     if ((j & 0x1) != 0) {
/* 1325 */       this.rsTypeFO = 0;
/*      */     }
/*      */ 
/* 1328 */     if ((j & 0x10) != 0) {
/* 1329 */       this.rsTypeSI = 3;
/*      */     }
/*      */ 
/* 1333 */     if ((j & 0x10) != 0) {
/* 1334 */       s = 3;
/* 1335 */       i = getOdbcCursorAttr2(s);
/* 1336 */       if ((i & 0x40) != 0)
/* 1337 */         this.rsTypeSS = s;
/*      */     }
/* 1339 */     if (((j & 0x2) != 0) || ((j & 0x8) != 0))
/*      */     {
/* 1341 */       s = 1;
/* 1342 */       i = getOdbcCursorAttr2(s);
/* 1343 */       if ((i & 0x40) != 0)
/* 1344 */         this.rsTypeSS = s;
/*      */       else
/* 1346 */         this.rsTypeSI = s;
/*      */     }
/* 1348 */     if ((j & 0x4) != 0) {
/* 1349 */       s = 2;
/* 1350 */       i = getOdbcCursorAttr2(s);
/* 1351 */       if ((i & 0x40) != 0) {
/* 1352 */         this.rsTypeSS = s;
/*      */       }
/*      */     }
/* 1355 */     this.rsTypeBest = s;
/*      */ 
/* 1357 */     if (this.rsTypeBest == -1)
/* 1358 */       this.rsTypeBest = this.rsTypeSS;
/* 1359 */     if (this.rsTypeBest == -1)
/* 1360 */       this.rsTypeBest = this.rsTypeSI;
/* 1361 */     if (this.rsTypeBest == -1)
/* 1362 */       this.rsTypeBest = this.rsTypeFO;
/*      */   }
/*      */ 
/*      */   public short getBestOdbcCursorType()
/*      */   {
/* 1372 */     return this.rsTypeBest;
/*      */   }
/*      */ 
/*      */   public short getOdbcCursorType(int paramInt)
/*      */   {
/* 1385 */     short s = -1;
/*      */ 
/* 1387 */     switch (paramInt) {
/*      */     case 1003:
/* 1389 */       s = this.rsTypeFO;
/* 1390 */       break;
/*      */     case 1004:
/* 1392 */       s = this.rsTypeSI;
/* 1393 */       break;
/*      */     case 1005:
/* 1395 */       s = this.rsTypeSS;
/*      */     }
/*      */ 
/* 1399 */     return s;
/*      */   }
/*      */ 
/*      */   public short getOdbcConcurrency(int paramInt)
/*      */   {
/* 1411 */     switch (paramInt) {
/*      */     case 1007:
/* 1413 */       return 1;
/*      */     case 1008:
/* 1415 */       return 2;
/*      */     }
/* 1417 */     return 1;
/*      */   }
/*      */ 
/*      */   public int getOdbcCursorAttr2(short paramShort)
/*      */     throws SQLException
/*      */   {
/* 1430 */     short s = 0;
/*      */ 
/* 1432 */     switch (paramShort) {
/*      */     case 0:
/* 1434 */       s = 147;
/* 1435 */       break;
/*      */     case 3:
/* 1437 */       s = 168;
/* 1438 */       break;
/*      */     case 1:
/* 1440 */       s = 151;
/* 1441 */       break;
/*      */     case 2:
/* 1443 */       s = 145;
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/* 1449 */       return this.OdbcApi.SQLGetInfo(this.hDbc, s);
/*      */     }
/*      */     catch (SQLException localSQLException)
/*      */     {
/*      */     }
/* 1454 */     return 0;
/*      */   }
/*      */ 
/*      */   public Map<String, Class<?>> getTypeMap()
/*      */     throws SQLException
/*      */   {
/* 1462 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void setTypeMap(Map<String, Class<?>> paramMap)
/*      */     throws SQLException
/*      */   {
/* 1469 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   protected void buildTypeInfo()
/*      */     throws SQLException
/*      */   {
/* 1481 */     this.typeInfo = new Hashtable();
/*      */ 
/* 1485 */     if (this.tracer.isTracing()) {
/* 1486 */       this.tracer.trace("Caching SQL type information");
/*      */     }
/*      */ 
/* 1489 */     ResultSet localResultSet = getMetaData().getTypeInfo();
/*      */ 
/* 1498 */     boolean bool = localResultSet.next();
/*      */ 
/* 1502 */     while (bool) {
/* 1503 */       String str = localResultSet.getString(1);
/* 1504 */       int i = localResultSet.getInt(2);
/*      */ 
/* 1510 */       if (this.typeInfo.get(new Integer(i)) == null) {
/* 1511 */         JdbcOdbcTypeInfo localJdbcOdbcTypeInfo = new JdbcOdbcTypeInfo();
/* 1512 */         localJdbcOdbcTypeInfo.setName(str);
/* 1513 */         localJdbcOdbcTypeInfo.setPrec(localResultSet.getInt(3));
/*      */ 
/* 1515 */         this.typeInfo.put(new Integer(i), localJdbcOdbcTypeInfo);
/*      */       }
/* 1517 */       bool = localResultSet.next();
/*      */     }
/*      */ 
/* 1522 */     localResultSet.close();
/*      */   }
/*      */ 
/*      */   protected void registerStatement(Statement paramStatement)
/*      */   {
/* 1533 */     if (this.tracer.isTracing()) {
/* 1534 */       this.tracer.trace("Registering Statement " + paramStatement);
/*      */     }
/*      */ 
/* 1540 */     this.statements.put(paramStatement, "");
/*      */   }
/*      */ 
/*      */   public void deregisterStatement(Statement paramStatement)
/*      */   {
/* 1552 */     if (this.statements.get(paramStatement) != null) {
/* 1553 */       if (this.tracer.isTracing()) {
/* 1554 */         this.tracer.trace("deregistering Statement " + paramStatement);
/*      */       }
/* 1556 */       this.statements.remove(paramStatement);
/*      */     }
/*      */   }
/*      */ 
/*      */   public synchronized void closeAllStatements()
/*      */     throws SQLException
/*      */   {
/* 1568 */     if (this.tracer.isTracing()) {
/* 1569 */       this.tracer.trace("" + this.statements.size() + " Statement(s) to close");
/*      */     }
/*      */ 
/* 1572 */     if (this.statements.size() == 0) {
/* 1573 */       return;
/*      */     }
/*      */ 
/* 1577 */     Set localSet = this.statements.keySet();
/* 1578 */     Iterator localIterator = localSet.iterator();
/*      */ 
/* 1585 */     while (localIterator.hasNext())
/*      */     {
/*      */       try
/*      */       {
/* 1590 */         Statement localStatement = (Statement)localIterator.next();
/*      */ 
/* 1596 */         localStatement.close();
/*      */       } catch (Exception localException) {
/* 1598 */         localSet = this.statements.keySet();
/* 1599 */         localIterator = localSet.iterator();
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1606 */     this.batchStatements = null;
/*      */   }
/*      */ 
/*      */   public synchronized void setBatchVector(Vector paramVector, Statement paramStatement)
/*      */   {
/* 1616 */     int i = -1;
/*      */ 
/* 1618 */     if (this.tracer.isTracing()) {
/* 1619 */       this.tracer.trace("setBatchVector " + paramStatement);
/*      */     }
/*      */ 
/* 1625 */     this.batchStatements.put(paramStatement, paramVector);
/*      */   }
/*      */ 
/*      */   public Vector getBatchVector(Statement paramStatement)
/*      */   {
/* 1635 */     if (this.tracer.isTracing())
/*      */     {
/* 1637 */       this.tracer.trace("getBatchVector " + paramStatement);
/*      */     }
/*      */ 
/* 1640 */     return (Vector)this.batchStatements.get(paramStatement);
/*      */   }
/*      */ 
/*      */   public synchronized void removeBatchVector(Statement paramStatement)
/*      */   {
/* 1650 */     if (this.tracer.isTracing())
/*      */     {
/* 1652 */       this.tracer.trace("removeBatchVector " + paramStatement);
/*      */     }
/*      */ 
/* 1655 */     this.batchStatements.remove(paramStatement);
/*      */   }
/*      */ 
/*      */   protected void setResultSetBlockSize(String paramString)
/*      */     throws SQLException
/*      */   {
/* 1668 */     this.rsBlockSize = 10;
/*      */ 
/* 1670 */     if (paramString != null)
/*      */     {
/* 1672 */       paramString.trim();
/*      */ 
/* 1674 */       if (!paramString.equals(""))
/*      */       {
/*      */         try
/*      */         {
/* 1678 */           int i = new Integer(paramString).intValue();
/*      */ 
/* 1680 */           if (i > 0)
/* 1681 */             this.rsBlockSize = i;
/*      */         }
/*      */         catch (NumberFormatException localNumberFormatException)
/*      */         {
/* 1685 */           throw new SQLException("invalid property value: [odbcRowSetSize=" + paramString + "]");
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setHoldability(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 1698 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public int getHoldability() throws SQLException
/*      */   {
/* 1703 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public Savepoint setSavepoint() throws SQLException
/*      */   {
/* 1708 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public Savepoint setSavepoint(String paramString) throws SQLException
/*      */   {
/* 1713 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void rollback(Savepoint paramSavepoint) throws SQLException
/*      */   {
/* 1718 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public Statement createStatement(int paramInt1, int paramInt2, int paramInt3)
/*      */     throws SQLException
/*      */   {
/* 1724 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public PreparedStatement prepareStatement(String paramString, int paramInt1, int paramInt2, int paramInt3)
/*      */     throws SQLException
/*      */   {
/* 1731 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void releaseSavepoint(Savepoint paramSavepoint) throws SQLException {
/* 1735 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public CallableStatement prepareCall(String paramString, int paramInt1, int paramInt2, int paramInt3)
/*      */     throws SQLException
/*      */   {
/* 1743 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public PreparedStatement prepareStatement(String paramString, int paramInt)
/*      */     throws SQLException
/*      */   {
/* 1750 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public PreparedStatement prepareStatement(String paramString, int[] paramArrayOfInt)
/*      */     throws SQLException
/*      */   {
/* 1756 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public PreparedStatement prepareStatement(String paramString, String[] paramArrayOfString)
/*      */     throws SQLException
/*      */   {
/* 1762 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public Clob createClob()
/*      */     throws SQLException
/*      */   {
/* 1777 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public Blob createBlob()
/*      */     throws SQLException
/*      */   {
/* 1792 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public NClob createNClob()
/*      */     throws SQLException
/*      */   {
/* 1807 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public SQLXML createSQLXML()
/*      */     throws SQLException
/*      */   {
/* 1821 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public boolean isValid(int paramInt)
/*      */   {
/* 1847 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void setClientInfo(String paramString1, String paramString2)
/*      */     throws SQLClientInfoException
/*      */   {
/* 1904 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void setClientInfo(Properties paramProperties)
/*      */     throws SQLClientInfoException
/*      */   {
/* 1936 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public String getClientInfo(String paramString)
/*      */     throws SQLException
/*      */   {
/* 1962 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public Properties getClientInfo()
/*      */     throws SQLException
/*      */   {
/* 1981 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public <T> T unwrap(Class<T> paramClass)
/*      */     throws SQLException
/*      */   {
/* 1998 */     return null;
/*      */   }
/*      */ 
/*      */   public boolean isWrapperFor(Class<?> paramClass)
/*      */     throws SQLException
/*      */   {
/* 2017 */     return false;
/*      */   }
/*      */ 
/*      */   public Struct createStruct(String paramString, Object[] paramArrayOfObject)
/*      */     throws SQLException
/*      */   {
/* 2036 */     return null;
/*      */   }
/*      */ 
/*      */   public Array createArrayOf(String paramString, Object[] paramArrayOfObject)
/*      */     throws SQLException
/*      */   {
/* 2054 */     return null;
/*      */   }
/*      */ 
/*      */   public void setSchema(String paramString)
/*      */     throws SQLException
/*      */   {
/*      */   }
/*      */ 
/*      */   public String getSchema()
/*      */     throws SQLException
/*      */   {
/* 2092 */     return null;
/*      */   }
/*      */ 
/*      */   public void abort(Executor paramExecutor)
/*      */     throws SQLException
/*      */   {
/* 2132 */     throw new SQLFeatureNotSupportedException();
/*      */   }
/*      */ 
/*      */   public void setNetworkTimeout(Executor paramExecutor, int paramInt)
/*      */     throws SQLException
/*      */   {
/* 2226 */     throw new SQLFeatureNotSupportedException();
/*      */   }
/*      */ 
/*      */   public int getNetworkTimeout()
/*      */     throws SQLException
/*      */   {
/* 2246 */     throw new SQLFeatureNotSupportedException();
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.jdbc.odbc.JdbcOdbcConnection
 * JD-Core Version:    0.6.2
 */