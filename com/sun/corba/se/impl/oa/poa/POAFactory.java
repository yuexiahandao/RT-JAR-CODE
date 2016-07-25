/*     */ package com.sun.corba.se.impl.oa.poa;
/*     */ 
/*     */ import com.sun.corba.se.impl.logging.OMGSystemException;
/*     */ import com.sun.corba.se.impl.logging.POASystemException;
/*     */ import com.sun.corba.se.spi.ior.ObjectAdapterId;
/*     */ import com.sun.corba.se.spi.oa.ObjectAdapter;
/*     */ import com.sun.corba.se.spi.oa.ObjectAdapterFactory;
/*     */ import com.sun.corba.se.spi.orb.ORB;
/*     */ import com.sun.corba.se.spi.orbutil.closure.Closure;
/*     */ import com.sun.corba.se.spi.orbutil.closure.ClosureFactory;
/*     */ import com.sun.corba.se.spi.resolver.LocalResolver;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.WeakHashMap;
/*     */ import org.omg.CORBA.OBJECT_NOT_EXIST;
/*     */ import org.omg.CORBA.ORBPackage.InvalidName;
/*     */ import org.omg.CORBA.TRANSIENT;
/*     */ import org.omg.PortableServer.POA;
/*     */ import org.omg.PortableServer.POAManager;
/*     */ import org.omg.PortableServer.POAManagerPackage.AdapterInactive;
/*     */ import org.omg.PortableServer.POAPackage.AdapterNonExistent;
/*     */ import org.omg.PortableServer.Servant;
/*     */ import org.omg.PortableServer.portable.Delegate;
/*     */ 
/*     */ public class POAFactory
/*     */   implements ObjectAdapterFactory
/*     */ {
/*  69 */   private Map exportedServantsToPOA = new WeakHashMap();
/*     */   private Set poaManagers;
/*     */   private int poaManagerId;
/*     */   private int poaId;
/*     */   private POAImpl rootPOA;
/*     */   private DelegateImpl delegateImpl;
/*     */   private ORB orb;
/*     */   private POASystemException wrapper;
/*     */   private OMGSystemException omgWrapper;
/*  79 */   private boolean isShuttingDown = false;
/*     */ 
/*     */   public POASystemException getWrapper()
/*     */   {
/*  83 */     return this.wrapper;
/*     */   }
/*     */ 
/*     */   public POAFactory()
/*     */   {
/*  90 */     this.poaManagers = Collections.synchronizedSet(new HashSet(4));
/*  91 */     this.poaManagerId = 0;
/*  92 */     this.poaId = 0;
/*  93 */     this.rootPOA = null;
/*  94 */     this.delegateImpl = null;
/*  95 */     this.orb = null;
/*     */   }
/*     */ 
/*     */   public synchronized POA lookupPOA(Servant paramServant)
/*     */   {
/* 100 */     return (POA)this.exportedServantsToPOA.get(paramServant);
/*     */   }
/*     */ 
/*     */   public synchronized void registerPOAForServant(POA paramPOA, Servant paramServant)
/*     */   {
/* 105 */     this.exportedServantsToPOA.put(paramServant, paramPOA);
/*     */   }
/*     */ 
/*     */   public synchronized void unregisterPOAForServant(POA paramPOA, Servant paramServant)
/*     */   {
/* 110 */     this.exportedServantsToPOA.remove(paramServant);
/*     */   }
/*     */ 
/*     */   public void init(ORB paramORB)
/*     */   {
/* 117 */     this.orb = paramORB;
/* 118 */     this.wrapper = POASystemException.get(paramORB, "oa.lifecycle");
/*     */ 
/* 120 */     this.omgWrapper = OMGSystemException.get(paramORB, "oa.lifecycle");
/*     */ 
/* 122 */     this.delegateImpl = new DelegateImpl(paramORB, this);
/* 123 */     registerRootPOA();
/*     */ 
/* 125 */     POACurrent localPOACurrent = new POACurrent(paramORB);
/* 126 */     paramORB.getLocalResolver().register("POACurrent", ClosureFactory.makeConstant(localPOACurrent));
/*     */   }
/*     */ 
/*     */   public ObjectAdapter find(ObjectAdapterId paramObjectAdapterId)
/*     */   {
/* 132 */     POA localPOA = null;
/*     */     try {
/* 134 */       int i = 1;
/* 135 */       Iterator localIterator = paramObjectAdapterId.iterator();
/* 136 */       localPOA = getRootPOA();
/* 137 */       while (localIterator.hasNext()) {
/* 138 */         String str = (String)localIterator.next();
/*     */ 
/* 140 */         if (i != 0) {
/* 141 */           if (!str.equals("RootPOA"))
/* 142 */             throw this.wrapper.makeFactoryNotPoa(str);
/* 143 */           i = 0;
/*     */         } else {
/* 145 */           localPOA = localPOA.find_POA(str, true);
/*     */         }
/*     */       }
/*     */     } catch (AdapterNonExistent localAdapterNonExistent) {
/* 149 */       throw this.omgWrapper.noObjectAdaptor(localAdapterNonExistent);
/*     */     } catch (OBJECT_NOT_EXIST localOBJECT_NOT_EXIST) {
/* 151 */       throw localOBJECT_NOT_EXIST;
/*     */     } catch (TRANSIENT localTRANSIENT) {
/* 153 */       throw localTRANSIENT;
/*     */     } catch (Exception localException) {
/* 155 */       throw this.wrapper.poaLookupError(localException);
/*     */     }
/*     */ 
/* 158 */     if (localPOA == null) {
/* 159 */       throw this.wrapper.poaLookupError();
/*     */     }
/* 161 */     return (ObjectAdapter)localPOA;
/*     */   }
/*     */ 
/*     */   public void shutdown(boolean paramBoolean)
/*     */   {
/* 168 */     Iterator localIterator = null;
/* 169 */     synchronized (this) {
/* 170 */       this.isShuttingDown = true;
/* 171 */       localIterator = new HashSet(this.poaManagers).iterator();
/*     */     }
/*     */ 
/* 174 */     while (localIterator.hasNext())
/*     */       try {
/* 176 */         ((POAManager)localIterator.next()).deactivate(true, paramBoolean);
/*     */       }
/*     */       catch (AdapterInactive localAdapterInactive)
/*     */       {
/*     */       }
/*     */   }
/*     */ 
/*     */   public synchronized void removePoaManager(POAManager paramPOAManager)
/*     */   {
/* 185 */     this.poaManagers.remove(paramPOAManager);
/*     */   }
/*     */ 
/*     */   public synchronized void addPoaManager(POAManager paramPOAManager)
/*     */   {
/* 190 */     this.poaManagers.add(paramPOAManager);
/*     */   }
/*     */ 
/*     */   public synchronized int newPOAManagerId()
/*     */   {
/* 195 */     return this.poaManagerId++;
/*     */   }
/*     */ 
/*     */   public void registerRootPOA()
/*     */   {
/* 203 */     Closure local1 = new Closure() {
/*     */       public java.lang.Object evaluate() {
/* 205 */         return POAImpl.makeRootPOA(POAFactory.this.orb);
/*     */       }
/*     */     };
/* 209 */     this.orb.getLocalResolver().register("RootPOA", ClosureFactory.makeFuture(local1));
/*     */   }
/*     */ 
/*     */   public synchronized POA getRootPOA()
/*     */   {
/* 216 */     if (this.rootPOA == null)
/*     */     {
/* 218 */       if (this.isShuttingDown) {
/* 219 */         throw this.omgWrapper.noObjectAdaptor();
/*     */       }
/*     */       try
/*     */       {
/* 223 */         org.omg.CORBA.Object localObject = this.orb.resolve_initial_references("RootPOA");
/*     */ 
/* 225 */         this.rootPOA = ((POAImpl)localObject);
/*     */       } catch (InvalidName localInvalidName) {
/* 227 */         throw this.wrapper.cantResolveRootPoa(localInvalidName);
/*     */       }
/*     */     }
/*     */ 
/* 231 */     return this.rootPOA;
/*     */   }
/*     */ 
/*     */   public Delegate getDelegateImpl()
/*     */   {
/* 236 */     return this.delegateImpl;
/*     */   }
/*     */ 
/*     */   public synchronized int newPOAId()
/*     */   {
/* 241 */     return this.poaId++;
/*     */   }
/*     */ 
/*     */   public ORB getORB()
/*     */   {
/* 246 */     return this.orb;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.oa.poa.POAFactory
 * JD-Core Version:    0.6.2
 */