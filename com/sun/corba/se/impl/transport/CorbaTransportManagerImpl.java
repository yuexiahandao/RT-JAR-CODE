/*     */ package com.sun.corba.se.impl.transport;
/*     */ 
/*     */ import com.sun.corba.se.impl.oa.poa.Policies;
/*     */ import com.sun.corba.se.impl.orbutil.ORBUtility;
/*     */ import com.sun.corba.se.pept.transport.Acceptor;
/*     */ import com.sun.corba.se.pept.transport.ByteBufferPool;
/*     */ import com.sun.corba.se.pept.transport.ConnectionCache;
/*     */ import com.sun.corba.se.pept.transport.ContactInfo;
/*     */ import com.sun.corba.se.pept.transport.InboundConnectionCache;
/*     */ import com.sun.corba.se.pept.transport.OutboundConnectionCache;
/*     */ import com.sun.corba.se.pept.transport.Selector;
/*     */ import com.sun.corba.se.pept.transport.TransportManager;
/*     */ import com.sun.corba.se.spi.ior.IORTemplate;
/*     */ import com.sun.corba.se.spi.ior.ObjectAdapterId;
/*     */ import com.sun.corba.se.spi.orb.ORB;
/*     */ import com.sun.corba.se.spi.transport.CorbaAcceptor;
/*     */ import com.sun.corba.se.spi.transport.CorbaTransportManager;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class CorbaTransportManagerImpl
/*     */   implements CorbaTransportManager
/*     */ {
/*     */   protected ORB orb;
/*     */   protected List acceptors;
/*     */   protected Map outboundConnectionCaches;
/*     */   protected Map inboundConnectionCaches;
/*     */   protected Selector selector;
/*     */ 
/*     */   public CorbaTransportManagerImpl(ORB paramORB)
/*     */   {
/*  75 */     this.orb = paramORB;
/*  76 */     this.acceptors = new ArrayList();
/*  77 */     this.outboundConnectionCaches = new HashMap();
/*  78 */     this.inboundConnectionCaches = new HashMap();
/*  79 */     this.selector = new SelectorImpl(paramORB);
/*     */   }
/*     */ 
/*     */   public ByteBufferPool getByteBufferPool(int paramInt)
/*     */   {
/*  89 */     throw new RuntimeException();
/*     */   }
/*     */ 
/*     */   public OutboundConnectionCache getOutboundConnectionCache(ContactInfo paramContactInfo)
/*     */   {
/*  95 */     synchronized (paramContactInfo) {
/*  96 */       if (paramContactInfo.getConnectionCache() == null) {
/*  97 */         Object localObject1 = null;
/*  98 */         synchronized (this.outboundConnectionCaches) {
/*  99 */           localObject1 = (OutboundConnectionCache)this.outboundConnectionCaches.get(paramContactInfo.getConnectionCacheType());
/*     */ 
/* 102 */           if (localObject1 == null)
/*     */           {
/* 105 */             localObject1 = new CorbaOutboundConnectionCacheImpl(this.orb, paramContactInfo);
/*     */ 
/* 108 */             this.outboundConnectionCaches.put(paramContactInfo.getConnectionCacheType(), localObject1);
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 113 */         paramContactInfo.setConnectionCache((OutboundConnectionCache)localObject1);
/*     */       }
/* 115 */       return paramContactInfo.getConnectionCache();
/*     */     }
/*     */   }
/*     */ 
/*     */   public Collection getOutboundConnectionCaches()
/*     */   {
/* 121 */     return this.outboundConnectionCaches.values();
/*     */   }
/*     */ 
/*     */   public InboundConnectionCache getInboundConnectionCache(Acceptor paramAcceptor)
/*     */   {
/* 127 */     synchronized (paramAcceptor) {
/* 128 */       if (paramAcceptor.getConnectionCache() == null) {
/* 129 */         Object localObject1 = null;
/* 130 */         synchronized (this.inboundConnectionCaches) {
/* 131 */           localObject1 = (InboundConnectionCache)this.inboundConnectionCaches.get(paramAcceptor.getConnectionCacheType());
/*     */ 
/* 134 */           if (localObject1 == null)
/*     */           {
/* 137 */             localObject1 = new CorbaInboundConnectionCacheImpl(this.orb, paramAcceptor);
/*     */ 
/* 140 */             this.inboundConnectionCaches.put(paramAcceptor.getConnectionCacheType(), localObject1);
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 145 */         paramAcceptor.setConnectionCache((InboundConnectionCache)localObject1);
/*     */       }
/* 147 */       return paramAcceptor.getConnectionCache();
/*     */     }
/*     */   }
/*     */ 
/*     */   public Collection getInboundConnectionCaches()
/*     */   {
/* 153 */     return this.inboundConnectionCaches.values();
/*     */   }
/*     */ 
/*     */   public Selector getSelector(int paramInt)
/*     */   {
/* 158 */     return this.selector;
/*     */   }
/*     */ 
/*     */   public synchronized void registerAcceptor(Acceptor paramAcceptor)
/*     */   {
/* 163 */     if (this.orb.transportDebugFlag) {
/* 164 */       dprint(".registerAcceptor->: " + paramAcceptor);
/*     */     }
/* 166 */     this.acceptors.add(paramAcceptor);
/* 167 */     if (this.orb.transportDebugFlag)
/* 168 */       dprint(".registerAcceptor<-: " + paramAcceptor);
/*     */   }
/*     */ 
/*     */   public Collection getAcceptors()
/*     */   {
/* 174 */     return getAcceptors(null, null);
/*     */   }
/*     */ 
/*     */   public synchronized void unregisterAcceptor(Acceptor paramAcceptor)
/*     */   {
/* 179 */     this.acceptors.remove(paramAcceptor);
/*     */   }
/*     */ 
/*     */   public void close()
/*     */   {
/*     */     try {
/* 185 */       if (this.orb.transportDebugFlag) {
/* 186 */         dprint(".close->");
/*     */       }
/* 188 */       for (Iterator localIterator = this.outboundConnectionCaches.values().iterator(); localIterator.hasNext(); ) { localObject1 = localIterator.next();
/* 189 */         ((ConnectionCache)localObject1).close();
/*     */       }
/* 191 */       Object localObject1;
/* 191 */       for (localIterator = this.inboundConnectionCaches.values().iterator(); localIterator.hasNext(); ) { localObject1 = localIterator.next();
/* 192 */         ((ConnectionCache)localObject1).close();
/*     */       }
/* 194 */       getSelector(0).close();
/*     */     } finally {
/* 196 */       if (this.orb.transportDebugFlag)
/* 197 */         dprint(".close<-");
/*     */     }
/*     */   }
/*     */ 
/*     */   public Collection getAcceptors(String paramString, ObjectAdapterId paramObjectAdapterId)
/*     */   {
/* 214 */     Iterator localIterator = this.acceptors.iterator();
/* 215 */     while (localIterator.hasNext()) {
/* 216 */       Acceptor localAcceptor = (Acceptor)localIterator.next();
/* 217 */       if ((localAcceptor.initialize()) && 
/* 218 */         (localAcceptor.shouldRegisterAcceptEvent())) {
/* 219 */         this.orb.getTransportManager().getSelector(0).registerForEvent(localAcceptor.getEventHandler());
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 224 */     return this.acceptors;
/*     */   }
/*     */ 
/*     */   public void addToIORTemplate(IORTemplate paramIORTemplate, Policies paramPolicies, String paramString1, String paramString2, ObjectAdapterId paramObjectAdapterId)
/*     */   {
/* 234 */     Iterator localIterator = getAcceptors(paramString2, paramObjectAdapterId).iterator();
/*     */ 
/* 236 */     while (localIterator.hasNext()) {
/* 237 */       CorbaAcceptor localCorbaAcceptor = (CorbaAcceptor)localIterator.next();
/* 238 */       localCorbaAcceptor.addToIORTemplate(paramIORTemplate, paramPolicies, paramString1);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void dprint(String paramString)
/*     */   {
/* 250 */     ORBUtility.dprint("CorbaTransportManagerImpl", paramString);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.transport.CorbaTransportManagerImpl
 * JD-Core Version:    0.6.2
 */