/*     */ package com.sun.rowset.internal;
/*     */ 
/*     */ import com.sun.rowset.JdbcRowSetResourceBundle;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.io.Reader;
/*     */ import java.io.Serializable;
/*     */ import java.sql.Connection;
/*     */ import java.sql.Date;
/*     */ import java.sql.DriverManager;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.Time;
/*     */ import java.sql.Timestamp;
/*     */ import java.util.Calendar;
/*     */ import javax.naming.Context;
/*     */ import javax.naming.InitialContext;
/*     */ import javax.naming.NamingException;
/*     */ import javax.sql.DataSource;
/*     */ import javax.sql.RowSet;
/*     */ import javax.sql.RowSetInternal;
/*     */ import javax.sql.RowSetReader;
/*     */ import javax.sql.rowset.CachedRowSet;
/*     */ 
/*     */ public class CachedRowSetReader
/*     */   implements RowSetReader, Serializable
/*     */ {
/*  79 */   private int writerCalls = 0;
/*     */ 
/*  81 */   private boolean userCon = false;
/*     */   private int startPosition;
/*     */   private JdbcRowSetResourceBundle resBundle;
/*     */   static final long serialVersionUID = 5049738185801363801L;
/*     */ 
/*     */   public CachedRowSetReader()
/*     */   {
/*     */     try
/*     */     {
/*  89 */       this.resBundle = JdbcRowSetResourceBundle.getJdbcRowSetResourceBundle();
/*     */     } catch (IOException localIOException) {
/*  91 */       throw new RuntimeException(localIOException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void readData(RowSetInternal paramRowSetInternal)
/*     */     throws SQLException
/*     */   {
/* 132 */     Connection localConnection = null;
/*     */     try {
/* 134 */       CachedRowSet localCachedRowSet = (CachedRowSet)paramRowSetInternal;
/*     */ 
/* 144 */       if ((localCachedRowSet.getPageSize() == 0) && (localCachedRowSet.size() > 0))
/*     */       {
/* 147 */         localCachedRowSet.close();
/*     */       }
/*     */ 
/* 150 */       this.writerCalls = 0;
/*     */ 
/* 155 */       this.userCon = false;
/*     */ 
/* 157 */       localConnection = connect(paramRowSetInternal);
/*     */ 
/* 160 */       if ((localConnection == null) || (localCachedRowSet.getCommand() == null))
/* 161 */         throw new SQLException(this.resBundle.handleGetObject("crsreader.connecterr").toString());
/*     */       try
/*     */       {
/* 164 */         localConnection.setTransactionIsolation(localCachedRowSet.getTransactionIsolation());
/*     */       }
/*     */       catch (Exception localException2)
/*     */       {
/*     */       }
/* 169 */       PreparedStatement localPreparedStatement = localConnection.prepareStatement(localCachedRowSet.getCommand());
/*     */ 
/* 172 */       decodeParams(paramRowSetInternal.getParams(), localPreparedStatement);
/*     */       try {
/* 174 */         localPreparedStatement.setMaxRows(localCachedRowSet.getMaxRows());
/* 175 */         localPreparedStatement.setMaxFieldSize(localCachedRowSet.getMaxFieldSize());
/* 176 */         localPreparedStatement.setEscapeProcessing(localCachedRowSet.getEscapeProcessing());
/* 177 */         localPreparedStatement.setQueryTimeout(localCachedRowSet.getQueryTimeout());
/*     */       }
/*     */       catch (Exception localException3)
/*     */       {
/* 183 */         throw new SQLException(localException3.getMessage());
/*     */       }
/*     */ 
/* 186 */       if (localCachedRowSet.getCommand().toLowerCase().indexOf("select") != -1)
/*     */       {
/* 195 */         ResultSet localResultSet = localPreparedStatement.executeQuery();
/* 196 */         if (localCachedRowSet.getPageSize() == 0) {
/* 197 */           localCachedRowSet.populate(localResultSet);
/*     */         }
/*     */         else
/*     */         {
/* 205 */           localPreparedStatement = localConnection.prepareStatement(localCachedRowSet.getCommand(), 1004, 1008);
/* 206 */           decodeParams(paramRowSetInternal.getParams(), localPreparedStatement);
/*     */           try {
/* 208 */             localPreparedStatement.setMaxRows(localCachedRowSet.getMaxRows());
/* 209 */             localPreparedStatement.setMaxFieldSize(localCachedRowSet.getMaxFieldSize());
/* 210 */             localPreparedStatement.setEscapeProcessing(localCachedRowSet.getEscapeProcessing());
/* 211 */             localPreparedStatement.setQueryTimeout(localCachedRowSet.getQueryTimeout());
/*     */           }
/*     */           catch (Exception localException4)
/*     */           {
/* 217 */             throw new SQLException(localException4.getMessage());
/*     */           }
/* 219 */           localResultSet = localPreparedStatement.executeQuery();
/* 220 */           localCachedRowSet.populate(localResultSet, this.startPosition);
/*     */         }
/* 222 */         localResultSet.close();
/*     */       } else {
/* 224 */         localPreparedStatement.executeUpdate();
/*     */       }
/*     */ 
/* 228 */       localPreparedStatement.close();
/*     */       try {
/* 230 */         localConnection.commit();
/*     */       }
/*     */       catch (SQLException localSQLException3)
/*     */       {
/*     */       }
/* 235 */       if (getCloseConnection() == true) {
/* 236 */         localConnection.close();
/*     */       }
/*     */ 
/*     */       try
/*     */       {
/* 244 */         if ((localConnection != null) && (getCloseConnection() == true)) {
/*     */           try {
/* 246 */             if (!localConnection.getAutoCommit()) {
/* 247 */               localConnection.rollback();
/*     */             }
/*     */ 
/*     */           }
/*     */           catch (Exception localException1)
/*     */           {
/*     */           }
/*     */ 
/* 257 */           localConnection.close();
/* 258 */           localConnection = null;
/*     */         }
/*     */       }
/*     */       catch (SQLException localSQLException1)
/*     */       {
/*     */       }
/*     */     }
/*     */     catch (SQLException localSQLException2)
/*     */     {
/* 240 */       throw localSQLException2;
/*     */     }
/*     */     finally {
/*     */       try {
/* 244 */         if ((localConnection != null) && (getCloseConnection() == true)) {
/*     */           try {
/* 246 */             if (!localConnection.getAutoCommit()) {
/* 247 */               localConnection.rollback();
/*     */             }
/*     */ 
/*     */           }
/*     */           catch (Exception localException5)
/*     */           {
/*     */           }
/*     */ 
/* 257 */           localConnection.close();
/* 258 */           localConnection = null;
/*     */         }
/*     */       }
/*     */       catch (SQLException localSQLException4)
/*     */       {
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean reset()
/*     */     throws SQLException
/*     */   {
/* 279 */     this.writerCalls += 1;
/* 280 */     return this.writerCalls == 1;
/*     */   }
/*     */ 
/*     */   public Connection connect(RowSetInternal paramRowSetInternal)
/*     */     throws SQLException
/*     */   {
/* 307 */     if (paramRowSetInternal.getConnection() != null)
/*     */     {
/* 311 */       this.userCon = true;
/* 312 */       return paramRowSetInternal.getConnection();
/*     */     }
/* 314 */     if (((RowSet)paramRowSetInternal).getDataSourceName() != null)
/*     */       try
/*     */       {
/* 317 */         InitialContext localInitialContext = new InitialContext();
/* 318 */         localObject = (DataSource)localInitialContext.lookup(((RowSet)paramRowSetInternal).getDataSourceName());
/*     */ 
/* 326 */         if (((RowSet)paramRowSetInternal).getUsername() != null) {
/* 327 */           return ((DataSource)localObject).getConnection(((RowSet)paramRowSetInternal).getUsername(), ((RowSet)paramRowSetInternal).getPassword());
/*     */         }
/*     */ 
/* 330 */         return ((DataSource)localObject).getConnection();
/*     */       }
/*     */       catch (NamingException localNamingException)
/*     */       {
/* 334 */         Object localObject = new SQLException(this.resBundle.handleGetObject("crsreader.connect").toString());
/* 335 */         ((SQLException)localObject).initCause(localNamingException);
/* 336 */         throw ((Throwable)localObject);
/*     */       }
/* 338 */     if (((RowSet)paramRowSetInternal).getUrl() != null)
/*     */     {
/* 340 */       return DriverManager.getConnection(((RowSet)paramRowSetInternal).getUrl(), ((RowSet)paramRowSetInternal).getUsername(), ((RowSet)paramRowSetInternal).getPassword());
/*     */     }
/*     */ 
/* 345 */     return null;
/*     */   }
/*     */ 
/*     */   private void decodeParams(Object[] paramArrayOfObject, PreparedStatement paramPreparedStatement)
/*     */     throws SQLException
/*     */   {
/* 376 */     Object[] arrayOfObject = null;
/*     */ 
/* 378 */     for (int i = 0; i < paramArrayOfObject.length; i++)
/* 379 */       if ((paramArrayOfObject[i] instanceof Object[])) {
/* 380 */         arrayOfObject = (Object[])paramArrayOfObject[i];
/*     */ 
/* 382 */         if (arrayOfObject.length == 2) {
/* 383 */           if (arrayOfObject[0] == null) {
/* 384 */             paramPreparedStatement.setNull(i + 1, ((Integer)arrayOfObject[1]).intValue());
/*     */           }
/* 388 */           else if (((arrayOfObject[0] instanceof Date)) || ((arrayOfObject[0] instanceof Time)) || ((arrayOfObject[0] instanceof Timestamp)))
/*     */           {
/* 391 */             System.err.println(this.resBundle.handleGetObject("crsreader.datedetected").toString());
/* 392 */             if ((arrayOfObject[1] instanceof Calendar)) {
/* 393 */               System.err.println(this.resBundle.handleGetObject("crsreader.caldetected").toString());
/* 394 */               paramPreparedStatement.setDate(i + 1, (Date)arrayOfObject[0], (Calendar)arrayOfObject[1]);
/*     */             }
/*     */             else
/*     */             {
/* 399 */               throw new SQLException(this.resBundle.handleGetObject("crsreader.paramtype").toString());
/*     */             }
/*     */ 
/*     */           }
/* 403 */           else if ((arrayOfObject[0] instanceof Reader)) {
/* 404 */             paramPreparedStatement.setCharacterStream(i + 1, (Reader)arrayOfObject[0], ((Integer)arrayOfObject[1]).intValue());
/*     */           }
/* 412 */           else if ((arrayOfObject[1] instanceof Integer)) {
/* 413 */             paramPreparedStatement.setObject(i + 1, arrayOfObject[0], ((Integer)arrayOfObject[1]).intValue());
/*     */           }
/*     */ 
/*     */         }
/* 417 */         else if (arrayOfObject.length == 3)
/*     */         {
/* 419 */           if (arrayOfObject[0] == null) {
/* 420 */             paramPreparedStatement.setNull(i + 1, ((Integer)arrayOfObject[1]).intValue(), (String)arrayOfObject[2]);
/*     */           }
/*     */           else
/*     */           {
/* 425 */             if ((arrayOfObject[0] instanceof InputStream)) {
/* 426 */               switch (((Integer)arrayOfObject[2]).intValue()) {
/*     */               case 0:
/* 428 */                 paramPreparedStatement.setUnicodeStream(i + 1, (InputStream)arrayOfObject[0], ((Integer)arrayOfObject[1]).intValue());
/*     */               case 1:
/* 432 */                 paramPreparedStatement.setBinaryStream(i + 1, (InputStream)arrayOfObject[0], ((Integer)arrayOfObject[1]).intValue());
/*     */               case 2:
/* 436 */                 paramPreparedStatement.setAsciiStream(i + 1, (InputStream)arrayOfObject[0], ((Integer)arrayOfObject[1]).intValue());
/*     */               }
/*     */ 
/* 440 */               throw new SQLException(this.resBundle.handleGetObject("crsreader.paramtype").toString());
/*     */             }
/*     */ 
/* 448 */             if (((arrayOfObject[1] instanceof Integer)) && ((arrayOfObject[2] instanceof Integer))) {
/* 449 */               paramPreparedStatement.setObject(i + 1, arrayOfObject[0], ((Integer)arrayOfObject[1]).intValue(), ((Integer)arrayOfObject[2]).intValue());
/*     */             }
/*     */             else
/*     */             {
/* 454 */               throw new SQLException(this.resBundle.handleGetObject("crsreader.paramtype").toString());
/*     */             }
/*     */           }
/*     */         }
/* 458 */         else paramPreparedStatement.setObject(i + 1, paramArrayOfObject[i]);
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 463 */         paramPreparedStatement.setObject(i + 1, paramArrayOfObject[i]);
/*     */       }
/*     */   }
/*     */ 
/*     */   protected boolean getCloseConnection()
/*     */   {
/* 476 */     if (this.userCon == true) {
/* 477 */       return false;
/*     */     }
/* 479 */     return true;
/*     */   }
/*     */ 
/*     */   public void setStartPosition(int paramInt)
/*     */   {
/* 490 */     this.startPosition = paramInt;
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream) throws IOException, ClassNotFoundException
/*     */   {
/* 495 */     paramObjectInputStream.defaultReadObject();
/*     */     try
/*     */     {
/* 498 */       this.resBundle = JdbcRowSetResourceBundle.getJdbcRowSetResourceBundle();
/*     */     } catch (IOException localIOException) {
/* 500 */       throw new RuntimeException(localIOException);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.rowset.internal.CachedRowSetReader
 * JD-Core Version:    0.6.2
 */