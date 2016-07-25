/*     */ package sun.security.provider.certpath;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.security.cert.CertPath;
/*     */ import java.security.cert.Certificate;
/*     */ import java.security.cert.CertificateEncodingException;
/*     */ import java.security.cert.CertificateException;
/*     */ import java.security.cert.CertificateFactory;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import sun.security.pkcs.ContentInfo;
/*     */ import sun.security.pkcs.PKCS7;
/*     */ import sun.security.pkcs.SignerInfo;
/*     */ import sun.security.util.DerInputStream;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.DerValue;
/*     */ import sun.security.x509.AlgorithmId;
/*     */ 
/*     */ public class X509CertPath extends CertPath
/*     */ {
/*     */   private static final long serialVersionUID = 4989800333263052980L;
/*     */   private List<X509Certificate> certs;
/*     */   private static final String COUNT_ENCODING = "count";
/*     */   private static final String PKCS7_ENCODING = "PKCS7";
/*     */   private static final String PKIPATH_ENCODING = "PkiPath";
/*  90 */   private static final Collection<String> encodingList = Collections.unmodifiableCollection(localArrayList);
/*     */ 
/*     */   public X509CertPath(List<? extends Certificate> paramList)
/*     */     throws CertificateException
/*     */   {
/* 105 */     super("X.509");
/*     */ 
/* 108 */     for (Iterator localIterator = paramList.iterator(); localIterator.hasNext(); ) { Object localObject = localIterator.next();
/* 109 */       if (!(localObject instanceof X509Certificate)) {
/* 110 */         throw new CertificateException("List is not all X509Certificates: " + localObject.getClass().getName());
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 120 */     this.certs = Collections.unmodifiableList(new ArrayList(paramList));
/*     */   }
/*     */ 
/*     */   public X509CertPath(InputStream paramInputStream)
/*     */     throws CertificateException
/*     */   {
/* 133 */     this(paramInputStream, "PkiPath");
/*     */   }
/*     */ 
/*     */   public X509CertPath(InputStream paramInputStream, String paramString)
/*     */     throws CertificateException
/*     */   {
/* 148 */     super("X.509");
/*     */ 
/* 150 */     if ("PkiPath".equals(paramString))
/* 151 */       this.certs = parsePKIPATH(paramInputStream);
/* 152 */     else if ("PKCS7".equals(paramString))
/* 153 */       this.certs = parsePKCS7(paramInputStream);
/*     */     else
/* 155 */       throw new CertificateException("unsupported encoding");
/*     */   }
/*     */ 
/*     */   private static List<X509Certificate> parsePKIPATH(InputStream paramInputStream)
/*     */     throws CertificateException
/*     */   {
/* 169 */     ArrayList localArrayList = null;
/* 170 */     CertificateFactory localCertificateFactory = null;
/*     */ 
/* 172 */     if (paramInputStream == null)
/* 173 */       throw new CertificateException("input stream is null");
/*     */     Object localObject;
/*     */     try
/*     */     {
/* 177 */       DerInputStream localDerInputStream = new DerInputStream(readAllBytes(paramInputStream));
/* 178 */       localObject = localDerInputStream.getSequence(3);
/* 179 */       if (localObject.length == 0) {
/* 180 */         return Collections.emptyList();
/*     */       }
/*     */ 
/* 183 */       localCertificateFactory = CertificateFactory.getInstance("X.509");
/* 184 */       localArrayList = new ArrayList(localObject.length);
/*     */ 
/* 187 */       for (int i = localObject.length - 1; i >= 0; i--) {
/* 188 */         localArrayList.add((X509Certificate)localCertificateFactory.generateCertificate(new ByteArrayInputStream(localObject[i].toByteArray())));
/*     */       }
/*     */ 
/* 192 */       return Collections.unmodifiableList(localArrayList);
/*     */     }
/*     */     catch (IOException localIOException) {
/* 195 */       localObject = new CertificateException("IOException parsing PkiPath data: " + localIOException);
/*     */ 
/* 197 */       ((CertificateException)localObject).initCause(localIOException);
/* 198 */     }throw ((Throwable)localObject);
/*     */   }
/*     */ 
/*     */   private static List<X509Certificate> parsePKCS7(InputStream paramInputStream)
/*     */     throws CertificateException
/*     */   {
/* 214 */     if (paramInputStream == null)
/* 215 */       throw new CertificateException("input stream is null");
/*     */     Object localObject;
/*     */     try
/*     */     {
/* 219 */       if (!paramInputStream.markSupported())
/*     */       {
/* 222 */         paramInputStream = new ByteArrayInputStream(readAllBytes(paramInputStream));
/*     */       }
/* 224 */       PKCS7 localPKCS7 = new PKCS7(paramInputStream);
/*     */ 
/* 226 */       X509Certificate[] arrayOfX509Certificate = localPKCS7.getCertificates();
/*     */ 
/* 228 */       if (arrayOfX509Certificate != null) {
/* 229 */         localObject = Arrays.asList(arrayOfX509Certificate);
/*     */       }
/*     */       else
/* 232 */         localObject = new ArrayList(0);
/*     */     }
/*     */     catch (IOException localIOException) {
/* 235 */       throw new CertificateException("IOException parsing PKCS7 data: " + localIOException);
/*     */     }
/*     */ 
/* 242 */     return Collections.unmodifiableList((List)localObject);
/*     */   }
/*     */ 
/*     */   private static byte[] readAllBytes(InputStream paramInputStream)
/*     */     throws IOException
/*     */   {
/* 252 */     byte[] arrayOfByte = new byte[8192];
/* 253 */     ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream(2048);
/*     */     int i;
/* 255 */     while ((i = paramInputStream.read(arrayOfByte)) != -1) {
/* 256 */       localByteArrayOutputStream.write(arrayOfByte, 0, i);
/*     */     }
/* 258 */     return localByteArrayOutputStream.toByteArray();
/*     */   }
/*     */ 
/*     */   public byte[] getEncoded()
/*     */     throws CertificateEncodingException
/*     */   {
/* 270 */     return encodePKIPATH();
/*     */   }
/*     */ 
/*     */   private byte[] encodePKIPATH()
/*     */     throws CertificateEncodingException
/*     */   {
/* 281 */     ListIterator localListIterator = this.certs.listIterator(this.certs.size());
/*     */     Object localObject;
/*     */     try
/*     */     {
/* 283 */       DerOutputStream localDerOutputStream = new DerOutputStream();
/*     */ 
/* 286 */       while (localListIterator.hasPrevious()) {
/* 287 */         localObject = (X509Certificate)localListIterator.previous();
/*     */ 
/* 289 */         if (this.certs.lastIndexOf(localObject) != this.certs.indexOf(localObject)) {
/* 290 */           throw new CertificateEncodingException("Duplicate Certificate");
/*     */         }
/*     */ 
/* 294 */         byte[] arrayOfByte = ((X509Certificate)localObject).getEncoded();
/* 295 */         localDerOutputStream.write(arrayOfByte);
/*     */       }
/*     */ 
/* 299 */       localObject = new DerOutputStream();
/* 300 */       ((DerOutputStream)localObject).write((byte)48, localDerOutputStream);
/* 301 */       return ((DerOutputStream)localObject).toByteArray();
/*     */     }
/*     */     catch (IOException localIOException) {
/* 304 */       localObject = new CertificateEncodingException("IOException encoding PkiPath data: " + localIOException);
/*     */ 
/* 306 */       ((CertificateEncodingException)localObject).initCause(localIOException);
/* 307 */     }throw ((Throwable)localObject);
/*     */   }
/*     */ 
/*     */   private byte[] encodePKCS7()
/*     */     throws CertificateEncodingException
/*     */   {
/* 318 */     PKCS7 localPKCS7 = new PKCS7(new AlgorithmId[0], new ContentInfo(ContentInfo.DATA_OID, null), (X509Certificate[])this.certs.toArray(new X509Certificate[this.certs.size()]), new SignerInfo[0]);
/*     */ 
/* 322 */     DerOutputStream localDerOutputStream = new DerOutputStream();
/*     */     try {
/* 324 */       localPKCS7.encodeSignedData(localDerOutputStream);
/*     */     } catch (IOException localIOException) {
/* 326 */       throw new CertificateEncodingException(localIOException.getMessage());
/*     */     }
/* 328 */     return localDerOutputStream.toByteArray();
/*     */   }
/*     */ 
/*     */   public byte[] getEncoded(String paramString)
/*     */     throws CertificateEncodingException
/*     */   {
/* 342 */     if ("PkiPath".equals(paramString))
/* 343 */       return encodePKIPATH();
/* 344 */     if ("PKCS7".equals(paramString)) {
/* 345 */       return encodePKCS7();
/*     */     }
/* 347 */     throw new CertificateEncodingException("unsupported encoding");
/*     */   }
/*     */ 
/*     */   public static Iterator<String> getEncodingsStatic()
/*     */   {
/* 359 */     return encodingList.iterator();
/*     */   }
/*     */ 
/*     */   public Iterator<String> getEncodings()
/*     */   {
/* 374 */     return getEncodingsStatic();
/*     */   }
/*     */ 
/*     */   public List<X509Certificate> getCertificates()
/*     */   {
/* 385 */     return this.certs;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  87 */     ArrayList localArrayList = new ArrayList(2);
/*  88 */     localArrayList.add("PkiPath");
/*  89 */     localArrayList.add("PKCS7");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.provider.certpath.X509CertPath
 * JD-Core Version:    0.6.2
 */