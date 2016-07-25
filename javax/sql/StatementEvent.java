/*     */ package javax.sql;
/*     */ 
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.SQLException;
/*     */ import java.util.EventObject;
/*     */ 
/*     */ public class StatementEvent extends EventObject
/*     */ {
/*     */   private SQLException exception;
/*     */   private PreparedStatement statement;
/*     */ 
/*     */   public StatementEvent(PooledConnection paramPooledConnection, PreparedStatement paramPreparedStatement)
/*     */   {
/*  64 */     super(paramPooledConnection);
/*     */ 
/*  66 */     this.statement = paramPreparedStatement;
/*  67 */     this.exception = null;
/*     */   }
/*     */ 
/*     */   public StatementEvent(PooledConnection paramPooledConnection, PreparedStatement paramPreparedStatement, SQLException paramSQLException)
/*     */   {
/*  88 */     super(paramPooledConnection);
/*     */ 
/*  90 */     this.statement = paramPreparedStatement;
/*  91 */     this.exception = paramSQLException;
/*     */   }
/*     */ 
/*     */   public PreparedStatement getStatement()
/*     */   {
/* 103 */     return this.statement;
/*     */   }
/*     */ 
/*     */   public SQLException getSQLException()
/*     */   {
/* 115 */     return this.exception;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.sql.StatementEvent
 * JD-Core Version:    0.6.2
 */