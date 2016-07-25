/*      */ package com.sun.org.apache.xalan.internal.xsltc.compiler;
/*      */ 
/*      */ import com.sun.java_cup.internal.runtime.Symbol;
/*      */ import com.sun.org.apache.xalan.internal.utils.FactoryImpl;
/*      */ import com.sun.org.apache.xalan.internal.utils.ObjectFactory;
/*      */ import com.sun.org.apache.xalan.internal.utils.SecuritySupport;
/*      */ import com.sun.org.apache.xalan.internal.utils.XMLSecurityManager;
/*      */ import com.sun.org.apache.xalan.internal.utils.XMLSecurityManager.Limit;
/*      */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
/*      */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodType;
/*      */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
/*      */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
/*      */ import com.sun.org.apache.xml.internal.serializer.utils.SystemIDResolver;
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.io.PrintStream;
/*      */ import java.io.StringReader;
/*      */ import java.util.Dictionary;
/*      */ import java.util.Enumeration;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Properties;
/*      */ import java.util.Stack;
/*      */ import java.util.StringTokenizer;
/*      */ import java.util.Vector;
/*      */ import javax.xml.parsers.ParserConfigurationException;
/*      */ import javax.xml.parsers.SAXParser;
/*      */ import javax.xml.parsers.SAXParserFactory;
/*      */ import org.xml.sax.Attributes;
/*      */ import org.xml.sax.ContentHandler;
/*      */ import org.xml.sax.InputSource;
/*      */ import org.xml.sax.Locator;
/*      */ import org.xml.sax.SAXException;
/*      */ import org.xml.sax.SAXNotRecognizedException;
/*      */ import org.xml.sax.SAXParseException;
/*      */ import org.xml.sax.XMLReader;
/*      */ import org.xml.sax.helpers.AttributesImpl;
/*      */ 
/*      */ public class Parser
/*      */   implements Constants, ContentHandler
/*      */ {
/*      */   private static final String XSL = "xsl";
/*      */   private static final String TRANSLET = "translet";
/*   73 */   private Locator _locator = null;
/*      */   private XSLTC _xsltc;
/*      */   private XPathParser _xpathParser;
/*      */   private Vector _errors;
/*      */   private Vector _warnings;
/*      */   private Hashtable _instructionClasses;
/*      */   private Hashtable _instructionAttrs;
/*      */   private Hashtable _qNames;
/*      */   private Hashtable _namespaces;
/*      */   private QName _useAttributeSets;
/*      */   private QName _excludeResultPrefixes;
/*      */   private QName _extensionElementPrefixes;
/*      */   private Hashtable _variableScope;
/*      */   private Stylesheet _currentStylesheet;
/*      */   private SymbolTable _symbolTable;
/*      */   private Output _output;
/*      */   private Template _template;
/*      */   private boolean _rootNamespaceDef;
/*      */   private SyntaxTreeNode _root;
/*      */   private String _target;
/*      */   private int _currentImportPrecedence;
/*  101 */   private boolean _useServicesMechanism = true;
/*      */ 
/*  524 */   private String _PImedia = null;
/*  525 */   private String _PItitle = null;
/*  526 */   private String _PIcharset = null;
/*      */ 
/*  947 */   private int _templateIndex = 0;
/*      */ 
/*  965 */   private boolean versionIsOne = true;
/*      */ 
/* 1252 */   private Stack _parentStack = null;
/* 1253 */   private Hashtable _prefixMapping = null;
/*      */ 
/*      */   public Parser(XSLTC xsltc, boolean useServicesMechanism)
/*      */   {
/*  104 */     this._xsltc = xsltc;
/*  105 */     this._useServicesMechanism = useServicesMechanism;
/*      */   }
/*      */ 
/*      */   public void init() {
/*  109 */     this._qNames = new Hashtable(512);
/*  110 */     this._namespaces = new Hashtable();
/*  111 */     this._instructionClasses = new Hashtable();
/*  112 */     this._instructionAttrs = new Hashtable();
/*  113 */     this._variableScope = new Hashtable();
/*  114 */     this._template = null;
/*  115 */     this._errors = new Vector();
/*  116 */     this._warnings = new Vector();
/*  117 */     this._symbolTable = new SymbolTable();
/*  118 */     this._xpathParser = new XPathParser(this);
/*  119 */     this._currentStylesheet = null;
/*  120 */     this._output = null;
/*  121 */     this._root = null;
/*  122 */     this._rootNamespaceDef = false;
/*  123 */     this._currentImportPrecedence = 1;
/*      */ 
/*  125 */     initStdClasses();
/*  126 */     initInstructionAttrs();
/*  127 */     initExtClasses();
/*  128 */     initSymbolTable();
/*      */ 
/*  130 */     this._useAttributeSets = getQName("http://www.w3.org/1999/XSL/Transform", "xsl", "use-attribute-sets");
/*      */ 
/*  132 */     this._excludeResultPrefixes = getQName("http://www.w3.org/1999/XSL/Transform", "xsl", "exclude-result-prefixes");
/*      */ 
/*  134 */     this._extensionElementPrefixes = getQName("http://www.w3.org/1999/XSL/Transform", "xsl", "extension-element-prefixes");
/*      */   }
/*      */ 
/*      */   public void setOutput(Output output)
/*      */   {
/*  139 */     if (this._output != null) {
/*  140 */       if (this._output.getImportPrecedence() <= output.getImportPrecedence()) {
/*  141 */         String cdata = this._output.getCdata();
/*  142 */         output.mergeOutput(this._output);
/*  143 */         this._output.disable();
/*  144 */         this._output = output;
/*      */       }
/*      */       else {
/*  147 */         output.disable();
/*      */       }
/*      */     }
/*      */     else
/*  151 */       this._output = output;
/*      */   }
/*      */ 
/*      */   public Output getOutput()
/*      */   {
/*  156 */     return this._output;
/*      */   }
/*      */ 
/*      */   public Properties getOutputProperties() {
/*  160 */     return getTopLevelStylesheet().getOutputProperties();
/*      */   }
/*      */ 
/*      */   public void addVariable(Variable var) {
/*  164 */     addVariableOrParam(var);
/*      */   }
/*      */ 
/*      */   public void addParameter(Param param) {
/*  168 */     addVariableOrParam(param);
/*      */   }
/*      */ 
/*      */   private void addVariableOrParam(VariableBase var) {
/*  172 */     Object existing = this._variableScope.get(var.getName());
/*  173 */     if (existing != null) {
/*  174 */       if ((existing instanceof Stack)) {
/*  175 */         Stack stack = (Stack)existing;
/*  176 */         stack.push(var);
/*      */       }
/*  178 */       else if ((existing instanceof VariableBase)) {
/*  179 */         Stack stack = new Stack();
/*  180 */         stack.push(existing);
/*  181 */         stack.push(var);
/*  182 */         this._variableScope.put(var.getName(), stack);
/*      */       }
/*      */     }
/*      */     else
/*  186 */       this._variableScope.put(var.getName(), var);
/*      */   }
/*      */ 
/*      */   public void removeVariable(QName name)
/*      */   {
/*  191 */     Object existing = this._variableScope.get(name);
/*  192 */     if ((existing instanceof Stack)) {
/*  193 */       Stack stack = (Stack)existing;
/*  194 */       if (!stack.isEmpty()) stack.pop();
/*  195 */       if (!stack.isEmpty()) return;
/*      */     }
/*  197 */     this._variableScope.remove(name);
/*      */   }
/*      */ 
/*      */   public VariableBase lookupVariable(QName name) {
/*  201 */     Object existing = this._variableScope.get(name);
/*  202 */     if ((existing instanceof VariableBase)) {
/*  203 */       return (VariableBase)existing;
/*      */     }
/*  205 */     if ((existing instanceof Stack)) {
/*  206 */       Stack stack = (Stack)existing;
/*  207 */       return (VariableBase)stack.peek();
/*      */     }
/*  209 */     return null;
/*      */   }
/*      */ 
/*      */   public void setXSLTC(XSLTC xsltc) {
/*  213 */     this._xsltc = xsltc;
/*      */   }
/*      */ 
/*      */   public XSLTC getXSLTC() {
/*  217 */     return this._xsltc;
/*      */   }
/*      */ 
/*      */   public int getCurrentImportPrecedence() {
/*  221 */     return this._currentImportPrecedence;
/*      */   }
/*      */ 
/*      */   public int getNextImportPrecedence() {
/*  225 */     return ++this._currentImportPrecedence;
/*      */   }
/*      */ 
/*      */   public void setCurrentStylesheet(Stylesheet stylesheet) {
/*  229 */     this._currentStylesheet = stylesheet;
/*      */   }
/*      */ 
/*      */   public Stylesheet getCurrentStylesheet() {
/*  233 */     return this._currentStylesheet;
/*      */   }
/*      */ 
/*      */   public Stylesheet getTopLevelStylesheet() {
/*  237 */     return this._xsltc.getStylesheet();
/*      */   }
/*      */ 
/*      */   public QName getQNameSafe(String stringRep)
/*      */   {
/*  242 */     int colon = stringRep.lastIndexOf(':');
/*  243 */     if (colon != -1) {
/*  244 */       String prefix = stringRep.substring(0, colon);
/*  245 */       String localname = stringRep.substring(colon + 1);
/*  246 */       String namespace = null;
/*      */ 
/*  249 */       if (!prefix.equals("xmlns")) {
/*  250 */         namespace = this._symbolTable.lookupNamespace(prefix);
/*  251 */         if (namespace == null) namespace = "";
/*      */       }
/*  253 */       return getQName(namespace, prefix, localname);
/*      */     }
/*      */ 
/*  256 */     String uri = stringRep.equals("xmlns") ? null : this._symbolTable.lookupNamespace("");
/*      */ 
/*  258 */     return getQName(uri, null, stringRep);
/*      */   }
/*      */ 
/*      */   public QName getQName(String stringRep)
/*      */   {
/*  263 */     return getQName(stringRep, true, false);
/*      */   }
/*      */ 
/*      */   public QName getQNameIgnoreDefaultNs(String stringRep) {
/*  267 */     return getQName(stringRep, true, true);
/*      */   }
/*      */ 
/*      */   public QName getQName(String stringRep, boolean reportError) {
/*  271 */     return getQName(stringRep, reportError, false);
/*      */   }
/*      */ 
/*      */   private QName getQName(String stringRep, boolean reportError, boolean ignoreDefaultNs)
/*      */   {
/*  278 */     int colon = stringRep.lastIndexOf(':');
/*  279 */     if (colon != -1) {
/*  280 */       String prefix = stringRep.substring(0, colon);
/*  281 */       String localname = stringRep.substring(colon + 1);
/*  282 */       String namespace = null;
/*      */ 
/*  285 */       if (!prefix.equals("xmlns")) {
/*  286 */         namespace = this._symbolTable.lookupNamespace(prefix);
/*  287 */         if ((namespace == null) && (reportError)) {
/*  288 */           int line = getLineNumber();
/*  289 */           ErrorMsg err = new ErrorMsg("NAMESPACE_UNDEF_ERR", line, prefix);
/*      */ 
/*  291 */           reportError(3, err);
/*      */         }
/*      */       }
/*  294 */       return getQName(namespace, prefix, localname);
/*      */     }
/*      */ 
/*  297 */     if (stringRep.equals("xmlns")) {
/*  298 */       ignoreDefaultNs = true;
/*      */     }
/*  300 */     String defURI = ignoreDefaultNs ? null : this._symbolTable.lookupNamespace("");
/*      */ 
/*  302 */     return getQName(defURI, null, stringRep);
/*      */   }
/*      */ 
/*      */   public QName getQName(String namespace, String prefix, String localname)
/*      */   {
/*  307 */     if ((namespace == null) || (namespace.equals(""))) {
/*  308 */       QName name = (QName)this._qNames.get(localname);
/*  309 */       if (name == null) {
/*  310 */         name = new QName(null, prefix, localname);
/*  311 */         this._qNames.put(localname, name);
/*      */       }
/*  313 */       return name;
/*      */     }
/*      */ 
/*  316 */     Dictionary space = (Dictionary)this._namespaces.get(namespace);
/*  317 */     String lexicalQName = prefix + ':' + localname;
/*      */ 
/*  322 */     if (space == null) {
/*  323 */       QName name = new QName(namespace, prefix, localname);
/*  324 */       this._namespaces.put(namespace, space = new Hashtable());
/*  325 */       space.put(lexicalQName, name);
/*  326 */       return name;
/*      */     }
/*      */ 
/*  329 */     QName name = (QName)space.get(lexicalQName);
/*  330 */     if (name == null) {
/*  331 */       name = new QName(namespace, prefix, localname);
/*  332 */       space.put(lexicalQName, name);
/*      */     }
/*  334 */     return name;
/*      */   }
/*      */ 
/*      */   public QName getQName(String scope, String name)
/*      */   {
/*  340 */     return getQName(scope + name);
/*      */   }
/*      */ 
/*      */   public QName getQName(QName scope, QName name) {
/*  344 */     return getQName(scope.toString() + name.toString());
/*      */   }
/*      */ 
/*      */   public QName getUseAttributeSets() {
/*  348 */     return this._useAttributeSets;
/*      */   }
/*      */ 
/*      */   public QName getExtensionElementPrefixes() {
/*  352 */     return this._extensionElementPrefixes;
/*      */   }
/*      */ 
/*      */   public QName getExcludeResultPrefixes() {
/*  356 */     return this._excludeResultPrefixes;
/*      */   }
/*      */ 
/*      */   public Stylesheet makeStylesheet(SyntaxTreeNode element)
/*      */     throws CompilerException
/*      */   {
/*      */     try
/*      */     {
/*      */       Stylesheet stylesheet;
/*      */       Stylesheet stylesheet;
/*  369 */       if ((element instanceof Stylesheet)) {
/*  370 */         stylesheet = (Stylesheet)element;
/*      */       }
/*      */       else {
/*  373 */         stylesheet = new Stylesheet();
/*  374 */         stylesheet.setSimplified();
/*  375 */         stylesheet.addElement(element);
/*  376 */         stylesheet.setAttributes((AttributesImpl)element.getAttributes());
/*      */ 
/*  379 */         if (element.lookupNamespace("") == null) {
/*  380 */           element.addPrefixMapping("", "");
/*      */         }
/*      */       }
/*  383 */       stylesheet.setParser(this);
/*  384 */       return stylesheet;
/*      */     }
/*      */     catch (ClassCastException e) {
/*  387 */       ErrorMsg err = new ErrorMsg("NOT_STYLESHEET_ERR", element);
/*  388 */       throw new CompilerException(err.toString());
/*      */     }
/*      */   }
/*      */ 
/*      */   public void createAST(Stylesheet stylesheet)
/*      */   {
/*      */     try
/*      */     {
/*  397 */       if (stylesheet != null) {
/*  398 */         stylesheet.parseContents(this);
/*  399 */         int precedence = stylesheet.getImportPrecedence();
/*  400 */         Enumeration elements = stylesheet.elements();
/*  401 */         while (elements.hasMoreElements()) {
/*  402 */           Object child = elements.nextElement();
/*  403 */           if ((child instanceof Text)) {
/*  404 */             int l = getLineNumber();
/*  405 */             ErrorMsg err = new ErrorMsg("ILLEGAL_TEXT_NODE_ERR", l, null);
/*      */ 
/*  407 */             reportError(3, err);
/*      */           }
/*      */         }
/*  410 */         if (!errorsFound())
/*  411 */           stylesheet.typeCheck(this._symbolTable);
/*      */       }
/*      */     }
/*      */     catch (TypeCheckError e)
/*      */     {
/*  416 */       reportError(3, new ErrorMsg("JAXP_COMPILE_ERR", e));
/*      */     }
/*      */   }
/*      */ 
/*      */   public SyntaxTreeNode parse(XMLReader reader, InputSource input)
/*      */   {
/*      */     try
/*      */     {
/*  429 */       reader.setContentHandler(this);
/*  430 */       reader.parse(input);
/*      */ 
/*  432 */       return getStylesheet(this._root);
/*      */     }
/*      */     catch (IOException e) {
/*  435 */       if (this._xsltc.debug()) e.printStackTrace();
/*  436 */       reportError(3, new ErrorMsg("JAXP_COMPILE_ERR", e));
/*      */     }
/*      */     catch (SAXException e) {
/*  439 */       Throwable ex = e.getException();
/*  440 */       if (this._xsltc.debug()) {
/*  441 */         e.printStackTrace();
/*  442 */         if (ex != null) ex.printStackTrace();
/*      */       }
/*  444 */       reportError(3, new ErrorMsg("JAXP_COMPILE_ERR", e));
/*      */     }
/*      */     catch (CompilerException e) {
/*  447 */       if (this._xsltc.debug()) e.printStackTrace();
/*  448 */       reportError(3, new ErrorMsg("JAXP_COMPILE_ERR", e));
/*      */     }
/*      */     catch (Exception e) {
/*  451 */       if (this._xsltc.debug()) e.printStackTrace();
/*  452 */       reportError(3, new ErrorMsg("JAXP_COMPILE_ERR", e));
/*      */     }
/*  454 */     return null;
/*      */   }
/*      */ 
/*      */   public SyntaxTreeNode parse(InputSource input)
/*      */   {
/*      */     try
/*      */     {
/*  465 */       SAXParserFactory factory = FactoryImpl.getSAXFactory(this._useServicesMechanism);
/*      */ 
/*  467 */       if (this._xsltc.isSecureProcessing())
/*      */         try {
/*  469 */           factory.setFeature("http://javax.xml.XMLConstants/feature/secure-processing", true);
/*      */         }
/*      */         catch (SAXException e)
/*      */         {
/*      */         }
/*      */       try {
/*  475 */         factory.setFeature("http://xml.org/sax/features/namespaces", true);
/*      */       }
/*      */       catch (Exception e) {
/*  478 */         factory.setNamespaceAware(true);
/*      */       }
/*  480 */       SAXParser parser = factory.newSAXParser();
/*      */       try {
/*  482 */         parser.setProperty("http://javax.xml.XMLConstants/property/accessExternalDTD", this._xsltc.getProperty("http://javax.xml.XMLConstants/property/accessExternalDTD"));
/*      */       }
/*      */       catch (SAXNotRecognizedException e) {
/*  485 */         ErrorMsg err = new ErrorMsg("WARNING_MSG", parser.getClass().getName() + ": " + e.getMessage());
/*      */ 
/*  487 */         reportError(4, err);
/*      */       }
/*      */ 
/*  490 */       XMLReader reader = parser.getXMLReader();
/*      */       try {
/*  492 */         XMLSecurityManager securityManager = (XMLSecurityManager)this._xsltc.getProperty("http://apache.org/xml/properties/security-manager");
/*      */ 
/*  494 */         for (XMLSecurityManager.Limit limit : XMLSecurityManager.Limit.values()) {
/*  495 */           reader.setProperty(limit.apiProperty(), securityManager.getLimitValueAsString(limit));
/*      */         }
/*  497 */         if (securityManager.printEntityCountInfo())
/*  498 */           parser.setProperty("http://www.oracle.com/xml/jaxp/properties/getEntityCountInfo", "yes");
/*      */       }
/*      */       catch (SAXException se) {
/*  501 */         System.err.println("Warning:  " + reader.getClass().getName() + ": " + se.getMessage());
/*      */       }
/*      */ 
/*  505 */       return parse(reader, input);
/*      */     }
/*      */     catch (ParserConfigurationException e) {
/*  508 */       ErrorMsg err = new ErrorMsg("SAX_PARSER_CONFIG_ERR");
/*  509 */       reportError(3, err);
/*      */     }
/*      */     catch (SAXParseException e) {
/*  512 */       reportError(3, new ErrorMsg(e.getMessage(), e.getLineNumber()));
/*      */     }
/*      */     catch (SAXException e) {
/*  515 */       reportError(3, new ErrorMsg(e.getMessage()));
/*      */     }
/*  517 */     return null;
/*      */   }
/*      */ 
/*      */   public SyntaxTreeNode getDocumentRoot() {
/*  521 */     return this._root;
/*      */   }
/*      */ 
/*      */   protected void setPIParameters(String media, String title, String charset)
/*      */   {
/*  538 */     this._PImedia = media;
/*  539 */     this._PItitle = title;
/*  540 */     this._PIcharset = charset;
/*      */   }
/*      */ 
/*      */   private SyntaxTreeNode getStylesheet(SyntaxTreeNode root)
/*      */     throws CompilerException
/*      */   {
/*  557 */     if (this._target == null) {
/*  558 */       if (!this._rootNamespaceDef) {
/*  559 */         ErrorMsg msg = new ErrorMsg("MISSING_XSLT_URI_ERR");
/*  560 */         throw new CompilerException(msg.toString());
/*      */       }
/*  562 */       return root;
/*      */     }
/*      */ 
/*  566 */     if (this._target.charAt(0) == '#') {
/*  567 */       SyntaxTreeNode element = findStylesheet(root, this._target.substring(1));
/*  568 */       if (element == null) {
/*  569 */         ErrorMsg msg = new ErrorMsg("MISSING_XSLT_TARGET_ERR", this._target, root);
/*      */ 
/*  571 */         throw new CompilerException(msg.toString());
/*      */       }
/*  573 */       return element;
/*      */     }
/*      */     try
/*      */     {
/*  577 */       String path = this._target;
/*  578 */       if (path.indexOf(":") == -1) {
/*  579 */         path = "file:" + path;
/*      */       }
/*  581 */       path = SystemIDResolver.getAbsoluteURI(path);
/*  582 */       String accessError = SecuritySupport.checkAccess(path, (String)this._xsltc.getProperty("http://javax.xml.XMLConstants/property/accessExternalStylesheet"), "all");
/*      */ 
/*  585 */       if (accessError != null) {
/*  586 */         ErrorMsg msg = new ErrorMsg("ACCESSING_XSLT_TARGET_ERR", SecuritySupport.sanitizePath(this._target), accessError, root);
/*      */ 
/*  589 */         throw new CompilerException(msg.toString());
/*      */       }
/*      */     } catch (IOException ex) {
/*  592 */       throw new CompilerException(ex);
/*      */     }
/*      */ 
/*  595 */     return loadExternalStylesheet(this._target);
/*      */   }
/*      */ 
/*      */   private SyntaxTreeNode findStylesheet(SyntaxTreeNode root, String href)
/*      */   {
/*  606 */     if (root == null) return null;
/*      */ 
/*  608 */     if ((root instanceof Stylesheet)) {
/*  609 */       String id = root.getAttribute("id");
/*  610 */       if (id.equals(href)) return root;
/*      */     }
/*  612 */     Vector children = root.getContents();
/*  613 */     if (children != null) {
/*  614 */       int count = children.size();
/*  615 */       for (int i = 0; i < count; i++) {
/*  616 */         SyntaxTreeNode child = (SyntaxTreeNode)children.elementAt(i);
/*  617 */         SyntaxTreeNode node = findStylesheet(child, href);
/*  618 */         if (node != null) return node;
/*      */       }
/*      */     }
/*  621 */     return null;
/*      */   }
/*      */ 
/*      */   private SyntaxTreeNode loadExternalStylesheet(String location)
/*      */     throws CompilerException
/*      */   {
/*      */     InputSource source;
/*      */     InputSource source;
/*  633 */     if (new File(location).exists())
/*  634 */       source = new InputSource("file:" + location);
/*      */     else {
/*  636 */       source = new InputSource(location);
/*      */     }
/*  638 */     SyntaxTreeNode external = parse(source);
/*  639 */     return external;
/*      */   }
/*      */ 
/*      */   private void initAttrTable(String elementName, String[] attrs) {
/*  643 */     this._instructionAttrs.put(getQName("http://www.w3.org/1999/XSL/Transform", "xsl", elementName), attrs);
/*      */   }
/*      */ 
/*      */   private void initInstructionAttrs()
/*      */   {
/*  648 */     initAttrTable("template", new String[] { "match", "name", "priority", "mode" });
/*      */ 
/*  650 */     initAttrTable("stylesheet", new String[] { "id", "version", "extension-element-prefixes", "exclude-result-prefixes" });
/*      */ 
/*  653 */     initAttrTable("transform", new String[] { "id", "version", "extension-element-prefixes", "exclude-result-prefixes" });
/*      */ 
/*  656 */     initAttrTable("text", new String[] { "disable-output-escaping" });
/*  657 */     initAttrTable("if", new String[] { "test" });
/*  658 */     initAttrTable("choose", new String[0]);
/*  659 */     initAttrTable("when", new String[] { "test" });
/*  660 */     initAttrTable("otherwise", new String[0]);
/*  661 */     initAttrTable("for-each", new String[] { "select" });
/*  662 */     initAttrTable("message", new String[] { "terminate" });
/*  663 */     initAttrTable("number", new String[] { "level", "count", "from", "value", "format", "lang", "letter-value", "grouping-separator", "grouping-size" });
/*      */ 
/*  666 */     initAttrTable("comment", new String[0]);
/*  667 */     initAttrTable("copy", new String[] { "use-attribute-sets" });
/*  668 */     initAttrTable("copy-of", new String[] { "select" });
/*  669 */     initAttrTable("param", new String[] { "name", "select" });
/*  670 */     initAttrTable("with-param", new String[] { "name", "select" });
/*  671 */     initAttrTable("variable", new String[] { "name", "select" });
/*  672 */     initAttrTable("output", new String[] { "method", "version", "encoding", "omit-xml-declaration", "standalone", "doctype-public", "doctype-system", "cdata-section-elements", "indent", "media-type" });
/*      */ 
/*  677 */     initAttrTable("sort", new String[] { "select", "order", "case-order", "lang", "data-type" });
/*      */ 
/*  679 */     initAttrTable("key", new String[] { "name", "match", "use" });
/*  680 */     initAttrTable("fallback", new String[0]);
/*  681 */     initAttrTable("attribute", new String[] { "name", "namespace" });
/*  682 */     initAttrTable("attribute-set", new String[] { "name", "use-attribute-sets" });
/*      */ 
/*  684 */     initAttrTable("value-of", new String[] { "select", "disable-output-escaping" });
/*      */ 
/*  686 */     initAttrTable("element", new String[] { "name", "namespace", "use-attribute-sets" });
/*      */ 
/*  688 */     initAttrTable("call-template", new String[] { "name" });
/*  689 */     initAttrTable("apply-templates", new String[] { "select", "mode" });
/*  690 */     initAttrTable("apply-imports", new String[0]);
/*  691 */     initAttrTable("decimal-format", new String[] { "name", "decimal-separator", "grouping-separator", "infinity", "minus-sign", "NaN", "percent", "per-mille", "zero-digit", "digit", "pattern-separator" });
/*      */ 
/*  695 */     initAttrTable("import", new String[] { "href" });
/*  696 */     initAttrTable("include", new String[] { "href" });
/*  697 */     initAttrTable("strip-space", new String[] { "elements" });
/*  698 */     initAttrTable("preserve-space", new String[] { "elements" });
/*  699 */     initAttrTable("processing-instruction", new String[] { "name" });
/*  700 */     initAttrTable("namespace-alias", new String[] { "stylesheet-prefix", "result-prefix" });
/*      */   }
/*      */ 
/*      */   private void initStdClasses()
/*      */   {
/*  711 */     initStdClass("template", "Template");
/*  712 */     initStdClass("stylesheet", "Stylesheet");
/*  713 */     initStdClass("transform", "Stylesheet");
/*  714 */     initStdClass("text", "Text");
/*  715 */     initStdClass("if", "If");
/*  716 */     initStdClass("choose", "Choose");
/*  717 */     initStdClass("when", "When");
/*  718 */     initStdClass("otherwise", "Otherwise");
/*  719 */     initStdClass("for-each", "ForEach");
/*  720 */     initStdClass("message", "Message");
/*  721 */     initStdClass("number", "Number");
/*  722 */     initStdClass("comment", "Comment");
/*  723 */     initStdClass("copy", "Copy");
/*  724 */     initStdClass("copy-of", "CopyOf");
/*  725 */     initStdClass("param", "Param");
/*  726 */     initStdClass("with-param", "WithParam");
/*  727 */     initStdClass("variable", "Variable");
/*  728 */     initStdClass("output", "Output");
/*  729 */     initStdClass("sort", "Sort");
/*  730 */     initStdClass("key", "Key");
/*  731 */     initStdClass("fallback", "Fallback");
/*  732 */     initStdClass("attribute", "XslAttribute");
/*  733 */     initStdClass("attribute-set", "AttributeSet");
/*  734 */     initStdClass("value-of", "ValueOf");
/*  735 */     initStdClass("element", "XslElement");
/*  736 */     initStdClass("call-template", "CallTemplate");
/*  737 */     initStdClass("apply-templates", "ApplyTemplates");
/*  738 */     initStdClass("apply-imports", "ApplyImports");
/*  739 */     initStdClass("decimal-format", "DecimalFormatting");
/*  740 */     initStdClass("import", "Import");
/*  741 */     initStdClass("include", "Include");
/*  742 */     initStdClass("strip-space", "Whitespace");
/*  743 */     initStdClass("preserve-space", "Whitespace");
/*  744 */     initStdClass("processing-instruction", "ProcessingInstruction");
/*  745 */     initStdClass("namespace-alias", "NamespaceAlias");
/*      */   }
/*      */ 
/*      */   private void initStdClass(String elementName, String className) {
/*  749 */     this._instructionClasses.put(getQName("http://www.w3.org/1999/XSL/Transform", "xsl", elementName), "com.sun.org.apache.xalan.internal.xsltc.compiler." + className);
/*      */   }
/*      */ 
/*      */   public boolean elementSupported(String namespace, String localName)
/*      */   {
/*  754 */     return this._instructionClasses.get(getQName(namespace, "xsl", localName)) != null;
/*      */   }
/*      */ 
/*      */   public boolean functionSupported(String fname) {
/*  758 */     return this._symbolTable.lookupPrimop(fname) != null;
/*      */   }
/*      */ 
/*      */   private void initExtClasses() {
/*  762 */     initExtClass("output", "TransletOutput");
/*  763 */     initExtClass("http://xml.apache.org/xalan/redirect", "write", "TransletOutput");
/*      */   }
/*      */ 
/*      */   private void initExtClass(String elementName, String className) {
/*  767 */     this._instructionClasses.put(getQName("http://xml.apache.org/xalan/xsltc", "translet", elementName), "com.sun.org.apache.xalan.internal.xsltc.compiler." + className);
/*      */   }
/*      */ 
/*      */   private void initExtClass(String namespace, String elementName, String className)
/*      */   {
/*  772 */     this._instructionClasses.put(getQName(namespace, "translet", elementName), "com.sun.org.apache.xalan.internal.xsltc.compiler." + className);
/*      */   }
/*      */ 
/*      */   private void initSymbolTable()
/*      */   {
/*  780 */     MethodType I_V = new MethodType(Type.Int, Type.Void);
/*  781 */     MethodType I_R = new MethodType(Type.Int, Type.Real);
/*  782 */     MethodType I_S = new MethodType(Type.Int, Type.String);
/*  783 */     MethodType I_D = new MethodType(Type.Int, Type.NodeSet);
/*  784 */     MethodType R_I = new MethodType(Type.Real, Type.Int);
/*  785 */     MethodType R_V = new MethodType(Type.Real, Type.Void);
/*  786 */     MethodType R_R = new MethodType(Type.Real, Type.Real);
/*  787 */     MethodType R_D = new MethodType(Type.Real, Type.NodeSet);
/*  788 */     MethodType R_O = new MethodType(Type.Real, Type.Reference);
/*  789 */     MethodType I_I = new MethodType(Type.Int, Type.Int);
/*  790 */     MethodType D_O = new MethodType(Type.NodeSet, Type.Reference);
/*  791 */     MethodType D_V = new MethodType(Type.NodeSet, Type.Void);
/*  792 */     MethodType D_S = new MethodType(Type.NodeSet, Type.String);
/*  793 */     MethodType D_D = new MethodType(Type.NodeSet, Type.NodeSet);
/*  794 */     MethodType A_V = new MethodType(Type.Node, Type.Void);
/*  795 */     MethodType S_V = new MethodType(Type.String, Type.Void);
/*  796 */     MethodType S_S = new MethodType(Type.String, Type.String);
/*  797 */     MethodType S_A = new MethodType(Type.String, Type.Node);
/*  798 */     MethodType S_D = new MethodType(Type.String, Type.NodeSet);
/*  799 */     MethodType S_O = new MethodType(Type.String, Type.Reference);
/*  800 */     MethodType B_O = new MethodType(Type.Boolean, Type.Reference);
/*  801 */     MethodType B_V = new MethodType(Type.Boolean, Type.Void);
/*  802 */     MethodType B_B = new MethodType(Type.Boolean, Type.Boolean);
/*  803 */     MethodType B_S = new MethodType(Type.Boolean, Type.String);
/*  804 */     MethodType D_X = new MethodType(Type.NodeSet, Type.Object);
/*  805 */     MethodType R_RR = new MethodType(Type.Real, Type.Real, Type.Real);
/*  806 */     MethodType I_II = new MethodType(Type.Int, Type.Int, Type.Int);
/*  807 */     MethodType B_RR = new MethodType(Type.Boolean, Type.Real, Type.Real);
/*  808 */     MethodType B_II = new MethodType(Type.Boolean, Type.Int, Type.Int);
/*  809 */     MethodType S_SS = new MethodType(Type.String, Type.String, Type.String);
/*  810 */     MethodType S_DS = new MethodType(Type.String, Type.Real, Type.String);
/*  811 */     MethodType S_SR = new MethodType(Type.String, Type.String, Type.Real);
/*  812 */     MethodType O_SO = new MethodType(Type.Reference, Type.String, Type.Reference);
/*      */ 
/*  814 */     MethodType D_SS = new MethodType(Type.NodeSet, Type.String, Type.String);
/*      */ 
/*  816 */     MethodType D_SD = new MethodType(Type.NodeSet, Type.String, Type.NodeSet);
/*      */ 
/*  818 */     MethodType B_BB = new MethodType(Type.Boolean, Type.Boolean, Type.Boolean);
/*      */ 
/*  820 */     MethodType B_SS = new MethodType(Type.Boolean, Type.String, Type.String);
/*      */ 
/*  822 */     MethodType S_SD = new MethodType(Type.String, Type.String, Type.NodeSet);
/*      */ 
/*  824 */     MethodType S_DSS = new MethodType(Type.String, Type.Real, Type.String, Type.String);
/*      */ 
/*  826 */     MethodType S_SRR = new MethodType(Type.String, Type.String, Type.Real, Type.Real);
/*      */ 
/*  828 */     MethodType S_SSS = new MethodType(Type.String, Type.String, Type.String, Type.String);
/*      */ 
/*  839 */     this._symbolTable.addPrimop("current", A_V);
/*  840 */     this._symbolTable.addPrimop("last", I_V);
/*  841 */     this._symbolTable.addPrimop("position", I_V);
/*  842 */     this._symbolTable.addPrimop("true", B_V);
/*  843 */     this._symbolTable.addPrimop("false", B_V);
/*  844 */     this._symbolTable.addPrimop("not", B_B);
/*  845 */     this._symbolTable.addPrimop("name", S_V);
/*  846 */     this._symbolTable.addPrimop("name", S_A);
/*  847 */     this._symbolTable.addPrimop("generate-id", S_V);
/*  848 */     this._symbolTable.addPrimop("generate-id", S_A);
/*  849 */     this._symbolTable.addPrimop("ceiling", R_R);
/*  850 */     this._symbolTable.addPrimop("floor", R_R);
/*  851 */     this._symbolTable.addPrimop("round", R_R);
/*  852 */     this._symbolTable.addPrimop("contains", B_SS);
/*  853 */     this._symbolTable.addPrimop("number", R_O);
/*  854 */     this._symbolTable.addPrimop("number", R_V);
/*  855 */     this._symbolTable.addPrimop("boolean", B_O);
/*  856 */     this._symbolTable.addPrimop("string", S_O);
/*  857 */     this._symbolTable.addPrimop("string", S_V);
/*  858 */     this._symbolTable.addPrimop("translate", S_SSS);
/*  859 */     this._symbolTable.addPrimop("string-length", I_V);
/*  860 */     this._symbolTable.addPrimop("string-length", I_S);
/*  861 */     this._symbolTable.addPrimop("starts-with", B_SS);
/*  862 */     this._symbolTable.addPrimop("format-number", S_DS);
/*  863 */     this._symbolTable.addPrimop("format-number", S_DSS);
/*  864 */     this._symbolTable.addPrimop("unparsed-entity-uri", S_S);
/*  865 */     this._symbolTable.addPrimop("key", D_SS);
/*  866 */     this._symbolTable.addPrimop("key", D_SD);
/*  867 */     this._symbolTable.addPrimop("id", D_S);
/*  868 */     this._symbolTable.addPrimop("id", D_D);
/*  869 */     this._symbolTable.addPrimop("namespace-uri", S_V);
/*  870 */     this._symbolTable.addPrimop("function-available", B_S);
/*  871 */     this._symbolTable.addPrimop("element-available", B_S);
/*  872 */     this._symbolTable.addPrimop("document", D_S);
/*  873 */     this._symbolTable.addPrimop("document", D_V);
/*      */ 
/*  876 */     this._symbolTable.addPrimop("count", I_D);
/*  877 */     this._symbolTable.addPrimop("sum", R_D);
/*  878 */     this._symbolTable.addPrimop("local-name", S_V);
/*  879 */     this._symbolTable.addPrimop("local-name", S_D);
/*  880 */     this._symbolTable.addPrimop("namespace-uri", S_V);
/*  881 */     this._symbolTable.addPrimop("namespace-uri", S_D);
/*  882 */     this._symbolTable.addPrimop("substring", S_SR);
/*  883 */     this._symbolTable.addPrimop("substring", S_SRR);
/*  884 */     this._symbolTable.addPrimop("substring-after", S_SS);
/*  885 */     this._symbolTable.addPrimop("substring-before", S_SS);
/*  886 */     this._symbolTable.addPrimop("normalize-space", S_V);
/*  887 */     this._symbolTable.addPrimop("normalize-space", S_S);
/*  888 */     this._symbolTable.addPrimop("system-property", S_S);
/*      */ 
/*  891 */     this._symbolTable.addPrimop("nodeset", D_O);
/*  892 */     this._symbolTable.addPrimop("objectType", S_O);
/*  893 */     this._symbolTable.addPrimop("cast", O_SO);
/*      */ 
/*  896 */     this._symbolTable.addPrimop("+", R_RR);
/*  897 */     this._symbolTable.addPrimop("-", R_RR);
/*  898 */     this._symbolTable.addPrimop("*", R_RR);
/*  899 */     this._symbolTable.addPrimop("/", R_RR);
/*  900 */     this._symbolTable.addPrimop("%", R_RR);
/*      */ 
/*  904 */     this._symbolTable.addPrimop("+", I_II);
/*  905 */     this._symbolTable.addPrimop("-", I_II);
/*  906 */     this._symbolTable.addPrimop("*", I_II);
/*      */ 
/*  909 */     this._symbolTable.addPrimop("<", B_RR);
/*  910 */     this._symbolTable.addPrimop("<=", B_RR);
/*  911 */     this._symbolTable.addPrimop(">", B_RR);
/*  912 */     this._symbolTable.addPrimop(">=", B_RR);
/*      */ 
/*  915 */     this._symbolTable.addPrimop("<", B_II);
/*  916 */     this._symbolTable.addPrimop("<=", B_II);
/*  917 */     this._symbolTable.addPrimop(">", B_II);
/*  918 */     this._symbolTable.addPrimop(">=", B_II);
/*      */ 
/*  921 */     this._symbolTable.addPrimop("<", B_BB);
/*  922 */     this._symbolTable.addPrimop("<=", B_BB);
/*  923 */     this._symbolTable.addPrimop(">", B_BB);
/*  924 */     this._symbolTable.addPrimop(">=", B_BB);
/*      */ 
/*  927 */     this._symbolTable.addPrimop("or", B_BB);
/*  928 */     this._symbolTable.addPrimop("and", B_BB);
/*      */ 
/*  931 */     this._symbolTable.addPrimop("u-", R_R);
/*  932 */     this._symbolTable.addPrimop("u-", I_I);
/*      */   }
/*      */ 
/*      */   public SymbolTable getSymbolTable() {
/*  936 */     return this._symbolTable;
/*      */   }
/*      */ 
/*      */   public Template getTemplate() {
/*  940 */     return this._template;
/*      */   }
/*      */ 
/*      */   public void setTemplate(Template template) {
/*  944 */     this._template = template;
/*      */   }
/*      */ 
/*      */   public int getTemplateIndex()
/*      */   {
/*  950 */     return this._templateIndex++;
/*      */   }
/*      */ 
/*      */   public SyntaxTreeNode makeInstance(String uri, String prefix, String local, Attributes attributes)
/*      */   {
/*  970 */     SyntaxTreeNode node = null;
/*  971 */     QName qname = getQName(uri, prefix, local);
/*  972 */     String className = (String)this._instructionClasses.get(qname);
/*      */ 
/*  974 */     if (className != null) {
/*      */       try {
/*  976 */         Class clazz = ObjectFactory.findProviderClass(className, true);
/*  977 */         node = (SyntaxTreeNode)clazz.newInstance();
/*  978 */         node.setQName(qname);
/*  979 */         node.setParser(this);
/*  980 */         if (this._locator != null) {
/*  981 */           node.setLineNumber(getLineNumber());
/*      */         }
/*  983 */         if ((node instanceof Stylesheet)) {
/*  984 */           this._xsltc.setStylesheet((Stylesheet)node);
/*      */         }
/*  986 */         checkForSuperfluousAttributes(node, attributes);
/*      */       }
/*      */       catch (ClassNotFoundException e) {
/*  989 */         ErrorMsg err = new ErrorMsg("CLASS_NOT_FOUND_ERR", node);
/*  990 */         reportError(3, err);
/*      */       }
/*      */       catch (Exception e) {
/*  993 */         ErrorMsg err = new ErrorMsg("INTERNAL_ERR", e.getMessage(), node);
/*      */ 
/*  995 */         reportError(2, err);
/*      */       }
/*      */     }
/*      */     else {
/*  999 */       if (uri != null)
/*      */       {
/* 1001 */         if (uri.equals("http://www.w3.org/1999/XSL/Transform")) {
/* 1002 */           node = new UnsupportedElement(uri, prefix, local, false);
/* 1003 */           UnsupportedElement element = (UnsupportedElement)node;
/* 1004 */           ErrorMsg msg = new ErrorMsg("UNSUPPORTED_XSL_ERR", getLineNumber(), local);
/*      */ 
/* 1006 */           element.setErrorMessage(msg);
/* 1007 */           if (this.versionIsOne) {
/* 1008 */             reportError(1, msg);
/*      */           }
/*      */ 
/*      */         }
/* 1012 */         else if (uri.equals("http://xml.apache.org/xalan/xsltc")) {
/* 1013 */           node = new UnsupportedElement(uri, prefix, local, true);
/* 1014 */           UnsupportedElement element = (UnsupportedElement)node;
/* 1015 */           ErrorMsg msg = new ErrorMsg("UNSUPPORTED_EXT_ERR", getLineNumber(), local);
/*      */ 
/* 1017 */           element.setErrorMessage(msg);
/*      */         }
/*      */         else
/*      */         {
/* 1021 */           Stylesheet sheet = this._xsltc.getStylesheet();
/* 1022 */           if ((sheet != null) && (sheet.isExtension(uri)) && 
/* 1023 */             (sheet != (SyntaxTreeNode)this._parentStack.peek())) {
/* 1024 */             node = new UnsupportedElement(uri, prefix, local, true);
/* 1025 */             UnsupportedElement elem = (UnsupportedElement)node;
/* 1026 */             ErrorMsg msg = new ErrorMsg("UNSUPPORTED_EXT_ERR", getLineNumber(), prefix + ":" + local);
/*      */ 
/* 1030 */             elem.setErrorMessage(msg);
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/* 1035 */       if (node == null) {
/* 1036 */         node = new LiteralElement();
/* 1037 */         node.setLineNumber(getLineNumber());
/*      */       }
/*      */     }
/* 1040 */     if ((node != null) && ((node instanceof LiteralElement))) {
/* 1041 */       ((LiteralElement)node).setQName(qname);
/*      */     }
/* 1043 */     return node;
/*      */   }
/*      */ 
/*      */   private void checkForSuperfluousAttributes(SyntaxTreeNode node, Attributes attrs)
/*      */   {
/* 1053 */     QName qname = node.getQName();
/* 1054 */     boolean isStylesheet = node instanceof Stylesheet;
/* 1055 */     String[] legal = (String[])this._instructionAttrs.get(qname);
/* 1056 */     if ((this.versionIsOne) && (legal != null))
/*      */     {
/* 1058 */       int n = attrs.getLength();
/*      */ 
/* 1060 */       for (int i = 0; i < n; i++) {
/* 1061 */         String attrQName = attrs.getQName(i);
/*      */ 
/* 1063 */         if ((isStylesheet) && (attrQName.equals("version"))) {
/* 1064 */           this.versionIsOne = attrs.getValue(i).equals("1.0");
/*      */         }
/*      */ 
/* 1068 */         if ((!attrQName.startsWith("xml")) && (attrQName.indexOf(':') <= 0))
/*      */         {
/* 1071 */           for (int j = 0; (j < legal.length) && 
/* 1072 */             (!attrQName.equalsIgnoreCase(legal[j])); j++);
/* 1076 */           if (j == legal.length) {
/* 1077 */             ErrorMsg err = new ErrorMsg("ILLEGAL_ATTRIBUTE_ERR", attrQName, node);
/*      */ 
/* 1081 */             err.setWarningError(true);
/* 1082 */             reportError(4, err);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public Expression parseExpression(SyntaxTreeNode parent, String exp)
/*      */   {
/* 1095 */     return (Expression)parseTopLevel(parent, "<EXPRESSION>" + exp, null);
/*      */   }
/*      */ 
/*      */   public Expression parseExpression(SyntaxTreeNode parent, String attr, String def)
/*      */   {
/* 1107 */     String exp = parent.getAttribute(attr);
/*      */ 
/* 1109 */     if ((exp.length() == 0) && (def != null)) exp = def;
/*      */ 
/* 1111 */     return (Expression)parseTopLevel(parent, "<EXPRESSION>" + exp, exp);
/*      */   }
/*      */ 
/*      */   public Pattern parsePattern(SyntaxTreeNode parent, String pattern)
/*      */   {
/* 1120 */     return (Pattern)parseTopLevel(parent, "<PATTERN>" + pattern, pattern);
/*      */   }
/*      */ 
/*      */   public Pattern parsePattern(SyntaxTreeNode parent, String attr, String def)
/*      */   {
/* 1132 */     String pattern = parent.getAttribute(attr);
/*      */ 
/* 1134 */     if ((pattern.length() == 0) && (def != null)) pattern = def;
/*      */ 
/* 1136 */     return (Pattern)parseTopLevel(parent, "<PATTERN>" + pattern, pattern);
/*      */   }
/*      */ 
/*      */   private SyntaxTreeNode parseTopLevel(SyntaxTreeNode parent, String text, String expression)
/*      */   {
/* 1145 */     int line = getLineNumber();
/*      */     try
/*      */     {
/* 1148 */       this._xpathParser.setScanner(new XPathLexer(new StringReader(text)));
/* 1149 */       Symbol result = this._xpathParser.parse(expression, line);
/* 1150 */       if (result != null) {
/* 1151 */         SyntaxTreeNode node = (SyntaxTreeNode)result.value;
/* 1152 */         if (node != null) {
/* 1153 */           node.setParser(this);
/* 1154 */           node.setParent(parent);
/* 1155 */           node.setLineNumber(line);
/*      */ 
/* 1157 */           return node;
/*      */         }
/*      */       }
/* 1160 */       reportError(3, new ErrorMsg("XPATH_PARSER_ERR", expression, parent));
/*      */     }
/*      */     catch (Exception e)
/*      */     {
/* 1164 */       if (this._xsltc.debug()) e.printStackTrace();
/* 1165 */       reportError(3, new ErrorMsg("XPATH_PARSER_ERR", expression, parent));
/*      */     }
/*      */ 
/* 1170 */     SyntaxTreeNode.Dummy.setParser(this);
/* 1171 */     return SyntaxTreeNode.Dummy;
/*      */   }
/*      */ 
/*      */   public boolean errorsFound()
/*      */   {
/* 1180 */     return this._errors.size() > 0;
/*      */   }
/*      */ 
/*      */   public void printErrors()
/*      */   {
/* 1187 */     int size = this._errors.size();
/* 1188 */     if (size > 0) {
/* 1189 */       System.err.println(new ErrorMsg("COMPILER_ERROR_KEY"));
/* 1190 */       for (int i = 0; i < size; i++)
/* 1191 */         System.err.println("  " + this._errors.elementAt(i));
/*      */     }
/*      */   }
/*      */ 
/*      */   public void printWarnings()
/*      */   {
/* 1200 */     int size = this._warnings.size();
/* 1201 */     if (size > 0) {
/* 1202 */       System.err.println(new ErrorMsg("COMPILER_WARNING_KEY"));
/* 1203 */       for (int i = 0; i < size; i++)
/* 1204 */         System.err.println("  " + this._warnings.elementAt(i));
/*      */     }
/*      */   }
/*      */ 
/*      */   public void reportError(int category, ErrorMsg error)
/*      */   {
/* 1213 */     switch (category)
/*      */     {
/*      */     case 0:
/* 1217 */       this._errors.addElement(error);
/* 1218 */       break;
/*      */     case 1:
/* 1222 */       this._errors.addElement(error);
/* 1223 */       break;
/*      */     case 2:
/* 1227 */       this._errors.addElement(error);
/* 1228 */       break;
/*      */     case 3:
/* 1232 */       this._errors.addElement(error);
/* 1233 */       break;
/*      */     case 4:
/* 1237 */       this._warnings.addElement(error);
/*      */     }
/*      */   }
/*      */ 
/*      */   public Vector getErrors()
/*      */   {
/* 1243 */     return this._errors;
/*      */   }
/*      */ 
/*      */   public Vector getWarnings() {
/* 1247 */     return this._warnings;
/*      */   }
/*      */ 
/*      */   public void startDocument()
/*      */   {
/* 1259 */     this._root = null;
/* 1260 */     this._target = null;
/* 1261 */     this._prefixMapping = null;
/* 1262 */     this._parentStack = new Stack();
/*      */   }
/*      */ 
/*      */   public void endDocument()
/*      */   {
/*      */   }
/*      */ 
/*      */   public void startPrefixMapping(String prefix, String uri)
/*      */   {
/* 1276 */     if (this._prefixMapping == null) {
/* 1277 */       this._prefixMapping = new Hashtable();
/*      */     }
/* 1279 */     this._prefixMapping.put(prefix, uri);
/*      */   }
/*      */ 
/*      */   public void endPrefixMapping(String prefix)
/*      */   {
/*      */   }
/*      */ 
/*      */   public void startElement(String uri, String localname, String qname, Attributes attributes)
/*      */     throws SAXException
/*      */   {
/* 1296 */     int col = qname.lastIndexOf(':');
/* 1297 */     String prefix = col == -1 ? null : qname.substring(0, col);
/*      */ 
/* 1299 */     SyntaxTreeNode element = makeInstance(uri, prefix, localname, attributes);
/*      */ 
/* 1301 */     if (element == null) {
/* 1302 */       ErrorMsg err = new ErrorMsg("ELEMENT_PARSE_ERR", prefix + ':' + localname);
/*      */ 
/* 1304 */       throw new SAXException(err.toString());
/*      */     }
/*      */ 
/* 1309 */     if (this._root == null) {
/* 1310 */       if ((this._prefixMapping == null) || (!this._prefixMapping.containsValue("http://www.w3.org/1999/XSL/Transform")))
/*      */       {
/* 1312 */         this._rootNamespaceDef = false;
/*      */       }
/* 1314 */       else this._rootNamespaceDef = true;
/* 1315 */       this._root = element;
/*      */     }
/*      */     else {
/* 1318 */       SyntaxTreeNode parent = (SyntaxTreeNode)this._parentStack.peek();
/* 1319 */       parent.addElement(element);
/* 1320 */       element.setParent(parent);
/*      */     }
/* 1322 */     element.setAttributes(new AttributesImpl(attributes));
/* 1323 */     element.setPrefixMapping(this._prefixMapping);
/*      */ 
/* 1325 */     if ((element instanceof Stylesheet))
/*      */     {
/* 1329 */       getSymbolTable().setCurrentNode(element);
/* 1330 */       ((Stylesheet)element).declareExtensionPrefixes(this);
/*      */     }
/*      */ 
/* 1333 */     this._prefixMapping = null;
/* 1334 */     this._parentStack.push(element);
/*      */   }
/*      */ 
/*      */   public void endElement(String uri, String localname, String qname)
/*      */   {
/* 1341 */     this._parentStack.pop();
/*      */   }
/*      */ 
/*      */   public void characters(char[] ch, int start, int length)
/*      */   {
/* 1348 */     String string = new String(ch, start, length);
/* 1349 */     SyntaxTreeNode parent = (SyntaxTreeNode)this._parentStack.peek();
/*      */ 
/* 1351 */     if (string.length() == 0) return;
/*      */ 
/* 1355 */     if ((parent instanceof Text)) {
/* 1356 */       ((Text)parent).setText(string);
/* 1357 */       return;
/*      */     }
/*      */ 
/* 1361 */     if ((parent instanceof Stylesheet)) return;
/*      */ 
/* 1363 */     SyntaxTreeNode bro = parent.lastChild();
/* 1364 */     if ((bro != null) && ((bro instanceof Text))) {
/* 1365 */       Text text = (Text)bro;
/* 1366 */       if ((!text.isTextElement()) && (
/* 1367 */         (length > 1) || (ch[0] < 'Ā'))) {
/* 1368 */         text.setText(string);
/* 1369 */         return;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1375 */     parent.addElement(new Text(string));
/*      */   }
/*      */ 
/*      */   private String getTokenValue(String token) {
/* 1379 */     int start = token.indexOf('"');
/* 1380 */     int stop = token.lastIndexOf('"');
/* 1381 */     return token.substring(start + 1, stop);
/*      */   }
/*      */ 
/*      */   public void processingInstruction(String name, String value)
/*      */   {
/* 1390 */     if ((this._target == null) && (name.equals("xml-stylesheet")))
/*      */     {
/* 1392 */       String href = null;
/* 1393 */       String media = null;
/* 1394 */       String title = null;
/* 1395 */       String charset = null;
/*      */ 
/* 1398 */       StringTokenizer tokens = new StringTokenizer(value);
/* 1399 */       while (tokens.hasMoreElements()) {
/* 1400 */         String token = (String)tokens.nextElement();
/* 1401 */         if (token.startsWith("href"))
/* 1402 */           href = getTokenValue(token);
/* 1403 */         else if (token.startsWith("media"))
/* 1404 */           media = getTokenValue(token);
/* 1405 */         else if (token.startsWith("title"))
/* 1406 */           title = getTokenValue(token);
/* 1407 */         else if (token.startsWith("charset")) {
/* 1408 */           charset = getTokenValue(token);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1413 */       if (((this._PImedia == null) || (this._PImedia.equals(media))) && ((this._PItitle == null) || (this._PImedia.equals(title))) && ((this._PIcharset == null) || (this._PImedia.equals(charset))))
/*      */       {
/* 1416 */         this._target = href;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void ignorableWhitespace(char[] ch, int start, int length)
/*      */   {
/*      */   }
/*      */ 
/*      */   public void skippedEntity(String name)
/*      */   {
/*      */   }
/*      */ 
/*      */   public void setDocumentLocator(Locator locator)
/*      */   {
/* 1436 */     this._locator = locator;
/*      */   }
/*      */ 
/*      */   private int getLineNumber()
/*      */   {
/* 1444 */     int line = 0;
/* 1445 */     if (this._locator != null)
/* 1446 */       line = this._locator.getLineNumber();
/* 1447 */     return line;
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.Parser
 * JD-Core Version:    0.6.2
 */