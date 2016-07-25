/*      */ package com.sun.org.apache.xpath.internal.compiler;
/*      */ 
/*      */ import com.sun.org.apache.xalan.internal.res.XSLMessages;
/*      */ import com.sun.org.apache.xml.internal.dtm.DTMIterator;
/*      */ import com.sun.org.apache.xml.internal.utils.ObjectVector;
/*      */ import com.sun.org.apache.xml.internal.utils.PrefixResolver;
/*      */ import com.sun.org.apache.xml.internal.utils.QName;
/*      */ import com.sun.org.apache.xml.internal.utils.SAXSourceLocator;
/*      */ import com.sun.org.apache.xpath.internal.Expression;
/*      */ import com.sun.org.apache.xpath.internal.axes.UnionPathIterator;
/*      */ import com.sun.org.apache.xpath.internal.axes.WalkerFactory;
/*      */ import com.sun.org.apache.xpath.internal.functions.FuncExtFunction;
/*      */ import com.sun.org.apache.xpath.internal.functions.FuncExtFunctionAvailable;
/*      */ import com.sun.org.apache.xpath.internal.functions.Function;
/*      */ import com.sun.org.apache.xpath.internal.functions.WrongNumberArgsException;
/*      */ import com.sun.org.apache.xpath.internal.objects.XNumber;
/*      */ import com.sun.org.apache.xpath.internal.objects.XString;
/*      */ import com.sun.org.apache.xpath.internal.operations.And;
/*      */ import com.sun.org.apache.xpath.internal.operations.Bool;
/*      */ import com.sun.org.apache.xpath.internal.operations.Div;
/*      */ import com.sun.org.apache.xpath.internal.operations.Equals;
/*      */ import com.sun.org.apache.xpath.internal.operations.Gt;
/*      */ import com.sun.org.apache.xpath.internal.operations.Gte;
/*      */ import com.sun.org.apache.xpath.internal.operations.Lt;
/*      */ import com.sun.org.apache.xpath.internal.operations.Lte;
/*      */ import com.sun.org.apache.xpath.internal.operations.Minus;
/*      */ import com.sun.org.apache.xpath.internal.operations.Mod;
/*      */ import com.sun.org.apache.xpath.internal.operations.Mult;
/*      */ import com.sun.org.apache.xpath.internal.operations.Neg;
/*      */ import com.sun.org.apache.xpath.internal.operations.NotEquals;
/*      */ import com.sun.org.apache.xpath.internal.operations.Number;
/*      */ import com.sun.org.apache.xpath.internal.operations.Operation;
/*      */ import com.sun.org.apache.xpath.internal.operations.Or;
/*      */ import com.sun.org.apache.xpath.internal.operations.Plus;
/*      */ import com.sun.org.apache.xpath.internal.operations.UnaryOperation;
/*      */ import com.sun.org.apache.xpath.internal.operations.Variable;
/*      */ import com.sun.org.apache.xpath.internal.patterns.FunctionPattern;
/*      */ import com.sun.org.apache.xpath.internal.patterns.StepPattern;
/*      */ import com.sun.org.apache.xpath.internal.patterns.UnionPattern;
/*      */ import java.io.PrintStream;
/*      */ import javax.xml.transform.ErrorListener;
/*      */ import javax.xml.transform.SourceLocator;
/*      */ import javax.xml.transform.TransformerException;
/*      */ 
/*      */ public class Compiler extends OpMap
/*      */ {
/*  614 */   private int locPathDepth = -1;
/*      */   private static final boolean DEBUG = false;
/* 1080 */   private static long s_nextMethodId = 0L;
/*      */ 
/* 1237 */   private PrefixResolver m_currentPrefixResolver = null;
/*      */   ErrorListener m_errorHandler;
/*      */   SourceLocator m_locator;
/*      */   private FunctionTable m_functionTable;
/*      */ 
/*      */   public Compiler(ErrorListener errorHandler, SourceLocator locator, FunctionTable fTable)
/*      */   {
/*   93 */     this.m_errorHandler = errorHandler;
/*   94 */     this.m_locator = locator;
/*   95 */     this.m_functionTable = fTable;
/*      */   }
/*      */ 
/*      */   public Compiler()
/*      */   {
/*  104 */     this.m_errorHandler = null;
/*  105 */     this.m_locator = null;
/*      */   }
/*      */ 
/*      */   public Expression compile(int opPos)
/*      */     throws TransformerException
/*      */   {
/*  119 */     int op = getOp(opPos);
/*      */ 
/*  121 */     Expression expr = null;
/*      */ 
/*  123 */     switch (op)
/*      */     {
/*      */     case 1:
/*  126 */       expr = compile(opPos + 2); break;
/*      */     case 2:
/*  128 */       expr = or(opPos); break;
/*      */     case 3:
/*  130 */       expr = and(opPos); break;
/*      */     case 4:
/*  132 */       expr = notequals(opPos); break;
/*      */     case 5:
/*  134 */       expr = equals(opPos); break;
/*      */     case 6:
/*  136 */       expr = lte(opPos); break;
/*      */     case 7:
/*  138 */       expr = lt(opPos); break;
/*      */     case 8:
/*  140 */       expr = gte(opPos); break;
/*      */     case 9:
/*  142 */       expr = gt(opPos); break;
/*      */     case 10:
/*  144 */       expr = plus(opPos); break;
/*      */     case 11:
/*  146 */       expr = minus(opPos); break;
/*      */     case 12:
/*  148 */       expr = mult(opPos); break;
/*      */     case 13:
/*  150 */       expr = div(opPos); break;
/*      */     case 14:
/*  152 */       expr = mod(opPos); break;
/*      */     case 16:
/*  156 */       expr = neg(opPos); break;
/*      */     case 17:
/*  158 */       expr = string(opPos); break;
/*      */     case 18:
/*  160 */       expr = bool(opPos); break;
/*      */     case 19:
/*  162 */       expr = number(opPos); break;
/*      */     case 20:
/*  164 */       expr = union(opPos); break;
/*      */     case 21:
/*  166 */       expr = literal(opPos); break;
/*      */     case 22:
/*  168 */       expr = variable(opPos); break;
/*      */     case 23:
/*  170 */       expr = group(opPos); break;
/*      */     case 27:
/*  172 */       expr = numberlit(opPos); break;
/*      */     case 26:
/*  174 */       expr = arg(opPos); break;
/*      */     case 24:
/*  176 */       expr = compileExtension(opPos); break;
/*      */     case 25:
/*  178 */       expr = compileFunction(opPos); break;
/*      */     case 28:
/*  180 */       expr = locationPath(opPos); break;
/*      */     case 29:
/*  182 */       expr = null; break;
/*      */     case 30:
/*  184 */       expr = matchPattern(opPos + 2); break;
/*      */     case 31:
/*  186 */       expr = locationPathPattern(opPos); break;
/*      */     case 15:
/*  188 */       error("ER_UNKNOWN_OPCODE", new Object[] { "quo" });
/*      */ 
/*  190 */       break;
/*      */     default:
/*  192 */       error("ER_UNKNOWN_OPCODE", new Object[] { Integer.toString(getOp(opPos)) });
/*      */     }
/*      */ 
/*  198 */     return expr;
/*      */   }
/*      */ 
/*      */   private Expression compileOperation(Operation operation, int opPos)
/*      */     throws TransformerException
/*      */   {
/*  215 */     int leftPos = getFirstChildPos(opPos);
/*  216 */     int rightPos = getNextOpPos(leftPos);
/*      */ 
/*  218 */     operation.setLeftRight(compile(leftPos), compile(rightPos));
/*      */ 
/*  220 */     return operation;
/*      */   }
/*      */ 
/*      */   private Expression compileUnary(UnaryOperation unary, int opPos)
/*      */     throws TransformerException
/*      */   {
/*  237 */     int rightPos = getFirstChildPos(opPos);
/*      */ 
/*  239 */     unary.setRight(compile(rightPos));
/*      */ 
/*  241 */     return unary;
/*      */   }
/*      */ 
/*      */   protected Expression or(int opPos)
/*      */     throws TransformerException
/*      */   {
/*  255 */     return compileOperation(new Or(), opPos);
/*      */   }
/*      */ 
/*      */   protected Expression and(int opPos)
/*      */     throws TransformerException
/*      */   {
/*  269 */     return compileOperation(new And(), opPos);
/*      */   }
/*      */ 
/*      */   protected Expression notequals(int opPos)
/*      */     throws TransformerException
/*      */   {
/*  283 */     return compileOperation(new NotEquals(), opPos);
/*      */   }
/*      */ 
/*      */   protected Expression equals(int opPos)
/*      */     throws TransformerException
/*      */   {
/*  297 */     return compileOperation(new Equals(), opPos);
/*      */   }
/*      */ 
/*      */   protected Expression lte(int opPos)
/*      */     throws TransformerException
/*      */   {
/*  311 */     return compileOperation(new Lte(), opPos);
/*      */   }
/*      */ 
/*      */   protected Expression lt(int opPos)
/*      */     throws TransformerException
/*      */   {
/*  325 */     return compileOperation(new Lt(), opPos);
/*      */   }
/*      */ 
/*      */   protected Expression gte(int opPos)
/*      */     throws TransformerException
/*      */   {
/*  339 */     return compileOperation(new Gte(), opPos);
/*      */   }
/*      */ 
/*      */   protected Expression gt(int opPos)
/*      */     throws TransformerException
/*      */   {
/*  353 */     return compileOperation(new Gt(), opPos);
/*      */   }
/*      */ 
/*      */   protected Expression plus(int opPos)
/*      */     throws TransformerException
/*      */   {
/*  367 */     return compileOperation(new Plus(), opPos);
/*      */   }
/*      */ 
/*      */   protected Expression minus(int opPos)
/*      */     throws TransformerException
/*      */   {
/*  381 */     return compileOperation(new Minus(), opPos);
/*      */   }
/*      */ 
/*      */   protected Expression mult(int opPos)
/*      */     throws TransformerException
/*      */   {
/*  395 */     return compileOperation(new Mult(), opPos);
/*      */   }
/*      */ 
/*      */   protected Expression div(int opPos)
/*      */     throws TransformerException
/*      */   {
/*  409 */     return compileOperation(new Div(), opPos);
/*      */   }
/*      */ 
/*      */   protected Expression mod(int opPos)
/*      */     throws TransformerException
/*      */   {
/*  423 */     return compileOperation(new Mod(), opPos);
/*      */   }
/*      */ 
/*      */   protected Expression neg(int opPos)
/*      */     throws TransformerException
/*      */   {
/*  451 */     return compileUnary(new Neg(), opPos);
/*      */   }
/*      */ 
/*      */   protected Expression string(int opPos)
/*      */     throws TransformerException
/*      */   {
/*  465 */     return compileUnary(new com.sun.org.apache.xpath.internal.operations.String(), opPos);
/*      */   }
/*      */ 
/*      */   protected Expression bool(int opPos)
/*      */     throws TransformerException
/*      */   {
/*  479 */     return compileUnary(new Bool(), opPos);
/*      */   }
/*      */ 
/*      */   protected Expression number(int opPos)
/*      */     throws TransformerException
/*      */   {
/*  493 */     return compileUnary(new Number(), opPos);
/*      */   }
/*      */ 
/*      */   protected Expression literal(int opPos)
/*      */   {
/*  508 */     opPos = getFirstChildPos(opPos);
/*      */ 
/*  510 */     return (XString)getTokenQueue().elementAt(getOp(opPos));
/*      */   }
/*      */ 
/*      */   protected Expression numberlit(int opPos)
/*      */   {
/*  525 */     opPos = getFirstChildPos(opPos);
/*      */ 
/*  527 */     return (XNumber)getTokenQueue().elementAt(getOp(opPos));
/*      */   }
/*      */ 
/*      */   protected Expression variable(int opPos)
/*      */     throws TransformerException
/*      */   {
/*  542 */     Variable var = new Variable();
/*      */ 
/*  544 */     opPos = getFirstChildPos(opPos);
/*      */ 
/*  546 */     int nsPos = getOp(opPos);
/*  547 */     java.lang.String namespace = -2 == nsPos ? null : (java.lang.String)getTokenQueue().elementAt(nsPos);
/*      */ 
/*  550 */     java.lang.String localname = (java.lang.String)getTokenQueue().elementAt(getOp(opPos + 1));
/*      */ 
/*  552 */     QName qname = new QName(namespace, localname);
/*      */ 
/*  554 */     var.setQName(qname);
/*      */ 
/*  556 */     return var;
/*      */   }
/*      */ 
/*      */   protected Expression group(int opPos)
/*      */     throws TransformerException
/*      */   {
/*  572 */     return compile(opPos + 2);
/*      */   }
/*      */ 
/*      */   protected Expression arg(int opPos)
/*      */     throws TransformerException
/*      */   {
/*  588 */     return compile(opPos + 2);
/*      */   }
/*      */ 
/*      */   protected Expression union(int opPos)
/*      */     throws TransformerException
/*      */   {
/*  603 */     this.locPathDepth += 1;
/*      */     try
/*      */     {
/*  606 */       return UnionPathIterator.createUnionIterator(this, opPos);
/*      */     }
/*      */     finally
/*      */     {
/*  610 */       this.locPathDepth -= 1;
/*      */     }
/*      */   }
/*      */ 
/*      */   public int getLocationPathDepth()
/*      */   {
/*  622 */     return this.locPathDepth;
/*      */   }
/*      */ 
/*      */   FunctionTable getFunctionTable()
/*      */   {
/*  630 */     return this.m_functionTable;
/*      */   }
/*      */ 
/*      */   public Expression locationPath(int opPos)
/*      */     throws TransformerException
/*      */   {
/*  645 */     this.locPathDepth += 1;
/*      */     try
/*      */     {
/*  648 */       DTMIterator iter = WalkerFactory.newDTMIterator(this, opPos, this.locPathDepth == 0);
/*  649 */       return (Expression)iter;
/*      */     }
/*      */     finally
/*      */     {
/*  653 */       this.locPathDepth -= 1;
/*      */     }
/*      */   }
/*      */ 
/*      */   public Expression predicate(int opPos)
/*      */     throws TransformerException
/*      */   {
/*  668 */     return compile(opPos + 2);
/*      */   }
/*      */ 
/*      */   protected Expression matchPattern(int opPos)
/*      */     throws TransformerException
/*      */   {
/*  682 */     this.locPathDepth += 1;
/*      */     try
/*      */     {
/*  686 */       int nextOpPos = opPos;
/*      */ 
/*  689 */       for (int i = 0; getOp(nextOpPos) == 31; i++)
/*      */       {
/*  691 */         nextOpPos = getNextOpPos(nextOpPos);
/*      */       }
/*      */ 
/*  694 */       if (i == 1) {
/*  695 */         return compile(opPos);
/*      */       }
/*  697 */       UnionPattern up = new UnionPattern();
/*  698 */       StepPattern[] patterns = new StepPattern[i];
/*      */ 
/*  700 */       for (i = 0; getOp(opPos) == 31; i++)
/*      */       {
/*  702 */         nextOpPos = getNextOpPos(opPos);
/*  703 */         patterns[i] = ((StepPattern)compile(opPos));
/*  704 */         opPos = nextOpPos;
/*      */       }
/*      */ 
/*  707 */       up.setPatterns(patterns);
/*      */ 
/*  709 */       return up;
/*      */     }
/*      */     finally
/*      */     {
/*  713 */       this.locPathDepth -= 1;
/*      */     }
/*      */   }
/*      */ 
/*      */   public Expression locationPathPattern(int opPos)
/*      */     throws TransformerException
/*      */   {
/*  730 */     opPos = getFirstChildPos(opPos);
/*      */ 
/*  732 */     return stepPattern(opPos, 0, null);
/*      */   }
/*      */ 
/*      */   public int getWhatToShow(int opPos)
/*      */   {
/*  747 */     int axesType = getOp(opPos);
/*  748 */     int testType = getOp(opPos + 3);
/*      */ 
/*  751 */     switch (testType)
/*      */     {
/*      */     case 1030:
/*  754 */       return 128;
/*      */     case 1031:
/*  757 */       return 12;
/*      */     case 1032:
/*  759 */       return 64;
/*      */     case 1033:
/*  762 */       switch (axesType)
/*      */       {
/*      */       case 49:
/*  765 */         return 4096;
/*      */       case 39:
/*      */       case 51:
/*  768 */         return 2;
/*      */       case 38:
/*      */       case 42:
/*      */       case 48:
/*  772 */         return -1;
/*      */       case 40:
/*      */       case 41:
/*      */       case 43:
/*      */       case 44:
/*      */       case 45:
/*      */       case 46:
/*      */       case 47:
/*  774 */       case 50: } if (getOp(0) == 30) {
/*  775 */         return -1283;
/*      */       }
/*      */ 
/*  779 */       return -3;
/*      */     case 35:
/*  782 */       return 1280;
/*      */     case 1034:
/*  784 */       return 65536;
/*      */     case 34:
/*  786 */       switch (axesType)
/*      */       {
/*      */       case 49:
/*  789 */         return 4096;
/*      */       case 39:
/*      */       case 51:
/*  792 */         return 2;
/*      */       case 52:
/*      */       case 53:
/*  797 */         return 1;
/*      */       case 40:
/*      */       case 41:
/*      */       case 42:
/*      */       case 43:
/*      */       case 44:
/*      */       case 45:
/*      */       case 46:
/*      */       case 47:
/*      */       case 48:
/*  801 */       case 50: } return 1;
/*      */     }
/*      */ 
/*  805 */     return -1;
/*      */   }
/*      */ 
/*      */   protected StepPattern stepPattern(int opPos, int stepCount, StepPattern ancestorPattern)
/*      */     throws TransformerException
/*      */   {
/*  828 */     int startOpPos = opPos;
/*  829 */     int stepType = getOp(opPos);
/*      */ 
/*  831 */     if (-1 == stepType)
/*      */     {
/*  833 */       return null;
/*      */     }
/*      */ 
/*  836 */     boolean addMagicSelf = true;
/*      */ 
/*  838 */     int endStep = getNextOpPos(opPos);
/*      */     int argLen;
/*      */     StepPattern pattern;
/*  846 */     switch (stepType)
/*      */     {
/*      */     case 25:
/*  851 */       addMagicSelf = false;
/*  852 */       argLen = getOp(opPos + 1);
/*  853 */       pattern = new FunctionPattern(compileFunction(opPos), 10, 3);
/*  854 */       break;
/*      */     case 50:
/*  858 */       addMagicSelf = false;
/*  859 */       argLen = getArgLengthOfStep(opPos);
/*  860 */       opPos = getFirstChildPosOfStep(opPos);
/*  861 */       pattern = new StepPattern(1280, 10, 3);
/*      */ 
/*  864 */       break;
/*      */     case 51:
/*  868 */       argLen = getArgLengthOfStep(opPos);
/*  869 */       opPos = getFirstChildPosOfStep(opPos);
/*  870 */       pattern = new StepPattern(2, getStepNS(startOpPos), getStepLocalName(startOpPos), 10, 2);
/*      */ 
/*  874 */       break;
/*      */     case 52:
/*  878 */       argLen = getArgLengthOfStep(opPos);
/*  879 */       opPos = getFirstChildPosOfStep(opPos);
/*  880 */       int what = getWhatToShow(startOpPos);
/*      */ 
/*  882 */       if (1280 == what)
/*  883 */         addMagicSelf = false;
/*  884 */       pattern = new StepPattern(getWhatToShow(startOpPos), getStepNS(startOpPos), getStepLocalName(startOpPos), 0, 3);
/*      */ 
/*  888 */       break;
/*      */     case 53:
/*  892 */       argLen = getArgLengthOfStep(opPos);
/*  893 */       opPos = getFirstChildPosOfStep(opPos);
/*  894 */       pattern = new StepPattern(getWhatToShow(startOpPos), getStepNS(startOpPos), getStepLocalName(startOpPos), 10, 3);
/*      */ 
/*  898 */       break;
/*      */     default:
/*  900 */       error("ER_UNKNOWN_MATCH_OPERATION", null);
/*      */ 
/*  902 */       return null;
/*      */     }
/*      */ 
/*  905 */     pattern.setPredicates(getCompiledPredicates(opPos + argLen));
/*  906 */     if (null != ancestorPattern)
/*      */     {
/*  931 */       pattern.setRelativePathPattern(ancestorPattern);
/*      */     }
/*      */ 
/*  934 */     StepPattern relativePathPattern = stepPattern(endStep, stepCount + 1, pattern);
/*      */ 
/*  937 */     return null != relativePathPattern ? relativePathPattern : pattern;
/*      */   }
/*      */ 
/*      */   public Expression[] getCompiledPredicates(int opPos)
/*      */     throws TransformerException
/*      */   {
/*  953 */     int count = countPredicates(opPos);
/*      */ 
/*  955 */     if (count > 0)
/*      */     {
/*  957 */       Expression[] predicates = new Expression[count];
/*      */ 
/*  959 */       compilePredicates(opPos, predicates);
/*      */ 
/*  961 */       return predicates;
/*      */     }
/*      */ 
/*  964 */     return null;
/*      */   }
/*      */ 
/*      */   public int countPredicates(int opPos)
/*      */     throws TransformerException
/*      */   {
/*  979 */     int count = 0;
/*      */ 
/*  981 */     while (29 == getOp(opPos))
/*      */     {
/*  983 */       count++;
/*      */ 
/*  985 */       opPos = getNextOpPos(opPos);
/*      */     }
/*      */ 
/*  988 */     return count;
/*      */   }
/*      */ 
/*      */   private void compilePredicates(int opPos, Expression[] predicates)
/*      */     throws TransformerException
/*      */   {
/* 1004 */     for (int i = 0; 29 == getOp(opPos); i++)
/*      */     {
/* 1006 */       predicates[i] = predicate(opPos);
/* 1007 */       opPos = getNextOpPos(opPos);
/*      */     }
/*      */   }
/*      */ 
/*      */   Expression compileFunction(int opPos)
/*      */     throws TransformerException
/*      */   {
/* 1023 */     int endFunc = opPos + getOp(opPos + 1) - 1;
/*      */ 
/* 1025 */     opPos = getFirstChildPos(opPos);
/*      */ 
/* 1027 */     int funcID = getOp(opPos);
/*      */ 
/* 1029 */     opPos++;
/*      */ 
/* 1031 */     if (-1 != funcID)
/*      */     {
/* 1033 */       Function func = this.m_functionTable.getFunction(funcID);
/*      */ 
/* 1040 */       if ((func instanceof FuncExtFunctionAvailable)) {
/* 1041 */         ((FuncExtFunctionAvailable)func).setFunctionTable(this.m_functionTable);
/*      */       }
/* 1043 */       func.postCompileStep(this);
/*      */       try
/*      */       {
/* 1047 */         int i = 0;
/*      */ 
/* 1049 */         for (int p = opPos; p < endFunc; i++)
/*      */         {
/* 1054 */           func.setArg(compile(p), i);
/*      */ 
/* 1049 */           p = getNextOpPos(p);
/*      */         }
/*      */ 
/* 1057 */         func.checkNumberArgs(i);
/*      */       }
/*      */       catch (WrongNumberArgsException wnae)
/*      */       {
/* 1061 */         java.lang.String name = this.m_functionTable.getFunctionName(funcID);
/*      */ 
/* 1063 */         this.m_errorHandler.fatalError(new TransformerException(XSLMessages.createXPATHMessage("ER_ONLY_ALLOWS", new Object[] { name, wnae.getMessage() }), this.m_locator));
/*      */       }
/*      */ 
/* 1069 */       return func;
/*      */     }
/*      */ 
/* 1073 */     error("ER_FUNCTION_TOKEN_NOT_FOUND", null);
/*      */ 
/* 1075 */     return null;
/*      */   }
/*      */ 
/*      */   private synchronized long getNextMethodId()
/*      */   {
/* 1087 */     if (s_nextMethodId == 9223372036854775807L) {
/* 1088 */       s_nextMethodId = 0L;
/*      */     }
/* 1090 */     return s_nextMethodId++;
/*      */   }
/*      */ 
/*      */   private Expression compileExtension(int opPos)
/*      */     throws TransformerException
/*      */   {
/* 1106 */     int endExtFunc = opPos + getOp(opPos + 1) - 1;
/*      */ 
/* 1108 */     opPos = getFirstChildPos(opPos);
/*      */ 
/* 1110 */     java.lang.String ns = (java.lang.String)getTokenQueue().elementAt(getOp(opPos));
/*      */ 
/* 1112 */     opPos++;
/*      */ 
/* 1114 */     java.lang.String funcName = (java.lang.String)getTokenQueue().elementAt(getOp(opPos));
/*      */ 
/* 1117 */     opPos++;
/*      */ 
/* 1123 */     Function extension = new FuncExtFunction(ns, funcName, java.lang.String.valueOf(getNextMethodId()));
/*      */     try
/*      */     {
/* 1127 */       int i = 0;
/*      */ 
/* 1129 */       while (opPos < endExtFunc)
/*      */       {
/* 1131 */         int nextOpPos = getNextOpPos(opPos);
/*      */ 
/* 1133 */         extension.setArg(compile(opPos), i);
/*      */ 
/* 1135 */         opPos = nextOpPos;
/*      */ 
/* 1137 */         i++;
/*      */       }
/*      */ 
/*      */     }
/*      */     catch (WrongNumberArgsException wnae)
/*      */     {
/*      */     }
/*      */ 
/* 1145 */     return extension;
/*      */   }
/*      */ 
/*      */   public void warn(java.lang.String msg, Object[] args)
/*      */     throws TransformerException
/*      */   {
/* 1163 */     java.lang.String fmsg = XSLMessages.createXPATHWarning(msg, args);
/*      */ 
/* 1165 */     if (null != this.m_errorHandler)
/*      */     {
/* 1167 */       this.m_errorHandler.warning(new TransformerException(fmsg, this.m_locator));
/*      */     }
/*      */     else
/*      */     {
/* 1171 */       System.out.println(fmsg + "; file " + this.m_locator.getSystemId() + "; line " + this.m_locator.getLineNumber() + "; column " + this.m_locator.getColumnNumber());
/*      */     }
/*      */   }
/*      */ 
/*      */   public void assertion(boolean b, java.lang.String msg)
/*      */   {
/* 1190 */     if (!b)
/*      */     {
/* 1192 */       java.lang.String fMsg = XSLMessages.createXPATHMessage("ER_INCORRECT_PROGRAMMER_ASSERTION", new Object[] { msg });
/*      */ 
/* 1196 */       throw new RuntimeException(fMsg);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void error(java.lang.String msg, Object[] args)
/*      */     throws TransformerException
/*      */   {
/* 1216 */     java.lang.String fmsg = XSLMessages.createXPATHMessage(msg, args);
/*      */ 
/* 1219 */     if (null != this.m_errorHandler)
/*      */     {
/* 1221 */       this.m_errorHandler.fatalError(new TransformerException(fmsg, this.m_locator));
/*      */     }
/*      */     else
/*      */     {
/* 1230 */       throw new TransformerException(fmsg, (SAXSourceLocator)this.m_locator);
/*      */     }
/*      */   }
/*      */ 
/*      */   public PrefixResolver getNamespaceContext()
/*      */   {
/* 1246 */     return this.m_currentPrefixResolver;
/*      */   }
/*      */ 
/*      */   public void setNamespaceContext(PrefixResolver pr)
/*      */   {
/* 1256 */     this.m_currentPrefixResolver = pr;
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.compiler.Compiler
 * JD-Core Version:    0.6.2
 */