/*     */ package sun.security.jgss;
/*     */ 
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.security.Provider;
/*     */ import org.ietf.jgss.GSSContext;
/*     */ import org.ietf.jgss.GSSCredential;
/*     */ import org.ietf.jgss.GSSException;
/*     */ import org.ietf.jgss.GSSManager;
/*     */ import org.ietf.jgss.GSSName;
/*     */ import org.ietf.jgss.Oid;
/*     */ import sun.security.jgss.spi.GSSContextSpi;
/*     */ import sun.security.jgss.spi.GSSCredentialSpi;
/*     */ import sun.security.jgss.spi.GSSNameSpi;
/*     */ import sun.security.jgss.spi.MechanismFactory;
/*     */ 
/*     */ public class GSSManagerImpl extends GSSManager
/*     */ {
/*     */   private static final String USE_NATIVE_PROP = "sun.security.jgss.native";
/*  46 */   private static final Boolean USE_NATIVE = (Boolean)AccessController.doPrivileged(new PrivilegedAction()
/*     */   {
/*     */     public Boolean run() {
/*  49 */       String str = System.getProperty("os.name");
/*  50 */       if ((str.startsWith("SunOS")) || (str.startsWith("Linux")))
/*     */       {
/*  52 */         return new Boolean(System.getProperty("sun.security.jgss.native"));
/*     */       }
/*     */ 
/*  55 */       return Boolean.FALSE;
/*     */     }
/*     */   });
/*     */   private ProviderList list;
/*     */ 
/*     */   public GSSManagerImpl(GSSCaller paramGSSCaller, boolean paramBoolean)
/*     */   {
/*  65 */     this.list = new ProviderList(paramGSSCaller, paramBoolean);
/*     */   }
/*     */ 
/*     */   public GSSManagerImpl(GSSCaller paramGSSCaller)
/*     */   {
/*  70 */     this.list = new ProviderList(paramGSSCaller, USE_NATIVE.booleanValue());
/*     */   }
/*     */ 
/*     */   public GSSManagerImpl() {
/*  74 */     this.list = new ProviderList(GSSCaller.CALLER_UNKNOWN, USE_NATIVE.booleanValue());
/*     */   }
/*     */ 
/*     */   public Oid[] getMechs() {
/*  78 */     return this.list.getMechs();
/*     */   }
/*     */ 
/*     */   public Oid[] getNamesForMech(Oid paramOid) throws GSSException
/*     */   {
/*  83 */     MechanismFactory localMechanismFactory = this.list.getMechFactory(paramOid);
/*  84 */     return (Oid[])localMechanismFactory.getNameTypes().clone();
/*     */   }
/*     */ 
/*     */   public Oid[] getMechsForName(Oid paramOid) {
/*  88 */     Oid[] arrayOfOid1 = this.list.getMechs();
/*  89 */     Object localObject = new Oid[arrayOfOid1.length];
/*  90 */     int i = 0;
/*     */ 
/*  93 */     if (paramOid.equals(GSSNameImpl.oldHostbasedServiceName)) {
/*  94 */       paramOid = GSSName.NT_HOSTBASED_SERVICE;
/*     */     }
/*     */ 
/*  98 */     for (int j = 0; j < arrayOfOid1.length; j++)
/*     */     {
/* 100 */       Oid localOid = arrayOfOid1[j];
/*     */       try {
/* 102 */         Oid[] arrayOfOid3 = getNamesForMech(localOid);
/*     */ 
/* 104 */         if (paramOid.containedIn(arrayOfOid3))
/* 105 */           localObject[(i++)] = localOid;
/*     */       }
/*     */       catch (GSSException localGSSException)
/*     */       {
/* 109 */         GSSUtil.debug("Skip " + localOid + ": error retrieving supported name types");
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 115 */     if (i < localObject.length) {
/* 116 */       Oid[] arrayOfOid2 = new Oid[i];
/* 117 */       for (int k = 0; k < i; k++)
/* 118 */         arrayOfOid2[k] = localObject[k];
/* 119 */       localObject = arrayOfOid2;
/*     */     }
/*     */ 
/* 122 */     return localObject;
/*     */   }
/*     */ 
/*     */   public GSSName createName(String paramString, Oid paramOid) throws GSSException
/*     */   {
/* 127 */     return new GSSNameImpl(this, paramString, paramOid);
/*     */   }
/*     */ 
/*     */   public GSSName createName(byte[] paramArrayOfByte, Oid paramOid) throws GSSException
/*     */   {
/* 132 */     return new GSSNameImpl(this, paramArrayOfByte, paramOid);
/*     */   }
/*     */ 
/*     */   public GSSName createName(String paramString, Oid paramOid1, Oid paramOid2) throws GSSException
/*     */   {
/* 137 */     return new GSSNameImpl(this, paramString, paramOid1, paramOid2);
/*     */   }
/*     */ 
/*     */   public GSSName createName(byte[] paramArrayOfByte, Oid paramOid1, Oid paramOid2) throws GSSException
/*     */   {
/* 142 */     return new GSSNameImpl(this, paramArrayOfByte, paramOid1, paramOid2);
/*     */   }
/*     */ 
/*     */   public GSSCredential createCredential(int paramInt) throws GSSException
/*     */   {
/* 147 */     return new GSSCredentialImpl(this, paramInt);
/*     */   }
/*     */ 
/*     */   public GSSCredential createCredential(GSSName paramGSSName, int paramInt1, Oid paramOid, int paramInt2)
/*     */     throws GSSException
/*     */   {
/* 153 */     return new GSSCredentialImpl(this, paramGSSName, paramInt1, paramOid, paramInt2);
/*     */   }
/*     */ 
/*     */   public GSSCredential createCredential(GSSName paramGSSName, int paramInt1, Oid[] paramArrayOfOid, int paramInt2)
/*     */     throws GSSException
/*     */   {
/* 159 */     return new GSSCredentialImpl(this, paramGSSName, paramInt1, paramArrayOfOid, paramInt2);
/*     */   }
/*     */ 
/*     */   public GSSContext createContext(GSSName paramGSSName, Oid paramOid, GSSCredential paramGSSCredential, int paramInt)
/*     */     throws GSSException
/*     */   {
/* 165 */     return new GSSContextImpl(this, paramGSSName, paramOid, paramGSSCredential, paramInt);
/*     */   }
/*     */ 
/*     */   public GSSContext createContext(GSSCredential paramGSSCredential) throws GSSException
/*     */   {
/* 170 */     return new GSSContextImpl(this, paramGSSCredential);
/*     */   }
/*     */ 
/*     */   public GSSContext createContext(byte[] paramArrayOfByte) throws GSSException
/*     */   {
/* 175 */     return new GSSContextImpl(this, paramArrayOfByte);
/*     */   }
/*     */ 
/*     */   public void addProviderAtFront(Provider paramProvider, Oid paramOid) throws GSSException
/*     */   {
/* 180 */     this.list.addProviderAtFront(paramProvider, paramOid);
/*     */   }
/*     */ 
/*     */   public void addProviderAtEnd(Provider paramProvider, Oid paramOid) throws GSSException
/*     */   {
/* 185 */     this.list.addProviderAtEnd(paramProvider, paramOid);
/*     */   }
/*     */ 
/*     */   public GSSCredentialSpi getCredentialElement(GSSNameSpi paramGSSNameSpi, int paramInt1, int paramInt2, Oid paramOid, int paramInt3)
/*     */     throws GSSException
/*     */   {
/* 191 */     MechanismFactory localMechanismFactory = this.list.getMechFactory(paramOid);
/* 192 */     return localMechanismFactory.getCredentialElement(paramGSSNameSpi, paramInt1, paramInt2, paramInt3);
/*     */   }
/*     */ 
/*     */   public GSSNameSpi getNameElement(String paramString, Oid paramOid1, Oid paramOid2)
/*     */     throws GSSException
/*     */   {
/* 201 */     MechanismFactory localMechanismFactory = this.list.getMechFactory(paramOid2);
/* 202 */     return localMechanismFactory.getNameElement(paramString, paramOid1);
/*     */   }
/*     */ 
/*     */   public GSSNameSpi getNameElement(byte[] paramArrayOfByte, Oid paramOid1, Oid paramOid2)
/*     */     throws GSSException
/*     */   {
/* 210 */     MechanismFactory localMechanismFactory = this.list.getMechFactory(paramOid2);
/* 211 */     return localMechanismFactory.getNameElement(paramArrayOfByte, paramOid1);
/*     */   }
/*     */ 
/*     */   GSSContextSpi getMechanismContext(GSSNameSpi paramGSSNameSpi, GSSCredentialSpi paramGSSCredentialSpi, int paramInt, Oid paramOid)
/*     */     throws GSSException
/*     */   {
/* 218 */     Provider localProvider = null;
/* 219 */     if (paramGSSCredentialSpi != null) {
/* 220 */       localProvider = paramGSSCredentialSpi.getProvider();
/*     */     }
/* 222 */     MechanismFactory localMechanismFactory = this.list.getMechFactory(paramOid, localProvider);
/* 223 */     return localMechanismFactory.getMechanismContext(paramGSSNameSpi, paramGSSCredentialSpi, paramInt);
/*     */   }
/*     */ 
/*     */   GSSContextSpi getMechanismContext(GSSCredentialSpi paramGSSCredentialSpi, Oid paramOid)
/*     */     throws GSSException
/*     */   {
/* 229 */     Provider localProvider = null;
/* 230 */     if (paramGSSCredentialSpi != null) {
/* 231 */       localProvider = paramGSSCredentialSpi.getProvider();
/*     */     }
/* 233 */     MechanismFactory localMechanismFactory = this.list.getMechFactory(paramOid, localProvider);
/* 234 */     return localMechanismFactory.getMechanismContext(paramGSSCredentialSpi);
/*     */   }
/*     */ 
/*     */   GSSContextSpi getMechanismContext(byte[] paramArrayOfByte) throws GSSException
/*     */   {
/* 239 */     if ((paramArrayOfByte == null) || (paramArrayOfByte.length == 0)) {
/* 240 */       throw new GSSException(12);
/*     */     }
/* 242 */     GSSContextSpi localGSSContextSpi = null;
/*     */ 
/* 246 */     Oid[] arrayOfOid = this.list.getMechs();
/* 247 */     for (int i = 0; i < arrayOfOid.length; i++) {
/* 248 */       MechanismFactory localMechanismFactory = this.list.getMechFactory(arrayOfOid[i]);
/* 249 */       if (localMechanismFactory.getProvider().getName().equals("SunNativeGSS")) {
/* 250 */         localGSSContextSpi = localMechanismFactory.getMechanismContext(paramArrayOfByte);
/* 251 */         if (localGSSContextSpi != null) break;
/*     */       }
/*     */     }
/* 254 */     if (localGSSContextSpi == null) {
/* 255 */       throw new GSSException(16);
/*     */     }
/* 257 */     return localGSSContextSpi;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.jgss.GSSManagerImpl
 * JD-Core Version:    0.6.2
 */