/*      */ package sun.jdbc.odbc;
/*      */ 
/*      */ import java.io.BufferedOutputStream;
/*      */ import java.io.BufferedReader;
/*      */ import java.io.ByteArrayInputStream;
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.InputStreamReader;
/*      */ import java.io.Reader;
/*      */ import java.io.UnsupportedEncodingException;
/*      */ import java.math.BigDecimal;
/*      */ import java.net.URL;
/*      */ import java.nio.charset.Charset;
/*      */ import java.nio.charset.CharsetEncoder;
/*      */ import java.nio.charset.IllegalCharsetNameException;
/*      */ import java.nio.charset.UnsupportedCharsetException;
/*      */ import java.sql.Array;
/*      */ import java.sql.Blob;
/*      */ import java.sql.Clob;
/*      */ import java.sql.Connection;
/*      */ import java.sql.Date;
/*      */ import java.sql.NClob;
/*      */ import java.sql.PreparedStatement;
/*      */ import java.sql.Ref;
/*      */ import java.sql.ResultSet;
/*      */ import java.sql.ResultSetMetaData;
/*      */ import java.sql.RowId;
/*      */ import java.sql.SQLException;
/*      */ import java.sql.SQLFeatureNotSupportedException;
/*      */ import java.sql.SQLWarning;
/*      */ import java.sql.SQLXML;
/*      */ import java.sql.Statement;
/*      */ import java.sql.Time;
/*      */ import java.sql.Timestamp;
/*      */ import java.util.Calendar;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Map;
/*      */ 
/*      */ public class JdbcOdbcResultSet extends JdbcOdbcObject
/*      */   implements JdbcOdbcResultSetInterface
/*      */ {
/*      */   protected JdbcOdbc OdbcApi;
/*      */   protected long hDbc;
/*      */   protected long hStmt;
/*      */   protected SQLWarning lastWarning;
/*      */   protected boolean keepHSTMT;
/*      */   protected JdbcOdbcBoundCol[] boundCols;
/*      */   protected int numberOfCols;
/*      */   protected int numResultCols;
/*      */   protected int firstPseudoCol;
/*      */   protected int lastPseudoCol;
/*      */   protected JdbcOdbcPseudoCol[] pseudoCols;
/*      */   protected int[] colMappings;
/*      */   protected ResultSetMetaData rsmd;
/*      */   private Hashtable colNameToNum;
/*      */   private Hashtable colNumToName;
/*      */   private boolean lastColumnNull;
/*      */   private boolean closed;
/*      */   private int sqlTypeColumn;
/*      */   private boolean freed;
/* 7254 */   private JdbcOdbcUtils utils = new JdbcOdbcUtils();
/*      */   private boolean ownInsertsAreVisible;
/*      */   private boolean ownDeletesAreVisible;
/*      */   protected JdbcOdbcStatement ownerStatement;
/*      */   protected int numberOfRows;
/*      */   protected int rowPosition;
/*      */   protected int lastRowPosition;
/*      */   protected int[] rowStatusArray;
/*      */   protected boolean atInsertRow;
/*      */   protected int lastForwardRecord;
/*      */   protected int lastColumnData;
/*      */   protected int rowSet;
/*      */   protected boolean blockCursor;
/*      */   protected int fetchCount;
/*      */   protected int currentBlockCell;
/*      */   protected int lastBlockPosition;
/*      */   protected boolean moveUpBlock;
/*      */   protected boolean moveDownBlock;
/*      */   protected short odbcCursorType;
/*      */   protected boolean rowUpdated;
/*      */   protected long[] pA;
/*      */ 
/*      */   public JdbcOdbcResultSet()
/*      */   {
/*   52 */     this.OdbcApi = null;
/*   53 */     this.hDbc = 0L;
/*   54 */     this.hStmt = 0L;
/*   55 */     this.lastWarning = null;
/*   56 */     this.keepHSTMT = false;
/*   57 */     this.numResultCols = -1;
/*   58 */     this.lastColumnNull = false;
/*      */   }
/*      */ 
/*      */   protected void finalize()
/*      */   {
/*   68 */     if (this.OdbcApi.getTracer().isTracing()) {
/*   69 */       this.OdbcApi.getTracer().trace("ResultSet.finalize " + this);
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/*   77 */       if (!this.closed)
/*      */       {
/*   79 */         this.hStmt = 0L;
/*   80 */         close();
/*      */       }
/*      */     }
/*      */     catch (SQLException localSQLException)
/*      */     {
/*      */     }
/*      */   }
/*      */ 
/*      */   public void initialize(JdbcOdbc paramJdbcOdbc, long paramLong1, long paramLong2, boolean paramBoolean, JdbcOdbcStatement paramJdbcOdbcStatement)
/*      */     throws SQLException
/*      */   {
/*  105 */     this.OdbcApi = paramJdbcOdbc;
/*  106 */     this.hDbc = paramLong1;
/*  107 */     this.hStmt = paramLong2;
/*  108 */     this.keepHSTMT = paramBoolean;
/*      */ 
/*  114 */     this.numberOfCols = getColumnCount();
/*      */ 
/*  116 */     this.boundCols = new JdbcOdbcBoundCol[this.numberOfCols];
/*      */ 
/*  119 */     for (int i = 0; i < this.numberOfCols; i++) {
/*  120 */       this.boundCols[i] = new JdbcOdbcBoundCol();
/*      */     }
/*      */ 
/*  125 */     this.ownerStatement = paramJdbcOdbcStatement;
/*      */ 
/*  128 */     this.rowPosition = 0;
/*  129 */     this.lastForwardRecord = 0;
/*  130 */     this.lastRowPosition = 0;
/*  131 */     this.lastColumnData = 0;
/*  132 */     this.currentBlockCell = 0;
/*  133 */     this.blockCursor = false;
/*  134 */     this.rowSet = 1;
/*      */ 
/*  136 */     if (getType() != 1003)
/*      */     {
/*  139 */       if (this.ownerStatement != null)
/*      */       {
/*  144 */         this.rowSet = this.ownerStatement.getBlockCursorSize();
/*      */       }
/*      */ 
/*  148 */       setRowStatusPtr();
/*      */ 
/*  151 */       setResultSetVisibilityIndicators();
/*      */ 
/*  154 */       calculateRowCount();
/*      */ 
/*  156 */       if (this.numberOfRows >= 0)
/*      */       {
/*  158 */         i = 0;
/*      */ 
/*  162 */         boolean bool = setRowArraySize();
/*      */ 
/*  166 */         if (!bool) {
/*  167 */           this.rowSet = 1;
/*      */         }
/*      */ 
/*  170 */         if ((this.pA != null) && 
/*  171 */           (this.pA[0] != 0L)) {
/*  172 */           JdbcOdbc.ReleaseStoredIntegers(this.pA[0], this.pA[1]);
/*  173 */           this.pA[0] = 0L;
/*  174 */           this.pA[1] = 0L;
/*      */         }
/*      */ 
/*  180 */         setRowStatusPtr();
/*      */ 
/*  182 */         if (this.rowSet > 1)
/*      */         {
/*  184 */           this.blockCursor = true;
/*  185 */           setCursorType();
/*      */         }
/*      */ 
/*  190 */         for (int j = 0; j < this.numberOfCols; j++)
/*      */         {
/*  192 */           this.boundCols[j].initStagingArea(this.rowSet);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean wasNull()
/*      */     throws SQLException
/*      */   {
/*  206 */     return this.lastColumnNull;
/*      */   }
/*      */ 
/*      */   public void setAliasColumnName(String paramString, int paramInt)
/*      */   {
/*  222 */     if ((paramInt > 0) && (paramInt <= this.numberOfCols))
/*  223 */       this.boundCols[(paramInt - 1)].setAliasName(paramString);
/*      */   }
/*      */ 
/*      */   public String mapColumnName(String paramString, int paramInt)
/*      */   {
/*  239 */     if ((paramInt > 0) && (paramInt <= this.numberOfCols)) {
/*  240 */       return this.boundCols[(paramInt - 1)].mapAliasName(paramString);
/*      */     }
/*      */ 
/*  243 */     return paramString;
/*      */   }
/*      */ 
/*      */   public String getString(int paramInt)
/*      */     throws SQLException
/*      */   {
/*  256 */     checkOpen();
/*      */ 
/*  259 */     clearWarnings();
/*  260 */     this.lastColumnNull = false;
/*      */ 
/*  264 */     paramInt = mapColumn(paramInt);
/*      */ 
/*  268 */     consecutiveFetch(paramInt);
/*      */ 
/*  272 */     if (getPseudoCol(paramInt) != null) {
/*  273 */       this.lastColumnNull = true;
/*  274 */       return null;
/*      */     }
/*      */ 
/*  277 */     int i = getMaxCharLen(paramInt);
/*  278 */     int j = getColumnLength(paramInt);
/*      */ 
/*  280 */     String str1 = null;
/*  281 */     if (j > 32767)
/*      */     {
/*  284 */       JdbcOdbcInputStream localJdbcOdbcInputStream = (JdbcOdbcInputStream)getAsciiStream(paramInt);
/*      */       try
/*      */       {
/*  289 */         byte[] arrayOfByte = localJdbcOdbcInputStream.readAllData();
/*      */ 
/*  301 */         if (arrayOfByte != null) {
/*  302 */           if ((arrayOfByte.length > 2) && (arrayOfByte[1] == 0))
/*      */           {
/*  304 */             str1 = BytesToChars("UnicodeLittleUnmarked", arrayOfByte);
/*  305 */           } else if ((arrayOfByte.length >= 2) && (arrayOfByte[0] == 0))
/*      */           {
/*  307 */             str1 = BytesToChars("UnicodeBigUnmarked", arrayOfByte);
/*      */           }
/*  309 */           else str1 = BytesToChars(this.OdbcApi.charSet, arrayOfByte);
/*      */         }
/*      */         else
/*  312 */           this.lastColumnNull = true;
/*      */       }
/*      */       catch (Exception localException)
/*      */       {
/*  316 */         SQLException localSQLException = new SQLException(localException.getMessage());
/*  317 */         localSQLException.fillInStackTrace();
/*  318 */         throw localSQLException;
/*      */       }
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*  327 */       if (i == -1) {
/*  328 */         this.lastColumnNull = true;
/*  329 */         return null;
/*      */       }
/*      */ 
/*  334 */       boolean bool = true;
/*      */ 
/*  336 */       int k = getColumnType(paramInt);
/*  337 */       switch (k)
/*      */       {
/*      */       case -1:
/*      */       case 1:
/*      */       case 12:
/*  342 */         bool = false;
/*  343 */         break;
/*      */       }
/*      */ 
/*  350 */       i++;
/*      */ 
/*  353 */       str1 = getDataString(paramInt, i, bool);
/*      */ 
/*  355 */       if (str1 == null) {
/*  356 */         this.lastColumnNull = true;
/*  357 */         return str1;
/*      */       }
/*      */ 
/*  363 */       int m = str1.length();
/*      */ 
/*  365 */       if ((m == i - 1) && (!bool)) {
/*  366 */         str1 = str1.substring(0, i - 1);
/*      */       }
/*      */ 
/*  372 */       if (((k == -1) || (k == -4)) && (m == i - 1))
/*      */       {
/*  376 */         String str2 = str1;
/*      */ 
/*  380 */         while (str2.length() == 32767) {
/*  381 */           str2 = getDataString(paramInt, i, bool);
/*  382 */           if (str2 == null) break;
/*  383 */           if (this.OdbcApi.getTracer().isTracing()) {
/*  384 */             this.OdbcApi.getTracer().trace("" + str2.length() + " byte(s) read");
/*      */           }
/*      */ 
/*  388 */           if (str2.length() == i) {
/*  389 */             str2 = str2.substring(0, i - 1);
/*      */           }
/*  391 */           str1 = str1 + str2;
/*      */ 
/*  393 */           if (this.OdbcApi.getTracer().isTracing()) {
/*  394 */             this.OdbcApi.getTracer().trace("" + str1.length() + " bytes total");
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  403 */     return str1;
/*      */   }
/*      */ 
/*      */   public String getString(String paramString)
/*      */     throws SQLException
/*      */   {
/*  410 */     return getString(findColumn(paramString));
/*      */   }
/*      */ 
/*      */   public boolean getBoolean(int paramInt)
/*      */     throws SQLException
/*      */   {
/*  421 */     checkOpen();
/*  422 */     boolean bool = false;
/*      */ 
/*  426 */     clearWarnings();
/*  427 */     this.lastColumnNull = false;
/*      */ 
/*  431 */     paramInt = mapColumn(paramInt);
/*      */ 
/*  435 */     consecutiveFetch(paramInt);
/*      */ 
/*  440 */     if (getPseudoCol(paramInt) == null)
/*      */     {
/*  444 */       bool = getInt(paramInt) != 0;
/*      */     }
/*      */     else {
/*  447 */       this.lastColumnNull = true;
/*      */     }
/*  449 */     return bool;
/*      */   }
/*      */ 
/*      */   public boolean getBoolean(String paramString)
/*      */     throws SQLException
/*      */   {
/*  456 */     return getBoolean(findColumn(paramString));
/*      */   }
/*      */ 
/*      */   public byte getByte(int paramInt)
/*      */     throws SQLException
/*      */   {
/*  467 */     checkOpen();
/*      */ 
/*  469 */     byte b = 0;
/*      */ 
/*  473 */     clearWarnings();
/*  474 */     this.lastColumnNull = false;
/*      */ 
/*  478 */     paramInt = mapColumn(paramInt);
/*      */ 
/*  482 */     consecutiveFetch(paramInt);
/*      */ 
/*  487 */     if (getPseudoCol(paramInt) == null)
/*      */     {
/*  491 */       b = (byte)getInt(paramInt);
/*      */     }
/*      */     else {
/*  494 */       this.lastColumnNull = true;
/*      */     }
/*  496 */     return b;
/*      */   }
/*      */ 
/*      */   public byte getByte(String paramString)
/*      */     throws SQLException
/*      */   {
/*  503 */     return getByte(findColumn(paramString));
/*      */   }
/*      */ 
/*      */   public short getShort(int paramInt)
/*      */     throws SQLException
/*      */   {
/*  514 */     checkOpen();
/*      */ 
/*  516 */     short s = 0;
/*      */ 
/*  520 */     clearWarnings();
/*  521 */     this.lastColumnNull = false;
/*      */ 
/*  525 */     paramInt = mapColumn(paramInt);
/*      */ 
/*  529 */     consecutiveFetch(paramInt);
/*      */ 
/*  534 */     if (getPseudoCol(paramInt) == null)
/*      */     {
/*  538 */       s = (short)getInt(paramInt);
/*      */     }
/*      */     else {
/*  541 */       this.lastColumnNull = true;
/*      */     }
/*      */ 
/*  544 */     return s;
/*      */   }
/*      */ 
/*      */   public short getShort(String paramString)
/*      */     throws SQLException
/*      */   {
/*  551 */     return getShort(findColumn(paramString));
/*      */   }
/*      */ 
/*      */   public int getInt(int paramInt)
/*      */     throws SQLException
/*      */   {
/*  562 */     checkOpen();
/*      */ 
/*  564 */     int i = 0;
/*      */ 
/*  568 */     clearWarnings();
/*  569 */     this.lastColumnNull = false;
/*      */ 
/*  573 */     paramInt = mapColumn(paramInt);
/*      */ 
/*  577 */     consecutiveFetch(paramInt);
/*      */ 
/*  581 */     if (getPseudoCol(paramInt) == null) {
/*  582 */       Integer localInteger = getDataInteger(paramInt);
/*      */ 
/*  586 */       if (localInteger != null)
/*  587 */         i = localInteger.intValue();
/*      */     }
/*      */     else
/*      */     {
/*  591 */       this.lastColumnNull = true;
/*      */     }
/*  593 */     return i;
/*      */   }
/*      */ 
/*      */   public int getInt(String paramString)
/*      */     throws SQLException
/*      */   {
/*  600 */     return getInt(findColumn(paramString));
/*      */   }
/*      */ 
/*      */   public long getLong(int paramInt)
/*      */     throws SQLException
/*      */   {
/*  611 */     checkOpen();
/*      */ 
/*  613 */     long l = 0L;
/*      */ 
/*  617 */     clearWarnings();
/*  618 */     this.lastColumnNull = false;
/*      */ 
/*  622 */     paramInt = mapColumn(paramInt);
/*      */ 
/*  626 */     consecutiveFetch(paramInt);
/*      */ 
/*  631 */     if (getPseudoCol(paramInt) == null) {
/*  632 */       Double localDouble = getDataDouble(paramInt);
/*      */ 
/*  636 */       if (localDouble != null)
/*  637 */         l = localDouble.longValue();
/*      */     }
/*      */     else
/*      */     {
/*  641 */       this.lastColumnNull = true;
/*      */     }
/*  643 */     return l;
/*      */   }
/*      */ 
/*      */   public long getLong(String paramString)
/*      */     throws SQLException
/*      */   {
/*  650 */     return getLong(findColumn(paramString));
/*      */   }
/*      */ 
/*      */   public float getFloat(int paramInt)
/*      */     throws SQLException
/*      */   {
/*  662 */     return (float)getDouble(paramInt);
/*      */   }
/*      */ 
/*      */   public float getFloat(String paramString)
/*      */     throws SQLException
/*      */   {
/*  705 */     return getFloat(findColumn(paramString));
/*      */   }
/*      */ 
/*      */   public double getDouble(int paramInt)
/*      */     throws SQLException
/*      */   {
/*  716 */     checkOpen();
/*      */ 
/*  718 */     double d = 0.0D;
/*      */ 
/*  722 */     clearWarnings();
/*  723 */     this.lastColumnNull = false;
/*      */ 
/*  727 */     paramInt = mapColumn(paramInt);
/*      */ 
/*  731 */     consecutiveFetch(paramInt);
/*      */ 
/*  736 */     if (getPseudoCol(paramInt) == null) {
/*  737 */       Double localDouble = getDataDouble(paramInt);
/*      */ 
/*  741 */       if (localDouble != null)
/*  742 */         d = localDouble.doubleValue();
/*      */     }
/*      */     else
/*      */     {
/*  746 */       this.lastColumnNull = true;
/*      */     }
/*  748 */     return d;
/*      */   }
/*      */ 
/*      */   public double getDouble(String paramString)
/*      */     throws SQLException
/*      */   {
/*  755 */     return getDouble(findColumn(paramString));
/*      */   }
/*      */ 
/*      */   public BigDecimal getBigDecimal(int paramInt1, int paramInt2)
/*      */     throws SQLException
/*      */   {
/*  767 */     checkOpen();
/*      */ 
/*  769 */     BigDecimal localBigDecimal = null;
/*      */ 
/*  773 */     clearWarnings();
/*  774 */     this.lastColumnNull = false;
/*      */ 
/*  778 */     paramInt1 = mapColumn(paramInt1);
/*      */ 
/*  782 */     consecutiveFetch(paramInt1);
/*      */ 
/*  787 */     if (getPseudoCol(paramInt1) == null)
/*      */     {
/*  791 */       String str = getDataString(paramInt1, 300, true);
/*      */ 
/*  796 */       if (str != null) {
/*  797 */         localBigDecimal = new BigDecimal(str);
/*  798 */         localBigDecimal = localBigDecimal.setScale(paramInt2, 6);
/*      */       }
/*      */     }
/*      */     else {
/*  802 */       this.lastColumnNull = true;
/*      */     }
/*      */ 
/*  805 */     return localBigDecimal;
/*      */   }
/*      */ 
/*      */   public BigDecimal getBigDecimal(String paramString, int paramInt)
/*      */     throws SQLException
/*      */   {
/*  813 */     return getBigDecimal(findColumn(paramString), paramInt);
/*      */   }
/*      */ 
/*      */   public synchronized byte[] getBytes(int paramInt)
/*      */     throws SQLException
/*      */   {
/*  824 */     checkOpen();
/*      */ 
/*  828 */     clearWarnings();
/*  829 */     this.lastColumnNull = false;
/*      */ 
/*  833 */     paramInt = mapColumn(paramInt);
/*      */ 
/*  837 */     consecutiveFetch(paramInt);
/*      */ 
/*  841 */     if (getPseudoCol(paramInt) != null) {
/*  842 */       this.lastColumnNull = true;
/*  843 */       return null;
/*      */     }
/*      */ 
/*  846 */     int i = getMaxBinaryLen(paramInt);
/*      */ 
/*  850 */     if (i == -1) {
/*  851 */       this.lastColumnNull = true;
/*  852 */       return null;
/*      */     }
/*      */ 
/*  855 */     int j = getColumnLength(paramInt);
/*  856 */     if (j > 32767)
/*      */     {
/*  859 */       JdbcOdbcInputStream localJdbcOdbcInputStream = (JdbcOdbcInputStream)getBinaryStream(paramInt);
/*      */       try
/*      */       {
/*  862 */         return localJdbcOdbcInputStream.readAllData();
/*      */       }
/*      */       catch (Exception localException)
/*      */       {
/*  866 */         throw new SQLException(localException.getMessage());
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  872 */     int k = getColumnType(paramInt);
/*      */ 
/*  874 */     byte[] arrayOfByte1 = new byte[i];
/*      */     int m;
/*      */     try
/*      */     {
/*  880 */       m = this.OdbcApi.SQLGetDataBinary(this.hStmt, paramInt, arrayOfByte1);
/*      */     }
/*      */     catch (JdbcOdbcSQLWarning localJdbcOdbcSQLWarning)
/*      */     {
/*  885 */       Integer localInteger = (Integer)localJdbcOdbcSQLWarning.value;
/*  886 */       m = localInteger.intValue();
/*      */     }
/*      */ 
/*  889 */     if (m == -1)
/*      */     {
/*  891 */       this.lastColumnNull = true;
/*  892 */       arrayOfByte1 = null;
/*      */     }
/*      */ 
/*  897 */     if ((k != -2) && (m != i) && (arrayOfByte1 != null))
/*      */     {
/*  901 */       byte[] arrayOfByte2 = new byte[m];
/*  902 */       System.arraycopy(arrayOfByte1, 0, arrayOfByte2, 0, m);
/*  903 */       return arrayOfByte2;
/*      */     }
/*      */ 
/*  907 */     return arrayOfByte1;
/*      */   }
/*      */ 
/*      */   public byte[] getBytes(String paramString)
/*      */     throws SQLException
/*      */   {
/*  915 */     return getBytes(findColumn(paramString));
/*      */   }
/*      */ 
/*      */   public Date getDate(int paramInt)
/*      */     throws SQLException
/*      */   {
/*  927 */     checkOpen();
/*      */ 
/*  931 */     clearWarnings();
/*  932 */     this.lastColumnNull = false;
/*      */ 
/*  936 */     paramInt = mapColumn(paramInt);
/*      */ 
/*  940 */     consecutiveFetch(paramInt);
/*      */ 
/*  944 */     if (getPseudoCol(paramInt) != null) {
/*  945 */       this.lastColumnNull = true;
/*  946 */       return null;
/*      */     }
/*      */ 
/*  949 */     String str = getDataStringDate(paramInt);
/*      */ 
/*  953 */     if (str == null) {
/*  954 */       return null;
/*      */     }
/*      */ 
/*  959 */     return Date.valueOf(str);
/*      */   }
/*      */ 
/*      */   public Date getDate(String paramString)
/*      */     throws SQLException
/*      */   {
/*  966 */     return getDate(findColumn(paramString));
/*      */   }
/*      */ 
/*      */   public Time getTime(int paramInt)
/*      */     throws SQLException
/*      */   {
/*  978 */     checkOpen();
/*      */ 
/*  982 */     clearWarnings();
/*  983 */     this.lastColumnNull = false;
/*      */ 
/*  987 */     paramInt = mapColumn(paramInt);
/*      */ 
/*  991 */     consecutiveFetch(paramInt);
/*      */ 
/*  995 */     if (getPseudoCol(paramInt) != null) {
/*  996 */       this.lastColumnNull = true;
/*  997 */       return null;
/*      */     }
/*      */ 
/* 1000 */     String str = getDataStringTime(paramInt);
/*      */ 
/* 1004 */     if (str == null) {
/* 1005 */       return null;
/*      */     }
/*      */ 
/* 1010 */     return Time.valueOf(str);
/*      */   }
/*      */ 
/*      */   public Time getTime(String paramString)
/*      */     throws SQLException
/*      */   {
/* 1017 */     return getTime(findColumn(paramString));
/*      */   }
/*      */ 
/*      */   public Timestamp getTimestamp(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 1029 */     checkOpen();
/*      */ 
/* 1033 */     clearWarnings();
/* 1034 */     this.lastColumnNull = false;
/*      */ 
/* 1038 */     paramInt = mapColumn(paramInt);
/*      */ 
/* 1042 */     consecutiveFetch(paramInt);
/*      */ 
/* 1046 */     if (getPseudoCol(paramInt) != null) {
/* 1047 */       this.lastColumnNull = true;
/* 1048 */       return null;
/*      */     }
/*      */ 
/* 1051 */     String str = getDataStringTimestamp(paramInt);
/*      */ 
/* 1055 */     if (str == null) {
/* 1056 */       return null;
/*      */     }
/*      */ 
/* 1061 */     if (str.length() == 10) {
/* 1062 */       str = str + " 00:00:00";
/*      */     }
/*      */ 
/* 1067 */     return Timestamp.valueOf(str);
/*      */   }
/*      */ 
/*      */   public Timestamp getTimestamp(String paramString)
/*      */     throws SQLException
/*      */   {
/* 1074 */     return getTimestamp(findColumn(paramString));
/*      */   }
/*      */ 
/*      */   public InputStream getAsciiStream(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 1090 */     checkOpen();
/*      */ 
/* 1094 */     clearWarnings();
/* 1095 */     this.lastColumnNull = false;
/*      */ 
/* 1099 */     paramInt = mapColumn(paramInt);
/*      */ 
/* 1103 */     consecutiveFetch(paramInt);
/*      */ 
/* 1107 */     int i = getColumnType(paramInt);
/* 1108 */     int j = -2;
/*      */ 
/* 1110 */     switch (i) {
/*      */     case -10:
/*      */     case -9:
/*      */     case -8:
/*      */     case -1:
/*      */     case 1:
/*      */     case 12:
/* 1117 */       j = 1;
/*      */     }
/*      */ 
/* 1121 */     JdbcOdbcInputStream localJdbcOdbcInputStream = new JdbcOdbcInputStream(this.OdbcApi, this.hStmt, paramInt, (short)1, j, this.ownerStatement);
/*      */ 
/* 1125 */     setInputStream(paramInt, localJdbcOdbcInputStream);
/*      */ 
/* 1127 */     return localJdbcOdbcInputStream;
/*      */   }
/*      */ 
/*      */   public InputStream getAsciiStream(String paramString)
/*      */     throws SQLException
/*      */   {
/* 1134 */     return getAsciiStream(findColumn(paramString));
/*      */   }
/*      */ 
/*      */   public InputStream getUnicodeStream(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 1141 */     checkOpen();
/*      */ 
/* 1145 */     clearWarnings();
/* 1146 */     this.lastColumnNull = false;
/*      */ 
/* 1150 */     paramInt = mapColumn(paramInt);
/*      */ 
/* 1154 */     consecutiveFetch(paramInt);
/*      */ 
/* 1158 */     int i = getColumnType(paramInt);
/* 1159 */     int j = -2;
/*      */ 
/* 1161 */     switch (i) {
/*      */     case -10:
/*      */     case -9:
/*      */     case -8:
/*      */     case -1:
/*      */     case 1:
/*      */     case 12:
/* 1168 */       j = 1;
/*      */     }
/*      */ 
/* 1172 */     JdbcOdbcInputStream localJdbcOdbcInputStream = new JdbcOdbcInputStream(this.OdbcApi, this.hStmt, paramInt, (short)2, j, this.ownerStatement);
/*      */ 
/* 1175 */     setInputStream(paramInt, localJdbcOdbcInputStream);
/*      */ 
/* 1177 */     return localJdbcOdbcInputStream;
/*      */   }
/*      */ 
/*      */   public InputStream getUnicodeStream(String paramString)
/*      */     throws SQLException
/*      */   {
/* 1184 */     return getUnicodeStream(findColumn(paramString));
/*      */   }
/*      */ 
/*      */   public InputStream getBinaryStream(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 1191 */     checkOpen();
/*      */ 
/* 1195 */     clearWarnings();
/* 1196 */     this.lastColumnNull = false;
/*      */ 
/* 1200 */     paramInt = mapColumn(paramInt);
/*      */ 
/* 1204 */     consecutiveFetch(paramInt);
/*      */ 
/* 1208 */     int i = getColumnType(paramInt);
/* 1209 */     int j = -2;
/*      */ 
/* 1211 */     switch (i) {
/*      */     case -10:
/*      */     case -9:
/*      */     case -8:
/*      */     case -1:
/*      */     case 1:
/*      */     case 12:
/* 1218 */       j = 1;
/*      */     }
/*      */ 
/* 1222 */     JdbcOdbcInputStream localJdbcOdbcInputStream = new JdbcOdbcInputStream(this.OdbcApi, this.hStmt, paramInt, (short)3, j, this.ownerStatement);
/*      */ 
/* 1225 */     setInputStream(paramInt, localJdbcOdbcInputStream);
/*      */ 
/* 1227 */     return localJdbcOdbcInputStream;
/*      */   }
/*      */ 
/*      */   public InputStream getBinaryStream(String paramString)
/*      */     throws SQLException
/*      */   {
/* 1234 */     return getBinaryStream(findColumn(paramString));
/*      */   }
/*      */ 
/*      */   public boolean next()
/*      */     throws SQLException
/*      */   {
/* 1248 */     checkOpen();
/*      */ 
/* 1250 */     if (getType() != 1003)
/*      */     {
/* 1252 */       bool = false;
/* 1253 */       int i = 0;
/*      */ 
/* 1255 */       if (getFetchDirection() == 1000)
/*      */       {
/* 1258 */         if (this.rowPosition == this.numberOfRows)
/*      */         {
/* 1260 */           afterLast();
/* 1261 */           return false;
/*      */         }
/*      */ 
/* 1264 */         if (this.blockCursor)
/*      */         {
/* 1266 */           bool = relative(1, false);
/*      */         }
/*      */         else
/*      */         {
/* 1270 */           this.rowPosition += 1;
/* 1271 */           bool = fetchScrollOption(this.rowPosition, (short)5);
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/* 1276 */         if (this.rowPosition == 1)
/*      */         {
/* 1278 */           beforeFirst();
/* 1279 */           return false;
/*      */         }
/*      */ 
/* 1282 */         if (this.blockCursor)
/*      */         {
/* 1284 */           bool = relative(-1, false);
/*      */         }
/*      */         else
/*      */         {
/* 1288 */           this.rowPosition -= 1;
/* 1289 */           bool = fetchScrollOption(this.rowPosition, (short)5);
/*      */         }
/*      */       }
/*      */ 
/* 1293 */       return bool;
/*      */     }
/*      */ 
/* 1298 */     boolean bool = true;
/* 1299 */     this.lastColumnNull = false;
/*      */ 
/* 1303 */     closeInputStreams();
/*      */ 
/* 1307 */     clearWarnings();
/*      */     try
/*      */     {
/* 1310 */       bool = this.OdbcApi.SQLFetch(this.hStmt);
/*      */     }
/*      */     catch (SQLWarning localSQLWarning)
/*      */     {
/* 1318 */       setWarning(localSQLWarning);
/*      */     }
/*      */ 
/* 1322 */     if (bool == true)
/* 1323 */       this.rowPosition += 1;
/* 1324 */     else if (!bool)
/*      */     {
/* 1326 */       if (this.lastForwardRecord == 0)
/*      */       {
/* 1328 */         this.lastForwardRecord = this.rowPosition;
/* 1329 */         this.rowPosition = 0;
/*      */       }
/*      */       else {
/* 1332 */         this.rowPosition = 0;
/*      */       }
/*      */     }
/* 1335 */     return bool;
/*      */   }
/*      */ 
/*      */   public int getRowNumber()
/*      */     throws SQLException
/*      */   {
/* 1347 */     checkOpen();
/*      */ 
/* 1349 */     int i = 0;
/*      */ 
/* 1353 */     clearWarnings();
/*      */     try
/*      */     {
/* 1356 */       i = (int)this.OdbcApi.SQLGetStmtOption(this.hStmt, (short)14);
/*      */     }
/*      */     catch (JdbcOdbcSQLWarning localJdbcOdbcSQLWarning)
/*      */     {
/* 1365 */       BigDecimal localBigDecimal = (BigDecimal)localJdbcOdbcSQLWarning.value;
/* 1366 */       i = localBigDecimal.intValue();
/*      */ 
/* 1368 */       setWarning(JdbcOdbc.convertWarning(localJdbcOdbcSQLWarning));
/*      */     }
/* 1370 */     return i;
/*      */   }
/*      */ 
/*      */   public int getColumnCount()
/*      */     throws SQLException
/*      */   {
/* 1382 */     checkOpen();
/*      */ 
/* 1384 */     int i = 0;
/*      */ 
/* 1388 */     clearWarnings();
/*      */ 
/* 1393 */     if (this.lastPseudoCol > 0) {
/* 1394 */       return this.lastPseudoCol;
/*      */     }
/*      */ 
/* 1400 */     if (this.colMappings != null) {
/* 1401 */       return this.colMappings.length;
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/* 1414 */       this.numResultCols = this.OdbcApi.SQLNumResultCols(this.hStmt);
/*      */ 
/* 1417 */       i = this.numResultCols;
/*      */     }
/*      */     catch (JdbcOdbcSQLWarning localJdbcOdbcSQLWarning)
/*      */     {
/* 1425 */       BigDecimal localBigDecimal = (BigDecimal)localJdbcOdbcSQLWarning.value;
/* 1426 */       i = localBigDecimal.intValue();
/*      */ 
/* 1428 */       setWarning(JdbcOdbc.convertWarning(localJdbcOdbcSQLWarning));
/*      */     }
/* 1430 */     return i;
/*      */   }
/*      */ 
/*      */   public int getRowCount()
/*      */     throws SQLException
/*      */   {
/* 1442 */     checkOpen();
/* 1443 */     return this.numberOfRows;
/*      */   }
/*      */ 
/*      */   public synchronized void close()
/*      */     throws SQLException
/*      */   {
/* 1457 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 1458 */       this.OdbcApi.getTracer().trace("*ResultSet.close");
/*      */     }
/*      */ 
/* 1462 */     closeInputStreams();
/*      */ 
/* 1467 */     clearWarnings();
/* 1468 */     this.lastColumnNull = false;
/*      */ 
/* 1470 */     if ((this.OdbcApi != null) && (this.hStmt != 0L))
/*      */     {
/* 1474 */       if (!this.keepHSTMT)
/*      */       {
/* 1484 */         this.OdbcApi.SQLFreeStmt(this.hStmt, 1);
/* 1485 */         this.hStmt = 0L;
/*      */       }
/*      */ 
/* 1490 */       this.closed = true;
/*      */ 
/* 1493 */       FreeCols();
/*      */ 
/* 1496 */       if (this.pA != null)
/*      */       {
/* 1498 */         if (this.pA[0] != 0L)
/*      */         {
/* 1500 */           JdbcOdbc.ReleaseStoredIntegers(this.pA[0], this.pA[1]);
/* 1501 */           this.pA[0] = 0L;
/* 1502 */           this.pA[1] = 0L;
/*      */         }
/*      */       }
/*      */ 
/* 1506 */       if (this.ownerStatement != null)
/*      */       {
/* 1508 */         this.ownerStatement.myResultSet = null;
/*      */       }
/* 1510 */       if (this.OdbcApi.getTracer().isTracing())
/* 1511 */         this.OdbcApi.getTracer().trace("*ResultSet has been closed");
/*      */     }
/*      */   }
/*      */ 
/*      */   public synchronized void FreeCols()
/*      */     throws NullPointerException
/*      */   {
/*      */     try
/*      */     {
/* 1528 */       for (int i = 0; i < this.boundCols.length; i++)
/*      */       {
/* 1530 */         if (this.boundCols[i].pA1 != 0L)
/*      */         {
/* 1532 */           JdbcOdbc.ReleaseStoredBytes(this.boundCols[i].pA1, this.boundCols[i].pA2);
/* 1533 */           this.boundCols[i].pA1 = 0L;
/* 1534 */           this.boundCols[i].pA2 = 0L;
/*      */         }
/*      */ 
/* 1537 */         if (this.boundCols[i].pB1 != 0L)
/*      */         {
/* 1539 */           JdbcOdbc.ReleaseStoredBytes(this.boundCols[i].pB1, this.boundCols[i].pB2);
/* 1540 */           this.boundCols[i].pB1 = 0L;
/* 1541 */           this.boundCols[i].pB2 = 0L;
/*      */         }
/*      */ 
/* 1544 */         if (this.boundCols[i].pC1 != 0L)
/*      */         {
/* 1549 */           JdbcOdbc.ReleaseStoredBytes(this.boundCols[i].pC1, this.boundCols[i].pC2);
/* 1550 */           this.boundCols[i].pC1 = 0L;
/* 1551 */           this.boundCols[i].pC2 = 0L;
/*      */         }
/*      */ 
/* 1554 */         if (this.boundCols[i].pS1 != 0L)
/*      */         {
/* 1556 */           JdbcOdbc.ReleaseStoredChars(this.boundCols[i].pS1, this.boundCols[i].pS2);
/* 1557 */           this.boundCols[i].pS1 = 0L;
/* 1558 */           this.boundCols[i].pS2 = 0L;
/*      */         }
/*      */       }
/*      */     }
/*      */     catch (NullPointerException localNullPointerException)
/*      */     {
/*      */     }
/*      */   }
/*      */ 
/*      */   public String getCursorName()
/*      */     throws SQLException
/*      */   {
/* 1580 */     checkOpen();
/* 1581 */     String str = "";
/*      */ 
/* 1585 */     clearWarnings();
/*      */     try
/*      */     {
/* 1588 */       str = this.OdbcApi.SQLGetCursorName(this.hStmt);
/*      */     }
/*      */     catch (JdbcOdbcSQLWarning localJdbcOdbcSQLWarning)
/*      */     {
/* 1596 */       str = (String)localJdbcOdbcSQLWarning.value;
/* 1597 */       setWarning(JdbcOdbc.convertWarning(localJdbcOdbcSQLWarning));
/*      */     }
/* 1599 */     return str.trim();
/*      */   }
/*      */ 
/*      */   public ResultSetMetaData getMetaData()
/*      */     throws SQLException
/*      */   {
/* 1611 */     checkOpen();
/*      */ 
/* 1613 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 1614 */       this.OdbcApi.getTracer().trace("*ResultSet.getMetaData");
/*      */     }
/*      */ 
/* 1619 */     if (this.closed) {
/* 1620 */       throw new SQLException("ResultSet is closed");
/*      */     }
/*      */ 
/* 1623 */     return new JdbcOdbcResultSetMetaData(this.OdbcApi, this);
/*      */   }
/*      */ 
/*      */   public Object getObject(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 1637 */     checkOpen();
/*      */ 
/* 1639 */     Object localObject = null;
/*      */ 
/* 1643 */     int i = getColumnType(paramInt);
/*      */ 
/* 1647 */     int j = paramInt;
/*      */ 
/* 1651 */     clearWarnings();
/* 1652 */     this.lastColumnNull = false;
/*      */ 
/* 1656 */     paramInt = mapColumn(paramInt);
/*      */ 
/* 1660 */     consecutiveFetch(paramInt);
/*      */ 
/* 1664 */     if (getPseudoCol(paramInt) != null) {
/* 1665 */       this.lastColumnNull = true;
/* 1666 */       return null;
/*      */     }
/*      */ 
/* 1672 */     switch (i)
/*      */     {
/*      */     case -1:
/*      */     case 1:
/*      */     case 12:
/* 1677 */       localObject = getString(paramInt);
/* 1678 */       break;
/*      */     case 2:
/*      */     case 3:
/* 1682 */       localObject = getBigDecimal(paramInt, getScale(j));
/* 1683 */       break;
/*      */     case -7:
/* 1686 */       localObject = new Boolean(getBoolean(paramInt));
/* 1687 */       break;
/*      */     case -6:
/*      */     case 4:
/*      */     case 5:
/* 1692 */       localObject = new Integer(getInt(paramInt));
/* 1693 */       break;
/*      */     case -5:
/* 1696 */       localObject = new Long(getLong(paramInt));
/* 1697 */       break;
/*      */     case 7:
/* 1700 */       localObject = new Float(getFloat(paramInt));
/* 1701 */       break;
/*      */     case 6:
/*      */     case 8:
/* 1705 */       localObject = new Double(getDouble(paramInt));
/* 1706 */       break;
/*      */     case -4:
/*      */     case -3:
/*      */     case -2:
/* 1711 */       localObject = getBytes(paramInt);
/* 1712 */       break;
/*      */     case 91:
/* 1715 */       localObject = getDate(paramInt);
/* 1716 */       break;
/*      */     case 92:
/* 1719 */       localObject = getTime(paramInt);
/* 1720 */       break;
/*      */     case 93:
/* 1723 */       localObject = getTimestamp(paramInt);
/*      */     }
/*      */ 
/* 1730 */     if (wasNull()) {
/* 1731 */       localObject = null;
/*      */     }
/*      */ 
/* 1735 */     return localObject;
/*      */   }
/*      */ 
/*      */   public Object getObject(String paramString)
/*      */     throws SQLException
/*      */   {
/* 1742 */     return getObject(findColumn(paramString));
/*      */   }
/*      */ 
/*      */   public SQLWarning getWarnings()
/*      */     throws SQLException
/*      */   {
/* 1752 */     checkOpen();
/* 1753 */     return this.lastWarning;
/*      */   }
/*      */ 
/*      */   public void clearWarnings()
/*      */     throws SQLException
/*      */   {
/* 1764 */     checkOpen();
/* 1765 */     this.lastWarning = null;
/*      */   }
/*      */ 
/*      */   public void setWarning(SQLWarning paramSQLWarning)
/*      */     throws SQLException
/*      */   {
/* 1777 */     checkOpen();
/* 1778 */     this.lastWarning = paramSQLWarning;
/*      */   }
/*      */ 
/*      */   public long getHSTMT()
/*      */   {
/* 1788 */     return this.hStmt;
/*      */   }
/*      */ 
/*      */   public synchronized int findColumn(String paramString)
/*      */     throws SQLException
/*      */   {
/* 1801 */     if (this.rsmd == null) {
/* 1802 */       this.rsmd = getMetaData();
/* 1803 */       this.colNameToNum = new Hashtable();
/* 1804 */       this.colNumToName = new Hashtable();
/*      */     }
/*      */ 
/* 1809 */     Integer localInteger = (Integer)this.colNameToNum.get(paramString);
/*      */ 
/* 1814 */     if (localInteger == null)
/*      */     {
/* 1818 */       for (int i = 1; i <= this.rsmd.getColumnCount(); i++)
/*      */       {
/* 1827 */         String str = (String)this.colNumToName.get(new Integer(i));
/*      */ 
/* 1830 */         if (str == null) {
/* 1831 */           str = this.rsmd.getColumnName(i);
/*      */ 
/* 1833 */           this.colNameToNum.put(str, new Integer(i));
/*      */ 
/* 1835 */           this.colNumToName.put(new Integer(i), str);
/*      */         }
/*      */ 
/* 1842 */         if (str.equalsIgnoreCase(paramString)) {
/* 1843 */           return i;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1849 */       throw new SQLException("Column not found", "S0022");
/*      */     }
/*      */ 
/* 1852 */     return localInteger.intValue();
/*      */   }
/*      */ 
/*      */   public Reader getCharacterStream(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 1862 */     checkOpen();
/*      */ 
/* 1865 */     InputStreamReader localInputStreamReader = null;
/*      */ 
/* 1867 */     clearWarnings();
/* 1868 */     this.lastColumnNull = false;
/*      */ 
/* 1872 */     paramInt = mapColumn(paramInt);
/*      */ 
/* 1876 */     consecutiveFetch(paramInt);
/*      */ 
/* 1880 */     int i = getColumnType(paramInt);
/* 1881 */     int j = -2;
/*      */ 
/* 1883 */     switch (i) {
/*      */     case -10:
/*      */     case -9:
/*      */     case -8:
/*      */     case -1:
/*      */     case 1:
/*      */     case 12:
/* 1890 */       j = 1;
/*      */     }
/*      */ 
/* 1894 */     String str = this.OdbcApi.charSet;
/*      */ 
/* 1896 */     JdbcOdbcInputStream localJdbcOdbcInputStream = new JdbcOdbcInputStream(this.OdbcApi, this.hStmt, paramInt, (short)5, j, this.ownerStatement);
/*      */ 
/* 1900 */     setInputStream(paramInt, localJdbcOdbcInputStream);
/*      */     try
/*      */     {
/* 1904 */       localInputStreamReader = new InputStreamReader(localJdbcOdbcInputStream, str);
/*      */     }
/*      */     catch (UnsupportedEncodingException localUnsupportedEncodingException)
/*      */     {
/* 1908 */       throw new SQLException("getCharacterStream() with Encoding ('encoding') :" + localUnsupportedEncodingException.getMessage());
/*      */     }
/* 1910 */     return localInputStreamReader;
/*      */   }
/*      */ 
/*      */   public Reader getCharacterStream(String paramString)
/*      */     throws SQLException
/*      */   {
/* 1917 */     int i = findColumn(paramString);
/*      */ 
/* 1919 */     return getCharacterStream(i);
/*      */   }
/*      */ 
/*      */   public BigDecimal getBigDecimal(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 1926 */     checkOpen();
/* 1927 */     BigDecimal localBigDecimal = null;
/*      */ 
/* 1931 */     clearWarnings();
/* 1932 */     this.lastColumnNull = false;
/*      */ 
/* 1936 */     paramInt = mapColumn(paramInt);
/*      */ 
/* 1940 */     consecutiveFetch(paramInt);
/*      */ 
/* 1945 */     if (getPseudoCol(paramInt) == null)
/*      */     {
/* 1949 */       String str = getDataString(paramInt, 300, true);
/*      */ 
/* 1954 */       if (str != null)
/*      */       {
/* 1956 */         localBigDecimal = new BigDecimal(str);
/*      */       }
/*      */     }
/*      */     else {
/* 1960 */       this.lastColumnNull = true;
/*      */     }
/*      */ 
/* 1963 */     return localBigDecimal;
/*      */   }
/*      */ 
/*      */   public BigDecimal getBigDecimal(String paramString)
/*      */     throws SQLException
/*      */   {
/* 1970 */     int i = findColumn(paramString);
/*      */ 
/* 1972 */     return getBigDecimal(i);
/*      */   }
/*      */ 
/*      */   public boolean isBeforeFirst()
/*      */     throws SQLException
/*      */   {
/* 1985 */     checkOpen();
/*      */ 
/* 1987 */     if (getType() != 1003)
/*      */     {
/* 1989 */       if (this.numberOfRows > 0)
/*      */       {
/* 1991 */         return this.rowPosition == 0;
/*      */       }
/*      */ 
/* 1994 */       return false;
/*      */     }
/*      */ 
/* 1998 */     throw new SQLException("Result set type is TYPE_FORWARD_ONLY");
/*      */   }
/*      */ 
/*      */   public boolean isAfterLast()
/*      */     throws SQLException
/*      */   {
/* 2012 */     checkOpen();
/*      */ 
/* 2014 */     if (this.closed) {
/* 2015 */       throw new SQLException("ResultSet is closed");
/*      */     }
/*      */ 
/* 2018 */     if (getType() != 1003)
/*      */     {
/* 2020 */       if (this.numberOfRows > 0)
/*      */       {
/* 2022 */         return this.rowPosition > this.numberOfRows;
/*      */       }
/*      */ 
/* 2026 */       return false;
/*      */     }
/*      */ 
/* 2031 */     throw new SQLException("Result set type is TYPE_FORWARD_ONLY");
/*      */   }
/*      */ 
/*      */   public boolean isFirst()
/*      */     throws SQLException
/*      */   {
/* 2044 */     checkOpen();
/*      */ 
/* 2046 */     if (getType() != 1003)
/*      */     {
/* 2048 */       if (this.numberOfRows > 0)
/*      */       {
/* 2050 */         return this.rowPosition == 1;
/*      */       }
/* 2052 */       return false;
/*      */     }
/*      */ 
/* 2056 */     throw new SQLException("Result set type is TYPE_FORWARD_ONLY");
/*      */   }
/*      */ 
/*      */   public boolean isLast()
/*      */     throws SQLException
/*      */   {
/* 2069 */     checkOpen();
/*      */ 
/* 2071 */     if (getType() != 1003)
/*      */     {
/* 2073 */       if (this.numberOfRows > 0)
/*      */       {
/* 2075 */         return this.rowPosition == this.numberOfRows;
/*      */       }
/*      */ 
/* 2078 */       return false;
/*      */     }
/*      */ 
/* 2082 */     throw new SQLException("Result set type is TYPE_FORWARD_ONLY");
/*      */   }
/*      */ 
/*      */   public void beforeFirst()
/*      */     throws SQLException
/*      */   {
/* 2096 */     checkOpen();
/*      */ 
/* 2098 */     if (getType() != 1003)
/*      */     {
/* 2100 */       boolean bool = false;
/*      */ 
/* 2102 */       bool = fetchScrollOption(0, (short)5);
/*      */ 
/* 2105 */       this.rowPosition = 0;
/* 2106 */       this.currentBlockCell = 0;
/*      */ 
/* 2108 */       if (this.atInsertRow)
/*      */       {
/* 2110 */         this.lastRowPosition = 0;
/* 2111 */         this.lastBlockPosition = 0;
/* 2112 */         this.atInsertRow = false;
/*      */       }
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/* 2118 */       throw new SQLException("Result set type is TYPE_FORWARD_ONLY");
/*      */     }
/*      */   }
/*      */ 
/*      */   public void afterLast()
/*      */     throws SQLException
/*      */   {
/* 2132 */     checkOpen();
/*      */ 
/* 2134 */     boolean bool = false;
/*      */ 
/* 2136 */     if (getType() != 1003)
/*      */     {
/* 2139 */       bool = fetchScrollOption(this.numberOfRows + 1, (short)5);
/*      */ 
/* 2142 */       this.rowPosition = (this.numberOfRows + 1);
/* 2143 */       this.currentBlockCell = (this.rowSet + 1);
/*      */ 
/* 2145 */       if (this.atInsertRow)
/*      */       {
/* 2147 */         this.lastRowPosition = 0;
/* 2148 */         this.lastBlockPosition = 0;
/* 2149 */         this.atInsertRow = false;
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/* 2154 */       throw new SQLException("Result set type is TYPE_FORWARD_ONLY");
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean first()
/*      */     throws SQLException
/*      */   {
/* 2168 */     checkOpen();
/*      */ 
/* 2170 */     if (getType() != 1003)
/*      */     {
/* 2172 */       if (this.numberOfRows > 0)
/*      */       {
/* 2174 */         boolean bool1 = false;
/* 2175 */         boolean bool2 = false;
/*      */ 
/* 2177 */         if (this.blockCursor)
/*      */         {
/* 2179 */           bool1 = blockFetch(1, (short)2);
/*      */ 
/* 2181 */           if (!bool1) {
/* 2182 */             bool2 = true;
/*      */           }
/*      */         }
/* 2185 */         if ((!this.blockCursor) || (bool1))
/*      */         {
/* 2187 */           resetInsertRow();
/*      */ 
/* 2189 */           this.lastColumnNull = false;
/*      */ 
/* 2193 */           closeInputStreams();
/*      */ 
/* 2197 */           clearWarnings();
/*      */ 
/* 2199 */           bool2 = fetchScrollOption(this.rowPosition, (short)2);
/*      */         }
/*      */ 
/* 2202 */         if (bool2)
/*      */         {
/* 2204 */           this.rowPosition = 1;
/* 2205 */           this.currentBlockCell = this.rowPosition;
/*      */         }
/*      */ 
/* 2208 */         return bool2;
/*      */       }
/*      */ 
/* 2211 */       return false;
/*      */     }
/*      */ 
/* 2216 */     throw new SQLException("Result set type is TYPE_FORWARD_ONLY");
/*      */   }
/*      */ 
/*      */   public boolean last()
/*      */     throws SQLException
/*      */   {
/* 2231 */     checkOpen();
/*      */ 
/* 2233 */     if (getType() != 1003)
/*      */     {
/* 2235 */       if (this.numberOfRows > 0)
/*      */       {
/* 2239 */         moveToCurrentRow();
/*      */ 
/* 2241 */         boolean bool1 = false;
/* 2242 */         boolean bool2 = false;
/*      */ 
/* 2244 */         if (this.blockCursor)
/*      */         {
/* 2246 */           bool1 = blockFetch(this.numberOfRows, (short)3);
/*      */ 
/* 2248 */           if (!bool1)
/*      */           {
/* 2250 */             setPos(this.currentBlockCell, 0);
/* 2251 */             bool2 = true;
/*      */           }
/*      */           else {
/* 2254 */             bool2 = true;
/*      */           }
/*      */         }
/*      */ 
/* 2258 */         if ((!this.blockCursor) || (bool1))
/*      */         {
/* 2260 */           resetInsertRow();
/*      */ 
/* 2262 */           this.lastColumnNull = false;
/*      */ 
/* 2266 */           closeInputStreams();
/*      */ 
/* 2270 */           clearWarnings();
/*      */ 
/* 2272 */           if (bool1)
/* 2273 */             bool2 = fetchScrollOption(this.numberOfRows, (short)5);
/*      */           else {
/* 2275 */             bool2 = fetchScrollOption(this.numberOfRows, (short)3);
/*      */           }
/* 2277 */           if (bool2)
/* 2278 */             this.rowPosition = this.numberOfRows;
/* 2279 */           this.currentBlockCell = 1;
/*      */         }
/*      */ 
/* 2282 */         return bool2;
/*      */       }
/*      */ 
/* 2286 */       return false;
/*      */     }
/*      */ 
/* 2290 */     throw new SQLException("Result set type is TYPE_FORWARD_ONLY");
/*      */   }
/*      */ 
/*      */   public int getRow()
/*      */     throws SQLException
/*      */   {
/* 2305 */     checkOpen();
/* 2306 */     if (getType() == 1003)
/*      */     {
/* 2308 */       if (this.lastForwardRecord == 0)
/*      */       {
/* 2310 */         return this.rowPosition;
/*      */       }
/*      */ 
/* 2314 */       return 0;
/*      */     }
/*      */ 
/* 2321 */     if (this.numberOfRows > 0)
/*      */     {
/* 2323 */       if ((this.rowPosition <= 0) || (this.rowPosition > this.numberOfRows))
/*      */       {
/* 2325 */         return 0;
/*      */       }
/*      */ 
/* 2330 */       return this.rowPosition;
/*      */     }
/*      */ 
/* 2334 */     return 0;
/*      */   }
/*      */ 
/*      */   public boolean absolute(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 2348 */     checkOpen();
/*      */ 
/* 2350 */     if (getType() != 1003)
/*      */     {
/* 2352 */       if (this.numberOfRows > 0)
/*      */       {
/* 2355 */         boolean bool1 = false;
/* 2356 */         boolean bool2 = false;
/*      */ 
/* 2358 */         if (paramInt != 0)
/*      */         {
/* 2361 */           if (this.blockCursor)
/*      */           {
/* 2363 */             if (this.atInsertRow)
/*      */             {
/* 2365 */               this.rowPosition = this.lastRowPosition;
/* 2366 */               this.currentBlockCell = this.lastBlockPosition;
/* 2367 */               this.atInsertRow = false;
/*      */             }
/*      */ 
/* 2370 */             bool2 = blockFetch(paramInt, (short)5);
/*      */ 
/* 2372 */             if (bool2)
/*      */             {
/* 2374 */               this.currentBlockCell = 1;
/*      */             }
/*      */             else
/*      */             {
/* 2379 */               setPos(this.currentBlockCell, 0);
/* 2380 */               bool1 = true;
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/* 2385 */           if ((!this.blockCursor) || (bool2))
/*      */           {
/* 2388 */             if (paramInt >= 0)
/* 2389 */               this.rowPosition = paramInt;
/*      */             else {
/* 2391 */               this.rowPosition = (this.numberOfRows + 1 + paramInt);
/*      */             }
/* 2393 */             if (this.rowPosition > this.numberOfRows)
/*      */             {
/* 2395 */               afterLast();
/* 2396 */               return false;
/*      */             }
/* 2398 */             if (this.rowPosition < 1)
/*      */             {
/* 2400 */               beforeFirst();
/* 2401 */               return false;
/*      */             }
/*      */ 
/* 2404 */             this.lastColumnNull = false;
/*      */ 
/* 2408 */             closeInputStreams();
/*      */ 
/* 2412 */             clearWarnings();
/*      */ 
/* 2414 */             bool1 = fetchScrollOption(paramInt, (short)5);
/*      */           }
/*      */ 
/* 2417 */           return bool1;
/*      */         }
/*      */ 
/* 2422 */         throw new SQLException("Cursor position (" + paramInt + ") is invalid");
/*      */       }
/*      */ 
/* 2427 */       return false;
/*      */     }
/*      */ 
/* 2431 */     throw new SQLException("Result set type is TYPE_FORWARD_ONLY");
/*      */   }
/*      */ 
/*      */   protected boolean fetchScrollOption(int paramInt, short paramShort)
/*      */     throws SQLException
/*      */   {
/* 2447 */     if (this.numberOfRows > 0)
/*      */     {
/*      */       try {
/* 2450 */         this.OdbcApi.SQLFetchScroll(this.hStmt, paramShort, paramInt);
/*      */       }
/*      */       catch (SQLWarning localSQLWarning)
/*      */       {
/* 2462 */         setWarning(localSQLWarning);
/*      */ 
/* 2464 */         return true;
/*      */       }
/*      */       catch (SQLException localSQLException)
/*      */       {
/* 2468 */         return false;
/*      */       }
/* 2470 */       return true;
/*      */     }
/*      */ 
/* 2473 */     return false;
/*      */   }
/*      */ 
/*      */   protected void consecutiveFetch(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 2490 */     int i = 0;
/*      */ 
/* 2492 */     if ((this.blockCursor) && (this.rowUpdated)) {
/* 2493 */       i = 1;
/*      */     }
/* 2495 */     if (this.rowSet != 1)
/*      */     {
/* 2499 */       if ((this.lastColumnData == paramInt) || (i != 0))
/*      */       {
/*      */         try {
/* 2502 */           this.OdbcApi.SQLFetchScroll(this.hStmt, (short)5, getRow());
/*      */ 
/* 2507 */           this.lastColumnData = 0;
/*      */ 
/* 2509 */           if (this.blockCursor)
/*      */           {
/* 2511 */             this.currentBlockCell = 1;
/*      */           }
/*      */ 
/*      */         }
/*      */         catch (SQLWarning localSQLWarning)
/*      */         {
/* 2522 */           setWarning(localSQLWarning);
/*      */         }
/*      */         catch (SQLException localSQLException)
/*      */         {
/*      */         }
/*      */ 
/* 2529 */         this.rowUpdated = false;
/*      */       }
/*      */       else {
/* 2532 */         this.lastColumnData = paramInt;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean relative(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 2543 */     checkOpen();
/* 2544 */     return relative(paramInt, true);
/*      */   }
/*      */ 
/*      */   protected boolean relative(int paramInt, boolean paramBoolean)
/*      */     throws SQLException
/*      */   {
/* 2556 */     checkOpen();
/*      */ 
/* 2558 */     if (getType() != 1003)
/*      */     {
/* 2560 */       if (this.numberOfRows > 0)
/*      */       {
/* 2565 */         moveToCurrentRow();
/*      */ 
/* 2567 */         boolean bool1 = false;
/* 2568 */         boolean bool2 = false;
/* 2569 */         int i = -1;
/*      */ 
/* 2576 */         if (paramBoolean)
/*      */         {
/* 2578 */           if (this.rowPosition == 0)
/*      */           {
/* 2580 */             throw new SQLException("Cursor is positioned before the ResultSet");
/*      */           }
/*      */ 
/* 2583 */           if (this.rowPosition > this.numberOfRows)
/*      */           {
/* 2585 */             throw new SQLException("Cursor is positioned after the ResultSet");
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/* 2593 */         if (this.blockCursor)
/*      */         {
/* 2595 */           bool2 = blockFetch(this.rowPosition + paramInt, (short)5);
/*      */ 
/* 2600 */           if (bool2)
/*      */           {
/* 2602 */             i = this.rowPosition;
/* 2603 */             i += paramInt;
/*      */           }
/* 2605 */           else if (paramInt == 0)
/*      */           {
/* 2607 */             i = paramInt;
/* 2608 */             bool2 = true;
/*      */           }
/*      */         }
/*      */         else
/*      */         {
/* 2613 */           i = this.rowPosition;
/* 2614 */           i += paramInt;
/*      */         }
/*      */ 
/* 2621 */         if ((i <= 1) && (paramInt < 0))
/*      */         {
/* 2623 */           beforeFirst();
/*      */ 
/* 2625 */           if (paramBoolean)
/* 2626 */             return false;
/* 2627 */           if (i == 1)
/* 2628 */             return true;
/*      */         }
/* 2630 */         else if ((i >= this.numberOfRows) && (paramInt > 0))
/*      */         {
/* 2632 */           afterLast();
/*      */ 
/* 2634 */           if (paramBoolean)
/* 2635 */             return false;
/* 2636 */           if (i == this.numberOfRows) {
/* 2637 */             return true;
/*      */           }
/*      */         }
/*      */         else
/*      */         {
/* 2642 */           this.lastColumnNull = false;
/*      */ 
/* 2646 */           closeInputStreams();
/*      */ 
/* 2650 */           clearWarnings();
/*      */ 
/* 2652 */           if (this.blockCursor)
/*      */           {
/* 2654 */             if (bool2)
/*      */             {
/* 2656 */               bool1 = fetchScrollOption(i, (short)5);
/*      */ 
/* 2658 */               if (bool1)
/*      */               {
/* 2660 */                 this.rowPosition = i;
/* 2661 */                 this.currentBlockCell = 1;
/*      */               }
/*      */ 
/*      */             }
/*      */             else
/*      */             {
/* 2667 */               setPos(this.currentBlockCell, 0);
/* 2668 */               bool1 = true;
/*      */             }
/*      */           }
/*      */           else
/*      */           {
/* 2673 */             bool1 = fetchScrollOption(paramInt, (short)6);
/*      */ 
/* 2675 */             if (bool1)
/* 2676 */               this.rowPosition = i;
/*      */           }
/* 2678 */           return bool1;
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/* 2683 */         throw new SQLException("Call to relative(" + paramInt + ") when there is no current row.");
/*      */       }
/* 2685 */       return false;
/*      */     }
/*      */ 
/* 2690 */     throw new SQLException("Result set type is TYPE_FORWARD_ONLY");
/*      */   }
/*      */ 
/*      */   public boolean previous()
/*      */     throws SQLException
/*      */   {
/* 2704 */     checkOpen();
/*      */ 
/* 2706 */     if (getType() != 1003)
/*      */     {
/* 2708 */       int i = 0;
/*      */ 
/* 2710 */       if (this.numberOfRows > 0)
/*      */       {
/* 2713 */         if (this.atInsertRow) {
/* 2714 */           i = this.lastRowPosition;
/*      */         }
/*      */ 
/* 2718 */         moveToCurrentRow();
/*      */ 
/* 2720 */         if (getFetchDirection() == 1000)
/*      */         {
/* 2722 */           if (i > 0)
/* 2723 */             return absolute(i - 1);
/* 2724 */           if (this.rowPosition > 1)
/* 2725 */             return absolute(this.rowPosition - 1);
/* 2726 */           if (this.rowPosition == 1)
/*      */           {
/* 2728 */             beforeFirst();
/* 2729 */             return false;
/*      */           }
/* 2731 */           if (isBeforeFirst()) {
/* 2732 */             return false;
/*      */           }
/* 2734 */           return false;
/*      */         }
/*      */ 
/* 2739 */         if (i > 0)
/* 2740 */           return absolute(i + 1);
/* 2741 */         if (this.rowPosition < this.numberOfRows)
/* 2742 */           return absolute(this.rowPosition + 1);
/* 2743 */         if (this.rowPosition == this.numberOfRows)
/*      */         {
/* 2745 */           afterLast();
/* 2746 */           return false;
/*      */         }
/* 2748 */         if (isAfterLast()) {
/* 2749 */           return false;
/*      */         }
/* 2751 */         return false;
/*      */       }
/*      */ 
/* 2757 */       return false;
/*      */     }
/*      */ 
/* 2761 */     throw new SQLException("Result set type is TYPE_FORWARD_ONLY");
/*      */   }
/*      */ 
/*      */   protected boolean blockFetch(int paramInt, short paramShort)
/*      */     throws SQLException
/*      */   {
/* 2776 */     boolean bool = false;
/*      */ 
/* 2778 */     if ((isBeforeFirst()) || (isAfterLast()))
/*      */     {
/* 2780 */       return true;
/*      */     }
/*      */ 
/* 2783 */     switch (paramShort)
/*      */     {
/*      */     case 2:
/* 2788 */       if (this.rowPosition != 1)
/*      */       {
/* 2790 */         if (!isRowWithinTheBlock(1))
/*      */         {
/* 2792 */           bool = true;
/*      */         }
/*      */         else
/*      */         {
/* 2796 */           this.rowPosition = 1;
/* 2797 */           this.currentBlockCell = this.rowPosition; }  } break;
/*      */     case 3:
/* 2805 */       if (this.rowPosition < this.numberOfRows)
/*      */       {
/* 2807 */         if (!isRowWithinTheBlock(this.numberOfRows))
/*      */         {
/* 2809 */           bool = true;
/*      */         }
/*      */         else
/*      */         {
/* 2815 */           while (this.rowPosition != this.numberOfRows)
/*      */           {
/* 2817 */             this.rowPosition += 1;
/* 2818 */             this.currentBlockCell += 1;
/*      */           }
/* 2820 */           bool = false;
/*      */         }
/*      */ 
/*      */       }
/* 2825 */       else if (this.rowPosition == this.numberOfRows)
/*      */       {
/* 2827 */         bool = false; } break;
/*      */     case 5:
/* 2835 */       if (this.rowPosition != paramInt)
/*      */       {
/* 2837 */         if ((paramInt < 0) || (paramInt > this.numberOfRows))
/*      */         {
/* 2841 */           bool = true;
/*      */         }
/* 2843 */         else if (!isRowWithinTheBlock(paramInt))
/*      */         {
/* 2845 */           bool = true;
/*      */         }
/*      */         else
/*      */         {
/* 2851 */           while (this.rowPosition != paramInt)
/*      */           {
/* 2853 */             if (this.moveUpBlock)
/*      */             {
/* 2855 */               this.rowPosition -= 1;
/* 2856 */               this.currentBlockCell -= 1;
/*      */             }
/* 2858 */             else if (this.moveDownBlock)
/*      */             {
/* 2860 */               this.rowPosition += 1;
/* 2861 */               this.currentBlockCell += 1;
/*      */             }
/*      */           }
/*      */ 
/* 2865 */           bool = false; }  } break;
/*      */     case 4:
/*      */     }
/*      */ 
/* 2878 */     return bool;
/*      */   }
/*      */ 
/*      */   protected boolean isRowWithinTheBlock(int paramInt)
/*      */   {
/* 2889 */     boolean bool = false;
/*      */ 
/* 2891 */     if (this.rowPosition != 0)
/*      */     {
/* 2893 */       int i = this.rowPosition - (this.currentBlockCell - 1);
/* 2894 */       int j = this.rowPosition + (this.rowSet - this.currentBlockCell);
/*      */ 
/* 2896 */       if ((j < paramInt) || (i > paramInt))
/*      */       {
/* 2898 */         bool = false;
/*      */       }
/* 2900 */       else if (paramInt > this.rowPosition)
/*      */       {
/* 2902 */         bool = true;
/* 2903 */         this.moveUpBlock = false;
/* 2904 */         this.moveDownBlock = true;
/*      */       }
/* 2906 */       else if (paramInt < this.rowPosition)
/*      */       {
/* 2908 */         bool = true;
/* 2909 */         this.moveUpBlock = true;
/* 2910 */         this.moveDownBlock = false;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2915 */     return bool;
/*      */   }
/*      */ 
/*      */   protected int getRowIndex()
/*      */   {
/* 2925 */     int i = 0;
/*      */ 
/* 2927 */     if (this.blockCursor)
/*      */     {
/* 2929 */       i = this.currentBlockCell - 1;
/*      */     }
/* 2931 */     else if (this.atInsertRow)
/*      */     {
/* 2933 */       i = this.rowSet;
/*      */     }
/*      */ 
/* 2936 */     return i;
/*      */   }
/*      */ 
/*      */   public void setFetchDirection(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 2948 */     checkOpen();
/* 2949 */     this.ownerStatement.setFetchDirection(paramInt);
/*      */   }
/*      */ 
/*      */   public int getFetchDirection()
/*      */     throws SQLException
/*      */   {
/* 2960 */     checkOpen();
/* 2961 */     return this.ownerStatement.getFetchDirection();
/*      */   }
/*      */ 
/*      */   public void setFetchSize(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 2973 */     checkOpen();
/* 2974 */     this.ownerStatement.setFetchSize(paramInt);
/*      */   }
/*      */ 
/*      */   public int getFetchSize()
/*      */     throws SQLException
/*      */   {
/* 2985 */     checkOpen();
/* 2986 */     return this.ownerStatement.getFetchSize();
/*      */   }
/*      */ 
/*      */   public int getType()
/*      */     throws SQLException
/*      */   {
/* 2998 */     checkOpen();
/* 2999 */     if (this.ownerStatement != null) {
/* 3000 */       return this.ownerStatement.getResultSetType();
/*      */     }
/* 3002 */     return 1003;
/*      */   }
/*      */ 
/*      */   public int getConcurrency()
/*      */     throws SQLException
/*      */   {
/* 3014 */     checkOpen();
/* 3015 */     return this.ownerStatement.getResultSetConcurrency();
/*      */   }
/*      */ 
/*      */   public boolean rowUpdated()
/*      */     throws SQLException
/*      */   {
/* 3027 */     checkOpen();
/* 3028 */     if (this.numberOfRows > 0)
/*      */     {
/* 3030 */       int i = getRowIndex();
/*      */ 
/* 3032 */       if (this.blockCursor)
/*      */       {
/* 3034 */         return this.rowStatusArray[i] == 2;
/*      */       }
/*      */ 
/* 3037 */       return this.rowStatusArray[(this.rowSet - 1)] == 2;
/*      */     }
/*      */ 
/* 3040 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean rowInserted()
/*      */     throws SQLException
/*      */   {
/* 3052 */     checkOpen();
/* 3053 */     if (this.numberOfRows > 0)
/*      */     {
/* 3055 */       int i = getRowIndex();
/*      */ 
/* 3057 */       if (this.blockCursor)
/*      */       {
/* 3059 */         return this.rowStatusArray[i] == 4;
/*      */       }
/*      */ 
/* 3062 */       return this.rowStatusArray[(this.rowSet - 1)] == 4;
/*      */     }
/*      */ 
/* 3065 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean rowDeleted()
/*      */     throws SQLException
/*      */   {
/* 3077 */     checkOpen();
/* 3078 */     if (this.numberOfRows > 0)
/*      */     {
/* 3081 */       int i = getRowIndex();
/*      */ 
/* 3083 */       if (this.blockCursor)
/*      */       {
/* 3085 */         return this.rowStatusArray[i] == 1;
/*      */       }
/*      */ 
/* 3088 */       return this.rowStatusArray[(this.rowSet - 1)] == 1;
/*      */     }
/*      */ 
/* 3091 */     return false;
/*      */   }
/*      */ 
/*      */   public void updateNull(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 3103 */     checkOpen();
/* 3104 */     int i = getRowIndex();
/*      */ 
/* 3106 */     if ((paramInt > 0) && (paramInt <= this.numberOfCols))
/*      */     {
/* 3109 */       int j = getColumnType(paramInt);
/*      */ 
/* 3112 */       if (j != 9999)
/*      */       {
/* 3117 */         this.boundCols[(paramInt - 1)].setRowValues(i, null, -1);
/*      */       }
/*      */       else
/* 3120 */         throw new SQLException("Unknown Data Type for column [#" + paramInt + "]");
/*      */     }
/*      */   }
/*      */ 
/*      */   public void updateBoolean(int paramInt, boolean paramBoolean)
/*      */     throws SQLException
/*      */   {
/* 3134 */     checkOpen();
/* 3135 */     int i = 0;
/*      */ 
/* 3138 */     if (paramBoolean) {
/* 3139 */       i = 1;
/*      */     }
/*      */ 
/* 3143 */     updateInt(paramInt, i);
/*      */   }
/*      */ 
/*      */   public void updateByte(int paramInt, byte paramByte)
/*      */     throws SQLException
/*      */   {
/* 3156 */     checkOpen();
/*      */ 
/* 3159 */     int i = getRowIndex();
/*      */ 
/* 3161 */     if ((paramInt > 0) && (paramInt <= this.numberOfCols))
/*      */     {
/* 3164 */       int j = getColumnType(paramInt);
/*      */ 
/* 3166 */       if (j != -6) {
/* 3167 */         this.boundCols[(paramInt - 1)].setType(-6);
/*      */       }
/*      */ 
/* 3170 */       this.boundCols[(paramInt - 1)].setRowValues(i, new Integer(paramByte), 4);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void updateShort(int paramInt, short paramShort)
/*      */     throws SQLException
/*      */   {
/* 3184 */     checkOpen();
/*      */ 
/* 3187 */     int i = getRowIndex();
/*      */ 
/* 3189 */     if ((paramInt > 0) && (paramInt <= this.numberOfCols))
/*      */     {
/* 3192 */       int j = getColumnType(paramInt);
/*      */ 
/* 3194 */       if (j != 5) {
/* 3195 */         this.boundCols[(paramInt - 1)].setType(5);
/*      */       }
/*      */ 
/* 3198 */       this.boundCols[(paramInt - 1)].setRowValues(i, new Integer(paramShort), 4);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void updateInt(int paramInt1, int paramInt2)
/*      */     throws SQLException
/*      */   {
/* 3214 */     checkOpen();
/*      */ 
/* 3216 */     int i = getRowIndex();
/*      */ 
/* 3218 */     if ((paramInt1 > 0) && (paramInt1 <= this.numberOfCols))
/*      */     {
/* 3221 */       int j = getColumnType(paramInt1);
/*      */ 
/* 3223 */       if (j != 4) {
/* 3224 */         this.boundCols[(paramInt1 - 1)].setType(4);
/*      */       }
/*      */ 
/* 3227 */       this.boundCols[(paramInt1 - 1)].setRowValues(i, new Integer(paramInt2), 4);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void updateLong(int paramInt, long paramLong)
/*      */     throws SQLException
/*      */   {
/* 3241 */     checkOpen();
/*      */ 
/* 3243 */     updateFloat(paramInt, (float)paramLong);
/*      */   }
/*      */ 
/*      */   public void updateFloat(int paramInt, float paramFloat)
/*      */     throws SQLException
/*      */   {
/* 3256 */     checkOpen();
/* 3257 */     int i = getRowIndex();
/*      */ 
/* 3259 */     if ((paramInt > 0) && (paramInt <= this.numberOfCols))
/*      */     {
/* 3262 */       int j = getColumnType(paramInt);
/*      */ 
/* 3264 */       if (j != 6) {
/* 3265 */         this.boundCols[(paramInt - 1)].setType(6);
/*      */       }
/*      */ 
/* 3268 */       this.boundCols[(paramInt - 1)].setRowValues(i, new Float(paramFloat), 4);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void updateDouble(int paramInt, double paramDouble)
/*      */     throws SQLException
/*      */   {
/* 3282 */     checkOpen();
/* 3283 */     int i = getRowIndex();
/*      */ 
/* 3285 */     if ((paramInt > 0) && (paramInt <= this.numberOfCols))
/*      */     {
/* 3288 */       int j = getColumnType(paramInt);
/*      */ 
/* 3290 */       if (j != 8) {
/* 3291 */         this.boundCols[(paramInt - 1)].setType(8);
/*      */       }
/*      */ 
/* 3294 */       this.boundCols[(paramInt - 1)].setRowValues(i, new Double(paramDouble), 8);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void updateBigDecimal(int paramInt, BigDecimal paramBigDecimal)
/*      */     throws SQLException
/*      */   {
/* 3308 */     checkOpen();
/* 3309 */     int i = getColumnType(paramInt);
/*      */ 
/* 3311 */     if ((i == 3) || (i == 2))
/*      */     {
/* 3314 */       if (paramBigDecimal == null)
/* 3315 */         updateChar(paramInt, i, null);
/*      */       else {
/* 3317 */         updateChar(paramInt, i, paramBigDecimal.toString());
/*      */       }
/*      */ 
/*      */     }
/* 3323 */     else if (paramBigDecimal == null)
/* 3324 */       updateChar(paramInt, 2, null);
/*      */     else
/* 3326 */       updateChar(paramInt, 2, paramBigDecimal.toString());
/*      */   }
/*      */ 
/*      */   public void updateString(int paramInt, String paramString)
/*      */     throws SQLException
/*      */   {
/* 3342 */     checkOpen();
/*      */     byte[] arrayOfByte;
/*      */     try
/*      */     {
/* 3348 */       if (paramString == null)
/* 3349 */         arrayOfByte = null;
/*      */       else
/* 3351 */         arrayOfByte = paramString.getBytes(this.OdbcApi.charSet);
/*      */     }
/*      */     catch (UnsupportedEncodingException localUnsupportedEncodingException)
/*      */     {
/* 3355 */       throw new SQLException(localUnsupportedEncodingException.getMessage());
/*      */     }
/*      */ 
/* 3358 */     updateBytes(paramInt, arrayOfByte);
/*      */   }
/*      */ 
/*      */   public void updateBytes(int paramInt, byte[] paramArrayOfByte)
/*      */     throws SQLException
/*      */   {
/* 3372 */     checkOpen();
/*      */ 
/* 3374 */     int i = getRowIndex();
/*      */ 
/* 3377 */     if ((paramArrayOfByte != null) && (paramArrayOfByte.length > 8000))
/*      */     {
/* 3379 */       updateBinaryStream(paramInt, new ByteArrayInputStream(paramArrayOfByte), paramArrayOfByte.length);
/*      */     }
/*      */ 
/* 3382 */     if ((paramInt > 0) && (paramInt <= this.numberOfCols))
/*      */     {
/* 3387 */       int j = getColumnType(paramInt);
/*      */ 
/* 3389 */       if ((j != -2) && (j != -3)) {
/* 3390 */         this.boundCols[(paramInt - 1)].setType(-2);
/*      */ 
/* 3392 */         if (paramArrayOfByte != null) {
/* 3393 */           this.boundCols[(paramInt - 1)].setLength(paramArrayOfByte.length);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 3398 */       if (paramArrayOfByte == null) {
/* 3399 */         updateNull(paramInt);
/* 3400 */         return;
/*      */       }
/*      */ 
/* 3404 */       if (paramArrayOfByte.length > this.boundCols[(paramInt - 1)].getLength()) {
/* 3405 */         this.boundCols[(paramInt - 1)].setLength(paramArrayOfByte.length);
/*      */       }
/* 3407 */       this.boundCols[(paramInt - 1)].setRowValues(i, (byte[])paramArrayOfByte, paramArrayOfByte.length);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void updateDate(int paramInt, Date paramDate)
/*      */     throws SQLException
/*      */   {
/* 3422 */     checkOpen();
/* 3423 */     int i = getRowIndex();
/*      */ 
/* 3425 */     if ((paramInt > 0) && (paramInt <= this.numberOfCols))
/*      */     {
/* 3428 */       if (getColumnType(paramInt) != 91) {
/* 3429 */         this.boundCols[(paramInt - 1)].setType(91);
/*      */       }
/*      */ 
/* 3432 */       if (paramDate == null) {
/* 3433 */         updateNull(paramInt);
/* 3434 */         return;
/*      */       }
/*      */ 
/* 3438 */       this.boundCols[(paramInt - 1)].setRowValues(i, paramDate, 6);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void updateTime(int paramInt, Time paramTime)
/*      */     throws SQLException
/*      */   {
/* 3452 */     checkOpen();
/* 3453 */     int i = getRowIndex();
/*      */ 
/* 3455 */     if ((paramInt > 0) && (paramInt <= this.numberOfCols))
/*      */     {
/* 3459 */       if (getColumnType(paramInt) != 92) {
/* 3460 */         this.boundCols[(paramInt - 1)].setType(92);
/*      */       }
/*      */ 
/* 3463 */       if (paramTime == null) {
/* 3464 */         updateNull(paramInt);
/* 3465 */         return;
/*      */       }
/*      */ 
/* 3469 */       this.boundCols[(paramInt - 1)].setRowValues(i, paramTime, 6);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void updateTimestamp(int paramInt, Timestamp paramTimestamp)
/*      */     throws SQLException
/*      */   {
/* 3484 */     checkOpen();
/* 3485 */     int i = getRowIndex();
/*      */ 
/* 3487 */     if ((paramInt > 0) && (paramInt <= this.numberOfCols))
/*      */     {
/* 3491 */       if (getColumnType(paramInt) != 93) {
/* 3492 */         this.boundCols[(paramInt - 1)].setType(93);
/*      */       }
/*      */ 
/* 3495 */       if (paramTimestamp == null) {
/* 3496 */         updateNull(paramInt);
/* 3497 */         return;
/*      */       }
/*      */ 
/* 3501 */       this.boundCols[(paramInt - 1)].setRowValues(i, paramTimestamp, 16);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void updateAsciiStream(int paramInt1, InputStream paramInputStream, int paramInt2)
/*      */     throws SQLException
/*      */   {
/* 3531 */     checkOpen();
/* 3532 */     int i = getRowIndex();
/*      */ 
/* 3534 */     if ((paramInt1 > 0) && (paramInt1 <= this.numberOfCols))
/*      */     {
/* 3537 */       if (getColumnType(paramInt1) != -1) {
/* 3538 */         this.boundCols[(paramInt1 - 1)].setType(-1);
/*      */       }
/*      */ 
/* 3541 */       if (paramInputStream == null) {
/* 3542 */         updateNull(paramInt1);
/* 3543 */         return;
/*      */       }
/*      */ 
/* 3547 */       if (paramInt2 != this.boundCols[(paramInt1 - 1)].getLength()) {
/* 3548 */         this.boundCols[(paramInt1 - 1)].setLength(paramInt2);
/*      */       }
/* 3550 */       this.boundCols[(paramInt1 - 1)].setRowValues(i, paramInputStream, paramInt2);
/* 3551 */       this.boundCols[(paramInt1 - 1)].setStreamType(1);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void updateBinaryStream(int paramInt1, InputStream paramInputStream, int paramInt2)
/*      */     throws SQLException
/*      */   {
/* 3567 */     checkOpen();
/* 3568 */     int i = getRowIndex();
/*      */ 
/* 3570 */     if ((paramInt1 > 0) && (paramInt1 <= this.numberOfCols))
/*      */     {
/* 3574 */       if (getColumnType(paramInt1) != -4) {
/* 3575 */         this.boundCols[(paramInt1 - 1)].setType(-4);
/*      */       }
/*      */ 
/* 3578 */       if (paramInputStream == null) {
/* 3579 */         updateNull(paramInt1);
/* 3580 */         return;
/*      */       }
/*      */ 
/* 3583 */       if (paramInt2 != this.boundCols[(paramInt1 - 1)].getLength()) {
/* 3584 */         this.boundCols[(paramInt1 - 1)].setLength(paramInt2);
/*      */       }
/* 3586 */       this.boundCols[(paramInt1 - 1)].setRowValues(i, paramInputStream, paramInt2);
/* 3587 */       this.boundCols[(paramInt1 - 1)].setStreamType(3);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void updateCharacterStream(int paramInt1, Reader paramReader, int paramInt2)
/*      */     throws SQLException
/*      */   {
/* 3602 */     checkOpen();
/* 3603 */     int i = getRowIndex();
/*      */ 
/* 3606 */     BufferedReader localBufferedReader = null;
/* 3607 */     BufferedOutputStream localBufferedOutputStream = null;
/*      */ 
/* 3610 */     ByteArrayOutputStream localByteArrayOutputStream = null;
/* 3611 */     ByteArrayInputStream localByteArrayInputStream = null;
/*      */ 
/* 3615 */     String str = this.OdbcApi.charSet;
/*      */ 
/* 3618 */     int j = 300;
/*      */ 
/* 3620 */     if (paramInt2 < j) {
/* 3621 */       j = paramInt2;
/*      */     }
/* 3623 */     int k = 0;
/* 3624 */     int m = 0;
/*      */     try
/*      */     {
/* 3628 */       m = (int)Charset.forName(str).newEncoder().maxBytesPerChar();
/*      */     }
/*      */     catch (UnsupportedCharsetException localUnsupportedCharsetException)
/*      */     {
/*      */     }
/*      */     catch (IllegalCharsetNameException localIllegalCharsetNameException)
/*      */     {
/*      */     }
/* 3636 */     if (m == 0) {
/* 3637 */       m = 1;
/*      */     }
/*      */     try
/*      */     {
/* 3641 */       if (paramReader != null)
/*      */       {
/* 3643 */         int n = 0;
/* 3644 */         int i1 = 0;
/*      */ 
/* 3646 */         localBufferedReader = new BufferedReader(paramReader);
/* 3647 */         localByteArrayOutputStream = new ByteArrayOutputStream();
/* 3648 */         localBufferedOutputStream = new BufferedOutputStream(localByteArrayOutputStream);
/*      */ 
/* 3650 */         char[] arrayOfChar1 = new char[j];
/*      */ 
/* 3652 */         while (i1 != -1)
/*      */         {
/* 3654 */           byte[] arrayOfByte = new byte[0];
/*      */ 
/* 3656 */           i1 = localBufferedReader.read(arrayOfChar1);
/*      */ 
/* 3658 */           if (i1 != -1)
/*      */           {
/* 3665 */             char[] arrayOfChar2 = new char[i1];
/*      */ 
/* 3667 */             for (int i2 = 0; i2 < i1; i2++)
/*      */             {
/* 3669 */               arrayOfChar2[i2] = arrayOfChar1[i2];
/*      */             }
/*      */ 
/* 3682 */             arrayOfByte = CharsToBytes(str, arrayOfChar2);
/*      */ 
/* 3685 */             i2 = arrayOfByte.length - 1;
/*      */ 
/* 3688 */             localBufferedOutputStream.write(arrayOfByte, 0, i2);
/*      */ 
/* 3691 */             localBufferedOutputStream.flush();
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/* 3700 */         k = localByteArrayOutputStream.size();
/*      */ 
/* 3706 */         localByteArrayInputStream = new ByteArrayInputStream(localByteArrayOutputStream.toByteArray());
/*      */       }
/*      */ 
/*      */     }
/*      */     catch (IOException localIOException)
/*      */     {
/* 3713 */       throw new SQLException("CharsToBytes Reader Conversion: " + localIOException.getMessage());
/*      */     }
/*      */ 
/* 3717 */     if ((paramInt1 > 0) && (paramInt1 <= this.numberOfCols))
/*      */     {
/* 3720 */       if ((getColumnType(paramInt1) != -1) || (getColumnType(paramInt1) != 12))
/*      */       {
/* 3723 */         this.boundCols[(paramInt1 - 1)].setType(-1);
/*      */       }
/*      */ 
/* 3727 */       if (paramReader == null) {
/* 3728 */         updateNull(paramInt1);
/* 3729 */         return;
/*      */       }
/*      */ 
/* 3732 */       if (k != this.boundCols[(paramInt1 - 1)].getLength()) {
/* 3733 */         this.boundCols[(paramInt1 - 1)].setLength(k);
/*      */       }
/* 3735 */       this.boundCols[(paramInt1 - 1)].setRowValues(i, localByteArrayInputStream, k);
/* 3736 */       this.boundCols[(paramInt1 - 1)].setStreamType(3);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void updateObject(int paramInt1, Object paramObject, int paramInt2)
/*      */     throws SQLException
/*      */   {
/* 3755 */     updateObject(paramInt1, paramObject, paramInt2, this.boundCols[(paramInt1 - 1)].getType());
/*      */   }
/*      */ 
/*      */   public void updateObject(int paramInt, Object paramObject)
/*      */     throws SQLException
/*      */   {
/* 3763 */     updateObject(paramInt, paramObject, 0, this.boundCols[(paramInt - 1)].getType());
/*      */   }
/*      */ 
/*      */   protected void updateObject(int paramInt1, Object paramObject, int paramInt2, int paramInt3)
/*      */     throws SQLException
/*      */   {
/* 3774 */     checkOpen();
/*      */ 
/* 3776 */     if ((paramInt3 == 9999) && (paramObject != null))
/*      */     {
/* 3778 */       paramInt3 = JdbcOdbcStatement.getTypeFromObject(paramObject);
/*      */     }
/* 3780 */     else if (paramObject == null)
/*      */     {
/* 3782 */       paramInt3 = 0;
/*      */     }
/*      */ 
/* 3785 */     if ((paramInt1 > 0) && (paramInt1 <= this.numberOfCols))
/*      */     {
/* 3791 */       switch (paramInt3)
/*      */       {
/*      */       case 1:
/*      */       case 12:
/* 3795 */         updateString(paramInt1, (String)paramObject);
/* 3796 */         break;
/*      */       case -1:
/* 3801 */         if (((paramObject instanceof byte[])) && ((byte[])paramObject != null))
/*      */         {
/* 3803 */           byte[] arrayOfByte1 = (byte[])paramObject;
/*      */ 
/* 3805 */           updateAsciiStream(paramInt1, new ByteArrayInputStream(arrayOfByte1), arrayOfByte1.length);
/*      */         } else {
/* 3807 */           if (((paramObject instanceof Reader)) && ((Reader)paramObject != null))
/*      */           {
/* 3809 */             throw new SQLException("Unknown length for Reader Object, try updateCharacterStream.");
/*      */           }
/* 3811 */           if (((paramObject instanceof String)) && ((String)paramObject != null))
/*      */           {
/* 3814 */             updateString(paramInt1, (String)paramObject); }  } break;
/*      */       case 2:
/*      */       case 3:
/* 3820 */         updateBigDecimal(paramInt1, (BigDecimal)paramObject);
/* 3821 */         break;
/*      */       case -7:
/* 3824 */         updateBoolean(paramInt1, ((Boolean)paramObject).booleanValue());
/*      */ 
/* 3826 */         break;
/*      */       case -6:
/* 3829 */         updateByte(paramInt1, (byte)((Integer)paramObject).intValue());
/* 3830 */         break;
/*      */       case 5:
/* 3833 */         updateShort(paramInt1, (short)((Integer)paramObject).intValue());
/*      */ 
/* 3835 */         break;
/*      */       case 4:
/* 3838 */         updateInt(paramInt1, ((Integer)paramObject).intValue());
/*      */ 
/* 3840 */         break;
/*      */       case -5:
/* 3843 */         updateLong(paramInt1, ((Integer)paramObject).longValue());
/*      */ 
/* 3845 */         break;
/*      */       case 6:
/*      */       case 7:
/* 3849 */         updateFloat(paramInt1, ((Float)paramObject).floatValue());
/* 3850 */         break;
/*      */       case 8:
/* 3853 */         updateDouble(paramInt1, ((Double)paramObject).doubleValue());
/*      */ 
/* 3855 */         break;
/*      */       case -2:
/* 3859 */         if ((paramObject instanceof String))
/*      */           try {
/* 3861 */             updateBytes(paramInt1, ((String)paramObject).getBytes(this.OdbcApi.charSet));
/*      */           } catch (UnsupportedEncodingException localUnsupportedEncodingException1) {
/* 3863 */             throw new SQLException(localUnsupportedEncodingException1.getMessage());
/*      */           }
/*      */         else {
/* 3866 */           updateBytes(paramInt1, (byte[])paramObject);
/*      */         }
/* 3868 */         break;
/*      */       case -4:
/*      */       case -3:
/* 3874 */         byte[] arrayOfByte2 = null;
/* 3875 */         if ((paramObject instanceof String))
/*      */           try {
/* 3877 */             arrayOfByte2 = ((String)paramObject).getBytes(this.OdbcApi.charSet);
/*      */           } catch (UnsupportedEncodingException localUnsupportedEncodingException2) {
/* 3879 */             throw new SQLException(localUnsupportedEncodingException2.getMessage());
/*      */           }
/*      */         else {
/* 3882 */           arrayOfByte2 = (byte[])paramObject;
/*      */         }
/*      */ 
/* 3885 */         if (arrayOfByte2.length > 8000)
/*      */         {
/* 3887 */           updateBinaryStream(paramInt1, new ByteArrayInputStream(arrayOfByte2), arrayOfByte2.length);
/*      */         }
/*      */         else
/*      */         {
/* 3891 */           updateBytes(paramInt1, arrayOfByte2);
/*      */         }
/* 3893 */         break;
/*      */       case 91:
/* 3896 */         updateDate(paramInt1, (Date)paramObject);
/* 3897 */         break;
/*      */       case 92:
/* 3900 */         updateTime(paramInt1, (Time)paramObject);
/* 3901 */         break;
/*      */       case 93:
/* 3904 */         updateTimestamp(paramInt1, (Timestamp)paramObject);
/*      */ 
/* 3906 */         break;
/*      */       case 0:
/* 3909 */         updateNull(paramInt1);
/* 3910 */         break;
/*      */       default:
/* 3913 */         throw new SQLException("Unknown SQL Type for ResultSet.updateObject SQL Type = " + paramInt3);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void updateChar(int paramInt1, int paramInt2, String paramString)
/*      */     throws SQLException
/*      */   {
/* 3930 */     checkOpen();
/* 3931 */     int i = getRowIndex();
/*      */ 
/* 3933 */     if ((paramInt1 > 0) && (paramInt1 <= this.numberOfCols))
/*      */     {
/* 3936 */       int j = getColumnType(paramInt1);
/*      */ 
/* 3938 */       if (j != paramInt2) {
/* 3939 */         this.boundCols[(paramInt1 - 1)].setType(paramInt2);
/*      */       }
/*      */ 
/* 3942 */       if (paramString == null)
/* 3943 */         updateNull(paramInt1);
/*      */       else
/* 3945 */         this.boundCols[(paramInt1 - 1)].setRowValues(i, paramString, -3);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void updateNull(String paramString)
/*      */     throws SQLException
/*      */   {
/* 3955 */     updateNull(findColumn(paramString));
/*      */   }
/*      */ 
/*      */   public void updateBoolean(String paramString, boolean paramBoolean)
/*      */     throws SQLException
/*      */   {
/* 3963 */     updateBoolean(findColumn(paramString), paramBoolean);
/*      */   }
/*      */ 
/*      */   public void updateByte(String paramString, byte paramByte)
/*      */     throws SQLException
/*      */   {
/* 3971 */     updateInt(findColumn(paramString), paramByte);
/*      */   }
/*      */ 
/*      */   public void updateShort(String paramString, short paramShort)
/*      */     throws SQLException
/*      */   {
/* 3979 */     updateInt(findColumn(paramString), paramShort);
/*      */   }
/*      */ 
/*      */   public void updateInt(String paramString, int paramInt)
/*      */     throws SQLException
/*      */   {
/* 3987 */     updateInt(findColumn(paramString), paramInt);
/*      */   }
/*      */ 
/*      */   public void updateLong(String paramString, long paramLong)
/*      */     throws SQLException
/*      */   {
/* 3995 */     updateFloat(findColumn(paramString), (float)paramLong);
/*      */   }
/*      */ 
/*      */   public void updateFloat(String paramString, float paramFloat)
/*      */     throws SQLException
/*      */   {
/* 4003 */     updateFloat(findColumn(paramString), paramFloat);
/*      */   }
/*      */ 
/*      */   public void updateDouble(String paramString, double paramDouble)
/*      */     throws SQLException
/*      */   {
/* 4011 */     updateDouble(findColumn(paramString), paramDouble);
/*      */   }
/*      */ 
/*      */   public void updateBigDecimal(String paramString, BigDecimal paramBigDecimal)
/*      */     throws SQLException
/*      */   {
/* 4019 */     updateBigDecimal(findColumn(paramString), paramBigDecimal);
/*      */   }
/*      */ 
/*      */   public void updateString(String paramString1, String paramString2)
/*      */     throws SQLException
/*      */   {
/* 4027 */     updateString(findColumn(paramString1), paramString2);
/*      */   }
/*      */ 
/*      */   public void updateBytes(String paramString, byte[] paramArrayOfByte)
/*      */     throws SQLException
/*      */   {
/* 4035 */     updateBytes(findColumn(paramString), paramArrayOfByte);
/*      */   }
/*      */ 
/*      */   public void updateDate(String paramString, Date paramDate)
/*      */     throws SQLException
/*      */   {
/* 4043 */     updateDate(findColumn(paramString), paramDate);
/*      */   }
/*      */ 
/*      */   public void updateTime(String paramString, Time paramTime)
/*      */     throws SQLException
/*      */   {
/* 4051 */     updateTime(findColumn(paramString), paramTime);
/*      */   }
/*      */ 
/*      */   public void updateTimestamp(String paramString, Timestamp paramTimestamp)
/*      */     throws SQLException
/*      */   {
/* 4059 */     updateTimestamp(findColumn(paramString), paramTimestamp);
/*      */   }
/*      */ 
/*      */   public void updateAsciiStream(String paramString, InputStream paramInputStream, int paramInt)
/*      */     throws SQLException
/*      */   {
/* 4068 */     updateAsciiStream(findColumn(paramString), paramInputStream, paramInt);
/*      */   }
/*      */ 
/*      */   public void updateBinaryStream(String paramString, InputStream paramInputStream, int paramInt)
/*      */     throws SQLException
/*      */   {
/* 4077 */     updateBinaryStream(findColumn(paramString), paramInputStream, paramInt);
/*      */   }
/*      */ 
/*      */   public void updateCharacterStream(String paramString, Reader paramReader, int paramInt)
/*      */     throws SQLException
/*      */   {
/* 4086 */     updateCharacterStream(findColumn(paramString), paramReader, paramInt);
/*      */   }
/*      */ 
/*      */   public void updateObject(String paramString, Object paramObject, int paramInt)
/*      */     throws SQLException
/*      */   {
/* 4095 */     updateObject(findColumn(paramString), paramObject, paramInt);
/*      */   }
/*      */ 
/*      */   public void updateObject(String paramString, Object paramObject)
/*      */     throws SQLException
/*      */   {
/* 4103 */     updateObject(findColumn(paramString), paramObject);
/*      */   }
/*      */ 
/*      */   public void insertRow()
/*      */     throws SQLException
/*      */   {
/* 4115 */     checkOpen();
/* 4116 */     for (int i = 0; i < this.numberOfCols; i++)
/*      */     {
/* 4118 */       int j = this.boundCols[i].getType();
/* 4119 */       bindCol(i + 1, j);
/*      */     }
/*      */ 
/* 4122 */     if (getType() != 1003)
/*      */     {
/* 4124 */       if (this.blockCursor)
/*      */       {
/* 4126 */         setPos(this.currentBlockCell, 4);
/*      */       }
/*      */       else
/*      */       {
/* 4130 */         setPos(this.rowSet, 4);
/*      */       }
/* 4132 */       FreeCols();
/*      */ 
/* 4135 */       if (this.ownInsertsAreVisible) {
/* 4136 */         this.numberOfRows += 1;
/*      */       }
/* 4138 */       resetColumnState();
/* 4139 */       resetInsertRow();
/*      */     }
/*      */     else {
/* 4142 */       throw new SQLException("Result set type is TYPE_FORWARD_ONLY");
/*      */     }
/*      */   }
/*      */ 
/*      */   public void updateRow()
/*      */     throws SQLException
/*      */   {
/* 4154 */     checkOpen();
/*      */ 
/* 4156 */     for (int i = 0; i < this.numberOfCols; i++)
/*      */     {
/* 4158 */       int j = this.boundCols[i].getType();
/* 4159 */       bindCol(i + 1, j);
/*      */     }
/*      */ 
/* 4162 */     if (getType() != 1003)
/*      */     {
/* 4164 */       if (this.blockCursor)
/*      */       {
/* 4166 */         setPos(this.currentBlockCell, 2);
/*      */       }
/*      */       else
/*      */       {
/* 4170 */         setPos(this.rowSet, 2);
/*      */       }
/* 4172 */       FreeCols();
/*      */ 
/* 4174 */       resetColumnState();
/*      */ 
/* 4178 */       this.rowUpdated = true;
/*      */     }
/*      */     else {
/* 4181 */       throw new SQLException("Result set type is TYPE_FORWARD_ONLY");
/*      */     }
/*      */   }
/*      */ 
/*      */   public void deleteRow()
/*      */     throws SQLException
/*      */   {
/* 4193 */     checkOpen();
/* 4194 */     if (this.blockCursor)
/*      */     {
/* 4196 */       setPos(this.currentBlockCell, 3);
/*      */     }
/*      */     else {
/* 4199 */       setPos(this.rowSet, 3);
/*      */     }
/*      */ 
/* 4202 */     if (this.ownDeletesAreVisible)
/* 4203 */       this.numberOfRows -= 1;
/*      */   }
/*      */ 
/*      */   private void setResultSetVisibilityIndicators()
/*      */     throws SQLException
/*      */   {
/* 4214 */     int i = this.OdbcApi.SQLGetStmtAttr(this.hStmt, 6);
/* 4215 */     short s = 0;
/* 4216 */     switch (i)
/*      */     {
/*      */     case 1:
/* 4219 */       s = 151;
/* 4220 */       break;
/*      */     case 2:
/* 4222 */       s = 145;
/* 4223 */       break;
/*      */     case 3:
/* 4225 */       s = 168;
/*      */     }
/*      */ 
/* 4230 */     if (s > 0)
/*      */       try {
/* 4232 */         int j = this.OdbcApi.SQLGetInfo(this.hDbc, s);
/*      */ 
/* 4234 */         if ((j & 0x20) > 0) {
/* 4235 */           this.ownDeletesAreVisible = true;
/*      */         }
/* 4237 */         if ((j & 0x10) > 0)
/* 4238 */           this.ownInsertsAreVisible = true;
/*      */       }
/*      */       catch (SQLException localSQLException)
/*      */       {
/*      */       }
/*      */   }
/*      */ 
/*      */   public void refreshRow()
/*      */     throws SQLException
/*      */   {
/* 4256 */     checkOpen();
/* 4257 */     if (getType() != 1003)
/*      */     {
/* 4260 */       if ((!this.atInsertRow) && (getRow() > 0))
/*      */       {
/* 4262 */         fetchScrollOption(0, (short)6);
/*      */       }
/*      */       else
/*      */       {
/* 4266 */         throw new SQLException("Cursor position is invalid");
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/* 4271 */       throw new SQLException("Result set type is TYPE_FORWARD_ONLY");
/*      */     }
/*      */   }
/*      */ 
/*      */   public void cancelRowUpdates()
/*      */     throws SQLException
/*      */   {
/* 4284 */     checkOpen();
/* 4285 */     if (!this.atInsertRow)
/*      */     {
/* 4287 */       resetColumnState();
/*      */     }
/*      */     else
/*      */     {
/* 4291 */       throw new SQLException("Cursor position on insert row");
/*      */     }
/*      */   }
/*      */ 
/*      */   public void moveToInsertRow()
/*      */     throws SQLException
/*      */   {
/* 4305 */     if (getType() == 1003) {
/* 4306 */       throw new SQLException("Invalid Cursor Type: " + getType());
/*      */     }
/*      */ 
/* 4309 */     checkOpen();
/*      */ 
/* 4312 */     this.atInsertRow = true;
/*      */ 
/* 4314 */     this.lastRowPosition = this.rowPosition;
/* 4315 */     this.lastBlockPosition = this.currentBlockCell;
/*      */ 
/* 4317 */     if (this.blockCursor) {
/* 4318 */       this.currentBlockCell = (this.rowSet + 1);
/*      */     }
/* 4320 */     resetInsertRow();
/*      */   }
/*      */ 
/*      */   public void moveToCurrentRow()
/*      */     throws SQLException
/*      */   {
/* 4331 */     checkOpen();
/* 4332 */     boolean bool = false;
/*      */ 
/* 4334 */     if (this.atInsertRow)
/*      */     {
/* 4336 */       resetInsertRow();
/*      */ 
/* 4338 */       this.rowPosition = this.lastRowPosition;
/*      */ 
/* 4340 */       this.currentBlockCell = this.lastBlockPosition;
/*      */ 
/* 4342 */       bool = absolute(this.rowPosition);
/*      */ 
/* 4344 */       if (bool)
/*      */       {
/* 4346 */         this.lastRowPosition = 0;
/* 4347 */         this.lastBlockPosition = 0;
/*      */       }
/*      */ 
/* 4350 */       this.atInsertRow = false;
/*      */     }
/*      */   }
/*      */ 
/*      */   public Statement getStatement()
/*      */     throws SQLException
/*      */   {
/* 4362 */     checkOpen();
/* 4363 */     if (this.ownerStatement != null)
/*      */     {
/* 4365 */       return this.ownerStatement;
/*      */     }
/*      */ 
/* 4368 */     return null;
/*      */   }
/*      */ 
/*      */   public Object getObject(int paramInt, Map<String, Class<?>> paramMap)
/*      */     throws SQLException
/*      */   {
/* 4375 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public Ref getRef(int paramInt) throws SQLException
/*      */   {
/* 4380 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public Blob getBlob(int paramInt) throws SQLException
/*      */   {
/* 4385 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public Clob getClob(int paramInt) throws SQLException
/*      */   {
/* 4390 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public Array getArray(int paramInt) throws SQLException
/*      */   {
/* 4395 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public Object getObject(String paramString, Map<String, Class<?>> paramMap)
/*      */     throws SQLException
/*      */   {
/* 4401 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public Ref getRef(String paramString) throws SQLException
/*      */   {
/* 4406 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public Blob getBlob(String paramString)
/*      */     throws SQLException
/*      */   {
/* 4412 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public Clob getClob(String paramString)
/*      */     throws SQLException
/*      */   {
/* 4418 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public Array getArray(String paramString) throws SQLException
/*      */   {
/* 4423 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public URL getURL(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 4430 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public URL getURL(String paramString) throws SQLException {
/* 4434 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void updateRef(int paramInt, Ref paramRef) throws SQLException {
/* 4438 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void updateRef(String paramString, Ref paramRef) throws SQLException {
/* 4442 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void updateBlob(int paramInt, Blob paramBlob) throws SQLException {
/* 4446 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void updateBlob(String paramString, Blob paramBlob) throws SQLException {
/* 4450 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void updateClob(int paramInt, Clob paramClob) throws SQLException {
/* 4454 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void updateClob(String paramString, Clob paramClob) throws SQLException {
/* 4458 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void updateArray(int paramInt, Array paramArray) throws SQLException {
/* 4462 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void updateArray(String paramString, Array paramArray) throws SQLException {
/* 4466 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public Date getDate(int paramInt, Calendar paramCalendar)
/*      */     throws SQLException
/*      */   {
/* 4478 */     checkOpen();
/*      */ 
/* 4481 */     clearWarnings();
/* 4482 */     this.lastColumnNull = false;
/* 4483 */     long l = 0L;
/*      */ 
/* 4487 */     paramInt = mapColumn(paramInt);
/*      */ 
/* 4491 */     if (getPseudoCol(paramInt) != null) {
/* 4492 */       this.lastColumnNull = true;
/* 4493 */       return null;
/*      */     }
/*      */ 
/* 4496 */     l = getDataLongDate(paramInt, paramCalendar);
/*      */ 
/* 4500 */     if (l == 0L)
/*      */     {
/* 4502 */       return null;
/*      */     }
/*      */ 
/* 4508 */     return new Date(l);
/*      */   }
/*      */ 
/*      */   public Date getDate(String paramString, Calendar paramCalendar)
/*      */     throws SQLException
/*      */   {
/* 4517 */     return getDate(findColumn(paramString), paramCalendar);
/*      */   }
/*      */ 
/*      */   public Time getTime(int paramInt, Calendar paramCalendar)
/*      */     throws SQLException
/*      */   {
/* 4530 */     checkOpen();
/*      */ 
/* 4533 */     clearWarnings();
/* 4534 */     this.lastColumnNull = false;
/* 4535 */     long l = 0L;
/*      */ 
/* 4539 */     paramInt = mapColumn(paramInt);
/*      */ 
/* 4543 */     if (getPseudoCol(paramInt) != null) {
/* 4544 */       this.lastColumnNull = true;
/* 4545 */       return null;
/*      */     }
/*      */ 
/* 4548 */     l = getDataLongTime(paramInt, paramCalendar);
/*      */ 
/* 4552 */     if (l == 0L)
/*      */     {
/* 4554 */       return null;
/*      */     }
/*      */ 
/* 4559 */     return new Time(l);
/*      */   }
/*      */ 
/*      */   public Time getTime(String paramString, Calendar paramCalendar)
/*      */     throws SQLException
/*      */   {
/* 4567 */     return getTime(findColumn(paramString), paramCalendar);
/*      */   }
/*      */ 
/*      */   public Timestamp getTimestamp(int paramInt, Calendar paramCalendar)
/*      */     throws SQLException
/*      */   {
/* 4580 */     checkOpen();
/*      */ 
/* 4583 */     clearWarnings();
/* 4584 */     this.lastColumnNull = false;
/*      */ 
/* 4586 */     long l = 0L;
/*      */ 
/* 4590 */     paramInt = mapColumn(paramInt);
/*      */ 
/* 4594 */     if (getPseudoCol(paramInt) != null) {
/* 4595 */       this.lastColumnNull = true;
/* 4596 */       return null;
/*      */     }
/*      */ 
/* 4599 */     l = getDataLongTimestamp(paramInt, paramCalendar);
/*      */ 
/* 4603 */     if (l == 0L)
/*      */     {
/* 4605 */       return null;
/*      */     }
/*      */ 
/* 4610 */     return new Timestamp(l);
/*      */   }
/*      */ 
/*      */   public Timestamp getTimestamp(String paramString, Calendar paramCalendar)
/*      */     throws SQLException
/*      */   {
/* 4618 */     return getTimestamp(findColumn(paramString), paramCalendar);
/*      */   }
/*      */ 
/*      */   protected void setRowStatusPtr()
/*      */     throws SQLException
/*      */   {
/* 4634 */     checkOpen();
/* 4635 */     clearWarnings();
/*      */ 
/* 4637 */     this.rowStatusArray = new int[this.rowSet + 1];
/*      */ 
/* 4639 */     this.pA = new long[2];
/* 4640 */     this.pA[0] = 0L;
/* 4641 */     this.pA[1] = 0L;
/*      */ 
/* 4643 */     this.OdbcApi.SQLSetStmtAttrPtr(this.hStmt, 25, this.rowStatusArray, 0, this.pA);
/*      */   }
/*      */ 
/*      */   protected boolean setRowArraySize()
/*      */   {
/* 4655 */     int i = 0;
/*      */     try
/*      */     {
/* 4660 */       clearWarnings();
/*      */ 
/* 4662 */       if (this.rowSet > 1)
/*      */       {
/* 4667 */         if (this.numberOfRows < this.rowSet) {
/* 4668 */           this.rowSet = this.numberOfRows;
/*      */         }
/* 4670 */         this.OdbcApi.SQLSetStmtAttr(this.hStmt, 5, 0, 0);
/*      */ 
/* 4674 */         this.OdbcApi.SQLSetStmtAttr(this.hStmt, 27, this.rowSet, 0);
/*      */ 
/* 4677 */         i = this.OdbcApi.SQLGetStmtAttr(this.hStmt, 27);
/*      */ 
/* 4680 */         if ((i > 1) && (i < this.rowSet))
/*      */         {
/* 4682 */           this.rowSet = i;
/* 4683 */           return true;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */     catch (SQLException localSQLException)
/*      */     {
/* 4691 */       return false;
/*      */     }
/*      */ 
/* 4695 */     if (i == this.rowSet)
/*      */     {
/* 4697 */       return true;
/*      */     }
/*      */ 
/* 4700 */     return false;
/*      */   }
/*      */ 
/*      */   protected void resetInsertRow()
/*      */     throws SQLException
/*      */   {
/* 4710 */     checkOpen();
/* 4711 */     int i = getRowIndex();
/*      */ 
/* 4713 */     if (this.atInsertRow)
/*      */     {
/* 4716 */       for (int j = 0; j < this.numberOfCols; j++)
/* 4717 */         this.boundCols[j].resetColumnToIgnoreData();
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void resetColumnState()
/*      */     throws SQLException
/*      */   {
/* 4732 */     checkOpen();
/*      */ 
/* 4736 */     if (this.hStmt != 0L)
/*      */     {
/* 4738 */       this.OdbcApi.SQLFreeStmt(this.hStmt, 2);
/*      */     }
/*      */ 
/* 4741 */     for (int i = 0; i < this.numberOfCols; i++)
/*      */     {
/* 4743 */       this.boundCols[i].resetColumnToIgnoreData();
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void bindCol(int paramInt1, int paramInt2)
/*      */     throws SQLException
/*      */   {
/* 4757 */     int i = 0;
/* 4758 */     int j = 0;
/* 4759 */     Object localObject1 = null;
/*      */ 
/* 4762 */     Object[] arrayOfObject = this.boundCols[(paramInt1 - 1)].getRowValues();
/*      */ 
/* 4764 */     byte[] arrayOfByte = this.boundCols[(paramInt1 - 1)].getRowLengths();
/*      */ 
/* 4766 */     if ((!this.blockCursor) && (this.atInsertRow))
/*      */     {
/* 4768 */       j = 0;
/*      */     }
/*      */ 
/* 4785 */     if (this.blockCursor)
/*      */     {
/* 4787 */       j = this.currentBlockCell - 1;
/*      */     }
/*      */ 
/* 4791 */     Object localObject2 = this.boundCols[(paramInt1 - 1)].getRowValue(j);
/* 4792 */     int k = this.boundCols[(paramInt1 - 1)].getLength();
/*      */ 
/* 4794 */     if (k < 0) {
/* 4795 */       k = getColumnLength(paramInt1);
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/* 4802 */       switch (paramInt2)
/*      */       {
/*      */       case 1:
/*      */       case 2:
/*      */       case 3:
/*      */       case 12:
/* 4809 */         if ((paramInt2 == 2) || (paramInt2 == 3))
/*      */         {
/* 4811 */           if (localObject2 != null)
/*      */           {
/* 4813 */             Object localObject3 = localObject2;
/* 4814 */             String str = localObject3.toString();
/* 4815 */             k = str.length();
/*      */ 
/* 4817 */             BigDecimal localBigDecimal = new BigDecimal(str);
/* 4818 */             int n = localBigDecimal.scale();
/*      */ 
/* 4821 */             if (n <= 0) {
/* 4822 */               k++;
/*      */             }
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/* 4828 */         bindStringCol(paramInt1, paramInt2, arrayOfObject, arrayOfByte, k);
/*      */ 
/* 4831 */         break;
/*      */       case -1:
/* 4836 */         int m = JdbcOdbcStatement.getTypeFromObject(localObject2);
/*      */ 
/* 4838 */         if ((m == -4) || (m == 0))
/*      */         {
/* 4840 */           bindAtExecCol(paramInt1, paramInt2, arrayOfByte);
/*      */         }
/* 4842 */         else if (m == -2)
/*      */         {
/* 4844 */           bindBinaryCol(paramInt1, arrayOfObject, arrayOfByte, k); } break;
/*      */       case -7:
/*      */       case -6:
/*      */       case 4:
/*      */       case 5:
/* 4854 */         bindIntegerCol(paramInt1, arrayOfObject, arrayOfByte);
/* 4855 */         break;
/*      */       case 8:
/* 4859 */         bindDoubleCol(paramInt1, arrayOfObject, arrayOfByte);
/* 4860 */         break;
/*      */       case -5:
/*      */       case 6:
/*      */       case 7:
/* 4866 */         bindFloatCol(paramInt1, arrayOfObject, arrayOfByte);
/* 4867 */         break;
/*      */       case 91:
/* 4871 */         bindDateCol(paramInt1, arrayOfObject, arrayOfByte);
/* 4872 */         break;
/*      */       case 92:
/* 4876 */         bindTimeCol(paramInt1, arrayOfObject, arrayOfByte);
/* 4877 */         break;
/*      */       case 93:
/* 4881 */         bindTimestampCol(paramInt1, arrayOfObject, arrayOfByte);
/* 4882 */         break;
/*      */       case -3:
/*      */       case -2:
/* 4888 */         bindBinaryCol(paramInt1, arrayOfObject, arrayOfByte, k);
/* 4889 */         break;
/*      */       case -4:
/* 4893 */         bindAtExecCol(paramInt1, paramInt2, arrayOfByte);
/*      */       }
/*      */ 
/*      */     }
/*      */     catch (SQLException localSQLException)
/*      */     {
/* 4901 */       throw new SQLException("SQLBinCol (" + paramInt1 + ") SQLType = " + paramInt2 + ". " + localSQLException.getMessage());
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void bindStringCol(int paramInt1, int paramInt2, Object[] paramArrayOfObject, byte[] paramArrayOfByte, int paramInt3)
/*      */     throws SQLException
/*      */   {
/* 4920 */     byte[] arrayOfByte = this.boundCols[(paramInt1 - 1)].allocBindDataBuffer((paramInt3 + 1) * paramArrayOfObject.length);
/*      */ 
/* 4922 */     long[] arrayOfLong = new long[4];
/* 4923 */     arrayOfLong[0] = 0L;
/* 4924 */     arrayOfLong[1] = 0L;
/* 4925 */     arrayOfLong[2] = 0L;
/* 4926 */     arrayOfLong[3] = 0L;
/*      */ 
/* 4928 */     this.OdbcApi.SQLBindColString(this.hStmt, paramInt1, paramInt2, paramArrayOfObject, paramInt3, paramArrayOfByte, arrayOfByte, arrayOfLong);
/*      */ 
/* 4933 */     this.boundCols[(paramInt1 - 1)].pA1 = arrayOfLong[0];
/* 4934 */     this.boundCols[(paramInt1 - 1)].pA2 = arrayOfLong[1];
/* 4935 */     this.boundCols[(paramInt1 - 1)].pC1 = arrayOfLong[2];
/* 4936 */     this.boundCols[(paramInt1 - 1)].pC2 = arrayOfLong[3];
/*      */   }
/*      */ 
/*      */   protected void bindIntegerCol(int paramInt, Object[] paramArrayOfObject, byte[] paramArrayOfByte)
/*      */     throws SQLException
/*      */   {
/* 4952 */     byte[] arrayOfByte = this.boundCols[(paramInt - 1)].allocBindDataBuffer(4 * paramArrayOfObject.length);
/*      */ 
/* 4954 */     long[] arrayOfLong = new long[4];
/* 4955 */     arrayOfLong[0] = 0L;
/* 4956 */     arrayOfLong[1] = 0L;
/* 4957 */     arrayOfLong[2] = 0L;
/* 4958 */     arrayOfLong[3] = 0L;
/*      */ 
/* 4960 */     this.OdbcApi.SQLBindColInteger(this.hStmt, paramInt, paramArrayOfObject, paramArrayOfByte, arrayOfByte, arrayOfLong);
/*      */ 
/* 4965 */     this.boundCols[(paramInt - 1)].pA1 = arrayOfLong[0];
/* 4966 */     this.boundCols[(paramInt - 1)].pA2 = arrayOfLong[1];
/* 4967 */     this.boundCols[(paramInt - 1)].pC1 = arrayOfLong[2];
/* 4968 */     this.boundCols[(paramInt - 1)].pC2 = arrayOfLong[3];
/*      */   }
/*      */ 
/*      */   protected void bindFloatCol(int paramInt, Object[] paramArrayOfObject, byte[] paramArrayOfByte)
/*      */     throws SQLException
/*      */   {
/* 4985 */     byte[] arrayOfByte = this.boundCols[(paramInt - 1)].allocBindDataBuffer(8 * paramArrayOfObject.length);
/*      */ 
/* 4987 */     long[] arrayOfLong = new long[4];
/* 4988 */     arrayOfLong[0] = 0L;
/* 4989 */     arrayOfLong[1] = 0L;
/* 4990 */     arrayOfLong[2] = 0L;
/* 4991 */     arrayOfLong[3] = 0L;
/*      */ 
/* 4993 */     this.OdbcApi.SQLBindColFloat(this.hStmt, paramInt, paramArrayOfObject, paramArrayOfByte, arrayOfByte, arrayOfLong);
/*      */ 
/* 4998 */     this.boundCols[(paramInt - 1)].pA1 = arrayOfLong[0];
/* 4999 */     this.boundCols[(paramInt - 1)].pA2 = arrayOfLong[1];
/* 5000 */     this.boundCols[(paramInt - 1)].pC1 = arrayOfLong[2];
/* 5001 */     this.boundCols[(paramInt - 1)].pC2 = arrayOfLong[3];
/*      */   }
/*      */ 
/*      */   protected void bindDoubleCol(int paramInt, Object[] paramArrayOfObject, byte[] paramArrayOfByte)
/*      */     throws SQLException
/*      */   {
/* 5017 */     byte[] arrayOfByte = this.boundCols[(paramInt - 1)].allocBindDataBuffer(8 * paramArrayOfObject.length);
/*      */ 
/* 5019 */     long[] arrayOfLong = new long[4];
/* 5020 */     arrayOfLong[0] = 0L;
/* 5021 */     arrayOfLong[1] = 0L;
/* 5022 */     arrayOfLong[2] = 0L;
/* 5023 */     arrayOfLong[3] = 0L;
/*      */ 
/* 5025 */     this.OdbcApi.SQLBindColDouble(this.hStmt, paramInt, paramArrayOfObject, paramArrayOfByte, arrayOfByte, arrayOfLong);
/*      */ 
/* 5030 */     this.boundCols[(paramInt - 1)].pA1 = arrayOfLong[0];
/* 5031 */     this.boundCols[(paramInt - 1)].pA2 = arrayOfLong[1];
/* 5032 */     this.boundCols[(paramInt - 1)].pC1 = arrayOfLong[2];
/* 5033 */     this.boundCols[(paramInt - 1)].pC2 = arrayOfLong[3];
/*      */   }
/*      */ 
/*      */   protected void bindDateCol(int paramInt, Object[] paramArrayOfObject, byte[] paramArrayOfByte)
/*      */     throws SQLException
/*      */   {
/* 5050 */     byte[] arrayOfByte = this.boundCols[(paramInt - 1)].allocBindDataBuffer(10 * paramArrayOfObject.length);
/*      */ 
/* 5052 */     long[] arrayOfLong = new long[4];
/* 5053 */     arrayOfLong[0] = 0L;
/* 5054 */     arrayOfLong[1] = 0L;
/* 5055 */     arrayOfLong[2] = 0L;
/* 5056 */     arrayOfLong[3] = 0L;
/*      */ 
/* 5058 */     this.OdbcApi.SQLBindColDate(this.hStmt, paramInt, paramArrayOfObject, paramArrayOfByte, arrayOfByte, arrayOfLong);
/*      */ 
/* 5061 */     this.boundCols[(paramInt - 1)].pA1 = arrayOfLong[0];
/* 5062 */     this.boundCols[(paramInt - 1)].pA2 = arrayOfLong[1];
/* 5063 */     this.boundCols[(paramInt - 1)].pC1 = arrayOfLong[2];
/* 5064 */     this.boundCols[(paramInt - 1)].pC2 = arrayOfLong[3];
/*      */   }
/*      */ 
/*      */   protected void bindTimeCol(int paramInt, Object[] paramArrayOfObject, byte[] paramArrayOfByte)
/*      */     throws SQLException
/*      */   {
/* 5081 */     byte[] arrayOfByte = this.boundCols[(paramInt - 1)].allocBindDataBuffer(9 * paramArrayOfObject.length);
/*      */ 
/* 5083 */     long[] arrayOfLong = new long[4];
/* 5084 */     arrayOfLong[0] = 0L;
/* 5085 */     arrayOfLong[1] = 0L;
/* 5086 */     arrayOfLong[2] = 0L;
/* 5087 */     arrayOfLong[3] = 0L;
/*      */ 
/* 5089 */     this.OdbcApi.SQLBindColTime(this.hStmt, paramInt, paramArrayOfObject, paramArrayOfByte, arrayOfByte, arrayOfLong);
/*      */ 
/* 5092 */     this.boundCols[(paramInt - 1)].pA1 = arrayOfLong[0];
/* 5093 */     this.boundCols[(paramInt - 1)].pA2 = arrayOfLong[1];
/* 5094 */     this.boundCols[(paramInt - 1)].pC1 = arrayOfLong[2];
/* 5095 */     this.boundCols[(paramInt - 1)].pC2 = arrayOfLong[3];
/*      */   }
/*      */ 
/*      */   protected void bindTimestampCol(int paramInt, Object[] paramArrayOfObject, byte[] paramArrayOfByte)
/*      */     throws SQLException
/*      */   {
/* 5111 */     byte[] arrayOfByte = this.boundCols[(paramInt - 1)].allocBindDataBuffer(30 * paramArrayOfObject.length);
/*      */ 
/* 5113 */     long[] arrayOfLong = new long[4];
/* 5114 */     arrayOfLong[0] = 0L;
/* 5115 */     arrayOfLong[1] = 0L;
/* 5116 */     arrayOfLong[2] = 0L;
/* 5117 */     arrayOfLong[3] = 0L;
/*      */ 
/* 5119 */     this.OdbcApi.SQLBindColTimestamp(this.hStmt, paramInt, paramArrayOfObject, paramArrayOfByte, arrayOfByte, arrayOfLong);
/*      */ 
/* 5122 */     this.boundCols[(paramInt - 1)].pA1 = arrayOfLong[0];
/* 5123 */     this.boundCols[(paramInt - 1)].pA2 = arrayOfLong[1];
/* 5124 */     this.boundCols[(paramInt - 1)].pC1 = arrayOfLong[2];
/* 5125 */     this.boundCols[(paramInt - 1)].pC2 = arrayOfLong[3];
/*      */   }
/*      */ 
/*      */   protected void bindBinaryCol(int paramInt1, Object[] paramArrayOfObject, byte[] paramArrayOfByte, int paramInt2)
/*      */     throws SQLException
/*      */   {
/* 5142 */     byte[] arrayOfByte = this.boundCols[(paramInt1 - 1)].allocBindDataBuffer((paramInt2 + 1) * paramArrayOfObject.length);
/*      */ 
/* 5144 */     long[] arrayOfLong = new long[4];
/*      */ 
/* 5146 */     arrayOfLong[0] = 0L;
/* 5147 */     arrayOfLong[1] = 0L;
/* 5148 */     arrayOfLong[2] = 0L;
/* 5149 */     arrayOfLong[3] = 0L;
/*      */ 
/* 5152 */     this.OdbcApi.SQLBindColBinary(this.hStmt, paramInt1, paramArrayOfObject, paramArrayOfByte, paramInt2, arrayOfByte, arrayOfLong);
/*      */ 
/* 5157 */     this.boundCols[(paramInt1 - 1)].pA1 = arrayOfLong[0];
/* 5158 */     this.boundCols[(paramInt1 - 1)].pA2 = arrayOfLong[1];
/*      */ 
/* 5160 */     this.boundCols[(paramInt1 - 1)].pC1 = arrayOfLong[2];
/* 5161 */     this.boundCols[(paramInt1 - 1)].pC2 = arrayOfLong[3];
/*      */   }
/*      */ 
/*      */   protected void bindAtExecCol(int paramInt1, int paramInt2, byte[] paramArrayOfByte)
/*      */     throws SQLException
/*      */   {
/* 5181 */     byte[] arrayOfByte = this.boundCols[(paramInt1 - 1)].allocBindDataBuffer(4);
/*      */ 
/* 5185 */     long[] arrayOfLong = new long[4];
/*      */ 
/* 5187 */     arrayOfLong[0] = 0L;
/* 5188 */     arrayOfLong[1] = 0L;
/* 5189 */     arrayOfLong[2] = 0L;
/* 5190 */     arrayOfLong[3] = 0L;
/*      */ 
/* 5192 */     this.OdbcApi.SQLBindColAtExec(this.hStmt, paramInt1, paramInt2, paramArrayOfByte, arrayOfByte, arrayOfLong);
/*      */ 
/* 5196 */     this.boundCols[(paramInt1 - 1)].pA1 = arrayOfLong[0];
/* 5197 */     this.boundCols[(paramInt1 - 1)].pA2 = arrayOfLong[1];
/* 5198 */     this.boundCols[(paramInt1 - 1)].pC1 = arrayOfLong[2];
/* 5199 */     this.boundCols[(paramInt1 - 1)].pC2 = arrayOfLong[3];
/*      */   }
/*      */ 
/*      */   protected void setPos(int paramInt1, int paramInt2)
/*      */     throws SQLException
/*      */   {
/* 5213 */     Object localObject = null;
/* 5214 */     boolean bool = false;
/*      */     try
/*      */     {
/* 5219 */       clearWarnings();
/*      */ 
/* 5221 */       bool = this.OdbcApi.SQLSetPos(this.hStmt, paramInt1, paramInt2, 0);
/*      */ 
/* 5229 */       int i = 0;
/*      */ 
/* 5231 */       while (bool)
/*      */       {
/* 5233 */         int j = getRowIndex();
/*      */ 
/* 5235 */         String str = this.OdbcApi.odbcDriverName;
/*      */ 
/* 5237 */         if ((this.blockCursor) && (str.indexOf("(IV") == -1))
/*      */         {
/* 5239 */           i = this.OdbcApi.SQLParamDataInBlock(this.hStmt, j);
/*      */         }
/*      */         else {
/* 5242 */           i = this.OdbcApi.SQLParamData(this.hStmt);
/*      */         }
/*      */ 
/* 5247 */         if (i == -1) {
/* 5248 */           bool = false;
/*      */         }
/*      */         else
/*      */         {
/* 5256 */           putColumnData(i);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */     catch (SQLWarning localSQLWarning)
/*      */     {
/* 5265 */       localObject = localSQLWarning;
/*      */     }
/*      */     catch (SQLException localSQLException)
/*      */     {
/* 5270 */       throw new SQLException(localSQLException.getMessage());
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void putColumnData(int paramInt)
/*      */     throws SQLException, JdbcOdbcSQLWarning
/*      */   {
/* 5287 */     int i = 2000;
/*      */ 
/* 5290 */     byte[] arrayOfByte = new byte[i];
/* 5291 */     int m = 0;
/*      */ 
/* 5294 */     if ((paramInt < 1) || (paramInt > this.numberOfCols))
/*      */     {
/* 5297 */       if (this.OdbcApi.getTracer().isTracing()) {
/* 5298 */         this.OdbcApi.getTracer().trace("Invalid index for putColumnData()");
/*      */       }
/* 5300 */       return;
/*      */     }
/*      */ 
/* 5303 */     InputStream localInputStream = null;
/*      */ 
/* 5306 */     int n = getRowIndex();
/*      */     try
/*      */     {
/* 5311 */       localInputStream = (InputStream)this.boundCols[(paramInt - 1)].getRowValue(n);
/*      */     }
/*      */     catch (Exception localException)
/*      */     {
/* 5315 */       throw new SQLException("Invalid data for columnIndex(" + paramInt + "): " + localException.getMessage());
/*      */     }
/*      */ 
/* 5322 */     int i1 = this.boundCols[(paramInt - 1)].getLength();
/*      */ 
/* 5324 */     int i2 = this.boundCols[(paramInt - 1)].getStreamType();
/*      */ 
/* 5329 */     while (m == 0)
/*      */     {
/*      */       int j;
/*      */       try
/*      */       {
/* 5334 */         if (this.OdbcApi.getTracer().isTracing()) {
/* 5335 */           this.OdbcApi.getTracer().trace("Reading from input stream");
/*      */         }
/* 5337 */         j = localInputStream.read(arrayOfByte);
/* 5338 */         if (this.OdbcApi.getTracer().isTracing()) {
/* 5339 */           this.OdbcApi.getTracer().trace("Bytes read: " + j);
/*      */         }
/*      */ 
/*      */       }
/*      */       catch (IOException localIOException)
/*      */       {
/* 5347 */         throw new SQLException(localIOException.getMessage());
/*      */       }
/*      */ 
/* 5353 */       if (j == -1)
/*      */       {
/* 5358 */         if (i1 != 0) {
/* 5359 */           throw new SQLException("End of InputStream reached before satisfying length specified when InputStream was set");
/*      */         }
/* 5361 */         m = 1;
/* 5362 */         break;
/*      */       }
/*      */ 
/* 5369 */       if (j > i1) {
/* 5370 */         j = i1;
/* 5371 */         m = 1;
/*      */       }
/*      */ 
/* 5374 */       int k = j;
/*      */       try
/*      */       {
/* 5398 */         this.OdbcApi.SQLPutData(this.hStmt, arrayOfByte, k);
/*      */       }
/*      */       catch (SQLWarning localSQLWarning)
/*      */       {
/* 5402 */         setWarning(localSQLWarning);
/*      */       }
/*      */       catch (SQLException localSQLException)
/*      */       {
/*      */       }
/*      */ 
/* 5411 */       i1 -= j;
/*      */ 
/* 5413 */       if (this.OdbcApi.getTracer().isTracing()) {
/* 5414 */         this.OdbcApi.getTracer().trace("" + i1 + " bytes remaining");
/*      */       }
/*      */ 
/* 5419 */       if (i1 == 0)
/* 5420 */         m = 1;
/*      */     }
/*      */   }
/*      */ 
/*      */   public int getColAttribute(int paramInt1, int paramInt2)
/*      */     throws SQLException
/*      */   {
/* 5436 */     int i = 0;
/*      */ 
/* 5440 */     clearWarnings();
/*      */     try
/*      */     {
/* 5443 */       i = this.OdbcApi.SQLColAttributes(this.hStmt, paramInt1, paramInt2);
/*      */     }
/*      */     catch (JdbcOdbcSQLWarning localJdbcOdbcSQLWarning)
/*      */     {
/* 5452 */       BigDecimal localBigDecimal = (BigDecimal)localJdbcOdbcSQLWarning.value;
/* 5453 */       i = localBigDecimal.intValue();
/*      */ 
/* 5455 */       setWarning(JdbcOdbc.convertWarning(localJdbcOdbcSQLWarning));
/*      */     }
/* 5457 */     return i;
/*      */   }
/*      */ 
/*      */   protected int getMaxCharLen(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 5471 */     int i = getColumnType(paramInt);
/* 5472 */     int j = getColumnLength(paramInt);
/*      */ 
/* 5474 */     if (j != -1)
/*      */     {
/* 5478 */       switch (i)
/*      */       {
/*      */       case -4:
/*      */       case -3:
/*      */       case -2:
/* 5487 */         j *= 2;
/* 5488 */         break;
/*      */       case 91:
/* 5490 */         j = 10;
/* 5491 */         break;
/*      */       case 92:
/* 5493 */         j = 8;
/* 5494 */         break;
/*      */       case 93:
/* 5496 */         j = 29;
/* 5497 */         break;
/*      */       case 2:
/*      */       case 3:
/* 5500 */         j += 2;
/* 5501 */         break;
/*      */       case -7:
/* 5503 */         j = 1;
/* 5504 */         break;
/*      */       case -6:
/* 5506 */         j = 4;
/* 5507 */         break;
/*      */       case 5:
/* 5509 */         j = 6;
/* 5510 */         break;
/*      */       case 4:
/* 5512 */         j = 11;
/* 5513 */         break;
/*      */       case -5:
/* 5515 */         j = 20;
/* 5516 */         break;
/*      */       case 7:
/* 5518 */         j = 13;
/* 5519 */         break;
/*      */       case 6:
/*      */       case 8:
/* 5522 */         j = 22;
/*      */       }
/*      */ 
/* 5529 */       if ((j <= 0) || (j > 32767))
/*      */       {
/* 5531 */         j = 32767;
/*      */       }
/*      */     }
/* 5534 */     return j;
/*      */   }
/*      */ 
/*      */   protected int getMaxBinaryLen(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 5547 */     int i = getColumnLength(paramInt);
/*      */ 
/* 5549 */     if (i != -1)
/*      */     {
/* 5553 */       if ((i <= 0) || (i > 32767))
/*      */       {
/* 5555 */         i = 32767;
/*      */       }
/*      */     }
/* 5558 */     return i;
/*      */   }
/*      */ 
/*      */   public Double getDataDouble(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 5572 */     this.lastColumnNull = false;
/*      */     Double localDouble;
/*      */     try
/*      */     {
/* 5574 */       localDouble = this.OdbcApi.SQLGetDataDouble(this.hStmt, paramInt);
/*      */     }
/*      */     catch (JdbcOdbcSQLWarning localJdbcOdbcSQLWarning)
/*      */     {
/* 5582 */       localDouble = (Double)localJdbcOdbcSQLWarning.value;
/*      */ 
/* 5584 */       setWarning(JdbcOdbc.convertWarning(localJdbcOdbcSQLWarning));
/*      */     }
/* 5586 */     if (localDouble == null) {
/* 5587 */       this.lastColumnNull = true;
/*      */     }
/* 5589 */     return localDouble;
/*      */   }
/*      */ 
/*      */   public Float getDataFloat(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 5603 */     this.lastColumnNull = false;
/*      */     Float localFloat;
/*      */     try
/*      */     {
/* 5605 */       localFloat = this.OdbcApi.SQLGetDataFloat(this.hStmt, paramInt);
/*      */     }
/*      */     catch (JdbcOdbcSQLWarning localJdbcOdbcSQLWarning)
/*      */     {
/* 5613 */       localFloat = (Float)localJdbcOdbcSQLWarning.value;
/*      */ 
/* 5615 */       setWarning(JdbcOdbc.convertWarning(localJdbcOdbcSQLWarning));
/*      */     }
/* 5617 */     if (localFloat == null) {
/* 5618 */       this.lastColumnNull = true;
/*      */     }
/* 5620 */     return localFloat;
/*      */   }
/*      */ 
/*      */   public Integer getDataInteger(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 5635 */     this.lastColumnNull = false;
/*      */     Integer localInteger;
/*      */     try
/*      */     {
/* 5637 */       localInteger = this.OdbcApi.SQLGetDataInteger(this.hStmt, paramInt);
/*      */     }
/*      */     catch (JdbcOdbcSQLWarning localJdbcOdbcSQLWarning)
/*      */     {
/* 5645 */       localInteger = (Integer)localJdbcOdbcSQLWarning.value;
/*      */ 
/* 5647 */       setWarning(JdbcOdbc.convertWarning(localJdbcOdbcSQLWarning));
/*      */     }
/* 5649 */     if (localInteger == null) {
/* 5650 */       this.lastColumnNull = true;
/*      */     }
/* 5652 */     else if (paramInt == this.sqlTypeColumn)
/*      */     {
/* 5657 */       localInteger = new Integer(OdbcDef.odbcTypeToJdbc(localInteger.intValue()));
/*      */     }
/* 5659 */     return localInteger;
/*      */   }
/*      */ 
/*      */   public Long getDataLong(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 5672 */     Long localLong = null;
/* 5673 */     Double localDouble = getDataDouble(paramInt);
/*      */ 
/* 5675 */     if (localDouble != null) {
/* 5676 */       localLong = new Long(localDouble.longValue());
/*      */     }
/* 5678 */     return localLong;
/*      */   }
/*      */ 
/*      */   public String getDataString(int paramInt1, int paramInt2, boolean paramBoolean)
/*      */     throws SQLException
/*      */   {
/* 5695 */     this.lastColumnNull = false;
/*      */     String str;
/*      */     try
/*      */     {
/* 5697 */       str = this.OdbcApi.SQLGetDataString(this.hStmt, paramInt1, paramInt2, paramBoolean);
/*      */     }
/*      */     catch (JdbcOdbcSQLWarning localJdbcOdbcSQLWarning)
/*      */     {
/* 5706 */       str = (String)localJdbcOdbcSQLWarning.value;
/* 5707 */       setWarning(JdbcOdbc.convertWarning(localJdbcOdbcSQLWarning));
/*      */     }
/* 5709 */     if (str == null) {
/* 5710 */       this.lastColumnNull = true;
/*      */     }
/* 5712 */     else if (paramInt1 == this.sqlTypeColumn)
/*      */     {
/*      */       try
/*      */       {
/* 5720 */         int i = OdbcDef.odbcTypeToJdbc(Integer.valueOf(str).intValue());
/*      */ 
/* 5722 */         str = "" + i;
/*      */       }
/*      */       catch (Exception localException) {
/*      */       }
/*      */     }
/* 5727 */     return str;
/*      */   }
/*      */ 
/*      */   public String getDataStringDate(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 5741 */     this.lastColumnNull = false;
/*      */     String str;
/*      */     try
/*      */     {
/* 5743 */       str = this.OdbcApi.SQLGetDataStringDate(this.hStmt, paramInt);
/*      */     }
/*      */     catch (JdbcOdbcSQLWarning localJdbcOdbcSQLWarning)
/*      */     {
/* 5751 */       str = (String)localJdbcOdbcSQLWarning.value;
/* 5752 */       setWarning(JdbcOdbc.convertWarning(localJdbcOdbcSQLWarning));
/*      */     }
/* 5754 */     if (str == null) {
/* 5755 */       this.lastColumnNull = true;
/*      */     }
/* 5757 */     return str;
/*      */   }
/*      */ 
/*      */   public String getDataStringTime(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 5771 */     this.lastColumnNull = false;
/*      */     String str;
/*      */     try
/*      */     {
/* 5773 */       str = this.OdbcApi.SQLGetDataStringTime(this.hStmt, paramInt);
/*      */     }
/*      */     catch (JdbcOdbcSQLWarning localJdbcOdbcSQLWarning)
/*      */     {
/* 5781 */       str = (String)localJdbcOdbcSQLWarning.value;
/* 5782 */       setWarning(JdbcOdbc.convertWarning(localJdbcOdbcSQLWarning));
/*      */     }
/* 5784 */     if (str == null) {
/* 5785 */       this.lastColumnNull = true;
/*      */     }
/* 5787 */     return str;
/*      */   }
/*      */ 
/*      */   public String getDataStringTimestamp(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 5802 */     this.lastColumnNull = false;
/*      */     String str;
/*      */     try
/*      */     {
/* 5804 */       str = this.OdbcApi.SQLGetDataStringTimestamp(this.hStmt, paramInt);
/*      */     }
/*      */     catch (JdbcOdbcSQLWarning localJdbcOdbcSQLWarning)
/*      */     {
/* 5812 */       str = (String)localJdbcOdbcSQLWarning.value;
/* 5813 */       setWarning(JdbcOdbc.convertWarning(localJdbcOdbcSQLWarning));
/*      */     }
/* 5815 */     if (str == null) {
/* 5816 */       this.lastColumnNull = true;
/*      */     }
/* 5818 */     return str;
/*      */   }
/*      */ 
/*      */   public long getDataLongDate(int paramInt, Calendar paramCalendar)
/*      */     throws SQLException
/*      */   {
/* 5834 */     this.lastColumnNull = false;
/* 5835 */     Date localDate = null;
/* 5836 */     long l = 0L;
/*      */     String str;
/*      */     try
/*      */     {
/* 5839 */       str = this.OdbcApi.SQLGetDataStringDate(this.hStmt, paramInt);
/*      */     }
/*      */     catch (JdbcOdbcSQLWarning localJdbcOdbcSQLWarning)
/*      */     {
/* 5847 */       str = (String)localJdbcOdbcSQLWarning.value;
/* 5848 */       setWarning(JdbcOdbc.convertWarning(localJdbcOdbcSQLWarning));
/*      */     }
/*      */ 
/* 5856 */     if (str != null)
/*      */     {
/* 5858 */       localDate = Date.valueOf(str);
/*      */ 
/* 5860 */       l = this.utils.convertFromGMT(localDate, paramCalendar);
/*      */     }
/* 5862 */     else if (str == null)
/*      */     {
/* 5864 */       this.lastColumnNull = true;
/*      */     }
/*      */ 
/* 5868 */     return l;
/*      */   }
/*      */ 
/*      */   public long getDataLongTime(int paramInt, Calendar paramCalendar)
/*      */     throws SQLException
/*      */   {
/* 5884 */     this.lastColumnNull = false;
/* 5885 */     Time localTime = null;
/*      */ 
/* 5887 */     long l = 0L;
/*      */     String str;
/*      */     try
/*      */     {
/* 5890 */       str = this.OdbcApi.SQLGetDataStringTime(this.hStmt, paramInt);
/*      */     }
/*      */     catch (JdbcOdbcSQLWarning localJdbcOdbcSQLWarning)
/*      */     {
/* 5898 */       str = (String)localJdbcOdbcSQLWarning.value;
/* 5899 */       setWarning(JdbcOdbc.convertWarning(localJdbcOdbcSQLWarning));
/*      */     }
/*      */ 
/* 5902 */     if (str != null)
/*      */     {
/* 5904 */       localTime = Time.valueOf(str);
/*      */ 
/* 5906 */       l = this.utils.convertFromGMT(localTime, paramCalendar);
/*      */     }
/* 5908 */     else if (str == null)
/*      */     {
/* 5910 */       this.lastColumnNull = true;
/*      */     }
/* 5912 */     return l;
/*      */   }
/*      */ 
/*      */   public long getDataLongTimestamp(int paramInt, Calendar paramCalendar)
/*      */     throws SQLException
/*      */   {
/* 5930 */     this.lastColumnNull = false;
/* 5931 */     Timestamp localTimestamp = null;
/*      */ 
/* 5933 */     long l = 0L;
/*      */     String str;
/*      */     try
/*      */     {
/* 5936 */       str = this.OdbcApi.SQLGetDataStringTimestamp(this.hStmt, paramInt);
/*      */     }
/*      */     catch (JdbcOdbcSQLWarning localJdbcOdbcSQLWarning)
/*      */     {
/* 5944 */       str = (String)localJdbcOdbcSQLWarning.value;
/* 5945 */       setWarning(JdbcOdbc.convertWarning(localJdbcOdbcSQLWarning));
/*      */     }
/*      */ 
/* 5948 */     if (str != null)
/*      */     {
/* 5950 */       localTimestamp = Timestamp.valueOf(str);
/*      */ 
/* 5952 */       l = this.utils.convertFromGMT(localTimestamp, paramCalendar);
/*      */     }
/* 5954 */     else if (str == null)
/*      */     {
/* 5956 */       this.lastColumnNull = true;
/*      */     }
/*      */ 
/* 5960 */     return l;
/*      */   }
/*      */ 
/*      */   public int getColumnLength(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 5972 */     int i = -1;
/*      */ 
/* 5977 */     if ((paramInt > 0) && (paramInt <= this.numberOfCols))
/*      */     {
/* 5982 */       i = this.boundCols[(paramInt - 1)].getLength();
/*      */     }
/*      */ 
/* 5988 */     if (i == -1) {
/* 5989 */       i = getColAttribute(paramInt, 3);
/*      */ 
/* 5994 */       if ((paramInt > 0) && (paramInt <= this.numberOfCols))
/*      */       {
/* 5996 */         this.boundCols[(paramInt - 1)].setLength(i);
/*      */       }
/*      */     }
/* 5999 */     return i;
/*      */   }
/*      */ 
/*      */   public int getScale(int paramInt)
/*      */     throws SQLException
/*      */   {
/*      */     int i;
/* 6015 */     if (getPseudoCol(paramInt) != null) {
/* 6016 */       this.lastColumnNull = true;
/* 6017 */       i = 0;
/*      */     }
/*      */     else {
/* 6020 */       i = getColAttribute(paramInt, 5);
/*      */     }
/*      */ 
/* 6023 */     return i;
/*      */   }
/*      */ 
/*      */   public int getColumnType(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 6036 */     int i = 9999;
/*      */ 
/* 6041 */     if ((paramInt > 0) && (paramInt <= this.numberOfCols))
/*      */     {
/* 6046 */       i = this.boundCols[(paramInt - 1)].getType();
/*      */     }
/*      */ 
/* 6052 */     if (i == 9999)
/*      */     {
/* 6054 */       i = getColAttribute(paramInt, 2);
/*      */ 
/* 6059 */       i = OdbcDef.odbcTypeToJdbc(i);
/*      */ 
/* 6062 */       if ((paramInt > 0) && (paramInt <= this.numberOfCols))
/*      */       {
/* 6064 */         this.boundCols[(paramInt - 1)].setType(i);
/*      */       }
/*      */     }
/*      */ 
/* 6068 */     return i;
/*      */   }
/*      */ 
/*      */   public void setPseudoCols(int paramInt1, int paramInt2, JdbcOdbcPseudoCol[] paramArrayOfJdbcOdbcPseudoCol)
/*      */   {
/* 6081 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 6082 */       this.OdbcApi.getTracer().trace("Setting pseudo columns, first=" + paramInt1 + ", last=" + paramInt2);
/*      */     }
/*      */ 
/* 6085 */     this.firstPseudoCol = paramInt1;
/* 6086 */     this.lastPseudoCol = paramInt2;
/* 6087 */     this.pseudoCols = paramArrayOfJdbcOdbcPseudoCol;
/*      */   }
/*      */ 
/*      */   public JdbcOdbcPseudoCol getPseudoCol(int paramInt)
/*      */   {
/* 6099 */     JdbcOdbcPseudoCol localJdbcOdbcPseudoCol = null;
/*      */ 
/* 6107 */     if ((paramInt > 0) && (paramInt >= this.firstPseudoCol) && (paramInt <= this.lastPseudoCol))
/*      */     {
/* 6110 */       localJdbcOdbcPseudoCol = this.pseudoCols[(paramInt - this.firstPseudoCol)];
/*      */     }
/* 6112 */     return localJdbcOdbcPseudoCol;
/*      */   }
/*      */ 
/*      */   public void setSQLTypeColumn(int paramInt)
/*      */   {
/* 6124 */     this.sqlTypeColumn = paramInt;
/*      */   }
/*      */ 
/*      */   protected void setInputStream(int paramInt, JdbcOdbcInputStream paramJdbcOdbcInputStream)
/*      */   {
/* 6142 */     if ((paramInt > 0) && (paramInt <= this.numberOfCols))
/*      */     {
/* 6147 */       this.boundCols[(paramInt - 1)].setInputStream(paramJdbcOdbcInputStream);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void closeInputStreams()
/*      */   {
/* 6161 */     for (int i = 0; i < this.numberOfCols; i++)
/* 6162 */       this.boundCols[i].closeInputStream();
/*      */   }
/*      */ 
/*      */   public void setColumnMappings(int[] paramArrayOfInt)
/*      */   {
/* 6190 */     this.colMappings = paramArrayOfInt;
/*      */   }
/*      */ 
/*      */   public int mapColumn(int paramInt)
/*      */   {
/* 6203 */     int i = paramInt;
/*      */ 
/* 6205 */     if (this.colMappings != null)
/*      */     {
/* 6209 */       if ((paramInt > 0) && (paramInt <= this.colMappings.length))
/*      */       {
/* 6211 */         i = this.colMappings[(paramInt - 1)];
/*      */       }
/*      */       else
/*      */       {
/* 6215 */         i = -1;
/*      */       }
/*      */     }
/*      */ 
/* 6219 */     return i;
/*      */   }
/*      */ 
/*      */   protected void calculateRowCount()
/*      */     throws SQLException
/*      */   {
/*      */     Object localObject1;
/*      */     try
/*      */     {
/* 6237 */       this.numberOfRows = this.OdbcApi.SQLRowCount(this.hStmt);
/*      */     }
/*      */     catch (JdbcOdbcSQLWarning localJdbcOdbcSQLWarning)
/*      */     {
/* 6245 */       localObject1 = (BigDecimal)localJdbcOdbcSQLWarning.value;
/* 6246 */       this.numberOfRows = ((BigDecimal)localObject1).intValue();
/*      */     }
/*      */ 
/* 6249 */     if (this.numberOfRows > 0) return;
/*      */ 
/*      */     try
/*      */     {
/* 6256 */       this.OdbcApi.SQLFetchScroll(this.hStmt, (short)3, 0);
/*      */ 
/* 6266 */       this.numberOfRows = ((int)this.OdbcApi.SQLGetStmtOption(this.hStmt, (short)14));
/*      */     }
/*      */     catch (SQLException localSQLException)
/*      */     {
/*      */     }
/*      */     finally
/*      */     {
/* 6286 */       this.OdbcApi.SQLFetchScroll(this.hStmt, (short)5, 0);
/*      */     }
/*      */ 
/* 6292 */     if (this.numberOfRows > 0)
/*      */     {
/* 6296 */       this.OdbcApi.SQLFetchScroll(this.hStmt, (short)5, 0);
/*      */ 
/* 6301 */       return;
/*      */     }
/*      */ 
/* 6305 */     if (this.ownerStatement != null)
/*      */     {
/* 6307 */       Connection localConnection = this.ownerStatement.getConnection();
/*      */ 
/* 6309 */       localObject1 = this.ownerStatement.getSql();
/*      */ 
/* 6311 */       String str1 = null;
/*      */ 
/* 6313 */       String str2 = this.ownerStatement.getClass().getName();
/*      */ 
/* 6315 */       int i = 0;
/*      */ 
/* 6317 */       i = this.ownerStatement.getParamCount();
/*      */ 
/* 6319 */       if (i > 0)
/*      */       {
/* 6321 */         if (str2.indexOf("CallableStatement") > 0)
/*      */         {
/* 6323 */           throw new SQLException("Unable to obtain result set row count. From " + (String)localObject1);
/*      */         }
/* 6325 */         if (str2.indexOf("PreparedStatement") > 0)
/*      */         {
/* 6328 */           if (((String)localObject1).toLowerCase().indexOf("select") == -1) {
/* 6329 */             throw new SQLException("Cannot obtain result set row count for " + (String)localObject1);
/*      */           }
/* 6331 */           str1 = reWordAsCountQuery((String)localObject1);
/*      */ 
/* 6333 */           if (str1.indexOf("?") > 0)
/*      */           {
/* 6335 */             this.numberOfRows = parameterQuery(localConnection.prepareStatement(str1));
/*      */           }
/*      */           else {
/* 6338 */             i = 0;
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/* 6343 */       if ((str2.indexOf("Statement") > 0) && (i == 0))
/*      */       {
/* 6345 */         Statement localStatement = localConnection.createStatement();
/*      */ 
/* 6347 */         if ((localObject1 != null) && (((String)localObject1).startsWith("SELECT")))
/*      */         {
/* 6349 */           if (str1 == null) {
/* 6350 */             str1 = reWordAsCountQuery((String)localObject1);
/*      */           }
/* 6352 */           ResultSet localResultSet = localStatement.executeQuery(str1);
/*      */ 
/* 6354 */           localResultSet.next();
/*      */ 
/* 6357 */           this.numberOfRows = localResultSet.getInt(1);
/*      */ 
/* 6363 */           if ((str1.indexOf("COUNT(*)") < 0) && (this.numberOfRows > 0))
/*      */           {
/* 6365 */             this.numberOfRows = 1;
/* 6366 */             setWarning(new SQLWarning("ResultSet is not updatable."));
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/* 6371 */         if (localStatement != null) {
/* 6372 */           localStatement.close();
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 6379 */     if (this.numberOfRows > 0) {
/* 6380 */       return;
/*      */     }
/* 6382 */     setWarning(new SQLWarning("Can not determine result set row count."));
/*      */   }
/*      */ 
/*      */   protected int parameterQuery(PreparedStatement paramPreparedStatement)
/*      */     throws SQLException
/*      */   {
/* 6397 */     int i = 0;
/*      */ 
/* 6399 */     Object[] arrayOfObject = null;
/* 6400 */     int[] arrayOfInt = null;
/*      */ 
/* 6402 */     if (paramPreparedStatement != null)
/*      */     {
/*      */       try
/*      */       {
/* 6406 */         arrayOfObject = this.ownerStatement.getObjects();
/* 6407 */         arrayOfInt = this.ownerStatement.getObjectTypes();
/*      */ 
/* 6409 */         for (int j = 0; j < arrayOfObject.length; j++)
/*      */         {
/* 6411 */           paramPreparedStatement.setObject(j + 1, arrayOfObject[j], arrayOfInt[j]);
/*      */         }
/*      */       }
/*      */       catch (Exception localException)
/*      */       {
/* 6416 */         throw new SQLException("while calculating row count: " + localException.getMessage());
/*      */       }
/* 6418 */       ResultSet localResultSet = paramPreparedStatement.executeQuery();
/*      */ 
/* 6420 */       localResultSet.next();
/*      */ 
/* 6423 */       i = localResultSet.getInt(1);
/*      */ 
/* 6425 */       paramPreparedStatement.close();
/*      */     }
/*      */ 
/* 6428 */     return i;
/*      */   }
/*      */ 
/*      */   protected String reWordAsCountQuery(String paramString)
/*      */   {
/* 6441 */     int i = paramString.indexOf(" COUNT(*) ");
/* 6442 */     int j = -1;
/* 6443 */     int k = paramString.indexOf(" FROM ");
/*      */ 
/* 6448 */     int m = paramString.indexOf("'");
/* 6449 */     int n = -1;
/*      */ 
/* 6451 */     if (m > 0)
/*      */     {
/* 6453 */       n = paramString.indexOf("'", m + 2);
/*      */     }
/*      */ 
/* 6461 */     if ((k > m) && (n > k))
/*      */     {
/* 6463 */       k = paramString.indexOf(" FROM ", n);
/*      */     }
/*      */ 
/* 6467 */     if ((i > m) && (n > i)) {
/* 6468 */       i = -1;
/*      */     }
/*      */ 
/* 6475 */     int i1 = -1;
/* 6476 */     int i2 = -1;
/* 6477 */     int i3 = -1;
/* 6478 */     int i4 = -1;
/* 6479 */     int i5 = -1;
/*      */ 
/* 6481 */     int i6 = paramString.indexOf("WHERE");
/* 6482 */     int i7 = -1;
/* 6483 */     if (i6 < k) {
/* 6484 */       i6 = paramString.indexOf("WHERE", i6 + 2);
/*      */     }
/* 6486 */     String str1 = "";
/*      */ 
/* 6488 */     if (i5 < 0)
/*      */     {
/* 6490 */       i1 = paramString.lastIndexOf("ORDER BY");
/* 6491 */       if (i1 > i6) {
/* 6492 */         i5 = i1;
/*      */       }
/* 6494 */       str1 = "ORDER BY";
/*      */     }
/* 6496 */     if (i5 < 0)
/*      */     {
/* 6498 */       i2 = paramString.lastIndexOf("GROUP BY");
/* 6499 */       if ((i2 > i6) && (i2 > i1)) {
/* 6500 */         i5 = i2;
/*      */       }
/* 6502 */       str1 = "GROUP BY";
/*      */     }
/* 6504 */     if (i5 < 0)
/*      */     {
/* 6506 */       i3 = paramString.lastIndexOf("FOR UPDATE");
/* 6507 */       if ((i3 > i6) && (i3 > i2)) {
/* 6508 */         i5 = i3;
/*      */       }
/* 6510 */       str1 = "FOR UPDATE";
/*      */     }
/* 6512 */     if (i5 < 0)
/*      */     {
/* 6514 */       i4 = paramString.lastIndexOf("UNION");
/* 6515 */       if ((i4 > i6) && (i4 > i3)) {
/* 6516 */         i5 = i4;
/*      */       }
/* 6518 */       str1 = "UNION";
/*      */     }
/*      */ 
/* 6521 */     if (i5 > 0)
/*      */     {
/* 6527 */       if (i5 > k)
/*      */       {
/* 6529 */         if ((i6 > 0) && (i6 > k))
/*      */         {
/* 6532 */           int i8 = paramString.indexOf("'", i6);
/* 6533 */           int i9 = -1;
/*      */ 
/* 6535 */           if (i8 > 0)
/*      */           {
/* 6537 */             i9 = paramString.indexOf("'", i8 + 2);
/*      */           }
/*      */ 
/* 6542 */           if ((i5 > i8) && (n > i5))
/*      */           {
/* 6544 */             i5 = paramString.indexOf(str1, i9);
/*      */           }
/*      */ 
/* 6547 */           if (i5 > i9)
/* 6548 */             paramString = paramString.substring(0, i5);
/*      */         }
/*      */         else {
/* 6551 */           paramString = paramString.substring(0, i5);
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 6557 */     String str2 = paramString.substring(0, k);
/* 6558 */     StringBuffer localStringBuffer = new StringBuffer(paramString);
/*      */ 
/* 6563 */     if ((i < 0) && (j < 0))
/*      */     {
/* 6565 */       j = str2.lastIndexOf(")");
/*      */ 
/* 6567 */       if (j > 0)
/*      */       {
/* 6569 */         int i10 = paramString.indexOf(" (");
/*      */ 
/* 6572 */         if (i10 > 0)
/*      */         {
/* 6574 */           if ((i10 < m) && (i10 < n))
/* 6575 */             j = -1;
/*      */         }
/* 6577 */         else if (i10 < 0)
/*      */         {
/* 6579 */           j = -1;
/* 6580 */           i = i10;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 6588 */     if (j > 0)
/*      */     {
/* 6590 */       localStringBuffer.insert(6, " COUNT(*), ");
/*      */     }
/* 6592 */     else if ((i < 0) && (k > 0))
/*      */     {
/* 6594 */       localStringBuffer.replace(6, k, " COUNT(*) ");
/*      */     }
/*      */ 
/* 6597 */     return localStringBuffer.toString();
/*      */   }
/*      */ 
/*      */   protected void setCursorType()
/*      */     throws SQLException
/*      */   {
/* 6613 */     clearWarnings();
/*      */     try
/*      */     {
/* 6616 */       long l = this.OdbcApi.SQLGetStmtOption(this.hStmt, (short)6);
/*      */ 
/* 6618 */       this.odbcCursorType = ((short)(int)l);
/*      */     }
/*      */     catch (JdbcOdbcSQLWarning localJdbcOdbcSQLWarning)
/*      */     {
/* 6626 */       BigDecimal localBigDecimal = (BigDecimal)localJdbcOdbcSQLWarning.value;
/* 6627 */       this.odbcCursorType = localBigDecimal.shortValue();
/*      */ 
/* 6629 */       setWarning(JdbcOdbc.convertWarning(localJdbcOdbcSQLWarning));
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void checkOpen()
/*      */     throws SQLException
/*      */   {
/* 6644 */     if (this.closed)
/* 6645 */       throw new SQLException("ResultSet is closed");
/*      */   }
/*      */ 
/*      */   public SQLXML getSQLXML(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 6658 */     throw new UnsupportedOperationException("Operation not yet supported");
/*      */   }
/*      */ 
/*      */   public SQLXML getSQLXML(String paramString)
/*      */     throws SQLException
/*      */   {
/* 6670 */     throw new UnsupportedOperationException("Operation not yet supported");
/*      */   }
/*      */ 
/*      */   public RowId getRowId(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 6685 */     throw new UnsupportedOperationException("Operation not yet supported");
/*      */   }
/*      */ 
/*      */   public RowId getRowId(String paramString)
/*      */     throws SQLException
/*      */   {
/* 6700 */     throw new UnsupportedOperationException("Operation not yet supported");
/*      */   }
/*      */ 
/*      */   public void updateRowId(int paramInt, RowId paramRowId)
/*      */     throws SQLException
/*      */   {
/* 6716 */     throw new UnsupportedOperationException("Operation not yet supported");
/*      */   }
/*      */ 
/*      */   public void updateRowId(String paramString, RowId paramRowId)
/*      */     throws SQLException
/*      */   {
/* 6732 */     throw new UnsupportedOperationException("Operation not yet supported");
/*      */   }
/*      */ 
/*      */   public int getHoldability()
/*      */     throws SQLException
/*      */   {
/* 6742 */     throw new UnsupportedOperationException("Operation not yet supported");
/*      */   }
/*      */ 
/*      */   public boolean isClosed()
/*      */     throws SQLException
/*      */   {
/* 6753 */     throw new UnsupportedOperationException("Operation not yet supported");
/*      */   }
/*      */ 
/*      */   public void updateNString(int paramInt, String paramString)
/*      */     throws SQLException
/*      */   {
/* 6765 */     throw new UnsupportedOperationException("Operation not yet supported");
/*      */   }
/*      */ 
/*      */   public void updateNString(String paramString1, String paramString2)
/*      */     throws SQLException
/*      */   {
/* 6777 */     throw new UnsupportedOperationException("Operation not yet supported");
/*      */   }
/*      */ 
/*      */   public void updateNClob(int paramInt, NClob paramNClob)
/*      */     throws SQLException
/*      */   {
/* 6790 */     throw new UnsupportedOperationException("Operation not yet supported");
/*      */   }
/*      */ 
/*      */   public void updateNClob(String paramString, NClob paramNClob)
/*      */     throws SQLException
/*      */   {
/* 6802 */     throw new UnsupportedOperationException("Operation not yet supported");
/*      */   }
/*      */ 
/*      */   public NClob getNClob(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 6817 */     throw new UnsupportedOperationException("Operation not yet supported");
/*      */   }
/*      */ 
/*      */   public NClob getNClob(String paramString)
/*      */     throws SQLException
/*      */   {
/* 6833 */     throw new UnsupportedOperationException("Operation not yet supported");
/*      */   }
/*      */ 
/*      */   public <T> T unwrap(Class<T> paramClass)
/*      */     throws SQLException
/*      */   {
/* 6850 */     return null;
/*      */   }
/*      */ 
/*      */   public boolean isWrapperFor(Class<?> paramClass)
/*      */     throws SQLException
/*      */   {
/* 6869 */     return false;
/*      */   }
/*      */ 
/*      */   public Reader getNCharacterStream(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 6889 */     throw new UnsupportedOperationException("Operation not yet supported");
/*      */   }
/*      */ 
/*      */   public Reader getNCharacterStream(String paramString)
/*      */     throws SQLException
/*      */   {
/* 6908 */     throw new UnsupportedOperationException("Operation not yet supported");
/*      */   }
/*      */ 
/*      */   public void updateSQLXML(int paramInt, SQLXML paramSQLXML)
/*      */     throws SQLException
/*      */   {
/* 6924 */     throw new UnsupportedOperationException("Operation not yet supported");
/*      */   }
/*      */ 
/*      */   public void updateSQLXML(String paramString, SQLXML paramSQLXML)
/*      */     throws SQLException
/*      */   {
/* 6941 */     throw new UnsupportedOperationException("Operation not yet supported");
/*      */   }
/*      */ 
/*      */   public String getNString(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 6959 */     throw new UnsupportedOperationException("Operation not yet supported");
/*      */   }
/*      */ 
/*      */   public String getNString(String paramString)
/*      */     throws SQLException
/*      */   {
/* 6977 */     throw new UnsupportedOperationException("Operation not yet supported");
/*      */   }
/*      */ 
/*      */   public void updateNCharacterStream(int paramInt1, Reader paramReader, int paramInt2)
/*      */     throws SQLException
/*      */   {
/* 6999 */     throw new UnsupportedOperationException("Operation not yet supported");
/*      */   }
/*      */ 
/*      */   public void updateNCharacterStream(String paramString, Reader paramReader, int paramInt)
/*      */     throws SQLException
/*      */   {
/* 7021 */     throw new UnsupportedOperationException("Operation not yet supported");
/*      */   }
/*      */ 
/*      */   public void updateNClob(String paramString, Reader paramReader, long paramLong)
/*      */     throws SQLException
/*      */   {
/*      */   }
/*      */ 
/*      */   public void updateNClob(int paramInt, Reader paramReader, long paramLong)
/*      */     throws SQLException
/*      */   {
/*      */   }
/*      */ 
/*      */   public void updateClob(String paramString, Reader paramReader, long paramLong)
/*      */     throws SQLException
/*      */   {
/*      */   }
/*      */ 
/*      */   public void updateClob(int paramInt, Reader paramReader, long paramLong)
/*      */     throws SQLException
/*      */   {
/*      */   }
/*      */ 
/*      */   public void updateBlob(String paramString, InputStream paramInputStream, long paramLong)
/*      */     throws SQLException
/*      */   {
/*      */   }
/*      */ 
/*      */   public void updateBlob(int paramInt, InputStream paramInputStream, long paramLong)
/*      */     throws SQLException
/*      */   {
/*      */   }
/*      */ 
/*      */   public void updateCharacterStream(String paramString, Reader paramReader, long paramLong)
/*      */     throws SQLException
/*      */   {
/*      */   }
/*      */ 
/*      */   public void updateBinaryStream(String paramString, InputStream paramInputStream, long paramLong)
/*      */     throws SQLException
/*      */   {
/*      */   }
/*      */ 
/*      */   public void updateAsciiStream(String paramString, InputStream paramInputStream, long paramLong) throws SQLException
/*      */   {
/*      */   }
/*      */ 
/*      */   public void updateCharacterStream(int paramInt, Reader paramReader, long paramLong) throws SQLException
/*      */   {
/*      */   }
/*      */ 
/*      */   public void updateBinaryStream(int paramInt, InputStream paramInputStream, long paramLong) throws SQLException
/*      */   {
/*      */   }
/*      */ 
/*      */   public void updateAsciiStream(int paramInt, InputStream paramInputStream, long paramLong) throws SQLException
/*      */   {
/*      */   }
/*      */ 
/*      */   public void updateNCharacterStream(String paramString, Reader paramReader, long paramLong) throws SQLException
/*      */   {
/*      */   }
/*      */ 
/*      */   public void updateNCharacterStream(int paramInt, Reader paramReader, long paramLong) throws SQLException
/*      */   {
/*      */   }
/*      */ 
/*      */   public void updateNCharacterStream(int paramInt, Reader paramReader) throws SQLException
/*      */   {
/*      */   }
/*      */ 
/*      */   public void updateNCharacterStream(String paramString, Reader paramReader) throws SQLException
/*      */   {
/*      */   }
/*      */ 
/*      */   public void updateAsciiStream(int paramInt, InputStream paramInputStream) throws SQLException
/*      */   {
/*      */   }
/*      */ 
/*      */   public void updateBinaryStream(int paramInt, InputStream paramInputStream) throws SQLException
/*      */   {
/*      */   }
/*      */ 
/*      */   public void updateCharacterStream(int paramInt, Reader paramReader) throws SQLException
/*      */   {
/*      */   }
/*      */ 
/*      */   public void updateAsciiStream(String paramString, InputStream paramInputStream) throws SQLException
/*      */   {
/*      */   }
/*      */ 
/*      */   public void updateBinaryStream(String paramString, InputStream paramInputStream) throws SQLException
/*      */   {
/*      */   }
/*      */ 
/*      */   public void updateCharacterStream(String paramString, Reader paramReader) throws SQLException
/*      */   {
/*      */   }
/*      */ 
/*      */   public void updateBlob(int paramInt, InputStream paramInputStream) throws SQLException
/*      */   {
/*      */   }
/*      */ 
/*      */   public void updateBlob(String paramString, InputStream paramInputStream) throws SQLException
/*      */   {
/*      */   }
/*      */ 
/*      */   public void updateClob(int paramInt, Reader paramReader) throws SQLException
/*      */   {
/*      */   }
/*      */ 
/*      */   public void updateClob(String paramString, Reader paramReader) throws SQLException
/*      */   {
/*      */   }
/*      */ 
/*      */   public void updateNClob(int paramInt, Reader paramReader) throws SQLException
/*      */   {
/*      */   }
/*      */ 
/*      */   public void updateNClob(String paramString, Reader paramReader) throws SQLException
/*      */   {
/*      */   }
/*      */ 
/*      */   public <T> T getObject(int paramInt, Class<T> paramClass) throws SQLException
/*      */   {
/* 7146 */     throw new SQLFeatureNotSupportedException();
/*      */   }
/*      */ 
/*      */   public <T> T getObject(String paramString, Class<T> paramClass)
/*      */     throws SQLException
/*      */   {
/* 7178 */     throw new SQLFeatureNotSupportedException();
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.jdbc.odbc.JdbcOdbcResultSet
 * JD-Core Version:    0.6.2
 */