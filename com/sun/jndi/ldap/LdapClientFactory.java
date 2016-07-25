/*    */ package com.sun.jndi.ldap;
/*    */ 
/*    */ import com.sun.jndi.ldap.pool.PoolCallback;
/*    */ import com.sun.jndi.ldap.pool.PooledConnection;
/*    */ import com.sun.jndi.ldap.pool.PooledConnectionFactory;
/*    */ import java.io.OutputStream;
/*    */ import javax.naming.NamingException;
/*    */ 
/*    */ final class LdapClientFactory
/*    */   implements PooledConnectionFactory
/*    */ {
/*    */   private final String host;
/*    */   private final int port;
/*    */   private final String socketFactory;
/*    */   private final int connTimeout;
/*    */   private final int readTimeout;
/*    */   private final OutputStream trace;
/*    */ 
/*    */   LdapClientFactory(String paramString1, int paramInt1, String paramString2, int paramInt2, int paramInt3, OutputStream paramOutputStream)
/*    */   {
/* 54 */     this.host = paramString1;
/* 55 */     this.port = paramInt1;
/* 56 */     this.socketFactory = paramString2;
/* 57 */     this.connTimeout = paramInt2;
/* 58 */     this.readTimeout = paramInt3;
/* 59 */     this.trace = paramOutputStream;
/*    */   }
/*    */ 
/*    */   public PooledConnection createPooledConnection(PoolCallback paramPoolCallback) throws NamingException
/*    */   {
/* 64 */     return new LdapClient(this.host, this.port, this.socketFactory, this.connTimeout, this.readTimeout, this.trace, paramPoolCallback);
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 69 */     return this.host + ":" + this.port;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.ldap.LdapClientFactory
 * JD-Core Version:    0.6.2
 */