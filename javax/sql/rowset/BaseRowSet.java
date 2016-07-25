/*      */ package javax.sql.rowset;
/*      */ 
/*      */ import java.io.InputStream;
/*      */ import java.io.Reader;
/*      */ import java.io.Serializable;
/*      */ import java.math.BigDecimal;
/*      */ import java.net.URL;
/*      */ import java.sql.Array;
/*      */ import java.sql.Blob;
/*      */ import java.sql.Clob;
/*      */ import java.sql.Date;
/*      */ import java.sql.NClob;
/*      */ import java.sql.Ref;
/*      */ import java.sql.RowId;
/*      */ import java.sql.SQLException;
/*      */ import java.sql.SQLFeatureNotSupportedException;
/*      */ import java.sql.SQLXML;
/*      */ import java.sql.Time;
/*      */ import java.sql.Timestamp;
/*      */ import java.util.Calendar;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Iterator;
/*      */ import java.util.Map;
/*      */ import java.util.Vector;
/*      */ import javax.sql.RowSet;
/*      */ import javax.sql.RowSetEvent;
/*      */ import javax.sql.RowSetListener;
/*      */ import javax.sql.rowset.serial.SerialArray;
/*      */ import javax.sql.rowset.serial.SerialBlob;
/*      */ import javax.sql.rowset.serial.SerialClob;
/*      */ import javax.sql.rowset.serial.SerialRef;
/*      */ 
/*      */ public abstract class BaseRowSet
/*      */   implements Serializable, Cloneable
/*      */ {
/*      */   public static final int UNICODE_STREAM_PARAM = 0;
/*      */   public static final int BINARY_STREAM_PARAM = 1;
/*      */   public static final int ASCII_STREAM_PARAM = 2;
/*      */   protected InputStream binaryStream;
/*      */   protected InputStream unicodeStream;
/*      */   protected InputStream asciiStream;
/*      */   protected Reader charStream;
/*      */   private String command;
/*      */   private String URL;
/*      */   private String dataSource;
/*      */   private transient String username;
/*      */   private transient String password;
/*  409 */   private int rowSetType = 1004;
/*      */ 
/*  416 */   private boolean showDeleted = false;
/*      */ 
/*  426 */   private int queryTimeout = 0;
/*      */ 
/*  432 */   private int maxRows = 0;
/*      */ 
/*  438 */   private int maxFieldSize = 0;
/*      */ 
/*  447 */   private int concurrency = 1008;
/*      */   private boolean readOnly;
/*      */   private boolean escapeProcessing;
/*      */   private int isolation;
/*  491 */   private int fetchDir = 1000;
/*      */ 
/*  500 */   private int fetchSize = 0;
/*      */   private Map<String, Class<?>> map;
/*      */   private Vector<RowSetListener> listeners;
/*      */   private Hashtable<Integer, Object> params;
/*      */   static final long serialVersionUID = 4886719666485113312L;
/*      */ 
/*      */   public BaseRowSet()
/*      */   {
/*  532 */     this.listeners = new Vector();
/*      */   }
/*      */ 
/*      */   protected void initParams()
/*      */   {
/*  545 */     this.params = new Hashtable();
/*      */   }
/*      */ 
/*      */   public void addRowSetListener(RowSetListener paramRowSetListener)
/*      */   {
/*  575 */     this.listeners.add(paramRowSetListener);
/*      */   }
/*      */ 
/*      */   public void removeRowSetListener(RowSetListener paramRowSetListener)
/*      */   {
/*  592 */     this.listeners.remove(paramRowSetListener);
/*      */   }
/*      */ 
/*      */   private void checkforRowSetInterface()
/*      */     throws SQLException
/*      */   {
/*  599 */     if (!(this instanceof RowSet))
/*  600 */       throw new SQLException("The class extending abstract class BaseRowSet must implement javax.sql.RowSet or one of it's sub-interfaces.");
/*      */   }
/*      */ 
/*      */   protected void notifyCursorMoved()
/*      */     throws SQLException
/*      */   {
/*  619 */     checkforRowSetInterface();
/*      */     RowSetEvent localRowSetEvent;
/*      */     Iterator localIterator;
/*  620 */     if (!this.listeners.isEmpty()) {
/*  621 */       localRowSetEvent = new RowSetEvent((RowSet)this);
/*  622 */       for (localIterator = this.listeners.iterator(); localIterator.hasNext(); )
/*  623 */         ((RowSetListener)localIterator.next()).cursorMoved(localRowSetEvent);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void notifyRowChanged()
/*      */     throws SQLException
/*      */   {
/*  644 */     checkforRowSetInterface();
/*      */     RowSetEvent localRowSetEvent;
/*      */     Iterator localIterator;
/*  645 */     if (!this.listeners.isEmpty()) {
/*  646 */       localRowSetEvent = new RowSetEvent((RowSet)this);
/*  647 */       for (localIterator = this.listeners.iterator(); localIterator.hasNext(); )
/*  648 */         ((RowSetListener)localIterator.next()).rowChanged(localRowSetEvent);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void notifyRowSetChanged()
/*      */     throws SQLException
/*      */   {
/*  669 */     checkforRowSetInterface();
/*      */     RowSetEvent localRowSetEvent;
/*      */     Iterator localIterator;
/*  670 */     if (!this.listeners.isEmpty()) {
/*  671 */       localRowSetEvent = new RowSetEvent((RowSet)this);
/*  672 */       for (localIterator = this.listeners.iterator(); localIterator.hasNext(); )
/*  673 */         ((RowSetListener)localIterator.next()).rowSetChanged(localRowSetEvent);
/*      */     }
/*      */   }
/*      */ 
/*      */   public String getCommand()
/*      */   {
/*  702 */     return this.command;
/*      */   }
/*      */ 
/*      */   public void setCommand(String paramString)
/*      */     throws SQLException
/*      */   {
/*  726 */     if (paramString == null) {
/*  727 */       this.command = null; } else {
/*  728 */       if (paramString.length() == 0) {
/*  729 */         throw new SQLException("Invalid command string detected. Cannot be of length less than 0");
/*      */       }
/*      */ 
/*  733 */       if (this.params == null) {
/*  734 */         throw new SQLException("Set initParams() before setCommand");
/*      */       }
/*  736 */       this.params.clear();
/*  737 */       this.command = paramString;
/*      */     }
/*      */   }
/*      */ 
/*      */   public String getUrl()
/*      */     throws SQLException
/*      */   {
/*  758 */     return this.URL;
/*      */   }
/*      */ 
/*      */   public void setUrl(String paramString)
/*      */     throws SQLException
/*      */   {
/*  794 */     if (paramString == null) {
/*  795 */       paramString = null; } else {
/*  796 */       if (paramString.length() < 1) {
/*  797 */         throw new SQLException("Invalid url string detected. Cannot be of length less than 1");
/*      */       }
/*      */ 
/*  800 */       this.URL = paramString;
/*      */     }
/*      */ 
/*  803 */     this.dataSource = null;
/*      */   }
/*      */ 
/*      */   public String getDataSourceName()
/*      */   {
/*  825 */     return this.dataSource;
/*      */   }
/*      */ 
/*      */   public void setDataSourceName(String paramString)
/*      */     throws SQLException
/*      */   {
/*  852 */     if (paramString == null) {
/*  853 */       this.dataSource = null; } else {
/*  854 */       if (paramString.equals("")) {
/*  855 */         throw new SQLException("DataSource name cannot be empty string");
/*      */       }
/*  857 */       this.dataSource = paramString;
/*      */     }
/*      */ 
/*  860 */     this.URL = null;
/*      */   }
/*      */ 
/*      */   public String getUsername()
/*      */   {
/*  874 */     return this.username;
/*      */   }
/*      */ 
/*      */   public void setUsername(String paramString)
/*      */   {
/*  888 */     if (paramString == null)
/*      */     {
/*  890 */       this.username = null;
/*      */     }
/*  892 */     else this.username = paramString;
/*      */   }
/*      */ 
/*      */   public String getPassword()
/*      */   {
/*  907 */     return this.password;
/*      */   }
/*      */ 
/*      */   public void setPassword(String paramString)
/*      */   {
/*  923 */     if (paramString == null)
/*      */     {
/*  925 */       this.password = null;
/*      */     }
/*  927 */     else this.password = paramString;
/*      */   }
/*      */ 
/*      */   public void setType(int paramInt)
/*      */     throws SQLException
/*      */   {
/*  949 */     if ((paramInt != 1003) && (paramInt != 1004) && (paramInt != 1005))
/*      */     {
/*  952 */       throw new SQLException("Invalid type of RowSet set. Must be either ResultSet.TYPE_FORWARD_ONLY or ResultSet.TYPE_SCROLL_INSENSITIVE or ResultSet.TYPE_SCROLL_SENSITIVE.");
/*      */     }
/*      */ 
/*  956 */     this.rowSetType = paramInt;
/*      */   }
/*      */ 
/*      */   public int getType()
/*      */     throws SQLException
/*      */   {
/*  976 */     return this.rowSetType;
/*      */   }
/*      */ 
/*      */   public void setConcurrency(int paramInt)
/*      */     throws SQLException
/*      */   {
/*  997 */     if ((paramInt != 1007) && (paramInt != 1008))
/*      */     {
/*  999 */       throw new SQLException("Invalid concurrency set. Must be either ResultSet.CONCUR_READ_ONLY or ResultSet.CONCUR_UPDATABLE.");
/*      */     }
/*      */ 
/* 1002 */     this.concurrency = paramInt;
/*      */   }
/*      */ 
/*      */   public boolean isReadOnly()
/*      */   {
/* 1018 */     return this.readOnly;
/*      */   }
/*      */ 
/*      */   public void setReadOnly(boolean paramBoolean)
/*      */   {
/* 1029 */     this.readOnly = paramBoolean;
/*      */   }
/*      */ 
/*      */   public int getTransactionIsolation()
/*      */   {
/* 1056 */     return this.isolation;
/*      */   }
/*      */ 
/*      */   public void setTransactionIsolation(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 1084 */     if ((paramInt != 0) && (paramInt != 2) && (paramInt != 1) && (paramInt != 4) && (paramInt != 8))
/*      */     {
/* 1090 */       throw new SQLException("Invalid transaction isolation set. Must be either Connection.TRANSACTION_NONE or Connection.TRANSACTION_READ_UNCOMMITTED or Connection.TRANSACTION_READ_COMMITTED or Connection.RRANSACTION_REPEATABLE_READ or Connection.TRANSACTION_SERIALIZABLE");
/*      */     }
/*      */ 
/* 1098 */     this.isolation = paramInt;
/*      */   }
/*      */ 
/*      */   public Map<String, Class<?>> getTypeMap()
/*      */   {
/* 1120 */     return this.map;
/*      */   }
/*      */ 
/*      */   public void setTypeMap(Map<String, Class<?>> paramMap)
/*      */   {
/* 1138 */     this.map = paramMap;
/*      */   }
/*      */ 
/*      */   public int getMaxFieldSize()
/*      */     throws SQLException
/*      */   {
/* 1156 */     return this.maxFieldSize;
/*      */   }
/*      */ 
/*      */   public void setMaxFieldSize(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 1175 */     if (paramInt < 0) {
/* 1176 */       throw new SQLException("Invalid max field size set. Cannot be of value: " + paramInt);
/*      */     }
/*      */ 
/* 1179 */     this.maxFieldSize = paramInt;
/*      */   }
/*      */ 
/*      */   public int getMaxRows()
/*      */     throws SQLException
/*      */   {
/* 1192 */     return this.maxRows;
/*      */   }
/*      */ 
/*      */   public void setMaxRows(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 1209 */     if (paramInt < 0) {
/* 1210 */       throw new SQLException("Invalid max row size set. Cannot be of value: " + paramInt);
/*      */     }
/* 1212 */     if (paramInt < getFetchSize()) {
/* 1213 */       throw new SQLException("Invalid max row size set. Cannot be less than the fetchSize.");
/*      */     }
/*      */ 
/* 1216 */     this.maxRows = paramInt;
/*      */   }
/*      */ 
/*      */   public void setEscapeProcessing(boolean paramBoolean)
/*      */     throws SQLException
/*      */   {
/* 1235 */     this.escapeProcessing = paramBoolean;
/*      */   }
/*      */ 
/*      */   public int getQueryTimeout()
/*      */     throws SQLException
/*      */   {
/* 1249 */     return this.queryTimeout;
/*      */   }
/*      */ 
/*      */   public void setQueryTimeout(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 1263 */     if (paramInt < 0) {
/* 1264 */       throw new SQLException("Invalid query timeout value set. Cannot be of value: " + paramInt);
/*      */     }
/*      */ 
/* 1267 */     this.queryTimeout = paramInt;
/*      */   }
/*      */ 
/*      */   public boolean getShowDeleted()
/*      */     throws SQLException
/*      */   {
/* 1287 */     return this.showDeleted;
/*      */   }
/*      */ 
/*      */   public void setShowDeleted(boolean paramBoolean)
/*      */     throws SQLException
/*      */   {
/* 1302 */     this.showDeleted = paramBoolean;
/*      */   }
/*      */ 
/*      */   public boolean getEscapeProcessing()
/*      */     throws SQLException
/*      */   {
/* 1316 */     return this.escapeProcessing;
/*      */   }
/*      */ 
/*      */   public void setFetchDirection(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 1353 */     if (((getType() == 1003) && (paramInt != 1000)) || ((paramInt != 1000) && (paramInt != 1001) && (paramInt != 1002)))
/*      */     {
/* 1357 */       throw new SQLException("Invalid Fetch Direction");
/*      */     }
/* 1359 */     this.fetchDir = paramInt;
/*      */   }
/*      */ 
/*      */   public int getFetchDirection()
/*      */     throws SQLException
/*      */   {
/* 1388 */     return this.fetchDir;
/*      */   }
/*      */ 
/*      */   public void setFetchSize(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 1437 */     if ((getMaxRows() == 0) && (paramInt >= 0)) {
/* 1438 */       this.fetchSize = paramInt;
/* 1439 */       return;
/*      */     }
/* 1441 */     if ((paramInt < 0) || (paramInt > getMaxRows())) {
/* 1442 */       throw new SQLException("Invalid fetch size set. Cannot be of value: " + paramInt);
/*      */     }
/*      */ 
/* 1445 */     this.fetchSize = paramInt;
/*      */   }
/*      */ 
/*      */   public int getFetchSize()
/*      */     throws SQLException
/*      */   {
/* 1459 */     return this.fetchSize;
/*      */   }
/*      */ 
/*      */   public int getConcurrency()
/*      */     throws SQLException
/*      */   {
/* 1480 */     return this.concurrency;
/*      */   }
/*      */ 
/*      */   private void checkParamIndex(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 1499 */     if (paramInt < 1)
/* 1500 */       throw new SQLException("Invalid Parameter Index");
/*      */   }
/*      */ 
/*      */   public void setNull(int paramInt1, int paramInt2)
/*      */     throws SQLException
/*      */   {
/* 1562 */     checkParamIndex(paramInt1);
/*      */ 
/* 1564 */     Object[] arrayOfObject = new Object[2];
/* 1565 */     arrayOfObject[0] = null;
/* 1566 */     arrayOfObject[1] = Integer.valueOf(paramInt2);
/*      */ 
/* 1568 */     if (this.params == null) {
/* 1569 */       throw new SQLException("Set initParams() before setNull");
/*      */     }
/*      */ 
/* 1572 */     this.params.put(Integer.valueOf(paramInt1 - 1), arrayOfObject);
/*      */   }
/*      */ 
/*      */   public void setNull(int paramInt1, int paramInt2, String paramString)
/*      */     throws SQLException
/*      */   {
/* 1643 */     checkParamIndex(paramInt1);
/*      */ 
/* 1645 */     Object[] arrayOfObject = new Object[3];
/* 1646 */     arrayOfObject[0] = null;
/* 1647 */     arrayOfObject[1] = Integer.valueOf(paramInt2);
/* 1648 */     arrayOfObject[2] = paramString;
/*      */ 
/* 1650 */     if (this.params == null) {
/* 1651 */       throw new SQLException("Set initParams() before setNull");
/*      */     }
/*      */ 
/* 1654 */     this.params.put(Integer.valueOf(paramInt1 - 1), arrayOfObject);
/*      */   }
/*      */ 
/*      */   public void setBoolean(int paramInt, boolean paramBoolean)
/*      */     throws SQLException
/*      */   {
/* 1683 */     checkParamIndex(paramInt);
/*      */ 
/* 1685 */     if (this.params == null) {
/* 1686 */       throw new SQLException("Set initParams() before setNull");
/*      */     }
/*      */ 
/* 1689 */     this.params.put(Integer.valueOf(paramInt - 1), Boolean.valueOf(paramBoolean));
/*      */   }
/*      */ 
/*      */   public void setByte(int paramInt, byte paramByte)
/*      */     throws SQLException
/*      */   {
/* 1717 */     checkParamIndex(paramInt);
/*      */ 
/* 1719 */     if (this.params == null) {
/* 1720 */       throw new SQLException("Set initParams() before setByte");
/*      */     }
/*      */ 
/* 1723 */     this.params.put(Integer.valueOf(paramInt - 1), Byte.valueOf(paramByte));
/*      */   }
/*      */ 
/*      */   public void setShort(int paramInt, short paramShort)
/*      */     throws SQLException
/*      */   {
/* 1751 */     checkParamIndex(paramInt);
/*      */ 
/* 1753 */     if (this.params == null) {
/* 1754 */       throw new SQLException("Set initParams() before setShort");
/*      */     }
/*      */ 
/* 1757 */     this.params.put(Integer.valueOf(paramInt - 1), Short.valueOf(paramShort));
/*      */   }
/*      */ 
/*      */   public void setInt(int paramInt1, int paramInt2)
/*      */     throws SQLException
/*      */   {
/* 1785 */     checkParamIndex(paramInt1);
/* 1786 */     if (this.params == null) {
/* 1787 */       throw new SQLException("Set initParams() before setInt");
/*      */     }
/* 1789 */     this.params.put(Integer.valueOf(paramInt1 - 1), Integer.valueOf(paramInt2));
/*      */   }
/*      */ 
/*      */   public void setLong(int paramInt, long paramLong)
/*      */     throws SQLException
/*      */   {
/* 1817 */     checkParamIndex(paramInt);
/* 1818 */     if (this.params == null) {
/* 1819 */       throw new SQLException("Set initParams() before setLong");
/*      */     }
/* 1821 */     this.params.put(Integer.valueOf(paramInt - 1), Long.valueOf(paramLong));
/*      */   }
/*      */ 
/*      */   public void setFloat(int paramInt, float paramFloat)
/*      */     throws SQLException
/*      */   {
/* 1849 */     checkParamIndex(paramInt);
/* 1850 */     if (this.params == null) {
/* 1851 */       throw new SQLException("Set initParams() before setFloat");
/*      */     }
/* 1853 */     this.params.put(Integer.valueOf(paramInt - 1), new Float(paramFloat));
/*      */   }
/*      */ 
/*      */   public void setDouble(int paramInt, double paramDouble)
/*      */     throws SQLException
/*      */   {
/* 1881 */     checkParamIndex(paramInt);
/* 1882 */     if (this.params == null) {
/* 1883 */       throw new SQLException("Set initParams() before setDouble");
/*      */     }
/* 1885 */     this.params.put(Integer.valueOf(paramInt - 1), new Double(paramDouble));
/*      */   }
/*      */ 
/*      */   public void setBigDecimal(int paramInt, BigDecimal paramBigDecimal)
/*      */     throws SQLException
/*      */   {
/* 1913 */     checkParamIndex(paramInt);
/* 1914 */     if (this.params == null) {
/* 1915 */       throw new SQLException("Set initParams() before setBigDecimal");
/*      */     }
/* 1917 */     this.params.put(Integer.valueOf(paramInt - 1), paramBigDecimal);
/*      */   }
/*      */ 
/*      */   public void setString(int paramInt, String paramString)
/*      */     throws SQLException
/*      */   {
/* 1947 */     checkParamIndex(paramInt);
/* 1948 */     if (this.params == null) {
/* 1949 */       throw new SQLException("Set initParams() before setString");
/*      */     }
/* 1951 */     this.params.put(Integer.valueOf(paramInt - 1), paramString);
/*      */   }
/*      */ 
/*      */   public void setBytes(int paramInt, byte[] paramArrayOfByte)
/*      */     throws SQLException
/*      */   {
/* 1981 */     checkParamIndex(paramInt);
/* 1982 */     if (this.params == null) {
/* 1983 */       throw new SQLException("Set initParams() before setBytes");
/*      */     }
/* 1985 */     this.params.put(Integer.valueOf(paramInt - 1), paramArrayOfByte);
/*      */   }
/*      */ 
/*      */   public void setDate(int paramInt, Date paramDate)
/*      */     throws SQLException
/*      */   {
/* 2022 */     checkParamIndex(paramInt);
/*      */ 
/* 2024 */     if (this.params == null) {
/* 2025 */       throw new SQLException("Set initParams() before setDate");
/*      */     }
/* 2027 */     this.params.put(Integer.valueOf(paramInt - 1), paramDate);
/*      */   }
/*      */ 
/*      */   public void setTime(int paramInt, Time paramTime)
/*      */     throws SQLException
/*      */   {
/* 2067 */     checkParamIndex(paramInt);
/* 2068 */     if (this.params == null) {
/* 2069 */       throw new SQLException("Set initParams() before setTime");
/*      */     }
/*      */ 
/* 2072 */     this.params.put(Integer.valueOf(paramInt - 1), paramTime);
/*      */   }
/*      */ 
/*      */   public void setTimestamp(int paramInt, Timestamp paramTimestamp)
/*      */     throws SQLException
/*      */   {
/* 2110 */     checkParamIndex(paramInt);
/* 2111 */     if (this.params == null) {
/* 2112 */       throw new SQLException("Set initParams() before setTimestamp");
/*      */     }
/*      */ 
/* 2115 */     this.params.put(Integer.valueOf(paramInt - 1), paramTimestamp);
/*      */   }
/*      */ 
/*      */   public void setAsciiStream(int paramInt1, InputStream paramInputStream, int paramInt2)
/*      */     throws SQLException
/*      */   {
/* 2184 */     checkParamIndex(paramInt1);
/*      */ 
/* 2186 */     Object[] arrayOfObject = new Object[3];
/* 2187 */     arrayOfObject[0] = paramInputStream;
/* 2188 */     arrayOfObject[1] = Integer.valueOf(paramInt2);
/* 2189 */     arrayOfObject[2] = Integer.valueOf(2);
/*      */ 
/* 2191 */     if (this.params == null) {
/* 2192 */       throw new SQLException("Set initParams() before setAsciiStream");
/*      */     }
/*      */ 
/* 2195 */     this.params.put(Integer.valueOf(paramInt1 - 1), arrayOfObject);
/*      */   }
/*      */ 
/*      */   public void setAsciiStream(int paramInt, InputStream paramInputStream)
/*      */     throws SQLException
/*      */   {
/* 2223 */     throw new SQLFeatureNotSupportedException("Feature not supported");
/*      */   }
/*      */ 
/*      */   public void setBinaryStream(int paramInt1, InputStream paramInputStream, int paramInt2)
/*      */     throws SQLException
/*      */   {
/* 2289 */     checkParamIndex(paramInt1);
/*      */ 
/* 2291 */     Object[] arrayOfObject = new Object[3];
/* 2292 */     arrayOfObject[0] = paramInputStream;
/* 2293 */     arrayOfObject[1] = Integer.valueOf(paramInt2);
/* 2294 */     arrayOfObject[2] = Integer.valueOf(1);
/* 2295 */     if (this.params == null) {
/* 2296 */       throw new SQLException("Set initParams() before setBinaryStream");
/*      */     }
/*      */ 
/* 2299 */     this.params.put(Integer.valueOf(paramInt1 - 1), arrayOfObject);
/*      */   }
/*      */ 
/*      */   public void setBinaryStream(int paramInt, InputStream paramInputStream)
/*      */     throws SQLException
/*      */   {
/* 2327 */     throw new SQLFeatureNotSupportedException("Feature not supported");
/*      */   }
/*      */ 
/*      */   /** @deprecated */
/*      */   public void setUnicodeStream(int paramInt1, InputStream paramInputStream, int paramInt2)
/*      */     throws SQLException
/*      */   {
/* 2395 */     checkParamIndex(paramInt1);
/*      */ 
/* 2397 */     Object[] arrayOfObject = new Object[3];
/* 2398 */     arrayOfObject[0] = paramInputStream;
/* 2399 */     arrayOfObject[1] = Integer.valueOf(paramInt2);
/* 2400 */     arrayOfObject[2] = Integer.valueOf(0);
/* 2401 */     if (this.params == null) {
/* 2402 */       throw new SQLException("Set initParams() before setUnicodeStream");
/*      */     }
/* 2404 */     this.params.put(Integer.valueOf(paramInt1 - 1), arrayOfObject);
/*      */   }
/*      */ 
/*      */   public void setCharacterStream(int paramInt1, Reader paramReader, int paramInt2)
/*      */     throws SQLException
/*      */   {
/* 2474 */     checkParamIndex(paramInt1);
/*      */ 
/* 2476 */     Object[] arrayOfObject = new Object[2];
/* 2477 */     arrayOfObject[0] = paramReader;
/* 2478 */     arrayOfObject[1] = Integer.valueOf(paramInt2);
/* 2479 */     if (this.params == null) {
/* 2480 */       throw new SQLException("Set initParams() before setCharacterStream");
/*      */     }
/* 2482 */     this.params.put(Integer.valueOf(paramInt1 - 1), arrayOfObject);
/*      */   }
/*      */ 
/*      */   public void setCharacterStream(int paramInt, Reader paramReader)
/*      */     throws SQLException
/*      */   {
/* 2512 */     throw new SQLFeatureNotSupportedException("Feature not supported");
/*      */   }
/*      */ 
/*      */   public void setObject(int paramInt1, Object paramObject, int paramInt2, int paramInt3)
/*      */     throws SQLException
/*      */   {
/* 2590 */     checkParamIndex(paramInt1);
/*      */ 
/* 2592 */     Object[] arrayOfObject = new Object[3];
/* 2593 */     arrayOfObject[0] = paramObject;
/* 2594 */     arrayOfObject[1] = Integer.valueOf(paramInt2);
/* 2595 */     arrayOfObject[2] = Integer.valueOf(paramInt3);
/* 2596 */     if (this.params == null) {
/* 2597 */       throw new SQLException("Set initParams() before setObject");
/*      */     }
/* 2599 */     this.params.put(Integer.valueOf(paramInt1 - 1), arrayOfObject);
/*      */   }
/*      */ 
/*      */   public void setObject(int paramInt1, Object paramObject, int paramInt2)
/*      */     throws SQLException
/*      */   {
/* 2653 */     checkParamIndex(paramInt1);
/*      */ 
/* 2655 */     Object[] arrayOfObject = new Object[2];
/* 2656 */     arrayOfObject[0] = paramObject;
/* 2657 */     arrayOfObject[1] = Integer.valueOf(paramInt2);
/* 2658 */     if (this.params == null) {
/* 2659 */       throw new SQLException("Set initParams() before setObject");
/*      */     }
/* 2661 */     this.params.put(Integer.valueOf(paramInt1 - 1), arrayOfObject);
/*      */   }
/*      */ 
/*      */   public void setObject(int paramInt, Object paramObject)
/*      */     throws SQLException
/*      */   {
/* 2725 */     checkParamIndex(paramInt);
/* 2726 */     if (this.params == null) {
/* 2727 */       throw new SQLException("Set initParams() before setObject");
/*      */     }
/* 2729 */     this.params.put(Integer.valueOf(paramInt - 1), paramObject);
/*      */   }
/*      */ 
/*      */   public void setRef(int paramInt, Ref paramRef)
/*      */     throws SQLException
/*      */   {
/* 2772 */     checkParamIndex(paramInt);
/* 2773 */     if (this.params == null) {
/* 2774 */       throw new SQLException("Set initParams() before setRef");
/*      */     }
/* 2776 */     this.params.put(Integer.valueOf(paramInt - 1), new SerialRef(paramRef));
/*      */   }
/*      */ 
/*      */   public void setBlob(int paramInt, Blob paramBlob)
/*      */     throws SQLException
/*      */   {
/* 2816 */     checkParamIndex(paramInt);
/* 2817 */     if (this.params == null) {
/* 2818 */       throw new SQLException("Set initParams() before setBlob");
/*      */     }
/* 2820 */     this.params.put(Integer.valueOf(paramInt - 1), new SerialBlob(paramBlob));
/*      */   }
/*      */ 
/*      */   public void setClob(int paramInt, Clob paramClob)
/*      */     throws SQLException
/*      */   {
/* 2861 */     checkParamIndex(paramInt);
/* 2862 */     if (this.params == null) {
/* 2863 */       throw new SQLException("Set initParams() before setClob");
/*      */     }
/* 2865 */     this.params.put(Integer.valueOf(paramInt - 1), new SerialClob(paramClob));
/*      */   }
/*      */ 
/*      */   public void setArray(int paramInt, Array paramArray)
/*      */     throws SQLException
/*      */   {
/* 2909 */     checkParamIndex(paramInt);
/* 2910 */     if (this.params == null) {
/* 2911 */       throw new SQLException("Set initParams() before setArray");
/*      */     }
/* 2913 */     this.params.put(Integer.valueOf(paramInt - 1), new SerialArray(paramArray));
/*      */   }
/*      */ 
/*      */   public void setDate(int paramInt, Date paramDate, Calendar paramCalendar)
/*      */     throws SQLException
/*      */   {
/* 2970 */     checkParamIndex(paramInt);
/*      */ 
/* 2972 */     Object[] arrayOfObject = new Object[2];
/* 2973 */     arrayOfObject[0] = paramDate;
/* 2974 */     arrayOfObject[1] = paramCalendar;
/* 2975 */     if (this.params == null) {
/* 2976 */       throw new SQLException("Set initParams() before setDate");
/*      */     }
/* 2978 */     this.params.put(Integer.valueOf(paramInt - 1), arrayOfObject);
/*      */   }
/*      */ 
/*      */   public void setTime(int paramInt, Time paramTime, Calendar paramCalendar)
/*      */     throws SQLException
/*      */   {
/* 3036 */     checkParamIndex(paramInt);
/*      */ 
/* 3038 */     Object[] arrayOfObject = new Object[2];
/* 3039 */     arrayOfObject[0] = paramTime;
/* 3040 */     arrayOfObject[1] = paramCalendar;
/* 3041 */     if (this.params == null) {
/* 3042 */       throw new SQLException("Set initParams() before setTime");
/*      */     }
/* 3044 */     this.params.put(Integer.valueOf(paramInt - 1), arrayOfObject);
/*      */   }
/*      */ 
/*      */   public void setTimestamp(int paramInt, Timestamp paramTimestamp, Calendar paramCalendar)
/*      */     throws SQLException
/*      */   {
/* 3102 */     checkParamIndex(paramInt);
/*      */ 
/* 3104 */     Object[] arrayOfObject = new Object[2];
/* 3105 */     arrayOfObject[0] = paramTimestamp;
/* 3106 */     arrayOfObject[1] = paramCalendar;
/* 3107 */     if (this.params == null) {
/* 3108 */       throw new SQLException("Set initParams() before setTimestamp");
/*      */     }
/* 3110 */     this.params.put(Integer.valueOf(paramInt - 1), arrayOfObject);
/*      */   }
/*      */ 
/*      */   public void clearParameters()
/*      */     throws SQLException
/*      */   {
/* 3132 */     this.params.clear();
/*      */   }
/*      */ 
/*      */   public Object[] getParams()
/*      */     throws SQLException
/*      */   {
/* 3170 */     if (this.params == null)
/*      */     {
/* 3172 */       initParams();
/* 3173 */       arrayOfObject = new Object[this.params.size()];
/* 3174 */       return arrayOfObject;
/*      */     }
/*      */ 
/* 3182 */     Object[] arrayOfObject = new Object[this.params.size()];
/* 3183 */     for (int i = 0; i < this.params.size(); i++) {
/* 3184 */       arrayOfObject[i] = this.params.get(Integer.valueOf(i));
/* 3185 */       if (arrayOfObject[i] == null) {
/* 3186 */         throw new SQLException("missing parameter: " + (i + 1));
/*      */       }
/*      */     }
/* 3189 */     return arrayOfObject;
/*      */   }
/*      */ 
/*      */   public void setNull(String paramString, int paramInt)
/*      */     throws SQLException
/*      */   {
/* 3210 */     throw new SQLFeatureNotSupportedException("Feature not supported");
/*      */   }
/*      */ 
/*      */   public void setNull(String paramString1, int paramInt, String paramString2)
/*      */     throws SQLException
/*      */   {
/* 3248 */     throw new SQLFeatureNotSupportedException("Feature not supported");
/*      */   }
/*      */ 
/*      */   public void setBoolean(String paramString, boolean paramBoolean)
/*      */     throws SQLException
/*      */   {
/* 3268 */     throw new SQLFeatureNotSupportedException("Feature not supported");
/*      */   }
/*      */ 
/*      */   public void setByte(String paramString, byte paramByte)
/*      */     throws SQLException
/*      */   {
/* 3288 */     throw new SQLFeatureNotSupportedException("Feature not supported");
/*      */   }
/*      */ 
/*      */   public void setShort(String paramString, short paramShort)
/*      */     throws SQLException
/*      */   {
/* 3308 */     throw new SQLFeatureNotSupportedException("Feature not supported");
/*      */   }
/*      */ 
/*      */   public void setInt(String paramString, int paramInt)
/*      */     throws SQLException
/*      */   {
/* 3327 */     throw new SQLFeatureNotSupportedException("Feature not supported");
/*      */   }
/*      */ 
/*      */   public void setLong(String paramString, long paramLong)
/*      */     throws SQLException
/*      */   {
/* 3346 */     throw new SQLFeatureNotSupportedException("Feature not supported");
/*      */   }
/*      */ 
/*      */   public void setFloat(String paramString, float paramFloat)
/*      */     throws SQLException
/*      */   {
/* 3365 */     throw new SQLFeatureNotSupportedException("Feature not supported");
/*      */   }
/*      */ 
/*      */   public void setDouble(String paramString, double paramDouble)
/*      */     throws SQLException
/*      */   {
/* 3384 */     throw new SQLFeatureNotSupportedException("Feature not supported");
/*      */   }
/*      */ 
/*      */   public void setBigDecimal(String paramString, BigDecimal paramBigDecimal)
/*      */     throws SQLException
/*      */   {
/* 3405 */     throw new SQLFeatureNotSupportedException("Feature not supported");
/*      */   }
/*      */ 
/*      */   public void setString(String paramString1, String paramString2)
/*      */     throws SQLException
/*      */   {
/* 3428 */     throw new SQLFeatureNotSupportedException("Feature not supported");
/*      */   }
/*      */ 
/*      */   public void setBytes(String paramString, byte[] paramArrayOfByte)
/*      */     throws SQLException
/*      */   {
/* 3450 */     throw new SQLFeatureNotSupportedException("Feature not supported");
/*      */   }
/*      */ 
/*      */   public void setTimestamp(String paramString, Timestamp paramTimestamp)
/*      */     throws SQLException
/*      */   {
/* 3472 */     throw new SQLFeatureNotSupportedException("Feature not supported");
/*      */   }
/*      */ 
/*      */   public void setAsciiStream(String paramString, InputStream paramInputStream, int paramInt)
/*      */     throws SQLException
/*      */   {
/* 3501 */     throw new SQLFeatureNotSupportedException("Feature not supported");
/*      */   }
/*      */ 
/*      */   public void setBinaryStream(String paramString, InputStream paramInputStream, int paramInt)
/*      */     throws SQLException
/*      */   {
/* 3528 */     throw new SQLFeatureNotSupportedException("Feature not supported");
/*      */   }
/*      */ 
/*      */   public void setCharacterStream(String paramString, Reader paramReader, int paramInt)
/*      */     throws SQLException
/*      */   {
/* 3558 */     throw new SQLFeatureNotSupportedException("Feature not supported");
/*      */   }
/*      */ 
/*      */   public void setAsciiStream(String paramString, InputStream paramInputStream)
/*      */     throws SQLException
/*      */   {
/* 3586 */     throw new SQLFeatureNotSupportedException("Feature not supported");
/*      */   }
/*      */ 
/*      */   public void setBinaryStream(String paramString, InputStream paramInputStream)
/*      */     throws SQLException
/*      */   {
/* 3613 */     throw new SQLFeatureNotSupportedException("Feature not supported");
/*      */   }
/*      */ 
/*      */   public void setCharacterStream(String paramString, Reader paramReader)
/*      */     throws SQLException
/*      */   {
/* 3644 */     throw new SQLFeatureNotSupportedException("Feature not supported");
/*      */   }
/*      */ 
/*      */   public void setNCharacterStream(int paramInt, Reader paramReader)
/*      */     throws SQLException
/*      */   {
/* 3672 */     throw new SQLFeatureNotSupportedException("Feature not supported");
/*      */   }
/*      */ 
/*      */   public void setObject(String paramString, Object paramObject, int paramInt1, int paramInt2)
/*      */     throws SQLException
/*      */   {
/* 3720 */     throw new SQLFeatureNotSupportedException("Feature not supported");
/*      */   }
/*      */ 
/*      */   public void setObject(String paramString, Object paramObject, int paramInt)
/*      */     throws SQLException
/*      */   {
/* 3748 */     throw new SQLFeatureNotSupportedException("Feature not supported");
/*      */   }
/*      */ 
/*      */   public void setObject(String paramString, Object paramObject)
/*      */     throws SQLException
/*      */   {
/* 3789 */     throw new SQLFeatureNotSupportedException("Feature not supported");
/*      */   }
/*      */ 
/*      */   public void setBlob(int paramInt, InputStream paramInputStream, long paramLong)
/*      */     throws SQLException
/*      */   {
/* 3820 */     throw new SQLFeatureNotSupportedException("Feature not supported");
/*      */   }
/*      */ 
/*      */   public void setBlob(int paramInt, InputStream paramInputStream)
/*      */     throws SQLException
/*      */   {
/* 3850 */     throw new SQLFeatureNotSupportedException("Feature not supported");
/*      */   }
/*      */ 
/*      */   public void setBlob(String paramString, InputStream paramInputStream, long paramLong)
/*      */     throws SQLException
/*      */   {
/* 3882 */     throw new SQLFeatureNotSupportedException("Feature not supported");
/*      */   }
/*      */ 
/*      */   public void setBlob(String paramString, Blob paramBlob)
/*      */     throws SQLException
/*      */   {
/* 3900 */     throw new SQLFeatureNotSupportedException("Feature not supported");
/*      */   }
/*      */ 
/*      */   public void setBlob(String paramString, InputStream paramInputStream)
/*      */     throws SQLException
/*      */   {
/* 3927 */     throw new SQLFeatureNotSupportedException("Feature not supported");
/*      */   }
/*      */ 
/*      */   public void setClob(int paramInt, Reader paramReader, long paramLong)
/*      */     throws SQLException
/*      */   {
/* 3952 */     throw new SQLFeatureNotSupportedException("Feature not supported");
/*      */   }
/*      */ 
/*      */   public void setClob(int paramInt, Reader paramReader)
/*      */     throws SQLException
/*      */   {
/* 3979 */     throw new SQLFeatureNotSupportedException("Feature not supported");
/*      */   }
/*      */ 
/*      */   public void setClob(String paramString, Reader paramReader, long paramLong)
/*      */     throws SQLException
/*      */   {
/* 4006 */     throw new SQLFeatureNotSupportedException("Feature not supported");
/*      */   }
/*      */ 
/*      */   public void setClob(String paramString, Clob paramClob)
/*      */     throws SQLException
/*      */   {
/* 4024 */     throw new SQLFeatureNotSupportedException("Feature not supported");
/*      */   }
/*      */ 
/*      */   public void setClob(String paramString, Reader paramReader)
/*      */     throws SQLException
/*      */   {
/* 4050 */     throw new SQLFeatureNotSupportedException("Feature not supported");
/*      */   }
/*      */ 
/*      */   public void setDate(String paramString, Date paramDate)
/*      */     throws SQLException
/*      */   {
/* 4072 */     throw new SQLFeatureNotSupportedException("Feature not supported");
/*      */   }
/*      */ 
/*      */   public void setDate(String paramString, Date paramDate, Calendar paramCalendar)
/*      */     throws SQLException
/*      */   {
/* 4099 */     throw new SQLFeatureNotSupportedException("Feature not supported");
/*      */   }
/*      */ 
/*      */   public void setTime(String paramString, Time paramTime)
/*      */     throws SQLException
/*      */   {
/* 4119 */     throw new SQLFeatureNotSupportedException("Feature not supported");
/*      */   }
/*      */ 
/*      */   public void setTime(String paramString, Time paramTime, Calendar paramCalendar)
/*      */     throws SQLException
/*      */   {
/* 4146 */     throw new SQLFeatureNotSupportedException("Feature not supported");
/*      */   }
/*      */ 
/*      */   public void setTimestamp(String paramString, Timestamp paramTimestamp, Calendar paramCalendar)
/*      */     throws SQLException
/*      */   {
/* 4173 */     throw new SQLFeatureNotSupportedException("Feature not supported");
/*      */   }
/*      */ 
/*      */   public void setSQLXML(int paramInt, SQLXML paramSQLXML)
/*      */     throws SQLException
/*      */   {
/* 4193 */     throw new SQLFeatureNotSupportedException("Feature not supported");
/*      */   }
/*      */ 
/*      */   public void setSQLXML(String paramString, SQLXML paramSQLXML)
/*      */     throws SQLException
/*      */   {
/* 4213 */     throw new SQLFeatureNotSupportedException("Feature not supported");
/*      */   }
/*      */ 
/*      */   public void setRowId(int paramInt, RowId paramRowId)
/*      */     throws SQLException
/*      */   {
/* 4229 */     throw new SQLFeatureNotSupportedException("Feature not supported");
/*      */   }
/*      */ 
/*      */   public void setRowId(String paramString, RowId paramRowId)
/*      */     throws SQLException
/*      */   {
/* 4244 */     throw new SQLFeatureNotSupportedException("Feature not supported");
/*      */   }
/*      */ 
/*      */   public void setNString(int paramInt, String paramString)
/*      */     throws SQLException
/*      */   {
/* 4263 */     throw new SQLFeatureNotSupportedException("Feature not supported");
/*      */   }
/*      */ 
/*      */   public void setNString(String paramString1, String paramString2)
/*      */     throws SQLException
/*      */   {
/* 4280 */     throw new SQLFeatureNotSupportedException("Feature not supported");
/*      */   }
/*      */ 
/*      */   public void setNCharacterStream(int paramInt, Reader paramReader, long paramLong)
/*      */     throws SQLException
/*      */   {
/* 4298 */     throw new SQLFeatureNotSupportedException("Feature not supported");
/*      */   }
/*      */ 
/*      */   public void setNCharacterStream(String paramString, Reader paramReader, long paramLong)
/*      */     throws SQLException
/*      */   {
/* 4317 */     throw new SQLFeatureNotSupportedException("Feature not supported");
/*      */   }
/*      */ 
/*      */   public void setNCharacterStream(String paramString, Reader paramReader)
/*      */     throws SQLException
/*      */   {
/* 4344 */     throw new SQLFeatureNotSupportedException("Feature not supported");
/*      */   }
/*      */ 
/*      */   public void setNClob(String paramString, NClob paramNClob)
/*      */     throws SQLException
/*      */   {
/* 4360 */     throw new SQLFeatureNotSupportedException("Feature not supported");
/*      */   }
/*      */ 
/*      */   public void setNClob(String paramString, Reader paramReader, long paramLong)
/*      */     throws SQLException
/*      */   {
/* 4389 */     throw new SQLFeatureNotSupportedException("Feature not supported");
/*      */   }
/*      */ 
/*      */   public void setNClob(String paramString, Reader paramReader)
/*      */     throws SQLException
/*      */   {
/* 4416 */     throw new SQLFeatureNotSupportedException("Feature not supported");
/*      */   }
/*      */ 
/*      */   public void setNClob(int paramInt, Reader paramReader, long paramLong)
/*      */     throws SQLException
/*      */   {
/* 4444 */     throw new SQLFeatureNotSupportedException("Feature not supported");
/*      */   }
/*      */ 
/*      */   public void setNClob(int paramInt, NClob paramNClob)
/*      */     throws SQLException
/*      */   {
/* 4459 */     throw new SQLFeatureNotSupportedException("Feature not supported");
/*      */   }
/*      */ 
/*      */   public void setNClob(int paramInt, Reader paramReader)
/*      */     throws SQLException
/*      */   {
/* 4488 */     throw new SQLFeatureNotSupportedException("Feature not supported");
/*      */   }
/*      */ 
/*      */   public void setURL(int paramInt, URL paramURL)
/*      */     throws SQLException
/*      */   {
/* 4505 */     throw new SQLFeatureNotSupportedException("Feature not supported");
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.sql.rowset.BaseRowSet
 * JD-Core Version:    0.6.2
 */