/*     */ package sun.jdbc.odbc.ee;
/*     */ 
/*     */ import java.sql.Connection;
/*     */ import java.sql.SQLException;
/*     */ import java.util.Properties;
/*     */ import javax.naming.NamingException;
/*     */ import javax.naming.Reference;
/*     */ import javax.naming.StringRefAddr;
/*     */ import javax.sql.PooledConnection;
/*     */ 
/*     */ public class ConnectionPoolDataSource extends CommonDataSource
/*     */   implements javax.sql.ConnectionPoolDataSource
/*     */ {
/*     */   private int maxStatements;
/*     */   private int initialPoolSize;
/*     */   private int minPoolSize;
/*     */   private int maxPoolSize;
/*     */   private int maxIdleTime;
/*     */   private int propertyCycle;
/*     */   private int timeoutFromPool;
/*     */   private int mInterval;
/* 100 */   private boolean shutdown = false;
/*     */   static final long serialVersionUID = 8730440750011279189L;
/*     */ 
/*     */   public ConnectionPoolDataSource()
/*     */   {
/*     */   }
/*     */ 
/*     */   public ConnectionPoolDataSource(String paramString)
/*     */   {
/* 114 */     super.setDataSourceName(paramString);
/*     */   }
/*     */ 
/*     */   public Connection getConnection()
/*     */     throws SQLException
/*     */   {
/* 124 */     return getPooledConnection().getConnection();
/*     */   }
/*     */ 
/*     */   public Connection getConnection(String paramString1, String paramString2)
/*     */     throws SQLException
/*     */   {
/* 136 */     return getPooledConnection(paramString1, paramString2).getConnection();
/*     */   }
/*     */ 
/*     */   public PooledConnection getPooledConnection()
/*     */     throws SQLException
/*     */   {
/* 146 */     return (PooledConnection)getPool().checkOut();
/*     */   }
/*     */ 
/*     */   public PooledConnection getPooledConnection(String paramString1, String paramString2)
/*     */     throws SQLException
/*     */   {
/* 156 */     Properties localProperties = super.getAttributes().getProperties();
/* 157 */     localProperties.put("user", paramString1);
/* 158 */     localProperties.put("password", paramString2);
/* 159 */     return (PooledConnection)getPool().checkOut(localProperties);
/*     */   }
/*     */ 
/*     */   public void setMaxStatements(String paramString)
/*     */     throws SQLException
/*     */   {
/*     */   }
/*     */ 
/*     */   public int getMaxStatements()
/*     */   {
/* 181 */     return this.maxStatements;
/*     */   }
/*     */ 
/*     */   public void setInitialPoolSize(String paramString)
/*     */     throws SQLException
/*     */   {
/* 191 */     if (paramString == null)
/* 192 */       throw new SQLException("Initial pool size cannot be null");
/*     */     try
/*     */     {
/* 195 */       this.initialPoolSize = Integer.parseInt(paramString.trim());
/*     */     } catch (NumberFormatException localNumberFormatException) {
/* 197 */       throw new SQLException("Initial pool size is not a number ");
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getInitialPoolSize()
/*     */   {
/* 207 */     return this.initialPoolSize;
/*     */   }
/*     */ 
/*     */   public void setMaxPoolSize(String paramString)
/*     */     throws SQLException
/*     */   {
/* 218 */     if (paramString == null)
/* 219 */       throw new SQLException("Max pool size cannot be null");
/*     */     try
/*     */     {
/* 222 */       this.maxPoolSize = Integer.parseInt(paramString.trim());
/*     */     } catch (NumberFormatException localNumberFormatException) {
/* 224 */       throw new SQLException("Max pool size is not a number ");
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getMaxPoolSize()
/*     */   {
/* 234 */     return this.maxPoolSize;
/*     */   }
/*     */ 
/*     */   public void setMinPoolSize(String paramString)
/*     */     throws SQLException
/*     */   {
/* 245 */     if (paramString == null)
/* 246 */       throw new SQLException("Min pool size cannot be null");
/*     */     try
/*     */     {
/* 249 */       this.minPoolSize = Integer.parseInt(paramString.trim());
/*     */     } catch (NumberFormatException localNumberFormatException) {
/* 251 */       throw new SQLException("Min pool size is not a number ");
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getMinPoolSize()
/*     */   {
/* 261 */     return this.minPoolSize;
/*     */   }
/*     */ 
/*     */   public void setMaxIdleTime(String paramString)
/*     */     throws SQLException
/*     */   {
/* 272 */     if (paramString == null)
/* 273 */       throw new SQLException("Idle time cannot be null");
/*     */     try
/*     */     {
/* 276 */       this.maxIdleTime = Integer.parseInt(paramString.trim());
/*     */     } catch (NumberFormatException localNumberFormatException) {
/* 278 */       throw new SQLException("Max Idle time is not a number ");
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getMaxIdleTime()
/*     */   {
/* 288 */     return this.maxIdleTime;
/*     */   }
/*     */ 
/*     */   public void setPropertyCycle(String paramString)
/*     */   {
/*     */   }
/*     */ 
/*     */   public int getPropertyCycle()
/*     */   {
/* 309 */     return this.propertyCycle;
/*     */   }
/*     */ 
/*     */   public void setTimeoutFromPool(String paramString)
/*     */     throws SQLException
/*     */   {
/* 321 */     if (paramString == null)
/* 322 */       throw new SQLException("timeout cannot be null");
/*     */     try
/*     */     {
/* 325 */       this.timeoutFromPool = Integer.parseInt(paramString.trim());
/*     */     } catch (NumberFormatException localNumberFormatException) {
/* 327 */       throw new SQLException("Timeout is not a number ");
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getTimeoutFromPool()
/*     */   {
/* 337 */     return this.timeoutFromPool;
/*     */   }
/*     */ 
/*     */   public void setMaintenanceInterval(String paramString)
/*     */     throws SQLException
/*     */   {
/* 349 */     if (paramString == null)
/* 350 */       throw new SQLException("Maintenance interval cannot be null");
/*     */     try
/*     */     {
/* 353 */       this.mInterval = Integer.parseInt(paramString.trim());
/*     */     } catch (NumberFormatException localNumberFormatException) {
/* 355 */       throw new SQLException("Maintenance interval is not a number ");
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getMaintenanceInterval()
/*     */   {
/* 365 */     return this.mInterval;
/*     */   }
/*     */ 
/*     */   public Reference getReference()
/*     */     throws NamingException
/*     */   {
/* 375 */     Reference localReference = new Reference(getClass().getName(), "sun.jdbc.odbc.ee.ObjectFactory", null);
/*     */ 
/* 378 */     localReference.add(new StringRefAddr("databaseName", super.getDatabaseName()));
/* 379 */     localReference.add(new StringRefAddr("dataSourceName", super.getDataSourceName()));
/*     */ 
/* 381 */     ConnectionAttributes localConnectionAttributes = super.getAttributes();
/*     */ 
/* 383 */     localReference.add(new StringRefAddr("user", localConnectionAttributes.getUser()));
/* 384 */     localReference.add(new StringRefAddr("password", localConnectionAttributes.getPassword()));
/* 385 */     localReference.add(new StringRefAddr("charSet", localConnectionAttributes.getCharSet()));
/* 386 */     localReference.add(new StringRefAddr("loginTimeout", "" + super.getLoginTimeout()));
/* 387 */     localReference.add(new StringRefAddr("maxStatements", "" + this.maxStatements));
/* 388 */     localReference.add(new StringRefAddr("initialPoolSize", "" + this.initialPoolSize));
/* 389 */     localReference.add(new StringRefAddr("maxPoolSize", "" + this.maxPoolSize));
/* 390 */     localReference.add(new StringRefAddr("minPoolSize", "" + this.minPoolSize));
/* 391 */     localReference.add(new StringRefAddr("maxIdleTime", "" + this.maxIdleTime));
/* 392 */     localReference.add(new StringRefAddr("propertyCycle", "" + this.propertyCycle));
/* 393 */     localReference.add(new StringRefAddr("timeoutFromPool", "" + this.timeoutFromPool));
/* 394 */     localReference.add(new StringRefAddr("mInterval", "" + this.mInterval));
/* 395 */     return localReference;
/*     */   }
/*     */ 
/*     */   public void shutDown(boolean paramBoolean)
/*     */   {
/* 405 */     ConnectionPool localConnectionPool = ConnectionPoolFactory.obtainConnectionPool(getDataSourceName());
/* 406 */     localConnectionPool.shutDown(paramBoolean);
/* 407 */     this.shutdown = true;
/*     */   }
/*     */ 
/*     */   private ConnectionPool getPool()
/*     */     throws SQLException
/*     */   {
/* 417 */     if (this.shutdown) {
/* 418 */       throw new SQLException("Pool is shutdown!");
/*     */     }
/* 420 */     ConnectionPool localConnectionPool = ConnectionPoolFactory.obtainConnectionPool(super.getDataSourceName());
/* 421 */     localConnectionPool.setTracer(super.getTracer());
/*     */ 
/* 423 */     PoolProperties localPoolProperties = new PoolProperties();
/* 424 */     localPoolProperties.set("initialPoolSize", this.initialPoolSize);
/* 425 */     localPoolProperties.set("maxPoolSize", this.maxPoolSize);
/* 426 */     localPoolProperties.set("minPoolSize", this.minPoolSize);
/* 427 */     localPoolProperties.set("maxIdleTime", this.maxIdleTime);
/* 428 */     localPoolProperties.set("timeOutFromPool", this.timeoutFromPool);
/* 429 */     localPoolProperties.set("mInterval", this.mInterval);
/*     */ 
/* 431 */     localConnectionPool.setProperties(localPoolProperties);
/* 432 */     localConnectionPool.setConnectionDetails(super.getAttributes().getProperties());
/* 433 */     localConnectionPool.initializePool();
/* 434 */     return localConnectionPool;
/*     */   }
/*     */ 
/*     */   public <T> T unwrap(Class<T> paramClass)
/*     */     throws SQLException
/*     */   {
/* 451 */     return null;
/*     */   }
/*     */ 
/*     */   public boolean isWrapperFor(Class<?> paramClass)
/*     */     throws SQLException
/*     */   {
/* 470 */     return false;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.jdbc.odbc.ee.ConnectionPoolDataSource
 * JD-Core Version:    0.6.2
 */