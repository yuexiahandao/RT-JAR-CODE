/*      */ package com.sun.org.apache.xalan.internal.xsltc.compiler;
/*      */ 
/*      */ import com.sun.org.apache.bcel.internal.generic.ANEWARRAY;
/*      */ import com.sun.org.apache.bcel.internal.generic.BasicType;
/*      */ import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
/*      */ import com.sun.org.apache.bcel.internal.generic.FieldGen;
/*      */ import com.sun.org.apache.bcel.internal.generic.GETFIELD;
/*      */ import com.sun.org.apache.bcel.internal.generic.GETSTATIC;
/*      */ import com.sun.org.apache.bcel.internal.generic.INVOKEINTERFACE;
/*      */ import com.sun.org.apache.bcel.internal.generic.INVOKESPECIAL;
/*      */ import com.sun.org.apache.bcel.internal.generic.INVOKEVIRTUAL;
/*      */ import com.sun.org.apache.bcel.internal.generic.ISTORE;
/*      */ import com.sun.org.apache.bcel.internal.generic.InstructionHandle;
/*      */ import com.sun.org.apache.bcel.internal.generic.InstructionList;
/*      */ import com.sun.org.apache.bcel.internal.generic.LocalVariableGen;
/*      */ import com.sun.org.apache.bcel.internal.generic.NEW;
/*      */ import com.sun.org.apache.bcel.internal.generic.NEWARRAY;
/*      */ import com.sun.org.apache.bcel.internal.generic.PUSH;
/*      */ import com.sun.org.apache.bcel.internal.generic.PUTFIELD;
/*      */ import com.sun.org.apache.bcel.internal.generic.PUTSTATIC;
/*      */ import com.sun.org.apache.bcel.internal.generic.TargetLostException;
/*      */ import com.sun.org.apache.bcel.internal.util.InstructionFinder;
/*      */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
/*      */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
/*      */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
/*      */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
/*      */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util;
/*      */ import com.sun.org.apache.xml.internal.utils.SystemIDResolver;
/*      */ import java.util.Enumeration;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Iterator;
/*      */ import java.util.Properties;
/*      */ import java.util.StringTokenizer;
/*      */ import java.util.Vector;
/*      */ 
/*      */ public final class Stylesheet extends SyntaxTreeNode
/*      */ {
/*      */   private String _version;
/*      */   private QName _name;
/*      */   private String _systemId;
/*      */   private Stylesheet _parentStylesheet;
/*   93 */   private Vector _globals = new Vector();
/*      */ 
/*   98 */   private Boolean _hasLocalParams = null;
/*      */   private String _className;
/*  108 */   private final Vector _templates = new Vector();
/*      */ 
/*  114 */   private Vector _allValidTemplates = null;
/*      */ 
/*  119 */   private int _nextModeSerial = 1;
/*      */ 
/*  124 */   private final Hashtable _modes = new Hashtable();
/*      */   private Mode _defaultMode;
/*  134 */   private final Hashtable _extensions = new Hashtable();
/*      */ 
/*  140 */   public Stylesheet _importedFrom = null;
/*      */ 
/*  146 */   public Stylesheet _includedFrom = null;
/*      */ 
/*  151 */   private Vector _includedStylesheets = null;
/*      */ 
/*  156 */   private int _importPrecedence = 1;
/*      */ 
/*  162 */   private int _minimumDescendantPrecedence = -1;
/*      */ 
/*  167 */   private Hashtable _keys = new Hashtable();
/*      */ 
/*  173 */   private SourceLoader _loader = null;
/*      */ 
/*  178 */   private boolean _numberFormattingUsed = false;
/*      */ 
/*  184 */   private boolean _simplified = false;
/*      */ 
/*  189 */   private boolean _multiDocument = false;
/*      */ 
/*  194 */   private boolean _callsNodeset = false;
/*      */ 
/*  199 */   private boolean _hasIdCall = false;
/*      */ 
/*  205 */   private boolean _templateInlining = false;
/*      */ 
/*  210 */   private Output _lastOutputElement = null;
/*      */ 
/*  215 */   private Properties _outputProperties = null;
/*      */ 
/*  221 */   private int _outputMethod = 0;
/*      */   public static final int UNKNOWN_OUTPUT = 0;
/*      */   public static final int XML_OUTPUT = 1;
/*      */   public static final int HTML_OUTPUT = 2;
/*      */   public static final int TEXT_OUTPUT = 3;
/*      */ 
/*      */   public int getOutputMethod()
/*      */   {
/*  233 */     return this._outputMethod;
/*      */   }
/*      */ 
/*      */   private void checkOutputMethod()
/*      */   {
/*  240 */     if (this._lastOutputElement != null) {
/*  241 */       String method = this._lastOutputElement.getOutputMethod();
/*  242 */       if (method != null)
/*  243 */         if (method.equals("xml"))
/*  244 */           this._outputMethod = 1;
/*  245 */         else if (method.equals("html"))
/*  246 */           this._outputMethod = 2;
/*  247 */         else if (method.equals("text"))
/*  248 */           this._outputMethod = 3;
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean getTemplateInlining()
/*      */   {
/*  254 */     return this._templateInlining;
/*      */   }
/*      */ 
/*      */   public void setTemplateInlining(boolean flag) {
/*  258 */     this._templateInlining = flag;
/*      */   }
/*      */ 
/*      */   public boolean isSimplified() {
/*  262 */     return this._simplified;
/*      */   }
/*      */ 
/*      */   public void setSimplified() {
/*  266 */     this._simplified = true;
/*      */   }
/*      */ 
/*      */   public void setHasIdCall(boolean flag) {
/*  270 */     this._hasIdCall = flag;
/*      */   }
/*      */ 
/*      */   public void setOutputProperty(String key, String value) {
/*  274 */     if (this._outputProperties == null) {
/*  275 */       this._outputProperties = new Properties();
/*      */     }
/*  277 */     this._outputProperties.setProperty(key, value);
/*      */   }
/*      */ 
/*      */   public void setOutputProperties(Properties props) {
/*  281 */     this._outputProperties = props;
/*      */   }
/*      */ 
/*      */   public Properties getOutputProperties() {
/*  285 */     return this._outputProperties;
/*      */   }
/*      */ 
/*      */   public Output getLastOutputElement() {
/*  289 */     return this._lastOutputElement;
/*      */   }
/*      */ 
/*      */   public void setMultiDocument(boolean flag) {
/*  293 */     this._multiDocument = flag;
/*      */   }
/*      */ 
/*      */   public boolean isMultiDocument() {
/*  297 */     return this._multiDocument;
/*      */   }
/*      */ 
/*      */   public void setCallsNodeset(boolean flag) {
/*  301 */     if (flag) setMultiDocument(flag);
/*  302 */     this._callsNodeset = flag;
/*      */   }
/*      */ 
/*      */   public boolean callsNodeset() {
/*  306 */     return this._callsNodeset;
/*      */   }
/*      */ 
/*      */   public void numberFormattingUsed() {
/*  310 */     this._numberFormattingUsed = true;
/*      */ 
/*  317 */     Stylesheet parent = getParentStylesheet();
/*  318 */     if (null != parent) parent.numberFormattingUsed();
/*      */   }
/*      */ 
/*      */   public void setImportPrecedence(int precedence)
/*      */   {
/*  323 */     this._importPrecedence = precedence;
/*      */ 
/*  326 */     Enumeration elements = elements();
/*  327 */     while (elements.hasMoreElements()) {
/*  328 */       SyntaxTreeNode child = (SyntaxTreeNode)elements.nextElement();
/*  329 */       if ((child instanceof Include)) {
/*  330 */         Stylesheet included = ((Include)child).getIncludedStylesheet();
/*  331 */         if ((included != null) && (included._includedFrom == this)) {
/*  332 */           included.setImportPrecedence(precedence);
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  338 */     if (this._importedFrom != null) {
/*  339 */       if (this._importedFrom.getImportPrecedence() < precedence) {
/*  340 */         Parser parser = getParser();
/*  341 */         int nextPrecedence = parser.getNextImportPrecedence();
/*  342 */         this._importedFrom.setImportPrecedence(nextPrecedence);
/*      */       }
/*      */ 
/*      */     }
/*  346 */     else if ((this._includedFrom != null) && 
/*  347 */       (this._includedFrom.getImportPrecedence() != precedence))
/*  348 */       this._includedFrom.setImportPrecedence(precedence);
/*      */   }
/*      */ 
/*      */   public int getImportPrecedence()
/*      */   {
/*  353 */     return this._importPrecedence;
/*      */   }
/*      */ 
/*      */   public int getMinimumDescendantPrecedence()
/*      */   {
/*  362 */     if (this._minimumDescendantPrecedence == -1)
/*      */     {
/*  364 */       int min = getImportPrecedence();
/*      */ 
/*  367 */       int inclImpCount = this._includedStylesheets != null ? this._includedStylesheets.size() : 0;
/*      */ 
/*  371 */       for (int i = 0; i < inclImpCount; i++) {
/*  372 */         int prec = ((Stylesheet)this._includedStylesheets.elementAt(i)).getMinimumDescendantPrecedence();
/*      */ 
/*  375 */         if (prec < min) {
/*  376 */           min = prec;
/*      */         }
/*      */       }
/*      */ 
/*  380 */       this._minimumDescendantPrecedence = min;
/*      */     }
/*  382 */     return this._minimumDescendantPrecedence;
/*      */   }
/*      */ 
/*      */   public boolean checkForLoop(String systemId)
/*      */   {
/*  387 */     if ((this._systemId != null) && (this._systemId.equals(systemId))) {
/*  388 */       return true;
/*      */     }
/*      */ 
/*  391 */     if (this._parentStylesheet != null) {
/*  392 */       return this._parentStylesheet.checkForLoop(systemId);
/*      */     }
/*  394 */     return false;
/*      */   }
/*      */ 
/*      */   public void setParser(Parser parser) {
/*  398 */     super.setParser(parser);
/*  399 */     this._name = makeStylesheetName("__stylesheet_");
/*      */   }
/*      */ 
/*      */   public void setParentStylesheet(Stylesheet parent) {
/*  403 */     this._parentStylesheet = parent;
/*      */   }
/*      */ 
/*      */   public Stylesheet getParentStylesheet() {
/*  407 */     return this._parentStylesheet;
/*      */   }
/*      */ 
/*      */   public void setImportingStylesheet(Stylesheet parent) {
/*  411 */     this._importedFrom = parent;
/*  412 */     parent.addIncludedStylesheet(this);
/*      */   }
/*      */ 
/*      */   public void setIncludingStylesheet(Stylesheet parent) {
/*  416 */     this._includedFrom = parent;
/*  417 */     parent.addIncludedStylesheet(this);
/*      */   }
/*      */ 
/*      */   public void addIncludedStylesheet(Stylesheet child) {
/*  421 */     if (this._includedStylesheets == null) {
/*  422 */       this._includedStylesheets = new Vector();
/*      */     }
/*  424 */     this._includedStylesheets.addElement(child);
/*      */   }
/*      */ 
/*      */   public void setSystemId(String systemId) {
/*  428 */     if (systemId != null)
/*  429 */       this._systemId = SystemIDResolver.getAbsoluteURI(systemId);
/*      */   }
/*      */ 
/*      */   public String getSystemId()
/*      */   {
/*  434 */     return this._systemId;
/*      */   }
/*      */ 
/*      */   public void setSourceLoader(SourceLoader loader) {
/*  438 */     this._loader = loader;
/*      */   }
/*      */ 
/*      */   public SourceLoader getSourceLoader() {
/*  442 */     return this._loader;
/*      */   }
/*      */ 
/*      */   private QName makeStylesheetName(String prefix) {
/*  446 */     return getParser().getQName(prefix + getXSLTC().nextStylesheetSerial());
/*      */   }
/*      */ 
/*      */   public boolean hasGlobals()
/*      */   {
/*  453 */     return this._globals.size() > 0;
/*      */   }
/*      */ 
/*      */   public boolean hasLocalParams()
/*      */   {
/*  462 */     if (this._hasLocalParams == null) {
/*  463 */       Vector templates = getAllValidTemplates();
/*  464 */       int n = templates.size();
/*  465 */       for (int i = 0; i < n; i++) {
/*  466 */         Template template = (Template)templates.elementAt(i);
/*  467 */         if (template.hasParams()) {
/*  468 */           this._hasLocalParams = Boolean.TRUE;
/*  469 */           return true;
/*      */         }
/*      */       }
/*  472 */       this._hasLocalParams = Boolean.FALSE;
/*  473 */       return false;
/*      */     }
/*      */ 
/*  476 */     return this._hasLocalParams.booleanValue();
/*      */   }
/*      */ 
/*      */   protected void addPrefixMapping(String prefix, String uri)
/*      */   {
/*  486 */     if ((prefix.equals("")) && (uri.equals("http://www.w3.org/1999/xhtml"))) return;
/*  487 */     super.addPrefixMapping(prefix, uri);
/*      */   }
/*      */ 
/*      */   private void extensionURI(String prefixes, SymbolTable stable)
/*      */   {
/*  494 */     if (prefixes != null) {
/*  495 */       StringTokenizer tokens = new StringTokenizer(prefixes);
/*  496 */       while (tokens.hasMoreTokens()) {
/*  497 */         String prefix = tokens.nextToken();
/*  498 */         String uri = lookupNamespace(prefix);
/*  499 */         if (uri != null)
/*  500 */           this._extensions.put(uri, prefix);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean isExtension(String uri)
/*      */   {
/*  507 */     return this._extensions.get(uri) != null;
/*      */   }
/*      */ 
/*      */   public void declareExtensionPrefixes(Parser parser) {
/*  511 */     SymbolTable stable = parser.getSymbolTable();
/*  512 */     String extensionPrefixes = getAttribute("extension-element-prefixes");
/*  513 */     extensionURI(extensionPrefixes, stable);
/*      */   }
/*      */ 
/*      */   public void parseContents(Parser parser)
/*      */   {
/*  522 */     SymbolTable stable = parser.getSymbolTable();
/*      */ 
/*  536 */     addPrefixMapping("xml", "http://www.w3.org/XML/1998/namespace");
/*      */ 
/*  539 */     Stylesheet sheet = stable.addStylesheet(this._name, this);
/*  540 */     if (sheet != null)
/*      */     {
/*  542 */       ErrorMsg err = new ErrorMsg("MULTIPLE_STYLESHEET_ERR", this);
/*  543 */       parser.reportError(3, err);
/*      */     }
/*      */ 
/*  551 */     if (this._simplified) {
/*  552 */       stable.excludeURI("http://www.w3.org/1999/XSL/Transform");
/*  553 */       Template template = new Template();
/*  554 */       template.parseSimplified(this, parser);
/*      */     }
/*      */     else
/*      */     {
/*  558 */       parseOwnChildren(parser);
/*      */     }
/*      */   }
/*      */ 
/*      */   public final void parseOwnChildren(Parser parser)
/*      */   {
/*  566 */     SymbolTable stable = parser.getSymbolTable();
/*  567 */     String excludePrefixes = getAttribute("exclude-result-prefixes");
/*  568 */     String extensionPrefixes = getAttribute("extension-element-prefixes");
/*      */ 
/*  571 */     stable.pushExcludedNamespacesContext();
/*  572 */     stable.excludeURI("http://www.w3.org/1999/XSL/Transform");
/*  573 */     stable.excludeNamespaces(excludePrefixes);
/*  574 */     stable.excludeNamespaces(extensionPrefixes);
/*      */ 
/*  576 */     Vector contents = getContents();
/*  577 */     int count = contents.size();
/*      */ 
/*  581 */     for (int i = 0; i < count; i++) {
/*  582 */       SyntaxTreeNode child = (SyntaxTreeNode)contents.elementAt(i);
/*  583 */       if (((child instanceof VariableBase)) || ((child instanceof NamespaceAlias)))
/*      */       {
/*  585 */         parser.getSymbolTable().setCurrentNode(child);
/*  586 */         child.parseContents(parser);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  591 */     for (int i = 0; i < count; i++) {
/*  592 */       SyntaxTreeNode child = (SyntaxTreeNode)contents.elementAt(i);
/*  593 */       if ((!(child instanceof VariableBase)) && (!(child instanceof NamespaceAlias)))
/*      */       {
/*  595 */         parser.getSymbolTable().setCurrentNode(child);
/*  596 */         child.parseContents(parser);
/*      */       }
/*      */ 
/*  601 */       if ((!this._templateInlining) && ((child instanceof Template))) {
/*  602 */         Template template = (Template)child;
/*  603 */         String name = "template$dot$" + template.getPosition();
/*  604 */         template.setName(parser.getQName(name));
/*      */       }
/*      */     }
/*      */ 
/*  608 */     stable.popExcludedNamespacesContext();
/*      */   }
/*      */ 
/*      */   public void processModes() {
/*  612 */     if (this._defaultMode == null)
/*  613 */       this._defaultMode = new Mode(null, this, "");
/*  614 */     this._defaultMode.processPatterns(this._keys);
/*  615 */     Enumeration modes = this._modes.elements();
/*  616 */     while (modes.hasMoreElements()) {
/*  617 */       Mode mode = (Mode)modes.nextElement();
/*  618 */       mode.processPatterns(this._keys);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void compileModes(ClassGenerator classGen) {
/*  623 */     this._defaultMode.compileApplyTemplates(classGen);
/*  624 */     Enumeration modes = this._modes.elements();
/*  625 */     while (modes.hasMoreElements()) {
/*  626 */       Mode mode = (Mode)modes.nextElement();
/*  627 */       mode.compileApplyTemplates(classGen);
/*      */     }
/*      */   }
/*      */ 
/*      */   public Mode getMode(QName modeName) {
/*  632 */     if (modeName == null) {
/*  633 */       if (this._defaultMode == null) {
/*  634 */         this._defaultMode = new Mode(null, this, "");
/*      */       }
/*  636 */       return this._defaultMode;
/*      */     }
/*      */ 
/*  639 */     Mode mode = (Mode)this._modes.get(modeName);
/*  640 */     if (mode == null) {
/*  641 */       String suffix = Integer.toString(this._nextModeSerial++);
/*  642 */       this._modes.put(modeName, mode = new Mode(modeName, this, suffix));
/*      */     }
/*  644 */     return mode;
/*      */   }
/*      */ 
/*      */   public com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type typeCheck(SymbolTable stable)
/*      */     throws TypeCheckError
/*      */   {
/*  652 */     int count = this._globals.size();
/*  653 */     for (int i = 0; i < count; i++) {
/*  654 */       VariableBase var = (VariableBase)this._globals.elementAt(i);
/*  655 */       var.typeCheck(stable);
/*      */     }
/*  657 */     return typeCheckContents(stable);
/*      */   }
/*      */ 
/*      */   public void translate(ClassGenerator classGen, MethodGenerator methodGen)
/*      */   {
/*  664 */     translate();
/*      */   }
/*      */ 
/*      */   private void addDOMField(ClassGenerator classGen) {
/*  668 */     FieldGen fgen = new FieldGen(1, Util.getJCRefType("Lcom/sun/org/apache/xalan/internal/xsltc/DOM;"), "_dom", classGen.getConstantPool());
/*      */ 
/*  672 */     classGen.addField(fgen.getField());
/*      */   }
/*      */ 
/*      */   private void addStaticField(ClassGenerator classGen, String type, String name)
/*      */   {
/*  681 */     FieldGen fgen = new FieldGen(12, Util.getJCRefType(type), name, classGen.getConstantPool());
/*      */ 
/*  685 */     classGen.addField(fgen.getField());
/*      */   }
/*      */ 
/*      */   public void translate()
/*      */   {
/*  693 */     this._className = getXSLTC().getClassName();
/*      */ 
/*  696 */     ClassGenerator classGen = new ClassGenerator(this._className, "com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet", "", 33, null, this);
/*      */ 
/*  703 */     addDOMField(classGen);
/*      */ 
/*  707 */     compileTransform(classGen);
/*      */ 
/*  710 */     Enumeration elements = elements();
/*  711 */     while (elements.hasMoreElements()) {
/*  712 */       Object element = elements.nextElement();
/*      */ 
/*  714 */       if ((element instanceof Template))
/*      */       {
/*  716 */         Template template = (Template)element;
/*      */ 
/*  718 */         getMode(template.getModeName()).addTemplate(template);
/*      */       }
/*  721 */       else if ((element instanceof AttributeSet)) {
/*  722 */         ((AttributeSet)element).translate(classGen, null);
/*      */       }
/*  724 */       else if ((element instanceof Output))
/*      */       {
/*  726 */         Output output = (Output)element;
/*  727 */         if (output.enabled()) this._lastOutputElement = output;
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  736 */     checkOutputMethod();
/*  737 */     processModes();
/*  738 */     compileModes(classGen);
/*  739 */     compileStaticInitializer(classGen);
/*  740 */     compileConstructor(classGen, this._lastOutputElement);
/*      */ 
/*  742 */     if (!getParser().errorsFound())
/*  743 */       getXSLTC().dumpClass(classGen.getJavaClass());
/*      */   }
/*      */ 
/*      */   private void compileStaticInitializer(ClassGenerator classGen)
/*      */   {
/*  754 */     ConstantPoolGen cpg = classGen.getConstantPool();
/*  755 */     InstructionList il = new InstructionList();
/*      */ 
/*  757 */     MethodGenerator staticConst = new MethodGenerator(9, com.sun.org.apache.bcel.internal.generic.Type.VOID, null, null, "<clinit>", this._className, il, cpg);
/*      */ 
/*  763 */     addStaticField(classGen, "[Ljava/lang/String;", "_sNamesArray");
/*  764 */     addStaticField(classGen, "[Ljava/lang/String;", "_sUrisArray");
/*  765 */     addStaticField(classGen, "[I", "_sTypesArray");
/*  766 */     addStaticField(classGen, "[Ljava/lang/String;", "_sNamespaceArray");
/*      */ 
/*  769 */     int charDataFieldCount = getXSLTC().getCharacterDataCount();
/*  770 */     for (int i = 0; i < charDataFieldCount; i++) {
/*  771 */       addStaticField(classGen, "[C", "_scharData" + i);
/*      */     }
/*      */ 
/*  776 */     Vector namesIndex = getXSLTC().getNamesIndex();
/*  777 */     int size = namesIndex.size();
/*  778 */     String[] namesArray = new String[size];
/*  779 */     String[] urisArray = new String[size];
/*  780 */     int[] typesArray = new int[size];
/*      */ 
/*  783 */     for (int i = 0; i < size; i++) {
/*  784 */       String encodedName = (String)namesIndex.elementAt(i);
/*      */       int index;
/*  785 */       if ((index = encodedName.lastIndexOf(':')) > -1) {
/*  786 */         urisArray[i] = encodedName.substring(0, index);
/*      */       }
/*      */ 
/*  789 */       index += 1;
/*  790 */       if (encodedName.charAt(index) == '@') {
/*  791 */         typesArray[i] = 2;
/*  792 */         index++;
/*  793 */       } else if (encodedName.charAt(index) == '?') {
/*  794 */         typesArray[i] = 13;
/*  795 */         index++;
/*      */       } else {
/*  797 */         typesArray[i] = 1;
/*      */       }
/*      */ 
/*  800 */       if (index == 0) {
/*  801 */         namesArray[i] = encodedName;
/*      */       }
/*      */       else {
/*  804 */         namesArray[i] = encodedName.substring(index);
/*      */       }
/*      */     }
/*      */ 
/*  808 */     staticConst.markChunkStart();
/*  809 */     il.append(new PUSH(cpg, size));
/*  810 */     il.append(new ANEWARRAY(cpg.addClass("java.lang.String")));
/*  811 */     int namesArrayRef = cpg.addFieldref(this._className, "_sNamesArray", "[Ljava/lang/String;");
/*      */ 
/*  814 */     il.append(new PUTSTATIC(namesArrayRef));
/*  815 */     staticConst.markChunkEnd();
/*      */ 
/*  817 */     for (int i = 0; i < size; i++) {
/*  818 */       String name = namesArray[i];
/*  819 */       staticConst.markChunkStart();
/*  820 */       il.append(new GETSTATIC(namesArrayRef));
/*  821 */       il.append(new PUSH(cpg, i));
/*  822 */       il.append(new PUSH(cpg, name));
/*  823 */       il.append(AASTORE);
/*  824 */       staticConst.markChunkEnd();
/*      */     }
/*      */ 
/*  827 */     staticConst.markChunkStart();
/*  828 */     il.append(new PUSH(cpg, size));
/*  829 */     il.append(new ANEWARRAY(cpg.addClass("java.lang.String")));
/*  830 */     int urisArrayRef = cpg.addFieldref(this._className, "_sUrisArray", "[Ljava/lang/String;");
/*      */ 
/*  833 */     il.append(new PUTSTATIC(urisArrayRef));
/*  834 */     staticConst.markChunkEnd();
/*      */ 
/*  836 */     for (int i = 0; i < size; i++) {
/*  837 */       String uri = urisArray[i];
/*  838 */       staticConst.markChunkStart();
/*  839 */       il.append(new GETSTATIC(urisArrayRef));
/*  840 */       il.append(new PUSH(cpg, i));
/*  841 */       il.append(new PUSH(cpg, uri));
/*  842 */       il.append(AASTORE);
/*  843 */       staticConst.markChunkEnd();
/*      */     }
/*      */ 
/*  846 */     staticConst.markChunkStart();
/*  847 */     il.append(new PUSH(cpg, size));
/*  848 */     il.append(new NEWARRAY(BasicType.INT));
/*  849 */     int typesArrayRef = cpg.addFieldref(this._className, "_sTypesArray", "[I");
/*      */ 
/*  852 */     il.append(new PUTSTATIC(typesArrayRef));
/*  853 */     staticConst.markChunkEnd();
/*      */ 
/*  855 */     for (int i = 0; i < size; i++) {
/*  856 */       int nodeType = typesArray[i];
/*  857 */       staticConst.markChunkStart();
/*  858 */       il.append(new GETSTATIC(typesArrayRef));
/*  859 */       il.append(new PUSH(cpg, i));
/*  860 */       il.append(new PUSH(cpg, nodeType));
/*  861 */       il.append(IASTORE);
/*      */     }
/*      */ 
/*  865 */     Vector namespaces = getXSLTC().getNamespaceIndex();
/*  866 */     staticConst.markChunkStart();
/*  867 */     il.append(new PUSH(cpg, namespaces.size()));
/*  868 */     il.append(new ANEWARRAY(cpg.addClass("java.lang.String")));
/*  869 */     int namespaceArrayRef = cpg.addFieldref(this._className, "_sNamespaceArray", "[Ljava/lang/String;");
/*      */ 
/*  872 */     il.append(new PUTSTATIC(namespaceArrayRef));
/*  873 */     staticConst.markChunkEnd();
/*      */ 
/*  875 */     for (int i = 0; i < namespaces.size(); i++) {
/*  876 */       String ns = (String)namespaces.elementAt(i);
/*  877 */       staticConst.markChunkStart();
/*  878 */       il.append(new GETSTATIC(namespaceArrayRef));
/*  879 */       il.append(new PUSH(cpg, i));
/*  880 */       il.append(new PUSH(cpg, ns));
/*  881 */       il.append(AASTORE);
/*  882 */       staticConst.markChunkEnd();
/*      */     }
/*      */ 
/*  886 */     int charDataCount = getXSLTC().getCharacterDataCount();
/*  887 */     int toCharArray = cpg.addMethodref("java.lang.String", "toCharArray", "()[C");
/*  888 */     for (int i = 0; i < charDataCount; i++) {
/*  889 */       staticConst.markChunkStart();
/*  890 */       il.append(new PUSH(cpg, getXSLTC().getCharacterData(i)));
/*  891 */       il.append(new INVOKEVIRTUAL(toCharArray));
/*  892 */       il.append(new PUTSTATIC(cpg.addFieldref(this._className, "_scharData" + i, "[C")));
/*      */ 
/*  895 */       staticConst.markChunkEnd();
/*      */     }
/*      */ 
/*  898 */     il.append(RETURN);
/*      */ 
/*  900 */     classGen.addMethod(staticConst);
/*      */   }
/*      */ 
/*      */   private void compileConstructor(ClassGenerator classGen, Output output)
/*      */   {
/*  909 */     ConstantPoolGen cpg = classGen.getConstantPool();
/*  910 */     InstructionList il = new InstructionList();
/*      */ 
/*  912 */     MethodGenerator constructor = new MethodGenerator(1, com.sun.org.apache.bcel.internal.generic.Type.VOID, null, null, "<init>", this._className, il, cpg);
/*      */ 
/*  919 */     il.append(classGen.loadTranslet());
/*  920 */     il.append(new INVOKESPECIAL(cpg.addMethodref("com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet", "<init>", "()V")));
/*      */ 
/*  923 */     constructor.markChunkStart();
/*  924 */     il.append(classGen.loadTranslet());
/*  925 */     il.append(new GETSTATIC(cpg.addFieldref(this._className, "_sNamesArray", "[Ljava/lang/String;")));
/*      */ 
/*  928 */     il.append(new PUTFIELD(cpg.addFieldref("com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet", "namesArray", "[Ljava/lang/String;")));
/*      */ 
/*  932 */     il.append(classGen.loadTranslet());
/*  933 */     il.append(new GETSTATIC(cpg.addFieldref(this._className, "_sUrisArray", "[Ljava/lang/String;")));
/*      */ 
/*  936 */     il.append(new PUTFIELD(cpg.addFieldref("com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet", "urisArray", "[Ljava/lang/String;")));
/*      */ 
/*  939 */     constructor.markChunkEnd();
/*      */ 
/*  941 */     constructor.markChunkStart();
/*  942 */     il.append(classGen.loadTranslet());
/*  943 */     il.append(new GETSTATIC(cpg.addFieldref(this._className, "_sTypesArray", "[I")));
/*      */ 
/*  946 */     il.append(new PUTFIELD(cpg.addFieldref("com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet", "typesArray", "[I")));
/*      */ 
/*  949 */     constructor.markChunkEnd();
/*      */ 
/*  951 */     constructor.markChunkStart();
/*  952 */     il.append(classGen.loadTranslet());
/*  953 */     il.append(new GETSTATIC(cpg.addFieldref(this._className, "_sNamespaceArray", "[Ljava/lang/String;")));
/*      */ 
/*  956 */     il.append(new PUTFIELD(cpg.addFieldref("com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet", "namespaceArray", "[Ljava/lang/String;")));
/*      */ 
/*  959 */     constructor.markChunkEnd();
/*      */ 
/*  961 */     constructor.markChunkStart();
/*  962 */     il.append(classGen.loadTranslet());
/*  963 */     il.append(new PUSH(cpg, 101));
/*  964 */     il.append(new PUTFIELD(cpg.addFieldref("com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet", "transletVersion", "I")));
/*      */ 
/*  967 */     constructor.markChunkEnd();
/*      */ 
/*  969 */     if (this._hasIdCall) {
/*  970 */       constructor.markChunkStart();
/*  971 */       il.append(classGen.loadTranslet());
/*  972 */       il.append(new PUSH(cpg, Boolean.TRUE));
/*  973 */       il.append(new PUTFIELD(cpg.addFieldref("com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet", "_hasIdCall", "Z")));
/*      */ 
/*  976 */       constructor.markChunkEnd();
/*      */     }
/*      */ 
/*  980 */     if (output != null)
/*      */     {
/*  982 */       constructor.markChunkStart();
/*  983 */       output.translate(classGen, constructor);
/*  984 */       constructor.markChunkEnd();
/*      */     }
/*      */ 
/*  989 */     if (this._numberFormattingUsed) {
/*  990 */       constructor.markChunkStart();
/*  991 */       DecimalFormatting.translateDefaultDFS(classGen, constructor);
/*  992 */       constructor.markChunkEnd();
/*      */     }
/*      */ 
/*  995 */     il.append(RETURN);
/*      */ 
/*  997 */     classGen.addMethod(constructor);
/*      */   }
/*      */ 
/*      */   private String compileTopLevel(ClassGenerator classGen)
/*      */   {
/* 1014 */     ConstantPoolGen cpg = classGen.getConstantPool();
/*      */ 
/* 1016 */     com.sun.org.apache.bcel.internal.generic.Type[] argTypes = { Util.getJCRefType("Lcom/sun/org/apache/xalan/internal/xsltc/DOM;"), Util.getJCRefType("Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;"), Util.getJCRefType("Lcom/sun/org/apache/xml/internal/serializer/SerializationHandler;") };
/*      */ 
/* 1022 */     String[] argNames = { "document", "iterator", "handler" };
/*      */ 
/* 1026 */     InstructionList il = new InstructionList();
/*      */ 
/* 1028 */     MethodGenerator toplevel = new MethodGenerator(1, com.sun.org.apache.bcel.internal.generic.Type.VOID, argTypes, argNames, "topLevel", this._className, il, classGen.getConstantPool());
/*      */ 
/* 1035 */     toplevel.addException("com.sun.org.apache.xalan.internal.xsltc.TransletException");
/*      */ 
/* 1038 */     LocalVariableGen current = toplevel.addLocalVariable("current", com.sun.org.apache.bcel.internal.generic.Type.INT, null, null);
/*      */ 
/* 1043 */     int setFilter = cpg.addInterfaceMethodref("com.sun.org.apache.xalan.internal.xsltc.DOM", "setFilter", "(Lcom/sun/org/apache/xalan/internal/xsltc/StripFilter;)V");
/*      */ 
/* 1047 */     int gitr = cpg.addInterfaceMethodref("com.sun.org.apache.xalan.internal.xsltc.DOM", "getIterator", "()Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;");
/*      */ 
/* 1050 */     il.append(toplevel.loadDOM());
/* 1051 */     il.append(new INVOKEINTERFACE(gitr, 1));
/* 1052 */     il.append(toplevel.nextNode());
/* 1053 */     current.setStart(il.append(new ISTORE(current.getIndex())));
/*      */ 
/* 1056 */     Vector varDepElements = new Vector(this._globals);
/* 1057 */     Enumeration elements = elements();
/* 1058 */     while (elements.hasMoreElements()) {
/* 1059 */       Object element = elements.nextElement();
/* 1060 */       if ((element instanceof Key)) {
/* 1061 */         varDepElements.add(element);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1066 */     varDepElements = resolveDependencies(varDepElements);
/*      */ 
/* 1069 */     int count = varDepElements.size();
/* 1070 */     for (int i = 0; i < count; i++) {
/* 1071 */       TopLevelElement tle = (TopLevelElement)varDepElements.elementAt(i);
/* 1072 */       tle.translate(classGen, toplevel);
/* 1073 */       if ((tle instanceof Key)) {
/* 1074 */         Key key = (Key)tle;
/* 1075 */         this._keys.put(key.getName(), key);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1080 */     Vector whitespaceRules = new Vector();
/* 1081 */     elements = elements();
/* 1082 */     while (elements.hasMoreElements()) {
/* 1083 */       Object element = elements.nextElement();
/*      */ 
/* 1085 */       if ((element instanceof DecimalFormatting)) {
/* 1086 */         ((DecimalFormatting)element).translate(classGen, toplevel);
/*      */       }
/* 1089 */       else if ((element instanceof Whitespace)) {
/* 1090 */         whitespaceRules.addAll(((Whitespace)element).getRules());
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1095 */     if (whitespaceRules.size() > 0) {
/* 1096 */       Whitespace.translateRules(whitespaceRules, classGen);
/*      */     }
/*      */ 
/* 1099 */     if (classGen.containsMethod("stripSpace", "(Lcom/sun/org/apache/xalan/internal/xsltc/DOM;II)Z") != null) {
/* 1100 */       il.append(toplevel.loadDOM());
/* 1101 */       il.append(classGen.loadTranslet());
/* 1102 */       il.append(new INVOKEINTERFACE(setFilter, 2));
/*      */     }
/*      */ 
/* 1105 */     il.append(RETURN);
/*      */ 
/* 1108 */     classGen.addMethod(toplevel);
/*      */ 
/* 1110 */     return "(Lcom/sun/org/apache/xalan/internal/xsltc/DOM;Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;Lcom/sun/org/apache/xml/internal/serializer/SerializationHandler;)V";
/*      */   }
/*      */ 
/*      */   private Vector resolveDependencies(Vector input)
/*      */   {
/* 1134 */     Vector result = new Vector();
/* 1135 */     while (input.size() > 0) {
/* 1136 */       boolean changed = false;
/* 1137 */       for (int i = 0; i < input.size(); ) {
/* 1138 */         TopLevelElement vde = (TopLevelElement)input.elementAt(i);
/* 1139 */         Vector dep = vde.getDependencies();
/* 1140 */         if ((dep == null) || (result.containsAll(dep))) {
/* 1141 */           result.addElement(vde);
/* 1142 */           input.remove(i);
/* 1143 */           changed = true;
/*      */         }
/*      */         else {
/* 1146 */           i++;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1151 */       if (!changed) {
/* 1152 */         ErrorMsg err = new ErrorMsg("CIRCULAR_VARIABLE_ERR", input.toString(), this);
/*      */ 
/* 1154 */         getParser().reportError(3, err);
/* 1155 */         return result;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1167 */     return result;
/*      */   }
/*      */ 
/*      */   private String compileBuildKeys(ClassGenerator classGen)
/*      */   {
/* 1177 */     ConstantPoolGen cpg = classGen.getConstantPool();
/*      */ 
/* 1179 */     com.sun.org.apache.bcel.internal.generic.Type[] argTypes = { Util.getJCRefType("Lcom/sun/org/apache/xalan/internal/xsltc/DOM;"), Util.getJCRefType("Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;"), Util.getJCRefType("Lcom/sun/org/apache/xml/internal/serializer/SerializationHandler;"), com.sun.org.apache.bcel.internal.generic.Type.INT };
/*      */ 
/* 1186 */     String[] argNames = { "document", "iterator", "handler", "current" };
/*      */ 
/* 1190 */     InstructionList il = new InstructionList();
/*      */ 
/* 1192 */     MethodGenerator buildKeys = new MethodGenerator(1, com.sun.org.apache.bcel.internal.generic.Type.VOID, argTypes, argNames, "buildKeys", this._className, il, classGen.getConstantPool());
/*      */ 
/* 1199 */     buildKeys.addException("com.sun.org.apache.xalan.internal.xsltc.TransletException");
/*      */ 
/* 1201 */     Enumeration elements = elements();
/* 1202 */     while (elements.hasMoreElements())
/*      */     {
/* 1204 */       Object element = elements.nextElement();
/* 1205 */       if ((element instanceof Key)) {
/* 1206 */         Key key = (Key)element;
/* 1207 */         key.translate(classGen, buildKeys);
/* 1208 */         this._keys.put(key.getName(), key);
/*      */       }
/*      */     }
/*      */ 
/* 1212 */     il.append(RETURN);
/*      */ 
/* 1215 */     buildKeys.stripAttributes(true);
/* 1216 */     buildKeys.setMaxLocals();
/* 1217 */     buildKeys.setMaxStack();
/* 1218 */     buildKeys.removeNOPs();
/*      */ 
/* 1220 */     classGen.addMethod(buildKeys.getMethod());
/*      */ 
/* 1222 */     return "(Lcom/sun/org/apache/xalan/internal/xsltc/DOM;Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;Lcom/sun/org/apache/xml/internal/serializer/SerializationHandler;I)V";
/*      */   }
/*      */ 
/*      */   private void compileTransform(ClassGenerator classGen)
/*      */   {
/* 1231 */     ConstantPoolGen cpg = classGen.getConstantPool();
/*      */ 
/* 1237 */     com.sun.org.apache.bcel.internal.generic.Type[] argTypes = new com.sun.org.apache.bcel.internal.generic.Type[3];
/*      */ 
/* 1239 */     argTypes[0] = Util.getJCRefType("Lcom/sun/org/apache/xalan/internal/xsltc/DOM;");
/* 1240 */     argTypes[1] = Util.getJCRefType("Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;");
/* 1241 */     argTypes[2] = Util.getJCRefType("Lcom/sun/org/apache/xml/internal/serializer/SerializationHandler;");
/*      */ 
/* 1243 */     String[] argNames = new String[3];
/* 1244 */     argNames[0] = "document";
/* 1245 */     argNames[1] = "iterator";
/* 1246 */     argNames[2] = "handler";
/*      */ 
/* 1248 */     InstructionList il = new InstructionList();
/* 1249 */     MethodGenerator transf = new MethodGenerator(1, com.sun.org.apache.bcel.internal.generic.Type.VOID, argTypes, argNames, "transform", this._className, il, classGen.getConstantPool());
/*      */ 
/* 1257 */     transf.addException("com.sun.org.apache.xalan.internal.xsltc.TransletException");
/*      */ 
/* 1260 */     LocalVariableGen current = transf.addLocalVariable("current", com.sun.org.apache.bcel.internal.generic.Type.INT, null, null);
/*      */ 
/* 1264 */     String applyTemplatesSig = classGen.getApplyTemplatesSig();
/* 1265 */     int applyTemplates = cpg.addMethodref(getClassName(), "applyTemplates", applyTemplatesSig);
/*      */ 
/* 1268 */     int domField = cpg.addFieldref(getClassName(), "_dom", "Lcom/sun/org/apache/xalan/internal/xsltc/DOM;");
/*      */ 
/* 1273 */     il.append(classGen.loadTranslet());
/*      */ 
/* 1276 */     if (isMultiDocument()) {
/* 1277 */       il.append(new NEW(cpg.addClass("com.sun.org.apache.xalan.internal.xsltc.dom.MultiDOM")));
/* 1278 */       il.append(DUP);
/*      */     }
/*      */ 
/* 1281 */     il.append(classGen.loadTranslet());
/* 1282 */     il.append(transf.loadDOM());
/* 1283 */     il.append(new INVOKEVIRTUAL(cpg.addMethodref("com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet", "makeDOMAdapter", "(Lcom/sun/org/apache/xalan/internal/xsltc/DOM;)Lcom/sun/org/apache/xalan/internal/xsltc/dom/DOMAdapter;")));
/*      */ 
/* 1289 */     if (isMultiDocument()) {
/* 1290 */       int init = cpg.addMethodref("com.sun.org.apache.xalan.internal.xsltc.dom.MultiDOM", "<init>", "(Lcom/sun/org/apache/xalan/internal/xsltc/DOM;)V");
/*      */ 
/* 1293 */       il.append(new INVOKESPECIAL(init));
/*      */     }
/*      */ 
/* 1298 */     il.append(new PUTFIELD(domField));
/*      */ 
/* 1301 */     int gitr = cpg.addInterfaceMethodref("com.sun.org.apache.xalan.internal.xsltc.DOM", "getIterator", "()Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;");
/*      */ 
/* 1304 */     il.append(transf.loadDOM());
/* 1305 */     il.append(new INVOKEINTERFACE(gitr, 1));
/* 1306 */     il.append(transf.nextNode());
/* 1307 */     current.setStart(il.append(new ISTORE(current.getIndex())));
/*      */ 
/* 1310 */     il.append(classGen.loadTranslet());
/* 1311 */     il.append(transf.loadHandler());
/* 1312 */     int index = cpg.addMethodref("com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet", "transferOutputSettings", "(Lcom/sun/org/apache/xml/internal/serializer/SerializationHandler;)V");
/*      */ 
/* 1315 */     il.append(new INVOKEVIRTUAL(index));
/*      */ 
/* 1323 */     String keySig = compileBuildKeys(classGen);
/* 1324 */     int keyIdx = cpg.addMethodref(getClassName(), "buildKeys", keySig);
/*      */ 
/* 1328 */     Enumeration toplevel = elements();
/* 1329 */     if ((this._globals.size() > 0) || (toplevel.hasMoreElements()))
/*      */     {
/* 1331 */       String topLevelSig = compileTopLevel(classGen);
/*      */ 
/* 1333 */       int topLevelIdx = cpg.addMethodref(getClassName(), "topLevel", topLevelSig);
/*      */ 
/* 1337 */       il.append(classGen.loadTranslet());
/* 1338 */       il.append(classGen.loadTranslet());
/* 1339 */       il.append(new GETFIELD(domField));
/* 1340 */       il.append(transf.loadIterator());
/* 1341 */       il.append(transf.loadHandler());
/* 1342 */       il.append(new INVOKEVIRTUAL(topLevelIdx));
/*      */     }
/*      */ 
/* 1346 */     il.append(transf.loadHandler());
/* 1347 */     il.append(transf.startDocument());
/*      */ 
/* 1350 */     il.append(classGen.loadTranslet());
/*      */ 
/* 1352 */     il.append(classGen.loadTranslet());
/* 1353 */     il.append(new GETFIELD(domField));
/*      */ 
/* 1355 */     il.append(transf.loadIterator());
/* 1356 */     il.append(transf.loadHandler());
/* 1357 */     il.append(new INVOKEVIRTUAL(applyTemplates));
/*      */ 
/* 1359 */     il.append(transf.loadHandler());
/* 1360 */     il.append(transf.endDocument());
/*      */ 
/* 1362 */     il.append(RETURN);
/*      */ 
/* 1365 */     classGen.addMethod(transf);
/*      */   }
/*      */ 
/*      */   private void peepHoleOptimization(MethodGenerator methodGen)
/*      */   {
/* 1373 */     String pattern = "`aload'`pop'`instruction'";
/* 1374 */     InstructionList il = methodGen.getInstructionList();
/* 1375 */     InstructionFinder find = new InstructionFinder(il);
/* 1376 */     for (Iterator iter = find.search("`aload'`pop'`instruction'"); iter.hasNext(); ) {
/* 1377 */       InstructionHandle[] match = (InstructionHandle[])iter.next();
/*      */       try {
/* 1379 */         il.delete(match[0], match[1]);
/*      */       }
/*      */       catch (TargetLostException e)
/*      */       {
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public int addParam(Param param) {
/* 1388 */     this._globals.addElement(param);
/* 1389 */     return this._globals.size() - 1;
/*      */   }
/*      */ 
/*      */   public int addVariable(Variable global) {
/* 1393 */     this._globals.addElement(global);
/* 1394 */     return this._globals.size() - 1;
/*      */   }
/*      */ 
/*      */   public void display(int indent) {
/* 1398 */     indent(indent);
/* 1399 */     Util.println("Stylesheet");
/* 1400 */     displayContents(indent + 4);
/*      */   }
/*      */ 
/*      */   public String getNamespace(String prefix)
/*      */   {
/* 1405 */     return lookupNamespace(prefix);
/*      */   }
/*      */ 
/*      */   public String getClassName() {
/* 1409 */     return this._className;
/*      */   }
/*      */ 
/*      */   public Vector getTemplates() {
/* 1413 */     return this._templates;
/*      */   }
/*      */ 
/*      */   public Vector getAllValidTemplates()
/*      */   {
/* 1418 */     if (this._includedStylesheets == null) {
/* 1419 */       return this._templates;
/*      */     }
/*      */ 
/* 1423 */     if (this._allValidTemplates == null) {
/* 1424 */       Vector templates = new Vector();
/* 1425 */       templates.addAll(this._templates);
/* 1426 */       int size = this._includedStylesheets.size();
/* 1427 */       for (int i = 0; i < size; i++) {
/* 1428 */         Stylesheet included = (Stylesheet)this._includedStylesheets.elementAt(i);
/* 1429 */         templates.addAll(included.getAllValidTemplates());
/*      */       }
/*      */ 
/* 1434 */       if (this._parentStylesheet != null) {
/* 1435 */         return templates;
/*      */       }
/* 1437 */       this._allValidTemplates = templates;
/*      */     }
/*      */ 
/* 1440 */     return this._allValidTemplates;
/*      */   }
/*      */ 
/*      */   protected void addTemplate(Template template) {
/* 1444 */     this._templates.addElement(template);
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.Stylesheet
 * JD-Core Version:    0.6.2
 */