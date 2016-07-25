/*      */ package com.sun.org.apache.xerces.internal.impl;
/*      */ 
/*      */ import com.sun.org.apache.xerces.internal.impl.io.ASCIIReader;
/*      */ import com.sun.org.apache.xerces.internal.impl.io.UCSReader;
/*      */ import com.sun.org.apache.xerces.internal.impl.io.UTF8Reader;
/*      */ import com.sun.org.apache.xerces.internal.impl.validation.ValidationManager;
/*      */ import com.sun.org.apache.xerces.internal.util.AugmentationsImpl;
/*      */ import com.sun.org.apache.xerces.internal.util.EncodingMap;
/*      */ import com.sun.org.apache.xerces.internal.util.HTTPInputSource;
/*      */ import com.sun.org.apache.xerces.internal.util.SymbolTable;
/*      */ import com.sun.org.apache.xerces.internal.util.URI.MalformedURIException;
/*      */ import com.sun.org.apache.xerces.internal.util.XMLChar;
/*      */ import com.sun.org.apache.xerces.internal.util.XMLEntityDescriptionImpl;
/*      */ import com.sun.org.apache.xerces.internal.util.XMLResourceIdentifierImpl;
/*      */ import com.sun.org.apache.xerces.internal.utils.SecuritySupport;
/*      */ import com.sun.org.apache.xerces.internal.utils.XMLLimitAnalyzer;
/*      */ import com.sun.org.apache.xerces.internal.utils.XMLSecurityManager;
/*      */ import com.sun.org.apache.xerces.internal.utils.XMLSecurityManager.Limit;
/*      */ import com.sun.org.apache.xerces.internal.utils.XMLSecurityPropertyManager;
/*      */ import com.sun.org.apache.xerces.internal.utils.XMLSecurityPropertyManager.Property;
/*      */ import com.sun.org.apache.xerces.internal.xni.Augmentations;
/*      */ import com.sun.org.apache.xerces.internal.xni.XMLResourceIdentifier;
/*      */ import com.sun.org.apache.xerces.internal.xni.XNIException;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLComponent;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLComponentManager;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLConfigurationException;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLEntityResolver;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource;
/*      */ import com.sun.xml.internal.stream.Entity;
/*      */ import com.sun.xml.internal.stream.Entity.ExternalEntity;
/*      */ import com.sun.xml.internal.stream.Entity.InternalEntity;
/*      */ import com.sun.xml.internal.stream.Entity.ScannedEntity;
/*      */ import com.sun.xml.internal.stream.StaxEntityResolverWrapper;
/*      */ import com.sun.xml.internal.stream.StaxXMLInputSource;
/*      */ import com.sun.xml.internal.stream.XMLEntityStorage;
/*      */ import java.io.BufferedReader;
/*      */ import java.io.EOFException;
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.InputStreamReader;
/*      */ import java.io.Reader;
/*      */ import java.io.StringReader;
/*      */ import java.io.UnsupportedEncodingException;
/*      */ import java.lang.reflect.Method;
/*      */ import java.net.HttpURLConnection;
/*      */ import java.net.URISyntaxException;
/*      */ import java.net.URL;
/*      */ import java.net.URLConnection;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Iterator;
/*      */ import java.util.Locale;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.Stack;
/*      */ 
/*      */ public class XMLEntityManager
/*      */   implements XMLComponent, XMLEntityResolver
/*      */ {
/*      */   public static final int DEFAULT_BUFFER_SIZE = 8192;
/*      */   public static final int DEFAULT_XMLDECL_BUFFER_SIZE = 64;
/*      */   public static final int DEFAULT_INTERNAL_BUFFER_SIZE = 1024;
/*      */   protected static final String VALIDATION = "http://xml.org/sax/features/validation";
/*      */   protected boolean fStrictURI;
/*      */   protected static final String EXTERNAL_GENERAL_ENTITIES = "http://xml.org/sax/features/external-general-entities";
/*      */   protected static final String EXTERNAL_PARAMETER_ENTITIES = "http://xml.org/sax/features/external-parameter-entities";
/*      */   protected static final String ALLOW_JAVA_ENCODINGS = "http://apache.org/xml/features/allow-java-encodings";
/*      */   protected static final String WARN_ON_DUPLICATE_ENTITYDEF = "http://apache.org/xml/features/warn-on-duplicate-entitydef";
/*      */   protected static final String LOAD_EXTERNAL_DTD = "http://apache.org/xml/features/nonvalidating/load-external-dtd";
/*      */   protected static final String SYMBOL_TABLE = "http://apache.org/xml/properties/internal/symbol-table";
/*      */   protected static final String ERROR_REPORTER = "http://apache.org/xml/properties/internal/error-reporter";
/*      */   protected static final String STANDARD_URI_CONFORMANT = "http://apache.org/xml/features/standard-uri-conformant";
/*      */   protected static final String ENTITY_RESOLVER = "http://apache.org/xml/properties/internal/entity-resolver";
/*      */   protected static final String STAX_ENTITY_RESOLVER = "http://apache.org/xml/properties/internal/stax-entity-resolver";
/*      */   protected static final String VALIDATION_MANAGER = "http://apache.org/xml/properties/internal/validation-manager";
/*      */   protected static final String BUFFER_SIZE = "http://apache.org/xml/properties/input-buffer-size";
/*      */   protected static final String SECURITY_MANAGER = "http://apache.org/xml/properties/security-manager";
/*      */   protected static final String PARSER_SETTINGS = "http://apache.org/xml/features/internal/parser-settings";
/*      */   private static final String XML_SECURITY_PROPERTY_MANAGER = "http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager";
/*      */   static final String EXTERNAL_ACCESS_DEFAULT = "all";
/*  179 */   private static final String[] RECOGNIZED_FEATURES = { "http://xml.org/sax/features/validation", "http://xml.org/sax/features/external-general-entities", "http://xml.org/sax/features/external-parameter-entities", "http://apache.org/xml/features/allow-java-encodings", "http://apache.org/xml/features/warn-on-duplicate-entitydef", "http://apache.org/xml/features/standard-uri-conformant" };
/*      */ 
/*  189 */   private static final Boolean[] FEATURE_DEFAULTS = { null, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE };
/*      */ 
/*  199 */   private static final String[] RECOGNIZED_PROPERTIES = { "http://apache.org/xml/properties/internal/symbol-table", "http://apache.org/xml/properties/internal/error-reporter", "http://apache.org/xml/properties/internal/entity-resolver", "http://apache.org/xml/properties/internal/validation-manager", "http://apache.org/xml/properties/input-buffer-size", "http://apache.org/xml/properties/security-manager", "http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager" };
/*      */ 
/*  210 */   private static final Object[] PROPERTY_DEFAULTS = { null, null, null, null, new Integer(8192), null, null };
/*      */ 
/*  220 */   private static final String XMLEntity = "[xml]".intern();
/*  221 */   private static final String DTDEntity = "[dtd]".intern();
/*      */   private static final boolean DEBUG_BUFFER = false;
/*      */   protected boolean fWarnDuplicateEntityDef;
/*      */   private static final boolean DEBUG_ENTITIES = false;
/*      */   private static final boolean DEBUG_ENCODINGS = false;
/*      */   private static final boolean DEBUG_RESOLVER = false;
/*      */   protected boolean fValidation;
/*      */   protected boolean fExternalGeneralEntities;
/*      */   protected boolean fExternalParameterEntities;
/*  274 */   protected boolean fAllowJavaEncodings = true;
/*      */ 
/*  277 */   protected boolean fLoadExternalDTD = true;
/*      */   protected SymbolTable fSymbolTable;
/*      */   protected XMLErrorReporter fErrorReporter;
/*      */   protected XMLEntityResolver fEntityResolver;
/*      */   protected StaxEntityResolverWrapper fStaxEntityResolver;
/*      */   protected PropertyManager fPropertyManager;
/*  307 */   boolean fSupportDTD = true;
/*  308 */   boolean fReplaceEntityReferences = true;
/*  309 */   boolean fSupportExternalEntities = true;
/*      */ 
/*  312 */   protected String fAccessExternalDTD = "all";
/*      */   protected ValidationManager fValidationManager;
/*  329 */   protected int fBufferSize = 8192;
/*      */ 
/*  332 */   protected XMLSecurityManager fSecurityManager = null;
/*      */ 
/*  334 */   protected XMLLimitAnalyzer fLimitAnalyzer = null;
/*      */   protected int entityExpansionIndex;
/*      */   protected boolean fStandalone;
/*  346 */   protected boolean fInExternalSubset = false;
/*      */   protected XMLEntityHandler fEntityHandler;
/*      */   protected XMLEntityScanner fEntityScanner;
/*      */   protected XMLEntityScanner fXML10EntityScanner;
/*      */   protected XMLEntityScanner fXML11EntityScanner;
/*  363 */   protected int fEntityExpansionCount = 0;
/*      */ 
/*  368 */   protected Hashtable fEntities = new Hashtable();
/*      */ 
/*  371 */   protected Stack fEntityStack = new Stack();
/*      */ 
/*  374 */   protected Entity.ScannedEntity fCurrentEntity = null;
/*      */ 
/*  377 */   boolean fISCreatedByResolver = false;
/*      */   protected XMLEntityStorage fEntityStorage;
/*  383 */   protected final Object[] defaultEncoding = { "UTF-8", null };
/*      */ 
/*  389 */   private final XMLResourceIdentifierImpl fResourceIdentifier = new XMLResourceIdentifierImpl();
/*      */ 
/*  392 */   private final Augmentations fEntityAugs = new AugmentationsImpl();
/*      */ 
/*  395 */   private CharacterBufferPool fBufferPool = new CharacterBufferPool(this.fBufferSize, 1024);
/*      */   private static String gUserDir;
/*      */   private static com.sun.org.apache.xerces.internal.util.URI gUserDirURI;
/* 1762 */   private static boolean[] gNeedEscaping = new boolean[''];
/*      */ 
/* 1764 */   private static char[] gAfterEscaping1 = new char[''];
/*      */ 
/* 1766 */   private static char[] gAfterEscaping2 = new char[''];
/* 1767 */   private static char[] gHexChs = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
/*      */ 
/*      */   public XMLEntityManager()
/*      */   {
/*  405 */     this.fEntityStorage = new XMLEntityStorage(this);
/*  406 */     setScannerVersion((short)1);
/*      */   }
/*      */ 
/*      */   public XMLEntityManager(PropertyManager propertyManager)
/*      */   {
/*  411 */     this.fPropertyManager = propertyManager;
/*      */ 
/*  414 */     this.fEntityStorage = new XMLEntityStorage(this);
/*  415 */     this.fEntityScanner = new XMLEntityScanner(propertyManager, this);
/*  416 */     reset(propertyManager);
/*      */   }
/*      */ 
/*      */   public void addInternalEntity(String name, String text)
/*      */   {
/*  434 */     if (!this.fEntities.containsKey(name)) {
/*  435 */       Entity entity = new Entity.InternalEntity(name, text, this.fInExternalSubset);
/*  436 */       this.fEntities.put(name, entity);
/*      */     }
/*  438 */     else if (this.fWarnDuplicateEntityDef) {
/*  439 */       this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_DUPLICATE_ENTITY_DEFINITION", new Object[] { name }, (short)0);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void addExternalEntity(String name, String publicId, String literalSystemId, String baseSystemId)
/*      */     throws IOException
/*      */   {
/*  473 */     if (!this.fEntities.containsKey(name)) {
/*  474 */       if (baseSystemId == null)
/*      */       {
/*  476 */         int size = this.fEntityStack.size();
/*  477 */         if ((size == 0) && (this.fCurrentEntity != null) && (this.fCurrentEntity.entityLocation != null)) {
/*  478 */           baseSystemId = this.fCurrentEntity.entityLocation.getExpandedSystemId();
/*      */         }
/*  480 */         for (int i = size - 1; i >= 0; i--) {
/*  481 */           Entity.ScannedEntity externalEntity = (Entity.ScannedEntity)this.fEntityStack.elementAt(i);
/*      */ 
/*  483 */           if ((externalEntity.entityLocation != null) && (externalEntity.entityLocation.getExpandedSystemId() != null)) {
/*  484 */             baseSystemId = externalEntity.entityLocation.getExpandedSystemId();
/*  485 */             break;
/*      */           }
/*      */         }
/*      */       }
/*  489 */       Entity entity = new Entity.ExternalEntity(name, new XMLEntityDescriptionImpl(name, publicId, literalSystemId, baseSystemId, expandSystemId(literalSystemId, baseSystemId, false)), null, this.fInExternalSubset);
/*      */ 
/*  492 */       this.fEntities.put(name, entity);
/*      */     }
/*  494 */     else if (this.fWarnDuplicateEntityDef) {
/*  495 */       this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_DUPLICATE_ENTITY_DEFINITION", new Object[] { name }, (short)0);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void addUnparsedEntity(String name, String publicId, String systemId, String baseSystemId, String notation)
/*      */   {
/*  524 */     if (!this.fEntities.containsKey(name)) {
/*  525 */       Entity.ExternalEntity entity = new Entity.ExternalEntity(name, new XMLEntityDescriptionImpl(name, publicId, systemId, baseSystemId, null), notation, this.fInExternalSubset);
/*      */ 
/*  528 */       this.fEntities.put(name, entity);
/*      */     }
/*  530 */     else if (this.fWarnDuplicateEntityDef) {
/*  531 */       this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_DUPLICATE_ENTITY_DEFINITION", new Object[] { name }, (short)0);
/*      */     }
/*      */   }
/*      */ 
/*      */   public XMLEntityStorage getEntityStore()
/*      */   {
/*  542 */     return this.fEntityStorage;
/*      */   }
/*      */ 
/*      */   public XMLEntityScanner getEntityScanner()
/*      */   {
/*  547 */     if (this.fEntityScanner == null)
/*      */     {
/*  549 */       if (this.fXML10EntityScanner == null) {
/*  550 */         this.fXML10EntityScanner = new XMLEntityScanner();
/*      */       }
/*  552 */       this.fXML10EntityScanner.reset(this.fSymbolTable, this, this.fErrorReporter);
/*  553 */       this.fEntityScanner = this.fXML10EntityScanner;
/*      */     }
/*  555 */     return this.fEntityScanner;
/*      */   }
/*      */ 
/*      */   public void setScannerVersion(short version)
/*      */   {
/*  561 */     if (version == 1) {
/*  562 */       if (this.fXML10EntityScanner == null) {
/*  563 */         this.fXML10EntityScanner = new XMLEntityScanner();
/*      */       }
/*  565 */       this.fXML10EntityScanner.reset(this.fSymbolTable, this, this.fErrorReporter);
/*  566 */       this.fEntityScanner = this.fXML10EntityScanner;
/*  567 */       this.fEntityScanner.setCurrentEntity(this.fCurrentEntity);
/*      */     } else {
/*  569 */       if (this.fXML11EntityScanner == null) {
/*  570 */         this.fXML11EntityScanner = new XML11EntityScanner();
/*      */       }
/*  572 */       this.fXML11EntityScanner.reset(this.fSymbolTable, this, this.fErrorReporter);
/*  573 */       this.fEntityScanner = this.fXML11EntityScanner;
/*  574 */       this.fEntityScanner.setCurrentEntity(this.fCurrentEntity);
/*      */     }
/*      */   }
/*      */ 
/*      */   public String setupCurrentEntity(String name, XMLInputSource xmlInputSource, boolean literal, boolean isExternal)
/*      */     throws IOException, XNIException
/*      */   {
/*  597 */     String publicId = xmlInputSource.getPublicId();
/*  598 */     String literalSystemId = xmlInputSource.getSystemId();
/*  599 */     String baseSystemId = xmlInputSource.getBaseSystemId();
/*  600 */     String encoding = xmlInputSource.getEncoding();
/*  601 */     boolean encodingExternallySpecified = encoding != null;
/*  602 */     Boolean isBigEndian = null;
/*      */ 
/*  605 */     InputStream stream = null;
/*  606 */     Reader reader = xmlInputSource.getCharacterStream();
/*      */ 
/*  609 */     String expandedSystemId = expandSystemId(literalSystemId, baseSystemId, this.fStrictURI);
/*  610 */     if (baseSystemId == null) {
/*  611 */       baseSystemId = expandedSystemId;
/*      */     }
/*  613 */     if (reader == null) {
/*  614 */       stream = xmlInputSource.getByteStream();
/*  615 */       if (stream == null) {
/*  616 */         URL location = new URL(expandedSystemId);
/*  617 */         URLConnection connect = location.openConnection();
/*  618 */         if (!(connect instanceof HttpURLConnection)) {
/*  619 */           stream = connect.getInputStream();
/*      */         }
/*      */         else {
/*  622 */           boolean followRedirects = true;
/*      */ 
/*  625 */           if ((xmlInputSource instanceof HTTPInputSource)) {
/*  626 */             HttpURLConnection urlConnection = (HttpURLConnection)connect;
/*  627 */             HTTPInputSource httpInputSource = (HTTPInputSource)xmlInputSource;
/*      */ 
/*  630 */             Iterator propIter = httpInputSource.getHTTPRequestProperties();
/*  631 */             while (propIter.hasNext()) {
/*  632 */               Map.Entry entry = (Map.Entry)propIter.next();
/*  633 */               urlConnection.setRequestProperty((String)entry.getKey(), (String)entry.getValue());
/*      */             }
/*      */ 
/*  637 */             followRedirects = httpInputSource.getFollowHTTPRedirects();
/*  638 */             if (!followRedirects) {
/*  639 */               setInstanceFollowRedirects(urlConnection, followRedirects);
/*      */             }
/*      */           }
/*      */ 
/*  643 */           stream = connect.getInputStream();
/*      */ 
/*  649 */           if (followRedirects) {
/*  650 */             String redirect = connect.getURL().toString();
/*      */ 
/*  653 */             if (!redirect.equals(expandedSystemId)) {
/*  654 */               literalSystemId = redirect;
/*  655 */               expandedSystemId = redirect;
/*      */             }
/*      */           }
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  662 */       stream = new RewindableInputStream(stream);
/*      */ 
/*  665 */       if (encoding == null)
/*      */       {
/*  667 */         byte[] b4 = new byte[4];
/*  668 */         for (int count = 0; 
/*  669 */           count < 4; count++) {
/*  670 */           b4[count] = ((byte)stream.read());
/*      */         }
/*  672 */         if (count == 4) {
/*  673 */           Object[] encodingDesc = getEncodingName(b4, count);
/*  674 */           encoding = (String)encodingDesc[0];
/*  675 */           isBigEndian = (Boolean)encodingDesc[1];
/*      */ 
/*  677 */           stream.reset();
/*      */ 
/*  681 */           if ((count > 2) && (encoding.equals("UTF-8"))) {
/*  682 */             int b0 = b4[0] & 0xFF;
/*  683 */             int b1 = b4[1] & 0xFF;
/*  684 */             int b2 = b4[2] & 0xFF;
/*  685 */             if ((b0 == 239) && (b1 == 187) && (b2 == 191))
/*      */             {
/*  687 */               stream.skip(3L);
/*      */             }
/*      */           }
/*  690 */           reader = createReader(stream, encoding, isBigEndian);
/*      */         } else {
/*  692 */           reader = createReader(stream, encoding, isBigEndian);
/*      */         }
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*  698 */         encoding = encoding.toUpperCase(Locale.ENGLISH);
/*      */ 
/*  701 */         if (encoding.equals("UTF-8")) {
/*  702 */           int[] b3 = new int[3];
/*  703 */           for (int count = 0; 
/*  704 */             count < 3; count++) {
/*  705 */             b3[count] = stream.read();
/*  706 */             if (b3[count] == -1)
/*      */               break;
/*      */           }
/*  709 */           if (count == 3) {
/*  710 */             if ((b3[0] != 239) || (b3[1] != 187) || (b3[2] != 191))
/*      */             {
/*  712 */               stream.reset();
/*      */             }
/*      */           }
/*  715 */           else stream.reset();
/*      */ 
/*      */         }
/*  720 */         else if (encoding.equals("UTF-16")) {
/*  721 */           int[] b4 = new int[4];
/*  722 */           for (int count = 0; 
/*  723 */             count < 4; count++) {
/*  724 */             b4[count] = stream.read();
/*  725 */             if (b4[count] == -1)
/*      */               break;
/*      */           }
/*  728 */           stream.reset();
/*      */ 
/*  730 */           String utf16Encoding = "UTF-16";
/*  731 */           if (count >= 2) {
/*  732 */             int b0 = b4[0];
/*  733 */             int b1 = b4[1];
/*  734 */             if ((b0 == 254) && (b1 == 255))
/*      */             {
/*  736 */               utf16Encoding = "UTF-16BE";
/*  737 */               isBigEndian = Boolean.TRUE;
/*      */             }
/*  739 */             else if ((b0 == 255) && (b1 == 254))
/*      */             {
/*  741 */               utf16Encoding = "UTF-16LE";
/*  742 */               isBigEndian = Boolean.FALSE;
/*      */             }
/*  744 */             else if (count == 4) {
/*  745 */               int b2 = b4[2];
/*  746 */               int b3 = b4[3];
/*  747 */               if ((b0 == 0) && (b1 == 60) && (b2 == 0) && (b3 == 63))
/*      */               {
/*  749 */                 utf16Encoding = "UTF-16BE";
/*  750 */                 isBigEndian = Boolean.TRUE;
/*      */               }
/*  752 */               if ((b0 == 60) && (b1 == 0) && (b2 == 63) && (b3 == 0))
/*      */               {
/*  754 */                 utf16Encoding = "UTF-16LE";
/*  755 */                 isBigEndian = Boolean.FALSE;
/*      */               }
/*      */             }
/*      */           }
/*  759 */           reader = createReader(stream, utf16Encoding, isBigEndian);
/*      */         }
/*  763 */         else if (encoding.equals("ISO-10646-UCS-4")) {
/*  764 */           int[] b4 = new int[4];
/*  765 */           for (int count = 0; 
/*  766 */             count < 4; count++) {
/*  767 */             b4[count] = stream.read();
/*  768 */             if (b4[count] == -1)
/*      */               break;
/*      */           }
/*  771 */           stream.reset();
/*      */ 
/*  774 */           if (count == 4)
/*      */           {
/*  776 */             if ((b4[0] == 0) && (b4[1] == 0) && (b4[2] == 0) && (b4[3] == 60)) {
/*  777 */               isBigEndian = Boolean.TRUE;
/*      */             }
/*  780 */             else if ((b4[0] == 60) && (b4[1] == 0) && (b4[2] == 0) && (b4[3] == 0)) {
/*  781 */               isBigEndian = Boolean.FALSE;
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/*      */         }
/*  787 */         else if (encoding.equals("ISO-10646-UCS-2")) {
/*  788 */           int[] b4 = new int[4];
/*  789 */           for (int count = 0; 
/*  790 */             count < 4; count++) {
/*  791 */             b4[count] = stream.read();
/*  792 */             if (b4[count] == -1)
/*      */               break;
/*      */           }
/*  795 */           stream.reset();
/*      */ 
/*  797 */           if (count == 4)
/*      */           {
/*  799 */             if ((b4[0] == 0) && (b4[1] == 60) && (b4[2] == 0) && (b4[3] == 63)) {
/*  800 */               isBigEndian = Boolean.TRUE;
/*      */             }
/*  803 */             else if ((b4[0] == 60) && (b4[1] == 0) && (b4[2] == 63) && (b4[3] == 0)) {
/*  804 */               isBigEndian = Boolean.FALSE;
/*      */             }
/*      */           }
/*      */         }
/*      */ 
/*  809 */         reader = createReader(stream, encoding, isBigEndian);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  826 */     if (this.fCurrentEntity != null) {
/*  827 */       this.fEntityStack.push(this.fCurrentEntity);
/*      */     }
/*      */ 
/*  835 */     this.fCurrentEntity = new Entity.ScannedEntity(name, new XMLResourceIdentifierImpl(publicId, literalSystemId, baseSystemId, expandedSystemId), stream, reader, encoding, literal, encodingExternallySpecified, isExternal);
/*  836 */     this.fCurrentEntity.setEncodingExternallySpecified(encodingExternallySpecified);
/*  837 */     this.fEntityScanner.setCurrentEntity(this.fCurrentEntity);
/*  838 */     this.fResourceIdentifier.setValues(publicId, literalSystemId, baseSystemId, expandedSystemId);
/*  839 */     if (this.fLimitAnalyzer != null) {
/*  840 */       this.fLimitAnalyzer.startEntity(name);
/*      */     }
/*  842 */     return encoding;
/*      */   }
/*      */ 
/*      */   public boolean isExternalEntity(String entityName)
/*      */   {
/*  855 */     Entity entity = (Entity)this.fEntities.get(entityName);
/*  856 */     if (entity == null) {
/*  857 */       return false;
/*      */     }
/*  859 */     return entity.isExternal();
/*      */   }
/*      */ 
/*      */   public boolean isEntityDeclInExternalSubset(String entityName)
/*      */   {
/*  872 */     Entity entity = (Entity)this.fEntities.get(entityName);
/*  873 */     if (entity == null) {
/*  874 */       return false;
/*      */     }
/*  876 */     return entity.isEntityDeclInExternalSubset();
/*      */   }
/*      */ 
/*      */   public void setStandalone(boolean standalone)
/*      */   {
/*  891 */     this.fStandalone = standalone;
/*      */   }
/*      */ 
/*      */   public boolean isStandalone()
/*      */   {
/*  897 */     return this.fStandalone;
/*      */   }
/*      */ 
/*      */   public boolean isDeclaredEntity(String entityName)
/*      */   {
/*  902 */     Entity entity = (Entity)this.fEntities.get(entityName);
/*  903 */     return entity != null;
/*      */   }
/*      */ 
/*      */   public boolean isUnparsedEntity(String entityName)
/*      */   {
/*  908 */     Entity entity = (Entity)this.fEntities.get(entityName);
/*  909 */     if (entity == null) {
/*  910 */       return false;
/*      */     }
/*  912 */     return entity.isUnparsed();
/*      */   }
/*      */ 
/*      */   public XMLResourceIdentifier getCurrentResourceIdentifier()
/*      */   {
/*  923 */     return this.fResourceIdentifier;
/*      */   }
/*      */ 
/*      */   public void setEntityHandler(XMLEntityHandler entityHandler)
/*      */   {
/*  934 */     this.fEntityHandler = entityHandler;
/*      */   }
/*      */ 
/*      */   public StaxXMLInputSource resolveEntityAsPerStax(XMLResourceIdentifier resourceIdentifier)
/*      */     throws IOException
/*      */   {
/*  940 */     if (resourceIdentifier == null) return null;
/*      */ 
/*  942 */     String publicId = resourceIdentifier.getPublicId();
/*  943 */     String literalSystemId = resourceIdentifier.getLiteralSystemId();
/*  944 */     String baseSystemId = resourceIdentifier.getBaseSystemId();
/*  945 */     String expandedSystemId = resourceIdentifier.getExpandedSystemId();
/*      */ 
/*  952 */     boolean needExpand = expandedSystemId == null;
/*      */ 
/*  956 */     if ((baseSystemId == null) && (this.fCurrentEntity != null) && (this.fCurrentEntity.entityLocation != null)) {
/*  957 */       baseSystemId = this.fCurrentEntity.entityLocation.getExpandedSystemId();
/*  958 */       if (baseSystemId != null)
/*  959 */         needExpand = true;
/*      */     }
/*  961 */     if (needExpand) {
/*  962 */       expandedSystemId = expandSystemId(literalSystemId, baseSystemId, false);
/*      */     }
/*      */ 
/*  965 */     StaxXMLInputSource staxInputSource = null;
/*  966 */     XMLInputSource xmlInputSource = null;
/*      */ 
/*  968 */     XMLResourceIdentifierImpl ri = null;
/*      */ 
/*  970 */     if ((resourceIdentifier instanceof XMLResourceIdentifierImpl)) {
/*  971 */       ri = (XMLResourceIdentifierImpl)resourceIdentifier;
/*      */     } else {
/*  973 */       this.fResourceIdentifier.clear();
/*  974 */       ri = this.fResourceIdentifier;
/*      */     }
/*  976 */     ri.setValues(publicId, literalSystemId, baseSystemId, expandedSystemId);
/*      */ 
/*  981 */     this.fISCreatedByResolver = false;
/*      */ 
/*  983 */     if (this.fStaxEntityResolver != null) {
/*  984 */       staxInputSource = this.fStaxEntityResolver.resolveEntity(ri);
/*  985 */       if (staxInputSource != null) {
/*  986 */         this.fISCreatedByResolver = true;
/*      */       }
/*      */     }
/*      */ 
/*  990 */     if (this.fEntityResolver != null) {
/*  991 */       xmlInputSource = this.fEntityResolver.resolveEntity(ri);
/*  992 */       if (xmlInputSource != null) {
/*  993 */         this.fISCreatedByResolver = true;
/*      */       }
/*      */     }
/*      */ 
/*  997 */     if (xmlInputSource != null)
/*      */     {
/*  999 */       staxInputSource = new StaxXMLInputSource(xmlInputSource, this.fISCreatedByResolver);
/*      */     }
/*      */ 
/* 1004 */     if (staxInputSource == null)
/*      */     {
/* 1008 */       staxInputSource = new StaxXMLInputSource(new XMLInputSource(publicId, literalSystemId, baseSystemId));
/* 1009 */     } else if (!staxInputSource.hasXMLStreamOrXMLEventReader());
/* 1018 */     return staxInputSource;
/*      */   }
/*      */ 
/*      */   public XMLInputSource resolveEntity(XMLResourceIdentifier resourceIdentifier)
/*      */     throws IOException, XNIException
/*      */   {
/* 1045 */     if (resourceIdentifier == null) return null;
/* 1046 */     String publicId = resourceIdentifier.getPublicId();
/* 1047 */     String literalSystemId = resourceIdentifier.getLiteralSystemId();
/* 1048 */     String baseSystemId = resourceIdentifier.getBaseSystemId();
/* 1049 */     String expandedSystemId = resourceIdentifier.getExpandedSystemId();
/* 1050 */     String namespace = resourceIdentifier.getNamespace();
/*      */ 
/* 1058 */     boolean needExpand = expandedSystemId == null;
/*      */ 
/* 1062 */     if ((baseSystemId == null) && (this.fCurrentEntity != null) && (this.fCurrentEntity.entityLocation != null)) {
/* 1063 */       baseSystemId = this.fCurrentEntity.entityLocation.getExpandedSystemId();
/* 1064 */       if (baseSystemId != null)
/* 1065 */         needExpand = true;
/*      */     }
/* 1067 */     if (needExpand) {
/* 1068 */       expandedSystemId = expandSystemId(literalSystemId, baseSystemId, false);
/*      */     }
/*      */ 
/* 1071 */     XMLInputSource xmlInputSource = null;
/*      */ 
/* 1073 */     if (this.fEntityResolver != null) {
/* 1074 */       resourceIdentifier.setBaseSystemId(baseSystemId);
/* 1075 */       resourceIdentifier.setExpandedSystemId(expandedSystemId);
/* 1076 */       xmlInputSource = this.fEntityResolver.resolveEntity(resourceIdentifier);
/*      */     }
/*      */ 
/* 1084 */     if (xmlInputSource == null)
/*      */     {
/* 1088 */       xmlInputSource = new XMLInputSource(publicId, literalSystemId, baseSystemId);
/*      */     }
/*      */ 
/* 1096 */     return xmlInputSource;
/*      */   }
/*      */ 
/*      */   public void startEntity(String entityName, boolean literal)
/*      */     throws IOException, XNIException
/*      */   {
/* 1114 */     Entity entity = this.fEntityStorage.getEntity(entityName);
/* 1115 */     if (entity == null) {
/* 1116 */       if (this.fEntityHandler != null) {
/* 1117 */         String encoding = null;
/* 1118 */         this.fResourceIdentifier.clear();
/* 1119 */         this.fEntityAugs.removeAllItems();
/* 1120 */         this.fEntityAugs.putItem("ENTITY_SKIPPED", Boolean.TRUE);
/* 1121 */         this.fEntityHandler.startEntity(entityName, this.fResourceIdentifier, encoding, this.fEntityAugs);
/* 1122 */         this.fEntityAugs.removeAllItems();
/* 1123 */         this.fEntityAugs.putItem("ENTITY_SKIPPED", Boolean.TRUE);
/* 1124 */         this.fEntityHandler.endEntity(entityName, this.fEntityAugs);
/*      */       }
/* 1126 */       return;
/*      */     }
/*      */ 
/* 1130 */     boolean external = entity.isExternal();
/* 1131 */     Entity.ExternalEntity externalEntity = null;
/* 1132 */     String extLitSysId = null; String extBaseSysId = null; String expandedSystemId = null;
/* 1133 */     if (external) {
/* 1134 */       externalEntity = (Entity.ExternalEntity)entity;
/* 1135 */       extLitSysId = externalEntity.entityLocation != null ? externalEntity.entityLocation.getLiteralSystemId() : null;
/* 1136 */       extBaseSysId = externalEntity.entityLocation != null ? externalEntity.entityLocation.getBaseSystemId() : null;
/* 1137 */       expandedSystemId = expandSystemId(extLitSysId, extBaseSysId);
/* 1138 */       boolean unparsed = entity.isUnparsed();
/* 1139 */       boolean parameter = entityName.startsWith("%");
/* 1140 */       boolean general = !parameter;
/* 1141 */       if ((unparsed) || ((general) && (!this.fExternalGeneralEntities)) || ((parameter) && (!this.fExternalParameterEntities)) || (!this.fSupportDTD) || (!this.fSupportExternalEntities))
/*      */       {
/* 1145 */         if (this.fEntityHandler != null) {
/* 1146 */           this.fResourceIdentifier.clear();
/* 1147 */           String encoding = null;
/* 1148 */           this.fResourceIdentifier.setValues(externalEntity.entityLocation != null ? externalEntity.entityLocation.getPublicId() : null, extLitSysId, extBaseSysId, expandedSystemId);
/*      */ 
/* 1151 */           this.fEntityAugs.removeAllItems();
/* 1152 */           this.fEntityAugs.putItem("ENTITY_SKIPPED", Boolean.TRUE);
/* 1153 */           this.fEntityHandler.startEntity(entityName, this.fResourceIdentifier, encoding, this.fEntityAugs);
/* 1154 */           this.fEntityAugs.removeAllItems();
/* 1155 */           this.fEntityAugs.putItem("ENTITY_SKIPPED", Boolean.TRUE);
/* 1156 */           this.fEntityHandler.endEntity(entityName, this.fEntityAugs);
/*      */         }
/* 1158 */         return;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1163 */     int size = this.fEntityStack.size();
/* 1164 */     for (int i = size; i >= 0; i--) {
/* 1165 */       Entity activeEntity = i == size ? this.fCurrentEntity : (Entity)this.fEntityStack.elementAt(i);
/*      */ 
/* 1168 */       if (activeEntity.name == entityName) {
/* 1169 */         String path = entityName;
/* 1170 */         for (int j = i + 1; j < size; j++) {
/* 1171 */           activeEntity = (Entity)this.fEntityStack.elementAt(j);
/* 1172 */           path = path + " -> " + activeEntity.name;
/*      */         }
/* 1174 */         path = path + " -> " + this.fCurrentEntity.name;
/* 1175 */         path = path + " -> " + entityName;
/* 1176 */         this.fErrorReporter.reportError(getEntityScanner(), "http://www.w3.org/TR/1998/REC-xml-19980210", "RecursiveReference", new Object[] { entityName, path }, (short)2);
/*      */ 
/* 1181 */         if (this.fEntityHandler != null) {
/* 1182 */           this.fResourceIdentifier.clear();
/* 1183 */           String encoding = null;
/* 1184 */           if (external) {
/* 1185 */             this.fResourceIdentifier.setValues(externalEntity.entityLocation != null ? externalEntity.entityLocation.getPublicId() : null, extLitSysId, extBaseSysId, expandedSystemId);
/*      */           }
/*      */ 
/* 1189 */           this.fEntityAugs.removeAllItems();
/* 1190 */           this.fEntityAugs.putItem("ENTITY_SKIPPED", Boolean.TRUE);
/* 1191 */           this.fEntityHandler.startEntity(entityName, this.fResourceIdentifier, encoding, this.fEntityAugs);
/* 1192 */           this.fEntityAugs.removeAllItems();
/* 1193 */           this.fEntityAugs.putItem("ENTITY_SKIPPED", Boolean.TRUE);
/* 1194 */           this.fEntityHandler.endEntity(entityName, this.fEntityAugs);
/*      */         }
/*      */ 
/* 1197 */         return;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1202 */     StaxXMLInputSource staxInputSource = null;
/* 1203 */     XMLInputSource xmlInputSource = null;
/*      */ 
/* 1205 */     if (external) {
/* 1206 */       staxInputSource = resolveEntityAsPerStax(externalEntity.entityLocation);
/*      */ 
/* 1212 */       xmlInputSource = staxInputSource.getXMLInputSource();
/* 1213 */       if (!this.fISCreatedByResolver)
/*      */       {
/* 1215 */         if (this.fLoadExternalDTD) {
/* 1216 */           String accessError = SecuritySupport.checkAccess(expandedSystemId, this.fAccessExternalDTD, "all");
/* 1217 */           if (accessError != null) {
/* 1218 */             this.fErrorReporter.reportError(getEntityScanner(), "http://www.w3.org/TR/1998/REC-xml-19980210", "AccessExternalEntity", new Object[] { SecuritySupport.sanitizePath(expandedSystemId), accessError }, (short)2);
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/* 1228 */       Entity.InternalEntity internalEntity = (Entity.InternalEntity)entity;
/* 1229 */       Reader reader = new StringReader(internalEntity.text);
/* 1230 */       xmlInputSource = new XMLInputSource(null, null, null, reader, null);
/*      */     }
/*      */ 
/* 1234 */     startEntity(entityName, xmlInputSource, literal, external);
/*      */   }
/*      */ 
/*      */   public void startDocumentEntity(XMLInputSource xmlInputSource)
/*      */     throws IOException, XNIException
/*      */   {
/* 1249 */     startEntity(XMLEntity, xmlInputSource, false, true);
/*      */   }
/*      */ 
/*      */   public void startDTDEntity(XMLInputSource xmlInputSource)
/*      */     throws IOException, XNIException
/*      */   {
/* 1264 */     startEntity(DTDEntity, xmlInputSource, false, true);
/*      */   }
/*      */ 
/*      */   public void startExternalSubset()
/*      */   {
/* 1270 */     this.fInExternalSubset = true;
/*      */   }
/*      */ 
/*      */   public void endExternalSubset() {
/* 1274 */     this.fInExternalSubset = false;
/*      */   }
/*      */ 
/*      */   public void startEntity(String name, XMLInputSource xmlInputSource, boolean literal, boolean isExternal)
/*      */     throws IOException, XNIException
/*      */   {
/* 1297 */     String encoding = setupCurrentEntity(name, xmlInputSource, literal, isExternal);
/*      */ 
/* 1303 */     this.fEntityExpansionCount += 1;
/* 1304 */     if (this.fLimitAnalyzer != null) {
/* 1305 */       this.fLimitAnalyzer.addValue(this.entityExpansionIndex, name, 1);
/*      */     }
/* 1307 */     if ((this.fSecurityManager != null) && (this.fSecurityManager.isOverLimit(this.entityExpansionIndex, this.fLimitAnalyzer))) {
/* 1308 */       this.fSecurityManager.debugPrint(this.fLimitAnalyzer);
/* 1309 */       this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "EntityExpansionLimitExceeded", new Object[] { this.fSecurityManager.getLimitValueByIndex(this.entityExpansionIndex) }, (short)2);
/*      */ 
/* 1315 */       this.fEntityExpansionCount = 0;
/*      */     }
/*      */ 
/* 1319 */     if (this.fEntityHandler != null)
/* 1320 */       this.fEntityHandler.startEntity(name, this.fResourceIdentifier, encoding, null);
/*      */   }
/*      */ 
/*      */   public Entity.ScannedEntity getCurrentEntity()
/*      */   {
/* 1331 */     return this.fCurrentEntity;
/*      */   }
/*      */ 
/*      */   public Entity.ScannedEntity getTopLevelEntity()
/*      */   {
/* 1339 */     return (Entity.ScannedEntity)(this.fEntityStack.empty() ? null : this.fEntityStack.elementAt(0));
/*      */   }
/*      */ 
/*      */   public void closeReaders()
/*      */   {
/*      */   }
/*      */ 
/*      */   public void endEntity()
/*      */     throws IOException, XNIException
/*      */   {
/* 1364 */     Entity.ScannedEntity entity = this.fEntityStack.size() > 0 ? (Entity.ScannedEntity)this.fEntityStack.pop() : null;
/*      */ 
/* 1371 */     if (this.fCurrentEntity != null) {
/*      */       try
/*      */       {
/* 1374 */         if (this.fLimitAnalyzer != null) {
/* 1375 */           this.fLimitAnalyzer.endEntity(XMLSecurityManager.Limit.GENEAL_ENTITY_SIZE_LIMIT, this.fCurrentEntity.name);
/* 1376 */           if (this.fCurrentEntity.name.equals("[xml]")) {
/* 1377 */             this.fSecurityManager.debugPrint(this.fLimitAnalyzer);
/*      */           }
/*      */         }
/* 1380 */         this.fCurrentEntity.close();
/*      */       } catch (IOException ex) {
/* 1382 */         throw new XNIException(ex);
/*      */       }
/*      */     }
/*      */ 
/* 1386 */     if (this.fEntityHandler != null)
/*      */     {
/* 1388 */       if (entity == null) {
/* 1389 */         this.fEntityAugs.removeAllItems();
/* 1390 */         this.fEntityAugs.putItem("LAST_ENTITY", Boolean.TRUE);
/* 1391 */         this.fEntityHandler.endEntity(this.fCurrentEntity.name, this.fEntityAugs);
/* 1392 */         this.fEntityAugs.removeAllItems();
/*      */       } else {
/* 1394 */         this.fEntityHandler.endEntity(this.fCurrentEntity.name, null);
/*      */       }
/*      */     }
/*      */ 
/* 1398 */     boolean documentEntity = this.fCurrentEntity.name == XMLEntity;
/*      */ 
/* 1401 */     this.fCurrentEntity = entity;
/* 1402 */     this.fEntityScanner.setCurrentEntity(this.fCurrentEntity);
/*      */ 
/* 1408 */     if (((this.fCurrentEntity == null ? 1 : 0) & (!documentEntity ? 1 : 0)) != 0)
/* 1409 */       throw new EOFException();
/*      */   }
/*      */ 
/*      */   public void reset(PropertyManager propertyManager)
/*      */   {
/* 1426 */     this.fEntityStorage.reset(propertyManager);
/*      */ 
/* 1428 */     this.fEntityScanner.reset(propertyManager);
/*      */ 
/* 1430 */     this.fSymbolTable = ((SymbolTable)propertyManager.getProperty("http://apache.org/xml/properties/internal/symbol-table"));
/* 1431 */     this.fErrorReporter = ((XMLErrorReporter)propertyManager.getProperty("http://apache.org/xml/properties/internal/error-reporter"));
/*      */     try {
/* 1433 */       this.fStaxEntityResolver = ((StaxEntityResolverWrapper)propertyManager.getProperty("http://apache.org/xml/properties/internal/stax-entity-resolver"));
/*      */     } catch (XMLConfigurationException e) {
/* 1435 */       this.fStaxEntityResolver = null;
/*      */     }
/*      */ 
/* 1438 */     this.fSupportDTD = ((Boolean)propertyManager.getProperty("javax.xml.stream.supportDTD")).booleanValue();
/* 1439 */     this.fReplaceEntityReferences = ((Boolean)propertyManager.getProperty("javax.xml.stream.isReplacingEntityReferences")).booleanValue();
/* 1440 */     this.fSupportExternalEntities = ((Boolean)propertyManager.getProperty("javax.xml.stream.isSupportingExternalEntities")).booleanValue();
/*      */ 
/* 1443 */     this.fLoadExternalDTD = (!((Boolean)propertyManager.getProperty("http://java.sun.com/xml/stream/properties/ignore-external-dtd")).booleanValue());
/*      */ 
/* 1446 */     XMLSecurityPropertyManager spm = (XMLSecurityPropertyManager)propertyManager.getProperty("http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager");
/* 1447 */     this.fAccessExternalDTD = spm.getValue(XMLSecurityPropertyManager.Property.ACCESS_EXTERNAL_DTD);
/*      */ 
/* 1449 */     this.fSecurityManager = ((XMLSecurityManager)propertyManager.getProperty("http://apache.org/xml/properties/security-manager"));
/*      */ 
/* 1453 */     this.fEntities.clear();
/* 1454 */     this.fEntityStack.removeAllElements();
/* 1455 */     this.fCurrentEntity = null;
/* 1456 */     this.fValidation = false;
/* 1457 */     this.fExternalGeneralEntities = true;
/* 1458 */     this.fExternalParameterEntities = true;
/* 1459 */     this.fAllowJavaEncodings = true;
/*      */   }
/*      */ 
/*      */   public void reset(XMLComponentManager componentManager)
/*      */     throws XMLConfigurationException
/*      */   {
/* 1479 */     boolean parser_settings = componentManager.getFeature("http://apache.org/xml/features/internal/parser-settings", true);
/*      */ 
/* 1481 */     if (!parser_settings)
/*      */     {
/* 1483 */       reset();
/* 1484 */       if (this.fEntityScanner != null) {
/* 1485 */         this.fEntityScanner.reset(componentManager);
/*      */       }
/* 1487 */       if (this.fEntityStorage != null) {
/* 1488 */         this.fEntityStorage.reset(componentManager);
/*      */       }
/* 1490 */       return;
/*      */     }
/*      */ 
/* 1494 */     this.fValidation = componentManager.getFeature("http://xml.org/sax/features/validation", false);
/* 1495 */     this.fExternalGeneralEntities = componentManager.getFeature("http://xml.org/sax/features/external-general-entities", true);
/* 1496 */     this.fExternalParameterEntities = componentManager.getFeature("http://xml.org/sax/features/external-parameter-entities", true);
/*      */ 
/* 1499 */     this.fAllowJavaEncodings = componentManager.getFeature("http://apache.org/xml/features/allow-java-encodings", false);
/* 1500 */     this.fWarnDuplicateEntityDef = componentManager.getFeature("http://apache.org/xml/features/warn-on-duplicate-entitydef", false);
/* 1501 */     this.fStrictURI = componentManager.getFeature("http://apache.org/xml/features/standard-uri-conformant", false);
/* 1502 */     this.fLoadExternalDTD = componentManager.getFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", true);
/*      */ 
/* 1505 */     this.fSymbolTable = ((SymbolTable)componentManager.getProperty("http://apache.org/xml/properties/internal/symbol-table"));
/* 1506 */     this.fErrorReporter = ((XMLErrorReporter)componentManager.getProperty("http://apache.org/xml/properties/internal/error-reporter"));
/* 1507 */     this.fEntityResolver = ((XMLEntityResolver)componentManager.getProperty("http://apache.org/xml/properties/internal/entity-resolver", null));
/* 1508 */     this.fStaxEntityResolver = ((StaxEntityResolverWrapper)componentManager.getProperty("http://apache.org/xml/properties/internal/stax-entity-resolver", null));
/* 1509 */     this.fValidationManager = ((ValidationManager)componentManager.getProperty("http://apache.org/xml/properties/internal/validation-manager", null));
/* 1510 */     this.fSecurityManager = ((XMLSecurityManager)componentManager.getProperty("http://apache.org/xml/properties/security-manager", null));
/* 1511 */     this.entityExpansionIndex = this.fSecurityManager.getIndex("http://www.oracle.com/xml/jaxp/properties/entityExpansionLimit");
/*      */ 
/* 1514 */     this.fSupportDTD = true;
/* 1515 */     this.fReplaceEntityReferences = true;
/* 1516 */     this.fSupportExternalEntities = true;
/*      */ 
/* 1519 */     XMLSecurityPropertyManager spm = (XMLSecurityPropertyManager)componentManager.getProperty("http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager", null);
/* 1520 */     if (spm == null) {
/* 1521 */       spm = new XMLSecurityPropertyManager();
/*      */     }
/* 1523 */     this.fAccessExternalDTD = spm.getValue(XMLSecurityPropertyManager.Property.ACCESS_EXTERNAL_DTD);
/*      */ 
/* 1526 */     reset();
/*      */ 
/* 1528 */     this.fEntityScanner.reset(componentManager);
/* 1529 */     this.fEntityStorage.reset(componentManager);
/*      */   }
/*      */ 
/*      */   public void reset()
/*      */   {
/* 1539 */     this.fStandalone = false;
/* 1540 */     this.fEntities.clear();
/* 1541 */     this.fEntityStack.removeAllElements();
/* 1542 */     this.fEntityExpansionCount = 0;
/*      */ 
/* 1544 */     this.fCurrentEntity = null;
/*      */ 
/* 1546 */     if (this.fXML10EntityScanner != null) {
/* 1547 */       this.fXML10EntityScanner.reset(this.fSymbolTable, this, this.fErrorReporter);
/*      */     }
/* 1549 */     if (this.fXML11EntityScanner != null) {
/* 1550 */       this.fXML11EntityScanner.reset(this.fSymbolTable, this, this.fErrorReporter);
/*      */     }
/*      */ 
/* 1575 */     this.fEntityHandler = null;
/*      */   }
/*      */ 
/*      */   public String[] getRecognizedFeatures()
/*      */   {
/* 1588 */     return (String[])RECOGNIZED_FEATURES.clone();
/*      */   }
/*      */ 
/*      */   public void setFeature(String featureId, boolean state)
/*      */     throws XMLConfigurationException
/*      */   {
/* 1610 */     if (featureId.startsWith("http://apache.org/xml/features/")) {
/* 1611 */       int suffixLength = featureId.length() - "http://apache.org/xml/features/".length();
/* 1612 */       if ((suffixLength == "allow-java-encodings".length()) && (featureId.endsWith("allow-java-encodings")))
/*      */       {
/* 1614 */         this.fAllowJavaEncodings = state;
/*      */       }
/* 1616 */       if ((suffixLength == "nonvalidating/load-external-dtd".length()) && (featureId.endsWith("nonvalidating/load-external-dtd")))
/*      */       {
/* 1618 */         this.fLoadExternalDTD = state;
/* 1619 */         return;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setProperty(String propertyId, Object value)
/*      */   {
/* 1642 */     if (propertyId.startsWith("http://apache.org/xml/properties/")) {
/* 1643 */       int suffixLength = propertyId.length() - "http://apache.org/xml/properties/".length();
/*      */ 
/* 1645 */       if ((suffixLength == "internal/symbol-table".length()) && (propertyId.endsWith("internal/symbol-table")))
/*      */       {
/* 1647 */         this.fSymbolTable = ((SymbolTable)value);
/* 1648 */         return;
/*      */       }
/* 1650 */       if ((suffixLength == "internal/error-reporter".length()) && (propertyId.endsWith("internal/error-reporter")))
/*      */       {
/* 1652 */         this.fErrorReporter = ((XMLErrorReporter)value);
/* 1653 */         return;
/*      */       }
/* 1655 */       if ((suffixLength == "internal/entity-resolver".length()) && (propertyId.endsWith("internal/entity-resolver")))
/*      */       {
/* 1657 */         this.fEntityResolver = ((XMLEntityResolver)value);
/* 1658 */         return;
/*      */       }
/* 1660 */       if ((suffixLength == "input-buffer-size".length()) && (propertyId.endsWith("input-buffer-size")))
/*      */       {
/* 1662 */         Integer bufferSize = (Integer)value;
/* 1663 */         if ((bufferSize != null) && (bufferSize.intValue() > 64))
/*      */         {
/* 1665 */           this.fBufferSize = bufferSize.intValue();
/* 1666 */           this.fEntityScanner.setBufferSize(this.fBufferSize);
/* 1667 */           this.fBufferPool.setExternalBufferSize(this.fBufferSize);
/*      */         }
/*      */       }
/* 1670 */       if ((suffixLength == "security-manager".length()) && (propertyId.endsWith("security-manager")))
/*      */       {
/* 1672 */         this.fSecurityManager = ((XMLSecurityManager)value);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1677 */     if (propertyId.equals("http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager"))
/*      */     {
/* 1679 */       XMLSecurityPropertyManager spm = (XMLSecurityPropertyManager)value;
/* 1680 */       this.fAccessExternalDTD = spm.getValue(XMLSecurityPropertyManager.Property.ACCESS_EXTERNAL_DTD);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setLimitAnalyzer(XMLLimitAnalyzer fLimitAnalyzer) {
/* 1685 */     this.fLimitAnalyzer = fLimitAnalyzer;
/*      */   }
/*      */ 
/*      */   public String[] getRecognizedProperties()
/*      */   {
/* 1694 */     return (String[])RECOGNIZED_PROPERTIES.clone();
/*      */   }
/*      */ 
/*      */   public Boolean getFeatureDefault(String featureId)
/*      */   {
/* 1706 */     for (int i = 0; i < RECOGNIZED_FEATURES.length; i++) {
/* 1707 */       if (RECOGNIZED_FEATURES[i].equals(featureId)) {
/* 1708 */         return FEATURE_DEFAULTS[i];
/*      */       }
/*      */     }
/* 1711 */     return null;
/*      */   }
/*      */ 
/*      */   public Object getPropertyDefault(String propertyId)
/*      */   {
/* 1724 */     for (int i = 0; i < RECOGNIZED_PROPERTIES.length; i++) {
/* 1725 */       if (RECOGNIZED_PROPERTIES[i].equals(propertyId)) {
/* 1726 */         return PROPERTY_DEFAULTS[i];
/*      */       }
/*      */     }
/* 1729 */     return null;
/*      */   }
/*      */ 
/*      */   public static String expandSystemId(String systemId)
/*      */   {
/* 1750 */     return expandSystemId(systemId, null);
/*      */   }
/*      */ 
/*      */   private static synchronized com.sun.org.apache.xerces.internal.util.URI getUserDir()
/*      */     throws URI.MalformedURIException
/*      */   {
/* 1803 */     String userDir = "";
/*      */     try {
/* 1805 */       userDir = SecuritySupport.getSystemProperty("user.dir");
/*      */     }
/*      */     catch (SecurityException se)
/*      */     {
/*      */     }
/*      */ 
/* 1811 */     if (userDir.length() == 0) {
/* 1812 */       return new com.sun.org.apache.xerces.internal.util.URI("file", "", "", null, null);
/*      */     }
/*      */ 
/* 1815 */     if ((gUserDirURI != null) && (userDir.equals(gUserDir))) {
/* 1816 */       return gUserDirURI;
/*      */     }
/*      */ 
/* 1820 */     gUserDir = userDir;
/*      */ 
/* 1822 */     char separator = File.separatorChar;
/* 1823 */     userDir = userDir.replace(separator, '/');
/*      */ 
/* 1825 */     int len = userDir.length();
/* 1826 */     StringBuffer buffer = new StringBuffer(len * 3);
/*      */ 
/* 1828 */     if ((len >= 2) && (userDir.charAt(1) == ':')) {
/* 1829 */       int ch = Character.toUpperCase(userDir.charAt(0));
/* 1830 */       if ((ch >= 65) && (ch <= 90)) {
/* 1831 */         buffer.append('/');
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1836 */     for (int i = 0; 
/* 1837 */       i < len; i++) {
/* 1838 */       int ch = userDir.charAt(i);
/*      */ 
/* 1840 */       if (ch >= 128)
/*      */         break;
/* 1842 */       if (gNeedEscaping[ch] != 0) {
/* 1843 */         buffer.append('%');
/* 1844 */         buffer.append(gAfterEscaping1[ch]);
/* 1845 */         buffer.append(gAfterEscaping2[ch]);
/*      */       }
/*      */       else
/*      */       {
/* 1849 */         buffer.append((char)ch);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1854 */     if (i < len)
/*      */     {
/* 1856 */       byte[] bytes = null;
/*      */       try
/*      */       {
/* 1859 */         bytes = userDir.substring(i).getBytes("UTF-8");
/*      */       }
/*      */       catch (UnsupportedEncodingException e) {
/* 1862 */         return new com.sun.org.apache.xerces.internal.util.URI("file", "", userDir, null, null);
/*      */       }
/* 1864 */       len = bytes.length;
/*      */ 
/* 1867 */       for (i = 0; i < len; i++) {
/* 1868 */         byte b = bytes[i];
/*      */ 
/* 1870 */         if (b < 0) {
/* 1871 */           int ch = b + 256;
/* 1872 */           buffer.append('%');
/* 1873 */           buffer.append(gHexChs[(ch >> 4)]);
/* 1874 */           buffer.append(gHexChs[(ch & 0xF)]);
/*      */         }
/* 1876 */         else if (gNeedEscaping[b] != 0) {
/* 1877 */           buffer.append('%');
/* 1878 */           buffer.append(gAfterEscaping1[b]);
/* 1879 */           buffer.append(gAfterEscaping2[b]);
/*      */         }
/*      */         else {
/* 1882 */           buffer.append((char)b);
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1888 */     if (!userDir.endsWith("/")) {
/* 1889 */       buffer.append('/');
/*      */     }
/* 1891 */     gUserDirURI = new com.sun.org.apache.xerces.internal.util.URI("file", "", buffer.toString(), null, null);
/*      */ 
/* 1893 */     return gUserDirURI;
/*      */   }
/*      */ 
/*      */   public static void absolutizeAgainstUserDir(com.sun.org.apache.xerces.internal.util.URI uri)
/*      */     throws URI.MalformedURIException
/*      */   {
/* 1905 */     uri.absolutize(getUserDir());
/*      */   }
/*      */ 
/*      */   public static String expandSystemId(String systemId, String baseSystemId)
/*      */   {
/* 1924 */     if ((systemId == null) || (systemId.length() == 0)) {
/* 1925 */       return systemId;
/*      */     }
/*      */     try
/*      */     {
/* 1929 */       com.sun.org.apache.xerces.internal.util.URI uri = new com.sun.org.apache.xerces.internal.util.URI(systemId);
/* 1930 */       if (uri != null) {
/* 1931 */         return systemId;
/*      */       }
/*      */     }
/*      */     catch (URI.MalformedURIException e)
/*      */     {
/*      */     }
/* 1937 */     String id = fixURI(systemId);
/*      */ 
/* 1940 */     com.sun.org.apache.xerces.internal.util.URI base = null;
/* 1941 */     com.sun.org.apache.xerces.internal.util.URI uri = null;
/*      */     try {
/* 1943 */       if ((baseSystemId == null) || (baseSystemId.length() == 0) || (baseSystemId.equals(systemId)))
/*      */       {
/* 1945 */         String dir = getUserDir().toString();
/* 1946 */         base = new com.sun.org.apache.xerces.internal.util.URI("file", "", dir, null, null);
/*      */       } else {
/*      */         try {
/* 1949 */           base = new com.sun.org.apache.xerces.internal.util.URI(fixURI(baseSystemId));
/*      */         } catch (URI.MalformedURIException e) {
/* 1951 */           if (baseSystemId.indexOf(':') != -1)
/*      */           {
/* 1954 */             base = new com.sun.org.apache.xerces.internal.util.URI("file", "", fixURI(baseSystemId), null, null);
/*      */           } else {
/* 1956 */             String dir = getUserDir().toString();
/* 1957 */             dir = dir + fixURI(baseSystemId);
/* 1958 */             base = new com.sun.org.apache.xerces.internal.util.URI("file", "", dir, null, null);
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/* 1963 */       uri = new com.sun.org.apache.xerces.internal.util.URI(base, id);
/*      */     }
/*      */     catch (Exception e)
/*      */     {
/*      */     }
/*      */ 
/* 1969 */     if (uri == null) {
/* 1970 */       return systemId;
/*      */     }
/* 1972 */     return uri.toString();
/*      */   }
/*      */ 
/*      */   public static String expandSystemId(String systemId, String baseSystemId, boolean strict)
/*      */     throws URI.MalformedURIException
/*      */   {
/* 1995 */     if (systemId == null) {
/* 1996 */       return null;
/*      */     }
/*      */ 
/* 2000 */     if (strict)
/*      */     {
/* 2005 */       if (systemId == null) {
/* 2006 */         return null;
/*      */       }
/*      */ 
/*      */       try
/*      */       {
/* 2011 */         new com.sun.org.apache.xerces.internal.util.URI(systemId);
/* 2012 */         return systemId;
/*      */       }
/*      */       catch (URI.MalformedURIException ex)
/*      */       {
/* 2016 */         com.sun.org.apache.xerces.internal.util.URI base = null;
/*      */ 
/* 2018 */         if ((baseSystemId == null) || (baseSystemId.length() == 0)) {
/* 2019 */           base = new com.sun.org.apache.xerces.internal.util.URI("file", "", getUserDir().toString(), null, null);
/*      */         }
/*      */         else {
/*      */           try
/*      */           {
/* 2024 */             base = new com.sun.org.apache.xerces.internal.util.URI(baseSystemId);
/*      */           }
/*      */           catch (URI.MalformedURIException e)
/*      */           {
/* 2028 */             String dir = getUserDir().toString();
/* 2029 */             dir = dir + baseSystemId;
/* 2030 */             base = new com.sun.org.apache.xerces.internal.util.URI("file", "", dir, null, null);
/*      */           }
/*      */         }
/*      */ 
/* 2034 */         com.sun.org.apache.xerces.internal.util.URI uri = new com.sun.org.apache.xerces.internal.util.URI(base, systemId);
/*      */ 
/* 2036 */         return uri.toString();
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/* 2043 */       return expandSystemIdStrictOff(systemId, baseSystemId);
/*      */     }
/*      */     catch (URI.MalformedURIException e)
/*      */     {
/*      */       try
/*      */       {
/* 2052 */         return expandSystemIdStrictOff1(systemId, baseSystemId);
/*      */       }
/*      */       catch (URISyntaxException ex)
/*      */       {
/* 2058 */         if (systemId.length() == 0) {
/* 2059 */           return systemId;
/*      */         }
/*      */ 
/* 2063 */         String id = fixURI(systemId);
/*      */ 
/* 2066 */         com.sun.org.apache.xerces.internal.util.URI base = null;
/* 2067 */         com.sun.org.apache.xerces.internal.util.URI uri = null;
/*      */         try {
/* 2069 */           if ((baseSystemId == null) || (baseSystemId.length() == 0) || (baseSystemId.equals(systemId)))
/*      */           {
/* 2071 */             base = getUserDir();
/*      */           }
/*      */           else {
/*      */             try {
/* 2075 */               base = new com.sun.org.apache.xerces.internal.util.URI(fixURI(baseSystemId).trim());
/*      */             }
/*      */             catch (URI.MalformedURIException e) {
/* 2078 */               if (baseSystemId.indexOf(':') != -1)
/*      */               {
/* 2081 */                 base = new com.sun.org.apache.xerces.internal.util.URI("file", "", fixURI(baseSystemId).trim(), null, null);
/*      */               }
/*      */               else {
/* 2084 */                 base = new com.sun.org.apache.xerces.internal.util.URI(getUserDir(), fixURI(baseSystemId));
/*      */               }
/*      */             }
/*      */           }
/*      */ 
/* 2089 */           uri = new com.sun.org.apache.xerces.internal.util.URI(base, id.trim());
/*      */         }
/*      */         catch (Exception e)
/*      */         {
/*      */         }
/*      */ 
/* 2096 */         if (uri == null) {
/* 2097 */           return systemId;
/*      */         }
/* 2099 */         return uri.toString();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private static String expandSystemIdStrictOn(String systemId, String baseSystemId)
/*      */     throws URI.MalformedURIException
/*      */   {
/* 2109 */     com.sun.org.apache.xerces.internal.util.URI systemURI = new com.sun.org.apache.xerces.internal.util.URI(systemId, true);
/*      */ 
/* 2111 */     if (systemURI.isAbsoluteURI()) {
/* 2112 */       return systemId;
/*      */     }
/*      */ 
/* 2116 */     com.sun.org.apache.xerces.internal.util.URI baseURI = null;
/* 2117 */     if ((baseSystemId == null) || (baseSystemId.length() == 0)) {
/* 2118 */       baseURI = getUserDir();
/*      */     }
/*      */     else {
/* 2121 */       baseURI = new com.sun.org.apache.xerces.internal.util.URI(baseSystemId, true);
/* 2122 */       if (!baseURI.isAbsoluteURI())
/*      */       {
/* 2124 */         baseURI.absolutize(getUserDir());
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2129 */     systemURI.absolutize(baseURI);
/*      */ 
/* 2132 */     return systemURI.toString();
/*      */   }
/*      */ 
/*      */   public static void setInstanceFollowRedirects(HttpURLConnection urlCon, boolean followRedirects)
/*      */   {
/*      */     try
/*      */     {
/* 2144 */       Method method = HttpURLConnection.class.getMethod("setInstanceFollowRedirects", new Class[] { Boolean.TYPE });
/* 2145 */       method.invoke(urlCon, new Object[] { followRedirects ? Boolean.TRUE : Boolean.FALSE });
/*      */     }
/*      */     catch (Exception exc)
/*      */     {
/*      */     }
/*      */   }
/*      */ 
/*      */   private static String expandSystemIdStrictOff(String systemId, String baseSystemId)
/*      */     throws URI.MalformedURIException
/*      */   {
/* 2158 */     com.sun.org.apache.xerces.internal.util.URI systemURI = new com.sun.org.apache.xerces.internal.util.URI(systemId, true);
/*      */ 
/* 2160 */     if (systemURI.isAbsoluteURI()) {
/* 2161 */       if (systemURI.getScheme().length() > 1) {
/* 2162 */         return systemId;
/*      */       }
/*      */ 
/* 2170 */       throw new URI.MalformedURIException();
/*      */     }
/*      */ 
/* 2174 */     com.sun.org.apache.xerces.internal.util.URI baseURI = null;
/* 2175 */     if ((baseSystemId == null) || (baseSystemId.length() == 0)) {
/* 2176 */       baseURI = getUserDir();
/*      */     }
/*      */     else {
/* 2179 */       baseURI = new com.sun.org.apache.xerces.internal.util.URI(baseSystemId, true);
/* 2180 */       if (!baseURI.isAbsoluteURI())
/*      */       {
/* 2182 */         baseURI.absolutize(getUserDir());
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2187 */     systemURI.absolutize(baseURI);
/*      */ 
/* 2190 */     return systemURI.toString();
/*      */   }
/*      */ 
/*      */   private static String expandSystemIdStrictOff1(String systemId, String baseSystemId)
/*      */     throws URISyntaxException, URI.MalformedURIException
/*      */   {
/* 2199 */     java.net.URI systemURI = new java.net.URI(systemId);
/*      */ 
/* 2201 */     if (systemURI.isAbsolute()) {
/* 2202 */       if (systemURI.getScheme().length() > 1) {
/* 2203 */         return systemId;
/*      */       }
/*      */ 
/* 2211 */       throw new URISyntaxException(systemId, "the scheme's length is only one character");
/*      */     }
/*      */ 
/* 2215 */     com.sun.org.apache.xerces.internal.util.URI baseURI = null;
/* 2216 */     if ((baseSystemId == null) || (baseSystemId.length() == 0)) {
/* 2217 */       baseURI = getUserDir();
/*      */     }
/*      */     else {
/* 2220 */       baseURI = new com.sun.org.apache.xerces.internal.util.URI(baseSystemId, true);
/* 2221 */       if (!baseURI.isAbsoluteURI())
/*      */       {
/* 2223 */         baseURI.absolutize(getUserDir());
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2229 */     systemURI = new java.net.URI(baseURI.toString()).resolve(systemURI);
/*      */ 
/* 2232 */     return systemURI.toString();
/*      */   }
/*      */ 
/*      */   protected Object[] getEncodingName(byte[] b4, int count)
/*      */   {
/* 2255 */     if (count < 2) {
/* 2256 */       return this.defaultEncoding;
/*      */     }
/*      */ 
/* 2260 */     int b0 = b4[0] & 0xFF;
/* 2261 */     int b1 = b4[1] & 0xFF;
/* 2262 */     if ((b0 == 254) && (b1 == 255))
/*      */     {
/* 2264 */       return new Object[] { "UTF-16BE", new Boolean(true) };
/*      */     }
/* 2266 */     if ((b0 == 255) && (b1 == 254))
/*      */     {
/* 2268 */       return new Object[] { "UTF-16LE", new Boolean(false) };
/*      */     }
/*      */ 
/* 2273 */     if (count < 3) {
/* 2274 */       return this.defaultEncoding;
/*      */     }
/*      */ 
/* 2278 */     int b2 = b4[2] & 0xFF;
/* 2279 */     if ((b0 == 239) && (b1 == 187) && (b2 == 191)) {
/* 2280 */       return this.defaultEncoding;
/*      */     }
/*      */ 
/* 2285 */     if (count < 4) {
/* 2286 */       return this.defaultEncoding;
/*      */     }
/*      */ 
/* 2290 */     int b3 = b4[3] & 0xFF;
/* 2291 */     if ((b0 == 0) && (b1 == 0) && (b2 == 0) && (b3 == 60))
/*      */     {
/* 2293 */       return new Object[] { "ISO-10646-UCS-4", new Boolean(true) };
/*      */     }
/* 2295 */     if ((b0 == 60) && (b1 == 0) && (b2 == 0) && (b3 == 0))
/*      */     {
/* 2297 */       return new Object[] { "ISO-10646-UCS-4", new Boolean(false) };
/*      */     }
/* 2299 */     if ((b0 == 0) && (b1 == 0) && (b2 == 60) && (b3 == 0))
/*      */     {
/* 2302 */       return new Object[] { "ISO-10646-UCS-4", null };
/*      */     }
/* 2304 */     if ((b0 == 0) && (b1 == 60) && (b2 == 0) && (b3 == 0))
/*      */     {
/* 2307 */       return new Object[] { "ISO-10646-UCS-4", null };
/*      */     }
/* 2309 */     if ((b0 == 0) && (b1 == 60) && (b2 == 0) && (b3 == 63))
/*      */     {
/* 2313 */       return new Object[] { "UTF-16BE", new Boolean(true) };
/*      */     }
/* 2315 */     if ((b0 == 60) && (b1 == 0) && (b2 == 63) && (b3 == 0))
/*      */     {
/* 2318 */       return new Object[] { "UTF-16LE", new Boolean(false) };
/*      */     }
/* 2320 */     if ((b0 == 76) && (b1 == 111) && (b2 == 167) && (b3 == 148))
/*      */     {
/* 2323 */       return new Object[] { "CP037", null };
/*      */     }
/*      */ 
/* 2326 */     return this.defaultEncoding;
/*      */   }
/*      */ 
/*      */   protected Reader createReader(InputStream inputStream, String encoding, Boolean isBigEndian)
/*      */     throws IOException
/*      */   {
/* 2350 */     if (encoding == null) {
/* 2351 */       encoding = "UTF-8";
/*      */     }
/*      */ 
/* 2355 */     String ENCODING = encoding.toUpperCase(Locale.ENGLISH);
/* 2356 */     if (ENCODING.equals("UTF-8"))
/*      */     {
/* 2360 */       return new UTF8Reader(inputStream, this.fBufferSize, this.fErrorReporter.getMessageFormatter("http://www.w3.org/TR/1998/REC-xml-19980210"), this.fErrorReporter.getLocale());
/*      */     }
/* 2362 */     if (ENCODING.equals("US-ASCII"))
/*      */     {
/* 2366 */       return new ASCIIReader(inputStream, this.fBufferSize, this.fErrorReporter.getMessageFormatter("http://www.w3.org/TR/1998/REC-xml-19980210"), this.fErrorReporter.getLocale());
/*      */     }
/* 2368 */     if (ENCODING.equals("ISO-10646-UCS-4")) {
/* 2369 */       if (isBigEndian != null) {
/* 2370 */         boolean isBE = isBigEndian.booleanValue();
/* 2371 */         if (isBE) {
/* 2372 */           return new UCSReader(inputStream, (short)8);
/*      */         }
/* 2374 */         return new UCSReader(inputStream, (short)4);
/*      */       }
/*      */ 
/* 2377 */       this.fErrorReporter.reportError(getEntityScanner(), "http://www.w3.org/TR/1998/REC-xml-19980210", "EncodingByteOrderUnsupported", new Object[] { encoding }, (short)2);
/*      */     }
/*      */ 
/* 2383 */     if (ENCODING.equals("ISO-10646-UCS-2")) {
/* 2384 */       if (isBigEndian != null) {
/* 2385 */         boolean isBE = isBigEndian.booleanValue();
/* 2386 */         if (isBE) {
/* 2387 */           return new UCSReader(inputStream, (short)2);
/*      */         }
/* 2389 */         return new UCSReader(inputStream, (short)1);
/*      */       }
/*      */ 
/* 2392 */       this.fErrorReporter.reportError(getEntityScanner(), "http://www.w3.org/TR/1998/REC-xml-19980210", "EncodingByteOrderUnsupported", new Object[] { encoding }, (short)2);
/*      */     }
/*      */ 
/* 2400 */     boolean validIANA = XMLChar.isValidIANAEncoding(encoding);
/* 2401 */     boolean validJava = XMLChar.isValidJavaEncoding(encoding);
/* 2402 */     if ((!validIANA) || ((this.fAllowJavaEncodings) && (!validJava))) {
/* 2403 */       this.fErrorReporter.reportError(getEntityScanner(), "http://www.w3.org/TR/1998/REC-xml-19980210", "EncodingDeclInvalid", new Object[] { encoding }, (short)2);
/*      */ 
/* 2415 */       encoding = "ISO-8859-1";
/*      */     }
/*      */ 
/* 2419 */     String javaEncoding = EncodingMap.getIANA2JavaMapping(ENCODING);
/* 2420 */     if (javaEncoding == null) {
/* 2421 */       if (this.fAllowJavaEncodings) {
/* 2422 */         javaEncoding = encoding;
/*      */       } else {
/* 2424 */         this.fErrorReporter.reportError(getEntityScanner(), "http://www.w3.org/TR/1998/REC-xml-19980210", "EncodingDeclInvalid", new Object[] { encoding }, (short)2);
/*      */ 
/* 2429 */         javaEncoding = "ISO8859_1";
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2439 */     return new BufferedReader(new InputStreamReader(inputStream, javaEncoding));
/*      */   }
/*      */ 
/*      */   public String getPublicId()
/*      */   {
/* 2455 */     return (this.fCurrentEntity != null) && (this.fCurrentEntity.entityLocation != null) ? this.fCurrentEntity.entityLocation.getPublicId() : null;
/*      */   }
/*      */ 
/*      */   public String getExpandedSystemId()
/*      */   {
/* 2472 */     if (this.fCurrentEntity != null) {
/* 2473 */       if ((this.fCurrentEntity.entityLocation != null) && (this.fCurrentEntity.entityLocation.getExpandedSystemId() != null))
/*      */       {
/* 2475 */         return this.fCurrentEntity.entityLocation.getExpandedSystemId();
/*      */       }
/*      */ 
/* 2478 */       int size = this.fEntityStack.size();
/* 2479 */       for (int i = size - 1; i >= 0; i--) {
/* 2480 */         Entity.ScannedEntity externalEntity = (Entity.ScannedEntity)this.fEntityStack.elementAt(i);
/*      */ 
/* 2483 */         if ((externalEntity.entityLocation != null) && (externalEntity.entityLocation.getExpandedSystemId() != null))
/*      */         {
/* 2485 */           return externalEntity.entityLocation.getExpandedSystemId();
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 2490 */     return null;
/*      */   }
/*      */ 
/*      */   public String getLiteralSystemId()
/*      */   {
/* 2504 */     if (this.fCurrentEntity != null) {
/* 2505 */       if ((this.fCurrentEntity.entityLocation != null) && (this.fCurrentEntity.entityLocation.getLiteralSystemId() != null))
/*      */       {
/* 2507 */         return this.fCurrentEntity.entityLocation.getLiteralSystemId();
/*      */       }
/*      */ 
/* 2510 */       int size = this.fEntityStack.size();
/* 2511 */       for (int i = size - 1; i >= 0; i--) {
/* 2512 */         Entity.ScannedEntity externalEntity = (Entity.ScannedEntity)this.fEntityStack.elementAt(i);
/*      */ 
/* 2515 */         if ((externalEntity.entityLocation != null) && (externalEntity.entityLocation.getLiteralSystemId() != null))
/*      */         {
/* 2517 */           return externalEntity.entityLocation.getLiteralSystemId();
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 2522 */     return null;
/*      */   }
/*      */ 
/*      */   public int getLineNumber()
/*      */   {
/* 2544 */     if (this.fCurrentEntity != null) {
/* 2545 */       if (this.fCurrentEntity.isExternal()) {
/* 2546 */         return this.fCurrentEntity.lineNumber;
/*      */       }
/*      */ 
/* 2549 */       int size = this.fEntityStack.size();
/* 2550 */       for (int i = size - 1; i > 0; i--) {
/* 2551 */         Entity.ScannedEntity firstExternalEntity = (Entity.ScannedEntity)this.fEntityStack.elementAt(i);
/* 2552 */         if (firstExternalEntity.isExternal()) {
/* 2553 */           return firstExternalEntity.lineNumber;
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2559 */     return -1;
/*      */   }
/*      */ 
/*      */   public int getColumnNumber()
/*      */   {
/* 2586 */     if (this.fCurrentEntity != null) {
/* 2587 */       if (this.fCurrentEntity.isExternal()) {
/* 2588 */         return this.fCurrentEntity.columnNumber;
/*      */       }
/*      */ 
/* 2591 */       int size = this.fEntityStack.size();
/* 2592 */       for (int i = size - 1; i > 0; i--) {
/* 2593 */         Entity.ScannedEntity firstExternalEntity = (Entity.ScannedEntity)this.fEntityStack.elementAt(i);
/* 2594 */         if (firstExternalEntity.isExternal()) {
/* 2595 */           return firstExternalEntity.columnNumber;
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2601 */     return -1;
/*      */   }
/*      */ 
/*      */   protected static String fixURI(String str)
/*      */   {
/* 2619 */     str = str.replace(File.separatorChar, '/');
/*      */ 
/* 2622 */     if (str.length() >= 2) {
/* 2623 */       char ch1 = str.charAt(1);
/*      */ 
/* 2625 */       if (ch1 == ':') {
/* 2626 */         char ch0 = Character.toUpperCase(str.charAt(0));
/* 2627 */         if ((ch0 >= 'A') && (ch0 <= 'Z')) {
/* 2628 */           str = "/" + str;
/*      */         }
/*      */ 
/*      */       }
/* 2632 */       else if ((ch1 == '/') && (str.charAt(0) == '/')) {
/* 2633 */         str = "file:" + str;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2641 */     int pos = str.indexOf(' ');
/* 2642 */     if (pos >= 0) {
/* 2643 */       StringBuilder sb = new StringBuilder(str.length());
/*      */ 
/* 2645 */       for (int i = 0; i < pos; i++) {
/* 2646 */         sb.append(str.charAt(i));
/*      */       }
/* 2648 */       sb.append("%20");
/*      */ 
/* 2650 */       for (int i = pos + 1; i < str.length(); i++) {
/* 2651 */         if (str.charAt(i) == ' ')
/* 2652 */           sb.append("%20");
/*      */         else
/* 2654 */           sb.append(str.charAt(i));
/*      */       }
/* 2656 */       str = sb.toString();
/*      */     }
/*      */ 
/* 2660 */     return str;
/*      */   }
/*      */ 
/*      */   final void print()
/*      */   {
/*      */   }
/*      */ 
/*      */   public void test()
/*      */   {
/* 3013 */     this.fEntityStorage.addExternalEntity("entityUsecase1", null, "/space/home/stax/sun/6thJan2004/zephyr/data/test.txt", "/space/home/stax/sun/6thJan2004/zephyr/data/entity.xml");
/*      */ 
/* 3018 */     this.fEntityStorage.addInternalEntity("entityUsecase2", "<Test>value</Test>");
/* 3019 */     this.fEntityStorage.addInternalEntity("entityUsecase3", "value3");
/* 3020 */     this.fEntityStorage.addInternalEntity("text", "Hello World.");
/* 3021 */     this.fEntityStorage.addInternalEntity("empty-element", "<foo/>");
/* 3022 */     this.fEntityStorage.addInternalEntity("balanced-element", "<foo></foo>");
/* 3023 */     this.fEntityStorage.addInternalEntity("balanced-element-with-text", "<foo>Hello, World</foo>");
/* 3024 */     this.fEntityStorage.addInternalEntity("balanced-element-with-entity", "<foo>&text;</foo>");
/* 3025 */     this.fEntityStorage.addInternalEntity("unbalanced-entity", "<foo>");
/* 3026 */     this.fEntityStorage.addInternalEntity("recursive-entity", "<foo>&recursive-entity2;</foo>");
/* 3027 */     this.fEntityStorage.addInternalEntity("recursive-entity2", "<bar>&recursive-entity3;</bar>");
/* 3028 */     this.fEntityStorage.addInternalEntity("recursive-entity3", "<baz>&recursive-entity;</baz>");
/* 3029 */     this.fEntityStorage.addInternalEntity("ch", "&#x00A9;");
/* 3030 */     this.fEntityStorage.addInternalEntity("ch1", "&#84;");
/* 3031 */     this.fEntityStorage.addInternalEntity("% ch2", "param");
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/* 1771 */     for (int i = 0; i <= 31; i++) {
/* 1772 */       gNeedEscaping[i] = true;
/* 1773 */       gAfterEscaping1[i] = gHexChs[(i >> 4)];
/* 1774 */       gAfterEscaping2[i] = gHexChs[(i & 0xF)];
/*      */     }
/* 1776 */     gNeedEscaping[127] = true;
/* 1777 */     gAfterEscaping1[127] = '7';
/* 1778 */     gAfterEscaping2[127] = 'F';
/* 1779 */     char[] escChs = { ' ', '<', '>', '#', '%', '"', '{', '}', '|', '\\', '^', '~', '[', ']', '`' };
/*      */ 
/* 1781 */     int len = escChs.length;
/*      */ 
/* 1783 */     for (int i = 0; i < len; i++) {
/* 1784 */       char ch = escChs[i];
/* 1785 */       gNeedEscaping[ch] = true;
/* 1786 */       gAfterEscaping1[ch] = gHexChs[(ch >> '\004')];
/* 1787 */       gAfterEscaping2[ch] = gHexChs[(ch & 0xF)];
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class CharacterBuffer
/*      */   {
/*      */     private char[] ch;
/*      */     private boolean isExternal;
/*      */ 
/*      */     public CharacterBuffer(boolean isExternal, int size)
/*      */     {
/* 2738 */       this.isExternal = isExternal;
/* 2739 */       this.ch = new char[size];
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class CharacterBufferPool
/*      */   {
/*      */     private static final int DEFAULT_POOL_SIZE = 3;
/*      */     private XMLEntityManager.CharacterBuffer[] fInternalBufferPool;
/*      */     private XMLEntityManager.CharacterBuffer[] fExternalBufferPool;
/*      */     private int fExternalBufferSize;
/*      */     private int fInternalBufferSize;
/*      */     private int poolSize;
/*      */     private int fInternalTop;
/*      */     private int fExternalTop;
/*      */ 
/*      */     public CharacterBufferPool(int externalBufferSize, int internalBufferSize)
/*      */     {
/* 2767 */       this(3, externalBufferSize, internalBufferSize);
/*      */     }
/*      */ 
/*      */     public CharacterBufferPool(int poolSize, int externalBufferSize, int internalBufferSize) {
/* 2771 */       this.fExternalBufferSize = externalBufferSize;
/* 2772 */       this.fInternalBufferSize = internalBufferSize;
/* 2773 */       this.poolSize = poolSize;
/* 2774 */       init();
/*      */     }
/*      */ 
/*      */     private void init()
/*      */     {
/* 2779 */       this.fInternalBufferPool = new XMLEntityManager.CharacterBuffer[this.poolSize];
/* 2780 */       this.fExternalBufferPool = new XMLEntityManager.CharacterBuffer[this.poolSize];
/* 2781 */       this.fInternalTop = -1;
/* 2782 */       this.fExternalTop = -1;
/*      */     }
/*      */ 
/*      */     public XMLEntityManager.CharacterBuffer getBuffer(boolean external)
/*      */     {
/* 2787 */       if (external) {
/* 2788 */         if (this.fExternalTop > -1) {
/* 2789 */           return this.fExternalBufferPool[(this.fExternalTop--)];
/*      */         }
/*      */ 
/* 2792 */         return new XMLEntityManager.CharacterBuffer(true, this.fExternalBufferSize);
/*      */       }
/*      */ 
/* 2796 */       if (this.fInternalTop > -1) {
/* 2797 */         return this.fInternalBufferPool[(this.fInternalTop--)];
/*      */       }
/*      */ 
/* 2800 */       return new XMLEntityManager.CharacterBuffer(false, this.fInternalBufferSize);
/*      */     }
/*      */ 
/*      */     public void returnToPool(XMLEntityManager.CharacterBuffer buffer)
/*      */     {
/* 2807 */       if (buffer.isExternal) {
/* 2808 */         if (this.fExternalTop < this.fExternalBufferPool.length - 1) {
/* 2809 */           this.fExternalBufferPool[(++this.fExternalTop)] = buffer;
/*      */         }
/*      */       }
/* 2812 */       else if (this.fInternalTop < this.fInternalBufferPool.length - 1)
/* 2813 */         this.fInternalBufferPool[(++this.fInternalTop)] = buffer;
/*      */     }
/*      */ 
/*      */     public void setExternalBufferSize(int bufferSize)
/*      */     {
/* 2819 */       this.fExternalBufferSize = bufferSize;
/* 2820 */       this.fExternalBufferPool = new XMLEntityManager.CharacterBuffer[this.poolSize];
/* 2821 */       this.fExternalTop = -1;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected final class RewindableInputStream extends InputStream
/*      */   {
/*      */     private InputStream fInputStream;
/*      */     private byte[] fData;
/*      */     private int fStartOffset;
/*      */     private int fEndOffset;
/*      */     private int fOffset;
/*      */     private int fLength;
/*      */     private int fMark;
/*      */ 
/*      */     public RewindableInputStream(InputStream is)
/*      */     {
/* 2859 */       this.fData = new byte[64];
/* 2860 */       this.fInputStream = is;
/* 2861 */       this.fStartOffset = 0;
/* 2862 */       this.fEndOffset = -1;
/* 2863 */       this.fOffset = 0;
/* 2864 */       this.fLength = 0;
/* 2865 */       this.fMark = 0;
/*      */     }
/*      */ 
/*      */     public void setStartOffset(int offset) {
/* 2869 */       this.fStartOffset = offset;
/*      */     }
/*      */ 
/*      */     public void rewind() {
/* 2873 */       this.fOffset = this.fStartOffset;
/*      */     }
/*      */ 
/*      */     public int read() throws IOException {
/* 2877 */       int b = 0;
/* 2878 */       if (this.fOffset < this.fLength) {
/* 2879 */         return this.fData[(this.fOffset++)] & 0xFF;
/*      */       }
/* 2881 */       if (this.fOffset == this.fEndOffset) {
/* 2882 */         return -1;
/*      */       }
/* 2884 */       if (this.fOffset == this.fData.length) {
/* 2885 */         byte[] newData = new byte[this.fOffset << 1];
/* 2886 */         System.arraycopy(this.fData, 0, newData, 0, this.fOffset);
/* 2887 */         this.fData = newData;
/*      */       }
/* 2889 */       b = this.fInputStream.read();
/* 2890 */       if (b == -1) {
/* 2891 */         this.fEndOffset = this.fOffset;
/* 2892 */         return -1;
/*      */       }
/* 2894 */       this.fData[(this.fLength++)] = ((byte)b);
/* 2895 */       this.fOffset += 1;
/* 2896 */       return b & 0xFF;
/*      */     }
/*      */ 
/*      */     public int read(byte[] b, int off, int len) throws IOException {
/* 2900 */       int bytesLeft = this.fLength - this.fOffset;
/* 2901 */       if (bytesLeft == 0) {
/* 2902 */         if (this.fOffset == this.fEndOffset) {
/* 2903 */           return -1;
/*      */         }
/*      */ 
/* 2911 */         if ((XMLEntityManager.this.fCurrentEntity.mayReadChunks) || (!XMLEntityManager.this.fCurrentEntity.xmlDeclChunkRead))
/*      */         {
/* 2913 */           if (!XMLEntityManager.this.fCurrentEntity.xmlDeclChunkRead)
/*      */           {
/* 2915 */             XMLEntityManager.this.fCurrentEntity.xmlDeclChunkRead = true;
/* 2916 */             len = 28;
/*      */           }
/* 2918 */           return this.fInputStream.read(b, off, len);
/*      */         }
/*      */ 
/* 2921 */         int returnedVal = read();
/* 2922 */         if (returnedVal == -1) {
/* 2923 */           this.fEndOffset = this.fOffset;
/* 2924 */           return -1;
/*      */         }
/* 2926 */         b[off] = ((byte)returnedVal);
/* 2927 */         return 1;
/*      */       }
/*      */ 
/* 2930 */       if (len < bytesLeft) {
/* 2931 */         if (len <= 0)
/* 2932 */           return 0;
/*      */       }
/*      */       else {
/* 2935 */         len = bytesLeft;
/*      */       }
/* 2937 */       if (b != null) {
/* 2938 */         System.arraycopy(this.fData, this.fOffset, b, off, len);
/*      */       }
/* 2940 */       this.fOffset += len;
/* 2941 */       return len;
/*      */     }
/*      */ 
/*      */     public long skip(long n)
/*      */       throws IOException
/*      */     {
/* 2947 */       if (n <= 0L) {
/* 2948 */         return 0L;
/*      */       }
/* 2950 */       int bytesLeft = this.fLength - this.fOffset;
/* 2951 */       if (bytesLeft == 0) {
/* 2952 */         if (this.fOffset == this.fEndOffset) {
/* 2953 */           return 0L;
/*      */         }
/* 2955 */         return this.fInputStream.skip(n);
/*      */       }
/* 2957 */       if (n <= bytesLeft) {
/* 2958 */         this.fOffset = ((int)(this.fOffset + n));
/* 2959 */         return n;
/*      */       }
/* 2961 */       this.fOffset += bytesLeft;
/* 2962 */       if (this.fOffset == this.fEndOffset) {
/* 2963 */         return bytesLeft;
/*      */       }
/* 2965 */       n -= bytesLeft;
/*      */ 
/* 2974 */       return this.fInputStream.skip(n) + bytesLeft;
/*      */     }
/*      */ 
/*      */     public int available() throws IOException {
/* 2978 */       int bytesLeft = this.fLength - this.fOffset;
/* 2979 */       if (bytesLeft == 0) {
/* 2980 */         if (this.fOffset == this.fEndOffset) {
/* 2981 */           return -1;
/*      */         }
/* 2983 */         return XMLEntityManager.this.fCurrentEntity.mayReadChunks ? this.fInputStream.available() : 0;
/*      */       }
/*      */ 
/* 2986 */       return bytesLeft;
/*      */     }
/*      */ 
/*      */     public void mark(int howMuch) {
/* 2990 */       this.fMark = this.fOffset;
/*      */     }
/*      */ 
/*      */     public void reset() {
/* 2994 */       this.fOffset = this.fMark;
/*      */     }
/*      */ 
/*      */     public boolean markSupported()
/*      */     {
/* 2999 */       return true;
/*      */     }
/*      */ 
/*      */     public void close() throws IOException {
/* 3003 */       if (this.fInputStream != null) {
/* 3004 */         this.fInputStream.close();
/* 3005 */         this.fInputStream = null;
/*      */       }
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.XMLEntityManager
 * JD-Core Version:    0.6.2
 */