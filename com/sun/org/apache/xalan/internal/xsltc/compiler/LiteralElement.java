/*     */ package com.sun.org.apache.xalan.internal.xsltc.compiler;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
/*     */ import com.sun.org.apache.bcel.internal.generic.InstructionList;
/*     */ import com.sun.org.apache.bcel.internal.generic.PUSH;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util;
/*     */ import com.sun.org.apache.xml.internal.serializer.ElemDesc;
/*     */ import com.sun.org.apache.xml.internal.serializer.ToHTMLStream;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Vector;
/*     */ import org.xml.sax.helpers.AttributesImpl;
/*     */ 
/*     */ final class LiteralElement extends Instruction
/*     */ {
/*     */   private String _name;
/*  51 */   private LiteralElement _literalElemParent = null;
/*  52 */   private Vector _attributeElements = null;
/*  53 */   private Hashtable _accessedPrefixes = null;
/*     */ 
/*  58 */   private boolean _allAttributesUnique = false;
/*     */   private static final String XMLNS_STRING = "xmlns";
/*     */ 
/*     */   public QName getName()
/*     */   {
/*  66 */     return this._qname;
/*     */   }
/*     */ 
/*     */   public void display(int indent)
/*     */   {
/*  73 */     indent(indent);
/*  74 */     Util.println("LiteralElement name = " + this._name);
/*  75 */     displayContents(indent + 4);
/*     */   }
/*     */ 
/*     */   private String accessedNamespace(String prefix)
/*     */   {
/*  82 */     if (this._literalElemParent != null) {
/*  83 */       String result = this._literalElemParent.accessedNamespace(prefix);
/*  84 */       if (result != null) {
/*  85 */         return result;
/*     */       }
/*     */     }
/*  88 */     return this._accessedPrefixes != null ? (String)this._accessedPrefixes.get(prefix) : null;
/*     */   }
/*     */ 
/*     */   public void registerNamespace(String prefix, String uri, SymbolTable stable, boolean declared)
/*     */   {
/* 101 */     if (this._literalElemParent != null) {
/* 102 */       String parentUri = this._literalElemParent.accessedNamespace(prefix);
/* 103 */       if ((parentUri != null) && (parentUri.equals(uri))) {
/* 104 */         return;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 109 */     if (this._accessedPrefixes == null) {
/* 110 */       this._accessedPrefixes = new Hashtable();
/*     */     }
/* 113 */     else if (!declared)
/*     */     {
/* 115 */       String old = (String)this._accessedPrefixes.get(prefix);
/* 116 */       if (old != null) {
/* 117 */         if (old.equals(uri)) {
/* 118 */           return;
/*     */         }
/* 120 */         prefix = stable.generateNamespacePrefix();
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 125 */     if (!prefix.equals("xml"))
/* 126 */       this._accessedPrefixes.put(prefix, uri);
/*     */   }
/*     */ 
/*     */   private String translateQName(QName qname, SymbolTable stable)
/*     */   {
/* 137 */     String localname = qname.getLocalPart();
/* 138 */     String prefix = qname.getPrefix();
/*     */ 
/* 141 */     if (prefix == null)
/* 142 */       prefix = "";
/* 143 */     else if (prefix.equals("xmlns")) {
/* 144 */       return "xmlns";
/*     */     }
/*     */ 
/* 147 */     String alternative = stable.lookupPrefixAlias(prefix);
/* 148 */     if (alternative != null) {
/* 149 */       stable.excludeNamespaces(prefix);
/* 150 */       prefix = alternative;
/*     */     }
/*     */ 
/* 154 */     String uri = lookupNamespace(prefix);
/* 155 */     if (uri == null) return localname;
/*     */ 
/* 158 */     registerNamespace(prefix, uri, stable, false);
/*     */ 
/* 161 */     if (prefix != "") {
/* 162 */       return prefix + ":" + localname;
/*     */     }
/* 164 */     return localname;
/*     */   }
/*     */ 
/*     */   public void addAttribute(SyntaxTreeNode attribute)
/*     */   {
/* 171 */     if (this._attributeElements == null) {
/* 172 */       this._attributeElements = new Vector(2);
/*     */     }
/* 174 */     this._attributeElements.add(attribute);
/*     */   }
/*     */ 
/*     */   public void setFirstAttribute(SyntaxTreeNode attribute)
/*     */   {
/* 181 */     if (this._attributeElements == null) {
/* 182 */       this._attributeElements = new Vector(2);
/*     */     }
/* 184 */     this._attributeElements.insertElementAt(attribute, 0);
/*     */   }
/*     */ 
/*     */   public Type typeCheck(SymbolTable stable)
/*     */     throws TypeCheckError
/*     */   {
/* 193 */     if (this._attributeElements != null) {
/* 194 */       int count = this._attributeElements.size();
/* 195 */       for (int i = 0; i < count; i++) {
/* 196 */         SyntaxTreeNode node = (SyntaxTreeNode)this._attributeElements.elementAt(i);
/*     */ 
/* 198 */         node.typeCheck(stable);
/*     */       }
/*     */     }
/* 201 */     typeCheckContents(stable);
/* 202 */     return Type.Void;
/*     */   }
/*     */ 
/*     */   public Enumeration getNamespaceScope(SyntaxTreeNode node)
/*     */   {
/* 211 */     Hashtable all = new Hashtable();
/*     */ 
/* 213 */     while (node != null) {
/* 214 */       Hashtable mapping = node.getPrefixMapping();
/* 215 */       if (mapping != null) {
/* 216 */         Enumeration prefixes = mapping.keys();
/* 217 */         while (prefixes.hasMoreElements()) {
/* 218 */           String prefix = (String)prefixes.nextElement();
/* 219 */           if (!all.containsKey(prefix)) {
/* 220 */             all.put(prefix, mapping.get(prefix));
/*     */           }
/*     */         }
/*     */       }
/* 224 */       node = node.getParent();
/*     */     }
/* 226 */     return all.keys();
/*     */   }
/*     */ 
/*     */   public void parseContents(Parser parser)
/*     */   {
/* 234 */     SymbolTable stable = parser.getSymbolTable();
/* 235 */     stable.setCurrentNode(this);
/*     */ 
/* 238 */     SyntaxTreeNode parent = getParent();
/* 239 */     if ((parent != null) && ((parent instanceof LiteralElement))) {
/* 240 */       this._literalElemParent = ((LiteralElement)parent);
/*     */     }
/*     */ 
/* 243 */     this._name = translateQName(this._qname, stable);
/*     */ 
/* 246 */     int count = this._attributes.getLength();
/* 247 */     for (int i = 0; i < count; i++) {
/* 248 */       QName qname = parser.getQName(this._attributes.getQName(i));
/* 249 */       String uri = qname.getNamespace();
/* 250 */       String val = this._attributes.getValue(i);
/*     */ 
/* 255 */       if (qname.equals(parser.getUseAttributeSets())) {
/* 256 */         if (!Util.isValidQNames(val)) {
/* 257 */           ErrorMsg err = new ErrorMsg("INVALID_QNAME_ERR", val, this);
/* 258 */           parser.reportError(3, err);
/*     */         }
/* 260 */         setFirstAttribute(new UseAttributeSets(val, parser));
/*     */       }
/* 263 */       else if (qname.equals(parser.getExtensionElementPrefixes())) {
/* 264 */         stable.excludeNamespaces(val);
/*     */       }
/* 267 */       else if (qname.equals(parser.getExcludeResultPrefixes())) {
/* 268 */         stable.excludeNamespaces(val);
/*     */       }
/*     */       else
/*     */       {
/* 272 */         String prefix = qname.getPrefix();
/* 273 */         if (((prefix == null) || (!prefix.equals("xmlns"))) && ((prefix != null) || (!qname.getLocalPart().equals("xmlns"))) && ((uri == null) || (!uri.equals("http://www.w3.org/1999/XSL/Transform"))))
/*     */         {
/* 281 */           String name = translateQName(qname, stable);
/* 282 */           LiteralAttribute attr = new LiteralAttribute(name, val, parser, this);
/* 283 */           addAttribute(attr);
/* 284 */           attr.setParent(this);
/* 285 */           attr.parseContents(parser);
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 291 */     Enumeration include = getNamespaceScope(this);
/* 292 */     while (include.hasMoreElements()) {
/* 293 */       String prefix = (String)include.nextElement();
/* 294 */       if (!prefix.equals("xml")) {
/* 295 */         String uri = lookupNamespace(prefix);
/* 296 */         if ((uri != null) && (!stable.isExcludedNamespace(uri))) {
/* 297 */           registerNamespace(prefix, uri, stable, true);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 302 */     parseChildren(parser);
/*     */ 
/* 305 */     for (int i = 0; i < count; i++) {
/* 306 */       QName qname = parser.getQName(this._attributes.getQName(i));
/* 307 */       String val = this._attributes.getValue(i);
/*     */ 
/* 310 */       if (qname.equals(parser.getExtensionElementPrefixes())) {
/* 311 */         stable.unExcludeNamespaces(val);
/*     */       }
/* 314 */       else if (qname.equals(parser.getExcludeResultPrefixes()))
/* 315 */         stable.unExcludeNamespaces(val);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected boolean contextDependent()
/*     */   {
/* 321 */     return dependentContents();
/*     */   }
/*     */ 
/*     */   public void translate(ClassGenerator classGen, MethodGenerator methodGen)
/*     */   {
/* 333 */     ConstantPoolGen cpg = classGen.getConstantPool();
/* 334 */     InstructionList il = methodGen.getInstructionList();
/*     */ 
/* 337 */     this._allAttributesUnique = checkAttributesUnique();
/*     */ 
/* 340 */     il.append(methodGen.loadHandler());
/*     */ 
/* 342 */     il.append(new PUSH(cpg, this._name));
/* 343 */     il.append(DUP2);
/* 344 */     il.append(methodGen.startElement());
/*     */ 
/* 347 */     int j = 0;
/* 348 */     while (j < elementCount()) {
/* 349 */       SyntaxTreeNode item = (SyntaxTreeNode)elementAt(j);
/* 350 */       if ((item instanceof Variable)) {
/* 351 */         item.translate(classGen, methodGen);
/*     */       }
/* 353 */       j++;
/*     */     }
/*     */ 
/* 357 */     if (this._accessedPrefixes != null) {
/* 358 */       boolean declaresDefaultNS = false;
/* 359 */       Enumeration e = this._accessedPrefixes.keys();
/*     */ 
/* 361 */       while (e.hasMoreElements()) {
/* 362 */         String prefix = (String)e.nextElement();
/* 363 */         String uri = (String)this._accessedPrefixes.get(prefix);
/*     */ 
/* 365 */         if ((uri != "") || (prefix != ""))
/*     */         {
/* 368 */           if (prefix == "") {
/* 369 */             declaresDefaultNS = true;
/*     */           }
/* 371 */           il.append(methodGen.loadHandler());
/* 372 */           il.append(new PUSH(cpg, prefix));
/* 373 */           il.append(new PUSH(cpg, uri));
/* 374 */           il.append(methodGen.namespace());
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 382 */       if ((!declaresDefaultNS) && ((this._parent instanceof XslElement)) && (((XslElement)this._parent).declaresDefaultNS()))
/*     */       {
/* 385 */         il.append(methodGen.loadHandler());
/* 386 */         il.append(new PUSH(cpg, ""));
/* 387 */         il.append(new PUSH(cpg, ""));
/* 388 */         il.append(methodGen.namespace());
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 393 */     if (this._attributeElements != null) {
/* 394 */       int count = this._attributeElements.size();
/* 395 */       for (int i = 0; i < count; i++) {
/* 396 */         SyntaxTreeNode node = (SyntaxTreeNode)this._attributeElements.elementAt(i);
/*     */ 
/* 398 */         if (!(node instanceof XslAttribute)) {
/* 399 */           node.translate(classGen, methodGen);
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 405 */     translateContents(classGen, methodGen);
/*     */ 
/* 408 */     il.append(methodGen.endElement());
/*     */   }
/*     */ 
/*     */   private boolean isHTMLOutput()
/*     */   {
/* 415 */     return getStylesheet().getOutputMethod() == 2;
/*     */   }
/*     */ 
/*     */   public ElemDesc getElemDesc()
/*     */   {
/* 424 */     if (isHTMLOutput()) {
/* 425 */       return ToHTMLStream.getElemDesc(this._name);
/*     */     }
/*     */ 
/* 428 */     return null;
/*     */   }
/*     */ 
/*     */   public boolean allAttributesUnique()
/*     */   {
/* 435 */     return this._allAttributesUnique;
/*     */   }
/*     */ 
/*     */   private boolean checkAttributesUnique()
/*     */   {
/* 442 */     boolean hasHiddenXslAttribute = canProduceAttributeNodes(this, true);
/* 443 */     if (hasHiddenXslAttribute) {
/* 444 */       return false;
/*     */     }
/* 446 */     if (this._attributeElements != null) {
/* 447 */       int numAttrs = this._attributeElements.size();
/* 448 */       Hashtable attrsTable = null;
/* 449 */       for (int i = 0; i < numAttrs; i++) {
/* 450 */         SyntaxTreeNode node = (SyntaxTreeNode)this._attributeElements.elementAt(i);
/*     */ 
/* 452 */         if ((node instanceof UseAttributeSets)) {
/* 453 */           return false;
/*     */         }
/* 455 */         if ((node instanceof XslAttribute)) {
/* 456 */           if (attrsTable == null) {
/* 457 */             attrsTable = new Hashtable();
/* 458 */             for (int k = 0; k < i; k++) {
/* 459 */               SyntaxTreeNode n = (SyntaxTreeNode)this._attributeElements.elementAt(k);
/* 460 */               if ((n instanceof LiteralAttribute)) {
/* 461 */                 LiteralAttribute literalAttr = (LiteralAttribute)n;
/* 462 */                 attrsTable.put(literalAttr.getName(), literalAttr);
/*     */               }
/*     */             }
/*     */           }
/*     */ 
/* 467 */           XslAttribute xslAttr = (XslAttribute)node;
/* 468 */           AttributeValue attrName = xslAttr.getName();
/* 469 */           if ((attrName instanceof AttributeValueTemplate)) {
/* 470 */             return false;
/*     */           }
/* 472 */           if ((attrName instanceof SimpleAttributeValue)) {
/* 473 */             SimpleAttributeValue simpleAttr = (SimpleAttributeValue)attrName;
/* 474 */             String name = simpleAttr.toString();
/* 475 */             if ((name != null) && (attrsTable.get(name) != null))
/* 476 */               return false;
/* 477 */             if (name != null) {
/* 478 */               attrsTable.put(name, xslAttr);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 484 */     return true;
/*     */   }
/*     */ 
/*     */   private boolean canProduceAttributeNodes(SyntaxTreeNode node, boolean ignoreXslAttribute)
/*     */   {
/* 494 */     Vector contents = node.getContents();
/* 495 */     int size = contents.size();
/* 496 */     for (int i = 0; i < size; i++) {
/* 497 */       SyntaxTreeNode child = (SyntaxTreeNode)contents.elementAt(i);
/* 498 */       if ((child instanceof Text)) {
/* 499 */         Text text = (Text)child;
/* 500 */         if (!text.isIgnore())
/*     */         {
/* 503 */           return false;
/*     */         }
/*     */       }
/*     */       else {
/* 507 */         if (((child instanceof LiteralElement)) || ((child instanceof ValueOf)) || ((child instanceof XslElement)) || ((child instanceof Comment)) || ((child instanceof Number)) || ((child instanceof ProcessingInstruction)))
/*     */         {
/* 513 */           return false;
/* 514 */         }if ((child instanceof XslAttribute)) {
/* 515 */           if (!ignoreXslAttribute)
/*     */           {
/* 518 */             return true;
/*     */           }
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/* 524 */           if (((child instanceof CallTemplate)) || ((child instanceof ApplyTemplates)) || ((child instanceof Copy)) || ((child instanceof CopyOf)))
/*     */           {
/* 528 */             return true;
/* 529 */           }if ((((child instanceof If)) || ((child instanceof ForEach))) && (canProduceAttributeNodes(child, false)))
/*     */           {
/* 532 */             return true;
/*     */           }
/* 534 */           if ((child instanceof Choose)) {
/* 535 */             Vector chooseContents = child.getContents();
/* 536 */             int num = chooseContents.size();
/* 537 */             for (int k = 0; k < num; k++) {
/* 538 */               SyntaxTreeNode chooseChild = (SyntaxTreeNode)chooseContents.elementAt(k);
/* 539 */               if ((((chooseChild instanceof When)) || ((chooseChild instanceof Otherwise))) && 
/* 540 */                 (canProduceAttributeNodes(chooseChild, false)))
/* 541 */                 return true; 
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 546 */     return false;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.LiteralElement
 * JD-Core Version:    0.6.2
 */