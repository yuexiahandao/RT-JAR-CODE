/*    */ package com.sun.jndi.ldap.pool;
/*    */ 
/*    */ final class ConnectionsRef
/*    */ {
/*    */   private final Connections conns;
/*    */ 
/*    */   ConnectionsRef(Connections paramConnections)
/*    */   {
/* 52 */     this.conns = paramConnections;
/*    */   }
/*    */ 
/*    */   Connections getConnections() {
/* 56 */     return this.conns;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.ldap.pool.ConnectionsRef
 * JD-Core Version:    0.6.2
 */