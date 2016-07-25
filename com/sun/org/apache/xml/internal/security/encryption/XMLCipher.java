/*      */ package com.sun.org.apache.xml.internal.security.encryption;
/*      */ 
/*      */ import com.sun.org.apache.xml.internal.security.algorithms.JCEMapper;
/*      */ import com.sun.org.apache.xml.internal.security.c14n.Canonicalizer;
/*      */ import com.sun.org.apache.xml.internal.security.c14n.InvalidCanonicalizerException;
/*      */ import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
/*      */ import com.sun.org.apache.xml.internal.security.keys.KeyInfo;
/*      */ import com.sun.org.apache.xml.internal.security.keys.keyresolver.KeyResolverException;
/*      */ import com.sun.org.apache.xml.internal.security.keys.keyresolver.implementations.EncryptedKeyResolver;
/*      */ import com.sun.org.apache.xml.internal.security.signature.XMLSignatureException;
/*      */ import com.sun.org.apache.xml.internal.security.transforms.InvalidTransformException;
/*      */ import com.sun.org.apache.xml.internal.security.transforms.TransformationException;
/*      */ import com.sun.org.apache.xml.internal.security.utils.Base64;
/*      */ import com.sun.org.apache.xml.internal.security.utils.ElementProxy;
/*      */ import com.sun.org.apache.xml.internal.security.utils.XMLUtils;
/*      */ import com.sun.org.apache.xml.internal.utils.URI;
/*      */ import com.sun.org.apache.xml.internal.utils.URI.MalformedURIException;
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.StringReader;
/*      */ import java.io.UnsupportedEncodingException;
/*      */ import java.security.InvalidAlgorithmParameterException;
/*      */ import java.security.InvalidKeyException;
/*      */ import java.security.Key;
/*      */ import java.security.NoSuchAlgorithmException;
/*      */ import java.security.NoSuchProviderException;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
/*      */ import javax.crypto.BadPaddingException;
/*      */ import javax.crypto.Cipher;
/*      */ import javax.crypto.IllegalBlockSizeException;
/*      */ import javax.crypto.NoSuchPaddingException;
/*      */ import javax.crypto.spec.IvParameterSpec;
/*      */ import javax.xml.parsers.DocumentBuilder;
/*      */ import javax.xml.parsers.DocumentBuilderFactory;
/*      */ import javax.xml.parsers.ParserConfigurationException;
/*      */ import org.w3c.dom.Attr;
/*      */ import org.w3c.dom.Document;
/*      */ import org.w3c.dom.DocumentFragment;
/*      */ import org.w3c.dom.Element;
/*      */ import org.w3c.dom.NamedNodeMap;
/*      */ import org.w3c.dom.Node;
/*      */ import org.w3c.dom.NodeList;
/*      */ import org.w3c.dom.Text;
/*      */ import org.xml.sax.InputSource;
/*      */ import org.xml.sax.SAXException;
/*      */ 
/*      */ public class XMLCipher
/*      */ {
/*   88 */   private static Logger logger = Logger.getLogger(XMLCipher.class.getName());
/*      */   public static final String TRIPLEDES = "http://www.w3.org/2001/04/xmlenc#tripledes-cbc";
/*      */   public static final String AES_128 = "http://www.w3.org/2001/04/xmlenc#aes128-cbc";
/*      */   public static final String AES_256 = "http://www.w3.org/2001/04/xmlenc#aes256-cbc";
/*      */   public static final String AES_192 = "http://www.w3.org/2001/04/xmlenc#aes192-cbc";
/*      */   public static final String RSA_v1dot5 = "http://www.w3.org/2001/04/xmlenc#rsa-1_5";
/*      */   public static final String RSA_OAEP = "http://www.w3.org/2001/04/xmlenc#rsa-oaep-mgf1p";
/*      */   public static final String DIFFIE_HELLMAN = "http://www.w3.org/2001/04/xmlenc#dh";
/*      */   public static final String TRIPLEDES_KeyWrap = "http://www.w3.org/2001/04/xmlenc#kw-tripledes";
/*      */   public static final String AES_128_KeyWrap = "http://www.w3.org/2001/04/xmlenc#kw-aes128";
/*      */   public static final String AES_256_KeyWrap = "http://www.w3.org/2001/04/xmlenc#kw-aes256";
/*      */   public static final String AES_192_KeyWrap = "http://www.w3.org/2001/04/xmlenc#kw-aes192";
/*      */   public static final String SHA1 = "http://www.w3.org/2000/09/xmldsig#sha1";
/*      */   public static final String SHA256 = "http://www.w3.org/2001/04/xmlenc#sha256";
/*      */   public static final String SHA512 = "http://www.w3.org/2001/04/xmlenc#sha512";
/*      */   public static final String RIPEMD_160 = "http://www.w3.org/2001/04/xmlenc#ripemd160";
/*      */   public static final String XML_DSIG = "http://www.w3.org/2000/09/xmldsig#";
/*      */   public static final String N14C_XML = "http://www.w3.org/TR/2001/REC-xml-c14n-20010315";
/*      */   public static final String N14C_XML_WITH_COMMENTS = "http://www.w3.org/TR/2001/REC-xml-c14n-20010315#WithComments";
/*      */   public static final String EXCL_XML_N14C = "http://www.w3.org/2001/10/xml-exc-c14n#";
/*      */   public static final String EXCL_XML_N14C_WITH_COMMENTS = "http://www.w3.org/2001/10/xml-exc-c14n#WithComments";
/*      */   public static final String BASE64_ENCODING = "http://www.w3.org/2000/09/xmldsig#base64";
/*      */   public static final int ENCRYPT_MODE = 1;
/*      */   public static final int DECRYPT_MODE = 2;
/*      */   public static final int UNWRAP_MODE = 4;
/*      */   public static final int WRAP_MODE = 3;
/*      */   private static final String ENC_ALGORITHMS = "http://www.w3.org/2001/04/xmlenc#tripledes-cbc\nhttp://www.w3.org/2001/04/xmlenc#aes128-cbc\nhttp://www.w3.org/2001/04/xmlenc#aes256-cbc\nhttp://www.w3.org/2001/04/xmlenc#aes192-cbc\nhttp://www.w3.org/2001/04/xmlenc#rsa-1_5\nhttp://www.w3.org/2001/04/xmlenc#rsa-oaep-mgf1p\nhttp://www.w3.org/2001/04/xmlenc#kw-tripledes\nhttp://www.w3.org/2001/04/xmlenc#kw-aes128\nhttp://www.w3.org/2001/04/xmlenc#kw-aes256\nhttp://www.w3.org/2001/04/xmlenc#kw-aes192\n";
/*      */   private Cipher _contextCipher;
/*  174 */   private int _cipherMode = -2147483648;
/*      */ 
/*  176 */   private String _algorithm = null;
/*      */ 
/*  178 */   private String _requestedJCEProvider = null;
/*      */   private Canonicalizer _canon;
/*      */   private Document _contextDocument;
/*      */   private Factory _factory;
/*      */   private Serializer _serializer;
/*      */   private Key _key;
/*      */   private Key _kek;
/*      */   private EncryptedKey _ek;
/*      */   private EncryptedData _ed;
/*      */ 
/*      */   private XMLCipher()
/*      */   {
/*  210 */     logger.log(Level.FINE, "Constructing XMLCipher...");
/*      */ 
/*  212 */     this._factory = new Factory(null);
/*  213 */     this._serializer = new Serializer();
/*      */   }
/*      */ 
/*      */   private static boolean isValidEncryptionAlgorithm(String paramString)
/*      */   {
/*  225 */     boolean bool = (paramString.equals("http://www.w3.org/2001/04/xmlenc#tripledes-cbc")) || (paramString.equals("http://www.w3.org/2001/04/xmlenc#aes128-cbc")) || (paramString.equals("http://www.w3.org/2001/04/xmlenc#aes256-cbc")) || (paramString.equals("http://www.w3.org/2001/04/xmlenc#aes192-cbc")) || (paramString.equals("http://www.w3.org/2001/04/xmlenc#rsa-1_5")) || (paramString.equals("http://www.w3.org/2001/04/xmlenc#rsa-oaep-mgf1p")) || (paramString.equals("http://www.w3.org/2001/04/xmlenc#kw-tripledes")) || (paramString.equals("http://www.w3.org/2001/04/xmlenc#kw-aes128")) || (paramString.equals("http://www.w3.org/2001/04/xmlenc#kw-aes256")) || (paramString.equals("http://www.w3.org/2001/04/xmlenc#kw-aes192"));
/*      */ 
/*  238 */     return bool;
/*      */   }
/*      */ 
/*      */   public static XMLCipher getInstance(String paramString)
/*      */     throws XMLEncryptionException
/*      */   {
/*  272 */     logger.log(Level.FINE, "Getting XMLCipher...");
/*  273 */     if (null == paramString)
/*  274 */       logger.log(Level.SEVERE, "Transformation unexpectedly null...");
/*  275 */     if (!isValidEncryptionAlgorithm(paramString)) {
/*  276 */       logger.log(Level.WARNING, "Algorithm non-standard, expected one of http://www.w3.org/2001/04/xmlenc#tripledes-cbc\nhttp://www.w3.org/2001/04/xmlenc#aes128-cbc\nhttp://www.w3.org/2001/04/xmlenc#aes256-cbc\nhttp://www.w3.org/2001/04/xmlenc#aes192-cbc\nhttp://www.w3.org/2001/04/xmlenc#rsa-1_5\nhttp://www.w3.org/2001/04/xmlenc#rsa-oaep-mgf1p\nhttp://www.w3.org/2001/04/xmlenc#kw-tripledes\nhttp://www.w3.org/2001/04/xmlenc#kw-aes128\nhttp://www.w3.org/2001/04/xmlenc#kw-aes256\nhttp://www.w3.org/2001/04/xmlenc#kw-aes192\n");
/*      */     }
/*  278 */     XMLCipher localXMLCipher = new XMLCipher();
/*      */ 
/*  280 */     localXMLCipher._algorithm = paramString;
/*  281 */     localXMLCipher._key = null;
/*  282 */     localXMLCipher._kek = null;
/*      */     try
/*      */     {
/*  289 */       localXMLCipher._canon = Canonicalizer.getInstance("http://www.w3.org/TR/2001/REC-xml-c14n-20010315#WithComments");
/*      */     }
/*      */     catch (InvalidCanonicalizerException localInvalidCanonicalizerException)
/*      */     {
/*  293 */       throw new XMLEncryptionException("empty", localInvalidCanonicalizerException);
/*      */     }
/*      */ 
/*  296 */     String str = JCEMapper.translateURItoJCEID(paramString);
/*      */     try
/*      */     {
/*  299 */       localXMLCipher._contextCipher = Cipher.getInstance(str);
/*  300 */       logger.log(Level.FINE, "cihper.algoritm = " + localXMLCipher._contextCipher.getAlgorithm());
/*      */     }
/*      */     catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
/*  303 */       throw new XMLEncryptionException("empty", localNoSuchAlgorithmException);
/*      */     } catch (NoSuchPaddingException localNoSuchPaddingException) {
/*  305 */       throw new XMLEncryptionException("empty", localNoSuchPaddingException);
/*      */     }
/*      */ 
/*  308 */     return localXMLCipher;
/*      */   }
/*      */ 
/*      */   public static XMLCipher getInstance(String paramString1, String paramString2)
/*      */     throws XMLEncryptionException
/*      */   {
/*  330 */     XMLCipher localXMLCipher = getInstance(paramString1);
/*      */ 
/*  332 */     if (paramString2 != null) {
/*      */       try {
/*  334 */         localXMLCipher._canon = Canonicalizer.getInstance(paramString2);
/*      */       } catch (InvalidCanonicalizerException localInvalidCanonicalizerException) {
/*  336 */         throw new XMLEncryptionException("empty", localInvalidCanonicalizerException);
/*      */       }
/*      */     }
/*      */ 
/*  340 */     return localXMLCipher;
/*      */   }
/*      */ 
/*      */   public static XMLCipher getInstance(String paramString, Cipher paramCipher) throws XMLEncryptionException
/*      */   {
/*  345 */     logger.log(Level.FINE, "Getting XMLCipher...");
/*  346 */     if (null == paramString)
/*  347 */       logger.log(Level.SEVERE, "Transformation unexpectedly null...");
/*  348 */     if (!isValidEncryptionAlgorithm(paramString)) {
/*  349 */       logger.log(Level.WARNING, "Algorithm non-standard, expected one of http://www.w3.org/2001/04/xmlenc#tripledes-cbc\nhttp://www.w3.org/2001/04/xmlenc#aes128-cbc\nhttp://www.w3.org/2001/04/xmlenc#aes256-cbc\nhttp://www.w3.org/2001/04/xmlenc#aes192-cbc\nhttp://www.w3.org/2001/04/xmlenc#rsa-1_5\nhttp://www.w3.org/2001/04/xmlenc#rsa-oaep-mgf1p\nhttp://www.w3.org/2001/04/xmlenc#kw-tripledes\nhttp://www.w3.org/2001/04/xmlenc#kw-aes128\nhttp://www.w3.org/2001/04/xmlenc#kw-aes256\nhttp://www.w3.org/2001/04/xmlenc#kw-aes192\n");
/*      */     }
/*  351 */     XMLCipher localXMLCipher = new XMLCipher();
/*      */ 
/*  353 */     localXMLCipher._algorithm = paramString;
/*  354 */     localXMLCipher._key = null;
/*  355 */     localXMLCipher._kek = null;
/*      */     try
/*      */     {
/*  362 */       localXMLCipher._canon = Canonicalizer.getInstance("http://www.w3.org/TR/2001/REC-xml-c14n-20010315#WithComments");
/*      */     }
/*      */     catch (InvalidCanonicalizerException localInvalidCanonicalizerException)
/*      */     {
/*  366 */       throw new XMLEncryptionException("empty", localInvalidCanonicalizerException);
/*      */     }
/*      */ 
/*  369 */     String str = JCEMapper.translateURItoJCEID(paramString);
/*      */     try
/*      */     {
/*  372 */       localXMLCipher._contextCipher = paramCipher;
/*      */ 
/*  374 */       logger.log(Level.FINE, "cihper.algoritm = " + localXMLCipher._contextCipher.getAlgorithm());
/*      */     }
/*      */     catch (Exception localException) {
/*  377 */       throw new XMLEncryptionException("empty", localException);
/*      */     }
/*      */ 
/*  380 */     return localXMLCipher;
/*      */   }
/*      */ 
/*      */   public static XMLCipher getProviderInstance(String paramString1, String paramString2)
/*      */     throws XMLEncryptionException
/*      */   {
/*  398 */     logger.log(Level.FINE, "Getting XMLCipher...");
/*  399 */     if (null == paramString1)
/*  400 */       logger.log(Level.SEVERE, "Transformation unexpectedly null...");
/*  401 */     if (null == paramString2)
/*  402 */       logger.log(Level.SEVERE, "Provider unexpectedly null..");
/*  403 */     if ("" == paramString2)
/*  404 */       logger.log(Level.SEVERE, "Provider's value unexpectedly not specified...");
/*  405 */     if (!isValidEncryptionAlgorithm(paramString1)) {
/*  406 */       logger.log(Level.WARNING, "Algorithm non-standard, expected one of http://www.w3.org/2001/04/xmlenc#tripledes-cbc\nhttp://www.w3.org/2001/04/xmlenc#aes128-cbc\nhttp://www.w3.org/2001/04/xmlenc#aes256-cbc\nhttp://www.w3.org/2001/04/xmlenc#aes192-cbc\nhttp://www.w3.org/2001/04/xmlenc#rsa-1_5\nhttp://www.w3.org/2001/04/xmlenc#rsa-oaep-mgf1p\nhttp://www.w3.org/2001/04/xmlenc#kw-tripledes\nhttp://www.w3.org/2001/04/xmlenc#kw-aes128\nhttp://www.w3.org/2001/04/xmlenc#kw-aes256\nhttp://www.w3.org/2001/04/xmlenc#kw-aes192\n");
/*      */     }
/*  408 */     XMLCipher localXMLCipher = new XMLCipher();
/*      */ 
/*  410 */     localXMLCipher._algorithm = paramString1;
/*  411 */     localXMLCipher._requestedJCEProvider = paramString2;
/*  412 */     localXMLCipher._key = null;
/*  413 */     localXMLCipher._kek = null;
/*      */     try
/*      */     {
/*  419 */       localXMLCipher._canon = Canonicalizer.getInstance("http://www.w3.org/TR/2001/REC-xml-c14n-20010315#WithComments");
/*      */     }
/*      */     catch (InvalidCanonicalizerException localInvalidCanonicalizerException) {
/*  422 */       throw new XMLEncryptionException("empty", localInvalidCanonicalizerException);
/*      */     }
/*      */     try
/*      */     {
/*  426 */       String str = JCEMapper.translateURItoJCEID(paramString1);
/*      */ 
/*  429 */       localXMLCipher._contextCipher = Cipher.getInstance(str, paramString2);
/*      */ 
/*  431 */       logger.log(Level.FINE, "cipher._algorithm = " + localXMLCipher._contextCipher.getAlgorithm());
/*      */ 
/*  433 */       logger.log(Level.FINE, "provider.name = " + paramString2);
/*      */     } catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
/*  435 */       throw new XMLEncryptionException("empty", localNoSuchAlgorithmException);
/*      */     } catch (NoSuchProviderException localNoSuchProviderException) {
/*  437 */       throw new XMLEncryptionException("empty", localNoSuchProviderException);
/*      */     } catch (NoSuchPaddingException localNoSuchPaddingException) {
/*  439 */       throw new XMLEncryptionException("empty", localNoSuchPaddingException);
/*      */     }
/*      */ 
/*  442 */     return localXMLCipher;
/*      */   }
/*      */ 
/*      */   public static XMLCipher getProviderInstance(String paramString1, String paramString2, String paramString3)
/*      */     throws XMLEncryptionException
/*      */   {
/*  468 */     XMLCipher localXMLCipher = getProviderInstance(paramString1, paramString2);
/*  469 */     if (paramString3 != null) {
/*      */       try {
/*  471 */         localXMLCipher._canon = Canonicalizer.getInstance(paramString3);
/*      */       } catch (InvalidCanonicalizerException localInvalidCanonicalizerException) {
/*  473 */         throw new XMLEncryptionException("empty", localInvalidCanonicalizerException);
/*      */       }
/*      */     }
/*  476 */     return localXMLCipher;
/*      */   }
/*      */ 
/*      */   public static XMLCipher getInstance()
/*      */     throws XMLEncryptionException
/*      */   {
/*  492 */     logger.log(Level.FINE, "Getting XMLCipher for no transformation...");
/*      */ 
/*  494 */     XMLCipher localXMLCipher = new XMLCipher();
/*      */ 
/*  496 */     localXMLCipher._algorithm = null;
/*  497 */     localXMLCipher._requestedJCEProvider = null;
/*  498 */     localXMLCipher._key = null;
/*  499 */     localXMLCipher._kek = null;
/*  500 */     localXMLCipher._contextCipher = null;
/*      */     try
/*      */     {
/*  506 */       localXMLCipher._canon = Canonicalizer.getInstance("http://www.w3.org/TR/2001/REC-xml-c14n-20010315#WithComments");
/*      */     }
/*      */     catch (InvalidCanonicalizerException localInvalidCanonicalizerException) {
/*  509 */       throw new XMLEncryptionException("empty", localInvalidCanonicalizerException);
/*      */     }
/*      */ 
/*  512 */     return localXMLCipher;
/*      */   }
/*      */ 
/*      */   public static XMLCipher getProviderInstance(String paramString)
/*      */     throws XMLEncryptionException
/*      */   {
/*  534 */     logger.log(Level.FINE, "Getting XMLCipher, provider but no transformation");
/*  535 */     if (null == paramString)
/*  536 */       logger.log(Level.SEVERE, "Provider unexpectedly null..");
/*  537 */     if ("" == paramString) {
/*  538 */       logger.log(Level.SEVERE, "Provider's value unexpectedly not specified...");
/*      */     }
/*  540 */     XMLCipher localXMLCipher = new XMLCipher();
/*      */ 
/*  542 */     localXMLCipher._algorithm = null;
/*  543 */     localXMLCipher._requestedJCEProvider = paramString;
/*  544 */     localXMLCipher._key = null;
/*  545 */     localXMLCipher._kek = null;
/*  546 */     localXMLCipher._contextCipher = null;
/*      */     try
/*      */     {
/*  549 */       localXMLCipher._canon = Canonicalizer.getInstance("http://www.w3.org/TR/2001/REC-xml-c14n-20010315#WithComments");
/*      */     }
/*      */     catch (InvalidCanonicalizerException localInvalidCanonicalizerException) {
/*  552 */       throw new XMLEncryptionException("empty", localInvalidCanonicalizerException);
/*      */     }
/*      */ 
/*  555 */     return localXMLCipher;
/*      */   }
/*      */ 
/*      */   public void init(int paramInt, Key paramKey)
/*      */     throws XMLEncryptionException
/*      */   {
/*  580 */     logger.log(Level.FINE, "Initializing XMLCipher...");
/*      */ 
/*  582 */     this._ek = null;
/*  583 */     this._ed = null;
/*      */ 
/*  585 */     switch (paramInt)
/*      */     {
/*      */     case 1:
/*  588 */       logger.log(Level.FINE, "opmode = ENCRYPT_MODE");
/*  589 */       this._ed = createEncryptedData(1, "NO VALUE YET");
/*  590 */       break;
/*      */     case 2:
/*  592 */       logger.log(Level.FINE, "opmode = DECRYPT_MODE");
/*  593 */       break;
/*      */     case 3:
/*  595 */       logger.log(Level.FINE, "opmode = WRAP_MODE");
/*  596 */       this._ek = createEncryptedKey(1, "NO VALUE YET");
/*  597 */       break;
/*      */     case 4:
/*  599 */       logger.log(Level.FINE, "opmode = UNWRAP_MODE");
/*  600 */       break;
/*      */     default:
/*  602 */       logger.log(Level.SEVERE, "Mode unexpectedly invalid");
/*  603 */       throw new XMLEncryptionException("Invalid mode in init");
/*      */     }
/*      */ 
/*  606 */     this._cipherMode = paramInt;
/*  607 */     this._key = paramKey;
/*      */   }
/*      */ 
/*      */   public EncryptedData getEncryptedData()
/*      */   {
/*  624 */     logger.log(Level.FINE, "Returning EncryptedData");
/*  625 */     return this._ed;
/*      */   }
/*      */ 
/*      */   public EncryptedKey getEncryptedKey()
/*      */   {
/*  642 */     logger.log(Level.FINE, "Returning EncryptedKey");
/*  643 */     return this._ek;
/*      */   }
/*      */ 
/*      */   public void setKEK(Key paramKey)
/*      */   {
/*  659 */     this._kek = paramKey;
/*      */   }
/*      */ 
/*      */   public Element martial(EncryptedData paramEncryptedData)
/*      */   {
/*  679 */     return this._factory.toElement(paramEncryptedData);
/*      */   }
/*      */ 
/*      */   public Element martial(EncryptedKey paramEncryptedKey)
/*      */   {
/*  699 */     return this._factory.toElement(paramEncryptedKey);
/*      */   }
/*      */ 
/*      */   public Element martial(Document paramDocument, EncryptedData paramEncryptedData)
/*      */   {
/*  716 */     this._contextDocument = paramDocument;
/*  717 */     return this._factory.toElement(paramEncryptedData);
/*      */   }
/*      */ 
/*      */   public Element martial(Document paramDocument, EncryptedKey paramEncryptedKey)
/*      */   {
/*  734 */     this._contextDocument = paramDocument;
/*  735 */     return this._factory.toElement(paramEncryptedKey);
/*      */   }
/*      */ 
/*      */   private Document encryptElement(Element paramElement)
/*      */     throws Exception
/*      */   {
/*  752 */     logger.log(Level.FINE, "Encrypting element...");
/*  753 */     if (null == paramElement)
/*  754 */       logger.log(Level.SEVERE, "Element unexpectedly null...");
/*  755 */     if (this._cipherMode != 1) {
/*  756 */       logger.log(Level.FINE, "XMLCipher unexpectedly not in ENCRYPT_MODE...");
/*      */     }
/*  758 */     if (this._algorithm == null) {
/*  759 */       throw new XMLEncryptionException("XMLCipher instance without transformation specified");
/*      */     }
/*  761 */     encryptData(this._contextDocument, paramElement, false);
/*      */ 
/*  763 */     Element localElement = this._factory.toElement(this._ed);
/*      */ 
/*  765 */     Node localNode = paramElement.getParentNode();
/*  766 */     localNode.replaceChild(localElement, paramElement);
/*      */ 
/*  768 */     return this._contextDocument;
/*      */   }
/*      */ 
/*      */   private Document encryptElementContent(Element paramElement)
/*      */     throws Exception
/*      */   {
/*  787 */     logger.log(Level.FINE, "Encrypting element content...");
/*  788 */     if (null == paramElement)
/*  789 */       logger.log(Level.SEVERE, "Element unexpectedly null...");
/*  790 */     if (this._cipherMode != 1) {
/*  791 */       logger.log(Level.FINE, "XMLCipher unexpectedly not in ENCRYPT_MODE...");
/*      */     }
/*  793 */     if (this._algorithm == null) {
/*  794 */       throw new XMLEncryptionException("XMLCipher instance without transformation specified");
/*      */     }
/*  796 */     encryptData(this._contextDocument, paramElement, true);
/*      */ 
/*  798 */     Element localElement = this._factory.toElement(this._ed);
/*      */ 
/*  800 */     removeContent(paramElement);
/*  801 */     paramElement.appendChild(localElement);
/*      */ 
/*  803 */     return this._contextDocument;
/*      */   }
/*      */ 
/*      */   public Document doFinal(Document paramDocument1, Document paramDocument2)
/*      */     throws Exception
/*      */   {
/*  817 */     logger.log(Level.FINE, "Processing source document...");
/*  818 */     if (null == paramDocument1)
/*  819 */       logger.log(Level.SEVERE, "Context document unexpectedly null...");
/*  820 */     if (null == paramDocument2) {
/*  821 */       logger.log(Level.SEVERE, "Source document unexpectedly null...");
/*      */     }
/*  823 */     this._contextDocument = paramDocument1;
/*      */ 
/*  825 */     Document localDocument = null;
/*      */ 
/*  827 */     switch (this._cipherMode) {
/*      */     case 2:
/*  829 */       localDocument = decryptElement(paramDocument2.getDocumentElement());
/*  830 */       break;
/*      */     case 1:
/*  832 */       localDocument = encryptElement(paramDocument2.getDocumentElement());
/*  833 */       break;
/*      */     case 4:
/*  835 */       break;
/*      */     case 3:
/*  837 */       break;
/*      */     default:
/*  839 */       throw new XMLEncryptionException("empty", new IllegalStateException());
/*      */     }
/*      */ 
/*  843 */     return localDocument;
/*      */   }
/*      */ 
/*      */   public Document doFinal(Document paramDocument, Element paramElement)
/*      */     throws Exception
/*      */   {
/*  857 */     logger.log(Level.FINE, "Processing source element...");
/*  858 */     if (null == paramDocument)
/*  859 */       logger.log(Level.SEVERE, "Context document unexpectedly null...");
/*  860 */     if (null == paramElement) {
/*  861 */       logger.log(Level.SEVERE, "Source element unexpectedly null...");
/*      */     }
/*  863 */     this._contextDocument = paramDocument;
/*      */ 
/*  865 */     Document localDocument = null;
/*      */ 
/*  867 */     switch (this._cipherMode) {
/*      */     case 2:
/*  869 */       localDocument = decryptElement(paramElement);
/*  870 */       break;
/*      */     case 1:
/*  872 */       localDocument = encryptElement(paramElement);
/*  873 */       break;
/*      */     case 4:
/*  875 */       break;
/*      */     case 3:
/*  877 */       break;
/*      */     default:
/*  879 */       throw new XMLEncryptionException("empty", new IllegalStateException());
/*      */     }
/*      */ 
/*  883 */     return localDocument;
/*      */   }
/*      */ 
/*      */   public Document doFinal(Document paramDocument, Element paramElement, boolean paramBoolean)
/*      */     throws Exception
/*      */   {
/*  900 */     logger.log(Level.FINE, "Processing source element...");
/*  901 */     if (null == paramDocument)
/*  902 */       logger.log(Level.SEVERE, "Context document unexpectedly null...");
/*  903 */     if (null == paramElement) {
/*  904 */       logger.log(Level.SEVERE, "Source element unexpectedly null...");
/*      */     }
/*  906 */     this._contextDocument = paramDocument;
/*      */ 
/*  908 */     Document localDocument = null;
/*      */ 
/*  910 */     switch (this._cipherMode) {
/*      */     case 2:
/*  912 */       if (paramBoolean)
/*  913 */         localDocument = decryptElementContent(paramElement);
/*      */       else {
/*  915 */         localDocument = decryptElement(paramElement);
/*      */       }
/*  917 */       break;
/*      */     case 1:
/*  919 */       if (paramBoolean)
/*  920 */         localDocument = encryptElementContent(paramElement);
/*      */       else {
/*  922 */         localDocument = encryptElement(paramElement);
/*      */       }
/*  924 */       break;
/*      */     case 4:
/*  926 */       break;
/*      */     case 3:
/*  928 */       break;
/*      */     default:
/*  930 */       throw new XMLEncryptionException("empty", new IllegalStateException());
/*      */     }
/*      */ 
/*  934 */     return localDocument;
/*      */   }
/*      */ 
/*      */   public EncryptedData encryptData(Document paramDocument, Element paramElement)
/*      */     throws Exception
/*      */   {
/*  951 */     return encryptData(paramDocument, paramElement, false);
/*      */   }
/*      */ 
/*      */   public EncryptedData encryptData(Document paramDocument, String paramString, InputStream paramInputStream)
/*      */     throws Exception
/*      */   {
/*  971 */     logger.log(Level.FINE, "Encrypting element...");
/*  972 */     if (null == paramDocument)
/*  973 */       logger.log(Level.SEVERE, "Context document unexpectedly null...");
/*  974 */     if (null == paramInputStream)
/*  975 */       logger.log(Level.SEVERE, "Serialized data unexpectedly null...");
/*  976 */     if (this._cipherMode != 1) {
/*  977 */       logger.log(Level.FINE, "XMLCipher unexpectedly not in ENCRYPT_MODE...");
/*      */     }
/*  979 */     return encryptData(paramDocument, null, paramString, paramInputStream);
/*      */   }
/*      */ 
/*      */   public EncryptedData encryptData(Document paramDocument, Element paramElement, boolean paramBoolean)
/*      */     throws Exception
/*      */   {
/* 1000 */     logger.log(Level.FINE, "Encrypting element...");
/* 1001 */     if (null == paramDocument)
/* 1002 */       logger.log(Level.SEVERE, "Context document unexpectedly null...");
/* 1003 */     if (null == paramElement)
/* 1004 */       logger.log(Level.SEVERE, "Element unexpectedly null...");
/* 1005 */     if (this._cipherMode != 1) {
/* 1006 */       logger.log(Level.FINE, "XMLCipher unexpectedly not in ENCRYPT_MODE...");
/*      */     }
/* 1008 */     if (paramBoolean) {
/* 1009 */       return encryptData(paramDocument, paramElement, "http://www.w3.org/2001/04/xmlenc#Content", null);
/*      */     }
/*      */ 
/* 1012 */     return encryptData(paramDocument, paramElement, "http://www.w3.org/2001/04/xmlenc#Element", null);
/*      */   }
/*      */ 
/*      */   private EncryptedData encryptData(Document paramDocument, Element paramElement, String paramString, InputStream paramInputStream)
/*      */     throws Exception
/*      */   {
/* 1021 */     this._contextDocument = paramDocument;
/*      */ 
/* 1023 */     if (this._algorithm == null) {
/* 1024 */       throw new XMLEncryptionException("XMLCipher instance without transformation specified");
/*      */     }
/*      */ 
/* 1028 */     String str1 = null;
/*      */     Object localObject2;
/* 1029 */     if (paramInputStream == null) {
/* 1030 */       if (paramString == "http://www.w3.org/2001/04/xmlenc#Content") {
/* 1031 */         localObject1 = paramElement.getChildNodes();
/* 1032 */         if (null != localObject1) {
/* 1033 */           str1 = this._serializer.serialize((NodeList)localObject1);
/*      */         } else {
/* 1035 */           localObject2 = new Object[] { "Element has no content." };
/* 1036 */           throw new XMLEncryptionException("empty", (Object[])localObject2);
/*      */         }
/*      */       } else {
/* 1039 */         str1 = this._serializer.serialize(paramElement);
/*      */       }
/* 1041 */       logger.log(Level.FINE, "Serialized octets:\n" + str1);
/*      */     }
/*      */ 
/* 1044 */     Object localObject1 = null;
/*      */ 
/* 1048 */     if (this._contextCipher == null) {
/* 1049 */       String str2 = JCEMapper.translateURItoJCEID(this._algorithm);
/* 1050 */       logger.log(Level.FINE, "alg = " + str2);
/*      */       try
/*      */       {
/* 1053 */         if (this._requestedJCEProvider == null)
/* 1054 */           localObject2 = Cipher.getInstance(str2);
/*      */         else
/* 1056 */           localObject2 = Cipher.getInstance(str2, this._requestedJCEProvider);
/*      */       } catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
/* 1058 */         throw new XMLEncryptionException("empty", localNoSuchAlgorithmException);
/*      */       } catch (NoSuchProviderException localNoSuchProviderException) {
/* 1060 */         throw new XMLEncryptionException("empty", localNoSuchProviderException);
/*      */       } catch (NoSuchPaddingException localNoSuchPaddingException) {
/* 1062 */         throw new XMLEncryptionException("empty", localNoSuchPaddingException);
/*      */       }
/*      */     } else {
/* 1065 */       localObject2 = this._contextCipher;
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/* 1072 */       ((Cipher)localObject2).init(this._cipherMode, this._key);
/*      */     } catch (InvalidKeyException localInvalidKeyException) {
/* 1074 */       throw new XMLEncryptionException("empty", localInvalidKeyException);
/*      */     }
/*      */     Object localObject4;
/*      */     try {
/* 1078 */       if (paramInputStream != null)
/*      */       {
/* 1080 */         arrayOfByte2 = new byte[8192];
/* 1081 */         localObject3 = new ByteArrayOutputStream();
/*      */         int i;
/* 1082 */         while ((i = paramInputStream.read(arrayOfByte2)) != -1) {
/* 1083 */           localObject4 = ((Cipher)localObject2).update(arrayOfByte2, 0, i);
/* 1084 */           ((ByteArrayOutputStream)localObject3).write((byte[])localObject4);
/*      */         }
/* 1086 */         ((ByteArrayOutputStream)localObject3).write(((Cipher)localObject2).doFinal());
/* 1087 */         localObject1 = ((ByteArrayOutputStream)localObject3).toByteArray();
/*      */       } else {
/* 1089 */         localObject1 = ((Cipher)localObject2).doFinal(str1.getBytes("UTF-8"));
/* 1090 */         logger.log(Level.FINE, "Expected cipher.outputSize = " + Integer.toString(((Cipher)localObject2).getOutputSize(str1.getBytes().length)));
/*      */       }
/*      */ 
/* 1094 */       logger.log(Level.FINE, "Actual cipher.outputSize = " + Integer.toString(localObject1.length));
/*      */     }
/*      */     catch (IllegalStateException localIllegalStateException) {
/* 1097 */       throw new XMLEncryptionException("empty", localIllegalStateException);
/*      */     } catch (IllegalBlockSizeException localIllegalBlockSizeException) {
/* 1099 */       throw new XMLEncryptionException("empty", localIllegalBlockSizeException);
/*      */     } catch (BadPaddingException localBadPaddingException) {
/* 1101 */       throw new XMLEncryptionException("empty", localBadPaddingException);
/*      */     } catch (UnsupportedEncodingException localUnsupportedEncodingException) {
/* 1103 */       throw new XMLEncryptionException("empty", localUnsupportedEncodingException);
/*      */     }
/*      */ 
/* 1108 */     byte[] arrayOfByte1 = ((Cipher)localObject2).getIV();
/* 1109 */     byte[] arrayOfByte2 = new byte[arrayOfByte1.length + localObject1.length];
/*      */ 
/* 1111 */     System.arraycopy(arrayOfByte1, 0, arrayOfByte2, 0, arrayOfByte1.length);
/* 1112 */     System.arraycopy(localObject1, 0, arrayOfByte2, arrayOfByte1.length, localObject1.length);
/*      */ 
/* 1114 */     Object localObject3 = Base64.encode(arrayOfByte2);
/*      */ 
/* 1116 */     logger.log(Level.FINE, "Encrypted octets:\n" + (String)localObject3);
/* 1117 */     logger.log(Level.FINE, "Encrypted octets length = " + ((String)localObject3).length());
/*      */     try
/*      */     {
/* 1121 */       localObject4 = this._ed.getCipherData();
/* 1122 */       CipherValue localCipherValue = ((CipherData)localObject4).getCipherValue();
/*      */ 
/* 1124 */       localCipherValue.setValue((String)localObject3);
/*      */ 
/* 1126 */       if (paramString != null) {
/* 1127 */         this._ed.setType(new URI(paramString).toString());
/*      */       }
/* 1129 */       EncryptionMethod localEncryptionMethod = this._factory.newEncryptionMethod(new URI(this._algorithm).toString());
/*      */ 
/* 1131 */       this._ed.setEncryptionMethod(localEncryptionMethod);
/*      */     } catch (URI.MalformedURIException localMalformedURIException) {
/* 1133 */       throw new XMLEncryptionException("empty", localMalformedURIException);
/*      */     }
/* 1135 */     return this._ed;
/*      */   }
/*      */ 
/*      */   public EncryptedData loadEncryptedData(Document paramDocument, Element paramElement)
/*      */     throws XMLEncryptionException
/*      */   {
/* 1150 */     logger.log(Level.FINE, "Loading encrypted element...");
/* 1151 */     if (null == paramDocument)
/* 1152 */       logger.log(Level.SEVERE, "Context document unexpectedly null...");
/* 1153 */     if (null == paramElement)
/* 1154 */       logger.log(Level.SEVERE, "Element unexpectedly null...");
/* 1155 */     if (this._cipherMode != 2) {
/* 1156 */       logger.log(Level.SEVERE, "XMLCipher unexpectedly not in DECRYPT_MODE...");
/*      */     }
/* 1158 */     this._contextDocument = paramDocument;
/* 1159 */     this._ed = this._factory.newEncryptedData(paramElement);
/*      */ 
/* 1161 */     return this._ed;
/*      */   }
/*      */ 
/*      */   public EncryptedKey loadEncryptedKey(Document paramDocument, Element paramElement)
/*      */     throws XMLEncryptionException
/*      */   {
/* 1177 */     logger.log(Level.FINE, "Loading encrypted key...");
/* 1178 */     if (null == paramDocument)
/* 1179 */       logger.log(Level.SEVERE, "Context document unexpectedly null...");
/* 1180 */     if (null == paramElement)
/* 1181 */       logger.log(Level.SEVERE, "Element unexpectedly null...");
/* 1182 */     if ((this._cipherMode != 4) && (this._cipherMode != 2)) {
/* 1183 */       logger.log(Level.FINE, "XMLCipher unexpectedly not in UNWRAP_MODE or DECRYPT_MODE...");
/*      */     }
/* 1185 */     this._contextDocument = paramDocument;
/* 1186 */     this._ek = this._factory.newEncryptedKey(paramElement);
/* 1187 */     return this._ek;
/*      */   }
/*      */ 
/*      */   public EncryptedKey loadEncryptedKey(Element paramElement)
/*      */     throws XMLEncryptionException
/*      */   {
/* 1205 */     return loadEncryptedKey(paramElement.getOwnerDocument(), paramElement);
/*      */   }
/*      */ 
/*      */   public EncryptedKey encryptKey(Document paramDocument, Key paramKey)
/*      */     throws XMLEncryptionException
/*      */   {
/* 1221 */     logger.log(Level.FINE, "Encrypting key ...");
/*      */ 
/* 1223 */     if (null == paramKey)
/* 1224 */       logger.log(Level.SEVERE, "Key unexpectedly null...");
/* 1225 */     if (this._cipherMode != 3) {
/* 1226 */       logger.log(Level.FINE, "XMLCipher unexpectedly not in WRAP_MODE...");
/*      */     }
/* 1228 */     if (this._algorithm == null)
/*      */     {
/* 1230 */       throw new XMLEncryptionException("XMLCipher instance without transformation specified");
/*      */     }
/*      */ 
/* 1233 */     this._contextDocument = paramDocument;
/*      */ 
/* 1235 */     byte[] arrayOfByte = null;
/*      */     Cipher localCipher;
/* 1238 */     if (this._contextCipher == null)
/*      */     {
/* 1241 */       String str1 = JCEMapper.translateURItoJCEID(this._algorithm);
/*      */ 
/* 1244 */       logger.log(Level.FINE, "alg = " + str1);
/*      */       try
/*      */       {
/* 1247 */         if (this._requestedJCEProvider == null)
/* 1248 */           localCipher = Cipher.getInstance(str1);
/*      */         else
/* 1250 */           localCipher = Cipher.getInstance(str1, this._requestedJCEProvider);
/*      */       } catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
/* 1252 */         throw new XMLEncryptionException("empty", localNoSuchAlgorithmException);
/*      */       } catch (NoSuchProviderException localNoSuchProviderException) {
/* 1254 */         throw new XMLEncryptionException("empty", localNoSuchProviderException);
/*      */       } catch (NoSuchPaddingException localNoSuchPaddingException) {
/* 1256 */         throw new XMLEncryptionException("empty", localNoSuchPaddingException);
/*      */       }
/*      */     } else {
/* 1259 */       localCipher = this._contextCipher;
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/* 1266 */       localCipher.init(3, this._key);
/* 1267 */       arrayOfByte = localCipher.wrap(paramKey);
/*      */     } catch (InvalidKeyException localInvalidKeyException) {
/* 1269 */       throw new XMLEncryptionException("empty", localInvalidKeyException);
/*      */     } catch (IllegalBlockSizeException localIllegalBlockSizeException) {
/* 1271 */       throw new XMLEncryptionException("empty", localIllegalBlockSizeException);
/*      */     }
/*      */ 
/* 1274 */     String str2 = Base64.encode(arrayOfByte);
/*      */ 
/* 1276 */     logger.log(Level.FINE, "Encrypted key octets:\n" + str2);
/* 1277 */     logger.log(Level.FINE, "Encrypted key octets length = " + str2.length());
/*      */ 
/* 1280 */     CipherValue localCipherValue = this._ek.getCipherData().getCipherValue();
/* 1281 */     localCipherValue.setValue(str2);
/*      */     try
/*      */     {
/* 1284 */       EncryptionMethod localEncryptionMethod = this._factory.newEncryptionMethod(new URI(this._algorithm).toString());
/*      */ 
/* 1286 */       this._ek.setEncryptionMethod(localEncryptionMethod);
/*      */     } catch (URI.MalformedURIException localMalformedURIException) {
/* 1288 */       throw new XMLEncryptionException("empty", localMalformedURIException);
/*      */     }
/* 1290 */     return this._ek;
/*      */   }
/*      */ 
/*      */   public Key decryptKey(EncryptedKey paramEncryptedKey, String paramString)
/*      */     throws XMLEncryptionException
/*      */   {
/* 1307 */     logger.log(Level.FINE, "Decrypting key from previously loaded EncryptedKey...");
/*      */ 
/* 1309 */     if (this._cipherMode != 4) {
/* 1310 */       logger.log(Level.FINE, "XMLCipher unexpectedly not in UNWRAP_MODE...");
/*      */     }
/* 1312 */     if (paramString == null) {
/* 1313 */       throw new XMLEncryptionException("Cannot decrypt a key without knowing the algorithm");
/*      */     }
/*      */ 
/* 1316 */     if (this._key == null)
/*      */     {
/* 1318 */       logger.log(Level.FINE, "Trying to find a KEK via key resolvers");
/*      */ 
/* 1320 */       localObject1 = paramEncryptedKey.getKeyInfo();
/* 1321 */       if (localObject1 != null)
/*      */         try {
/* 1323 */           this._key = ((KeyInfo)localObject1).getSecretKey();
/*      */         }
/*      */         catch (Exception localException)
/*      */         {
/*      */         }
/* 1328 */       if (this._key == null) {
/* 1329 */         logger.log(Level.SEVERE, "XMLCipher::decryptKey called without a KEK and cannot resolve");
/* 1330 */         throw new XMLEncryptionException("Unable to decrypt without a KEK");
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1335 */     Object localObject1 = new XMLCipherInput(paramEncryptedKey);
/* 1336 */     byte[] arrayOfByte = ((XMLCipherInput)localObject1).getBytes();
/*      */ 
/* 1338 */     String str = JCEMapper.getJCEKeyAlgorithmFromURI(paramString);
/*      */     Object localObject2;
/*      */     Cipher localCipher;
/* 1342 */     if (this._contextCipher == null)
/*      */     {
/* 1345 */       localObject2 = JCEMapper.translateURItoJCEID(paramEncryptedKey.getEncryptionMethod().getAlgorithm());
/*      */ 
/* 1349 */       logger.log(Level.FINE, "JCE Algorithm = " + (String)localObject2);
/*      */       try
/*      */       {
/* 1352 */         if (this._requestedJCEProvider == null)
/* 1353 */           localCipher = Cipher.getInstance((String)localObject2);
/*      */         else
/* 1355 */           localCipher = Cipher.getInstance((String)localObject2, this._requestedJCEProvider);
/*      */       } catch (NoSuchAlgorithmException localNoSuchAlgorithmException1) {
/* 1357 */         throw new XMLEncryptionException("empty", localNoSuchAlgorithmException1);
/*      */       } catch (NoSuchProviderException localNoSuchProviderException) {
/* 1359 */         throw new XMLEncryptionException("empty", localNoSuchProviderException);
/*      */       } catch (NoSuchPaddingException localNoSuchPaddingException) {
/* 1361 */         throw new XMLEncryptionException("empty", localNoSuchPaddingException);
/*      */       }
/*      */     } else {
/* 1364 */       localCipher = this._contextCipher;
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/* 1370 */       localCipher.init(4, this._key);
/* 1371 */       localObject2 = localCipher.unwrap(arrayOfByte, str, 3);
/*      */     }
/*      */     catch (InvalidKeyException localInvalidKeyException) {
/* 1374 */       throw new XMLEncryptionException("empty", localInvalidKeyException);
/*      */     } catch (NoSuchAlgorithmException localNoSuchAlgorithmException2) {
/* 1376 */       throw new XMLEncryptionException("empty", localNoSuchAlgorithmException2);
/*      */     }
/*      */ 
/* 1379 */     logger.log(Level.FINE, "Decryption of key type " + paramString + " OK");
/*      */ 
/* 1381 */     return localObject2;
/*      */   }
/*      */ 
/*      */   public Key decryptKey(EncryptedKey paramEncryptedKey)
/*      */     throws XMLEncryptionException
/*      */   {
/* 1400 */     return decryptKey(paramEncryptedKey, this._ed.getEncryptionMethod().getAlgorithm());
/*      */   }
/*      */ 
/*      */   private static void removeContent(Node paramNode)
/*      */   {
/* 1410 */     while (paramNode.hasChildNodes())
/* 1411 */       paramNode.removeChild(paramNode.getFirstChild());
/*      */   }
/*      */ 
/*      */   private Document decryptElement(Element paramElement)
/*      */     throws XMLEncryptionException
/*      */   {
/* 1425 */     logger.log(Level.FINE, "Decrypting element...");
/*      */ 
/* 1427 */     if (this._cipherMode != 2)
/* 1428 */       logger.log(Level.SEVERE, "XMLCipher unexpectedly not in DECRYPT_MODE...");
/*      */     String str;
/*      */     try
/*      */     {
/* 1432 */       str = new String(decryptToByteArray(paramElement), "UTF-8");
/*      */     } catch (UnsupportedEncodingException localUnsupportedEncodingException) {
/* 1434 */       throw new XMLEncryptionException("empty", localUnsupportedEncodingException);
/*      */     }
/*      */ 
/* 1438 */     logger.log(Level.FINE, "Decrypted octets:\n" + str);
/*      */ 
/* 1440 */     Node localNode = paramElement.getParentNode();
/*      */ 
/* 1442 */     DocumentFragment localDocumentFragment = this._serializer.deserialize(str, localNode);
/*      */ 
/* 1449 */     if ((localNode != null) && (localNode.getNodeType() == 9))
/*      */     {
/* 1453 */       this._contextDocument.removeChild(this._contextDocument.getDocumentElement());
/* 1454 */       this._contextDocument.appendChild(localDocumentFragment);
/*      */     }
/*      */     else {
/* 1457 */       localNode.replaceChild(localDocumentFragment, paramElement);
/*      */     }
/*      */ 
/* 1461 */     return this._contextDocument;
/*      */   }
/*      */ 
/*      */   private Document decryptElementContent(Element paramElement)
/*      */     throws XMLEncryptionException
/*      */   {
/* 1473 */     Element localElement = (Element)paramElement.getElementsByTagNameNS("http://www.w3.org/2001/04/xmlenc#", "EncryptedData").item(0);
/*      */ 
/* 1477 */     if (null == localElement) {
/* 1478 */       throw new XMLEncryptionException("No EncryptedData child element.");
/*      */     }
/*      */ 
/* 1481 */     return decryptElement(localElement);
/*      */   }
/*      */ 
/*      */   public byte[] decryptToByteArray(Element paramElement)
/*      */     throws XMLEncryptionException
/*      */   {
/* 1499 */     logger.log(Level.FINE, "Decrypting to ByteArray...");
/*      */ 
/* 1501 */     if (this._cipherMode != 2) {
/* 1502 */       logger.log(Level.SEVERE, "XMLCipher unexpectedly not in DECRYPT_MODE...");
/*      */     }
/* 1504 */     EncryptedData localEncryptedData = this._factory.newEncryptedData(paramElement);
/*      */ 
/* 1506 */     if (this._key == null)
/*      */     {
/* 1508 */       localObject = localEncryptedData.getKeyInfo();
/*      */ 
/* 1510 */       if (localObject != null) {
/*      */         try
/*      */         {
/* 1513 */           ((KeyInfo)localObject).registerInternalKeyResolver(new EncryptedKeyResolver(localEncryptedData.getEncryptionMethod().getAlgorithm(), this._kek));
/*      */ 
/* 1518 */           this._key = ((KeyInfo)localObject).getSecretKey();
/*      */         }
/*      */         catch (KeyResolverException localKeyResolverException)
/*      */         {
/*      */         }
/*      */       }
/* 1524 */       if (this._key == null) {
/* 1525 */         logger.log(Level.SEVERE, "XMLCipher::decryptElement called without a key and unable to resolve");
/*      */ 
/* 1527 */         throw new XMLEncryptionException("encryption.nokey");
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1532 */     Object localObject = new XMLCipherInput(localEncryptedData);
/* 1533 */     byte[] arrayOfByte1 = ((XMLCipherInput)localObject).getBytes();
/*      */ 
/* 1537 */     String str = JCEMapper.translateURItoJCEID(localEncryptedData.getEncryptionMethod().getAlgorithm());
/*      */     Cipher localCipher;
/*      */     try
/*      */     {
/* 1542 */       if (this._requestedJCEProvider == null)
/* 1543 */         localCipher = Cipher.getInstance(str);
/*      */       else
/* 1545 */         localCipher = Cipher.getInstance(str, this._requestedJCEProvider);
/*      */     } catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
/* 1547 */       throw new XMLEncryptionException("empty", localNoSuchAlgorithmException);
/*      */     } catch (NoSuchProviderException localNoSuchProviderException) {
/* 1549 */       throw new XMLEncryptionException("empty", localNoSuchProviderException);
/*      */     } catch (NoSuchPaddingException localNoSuchPaddingException) {
/* 1551 */       throw new XMLEncryptionException("empty", localNoSuchPaddingException);
/*      */     }
/*      */ 
/* 1559 */     int i = localCipher.getBlockSize();
/* 1560 */     byte[] arrayOfByte2 = new byte[i];
/*      */ 
/* 1567 */     System.arraycopy(arrayOfByte1, 0, arrayOfByte2, 0, i);
/* 1568 */     IvParameterSpec localIvParameterSpec = new IvParameterSpec(arrayOfByte2);
/*      */     try
/*      */     {
/* 1571 */       localCipher.init(this._cipherMode, this._key, localIvParameterSpec);
/*      */     } catch (InvalidKeyException localInvalidKeyException) {
/* 1573 */       throw new XMLEncryptionException("empty", localInvalidKeyException);
/*      */     } catch (InvalidAlgorithmParameterException localInvalidAlgorithmParameterException) {
/* 1575 */       throw new XMLEncryptionException("empty", localInvalidAlgorithmParameterException);
/*      */     }
/*      */ 
/*      */     byte[] arrayOfByte3;
/*      */     try
/*      */     {
/* 1581 */       arrayOfByte3 = localCipher.doFinal(arrayOfByte1, i, arrayOfByte1.length - i);
/*      */     }
/*      */     catch (IllegalBlockSizeException localIllegalBlockSizeException)
/*      */     {
/* 1586 */       throw new XMLEncryptionException("empty", localIllegalBlockSizeException);
/*      */     } catch (BadPaddingException localBadPaddingException) {
/* 1588 */       throw new XMLEncryptionException("empty", localBadPaddingException);
/*      */     }
/*      */ 
/* 1591 */     return arrayOfByte3;
/*      */   }
/*      */ 
/*      */   public EncryptedData createEncryptedData(int paramInt, String paramString)
/*      */     throws XMLEncryptionException
/*      */   {
/* 1635 */     EncryptedData localEncryptedData = null;
/* 1636 */     CipherData localCipherData = null;
/*      */ 
/* 1638 */     switch (paramInt) {
/*      */     case 2:
/* 1640 */       CipherReference localCipherReference = this._factory.newCipherReference(paramString);
/*      */ 
/* 1642 */       localCipherData = this._factory.newCipherData(paramInt);
/* 1643 */       localCipherData.setCipherReference(localCipherReference);
/* 1644 */       localEncryptedData = this._factory.newEncryptedData(localCipherData);
/* 1645 */       break;
/*      */     case 1:
/* 1647 */       CipherValue localCipherValue = this._factory.newCipherValue(paramString);
/* 1648 */       localCipherData = this._factory.newCipherData(paramInt);
/* 1649 */       localCipherData.setCipherValue(localCipherValue);
/* 1650 */       localEncryptedData = this._factory.newEncryptedData(localCipherData);
/*      */     }
/*      */ 
/* 1653 */     return localEncryptedData;
/*      */   }
/*      */ 
/*      */   public EncryptedKey createEncryptedKey(int paramInt, String paramString)
/*      */     throws XMLEncryptionException
/*      */   {
/* 1693 */     EncryptedKey localEncryptedKey = null;
/* 1694 */     CipherData localCipherData = null;
/*      */ 
/* 1696 */     switch (paramInt) {
/*      */     case 2:
/* 1698 */       CipherReference localCipherReference = this._factory.newCipherReference(paramString);
/*      */ 
/* 1700 */       localCipherData = this._factory.newCipherData(paramInt);
/* 1701 */       localCipherData.setCipherReference(localCipherReference);
/* 1702 */       localEncryptedKey = this._factory.newEncryptedKey(localCipherData);
/* 1703 */       break;
/*      */     case 1:
/* 1705 */       CipherValue localCipherValue = this._factory.newCipherValue(paramString);
/* 1706 */       localCipherData = this._factory.newCipherData(paramInt);
/* 1707 */       localCipherData.setCipherValue(localCipherValue);
/* 1708 */       localEncryptedKey = this._factory.newEncryptedKey(localCipherData);
/*      */     }
/*      */ 
/* 1711 */     return localEncryptedKey;
/*      */   }
/*      */ 
/*      */   public AgreementMethod createAgreementMethod(String paramString)
/*      */   {
/* 1722 */     return this._factory.newAgreementMethod(paramString);
/*      */   }
/*      */ 
/*      */   public CipherData createCipherData(int paramInt)
/*      */   {
/* 1734 */     return this._factory.newCipherData(paramInt);
/*      */   }
/*      */ 
/*      */   public CipherReference createCipherReference(String paramString)
/*      */   {
/* 1745 */     return this._factory.newCipherReference(paramString);
/*      */   }
/*      */ 
/*      */   public CipherValue createCipherValue(String paramString)
/*      */   {
/* 1756 */     return this._factory.newCipherValue(paramString);
/*      */   }
/*      */ 
/*      */   public EncryptionMethod createEncryptionMethod(String paramString)
/*      */   {
/* 1766 */     return this._factory.newEncryptionMethod(paramString);
/*      */   }
/*      */ 
/*      */   public EncryptionProperties createEncryptionProperties()
/*      */   {
/* 1774 */     return this._factory.newEncryptionProperties();
/*      */   }
/*      */ 
/*      */   public EncryptionProperty createEncryptionProperty()
/*      */   {
/* 1782 */     return this._factory.newEncryptionProperty();
/*      */   }
/*      */ 
/*      */   public ReferenceList createReferenceList(int paramInt)
/*      */   {
/* 1791 */     return this._factory.newReferenceList(paramInt);
/*      */   }
/*      */ 
/*      */   public Transforms createTransforms()
/*      */   {
/* 1804 */     return this._factory.newTransforms();
/*      */   }
/*      */ 
/*      */   public Transforms createTransforms(Document paramDocument)
/*      */   {
/* 1818 */     return this._factory.newTransforms(paramDocument);
/*      */   }
/*      */ 
/*      */   private class Factory
/*      */   {
/*      */     private Factory()
/*      */     {
/*      */     }
/*      */ 
/*      */     AgreementMethod newAgreementMethod(String paramString)
/*      */     {
/* 2027 */       return new AgreementMethodImpl(paramString);
/*      */     }
/*      */ 
/*      */     CipherData newCipherData(int paramInt)
/*      */     {
/* 2036 */       return new CipherDataImpl(paramInt);
/*      */     }
/*      */ 
/*      */     CipherReference newCipherReference(String paramString)
/*      */     {
/* 2045 */       return new CipherReferenceImpl(paramString);
/*      */     }
/*      */ 
/*      */     CipherValue newCipherValue(String paramString)
/*      */     {
/* 2054 */       return new CipherValueImpl(paramString);
/*      */     }
/*      */ 
/*      */     EncryptedData newEncryptedData(CipherData paramCipherData)
/*      */     {
/* 2070 */       return new EncryptedDataImpl(paramCipherData);
/*      */     }
/*      */ 
/*      */     EncryptedKey newEncryptedKey(CipherData paramCipherData)
/*      */     {
/* 2079 */       return new EncryptedKeyImpl(paramCipherData);
/*      */     }
/*      */ 
/*      */     EncryptionMethod newEncryptionMethod(String paramString)
/*      */     {
/* 2088 */       return new EncryptionMethodImpl(paramString);
/*      */     }
/*      */ 
/*      */     EncryptionProperties newEncryptionProperties()
/*      */     {
/* 2096 */       return new EncryptionPropertiesImpl();
/*      */     }
/*      */ 
/*      */     EncryptionProperty newEncryptionProperty()
/*      */     {
/* 2104 */       return new EncryptionPropertyImpl();
/*      */     }
/*      */ 
/*      */     ReferenceList newReferenceList(int paramInt)
/*      */     {
/* 2113 */       return new ReferenceListImpl(paramInt);
/*      */     }
/*      */ 
/*      */     Transforms newTransforms()
/*      */     {
/* 2121 */       return new TransformsImpl();
/*      */     }
/*      */ 
/*      */     Transforms newTransforms(Document paramDocument)
/*      */     {
/* 2130 */       return new TransformsImpl(paramDocument);
/*      */     }
/*      */ 
/*      */     AgreementMethod newAgreementMethod(Element paramElement)
/*      */       throws XMLEncryptionException
/*      */     {
/* 2152 */       if (null == paramElement) {
/* 2153 */         throw new NullPointerException("element is null");
/*      */       }
/*      */ 
/* 2156 */       String str = paramElement.getAttributeNS(null, "Algorithm");
/*      */ 
/* 2158 */       AgreementMethod localAgreementMethod = newAgreementMethod(str);
/*      */ 
/* 2160 */       Element localElement1 = (Element)paramElement.getElementsByTagNameNS("http://www.w3.org/2001/04/xmlenc#", "KA-Nonce").item(0);
/*      */ 
/* 2163 */       if (null != localElement1) {
/* 2164 */         localAgreementMethod.setKANonce(localElement1.getNodeValue().getBytes());
/*      */       }
/*      */ 
/* 2172 */       Element localElement2 = (Element)paramElement.getElementsByTagNameNS("http://www.w3.org/2001/04/xmlenc#", "OriginatorKeyInfo").item(0);
/*      */ 
/* 2176 */       if (null != localElement2) {
/*      */         try {
/* 2178 */           localAgreementMethod.setOriginatorKeyInfo(new KeyInfo(localElement2, null));
/*      */         }
/*      */         catch (XMLSecurityException localXMLSecurityException1) {
/* 2181 */           throw new XMLEncryptionException("empty", localXMLSecurityException1);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 2187 */       Element localElement3 = (Element)paramElement.getElementsByTagNameNS("http://www.w3.org/2001/04/xmlenc#", "RecipientKeyInfo").item(0);
/*      */ 
/* 2191 */       if (null != localElement3) {
/*      */         try {
/* 2193 */           localAgreementMethod.setRecipientKeyInfo(new KeyInfo(localElement3, null));
/*      */         }
/*      */         catch (XMLSecurityException localXMLSecurityException2) {
/* 2196 */           throw new XMLEncryptionException("empty", localXMLSecurityException2);
/*      */         }
/*      */       }
/*      */ 
/* 2200 */       return localAgreementMethod;
/*      */     }
/*      */ 
/*      */     CipherData newCipherData(Element paramElement)
/*      */       throws XMLEncryptionException
/*      */     {
/* 2218 */       if (null == paramElement) {
/* 2219 */         throw new NullPointerException("element is null");
/*      */       }
/*      */ 
/* 2222 */       int i = 0;
/* 2223 */       Element localElement = null;
/* 2224 */       if (paramElement.getElementsByTagNameNS("http://www.w3.org/2001/04/xmlenc#", "CipherValue").getLength() > 0)
/*      */       {
/* 2227 */         i = 1;
/* 2228 */         localElement = (Element)paramElement.getElementsByTagNameNS("http://www.w3.org/2001/04/xmlenc#", "CipherValue").item(0);
/*      */       }
/* 2231 */       else if (paramElement.getElementsByTagNameNS("http://www.w3.org/2001/04/xmlenc#", "CipherReference").getLength() > 0)
/*      */       {
/* 2234 */         i = 2;
/* 2235 */         localElement = (Element)paramElement.getElementsByTagNameNS("http://www.w3.org/2001/04/xmlenc#", "CipherReference").item(0);
/*      */       }
/*      */ 
/* 2240 */       CipherData localCipherData = newCipherData(i);
/* 2241 */       if (i == 1)
/* 2242 */         localCipherData.setCipherValue(newCipherValue(localElement));
/* 2243 */       else if (i == 2) {
/* 2244 */         localCipherData.setCipherReference(newCipherReference(localElement));
/*      */       }
/*      */ 
/* 2247 */       return localCipherData;
/*      */     }
/*      */ 
/*      */     CipherReference newCipherReference(Element paramElement)
/*      */       throws XMLEncryptionException
/*      */     {
/* 2266 */       Attr localAttr = paramElement.getAttributeNodeNS(null, "URI");
/*      */ 
/* 2268 */       CipherReferenceImpl localCipherReferenceImpl = new CipherReferenceImpl(localAttr);
/*      */ 
/* 2272 */       NodeList localNodeList = paramElement.getElementsByTagNameNS("http://www.w3.org/2001/04/xmlenc#", "Transforms");
/*      */ 
/* 2275 */       Element localElement = (Element)localNodeList.item(0);
/*      */ 
/* 2278 */       if (localElement != null) {
/* 2279 */         XMLCipher.logger.log(Level.FINE, "Creating a DSIG based Transforms element");
/*      */         try {
/* 2281 */           localCipherReferenceImpl.setTransforms(new TransformsImpl(localElement));
/*      */         }
/*      */         catch (XMLSignatureException localXMLSignatureException) {
/* 2284 */           throw new XMLEncryptionException("empty", localXMLSignatureException);
/*      */         } catch (InvalidTransformException localInvalidTransformException) {
/* 2286 */           throw new XMLEncryptionException("empty", localInvalidTransformException);
/*      */         } catch (XMLSecurityException localXMLSecurityException) {
/* 2288 */           throw new XMLEncryptionException("empty", localXMLSecurityException);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 2293 */       return localCipherReferenceImpl;
/*      */     }
/*      */ 
/*      */     CipherValue newCipherValue(Element paramElement)
/*      */     {
/* 2302 */       String str = XMLUtils.getFullTextChildrenFromElement(paramElement);
/*      */ 
/* 2304 */       CipherValue localCipherValue = newCipherValue(str);
/*      */ 
/* 2306 */       return localCipherValue;
/*      */     }
/*      */ 
/*      */     EncryptedData newEncryptedData(Element paramElement)
/*      */       throws XMLEncryptionException
/*      */     {
/* 2336 */       EncryptedData localEncryptedData = null;
/*      */ 
/* 2338 */       NodeList localNodeList = paramElement.getElementsByTagNameNS("http://www.w3.org/2001/04/xmlenc#", "CipherData");
/*      */ 
/* 2345 */       Element localElement1 = (Element)localNodeList.item(localNodeList.getLength() - 1);
/*      */ 
/* 2348 */       CipherData localCipherData = newCipherData(localElement1);
/*      */ 
/* 2350 */       localEncryptedData = newEncryptedData(localCipherData);
/*      */ 
/* 2352 */       localEncryptedData.setId(paramElement.getAttributeNS(null, "Id"));
/*      */ 
/* 2354 */       localEncryptedData.setType(paramElement.getAttributeNS(null, "Type"));
/*      */ 
/* 2356 */       localEncryptedData.setMimeType(paramElement.getAttributeNS(null, "MimeType"));
/*      */ 
/* 2358 */       localEncryptedData.setEncoding(paramElement.getAttributeNS(null, "Encoding"));
/*      */ 
/* 2361 */       Element localElement2 = (Element)paramElement.getElementsByTagNameNS("http://www.w3.org/2001/04/xmlenc#", "EncryptionMethod").item(0);
/*      */ 
/* 2365 */       if (null != localElement2) {
/* 2366 */         localEncryptedData.setEncryptionMethod(newEncryptionMethod(localElement2));
/*      */       }
/*      */ 
/* 2373 */       Element localElement3 = (Element)paramElement.getElementsByTagNameNS("http://www.w3.org/2000/09/xmldsig#", "KeyInfo").item(0);
/*      */ 
/* 2376 */       if (null != localElement3) {
/*      */         try {
/* 2378 */           localEncryptedData.setKeyInfo(new KeyInfo(localElement3, null));
/*      */         } catch (XMLSecurityException localXMLSecurityException) {
/* 2380 */           throw new XMLEncryptionException("Error loading Key Info", localXMLSecurityException);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 2386 */       Element localElement4 = (Element)paramElement.getElementsByTagNameNS("http://www.w3.org/2001/04/xmlenc#", "EncryptionProperties").item(0);
/*      */ 
/* 2390 */       if (null != localElement4) {
/* 2391 */         localEncryptedData.setEncryptionProperties(newEncryptionProperties(localElement4));
/*      */       }
/*      */ 
/* 2395 */       return localEncryptedData;
/*      */     }
/*      */ 
/*      */     EncryptedKey newEncryptedKey(Element paramElement)
/*      */       throws XMLEncryptionException
/*      */     {
/* 2431 */       EncryptedKey localEncryptedKey = null;
/* 2432 */       NodeList localNodeList = paramElement.getElementsByTagNameNS("http://www.w3.org/2001/04/xmlenc#", "CipherData");
/*      */ 
/* 2435 */       Element localElement1 = (Element)localNodeList.item(localNodeList.getLength() - 1);
/*      */ 
/* 2438 */       CipherData localCipherData = newCipherData(localElement1);
/* 2439 */       localEncryptedKey = newEncryptedKey(localCipherData);
/*      */ 
/* 2441 */       localEncryptedKey.setId(paramElement.getAttributeNS(null, "Id"));
/*      */ 
/* 2443 */       localEncryptedKey.setType(paramElement.getAttributeNS(null, "Type"));
/*      */ 
/* 2445 */       localEncryptedKey.setMimeType(paramElement.getAttributeNS(null, "MimeType"));
/*      */ 
/* 2447 */       localEncryptedKey.setEncoding(paramElement.getAttributeNS(null, "Encoding"));
/*      */ 
/* 2449 */       localEncryptedKey.setRecipient(paramElement.getAttributeNS(null, "Recipient"));
/*      */ 
/* 2452 */       Element localElement2 = (Element)paramElement.getElementsByTagNameNS("http://www.w3.org/2001/04/xmlenc#", "EncryptionMethod").item(0);
/*      */ 
/* 2456 */       if (null != localElement2) {
/* 2457 */         localEncryptedKey.setEncryptionMethod(newEncryptionMethod(localElement2));
/*      */       }
/*      */ 
/* 2461 */       Element localElement3 = (Element)paramElement.getElementsByTagNameNS("http://www.w3.org/2000/09/xmldsig#", "KeyInfo").item(0);
/*      */ 
/* 2464 */       if (null != localElement3) {
/*      */         try {
/* 2466 */           localEncryptedKey.setKeyInfo(new KeyInfo(localElement3, null));
/*      */         } catch (XMLSecurityException localXMLSecurityException) {
/* 2468 */           throw new XMLEncryptionException("Error loading Key Info", localXMLSecurityException);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 2474 */       Element localElement4 = (Element)paramElement.getElementsByTagNameNS("http://www.w3.org/2001/04/xmlenc#", "EncryptionProperties").item(0);
/*      */ 
/* 2478 */       if (null != localElement4) {
/* 2479 */         localEncryptedKey.setEncryptionProperties(newEncryptionProperties(localElement4));
/*      */       }
/*      */ 
/* 2483 */       Element localElement5 = (Element)paramElement.getElementsByTagNameNS("http://www.w3.org/2001/04/xmlenc#", "ReferenceList").item(0);
/*      */ 
/* 2487 */       if (null != localElement5) {
/* 2488 */         localEncryptedKey.setReferenceList(newReferenceList(localElement5));
/*      */       }
/*      */ 
/* 2491 */       Element localElement6 = (Element)paramElement.getElementsByTagNameNS("http://www.w3.org/2001/04/xmlenc#", "CarriedKeyName").item(0);
/*      */ 
/* 2495 */       if (null != localElement6) {
/* 2496 */         localEncryptedKey.setCarriedName(localElement6.getFirstChild().getNodeValue());
/*      */       }
/*      */ 
/* 2500 */       return localEncryptedKey;
/*      */     }
/*      */ 
/*      */     EncryptionMethod newEncryptionMethod(Element paramElement)
/*      */     {
/* 2517 */       String str = paramElement.getAttributeNS(null, "Algorithm");
/*      */ 
/* 2519 */       EncryptionMethod localEncryptionMethod = newEncryptionMethod(str);
/*      */ 
/* 2521 */       Element localElement1 = (Element)paramElement.getElementsByTagNameNS("http://www.w3.org/2001/04/xmlenc#", "KeySize").item(0);
/*      */ 
/* 2525 */       if (null != localElement1) {
/* 2526 */         localEncryptionMethod.setKeySize(Integer.valueOf(localElement1.getFirstChild().getNodeValue()).intValue());
/*      */       }
/*      */ 
/* 2531 */       Element localElement2 = (Element)paramElement.getElementsByTagNameNS("http://www.w3.org/2001/04/xmlenc#", "OAEPparams").item(0);
/*      */ 
/* 2535 */       if (null != localElement2) {
/* 2536 */         localEncryptionMethod.setOAEPparams(localElement2.getNodeValue().getBytes());
/*      */       }
/*      */ 
/* 2543 */       return localEncryptionMethod;
/*      */     }
/*      */ 
/*      */     EncryptionProperties newEncryptionProperties(Element paramElement)
/*      */     {
/* 2559 */       EncryptionProperties localEncryptionProperties = newEncryptionProperties();
/*      */ 
/* 2561 */       localEncryptionProperties.setId(paramElement.getAttributeNS(null, "Id"));
/*      */ 
/* 2564 */       NodeList localNodeList = paramElement.getElementsByTagNameNS("http://www.w3.org/2001/04/xmlenc#", "EncryptionProperty");
/*      */ 
/* 2568 */       for (int i = 0; i < localNodeList.getLength(); i++) {
/* 2569 */         Node localNode = localNodeList.item(i);
/* 2570 */         if (null != localNode) {
/* 2571 */           localEncryptionProperties.addEncryptionProperty(newEncryptionProperty((Element)localNode));
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 2576 */       return localEncryptionProperties;
/*      */     }
/*      */ 
/*      */     EncryptionProperty newEncryptionProperty(Element paramElement)
/*      */     {
/* 2594 */       EncryptionProperty localEncryptionProperty = newEncryptionProperty();
/*      */ 
/* 2596 */       localEncryptionProperty.setTarget(paramElement.getAttributeNS(null, "Target"));
/*      */ 
/* 2598 */       localEncryptionProperty.setId(paramElement.getAttributeNS(null, "Id"));
/*      */ 
/* 2606 */       return localEncryptionProperty;
/*      */     }
/*      */ 
/*      */     ReferenceList newReferenceList(Element paramElement)
/*      */     {
/* 2623 */       int i = 0;
/* 2624 */       if (null != paramElement.getElementsByTagNameNS("http://www.w3.org/2001/04/xmlenc#", "DataReference").item(0))
/*      */       {
/* 2627 */         i = 1;
/* 2628 */       } else if (null != paramElement.getElementsByTagNameNS("http://www.w3.org/2001/04/xmlenc#", "KeyReference").item(0))
/*      */       {
/* 2631 */         i = 2;
/*      */       }
/*      */ 
/* 2636 */       ReferenceListImpl localReferenceListImpl = new ReferenceListImpl(i);
/* 2637 */       NodeList localNodeList = null;
/*      */       int j;
/*      */       String str;
/* 2638 */       switch (i) {
/*      */       case 1:
/* 2640 */         localNodeList = paramElement.getElementsByTagNameNS("http://www.w3.org/2001/04/xmlenc#", "DataReference");
/*      */ 
/* 2643 */         for (j = 0; j < localNodeList.getLength(); j++) {
/* 2644 */           str = ((Element)localNodeList.item(j)).getAttribute("URI");
/* 2645 */           localReferenceListImpl.add(localReferenceListImpl.newDataReference(str));
/*      */         }
/* 2647 */         break;
/*      */       case 2:
/* 2649 */         localNodeList = paramElement.getElementsByTagNameNS("http://www.w3.org/2001/04/xmlenc#", "KeyReference");
/*      */ 
/* 2652 */         for (j = 0; j < localNodeList.getLength(); j++) {
/* 2653 */           str = ((Element)localNodeList.item(j)).getAttribute("URI");
/* 2654 */           localReferenceListImpl.add(localReferenceListImpl.newKeyReference(str));
/*      */         }
/*      */       }
/*      */ 
/* 2658 */       return localReferenceListImpl;
/*      */     }
/*      */ 
/*      */     Transforms newTransforms(Element paramElement)
/*      */     {
/* 2667 */       return null;
/*      */     }
/*      */ 
/*      */     Element toElement(AgreementMethod paramAgreementMethod)
/*      */     {
/* 2676 */       return ((AgreementMethodImpl)paramAgreementMethod).toElement();
/*      */     }
/*      */ 
/*      */     Element toElement(CipherData paramCipherData)
/*      */     {
/* 2685 */       return ((CipherDataImpl)paramCipherData).toElement();
/*      */     }
/*      */ 
/*      */     Element toElement(CipherReference paramCipherReference)
/*      */     {
/* 2694 */       return ((CipherReferenceImpl)paramCipherReference).toElement();
/*      */     }
/*      */ 
/*      */     Element toElement(CipherValue paramCipherValue)
/*      */     {
/* 2703 */       return ((CipherValueImpl)paramCipherValue).toElement();
/*      */     }
/*      */ 
/*      */     Element toElement(EncryptedData paramEncryptedData)
/*      */     {
/* 2712 */       return ((EncryptedDataImpl)paramEncryptedData).toElement();
/*      */     }
/*      */ 
/*      */     Element toElement(EncryptedKey paramEncryptedKey)
/*      */     {
/* 2721 */       return ((EncryptedKeyImpl)paramEncryptedKey).toElement();
/*      */     }
/*      */ 
/*      */     Element toElement(EncryptionMethod paramEncryptionMethod)
/*      */     {
/* 2730 */       return ((EncryptionMethodImpl)paramEncryptionMethod).toElement();
/*      */     }
/*      */ 
/*      */     Element toElement(EncryptionProperties paramEncryptionProperties)
/*      */     {
/* 2739 */       return ((EncryptionPropertiesImpl)paramEncryptionProperties).toElement();
/*      */     }
/*      */ 
/*      */     Element toElement(EncryptionProperty paramEncryptionProperty)
/*      */     {
/* 2748 */       return ((EncryptionPropertyImpl)paramEncryptionProperty).toElement();
/*      */     }
/*      */ 
/*      */     Element toElement(ReferenceList paramReferenceList) {
/* 2752 */       return ((ReferenceListImpl)paramReferenceList).toElement();
/*      */     }
/*      */ 
/*      */     Element toElement(Transforms paramTransforms)
/*      */     {
/* 2761 */       return ((TransformsImpl)paramTransforms).toElement();
/*      */     }
/*      */ 
/*      */     private class AgreementMethodImpl
/*      */       implements AgreementMethod
/*      */     {
/* 2776 */       private byte[] kaNonce = null;
/* 2777 */       private List agreementMethodInformation = null;
/* 2778 */       private KeyInfo originatorKeyInfo = null;
/* 2779 */       private KeyInfo recipientKeyInfo = null;
/* 2780 */       private String algorithmURI = null;
/*      */ 
/*      */       public AgreementMethodImpl(String arg2)
/*      */       {
/* 2786 */         this.agreementMethodInformation = new LinkedList();
/* 2787 */         URI localURI = null;
/*      */         try
/*      */         {
/*      */           String str;
/* 2789 */           localURI = new URI(str);
/*      */         }
/*      */         catch (URI.MalformedURIException localMalformedURIException) {
/*      */         }
/* 2793 */         this.algorithmURI = localURI.toString();
/*      */       }
/*      */ 
/*      */       public byte[] getKANonce()
/*      */       {
/* 2798 */         return this.kaNonce;
/*      */       }
/*      */ 
/*      */       public void setKANonce(byte[] paramArrayOfByte)
/*      */       {
/* 2803 */         this.kaNonce = paramArrayOfByte;
/*      */       }
/*      */ 
/*      */       public Iterator getAgreementMethodInformation()
/*      */       {
/* 2808 */         return this.agreementMethodInformation.iterator();
/*      */       }
/*      */ 
/*      */       public void addAgreementMethodInformation(Element paramElement)
/*      */       {
/* 2813 */         this.agreementMethodInformation.add(paramElement);
/*      */       }
/*      */ 
/*      */       public void revoveAgreementMethodInformation(Element paramElement)
/*      */       {
/* 2818 */         this.agreementMethodInformation.remove(paramElement);
/*      */       }
/*      */ 
/*      */       public KeyInfo getOriginatorKeyInfo()
/*      */       {
/* 2823 */         return this.originatorKeyInfo;
/*      */       }
/*      */ 
/*      */       public void setOriginatorKeyInfo(KeyInfo paramKeyInfo)
/*      */       {
/* 2828 */         this.originatorKeyInfo = paramKeyInfo;
/*      */       }
/*      */ 
/*      */       public KeyInfo getRecipientKeyInfo()
/*      */       {
/* 2833 */         return this.recipientKeyInfo;
/*      */       }
/*      */ 
/*      */       public void setRecipientKeyInfo(KeyInfo paramKeyInfo)
/*      */       {
/* 2838 */         this.recipientKeyInfo = paramKeyInfo;
/*      */       }
/*      */ 
/*      */       public String getAlgorithm()
/*      */       {
/* 2843 */         return this.algorithmURI;
/*      */       }
/*      */ 
/*      */       public void setAlgorithm(String paramString)
/*      */       {
/* 2848 */         URI localURI = null;
/*      */         try {
/* 2850 */           localURI = new URI(paramString);
/*      */         }
/*      */         catch (URI.MalformedURIException localMalformedURIException) {
/*      */         }
/* 2854 */         this.algorithmURI = localURI.toString();
/*      */       }
/*      */ 
/*      */       Element toElement()
/*      */       {
/* 2869 */         Element localElement = ElementProxy.createElementForFamily(XMLCipher.this._contextDocument, "http://www.w3.org/2001/04/xmlenc#", "AgreementMethod");
/*      */ 
/* 2873 */         localElement.setAttributeNS(null, "Algorithm", this.algorithmURI);
/*      */ 
/* 2875 */         if (null != this.kaNonce) {
/* 2876 */           localElement.appendChild(ElementProxy.createElementForFamily(XMLCipher.this._contextDocument, "http://www.w3.org/2001/04/xmlenc#", "KA-Nonce")).appendChild(XMLCipher.this._contextDocument.createTextNode(new String(this.kaNonce)));
/*      */         }
/*      */ 
/* 2883 */         if (!this.agreementMethodInformation.isEmpty()) {
/* 2884 */           Iterator localIterator = this.agreementMethodInformation.iterator();
/* 2885 */           while (localIterator.hasNext()) {
/* 2886 */             localElement.appendChild((Element)localIterator.next());
/*      */           }
/*      */         }
/* 2889 */         if (null != this.originatorKeyInfo) {
/* 2890 */           localElement.appendChild(this.originatorKeyInfo.getElement());
/*      */         }
/* 2892 */         if (null != this.recipientKeyInfo) {
/* 2893 */           localElement.appendChild(this.recipientKeyInfo.getElement());
/*      */         }
/*      */ 
/* 2896 */         return localElement;
/*      */       }
/*      */     }
/*      */ 
/*      */     private class CipherDataImpl
/*      */       implements CipherData
/*      */     {
/*      */       private static final String valueMessage = "Data type is reference type.";
/*      */       private static final String referenceMessage = "Data type is value type.";
/* 2912 */       private CipherValue cipherValue = null;
/* 2913 */       private CipherReference cipherReference = null;
/* 2914 */       private int cipherType = -2147483648;
/*      */ 
/*      */       public CipherDataImpl(int arg2)
/*      */       {
/*      */         int i;
/* 2920 */         this.cipherType = i;
/*      */       }
/*      */ 
/*      */       public CipherValue getCipherValue()
/*      */       {
/* 2925 */         return this.cipherValue;
/*      */       }
/*      */ 
/*      */       public void setCipherValue(CipherValue paramCipherValue)
/*      */         throws XMLEncryptionException
/*      */       {
/* 2932 */         if (this.cipherType == 2) {
/* 2933 */           throw new XMLEncryptionException("empty", new UnsupportedOperationException("Data type is reference type."));
/*      */         }
/*      */ 
/* 2937 */         this.cipherValue = paramCipherValue;
/*      */       }
/*      */ 
/*      */       public CipherReference getCipherReference()
/*      */       {
/* 2942 */         return this.cipherReference;
/*      */       }
/*      */ 
/*      */       public void setCipherReference(CipherReference paramCipherReference)
/*      */         throws XMLEncryptionException
/*      */       {
/* 2948 */         if (this.cipherType == 1) {
/* 2949 */           throw new XMLEncryptionException("empty", new UnsupportedOperationException("Data type is value type."));
/*      */         }
/*      */ 
/* 2953 */         this.cipherReference = paramCipherReference;
/*      */       }
/*      */ 
/*      */       public int getDataType()
/*      */       {
/* 2958 */         return this.cipherType;
/*      */       }
/*      */ 
/*      */       Element toElement()
/*      */       {
/* 2969 */         Element localElement = ElementProxy.createElementForFamily(XMLCipher.this._contextDocument, "http://www.w3.org/2001/04/xmlenc#", "CipherData");
/*      */ 
/* 2973 */         if (this.cipherType == 1) {
/* 2974 */           localElement.appendChild(((XMLCipher.Factory.CipherValueImpl)this.cipherValue).toElement());
/*      */         }
/* 2976 */         else if (this.cipherType == 2) {
/* 2977 */           localElement.appendChild(((XMLCipher.Factory.CipherReferenceImpl)this.cipherReference).toElement());
/*      */         }
/*      */ 
/* 2983 */         return localElement;
/*      */       }
/*      */     }
/*      */ 
/*      */     private class CipherReferenceImpl
/*      */       implements CipherReference
/*      */     {
/* 2995 */       private String referenceURI = null;
/* 2996 */       private Transforms referenceTransforms = null;
/* 2997 */       private Attr referenceNode = null;
/*      */ 
/*      */       public CipherReferenceImpl(String arg2)
/*      */       {
/*      */         Object localObject;
/* 3004 */         this.referenceURI = localObject;
/* 3005 */         this.referenceNode = null;
/*      */       }
/*      */ 
/*      */       public CipherReferenceImpl(Attr arg2)
/*      */       {
/*      */         Object localObject;
/* 3012 */         this.referenceURI = localObject.getNodeValue();
/* 3013 */         this.referenceNode = localObject;
/*      */       }
/*      */ 
/*      */       public String getURI()
/*      */       {
/* 3018 */         return this.referenceURI;
/*      */       }
/*      */ 
/*      */       public Attr getURIAsAttr()
/*      */       {
/* 3023 */         return this.referenceNode;
/*      */       }
/*      */ 
/*      */       public Transforms getTransforms()
/*      */       {
/* 3028 */         return this.referenceTransforms;
/*      */       }
/*      */ 
/*      */       public void setTransforms(Transforms paramTransforms)
/*      */       {
/* 3033 */         this.referenceTransforms = paramTransforms;
/*      */       }
/*      */ 
/*      */       Element toElement()
/*      */       {
/* 3044 */         Element localElement = ElementProxy.createElementForFamily(XMLCipher.this._contextDocument, "http://www.w3.org/2001/04/xmlenc#", "CipherReference");
/*      */ 
/* 3048 */         localElement.setAttributeNS(null, "URI", this.referenceURI);
/*      */ 
/* 3050 */         if (null != this.referenceTransforms) {
/* 3051 */           localElement.appendChild(((XMLCipher.Factory.TransformsImpl)this.referenceTransforms).toElement());
/*      */         }
/*      */ 
/* 3055 */         return localElement;
/*      */       }
/*      */     }
/*      */ 
/*      */     private class CipherValueImpl implements CipherValue {
/* 3060 */       private String cipherValue = null;
/*      */ 
/*      */       public CipherValueImpl(String arg2)
/*      */       {
/*      */         Object localObject;
/* 3071 */         this.cipherValue = localObject;
/*      */       }
/*      */ 
/*      */       public String getValue()
/*      */       {
/* 3076 */         return this.cipherValue;
/*      */       }
/*      */ 
/*      */       public void setValue(String paramString)
/*      */       {
/* 3086 */         this.cipherValue = paramString;
/*      */       }
/*      */ 
/*      */       Element toElement() {
/* 3090 */         Element localElement = ElementProxy.createElementForFamily(XMLCipher.this._contextDocument, "http://www.w3.org/2001/04/xmlenc#", "CipherValue");
/*      */ 
/* 3093 */         localElement.appendChild(XMLCipher.this._contextDocument.createTextNode(this.cipherValue));
/*      */ 
/* 3096 */         return localElement;
/*      */       }
/*      */     }
/*      */ 
/*      */     private class EncryptedDataImpl extends XMLCipher.Factory.EncryptedTypeImpl
/*      */       implements EncryptedData
/*      */     {
/*      */       public EncryptedDataImpl(CipherData arg2)
/*      */       {
/* 3125 */         super(localCipherData);
/*      */       }
/*      */ 
/*      */       Element toElement()
/*      */       {
/* 3148 */         Element localElement = ElementProxy.createElementForFamily(XMLCipher.this._contextDocument, "http://www.w3.org/2001/04/xmlenc#", "EncryptedData");
/*      */ 
/* 3152 */         if (null != super.getId()) {
/* 3153 */           localElement.setAttributeNS(null, "Id", super.getId());
/*      */         }
/*      */ 
/* 3156 */         if (null != super.getType()) {
/* 3157 */           localElement.setAttributeNS(null, "Type", super.getType());
/*      */         }
/*      */ 
/* 3160 */         if (null != super.getMimeType()) {
/* 3161 */           localElement.setAttributeNS(null, "MimeType", super.getMimeType());
/*      */         }
/*      */ 
/* 3165 */         if (null != super.getEncoding()) {
/* 3166 */           localElement.setAttributeNS(null, "Encoding", super.getEncoding());
/*      */         }
/*      */ 
/* 3170 */         if (null != super.getEncryptionMethod()) {
/* 3171 */           localElement.appendChild(((XMLCipher.Factory.EncryptionMethodImpl)super.getEncryptionMethod()).toElement());
/*      */         }
/*      */ 
/* 3174 */         if (null != super.getKeyInfo()) {
/* 3175 */           localElement.appendChild(super.getKeyInfo().getElement());
/*      */         }
/*      */ 
/* 3178 */         localElement.appendChild(((XMLCipher.Factory.CipherDataImpl)super.getCipherData()).toElement());
/*      */ 
/* 3180 */         if (null != super.getEncryptionProperties()) {
/* 3181 */           localElement.appendChild(((XMLCipher.Factory.EncryptionPropertiesImpl)super.getEncryptionProperties()).toElement());
/*      */         }
/*      */ 
/* 3185 */         return localElement;
/*      */       }
/*      */     }
/*      */ 
/*      */     private class EncryptedKeyImpl extends XMLCipher.Factory.EncryptedTypeImpl
/*      */       implements EncryptedKey
/*      */     {
/* 3216 */       private String keyRecipient = null;
/* 3217 */       private ReferenceList referenceList = null;
/* 3218 */       private String carriedName = null;
/*      */ 
/*      */       public EncryptedKeyImpl(CipherData arg2)
/*      */       {
/* 3224 */         super(localCipherData);
/*      */       }
/*      */ 
/*      */       public String getRecipient()
/*      */       {
/* 3229 */         return this.keyRecipient;
/*      */       }
/*      */ 
/*      */       public void setRecipient(String paramString)
/*      */       {
/* 3234 */         this.keyRecipient = paramString;
/*      */       }
/*      */ 
/*      */       public ReferenceList getReferenceList()
/*      */       {
/* 3239 */         return this.referenceList;
/*      */       }
/*      */ 
/*      */       public void setReferenceList(ReferenceList paramReferenceList)
/*      */       {
/* 3244 */         this.referenceList = paramReferenceList;
/*      */       }
/*      */ 
/*      */       public String getCarriedName()
/*      */       {
/* 3249 */         return this.carriedName;
/*      */       }
/*      */ 
/*      */       public void setCarriedName(String paramString)
/*      */       {
/* 3254 */         this.carriedName = paramString;
/*      */       }
/*      */ 
/*      */       Element toElement()
/*      */       {
/* 3283 */         Element localElement1 = ElementProxy.createElementForFamily(XMLCipher.this._contextDocument, "http://www.w3.org/2001/04/xmlenc#", "EncryptedKey");
/*      */ 
/* 3287 */         if (null != super.getId()) {
/* 3288 */           localElement1.setAttributeNS(null, "Id", super.getId());
/*      */         }
/*      */ 
/* 3291 */         if (null != super.getType()) {
/* 3292 */           localElement1.setAttributeNS(null, "Type", super.getType());
/*      */         }
/*      */ 
/* 3295 */         if (null != super.getMimeType()) {
/* 3296 */           localElement1.setAttributeNS(null, "MimeType", super.getMimeType());
/*      */         }
/*      */ 
/* 3299 */         if (null != super.getEncoding()) {
/* 3300 */           localElement1.setAttributeNS(null, "Encoding", super.getEncoding());
/*      */         }
/*      */ 
/* 3303 */         if (null != getRecipient()) {
/* 3304 */           localElement1.setAttributeNS(null, "Recipient", getRecipient());
/*      */         }
/*      */ 
/* 3307 */         if (null != super.getEncryptionMethod()) {
/* 3308 */           localElement1.appendChild(((XMLCipher.Factory.EncryptionMethodImpl)super.getEncryptionMethod()).toElement());
/*      */         }
/*      */ 
/* 3311 */         if (null != super.getKeyInfo()) {
/* 3312 */           localElement1.appendChild(super.getKeyInfo().getElement());
/*      */         }
/* 3314 */         localElement1.appendChild(((XMLCipher.Factory.CipherDataImpl)super.getCipherData()).toElement());
/*      */ 
/* 3316 */         if (null != super.getEncryptionProperties()) {
/* 3317 */           localElement1.appendChild(((XMLCipher.Factory.EncryptionPropertiesImpl)super.getEncryptionProperties()).toElement());
/*      */         }
/*      */ 
/* 3320 */         if ((this.referenceList != null) && (!this.referenceList.isEmpty())) {
/* 3321 */           localElement1.appendChild(((XMLCipher.Factory.ReferenceListImpl)getReferenceList()).toElement());
/*      */         }
/*      */ 
/* 3324 */         if (null != this.carriedName) {
/* 3325 */           Element localElement2 = ElementProxy.createElementForFamily(XMLCipher.this._contextDocument, "http://www.w3.org/2001/04/xmlenc#", "CarriedKeyName");
/*      */ 
/* 3329 */           Text localText = XMLCipher.this._contextDocument.createTextNode(this.carriedName);
/* 3330 */           localElement2.appendChild(localText);
/* 3331 */           localElement1.appendChild(localElement2);
/*      */         }
/*      */ 
/* 3334 */         return localElement1;
/*      */       }
/*      */     }
/*      */ 
/*      */     private abstract class EncryptedTypeImpl {
/* 3339 */       private String id = null;
/* 3340 */       private String type = null;
/* 3341 */       private String mimeType = null;
/* 3342 */       private String encoding = null;
/* 3343 */       private EncryptionMethod encryptionMethod = null;
/* 3344 */       private KeyInfo keyInfo = null;
/* 3345 */       private CipherData cipherData = null;
/* 3346 */       private EncryptionProperties encryptionProperties = null;
/*      */ 
/*      */       protected EncryptedTypeImpl(CipherData arg2)
/*      */       {
/*      */         Object localObject;
/* 3349 */         this.cipherData = localObject;
/*      */       }
/*      */ 
/*      */       public String getId()
/*      */       {
/* 3356 */         return this.id;
/*      */       }
/*      */ 
/*      */       public void setId(String paramString)
/*      */       {
/* 3363 */         this.id = paramString;
/*      */       }
/*      */ 
/*      */       public String getType()
/*      */       {
/* 3370 */         return this.type;
/*      */       }
/*      */ 
/*      */       public void setType(String paramString)
/*      */       {
/* 3377 */         if ((paramString == null) || (paramString.length() == 0)) {
/* 3378 */           this.type = null;
/*      */         } else {
/* 3380 */           URI localURI = null;
/*      */           try {
/* 3382 */             localURI = new URI(paramString);
/*      */           }
/*      */           catch (URI.MalformedURIException localMalformedURIException) {
/*      */           }
/* 3386 */           this.type = localURI.toString();
/*      */         }
/*      */       }
/*      */ 
/*      */       public String getMimeType()
/*      */       {
/* 3394 */         return this.mimeType;
/*      */       }
/*      */ 
/*      */       public void setMimeType(String paramString)
/*      */       {
/* 3401 */         this.mimeType = paramString;
/*      */       }
/*      */ 
/*      */       public String getEncoding()
/*      */       {
/* 3408 */         return this.encoding;
/*      */       }
/*      */ 
/*      */       public void setEncoding(String paramString)
/*      */       {
/* 3415 */         if ((paramString == null) || (paramString.length() == 0)) {
/* 3416 */           this.encoding = null;
/*      */         } else {
/* 3418 */           URI localURI = null;
/*      */           try {
/* 3420 */             localURI = new URI(paramString);
/*      */           }
/*      */           catch (URI.MalformedURIException localMalformedURIException) {
/*      */           }
/* 3424 */           this.encoding = localURI.toString();
/*      */         }
/*      */       }
/*      */ 
/*      */       public EncryptionMethod getEncryptionMethod()
/*      */       {
/* 3432 */         return this.encryptionMethod;
/*      */       }
/*      */ 
/*      */       public void setEncryptionMethod(EncryptionMethod paramEncryptionMethod)
/*      */       {
/* 3439 */         this.encryptionMethod = paramEncryptionMethod;
/*      */       }
/*      */ 
/*      */       public KeyInfo getKeyInfo()
/*      */       {
/* 3446 */         return this.keyInfo;
/*      */       }
/*      */ 
/*      */       public void setKeyInfo(KeyInfo paramKeyInfo)
/*      */       {
/* 3453 */         this.keyInfo = paramKeyInfo;
/*      */       }
/*      */ 
/*      */       public CipherData getCipherData()
/*      */       {
/* 3460 */         return this.cipherData;
/*      */       }
/*      */ 
/*      */       public EncryptionProperties getEncryptionProperties()
/*      */       {
/* 3467 */         return this.encryptionProperties;
/*      */       }
/*      */ 
/*      */       public void setEncryptionProperties(EncryptionProperties paramEncryptionProperties)
/*      */       {
/* 3475 */         this.encryptionProperties = paramEncryptionProperties;
/*      */       }
/*      */     }
/*      */ 
/*      */     private class EncryptionMethodImpl
/*      */       implements EncryptionMethod
/*      */     {
/* 3488 */       private String algorithm = null;
/* 3489 */       private int keySize = -2147483648;
/* 3490 */       private byte[] oaepParams = null;
/* 3491 */       private List encryptionMethodInformation = null;
/*      */ 
/*      */       public EncryptionMethodImpl(String arg2)
/*      */       {
/* 3497 */         URI localURI = null;
/*      */         try
/*      */         {
/*      */           String str;
/* 3499 */           localURI = new URI(str);
/*      */         }
/*      */         catch (URI.MalformedURIException localMalformedURIException) {
/*      */         }
/* 3503 */         this.algorithm = localURI.toString();
/* 3504 */         this.encryptionMethodInformation = new LinkedList();
/*      */       }
/*      */ 
/*      */       public String getAlgorithm() {
/* 3508 */         return this.algorithm;
/*      */       }
/*      */ 
/*      */       public int getKeySize() {
/* 3512 */         return this.keySize;
/*      */       }
/*      */ 
/*      */       public void setKeySize(int paramInt) {
/* 3516 */         this.keySize = paramInt;
/*      */       }
/*      */ 
/*      */       public byte[] getOAEPparams() {
/* 3520 */         return this.oaepParams;
/*      */       }
/*      */ 
/*      */       public void setOAEPparams(byte[] paramArrayOfByte) {
/* 3524 */         this.oaepParams = paramArrayOfByte;
/*      */       }
/*      */ 
/*      */       public Iterator getEncryptionMethodInformation() {
/* 3528 */         return this.encryptionMethodInformation.iterator();
/*      */       }
/*      */ 
/*      */       public void addEncryptionMethodInformation(Element paramElement) {
/* 3532 */         this.encryptionMethodInformation.add(paramElement);
/*      */       }
/*      */ 
/*      */       public void removeEncryptionMethodInformation(Element paramElement) {
/* 3536 */         this.encryptionMethodInformation.remove(paramElement);
/*      */       }
/*      */ 
/*      */       Element toElement()
/*      */       {
/* 3548 */         Element localElement = ElementProxy.createElementForFamily(XMLCipher.this._contextDocument, "http://www.w3.org/2001/04/xmlenc#", "EncryptionMethod");
/*      */ 
/* 3551 */         localElement.setAttributeNS(null, "Algorithm", this.algorithm);
/*      */ 
/* 3553 */         if (this.keySize > 0) {
/* 3554 */           localElement.appendChild(ElementProxy.createElementForFamily(XMLCipher.this._contextDocument, "http://www.w3.org/2001/04/xmlenc#", "KeySize").appendChild(XMLCipher.this._contextDocument.createTextNode(String.valueOf(this.keySize))));
/*      */         }
/*      */ 
/* 3561 */         if (null != this.oaepParams) {
/* 3562 */           localElement.appendChild(ElementProxy.createElementForFamily(XMLCipher.this._contextDocument, "http://www.w3.org/2001/04/xmlenc#", "OAEPparams").appendChild(XMLCipher.this._contextDocument.createTextNode(new String(this.oaepParams))));
/*      */         }
/*      */ 
/* 3569 */         if (!this.encryptionMethodInformation.isEmpty()) {
/* 3570 */           Iterator localIterator = this.encryptionMethodInformation.iterator();
/* 3571 */           localElement.appendChild((Element)localIterator.next());
/*      */         }
/*      */ 
/* 3574 */         return localElement;
/*      */       }
/*      */     }
/*      */ 
/*      */     private class EncryptionPropertiesImpl
/*      */       implements EncryptionProperties
/*      */     {
/* 3586 */       private String id = null;
/* 3587 */       private List encryptionProperties = null;
/*      */ 
/*      */       public EncryptionPropertiesImpl()
/*      */       {
/* 3593 */         this.encryptionProperties = new LinkedList();
/*      */       }
/*      */ 
/*      */       public String getId() {
/* 3597 */         return this.id;
/*      */       }
/*      */ 
/*      */       public void setId(String paramString) {
/* 3601 */         this.id = paramString;
/*      */       }
/*      */ 
/*      */       public Iterator getEncryptionProperties() {
/* 3605 */         return this.encryptionProperties.iterator();
/*      */       }
/*      */ 
/*      */       public void addEncryptionProperty(EncryptionProperty paramEncryptionProperty) {
/* 3609 */         this.encryptionProperties.add(paramEncryptionProperty);
/*      */       }
/*      */ 
/*      */       public void removeEncryptionProperty(EncryptionProperty paramEncryptionProperty) {
/* 3613 */         this.encryptionProperties.remove(paramEncryptionProperty);
/*      */       }
/*      */ 
/*      */       Element toElement()
/*      */       {
/* 3624 */         Element localElement = ElementProxy.createElementForFamily(XMLCipher.this._contextDocument, "http://www.w3.org/2001/04/xmlenc#", "EncryptionProperties");
/*      */ 
/* 3627 */         if (null != this.id) {
/* 3628 */           localElement.setAttributeNS(null, "Id", this.id);
/*      */         }
/* 3630 */         Iterator localIterator = getEncryptionProperties();
/* 3631 */         while (localIterator.hasNext()) {
/* 3632 */           localElement.appendChild(((XMLCipher.Factory.EncryptionPropertyImpl)localIterator.next()).toElement());
/*      */         }
/*      */ 
/* 3636 */         return localElement;
/*      */       }
/*      */     }
/*      */ 
/*      */     private class EncryptionPropertyImpl
/*      */       implements EncryptionProperty
/*      */     {
/* 3650 */       private String target = null;
/* 3651 */       private String id = null;
/* 3652 */       private HashMap attributeMap = new HashMap();
/* 3653 */       private List encryptionInformation = null;
/*      */ 
/*      */       public EncryptionPropertyImpl()
/*      */       {
/* 3660 */         this.encryptionInformation = new LinkedList();
/*      */       }
/*      */ 
/*      */       public String getTarget() {
/* 3664 */         return this.target;
/*      */       }
/*      */ 
/*      */       public void setTarget(String paramString) {
/* 3668 */         if ((paramString == null) || (paramString.length() == 0)) {
/* 3669 */           this.target = null;
/* 3670 */         } else if (paramString.startsWith("#"))
/*      */         {
/* 3676 */           this.target = paramString;
/*      */         } else {
/* 3678 */           URI localURI = null;
/*      */           try {
/* 3680 */             localURI = new URI(paramString);
/*      */           }
/*      */           catch (URI.MalformedURIException localMalformedURIException) {
/*      */           }
/* 3684 */           this.target = localURI.toString();
/*      */         }
/*      */       }
/*      */ 
/*      */       public String getId() {
/* 3689 */         return this.id;
/*      */       }
/*      */ 
/*      */       public void setId(String paramString) {
/* 3693 */         this.id = paramString;
/*      */       }
/*      */ 
/*      */       public String getAttribute(String paramString) {
/* 3697 */         return (String)this.attributeMap.get(paramString);
/*      */       }
/*      */ 
/*      */       public void setAttribute(String paramString1, String paramString2) {
/* 3701 */         this.attributeMap.put(paramString1, paramString2);
/*      */       }
/*      */ 
/*      */       public Iterator getEncryptionInformation() {
/* 3705 */         return this.encryptionInformation.iterator();
/*      */       }
/*      */ 
/*      */       public void addEncryptionInformation(Element paramElement) {
/* 3709 */         this.encryptionInformation.add(paramElement);
/*      */       }
/*      */ 
/*      */       public void removeEncryptionInformation(Element paramElement) {
/* 3713 */         this.encryptionInformation.remove(paramElement);
/*      */       }
/*      */ 
/*      */       Element toElement()
/*      */       {
/* 3726 */         Element localElement = ElementProxy.createElementForFamily(XMLCipher.this._contextDocument, "http://www.w3.org/2001/04/xmlenc#", "EncryptionProperty");
/*      */ 
/* 3729 */         if (null != this.target) {
/* 3730 */           localElement.setAttributeNS(null, "Target", this.target);
/*      */         }
/*      */ 
/* 3733 */         if (null != this.id) {
/* 3734 */           localElement.setAttributeNS(null, "Id", this.id);
/*      */         }
/*      */ 
/* 3740 */         return localElement;
/*      */       }
/*      */     }
/*      */ 
/*      */     private class ReferenceListImpl
/*      */       implements ReferenceList
/*      */     {
/*      */       private Class sentry;
/*      */       private List references;
/*      */ 
/*      */       public ReferenceListImpl(int arg2)
/*      */       {
/*      */         int i;
/* 3833 */         if (i == 1)
/* 3834 */           this.sentry = DataReference.class;
/* 3835 */         else if (i == 2)
/* 3836 */           this.sentry = KeyReference.class;
/*      */         else {
/* 3838 */           throw new IllegalArgumentException();
/*      */         }
/* 3840 */         this.references = new LinkedList();
/*      */       }
/*      */ 
/*      */       public void add(Reference paramReference) {
/* 3844 */         if (!paramReference.getClass().equals(this.sentry)) {
/* 3845 */           throw new IllegalArgumentException();
/*      */         }
/* 3847 */         this.references.add(paramReference);
/*      */       }
/*      */ 
/*      */       public void remove(Reference paramReference) {
/* 3851 */         if (!paramReference.getClass().equals(this.sentry)) {
/* 3852 */           throw new IllegalArgumentException();
/*      */         }
/* 3854 */         this.references.remove(paramReference);
/*      */       }
/*      */ 
/*      */       public int size() {
/* 3858 */         return this.references.size();
/*      */       }
/*      */ 
/*      */       public boolean isEmpty() {
/* 3862 */         return this.references.isEmpty();
/*      */       }
/*      */ 
/*      */       public Iterator getReferences() {
/* 3866 */         return this.references.iterator();
/*      */       }
/*      */ 
/*      */       Element toElement() {
/* 3870 */         Element localElement = ElementProxy.createElementForFamily(XMLCipher.this._contextDocument, "http://www.w3.org/2001/04/xmlenc#", "ReferenceList");
/*      */ 
/* 3874 */         Iterator localIterator = this.references.iterator();
/* 3875 */         while (localIterator.hasNext()) {
/* 3876 */           Reference localReference = (Reference)localIterator.next();
/* 3877 */           localElement.appendChild(((ReferenceImpl)localReference).toElement());
/*      */         }
/*      */ 
/* 3880 */         return localElement;
/*      */       }
/*      */ 
/*      */       public Reference newDataReference(String paramString) {
/* 3884 */         return new DataReference(paramString);
/*      */       }
/*      */ 
/*      */       public Reference newKeyReference(String paramString) {
/* 3888 */         return new KeyReference(paramString);
/*      */       }
/*      */ 
/*      */       private class DataReference extends XMLCipher.Factory.ReferenceListImpl.ReferenceImpl
/*      */       {
/*      */         DataReference(String arg2)
/*      */         {
/* 3948 */           super(str);
/*      */         }
/*      */ 
/*      */         public Element toElement() {
/* 3952 */           return super.toElement("DataReference");
/*      */         }
/*      */       }
/*      */ 
/*      */       private class KeyReference extends XMLCipher.Factory.ReferenceListImpl.ReferenceImpl {
/*      */         KeyReference(String arg2) {
/* 3958 */           super(str);
/*      */         }
/*      */ 
/*      */         public Element toElement() {
/* 3962 */           return super.toElement("KeyReference");
/*      */         }
/*      */       }
/*      */ 
/*      */       private abstract class ReferenceImpl
/*      */         implements Reference
/*      */       {
/*      */         private String uri;
/*      */         private List referenceInformation;
/*      */ 
/*      */         ReferenceImpl(String arg2)
/*      */         {
/*      */           Object localObject;
/* 3902 */           this.uri = localObject;
/* 3903 */           this.referenceInformation = new LinkedList();
/*      */         }
/*      */ 
/*      */         public String getURI() {
/* 3907 */           return this.uri;
/*      */         }
/*      */ 
/*      */         public Iterator getElementRetrievalInformation() {
/* 3911 */           return this.referenceInformation.iterator();
/*      */         }
/*      */ 
/*      */         public void setURI(String paramString) {
/* 3915 */           this.uri = paramString;
/*      */         }
/*      */ 
/*      */         public void removeElementRetrievalInformation(Element paramElement) {
/* 3919 */           this.referenceInformation.remove(paramElement);
/*      */         }
/*      */ 
/*      */         public void addElementRetrievalInformation(Element paramElement) {
/* 3923 */           this.referenceInformation.add(paramElement);
/*      */         }
/*      */ 
/*      */         public abstract Element toElement();
/*      */ 
/*      */         Element toElement(String paramString)
/*      */         {
/* 3932 */           Element localElement = ElementProxy.createElementForFamily(XMLCipher.this._contextDocument, "http://www.w3.org/2001/04/xmlenc#", paramString);
/*      */ 
/* 3936 */           localElement.setAttribute("URI", this.uri);
/*      */ 
/* 3942 */           return localElement;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     private class TransformsImpl extends com.sun.org.apache.xml.internal.security.transforms.Transforms
/*      */       implements Transforms
/*      */     {
/*      */       public TransformsImpl()
/*      */       {
/* 3758 */         super();
/*      */       }
/*      */ 
/*      */       public TransformsImpl(Document arg2)
/*      */       {
/*      */         Object localObject;
/* 3765 */         if (localObject == null) {
/* 3766 */           throw new RuntimeException("Document is null");
/*      */         }
/*      */ 
/* 3769 */         this._doc = localObject;
/* 3770 */         this._constructionElement = createElementForFamilyLocal(this._doc, getBaseNamespace(), getBaseLocalName());
/*      */       }
/*      */ 
/*      */       public TransformsImpl(Element arg2)
/*      */         throws XMLSignatureException, InvalidTransformException, XMLSecurityException, TransformationException
/*      */       {
/* 3787 */         super("");
/*      */       }
/*      */ 
/*      */       public Element toElement()
/*      */       {
/* 3797 */         if (this._doc == null) {
/* 3798 */           this._doc = XMLCipher.this._contextDocument;
/*      */         }
/* 3800 */         return getElement();
/*      */       }
/*      */ 
/*      */       public com.sun.org.apache.xml.internal.security.transforms.Transforms getDSTransforms()
/*      */       {
/* 3805 */         return this;
/*      */       }
/*      */ 
/*      */       public String getBaseNamespace()
/*      */       {
/* 3812 */         return "http://www.w3.org/2001/04/xmlenc#";
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private class Serializer
/*      */   {
/*      */     Serializer()
/*      */     {
/*      */     }
/*      */ 
/*      */     String serialize(Document paramDocument)
/*      */       throws Exception
/*      */     {
/* 1856 */       return canonSerialize(paramDocument);
/*      */     }
/*      */ 
/*      */     String serialize(Element paramElement)
/*      */       throws Exception
/*      */     {
/* 1871 */       return canonSerialize(paramElement);
/*      */     }
/*      */ 
/*      */     String serialize(NodeList paramNodeList)
/*      */       throws Exception
/*      */     {
/* 1897 */       ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
/* 1898 */       XMLCipher.this._canon.setWriter(localByteArrayOutputStream);
/* 1899 */       XMLCipher.this._canon.notReset();
/* 1900 */       for (int i = 0; i < paramNodeList.getLength(); i++) {
/* 1901 */         XMLCipher.this._canon.canonicalizeSubtree(paramNodeList.item(i));
/*      */       }
/* 1903 */       localByteArrayOutputStream.close();
/* 1904 */       return localByteArrayOutputStream.toString("UTF-8");
/*      */     }
/*      */ 
/*      */     String canonSerialize(Node paramNode)
/*      */       throws Exception
/*      */     {
/* 1914 */       ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
/* 1915 */       XMLCipher.this._canon.setWriter(localByteArrayOutputStream);
/* 1916 */       XMLCipher.this._canon.notReset();
/* 1917 */       XMLCipher.this._canon.canonicalizeSubtree(paramNode);
/* 1918 */       localByteArrayOutputStream.close();
/* 1919 */       return localByteArrayOutputStream.toString("UTF-8");
/*      */     }
/*      */ 
/*      */     DocumentFragment deserialize(String paramString, Node paramNode)
/*      */       throws XMLEncryptionException
/*      */     {
/* 1935 */       StringBuffer localStringBuffer = new StringBuffer();
/* 1936 */       localStringBuffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><fragment");
/*      */ 
/* 1941 */       Node localNode1 = paramNode;
/*      */       Object localObject2;
/*      */       Object localObject3;
/* 1943 */       while (localNode1 != null)
/*      */       {
/* 1945 */         localObject1 = localNode1.getAttributes();
/*      */         int i;
/* 1947 */         if (localObject1 != null)
/* 1948 */           i = ((NamedNodeMap)localObject1).getLength();
/*      */         else {
/* 1950 */           i = 0;
/*      */         }
/* 1952 */         for (int j = 0; j < i; j++) {
/* 1953 */           localObject2 = ((NamedNodeMap)localObject1).item(j);
/* 1954 */           if ((((Node)localObject2).getNodeName().startsWith("xmlns:")) || (((Node)localObject2).getNodeName().equals("xmlns")))
/*      */           {
/* 1958 */             localObject3 = paramNode;
/* 1959 */             int k = 0;
/* 1960 */             while (localObject3 != localNode1) {
/* 1961 */               NamedNodeMap localNamedNodeMap = ((Node)localObject3).getAttributes();
/* 1962 */               if ((localNamedNodeMap != null) && (localNamedNodeMap.getNamedItem(((Node)localObject2).getNodeName()) != null))
/*      */               {
/* 1964 */                 k = 1;
/* 1965 */                 break;
/*      */               }
/* 1967 */               localObject3 = ((Node)localObject3).getParentNode();
/*      */             }
/* 1969 */             if (k == 0)
/*      */             {
/* 1972 */               localStringBuffer.append(" " + ((Node)localObject2).getNodeName() + "=\"" + ((Node)localObject2).getNodeValue() + "\"");
/*      */             }
/*      */           }
/*      */         }
/*      */ 
/* 1977 */         localNode1 = localNode1.getParentNode();
/* 1979 */       }localStringBuffer.append(">" + paramString + "</" + "fragment" + ">");
/* 1980 */       Object localObject1 = localStringBuffer.toString();
/*      */       DocumentFragment localDocumentFragment;
/*      */       try { DocumentBuilderFactory localDocumentBuilderFactory = DocumentBuilderFactory.newInstance();
/*      */ 
/* 1985 */         localDocumentBuilderFactory.setNamespaceAware(true);
/* 1986 */         localDocumentBuilderFactory.setFeature("http://javax.xml.XMLConstants/feature/secure-processing", Boolean.TRUE.booleanValue());
/* 1987 */         localDocumentBuilderFactory.setAttribute("http://xml.org/sax/features/namespaces", Boolean.TRUE);
/* 1988 */         DocumentBuilder localDocumentBuilder = localDocumentBuilderFactory.newDocumentBuilder();
/* 1989 */         localObject2 = localDocumentBuilder.parse(new InputSource(new StringReader((String)localObject1)));
/*      */ 
/* 1992 */         localObject3 = (Element)XMLCipher.this._contextDocument.importNode(((Document)localObject2).getDocumentElement(), true);
/*      */ 
/* 1994 */         localDocumentFragment = XMLCipher.this._contextDocument.createDocumentFragment();
/* 1995 */         Node localNode2 = ((Element)localObject3).getFirstChild();
/* 1996 */         while (localNode2 != null) {
/* 1997 */           ((Element)localObject3).removeChild(localNode2);
/* 1998 */           localDocumentFragment.appendChild(localNode2);
/* 1999 */           localNode2 = ((Element)localObject3).getFirstChild();
/*      */         }
/*      */       }
/*      */       catch (SAXException localSAXException)
/*      */       {
/* 2004 */         throw new XMLEncryptionException("empty", localSAXException);
/*      */       } catch (ParserConfigurationException localParserConfigurationException) {
/* 2006 */         throw new XMLEncryptionException("empty", localParserConfigurationException);
/*      */       } catch (IOException localIOException) {
/* 2008 */         throw new XMLEncryptionException("empty", localIOException);
/*      */       }
/*      */ 
/* 2011 */       return localDocumentFragment;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.encryption.XMLCipher
 * JD-Core Version:    0.6.2
 */