/*    */ package javax.sql;
/*    */ 
/*    */ import java.sql.SQLException;
/*    */ import java.util.EventObject;
/*    */ 
/*    */ public class ConnectionEvent extends EventObject
/*    */ {
/* 87 */   private SQLException ex = null;
/*    */   static final long serialVersionUID = -4843217645290030002L;
/*    */ 
/*    */   public ConnectionEvent(PooledConnection paramPooledConnection)
/*    */   {
/* 56 */     super(paramPooledConnection);
/*    */   }
/*    */ 
/*    */   public ConnectionEvent(PooledConnection paramPooledConnection, SQLException paramSQLException)
/*    */   {
/* 69 */     super(paramPooledConnection);
/* 70 */     this.ex = paramSQLException;
/*    */   }
/*    */ 
/*    */   public SQLException getSQLException()
/*    */   {
/* 79 */     return this.ex;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.sql.ConnectionEvent
 * JD-Core Version:    0.6.2
 */