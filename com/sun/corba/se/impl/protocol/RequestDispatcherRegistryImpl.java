/*     */ package com.sun.corba.se.impl.protocol;
/*     */ 
/*     */ import com.sun.corba.se.impl.orbutil.DenseIntMapImpl;
/*     */ import com.sun.corba.se.pept.protocol.ClientRequestDispatcher;
/*     */ import com.sun.corba.se.spi.oa.ObjectAdapterFactory;
/*     */ import com.sun.corba.se.spi.orb.ORB;
/*     */ import com.sun.corba.se.spi.protocol.CorbaServerRequestDispatcher;
/*     */ import com.sun.corba.se.spi.protocol.LocalClientRequestDispatcherFactory;
/*     */ import com.sun.corba.se.spi.protocol.RequestDispatcherRegistry;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class RequestDispatcherRegistryImpl
/*     */   implements RequestDispatcherRegistry
/*     */ {
/*     */   private ORB orb;
/*     */   protected int defaultId;
/*     */   private DenseIntMapImpl SDRegistry;
/*     */   private DenseIntMapImpl CSRegistry;
/*     */   private DenseIntMapImpl OAFRegistry;
/*     */   private DenseIntMapImpl LCSFRegistry;
/*     */   private Set objectAdapterFactories;
/*     */   private Set objectAdapterFactoriesView;
/*     */   private Map stringToServerSubcontract;
/*     */ 
/*     */   public RequestDispatcherRegistryImpl(ORB paramORB, int paramInt)
/*     */   {
/*  71 */     this.orb = paramORB;
/*  72 */     this.defaultId = paramInt;
/*  73 */     this.SDRegistry = new DenseIntMapImpl();
/*  74 */     this.CSRegistry = new DenseIntMapImpl();
/*  75 */     this.OAFRegistry = new DenseIntMapImpl();
/*  76 */     this.LCSFRegistry = new DenseIntMapImpl();
/*  77 */     this.objectAdapterFactories = new HashSet();
/*  78 */     this.objectAdapterFactoriesView = Collections.unmodifiableSet(this.objectAdapterFactories);
/*  79 */     this.stringToServerSubcontract = new HashMap();
/*     */   }
/*     */ 
/*     */   public synchronized void registerClientRequestDispatcher(ClientRequestDispatcher paramClientRequestDispatcher, int paramInt)
/*     */   {
/*  85 */     this.CSRegistry.set(paramInt, paramClientRequestDispatcher);
/*     */   }
/*     */ 
/*     */   public synchronized void registerLocalClientRequestDispatcherFactory(LocalClientRequestDispatcherFactory paramLocalClientRequestDispatcherFactory, int paramInt)
/*     */   {
/*  91 */     this.LCSFRegistry.set(paramInt, paramLocalClientRequestDispatcherFactory);
/*     */   }
/*     */ 
/*     */   public synchronized void registerServerRequestDispatcher(CorbaServerRequestDispatcher paramCorbaServerRequestDispatcher, int paramInt)
/*     */   {
/*  97 */     this.SDRegistry.set(paramInt, paramCorbaServerRequestDispatcher);
/*     */   }
/*     */ 
/*     */   public synchronized void registerServerRequestDispatcher(CorbaServerRequestDispatcher paramCorbaServerRequestDispatcher, String paramString)
/*     */   {
/* 103 */     this.stringToServerSubcontract.put(paramString, paramCorbaServerRequestDispatcher);
/*     */   }
/*     */ 
/*     */   public synchronized void registerObjectAdapterFactory(ObjectAdapterFactory paramObjectAdapterFactory, int paramInt)
/*     */   {
/* 109 */     this.objectAdapterFactories.add(paramObjectAdapterFactory);
/* 110 */     this.OAFRegistry.set(paramInt, paramObjectAdapterFactory);
/*     */   }
/*     */ 
/*     */   public CorbaServerRequestDispatcher getServerRequestDispatcher(int paramInt)
/*     */   {
/* 127 */     CorbaServerRequestDispatcher localCorbaServerRequestDispatcher = (CorbaServerRequestDispatcher)this.SDRegistry.get(paramInt);
/*     */ 
/* 129 */     if (localCorbaServerRequestDispatcher == null) {
/* 130 */       localCorbaServerRequestDispatcher = (CorbaServerRequestDispatcher)this.SDRegistry.get(this.defaultId);
/*     */     }
/* 132 */     return localCorbaServerRequestDispatcher;
/*     */   }
/*     */ 
/*     */   public CorbaServerRequestDispatcher getServerRequestDispatcher(String paramString)
/*     */   {
/* 137 */     CorbaServerRequestDispatcher localCorbaServerRequestDispatcher = (CorbaServerRequestDispatcher)this.stringToServerSubcontract.get(paramString);
/*     */ 
/* 140 */     if (localCorbaServerRequestDispatcher == null) {
/* 141 */       localCorbaServerRequestDispatcher = (CorbaServerRequestDispatcher)this.SDRegistry.get(this.defaultId);
/*     */     }
/* 143 */     return localCorbaServerRequestDispatcher;
/*     */   }
/*     */ 
/*     */   public LocalClientRequestDispatcherFactory getLocalClientRequestDispatcherFactory(int paramInt)
/*     */   {
/* 149 */     LocalClientRequestDispatcherFactory localLocalClientRequestDispatcherFactory = (LocalClientRequestDispatcherFactory)this.LCSFRegistry.get(paramInt);
/*     */ 
/* 151 */     if (localLocalClientRequestDispatcherFactory == null) {
/* 152 */       localLocalClientRequestDispatcherFactory = (LocalClientRequestDispatcherFactory)this.LCSFRegistry.get(this.defaultId);
/*     */     }
/*     */ 
/* 155 */     return localLocalClientRequestDispatcherFactory;
/*     */   }
/*     */ 
/*     */   public ClientRequestDispatcher getClientRequestDispatcher(int paramInt)
/*     */   {
/* 160 */     ClientRequestDispatcher localClientRequestDispatcher = (ClientRequestDispatcher)this.CSRegistry.get(paramInt);
/*     */ 
/* 162 */     if (localClientRequestDispatcher == null) {
/* 163 */       localClientRequestDispatcher = (ClientRequestDispatcher)this.CSRegistry.get(this.defaultId);
/*     */     }
/*     */ 
/* 166 */     return localClientRequestDispatcher;
/*     */   }
/*     */ 
/*     */   public ObjectAdapterFactory getObjectAdapterFactory(int paramInt)
/*     */   {
/* 171 */     ObjectAdapterFactory localObjectAdapterFactory = (ObjectAdapterFactory)this.OAFRegistry.get(paramInt);
/*     */ 
/* 173 */     if (localObjectAdapterFactory == null) {
/* 174 */       localObjectAdapterFactory = (ObjectAdapterFactory)this.OAFRegistry.get(this.defaultId);
/*     */     }
/* 176 */     return localObjectAdapterFactory;
/*     */   }
/*     */ 
/*     */   public Set getObjectAdapterFactories()
/*     */   {
/* 181 */     return this.objectAdapterFactoriesView;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.protocol.RequestDispatcherRegistryImpl
 * JD-Core Version:    0.6.2
 */