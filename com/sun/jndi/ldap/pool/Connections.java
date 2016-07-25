/*     */ package com.sun.jndi.ldap.pool;
/*     */ 
/*     */ import com.sun.jndi.ldap.LdapPoolManager;
/*     */ import java.io.PrintStream;
/*     */ import java.lang.ref.Reference;
/*     */ import java.lang.ref.SoftReference;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import javax.naming.CommunicationException;
/*     */ import javax.naming.InterruptedNamingException;
/*     */ import javax.naming.NamingException;
/*     */ 
/*     */ final class Connections
/*     */   implements PoolCallback
/*     */ {
/*  67 */   private static final boolean debug = Pool.debug;
/*  68 */   private static final boolean trace = LdapPoolManager.trace;
/*     */   private static final int DEFAULT_SIZE = 10;
/*     */   private final int maxSize;
/*     */   private final int prefSize;
/*     */   private final List conns;
/*  76 */   private boolean closed = false;
/*     */   private Reference ref;
/*     */ 
/*     */   Connections(Object paramObject, int paramInt1, int paramInt2, int paramInt3, PooledConnectionFactory paramPooledConnectionFactory)
/*     */     throws NamingException
/*     */   {
/*  94 */     this.maxSize = paramInt3;
/*  95 */     if (paramInt3 > 0)
/*     */     {
/*  97 */       this.prefSize = Math.min(paramInt2, paramInt3);
/*  98 */       paramInt1 = Math.min(paramInt1, paramInt3);
/*     */     } else {
/* 100 */       this.prefSize = paramInt2;
/*     */     }
/* 102 */     this.conns = new ArrayList(paramInt3 > 0 ? paramInt3 : 10);
/*     */ 
/* 106 */     this.ref = new SoftReference(paramObject);
/*     */ 
/* 108 */     d("init size=", paramInt1);
/* 109 */     d("max size=", paramInt3);
/* 110 */     d("preferred size=", paramInt2);
/*     */ 
/* 114 */     for (int i = 0; i < paramInt1; i++) {
/* 115 */       PooledConnection localPooledConnection = paramPooledConnectionFactory.createPooledConnection(this);
/* 116 */       td("Create ", localPooledConnection, paramPooledConnectionFactory);
/* 117 */       this.conns.add(new ConnectionDesc(localPooledConnection));
/*     */     }
/*     */   }
/*     */ 
/*     */   synchronized PooledConnection get(long paramLong, PooledConnectionFactory paramPooledConnectionFactory)
/*     */     throws NamingException
/*     */   {
/* 140 */     long l1 = paramLong > 0L ? System.currentTimeMillis() : 0L;
/* 141 */     long l2 = paramLong;
/*     */ 
/* 143 */     d("get(): before");
/*     */     PooledConnection localPooledConnection;
/* 144 */     while ((localPooledConnection = getOrCreateConnection(paramPooledConnectionFactory)) == null) {
/* 145 */       if ((paramLong > 0L) && (l2 <= 0L)) {
/* 146 */         throw new CommunicationException("Timeout exceeded while waiting for a connection: " + paramLong + "ms");
/*     */       }
/*     */ 
/*     */       try
/*     */       {
/* 151 */         d("get(): waiting");
/* 152 */         if (l2 > 0L)
/* 153 */           wait(l2);
/*     */         else
/* 155 */           wait();
/*     */       }
/*     */       catch (InterruptedException localInterruptedException) {
/* 158 */         throw new InterruptedNamingException("Interrupted while waiting for a connection");
/*     */       }
/*     */ 
/* 162 */       if (paramLong > 0L) {
/* 163 */         long l3 = System.currentTimeMillis();
/* 164 */         l2 = paramLong - (l3 - l1);
/*     */       }
/*     */     }
/*     */ 
/* 168 */     d("get(): after");
/* 169 */     return localPooledConnection;
/*     */   }
/*     */ 
/*     */   private PooledConnection getOrCreateConnection(PooledConnectionFactory paramPooledConnectionFactory)
/*     */     throws NamingException
/*     */   {
/* 181 */     int i = this.conns.size();
/* 182 */     PooledConnection localPooledConnection = null;
/*     */ 
/* 184 */     if ((this.prefSize <= 0) || (i >= this.prefSize))
/*     */     {
/* 188 */       for (int j = 0; j < i; j++) {
/* 189 */         ConnectionDesc localConnectionDesc = (ConnectionDesc)this.conns.get(j);
/* 190 */         if ((localPooledConnection = localConnectionDesc.tryUse()) != null) {
/* 191 */           d("get(): use ", localPooledConnection);
/* 192 */           td("Use ", localPooledConnection);
/* 193 */           return localPooledConnection;
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 199 */     if ((this.maxSize > 0) && (i >= this.maxSize)) {
/* 200 */       return null;
/*     */     }
/*     */ 
/* 203 */     localPooledConnection = paramPooledConnectionFactory.createPooledConnection(this);
/* 204 */     td("Create and use ", localPooledConnection, paramPooledConnectionFactory);
/* 205 */     this.conns.add(new ConnectionDesc(localPooledConnection, true));
/*     */ 
/* 207 */     return localPooledConnection;
/*     */   }
/*     */ 
/*     */   public synchronized boolean releasePooledConnection(PooledConnection paramPooledConnection)
/*     */   {
/*     */     ConnectionDesc localConnectionDesc;
/* 220 */     int i = this.conns.indexOf(localConnectionDesc = new ConnectionDesc(paramPooledConnection));
/*     */ 
/* 222 */     d("release(): ", paramPooledConnection);
/*     */ 
/* 224 */     if (i >= 0)
/*     */     {
/* 227 */       if ((this.closed) || ((this.prefSize > 0) && (this.conns.size() > this.prefSize)))
/*     */       {
/* 230 */         d("release(): closing ", paramPooledConnection);
/* 231 */         td("Close ", paramPooledConnection);
/*     */ 
/* 234 */         this.conns.remove(localConnectionDesc);
/* 235 */         paramPooledConnection.closeConnection();
/*     */       }
/*     */       else {
/* 238 */         d("release(): release ", paramPooledConnection);
/* 239 */         td("Release ", paramPooledConnection);
/*     */ 
/* 242 */         localConnectionDesc = (ConnectionDesc)this.conns.get(i);
/*     */ 
/* 244 */         localConnectionDesc.release();
/*     */       }
/* 246 */       notifyAll();
/* 247 */       d("release(): notify");
/* 248 */       return true;
/*     */     }
/* 250 */     return false;
/*     */   }
/*     */ 
/*     */   public synchronized boolean removePooledConnection(PooledConnection paramPooledConnection)
/*     */   {
/* 265 */     if (this.conns.remove(new ConnectionDesc(paramPooledConnection))) {
/* 266 */       d("remove(): ", paramPooledConnection);
/*     */ 
/* 268 */       notifyAll();
/*     */ 
/* 270 */       d("remove(): notify");
/* 271 */       td("Remove ", paramPooledConnection);
/*     */ 
/* 273 */       if (this.conns.isEmpty())
/*     */       {
/* 276 */         this.ref = null;
/*     */       }
/*     */ 
/* 279 */       return true;
/*     */     }
/* 281 */     d("remove(): not found ", paramPooledConnection);
/* 282 */     return false;
/*     */   }
/*     */ 
/*     */   synchronized boolean expire(long paramLong)
/*     */   {
/* 294 */     Iterator localIterator = this.conns.iterator();
/*     */ 
/* 296 */     while (localIterator.hasNext()) {
/* 297 */       ConnectionDesc localConnectionDesc = (ConnectionDesc)localIterator.next();
/* 298 */       if (localConnectionDesc.expire(paramLong)) {
/* 299 */         d("expire(): removing ", localConnectionDesc);
/* 300 */         td("Expired ", localConnectionDesc);
/*     */ 
/* 302 */         localIterator.remove();
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 309 */     return this.conns.isEmpty();
/*     */   }
/*     */ 
/*     */   synchronized void close()
/*     */   {
/* 320 */     expire(System.currentTimeMillis());
/* 321 */     this.closed = true;
/*     */   }
/*     */ 
/*     */   String getStats() {
/* 325 */     int i = 0;
/* 326 */     int j = 0;
/* 327 */     int k = 0;
/* 328 */     long l = 0L;
/*     */     int m;
/* 331 */     synchronized (this) {
/* 332 */       m = this.conns.size();
/*     */ 
/* 335 */       for (int n = 0; n < m; n++) {
/* 336 */         ConnectionDesc localConnectionDesc = (ConnectionDesc)this.conns.get(n);
/* 337 */         l += localConnectionDesc.getUseCount();
/* 338 */         switch (localConnectionDesc.getState()) {
/*     */         case 0:
/* 340 */           j++;
/* 341 */           break;
/*     */         case 1:
/* 343 */           i++;
/* 344 */           break;
/*     */         case 2:
/* 346 */           k++;
/*     */         }
/*     */       }
/*     */     }
/* 350 */     return "size=" + m + "; use=" + l + "; busy=" + j + "; idle=" + i + "; expired=" + k;
/*     */   }
/*     */ 
/*     */   private void d(String paramString, Object paramObject)
/*     */   {
/* 355 */     if (debug)
/* 356 */       d(paramString + paramObject);
/*     */   }
/*     */ 
/*     */   private void d(String paramString, int paramInt)
/*     */   {
/* 361 */     if (debug)
/* 362 */       d(paramString + paramInt);
/*     */   }
/*     */ 
/*     */   private void d(String paramString)
/*     */   {
/* 367 */     if (debug)
/* 368 */       System.err.println(this + "." + paramString + "; size: " + this.conns.size());
/*     */   }
/*     */ 
/*     */   private void td(String paramString, Object paramObject1, Object paramObject2)
/*     */   {
/* 373 */     if (trace)
/* 374 */       td(paramString + paramObject1 + "[" + paramObject2 + "]");
/*     */   }
/*     */ 
/*     */   private void td(String paramString, Object paramObject) {
/* 378 */     if (trace)
/* 379 */       td(paramString + paramObject);
/*     */   }
/*     */ 
/*     */   private void td(String paramString) {
/* 383 */     if (trace)
/* 384 */       System.err.println(paramString);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.ldap.pool.Connections
 * JD-Core Version:    0.6.2
 */