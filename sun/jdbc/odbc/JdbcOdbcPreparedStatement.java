/*      */ package sun.jdbc.odbc;
/*      */ 
/*      */ import java.io.BufferedOutputStream;
/*      */ import java.io.BufferedReader;
/*      */ import java.io.ByteArrayInputStream;
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.PrintStream;
/*      */ import java.io.Reader;
/*      */ import java.io.UnsupportedEncodingException;
/*      */ import java.math.BigDecimal;
/*      */ import java.math.BigInteger;
/*      */ import java.net.URL;
/*      */ import java.nio.charset.Charset;
/*      */ import java.nio.charset.CharsetEncoder;
/*      */ import java.nio.charset.IllegalCharsetNameException;
/*      */ import java.nio.charset.UnsupportedCharsetException;
/*      */ import java.sql.Array;
/*      */ import java.sql.BatchUpdateException;
/*      */ import java.sql.Blob;
/*      */ import java.sql.Clob;
/*      */ import java.sql.Date;
/*      */ import java.sql.NClob;
/*      */ import java.sql.ParameterMetaData;
/*      */ import java.sql.PreparedStatement;
/*      */ import java.sql.Ref;
/*      */ import java.sql.ResultSet;
/*      */ import java.sql.ResultSetMetaData;
/*      */ import java.sql.RowId;
/*      */ import java.sql.SQLException;
/*      */ import java.sql.SQLWarning;
/*      */ import java.sql.SQLXML;
/*      */ import java.sql.Time;
/*      */ import java.sql.Timestamp;
/*      */ import java.util.Calendar;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Vector;
/*      */ 
/*      */ public class JdbcOdbcPreparedStatement extends JdbcOdbcStatement
/*      */   implements PreparedStatement
/*      */ {
/*      */   protected int numParams;
/*      */   protected JdbcOdbcBoundParam[] boundParams;
/*      */   protected JdbcOdbcBoundArrayOfParams arrayParams;
/*      */   protected Vector batchSqlVec;
/*      */   protected boolean batchSupport;
/*      */   protected boolean batchParamsOn;
/*      */   protected int batchSize;
/*      */   protected int arrayDef;
/*      */   protected int arrayScale;
/*      */   protected int StringDef;
/*      */   protected int NumberDef;
/*      */   protected int NumberScale;
/*      */   protected int batchRCFlag;
/*      */   protected int[] paramsProcessed;
/*      */   protected int[] paramStatusArray;
/*      */   protected long[] pA1;
/*      */   protected long[] pA2;
/*      */   protected int binaryPrec;
/* 3753 */   protected JdbcOdbcUtils utils = new JdbcOdbcUtils();
/*      */ 
/*      */   public JdbcOdbcPreparedStatement(JdbcOdbcConnectionInterface paramJdbcOdbcConnectionInterface)
/*      */   {
/*   50 */     super(paramJdbcOdbcConnectionInterface);
/*      */   }
/*      */ 
/*      */   public void initialize(JdbcOdbc paramJdbcOdbc, long paramLong1, long paramLong2, Hashtable paramHashtable, int paramInt1, int paramInt2)
/*      */     throws SQLException
/*      */   {
/*   69 */     super.initialize(paramJdbcOdbc, paramLong1, paramLong2, paramHashtable, paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   public ResultSet executeQuery()
/*      */     throws SQLException
/*      */   {
/*   84 */     if (this.OdbcApi.getTracer().isTracing()) {
/*   85 */       this.OdbcApi.getTracer().trace("*PreparedStatement.executeQuery");
/*      */     }
/*      */ 
/*   88 */     ResultSet localResultSet = null;
/*      */ 
/*   90 */     if (execute())
/*      */     {
/*   92 */       localResultSet = getResultSet(false);
/*      */     }
/*      */     else
/*      */     {
/*   98 */       throw new SQLException("No ResultSet was produced");
/*      */     }
/*      */ 
/*  101 */     return localResultSet;
/*      */   }
/*      */ 
/*      */   public ResultSet executeQuery(String paramString)
/*      */     throws SQLException
/*      */   {
/*  113 */     if (this.OdbcApi.getTracer().isTracing()) {
/*  114 */       this.OdbcApi.getTracer().trace("*PreparedStatement.executeQuery (" + paramString + ")");
/*      */     }
/*  116 */     throw new SQLException("Driver does not support this function", "IM001");
/*      */   }
/*      */ 
/*      */   public int executeUpdate()
/*      */     throws SQLException
/*      */   {
/*  129 */     if (this.OdbcApi.getTracer().isTracing()) {
/*  130 */       this.OdbcApi.getTracer().trace("*PreparedStatement.executeUpdate");
/*      */     }
/*  132 */     int i = -1;
/*      */ 
/*  137 */     if (!execute()) {
/*  138 */       i = getUpdateCount();
/*      */     }
/*      */     else
/*      */     {
/*  145 */       throw new SQLException("No row count was produced");
/*      */     }
/*      */ 
/*  148 */     return i;
/*      */   }
/*      */ 
/*      */   public int executeUpdate(String paramString)
/*      */     throws SQLException
/*      */   {
/*  160 */     if (this.OdbcApi.getTracer().isTracing()) {
/*  161 */       this.OdbcApi.getTracer().trace("*PreparedStatement.executeUpdate (" + paramString + ")");
/*      */     }
/*  163 */     throw new SQLException("Driver does not support this function", "IM001");
/*      */   }
/*      */ 
/*      */   public boolean execute(String paramString)
/*      */     throws SQLException
/*      */   {
/*  178 */     if (this.OdbcApi.getTracer().isTracing()) {
/*  179 */       this.OdbcApi.getTracer().trace("*PreparedStatement.execute (" + paramString + ")");
/*      */     }
/*  181 */     throw new SQLException("Driver does not support this function", "IM001");
/*      */   }
/*      */ 
/*      */   public synchronized boolean execute()
/*      */     throws SQLException
/*      */   {
/*  197 */     if (this.OdbcApi.getTracer().isTracing()) {
/*  198 */       this.OdbcApi.getTracer().trace("*PreparedStatement.execute");
/*      */     }
/*  200 */     boolean bool1 = false;
/*  201 */     Object localObject = null;
/*  202 */     boolean bool2 = false;
/*      */ 
/*  206 */     clearWarnings();
/*      */ 
/*  210 */     reset();
/*      */     try
/*      */     {
/*  215 */       bool2 = this.OdbcApi.SQLExecute(this.hStmt);
/*      */ 
/*  221 */       while (bool2)
/*      */       {
/*  226 */         int i = this.OdbcApi.SQLParamData(this.hStmt);
/*      */ 
/*  231 */         if (i == -1) {
/*  232 */           bool2 = false;
/*      */         }
/*      */         else
/*      */         {
/*  240 */           if (this.batchParamsOn)
/*      */           {
/*  242 */             InputStream localInputStream = null;
/*      */ 
/*  245 */             int j = this.paramsProcessed[0];
/*      */ 
/*  252 */             localInputStream = this.arrayParams.getInputStreamElement(i, j);
/*      */ 
/*  254 */             this.boundParams[(i - 1)].setInputStream(localInputStream, this.arrayParams.getElementLength(i, j));
/*      */           }
/*      */ 
/*  257 */           putParamData(i);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */     catch (SQLWarning localSQLWarning)
/*      */     {
/*  267 */       localObject = localSQLWarning;
/*      */     }
/*      */ 
/*  298 */     if (getColumnCount() > 0) {
/*  299 */       bool1 = true;
/*      */     }
/*      */ 
/*  302 */     return bool1;
/*      */   }
/*      */ 
/*      */   public void setNull(int paramInt1, int paramInt2)
/*      */     throws SQLException
/*      */   {
/*  318 */     if (this.OdbcApi.getTracer().isTracing()) {
/*  319 */       this.OdbcApi.getTracer().trace("*PreparedStatement.setNull (" + paramInt1 + "," + paramInt2 + ")");
/*      */     }
/*      */ 
/*  322 */     clearParameter(paramInt1);
/*      */ 
/*  324 */     setInputParameter(paramInt1, true);
/*      */ 
/*  328 */     byte[] arrayOfByte = getLengthBuf(paramInt1);
/*      */ 
/*  330 */     long[] arrayOfLong = new long[2];
/*  331 */     arrayOfLong[0] = 0L;
/*  332 */     arrayOfLong[1] = 0L;
/*      */ 
/*  336 */     int i = 0;
/*  337 */     int j = 0;
/*      */ 
/*  339 */     if ((paramInt2 == 1) || (paramInt2 == 12))
/*      */     {
/*  341 */       i = this.StringDef;
/*      */     }
/*  343 */     else if ((paramInt2 == 2) || (paramInt2 == 3))
/*      */     {
/*  345 */       i = this.NumberDef;
/*  346 */       j = this.NumberScale;
/*      */     }
/*  348 */     else if ((paramInt2 == -2) || (paramInt2 == -3) || (paramInt2 == -4))
/*      */     {
/*  350 */       paramInt2 = this.boundParams[(paramInt1 - 1)].boundType;
/*  351 */       i = this.binaryPrec;
/*      */     }
/*      */ 
/*  354 */     if (i <= 0)
/*  355 */       i = getPrecision(paramInt2);
/*  356 */     if (i <= 0) {
/*  357 */       i = 1;
/*      */     }
/*      */ 
/*  360 */     if (!this.batchOn)
/*      */     {
/*  362 */       this.OdbcApi.SQLBindInParameterNull(this.hStmt, paramInt1, paramInt2, i, j, arrayOfByte, arrayOfLong);
/*      */     }
/*      */ 
/*  367 */     this.boundParams[(paramInt1 - 1)].pA1 = arrayOfLong[0];
/*  368 */     this.boundParams[(paramInt1 - 1)].pA2 = arrayOfLong[1];
/*  369 */     this.boundParams[(paramInt1 - 1)].scale = j;
/*  370 */     this.boundParams[(paramInt1 - 1)].boundType = paramInt2;
/*  371 */     this.boundParams[(paramInt1 - 1)].boundValue = null;
/*      */ 
/*  374 */     this.arrayParams.storeValue(paramInt1 - 1, null, -1);
/*  375 */     setSqlType(paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   public void setBoolean(int paramInt, boolean paramBoolean)
/*      */     throws SQLException
/*      */   {
/*  395 */     int i = 0;
/*      */ 
/*  398 */     if (paramBoolean) {
/*  399 */       i = 1;
/*      */     }
/*  401 */     clearParameter(paramInt);
/*      */ 
/*  403 */     setInputParameter(paramInt, true);
/*      */ 
/*  411 */     byte[] arrayOfByte = allocBindBuf(paramInt, 4);
/*      */ 
/*  413 */     long[] arrayOfLong = new long[2];
/*  414 */     arrayOfLong[0] = 0L;
/*  415 */     arrayOfLong[1] = 0L;
/*      */ 
/*  417 */     if (!this.batchOn)
/*      */     {
/*  419 */       this.OdbcApi.SQLBindInParameterInteger(this.hStmt, paramInt, -7, i, arrayOfByte, arrayOfLong);
/*      */     }
/*      */ 
/*  424 */     this.boundParams[(paramInt - 1)].pA1 = arrayOfLong[0];
/*  425 */     this.boundParams[(paramInt - 1)].pA2 = arrayOfLong[1];
/*  426 */     this.boundParams[(paramInt - 1)].boundType = -7;
/*  427 */     this.boundParams[(paramInt - 1)].boundValue = new Boolean(paramBoolean);
/*      */ 
/*  430 */     this.arrayParams.storeValue(paramInt - 1, new Boolean(paramBoolean), 0);
/*  431 */     setSqlType(paramInt, -7);
/*      */   }
/*      */ 
/*      */   public void setByte(int paramInt, byte paramByte)
/*      */     throws SQLException
/*      */   {
/*  444 */     clearParameter(paramInt);
/*      */ 
/*  446 */     setInputParameter(paramInt, true);
/*      */ 
/*  452 */     byte[] arrayOfByte = allocBindBuf(paramInt, 4);
/*      */ 
/*  454 */     long[] arrayOfLong = new long[2];
/*  455 */     arrayOfLong[0] = 0L;
/*  456 */     arrayOfLong[1] = 0L;
/*      */ 
/*  458 */     if (!this.batchOn)
/*      */     {
/*  460 */       this.OdbcApi.SQLBindInParameterInteger(this.hStmt, paramInt, -6, paramByte, arrayOfByte, arrayOfLong);
/*      */     }
/*      */ 
/*  465 */     this.boundParams[(paramInt - 1)].pA1 = arrayOfLong[0];
/*  466 */     this.boundParams[(paramInt - 1)].pA2 = arrayOfLong[1];
/*  467 */     this.boundParams[(paramInt - 1)].boundType = -6;
/*  468 */     this.boundParams[(paramInt - 1)].boundValue = new Byte(paramByte);
/*      */ 
/*  472 */     this.arrayParams.storeValue(paramInt - 1, new Byte(paramByte), 0);
/*  473 */     setSqlType(paramInt, -6);
/*      */   }
/*      */ 
/*      */   public void setShort(int paramInt, short paramShort)
/*      */     throws SQLException
/*      */   {
/*  486 */     clearParameter(paramInt);
/*      */ 
/*  488 */     setInputParameter(paramInt, true);
/*      */ 
/*  494 */     byte[] arrayOfByte = allocBindBuf(paramInt, 4);
/*      */ 
/*  496 */     long[] arrayOfLong = new long[2];
/*  497 */     arrayOfLong[0] = 0L;
/*  498 */     arrayOfLong[1] = 0L;
/*      */ 
/*  500 */     if (!this.batchOn)
/*      */     {
/*  502 */       this.OdbcApi.SQLBindInParameterInteger(this.hStmt, paramInt, 5, paramShort, arrayOfByte, arrayOfLong);
/*      */     }
/*      */ 
/*  507 */     this.boundParams[(paramInt - 1)].pA1 = arrayOfLong[0];
/*  508 */     this.boundParams[(paramInt - 1)].pA2 = arrayOfLong[1];
/*  509 */     this.boundParams[(paramInt - 1)].boundType = 5;
/*  510 */     this.boundParams[(paramInt - 1)].boundValue = new Short(paramShort);
/*      */ 
/*  513 */     this.arrayParams.storeValue(paramInt - 1, new Short(paramShort), 0);
/*  514 */     setSqlType(paramInt, 5);
/*      */   }
/*      */ 
/*      */   public void setInt(int paramInt1, int paramInt2)
/*      */     throws SQLException
/*      */   {
/*  526 */     clearParameter(paramInt1);
/*      */ 
/*  528 */     setInputParameter(paramInt1, true);
/*      */ 
/*  534 */     byte[] arrayOfByte = allocBindBuf(paramInt1, 4);
/*      */ 
/*  536 */     long[] arrayOfLong = new long[2];
/*  537 */     arrayOfLong[0] = 0L;
/*  538 */     arrayOfLong[1] = 0L;
/*      */ 
/*  540 */     if (!this.batchOn)
/*      */     {
/*  542 */       this.OdbcApi.SQLBindInParameterInteger(this.hStmt, paramInt1, 4, paramInt2, arrayOfByte, arrayOfLong);
/*      */     }
/*      */ 
/*  547 */     this.boundParams[(paramInt1 - 1)].pA1 = arrayOfLong[0];
/*  548 */     this.boundParams[(paramInt1 - 1)].pA2 = arrayOfLong[1];
/*  549 */     this.boundParams[(paramInt1 - 1)].boundType = 4;
/*  550 */     this.boundParams[(paramInt1 - 1)].boundValue = new Integer(paramInt2);
/*      */ 
/*  553 */     this.arrayParams.storeValue(paramInt1 - 1, new Integer(paramInt2), 0);
/*  554 */     setSqlType(paramInt1, 4);
/*      */   }
/*      */ 
/*      */   public void setLong(int paramInt, long paramLong)
/*      */     throws SQLException
/*      */   {
/*  568 */     clearParameter(paramInt);
/*      */ 
/*  574 */     if (this.myConnection.getODBCVer() == 2) {
/*  575 */       setChar(paramInt, -5, new Long(paramLong).intValue(), String.valueOf(paramLong));
/*      */     }
/*  578 */     else if (this.myConnection.getODBCVer() >= 3) {
/*  579 */       setInputParameter(paramInt, true);
/*      */ 
/*  585 */       byte[] arrayOfByte = allocBindBuf(paramInt, 8);
/*      */ 
/*  587 */       long[] arrayOfLong = new long[2];
/*  588 */       arrayOfLong[0] = 0L;
/*  589 */       arrayOfLong[1] = 0L;
/*      */ 
/*  591 */       if (!this.batchOn)
/*      */       {
/*  593 */         this.OdbcApi.SQLBindInParameterBigint(this.hStmt, paramInt, -5, 0, paramLong, arrayOfByte, arrayOfLong);
/*      */       }
/*      */ 
/*  598 */       this.boundParams[(paramInt - 1)].pA1 = arrayOfLong[0];
/*  599 */       this.boundParams[(paramInt - 1)].pA2 = arrayOfLong[1];
/*      */ 
/*  602 */       this.arrayParams.storeValue(paramInt - 1, new BigInteger(String.valueOf(paramLong)), 0);
/*  603 */       setSqlType(paramInt, -5);
/*      */     }
/*      */ 
/*  607 */     this.boundParams[(paramInt - 1)].boundType = -5;
/*  608 */     this.boundParams[(paramInt - 1)].boundValue = new BigInteger(String.valueOf(paramLong));
/*      */   }
/*      */ 
/*      */   public void setReal(int paramInt, float paramFloat)
/*      */     throws SQLException
/*      */   {
/*  621 */     clearParameter(paramInt);
/*      */ 
/*  623 */     setInputParameter(paramInt, true);
/*      */ 
/*  629 */     byte[] arrayOfByte = allocBindBuf(paramInt, 8);
/*      */ 
/*  631 */     long[] arrayOfLong = new long[2];
/*  632 */     arrayOfLong[0] = 0L;
/*  633 */     arrayOfLong[1] = 0L;
/*      */ 
/*  635 */     if (!this.batchOn)
/*      */     {
/*  637 */       this.OdbcApi.SQLBindInParameterFloat(this.hStmt, paramInt, 7, 0, paramFloat, arrayOfByte, arrayOfLong);
/*      */     }
/*      */ 
/*  642 */     this.boundParams[(paramInt - 1)].pA1 = arrayOfLong[0];
/*  643 */     this.boundParams[(paramInt - 1)].pA2 = arrayOfLong[1];
/*      */ 
/*  646 */     this.arrayParams.storeValue(paramInt - 1, new Float(paramFloat), 0);
/*  647 */     setSqlType(paramInt, 7);
/*      */   }
/*      */ 
/*      */   public void setFloat(int paramInt, float paramFloat)
/*      */     throws SQLException
/*      */   {
/*  661 */     setDouble(paramInt, paramFloat);
/*      */   }
/*      */ 
/*      */   public void setDouble(int paramInt, double paramDouble)
/*      */     throws SQLException
/*      */   {
/*  673 */     clearParameter(paramInt);
/*      */ 
/*  675 */     setInputParameter(paramInt, true);
/*      */ 
/*  681 */     byte[] arrayOfByte = allocBindBuf(paramInt, 8);
/*      */ 
/*  683 */     long[] arrayOfLong = new long[2];
/*  684 */     arrayOfLong[0] = 0L;
/*  685 */     arrayOfLong[1] = 0L;
/*      */ 
/*  687 */     if (!this.batchOn)
/*      */     {
/*  689 */       this.OdbcApi.SQLBindInParameterDouble(this.hStmt, paramInt, 8, 0, paramDouble, arrayOfByte, arrayOfLong);
/*      */     }
/*      */ 
/*  694 */     this.boundParams[(paramInt - 1)].pA1 = arrayOfLong[0];
/*  695 */     this.boundParams[(paramInt - 1)].pA2 = arrayOfLong[1];
/*  696 */     this.boundParams[(paramInt - 1)].boundType = 8;
/*  697 */     this.boundParams[(paramInt - 1)].boundValue = new Double(paramDouble);
/*      */ 
/*  700 */     this.arrayParams.storeValue(paramInt - 1, new Double(paramDouble), 0);
/*  701 */     setSqlType(paramInt, 8);
/*      */   }
/*      */ 
/*      */   public void setBigDecimal(int paramInt, BigDecimal paramBigDecimal)
/*      */     throws SQLException
/*      */   {
/*  714 */     clearParameter(paramInt);
/*      */ 
/*  716 */     if (paramBigDecimal == null) {
/*  717 */       setNull(paramInt, 2);
/*      */     }
/*      */     else
/*      */     {
/*  722 */       setChar(paramInt, 2, paramBigDecimal.scale(), paramBigDecimal.toString());
/*      */     }
/*  724 */     this.boundParams[(paramInt - 1)].boundType = 2;
/*  725 */     this.boundParams[(paramInt - 1)].boundValue = paramBigDecimal;
/*      */   }
/*      */ 
/*      */   public void setDecimal(int paramInt, BigDecimal paramBigDecimal)
/*      */     throws SQLException
/*      */   {
/*  737 */     clearParameter(paramInt);
/*      */ 
/*  739 */     if (paramBigDecimal == null) {
/*  740 */       setNull(paramInt, 3);
/*      */     }
/*      */     else
/*      */     {
/*  744 */       setChar(paramInt, 3, paramBigDecimal.scale(), paramBigDecimal.toString());
/*  745 */     }this.boundParams[(paramInt - 1)].boundType = 3;
/*  746 */     this.boundParams[(paramInt - 1)].boundValue = paramBigDecimal;
/*      */   }
/*      */ 
/*      */   public void setString(int paramInt, String paramString)
/*      */     throws SQLException
/*      */   {
/*  760 */     if (paramString == null)
/*  761 */       setNull(paramInt, 1);
/*  762 */     else if (paramString.length() >= 254)
/*  763 */       setChar(paramInt, -1, 0, paramString);
/*      */     else
/*  765 */       setChar(paramInt, 1, 0, paramString);
/*      */   }
/*      */ 
/*      */   public void setBytes(int paramInt, byte[] paramArrayOfByte)
/*      */     throws SQLException
/*      */   {
/*  781 */     if (paramArrayOfByte == null)
/*  782 */       setNull(paramInt, -2);
/*  783 */     else if (paramArrayOfByte.length > 8000)
/*      */     {
/*  785 */       setBinaryStream(paramInt, new ByteArrayInputStream(paramArrayOfByte), paramArrayOfByte.length);
/*      */     }
/*      */     else
/*      */     {
/*  789 */       setBinary(paramInt, -2, paramArrayOfByte);
/*      */     }
/*  791 */     this.boundParams[(paramInt - 1)].boundType = -2;
/*  792 */     this.boundParams[(paramInt - 1)].boundValue = paramArrayOfByte;
/*      */   }
/*      */ 
/*      */   public void setDate(int paramInt, Date paramDate)
/*      */     throws SQLException
/*      */   {
/*  805 */     if (paramDate == null)
/*      */     {
/*  807 */       setNull(paramInt, 91);
/*  808 */       return;
/*      */     }
/*  810 */     clearParameter(paramInt);
/*      */ 
/*  812 */     setInputParameter(paramInt, true);
/*      */ 
/*  817 */     byte[] arrayOfByte = allocBindBuf(paramInt, 32);
/*      */ 
/*  819 */     long[] arrayOfLong = new long[2];
/*  820 */     arrayOfLong[0] = 0L;
/*  821 */     arrayOfLong[1] = 0L;
/*      */ 
/*  823 */     if (!this.batchOn)
/*      */     {
/*  825 */       this.OdbcApi.SQLBindInParameterDate(this.hStmt, paramInt, paramDate, arrayOfByte, arrayOfLong);
/*      */     }
/*      */ 
/*  830 */     this.boundParams[(paramInt - 1)].pA1 = arrayOfLong[0];
/*  831 */     this.boundParams[(paramInt - 1)].pA2 = arrayOfLong[1];
/*  832 */     this.boundParams[(paramInt - 1)].boundType = 91;
/*  833 */     this.boundParams[(paramInt - 1)].boundValue = paramDate;
/*      */ 
/*  836 */     this.arrayParams.storeValue(paramInt - 1, paramDate, -3);
/*  837 */     setSqlType(paramInt, 91);
/*      */   }
/*      */ 
/*      */   public void setTime(int paramInt, Time paramTime)
/*      */     throws SQLException
/*      */   {
/*  851 */     if (paramTime == null)
/*      */     {
/*  853 */       setNull(paramInt, 92);
/*  854 */       return;
/*      */     }
/*  856 */     clearParameter(paramInt);
/*      */ 
/*  858 */     setInputParameter(paramInt, true);
/*      */ 
/*  863 */     byte[] arrayOfByte = allocBindBuf(paramInt, 32);
/*      */ 
/*  865 */     long[] arrayOfLong = new long[2];
/*  866 */     arrayOfLong[0] = 0L;
/*  867 */     arrayOfLong[1] = 0L;
/*      */ 
/*  869 */     if (!this.batchOn)
/*      */     {
/*  871 */       this.OdbcApi.SQLBindInParameterTime(this.hStmt, paramInt, paramTime, arrayOfByte, arrayOfLong);
/*      */     }
/*      */ 
/*  876 */     this.boundParams[(paramInt - 1)].pA1 = arrayOfLong[0];
/*  877 */     this.boundParams[(paramInt - 1)].pA2 = arrayOfLong[1];
/*  878 */     this.boundParams[(paramInt - 1)].boundType = 92;
/*  879 */     this.boundParams[(paramInt - 1)].boundValue = paramTime;
/*      */ 
/*  882 */     this.arrayParams.storeValue(paramInt - 1, paramTime, -3);
/*  883 */     setSqlType(paramInt, 92);
/*      */   }
/*      */ 
/*      */   public void setTimestamp(int paramInt, Timestamp paramTimestamp)
/*      */     throws SQLException
/*      */   {
/*  897 */     if (paramTimestamp == null)
/*      */     {
/*  899 */       setNull(paramInt, 93);
/*  900 */       return;
/*      */     }
/*  902 */     clearParameter(paramInt);
/*      */ 
/*  904 */     setInputParameter(paramInt, true);
/*      */ 
/*  909 */     byte[] arrayOfByte = allocBindBuf(paramInt, 32);
/*      */ 
/*  911 */     long[] arrayOfLong = new long[2];
/*  912 */     arrayOfLong[0] = 0L;
/*  913 */     arrayOfLong[1] = 0L;
/*      */ 
/*  915 */     if (!this.batchOn)
/*      */     {
/*  917 */       this.OdbcApi.SQLBindInParameterTimestamp(this.hStmt, paramInt, paramTimestamp, arrayOfByte, arrayOfLong);
/*      */     }
/*      */ 
/*  922 */     this.boundParams[(paramInt - 1)].pA1 = arrayOfLong[0];
/*  923 */     this.boundParams[(paramInt - 1)].pA2 = arrayOfLong[1];
/*  924 */     this.boundParams[(paramInt - 1)].boundValue = paramTimestamp;
/*  925 */     this.boundParams[(paramInt - 1)].boundType = 93;
/*      */ 
/*  928 */     this.arrayParams.storeValue(paramInt - 1, paramTimestamp, -3);
/*  929 */     setSqlType(paramInt, 93);
/*      */   }
/*      */ 
/*      */   public void setAsciiStream(int paramInt1, InputStream paramInputStream, int paramInt2)
/*      */     throws SQLException
/*      */   {
/*  955 */     setStream(paramInt1, paramInputStream, paramInt2, -1, 1);
/*      */   }
/*      */ 
/*      */   public void setUnicodeStream(int paramInt1, InputStream paramInputStream, int paramInt2)
/*      */     throws SQLException
/*      */   {
/*  965 */     setStream(paramInt1, paramInputStream, paramInt2, -1, 2);
/*      */   }
/*      */ 
/*      */   public void setBinaryStream(int paramInt1, InputStream paramInputStream, int paramInt2)
/*      */     throws SQLException
/*      */   {
/*  976 */     setStream(paramInt1, paramInputStream, paramInt2, -4, 3);
/*      */ 
/*  978 */     this.binaryPrec = paramInt2;
/*      */   }
/*      */ 
/*      */   public void clearParameters()
/*      */     throws SQLException
/*      */   {
/* 1002 */     if (this.hStmt != 0L)
/*      */     {
/* 1004 */       this.OdbcApi.SQLFreeStmt(this.hStmt, 3);
/* 1005 */       FreeParams();
/* 1006 */       for (int i = 1; (this.boundParams != null) && (i <= this.boundParams.length); 
/* 1007 */         i++)
/*      */       {
/* 1009 */         this.boundParams[(i - 1)].binaryData = null;
/* 1010 */         this.boundParams[(i - 1)].initialize();
/* 1011 */         this.boundParams[(i - 1)].paramInputStream = null;
/* 1012 */         this.boundParams[(i - 1)].inputParameter = false;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void clearParameter(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 1020 */     if (this.hStmt != 0L)
/*      */     {
/* 1022 */       if (this.boundParams[(paramInt - 1)].pA1 != 0L)
/*      */       {
/* 1024 */         JdbcOdbc.ReleaseStoredBytes(this.boundParams[(paramInt - 1)].pA1, this.boundParams[(paramInt - 1)].pA2);
/* 1025 */         this.boundParams[(paramInt - 1)].pA1 = 0L;
/* 1026 */         this.boundParams[(paramInt - 1)].pA2 = 0L;
/*      */       }
/* 1028 */       if (this.boundParams[(paramInt - 1)].pB1 != 0L)
/*      */       {
/* 1030 */         JdbcOdbc.ReleaseStoredBytes(this.boundParams[(paramInt - 1)].pB1, this.boundParams[(paramInt - 1)].pB2);
/* 1031 */         this.boundParams[(paramInt - 1)].pB1 = 0L;
/* 1032 */         this.boundParams[(paramInt - 1)].pB2 = 0L;
/*      */       }
/* 1034 */       if (this.boundParams[(paramInt - 1)].pC1 != 0L)
/*      */       {
/* 1036 */         JdbcOdbc.ReleaseStoredBytes(this.boundParams[(paramInt - 1)].pC1, this.boundParams[(paramInt - 1)].pC2);
/* 1037 */         this.boundParams[(paramInt - 1)].pC1 = 0L;
/* 1038 */         this.boundParams[(paramInt - 1)].pC2 = 0L;
/*      */       }
/* 1040 */       if (this.boundParams[(paramInt - 1)].pS1 != 0L)
/*      */       {
/* 1042 */         JdbcOdbc.ReleaseStoredChars(this.boundParams[(paramInt - 1)].pS1, this.boundParams[(paramInt - 1)].pS2);
/* 1043 */         this.boundParams[(paramInt - 1)].pS1 = 0L;
/* 1044 */         this.boundParams[(paramInt - 1)].pS2 = 0L;
/*      */       }
/* 1046 */       this.boundParams[(paramInt - 1)].binaryData = null;
/* 1047 */       this.boundParams[(paramInt - 1)].initialize();
/* 1048 */       this.boundParams[(paramInt - 1)].paramInputStream = null;
/* 1049 */       this.boundParams[(paramInt - 1)].inputParameter = false;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setObject(int paramInt, Object paramObject)
/*      */     throws SQLException
/*      */   {
/* 1064 */     setObject(paramInt, paramObject, getTypeFromObject(paramObject));
/*      */   }
/*      */ 
/*      */   public void setObject(int paramInt1, Object paramObject, int paramInt2)
/*      */     throws SQLException
/*      */   {
/* 1073 */     setObject(paramInt1, paramObject, paramInt2, 0);
/*      */   }
/*      */ 
/*      */   public void setObject(int paramInt1, Object paramObject, int paramInt2, int paramInt3)
/*      */     throws SQLException
/*      */   {
/* 1084 */     if (paramObject == null)
/*      */     {
/* 1086 */       setNull(paramInt1, paramInt2);
/* 1087 */       return;
/*      */     }
/*      */ 
/* 1096 */     String str = null;
/*      */ 
/* 1098 */     if ((paramObject instanceof byte[]))
/* 1099 */       str = new String("byte[]");
/*      */     else {
/* 1101 */       str = new String(paramObject.getClass().getName());
/*      */     }
/*      */ 
/* 1107 */     int i = 0;
/* 1108 */     BigInteger localBigInteger = null;
/* 1109 */     if (str.equalsIgnoreCase("java.lang.Boolean"))
/* 1110 */       if (paramObject.toString().equalsIgnoreCase("true")) {
/* 1111 */         i = 1;
/* 1112 */         localBigInteger = BigInteger.ONE;
/*      */       } else {
/* 1114 */         i = 0;
/* 1115 */         localBigInteger = BigInteger.ZERO;
/*      */       }
/*      */     try
/*      */     {
/*      */       Object localObject;
/*      */       BigDecimal localBigDecimal1;
/*      */       BigDecimal localBigDecimal2;
/* 1121 */       switch (paramInt2)
/*      */       {
/*      */       case 1:
/* 1127 */         if ((str.equalsIgnoreCase("java.lang.String")) || (str.equalsIgnoreCase("java.math.BigDecimal")) || (str.equalsIgnoreCase("java.lang.Boolean")) || (str.equalsIgnoreCase("java.lang.Integer")) || (str.equalsIgnoreCase("java.lang.Short")) || (str.equalsIgnoreCase("java.lang.Long")) || (str.equalsIgnoreCase("java.lang.Float")) || (str.equalsIgnoreCase("java.lang.Double")) || (str.equalsIgnoreCase("java.sql.Date")) || (str.equalsIgnoreCase("java.sql.Time")) || (str.equalsIgnoreCase("java.sql.Timestamp")))
/*      */         {
/* 1139 */           setString(paramInt1, paramObject.toString());
/*      */         }
/* 1141 */         else throw new SQLException("Conversion not supported by setObject!!");
/*      */ 
/*      */         break;
/*      */       case 12:
/* 1147 */         if ((str.equalsIgnoreCase("java.lang.String")) || (str.equalsIgnoreCase("java.math.BigDecimal")) || (str.equalsIgnoreCase("java.lang.Boolean")) || (str.equalsIgnoreCase("java.lang.Integer")) || (str.equalsIgnoreCase("java.lang.Short")) || (str.equalsIgnoreCase("java.lang.Long")) || (str.equalsIgnoreCase("java.lang.Float")) || (str.equalsIgnoreCase("java.lang.Double")) || (str.equalsIgnoreCase("java.sql.Date")) || (str.equalsIgnoreCase("java.sql.Time")) || (str.equalsIgnoreCase("java.sql.Timestamp")))
/*      */         {
/* 1159 */           setChar(paramInt1, paramInt2, 0, paramObject.toString());
/*      */         }
/* 1161 */         else throw new SQLException("Conversion not supported by setObject!!");
/*      */ 
/*      */         break;
/*      */       case -1:
/* 1167 */         if ((str.equalsIgnoreCase("java.lang.String")) || (str.equalsIgnoreCase("java.math.BigDecimal")) || (str.equalsIgnoreCase("java.lang.Boolean")) || (str.equalsIgnoreCase("java.lang.Integer")) || (str.equalsIgnoreCase("java.lang.Short")) || (str.equalsIgnoreCase("java.lang.Long")) || (str.equalsIgnoreCase("java.lang.Float")) || (str.equalsIgnoreCase("java.lang.Double")) || (str.equalsIgnoreCase("java.sql.Date")) || (str.equalsIgnoreCase("java.sql.Time")) || (str.equalsIgnoreCase("java.sql.Timestamp")))
/*      */         {
/* 1179 */           setChar(paramInt1, paramInt2, 0, paramObject.toString());
/*      */         }
/* 1181 */         else throw new SQLException("Conversion not supported by setObject!!");
/*      */ 
/*      */         break;
/*      */       case 2:
/* 1187 */         if ((str.equalsIgnoreCase("java.lang.Integer")) || (str.equalsIgnoreCase("java.lang.Long")) || (str.equalsIgnoreCase("java.lang.Short")))
/*      */         {
/* 1191 */           localObject = new BigDecimal(new BigInteger(paramObject.toString()), 0);
/* 1192 */           localBigDecimal1 = ((BigDecimal)localObject).movePointRight(paramInt3);
/* 1193 */           localBigDecimal2 = localBigDecimal1.movePointLeft(paramInt3);
/*      */ 
/* 1195 */           setBigDecimal(paramInt1, localBigDecimal2);
/* 1196 */         } else if ((str.equalsIgnoreCase("java.lang.Float")) || (str.equalsIgnoreCase("java.lang.Double")) || (str.equalsIgnoreCase("java.lang.String")) || (str.equalsIgnoreCase("java.math.BigDecimal")))
/*      */         {
/* 1200 */           setBigDecimal(paramInt1, new BigDecimal(paramObject.toString()));
/* 1201 */         } else if (str.equalsIgnoreCase("java.lang.Boolean")) {
/* 1202 */           setBigDecimal(paramInt1, new BigDecimal(localBigInteger.toString()));
/*      */         } else {
/* 1204 */           throw new SQLException("Conversion not supported by setObject!!");
/*      */         }
/*      */ 
/*      */         break;
/*      */       case 3:
/* 1210 */         if ((str.equalsIgnoreCase("java.lang.Integer")) || (str.equalsIgnoreCase("java.lang.Long")) || (str.equalsIgnoreCase("java.lang.Short")))
/*      */         {
/* 1214 */           localObject = new BigDecimal(new BigInteger(paramObject.toString()), 0);
/* 1215 */           localBigDecimal1 = ((BigDecimal)localObject).movePointRight(paramInt3);
/* 1216 */           localBigDecimal2 = localBigDecimal1.movePointLeft(paramInt3);
/*      */ 
/* 1218 */           setDecimal(paramInt1, localBigDecimal2);
/* 1219 */         } else if ((str.equalsIgnoreCase("java.lang.Float")) || (str.equalsIgnoreCase("java.lang.Double")) || (str.equalsIgnoreCase("java.lang.String")) || (str.equalsIgnoreCase("java.math.BigDecimal")))
/*      */         {
/* 1223 */           setDecimal(paramInt1, new BigDecimal(paramObject.toString()));
/* 1224 */         } else if (str.equalsIgnoreCase("java.lang.Boolean")) {
/* 1225 */           setDecimal(paramInt1, new BigDecimal(localBigInteger.toString()));
/*      */         } else {
/* 1227 */           throw new SQLException("Conversion not supported by setObject!!");
/*      */         }
/*      */ 
/*      */         break;
/*      */       case -7:
/* 1237 */         if ((str.equalsIgnoreCase("java.lang.String")) || (str.equalsIgnoreCase("java.lang.Boolean")))
/*      */         {
/* 1239 */           if (paramObject.toString().equalsIgnoreCase("true"))
/* 1240 */             setBoolean(paramInt1, true);
/*      */           else
/* 1242 */             setBoolean(paramInt1, false);
/*      */         }
/* 1244 */         else if ((str.equalsIgnoreCase("java.lang.Integer")) || (str.equalsIgnoreCase("java.lang.Long")) || (str.equalsIgnoreCase("java.lang.Short")) || (str.equalsIgnoreCase("java.math.BigDecimal")))
/*      */         {
/* 1248 */           if (paramObject.toString().equalsIgnoreCase("1"))
/* 1249 */             setBoolean(paramInt1, true);
/*      */           else
/* 1251 */             setBoolean(paramInt1, false);
/*      */         }
/* 1253 */         else if (str.equalsIgnoreCase("java.lang.Float")) {
/* 1254 */           if (new Float(0.0F).compareTo((Float)paramObject) == 0)
/* 1255 */             setBoolean(paramInt1, false);
/*      */           else
/* 1257 */             setBoolean(paramInt1, true);
/*      */         }
/* 1259 */         else if (str.equalsIgnoreCase("java.lang.Double")) {
/* 1260 */           if (new Double(0.0D).compareTo((Double)paramObject) == 0)
/* 1261 */             setBoolean(paramInt1, false);
/*      */           else
/* 1263 */             setBoolean(paramInt1, true);
/*      */         }
/*      */         else {
/* 1266 */           throw new SQLException("Conversion not supported by setObject!!");
/*      */         }
/*      */ 
/*      */         break;
/*      */       case -6:
/* 1272 */         if (str.equalsIgnoreCase("java.lang.Float"))
/* 1273 */           setByte(paramInt1, new Float(paramObject.toString()).byteValue());
/* 1274 */         else if (str.equalsIgnoreCase("java.lang.Double"))
/* 1275 */           setByte(paramInt1, new Double(paramObject.toString()).byteValue());
/* 1276 */         else if (str.equalsIgnoreCase("java.lang.Boolean"))
/* 1277 */           setByte(paramInt1, (byte)i);
/* 1278 */         else if ((str.equalsIgnoreCase("java.lang.String")) || (str.equalsIgnoreCase("java.lang.Integer")) || (str.equalsIgnoreCase("java.lang.Short")) || (str.equalsIgnoreCase("java.lang.Long")) || (str.equalsIgnoreCase("java.math.BigDecimal")))
/*      */         {
/* 1283 */           setByte(paramInt1, new Byte(paramObject.toString()).byteValue());
/*      */         }
/* 1285 */         else throw new SQLException("Conversion not supported by setObject!!");
/*      */ 
/*      */         break;
/*      */       case 5:
/* 1291 */         if (str.equalsIgnoreCase("java.lang.Float"))
/* 1292 */           setShort(paramInt1, new Float(paramObject.toString()).shortValue());
/* 1293 */         else if (str.equalsIgnoreCase("java.lang.Double"))
/* 1294 */           setShort(paramInt1, new Double(paramObject.toString()).shortValue());
/* 1295 */         else if (str.equalsIgnoreCase("java.lang.Boolean"))
/* 1296 */           setShort(paramInt1, (short)i);
/* 1297 */         else if ((str.equalsIgnoreCase("java.lang.String")) || (str.equalsIgnoreCase("java.lang.Integer")) || (str.equalsIgnoreCase("java.lang.Short")) || (str.equalsIgnoreCase("java.lang.Long")) || (str.equalsIgnoreCase("java.math.BigDecimal")))
/*      */         {
/* 1302 */           setShort(paramInt1, new Short(paramObject.toString()).shortValue());
/*      */         }
/* 1304 */         else throw new SQLException("Conversion not supported by setObject!!");
/*      */ 
/*      */         break;
/*      */       case 4:
/* 1310 */         if (str.equalsIgnoreCase("java.lang.Float"))
/* 1311 */           setInt(paramInt1, new Float(paramObject.toString()).intValue());
/* 1312 */         else if (str.equalsIgnoreCase("java.lang.Double"))
/* 1313 */           setInt(paramInt1, new Double(paramObject.toString()).intValue());
/* 1314 */         else if (str.equalsIgnoreCase("java.lang.Boolean"))
/* 1315 */           setInt(paramInt1, i);
/* 1316 */         else if ((str.equalsIgnoreCase("java.lang.String")) || (str.equalsIgnoreCase("java.lang.Integer")) || (str.equalsIgnoreCase("java.lang.Short")) || (str.equalsIgnoreCase("java.lang.Long")) || (str.equalsIgnoreCase("java.math.BigDecimal")))
/*      */         {
/* 1321 */           setInt(paramInt1, new Integer(paramObject.toString()).intValue());
/*      */         }
/* 1323 */         else throw new SQLException("Conversion not supported by setObject!!");
/*      */ 
/*      */         break;
/*      */       case -5:
/* 1330 */         if (str.equalsIgnoreCase("java.lang.Float"))
/* 1331 */           setLong(paramInt1, new Float(paramObject.toString()).longValue());
/* 1332 */         else if (str.equalsIgnoreCase("java.lang.Double"))
/* 1333 */           setLong(paramInt1, new Double(paramObject.toString()).longValue());
/* 1334 */         else if (str.equalsIgnoreCase("java.lang.Boolean"))
/* 1335 */           setLong(paramInt1, i);
/* 1336 */         else if ((str.equalsIgnoreCase("java.lang.String")) || (str.equalsIgnoreCase("java.lang.Integer")) || (str.equalsIgnoreCase("java.lang.Short")) || (str.equalsIgnoreCase("java.lang.Long")) || (str.equalsIgnoreCase("java.math.BigDecimal")))
/*      */         {
/* 1341 */           setLong(paramInt1, new Long(paramObject.toString()).longValue());
/*      */         }
/* 1343 */         else throw new SQLException("Conversion not supported by setObject!!");
/*      */ 
/*      */         break;
/*      */       case 6:
/*      */       case 7:
/*      */       case 8:
/* 1350 */         if (str.equalsIgnoreCase("java.lang.Boolean"))
/* 1351 */           setDouble(paramInt1, i);
/* 1352 */         else if ((str.equalsIgnoreCase("java.lang.String")) || (str.equalsIgnoreCase("java.lang.Integer")) || (str.equalsIgnoreCase("java.lang.Short")) || (str.equalsIgnoreCase("java.lang.Long")) || (str.equalsIgnoreCase("java.math.BigDecimal")) || (str.equalsIgnoreCase("java.lang.Float")) || (str.equalsIgnoreCase("java.lang.Double")))
/*      */         {
/* 1359 */           setDouble(paramInt1, new Double(paramObject.toString()).doubleValue());
/*      */         }
/* 1361 */         else throw new SQLException("Conversion not supported by setObject!!");
/*      */ 
/*      */         break;
/*      */       case -2:
/* 1367 */         if (str.equalsIgnoreCase("java.lang.String"))
/* 1368 */           setBytes(paramInt1, ((String)paramObject).getBytes());
/* 1369 */         else if (str.equalsIgnoreCase("byte[]"))
/* 1370 */           setBytes(paramInt1, (byte[])paramObject);
/*      */         else {
/* 1372 */           throw new SQLException("Conversion not supported by setObject!!");
/*      */         }
/*      */ 
/*      */         break;
/*      */       case -4:
/*      */       case -3:
/* 1379 */         localObject = null;
/*      */ 
/* 1381 */         if (str.equalsIgnoreCase("java.lang.String"))
/* 1382 */           localObject = ((String)paramObject).getBytes();
/* 1383 */         else if (str.equalsIgnoreCase("byte[]"))
/* 1384 */           localObject = (byte[])paramObject;
/*      */         else {
/* 1386 */           throw new SQLException("Conversion not supported by setObject!!");
/*      */         }
/*      */ 
/* 1389 */         if (localObject.length > 8000)
/* 1390 */           setBinaryStream(paramInt1, new ByteArrayInputStream((byte[])localObject), localObject.length);
/*      */         else {
/* 1392 */           setBinary(paramInt1, paramInt2, (byte[])localObject);
/*      */         }
/* 1394 */         break;
/*      */       case 91:
/* 1397 */         if (str.equalsIgnoreCase("java.lang.String"))
/* 1398 */           setDate(paramInt1, Date.valueOf(paramObject.toString()));
/* 1399 */         else if (str.equalsIgnoreCase("java.sql.Timestamp"))
/* 1400 */           setDate(paramInt1, new Date(Timestamp.valueOf(paramObject.toString()).getTime()));
/* 1401 */         else if (str.equalsIgnoreCase("java.sql.Date"))
/* 1402 */           setDate(paramInt1, (Date)paramObject);
/*      */         else {
/* 1404 */           throw new SQLException("Conversion not supported by setObject!!");
/*      */         }
/*      */ 
/*      */         break;
/*      */       case 92:
/* 1409 */         if (str.equalsIgnoreCase("java.lang.String"))
/* 1410 */           setTime(paramInt1, Time.valueOf(paramObject.toString()));
/* 1411 */         else if (str.equalsIgnoreCase("java.sql.Timestamp"))
/* 1412 */           setTime(paramInt1, new Time(Timestamp.valueOf(paramObject.toString()).getTime()));
/* 1413 */         else if (str.equalsIgnoreCase("java.sql.Time"))
/* 1414 */           setTime(paramInt1, (Time)paramObject);
/*      */         else {
/* 1416 */           throw new SQLException("Conversion not supported by setObject!!");
/*      */         }
/*      */ 
/*      */         break;
/*      */       case 93:
/* 1421 */         if (str.equalsIgnoreCase("java.lang.String"))
/* 1422 */           setTimestamp(paramInt1, Timestamp.valueOf(paramObject.toString()));
/* 1423 */         else if (str.equalsIgnoreCase("java.sql.Date"))
/* 1424 */           setTimestamp(paramInt1, new Timestamp(Date.valueOf(paramObject.toString()).getTime()));
/* 1425 */         else if (str.equalsIgnoreCase("java.sql.Timestamp"))
/* 1426 */           setTimestamp(paramInt1, (Timestamp)paramObject);
/*      */         else {
/* 1428 */           throw new SQLException("Conversion not supported by setObject!!");
/*      */         }
/*      */ 
/*      */         break;
/*      */       default:
/* 1435 */         throw new SQLException("Unknown SQL Type for PreparedStatement.setObject (SQL Type=" + paramInt2);
/*      */       }
/*      */     } catch (SQLException localSQLException) {
/* 1438 */       throw new SQLException("SQL Exception : " + localSQLException.getMessage());
/*      */     }
/*      */     catch (Exception localException) {
/* 1441 */       throw new SQLException("Unexpected exception : " + localException.getMessage());
/*      */     }
/*      */   }
/*      */ 
/*      */   public void addBatch(String paramString)
/*      */     throws SQLException
/*      */   {
/* 1457 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 1458 */       this.OdbcApi.getTracer().trace("*PreparedStatement.addBatch (" + paramString + ")");
/*      */     }
/* 1460 */     throw new SQLException("Driver does not support this function", "IM001");
/*      */   }
/*      */ 
/*      */   public void clearBatch()
/*      */   {
/* 1470 */     if (this.OdbcApi.getTracer().isTracing())
/*      */     {
/* 1472 */       this.OdbcApi.getTracer().trace("*PreparedStatement.clearBatch");
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/* 1477 */       if (this.batchSqlVec != null)
/*      */       {
/* 1480 */         cleanUpBatch();
/*      */ 
/* 1482 */         this.batchOn = false;
/* 1483 */         this.batchParamsOn = false;
/*      */       }
/*      */ 
/*      */     }
/*      */     catch (Exception localException)
/*      */     {
/* 1491 */       localException.printStackTrace();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void addBatch()
/*      */     throws SQLException
/*      */   {
/* 1504 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 1505 */       this.OdbcApi.getTracer().trace("*PreparedStatement.addBatch");
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/* 1513 */       this.batchSqlVec = this.myConnection.getBatchVector(this);
/*      */       int i;
/* 1517 */       if (this.batchSqlVec == null)
/*      */       {
/* 1519 */         this.batchSqlVec = new Vector(5, 10);
/*      */ 
/* 1521 */         i = 0;
/*      */       }
/*      */       else
/*      */       {
/* 1525 */         i = this.batchSqlVec.size();
/*      */       }
/*      */ 
/* 1528 */       Object[] arrayOfObject = this.arrayParams.getStoredParameterSet();
/* 1529 */       int[] arrayOfInt = this.arrayParams.getStoredIndexSet();
/*      */ 
/* 1531 */       int j = arrayOfObject.length;
/* 1532 */       int k = arrayOfInt.length;
/*      */ 
/* 1534 */       if (k == this.numParams)
/*      */       {
/* 1536 */         this.batchSqlVec.addElement(arrayOfObject);
/*      */ 
/* 1538 */         this.myConnection.setBatchVector(this.batchSqlVec, this);
/*      */ 
/* 1540 */         this.arrayParams.storeRowIndex(i, arrayOfInt);
/*      */ 
/* 1542 */         this.batchOn = true;
/*      */       } else {
/* 1544 */         if (i == 0)
/*      */         {
/* 1546 */           throw new SQLException("Parameter-Set has missing values.");
/*      */         }
/*      */ 
/* 1551 */         this.batchOn = true;
/*      */       }
/*      */ 
/*      */     }
/*      */     catch (NullPointerException localNullPointerException)
/*      */     {
/* 1558 */       this.batchOn = false;
/*      */     }
/*      */   }
/*      */ 
/*      */   public int[] executeBatchUpdate()
/*      */     throws BatchUpdateException
/*      */   {
/* 1572 */     int[] arrayOfInt1 = new int[0];
/*      */ 
/* 1574 */     if (this.numParams <= 0)
/*      */     {
/* 1577 */       this.batchSize = 0;
/* 1578 */       this.batchOn = false;
/* 1579 */       this.batchParamsOn = false;
/* 1580 */       return executeNoParametersBatch();
/*      */     }
/*      */ 
/* 1583 */     this.batchSqlVec = this.myConnection.getBatchVector(this);
/*      */ 
/* 1585 */     if (this.batchSqlVec != null)
/*      */     {
/* 1587 */       this.batchSize = this.batchSqlVec.size();
/*      */     }
/*      */     else
/*      */     {
/* 1592 */       arrayOfInt1 = new int[0];
/* 1593 */       return arrayOfInt1;
/*      */     }
/*      */ 
/* 1596 */     if (this.batchSize > 0)
/*      */     {
/* 1598 */       arrayOfInt1 = new int[this.batchSize];
/*      */ 
/* 1601 */       FreeIntParams();
/*      */ 
/* 1604 */       this.paramStatusArray = new int[this.batchSize];
/* 1605 */       this.paramsProcessed = new int[this.batchSize];
/*      */ 
/* 1607 */       int i = 1;
/* 1608 */       int j = 0;
/*      */       try
/*      */       {
/* 1612 */         if (i == 0)
/*      */         {
/* 1619 */           this.OdbcApi.SQLSetStmtAttr(this.hStmt, 18, 0, 0);
/*      */           try
/*      */           {
/* 1629 */             setStmtParameterSize(this.batchSize);
/* 1630 */             j = getStmtParameterAttr(22);
/*      */           }
/*      */           catch (SQLException localSQLException1)
/*      */           {
/* 1634 */             this.batchSupport = false;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/* 1639 */         if (j != this.batchSize)
/*      */         {
/* 1641 */           this.batchSupport = false;
/*      */           try
/*      */           {
/* 1645 */             setStmtParameterSize(1);
/*      */           }
/*      */           catch (SQLException localSQLException2)
/*      */           {
/*      */           }
/*      */ 
/*      */         }
/*      */         else
/*      */         {
/* 1656 */           this.pA2 = new long[2];
/* 1657 */           this.pA2[0] = 0L;
/* 1658 */           this.pA2[1] = 0L;
/*      */ 
/* 1660 */           this.OdbcApi.SQLSetStmtAttrPtr(this.hStmt, 20, this.paramStatusArray, 0, this.pA2);
/*      */ 
/* 1666 */           this.pA1 = new long[2];
/* 1667 */           this.pA1[0] = 0L;
/* 1668 */           this.pA1[1] = 0L;
/*      */ 
/* 1670 */           this.OdbcApi.SQLSetStmtAttrPtr(this.hStmt, 21, this.paramsProcessed, 0, this.pA1);
/*      */ 
/* 1674 */           this.batchSupport = true;
/*      */         }
/*      */ 
/*      */       }
/*      */       catch (SQLException localSQLException3)
/*      */       {
/* 1682 */         this.batchSupport = false;
/*      */       }
/*      */ 
/* 1685 */       if (this.batchSupport == true)
/*      */       {
/* 1687 */         this.batchParamsOn = true;
/* 1688 */         int[] arrayOfInt2 = new int[0];
/*      */ 
/* 1691 */         this.arrayParams.builtColumWiseParameteSets(this.batchSize, this.batchSqlVec);
/*      */ 
/* 1693 */         for (int k = 0; k < this.numParams; k++)
/*      */         {
/* 1697 */           this.arrayDef = 0;
/* 1698 */           this.arrayScale = 0;
/* 1699 */           int m = 0;
/* 1700 */           int n = k + 1;
/*      */           try
/*      */           {
/* 1705 */             Object[] arrayOfObject = this.arrayParams.getColumnWiseParamSet(n);
/* 1706 */             int[] arrayOfInt3 = this.arrayParams.getColumnWiseIndexArray(n);
/*      */ 
/* 1710 */             setPrecisionScaleArgs(arrayOfObject, arrayOfInt3);
/*      */ 
/* 1712 */             m = getSqlType(n);
/*      */ 
/* 1718 */             bindArrayOfParameters(n, m, this.arrayDef, this.arrayScale, arrayOfObject, arrayOfInt3);
/*      */           }
/*      */           catch (SQLException localSQLException6)
/*      */           {
/* 1726 */             localSQLException6.printStackTrace();
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */         try
/*      */         {
/* 1735 */           if (!execute())
/*      */           {
/* 1737 */             this.paramStatusArray[0] = getUpdateCount();
/*      */ 
/* 1739 */             this.arrayParams.clearStoredRowIndexs();
/*      */ 
/* 1741 */             arrayOfInt1 = this.paramStatusArray;
/*      */ 
/* 1744 */             this.batchOn = false;
/* 1745 */             this.batchParamsOn = false;
/*      */ 
/* 1747 */             cleanUpBatch();
/*      */           }
/*      */           else
/*      */           {
/* 1752 */             cleanUpBatch();
/* 1753 */             throw new JdbcOdbcBatchUpdateException("SQL Attempt to produce a ResultSet from executeBatch", this.paramStatusArray);
/*      */           }
/*      */ 
/*      */         }
/*      */         catch (SQLException localSQLException4)
/*      */         {
/*      */           try
/*      */           {
/* 1761 */             this.paramStatusArray[0] = getUpdateCount();
/*      */           }
/*      */           catch (SQLException localSQLException5)
/*      */           {
/*      */           }
/* 1766 */           arrayOfInt2 = new int[this.paramsProcessed[0] - 1];
/*      */ 
/* 1768 */           cleanUpBatch();
/*      */ 
/* 1770 */           throw new JdbcOdbcBatchUpdateException(localSQLException4.getMessage(), localSQLException4.getSQLState(), arrayOfInt2);
/*      */         }
/*      */ 
/*      */       }
/* 1775 */       else if (!this.batchSupport)
/*      */       {
/* 1780 */         this.batchOn = false;
/* 1781 */         this.batchParamsOn = false;
/* 1782 */         return emulateExecuteBatch();
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1788 */     return arrayOfInt1;
/*      */   }
/*      */ 
/*      */   protected int[] executeNoParametersBatch()
/*      */     throws BatchUpdateException
/*      */   {
/* 1800 */     int[] arrayOfInt = new int[1];
/*      */     try
/*      */     {
/* 1804 */       if (!execute())
/*      */       {
/* 1806 */         cleanUpBatch();
/* 1807 */         arrayOfInt[0] = getUpdateCount();
/*      */       }
/*      */       else
/*      */       {
/* 1811 */         cleanUpBatch();
/* 1812 */         throw new JdbcOdbcBatchUpdateException("SQL Attempt to produce a ResultSet from executeBatch", arrayOfInt);
/*      */       }
/*      */ 
/*      */     }
/*      */     catch (SQLException localSQLException1)
/*      */     {
/*      */       try
/*      */       {
/* 1820 */         arrayOfInt[0] = getUpdateCount();
/*      */       }
/*      */       catch (SQLException localSQLException2) {
/*      */       }
/* 1824 */       cleanUpBatch();
/*      */ 
/* 1826 */       throw new JdbcOdbcBatchUpdateException(localSQLException1.getMessage(), localSQLException1.getSQLState(), arrayOfInt);
/*      */     }
/*      */ 
/* 1829 */     return arrayOfInt;
/*      */   }
/*      */ 
/*      */   protected int getStmtParameterAttr(int paramInt)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/* 1843 */       clearWarnings();
/*      */ 
/* 1845 */       return this.OdbcApi.SQLGetStmtAttr(this.hStmt, paramInt);
/*      */     }
/*      */     catch (SQLException localSQLException)
/*      */     {
/* 1849 */       localSQLException.printStackTrace();
/* 1850 */     }return -1;
/*      */   }
/*      */ 
/*      */   protected void setStmtParameterSize(int paramInt)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/* 1865 */       clearWarnings();
/*      */ 
/* 1867 */       this.OdbcApi.SQLSetStmtAttr(this.hStmt, 22, paramInt, 0);
/*      */     }
/*      */     catch (SQLException localSQLException)
/*      */     {
/* 1871 */       localSQLException.printStackTrace();
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void bindArrayOfParameters(int paramInt1, int paramInt2, int paramInt3, int paramInt4, Object[] paramArrayOfObject, int[] paramArrayOfInt)
/*      */     throws SQLException
/*      */   {
/* 1892 */     switch (paramInt2)
/*      */     {
/*      */     case 1:
/*      */     case 2:
/*      */     case 3:
/*      */     case 12:
/* 1898 */       this.OdbcApi.SQLBindInParameterStringArray(this.hStmt, paramInt1, paramInt2, paramArrayOfObject, paramInt3, paramInt4, paramArrayOfInt);
/*      */ 
/* 1902 */       break;
/*      */     case -1:
/* 1907 */       if (getTypeFromObjectArray(paramArrayOfObject) == -4)
/*      */       {
/* 1910 */         this.arrayParams.setInputStreamElements(paramInt1, paramArrayOfObject);
/*      */ 
/* 1913 */         this.OdbcApi.SQLBindInParameterAtExecArray(this.hStmt, paramInt1, paramInt2, paramInt3, paramArrayOfInt);
/*      */       }
/*      */       else
/*      */       {
/* 1921 */         this.OdbcApi.SQLBindInParameterStringArray(this.hStmt, paramInt1, paramInt2, paramArrayOfObject, paramInt3, paramInt4, paramArrayOfInt);
/*      */       }
/*      */ 
/* 1925 */       break;
/*      */     case -7:
/*      */     case -6:
/*      */     case 4:
/*      */     case 5:
/* 1934 */       this.OdbcApi.SQLBindInParameterIntegerArray(this.hStmt, paramInt1, paramInt2, paramArrayOfObject, paramArrayOfInt);
/*      */ 
/* 1941 */       break;
/*      */     case 8:
/* 1945 */       this.OdbcApi.SQLBindInParameterDoubleArray(this.hStmt, paramInt1, paramInt2, paramArrayOfObject, paramArrayOfInt);
/*      */ 
/* 1951 */       break;
/*      */     case -5:
/*      */     case 6:
/*      */     case 7:
/* 1957 */       this.OdbcApi.SQLBindInParameterFloatArray(this.hStmt, paramInt1, paramInt2, paramArrayOfObject, paramArrayOfInt);
/*      */ 
/* 1964 */       break;
/*      */     case 91:
/* 1968 */       this.OdbcApi.SQLBindInParameterDateArray(this.hStmt, paramInt1, paramArrayOfObject, paramArrayOfInt);
/*      */ 
/* 1973 */       break;
/*      */     case 92:
/* 1977 */       this.OdbcApi.SQLBindInParameterTimeArray(this.hStmt, paramInt1, paramArrayOfObject, paramArrayOfInt);
/*      */ 
/* 1982 */       break;
/*      */     case 93:
/* 1986 */       this.OdbcApi.SQLBindInParameterTimestampArray(this.hStmt, paramInt1, paramArrayOfObject, paramArrayOfInt);
/*      */ 
/* 1991 */       break;
/*      */     case -3:
/*      */     case -2:
/* 1997 */       this.OdbcApi.SQLBindInParameterBinaryArray(this.hStmt, paramInt1, paramInt2, paramArrayOfObject, paramInt3, paramArrayOfInt);
/*      */ 
/* 2002 */       break;
/*      */     case -4:
/* 2007 */       this.arrayParams.setInputStreamElements(paramInt1, paramArrayOfObject);
/*      */ 
/* 2009 */       this.OdbcApi.SQLBindInParameterAtExecArray(this.hStmt, paramInt1, paramInt2, paramInt3, paramArrayOfInt);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected int[] emulateExecuteBatch()
/*      */     throws BatchUpdateException
/*      */   {
/* 2029 */     int[] arrayOfInt1 = new int[this.batchSize];
/*      */ 
/* 2031 */     int[] arrayOfInt2 = new int[0];
/* 2032 */     int i = 0;
/*      */ 
/* 2034 */     for (int j = 0; j < this.batchSize; j++)
/*      */     {
/* 2038 */       Object[] arrayOfObject = (Object[])this.batchSqlVec.elementAt(j);
/*      */ 
/* 2040 */       int[] arrayOfInt3 = this.arrayParams.getStoredRowIndex(j);
/*      */       int n;
/*      */       try
/*      */       {
/* 2044 */         for (int k = 0; k < arrayOfObject.length; k++)
/*      */         {
/* 2046 */           n = 1111;
/* 2047 */           int i1 = 0;
/* 2048 */           int i2 = 0;
/* 2049 */           int i3 = k + 1;
/* 2050 */           InputStream localInputStream = null;
/*      */ 
/* 2052 */           n = getTypeFromObject(arrayOfObject[k]);
/* 2053 */           int i4 = getSqlType(i3);
/*      */ 
/* 2057 */           if (n == -4)
/*      */           {
/* 2059 */             localInputStream = (InputStream)arrayOfObject[k];
/*      */ 
/* 2061 */             i1 = arrayOfInt3[k];
/*      */ 
/* 2063 */             switch (i4)
/*      */             {
/*      */             case -4:
/* 2066 */               i2 = 3;
/* 2067 */               break;
/*      */             case -1:
/* 2070 */               i2 = this.boundParams[k].getStreamType();
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/* 2077 */           if ((i1 > 0) && (0 < i2))
/*      */           {
/* 2080 */             switch (i2)
/*      */             {
/*      */             case 1:
/*      */             case 2:
/* 2085 */               setStream(i3, localInputStream, i1, -1, i2);
/*      */ 
/* 2087 */               break;
/*      */             case 3:
/* 2091 */               setStream(i3, localInputStream, i1, -4, i2);
/*      */             }
/*      */ 
/*      */           }
/* 2099 */           else if (n != 1111)
/*      */           {
/* 2102 */             if (n != 0)
/*      */             {
/* 2104 */               setObject(i3, arrayOfObject[k], i4);
/*      */             }
/*      */             else
/*      */             {
/* 2108 */               setNull(i3, i4);
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */       catch (Exception localException)
/*      */       {
/* 2120 */         localException.printStackTrace();
/*      */       }
/*      */ 
/*      */       try
/*      */       {
/* 2126 */         if (!execute())
/*      */         {
/* 2128 */           this.myConnection.removeBatchVector(this);
/* 2129 */           arrayOfInt1[j] = getUpdateCount();
/* 2130 */           i++;
/*      */         }
/*      */         else
/*      */         {
/* 2143 */           for (int m = 0; m < j - 1; m++)
/*      */           {
/* 2145 */             arrayOfInt2 = new int[i];
/* 2146 */             arrayOfInt2[m] = arrayOfInt1[m];
/*      */           }
/* 2148 */           cleanUpBatch();
/*      */ 
/* 2150 */           throw new JdbcOdbcBatchUpdateException("SQL Attempt to produce a ResultSet from executeBatch", arrayOfInt2);
/*      */         }
/*      */ 
/*      */       }
/*      */       catch (SQLException localSQLException)
/*      */       {
/* 2157 */         for (n = 0; n < j - 1; n++)
/*      */         {
/* 2159 */           arrayOfInt2 = new int[i];
/* 2160 */           arrayOfInt2[n] = arrayOfInt1[n];
/*      */         }
/* 2162 */         cleanUpBatch();
/*      */ 
/* 2164 */         throw new JdbcOdbcBatchUpdateException(localSQLException.getMessage(), localSQLException.getSQLState(), arrayOfInt2);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2171 */     cleanUpBatch();
/*      */ 
/* 2173 */     return arrayOfInt1;
/*      */   }
/*      */ 
/*      */   protected void cleanUpBatch()
/*      */   {
/* 2188 */     this.myConnection.removeBatchVector(this);
/*      */ 
/* 2190 */     if (this.batchSqlVec != null)
/*      */     {
/* 2192 */       this.batchSqlVec.setSize(0);
/* 2193 */       this.batchSize = 0;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void setPrecisionScaleArgs(Object[] paramArrayOfObject, int[] paramArrayOfInt)
/*      */   {
/* 2207 */     int i = getTypeFromObjectArray(paramArrayOfObject);
/*      */ 
/* 2209 */     for (int j = 0; j < this.batchSize; j++)
/*      */     {
/* 2212 */       byte[] arrayOfByte = null;
/* 2213 */       String str = null;
/* 2214 */       BigDecimal localBigDecimal = null;
/* 2215 */       int k = 0;
/*      */       try
/*      */       {
/* 2219 */         if ((i == 3) || (i == 2))
/*      */         {
/* 2222 */           if (paramArrayOfObject[j] != null)
/*      */           {
/* 2224 */             int m = 0;
/* 2225 */             localBigDecimal = (BigDecimal)paramArrayOfObject[j];
/* 2226 */             str = localBigDecimal.toString();
/* 2227 */             k = str.indexOf('.');
/*      */ 
/* 2229 */             if (k == -1)
/*      */             {
/* 2231 */               k = str.length();
/*      */             }
/*      */             else
/*      */             {
/* 2235 */               m = localBigDecimal.scale();
/* 2236 */               k += m + 1;
/*      */             }
/*      */ 
/* 2244 */             if (m > this.arrayScale)
/*      */             {
/* 2246 */               this.arrayScale = m;
/*      */             }
/*      */           }
/*      */         }
/* 2250 */         else if ((i == 1) || (i == 12))
/*      */         {
/* 2253 */           if (paramArrayOfObject[j] != null)
/*      */           {
/* 2255 */             str = (String)paramArrayOfObject[j];
/* 2256 */             k = str.length();
/*      */           }
/*      */         }
/* 2259 */         else if (i == -4)
/*      */         {
/* 2262 */           if (paramArrayOfInt[j] > this.arrayDef)
/*      */           {
/* 2264 */             this.arrayDef = paramArrayOfInt[j];
/*      */           }
/*      */ 
/*      */         }
/* 2268 */         else if ((i == -2) || (i == -3))
/*      */         {
/* 2271 */           if (paramArrayOfObject[j] != null)
/*      */           {
/* 2273 */             arrayOfByte = (byte[])paramArrayOfObject[j];
/* 2274 */             k = arrayOfByte.length;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/* 2279 */         if (k > this.arrayDef)
/*      */         {
/* 2281 */           this.arrayDef = k;
/*      */         }
/*      */ 
/*      */       }
/*      */       catch (Exception localException)
/*      */       {
/* 2288 */         localException.printStackTrace();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void setSqlType(int paramInt1, int paramInt2)
/*      */   {
/* 2309 */     if ((paramInt1 >= 1) && (paramInt1 <= this.numParams))
/*      */     {
/* 2311 */       this.boundParams[(paramInt1 - 1)].setSqlType(paramInt2);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected int getSqlType(int paramInt)
/*      */   {
/* 2324 */     int i = 1111;
/*      */ 
/* 2328 */     if ((paramInt >= 1) && (paramInt <= this.numParams))
/*      */     {
/* 2330 */       i = this.boundParams[(paramInt - 1)].getSqlType();
/*      */     }
/* 2332 */     return i;
/*      */   }
/*      */ 
/*      */   public void setCharacterStream(int paramInt1, Reader paramReader, int paramInt2)
/*      */     throws SQLException
/*      */   {
/* 2343 */     clearParameter(paramInt1);
/*      */ 
/* 2347 */     BufferedReader localBufferedReader = null;
/* 2348 */     BufferedOutputStream localBufferedOutputStream = null;
/*      */ 
/* 2351 */     ByteArrayOutputStream localByteArrayOutputStream = null;
/* 2352 */     ByteArrayInputStream localByteArrayInputStream = null;
/*      */ 
/* 2355 */     String str = this.OdbcApi.charSet;
/*      */ 
/* 2358 */     int i = 300;
/*      */ 
/* 2360 */     if (paramInt2 < i) {
/* 2361 */       i = paramInt2;
/*      */     }
/* 2363 */     int j = 0;
/* 2364 */     int k = 0;
/*      */     try
/*      */     {
/* 2369 */       k = (int)Charset.forName(str).newEncoder().maxBytesPerChar();
/*      */     }
/*      */     catch (UnsupportedCharsetException localUnsupportedCharsetException)
/*      */     {
/*      */     }
/*      */     catch (IllegalCharsetNameException localIllegalCharsetNameException)
/*      */     {
/*      */     }
/* 2377 */     if (k == 0) {
/* 2378 */       k = 1;
/*      */     }
/*      */     try
/*      */     {
/* 2382 */       if (paramReader != null)
/*      */       {
/* 2384 */         int m = 0;
/* 2385 */         int n = 0;
/*      */ 
/* 2387 */         localBufferedReader = new BufferedReader(paramReader);
/* 2388 */         localByteArrayOutputStream = new ByteArrayOutputStream();
/* 2389 */         localBufferedOutputStream = new BufferedOutputStream(localByteArrayOutputStream);
/*      */ 
/* 2391 */         char[] arrayOfChar1 = new char[i];
/*      */ 
/* 2393 */         while (n != -1)
/*      */         {
/* 2395 */           byte[] arrayOfByte = new byte[0];
/*      */ 
/* 2397 */           n = localBufferedReader.read(arrayOfChar1);
/*      */ 
/* 2399 */           if (n != -1)
/*      */           {
/* 2406 */             char[] arrayOfChar2 = new char[n];
/*      */ 
/* 2408 */             for (int i1 = 0; i1 < n; i1++)
/*      */             {
/* 2410 */               arrayOfChar2[i1] = arrayOfChar1[i1];
/*      */             }
/*      */ 
/* 2423 */             arrayOfByte = CharsToBytes(str, arrayOfChar2);
/*      */ 
/* 2426 */             i1 = arrayOfByte.length - 1;
/*      */ 
/* 2429 */             localBufferedOutputStream.write(arrayOfByte, 0, i1);
/*      */ 
/* 2432 */             localBufferedOutputStream.flush();
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/* 2441 */         j = localByteArrayOutputStream.size();
/*      */ 
/* 2447 */         localByteArrayInputStream = new ByteArrayInputStream(localByteArrayOutputStream.toByteArray());
/*      */       }
/*      */ 
/*      */     }
/*      */     catch (IOException localIOException)
/*      */     {
/* 2454 */       throw new SQLException("CharsToBytes Reader Conversion: " + localIOException.getMessage());
/*      */     }
/*      */ 
/* 2460 */     setStream(paramInt1, localByteArrayInputStream, j, -1, 3);
/*      */   }
/*      */ 
/*      */   public void setRef(int paramInt, Ref paramRef)
/*      */     throws SQLException
/*      */   {
/* 2470 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void setBlob(int paramInt, Blob paramBlob)
/*      */     throws SQLException
/*      */   {
/* 2478 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void setClob(int paramInt, Clob paramClob)
/*      */     throws SQLException
/*      */   {
/* 2486 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void setArray(int paramInt, Array paramArray)
/*      */     throws SQLException
/*      */   {
/* 2494 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public ResultSetMetaData getMetaData()
/*      */     throws SQLException
/*      */   {
/* 2500 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 2501 */       this.OdbcApi.getTracer().trace("*PreparedStatement.getMetaData");
/*      */     }
/*      */ 
/* 2504 */     JdbcOdbcResultSet localJdbcOdbcResultSet = null;
/*      */ 
/* 2508 */     if (this.hStmt == 0L) {
/* 2509 */       throw new SQLException("Statement is closed");
/*      */     }
/*      */ 
/* 2514 */     localJdbcOdbcResultSet = new JdbcOdbcResultSet();
/* 2515 */     localJdbcOdbcResultSet.initialize(this.OdbcApi, this.hDbc, this.hStmt, true, null);
/*      */ 
/* 2517 */     return new JdbcOdbcResultSetMetaData(this.OdbcApi, localJdbcOdbcResultSet);
/*      */   }
/*      */ 
/*      */   public void setDate(int paramInt, Date paramDate, Calendar paramCalendar)
/*      */     throws SQLException
/*      */   {
/* 2535 */     if (paramDate == null)
/*      */     {
/* 2537 */       setNull(paramInt, 91);
/* 2538 */       return;
/*      */     }
/*      */ 
/* 2544 */     long l = this.utils.convertToGMT(paramDate, paramCalendar);
/* 2545 */     paramDate = new Date(l);
/* 2546 */     paramCalendar = Calendar.getInstance();
/*      */ 
/* 2549 */     paramCalendar.setTime(paramDate);
/* 2550 */     clearParameter(paramInt);
/*      */ 
/* 2552 */     setInputParameter(paramInt, true);
/*      */ 
/* 2557 */     byte[] arrayOfByte = allocBindBuf(paramInt, 32);
/*      */ 
/* 2559 */     long[] arrayOfLong = new long[2];
/* 2560 */     arrayOfLong[0] = 0L;
/* 2561 */     arrayOfLong[1] = 0L;
/*      */ 
/* 2563 */     if (!this.batchOn)
/*      */     {
/* 2565 */       this.OdbcApi.SQLBindInParameterCalendarDate(this.hStmt, paramInt, paramCalendar, arrayOfByte, arrayOfLong);
/*      */     }
/*      */ 
/* 2570 */     this.boundParams[(paramInt - 1)].pA1 = arrayOfLong[0];
/* 2571 */     this.boundParams[(paramInt - 1)].pA2 = arrayOfLong[1];
/* 2572 */     this.boundParams[(paramInt - 1)].boundType = 91;
/* 2573 */     this.boundParams[(paramInt - 1)].boundValue = paramDate;
/*      */ 
/* 2576 */     this.arrayParams.storeValue(paramInt - 1, paramCalendar, -3);
/* 2577 */     setSqlType(paramInt, 91);
/*      */   }
/*      */ 
/*      */   public void setTime(int paramInt, Time paramTime, Calendar paramCalendar)
/*      */     throws SQLException
/*      */   {
/* 2596 */     if (paramTime == null)
/*      */     {
/* 2598 */       setNull(paramInt, 92);
/* 2599 */       return;
/*      */     }
/*      */ 
/* 2605 */     long l = this.utils.convertToGMT(paramTime, paramCalendar);
/* 2606 */     paramTime = new Time(l);
/* 2607 */     paramCalendar = Calendar.getInstance();
/*      */ 
/* 2610 */     paramCalendar.setTime(paramTime);
/* 2611 */     clearParameter(paramInt);
/*      */ 
/* 2613 */     setInputParameter(paramInt, true);
/*      */ 
/* 2618 */     byte[] arrayOfByte = allocBindBuf(paramInt, 32);
/*      */ 
/* 2620 */     long[] arrayOfLong = new long[2];
/* 2621 */     arrayOfLong[0] = 0L;
/* 2622 */     arrayOfLong[1] = 0L;
/*      */ 
/* 2624 */     if (!this.batchOn)
/*      */     {
/* 2626 */       this.OdbcApi.SQLBindInParameterCalendarTime(this.hStmt, paramInt, paramCalendar, arrayOfByte, arrayOfLong);
/*      */     }
/*      */ 
/* 2631 */     this.boundParams[(paramInt - 1)].pA1 = arrayOfLong[0];
/* 2632 */     this.boundParams[(paramInt - 1)].pA2 = arrayOfLong[1];
/* 2633 */     this.boundParams[(paramInt - 1)].boundType = 92;
/* 2634 */     this.boundParams[(paramInt - 1)].boundValue = paramTime;
/*      */ 
/* 2637 */     this.arrayParams.storeValue(paramInt - 1, paramCalendar, -3);
/* 2638 */     setSqlType(paramInt, 92);
/*      */   }
/*      */ 
/*      */   public void setTimestamp(int paramInt, Timestamp paramTimestamp, Calendar paramCalendar)
/*      */     throws SQLException
/*      */   {
/* 2655 */     if (paramTimestamp == null)
/*      */     {
/* 2657 */       setNull(paramInt, 93);
/* 2658 */       return;
/*      */     }
/*      */ 
/* 2664 */     long l = this.utils.convertToGMT(paramTimestamp, paramCalendar);
/* 2665 */     paramTimestamp = new Timestamp(l);
/* 2666 */     paramCalendar = Calendar.getInstance();
/*      */ 
/* 2669 */     paramCalendar.setTime(paramTimestamp);
/* 2670 */     clearParameter(paramInt);
/*      */ 
/* 2672 */     setInputParameter(paramInt, true);
/*      */ 
/* 2677 */     byte[] arrayOfByte = allocBindBuf(paramInt, 32);
/*      */ 
/* 2680 */     long[] arrayOfLong = new long[2];
/* 2681 */     arrayOfLong[0] = 0L;
/* 2682 */     arrayOfLong[1] = 0L;
/*      */ 
/* 2684 */     if (!this.batchOn)
/*      */     {
/* 2686 */       this.OdbcApi.SQLBindInParameterCalendarTimestamp(this.hStmt, paramInt, paramCalendar, arrayOfByte, arrayOfLong);
/*      */     }
/*      */ 
/* 2691 */     this.boundParams[(paramInt - 1)].pA1 = arrayOfLong[0];
/* 2692 */     this.boundParams[(paramInt - 1)].pA2 = arrayOfLong[1];
/* 2693 */     this.boundParams[(paramInt - 1)].boundType = 93;
/* 2694 */     this.boundParams[(paramInt - 1)].boundValue = paramTimestamp;
/*      */ 
/* 2697 */     this.arrayParams.storeValue(paramInt - 1, paramCalendar, -3);
/* 2698 */     setSqlType(paramInt, 93);
/*      */   }
/*      */ 
/*      */   public void setNull(int paramInt1, int paramInt2, String paramString)
/*      */     throws SQLException
/*      */   {
/* 2708 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void initBoundParam()
/*      */     throws SQLException
/*      */   {
/* 2722 */     this.numParams = this.OdbcApi.SQLNumParams(this.hStmt);
/*      */ 
/* 2727 */     if (this.numParams > 0)
/*      */     {
/* 2731 */       this.boundParams = new JdbcOdbcBoundParam[this.numParams];
/*      */ 
/* 2735 */       for (int i = 0; i < this.numParams; i++) {
/* 2736 */         this.boundParams[i] = new JdbcOdbcBoundParam();
/* 2737 */         this.boundParams[i].initialize();
/*      */       }
/*      */ 
/* 2742 */       this.arrayParams = new JdbcOdbcBoundArrayOfParams(this.numParams);
/*      */ 
/* 2749 */       this.batchRCFlag = this.myConnection.getBatchRowCountFlag(1);
/*      */ 
/* 2752 */       if ((this.batchRCFlag > 0) && (this.batchRCFlag == 1))
/*      */       {
/* 2754 */         this.batchSupport = true;
/*      */       }
/*      */       else
/*      */       {
/* 2758 */         this.batchSupport = false;
/*      */       }
/* 2760 */       this.StringDef = 0;
/* 2761 */       this.NumberDef = 0;
/* 2762 */       this.NumberDef = 0;
/* 2763 */       this.binaryPrec = 0;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected byte[] allocBindBuf(int paramInt1, int paramInt2)
/*      */   {
/* 2782 */     byte[] arrayOfByte = null;
/*      */ 
/* 2786 */     if ((paramInt1 >= 1) && (paramInt1 <= this.numParams))
/*      */     {
/* 2788 */       arrayOfByte = this.boundParams[(paramInt1 - 1)].allocBindDataBuffer(paramInt2);
/*      */     }
/*      */ 
/* 2792 */     return arrayOfByte;
/*      */   }
/*      */ 
/*      */   protected byte[] getDataBuf(int paramInt)
/*      */   {
/* 2803 */     byte[] arrayOfByte = null;
/*      */ 
/* 2807 */     if ((paramInt >= 1) && (paramInt <= this.numParams))
/*      */     {
/* 2809 */       arrayOfByte = this.boundParams[(paramInt - 1)].getBindDataBuffer();
/*      */     }
/*      */ 
/* 2812 */     return arrayOfByte;
/*      */   }
/*      */ 
/*      */   protected byte[] getLengthBuf(int paramInt)
/*      */   {
/* 2823 */     byte[] arrayOfByte = null;
/*      */ 
/* 2827 */     if ((paramInt >= 1) && (paramInt <= this.numParams))
/*      */     {
/* 2829 */       arrayOfByte = this.boundParams[(paramInt - 1)].getBindLengthBuffer();
/*      */     }
/*      */ 
/* 2832 */     return arrayOfByte;
/*      */   }
/*      */ 
/*      */   public int getParamLength(int paramInt)
/*      */   {
/* 2847 */     int i = -1;
/*      */ 
/* 2851 */     if ((paramInt >= 1) && (paramInt <= this.numParams))
/*      */     {
/* 2856 */       i = this.OdbcApi.bufferToInt(this.boundParams[(paramInt - 1)].getBindLengthBuffer());
/*      */     }
/*      */ 
/* 2859 */     return i;
/*      */   }
/*      */ 
/*      */   protected void putParamData(int paramInt)
/*      */     throws SQLException, JdbcOdbcSQLWarning
/*      */   {
/* 2873 */     int i = 2000;
/*      */ 
/* 2876 */     byte[] arrayOfByte = new byte[i];
/* 2877 */     int m = 0;
/*      */ 
/* 2881 */     if ((paramInt < 1) || (paramInt > this.numParams))
/*      */     {
/* 2884 */       if (this.OdbcApi.getTracer().isTracing()) {
/* 2885 */         this.OdbcApi.getTracer().trace("Invalid index for putParamData()");
/*      */       }
/* 2887 */       return;
/*      */     }
/*      */ 
/* 2893 */     InputStream localInputStream = this.boundParams[(paramInt - 1)].getInputStream();
/*      */ 
/* 2895 */     int n = this.boundParams[(paramInt - 1)].getInputStreamLen();
/* 2896 */     int i1 = this.boundParams[(paramInt - 1)].getStreamType();
/*      */ 
/* 2900 */     while (m == 0)
/*      */     {
/*      */       int j;
/*      */       try
/*      */       {
/* 2905 */         if (this.OdbcApi.getTracer().isTracing()) {
/* 2906 */           this.OdbcApi.getTracer().trace("Reading from input stream");
/*      */         }
/* 2908 */         j = localInputStream.read(arrayOfByte);
/* 2909 */         if (this.OdbcApi.getTracer().isTracing()) {
/* 2910 */           this.OdbcApi.getTracer().trace("Bytes read: " + j);
/*      */         }
/*      */ 
/*      */       }
/*      */       catch (IOException localIOException)
/*      */       {
/* 2918 */         throw new SQLException(localIOException.getMessage());
/*      */       }
/*      */ 
/* 2924 */       if (j == -1)
/*      */       {
/* 2929 */         if (n != 0) {
/* 2930 */           throw new SQLException("End of InputStream reached before satisfying length specified when InputStream was set");
/*      */         }
/* 2932 */         m = 1;
/* 2933 */         break;
/*      */       }
/*      */ 
/* 2940 */       if (j > n) {
/* 2941 */         j = n;
/* 2942 */         m = 1;
/*      */       }
/*      */ 
/* 2945 */       int k = j;
/*      */ 
/* 2952 */       if (i1 == 2) {
/* 2953 */         k = j / 2;
/*      */ 
/* 2955 */         for (int i2 = 0; i2 < k; i2++) {
/* 2956 */           arrayOfByte[i2] = arrayOfByte[(i2 * 2 + 1)];
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 2962 */       this.OdbcApi.SQLPutData(this.hStmt, arrayOfByte, k);
/*      */ 
/* 2966 */       n -= j;
/*      */ 
/* 2968 */       if (this.OdbcApi.getTracer().isTracing()) {
/* 2969 */         this.OdbcApi.getTracer().trace("" + n + " bytes remaining");
/*      */       }
/*      */ 
/* 2974 */       if (n == 0)
/* 2975 */         m = 1;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setStream(int paramInt1, InputStream paramInputStream, int paramInt2, int paramInt3, int paramInt4)
/*      */     throws SQLException
/*      */   {
/* 2993 */     clearParameter(paramInt1);
/*      */ 
/* 2995 */     setInputParameter(paramInt1, true);
/*      */ 
/* 2998 */     byte[] arrayOfByte1 = getLengthBuf(paramInt1);
/*      */ 
/* 3004 */     byte[] arrayOfByte2 = allocBindBuf(paramInt1, 4);
/*      */ 
/* 3008 */     long[] arrayOfLong = new long[4];
/*      */ 
/* 3010 */     arrayOfLong[0] = 0L;
/* 3011 */     arrayOfLong[1] = 0L;
/* 3012 */     arrayOfLong[2] = 0L;
/* 3013 */     arrayOfLong[3] = 0L;
/*      */ 
/* 3015 */     if (!this.batchOn)
/*      */     {
/* 3017 */       this.OdbcApi.SQLBindInParameterAtExec(this.hStmt, paramInt1, paramInt3, paramInt2, arrayOfByte2, arrayOfByte1, arrayOfLong);
/*      */     }
/*      */ 
/* 3022 */     this.boundParams[(paramInt1 - 1)].pA1 = arrayOfLong[0];
/* 3023 */     this.boundParams[(paramInt1 - 1)].pA2 = arrayOfLong[1];
/* 3024 */     this.boundParams[(paramInt1 - 1)].pB1 = arrayOfLong[2];
/* 3025 */     this.boundParams[(paramInt1 - 1)].pB2 = arrayOfLong[3];
/* 3026 */     this.boundParams[(paramInt1 - 1)].boundType = paramInt3;
/* 3027 */     this.boundParams[(paramInt1 - 1)].boundValue = paramInputStream;
/*      */ 
/* 3032 */     this.boundParams[(paramInt1 - 1)].setInputStream(paramInputStream, paramInt2);
/*      */ 
/* 3036 */     this.boundParams[(paramInt1 - 1)].setStreamType(paramInt4);
/*      */ 
/* 3039 */     this.arrayParams.storeValue(paramInt1 - 1, paramInputStream, paramInt2);
/* 3040 */     setSqlType(paramInt1, paramInt3);
/*      */   }
/*      */ 
/*      */   protected void setChar(int paramInt1, int paramInt2, int paramInt3, String paramString)
/*      */     throws SQLException
/*      */   {
/* 3056 */     clearParameter(paramInt1);
/*      */ 
/* 3058 */     setInputParameter(paramInt1, true);
/*      */ 
/* 3063 */     int i = 0;
/* 3064 */     int j = 0;
/*      */ 
/* 3066 */     char[] arrayOfChar = paramString.toCharArray();
/* 3067 */     byte[] arrayOfByte1 = new byte[0];
/*      */     try
/*      */     {
/* 3070 */       arrayOfByte1 = CharsToBytes(this.OdbcApi.charSet, arrayOfChar);
/*      */     }
/*      */     catch (UnsupportedEncodingException localUnsupportedEncodingException) {
/*      */     }
/* 3074 */     byte[] arrayOfByte2 = allocBindBuf(paramInt1, arrayOfByte1.length);
/*      */ 
/* 3081 */     i = getPrecision(paramInt2);
/*      */ 
/* 3110 */     if ((i < 0) || (i > 8000))
/*      */     {
/* 3115 */       i = arrayOfByte1.length;
/*      */     }
/*      */ 
/* 3119 */     long[] arrayOfLong = new long[4];
/*      */ 
/* 3121 */     arrayOfLong[0] = 0L;
/* 3122 */     arrayOfLong[1] = 0L;
/* 3123 */     arrayOfLong[2] = 0L;
/* 3124 */     arrayOfLong[3] = 0L;
/*      */ 
/* 3126 */     if (!this.batchOn)
/*      */     {
/* 3128 */       this.OdbcApi.SQLBindInParameterString(this.hStmt, paramInt1, paramInt2, i, paramInt3, arrayOfByte1, arrayOfByte2, arrayOfLong);
/*      */     }
/*      */ 
/* 3133 */     this.boundParams[(paramInt1 - 1)].pA1 = arrayOfLong[0];
/* 3134 */     this.boundParams[(paramInt1 - 1)].pA2 = arrayOfLong[1];
/* 3135 */     this.boundParams[(paramInt1 - 1)].pB1 = arrayOfLong[2];
/* 3136 */     this.boundParams[(paramInt1 - 1)].pB2 = arrayOfLong[3];
/* 3137 */     this.boundParams[(paramInt1 - 1)].scale = paramInt3;
/* 3138 */     this.boundParams[(paramInt1 - 1)].boundType = paramInt2;
/* 3139 */     this.boundParams[(paramInt1 - 1)].boundValue = paramString;
/*      */ 
/* 3144 */     if ((paramInt2 == 2) || (paramInt2 == 3))
/*      */     {
/* 3146 */       this.arrayParams.storeValue(paramInt1 - 1, new BigDecimal(paramString.trim()), -3);
/*      */ 
/* 3148 */       this.NumberDef = i;
/*      */ 
/* 3150 */       if (paramInt3 > this.NumberScale) {
/* 3151 */         this.NumberScale = paramInt3;
/*      */       }
/*      */ 
/*      */     }
/* 3155 */     else if (paramInt2 == -5)
/*      */     {
/* 3157 */       this.arrayParams.storeValue(paramInt1 - 1, new BigInteger(paramString.trim()), -3);
/*      */ 
/* 3159 */       this.NumberDef = i;
/*      */ 
/* 3161 */       if (paramInt3 > this.NumberScale) {
/* 3162 */         this.NumberScale = paramInt3;
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/* 3167 */       this.arrayParams.storeValue(paramInt1 - 1, paramString, -3);
/* 3168 */       this.StringDef = i;
/*      */     }
/*      */ 
/* 3171 */     setSqlType(paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   protected void setBinary(int paramInt1, int paramInt2, byte[] paramArrayOfByte)
/*      */     throws SQLException
/*      */   {
/* 3186 */     clearParameter(paramInt1);
/*      */ 
/* 3188 */     setInputParameter(paramInt1, true);
/*      */ 
/* 3194 */     byte[] arrayOfByte1 = allocBindBuf(paramInt1, paramArrayOfByte.length);
/*      */ 
/* 3199 */     byte[] arrayOfByte2 = getLengthBuf(paramInt1);
/*      */ 
/* 3201 */     long[] arrayOfLong = new long[6];
/*      */ 
/* 3203 */     arrayOfLong[0] = 0L;
/* 3204 */     arrayOfLong[1] = 0L;
/* 3205 */     arrayOfLong[2] = 0L;
/* 3206 */     arrayOfLong[3] = 0L;
/* 3207 */     arrayOfLong[4] = 0L;
/* 3208 */     arrayOfLong[5] = 0L;
/*      */ 
/* 3210 */     if (!this.batchOn)
/*      */     {
/* 3212 */       this.OdbcApi.SQLBindInParameterBinary(this.hStmt, paramInt1, paramInt2, paramArrayOfByte, arrayOfByte1, arrayOfByte2, arrayOfLong);
/*      */     }
/*      */ 
/* 3217 */     this.boundParams[(paramInt1 - 1)].pA1 = arrayOfLong[0];
/* 3218 */     this.boundParams[(paramInt1 - 1)].pA2 = arrayOfLong[1];
/* 3219 */     this.boundParams[(paramInt1 - 1)].pB1 = arrayOfLong[2];
/* 3220 */     this.boundParams[(paramInt1 - 1)].pB2 = arrayOfLong[3];
/* 3221 */     this.boundParams[(paramInt1 - 1)].pC1 = arrayOfLong[4];
/* 3222 */     this.boundParams[(paramInt1 - 1)].pC2 = arrayOfLong[5];
/* 3223 */     this.boundParams[(paramInt1 - 1)].boundType = paramInt2;
/* 3224 */     this.boundParams[(paramInt1 - 1)].boundValue = paramArrayOfByte;
/*      */ 
/* 3226 */     this.binaryPrec = paramArrayOfByte.length;
/*      */ 
/* 3228 */     this.arrayParams.storeValue(paramInt1 - 1, (byte[])paramArrayOfByte, -3);
/* 3229 */     setSqlType(paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   protected int getTypeFromObjectArray(Object[] paramArrayOfObject)
/*      */   {
/* 3241 */     int i = 1111;
/*      */ 
/* 3243 */     for (int j = 0; j < this.batchSize; j++)
/*      */     {
/* 3245 */       i = getTypeFromObject(paramArrayOfObject[j]);
/*      */ 
/* 3247 */       if (i != 0)
/*      */       {
/*      */         break;
/*      */       }
/*      */     }
/*      */ 
/* 3253 */     return i;
/*      */   }
/*      */ 
/*      */   public synchronized void close()
/*      */     throws SQLException
/*      */   {
/* 3261 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 3262 */       this.OdbcApi.getTracer().trace("*PreparedStatement.close");
/*      */     }
/*      */ 
/* 3267 */     clearMyResultSet();
/*      */     try
/*      */     {
/* 3272 */       clearWarnings();
/* 3273 */       if (this.hStmt != 0L)
/*      */       {
/* 3275 */         if (this.closeCalledFromFinalize == true) {
/* 3276 */           if (!this.myConnection.isFreeStmtsFromConnectionOnly()) {
/* 3277 */             this.OdbcApi.SQLFreeStmt(this.hStmt, 1);
/*      */           }
/*      */         }
/*      */         else {
/* 3281 */           this.OdbcApi.SQLFreeStmt(this.hStmt, 1);
/*      */         }
/* 3283 */         this.hStmt = 0L;
/* 3284 */         FreeParams();
/* 3285 */         for (int i = 1; (this.boundParams != null) && (i <= this.boundParams.length); i++)
/*      */         {
/* 3287 */           this.boundParams[(i - 1)].binaryData = null;
/* 3288 */           this.boundParams[(i - 1)].initialize();
/* 3289 */           this.boundParams[(i - 1)].paramInputStream = null;
/* 3290 */           this.boundParams[(i - 1)].inputParameter = false;
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */     catch (SQLException localSQLException)
/*      */     {
/*      */     }
/*      */ 
/* 3299 */     FreeIntParams();
/*      */ 
/* 3304 */     this.myConnection.deregisterStatement(this);
/*      */ 
/* 3306 */     if (this.batchOn)
/* 3307 */       clearBatch();
/*      */   }
/*      */ 
/*      */   public synchronized void FreeIntParams()
/*      */   {
/* 3314 */     if (this.pA1 != null)
/*      */     {
/* 3316 */       if (this.pA1[0] != 0L)
/*      */       {
/* 3318 */         JdbcOdbc.ReleaseStoredIntegers(this.pA1[0], this.pA1[1]);
/* 3319 */         this.pA1[0] = 0L;
/* 3320 */         this.pA1[1] = 0L;
/*      */       }
/*      */     }
/*      */ 
/* 3324 */     if (this.pA2 != null)
/*      */     {
/* 3326 */       if (this.pA2[0] != 0L)
/*      */       {
/* 3328 */         JdbcOdbc.ReleaseStoredIntegers(this.pA2[0], this.pA2[1]);
/* 3329 */         this.pA2[0] = 0L;
/* 3330 */         this.pA2[1] = 0L;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public synchronized void FreeParams()
/*      */     throws NullPointerException
/*      */   {
/*      */     try
/*      */     {
/* 3340 */       for (int i = 1; i <= this.boundParams.length; i++)
/*      */       {
/* 3342 */         if (this.boundParams[(i - 1)].pA1 != 0L)
/*      */         {
/* 3344 */           JdbcOdbc.ReleaseStoredBytes(this.boundParams[(i - 1)].pA1, this.boundParams[(i - 1)].pA2);
/* 3345 */           this.boundParams[(i - 1)].pA1 = 0L;
/* 3346 */           this.boundParams[(i - 1)].pA2 = 0L;
/*      */         }
/* 3348 */         if (this.boundParams[(i - 1)].pB1 != 0L)
/*      */         {
/* 3350 */           JdbcOdbc.ReleaseStoredBytes(this.boundParams[(i - 1)].pB1, this.boundParams[(i - 1)].pB2);
/* 3351 */           this.boundParams[(i - 1)].pB1 = 0L;
/* 3352 */           this.boundParams[(i - 1)].pB2 = 0L;
/*      */         }
/* 3354 */         if (this.boundParams[(i - 1)].pC1 != 0L)
/*      */         {
/* 3356 */           JdbcOdbc.ReleaseStoredBytes(this.boundParams[(i - 1)].pC1, this.boundParams[(i - 1)].pC2);
/* 3357 */           this.boundParams[(i - 1)].pC1 = 0L;
/* 3358 */           this.boundParams[(i - 1)].pC2 = 0L;
/*      */         }
/* 3360 */         if (this.boundParams[(i - 1)].pS1 != 0L)
/*      */         {
/* 3362 */           JdbcOdbc.ReleaseStoredChars(this.boundParams[(i - 1)].pS1, this.boundParams[(i - 1)].pS2);
/* 3363 */           this.boundParams[(i - 1)].pS1 = 0L;
/* 3364 */           this.boundParams[(i - 1)].pS2 = 0L;
/*      */         }
/*      */       }
/*      */     }
/*      */     catch (NullPointerException localNullPointerException)
/*      */     {
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setSql(String paramString)
/*      */   {
/* 3381 */     this.mySql = paramString.toUpperCase();
/*      */   }
/*      */ 
/*      */   public Object[] getObjects()
/*      */   {
/* 3392 */     Object[] arrayOfObject1 = new Object[this.numParams];
/*      */ 
/* 3394 */     Object[] arrayOfObject2 = this.arrayParams.getStoredParameterSet();
/*      */ 
/* 3396 */     if (arrayOfObject2 != null)
/*      */     {
/*      */       try
/*      */       {
/* 3400 */         for (int i = 0; i < this.numParams; i++)
/*      */         {
/* 3402 */           arrayOfObject1[i] = arrayOfObject2[i];
/*      */         }
/*      */       }
/*      */       catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException)
/*      */       {
/* 3407 */         System.out.println("Exception, while calculating row count: " + localArrayIndexOutOfBoundsException.getMessage());
/* 3408 */         localArrayIndexOutOfBoundsException.printStackTrace();
/*      */       }
/*      */     }
/*      */ 
/* 3412 */     return arrayOfObject1;
/*      */   }
/*      */ 
/*      */   public int[] getObjectTypes()
/*      */   {
/* 3423 */     int[] arrayOfInt = new int[this.numParams];
/*      */ 
/* 3425 */     for (int i = 0; i < this.numParams; i++)
/*      */     {
/* 3427 */       arrayOfInt[i] = this.boundParams[i].getSqlType();
/*      */     }
/*      */ 
/* 3430 */     return arrayOfInt;
/*      */   }
/*      */ 
/*      */   public int getParamCount()
/*      */   {
/* 3440 */     return this.numParams;
/*      */   }
/*      */ 
/*      */   protected void setInputParameter(int paramInt, boolean paramBoolean)
/*      */   {
/* 3453 */     if ((paramInt >= 1) && (paramInt <= this.numParams))
/*      */     {
/* 3455 */       this.boundParams[(paramInt - 1)].setInputParameter(paramBoolean);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setURL(int paramInt, URL paramURL)
/*      */     throws SQLException
/*      */   {
/* 3464 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public ParameterMetaData getParameterMetaData() throws SQLException {
/* 3468 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void setRowId(int paramInt, RowId paramRowId)
/*      */     throws SQLException
/*      */   {
/* 3483 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void setNString(int paramInt, String paramString)
/*      */     throws SQLException
/*      */   {
/* 3500 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void setNCharacterStream(int paramInt, Reader paramReader, long paramLong)
/*      */     throws SQLException
/*      */   {
/* 3518 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void setNClob(int paramInt, NClob paramNClob)
/*      */     throws SQLException
/*      */   {
/* 3534 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void setClob(int paramInt, Reader paramReader, long paramLong)
/*      */     throws SQLException
/*      */   {
/* 3556 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void setBlob(int paramInt, InputStream paramInputStream, long paramLong)
/*      */     throws SQLException
/*      */   {
/* 3582 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void setNClob(int paramInt, Reader paramReader, long paramLong)
/*      */     throws SQLException
/*      */   {
/* 3604 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void setSQLXML(int paramInt, SQLXML paramSQLXML)
/*      */     throws SQLException
/*      */   {
/* 3616 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void setPoolable(boolean paramBoolean)
/*      */     throws SQLException
/*      */   {
/* 3640 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public boolean isPoolable()
/*      */     throws SQLException
/*      */   {
/* 3657 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public boolean isClosed()
/*      */     throws SQLException
/*      */   {
/* 3669 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void setCharacterStream(int paramInt, Reader paramReader, long paramLong)
/*      */     throws SQLException
/*      */   {
/*      */   }
/*      */ 
/*      */   public void setBinaryStream(int paramInt, InputStream paramInputStream, long paramLong)
/*      */     throws SQLException
/*      */   {
/*      */   }
/*      */ 
/*      */   public void setAsciiStream(int paramInt, InputStream paramInputStream, long paramLong)
/*      */     throws SQLException
/*      */   {
/*      */   }
/*      */ 
/*      */   public void setAsciiStream(int paramInt, InputStream paramInputStream)
/*      */     throws SQLException
/*      */   {
/*      */   }
/*      */ 
/*      */   public void setBinaryStream(int paramInt, InputStream paramInputStream)
/*      */     throws SQLException
/*      */   {
/*      */   }
/*      */ 
/*      */   public void setCharacterStream(int paramInt, Reader paramReader)
/*      */     throws SQLException
/*      */   {
/*      */   }
/*      */ 
/*      */   public void setNCharacterStream(int paramInt, Reader paramReader)
/*      */     throws SQLException
/*      */   {
/*      */   }
/*      */ 
/*      */   public void setClob(int paramInt, Reader paramReader)
/*      */     throws SQLException
/*      */   {
/*      */   }
/*      */ 
/*      */   public void setBlob(int paramInt, InputStream paramInputStream)
/*      */     throws SQLException
/*      */   {
/*      */   }
/*      */ 
/*      */   public void setNClob(int paramInt, Reader paramReader)
/*      */     throws SQLException
/*      */   {
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.jdbc.odbc.JdbcOdbcPreparedStatement
 * JD-Core Version:    0.6.2
 */