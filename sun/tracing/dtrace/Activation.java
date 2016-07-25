/*    */ package sun.tracing.dtrace;
/*    */ 
/*    */ import java.security.Permission;
/*    */ 
/*    */ class Activation
/*    */ {
/*    */   private SystemResource resource;
/*    */   private int referenceCount;
/*    */ 
/*    */   Activation(String paramString, DTraceProvider[] paramArrayOfDTraceProvider)
/*    */   {
/* 38 */     SecurityManager localSecurityManager = System.getSecurityManager();
/*    */     Object localObject1;
/* 39 */     if (localSecurityManager != null) {
/* 40 */       localObject1 = new RuntimePermission("com.sun.tracing.dtrace.createProvider");
/*    */ 
/* 42 */       localSecurityManager.checkPermission((Permission)localObject1);
/*    */     }
/* 44 */     this.referenceCount = paramArrayOfDTraceProvider.length;
/* 45 */     for (Object localObject2 : paramArrayOfDTraceProvider) {
/* 46 */       localObject2.setActivation(this);
/*    */     }
/* 48 */     this.resource = new SystemResource(this, JVM.activate(paramString, paramArrayOfDTraceProvider));
/*    */   }
/*    */ 
/*    */   void disposeProvider(DTraceProvider paramDTraceProvider)
/*    */   {
/* 53 */     if (--this.referenceCount == 0)
/* 54 */       this.resource.dispose();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.tracing.dtrace.Activation
 * JD-Core Version:    0.6.2
 */