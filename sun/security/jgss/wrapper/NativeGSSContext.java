/*     */ package sun.security.jgss.wrapper;
/*     */ 
/*     */ import com.sun.security.jgss.InquireType;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.security.Provider;
/*     */ import javax.security.auth.kerberos.DelegationPermission;
/*     */ import org.ietf.jgss.ChannelBinding;
/*     */ import org.ietf.jgss.GSSException;
/*     */ import org.ietf.jgss.MessageProp;
/*     */ import org.ietf.jgss.Oid;
/*     */ import sun.security.jgss.GSSExceptionImpl;
/*     */ import sun.security.jgss.GSSHeader;
/*     */ import sun.security.jgss.GSSUtil;
/*     */ import sun.security.jgss.spi.GSSContextSpi;
/*     */ import sun.security.jgss.spi.GSSCredentialSpi;
/*     */ import sun.security.jgss.spi.GSSNameSpi;
/*     */ import sun.security.jgss.spnego.NegTokenInit;
/*     */ import sun.security.jgss.spnego.NegTokenTarg;
/*     */ import sun.security.util.DerValue;
/*     */ import sun.security.util.ObjectIdentifier;
/*     */ 
/*     */ class NativeGSSContext
/*     */   implements GSSContextSpi
/*     */ {
/*     */   private static final int GSS_C_DELEG_FLAG = 1;
/*     */   private static final int GSS_C_MUTUAL_FLAG = 2;
/*     */   private static final int GSS_C_REPLAY_FLAG = 4;
/*     */   private static final int GSS_C_SEQUENCE_FLAG = 8;
/*     */   private static final int GSS_C_CONF_FLAG = 16;
/*     */   private static final int GSS_C_INTEG_FLAG = 32;
/*     */   private static final int GSS_C_ANON_FLAG = 64;
/*     */   private static final int GSS_C_PROT_READY_FLAG = 128;
/*     */   private static final int GSS_C_TRANS_FLAG = 256;
/*     */   private static final int NUM_OF_INQUIRE_VALUES = 6;
/*  63 */   private long pContext = 0L;
/*     */   private GSSNameElement srcName;
/*     */   private GSSNameElement targetName;
/*     */   private GSSCredElement cred;
/*     */   private boolean isInitiator;
/*     */   private boolean isEstablished;
/*     */   private Oid actualMech;
/*     */   private ChannelBinding cb;
/*     */   private GSSCredElement delegatedCred;
/*     */   private int flags;
/*  74 */   private int lifetime = 0;
/*     */   private final GSSLibStub cStub;
/*     */   private boolean skipDelegPermCheck;
/*     */   private boolean skipServicePermCheck;
/*     */ 
/*     */   private static Oid getMechFromSpNegoToken(byte[] paramArrayOfByte, boolean paramBoolean)
/*     */     throws GSSException
/*     */   {
/*  85 */     Oid localOid = null;
/*     */     Object localObject;
/*  86 */     if (paramBoolean) {
/*  87 */       localObject = null;
/*     */       try {
/*  89 */         localObject = new GSSHeader(new ByteArrayInputStream(paramArrayOfByte));
/*     */       } catch (IOException localIOException) {
/*  91 */         throw new GSSExceptionImpl(11, localIOException);
/*     */       }
/*  93 */       int i = ((GSSHeader)localObject).getMechTokenLength();
/*  94 */       byte[] arrayOfByte = new byte[i];
/*  95 */       System.arraycopy(paramArrayOfByte, paramArrayOfByte.length - i, arrayOfByte, 0, arrayOfByte.length);
/*     */ 
/*  98 */       NegTokenInit localNegTokenInit = new NegTokenInit(arrayOfByte);
/*  99 */       if (localNegTokenInit.getMechToken() != null) {
/* 100 */         Oid[] arrayOfOid = localNegTokenInit.getMechTypeList();
/* 101 */         localOid = arrayOfOid[0];
/*     */       }
/*     */     } else {
/* 104 */       localObject = new NegTokenTarg(paramArrayOfByte);
/* 105 */       localOid = ((NegTokenTarg)localObject).getSupportedMech();
/*     */     }
/* 107 */     return localOid;
/*     */   }
/*     */ 
/*     */   private void doServicePermCheck() throws GSSException
/*     */   {
/* 112 */     if (System.getSecurityManager() != null) {
/* 113 */       String str = this.isInitiator ? "initiate" : "accept";
/*     */ 
/* 116 */       if ((GSSUtil.isSpNegoMech(this.cStub.getMech())) && (this.isInitiator) && (!this.isEstablished))
/*     */       {
/* 118 */         if (this.srcName == null)
/*     */         {
/* 120 */           localObject = new GSSCredElement(null, this.lifetime, 1, GSSLibStub.getInstance(GSSUtil.GSS_KRB5_MECH_OID));
/*     */ 
/* 124 */           ((GSSCredElement)localObject).dispose();
/*     */         } else {
/* 126 */           localObject = Krb5Util.getTGSName(this.srcName);
/* 127 */           Krb5Util.checkServicePermission((String)localObject, str);
/*     */         }
/*     */       }
/* 130 */       Object localObject = this.targetName.getKrbName();
/* 131 */       Krb5Util.checkServicePermission((String)localObject, str);
/* 132 */       this.skipServicePermCheck = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void doDelegPermCheck() throws GSSException
/*     */   {
/* 138 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 139 */     if (localSecurityManager != null) {
/* 140 */       String str1 = this.targetName.getKrbName();
/* 141 */       String str2 = Krb5Util.getTGSName(this.targetName);
/* 142 */       StringBuffer localStringBuffer = new StringBuffer("\"");
/* 143 */       localStringBuffer.append(str1).append("\" \"");
/* 144 */       localStringBuffer.append(str2).append('"');
/* 145 */       String str3 = localStringBuffer.toString();
/* 146 */       SunNativeProvider.debug("Checking DelegationPermission (" + str3 + ")");
/*     */ 
/* 148 */       DelegationPermission localDelegationPermission = new DelegationPermission(str3);
/*     */ 
/* 150 */       localSecurityManager.checkPermission(localDelegationPermission);
/* 151 */       this.skipDelegPermCheck = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   private byte[] retrieveToken(InputStream paramInputStream, int paramInt) throws GSSException
/*     */   {
/*     */     try {
/* 158 */       byte[] arrayOfByte1 = null;
/*     */       Object localObject;
/* 159 */       if (paramInt != -1)
/*     */       {
/* 161 */         SunNativeProvider.debug("Precomputed mechToken length: " + paramInt);
/*     */ 
/* 163 */         localObject = new GSSHeader(new ObjectIdentifier(this.cStub.getMech().toString()), paramInt);
/*     */ 
/* 166 */         ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream(600);
/*     */ 
/* 168 */         byte[] arrayOfByte2 = new byte[paramInt];
/* 169 */         int i = paramInputStream.read(arrayOfByte2);
/* 170 */         assert (paramInt == i);
/* 171 */         ((GSSHeader)localObject).encode(localByteArrayOutputStream);
/* 172 */         localByteArrayOutputStream.write(arrayOfByte2);
/* 173 */         arrayOfByte1 = localByteArrayOutputStream.toByteArray();
/*     */       }
/*     */       else {
/* 176 */         assert (paramInt == -1);
/* 177 */         localObject = new DerValue(paramInputStream);
/* 178 */         arrayOfByte1 = ((DerValue)localObject).toByteArray();
/*     */       }
/* 180 */       SunNativeProvider.debug("Complete Token length: " + arrayOfByte1.length);
/*     */ 
/* 182 */       return arrayOfByte1;
/*     */     } catch (IOException localIOException) {
/* 184 */       throw new GSSExceptionImpl(11, localIOException);
/*     */     }
/*     */   }
/*     */ 
/*     */   NativeGSSContext(GSSNameElement paramGSSNameElement, GSSCredElement paramGSSCredElement, int paramInt, GSSLibStub paramGSSLibStub)
/*     */     throws GSSException
/*     */   {
/* 191 */     if (paramGSSNameElement == null) {
/* 192 */       throw new GSSException(11, 1, "null peer");
/*     */     }
/* 194 */     this.cStub = paramGSSLibStub;
/* 195 */     this.cred = paramGSSCredElement;
/* 196 */     this.targetName = paramGSSNameElement;
/* 197 */     this.isInitiator = true;
/* 198 */     this.lifetime = paramInt;
/*     */ 
/* 200 */     if (GSSUtil.isKerberosMech(this.cStub.getMech())) {
/* 201 */       doServicePermCheck();
/* 202 */       if (this.cred == null) {
/* 203 */         this.cred = new GSSCredElement(null, this.lifetime, 1, this.cStub);
/*     */       }
/*     */ 
/* 206 */       this.srcName = this.cred.getName();
/*     */     }
/*     */   }
/*     */ 
/*     */   NativeGSSContext(GSSCredElement paramGSSCredElement, GSSLibStub paramGSSLibStub)
/*     */     throws GSSException
/*     */   {
/* 213 */     this.cStub = paramGSSLibStub;
/* 214 */     this.cred = paramGSSCredElement;
/*     */ 
/* 216 */     if (this.cred != null) this.targetName = this.cred.getName();
/*     */ 
/* 218 */     this.isInitiator = false;
/*     */ 
/* 221 */     if ((GSSUtil.isKerberosMech(this.cStub.getMech())) && (this.targetName != null))
/* 222 */       doServicePermCheck();
/*     */   }
/*     */ 
/*     */   NativeGSSContext(long paramLong, GSSLibStub paramGSSLibStub)
/*     */     throws GSSException
/*     */   {
/* 231 */     assert (this.pContext != 0L);
/* 232 */     this.pContext = paramLong;
/* 233 */     this.cStub = paramGSSLibStub;
/*     */ 
/* 236 */     long[] arrayOfLong = this.cStub.inquireContext(this.pContext);
/* 237 */     if (arrayOfLong.length != 6) {
/* 238 */       throw new RuntimeException("Bug w/ GSSLibStub.inquireContext()");
/*     */     }
/* 240 */     this.srcName = new GSSNameElement(arrayOfLong[0], this.cStub);
/* 241 */     this.targetName = new GSSNameElement(arrayOfLong[1], this.cStub);
/* 242 */     this.isInitiator = (arrayOfLong[2] != 0L);
/* 243 */     this.isEstablished = (arrayOfLong[3] != 0L);
/* 244 */     this.flags = ((int)arrayOfLong[4]);
/* 245 */     this.lifetime = ((int)arrayOfLong[5]);
/*     */ 
/* 249 */     Oid localOid = this.cStub.getMech();
/* 250 */     if ((GSSUtil.isSpNegoMech(localOid)) || (GSSUtil.isKerberosMech(localOid)))
/* 251 */       doServicePermCheck();
/*     */   }
/*     */ 
/*     */   public Provider getProvider()
/*     */   {
/* 256 */     return SunNativeProvider.INSTANCE;
/*     */   }
/*     */ 
/*     */   public byte[] initSecContext(InputStream paramInputStream, int paramInt) throws GSSException
/*     */   {
/* 261 */     byte[] arrayOfByte1 = null;
/* 262 */     if ((!this.isEstablished) && (this.isInitiator)) {
/* 263 */       byte[] arrayOfByte2 = null;
/*     */ 
/* 265 */       if (this.pContext != 0L) {
/* 266 */         arrayOfByte2 = retrieveToken(paramInputStream, paramInt);
/* 267 */         SunNativeProvider.debug("initSecContext=> inToken len=" + arrayOfByte2.length);
/*     */       }
/*     */ 
/* 271 */       if (!getCredDelegState()) this.skipDelegPermCheck = true;
/*     */ 
/* 273 */       if ((GSSUtil.isKerberosMech(this.cStub.getMech())) && (!this.skipDelegPermCheck)) {
/* 274 */         doDelegPermCheck();
/*     */       }
/*     */ 
/* 277 */       long l = this.cred == null ? 0L : this.cred.pCred;
/* 278 */       arrayOfByte1 = this.cStub.initContext(l, this.targetName.pName, this.cb, arrayOfByte2, this);
/*     */ 
/* 280 */       SunNativeProvider.debug("initSecContext=> outToken len=" + (arrayOfByte1 == null ? 0 : arrayOfByte1.length));
/*     */ 
/* 285 */       if ((GSSUtil.isSpNegoMech(this.cStub.getMech())) && (arrayOfByte1 != null))
/*     */       {
/* 287 */         this.actualMech = getMechFromSpNegoToken(arrayOfByte1, true);
/*     */ 
/* 289 */         if (GSSUtil.isKerberosMech(this.actualMech)) {
/* 290 */           if (!this.skipServicePermCheck) doServicePermCheck();
/* 291 */           if (!this.skipDelegPermCheck) doDelegPermCheck();
/*     */         }
/*     */       }
/*     */ 
/* 295 */       if (this.isEstablished) {
/* 296 */         if (this.srcName == null) {
/* 297 */           this.srcName = new GSSNameElement(this.cStub.getContextName(this.pContext, true), this.cStub);
/*     */         }
/*     */ 
/* 300 */         if (this.cred == null) {
/* 301 */           this.cred = new GSSCredElement(this.srcName, this.lifetime, 1, this.cStub);
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 307 */     return arrayOfByte1;
/*     */   }
/*     */ 
/*     */   public byte[] acceptSecContext(InputStream paramInputStream, int paramInt) throws GSSException
/*     */   {
/* 312 */     byte[] arrayOfByte1 = null;
/* 313 */     if ((!this.isEstablished) && (!this.isInitiator)) {
/* 314 */       byte[] arrayOfByte2 = retrieveToken(paramInputStream, paramInt);
/* 315 */       SunNativeProvider.debug("acceptSecContext=> inToken len=" + arrayOfByte2.length);
/*     */ 
/* 317 */       long l = this.cred == null ? 0L : this.cred.pCred;
/* 318 */       arrayOfByte1 = this.cStub.acceptContext(l, this.cb, arrayOfByte2, this);
/* 319 */       SunNativeProvider.debug("acceptSecContext=> outToken len=" + (arrayOfByte1 == null ? 0 : arrayOfByte1.length));
/*     */ 
/* 322 */       if (this.targetName == null) {
/* 323 */         this.targetName = new GSSNameElement(this.cStub.getContextName(this.pContext, false), this.cStub);
/*     */ 
/* 327 */         if (this.cred != null) this.cred.dispose();
/* 328 */         this.cred = new GSSCredElement(this.targetName, this.lifetime, 2, this.cStub);
/*     */       }
/*     */ 
/* 334 */       if ((GSSUtil.isSpNegoMech(this.cStub.getMech())) && (arrayOfByte1 != null) && (!this.skipServicePermCheck))
/*     */       {
/* 336 */         if (GSSUtil.isKerberosMech(getMechFromSpNegoToken(arrayOfByte1, false)))
/*     */         {
/* 338 */           doServicePermCheck();
/*     */         }
/*     */       }
/*     */     }
/* 342 */     return arrayOfByte1;
/*     */   }
/*     */ 
/*     */   public boolean isEstablished() {
/* 346 */     return this.isEstablished;
/*     */   }
/*     */ 
/*     */   public void dispose() throws GSSException {
/* 350 */     this.srcName = null;
/* 351 */     this.targetName = null;
/* 352 */     this.cred = null;
/* 353 */     this.delegatedCred = null;
/* 354 */     if (this.pContext != 0L) {
/* 355 */       this.pContext = this.cStub.deleteContext(this.pContext);
/* 356 */       this.pContext = 0L;
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getWrapSizeLimit(int paramInt1, boolean paramBoolean, int paramInt2)
/*     */     throws GSSException
/*     */   {
/* 363 */     return this.cStub.wrapSizeLimit(this.pContext, paramBoolean ? 1 : 0, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public byte[] wrap(byte[] paramArrayOfByte, int paramInt1, int paramInt2, MessageProp paramMessageProp)
/*     */     throws GSSException
/*     */   {
/* 369 */     byte[] arrayOfByte = paramArrayOfByte;
/* 370 */     if ((paramInt1 != 0) || (paramInt2 != paramArrayOfByte.length)) {
/* 371 */       arrayOfByte = new byte[paramInt2];
/* 372 */       System.arraycopy(paramArrayOfByte, paramInt1, arrayOfByte, 0, paramInt2);
/*     */     }
/* 374 */     return this.cStub.wrap(this.pContext, arrayOfByte, paramMessageProp);
/*     */   }
/*     */ 
/*     */   public void wrap(byte[] paramArrayOfByte, int paramInt1, int paramInt2, OutputStream paramOutputStream, MessageProp paramMessageProp) throws GSSException
/*     */   {
/*     */     try {
/* 380 */       byte[] arrayOfByte = wrap(paramArrayOfByte, paramInt1, paramInt2, paramMessageProp);
/* 381 */       paramOutputStream.write(arrayOfByte);
/*     */     } catch (IOException localIOException) {
/* 383 */       throw new GSSExceptionImpl(11, localIOException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public int wrap(byte[] paramArrayOfByte1, int paramInt1, int paramInt2, byte[] paramArrayOfByte2, int paramInt3, MessageProp paramMessageProp) throws GSSException
/*     */   {
/* 389 */     byte[] arrayOfByte = wrap(paramArrayOfByte1, paramInt1, paramInt2, paramMessageProp);
/* 390 */     System.arraycopy(arrayOfByte, 0, paramArrayOfByte2, paramInt3, arrayOfByte.length);
/* 391 */     return arrayOfByte.length;
/*     */   }
/*     */ 
/*     */   public void wrap(InputStream paramInputStream, OutputStream paramOutputStream, MessageProp paramMessageProp) throws GSSException {
/*     */     try {
/* 396 */       byte[] arrayOfByte1 = new byte[paramInputStream.available()];
/* 397 */       int i = paramInputStream.read(arrayOfByte1);
/* 398 */       byte[] arrayOfByte2 = wrap(arrayOfByte1, 0, i, paramMessageProp);
/* 399 */       paramOutputStream.write(arrayOfByte2);
/*     */     } catch (IOException localIOException) {
/* 401 */       throw new GSSExceptionImpl(11, localIOException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public byte[] unwrap(byte[] paramArrayOfByte, int paramInt1, int paramInt2, MessageProp paramMessageProp)
/*     */     throws GSSException
/*     */   {
/* 408 */     if ((paramInt1 != 0) || (paramInt2 != paramArrayOfByte.length)) {
/* 409 */       byte[] arrayOfByte = new byte[paramInt2];
/* 410 */       System.arraycopy(paramArrayOfByte, paramInt1, arrayOfByte, 0, paramInt2);
/* 411 */       return this.cStub.unwrap(this.pContext, arrayOfByte, paramMessageProp);
/*     */     }
/* 413 */     return this.cStub.unwrap(this.pContext, paramArrayOfByte, paramMessageProp);
/*     */   }
/*     */ 
/*     */   public int unwrap(byte[] paramArrayOfByte1, int paramInt1, int paramInt2, byte[] paramArrayOfByte2, int paramInt3, MessageProp paramMessageProp)
/*     */     throws GSSException
/*     */   {
/* 419 */     byte[] arrayOfByte1 = null;
/* 420 */     if ((paramInt1 != 0) || (paramInt2 != paramArrayOfByte1.length)) {
/* 421 */       byte[] arrayOfByte2 = new byte[paramInt2];
/* 422 */       System.arraycopy(paramArrayOfByte1, paramInt1, arrayOfByte2, 0, paramInt2);
/* 423 */       arrayOfByte1 = this.cStub.unwrap(this.pContext, arrayOfByte2, paramMessageProp);
/*     */     } else {
/* 425 */       arrayOfByte1 = this.cStub.unwrap(this.pContext, paramArrayOfByte1, paramMessageProp);
/*     */     }
/* 427 */     System.arraycopy(arrayOfByte1, 0, paramArrayOfByte2, paramInt3, arrayOfByte1.length);
/* 428 */     return arrayOfByte1.length;
/*     */   }
/*     */ 
/*     */   public void unwrap(InputStream paramInputStream, OutputStream paramOutputStream, MessageProp paramMessageProp) throws GSSException {
/*     */     try {
/* 433 */       byte[] arrayOfByte1 = new byte[paramInputStream.available()];
/* 434 */       int i = paramInputStream.read(arrayOfByte1);
/* 435 */       byte[] arrayOfByte2 = unwrap(arrayOfByte1, 0, i, paramMessageProp);
/* 436 */       paramOutputStream.write(arrayOfByte2);
/* 437 */       paramOutputStream.flush();
/*     */     } catch (IOException localIOException) {
/* 439 */       throw new GSSExceptionImpl(11, localIOException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public int unwrap(InputStream paramInputStream, byte[] paramArrayOfByte, int paramInt, MessageProp paramMessageProp)
/*     */     throws GSSException
/*     */   {
/* 446 */     byte[] arrayOfByte1 = null;
/* 447 */     int i = 0;
/*     */     try {
/* 449 */       arrayOfByte1 = new byte[paramInputStream.available()];
/* 450 */       i = paramInputStream.read(arrayOfByte1);
/* 451 */       byte[] arrayOfByte2 = unwrap(arrayOfByte1, 0, i, paramMessageProp);
/*     */     } catch (IOException localIOException) {
/* 453 */       throw new GSSExceptionImpl(11, localIOException);
/*     */     }
/* 455 */     byte[] arrayOfByte3 = unwrap(arrayOfByte1, 0, i, paramMessageProp);
/* 456 */     System.arraycopy(arrayOfByte3, 0, paramArrayOfByte, paramInt, arrayOfByte3.length);
/* 457 */     return arrayOfByte3.length;
/*     */   }
/*     */ 
/*     */   public byte[] getMIC(byte[] paramArrayOfByte, int paramInt1, int paramInt2, MessageProp paramMessageProp) throws GSSException
/*     */   {
/* 462 */     int i = paramMessageProp == null ? 0 : paramMessageProp.getQOP();
/* 463 */     byte[] arrayOfByte = paramArrayOfByte;
/* 464 */     if ((paramInt1 != 0) || (paramInt2 != paramArrayOfByte.length)) {
/* 465 */       arrayOfByte = new byte[paramInt2];
/* 466 */       System.arraycopy(paramArrayOfByte, paramInt1, arrayOfByte, 0, paramInt2);
/*     */     }
/* 468 */     return this.cStub.getMic(this.pContext, i, arrayOfByte);
/*     */   }
/*     */ 
/*     */   public void getMIC(InputStream paramInputStream, OutputStream paramOutputStream, MessageProp paramMessageProp) throws GSSException
/*     */   {
/*     */     try {
/* 474 */       int i = 0;
/* 475 */       byte[] arrayOfByte1 = new byte[paramInputStream.available()];
/* 476 */       i = paramInputStream.read(arrayOfByte1);
/*     */ 
/* 478 */       byte[] arrayOfByte2 = getMIC(arrayOfByte1, 0, i, paramMessageProp);
/* 479 */       if ((arrayOfByte2 != null) && (arrayOfByte2.length != 0))
/* 480 */         paramOutputStream.write(arrayOfByte2);
/*     */     }
/*     */     catch (IOException localIOException) {
/* 483 */       throw new GSSExceptionImpl(11, localIOException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void verifyMIC(byte[] paramArrayOfByte1, int paramInt1, int paramInt2, byte[] paramArrayOfByte2, int paramInt3, int paramInt4, MessageProp paramMessageProp)
/*     */     throws GSSException
/*     */   {
/* 490 */     byte[] arrayOfByte1 = paramArrayOfByte1;
/* 491 */     byte[] arrayOfByte2 = paramArrayOfByte2;
/* 492 */     if ((paramInt1 != 0) || (paramInt2 != paramArrayOfByte1.length)) {
/* 493 */       arrayOfByte1 = new byte[paramInt2];
/* 494 */       System.arraycopy(paramArrayOfByte1, paramInt1, arrayOfByte1, 0, paramInt2);
/*     */     }
/* 496 */     if ((paramInt3 != 0) || (paramInt4 != paramArrayOfByte2.length)) {
/* 497 */       arrayOfByte2 = new byte[paramInt4];
/* 498 */       System.arraycopy(paramArrayOfByte2, paramInt3, arrayOfByte2, 0, paramInt4);
/*     */     }
/* 500 */     this.cStub.verifyMic(this.pContext, arrayOfByte1, arrayOfByte2, paramMessageProp);
/*     */   }
/*     */ 
/*     */   public void verifyMIC(InputStream paramInputStream1, InputStream paramInputStream2, MessageProp paramMessageProp) throws GSSException
/*     */   {
/*     */     try {
/* 506 */       byte[] arrayOfByte1 = new byte[paramInputStream2.available()];
/* 507 */       int i = paramInputStream2.read(arrayOfByte1);
/* 508 */       byte[] arrayOfByte2 = new byte[paramInputStream1.available()];
/* 509 */       int j = paramInputStream1.read(arrayOfByte2);
/* 510 */       verifyMIC(arrayOfByte2, 0, j, arrayOfByte1, 0, i, paramMessageProp);
/*     */     } catch (IOException localIOException) {
/* 512 */       throw new GSSExceptionImpl(11, localIOException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public byte[] export() throws GSSException {
/* 517 */     byte[] arrayOfByte = this.cStub.exportContext(this.pContext);
/* 518 */     this.pContext = 0L;
/* 519 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   private void changeFlags(int paramInt, boolean paramBoolean) {
/* 523 */     if ((this.isInitiator) && (this.pContext == 0L))
/* 524 */       if (paramBoolean)
/* 525 */         this.flags |= paramInt;
/*     */       else
/* 527 */         this.flags &= (paramInt ^ 0xFFFFFFFF);
/*     */   }
/*     */ 
/*     */   public void requestMutualAuth(boolean paramBoolean) throws GSSException
/*     */   {
/* 532 */     changeFlags(2, paramBoolean);
/*     */   }
/*     */   public void requestReplayDet(boolean paramBoolean) throws GSSException {
/* 535 */     changeFlags(4, paramBoolean);
/*     */   }
/*     */   public void requestSequenceDet(boolean paramBoolean) throws GSSException {
/* 538 */     changeFlags(8, paramBoolean);
/*     */   }
/*     */   public void requestCredDeleg(boolean paramBoolean) throws GSSException {
/* 541 */     changeFlags(1, paramBoolean);
/*     */   }
/*     */   public void requestAnonymity(boolean paramBoolean) throws GSSException {
/* 544 */     changeFlags(64, paramBoolean);
/*     */   }
/*     */   public void requestConf(boolean paramBoolean) throws GSSException {
/* 547 */     changeFlags(16, paramBoolean);
/*     */   }
/*     */   public void requestInteg(boolean paramBoolean) throws GSSException {
/* 550 */     changeFlags(32, paramBoolean);
/*     */   }
/*     */   public void requestDelegPolicy(boolean paramBoolean) throws GSSException {
/*     */   }
/*     */ 
/*     */   public void requestLifetime(int paramInt) throws GSSException {
/* 556 */     if ((this.isInitiator) && (this.pContext == 0L))
/* 557 */       this.lifetime = paramInt;
/*     */   }
/*     */ 
/*     */   public void setChannelBinding(ChannelBinding paramChannelBinding) throws GSSException {
/* 561 */     if (this.pContext == 0L)
/* 562 */       this.cb = paramChannelBinding;
/*     */   }
/*     */ 
/*     */   private boolean checkFlags(int paramInt)
/*     */   {
/* 567 */     return (this.flags & paramInt) != 0;
/*     */   }
/*     */   public boolean getCredDelegState() {
/* 570 */     return checkFlags(1);
/*     */   }
/*     */   public boolean getMutualAuthState() {
/* 573 */     return checkFlags(2);
/*     */   }
/*     */   public boolean getReplayDetState() {
/* 576 */     return checkFlags(4);
/*     */   }
/*     */   public boolean getSequenceDetState() {
/* 579 */     return checkFlags(8);
/*     */   }
/*     */   public boolean getAnonymityState() {
/* 582 */     return checkFlags(64);
/*     */   }
/*     */   public boolean isTransferable() throws GSSException {
/* 585 */     return checkFlags(256);
/*     */   }
/*     */   public boolean isProtReady() {
/* 588 */     return checkFlags(128);
/*     */   }
/*     */   public boolean getConfState() {
/* 591 */     return checkFlags(16);
/*     */   }
/*     */   public boolean getIntegState() {
/* 594 */     return checkFlags(32);
/*     */   }
/*     */   public boolean getDelegPolicyState() {
/* 597 */     return false;
/*     */   }
/*     */   public int getLifetime() {
/* 600 */     return this.cStub.getContextTime(this.pContext);
/*     */   }
/*     */   public GSSNameSpi getSrcName() throws GSSException {
/* 603 */     return this.srcName;
/*     */   }
/*     */   public GSSNameSpi getTargName() throws GSSException {
/* 606 */     return this.targetName;
/*     */   }
/*     */   public Oid getMech() throws GSSException {
/* 609 */     if ((this.isEstablished) && (this.actualMech != null)) {
/* 610 */       return this.actualMech;
/*     */     }
/* 612 */     return this.cStub.getMech();
/*     */   }
/*     */ 
/*     */   public GSSCredentialSpi getDelegCred() throws GSSException {
/* 616 */     return this.delegatedCred;
/*     */   }
/*     */   public boolean isInitiator() {
/* 619 */     return this.isInitiator;
/*     */   }
/*     */ 
/*     */   protected void finalize() throws Throwable {
/* 623 */     dispose();
/*     */   }
/*     */ 
/*     */   public Object inquireSecContext(InquireType paramInquireType) throws GSSException
/*     */   {
/* 628 */     throw new GSSException(16, -1, "Inquire type not supported.");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.jgss.wrapper.NativeGSSContext
 * JD-Core Version:    0.6.2
 */