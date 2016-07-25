/*    */ package sun.jdbc.odbc.ee;
/*    */ 
/*    */ import java.util.Hashtable;
/*    */ import javax.naming.Context;
/*    */ import javax.naming.Name;
/*    */ import javax.naming.NamingException;
/*    */ import javax.naming.RefAddr;
/*    */ import javax.naming.Reference;
/*    */ 
/*    */ public class ObjectFactory
/*    */   implements javax.naming.spi.ObjectFactory
/*    */ {
/*    */   public Object getObjectInstance(Object paramObject, Name paramName, Context paramContext, Hashtable<?, ?> paramHashtable)
/*    */     throws Exception
/*    */   {
/* 34 */     Reference localReference = (Reference)paramObject;
/* 35 */     String str1 = localReference.getClassName();
/* 36 */     String str2 = (String)localReference.get("databaseName").getContent();
/* 37 */     String str3 = (String)localReference.get("dataSourceName").getContent();
/* 38 */     String str4 = (String)localReference.get("user").getContent();
/* 39 */     String str5 = (String)localReference.get("password").getContent();
/* 40 */     String str6 = (String)localReference.get("charSet").getContent();
/* 41 */     int i = Integer.parseInt((String)localReference.get("loginTimeout").getContent());
/*    */     Object localObject;
/* 43 */     if (str1.equals("sun.jdbc.odbc.ee.DataSource")) {
/* 44 */       localObject = new DataSource();
/* 45 */       if (str2 != null) ((DataSource)localObject).setDatabaseName(str2);
/* 46 */       if (str3 != null) ((DataSource)localObject).setDataSourceName(str3);
/* 47 */       if (str4 != null) ((DataSource)localObject).setUser(str4);
/* 48 */       if (str5 != null) ((DataSource)localObject).setPassword(str5);
/* 49 */       if (str6 != null) ((DataSource)localObject).setCharSet(str6);
/* 50 */       ((DataSource)localObject).setLoginTimeout(i);
/* 51 */       return localObject;
/*    */     }
/*    */ 
/* 54 */     if (str1.equals("sun.jdbc.odbc.ee.ConnectionPoolDataSource")) {
/* 55 */       if (str3 == null) {
/* 56 */         throw new NamingException("Datasource Name is null for a connection pool");
/*    */       }
/* 58 */       localObject = new ConnectionPoolDataSource(str3);
/* 59 */       String str7 = (String)localReference.get("maxStatements").getContent();
/* 60 */       String str8 = (String)localReference.get("initialPoolSize").getContent();
/* 61 */       String str9 = (String)localReference.get("minPoolSize").getContent();
/* 62 */       String str10 = (String)localReference.get("maxPoolSize").getContent();
/* 63 */       String str11 = (String)localReference.get("maxIdleTime").getContent();
/* 64 */       String str12 = (String)localReference.get("propertyCycle").getContent();
/* 65 */       String str13 = (String)localReference.get("timeoutFromPool").getContent();
/* 66 */       String str14 = (String)localReference.get("mInterval").getContent();
/* 67 */       if (str2 != null) ((ConnectionPoolDataSource)localObject).setDatabaseName(str2);
/* 68 */       if (str4 != null) ((ConnectionPoolDataSource)localObject).setUser(str4);
/* 69 */       if (str5 != null) ((ConnectionPoolDataSource)localObject).setPassword(str5);
/* 70 */       if (str6 != null) ((ConnectionPoolDataSource)localObject).setCharSet(str6);
/* 71 */       ((ConnectionPoolDataSource)localObject).setLoginTimeout(i);
/* 72 */       ((ConnectionPoolDataSource)localObject).setMaxStatements(str7);
/* 73 */       ((ConnectionPoolDataSource)localObject).setInitialPoolSize(str8);
/* 74 */       ((ConnectionPoolDataSource)localObject).setMinPoolSize(str9);
/* 75 */       ((ConnectionPoolDataSource)localObject).setMaxPoolSize(str10);
/* 76 */       ((ConnectionPoolDataSource)localObject).setMaxIdleTime(str11);
/* 77 */       ((ConnectionPoolDataSource)localObject).setPropertyCycle(str12);
/* 78 */       ((ConnectionPoolDataSource)localObject).setTimeoutFromPool(str13);
/* 79 */       ((ConnectionPoolDataSource)localObject).setMaintenanceInterval(str14);
/* 80 */       return localObject;
/*    */     }
/*    */ 
/* 83 */     if (str1.equals("sun.jdbc.odbc.ee.XADataSource"));
/* 87 */     return null;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.jdbc.odbc.ee.ObjectFactory
 * JD-Core Version:    0.6.2
 */