/*     */ package sun.jdbc.odbc.ee;
/*     */ 
/*     */ import java.sql.Connection;
/*     */ import java.sql.SQLException;
/*     */ import java.util.Properties;
/*     */ import javax.naming.NamingException;
/*     */ import javax.naming.Reference;
/*     */ import javax.naming.StringRefAddr;
/*     */ import sun.jdbc.odbc.JdbcOdbcDriver;
/*     */ 
/*     */ public class DataSource extends CommonDataSource
/*     */ {
/*  51 */   private ConnectionAttributes attrib = null;
/*     */   static final long serialVersionUID = -7768089779584724575L;
/*     */ 
/*     */   public Connection getConnection()
/*     */     throws SQLException
/*     */   {
/*  59 */     this.attrib = super.getAttributes();
/*  60 */     JdbcOdbcDriver localJdbcOdbcDriver = new JdbcOdbcDriver();
/*  61 */     localJdbcOdbcDriver.setTimeOut(super.getLoginTimeout());
/*  62 */     localJdbcOdbcDriver.setWriter(super.getLogWriter());
/*  63 */     return localJdbcOdbcDriver.connect(this.attrib.getUrl(), this.attrib.getProperties());
/*     */   }
/*     */ 
/*     */   public Connection getConnection(String paramString1, String paramString2)
/*     */     throws SQLException
/*     */   {
/*  75 */     this.attrib = super.getAttributes();
/*  76 */     JdbcOdbcDriver localJdbcOdbcDriver = new JdbcOdbcDriver();
/*  77 */     localJdbcOdbcDriver.setTimeOut(super.getLoginTimeout());
/*  78 */     localJdbcOdbcDriver.setWriter(super.getLogWriter());
/*  79 */     Properties localProperties = this.attrib.getProperties();
/*  80 */     localProperties.put("user", paramString1);
/*  81 */     localProperties.put("password", paramString2);
/*  82 */     return localJdbcOdbcDriver.connect(this.attrib.getUrl(), localProperties);
/*     */   }
/*     */ 
/*     */   public Reference getReference()
/*     */     throws NamingException
/*     */   {
/*  92 */     Reference localReference = new Reference(getClass().getName(), "sun.jdbc.odbc.ee.ObjectFactory", null);
/*     */ 
/*  94 */     localReference.add(new StringRefAddr("databaseName", super.getDatabaseName()));
/*  95 */     localReference.add(new StringRefAddr("dataSourceName", super.getDataSourceName()));
/*  96 */     localReference.add(new StringRefAddr("user", super.getUser()));
/*  97 */     localReference.add(new StringRefAddr("password", super.getPassword()));
/*  98 */     localReference.add(new StringRefAddr("charSet", super.getCharSet()));
/*  99 */     localReference.add(new StringRefAddr("loginTimeout", "" + super.getLoginTimeout()));
/* 100 */     return localReference;
/*     */   }
/*     */ 
/*     */   public <T> T unwrap(Class<T> paramClass)
/*     */     throws SQLException
/*     */   {
/* 117 */     return null;
/*     */   }
/*     */ 
/*     */   public boolean isWrapperFor(Class<?> paramClass)
/*     */     throws SQLException
/*     */   {
/* 136 */     return false;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.jdbc.odbc.ee.DataSource
 * JD-Core Version:    0.6.2
 */