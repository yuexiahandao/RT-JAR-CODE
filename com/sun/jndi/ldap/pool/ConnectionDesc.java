/*     */ package com.sun.jndi.ldap.pool;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ 
/*     */ final class ConnectionDesc
/*     */ {
/*  37 */   private static final boolean debug = Pool.debug;
/*     */   static final byte BUSY = 0;
/*     */   static final byte IDLE = 1;
/*     */   static final byte EXPIRED = 2;
/*     */   private final PooledConnection conn;
/*  46 */   private byte state = 1;
/*     */   private long idleSince;
/*  48 */   private long useCount = 0L;
/*     */ 
/*     */   ConnectionDesc(PooledConnection paramPooledConnection) {
/*  51 */     this.conn = paramPooledConnection;
/*     */   }
/*     */ 
/*     */   ConnectionDesc(PooledConnection paramPooledConnection, boolean paramBoolean) {
/*  55 */     this.conn = paramPooledConnection;
/*  56 */     if (paramBoolean) {
/*  57 */       this.state = 0;
/*  58 */       this.useCount += 1L;
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/*  68 */     return (paramObject != null) && ((paramObject instanceof ConnectionDesc)) && (((ConnectionDesc)paramObject).conn == this.conn);
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/*  78 */     return this.conn.hashCode();
/*     */   }
/*     */ 
/*     */   synchronized boolean release()
/*     */   {
/*  87 */     d("release()");
/*  88 */     if (this.state == 0) {
/*  89 */       this.state = 1;
/*     */ 
/*  91 */       this.idleSince = System.currentTimeMillis();
/*  92 */       return true;
/*     */     }
/*  94 */     return false;
/*     */   }
/*     */ 
/*     */   synchronized PooledConnection tryUse()
/*     */   {
/* 105 */     d("tryUse()");
/*     */ 
/* 107 */     if (this.state == 1) {
/* 108 */       this.state = 0;
/* 109 */       this.useCount += 1L;
/* 110 */       return this.conn;
/*     */     }
/*     */ 
/* 113 */     return null;
/*     */   }
/*     */ 
/*     */   synchronized boolean expire(long paramLong)
/*     */   {
/* 126 */     if ((this.state == 1) && (this.idleSince < paramLong))
/*     */     {
/* 128 */       d("expire(): expired");
/*     */ 
/* 130 */       this.state = 2;
/* 131 */       this.conn.closeConnection();
/*     */ 
/* 133 */       return true;
/*     */     }
/* 135 */     d("expire(): not expired");
/* 136 */     return false;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 141 */     return this.conn.toString() + " " + (this.state == 1 ? "idle" : this.state == 0 ? "busy" : "expired");
/*     */   }
/*     */ 
/*     */   int getState()
/*     */   {
/* 147 */     return this.state;
/*     */   }
/*     */ 
/*     */   long getUseCount()
/*     */   {
/* 152 */     return this.useCount;
/*     */   }
/*     */ 
/*     */   private void d(String paramString) {
/* 156 */     if (debug)
/* 157 */       System.err.println("ConnectionDesc." + paramString + " " + toString());
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.ldap.pool.ConnectionDesc
 * JD-Core Version:    0.6.2
 */