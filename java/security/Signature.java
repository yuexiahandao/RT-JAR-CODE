/*      */ package java.security;
/*      */ 
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.security.cert.Certificate;
/*      */ import java.security.cert.X509Certificate;
/*      */ import java.security.spec.AlgorithmParameterSpec;
/*      */ import java.util.Arrays;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import javax.crypto.BadPaddingException;
/*      */ import javax.crypto.Cipher;
/*      */ import javax.crypto.IllegalBlockSizeException;
/*      */ import javax.crypto.NoSuchPaddingException;
/*      */ import sun.security.jca.GetInstance;
/*      */ import sun.security.jca.GetInstance.Instance;
/*      */ import sun.security.jca.ServiceId;
/*      */ import sun.security.util.Debug;
/*      */ 
/*      */ public abstract class Signature extends SignatureSpi
/*      */ {
/*  121 */   private static final Debug debug = Debug.getInstance("jca", "Signature");
/*      */   private String algorithm;
/*      */   Provider provider;
/*      */   protected static final int UNINITIALIZED = 0;
/*      */   protected static final int SIGN = 2;
/*      */   protected static final int VERIFY = 3;
/*  155 */   protected int state = 0;
/*      */   private static final String RSA_SIGNATURE = "NONEwithRSA";
/*      */   private static final String RSA_CIPHER = "RSA/ECB/PKCS1Padding";
/*  177 */   private static final List<ServiceId> rsaIds = Arrays.asList(new ServiceId[] { new ServiceId("Signature", "NONEwithRSA"), new ServiceId("Cipher", "RSA/ECB/PKCS1Padding"), new ServiceId("Cipher", "RSA/ECB"), new ServiceId("Cipher", "RSA//PKCS1Padding"), new ServiceId("Cipher", "RSA") });
/*      */ 
/*  262 */   private static final Map<String, Boolean> signatureInfo = new ConcurrentHashMap();
/*      */ 
/*      */   protected Signature(String paramString)
/*      */   {
/*  167 */     this.algorithm = paramString;
/*      */   }
/*      */ 
/*      */   public static Signature getInstance(String paramString)
/*      */     throws NoSuchAlgorithmException
/*      */   {
/*      */     List localList;
/*  217 */     if (paramString.equalsIgnoreCase("NONEwithRSA"))
/*  218 */       localList = GetInstance.getServices(rsaIds);
/*      */     else {
/*  220 */       localList = GetInstance.getServices("Signature", paramString);
/*      */     }
/*  222 */     Iterator localIterator = localList.iterator();
/*  223 */     if (!localIterator.hasNext()) {
/*  224 */       throw new NoSuchAlgorithmException(paramString + " Signature not available");
/*      */     }
/*      */ 
/*      */     NoSuchAlgorithmException localNoSuchAlgorithmException1;
/*      */     do
/*      */     {
/*  230 */       Provider.Service localService = (Provider.Service)localIterator.next();
/*  231 */       if (isSpi(localService)) {
/*  232 */         return new Delegate(localService, localIterator, paramString);
/*      */       }
/*      */       try
/*      */       {
/*  236 */         GetInstance.Instance localInstance = GetInstance.getInstance(localService, SignatureSpi.class);
/*      */ 
/*  238 */         return getInstance(localInstance, paramString);
/*      */       } catch (NoSuchAlgorithmException localNoSuchAlgorithmException2) {
/*  240 */         localNoSuchAlgorithmException1 = localNoSuchAlgorithmException2;
/*      */       }
/*      */     }
/*  243 */     while (localIterator.hasNext());
/*  244 */     throw localNoSuchAlgorithmException1;
/*      */   }
/*      */ 
/*      */   private static Signature getInstance(GetInstance.Instance paramInstance, String paramString)
/*      */   {
/*      */     Object localObject;
/*  249 */     if ((paramInstance.impl instanceof Signature)) {
/*  250 */       localObject = (Signature)paramInstance.impl;
/*      */     } else {
/*  252 */       SignatureSpi localSignatureSpi = (SignatureSpi)paramInstance.impl;
/*  253 */       localObject = new Delegate(localSignatureSpi, paramString);
/*      */     }
/*  255 */     ((Signature)localObject).provider = paramInstance.provider;
/*  256 */     return localObject;
/*      */   }
/*      */ 
/*      */   private static boolean isSpi(Provider.Service paramService)
/*      */   {
/*  278 */     if (paramService.getType().equals("Cipher"))
/*      */     {
/*  280 */       return true;
/*      */     }
/*  282 */     String str = paramService.getClassName();
/*  283 */     Boolean localBoolean = (Boolean)signatureInfo.get(str);
/*  284 */     if (localBoolean == null) {
/*      */       try {
/*  286 */         Object localObject = paramService.newInstance(null);
/*      */ 
/*  290 */         boolean bool = ((localObject instanceof SignatureSpi)) && (!(localObject instanceof Signature));
/*      */ 
/*  292 */         if ((debug != null) && (!bool)) {
/*  293 */           debug.println("Not a SignatureSpi " + str);
/*  294 */           debug.println("Delayed provider selection may not be available for algorithm " + paramService.getAlgorithm());
/*      */         }
/*      */ 
/*  297 */         localBoolean = Boolean.valueOf(bool);
/*  298 */         signatureInfo.put(str, localBoolean);
/*      */       }
/*      */       catch (Exception localException) {
/*  301 */         return false;
/*      */       }
/*      */     }
/*  304 */     return localBoolean.booleanValue();
/*      */   }
/*      */ 
/*      */   public static Signature getInstance(String paramString1, String paramString2)
/*      */     throws NoSuchAlgorithmException, NoSuchProviderException
/*      */   {
/*  343 */     if (paramString1.equalsIgnoreCase("NONEwithRSA"))
/*      */     {
/*  345 */       if ((paramString2 == null) || (paramString2.length() == 0)) {
/*  346 */         throw new IllegalArgumentException("missing provider");
/*      */       }
/*  348 */       localObject = Security.getProvider(paramString2);
/*  349 */       if (localObject == null) {
/*  350 */         throw new NoSuchProviderException("no such provider: " + paramString2);
/*      */       }
/*      */ 
/*  353 */       return getInstanceRSA((Provider)localObject);
/*      */     }
/*  355 */     Object localObject = GetInstance.getInstance("Signature", SignatureSpi.class, paramString1, paramString2);
/*      */ 
/*  357 */     return getInstance((GetInstance.Instance)localObject, paramString1);
/*      */   }
/*      */ 
/*      */   public static Signature getInstance(String paramString, Provider paramProvider)
/*      */     throws NoSuchAlgorithmException
/*      */   {
/*  391 */     if (paramString.equalsIgnoreCase("NONEwithRSA"))
/*      */     {
/*  393 */       if (paramProvider == null) {
/*  394 */         throw new IllegalArgumentException("missing provider");
/*      */       }
/*  396 */       return getInstanceRSA(paramProvider);
/*      */     }
/*  398 */     GetInstance.Instance localInstance = GetInstance.getInstance("Signature", SignatureSpi.class, paramString, paramProvider);
/*      */ 
/*  400 */     return getInstance(localInstance, paramString);
/*      */   }
/*      */ 
/*      */   private static Signature getInstanceRSA(Provider paramProvider)
/*      */     throws NoSuchAlgorithmException
/*      */   {
/*  408 */     Provider.Service localService = paramProvider.getService("Signature", "NONEwithRSA");
/*      */     Object localObject;
/*  409 */     if (localService != null) {
/*  410 */       localObject = GetInstance.getInstance(localService, SignatureSpi.class);
/*  411 */       return getInstance((GetInstance.Instance)localObject, "NONEwithRSA");
/*      */     }
/*      */     try
/*      */     {
/*  415 */       localObject = Cipher.getInstance("RSA/ECB/PKCS1Padding", paramProvider);
/*  416 */       return new Delegate(new CipherAdapter((Cipher)localObject), "NONEwithRSA");
/*      */     }
/*      */     catch (GeneralSecurityException localGeneralSecurityException)
/*      */     {
/*  420 */       throw new NoSuchAlgorithmException("no such algorithm: NONEwithRSA for provider " + paramProvider.getName(), localGeneralSecurityException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public final Provider getProvider()
/*      */   {
/*  431 */     chooseFirstProvider();
/*  432 */     return this.provider;
/*      */   }
/*      */ 
/*      */   void chooseFirstProvider()
/*      */   {
/*      */   }
/*      */ 
/*      */   public final void initVerify(PublicKey paramPublicKey)
/*      */     throws InvalidKeyException
/*      */   {
/*  451 */     engineInitVerify(paramPublicKey);
/*  452 */     this.state = 3;
/*      */   }
/*      */ 
/*      */   public final void initVerify(Certificate paramCertificate)
/*      */     throws InvalidKeyException
/*      */   {
/*  478 */     if ((paramCertificate instanceof X509Certificate))
/*      */     {
/*  482 */       localObject = (X509Certificate)paramCertificate;
/*  483 */       Set localSet = ((X509Certificate)localObject).getCriticalExtensionOIDs();
/*      */ 
/*  485 */       if ((localSet != null) && (!localSet.isEmpty()) && (localSet.contains("2.5.29.15")))
/*      */       {
/*  487 */         boolean[] arrayOfBoolean = ((X509Certificate)localObject).getKeyUsage();
/*      */ 
/*  489 */         if ((arrayOfBoolean != null) && (arrayOfBoolean[0] == 0)) {
/*  490 */           throw new InvalidKeyException("Wrong key usage");
/*      */         }
/*      */       }
/*      */     }
/*  494 */     Object localObject = paramCertificate.getPublicKey();
/*  495 */     engineInitVerify((PublicKey)localObject);
/*  496 */     this.state = 3;
/*      */   }
/*      */ 
/*      */   public final void initSign(PrivateKey paramPrivateKey)
/*      */     throws InvalidKeyException
/*      */   {
/*  511 */     engineInitSign(paramPrivateKey);
/*  512 */     this.state = 2;
/*      */   }
/*      */ 
/*      */   public final void initSign(PrivateKey paramPrivateKey, SecureRandom paramSecureRandom)
/*      */     throws InvalidKeyException
/*      */   {
/*  529 */     engineInitSign(paramPrivateKey, paramSecureRandom);
/*  530 */     this.state = 2;
/*      */   }
/*      */ 
/*      */   public final byte[] sign()
/*      */     throws SignatureException
/*      */   {
/*  552 */     if (this.state == 2) {
/*  553 */       return engineSign();
/*      */     }
/*  555 */     throw new SignatureException("object not initialized for signing");
/*      */   }
/*      */ 
/*      */   public final int sign(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*      */     throws SignatureException
/*      */   {
/*  589 */     if (paramArrayOfByte == null) {
/*  590 */       throw new IllegalArgumentException("No output buffer given");
/*      */     }
/*  592 */     if ((paramInt1 < 0) || (paramInt2 < 0)) {
/*  593 */       throw new IllegalArgumentException("offset or len is less than 0");
/*      */     }
/*  595 */     if (paramArrayOfByte.length - paramInt1 < paramInt2) {
/*  596 */       throw new IllegalArgumentException("Output buffer too small for specified offset and length");
/*      */     }
/*      */ 
/*  599 */     if (this.state != 2) {
/*  600 */       throw new SignatureException("object not initialized for signing");
/*      */     }
/*      */ 
/*  603 */     return engineSign(paramArrayOfByte, paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   public final boolean verify(byte[] paramArrayOfByte)
/*      */     throws SignatureException
/*      */   {
/*  625 */     if (this.state == 3) {
/*  626 */       return engineVerify(paramArrayOfByte);
/*      */     }
/*  628 */     throw new SignatureException("object not initialized for verification");
/*      */   }
/*      */ 
/*      */   public final boolean verify(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*      */     throws SignatureException
/*      */   {
/*  662 */     if (this.state == 3) {
/*  663 */       if (paramArrayOfByte == null) {
/*  664 */         throw new IllegalArgumentException("signature is null");
/*      */       }
/*  666 */       if ((paramInt1 < 0) || (paramInt2 < 0)) {
/*  667 */         throw new IllegalArgumentException("offset or length is less than 0");
/*      */       }
/*      */ 
/*  670 */       if (paramArrayOfByte.length - paramInt1 < paramInt2) {
/*  671 */         throw new IllegalArgumentException("signature too small for specified offset and length");
/*      */       }
/*      */ 
/*  675 */       return engineVerify(paramArrayOfByte, paramInt1, paramInt2);
/*      */     }
/*  677 */     throw new SignatureException("object not initialized for verification");
/*      */   }
/*      */ 
/*      */   public final void update(byte paramByte)
/*      */     throws SignatureException
/*      */   {
/*  690 */     if ((this.state == 3) || (this.state == 2))
/*  691 */       engineUpdate(paramByte);
/*      */     else
/*  693 */       throw new SignatureException("object not initialized for signature or verification");
/*      */   }
/*      */ 
/*      */   public final void update(byte[] paramArrayOfByte)
/*      */     throws SignatureException
/*      */   {
/*  708 */     update(paramArrayOfByte, 0, paramArrayOfByte.length);
/*      */   }
/*      */ 
/*      */   public final void update(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*      */     throws SignatureException
/*      */   {
/*  724 */     if ((this.state == 2) || (this.state == 3)) {
/*  725 */       if (paramArrayOfByte == null) {
/*  726 */         throw new IllegalArgumentException("data is null");
/*      */       }
/*  728 */       if ((paramInt1 < 0) || (paramInt2 < 0)) {
/*  729 */         throw new IllegalArgumentException("off or len is less than 0");
/*      */       }
/*  731 */       if (paramArrayOfByte.length - paramInt1 < paramInt2) {
/*  732 */         throw new IllegalArgumentException("data too small for specified offset and length");
/*      */       }
/*      */ 
/*  735 */       engineUpdate(paramArrayOfByte, paramInt1, paramInt2);
/*      */     } else {
/*  737 */       throw new SignatureException("object not initialized for signature or verification");
/*      */     }
/*      */   }
/*      */ 
/*      */   public final void update(ByteBuffer paramByteBuffer)
/*      */     throws SignatureException
/*      */   {
/*  756 */     if ((this.state != 2) && (this.state != 3)) {
/*  757 */       throw new SignatureException("object not initialized for signature or verification");
/*      */     }
/*      */ 
/*  760 */     if (paramByteBuffer == null) {
/*  761 */       throw new NullPointerException();
/*      */     }
/*  763 */     engineUpdate(paramByteBuffer);
/*      */   }
/*      */ 
/*      */   public final String getAlgorithm()
/*      */   {
/*  772 */     return this.algorithm;
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/*  783 */     String str = "";
/*  784 */     switch (this.state) {
/*      */     case 0:
/*  786 */       str = "<not initialized>";
/*  787 */       break;
/*      */     case 3:
/*  789 */       str = "<initialized for verifying>";
/*  790 */       break;
/*      */     case 2:
/*  792 */       str = "<initialized for signing>";
/*      */     case 1:
/*      */     }
/*  795 */     return "Signature object: " + getAlgorithm() + str;
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public final void setParameter(String paramString, Object paramObject)
/*      */     throws InvalidParameterException
/*      */   {
/*  826 */     engineSetParameter(paramString, paramObject);
/*      */   }
/*      */ 
/*      */   public final void setParameter(AlgorithmParameterSpec paramAlgorithmParameterSpec)
/*      */     throws InvalidAlgorithmParameterException
/*      */   {
/*  841 */     engineSetParameter(paramAlgorithmParameterSpec);
/*      */   }
/*      */ 
/*      */   public final AlgorithmParameters getParameters()
/*      */   {
/*  860 */     return engineGetParameters();
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public final Object getParameter(String paramString)
/*      */     throws InvalidParameterException
/*      */   {
/*  889 */     return engineGetParameter(paramString);
/*      */   }
/*      */ 
/*      */   public Object clone()
/*      */     throws CloneNotSupportedException
/*      */   {
/*  901 */     if ((this instanceof Cloneable)) {
/*  902 */       return super.clone();
/*      */     }
/*  904 */     throw new CloneNotSupportedException();
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*  263 */     Boolean localBoolean = Boolean.TRUE;
/*      */ 
/*  265 */     signatureInfo.put("sun.security.provider.DSA$RawDSA", localBoolean);
/*  266 */     signatureInfo.put("sun.security.provider.DSA$SHA1withDSA", localBoolean);
/*  267 */     signatureInfo.put("sun.security.rsa.RSASignature$MD2withRSA", localBoolean);
/*  268 */     signatureInfo.put("sun.security.rsa.RSASignature$MD5withRSA", localBoolean);
/*  269 */     signatureInfo.put("sun.security.rsa.RSASignature$SHA1withRSA", localBoolean);
/*  270 */     signatureInfo.put("sun.security.rsa.RSASignature$SHA256withRSA", localBoolean);
/*  271 */     signatureInfo.put("sun.security.rsa.RSASignature$SHA384withRSA", localBoolean);
/*  272 */     signatureInfo.put("sun.security.rsa.RSASignature$SHA512withRSA", localBoolean);
/*  273 */     signatureInfo.put("com.sun.net.ssl.internal.ssl.RSASignature", localBoolean);
/*  274 */     signatureInfo.put("sun.security.pkcs11.P11Signature", localBoolean);
/*      */   }
/*      */ 
/*      */   private static class CipherAdapter extends SignatureSpi
/*      */   {
/*      */     private final Cipher cipher;
/*      */     private ByteArrayOutputStream data;
/*      */ 
/*      */     CipherAdapter(Cipher paramCipher)
/*      */     {
/* 1233 */       this.cipher = paramCipher;
/*      */     }
/*      */ 
/*      */     protected void engineInitVerify(PublicKey paramPublicKey) throws InvalidKeyException
/*      */     {
/* 1238 */       this.cipher.init(2, paramPublicKey);
/* 1239 */       if (this.data == null)
/* 1240 */         this.data = new ByteArrayOutputStream(128);
/*      */       else
/* 1242 */         this.data.reset();
/*      */     }
/*      */ 
/*      */     protected void engineInitSign(PrivateKey paramPrivateKey)
/*      */       throws InvalidKeyException
/*      */     {
/* 1248 */       this.cipher.init(1, paramPrivateKey);
/* 1249 */       this.data = null;
/*      */     }
/*      */ 
/*      */     protected void engineInitSign(PrivateKey paramPrivateKey, SecureRandom paramSecureRandom) throws InvalidKeyException
/*      */     {
/* 1254 */       this.cipher.init(1, paramPrivateKey, paramSecureRandom);
/* 1255 */       this.data = null;
/*      */     }
/*      */ 
/*      */     protected void engineUpdate(byte paramByte) throws SignatureException {
/* 1259 */       engineUpdate(new byte[] { paramByte }, 0, 1);
/*      */     }
/*      */ 
/*      */     protected void engineUpdate(byte[] paramArrayOfByte, int paramInt1, int paramInt2) throws SignatureException
/*      */     {
/* 1264 */       if (this.data != null) {
/* 1265 */         this.data.write(paramArrayOfByte, paramInt1, paramInt2);
/* 1266 */         return;
/*      */       }
/* 1268 */       byte[] arrayOfByte = this.cipher.update(paramArrayOfByte, paramInt1, paramInt2);
/* 1269 */       if ((arrayOfByte != null) && (arrayOfByte.length != 0))
/* 1270 */         throw new SignatureException("Cipher unexpectedly returned data");
/*      */     }
/*      */ 
/*      */     protected byte[] engineSign() throws SignatureException
/*      */     {
/*      */       try
/*      */       {
/* 1277 */         return this.cipher.doFinal();
/*      */       } catch (IllegalBlockSizeException localIllegalBlockSizeException) {
/* 1279 */         throw new SignatureException("doFinal() failed", localIllegalBlockSizeException);
/*      */       } catch (BadPaddingException localBadPaddingException) {
/* 1281 */         throw new SignatureException("doFinal() failed", localBadPaddingException);
/*      */       }
/*      */     }
/*      */ 
/*      */     protected boolean engineVerify(byte[] paramArrayOfByte) throws SignatureException
/*      */     {
/*      */       try {
/* 1288 */         byte[] arrayOfByte1 = this.cipher.doFinal(paramArrayOfByte);
/* 1289 */         byte[] arrayOfByte2 = this.data.toByteArray();
/* 1290 */         this.data.reset();
/* 1291 */         return Arrays.equals(arrayOfByte1, arrayOfByte2);
/*      */       }
/*      */       catch (BadPaddingException localBadPaddingException)
/*      */       {
/* 1295 */         return false;
/*      */       } catch (IllegalBlockSizeException localIllegalBlockSizeException) {
/* 1297 */         throw new SignatureException("doFinal() failed", localIllegalBlockSizeException);
/*      */       }
/*      */     }
/*      */ 
/*      */     protected void engineSetParameter(String paramString, Object paramObject) throws InvalidParameterException
/*      */     {
/* 1303 */       throw new InvalidParameterException("Parameters not supported");
/*      */     }
/*      */ 
/*      */     protected Object engineGetParameter(String paramString) throws InvalidParameterException
/*      */     {
/* 1308 */       throw new InvalidParameterException("Parameters not supported");
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class Delegate extends Signature
/*      */   {
/*      */     private SignatureSpi sigSpi;
/*      */     private final Object lock;
/*      */     private Provider.Service firstService;
/*      */     private Iterator<Provider.Service> serviceIterator;
/* 1000 */     private static int warnCount = 10;
/*      */     private static final int I_PUB = 1;
/*      */     private static final int I_PRIV = 2;
/*      */     private static final int I_PRIV_SR = 3;
/*      */ 
/*      */     Delegate(SignatureSpi paramSignatureSpi, String paramString)
/*      */     {
/*  941 */       super();
/*  942 */       this.sigSpi = paramSignatureSpi;
/*  943 */       this.lock = null;
/*      */     }
/*      */ 
/*      */     Delegate(Provider.Service paramService, Iterator<Provider.Service> paramIterator, String paramString)
/*      */     {
/*  949 */       super();
/*  950 */       this.firstService = paramService;
/*  951 */       this.serviceIterator = paramIterator;
/*  952 */       this.lock = new Object();
/*      */     }
/*      */ 
/*      */     public Object clone()
/*      */       throws CloneNotSupportedException
/*      */     {
/*  964 */       chooseFirstProvider();
/*  965 */       if ((this.sigSpi instanceof Cloneable)) {
/*  966 */         SignatureSpi localSignatureSpi = (SignatureSpi)this.sigSpi.clone();
/*      */ 
/*  970 */         Delegate localDelegate = new Delegate(localSignatureSpi, this.algorithm);
/*      */ 
/*  972 */         localDelegate.provider = this.provider;
/*  973 */         return localDelegate;
/*      */       }
/*  975 */       throw new CloneNotSupportedException();
/*      */     }
/*      */ 
/*      */     private static SignatureSpi newInstance(Provider.Service paramService)
/*      */       throws NoSuchAlgorithmException
/*      */     {
/*  981 */       if (paramService.getType().equals("Cipher")) {
/*      */         try
/*      */         {
/*  984 */           Cipher localCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", paramService.getProvider());
/*  985 */           return new Signature.CipherAdapter(localCipher);
/*      */         } catch (NoSuchPaddingException localNoSuchPaddingException) {
/*  987 */           throw new NoSuchAlgorithmException(localNoSuchPaddingException);
/*      */         }
/*      */       }
/*  990 */       Object localObject = paramService.newInstance(null);
/*  991 */       if (!(localObject instanceof SignatureSpi)) {
/*  992 */         throw new NoSuchAlgorithmException("Not a SignatureSpi: " + localObject.getClass().getName());
/*      */       }
/*      */ 
/*  995 */       return (SignatureSpi)localObject;
/*      */     }
/*      */ 
/*      */     void chooseFirstProvider()
/*      */     {
/* 1008 */       if (this.sigSpi != null) {
/* 1009 */         return;
/*      */       }
/* 1011 */       synchronized (this.lock) {
/* 1012 */         if (this.sigSpi != null) {
/* 1013 */           return;
/*      */         }
/* 1015 */         if (Signature.debug != null) {
/* 1016 */           int i = --warnCount;
/* 1017 */           if (i >= 0) {
/* 1018 */             Signature.debug.println("Signature.init() not first method called, disabling delayed provider selection");
/*      */ 
/* 1020 */             if (i == 0) {
/* 1021 */               Signature.debug.println("Further warnings of this type will be suppressed");
/*      */             }
/*      */ 
/* 1024 */             new Exception("Call trace").printStackTrace();
/*      */           }
/*      */         }
/* 1027 */         Object localObject1 = null;
/* 1028 */         while ((this.firstService != null) || (this.serviceIterator.hasNext()))
/*      */         {
/* 1030 */           if (this.firstService != null) {
/* 1031 */             localObject2 = this.firstService;
/* 1032 */             this.firstService = null;
/*      */           } else {
/* 1034 */             localObject2 = (Provider.Service)this.serviceIterator.next();
/*      */           }
/* 1036 */           if (Signature.isSpi((Provider.Service)localObject2))
/*      */           {
/*      */             try
/*      */             {
/* 1040 */               this.sigSpi = newInstance((Provider.Service)localObject2);
/* 1041 */               this.provider = ((Provider.Service)localObject2).getProvider();
/*      */ 
/* 1043 */               this.firstService = null;
/* 1044 */               this.serviceIterator = null;
/* 1045 */               return;
/*      */             } catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
/* 1047 */               localObject1 = localNoSuchAlgorithmException;
/*      */             }
/*      */           }
/*      */         }
/* 1050 */         Object localObject2 = new ProviderException("Could not construct SignatureSpi instance");
/*      */ 
/* 1052 */         if (localObject1 != null) {
/* 1053 */           ((ProviderException)localObject2).initCause(localObject1);
/*      */         }
/* 1055 */         throw ((Throwable)localObject2);
/*      */       }
/*      */     }
/*      */ 
/*      */     private void chooseProvider(int paramInt, Key paramKey, SecureRandom paramSecureRandom) throws InvalidKeyException
/*      */     {
/* 1061 */       synchronized (this.lock) {
/* 1062 */         if (this.sigSpi != null) {
/* 1063 */           init(this.sigSpi, paramInt, paramKey, paramSecureRandom);
/* 1064 */           return;
/*      */         }
/* 1066 */         Object localObject1 = null;
/* 1067 */         while ((this.firstService != null) || (this.serviceIterator.hasNext()))
/*      */         {
/* 1069 */           if (this.firstService != null) {
/* 1070 */             localObject2 = this.firstService;
/* 1071 */             this.firstService = null;
/*      */           } else {
/* 1073 */             localObject2 = (Provider.Service)this.serviceIterator.next();
/*      */           }
/*      */ 
/* 1076 */           if ((((Provider.Service)localObject2).supportsParameter(paramKey)) && 
/* 1080 */             (Signature.isSpi((Provider.Service)localObject2)))
/*      */           {
/*      */             try
/*      */             {
/* 1084 */               SignatureSpi localSignatureSpi = newInstance((Provider.Service)localObject2);
/* 1085 */               init(localSignatureSpi, paramInt, paramKey, paramSecureRandom);
/* 1086 */               this.provider = ((Provider.Service)localObject2).getProvider();
/* 1087 */               this.sigSpi = localSignatureSpi;
/* 1088 */               this.firstService = null;
/* 1089 */               this.serviceIterator = null;
/* 1090 */               return;
/*      */             }
/*      */             catch (Exception localException)
/*      */             {
/* 1095 */               if (localObject1 == null) {
/* 1096 */                 localObject1 = localException;
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/* 1101 */         if ((localObject1 instanceof InvalidKeyException)) {
/* 1102 */           throw ((InvalidKeyException)localObject1);
/*      */         }
/* 1104 */         if ((localObject1 instanceof RuntimeException)) {
/* 1105 */           throw ((RuntimeException)localObject1);
/*      */         }
/* 1107 */         Object localObject2 = paramKey != null ? paramKey.getClass().getName() : "(null)";
/* 1108 */         throw new InvalidKeyException("No installed provider supports this key: " + (String)localObject2, localObject1);
/*      */       }
/*      */     }
/*      */ 
/*      */     private void init(SignatureSpi paramSignatureSpi, int paramInt, Key paramKey, SecureRandom paramSecureRandom)
/*      */       throws InvalidKeyException
/*      */     {
/* 1120 */       switch (paramInt) {
/*      */       case 1:
/* 1122 */         paramSignatureSpi.engineInitVerify((PublicKey)paramKey);
/* 1123 */         break;
/*      */       case 2:
/* 1125 */         paramSignatureSpi.engineInitSign((PrivateKey)paramKey);
/* 1126 */         break;
/*      */       case 3:
/* 1128 */         paramSignatureSpi.engineInitSign((PrivateKey)paramKey, paramSecureRandom);
/* 1129 */         break;
/*      */       default:
/* 1131 */         throw new AssertionError("Internal error: " + paramInt);
/*      */       }
/*      */     }
/*      */ 
/*      */     protected void engineInitVerify(PublicKey paramPublicKey) throws InvalidKeyException
/*      */     {
/* 1137 */       if (this.sigSpi != null)
/* 1138 */         this.sigSpi.engineInitVerify(paramPublicKey);
/*      */       else
/* 1140 */         chooseProvider(1, paramPublicKey, null);
/*      */     }
/*      */ 
/*      */     protected void engineInitSign(PrivateKey paramPrivateKey)
/*      */       throws InvalidKeyException
/*      */     {
/* 1146 */       if (this.sigSpi != null)
/* 1147 */         this.sigSpi.engineInitSign(paramPrivateKey);
/*      */       else
/* 1149 */         chooseProvider(2, paramPrivateKey, null);
/*      */     }
/*      */ 
/*      */     protected void engineInitSign(PrivateKey paramPrivateKey, SecureRandom paramSecureRandom)
/*      */       throws InvalidKeyException
/*      */     {
/* 1155 */       if (this.sigSpi != null)
/* 1156 */         this.sigSpi.engineInitSign(paramPrivateKey, paramSecureRandom);
/*      */       else
/* 1158 */         chooseProvider(3, paramPrivateKey, paramSecureRandom);
/*      */     }
/*      */ 
/*      */     protected void engineUpdate(byte paramByte) throws SignatureException
/*      */     {
/* 1163 */       chooseFirstProvider();
/* 1164 */       this.sigSpi.engineUpdate(paramByte);
/*      */     }
/*      */ 
/*      */     protected void engineUpdate(byte[] paramArrayOfByte, int paramInt1, int paramInt2) throws SignatureException
/*      */     {
/* 1169 */       chooseFirstProvider();
/* 1170 */       this.sigSpi.engineUpdate(paramArrayOfByte, paramInt1, paramInt2);
/*      */     }
/*      */ 
/*      */     protected void engineUpdate(ByteBuffer paramByteBuffer) {
/* 1174 */       chooseFirstProvider();
/* 1175 */       this.sigSpi.engineUpdate(paramByteBuffer);
/*      */     }
/*      */ 
/*      */     protected byte[] engineSign() throws SignatureException {
/* 1179 */       chooseFirstProvider();
/* 1180 */       return this.sigSpi.engineSign();
/*      */     }
/*      */ 
/*      */     protected int engineSign(byte[] paramArrayOfByte, int paramInt1, int paramInt2) throws SignatureException
/*      */     {
/* 1185 */       chooseFirstProvider();
/* 1186 */       return this.sigSpi.engineSign(paramArrayOfByte, paramInt1, paramInt2);
/*      */     }
/*      */ 
/*      */     protected boolean engineVerify(byte[] paramArrayOfByte) throws SignatureException
/*      */     {
/* 1191 */       chooseFirstProvider();
/* 1192 */       return this.sigSpi.engineVerify(paramArrayOfByte);
/*      */     }
/*      */ 
/*      */     protected boolean engineVerify(byte[] paramArrayOfByte, int paramInt1, int paramInt2) throws SignatureException
/*      */     {
/* 1197 */       chooseFirstProvider();
/* 1198 */       return this.sigSpi.engineVerify(paramArrayOfByte, paramInt1, paramInt2);
/*      */     }
/*      */ 
/*      */     protected void engineSetParameter(String paramString, Object paramObject) throws InvalidParameterException
/*      */     {
/* 1203 */       chooseFirstProvider();
/* 1204 */       this.sigSpi.engineSetParameter(paramString, paramObject);
/*      */     }
/*      */ 
/*      */     protected void engineSetParameter(AlgorithmParameterSpec paramAlgorithmParameterSpec) throws InvalidAlgorithmParameterException
/*      */     {
/* 1209 */       chooseFirstProvider();
/* 1210 */       this.sigSpi.engineSetParameter(paramAlgorithmParameterSpec);
/*      */     }
/*      */ 
/*      */     protected Object engineGetParameter(String paramString) throws InvalidParameterException
/*      */     {
/* 1215 */       chooseFirstProvider();
/* 1216 */       return this.sigSpi.engineGetParameter(paramString);
/*      */     }
/*      */ 
/*      */     protected AlgorithmParameters engineGetParameters() {
/* 1220 */       chooseFirstProvider();
/* 1221 */       return this.sigSpi.engineGetParameters();
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.Signature
 * JD-Core Version:    0.6.2
 */