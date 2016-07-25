/*      */ package sun.security.tools;
/*      */ 
/*      */ import java.io.BufferedReader;
/*      */ import java.io.ByteArrayInputStream;
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.io.File;
/*      */ import java.io.FileInputStream;
/*      */ import java.io.FileNotFoundException;
/*      */ import java.io.FileOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.InputStreamReader;
/*      */ import java.io.OutputStream;
/*      */ import java.io.PrintStream;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.math.BigInteger;
/*      */ import java.net.MalformedURLException;
/*      */ import java.net.Socket;
/*      */ import java.net.URI;
/*      */ import java.net.URL;
/*      */ import java.net.URLClassLoader;
/*      */ import java.net.URLConnection;
/*      */ import java.security.CodeSigner;
/*      */ import java.security.Key;
/*      */ import java.security.KeyStore;
/*      */ import java.security.KeyStore.Entry;
/*      */ import java.security.KeyStore.PasswordProtection;
/*      */ import java.security.KeyStore.PrivateKeyEntry;
/*      */ import java.security.KeyStore.ProtectionParameter;
/*      */ import java.security.KeyStore.SecretKeyEntry;
/*      */ import java.security.KeyStore.TrustedCertificateEntry;
/*      */ import java.security.KeyStoreException;
/*      */ import java.security.MessageDigest;
/*      */ import java.security.Principal;
/*      */ import java.security.PrivateKey;
/*      */ import java.security.Provider;
/*      */ import java.security.PublicKey;
/*      */ import java.security.Security;
/*      */ import java.security.Signature;
/*      */ import java.security.Timestamp;
/*      */ import java.security.UnrecoverableEntryException;
/*      */ import java.security.UnrecoverableKeyException;
/*      */ import java.security.cert.CRL;
/*      */ import java.security.cert.CertPath;
/*      */ import java.security.cert.CertStore;
/*      */ import java.security.cert.Certificate;
/*      */ import java.security.cert.CertificateException;
/*      */ import java.security.cert.CertificateFactory;
/*      */ import java.security.cert.X509CRL;
/*      */ import java.security.cert.X509CRLEntry;
/*      */ import java.security.cert.X509CRLSelector;
/*      */ import java.security.cert.X509Certificate;
/*      */ import java.text.Collator;
/*      */ import java.text.MessageFormat;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Calendar;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Date;
/*      */ import java.util.Enumeration;
/*      */ import java.util.GregorianCalendar;
/*      */ import java.util.HashSet;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.Random;
/*      */ import java.util.ResourceBundle;
/*      */ import java.util.Set;
/*      */ import java.util.Vector;
/*      */ import java.util.jar.JarEntry;
/*      */ import java.util.jar.JarFile;
/*      */ import javax.crypto.KeyGenerator;
/*      */ import javax.net.ssl.HostnameVerifier;
/*      */ import javax.net.ssl.HttpsURLConnection;
/*      */ import javax.net.ssl.SSLContext;
/*      */ import javax.net.ssl.SSLEngine;
/*      */ import javax.net.ssl.SSLSession;
/*      */ import javax.net.ssl.TrustManager;
/*      */ import javax.net.ssl.X509ExtendedTrustManager;
/*      */ import javax.security.auth.x500.X500Principal;
/*      */ import sun.misc.BASE64Decoder;
/*      */ import sun.misc.BASE64Encoder;
/*      */ import sun.misc.HexDumpEncoder;
/*      */ import sun.security.pkcs.PKCS10;
/*      */ import sun.security.pkcs.PKCS10Attribute;
/*      */ import sun.security.pkcs.PKCS10Attributes;
/*      */ import sun.security.pkcs.PKCS9Attribute;
/*      */ import sun.security.provider.certpath.ldap.LDAPCertStoreHelper;
/*      */ import sun.security.util.DerValue;
/*      */ import sun.security.util.ObjectIdentifier;
/*      */ import sun.security.util.Password;
/*      */ import sun.security.util.PathList;
/*      */ import sun.security.x509.AccessDescription;
/*      */ import sun.security.x509.AlgorithmId;
/*      */ import sun.security.x509.AuthorityInfoAccessExtension;
/*      */ import sun.security.x509.AuthorityKeyIdentifierExtension;
/*      */ import sun.security.x509.BasicConstraintsExtension;
/*      */ import sun.security.x509.CRLDistributionPointsExtension;
/*      */ import sun.security.x509.CRLExtensions;
/*      */ import sun.security.x509.CRLReasonCodeExtension;
/*      */ import sun.security.x509.CertAndKeyGen;
/*      */ import sun.security.x509.CertificateAlgorithmId;
/*      */ import sun.security.x509.CertificateExtensions;
/*      */ import sun.security.x509.CertificateIssuerName;
/*      */ import sun.security.x509.CertificateSerialNumber;
/*      */ import sun.security.x509.CertificateSubjectName;
/*      */ import sun.security.x509.CertificateValidity;
/*      */ import sun.security.x509.CertificateVersion;
/*      */ import sun.security.x509.CertificateX509Key;
/*      */ import sun.security.x509.DNSName;
/*      */ import sun.security.x509.DistributionPoint;
/*      */ import sun.security.x509.ExtendedKeyUsageExtension;
/*      */ import sun.security.x509.Extension;
/*      */ import sun.security.x509.GeneralName;
/*      */ import sun.security.x509.GeneralNameInterface;
/*      */ import sun.security.x509.GeneralNames;
/*      */ import sun.security.x509.IPAddressName;
/*      */ import sun.security.x509.IssuerAlternativeNameExtension;
/*      */ import sun.security.x509.KeyIdentifier;
/*      */ import sun.security.x509.KeyUsageExtension;
/*      */ import sun.security.x509.OIDName;
/*      */ import sun.security.x509.PKIXExtensions;
/*      */ import sun.security.x509.RFC822Name;
/*      */ import sun.security.x509.SubjectAlternativeNameExtension;
/*      */ import sun.security.x509.SubjectInfoAccessExtension;
/*      */ import sun.security.x509.SubjectKeyIdentifierExtension;
/*      */ import sun.security.x509.URIName;
/*      */ import sun.security.x509.X500Name;
/*      */ import sun.security.x509.X509CRLEntryImpl;
/*      */ import sun.security.x509.X509CRLImpl;
/*      */ import sun.security.x509.X509CertImpl;
/*      */ import sun.security.x509.X509CertInfo;
/*      */ 
/*      */ public final class KeyTool
/*      */ {
/*  106 */   private boolean debug = false;
/*  107 */   private Command command = null;
/*  108 */   private String sigAlgName = null;
/*  109 */   private String keyAlgName = null;
/*  110 */   private boolean verbose = false;
/*  111 */   private int keysize = -1;
/*  112 */   private boolean rfc = false;
/*  113 */   private long validity = 90L;
/*  114 */   private String alias = null;
/*  115 */   private String dname = null;
/*  116 */   private String dest = null;
/*  117 */   private String filename = null;
/*  118 */   private String infilename = null;
/*  119 */   private String outfilename = null;
/*  120 */   private String srcksfname = null;
/*      */ 
/*  128 */   private Set<Pair<String, String>> providers = null;
/*  129 */   private String storetype = null;
/*  130 */   private String srcProviderName = null;
/*  131 */   private String providerName = null;
/*  132 */   private String pathlist = null;
/*  133 */   private char[] storePass = null;
/*  134 */   private char[] storePassNew = null;
/*  135 */   private char[] keyPass = null;
/*  136 */   private char[] keyPassNew = null;
/*  137 */   private char[] newPass = null;
/*  138 */   private char[] destKeyPass = null;
/*  139 */   private char[] srckeyPass = null;
/*  140 */   private String ksfname = null;
/*  141 */   private File ksfile = null;
/*  142 */   private InputStream ksStream = null;
/*  143 */   private String sslserver = null;
/*  144 */   private String jarfile = null;
/*  145 */   private KeyStore keyStore = null;
/*  146 */   private boolean token = false;
/*  147 */   private boolean nullStream = false;
/*  148 */   private boolean kssave = false;
/*  149 */   private boolean noprompt = false;
/*  150 */   private boolean trustcacerts = false;
/*  151 */   private boolean protectedPath = false;
/*  152 */   private boolean srcprotectedPath = false;
/*  153 */   private CertificateFactory cf = null;
/*  154 */   private KeyStore caks = null;
/*  155 */   private char[] srcstorePass = null;
/*  156 */   private String srcstoretype = null;
/*  157 */   private Set<char[]> passwords = new HashSet();
/*  158 */   private String startDate = null;
/*      */ 
/*  160 */   private List<String> ids = new ArrayList();
/*  161 */   private List<String> v3ext = new ArrayList();
/*      */ 
/*  312 */   private static final Class[] PARAM_STRING = { String.class };
/*      */   private static final String JKS = "jks";
/*      */   private static final String NONE = "NONE";
/*      */   private static final String P11KEYSTORE = "PKCS11";
/*      */   private static final String P12KEYSTORE = "PKCS12";
/*  318 */   private final String keyAlias = "mykey";
/*      */ 
/*  321 */   private static final ResourceBundle rb = ResourceBundle.getBundle("sun.security.util.Resources");
/*      */ 
/*  323 */   private static final Collator collator = Collator.getInstance();
/*      */ 
/* 3687 */   private static final String[] extSupported = { "BasicConstraints", "KeyUsage", "ExtendedKeyUsage", "SubjectAlternativeName", "IssuerAlternativeName", "SubjectInfoAccess", "AuthorityInfoAccess", null, "CRLDistributionPoints" };
/*      */ 
/*      */   public static void main(String[] paramArrayOfString)
/*      */     throws Exception
/*      */   {
/*  332 */     KeyTool localKeyTool = new KeyTool();
/*  333 */     localKeyTool.run(paramArrayOfString, System.out);
/*      */   }
/*      */ 
/*      */   private void run(String[] paramArrayOfString, PrintStream paramPrintStream) throws Exception {
/*      */     try {
/*  338 */       parseArgs(paramArrayOfString);
/*  339 */       if (this.command != null)
/*  340 */         doCommands(paramPrintStream);
/*      */     }
/*      */     catch (Exception localException)
/*      */     {
/*      */       Iterator localIterator1;
/*  343 */       System.out.println(rb.getString("keytool.error.") + localException);
/*  344 */       if (this.verbose) {
/*  345 */         localException.printStackTrace(System.out);
/*      */       }
/*  347 */       if (!this.debug)
/*  348 */         System.exit(1);
/*      */       else
/*  350 */         throw localException;
/*      */     }
/*      */     finally
/*      */     {
/*      */       char[] arrayOfChar1;
/*      */       Iterator localIterator2;
/*  353 */       for (char[] arrayOfChar2 : this.passwords) {
/*  354 */         if (arrayOfChar2 != null) {
/*  355 */           Arrays.fill(arrayOfChar2, ' ');
/*  356 */           arrayOfChar2 = null;
/*      */         }
/*      */       }
/*      */ 
/*  360 */       if (this.ksStream != null)
/*  361 */         this.ksStream.close();
/*      */     }
/*      */   }
/*      */ 
/*      */   void parseArgs(String[] paramArrayOfString)
/*      */   {
/*  371 */     int i = 0;
/*  372 */     int j = paramArrayOfString.length == 0 ? 1 : 0;
/*      */ 
/*  374 */     for (i = 0; (i < paramArrayOfString.length) && (paramArrayOfString[i].startsWith("-")); i++)
/*      */     {
/*  376 */       String str1 = paramArrayOfString[i];
/*      */       Object localObject2;
/*  379 */       if (i == paramArrayOfString.length - 1) {
/*  380 */         for (localObject2 : Option.values())
/*      */         {
/*  382 */           if (collator.compare(str1, ((Option)localObject2).toString()) == 0) {
/*  383 */             if (((Option)localObject2).arg == null) break; errorNeedArgument(str1); break;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  392 */       ??? = null;
/*  393 */       ??? = str1.indexOf(':');
/*  394 */       if (??? > 0) {
/*  395 */         ??? = str1.substring(??? + 1);
/*  396 */         str1 = str1.substring(0, ???);
/*      */       }
/*      */ 
/*  401 */       ??? = 0;
/*  402 */       for (Object localObject3 : Command.values()) {
/*  403 */         if (collator.compare(str1, localObject3.toString()) == 0) {
/*  404 */           this.command = localObject3;
/*  405 */           ??? = 1;
/*  406 */           break;
/*      */         }
/*      */       }
/*      */ 
/*  410 */       if (??? == 0)
/*      */       {
/*  412 */         if (collator.compare(str1, "-export") == 0) {
/*  413 */           this.command = Command.EXPORTCERT;
/*  414 */         } else if (collator.compare(str1, "-genkey") == 0) {
/*  415 */           this.command = Command.GENKEYPAIR;
/*  416 */         } else if (collator.compare(str1, "-import") == 0) {
/*  417 */           this.command = Command.IMPORTCERT;
/*      */         }
/*  422 */         else if (collator.compare(str1, "-help") == 0) {
/*  423 */           j = 1;
/*      */         }
/*  429 */         else if ((collator.compare(str1, "-keystore") == 0) || (collator.compare(str1, "-destkeystore") == 0))
/*      */         {
/*  431 */           this.ksfname = paramArrayOfString[(++i)];
/*  432 */         } else if ((collator.compare(str1, "-storepass") == 0) || (collator.compare(str1, "-deststorepass") == 0))
/*      */         {
/*  434 */           this.storePass = getPass((String)???, paramArrayOfString[(++i)]);
/*  435 */           this.passwords.add(this.storePass);
/*  436 */         } else if ((collator.compare(str1, "-storetype") == 0) || (collator.compare(str1, "-deststoretype") == 0))
/*      */         {
/*  438 */           this.storetype = paramArrayOfString[(++i)];
/*  439 */         } else if (collator.compare(str1, "-srcstorepass") == 0) {
/*  440 */           this.srcstorePass = getPass((String)???, paramArrayOfString[(++i)]);
/*  441 */           this.passwords.add(this.srcstorePass);
/*  442 */         } else if (collator.compare(str1, "-srcstoretype") == 0) {
/*  443 */           this.srcstoretype = paramArrayOfString[(++i)];
/*  444 */         } else if (collator.compare(str1, "-srckeypass") == 0) {
/*  445 */           this.srckeyPass = getPass((String)???, paramArrayOfString[(++i)]);
/*  446 */           this.passwords.add(this.srckeyPass);
/*  447 */         } else if (collator.compare(str1, "-srcprovidername") == 0) {
/*  448 */           this.srcProviderName = paramArrayOfString[(++i)];
/*  449 */         } else if ((collator.compare(str1, "-providername") == 0) || (collator.compare(str1, "-destprovidername") == 0))
/*      */         {
/*  451 */           this.providerName = paramArrayOfString[(++i)];
/*  452 */         } else if (collator.compare(str1, "-providerpath") == 0) {
/*  453 */           this.pathlist = paramArrayOfString[(++i)];
/*  454 */         } else if (collator.compare(str1, "-keypass") == 0) {
/*  455 */           this.keyPass = getPass((String)???, paramArrayOfString[(++i)]);
/*  456 */           this.passwords.add(this.keyPass);
/*  457 */         } else if (collator.compare(str1, "-new") == 0) {
/*  458 */           this.newPass = getPass((String)???, paramArrayOfString[(++i)]);
/*  459 */           this.passwords.add(this.newPass);
/*  460 */         } else if (collator.compare(str1, "-destkeypass") == 0) {
/*  461 */           this.destKeyPass = getPass((String)???, paramArrayOfString[(++i)]);
/*  462 */           this.passwords.add(this.destKeyPass);
/*  463 */         } else if ((collator.compare(str1, "-alias") == 0) || (collator.compare(str1, "-srcalias") == 0))
/*      */         {
/*  465 */           this.alias = paramArrayOfString[(++i)];
/*  466 */         } else if ((collator.compare(str1, "-dest") == 0) || (collator.compare(str1, "-destalias") == 0))
/*      */         {
/*  468 */           this.dest = paramArrayOfString[(++i)];
/*  469 */         } else if (collator.compare(str1, "-dname") == 0) {
/*  470 */           this.dname = paramArrayOfString[(++i)];
/*  471 */         } else if (collator.compare(str1, "-keysize") == 0) {
/*  472 */           this.keysize = Integer.parseInt(paramArrayOfString[(++i)]);
/*  473 */         } else if (collator.compare(str1, "-keyalg") == 0) {
/*  474 */           this.keyAlgName = paramArrayOfString[(++i)];
/*  475 */         } else if (collator.compare(str1, "-sigalg") == 0) {
/*  476 */           this.sigAlgName = paramArrayOfString[(++i)];
/*  477 */         } else if (collator.compare(str1, "-startdate") == 0) {
/*  478 */           this.startDate = paramArrayOfString[(++i)];
/*  479 */         } else if (collator.compare(str1, "-validity") == 0) {
/*  480 */           this.validity = Long.parseLong(paramArrayOfString[(++i)]);
/*  481 */         } else if (collator.compare(str1, "-ext") == 0) {
/*  482 */           this.v3ext.add(paramArrayOfString[(++i)]);
/*  483 */         } else if (collator.compare(str1, "-id") == 0) {
/*  484 */           this.ids.add(paramArrayOfString[(++i)]);
/*  485 */         } else if (collator.compare(str1, "-file") == 0) {
/*  486 */           this.filename = paramArrayOfString[(++i)];
/*  487 */         } else if (collator.compare(str1, "-infile") == 0) {
/*  488 */           this.infilename = paramArrayOfString[(++i)];
/*  489 */         } else if (collator.compare(str1, "-outfile") == 0) {
/*  490 */           this.outfilename = paramArrayOfString[(++i)];
/*  491 */         } else if (collator.compare(str1, "-sslserver") == 0) {
/*  492 */           this.sslserver = paramArrayOfString[(++i)];
/*  493 */         } else if (collator.compare(str1, "-jarfile") == 0) {
/*  494 */           this.jarfile = paramArrayOfString[(++i)];
/*  495 */         } else if (collator.compare(str1, "-srckeystore") == 0) {
/*  496 */           this.srcksfname = paramArrayOfString[(++i)];
/*  497 */         } else if ((collator.compare(str1, "-provider") == 0) || (collator.compare(str1, "-providerclass") == 0))
/*      */         {
/*  499 */           if (this.providers == null) {
/*  500 */             this.providers = new HashSet(3);
/*      */           }
/*  502 */           localObject2 = paramArrayOfString[(++i)];
/*  503 */           String str2 = null;
/*      */ 
/*  505 */           if (paramArrayOfString.length > i + 1) {
/*  506 */             str1 = paramArrayOfString[(i + 1)];
/*  507 */             if (collator.compare(str1, "-providerarg") == 0) {
/*  508 */               if (paramArrayOfString.length == i + 2) errorNeedArgument(str1);
/*  509 */               str2 = paramArrayOfString[(i + 2)];
/*  510 */               i += 2;
/*      */             }
/*      */           }
/*  513 */           this.providers.add(Pair.of(localObject2, str2));
/*      */         }
/*  520 */         else if (collator.compare(str1, "-v") == 0) {
/*  521 */           this.verbose = true;
/*  522 */         } else if (collator.compare(str1, "-debug") == 0) {
/*  523 */           this.debug = true;
/*  524 */         } else if (collator.compare(str1, "-rfc") == 0) {
/*  525 */           this.rfc = true;
/*  526 */         } else if (collator.compare(str1, "-noprompt") == 0) {
/*  527 */           this.noprompt = true;
/*  528 */         } else if (collator.compare(str1, "-trustcacerts") == 0) {
/*  529 */           this.trustcacerts = true;
/*  530 */         } else if ((collator.compare(str1, "-protected") == 0) || (collator.compare(str1, "-destprotected") == 0))
/*      */         {
/*  532 */           this.protectedPath = true;
/*  533 */         } else if (collator.compare(str1, "-srcprotected") == 0) {
/*  534 */           this.srcprotectedPath = true;
/*      */         } else {
/*  536 */           System.err.println(rb.getString("Illegal.option.") + str1);
/*  537 */           tinyHelp();
/*      */         }
/*      */       }
/*      */     }
/*  541 */     if (i < paramArrayOfString.length) {
/*  542 */       System.err.println(rb.getString("Illegal.option.") + paramArrayOfString[i]);
/*  543 */       tinyHelp();
/*      */     }
/*      */ 
/*  546 */     if (this.command == null) {
/*  547 */       if (j != 0) {
/*  548 */         usage();
/*      */       } else {
/*  550 */         System.err.println(rb.getString("Usage.error.no.command.provided"));
/*  551 */         tinyHelp();
/*      */       }
/*  553 */     } else if (j != 0) {
/*  554 */       usage();
/*  555 */       this.command = null;
/*      */     }
/*      */   }
/*      */ 
/*      */   boolean isKeyStoreRelated(Command paramCommand) {
/*  560 */     return (paramCommand != Command.PRINTCERT) && (paramCommand != Command.PRINTCERTREQ);
/*      */   }
/*      */ 
/*      */   void doCommands(PrintStream paramPrintStream)
/*      */     throws Exception
/*      */   {
/*  568 */     if (this.storetype == null) {
/*  569 */       this.storetype = KeyStore.getDefaultType();
/*      */     }
/*  571 */     this.storetype = KeyStoreUtil.niceStoreTypeName(this.storetype);
/*      */ 
/*  573 */     if (this.srcstoretype == null) {
/*  574 */       this.srcstoretype = KeyStore.getDefaultType();
/*      */     }
/*  576 */     this.srcstoretype = KeyStoreUtil.niceStoreTypeName(this.srcstoretype);
/*      */ 
/*  578 */     if (("PKCS11".equalsIgnoreCase(this.storetype)) || (KeyStoreUtil.isWindowsKeyStore(this.storetype)))
/*      */     {
/*  580 */       this.token = true;
/*  581 */       if (this.ksfname == null) {
/*  582 */         this.ksfname = "NONE";
/*      */       }
/*      */     }
/*  585 */     if ("NONE".equals(this.ksfname)) {
/*  586 */       this.nullStream = true;
/*      */     }
/*      */ 
/*  589 */     if ((this.token) && (!this.nullStream)) {
/*  590 */       System.err.println(MessageFormat.format(rb.getString(".keystore.must.be.NONE.if.storetype.is.{0}"), new Object[] { this.storetype }));
/*      */ 
/*  592 */       System.err.println();
/*  593 */       tinyHelp();
/*      */     }
/*      */ 
/*  596 */     if ((this.token) && ((this.command == Command.KEYPASSWD) || (this.command == Command.STOREPASSWD)))
/*      */     {
/*  598 */       throw new UnsupportedOperationException(MessageFormat.format(rb.getString(".storepasswd.and.keypasswd.commands.not.supported.if.storetype.is.{0}"), new Object[] { this.storetype }));
/*      */     }
/*      */ 
/*  602 */     if (("PKCS12".equalsIgnoreCase(this.storetype)) && (this.command == Command.KEYPASSWD)) {
/*  603 */       throw new UnsupportedOperationException(rb.getString(".keypasswd.commands.not.supported.if.storetype.is.PKCS12"));
/*      */     }
/*      */ 
/*  607 */     if ((this.token) && ((this.keyPass != null) || (this.newPass != null) || (this.destKeyPass != null))) {
/*  608 */       throw new IllegalArgumentException(MessageFormat.format(rb.getString(".keypass.and.new.can.not.be.specified.if.storetype.is.{0}"), new Object[] { this.storetype }));
/*      */     }
/*      */ 
/*  612 */     if ((this.protectedPath) && (
/*  613 */       (this.storePass != null) || (this.keyPass != null) || (this.newPass != null) || (this.destKeyPass != null)))
/*      */     {
/*  615 */       throw new IllegalArgumentException(rb.getString("if.protected.is.specified.then.storepass.keypass.and.new.must.not.be.specified"));
/*      */     }
/*      */ 
/*  620 */     if ((this.srcprotectedPath) && (
/*  621 */       (this.srcstorePass != null) || (this.srckeyPass != null))) {
/*  622 */       throw new IllegalArgumentException(rb.getString("if.srcprotected.is.specified.then.srcstorepass.and.srckeypass.must.not.be.specified"));
/*      */     }
/*      */ 
/*  627 */     if ((KeyStoreUtil.isWindowsKeyStore(this.storetype)) && (
/*  628 */       (this.storePass != null) || (this.keyPass != null) || (this.newPass != null) || (this.destKeyPass != null)))
/*      */     {
/*  630 */       throw new IllegalArgumentException(rb.getString("if.keystore.is.not.password.protected.then.storepass.keypass.and.new.must.not.be.specified"));
/*      */     }
/*      */ 
/*  635 */     if ((KeyStoreUtil.isWindowsKeyStore(this.srcstoretype)) && (
/*  636 */       (this.srcstorePass != null) || (this.srckeyPass != null))) {
/*  637 */       throw new IllegalArgumentException(rb.getString("if.source.keystore.is.not.password.protected.then.srcstorepass.and.srckeypass.must.not.be.specified"));
/*      */     }
/*      */ 
/*  642 */     if (this.validity <= 0L)
/*  643 */       throw new Exception(rb.getString("Validity.must.be.greater.than.zero"));
/*      */     Object localObject1;
/*      */     Object localObject3;
/*      */     Object localObject4;
/*  648 */     if (this.providers != null) {
/*  649 */       localObject1 = null;
/*  650 */       if (this.pathlist != null) {
/*  651 */         localObject3 = null;
/*  652 */         localObject3 = PathList.appendPath((String)localObject3, System.getProperty("java.class.path"));
/*      */ 
/*  654 */         localObject3 = PathList.appendPath((String)localObject3, System.getProperty("env.class.path"));
/*      */ 
/*  656 */         localObject3 = PathList.appendPath((String)localObject3, this.pathlist);
/*      */ 
/*  658 */         localObject4 = PathList.pathToURLs((String)localObject3);
/*  659 */         localObject1 = new URLClassLoader((URL[])localObject4);
/*      */       } else {
/*  661 */         localObject1 = ClassLoader.getSystemClassLoader();
/*      */       }
/*      */ 
/*  664 */       for (localObject3 = this.providers.iterator(); ((Iterator)localObject3).hasNext(); ) { localObject4 = (Pair)((Iterator)localObject3).next();
/*  665 */         localObject5 = (String)((Pair)localObject4).fst;
/*      */         Class localClass;
/*  667 */         if (localObject1 != null)
/*  668 */           localClass = ((ClassLoader)localObject1).loadClass((String)localObject5);
/*      */         else {
/*  670 */           localClass = Class.forName((String)localObject5);
/*      */         }
/*      */ 
/*  673 */         String str = (String)((Pair)localObject4).snd;
/*      */         Object localObject6;
/*      */         Object localObject7;
/*  675 */         if (str == null) {
/*  676 */           localObject6 = localClass.newInstance();
/*      */         } else {
/*  678 */           localObject7 = localClass.getConstructor(PARAM_STRING);
/*  679 */           localObject6 = ((Constructor)localObject7).newInstance(new Object[] { str });
/*      */         }
/*  681 */         if (!(localObject6 instanceof Provider)) {
/*  682 */           localObject7 = new MessageFormat(rb.getString("provName.not.a.provider"));
/*      */ 
/*  684 */           Object[] arrayOfObject = { localObject5 };
/*  685 */           throw new Exception(((MessageFormat)localObject7).format(arrayOfObject));
/*      */         }
/*  687 */         Security.addProvider((Provider)localObject6);
/*      */       }
/*      */     }
/*      */     Object localObject5;
/*  691 */     if ((this.command == Command.LIST) && (this.verbose) && (this.rfc)) {
/*  692 */       System.err.println(rb.getString("Must.not.specify.both.v.and.rfc.with.list.command"));
/*      */ 
/*  694 */       tinyHelp();
/*      */     }
/*      */ 
/*  698 */     if ((this.command == Command.GENKEYPAIR) && (this.keyPass != null) && (this.keyPass.length < 6)) {
/*  699 */       throw new Exception(rb.getString("Key.password.must.be.at.least.6.characters"));
/*      */     }
/*      */ 
/*  702 */     if ((this.newPass != null) && (this.newPass.length < 6)) {
/*  703 */       throw new Exception(rb.getString("New.password.must.be.at.least.6.characters"));
/*      */     }
/*      */ 
/*  706 */     if ((this.destKeyPass != null) && (this.destKeyPass.length < 6)) {
/*  707 */       throw new Exception(rb.getString("New.password.must.be.at.least.6.characters"));
/*      */     }
/*      */ 
/*  716 */     if (isKeyStoreRelated(this.command)) {
/*  717 */       if (this.ksfname == null) {
/*  718 */         this.ksfname = (System.getProperty("user.home") + File.separator + ".keystore");
/*      */       }
/*      */ 
/*  722 */       if (!this.nullStream) {
/*      */         try {
/*  724 */           this.ksfile = new File(this.ksfname);
/*      */ 
/*  726 */           if ((this.ksfile.exists()) && (this.ksfile.length() == 0L)) {
/*  727 */             throw new Exception(rb.getString("Keystore.file.exists.but.is.empty.") + this.ksfname);
/*      */           }
/*      */ 
/*  730 */           this.ksStream = new FileInputStream(this.ksfile);
/*      */         } catch (FileNotFoundException localFileNotFoundException) {
/*  732 */           if ((this.command != Command.GENKEYPAIR) && (this.command != Command.GENSECKEY) && (this.command != Command.IDENTITYDB) && (this.command != Command.IMPORTCERT) && (this.command != Command.IMPORTKEYSTORE) && (this.command != Command.PRINTCRL))
/*      */           {
/*  738 */             throw new Exception(rb.getString("Keystore.file.does.not.exist.") + this.ksfname);
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  745 */     if (((this.command == Command.KEYCLONE) || (this.command == Command.CHANGEALIAS)) && (this.dest == null))
/*      */     {
/*  747 */       this.dest = getAlias("destination");
/*  748 */       if ("".equals(this.dest)) {
/*  749 */         throw new Exception(rb.getString("Must.specify.destination.alias"));
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  754 */     if ((this.command == Command.DELETE) && (this.alias == null)) {
/*  755 */       this.alias = getAlias(null);
/*  756 */       if ("".equals(this.alias)) {
/*  757 */         throw new Exception(rb.getString("Must.specify.alias"));
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  762 */     if (this.providerName == null)
/*  763 */       this.keyStore = KeyStore.getInstance(this.storetype);
/*      */     else {
/*  765 */       this.keyStore = KeyStore.getInstance(this.storetype, this.providerName);
/*      */     }
/*      */ 
/*  788 */     if (!this.nullStream) {
/*  789 */       this.keyStore.load(this.ksStream, this.storePass);
/*  790 */       if (this.ksStream != null) {
/*  791 */         this.ksStream.close();
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  798 */     if ((this.nullStream) && (this.storePass != null)) {
/*  799 */       this.keyStore.load(null, this.storePass);
/*  800 */     } else if ((!this.nullStream) && (this.storePass != null))
/*      */     {
/*  803 */       if ((this.ksStream == null) && (this.storePass.length < 6)) {
/*  804 */         throw new Exception(rb.getString("Keystore.password.must.be.at.least.6.characters"));
/*      */       }
/*      */     }
/*  807 */     else if (this.storePass == null)
/*      */     {
/*  811 */       if ((!this.protectedPath) && (!KeyStoreUtil.isWindowsKeyStore(this.storetype)) && ((this.command == Command.CERTREQ) || (this.command == Command.DELETE) || (this.command == Command.GENKEYPAIR) || (this.command == Command.GENSECKEY) || (this.command == Command.IMPORTCERT) || (this.command == Command.IMPORTKEYSTORE) || (this.command == Command.KEYCLONE) || (this.command == Command.CHANGEALIAS) || (this.command == Command.SELFCERT) || (this.command == Command.STOREPASSWD) || (this.command == Command.KEYPASSWD) || (this.command == Command.IDENTITYDB)))
/*      */       {
/*  824 */         int i = 0;
/*      */         do {
/*  826 */           if (this.command == Command.IMPORTKEYSTORE) {
/*  827 */             System.err.print(rb.getString("Enter.destination.keystore.password."));
/*      */           }
/*      */           else {
/*  830 */             System.err.print(rb.getString("Enter.keystore.password."));
/*      */           }
/*      */ 
/*  833 */           System.err.flush();
/*  834 */           this.storePass = Password.readPassword(System.in);
/*  835 */           this.passwords.add(this.storePass);
/*      */ 
/*  839 */           if ((!this.nullStream) && ((this.storePass == null) || (this.storePass.length < 6))) {
/*  840 */             System.err.println(rb.getString("Keystore.password.is.too.short.must.be.at.least.6.characters"));
/*      */ 
/*  842 */             this.storePass = null;
/*      */           }
/*      */ 
/*  847 */           if ((this.storePass != null) && (!this.nullStream) && (this.ksStream == null)) {
/*  848 */             System.err.print(rb.getString("Re.enter.new.password."));
/*  849 */             localObject3 = Password.readPassword(System.in);
/*  850 */             this.passwords.add(localObject3);
/*  851 */             if (!Arrays.equals(this.storePass, (char[])localObject3)) {
/*  852 */               System.err.println(rb.getString("They.don.t.match.Try.again"));
/*      */ 
/*  854 */               this.storePass = null;
/*      */             }
/*      */           }
/*      */ 
/*  858 */           i++;
/*  859 */         }while ((this.storePass == null) && (i < 3));
/*      */ 
/*  862 */         if (this.storePass == null) {
/*  863 */           System.err.println(rb.getString("Too.many.failures.try.later"));
/*      */ 
/*  865 */           return;
/*      */         }
/*  867 */       } else if ((!this.protectedPath) && (!KeyStoreUtil.isWindowsKeyStore(this.storetype)) && (isKeyStoreRelated(this.command)))
/*      */       {
/*  871 */         if (this.command != Command.PRINTCRL) {
/*  872 */           System.err.print(rb.getString("Enter.keystore.password."));
/*  873 */           System.err.flush();
/*  874 */           this.storePass = Password.readPassword(System.in);
/*  875 */           this.passwords.add(this.storePass);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  881 */       if (this.nullStream) {
/*  882 */         this.keyStore.load(null, this.storePass);
/*  883 */       } else if (this.ksStream != null) {
/*  884 */         this.ksStream = new FileInputStream(this.ksfile);
/*  885 */         this.keyStore.load(this.ksStream, this.storePass);
/*  886 */         this.ksStream.close();
/*      */       }
/*      */     }
/*      */     Object localObject2;
/*  890 */     if ((this.storePass != null) && ("PKCS12".equalsIgnoreCase(this.storetype))) {
/*  891 */       localObject2 = new MessageFormat(rb.getString("Warning.Different.store.and.key.passwords.not.supported.for.PKCS12.KeyStores.Ignoring.user.specified.command.value."));
/*      */ 
/*  893 */       if ((this.keyPass != null) && (!Arrays.equals(this.storePass, this.keyPass))) {
/*  894 */         localObject3 = new Object[] { "-keypass" };
/*  895 */         System.err.println(((MessageFormat)localObject2).format(localObject3));
/*  896 */         this.keyPass = this.storePass;
/*      */       }
/*  898 */       if ((this.newPass != null) && (!Arrays.equals(this.storePass, this.newPass))) {
/*  899 */         localObject3 = new Object[] { "-new" };
/*  900 */         System.err.println(((MessageFormat)localObject2).format(localObject3));
/*  901 */         this.newPass = this.storePass;
/*      */       }
/*  903 */       if ((this.destKeyPass != null) && (!Arrays.equals(this.storePass, this.destKeyPass))) {
/*  904 */         localObject3 = new Object[] { "-destkeypass" };
/*  905 */         System.err.println(((MessageFormat)localObject2).format(localObject3));
/*  906 */         this.destKeyPass = this.storePass;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  911 */     if ((this.command == Command.PRINTCERT) || (this.command == Command.IMPORTCERT) || (this.command == Command.IDENTITYDB) || (this.command == Command.PRINTCRL))
/*      */     {
/*  913 */       this.cf = CertificateFactory.getInstance("X509");
/*      */     }
/*      */ 
/*  916 */     if (this.trustcacerts) {
/*  917 */       this.caks = getCacertsKeyStore();
/*      */     }
/*      */ 
/*  921 */     if (this.command == Command.CERTREQ) {
/*  922 */       localObject2 = null;
/*  923 */       if (this.filename != null) {
/*  924 */         localObject2 = new PrintStream(new FileOutputStream(this.filename));
/*      */ 
/*  926 */         paramPrintStream = (PrintStream)localObject2;
/*      */       }
/*      */       try {
/*  929 */         doCertReq(this.alias, this.sigAlgName, paramPrintStream);
/*      */       } finally {
/*  931 */         if (localObject2 != null) {
/*  932 */           ((PrintStream)localObject2).close();
/*      */         }
/*      */       }
/*  935 */       if ((this.verbose) && (this.filename != null)) {
/*  936 */         localObject3 = new MessageFormat(rb.getString("Certification.request.stored.in.file.filename."));
/*      */ 
/*  938 */         localObject4 = new Object[] { this.filename };
/*  939 */         System.err.println(((MessageFormat)localObject3).format(localObject4));
/*  940 */         System.err.println(rb.getString("Submit.this.to.your.CA"));
/*      */       }
/*  942 */     } else if (this.command == Command.DELETE) {
/*  943 */       doDeleteEntry(this.alias);
/*  944 */       this.kssave = true;
/*  945 */     } else if (this.command == Command.EXPORTCERT) {
/*  946 */       localObject2 = null;
/*  947 */       if (this.filename != null) {
/*  948 */         localObject2 = new PrintStream(new FileOutputStream(this.filename));
/*      */ 
/*  950 */         paramPrintStream = (PrintStream)localObject2;
/*      */       }
/*      */       try {
/*  953 */         doExportCert(this.alias, paramPrintStream);
/*      */       } finally {
/*  955 */         if (localObject2 != null) {
/*  956 */           ((PrintStream)localObject2).close();
/*      */         }
/*      */       }
/*  959 */       if (this.filename != null) {
/*  960 */         localObject3 = new MessageFormat(rb.getString("Certificate.stored.in.file.filename."));
/*      */ 
/*  962 */         localObject4 = new Object[] { this.filename };
/*  963 */         System.err.println(((MessageFormat)localObject3).format(localObject4));
/*      */       }
/*  965 */     } else if (this.command == Command.GENKEYPAIR) {
/*  966 */       if (this.keyAlgName == null) {
/*  967 */         this.keyAlgName = "DSA";
/*      */       }
/*  969 */       doGenKeyPair(this.alias, this.dname, this.keyAlgName, this.keysize, this.sigAlgName);
/*  970 */       this.kssave = true;
/*  971 */     } else if (this.command == Command.GENSECKEY) {
/*  972 */       if (this.keyAlgName == null) {
/*  973 */         this.keyAlgName = "DES";
/*      */       }
/*  975 */       doGenSecretKey(this.alias, this.keyAlgName, this.keysize);
/*  976 */       this.kssave = true;
/*  977 */     } else if (this.command == Command.IDENTITYDB) {
/*  978 */       localObject2 = System.in;
/*  979 */       if (this.filename != null)
/*  980 */         localObject2 = new FileInputStream(this.filename);
/*      */       try
/*      */       {
/*  983 */         doImportIdentityDatabase((InputStream)localObject2);
/*      */ 
/*  985 */         if (localObject2 != System.in)
/*  986 */           ((InputStream)localObject2).close();
/*      */       }
/*      */       finally
/*      */       {
/*  985 */         if (localObject2 != System.in)
/*  986 */           ((InputStream)localObject2).close();
/*      */       }
/*      */     }
/*  989 */     else if (this.command == Command.IMPORTCERT) {
/*  990 */       localObject2 = System.in;
/*  991 */       if (this.filename != null) {
/*  992 */         localObject2 = new FileInputStream(this.filename);
/*      */       }
/*  994 */       localObject3 = this.alias != null ? this.alias : "mykey";
/*      */       try {
/*  996 */         if (this.keyStore.entryInstanceOf((String)localObject3, KeyStore.PrivateKeyEntry.class))
/*      */         {
/*  998 */           this.kssave = installReply((String)localObject3, (InputStream)localObject2);
/*  999 */           if (this.kssave) {
/* 1000 */             System.err.println(rb.getString("Certificate.reply.was.installed.in.keystore"));
/*      */           }
/*      */           else {
/* 1003 */             System.err.println(rb.getString("Certificate.reply.was.not.installed.in.keystore"));
/*      */           }
/*      */         }
/* 1006 */         else if ((!this.keyStore.containsAlias((String)localObject3)) || (this.keyStore.entryInstanceOf((String)localObject3, KeyStore.TrustedCertificateEntry.class)))
/*      */         {
/* 1009 */           this.kssave = addTrustedCert((String)localObject3, (InputStream)localObject2);
/* 1010 */           if (this.kssave) {
/* 1011 */             System.err.println(rb.getString("Certificate.was.added.to.keystore"));
/*      */           }
/*      */           else {
/* 1014 */             System.err.println(rb.getString("Certificate.was.not.added.to.keystore"));
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/* 1019 */         if (localObject2 != System.in)
/* 1020 */           ((InputStream)localObject2).close();
/*      */       }
/*      */       finally
/*      */       {
/* 1019 */         if (localObject2 != System.in)
/* 1020 */           ((InputStream)localObject2).close();
/*      */       }
/*      */     }
/* 1023 */     else if (this.command == Command.IMPORTKEYSTORE) {
/* 1024 */       doImportKeyStore();
/* 1025 */       this.kssave = true;
/* 1026 */     } else if (this.command == Command.KEYCLONE) {
/* 1027 */       this.keyPassNew = this.newPass;
/*      */ 
/* 1030 */       if (this.alias == null) {
/* 1031 */         this.alias = "mykey";
/*      */       }
/* 1033 */       if (!this.keyStore.containsAlias(this.alias)) {
/* 1034 */         localObject2 = new MessageFormat(rb.getString("Alias.alias.does.not.exist"));
/*      */ 
/* 1036 */         localObject3 = new Object[] { this.alias };
/* 1037 */         throw new Exception(((MessageFormat)localObject2).format(localObject3));
/*      */       }
/* 1039 */       if (!this.keyStore.entryInstanceOf(this.alias, KeyStore.PrivateKeyEntry.class)) {
/* 1040 */         localObject2 = new MessageFormat(rb.getString("Alias.alias.references.an.entry.type.that.is.not.a.private.key.entry.The.keyclone.command.only.supports.cloning.of.private.key"));
/*      */ 
/* 1042 */         localObject3 = new Object[] { this.alias };
/* 1043 */         throw new Exception(((MessageFormat)localObject2).format(localObject3));
/*      */       }
/*      */ 
/* 1046 */       doCloneEntry(this.alias, this.dest, true);
/* 1047 */       this.kssave = true;
/* 1048 */     } else if (this.command == Command.CHANGEALIAS) {
/* 1049 */       if (this.alias == null) {
/* 1050 */         this.alias = "mykey";
/*      */       }
/* 1052 */       doCloneEntry(this.alias, this.dest, false);
/*      */ 
/* 1054 */       if (this.keyStore.containsAlias(this.alias)) {
/* 1055 */         doDeleteEntry(this.alias);
/*      */       }
/* 1057 */       this.kssave = true;
/* 1058 */     } else if (this.command == Command.KEYPASSWD) {
/* 1059 */       this.keyPassNew = this.newPass;
/* 1060 */       doChangeKeyPasswd(this.alias);
/* 1061 */       this.kssave = true;
/* 1062 */     } else if (this.command == Command.LIST) {
/* 1063 */       if (this.alias != null)
/* 1064 */         doPrintEntry(this.alias, paramPrintStream, true);
/*      */       else
/* 1066 */         doPrintEntries(paramPrintStream);
/*      */     }
/* 1068 */     else if (this.command == Command.PRINTCERT) {
/* 1069 */       doPrintCert(paramPrintStream);
/* 1070 */     } else if (this.command == Command.SELFCERT) {
/* 1071 */       doSelfCert(this.alias, this.dname, this.sigAlgName);
/* 1072 */       this.kssave = true;
/* 1073 */     } else if (this.command == Command.STOREPASSWD) {
/* 1074 */       this.storePassNew = this.newPass;
/* 1075 */       if (this.storePassNew == null) {
/* 1076 */         this.storePassNew = getNewPasswd("keystore password", this.storePass);
/*      */       }
/* 1078 */       this.kssave = true;
/* 1079 */     } else if (this.command == Command.GENCERT) {
/* 1080 */       if (this.alias == null) {
/* 1081 */         this.alias = "mykey";
/*      */       }
/* 1083 */       localObject2 = System.in;
/* 1084 */       if (this.infilename != null) {
/* 1085 */         localObject2 = new FileInputStream(this.infilename);
/*      */       }
/* 1087 */       localObject3 = null;
/* 1088 */       if (this.outfilename != null) {
/* 1089 */         localObject3 = new PrintStream(new FileOutputStream(this.outfilename));
/* 1090 */         paramPrintStream = (PrintStream)localObject3;
/*      */       }
/*      */       try {
/* 1093 */         doGenCert(this.alias, this.sigAlgName, (InputStream)localObject2, paramPrintStream);
/*      */       } finally {
/* 1095 */         if (localObject2 != System.in) {
/* 1096 */           ((InputStream)localObject2).close();
/*      */         }
/* 1098 */         if (localObject3 != null)
/* 1099 */           ((PrintStream)localObject3).close();
/*      */       }
/*      */     }
/* 1102 */     else if (this.command == Command.GENCRL) {
/* 1103 */       if (this.alias == null) {
/* 1104 */         this.alias = "mykey";
/*      */       }
/* 1106 */       localObject2 = null;
/* 1107 */       if (this.filename != null) {
/* 1108 */         localObject2 = new PrintStream(new FileOutputStream(this.filename));
/* 1109 */         paramPrintStream = (PrintStream)localObject2;
/*      */       }
/*      */       try {
/* 1112 */         doGenCRL(paramPrintStream);
/*      */       } finally {
/* 1114 */         if (localObject2 != null)
/* 1115 */           ((PrintStream)localObject2).close();
/*      */       }
/*      */     }
/* 1118 */     else if (this.command == Command.PRINTCERTREQ) {
/* 1119 */       localObject2 = System.in;
/* 1120 */       if (this.filename != null)
/* 1121 */         localObject2 = new FileInputStream(this.filename);
/*      */       try
/*      */       {
/* 1124 */         doPrintCertReq((InputStream)localObject2, paramPrintStream);
/*      */ 
/* 1126 */         if (localObject2 != System.in)
/* 1127 */           ((InputStream)localObject2).close();
/*      */       }
/*      */       finally
/*      */       {
/* 1126 */         if (localObject2 != System.in)
/* 1127 */           ((InputStream)localObject2).close();
/*      */       }
/*      */     }
/* 1130 */     else if (this.command == Command.PRINTCRL) {
/* 1131 */       doPrintCRL(this.filename, paramPrintStream);
/*      */     }
/*      */ 
/* 1135 */     if (this.kssave) {
/* 1136 */       if (this.verbose) {
/* 1137 */         localObject2 = new MessageFormat(rb.getString(".Storing.ksfname."));
/*      */ 
/* 1139 */         localObject3 = new Object[] { this.nullStream ? "keystore" : this.ksfname };
/* 1140 */         System.err.println(((MessageFormat)localObject2).format(localObject3));
/*      */       }
/*      */ 
/* 1143 */       if (this.token) {
/* 1144 */         this.keyStore.store(null, null);
/*      */       } else {
/* 1146 */         localObject2 = this.storePassNew != null ? this.storePassNew : this.storePass;
/* 1147 */         if (this.nullStream) {
/* 1148 */           this.keyStore.store(null, (char[])localObject2);
/*      */         } else {
/* 1150 */           localObject3 = new ByteArrayOutputStream();
/* 1151 */           this.keyStore.store((OutputStream)localObject3, (char[])localObject2);
/* 1152 */           localObject4 = new FileOutputStream(this.ksfname); localObject5 = null;
/*      */           try { ((FileOutputStream)localObject4).write(((ByteArrayOutputStream)localObject3).toByteArray()); }
/*      */           catch (Throwable localThrowable2)
/*      */           {
/* 1152 */             localObject5 = localThrowable2; throw localThrowable2;
/*      */           } finally {
/* 1154 */             if (localObject4 != null) if (localObject5 != null) try { ((FileOutputStream)localObject4).close(); } catch (Throwable localThrowable3) { ((Throwable)localObject5).addSuppressed(localThrowable3); } else ((FileOutputStream)localObject4).close();
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void doGenCert(String paramString1, String paramString2, InputStream paramInputStream, PrintStream paramPrintStream)
/*      */     throws Exception
/*      */   {
/* 1169 */     Certificate localCertificate1 = this.keyStore.getCertificate(paramString1);
/* 1170 */     byte[] arrayOfByte = localCertificate1.getEncoded();
/* 1171 */     X509CertImpl localX509CertImpl1 = new X509CertImpl(arrayOfByte);
/* 1172 */     X509CertInfo localX509CertInfo1 = (X509CertInfo)localX509CertImpl1.get("x509.info");
/*      */ 
/* 1174 */     X500Name localX500Name = (X500Name)localX509CertInfo1.get("subject.dname");
/*      */ 
/* 1177 */     Date localDate1 = getStartDate(this.startDate);
/* 1178 */     Date localDate2 = new Date();
/* 1179 */     localDate2.setTime(localDate1.getTime() + this.validity * 1000L * 24L * 60L * 60L);
/* 1180 */     CertificateValidity localCertificateValidity = new CertificateValidity(localDate1, localDate2);
/*      */ 
/* 1183 */     PrivateKey localPrivateKey = (PrivateKey)recoverKey(paramString1, this.storePass, this.keyPass).fst;
/*      */ 
/* 1185 */     if (paramString2 == null) {
/* 1186 */       paramString2 = getCompatibleSigAlgName(localPrivateKey.getAlgorithm());
/*      */     }
/* 1188 */     Signature localSignature = Signature.getInstance(paramString2);
/* 1189 */     localSignature.initSign(localPrivateKey);
/*      */ 
/* 1191 */     X509CertInfo localX509CertInfo2 = new X509CertInfo();
/* 1192 */     localX509CertInfo2.set("validity", localCertificateValidity);
/* 1193 */     localX509CertInfo2.set("serialNumber", new CertificateSerialNumber(new Random().nextInt() & 0x7FFFFFFF));
/*      */ 
/* 1195 */     localX509CertInfo2.set("version", new CertificateVersion(2));
/*      */ 
/* 1197 */     localX509CertInfo2.set("algorithmID", new CertificateAlgorithmId(AlgorithmId.getAlgorithmId(paramString2)));
/*      */ 
/* 1200 */     localX509CertInfo2.set("issuer", new CertificateIssuerName(localX500Name));
/*      */ 
/* 1202 */     BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(paramInputStream));
/* 1203 */     int i = 0;
/* 1204 */     StringBuffer localStringBuffer = new StringBuffer();
/*      */     while (true) {
/* 1206 */       localObject1 = localBufferedReader.readLine();
/* 1207 */       if (localObject1 == null) {
/*      */         break;
/*      */       }
/* 1210 */       if ((((String)localObject1).startsWith("-----BEGIN")) && (((String)localObject1).indexOf("REQUEST") >= 0)) {
/* 1211 */         i = 1;
/*      */       } else {
/* 1213 */         if ((((String)localObject1).startsWith("-----END")) && (((String)localObject1).indexOf("REQUEST") >= 0))
/*      */           break;
/* 1215 */         if (i != 0)
/* 1216 */           localStringBuffer.append((String)localObject1);
/*      */       }
/*      */     }
/* 1219 */     Object localObject1 = new BASE64Decoder().decodeBuffer(new String(localStringBuffer));
/* 1220 */     PKCS10 localPKCS10 = new PKCS10((byte[])localObject1);
/*      */ 
/* 1222 */     localX509CertInfo2.set("key", new CertificateX509Key(localPKCS10.getSubjectPublicKeyInfo()));
/* 1223 */     localX509CertInfo2.set("subject", new CertificateSubjectName(this.dname == null ? localPKCS10.getSubjectName() : new X500Name(this.dname)));
/*      */ 
/* 1225 */     CertificateExtensions localCertificateExtensions = null;
/* 1226 */     Iterator localIterator = localPKCS10.getAttributes().getAttributes().iterator();
/* 1227 */     while (localIterator.hasNext()) {
/* 1228 */       localObject2 = (PKCS10Attribute)localIterator.next();
/* 1229 */       if (((PKCS10Attribute)localObject2).getAttributeId().equals(PKCS9Attribute.EXTENSION_REQUEST_OID)) {
/* 1230 */         localCertificateExtensions = (CertificateExtensions)((PKCS10Attribute)localObject2).getAttributeValue();
/*      */       }
/*      */     }
/* 1233 */     Object localObject2 = createV3Extensions(localCertificateExtensions, null, this.v3ext, localPKCS10.getSubjectPublicKeyInfo(), localCertificate1.getPublicKey());
/*      */ 
/* 1239 */     localX509CertInfo2.set("extensions", localObject2);
/* 1240 */     X509CertImpl localX509CertImpl2 = new X509CertImpl(localX509CertInfo2);
/* 1241 */     localX509CertImpl2.sign(localPrivateKey, paramString2);
/* 1242 */     dumpCert(localX509CertImpl2, paramPrintStream);
/* 1243 */     for (Certificate localCertificate2 : this.keyStore.getCertificateChain(paramString1))
/* 1244 */       if ((localCertificate2 instanceof X509Certificate)) {
/* 1245 */         X509Certificate localX509Certificate = (X509Certificate)localCertificate2;
/* 1246 */         if (!isSelfSigned(localX509Certificate))
/* 1247 */           dumpCert(localX509Certificate, paramPrintStream);
/*      */       }
/*      */   }
/*      */ 
/*      */   private void doGenCRL(PrintStream paramPrintStream)
/*      */     throws Exception
/*      */   {
/* 1255 */     if (this.ids == null) {
/* 1256 */       throw new Exception("Must provide -id when -gencrl");
/*      */     }
/* 1258 */     Certificate localCertificate = this.keyStore.getCertificate(this.alias);
/* 1259 */     byte[] arrayOfByte = localCertificate.getEncoded();
/* 1260 */     X509CertImpl localX509CertImpl = new X509CertImpl(arrayOfByte);
/* 1261 */     X509CertInfo localX509CertInfo = (X509CertInfo)localX509CertImpl.get("x509.info");
/*      */ 
/* 1263 */     X500Name localX500Name = (X500Name)localX509CertInfo.get("subject.dname");
/*      */ 
/* 1266 */     Date localDate1 = getStartDate(this.startDate);
/* 1267 */     Date localDate2 = (Date)localDate1.clone();
/* 1268 */     localDate2.setTime(localDate2.getTime() + this.validity * 1000L * 24L * 60L * 60L);
/* 1269 */     CertificateValidity localCertificateValidity = new CertificateValidity(localDate1, localDate2);
/*      */ 
/* 1273 */     PrivateKey localPrivateKey = (PrivateKey)recoverKey(this.alias, this.storePass, this.keyPass).fst;
/*      */ 
/* 1275 */     if (this.sigAlgName == null) {
/* 1276 */       this.sigAlgName = getCompatibleSigAlgName(localPrivateKey.getAlgorithm());
/*      */     }
/*      */ 
/* 1279 */     X509CRLEntry[] arrayOfX509CRLEntry = new X509CRLEntry[this.ids.size()];
/* 1280 */     for (int i = 0; i < this.ids.size(); i++) {
/* 1281 */       String str = (String)this.ids.get(i);
/* 1282 */       int j = str.indexOf(':');
/* 1283 */       if (j >= 0) {
/* 1284 */         CRLExtensions localCRLExtensions = new CRLExtensions();
/* 1285 */         localCRLExtensions.set("Reason", new CRLReasonCodeExtension(Integer.parseInt(str.substring(j + 1))));
/* 1286 */         arrayOfX509CRLEntry[i] = new X509CRLEntryImpl(new BigInteger(str.substring(0, j)), localDate1, localCRLExtensions);
/*      */       }
/*      */       else {
/* 1289 */         arrayOfX509CRLEntry[i] = new X509CRLEntryImpl(new BigInteger((String)this.ids.get(i)), localDate1);
/*      */       }
/*      */     }
/* 1292 */     X509CRLImpl localX509CRLImpl = new X509CRLImpl(localX500Name, localDate1, localDate2, arrayOfX509CRLEntry);
/* 1293 */     localX509CRLImpl.sign(localPrivateKey, this.sigAlgName);
/* 1294 */     if (this.rfc) {
/* 1295 */       paramPrintStream.println("-----BEGIN X509 CRL-----");
/* 1296 */       new BASE64Encoder().encodeBuffer(localX509CRLImpl.getEncodedInternal(), paramPrintStream);
/* 1297 */       paramPrintStream.println("-----END X509 CRL-----");
/*      */     } else {
/* 1299 */       paramPrintStream.write(localX509CRLImpl.getEncodedInternal());
/*      */     }
/*      */   }
/*      */ 
/*      */   private void doCertReq(String paramString1, String paramString2, PrintStream paramPrintStream)
/*      */     throws Exception
/*      */   {
/* 1310 */     if (paramString1 == null) {
/* 1311 */       paramString1 = "mykey";
/*      */     }
/*      */ 
/* 1314 */     Pair localPair = recoverKey(paramString1, this.storePass, this.keyPass);
/* 1315 */     PrivateKey localPrivateKey = (PrivateKey)localPair.fst;
/* 1316 */     if (this.keyPass == null) {
/* 1317 */       this.keyPass = ((char[])localPair.snd);
/*      */     }
/*      */ 
/* 1320 */     Certificate localCertificate = this.keyStore.getCertificate(paramString1);
/* 1321 */     if (localCertificate == null) {
/* 1322 */       localObject1 = new MessageFormat(rb.getString("alias.has.no.public.key.certificate."));
/*      */ 
/* 1324 */       localObject2 = new Object[] { paramString1 };
/* 1325 */       throw new Exception(((MessageFormat)localObject1).format(localObject2));
/*      */     }
/* 1327 */     Object localObject1 = new PKCS10(localCertificate.getPublicKey());
/* 1328 */     Object localObject2 = createV3Extensions(null, null, this.v3ext, localCertificate.getPublicKey(), null);
/*      */ 
/* 1330 */     ((PKCS10)localObject1).getAttributes().setAttribute("extensions", new PKCS10Attribute(PKCS9Attribute.EXTENSION_REQUEST_OID, localObject2));
/*      */ 
/* 1334 */     if (paramString2 == null) {
/* 1335 */       paramString2 = getCompatibleSigAlgName(localPrivateKey.getAlgorithm());
/*      */     }
/*      */ 
/* 1338 */     Signature localSignature = Signature.getInstance(paramString2);
/* 1339 */     localSignature.initSign(localPrivateKey);
/* 1340 */     X500Name localX500Name = this.dname == null ? new X500Name(((X509Certificate)localCertificate).getSubjectDN().toString()) : new X500Name(this.dname);
/*      */ 
/* 1345 */     ((PKCS10)localObject1).encodeAndSign(localX500Name, localSignature);
/* 1346 */     ((PKCS10)localObject1).print(paramPrintStream);
/*      */   }
/*      */ 
/*      */   private void doDeleteEntry(String paramString)
/*      */     throws Exception
/*      */   {
/* 1353 */     if (!this.keyStore.containsAlias(paramString)) {
/* 1354 */       MessageFormat localMessageFormat = new MessageFormat(rb.getString("Alias.alias.does.not.exist"));
/*      */ 
/* 1356 */       Object[] arrayOfObject = { paramString };
/* 1357 */       throw new Exception(localMessageFormat.format(arrayOfObject));
/*      */     }
/* 1359 */     this.keyStore.deleteEntry(paramString);
/*      */   }
/*      */ 
/*      */   private void doExportCert(String paramString, PrintStream paramPrintStream)
/*      */     throws Exception
/*      */   {
/* 1368 */     if ((this.storePass == null) && (!KeyStoreUtil.isWindowsKeyStore(this.storetype)))
/*      */     {
/* 1370 */       printWarning();
/*      */     }
/* 1372 */     if (paramString == null)
/* 1373 */       paramString = "mykey";
/*      */     Object localObject2;
/* 1375 */     if (!this.keyStore.containsAlias(paramString)) {
/* 1376 */       localObject1 = new MessageFormat(rb.getString("Alias.alias.does.not.exist"));
/*      */ 
/* 1378 */       localObject2 = new Object[] { paramString };
/* 1379 */       throw new Exception(((MessageFormat)localObject1).format(localObject2));
/*      */     }
/*      */ 
/* 1382 */     Object localObject1 = (X509Certificate)this.keyStore.getCertificate(paramString);
/* 1383 */     if (localObject1 == null) {
/* 1384 */       localObject2 = new MessageFormat(rb.getString("Alias.alias.has.no.certificate"));
/*      */ 
/* 1386 */       Object[] arrayOfObject = { paramString };
/* 1387 */       throw new Exception(((MessageFormat)localObject2).format(arrayOfObject));
/*      */     }
/* 1389 */     dumpCert((Certificate)localObject1, paramPrintStream);
/*      */   }
/*      */ 
/*      */   private char[] promptForKeyPass(String paramString1, String paramString2, char[] paramArrayOfChar)
/*      */     throws Exception
/*      */   {
/* 1399 */     if ("PKCS12".equalsIgnoreCase(this.storetype))
/* 1400 */       return paramArrayOfChar;
/* 1401 */     if ((!this.token) && (!this.protectedPath))
/*      */     {
/* 1404 */       for (int i = 0; i < 3; i++) {
/* 1405 */         MessageFormat localMessageFormat = new MessageFormat(rb.getString("Enter.key.password.for.alias."));
/*      */ 
/* 1407 */         Object[] arrayOfObject = { paramString1 };
/* 1408 */         System.err.println(localMessageFormat.format(arrayOfObject));
/* 1409 */         if (paramString2 == null) {
/* 1410 */           System.err.print(rb.getString(".RETURN.if.same.as.keystore.password."));
/*      */         }
/*      */         else {
/* 1413 */           localMessageFormat = new MessageFormat(rb.getString(".RETURN.if.same.as.for.otherAlias."));
/*      */ 
/* 1415 */           localObject = new Object[] { paramString2 };
/* 1416 */           System.err.print(localMessageFormat.format(localObject));
/*      */         }
/* 1418 */         System.err.flush();
/* 1419 */         Object localObject = Password.readPassword(System.in);
/* 1420 */         this.passwords.add(localObject);
/* 1421 */         if (localObject == null)
/* 1422 */           return paramArrayOfChar;
/* 1423 */         if (localObject.length >= 6) {
/* 1424 */           System.err.print(rb.getString("Re.enter.new.password."));
/* 1425 */           char[] arrayOfChar = Password.readPassword(System.in);
/* 1426 */           this.passwords.add(arrayOfChar);
/* 1427 */           if (!Arrays.equals((char[])localObject, arrayOfChar)) {
/* 1428 */             System.err.println(rb.getString("They.don.t.match.Try.again"));
/*      */           }
/*      */           else
/*      */           {
/* 1432 */             return localObject;
/*      */           }
/*      */         } else { System.err.println(rb.getString("Key.password.is.too.short.must.be.at.least.6.characters")); }
/*      */ 
/*      */       }
/*      */ 
/* 1438 */       if (i == 3) {
/* 1439 */         if (this.command == Command.KEYCLONE) {
/* 1440 */           throw new Exception(rb.getString("Too.many.failures.Key.entry.not.cloned"));
/*      */         }
/*      */ 
/* 1443 */         throw new Exception(rb.getString("Too.many.failures.key.not.added.to.keystore"));
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1448 */     return null;
/*      */   }
/*      */ 
/*      */   private void doGenSecretKey(String paramString1, String paramString2, int paramInt)
/*      */     throws Exception
/*      */   {
/* 1457 */     if (paramString1 == null) {
/* 1458 */       paramString1 = "mykey";
/*      */     }
/* 1460 */     if (this.keyStore.containsAlias(paramString1)) {
/* 1461 */       localObject1 = new MessageFormat(rb.getString("Secret.key.not.generated.alias.alias.already.exists"));
/*      */ 
/* 1463 */       localObject2 = new Object[] { paramString1 };
/* 1464 */       throw new Exception(((MessageFormat)localObject1).format(localObject2));
/*      */     }
/*      */ 
/* 1467 */     Object localObject1 = null;
/* 1468 */     Object localObject2 = KeyGenerator.getInstance(paramString2);
/* 1469 */     if (paramInt != -1)
/* 1470 */       ((KeyGenerator)localObject2).init(paramInt);
/* 1471 */     else if ("DES".equalsIgnoreCase(paramString2))
/* 1472 */       ((KeyGenerator)localObject2).init(56);
/* 1473 */     else if ("DESede".equalsIgnoreCase(paramString2))
/* 1474 */       ((KeyGenerator)localObject2).init(168);
/*      */     else {
/* 1476 */       throw new Exception(rb.getString("Please.provide.keysize.for.secret.key.generation"));
/*      */     }
/*      */ 
/* 1480 */     localObject1 = ((KeyGenerator)localObject2).generateKey();
/* 1481 */     if (this.keyPass == null) {
/* 1482 */       this.keyPass = promptForKeyPass(paramString1, null, this.storePass);
/*      */     }
/* 1484 */     this.keyStore.setKeyEntry(paramString1, (Key)localObject1, this.keyPass, null);
/*      */   }
/*      */ 
/*      */   private static String getCompatibleSigAlgName(String paramString)
/*      */     throws Exception
/*      */   {
/* 1493 */     if ("DSA".equalsIgnoreCase(paramString))
/* 1494 */       return "SHA1WithDSA";
/* 1495 */     if ("RSA".equalsIgnoreCase(paramString))
/* 1496 */       return "SHA256WithRSA";
/* 1497 */     if ("EC".equalsIgnoreCase(paramString)) {
/* 1498 */       return "SHA256withECDSA";
/*      */     }
/* 1500 */     throw new Exception(rb.getString("Cannot.derive.signature.algorithm"));
/*      */   }
/*      */ 
/*      */   private void doGenKeyPair(String paramString1, String paramString2, String paramString3, int paramInt, String paramString4)
/*      */     throws Exception
/*      */   {
/* 1511 */     if (paramInt == -1) {
/* 1512 */       if ("EC".equalsIgnoreCase(paramString3))
/* 1513 */         paramInt = 256;
/* 1514 */       else if ("RSA".equalsIgnoreCase(paramString3))
/* 1515 */         paramInt = 2048;
/*      */       else {
/* 1517 */         paramInt = 1024;
/*      */       }
/*      */     }
/*      */ 
/* 1521 */     if (paramString1 == null)
/* 1522 */       paramString1 = "mykey";
/*      */     Object localObject2;
/* 1525 */     if (this.keyStore.containsAlias(paramString1)) {
/* 1526 */       localObject1 = new MessageFormat(rb.getString("Key.pair.not.generated.alias.alias.already.exists"));
/*      */ 
/* 1528 */       localObject2 = new Object[] { paramString1 };
/* 1529 */       throw new Exception(((MessageFormat)localObject1).format(localObject2));
/*      */     }
/*      */ 
/* 1532 */     if (paramString4 == null) {
/* 1533 */       paramString4 = getCompatibleSigAlgName(paramString3);
/*      */     }
/* 1535 */     Object localObject1 = new CertAndKeyGen(paramString3, paramString4, this.providerName);
/*      */ 
/* 1541 */     if (paramString2 == null)
/* 1542 */       localObject2 = getX500Name();
/*      */     else {
/* 1544 */       localObject2 = new X500Name(paramString2);
/*      */     }
/*      */ 
/* 1547 */     ((CertAndKeyGen)localObject1).generate(paramInt);
/* 1548 */     PrivateKey localPrivateKey = ((CertAndKeyGen)localObject1).getPrivateKey();
/*      */ 
/* 1550 */     CertificateExtensions localCertificateExtensions = createV3Extensions(null, null, this.v3ext, ((CertAndKeyGen)localObject1).getPublicKeyAnyway(), null);
/*      */ 
/* 1557 */     X509Certificate[] arrayOfX509Certificate = new X509Certificate[1];
/* 1558 */     arrayOfX509Certificate[0] = ((CertAndKeyGen)localObject1).getSelfCertificate((X500Name)localObject2, getStartDate(this.startDate), this.validity * 24L * 60L * 60L, localCertificateExtensions);
/*      */ 
/* 1561 */     if (this.verbose) {
/* 1562 */       MessageFormat localMessageFormat = new MessageFormat(rb.getString("Generating.keysize.bit.keyAlgName.key.pair.and.self.signed.certificate.sigAlgName.with.a.validity.of.validality.days.for"));
/*      */ 
/* 1564 */       Object[] arrayOfObject = { new Integer(paramInt), localPrivateKey.getAlgorithm(), arrayOfX509Certificate[0].getSigAlgName(), new Long(this.validity), localObject2 };
/*      */ 
/* 1569 */       System.err.println(localMessageFormat.format(arrayOfObject));
/*      */     }
/*      */ 
/* 1572 */     if (this.keyPass == null) {
/* 1573 */       this.keyPass = promptForKeyPass(paramString1, null, this.storePass);
/*      */     }
/* 1575 */     this.keyStore.setKeyEntry(paramString1, localPrivateKey, this.keyPass, arrayOfX509Certificate);
/*      */   }
/*      */ 
/*      */   private void doCloneEntry(String paramString1, String paramString2, boolean paramBoolean)
/*      */     throws Exception
/*      */   {
/* 1587 */     if (paramString1 == null) {
/* 1588 */       paramString1 = "mykey";
/*      */     }
/*      */ 
/* 1591 */     if (this.keyStore.containsAlias(paramString2)) {
/* 1592 */       localObject1 = new MessageFormat(rb.getString("Destination.alias.dest.already.exists"));
/*      */ 
/* 1594 */       localObject2 = new Object[] { paramString2 };
/* 1595 */       throw new Exception(((MessageFormat)localObject1).format(localObject2));
/*      */     }
/*      */ 
/* 1598 */     Object localObject1 = recoverEntry(this.keyStore, paramString1, this.storePass, this.keyPass);
/* 1599 */     Object localObject2 = (KeyStore.Entry)((Pair)localObject1).fst;
/* 1600 */     this.keyPass = ((char[])((Pair)localObject1).snd);
/*      */ 
/* 1602 */     KeyStore.PasswordProtection localPasswordProtection = null;
/*      */ 
/* 1604 */     if (this.keyPass != null) {
/* 1605 */       if ((!paramBoolean) || ("PKCS12".equalsIgnoreCase(this.storetype))) {
/* 1606 */         this.keyPassNew = this.keyPass;
/*      */       }
/* 1608 */       else if (this.keyPassNew == null) {
/* 1609 */         this.keyPassNew = promptForKeyPass(paramString2, paramString1, this.keyPass);
/*      */       }
/*      */ 
/* 1612 */       localPasswordProtection = new KeyStore.PasswordProtection(this.keyPassNew);
/*      */     }
/* 1614 */     this.keyStore.setEntry(paramString2, (KeyStore.Entry)localObject2, localPasswordProtection);
/*      */   }
/*      */ 
/*      */   private void doChangeKeyPasswd(String paramString)
/*      */     throws Exception
/*      */   {
/* 1623 */     if (paramString == null) {
/* 1624 */       paramString = "mykey";
/*      */     }
/* 1626 */     Pair localPair = recoverKey(paramString, this.storePass, this.keyPass);
/* 1627 */     Key localKey = (Key)localPair.fst;
/* 1628 */     if (this.keyPass == null) {
/* 1629 */       this.keyPass = ((char[])localPair.snd);
/*      */     }
/*      */ 
/* 1632 */     if (this.keyPassNew == null) {
/* 1633 */       MessageFormat localMessageFormat = new MessageFormat(rb.getString("key.password.for.alias."));
/*      */ 
/* 1635 */       Object[] arrayOfObject = { paramString };
/* 1636 */       this.keyPassNew = getNewPasswd(localMessageFormat.format(arrayOfObject), this.keyPass);
/*      */     }
/* 1638 */     this.keyStore.setKeyEntry(paramString, localKey, this.keyPassNew, this.keyStore.getCertificateChain(paramString));
/*      */   }
/*      */ 
/*      */   private void doImportIdentityDatabase(InputStream paramInputStream)
/*      */     throws Exception
/*      */   {
/* 1650 */     System.err.println(rb.getString("No.entries.from.identity.database.added"));
/*      */   }
/*      */ 
/*      */   private void doPrintEntry(String paramString, PrintStream paramPrintStream, boolean paramBoolean)
/*      */     throws Exception
/*      */   {
/* 1661 */     if ((this.storePass == null) && (paramBoolean) && (!KeyStoreUtil.isWindowsKeyStore(this.storetype)))
/*      */     {
/* 1663 */       printWarning();
/*      */     }
/*      */     Object localObject1;
/*      */     Object[] arrayOfObject1;
/* 1666 */     if (!this.keyStore.containsAlias(paramString)) {
/* 1667 */       localObject1 = new MessageFormat(rb.getString("Alias.alias.does.not.exist"));
/*      */ 
/* 1669 */       arrayOfObject1 = new Object[] { paramString };
/* 1670 */       throw new Exception(((MessageFormat)localObject1).format(arrayOfObject1));
/*      */     }
/*      */     Object localObject2;
/* 1673 */     if ((this.verbose) || (this.rfc) || (this.debug)) {
/* 1674 */       localObject1 = new MessageFormat(rb.getString("Alias.name.alias"));
/*      */ 
/* 1676 */       arrayOfObject1 = new Object[] { paramString };
/* 1677 */       paramPrintStream.println(((MessageFormat)localObject1).format(arrayOfObject1));
/*      */ 
/* 1679 */       if (!this.token) {
/* 1680 */         localObject1 = new MessageFormat(rb.getString("Creation.date.keyStore.getCreationDate.alias."));
/*      */ 
/* 1682 */         localObject2 = new Object[] { this.keyStore.getCreationDate(paramString) };
/* 1683 */         paramPrintStream.println(((MessageFormat)localObject1).format(localObject2));
/*      */       }
/*      */     }
/* 1686 */     else if (!this.token) {
/* 1687 */       localObject1 = new MessageFormat(rb.getString("alias.keyStore.getCreationDate.alias."));
/*      */ 
/* 1689 */       arrayOfObject1 = new Object[] { paramString, this.keyStore.getCreationDate(paramString) };
/* 1690 */       paramPrintStream.print(((MessageFormat)localObject1).format(arrayOfObject1));
/*      */     } else {
/* 1692 */       localObject1 = new MessageFormat(rb.getString("alias."));
/*      */ 
/* 1694 */       arrayOfObject1 = new Object[] { paramString };
/* 1695 */       paramPrintStream.print(((MessageFormat)localObject1).format(arrayOfObject1));
/*      */     }
/*      */ 
/* 1699 */     if (this.keyStore.entryInstanceOf(paramString, KeyStore.SecretKeyEntry.class)) {
/* 1700 */       if ((this.verbose) || (this.rfc) || (this.debug)) {
/* 1701 */         localObject1 = new Object[] { "SecretKeyEntry" };
/* 1702 */         paramPrintStream.println(new MessageFormat(rb.getString("Entry.type.type.")).format(localObject1));
/*      */       }
/*      */       else {
/* 1705 */         paramPrintStream.println("SecretKeyEntry, ");
/*      */       }
/* 1707 */     } else if (this.keyStore.entryInstanceOf(paramString, KeyStore.PrivateKeyEntry.class)) {
/* 1708 */       if ((this.verbose) || (this.rfc) || (this.debug)) {
/* 1709 */         localObject1 = new Object[] { "PrivateKeyEntry" };
/* 1710 */         paramPrintStream.println(new MessageFormat(rb.getString("Entry.type.type.")).format(localObject1));
/*      */       }
/*      */       else {
/* 1713 */         paramPrintStream.println("PrivateKeyEntry, ");
/*      */       }
/*      */ 
/* 1717 */       localObject1 = this.keyStore.getCertificateChain(paramString);
/* 1718 */       if (localObject1 != null) {
/* 1719 */         if ((this.verbose) || (this.rfc) || (this.debug)) {
/* 1720 */           paramPrintStream.println(rb.getString("Certificate.chain.length.") + localObject1.length);
/*      */ 
/* 1722 */           for (int i = 0; i < localObject1.length; i++) {
/* 1723 */             localObject2 = new MessageFormat(rb.getString("Certificate.i.1."));
/*      */ 
/* 1725 */             Object[] arrayOfObject3 = { new Integer(i + 1) };
/* 1726 */             paramPrintStream.println(((MessageFormat)localObject2).format(arrayOfObject3));
/* 1727 */             if ((this.verbose) && ((localObject1[i] instanceof X509Certificate)))
/* 1728 */               printX509Cert((X509Certificate)localObject1[i], paramPrintStream);
/* 1729 */             else if (this.debug)
/* 1730 */               paramPrintStream.println(localObject1[i].toString());
/*      */             else
/* 1732 */               dumpCert(localObject1[i], paramPrintStream);
/*      */           }
/*      */         }
/*      */         else
/*      */         {
/* 1737 */           paramPrintStream.println(rb.getString("Certificate.fingerprint.SHA1.") + getCertFingerPrint("SHA1", localObject1[0]));
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/* 1742 */     else if (this.keyStore.entryInstanceOf(paramString, KeyStore.TrustedCertificateEntry.class))
/*      */     {
/* 1745 */       localObject1 = this.keyStore.getCertificate(paramString);
/* 1746 */       Object[] arrayOfObject2 = { "trustedCertEntry" };
/* 1747 */       localObject2 = new MessageFormat(rb.getString("Entry.type.type.")).format(arrayOfObject2) + "\n";
/*      */ 
/* 1749 */       if ((this.verbose) && ((localObject1 instanceof X509Certificate))) {
/* 1750 */         paramPrintStream.println((String)localObject2);
/* 1751 */         printX509Cert((X509Certificate)localObject1, paramPrintStream);
/* 1752 */       } else if (this.rfc) {
/* 1753 */         paramPrintStream.println((String)localObject2);
/* 1754 */         dumpCert((Certificate)localObject1, paramPrintStream);
/* 1755 */       } else if (this.debug) {
/* 1756 */         paramPrintStream.println(((Certificate)localObject1).toString());
/*      */       } else {
/* 1758 */         paramPrintStream.println("trustedCertEntry, ");
/* 1759 */         paramPrintStream.println(rb.getString("Certificate.fingerprint.SHA1.") + getCertFingerPrint("SHA1", (Certificate)localObject1));
/*      */       }
/*      */     }
/*      */     else {
/* 1763 */       paramPrintStream.println(rb.getString("Unknown.Entry.Type"));
/*      */     }
/*      */   }
/*      */ 
/*      */   KeyStore loadSourceKeyStore()
/*      */     throws Exception
/*      */   {
/* 1772 */     int i = 0;
/*      */ 
/* 1774 */     FileInputStream localFileInputStream = null;
/*      */     Object localObject1;
/* 1776 */     if (("PKCS11".equalsIgnoreCase(this.srcstoretype)) || (KeyStoreUtil.isWindowsKeyStore(this.srcstoretype)))
/*      */     {
/* 1778 */       if (!"NONE".equals(this.srcksfname)) {
/* 1779 */         System.err.println(MessageFormat.format(rb.getString(".keystore.must.be.NONE.if.storetype.is.{0}"), new Object[] { this.srcstoretype }));
/*      */ 
/* 1781 */         System.err.println();
/* 1782 */         tinyHelp();
/*      */       }
/* 1784 */       i = 1;
/*      */     }
/* 1786 */     else if (this.srcksfname != null) {
/* 1787 */       localObject1 = new File(this.srcksfname);
/* 1788 */       if ((((File)localObject1).exists()) && (((File)localObject1).length() == 0L)) {
/* 1789 */         throw new Exception(rb.getString("Source.keystore.file.exists.but.is.empty.") + this.srcksfname);
/*      */       }
/*      */ 
/* 1793 */       localFileInputStream = new FileInputStream((File)localObject1);
/*      */     } else {
/* 1795 */       throw new Exception(rb.getString("Please.specify.srckeystore"));
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/* 1802 */       if (this.srcProviderName == null)
/* 1803 */         localObject1 = KeyStore.getInstance(this.srcstoretype);
/*      */       else {
/* 1805 */         localObject1 = KeyStore.getInstance(this.srcstoretype, this.srcProviderName);
/*      */       }
/*      */ 
/* 1808 */       if ((this.srcstorePass == null) && (!this.srcprotectedPath) && (!KeyStoreUtil.isWindowsKeyStore(this.srcstoretype)))
/*      */       {
/* 1811 */         System.err.print(rb.getString("Enter.source.keystore.password."));
/* 1812 */         System.err.flush();
/* 1813 */         this.srcstorePass = Password.readPassword(System.in);
/* 1814 */         this.passwords.add(this.srcstorePass);
/*      */       }
/*      */ 
/* 1818 */       if (("PKCS12".equalsIgnoreCase(this.srcstoretype)) && 
/* 1819 */         (this.srckeyPass != null) && (this.srcstorePass != null) && (!Arrays.equals(this.srcstorePass, this.srckeyPass)))
/*      */       {
/* 1821 */         MessageFormat localMessageFormat = new MessageFormat(rb.getString("Warning.Different.store.and.key.passwords.not.supported.for.PKCS12.KeyStores.Ignoring.user.specified.command.value."));
/*      */ 
/* 1823 */         Object[] arrayOfObject = { "-srckeypass" };
/* 1824 */         System.err.println(localMessageFormat.format(arrayOfObject));
/* 1825 */         this.srckeyPass = this.srcstorePass;
/*      */       }
/*      */ 
/* 1829 */       ((KeyStore)localObject1).load(localFileInputStream, this.srcstorePass);
/*      */     } finally {
/* 1831 */       if (localFileInputStream != null) {
/* 1832 */         localFileInputStream.close();
/*      */       }
/*      */     }
/*      */ 
/* 1836 */     if ((this.srcstorePass == null) && (!KeyStoreUtil.isWindowsKeyStore(this.srcstoretype)))
/*      */     {
/* 1840 */       System.err.println();
/* 1841 */       System.err.println(rb.getString(".WARNING.WARNING.WARNING."));
/*      */ 
/* 1843 */       System.err.println(rb.getString(".The.integrity.of.the.information.stored.in.the.srckeystore."));
/*      */ 
/* 1845 */       System.err.println(rb.getString(".WARNING.WARNING.WARNING."));
/*      */ 
/* 1847 */       System.err.println();
/*      */     }
/*      */ 
/* 1850 */     return localObject1;
/*      */   }
/*      */ 
/*      */   private void doImportKeyStore()
/*      */     throws Exception
/*      */   {
/* 1860 */     if (this.alias != null) {
/* 1861 */       doImportKeyStoreSingle(loadSourceKeyStore(), this.alias);
/*      */     } else {
/* 1863 */       if ((this.dest != null) || (this.srckeyPass != null) || (this.destKeyPass != null)) {
/* 1864 */         throw new Exception(rb.getString("if.alias.not.specified.destalias.srckeypass.and.destkeypass.must.not.be.specified"));
/*      */       }
/*      */ 
/* 1867 */       doImportKeyStoreAll(loadSourceKeyStore());
/*      */     }
/*      */   }
/*      */ 
/*      */   private int doImportKeyStoreSingle(KeyStore paramKeyStore, String paramString)
/*      */     throws Exception
/*      */   {
/* 1887 */     String str = this.dest == null ? paramString : this.dest;
/*      */ 
/* 1889 */     if (this.keyStore.containsAlias(str)) {
/* 1890 */       localObject1 = new Object[] { paramString };
/* 1891 */       if (this.noprompt) {
/* 1892 */         System.err.println(new MessageFormat(rb.getString("Warning.Overwriting.existing.alias.alias.in.destination.keystore")).format(localObject1));
/*      */       }
/*      */       else {
/* 1895 */         localObject2 = getYesNoReply(new MessageFormat(rb.getString("Existing.entry.alias.alias.exists.overwrite.no.")).format(localObject1));
/*      */ 
/* 1897 */         if ("NO".equals(localObject2)) {
/* 1898 */           str = inputStringFromStdin(rb.getString("Enter.new.alias.name.RETURN.to.cancel.import.for.this.entry."));
/*      */ 
/* 1900 */           if ("".equals(str)) {
/* 1901 */             System.err.println(new MessageFormat(rb.getString("Entry.for.alias.alias.not.imported.")).format(localObject1));
/*      */ 
/* 1904 */             return 0;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1910 */     Object localObject1 = recoverEntry(paramKeyStore, paramString, this.srcstorePass, this.srckeyPass);
/* 1911 */     Object localObject2 = (KeyStore.Entry)((Pair)localObject1).fst;
/*      */ 
/* 1913 */     KeyStore.PasswordProtection localPasswordProtection = null;
/*      */ 
/* 1919 */     if (this.destKeyPass != null)
/* 1920 */       localPasswordProtection = new KeyStore.PasswordProtection(this.destKeyPass);
/* 1921 */     else if (((Pair)localObject1).snd != null) {
/* 1922 */       localPasswordProtection = new KeyStore.PasswordProtection((char[])((Pair)localObject1).snd);
/*      */     }
/*      */     try
/*      */     {
/* 1926 */       this.keyStore.setEntry(str, (KeyStore.Entry)localObject2, localPasswordProtection);
/* 1927 */       return 1;
/*      */     } catch (KeyStoreException localKeyStoreException) {
/* 1929 */       Object[] arrayOfObject = { paramString, localKeyStoreException.toString() };
/* 1930 */       MessageFormat localMessageFormat = new MessageFormat(rb.getString("Problem.importing.entry.for.alias.alias.exception.Entry.for.alias.alias.not.imported."));
/*      */ 
/* 1932 */       System.err.println(localMessageFormat.format(arrayOfObject));
/* 1933 */     }return 2;
/*      */   }
/*      */ 
/*      */   private void doImportKeyStoreAll(KeyStore paramKeyStore)
/*      */     throws Exception
/*      */   {
/* 1939 */     int i = 0;
/* 1940 */     int j = paramKeyStore.size();
/* 1941 */     Object localObject1 = paramKeyStore.aliases();
/* 1942 */     while (((Enumeration)localObject1).hasMoreElements()) {
/* 1943 */       localObject2 = (String)((Enumeration)localObject1).nextElement();
/* 1944 */       int k = doImportKeyStoreSingle(paramKeyStore, (String)localObject2);
/*      */       Object localObject3;
/* 1945 */       if (k == 1) {
/* 1946 */         i++;
/* 1947 */         localObject3 = new Object[] { localObject2 };
/* 1948 */         MessageFormat localMessageFormat = new MessageFormat(rb.getString("Entry.for.alias.alias.successfully.imported."));
/* 1949 */         System.err.println(localMessageFormat.format(localObject3));
/* 1950 */       } else if ((k == 2) && 
/* 1951 */         (!this.noprompt)) {
/* 1952 */         localObject3 = getYesNoReply("Do you want to quit the import process? [no]:  ");
/* 1953 */         if ("YES".equals(localObject3))
/*      */         {
/*      */           break;
/*      */         }
/*      */       }
/*      */     }
/* 1959 */     localObject1 = new Object[] { Integer.valueOf(i), Integer.valueOf(j - i) };
/* 1960 */     Object localObject2 = new MessageFormat(rb.getString("Import.command.completed.ok.entries.successfully.imported.fail.entries.failed.or.cancelled"));
/*      */ 
/* 1962 */     System.err.println(((MessageFormat)localObject2).format(localObject1));
/*      */   }
/*      */ 
/*      */   private void doPrintEntries(PrintStream paramPrintStream)
/*      */     throws Exception
/*      */   {
/* 1971 */     if ((this.storePass == null) && (!KeyStoreUtil.isWindowsKeyStore(this.storetype)))
/*      */     {
/* 1973 */       printWarning();
/*      */     }
/* 1975 */     else paramPrintStream.println();
/*      */ 
/* 1978 */     paramPrintStream.println(rb.getString("Keystore.type.") + this.keyStore.getType());
/* 1979 */     paramPrintStream.println(rb.getString("Keystore.provider.") + this.keyStore.getProvider().getName());
/*      */ 
/* 1981 */     paramPrintStream.println();
/*      */ 
/* 1984 */     MessageFormat localMessageFormat = this.keyStore.size() == 1 ? new MessageFormat(rb.getString("Your.keystore.contains.keyStore.size.entry")) : new MessageFormat(rb.getString("Your.keystore.contains.keyStore.size.entries"));
/*      */ 
/* 1989 */     Object[] arrayOfObject = { new Integer(this.keyStore.size()) };
/* 1990 */     paramPrintStream.println(localMessageFormat.format(arrayOfObject));
/* 1991 */     paramPrintStream.println();
/*      */ 
/* 1993 */     Enumeration localEnumeration = this.keyStore.aliases();
/* 1994 */     while (localEnumeration.hasMoreElements()) {
/* 1995 */       String str = (String)localEnumeration.nextElement();
/* 1996 */       doPrintEntry(str, paramPrintStream, false);
/* 1997 */       if ((this.verbose) || (this.rfc)) {
/* 1998 */         paramPrintStream.println(rb.getString("NEWLINE"));
/* 1999 */         paramPrintStream.println(rb.getString("STAR"));
/*      */ 
/* 2001 */         paramPrintStream.println(rb.getString("STARNN"));
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private static <T> Iterable<T> e2i(Enumeration<T> paramEnumeration)
/*      */   {
/* 2008 */     return new Iterable()
/*      */     {
/*      */       public Iterator<T> iterator() {
/* 2011 */         return new Iterator()
/*      */         {
/*      */           public boolean hasNext() {
/* 2014 */             return KeyTool.1.this.val$e.hasMoreElements();
/*      */           }
/*      */ 
/*      */           public T next() {
/* 2018 */             return KeyTool.1.this.val$e.nextElement();
/*      */           }
/*      */           public void remove() {
/* 2021 */             throw new UnsupportedOperationException("Not supported yet.");
/*      */           }
/*      */         };
/*      */       }
/*      */     };
/*      */   }
/*      */ 
/*      */   public static Collection<? extends CRL> loadCRLs(String paramString)
/*      */     throws Exception
/*      */   {
/* 2034 */     Object localObject1 = null;
/* 2035 */     URI localURI = null;
/* 2036 */     if (paramString == null)
/* 2037 */       localObject1 = System.in;
/*      */     else {
/*      */       try {
/* 2040 */         localURI = new URI(paramString);
/* 2041 */         if (!localURI.getScheme().equals("ldap"))
/*      */         {
/* 2044 */           localObject1 = localURI.toURL().openStream();
/*      */         }
/*      */       } catch (Exception localException1) {
/*      */         try {
/* 2048 */           localObject1 = new FileInputStream(paramString);
/*      */         } catch (Exception localException2) {
/* 2050 */           if ((localURI == null) || (localURI.getScheme() == null)) {
/* 2051 */             throw localException2;
/*      */           }
/* 2053 */           throw localException1;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 2058 */     if (localObject1 != null)
/*      */     {
/*      */       try
/*      */       {
/* 2064 */         localObject2 = new ByteArrayOutputStream();
/* 2065 */         localObject3 = new byte[4096];
/*      */         while (true) {
/* 2067 */           int i = ((InputStream)localObject1).read((byte[])localObject3);
/* 2068 */           if (i < 0) break;
/* 2069 */           ((ByteArrayOutputStream)localObject2).write((byte[])localObject3, 0, i);
/*      */         }
/* 2071 */         return CertificateFactory.getInstance("X509").generateCRLs(new ByteArrayInputStream(((ByteArrayOutputStream)localObject2).toByteArray()));
/*      */       }
/*      */       finally {
/* 2074 */         if (localObject1 != System.in) {
/* 2075 */           ((InputStream)localObject1).close();
/*      */         }
/*      */       }
/*      */     }
/* 2079 */     Object localObject2 = localURI.getPath();
/* 2080 */     if (((String)localObject2).charAt(0) == '/') localObject2 = ((String)localObject2).substring(1);
/* 2081 */     Object localObject3 = new LDAPCertStoreHelper();
/* 2082 */     Object localObject4 = ((LDAPCertStoreHelper)localObject3).getCertStore(localURI);
/* 2083 */     X509CRLSelector localX509CRLSelector = ((LDAPCertStoreHelper)localObject3).wrap(new X509CRLSelector(), null, (String)localObject2);
/*      */ 
/* 2085 */     return ((CertStore)localObject4).getCRLs(localX509CRLSelector);
/*      */   }
/*      */ 
/*      */   public static List<CRL> readCRLsFromCert(X509Certificate paramX509Certificate)
/*      */     throws Exception
/*      */   {
/* 2095 */     ArrayList localArrayList = new ArrayList();
/* 2096 */     CRLDistributionPointsExtension localCRLDistributionPointsExtension = X509CertImpl.toImpl(paramX509Certificate).getCRLDistributionPointsExtension();
/*      */ 
/* 2098 */     if (localCRLDistributionPointsExtension == null) return localArrayList;
/* 2099 */     for (DistributionPoint localDistributionPoint : (List)localCRLDistributionPointsExtension.get("points"))
/*      */     {
/* 2101 */       GeneralNames localGeneralNames = localDistributionPoint.getFullName();
/* 2102 */       if (localGeneralNames != null) {
/* 2103 */         for (GeneralName localGeneralName : localGeneralNames.names()) {
/* 2104 */           if (localGeneralName.getType() == 6) {
/* 2105 */             URIName localURIName = (URIName)localGeneralName.getName();
/* 2106 */             for (CRL localCRL : loadCRLs(localURIName.getName())) {
/* 2107 */               if ((localCRL instanceof X509CRL)) {
/* 2108 */                 localArrayList.add((X509CRL)localCRL);
/*      */               }
/*      */             }
/* 2111 */             break;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 2116 */     return localArrayList;
/*      */   }
/*      */ 
/*      */   private static String verifyCRL(KeyStore paramKeyStore, CRL paramCRL) throws Exception
/*      */   {
/* 2121 */     X509CRLImpl localX509CRLImpl = (X509CRLImpl)paramCRL;
/* 2122 */     X500Principal localX500Principal = localX509CRLImpl.getIssuerX500Principal();
/* 2123 */     for (String str : e2i(paramKeyStore.aliases())) {
/* 2124 */       Certificate localCertificate = paramKeyStore.getCertificate(str);
/* 2125 */       if ((localCertificate instanceof X509Certificate)) {
/* 2126 */         X509Certificate localX509Certificate = (X509Certificate)localCertificate;
/* 2127 */         if (localX509Certificate.getSubjectX500Principal().equals(localX500Principal))
/*      */           try {
/* 2129 */             ((X509CRLImpl)paramCRL).verify(localCertificate.getPublicKey());
/* 2130 */             return str;
/*      */           }
/*      */           catch (Exception localException) {
/*      */           }
/*      */       }
/*      */     }
/* 2136 */     return null;
/*      */   }
/*      */ 
/*      */   private void doPrintCRL(String paramString, PrintStream paramPrintStream) throws Exception
/*      */   {
/* 2141 */     for (CRL localCRL : loadCRLs(paramString)) {
/* 2142 */       printCRL(localCRL, paramPrintStream);
/* 2143 */       String str = null;
/* 2144 */       if (this.caks != null) {
/* 2145 */         str = verifyCRL(this.caks, localCRL);
/* 2146 */         if (str != null) {
/* 2147 */           System.out.println("Verified by " + str + " in cacerts");
/*      */         }
/*      */       }
/* 2150 */       if ((str == null) && (this.keyStore != null)) {
/* 2151 */         str = verifyCRL(this.keyStore, localCRL);
/* 2152 */         if (str != null) {
/* 2153 */           System.out.println("Verified by " + str + " in keystore");
/*      */         }
/*      */       }
/* 2156 */       if (str == null) {
/* 2157 */         paramPrintStream.println(rb.getString("STAR"));
/*      */ 
/* 2159 */         paramPrintStream.println("WARNING: not verified. Make sure -keystore and -alias are correct.");
/* 2160 */         paramPrintStream.println(rb.getString("STARNN"));
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void printCRL(CRL paramCRL, PrintStream paramPrintStream)
/*      */     throws Exception
/*      */   {
/* 2168 */     if (this.rfc) {
/* 2169 */       X509CRL localX509CRL = (X509CRL)paramCRL;
/* 2170 */       paramPrintStream.println("-----BEGIN X509 CRL-----");
/* 2171 */       new BASE64Encoder().encodeBuffer(localX509CRL.getEncoded(), paramPrintStream);
/* 2172 */       paramPrintStream.println("-----END X509 CRL-----");
/*      */     } else {
/* 2174 */       paramPrintStream.println(paramCRL.toString());
/*      */     }
/*      */   }
/*      */ 
/*      */   private void doPrintCertReq(InputStream paramInputStream, PrintStream paramPrintStream)
/*      */     throws Exception
/*      */   {
/* 2181 */     BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(paramInputStream));
/* 2182 */     StringBuffer localStringBuffer = new StringBuffer();
/* 2183 */     int i = 0;
/*      */     while (true) {
/* 2185 */       localObject = localBufferedReader.readLine();
/* 2186 */       if (localObject == null) break;
/* 2187 */       if (i == 0) {
/* 2188 */         if (((String)localObject).startsWith("-----"))
/* 2189 */           i = 1;
/*      */       }
/*      */       else {
/* 2192 */         if (((String)localObject).startsWith("-----")) {
/*      */           break;
/*      */         }
/* 2195 */         localStringBuffer.append((String)localObject);
/*      */       }
/*      */     }
/* 2198 */     Object localObject = new PKCS10(new BASE64Decoder().decodeBuffer(new String(localStringBuffer)));
/*      */ 
/* 2200 */     PublicKey localPublicKey = ((PKCS10)localObject).getSubjectPublicKeyInfo();
/* 2201 */     paramPrintStream.printf(rb.getString("PKCS.10.Certificate.Request.Version.1.0.Subject.s.Public.Key.s.format.s.key."), new Object[] { ((PKCS10)localObject).getSubjectName(), localPublicKey.getFormat(), localPublicKey.getAlgorithm() });
/*      */ 
/* 2203 */     for (PKCS10Attribute localPKCS10Attribute : ((PKCS10)localObject).getAttributes().getAttributes()) {
/* 2204 */       ObjectIdentifier localObjectIdentifier = localPKCS10Attribute.getAttributeId();
/* 2205 */       if (localObjectIdentifier.equals(PKCS9Attribute.EXTENSION_REQUEST_OID)) {
/* 2206 */         CertificateExtensions localCertificateExtensions = (CertificateExtensions)localPKCS10Attribute.getAttributeValue();
/* 2207 */         if (localCertificateExtensions != null)
/* 2208 */           printExtensions(rb.getString("Extension.Request."), localCertificateExtensions, paramPrintStream);
/*      */       }
/*      */       else {
/* 2211 */         paramPrintStream.println(localPKCS10Attribute.getAttributeId());
/* 2212 */         paramPrintStream.println(localPKCS10Attribute.getAttributeValue());
/*      */       }
/*      */     }
/* 2215 */     if (this.debug)
/* 2216 */       paramPrintStream.println(localObject);
/*      */   }
/*      */ 
/*      */   private void printCertFromStream(InputStream paramInputStream, PrintStream paramPrintStream)
/*      */     throws Exception
/*      */   {
/* 2227 */     Collection localCollection = null;
/*      */     try {
/* 2229 */       localCollection = this.cf.generateCertificates(paramInputStream);
/*      */     } catch (CertificateException localCertificateException) {
/* 2231 */       throw new Exception(rb.getString("Failed.to.parse.input"), localCertificateException);
/*      */     }
/* 2233 */     if (localCollection.isEmpty()) {
/* 2234 */       throw new Exception(rb.getString("Empty.input"));
/*      */     }
/* 2236 */     Certificate[] arrayOfCertificate = (Certificate[])localCollection.toArray(new Certificate[localCollection.size()]);
/* 2237 */     for (int i = 0; i < arrayOfCertificate.length; i++) {
/* 2238 */       X509Certificate localX509Certificate = null;
/*      */       try {
/* 2240 */         localX509Certificate = (X509Certificate)arrayOfCertificate[i];
/*      */       } catch (ClassCastException localClassCastException) {
/* 2242 */         throw new Exception(rb.getString("Not.X.509.certificate"));
/*      */       }
/* 2244 */       if (arrayOfCertificate.length > 1) {
/* 2245 */         MessageFormat localMessageFormat = new MessageFormat(rb.getString("Certificate.i.1."));
/*      */ 
/* 2247 */         Object[] arrayOfObject = { new Integer(i + 1) };
/* 2248 */         paramPrintStream.println(localMessageFormat.format(arrayOfObject));
/*      */       }
/* 2250 */       if (this.rfc) dumpCert(localX509Certificate, paramPrintStream); else
/* 2251 */         printX509Cert(localX509Certificate, paramPrintStream);
/* 2252 */       if (i < arrayOfCertificate.length - 1)
/* 2253 */         paramPrintStream.println();
/*      */     }
/*      */   }
/*      */ 
/*      */   private void doPrintCert(final PrintStream paramPrintStream)
/*      */     throws Exception
/*      */   {
/*      */     Object localObject1;
/*      */     Object localObject2;
/*      */     Object localObject3;
/* 2259 */     if (this.jarfile != null) {
/* 2260 */       localObject1 = new JarFile(this.jarfile, true);
/* 2261 */       localObject2 = ((JarFile)localObject1).entries();
/* 2262 */       localObject3 = new HashSet();
/* 2263 */       byte[] arrayOfByte = new byte[8192];
/* 2264 */       int i = 0;
/* 2265 */       while (((Enumeration)localObject2).hasMoreElements()) {
/* 2266 */         JarEntry localJarEntry = (JarEntry)((Enumeration)localObject2).nextElement();
/* 2267 */         InputStream localInputStream = null;
/*      */         try
/*      */         {
/* 2269 */           localInputStream = ((JarFile)localObject1).getInputStream(localJarEntry);
/* 2270 */           while (localInputStream.read(arrayOfByte) != -1);
/*      */         }
/*      */         finally
/*      */         {
/* 2276 */           if (localInputStream != null) {
/* 2277 */             localInputStream.close();
/*      */           }
/*      */         }
/* 2280 */         CodeSigner[] arrayOfCodeSigner1 = localJarEntry.getCodeSigners();
/* 2281 */         if (arrayOfCodeSigner1 != null)
/*      */         {
/*      */           Object localObject6;
/*      */           Object localObject7;
/* 2282 */           for (CodeSigner localCodeSigner : arrayOfCodeSigner1) {
/* 2283 */             if (!((Set)localObject3).contains(localCodeSigner)) {
/* 2284 */               ((Set)localObject3).add(localCodeSigner);
/* 2285 */               paramPrintStream.printf(rb.getString("Signer.d."), new Object[] { Integer.valueOf(++i) });
/* 2286 */               paramPrintStream.println();
/* 2287 */               paramPrintStream.println();
/* 2288 */               paramPrintStream.println(rb.getString("Signature."));
/* 2289 */               paramPrintStream.println();
/* 2290 */               for (Object localObject5 = localCodeSigner.getSignerCertPath().getCertificates().iterator(); ((Iterator)localObject5).hasNext(); ) { localObject6 = (Certificate)((Iterator)localObject5).next();
/* 2291 */                 localObject7 = (X509Certificate)localObject6;
/* 2292 */                 if (this.rfc) {
/* 2293 */                   paramPrintStream.println(rb.getString("Certificate.owner.") + ((X509Certificate)localObject7).getSubjectDN() + "\n");
/* 2294 */                   dumpCert((Certificate)localObject7, paramPrintStream);
/*      */                 } else {
/* 2296 */                   printX509Cert((X509Certificate)localObject7, paramPrintStream);
/*      */                 }
/* 2298 */                 paramPrintStream.println();
/*      */               }
/* 2300 */               localObject5 = localCodeSigner.getTimestamp();
/* 2301 */               if (localObject5 != null) {
/* 2302 */                 paramPrintStream.println(rb.getString("Timestamp."));
/* 2303 */                 paramPrintStream.println();
/* 2304 */                 for (localObject6 = ((Timestamp)localObject5).getSignerCertPath().getCertificates().iterator(); ((Iterator)localObject6).hasNext(); ) { localObject7 = (Certificate)((Iterator)localObject6).next();
/* 2305 */                   X509Certificate localX509Certificate = (X509Certificate)localObject7;
/* 2306 */                   if (this.rfc) {
/* 2307 */                     paramPrintStream.println(rb.getString("Certificate.owner.") + localX509Certificate.getSubjectDN() + "\n");
/* 2308 */                     dumpCert(localX509Certificate, paramPrintStream);
/*      */                   } else {
/* 2310 */                     printX509Cert(localX509Certificate, paramPrintStream);
/*      */                   }
/* 2312 */                   paramPrintStream.println();
/*      */                 }
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/* 2319 */       ((JarFile)localObject1).close();
/* 2320 */       if (((Set)localObject3).size() == 0)
/* 2321 */         paramPrintStream.println(rb.getString("Not.a.signed.jar.file"));
/*      */     }
/* 2323 */     else if (this.sslserver != null) {
/* 2324 */       localObject1 = SSLContext.getInstance("SSL");
/* 2325 */       localObject2 = new boolean[1];
/* 2326 */       ((SSLContext)localObject1).init(null, new TrustManager[] { new X509ExtendedTrustManager()
/*      */       {
/*      */         public X509Certificate[] getAcceptedIssuers()
/*      */         {
/* 2330 */           return new X509Certificate[0];
/*      */         }
/*      */ 
/*      */         public void checkClientTrusted(X509Certificate[] paramAnonymousArrayOfX509Certificate, String paramAnonymousString)
/*      */         {
/* 2335 */           throw new UnsupportedOperationException();
/*      */         }
/*      */ 
/*      */         public void checkClientTrusted(X509Certificate[] paramAnonymousArrayOfX509Certificate, String paramAnonymousString, Socket paramAnonymousSocket)
/*      */           throws CertificateException
/*      */         {
/* 2341 */           throw new UnsupportedOperationException();
/*      */         }
/*      */ 
/*      */         public void checkClientTrusted(X509Certificate[] paramAnonymousArrayOfX509Certificate, String paramAnonymousString, SSLEngine paramAnonymousSSLEngine)
/*      */           throws CertificateException
/*      */         {
/* 2347 */           throw new UnsupportedOperationException();
/*      */         }
/*      */ 
/*      */         public void checkServerTrusted(X509Certificate[] paramAnonymousArrayOfX509Certificate, String paramAnonymousString)
/*      */         {
/* 2352 */           for (int i = 0; i < paramAnonymousArrayOfX509Certificate.length; i++) {
/* 2353 */             X509Certificate localX509Certificate = paramAnonymousArrayOfX509Certificate[i];
/*      */             try {
/* 2355 */               if (KeyTool.this.rfc) {
/* 2356 */                 KeyTool.this.dumpCert(localX509Certificate, paramPrintStream);
/*      */               } else {
/* 2358 */                 paramPrintStream.println("Certificate #" + i);
/* 2359 */                 paramPrintStream.println("====================================");
/* 2360 */                 KeyTool.this.printX509Cert(localX509Certificate, paramPrintStream);
/* 2361 */                 paramPrintStream.println();
/*      */               }
/*      */             } catch (Exception localException) {
/* 2364 */               if (KeyTool.this.debug) {
/* 2365 */                 localException.printStackTrace();
/*      */               }
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/* 2371 */           if (paramAnonymousArrayOfX509Certificate.length > 0)
/* 2372 */             this.val$certPrinted[0] = true;
/*      */         }
/*      */ 
/*      */         public void checkServerTrusted(X509Certificate[] paramAnonymousArrayOfX509Certificate, String paramAnonymousString, Socket paramAnonymousSocket)
/*      */           throws CertificateException
/*      */         {
/* 2379 */           checkServerTrusted(paramAnonymousArrayOfX509Certificate, paramAnonymousString);
/*      */         }
/*      */ 
/*      */         public void checkServerTrusted(X509Certificate[] paramAnonymousArrayOfX509Certificate, String paramAnonymousString, SSLEngine paramAnonymousSSLEngine)
/*      */           throws CertificateException
/*      */         {
/* 2385 */           checkServerTrusted(paramAnonymousArrayOfX509Certificate, paramAnonymousString);
/*      */         }
/*      */       }
/*      */        }, null);
/*      */ 
/* 2389 */       HttpsURLConnection.setDefaultSSLSocketFactory(((SSLContext)localObject1).getSocketFactory());
/* 2390 */       HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier()
/*      */       {
/*      */         public boolean verify(String paramAnonymousString, SSLSession paramAnonymousSSLSession) {
/* 2393 */           return true;
/*      */         }
/*      */       });
/* 2401 */       localObject3 = null;
/*      */       try {
/* 2403 */         new URL("https://" + this.sslserver).openConnection().connect();
/*      */       } catch (Exception localException1) {
/* 2405 */         localObject3 = localException1;
/*      */       }
/*      */ 
/* 2409 */       if (localObject2[0] == 0) {
/* 2410 */         Exception localException2 = new Exception(rb.getString("No.certificate.from.the.SSL.server"));
/*      */ 
/* 2412 */         if (localObject3 != null) {
/* 2413 */           localException2.initCause((Throwable)localObject3);
/*      */         }
/* 2415 */         throw localException2;
/*      */       }
/*      */     } else {
/* 2418 */       localObject1 = System.in;
/* 2419 */       if (this.filename != null)
/* 2420 */         localObject1 = new FileInputStream(this.filename);
/*      */       try
/*      */       {
/* 2423 */         printCertFromStream((InputStream)localObject1, paramPrintStream);
/*      */ 
/* 2425 */         if (localObject1 != System.in)
/* 2426 */           ((InputStream)localObject1).close();
/*      */       }
/*      */       finally
/*      */       {
/* 2425 */         if (localObject1 != System.in)
/* 2426 */           ((InputStream)localObject1).close();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void doSelfCert(String paramString1, String paramString2, String paramString3)
/*      */     throws Exception
/*      */   {
/* 2438 */     if (paramString1 == null) {
/* 2439 */       paramString1 = "mykey";
/*      */     }
/*      */ 
/* 2442 */     Pair localPair = recoverKey(paramString1, this.storePass, this.keyPass);
/* 2443 */     PrivateKey localPrivateKey = (PrivateKey)localPair.fst;
/* 2444 */     if (this.keyPass == null) {
/* 2445 */       this.keyPass = ((char[])localPair.snd);
/*      */     }
/*      */ 
/* 2448 */     if (paramString3 == null) {
/* 2449 */       paramString3 = getCompatibleSigAlgName(localPrivateKey.getAlgorithm());
/*      */     }
/*      */ 
/* 2453 */     Certificate localCertificate = this.keyStore.getCertificate(paramString1);
/* 2454 */     if (localCertificate == null) {
/* 2455 */       localObject1 = new MessageFormat(rb.getString("alias.has.no.public.key"));
/*      */ 
/* 2457 */       localObject2 = new Object[] { paramString1 };
/* 2458 */       throw new Exception(((MessageFormat)localObject1).format(localObject2));
/*      */     }
/* 2460 */     if (!(localCertificate instanceof X509Certificate)) {
/* 2461 */       localObject1 = new MessageFormat(rb.getString("alias.has.no.X.509.certificate"));
/*      */ 
/* 2463 */       localObject2 = new Object[] { paramString1 };
/* 2464 */       throw new Exception(((MessageFormat)localObject1).format(localObject2));
/*      */     }
/*      */ 
/* 2469 */     Object localObject1 = localCertificate.getEncoded();
/* 2470 */     Object localObject2 = new X509CertImpl((byte[])localObject1);
/* 2471 */     X509CertInfo localX509CertInfo = (X509CertInfo)((X509CertImpl)localObject2).get("x509.info");
/*      */ 
/* 2476 */     Date localDate1 = getStartDate(this.startDate);
/* 2477 */     Date localDate2 = new Date();
/* 2478 */     localDate2.setTime(localDate1.getTime() + this.validity * 1000L * 24L * 60L * 60L);
/* 2479 */     CertificateValidity localCertificateValidity = new CertificateValidity(localDate1, localDate2);
/*      */ 
/* 2481 */     localX509CertInfo.set("validity", localCertificateValidity);
/*      */ 
/* 2484 */     localX509CertInfo.set("serialNumber", new CertificateSerialNumber(new Random().nextInt() & 0x7FFFFFFF));
/*      */     X500Name localX500Name;
/* 2489 */     if (paramString2 == null)
/*      */     {
/* 2491 */       localX500Name = (X500Name)localX509CertInfo.get("subject.dname");
/*      */     }
/*      */     else
/*      */     {
/* 2495 */       localX500Name = new X500Name(paramString2);
/* 2496 */       localX509CertInfo.set("subject.dname", localX500Name);
/*      */     }
/*      */ 
/* 2500 */     localX509CertInfo.set("issuer.dname", localX500Name);
/*      */ 
/* 2507 */     X509CertImpl localX509CertImpl = new X509CertImpl(localX509CertInfo);
/* 2508 */     localX509CertImpl.sign(localPrivateKey, paramString3);
/* 2509 */     AlgorithmId localAlgorithmId = (AlgorithmId)localX509CertImpl.get("x509.algorithm");
/* 2510 */     localX509CertInfo.set("algorithmID.algorithm", localAlgorithmId);
/*      */ 
/* 2513 */     localX509CertInfo.set("version", new CertificateVersion(2));
/*      */ 
/* 2516 */     CertificateExtensions localCertificateExtensions = createV3Extensions(null, (CertificateExtensions)localX509CertInfo.get("extensions"), this.v3ext, localCertificate.getPublicKey(), null);
/*      */ 
/* 2522 */     localX509CertInfo.set("extensions", localCertificateExtensions);
/*      */ 
/* 2524 */     localX509CertImpl = new X509CertImpl(localX509CertInfo);
/* 2525 */     localX509CertImpl.sign(localPrivateKey, paramString3);
/*      */ 
/* 2528 */     this.keyStore.setKeyEntry(paramString1, localPrivateKey, this.keyPass != null ? this.keyPass : this.storePass, new Certificate[] { localX509CertImpl });
/*      */ 
/* 2532 */     if (this.verbose) {
/* 2533 */       System.err.println(rb.getString("New.certificate.self.signed."));
/* 2534 */       System.err.print(localX509CertImpl.toString());
/* 2535 */       System.err.println();
/*      */     }
/*      */   }
/*      */ 
/*      */   private boolean installReply(String paramString, InputStream paramInputStream)
/*      */     throws Exception
/*      */   {
/* 2556 */     if (paramString == null) {
/* 2557 */       paramString = "mykey";
/*      */     }
/*      */ 
/* 2560 */     Pair localPair = recoverKey(paramString, this.storePass, this.keyPass);
/* 2561 */     PrivateKey localPrivateKey = (PrivateKey)localPair.fst;
/* 2562 */     if (this.keyPass == null) {
/* 2563 */       this.keyPass = ((char[])localPair.snd);
/*      */     }
/*      */ 
/* 2566 */     Certificate localCertificate = this.keyStore.getCertificate(paramString);
/* 2567 */     if (localCertificate == null) {
/* 2568 */       localObject1 = new MessageFormat(rb.getString("alias.has.no.public.key.certificate."));
/*      */ 
/* 2570 */       localObject2 = new Object[] { paramString };
/* 2571 */       throw new Exception(((MessageFormat)localObject1).format(localObject2));
/*      */     }
/*      */ 
/* 2575 */     Object localObject1 = this.cf.generateCertificates(paramInputStream);
/* 2576 */     if (((Collection)localObject1).isEmpty()) {
/* 2577 */       throw new Exception(rb.getString("Reply.has.no.certificates"));
/*      */     }
/* 2579 */     Object localObject2 = (Certificate[])((Collection)localObject1).toArray(new Certificate[((Collection)localObject1).size()]);
/*      */     Certificate[] arrayOfCertificate;
/* 2581 */     if (localObject2.length == 1)
/*      */     {
/* 2583 */       arrayOfCertificate = establishCertChain(localCertificate, localObject2[0]);
/*      */     }
/*      */     else {
/* 2586 */       arrayOfCertificate = validateReply(paramString, localCertificate, (Certificate[])localObject2);
/*      */     }
/*      */ 
/* 2591 */     if (arrayOfCertificate != null) {
/* 2592 */       this.keyStore.setKeyEntry(paramString, localPrivateKey, this.keyPass != null ? this.keyPass : this.storePass, arrayOfCertificate);
/*      */ 
/* 2595 */       return true;
/*      */     }
/* 2597 */     return false;
/*      */   }
/*      */ 
/*      */   private boolean addTrustedCert(String paramString, InputStream paramInputStream)
/*      */     throws Exception
/*      */   {
/* 2609 */     if (paramString == null) {
/* 2610 */       throw new Exception(rb.getString("Must.specify.alias"));
/*      */     }
/* 2612 */     if (this.keyStore.containsAlias(paramString)) {
/* 2613 */       localObject1 = new MessageFormat(rb.getString("Certificate.not.imported.alias.alias.already.exists"));
/*      */ 
/* 2615 */       Object[] arrayOfObject1 = { paramString };
/* 2616 */       throw new Exception(((MessageFormat)localObject1).format(arrayOfObject1));
/*      */     }
/*      */ 
/* 2620 */     Object localObject1 = null;
/*      */     try {
/* 2622 */       localObject1 = (X509Certificate)this.cf.generateCertificate(paramInputStream);
/*      */     } catch (ClassCastException localClassCastException) {
/* 2624 */       throw new Exception(rb.getString("Input.not.an.X.509.certificate"));
/*      */     } catch (CertificateException localCertificateException) {
/* 2626 */       throw new Exception(rb.getString("Input.not.an.X.509.certificate"));
/*      */     }
/*      */ 
/* 2630 */     int i = 0;
/* 2631 */     if (isSelfSigned((X509Certificate)localObject1)) {
/* 2632 */       ((X509Certificate)localObject1).verify(((X509Certificate)localObject1).getPublicKey());
/* 2633 */       i = 1;
/*      */     }
/*      */ 
/* 2636 */     if (this.noprompt) {
/* 2637 */       this.keyStore.setCertificateEntry(paramString, (Certificate)localObject1);
/* 2638 */       return true;
/*      */     }
/*      */ 
/* 2642 */     String str1 = null;
/* 2643 */     String str2 = this.keyStore.getCertificateAlias((Certificate)localObject1);
/*      */     Object localObject2;
/*      */     Object[] arrayOfObject2;
/* 2644 */     if (str2 != null) {
/* 2645 */       localObject2 = new MessageFormat(rb.getString("Certificate.already.exists.in.keystore.under.alias.trustalias."));
/*      */ 
/* 2647 */       arrayOfObject2 = new Object[] { str2 };
/* 2648 */       System.err.println(((MessageFormat)localObject2).format(arrayOfObject2));
/* 2649 */       str1 = getYesNoReply(rb.getString("Do.you.still.want.to.add.it.no."));
/*      */     }
/* 2651 */     else if (i != 0) {
/* 2652 */       if ((this.trustcacerts) && (this.caks != null) && ((str2 = this.caks.getCertificateAlias((Certificate)localObject1)) != null))
/*      */       {
/* 2654 */         localObject2 = new MessageFormat(rb.getString("Certificate.already.exists.in.system.wide.CA.keystore.under.alias.trustalias."));
/*      */ 
/* 2656 */         arrayOfObject2 = new Object[] { str2 };
/* 2657 */         System.err.println(((MessageFormat)localObject2).format(arrayOfObject2));
/* 2658 */         str1 = getYesNoReply(rb.getString("Do.you.still.want.to.add.it.to.your.own.keystore.no."));
/*      */       }
/*      */ 
/* 2661 */       if (str2 == null)
/*      */       {
/* 2664 */         printX509Cert((X509Certificate)localObject1, System.out);
/* 2665 */         str1 = getYesNoReply(rb.getString("Trust.this.certificate.no."));
/*      */       }
/*      */     }
/*      */ 
/* 2669 */     if (str1 != null) {
/* 2670 */       if ("YES".equals(str1)) {
/* 2671 */         this.keyStore.setCertificateEntry(paramString, (Certificate)localObject1);
/* 2672 */         return true;
/*      */       }
/* 2674 */       return false;
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/* 2680 */       localObject2 = establishCertChain(null, (Certificate)localObject1);
/* 2681 */       if (localObject2 != null) {
/* 2682 */         this.keyStore.setCertificateEntry(paramString, (Certificate)localObject1);
/* 2683 */         return true;
/*      */       }
/*      */     }
/*      */     catch (Exception localException)
/*      */     {
/* 2688 */       printX509Cert((X509Certificate)localObject1, System.out);
/* 2689 */       str1 = getYesNoReply(rb.getString("Trust.this.certificate.no."));
/*      */ 
/* 2691 */       if ("YES".equals(str1)) {
/* 2692 */         this.keyStore.setCertificateEntry(paramString, (Certificate)localObject1);
/* 2693 */         return true;
/*      */       }
/* 2695 */       return false;
/*      */     }
/*      */ 
/* 2699 */     return false;
/*      */   }
/*      */ 
/*      */   private char[] getNewPasswd(String paramString, char[] paramArrayOfChar)
/*      */     throws Exception
/*      */   {
/* 2712 */     char[] arrayOfChar1 = null;
/* 2713 */     char[] arrayOfChar2 = null;
/*      */ 
/* 2715 */     for (int i = 0; i < 3; i++) {
/* 2716 */       MessageFormat localMessageFormat = new MessageFormat(rb.getString("New.prompt."));
/*      */ 
/* 2718 */       Object[] arrayOfObject1 = { paramString };
/* 2719 */       System.err.print(localMessageFormat.format(arrayOfObject1));
/* 2720 */       arrayOfChar1 = Password.readPassword(System.in);
/* 2721 */       this.passwords.add(arrayOfChar1);
/* 2722 */       if ((arrayOfChar1 == null) || (arrayOfChar1.length < 6)) {
/* 2723 */         System.err.println(rb.getString("Password.is.too.short.must.be.at.least.6.characters"));
/*      */       }
/* 2725 */       else if (Arrays.equals(arrayOfChar1, paramArrayOfChar)) {
/* 2726 */         System.err.println(rb.getString("Passwords.must.differ"));
/*      */       } else {
/* 2728 */         localMessageFormat = new MessageFormat(rb.getString("Re.enter.new.prompt."));
/*      */ 
/* 2730 */         Object[] arrayOfObject2 = { paramString };
/* 2731 */         System.err.print(localMessageFormat.format(arrayOfObject2));
/* 2732 */         arrayOfChar2 = Password.readPassword(System.in);
/* 2733 */         this.passwords.add(arrayOfChar2);
/* 2734 */         if (!Arrays.equals(arrayOfChar1, arrayOfChar2)) {
/* 2735 */           System.err.println(rb.getString("They.don.t.match.Try.again"));
/*      */         }
/*      */         else {
/* 2738 */           Arrays.fill(arrayOfChar2, ' ');
/* 2739 */           return arrayOfChar1;
/*      */         }
/*      */       }
/* 2742 */       if (arrayOfChar1 != null) {
/* 2743 */         Arrays.fill(arrayOfChar1, ' ');
/* 2744 */         arrayOfChar1 = null;
/*      */       }
/* 2746 */       if (arrayOfChar2 != null) {
/* 2747 */         Arrays.fill(arrayOfChar2, ' ');
/* 2748 */         arrayOfChar2 = null;
/*      */       }
/*      */     }
/* 2751 */     throw new Exception(rb.getString("Too.many.failures.try.later"));
/*      */   }
/*      */ 
/*      */   private String getAlias(String paramString)
/*      */     throws Exception
/*      */   {
/* 2760 */     if (paramString != null) {
/* 2761 */       MessageFormat localMessageFormat = new MessageFormat(rb.getString("Enter.prompt.alias.name."));
/*      */ 
/* 2763 */       Object[] arrayOfObject = { paramString };
/* 2764 */       System.err.print(localMessageFormat.format(arrayOfObject));
/*      */     } else {
/* 2766 */       System.err.print(rb.getString("Enter.alias.name."));
/*      */     }
/* 2768 */     return new BufferedReader(new InputStreamReader(System.in)).readLine();
/*      */   }
/*      */ 
/*      */   private String inputStringFromStdin(String paramString)
/*      */     throws Exception
/*      */   {
/* 2778 */     System.err.print(paramString);
/* 2779 */     return new BufferedReader(new InputStreamReader(System.in)).readLine();
/*      */   }
/*      */ 
/*      */   private char[] getKeyPasswd(String paramString1, String paramString2, char[] paramArrayOfChar)
/*      */     throws Exception
/*      */   {
/* 2791 */     int i = 0;
/* 2792 */     char[] arrayOfChar = null;
/*      */     do
/*      */     {
/*      */       MessageFormat localMessageFormat;
/*      */       Object[] arrayOfObject1;
/* 2795 */       if (paramArrayOfChar != null) {
/* 2796 */         localMessageFormat = new MessageFormat(rb.getString("Enter.key.password.for.alias."));
/*      */ 
/* 2798 */         arrayOfObject1 = new Object[] { paramString1 };
/* 2799 */         System.err.println(localMessageFormat.format(arrayOfObject1));
/*      */ 
/* 2801 */         localMessageFormat = new MessageFormat(rb.getString(".RETURN.if.same.as.for.otherAlias."));
/*      */ 
/* 2803 */         Object[] arrayOfObject2 = { paramString2 };
/* 2804 */         System.err.print(localMessageFormat.format(arrayOfObject2));
/*      */       } else {
/* 2806 */         localMessageFormat = new MessageFormat(rb.getString("Enter.key.password.for.alias."));
/*      */ 
/* 2808 */         arrayOfObject1 = new Object[] { paramString1 };
/* 2809 */         System.err.print(localMessageFormat.format(arrayOfObject1));
/*      */       }
/* 2811 */       System.err.flush();
/* 2812 */       arrayOfChar = Password.readPassword(System.in);
/* 2813 */       this.passwords.add(arrayOfChar);
/* 2814 */       if (arrayOfChar == null) {
/* 2815 */         arrayOfChar = paramArrayOfChar;
/*      */       }
/* 2817 */       i++;
/* 2818 */     }while ((arrayOfChar == null) && (i < 3));
/*      */ 
/* 2820 */     if (arrayOfChar == null) {
/* 2821 */       throw new Exception(rb.getString("Too.many.failures.try.later"));
/*      */     }
/*      */ 
/* 2824 */     return arrayOfChar;
/*      */   }
/*      */ 
/*      */   private void printX509Cert(X509Certificate paramX509Certificate, PrintStream paramPrintStream)
/*      */     throws Exception
/*      */   {
/* 2851 */     MessageFormat localMessageFormat = new MessageFormat(rb.getString(".PATTERN.printX509Cert"));
/*      */ 
/* 2853 */     Object[] arrayOfObject = { paramX509Certificate.getSubjectDN().toString(), paramX509Certificate.getIssuerDN().toString(), paramX509Certificate.getSerialNumber().toString(16), paramX509Certificate.getNotBefore().toString(), paramX509Certificate.getNotAfter().toString(), getCertFingerPrint("MD5", paramX509Certificate), getCertFingerPrint("SHA1", paramX509Certificate), getCertFingerPrint("SHA-256", paramX509Certificate), paramX509Certificate.getSigAlgName(), Integer.valueOf(paramX509Certificate.getVersion()) };
/*      */ 
/* 2864 */     paramPrintStream.println(localMessageFormat.format(arrayOfObject));
/*      */ 
/* 2866 */     if ((paramX509Certificate instanceof X509CertImpl)) {
/* 2867 */       X509CertImpl localX509CertImpl = (X509CertImpl)paramX509Certificate;
/* 2868 */       X509CertInfo localX509CertInfo = (X509CertInfo)localX509CertImpl.get("x509.info");
/*      */ 
/* 2871 */       CertificateExtensions localCertificateExtensions = (CertificateExtensions)localX509CertInfo.get("extensions");
/*      */ 
/* 2873 */       if (localCertificateExtensions != null)
/* 2874 */         printExtensions(rb.getString("Extensions."), localCertificateExtensions, paramPrintStream);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static void printExtensions(String paramString, CertificateExtensions paramCertificateExtensions, PrintStream paramPrintStream)
/*      */     throws Exception
/*      */   {
/* 2881 */     int i = 0;
/* 2882 */     Iterator localIterator1 = paramCertificateExtensions.getAllExtensions().iterator();
/* 2883 */     Iterator localIterator2 = paramCertificateExtensions.getUnparseableExtensions().values().iterator();
/* 2884 */     while ((localIterator1.hasNext()) || (localIterator2.hasNext())) {
/* 2885 */       Extension localExtension = localIterator1.hasNext() ? (Extension)localIterator1.next() : (Extension)localIterator2.next();
/* 2886 */       if (i == 0) {
/* 2887 */         paramPrintStream.println();
/* 2888 */         paramPrintStream.println(paramString);
/* 2889 */         paramPrintStream.println();
/*      */       }
/* 2891 */       paramPrintStream.print("#" + ++i + ": " + localExtension);
/* 2892 */       if (localExtension.getClass() == Extension.class) {
/* 2893 */         byte[] arrayOfByte = localExtension.getExtensionValue();
/* 2894 */         if (arrayOfByte.length == 0) {
/* 2895 */           paramPrintStream.println(rb.getString(".Empty.value."));
/*      */         } else {
/* 2897 */           new HexDumpEncoder().encodeBuffer(localExtension.getExtensionValue(), paramPrintStream);
/* 2898 */           paramPrintStream.println();
/*      */         }
/*      */       }
/* 2901 */       paramPrintStream.println();
/*      */     }
/*      */   }
/*      */ 
/*      */   private boolean isSelfSigned(X509Certificate paramX509Certificate)
/*      */   {
/* 2909 */     return signedBy(paramX509Certificate, paramX509Certificate);
/*      */   }
/*      */ 
/*      */   private boolean signedBy(X509Certificate paramX509Certificate1, X509Certificate paramX509Certificate2) {
/* 2913 */     if (!paramX509Certificate2.getSubjectDN().equals(paramX509Certificate1.getIssuerDN()))
/* 2914 */       return false;
/*      */     try
/*      */     {
/* 2917 */       paramX509Certificate1.verify(paramX509Certificate2.getPublicKey());
/* 2918 */       return true; } catch (Exception localException) {
/*      */     }
/* 2920 */     return false;
/*      */   }
/*      */ 
/*      */   private static Certificate getTrustedSigner(Certificate paramCertificate, KeyStore paramKeyStore)
/*      */     throws Exception
/*      */   {
/* 2935 */     if (paramKeyStore.getCertificateAlias(paramCertificate) != null) {
/* 2936 */       return paramCertificate;
/*      */     }
/* 2938 */     Enumeration localEnumeration = paramKeyStore.aliases();
/* 2939 */     while (localEnumeration.hasMoreElements()) {
/* 2940 */       String str = (String)localEnumeration.nextElement();
/* 2941 */       Certificate localCertificate = paramKeyStore.getCertificate(str);
/* 2942 */       if (localCertificate != null)
/*      */         try {
/* 2944 */           paramCertificate.verify(localCertificate.getPublicKey());
/* 2945 */           return localCertificate;
/*      */         }
/*      */         catch (Exception localException)
/*      */         {
/*      */         }
/*      */     }
/* 2951 */     return null; } 
/* 2959 */   private X500Name getX500Name() throws IOException { BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(System.in));
/* 2960 */     String str1 = "Unknown";
/* 2961 */     String str2 = "Unknown";
/* 2962 */     String str3 = "Unknown";
/* 2963 */     String str4 = "Unknown";
/* 2964 */     String str5 = "Unknown";
/* 2965 */     String str6 = "Unknown";
/*      */ 
/* 2967 */     String str7 = null;
/*      */ 
/* 2969 */     int i = 20;
/*      */     X500Name localX500Name;
/*      */     do { if (i-- < 0) {
/* 2972 */         throw new RuntimeException(rb.getString("Too.many.retries.program.terminated"));
/*      */       }
/*      */ 
/* 2975 */       str1 = inputString(localBufferedReader, rb.getString("What.is.your.first.and.last.name."), str1);
/*      */ 
/* 2978 */       str2 = inputString(localBufferedReader, rb.getString("What.is.the.name.of.your.organizational.unit."), str2);
/*      */ 
/* 2982 */       str3 = inputString(localBufferedReader, rb.getString("What.is.the.name.of.your.organization."), str3);
/*      */ 
/* 2985 */       str4 = inputString(localBufferedReader, rb.getString("What.is.the.name.of.your.City.or.Locality."), str4);
/*      */ 
/* 2988 */       str5 = inputString(localBufferedReader, rb.getString("What.is.the.name.of.your.State.or.Province."), str5);
/*      */ 
/* 2991 */       str6 = inputString(localBufferedReader, rb.getString("What.is.the.two.letter.country.code.for.this.unit."), str6);
/*      */ 
/* 2995 */       localX500Name = new X500Name(str1, str2, str3, str4, str5, str6);
/*      */ 
/* 2997 */       MessageFormat localMessageFormat = new MessageFormat(rb.getString("Is.name.correct."));
/*      */ 
/* 2999 */       Object[] arrayOfObject = { localX500Name };
/* 3000 */       str7 = inputString(localBufferedReader, localMessageFormat.format(arrayOfObject), rb.getString("no"));
/*      */     }
/* 3002 */     while ((collator.compare(str7, rb.getString("yes")) != 0) && (collator.compare(str7, rb.getString("y")) != 0));
/*      */ 
/* 3005 */     System.err.println();
/* 3006 */     return localX500Name;
/*      */   }
/*      */ 
/*      */   private String inputString(BufferedReader paramBufferedReader, String paramString1, String paramString2)
/*      */     throws IOException
/*      */   {
/* 3013 */     System.err.println(paramString1);
/* 3014 */     MessageFormat localMessageFormat = new MessageFormat(rb.getString(".defaultValue."));
/*      */ 
/* 3016 */     Object[] arrayOfObject = { paramString2 };
/* 3017 */     System.err.print(localMessageFormat.format(arrayOfObject));
/* 3018 */     System.err.flush();
/*      */ 
/* 3020 */     String str = paramBufferedReader.readLine();
/* 3021 */     if ((str == null) || (collator.compare(str, "") == 0)) {
/* 3022 */       str = paramString2;
/*      */     }
/* 3024 */     return str;
/*      */   }
/*      */ 
/*      */   private void dumpCert(Certificate paramCertificate, PrintStream paramPrintStream)
/*      */     throws IOException, CertificateException
/*      */   {
/* 3034 */     if (this.rfc) {
/* 3035 */       BASE64Encoder localBASE64Encoder = new BASE64Encoder();
/* 3036 */       paramPrintStream.println("-----BEGIN CERTIFICATE-----");
/* 3037 */       localBASE64Encoder.encodeBuffer(paramCertificate.getEncoded(), paramPrintStream);
/* 3038 */       paramPrintStream.println("-----END CERTIFICATE-----");
/*      */     } else {
/* 3040 */       paramPrintStream.write(paramCertificate.getEncoded());
/*      */     }
/*      */   }
/*      */ 
/*      */   private void byte2hex(byte paramByte, StringBuffer paramStringBuffer)
/*      */   {
/* 3048 */     char[] arrayOfChar = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
/*      */ 
/* 3050 */     int i = (paramByte & 0xF0) >> 4;
/* 3051 */     int j = paramByte & 0xF;
/* 3052 */     paramStringBuffer.append(arrayOfChar[i]);
/* 3053 */     paramStringBuffer.append(arrayOfChar[j]);
/*      */   }
/*      */ 
/*      */   private String toHexString(byte[] paramArrayOfByte)
/*      */   {
/* 3060 */     StringBuffer localStringBuffer = new StringBuffer();
/* 3061 */     int i = paramArrayOfByte.length;
/* 3062 */     for (int j = 0; j < i; j++) {
/* 3063 */       byte2hex(paramArrayOfByte[j], localStringBuffer);
/* 3064 */       if (j < i - 1) {
/* 3065 */         localStringBuffer.append(":");
/*      */       }
/*      */     }
/* 3068 */     return localStringBuffer.toString();
/*      */   }
/*      */ 
/*      */   private Pair<Key, char[]> recoverKey(String paramString, char[] paramArrayOfChar1, char[] paramArrayOfChar2)
/*      */     throws Exception
/*      */   {
/* 3082 */     Key localKey = null;
/*      */     MessageFormat localMessageFormat;
/*      */     Object[] arrayOfObject;
/* 3084 */     if (!this.keyStore.containsAlias(paramString)) {
/* 3085 */       localMessageFormat = new MessageFormat(rb.getString("Alias.alias.does.not.exist"));
/*      */ 
/* 3087 */       arrayOfObject = new Object[] { paramString };
/* 3088 */       throw new Exception(localMessageFormat.format(arrayOfObject));
/*      */     }
/* 3090 */     if ((!this.keyStore.entryInstanceOf(paramString, KeyStore.PrivateKeyEntry.class)) && (!this.keyStore.entryInstanceOf(paramString, KeyStore.SecretKeyEntry.class)))
/*      */     {
/* 3092 */       localMessageFormat = new MessageFormat(rb.getString("Alias.alias.has.no.key"));
/*      */ 
/* 3094 */       arrayOfObject = new Object[] { paramString };
/* 3095 */       throw new Exception(localMessageFormat.format(arrayOfObject));
/*      */     }
/*      */ 
/* 3098 */     if (paramArrayOfChar2 == null)
/*      */       try
/*      */       {
/* 3101 */         localKey = this.keyStore.getKey(paramString, paramArrayOfChar1);
/*      */ 
/* 3103 */         paramArrayOfChar2 = paramArrayOfChar1;
/* 3104 */         this.passwords.add(paramArrayOfChar2);
/*      */       }
/*      */       catch (UnrecoverableKeyException localUnrecoverableKeyException) {
/* 3107 */         if (!this.token) {
/* 3108 */           paramArrayOfChar2 = getKeyPasswd(paramString, null, null);
/* 3109 */           localKey = this.keyStore.getKey(paramString, paramArrayOfChar2);
/*      */         } else {
/* 3111 */           throw localUnrecoverableKeyException;
/*      */         }
/*      */       }
/*      */     else {
/* 3115 */       localKey = this.keyStore.getKey(paramString, paramArrayOfChar2);
/*      */     }
/*      */ 
/* 3118 */     return Pair.of(localKey, paramArrayOfChar2);
/*      */   }
/*      */ 
/*      */   private Pair<KeyStore.Entry, char[]> recoverEntry(KeyStore paramKeyStore, String paramString, char[] paramArrayOfChar1, char[] paramArrayOfChar2)
/*      */     throws Exception
/*      */   {
/*      */     Object localObject2;
/* 3133 */     if (!paramKeyStore.containsAlias(paramString)) {
/* 3134 */       localObject1 = new MessageFormat(rb.getString("Alias.alias.does.not.exist"));
/*      */ 
/* 3136 */       localObject2 = new Object[] { paramString };
/* 3137 */       throw new Exception(((MessageFormat)localObject1).format(localObject2));
/*      */     }
/*      */ 
/* 3140 */     Object localObject1 = null;
/*      */     try
/*      */     {
/* 3147 */       localObject2 = paramKeyStore.getEntry(paramString, (KeyStore.ProtectionParameter)localObject1);
/* 3148 */       paramArrayOfChar2 = null;
/*      */     }
/*      */     catch (UnrecoverableEntryException localUnrecoverableEntryException1) {
/* 3151 */       if (("PKCS11".equalsIgnoreCase(paramKeyStore.getType())) || (KeyStoreUtil.isWindowsKeyStore(paramKeyStore.getType())))
/*      */       {
/* 3154 */         throw localUnrecoverableEntryException1;
/*      */       }
/*      */ 
/* 3159 */       if (paramArrayOfChar2 != null)
/*      */       {
/* 3163 */         localObject1 = new KeyStore.PasswordProtection(paramArrayOfChar2);
/* 3164 */         localObject2 = paramKeyStore.getEntry(paramString, (KeyStore.ProtectionParameter)localObject1);
/*      */       }
/*      */       else
/*      */       {
/*      */         try
/*      */         {
/* 3171 */           localObject1 = new KeyStore.PasswordProtection(paramArrayOfChar1);
/* 3172 */           localObject2 = paramKeyStore.getEntry(paramString, (KeyStore.ProtectionParameter)localObject1);
/* 3173 */           paramArrayOfChar2 = paramArrayOfChar1;
/*      */         } catch (UnrecoverableEntryException localUnrecoverableEntryException2) {
/* 3175 */           if ("PKCS12".equalsIgnoreCase(paramKeyStore.getType()))
/*      */           {
/* 3180 */             throw localUnrecoverableEntryException2;
/*      */           }
/*      */ 
/* 3185 */           paramArrayOfChar2 = getKeyPasswd(paramString, null, null);
/* 3186 */           localObject1 = new KeyStore.PasswordProtection(paramArrayOfChar2);
/* 3187 */           localObject2 = paramKeyStore.getEntry(paramString, (KeyStore.ProtectionParameter)localObject1);
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 3193 */     return Pair.of(localObject2, paramArrayOfChar2);
/*      */   }
/*      */ 
/*      */   private String getCertFingerPrint(String paramString, Certificate paramCertificate)
/*      */     throws Exception
/*      */   {
/* 3201 */     byte[] arrayOfByte1 = paramCertificate.getEncoded();
/* 3202 */     MessageDigest localMessageDigest = MessageDigest.getInstance(paramString);
/* 3203 */     byte[] arrayOfByte2 = localMessageDigest.digest(arrayOfByte1);
/* 3204 */     return toHexString(arrayOfByte2);
/*      */   }
/*      */ 
/*      */   private void printWarning()
/*      */   {
/* 3211 */     System.err.println();
/* 3212 */     System.err.println(rb.getString(".WARNING.WARNING.WARNING."));
/*      */ 
/* 3214 */     System.err.println(rb.getString(".The.integrity.of.the.information.stored.in.your.keystore."));
/*      */ 
/* 3216 */     System.err.println(rb.getString(".WARNING.WARNING.WARNING."));
/*      */ 
/* 3218 */     System.err.println();
/*      */   }
/*      */ 
/*      */   private Certificate[] validateReply(String paramString, Certificate paramCertificate, Certificate[] paramArrayOfCertificate)
/*      */     throws Exception
/*      */   {
/* 3239 */     PublicKey localPublicKey = paramCertificate.getPublicKey();
/* 3240 */     for (int i = 0; (i < paramArrayOfCertificate.length) && 
/* 3241 */       (!localPublicKey.equals(paramArrayOfCertificate[i].getPublicKey())); i++);
/* 3245 */     if (i == paramArrayOfCertificate.length) {
/* 3246 */       localObject1 = new MessageFormat(rb.getString("Certificate.reply.does.not.contain.public.key.for.alias."));
/*      */ 
/* 3248 */       localObject2 = new Object[] { paramString };
/* 3249 */       throw new Exception(((MessageFormat)localObject1).format(localObject2));
/*      */     }
/*      */ 
/* 3252 */     Object localObject1 = paramArrayOfCertificate[0];
/* 3253 */     paramArrayOfCertificate[0] = paramArrayOfCertificate[i];
/* 3254 */     paramArrayOfCertificate[i] = localObject1;
/*      */ 
/* 3256 */     Object localObject2 = (X509Certificate)paramArrayOfCertificate[0];
/*      */ 
/* 3258 */     for (i = 1; i < paramArrayOfCertificate.length - 1; i++)
/*      */     {
/* 3261 */       for (int j = i; j < paramArrayOfCertificate.length; j++) {
/* 3262 */         if (signedBy((X509Certificate)localObject2, (X509Certificate)paramArrayOfCertificate[j])) {
/* 3263 */           localObject1 = paramArrayOfCertificate[i];
/* 3264 */           paramArrayOfCertificate[i] = paramArrayOfCertificate[j];
/* 3265 */           paramArrayOfCertificate[j] = localObject1;
/* 3266 */           localObject2 = (X509Certificate)paramArrayOfCertificate[i];
/* 3267 */           break;
/*      */         }
/*      */       }
/* 3270 */       if (j == paramArrayOfCertificate.length) {
/* 3271 */         throw new Exception(rb.getString("Incomplete.certificate.chain.in.reply"));
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 3276 */     if (this.noprompt) {
/* 3277 */       return paramArrayOfCertificate;
/*      */     }
/*      */ 
/* 3281 */     Certificate localCertificate1 = paramArrayOfCertificate[(paramArrayOfCertificate.length - 1)];
/* 3282 */     Certificate localCertificate2 = getTrustedSigner(localCertificate1, this.keyStore);
/* 3283 */     if ((localCertificate2 == null) && (this.trustcacerts) && (this.caks != null))
/* 3284 */       localCertificate2 = getTrustedSigner(localCertificate1, this.caks);
/*      */     Object localObject3;
/* 3286 */     if (localCertificate2 == null) {
/* 3287 */       System.err.println();
/* 3288 */       System.err.println(rb.getString("Top.level.certificate.in.reply."));
/*      */ 
/* 3290 */       printX509Cert((X509Certificate)localCertificate1, System.out);
/* 3291 */       System.err.println();
/* 3292 */       System.err.print(rb.getString(".is.not.trusted."));
/* 3293 */       localObject3 = getYesNoReply(rb.getString("Install.reply.anyway.no."));
/*      */ 
/* 3295 */       if ("NO".equals(localObject3)) {
/* 3296 */         return null;
/*      */       }
/*      */     }
/* 3299 */     else if (localCertificate2 != localCertificate1)
/*      */     {
/* 3301 */       localObject3 = new Certificate[paramArrayOfCertificate.length + 1];
/*      */ 
/* 3303 */       System.arraycopy(paramArrayOfCertificate, 0, localObject3, 0, paramArrayOfCertificate.length);
/*      */ 
/* 3305 */       localObject3[(localObject3.length - 1)] = localCertificate2;
/* 3306 */       paramArrayOfCertificate = (Certificate[])localObject3;
/*      */     }
/*      */ 
/* 3310 */     return paramArrayOfCertificate;
/*      */   }
/*      */ 
/*      */   private Certificate[] establishCertChain(Certificate paramCertificate1, Certificate paramCertificate2)
/*      */     throws Exception
/*      */   {
/* 3325 */     if (paramCertificate1 != null)
/*      */     {
/* 3328 */       localObject1 = paramCertificate1.getPublicKey();
/* 3329 */       localObject2 = paramCertificate2.getPublicKey();
/* 3330 */       if (!localObject1.equals(localObject2)) {
/* 3331 */         throw new Exception(rb.getString("Public.keys.in.reply.and.keystore.don.t.match"));
/*      */       }
/*      */ 
/* 3337 */       if (paramCertificate2.equals(paramCertificate1)) {
/* 3338 */         throw new Exception(rb.getString("Certificate.reply.and.certificate.in.keystore.are.identical"));
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 3347 */     Object localObject1 = null;
/* 3348 */     if (this.keyStore.size() > 0) {
/* 3349 */       localObject1 = new Hashtable(11);
/* 3350 */       keystorecerts2Hashtable(this.keyStore, (Hashtable)localObject1);
/*      */     }
/* 3352 */     if ((this.trustcacerts) && 
/* 3353 */       (this.caks != null) && (this.caks.size() > 0)) {
/* 3354 */       if (localObject1 == null) {
/* 3355 */         localObject1 = new Hashtable(11);
/*      */       }
/* 3357 */       keystorecerts2Hashtable(this.caks, (Hashtable)localObject1);
/*      */     }
/*      */ 
/* 3362 */     Object localObject2 = new Vector(2);
/* 3363 */     if (buildChain((X509Certificate)paramCertificate2, (Vector)localObject2, (Hashtable)localObject1)) {
/* 3364 */       Certificate[] arrayOfCertificate = new Certificate[((Vector)localObject2).size()];
/*      */ 
/* 3368 */       int i = 0;
/* 3369 */       for (int j = ((Vector)localObject2).size() - 1; j >= 0; j--) {
/* 3370 */         arrayOfCertificate[i] = ((Certificate)((Vector)localObject2).elementAt(j));
/* 3371 */         i++;
/*      */       }
/* 3373 */       return arrayOfCertificate;
/*      */     }
/* 3375 */     throw new Exception(rb.getString("Failed.to.establish.chain.from.reply"));
/*      */   }
/*      */ 
/*      */   private boolean buildChain(X509Certificate paramX509Certificate, Vector<Certificate> paramVector, Hashtable<Principal, Vector<Certificate>> paramHashtable)
/*      */   {
/* 3392 */     Principal localPrincipal = paramX509Certificate.getIssuerDN();
/* 3393 */     if (isSelfSigned(paramX509Certificate))
/*      */     {
/* 3396 */       paramVector.addElement(paramX509Certificate);
/* 3397 */       return true;
/*      */     }
/*      */ 
/* 3401 */     Vector localVector = (Vector)paramHashtable.get(localPrincipal);
/* 3402 */     if (localVector == null) {
/* 3403 */       return false;
/*      */     }
/*      */ 
/* 3409 */     Enumeration localEnumeration = localVector.elements();
/* 3410 */     while (localEnumeration.hasMoreElements()) {
/* 3411 */       X509Certificate localX509Certificate = (X509Certificate)localEnumeration.nextElement();
/*      */ 
/* 3413 */       PublicKey localPublicKey = localX509Certificate.getPublicKey();
/*      */       try {
/* 3415 */         paramX509Certificate.verify(localPublicKey); } catch (Exception localException) {
/*      */       }
/* 3417 */       continue;
/*      */ 
/* 3419 */       if (buildChain(localX509Certificate, paramVector, paramHashtable)) {
/* 3420 */         paramVector.addElement(paramX509Certificate);
/* 3421 */         return true;
/*      */       }
/*      */     }
/* 3424 */     return false;
/*      */   }
/*      */ 
/*      */   private String getYesNoReply(String paramString)
/*      */     throws IOException
/*      */   {
/* 3435 */     String str = null;
/* 3436 */     int i = 20;
/*      */     do {
/* 3438 */       if (i-- < 0) {
/* 3439 */         throw new RuntimeException(rb.getString("Too.many.retries.program.terminated"));
/*      */       }
/*      */ 
/* 3442 */       System.err.print(paramString);
/* 3443 */       System.err.flush();
/* 3444 */       str = new BufferedReader(new InputStreamReader(System.in)).readLine();
/*      */ 
/* 3446 */       if ((collator.compare(str, "") == 0) || (collator.compare(str, rb.getString("n")) == 0) || (collator.compare(str, rb.getString("no")) == 0))
/*      */       {
/* 3449 */         str = "NO";
/* 3450 */       } else if ((collator.compare(str, rb.getString("y")) == 0) || (collator.compare(str, rb.getString("yes")) == 0))
/*      */       {
/* 3452 */         str = "YES";
/*      */       } else {
/* 3454 */         System.err.println(rb.getString("Wrong.answer.try.again"));
/* 3455 */         str = null;
/*      */       }
/*      */     }
/* 3457 */     while (str == null);
/* 3458 */     return str;
/*      */   }
/*      */ 
/*      */   public static KeyStore getCacertsKeyStore()
/*      */     throws Exception
/*      */   {
/* 3467 */     String str = File.separator;
/* 3468 */     File localFile = new File(System.getProperty("java.home") + str + "lib" + str + "security" + str + "cacerts");
/*      */ 
/* 3471 */     if (!localFile.exists()) {
/* 3472 */       return null;
/*      */     }
/* 3474 */     FileInputStream localFileInputStream = null;
/* 3475 */     KeyStore localKeyStore = null;
/*      */     try {
/* 3477 */       localFileInputStream = new FileInputStream(localFile);
/* 3478 */       localKeyStore = KeyStore.getInstance("jks");
/* 3479 */       localKeyStore.load(localFileInputStream, null);
/*      */     } finally {
/* 3481 */       if (localFileInputStream != null) {
/* 3482 */         localFileInputStream.close();
/*      */       }
/*      */     }
/* 3485 */     return localKeyStore;
/*      */   }
/*      */ 
/*      */   private void keystorecerts2Hashtable(KeyStore paramKeyStore, Hashtable<Principal, Vector<Certificate>> paramHashtable)
/*      */     throws Exception
/*      */   {
/* 3497 */     Enumeration localEnumeration = paramKeyStore.aliases();
/* 3498 */     while (localEnumeration.hasMoreElements()) {
/* 3499 */       String str = (String)localEnumeration.nextElement();
/* 3500 */       Certificate localCertificate = paramKeyStore.getCertificate(str);
/* 3501 */       if (localCertificate != null) {
/* 3502 */         Principal localPrincipal = ((X509Certificate)localCertificate).getSubjectDN();
/* 3503 */         Vector localVector = (Vector)paramHashtable.get(localPrincipal);
/* 3504 */         if (localVector == null) {
/* 3505 */           localVector = new Vector();
/* 3506 */           localVector.addElement(localCertificate);
/*      */         }
/* 3508 */         else if (!localVector.contains(localCertificate)) {
/* 3509 */           localVector.addElement(localCertificate);
/*      */         }
/*      */ 
/* 3512 */         paramHashtable.put(localPrincipal, localVector);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private static Date getStartDate(String paramString)
/*      */     throws IOException
/*      */   {
/* 3522 */     GregorianCalendar localGregorianCalendar = new GregorianCalendar();
/* 3523 */     if (paramString != null) {
/* 3524 */       IOException localIOException = new IOException(rb.getString("Illegal.startdate.value"));
/*      */ 
/* 3526 */       int i = paramString.length();
/* 3527 */       if (i == 0) {
/* 3528 */         throw localIOException;
/*      */       }
/* 3530 */       if ((paramString.charAt(0) == '-') || (paramString.charAt(0) == '+'))
/*      */       {
/* 3532 */         int j = 0;
/* 3533 */         while (j < i) {
/* 3534 */           int k = 0;
/* 3535 */           switch (paramString.charAt(j)) { case '+':
/* 3536 */             k = 1; break;
/*      */           case '-':
/* 3537 */             k = -1; break;
/*      */           default:
/* 3538 */             throw localIOException;
/*      */           }
/* 3540 */           for (int m = j + 1; 
/* 3541 */             m < i; m++) {
/* 3542 */             n = paramString.charAt(m);
/* 3543 */             if ((n < 48) || (n > 57)) break;
/*      */           }
/* 3545 */           if (m == j + 1) throw localIOException;
/* 3546 */           int n = Integer.parseInt(paramString.substring(j + 1, m));
/* 3547 */           if (m >= i) throw localIOException;
/* 3548 */           int i1 = 0;
/* 3549 */           switch (paramString.charAt(m)) { case 'y':
/* 3550 */             i1 = 1; break;
/*      */           case 'm':
/* 3551 */             i1 = 2; break;
/*      */           case 'd':
/* 3552 */             i1 = 5; break;
/*      */           case 'H':
/* 3553 */             i1 = 10; break;
/*      */           case 'M':
/* 3554 */             i1 = 12; break;
/*      */           case 'S':
/* 3555 */             i1 = 13; break;
/*      */           default:
/* 3556 */             throw localIOException;
/*      */           }
/* 3558 */           localGregorianCalendar.add(i1, k * n);
/* 3559 */           j = m + 1;
/*      */         }
/*      */       }
/*      */       else {
/* 3563 */         String str1 = null; String str2 = null;
/* 3564 */         if (i == 19) {
/* 3565 */           str1 = paramString.substring(0, 10);
/* 3566 */           str2 = paramString.substring(11);
/* 3567 */           if (paramString.charAt(10) != ' ')
/* 3568 */             throw localIOException;
/* 3569 */         } else if (i == 10) {
/* 3570 */           str1 = paramString;
/* 3571 */         } else if (i == 8) {
/* 3572 */           str2 = paramString;
/*      */         } else {
/* 3574 */           throw localIOException;
/*      */         }
/* 3576 */         if (str1 != null) {
/* 3577 */           if (str1.matches("\\d\\d\\d\\d\\/\\d\\d\\/\\d\\d")) {
/* 3578 */             localGregorianCalendar.set(Integer.valueOf(str1.substring(0, 4)).intValue(), Integer.valueOf(str1.substring(5, 7)).intValue() - 1, Integer.valueOf(str1.substring(8, 10)).intValue());
/*      */           }
/*      */           else
/*      */           {
/* 3582 */             throw localIOException;
/*      */           }
/*      */         }
/* 3585 */         if (str2 != null) {
/* 3586 */           if (str2.matches("\\d\\d:\\d\\d:\\d\\d")) {
/* 3587 */             localGregorianCalendar.set(11, Integer.valueOf(str2.substring(0, 2)).intValue());
/* 3588 */             localGregorianCalendar.set(12, Integer.valueOf(str2.substring(0, 2)).intValue());
/* 3589 */             localGregorianCalendar.set(13, Integer.valueOf(str2.substring(0, 2)).intValue());
/* 3590 */             localGregorianCalendar.set(14, 0);
/*      */           } else {
/* 3592 */             throw localIOException;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 3597 */     return localGregorianCalendar.getTime();
/*      */   }
/*      */ 
/*      */   private static int oneOf(String paramString, String[] paramArrayOfString)
/*      */     throws Exception
/*      */   {
/* 3610 */     int[] arrayOfInt = new int[paramArrayOfString.length];
/* 3611 */     int i = 0;
/* 3612 */     int j = 2147483647;
/* 3613 */     for (int k = 0; k < paramArrayOfString.length; k++) {
/* 3614 */       localObject1 = paramArrayOfString[k];
/* 3615 */       if (localObject1 == null) {
/* 3616 */         j = k;
/*      */       }
/* 3619 */       else if (((String)localObject1).toLowerCase(Locale.ENGLISH).startsWith(paramString.toLowerCase(Locale.ENGLISH)))
/*      */       {
/* 3621 */         arrayOfInt[(i++)] = k;
/*      */       } else {
/* 3623 */         localObject2 = new StringBuffer();
/* 3624 */         m = 1;
/* 3625 */         for (char c : ((String)localObject1).toCharArray()) {
/* 3626 */           if (m != 0) {
/* 3627 */             ((StringBuffer)localObject2).append(c);
/* 3628 */             m = 0;
/*      */           }
/* 3630 */           else if (!Character.isLowerCase(c)) {
/* 3631 */             ((StringBuffer)localObject2).append(c);
/*      */           }
/*      */         }
/*      */ 
/* 3635 */         if (((StringBuffer)localObject2).toString().equalsIgnoreCase(paramString)) {
/* 3636 */           arrayOfInt[(i++)] = k;
/*      */         }
/*      */       }
/*      */     }
/* 3640 */     if (i == 0)
/* 3641 */       return -1;
/* 3642 */     if (i == 1) {
/* 3643 */       return arrayOfInt[0];
/*      */     }
/*      */ 
/* 3646 */     if (arrayOfInt[1] > j) {
/* 3647 */       return arrayOfInt[0];
/*      */     }
/* 3649 */     StringBuffer localStringBuffer = new StringBuffer();
/* 3650 */     Object localObject1 = new MessageFormat(rb.getString("command.{0}.is.ambiguous."));
/*      */ 
/* 3652 */     Object localObject2 = { paramString };
/* 3653 */     localStringBuffer.append(((MessageFormat)localObject1).format(localObject2));
/* 3654 */     localStringBuffer.append("\n    ");
/* 3655 */     for (int m = 0; (m < i) && (arrayOfInt[m] < j); m++) {
/* 3656 */       localStringBuffer.append(' ');
/* 3657 */       localStringBuffer.append(paramArrayOfString[arrayOfInt[m]]);
/*      */     }
/* 3659 */     throw new Exception(localStringBuffer.toString());
/*      */   }
/*      */ 
/*      */   private GeneralName createGeneralName(String paramString1, String paramString2)
/*      */     throws Exception
/*      */   {
/* 3672 */     int i = oneOf(paramString1, new String[] { "EMAIL", "URI", "DNS", "IP", "OID" });
/* 3673 */     if (i < 0)
/* 3674 */       throw new Exception(rb.getString("Unrecognized.GeneralName.type.") + paramString1);
/*      */     Object localObject;
/* 3677 */     switch (i) { case 0:
/* 3678 */       localObject = new RFC822Name(paramString2); break;
/*      */     case 1:
/* 3679 */       localObject = new URIName(paramString2); break;
/*      */     case 2:
/* 3680 */       localObject = new DNSName(paramString2); break;
/*      */     case 3:
/* 3681 */       localObject = new IPAddressName(paramString2); break;
/*      */     default:
/* 3682 */       localObject = new OIDName(paramString2);
/*      */     }
/* 3684 */     return new GeneralName((GeneralNameInterface)localObject);
/*      */   }
/*      */ 
/*      */   private ObjectIdentifier findOidForExtName(String paramString)
/*      */     throws Exception
/*      */   {
/* 3701 */     switch (oneOf(paramString, extSupported)) { case 0:
/* 3702 */       return PKIXExtensions.BasicConstraints_Id;
/*      */     case 1:
/* 3703 */       return PKIXExtensions.KeyUsage_Id;
/*      */     case 2:
/* 3704 */       return PKIXExtensions.ExtendedKeyUsage_Id;
/*      */     case 3:
/* 3705 */       return PKIXExtensions.SubjectAlternativeName_Id;
/*      */     case 4:
/* 3706 */       return PKIXExtensions.IssuerAlternativeName_Id;
/*      */     case 5:
/* 3707 */       return PKIXExtensions.SubjectInfoAccess_Id;
/*      */     case 6:
/* 3708 */       return PKIXExtensions.AuthInfoAccess_Id;
/*      */     case 8:
/* 3709 */       return PKIXExtensions.CRLDistributionPoints_Id;
/* 3710 */     case 7: } return new ObjectIdentifier(paramString);
/*      */   }
/*      */ 
/*      */   private CertificateExtensions createV3Extensions(CertificateExtensions paramCertificateExtensions1, CertificateExtensions paramCertificateExtensions2, List<String> paramList, PublicKey paramPublicKey1, PublicKey paramPublicKey2)
/*      */     throws Exception
/*      */   {
/* 3733 */     if ((paramCertificateExtensions2 != null) && (paramCertificateExtensions1 != null))
/*      */     {
/* 3735 */       throw new Exception("One of request and original should be null.");
/*      */     }
/* 3737 */     if (paramCertificateExtensions2 == null) paramCertificateExtensions2 = new CertificateExtensions();
/*      */ 
/*      */     try
/*      */     {
/* 3741 */       if (paramCertificateExtensions1 != null)
/* 3742 */         for (localIterator = paramList.iterator(); localIterator.hasNext(); ) { str1 = (String)localIterator.next();
/* 3743 */           if (str1.toLowerCase(Locale.ENGLISH).startsWith("honored=")) {
/* 3744 */             localObject1 = Arrays.asList(str1.toLowerCase(Locale.ENGLISH).substring(8).split(","));
/*      */ 
/* 3747 */             if (((List)localObject1).contains("all")) {
/* 3748 */               paramCertificateExtensions2 = paramCertificateExtensions1;
/*      */             }
/*      */ 
/* 3751 */             for (localObject2 = ((List)localObject1).iterator(); ((Iterator)localObject2).hasNext(); ) { String str2 = (String)((Iterator)localObject2).next();
/* 3752 */               if (!str2.equals("all"))
/*      */               {
/* 3755 */                 i = 1;
/*      */ 
/* 3757 */                 j = -1;
/* 3758 */                 String str3 = null;
/* 3759 */                 if (str2.startsWith("-")) {
/* 3760 */                   i = 0;
/* 3761 */                   str3 = str2.substring(1);
/*      */                 } else {
/* 3763 */                   int m = str2.indexOf(':');
/* 3764 */                   if (m >= 0) {
/* 3765 */                     str3 = str2.substring(0, m);
/* 3766 */                     j = oneOf(str2.substring(m + 1), new String[] { "critical", "non-critical" });
/*      */ 
/* 3768 */                     if (j == -1) {
/* 3769 */                       throw new Exception(rb.getString("Illegal.value.") + str2);
/*      */                     }
/*      */                   }
/*      */                 }
/*      */ 
/* 3774 */                 String str4 = paramCertificateExtensions1.getNameByOid(findOidForExtName(str3));
/* 3775 */                 if (i != 0) {
/* 3776 */                   Extension localExtension = (Extension)paramCertificateExtensions1.get(str4);
/* 3777 */                   if (((!localExtension.isCritical()) && (j == 0)) || ((localExtension.isCritical()) && (j == 1)))
/*      */                   {
/* 3779 */                     localExtension = Extension.newExtension(localExtension.getExtensionId(), !localExtension.isCritical(), localExtension.getExtensionValue());
/*      */ 
/* 3783 */                     paramCertificateExtensions2.set(str4, localExtension);
/*      */                   }
/*      */                 } else {
/* 3786 */                   paramCertificateExtensions2.delete(str4);
/*      */                 }
/*      */               } }
/* 3789 */             break;
/*      */           }
/*      */         }
/* 3793 */       String str1;
/*      */       Object localObject1;
/*      */       Object localObject2;
/*      */       int i;
/*      */       int j;
/* 3793 */       for (Iterator localIterator = paramList.iterator(); localIterator.hasNext(); ) { str1 = (String)localIterator.next();
/*      */ 
/* 3795 */         boolean bool1 = false;
/*      */ 
/* 3797 */         i = str1.indexOf('=');
/* 3798 */         if (i >= 0) {
/* 3799 */           localObject1 = str1.substring(0, i);
/* 3800 */           localObject2 = str1.substring(i + 1);
/*      */         } else {
/* 3802 */           localObject1 = str1;
/* 3803 */           localObject2 = null;
/*      */         }
/*      */ 
/* 3806 */         j = ((String)localObject1).indexOf(':');
/* 3807 */         if (j >= 0) {
/* 3808 */           if (oneOf(((String)localObject1).substring(j + 1), new String[] { "critical" }) == 0) {
/* 3809 */             bool1 = true;
/*      */           }
/* 3811 */           localObject1 = ((String)localObject1).substring(0, j);
/*      */         }
/*      */ 
/* 3814 */         if (!((String)localObject1).equalsIgnoreCase("honored"))
/*      */         {
/* 3817 */           int k = oneOf((String)localObject1, extSupported);
/*      */           Object localObject4;
/*      */           int i1;
/*      */           int i3;
/*      */           String str5;
/*      */           Object localObject3;
/*      */           int i5;
/*      */           String str6;
/*      */           String str9;
/* 3818 */           switch (k) {
/*      */           case 0:
/* 3820 */             int n = -1;
/* 3821 */             boolean bool2 = false;
/* 3822 */             if (localObject2 == null) {
/* 3823 */               bool2 = true;
/*      */             } else {
/*      */               try {
/* 3826 */                 n = Integer.parseInt((String)localObject2);
/* 3827 */                 bool2 = true;
/*      */               }
/*      */               catch (NumberFormatException localNumberFormatException) {
/* 3830 */                 localObject4 = ((String)localObject2).split(","); i1 = localObject4.length; i3 = 0; } for (; i3 < i1; i3++) { str5 = localObject4[i3];
/* 3831 */                 String[] arrayOfString = str5.split(":");
/* 3832 */                 if (arrayOfString.length != 2) {
/* 3833 */                   throw new Exception(rb.getString("Illegal.value.") + str1);
/*      */                 }
/*      */ 
/* 3836 */                 if (arrayOfString[0].equalsIgnoreCase("ca"))
/* 3837 */                   bool2 = Boolean.parseBoolean(arrayOfString[1]);
/* 3838 */                 else if (arrayOfString[0].equalsIgnoreCase("pathlen"))
/* 3839 */                   n = Integer.parseInt(arrayOfString[1]);
/*      */                 else {
/* 3841 */                   throw new Exception(rb.getString("Illegal.value.") + str1);
/*      */                 }
/*      */ 
/*      */               }
/*      */ 
/*      */             }
/*      */ 
/* 3848 */             paramCertificateExtensions2.set("BasicConstraints", new BasicConstraintsExtension(Boolean.valueOf(bool1), bool2, n));
/*      */ 
/* 3851 */             break;
/*      */           case 1:
/* 3853 */             if (localObject2 != null) {
/* 3854 */               localObject3 = new boolean[9];
/* 3855 */               for (str5 : ((String)localObject2).split(",")) {
/* 3856 */                 i5 = oneOf(str5, new String[] { "digitalSignature", "nonRepudiation", "keyEncipherment", "dataEncipherment", "keyAgreement", "keyCertSign", "cRLSign", "encipherOnly", "decipherOnly", "contentCommitment" });
/*      */ 
/* 3868 */                 if (i5 < 0) {
/* 3869 */                   throw new Exception(rb.getString("Unknown.keyUsage.type.") + str5);
/*      */                 }
/* 3871 */                 if (i5 == 9) i5 = 1;
/* 3872 */                 localObject3[i5] = 1;
/*      */               }
/* 3874 */               localObject4 = new KeyUsageExtension((boolean[])localObject3);
/*      */ 
/* 3877 */               paramCertificateExtensions2.set("KeyUsage", Extension.newExtension(((KeyUsageExtension)localObject4).getExtensionId(), bool1, ((KeyUsageExtension)localObject4).getExtensionValue()));
/*      */             }
/*      */             else
/*      */             {
/* 3882 */               throw new Exception(rb.getString("Illegal.value.") + str1);
/*      */             }
/*      */ 
/*      */             break;
/*      */           case 2:
/* 3887 */             if (localObject2 != null) {
/* 3888 */               localObject3 = new Vector();
/* 3889 */               for (str5 : ((String)localObject2).split(",")) {
/* 3890 */                 i5 = oneOf(str5, new String[] { "anyExtendedKeyUsage", "serverAuth", "clientAuth", "codeSigning", "emailProtection", "", "", "", "timeStamping", "OCSPSigning" });
/*      */ 
/* 3902 */                 if (i5 < 0) {
/*      */                   try {
/* 3904 */                     ((Vector)localObject3).add(new ObjectIdentifier(str5));
/*      */                   } catch (Exception localException1) {
/* 3906 */                     throw new Exception(rb.getString("Unknown.extendedkeyUsage.type.") + str5);
/*      */                   }
/*      */                 }
/* 3909 */                 else if (i5 == 0)
/* 3910 */                   ((Vector)localObject3).add(new ObjectIdentifier("2.5.29.37.0"));
/*      */                 else {
/* 3912 */                   ((Vector)localObject3).add(new ObjectIdentifier("1.3.6.1.5.5.7.3." + i5));
/*      */                 }
/*      */               }
/* 3915 */               paramCertificateExtensions2.set("ExtendedKeyUsage", new ExtendedKeyUsageExtension(Boolean.valueOf(bool1), (Vector)localObject3));
/*      */             }
/*      */             else {
/* 3918 */               throw new Exception(rb.getString("Illegal.value.") + str1);
/*      */             }
/*      */ 
/*      */             break;
/*      */           case 3:
/*      */           case 4:
/* 3924 */             if (localObject2 != null) {
/* 3925 */               localObject3 = ((String)localObject2).split(",");
/* 3926 */               localObject4 = new GeneralNames();
/* 3927 */               for (str6 : localObject3) {
/* 3928 */                 j = str6.indexOf(':');
/* 3929 */                 if (j < 0) {
/* 3930 */                   throw new Exception("Illegal item " + str6 + " in " + str1);
/*      */                 }
/* 3932 */                 String str7 = str6.substring(0, j);
/* 3933 */                 str9 = str6.substring(j + 1);
/* 3934 */                 ((GeneralNames)localObject4).add(createGeneralName(str7, str9));
/*      */               }
/* 3936 */               if (k == 3) {
/* 3937 */                 paramCertificateExtensions2.set("SubjectAlternativeName", new SubjectAlternativeNameExtension(Boolean.valueOf(bool1), (GeneralNames)localObject4));
/*      */               }
/*      */               else
/*      */               {
/* 3941 */                 paramCertificateExtensions2.set("IssuerAlternativeName", new IssuerAlternativeNameExtension(Boolean.valueOf(bool1), (GeneralNames)localObject4));
/*      */               }
/*      */             }
/*      */             else
/*      */             {
/* 3946 */               throw new Exception(rb.getString("Illegal.value.") + str1);
/*      */             }
/*      */ 
/*      */             break;
/*      */           case 5:
/*      */           case 6:
/* 3952 */             if (bool1) {
/* 3953 */               throw new Exception(rb.getString("This.extension.cannot.be.marked.as.critical.") + str1);
/*      */             }
/*      */ 
/* 3956 */             if (localObject2 != null) {
/* 3957 */               localObject3 = new ArrayList();
/*      */ 
/* 3959 */               localObject4 = ((String)localObject2).split(",");
/* 3960 */               for (str6 : localObject4) {
/* 3961 */                 j = str6.indexOf(':');
/* 3962 */                 int i7 = str6.indexOf(':', j + 1);
/* 3963 */                 if ((j < 0) || (i7 < 0)) {
/* 3964 */                   throw new Exception(rb.getString("Illegal.value.") + str1);
/*      */                 }
/*      */ 
/* 3967 */                 str9 = str6.substring(0, j);
/* 3968 */                 String str10 = str6.substring(j + 1, i7);
/* 3969 */                 String str11 = str6.substring(i7 + 1);
/* 3970 */                 int i10 = oneOf(str9, new String[] { "", "ocsp", "caIssuers", "timeStamping", "", "caRepository" });
/*      */                 ObjectIdentifier localObjectIdentifier;
/* 3979 */                 if (i10 < 0) {
/*      */                   try {
/* 3981 */                     localObjectIdentifier = new ObjectIdentifier(str9);
/*      */                   } catch (Exception localException2) {
/* 3983 */                     throw new Exception(rb.getString("Unknown.AccessDescription.type.") + str9);
/*      */                   }
/*      */                 }
/*      */                 else {
/* 3987 */                   localObjectIdentifier = new ObjectIdentifier("1.3.6.1.5.5.7.48." + i10);
/*      */                 }
/* 3989 */                 ((List)localObject3).add(new AccessDescription(localObjectIdentifier, createGeneralName(str10, str11)));
/*      */               }
/*      */ 
/* 3992 */               if (k == 5) {
/* 3993 */                 paramCertificateExtensions2.set("SubjectInfoAccess", new SubjectInfoAccessExtension((List)localObject3));
/*      */               }
/*      */               else
/* 3996 */                 paramCertificateExtensions2.set("AuthorityInfoAccess", new AuthorityInfoAccessExtension((List)localObject3));
/*      */             }
/*      */             else
/*      */             {
/* 4000 */               throw new Exception(rb.getString("Illegal.value.") + str1);
/*      */             }
/*      */ 
/*      */             break;
/*      */           case 8:
/* 4005 */             if (localObject2 != null) {
/* 4006 */               localObject3 = ((String)localObject2).split(",");
/* 4007 */               localObject4 = new GeneralNames();
/* 4008 */               for (str6 : localObject3) {
/* 4009 */                 j = str6.indexOf(':');
/* 4010 */                 if (j < 0) {
/* 4011 */                   throw new Exception("Illegal item " + str6 + " in " + str1);
/*      */                 }
/* 4013 */                 String str8 = str6.substring(0, j);
/* 4014 */                 str9 = str6.substring(j + 1);
/* 4015 */                 ((GeneralNames)localObject4).add(createGeneralName(str8, str9));
/*      */               }
/* 4017 */               paramCertificateExtensions2.set("CRLDistributionPoints", new CRLDistributionPointsExtension(bool1, Collections.singletonList(new DistributionPoint((GeneralNames)localObject4, null, null))));
/*      */             }
/*      */             else
/*      */             {
/* 4022 */               throw new Exception(rb.getString("Illegal.value.") + str1);
/*      */             }
/*      */ 
/*      */             break;
/*      */           case -1:
/* 4027 */             localObject3 = new ObjectIdentifier((String)localObject1);
/* 4028 */             localObject4 = null;
/* 4029 */             if (localObject2 != null) {
/* 4030 */               localObject4 = new byte[((String)localObject2).length() / 2 + 1];
/* 4031 */               int i2 = 0;
/* 4032 */               for (int i8 : ((String)localObject2).toCharArray())
/*      */               {
/*      */                 int i9;
/* 4034 */                 if ((i8 >= 48) && (i8 <= 57)) {
/* 4035 */                   i9 = i8 - 48;
/* 4036 */                 } else if ((i8 >= 65) && (i8 <= 70)) {
/* 4037 */                   i9 = i8 - 65 + 10; } else {
/* 4038 */                   if ((i8 < 97) || (i8 > 102)) continue;
/* 4039 */                   i9 = i8 - 97 + 10;
/*      */                 }
/*      */ 
/* 4043 */                 if (i2 % 2 == 0) {
/* 4044 */                   localObject4[(i2 / 2)] = ((byte)(i9 << 4));
/*      */                 }
/*      */                 else
/*      */                 {
/*      */                   int tmp2446_2445 = (i2 / 2);
/*      */                   Object tmp2446_2440 = localObject4; tmp2446_2440[tmp2446_2445] = ((byte)(tmp2446_2440[tmp2446_2445] + i9));
/*      */                 }
/* 4048 */                 i2++;
/*      */               }
/* 4050 */               if (i2 % 2 != 0) {
/* 4051 */                 throw new Exception(rb.getString("Odd.number.of.hex.digits.found.") + str1);
/*      */               }
/*      */ 
/* 4054 */               localObject4 = Arrays.copyOf((byte[])localObject4, i2 / 2);
/*      */             } else {
/* 4056 */               localObject4 = new byte[0];
/*      */             }
/* 4058 */             paramCertificateExtensions2.set(((ObjectIdentifier)localObject3).toString(), new Extension((ObjectIdentifier)localObject3, bool1, new DerValue((byte)4, (byte[])localObject4).toByteArray()));
/*      */ 
/* 4061 */             break;
/*      */           case 7:
/*      */           default:
/* 4063 */             throw new Exception(rb.getString("Unknown.extension.type.") + str1);
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/* 4068 */       paramCertificateExtensions2.set("SubjectKeyIdentifier", new SubjectKeyIdentifierExtension(new KeyIdentifier(paramPublicKey1).getIdentifier()));
/*      */ 
/* 4071 */       if ((paramPublicKey2 != null) && (!paramPublicKey1.equals(paramPublicKey2))) {
/* 4072 */         paramCertificateExtensions2.set("AuthorityKeyIdentifier", new AuthorityKeyIdentifierExtension(new KeyIdentifier(paramPublicKey2), null, null));
/*      */       }
/*      */     }
/*      */     catch (IOException localIOException)
/*      */     {
/* 4077 */       throw new RuntimeException(localIOException);
/*      */     }
/* 4079 */     return paramCertificateExtensions2;
/*      */   }
/*      */ 
/*      */   private void usage()
/*      */   {
/*      */     Object localObject1;
/*      */     int j;
/* 4086 */     if (this.command != null) {
/* 4087 */       System.err.println("keytool " + this.command + rb.getString(".OPTION."));
/*      */ 
/* 4089 */       System.err.println();
/* 4090 */       System.err.println(rb.getString(this.command.description));
/* 4091 */       System.err.println();
/* 4092 */       System.err.println(rb.getString("Options."));
/* 4093 */       System.err.println();
/*      */ 
/* 4096 */       localObject1 = new String[this.command.options.length];
/* 4097 */       String[] arrayOfString = new String[this.command.options.length];
/*      */ 
/* 4100 */       j = 0;
/*      */ 
/* 4103 */       int k = 0;
/* 4104 */       for (int m = 0; m < localObject1.length; m++) {
/* 4105 */         Option localOption = this.command.options[m];
/* 4106 */         localObject1[m] = localOption.toString();
/* 4107 */         if (localOption.arg != null)
/*      */         {
/*      */           int tmp178_176 = m;
/*      */           Object tmp178_175 = localObject1; tmp178_175[tmp178_176] = (tmp178_175[tmp178_176] + " " + localOption.arg);
/* 4108 */         }if (localObject1[m].length() > k) {
/* 4109 */           k = localObject1[m].length();
/*      */         }
/* 4111 */         arrayOfString[m] = rb.getString(localOption.description);
/*      */       }
/* 4113 */       for (m = 0; m < localObject1.length; m++) {
/* 4114 */         System.err.printf(" %-" + k + "s  %s\n", new Object[] { localObject1[m], arrayOfString[m] });
/*      */       }
/*      */ 
/* 4117 */       System.err.println();
/* 4118 */       System.err.println(rb.getString("Use.keytool.help.for.all.available.commands"));
/*      */     }
/*      */     else {
/* 4121 */       System.err.println(rb.getString("Key.and.Certificate.Management.Tool"));
/*      */ 
/* 4123 */       System.err.println();
/* 4124 */       System.err.println(rb.getString("Commands."));
/* 4125 */       System.err.println();
/* 4126 */       for (Object localObject2 : Command.values()) {
/* 4127 */         if (localObject2 == Command.KEYCLONE) break;
/* 4128 */         System.err.printf(" %-20s%s\n", new Object[] { localObject2, rb.getString(localObject2.description) });
/*      */       }
/* 4130 */       System.err.println();
/* 4131 */       System.err.println(rb.getString("Use.keytool.command.name.help.for.usage.of.command.name"));
/*      */     }
/*      */   }
/*      */ 
/*      */   private void tinyHelp()
/*      */   {
/* 4137 */     usage();
/* 4138 */     if (this.debug) {
/* 4139 */       throw new RuntimeException("NO BIG ERROR, SORRY");
/*      */     }
/* 4141 */     System.exit(1);
/*      */   }
/*      */ 
/*      */   private void errorNeedArgument(String paramString)
/*      */   {
/* 4146 */     Object[] arrayOfObject = { paramString };
/* 4147 */     System.err.println(new MessageFormat(rb.getString("Command.option.flag.needs.an.argument.")).format(arrayOfObject));
/*      */ 
/* 4149 */     tinyHelp();
/*      */   }
/*      */ 
/*      */   private char[] getPass(String paramString1, String paramString2) {
/* 4153 */     char[] arrayOfChar = getPassWithModifier(paramString1, paramString2);
/* 4154 */     if (arrayOfChar != null) return arrayOfChar;
/* 4155 */     tinyHelp();
/* 4156 */     return null;
/*      */   }
/*      */ 
/*      */   public static char[] getPassWithModifier(String paramString1, String paramString2)
/*      */   {
/* 4161 */     if (paramString1 == null)
/* 4162 */       return paramString2.toCharArray();
/*      */     Object localObject1;
/* 4163 */     if (collator.compare(paramString1, "env") == 0) {
/* 4164 */       localObject1 = System.getenv(paramString2);
/* 4165 */       if (localObject1 == null) {
/* 4166 */         System.err.println(rb.getString("Cannot.find.environment.variable.") + paramString2);
/*      */ 
/* 4168 */         return null;
/*      */       }
/* 4170 */       return ((String)localObject1).toCharArray();
/*      */     }
/* 4172 */     if (collator.compare(paramString1, "file") == 0) {
/*      */       try {
/* 4174 */         localObject1 = null;
/*      */         try {
/* 4176 */           localObject1 = new URL(paramString2);
/*      */         } catch (MalformedURLException localMalformedURLException) {
/* 4178 */           localObject2 = new File(paramString2);
/* 4179 */           if (((File)localObject2).exists()) {
/* 4180 */             localObject1 = ((File)localObject2).toURI().toURL();
/*      */           } else {
/* 4182 */             System.err.println(rb.getString("Cannot.find.file.") + paramString2);
/*      */ 
/* 4184 */             return null;
/*      */           }
/*      */         }
/* 4187 */         BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(((URL)localObject1).openStream()));
/*      */ 
/* 4189 */         Object localObject2 = localBufferedReader.readLine();
/* 4190 */         localBufferedReader.close();
/* 4191 */         if (localObject2 == null) {
/* 4192 */           return new char[0];
/*      */         }
/* 4194 */         return ((String)localObject2).toCharArray();
/*      */       }
/*      */       catch (IOException localIOException) {
/* 4197 */         System.err.println(localIOException);
/* 4198 */         return null;
/*      */       }
/*      */     }
/* 4201 */     System.err.println(rb.getString("Unknown.password.type.") + paramString1);
/*      */ 
/* 4203 */     return null;
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*  326 */     collator.setStrength(0);
/*      */   }
/*      */ 
/*      */   static enum Command
/*      */   {
/*  164 */     CERTREQ("Generates.a.certificate.request", new KeyTool.Option[] { KeyTool.Option.ALIAS, KeyTool.Option.SIGALG, KeyTool.Option.FILEOUT, KeyTool.Option.KEYPASS, KeyTool.Option.KEYSTORE, KeyTool.Option.DNAME, KeyTool.Option.STOREPASS, KeyTool.Option.STORETYPE, KeyTool.Option.PROVIDERNAME, KeyTool.Option.PROVIDERCLASS, KeyTool.Option.PROVIDERARG, KeyTool.Option.PROVIDERPATH, KeyTool.Option.V, KeyTool.Option.PROTECTED }), 
/*      */ 
/*  168 */     CHANGEALIAS("Changes.an.entry.s.alias", new KeyTool.Option[] { KeyTool.Option.ALIAS, KeyTool.Option.DESTALIAS, KeyTool.Option.KEYPASS, KeyTool.Option.KEYSTORE, KeyTool.Option.STOREPASS, KeyTool.Option.STORETYPE, KeyTool.Option.PROVIDERNAME, KeyTool.Option.PROVIDERCLASS, KeyTool.Option.PROVIDERARG, KeyTool.Option.PROVIDERPATH, KeyTool.Option.V, KeyTool.Option.PROTECTED }), 
/*      */ 
/*  172 */     DELETE("Deletes.an.entry", new KeyTool.Option[] { KeyTool.Option.ALIAS, KeyTool.Option.KEYSTORE, KeyTool.Option.STOREPASS, KeyTool.Option.STORETYPE, KeyTool.Option.PROVIDERNAME, KeyTool.Option.PROVIDERCLASS, KeyTool.Option.PROVIDERARG, KeyTool.Option.PROVIDERPATH, KeyTool.Option.V, KeyTool.Option.PROTECTED }), 
/*      */ 
/*  176 */     EXPORTCERT("Exports.certificate", new KeyTool.Option[] { KeyTool.Option.RFC, KeyTool.Option.ALIAS, KeyTool.Option.FILEOUT, KeyTool.Option.KEYSTORE, KeyTool.Option.STOREPASS, KeyTool.Option.STORETYPE, KeyTool.Option.PROVIDERNAME, KeyTool.Option.PROVIDERCLASS, KeyTool.Option.PROVIDERARG, KeyTool.Option.PROVIDERPATH, KeyTool.Option.V, KeyTool.Option.PROTECTED }), 
/*      */ 
/*  180 */     GENKEYPAIR("Generates.a.key.pair", new KeyTool.Option[] { KeyTool.Option.ALIAS, KeyTool.Option.KEYALG, KeyTool.Option.KEYSIZE, KeyTool.Option.SIGALG, KeyTool.Option.DESTALIAS, KeyTool.Option.DNAME, KeyTool.Option.STARTDATE, KeyTool.Option.EXT, KeyTool.Option.VALIDITY, KeyTool.Option.KEYPASS, KeyTool.Option.KEYSTORE, KeyTool.Option.STOREPASS, KeyTool.Option.STORETYPE, KeyTool.Option.PROVIDERNAME, KeyTool.Option.PROVIDERCLASS, KeyTool.Option.PROVIDERARG, KeyTool.Option.PROVIDERPATH, KeyTool.Option.V, KeyTool.Option.PROTECTED }), 
/*      */ 
/*  185 */     GENSECKEY("Generates.a.secret.key", new KeyTool.Option[] { KeyTool.Option.ALIAS, KeyTool.Option.KEYPASS, KeyTool.Option.KEYALG, KeyTool.Option.KEYSIZE, KeyTool.Option.KEYSTORE, KeyTool.Option.STOREPASS, KeyTool.Option.STORETYPE, KeyTool.Option.PROVIDERNAME, KeyTool.Option.PROVIDERCLASS, KeyTool.Option.PROVIDERARG, KeyTool.Option.PROVIDERPATH, KeyTool.Option.V, KeyTool.Option.PROTECTED }), 
/*      */ 
/*  189 */     GENCERT("Generates.certificate.from.a.certificate.request", new KeyTool.Option[] { KeyTool.Option.RFC, KeyTool.Option.INFILE, KeyTool.Option.OUTFILE, KeyTool.Option.ALIAS, KeyTool.Option.SIGALG, KeyTool.Option.DNAME, KeyTool.Option.STARTDATE, KeyTool.Option.EXT, KeyTool.Option.VALIDITY, KeyTool.Option.KEYPASS, KeyTool.Option.KEYSTORE, KeyTool.Option.STOREPASS, KeyTool.Option.STORETYPE, KeyTool.Option.PROVIDERNAME, KeyTool.Option.PROVIDERCLASS, KeyTool.Option.PROVIDERARG, KeyTool.Option.PROVIDERPATH, KeyTool.Option.V, KeyTool.Option.PROTECTED }), 
/*      */ 
/*  194 */     IMPORTCERT("Imports.a.certificate.or.a.certificate.chain", new KeyTool.Option[] { KeyTool.Option.NOPROMPT, KeyTool.Option.TRUSTCACERTS, KeyTool.Option.PROTECTED, KeyTool.Option.ALIAS, KeyTool.Option.FILEIN, KeyTool.Option.KEYPASS, KeyTool.Option.KEYSTORE, KeyTool.Option.STOREPASS, KeyTool.Option.STORETYPE, KeyTool.Option.PROVIDERNAME, KeyTool.Option.PROVIDERCLASS, KeyTool.Option.PROVIDERARG, KeyTool.Option.PROVIDERPATH, KeyTool.Option.V }), 
/*      */ 
/*  199 */     IMPORTKEYSTORE("Imports.one.or.all.entries.from.another.keystore", new KeyTool.Option[] { KeyTool.Option.SRCKEYSTORE, KeyTool.Option.DESTKEYSTORE, KeyTool.Option.SRCSTORETYPE, KeyTool.Option.DESTSTORETYPE, KeyTool.Option.SRCSTOREPASS, KeyTool.Option.DESTSTOREPASS, KeyTool.Option.SRCPROTECTED, KeyTool.Option.SRCPROVIDERNAME, KeyTool.Option.DESTPROVIDERNAME, KeyTool.Option.SRCALIAS, KeyTool.Option.DESTALIAS, KeyTool.Option.SRCKEYPASS, KeyTool.Option.DESTKEYPASS, KeyTool.Option.NOPROMPT, KeyTool.Option.PROVIDERCLASS, KeyTool.Option.PROVIDERARG, KeyTool.Option.PROVIDERPATH, KeyTool.Option.V }), 
/*      */ 
/*  206 */     KEYPASSWD("Changes.the.key.password.of.an.entry", new KeyTool.Option[] { KeyTool.Option.ALIAS, KeyTool.Option.KEYPASS, KeyTool.Option.NEW, KeyTool.Option.KEYSTORE, KeyTool.Option.STOREPASS, KeyTool.Option.STORETYPE, KeyTool.Option.PROVIDERNAME, KeyTool.Option.PROVIDERCLASS, KeyTool.Option.PROVIDERARG, KeyTool.Option.PROVIDERPATH, KeyTool.Option.V }), 
/*      */ 
/*  210 */     LIST("Lists.entries.in.a.keystore", new KeyTool.Option[] { KeyTool.Option.RFC, KeyTool.Option.ALIAS, KeyTool.Option.KEYSTORE, KeyTool.Option.STOREPASS, KeyTool.Option.STORETYPE, KeyTool.Option.PROVIDERNAME, KeyTool.Option.PROVIDERCLASS, KeyTool.Option.PROVIDERARG, KeyTool.Option.PROVIDERPATH, KeyTool.Option.V, KeyTool.Option.PROTECTED }), 
/*      */ 
/*  214 */     PRINTCERT("Prints.the.content.of.a.certificate", new KeyTool.Option[] { KeyTool.Option.RFC, KeyTool.Option.FILEIN, KeyTool.Option.SSLSERVER, KeyTool.Option.JARFILE, KeyTool.Option.V }), 
/*      */ 
/*  216 */     PRINTCERTREQ("Prints.the.content.of.a.certificate.request", new KeyTool.Option[] { KeyTool.Option.FILEIN, KeyTool.Option.V }), 
/*      */ 
/*  218 */     PRINTCRL("Prints.the.content.of.a.CRL.file", new KeyTool.Option[] { KeyTool.Option.FILEIN, KeyTool.Option.V }), 
/*      */ 
/*  220 */     STOREPASSWD("Changes.the.store.password.of.a.keystore", new KeyTool.Option[] { KeyTool.Option.NEW, KeyTool.Option.KEYSTORE, KeyTool.Option.STOREPASS, KeyTool.Option.STORETYPE, KeyTool.Option.PROVIDERNAME, KeyTool.Option.PROVIDERCLASS, KeyTool.Option.PROVIDERARG, KeyTool.Option.PROVIDERPATH, KeyTool.Option.V }), 
/*      */ 
/*  226 */     KEYCLONE("Clones.a.key.entry", new KeyTool.Option[] { KeyTool.Option.ALIAS, KeyTool.Option.DESTALIAS, KeyTool.Option.KEYPASS, KeyTool.Option.NEW, KeyTool.Option.STORETYPE, KeyTool.Option.KEYSTORE, KeyTool.Option.STOREPASS, KeyTool.Option.PROVIDERNAME, KeyTool.Option.PROVIDERCLASS, KeyTool.Option.PROVIDERARG, KeyTool.Option.PROVIDERPATH, KeyTool.Option.V }), 
/*      */ 
/*  230 */     SELFCERT("Generates.a.self.signed.certificate", new KeyTool.Option[] { KeyTool.Option.ALIAS, KeyTool.Option.SIGALG, KeyTool.Option.DNAME, KeyTool.Option.STARTDATE, KeyTool.Option.VALIDITY, KeyTool.Option.KEYPASS, KeyTool.Option.STORETYPE, KeyTool.Option.KEYSTORE, KeyTool.Option.STOREPASS, KeyTool.Option.PROVIDERNAME, KeyTool.Option.PROVIDERCLASS, KeyTool.Option.PROVIDERARG, KeyTool.Option.PROVIDERPATH, KeyTool.Option.V }), 
/*      */ 
/*  234 */     GENCRL("Generates.CRL", new KeyTool.Option[] { KeyTool.Option.RFC, KeyTool.Option.FILEOUT, KeyTool.Option.ID, KeyTool.Option.ALIAS, KeyTool.Option.SIGALG, KeyTool.Option.EXT, KeyTool.Option.KEYPASS, KeyTool.Option.KEYSTORE, KeyTool.Option.STOREPASS, KeyTool.Option.STORETYPE, KeyTool.Option.PROVIDERNAME, KeyTool.Option.PROVIDERCLASS, KeyTool.Option.PROVIDERARG, KeyTool.Option.PROVIDERPATH, KeyTool.Option.V, KeyTool.Option.PROTECTED }), 
/*      */ 
/*  239 */     IDENTITYDB("Imports.entries.from.a.JDK.1.1.x.style.identity.database", new KeyTool.Option[] { KeyTool.Option.FILEIN, KeyTool.Option.STORETYPE, KeyTool.Option.KEYSTORE, KeyTool.Option.STOREPASS, KeyTool.Option.PROVIDERNAME, KeyTool.Option.PROVIDERCLASS, KeyTool.Option.PROVIDERARG, KeyTool.Option.PROVIDERPATH, KeyTool.Option.V });
/*      */ 
/*      */     final String description;
/*      */     final KeyTool.Option[] options;
/*      */ 
/*      */     private Command(String paramString, KeyTool.Option[] paramArrayOfOption) {
/*  246 */       this.description = paramString;
/*  247 */       this.options = paramArrayOfOption;
/*      */     }
/*      */ 
/*      */     public String toString() {
/*  251 */       return "-" + name().toLowerCase(Locale.ENGLISH);
/*      */     }
/*      */   }
/*      */ 
/*      */   static enum Option {
/*  256 */     ALIAS("alias", "<alias>", "alias.name.of.the.entry.to.process"), 
/*  257 */     DESTALIAS("destalias", "<destalias>", "destination.alias"), 
/*  258 */     DESTKEYPASS("destkeypass", "<arg>", "destination.key.password"), 
/*  259 */     DESTKEYSTORE("destkeystore", "<destkeystore>", "destination.keystore.name"), 
/*  260 */     DESTPROTECTED("destprotected", null, "destination.keystore.password.protected"), 
/*  261 */     DESTPROVIDERNAME("destprovidername", "<destprovidername>", "destination.keystore.provider.name"), 
/*  262 */     DESTSTOREPASS("deststorepass", "<arg>", "destination.keystore.password"), 
/*  263 */     DESTSTORETYPE("deststoretype", "<deststoretype>", "destination.keystore.type"), 
/*  264 */     DNAME("dname", "<dname>", "distinguished.name"), 
/*  265 */     EXT("ext", "<value>", "X.509.extension"), 
/*  266 */     FILEOUT("file", "<filename>", "output.file.name"), 
/*  267 */     FILEIN("file", "<filename>", "input.file.name"), 
/*  268 */     ID("id", "<id:reason>", "Serial.ID.of.cert.to.revoke"), 
/*  269 */     INFILE("infile", "<filename>", "input.file.name"), 
/*  270 */     KEYALG("keyalg", "<keyalg>", "key.algorithm.name"), 
/*  271 */     KEYPASS("keypass", "<arg>", "key.password"), 
/*  272 */     KEYSIZE("keysize", "<keysize>", "key.bit.size"), 
/*  273 */     KEYSTORE("keystore", "<keystore>", "keystore.name"), 
/*  274 */     NEW("new", "<arg>", "new.password"), 
/*  275 */     NOPROMPT("noprompt", null, "do.not.prompt"), 
/*  276 */     OUTFILE("outfile", "<filename>", "output.file.name"), 
/*  277 */     PROTECTED("protected", null, "password.through.protected.mechanism"), 
/*  278 */     PROVIDERARG("providerarg", "<arg>", "provider.argument"), 
/*  279 */     PROVIDERCLASS("providerclass", "<providerclass>", "provider.class.name"), 
/*  280 */     PROVIDERNAME("providername", "<providername>", "provider.name"), 
/*  281 */     PROVIDERPATH("providerpath", "<pathlist>", "provider.classpath"), 
/*  282 */     RFC("rfc", null, "output.in.RFC.style"), 
/*  283 */     SIGALG("sigalg", "<sigalg>", "signature.algorithm.name"), 
/*  284 */     SRCALIAS("srcalias", "<srcalias>", "source.alias"), 
/*  285 */     SRCKEYPASS("srckeypass", "<arg>", "source.key.password"), 
/*  286 */     SRCKEYSTORE("srckeystore", "<srckeystore>", "source.keystore.name"), 
/*  287 */     SRCPROTECTED("srcprotected", null, "source.keystore.password.protected"), 
/*  288 */     SRCPROVIDERNAME("srcprovidername", "<srcprovidername>", "source.keystore.provider.name"), 
/*  289 */     SRCSTOREPASS("srcstorepass", "<arg>", "source.keystore.password"), 
/*  290 */     SRCSTORETYPE("srcstoretype", "<srcstoretype>", "source.keystore.type"), 
/*  291 */     SSLSERVER("sslserver", "<server[:port]>", "SSL.server.host.and.port"), 
/*  292 */     JARFILE("jarfile", "<filename>", "signed.jar.file"), 
/*  293 */     STARTDATE("startdate", "<startdate>", "certificate.validity.start.date.time"), 
/*  294 */     STOREPASS("storepass", "<arg>", "keystore.password"), 
/*  295 */     STORETYPE("storetype", "<storetype>", "keystore.type"), 
/*  296 */     TRUSTCACERTS("trustcacerts", null, "trust.certificates.from.cacerts"), 
/*  297 */     V("v", null, "verbose.output"), 
/*  298 */     VALIDITY("validity", "<valDays>", "validity.number.of.days");
/*      */ 
/*      */     final String name;
/*      */     final String arg;
/*      */     final String description;
/*      */ 
/*  302 */     private Option(String paramString1, String paramString2, String paramString3) { this.name = paramString1;
/*  303 */       this.arg = paramString2;
/*  304 */       this.description = paramString3; }
/*      */ 
/*      */     public String toString()
/*      */     {
/*  308 */       return "-" + this.name;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.tools.KeyTool
 * JD-Core Version:    0.6.2
 */