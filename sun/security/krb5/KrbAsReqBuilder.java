/*     */ package sun.security.krb5;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.util.Arrays;
/*     */ import javax.security.auth.kerberos.KeyTab;
/*     */ import sun.security.jgss.krb5.Krb5Util;
/*     */ import sun.security.krb5.internal.HostAddresses;
/*     */ import sun.security.krb5.internal.KDCOptions;
/*     */ import sun.security.krb5.internal.KRBError;
/*     */ import sun.security.krb5.internal.KerberosTime;
/*     */ import sun.security.krb5.internal.Krb5;
/*     */ import sun.security.krb5.internal.PAData;
/*     */ import sun.security.krb5.internal.PAData.SaltAndParams;
/*     */ import sun.security.krb5.internal.crypto.EType;
/*     */ 
/*     */ public final class KrbAsReqBuilder
/*     */ {
/*     */   private KDCOptions options;
/*     */   private PrincipalName cname;
/*     */   private PrincipalName sname;
/*     */   private KerberosTime from;
/*     */   private KerberosTime till;
/*     */   private KerberosTime rtime;
/*     */   private HostAddresses addresses;
/*     */   private final char[] password;
/*     */   private final KeyTab ktab;
/*     */   private PAData[] paList;
/*     */   private KrbAsReq req;
/*     */   private KrbAsRep rep;
/*     */   private State state;
/*     */ 
/*     */   private void init(PrincipalName paramPrincipalName)
/*     */     throws KrbException
/*     */   {
/* 102 */     if (paramPrincipalName.getRealm() == null) {
/* 103 */       paramPrincipalName.setRealm(Config.getInstance().getDefaultRealm());
/*     */     }
/* 105 */     this.cname = paramPrincipalName;
/* 106 */     this.state = State.INIT;
/*     */   }
/*     */ 
/*     */   public KrbAsReqBuilder(PrincipalName paramPrincipalName, KeyTab paramKeyTab)
/*     */     throws KrbException
/*     */   {
/* 122 */     init(paramPrincipalName);
/* 123 */     this.ktab = paramKeyTab;
/* 124 */     this.password = null;
/*     */   }
/*     */ 
/*     */   public KrbAsReqBuilder(PrincipalName paramPrincipalName, char[] paramArrayOfChar)
/*     */     throws KrbException
/*     */   {
/* 140 */     init(paramPrincipalName);
/* 141 */     this.password = ((char[])paramArrayOfChar.clone());
/* 142 */     this.ktab = null;
/*     */   }
/*     */ 
/*     */   public EncryptionKey[] getKeys(boolean paramBoolean)
/*     */     throws KrbException
/*     */   {
/* 158 */     checkState(paramBoolean ? State.REQ_OK : State.INIT, "Cannot get keys");
/* 159 */     if (this.password != null) {
/* 160 */       int[] arrayOfInt = EType.getDefaults("default_tkt_enctypes");
/* 161 */       EncryptionKey[] arrayOfEncryptionKey = new EncryptionKey[arrayOfInt.length];
/*     */ 
/* 179 */       String str = null;
/*     */       try {
/* 181 */         for (int i = 0; i < arrayOfInt.length; i++)
/*     */         {
/* 183 */           localObject = PAData.getSaltAndParams(arrayOfInt[i], this.paList);
/*     */ 
/* 185 */           if (localObject != null)
/*     */           {
/* 188 */             if ((arrayOfInt[i] != 23) && (((PAData.SaltAndParams)localObject).salt != null))
/*     */             {
/* 190 */               str = ((PAData.SaltAndParams)localObject).salt;
/*     */             }
/* 192 */             arrayOfEncryptionKey[i] = EncryptionKey.acquireSecretKey(this.cname, this.password, arrayOfInt[i], (PAData.SaltAndParams)localObject);
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 199 */         if (str == null) str = this.cname.getSalt();
/* 200 */         for (i = 0; i < arrayOfInt.length; i++)
/*     */         {
/* 202 */           if (arrayOfEncryptionKey[i] == null) {
/* 203 */             arrayOfEncryptionKey[i] = EncryptionKey.acquireSecretKey(this.password, str, arrayOfInt[i], null);
/*     */           }
/*     */         }
/*     */ 
/*     */       }
/*     */       catch (IOException localIOException)
/*     */       {
/* 210 */         Object localObject = new KrbException(909);
/* 211 */         ((KrbException)localObject).initCause(localIOException);
/* 212 */         throw ((Throwable)localObject);
/*     */       }
/* 214 */       return arrayOfEncryptionKey;
/*     */     }
/* 216 */     throw new IllegalStateException("Required password not provided");
/*     */   }
/*     */ 
/*     */   public void setOptions(KDCOptions paramKDCOptions)
/*     */   {
/* 226 */     checkState(State.INIT, "Cannot specify options");
/* 227 */     this.options = paramKDCOptions;
/*     */   }
/*     */ 
/*     */   public void setTarget(PrincipalName paramPrincipalName)
/*     */   {
/* 236 */     checkState(State.INIT, "Cannot specify target");
/* 237 */     this.sname = paramPrincipalName;
/*     */   }
/*     */ 
/*     */   public void setAddresses(HostAddresses paramHostAddresses)
/*     */   {
/* 246 */     checkState(State.INIT, "Cannot specify addresses");
/* 247 */     this.addresses = paramHostAddresses;
/*     */   }
/*     */ 
/*     */   private KrbAsReq build(EncryptionKey paramEncryptionKey)
/*     */     throws KrbException, IOException
/*     */   {
/*     */     int[] arrayOfInt;
/* 260 */     if (this.password != null) {
/* 261 */       arrayOfInt = EType.getDefaults("default_tkt_enctypes");
/*     */     } else {
/* 263 */       EncryptionKey[] arrayOfEncryptionKey1 = Krb5Util.keysFromJavaxKeyTab(this.ktab, this.cname);
/* 264 */       arrayOfInt = EType.getDefaults("default_tkt_enctypes", arrayOfEncryptionKey1);
/*     */ 
/* 266 */       for (EncryptionKey localEncryptionKey : arrayOfEncryptionKey1) localEncryptionKey.destroy();
/*     */     }
/* 268 */     return new KrbAsReq(paramEncryptionKey, this.options, this.cname, this.sname, this.from, this.till, this.rtime, arrayOfInt, this.addresses);
/*     */   }
/*     */ 
/*     */   private KrbAsReqBuilder resolve()
/*     */     throws KrbException, Asn1Exception, IOException
/*     */   {
/* 287 */     if (this.ktab != null)
/* 288 */       this.rep.decryptUsingKeyTab(this.ktab, this.req, this.cname);
/*     */     else {
/* 290 */       this.rep.decryptUsingPassword(this.password, this.req, this.cname);
/*     */     }
/* 292 */     if (this.rep.getPA() != null) {
/* 293 */       if ((this.paList == null) || (this.paList.length == 0)) {
/* 294 */         this.paList = this.rep.getPA();
/*     */       } else {
/* 296 */         int i = this.rep.getPA().length;
/* 297 */         if (i > 0) {
/* 298 */           int j = this.paList.length;
/* 299 */           this.paList = ((PAData[])Arrays.copyOf(this.paList, this.paList.length + i));
/* 300 */           System.arraycopy(this.rep.getPA(), 0, this.paList, j, i);
/*     */         }
/*     */       }
/*     */     }
/* 304 */     return this;
/*     */   }
/*     */ 
/*     */   private KrbAsReqBuilder send()
/*     */     throws KrbException, IOException
/*     */   {
/* 313 */     int i = 0;
/* 314 */     KdcComm localKdcComm = new KdcComm(this.cname.getRealmAsString());
/* 315 */     EncryptionKey localEncryptionKey1 = null;
/*     */     while (true)
/*     */       try {
/* 318 */         this.req = build(localEncryptionKey1);
/* 319 */         this.rep = new KrbAsRep(localKdcComm.send(this.req.encoding()));
/* 320 */         return this;
/*     */       } catch (KrbException localKrbException) {
/* 322 */         if ((i == 0) && ((localKrbException.returnCode() == 24) || (localKrbException.returnCode() == 25)))
/*     */         {
/* 325 */           if (Krb5.DEBUG) {
/* 326 */             System.out.println("KrbAsReqBuilder: PREAUTH FAILED/REQ, re-send AS-REQ");
/*     */           }
/*     */ 
/* 329 */           i = 1;
/* 330 */           KRBError localKRBError = localKrbException.getError();
/* 331 */           int j = PAData.getPreferredEType(localKRBError.getPA(), EType.getDefaults("default_tkt_enctypes")[0]);
/*     */ 
/* 333 */           if (this.password == null) {
/* 334 */             EncryptionKey[] arrayOfEncryptionKey1 = Krb5Util.keysFromJavaxKeyTab(this.ktab, this.cname);
/* 335 */             localEncryptionKey1 = EncryptionKey.findKey(j, arrayOfEncryptionKey1);
/* 336 */             if (localEncryptionKey1 != null) localEncryptionKey1 = (EncryptionKey)localEncryptionKey1.clone();
/* 337 */             EncryptionKey[] arrayOfEncryptionKey2 = arrayOfEncryptionKey1; int k = arrayOfEncryptionKey2.length; int m = 0; if (m < k) { EncryptionKey localEncryptionKey2 = arrayOfEncryptionKey2[m]; localEncryptionKey2.destroy(); m++; continue; }
/*     */           } else {
/* 339 */             localEncryptionKey1 = EncryptionKey.acquireSecretKey(this.cname, this.password, j, PAData.getSaltAndParams(j, localKRBError.getPA()));
/*     */           }
/*     */ 
/* 345 */           this.paList = localKRBError.getPA();
/*     */         } else {
/* 347 */           throw localKrbException;
/*     */         }
/*     */       }
/*     */   }
/*     */ 
/*     */   public KrbAsReqBuilder action()
/*     */     throws KrbException, Asn1Exception, IOException
/*     */   {
/* 362 */     checkState(State.INIT, "Cannot call action");
/* 363 */     this.state = State.REQ_OK;
/* 364 */     return send().resolve();
/*     */   }
/*     */ 
/*     */   public Credentials getCreds()
/*     */   {
/* 371 */     checkState(State.REQ_OK, "Cannot retrieve creds");
/* 372 */     return this.rep.getCreds();
/*     */   }
/*     */ 
/*     */   public sun.security.krb5.internal.ccache.Credentials getCCreds()
/*     */   {
/* 379 */     checkState(State.REQ_OK, "Cannot retrieve CCreds");
/* 380 */     return this.rep.getCCreds();
/*     */   }
/*     */ 
/*     */   public void destroy()
/*     */   {
/* 387 */     this.state = State.DESTROYED;
/* 388 */     if (this.password != null)
/* 389 */       Arrays.fill(this.password, '\000');
/*     */   }
/*     */ 
/*     */   private void checkState(State paramState, String paramString)
/*     */   {
/* 400 */     if (this.state != paramState)
/* 401 */       throw new IllegalStateException(paramString + " at " + paramState + " state");
/*     */   }
/*     */ 
/*     */   private static enum State
/*     */   {
/*  93 */     INIT, 
/*  94 */     REQ_OK, 
/*  95 */     DESTROYED;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.KrbAsReqBuilder
 * JD-Core Version:    0.6.2
 */