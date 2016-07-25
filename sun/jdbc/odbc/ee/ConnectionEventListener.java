/*    */ package sun.jdbc.odbc.ee;
/*    */ 
/*    */ import java.sql.SQLException;
/*    */ import javax.sql.ConnectionEvent;
/*    */ 
/*    */ public class ConnectionEventListener
/*    */   implements javax.sql.ConnectionEventListener
/*    */ {
/*    */   private PooledObject objPool;
/*    */   private String name;
/*    */ 
/*    */   public ConnectionEventListener(String paramString)
/*    */   {
/* 38 */     this.name = paramString;
/*    */   }
/*    */ 
/*    */   public void connectionClosed(ConnectionEvent paramConnectionEvent)
/*    */   {
/* 49 */     Object localObject = paramConnectionEvent.getSource();
/* 50 */     this.objPool = ((PooledObject)localObject);
/* 51 */     ConnectionPool localConnectionPool = ConnectionPoolFactory.obtainConnectionPool(this.name);
/* 52 */     localConnectionPool.checkIn(this.objPool);
/*    */   }
/*    */ 
/*    */   public void connectionErrorOccurred(ConnectionEvent paramConnectionEvent)
/*    */   {
/* 63 */     Object localObject = paramConnectionEvent.getSource();
/* 64 */     this.objPool = ((PooledObject)localObject);
/* 65 */     this.objPool.markForSweep();
/* 66 */     ConnectionPool localConnectionPool = ConnectionPoolFactory.obtainConnectionPool(this.name);
/* 67 */     localConnectionPool.checkIn(this.objPool);
/*    */   }
/*    */ 
/*    */   public void connectionCheckOut(ConnectionEvent paramConnectionEvent)
/*    */     throws SQLException
/*    */   {
/* 79 */     Object localObject = paramConnectionEvent.getSource();
/* 80 */     this.objPool = ((PooledObject)localObject);
/* 81 */     ConnectionPool localConnectionPool = ConnectionPoolFactory.obtainConnectionPool(this.name);
/* 82 */     localConnectionPool.tryCheckOut(this.objPool);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.jdbc.odbc.ee.ConnectionEventListener
 * JD-Core Version:    0.6.2
 */