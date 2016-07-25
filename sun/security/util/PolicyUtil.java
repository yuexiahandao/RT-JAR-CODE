/*    */ package sun.security.util;
/*    */ 
/*    */ import java.io.BufferedInputStream;
/*    */ import java.io.File;
/*    */ import java.io.FileInputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.net.MalformedURLException;
/*    */ import java.net.URL;
/*    */ import java.security.KeyStore;
/*    */ import java.security.KeyStoreException;
/*    */ import java.security.NoSuchAlgorithmException;
/*    */ import java.security.NoSuchProviderException;
/*    */ import java.security.cert.CertificateException;
/*    */ import java.util.Arrays;
/*    */ import sun.net.www.ParseUtil;
/*    */ 
/*    */ public class PolicyUtil
/*    */ {
/*    */   private static final String P11KEYSTORE = "PKCS11";
/*    */   private static final String NONE = "NONE";
/*    */ 
/*    */   public static InputStream getInputStream(URL paramURL)
/*    */     throws IOException
/*    */   {
/* 57 */     if ("file".equals(paramURL.getProtocol())) {
/* 58 */       String str = paramURL.getFile().replace('/', File.separatorChar);
/* 59 */       str = ParseUtil.decode(str);
/* 60 */       return new FileInputStream(str);
/*    */     }
/* 62 */     return paramURL.openStream();
/*    */   }
/*    */ 
/*    */   public static KeyStore getKeyStore(URL paramURL, String paramString1, String paramString2, String paramString3, String paramString4, Debug paramDebug)
/*    */     throws KeyStoreException, MalformedURLException, IOException, NoSuchProviderException, NoSuchAlgorithmException, CertificateException
/*    */   {
/* 81 */     if (paramString1 == null) {
/* 82 */       throw new IllegalArgumentException("null KeyStore name");
/*    */     }
/*    */ 
/* 85 */     char[] arrayOfChar = null;
/*    */     try
/*    */     {
/* 88 */       if (paramString2 == null) {
/* 89 */         paramString2 = KeyStore.getDefaultType();
/*    */       }
/*    */ 
/* 92 */       if (("PKCS11".equalsIgnoreCase(paramString2)) && (!"NONE".equals(paramString1)))
/*    */       {
/* 94 */         throw new IllegalArgumentException("Invalid value (" + paramString1 + ") for keystore URL.  If the keystore type is \"" + "PKCS11" + "\", the keystore url must be \"" + "NONE" + "\"");
/*    */       }
/*    */       KeyStore localKeyStore1;
/* 104 */       if (paramString3 != null)
/* 105 */         localKeyStore1 = KeyStore.getInstance(paramString2, paramString3);
/*    */       else {
/* 107 */         localKeyStore1 = KeyStore.getInstance(paramString2);
/*    */       }
/*    */ 
/* 110 */       if (paramString4 != null)
/*    */       {
/*    */         try {
/* 113 */           localObject1 = new URL(paramString4);
/*    */         }
/*    */         catch (MalformedURLException localMalformedURLException1)
/*    */         {
/* 117 */           if (paramURL == null) {
/* 118 */             throw localMalformedURLException1;
/*    */           }
/* 120 */           localObject1 = new URL(paramURL, paramString4);
/*    */         }
/*    */ 
/* 123 */         if (paramDebug != null) {
/* 124 */           paramDebug.println("reading password" + localObject1);
/*    */         }
/*    */ 
/* 127 */         InputStream localInputStream = null;
/*    */         try {
/* 129 */           localInputStream = ((URL)localObject1).openStream();
/* 130 */           arrayOfChar = Password.readPassword(localInputStream);
/*    */         } finally {
/* 132 */           if (localInputStream != null) {
/* 133 */             localInputStream.close();
/*    */           }
/*    */         }
/*    */       }
/*    */ 
/* 138 */       if ("NONE".equals(paramString1)) {
/* 139 */         localKeyStore1.load(null, arrayOfChar);
/* 140 */         return localKeyStore1;
/*    */       }
/*    */ 
/* 146 */       Object localObject1 = null;
/*    */       try {
/* 148 */         localObject1 = new URL(paramString1);
/*    */       }
/*    */       catch (MalformedURLException localMalformedURLException2)
/*    */       {
/* 152 */         if (paramURL == null) {
/* 153 */           throw localMalformedURLException2;
/*    */         }
/* 155 */         localObject1 = new URL(paramURL, paramString1);
/*    */       }
/*    */ 
/* 158 */       if (paramDebug != null) {
/* 159 */         paramDebug.println("reading keystore" + localObject1);
/*    */       }
/*    */ 
/* 162 */       BufferedInputStream localBufferedInputStream = null;
/*    */       try {
/* 164 */         localBufferedInputStream = new BufferedInputStream(getInputStream((URL)localObject1));
/*    */ 
/* 166 */         localKeyStore1.load(localBufferedInputStream, arrayOfChar);
/*    */       } finally {
/* 168 */         localBufferedInputStream.close();
/*    */       }
/* 170 */       return localKeyStore1;
/*    */     }
/*    */     finally {
/* 173 */       if (arrayOfChar != null)
/* 174 */         Arrays.fill(arrayOfChar, ' ');
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.util.PolicyUtil
 * JD-Core Version:    0.6.2
 */