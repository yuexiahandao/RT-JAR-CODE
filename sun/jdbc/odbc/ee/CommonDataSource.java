/*     */ package sun.jdbc.odbc.ee;
/*     */ 
/*     */ import java.io.PrintWriter;
/*     */ import java.io.Serializable;
/*     */ import java.sql.Connection;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.SQLFeatureNotSupportedException;
/*     */ import java.sql.SQLPermission;
/*     */ import java.util.logging.Logger;
/*     */ import javax.naming.NamingException;
/*     */ import javax.naming.Reference;
/*     */ import javax.naming.Referenceable;
/*     */ import javax.sql.DataSource;
/*     */ import sun.jdbc.odbc.JdbcOdbcTracer;
/*     */ 
/*     */ public abstract class CommonDataSource
/*     */   implements DataSource, Referenceable, Serializable
/*     */ {
/*     */   private String strDbName;
/*     */   private String strDSName;
/*     */   private String strDesc;
/*     */   private String strUser;
/*     */   private String strPasswd;
/*     */   private int iPortNo;
/*     */   private String strRoleName;
/*     */   private String strCharSet;
/*     */   private int iLoginTimeout;
/*  62 */   private transient JdbcOdbcTracer tracer = new JdbcOdbcTracer();
/*     */ 
/*     */   public abstract Connection getConnection()
/*     */     throws SQLException;
/*     */ 
/*     */   public abstract Connection getConnection(String paramString1, String paramString2)
/*     */     throws SQLException;
/*     */ 
/*     */   public abstract Reference getReference()
/*     */     throws NamingException;
/*     */ 
/*     */   public ConnectionAttributes getAttributes()
/*     */   {
/* 104 */     return new ConnectionAttributes(this.strDbName, this.strUser, this.strPasswd, this.strCharSet, this.iLoginTimeout);
/*     */   }
/*     */ 
/*     */   public void setDatabaseName(String paramString)
/*     */   {
/* 113 */     this.strDbName = paramString;
/*     */   }
/*     */ 
/*     */   public String getDatabaseName()
/*     */   {
/* 122 */     return this.strDbName;
/*     */   }
/*     */ 
/*     */   public void setDataSourceName(String paramString)
/*     */   {
/* 131 */     this.strDSName = paramString;
/*     */   }
/*     */ 
/*     */   public String getDataSourceName()
/*     */   {
/* 140 */     return this.strDSName;
/*     */   }
/*     */ 
/*     */   public void setDescription(String paramString)
/*     */   {
/* 149 */     this.strDesc = paramString;
/*     */   }
/*     */ 
/*     */   public String getDescription()
/*     */     throws Exception
/*     */   {
/* 158 */     return this.strDesc;
/*     */   }
/*     */ 
/*     */   public void setPassword(String paramString)
/*     */   {
/* 167 */     this.strPasswd = paramString;
/*     */   }
/*     */ 
/*     */   public String getPassword()
/*     */   {
/* 176 */     return this.strPasswd;
/*     */   }
/*     */ 
/*     */   public void setPortNumber(int paramInt)
/*     */   {
/* 187 */     this.iPortNo = paramInt;
/*     */   }
/*     */ 
/*     */   public int getPortNumber()
/*     */   {
/* 196 */     return this.iPortNo;
/*     */   }
/*     */ 
/*     */   public void setRoleName(String paramString)
/*     */   {
/* 207 */     this.strRoleName = paramString;
/*     */   }
/*     */ 
/*     */   public String getRoleName()
/*     */   {
/* 216 */     return this.strRoleName;
/*     */   }
/*     */ 
/*     */   public void setCharSet(String paramString)
/*     */   {
/* 229 */     this.strCharSet = paramString;
/*     */   }
/*     */ 
/*     */   public String getCharSet()
/*     */   {
/* 238 */     return this.strCharSet;
/*     */   }
/*     */ 
/*     */   public void setUser(String paramString)
/*     */   {
/* 247 */     this.strUser = paramString;
/*     */   }
/*     */ 
/*     */   public String getUser()
/*     */   {
/* 256 */     return this.strUser;
/*     */   }
/*     */ 
/*     */   public void setLoginTimeout(int paramInt)
/*     */   {
/* 266 */     this.iLoginTimeout = paramInt;
/*     */   }
/*     */ 
/*     */   public int getLoginTimeout()
/*     */   {
/* 275 */     return this.iLoginTimeout;
/*     */   }
/*     */ 
/*     */   public void setLogWriter(PrintWriter paramPrintWriter)
/*     */   {
/* 284 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 285 */     if (localSecurityManager != null) {
/* 286 */       localSecurityManager.checkPermission(new SQLPermission("setLog"));
/*     */     }
/*     */ 
/* 289 */     if (this.tracer == null) {
/* 290 */       this.tracer = new JdbcOdbcTracer();
/*     */     }
/* 292 */     this.tracer.setWriter(paramPrintWriter);
/*     */   }
/*     */ 
/*     */   public PrintWriter getLogWriter()
/*     */   {
/* 302 */     if (this.tracer == null) {
/* 303 */       return null;
/*     */     }
/* 305 */     return this.tracer.getWriter();
/*     */   }
/*     */ 
/*     */   public JdbcOdbcTracer getTracer()
/*     */   {
/* 317 */     if (this.tracer == null) {
/* 318 */       this.tracer = new JdbcOdbcTracer();
/*     */     }
/* 320 */     return this.tracer;
/*     */   }
/*     */ 
/*     */   public Logger getParentLogger()
/*     */     throws SQLFeatureNotSupportedException
/*     */   {
/* 337 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.jdbc.odbc.ee.CommonDataSource
 * JD-Core Version:    0.6.2
 */