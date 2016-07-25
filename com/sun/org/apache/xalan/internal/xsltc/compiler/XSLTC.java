/*      */ package com.sun.org.apache.xalan.internal.xsltc.compiler;
/*      */ 
/*      */ import com.sun.org.apache.bcel.internal.classfile.JavaClass;
/*      */ import com.sun.org.apache.xalan.internal.utils.FeatureManager;
/*      */ import com.sun.org.apache.xalan.internal.utils.FeatureManager.Feature;
/*      */ import com.sun.org.apache.xalan.internal.utils.SecuritySupport;
/*      */ import com.sun.org.apache.xalan.internal.utils.XMLSecurityManager;
/*      */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
/*      */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util;
/*      */ import java.io.BufferedOutputStream;
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.io.File;
/*      */ import java.io.FileOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.net.URL;
/*      */ import java.util.Collections;
/*      */ import java.util.Date;
/*      */ import java.util.Enumeration;
/*      */ import java.util.HashMap;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Map;
/*      */ import java.util.Properties;
/*      */ import java.util.Vector;
/*      */ import java.util.jar.Attributes;
/*      */ import java.util.jar.Attributes.Name;
/*      */ import java.util.jar.JarEntry;
/*      */ import java.util.jar.JarOutputStream;
/*      */ import java.util.jar.Manifest;
/*      */ import org.xml.sax.InputSource;
/*      */ import org.xml.sax.XMLReader;
/*      */ 
/*      */ public final class XSLTC
/*      */ {
/*      */   private Parser _parser;
/*   72 */   private XMLReader _reader = null;
/*      */ 
/*   75 */   private SourceLoader _loader = null;
/*      */   private Stylesheet _stylesheet;
/*   82 */   private int _modeSerial = 1;
/*   83 */   private int _stylesheetSerial = 1;
/*   84 */   private int _stepPatternSerial = 1;
/*   85 */   private int _helperClassSerial = 0;
/*   86 */   private int _attributeSetSerial = 0;
/*      */   private int[] _numberFieldIndexes;
/*      */   private int _nextGType;
/*      */   private Vector _namesIndex;
/*      */   private Hashtable _elements;
/*      */   private Hashtable _attributes;
/*      */   private int _nextNSType;
/*      */   private Vector _namespaceIndex;
/*      */   private Hashtable _namespaces;
/*      */   private Hashtable _namespacePrefixes;
/*      */   private Vector m_characterData;
/*      */   public static final int FILE_OUTPUT = 0;
/*      */   public static final int JAR_OUTPUT = 1;
/*      */   public static final int BYTEARRAY_OUTPUT = 2;
/*      */   public static final int CLASSLOADER_OUTPUT = 3;
/*      */   public static final int BYTEARRAY_AND_FILE_OUTPUT = 4;
/*      */   public static final int BYTEARRAY_AND_JAR_OUTPUT = 5;
/*  116 */   private boolean _debug = false;
/*  117 */   private String _jarFileName = null;
/*  118 */   private String _className = null;
/*  119 */   private String _packageName = null;
/*  120 */   private File _destDir = null;
/*  121 */   private int _outputType = 0;
/*      */   private Vector _classes;
/*      */   private Vector _bcelClasses;
/*  125 */   private boolean _callsNodeset = false;
/*  126 */   private boolean _multiDocument = false;
/*  127 */   private boolean _hasIdCall = false;
/*      */ 
/*  135 */   private boolean _templateInlining = false;
/*      */ 
/*  140 */   private boolean _isSecureProcessing = false;
/*      */ 
/*  142 */   private boolean _useServicesMechanism = true;
/*      */ 
/*  147 */   private String _accessExternalStylesheet = "all";
/*      */ 
/*  151 */   private String _accessExternalDTD = "all";
/*      */   private XMLSecurityManager _xmlSecurityManager;
/*      */   private final FeatureManager _featureManager;
/*      */   private ClassLoader _extensionClassLoader;
/*      */   private final Map<String, Class> _externalExtensionFunctions;
/*      */ 
/*      */   public XSLTC(boolean useServicesMechanism, FeatureManager featureManager)
/*      */   {
/*  173 */     this._parser = new Parser(this, useServicesMechanism);
/*  174 */     this._featureManager = featureManager;
/*  175 */     this._extensionClassLoader = null;
/*  176 */     this._externalExtensionFunctions = new HashMap();
/*      */   }
/*      */ 
/*      */   public void setSecureProcessing(boolean flag)
/*      */   {
/*  183 */     this._isSecureProcessing = flag;
/*      */   }
/*      */ 
/*      */   public boolean isSecureProcessing()
/*      */   {
/*  190 */     return this._isSecureProcessing;
/*      */   }
/*      */ 
/*      */   public boolean useServicesMechnism()
/*      */   {
/*  196 */     return this._useServicesMechanism;
/*      */   }
/*      */ 
/*      */   public void setServicesMechnism(boolean flag)
/*      */   {
/*  203 */     this._useServicesMechanism = flag;
/*      */   }
/*      */ 
/*      */   public boolean getFeature(FeatureManager.Feature name)
/*      */   {
/*  212 */     return this._featureManager.isFeatureEnabled(name);
/*      */   }
/*      */ 
/*      */   public Object getProperty(String name)
/*      */   {
/*  219 */     if (name.equals("http://javax.xml.XMLConstants/property/accessExternalStylesheet")) {
/*  220 */       return this._accessExternalStylesheet;
/*      */     }
/*  222 */     if (name.equals("http://javax.xml.XMLConstants/property/accessExternalDTD"))
/*  223 */       return this._accessExternalDTD;
/*  224 */     if (name.equals("http://apache.org/xml/properties/security-manager"))
/*  225 */       return this._xmlSecurityManager;
/*  226 */     if (name.equals("jdk.xml.transform.extensionClassLoader")) {
/*  227 */       return this._extensionClassLoader;
/*      */     }
/*  229 */     return null;
/*      */   }
/*      */ 
/*      */   public void setProperty(String name, Object value)
/*      */   {
/*  236 */     if (name.equals("http://javax.xml.XMLConstants/property/accessExternalStylesheet")) {
/*  237 */       this._accessExternalStylesheet = ((String)value);
/*      */     }
/*  239 */     else if (name.equals("http://javax.xml.XMLConstants/property/accessExternalDTD")) {
/*  240 */       this._accessExternalDTD = ((String)value);
/*  241 */     } else if (name.equals("http://apache.org/xml/properties/security-manager")) {
/*  242 */       this._xmlSecurityManager = ((XMLSecurityManager)value);
/*  243 */     } else if (name.equals("jdk.xml.transform.extensionClassLoader")) {
/*  244 */       this._extensionClassLoader = ((ClassLoader)value);
/*      */ 
/*  247 */       this._externalExtensionFunctions.clear();
/*      */     }
/*      */   }
/*      */ 
/*      */   public Parser getParser()
/*      */   {
/*  255 */     return this._parser;
/*      */   }
/*      */ 
/*      */   public void setOutputType(int type)
/*      */   {
/*  262 */     this._outputType = type;
/*      */   }
/*      */ 
/*      */   public Properties getOutputProperties()
/*      */   {
/*  269 */     return this._parser.getOutputProperties();
/*      */   }
/*      */ 
/*      */   public void init()
/*      */   {
/*  276 */     reset();
/*  277 */     this._reader = null;
/*  278 */     this._classes = new Vector();
/*  279 */     this._bcelClasses = new Vector();
/*      */   }
/*      */ 
/*      */   private void setExternalExtensionFunctions(String name, Class clazz) {
/*  283 */     if ((this._isSecureProcessing) && (clazz != null) && (!this._externalExtensionFunctions.containsKey(name)))
/*  284 */       this._externalExtensionFunctions.put(name, clazz);
/*      */   }
/*      */ 
/*      */   Class loadExternalFunction(String name)
/*      */     throws ClassNotFoundException
/*      */   {
/*  294 */     Class loaded = null;
/*      */ 
/*  296 */     if (this._externalExtensionFunctions.containsKey(name)) {
/*  297 */       loaded = (Class)this._externalExtensionFunctions.get(name);
/*  298 */     } else if (this._extensionClassLoader != null) {
/*  299 */       loaded = Class.forName(name, true, this._extensionClassLoader);
/*  300 */       setExternalExtensionFunctions(name, loaded);
/*      */     }
/*  302 */     if (loaded == null) {
/*  303 */       throw new ClassNotFoundException(name);
/*      */     }
/*      */ 
/*  306 */     return loaded;
/*      */   }
/*      */ 
/*      */   public Map<String, Class> getExternalExtensionFunctions()
/*      */   {
/*  314 */     return Collections.unmodifiableMap(this._externalExtensionFunctions);
/*      */   }
/*      */ 
/*      */   private void reset()
/*      */   {
/*  321 */     this._nextGType = 14;
/*  322 */     this._elements = new Hashtable();
/*  323 */     this._attributes = new Hashtable();
/*  324 */     this._namespaces = new Hashtable();
/*  325 */     this._namespaces.put("", new Integer(this._nextNSType));
/*  326 */     this._namesIndex = new Vector(128);
/*  327 */     this._namespaceIndex = new Vector(32);
/*  328 */     this._namespacePrefixes = new Hashtable();
/*  329 */     this._stylesheet = null;
/*  330 */     this._parser.init();
/*      */ 
/*  332 */     this._modeSerial = 1;
/*  333 */     this._stylesheetSerial = 1;
/*  334 */     this._stepPatternSerial = 1;
/*  335 */     this._helperClassSerial = 0;
/*  336 */     this._attributeSetSerial = 0;
/*  337 */     this._multiDocument = false;
/*  338 */     this._hasIdCall = false;
/*  339 */     this._numberFieldIndexes = new int[] { -1, -1, -1 };
/*      */ 
/*  344 */     this._externalExtensionFunctions.clear();
/*      */   }
/*      */ 
/*      */   public void setSourceLoader(SourceLoader loader)
/*      */   {
/*  353 */     this._loader = loader;
/*      */   }
/*      */ 
/*      */   public void setTemplateInlining(boolean templateInlining)
/*      */   {
/*  363 */     this._templateInlining = templateInlining;
/*      */   }
/*      */ 
/*      */   public boolean getTemplateInlining()
/*      */   {
/*  369 */     return this._templateInlining;
/*      */   }
/*      */ 
/*      */   public void setPIParameters(String media, String title, String charset)
/*      */   {
/*  382 */     this._parser.setPIParameters(media, title, charset);
/*      */   }
/*      */ 
/*      */   public boolean compile(URL url)
/*      */   {
/*      */     try
/*      */     {
/*  392 */       InputStream stream = url.openStream();
/*  393 */       InputSource input = new InputSource(stream);
/*  394 */       input.setSystemId(url.toString());
/*  395 */       return compile(input, this._className);
/*      */     }
/*      */     catch (IOException e) {
/*  398 */       this._parser.reportError(2, new ErrorMsg("JAXP_COMPILE_ERR", e));
/*  399 */     }return false;
/*      */   }
/*      */ 
/*      */   public boolean compile(URL url, String name)
/*      */   {
/*      */     try
/*      */     {
/*  411 */       InputStream stream = url.openStream();
/*  412 */       InputSource input = new InputSource(stream);
/*  413 */       input.setSystemId(url.toString());
/*  414 */       return compile(input, name);
/*      */     }
/*      */     catch (IOException e) {
/*  417 */       this._parser.reportError(2, new ErrorMsg("JAXP_COMPILE_ERR", e));
/*  418 */     }return false;
/*      */   }
/*      */ 
/*      */   public boolean compile(InputStream stream, String name)
/*      */   {
/*  429 */     InputSource input = new InputSource(stream);
/*  430 */     input.setSystemId(name);
/*  431 */     return compile(input, name);
/*      */   }
/*      */ 
/*      */   public boolean compile(InputSource input, String name)
/*      */   {
/*      */     try
/*      */     {
/*  443 */       reset();
/*      */ 
/*  446 */       String systemId = null;
/*  447 */       if (input != null) {
/*  448 */         systemId = input.getSystemId();
/*      */       }
/*      */ 
/*  452 */       if (this._className == null) {
/*  453 */         if (name != null) {
/*  454 */           setClassName(name);
/*      */         }
/*  456 */         else if ((systemId != null) && (!systemId.equals(""))) {
/*  457 */           setClassName(Util.baseName(systemId));
/*      */         }
/*      */ 
/*  461 */         if ((this._className == null) || (this._className.length() == 0)) {
/*  462 */           setClassName("GregorSamsa");
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  467 */       SyntaxTreeNode element = null;
/*  468 */       if (this._reader == null) {
/*  469 */         element = this._parser.parse(input);
/*      */       }
/*      */       else {
/*  472 */         element = this._parser.parse(this._reader, input);
/*      */       }
/*      */ 
/*  476 */       if ((!this._parser.errorsFound()) && (element != null))
/*      */       {
/*  478 */         this._stylesheet = this._parser.makeStylesheet(element);
/*  479 */         this._stylesheet.setSourceLoader(this._loader);
/*  480 */         this._stylesheet.setSystemId(systemId);
/*  481 */         this._stylesheet.setParentStylesheet(null);
/*  482 */         this._stylesheet.setTemplateInlining(this._templateInlining);
/*  483 */         this._parser.setCurrentStylesheet(this._stylesheet);
/*      */ 
/*  486 */         this._parser.createAST(this._stylesheet);
/*      */       }
/*      */ 
/*  489 */       if ((!this._parser.errorsFound()) && (this._stylesheet != null)) {
/*  490 */         this._stylesheet.setCallsNodeset(this._callsNodeset);
/*  491 */         this._stylesheet.setMultiDocument(this._multiDocument);
/*  492 */         this._stylesheet.setHasIdCall(this._hasIdCall);
/*      */ 
/*  495 */         synchronized (getClass()) {
/*  496 */           this._stylesheet.translate();
/*      */         }
/*      */       }
/*      */     }
/*      */     catch (Exception e) {
/*  501 */       e.printStackTrace();
/*  502 */       this._parser.reportError(2, new ErrorMsg("JAXP_COMPILE_ERR", e));
/*      */     }
/*      */     catch (Error e) {
/*  505 */       if (this._debug) e.printStackTrace();
/*  506 */       this._parser.reportError(2, new ErrorMsg("JAXP_COMPILE_ERR", e));
/*      */     }
/*      */     finally {
/*  509 */       this._reader = null;
/*      */     }
/*  511 */     return !this._parser.errorsFound();
/*      */   }
/*      */ 
/*      */   public boolean compile(Vector stylesheets)
/*      */   {
/*  521 */     int count = stylesheets.size();
/*      */ 
/*  524 */     if (count == 0) return true;
/*      */ 
/*  528 */     if (count == 1) {
/*  529 */       Object url = stylesheets.firstElement();
/*  530 */       if ((url instanceof URL)) {
/*  531 */         return compile((URL)url);
/*      */       }
/*  533 */       return false;
/*      */     }
/*      */ 
/*  537 */     Enumeration urls = stylesheets.elements();
/*  538 */     while (urls.hasMoreElements()) {
/*  539 */       this._className = null;
/*  540 */       Object url = urls.nextElement();
/*  541 */       if (((url instanceof URL)) && 
/*  542 */         (!compile((URL)url))) return false;
/*      */ 
/*      */     }
/*      */ 
/*  546 */     return true;
/*      */   }
/*      */ 
/*      */   public byte[][] getBytecodes()
/*      */   {
/*  554 */     int count = this._classes.size();
/*  555 */     byte[][] result = new byte[count][1];
/*  556 */     for (int i = 0; i < count; i++)
/*  557 */       result[i] = ((byte[])(byte[])this._classes.elementAt(i));
/*  558 */     return result;
/*      */   }
/*      */ 
/*      */   public byte[][] compile(String name, InputSource input, int outputType)
/*      */   {
/*  570 */     this._outputType = outputType;
/*  571 */     if (compile(input, name)) {
/*  572 */       return getBytecodes();
/*      */     }
/*  574 */     return (byte[][])null;
/*      */   }
/*      */ 
/*      */   public byte[][] compile(String name, InputSource input)
/*      */   {
/*  585 */     return compile(name, input, 2);
/*      */   }
/*      */ 
/*      */   public void setXMLReader(XMLReader reader)
/*      */   {
/*  593 */     this._reader = reader;
/*      */   }
/*      */ 
/*      */   public XMLReader getXMLReader()
/*      */   {
/*  600 */     return this._reader;
/*      */   }
/*      */ 
/*      */   public Vector getErrors()
/*      */   {
/*  608 */     return this._parser.getErrors();
/*      */   }
/*      */ 
/*      */   public Vector getWarnings()
/*      */   {
/*  616 */     return this._parser.getWarnings();
/*      */   }
/*      */ 
/*      */   public void printErrors()
/*      */   {
/*  623 */     this._parser.printErrors();
/*      */   }
/*      */ 
/*      */   public void printWarnings()
/*      */   {
/*  630 */     this._parser.printWarnings();
/*      */   }
/*      */ 
/*      */   protected void setMultiDocument(boolean flag)
/*      */   {
/*  638 */     this._multiDocument = flag;
/*      */   }
/*      */ 
/*      */   public boolean isMultiDocument() {
/*  642 */     return this._multiDocument;
/*      */   }
/*      */ 
/*      */   protected void setCallsNodeset(boolean flag)
/*      */   {
/*  650 */     if (flag) setMultiDocument(flag);
/*  651 */     this._callsNodeset = flag;
/*      */   }
/*      */ 
/*      */   public boolean callsNodeset() {
/*  655 */     return this._callsNodeset;
/*      */   }
/*      */ 
/*      */   protected void setHasIdCall(boolean flag) {
/*  659 */     this._hasIdCall = flag;
/*      */   }
/*      */ 
/*      */   public boolean hasIdCall() {
/*  663 */     return this._hasIdCall;
/*      */   }
/*      */ 
/*      */   public void setClassName(String className)
/*      */   {
/*  673 */     String base = Util.baseName(className);
/*  674 */     String noext = Util.noExtName(base);
/*  675 */     String name = Util.toJavaName(noext);
/*      */ 
/*  677 */     if (this._packageName == null)
/*  678 */       this._className = name;
/*      */     else
/*  680 */       this._className = (this._packageName + '.' + name);
/*      */   }
/*      */ 
/*      */   public String getClassName()
/*      */   {
/*  687 */     return this._className;
/*      */   }
/*      */ 
/*      */   private String classFileName(String className)
/*      */   {
/*  695 */     return className.replace('.', File.separatorChar) + ".class";
/*      */   }
/*      */ 
/*      */   private File getOutputFile(String className)
/*      */   {
/*  702 */     if (this._destDir != null) {
/*  703 */       return new File(this._destDir, classFileName(className));
/*      */     }
/*  705 */     return new File(classFileName(className));
/*      */   }
/*      */ 
/*      */   public boolean setDestDirectory(String dstDirName)
/*      */   {
/*  713 */     File dir = new File(dstDirName);
/*  714 */     if ((SecuritySupport.getFileExists(dir)) || (dir.mkdirs())) {
/*  715 */       this._destDir = dir;
/*  716 */       return true;
/*      */     }
/*      */ 
/*  719 */     this._destDir = null;
/*  720 */     return false;
/*      */   }
/*      */ 
/*      */   public void setPackageName(String packageName)
/*      */   {
/*  728 */     this._packageName = packageName;
/*  729 */     if (this._className != null) setClassName(this._className);
/*      */   }
/*      */ 
/*      */   public void setJarFileName(String jarFileName)
/*      */   {
/*  737 */     String JAR_EXT = ".jar";
/*  738 */     if (jarFileName.endsWith(".jar"))
/*  739 */       this._jarFileName = jarFileName;
/*      */     else
/*  741 */       this._jarFileName = (jarFileName + ".jar");
/*  742 */     this._outputType = 1;
/*      */   }
/*      */ 
/*      */   public String getJarFileName() {
/*  746 */     return this._jarFileName;
/*      */   }
/*      */ 
/*      */   public void setStylesheet(Stylesheet stylesheet)
/*      */   {
/*  753 */     if (this._stylesheet == null) this._stylesheet = stylesheet;
/*      */   }
/*      */ 
/*      */   public Stylesheet getStylesheet()
/*      */   {
/*  760 */     return this._stylesheet;
/*      */   }
/*      */ 
/*      */   public int registerAttribute(QName name)
/*      */   {
/*  768 */     Integer code = (Integer)this._attributes.get(name.toString());
/*  769 */     if (code == null) {
/*  770 */       code = new Integer(this._nextGType++);
/*  771 */       this._attributes.put(name.toString(), code);
/*  772 */       String uri = name.getNamespace();
/*  773 */       String local = "@" + name.getLocalPart();
/*  774 */       if ((uri != null) && (!uri.equals("")))
/*  775 */         this._namesIndex.addElement(uri + ":" + local);
/*      */       else
/*  777 */         this._namesIndex.addElement(local);
/*  778 */       if (name.getLocalPart().equals("*")) {
/*  779 */         registerNamespace(name.getNamespace());
/*      */       }
/*      */     }
/*  782 */     return code.intValue();
/*      */   }
/*      */ 
/*      */   public int registerElement(QName name)
/*      */   {
/*  791 */     Integer code = (Integer)this._elements.get(name.toString());
/*  792 */     if (code == null) {
/*  793 */       this._elements.put(name.toString(), code = new Integer(this._nextGType++));
/*  794 */       this._namesIndex.addElement(name.toString());
/*      */     }
/*  796 */     if (name.getLocalPart().equals("*")) {
/*  797 */       registerNamespace(name.getNamespace());
/*      */     }
/*  799 */     return code.intValue();
/*      */   }
/*      */ 
/*      */   public int registerNamespacePrefix(QName name)
/*      */   {
/*  809 */     Integer code = (Integer)this._namespacePrefixes.get(name.toString());
/*  810 */     if (code == null) {
/*  811 */       code = new Integer(this._nextGType++);
/*  812 */       this._namespacePrefixes.put(name.toString(), code);
/*  813 */       String uri = name.getNamespace();
/*  814 */       if ((uri != null) && (!uri.equals("")))
/*      */       {
/*  816 */         this._namesIndex.addElement("?");
/*      */       }
/*  818 */       else this._namesIndex.addElement("?" + name.getLocalPart());
/*      */     }
/*      */ 
/*  821 */     return code.intValue();
/*      */   }
/*      */ 
/*      */   public int registerNamespace(String namespaceURI)
/*      */   {
/*  829 */     Integer code = (Integer)this._namespaces.get(namespaceURI);
/*  830 */     if (code == null) {
/*  831 */       code = new Integer(this._nextNSType++);
/*  832 */       this._namespaces.put(namespaceURI, code);
/*  833 */       this._namespaceIndex.addElement(namespaceURI);
/*      */     }
/*  835 */     return code.intValue();
/*      */   }
/*      */ 
/*      */   public int nextModeSerial() {
/*  839 */     return this._modeSerial++;
/*      */   }
/*      */ 
/*      */   public int nextStylesheetSerial() {
/*  843 */     return this._stylesheetSerial++;
/*      */   }
/*      */ 
/*      */   public int nextStepPatternSerial() {
/*  847 */     return this._stepPatternSerial++;
/*      */   }
/*      */ 
/*      */   public int[] getNumberFieldIndexes() {
/*  851 */     return this._numberFieldIndexes;
/*      */   }
/*      */ 
/*      */   public int nextHelperClassSerial() {
/*  855 */     return this._helperClassSerial++;
/*      */   }
/*      */ 
/*      */   public int nextAttributeSetSerial() {
/*  859 */     return this._attributeSetSerial++;
/*      */   }
/*      */ 
/*      */   public Vector getNamesIndex() {
/*  863 */     return this._namesIndex;
/*      */   }
/*      */ 
/*      */   public Vector getNamespaceIndex() {
/*  867 */     return this._namespaceIndex;
/*      */   }
/*      */ 
/*      */   public String getHelperClassName()
/*      */   {
/*  875 */     return getClassName() + '$' + this._helperClassSerial++;
/*      */   }
/*      */ 
/*      */   public void dumpClass(JavaClass clazz)
/*      */   {
/*  880 */     if ((this._outputType == 0) || (this._outputType == 4))
/*      */     {
/*  883 */       File outFile = getOutputFile(clazz.getClassName());
/*  884 */       String parentDir = outFile.getParent();
/*  885 */       if (parentDir != null) {
/*  886 */         File parentFile = new File(parentDir);
/*  887 */         if (!SecuritySupport.getFileExists(parentFile))
/*  888 */           parentFile.mkdirs();
/*      */       }
/*      */     }
/*      */     try
/*      */     {
/*  893 */       switch (this._outputType) {
/*      */       case 0:
/*  895 */         clazz.dump(new BufferedOutputStream(new FileOutputStream(getOutputFile(clazz.getClassName()))));
/*      */ 
/*  899 */         break;
/*      */       case 1:
/*  901 */         this._bcelClasses.addElement(clazz);
/*  902 */         break;
/*      */       case 2:
/*      */       case 3:
/*      */       case 4:
/*      */       case 5:
/*  907 */         ByteArrayOutputStream out = new ByteArrayOutputStream(2048);
/*  908 */         clazz.dump(out);
/*  909 */         this._classes.addElement(out.toByteArray());
/*      */ 
/*  911 */         if (this._outputType == 4) {
/*  912 */           clazz.dump(new BufferedOutputStream(new FileOutputStream(getOutputFile(clazz.getClassName()))));
/*      */         }
/*  914 */         else if (this._outputType == 5)
/*  915 */           this._bcelClasses.addElement(clazz);
/*      */         break;
/*      */       }
/*      */     }
/*      */     catch (Exception e)
/*      */     {
/*  921 */       e.printStackTrace();
/*      */     }
/*      */   }
/*      */ 
/*      */   private String entryName(File f)
/*      */     throws IOException
/*      */   {
/*  929 */     return f.getName().replace(File.separatorChar, '/');
/*      */   }
/*      */ 
/*      */   public void outputToJar()
/*      */     throws IOException
/*      */   {
/*  937 */     Manifest manifest = new Manifest();
/*  938 */     Attributes atrs = manifest.getMainAttributes();
/*  939 */     atrs.put(Attributes.Name.MANIFEST_VERSION, "1.2");
/*      */ 
/*  941 */     Map map = manifest.getEntries();
/*      */ 
/*  943 */     Enumeration classes = this._bcelClasses.elements();
/*  944 */     String now = new Date().toString();
/*  945 */     Attributes.Name dateAttr = new Attributes.Name("Date");
/*      */ 
/*  947 */     while (classes.hasMoreElements()) {
/*  948 */       JavaClass clazz = (JavaClass)classes.nextElement();
/*  949 */       String className = clazz.getClassName().replace('.', '/');
/*  950 */       Attributes attr = new Attributes();
/*  951 */       attr.put(dateAttr, now);
/*  952 */       map.put(className + ".class", attr);
/*      */     }
/*      */ 
/*  955 */     File jarFile = new File(this._destDir, this._jarFileName);
/*  956 */     JarOutputStream jos = new JarOutputStream(new FileOutputStream(jarFile), manifest);
/*      */ 
/*  958 */     classes = this._bcelClasses.elements();
/*  959 */     while (classes.hasMoreElements()) {
/*  960 */       JavaClass clazz = (JavaClass)classes.nextElement();
/*  961 */       String className = clazz.getClassName().replace('.', '/');
/*  962 */       jos.putNextEntry(new JarEntry(className + ".class"));
/*  963 */       ByteArrayOutputStream out = new ByteArrayOutputStream(2048);
/*  964 */       clazz.dump(out);
/*  965 */       out.writeTo(jos);
/*      */     }
/*  967 */     jos.close();
/*      */   }
/*      */ 
/*      */   public void setDebug(boolean debug)
/*      */   {
/*  974 */     this._debug = debug;
/*      */   }
/*      */ 
/*      */   public boolean debug()
/*      */   {
/*  981 */     return this._debug;
/*      */   }
/*      */ 
/*      */   public String getCharacterData(int index)
/*      */   {
/*  994 */     return ((StringBuffer)this.m_characterData.elementAt(index)).toString();
/*      */   }
/*      */ 
/*      */   public int getCharacterDataCount()
/*      */   {
/* 1002 */     return this.m_characterData != null ? this.m_characterData.size() : 0;
/*      */   }
/*      */ 
/*      */   public int addCharacterData(String newData)
/*      */   {
/*      */     StringBuffer currData;
/* 1014 */     if (this.m_characterData == null) {
/* 1015 */       this.m_characterData = new Vector();
/* 1016 */       StringBuffer currData = new StringBuffer();
/* 1017 */       this.m_characterData.addElement(currData);
/*      */     } else {
/* 1019 */       currData = (StringBuffer)this.m_characterData.elementAt(this.m_characterData.size() - 1);
/*      */     }
/*      */ 
/* 1027 */     if (newData.length() + currData.length() > 21845) {
/* 1028 */       currData = new StringBuffer();
/* 1029 */       this.m_characterData.addElement(currData);
/*      */     }
/*      */ 
/* 1032 */     int newDataOffset = currData.length();
/* 1033 */     currData.append(newData);
/*      */ 
/* 1035 */     return newDataOffset;
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.XSLTC
 * JD-Core Version:    0.6.2
 */