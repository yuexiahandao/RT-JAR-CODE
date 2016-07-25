/*    */ package sun.security.jgss.spnego;
/*    */ 
/*    */ import java.security.Provider;
/*    */ import org.ietf.jgss.GSSException;
/*    */ import org.ietf.jgss.Oid;
/*    */ import sun.security.jgss.GSSUtil;
/*    */ import sun.security.jgss.spi.GSSCredentialSpi;
/*    */ import sun.security.jgss.spi.GSSNameSpi;
/*    */ 
/*    */ public class SpNegoCredElement
/*    */   implements GSSCredentialSpi
/*    */ {
/* 45 */   private GSSCredentialSpi cred = null;
/*    */ 
/*    */   SpNegoCredElement(GSSCredentialSpi paramGSSCredentialSpi) throws GSSException {
/* 48 */     this.cred = paramGSSCredentialSpi;
/*    */   }
/*    */ 
/*    */   Oid getInternalMech() {
/* 52 */     return this.cred.getMechanism();
/*    */   }
/*    */ 
/*    */   public GSSCredentialSpi getInternalCred()
/*    */   {
/* 57 */     return this.cred;
/*    */   }
/*    */ 
/*    */   public Provider getProvider() {
/* 61 */     return SpNegoMechFactory.PROVIDER;
/*    */   }
/*    */ 
/*    */   public void dispose() throws GSSException {
/* 65 */     this.cred.dispose();
/*    */   }
/*    */ 
/*    */   public GSSNameSpi getName() throws GSSException {
/* 69 */     return this.cred.getName();
/*    */   }
/*    */ 
/*    */   public int getInitLifetime() throws GSSException {
/* 73 */     return this.cred.getInitLifetime();
/*    */   }
/*    */ 
/*    */   public int getAcceptLifetime() throws GSSException {
/* 77 */     return this.cred.getAcceptLifetime();
/*    */   }
/*    */ 
/*    */   public boolean isInitiatorCredential() throws GSSException {
/* 81 */     return this.cred.isInitiatorCredential();
/*    */   }
/*    */ 
/*    */   public boolean isAcceptorCredential() throws GSSException {
/* 85 */     return this.cred.isAcceptorCredential();
/*    */   }
/*    */ 
/*    */   public Oid getMechanism() {
/* 89 */     return GSSUtil.GSS_SPNEGO_MECH_OID;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.jgss.spnego.SpNegoCredElement
 * JD-Core Version:    0.6.2
 */