/*      */ package com.sun.org.apache.xpath.internal.axes;
/*      */ 
/*      */ import com.sun.org.apache.xalan.internal.res.XSLMessages;
/*      */ import com.sun.org.apache.xml.internal.dtm.DTM;
/*      */ import com.sun.org.apache.xml.internal.dtm.DTMFilter;
/*      */ import com.sun.org.apache.xml.internal.dtm.DTMIterator;
/*      */ import com.sun.org.apache.xml.internal.dtm.DTMManager;
/*      */ import com.sun.org.apache.xml.internal.utils.PrefixResolver;
/*      */ import com.sun.org.apache.xpath.internal.ExpressionOwner;
/*      */ import com.sun.org.apache.xpath.internal.VariableStack;
/*      */ import com.sun.org.apache.xpath.internal.XPathContext;
/*      */ import com.sun.org.apache.xpath.internal.XPathVisitor;
/*      */ import com.sun.org.apache.xpath.internal.compiler.Compiler;
/*      */ import com.sun.org.apache.xpath.internal.objects.XNodeSet;
/*      */ import com.sun.org.apache.xpath.internal.objects.XObject;
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.Serializable;
/*      */ import javax.xml.transform.TransformerException;
/*      */ import org.xml.sax.ContentHandler;
/*      */ import org.xml.sax.SAXException;
/*      */ 
/*      */ public abstract class LocPathIterator extends PredicatedNodeTest
/*      */   implements Cloneable, DTMIterator, Serializable, PathComponent
/*      */ {
/*      */   static final long serialVersionUID = -4602476357268405754L;
/*  658 */   protected boolean m_allowDetach = true;
/*      */ 
/*  939 */   protected transient IteratorPool m_clones = new IteratorPool(this);
/*      */   protected transient DTM m_cdtm;
/*  950 */   transient int m_stackFrame = -1;
/*      */ 
/*  958 */   private boolean m_isTopLevel = false;
/*      */ 
/*  961 */   public transient int m_lastFetched = -1;
/*      */ 
/*  967 */   protected transient int m_context = -1;
/*      */ 
/*  975 */   protected transient int m_currentContextNode = -1;
/*      */ 
/*  980 */   protected transient int m_pos = 0;
/*      */ 
/*  982 */   protected transient int m_length = -1;
/*      */   private PrefixResolver m_prefixResolver;
/*      */   protected transient XPathContext m_execContext;
/*      */ 
/*      */   protected LocPathIterator()
/*      */   {
/*      */   }
/*      */ 
/*      */   protected LocPathIterator(PrefixResolver nscontext)
/*      */   {
/*   74 */     setLocPathIterator(this);
/*   75 */     this.m_prefixResolver = nscontext;
/*      */   }
/*      */ 
/*      */   protected LocPathIterator(Compiler compiler, int opPos, int analysis)
/*      */     throws TransformerException
/*      */   {
/*   93 */     this(compiler, opPos, analysis, true);
/*      */   }
/*      */ 
/*      */   protected LocPathIterator(Compiler compiler, int opPos, int analysis, boolean shouldLoadWalkers)
/*      */     throws TransformerException
/*      */   {
/*  115 */     setLocPathIterator(this);
/*      */   }
/*      */ 
/*      */   public int getAnalysisBits()
/*      */   {
/*  124 */     int axis = getAxis();
/*  125 */     int bit = WalkerFactory.getAnalysisBitFromAxes(axis);
/*  126 */     return bit;
/*      */   }
/*      */ 
/*      */   private void readObject(ObjectInputStream stream)
/*      */     throws IOException, TransformerException
/*      */   {
/*      */     try
/*      */     {
/*  142 */       stream.defaultReadObject();
/*  143 */       this.m_clones = new IteratorPool(this);
/*      */     }
/*      */     catch (ClassNotFoundException cnfe)
/*      */     {
/*  147 */       throw new TransformerException(cnfe);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setEnvironment(Object environment)
/*      */   {
/*      */   }
/*      */ 
/*      */   public DTM getDTM(int nodeHandle)
/*      */   {
/*  181 */     return this.m_execContext.getDTM(nodeHandle);
/*      */   }
/*      */ 
/*      */   public DTMManager getDTMManager()
/*      */   {
/*  193 */     return this.m_execContext.getDTMManager();
/*      */   }
/*      */ 
/*      */   public XObject execute(XPathContext xctxt)
/*      */     throws TransformerException
/*      */   {
/*  212 */     XNodeSet iter = new XNodeSet((LocPathIterator)this.m_clones.getInstance());
/*      */ 
/*  214 */     iter.setRoot(xctxt.getCurrentNode(), xctxt);
/*      */ 
/*  216 */     return iter;
/*      */   }
/*      */ 
/*      */   public void executeCharsToContentHandler(XPathContext xctxt, ContentHandler handler)
/*      */     throws TransformerException, SAXException
/*      */   {
/*  238 */     LocPathIterator clone = (LocPathIterator)this.m_clones.getInstance();
/*      */ 
/*  240 */     int current = xctxt.getCurrentNode();
/*  241 */     clone.setRoot(current, xctxt);
/*      */ 
/*  243 */     int node = clone.nextNode();
/*  244 */     DTM dtm = clone.getDTM(node);
/*  245 */     clone.detach();
/*      */ 
/*  247 */     if (node != -1)
/*      */     {
/*  249 */       dtm.dispatchCharactersEvents(node, handler, false);
/*      */     }
/*      */   }
/*      */ 
/*      */   public DTMIterator asIterator(XPathContext xctxt, int contextNode)
/*      */     throws TransformerException
/*      */   {
/*  269 */     XNodeSet iter = new XNodeSet((LocPathIterator)this.m_clones.getInstance());
/*      */ 
/*  271 */     iter.setRoot(contextNode, xctxt);
/*      */ 
/*  273 */     return iter;
/*      */   }
/*      */ 
/*      */   public boolean isNodesetExpr()
/*      */   {
/*  284 */     return true;
/*      */   }
/*      */ 
/*      */   public int asNode(XPathContext xctxt)
/*      */     throws TransformerException
/*      */   {
/*  298 */     DTMIterator iter = this.m_clones.getInstance();
/*      */ 
/*  300 */     int current = xctxt.getCurrentNode();
/*      */ 
/*  302 */     iter.setRoot(current, xctxt);
/*      */ 
/*  304 */     int next = iter.nextNode();
/*      */ 
/*  306 */     iter.detach();
/*  307 */     return next;
/*      */   }
/*      */ 
/*      */   public boolean bool(XPathContext xctxt)
/*      */     throws TransformerException
/*      */   {
/*  322 */     return asNode(xctxt) != -1;
/*      */   }
/*      */ 
/*      */   public void setIsTopLevel(boolean b)
/*      */   {
/*  336 */     this.m_isTopLevel = b;
/*      */   }
/*      */ 
/*      */   public boolean getIsTopLevel()
/*      */   {
/*  349 */     return this.m_isTopLevel;
/*      */   }
/*      */ 
/*      */   public void setRoot(int context, Object environment)
/*      */   {
/*  362 */     this.m_context = context;
/*      */ 
/*  364 */     XPathContext xctxt = (XPathContext)environment;
/*  365 */     this.m_execContext = xctxt;
/*  366 */     this.m_cdtm = xctxt.getDTM(context);
/*      */ 
/*  368 */     this.m_currentContextNode = context;
/*      */ 
/*  371 */     if (null == this.m_prefixResolver) {
/*  372 */       this.m_prefixResolver = xctxt.getNamespaceContext();
/*      */     }
/*  374 */     this.m_lastFetched = -1;
/*  375 */     this.m_foundLast = false;
/*  376 */     this.m_pos = 0;
/*  377 */     this.m_length = -1;
/*      */ 
/*  379 */     if (this.m_isTopLevel)
/*  380 */       this.m_stackFrame = xctxt.getVarStack().getStackFrame();
/*      */   }
/*      */ 
/*      */   protected void setNextPosition(int next)
/*      */   {
/*  393 */     assertion(false, "setNextPosition not supported in this iterator!");
/*      */   }
/*      */ 
/*      */   public final int getCurrentPos()
/*      */   {
/*  407 */     return this.m_pos;
/*      */   }
/*      */ 
/*      */   public void setShouldCacheNodes(boolean b)
/*      */   {
/*  420 */     assertion(false, "setShouldCacheNodes not supported by this iterater!");
/*      */   }
/*      */ 
/*      */   public boolean isMutable()
/*      */   {
/*  431 */     return false;
/*      */   }
/*      */ 
/*      */   public void setCurrentPos(int i)
/*      */   {
/*  442 */     assertion(false, "setCurrentPos not supported by this iterator!");
/*      */   }
/*      */ 
/*      */   public void incrementCurrentPos()
/*      */   {
/*  450 */     this.m_pos += 1;
/*      */   }
/*      */ 
/*      */   public int size()
/*      */   {
/*  466 */     assertion(false, "size() not supported by this iterator!");
/*  467 */     return 0;
/*      */   }
/*      */ 
/*      */   public int item(int index)
/*      */   {
/*  481 */     assertion(false, "item(int index) not supported by this iterator!");
/*  482 */     return 0;
/*      */   }
/*      */ 
/*      */   public void setItem(int node, int index)
/*      */   {
/*  500 */     assertion(false, "setItem not supported by this iterator!");
/*      */   }
/*      */ 
/*      */   public int getLength()
/*      */   {
/*  512 */     boolean isPredicateTest = this == this.m_execContext.getSubContextList();
/*      */ 
/*  515 */     int predCount = getPredicateCount();
/*      */ 
/*  520 */     if ((-1 != this.m_length) && (isPredicateTest) && (this.m_predicateIndex < 1)) {
/*  521 */       return this.m_length;
/*      */     }
/*      */ 
/*  525 */     if (this.m_foundLast) {
/*  526 */       return this.m_pos;
/*      */     }
/*      */ 
/*  531 */     int pos = this.m_predicateIndex >= 0 ? getProximityPosition() : this.m_pos;
/*      */     LocPathIterator clone;
/*      */     try
/*      */     {
/*  537 */       clone = (LocPathIterator)clone();
/*      */     }
/*      */     catch (CloneNotSupportedException cnse)
/*      */     {
/*  541 */       return -1;
/*      */     }
/*      */ 
/*  547 */     if ((predCount > 0) && (isPredicateTest))
/*      */     {
/*  550 */       clone.m_predCount = this.m_predicateIndex;
/*      */     }
/*      */     int next;
/*  558 */     while (-1 != (next = clone.nextNode()))
/*      */     {
/*  560 */       pos++;
/*      */     }
/*      */ 
/*  563 */     if ((isPredicateTest) && (this.m_predicateIndex < 1)) {
/*  564 */       this.m_length = pos;
/*      */     }
/*  566 */     return pos;
/*      */   }
/*      */ 
/*      */   public boolean isFresh()
/*      */   {
/*  578 */     return this.m_pos == 0;
/*      */   }
/*      */ 
/*      */   public int previousNode()
/*      */   {
/*  589 */     throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESETDTM_CANNOT_ITERATE", null));
/*      */   }
/*      */ 
/*      */   public int getWhatToShow()
/*      */   {
/*  610 */     return -17;
/*      */   }
/*      */ 
/*      */   public DTMFilter getFilter()
/*      */   {
/*  623 */     return null;
/*      */   }
/*      */ 
/*      */   public int getRoot()
/*      */   {
/*  634 */     return this.m_context;
/*      */   }
/*      */ 
/*      */   public boolean getExpandEntityReferences()
/*      */   {
/*  654 */     return true;
/*      */   }
/*      */ 
/*      */   public void allowDetachToRelease(boolean allowRelease)
/*      */   {
/*  668 */     this.m_allowDetach = allowRelease;
/*      */   }
/*      */ 
/*      */   public void detach()
/*      */   {
/*  680 */     if (this.m_allowDetach)
/*      */     {
/*  684 */       this.m_execContext = null;
/*      */ 
/*  686 */       this.m_cdtm = null;
/*  687 */       this.m_length = -1;
/*  688 */       this.m_pos = 0;
/*  689 */       this.m_lastFetched = -1;
/*  690 */       this.m_context = -1;
/*  691 */       this.m_currentContextNode = -1;
/*      */ 
/*  693 */       this.m_clones.freeInstance(this);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void reset()
/*      */   {
/*  702 */     assertion(false, "This iterator can not reset!");
/*      */   }
/*      */ 
/*      */   public DTMIterator cloneWithReset()
/*      */     throws CloneNotSupportedException
/*      */   {
/*  717 */     LocPathIterator clone = (LocPathIterator)this.m_clones.getInstanceOrThrow();
/*  718 */     clone.m_execContext = this.m_execContext;
/*  719 */     clone.m_cdtm = this.m_cdtm;
/*      */ 
/*  721 */     clone.m_context = this.m_context;
/*  722 */     clone.m_currentContextNode = this.m_currentContextNode;
/*  723 */     clone.m_stackFrame = this.m_stackFrame;
/*      */ 
/*  727 */     return clone;
/*      */   }
/*      */ 
/*      */   public abstract int nextNode();
/*      */ 
/*      */   protected int returnNextNode(int nextNode)
/*      */   {
/*  766 */     if (-1 != nextNode)
/*      */     {
/*  768 */       this.m_pos += 1;
/*      */     }
/*      */ 
/*  771 */     this.m_lastFetched = nextNode;
/*      */ 
/*  773 */     if (-1 == nextNode) {
/*  774 */       this.m_foundLast = true;
/*      */     }
/*  776 */     return nextNode;
/*      */   }
/*      */ 
/*      */   public int getCurrentNode()
/*      */   {
/*  786 */     return this.m_lastFetched;
/*      */   }
/*      */ 
/*      */   public void runTo(int index)
/*      */   {
/*  801 */     if ((this.m_foundLast) || ((index >= 0) && (index <= getCurrentPos()))) {
/*  802 */       return;
/*      */     }
/*      */ 
/*  806 */     if (-1 == index)
/*      */     {
/*      */       int n;
/*  808 */       while (-1 != (n = nextNode()));
/*      */     }
/*      */     int n;
/*  812 */     while (-1 != (n = nextNode()))
/*      */     {
/*  814 */       if (getCurrentPos() >= index)
/*  815 */         break;
/*      */     }
/*      */   }
/*      */ 
/*      */   public final boolean getFoundLast()
/*      */   {
/*  827 */     return this.m_foundLast;
/*      */   }
/*      */ 
/*      */   public final XPathContext getXPathContext()
/*      */   {
/*  838 */     return this.m_execContext;
/*      */   }
/*      */ 
/*      */   public final int getContext()
/*      */   {
/*  848 */     return this.m_context;
/*      */   }
/*      */ 
/*      */   public final int getCurrentContextNode()
/*      */   {
/*  859 */     return this.m_currentContextNode;
/*      */   }
/*      */ 
/*      */   public final void setCurrentContextNode(int n)
/*      */   {
/*  869 */     this.m_currentContextNode = n;
/*      */   }
/*      */ 
/*      */   public final PrefixResolver getPrefixResolver()
/*      */   {
/*  891 */     if (null == this.m_prefixResolver)
/*      */     {
/*  893 */       this.m_prefixResolver = ((PrefixResolver)getExpressionOwner());
/*      */     }
/*      */ 
/*  896 */     return this.m_prefixResolver;
/*      */   }
/*      */ 
/*      */   public void callVisitors(ExpressionOwner owner, XPathVisitor visitor)
/*      */   {
/*  924 */     if (visitor.visitLocationPath(owner, this))
/*      */     {
/*  926 */       visitor.visitStep(owner, this);
/*  927 */       callPredicateVisitors(visitor);
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean isDocOrdered()
/*      */   {
/* 1005 */     return true;
/*      */   }
/*      */ 
/*      */   public int getAxis()
/*      */   {
/* 1016 */     return -1;
/*      */   }
/*      */ 
/*      */   public int getLastPos(XPathContext xctxt)
/*      */   {
/* 1032 */     return getLength();
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.axes.LocPathIterator
 * JD-Core Version:    0.6.2
 */