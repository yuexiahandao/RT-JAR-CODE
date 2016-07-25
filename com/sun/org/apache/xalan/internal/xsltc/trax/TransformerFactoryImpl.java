/*      */ package com.sun.org.apache.xalan.internal.xsltc.trax;
/*      */ 
/*      */ import com.sun.org.apache.xalan.internal.XalanConstants;
/*      */ import com.sun.org.apache.xalan.internal.utils.FactoryImpl;
/*      */ import com.sun.org.apache.xalan.internal.utils.FeatureManager;
/*      */ import com.sun.org.apache.xalan.internal.utils.FeatureManager.Feature;
/*      */ import com.sun.org.apache.xalan.internal.utils.FeaturePropertyBase.State;
/*      */ import com.sun.org.apache.xalan.internal.utils.ObjectFactory;
/*      */ import com.sun.org.apache.xalan.internal.utils.SecuritySupport;
/*      */ import com.sun.org.apache.xalan.internal.utils.XMLSecurityManager;
/*      */ import com.sun.org.apache.xalan.internal.utils.XMLSecurityManager.State;
/*      */ import com.sun.org.apache.xalan.internal.utils.XMLSecurityPropertyManager;
/*      */ import com.sun.org.apache.xalan.internal.utils.XMLSecurityPropertyManager.Property;
/*      */ import com.sun.org.apache.xalan.internal.xsltc.compiler.Parser;
/*      */ import com.sun.org.apache.xalan.internal.xsltc.compiler.SourceLoader;
/*      */ import com.sun.org.apache.xalan.internal.xsltc.compiler.XSLTC;
/*      */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
/*      */ import com.sun.org.apache.xalan.internal.xsltc.dom.XSLTCDTMManager;
/*      */ import com.sun.org.apache.xml.internal.utils.StopParseException;
/*      */ import com.sun.org.apache.xml.internal.utils.StylesheetPIHandler;
/*      */ import java.io.File;
/*      */ import java.io.FileInputStream;
/*      */ import java.io.FileNotFoundException;
/*      */ import java.io.FilenameFilter;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.PrintStream;
/*      */ import java.net.MalformedURLException;
/*      */ import java.net.URL;
/*      */ import java.util.Enumeration;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Map;
/*      */ import java.util.Properties;
/*      */ import java.util.Vector;
/*      */ import java.util.zip.ZipEntry;
/*      */ import java.util.zip.ZipFile;
/*      */ import javax.xml.parsers.ParserConfigurationException;
/*      */ import javax.xml.parsers.SAXParser;
/*      */ import javax.xml.parsers.SAXParserFactory;
/*      */ import javax.xml.transform.ErrorListener;
/*      */ import javax.xml.transform.Source;
/*      */ import javax.xml.transform.Templates;
/*      */ import javax.xml.transform.Transformer;
/*      */ import javax.xml.transform.TransformerConfigurationException;
/*      */ import javax.xml.transform.TransformerException;
/*      */ import javax.xml.transform.TransformerFactory;
/*      */ import javax.xml.transform.URIResolver;
/*      */ import javax.xml.transform.dom.DOMSource;
/*      */ import javax.xml.transform.sax.SAXSource;
/*      */ import javax.xml.transform.sax.SAXTransformerFactory;
/*      */ import javax.xml.transform.sax.TemplatesHandler;
/*      */ import javax.xml.transform.sax.TransformerHandler;
/*      */ import org.w3c.dom.Node;
/*      */ import org.xml.sax.InputSource;
/*      */ import org.xml.sax.SAXException;
/*      */ import org.xml.sax.XMLFilter;
/*      */ import org.xml.sax.XMLReader;
/*      */ import org.xml.sax.helpers.XMLReaderFactory;
/*      */ 
/*      */ public class TransformerFactoryImpl extends SAXTransformerFactory
/*      */   implements SourceLoader, ErrorListener
/*      */ {
/*      */   public static final String TRANSLET_NAME = "translet-name";
/*      */   public static final String DESTINATION_DIRECTORY = "destination-directory";
/*      */   public static final String PACKAGE_NAME = "package-name";
/*      */   public static final String JAR_NAME = "jar-name";
/*      */   public static final String GENERATE_TRANSLET = "generate-translet";
/*      */   public static final String AUTO_TRANSLET = "auto-translet";
/*      */   public static final String USE_CLASSPATH = "use-classpath";
/*      */   public static final String DEBUG = "debug";
/*      */   public static final String ENABLE_INLINING = "enable-inlining";
/*      */   public static final String INDENT_NUMBER = "indent-number";
/*  109 */   private ErrorListener _errorListener = this;
/*      */ 
/*  114 */   private URIResolver _uriResolver = null;
/*      */   protected static final String DEFAULT_TRANSLET_NAME = "GregorSamsa";
/*  131 */   private String _transletName = "GregorSamsa";
/*      */ 
/*  136 */   private String _destinationDirectory = null;
/*      */ 
/*  141 */   private String _packageName = null;
/*      */ 
/*  146 */   private String _jarFileName = null;
/*      */ 
/*  152 */   private Hashtable _piParams = null;
/*      */ 
/*  172 */   private boolean _debug = false;
/*      */ 
/*  177 */   private boolean _enableInlining = false;
/*      */ 
/*  183 */   private boolean _generateTranslet = false;
/*      */ 
/*  191 */   private boolean _autoTranslet = false;
/*      */ 
/*  197 */   private boolean _useClasspath = false;
/*      */ 
/*  202 */   private int _indentNumber = -1;
/*      */   private Class m_DTMManagerClass;
/*  215 */   private boolean _isNotSecureProcessing = true;
/*      */ 
/*  219 */   private boolean _isSecureMode = false;
/*      */   private boolean _useServicesMechanism;
/*  231 */   private String _accessExternalStylesheet = "all";
/*      */ 
/*  235 */   private String _accessExternalDTD = "all";
/*      */   private XMLSecurityPropertyManager _xmlSecurityPropertyMgr;
/*      */   private XMLSecurityManager _xmlSecurityManager;
/*      */   private final FeatureManager _featureManager;
/*  242 */   private ClassLoader _extensionClassLoader = null;
/*      */   private Map<String, Class> _xsltcExtensionFunctions;
/*      */ 
/*      */   public TransformerFactoryImpl()
/*      */   {
/*  253 */     this(true);
/*      */   }
/*      */ 
/*      */   public static TransformerFactory newTransformerFactoryNoServiceLoader() {
/*  257 */     return new TransformerFactoryImpl(false);
/*      */   }
/*      */ 
/*      */   private TransformerFactoryImpl(boolean useServicesMechanism) {
/*  261 */     this.m_DTMManagerClass = XSLTCDTMManager.getDTMManagerClass(useServicesMechanism);
/*  262 */     this._useServicesMechanism = useServicesMechanism;
/*  263 */     this._featureManager = new FeatureManager();
/*      */ 
/*  265 */     if (System.getSecurityManager() != null) {
/*  266 */       this._isSecureMode = true;
/*  267 */       this._isNotSecureProcessing = false;
/*  268 */       this._featureManager.setValue(FeatureManager.Feature.ORACLE_ENABLE_EXTENSION_FUNCTION, FeaturePropertyBase.State.FSP, "false");
/*      */     }
/*      */ 
/*  272 */     this._xmlSecurityPropertyMgr = new XMLSecurityPropertyManager();
/*  273 */     this._accessExternalDTD = this._xmlSecurityPropertyMgr.getValue(XMLSecurityPropertyManager.Property.ACCESS_EXTERNAL_DTD);
/*      */ 
/*  275 */     this._accessExternalStylesheet = this._xmlSecurityPropertyMgr.getValue(XMLSecurityPropertyManager.Property.ACCESS_EXTERNAL_STYLESHEET);
/*      */ 
/*  279 */     this._xmlSecurityManager = new XMLSecurityManager(true);
/*      */ 
/*  281 */     this._xsltcExtensionFunctions = null;
/*      */   }
/*      */ 
/*      */   public Map<String, Class> getExternalExtensionsMap() {
/*  285 */     return this._xsltcExtensionFunctions;
/*      */   }
/*      */ 
/*      */   public void setErrorListener(ErrorListener listener)
/*      */     throws IllegalArgumentException
/*      */   {
/*  300 */     if (listener == null) {
/*  301 */       ErrorMsg err = new ErrorMsg("ERROR_LISTENER_NULL_ERR", "TransformerFactory");
/*      */ 
/*  303 */       throw new IllegalArgumentException(err.toString());
/*      */     }
/*  305 */     this._errorListener = listener;
/*      */   }
/*      */ 
/*      */   public ErrorListener getErrorListener()
/*      */   {
/*  315 */     return this._errorListener;
/*      */   }
/*      */ 
/*      */   public Object getAttribute(String name)
/*      */     throws IllegalArgumentException
/*      */   {
/*  330 */     if (name.equals("translet-name")) {
/*  331 */       return this._transletName;
/*      */     }
/*  333 */     if (name.equals("generate-translet")) {
/*  334 */       return new Boolean(this._generateTranslet);
/*      */     }
/*  336 */     if (name.equals("auto-translet")) {
/*  337 */       return new Boolean(this._autoTranslet);
/*      */     }
/*  339 */     if (name.equals("enable-inlining")) {
/*  340 */       if (this._enableInlining) {
/*  341 */         return Boolean.TRUE;
/*      */       }
/*  343 */       return Boolean.FALSE;
/*  344 */     }if (name.equals("http://apache.org/xml/properties/security-manager"))
/*  345 */       return this._xmlSecurityManager;
/*  346 */     if (name.equals("jdk.xml.transform.extensionClassLoader")) {
/*  347 */       return this._extensionClassLoader;
/*      */     }
/*      */ 
/*  351 */     String propertyValue = this._xmlSecurityManager != null ? this._xmlSecurityManager.getLimitAsString(name) : null;
/*      */ 
/*  353 */     if (propertyValue != null) {
/*  354 */       return propertyValue;
/*      */     }
/*  356 */     propertyValue = this._xmlSecurityPropertyMgr != null ? this._xmlSecurityPropertyMgr.getValue(name) : null;
/*      */ 
/*  358 */     if (propertyValue != null) {
/*  359 */       return propertyValue;
/*      */     }
/*      */ 
/*  364 */     ErrorMsg err = new ErrorMsg("JAXP_INVALID_ATTR_ERR", name);
/*  365 */     throw new IllegalArgumentException(err.toString());
/*      */   }
/*      */ 
/*      */   public void setAttribute(String name, Object value)
/*      */     throws IllegalArgumentException
/*      */   {
/*  381 */     if ((name.equals("translet-name")) && ((value instanceof String))) {
/*  382 */       this._transletName = ((String)value);
/*  383 */       return;
/*      */     }
/*  385 */     if ((name.equals("destination-directory")) && ((value instanceof String))) {
/*  386 */       this._destinationDirectory = ((String)value);
/*  387 */       return;
/*      */     }
/*  389 */     if ((name.equals("package-name")) && ((value instanceof String))) {
/*  390 */       this._packageName = ((String)value);
/*  391 */       return;
/*      */     }
/*  393 */     if ((name.equals("jar-name")) && ((value instanceof String))) {
/*  394 */       this._jarFileName = ((String)value);
/*  395 */       return;
/*      */     }
/*  397 */     if (name.equals("generate-translet")) {
/*  398 */       if ((value instanceof Boolean)) {
/*  399 */         this._generateTranslet = ((Boolean)value).booleanValue();
/*  400 */         return;
/*      */       }
/*  402 */       if ((value instanceof String)) {
/*  403 */         this._generateTranslet = ((String)value).equalsIgnoreCase("true");
/*      */       }
/*      */ 
/*      */     }
/*  407 */     else if (name.equals("auto-translet")) {
/*  408 */       if ((value instanceof Boolean)) {
/*  409 */         this._autoTranslet = ((Boolean)value).booleanValue();
/*  410 */         return;
/*      */       }
/*  412 */       if ((value instanceof String)) {
/*  413 */         this._autoTranslet = ((String)value).equalsIgnoreCase("true");
/*      */       }
/*      */ 
/*      */     }
/*  417 */     else if (name.equals("use-classpath")) {
/*  418 */       if ((value instanceof Boolean)) {
/*  419 */         this._useClasspath = ((Boolean)value).booleanValue();
/*  420 */         return;
/*      */       }
/*  422 */       if ((value instanceof String)) {
/*  423 */         this._useClasspath = ((String)value).equalsIgnoreCase("true");
/*      */       }
/*      */ 
/*      */     }
/*  427 */     else if (name.equals("debug")) {
/*  428 */       if ((value instanceof Boolean)) {
/*  429 */         this._debug = ((Boolean)value).booleanValue();
/*  430 */         return;
/*      */       }
/*  432 */       if ((value instanceof String)) {
/*  433 */         this._debug = ((String)value).equalsIgnoreCase("true");
/*      */       }
/*      */ 
/*      */     }
/*  437 */     else if (name.equals("enable-inlining")) {
/*  438 */       if ((value instanceof Boolean)) {
/*  439 */         this._enableInlining = ((Boolean)value).booleanValue();
/*  440 */         return;
/*      */       }
/*  442 */       if ((value instanceof String)) {
/*  443 */         this._enableInlining = ((String)value).equalsIgnoreCase("true");
/*      */       }
/*      */ 
/*      */     }
/*  447 */     else if (name.equals("indent-number")) {
/*  448 */       if ((value instanceof String)) {
/*      */         try {
/*  450 */           this._indentNumber = Integer.parseInt((String)value);
/*  451 */           return;
/*      */         }
/*      */         catch (NumberFormatException e)
/*      */         {
/*      */         }
/*      */       }
/*  457 */       else if ((value instanceof Integer)) {
/*  458 */         this._indentNumber = ((Integer)value).intValue();
/*      */       }
/*      */ 
/*      */     }
/*  462 */     else if (name.equals("jdk.xml.transform.extensionClassLoader")) {
/*  463 */       if ((value instanceof ClassLoader)) {
/*  464 */         this._extensionClassLoader = ((ClassLoader)value);
/*  465 */         return;
/*      */       }
/*  467 */       ErrorMsg err = new ErrorMsg("JAXP_INVALID_ATTR_VALUE_ERR", "Extension Functions ClassLoader");
/*      */ 
/*  469 */       throw new IllegalArgumentException(err.toString());
/*      */     }
/*      */ 
/*  473 */     if ((this._xmlSecurityManager != null) && (this._xmlSecurityManager.setLimit(name, XMLSecurityManager.State.APIPROPERTY, value)))
/*      */     {
/*  475 */       return;
/*      */     }
/*      */ 
/*  478 */     if ((this._xmlSecurityPropertyMgr != null) && (this._xmlSecurityPropertyMgr.setValue(name, FeaturePropertyBase.State.APIPROPERTY, value)))
/*      */     {
/*  480 */       this._accessExternalDTD = this._xmlSecurityPropertyMgr.getValue(XMLSecurityPropertyManager.Property.ACCESS_EXTERNAL_DTD);
/*      */ 
/*  482 */       this._accessExternalStylesheet = this._xmlSecurityPropertyMgr.getValue(XMLSecurityPropertyManager.Property.ACCESS_EXTERNAL_STYLESHEET);
/*      */ 
/*  484 */       return;
/*      */     }
/*      */ 
/*  488 */     ErrorMsg err = new ErrorMsg("JAXP_INVALID_ATTR_ERR", name);
/*      */ 
/*  490 */     throw new IllegalArgumentException(err.toString());
/*      */   }
/*      */ 
/*      */   public void setFeature(String name, boolean value)
/*      */     throws TransformerConfigurationException
/*      */   {
/*  518 */     if (name == null) {
/*  519 */       ErrorMsg err = new ErrorMsg("JAXP_SET_FEATURE_NULL_NAME");
/*  520 */       throw new NullPointerException(err.toString());
/*      */     }
/*      */ 
/*  523 */     if (name.equals("http://javax.xml.XMLConstants/feature/secure-processing")) {
/*  524 */       if ((this._isSecureMode) && (!value)) {
/*  525 */         ErrorMsg err = new ErrorMsg("JAXP_SECUREPROCESSING_FEATURE");
/*  526 */         throw new TransformerConfigurationException(err.toString());
/*      */       }
/*  528 */       this._isNotSecureProcessing = (!value);
/*  529 */       this._xmlSecurityManager.setSecureProcessing(value);
/*      */ 
/*  532 */       if ((value) && (XalanConstants.IS_JDK8_OR_ABOVE)) {
/*  533 */         this._xmlSecurityPropertyMgr.setValue(XMLSecurityPropertyManager.Property.ACCESS_EXTERNAL_DTD, FeaturePropertyBase.State.FSP, "");
/*      */ 
/*  535 */         this._xmlSecurityPropertyMgr.setValue(XMLSecurityPropertyManager.Property.ACCESS_EXTERNAL_STYLESHEET, FeaturePropertyBase.State.FSP, "");
/*      */ 
/*  537 */         this._accessExternalDTD = this._xmlSecurityPropertyMgr.getValue(XMLSecurityPropertyManager.Property.ACCESS_EXTERNAL_DTD);
/*      */ 
/*  539 */         this._accessExternalStylesheet = this._xmlSecurityPropertyMgr.getValue(XMLSecurityPropertyManager.Property.ACCESS_EXTERNAL_STYLESHEET);
/*      */       }
/*      */ 
/*  543 */       if ((value) && (this._featureManager != null)) {
/*  544 */         this._featureManager.setValue(FeatureManager.Feature.ORACLE_ENABLE_EXTENSION_FUNCTION, FeaturePropertyBase.State.FSP, "false");
/*      */       }
/*      */ 
/*  547 */       return;
/*      */     }
/*  549 */     if (name.equals("http://www.oracle.com/feature/use-service-mechanism"))
/*      */     {
/*  551 */       if (!this._isSecureMode)
/*  552 */         this._useServicesMechanism = value;
/*      */     }
/*      */     else {
/*  555 */       if ((this._featureManager != null) && (this._featureManager.setValue(name, FeaturePropertyBase.State.APIPROPERTY, value)))
/*      */       {
/*  557 */         return;
/*      */       }
/*      */ 
/*  561 */       ErrorMsg err = new ErrorMsg("JAXP_UNSUPPORTED_FEATURE", name);
/*  562 */       throw new TransformerConfigurationException(err.toString());
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean getFeature(String name)
/*      */   {
/*  577 */     String[] features = { "http://javax.xml.transform.dom.DOMSource/feature", "http://javax.xml.transform.dom.DOMResult/feature", "http://javax.xml.transform.sax.SAXSource/feature", "http://javax.xml.transform.sax.SAXResult/feature", "http://javax.xml.transform.stax.StAXSource/feature", "http://javax.xml.transform.stax.StAXResult/feature", "http://javax.xml.transform.stream.StreamSource/feature", "http://javax.xml.transform.stream.StreamResult/feature", "http://javax.xml.transform.sax.SAXTransformerFactory/feature", "http://javax.xml.transform.sax.SAXTransformerFactory/feature/xmlfilter", "http://www.oracle.com/feature/use-service-mechanism" };
/*      */ 
/*  592 */     if (name == null) {
/*  593 */       ErrorMsg err = new ErrorMsg("JAXP_GET_FEATURE_NULL_NAME");
/*  594 */       throw new NullPointerException(err.toString());
/*      */     }
/*      */ 
/*  598 */     for (int i = 0; i < features.length; i++) {
/*  599 */       if (name.equals(features[i])) {
/*  600 */         return true;
/*      */       }
/*      */     }
/*      */ 
/*  604 */     if (name.equals("http://javax.xml.XMLConstants/feature/secure-processing")) {
/*  605 */       return !this._isNotSecureProcessing;
/*      */     }
/*      */ 
/*  609 */     String propertyValue = this._featureManager != null ? this._featureManager.getValueAsString(name) : null;
/*      */ 
/*  611 */     if (propertyValue != null) {
/*  612 */       return Boolean.parseBoolean(propertyValue);
/*      */     }
/*      */ 
/*  616 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean useServicesMechnism()
/*      */   {
/*  622 */     return this._useServicesMechanism;
/*      */   }
/*      */ 
/*      */   public FeatureManager getFeatureManager()
/*      */   {
/*  629 */     return this._featureManager;
/*      */   }
/*      */ 
/*      */   public URIResolver getURIResolver()
/*      */   {
/*  641 */     return this._uriResolver;
/*      */   }
/*      */ 
/*      */   public void setURIResolver(URIResolver resolver)
/*      */   {
/*  655 */     this._uriResolver = resolver;
/*      */   }
/*      */ 
/*      */   public Source getAssociatedStylesheet(Source source, String media, String title, String charset)
/*      */     throws TransformerConfigurationException
/*      */   {
/*  678 */     XMLReader reader = null;
/*  679 */     InputSource isource = null;
/*      */ 
/*  685 */     StylesheetPIHandler _stylesheetPIHandler = new StylesheetPIHandler(null, media, title, charset);
/*      */     try
/*      */     {
/*  689 */       if ((source instanceof DOMSource)) {
/*  690 */         DOMSource domsrc = (DOMSource)source;
/*  691 */         String baseId = domsrc.getSystemId();
/*  692 */         Node node = domsrc.getNode();
/*  693 */         DOM2SAX dom2sax = new DOM2SAX(node);
/*      */ 
/*  695 */         _stylesheetPIHandler.setBaseId(baseId);
/*      */ 
/*  697 */         dom2sax.setContentHandler(_stylesheetPIHandler);
/*  698 */         dom2sax.parse();
/*      */       } else {
/*  700 */         isource = SAXSource.sourceToInputSource(source);
/*  701 */         String baseId = isource.getSystemId();
/*      */ 
/*  703 */         SAXParserFactory factory = FactoryImpl.getSAXFactory(this._useServicesMechanism);
/*  704 */         factory.setNamespaceAware(true);
/*      */ 
/*  706 */         if (!this._isNotSecureProcessing)
/*      */           try {
/*  708 */             factory.setFeature("http://javax.xml.XMLConstants/feature/secure-processing", true);
/*      */           }
/*      */           catch (SAXException e)
/*      */           {
/*      */           }
/*  713 */         SAXParser jaxpParser = factory.newSAXParser();
/*      */ 
/*  715 */         reader = jaxpParser.getXMLReader();
/*  716 */         if (reader == null) {
/*  717 */           reader = XMLReaderFactory.createXMLReader();
/*      */         }
/*      */ 
/*  720 */         _stylesheetPIHandler.setBaseId(baseId);
/*  721 */         reader.setContentHandler(_stylesheetPIHandler);
/*  722 */         reader.parse(isource);
/*      */       }
/*      */ 
/*  726 */       if (this._uriResolver != null) {
/*  727 */         _stylesheetPIHandler.setURIResolver(this._uriResolver);
/*      */       }
/*      */     }
/*      */     catch (StopParseException e)
/*      */     {
/*      */     }
/*      */     catch (ParserConfigurationException e)
/*      */     {
/*  735 */       throw new TransformerConfigurationException("getAssociatedStylesheets failed", e);
/*      */     }
/*      */     catch (SAXException se)
/*      */     {
/*  740 */       throw new TransformerConfigurationException("getAssociatedStylesheets failed", se);
/*      */     }
/*      */     catch (IOException ioe)
/*      */     {
/*  745 */       throw new TransformerConfigurationException("getAssociatedStylesheets failed", ioe);
/*      */     }
/*      */ 
/*  750 */     return _stylesheetPIHandler.getAssociatedStylesheet();
/*      */   }
/*      */ 
/*      */   public Transformer newTransformer()
/*      */     throws TransformerConfigurationException
/*      */   {
/*  764 */     TransformerImpl result = new TransformerImpl(new Properties(), this._indentNumber, this);
/*      */ 
/*  766 */     if (this._uriResolver != null) {
/*  767 */       result.setURIResolver(this._uriResolver);
/*      */     }
/*      */ 
/*  770 */     if (!this._isNotSecureProcessing) {
/*  771 */       result.setSecureProcessing(true);
/*      */     }
/*  773 */     return result;
/*      */   }
/*      */ 
/*      */   public Transformer newTransformer(Source source)
/*      */     throws TransformerConfigurationException
/*      */   {
/*  789 */     Templates templates = newTemplates(source);
/*  790 */     Transformer transformer = templates.newTransformer();
/*  791 */     if (this._uriResolver != null) {
/*  792 */       transformer.setURIResolver(this._uriResolver);
/*      */     }
/*  794 */     return transformer;
/*      */   }
/*      */ 
/*      */   private void passWarningsToListener(Vector messages)
/*      */     throws TransformerException
/*      */   {
/*  803 */     if ((this._errorListener == null) || (messages == null)) {
/*  804 */       return;
/*      */     }
/*      */ 
/*  807 */     int count = messages.size();
/*  808 */     for (int pos = 0; pos < count; pos++) {
/*  809 */       ErrorMsg msg = (ErrorMsg)messages.elementAt(pos);
/*      */ 
/*  811 */       if (msg.isWarningError()) {
/*  812 */         this._errorListener.error(new TransformerConfigurationException(msg.toString()));
/*      */       }
/*      */       else
/*  815 */         this._errorListener.warning(new TransformerConfigurationException(msg.toString()));
/*      */     }
/*      */   }
/*      */ 
/*      */   private void passErrorsToListener(Vector messages)
/*      */   {
/*      */     try
/*      */     {
/*  825 */       if ((this._errorListener == null) || (messages == null)) {
/*  826 */         return;
/*      */       }
/*      */ 
/*  829 */       int count = messages.size();
/*  830 */       for (int pos = 0; pos < count; pos++) {
/*  831 */         String message = messages.elementAt(pos).toString();
/*  832 */         this._errorListener.error(new TransformerException(message));
/*      */       }
/*      */     }
/*      */     catch (TransformerException e)
/*      */     {
/*      */     }
/*      */   }
/*      */ 
/*      */   public Templates newTemplates(Source source)
/*      */     throws TransformerConfigurationException
/*      */   {
/*  855 */     if (this._useClasspath) {
/*  856 */       String transletName = getTransletBaseName(source);
/*      */ 
/*  858 */       if (this._packageName != null)
/*  859 */         transletName = this._packageName + "." + transletName;
/*      */       try
/*      */       {
/*  862 */         Class clazz = ObjectFactory.findProviderClass(transletName, true);
/*  863 */         resetTransientAttributes();
/*      */ 
/*  865 */         return new TemplatesImpl(new Class[] { clazz }, transletName, null, this._indentNumber, this);
/*      */       }
/*      */       catch (ClassNotFoundException cnfe) {
/*  868 */         ErrorMsg err = new ErrorMsg("CLASS_NOT_FOUND_ERR", transletName);
/*  869 */         throw new TransformerConfigurationException(err.toString());
/*      */       }
/*      */       catch (Exception e) {
/*  872 */         ErrorMsg err = new ErrorMsg(new ErrorMsg("RUNTIME_ERROR_KEY") + e.getMessage());
/*      */ 
/*  875 */         throw new TransformerConfigurationException(err.toString());
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  881 */     if (this._autoTranslet) {
/*  882 */       byte[][] bytecodes = (byte[][])null;
/*  883 */       String transletClassName = getTransletBaseName(source);
/*      */ 
/*  885 */       if (this._packageName != null) {
/*  886 */         transletClassName = this._packageName + "." + transletClassName;
/*      */       }
/*  888 */       if (this._jarFileName != null)
/*  889 */         bytecodes = getBytecodesFromJar(source, transletClassName);
/*      */       else {
/*  891 */         bytecodes = getBytecodesFromClasses(source, transletClassName);
/*      */       }
/*  893 */       if (bytecodes != null) {
/*  894 */         if (this._debug) {
/*  895 */           if (this._jarFileName != null) {
/*  896 */             System.err.println(new ErrorMsg("TRANSFORM_WITH_JAR_STR", transletClassName, this._jarFileName));
/*      */           }
/*      */           else {
/*  899 */             System.err.println(new ErrorMsg("TRANSFORM_WITH_TRANSLET_STR", transletClassName));
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*  905 */         resetTransientAttributes();
/*  906 */         return new TemplatesImpl(bytecodes, transletClassName, null, this._indentNumber, this);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  911 */     XSLTC xsltc = new XSLTC(this._useServicesMechanism, this._featureManager);
/*  912 */     if (this._debug) xsltc.setDebug(true);
/*  913 */     if (this._enableInlining)
/*  914 */       xsltc.setTemplateInlining(true);
/*      */     else {
/*  916 */       xsltc.setTemplateInlining(false);
/*      */     }
/*  918 */     if (!this._isNotSecureProcessing) xsltc.setSecureProcessing(true);
/*  919 */     xsltc.setProperty("http://javax.xml.XMLConstants/property/accessExternalStylesheet", this._accessExternalStylesheet);
/*  920 */     xsltc.setProperty("http://javax.xml.XMLConstants/property/accessExternalDTD", this._accessExternalDTD);
/*  921 */     xsltc.setProperty("http://apache.org/xml/properties/security-manager", this._xmlSecurityManager);
/*  922 */     xsltc.setProperty("jdk.xml.transform.extensionClassLoader", this._extensionClassLoader);
/*  923 */     xsltc.init();
/*  924 */     if (!this._isNotSecureProcessing) {
/*  925 */       this._xsltcExtensionFunctions = xsltc.getExternalExtensionFunctions();
/*      */     }
/*  927 */     if (this._uriResolver != null) {
/*  928 */       xsltc.setSourceLoader(this);
/*      */     }
/*      */ 
/*  933 */     if ((this._piParams != null) && (this._piParams.get(source) != null))
/*      */     {
/*  935 */       PIParamWrapper p = (PIParamWrapper)this._piParams.get(source);
/*      */ 
/*  937 */       if (p != null) {
/*  938 */         xsltc.setPIParameters(p._media, p._title, p._charset);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  943 */     int outputType = 2;
/*  944 */     if ((this._generateTranslet) || (this._autoTranslet))
/*      */     {
/*  946 */       xsltc.setClassName(getTransletBaseName(source));
/*      */ 
/*  948 */       if (this._destinationDirectory != null) {
/*  949 */         xsltc.setDestDirectory(this._destinationDirectory);
/*      */       } else {
/*  951 */         String xslName = getStylesheetFileName(source);
/*  952 */         if (xslName != null) {
/*  953 */           File xslFile = new File(xslName);
/*  954 */           String xslDir = xslFile.getParent();
/*      */ 
/*  956 */           if (xslDir != null) {
/*  957 */             xsltc.setDestDirectory(xslDir);
/*      */           }
/*      */         }
/*      */       }
/*  961 */       if (this._packageName != null) {
/*  962 */         xsltc.setPackageName(this._packageName);
/*      */       }
/*  964 */       if (this._jarFileName != null) {
/*  965 */         xsltc.setJarFileName(this._jarFileName);
/*  966 */         outputType = 5;
/*      */       }
/*      */       else {
/*  969 */         outputType = 4;
/*      */       }
/*      */     }
/*      */ 
/*  973 */     InputSource input = Util.getInputSource(xsltc, source);
/*  974 */     byte[][] bytecodes = xsltc.compile(null, input, outputType);
/*  975 */     String transletName = xsltc.getClassName();
/*      */ 
/*  978 */     if (((this._generateTranslet) || (this._autoTranslet)) && (bytecodes != null) && (this._jarFileName != null)) {
/*      */       try
/*      */       {
/*  981 */         xsltc.outputToJar();
/*      */       }
/*      */       catch (IOException e)
/*      */       {
/*      */       }
/*      */     }
/*      */ 
/*  988 */     resetTransientAttributes();
/*      */ 
/*  991 */     if (this._errorListener != this) {
/*      */       try {
/*  993 */         passWarningsToListener(xsltc.getWarnings());
/*      */       }
/*      */       catch (TransformerException e) {
/*  996 */         throw new TransformerConfigurationException(e);
/*      */       }
/*      */     }
/*      */     else {
/* 1000 */       xsltc.printWarnings();
/*      */     }
/*      */ 
/* 1004 */     if (bytecodes == null) {
/* 1005 */       Vector errs = xsltc.getErrors();
/* 1006 */       ErrorMsg err = null;
/* 1007 */       if (errs != null)
/* 1008 */         err = (ErrorMsg)errs.elementAt(errs.size() - 1);
/*      */       else {
/* 1010 */         err = new ErrorMsg("JAXP_COMPILE_ERR");
/*      */       }
/* 1012 */       Throwable cause = err.getCause();
/*      */       TransformerConfigurationException exc;
/*      */       TransformerConfigurationException exc;
/* 1014 */       if (cause != null)
/* 1015 */         exc = new TransformerConfigurationException(cause.getMessage(), cause);
/*      */       else {
/* 1017 */         exc = new TransformerConfigurationException(err.toString());
/*      */       }
/*      */ 
/* 1021 */       if (this._errorListener != null) {
/* 1022 */         passErrorsToListener(xsltc.getErrors());
/*      */         try
/*      */         {
/* 1028 */           this._errorListener.fatalError(exc);
/*      */         }
/*      */         catch (TransformerException te) {
/*      */         }
/*      */       }
/*      */       else {
/* 1034 */         xsltc.printErrors();
/*      */       }
/* 1036 */       throw exc;
/*      */     }
/*      */ 
/* 1039 */     return new TemplatesImpl(bytecodes, transletName, xsltc.getOutputProperties(), this._indentNumber, this);
/*      */   }
/*      */ 
/*      */   public TemplatesHandler newTemplatesHandler()
/*      */     throws TransformerConfigurationException
/*      */   {
/* 1054 */     TemplatesHandlerImpl handler = new TemplatesHandlerImpl(this._indentNumber, this);
/*      */ 
/* 1056 */     if (this._uriResolver != null) {
/* 1057 */       handler.setURIResolver(this._uriResolver);
/*      */     }
/* 1059 */     return handler;
/*      */   }
/*      */ 
/*      */   public TransformerHandler newTransformerHandler()
/*      */     throws TransformerConfigurationException
/*      */   {
/* 1073 */     Transformer transformer = newTransformer();
/* 1074 */     if (this._uriResolver != null) {
/* 1075 */       transformer.setURIResolver(this._uriResolver);
/*      */     }
/* 1077 */     return new TransformerHandlerImpl((TransformerImpl)transformer);
/*      */   }
/*      */ 
/*      */   public TransformerHandler newTransformerHandler(Source src)
/*      */     throws TransformerConfigurationException
/*      */   {
/* 1093 */     Transformer transformer = newTransformer(src);
/* 1094 */     if (this._uriResolver != null) {
/* 1095 */       transformer.setURIResolver(this._uriResolver);
/*      */     }
/* 1097 */     return new TransformerHandlerImpl((TransformerImpl)transformer);
/*      */   }
/*      */ 
/*      */   public TransformerHandler newTransformerHandler(Templates templates)
/*      */     throws TransformerConfigurationException
/*      */   {
/* 1113 */     Transformer transformer = templates.newTransformer();
/* 1114 */     TransformerImpl internal = (TransformerImpl)transformer;
/* 1115 */     return new TransformerHandlerImpl(internal);
/*      */   }
/*      */ 
/*      */   public XMLFilter newXMLFilter(Source src)
/*      */     throws TransformerConfigurationException
/*      */   {
/* 1130 */     Templates templates = newTemplates(src);
/* 1131 */     if (templates == null) return null;
/* 1132 */     return newXMLFilter(templates);
/*      */   }
/*      */ 
/*      */   public XMLFilter newXMLFilter(Templates templates)
/*      */     throws TransformerConfigurationException
/*      */   {
/*      */     try
/*      */     {
/* 1148 */       return new TrAXFilter(templates);
/*      */     }
/*      */     catch (TransformerConfigurationException e1) {
/* 1151 */       if (this._errorListener != null) {
/*      */         try {
/* 1153 */           this._errorListener.fatalError(e1);
/* 1154 */           return null;
/*      */         }
/*      */         catch (TransformerException e2) {
/* 1157 */           new TransformerConfigurationException(e2);
/*      */         }
/*      */       }
/* 1160 */       throw e1;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void error(TransformerException e)
/*      */     throws TransformerException
/*      */   {
/* 1178 */     Throwable wrapped = e.getException();
/* 1179 */     if (wrapped != null) {
/* 1180 */       System.err.println(new ErrorMsg("ERROR_PLUS_WRAPPED_MSG", e.getMessageAndLocation(), wrapped.getMessage()));
/*      */     }
/*      */     else
/*      */     {
/* 1184 */       System.err.println(new ErrorMsg("ERROR_MSG", e.getMessageAndLocation()));
/*      */     }
/*      */ 
/* 1187 */     throw e;
/*      */   }
/*      */ 
/*      */   public void fatalError(TransformerException e)
/*      */     throws TransformerException
/*      */   {
/* 1206 */     Throwable wrapped = e.getException();
/* 1207 */     if (wrapped != null) {
/* 1208 */       System.err.println(new ErrorMsg("FATAL_ERR_PLUS_WRAPPED_MSG", e.getMessageAndLocation(), wrapped.getMessage()));
/*      */     }
/*      */     else
/*      */     {
/* 1212 */       System.err.println(new ErrorMsg("FATAL_ERR_MSG", e.getMessageAndLocation()));
/*      */     }
/*      */ 
/* 1215 */     throw e;
/*      */   }
/*      */ 
/*      */   public void warning(TransformerException e)
/*      */     throws TransformerException
/*      */   {
/* 1234 */     Throwable wrapped = e.getException();
/* 1235 */     if (wrapped != null) {
/* 1236 */       System.err.println(new ErrorMsg("WARNING_PLUS_WRAPPED_MSG", e.getMessageAndLocation(), wrapped.getMessage()));
/*      */     }
/*      */     else
/*      */     {
/* 1240 */       System.err.println(new ErrorMsg("WARNING_MSG", e.getMessageAndLocation()));
/*      */     }
/*      */   }
/*      */ 
/*      */   public InputSource loadSource(String href, String context, XSLTC xsltc)
/*      */   {
/*      */     try
/*      */     {
/* 1256 */       if (this._uriResolver != null) {
/* 1257 */         Source source = this._uriResolver.resolve(href, context);
/* 1258 */         if (source != null) {
/* 1259 */           return Util.getInputSource(xsltc, source);
/*      */         }
/*      */       }
/*      */     }
/*      */     catch (TransformerException e)
/*      */     {
/* 1265 */       ErrorMsg msg = new ErrorMsg("INVALID_URI_ERR", href + "\n" + e.getMessage(), this);
/* 1266 */       xsltc.getParser().reportError(2, msg);
/*      */     }
/*      */ 
/* 1269 */     return null;
/*      */   }
/*      */ 
/*      */   private void resetTransientAttributes()
/*      */   {
/* 1276 */     this._transletName = "GregorSamsa";
/* 1277 */     this._destinationDirectory = null;
/* 1278 */     this._packageName = null;
/* 1279 */     this._jarFileName = null;
/*      */   }
/*      */ 
/*      */   private byte[][] getBytecodesFromClasses(Source source, String fullClassName)
/*      */   {
/* 1292 */     if (fullClassName == null) {
/* 1293 */       return (byte[][])null;
/*      */     }
/* 1295 */     String xslFileName = getStylesheetFileName(source);
/* 1296 */     File xslFile = null;
/* 1297 */     if (xslFileName != null) {
/* 1298 */       xslFile = new File(xslFileName);
/*      */     }
/*      */ 
/* 1302 */     int lastDotIndex = fullClassName.lastIndexOf('.');
/*      */     String transletName;
/*      */     String transletName;
/* 1303 */     if (lastDotIndex > 0)
/* 1304 */       transletName = fullClassName.substring(lastDotIndex + 1);
/*      */     else {
/* 1306 */       transletName = fullClassName;
/*      */     }
/*      */ 
/* 1309 */     String transletPath = fullClassName.replace('.', '/');
/* 1310 */     if (this._destinationDirectory != null) {
/* 1311 */       transletPath = this._destinationDirectory + "/" + transletPath + ".class";
/*      */     }
/* 1314 */     else if ((xslFile != null) && (xslFile.getParent() != null))
/* 1315 */       transletPath = xslFile.getParent() + "/" + transletPath + ".class";
/*      */     else {
/* 1317 */       transletPath = transletPath + ".class";
/*      */     }
/*      */ 
/* 1321 */     File transletFile = new File(transletPath);
/* 1322 */     if (!transletFile.exists()) {
/* 1323 */       return (byte[][])null;
/*      */     }
/*      */ 
/* 1329 */     if ((xslFile != null) && (xslFile.exists())) {
/* 1330 */       long xslTimestamp = xslFile.lastModified();
/* 1331 */       long transletTimestamp = transletFile.lastModified();
/* 1332 */       if (transletTimestamp < xslTimestamp) {
/* 1333 */         return (byte[][])null;
/*      */       }
/*      */     }
/*      */ 
/* 1337 */     Vector bytecodes = new Vector();
/* 1338 */     int fileLength = (int)transletFile.length();
/* 1339 */     if (fileLength > 0) {
/* 1340 */       FileInputStream input = null;
/*      */       try {
/* 1342 */         input = new FileInputStream(transletFile);
/*      */       }
/*      */       catch (FileNotFoundException e) {
/* 1345 */         return (byte[][])null;
/*      */       }
/*      */ 
/* 1348 */       byte[] bytes = new byte[fileLength];
/*      */       try {
/* 1350 */         readFromInputStream(bytes, input, fileLength);
/* 1351 */         input.close();
/*      */       }
/*      */       catch (IOException e) {
/* 1354 */         return (byte[][])null;
/*      */       }
/*      */ 
/* 1357 */       bytecodes.addElement(bytes);
/*      */     }
/*      */     else {
/* 1360 */       return (byte[][])null;
/*      */     }
/*      */ 
/* 1363 */     String transletParentDir = transletFile.getParent();
/* 1364 */     if (transletParentDir == null) {
/* 1365 */       transletParentDir = SecuritySupport.getSystemProperty("user.dir");
/*      */     }
/* 1367 */     File transletParentFile = new File(transletParentDir);
/*      */ 
/* 1370 */     final String transletAuxPrefix = transletName + "$";
/* 1371 */     File[] auxfiles = transletParentFile.listFiles(new FilenameFilter()
/*      */     {
/*      */       public boolean accept(File dir, String name) {
/* 1374 */         return (name.endsWith(".class")) && (name.startsWith(transletAuxPrefix));
/*      */       }
/*      */     });
/* 1379 */     for (int i = 0; i < auxfiles.length; i++)
/*      */     {
/* 1381 */       File auxfile = auxfiles[i];
/* 1382 */       int auxlength = (int)auxfile.length();
/* 1383 */       if (auxlength > 0) {
/* 1384 */         FileInputStream auxinput = null;
/*      */         try {
/* 1386 */           auxinput = new FileInputStream(auxfile);
/*      */         }
/*      */         catch (FileNotFoundException e) {
/* 1389 */           continue;
/*      */         }
/*      */ 
/* 1392 */         byte[] bytes = new byte[auxlength];
/*      */         try
/*      */         {
/* 1395 */           readFromInputStream(bytes, auxinput, auxlength);
/* 1396 */           auxinput.close();
/*      */         }
/*      */         catch (IOException e) {
/* 1399 */           continue;
/*      */         }
/*      */ 
/* 1402 */         bytecodes.addElement(bytes);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1407 */     int count = bytecodes.size();
/* 1408 */     if (count > 0) {
/* 1409 */       byte[][] result = new byte[count][1];
/* 1410 */       for (int i = 0; i < count; i++) {
/* 1411 */         result[i] = ((byte[])(byte[])bytecodes.elementAt(i));
/*      */       }
/*      */ 
/* 1414 */       return result;
/*      */     }
/*      */ 
/* 1417 */     return (byte[][])null;
/*      */   }
/*      */ 
/*      */   private byte[][] getBytecodesFromJar(Source source, String fullClassName)
/*      */   {
/* 1429 */     String xslFileName = getStylesheetFileName(source);
/* 1430 */     File xslFile = null;
/* 1431 */     if (xslFileName != null) {
/* 1432 */       xslFile = new File(xslFileName);
/*      */     }
/*      */ 
/* 1435 */     String jarPath = null;
/* 1436 */     if (this._destinationDirectory != null) {
/* 1437 */       jarPath = this._destinationDirectory + "/" + this._jarFileName;
/*      */     }
/* 1439 */     else if ((xslFile != null) && (xslFile.getParent() != null))
/* 1440 */       jarPath = xslFile.getParent() + "/" + this._jarFileName;
/*      */     else {
/* 1442 */       jarPath = this._jarFileName;
/*      */     }
/*      */ 
/* 1446 */     File file = new File(jarPath);
/* 1447 */     if (!file.exists()) {
/* 1448 */       return (byte[][])null;
/*      */     }
/*      */ 
/* 1452 */     if ((xslFile != null) && (xslFile.exists())) {
/* 1453 */       long xslTimestamp = xslFile.lastModified();
/* 1454 */       long transletTimestamp = file.lastModified();
/* 1455 */       if (transletTimestamp < xslTimestamp) {
/* 1456 */         return (byte[][])null;
/*      */       }
/*      */     }
/*      */ 
/* 1460 */     ZipFile jarFile = null;
/*      */     try {
/* 1462 */       jarFile = new ZipFile(file);
/*      */     }
/*      */     catch (IOException e) {
/* 1465 */       return (byte[][])null;
/*      */     }
/*      */ 
/* 1468 */     String transletPath = fullClassName.replace('.', '/');
/* 1469 */     String transletAuxPrefix = transletPath + "$";
/* 1470 */     String transletFullName = transletPath + ".class";
/*      */ 
/* 1472 */     Vector bytecodes = new Vector();
/*      */ 
/* 1476 */     Enumeration entries = jarFile.entries();
/* 1477 */     while (entries.hasMoreElements())
/*      */     {
/* 1479 */       ZipEntry entry = (ZipEntry)entries.nextElement();
/* 1480 */       String entryName = entry.getName();
/* 1481 */       if ((entry.getSize() > 0L) && ((entryName.equals(transletFullName)) || ((entryName.endsWith(".class")) && (entryName.startsWith(transletAuxPrefix)))))
/*      */       {
/*      */         try
/*      */         {
/* 1487 */           InputStream input = jarFile.getInputStream(entry);
/* 1488 */           int size = (int)entry.getSize();
/* 1489 */           byte[] bytes = new byte[size];
/* 1490 */           readFromInputStream(bytes, input, size);
/* 1491 */           input.close();
/* 1492 */           bytecodes.addElement(bytes);
/*      */         }
/*      */         catch (IOException e) {
/* 1495 */           return (byte[][])null;
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1501 */     int count = bytecodes.size();
/* 1502 */     if (count > 0) {
/* 1503 */       byte[][] result = new byte[count][1];
/* 1504 */       for (int i = 0; i < count; i++) {
/* 1505 */         result[i] = ((byte[])(byte[])bytecodes.elementAt(i));
/*      */       }
/*      */ 
/* 1508 */       return result;
/*      */     }
/*      */ 
/* 1511 */     return (byte[][])null;
/*      */   }
/*      */ 
/*      */   private void readFromInputStream(byte[] bytes, InputStream input, int size)
/*      */     throws IOException
/*      */   {
/* 1524 */     int n = 0;
/* 1525 */     int offset = 0;
/* 1526 */     int length = size;
/* 1527 */     while ((length > 0) && ((n = input.read(bytes, offset, length)) > 0)) {
/* 1528 */       offset += n;
/* 1529 */       length -= n;
/*      */     }
/*      */   }
/*      */ 
/*      */   private String getTransletBaseName(Source source)
/*      */   {
/* 1546 */     String transletBaseName = null;
/* 1547 */     if (!this._transletName.equals("GregorSamsa")) {
/* 1548 */       return this._transletName;
/*      */     }
/* 1550 */     String systemId = source.getSystemId();
/* 1551 */     if (systemId != null) {
/* 1552 */       String baseName = Util.baseName(systemId);
/* 1553 */       if (baseName != null) {
/* 1554 */         baseName = Util.noExtName(baseName);
/* 1555 */         transletBaseName = Util.toJavaName(baseName);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1560 */     return transletBaseName != null ? transletBaseName : "GregorSamsa";
/*      */   }
/*      */ 
/*      */   private String getStylesheetFileName(Source source)
/*      */   {
/* 1572 */     String systemId = source.getSystemId();
/* 1573 */     if (systemId != null) {
/* 1574 */       File file = new File(systemId);
/* 1575 */       if (file.exists()) {
/* 1576 */         return systemId;
/*      */       }
/* 1578 */       URL url = null;
/*      */       try {
/* 1580 */         url = new URL(systemId);
/*      */       }
/*      */       catch (MalformedURLException e) {
/* 1583 */         return null;
/*      */       }
/*      */ 
/* 1586 */       if ("file".equals(url.getProtocol())) {
/* 1587 */         return url.getFile();
/*      */       }
/* 1589 */       return null;
/*      */     }
/*      */ 
/* 1593 */     return null;
/*      */   }
/*      */ 
/*      */   protected Class getDTMManagerClass()
/*      */   {
/* 1600 */     return this.m_DTMManagerClass;
/*      */   }
/*      */ 
/*      */   private static class PIParamWrapper
/*      */   {
/*  158 */     public String _media = null;
/*  159 */     public String _title = null;
/*  160 */     public String _charset = null;
/*      */ 
/*      */     public PIParamWrapper(String media, String title, String charset) {
/*  163 */       this._media = media;
/*  164 */       this._title = title;
/*  165 */       this._charset = charset;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl
 * JD-Core Version:    0.6.2
 */