/*     */ package com.sun.corba.se.impl.oa.poa;
/*     */ 
/*     */ import com.sun.corba.se.impl.logging.OMGSystemException;
/*     */ import com.sun.corba.se.impl.logging.POASystemException;
/*     */ import com.sun.corba.se.impl.oa.NullServantImpl;
/*     */ import com.sun.corba.se.impl.orbutil.ORBUtility;
/*     */ import com.sun.corba.se.spi.oa.NullServant;
/*     */ import com.sun.corba.se.spi.oa.OAInvocationInfo;
/*     */ import com.sun.corba.se.spi.orb.ORB;
/*     */ import java.util.Set;
/*     */ import org.omg.CORBA.SystemException;
/*     */ import org.omg.PortableServer.ForwardRequest;
/*     */ import org.omg.PortableServer.POAPackage.NoServant;
/*     */ import org.omg.PortableServer.POAPackage.ObjectNotActive;
/*     */ import org.omg.PortableServer.POAPackage.WrongPolicy;
/*     */ import org.omg.PortableServer.Servant;
/*     */ import org.omg.PortableServer.ServantActivator;
/*     */ import org.omg.PortableServer.ServantManager;
/*     */ 
/*     */ public class POAPolicyMediatorImpl_R_USM extends POAPolicyMediatorBase_R
/*     */ {
/*     */   protected ServantActivator activator;
/*     */ 
/*     */   POAPolicyMediatorImpl_R_USM(Policies paramPolicies, POAImpl paramPOAImpl)
/*     */   {
/*  63 */     super(paramPolicies, paramPOAImpl);
/*  64 */     this.activator = null;
/*     */ 
/*  66 */     if (!paramPolicies.useServantManager())
/*  67 */       throw paramPOAImpl.invocationWrapper().policyMediatorBadPolicyInFactory();
/*     */   }
/*     */ 
/*     */   private AOMEntry enterEntry(ActiveObjectMap.Key paramKey)
/*     */   {
/*  81 */     AOMEntry localAOMEntry = null;
/*     */     int i;
/*     */     do
/*     */     {
/*  84 */       i = 0;
/*  85 */       localAOMEntry = this.activeObjectMap.get(paramKey);
/*     */       try
/*     */       {
/*  88 */         localAOMEntry.enter();
/*     */       } catch (Exception localException) {
/*  90 */         i = 1;
/*     */       }
/*     */     }
/*  92 */     while (i != 0);
/*     */ 
/*  94 */     return localAOMEntry;
/*     */   }
/*     */ 
/*     */   protected Object internalGetServant(byte[] paramArrayOfByte, String paramString)
/*     */     throws ForwardRequest
/*     */   {
/* 100 */     if (this.poa.getDebug()) {
/* 101 */       ORBUtility.dprint(this, "Calling POAPolicyMediatorImpl_R_USM.internalGetServant for poa " + this.poa + " operation=" + paramString);
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 107 */       ActiveObjectMap.Key localKey = new ActiveObjectMap.Key(paramArrayOfByte);
/* 108 */       AOMEntry localAOMEntry = enterEntry(localKey);
/* 109 */       Object localObject1 = this.activeObjectMap.getServant(localAOMEntry);
/* 110 */       if (localObject1 != null) {
/* 111 */         if (this.poa.getDebug()) {
/* 112 */           ORBUtility.dprint(this, "internalGetServant: servant already activated");
/*     */         }
/*     */ 
/* 116 */         return localObject1;
/*     */       }
/*     */ 
/* 119 */       if (this.activator == null) {
/* 120 */         if (this.poa.getDebug()) {
/* 121 */           ORBUtility.dprint(this, "internalGetServant: no servant activator in POA");
/*     */         }
/*     */ 
/* 125 */         localAOMEntry.incarnateFailure();
/* 126 */         throw this.poa.invocationWrapper().poaNoServantManager();
/*     */       }
/*     */ 
/*     */       try
/*     */       {
/* 134 */         if (this.poa.getDebug()) {
/* 135 */           ORBUtility.dprint(this, "internalGetServant: upcall to incarnate");
/*     */         }
/*     */ 
/* 139 */         this.poa.unlock();
/*     */ 
/* 141 */         localObject1 = this.activator.incarnate(paramArrayOfByte, this.poa);
/*     */ 
/* 143 */         if (localObject1 == null)
/* 144 */           localObject1 = new NullServantImpl(this.poa.omgInvocationWrapper().nullServantReturned());
/*     */       }
/*     */       catch (ForwardRequest localForwardRequest) {
/* 147 */         if (this.poa.getDebug()) {
/* 148 */           ORBUtility.dprint(this, "internalGetServant: incarnate threw ForwardRequest");
/*     */         }
/*     */ 
/* 152 */         throw localForwardRequest;
/*     */       } catch (SystemException localSystemException) {
/* 154 */         if (this.poa.getDebug()) {
/* 155 */           ORBUtility.dprint(this, "internalGetServant: incarnate threw SystemException " + localSystemException);
/*     */         }
/*     */ 
/* 159 */         throw localSystemException;
/*     */       } catch (Throwable localThrowable) {
/* 161 */         if (this.poa.getDebug()) {
/* 162 */           ORBUtility.dprint(this, "internalGetServant: incarnate threw Throwable " + localThrowable);
/*     */         }
/*     */ 
/* 166 */         throw this.poa.invocationWrapper().poaServantActivatorLookupFailed(localThrowable);
/*     */       }
/*     */       finally {
/* 169 */         this.poa.lock();
/*     */ 
/* 175 */         if ((localObject1 == null) || ((localObject1 instanceof NullServant))) {
/* 176 */           if (this.poa.getDebug()) {
/* 177 */             ORBUtility.dprint(this, "internalGetServant: incarnate failed");
/*     */           }
/*     */ 
/* 189 */           localAOMEntry.incarnateFailure();
/*     */         }
/*     */         else
/*     */         {
/* 195 */           if (this.isUnique)
/*     */           {
/* 197 */             if (this.activeObjectMap.contains((Servant)localObject1)) {
/* 198 */               if (this.poa.getDebug()) {
/* 199 */                 ORBUtility.dprint(this, "internalGetServant: servant already assigned to ID");
/*     */               }
/*     */ 
/* 203 */               localAOMEntry.incarnateFailure();
/* 204 */               throw this.poa.invocationWrapper().poaServantNotUnique();
/*     */             }
/*     */           }
/*     */ 
/* 208 */           if (this.poa.getDebug()) {
/* 209 */             ORBUtility.dprint(this, "internalGetServant: incarnate complete");
/*     */           }
/*     */ 
/* 213 */           localAOMEntry.incarnateComplete();
/* 214 */           activateServant(localKey, localAOMEntry, (Servant)localObject1);
/*     */         }
/*     */       }
/*     */ 
/* 218 */       return localObject1;
/*     */     } finally {
/* 220 */       if (this.poa.getDebug())
/* 221 */         ORBUtility.dprint(this, "Exiting POAPolicyMediatorImpl_R_USM.internalGetServant for poa " + this.poa);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void returnServant()
/*     */   {
/* 230 */     OAInvocationInfo localOAInvocationInfo = this.orb.peekInvocationInfo();
/* 231 */     byte[] arrayOfByte = localOAInvocationInfo.id();
/* 232 */     ActiveObjectMap.Key localKey = new ActiveObjectMap.Key(arrayOfByte);
/* 233 */     AOMEntry localAOMEntry = this.activeObjectMap.get(localKey);
/* 234 */     localAOMEntry.exit();
/*     */   }
/*     */ 
/*     */   public void etherealizeAll()
/*     */   {
/* 239 */     if (this.activator != null) {
/* 240 */       Set localSet = this.activeObjectMap.keySet();
/*     */ 
/* 244 */       ActiveObjectMap.Key[] arrayOfKey = (ActiveObjectMap.Key[])localSet.toArray(new ActiveObjectMap.Key[localSet.size()]);
/*     */ 
/* 248 */       for (int i = 0; i < localSet.size(); i++) {
/* 249 */         ActiveObjectMap.Key localKey = arrayOfKey[i];
/* 250 */         AOMEntry localAOMEntry = this.activeObjectMap.get(localKey);
/* 251 */         Servant localServant = this.activeObjectMap.getServant(localAOMEntry);
/* 252 */         if (localServant != null) {
/* 253 */           boolean bool = this.activeObjectMap.hasMultipleIDs(localAOMEntry);
/*     */ 
/* 261 */           localAOMEntry.startEtherealize(null);
/*     */           try {
/* 263 */             this.poa.unlock();
/*     */             try {
/* 265 */               this.activator.etherealize(localKey.id, this.poa, localServant, true, bool);
/*     */             }
/*     */             catch (Exception localException) {
/*     */             }
/*     */           }
/*     */           finally {
/* 271 */             this.poa.lock();
/* 272 */             localAOMEntry.etherealizeComplete();
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public ServantManager getServantManager() throws WrongPolicy
/*     */   {
/* 281 */     return this.activator;
/*     */   }
/*     */ 
/*     */   public void setServantManager(ServantManager paramServantManager)
/*     */     throws WrongPolicy
/*     */   {
/* 287 */     if (this.activator != null) {
/* 288 */       throw this.poa.invocationWrapper().servantManagerAlreadySet();
/*     */     }
/* 290 */     if ((paramServantManager instanceof ServantActivator))
/* 291 */       this.activator = ((ServantActivator)paramServantManager);
/*     */     else
/* 293 */       throw this.poa.invocationWrapper().servantManagerBadType();
/*     */   }
/*     */ 
/*     */   public Servant getDefaultServant() throws NoServant, WrongPolicy
/*     */   {
/* 298 */     throw new WrongPolicy();
/*     */   }
/*     */ 
/*     */   public void setDefaultServant(Servant paramServant) throws WrongPolicy
/*     */   {
/* 303 */     throw new WrongPolicy();
/*     */   }
/*     */ 
/*     */   public void deactivateHelper(ActiveObjectMap.Key paramKey, AOMEntry paramAOMEntry, Servant paramServant)
/*     */     throws ObjectNotActive, WrongPolicy
/*     */   {
/* 361 */     if (this.activator == null) {
/* 362 */       throw this.poa.invocationWrapper().poaNoServantManager();
/*     */     }
/* 364 */     Etherealizer localEtherealizer = new Etherealizer(this, paramKey, paramAOMEntry, paramServant, this.poa.getDebug());
/* 365 */     paramAOMEntry.startEtherealize(localEtherealizer);
/*     */   }
/*     */ 
/*     */   public Servant idToServant(byte[] paramArrayOfByte)
/*     */     throws WrongPolicy, ObjectNotActive
/*     */   {
/* 371 */     ActiveObjectMap.Key localKey = new ActiveObjectMap.Key(paramArrayOfByte);
/* 372 */     AOMEntry localAOMEntry = this.activeObjectMap.get(localKey);
/*     */ 
/* 374 */     Servant localServant = this.activeObjectMap.getServant(localAOMEntry);
/* 375 */     if (localServant != null) {
/* 376 */       return localServant;
/*     */     }
/* 378 */     throw new ObjectNotActive();
/*     */   }
/*     */ 
/*     */   class Etherealizer extends Thread
/*     */   {
/*     */     private POAPolicyMediatorImpl_R_USM mediator;
/*     */     private ActiveObjectMap.Key key;
/*     */     private AOMEntry entry;
/*     */     private Servant servant;
/*     */     private boolean debug;
/*     */ 
/*     */     public Etherealizer(POAPolicyMediatorImpl_R_USM paramKey, ActiveObjectMap.Key paramAOMEntry, AOMEntry paramServant, Servant paramBoolean, boolean arg6)
/*     */     {
/* 317 */       this.mediator = paramKey;
/* 318 */       this.key = paramAOMEntry;
/* 319 */       this.entry = paramServant;
/* 320 */       this.servant = paramBoolean;
/*     */       boolean bool;
/* 321 */       this.debug = bool;
/*     */     }
/*     */ 
/*     */     public void run() {
/* 325 */       if (this.debug) {
/* 326 */         ORBUtility.dprint(this, "Calling Etherealizer.run on key " + this.key);
/*     */       }
/*     */       try
/*     */       {
/*     */         try
/*     */         {
/* 332 */           this.mediator.activator.etherealize(this.key.id, this.mediator.poa, this.servant, false, this.mediator.activeObjectMap.hasMultipleIDs(this.entry));
/*     */         }
/*     */         catch (Exception localException)
/*     */         {
/*     */         }
/*     */         try
/*     */         {
/* 339 */           this.mediator.poa.lock();
/*     */ 
/* 341 */           this.entry.etherealizeComplete();
/* 342 */           this.mediator.activeObjectMap.remove(this.key);
/*     */ 
/* 344 */           POAManagerImpl localPOAManagerImpl = (POAManagerImpl)this.mediator.poa.the_POAManager();
/* 345 */           POAFactory localPOAFactory = localPOAManagerImpl.getFactory();
/* 346 */           localPOAFactory.unregisterPOAForServant(this.mediator.poa, this.servant);
/*     */         } finally {
/* 348 */           this.mediator.poa.unlock();
/*     */         }
/*     */       } finally {
/* 351 */         if (this.debug)
/* 352 */           ORBUtility.dprint(this, "Exiting Etherealizer.run");
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.oa.poa.POAPolicyMediatorImpl_R_USM
 * JD-Core Version:    0.6.2
 */