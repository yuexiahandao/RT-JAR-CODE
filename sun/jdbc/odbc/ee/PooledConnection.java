/*     */ package sun.jdbc.odbc.ee;
/*     */ 
/*     */ import java.sql.Connection;
/*     */ import java.sql.SQLException;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Properties;
/*     */ import javax.sql.ConnectionEvent;
/*     */ import javax.sql.StatementEventListener;
/*     */ import sun.jdbc.odbc.JdbcOdbcDriver;
/*     */ import sun.jdbc.odbc.JdbcOdbcTracer;
/*     */ 
/*     */ public class PooledConnection
/*     */   implements javax.sql.PooledConnection, PooledObject
/*     */ {
/*  23 */   private String strUserId = null;
/*     */ 
/*  26 */   private String strPassword = null;
/*     */ 
/*  29 */   private String strUrl = null;
/*     */   private String strCharset;
/*     */   private int timeout;
/*  38 */   private Properties pr = null;
/*     */   private ConnectionHandler conHandler;
/*  44 */   private boolean isAvailableForUse = true;
/*     */   private Hashtable htListener;
/*  53 */   private long time = 0L;
/*     */ 
/*  56 */   private JdbcOdbcTracer tracer = new JdbcOdbcTracer();
/*     */ 
/*  59 */   private JdbcOdbcDriver driver = null;
/*     */   private int state;
/*     */   private ConnectionEventListener listener;
/*     */ 
/*     */   public PooledConnection(Properties paramProperties, JdbcOdbcTracer paramJdbcOdbcTracer)
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/*  76 */       this.tracer = paramJdbcOdbcTracer;
/*  77 */       this.strUserId = ((String)paramProperties.get("user"));
/*  78 */       this.strPassword = ((String)paramProperties.get("password"));
/*  79 */       this.strUrl = ((String)paramProperties.get("url"));
/*  80 */       this.strCharset = ((String)paramProperties.get("charset"));
/*  81 */       this.timeout = Integer.parseInt((String)paramProperties.get("loginTimeout"));
/*  82 */       this.pr = paramProperties;
/*  83 */       this.time = System.currentTimeMillis();
/*  84 */       this.htListener = new Hashtable();
/*  85 */       this.driver = new JdbcOdbcDriver();
/*  86 */       this.driver.setTimeOut(this.timeout);
/*  87 */       this.driver.setWriter(paramJdbcOdbcTracer.getWriter());
/*  88 */       paramJdbcOdbcTracer.trace(" PooledConnection Being created ...." + this.strUserId + ":" + this.strPassword + ":" + this.strUrl + ":" + this.driver);
/*  89 */       this.conHandler = ((ConnectionHandler)this.driver.EEConnect(this.strUrl, paramProperties));
/*  90 */       this.conHandler.setPooledObject(this);
/*     */     } catch (SQLException localSQLException) {
/*  92 */       throw localSQLException;
/*     */     } catch (Exception localException) {
/*  94 */       throw new SQLException("Error in creating pooled connection" + localException.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean isMatching(Properties paramProperties)
/*     */   {
/* 105 */     return this.pr.equals(paramProperties);
/*     */   }
/*     */ 
/*     */   public boolean isUsable()
/*     */   {
/* 114 */     return this.isAvailableForUse;
/*     */   }
/*     */ 
/*     */   public void markForSweep()
/*     */   {
/* 121 */     this.state = 3;
/*     */   }
/*     */ 
/*     */   public boolean isMarkedForSweep()
/*     */   {
/* 130 */     return this.state == 3;
/*     */   }
/*     */ 
/*     */   public void markUsable()
/*     */   {
/* 137 */     this.isAvailableForUse = true;
/*     */   }
/*     */ 
/*     */   public long getCreatedTime()
/*     */   {
/* 146 */     return this.time;
/*     */   }
/*     */ 
/*     */   public void addConnectionEventListener(javax.sql.ConnectionEventListener paramConnectionEventListener)
/*     */   {
/* 155 */     this.htListener.put(paramConnectionEventListener, "");
/* 156 */     if ((paramConnectionEventListener instanceof ConnectionEventListener))
/* 157 */       this.listener = ((ConnectionEventListener)paramConnectionEventListener);
/*     */   }
/*     */ 
/*     */   public Connection getConnection()
/*     */     throws SQLException
/*     */   {
/* 169 */     this.conHandler.getClass(); if (this.conHandler.getState() != 3) { this.conHandler.getClass(); if (this.conHandler.getState() != 0)
/*     */       {
/* 171 */         throw new SQLException("Connection is not available now!");
/*     */       } }
/* 173 */     if (this.state == 3) {
/* 174 */       throw new SQLException("PooledConnection is not usable");
/*     */     }
/* 176 */     if (this.state == 1) {
/* 177 */       this.listener.connectionCheckOut(new ConnectionEvent(this));
/*     */     }
/* 179 */     this.isAvailableForUse = false;
/* 180 */     this.conHandler.getClass(); this.conHandler.setState(1);
/* 181 */     return this.conHandler;
/*     */   }
/*     */ 
/*     */   public void removeConnectionEventListener(javax.sql.ConnectionEventListener paramConnectionEventListener)
/*     */   {
/* 190 */     this.htListener.remove(paramConnectionEventListener);
/*     */   }
/*     */ 
/*     */   public void close()
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 200 */       this.isAvailableForUse = false;
/* 201 */       this.state = 3;
/* 202 */       this.conHandler.actualClose();
/*     */     } catch (SQLException localSQLException) {
/* 204 */       throw localSQLException;
/*     */     } catch (Exception localException) {
/* 206 */       throw new SQLException("Unexpected Exception : " + localException.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   public void destroy()
/*     */   {
/*     */     try
/*     */     {
/* 215 */       this.isAvailableForUse = false;
/* 216 */       this.state = 3;
/* 217 */       this.conHandler.destroy();
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   public void connectionClosed()
/*     */   {
/* 227 */     Enumeration localEnumeration = this.htListener.keys();
/* 228 */     while (localEnumeration.hasMoreElements()) {
/* 229 */       javax.sql.ConnectionEventListener localConnectionEventListener = (javax.sql.ConnectionEventListener)localEnumeration.nextElement();
/* 230 */       ConnectionEvent localConnectionEvent = new ConnectionEvent(this);
/* 231 */       localConnectionEventListener.connectionClosed(localConnectionEvent);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void connectionErrorOccurred(SQLException paramSQLException)
/*     */   {
/* 241 */     Enumeration localEnumeration = this.htListener.keys();
/* 242 */     while (localEnumeration.hasMoreElements()) {
/* 243 */       javax.sql.ConnectionEventListener localConnectionEventListener = (javax.sql.ConnectionEventListener)localEnumeration.nextElement();
/* 244 */       ConnectionEvent localConnectionEvent = new ConnectionEvent(this, paramSQLException);
/* 245 */       localConnectionEventListener.connectionErrorOccurred(localConnectionEvent);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void checkedOut()
/*     */   {
/* 253 */     if (this.state != 3)
/* 254 */       this.state = 2;
/*     */   }
/*     */ 
/*     */   public void checkedIn()
/*     */   {
/* 262 */     if (this.state != 3)
/* 263 */       this.state = 1;
/*     */   }
/*     */ 
/*     */   public void addConnectionEventListener(ConnectionEventListener paramConnectionEventListener)
/*     */   {
/* 279 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public void removeConnectionEventListener(ConnectionEventListener paramConnectionEventListener)
/*     */   {
/* 296 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public void addStatementEventListener(StatementEventListener paramStatementEventListener)
/*     */   {
/* 311 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public void removeStatementEventListener(StatementEventListener paramStatementEventListener)
/*     */   {
/* 326 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.jdbc.odbc.ee.PooledConnection
 * JD-Core Version:    0.6.2
 */