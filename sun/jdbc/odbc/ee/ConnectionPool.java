/*    */ package sun.jdbc.odbc.ee;
/*    */ 
/*    */ import java.sql.SQLException;
/*    */ import java.util.Hashtable;
/*    */ import java.util.Properties;
/*    */ 
/*    */ public class ConnectionPool extends ObjectPool
/*    */ {
/*    */   private ConnectionEventListener cel;
/*    */   Properties cp;
/*    */ 
/*    */   public ConnectionPool(String paramString)
/*    */   {
/* 44 */     super(paramString);
/* 45 */     this.cel = new ConnectionEventListener(paramString);
/*    */   }
/*    */ 
/*    */   public void setConnectionDetails(Properties paramProperties)
/*    */   {
/* 55 */     this.cp = paramProperties;
/*    */   }
/*    */ 
/*    */   protected PooledObject create(Properties paramProperties)
/*    */     throws SQLException
/*    */   {
/* 69 */     Properties localProperties = null;
/* 70 */     if (paramProperties != null)
/* 71 */       localProperties = paramProperties;
/*    */     else {
/* 73 */       localProperties = this.cp;
/*    */     }
/* 75 */     PooledConnection localPooledConnection = new PooledConnection(localProperties, super.getTracer());
/* 76 */     localPooledConnection.addConnectionEventListener(this.cel);
/* 77 */     return localPooledConnection;
/*    */   }
/*    */ 
/*    */   protected void destroyFromPool(PooledObject paramPooledObject, Hashtable paramHashtable)
/*    */   {
/* 88 */     super.destroyFromPool(paramPooledObject, paramHashtable);
/* 89 */     ((PooledConnection)paramPooledObject).removeConnectionEventListener(this.cel);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.jdbc.odbc.ee.ConnectionPool
 * JD-Core Version:    0.6.2
 */