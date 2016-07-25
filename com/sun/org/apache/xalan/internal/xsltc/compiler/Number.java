/*     */ package com.sun.org.apache.xalan.internal.xsltc.compiler;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.classfile.Field;
/*     */ import com.sun.org.apache.bcel.internal.generic.ALOAD;
/*     */ import com.sun.org.apache.bcel.internal.generic.ASTORE;
/*     */ import com.sun.org.apache.bcel.internal.generic.BranchHandle;
/*     */ import com.sun.org.apache.bcel.internal.generic.CHECKCAST;
/*     */ import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
/*     */ import com.sun.org.apache.bcel.internal.generic.GETFIELD;
/*     */ import com.sun.org.apache.bcel.internal.generic.GOTO;
/*     */ import com.sun.org.apache.bcel.internal.generic.IFNONNULL;
/*     */ import com.sun.org.apache.bcel.internal.generic.ILOAD;
/*     */ import com.sun.org.apache.bcel.internal.generic.INVOKESPECIAL;
/*     */ import com.sun.org.apache.bcel.internal.generic.INVOKESTATIC;
/*     */ import com.sun.org.apache.bcel.internal.generic.INVOKEVIRTUAL;
/*     */ import com.sun.org.apache.bcel.internal.generic.InstructionList;
/*     */ import com.sun.org.apache.bcel.internal.generic.LocalVariableGen;
/*     */ import com.sun.org.apache.bcel.internal.generic.NEW;
/*     */ import com.sun.org.apache.bcel.internal.generic.PUSH;
/*     */ import com.sun.org.apache.bcel.internal.generic.PUTFIELD;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MatchGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.NodeCounterGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.RealType;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util;
/*     */ import java.util.ArrayList;
/*     */ import org.xml.sax.helpers.AttributesImpl;
/*     */ 
/*     */ final class Number extends Instruction
/*     */   implements Closure
/*     */ {
/*     */   private static final int LEVEL_SINGLE = 0;
/*     */   private static final int LEVEL_MULTIPLE = 1;
/*     */   private static final int LEVEL_ANY = 2;
/*  65 */   private static final String[] ClassNames = { "com.sun.org.apache.xalan.internal.xsltc.dom.SingleNodeCounter", "com.sun.org.apache.xalan.internal.xsltc.dom.MultipleNodeCounter", "com.sun.org.apache.xalan.internal.xsltc.dom.AnyNodeCounter" };
/*     */ 
/*  71 */   private static final String[] FieldNames = { "___single_node_counter", "___multiple_node_counter", "___any_node_counter" };
/*     */ 
/*  77 */   private Pattern _from = null;
/*  78 */   private Pattern _count = null;
/*  79 */   private Expression _value = null;
/*     */ 
/*  81 */   private AttributeValueTemplate _lang = null;
/*  82 */   private AttributeValueTemplate _format = null;
/*  83 */   private AttributeValueTemplate _letterValue = null;
/*  84 */   private AttributeValueTemplate _groupingSeparator = null;
/*  85 */   private AttributeValueTemplate _groupingSize = null;
/*     */ 
/*  87 */   private int _level = 0;
/*  88 */   private boolean _formatNeeded = false;
/*     */ 
/*  90 */   private String _className = null;
/*  91 */   private ArrayList _closureVars = null;
/*     */ 
/*     */   public boolean inInnerClass()
/*     */   {
/* 100 */     return this._className != null;
/*     */   }
/*     */ 
/*     */   public Closure getParentClosure()
/*     */   {
/* 107 */     return null;
/*     */   }
/*     */ 
/*     */   public String getInnerClassName()
/*     */   {
/* 115 */     return this._className;
/*     */   }
/*     */ 
/*     */   public void addVariable(VariableRefBase variableRef)
/*     */   {
/* 122 */     if (this._closureVars == null) {
/* 123 */       this._closureVars = new ArrayList();
/*     */     }
/*     */ 
/* 127 */     if (!this._closureVars.contains(variableRef))
/* 128 */       this._closureVars.add(variableRef);
/*     */   }
/*     */ 
/*     */   public void parseContents(Parser parser)
/*     */   {
/* 135 */     int count = this._attributes.getLength();
/*     */ 
/* 137 */     for (int i = 0; i < count; i++) {
/* 138 */       String name = this._attributes.getQName(i);
/* 139 */       String value = this._attributes.getValue(i);
/*     */ 
/* 141 */       if (name.equals("value")) {
/* 142 */         this._value = parser.parseExpression(this, name, null);
/*     */       }
/* 144 */       else if (name.equals("count")) {
/* 145 */         this._count = parser.parsePattern(this, name, null);
/*     */       }
/* 147 */       else if (name.equals("from")) {
/* 148 */         this._from = parser.parsePattern(this, name, null);
/*     */       }
/* 150 */       else if (name.equals("level")) {
/* 151 */         if (value.equals("single")) {
/* 152 */           this._level = 0;
/*     */         }
/* 154 */         else if (value.equals("multiple")) {
/* 155 */           this._level = 1;
/*     */         }
/* 157 */         else if (value.equals("any")) {
/* 158 */           this._level = 2;
/*     */         }
/*     */       }
/* 161 */       else if (name.equals("format")) {
/* 162 */         this._format = new AttributeValueTemplate(value, parser, this);
/* 163 */         this._formatNeeded = true;
/*     */       }
/* 165 */       else if (name.equals("lang")) {
/* 166 */         this._lang = new AttributeValueTemplate(value, parser, this);
/* 167 */         this._formatNeeded = true;
/*     */       }
/* 169 */       else if (name.equals("letter-value")) {
/* 170 */         this._letterValue = new AttributeValueTemplate(value, parser, this);
/* 171 */         this._formatNeeded = true;
/*     */       }
/* 173 */       else if (name.equals("grouping-separator")) {
/* 174 */         this._groupingSeparator = new AttributeValueTemplate(value, parser, this);
/* 175 */         this._formatNeeded = true;
/*     */       }
/* 177 */       else if (name.equals("grouping-size")) {
/* 178 */         this._groupingSize = new AttributeValueTemplate(value, parser, this);
/* 179 */         this._formatNeeded = true;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type typeCheck(SymbolTable stable) throws TypeCheckError {
/* 185 */     if (this._value != null) {
/* 186 */       com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type tvalue = this._value.typeCheck(stable);
/* 187 */       if (!(tvalue instanceof RealType)) {
/* 188 */         this._value = new CastExpr(this._value, com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type.Real);
/*     */       }
/*     */     }
/* 191 */     if (this._count != null) {
/* 192 */       this._count.typeCheck(stable);
/*     */     }
/* 194 */     if (this._from != null) {
/* 195 */       this._from.typeCheck(stable);
/*     */     }
/* 197 */     if (this._format != null) {
/* 198 */       this._format.typeCheck(stable);
/*     */     }
/* 200 */     if (this._lang != null) {
/* 201 */       this._lang.typeCheck(stable);
/*     */     }
/* 203 */     if (this._letterValue != null) {
/* 204 */       this._letterValue.typeCheck(stable);
/*     */     }
/* 206 */     if (this._groupingSeparator != null) {
/* 207 */       this._groupingSeparator.typeCheck(stable);
/*     */     }
/* 209 */     if (this._groupingSize != null) {
/* 210 */       this._groupingSize.typeCheck(stable);
/*     */     }
/* 212 */     return com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type.Void;
/*     */   }
/*     */ 
/*     */   public boolean hasValue()
/*     */   {
/* 219 */     return this._value != null;
/*     */   }
/*     */ 
/*     */   public boolean isDefault()
/*     */   {
/* 227 */     return (this._from == null) && (this._count == null);
/*     */   }
/*     */ 
/*     */   private void compileDefault(ClassGenerator classGen, MethodGenerator methodGen)
/*     */   {
/* 233 */     ConstantPoolGen cpg = classGen.getConstantPool();
/* 234 */     InstructionList il = methodGen.getInstructionList();
/*     */ 
/* 236 */     int[] fieldIndexes = getXSLTC().getNumberFieldIndexes();
/*     */ 
/* 238 */     if (fieldIndexes[this._level] == -1) {
/* 239 */       Field defaultNode = new Field(2, cpg.addUtf8(FieldNames[this._level]), cpg.addUtf8("Lcom/sun/org/apache/xalan/internal/xsltc/dom/NodeCounter;"), null, cpg.getConstantPool());
/*     */ 
/* 246 */       classGen.addField(defaultNode);
/*     */ 
/* 249 */       fieldIndexes[this._level] = cpg.addFieldref(classGen.getClassName(), FieldNames[this._level], "Lcom/sun/org/apache/xalan/internal/xsltc/dom/NodeCounter;");
/*     */     }
/*     */ 
/* 255 */     il.append(classGen.loadTranslet());
/* 256 */     il.append(new GETFIELD(fieldIndexes[this._level]));
/* 257 */     BranchHandle ifBlock1 = il.append(new IFNONNULL(null));
/*     */ 
/* 260 */     int index = cpg.addMethodref(ClassNames[this._level], "getDefaultNodeCounter", "(Lcom/sun/org/apache/xalan/internal/xsltc/Translet;Lcom/sun/org/apache/xalan/internal/xsltc/DOM;Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;)Lcom/sun/org/apache/xalan/internal/xsltc/dom/NodeCounter;");
/*     */ 
/* 266 */     il.append(classGen.loadTranslet());
/* 267 */     il.append(methodGen.loadDOM());
/* 268 */     il.append(methodGen.loadIterator());
/* 269 */     il.append(new INVOKESTATIC(index));
/* 270 */     il.append(DUP);
/*     */ 
/* 273 */     il.append(classGen.loadTranslet());
/* 274 */     il.append(SWAP);
/* 275 */     il.append(new PUTFIELD(fieldIndexes[this._level]));
/* 276 */     BranchHandle ifBlock2 = il.append(new GOTO(null));
/*     */ 
/* 279 */     ifBlock1.setTarget(il.append(classGen.loadTranslet()));
/* 280 */     il.append(new GETFIELD(fieldIndexes[this._level]));
/*     */ 
/* 282 */     ifBlock2.setTarget(il.append(NOP));
/*     */   }
/*     */ 
/*     */   private void compileConstructor(ClassGenerator classGen)
/*     */   {
/* 292 */     InstructionList il = new InstructionList();
/* 293 */     ConstantPoolGen cpg = classGen.getConstantPool();
/*     */ 
/* 295 */     MethodGenerator cons = new MethodGenerator(1, com.sun.org.apache.bcel.internal.generic.Type.VOID, new com.sun.org.apache.bcel.internal.generic.Type[] { Util.getJCRefType("Lcom/sun/org/apache/xalan/internal/xsltc/Translet;"), Util.getJCRefType("Lcom/sun/org/apache/xalan/internal/xsltc/DOM;"), Util.getJCRefType("Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;"), com.sun.org.apache.bcel.internal.generic.Type.BOOLEAN }, new String[] { "dom", "translet", "iterator", "hasFrom" }, "<init>", this._className, il, cpg);
/*     */ 
/* 311 */     il.append(ALOAD_0);
/* 312 */     il.append(ALOAD_1);
/* 313 */     il.append(ALOAD_2);
/* 314 */     il.append(new ALOAD(3));
/* 315 */     il.append(new ILOAD(4));
/*     */ 
/* 317 */     int index = cpg.addMethodref(ClassNames[this._level], "<init>", "(Lcom/sun/org/apache/xalan/internal/xsltc/Translet;Lcom/sun/org/apache/xalan/internal/xsltc/DOM;Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;Z)V");
/*     */ 
/* 323 */     il.append(new INVOKESPECIAL(index));
/* 324 */     il.append(RETURN);
/*     */ 
/* 326 */     classGen.addMethod(cons);
/*     */   }
/*     */ 
/*     */   private void compileLocals(NodeCounterGenerator nodeCounterGen, MatchGenerator matchGen, InstructionList il)
/*     */   {
/* 339 */     ConstantPoolGen cpg = nodeCounterGen.getConstantPool();
/*     */ 
/* 342 */     LocalVariableGen local = matchGen.addLocalVariable("iterator", Util.getJCRefType("Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;"), null, null);
/*     */ 
/* 345 */     int field = cpg.addFieldref("com.sun.org.apache.xalan.internal.xsltc.dom.NodeCounter", "_iterator", "Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;");
/*     */ 
/* 347 */     il.append(ALOAD_0);
/* 348 */     il.append(new GETFIELD(field));
/* 349 */     local.setStart(il.append(new ASTORE(local.getIndex())));
/* 350 */     matchGen.setIteratorIndex(local.getIndex());
/*     */ 
/* 353 */     local = matchGen.addLocalVariable("translet", Util.getJCRefType("Lcom/sun/org/apache/xalan/internal/xsltc/runtime/AbstractTranslet;"), null, null);
/*     */ 
/* 356 */     field = cpg.addFieldref("com.sun.org.apache.xalan.internal.xsltc.dom.NodeCounter", "_translet", "Lcom/sun/org/apache/xalan/internal/xsltc/Translet;");
/*     */ 
/* 358 */     il.append(ALOAD_0);
/* 359 */     il.append(new GETFIELD(field));
/* 360 */     il.append(new CHECKCAST(cpg.addClass("com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet")));
/* 361 */     local.setStart(il.append(new ASTORE(local.getIndex())));
/* 362 */     nodeCounterGen.setTransletIndex(local.getIndex());
/*     */ 
/* 365 */     local = matchGen.addLocalVariable("document", Util.getJCRefType("Lcom/sun/org/apache/xalan/internal/xsltc/DOM;"), null, null);
/*     */ 
/* 368 */     field = cpg.addFieldref(this._className, "_document", "Lcom/sun/org/apache/xalan/internal/xsltc/DOM;");
/* 369 */     il.append(ALOAD_0);
/* 370 */     il.append(new GETFIELD(field));
/*     */ 
/* 372 */     local.setStart(il.append(new ASTORE(local.getIndex())));
/* 373 */     matchGen.setDomIndex(local.getIndex());
/*     */   }
/*     */ 
/*     */   private void compilePatterns(ClassGenerator classGen, MethodGenerator methodGen)
/*     */   {
/* 385 */     this._className = getXSLTC().getHelperClassName();
/* 386 */     NodeCounterGenerator nodeCounterGen = new NodeCounterGenerator(this._className, ClassNames[this._level], toString(), 33, null, classGen.getStylesheet());
/*     */ 
/* 392 */     InstructionList il = null;
/* 393 */     ConstantPoolGen cpg = nodeCounterGen.getConstantPool();
/*     */ 
/* 396 */     int closureLen = this._closureVars == null ? 0 : this._closureVars.size();
/*     */ 
/* 399 */     for (int i = 0; i < closureLen; i++) {
/* 400 */       VariableBase var = ((VariableRefBase)this._closureVars.get(i)).getVariable();
/*     */ 
/* 403 */       nodeCounterGen.addField(new Field(1, cpg.addUtf8(var.getEscapedName()), cpg.addUtf8(var.getType().toSignature()), null, cpg.getConstantPool()));
/*     */     }
/*     */ 
/* 410 */     compileConstructor(nodeCounterGen);
/*     */ 
/* 415 */     if (this._from != null) {
/* 416 */       il = new InstructionList();
/* 417 */       MatchGenerator matchGen = new MatchGenerator(17, com.sun.org.apache.bcel.internal.generic.Type.BOOLEAN, new com.sun.org.apache.bcel.internal.generic.Type[] { com.sun.org.apache.bcel.internal.generic.Type.INT }, new String[] { "node" }, "matchesFrom", this._className, il, cpg);
/*     */ 
/* 428 */       compileLocals(nodeCounterGen, matchGen, il);
/*     */ 
/* 431 */       il.append(matchGen.loadContextNode());
/* 432 */       this._from.translate(nodeCounterGen, matchGen);
/* 433 */       this._from.synthesize(nodeCounterGen, matchGen);
/* 434 */       il.append(IRETURN);
/*     */ 
/* 436 */       nodeCounterGen.addMethod(matchGen);
/*     */     }
/*     */ 
/* 442 */     if (this._count != null) {
/* 443 */       il = new InstructionList();
/* 444 */       MatchGenerator matchGen = new MatchGenerator(17, com.sun.org.apache.bcel.internal.generic.Type.BOOLEAN, new com.sun.org.apache.bcel.internal.generic.Type[] { com.sun.org.apache.bcel.internal.generic.Type.INT }, new String[] { "node" }, "matchesCount", this._className, il, cpg);
/*     */ 
/* 454 */       compileLocals(nodeCounterGen, matchGen, il);
/*     */ 
/* 457 */       il.append(matchGen.loadContextNode());
/* 458 */       this._count.translate(nodeCounterGen, matchGen);
/* 459 */       this._count.synthesize(nodeCounterGen, matchGen);
/*     */ 
/* 461 */       il.append(IRETURN);
/*     */ 
/* 463 */       nodeCounterGen.addMethod(matchGen);
/*     */     }
/*     */ 
/* 466 */     getXSLTC().dumpClass(nodeCounterGen.getJavaClass());
/*     */ 
/* 469 */     cpg = classGen.getConstantPool();
/* 470 */     il = methodGen.getInstructionList();
/*     */ 
/* 472 */     int index = cpg.addMethodref(this._className, "<init>", "(Lcom/sun/org/apache/xalan/internal/xsltc/Translet;Lcom/sun/org/apache/xalan/internal/xsltc/DOM;Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;Z)V");
/*     */ 
/* 477 */     il.append(new NEW(cpg.addClass(this._className)));
/* 478 */     il.append(DUP);
/* 479 */     il.append(classGen.loadTranslet());
/* 480 */     il.append(methodGen.loadDOM());
/* 481 */     il.append(methodGen.loadIterator());
/* 482 */     il.append(this._from != null ? ICONST_1 : ICONST_0);
/* 483 */     il.append(new INVOKESPECIAL(index));
/*     */ 
/* 486 */     for (int i = 0; i < closureLen; i++) {
/* 487 */       VariableRefBase varRef = (VariableRefBase)this._closureVars.get(i);
/* 488 */       VariableBase var = varRef.getVariable();
/* 489 */       com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type varType = var.getType();
/*     */ 
/* 492 */       il.append(DUP);
/* 493 */       il.append(var.loadInstruction());
/* 494 */       il.append(new PUTFIELD(cpg.addFieldref(this._className, var.getEscapedName(), varType.toSignature())));
/*     */     }
/*     */   }
/*     */ 
/*     */   public void translate(ClassGenerator classGen, MethodGenerator methodGen)
/*     */   {
/* 502 */     ConstantPoolGen cpg = classGen.getConstantPool();
/* 503 */     InstructionList il = methodGen.getInstructionList();
/*     */ 
/* 506 */     il.append(classGen.loadTranslet());
/*     */ 
/* 508 */     if (hasValue()) {
/* 509 */       compileDefault(classGen, methodGen);
/* 510 */       this._value.translate(classGen, methodGen);
/*     */ 
/* 513 */       il.append(new PUSH(cpg, 0.5D));
/* 514 */       il.append(DADD);
/* 515 */       int index = cpg.addMethodref("java.lang.Math", "floor", "(D)D");
/* 516 */       il.append(new INVOKESTATIC(index));
/*     */ 
/* 519 */       index = cpg.addMethodref("com.sun.org.apache.xalan.internal.xsltc.dom.NodeCounter", "setValue", "(D)Lcom/sun/org/apache/xalan/internal/xsltc/dom/NodeCounter;");
/*     */ 
/* 522 */       il.append(new INVOKEVIRTUAL(index));
/*     */     }
/* 524 */     else if (isDefault()) {
/* 525 */       compileDefault(classGen, methodGen);
/*     */     }
/*     */     else {
/* 528 */       compilePatterns(classGen, methodGen);
/*     */     }
/*     */ 
/* 532 */     if (!hasValue()) {
/* 533 */       il.append(methodGen.loadContextNode());
/* 534 */       int index = cpg.addMethodref("com.sun.org.apache.xalan.internal.xsltc.dom.NodeCounter", "setStartNode", "(I)Lcom/sun/org/apache/xalan/internal/xsltc/dom/NodeCounter;");
/*     */ 
/* 537 */       il.append(new INVOKEVIRTUAL(index));
/*     */     }
/*     */ 
/* 541 */     if (this._formatNeeded) {
/* 542 */       if (this._format != null) {
/* 543 */         this._format.translate(classGen, methodGen);
/*     */       }
/*     */       else {
/* 546 */         il.append(new PUSH(cpg, "1"));
/*     */       }
/*     */ 
/* 549 */       if (this._lang != null) {
/* 550 */         this._lang.translate(classGen, methodGen);
/*     */       }
/*     */       else {
/* 553 */         il.append(new PUSH(cpg, "en"));
/*     */       }
/*     */ 
/* 556 */       if (this._letterValue != null) {
/* 557 */         this._letterValue.translate(classGen, methodGen);
/*     */       }
/*     */       else {
/* 560 */         il.append(new PUSH(cpg, ""));
/*     */       }
/*     */ 
/* 563 */       if (this._groupingSeparator != null) {
/* 564 */         this._groupingSeparator.translate(classGen, methodGen);
/*     */       }
/*     */       else {
/* 567 */         il.append(new PUSH(cpg, ""));
/*     */       }
/*     */ 
/* 570 */       if (this._groupingSize != null) {
/* 571 */         this._groupingSize.translate(classGen, methodGen);
/*     */       }
/*     */       else {
/* 574 */         il.append(new PUSH(cpg, "0"));
/*     */       }
/*     */ 
/* 577 */       int index = cpg.addMethodref("com.sun.org.apache.xalan.internal.xsltc.dom.NodeCounter", "getCounter", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;");
/*     */ 
/* 581 */       il.append(new INVOKEVIRTUAL(index));
/*     */     }
/*     */     else {
/* 584 */       index = cpg.addMethodref("com.sun.org.apache.xalan.internal.xsltc.dom.NodeCounter", "setDefaultFormatting", "()Lcom/sun/org/apache/xalan/internal/xsltc/dom/NodeCounter;");
/*     */ 
/* 586 */       il.append(new INVOKEVIRTUAL(index));
/*     */ 
/* 588 */       index = cpg.addMethodref("com.sun.org.apache.xalan.internal.xsltc.dom.NodeCounter", "getCounter", "()Ljava/lang/String;");
/*     */ 
/* 590 */       il.append(new INVOKEVIRTUAL(index));
/*     */     }
/*     */ 
/* 594 */     il.append(methodGen.loadHandler());
/* 595 */     int index = cpg.addMethodref("com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet", "characters", "(Ljava/lang/String;Lcom/sun/org/apache/xml/internal/serializer/SerializationHandler;)V");
/*     */ 
/* 598 */     il.append(new INVOKEVIRTUAL(index));
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.Number
 * JD-Core Version:    0.6.2
 */