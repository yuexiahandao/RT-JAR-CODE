/*     */ package com.sun.corba.se.impl.transport;
/*     */ 
/*     */ import com.sun.corba.se.impl.logging.ORBUtilSystemException;
/*     */ import com.sun.corba.se.impl.orbutil.ORBUtility;
/*     */ import com.sun.corba.se.pept.transport.Connection;
/*     */ import com.sun.corba.se.pept.transport.ConnectionCache;
/*     */ import com.sun.corba.se.spi.orb.ORB;
/*     */ import com.sun.corba.se.spi.orb.ORBData;
/*     */ import com.sun.corba.se.spi.transport.CorbaConnection;
/*     */ import com.sun.corba.se.spi.transport.CorbaConnectionCache;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ 
/*     */ public abstract class CorbaConnectionCacheBase
/*     */   implements ConnectionCache, CorbaConnectionCache
/*     */ {
/*     */   protected ORB orb;
/*  52 */   protected long timestamp = 0L;
/*     */   protected String cacheType;
/*     */   protected String monitoringName;
/*     */   protected ORBUtilSystemException wrapper;
/*     */ 
/*     */   protected CorbaConnectionCacheBase(ORB paramORB, String paramString1, String paramString2)
/*     */   {
/*  60 */     this.orb = paramORB;
/*  61 */     this.cacheType = paramString1;
/*  62 */     this.monitoringName = paramString2;
/*  63 */     this.wrapper = ORBUtilSystemException.get(paramORB, "rpc.transport");
/*  64 */     registerWithMonitoring();
/*  65 */     dprintCreation();
/*     */   }
/*     */ 
/*     */   public String getCacheType()
/*     */   {
/*  75 */     return this.cacheType;
/*     */   }
/*     */ 
/*     */   public synchronized void stampTime(Connection paramConnection)
/*     */   {
/*  81 */     paramConnection.setTimeStamp(this.timestamp++);
/*     */   }
/*     */ 
/*     */   public long numberOfConnections()
/*     */   {
/*  86 */     synchronized (backingStore()) {
/*  87 */       return values().size();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void close()
/*     */   {
/*     */     Iterator localIterator;
/*  92 */     synchronized (backingStore()) {
/*  93 */       for (localIterator = values().iterator(); localIterator.hasNext(); ) { Object localObject1 = localIterator.next();
/*  94 */         ((CorbaConnection)localObject1).closeConnectionResources();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public long numberOfIdleConnections()
/*     */   {
/* 101 */     long l = 0L;
/* 102 */     synchronized (backingStore()) {
/* 103 */       Iterator localIterator = values().iterator();
/* 104 */       while (localIterator.hasNext()) {
/* 105 */         if (!((Connection)localIterator.next()).isBusy()) {
/* 106 */           l += 1L;
/*     */         }
/*     */       }
/*     */     }
/* 110 */     return l;
/*     */   }
/*     */ 
/*     */   public long numberOfBusyConnections()
/*     */   {
/* 115 */     long l = 0L;
/* 116 */     synchronized (backingStore()) {
/* 117 */       Iterator localIterator = values().iterator();
/* 118 */       while (localIterator.hasNext()) {
/* 119 */         if (((Connection)localIterator.next()).isBusy()) {
/* 120 */           l += 1L;
/*     */         }
/*     */       }
/*     */     }
/* 124 */     return l;
/*     */   }
/*     */ 
/*     */   public synchronized boolean reclaim()
/*     */   {
/*     */     try
/*     */     {
/* 147 */       long l1 = numberOfConnections();
/*     */ 
/* 149 */       if (this.orb.transportDebugFlag) {
/* 150 */         dprint(".reclaim->: " + l1 + " (" + this.orb.getORBData().getHighWaterMark() + "/" + this.orb.getORBData().getLowWaterMark() + "/" + this.orb.getORBData().getNumberToReclaim() + ")");
/*     */       }
/*     */ 
/* 160 */       if ((l1 <= this.orb.getORBData().getHighWaterMark()) || (l1 < this.orb.getORBData().getLowWaterMark()))
/*     */       {
/* 162 */         return false;
/*     */       }
/*     */ 
/* 165 */       Object localObject1 = backingStore();
/* 166 */       synchronized (localObject1)
/*     */       {
/* 171 */         for (int i = 0; i < this.orb.getORBData().getNumberToReclaim(); i++) {
/* 172 */           Object localObject2 = null;
/* 173 */           long l2 = 9223372036854775807L;
/* 174 */           Iterator localIterator = values().iterator();
/*     */ 
/* 177 */           while (localIterator.hasNext()) {
/* 178 */             Connection localConnection = (Connection)localIterator.next();
/* 179 */             if ((!localConnection.isBusy()) && (localConnection.getTimeStamp() < l2)) {
/* 180 */               localObject2 = localConnection;
/* 181 */               l2 = localConnection.getTimeStamp();
/*     */             }
/*     */           }
/*     */ 
/* 185 */           if (localObject2 == null) {
/* 186 */             return false;
/*     */           }
/*     */           try
/*     */           {
/* 190 */             if (this.orb.transportDebugFlag) {
/* 191 */               dprint(".reclaim: closing: " + localObject2);
/*     */             }
/* 193 */             localObject2.close();
/*     */           }
/*     */           catch (Exception localException)
/*     */           {
/*     */           }
/*     */         }
/* 199 */         if (this.orb.transportDebugFlag) {
/* 200 */           dprint(".reclaim: connections reclaimed (" + (l1 - numberOfConnections()) + ")");
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 209 */       return true;
/*     */     } finally {
/* 211 */       if (this.orb.transportDebugFlag)
/* 212 */         dprint(".reclaim<-: " + numberOfConnections());
/*     */     }
/*     */   }
/*     */ 
/*     */   public String getMonitoringName()
/*     */   {
/* 224 */     return this.monitoringName;
/*     */   }
/*     */ 
/*     */   public abstract Collection values();
/*     */ 
/*     */   protected abstract Object backingStore();
/*     */ 
/*     */   protected abstract void registerWithMonitoring();
/*     */ 
/*     */   protected void dprintCreation()
/*     */   {
/* 241 */     if (this.orb.transportDebugFlag)
/* 242 */       dprint(".constructor: cacheType: " + getCacheType() + " monitoringName: " + getMonitoringName());
/*     */   }
/*     */ 
/*     */   protected void dprintStatistics()
/*     */   {
/* 249 */     if (this.orb.transportDebugFlag)
/* 250 */       dprint(".stats: " + numberOfConnections() + "/total " + numberOfBusyConnections() + "/busy " + numberOfIdleConnections() + "/idle" + " (" + this.orb.getORBData().getHighWaterMark() + "/" + this.orb.getORBData().getLowWaterMark() + "/" + this.orb.getORBData().getNumberToReclaim() + ")");
/*     */   }
/*     */ 
/*     */   protected void dprint(String paramString)
/*     */   {
/* 264 */     ORBUtility.dprint("CorbaConnectionCacheBase", paramString);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.transport.CorbaConnectionCacheBase
 * JD-Core Version:    0.6.2
 */