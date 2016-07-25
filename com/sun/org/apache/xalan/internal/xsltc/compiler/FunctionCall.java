/*      */ package com.sun.org.apache.xalan.internal.xsltc.compiler;
/*      */ 
/*      */ import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
/*      */ import com.sun.org.apache.bcel.internal.generic.IFEQ;
/*      */ import com.sun.org.apache.bcel.internal.generic.INVOKEINTERFACE;
/*      */ import com.sun.org.apache.bcel.internal.generic.INVOKESPECIAL;
/*      */ import com.sun.org.apache.bcel.internal.generic.INVOKESTATIC;
/*      */ import com.sun.org.apache.bcel.internal.generic.INVOKEVIRTUAL;
/*      */ import com.sun.org.apache.bcel.internal.generic.InstructionConstants;
/*      */ import com.sun.org.apache.bcel.internal.generic.InstructionList;
/*      */ import com.sun.org.apache.bcel.internal.generic.LocalVariableGen;
/*      */ import com.sun.org.apache.bcel.internal.generic.NEW;
/*      */ import com.sun.org.apache.bcel.internal.generic.PUSH;
/*      */ import com.sun.org.apache.xalan.internal.utils.FeatureManager.Feature;
/*      */ import com.sun.org.apache.xalan.internal.utils.ObjectFactory;
/*      */ import com.sun.org.apache.xalan.internal.utils.Objects;
/*      */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.BooleanType;
/*      */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
/*      */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
/*      */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.IntType;
/*      */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
/*      */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodType;
/*      */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MultiHashtable;
/*      */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ObjectType;
/*      */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ReferenceType;
/*      */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
/*      */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
/*      */ import java.io.PrintStream;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.lang.reflect.Method;
/*      */ import java.lang.reflect.Modifier;
/*      */ import java.util.Enumeration;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Vector;
/*      */ 
/*      */ class FunctionCall extends Expression
/*      */ {
/*      */   private QName _fname;
/*      */   private final Vector _arguments;
/*   74 */   private static final Vector EMPTY_ARG_LIST = new Vector(0);
/*      */   protected static final String EXT_XSLTC = "http://xml.apache.org/xalan/xsltc";
/*      */   protected static final String JAVA_EXT_XSLTC = "http://xml.apache.org/xalan/xsltc/java";
/*      */   protected static final String EXT_XALAN = "http://xml.apache.org/xalan";
/*      */   protected static final String JAVA_EXT_XALAN = "http://xml.apache.org/xalan/java";
/*      */   protected static final String JAVA_EXT_XALAN_OLD = "http://xml.apache.org/xslt/java";
/*      */   protected static final String EXSLT_COMMON = "http://exslt.org/common";
/*      */   protected static final String EXSLT_MATH = "http://exslt.org/math";
/*      */   protected static final String EXSLT_SETS = "http://exslt.org/sets";
/*      */   protected static final String EXSLT_DATETIME = "http://exslt.org/dates-and-times";
/*      */   protected static final String EXSLT_STRINGS = "http://exslt.org/strings";
/*      */   protected static final String XALAN_CLASSPACKAGE_NAMESPACE = "xalan://";
/*      */   protected static final int NAMESPACE_FORMAT_JAVA = 0;
/*      */   protected static final int NAMESPACE_FORMAT_CLASS = 1;
/*      */   protected static final int NAMESPACE_FORMAT_PACKAGE = 2;
/*      */   protected static final int NAMESPACE_FORMAT_CLASS_OR_PACKAGE = 3;
/*  117 */   private int _namespace_format = 0;
/*      */ 
/*  122 */   Expression _thisArgument = null;
/*      */   private String _className;
/*      */   private Class _clazz;
/*      */   private Method _chosenMethod;
/*      */   private Constructor _chosenConstructor;
/*      */   private MethodType _chosenMethodType;
/*      */   private boolean unresolvedExternal;
/*  135 */   private boolean _isExtConstructor = false;
/*      */ 
/*  138 */   private boolean _isStatic = false;
/*      */ 
/*  141 */   private static final MultiHashtable _internal2Java = new MultiHashtable();
/*      */ 
/*  144 */   private static final Hashtable _java2Internal = new Hashtable();
/*      */ 
/*  147 */   private static final Hashtable _extensionNamespaceTable = new Hashtable();
/*      */ 
/*  150 */   private static final Hashtable _extensionFunctionTable = new Hashtable();
/*      */ 
/*      */   public FunctionCall(QName fname, Vector arguments)
/*      */   {
/*  279 */     this._fname = fname;
/*  280 */     this._arguments = arguments;
/*  281 */     this._type = null;
/*      */   }
/*      */ 
/*      */   public FunctionCall(QName fname) {
/*  285 */     this(fname, EMPTY_ARG_LIST);
/*      */   }
/*      */ 
/*      */   public String getName() {
/*  289 */     return this._fname.toString();
/*      */   }
/*      */ 
/*      */   public void setParser(Parser parser)
/*      */   {
/*  294 */     super.setParser(parser);
/*  295 */     if (this._arguments != null) {
/*  296 */       int n = this._arguments.size();
/*  297 */       for (int i = 0; i < n; i++) {
/*  298 */         Expression exp = (Expression)this._arguments.elementAt(i);
/*  299 */         exp.setParser(parser);
/*  300 */         exp.setParent(this);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public String getClassNameFromUri(String uri)
/*      */   {
/*  307 */     String className = (String)_extensionNamespaceTable.get(uri);
/*      */ 
/*  309 */     if (className != null) {
/*  310 */       return className;
/*      */     }
/*  312 */     if (uri.startsWith("http://xml.apache.org/xalan/xsltc/java")) {
/*  313 */       int length = "http://xml.apache.org/xalan/xsltc/java".length() + 1;
/*  314 */       return uri.length() > length ? uri.substring(length) : "";
/*      */     }
/*  316 */     if (uri.startsWith("http://xml.apache.org/xalan/java")) {
/*  317 */       int length = "http://xml.apache.org/xalan/java".length() + 1;
/*  318 */       return uri.length() > length ? uri.substring(length) : "";
/*      */     }
/*  320 */     if (uri.startsWith("http://xml.apache.org/xslt/java")) {
/*  321 */       int length = "http://xml.apache.org/xslt/java".length() + 1;
/*  322 */       return uri.length() > length ? uri.substring(length) : "";
/*      */     }
/*      */ 
/*  325 */     int index = uri.lastIndexOf('/');
/*  326 */     return index > 0 ? uri.substring(index + 1) : uri;
/*      */   }
/*      */ 
/*      */   public Type typeCheck(SymbolTable stable)
/*      */     throws TypeCheckError
/*      */   {
/*  339 */     if (this._type != null) return this._type;
/*      */ 
/*  341 */     String namespace = this._fname.getNamespace();
/*  342 */     String local = this._fname.getLocalPart();
/*      */ 
/*  344 */     if (isExtension()) {
/*  345 */       this._fname = new QName(null, null, local);
/*  346 */       return typeCheckStandard(stable);
/*      */     }
/*  348 */     if (isStandard()) {
/*  349 */       return typeCheckStandard(stable);
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/*  354 */       this._className = getClassNameFromUri(namespace);
/*      */ 
/*  356 */       int pos = local.lastIndexOf('.');
/*  357 */       if (pos > 0) {
/*  358 */         this._isStatic = true;
/*  359 */         if ((this._className != null) && (this._className.length() > 0)) {
/*  360 */           this._namespace_format = 2;
/*  361 */           this._className = (this._className + "." + local.substring(0, pos));
/*      */         }
/*      */         else {
/*  364 */           this._namespace_format = 0;
/*  365 */           this._className = local.substring(0, pos);
/*      */         }
/*      */ 
/*  368 */         this._fname = new QName(namespace, null, local.substring(pos + 1));
/*      */       }
/*      */       else {
/*  371 */         if ((this._className != null) && (this._className.length() > 0)) {
/*      */           try {
/*  373 */             this._clazz = ObjectFactory.findProviderClass(this._className, true);
/*  374 */             this._namespace_format = 1;
/*      */           }
/*      */           catch (ClassNotFoundException e) {
/*  377 */             this._namespace_format = 2;
/*      */           }
/*      */         }
/*      */         else {
/*  381 */           this._namespace_format = 0;
/*      */         }
/*  383 */         if (local.indexOf('-') > 0) {
/*  384 */           local = replaceDash(local);
/*      */         }
/*      */ 
/*  387 */         String extFunction = (String)_extensionFunctionTable.get(namespace + ":" + local);
/*  388 */         if (extFunction != null) {
/*  389 */           this._fname = new QName(null, null, extFunction);
/*  390 */           return typeCheckStandard(stable);
/*      */         }
/*      */ 
/*  393 */         this._fname = new QName(namespace, null, local);
/*      */       }
/*      */ 
/*  396 */       return typeCheckExternal(stable);
/*      */     }
/*      */     catch (TypeCheckError e) {
/*  399 */       ErrorMsg errorMsg = e.getErrorMsg();
/*  400 */       if (errorMsg == null) {
/*  401 */         String name = this._fname.getLocalPart();
/*  402 */         errorMsg = new ErrorMsg("METHOD_NOT_FOUND_ERR", name);
/*      */       }
/*  404 */       getParser().reportError(3, errorMsg);
/*  405 */     }return this._type = Type.Void;
/*      */   }
/*      */ 
/*      */   public Type typeCheckStandard(SymbolTable stable)
/*      */     throws TypeCheckError
/*      */   {
/*  416 */     this._fname.clearNamespace();
/*      */ 
/*  418 */     int n = this._arguments.size();
/*  419 */     Vector argsType = typeCheckArgs(stable);
/*  420 */     MethodType args = new MethodType(Type.Void, argsType);
/*  421 */     MethodType ptype = lookupPrimop(stable, this._fname.getLocalPart(), args);
/*      */ 
/*  424 */     if (ptype != null) {
/*  425 */       for (int i = 0; i < n; i++) {
/*  426 */         Type argType = (Type)ptype.argsType().elementAt(i);
/*  427 */         Expression exp = (Expression)this._arguments.elementAt(i);
/*  428 */         if (!argType.identicalTo(exp.getType())) {
/*      */           try {
/*  430 */             this._arguments.setElementAt(new CastExpr(exp, argType), i);
/*      */           }
/*      */           catch (TypeCheckError e) {
/*  433 */             throw new TypeCheckError(this);
/*      */           }
/*      */         }
/*      */       }
/*  437 */       this._chosenMethodType = ptype;
/*  438 */       return this._type = ptype.resultType();
/*      */     }
/*  440 */     throw new TypeCheckError(this);
/*      */   }
/*      */ 
/*      */   public Type typeCheckConstructor(SymbolTable stable)
/*      */     throws TypeCheckError
/*      */   {
/*  446 */     Vector constructors = findConstructors();
/*  447 */     if (constructors == null)
/*      */     {
/*  449 */       throw new TypeCheckError("CONSTRUCTOR_NOT_FOUND", this._className);
/*      */     }
/*      */ 
/*  454 */     int nConstructors = constructors.size();
/*  455 */     int nArgs = this._arguments.size();
/*  456 */     Vector argsType = typeCheckArgs(stable);
/*      */ 
/*  459 */     int bestConstrDistance = 2147483647;
/*  460 */     this._type = null;
/*  461 */     for (int i = 0; i < nConstructors; i++)
/*      */     {
/*  463 */       Constructor constructor = (Constructor)constructors.elementAt(i);
/*      */ 
/*  465 */       Class[] paramTypes = constructor.getParameterTypes();
/*      */ 
/*  467 */       Class extType = null;
/*  468 */       int currConstrDistance = 0;
/*  469 */       for (int j = 0; j < nArgs; j++)
/*      */       {
/*  471 */         extType = paramTypes[j];
/*  472 */         Type intType = (Type)argsType.elementAt(j);
/*  473 */         Object match = _internal2Java.maps(intType, extType);
/*  474 */         if (match != null) {
/*  475 */           currConstrDistance += ((JavaType)match).distance;
/*      */         }
/*  477 */         else if ((intType instanceof ObjectType)) {
/*  478 */           ObjectType objectType = (ObjectType)intType;
/*  479 */           if (objectType.getJavaClass() != extType)
/*      */           {
/*  481 */             if (extType.isAssignableFrom(objectType.getJavaClass())) {
/*  482 */               currConstrDistance++;
/*      */             } else {
/*  484 */               currConstrDistance = 2147483647;
/*  485 */               break;
/*      */             }
/*      */           }
/*      */         }
/*      */         else {
/*  490 */           currConstrDistance = 2147483647;
/*  491 */           break;
/*      */         }
/*      */       }
/*      */ 
/*  495 */       if ((j == nArgs) && (currConstrDistance < bestConstrDistance)) {
/*  496 */         this._chosenConstructor = constructor;
/*  497 */         this._isExtConstructor = true;
/*  498 */         bestConstrDistance = currConstrDistance;
/*      */ 
/*  500 */         this._type = (this._clazz != null ? Type.newObjectType(this._clazz) : Type.newObjectType(this._className));
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  505 */     if (this._type != null) {
/*  506 */       return this._type;
/*      */     }
/*      */ 
/*  509 */     throw new TypeCheckError("ARGUMENT_CONVERSION_ERR", getMethodSignature(argsType));
/*      */   }
/*      */ 
/*      */   public Type typeCheckExternal(SymbolTable stable)
/*      */     throws TypeCheckError
/*      */   {
/*  521 */     int nArgs = this._arguments.size();
/*  522 */     String name = this._fname.getLocalPart();
/*      */ 
/*  525 */     if (this._fname.getLocalPart().equals("new")) {
/*  526 */       return typeCheckConstructor(stable);
/*      */     }
/*      */ 
/*  530 */     boolean hasThisArgument = false;
/*      */ 
/*  532 */     if (nArgs == 0) {
/*  533 */       this._isStatic = true;
/*      */     }
/*  535 */     if (!this._isStatic) {
/*  536 */       if ((this._namespace_format == 0) || (this._namespace_format == 2))
/*      */       {
/*  538 */         hasThisArgument = true;
/*      */       }
/*  540 */       Expression firstArg = (Expression)this._arguments.elementAt(0);
/*  541 */       Type firstArgType = firstArg.typeCheck(stable);
/*      */ 
/*  543 */       if ((this._namespace_format == 1) && ((firstArgType instanceof ObjectType)) && (this._clazz != null) && (this._clazz.isAssignableFrom(((ObjectType)firstArgType).getJavaClass())))
/*      */       {
/*  547 */         hasThisArgument = true;
/*      */       }
/*  549 */       if (hasThisArgument) {
/*  550 */         this._thisArgument = ((Expression)this._arguments.elementAt(0));
/*  551 */         this._arguments.remove(0); nArgs--;
/*  552 */         if ((firstArgType instanceof ObjectType)) {
/*  553 */           this._className = ((ObjectType)firstArgType).getJavaClassName();
/*      */         }
/*      */         else
/*  556 */           throw new TypeCheckError("NO_JAVA_FUNCT_THIS_REF", name);
/*      */       }
/*      */     }
/*  559 */     else if (this._className.length() == 0)
/*      */     {
/*  566 */       Parser parser = getParser();
/*  567 */       if (parser != null) {
/*  568 */         reportWarning(this, parser, "FUNCTION_RESOLVE_ERR", this._fname.toString());
/*      */       }
/*      */ 
/*  571 */       this.unresolvedExternal = true;
/*  572 */       return this._type = Type.Int;
/*      */     }
/*      */ 
/*  576 */     Vector methods = findMethods();
/*      */ 
/*  578 */     if (methods == null)
/*      */     {
/*  580 */       throw new TypeCheckError("METHOD_NOT_FOUND_ERR", this._className + "." + name);
/*      */     }
/*      */ 
/*  583 */     Class extType = null;
/*  584 */     int nMethods = methods.size();
/*  585 */     Vector argsType = typeCheckArgs(stable);
/*      */ 
/*  588 */     int bestMethodDistance = 2147483647;
/*  589 */     this._type = null;
/*  590 */     for (int i = 0; i < nMethods; i++)
/*      */     {
/*  592 */       Method method = (Method)methods.elementAt(i);
/*  593 */       Class[] paramTypes = method.getParameterTypes();
/*      */ 
/*  595 */       int currMethodDistance = 0;
/*  596 */       for (int j = 0; j < nArgs; j++)
/*      */       {
/*  598 */         extType = paramTypes[j];
/*  599 */         Type intType = (Type)argsType.elementAt(j);
/*  600 */         Object match = _internal2Java.maps(intType, extType);
/*  601 */         if (match != null) {
/*  602 */           currMethodDistance += ((JavaType)match).distance;
/*      */         }
/*  609 */         else if ((intType instanceof ReferenceType)) {
/*  610 */           currMethodDistance++;
/*      */         }
/*  612 */         else if ((intType instanceof ObjectType)) {
/*  613 */           ObjectType object = (ObjectType)intType;
/*  614 */           if (extType.getName().equals(object.getJavaClassName())) {
/*  615 */             currMethodDistance += 0;
/*  616 */           } else if (extType.isAssignableFrom(object.getJavaClass())) {
/*  617 */             currMethodDistance++;
/*      */           } else {
/*  619 */             currMethodDistance = 2147483647;
/*  620 */             break;
/*      */           }
/*      */         }
/*      */         else {
/*  624 */           currMethodDistance = 2147483647;
/*  625 */           break;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  630 */       if (j == nArgs)
/*      */       {
/*  632 */         extType = method.getReturnType();
/*      */ 
/*  634 */         this._type = ((Type)_java2Internal.get(extType));
/*  635 */         if (this._type == null) {
/*  636 */           this._type = Type.newObjectType(extType);
/*      */         }
/*      */ 
/*  640 */         if ((this._type != null) && (currMethodDistance < bestMethodDistance)) {
/*  641 */           this._chosenMethod = method;
/*  642 */           bestMethodDistance = currMethodDistance;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  649 */     if ((this._chosenMethod != null) && (this._thisArgument == null) && (!Modifier.isStatic(this._chosenMethod.getModifiers())))
/*      */     {
/*  651 */       throw new TypeCheckError("NO_JAVA_FUNCT_THIS_REF", getMethodSignature(argsType));
/*      */     }
/*      */ 
/*  654 */     if (this._type != null) {
/*  655 */       if (this._type == Type.NodeSet) {
/*  656 */         getXSLTC().setMultiDocument(true);
/*      */       }
/*  658 */       return this._type;
/*      */     }
/*      */ 
/*  661 */     throw new TypeCheckError("ARGUMENT_CONVERSION_ERR", getMethodSignature(argsType));
/*      */   }
/*      */ 
/*      */   public Vector typeCheckArgs(SymbolTable stable)
/*      */     throws TypeCheckError
/*      */   {
/*  668 */     Vector result = new Vector();
/*  669 */     Enumeration e = this._arguments.elements();
/*  670 */     while (e.hasMoreElements()) {
/*  671 */       Expression exp = (Expression)e.nextElement();
/*  672 */       result.addElement(exp.typeCheck(stable));
/*      */     }
/*  674 */     return result;
/*      */   }
/*      */ 
/*      */   protected final Expression argument(int i) {
/*  678 */     return (Expression)this._arguments.elementAt(i);
/*      */   }
/*      */ 
/*      */   protected final Expression argument() {
/*  682 */     return argument(0);
/*      */   }
/*      */ 
/*      */   protected final int argumentCount() {
/*  686 */     return this._arguments.size();
/*      */   }
/*      */ 
/*      */   protected final void setArgument(int i, Expression exp) {
/*  690 */     this._arguments.setElementAt(exp, i);
/*      */   }
/*      */ 
/*      */   public void translateDesynthesized(ClassGenerator classGen, MethodGenerator methodGen)
/*      */   {
/*  701 */     Type type = Type.Boolean;
/*  702 */     if (this._chosenMethodType != null) {
/*  703 */       type = this._chosenMethodType.resultType();
/*      */     }
/*  705 */     InstructionList il = methodGen.getInstructionList();
/*  706 */     translate(classGen, methodGen);
/*      */ 
/*  708 */     if (((type instanceof BooleanType)) || ((type instanceof IntType)))
/*  709 */       this._falseList.add(il.append(new IFEQ(null)));
/*      */   }
/*      */ 
/*      */   public void translate(ClassGenerator classGen, MethodGenerator methodGen)
/*      */   {
/*  720 */     int n = argumentCount();
/*  721 */     ConstantPoolGen cpg = classGen.getConstantPool();
/*  722 */     InstructionList il = methodGen.getInstructionList();
/*  723 */     boolean isSecureProcessing = classGen.getParser().getXSLTC().isSecureProcessing();
/*  724 */     boolean isExtensionFunctionEnabled = classGen.getParser().getXSLTC().getFeature(FeatureManager.Feature.ORACLE_ENABLE_EXTENSION_FUNCTION);
/*      */ 
/*  729 */     if ((isStandard()) || (isExtension())) {
/*  730 */       for (int i = 0; i < n; i++) {
/*  731 */         Expression exp = argument(i);
/*  732 */         exp.translate(classGen, methodGen);
/*  733 */         exp.startIterator(classGen, methodGen);
/*      */       }
/*      */ 
/*  737 */       String name = this._fname.toString().replace('-', '_') + "F";
/*  738 */       String args = "";
/*      */ 
/*  741 */       if (name.equals("sumF")) {
/*  742 */         args = "Lcom/sun/org/apache/xalan/internal/xsltc/DOM;";
/*  743 */         il.append(methodGen.loadDOM());
/*      */       }
/*  745 */       else if ((name.equals("normalize_spaceF")) && 
/*  746 */         (this._chosenMethodType.toSignature(args).equals("()Ljava/lang/String;")))
/*      */       {
/*  748 */         args = "ILcom/sun/org/apache/xalan/internal/xsltc/DOM;";
/*  749 */         il.append(methodGen.loadContextNode());
/*  750 */         il.append(methodGen.loadDOM());
/*      */       }
/*      */ 
/*  755 */       int index = cpg.addMethodref("com.sun.org.apache.xalan.internal.xsltc.runtime.BasisLibrary", name, this._chosenMethodType.toSignature(args));
/*      */ 
/*  757 */       il.append(new INVOKESTATIC(index));
/*      */     }
/*  761 */     else if (this.unresolvedExternal) {
/*  762 */       int index = cpg.addMethodref("com.sun.org.apache.xalan.internal.xsltc.runtime.BasisLibrary", "unresolved_externalF", "(Ljava/lang/String;)V");
/*      */ 
/*  765 */       il.append(new PUSH(cpg, this._fname.toString()));
/*  766 */       il.append(new INVOKESTATIC(index));
/*      */     }
/*  768 */     else if (this._isExtConstructor) {
/*  769 */       if ((isSecureProcessing) && (!isExtensionFunctionEnabled)) {
/*  770 */         translateUnallowedExtension(cpg, il);
/*      */       }
/*  772 */       String clazz = this._chosenConstructor.getDeclaringClass().getName();
/*      */ 
/*  774 */       Class[] paramTypes = this._chosenConstructor.getParameterTypes();
/*  775 */       LocalVariableGen[] paramTemp = new LocalVariableGen[n];
/*      */ 
/*  786 */       for (int i = 0; i < n; i++) {
/*  787 */         Expression exp = argument(i);
/*  788 */         Type expType = exp.getType();
/*  789 */         exp.translate(classGen, methodGen);
/*      */ 
/*  791 */         exp.startIterator(classGen, methodGen);
/*  792 */         expType.translateTo(classGen, methodGen, paramTypes[i]);
/*  793 */         paramTemp[i] = methodGen.addLocalVariable("function_call_tmp" + i, expType.toJCType(), null, null);
/*      */ 
/*  797 */         paramTemp[i].setStart(il.append(expType.STORE(paramTemp[i].getIndex())));
/*      */       }
/*      */ 
/*  802 */       il.append(new NEW(cpg.addClass(this._className)));
/*  803 */       il.append(InstructionConstants.DUP);
/*      */ 
/*  805 */       for (int i = 0; i < n; i++) {
/*  806 */         Expression arg = argument(i);
/*  807 */         paramTemp[i].setEnd(il.append(arg.getType().LOAD(paramTemp[i].getIndex())));
/*      */       }
/*      */ 
/*  811 */       StringBuffer buffer = new StringBuffer();
/*  812 */       buffer.append('(');
/*  813 */       for (int i = 0; i < paramTypes.length; i++) {
/*  814 */         buffer.append(getSignature(paramTypes[i]));
/*      */       }
/*  816 */       buffer.append(')');
/*  817 */       buffer.append("V");
/*      */ 
/*  819 */       int index = cpg.addMethodref(clazz, "<init>", buffer.toString());
/*      */ 
/*  822 */       il.append(new INVOKESPECIAL(index));
/*      */ 
/*  825 */       Type.Object.translateFrom(classGen, methodGen, this._chosenConstructor.getDeclaringClass());
/*      */     }
/*      */     else
/*      */     {
/*  831 */       if ((isSecureProcessing) && (!isExtensionFunctionEnabled)) {
/*  832 */         translateUnallowedExtension(cpg, il);
/*      */       }
/*  834 */       String clazz = this._chosenMethod.getDeclaringClass().getName();
/*  835 */       Class[] paramTypes = this._chosenMethod.getParameterTypes();
/*      */ 
/*  838 */       if (this._thisArgument != null) {
/*  839 */         this._thisArgument.translate(classGen, methodGen);
/*      */       }
/*      */ 
/*  842 */       for (int i = 0; i < n; i++) {
/*  843 */         Expression exp = argument(i);
/*  844 */         exp.translate(classGen, methodGen);
/*      */ 
/*  846 */         exp.startIterator(classGen, methodGen);
/*  847 */         exp.getType().translateTo(classGen, methodGen, paramTypes[i]);
/*      */       }
/*      */ 
/*  850 */       StringBuffer buffer = new StringBuffer();
/*  851 */       buffer.append('(');
/*  852 */       for (int i = 0; i < paramTypes.length; i++) {
/*  853 */         buffer.append(getSignature(paramTypes[i]));
/*      */       }
/*  855 */       buffer.append(')');
/*  856 */       buffer.append(getSignature(this._chosenMethod.getReturnType()));
/*      */ 
/*  858 */       if ((this._thisArgument != null) && (this._clazz.isInterface())) {
/*  859 */         int index = cpg.addInterfaceMethodref(clazz, this._fname.getLocalPart(), buffer.toString());
/*      */ 
/*  862 */         il.append(new INVOKEINTERFACE(index, n + 1));
/*      */       }
/*      */       else {
/*  865 */         int index = cpg.addMethodref(clazz, this._fname.getLocalPart(), buffer.toString());
/*      */ 
/*  868 */         il.append(this._thisArgument != null ? new INVOKEVIRTUAL(index) : new INVOKESTATIC(index));
/*      */       }
/*      */ 
/*  873 */       this._type.translateFrom(classGen, methodGen, this._chosenMethod.getReturnType());
/*      */     }
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/*  880 */     return "funcall(" + this._fname + ", " + this._arguments + ')';
/*      */   }
/*      */ 
/*      */   public boolean isStandard() {
/*  884 */     String namespace = this._fname.getNamespace();
/*  885 */     return (namespace == null) || (namespace.equals(""));
/*      */   }
/*      */ 
/*      */   public boolean isExtension() {
/*  889 */     String namespace = this._fname.getNamespace();
/*  890 */     return (namespace != null) && (namespace.equals("http://xml.apache.org/xalan/xsltc"));
/*      */   }
/*      */ 
/*      */   private Vector findMethods()
/*      */   {
/*  900 */     Vector result = null;
/*  901 */     String namespace = this._fname.getNamespace();
/*      */ 
/*  903 */     if ((this._className != null) && (this._className.length() > 0)) {
/*  904 */       int nArgs = this._arguments.size();
/*      */       try {
/*  906 */         if (this._clazz == null) {
/*  907 */           boolean isSecureProcessing = getXSLTC().isSecureProcessing();
/*  908 */           boolean isExtensionFunctionEnabled = getXSLTC().getFeature(FeatureManager.Feature.ORACLE_ENABLE_EXTENSION_FUNCTION);
/*      */ 
/*  912 */           if ((namespace != null) && (isSecureProcessing) && (isExtensionFunctionEnabled) && ((namespace.equals("http://xml.apache.org/xalan/java")) || (namespace.equals("http://xml.apache.org/xalan/xsltc/java")) || (namespace.equals("http://xml.apache.org/xslt/java")) || (namespace.startsWith("xalan://"))))
/*      */           {
/*  918 */             this._clazz = getXSLTC().loadExternalFunction(this._className);
/*      */           }
/*  920 */           else this._clazz = ObjectFactory.findProviderClass(this._className, true);
/*      */ 
/*  923 */           if (this._clazz == null) {
/*  924 */             ErrorMsg msg = new ErrorMsg("CLASS_NOT_FOUND_ERR", this._className);
/*      */ 
/*  926 */             getParser().reportError(3, msg);
/*      */           }
/*      */         }
/*      */ 
/*  930 */         String methodName = this._fname.getLocalPart();
/*  931 */         Method[] methods = this._clazz.getMethods();
/*      */ 
/*  933 */         for (int i = 0; i < methods.length; i++) {
/*  934 */           int mods = methods[i].getModifiers();
/*      */ 
/*  936 */           if ((Modifier.isPublic(mods)) && (methods[i].getName().equals(methodName)) && (methods[i].getParameterTypes().length == nArgs))
/*      */           {
/*  940 */             if (result == null) {
/*  941 */               result = new Vector();
/*      */             }
/*  943 */             result.addElement(methods[i]);
/*      */           }
/*      */         }
/*      */       }
/*      */       catch (ClassNotFoundException e) {
/*  948 */         ErrorMsg msg = new ErrorMsg("CLASS_NOT_FOUND_ERR", this._className);
/*  949 */         getParser().reportError(3, msg);
/*      */       }
/*      */     }
/*  952 */     return result;
/*      */   }
/*      */ 
/*      */   private Vector findConstructors()
/*      */   {
/*  961 */     Vector result = null;
/*  962 */     String namespace = this._fname.getNamespace();
/*      */ 
/*  964 */     int nArgs = this._arguments.size();
/*      */     try {
/*  966 */       if (this._clazz == null) {
/*  967 */         this._clazz = ObjectFactory.findProviderClass(this._className, true);
/*      */ 
/*  969 */         if (this._clazz == null) {
/*  970 */           ErrorMsg msg = new ErrorMsg("CLASS_NOT_FOUND_ERR", this._className);
/*  971 */           getParser().reportError(3, msg);
/*      */         }
/*      */       }
/*      */ 
/*  975 */       Constructor[] constructors = this._clazz.getConstructors();
/*      */ 
/*  977 */       for (int i = 0; i < constructors.length; i++) {
/*  978 */         int mods = constructors[i].getModifiers();
/*      */ 
/*  980 */         if ((Modifier.isPublic(mods)) && (constructors[i].getParameterTypes().length == nArgs))
/*      */         {
/*  983 */           if (result == null) {
/*  984 */             result = new Vector();
/*      */           }
/*  986 */           result.addElement(constructors[i]);
/*      */         }
/*      */       }
/*      */     }
/*      */     catch (ClassNotFoundException e) {
/*  991 */       ErrorMsg msg = new ErrorMsg("CLASS_NOT_FOUND_ERR", this._className);
/*  992 */       getParser().reportError(3, msg);
/*      */     }
/*      */ 
/*  995 */     return result;
/*      */   }
/*      */ 
/*      */   static final String getSignature(Class clazz)
/*      */   {
/* 1003 */     if (clazz.isArray()) {
/* 1004 */       StringBuffer sb = new StringBuffer();
/* 1005 */       Class cl = clazz;
/* 1006 */       while (cl.isArray()) {
/* 1007 */         sb.append("[");
/* 1008 */         cl = cl.getComponentType();
/*      */       }
/* 1010 */       sb.append(getSignature(cl));
/* 1011 */       return sb.toString();
/*      */     }
/* 1013 */     if (clazz.isPrimitive()) {
/* 1014 */       if (clazz == Integer.TYPE) {
/* 1015 */         return "I";
/*      */       }
/* 1017 */       if (clazz == Byte.TYPE) {
/* 1018 */         return "B";
/*      */       }
/* 1020 */       if (clazz == Long.TYPE) {
/* 1021 */         return "J";
/*      */       }
/* 1023 */       if (clazz == Float.TYPE) {
/* 1024 */         return "F";
/*      */       }
/* 1026 */       if (clazz == Double.TYPE) {
/* 1027 */         return "D";
/*      */       }
/* 1029 */       if (clazz == Short.TYPE) {
/* 1030 */         return "S";
/*      */       }
/* 1032 */       if (clazz == Character.TYPE) {
/* 1033 */         return "C";
/*      */       }
/* 1035 */       if (clazz == Boolean.TYPE) {
/* 1036 */         return "Z";
/*      */       }
/* 1038 */       if (clazz == Void.TYPE) {
/* 1039 */         return "V";
/*      */       }
/*      */ 
/* 1042 */       String name = clazz.toString();
/* 1043 */       ErrorMsg err = new ErrorMsg("UNKNOWN_SIG_TYPE_ERR", name);
/* 1044 */       throw new Error(err.toString());
/*      */     }
/*      */ 
/* 1048 */     return "L" + clazz.getName().replace('.', '/') + ';';
/*      */   }
/*      */ 
/*      */   static final String getSignature(Method meth)
/*      */   {
/* 1056 */     StringBuffer sb = new StringBuffer();
/* 1057 */     sb.append('(');
/* 1058 */     Class[] params = meth.getParameterTypes();
/* 1059 */     for (int j = 0; j < params.length; j++) {
/* 1060 */       sb.append(getSignature(params[j]));
/*      */     }
/* 1062 */     return ')' + getSignature(meth.getReturnType());
/*      */   }
/*      */ 
/*      */   static final String getSignature(Constructor cons)
/*      */   {
/* 1070 */     StringBuffer sb = new StringBuffer();
/* 1071 */     sb.append('(');
/* 1072 */     Class[] params = cons.getParameterTypes();
/* 1073 */     for (int j = 0; j < params.length; j++) {
/* 1074 */       sb.append(getSignature(params[j]));
/*      */     }
/* 1076 */     return ")V";
/*      */   }
/*      */ 
/*      */   private String getMethodSignature(Vector argsType)
/*      */   {
/* 1083 */     StringBuffer buf = new StringBuffer(this._className);
/* 1084 */     buf.append('.').append(this._fname.getLocalPart()).append('(');
/*      */ 
/* 1086 */     int nArgs = argsType.size();
/* 1087 */     for (int i = 0; i < nArgs; i++) {
/* 1088 */       Type intType = (Type)argsType.elementAt(i);
/* 1089 */       buf.append(intType.toString());
/* 1090 */       if (i < nArgs - 1) buf.append(", ");
/*      */     }
/*      */ 
/* 1093 */     buf.append(')');
/* 1094 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   protected static String replaceDash(String name)
/*      */   {
/* 1104 */     char dash = '-';
/* 1105 */     StringBuilder buff = new StringBuilder("");
/* 1106 */     for (int i = 0; i < name.length(); i++) {
/* 1107 */       if ((i > 0) && (name.charAt(i - 1) == dash))
/* 1108 */         buff.append(Character.toUpperCase(name.charAt(i)));
/* 1109 */       else if (name.charAt(i) != dash)
/* 1110 */         buff.append(name.charAt(i));
/*      */     }
/* 1112 */     return buff.toString();
/*      */   }
/*      */ 
/*      */   private void translateUnallowedExtension(ConstantPoolGen cpg, InstructionList il)
/*      */   {
/* 1121 */     int index = cpg.addMethodref("com.sun.org.apache.xalan.internal.xsltc.runtime.BasisLibrary", "unallowed_extension_functionF", "(Ljava/lang/String;)V");
/*      */ 
/* 1124 */     il.append(new PUSH(cpg, this._fname.toString()));
/* 1125 */     il.append(new INVOKESTATIC(index));
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*      */     try
/*      */     {
/*  184 */       Class nodeClass = Class.forName("org.w3c.dom.Node");
/*  185 */       Class nodeListClass = Class.forName("org.w3c.dom.NodeList");
/*      */ 
/*  190 */       _internal2Java.put(Type.Boolean, new JavaType(Boolean.TYPE, 0));
/*  191 */       _internal2Java.put(Type.Boolean, new JavaType(Boolean.class, 1));
/*  192 */       _internal2Java.put(Type.Boolean, new JavaType(Object.class, 2));
/*      */ 
/*  196 */       _internal2Java.put(Type.Real, new JavaType(Double.TYPE, 0));
/*  197 */       _internal2Java.put(Type.Real, new JavaType(Double.class, 1));
/*  198 */       _internal2Java.put(Type.Real, new JavaType(Float.TYPE, 2));
/*  199 */       _internal2Java.put(Type.Real, new JavaType(Long.TYPE, 3));
/*  200 */       _internal2Java.put(Type.Real, new JavaType(Integer.TYPE, 4));
/*  201 */       _internal2Java.put(Type.Real, new JavaType(Short.TYPE, 5));
/*  202 */       _internal2Java.put(Type.Real, new JavaType(Byte.TYPE, 6));
/*  203 */       _internal2Java.put(Type.Real, new JavaType(Character.TYPE, 7));
/*  204 */       _internal2Java.put(Type.Real, new JavaType(Object.class, 8));
/*      */ 
/*  207 */       _internal2Java.put(Type.Int, new JavaType(Double.TYPE, 0));
/*  208 */       _internal2Java.put(Type.Int, new JavaType(Double.class, 1));
/*  209 */       _internal2Java.put(Type.Int, new JavaType(Float.TYPE, 2));
/*  210 */       _internal2Java.put(Type.Int, new JavaType(Long.TYPE, 3));
/*  211 */       _internal2Java.put(Type.Int, new JavaType(Integer.TYPE, 4));
/*  212 */       _internal2Java.put(Type.Int, new JavaType(Short.TYPE, 5));
/*  213 */       _internal2Java.put(Type.Int, new JavaType(Byte.TYPE, 6));
/*  214 */       _internal2Java.put(Type.Int, new JavaType(Character.TYPE, 7));
/*  215 */       _internal2Java.put(Type.Int, new JavaType(Object.class, 8));
/*      */ 
/*  218 */       _internal2Java.put(Type.String, new JavaType(String.class, 0));
/*  219 */       _internal2Java.put(Type.String, new JavaType(Object.class, 1));
/*      */ 
/*  222 */       _internal2Java.put(Type.NodeSet, new JavaType(nodeListClass, 0));
/*  223 */       _internal2Java.put(Type.NodeSet, new JavaType(nodeClass, 1));
/*  224 */       _internal2Java.put(Type.NodeSet, new JavaType(Object.class, 2));
/*  225 */       _internal2Java.put(Type.NodeSet, new JavaType(String.class, 3));
/*      */ 
/*  228 */       _internal2Java.put(Type.Node, new JavaType(nodeListClass, 0));
/*  229 */       _internal2Java.put(Type.Node, new JavaType(nodeClass, 1));
/*  230 */       _internal2Java.put(Type.Node, new JavaType(Object.class, 2));
/*  231 */       _internal2Java.put(Type.Node, new JavaType(String.class, 3));
/*      */ 
/*  234 */       _internal2Java.put(Type.ResultTree, new JavaType(nodeListClass, 0));
/*  235 */       _internal2Java.put(Type.ResultTree, new JavaType(nodeClass, 1));
/*  236 */       _internal2Java.put(Type.ResultTree, new JavaType(Object.class, 2));
/*  237 */       _internal2Java.put(Type.ResultTree, new JavaType(String.class, 3));
/*      */ 
/*  239 */       _internal2Java.put(Type.Reference, new JavaType(Object.class, 0));
/*      */ 
/*  242 */       _java2Internal.put(Boolean.TYPE, Type.Boolean);
/*  243 */       _java2Internal.put(Void.TYPE, Type.Void);
/*  244 */       _java2Internal.put(Character.TYPE, Type.Real);
/*  245 */       _java2Internal.put(Byte.TYPE, Type.Real);
/*  246 */       _java2Internal.put(Short.TYPE, Type.Real);
/*  247 */       _java2Internal.put(Integer.TYPE, Type.Real);
/*  248 */       _java2Internal.put(Long.TYPE, Type.Real);
/*  249 */       _java2Internal.put(Float.TYPE, Type.Real);
/*  250 */       _java2Internal.put(Double.TYPE, Type.Real);
/*      */ 
/*  252 */       _java2Internal.put(String.class, Type.String);
/*      */ 
/*  254 */       _java2Internal.put(Object.class, Type.Reference);
/*      */ 
/*  257 */       _java2Internal.put(nodeListClass, Type.NodeSet);
/*  258 */       _java2Internal.put(nodeClass, Type.NodeSet);
/*      */ 
/*  261 */       _extensionNamespaceTable.put("http://xml.apache.org/xalan", "com.sun.org.apache.xalan.internal.lib.Extensions");
/*  262 */       _extensionNamespaceTable.put("http://exslt.org/common", "com.sun.org.apache.xalan.internal.lib.ExsltCommon");
/*  263 */       _extensionNamespaceTable.put("http://exslt.org/math", "com.sun.org.apache.xalan.internal.lib.ExsltMath");
/*  264 */       _extensionNamespaceTable.put("http://exslt.org/sets", "com.sun.org.apache.xalan.internal.lib.ExsltSets");
/*  265 */       _extensionNamespaceTable.put("http://exslt.org/dates-and-times", "com.sun.org.apache.xalan.internal.lib.ExsltDatetime");
/*  266 */       _extensionNamespaceTable.put("http://exslt.org/strings", "com.sun.org.apache.xalan.internal.lib.ExsltStrings");
/*      */ 
/*  269 */       _extensionFunctionTable.put("http://exslt.org/common:nodeSet", "nodeset");
/*  270 */       _extensionFunctionTable.put("http://exslt.org/common:objectType", "objectType");
/*  271 */       _extensionFunctionTable.put("http://xml.apache.org/xalan:nodeset", "nodeset");
/*      */     }
/*      */     catch (ClassNotFoundException e) {
/*  274 */       System.err.println(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   static class JavaType
/*      */   {
/*      */     public Class type;
/*      */     public int distance;
/*      */ 
/*      */     public JavaType(Class type, int distance)
/*      */     {
/*  161 */       this.type = type;
/*  162 */       this.distance = distance;
/*      */     }
/*      */ 
/*      */     public int hashCode()
/*      */     {
/*  167 */       return Objects.hashCode(this.type);
/*      */     }
/*      */ 
/*      */     public boolean equals(Object query)
/*      */     {
/*  172 */       return (query != null) && (query.equals(this.type));
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.FunctionCall
 * JD-Core Version:    0.6.2
 */