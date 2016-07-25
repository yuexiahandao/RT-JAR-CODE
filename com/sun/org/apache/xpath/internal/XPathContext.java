/*      */ package com.sun.org.apache.xpath.internal;
/*      */ 
/*      */ import com.sun.org.apache.xalan.internal.extensions.ExpressionContext;
/*      */ import com.sun.org.apache.xalan.internal.res.XSLMessages;
/*      */ import com.sun.org.apache.xml.internal.dtm.DTM;
/*      */ import com.sun.org.apache.xml.internal.dtm.DTMFilter;
/*      */ import com.sun.org.apache.xml.internal.dtm.DTMIterator;
/*      */ import com.sun.org.apache.xml.internal.dtm.DTMManager;
/*      */ import com.sun.org.apache.xml.internal.dtm.DTMWSFilter;
/*      */ import com.sun.org.apache.xml.internal.dtm.ref.DTMNodeIterator;
/*      */ import com.sun.org.apache.xml.internal.dtm.ref.sax2dtm.SAX2RTFDTM;
/*      */ import com.sun.org.apache.xml.internal.utils.DefaultErrorHandler;
/*      */ import com.sun.org.apache.xml.internal.utils.IntStack;
/*      */ import com.sun.org.apache.xml.internal.utils.NodeVector;
/*      */ import com.sun.org.apache.xml.internal.utils.ObjectStack;
/*      */ import com.sun.org.apache.xml.internal.utils.PrefixResolver;
/*      */ import com.sun.org.apache.xml.internal.utils.QName;
/*      */ import com.sun.org.apache.xml.internal.utils.SAXSourceLocator;
/*      */ import com.sun.org.apache.xml.internal.utils.XMLString;
/*      */ import com.sun.org.apache.xpath.internal.axes.OneStepIteratorForward;
/*      */ import com.sun.org.apache.xpath.internal.axes.SubContextList;
/*      */ import com.sun.org.apache.xpath.internal.objects.DTMXRTreeFrag;
/*      */ import com.sun.org.apache.xpath.internal.objects.XMLStringFactoryImpl;
/*      */ import com.sun.org.apache.xpath.internal.objects.XObject;
/*      */ import com.sun.org.apache.xpath.internal.objects.XString;
/*      */ import java.io.PrintStream;
/*      */ import java.lang.reflect.Method;
/*      */ import java.util.Collection;
/*      */ import java.util.Enumeration;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.Stack;
/*      */ import java.util.Vector;
/*      */ import javax.xml.transform.ErrorListener;
/*      */ import javax.xml.transform.Source;
/*      */ import javax.xml.transform.SourceLocator;
/*      */ import javax.xml.transform.TransformerException;
/*      */ import javax.xml.transform.URIResolver;
/*      */ import org.w3c.dom.Node;
/*      */ import org.w3c.dom.traversal.NodeIterator;
/*      */ import org.xml.sax.XMLReader;
/*      */ 
/*      */ public class XPathContext extends DTMManager
/*      */ {
/*   67 */   IntStack m_last_pushed_rtfdtm = new IntStack();
/*      */ 
/*   78 */   private Vector m_rtfdtm_stack = null;
/*      */ 
/*   80 */   private int m_which_rtfdtm = -1;
/*      */ 
/*   86 */   private SAX2RTFDTM m_global_rtfdtm = null;
/*      */ 
/*   92 */   private HashMap m_DTMXRTreeFrags = null;
/*      */ 
/*   97 */   private boolean m_isSecureProcessing = false;
/*      */ 
/*   99 */   private boolean m_useServicesMechanism = true;
/*      */ 
/*  106 */   protected DTMManager m_dtmManager = null;
/*      */ 
/*  385 */   ObjectStack m_saxLocations = new ObjectStack(4096);
/*      */   private Object m_owner;
/*      */   private Method m_ownerGetErrorListener;
/*  463 */   private VariableStack m_variableStacks = new VariableStack();
/*      */ 
/*  491 */   private SourceTreeManager m_sourceTreeManager = new SourceTreeManager();
/*      */   private ErrorListener m_errorListener;
/*      */   private ErrorListener m_defaultErrorListener;
/*      */   private URIResolver m_uriResolver;
/*      */   public XMLReader m_primaryReader;
/*  657 */   private Stack m_contextNodeLists = new Stack();
/*      */   public static final int RECURSIONLIMIT = 4096;
/*  711 */   private IntStack m_currentNodes = new IntStack(4096);
/*      */ 
/*  842 */   private NodeVector m_iteratorRoots = new NodeVector();
/*      */ 
/*  845 */   private NodeVector m_predicateRoots = new NodeVector();
/*      */ 
/*  848 */   private IntStack m_currentExpressionNodes = new IntStack(4096);
/*      */ 
/*  854 */   private IntStack m_predicatePos = new IntStack();
/*      */ 
/*  900 */   private ObjectStack m_prefixResolvers = new ObjectStack(4096);
/*      */ 
/*  960 */   private Stack m_axesIteratorStack = new Stack();
/*      */ 
/* 1046 */   XPathExpressionContext expressionContext = new XPathExpressionContext();
/*      */ 
/*      */   public DTMManager getDTMManager()
/*      */   {
/*  116 */     return this.m_dtmManager;
/*      */   }
/*      */ 
/*      */   public void setSecureProcessing(boolean flag)
/*      */   {
/*  124 */     this.m_isSecureProcessing = flag;
/*      */   }
/*      */ 
/*      */   public boolean isSecureProcessing()
/*      */   {
/*  132 */     return this.m_isSecureProcessing;
/*      */   }
/*      */ 
/*      */   public DTM getDTM(Source source, boolean unique, DTMWSFilter wsfilter, boolean incremental, boolean doIndexing)
/*      */   {
/*  161 */     return this.m_dtmManager.getDTM(source, unique, wsfilter, incremental, doIndexing);
/*      */   }
/*      */ 
/*      */   public DTM getDTM(int nodeHandle)
/*      */   {
/*  174 */     return this.m_dtmManager.getDTM(nodeHandle);
/*      */   }
/*      */ 
/*      */   public int getDTMHandleFromNode(Node node)
/*      */   {
/*  187 */     return this.m_dtmManager.getDTMHandleFromNode(node);
/*      */   }
/*      */ 
/*      */   public int getDTMIdentity(DTM dtm)
/*      */   {
/*  196 */     return this.m_dtmManager.getDTMIdentity(dtm);
/*      */   }
/*      */ 
/*      */   public DTM createDocumentFragment()
/*      */   {
/*  205 */     return this.m_dtmManager.createDocumentFragment();
/*      */   }
/*      */ 
/*      */   public boolean release(DTM dtm, boolean shouldHardDelete)
/*      */   {
/*  224 */     if ((this.m_rtfdtm_stack != null) && (this.m_rtfdtm_stack.contains(dtm)))
/*      */     {
/*  226 */       return false;
/*      */     }
/*      */ 
/*  229 */     return this.m_dtmManager.release(dtm, shouldHardDelete);
/*      */   }
/*      */ 
/*      */   public DTMIterator createDTMIterator(Object xpathCompiler, int pos)
/*      */   {
/*  246 */     return this.m_dtmManager.createDTMIterator(xpathCompiler, pos);
/*      */   }
/*      */ 
/*      */   public DTMIterator createDTMIterator(String xpathString, PrefixResolver presolver)
/*      */   {
/*  265 */     return this.m_dtmManager.createDTMIterator(xpathString, presolver);
/*      */   }
/*      */ 
/*      */   public DTMIterator createDTMIterator(int whatToShow, DTMFilter filter, boolean entityReferenceExpansion)
/*      */   {
/*  288 */     return this.m_dtmManager.createDTMIterator(whatToShow, filter, entityReferenceExpansion);
/*      */   }
/*      */ 
/*      */   public DTMIterator createDTMIterator(int node)
/*      */   {
/*  301 */     DTMIterator iter = new OneStepIteratorForward(13);
/*  302 */     iter.setRoot(node, this);
/*  303 */     return iter;
/*      */   }
/*      */ 
/*      */   public XPathContext()
/*      */   {
/*  312 */     this(true);
/*      */   }
/*      */ 
/*      */   public XPathContext(boolean useServicesMechanism) {
/*  316 */     init(useServicesMechanism);
/*      */   }
/*      */ 
/*      */   public XPathContext(Object owner)
/*      */   {
/*  326 */     this.m_owner = owner;
/*      */     try {
/*  328 */       this.m_ownerGetErrorListener = this.m_owner.getClass().getMethod("getErrorListener", new Class[0]);
/*      */     } catch (NoSuchMethodException nsme) {
/*      */     }
/*  331 */     init(true);
/*      */   }
/*      */ 
/*      */   private void init(boolean useServicesMechanism) {
/*  335 */     this.m_prefixResolvers.push(null);
/*  336 */     this.m_currentNodes.push(-1);
/*  337 */     this.m_currentExpressionNodes.push(-1);
/*  338 */     this.m_saxLocations.push(null);
/*  339 */     this.m_useServicesMechanism = useServicesMechanism;
/*  340 */     this.m_dtmManager = DTMManager.newInstance(XMLStringFactoryImpl.getFactory(), this.m_useServicesMechanism);
/*      */   }
/*      */ 
/*      */   public void reset()
/*      */   {
/*  350 */     releaseDTMXRTreeFrags();
/*      */     Enumeration e;
/*  352 */     if (this.m_rtfdtm_stack != null) {
/*  353 */       for (e = this.m_rtfdtm_stack.elements(); e.hasMoreElements(); )
/*  354 */         this.m_dtmManager.release((DTM)e.nextElement(), true);
/*      */     }
/*  356 */     this.m_rtfdtm_stack = null;
/*  357 */     this.m_which_rtfdtm = -1;
/*      */ 
/*  359 */     if (this.m_global_rtfdtm != null)
/*  360 */       this.m_dtmManager.release(this.m_global_rtfdtm, true);
/*  361 */     this.m_global_rtfdtm = null;
/*      */ 
/*  364 */     this.m_dtmManager = DTMManager.newInstance(XMLStringFactoryImpl.getFactory(), this.m_useServicesMechanism);
/*      */ 
/*  368 */     this.m_saxLocations.removeAllElements();
/*  369 */     this.m_axesIteratorStack.removeAllElements();
/*  370 */     this.m_contextNodeLists.removeAllElements();
/*  371 */     this.m_currentExpressionNodes.removeAllElements();
/*  372 */     this.m_currentNodes.removeAllElements();
/*  373 */     this.m_iteratorRoots.RemoveAllNoClear();
/*  374 */     this.m_predicatePos.removeAllElements();
/*  375 */     this.m_predicateRoots.RemoveAllNoClear();
/*  376 */     this.m_prefixResolvers.removeAllElements();
/*      */ 
/*  378 */     this.m_prefixResolvers.push(null);
/*  379 */     this.m_currentNodes.push(-1);
/*  380 */     this.m_currentExpressionNodes.push(-1);
/*  381 */     this.m_saxLocations.push(null);
/*      */   }
/*      */ 
/*      */   public void setSAXLocator(SourceLocator location)
/*      */   {
/*  394 */     this.m_saxLocations.setTop(location);
/*      */   }
/*      */ 
/*      */   public void pushSAXLocator(SourceLocator location)
/*      */   {
/*  404 */     this.m_saxLocations.push(location);
/*      */   }
/*      */ 
/*      */   public void pushSAXLocatorNull()
/*      */   {
/*  414 */     this.m_saxLocations.push(null);
/*      */   }
/*      */ 
/*      */   public void popSAXLocator()
/*      */   {
/*  423 */     this.m_saxLocations.pop();
/*      */   }
/*      */ 
/*      */   public SourceLocator getSAXLocator()
/*      */   {
/*  433 */     return (SourceLocator)this.m_saxLocations.peek();
/*      */   }
/*      */ 
/*      */   public Object getOwnerObject()
/*      */   {
/*  454 */     return this.m_owner;
/*      */   }
/*      */ 
/*      */   public final VariableStack getVarStack()
/*      */   {
/*  473 */     return this.m_variableStacks;
/*      */   }
/*      */ 
/*      */   public final void setVarStack(VariableStack varStack)
/*      */   {
/*  484 */     this.m_variableStacks = varStack;
/*      */   }
/*      */ 
/*      */   public final SourceTreeManager getSourceTreeManager()
/*      */   {
/*  500 */     return this.m_sourceTreeManager;
/*      */   }
/*      */ 
/*      */   public void setSourceTreeManager(SourceTreeManager mgr)
/*      */   {
/*  511 */     this.m_sourceTreeManager = mgr;
/*      */   }
/*      */ 
/*      */   public final ErrorListener getErrorListener()
/*      */   {
/*  532 */     if (null != this.m_errorListener) {
/*  533 */       return this.m_errorListener;
/*      */     }
/*  535 */     ErrorListener retval = null;
/*      */     try
/*      */     {
/*  538 */       if (null != this.m_ownerGetErrorListener)
/*  539 */         retval = (ErrorListener)this.m_ownerGetErrorListener.invoke(this.m_owner, new Object[0]);
/*      */     }
/*      */     catch (Exception e) {
/*      */     }
/*  543 */     if (null == retval)
/*      */     {
/*  545 */       if (null == this.m_defaultErrorListener)
/*  546 */         this.m_defaultErrorListener = new DefaultErrorHandler();
/*  547 */       retval = this.m_defaultErrorListener;
/*      */     }
/*      */ 
/*  550 */     return retval;
/*      */   }
/*      */ 
/*      */   public void setErrorListener(ErrorListener listener)
/*      */     throws IllegalArgumentException
/*      */   {
/*  560 */     if (listener == null)
/*  561 */       throw new IllegalArgumentException(XSLMessages.createXPATHMessage("ER_NULL_ERROR_HANDLER", null));
/*  562 */     this.m_errorListener = listener;
/*      */   }
/*      */ 
/*      */   public final URIResolver getURIResolver()
/*      */   {
/*  579 */     return this.m_uriResolver;
/*      */   }
/*      */ 
/*      */   public void setURIResolver(URIResolver resolver)
/*      */   {
/*  590 */     this.m_uriResolver = resolver;
/*      */   }
/*      */ 
/*      */   public final XMLReader getPrimaryReader()
/*      */   {
/*  605 */     return this.m_primaryReader;
/*      */   }
/*      */ 
/*      */   public void setPrimaryReader(XMLReader reader)
/*      */   {
/*  615 */     this.m_primaryReader = reader;
/*      */   }
/*      */ 
/*      */   private void assertion(boolean b, String msg)
/*      */     throws TransformerException
/*      */   {
/*  635 */     if (!b)
/*      */     {
/*  637 */       ErrorListener errorHandler = getErrorListener();
/*      */ 
/*  639 */       if (errorHandler != null)
/*      */       {
/*  641 */         errorHandler.fatalError(new TransformerException(XSLMessages.createMessage("ER_INCORRECT_PROGRAMMER_ASSERTION", new Object[] { msg }), (SAXSourceLocator)getSAXLocator()));
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public Stack getContextNodeListsStack()
/*      */   {
/*  659 */     return this.m_contextNodeLists; } 
/*  660 */   public void setContextNodeListsStack(Stack s) { this.m_contextNodeLists = s; }
/*      */ 
/*      */ 
/*      */   public final DTMIterator getContextNodeList()
/*      */   {
/*  671 */     if (this.m_contextNodeLists.size() > 0) {
/*  672 */       return (DTMIterator)this.m_contextNodeLists.peek();
/*      */     }
/*  674 */     return null;
/*      */   }
/*      */ 
/*      */   public final void pushContextNodeList(DTMIterator nl)
/*      */   {
/*  686 */     this.m_contextNodeLists.push(nl);
/*      */   }
/*      */ 
/*      */   public final void popContextNodeList()
/*      */   {
/*  695 */     if (this.m_contextNodeLists.isEmpty())
/*  696 */       System.err.println("Warning: popContextNodeList when stack is empty!");
/*      */     else
/*  698 */       this.m_contextNodeLists.pop();
/*      */   }
/*      */ 
/*      */   public IntStack getCurrentNodeStack()
/*      */   {
/*  715 */     return this.m_currentNodes; } 
/*  716 */   public void setCurrentNodeStack(IntStack nv) { this.m_currentNodes = nv; }
/*      */ 
/*      */ 
/*      */   public final int getCurrentNode()
/*      */   {
/*  725 */     return this.m_currentNodes.peek();
/*      */   }
/*      */ 
/*      */   public final void pushCurrentNodeAndExpression(int cn, int en)
/*      */   {
/*  736 */     this.m_currentNodes.push(cn);
/*  737 */     this.m_currentExpressionNodes.push(cn);
/*      */   }
/*      */ 
/*      */   public final void popCurrentNodeAndExpression()
/*      */   {
/*  745 */     this.m_currentNodes.quickPop(1);
/*  746 */     this.m_currentExpressionNodes.quickPop(1);
/*      */   }
/*      */ 
/*      */   public final void pushExpressionState(int cn, int en, PrefixResolver nc)
/*      */   {
/*  758 */     this.m_currentNodes.push(cn);
/*  759 */     this.m_currentExpressionNodes.push(cn);
/*  760 */     this.m_prefixResolvers.push(nc);
/*      */   }
/*      */ 
/*      */   public final void popExpressionState()
/*      */   {
/*  768 */     this.m_currentNodes.quickPop(1);
/*  769 */     this.m_currentExpressionNodes.quickPop(1);
/*  770 */     this.m_prefixResolvers.pop();
/*      */   }
/*      */ 
/*      */   public final void pushCurrentNode(int n)
/*      */   {
/*  782 */     this.m_currentNodes.push(n);
/*      */   }
/*      */ 
/*      */   public final void popCurrentNode()
/*      */   {
/*  790 */     this.m_currentNodes.quickPop(1);
/*      */   }
/*      */ 
/*      */   public final void pushPredicateRoot(int n)
/*      */   {
/*  798 */     this.m_predicateRoots.push(n);
/*      */   }
/*      */ 
/*      */   public final void popPredicateRoot()
/*      */   {
/*  806 */     this.m_predicateRoots.popQuick();
/*      */   }
/*      */ 
/*      */   public final int getPredicateRoot()
/*      */   {
/*  814 */     return this.m_predicateRoots.peepOrNull();
/*      */   }
/*      */ 
/*      */   public final void pushIteratorRoot(int n)
/*      */   {
/*  822 */     this.m_iteratorRoots.push(n);
/*      */   }
/*      */ 
/*      */   public final void popIteratorRoot()
/*      */   {
/*  830 */     this.m_iteratorRoots.popQuick();
/*      */   }
/*      */ 
/*      */   public final int getIteratorRoot()
/*      */   {
/*  838 */     return this.m_iteratorRoots.peepOrNull();
/*      */   }
/*      */ 
/*      */   public IntStack getCurrentExpressionNodeStack()
/*      */   {
/*  851 */     return this.m_currentExpressionNodes; } 
/*  852 */   public void setCurrentExpressionNodeStack(IntStack nv) { this.m_currentExpressionNodes = nv; }
/*      */ 
/*      */ 
/*      */   public final int getPredicatePos()
/*      */   {
/*  858 */     return this.m_predicatePos.peek();
/*      */   }
/*      */ 
/*      */   public final void pushPredicatePos(int n)
/*      */   {
/*  863 */     this.m_predicatePos.push(n);
/*      */   }
/*      */ 
/*      */   public final void popPredicatePos()
/*      */   {
/*  868 */     this.m_predicatePos.pop();
/*      */   }
/*      */ 
/*      */   public final int getCurrentExpressionNode()
/*      */   {
/*  878 */     return this.m_currentExpressionNodes.peek();
/*      */   }
/*      */ 
/*      */   public final void pushCurrentExpressionNode(int n)
/*      */   {
/*  888 */     this.m_currentExpressionNodes.push(n);
/*      */   }
/*      */ 
/*      */   public final void popCurrentExpressionNode()
/*      */   {
/*  897 */     this.m_currentExpressionNodes.quickPop(1);
/*      */   }
/*      */ 
/*      */   public final PrefixResolver getNamespaceContext()
/*      */   {
/*  911 */     return (PrefixResolver)this.m_prefixResolvers.peek();
/*      */   }
/*      */ 
/*      */   public final void setNamespaceContext(PrefixResolver pr)
/*      */   {
/*  922 */     this.m_prefixResolvers.setTop(pr);
/*      */   }
/*      */ 
/*      */   public final void pushNamespaceContext(PrefixResolver pr)
/*      */   {
/*  933 */     this.m_prefixResolvers.push(pr);
/*      */   }
/*      */ 
/*      */   public final void pushNamespaceContextNull()
/*      */   {
/*  942 */     this.m_prefixResolvers.push(null);
/*      */   }
/*      */ 
/*      */   public final void popNamespaceContext()
/*      */   {
/*  950 */     this.m_prefixResolvers.pop();
/*      */   }
/*      */ 
/*      */   public Stack getAxesIteratorStackStacks()
/*      */   {
/*  962 */     return this.m_axesIteratorStack; } 
/*  963 */   public void setAxesIteratorStackStacks(Stack s) { this.m_axesIteratorStack = s; }
/*      */ 
/*      */ 
/*      */   public final void pushSubContextList(SubContextList iter)
/*      */   {
/*  973 */     this.m_axesIteratorStack.push(iter);
/*      */   }
/*      */ 
/*      */   public final void popSubContextList()
/*      */   {
/*  982 */     this.m_axesIteratorStack.pop();
/*      */   }
/*      */ 
/*      */   public SubContextList getSubContextList()
/*      */   {
/*  993 */     return this.m_axesIteratorStack.isEmpty() ? null : (SubContextList)this.m_axesIteratorStack.peek();
/*      */   }
/*      */ 
/*      */   public SubContextList getCurrentNodeList()
/*      */   {
/* 1007 */     return this.m_axesIteratorStack.isEmpty() ? null : (SubContextList)this.m_axesIteratorStack.elementAt(0);
/*      */   }
/*      */ 
/*      */   public final int getContextNode()
/*      */   {
/* 1020 */     return getCurrentNode();
/*      */   }
/*      */ 
/*      */   public final DTMIterator getContextNodes()
/*      */   {
/*      */     try
/*      */     {
/* 1033 */       DTMIterator cnl = getContextNodeList();
/*      */ 
/* 1035 */       if (null != cnl) {
/* 1036 */         return cnl.cloneWithReset();
/*      */       }
/* 1038 */       return null;
/*      */     }
/*      */     catch (CloneNotSupportedException cnse) {
/*      */     }
/* 1042 */     return null;
/*      */   }
/*      */ 
/*      */   public ExpressionContext getExpressionContext()
/*      */   {
/* 1055 */     return this.expressionContext;
/*      */   }
/*      */ 
/*      */   public DTM getGlobalRTFDTM()
/*      */   {
/* 1200 */     if ((this.m_global_rtfdtm == null) || (this.m_global_rtfdtm.isTreeIncomplete()))
/*      */     {
/* 1202 */       this.m_global_rtfdtm = ((SAX2RTFDTM)this.m_dtmManager.getDTM(null, true, null, false, false));
/*      */     }
/* 1204 */     return this.m_global_rtfdtm;
/*      */   }
/*      */ 
/*      */   public DTM getRTFDTM()
/*      */   {
/*      */     SAX2RTFDTM rtfdtm;
/* 1233 */     if (this.m_rtfdtm_stack == null)
/*      */     {
/* 1235 */       this.m_rtfdtm_stack = new Vector();
/* 1236 */       SAX2RTFDTM rtfdtm = (SAX2RTFDTM)this.m_dtmManager.getDTM(null, true, null, false, false);
/* 1237 */       this.m_rtfdtm_stack.addElement(rtfdtm);
/* 1238 */       this.m_which_rtfdtm += 1;
/*      */     }
/*      */     else
/*      */     {
/*      */       SAX2RTFDTM rtfdtm;
/* 1240 */       if (this.m_which_rtfdtm < 0)
/*      */       {
/* 1242 */         rtfdtm = (SAX2RTFDTM)this.m_rtfdtm_stack.elementAt(++this.m_which_rtfdtm);
/*      */       }
/*      */       else
/*      */       {
/* 1246 */         rtfdtm = (SAX2RTFDTM)this.m_rtfdtm_stack.elementAt(this.m_which_rtfdtm);
/*      */ 
/* 1254 */         if (rtfdtm.isTreeIncomplete())
/*      */         {
/* 1256 */           if (++this.m_which_rtfdtm < this.m_rtfdtm_stack.size()) {
/* 1257 */             rtfdtm = (SAX2RTFDTM)this.m_rtfdtm_stack.elementAt(this.m_which_rtfdtm);
/*      */           }
/*      */           else {
/* 1260 */             rtfdtm = (SAX2RTFDTM)this.m_dtmManager.getDTM(null, true, null, false, false);
/* 1261 */             this.m_rtfdtm_stack.addElement(rtfdtm);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 1266 */     return rtfdtm;
/*      */   }
/*      */ 
/*      */   public void pushRTFContext()
/*      */   {
/* 1275 */     this.m_last_pushed_rtfdtm.push(this.m_which_rtfdtm);
/* 1276 */     if (null != this.m_rtfdtm_stack)
/* 1277 */       ((SAX2RTFDTM)getRTFDTM()).pushRewindMark();
/*      */   }
/*      */ 
/*      */   public void popRTFContext()
/*      */   {
/* 1296 */     int previous = this.m_last_pushed_rtfdtm.pop();
/* 1297 */     if (null == this.m_rtfdtm_stack)
/*      */       return;
/*      */     boolean isEmpty;
/* 1300 */     if (this.m_which_rtfdtm == previous)
/*      */     {
/* 1302 */       if (previous >= 0)
/*      */       {
/* 1304 */         isEmpty = ((SAX2RTFDTM)this.m_rtfdtm_stack.elementAt(previous)).popRewindMark();
/*      */       }
/*      */     }
/* 1307 */     else while (this.m_which_rtfdtm != previous)
/*      */       {
/* 1312 */         boolean isEmpty = ((SAX2RTFDTM)this.m_rtfdtm_stack.elementAt(this.m_which_rtfdtm)).popRewindMark();
/* 1313 */         this.m_which_rtfdtm -= 1;
/*      */       }
/*      */   }
/*      */ 
/*      */   public DTMXRTreeFrag getDTMXRTreeFrag(int dtmIdentity)
/*      */   {
/* 1325 */     if (this.m_DTMXRTreeFrags == null) {
/* 1326 */       this.m_DTMXRTreeFrags = new HashMap();
/*      */     }
/*      */ 
/* 1329 */     if (this.m_DTMXRTreeFrags.containsKey(new Integer(dtmIdentity))) {
/* 1330 */       return (DTMXRTreeFrag)this.m_DTMXRTreeFrags.get(new Integer(dtmIdentity));
/*      */     }
/* 1332 */     DTMXRTreeFrag frag = new DTMXRTreeFrag(dtmIdentity, this);
/* 1333 */     this.m_DTMXRTreeFrags.put(new Integer(dtmIdentity), frag);
/* 1334 */     return frag;
/*      */   }
/*      */ 
/*      */   private final void releaseDTMXRTreeFrags()
/*      */   {
/* 1343 */     if (this.m_DTMXRTreeFrags == null) {
/* 1344 */       return;
/*      */     }
/* 1346 */     Iterator iter = this.m_DTMXRTreeFrags.values().iterator();
/* 1347 */     while (iter.hasNext()) {
/* 1348 */       DTMXRTreeFrag frag = (DTMXRTreeFrag)iter.next();
/* 1349 */       frag.destruct();
/* 1350 */       iter.remove();
/*      */     }
/* 1352 */     this.m_DTMXRTreeFrags = null;
/*      */   }
/*      */ 
/*      */   public class XPathExpressionContext
/*      */     implements ExpressionContext
/*      */   {
/*      */     public XPathExpressionContext()
/*      */     {
/*      */     }
/*      */ 
/*      */     public XPathContext getXPathContext()
/*      */     {
/* 1069 */       return XPathContext.this;
/*      */     }
/*      */ 
/*      */     public DTMManager getDTMManager()
/*      */     {
/* 1080 */       return XPathContext.this.m_dtmManager;
/*      */     }
/*      */ 
/*      */     public Node getContextNode()
/*      */     {
/* 1089 */       int context = XPathContext.this.getCurrentNode();
/*      */ 
/* 1091 */       return XPathContext.this.getDTM(context).getNode(context);
/*      */     }
/*      */ 
/*      */     public NodeIterator getContextNodes()
/*      */     {
/* 1101 */       return new DTMNodeIterator(XPathContext.this.getContextNodeList());
/*      */     }
/*      */ 
/*      */     public ErrorListener getErrorListener()
/*      */     {
/* 1110 */       return XPathContext.this.getErrorListener();
/*      */     }
/*      */ 
/*      */     public boolean useServicesMechnism()
/*      */     {
/* 1116 */       return XPathContext.this.m_useServicesMechanism;
/*      */     }
/*      */ 
/*      */     public void setServicesMechnism(boolean flag)
/*      */     {
/* 1123 */       XPathContext.this.m_useServicesMechanism = flag;
/*      */     }
/*      */ 
/*      */     public double toNumber(Node n)
/*      */     {
/* 1134 */       int nodeHandle = XPathContext.this.getDTMHandleFromNode(n);
/* 1135 */       DTM dtm = XPathContext.this.getDTM(nodeHandle);
/* 1136 */       XString xobj = (XString)dtm.getStringValue(nodeHandle);
/* 1137 */       return xobj.num();
/*      */     }
/*      */ 
/*      */     public String toString(Node n)
/*      */     {
/* 1148 */       int nodeHandle = XPathContext.this.getDTMHandleFromNode(n);
/* 1149 */       DTM dtm = XPathContext.this.getDTM(nodeHandle);
/* 1150 */       XMLString strVal = dtm.getStringValue(nodeHandle);
/* 1151 */       return strVal.toString();
/*      */     }
/*      */ 
/*      */     public final XObject getVariableOrParam(QName qname)
/*      */       throws TransformerException
/*      */     {
/* 1164 */       return XPathContext.this.m_variableStacks.getVariableOrParam(XPathContext.this, qname);
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.XPathContext
 * JD-Core Version:    0.6.2
 */