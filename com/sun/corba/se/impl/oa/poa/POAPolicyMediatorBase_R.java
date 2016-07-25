/*     */ package com.sun.corba.se.impl.oa.poa;
/*     */ 
/*     */ import com.sun.corba.se.impl.javax.rmi.CORBA.Util;
/*     */ import com.sun.corba.se.impl.logging.POASystemException;
/*     */ import com.sun.corba.se.impl.orbutil.ORBUtility;
/*     */ import com.sun.corba.se.spi.orb.ORB;
/*     */ import java.io.PrintStream;
/*     */ import org.omg.PortableServer.POAPackage.ObjectAlreadyActive;
/*     */ import org.omg.PortableServer.POAPackage.ObjectNotActive;
/*     */ import org.omg.PortableServer.POAPackage.ServantAlreadyActive;
/*     */ import org.omg.PortableServer.POAPackage.ServantNotActive;
/*     */ import org.omg.PortableServer.POAPackage.WrongPolicy;
/*     */ import org.omg.PortableServer.Servant;
/*     */ 
/*     */ public abstract class POAPolicyMediatorBase_R extends POAPolicyMediatorBase
/*     */ {
/*     */   protected ActiveObjectMap activeObjectMap;
/*     */ 
/*     */   POAPolicyMediatorBase_R(Policies paramPolicies, POAImpl paramPOAImpl)
/*     */   {
/*  50 */     super(paramPolicies, paramPOAImpl);
/*     */ 
/*  53 */     if (!paramPolicies.retainServants()) {
/*  54 */       throw paramPOAImpl.invocationWrapper().policyMediatorBadPolicyInFactory();
/*     */     }
/*  56 */     this.activeObjectMap = ActiveObjectMap.create(paramPOAImpl, !this.isUnique);
/*     */   }
/*     */ 
/*     */   public void returnServant()
/*     */   {
/*     */   }
/*     */ 
/*     */   public void clearAOM()
/*     */   {
/*  66 */     this.activeObjectMap.clear();
/*  67 */     this.activeObjectMap = null;
/*     */   }
/*     */ 
/*     */   protected Servant internalKeyToServant(ActiveObjectMap.Key paramKey)
/*     */   {
/*  72 */     AOMEntry localAOMEntry = this.activeObjectMap.get(paramKey);
/*  73 */     if (localAOMEntry == null) {
/*  74 */       return null;
/*     */     }
/*  76 */     return this.activeObjectMap.getServant(localAOMEntry);
/*     */   }
/*     */ 
/*     */   protected Servant internalIdToServant(byte[] paramArrayOfByte)
/*     */   {
/*  81 */     ActiveObjectMap.Key localKey = new ActiveObjectMap.Key(paramArrayOfByte);
/*  82 */     return internalKeyToServant(localKey);
/*     */   }
/*     */ 
/*     */   protected void activateServant(ActiveObjectMap.Key paramKey, AOMEntry paramAOMEntry, Servant paramServant)
/*     */   {
/*  87 */     setDelegate(paramServant, paramKey.id);
/*     */ 
/*  89 */     if (this.orb.shutdownDebugFlag) {
/*  90 */       System.out.println("Activating object " + paramServant + " with POA " + this.poa);
/*     */     }
/*     */ 
/*  94 */     this.activeObjectMap.putServant(paramServant, paramAOMEntry);
/*     */ 
/*  96 */     if (Util.isInstanceDefined()) {
/*  97 */       POAManagerImpl localPOAManagerImpl = (POAManagerImpl)this.poa.the_POAManager();
/*  98 */       POAFactory localPOAFactory = localPOAManagerImpl.getFactory();
/*  99 */       localPOAFactory.registerPOAForServant(this.poa, paramServant);
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void activateObject(byte[] paramArrayOfByte, Servant paramServant)
/*     */     throws WrongPolicy, ServantAlreadyActive, ObjectAlreadyActive
/*     */   {
/* 106 */     if ((this.isUnique) && (this.activeObjectMap.contains(paramServant)))
/* 107 */       throw new ServantAlreadyActive();
/* 108 */     ActiveObjectMap.Key localKey = new ActiveObjectMap.Key(paramArrayOfByte);
/*     */ 
/* 110 */     AOMEntry localAOMEntry = this.activeObjectMap.get(localKey);
/*     */ 
/* 113 */     localAOMEntry.activateObject();
/* 114 */     activateServant(localKey, localAOMEntry, paramServant);
/*     */   }
/*     */ 
/*     */   public Servant deactivateObject(byte[] paramArrayOfByte)
/*     */     throws ObjectNotActive, WrongPolicy
/*     */   {
/* 120 */     ActiveObjectMap.Key localKey = new ActiveObjectMap.Key(paramArrayOfByte);
/* 121 */     return deactivateObject(localKey);
/*     */   }
/*     */ 
/*     */   protected void deactivateHelper(ActiveObjectMap.Key paramKey, AOMEntry paramAOMEntry, Servant paramServant)
/*     */     throws ObjectNotActive, WrongPolicy
/*     */   {
/* 130 */     this.activeObjectMap.remove(paramKey);
/*     */ 
/* 132 */     if (Util.isInstanceDefined()) {
/* 133 */       POAManagerImpl localPOAManagerImpl = (POAManagerImpl)this.poa.the_POAManager();
/* 134 */       POAFactory localPOAFactory = localPOAManagerImpl.getFactory();
/* 135 */       localPOAFactory.unregisterPOAForServant(this.poa, paramServant);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Servant deactivateObject(ActiveObjectMap.Key paramKey)
/*     */     throws ObjectNotActive, WrongPolicy
/*     */   {
/* 142 */     if (this.orb.poaDebugFlag) {
/* 143 */       ORBUtility.dprint(this, "Calling deactivateObject for key " + paramKey);
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 148 */       AOMEntry localAOMEntry = this.activeObjectMap.get(paramKey);
/* 149 */       if (localAOMEntry == null) {
/* 150 */         throw new ObjectNotActive();
/*     */       }
/* 152 */       Servant localServant1 = this.activeObjectMap.getServant(localAOMEntry);
/* 153 */       if (localServant1 == null) {
/* 154 */         throw new ObjectNotActive();
/*     */       }
/* 156 */       if (this.orb.poaDebugFlag) {
/* 157 */         System.out.println("Deactivating object " + localServant1 + " with POA " + this.poa);
/*     */       }
/*     */ 
/* 160 */       deactivateHelper(paramKey, localAOMEntry, localServant1);
/*     */ 
/* 162 */       return localServant1;
/*     */     } finally {
/* 164 */       if (this.orb.poaDebugFlag)
/* 165 */         ORBUtility.dprint(this, "Exiting deactivateObject");
/*     */     }
/*     */   }
/*     */ 
/*     */   public byte[] servantToId(Servant paramServant)
/*     */     throws ServantNotActive, WrongPolicy
/*     */   {
/* 175 */     if ((!this.isUnique) && (!this.isImplicit))
/* 176 */       throw new WrongPolicy();
/*     */     Object localObject;
/* 178 */     if (this.isUnique) {
/* 179 */       localObject = this.activeObjectMap.getKey(paramServant);
/* 180 */       if (localObject != null) {
/* 181 */         return ((ActiveObjectMap.Key)localObject).id;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 186 */     if (this.isImplicit) {
/*     */       try {
/* 188 */         localObject = newSystemId();
/* 189 */         activateObject((byte[])localObject, paramServant);
/* 190 */         return localObject;
/*     */       }
/*     */       catch (ObjectAlreadyActive localObjectAlreadyActive) {
/* 193 */         throw this.poa.invocationWrapper().servantToIdOaa(localObjectAlreadyActive);
/*     */       } catch (ServantAlreadyActive localServantAlreadyActive) {
/* 195 */         throw this.poa.invocationWrapper().servantToIdSaa(localServantAlreadyActive);
/*     */       } catch (WrongPolicy localWrongPolicy) {
/* 197 */         throw this.poa.invocationWrapper().servantToIdWp(localWrongPolicy);
/*     */       }
/*     */     }
/* 200 */     throw new ServantNotActive();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.oa.poa.POAPolicyMediatorBase_R
 * JD-Core Version:    0.6.2
 */