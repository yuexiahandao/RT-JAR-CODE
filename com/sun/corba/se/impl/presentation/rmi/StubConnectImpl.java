/*     */ package com.sun.corba.se.impl.presentation.rmi;
/*     */ 
/*     */ import com.sun.corba.se.impl.corba.CORBAObjectImpl;
/*     */ import com.sun.corba.se.impl.ior.StubIORImpl;
/*     */ import com.sun.corba.se.impl.logging.UtilSystemException;
/*     */ import com.sun.corba.se.impl.util.Utility;
/*     */ import com.sun.corba.se.spi.presentation.rmi.StubAdapter;
/*     */ import java.rmi.RemoteException;
/*     */ import javax.rmi.CORBA.Tie;
/*     */ import org.omg.CORBA.BAD_INV_ORDER;
/*     */ import org.omg.CORBA.BAD_OPERATION;
/*     */ import org.omg.CORBA.ORB;
/*     */ import org.omg.CORBA.SystemException;
/*     */ import org.omg.CORBA.portable.Delegate;
/*     */ import org.omg.CORBA.portable.ObjectImpl;
/*     */ 
/*     */ public abstract class StubConnectImpl
/*     */ {
/*  54 */   static UtilSystemException wrapper = UtilSystemException.get("rmiiiop");
/*     */ 
/*     */   public static StubIORImpl connect(StubIORImpl paramStubIORImpl, org.omg.CORBA.Object paramObject, ObjectImpl paramObjectImpl, ORB paramORB)
/*     */     throws RemoteException
/*     */   {
/*  66 */     Delegate localDelegate = null;
/*     */     try
/*     */     {
/*     */       try {
/*  70 */         localDelegate = StubAdapter.getDelegate(paramObjectImpl);
/*     */ 
/*  72 */         if (localDelegate.orb(paramObjectImpl) != paramORB)
/*  73 */           throw wrapper.connectWrongOrb();
/*     */       } catch (BAD_OPERATION localBAD_OPERATION1) {
/*  75 */         if (paramStubIORImpl == null)
/*     */         {
/*  77 */           Tie localTie = Utility.getAndForgetTie(paramObject);
/*  78 */           if (localTie == null) {
/*  79 */             throw wrapper.connectNoTie();
/*     */           }
/*     */ 
/*  83 */           ORB localORB = paramORB;
/*     */           try {
/*  85 */             localORB = localTie.orb();
/*     */           }
/*     */           catch (BAD_OPERATION localBAD_OPERATION2) {
/*  88 */             localTie.orb(paramORB);
/*     */           }
/*     */           catch (BAD_INV_ORDER localBAD_INV_ORDER) {
/*  91 */             localTie.orb(paramORB);
/*     */           }
/*     */ 
/*  94 */           if (localORB != paramORB) {
/*  95 */             throw wrapper.connectTieWrongOrb();
/*     */           }
/*     */ 
/*  98 */           localDelegate = StubAdapter.getDelegate(localTie);
/*  99 */           CORBAObjectImpl localCORBAObjectImpl = new CORBAObjectImpl();
/* 100 */           localCORBAObjectImpl._set_delegate(localDelegate);
/* 101 */           paramStubIORImpl = new StubIORImpl(localCORBAObjectImpl);
/*     */         }
/*     */         else
/*     */         {
/* 105 */           localDelegate = paramStubIORImpl.getDelegate(paramORB);
/*     */         }
/*     */ 
/* 108 */         StubAdapter.setDelegate(paramObjectImpl, localDelegate);
/*     */       }
/*     */     } catch (SystemException localSystemException) {
/* 111 */       throw new RemoteException("CORBA SystemException", localSystemException);
/*     */     }
/*     */ 
/* 114 */     return paramStubIORImpl;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.presentation.rmi.StubConnectImpl
 * JD-Core Version:    0.6.2
 */