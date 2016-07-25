/*     */ package sun.security.jgss.spnego;
/*     */ 
/*     */ import java.security.Provider;
/*     */ import java.util.Vector;
/*     */ import org.ietf.jgss.GSSException;
/*     */ import org.ietf.jgss.GSSName;
/*     */ import org.ietf.jgss.Oid;
/*     */ import sun.security.jgss.GSSCaller;
/*     */ import sun.security.jgss.GSSManagerImpl;
/*     */ import sun.security.jgss.GSSUtil;
/*     */ import sun.security.jgss.ProviderList;
/*     */ import sun.security.jgss.SunProvider;
/*     */ import sun.security.jgss.krb5.Krb5AcceptCredential;
/*     */ import sun.security.jgss.krb5.Krb5InitCredential;
/*     */ import sun.security.jgss.krb5.Krb5MechFactory;
/*     */ import sun.security.jgss.krb5.Krb5NameElement;
/*     */ import sun.security.jgss.spi.GSSContextSpi;
/*     */ import sun.security.jgss.spi.GSSCredentialSpi;
/*     */ import sun.security.jgss.spi.GSSNameSpi;
/*     */ import sun.security.jgss.spi.MechanismFactory;
/*     */ 
/*     */ public final class SpNegoMechFactory
/*     */   implements MechanismFactory
/*     */ {
/*  49 */   static final Provider PROVIDER = new SunProvider();
/*     */ 
/*  52 */   static final Oid GSS_SPNEGO_MECH_OID = GSSUtil.createOid("1.3.6.1.5.5.2");
/*     */ 
/*  55 */   private static Oid[] nameTypes = { GSSName.NT_USER_NAME, GSSName.NT_HOSTBASED_SERVICE, GSSName.NT_EXPORT_NAME };
/*     */ 
/*  61 */   private static final Oid DEFAULT_SPNEGO_MECH_OID = ProviderList.DEFAULT_MECH_OID.equals(GSS_SPNEGO_MECH_OID) ? GSSUtil.GSS_KRB5_MECH_OID : ProviderList.DEFAULT_MECH_OID;
/*     */   final GSSManagerImpl manager;
/*     */   final Oid[] availableMechs;
/*     */ 
/*     */   private static SpNegoCredElement getCredFromSubject(GSSNameSpi paramGSSNameSpi, boolean paramBoolean)
/*     */     throws GSSException
/*     */   {
/*  74 */     Vector localVector = GSSUtil.searchSubject(paramGSSNameSpi, GSS_SPNEGO_MECH_OID, paramBoolean, SpNegoCredElement.class);
/*     */ 
/*  78 */     SpNegoCredElement localSpNegoCredElement = (localVector == null) || (localVector.isEmpty()) ? null : (SpNegoCredElement)localVector.firstElement();
/*     */ 
/*  82 */     if (localSpNegoCredElement != null) {
/*  83 */       GSSCredentialSpi localGSSCredentialSpi = localSpNegoCredElement.getInternalCred();
/*  84 */       if (GSSUtil.isKerberosMech(localGSSCredentialSpi.getMechanism()))
/*     */       {
/*     */         Object localObject;
/*  85 */         if (paramBoolean) {
/*  86 */           localObject = (Krb5InitCredential)localGSSCredentialSpi;
/*  87 */           Krb5MechFactory.checkInitCredPermission((Krb5NameElement)((Krb5InitCredential)localObject).getName());
/*     */         }
/*     */         else {
/*  90 */           localObject = (Krb5AcceptCredential)localGSSCredentialSpi;
/*  91 */           Krb5MechFactory.checkAcceptCredPermission((Krb5NameElement)((Krb5AcceptCredential)localObject).getName(), paramGSSNameSpi);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/*  96 */     return localSpNegoCredElement;
/*     */   }
/*     */ 
/*     */   public SpNegoMechFactory(GSSCaller paramGSSCaller) {
/* 100 */     this.manager = new GSSManagerImpl(paramGSSCaller, false);
/* 101 */     Oid[] arrayOfOid = this.manager.getMechs();
/* 102 */     this.availableMechs = new Oid[arrayOfOid.length - 1];
/* 103 */     int i = 0; for (int j = 0; i < arrayOfOid.length; i++)
/*     */     {
/* 105 */       if (!arrayOfOid[i].equals(GSS_SPNEGO_MECH_OID)) {
/* 106 */         this.availableMechs[(j++)] = arrayOfOid[i];
/*     */       }
/*     */     }
/*     */ 
/* 110 */     for (i = 0; i < this.availableMechs.length; i++)
/* 111 */       if (this.availableMechs[i].equals(DEFAULT_SPNEGO_MECH_OID)) {
/* 112 */         if (i == 0) break;
/* 113 */         this.availableMechs[i] = this.availableMechs[0];
/* 114 */         this.availableMechs[0] = DEFAULT_SPNEGO_MECH_OID; break;
/*     */       }
/*     */   }
/*     */ 
/*     */   public GSSNameSpi getNameElement(String paramString, Oid paramOid)
/*     */     throws GSSException
/*     */   {
/* 123 */     return this.manager.getNameElement(paramString, paramOid, DEFAULT_SPNEGO_MECH_OID);
/*     */   }
/*     */ 
/*     */   public GSSNameSpi getNameElement(byte[] paramArrayOfByte, Oid paramOid)
/*     */     throws GSSException
/*     */   {
/* 129 */     return this.manager.getNameElement(paramArrayOfByte, paramOid, DEFAULT_SPNEGO_MECH_OID);
/*     */   }
/*     */ 
/*     */   public GSSCredentialSpi getCredentialElement(GSSNameSpi paramGSSNameSpi, int paramInt1, int paramInt2, int paramInt3)
/*     */     throws GSSException
/*     */   {
/* 136 */     SpNegoCredElement localSpNegoCredElement = getCredFromSubject(paramGSSNameSpi, paramInt3 != 2);
/*     */ 
/* 139 */     if (localSpNegoCredElement == null)
/*     */     {
/* 141 */       localSpNegoCredElement = new SpNegoCredElement(this.manager.getCredentialElement(paramGSSNameSpi, paramInt1, paramInt2, null, paramInt3));
/*     */     }
/*     */ 
/* 145 */     return localSpNegoCredElement;
/*     */   }
/*     */ 
/*     */   public GSSContextSpi getMechanismContext(GSSNameSpi paramGSSNameSpi, GSSCredentialSpi paramGSSCredentialSpi, int paramInt)
/*     */     throws GSSException
/*     */   {
/* 152 */     if (paramGSSCredentialSpi == null) {
/* 153 */       paramGSSCredentialSpi = getCredFromSubject(null, true);
/* 154 */     } else if (!(paramGSSCredentialSpi instanceof SpNegoCredElement))
/*     */     {
/* 156 */       SpNegoCredElement localSpNegoCredElement = new SpNegoCredElement(paramGSSCredentialSpi);
/* 157 */       return new SpNegoContext(this, paramGSSNameSpi, localSpNegoCredElement, paramInt);
/*     */     }
/* 159 */     return new SpNegoContext(this, paramGSSNameSpi, paramGSSCredentialSpi, paramInt);
/*     */   }
/*     */ 
/*     */   public GSSContextSpi getMechanismContext(GSSCredentialSpi paramGSSCredentialSpi)
/*     */     throws GSSException
/*     */   {
/* 165 */     if (paramGSSCredentialSpi == null) {
/* 166 */       paramGSSCredentialSpi = getCredFromSubject(null, false);
/* 167 */     } else if (!(paramGSSCredentialSpi instanceof SpNegoCredElement))
/*     */     {
/* 169 */       SpNegoCredElement localSpNegoCredElement = new SpNegoCredElement(paramGSSCredentialSpi);
/* 170 */       return new SpNegoContext(this, localSpNegoCredElement);
/*     */     }
/* 172 */     return new SpNegoContext(this, paramGSSCredentialSpi);
/*     */   }
/*     */ 
/*     */   public GSSContextSpi getMechanismContext(byte[] paramArrayOfByte)
/*     */     throws GSSException
/*     */   {
/* 178 */     return new SpNegoContext(this, paramArrayOfByte);
/*     */   }
/*     */ 
/*     */   public final Oid getMechanismOid() {
/* 182 */     return GSS_SPNEGO_MECH_OID;
/*     */   }
/*     */ 
/*     */   public Provider getProvider() {
/* 186 */     return PROVIDER;
/*     */   }
/*     */ 
/*     */   public Oid[] getNameTypes()
/*     */   {
/* 191 */     return nameTypes;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.jgss.spnego.SpNegoMechFactory
 * JD-Core Version:    0.6.2
 */