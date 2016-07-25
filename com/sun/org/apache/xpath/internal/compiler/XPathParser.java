/*      */ package com.sun.org.apache.xpath.internal.compiler;
/*      */ 
/*      */ import com.sun.org.apache.xalan.internal.res.XSLMessages;
/*      */ import com.sun.org.apache.xml.internal.utils.ObjectVector;
/*      */ import com.sun.org.apache.xml.internal.utils.PrefixResolver;
/*      */ import com.sun.org.apache.xpath.internal.XPathProcessorException;
/*      */ import com.sun.org.apache.xpath.internal.domapi.XPathStylesheetDOM3Exception;
/*      */ import com.sun.org.apache.xpath.internal.objects.XNumber;
/*      */ import com.sun.org.apache.xpath.internal.objects.XString;
/*      */ import java.io.PrintStream;
/*      */ import javax.xml.transform.ErrorListener;
/*      */ import javax.xml.transform.SourceLocator;
/*      */ import javax.xml.transform.TransformerException;
/*      */ 
/*      */ public class XPathParser
/*      */ {
/*      */   public static final String CONTINUE_AFTER_FATAL_ERROR = "CONTINUE_AFTER_FATAL_ERROR";
/*      */   private OpMap m_ops;
/*      */   transient String m_token;
/*   63 */   transient char m_tokenChar = '\000';
/*      */ 
/*   68 */   int m_queueMark = 0;
/*      */   protected static final int FILTER_MATCH_FAILED = 0;
/*      */   protected static final int FILTER_MATCH_PRIMARY = 1;
/*      */   protected static final int FILTER_MATCH_PREDICATES = 2;
/*      */   PrefixResolver m_namespaceContext;
/*      */   private ErrorListener m_errorListener;
/*      */   SourceLocator m_sourceLocator;
/*      */   private FunctionTable m_functionTable;
/*      */ 
/*      */   public XPathParser(ErrorListener errorListener, SourceLocator sourceLocator)
/*      */   {
/*   82 */     this.m_errorListener = errorListener;
/*   83 */     this.m_sourceLocator = sourceLocator;
/*      */   }
/*      */ 
/*      */   public void initXPath(Compiler compiler, String expression, PrefixResolver namespaceContext)
/*      */     throws TransformerException
/*      */   {
/*  108 */     this.m_ops = compiler;
/*  109 */     this.m_namespaceContext = namespaceContext;
/*  110 */     this.m_functionTable = compiler.getFunctionTable();
/*      */ 
/*  112 */     Lexer lexer = new Lexer(compiler, namespaceContext, this);
/*      */ 
/*  114 */     lexer.tokenize(expression);
/*      */ 
/*  116 */     this.m_ops.setOp(0, 1);
/*  117 */     this.m_ops.setOp(1, 2);
/*      */     try
/*      */     {
/*  130 */       nextToken();
/*  131 */       Expr();
/*      */ 
/*  133 */       if (null != this.m_token)
/*      */       {
/*  135 */         String extraTokens = "";
/*      */ 
/*  137 */         while (null != this.m_token)
/*      */         {
/*  139 */           extraTokens = extraTokens + "'" + this.m_token + "'";
/*      */ 
/*  141 */           nextToken();
/*      */ 
/*  143 */           if (null != this.m_token) {
/*  144 */             extraTokens = extraTokens + ", ";
/*      */           }
/*      */         }
/*  147 */         error("ER_EXTRA_ILLEGAL_TOKENS", new Object[] { extraTokens });
/*      */       }
/*      */ 
/*      */     }
/*      */     catch (XPathProcessorException e)
/*      */     {
/*  154 */       if ("CONTINUE_AFTER_FATAL_ERROR".equals(e.getMessage()))
/*      */       {
/*  159 */         initXPath(compiler, "/..", namespaceContext);
/*      */       }
/*      */       else {
/*  162 */         throw e;
/*      */       }
/*      */     }
/*  165 */     compiler.shrink();
/*      */   }
/*      */ 
/*      */   public void initMatchPattern(Compiler compiler, String expression, PrefixResolver namespaceContext)
/*      */     throws TransformerException
/*      */   {
/*  184 */     this.m_ops = compiler;
/*  185 */     this.m_namespaceContext = namespaceContext;
/*  186 */     this.m_functionTable = compiler.getFunctionTable();
/*      */ 
/*  188 */     Lexer lexer = new Lexer(compiler, namespaceContext, this);
/*      */ 
/*  190 */     lexer.tokenize(expression);
/*      */ 
/*  192 */     this.m_ops.setOp(0, 30);
/*  193 */     this.m_ops.setOp(1, 2);
/*      */ 
/*  195 */     nextToken();
/*  196 */     Pattern();
/*      */ 
/*  198 */     if (null != this.m_token)
/*      */     {
/*  200 */       String extraTokens = "";
/*      */ 
/*  202 */       while (null != this.m_token)
/*      */       {
/*  204 */         extraTokens = extraTokens + "'" + this.m_token + "'";
/*      */ 
/*  206 */         nextToken();
/*      */ 
/*  208 */         if (null != this.m_token) {
/*  209 */           extraTokens = extraTokens + ", ";
/*      */         }
/*      */       }
/*  212 */       error("ER_EXTRA_ILLEGAL_TOKENS", new Object[] { extraTokens });
/*      */     }
/*      */ 
/*  217 */     this.m_ops.setOp(this.m_ops.getOp(1), -1);
/*  218 */     this.m_ops.setOp(1, this.m_ops.getOp(1) + 1);
/*      */ 
/*  220 */     this.m_ops.shrink();
/*      */   }
/*      */ 
/*      */   public void setErrorHandler(ErrorListener handler)
/*      */   {
/*  243 */     this.m_errorListener = handler;
/*      */   }
/*      */ 
/*      */   public ErrorListener getErrorListener()
/*      */   {
/*  253 */     return this.m_errorListener;
/*      */   }
/*      */ 
/*      */   final boolean tokenIs(String s)
/*      */   {
/*  266 */     return s == null ? true : this.m_token != null ? this.m_token.equals(s) : false;
/*      */   }
/*      */ 
/*      */   final boolean tokenIs(char c)
/*      */   {
/*  279 */     return this.m_tokenChar == c;
/*      */   }
/*      */ 
/*      */   final boolean lookahead(char c, int n)
/*      */   {
/*  295 */     int pos = this.m_queueMark + n;
/*      */     boolean b;
/*      */     boolean b;
/*  298 */     if ((pos <= this.m_ops.getTokenQueueSize()) && (pos > 0) && (this.m_ops.getTokenQueueSize() != 0))
/*      */     {
/*  301 */       String tok = (String)this.m_ops.m_tokenQueue.elementAt(pos - 1);
/*      */ 
/*  303 */       b = tok.charAt(0) == c;
/*      */     }
/*      */     else
/*      */     {
/*  307 */       b = false;
/*      */     }
/*      */ 
/*  310 */     return b;
/*      */   }
/*      */ 
/*      */   private final boolean lookbehind(char c, int n)
/*      */   {
/*  331 */     int lookBehindPos = this.m_queueMark - (n + 1);
/*      */     boolean isToken;
/*      */     boolean isToken;
/*  333 */     if (lookBehindPos >= 0)
/*      */     {
/*  335 */       String lookbehind = (String)this.m_ops.m_tokenQueue.elementAt(lookBehindPos);
/*      */       boolean isToken;
/*  337 */       if (lookbehind.length() == 1)
/*      */       {
/*  339 */         char c0 = lookbehind == null ? '|' : lookbehind.charAt(0);
/*      */ 
/*  341 */         isToken = c0 != '|';
/*      */       }
/*      */       else
/*      */       {
/*  345 */         isToken = false;
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/*  350 */       isToken = false;
/*      */     }
/*      */ 
/*  353 */     return isToken;
/*      */   }
/*      */ 
/*      */   private final boolean lookbehindHasToken(int n)
/*      */   {
/*      */     boolean hasToken;
/*      */     boolean hasToken;
/*  373 */     if (this.m_queueMark - n > 0)
/*      */     {
/*  375 */       String lookbehind = (String)this.m_ops.m_tokenQueue.elementAt(this.m_queueMark - (n - 1));
/*  376 */       char c0 = lookbehind == null ? '|' : lookbehind.charAt(0);
/*      */ 
/*  378 */       hasToken = c0 != '|';
/*      */     }
/*      */     else
/*      */     {
/*  382 */       hasToken = false;
/*      */     }
/*      */ 
/*  385 */     return hasToken;
/*      */   }
/*      */ 
/*      */   private final boolean lookahead(String s, int n)
/*      */   {
/*      */     boolean isToken;
/*      */     boolean isToken;
/*  404 */     if (this.m_queueMark + n <= this.m_ops.getTokenQueueSize())
/*      */     {
/*  406 */       String lookahead = (String)this.m_ops.m_tokenQueue.elementAt(this.m_queueMark + (n - 1));
/*      */ 
/*  408 */       isToken = s == null ? true : lookahead != null ? lookahead.equals(s) : false;
/*      */     }
/*      */     else
/*      */     {
/*  412 */       isToken = null == s;
/*      */     }
/*      */ 
/*  415 */     return isToken;
/*      */   }
/*      */ 
/*      */   private final void nextToken()
/*      */   {
/*  425 */     if (this.m_queueMark < this.m_ops.getTokenQueueSize())
/*      */     {
/*  427 */       this.m_token = ((String)this.m_ops.m_tokenQueue.elementAt(this.m_queueMark++));
/*  428 */       this.m_tokenChar = this.m_token.charAt(0);
/*      */     }
/*      */     else
/*      */     {
/*  432 */       this.m_token = null;
/*  433 */       this.m_tokenChar = '\000';
/*      */     }
/*      */   }
/*      */ 
/*      */   private final String getTokenRelative(int i)
/*      */   {
/*  449 */     int relative = this.m_queueMark + i;
/*      */     String tok;
/*      */     String tok;
/*  451 */     if ((relative > 0) && (relative < this.m_ops.getTokenQueueSize()))
/*      */     {
/*  453 */       tok = (String)this.m_ops.m_tokenQueue.elementAt(relative);
/*      */     }
/*      */     else
/*      */     {
/*  457 */       tok = null;
/*      */     }
/*      */ 
/*  460 */     return tok;
/*      */   }
/*      */ 
/*      */   private final void prevToken()
/*      */   {
/*  470 */     if (this.m_queueMark > 0)
/*      */     {
/*  472 */       this.m_queueMark -= 1;
/*      */ 
/*  474 */       this.m_token = ((String)this.m_ops.m_tokenQueue.elementAt(this.m_queueMark));
/*  475 */       this.m_tokenChar = this.m_token.charAt(0);
/*      */     }
/*      */     else
/*      */     {
/*  479 */       this.m_token = null;
/*  480 */       this.m_tokenChar = '\000';
/*      */     }
/*      */   }
/*      */ 
/*      */   private final void consumeExpected(String expected)
/*      */     throws TransformerException
/*      */   {
/*  496 */     if (tokenIs(expected))
/*      */     {
/*  498 */       nextToken();
/*      */     }
/*      */     else
/*      */     {
/*  502 */       error("ER_EXPECTED_BUT_FOUND", new Object[] { expected, this.m_token });
/*      */ 
/*  508 */       throw new XPathProcessorException("CONTINUE_AFTER_FATAL_ERROR");
/*      */     }
/*      */   }
/*      */ 
/*      */   private final void consumeExpected(char expected)
/*      */     throws TransformerException
/*      */   {
/*  524 */     if (tokenIs(expected))
/*      */     {
/*  526 */       nextToken();
/*      */     }
/*      */     else
/*      */     {
/*  530 */       error("ER_EXPECTED_BUT_FOUND", new Object[] { String.valueOf(expected), this.m_token });
/*      */ 
/*  537 */       throw new XPathProcessorException("CONTINUE_AFTER_FATAL_ERROR");
/*      */     }
/*      */   }
/*      */ 
/*      */   void warn(String msg, Object[] args)
/*      */     throws TransformerException
/*      */   {
/*  556 */     String fmsg = XSLMessages.createXPATHWarning(msg, args);
/*  557 */     ErrorListener ehandler = getErrorListener();
/*      */ 
/*  559 */     if (null != ehandler)
/*      */     {
/*  562 */       ehandler.warning(new TransformerException(fmsg, this.m_sourceLocator));
/*      */     }
/*      */     else
/*      */     {
/*  567 */       System.err.println(fmsg);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void assertion(boolean b, String msg)
/*      */   {
/*  583 */     if (!b)
/*      */     {
/*  585 */       String fMsg = XSLMessages.createXPATHMessage("ER_INCORRECT_PROGRAMMER_ASSERTION", new Object[] { msg });
/*      */ 
/*  589 */       throw new RuntimeException(fMsg);
/*      */     }
/*      */   }
/*      */ 
/*      */   void error(String msg, Object[] args)
/*      */     throws TransformerException
/*      */   {
/*  609 */     String fmsg = XSLMessages.createXPATHMessage(msg, args);
/*  610 */     ErrorListener ehandler = getErrorListener();
/*      */ 
/*  612 */     TransformerException te = new TransformerException(fmsg, this.m_sourceLocator);
/*  613 */     if (null != ehandler)
/*      */     {
/*  616 */       ehandler.fatalError(te);
/*      */     }
/*      */     else
/*      */     {
/*  621 */       throw te;
/*      */     }
/*      */   }
/*      */ 
/*      */   void errorForDOM3(String msg, Object[] args)
/*      */     throws TransformerException
/*      */   {
/*  654 */     String fmsg = XSLMessages.createXPATHMessage(msg, args);
/*  655 */     ErrorListener ehandler = getErrorListener();
/*      */ 
/*  657 */     TransformerException te = new XPathStylesheetDOM3Exception(fmsg, this.m_sourceLocator);
/*  658 */     if (null != ehandler)
/*      */     {
/*  661 */       ehandler.fatalError(te);
/*      */     }
/*      */     else
/*      */     {
/*  666 */       throw te;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected String dumpRemainingTokenQueue()
/*      */   {
/*  679 */     int q = this.m_queueMark;
/*      */     String returnMsg;
/*      */     String returnMsg;
/*  682 */     if (q < this.m_ops.getTokenQueueSize())
/*      */     {
/*  684 */       String msg = "\n Remaining tokens: (";
/*      */ 
/*  686 */       while (q < this.m_ops.getTokenQueueSize())
/*      */       {
/*  688 */         String t = (String)this.m_ops.m_tokenQueue.elementAt(q++);
/*      */ 
/*  690 */         msg = msg + " '" + t + "'";
/*      */       }
/*      */ 
/*  693 */       returnMsg = msg + ")";
/*      */     }
/*      */     else
/*      */     {
/*  697 */       returnMsg = "";
/*      */     }
/*      */ 
/*  700 */     return returnMsg;
/*      */   }
/*      */ 
/*      */   final int getFunctionToken(String key)
/*      */   {
/*      */     int tok;
/*      */     try
/*      */     {
/*  723 */       Object id = Keywords.lookupNodeTest(key);
/*  724 */       if (null == id) id = this.m_functionTable.getFunctionID(key);
/*  725 */       tok = ((Integer)id).intValue();
/*      */     }
/*      */     catch (NullPointerException npe)
/*      */     {
/*  729 */       tok = -1;
/*      */     }
/*      */     catch (ClassCastException cce)
/*      */     {
/*  733 */       tok = -1;
/*      */     }
/*      */ 
/*  736 */     return tok;
/*      */   }
/*      */ 
/*      */   void insertOp(int pos, int length, int op)
/*      */   {
/*  751 */     int totalLen = this.m_ops.getOp(1);
/*      */ 
/*  753 */     for (int i = totalLen - 1; i >= pos; i--)
/*      */     {
/*  755 */       this.m_ops.setOp(i + length, this.m_ops.getOp(i));
/*      */     }
/*      */ 
/*  758 */     this.m_ops.setOp(pos, op);
/*  759 */     this.m_ops.setOp(1, totalLen + length);
/*      */   }
/*      */ 
/*      */   void appendOp(int length, int op)
/*      */   {
/*  773 */     int totalLen = this.m_ops.getOp(1);
/*      */ 
/*  775 */     this.m_ops.setOp(totalLen, op);
/*  776 */     this.m_ops.setOp(totalLen + 1, length);
/*  777 */     this.m_ops.setOp(1, totalLen + length);
/*      */   }
/*      */ 
/*      */   protected void Expr()
/*      */     throws TransformerException
/*      */   {
/*  792 */     OrExpr();
/*      */   }
/*      */ 
/*      */   protected void OrExpr()
/*      */     throws TransformerException
/*      */   {
/*  807 */     int opPos = this.m_ops.getOp(1);
/*      */ 
/*  809 */     AndExpr();
/*      */ 
/*  811 */     if ((null != this.m_token) && (tokenIs("or")))
/*      */     {
/*  813 */       nextToken();
/*  814 */       insertOp(opPos, 2, 2);
/*  815 */       OrExpr();
/*      */ 
/*  817 */       this.m_ops.setOp(opPos + 1, this.m_ops.getOp(1) - opPos);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void AndExpr()
/*      */     throws TransformerException
/*      */   {
/*  834 */     int opPos = this.m_ops.getOp(1);
/*      */ 
/*  836 */     EqualityExpr(-1);
/*      */ 
/*  838 */     if ((null != this.m_token) && (tokenIs("and")))
/*      */     {
/*  840 */       nextToken();
/*  841 */       insertOp(opPos, 2, 3);
/*  842 */       AndExpr();
/*      */ 
/*  844 */       this.m_ops.setOp(opPos + 1, this.m_ops.getOp(1) - opPos);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected int EqualityExpr(int addPos)
/*      */     throws TransformerException
/*      */   {
/*  867 */     int opPos = this.m_ops.getOp(1);
/*      */ 
/*  869 */     if (-1 == addPos) {
/*  870 */       addPos = opPos;
/*      */     }
/*  872 */     RelationalExpr(-1);
/*      */ 
/*  874 */     if (null != this.m_token)
/*      */     {
/*  876 */       if ((tokenIs('!')) && (lookahead('=', 1)))
/*      */       {
/*  878 */         nextToken();
/*  879 */         nextToken();
/*  880 */         insertOp(addPos, 2, 4);
/*      */ 
/*  882 */         int opPlusLeftHandLen = this.m_ops.getOp(1) - addPos;
/*      */ 
/*  884 */         addPos = EqualityExpr(addPos);
/*  885 */         this.m_ops.setOp(addPos + 1, this.m_ops.getOp(addPos + opPlusLeftHandLen + 1) + opPlusLeftHandLen);
/*      */ 
/*  887 */         addPos += 2;
/*      */       }
/*  889 */       else if (tokenIs('='))
/*      */       {
/*  891 */         nextToken();
/*  892 */         insertOp(addPos, 2, 5);
/*      */ 
/*  894 */         int opPlusLeftHandLen = this.m_ops.getOp(1) - addPos;
/*      */ 
/*  896 */         addPos = EqualityExpr(addPos);
/*  897 */         this.m_ops.setOp(addPos + 1, this.m_ops.getOp(addPos + opPlusLeftHandLen + 1) + opPlusLeftHandLen);
/*      */ 
/*  899 */         addPos += 2;
/*      */       }
/*      */     }
/*      */ 
/*  903 */     return addPos;
/*      */   }
/*      */ 
/*      */   protected int RelationalExpr(int addPos)
/*      */     throws TransformerException
/*      */   {
/*  927 */     int opPos = this.m_ops.getOp(1);
/*      */ 
/*  929 */     if (-1 == addPos) {
/*  930 */       addPos = opPos;
/*      */     }
/*  932 */     AdditiveExpr(-1);
/*      */ 
/*  934 */     if (null != this.m_token)
/*      */     {
/*  936 */       if (tokenIs('<'))
/*      */       {
/*  938 */         nextToken();
/*      */ 
/*  940 */         if (tokenIs('='))
/*      */         {
/*  942 */           nextToken();
/*  943 */           insertOp(addPos, 2, 6);
/*      */         }
/*      */         else
/*      */         {
/*  947 */           insertOp(addPos, 2, 7);
/*      */         }
/*      */ 
/*  950 */         int opPlusLeftHandLen = this.m_ops.getOp(1) - addPos;
/*      */ 
/*  952 */         addPos = RelationalExpr(addPos);
/*  953 */         this.m_ops.setOp(addPos + 1, this.m_ops.getOp(addPos + opPlusLeftHandLen + 1) + opPlusLeftHandLen);
/*      */ 
/*  955 */         addPos += 2;
/*      */       }
/*  957 */       else if (tokenIs('>'))
/*      */       {
/*  959 */         nextToken();
/*      */ 
/*  961 */         if (tokenIs('='))
/*      */         {
/*  963 */           nextToken();
/*  964 */           insertOp(addPos, 2, 8);
/*      */         }
/*      */         else
/*      */         {
/*  968 */           insertOp(addPos, 2, 9);
/*      */         }
/*      */ 
/*  971 */         int opPlusLeftHandLen = this.m_ops.getOp(1) - addPos;
/*      */ 
/*  973 */         addPos = RelationalExpr(addPos);
/*  974 */         this.m_ops.setOp(addPos + 1, this.m_ops.getOp(addPos + opPlusLeftHandLen + 1) + opPlusLeftHandLen);
/*      */ 
/*  976 */         addPos += 2;
/*      */       }
/*      */     }
/*      */ 
/*  980 */     return addPos;
/*      */   }
/*      */ 
/*      */   protected int AdditiveExpr(int addPos)
/*      */     throws TransformerException
/*      */   {
/* 1002 */     int opPos = this.m_ops.getOp(1);
/*      */ 
/* 1004 */     if (-1 == addPos) {
/* 1005 */       addPos = opPos;
/*      */     }
/* 1007 */     MultiplicativeExpr(-1);
/*      */ 
/* 1009 */     if (null != this.m_token)
/*      */     {
/* 1011 */       if (tokenIs('+'))
/*      */       {
/* 1013 */         nextToken();
/* 1014 */         insertOp(addPos, 2, 10);
/*      */ 
/* 1016 */         int opPlusLeftHandLen = this.m_ops.getOp(1) - addPos;
/*      */ 
/* 1018 */         addPos = AdditiveExpr(addPos);
/* 1019 */         this.m_ops.setOp(addPos + 1, this.m_ops.getOp(addPos + opPlusLeftHandLen + 1) + opPlusLeftHandLen);
/*      */ 
/* 1021 */         addPos += 2;
/*      */       }
/* 1023 */       else if (tokenIs('-'))
/*      */       {
/* 1025 */         nextToken();
/* 1026 */         insertOp(addPos, 2, 11);
/*      */ 
/* 1028 */         int opPlusLeftHandLen = this.m_ops.getOp(1) - addPos;
/*      */ 
/* 1030 */         addPos = AdditiveExpr(addPos);
/* 1031 */         this.m_ops.setOp(addPos + 1, this.m_ops.getOp(addPos + opPlusLeftHandLen + 1) + opPlusLeftHandLen);
/*      */ 
/* 1033 */         addPos += 2;
/*      */       }
/*      */     }
/*      */ 
/* 1037 */     return addPos;
/*      */   }
/*      */ 
/*      */   protected int MultiplicativeExpr(int addPos)
/*      */     throws TransformerException
/*      */   {
/* 1060 */     int opPos = this.m_ops.getOp(1);
/*      */ 
/* 1062 */     if (-1 == addPos) {
/* 1063 */       addPos = opPos;
/*      */     }
/* 1065 */     UnaryExpr();
/*      */ 
/* 1067 */     if (null != this.m_token)
/*      */     {
/* 1069 */       if (tokenIs('*'))
/*      */       {
/* 1071 */         nextToken();
/* 1072 */         insertOp(addPos, 2, 12);
/*      */ 
/* 1074 */         int opPlusLeftHandLen = this.m_ops.getOp(1) - addPos;
/*      */ 
/* 1076 */         addPos = MultiplicativeExpr(addPos);
/* 1077 */         this.m_ops.setOp(addPos + 1, this.m_ops.getOp(addPos + opPlusLeftHandLen + 1) + opPlusLeftHandLen);
/*      */ 
/* 1079 */         addPos += 2;
/*      */       }
/* 1081 */       else if (tokenIs("div"))
/*      */       {
/* 1083 */         nextToken();
/* 1084 */         insertOp(addPos, 2, 13);
/*      */ 
/* 1086 */         int opPlusLeftHandLen = this.m_ops.getOp(1) - addPos;
/*      */ 
/* 1088 */         addPos = MultiplicativeExpr(addPos);
/* 1089 */         this.m_ops.setOp(addPos + 1, this.m_ops.getOp(addPos + opPlusLeftHandLen + 1) + opPlusLeftHandLen);
/*      */ 
/* 1091 */         addPos += 2;
/*      */       }
/* 1093 */       else if (tokenIs("mod"))
/*      */       {
/* 1095 */         nextToken();
/* 1096 */         insertOp(addPos, 2, 14);
/*      */ 
/* 1098 */         int opPlusLeftHandLen = this.m_ops.getOp(1) - addPos;
/*      */ 
/* 1100 */         addPos = MultiplicativeExpr(addPos);
/* 1101 */         this.m_ops.setOp(addPos + 1, this.m_ops.getOp(addPos + opPlusLeftHandLen + 1) + opPlusLeftHandLen);
/*      */ 
/* 1103 */         addPos += 2;
/*      */       }
/* 1105 */       else if (tokenIs("quo"))
/*      */       {
/* 1107 */         nextToken();
/* 1108 */         insertOp(addPos, 2, 15);
/*      */ 
/* 1110 */         int opPlusLeftHandLen = this.m_ops.getOp(1) - addPos;
/*      */ 
/* 1112 */         addPos = MultiplicativeExpr(addPos);
/* 1113 */         this.m_ops.setOp(addPos + 1, this.m_ops.getOp(addPos + opPlusLeftHandLen + 1) + opPlusLeftHandLen);
/*      */ 
/* 1115 */         addPos += 2;
/*      */       }
/*      */     }
/*      */ 
/* 1119 */     return addPos;
/*      */   }
/*      */ 
/*      */   protected void UnaryExpr()
/*      */     throws TransformerException
/*      */   {
/* 1133 */     int opPos = this.m_ops.getOp(1);
/* 1134 */     boolean isNeg = false;
/*      */ 
/* 1136 */     if (this.m_tokenChar == '-')
/*      */     {
/* 1138 */       nextToken();
/* 1139 */       appendOp(2, 16);
/*      */ 
/* 1141 */       isNeg = true;
/*      */     }
/*      */ 
/* 1144 */     UnionExpr();
/*      */ 
/* 1146 */     if (isNeg)
/* 1147 */       this.m_ops.setOp(opPos + 1, this.m_ops.getOp(1) - opPos);
/*      */   }
/*      */ 
/*      */   protected void StringExpr()
/*      */     throws TransformerException
/*      */   {
/* 1161 */     int opPos = this.m_ops.getOp(1);
/*      */ 
/* 1163 */     appendOp(2, 17);
/* 1164 */     Expr();
/*      */ 
/* 1166 */     this.m_ops.setOp(opPos + 1, this.m_ops.getOp(1) - opPos);
/*      */   }
/*      */ 
/*      */   protected void BooleanExpr()
/*      */     throws TransformerException
/*      */   {
/* 1181 */     int opPos = this.m_ops.getOp(1);
/*      */ 
/* 1183 */     appendOp(2, 18);
/* 1184 */     Expr();
/*      */ 
/* 1186 */     int opLen = this.m_ops.getOp(1) - opPos;
/*      */ 
/* 1188 */     if (opLen == 2)
/*      */     {
/* 1190 */       error("ER_BOOLEAN_ARG_NO_LONGER_OPTIONAL", null);
/*      */     }
/*      */ 
/* 1193 */     this.m_ops.setOp(opPos + 1, opLen);
/*      */   }
/*      */ 
/*      */   protected void NumberExpr()
/*      */     throws TransformerException
/*      */   {
/* 1207 */     int opPos = this.m_ops.getOp(1);
/*      */ 
/* 1209 */     appendOp(2, 19);
/* 1210 */     Expr();
/*      */ 
/* 1212 */     this.m_ops.setOp(opPos + 1, this.m_ops.getOp(1) - opPos);
/*      */   }
/*      */ 
/*      */   protected void UnionExpr()
/*      */     throws TransformerException
/*      */   {
/* 1232 */     int opPos = this.m_ops.getOp(1);
/* 1233 */     boolean continueOrLoop = true;
/* 1234 */     boolean foundUnion = false;
/*      */     do
/*      */     {
/* 1238 */       PathExpr();
/*      */ 
/* 1240 */       if (!tokenIs('|'))
/*      */         break;
/* 1242 */       if (false == foundUnion)
/*      */       {
/* 1244 */         foundUnion = true;
/*      */ 
/* 1246 */         insertOp(opPos, 2, 20);
/*      */       }
/*      */ 
/* 1249 */       nextToken();
/*      */     }
/*      */ 
/* 1258 */     while (continueOrLoop);
/*      */ 
/* 1260 */     this.m_ops.setOp(opPos + 1, this.m_ops.getOp(1) - opPos);
/*      */   }
/*      */ 
/*      */   protected void PathExpr()
/*      */     throws TransformerException
/*      */   {
/* 1278 */     int opPos = this.m_ops.getOp(1);
/*      */ 
/* 1280 */     int filterExprMatch = FilterExpr();
/*      */ 
/* 1282 */     if (filterExprMatch != 0)
/*      */     {
/* 1286 */       boolean locationPathStarted = filterExprMatch == 2;
/*      */ 
/* 1288 */       if (tokenIs('/'))
/*      */       {
/* 1290 */         nextToken();
/*      */ 
/* 1292 */         if (!locationPathStarted)
/*      */         {
/* 1295 */           insertOp(opPos, 2, 28);
/*      */ 
/* 1297 */           locationPathStarted = true;
/*      */         }
/*      */ 
/* 1300 */         if (!RelativeLocationPath())
/*      */         {
/* 1303 */           error("ER_EXPECTED_REL_LOC_PATH", null);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1309 */       if (locationPathStarted)
/*      */       {
/* 1311 */         this.m_ops.setOp(this.m_ops.getOp(1), -1);
/* 1312 */         this.m_ops.setOp(1, this.m_ops.getOp(1) + 1);
/* 1313 */         this.m_ops.setOp(opPos + 1, this.m_ops.getOp(1) - opPos);
/*      */       }
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/* 1319 */       LocationPath();
/*      */     }
/*      */   }
/*      */ 
/*      */   protected int FilterExpr()
/*      */     throws TransformerException
/*      */   {
/* 1343 */     int opPos = this.m_ops.getOp(1);
/*      */     int filterMatch;
/*      */     int filterMatch;
/* 1347 */     if (PrimaryExpr())
/*      */     {
/*      */       int filterMatch;
/* 1349 */       if (tokenIs('['))
/*      */       {
/* 1353 */         insertOp(opPos, 2, 28);
/*      */ 
/* 1355 */         while (tokenIs('['))
/*      */         {
/* 1357 */           Predicate();
/*      */         }
/*      */ 
/* 1360 */         filterMatch = 2;
/*      */       }
/*      */       else
/*      */       {
/* 1364 */         filterMatch = 1;
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/* 1369 */       filterMatch = 0;
/*      */     }
/*      */ 
/* 1372 */     return filterMatch;
/*      */   }
/*      */ 
/*      */   protected boolean PrimaryExpr()
/*      */     throws TransformerException
/*      */   {
/* 1400 */     int opPos = this.m_ops.getOp(1);
/*      */     boolean matchFound;
/*      */     boolean matchFound;
/* 1402 */     if ((this.m_tokenChar == '\'') || (this.m_tokenChar == '"'))
/*      */     {
/* 1404 */       appendOp(2, 21);
/* 1405 */       Literal();
/*      */ 
/* 1407 */       this.m_ops.setOp(opPos + 1, this.m_ops.getOp(1) - opPos);
/*      */ 
/* 1410 */       matchFound = true;
/*      */     }
/*      */     else
/*      */     {
/*      */       boolean matchFound;
/* 1412 */       if (this.m_tokenChar == '$')
/*      */       {
/* 1414 */         nextToken();
/* 1415 */         appendOp(2, 22);
/* 1416 */         QName();
/*      */ 
/* 1418 */         this.m_ops.setOp(opPos + 1, this.m_ops.getOp(1) - opPos);
/*      */ 
/* 1421 */         matchFound = true;
/*      */       }
/*      */       else
/*      */       {
/*      */         boolean matchFound;
/* 1423 */         if (this.m_tokenChar == '(')
/*      */         {
/* 1425 */           nextToken();
/* 1426 */           appendOp(2, 23);
/* 1427 */           Expr();
/* 1428 */           consumeExpected(')');
/*      */ 
/* 1430 */           this.m_ops.setOp(opPos + 1, this.m_ops.getOp(1) - opPos);
/*      */ 
/* 1433 */           matchFound = true;
/*      */         }
/*      */         else
/*      */         {
/*      */           boolean matchFound;
/* 1435 */           if ((null != this.m_token) && ((('.' == this.m_tokenChar) && (this.m_token.length() > 1) && (Character.isDigit(this.m_token.charAt(1)))) || (Character.isDigit(this.m_tokenChar))))
/*      */           {
/* 1438 */             appendOp(2, 27);
/* 1439 */             Number();
/*      */ 
/* 1441 */             this.m_ops.setOp(opPos + 1, this.m_ops.getOp(1) - opPos);
/*      */ 
/* 1444 */             matchFound = true;
/*      */           }
/*      */           else
/*      */           {
/*      */             boolean matchFound;
/* 1446 */             if ((lookahead('(', 1)) || ((lookahead(':', 1)) && (lookahead('(', 3))))
/*      */             {
/* 1448 */               matchFound = FunctionCall();
/*      */             }
/*      */             else
/*      */             {
/* 1452 */               matchFound = false;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 1455 */     return matchFound;
/*      */   }
/*      */ 
/*      */   protected void Argument()
/*      */     throws TransformerException
/*      */   {
/* 1468 */     int opPos = this.m_ops.getOp(1);
/*      */ 
/* 1470 */     appendOp(2, 26);
/* 1471 */     Expr();
/*      */ 
/* 1473 */     this.m_ops.setOp(opPos + 1, this.m_ops.getOp(1) - opPos);
/*      */   }
/*      */ 
/*      */   protected boolean FunctionCall()
/*      */     throws TransformerException
/*      */   {
/* 1488 */     int opPos = this.m_ops.getOp(1);
/*      */ 
/* 1490 */     if (lookahead(':', 1))
/*      */     {
/* 1492 */       appendOp(4, 24);
/*      */ 
/* 1494 */       this.m_ops.setOp(opPos + 1 + 1, this.m_queueMark - 1);
/*      */ 
/* 1496 */       nextToken();
/* 1497 */       consumeExpected(':');
/*      */ 
/* 1499 */       this.m_ops.setOp(opPos + 1 + 2, this.m_queueMark - 1);
/*      */ 
/* 1501 */       nextToken();
/*      */     }
/*      */     else
/*      */     {
/* 1505 */       int funcTok = getFunctionToken(this.m_token);
/*      */ 
/* 1507 */       if (-1 == funcTok)
/*      */       {
/* 1509 */         error("ER_COULDNOT_FIND_FUNCTION", new Object[] { this.m_token });
/*      */       }
/*      */ 
/* 1513 */       switch (funcTok)
/*      */       {
/*      */       case 1030:
/*      */       case 1031:
/*      */       case 1032:
/*      */       case 1033:
/* 1520 */         return false;
/*      */       }
/* 1522 */       appendOp(3, 25);
/*      */ 
/* 1524 */       this.m_ops.setOp(opPos + 1 + 1, funcTok);
/*      */ 
/* 1527 */       nextToken();
/*      */     }
/*      */ 
/* 1530 */     consumeExpected('(');
/*      */ 
/* 1532 */     while ((!tokenIs(')')) && (this.m_token != null))
/*      */     {
/* 1534 */       if (tokenIs(','))
/*      */       {
/* 1536 */         error("ER_FOUND_COMMA_BUT_NO_PRECEDING_ARG", null);
/*      */       }
/*      */ 
/* 1539 */       Argument();
/*      */ 
/* 1541 */       if (!tokenIs(')'))
/*      */       {
/* 1543 */         consumeExpected(',');
/*      */ 
/* 1545 */         if (tokenIs(')'))
/*      */         {
/* 1547 */           error("ER_FOUND_COMMA_BUT_NO_FOLLOWING_ARG", null);
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1553 */     consumeExpected(')');
/*      */ 
/* 1556 */     this.m_ops.setOp(this.m_ops.getOp(1), -1);
/* 1557 */     this.m_ops.setOp(1, this.m_ops.getOp(1) + 1);
/* 1558 */     this.m_ops.setOp(opPos + 1, this.m_ops.getOp(1) - opPos);
/*      */ 
/* 1561 */     return true;
/*      */   }
/*      */ 
/*      */   protected void LocationPath()
/*      */     throws TransformerException
/*      */   {
/* 1577 */     int opPos = this.m_ops.getOp(1);
/*      */ 
/* 1580 */     appendOp(2, 28);
/*      */ 
/* 1582 */     boolean seenSlash = tokenIs('/');
/*      */ 
/* 1584 */     if (seenSlash)
/*      */     {
/* 1586 */       appendOp(4, 50);
/*      */ 
/* 1589 */       this.m_ops.setOp(this.m_ops.getOp(1) - 2, 4);
/* 1590 */       this.m_ops.setOp(this.m_ops.getOp(1) - 1, 35);
/*      */ 
/* 1592 */       nextToken();
/* 1593 */     } else if (this.m_token == null) {
/* 1594 */       error("ER_EXPECTED_LOC_PATH_AT_END_EXPR", null);
/*      */     }
/*      */ 
/* 1597 */     if (this.m_token != null)
/*      */     {
/* 1599 */       if ((!RelativeLocationPath()) && (!seenSlash))
/*      */       {
/* 1603 */         error("ER_EXPECTED_LOC_PATH", new Object[] { this.m_token });
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1609 */     this.m_ops.setOp(this.m_ops.getOp(1), -1);
/* 1610 */     this.m_ops.setOp(1, this.m_ops.getOp(1) + 1);
/* 1611 */     this.m_ops.setOp(opPos + 1, this.m_ops.getOp(1) - opPos);
/*      */   }
/*      */ 
/*      */   protected boolean RelativeLocationPath()
/*      */     throws TransformerException
/*      */   {
/* 1628 */     if (!Step())
/*      */     {
/* 1630 */       return false;
/*      */     }
/*      */ 
/* 1633 */     while (tokenIs('/'))
/*      */     {
/* 1635 */       nextToken();
/*      */ 
/* 1637 */       if (!Step())
/*      */       {
/* 1641 */         error("ER_EXPECTED_LOC_STEP", null);
/*      */       }
/*      */     }
/*      */ 
/* 1645 */     return true;
/*      */   }
/*      */ 
/*      */   protected boolean Step()
/*      */     throws TransformerException
/*      */   {
/* 1659 */     int opPos = this.m_ops.getOp(1);
/*      */ 
/* 1661 */     boolean doubleSlash = tokenIs('/');
/*      */ 
/* 1666 */     if (doubleSlash)
/*      */     {
/* 1668 */       nextToken();
/*      */ 
/* 1670 */       appendOp(2, 42);
/*      */ 
/* 1678 */       this.m_ops.setOp(1, this.m_ops.getOp(1) + 1);
/* 1679 */       this.m_ops.setOp(this.m_ops.getOp(1), 1033);
/* 1680 */       this.m_ops.setOp(1, this.m_ops.getOp(1) + 1);
/*      */ 
/* 1683 */       this.m_ops.setOp(opPos + 1 + 1, this.m_ops.getOp(1) - opPos);
/*      */ 
/* 1687 */       this.m_ops.setOp(opPos + 1, this.m_ops.getOp(1) - opPos);
/*      */ 
/* 1690 */       opPos = this.m_ops.getOp(1);
/*      */     }
/*      */ 
/* 1693 */     if (tokenIs("."))
/*      */     {
/* 1695 */       nextToken();
/*      */ 
/* 1697 */       if (tokenIs('['))
/*      */       {
/* 1699 */         error("ER_PREDICATE_ILLEGAL_SYNTAX", null);
/*      */       }
/*      */ 
/* 1702 */       appendOp(4, 48);
/*      */ 
/* 1705 */       this.m_ops.setOp(this.m_ops.getOp(1) - 2, 4);
/* 1706 */       this.m_ops.setOp(this.m_ops.getOp(1) - 1, 1033);
/*      */     }
/* 1708 */     else if (tokenIs(".."))
/*      */     {
/* 1710 */       nextToken();
/* 1711 */       appendOp(4, 45);
/*      */ 
/* 1714 */       this.m_ops.setOp(this.m_ops.getOp(1) - 2, 4);
/* 1715 */       this.m_ops.setOp(this.m_ops.getOp(1) - 1, 1033);
/*      */     }
/* 1721 */     else if ((tokenIs('*')) || (tokenIs('@')) || (tokenIs('_')) || ((this.m_token != null) && (Character.isLetter(this.m_token.charAt(0)))))
/*      */     {
/* 1724 */       Basis();
/*      */ 
/* 1726 */       while (tokenIs('['))
/*      */       {
/* 1728 */         Predicate();
/*      */       }
/*      */ 
/* 1732 */       this.m_ops.setOp(opPos + 1, this.m_ops.getOp(1) - opPos);
/*      */     }
/*      */     else
/*      */     {
/* 1738 */       if (doubleSlash)
/*      */       {
/* 1741 */         error("ER_EXPECTED_LOC_STEP", null);
/*      */       }
/*      */ 
/* 1744 */       return false;
/*      */     }
/*      */ 
/* 1747 */     return true;
/*      */   }
/*      */ 
/*      */   protected void Basis()
/*      */     throws TransformerException
/*      */   {
/* 1760 */     int opPos = this.m_ops.getOp(1);
/*      */     int axesType;
/* 1764 */     if (lookahead("::", 1))
/*      */     {
/* 1766 */       int axesType = AxisName();
/*      */ 
/* 1768 */       nextToken();
/* 1769 */       nextToken();
/*      */     }
/* 1771 */     else if (tokenIs('@'))
/*      */     {
/* 1773 */       int axesType = 39;
/*      */ 
/* 1775 */       appendOp(2, axesType);
/* 1776 */       nextToken();
/*      */     }
/*      */     else
/*      */     {
/* 1780 */       axesType = 40;
/*      */ 
/* 1782 */       appendOp(2, axesType);
/*      */     }
/*      */ 
/* 1786 */     this.m_ops.setOp(1, this.m_ops.getOp(1) + 1);
/*      */ 
/* 1788 */     NodeTest(axesType);
/*      */ 
/* 1791 */     this.m_ops.setOp(opPos + 1 + 1, this.m_ops.getOp(1) - opPos);
/*      */   }
/*      */ 
/*      */   protected int AxisName()
/*      */     throws TransformerException
/*      */   {
/* 1807 */     Object val = Keywords.getAxisName(this.m_token);
/*      */ 
/* 1809 */     if (null == val)
/*      */     {
/* 1811 */       error("ER_ILLEGAL_AXIS_NAME", new Object[] { this.m_token });
/*      */     }
/*      */ 
/* 1815 */     int axesType = ((Integer)val).intValue();
/*      */ 
/* 1817 */     appendOp(2, axesType);
/*      */ 
/* 1819 */     return axesType;
/*      */   }
/*      */ 
/*      */   protected void NodeTest(int axesType)
/*      */     throws TransformerException
/*      */   {
/* 1835 */     if (lookahead('(', 1))
/*      */     {
/* 1837 */       Object nodeTestOp = Keywords.getNodeType(this.m_token);
/*      */ 
/* 1839 */       if (null == nodeTestOp)
/*      */       {
/* 1841 */         error("ER_UNKNOWN_NODETYPE", new Object[] { this.m_token });
/*      */       }
/*      */       else
/*      */       {
/* 1846 */         nextToken();
/*      */ 
/* 1848 */         int nt = ((Integer)nodeTestOp).intValue();
/*      */ 
/* 1850 */         this.m_ops.setOp(this.m_ops.getOp(1), nt);
/* 1851 */         this.m_ops.setOp(1, this.m_ops.getOp(1) + 1);
/*      */ 
/* 1853 */         consumeExpected('(');
/*      */ 
/* 1855 */         if (1032 == nt)
/*      */         {
/* 1857 */           if (!tokenIs(')'))
/*      */           {
/* 1859 */             Literal();
/*      */           }
/*      */         }
/*      */ 
/* 1863 */         consumeExpected(')');
/*      */       }
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/* 1870 */       this.m_ops.setOp(this.m_ops.getOp(1), 34);
/* 1871 */       this.m_ops.setOp(1, this.m_ops.getOp(1) + 1);
/*      */ 
/* 1873 */       if (lookahead(':', 1))
/*      */       {
/* 1875 */         if (tokenIs('*'))
/*      */         {
/* 1877 */           this.m_ops.setOp(this.m_ops.getOp(1), -3);
/*      */         }
/*      */         else
/*      */         {
/* 1881 */           this.m_ops.setOp(this.m_ops.getOp(1), this.m_queueMark - 1);
/*      */ 
/* 1885 */           if ((!Character.isLetter(this.m_tokenChar)) && (!tokenIs('_')))
/*      */           {
/* 1888 */             error("ER_EXPECTED_NODE_TEST", null);
/*      */           }
/*      */         }
/*      */ 
/* 1892 */         nextToken();
/* 1893 */         consumeExpected(':');
/*      */       }
/*      */       else
/*      */       {
/* 1897 */         this.m_ops.setOp(this.m_ops.getOp(1), -2);
/*      */       }
/*      */ 
/* 1900 */       this.m_ops.setOp(1, this.m_ops.getOp(1) + 1);
/*      */ 
/* 1902 */       if (tokenIs('*'))
/*      */       {
/* 1904 */         this.m_ops.setOp(this.m_ops.getOp(1), -3);
/*      */       }
/*      */       else
/*      */       {
/* 1908 */         this.m_ops.setOp(this.m_ops.getOp(1), this.m_queueMark - 1);
/*      */ 
/* 1912 */         if ((!Character.isLetter(this.m_tokenChar)) && (!tokenIs('_')))
/*      */         {
/* 1915 */           error("ER_EXPECTED_NODE_TEST", null);
/*      */         }
/*      */       }
/*      */ 
/* 1919 */       this.m_ops.setOp(1, this.m_ops.getOp(1) + 1);
/*      */ 
/* 1921 */       nextToken();
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void Predicate()
/*      */     throws TransformerException
/*      */   {
/* 1935 */     if (tokenIs('['))
/*      */     {
/* 1937 */       nextToken();
/* 1938 */       PredicateExpr();
/* 1939 */       consumeExpected(']');
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void PredicateExpr()
/*      */     throws TransformerException
/*      */   {
/* 1953 */     int opPos = this.m_ops.getOp(1);
/*      */ 
/* 1955 */     appendOp(2, 29);
/* 1956 */     Expr();
/*      */ 
/* 1959 */     this.m_ops.setOp(this.m_ops.getOp(1), -1);
/* 1960 */     this.m_ops.setOp(1, this.m_ops.getOp(1) + 1);
/* 1961 */     this.m_ops.setOp(opPos + 1, this.m_ops.getOp(1) - opPos);
/*      */   }
/*      */ 
/*      */   protected void QName()
/*      */     throws TransformerException
/*      */   {
/* 1975 */     if (lookahead(':', 1))
/*      */     {
/* 1977 */       this.m_ops.setOp(this.m_ops.getOp(1), this.m_queueMark - 1);
/* 1978 */       this.m_ops.setOp(1, this.m_ops.getOp(1) + 1);
/*      */ 
/* 1980 */       nextToken();
/* 1981 */       consumeExpected(':');
/*      */     }
/*      */     else
/*      */     {
/* 1985 */       this.m_ops.setOp(this.m_ops.getOp(1), -2);
/* 1986 */       this.m_ops.setOp(1, this.m_ops.getOp(1) + 1);
/*      */     }
/*      */ 
/* 1990 */     this.m_ops.setOp(this.m_ops.getOp(1), this.m_queueMark - 1);
/* 1991 */     this.m_ops.setOp(1, this.m_ops.getOp(1) + 1);
/*      */ 
/* 1993 */     nextToken();
/*      */   }
/*      */ 
/*      */   protected void NCName()
/*      */   {
/* 2003 */     this.m_ops.setOp(this.m_ops.getOp(1), this.m_queueMark - 1);
/* 2004 */     this.m_ops.setOp(1, this.m_ops.getOp(1) + 1);
/*      */ 
/* 2006 */     nextToken();
/*      */   }
/*      */ 
/*      */   protected void Literal()
/*      */     throws TransformerException
/*      */   {
/* 2022 */     int last = this.m_token.length() - 1;
/* 2023 */     char c0 = this.m_tokenChar;
/* 2024 */     char cX = this.m_token.charAt(last);
/*      */ 
/* 2026 */     if (((c0 == '"') && (cX == '"')) || ((c0 == '\'') && (cX == '\'')))
/*      */     {
/* 2031 */       int tokenQueuePos = this.m_queueMark - 1;
/*      */ 
/* 2033 */       this.m_ops.m_tokenQueue.setElementAt(null, tokenQueuePos);
/*      */ 
/* 2035 */       Object obj = new XString(this.m_token.substring(1, last));
/*      */ 
/* 2037 */       this.m_ops.m_tokenQueue.setElementAt(obj, tokenQueuePos);
/*      */ 
/* 2040 */       this.m_ops.setOp(this.m_ops.getOp(1), tokenQueuePos);
/* 2041 */       this.m_ops.setOp(1, this.m_ops.getOp(1) + 1);
/*      */ 
/* 2043 */       nextToken();
/*      */     }
/*      */     else
/*      */     {
/* 2047 */       error("ER_PATTERN_LITERAL_NEEDS_BE_QUOTED", new Object[] { this.m_token });
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void Number()
/*      */     throws TransformerException
/*      */   {
/* 2062 */     if (null != this.m_token)
/*      */     {
/*      */       double num;
/*      */       try
/*      */       {
/* 2072 */         if ((this.m_token.indexOf('e') > -1) || (this.m_token.indexOf('E') > -1))
/* 2073 */           throw new NumberFormatException();
/* 2074 */         num = Double.valueOf(this.m_token).doubleValue();
/*      */       }
/*      */       catch (NumberFormatException nfe)
/*      */       {
/* 2078 */         num = 0.0D;
/*      */ 
/* 2080 */         error("ER_COULDNOT_BE_FORMATTED_TO_NUMBER", new Object[] { this.m_token });
/*      */       }
/*      */ 
/* 2084 */       this.m_ops.m_tokenQueue.setElementAt(new XNumber(num), this.m_queueMark - 1);
/* 2085 */       this.m_ops.setOp(this.m_ops.getOp(1), this.m_queueMark - 1);
/* 2086 */       this.m_ops.setOp(1, this.m_ops.getOp(1) + 1);
/*      */ 
/* 2088 */       nextToken();
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void Pattern()
/*      */     throws TransformerException
/*      */   {
/*      */     while (true)
/*      */     {
/* 2107 */       LocationPathPattern();
/*      */ 
/* 2109 */       if (!tokenIs('|'))
/*      */         break;
/* 2111 */       nextToken();
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void LocationPathPattern()
/*      */     throws TransformerException
/*      */   {
/* 2133 */     int opPos = this.m_ops.getOp(1);
/*      */ 
/* 2135 */     int RELATIVE_PATH_NOT_PERMITTED = 0;
/* 2136 */     int RELATIVE_PATH_PERMITTED = 1;
/* 2137 */     int RELATIVE_PATH_REQUIRED = 2;
/*      */ 
/* 2139 */     int relativePathStatus = 0;
/*      */ 
/* 2141 */     appendOp(2, 31);
/*      */ 
/* 2143 */     if ((lookahead('(', 1)) && ((tokenIs("id")) || (tokenIs("key"))))
/*      */     {
/* 2147 */       IdKeyPattern();
/*      */ 
/* 2149 */       if (tokenIs('/'))
/*      */       {
/* 2151 */         nextToken();
/*      */ 
/* 2153 */         if (tokenIs('/'))
/*      */         {
/* 2155 */           appendOp(4, 52);
/*      */ 
/* 2157 */           nextToken();
/*      */         }
/*      */         else
/*      */         {
/* 2161 */           appendOp(4, 53);
/*      */         }
/*      */ 
/* 2165 */         this.m_ops.setOp(this.m_ops.getOp(1) - 2, 4);
/* 2166 */         this.m_ops.setOp(this.m_ops.getOp(1) - 1, 1034);
/*      */ 
/* 2168 */         relativePathStatus = 2;
/*      */       }
/*      */     }
/* 2171 */     else if (tokenIs('/'))
/*      */     {
/* 2173 */       if (lookahead('/', 1))
/*      */       {
/* 2175 */         appendOp(4, 52);
/*      */ 
/* 2181 */         nextToken();
/*      */ 
/* 2183 */         relativePathStatus = 2;
/*      */       }
/*      */       else
/*      */       {
/* 2187 */         appendOp(4, 50);
/*      */ 
/* 2189 */         relativePathStatus = 1;
/*      */       }
/*      */ 
/* 2194 */       this.m_ops.setOp(this.m_ops.getOp(1) - 2, 4);
/* 2195 */       this.m_ops.setOp(this.m_ops.getOp(1) - 1, 35);
/*      */ 
/* 2197 */       nextToken();
/*      */     }
/*      */     else
/*      */     {
/* 2201 */       relativePathStatus = 2;
/*      */     }
/*      */ 
/* 2204 */     if (relativePathStatus != 0)
/*      */     {
/* 2206 */       if ((!tokenIs('|')) && (null != this.m_token))
/*      */       {
/* 2208 */         RelativePathPattern();
/*      */       }
/* 2210 */       else if (relativePathStatus == 2)
/*      */       {
/* 2213 */         error("ER_EXPECTED_REL_PATH_PATTERN", null);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2218 */     this.m_ops.setOp(this.m_ops.getOp(1), -1);
/* 2219 */     this.m_ops.setOp(1, this.m_ops.getOp(1) + 1);
/* 2220 */     this.m_ops.setOp(opPos + 1, this.m_ops.getOp(1) - opPos);
/*      */   }
/*      */ 
/*      */   protected void IdKeyPattern()
/*      */     throws TransformerException
/*      */   {
/* 2235 */     FunctionCall();
/*      */   }
/*      */ 
/*      */   protected void RelativePathPattern()
/*      */     throws TransformerException
/*      */   {
/* 2252 */     boolean trailingSlashConsumed = StepPattern(false);
/*      */ 
/* 2254 */     while (tokenIs('/'))
/*      */     {
/* 2256 */       nextToken();
/*      */ 
/* 2261 */       trailingSlashConsumed = StepPattern(!trailingSlashConsumed);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected boolean StepPattern(boolean isLeadingSlashPermitted)
/*      */     throws TransformerException
/*      */   {
/* 2279 */     return AbbreviatedNodeTestStep(isLeadingSlashPermitted);
/*      */   }
/*      */ 
/*      */   protected boolean AbbreviatedNodeTestStep(boolean isLeadingSlashPermitted)
/*      */     throws TransformerException
/*      */   {
/* 2297 */     int opPos = this.m_ops.getOp(1);
/*      */ 
/* 2301 */     int matchTypePos = -1;
/*      */     int axesType;
/* 2303 */     if (tokenIs('@'))
/*      */     {
/* 2305 */       int axesType = 51;
/*      */ 
/* 2307 */       appendOp(2, axesType);
/* 2308 */       nextToken();
/*      */     }
/* 2310 */     else if (lookahead("::", 1))
/*      */     {
/* 2312 */       if (tokenIs("attribute"))
/*      */       {
/* 2314 */         int axesType = 51;
/*      */ 
/* 2316 */         appendOp(2, axesType);
/*      */       }
/* 2318 */       else if (tokenIs("child"))
/*      */       {
/* 2320 */         matchTypePos = this.m_ops.getOp(1);
/* 2321 */         int axesType = 53;
/*      */ 
/* 2323 */         appendOp(2, axesType);
/*      */       }
/*      */       else
/*      */       {
/* 2327 */         int axesType = -1;
/*      */ 
/* 2329 */         error("ER_AXES_NOT_ALLOWED", new Object[] { this.m_token });
/*      */       }
/*      */ 
/* 2333 */       nextToken();
/* 2334 */       nextToken();
/*      */     }
/* 2336 */     else if (tokenIs('/'))
/*      */     {
/* 2338 */       if (!isLeadingSlashPermitted)
/*      */       {
/* 2341 */         error("ER_EXPECTED_STEP_PATTERN", null);
/*      */       }
/* 2343 */       int axesType = 52;
/*      */ 
/* 2345 */       appendOp(2, axesType);
/* 2346 */       nextToken();
/*      */     }
/*      */     else
/*      */     {
/* 2350 */       matchTypePos = this.m_ops.getOp(1);
/* 2351 */       axesType = 53;
/*      */ 
/* 2353 */       appendOp(2, axesType);
/*      */     }
/*      */ 
/* 2357 */     this.m_ops.setOp(1, this.m_ops.getOp(1) + 1);
/*      */ 
/* 2359 */     NodeTest(axesType);
/*      */ 
/* 2362 */     this.m_ops.setOp(opPos + 1 + 1, this.m_ops.getOp(1) - opPos);
/*      */ 
/* 2365 */     while (tokenIs('['))
/*      */     {
/* 2367 */       Predicate();
/*      */     }
/*      */     boolean trailingSlashConsumed;
/*      */     boolean trailingSlashConsumed;
/* 2384 */     if ((matchTypePos > -1) && (tokenIs('/')) && (lookahead('/', 1)))
/*      */     {
/* 2386 */       this.m_ops.setOp(matchTypePos, 52);
/*      */ 
/* 2388 */       nextToken();
/*      */ 
/* 2390 */       trailingSlashConsumed = true;
/*      */     }
/*      */     else
/*      */     {
/* 2394 */       trailingSlashConsumed = false;
/*      */     }
/*      */ 
/* 2398 */     this.m_ops.setOp(opPos + 1, this.m_ops.getOp(1) - opPos);
/*      */ 
/* 2401 */     return trailingSlashConsumed;
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.compiler.XPathParser
 * JD-Core Version:    0.6.2
 */