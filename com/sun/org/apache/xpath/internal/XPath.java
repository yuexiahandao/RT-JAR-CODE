/*     */ package com.sun.org.apache.xpath.internal;
/*     */ 
/*     */ import com.sun.org.apache.xalan.internal.res.XSLMessages;
/*     */ import com.sun.org.apache.xml.internal.utils.DefaultErrorHandler;
/*     */ import com.sun.org.apache.xml.internal.utils.PrefixResolver;
/*     */ import com.sun.org.apache.xml.internal.utils.SAXSourceLocator;
/*     */ import com.sun.org.apache.xml.internal.utils.WrappedRuntimeException;
/*     */ import com.sun.org.apache.xpath.internal.compiler.Compiler;
/*     */ import com.sun.org.apache.xpath.internal.compiler.FunctionTable;
/*     */ import com.sun.org.apache.xpath.internal.compiler.XPathParser;
/*     */ import com.sun.org.apache.xpath.internal.objects.XObject;
/*     */ import java.io.PrintStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.Vector;
/*     */ import javax.xml.transform.ErrorListener;
/*     */ import javax.xml.transform.SourceLocator;
/*     */ import javax.xml.transform.TransformerException;
/*     */ import org.w3c.dom.Node;
/*     */ 
/*     */ public class XPath
/*     */   implements Serializable, ExpressionOwner
/*     */ {
/*     */   static final long serialVersionUID = 3976493477939110553L;
/*     */   private Expression m_mainExp;
/*  58 */   private transient FunctionTable m_funcTable = null;
/*     */   String m_patternString;
/*     */   public static final int SELECT = 0;
/*     */   public static final int MATCH = 1;
/*     */   private static final boolean DEBUG_MATCHES = false;
/*     */   public static final double MATCH_SCORE_NONE = (-1.0D / 0.0D);
/*     */   public static final double MATCH_SCORE_QNAME = 0.0D;
/*     */   public static final double MATCH_SCORE_NSWILD = -0.25D;
/*     */   public static final double MATCH_SCORE_NODETEST = -0.5D;
/*     */   public static final double MATCH_SCORE_OTHER = 0.5D;
/*     */ 
/*     */   private void initFunctionTable()
/*     */   {
/*  64 */     this.m_funcTable = new FunctionTable();
/*     */   }
/*     */ 
/*     */   public Expression getExpression()
/*     */   {
/*  75 */     return this.m_mainExp;
/*     */   }
/*     */ 
/*     */   public void fixupVariables(Vector vars, int globalsSize)
/*     */   {
/*  90 */     this.m_mainExp.fixupVariables(vars, globalsSize);
/*     */   }
/*     */ 
/*     */   public void setExpression(Expression exp)
/*     */   {
/* 101 */     if (null != this.m_mainExp)
/* 102 */       exp.exprSetParent(this.m_mainExp.exprGetParent());
/* 103 */     this.m_mainExp = exp;
/*     */   }
/*     */ 
/*     */   public SourceLocator getLocator()
/*     */   {
/* 114 */     return this.m_mainExp;
/*     */   }
/*     */ 
/*     */   public String getPatternString()
/*     */   {
/* 142 */     return this.m_patternString;
/*     */   }
/*     */ 
/*     */   public XPath(String exprString, SourceLocator locator, PrefixResolver prefixResolver, int type, ErrorListener errorListener)
/*     */     throws TransformerException
/*     */   {
/* 170 */     initFunctionTable();
/* 171 */     if (null == errorListener) {
/* 172 */       errorListener = new DefaultErrorHandler();
/*     */     }
/* 174 */     this.m_patternString = exprString;
/*     */ 
/* 176 */     XPathParser parser = new XPathParser(errorListener, locator);
/* 177 */     Compiler compiler = new Compiler(errorListener, locator, this.m_funcTable);
/*     */ 
/* 179 */     if (0 == type)
/* 180 */       parser.initXPath(compiler, exprString, prefixResolver);
/* 181 */     else if (1 == type)
/* 182 */       parser.initMatchPattern(compiler, exprString, prefixResolver);
/*     */     else {
/* 184 */       throw new RuntimeException(XSLMessages.createXPATHMessage("ER_CANNOT_DEAL_XPATH_TYPE", new Object[] { Integer.toString(type) }));
/*     */     }
/*     */ 
/* 187 */     Expression expr = compiler.compile(0);
/*     */ 
/* 190 */     setExpression(expr);
/*     */ 
/* 192 */     if ((null != locator) && ((locator instanceof ExpressionNode)))
/*     */     {
/* 194 */       expr.exprSetParent((ExpressionNode)locator);
/*     */     }
/*     */   }
/*     */ 
/*     */   public XPath(String exprString, SourceLocator locator, PrefixResolver prefixResolver, int type, ErrorListener errorListener, FunctionTable aTable)
/*     */     throws TransformerException
/*     */   {
/* 219 */     this.m_funcTable = aTable;
/* 220 */     if (null == errorListener) {
/* 221 */       errorListener = new DefaultErrorHandler();
/*     */     }
/* 223 */     this.m_patternString = exprString;
/*     */ 
/* 225 */     XPathParser parser = new XPathParser(errorListener, locator);
/* 226 */     Compiler compiler = new Compiler(errorListener, locator, this.m_funcTable);
/*     */ 
/* 228 */     if (0 == type)
/* 229 */       parser.initXPath(compiler, exprString, prefixResolver);
/* 230 */     else if (1 == type)
/* 231 */       parser.initMatchPattern(compiler, exprString, prefixResolver);
/*     */     else {
/* 233 */       throw new RuntimeException(XSLMessages.createXPATHMessage("ER_CANNOT_DEAL_XPATH_TYPE", new Object[] { Integer.toString(type) }));
/*     */     }
/*     */ 
/* 239 */     Expression expr = compiler.compile(0);
/*     */ 
/* 242 */     setExpression(expr);
/*     */ 
/* 244 */     if ((null != locator) && ((locator instanceof ExpressionNode)))
/*     */     {
/* 246 */       expr.exprSetParent((ExpressionNode)locator);
/*     */     }
/*     */   }
/*     */ 
/*     */   public XPath(String exprString, SourceLocator locator, PrefixResolver prefixResolver, int type)
/*     */     throws TransformerException
/*     */   {
/* 268 */     this(exprString, locator, prefixResolver, type, null);
/*     */   }
/*     */ 
/*     */   public XPath(Expression expr)
/*     */   {
/* 280 */     setExpression(expr);
/* 281 */     initFunctionTable();
/*     */   }
/*     */ 
/*     */   public XObject execute(XPathContext xctxt, Node contextNode, PrefixResolver namespaceContext)
/*     */     throws TransformerException
/*     */   {
/* 305 */     return execute(xctxt, xctxt.getDTMHandleFromNode(contextNode), namespaceContext);
/*     */   }
/*     */ 
/*     */   public XObject execute(XPathContext xctxt, int contextNode, PrefixResolver namespaceContext)
/*     */     throws TransformerException
/*     */   {
/* 331 */     xctxt.pushNamespaceContext(namespaceContext);
/*     */ 
/* 333 */     xctxt.pushCurrentNodeAndExpression(contextNode, contextNode);
/*     */ 
/* 335 */     XObject xobj = null;
/*     */     try
/*     */     {
/* 339 */       xobj = this.m_mainExp.execute(xctxt);
/*     */     }
/*     */     catch (TransformerException te)
/*     */     {
/* 343 */       te.setLocator(getLocator());
/* 344 */       ErrorListener el = xctxt.getErrorListener();
/* 345 */       if (null != el)
/*     */       {
/* 347 */         el.error(te);
/*     */       }
/*     */       else
/* 350 */         throw te;
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 354 */       while ((e instanceof WrappedRuntimeException))
/*     */       {
/* 356 */         e = ((WrappedRuntimeException)e).getException();
/*     */       }
/*     */ 
/* 360 */       String msg = e.getMessage();
/*     */ 
/* 362 */       if ((msg == null) || (msg.length() == 0)) {
/* 363 */         msg = XSLMessages.createXPATHMessage("ER_XPATH_ERROR", null);
/*     */       }
/*     */ 
/* 367 */       TransformerException te = new TransformerException(msg, getLocator(), e);
/*     */ 
/* 369 */       ErrorListener el = xctxt.getErrorListener();
/*     */ 
/* 371 */       if (null != el)
/*     */       {
/* 373 */         el.fatalError(te);
/*     */       }
/*     */       else
/* 376 */         throw te;
/*     */     }
/*     */     finally
/*     */     {
/* 380 */       xctxt.popNamespaceContext();
/*     */ 
/* 382 */       xctxt.popCurrentNodeAndExpression();
/*     */     }
/*     */ 
/* 385 */     return xobj;
/*     */   }
/*     */ 
/*     */   public boolean bool(XPathContext xctxt, int contextNode, PrefixResolver namespaceContext)
/*     */     throws TransformerException
/*     */   {
/* 408 */     xctxt.pushNamespaceContext(namespaceContext);
/*     */ 
/* 410 */     xctxt.pushCurrentNodeAndExpression(contextNode, contextNode);
/*     */     try
/*     */     {
/* 414 */       return this.m_mainExp.bool(xctxt);
/*     */     }
/*     */     catch (TransformerException te)
/*     */     {
/* 418 */       te.setLocator(getLocator());
/* 419 */       ErrorListener el = xctxt.getErrorListener();
/* 420 */       if (null != el)
/*     */       {
/* 422 */         el.error(te);
/*     */       }
/*     */       else
/* 425 */         throw te;
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 429 */       while ((e instanceof WrappedRuntimeException))
/*     */       {
/* 431 */         e = ((WrappedRuntimeException)e).getException();
/*     */       }
/*     */ 
/* 435 */       String msg = e.getMessage();
/*     */ 
/* 437 */       if ((msg == null) || (msg.length() == 0)) {
/* 438 */         msg = XSLMessages.createXPATHMessage("ER_XPATH_ERROR", null);
/*     */       }
/*     */ 
/* 443 */       TransformerException te = new TransformerException(msg, getLocator(), e);
/*     */ 
/* 445 */       ErrorListener el = xctxt.getErrorListener();
/*     */ 
/* 447 */       if (null != el)
/*     */       {
/* 449 */         el.fatalError(te);
/*     */       }
/*     */       else
/* 452 */         throw te;
/*     */     }
/*     */     finally
/*     */     {
/* 456 */       xctxt.popNamespaceContext();
/*     */ 
/* 458 */       xctxt.popCurrentNodeAndExpression();
/*     */     }
/*     */ 
/* 461 */     return false;
/*     */   }
/*     */ 
/*     */   public double getMatchScore(XPathContext xctxt, int context)
/*     */     throws TransformerException
/*     */   {
/* 484 */     xctxt.pushCurrentNode(context);
/* 485 */     xctxt.pushCurrentExpressionNode(context);
/*     */     try
/*     */     {
/* 489 */       XObject score = this.m_mainExp.execute(xctxt);
/*     */ 
/* 499 */       return score.num();
/*     */     }
/*     */     finally
/*     */     {
/* 503 */       xctxt.popCurrentNode();
/* 504 */       xctxt.popCurrentExpressionNode();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void warn(XPathContext xctxt, int sourceNode, String msg, Object[] args)
/*     */     throws TransformerException
/*     */   {
/* 530 */     String fmsg = XSLMessages.createXPATHWarning(msg, args);
/* 531 */     ErrorListener ehandler = xctxt.getErrorListener();
/*     */ 
/* 533 */     if (null != ehandler)
/*     */     {
/* 537 */       ehandler.warning(new TransformerException(fmsg, (SAXSourceLocator)xctxt.getSAXLocator()));
/*     */     }
/*     */   }
/*     */ 
/*     */   public void assertion(boolean b, String msg)
/*     */   {
/* 553 */     if (!b)
/*     */     {
/* 555 */       String fMsg = XSLMessages.createXPATHMessage("ER_INCORRECT_PROGRAMMER_ASSERTION", new Object[] { msg });
/*     */ 
/* 559 */       throw new RuntimeException(fMsg);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void error(XPathContext xctxt, int sourceNode, String msg, Object[] args)
/*     */     throws TransformerException
/*     */   {
/* 583 */     String fmsg = XSLMessages.createXPATHMessage(msg, args);
/* 584 */     ErrorListener ehandler = xctxt.getErrorListener();
/*     */ 
/* 586 */     if (null != ehandler)
/*     */     {
/* 588 */       ehandler.fatalError(new TransformerException(fmsg, (SAXSourceLocator)xctxt.getSAXLocator()));
/*     */     }
/*     */     else
/*     */     {
/* 593 */       SourceLocator slocator = xctxt.getSAXLocator();
/* 594 */       System.out.println(fmsg + "; file " + slocator.getSystemId() + "; line " + slocator.getLineNumber() + "; column " + slocator.getColumnNumber());
/*     */     }
/*     */   }
/*     */ 
/*     */   public void callVisitors(ExpressionOwner owner, XPathVisitor visitor)
/*     */   {
/* 611 */     this.m_mainExp.callVisitors(this, visitor);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.XPath
 * JD-Core Version:    0.6.2
 */