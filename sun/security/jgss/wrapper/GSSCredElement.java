/*     */ package sun.security.jgss.wrapper;
/*     */ 
/*     */ import java.security.Provider;
/*     */ import org.ietf.jgss.GSSException;
/*     */ import org.ietf.jgss.Oid;
/*     */ import sun.security.jgss.GSSUtil;
/*     */ import sun.security.jgss.spi.GSSCredentialSpi;
/*     */ 
/*     */ public class GSSCredElement
/*     */   implements GSSCredentialSpi
/*     */ {
/*     */   private int usage;
/*     */   long pCred;
/*  42 */   private GSSNameElement name = null;
/*     */   private GSSLibStub cStub;
/*     */ 
/*     */   void doServicePermCheck()
/*     */     throws GSSException
/*     */   {
/*  47 */     if ((GSSUtil.isKerberosMech(this.cStub.getMech())) && 
/*  48 */       (System.getSecurityManager() != null))
/*     */     {
/*     */       String str;
/*  49 */       if (isInitiatorCredential()) {
/*  50 */         str = Krb5Util.getTGSName(this.name);
/*  51 */         Krb5Util.checkServicePermission(str, "initiate");
/*     */       }
/*  53 */       if ((isAcceptorCredential()) && (this.name != GSSNameElement.DEF_ACCEPTOR))
/*     */       {
/*  55 */         str = this.name.getKrbName();
/*  56 */         Krb5Util.checkServicePermission(str, "accept");
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   GSSCredElement(long paramLong, GSSNameElement paramGSSNameElement, Oid paramOid)
/*     */     throws GSSException
/*     */   {
/*  65 */     this.pCred = paramLong;
/*  66 */     this.cStub = GSSLibStub.getInstance(paramOid);
/*  67 */     this.usage = 1;
/*  68 */     this.name = paramGSSNameElement;
/*     */   }
/*     */ 
/*     */   GSSCredElement(GSSNameElement paramGSSNameElement, int paramInt1, int paramInt2, GSSLibStub paramGSSLibStub) throws GSSException
/*     */   {
/*  73 */     this.cStub = paramGSSLibStub;
/*  74 */     this.usage = paramInt2;
/*     */ 
/*  76 */     if (paramGSSNameElement != null) {
/*  77 */       this.name = paramGSSNameElement;
/*  78 */       doServicePermCheck();
/*  79 */       this.pCred = this.cStub.acquireCred(this.name.pName, paramInt1, paramInt2);
/*     */     } else {
/*  81 */       this.pCred = this.cStub.acquireCred(0L, paramInt1, paramInt2);
/*  82 */       this.name = new GSSNameElement(this.cStub.getCredName(this.pCred), this.cStub);
/*  83 */       doServicePermCheck();
/*     */     }
/*     */   }
/*     */ 
/*     */   public Provider getProvider() {
/*  88 */     return SunNativeProvider.INSTANCE;
/*     */   }
/*     */ 
/*     */   public void dispose() throws GSSException {
/*  92 */     this.name = null;
/*  93 */     if (this.pCred != 0L)
/*  94 */       this.pCred = this.cStub.releaseCred(this.pCred);
/*     */   }
/*     */ 
/*     */   public GSSNameElement getName() throws GSSException
/*     */   {
/*  99 */     return this.name == GSSNameElement.DEF_ACCEPTOR ? null : this.name;
/*     */   }
/*     */ 
/*     */   public int getInitLifetime() throws GSSException
/*     */   {
/* 104 */     if (isInitiatorCredential())
/* 105 */       return this.cStub.getCredTime(this.pCred);
/* 106 */     return 0;
/*     */   }
/*     */ 
/*     */   public int getAcceptLifetime() throws GSSException {
/* 110 */     if (isAcceptorCredential())
/* 111 */       return this.cStub.getCredTime(this.pCred);
/* 112 */     return 0;
/*     */   }
/*     */ 
/*     */   public boolean isInitiatorCredential() {
/* 116 */     return this.usage != 2;
/*     */   }
/*     */ 
/*     */   public boolean isAcceptorCredential() {
/* 120 */     return this.usage != 1;
/*     */   }
/*     */ 
/*     */   public Oid getMechanism() {
/* 124 */     return this.cStub.getMech();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 129 */     return "N/A";
/*     */   }
/*     */ 
/*     */   protected void finalize() throws Throwable {
/* 133 */     dispose();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.jgss.wrapper.GSSCredElement
 * JD-Core Version:    0.6.2
 */