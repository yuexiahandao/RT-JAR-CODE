/*     */ package com.sun.corba.se.impl.legacy.connection;
/*     */ 
/*     */ import com.sun.corba.se.impl.logging.ORBUtilSystemException;
/*     */ import com.sun.corba.se.impl.orbutil.ORBUtility;
/*     */ import com.sun.corba.se.spi.legacy.connection.LegacyServerSocketEndPointInfo;
/*     */ import com.sun.corba.se.spi.legacy.connection.LegacyServerSocketManager;
/*     */ import com.sun.corba.se.spi.orb.ORB;
/*     */ import com.sun.corba.se.spi.orb.ORBData;
/*     */ import com.sun.corba.se.spi.transport.CorbaTransportManager;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import org.omg.CORBA.CompletionStatus;
/*     */ import org.omg.CORBA.INTERNAL;
/*     */ 
/*     */ public class LegacyServerSocketManagerImpl
/*     */   implements LegacyServerSocketManager
/*     */ {
/*     */   protected ORB orb;
/*     */   private ORBUtilSystemException wrapper;
/*     */ 
/*     */   public LegacyServerSocketManagerImpl(ORB paramORB)
/*     */   {
/*  69 */     this.orb = paramORB;
/*  70 */     this.wrapper = ORBUtilSystemException.get(paramORB, "rpc.transport");
/*     */   }
/*     */ 
/*     */   public int legacyGetTransientServerPort(String paramString)
/*     */   {
/*  82 */     return legacyGetServerPort(paramString, false);
/*     */   }
/*     */ 
/*     */   public synchronized int legacyGetPersistentServerPort(String paramString)
/*     */   {
/*  88 */     if (this.orb.getORBData().getServerIsORBActivated())
/*     */     {
/*  90 */       return legacyGetServerPort(paramString, true);
/*  91 */     }if (this.orb.getORBData().getPersistentPortInitialized())
/*     */     {
/*  93 */       return this.orb.getORBData().getPersistentServerPort();
/*     */     }
/*  95 */     throw this.wrapper.persistentServerportNotSet(CompletionStatus.COMPLETED_MAYBE);
/*     */   }
/*     */ 
/*     */   public synchronized int legacyGetTransientOrPersistentServerPort(String paramString)
/*     */   {
/* 104 */     return legacyGetServerPort(paramString, this.orb.getORBData().getServerIsORBActivated());
/*     */   }
/*     */ 
/*     */   public synchronized LegacyServerSocketEndPointInfo legacyGetEndpoint(String paramString)
/*     */   {
/* 115 */     Iterator localIterator = getAcceptorIterator();
/* 116 */     while (localIterator.hasNext()) {
/* 117 */       LegacyServerSocketEndPointInfo localLegacyServerSocketEndPointInfo = cast(localIterator.next());
/* 118 */       if ((localLegacyServerSocketEndPointInfo != null) && (paramString.equals(localLegacyServerSocketEndPointInfo.getName()))) {
/* 119 */         return localLegacyServerSocketEndPointInfo;
/*     */       }
/*     */     }
/* 122 */     throw new INTERNAL("No acceptor for: " + paramString);
/*     */   }
/*     */ 
/*     */   public boolean legacyIsLocalServerPort(int paramInt)
/*     */   {
/* 130 */     Iterator localIterator = getAcceptorIterator();
/* 131 */     while (localIterator.hasNext()) {
/* 132 */       LegacyServerSocketEndPointInfo localLegacyServerSocketEndPointInfo = cast(localIterator.next());
/* 133 */       if ((localLegacyServerSocketEndPointInfo != null) && (localLegacyServerSocketEndPointInfo.getPort() == paramInt)) {
/* 134 */         return true;
/*     */       }
/*     */     }
/* 137 */     return false;
/*     */   }
/*     */ 
/*     */   private int legacyGetServerPort(String paramString, boolean paramBoolean)
/*     */   {
/* 147 */     Iterator localIterator = getAcceptorIterator();
/* 148 */     while (localIterator.hasNext()) {
/* 149 */       LegacyServerSocketEndPointInfo localLegacyServerSocketEndPointInfo = cast(localIterator.next());
/* 150 */       if ((localLegacyServerSocketEndPointInfo != null) && (localLegacyServerSocketEndPointInfo.getType().equals(paramString))) {
/* 151 */         if (paramBoolean) {
/* 152 */           return localLegacyServerSocketEndPointInfo.getLocatorPort();
/*     */         }
/* 154 */         return localLegacyServerSocketEndPointInfo.getPort();
/*     */       }
/*     */     }
/*     */ 
/* 158 */     return -1;
/*     */   }
/*     */ 
/*     */   private Iterator getAcceptorIterator()
/*     */   {
/* 163 */     Collection localCollection = this.orb.getCorbaTransportManager().getAcceptors(null, null);
/*     */ 
/* 165 */     if (localCollection != null) {
/* 166 */       return localCollection.iterator();
/*     */     }
/*     */ 
/* 169 */     throw this.wrapper.getServerPortCalledBeforeEndpointsInitialized();
/*     */   }
/*     */ 
/*     */   private LegacyServerSocketEndPointInfo cast(Object paramObject)
/*     */   {
/* 174 */     if ((paramObject instanceof LegacyServerSocketEndPointInfo)) {
/* 175 */       return (LegacyServerSocketEndPointInfo)paramObject;
/*     */     }
/* 177 */     return null;
/*     */   }
/*     */ 
/*     */   protected void dprint(String paramString)
/*     */   {
/* 182 */     ORBUtility.dprint("LegacyServerSocketManagerImpl", paramString);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.legacy.connection.LegacyServerSocketManagerImpl
 * JD-Core Version:    0.6.2
 */