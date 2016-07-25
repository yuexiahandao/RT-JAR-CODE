/*     */ package com.sun.jndi.ldap.pool;
/*     */ 
/*     */ import com.sun.jndi.ldap.LdapPoolManager;
/*     */ import java.io.PrintStream;
/*     */ import java.lang.ref.ReferenceQueue;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import java.util.WeakHashMap;
/*     */ import javax.naming.NamingException;
/*     */ 
/*     */ public final class Pool
/*     */ {
/*  81 */   static final boolean debug = LdapPoolManager.debug;
/*     */ 
/*  86 */   private static final ReferenceQueue queue = new ReferenceQueue();
/*  87 */   private static final Collection weakRefs = Collections.synchronizedList(new LinkedList());
/*     */   private final int maxSize;
/*     */   private final int prefSize;
/*     */   private final int initSize;
/*     */   private final Map map;
/*     */ 
/*     */   public Pool(int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/*  96 */     this.map = new WeakHashMap();
/*  97 */     this.prefSize = paramInt2;
/*  98 */     this.maxSize = paramInt3;
/*  99 */     this.initSize = paramInt1;
/*     */   }
/*     */ 
/*     */   public PooledConnection getPooledConnection(Object paramObject, long paramLong, PooledConnectionFactory paramPooledConnectionFactory)
/*     */     throws NamingException
/*     */   {
/* 120 */     d("get(): ", paramObject);
/* 121 */     d("size: ", this.map.size());
/*     */ 
/* 123 */     expungeStaleConnections();
/*     */     Connections localConnections;
/* 126 */     synchronized (this.map) {
/* 127 */       localConnections = getConnections(paramObject);
/* 128 */       if (localConnections == null) {
/* 129 */         d("get(): creating new connections list for ", paramObject);
/*     */ 
/* 132 */         localConnections = new Connections(paramObject, this.initSize, this.prefSize, this.maxSize, paramPooledConnectionFactory);
/*     */ 
/* 134 */         ConnectionsRef localConnectionsRef = new ConnectionsRef(localConnections);
/* 135 */         this.map.put(paramObject, localConnectionsRef);
/*     */ 
/* 138 */         ConnectionsWeakRef localConnectionsWeakRef = new ConnectionsWeakRef(localConnectionsRef, queue);
/*     */ 
/* 141 */         weakRefs.add(localConnectionsWeakRef);
/*     */       }
/*     */     }
/*     */ 
/* 145 */     d("get(): size after: ", this.map.size());
/*     */ 
/* 147 */     return localConnections.get(paramLong, paramPooledConnectionFactory);
/*     */   }
/*     */ 
/*     */   private Connections getConnections(Object paramObject) {
/* 151 */     ConnectionsRef localConnectionsRef = (ConnectionsRef)this.map.get(paramObject);
/* 152 */     return localConnectionsRef != null ? localConnectionsRef.getConnections() : null;
/*     */   }
/*     */ 
/*     */   public void expire(long paramLong)
/*     */   {
/* 165 */     synchronized (this.map) {
/* 166 */       Collection localCollection = this.map.values();
/* 167 */       Iterator localIterator = localCollection.iterator();
/*     */ 
/* 169 */       while (localIterator.hasNext()) {
/* 170 */         Connections localConnections = ((ConnectionsRef)localIterator.next()).getConnections();
/* 171 */         if (localConnections.expire(paramLong)) {
/* 172 */           d("expire(): removing ", localConnections);
/* 173 */           localIterator.remove();
/*     */         }
/*     */       }
/*     */     }
/* 177 */     expungeStaleConnections();
/*     */   }
/*     */ 
/*     */   private static void expungeStaleConnections()
/*     */   {
/* 186 */     ConnectionsWeakRef localConnectionsWeakRef = null;
/*     */ 
/* 188 */     while ((localConnectionsWeakRef = (ConnectionsWeakRef)queue.poll()) != null) {
/* 189 */       Connections localConnections = localConnectionsWeakRef.getConnections();
/*     */ 
/* 191 */       if (debug) {
/* 192 */         System.err.println("weak reference cleanup: Closing Connections:" + localConnections);
/*     */       }
/*     */ 
/* 197 */       localConnections.close();
/* 198 */       weakRefs.remove(localConnectionsWeakRef);
/* 199 */       localConnectionsWeakRef.clear();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void showStats(PrintStream paramPrintStream)
/*     */   {
/* 209 */     paramPrintStream.println("===== Pool start ======================");
/* 210 */     paramPrintStream.println("maximum pool size: " + this.maxSize);
/* 211 */     paramPrintStream.println("preferred pool size: " + this.prefSize);
/* 212 */     paramPrintStream.println("initial pool size: " + this.initSize);
/* 213 */     paramPrintStream.println("current pool size: " + this.map.size());
/*     */ 
/* 215 */     Set localSet = this.map.entrySet();
/* 216 */     Iterator localIterator = localSet.iterator();
/*     */ 
/* 218 */     while (localIterator.hasNext()) {
/* 219 */       Map.Entry localEntry = (Map.Entry)localIterator.next();
/* 220 */       Object localObject = localEntry.getKey();
/* 221 */       Connections localConnections = ((ConnectionsRef)localEntry.getValue()).getConnections();
/* 222 */       paramPrintStream.println("   " + localObject + ":" + localConnections.getStats());
/*     */     }
/*     */ 
/* 225 */     paramPrintStream.println("====== Pool end =====================");
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 229 */     return super.toString() + " " + this.map.toString();
/*     */   }
/*     */ 
/*     */   private void d(String paramString, int paramInt) {
/* 233 */     if (debug)
/* 234 */       System.err.println(this + "." + paramString + paramInt);
/*     */   }
/*     */ 
/*     */   private void d(String paramString, Object paramObject)
/*     */   {
/* 239 */     if (debug)
/* 240 */       System.err.println(this + "." + paramString + paramObject);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.ldap.pool.Pool
 * JD-Core Version:    0.6.2
 */