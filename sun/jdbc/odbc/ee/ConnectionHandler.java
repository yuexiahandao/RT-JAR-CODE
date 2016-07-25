/*     */ package sun.jdbc.odbc.ee;
/*     */ 
/*     */ import java.sql.SQLException;
/*     */ import sun.jdbc.odbc.JdbcOdbc;
/*     */ import sun.jdbc.odbc.JdbcOdbcConnection;
/*     */ import sun.jdbc.odbc.JdbcOdbcDriverInterface;
/*     */ import sun.jdbc.odbc.JdbcOdbcTracer;
/*     */ 
/*     */ public class ConnectionHandler extends JdbcOdbcConnection
/*     */ {
/*     */   private JdbcOdbcConnection con;
/*  29 */   private JdbcOdbcTracer tracer = new JdbcOdbcTracer();
/*     */ 
/*  32 */   final int NOTOPEN = 0;
/*     */ 
/*  39 */   final int OPEN = 1;
/*     */ 
/*  45 */   final int CLOSING = 2;
/*     */ 
/*  48 */   final int CLOSED = 3;
/*     */ 
/*  51 */   final int DESTROYING = 4;
/*     */ 
/*  54 */   final int DESTROYED = 5;
/*     */ 
/*  57 */   private int state = 0;
/*     */   private PooledObject jpo;
/*     */ 
/*     */   public ConnectionHandler(JdbcOdbc paramJdbcOdbc, long paramLong, JdbcOdbcDriverInterface paramJdbcOdbcDriverInterface)
/*     */   {
/*  70 */     super(paramJdbcOdbc, paramLong, paramJdbcOdbcDriverInterface);
/*  71 */     this.tracer = paramJdbcOdbc.getTracer();
/*     */   }
/*     */ 
/*     */   public synchronized void close()
/*     */   {
/*  79 */     if (this.state != 1)
/*     */     {
/*  81 */       return;
/*     */     }
/*  83 */     this.state = 2;
/*     */     try {
/*  85 */       if (this.tracer.isTracing()) {
/*  86 */         this.tracer.trace("*Releasing all resources to this connection Connection.close");
/*     */       }
/*     */ 
/*  90 */       super.setFreeStmtsFromConnectionOnly();
/*  91 */       super.closeAllStatements();
/*  92 */       super.setFreeStmtsFromAnyWhere();
/*  93 */       if (this.tracer.isTracing()) {
/*  94 */         this.tracer.trace("*Releasing all resources to this connection Connection.close");
/*     */       }
/*  96 */       this.jpo.markUsable();
/*  97 */       this.state = 3;
/*  98 */       ((PooledConnection)this.jpo).connectionClosed();
/*     */     } catch (Exception localException) {
/* 100 */       this.tracer.trace("Error occured while closing the connection " + this + " " + localException.getMessage());
/* 101 */       ((PooledConnection)this.jpo).connectionErrorOccurred(new SQLException(localException.getMessage()));
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean isClosed()
/*     */     throws SQLException
/*     */   {
/* 111 */     return this.state != 1;
/*     */   }
/*     */ 
/*     */   public synchronized void actualClose()
/*     */     throws SQLException
/*     */   {
/* 121 */     if ((this.state == 4) || (this.state == 5))
/*     */     {
/* 123 */       return;
/*     */     }
/* 125 */     if (this.state == 1) {
/* 126 */       this.jpo.markForSweep();
/* 127 */       close();
/*     */     }
/* 129 */     this.state = 4;
/*     */     try {
/* 131 */       if (this.tracer.isTracing()) {
/* 132 */         this.tracer.trace("*Actual Connection.close");
/*     */       }
/* 134 */       super.close();
/* 135 */       this.state = 5;
/*     */     } catch (SQLException localSQLException) {
/* 137 */       this.state = 5;
/* 138 */       this.tracer.trace("Error occured while closing the connection " + this + " " + localSQLException.getMessage());
/* 139 */       throw localSQLException;
/*     */     } catch (Exception localException) {
/* 141 */       this.state = 5;
/* 142 */       this.tracer.trace("Error occured while closing the connection " + this + " " + localException.getMessage());
/* 143 */       throw new SQLException("Unexpected exception:" + localException.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   public void destroy()
/*     */     throws SQLException
/*     */   {
/* 154 */     if ((this.state == 4) || (this.state == 5))
/*     */     {
/* 156 */       return;
/*     */     }
/* 158 */     this.state = 4;
/*     */     try {
/* 160 */       if (this.tracer.isTracing()) {
/* 161 */         this.tracer.trace("*ConnectionHandler.destroy");
/*     */       }
/* 163 */       super.close();
/* 164 */       this.state = 5;
/*     */     } catch (SQLException localSQLException) {
/* 166 */       this.state = 5;
/* 167 */       this.tracer.trace("Error occured while closing the connection " + this + " " + localSQLException.getMessage());
/* 168 */       throw localSQLException;
/*     */     } catch (Exception localException) {
/* 170 */       this.state = 5;
/* 171 */       this.tracer.trace("Error occured while closing the connection " + this + " " + localException.getMessage());
/* 172 */       throw new SQLException("Unexpected exception:" + localException.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getState()
/*     */   {
/* 182 */     return this.state;
/*     */   }
/*     */ 
/*     */   public void setState(int paramInt)
/*     */   {
/* 191 */     this.state = paramInt;
/*     */   }
/*     */ 
/*     */   public void setPooledObject(PooledObject paramPooledObject)
/*     */   {
/* 200 */     this.jpo = paramPooledObject;
/*     */   }
/*     */ 
/*     */   public void finalize()
/*     */   {
/* 207 */     this.tracer.trace("Connectionhandler Finalize....");
/*     */     try {
/* 209 */       destroy();
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.jdbc.odbc.ee.ConnectionHandler
 * JD-Core Version:    0.6.2
 */