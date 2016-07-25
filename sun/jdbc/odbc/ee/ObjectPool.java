/*     */ package sun.jdbc.odbc.ee;
/*     */ 
/*     */ import java.sql.SQLException;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Properties;
/*     */ import sun.jdbc.odbc.JdbcOdbcTracer;
/*     */ 
/*     */ public abstract class ObjectPool
/*     */ {
/*     */   private int initialSize;
/*     */   private int maxSize;
/*     */   private int minSize;
/*     */   private int maxIdleTime;
/*     */   private int timeoutFromPool;
/*     */   private int mInterval;
/*  35 */   private int currentSize = 0;
/*     */   private String name;
/*     */   private Hashtable freePool;
/*     */   private Hashtable lockedObjects;
/*     */   private Hashtable garbagePool;
/*     */   private PoolWorker worker;
/*  53 */   private JdbcOdbcTracer tracer = new JdbcOdbcTracer();
/*     */ 
/*  56 */   private boolean usable = true;
/*     */ 
/*  59 */   private boolean initialized = false;
/*     */   private String errorMessage;
/*     */ 
/*     */   public ObjectPool(String paramString)
/*     */   {
/*  68 */     this.name = paramString;
/*  69 */     this.worker = new PoolWorker(this);
/*  70 */     this.freePool = new Hashtable();
/*  71 */     this.lockedObjects = new Hashtable();
/*  72 */     this.garbagePool = new Hashtable();
/*     */   }
/*     */ 
/*     */   public void setProperties(PoolProperties paramPoolProperties)
/*     */     throws SQLException
/*     */   {
/*  83 */     this.tracer.trace("Setting the properties in Pool");
/*     */ 
/*  85 */     this.initialSize = paramPoolProperties.get("initialPoolSize");
/*  86 */     this.minSize = paramPoolProperties.get("minPoolSize");
/*  87 */     this.maxSize = paramPoolProperties.get("maxPoolSize");
/*  88 */     this.timeoutFromPool = paramPoolProperties.get("timeOutFromPool");
/*  89 */     this.mInterval = paramPoolProperties.get("mInterval");
/*  90 */     this.maxIdleTime = paramPoolProperties.get("maxIdleTime");
/*     */ 
/*  92 */     if (this.minSize > this.initialSize) {
/*  93 */       this.initialSize = this.minSize;
/*  94 */       this.tracer.trace("Connection Pool: Initial Size is set to Max Size ");
/*     */     }
/*     */ 
/*  97 */     if ((this.maxSize < this.initialSize) && (this.maxSize != 0)) {
/*  98 */       this.maxSize = this.initialSize;
/*  99 */       this.tracer.trace("Connection Pool: Maximum size is less than Initial size, using the Initial size ");
/*     */     }
/*     */ 
/* 102 */     if (this.mInterval == 0)
/* 103 */       throw new SQLException("Maintenance interval cannot be zero");
/*     */   }
/*     */ 
/*     */   public void initializePool()
/*     */     throws SQLException
/*     */   {
/* 114 */     this.tracer.trace("Setting the properties in Pool");
/* 115 */     if (this.initialized) {
/* 116 */       return;
/*     */     }
/* 118 */     this.initialized = true;
/* 119 */     fillThePool(this.initialSize);
/* 120 */     this.worker.start();
/*     */   }
/*     */ 
/*     */   protected void fillThePool(int paramInt)
/*     */     throws SQLException
/*     */   {
/* 130 */     this.tracer.trace("fillThePool: Filling the pool upto :" + paramInt + "from :" + this.currentSize);
/*     */ 
/* 132 */     if (!this.usable) {
/* 133 */       this.tracer.trace("The pool is marked non usable. Not filling the pool");
/* 134 */       return;
/*     */     }
/*     */     try {
/* 137 */       while (this.currentSize < paramInt)
/* 138 */         addNew(createObject());
/*     */     }
/*     */     catch (Exception localException) {
/* 141 */       this.tracer.trace("fillThePool: Exception thrown in filling." + localException.getMessage());
/* 142 */       throw new SQLException(localException.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   protected PooledObject createObject()
/*     */     throws SQLException
/*     */   {
/* 153 */     return createObject(null);
/*     */   }
/*     */ 
/*     */   protected synchronized PooledObject createObject(Properties paramProperties)
/*     */     throws SQLException
/*     */   {
/* 163 */     PooledObject localPooledObject = create(paramProperties);
/* 164 */     this.currentSize += 1;
/* 165 */     return localPooledObject;
/*     */   }
/*     */ 
/*     */   protected abstract PooledObject create(Properties paramProperties)
/*     */     throws SQLException;
/*     */ 
/*     */   protected synchronized void addNew(PooledObject paramPooledObject)
/*     */   {
/* 182 */     this.freePool.put(paramPooledObject, new Long(System.currentTimeMillis()));
/*     */   }
/*     */ 
/*     */   protected boolean checkAndMark(PooledObject paramPooledObject)
/*     */   {
/* 192 */     if (this.freePool.containsKey(paramPooledObject)) {
/* 193 */       long l = ((Long)this.freePool.get(paramPooledObject)).longValue();
/* 194 */       int i = 0;
/* 195 */       int j = 0;
/* 196 */       if ((paramPooledObject.getCreatedTime() + this.timeoutFromPool * 1000 < System.currentTimeMillis()) && (this.timeoutFromPool != 0))
/*     */       {
/* 198 */         i = 1;
/*     */       }
/* 200 */       if ((l + this.maxIdleTime * 1000 < System.currentTimeMillis()) && (this.maxIdleTime != 0))
/*     */       {
/* 202 */         j = 1;
/*     */       }
/* 204 */       if ((i != 0) || (j != 0) || (!paramPooledObject.isUsable())) {
/* 205 */         paramPooledObject.markForSweep();
/* 206 */         this.garbagePool.put(paramPooledObject, "");
/* 207 */         this.freePool.remove(paramPooledObject);
/* 208 */         return true;
/* 209 */       }if (paramPooledObject.isMarkedForSweep()) {
/* 210 */         this.garbagePool.put(paramPooledObject, "");
/* 211 */         this.freePool.remove(paramPooledObject);
/* 212 */         return true;
/*     */       }
/* 214 */       return false;
/*     */     }
/* 216 */     return false;
/*     */   }
/*     */ 
/*     */   protected void destroyFromPool(PooledObject paramPooledObject, Hashtable paramHashtable)
/*     */   {
/*     */     try
/*     */     {
/* 228 */       paramPooledObject.destroy();
/*     */     }
/*     */     catch (Exception localException) {
/* 231 */       this.tracer.trace("Connection Pool : Exception while destroying + e.getMessage()");
/*     */     }
/* 233 */     paramHashtable.remove(paramPooledObject);
/* 234 */     this.currentSize -= 1;
/*     */   }
/*     */ 
/*     */   public synchronized PooledObject checkOut()
/*     */     throws SQLException
/*     */   {
/* 244 */     if (!this.usable) throw new SQLException(" Connection Pool: " + this.errorMessage);
/* 245 */     Enumeration localEnumeration = this.freePool.keys();
/*     */     PooledObject localPooledObject;
/* 246 */     while (localEnumeration.hasMoreElements()) {
/* 247 */       localPooledObject = (PooledObject)localEnumeration.nextElement();
/* 248 */       if ((!checkAndMark(localPooledObject)) && (this.freePool.containsKey(localPooledObject))) {
/* 249 */         this.lockedObjects.put(localPooledObject, "");
/* 250 */         this.freePool.remove(localPooledObject);
/* 251 */         localPooledObject.checkedOut();
/* 252 */         return localPooledObject;
/*     */       }
/*     */     }
/* 255 */     if ((this.currentSize < this.maxSize) || (this.maxSize == 0)) {
/* 256 */       localPooledObject = createObject();
/* 257 */       this.lockedObjects.put(localPooledObject, "");
/* 258 */       localPooledObject.checkedOut();
/* 259 */       return localPooledObject;
/*     */     }
/* 261 */     throw new SQLException("Maximum limit has reached and no connection is free");
/*     */   }
/*     */ 
/*     */   public synchronized PooledObject checkOut(Properties paramProperties)
/*     */     throws SQLException
/*     */   {
/* 273 */     if (!this.usable) throw new SQLException(" Connection Pool: " + this.errorMessage);
/* 274 */     Enumeration localEnumeration = this.freePool.keys();
/*     */     PooledObject localPooledObject;
/* 275 */     while (localEnumeration.hasMoreElements()) {
/* 276 */       localPooledObject = (PooledObject)localEnumeration.nextElement();
/* 277 */       if ((!checkAndMark(localPooledObject)) && (this.freePool.containsKey(localPooledObject)) && (localPooledObject.isMatching(paramProperties))) {
/* 278 */         this.lockedObjects.put(localPooledObject, "");
/* 279 */         this.freePool.remove(localPooledObject);
/* 280 */         localPooledObject.checkedOut();
/* 281 */         return localPooledObject;
/*     */       }
/*     */     }
/* 284 */     if ((this.currentSize < this.maxSize) || (this.maxSize == 0)) {
/* 285 */       localPooledObject = createObject(paramProperties);
/* 286 */       this.lockedObjects.put(localPooledObject, "");
/* 287 */       localPooledObject.checkedOut();
/* 288 */       return localPooledObject;
/*     */     }
/* 290 */     throw new SQLException("Maximum limit has reached and no connection is free");
/*     */   }
/*     */ 
/*     */   public synchronized void tryCheckOut(PooledObject paramPooledObject)
/*     */     throws SQLException
/*     */   {
/* 300 */     if ((!checkAndMark(paramPooledObject)) && (this.freePool.containsKey(paramPooledObject))) {
/* 301 */       this.lockedObjects.put(paramPooledObject, "");
/* 302 */       this.freePool.remove(paramPooledObject);
/* 303 */       paramPooledObject.checkedOut();
/*     */     } else {
/* 305 */       throw new SQLException("Object is not available for use" + this.freePool.containsKey(paramPooledObject));
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized void checkIn(PooledObject paramPooledObject)
/*     */   {
/* 315 */     int i = 0;
/* 316 */     if ((paramPooledObject.getCreatedTime() + this.timeoutFromPool * 1000 < System.currentTimeMillis()) && (this.timeoutFromPool != 0))
/*     */     {
/* 318 */       i = 1;
/*     */     }
/* 320 */     if ((i != 0) || (!paramPooledObject.isUsable())) {
/* 321 */       paramPooledObject.markForSweep();
/* 322 */       this.garbagePool.put(paramPooledObject, "");
/* 323 */       this.lockedObjects.remove(paramPooledObject);
/*     */     } else {
/* 325 */       paramPooledObject.checkedIn();
/* 326 */       this.freePool.put(paramPooledObject, new Long(System.currentTimeMillis()));
/* 327 */       this.lockedObjects.remove(paramPooledObject);
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getCurrentSize()
/*     */   {
/* 337 */     return this.currentSize;
/*     */   }
/*     */ 
/*     */   public int getMaintenanceInterval()
/*     */   {
/* 346 */     return this.mInterval;
/*     */   }
/*     */ 
/*     */   public void setTracer(JdbcOdbcTracer paramJdbcOdbcTracer)
/*     */   {
/* 355 */     if (paramJdbcOdbcTracer != null)
/* 356 */       this.tracer = paramJdbcOdbcTracer;
/*     */   }
/*     */ 
/*     */   public void markError(String paramString)
/*     */   {
/* 366 */     this.usable = false;
/* 367 */     this.errorMessage = paramString;
/*     */   }
/*     */ 
/*     */   public JdbcOdbcTracer getTracer()
/*     */   {
/* 376 */     return this.tracer;
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 385 */     return this.name;
/*     */   }
/*     */ 
/*     */   public void maintain()
/*     */     throws SQLException
/*     */   {
/* 393 */     this.tracer.trace("Before <maintenance> Locked :" + this.lockedObjects.size() + " free :" + this.freePool.size() + "garbage :" + this.garbagePool.size() + "current size :" + this.currentSize);
/* 394 */     Enumeration localEnumeration1 = this.garbagePool.keys();
/* 395 */     while (localEnumeration1.hasMoreElements()) {
/* 396 */       PooledObject localPooledObject1 = (PooledObject)localEnumeration1.nextElement();
/* 397 */       destroyFromPool(localPooledObject1, this.garbagePool);
/*     */     }
/* 399 */     synchronized (this) {
/* 400 */       Enumeration localEnumeration2 = this.freePool.keys();
/* 401 */       while (localEnumeration2.hasMoreElements()) {
/* 402 */         PooledObject localPooledObject2 = (PooledObject)localEnumeration2.nextElement();
/* 403 */         checkAndMark(localPooledObject2);
/*     */       }
/*     */     }
/* 406 */     fillThePool(this.minSize);
/* 407 */     this.tracer.trace("Before <maintenance> Locked :" + this.lockedObjects.size() + " free :" + this.freePool.size() + "garbage :" + this.garbagePool.size() + "current size :" + this.currentSize);
/*     */   }
/*     */ 
/*     */   public void shutDown(boolean paramBoolean)
/*     */   {
/* 424 */     if (paramBoolean == true) {
/* 425 */       this.worker.release();
/* 426 */       shutDownNow();
/*     */     } else {
/* 428 */       markError("Being shut down now");
/*     */     }
/*     */   }
/*     */ 
/*     */   private synchronized void shutDownNow()
/*     */   {
/*     */     try
/*     */     {
/* 437 */       this.tracer.trace("Shutting down the pool");
/* 438 */       ConnectionPoolFactory.removePool(this.name);
/* 439 */       Enumeration localEnumeration = this.garbagePool.keys();
/*     */       PooledObject localPooledObject;
/* 440 */       while (localEnumeration.hasMoreElements()) {
/* 441 */         localPooledObject = (PooledObject)localEnumeration.nextElement();
/* 442 */         destroyFromPool(localPooledObject, this.garbagePool);
/*     */       }
/* 444 */       localEnumeration = this.freePool.keys();
/* 445 */       while (localEnumeration.hasMoreElements()) {
/* 446 */         localPooledObject = (PooledObject)localEnumeration.nextElement();
/* 447 */         destroyFromPool(localPooledObject, this.freePool);
/*     */       }
/* 449 */       localEnumeration = this.lockedObjects.keys();
/* 450 */       while (localEnumeration.hasMoreElements()) {
/* 451 */         localPooledObject = (PooledObject)localEnumeration.nextElement();
/* 452 */         destroyFromPool(localPooledObject, this.lockedObjects);
/*     */       }
/*     */     } catch (Exception localException) {
/* 455 */       localException.printStackTrace();
/* 456 */       this.tracer.trace("An error occurred while shutting down " + localException);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.jdbc.odbc.ee.ObjectPool
 * JD-Core Version:    0.6.2
 */