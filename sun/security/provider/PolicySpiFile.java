/*    */ package sun.security.provider;
/*    */ 
/*    */ import java.net.MalformedURLException;
/*    */ import java.net.URI;
/*    */ import java.security.CodeSource;
/*    */ import java.security.Permission;
/*    */ import java.security.PermissionCollection;
/*    */ import java.security.Policy.Parameters;
/*    */ import java.security.PolicySpi;
/*    */ import java.security.ProtectionDomain;
/*    */ import java.security.URIParameter;
/*    */ 
/*    */ public final class PolicySpiFile extends PolicySpi
/*    */ {
/*    */   private PolicyFile pf;
/*    */ 
/*    */   public PolicySpiFile(Policy.Parameters paramParameters)
/*    */   {
/* 50 */     if (paramParameters == null) {
/* 51 */       this.pf = new PolicyFile();
/*    */     } else {
/* 53 */       if (!(paramParameters instanceof URIParameter)) {
/* 54 */         throw new IllegalArgumentException("Unrecognized policy parameter: " + paramParameters);
/*    */       }
/*    */ 
/* 57 */       URIParameter localURIParameter = (URIParameter)paramParameters;
/*    */       try {
/* 59 */         this.pf = new PolicyFile(localURIParameter.getURI().toURL());
/*    */       } catch (MalformedURLException localMalformedURLException) {
/* 61 */         throw new IllegalArgumentException("Invalid URIParameter", localMalformedURLException);
/*    */       }
/*    */     }
/*    */   }
/*    */ 
/*    */   protected PermissionCollection engineGetPermissions(CodeSource paramCodeSource) {
/* 67 */     return this.pf.getPermissions(paramCodeSource);
/*    */   }
/*    */ 
/*    */   protected PermissionCollection engineGetPermissions(ProtectionDomain paramProtectionDomain) {
/* 71 */     return this.pf.getPermissions(paramProtectionDomain);
/*    */   }
/*    */ 
/*    */   protected boolean engineImplies(ProtectionDomain paramProtectionDomain, Permission paramPermission) {
/* 75 */     return this.pf.implies(paramProtectionDomain, paramPermission);
/*    */   }
/*    */ 
/*    */   protected void engineRefresh() {
/* 79 */     this.pf.refresh();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.provider.PolicySpiFile
 * JD-Core Version:    0.6.2
 */