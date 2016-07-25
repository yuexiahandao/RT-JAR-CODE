/*     */ package com.sun.corba.se.impl.transport;
/*     */ 
/*     */ import com.sun.corba.se.impl.logging.ORBUtilSystemException;
/*     */ import com.sun.corba.se.impl.orbutil.ORBUtility;
/*     */ import com.sun.corba.se.pept.transport.Acceptor;
/*     */ import com.sun.corba.se.pept.transport.Connection;
/*     */ import com.sun.corba.se.pept.transport.InboundConnectionCache;
/*     */ import com.sun.corba.se.spi.monitoring.LongMonitoredAttributeBase;
/*     */ import com.sun.corba.se.spi.monitoring.MonitoredAttribute;
/*     */ import com.sun.corba.se.spi.monitoring.MonitoredObject;
/*     */ import com.sun.corba.se.spi.monitoring.MonitoredObjectFactory;
/*     */ import com.sun.corba.se.spi.monitoring.MonitoringFactories;
/*     */ import com.sun.corba.se.spi.monitoring.MonitoringManager;
/*     */ import com.sun.corba.se.spi.orb.ORB;
/*     */ import com.sun.corba.se.spi.transport.CorbaAcceptor;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ 
/*     */ public class CorbaInboundConnectionCacheImpl extends CorbaConnectionCacheBase
/*     */   implements InboundConnectionCache
/*     */ {
/*     */   protected Collection connectionCache;
/*     */ 
/*     */   public CorbaInboundConnectionCacheImpl(ORB paramORB, Acceptor paramAcceptor)
/*     */   {
/*  59 */     super(paramORB, paramAcceptor.getConnectionCacheType(), ((CorbaAcceptor)paramAcceptor).getMonitoringName());
/*     */ 
/*  61 */     this.connectionCache = new ArrayList();
/*     */   }
/*     */ 
/*     */   public Connection get(Acceptor paramAcceptor)
/*     */   {
/*  71 */     throw this.wrapper.methodShouldNotBeCalled();
/*     */   }
/*     */ 
/*     */   public void put(Acceptor paramAcceptor, Connection paramConnection)
/*     */   {
/*  76 */     if (this.orb.transportDebugFlag) {
/*  77 */       dprint(".put: " + paramAcceptor + " " + paramConnection);
/*     */     }
/*  79 */     synchronized (backingStore()) {
/*  80 */       this.connectionCache.add(paramConnection);
/*  81 */       paramConnection.setConnectionCache(this);
/*  82 */       dprintStatistics();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void remove(Connection paramConnection)
/*     */   {
/*  88 */     if (this.orb.transportDebugFlag) {
/*  89 */       dprint(".remove: " + paramConnection);
/*     */     }
/*  91 */     synchronized (backingStore()) {
/*  92 */       this.connectionCache.remove(paramConnection);
/*  93 */       dprintStatistics();
/*     */     }
/*     */   }
/*     */ 
/*     */   public Collection values()
/*     */   {
/* 104 */     return this.connectionCache;
/*     */   }
/*     */ 
/*     */   protected Object backingStore()
/*     */   {
/* 109 */     return this.connectionCache;
/*     */   }
/*     */ 
/*     */   protected void registerWithMonitoring()
/*     */   {
/* 115 */     MonitoredObject localMonitoredObject1 = this.orb.getMonitoringManager().getRootMonitoredObject();
/*     */ 
/* 121 */     MonitoredObject localMonitoredObject2 = localMonitoredObject1.getChild("Connections");
/*     */ 
/* 123 */     if (localMonitoredObject2 == null) {
/* 124 */       localMonitoredObject2 = MonitoringFactories.getMonitoredObjectFactory().createMonitoredObject("Connections", "Statistics on inbound/outbound connections");
/*     */ 
/* 129 */       localMonitoredObject1.addChild(localMonitoredObject2);
/*     */     }
/*     */ 
/* 133 */     MonitoredObject localMonitoredObject3 = localMonitoredObject2.getChild("Inbound");
/*     */ 
/* 136 */     if (localMonitoredObject3 == null) {
/* 137 */       localMonitoredObject3 = MonitoringFactories.getMonitoredObjectFactory().createMonitoredObject("Inbound", "Statistics on inbound connections");
/*     */ 
/* 142 */       localMonitoredObject2.addChild(localMonitoredObject3);
/*     */     }
/*     */ 
/* 146 */     MonitoredObject localMonitoredObject4 = localMonitoredObject3.getChild(getMonitoringName());
/*     */ 
/* 148 */     if (localMonitoredObject4 == null) {
/* 149 */       localMonitoredObject4 = MonitoringFactories.getMonitoredObjectFactory().createMonitoredObject(getMonitoringName(), "Connection statistics");
/*     */ 
/* 154 */       localMonitoredObject3.addChild(localMonitoredObject4);
/*     */     }
/*     */ 
/* 160 */     Object localObject = new LongMonitoredAttributeBase("NumberOfConnections", "The total number of connections")
/*     */     {
/*     */       public Object getValue()
/*     */       {
/* 166 */         return new Long(CorbaInboundConnectionCacheImpl.this.numberOfConnections());
/*     */       }
/*     */     };
/* 169 */     localMonitoredObject4.addAttribute((MonitoredAttribute)localObject);
/*     */ 
/* 172 */     localObject = new LongMonitoredAttributeBase("NumberOfIdleConnections", "The number of idle connections")
/*     */     {
/*     */       public Object getValue()
/*     */       {
/* 178 */         return new Long(CorbaInboundConnectionCacheImpl.this.numberOfIdleConnections());
/*     */       }
/*     */     };
/* 181 */     localMonitoredObject4.addAttribute((MonitoredAttribute)localObject);
/*     */ 
/* 184 */     localObject = new LongMonitoredAttributeBase("NumberOfBusyConnections", "The number of busy connections")
/*     */     {
/*     */       public Object getValue()
/*     */       {
/* 190 */         return new Long(CorbaInboundConnectionCacheImpl.this.numberOfBusyConnections());
/*     */       }
/*     */     };
/* 193 */     localMonitoredObject4.addAttribute((MonitoredAttribute)localObject);
/*     */   }
/*     */ 
/*     */   protected void dprint(String paramString)
/*     */   {
/* 198 */     ORBUtility.dprint("CorbaInboundConnectionCacheImpl", paramString);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.transport.CorbaInboundConnectionCacheImpl
 * JD-Core Version:    0.6.2
 */