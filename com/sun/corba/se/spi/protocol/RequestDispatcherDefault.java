/*     */ package com.sun.corba.se.spi.protocol;
/*     */ 
/*     */ import com.sun.corba.se.impl.protocol.BootstrapServerRequestDispatcher;
/*     */ import com.sun.corba.se.impl.protocol.CorbaClientRequestDispatcherImpl;
/*     */ import com.sun.corba.se.impl.protocol.CorbaServerRequestDispatcherImpl;
/*     */ import com.sun.corba.se.impl.protocol.FullServantCacheLocalCRDImpl;
/*     */ import com.sun.corba.se.impl.protocol.INSServerRequestDispatcher;
/*     */ import com.sun.corba.se.impl.protocol.InfoOnlyServantCacheLocalCRDImpl;
/*     */ import com.sun.corba.se.impl.protocol.JIDLLocalCRDImpl;
/*     */ import com.sun.corba.se.impl.protocol.MinimalServantCacheLocalCRDImpl;
/*     */ import com.sun.corba.se.impl.protocol.POALocalCRDImpl;
/*     */ import com.sun.corba.se.pept.protocol.ClientRequestDispatcher;
/*     */ import com.sun.corba.se.spi.ior.IOR;
/*     */ import com.sun.corba.se.spi.orb.ORB;
/*     */ 
/*     */ public final class RequestDispatcherDefault
/*     */ {
/*     */   public static ClientRequestDispatcher makeClientRequestDispatcher()
/*     */   {
/*  53 */     return new CorbaClientRequestDispatcherImpl();
/*     */   }
/*     */ 
/*     */   public static CorbaServerRequestDispatcher makeServerRequestDispatcher(ORB paramORB)
/*     */   {
/*  58 */     return new CorbaServerRequestDispatcherImpl(paramORB);
/*     */   }
/*     */ 
/*     */   public static CorbaServerRequestDispatcher makeBootstrapServerRequestDispatcher(ORB paramORB)
/*     */   {
/*  63 */     return new BootstrapServerRequestDispatcher(paramORB);
/*     */   }
/*     */ 
/*     */   public static CorbaServerRequestDispatcher makeINSServerRequestDispatcher(ORB paramORB)
/*     */   {
/*  68 */     return new INSServerRequestDispatcher(paramORB);
/*     */   }
/*     */ 
/*     */   public static LocalClientRequestDispatcherFactory makeMinimalServantCacheLocalClientRequestDispatcherFactory(ORB paramORB)
/*     */   {
/*  73 */     return new LocalClientRequestDispatcherFactory() {
/*     */       public LocalClientRequestDispatcher create(int paramAnonymousInt, IOR paramAnonymousIOR) {
/*  75 */         return new MinimalServantCacheLocalCRDImpl(this.val$orb, paramAnonymousInt, paramAnonymousIOR);
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   public static LocalClientRequestDispatcherFactory makeInfoOnlyServantCacheLocalClientRequestDispatcherFactory(ORB paramORB)
/*     */   {
/*  82 */     return new LocalClientRequestDispatcherFactory() {
/*     */       public LocalClientRequestDispatcher create(int paramAnonymousInt, IOR paramAnonymousIOR) {
/*  84 */         return new InfoOnlyServantCacheLocalCRDImpl(this.val$orb, paramAnonymousInt, paramAnonymousIOR);
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   public static LocalClientRequestDispatcherFactory makeFullServantCacheLocalClientRequestDispatcherFactory(ORB paramORB)
/*     */   {
/*  91 */     return new LocalClientRequestDispatcherFactory() {
/*     */       public LocalClientRequestDispatcher create(int paramAnonymousInt, IOR paramAnonymousIOR) {
/*  93 */         return new FullServantCacheLocalCRDImpl(this.val$orb, paramAnonymousInt, paramAnonymousIOR);
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   public static LocalClientRequestDispatcherFactory makeJIDLLocalClientRequestDispatcherFactory(ORB paramORB)
/*     */   {
/* 100 */     return new LocalClientRequestDispatcherFactory() {
/*     */       public LocalClientRequestDispatcher create(int paramAnonymousInt, IOR paramAnonymousIOR) {
/* 102 */         return new JIDLLocalCRDImpl(this.val$orb, paramAnonymousInt, paramAnonymousIOR);
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   public static LocalClientRequestDispatcherFactory makePOALocalClientRequestDispatcherFactory(ORB paramORB)
/*     */   {
/* 109 */     return new LocalClientRequestDispatcherFactory() {
/*     */       public LocalClientRequestDispatcher create(int paramAnonymousInt, IOR paramAnonymousIOR) {
/* 111 */         return new POALocalCRDImpl(this.val$orb, paramAnonymousInt, paramAnonymousIOR);
/*     */       }
/*     */     };
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.protocol.RequestDispatcherDefault
 * JD-Core Version:    0.6.2
 */