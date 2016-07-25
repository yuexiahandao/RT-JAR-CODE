/*     */ package com.sun.org.apache.xalan.internal.xsltc.compiler;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.generic.ALOAD;
/*     */ import com.sun.org.apache.bcel.internal.generic.BranchHandle;
/*     */ import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
/*     */ import com.sun.org.apache.bcel.internal.generic.IF_ICMPEQ;
/*     */ import com.sun.org.apache.bcel.internal.generic.ILOAD;
/*     */ import com.sun.org.apache.bcel.internal.generic.INVOKEINTERFACE;
/*     */ import com.sun.org.apache.bcel.internal.generic.INVOKEVIRTUAL;
/*     */ import com.sun.org.apache.bcel.internal.generic.InstructionHandle;
/*     */ import com.sun.org.apache.bcel.internal.generic.InstructionList;
/*     */ import com.sun.org.apache.bcel.internal.generic.PUSH;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util;
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.Vector;
/*     */ 
/*     */ final class Whitespace extends TopLevelElement
/*     */ {
/*     */   public static final int USE_PREDICATE = 0;
/*     */   public static final int STRIP_SPACE = 1;
/*     */   public static final int PRESERVE_SPACE = 2;
/*     */   public static final int RULE_NONE = 0;
/*     */   public static final int RULE_ELEMENT = 1;
/*     */   public static final int RULE_NAMESPACE = 2;
/*     */   public static final int RULE_ALL = 3;
/*     */   private String _elementList;
/*     */   private int _action;
/*     */   private int _importPrecedence;
/*     */ 
/*     */   public void parseContents(Parser parser)
/*     */   {
/* 134 */     this._action = (this._qname.getLocalPart().endsWith("strip-space") ? 1 : 2);
/*     */ 
/* 138 */     this._importPrecedence = parser.getCurrentImportPrecedence();
/*     */ 
/* 141 */     this._elementList = getAttribute("elements");
/* 142 */     if ((this._elementList == null) || (this._elementList.length() == 0)) {
/* 143 */       reportError(this, parser, "REQUIRED_ATTR_ERR", "elements");
/* 144 */       return;
/*     */     }
/*     */ 
/* 147 */     SymbolTable stable = parser.getSymbolTable();
/* 148 */     StringTokenizer list = new StringTokenizer(this._elementList);
/* 149 */     StringBuffer elements = new StringBuffer("");
/*     */ 
/* 151 */     while (list.hasMoreElements()) {
/* 152 */       String token = list.nextToken();
/*     */ 
/* 155 */       int col = token.indexOf(':');
/*     */ 
/* 157 */       if (col != -1) {
/* 158 */         String namespace = lookupNamespace(token.substring(0, col));
/* 159 */         if (namespace != null)
/* 160 */           elements.append(namespace).append(':').append(token.substring(col + 1));
/*     */         else
/* 162 */           elements.append(token);
/*     */       }
/*     */       else {
/* 165 */         elements.append(token);
/*     */       }
/*     */ 
/* 168 */       if (list.hasMoreElements())
/* 169 */         elements.append(" ");
/*     */     }
/* 171 */     this._elementList = elements.toString();
/*     */   }
/*     */ 
/*     */   public Vector getRules()
/*     */   {
/* 180 */     Vector rules = new Vector();
/*     */ 
/* 182 */     StringTokenizer list = new StringTokenizer(this._elementList);
/* 183 */     while (list.hasMoreElements()) {
/* 184 */       rules.add(new WhitespaceRule(this._action, list.nextToken(), this._importPrecedence));
/*     */     }
/*     */ 
/* 188 */     return rules;
/*     */   }
/*     */ 
/*     */   private static WhitespaceRule findContradictingRule(Vector rules, WhitespaceRule rule)
/*     */   {
/* 198 */     for (int i = 0; i < rules.size(); i++)
/*     */     {
/* 200 */       WhitespaceRule currentRule = (WhitespaceRule)rules.elementAt(i);
/*     */ 
/* 202 */       if (currentRule == rule) {
/* 203 */         return null;
/*     */       }
/*     */ 
/* 211 */       switch (currentRule.getStrength()) {
/*     */       case 3:
/* 213 */         return currentRule;
/*     */       case 1:
/* 216 */         if (!rule.getElement().equals(currentRule.getElement()))
/*     */         {
/*     */           break;
/*     */         }
/*     */       case 2:
/* 221 */         if (rule.getNamespace().equals(currentRule.getNamespace())) {
/* 222 */           return currentRule;
/*     */         }
/*     */         break;
/*     */       }
/*     */     }
/* 227 */     return null;
/*     */   }
/*     */ 
/*     */   private static int prioritizeRules(Vector rules)
/*     */   {
/* 237 */     int defaultAction = 2;
/*     */ 
/* 240 */     quicksort(rules, 0, rules.size() - 1);
/*     */ 
/* 245 */     boolean strip = false;
/* 246 */     for (int i = 0; i < rules.size(); i++) {
/* 247 */       WhitespaceRule currentRule = (WhitespaceRule)rules.elementAt(i);
/* 248 */       if (currentRule.getAction() == 1) {
/* 249 */         strip = true;
/*     */       }
/*     */     }
/*     */ 
/* 253 */     if (!strip) {
/* 254 */       rules.removeAllElements();
/* 255 */       return 2;
/*     */     }
/*     */ 
/* 259 */     for (int idx = 0; idx < rules.size(); ) {
/* 260 */       WhitespaceRule currentRule = (WhitespaceRule)rules.elementAt(idx);
/*     */ 
/* 263 */       if (findContradictingRule(rules, currentRule) != null) {
/* 264 */         rules.remove(idx);
/*     */       }
/*     */       else
/*     */       {
/* 268 */         if (currentRule.getStrength() == 3) {
/* 269 */           defaultAction = currentRule.getAction();
/* 270 */           for (int i = idx; i < rules.size(); i++) {
/* 271 */             rules.removeElementAt(i);
/*     */           }
/*     */         }
/*     */ 
/* 275 */         idx++;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 280 */     if (rules.size() == 0) {
/* 281 */       return defaultAction;
/*     */     }
/*     */ 
/*     */     do
/*     */     {
/* 287 */       WhitespaceRule currentRule = (WhitespaceRule)rules.lastElement();
/* 288 */       if (currentRule.getAction() != defaultAction) break;
/* 289 */       rules.removeElementAt(rules.size() - 1);
/*     */     }
/*     */ 
/* 294 */     while (rules.size() > 0);
/*     */ 
/* 297 */     return defaultAction;
/*     */   }
/*     */ 
/*     */   public static void compileStripSpace(BranchHandle[] strip, int sCount, InstructionList il)
/*     */   {
/* 303 */     InstructionHandle target = il.append(ICONST_1);
/* 304 */     il.append(IRETURN);
/* 305 */     for (int i = 0; i < sCount; i++)
/* 306 */       strip[i].setTarget(target);
/*     */   }
/*     */ 
/*     */   public static void compilePreserveSpace(BranchHandle[] preserve, int pCount, InstructionList il)
/*     */   {
/* 313 */     InstructionHandle target = il.append(ICONST_0);
/* 314 */     il.append(IRETURN);
/* 315 */     for (int i = 0; i < pCount; i++)
/* 316 */       preserve[i].setTarget(target);
/*     */   }
/*     */ 
/*     */   private static void compilePredicate(Vector rules, int defaultAction, ClassGenerator classGen)
/*     */   {
/* 338 */     ConstantPoolGen cpg = classGen.getConstantPool();
/* 339 */     InstructionList il = new InstructionList();
/* 340 */     XSLTC xsltc = classGen.getParser().getXSLTC();
/*     */ 
/* 343 */     MethodGenerator stripSpace = new MethodGenerator(17, com.sun.org.apache.bcel.internal.generic.Type.BOOLEAN, new com.sun.org.apache.bcel.internal.generic.Type[] { Util.getJCRefType("Lcom/sun/org/apache/xalan/internal/xsltc/DOM;"), com.sun.org.apache.bcel.internal.generic.Type.INT, com.sun.org.apache.bcel.internal.generic.Type.INT }, new String[] { "dom", "node", "type" }, "stripSpace", classGen.getClassName(), il, cpg);
/*     */ 
/* 354 */     classGen.addInterface("com/sun/org/apache/xalan/internal/xsltc/StripFilter");
/*     */ 
/* 356 */     int paramDom = stripSpace.getLocalIndex("dom");
/* 357 */     int paramCurrent = stripSpace.getLocalIndex("node");
/* 358 */     int paramType = stripSpace.getLocalIndex("type");
/*     */ 
/* 360 */     BranchHandle[] strip = new BranchHandle[rules.size()];
/* 361 */     BranchHandle[] preserve = new BranchHandle[rules.size()];
/* 362 */     int sCount = 0;
/* 363 */     int pCount = 0;
/*     */ 
/* 366 */     for (int i = 0; i < rules.size(); i++)
/*     */     {
/* 368 */       WhitespaceRule rule = (WhitespaceRule)rules.elementAt(i);
/*     */ 
/* 371 */       int gns = cpg.addInterfaceMethodref("com.sun.org.apache.xalan.internal.xsltc.DOM", "getNamespaceName", "(I)Ljava/lang/String;");
/*     */ 
/* 375 */       int strcmp = cpg.addMethodref("java/lang/String", "compareTo", "(Ljava/lang/String;)I");
/*     */ 
/* 380 */       if (rule.getStrength() == 2) {
/* 381 */         il.append(new ALOAD(paramDom));
/* 382 */         il.append(new ILOAD(paramCurrent));
/* 383 */         il.append(new INVOKEINTERFACE(gns, 2));
/* 384 */         il.append(new PUSH(cpg, rule.getNamespace()));
/* 385 */         il.append(new INVOKEVIRTUAL(strcmp));
/* 386 */         il.append(ICONST_0);
/*     */ 
/* 388 */         if (rule.getAction() == 1) {
/* 389 */           strip[(sCount++)] = il.append(new IF_ICMPEQ(null));
/*     */         }
/*     */         else {
/* 392 */           preserve[(pCount++)] = il.append(new IF_ICMPEQ(null));
/*     */         }
/*     */ 
/*     */       }
/* 396 */       else if (rule.getStrength() == 1)
/*     */       {
/* 398 */         Parser parser = classGen.getParser();
/*     */         QName qname;
/*     */         QName qname;
/* 400 */         if (rule.getNamespace() != "") {
/* 401 */           qname = parser.getQName(rule.getNamespace(), null, rule.getElement());
/*     */         }
/*     */         else {
/* 404 */           qname = parser.getQName(rule.getElement());
/*     */         }
/*     */ 
/* 407 */         int elementType = xsltc.registerElement(qname);
/* 408 */         il.append(new ILOAD(paramType));
/* 409 */         il.append(new PUSH(cpg, elementType));
/*     */ 
/* 412 */         if (rule.getAction() == 1)
/* 413 */           strip[(sCount++)] = il.append(new IF_ICMPEQ(null));
/*     */         else {
/* 415 */           preserve[(pCount++)] = il.append(new IF_ICMPEQ(null));
/*     */         }
/*     */       }
/*     */     }
/* 419 */     if (defaultAction == 1) {
/* 420 */       compileStripSpace(strip, sCount, il);
/* 421 */       compilePreserveSpace(preserve, pCount, il);
/*     */     }
/*     */     else {
/* 424 */       compilePreserveSpace(preserve, pCount, il);
/* 425 */       compileStripSpace(strip, sCount, il);
/*     */     }
/*     */ 
/* 428 */     classGen.addMethod(stripSpace);
/*     */   }
/*     */ 
/*     */   private static void compileDefault(int defaultAction, ClassGenerator classGen)
/*     */   {
/* 436 */     ConstantPoolGen cpg = classGen.getConstantPool();
/* 437 */     InstructionList il = new InstructionList();
/* 438 */     XSLTC xsltc = classGen.getParser().getXSLTC();
/*     */ 
/* 441 */     MethodGenerator stripSpace = new MethodGenerator(17, com.sun.org.apache.bcel.internal.generic.Type.BOOLEAN, new com.sun.org.apache.bcel.internal.generic.Type[] { Util.getJCRefType("Lcom/sun/org/apache/xalan/internal/xsltc/DOM;"), com.sun.org.apache.bcel.internal.generic.Type.INT, com.sun.org.apache.bcel.internal.generic.Type.INT }, new String[] { "dom", "node", "type" }, "stripSpace", classGen.getClassName(), il, cpg);
/*     */ 
/* 452 */     classGen.addInterface("com/sun/org/apache/xalan/internal/xsltc/StripFilter");
/*     */ 
/* 454 */     if (defaultAction == 1)
/* 455 */       il.append(ICONST_1);
/*     */     else
/* 457 */       il.append(ICONST_0);
/* 458 */     il.append(IRETURN);
/*     */ 
/* 460 */     classGen.addMethod(stripSpace);
/*     */   }
/*     */ 
/*     */   public static int translateRules(Vector rules, ClassGenerator classGen)
/*     */   {
/* 475 */     int defaultAction = prioritizeRules(rules);
/*     */ 
/* 477 */     if (rules.size() == 0) {
/* 478 */       compileDefault(defaultAction, classGen);
/* 479 */       return defaultAction;
/*     */     }
/*     */ 
/* 482 */     compilePredicate(rules, defaultAction, classGen);
/*     */ 
/* 484 */     return 0;
/*     */   }
/*     */ 
/*     */   private static void quicksort(Vector rules, int p, int r)
/*     */   {
/* 491 */     while (p < r) {
/* 492 */       int q = partition(rules, p, r);
/* 493 */       quicksort(rules, p, q);
/* 494 */       p = q + 1;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static int partition(Vector rules, int p, int r)
/*     */   {
/* 502 */     WhitespaceRule x = (WhitespaceRule)rules.elementAt(p + r >>> 1);
/* 503 */     int i = p - 1; int j = r + 1;
/*     */     while (true) {
/* 505 */       if (x.compareTo((WhitespaceRule)rules.elementAt(--j)) >= 0)
/*     */       {
/* 507 */         while (x.compareTo((WhitespaceRule)rules.elementAt(++i)) > 0);
/* 509 */         if (i >= j) break;
/* 510 */         WhitespaceRule tmp = (WhitespaceRule)rules.elementAt(i);
/* 511 */         rules.setElementAt(rules.elementAt(j), i);
/* 512 */         rules.setElementAt(tmp, j);
/*     */       }
/*     */     }
/* 515 */     return j;
/*     */   }
/*     */ 
/*     */   public com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type typeCheck(SymbolTable stable)
/*     */     throws TypeCheckError
/*     */   {
/* 524 */     return com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type.Void;
/*     */   }
/*     */ 
/*     */   public void translate(ClassGenerator classGen, MethodGenerator methodGen)
/*     */   {
/*     */   }
/*     */ 
/*     */   private static final class WhitespaceRule
/*     */   {
/*     */     private final int _action;
/*     */     private String _namespace;
/*     */     private String _element;
/*     */     private int _type;
/*     */     private int _priority;
/*     */ 
/*     */     public WhitespaceRule(int action, String element, int precedence)
/*     */     {
/*  80 */       this._action = action;
/*     */ 
/*  83 */       int colon = element.lastIndexOf(':');
/*  84 */       if (colon >= 0) {
/*  85 */         this._namespace = element.substring(0, colon);
/*  86 */         this._element = element.substring(colon + 1, element.length());
/*     */       }
/*     */       else {
/*  89 */         this._namespace = "";
/*  90 */         this._element = element;
/*     */       }
/*     */ 
/*  94 */       this._priority = (precedence << 2);
/*     */ 
/*  97 */       if (this._element.equals("*")) {
/*  98 */         if (this._namespace == "") {
/*  99 */           this._type = 3;
/* 100 */           this._priority += 2;
/*     */         }
/*     */         else {
/* 103 */           this._type = 2;
/* 104 */           this._priority += 1;
/*     */         }
/*     */       }
/*     */       else
/* 108 */         this._type = 1;
/*     */     }
/*     */ 
/*     */     public int compareTo(WhitespaceRule other)
/*     */     {
/* 116 */       return this._priority > other._priority ? 1 : this._priority < other._priority ? -1 : 0;
/*     */     }
/*     */ 
/*     */     public int getAction()
/*     */     {
/* 121 */       return this._action; } 
/* 122 */     public int getStrength() { return this._type; } 
/* 123 */     public int getPriority() { return this._priority; } 
/* 124 */     public String getElement() { return this._element; } 
/* 125 */     public String getNamespace() { return this._namespace; }
/*     */ 
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.Whitespace
 * JD-Core Version:    0.6.2
 */