/*     */ package com.sun.org.apache.xalan.internal.xsltc.compiler;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
/*     */ import com.sun.org.apache.bcel.internal.generic.INVOKEVIRTUAL;
/*     */ import com.sun.org.apache.bcel.internal.generic.InstructionList;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.NamedMethodGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util;
/*     */ import com.sun.org.apache.xml.internal.utils.XML11Char;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public final class Template extends TopLevelElement
/*     */ {
/*     */   private QName _name;
/*     */   private QName _mode;
/*     */   private Pattern _pattern;
/*     */   private double _priority;
/*     */   private int _position;
/*  55 */   private boolean _disabled = false;
/*  56 */   private boolean _compiled = false;
/*  57 */   private boolean _simplified = false;
/*     */ 
/*  61 */   private boolean _isSimpleNamedTemplate = false;
/*     */ 
/*  65 */   private Vector _parameters = new Vector();
/*     */ 
/* 185 */   private Stylesheet _stylesheet = null;
/*     */ 
/*     */   public boolean hasParams()
/*     */   {
/*  68 */     return this._parameters.size() > 0;
/*     */   }
/*     */ 
/*     */   public boolean isSimplified() {
/*  72 */     return this._simplified;
/*     */   }
/*     */ 
/*     */   public void setSimplified() {
/*  76 */     this._simplified = true;
/*     */   }
/*     */ 
/*     */   public boolean isSimpleNamedTemplate() {
/*  80 */     return this._isSimpleNamedTemplate;
/*     */   }
/*     */ 
/*     */   public void addParameter(Param param) {
/*  84 */     this._parameters.addElement(param);
/*     */   }
/*     */ 
/*     */   public Vector getParameters() {
/*  88 */     return this._parameters;
/*     */   }
/*     */ 
/*     */   public void disable() {
/*  92 */     this._disabled = true;
/*     */   }
/*     */ 
/*     */   public boolean disabled() {
/*  96 */     return this._disabled;
/*     */   }
/*     */ 
/*     */   public double getPriority() {
/* 100 */     return this._priority;
/*     */   }
/*     */ 
/*     */   public int getPosition() {
/* 104 */     return this._position;
/*     */   }
/*     */ 
/*     */   public boolean isNamed() {
/* 108 */     return this._name != null;
/*     */   }
/*     */ 
/*     */   public Pattern getPattern() {
/* 112 */     return this._pattern;
/*     */   }
/*     */ 
/*     */   public QName getName() {
/* 116 */     return this._name;
/*     */   }
/*     */ 
/*     */   public void setName(QName qname) {
/* 120 */     if (this._name == null) this._name = qname; 
/*     */   }
/*     */ 
/*     */   public QName getModeName()
/*     */   {
/* 124 */     return this._mode;
/*     */   }
/*     */ 
/*     */   public int compareTo(Object template)
/*     */   {
/* 131 */     Template other = (Template)template;
/* 132 */     if (this._priority > other._priority)
/* 133 */       return 1;
/* 134 */     if (this._priority < other._priority)
/* 135 */       return -1;
/* 136 */     if (this._position > other._position)
/* 137 */       return 1;
/* 138 */     if (this._position < other._position) {
/* 139 */       return -1;
/*     */     }
/* 141 */     return 0;
/*     */   }
/*     */ 
/*     */   public void display(int indent) {
/* 145 */     Util.println('\n');
/* 146 */     indent(indent);
/* 147 */     if (this._name != null) {
/* 148 */       indent(indent);
/* 149 */       Util.println("name = " + this._name);
/*     */     }
/* 151 */     else if (this._pattern != null) {
/* 152 */       indent(indent);
/* 153 */       Util.println("match = " + this._pattern.toString());
/*     */     }
/* 155 */     if (this._mode != null) {
/* 156 */       indent(indent);
/* 157 */       Util.println("mode = " + this._mode);
/*     */     }
/* 159 */     displayContents(indent + 4);
/*     */   }
/*     */ 
/*     */   private boolean resolveNamedTemplates(Template other, Parser parser)
/*     */   {
/* 164 */     if (other == null) return true;
/*     */ 
/* 166 */     SymbolTable stable = parser.getSymbolTable();
/*     */ 
/* 168 */     int us = getImportPrecedence();
/* 169 */     int them = other.getImportPrecedence();
/*     */ 
/* 171 */     if (us > them) {
/* 172 */       other.disable();
/* 173 */       return true;
/*     */     }
/* 175 */     if (us < them) {
/* 176 */       stable.addTemplate(other);
/* 177 */       disable();
/* 178 */       return true;
/*     */     }
/*     */ 
/* 181 */     return false;
/*     */   }
/*     */ 
/*     */   public Stylesheet getStylesheet()
/*     */   {
/* 188 */     return this._stylesheet;
/*     */   }
/*     */ 
/*     */   public void parseContents(Parser parser)
/*     */   {
/* 193 */     String name = getAttribute("name");
/* 194 */     String mode = getAttribute("mode");
/* 195 */     String match = getAttribute("match");
/* 196 */     String priority = getAttribute("priority");
/*     */ 
/* 198 */     this._stylesheet = super.getStylesheet();
/*     */ 
/* 200 */     if (name.length() > 0) {
/* 201 */       if (!XML11Char.isXML11ValidQName(name)) {
/* 202 */         ErrorMsg err = new ErrorMsg("INVALID_QNAME_ERR", name, this);
/* 203 */         parser.reportError(3, err);
/*     */       }
/* 205 */       this._name = parser.getQNameIgnoreDefaultNs(name);
/*     */     }
/*     */ 
/* 208 */     if (mode.length() > 0) {
/* 209 */       if (!XML11Char.isXML11ValidQName(mode)) {
/* 210 */         ErrorMsg err = new ErrorMsg("INVALID_QNAME_ERR", mode, this);
/* 211 */         parser.reportError(3, err);
/*     */       }
/* 213 */       this._mode = parser.getQNameIgnoreDefaultNs(mode);
/*     */     }
/*     */ 
/* 216 */     if (match.length() > 0) {
/* 217 */       this._pattern = parser.parsePattern(this, "match", null);
/*     */     }
/*     */ 
/* 220 */     if (priority.length() > 0) {
/* 221 */       this._priority = Double.parseDouble(priority);
/*     */     }
/* 224 */     else if (this._pattern != null)
/* 225 */       this._priority = this._pattern.getPriority();
/*     */     else {
/* 227 */       this._priority = (0.0D / 0.0D);
/*     */     }
/*     */ 
/* 230 */     this._position = parser.getTemplateIndex();
/*     */ 
/* 233 */     if (this._name != null) {
/* 234 */       Template other = parser.getSymbolTable().addTemplate(this);
/* 235 */       if (!resolveNamedTemplates(other, parser)) {
/* 236 */         ErrorMsg err = new ErrorMsg("TEMPLATE_REDEF_ERR", this._name, this);
/*     */ 
/* 238 */         parser.reportError(3, err);
/*     */       }
/*     */ 
/* 241 */       if ((this._pattern == null) && (this._mode == null)) {
/* 242 */         this._isSimpleNamedTemplate = true;
/*     */       }
/*     */     }
/*     */ 
/* 246 */     if ((this._parent instanceof Stylesheet)) {
/* 247 */       ((Stylesheet)this._parent).addTemplate(this);
/*     */     }
/*     */ 
/* 250 */     parser.setTemplate(this);
/* 251 */     parseChildren(parser);
/* 252 */     parser.setTemplate(null);
/*     */   }
/*     */ 
/*     */   public void parseSimplified(Stylesheet stylesheet, Parser parser)
/*     */   {
/* 269 */     this._stylesheet = stylesheet;
/* 270 */     setParent(stylesheet);
/*     */ 
/* 272 */     this._name = null;
/* 273 */     this._mode = null;
/* 274 */     this._priority = (0.0D / 0.0D);
/* 275 */     this._pattern = parser.parsePattern(this, "/");
/*     */ 
/* 277 */     Vector contents = this._stylesheet.getContents();
/* 278 */     SyntaxTreeNode root = (SyntaxTreeNode)contents.elementAt(0);
/*     */ 
/* 280 */     if ((root instanceof LiteralElement)) {
/* 281 */       addElement(root);
/* 282 */       root.setParent(this);
/* 283 */       contents.set(0, this);
/* 284 */       parser.setTemplate(this);
/* 285 */       root.parseContents(parser);
/* 286 */       parser.setTemplate(null);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Type typeCheck(SymbolTable stable) throws TypeCheckError {
/* 291 */     if (this._pattern != null) {
/* 292 */       this._pattern.typeCheck(stable);
/*     */     }
/*     */ 
/* 295 */     return typeCheckContents(stable);
/*     */   }
/*     */ 
/*     */   public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
/* 299 */     ConstantPoolGen cpg = classGen.getConstantPool();
/* 300 */     InstructionList il = methodGen.getInstructionList();
/*     */ 
/* 302 */     if (this._disabled) return;
/*     */ 
/* 304 */     String className = classGen.getClassName();
/*     */ 
/* 306 */     if ((this._compiled) && (isNamed())) {
/* 307 */       String methodName = Util.escape(this._name.toString());
/* 308 */       il.append(classGen.loadTranslet());
/* 309 */       il.append(methodGen.loadDOM());
/* 310 */       il.append(methodGen.loadIterator());
/* 311 */       il.append(methodGen.loadHandler());
/* 312 */       il.append(methodGen.loadCurrentNode());
/* 313 */       il.append(new INVOKEVIRTUAL(cpg.addMethodref(className, methodName, "(Lcom/sun/org/apache/xalan/internal/xsltc/DOM;Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;Lcom/sun/org/apache/xml/internal/serializer/SerializationHandler;I)V")));
/*     */ 
/* 320 */       return;
/*     */     }
/*     */ 
/* 323 */     if (this._compiled) return;
/* 324 */     this._compiled = true;
/*     */ 
/* 327 */     if ((this._isSimpleNamedTemplate) && ((methodGen instanceof NamedMethodGenerator))) {
/* 328 */       int numParams = this._parameters.size();
/* 329 */       NamedMethodGenerator namedMethodGen = (NamedMethodGenerator)methodGen;
/*     */ 
/* 332 */       for (int i = 0; i < numParams; i++) {
/* 333 */         Param param = (Param)this._parameters.elementAt(i);
/* 334 */         param.setLoadInstruction(namedMethodGen.loadParameter(i));
/* 335 */         param.setStoreInstruction(namedMethodGen.storeParameter(i));
/*     */       }
/*     */     }
/*     */ 
/* 339 */     translateContents(classGen, methodGen);
/* 340 */     il.setPositions(true);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.Template
 * JD-Core Version:    0.6.2
 */