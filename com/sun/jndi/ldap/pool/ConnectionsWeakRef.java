/*    */ package com.sun.jndi.ldap.pool;
/*    */ 
/*    */ import java.lang.ref.ReferenceQueue;
/*    */ import java.lang.ref.WeakReference;
/*    */ 
/*    */ class ConnectionsWeakRef extends WeakReference
/*    */ {
/*    */   private final Connections conns;
/*    */ 
/*    */   ConnectionsWeakRef(ConnectionsRef paramConnectionsRef, ReferenceQueue paramReferenceQueue)
/*    */   {
/* 63 */     super(paramConnectionsRef, paramReferenceQueue);
/* 64 */     this.conns = paramConnectionsRef.getConnections();
/*    */   }
/*    */ 
/*    */   Connections getConnections() {
/* 68 */     return this.conns;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.ldap.pool.ConnectionsWeakRef
 * JD-Core Version:    0.6.2
 */