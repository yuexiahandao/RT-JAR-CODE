/*      */ package com.sun.rowset;
/*      */ 
/*      */ import java.beans.PropertyChangeSupport;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.PrintStream;
/*      */ import java.io.Reader;
/*      */ import java.math.BigDecimal;
/*      */ import java.net.URL;
/*      */ import java.sql.Array;
/*      */ import java.sql.Blob;
/*      */ import java.sql.Clob;
/*      */ import java.sql.Connection;
/*      */ import java.sql.DatabaseMetaData;
/*      */ import java.sql.Date;
/*      */ import java.sql.DriverManager;
/*      */ import java.sql.NClob;
/*      */ import java.sql.ParameterMetaData;
/*      */ import java.sql.PreparedStatement;
/*      */ import java.sql.Ref;
/*      */ import java.sql.ResultSet;
/*      */ import java.sql.ResultSetMetaData;
/*      */ import java.sql.RowId;
/*      */ import java.sql.SQLException;
/*      */ import java.sql.SQLFeatureNotSupportedException;
/*      */ import java.sql.SQLWarning;
/*      */ import java.sql.SQLXML;
/*      */ import java.sql.Savepoint;
/*      */ import java.sql.Statement;
/*      */ import java.sql.Time;
/*      */ import java.sql.Timestamp;
/*      */ import java.util.Calendar;
/*      */ import java.util.Map;
/*      */ import java.util.Vector;
/*      */ import javax.naming.Context;
/*      */ import javax.naming.InitialContext;
/*      */ import javax.naming.NamingException;
/*      */ import javax.sql.DataSource;
/*      */ import javax.sql.RowSetMetaData;
/*      */ import javax.sql.rowset.BaseRowSet;
/*      */ import javax.sql.rowset.JdbcRowSet;
/*      */ import javax.sql.rowset.Joinable;
/*      */ import javax.sql.rowset.RowSetMetaDataImpl;
/*      */ import javax.sql.rowset.RowSetWarning;
/*      */ 
/*      */ public class JdbcRowSetImpl extends BaseRowSet
/*      */   implements JdbcRowSet, Joinable
/*      */ {
/*      */   private Connection conn;
/*      */   private PreparedStatement ps;
/*      */   private ResultSet rs;
/*      */   private RowSetMetaDataImpl rowsMD;
/*      */   private ResultSetMetaData resMD;
/*      */   private PropertyChangeSupport propertyChangeSupport;
/*      */   private Vector<Integer> iMatchColumns;
/*      */   private Vector<String> strMatchColumns;
/*      */   protected transient JdbcRowSetResourceBundle resBundle;
/*      */   static final long serialVersionUID = -3591946023893483003L;
/*      */ 
/*      */   public JdbcRowSetImpl()
/*      */   {
/*  138 */     this.conn = null;
/*  139 */     this.ps = null;
/*  140 */     this.rs = null;
/*      */     try
/*      */     {
/*  143 */       this.resBundle = JdbcRowSetResourceBundle.getJdbcRowSetResourceBundle();
/*      */     } catch (IOException localIOException) {
/*  145 */       throw new RuntimeException(localIOException);
/*      */     }
/*      */ 
/*  148 */     this.propertyChangeSupport = new PropertyChangeSupport(this);
/*      */ 
/*  150 */     initParams();
/*      */     try
/*      */     {
/*  155 */       setShowDeleted(false);
/*      */     } catch (SQLException localSQLException1) {
/*  157 */       System.err.println(this.resBundle.handleGetObject("jdbcrowsetimpl.setshowdeleted").toString() + localSQLException1.getLocalizedMessage());
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/*  162 */       setQueryTimeout(0);
/*      */     } catch (SQLException localSQLException2) {
/*  164 */       System.err.println(this.resBundle.handleGetObject("jdbcrowsetimpl.setquerytimeout").toString() + localSQLException2.getLocalizedMessage());
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/*  169 */       setMaxRows(0);
/*      */     } catch (SQLException localSQLException3) {
/*  171 */       System.err.println(this.resBundle.handleGetObject("jdbcrowsetimpl.setmaxrows").toString() + localSQLException3.getLocalizedMessage());
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/*  176 */       setMaxFieldSize(0);
/*      */     } catch (SQLException localSQLException4) {
/*  178 */       System.err.println(this.resBundle.handleGetObject("jdbcrowsetimpl.setmaxfieldsize").toString() + localSQLException4.getLocalizedMessage());
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/*  183 */       setEscapeProcessing(true);
/*      */     } catch (SQLException localSQLException5) {
/*  185 */       System.err.println(this.resBundle.handleGetObject("jdbcrowsetimpl.setescapeprocessing").toString() + localSQLException5.getLocalizedMessage());
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/*  190 */       setConcurrency(1008);
/*      */     } catch (SQLException localSQLException6) {
/*  192 */       System.err.println(this.resBundle.handleGetObject("jdbcrowsetimpl.setconcurrency").toString() + localSQLException6.getLocalizedMessage());
/*      */     }
/*      */ 
/*  196 */     setTypeMap(null);
/*      */     try
/*      */     {
/*  199 */       setType(1004);
/*      */     } catch (SQLException localSQLException7) {
/*  201 */       System.err.println(this.resBundle.handleGetObject("jdbcrowsetimpl.settype").toString() + localSQLException7.getLocalizedMessage());
/*      */     }
/*      */ 
/*  205 */     setReadOnly(true);
/*      */     try
/*      */     {
/*  208 */       setTransactionIsolation(2);
/*      */     } catch (SQLException localSQLException8) {
/*  210 */       System.err.println(this.resBundle.handleGetObject("jdbcrowsetimpl.settransactionisolation").toString() + localSQLException8.getLocalizedMessage());
/*      */     }
/*      */ 
/*  216 */     this.iMatchColumns = new Vector(10);
/*  217 */     for (int i = 0; i < 10; i++) {
/*  218 */       this.iMatchColumns.add(i, Integer.valueOf(-1));
/*      */     }
/*      */ 
/*  221 */     this.strMatchColumns = new Vector(10);
/*  222 */     for (i = 0; i < 10; i++)
/*  223 */       this.strMatchColumns.add(i, null);
/*      */   }
/*      */ 
/*      */   public JdbcRowSetImpl(Connection paramConnection)
/*      */     throws SQLException
/*      */   {
/*  261 */     this.conn = paramConnection;
/*  262 */     this.ps = null;
/*  263 */     this.rs = null;
/*      */     try
/*      */     {
/*  266 */       this.resBundle = JdbcRowSetResourceBundle.getJdbcRowSetResourceBundle();
/*      */     } catch (IOException localIOException) {
/*  268 */       throw new RuntimeException(localIOException);
/*      */     }
/*      */ 
/*  271 */     this.propertyChangeSupport = new PropertyChangeSupport(this);
/*      */ 
/*  273 */     initParams();
/*      */ 
/*  275 */     setShowDeleted(false);
/*  276 */     setQueryTimeout(0);
/*  277 */     setMaxRows(0);
/*  278 */     setMaxFieldSize(0);
/*      */ 
/*  280 */     setParams();
/*      */ 
/*  282 */     setReadOnly(true);
/*  283 */     setTransactionIsolation(2);
/*  284 */     setEscapeProcessing(true);
/*  285 */     setTypeMap(null);
/*      */ 
/*  289 */     this.iMatchColumns = new Vector(10);
/*  290 */     for (int i = 0; i < 10; i++) {
/*  291 */       this.iMatchColumns.add(i, Integer.valueOf(-1));
/*      */     }
/*      */ 
/*  294 */     this.strMatchColumns = new Vector(10);
/*  295 */     for (i = 0; i < 10; i++)
/*  296 */       this.strMatchColumns.add(i, null);
/*      */   }
/*      */ 
/*      */   public JdbcRowSetImpl(String paramString1, String paramString2, String paramString3)
/*      */     throws SQLException
/*      */   {
/*  336 */     this.conn = null;
/*  337 */     this.ps = null;
/*  338 */     this.rs = null;
/*      */     try
/*      */     {
/*  341 */       this.resBundle = JdbcRowSetResourceBundle.getJdbcRowSetResourceBundle();
/*      */     } catch (IOException localIOException) {
/*  343 */       throw new RuntimeException(localIOException);
/*      */     }
/*      */ 
/*  346 */     this.propertyChangeSupport = new PropertyChangeSupport(this);
/*      */ 
/*  348 */     initParams();
/*      */ 
/*  353 */     setUsername(paramString2);
/*  354 */     setPassword(paramString3);
/*  355 */     setUrl(paramString1);
/*      */ 
/*  358 */     setShowDeleted(false);
/*  359 */     setQueryTimeout(0);
/*  360 */     setMaxRows(0);
/*  361 */     setMaxFieldSize(0);
/*      */ 
/*  366 */     this.conn = connect();
/*  367 */     setParams();
/*      */ 
/*  369 */     setReadOnly(true);
/*  370 */     setTransactionIsolation(2);
/*  371 */     setEscapeProcessing(true);
/*  372 */     setTypeMap(null);
/*      */ 
/*  376 */     this.iMatchColumns = new Vector(10);
/*  377 */     for (int i = 0; i < 10; i++) {
/*  378 */       this.iMatchColumns.add(i, Integer.valueOf(-1));
/*      */     }
/*      */ 
/*  381 */     this.strMatchColumns = new Vector(10);
/*  382 */     for (i = 0; i < 10; i++)
/*  383 */       this.strMatchColumns.add(i, null);
/*      */   }
/*      */ 
/*      */   public JdbcRowSetImpl(ResultSet paramResultSet)
/*      */     throws SQLException
/*      */   {
/*  426 */     this.conn = null;
/*      */ 
/*  428 */     this.ps = null;
/*      */ 
/*  430 */     this.rs = paramResultSet;
/*      */     try
/*      */     {
/*  433 */       this.resBundle = JdbcRowSetResourceBundle.getJdbcRowSetResourceBundle();
/*      */     } catch (IOException localIOException) {
/*  435 */       throw new RuntimeException(localIOException);
/*      */     }
/*      */ 
/*  438 */     this.propertyChangeSupport = new PropertyChangeSupport(this);
/*      */ 
/*  440 */     initParams();
/*      */ 
/*  443 */     setShowDeleted(false);
/*  444 */     setQueryTimeout(0);
/*  445 */     setMaxRows(0);
/*  446 */     setMaxFieldSize(0);
/*      */ 
/*  448 */     setParams();
/*      */ 
/*  450 */     setReadOnly(true);
/*  451 */     setTransactionIsolation(2);
/*  452 */     setEscapeProcessing(true);
/*  453 */     setTypeMap(null);
/*      */ 
/*  458 */     this.resMD = this.rs.getMetaData();
/*      */ 
/*  460 */     this.rowsMD = new RowSetMetaDataImpl();
/*      */ 
/*  462 */     initMetaData(this.rowsMD, this.resMD);
/*      */ 
/*  466 */     this.iMatchColumns = new Vector(10);
/*  467 */     for (int i = 0; i < 10; i++) {
/*  468 */       this.iMatchColumns.add(i, Integer.valueOf(-1));
/*      */     }
/*      */ 
/*  471 */     this.strMatchColumns = new Vector(10);
/*  472 */     for (i = 0; i < 10; i++)
/*  473 */       this.strMatchColumns.add(i, null);
/*      */   }
/*      */ 
/*      */   protected void initMetaData(RowSetMetaData paramRowSetMetaData, ResultSetMetaData paramResultSetMetaData)
/*      */     throws SQLException
/*      */   {
/*  489 */     int i = paramResultSetMetaData.getColumnCount();
/*      */ 
/*  491 */     paramRowSetMetaData.setColumnCount(i);
/*  492 */     for (int j = 1; j <= i; j++) {
/*  493 */       paramRowSetMetaData.setAutoIncrement(j, paramResultSetMetaData.isAutoIncrement(j));
/*  494 */       paramRowSetMetaData.setCaseSensitive(j, paramResultSetMetaData.isCaseSensitive(j));
/*  495 */       paramRowSetMetaData.setCurrency(j, paramResultSetMetaData.isCurrency(j));
/*  496 */       paramRowSetMetaData.setNullable(j, paramResultSetMetaData.isNullable(j));
/*  497 */       paramRowSetMetaData.setSigned(j, paramResultSetMetaData.isSigned(j));
/*  498 */       paramRowSetMetaData.setSearchable(j, paramResultSetMetaData.isSearchable(j));
/*  499 */       paramRowSetMetaData.setColumnDisplaySize(j, paramResultSetMetaData.getColumnDisplaySize(j));
/*  500 */       paramRowSetMetaData.setColumnLabel(j, paramResultSetMetaData.getColumnLabel(j));
/*  501 */       paramRowSetMetaData.setColumnName(j, paramResultSetMetaData.getColumnName(j));
/*  502 */       paramRowSetMetaData.setSchemaName(j, paramResultSetMetaData.getSchemaName(j));
/*  503 */       paramRowSetMetaData.setPrecision(j, paramResultSetMetaData.getPrecision(j));
/*  504 */       paramRowSetMetaData.setScale(j, paramResultSetMetaData.getScale(j));
/*  505 */       paramRowSetMetaData.setTableName(j, paramResultSetMetaData.getTableName(j));
/*  506 */       paramRowSetMetaData.setCatalogName(j, paramResultSetMetaData.getCatalogName(j));
/*  507 */       paramRowSetMetaData.setColumnType(j, paramResultSetMetaData.getColumnType(j));
/*  508 */       paramRowSetMetaData.setColumnTypeName(j, paramResultSetMetaData.getColumnTypeName(j));
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void checkState()
/*      */     throws SQLException
/*      */   {
/*  519 */     if ((this.conn == null) && (this.ps == null) && (this.rs == null))
/*  520 */       throw new SQLException(this.resBundle.handleGetObject("jdbcrowsetimpl.invalstate").toString());
/*      */   }
/*      */ 
/*      */   public void execute()
/*      */     throws SQLException
/*      */   {
/*  571 */     prepare();
/*      */ 
/*  574 */     setProperties(this.ps);
/*      */ 
/*  578 */     decodeParams(getParams(), this.ps);
/*      */ 
/*  582 */     this.rs = this.ps.executeQuery();
/*      */ 
/*  586 */     notifyRowSetChanged();
/*      */   }
/*      */ 
/*      */   protected void setProperties(PreparedStatement paramPreparedStatement)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/*  594 */       paramPreparedStatement.setEscapeProcessing(getEscapeProcessing());
/*      */     } catch (SQLException localSQLException1) {
/*  596 */       System.err.println(this.resBundle.handleGetObject("jdbcrowsetimpl.setescapeprocessing").toString() + localSQLException1.getLocalizedMessage());
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/*  601 */       paramPreparedStatement.setMaxFieldSize(getMaxFieldSize());
/*      */     } catch (SQLException localSQLException2) {
/*  603 */       System.err.println(this.resBundle.handleGetObject("jdbcrowsetimpl.setmaxfieldsize").toString() + localSQLException2.getLocalizedMessage());
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/*  608 */       paramPreparedStatement.setMaxRows(getMaxRows());
/*      */     } catch (SQLException localSQLException3) {
/*  610 */       System.err.println(this.resBundle.handleGetObject("jdbcrowsetimpl.setmaxrows").toString() + localSQLException3.getLocalizedMessage());
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/*  615 */       paramPreparedStatement.setQueryTimeout(getQueryTimeout());
/*      */     } catch (SQLException localSQLException4) {
/*  617 */       System.err.println(this.resBundle.handleGetObject("jdbcrowsetimpl.setquerytimeout").toString() + localSQLException4.getLocalizedMessage());
/*      */     }
/*      */   }
/*      */ 
/*      */   protected Connection connect()
/*      */     throws SQLException
/*      */   {
/*  635 */     if (this.conn != null) {
/*  636 */       return this.conn;
/*      */     }
/*  638 */     if (getDataSourceName() != null)
/*      */     {
/*      */       try
/*      */       {
/*  642 */         InitialContext localInitialContext = new InitialContext();
/*  643 */         DataSource localDataSource = (DataSource)localInitialContext.lookup(getDataSourceName());
/*      */ 
/*  647 */         if ((getUsername() != null) && (!getUsername().equals(""))) {
/*  648 */           return localDataSource.getConnection(getUsername(), getPassword());
/*      */         }
/*  650 */         return localDataSource.getConnection();
/*      */       }
/*      */       catch (NamingException localNamingException)
/*      */       {
/*  654 */         throw new SQLException(this.resBundle.handleGetObject("jdbcrowsetimpl.connect").toString());
/*      */       }
/*      */     }
/*  657 */     if (getUrl() != null)
/*      */     {
/*  662 */       return DriverManager.getConnection(getUrl(), getUsername(), getPassword());
/*      */     }
/*      */ 
/*  666 */     return null;
/*      */   }
/*      */ 
/*      */   protected PreparedStatement prepare()
/*      */     throws SQLException
/*      */   {
/*  674 */     this.conn = connect();
/*      */     try
/*      */     {
/*  678 */       Map localMap = getTypeMap();
/*  679 */       if (localMap != null) {
/*  680 */         this.conn.setTypeMap(localMap);
/*      */       }
/*  682 */       this.ps = this.conn.prepareStatement(getCommand(), 1004, 1008);
/*      */     } catch (SQLException localSQLException) {
/*  684 */       System.err.println(this.resBundle.handleGetObject("jdbcrowsetimpl.prepare").toString() + localSQLException.getLocalizedMessage());
/*      */ 
/*  687 */       if (this.ps != null)
/*  688 */         this.ps.close();
/*  689 */       if (this.conn != null) {
/*  690 */         this.conn.close();
/*      */       }
/*  692 */       throw new SQLException(localSQLException.getMessage());
/*      */     }
/*      */ 
/*  695 */     return this.ps;
/*      */   }
/*      */ 
/*      */   private void decodeParams(Object[] paramArrayOfObject, PreparedStatement paramPreparedStatement)
/*      */     throws SQLException
/*      */   {
/*  709 */     Object[] arrayOfObject = null;
/*      */ 
/*  711 */     for (int i = 0; i < paramArrayOfObject.length; i++)
/*  712 */       if ((paramArrayOfObject[i] instanceof Object[])) {
/*  713 */         arrayOfObject = (Object[])paramArrayOfObject[i];
/*      */ 
/*  715 */         if (arrayOfObject.length == 2) {
/*  716 */           if (arrayOfObject[0] == null) {
/*  717 */             paramPreparedStatement.setNull(i + 1, ((Integer)arrayOfObject[1]).intValue());
/*      */           }
/*  721 */           else if (((arrayOfObject[0] instanceof Date)) || ((arrayOfObject[0] instanceof Time)) || ((arrayOfObject[0] instanceof Timestamp)))
/*      */           {
/*  724 */             System.err.println(this.resBundle.handleGetObject("jdbcrowsetimpl.detecteddate"));
/*  725 */             if ((arrayOfObject[1] instanceof Calendar)) {
/*  726 */               System.err.println(this.resBundle.handleGetObject("jdbcrowsetimpl.detectedcalendar"));
/*  727 */               paramPreparedStatement.setDate(i + 1, (Date)arrayOfObject[0], (Calendar)arrayOfObject[1]);
/*      */             }
/*      */             else
/*      */             {
/*  732 */               throw new SQLException(this.resBundle.handleGetObject("jdbcrowsetimpl.paramtype").toString());
/*      */             }
/*      */ 
/*      */           }
/*  736 */           else if ((arrayOfObject[0] instanceof Reader)) {
/*  737 */             paramPreparedStatement.setCharacterStream(i + 1, (Reader)arrayOfObject[0], ((Integer)arrayOfObject[1]).intValue());
/*      */           }
/*  745 */           else if ((arrayOfObject[1] instanceof Integer)) {
/*  746 */             paramPreparedStatement.setObject(i + 1, arrayOfObject[0], ((Integer)arrayOfObject[1]).intValue());
/*      */           }
/*      */ 
/*      */         }
/*  750 */         else if (arrayOfObject.length == 3)
/*      */         {
/*  752 */           if (arrayOfObject[0] == null) {
/*  753 */             paramPreparedStatement.setNull(i + 1, ((Integer)arrayOfObject[1]).intValue(), (String)arrayOfObject[2]);
/*      */           }
/*      */           else
/*      */           {
/*  758 */             if ((arrayOfObject[0] instanceof InputStream)) {
/*  759 */               switch (((Integer)arrayOfObject[2]).intValue()) {
/*      */               case 0:
/*  761 */                 paramPreparedStatement.setUnicodeStream(i + 1, (InputStream)arrayOfObject[0], ((Integer)arrayOfObject[1]).intValue());
/*      */               case 1:
/*  765 */                 paramPreparedStatement.setBinaryStream(i + 1, (InputStream)arrayOfObject[0], ((Integer)arrayOfObject[1]).intValue());
/*      */               case 2:
/*  769 */                 paramPreparedStatement.setAsciiStream(i + 1, (InputStream)arrayOfObject[0], ((Integer)arrayOfObject[1]).intValue());
/*      */               }
/*      */ 
/*  773 */               throw new SQLException(this.resBundle.handleGetObject("jdbcrowsetimpl.paramtype").toString());
/*      */             }
/*      */ 
/*  781 */             if (((arrayOfObject[1] instanceof Integer)) && ((arrayOfObject[2] instanceof Integer))) {
/*  782 */               paramPreparedStatement.setObject(i + 1, arrayOfObject[0], ((Integer)arrayOfObject[1]).intValue(), ((Integer)arrayOfObject[2]).intValue());
/*      */             }
/*      */             else
/*      */             {
/*  787 */               throw new SQLException(this.resBundle.handleGetObject("jdbcrowsetimpl.paramtype").toString());
/*      */             }
/*      */           }
/*      */         }
/*  791 */         else paramPreparedStatement.setObject(i + 1, paramArrayOfObject[i]);
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*  796 */         paramPreparedStatement.setObject(i + 1, paramArrayOfObject[i]);
/*      */       }
/*      */   }
/*      */ 
/*      */   public boolean next()
/*      */     throws SQLException
/*      */   {
/*  822 */     checkState();
/*      */ 
/*  824 */     boolean bool = this.rs.next();
/*  825 */     notifyCursorMoved();
/*  826 */     return bool;
/*      */   }
/*      */ 
/*      */   public void close()
/*      */     throws SQLException
/*      */   {
/*  845 */     if (this.rs != null)
/*  846 */       this.rs.close();
/*  847 */     if (this.ps != null)
/*  848 */       this.ps.close();
/*  849 */     if (this.conn != null)
/*  850 */       this.conn.close();
/*      */   }
/*      */ 
/*      */   public boolean wasNull()
/*      */     throws SQLException
/*      */   {
/*  868 */     checkState();
/*      */ 
/*  870 */     return this.rs.wasNull();
/*      */   }
/*      */ 
/*      */   public String getString(int paramInt)
/*      */     throws SQLException
/*      */   {
/*  890 */     checkState();
/*      */ 
/*  892 */     return this.rs.getString(paramInt);
/*      */   }
/*      */ 
/*      */   public boolean getBoolean(int paramInt)
/*      */     throws SQLException
/*      */   {
/*  908 */     checkState();
/*      */ 
/*  910 */     return this.rs.getBoolean(paramInt);
/*      */   }
/*      */ 
/*      */   public byte getByte(int paramInt)
/*      */     throws SQLException
/*      */   {
/*  926 */     checkState();
/*      */ 
/*  928 */     return this.rs.getByte(paramInt);
/*      */   }
/*      */ 
/*      */   public short getShort(int paramInt)
/*      */     throws SQLException
/*      */   {
/*  944 */     checkState();
/*      */ 
/*  946 */     return this.rs.getShort(paramInt);
/*      */   }
/*      */ 
/*      */   public int getInt(int paramInt)
/*      */     throws SQLException
/*      */   {
/*  962 */     checkState();
/*      */ 
/*  964 */     return this.rs.getInt(paramInt);
/*      */   }
/*      */ 
/*      */   public long getLong(int paramInt)
/*      */     throws SQLException
/*      */   {
/*  980 */     checkState();
/*      */ 
/*  982 */     return this.rs.getLong(paramInt);
/*      */   }
/*      */ 
/*      */   public float getFloat(int paramInt)
/*      */     throws SQLException
/*      */   {
/*  998 */     checkState();
/*      */ 
/* 1000 */     return this.rs.getFloat(paramInt);
/*      */   }
/*      */ 
/*      */   public double getDouble(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 1016 */     checkState();
/*      */ 
/* 1018 */     return this.rs.getDouble(paramInt);
/*      */   }
/*      */ 
/*      */   /** @deprecated */
/*      */   public BigDecimal getBigDecimal(int paramInt1, int paramInt2)
/*      */     throws SQLException
/*      */   {
/* 1036 */     checkState();
/*      */ 
/* 1038 */     return this.rs.getBigDecimal(paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   public byte[] getBytes(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 1055 */     checkState();
/*      */ 
/* 1057 */     return this.rs.getBytes(paramInt);
/*      */   }
/*      */ 
/*      */   public Date getDate(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 1073 */     checkState();
/*      */ 
/* 1075 */     return this.rs.getDate(paramInt);
/*      */   }
/*      */ 
/*      */   public Time getTime(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 1091 */     checkState();
/*      */ 
/* 1093 */     return this.rs.getTime(paramInt);
/*      */   }
/*      */ 
/*      */   public Timestamp getTimestamp(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 1109 */     checkState();
/*      */ 
/* 1111 */     return this.rs.getTimestamp(paramInt);
/*      */   }
/*      */ 
/*      */   public InputStream getAsciiStream(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 1140 */     checkState();
/*      */ 
/* 1142 */     return this.rs.getAsciiStream(paramInt);
/*      */   }
/*      */ 
/*      */   /** @deprecated */
/*      */   public InputStream getUnicodeStream(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 1174 */     checkState();
/*      */ 
/* 1176 */     return this.rs.getUnicodeStream(paramInt);
/*      */   }
/*      */ 
/*      */   public InputStream getBinaryStream(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 1203 */     checkState();
/*      */ 
/* 1205 */     return this.rs.getBinaryStream(paramInt);
/*      */   }
/*      */ 
/*      */   public String getString(String paramString)
/*      */     throws SQLException
/*      */   {
/* 1226 */     return getString(findColumn(paramString));
/*      */   }
/*      */ 
/*      */   public boolean getBoolean(String paramString)
/*      */     throws SQLException
/*      */   {
/* 1242 */     return getBoolean(findColumn(paramString));
/*      */   }
/*      */ 
/*      */   public byte getByte(String paramString)
/*      */     throws SQLException
/*      */   {
/* 1258 */     return getByte(findColumn(paramString));
/*      */   }
/*      */ 
/*      */   public short getShort(String paramString)
/*      */     throws SQLException
/*      */   {
/* 1274 */     return getShort(findColumn(paramString));
/*      */   }
/*      */ 
/*      */   public int getInt(String paramString)
/*      */     throws SQLException
/*      */   {
/* 1290 */     return getInt(findColumn(paramString));
/*      */   }
/*      */ 
/*      */   public long getLong(String paramString)
/*      */     throws SQLException
/*      */   {
/* 1306 */     return getLong(findColumn(paramString));
/*      */   }
/*      */ 
/*      */   public float getFloat(String paramString)
/*      */     throws SQLException
/*      */   {
/* 1322 */     return getFloat(findColumn(paramString));
/*      */   }
/*      */ 
/*      */   public double getDouble(String paramString)
/*      */     throws SQLException
/*      */   {
/* 1338 */     return getDouble(findColumn(paramString));
/*      */   }
/*      */ 
/*      */   /** @deprecated */
/*      */   public BigDecimal getBigDecimal(String paramString, int paramInt)
/*      */     throws SQLException
/*      */   {
/* 1356 */     return getBigDecimal(findColumn(paramString), paramInt);
/*      */   }
/*      */ 
/*      */   public byte[] getBytes(String paramString)
/*      */     throws SQLException
/*      */   {
/* 1373 */     return getBytes(findColumn(paramString));
/*      */   }
/*      */ 
/*      */   public Date getDate(String paramString)
/*      */     throws SQLException
/*      */   {
/* 1389 */     return getDate(findColumn(paramString));
/*      */   }
/*      */ 
/*      */   public Time getTime(String paramString)
/*      */     throws SQLException
/*      */   {
/* 1406 */     return getTime(findColumn(paramString));
/*      */   }
/*      */ 
/*      */   public Timestamp getTimestamp(String paramString)
/*      */     throws SQLException
/*      */   {
/* 1422 */     return getTimestamp(findColumn(paramString));
/*      */   }
/*      */ 
/*      */   public InputStream getAsciiStream(String paramString)
/*      */     throws SQLException
/*      */   {
/* 1450 */     return getAsciiStream(findColumn(paramString));
/*      */   }
/*      */ 
/*      */   /** @deprecated */
/*      */   public InputStream getUnicodeStream(String paramString)
/*      */     throws SQLException
/*      */   {
/* 1481 */     return getUnicodeStream(findColumn(paramString));
/*      */   }
/*      */ 
/*      */   public InputStream getBinaryStream(String paramString)
/*      */     throws SQLException
/*      */   {
/* 1508 */     return getBinaryStream(findColumn(paramString));
/*      */   }
/*      */ 
/*      */   public SQLWarning getWarnings()
/*      */     throws SQLException
/*      */   {
/* 1538 */     checkState();
/*      */ 
/* 1540 */     return this.rs.getWarnings();
/*      */   }
/*      */ 
/*      */   public void clearWarnings()
/*      */     throws SQLException
/*      */   {
/* 1554 */     checkState();
/*      */ 
/* 1556 */     this.rs.clearWarnings();
/*      */   }
/*      */ 
/*      */   public String getCursorName()
/*      */     throws SQLException
/*      */   {
/* 1585 */     checkState();
/*      */ 
/* 1587 */     return this.rs.getCursorName();
/*      */   }
/*      */ 
/*      */   public ResultSetMetaData getMetaData()
/*      */     throws SQLException
/*      */   {
/* 1602 */     checkState();
/*      */     try
/*      */     {
/* 1611 */       checkState();
/*      */     } catch (SQLException localSQLException) {
/* 1613 */       prepare();
/*      */ 
/* 1615 */       return this.ps.getMetaData();
/*      */     }
/* 1617 */     return this.rs.getMetaData();
/*      */   }
/*      */ 
/*      */   public Object getObject(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 1648 */     checkState();
/*      */ 
/* 1650 */     return this.rs.getObject(paramInt);
/*      */   }
/*      */ 
/*      */   public Object getObject(String paramString)
/*      */     throws SQLException
/*      */   {
/* 1681 */     return getObject(findColumn(paramString));
/*      */   }
/*      */ 
/*      */   public int findColumn(String paramString)
/*      */     throws SQLException
/*      */   {
/* 1698 */     checkState();
/*      */ 
/* 1700 */     return this.rs.findColumn(paramString);
/*      */   }
/*      */ 
/*      */   public Reader getCharacterStream(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 1721 */     checkState();
/*      */ 
/* 1723 */     return this.rs.getCharacterStream(paramInt);
/*      */   }
/*      */ 
/*      */   public Reader getCharacterStream(String paramString)
/*      */     throws SQLException
/*      */   {
/* 1739 */     return getCharacterStream(findColumn(paramString));
/*      */   }
/*      */ 
/*      */   public BigDecimal getBigDecimal(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 1756 */     checkState();
/*      */ 
/* 1758 */     return this.rs.getBigDecimal(paramInt);
/*      */   }
/*      */ 
/*      */   public BigDecimal getBigDecimal(String paramString)
/*      */     throws SQLException
/*      */   {
/* 1775 */     return getBigDecimal(findColumn(paramString));
/*      */   }
/*      */ 
/*      */   public boolean isBeforeFirst()
/*      */     throws SQLException
/*      */   {
/* 1794 */     checkState();
/*      */ 
/* 1796 */     return this.rs.isBeforeFirst();
/*      */   }
/*      */ 
/*      */   public boolean isAfterLast()
/*      */     throws SQLException
/*      */   {
/* 1811 */     checkState();
/*      */ 
/* 1813 */     return this.rs.isAfterLast();
/*      */   }
/*      */ 
/*      */   public boolean isFirst()
/*      */     throws SQLException
/*      */   {
/* 1827 */     checkState();
/*      */ 
/* 1829 */     return this.rs.isFirst();
/*      */   }
/*      */ 
/*      */   public boolean isLast()
/*      */     throws SQLException
/*      */   {
/* 1848 */     checkState();
/*      */ 
/* 1850 */     return this.rs.isLast();
/*      */   }
/*      */ 
/*      */   public void beforeFirst()
/*      */     throws SQLException
/*      */   {
/* 1864 */     checkState();
/*      */ 
/* 1866 */     this.rs.beforeFirst();
/* 1867 */     notifyCursorMoved();
/*      */   }
/*      */ 
/*      */   public void afterLast()
/*      */     throws SQLException
/*      */   {
/* 1880 */     checkState();
/*      */ 
/* 1882 */     this.rs.afterLast();
/* 1883 */     notifyCursorMoved();
/*      */   }
/*      */ 
/*      */   public boolean first()
/*      */     throws SQLException
/*      */   {
/* 1898 */     checkState();
/*      */ 
/* 1900 */     boolean bool = this.rs.first();
/* 1901 */     notifyCursorMoved();
/* 1902 */     return bool;
/*      */   }
/*      */ 
/*      */   public boolean last()
/*      */     throws SQLException
/*      */   {
/* 1918 */     checkState();
/*      */ 
/* 1920 */     boolean bool = this.rs.last();
/* 1921 */     notifyCursorMoved();
/* 1922 */     return bool;
/*      */   }
/*      */ 
/*      */   public int getRow()
/*      */     throws SQLException
/*      */   {
/* 1935 */     checkState();
/*      */ 
/* 1937 */     return this.rs.getRow();
/*      */   }
/*      */ 
/*      */   public boolean absolute(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 1973 */     checkState();
/*      */ 
/* 1975 */     boolean bool = this.rs.absolute(paramInt);
/* 1976 */     notifyCursorMoved();
/* 1977 */     return bool;
/*      */   }
/*      */ 
/*      */   public boolean relative(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 2003 */     checkState();
/*      */ 
/* 2005 */     boolean bool = this.rs.relative(paramInt);
/* 2006 */     notifyCursorMoved();
/* 2007 */     return bool;
/*      */   }
/*      */ 
/*      */   public boolean previous()
/*      */     throws SQLException
/*      */   {
/* 2026 */     checkState();
/*      */ 
/* 2028 */     boolean bool = this.rs.previous();
/* 2029 */     notifyCursorMoved();
/* 2030 */     return bool;
/*      */   }
/*      */ 
/*      */   public void setFetchDirection(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 2049 */     checkState();
/*      */ 
/* 2051 */     this.rs.setFetchDirection(paramInt);
/*      */   }
/*      */ 
/*      */   public int getFetchDirection()
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/* 2066 */       checkState();
/*      */     } catch (SQLException localSQLException) {
/* 2068 */       super.getFetchDirection();
/*      */     }
/* 2070 */     return this.rs.getFetchDirection();
/*      */   }
/*      */ 
/*      */   public void setFetchSize(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 2091 */     checkState();
/*      */ 
/* 2093 */     this.rs.setFetchSize(paramInt);
/*      */   }
/*      */ 
/*      */   public int getType()
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/* 2108 */       checkState();
/*      */     } catch (SQLException localSQLException) {
/* 2110 */       return super.getType();
/*      */     }
/*      */ 
/* 2115 */     if (this.rs == null) {
/* 2116 */       return super.getType();
/*      */     }
/* 2118 */     int i = this.rs.getType();
/* 2119 */     return i;
/*      */   }
/*      */ 
/*      */   public int getConcurrency()
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/* 2138 */       checkState();
/*      */     } catch (SQLException localSQLException) {
/* 2140 */       super.getConcurrency();
/*      */     }
/* 2142 */     return this.rs.getConcurrency();
/*      */   }
/*      */ 
/*      */   public boolean rowUpdated()
/*      */     throws SQLException
/*      */   {
/* 2161 */     checkState();
/*      */ 
/* 2163 */     return this.rs.rowUpdated();
/*      */   }
/*      */ 
/*      */   public boolean rowInserted()
/*      */     throws SQLException
/*      */   {
/* 2180 */     checkState();
/*      */ 
/* 2182 */     return this.rs.rowInserted();
/*      */   }
/*      */ 
/*      */   public boolean rowDeleted()
/*      */     throws SQLException
/*      */   {
/* 2199 */     checkState();
/*      */ 
/* 2201 */     return this.rs.rowDeleted();
/*      */   }
/*      */ 
/*      */   public void updateNull(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 2218 */     checkState();
/*      */ 
/* 2222 */     checkTypeConcurrency();
/*      */ 
/* 2224 */     this.rs.updateNull(paramInt);
/*      */   }
/*      */ 
/*      */   public void updateBoolean(int paramInt, boolean paramBoolean)
/*      */     throws SQLException
/*      */   {
/* 2242 */     checkState();
/*      */ 
/* 2246 */     checkTypeConcurrency();
/*      */ 
/* 2248 */     this.rs.updateBoolean(paramInt, paramBoolean);
/*      */   }
/*      */ 
/*      */   public void updateByte(int paramInt, byte paramByte)
/*      */     throws SQLException
/*      */   {
/* 2267 */     checkState();
/*      */ 
/* 2271 */     checkTypeConcurrency();
/*      */ 
/* 2273 */     this.rs.updateByte(paramInt, paramByte);
/*      */   }
/*      */ 
/*      */   public void updateShort(int paramInt, short paramShort)
/*      */     throws SQLException
/*      */   {
/* 2291 */     checkState();
/*      */ 
/* 2295 */     checkTypeConcurrency();
/*      */ 
/* 2297 */     this.rs.updateShort(paramInt, paramShort);
/*      */   }
/*      */ 
/*      */   public void updateInt(int paramInt1, int paramInt2)
/*      */     throws SQLException
/*      */   {
/* 2314 */     checkState();
/*      */ 
/* 2318 */     checkTypeConcurrency();
/*      */ 
/* 2320 */     this.rs.updateInt(paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   public void updateLong(int paramInt, long paramLong)
/*      */     throws SQLException
/*      */   {
/* 2338 */     checkState();
/*      */ 
/* 2342 */     checkTypeConcurrency();
/*      */ 
/* 2344 */     this.rs.updateLong(paramInt, paramLong);
/*      */   }
/*      */ 
/*      */   public void updateFloat(int paramInt, float paramFloat)
/*      */     throws SQLException
/*      */   {
/* 2362 */     checkState();
/*      */ 
/* 2366 */     checkTypeConcurrency();
/*      */ 
/* 2368 */     this.rs.updateFloat(paramInt, paramFloat);
/*      */   }
/*      */ 
/*      */   public void updateDouble(int paramInt, double paramDouble)
/*      */     throws SQLException
/*      */   {
/* 2386 */     checkState();
/*      */ 
/* 2390 */     checkTypeConcurrency();
/*      */ 
/* 2392 */     this.rs.updateDouble(paramInt, paramDouble);
/*      */   }
/*      */ 
/*      */   public void updateBigDecimal(int paramInt, BigDecimal paramBigDecimal)
/*      */     throws SQLException
/*      */   {
/* 2411 */     checkState();
/*      */ 
/* 2415 */     checkTypeConcurrency();
/*      */ 
/* 2417 */     this.rs.updateBigDecimal(paramInt, paramBigDecimal);
/*      */   }
/*      */ 
/*      */   public void updateString(int paramInt, String paramString)
/*      */     throws SQLException
/*      */   {
/* 2435 */     checkState();
/*      */ 
/* 2439 */     checkTypeConcurrency();
/*      */ 
/* 2441 */     this.rs.updateString(paramInt, paramString);
/*      */   }
/*      */ 
/*      */   public void updateBytes(int paramInt, byte[] paramArrayOfByte)
/*      */     throws SQLException
/*      */   {
/* 2459 */     checkState();
/*      */ 
/* 2463 */     checkTypeConcurrency();
/*      */ 
/* 2465 */     this.rs.updateBytes(paramInt, paramArrayOfByte);
/*      */   }
/*      */ 
/*      */   public void updateDate(int paramInt, Date paramDate)
/*      */     throws SQLException
/*      */   {
/* 2483 */     checkState();
/*      */ 
/* 2487 */     checkTypeConcurrency();
/*      */ 
/* 2489 */     this.rs.updateDate(paramInt, paramDate);
/*      */   }
/*      */ 
/*      */   public void updateTime(int paramInt, Time paramTime)
/*      */     throws SQLException
/*      */   {
/* 2508 */     checkState();
/*      */ 
/* 2512 */     checkTypeConcurrency();
/*      */ 
/* 2514 */     this.rs.updateTime(paramInt, paramTime);
/*      */   }
/*      */ 
/*      */   public void updateTimestamp(int paramInt, Timestamp paramTimestamp)
/*      */     throws SQLException
/*      */   {
/* 2533 */     checkState();
/*      */ 
/* 2537 */     checkTypeConcurrency();
/*      */ 
/* 2539 */     this.rs.updateTimestamp(paramInt, paramTimestamp);
/*      */   }
/*      */ 
/*      */   public void updateAsciiStream(int paramInt1, InputStream paramInputStream, int paramInt2)
/*      */     throws SQLException
/*      */   {
/* 2558 */     checkState();
/*      */ 
/* 2562 */     checkTypeConcurrency();
/*      */ 
/* 2564 */     this.rs.updateAsciiStream(paramInt1, paramInputStream, paramInt2);
/*      */   }
/*      */ 
/*      */   public void updateBinaryStream(int paramInt1, InputStream paramInputStream, int paramInt2)
/*      */     throws SQLException
/*      */   {
/* 2583 */     checkState();
/*      */ 
/* 2587 */     checkTypeConcurrency();
/*      */ 
/* 2589 */     this.rs.updateBinaryStream(paramInt1, paramInputStream, paramInt2);
/*      */   }
/*      */ 
/*      */   public void updateCharacterStream(int paramInt1, Reader paramReader, int paramInt2)
/*      */     throws SQLException
/*      */   {
/* 2608 */     checkState();
/*      */ 
/* 2612 */     checkTypeConcurrency();
/*      */ 
/* 2614 */     this.rs.updateCharacterStream(paramInt1, paramReader, paramInt2);
/*      */   }
/*      */ 
/*      */   public void updateObject(int paramInt1, Object paramObject, int paramInt2)
/*      */     throws SQLException
/*      */   {
/* 2636 */     checkState();
/*      */ 
/* 2640 */     checkTypeConcurrency();
/*      */ 
/* 2642 */     this.rs.updateObject(paramInt1, paramObject, paramInt2);
/*      */   }
/*      */ 
/*      */   public void updateObject(int paramInt, Object paramObject)
/*      */     throws SQLException
/*      */   {
/* 2660 */     checkState();
/*      */ 
/* 2664 */     checkTypeConcurrency();
/*      */ 
/* 2666 */     this.rs.updateObject(paramInt, paramObject);
/*      */   }
/*      */ 
/*      */   public void updateNull(String paramString)
/*      */     throws SQLException
/*      */   {
/* 2683 */     updateNull(findColumn(paramString));
/*      */   }
/*      */ 
/*      */   public void updateBoolean(String paramString, boolean paramBoolean)
/*      */     throws SQLException
/*      */   {
/* 2699 */     updateBoolean(findColumn(paramString), paramBoolean);
/*      */   }
/*      */ 
/*      */   public void updateByte(String paramString, byte paramByte)
/*      */     throws SQLException
/*      */   {
/* 2715 */     updateByte(findColumn(paramString), paramByte);
/*      */   }
/*      */ 
/*      */   public void updateShort(String paramString, short paramShort)
/*      */     throws SQLException
/*      */   {
/* 2731 */     updateShort(findColumn(paramString), paramShort);
/*      */   }
/*      */ 
/*      */   public void updateInt(String paramString, int paramInt)
/*      */     throws SQLException
/*      */   {
/* 2747 */     updateInt(findColumn(paramString), paramInt);
/*      */   }
/*      */ 
/*      */   public void updateLong(String paramString, long paramLong)
/*      */     throws SQLException
/*      */   {
/* 2763 */     updateLong(findColumn(paramString), paramLong);
/*      */   }
/*      */ 
/*      */   public void updateFloat(String paramString, float paramFloat)
/*      */     throws SQLException
/*      */   {
/* 2779 */     updateFloat(findColumn(paramString), paramFloat);
/*      */   }
/*      */ 
/*      */   public void updateDouble(String paramString, double paramDouble)
/*      */     throws SQLException
/*      */   {
/* 2795 */     updateDouble(findColumn(paramString), paramDouble);
/*      */   }
/*      */ 
/*      */   public void updateBigDecimal(String paramString, BigDecimal paramBigDecimal)
/*      */     throws SQLException
/*      */   {
/* 2812 */     updateBigDecimal(findColumn(paramString), paramBigDecimal);
/*      */   }
/*      */ 
/*      */   public void updateString(String paramString1, String paramString2)
/*      */     throws SQLException
/*      */   {
/* 2828 */     updateString(findColumn(paramString1), paramString2);
/*      */   }
/*      */ 
/*      */   public void updateBytes(String paramString, byte[] paramArrayOfByte)
/*      */     throws SQLException
/*      */   {
/* 2853 */     updateBytes(findColumn(paramString), paramArrayOfByte);
/*      */   }
/*      */ 
/*      */   public void updateDate(String paramString, Date paramDate)
/*      */     throws SQLException
/*      */   {
/* 2869 */     updateDate(findColumn(paramString), paramDate);
/*      */   }
/*      */ 
/*      */   public void updateTime(String paramString, Time paramTime)
/*      */     throws SQLException
/*      */   {
/* 2885 */     updateTime(findColumn(paramString), paramTime);
/*      */   }
/*      */ 
/*      */   public void updateTimestamp(String paramString, Timestamp paramTimestamp)
/*      */     throws SQLException
/*      */   {
/* 2902 */     updateTimestamp(findColumn(paramString), paramTimestamp);
/*      */   }
/*      */ 
/*      */   public void updateAsciiStream(String paramString, InputStream paramInputStream, int paramInt)
/*      */     throws SQLException
/*      */   {
/* 2919 */     updateAsciiStream(findColumn(paramString), paramInputStream, paramInt);
/*      */   }
/*      */ 
/*      */   public void updateBinaryStream(String paramString, InputStream paramInputStream, int paramInt)
/*      */     throws SQLException
/*      */   {
/* 2936 */     updateBinaryStream(findColumn(paramString), paramInputStream, paramInt);
/*      */   }
/*      */ 
/*      */   public void updateCharacterStream(String paramString, Reader paramReader, int paramInt)
/*      */     throws SQLException
/*      */   {
/* 2954 */     updateCharacterStream(findColumn(paramString), paramReader, paramInt);
/*      */   }
/*      */ 
/*      */   public void updateObject(String paramString, Object paramObject, int paramInt)
/*      */     throws SQLException
/*      */   {
/* 2974 */     updateObject(findColumn(paramString), paramObject, paramInt);
/*      */   }
/*      */ 
/*      */   public void updateObject(String paramString, Object paramObject)
/*      */     throws SQLException
/*      */   {
/* 2990 */     updateObject(findColumn(paramString), paramObject);
/*      */   }
/*      */ 
/*      */   public void insertRow()
/*      */     throws SQLException
/*      */   {
/* 3007 */     checkState();
/*      */ 
/* 3009 */     this.rs.insertRow();
/* 3010 */     notifyRowChanged();
/*      */   }
/*      */ 
/*      */   public void updateRow()
/*      */     throws SQLException
/*      */   {
/* 3027 */     checkState();
/*      */ 
/* 3029 */     this.rs.updateRow();
/* 3030 */     notifyRowChanged();
/*      */   }
/*      */ 
/*      */   public void deleteRow()
/*      */     throws SQLException
/*      */   {
/* 3050 */     checkState();
/*      */ 
/* 3052 */     this.rs.deleteRow();
/* 3053 */     notifyRowChanged();
/*      */   }
/*      */ 
/*      */   public void refreshRow()
/*      */     throws SQLException
/*      */   {
/* 3084 */     checkState();
/*      */ 
/* 3086 */     this.rs.refreshRow();
/*      */   }
/*      */ 
/*      */   public void cancelRowUpdates()
/*      */     throws SQLException
/*      */   {
/* 3106 */     checkState();
/*      */ 
/* 3108 */     this.rs.cancelRowUpdates();
/*      */ 
/* 3110 */     notifyRowChanged();
/*      */   }
/*      */ 
/*      */   public void moveToInsertRow()
/*      */     throws SQLException
/*      */   {
/* 3138 */     checkState();
/*      */ 
/* 3140 */     this.rs.moveToInsertRow();
/*      */   }
/*      */ 
/*      */   public void moveToCurrentRow()
/*      */     throws SQLException
/*      */   {
/* 3155 */     checkState();
/*      */ 
/* 3157 */     this.rs.moveToCurrentRow();
/*      */   }
/*      */ 
/*      */   public Statement getStatement()
/*      */     throws SQLException
/*      */   {
/* 3174 */     if (this.rs != null)
/*      */     {
/* 3176 */       return this.rs.getStatement();
/*      */     }
/* 3178 */     return null;
/*      */   }
/*      */ 
/*      */   public Object getObject(int paramInt, Map<String, Class<?>> paramMap)
/*      */     throws SQLException
/*      */   {
/* 3201 */     checkState();
/*      */ 
/* 3203 */     return this.rs.getObject(paramInt, paramMap);
/*      */   }
/*      */ 
/*      */   public Ref getRef(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 3217 */     checkState();
/*      */ 
/* 3219 */     return this.rs.getRef(paramInt);
/*      */   }
/*      */ 
/*      */   public Blob getBlob(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 3235 */     checkState();
/*      */ 
/* 3237 */     return this.rs.getBlob(paramInt);
/*      */   }
/*      */ 
/*      */   public Clob getClob(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 3252 */     checkState();
/*      */ 
/* 3254 */     return this.rs.getClob(paramInt);
/*      */   }
/*      */ 
/*      */   public Array getArray(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 3269 */     checkState();
/*      */ 
/* 3271 */     return this.rs.getArray(paramInt);
/*      */   }
/*      */ 
/*      */   public Object getObject(String paramString, Map<String, Class<?>> paramMap)
/*      */     throws SQLException
/*      */   {
/* 3292 */     return getObject(findColumn(paramString), paramMap);
/*      */   }
/*      */ 
/*      */   public Ref getRef(String paramString)
/*      */     throws SQLException
/*      */   {
/* 3307 */     return getRef(findColumn(paramString));
/*      */   }
/*      */ 
/*      */   public Blob getBlob(String paramString)
/*      */     throws SQLException
/*      */   {
/* 3322 */     return getBlob(findColumn(paramString));
/*      */   }
/*      */ 
/*      */   public Clob getClob(String paramString)
/*      */     throws SQLException
/*      */   {
/* 3337 */     return getClob(findColumn(paramString));
/*      */   }
/*      */ 
/*      */   public Array getArray(String paramString)
/*      */     throws SQLException
/*      */   {
/* 3352 */     return getArray(findColumn(paramString));
/*      */   }
/*      */ 
/*      */   public Date getDate(int paramInt, Calendar paramCalendar)
/*      */     throws SQLException
/*      */   {
/* 3373 */     checkState();
/*      */ 
/* 3375 */     return this.rs.getDate(paramInt, paramCalendar);
/*      */   }
/*      */ 
/*      */   public Date getDate(String paramString, Calendar paramCalendar)
/*      */     throws SQLException
/*      */   {
/* 3397 */     return getDate(findColumn(paramString), paramCalendar);
/*      */   }
/*      */ 
/*      */   public Time getTime(int paramInt, Calendar paramCalendar)
/*      */     throws SQLException
/*      */   {
/* 3418 */     checkState();
/*      */ 
/* 3420 */     return this.rs.getTime(paramInt, paramCalendar);
/*      */   }
/*      */ 
/*      */   public Time getTime(String paramString, Calendar paramCalendar)
/*      */     throws SQLException
/*      */   {
/* 3441 */     return getTime(findColumn(paramString), paramCalendar);
/*      */   }
/*      */ 
/*      */   public Timestamp getTimestamp(int paramInt, Calendar paramCalendar)
/*      */     throws SQLException
/*      */   {
/* 3463 */     checkState();
/*      */ 
/* 3465 */     return this.rs.getTimestamp(paramInt, paramCalendar);
/*      */   }
/*      */ 
/*      */   public Timestamp getTimestamp(String paramString, Calendar paramCalendar)
/*      */     throws SQLException
/*      */   {
/* 3487 */     return getTimestamp(findColumn(paramString), paramCalendar);
/*      */   }
/*      */ 
/*      */   public void updateRef(int paramInt, Ref paramRef)
/*      */     throws SQLException
/*      */   {
/* 3516 */     checkState();
/* 3517 */     this.rs.updateRef(paramInt, paramRef);
/*      */   }
/*      */ 
/*      */   public void updateRef(String paramString, Ref paramRef)
/*      */     throws SQLException
/*      */   {
/* 3544 */     updateRef(findColumn(paramString), paramRef);
/*      */   }
/*      */ 
/*      */   public void updateClob(int paramInt, Clob paramClob)
/*      */     throws SQLException
/*      */   {
/* 3571 */     checkState();
/* 3572 */     this.rs.updateClob(paramInt, paramClob);
/*      */   }
/*      */ 
/*      */   public void updateClob(String paramString, Clob paramClob)
/*      */     throws SQLException
/*      */   {
/* 3599 */     updateClob(findColumn(paramString), paramClob);
/*      */   }
/*      */ 
/*      */   public void updateBlob(int paramInt, Blob paramBlob)
/*      */     throws SQLException
/*      */   {
/* 3626 */     checkState();
/* 3627 */     this.rs.updateBlob(paramInt, paramBlob);
/*      */   }
/*      */ 
/*      */   public void updateBlob(String paramString, Blob paramBlob)
/*      */     throws SQLException
/*      */   {
/* 3653 */     updateBlob(findColumn(paramString), paramBlob);
/*      */   }
/*      */ 
/*      */   public void updateArray(int paramInt, Array paramArray)
/*      */     throws SQLException
/*      */   {
/* 3680 */     checkState();
/* 3681 */     this.rs.updateArray(paramInt, paramArray);
/*      */   }
/*      */ 
/*      */   public void updateArray(String paramString, Array paramArray)
/*      */     throws SQLException
/*      */   {
/* 3707 */     updateArray(findColumn(paramString), paramArray);
/*      */   }
/*      */ 
/*      */   public URL getURL(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 3714 */     checkState();
/* 3715 */     return this.rs.getURL(paramInt);
/*      */   }
/*      */ 
/*      */   public URL getURL(String paramString)
/*      */     throws SQLException
/*      */   {
/* 3722 */     return getURL(findColumn(paramString));
/*      */   }
/*      */ 
/*      */   public RowSetWarning getRowSetWarnings()
/*      */     throws SQLException
/*      */   {
/* 3730 */     return null;
/*      */   }
/*      */ 
/*      */   public void unsetMatchColumn(int[] paramArrayOfInt)
/*      */     throws SQLException
/*      */   {
/* 3749 */     for (int j = 0; j < paramArrayOfInt.length; j++) {
/* 3750 */       int i = Integer.parseInt(((Integer)this.iMatchColumns.get(j)).toString());
/* 3751 */       if (paramArrayOfInt[j] != i) {
/* 3752 */         throw new SQLException(this.resBundle.handleGetObject("jdbcrowsetimpl.matchcols").toString());
/*      */       }
/*      */     }
/*      */ 
/* 3756 */     for (j = 0; j < paramArrayOfInt.length; j++)
/* 3757 */       this.iMatchColumns.set(j, Integer.valueOf(-1));
/*      */   }
/*      */ 
/*      */   public void unsetMatchColumn(String[] paramArrayOfString)
/*      */     throws SQLException
/*      */   {
/* 3777 */     for (int i = 0; i < paramArrayOfString.length; i++) {
/* 3778 */       if (!paramArrayOfString[i].equals(this.strMatchColumns.get(i))) {
/* 3779 */         throw new SQLException(this.resBundle.handleGetObject("jdbcrowsetimpl.matchcols").toString());
/*      */       }
/*      */     }
/*      */ 
/* 3783 */     for (i = 0; i < paramArrayOfString.length; i++)
/* 3784 */       this.strMatchColumns.set(i, null);
/*      */   }
/*      */ 
/*      */   public String[] getMatchColumnNames()
/*      */     throws SQLException
/*      */   {
/* 3800 */     String[] arrayOfString = new String[this.strMatchColumns.size()];
/*      */ 
/* 3802 */     if (this.strMatchColumns.get(0) == null) {
/* 3803 */       throw new SQLException(this.resBundle.handleGetObject("jdbcrowsetimpl.setmatchcols").toString());
/*      */     }
/*      */ 
/* 3806 */     this.strMatchColumns.copyInto(arrayOfString);
/* 3807 */     return arrayOfString;
/*      */   }
/*      */ 
/*      */   public int[] getMatchColumnIndexes()
/*      */     throws SQLException
/*      */   {
/* 3821 */     Integer[] arrayOfInteger = new Integer[this.iMatchColumns.size()];
/* 3822 */     int[] arrayOfInt = new int[this.iMatchColumns.size()];
/*      */ 
/* 3825 */     int i = ((Integer)this.iMatchColumns.get(0)).intValue();
/*      */ 
/* 3827 */     if (i == -1) {
/* 3828 */       throw new SQLException(this.resBundle.handleGetObject("jdbcrowsetimpl.setmatchcols").toString());
/*      */     }
/*      */ 
/* 3832 */     this.iMatchColumns.copyInto(arrayOfInteger);
/*      */ 
/* 3834 */     for (int j = 0; j < arrayOfInteger.length; j++) {
/* 3835 */       arrayOfInt[j] = arrayOfInteger[j].intValue();
/*      */     }
/*      */ 
/* 3838 */     return arrayOfInt;
/*      */   }
/*      */ 
/*      */   public void setMatchColumn(int[] paramArrayOfInt)
/*      */     throws SQLException
/*      */   {
/* 3860 */     for (int i = 0; i < paramArrayOfInt.length; i++) {
/* 3861 */       if (paramArrayOfInt[i] < 0) {
/* 3862 */         throw new SQLException(this.resBundle.handleGetObject("jdbcrowsetimpl.matchcols1").toString());
/*      */       }
/*      */     }
/* 3865 */     for (i = 0; i < paramArrayOfInt.length; i++)
/* 3866 */       this.iMatchColumns.add(i, Integer.valueOf(paramArrayOfInt[i]));
/*      */   }
/*      */ 
/*      */   public void setMatchColumn(String[] paramArrayOfString)
/*      */     throws SQLException
/*      */   {
/* 3887 */     for (int i = 0; i < paramArrayOfString.length; i++) {
/* 3888 */       if ((paramArrayOfString[i] == null) || (paramArrayOfString[i].equals(""))) {
/* 3889 */         throw new SQLException(this.resBundle.handleGetObject("jdbcrowsetimpl.matchcols2").toString());
/*      */       }
/*      */     }
/* 3892 */     for (i = 0; i < paramArrayOfString.length; i++)
/* 3893 */       this.strMatchColumns.add(i, paramArrayOfString[i]);
/*      */   }
/*      */ 
/*      */   public void setMatchColumn(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 3917 */     if (paramInt < 0) {
/* 3918 */       throw new SQLException(this.resBundle.handleGetObject("jdbcrowsetimpl.matchcols1").toString());
/*      */     }
/*      */ 
/* 3921 */     this.iMatchColumns.set(0, Integer.valueOf(paramInt));
/*      */   }
/*      */ 
/*      */   public void setMatchColumn(String paramString)
/*      */     throws SQLException
/*      */   {
/* 3943 */     if ((paramString == null) || ((paramString = paramString.trim()).equals(""))) {
/* 3944 */       throw new SQLException(this.resBundle.handleGetObject("jdbcrowsetimpl.matchcols2").toString());
/*      */     }
/*      */ 
/* 3947 */     this.strMatchColumns.set(0, paramString);
/*      */   }
/*      */ 
/*      */   public void unsetMatchColumn(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 3968 */     if (!((Integer)this.iMatchColumns.get(0)).equals(Integer.valueOf(paramInt)))
/* 3969 */       throw new SQLException(this.resBundle.handleGetObject("jdbcrowsetimpl.unsetmatch").toString());
/* 3970 */     if (this.strMatchColumns.get(0) != null) {
/* 3971 */       throw new SQLException(this.resBundle.handleGetObject("jdbcrowsetimpl.usecolname").toString());
/*      */     }
/*      */ 
/* 3974 */     this.iMatchColumns.set(0, Integer.valueOf(-1));
/*      */   }
/*      */ 
/*      */   public void unsetMatchColumn(String paramString)
/*      */     throws SQLException
/*      */   {
/* 3995 */     paramString = paramString.trim();
/*      */ 
/* 3997 */     if (!((String)this.strMatchColumns.get(0)).equals(paramString))
/* 3998 */       throw new SQLException(this.resBundle.handleGetObject("jdbcrowsetimpl.unsetmatch").toString());
/* 3999 */     if (((Integer)this.iMatchColumns.get(0)).intValue() > 0) {
/* 4000 */       throw new SQLException(this.resBundle.handleGetObject("jdbcrowsetimpl.usecolid").toString());
/*      */     }
/* 4002 */     this.strMatchColumns.set(0, null);
/*      */   }
/*      */ 
/*      */   public DatabaseMetaData getDatabaseMetaData()
/*      */     throws SQLException
/*      */   {
/* 4016 */     Connection localConnection = connect();
/* 4017 */     return localConnection.getMetaData();
/*      */   }
/*      */ 
/*      */   public ParameterMetaData getParameterMetaData()
/*      */     throws SQLException
/*      */   {
/* 4030 */     prepare();
/* 4031 */     return this.ps.getParameterMetaData();
/*      */   }
/*      */ 
/*      */   public void commit()
/*      */     throws SQLException
/*      */   {
/* 4049 */     this.conn.commit();
/*      */ 
/* 4054 */     if (this.conn.getHoldability() != 1) {
/* 4055 */       ResultSet localResultSet = this.rs;
/* 4056 */       this.rs = null;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setAutoCommit(boolean paramBoolean)
/*      */     throws SQLException
/*      */   {
/* 4071 */     if (this.conn != null) {
/* 4072 */       this.conn.setAutoCommit(paramBoolean);
/*      */     }
/*      */     else
/*      */     {
/* 4081 */       this.conn = connect();
/*      */ 
/* 4085 */       this.conn.setAutoCommit(paramBoolean);
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean getAutoCommit()
/*      */     throws SQLException
/*      */   {
/* 4097 */     return this.conn.getAutoCommit();
/*      */   }
/*      */ 
/*      */   public void rollback()
/*      */     throws SQLException
/*      */   {
/* 4113 */     this.conn.rollback();
/*      */ 
/* 4118 */     ResultSet localResultSet = this.rs;
/* 4119 */     this.rs = null;
/*      */   }
/*      */ 
/*      */   public void rollback(Savepoint paramSavepoint)
/*      */     throws SQLException
/*      */   {
/* 4135 */     this.conn.rollback(paramSavepoint);
/*      */   }
/*      */ 
/*      */   protected void setParams() throws SQLException
/*      */   {
/* 4140 */     if (this.rs == null) {
/* 4141 */       setType(1004);
/* 4142 */       setConcurrency(1008);
/*      */     }
/*      */     else {
/* 4145 */       setType(this.rs.getType());
/* 4146 */       setConcurrency(this.rs.getConcurrency());
/*      */     }
/*      */   }
/*      */ 
/*      */   private void checkTypeConcurrency()
/*      */     throws SQLException
/*      */   {
/* 4153 */     if ((this.rs.getType() == 1003) || (this.rs.getConcurrency() == 1007))
/*      */     {
/* 4155 */       throw new SQLException(this.resBundle.handleGetObject("jdbcrowsetimpl.resnotupd").toString());
/*      */     }
/*      */   }
/*      */ 
/*      */   protected Connection getConnection()
/*      */   {
/* 4170 */     return this.conn;
/*      */   }
/*      */ 
/*      */   protected void setConnection(Connection paramConnection)
/*      */   {
/* 4184 */     this.conn = paramConnection;
/*      */   }
/*      */ 
/*      */   protected PreparedStatement getPreparedStatement()
/*      */   {
/* 4198 */     return this.ps;
/*      */   }
/*      */ 
/*      */   protected void setPreparedStatement(PreparedStatement paramPreparedStatement)
/*      */   {
/* 4212 */     this.ps = paramPreparedStatement;
/*      */   }
/*      */ 
/*      */   protected ResultSet getResultSet()
/*      */     throws SQLException
/*      */   {
/* 4227 */     checkState();
/*      */ 
/* 4229 */     return this.rs;
/*      */   }
/*      */ 
/*      */   protected void setResultSet(ResultSet paramResultSet)
/*      */   {
/* 4243 */     this.rs = paramResultSet;
/*      */   }
/*      */ 
/*      */   public void setCommand(String paramString)
/*      */     throws SQLException
/*      */   {
/* 4278 */     if (getCommand() != null) {
/* 4279 */       if (!getCommand().equals(paramString)) {
/* 4280 */         String str = getCommand();
/* 4281 */         super.setCommand(paramString);
/* 4282 */         this.ps = null;
/* 4283 */         this.rs = null;
/* 4284 */         this.propertyChangeSupport.firePropertyChange("command", str, paramString);
/*      */       }
/*      */     }
/*      */     else {
/* 4288 */       super.setCommand(paramString);
/* 4289 */       this.propertyChangeSupport.firePropertyChange("command", null, paramString);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setDataSourceName(String paramString)
/*      */     throws SQLException
/*      */   {
/* 4330 */     if (getDataSourceName() != null) {
/* 4331 */       if (!getDataSourceName().equals(paramString)) {
/* 4332 */         String str = getDataSourceName();
/* 4333 */         super.setDataSourceName(paramString);
/* 4334 */         this.conn = null;
/* 4335 */         this.ps = null;
/* 4336 */         this.rs = null;
/* 4337 */         this.propertyChangeSupport.firePropertyChange("dataSourceName", str, paramString);
/*      */       }
/*      */     }
/*      */     else {
/* 4341 */       super.setDataSourceName(paramString);
/* 4342 */       this.propertyChangeSupport.firePropertyChange("dataSourceName", null, paramString);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setUrl(String paramString)
/*      */     throws SQLException
/*      */   {
/* 4395 */     if (getUrl() != null) {
/* 4396 */       if (!getUrl().equals(paramString)) {
/* 4397 */         String str = getUrl();
/* 4398 */         super.setUrl(paramString);
/* 4399 */         this.conn = null;
/* 4400 */         this.ps = null;
/* 4401 */         this.rs = null;
/* 4402 */         this.propertyChangeSupport.firePropertyChange("url", str, paramString);
/*      */       }
/*      */     }
/*      */     else {
/* 4406 */       super.setUrl(paramString);
/* 4407 */       this.propertyChangeSupport.firePropertyChange("url", null, paramString);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setUsername(String paramString)
/*      */   {
/* 4439 */     if (getUsername() != null) {
/* 4440 */       if (!getUsername().equals(paramString)) {
/* 4441 */         String str = getUsername();
/* 4442 */         super.setUsername(paramString);
/* 4443 */         this.conn = null;
/* 4444 */         this.ps = null;
/* 4445 */         this.rs = null;
/* 4446 */         this.propertyChangeSupport.firePropertyChange("username", str, paramString);
/*      */       }
/*      */     }
/*      */     else {
/* 4450 */       super.setUsername(paramString);
/* 4451 */       this.propertyChangeSupport.firePropertyChange("username", null, paramString);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setPassword(String paramString)
/*      */   {
/* 4482 */     if (getPassword() != null) {
/* 4483 */       if (!getPassword().equals(paramString)) {
/* 4484 */         String str = getPassword();
/* 4485 */         super.setPassword(paramString);
/* 4486 */         this.conn = null;
/* 4487 */         this.ps = null;
/* 4488 */         this.rs = null;
/* 4489 */         this.propertyChangeSupport.firePropertyChange("password", str, paramString);
/*      */       }
/*      */     }
/*      */     else {
/* 4493 */       super.setPassword(paramString);
/* 4494 */       this.propertyChangeSupport.firePropertyChange("password", null, paramString);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setType(int paramInt)
/*      */     throws SQLException
/*      */   {
/*      */     int i;
/*      */     try
/*      */     {
/* 4520 */       i = getType();
/*      */     } catch (SQLException localSQLException) {
/* 4522 */       i = 0;
/*      */     }
/*      */ 
/* 4525 */     if (i != paramInt) {
/* 4526 */       super.setType(paramInt);
/* 4527 */       this.propertyChangeSupport.firePropertyChange("type", i, paramInt);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setConcurrency(int paramInt)
/*      */     throws SQLException
/*      */   {
/*      */     int i;
/*      */     try
/*      */     {
/* 4553 */       i = getConcurrency();
/*      */     } catch (NullPointerException localNullPointerException) {
/* 4555 */       i = 0;
/*      */     }
/*      */ 
/* 4558 */     if (i != paramInt) {
/* 4559 */       super.setConcurrency(paramInt);
/* 4560 */       this.propertyChangeSupport.firePropertyChange("concurrency", i, paramInt);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setTransactionIsolation(int paramInt)
/*      */     throws SQLException
/*      */   {
/*      */     int i;
/*      */     try
/*      */     {
/* 4594 */       i = getTransactionIsolation();
/*      */     } catch (NullPointerException localNullPointerException) {
/* 4596 */       i = 0;
/*      */     }
/*      */ 
/* 4599 */     if (i != paramInt) {
/* 4600 */       super.setTransactionIsolation(paramInt);
/* 4601 */       this.propertyChangeSupport.firePropertyChange("transactionIsolation", i, paramInt);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setMaxRows(int paramInt)
/*      */     throws SQLException
/*      */   {
/*      */     int i;
/*      */     try
/*      */     {
/* 4624 */       i = getMaxRows();
/*      */     } catch (NullPointerException localNullPointerException) {
/* 4626 */       i = 0;
/*      */     }
/*      */ 
/* 4629 */     if (i != paramInt) {
/* 4630 */       super.setMaxRows(paramInt);
/* 4631 */       this.propertyChangeSupport.firePropertyChange("maxRows", i, paramInt);
/*      */     }
/*      */   }
/*      */ 
/*      */   public SQLXML getSQLXML(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 4645 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
/*      */   }
/*      */ 
/*      */   public SQLXML getSQLXML(String paramString)
/*      */     throws SQLException
/*      */   {
/* 4656 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
/*      */   }
/*      */ 
/*      */   public RowId getRowId(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 4671 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
/*      */   }
/*      */ 
/*      */   public RowId getRowId(String paramString)
/*      */     throws SQLException
/*      */   {
/* 4686 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
/*      */   }
/*      */ 
/*      */   public void updateRowId(int paramInt, RowId paramRowId)
/*      */     throws SQLException
/*      */   {
/* 4702 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
/*      */   }
/*      */ 
/*      */   public void updateRowId(String paramString, RowId paramRowId)
/*      */     throws SQLException
/*      */   {
/* 4718 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
/*      */   }
/*      */ 
/*      */   public int getHoldability()
/*      */     throws SQLException
/*      */   {
/* 4728 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
/*      */   }
/*      */ 
/*      */   public boolean isClosed()
/*      */     throws SQLException
/*      */   {
/* 4739 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
/*      */   }
/*      */ 
/*      */   public void updateNString(int paramInt, String paramString)
/*      */     throws SQLException
/*      */   {
/* 4751 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
/*      */   }
/*      */ 
/*      */   public void updateNString(String paramString1, String paramString2)
/*      */     throws SQLException
/*      */   {
/* 4763 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
/*      */   }
/*      */ 
/*      */   public void updateNClob(int paramInt, NClob paramNClob)
/*      */     throws SQLException
/*      */   {
/* 4776 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
/*      */   }
/*      */ 
/*      */   public void updateNClob(String paramString, NClob paramNClob)
/*      */     throws SQLException
/*      */   {
/* 4788 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
/*      */   }
/*      */ 
/*      */   public NClob getNClob(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 4803 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
/*      */   }
/*      */ 
/*      */   public NClob getNClob(String paramString)
/*      */     throws SQLException
/*      */   {
/* 4819 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
/*      */   }
/*      */ 
/*      */   public <T> T unwrap(Class<T> paramClass) throws SQLException {
/* 4823 */     return null;
/*      */   }
/*      */ 
/*      */   public boolean isWrapperFor(Class<?> paramClass) throws SQLException {
/* 4827 */     return false;
/*      */   }
/*      */ 
/*      */   public void setSQLXML(int paramInt, SQLXML paramSQLXML)
/*      */     throws SQLException
/*      */   {
/* 4839 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
/*      */   }
/*      */ 
/*      */   public void setSQLXML(String paramString, SQLXML paramSQLXML)
/*      */     throws SQLException
/*      */   {
/* 4851 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
/*      */   }
/*      */ 
/*      */   public void setRowId(int paramInt, RowId paramRowId)
/*      */     throws SQLException
/*      */   {
/* 4866 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
/*      */   }
/*      */ 
/*      */   public void setRowId(String paramString, RowId paramRowId)
/*      */     throws SQLException
/*      */   {
/* 4880 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
/*      */   }
/*      */ 
/*      */   public void setNString(int paramInt, String paramString)
/*      */     throws SQLException
/*      */   {
/* 4900 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
/*      */   }
/*      */ 
/*      */   public void setNCharacterStream(int paramInt, Reader paramReader)
/*      */     throws SQLException
/*      */   {
/* 4928 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
/*      */   }
/*      */ 
/*      */   public void setNClob(String paramString, NClob paramNClob)
/*      */     throws SQLException
/*      */   {
/* 4943 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
/*      */   }
/*      */ 
/*      */   public Reader getNCharacterStream(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 4963 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
/*      */   }
/*      */ 
/*      */   public Reader getNCharacterStream(String paramString)
/*      */     throws SQLException
/*      */   {
/* 4983 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
/*      */   }
/*      */ 
/*      */   public void updateSQLXML(int paramInt, SQLXML paramSQLXML)
/*      */     throws SQLException
/*      */   {
/* 4999 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
/*      */   }
/*      */ 
/*      */   public void updateSQLXML(String paramString, SQLXML paramSQLXML)
/*      */     throws SQLException
/*      */   {
/* 5016 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
/*      */   }
/*      */ 
/*      */   public String getNString(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 5034 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
/*      */   }
/*      */ 
/*      */   public String getNString(String paramString)
/*      */     throws SQLException
/*      */   {
/* 5052 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
/*      */   }
/*      */ 
/*      */   public void updateNCharacterStream(int paramInt, Reader paramReader, long paramLong)
/*      */     throws SQLException
/*      */   {
/* 5074 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
/*      */   }
/*      */ 
/*      */   public void updateNCharacterStream(String paramString, Reader paramReader, long paramLong)
/*      */     throws SQLException
/*      */   {
/* 5096 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
/*      */   }
/*      */ 
/*      */   public void updateNCharacterStream(int paramInt, Reader paramReader)
/*      */     throws SQLException
/*      */   {
/* 5126 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
/*      */   }
/*      */ 
/*      */   public void updateNCharacterStream(String paramString, Reader paramReader)
/*      */     throws SQLException
/*      */   {
/* 5158 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
/*      */   }
/*      */ 
/*      */   public void updateBlob(int paramInt, InputStream paramInputStream, long paramLong)
/*      */     throws SQLException
/*      */   {
/* 5191 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
/*      */   }
/*      */ 
/*      */   public void updateBlob(String paramString, InputStream paramInputStream, long paramLong)
/*      */     throws SQLException
/*      */   {
/* 5224 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
/*      */   }
/*      */ 
/*      */   public void updateBlob(int paramInt, InputStream paramInputStream)
/*      */     throws SQLException
/*      */   {
/* 5259 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
/*      */   }
/*      */ 
/*      */   public void updateBlob(String paramString, InputStream paramInputStream)
/*      */     throws SQLException
/*      */   {
/* 5294 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
/*      */   }
/*      */ 
/*      */   public void updateClob(int paramInt, Reader paramReader, long paramLong)
/*      */     throws SQLException
/*      */   {
/* 5326 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
/*      */   }
/*      */ 
/*      */   public void updateClob(String paramString, Reader paramReader, long paramLong)
/*      */     throws SQLException
/*      */   {
/* 5358 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
/*      */   }
/*      */ 
/*      */   public void updateClob(int paramInt, Reader paramReader)
/*      */     throws SQLException
/*      */   {
/* 5392 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
/*      */   }
/*      */ 
/*      */   public void updateClob(String paramString, Reader paramReader)
/*      */     throws SQLException
/*      */   {
/* 5427 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
/*      */   }
/*      */ 
/*      */   public void updateNClob(int paramInt, Reader paramReader, long paramLong)
/*      */     throws SQLException
/*      */   {
/* 5461 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
/*      */   }
/*      */ 
/*      */   public void updateNClob(String paramString, Reader paramReader, long paramLong)
/*      */     throws SQLException
/*      */   {
/* 5495 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
/*      */   }
/*      */ 
/*      */   public void updateNClob(int paramInt, Reader paramReader)
/*      */     throws SQLException
/*      */   {
/* 5531 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
/*      */   }
/*      */ 
/*      */   public void updateNClob(String paramString, Reader paramReader)
/*      */     throws SQLException
/*      */   {
/* 5568 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
/*      */   }
/*      */ 
/*      */   public void updateAsciiStream(int paramInt, InputStream paramInputStream, long paramLong)
/*      */     throws SQLException
/*      */   {
/* 5593 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
/*      */   }
/*      */ 
/*      */   public void updateBinaryStream(int paramInt, InputStream paramInputStream, long paramLong)
/*      */     throws SQLException
/*      */   {
/* 5617 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
/*      */   }
/*      */ 
/*      */   public void updateCharacterStream(int paramInt, Reader paramReader, long paramLong)
/*      */     throws SQLException
/*      */   {
/* 5641 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
/*      */   }
/*      */ 
/*      */   public void updateAsciiStream(String paramString, InputStream paramInputStream, long paramLong)
/*      */     throws SQLException
/*      */   {
/* 5665 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
/*      */   }
/*      */ 
/*      */   public void updateAsciiStream(int paramInt, InputStream paramInputStream)
/*      */     throws SQLException
/*      */   {
/* 5690 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
/*      */   }
/*      */ 
/*      */   public void updateAsciiStream(String paramString, InputStream paramInputStream)
/*      */     throws SQLException
/*      */   {
/* 5716 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
/*      */   }
/*      */ 
/*      */   public void updateBinaryStream(String paramString, InputStream paramInputStream, long paramLong)
/*      */     throws SQLException
/*      */   {
/* 5741 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
/*      */   }
/*      */ 
/*      */   public void updateBinaryStream(int paramInt, InputStream paramInputStream)
/*      */     throws SQLException
/*      */   {
/* 5766 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
/*      */   }
/*      */ 
/*      */   public void updateBinaryStream(String paramString, InputStream paramInputStream)
/*      */     throws SQLException
/*      */   {
/* 5793 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
/*      */   }
/*      */ 
/*      */   public void updateCharacterStream(String paramString, Reader paramReader, long paramLong)
/*      */     throws SQLException
/*      */   {
/* 5819 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
/*      */   }
/*      */ 
/*      */   public void updateCharacterStream(int paramInt, Reader paramReader)
/*      */     throws SQLException
/*      */   {
/* 5844 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
/*      */   }
/*      */ 
/*      */   public void updateCharacterStream(String paramString, Reader paramReader)
/*      */     throws SQLException
/*      */   {
/* 5871 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
/*      */   }
/*      */ 
/*      */   public void setURL(int paramInt, URL paramURL)
/*      */     throws SQLException
/*      */   {
/* 5888 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
/*      */   }
/*      */ 
/*      */   public void setNClob(int paramInt, Reader paramReader)
/*      */     throws SQLException
/*      */   {
/* 5917 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
/*      */   }
/*      */ 
/*      */   public void setNClob(String paramString, Reader paramReader, long paramLong)
/*      */     throws SQLException
/*      */   {
/* 5945 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
/*      */   }
/*      */ 
/*      */   public void setNClob(String paramString, Reader paramReader)
/*      */     throws SQLException
/*      */   {
/* 5972 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
/*      */   }
/*      */ 
/*      */   public void setNClob(int paramInt, Reader paramReader, long paramLong)
/*      */     throws SQLException
/*      */   {
/* 5999 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
/*      */   }
/*      */ 
/*      */   public void setNClob(int paramInt, NClob paramNClob)
/*      */     throws SQLException
/*      */   {
/* 6015 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
/*      */   }
/*      */ 
/*      */   public void setNString(String paramString1, String paramString2)
/*      */     throws SQLException
/*      */   {
/* 6032 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
/*      */   }
/*      */ 
/*      */   public void setNCharacterStream(int paramInt, Reader paramReader, long paramLong)
/*      */     throws SQLException
/*      */   {
/* 6049 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
/*      */   }
/*      */ 
/*      */   public void setNCharacterStream(String paramString, Reader paramReader, long paramLong)
/*      */     throws SQLException
/*      */   {
/* 6069 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
/*      */   }
/*      */ 
/*      */   public void setNCharacterStream(String paramString, Reader paramReader)
/*      */     throws SQLException
/*      */   {
/* 6095 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
/*      */   }
/*      */ 
/*      */   public void setTimestamp(String paramString, Timestamp paramTimestamp, Calendar paramCalendar)
/*      */     throws SQLException
/*      */   {
/* 6121 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
/*      */   }
/*      */ 
/*      */   public void setClob(String paramString, Reader paramReader, long paramLong)
/*      */     throws SQLException
/*      */   {
/* 6147 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
/*      */   }
/*      */ 
/*      */   public void setClob(String paramString, Clob paramClob)
/*      */     throws SQLException
/*      */   {
/* 6166 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
/*      */   }
/*      */ 
/*      */   public void setClob(String paramString, Reader paramReader)
/*      */     throws SQLException
/*      */   {
/* 6191 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
/*      */   }
/*      */ 
/*      */   public void setDate(String paramString, Date paramDate)
/*      */     throws SQLException
/*      */   {
/* 6213 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
/*      */   }
/*      */ 
/*      */   public void setDate(String paramString, Date paramDate, Calendar paramCalendar)
/*      */     throws SQLException
/*      */   {
/* 6239 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
/*      */   }
/*      */ 
/*      */   public void setTime(String paramString, Time paramTime)
/*      */     throws SQLException
/*      */   {
/* 6259 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
/*      */   }
/*      */ 
/*      */   public void setTime(String paramString, Time paramTime, Calendar paramCalendar)
/*      */     throws SQLException
/*      */   {
/* 6285 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
/*      */   }
/*      */ 
/*      */   public void setClob(int paramInt, Reader paramReader)
/*      */     throws SQLException
/*      */   {
/* 6311 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
/*      */   }
/*      */ 
/*      */   public void setClob(int paramInt, Reader paramReader, long paramLong)
/*      */     throws SQLException
/*      */   {
/* 6336 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
/*      */   }
/*      */ 
/*      */   public void setBlob(int paramInt, InputStream paramInputStream, long paramLong)
/*      */     throws SQLException
/*      */   {
/* 6366 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
/*      */   }
/*      */ 
/*      */   public void setBlob(int paramInt, InputStream paramInputStream)
/*      */     throws SQLException
/*      */   {
/* 6398 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
/*      */   }
/*      */ 
/*      */   public void setBlob(String paramString, InputStream paramInputStream, long paramLong)
/*      */     throws SQLException
/*      */   {
/* 6429 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
/*      */   }
/*      */ 
/*      */   public void setBlob(String paramString, Blob paramBlob)
/*      */     throws SQLException
/*      */   {
/* 6447 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
/*      */   }
/*      */ 
/*      */   public void setBlob(String paramString, InputStream paramInputStream)
/*      */     throws SQLException
/*      */   {
/* 6473 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
/*      */   }
/*      */ 
/*      */   public void setObject(String paramString, Object paramObject, int paramInt1, int paramInt2)
/*      */     throws SQLException
/*      */   {
/* 6519 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
/*      */   }
/*      */ 
/*      */   public void setObject(String paramString, Object paramObject, int paramInt)
/*      */     throws SQLException
/*      */   {
/* 6545 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
/*      */   }
/*      */ 
/*      */   public void setObject(String paramString, Object paramObject)
/*      */     throws SQLException
/*      */   {
/* 6585 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
/*      */   }
/*      */ 
/*      */   public void setAsciiStream(String paramString, InputStream paramInputStream, int paramInt)
/*      */     throws SQLException
/*      */   {
/* 6612 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
/*      */   }
/*      */ 
/*      */   public void setBinaryStream(String paramString, InputStream paramInputStream, int paramInt)
/*      */     throws SQLException
/*      */   {
/* 6639 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
/*      */   }
/*      */ 
/*      */   public void setCharacterStream(String paramString, Reader paramReader, int paramInt)
/*      */     throws SQLException
/*      */   {
/* 6668 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
/*      */   }
/*      */ 
/*      */   public void setAsciiStream(String paramString, InputStream paramInputStream)
/*      */     throws SQLException
/*      */   {
/* 6695 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
/*      */   }
/*      */ 
/*      */   public void setBinaryStream(String paramString, InputStream paramInputStream)
/*      */     throws SQLException
/*      */   {
/* 6722 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
/*      */   }
/*      */ 
/*      */   public void setCharacterStream(String paramString, Reader paramReader)
/*      */     throws SQLException
/*      */   {
/* 6751 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
/*      */   }
/*      */ 
/*      */   public void setBigDecimal(String paramString, BigDecimal paramBigDecimal)
/*      */     throws SQLException
/*      */   {
/* 6770 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
/*      */   }
/*      */ 
/*      */   public void setString(String paramString1, String paramString2)
/*      */     throws SQLException
/*      */   {
/* 6791 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
/*      */   }
/*      */ 
/*      */   public void setBytes(String paramString, byte[] paramArrayOfByte)
/*      */     throws SQLException
/*      */   {
/* 6813 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
/*      */   }
/*      */ 
/*      */   public void setTimestamp(String paramString, Timestamp paramTimestamp)
/*      */     throws SQLException
/*      */   {
/* 6833 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
/*      */   }
/*      */ 
/*      */   public void setNull(String paramString, int paramInt)
/*      */     throws SQLException
/*      */   {
/* 6850 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
/*      */   }
/*      */ 
/*      */   public void setNull(String paramString1, int paramInt, String paramString2)
/*      */     throws SQLException
/*      */   {
/* 6887 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
/*      */   }
/*      */ 
/*      */   public void setBoolean(String paramString, boolean paramBoolean)
/*      */     throws SQLException
/*      */   {
/* 6905 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
/*      */   }
/*      */ 
/*      */   public void setByte(String paramString, byte paramByte)
/*      */     throws SQLException
/*      */   {
/* 6925 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
/*      */   }
/*      */ 
/*      */   public void setShort(String paramString, short paramShort)
/*      */     throws SQLException
/*      */   {
/* 6944 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
/*      */   }
/*      */ 
/*      */   public void setInt(String paramString, int paramInt)
/*      */     throws SQLException
/*      */   {
/* 6963 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
/*      */   }
/*      */ 
/*      */   public void setLong(String paramString, long paramLong)
/*      */     throws SQLException
/*      */   {
/* 6981 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
/*      */   }
/*      */ 
/*      */   public void setFloat(String paramString, float paramFloat)
/*      */     throws SQLException
/*      */   {
/* 7000 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
/*      */   }
/*      */ 
/*      */   public void setDouble(String paramString, double paramDouble)
/*      */     throws SQLException
/*      */   {
/* 7018 */     throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
/*      */   }
/*      */ 
/*      */   private void readObject(ObjectInputStream paramObjectInputStream)
/*      */     throws IOException, ClassNotFoundException
/*      */   {
/* 7028 */     paramObjectInputStream.defaultReadObject();
/*      */     try
/*      */     {
/* 7031 */       this.resBundle = JdbcRowSetResourceBundle.getJdbcRowSetResourceBundle();
/*      */     }
/*      */     catch (IOException localIOException)
/*      */     {
/*      */     }
/*      */   }
/*      */ 
/*      */   public <T> T getObject(int paramInt, Class<T> paramClass)
/*      */     throws SQLException
/*      */   {
/* 7041 */     throw new SQLFeatureNotSupportedException("Not supported yet.");
/*      */   }
/*      */ 
/*      */   public <T> T getObject(String paramString, Class<T> paramClass) throws SQLException {
/* 7045 */     throw new SQLFeatureNotSupportedException("Not supported yet.");
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.rowset.JdbcRowSetImpl
 * JD-Core Version:    0.6.2
 */