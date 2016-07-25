/*    */ package sun.jdbc.odbc.ee;
/*    */ 
/*    */ import java.util.Hashtable;
/*    */ 
/*    */ public class ConnectionPoolFactory
/*    */ {
/*    */   private static Hashtable pools;
/*    */ 
/*    */   public static ConnectionPool obtainConnectionPool(String paramString)
/*    */   {
/* 29 */     if (pools == null) {
/* 30 */       pools = new Hashtable();
/*    */     }
/* 32 */     if ((pools.containsKey(paramString)) && (pools.get(paramString) != null)) {
/* 33 */       return (ConnectionPool)pools.get(paramString);
/*    */     }
/* 35 */     ConnectionPool localConnectionPool = new ConnectionPool(paramString);
/* 36 */     pools.put(paramString, localConnectionPool);
/* 37 */     return localConnectionPool;
/*    */   }
/*    */ 
/*    */   public static void removePool(String paramString)
/*    */   {
/* 47 */     pools.remove(paramString);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.jdbc.odbc.ee.ConnectionPoolFactory
 * JD-Core Version:    0.6.2
 */