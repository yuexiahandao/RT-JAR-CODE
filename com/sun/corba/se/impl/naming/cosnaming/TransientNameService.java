/*     */ package com.sun.corba.se.impl.naming.cosnaming;
/*     */ 
/*     */ import com.sun.corba.se.impl.logging.NamingSystemException;
/*     */ import com.sun.corba.se.spi.orb.ORB;
/*     */ import org.omg.CORBA.INITIALIZE;
/*     */ import org.omg.CORBA.Policy;
/*     */ import org.omg.CORBA.SystemException;
/*     */ import org.omg.PortableServer.IdAssignmentPolicyValue;
/*     */ import org.omg.PortableServer.LifespanPolicyValue;
/*     */ import org.omg.PortableServer.POA;
/*     */ import org.omg.PortableServer.POAManager;
/*     */ import org.omg.PortableServer.ServantRetentionPolicyValue;
/*     */ 
/*     */ public class TransientNameService
/*     */ {
/*     */   private org.omg.CORBA.Object theInitialNamingContext;
/*     */ 
/*     */   public TransientNameService(ORB paramORB)
/*     */     throws INITIALIZE
/*     */   {
/*  80 */     initialize(paramORB, "NameService");
/*     */   }
/*     */ 
/*     */   public TransientNameService(ORB paramORB, String paramString)
/*     */     throws INITIALIZE
/*     */   {
/*  97 */     initialize(paramORB, paramString);
/*     */   }
/*     */ 
/*     */   private void initialize(ORB paramORB, String paramString)
/*     */     throws INITIALIZE
/*     */   {
/* 109 */     NamingSystemException localNamingSystemException = NamingSystemException.get(paramORB, "naming");
/*     */     try
/*     */     {
/* 113 */       POA localPOA1 = (POA)paramORB.resolve_initial_references("RootPOA");
/*     */ 
/* 115 */       localPOA1.the_POAManager().activate();
/*     */ 
/* 117 */       int i = 0;
/* 118 */       Policy[] arrayOfPolicy = new Policy[3];
/* 119 */       arrayOfPolicy[(i++)] = localPOA1.create_lifespan_policy(LifespanPolicyValue.TRANSIENT);
/*     */ 
/* 121 */       arrayOfPolicy[(i++)] = localPOA1.create_id_assignment_policy(IdAssignmentPolicyValue.SYSTEM_ID);
/*     */ 
/* 123 */       arrayOfPolicy[(i++)] = localPOA1.create_servant_retention_policy(ServantRetentionPolicyValue.RETAIN);
/*     */ 
/* 126 */       POA localPOA2 = localPOA1.create_POA("TNameService", null, arrayOfPolicy);
/* 127 */       localPOA2.the_POAManager().activate();
/*     */ 
/* 130 */       TransientNamingContext localTransientNamingContext = new TransientNamingContext(paramORB, null, localPOA2);
/*     */ 
/* 132 */       byte[] arrayOfByte = localPOA2.activate_object(localTransientNamingContext);
/* 133 */       localTransientNamingContext.localRoot = localPOA2.id_to_reference(arrayOfByte);
/*     */ 
/* 135 */       this.theInitialNamingContext = localTransientNamingContext.localRoot;
/* 136 */       paramORB.register_initial_reference(paramString, this.theInitialNamingContext);
/*     */     }
/*     */     catch (SystemException localSystemException) {
/* 139 */       throw localNamingSystemException.transNsCannotCreateInitialNcSys(localSystemException);
/*     */     } catch (Exception localException) {
/* 141 */       throw localNamingSystemException.transNsCannotCreateInitialNc(localException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public org.omg.CORBA.Object initialNamingContext()
/*     */   {
/* 152 */     return this.theInitialNamingContext;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.naming.cosnaming.TransientNameService
 * JD-Core Version:    0.6.2
 */