/*     */ package com.sun.org.apache.xalan.internal.xsltc.compiler;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.generic.ANEWARRAY;
/*     */ import com.sun.org.apache.bcel.internal.generic.BasicType;
/*     */ import com.sun.org.apache.bcel.internal.generic.CHECKCAST;
/*     */ import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
/*     */ import com.sun.org.apache.bcel.internal.generic.DUP_X1;
/*     */ import com.sun.org.apache.bcel.internal.generic.GETFIELD;
/*     */ import com.sun.org.apache.bcel.internal.generic.ICONST;
/*     */ import com.sun.org.apache.bcel.internal.generic.INVOKEINTERFACE;
/*     */ import com.sun.org.apache.bcel.internal.generic.INVOKESPECIAL;
/*     */ import com.sun.org.apache.bcel.internal.generic.INVOKEVIRTUAL;
/*     */ import com.sun.org.apache.bcel.internal.generic.InstructionList;
/*     */ import com.sun.org.apache.bcel.internal.generic.NEW;
/*     */ import com.sun.org.apache.bcel.internal.generic.NEWARRAY;
/*     */ import com.sun.org.apache.bcel.internal.generic.PUSH;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util;
/*     */ import java.io.PrintStream;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Vector;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.helpers.AttributesImpl;
/*     */ 
/*     */ public abstract class SyntaxTreeNode
/*     */   implements Constants
/*     */ {
/*     */   private Parser _parser;
/*     */   protected SyntaxTreeNode _parent;
/*     */   private Stylesheet _stylesheet;
/*     */   private Template _template;
/*  73 */   private final Vector _contents = new Vector(2);
/*     */   protected QName _qname;
/*     */   private int _line;
/*  78 */   protected AttributesImpl _attributes = null;
/*  79 */   private Hashtable _prefixMapping = null;
/*     */ 
/*  82 */   protected static final SyntaxTreeNode Dummy = new AbsolutePathPattern(null);
/*     */   protected static final int IndentIncrement = 4;
/*  86 */   private static final char[] _spaces = "                                                       ".toCharArray();
/*     */ 
/*     */   public SyntaxTreeNode()
/*     */   {
/*  94 */     this._line = 0;
/*  95 */     this._qname = null;
/*     */   }
/*     */ 
/*     */   public SyntaxTreeNode(int line)
/*     */   {
/* 103 */     this._line = line;
/* 104 */     this._qname = null;
/*     */   }
/*     */ 
/*     */   public SyntaxTreeNode(String uri, String prefix, String local)
/*     */   {
/* 114 */     this._line = 0;
/* 115 */     setQName(uri, prefix, local);
/*     */   }
/*     */ 
/*     */   protected final void setLineNumber(int line)
/*     */   {
/* 123 */     this._line = line;
/*     */   }
/*     */ 
/*     */   public final int getLineNumber()
/*     */   {
/* 133 */     if (this._line > 0) return this._line;
/* 134 */     SyntaxTreeNode parent = getParent();
/* 135 */     return parent != null ? parent.getLineNumber() : 0;
/*     */   }
/*     */ 
/*     */   protected void setQName(QName qname)
/*     */   {
/* 143 */     this._qname = qname;
/*     */   }
/*     */ 
/*     */   protected void setQName(String uri, String prefix, String localname)
/*     */   {
/* 153 */     this._qname = new QName(uri, prefix, localname);
/*     */   }
/*     */ 
/*     */   protected QName getQName()
/*     */   {
/* 161 */     return this._qname;
/*     */   }
/*     */ 
/*     */   protected void setAttributes(AttributesImpl attributes)
/*     */   {
/* 170 */     this._attributes = attributes;
/*     */   }
/*     */ 
/*     */   protected String getAttribute(String qname)
/*     */   {
/* 179 */     if (this._attributes == null) {
/* 180 */       return "";
/*     */     }
/* 182 */     String value = this._attributes.getValue(qname);
/* 183 */     return (value == null) || (value.equals("")) ? "" : value;
/*     */   }
/*     */ 
/*     */   protected String getAttribute(String prefix, String localName)
/*     */   {
/* 188 */     return getAttribute(prefix + ':' + localName);
/*     */   }
/*     */ 
/*     */   protected boolean hasAttribute(String qname) {
/* 192 */     return (this._attributes != null) && (this._attributes.getValue(qname) != null);
/*     */   }
/*     */ 
/*     */   protected void addAttribute(String qname, String value) {
/* 196 */     int index = this._attributes.getIndex(qname);
/* 197 */     if (index != -1) {
/* 198 */       this._attributes.setAttribute(index, "", Util.getLocalName(qname), qname, "CDATA", value);
/*     */     }
/*     */     else
/*     */     {
/* 202 */       this._attributes.addAttribute("", Util.getLocalName(qname), qname, "CDATA", value);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected Attributes getAttributes()
/*     */   {
/* 213 */     return this._attributes;
/*     */   }
/*     */ 
/*     */   protected void setPrefixMapping(Hashtable mapping)
/*     */   {
/* 225 */     this._prefixMapping = mapping;
/*     */   }
/*     */ 
/*     */   protected Hashtable getPrefixMapping()
/*     */   {
/* 236 */     return this._prefixMapping;
/*     */   }
/*     */ 
/*     */   protected void addPrefixMapping(String prefix, String uri)
/*     */   {
/* 245 */     if (this._prefixMapping == null)
/* 246 */       this._prefixMapping = new Hashtable();
/* 247 */     this._prefixMapping.put(prefix, uri);
/*     */   }
/*     */ 
/*     */   protected String lookupNamespace(String prefix)
/*     */   {
/* 260 */     String uri = null;
/*     */ 
/* 263 */     if (this._prefixMapping != null) {
/* 264 */       uri = (String)this._prefixMapping.get(prefix);
/*     */     }
/* 266 */     if ((uri == null) && (this._parent != null)) {
/* 267 */       uri = this._parent.lookupNamespace(prefix);
/* 268 */       if ((prefix == "") && (uri == null)) {
/* 269 */         uri = "";
/*     */       }
/*     */     }
/* 272 */     return uri;
/*     */   }
/*     */ 
/*     */   protected String lookupPrefix(String uri)
/*     */   {
/* 287 */     String prefix = null;
/*     */ 
/* 290 */     if ((this._prefixMapping != null) && (this._prefixMapping.contains(uri)))
/*     */     {
/* 292 */       Enumeration prefixes = this._prefixMapping.keys();
/* 293 */       while (prefixes.hasMoreElements()) {
/* 294 */         prefix = (String)prefixes.nextElement();
/* 295 */         String mapsTo = (String)this._prefixMapping.get(prefix);
/* 296 */         if (mapsTo.equals(uri)) return prefix;
/*     */       }
/*     */ 
/*     */     }
/* 300 */     else if (this._parent != null) {
/* 301 */       prefix = this._parent.lookupPrefix(uri);
/* 302 */       if ((uri == "") && (prefix == null))
/* 303 */         prefix = "";
/*     */     }
/* 305 */     return prefix;
/*     */   }
/*     */ 
/*     */   protected void setParser(Parser parser)
/*     */   {
/* 314 */     this._parser = parser;
/*     */   }
/*     */ 
/*     */   public final Parser getParser()
/*     */   {
/* 322 */     return this._parser;
/*     */   }
/*     */ 
/*     */   protected void setParent(SyntaxTreeNode parent)
/*     */   {
/* 332 */     if (this._parent == null) this._parent = parent;
/*     */   }
/*     */ 
/*     */   protected final SyntaxTreeNode getParent()
/*     */   {
/* 340 */     return this._parent;
/*     */   }
/*     */ 
/*     */   protected final boolean isDummy()
/*     */   {
/* 348 */     return this == Dummy;
/*     */   }
/*     */ 
/*     */   protected int getImportPrecedence()
/*     */   {
/* 357 */     Stylesheet stylesheet = getStylesheet();
/* 358 */     if (stylesheet == null) return -2147483648;
/* 359 */     return stylesheet.getImportPrecedence();
/*     */   }
/*     */ 
/*     */   public Stylesheet getStylesheet()
/*     */   {
/* 368 */     if (this._stylesheet == null) {
/* 369 */       SyntaxTreeNode parent = this;
/* 370 */       while (parent != null) {
/* 371 */         if ((parent instanceof Stylesheet))
/* 372 */           return (Stylesheet)parent;
/* 373 */         parent = parent.getParent();
/*     */       }
/* 375 */       this._stylesheet = ((Stylesheet)parent);
/*     */     }
/* 377 */     return this._stylesheet;
/*     */   }
/*     */ 
/*     */   protected Template getTemplate()
/*     */   {
/* 387 */     if (this._template == null) {
/* 388 */       SyntaxTreeNode parent = this;
/* 389 */       while ((parent != null) && (!(parent instanceof Template)))
/* 390 */         parent = parent.getParent();
/* 391 */       this._template = ((Template)parent);
/*     */     }
/* 393 */     return this._template;
/*     */   }
/*     */ 
/*     */   protected final XSLTC getXSLTC()
/*     */   {
/* 401 */     return this._parser.getXSLTC();
/*     */   }
/*     */ 
/*     */   protected final SymbolTable getSymbolTable()
/*     */   {
/* 409 */     return this._parser == null ? null : this._parser.getSymbolTable();
/*     */   }
/*     */ 
/*     */   public void parseContents(Parser parser)
/*     */   {
/* 420 */     parseChildren(parser);
/*     */   }
/*     */ 
/*     */   protected final void parseChildren(Parser parser)
/*     */   {
/* 430 */     Vector locals = null;
/*     */ 
/* 432 */     int count = this._contents.size();
/* 433 */     for (int i = 0; i < count; i++) {
/* 434 */       SyntaxTreeNode child = (SyntaxTreeNode)this._contents.elementAt(i);
/* 435 */       parser.getSymbolTable().setCurrentNode(child);
/* 436 */       child.parseContents(parser);
/*     */ 
/* 438 */       QName varOrParamName = updateScope(parser, child);
/* 439 */       if (varOrParamName != null) {
/* 440 */         if (locals == null) {
/* 441 */           locals = new Vector(2);
/*     */         }
/* 443 */         locals.addElement(varOrParamName);
/*     */       }
/*     */     }
/*     */ 
/* 447 */     parser.getSymbolTable().setCurrentNode(this);
/*     */ 
/* 450 */     if (locals != null) {
/* 451 */       int nLocals = locals.size();
/* 452 */       for (int i = 0; i < nLocals; i++)
/* 453 */         parser.removeVariable((QName)locals.elementAt(i));
/*     */     }
/*     */   }
/*     */ 
/*     */   protected QName updateScope(Parser parser, SyntaxTreeNode node)
/*     */   {
/* 463 */     if ((node instanceof Variable)) {
/* 464 */       Variable var = (Variable)node;
/* 465 */       parser.addVariable(var);
/* 466 */       return var.getName();
/*     */     }
/* 468 */     if ((node instanceof Param)) {
/* 469 */       Param param = (Param)node;
/* 470 */       parser.addParameter(param);
/* 471 */       return param.getName();
/*     */     }
/*     */ 
/* 474 */     return null;
/*     */   }
/*     */ 
/*     */   public abstract Type typeCheck(SymbolTable paramSymbolTable)
/*     */     throws TypeCheckError;
/*     */ 
/*     */   protected Type typeCheckContents(SymbolTable stable)
/*     */     throws TypeCheckError
/*     */   {
/* 490 */     int n = elementCount();
/* 491 */     for (int i = 0; i < n; i++) {
/* 492 */       SyntaxTreeNode item = (SyntaxTreeNode)this._contents.elementAt(i);
/* 493 */       item.typeCheck(stable);
/*     */     }
/* 495 */     return Type.Void;
/*     */   }
/*     */ 
/*     */   public abstract void translate(ClassGenerator paramClassGenerator, MethodGenerator paramMethodGenerator);
/*     */ 
/*     */   protected void translateContents(ClassGenerator classGen, MethodGenerator methodGen)
/*     */   {
/* 514 */     int n = elementCount();
/*     */ 
/* 516 */     for (int i = 0; i < n; i++) {
/* 517 */       methodGen.markChunkStart();
/* 518 */       SyntaxTreeNode item = (SyntaxTreeNode)this._contents.elementAt(i);
/* 519 */       item.translate(classGen, methodGen);
/* 520 */       methodGen.markChunkEnd();
/*     */     }
/*     */ 
/* 528 */     for (int i = 0; i < n; i++)
/* 529 */       if ((this._contents.elementAt(i) instanceof VariableBase)) {
/* 530 */         VariableBase var = (VariableBase)this._contents.elementAt(i);
/* 531 */         var.unmapRegister(methodGen);
/*     */       }
/*     */   }
/*     */ 
/*     */   private boolean isSimpleRTF(SyntaxTreeNode node)
/*     */   {
/* 546 */     Vector contents = node.getContents();
/* 547 */     for (int i = 0; i < contents.size(); i++) {
/* 548 */       SyntaxTreeNode item = (SyntaxTreeNode)contents.elementAt(i);
/* 549 */       if (!isTextElement(item, false)) {
/* 550 */         return false;
/*     */       }
/*     */     }
/* 553 */     return true;
/*     */   }
/*     */ 
/*     */   private boolean isAdaptiveRTF(SyntaxTreeNode node)
/*     */   {
/* 567 */     Vector contents = node.getContents();
/* 568 */     for (int i = 0; i < contents.size(); i++) {
/* 569 */       SyntaxTreeNode item = (SyntaxTreeNode)contents.elementAt(i);
/* 570 */       if (!isTextElement(item, true)) {
/* 571 */         return false;
/*     */       }
/*     */     }
/* 574 */     return true;
/*     */   }
/*     */ 
/*     */   private boolean isTextElement(SyntaxTreeNode node, boolean doExtendedCheck)
/*     */   {
/* 594 */     if (((node instanceof ValueOf)) || ((node instanceof Number)) || ((node instanceof Text)))
/*     */     {
/* 597 */       return true;
/*     */     }
/* 599 */     if ((node instanceof If)) {
/* 600 */       return doExtendedCheck ? isAdaptiveRTF(node) : isSimpleRTF(node);
/*     */     }
/* 602 */     if ((node instanceof Choose)) {
/* 603 */       Vector contents = node.getContents();
/* 604 */       for (int i = 0; i < contents.size(); i++) {
/* 605 */         SyntaxTreeNode item = (SyntaxTreeNode)contents.elementAt(i);
/* 606 */         if ((!(item instanceof Text)) && (((!(item instanceof When)) && (!(item instanceof Otherwise))) || (((!doExtendedCheck) || (!isAdaptiveRTF(item))) && ((doExtendedCheck) || (!isSimpleRTF(item))))))
/*     */         {
/* 612 */           return false;
/*     */         }
/*     */       }
/* 614 */       return true;
/*     */     }
/* 616 */     if ((doExtendedCheck) && (((node instanceof CallTemplate)) || ((node instanceof ApplyTemplates))))
/*     */     {
/* 619 */       return true;
/*     */     }
/* 621 */     return false;
/*     */   }
/*     */ 
/*     */   protected void compileResultTree(ClassGenerator classGen, MethodGenerator methodGen)
/*     */   {
/* 632 */     ConstantPoolGen cpg = classGen.getConstantPool();
/* 633 */     InstructionList il = methodGen.getInstructionList();
/* 634 */     Stylesheet stylesheet = classGen.getStylesheet();
/*     */ 
/* 636 */     boolean isSimple = isSimpleRTF(this);
/* 637 */     boolean isAdaptive = false;
/* 638 */     if (!isSimple) {
/* 639 */       isAdaptive = isAdaptiveRTF(this);
/*     */     }
/*     */ 
/* 642 */     int rtfType = isAdaptive ? 1 : isSimple ? 0 : 2;
/*     */ 
/* 646 */     il.append(methodGen.loadHandler());
/*     */ 
/* 648 */     String DOM_CLASS = classGen.getDOMClass();
/*     */ 
/* 654 */     il.append(methodGen.loadDOM());
/* 655 */     int index = cpg.addInterfaceMethodref("com.sun.org.apache.xalan.internal.xsltc.DOM", "getResultTreeFrag", "(IIZ)Lcom/sun/org/apache/xalan/internal/xsltc/DOM;");
/*     */ 
/* 658 */     il.append(new PUSH(cpg, 32));
/* 659 */     il.append(new PUSH(cpg, rtfType));
/* 660 */     il.append(new PUSH(cpg, stylesheet.callsNodeset()));
/* 661 */     il.append(new INVOKEINTERFACE(index, 4));
/*     */ 
/* 663 */     il.append(DUP);
/*     */ 
/* 666 */     index = cpg.addInterfaceMethodref("com.sun.org.apache.xalan.internal.xsltc.DOM", "getOutputDomBuilder", "()Lcom/sun/org/apache/xml/internal/serializer/SerializationHandler;");
/*     */ 
/* 670 */     il.append(new INVOKEINTERFACE(index, 1));
/* 671 */     il.append(DUP);
/* 672 */     il.append(methodGen.storeHandler());
/*     */ 
/* 675 */     il.append(methodGen.startDocument());
/*     */ 
/* 678 */     translateContents(classGen, methodGen);
/*     */ 
/* 681 */     il.append(methodGen.loadHandler());
/* 682 */     il.append(methodGen.endDocument());
/*     */ 
/* 687 */     if ((stylesheet.callsNodeset()) && (!DOM_CLASS.equals("com/sun/org/apache/xalan/internal/xsltc/DOM")))
/*     */     {
/* 690 */       index = cpg.addMethodref("com/sun/org/apache/xalan/internal/xsltc/dom/DOMAdapter", "<init>", "(Lcom/sun/org/apache/xalan/internal/xsltc/DOM;[Ljava/lang/String;[Ljava/lang/String;[I[Ljava/lang/String;)V");
/*     */ 
/* 697 */       il.append(new NEW(cpg.addClass("com/sun/org/apache/xalan/internal/xsltc/dom/DOMAdapter")));
/* 698 */       il.append(new DUP_X1());
/* 699 */       il.append(SWAP);
/*     */ 
/* 705 */       if (!stylesheet.callsNodeset()) {
/* 706 */         il.append(new ICONST(0));
/* 707 */         il.append(new ANEWARRAY(cpg.addClass("java.lang.String")));
/* 708 */         il.append(DUP);
/* 709 */         il.append(DUP);
/* 710 */         il.append(new ICONST(0));
/* 711 */         il.append(new NEWARRAY(BasicType.INT));
/* 712 */         il.append(SWAP);
/* 713 */         il.append(new INVOKESPECIAL(index));
/*     */       }
/*     */       else
/*     */       {
/* 717 */         il.append(ALOAD_0);
/* 718 */         il.append(new GETFIELD(cpg.addFieldref("com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet", "namesArray", "[Ljava/lang/String;")));
/*     */ 
/* 721 */         il.append(ALOAD_0);
/* 722 */         il.append(new GETFIELD(cpg.addFieldref("com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet", "urisArray", "[Ljava/lang/String;")));
/*     */ 
/* 725 */         il.append(ALOAD_0);
/* 726 */         il.append(new GETFIELD(cpg.addFieldref("com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet", "typesArray", "[I")));
/*     */ 
/* 729 */         il.append(ALOAD_0);
/* 730 */         il.append(new GETFIELD(cpg.addFieldref("com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet", "namespaceArray", "[Ljava/lang/String;")));
/*     */ 
/* 735 */         il.append(new INVOKESPECIAL(index));
/*     */ 
/* 738 */         il.append(DUP);
/* 739 */         il.append(methodGen.loadDOM());
/* 740 */         il.append(new CHECKCAST(cpg.addClass(classGen.getDOMClass())));
/* 741 */         il.append(SWAP);
/* 742 */         index = cpg.addMethodref("com.sun.org.apache.xalan.internal.xsltc.dom.MultiDOM", "addDOMAdapter", "(Lcom/sun/org/apache/xalan/internal/xsltc/dom/DOMAdapter;)I");
/*     */ 
/* 745 */         il.append(new INVOKEVIRTUAL(index));
/* 746 */         il.append(POP);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 751 */     il.append(SWAP);
/* 752 */     il.append(methodGen.storeHandler());
/*     */   }
/*     */ 
/*     */   protected boolean contextDependent()
/*     */   {
/* 763 */     return true;
/*     */   }
/*     */ 
/*     */   protected boolean dependentContents()
/*     */   {
/* 772 */     int n = elementCount();
/* 773 */     for (int i = 0; i < n; i++) {
/* 774 */       SyntaxTreeNode item = (SyntaxTreeNode)this._contents.elementAt(i);
/* 775 */       if (item.contextDependent()) {
/* 776 */         return true;
/*     */       }
/*     */     }
/* 779 */     return false;
/*     */   }
/*     */ 
/*     */   protected final void addElement(SyntaxTreeNode element)
/*     */   {
/* 787 */     this._contents.addElement(element);
/* 788 */     element.setParent(this);
/*     */   }
/*     */ 
/*     */   protected final void setFirstElement(SyntaxTreeNode element)
/*     */   {
/* 797 */     this._contents.insertElementAt(element, 0);
/* 798 */     element.setParent(this);
/*     */   }
/*     */ 
/*     */   protected final void removeElement(SyntaxTreeNode element)
/*     */   {
/* 806 */     this._contents.remove(element);
/* 807 */     element.setParent(null);
/*     */   }
/*     */ 
/*     */   protected final Vector getContents()
/*     */   {
/* 815 */     return this._contents;
/*     */   }
/*     */ 
/*     */   protected final boolean hasContents()
/*     */   {
/* 823 */     return elementCount() > 0;
/*     */   }
/*     */ 
/*     */   protected final int elementCount()
/*     */   {
/* 831 */     return this._contents.size();
/*     */   }
/*     */ 
/*     */   protected final Enumeration elements()
/*     */   {
/* 839 */     return this._contents.elements();
/*     */   }
/*     */ 
/*     */   protected final Object elementAt(int pos)
/*     */   {
/* 848 */     return this._contents.elementAt(pos);
/*     */   }
/*     */ 
/*     */   protected final SyntaxTreeNode lastChild()
/*     */   {
/* 856 */     if (this._contents.size() == 0) return null;
/* 857 */     return (SyntaxTreeNode)this._contents.lastElement();
/*     */   }
/*     */ 
/*     */   public void display(int indent)
/*     */   {
/* 867 */     displayContents(indent);
/*     */   }
/*     */ 
/*     */   protected void displayContents(int indent)
/*     */   {
/* 876 */     int n = elementCount();
/* 877 */     for (int i = 0; i < n; i++) {
/* 878 */       SyntaxTreeNode item = (SyntaxTreeNode)this._contents.elementAt(i);
/* 879 */       item.display(indent);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected final void indent(int indent)
/*     */   {
/* 888 */     System.out.print(new String(_spaces, 0, indent));
/*     */   }
/*     */ 
/*     */   protected void reportError(SyntaxTreeNode element, Parser parser, String errorCode, String message)
/*     */   {
/* 901 */     ErrorMsg error = new ErrorMsg(errorCode, message, element);
/* 902 */     parser.reportError(3, error);
/*     */   }
/*     */ 
/*     */   protected void reportWarning(SyntaxTreeNode element, Parser parser, String errorCode, String message)
/*     */   {
/* 915 */     ErrorMsg error = new ErrorMsg(errorCode, message, element);
/* 916 */     parser.reportError(4, error);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
 * JD-Core Version:    0.6.2
 */