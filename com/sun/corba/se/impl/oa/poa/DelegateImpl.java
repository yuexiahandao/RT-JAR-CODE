/*     */ package com.sun.corba.se.impl.oa.poa;
/*     */ 
/*     */ import com.sun.corba.se.impl.logging.POASystemException;
/*     */ import com.sun.corba.se.spi.oa.OAInvocationInfo;
/*     */ import java.util.EmptyStackException;
/*     */ import org.omg.PortableServer.POA;
/*     */ import org.omg.PortableServer.POAPackage.ServantNotActive;
/*     */ import org.omg.PortableServer.POAPackage.WrongPolicy;
/*     */ import org.omg.PortableServer.Servant;
/*     */ import org.omg.PortableServer.portable.Delegate;
/*     */ 
/*     */ public class DelegateImpl
/*     */   implements Delegate
/*     */ {
/*     */   private com.sun.corba.se.spi.orb.ORB orb;
/*     */   private POASystemException wrapper;
/*     */   private POAFactory factory;
/*     */ 
/*     */   public DelegateImpl(com.sun.corba.se.spi.orb.ORB paramORB, POAFactory paramPOAFactory)
/*     */   {
/*  44 */     this.orb = paramORB;
/*  45 */     this.wrapper = POASystemException.get(paramORB, "oa");
/*     */ 
/*  47 */     this.factory = paramPOAFactory;
/*     */   }
/*     */ 
/*     */   public org.omg.CORBA.ORB orb(Servant paramServant)
/*     */   {
/*  52 */     return this.orb;
/*     */   }
/*     */ 
/*     */   public org.omg.CORBA.Object this_object(Servant paramServant)
/*     */   {
/*     */     try
/*     */     {
/*  60 */       byte[] arrayOfByte = this.orb.peekInvocationInfo().id();
/*  61 */       POA localPOA = (POA)this.orb.peekInvocationInfo().oa();
/*  62 */       String str = paramServant._all_interfaces(localPOA, arrayOfByte)[0];
/*  63 */       return localPOA.create_reference_with_id(arrayOfByte, str);
/*     */     }
/*     */     catch (EmptyStackException localEmptyStackException) {
/*  66 */       POAImpl localPOAImpl = null;
/*     */       try {
/*  68 */         localPOAImpl = (POAImpl)paramServant._default_POA();
/*     */       } catch (ClassCastException localClassCastException2) {
/*  70 */         throw this.wrapper.defaultPoaNotPoaimpl(localClassCastException2);
/*     */       }
/*     */       try
/*     */       {
/*  74 */         if ((localPOAImpl.getPolicies().isImplicitlyActivated()) || ((localPOAImpl.getPolicies().isUniqueIds()) && (localPOAImpl.getPolicies().retainServants())))
/*     */         {
/*  77 */           return localPOAImpl.servant_to_reference(paramServant);
/*     */         }
/*  79 */         throw this.wrapper.wrongPoliciesForThisObject();
/*     */       }
/*     */       catch (ServantNotActive localServantNotActive) {
/*  82 */         throw this.wrapper.thisObjectServantNotActive(localServantNotActive);
/*     */       } catch (WrongPolicy localWrongPolicy) {
/*  84 */         throw this.wrapper.thisObjectWrongPolicy(localWrongPolicy);
/*     */       }
/*     */     } catch (ClassCastException localClassCastException1) {
/*  87 */       throw this.wrapper.defaultPoaNotPoaimpl(localClassCastException1);
/*     */     }
/*     */   }
/*     */ 
/*     */   public POA poa(Servant paramServant)
/*     */   {
/*     */     try {
/*  94 */       return (POA)this.orb.peekInvocationInfo().oa();
/*     */     } catch (EmptyStackException localEmptyStackException) {
/*  96 */       POA localPOA = this.factory.lookupPOA(paramServant);
/*  97 */       if (localPOA != null) {
/*  98 */         return localPOA;
/*     */       }
/*     */ 
/* 101 */       throw this.wrapper.noContext(localEmptyStackException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public byte[] object_id(Servant paramServant)
/*     */   {
/*     */     try {
/* 108 */       return this.orb.peekInvocationInfo().id();
/*     */     } catch (EmptyStackException localEmptyStackException) {
/* 110 */       throw this.wrapper.noContext(localEmptyStackException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public POA default_POA(Servant paramServant)
/*     */   {
/* 116 */     return this.factory.getRootPOA();
/*     */   }
/*     */ 
/*     */   public boolean is_a(Servant paramServant, String paramString)
/*     */   {
/* 121 */     String[] arrayOfString = paramServant._all_interfaces(poa(paramServant), object_id(paramServant));
/* 122 */     for (int i = 0; i < arrayOfString.length; i++) {
/* 123 */       if (paramString.equals(arrayOfString[i]))
/* 124 */         return true;
/*     */     }
/* 126 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean non_existent(Servant paramServant)
/*     */   {
/*     */     try
/*     */     {
/* 133 */       byte[] arrayOfByte = this.orb.peekInvocationInfo().id();
/* 134 */       if (arrayOfByte == null) return true;
/* 135 */       return false;
/*     */     } catch (EmptyStackException localEmptyStackException) {
/* 137 */       throw this.wrapper.noContext(localEmptyStackException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public org.omg.CORBA.Object get_interface_def(Servant paramServant)
/*     */   {
/* 145 */     throw this.wrapper.methodNotImplemented();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.oa.poa.DelegateImpl
 * JD-Core Version:    0.6.2
 */