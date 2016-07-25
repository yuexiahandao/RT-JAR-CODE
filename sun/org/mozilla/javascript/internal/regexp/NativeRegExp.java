/*      */ package sun.org.mozilla.javascript.internal.regexp;
/*      */ 
/*      */ import sun.org.mozilla.javascript.internal.Context;
/*      */ import sun.org.mozilla.javascript.internal.Function;
/*      */ import sun.org.mozilla.javascript.internal.IdFunctionObject;
/*      */ import sun.org.mozilla.javascript.internal.IdScriptableObject;
/*      */ import sun.org.mozilla.javascript.internal.Kit;
/*      */ import sun.org.mozilla.javascript.internal.ScriptRuntime;
/*      */ import sun.org.mozilla.javascript.internal.Scriptable;
/*      */ import sun.org.mozilla.javascript.internal.Undefined;
/*      */ 
/*      */ public class NativeRegExp extends IdScriptableObject
/*      */   implements Function
/*      */ {
/*      */   static final long serialVersionUID = 4965263491464903264L;
/*   74 */   private static final Object REGEXP_TAG = new Object();
/*      */   public static final int JSREG_GLOB = 1;
/*      */   public static final int JSREG_FOLD = 2;
/*      */   public static final int JSREG_MULTILINE = 4;
/*      */   public static final int TEST = 0;
/*      */   public static final int MATCH = 1;
/*      */   public static final int PREFIX = 2;
/*      */   private static final boolean debug = false;
/*      */   private static final byte REOP_EMPTY = 0;
/*      */   private static final byte REOP_ALT = 1;
/*      */   private static final byte REOP_BOL = 2;
/*      */   private static final byte REOP_EOL = 3;
/*      */   private static final byte REOP_WBDRY = 4;
/*      */   private static final byte REOP_WNONBDRY = 5;
/*      */   private static final byte REOP_QUANT = 6;
/*      */   private static final byte REOP_STAR = 7;
/*      */   private static final byte REOP_PLUS = 8;
/*      */   private static final byte REOP_OPT = 9;
/*      */   private static final byte REOP_LPAREN = 10;
/*      */   private static final byte REOP_RPAREN = 11;
/*      */   private static final byte REOP_DOT = 12;
/*      */   private static final byte REOP_DIGIT = 14;
/*      */   private static final byte REOP_NONDIGIT = 15;
/*      */   private static final byte REOP_ALNUM = 16;
/*      */   private static final byte REOP_NONALNUM = 17;
/*      */   private static final byte REOP_SPACE = 18;
/*      */   private static final byte REOP_NONSPACE = 19;
/*      */   private static final byte REOP_BACKREF = 20;
/*      */   private static final byte REOP_FLAT = 21;
/*      */   private static final byte REOP_FLAT1 = 22;
/*      */   private static final byte REOP_JUMP = 23;
/*      */   private static final byte REOP_UCFLAT1 = 28;
/*      */   private static final byte REOP_FLATi = 32;
/*      */   private static final byte REOP_FLAT1i = 33;
/*      */   private static final byte REOP_UCFLAT1i = 35;
/*      */   private static final byte REOP_ASSERT = 41;
/*      */   private static final byte REOP_ASSERT_NOT = 42;
/*      */   private static final byte REOP_ASSERTTEST = 43;
/*      */   private static final byte REOP_ASSERTNOTTEST = 44;
/*      */   private static final byte REOP_MINIMALSTAR = 45;
/*      */   private static final byte REOP_MINIMALPLUS = 46;
/*      */   private static final byte REOP_MINIMALOPT = 47;
/*      */   private static final byte REOP_MINIMALQUANT = 48;
/*      */   private static final byte REOP_ENDCHILD = 49;
/*      */   private static final byte REOP_CLASS = 50;
/*      */   private static final byte REOP_REPEAT = 51;
/*      */   private static final byte REOP_MINIMALREPEAT = 52;
/*      */   private static final byte REOP_END = 53;
/*      */   private static final int OFFSET_LEN = 2;
/*      */   private static final int INDEX_LEN = 2;
/*      */   private static final int Id_lastIndex = 1;
/*      */   private static final int Id_source = 2;
/*      */   private static final int Id_global = 3;
/*      */   private static final int Id_ignoreCase = 4;
/*      */   private static final int Id_multiline = 5;
/*      */   private static final int MAX_INSTANCE_ID = 5;
/*      */   private static final int Id_compile = 1;
/*      */   private static final int Id_toString = 2;
/*      */   private static final int Id_toSource = 3;
/*      */   private static final int Id_exec = 4;
/*      */   private static final int Id_test = 5;
/*      */   private static final int Id_prefix = 6;
/*      */   private static final int MAX_PROTOTYPE_ID = 6;
/*      */   private RECompiled re;
/*      */   double lastIndex;
/*      */ 
/*      */   public static void init(Context paramContext, Scriptable paramScriptable, boolean paramBoolean)
/*      */   {
/*  147 */     NativeRegExp localNativeRegExp = new NativeRegExp();
/*  148 */     localNativeRegExp.re = ((RECompiled)compileRE(paramContext, "", null, false));
/*  149 */     localNativeRegExp.activatePrototypeMap(6);
/*  150 */     localNativeRegExp.setParentScope(paramScriptable);
/*  151 */     localNativeRegExp.setPrototype(getObjectPrototype(paramScriptable));
/*      */ 
/*  153 */     NativeRegExpCtor localNativeRegExpCtor = new NativeRegExpCtor();
/*      */ 
/*  156 */     localNativeRegExp.defineProperty("constructor", localNativeRegExpCtor, 2);
/*      */ 
/*  158 */     ScriptRuntime.setFunctionProtoAndParent(localNativeRegExpCtor, paramScriptable);
/*      */ 
/*  160 */     localNativeRegExpCtor.setImmunePrototypeProperty(localNativeRegExp);
/*      */ 
/*  162 */     if (paramBoolean) {
/*  163 */       localNativeRegExp.sealObject();
/*  164 */       localNativeRegExpCtor.sealObject();
/*      */     }
/*      */ 
/*  167 */     defineProperty(paramScriptable, "RegExp", localNativeRegExpCtor, 2);
/*      */   }
/*      */ 
/*      */   NativeRegExp(Scriptable paramScriptable, Object paramObject)
/*      */   {
/*  172 */     this.re = ((RECompiled)paramObject);
/*  173 */     this.lastIndex = 0.0D;
/*  174 */     ScriptRuntime.setObjectProtoAndParent(this, paramScriptable);
/*      */   }
/*      */ 
/*      */   public String getClassName()
/*      */   {
/*  180 */     return "RegExp";
/*      */   }
/*      */ 
/*      */   public String getTypeOf()
/*      */   {
/*  191 */     return "object";
/*      */   }
/*      */ 
/*      */   public Object call(Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject)
/*      */   {
/*  197 */     return execSub(paramContext, paramScriptable1, paramArrayOfObject, 1);
/*      */   }
/*      */ 
/*      */   public Scriptable construct(Context paramContext, Scriptable paramScriptable, Object[] paramArrayOfObject)
/*      */   {
/*  202 */     return (Scriptable)execSub(paramContext, paramScriptable, paramArrayOfObject, 1);
/*      */   }
/*      */ 
/*      */   Scriptable compile(Context paramContext, Scriptable paramScriptable, Object[] paramArrayOfObject)
/*      */   {
/*  207 */     if ((paramArrayOfObject.length > 0) && ((paramArrayOfObject[0] instanceof NativeRegExp))) {
/*  208 */       if ((paramArrayOfObject.length > 1) && (paramArrayOfObject[1] != Undefined.instance))
/*      */       {
/*  210 */         throw ScriptRuntime.typeError0("msg.bad.regexp.compile");
/*      */       }
/*  212 */       localObject = (NativeRegExp)paramArrayOfObject[0];
/*  213 */       this.re = ((NativeRegExp)localObject).re;
/*  214 */       this.lastIndex = ((NativeRegExp)localObject).lastIndex;
/*  215 */       return this;
/*      */     }
/*  217 */     Object localObject = paramArrayOfObject.length == 0 ? "" : ScriptRuntime.toString(paramArrayOfObject[0]);
/*  218 */     String str = (paramArrayOfObject.length > 1) && (paramArrayOfObject[1] != Undefined.instance) ? ScriptRuntime.toString(paramArrayOfObject[1]) : null;
/*      */ 
/*  221 */     this.re = ((RECompiled)compileRE(paramContext, (String)localObject, str, false));
/*  222 */     this.lastIndex = 0.0D;
/*  223 */     return this;
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/*  229 */     StringBuffer localStringBuffer = new StringBuffer();
/*  230 */     localStringBuffer.append('/');
/*  231 */     if (this.re.source.length != 0) {
/*  232 */       localStringBuffer.append(this.re.source);
/*      */     }
/*      */     else {
/*  235 */       localStringBuffer.append("(?:)");
/*      */     }
/*  237 */     localStringBuffer.append('/');
/*  238 */     if ((this.re.flags & 0x1) != 0)
/*  239 */       localStringBuffer.append('g');
/*  240 */     if ((this.re.flags & 0x2) != 0)
/*  241 */       localStringBuffer.append('i');
/*  242 */     if ((this.re.flags & 0x4) != 0)
/*  243 */       localStringBuffer.append('m');
/*  244 */     return localStringBuffer.toString();
/*      */   }
/*      */ 
/*      */   NativeRegExp() {
/*      */   }
/*      */ 
/*      */   private static RegExpImpl getImpl(Context paramContext) {
/*  251 */     return (RegExpImpl)ScriptRuntime.getRegExpProxy(paramContext);
/*      */   }
/*      */ 
/*      */   private Object execSub(Context paramContext, Scriptable paramScriptable, Object[] paramArrayOfObject, int paramInt)
/*      */   {
/*  257 */     RegExpImpl localRegExpImpl = getImpl(paramContext);
/*      */     String str;
/*  259 */     if (paramArrayOfObject.length == 0) {
/*  260 */       str = localRegExpImpl.input;
/*  261 */       if (str == null)
/*  262 */         reportError("msg.no.re.input.for", toString());
/*      */     }
/*      */     else {
/*  265 */       str = ScriptRuntime.toString(paramArrayOfObject[0]);
/*      */     }
/*  267 */     double d = (this.re.flags & 0x1) != 0 ? this.lastIndex : 0.0D;
/*      */     Object localObject;
/*  270 */     if ((d < 0.0D) || (str.length() < d)) {
/*  271 */       this.lastIndex = 0.0D;
/*  272 */       localObject = null;
/*      */     }
/*      */     else {
/*  275 */       int[] arrayOfInt = { (int)d };
/*  276 */       localObject = executeRegExp(paramContext, paramScriptable, localRegExpImpl, str, arrayOfInt, paramInt);
/*  277 */       if ((this.re.flags & 0x1) != 0) {
/*  278 */         this.lastIndex = ((localObject == null) || (localObject == Undefined.instance) ? 0.0D : arrayOfInt[0]);
/*      */       }
/*      */     }
/*      */ 
/*  282 */     return localObject;
/*      */   }
/*      */ 
/*      */   static Object compileRE(Context paramContext, String paramString1, String paramString2, boolean paramBoolean)
/*      */   {
/*  287 */     RECompiled localRECompiled = new RECompiled();
/*  288 */     localRECompiled.source = paramString1.toCharArray();
/*  289 */     int i = paramString1.length();
/*      */ 
/*  291 */     int j = 0;
/*  292 */     if (paramString2 != null) {
/*  293 */       for (int k = 0; k < paramString2.length(); k++) {
/*  294 */         char c = paramString2.charAt(k);
/*  295 */         if (c == 'g')
/*  296 */           j |= 1;
/*  297 */         else if (c == 'i')
/*  298 */           j |= 2;
/*  299 */         else if (c == 'm')
/*  300 */           j |= 4;
/*      */         else {
/*  302 */           reportError("msg.invalid.re.flag", String.valueOf(c));
/*      */         }
/*      */       }
/*      */     }
/*  306 */     localRECompiled.flags = j;
/*      */ 
/*  308 */     CompilerState localCompilerState = new CompilerState(paramContext, localRECompiled.source, i, j);
/*  309 */     if ((paramBoolean) && (i > 0))
/*      */     {
/*  313 */       localCompilerState.result = new RENode((byte)21);
/*  314 */       localCompilerState.result.chr = localCompilerState.cpbegin[0];
/*  315 */       localCompilerState.result.length = i;
/*  316 */       localCompilerState.result.flatIndex = 0;
/*  317 */       localCompilerState.progLength += 5;
/*      */     }
/*  320 */     else if (!parseDisjunction(localCompilerState)) {
/*  321 */       return null;
/*      */     }
/*  323 */     localRECompiled.program = new byte[localCompilerState.progLength + 1];
/*  324 */     if (localCompilerState.classCount != 0) {
/*  325 */       localRECompiled.classList = new RECharSet[localCompilerState.classCount];
/*  326 */       localRECompiled.classCount = localCompilerState.classCount;
/*      */     }
/*  328 */     int m = emitREBytecode(localCompilerState, localRECompiled, 0, localCompilerState.result);
/*  329 */     localRECompiled.program[(m++)] = 53;
/*      */ 
/*  339 */     localRECompiled.parenCount = localCompilerState.parenCount;
/*      */ 
/*  342 */     switch (localRECompiled.program[0]) {
/*      */     case 28:
/*      */     case 35:
/*  345 */       localRECompiled.anchorCh = ((char)getIndex(localRECompiled.program, 1));
/*  346 */       break;
/*      */     case 22:
/*      */     case 33:
/*  349 */       localRECompiled.anchorCh = ((char)(localRECompiled.program[1] & 0xFF));
/*  350 */       break;
/*      */     case 21:
/*      */     case 32:
/*  353 */       int n = getIndex(localRECompiled.program, 1);
/*  354 */       localRECompiled.anchorCh = localRECompiled.source[n];
/*      */     case 23:
/*      */     case 24:
/*      */     case 25:
/*      */     case 26:
/*      */     case 27:
/*      */     case 29:
/*      */     case 30:
/*      */     case 31:
/*  363 */     case 34: } return localRECompiled;
/*      */   }
/*      */ 
/*      */   static boolean isDigit(char paramChar)
/*      */   {
/*  368 */     return ('0' <= paramChar) && (paramChar <= '9');
/*      */   }
/*      */ 
/*      */   private static boolean isWord(char paramChar)
/*      */   {
/*  373 */     return (Character.isLetter(paramChar)) || (isDigit(paramChar)) || (paramChar == '_');
/*      */   }
/*      */ 
/*      */   private static boolean isLineTerm(char paramChar)
/*      */   {
/*  378 */     return ScriptRuntime.isJSLineTerminator(paramChar);
/*      */   }
/*      */ 
/*      */   private static boolean isREWhiteSpace(int paramInt)
/*      */   {
/*  383 */     return ScriptRuntime.isJSWhitespaceOrLineTerminator(paramInt);
/*      */   }
/*      */ 
/*      */   private static char upcase(char paramChar)
/*      */   {
/*  399 */     if (paramChar < '') {
/*  400 */       if (('a' <= paramChar) && (paramChar <= 'z')) {
/*  401 */         return (char)(paramChar + '￠');
/*      */       }
/*  403 */       return paramChar;
/*      */     }
/*  405 */     char c = Character.toUpperCase(paramChar);
/*  406 */     if ((paramChar >= '') && (c < '')) return paramChar;
/*  407 */     return c;
/*      */   }
/*      */ 
/*      */   private static char downcase(char paramChar)
/*      */   {
/*  412 */     if (paramChar < '') {
/*  413 */       if (('A' <= paramChar) && (paramChar <= 'Z')) {
/*  414 */         return (char)(paramChar + ' ');
/*      */       }
/*  416 */       return paramChar;
/*      */     }
/*  418 */     char c = Character.toLowerCase(paramChar);
/*  419 */     if ((paramChar >= '') && (c < '')) return paramChar;
/*  420 */     return c;
/*      */   }
/*      */ 
/*      */   private static int toASCIIHexDigit(int paramInt)
/*      */   {
/*  428 */     if (paramInt < 48)
/*  429 */       return -1;
/*  430 */     if (paramInt <= 57) {
/*  431 */       return paramInt - 48;
/*      */     }
/*  433 */     paramInt |= 32;
/*  434 */     if ((97 <= paramInt) && (paramInt <= 102)) {
/*  435 */       return paramInt - 97 + 10;
/*      */     }
/*  437 */     return -1;
/*      */   }
/*      */ 
/*      */   private static boolean parseDisjunction(CompilerState paramCompilerState)
/*      */   {
/*  448 */     if (!parseAlternative(paramCompilerState))
/*  449 */       return false;
/*  450 */     char[] arrayOfChar = paramCompilerState.cpbegin;
/*  451 */     int i = paramCompilerState.cp;
/*  452 */     if ((i != arrayOfChar.length) && (arrayOfChar[i] == '|'))
/*      */     {
/*  454 */       paramCompilerState.cp += 1;
/*  455 */       RENode localRENode = new RENode((byte)1);
/*  456 */       localRENode.kid = paramCompilerState.result;
/*  457 */       if (!parseDisjunction(paramCompilerState))
/*  458 */         return false;
/*  459 */       localRENode.kid2 = paramCompilerState.result;
/*  460 */       paramCompilerState.result = localRENode;
/*      */ 
/*  462 */       paramCompilerState.progLength += 9;
/*      */     }
/*  464 */     return true;
/*      */   }
/*      */ 
/*      */   private static boolean parseAlternative(CompilerState paramCompilerState)
/*      */   {
/*  473 */     RENode localRENode1 = null;
/*  474 */     RENode localRENode2 = null;
/*  475 */     char[] arrayOfChar = paramCompilerState.cpbegin;
/*      */     while (true) {
/*  477 */       if ((paramCompilerState.cp == paramCompilerState.cpend) || (arrayOfChar[paramCompilerState.cp] == '|') || ((paramCompilerState.parenNesting != 0) && (arrayOfChar[paramCompilerState.cp] == ')')))
/*      */       {
/*  480 */         if (localRENode1 == null) {
/*  481 */           paramCompilerState.result = new RENode((byte)0);
/*      */         }
/*      */         else
/*  484 */           paramCompilerState.result = localRENode1;
/*  485 */         return true;
/*      */       }
/*  487 */       if (!parseTerm(paramCompilerState))
/*  488 */         return false;
/*  489 */       if (localRENode1 == null) {
/*  490 */         localRENode1 = paramCompilerState.result;
/*      */       }
/*  492 */       else if (localRENode2 == null) {
/*  493 */         localRENode1.next = paramCompilerState.result;
/*  494 */         localRENode2 = paramCompilerState.result;
/*  495 */         while (localRENode2.next != null) localRENode2 = localRENode2.next; 
/*      */       }
/*      */       else
/*      */       {
/*  498 */         localRENode2.next = paramCompilerState.result;
/*  499 */         localRENode2 = localRENode2.next;
/*  500 */         while (localRENode2.next != null) localRENode2 = localRENode2.next;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private static boolean calculateBitmapSize(CompilerState paramCompilerState, RENode paramRENode, char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*      */   {
/*  511 */     int i = 0;
/*      */ 
/*  516 */     int i1 = 0;
/*  517 */     int i2 = 0;
/*      */ 
/*  519 */     paramRENode.bmsize = 0;
/*      */ 
/*  521 */     if (paramInt1 == paramInt2) {
/*  522 */       return true;
/*      */     }
/*  524 */     if (paramArrayOfChar[paramInt1] == '^') {
/*  525 */       paramInt1++;
/*      */     }
/*  527 */     while (paramInt1 != paramInt2) {
/*  528 */       int i3 = 0;
/*  529 */       int m = 2;
/*  530 */       switch (paramArrayOfChar[paramInt1]) {
/*      */       case '\\':
/*  532 */         paramInt1++;
/*  533 */         int j = paramArrayOfChar[(paramInt1++)];
/*      */         int k;
/*      */         int n;
/*  534 */         switch (j) {
/*      */         case 98:
/*  536 */           i3 = 8;
/*  537 */           break;
/*      */         case 102:
/*  539 */           i3 = 12;
/*  540 */           break;
/*      */         case 110:
/*  542 */           i3 = 10;
/*  543 */           break;
/*      */         case 114:
/*  545 */           i3 = 13;
/*  546 */           break;
/*      */         case 116:
/*  548 */           i3 = 9;
/*  549 */           break;
/*      */         case 118:
/*  551 */           i3 = 11;
/*  552 */           break;
/*      */         case 99:
/*  554 */           if ((paramInt1 + 1 < paramInt2) && (Character.isLetter(paramArrayOfChar[(paramInt1 + 1)])))
/*  555 */             i3 = (char)(paramArrayOfChar[(paramInt1++)] & 0x1F);
/*      */           else
/*  557 */             i3 = 92;
/*  558 */           break;
/*      */         case 117:
/*  560 */           m += 2;
/*      */         case 120:
/*  563 */           k = 0;
/*  564 */           for (n = 0; (n < m) && (paramInt1 < paramInt2); n++) {
/*  565 */             j = paramArrayOfChar[(paramInt1++)];
/*  566 */             k = Kit.xDigitToInt(j, k);
/*  567 */             if (k < 0)
/*      */             {
/*  570 */               paramInt1 -= n + 1;
/*  571 */               k = 92;
/*  572 */               break;
/*      */             }
/*      */           }
/*  575 */           i3 = k;
/*  576 */           break;
/*      */         case 100:
/*  578 */           if (i2 != 0) {
/*  579 */             reportError("msg.bad.range", "");
/*  580 */             return false;
/*      */           }
/*  582 */           i3 = 57;
/*  583 */           break;
/*      */         case 68:
/*      */         case 83:
/*      */         case 87:
/*      */         case 115:
/*      */         case 119:
/*  589 */           if (i2 != 0) {
/*  590 */             reportError("msg.bad.range", "");
/*  591 */             return false;
/*      */           }
/*  593 */           paramRENode.bmsize = 65535;
/*  594 */           return true;
/*      */         case 48:
/*      */         case 49:
/*      */         case 50:
/*      */         case 51:
/*      */         case 52:
/*      */         case 53:
/*      */         case 54:
/*      */         case 55:
/*  609 */           k = j - 48;
/*  610 */           j = paramArrayOfChar[paramInt1];
/*  611 */           if ((48 <= j) && (j <= 55)) {
/*  612 */             paramInt1++;
/*  613 */             k = 8 * k + (j - 48);
/*  614 */             j = paramArrayOfChar[paramInt1];
/*  615 */             if ((48 <= j) && (j <= 55)) {
/*  616 */               paramInt1++;
/*  617 */               n = 8 * k + (j - 48);
/*  618 */               if (n <= 255)
/*  619 */                 k = n;
/*      */               else
/*  621 */                 paramInt1--;
/*      */             }
/*      */           }
/*  624 */           i3 = k;
/*  625 */           break;
/*      */         case 56:
/*      */         case 57:
/*      */         case 58:
/*      */         case 59:
/*      */         case 60:
/*      */         case 61:
/*      */         case 62:
/*      */         case 63:
/*      */         case 64:
/*      */         case 65:
/*      */         case 66:
/*      */         case 67:
/*      */         case 69:
/*      */         case 70:
/*      */         case 71:
/*      */         case 72:
/*      */         case 73:
/*      */         case 74:
/*      */         case 75:
/*      */         case 76:
/*      */         case 77:
/*      */         case 78:
/*      */         case 79:
/*      */         case 80:
/*      */         case 81:
/*      */         case 82:
/*      */         case 84:
/*      */         case 85:
/*      */         case 86:
/*      */         case 88:
/*      */         case 89:
/*      */         case 90:
/*      */         case 91:
/*      */         case 92:
/*      */         case 93:
/*      */         case 94:
/*      */         case 95:
/*      */         case 96:
/*      */         case 97:
/*      */         case 101:
/*      */         case 103:
/*      */         case 104:
/*      */         case 105:
/*      */         case 106:
/*      */         case 107:
/*      */         case 108:
/*      */         case 109:
/*      */         case 111:
/*      */         case 112:
/*      */         case 113:
/*      */         default:
/*  628 */           i3 = j;
/*  629 */         }break;
/*      */       default:
/*  633 */         i3 = paramArrayOfChar[(paramInt1++)];
/*      */       }
/*      */ 
/*  636 */       if (i2 != 0) {
/*  637 */         if (i > i3) {
/*  638 */           reportError("msg.bad.range", "");
/*  639 */           return false;
/*      */         }
/*  641 */         i2 = 0;
/*      */       }
/*  644 */       else if ((paramInt1 < paramInt2 - 1) && 
/*  645 */         (paramArrayOfChar[paramInt1] == '-')) {
/*  646 */         paramInt1++;
/*  647 */         i2 = 1;
/*  648 */         i = (char)i3;
/*  649 */         continue;
/*      */       }
/*      */ 
/*  653 */       if ((paramCompilerState.flags & 0x2) != 0) {
/*  654 */         int i4 = upcase((char)i3);
/*  655 */         int i5 = downcase((char)i3);
/*  656 */         i3 = i4 >= i5 ? i4 : i5;
/*      */       }
/*  658 */       if (i3 > i1)
/*  659 */         i1 = i3;
/*      */     }
/*  661 */     paramRENode.bmsize = i1;
/*  662 */     return true;
/*      */   }
/*      */ 
/*      */   private static void doFlat(CompilerState paramCompilerState, char paramChar)
/*      */   {
/*  721 */     paramCompilerState.result = new RENode((byte)21);
/*  722 */     paramCompilerState.result.chr = paramChar;
/*  723 */     paramCompilerState.result.length = 1;
/*  724 */     paramCompilerState.result.flatIndex = -1;
/*  725 */     paramCompilerState.progLength += 3;
/*      */   }
/*      */ 
/*      */   private static int getDecimalValue(char paramChar, CompilerState paramCompilerState, int paramInt, String paramString)
/*      */   {
/*  732 */     int i = 0;
/*  733 */     int j = paramCompilerState.cp;
/*  734 */     char[] arrayOfChar = paramCompilerState.cpbegin;
/*  735 */     int k = paramChar - '0';
/*  736 */     for (; paramCompilerState.cp != paramCompilerState.cpend; paramCompilerState.cp += 1) {
/*  737 */       paramChar = arrayOfChar[paramCompilerState.cp];
/*  738 */       if (!isDigit(paramChar)) {
/*      */         break;
/*      */       }
/*  741 */       if (i == 0) {
/*  742 */         int m = paramChar - '0';
/*  743 */         if (k < (paramInt - m) / 10) {
/*  744 */           k = k * 10 + m;
/*      */         } else {
/*  746 */           i = 1;
/*  747 */           k = paramInt;
/*      */         }
/*      */       }
/*      */     }
/*  751 */     if (i != 0) {
/*  752 */       reportError(paramString, String.valueOf(arrayOfChar, j, paramCompilerState.cp - j));
/*      */     }
/*      */ 
/*  755 */     return k;
/*      */   }
/*      */ 
/*      */   private static boolean parseTerm(CompilerState paramCompilerState)
/*      */   {
/*  761 */     char[] arrayOfChar = paramCompilerState.cpbegin;
/*  762 */     char c = arrayOfChar[(paramCompilerState.cp++)];
/*  763 */     int i = 2;
/*  764 */     int j = paramCompilerState.parenCount;
/*      */     int n;
/*      */     int i3;
/*  769 */     switch (c)
/*      */     {
/*      */     case '^':
/*  772 */       paramCompilerState.result = new RENode((byte)2);
/*  773 */       paramCompilerState.progLength += 1;
/*  774 */       return true;
/*      */     case '$':
/*  776 */       paramCompilerState.result = new RENode((byte)3);
/*  777 */       paramCompilerState.progLength += 1;
/*  778 */       return true;
/*      */     case '\\':
/*  780 */       if (paramCompilerState.cp < paramCompilerState.cpend) {
/*  781 */         c = arrayOfChar[(paramCompilerState.cp++)];
/*      */         int k;
/*      */         int m;
/*  782 */         switch (c)
/*      */         {
/*      */         case 'b':
/*  785 */           paramCompilerState.result = new RENode((byte)4);
/*  786 */           paramCompilerState.progLength += 1;
/*  787 */           return true;
/*      */         case 'B':
/*  789 */           paramCompilerState.result = new RENode((byte)5);
/*  790 */           paramCompilerState.progLength += 1;
/*  791 */           return true;
/*      */         case '0':
/*  801 */           reportWarning(paramCompilerState.cx, "msg.bad.backref", "");
/*      */ 
/*  803 */           k = 0;
/*  804 */           while (paramCompilerState.cp < paramCompilerState.cpend) {
/*  805 */             c = arrayOfChar[paramCompilerState.cp];
/*  806 */             if ((c < '0') || (c > '7')) break;
/*  807 */             paramCompilerState.cp += 1;
/*  808 */             m = 8 * k + (c - '0');
/*  809 */             if (m > 255)
/*      */               break;
/*  811 */             k = m;
/*      */           }
/*      */ 
/*  816 */           c = (char)k;
/*  817 */           doFlat(paramCompilerState, c);
/*  818 */           break;
/*      */         case '1':
/*      */         case '2':
/*      */         case '3':
/*      */         case '4':
/*      */         case '5':
/*      */         case '6':
/*      */         case '7':
/*      */         case '8':
/*      */         case '9':
/*  828 */           n = paramCompilerState.cp - 1;
/*  829 */           k = getDecimalValue(c, paramCompilerState, 65535, "msg.overlarge.backref");
/*      */ 
/*  831 */           if (k > paramCompilerState.parenCount) {
/*  832 */             reportWarning(paramCompilerState.cx, "msg.bad.backref", "");
/*      */           }
/*      */ 
/*  837 */           if ((k > 9) && (k > paramCompilerState.parenCount)) {
/*  838 */             paramCompilerState.cp = n;
/*  839 */             k = 0;
/*  840 */             while (paramCompilerState.cp < paramCompilerState.cpend) {
/*  841 */               c = arrayOfChar[paramCompilerState.cp];
/*  842 */               if ((c < '0') || (c > '7')) break;
/*  843 */               paramCompilerState.cp += 1;
/*  844 */               m = 8 * k + (c - '0');
/*  845 */               if (m > 255)
/*      */                 break;
/*  847 */               k = m;
/*      */             }
/*      */ 
/*  852 */             c = (char)k;
/*  853 */             doFlat(paramCompilerState, c);
/*      */           }
/*      */           else
/*      */           {
/*  857 */             paramCompilerState.result = new RENode((byte)20);
/*  858 */             paramCompilerState.result.parenIndex = (k - 1);
/*  859 */             paramCompilerState.progLength += 3;
/*  860 */           }break;
/*      */         case 'f':
/*  863 */           c = '\f';
/*  864 */           doFlat(paramCompilerState, c);
/*  865 */           break;
/*      */         case 'n':
/*  867 */           c = '\n';
/*  868 */           doFlat(paramCompilerState, c);
/*  869 */           break;
/*      */         case 'r':
/*  871 */           c = '\r';
/*  872 */           doFlat(paramCompilerState, c);
/*  873 */           break;
/*      */         case 't':
/*  875 */           c = '\t';
/*  876 */           doFlat(paramCompilerState, c);
/*  877 */           break;
/*      */         case 'v':
/*  879 */           c = '\013';
/*  880 */           doFlat(paramCompilerState, c);
/*  881 */           break;
/*      */         case 'c':
/*  884 */           if ((paramCompilerState.cp + 1 < paramCompilerState.cpend) && (Character.isLetter(arrayOfChar[(paramCompilerState.cp + 1)])))
/*      */           {
/*  886 */             c = (char)(arrayOfChar[(paramCompilerState.cp++)] & 0x1F);
/*      */           }
/*      */           else {
/*  889 */             paramCompilerState.cp -= 1;
/*  890 */             c = '\\';
/*      */           }
/*  892 */           doFlat(paramCompilerState, c);
/*  893 */           break;
/*      */         case 'u':
/*  896 */           i += 2;
/*      */         case 'x':
/*  901 */           int i1 = 0;
/*      */ 
/*  903 */           for (i3 = 0; 
/*  904 */             (i3 < i) && (paramCompilerState.cp < paramCompilerState.cpend); i3++) {
/*  905 */             c = arrayOfChar[(paramCompilerState.cp++)];
/*  906 */             i1 = Kit.xDigitToInt(c, i1);
/*  907 */             if (i1 < 0)
/*      */             {
/*  910 */               paramCompilerState.cp -= i3 + 2;
/*  911 */               i1 = arrayOfChar[(paramCompilerState.cp++)];
/*  912 */               break;
/*      */             }
/*      */           }
/*  915 */           c = (char)i1;
/*      */ 
/*  917 */           doFlat(paramCompilerState, c);
/*  918 */           break;
/*      */         case 'd':
/*  921 */           paramCompilerState.result = new RENode((byte)14);
/*  922 */           paramCompilerState.progLength += 1;
/*  923 */           break;
/*      */         case 'D':
/*  925 */           paramCompilerState.result = new RENode((byte)15);
/*  926 */           paramCompilerState.progLength += 1;
/*  927 */           break;
/*      */         case 's':
/*  929 */           paramCompilerState.result = new RENode((byte)18);
/*  930 */           paramCompilerState.progLength += 1;
/*  931 */           break;
/*      */         case 'S':
/*  933 */           paramCompilerState.result = new RENode((byte)19);
/*  934 */           paramCompilerState.progLength += 1;
/*  935 */           break;
/*      */         case 'w':
/*  937 */           paramCompilerState.result = new RENode((byte)16);
/*  938 */           paramCompilerState.progLength += 1;
/*  939 */           break;
/*      */         case 'W':
/*  941 */           paramCompilerState.result = new RENode((byte)17);
/*  942 */           paramCompilerState.progLength += 1;
/*  943 */           break;
/*      */         case ':':
/*      */         case ';':
/*      */         case '<':
/*      */         case '=':
/*      */         case '>':
/*      */         case '?':
/*      */         case '@':
/*      */         case 'A':
/*      */         case 'C':
/*      */         case 'E':
/*      */         case 'F':
/*      */         case 'G':
/*      */         case 'H':
/*      */         case 'I':
/*      */         case 'J':
/*      */         case 'K':
/*      */         case 'L':
/*      */         case 'M':
/*      */         case 'N':
/*      */         case 'O':
/*      */         case 'P':
/*      */         case 'Q':
/*      */         case 'R':
/*      */         case 'T':
/*      */         case 'U':
/*      */         case 'V':
/*      */         case 'X':
/*      */         case 'Y':
/*      */         case 'Z':
/*      */         case '[':
/*      */         case '\\':
/*      */         case ']':
/*      */         case '^':
/*      */         case '_':
/*      */         case '`':
/*      */         case 'a':
/*      */         case 'e':
/*      */         case 'g':
/*      */         case 'h':
/*      */         case 'i':
/*      */         case 'j':
/*      */         case 'k':
/*      */         case 'l':
/*      */         case 'm':
/*      */         case 'o':
/*      */         case 'p':
/*      */         case 'q':
/*      */         default:
/*  946 */           paramCompilerState.result = new RENode((byte)21);
/*  947 */           paramCompilerState.result.chr = c;
/*  948 */           paramCompilerState.result.length = 1;
/*  949 */           paramCompilerState.result.flatIndex = (paramCompilerState.cp - 1);
/*  950 */           paramCompilerState.progLength += 3;
/*  951 */           break;
/*      */         }
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*  957 */         reportError("msg.trail.backslash", "");
/*  958 */         return false;
/*      */       }break;
/*      */     case '(':
/*  961 */       RENode localRENode2 = null;
/*  962 */       n = paramCompilerState.cp;
/*  963 */       if ((paramCompilerState.cp + 1 < paramCompilerState.cpend) && (arrayOfChar[paramCompilerState.cp] == '?') && (((c = arrayOfChar[(paramCompilerState.cp + 1)]) == '=') || (c == '!') || (c == ':')))
/*      */       {
/*  966 */         paramCompilerState.cp += 2;
/*  967 */         if (c == '=') {
/*  968 */           localRENode2 = new RENode((byte)41);
/*      */ 
/*  970 */           paramCompilerState.progLength += 4;
/*  971 */         } else if (c == '!') {
/*  972 */           localRENode2 = new RENode((byte)42);
/*      */ 
/*  974 */           paramCompilerState.progLength += 4;
/*      */         }
/*      */       } else {
/*  977 */         localRENode2 = new RENode((byte)10);
/*      */ 
/*  979 */         paramCompilerState.progLength += 6;
/*  980 */         localRENode2.parenIndex = (paramCompilerState.parenCount++);
/*      */       }
/*  982 */       paramCompilerState.parenNesting += 1;
/*  983 */       if (!parseDisjunction(paramCompilerState))
/*  984 */         return false;
/*  985 */       if ((paramCompilerState.cp == paramCompilerState.cpend) || (arrayOfChar[paramCompilerState.cp] != ')')) {
/*  986 */         reportError("msg.unterm.paren", "");
/*  987 */         return false;
/*      */       }
/*  989 */       paramCompilerState.cp += 1;
/*  990 */       paramCompilerState.parenNesting -= 1;
/*  991 */       if (localRENode2 != null) {
/*  992 */         localRENode2.kid = paramCompilerState.result;
/*  993 */         paramCompilerState.result = localRENode2; } break;
/*      */     case ')':
/*  998 */       reportError("msg.re.unmatched.right.paren", "");
/*  999 */       return false;
/*      */     case '[':
/* 1001 */       paramCompilerState.result = new RENode((byte)50);
/* 1002 */       n = paramCompilerState.cp;
/* 1003 */       paramCompilerState.result.startIndex = n;
/*      */       while (true) {
/* 1005 */         if (paramCompilerState.cp == paramCompilerState.cpend) {
/* 1006 */           reportError("msg.unterm.class", "");
/* 1007 */           return false;
/*      */         }
/* 1009 */         if (arrayOfChar[paramCompilerState.cp] == '\\') {
/* 1010 */           paramCompilerState.cp += 1;
/*      */         }
/* 1012 */         else if (arrayOfChar[paramCompilerState.cp] == ']') {
/* 1013 */           paramCompilerState.result.kidlen = (paramCompilerState.cp - n);
/* 1014 */           break;
/*      */         }
/*      */ 
/* 1017 */         paramCompilerState.cp += 1;
/*      */       }
/* 1019 */       paramCompilerState.result.index = (paramCompilerState.classCount++);
/*      */ 
/* 1024 */       if (!calculateBitmapSize(paramCompilerState, paramCompilerState.result, arrayOfChar, n, paramCompilerState.cp++))
/* 1025 */         return false;
/* 1026 */       paramCompilerState.progLength += 3;
/* 1027 */       break;
/*      */     case '.':
/* 1030 */       paramCompilerState.result = new RENode((byte)12);
/* 1031 */       paramCompilerState.progLength += 1;
/* 1032 */       break;
/*      */     case '*':
/*      */     case '+':
/*      */     case '?':
/* 1036 */       reportError("msg.bad.quant", String.valueOf(arrayOfChar[(paramCompilerState.cp - 1)]));
/* 1037 */       return false;
/*      */     default:
/* 1039 */       paramCompilerState.result = new RENode((byte)21);
/* 1040 */       paramCompilerState.result.chr = c;
/* 1041 */       paramCompilerState.result.length = 1;
/* 1042 */       paramCompilerState.result.flatIndex = (paramCompilerState.cp - 1);
/* 1043 */       paramCompilerState.progLength += 3;
/*      */     }
/*      */ 
/* 1047 */     RENode localRENode1 = paramCompilerState.result;
/* 1048 */     if (paramCompilerState.cp == paramCompilerState.cpend) {
/* 1049 */       return true;
/*      */     }
/* 1051 */     int i2 = 0;
/* 1052 */     switch (arrayOfChar[paramCompilerState.cp]) {
/*      */     case '+':
/* 1054 */       paramCompilerState.result = new RENode((byte)6);
/* 1055 */       paramCompilerState.result.min = 1;
/* 1056 */       paramCompilerState.result.max = -1;
/*      */ 
/* 1058 */       paramCompilerState.progLength += 8;
/* 1059 */       i2 = 1;
/* 1060 */       break;
/*      */     case '*':
/* 1062 */       paramCompilerState.result = new RENode((byte)6);
/* 1063 */       paramCompilerState.result.min = 0;
/* 1064 */       paramCompilerState.result.max = -1;
/*      */ 
/* 1066 */       paramCompilerState.progLength += 8;
/* 1067 */       i2 = 1;
/* 1068 */       break;
/*      */     case '?':
/* 1070 */       paramCompilerState.result = new RENode((byte)6);
/* 1071 */       paramCompilerState.result.min = 0;
/* 1072 */       paramCompilerState.result.max = 1;
/*      */ 
/* 1074 */       paramCompilerState.progLength += 8;
/* 1075 */       i2 = 1;
/* 1076 */       break;
/*      */     case '{':
/* 1079 */       i3 = 0;
/* 1080 */       int i4 = -1;
/* 1081 */       int i5 = paramCompilerState.cp;
/*      */ 
/* 1089 */       c = arrayOfChar[(++paramCompilerState.cp)];
/* 1090 */       if (isDigit(c)) {
/* 1091 */         paramCompilerState.cp += 1;
/* 1092 */         i3 = getDecimalValue(c, paramCompilerState, 65535, "msg.overlarge.min");
/*      */ 
/* 1094 */         c = arrayOfChar[paramCompilerState.cp];
/* 1095 */         if (c == ',') {
/* 1096 */           c = arrayOfChar[(++paramCompilerState.cp)];
/* 1097 */           if (isDigit(c)) {
/* 1098 */             paramCompilerState.cp += 1;
/* 1099 */             i4 = getDecimalValue(c, paramCompilerState, 65535, "msg.overlarge.max");
/*      */ 
/* 1101 */             c = arrayOfChar[paramCompilerState.cp];
/* 1102 */             if (i3 > i4) {
/* 1103 */               reportError("msg.max.lt.min", String.valueOf(arrayOfChar[paramCompilerState.cp]));
/*      */ 
/* 1105 */               return false;
/*      */             }
/*      */           }
/*      */         } else {
/* 1109 */           i4 = i3;
/*      */         }
/*      */ 
/* 1112 */         if (c == '}') {
/* 1113 */           paramCompilerState.result = new RENode((byte)6);
/* 1114 */           paramCompilerState.result.min = i3;
/* 1115 */           paramCompilerState.result.max = i4;
/*      */ 
/* 1118 */           paramCompilerState.progLength += 12;
/* 1119 */           i2 = 1;
/*      */         }
/*      */       }
/* 1122 */       if (i2 == 0)
/* 1123 */         paramCompilerState.cp = i5; break;
/*      */     }
/*      */ 
/* 1128 */     if (i2 == 0) {
/* 1129 */       return true;
/*      */     }
/* 1131 */     paramCompilerState.cp += 1;
/* 1132 */     paramCompilerState.result.kid = localRENode1;
/* 1133 */     paramCompilerState.result.parenIndex = j;
/* 1134 */     paramCompilerState.result.parenCount = (paramCompilerState.parenCount - j);
/* 1135 */     if ((paramCompilerState.cp < paramCompilerState.cpend) && (arrayOfChar[paramCompilerState.cp] == '?')) {
/* 1136 */       paramCompilerState.cp += 1;
/* 1137 */       paramCompilerState.result.greedy = false;
/*      */     }
/*      */     else {
/* 1140 */       paramCompilerState.result.greedy = true;
/* 1141 */     }return true;
/*      */   }
/*      */ 
/*      */   private static void resolveForwardJump(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*      */   {
/* 1146 */     if (paramInt1 > paramInt2) throw Kit.codeBug();
/* 1147 */     addIndex(paramArrayOfByte, paramInt1, paramInt2 - paramInt1);
/*      */   }
/*      */ 
/*      */   private static int getOffset(byte[] paramArrayOfByte, int paramInt)
/*      */   {
/* 1152 */     return getIndex(paramArrayOfByte, paramInt);
/*      */   }
/*      */ 
/*      */   private static int addIndex(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*      */   {
/* 1157 */     if (paramInt2 < 0) throw Kit.codeBug();
/* 1158 */     if (paramInt2 > 65535)
/* 1159 */       throw Context.reportRuntimeError("Too complex regexp");
/* 1160 */     paramArrayOfByte[paramInt1] = ((byte)(paramInt2 >> 8));
/* 1161 */     paramArrayOfByte[(paramInt1 + 1)] = ((byte)paramInt2);
/* 1162 */     return paramInt1 + 2;
/*      */   }
/*      */ 
/*      */   private static int getIndex(byte[] paramArrayOfByte, int paramInt)
/*      */   {
/* 1167 */     return (paramArrayOfByte[paramInt] & 0xFF) << 8 | paramArrayOfByte[(paramInt + 1)] & 0xFF;
/*      */   }
/*      */ 
/*      */   private static int emitREBytecode(CompilerState paramCompilerState, RECompiled paramRECompiled, int paramInt, RENode paramRENode)
/*      */   {
/* 1178 */     byte[] arrayOfByte = paramRECompiled.program;
/*      */ 
/* 1180 */     while (paramRENode != null) {
/* 1181 */       arrayOfByte[(paramInt++)] = paramRENode.op;
/*      */       int j;
/* 1182 */       switch (paramRENode.op) {
/*      */       case 0:
/* 1184 */         paramInt--;
/* 1185 */         break;
/*      */       case 1:
/* 1187 */         RENode localRENode = paramRENode.kid2;
/* 1188 */         int i = paramInt;
/* 1189 */         paramInt += 2;
/* 1190 */         paramInt = emitREBytecode(paramCompilerState, paramRECompiled, paramInt, paramRENode.kid);
/* 1191 */         arrayOfByte[(paramInt++)] = 23;
/* 1192 */         j = paramInt;
/* 1193 */         paramInt += 2;
/* 1194 */         resolveForwardJump(arrayOfByte, i, paramInt);
/* 1195 */         paramInt = emitREBytecode(paramCompilerState, paramRECompiled, paramInt, localRENode);
/*      */ 
/* 1197 */         arrayOfByte[(paramInt++)] = 23;
/* 1198 */         i = paramInt;
/* 1199 */         paramInt += 2;
/*      */ 
/* 1201 */         resolveForwardJump(arrayOfByte, j, paramInt);
/* 1202 */         resolveForwardJump(arrayOfByte, i, paramInt);
/* 1203 */         break;
/*      */       case 21:
/* 1208 */         if (paramRENode.flatIndex != -1)
/*      */         {
/* 1210 */           while ((paramRENode.next != null) && (paramRENode.next.op == 21) && (paramRENode.flatIndex + paramRENode.length == paramRENode.next.flatIndex))
/*      */           {
/* 1212 */             paramRENode.length += paramRENode.next.length;
/* 1213 */             paramRENode.next = paramRENode.next.next;
/*      */           }
/*      */         }
/* 1216 */         if ((paramRENode.flatIndex != -1) && (paramRENode.length > 1)) {
/* 1217 */           if ((paramCompilerState.flags & 0x2) != 0)
/* 1218 */             arrayOfByte[(paramInt - 1)] = 32;
/*      */           else
/* 1220 */             arrayOfByte[(paramInt - 1)] = 21;
/* 1221 */           paramInt = addIndex(arrayOfByte, paramInt, paramRENode.flatIndex);
/* 1222 */           paramInt = addIndex(arrayOfByte, paramInt, paramRENode.length);
/*      */         }
/* 1225 */         else if (paramRENode.chr < 'Ā') {
/* 1226 */           if ((paramCompilerState.flags & 0x2) != 0)
/* 1227 */             arrayOfByte[(paramInt - 1)] = 33;
/*      */           else
/* 1229 */             arrayOfByte[(paramInt - 1)] = 22;
/* 1230 */           arrayOfByte[(paramInt++)] = ((byte)paramRENode.chr);
/*      */         }
/*      */         else {
/* 1233 */           if ((paramCompilerState.flags & 0x2) != 0)
/* 1234 */             arrayOfByte[(paramInt - 1)] = 35;
/*      */           else
/* 1236 */             arrayOfByte[(paramInt - 1)] = 28;
/* 1237 */           paramInt = addIndex(arrayOfByte, paramInt, paramRENode.chr);
/*      */         }
/*      */ 
/* 1240 */         break;
/*      */       case 10:
/* 1242 */         paramInt = addIndex(arrayOfByte, paramInt, paramRENode.parenIndex);
/* 1243 */         paramInt = emitREBytecode(paramCompilerState, paramRECompiled, paramInt, paramRENode.kid);
/* 1244 */         arrayOfByte[(paramInt++)] = 11;
/* 1245 */         paramInt = addIndex(arrayOfByte, paramInt, paramRENode.parenIndex);
/* 1246 */         break;
/*      */       case 20:
/* 1248 */         paramInt = addIndex(arrayOfByte, paramInt, paramRENode.parenIndex);
/* 1249 */         break;
/*      */       case 41:
/* 1251 */         j = paramInt;
/* 1252 */         paramInt += 2;
/* 1253 */         paramInt = emitREBytecode(paramCompilerState, paramRECompiled, paramInt, paramRENode.kid);
/* 1254 */         arrayOfByte[(paramInt++)] = 43;
/* 1255 */         resolveForwardJump(arrayOfByte, j, paramInt);
/* 1256 */         break;
/*      */       case 42:
/* 1258 */         j = paramInt;
/* 1259 */         paramInt += 2;
/* 1260 */         paramInt = emitREBytecode(paramCompilerState, paramRECompiled, paramInt, paramRENode.kid);
/* 1261 */         arrayOfByte[(paramInt++)] = 44;
/* 1262 */         resolveForwardJump(arrayOfByte, j, paramInt);
/* 1263 */         break;
/*      */       case 6:
/* 1265 */         if ((paramRENode.min == 0) && (paramRENode.max == -1)) {
/* 1266 */           arrayOfByte[(paramInt - 1)] = (paramRENode.greedy ? 7 : 45);
/*      */         }
/* 1268 */         else if ((paramRENode.min == 0) && (paramRENode.max == 1)) {
/* 1269 */           arrayOfByte[(paramInt - 1)] = (paramRENode.greedy ? 9 : 47);
/*      */         }
/* 1271 */         else if ((paramRENode.min == 1) && (paramRENode.max == -1)) {
/* 1272 */           arrayOfByte[(paramInt - 1)] = (paramRENode.greedy ? 8 : 46);
/*      */         } else {
/* 1274 */           if (!paramRENode.greedy) arrayOfByte[(paramInt - 1)] = 48;
/* 1275 */           paramInt = addIndex(arrayOfByte, paramInt, paramRENode.min);
/*      */ 
/* 1277 */           paramInt = addIndex(arrayOfByte, paramInt, paramRENode.max + 1);
/*      */         }
/* 1279 */         paramInt = addIndex(arrayOfByte, paramInt, paramRENode.parenCount);
/* 1280 */         paramInt = addIndex(arrayOfByte, paramInt, paramRENode.parenIndex);
/* 1281 */         j = paramInt;
/* 1282 */         paramInt += 2;
/* 1283 */         paramInt = emitREBytecode(paramCompilerState, paramRECompiled, paramInt, paramRENode.kid);
/* 1284 */         arrayOfByte[(paramInt++)] = 49;
/* 1285 */         resolveForwardJump(arrayOfByte, j, paramInt);
/* 1286 */         break;
/*      */       case 50:
/* 1288 */         paramInt = addIndex(arrayOfByte, paramInt, paramRENode.index);
/* 1289 */         paramRECompiled.classList[paramRENode.index] = new RECharSet(paramRENode.bmsize, paramRENode.startIndex, paramRENode.kidlen);
/*      */ 
/* 1291 */         break;
/*      */       }
/*      */ 
/* 1295 */       paramRENode = paramRENode.next;
/*      */     }
/* 1297 */     return paramInt;
/*      */   }
/*      */ 
/*      */   private static void pushProgState(REGlobalData paramREGlobalData, int paramInt1, int paramInt2, REBackTrackData paramREBackTrackData, int paramInt3, int paramInt4)
/*      */   {
/* 1305 */     paramREGlobalData.stateStackTop = new REProgState(paramREGlobalData.stateStackTop, paramInt1, paramInt2, paramREGlobalData.cp, paramREBackTrackData, paramInt3, paramInt4);
/*      */   }
/*      */ 
/*      */   private static REProgState popProgState(REGlobalData paramREGlobalData)
/*      */   {
/* 1314 */     REProgState localREProgState = paramREGlobalData.stateStackTop;
/* 1315 */     paramREGlobalData.stateStackTop = localREProgState.previous;
/* 1316 */     return localREProgState;
/*      */   }
/*      */ 
/*      */   private static void pushBackTrackState(REGlobalData paramREGlobalData, byte paramByte, int paramInt)
/*      */   {
/* 1322 */     paramREGlobalData.backTrackStackTop = new REBackTrackData(paramREGlobalData, paramByte, paramInt);
/*      */   }
/*      */ 
/*      */   private static boolean flatNMatcher(REGlobalData paramREGlobalData, int paramInt1, int paramInt2, char[] paramArrayOfChar, int paramInt3)
/*      */   {
/* 1332 */     if (paramREGlobalData.cp + paramInt2 > paramInt3)
/* 1333 */       return false;
/* 1334 */     for (int i = 0; i < paramInt2; i++) {
/* 1335 */       if (paramREGlobalData.regexp.source[(paramInt1 + i)] != paramArrayOfChar[(paramREGlobalData.cp + i)]) {
/* 1336 */         return false;
/*      */       }
/*      */     }
/* 1339 */     paramREGlobalData.cp += paramInt2;
/* 1340 */     return true;
/*      */   }
/*      */ 
/*      */   private static boolean flatNIMatcher(REGlobalData paramREGlobalData, int paramInt1, int paramInt2, char[] paramArrayOfChar, int paramInt3)
/*      */   {
/* 1347 */     if (paramREGlobalData.cp + paramInt2 > paramInt3)
/* 1348 */       return false;
/* 1349 */     for (int i = 0; i < paramInt2; i++) {
/* 1350 */       if (upcase(paramREGlobalData.regexp.source[(paramInt1 + i)]) != upcase(paramArrayOfChar[(paramREGlobalData.cp + i)]))
/*      */       {
/* 1353 */         return false;
/*      */       }
/*      */     }
/* 1356 */     paramREGlobalData.cp += paramInt2;
/* 1357 */     return true;
/*      */   }
/*      */ 
/*      */   private static boolean backrefMatcher(REGlobalData paramREGlobalData, int paramInt1, char[] paramArrayOfChar, int paramInt2)
/*      */   {
/* 1389 */     int k = paramREGlobalData.parens_index(paramInt1);
/* 1390 */     if (k == -1) {
/* 1391 */       return true;
/*      */     }
/* 1393 */     int i = paramREGlobalData.parens_length(paramInt1);
/* 1394 */     if (paramREGlobalData.cp + i > paramInt2) {
/* 1395 */       return false;
/*      */     }
/* 1397 */     if ((paramREGlobalData.regexp.flags & 0x2) != 0) {
/* 1398 */       for (j = 0; j < i; j++) {
/* 1399 */         if (upcase(paramArrayOfChar[(k + j)]) != upcase(paramArrayOfChar[(paramREGlobalData.cp + j)])) {
/* 1400 */           return false;
/*      */         }
/*      */       }
/*      */     }
/* 1404 */     for (int j = 0; j < i; j++) {
/* 1405 */       if (paramArrayOfChar[(k + j)] != paramArrayOfChar[(paramREGlobalData.cp + j)]) {
/* 1406 */         return false;
/*      */       }
/*      */     }
/* 1409 */     paramREGlobalData.cp += i;
/* 1410 */     return true;
/*      */   }
/*      */ 
/*      */   private static void addCharacterToCharSet(RECharSet paramRECharSet, char paramChar)
/*      */   {
/* 1418 */     int i = paramChar / '\b';
/* 1419 */     if (paramChar > paramRECharSet.length)
/* 1420 */       throw new RuntimeException();
/*      */     int tmp26_25 = i;
/*      */     byte[] tmp26_22 = paramRECharSet.bits; tmp26_22[tmp26_25] = ((byte)(tmp26_22[tmp26_25] | '\001' << (paramChar & 0x7)));
/*      */   }
/*      */ 
/*      */   private static void addCharacterRangeToCharSet(RECharSet paramRECharSet, char paramChar1, char paramChar2)
/*      */   {
/* 1431 */     int j = paramChar1 / '\b';
/* 1432 */     int k = paramChar2 / '\b';
/*      */ 
/* 1434 */     if ((paramChar2 > paramRECharSet.length) || (paramChar1 > paramChar2)) {
/* 1435 */       throw new RuntimeException();
/*      */     }
/* 1437 */     paramChar1 = (char)(paramChar1 & 0x7);
/* 1438 */     paramChar2 = (char)(paramChar2 & 0x7);
/*      */ 
/* 1440 */     if (j == k)
/*      */     {
/*      */       int tmp58_56 = j;
/*      */       byte[] tmp58_53 = paramRECharSet.bits; tmp58_53[tmp58_56] = ((byte)(tmp58_53[tmp58_56] | 255 >> 7 - (paramChar2 - paramChar1) << paramChar1));
/*      */     }
/*      */     else
/*      */     {
/*      */       int tmp84_82 = j;
/*      */       byte[] tmp84_79 = paramRECharSet.bits; tmp84_79[tmp84_82] = ((byte)(tmp84_79[tmp84_82] | 'ÿ' << paramChar1));
/* 1445 */       for (int i = j + 1; i < k; i++)
/* 1446 */         paramRECharSet.bits[i] = -1;
/*      */       int tmp124_122 = k;
/*      */       byte[] tmp124_119 = paramRECharSet.bits; tmp124_119[tmp124_122] = ((byte)(tmp124_119[tmp124_122] | 255 >> '\007' - paramChar2));
/*      */     }
/*      */   }
/*      */ 
/*      */   private static void processCharSet(REGlobalData paramREGlobalData, RECharSet paramRECharSet)
/*      */   {
/* 1455 */     synchronized (paramRECharSet) {
/* 1456 */       if (!paramRECharSet.converted) {
/* 1457 */         processCharSetImpl(paramREGlobalData, paramRECharSet);
/* 1458 */         paramRECharSet.converted = true;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private static void processCharSetImpl(REGlobalData paramREGlobalData, RECharSet paramRECharSet)
/*      */   {
/* 1467 */     int i = paramRECharSet.startIndex;
/* 1468 */     int j = i + paramRECharSet.strlength;
/*      */ 
/* 1470 */     char c1 = '\000';
/*      */ 
/* 1476 */     int i2 = 0;
/*      */ 
/* 1478 */     paramRECharSet.sense = true;
/* 1479 */     int k = paramRECharSet.length / 8 + 1;
/* 1480 */     paramRECharSet.bits = new byte[k];
/*      */ 
/* 1482 */     if (i == j) {
/* 1483 */       return;
/*      */     }
/* 1485 */     if (paramREGlobalData.regexp.source[i] == '^') {
/* 1486 */       paramRECharSet.sense = false;
/* 1487 */       i++;
/*      */     }
/*      */ 
/* 1490 */     while (i != j) {
/* 1491 */       int n = 2;
/*      */       char c2;
/* 1492 */       switch (paramREGlobalData.regexp.source[i]) {
/*      */       case '\\':
/* 1494 */         i++;
/* 1495 */         char c3 = paramREGlobalData.regexp.source[(i++)];
/*      */         int m;
/*      */         int i1;
/* 1496 */         switch (c3) {
/*      */         case 'b':
/* 1498 */           c2 = '\b';
/* 1499 */           break;
/*      */         case 'f':
/* 1501 */           c2 = '\f';
/* 1502 */           break;
/*      */         case 'n':
/* 1504 */           c2 = '\n';
/* 1505 */           break;
/*      */         case 'r':
/* 1507 */           c2 = '\r';
/* 1508 */           break;
/*      */         case 't':
/* 1510 */           c2 = '\t';
/* 1511 */           break;
/*      */         case 'v':
/* 1513 */           c2 = '\013';
/* 1514 */           break;
/*      */         case 'c':
/* 1516 */           if ((i + 1 < j) && (isWord(paramREGlobalData.regexp.source[(i + 1)]))) {
/* 1517 */             c2 = (char)(paramREGlobalData.regexp.source[(i++)] & 0x1F);
/*      */           } else {
/* 1519 */             i--;
/* 1520 */             c2 = '\\';
/*      */           }
/* 1522 */           break;
/*      */         case 'u':
/* 1524 */           n += 2;
/*      */         case 'x':
/* 1527 */           m = 0;
/* 1528 */           for (i1 = 0; (i1 < n) && (i < j); i1++) {
/* 1529 */             c3 = paramREGlobalData.regexp.source[(i++)];
/* 1530 */             int i3 = toASCIIHexDigit(c3);
/* 1531 */             if (i3 < 0)
/*      */             {
/* 1535 */               i -= i1 + 1;
/* 1536 */               m = 92;
/* 1537 */               break;
/*      */             }
/* 1539 */             m = m << 4 | i3;
/*      */           }
/* 1541 */           c2 = (char)m;
/* 1542 */           break;
/*      */         case '0':
/*      */         case '1':
/*      */         case '2':
/*      */         case '3':
/*      */         case '4':
/*      */         case '5':
/*      */         case '6':
/*      */         case '7':
/* 1557 */           m = c3 - '0';
/* 1558 */           c3 = paramREGlobalData.regexp.source[i];
/* 1559 */           if (('0' <= c3) && (c3 <= '7')) {
/* 1560 */             i++;
/* 1561 */             m = 8 * m + (c3 - '0');
/* 1562 */             c3 = paramREGlobalData.regexp.source[i];
/* 1563 */             if (('0' <= c3) && (c3 <= '7')) {
/* 1564 */               i++;
/* 1565 */               i1 = 8 * m + (c3 - '0');
/* 1566 */               if (i1 <= 255)
/* 1567 */                 m = i1;
/*      */               else
/* 1569 */                 i--;
/*      */             }
/*      */           }
/* 1572 */           c2 = (char)m;
/* 1573 */           break;
/*      */         case 'd':
/* 1576 */           addCharacterRangeToCharSet(paramRECharSet, '0', '9');
/* 1577 */           break;
/*      */         case 'D':
/* 1579 */           addCharacterRangeToCharSet(paramRECharSet, '\000', '/');
/* 1580 */           addCharacterRangeToCharSet(paramRECharSet, ':', (char)paramRECharSet.length);
/*      */ 
/* 1582 */           break;
/*      */         case 's':
/* 1584 */           for (i1 = paramRECharSet.length; i1 >= 0; i1--) {
/* 1585 */             if (isREWhiteSpace(i1))
/* 1586 */               addCharacterToCharSet(paramRECharSet, (char)i1);
/*      */           }
/*      */         case 'S':
/* 1589 */           for (i1 = paramRECharSet.length; i1 >= 0; i1--) {
/* 1590 */             if (!isREWhiteSpace(i1))
/* 1591 */               addCharacterToCharSet(paramRECharSet, (char)i1);
/*      */           }
/*      */         case 'w':
/* 1594 */           for (i1 = paramRECharSet.length; i1 >= 0; i1--) {
/* 1595 */             if (isWord((char)i1))
/* 1596 */               addCharacterToCharSet(paramRECharSet, (char)i1);
/*      */           }
/*      */         case 'W':
/* 1599 */           for (i1 = paramRECharSet.length; i1 >= 0; i1--)
/* 1600 */             if (!isWord((char)i1))
/* 1601 */               addCharacterToCharSet(paramRECharSet, (char)i1);  case '8':
/*      */         case '9':
/*      */         case ':':
/*      */         case ';':
/*      */         case '<':
/*      */         case '=':
/*      */         case '>':
/*      */         case '?':
/*      */         case '@':
/*      */         case 'A':
/*      */         case 'B':
/*      */         case 'C':
/*      */         case 'E':
/*      */         case 'F':
/*      */         case 'G':
/*      */         case 'H':
/*      */         case 'I':
/*      */         case 'J':
/*      */         case 'K':
/*      */         case 'L':
/*      */         case 'M':
/*      */         case 'N':
/*      */         case 'O':
/*      */         case 'P':
/*      */         case 'Q':
/*      */         case 'R':
/*      */         case 'T':
/*      */         case 'U':
/*      */         case 'V':
/*      */         case 'X':
/*      */         case 'Y':
/*      */         case 'Z':
/*      */         case '[':
/*      */         case '\\':
/*      */         case ']':
/*      */         case '^':
/*      */         case '_':
/*      */         case '`':
/*      */         case 'a':
/*      */         case 'e':
/*      */         case 'g':
/*      */         case 'h':
/*      */         case 'i':
/*      */         case 'j':
/*      */         case 'k':
/*      */         case 'l':
/*      */         case 'm':
/*      */         case 'o':
/*      */         case 'p':
/*      */         case 'q':
/*      */         default:
/* 1604 */           c2 = c3;
/* 1605 */         }break;
/*      */       default:
/* 1611 */         c2 = paramREGlobalData.regexp.source[(i++)];
/*      */ 
/* 1615 */         if (i2 != 0) {
/* 1616 */           if ((paramREGlobalData.regexp.flags & 0x2) != 0) {
/* 1617 */             addCharacterRangeToCharSet(paramRECharSet, upcase(c1), upcase(c2));
/*      */ 
/* 1620 */             addCharacterRangeToCharSet(paramRECharSet, downcase(c1), downcase(c2));
/*      */           }
/*      */           else
/*      */           {
/* 1624 */             addCharacterRangeToCharSet(paramRECharSet, c1, c2);
/*      */           }
/* 1626 */           i2 = 0;
/*      */         }
/*      */         else {
/* 1629 */           if ((paramREGlobalData.regexp.flags & 0x2) != 0) {
/* 1630 */             addCharacterToCharSet(paramRECharSet, upcase(c2));
/* 1631 */             addCharacterToCharSet(paramRECharSet, downcase(c2));
/*      */           } else {
/* 1633 */             addCharacterToCharSet(paramRECharSet, c2);
/*      */           }
/* 1635 */           if ((i < j - 1) && 
/* 1636 */             (paramREGlobalData.regexp.source[i] == '-')) {
/* 1637 */             i++;
/* 1638 */             i2 = 1;
/* 1639 */             c1 = c2;
/*      */           }
/*      */         }
/*      */         break;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private static boolean classMatcher(REGlobalData paramREGlobalData, RECharSet paramRECharSet, char paramChar)
/*      */   {
/* 1654 */     if (!paramRECharSet.converted) {
/* 1655 */       processCharSet(paramREGlobalData, paramRECharSet);
/*      */     }
/*      */ 
/* 1658 */     int i = paramChar / '\b';
/* 1659 */     if (paramRECharSet.sense) {
/* 1660 */       if ((paramRECharSet.length == 0) || (paramChar > paramRECharSet.length) || ((paramRECharSet.bits[i] & '\001' << (paramChar & 0x7)) == 0))
/*      */       {
/* 1663 */         return false;
/*      */       }
/* 1665 */     } else if ((paramRECharSet.length != 0) && (paramChar <= paramRECharSet.length) && ((paramRECharSet.bits[i] & '\001' << (paramChar & 0x7)) != 0))
/*      */     {
/* 1668 */       return false;
/*      */     }
/* 1670 */     return true;
/*      */   }
/*      */ 
/*      */   private static boolean executeREBytecode(REGlobalData paramREGlobalData, char[] paramArrayOfChar, int paramInt)
/*      */   {
/* 1676 */     int i = 0;
/* 1677 */     byte[] arrayOfByte = paramREGlobalData.regexp.program;
/*      */ 
/* 1680 */     boolean bool = false;
/*      */ 
/* 1682 */     int k = 0;
/* 1683 */     int j = 53;
/*      */ 
/* 1687 */     int m = arrayOfByte[(i++)];
/*      */     while (true)
/*      */     {
/*      */       int n;
/*      */       int i3;
/*      */       char c;
/*      */       int i1;
/*      */       int i4;
/*      */       int i5;
/*      */       Object localObject;
/*      */       int i6;
/*      */       int i7;
/*      */       int i8;
/* 1692 */       switch (m) {
/*      */       case 0:
/* 1694 */         bool = true;
/* 1695 */         break;
/*      */       case 2:
/* 1697 */         if (paramREGlobalData.cp != 0) {
/* 1698 */           if ((paramREGlobalData.multiline) || ((paramREGlobalData.regexp.flags & 0x4) != 0))
/*      */           {
/* 1700 */             if (!isLineTerm(paramArrayOfChar[(paramREGlobalData.cp - 1)])) {
/* 1701 */               bool = false;
/* 1702 */               break label2440;
/*      */             }
/*      */           }
/*      */           else {
/* 1706 */             bool = false;
/* 1707 */             break label2440;
/*      */           }
/*      */         }
/* 1710 */         bool = true;
/* 1711 */         break;
/*      */       case 3:
/* 1713 */         if (paramREGlobalData.cp != paramInt) {
/* 1714 */           if ((paramREGlobalData.multiline) || ((paramREGlobalData.regexp.flags & 0x4) != 0))
/*      */           {
/* 1716 */             if (!isLineTerm(paramArrayOfChar[paramREGlobalData.cp])) {
/* 1717 */               bool = false;
/* 1718 */               break label2440;
/*      */             }
/*      */           }
/*      */           else {
/* 1722 */             bool = false;
/* 1723 */             break label2440;
/*      */           }
/*      */         }
/* 1726 */         bool = true;
/* 1727 */         break;
/*      */       case 4:
/* 1729 */         bool = ((paramREGlobalData.cp == 0) || (!isWord(paramArrayOfChar[(paramREGlobalData.cp - 1)])) ? 1 : 0) ^ ((paramREGlobalData.cp >= paramInt) || (!isWord(paramArrayOfChar[paramREGlobalData.cp])) ? 1 : 0);
/*      */ 
/* 1731 */         break;
/*      */       case 5:
/* 1733 */         bool = ((paramREGlobalData.cp == 0) || (!isWord(paramArrayOfChar[(paramREGlobalData.cp - 1)])) ? 1 : 0) ^ ((paramREGlobalData.cp < paramInt) && (isWord(paramArrayOfChar[paramREGlobalData.cp])) ? 1 : 0);
/*      */ 
/* 1735 */         break;
/*      */       case 12:
/* 1737 */         bool = (paramREGlobalData.cp != paramInt) && (!isLineTerm(paramArrayOfChar[paramREGlobalData.cp]));
/* 1738 */         if (bool)
/* 1739 */           paramREGlobalData.cp += 1; break;
/*      */       case 14:
/* 1743 */         bool = (paramREGlobalData.cp != paramInt) && (isDigit(paramArrayOfChar[paramREGlobalData.cp]));
/* 1744 */         if (bool)
/* 1745 */           paramREGlobalData.cp += 1; break;
/*      */       case 15:
/* 1749 */         bool = (paramREGlobalData.cp != paramInt) && (!isDigit(paramArrayOfChar[paramREGlobalData.cp]));
/* 1750 */         if (bool)
/* 1751 */           paramREGlobalData.cp += 1; break;
/*      */       case 18:
/* 1755 */         bool = (paramREGlobalData.cp != paramInt) && (isREWhiteSpace(paramArrayOfChar[paramREGlobalData.cp]));
/* 1756 */         if (bool)
/* 1757 */           paramREGlobalData.cp += 1; break;
/*      */       case 19:
/* 1761 */         bool = (paramREGlobalData.cp != paramInt) && (!isREWhiteSpace(paramArrayOfChar[paramREGlobalData.cp]));
/* 1762 */         if (bool)
/* 1763 */           paramREGlobalData.cp += 1; break;
/*      */       case 16:
/* 1767 */         bool = (paramREGlobalData.cp != paramInt) && (isWord(paramArrayOfChar[paramREGlobalData.cp]));
/* 1768 */         if (bool)
/* 1769 */           paramREGlobalData.cp += 1; break;
/*      */       case 17:
/* 1773 */         bool = (paramREGlobalData.cp != paramInt) && (!isWord(paramArrayOfChar[paramREGlobalData.cp]));
/* 1774 */         if (bool)
/* 1775 */           paramREGlobalData.cp += 1; break;
/*      */       case 21:
/* 1780 */         n = getIndex(arrayOfByte, i);
/* 1781 */         i += 2;
/* 1782 */         i3 = getIndex(arrayOfByte, i);
/* 1783 */         i += 2;
/* 1784 */         bool = flatNMatcher(paramREGlobalData, n, i3, paramArrayOfChar, paramInt);
/*      */ 
/* 1786 */         break;
/*      */       case 32:
/* 1789 */         n = getIndex(arrayOfByte, i);
/* 1790 */         i += 2;
/* 1791 */         i3 = getIndex(arrayOfByte, i);
/* 1792 */         i += 2;
/* 1793 */         bool = flatNIMatcher(paramREGlobalData, n, i3, paramArrayOfChar, paramInt);
/*      */ 
/* 1795 */         break;
/*      */       case 22:
/* 1798 */         n = (char)(arrayOfByte[(i++)] & 0xFF);
/* 1799 */         bool = (paramREGlobalData.cp != paramInt) && (paramArrayOfChar[paramREGlobalData.cp] == n);
/* 1800 */         if (bool) {
/* 1801 */           paramREGlobalData.cp += 1;
/*      */         }
/*      */ 
/* 1804 */         break;
/*      */       case 33:
/* 1807 */         n = (char)(arrayOfByte[(i++)] & 0xFF);
/* 1808 */         bool = (paramREGlobalData.cp != paramInt) && (upcase(paramArrayOfChar[paramREGlobalData.cp]) == upcase(n));
/*      */ 
/* 1810 */         if (bool) {
/* 1811 */           paramREGlobalData.cp += 1;
/*      */         }
/*      */ 
/* 1814 */         break;
/*      */       case 28:
/* 1817 */         c = (char)getIndex(arrayOfByte, i);
/* 1818 */         i += 2;
/* 1819 */         bool = (paramREGlobalData.cp != paramInt) && (paramArrayOfChar[paramREGlobalData.cp] == c);
/* 1820 */         if (bool) {
/* 1821 */           paramREGlobalData.cp += 1;
/*      */         }
/*      */ 
/* 1824 */         break;
/*      */       case 35:
/* 1827 */         c = (char)getIndex(arrayOfByte, i);
/* 1828 */         i += 2;
/* 1829 */         bool = (paramREGlobalData.cp != paramInt) && (upcase(paramArrayOfChar[paramREGlobalData.cp]) == upcase(c));
/*      */ 
/* 1831 */         if (bool) {
/* 1832 */           paramREGlobalData.cp += 1;
/*      */         }
/*      */ 
/* 1835 */         break;
/*      */       case 1:
/* 1840 */         pushProgState(paramREGlobalData, 0, 0, null, k, j);
/*      */ 
/* 1843 */         i1 = i + getOffset(arrayOfByte, i);
/* 1844 */         i3 = arrayOfByte[(i1++)];
/* 1845 */         pushBackTrackState(paramREGlobalData, i3, i1);
/* 1846 */         i += 2;
/* 1847 */         m = arrayOfByte[(i++)];
/*      */ 
/* 1849 */         break;
/*      */       case 23:
/* 1854 */         REProgState localREProgState2 = popProgState(paramREGlobalData);
/* 1855 */         k = localREProgState2.continuation_pc;
/* 1856 */         j = localREProgState2.continuation_op;
/* 1857 */         i1 = getOffset(arrayOfByte, i);
/* 1858 */         i += i1;
/* 1859 */         m = arrayOfByte[(i++)];
/*      */ 
/* 1861 */         break;
/*      */       case 10:
/* 1866 */         i1 = getIndex(arrayOfByte, i);
/* 1867 */         i += 2;
/* 1868 */         paramREGlobalData.set_parens(i1, paramREGlobalData.cp, 0);
/* 1869 */         m = arrayOfByte[(i++)];
/*      */ 
/* 1871 */         break;
/*      */       case 11:
/* 1875 */         i4 = getIndex(arrayOfByte, i);
/* 1876 */         i += 2;
/* 1877 */         i1 = paramREGlobalData.parens_index(i4);
/* 1878 */         paramREGlobalData.set_parens(i4, i1, paramREGlobalData.cp - i1);
/*      */ 
/* 1880 */         if (i4 > paramREGlobalData.lastParen)
/* 1881 */           paramREGlobalData.lastParen = i4;
/* 1882 */         m = arrayOfByte[(i++)];
/*      */ 
/* 1884 */         break;
/*      */       case 20:
/* 1887 */         i1 = getIndex(arrayOfByte, i);
/* 1888 */         i += 2;
/* 1889 */         bool = backrefMatcher(paramREGlobalData, i1, paramArrayOfChar, paramInt);
/*      */ 
/* 1891 */         break;
/*      */       case 50:
/* 1895 */         i1 = getIndex(arrayOfByte, i);
/* 1896 */         i += 2;
/* 1897 */         if ((paramREGlobalData.cp != paramInt) && 
/* 1898 */           (classMatcher(paramREGlobalData, paramREGlobalData.regexp.classList[i1], paramArrayOfChar[paramREGlobalData.cp])))
/*      */         {
/* 1901 */           paramREGlobalData.cp += 1;
/* 1902 */           bool = true;
/*      */         }
/*      */         else
/*      */         {
/* 1906 */           bool = false;
/*      */         }
/* 1908 */         break;
/*      */       case 41:
/*      */       case 42:
/* 1914 */         pushProgState(paramREGlobalData, 0, 0, paramREGlobalData.backTrackStackTop, k, j);
/*      */ 
/* 1917 */         if (m == 41)
/* 1918 */           i1 = 43;
/*      */         else {
/* 1920 */           i1 = 44;
/*      */         }
/* 1922 */         pushBackTrackState(paramREGlobalData, i1, i + getOffset(arrayOfByte, i));
/*      */ 
/* 1924 */         i += 2;
/* 1925 */         m = arrayOfByte[(i++)];
/*      */ 
/* 1927 */         break;
/*      */       case 43:
/*      */       case 44:
/* 1932 */         REProgState localREProgState1 = popProgState(paramREGlobalData);
/* 1933 */         paramREGlobalData.cp = localREProgState1.index;
/* 1934 */         paramREGlobalData.backTrackStackTop = localREProgState1.backTrack;
/* 1935 */         k = localREProgState1.continuation_pc;
/* 1936 */         j = localREProgState1.continuation_op;
/* 1937 */         if (bool) {
/* 1938 */           if (m == 43)
/* 1939 */             bool = true;
/*      */           else {
/* 1941 */             bool = false;
/*      */           }
/*      */         }
/* 1944 */         else if (m != 43)
/*      */         {
/* 1947 */           bool = true;
/*      */         }
/*      */ 
/* 1951 */         break;
/*      */       case 6:
/*      */       case 7:
/*      */       case 8:
/*      */       case 9:
/*      */       case 45:
/*      */       case 46:
/*      */       case 47:
/*      */       case 48:
/* 1963 */         i5 = 0;
/*      */         int i2;
/* 1964 */         switch (m) {
/*      */         case 7:
/* 1966 */           i5 = 1;
/*      */         case 45:
/* 1969 */           i2 = 0;
/* 1970 */           i4 = -1;
/* 1971 */           break;
/*      */         case 8:
/* 1973 */           i5 = 1;
/*      */         case 46:
/* 1976 */           i2 = 1;
/* 1977 */           i4 = -1;
/* 1978 */           break;
/*      */         case 9:
/* 1980 */           i5 = 1;
/*      */         case 47:
/* 1983 */           i2 = 0;
/* 1984 */           i4 = 1;
/* 1985 */           break;
/*      */         case 6:
/* 1987 */           i5 = 1;
/*      */         case 48:
/* 1990 */           i2 = getOffset(arrayOfByte, i);
/* 1991 */           i += 2;
/*      */ 
/* 1993 */           i4 = getOffset(arrayOfByte, i) - 1;
/* 1994 */           i += 2;
/* 1995 */           break;
/*      */         default:
/* 1997 */           throw Kit.codeBug();
/*      */         }
/* 1999 */         pushProgState(paramREGlobalData, i2, i4, null, k, j);
/*      */ 
/* 2002 */         if (i5 != 0) {
/* 2003 */           j = 51;
/* 2004 */           k = i;
/* 2005 */           pushBackTrackState(paramREGlobalData, (byte)51, i);
/*      */ 
/* 2007 */           i += 6;
/* 2008 */           m = arrayOfByte[(i++)];
/*      */         }
/* 2010 */         else if (i2 != 0) {
/* 2011 */           j = 52;
/* 2012 */           k = i;
/*      */ 
/* 2014 */           i += 6;
/* 2015 */           m = arrayOfByte[(i++)];
/*      */         } else {
/* 2017 */           pushBackTrackState(paramREGlobalData, (byte)52, i);
/* 2018 */           popProgState(paramREGlobalData);
/* 2019 */           i += 4;
/* 2020 */           i += getOffset(arrayOfByte, i);
/* 2021 */           m = arrayOfByte[(i++)];
/*      */         }
/*      */ 
/* 2025 */         break;
/*      */       case 49:
/* 2029 */         i = k;
/* 2030 */         m = j;
/* 2031 */         break;
/*      */       case 51:
/* 2035 */         localObject = popProgState(paramREGlobalData);
/* 2036 */         if (!bool)
/*      */         {
/* 2041 */           if (((REProgState)localObject).min == 0)
/* 2042 */             bool = true;
/* 2043 */           k = ((REProgState)localObject).continuation_pc;
/* 2044 */           j = ((REProgState)localObject).continuation_op;
/* 2045 */           i += 4;
/* 2046 */           i += getOffset(arrayOfByte, i);
/*      */         }
/* 2050 */         else if ((((REProgState)localObject).min == 0) && (paramREGlobalData.cp == ((REProgState)localObject).index))
/*      */         {
/* 2052 */           bool = false;
/* 2053 */           k = ((REProgState)localObject).continuation_pc;
/* 2054 */           j = ((REProgState)localObject).continuation_op;
/* 2055 */           i += 4;
/* 2056 */           i += getOffset(arrayOfByte, i);
/*      */         }
/*      */         else {
/* 2059 */           i4 = ((REProgState)localObject).min; i5 = ((REProgState)localObject).max;
/* 2060 */           if (i4 != 0) i4--;
/* 2061 */           if (i5 != -1) i5--;
/* 2062 */           if (i5 == 0) {
/* 2063 */             bool = true;
/* 2064 */             k = ((REProgState)localObject).continuation_pc;
/* 2065 */             j = ((REProgState)localObject).continuation_op;
/* 2066 */             i += 4;
/* 2067 */             i += getOffset(arrayOfByte, i);
/*      */           }
/*      */           else {
/* 2070 */             pushProgState(paramREGlobalData, i4, i5, null, ((REProgState)localObject).continuation_pc, ((REProgState)localObject).continuation_op);
/*      */ 
/* 2073 */             j = 51;
/* 2074 */             k = i;
/* 2075 */             pushBackTrackState(paramREGlobalData, (byte)51, i);
/* 2076 */             i6 = getIndex(arrayOfByte, i);
/* 2077 */             i += 2;
/* 2078 */             i7 = getIndex(arrayOfByte, i);
/* 2079 */             i += 4;
/* 2080 */             m = arrayOfByte[(i++)];
/* 2081 */             for (i8 = 0; i8 < i6; i8++) {
/* 2082 */               paramREGlobalData.set_parens(i7 + i8, -1, 0);
/*      */             }
/*      */           }
/*      */         }
/* 2086 */         break;
/*      */       case 52:
/* 2090 */         localObject = popProgState(paramREGlobalData);
/* 2091 */         if (!bool)
/*      */         {
/* 2095 */           if ((((REProgState)localObject).max == -1) || (((REProgState)localObject).max > 0)) {
/* 2096 */             pushProgState(paramREGlobalData, ((REProgState)localObject).min, ((REProgState)localObject).max, null, ((REProgState)localObject).continuation_pc, ((REProgState)localObject).continuation_op);
/*      */ 
/* 2099 */             j = 52;
/* 2100 */             k = i;
/* 2101 */             i4 = getIndex(arrayOfByte, i);
/* 2102 */             i += 2;
/* 2103 */             i5 = getIndex(arrayOfByte, i);
/* 2104 */             i += 4;
/* 2105 */             for (i6 = 0; i6 < i4; i6++) {
/* 2106 */               paramREGlobalData.set_parens(i5 + i6, -1, 0);
/*      */             }
/* 2108 */             m = arrayOfByte[(i++)];
/*      */           }
/*      */           else
/*      */           {
/* 2112 */             k = ((REProgState)localObject).continuation_pc;
/* 2113 */             j = ((REProgState)localObject).continuation_op;
/*      */           }
/*      */ 
/*      */         }
/* 2117 */         else if ((((REProgState)localObject).min == 0) && (paramREGlobalData.cp == ((REProgState)localObject).index))
/*      */         {
/* 2119 */           bool = false;
/* 2120 */           k = ((REProgState)localObject).continuation_pc;
/* 2121 */           j = ((REProgState)localObject).continuation_op;
/*      */         }
/*      */         else {
/* 2124 */           i4 = ((REProgState)localObject).min; i5 = ((REProgState)localObject).max;
/* 2125 */           if (i4 != 0) i4--;
/* 2126 */           if (i5 != -1) i5--;
/* 2127 */           pushProgState(paramREGlobalData, i4, i5, null, ((REProgState)localObject).continuation_pc, ((REProgState)localObject).continuation_op);
/*      */ 
/* 2130 */           if (i4 != 0) {
/* 2131 */             j = 52;
/* 2132 */             k = i;
/* 2133 */             i6 = getIndex(arrayOfByte, i);
/* 2134 */             i += 2;
/* 2135 */             i7 = getIndex(arrayOfByte, i);
/* 2136 */             i += 4;
/* 2137 */             for (i8 = 0; i8 < i6; i8++) {
/* 2138 */               paramREGlobalData.set_parens(i7 + i8, -1, 0);
/*      */             }
/* 2140 */             m = arrayOfByte[(i++)];
/* 2141 */             continue;
/* 2142 */           }k = ((REProgState)localObject).continuation_pc;
/* 2143 */           j = ((REProgState)localObject).continuation_op;
/* 2144 */           pushBackTrackState(paramREGlobalData, (byte)52, i);
/* 2145 */           popProgState(paramREGlobalData);
/* 2146 */           i += 4;
/* 2147 */           i += getOffset(arrayOfByte, i);
/* 2148 */           m = arrayOfByte[(i++)];
/*      */         }
/* 2150 */         break;
/*      */       case 53:
/* 2155 */         return true;
/*      */       case 13:
/*      */       case 24:
/*      */       case 25:
/*      */       case 26:
/*      */       case 27:
/*      */       case 29:
/*      */       case 30:
/*      */       case 31:
/*      */       case 34:
/*      */       case 36:
/*      */       case 37:
/*      */       case 38:
/*      */       case 39:
/*      */       case 40:
/*      */       default:
/* 2158 */         throw Kit.codeBug();
/*      */ 
/* 2165 */         label2440: if (!bool) {
/* 2166 */           localObject = paramREGlobalData.backTrackStackTop;
/* 2167 */           if (localObject != null) {
/* 2168 */             paramREGlobalData.backTrackStackTop = ((REBackTrackData)localObject).previous;
/*      */ 
/* 2170 */             paramREGlobalData.lastParen = ((REBackTrackData)localObject).lastParen;
/*      */ 
/* 2174 */             if (((REBackTrackData)localObject).parens != null) {
/* 2175 */               paramREGlobalData.parens = ((long[])((REBackTrackData)localObject).parens.clone());
/*      */             }
/*      */ 
/* 2178 */             paramREGlobalData.cp = ((REBackTrackData)localObject).cp;
/*      */ 
/* 2180 */             paramREGlobalData.stateStackTop = ((REBackTrackData)localObject).stateStackTop;
/*      */ 
/* 2182 */             j = paramREGlobalData.stateStackTop.continuation_op;
/*      */ 
/* 2184 */             k = paramREGlobalData.stateStackTop.continuation_pc;
/*      */ 
/* 2186 */             i = ((REBackTrackData)localObject).continuation_pc;
/* 2187 */             m = ((REBackTrackData)localObject).continuation_op;
/*      */           }
/*      */           else
/*      */           {
/* 2191 */             return false;
/*      */           }
/*      */         } else {
/* 2194 */           m = arrayOfByte[(i++)];
/*      */         }
/*      */         break;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private static boolean matchRegExp(REGlobalData paramREGlobalData, RECompiled paramRECompiled, char[] paramArrayOfChar, int paramInt1, int paramInt2, boolean paramBoolean)
/*      */   {
/* 2203 */     if (paramRECompiled.parenCount != 0)
/* 2204 */       paramREGlobalData.parens = new long[paramRECompiled.parenCount];
/*      */     else {
/* 2206 */       paramREGlobalData.parens = null;
/*      */     }
/*      */ 
/* 2209 */     paramREGlobalData.backTrackStackTop = null;
/*      */ 
/* 2211 */     paramREGlobalData.stateStackTop = null;
/*      */ 
/* 2213 */     paramREGlobalData.multiline = paramBoolean;
/* 2214 */     paramREGlobalData.regexp = paramRECompiled;
/* 2215 */     paramREGlobalData.lastParen = 0;
/*      */ 
/* 2217 */     int i = paramREGlobalData.regexp.anchorCh;
/*      */ 
/* 2222 */     for (int j = paramInt1; j <= paramInt2; j++)
/*      */     {
/* 2228 */       if (i >= 0) {
/*      */         while (true) {
/* 2230 */           if (j == paramInt2) {
/* 2231 */             return false;
/*      */           }
/* 2233 */           int k = paramArrayOfChar[j];
/* 2234 */           if ((k == i) || (((paramREGlobalData.regexp.flags & 0x2) != 0) && (upcase(k) == upcase((char)i))))
/*      */           {
/*      */             break;
/*      */           }
/*      */ 
/* 2240 */           j++;
/*      */         }
/*      */       }
/* 2243 */       paramREGlobalData.cp = j;
/* 2244 */       for (int m = 0; m < paramRECompiled.parenCount; m++) {
/* 2245 */         paramREGlobalData.set_parens(m, -1, 0);
/*      */       }
/* 2247 */       boolean bool = executeREBytecode(paramREGlobalData, paramArrayOfChar, paramInt2);
/*      */ 
/* 2249 */       paramREGlobalData.backTrackStackTop = null;
/* 2250 */       paramREGlobalData.stateStackTop = null;
/* 2251 */       if (bool) {
/* 2252 */         paramREGlobalData.skipped = (j - paramInt1);
/* 2253 */         return true;
/*      */       }
/*      */     }
/* 2256 */     return false;
/*      */   }
/*      */ 
/*      */   Object executeRegExp(Context paramContext, Scriptable paramScriptable, RegExpImpl paramRegExpImpl, String paramString, int[] paramArrayOfInt, int paramInt)
/*      */   {
/* 2265 */     REGlobalData localREGlobalData = new REGlobalData();
/*      */ 
/* 2267 */     int i = paramArrayOfInt[0];
/* 2268 */     char[] arrayOfChar = paramString.toCharArray();
/* 2269 */     int j = arrayOfChar.length;
/* 2270 */     if (i > j) {
/* 2271 */       i = j;
/*      */     }
/*      */ 
/* 2275 */     boolean bool = matchRegExp(localREGlobalData, this.re, arrayOfChar, i, j, paramRegExpImpl.multiline);
/*      */ 
/* 2277 */     if (!bool) {
/* 2278 */       if (paramInt != 2) return null;
/* 2279 */       return Undefined.instance;
/*      */     }
/* 2281 */     int k = localREGlobalData.cp;
/* 2282 */     int m = k;
/* 2283 */     paramArrayOfInt[0] = m;
/* 2284 */     int n = m - (i + localREGlobalData.skipped);
/* 2285 */     int i1 = k;
/* 2286 */     k -= n;
/*      */     Object localObject1;
/*      */     Scriptable localScriptable;
/*      */     Object localObject2;
/* 2290 */     if (paramInt == 0)
/*      */     {
/* 2295 */       localObject1 = Boolean.TRUE;
/* 2296 */       localScriptable = null;
/*      */     }
/*      */     else
/*      */     {
/* 2305 */       localObject2 = getTopLevelScope(paramScriptable);
/* 2306 */       localObject1 = ScriptRuntime.newObject(paramContext, (Scriptable)localObject2, "Array", null);
/* 2307 */       localScriptable = (Scriptable)localObject1;
/*      */ 
/* 2309 */       String str1 = new String(arrayOfChar, k, n);
/* 2310 */       localScriptable.put(0, localScriptable, str1);
/*      */     }
/*      */ 
/* 2313 */     if (this.re.parenCount == 0) {
/* 2314 */       paramRegExpImpl.parens = null;
/* 2315 */       paramRegExpImpl.lastParen = SubString.emptySubString;
/*      */     } else {
/* 2317 */       localObject2 = null;
/*      */ 
/* 2319 */       paramRegExpImpl.parens = new SubString[this.re.parenCount];
/* 2320 */       for (int i2 = 0; i2 < this.re.parenCount; i2++) {
/* 2321 */         int i3 = localREGlobalData.parens_index(i2);
/*      */ 
/* 2323 */         if (i3 != -1) {
/* 2324 */           int i4 = localREGlobalData.parens_length(i2);
/* 2325 */           localObject2 = new SubString(arrayOfChar, i3, i4);
/* 2326 */           paramRegExpImpl.parens[i2] = localObject2;
/* 2327 */           if (paramInt != 0) {
/* 2328 */             String str2 = ((SubString)localObject2).toString();
/* 2329 */             localScriptable.put(i2 + 1, localScriptable, str2);
/*      */           }
/*      */         }
/* 2332 */         else if (paramInt != 0) {
/* 2333 */           localScriptable.put(i2 + 1, localScriptable, Undefined.instance);
/*      */         }
/*      */       }
/* 2336 */       paramRegExpImpl.lastParen = ((SubString)localObject2);
/*      */     }
/*      */ 
/* 2339 */     if (paramInt != 0)
/*      */     {
/* 2344 */       localScriptable.put("index", localScriptable, Integer.valueOf(i + localREGlobalData.skipped));
/* 2345 */       localScriptable.put("input", localScriptable, paramString);
/*      */     }
/*      */ 
/* 2348 */     if (paramRegExpImpl.lastMatch == null) {
/* 2349 */       paramRegExpImpl.lastMatch = new SubString();
/* 2350 */       paramRegExpImpl.leftContext = new SubString();
/* 2351 */       paramRegExpImpl.rightContext = new SubString();
/*      */     }
/* 2353 */     paramRegExpImpl.lastMatch.charArray = arrayOfChar;
/* 2354 */     paramRegExpImpl.lastMatch.index = k;
/* 2355 */     paramRegExpImpl.lastMatch.length = n;
/*      */ 
/* 2357 */     paramRegExpImpl.leftContext.charArray = arrayOfChar;
/* 2358 */     if (paramContext.getLanguageVersion() == 120)
/*      */     {
/* 2372 */       paramRegExpImpl.leftContext.index = i;
/* 2373 */       paramRegExpImpl.leftContext.length = localREGlobalData.skipped;
/*      */     }
/*      */     else
/*      */     {
/* 2380 */       paramRegExpImpl.leftContext.index = 0;
/* 2381 */       paramRegExpImpl.leftContext.length = (i + localREGlobalData.skipped);
/*      */     }
/*      */ 
/* 2384 */     paramRegExpImpl.rightContext.charArray = arrayOfChar;
/* 2385 */     paramRegExpImpl.rightContext.index = i1;
/* 2386 */     paramRegExpImpl.rightContext.length = (j - i1);
/*      */ 
/* 2388 */     return localObject1;
/*      */   }
/*      */ 
/*      */   int getFlags()
/*      */   {
/* 2393 */     return this.re.flags;
/*      */   }
/*      */ 
/*      */   private static void reportWarning(Context paramContext, String paramString1, String paramString2)
/*      */   {
/* 2398 */     if (paramContext.hasFeature(11)) {
/* 2399 */       String str = ScriptRuntime.getMessage1(paramString1, paramString2);
/* 2400 */       Context.reportWarning(str);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static void reportError(String paramString1, String paramString2)
/*      */   {
/* 2406 */     String str = ScriptRuntime.getMessage1(paramString1, paramString2);
/* 2407 */     throw ScriptRuntime.constructError("SyntaxError", str);
/*      */   }
/*      */ 
/*      */   protected int getMaxInstanceId()
/*      */   {
/* 2424 */     return 5;
/*      */   }
/*      */ 
/*      */   protected int findInstanceIdInfo(String paramString)
/*      */   {
/* 2432 */     int i = 0; String str = null;
/* 2433 */     int m = paramString.length();
/*      */     int k;
/* 2434 */     if (m == 6) {
/* 2435 */       k = paramString.charAt(0);
/* 2436 */       if (k == 103) { str = "global"; i = 3;
/* 2437 */       } else if (k == 115) { str = "source"; i = 2; }
/*      */     }
/* 2439 */     else if (m == 9) {
/* 2440 */       k = paramString.charAt(0);
/* 2441 */       if (k == 108) { str = "lastIndex"; i = 1;
/* 2442 */       } else if (k == 109) { str = "multiline"; i = 5; }
/*      */     }
/* 2444 */     else if (m == 10) { str = "ignoreCase"; i = 4; }
/* 2445 */     if ((str != null) && (str != paramString) && (!str.equals(paramString))) i = 0;
/*      */ 
/* 2451 */     if (i == 0) return super.findInstanceIdInfo(paramString);
/*      */     int j;
/* 2454 */     switch (i) {
/*      */     case 1:
/* 2456 */       j = 6;
/* 2457 */       break;
/*      */     case 2:
/*      */     case 3:
/*      */     case 4:
/*      */     case 5:
/* 2462 */       j = 7;
/* 2463 */       break;
/*      */     default:
/* 2465 */       throw new IllegalStateException();
/*      */     }
/* 2467 */     return instanceIdInfo(j, i);
/*      */   }
/*      */ 
/*      */   protected String getInstanceIdName(int paramInt)
/*      */   {
/* 2473 */     switch (paramInt) { case 1:
/* 2474 */       return "lastIndex";
/*      */     case 2:
/* 2475 */       return "source";
/*      */     case 3:
/* 2476 */       return "global";
/*      */     case 4:
/* 2477 */       return "ignoreCase";
/*      */     case 5:
/* 2478 */       return "multiline";
/*      */     }
/* 2480 */     return super.getInstanceIdName(paramInt);
/*      */   }
/*      */ 
/*      */   protected Object getInstanceIdValue(int paramInt)
/*      */   {
/* 2486 */     switch (paramInt) {
/*      */     case 1:
/* 2488 */       return ScriptRuntime.wrapNumber(this.lastIndex);
/*      */     case 2:
/* 2490 */       return new String(this.re.source);
/*      */     case 3:
/* 2492 */       return ScriptRuntime.wrapBoolean((this.re.flags & 0x1) != 0);
/*      */     case 4:
/* 2494 */       return ScriptRuntime.wrapBoolean((this.re.flags & 0x2) != 0);
/*      */     case 5:
/* 2496 */       return ScriptRuntime.wrapBoolean((this.re.flags & 0x4) != 0);
/*      */     }
/* 2498 */     return super.getInstanceIdValue(paramInt);
/*      */   }
/*      */ 
/*      */   protected void setInstanceIdValue(int paramInt, Object paramObject)
/*      */   {
/* 2504 */     switch (paramInt) {
/*      */     case 1:
/* 2506 */       this.lastIndex = ScriptRuntime.toNumber(paramObject);
/* 2507 */       return;
/*      */     case 2:
/*      */     case 3:
/*      */     case 4:
/*      */     case 5:
/* 2512 */       return;
/*      */     }
/* 2514 */     super.setInstanceIdValue(paramInt, paramObject);
/*      */   }
/*      */ 
/*      */   protected void initPrototypeId(int paramInt)
/*      */   {
/*      */     int i;
/*      */     String str;
/* 2522 */     switch (paramInt) { case 1:
/* 2523 */       i = 1; str = "compile"; break;
/*      */     case 2:
/* 2524 */       i = 0; str = "toString"; break;
/*      */     case 3:
/* 2525 */       i = 0; str = "toSource"; break;
/*      */     case 4:
/* 2526 */       i = 1; str = "exec"; break;
/*      */     case 5:
/* 2527 */       i = 1; str = "test"; break;
/*      */     case 6:
/* 2528 */       i = 1; str = "prefix"; break;
/*      */     default:
/* 2529 */       throw new IllegalArgumentException(String.valueOf(paramInt));
/*      */     }
/* 2531 */     initPrototypeMethod(REGEXP_TAG, paramInt, str, i);
/*      */   }
/*      */ 
/*      */   public Object execIdCall(IdFunctionObject paramIdFunctionObject, Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject)
/*      */   {
/* 2538 */     if (!paramIdFunctionObject.hasTag(REGEXP_TAG)) {
/* 2539 */       return super.execIdCall(paramIdFunctionObject, paramContext, paramScriptable1, paramScriptable2, paramArrayOfObject);
/*      */     }
/* 2541 */     int i = paramIdFunctionObject.methodId();
/* 2542 */     switch (i) {
/*      */     case 1:
/* 2544 */       return realThis(paramScriptable2, paramIdFunctionObject).compile(paramContext, paramScriptable1, paramArrayOfObject);
/*      */     case 2:
/*      */     case 3:
/* 2548 */       return realThis(paramScriptable2, paramIdFunctionObject).toString();
/*      */     case 4:
/* 2551 */       return realThis(paramScriptable2, paramIdFunctionObject).execSub(paramContext, paramScriptable1, paramArrayOfObject, 1);
/*      */     case 5:
/* 2554 */       Object localObject = realThis(paramScriptable2, paramIdFunctionObject).execSub(paramContext, paramScriptable1, paramArrayOfObject, 0);
/* 2555 */       return Boolean.TRUE.equals(localObject) ? Boolean.TRUE : Boolean.FALSE;
/*      */     case 6:
/* 2559 */       return realThis(paramScriptable2, paramIdFunctionObject).execSub(paramContext, paramScriptable1, paramArrayOfObject, 2);
/*      */     }
/* 2561 */     throw new IllegalArgumentException(String.valueOf(i));
/*      */   }
/*      */ 
/*      */   private static NativeRegExp realThis(Scriptable paramScriptable, IdFunctionObject paramIdFunctionObject)
/*      */   {
/* 2566 */     if (!(paramScriptable instanceof NativeRegExp))
/* 2567 */       throw incompatibleCallError(paramIdFunctionObject);
/* 2568 */     return (NativeRegExp)paramScriptable;
/*      */   }
/*      */ 
/*      */   protected int findPrototypeId(String paramString)
/*      */   {
/* 2577 */     int i = 0; String str = null;
/*      */     int j;
/* 2578 */     switch (paramString.length()) { case 4:
/* 2579 */       j = paramString.charAt(0);
/* 2580 */       if (j == 101) { str = "exec"; i = 4;
/* 2581 */       } else if (j == 116) { str = "test"; i = 5; } break;
/*      */     case 6:
/* 2583 */       str = "prefix"; i = 6; break;
/*      */     case 7:
/* 2584 */       str = "compile"; i = 1; break;
/*      */     case 8:
/* 2585 */       j = paramString.charAt(3);
/* 2586 */       if (j == 111) { str = "toSource"; i = 3;
/* 2587 */       } else if (j == 116) { str = "toString"; i = 2; } break;
/*      */     case 5:
/*      */     }
/* 2590 */     if ((str != null) && (str != paramString) && (!str.equals(paramString))) i = 0;
/*      */ 
/* 2594 */     return i;
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.regexp.NativeRegExp
 * JD-Core Version:    0.6.2
 */