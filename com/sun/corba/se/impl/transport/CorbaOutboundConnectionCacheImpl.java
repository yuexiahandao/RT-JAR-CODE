/*     */ package com.sun.corba.se.impl.transport;
/*     */ 
/*     */ import com.sun.corba.se.impl.orbutil.ORBUtility;
/*     */ import com.sun.corba.se.pept.transport.Connection;
/*     */ import com.sun.corba.se.pept.transport.ContactInfo;
/*     */ import com.sun.corba.se.pept.transport.OutboundConnectionCache;
/*     */ import com.sun.corba.se.spi.monitoring.LongMonitoredAttributeBase;
/*     */ import com.sun.corba.se.spi.monitoring.MonitoredAttribute;
/*     */ import com.sun.corba.se.spi.monitoring.MonitoredObject;
/*     */ import com.sun.corba.se.spi.monitoring.MonitoredObjectFactory;
/*     */ import com.sun.corba.se.spi.monitoring.MonitoringFactories;
/*     */ import com.sun.corba.se.spi.monitoring.MonitoringManager;
/*     */ import com.sun.corba.se.spi.orb.ORB;
/*     */ import com.sun.corba.se.spi.transport.CorbaContactInfo;
/*     */ import java.util.Collection;
/*     */ import java.util.Hashtable;
/*     */ 
/*     */ public class CorbaOutboundConnectionCacheImpl extends CorbaConnectionCacheBase
/*     */   implements OutboundConnectionCache
/*     */ {
/*     */   protected Hashtable connectionCache;
/*     */ 
/*     */   public CorbaOutboundConnectionCacheImpl(ORB paramORB, ContactInfo paramContactInfo)
/*     */   {
/*  60 */     super(paramORB, paramContactInfo.getConnectionCacheType(), ((CorbaContactInfo)paramContactInfo).getMonitoringName());
/*     */ 
/*  62 */     this.connectionCache = new Hashtable();
/*     */   }
/*     */ 
/*     */   public Connection get(ContactInfo paramContactInfo)
/*     */   {
/*  72 */     if (this.orb.transportDebugFlag) {
/*  73 */       dprint(".get: " + paramContactInfo + " " + paramContactInfo.hashCode());
/*     */     }
/*  75 */     synchronized (backingStore()) {
/*  76 */       dprintStatistics();
/*  77 */       return (Connection)this.connectionCache.get(paramContactInfo);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void put(ContactInfo paramContactInfo, Connection paramConnection)
/*     */   {
/*  83 */     if (this.orb.transportDebugFlag) {
/*  84 */       dprint(".put: " + paramContactInfo + " " + paramContactInfo.hashCode() + " " + paramConnection);
/*     */     }
/*     */ 
/*  87 */     synchronized (backingStore()) {
/*  88 */       this.connectionCache.put(paramContactInfo, paramConnection);
/*  89 */       paramConnection.setConnectionCache(this);
/*  90 */       dprintStatistics();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void remove(ContactInfo paramContactInfo)
/*     */   {
/*  96 */     if (this.orb.transportDebugFlag) {
/*  97 */       dprint(".remove: " + paramContactInfo + " " + paramContactInfo.hashCode());
/*     */     }
/*  99 */     synchronized (backingStore()) {
/* 100 */       if (paramContactInfo != null) {
/* 101 */         this.connectionCache.remove(paramContactInfo);
/*     */       }
/* 103 */       dprintStatistics();
/*     */     }
/*     */   }
/*     */ 
/*     */   public Collection values()
/*     */   {
/* 114 */     return this.connectionCache.values();
/*     */   }
/*     */ 
/*     */   protected Object backingStore()
/*     */   {
/* 119 */     return this.connectionCache;
/*     */   }
/*     */ 
/*     */   protected void registerWithMonitoring()
/*     */   {
/* 125 */     MonitoredObject localMonitoredObject1 = this.orb.getMonitoringManager().getRootMonitoredObject();
/*     */ 
/* 129 */     MonitoredObject localMonitoredObject2 = localMonitoredObject1.getChild("Connections");
/*     */ 
/* 131 */     if (localMonitoredObject2 == null) {
/* 132 */       localMonitoredObject2 = MonitoringFactories.getMonitoredObjectFactory().createMonitoredObject("Connections", "Statistics on inbound/outbound connections");
/*     */ 
/* 137 */       localMonitoredObject1.addChild(localMonitoredObject2);
/*     */     }
/*     */ 
/* 141 */     MonitoredObject localMonitoredObject3 = localMonitoredObject2.getChild("Outbound");
/*     */ 
/* 144 */     if (localMonitoredObject3 == null) {
/* 145 */       localMonitoredObject3 = MonitoringFactories.getMonitoredObjectFactory().createMonitoredObject("Outbound", "Statistics on outbound connections");
/*     */ 
/* 150 */       localMonitoredObject2.addChild(localMonitoredObject3);
/*     */     }
/*     */ 
/* 154 */     MonitoredObject localMonitoredObject4 = localMonitoredObject3.getChild(getMonitoringName());
/*     */ 
/* 156 */     if (localMonitoredObject4 == null) {
/* 157 */       localMonitoredObject4 = MonitoringFactories.getMonitoredObjectFactory().createMonitoredObject(getMonitoringName(), "Connection statistics");
/*     */ 
/* 162 */       localMonitoredObject3.addChild(localMonitoredObject4);
/*     */     }
/*     */ 
/* 168 */     Object localObject = new LongMonitoredAttributeBase("NumberOfConnections", "The total number of connections")
/*     */     {
/*     */       public Object getValue()
/*     */       {
/* 174 */         return new Long(CorbaOutboundConnectionCacheImpl.this.numberOfConnections());
/*     */       }
/*     */     };
/* 177 */     localMonitoredObject4.addAttribute((MonitoredAttribute)localObject);
/*     */ 
/* 180 */     localObject = new LongMonitoredAttributeBase("NumberOfIdleConnections", "The number of idle connections")
/*     */     {
/*     */       public Object getValue()
/*     */       {
/* 186 */         return new Long(CorbaOutboundConnectionCacheImpl.this.numberOfIdleConnections());
/*     */       }
/*     */     };
/* 189 */     localMonitoredObject4.addAttribute((MonitoredAttribute)localObject);
/*     */ 
/* 192 */     localObject = new LongMonitoredAttributeBase("NumberOfBusyConnections", "The number of busy connections")
/*     */     {
/*     */       public Object getValue()
/*     */       {
/* 198 */         return new Long(CorbaOutboundConnectionCacheImpl.this.numberOfBusyConnections());
/*     */       }
/*     */     };
/* 201 */     localMonitoredObject4.addAttribute((MonitoredAttribute)localObject);
/*     */   }
/*     */ 
/*     */   protected void dprint(String paramString)
/*     */   {
/* 206 */     ORBUtility.dprint("CorbaOutboundConnectionCacheImpl", paramString);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.transport.CorbaOutboundConnectionCacheImpl
 * JD-Core Version:    0.6.2
 */