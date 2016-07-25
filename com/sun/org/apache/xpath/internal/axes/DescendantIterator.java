/*     */ package com.sun.org.apache.xpath.internal.axes;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.dtm.DTM;
/*     */ import com.sun.org.apache.xml.internal.dtm.DTMAxisTraverser;
/*     */ import com.sun.org.apache.xml.internal.dtm.DTMIterator;
/*     */ import com.sun.org.apache.xpath.internal.Expression;
/*     */ import com.sun.org.apache.xpath.internal.VariableStack;
/*     */ import com.sun.org.apache.xpath.internal.XPathContext;
/*     */ import com.sun.org.apache.xpath.internal.compiler.Compiler;
/*     */ import com.sun.org.apache.xpath.internal.compiler.OpMap;
/*     */ import javax.xml.transform.TransformerException;
/*     */ 
/*     */ public class DescendantIterator extends LocPathIterator
/*     */ {
/*     */   static final long serialVersionUID = -1190338607743976938L;
/*     */   protected transient DTMAxisTraverser m_traverser;
/*     */   protected int m_axis;
/*     */   protected int m_extendedTypeID;
/*     */ 
/*     */   DescendantIterator(Compiler compiler, int opPos, int analysis)
/*     */     throws TransformerException
/*     */   {
/*  60 */     super(compiler, opPos, analysis, false);
/*     */ 
/*  62 */     int firstStepPos = OpMap.getFirstChildPos(opPos);
/*  63 */     int stepType = compiler.getOp(firstStepPos);
/*     */ 
/*  65 */     boolean orSelf = 42 == stepType;
/*  66 */     boolean fromRoot = false;
/*  67 */     if (48 == stepType)
/*     */     {
/*  69 */       orSelf = true;
/*     */     }
/*  72 */     else if (50 == stepType)
/*     */     {
/*  74 */       fromRoot = true;
/*     */ 
/*  76 */       int nextStepPos = compiler.getNextStepPos(firstStepPos);
/*  77 */       if (compiler.getOp(nextStepPos) == 42) {
/*  78 */         orSelf = true;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/*  83 */     int nextStepPos = firstStepPos;
/*     */     while (true)
/*     */     {
/*  86 */       nextStepPos = compiler.getNextStepPos(nextStepPos);
/*  87 */       if (nextStepPos <= 0)
/*     */         break;
/*  89 */       int stepOp = compiler.getOp(nextStepPos);
/*  90 */       if (-1 == stepOp) break;
/*  91 */       firstStepPos = nextStepPos;
/*     */     }
/*     */ 
/* 101 */     if ((analysis & 0x10000) != 0) {
/* 102 */       orSelf = false;
/*     */     }
/* 104 */     if (fromRoot)
/*     */     {
/* 106 */       if (orSelf)
/* 107 */         this.m_axis = 18;
/*     */       else
/* 109 */         this.m_axis = 17;
/*     */     }
/* 111 */     else if (orSelf)
/* 112 */       this.m_axis = 5;
/*     */     else {
/* 114 */       this.m_axis = 4;
/*     */     }
/* 116 */     int whatToShow = compiler.getWhatToShow(firstStepPos);
/*     */ 
/* 118 */     if ((0 == (whatToShow & 0x43)) || (whatToShow == -1))
/*     */     {
/* 122 */       initNodeTest(whatToShow);
/*     */     }
/*     */     else {
/* 125 */       initNodeTest(whatToShow, compiler.getStepNS(firstStepPos), compiler.getStepLocalName(firstStepPos));
/*     */     }
/*     */ 
/* 128 */     initPredicateInfo(compiler, firstStepPos);
/*     */   }
/*     */ 
/*     */   public DescendantIterator()
/*     */   {
/* 137 */     super(null);
/* 138 */     this.m_axis = 18;
/* 139 */     int whatToShow = -1;
/* 140 */     initNodeTest(whatToShow);
/*     */   }
/*     */ 
/*     */   public DTMIterator cloneWithReset()
/*     */     throws CloneNotSupportedException
/*     */   {
/* 155 */     DescendantIterator clone = (DescendantIterator)super.cloneWithReset();
/* 156 */     clone.m_traverser = this.m_traverser;
/*     */ 
/* 158 */     clone.resetProximityPositions();
/*     */ 
/* 160 */     return clone;
/*     */   }
/*     */ 
/*     */   public int nextNode()
/*     */   {
/* 177 */     if (this.m_foundLast) {
/* 178 */       return -1;
/*     */     }
/* 180 */     if (-1 == this.m_lastFetched)
/*     */     {
/* 182 */       resetProximityPositions();
/*     */     }
/*     */     VariableStack vars;
/*     */     int savedStart;
/* 189 */     if (-1 != this.m_stackFrame)
/*     */     {
/* 191 */       VariableStack vars = this.m_execContext.getVarStack();
/*     */ 
/* 194 */       int savedStart = vars.getStackFrame();
/*     */ 
/* 196 */       vars.setStackFrame(this.m_stackFrame);
/*     */     }
/*     */     else
/*     */     {
/* 201 */       vars = null;
/* 202 */       savedStart = 0;
/*     */     }
/*     */     try
/*     */     {
/*     */       int next;
/*     */       do
/*     */       {
/*     */         int next;
/* 209 */         if (0 == this.m_extendedTypeID)
/*     */         {
/* 211 */           next = this.m_lastFetched = -1 == this.m_lastFetched ? this.m_traverser.first(this.m_context) : this.m_traverser.next(this.m_context, this.m_lastFetched);
/*     */         }
/*     */         else
/*     */         {
/* 217 */           next = this.m_lastFetched = -1 == this.m_lastFetched ? this.m_traverser.first(this.m_context, this.m_extendedTypeID) : this.m_traverser.next(this.m_context, this.m_lastFetched, this.m_extendedTypeID);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 223 */       while ((-1 != next) && 
/* 225 */         (1 != acceptNode(next)) && 
/* 233 */         (next != -1));
/*     */       int i;
/* 235 */       if (-1 != next)
/*     */       {
/* 237 */         this.m_pos += 1;
/* 238 */         return next;
/*     */       }
/*     */ 
/* 242 */       this.m_foundLast = true;
/*     */ 
/* 244 */       return -1;
/*     */     }
/*     */     finally
/*     */     {
/* 249 */       if (-1 != this.m_stackFrame)
/*     */       {
/* 252 */         vars.setStackFrame(savedStart);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setRoot(int context, Object environment)
/*     */   {
/* 266 */     super.setRoot(context, environment);
/* 267 */     this.m_traverser = this.m_cdtm.getAxisTraverser(this.m_axis);
/*     */ 
/* 269 */     String localName = getLocalName();
/* 270 */     String namespace = getNamespace();
/* 271 */     int what = this.m_whatToShow;
/*     */ 
/* 274 */     if ((-1 == what) || ("*".equals(localName)) || ("*".equals(namespace)))
/*     */     {
/* 278 */       this.m_extendedTypeID = 0;
/*     */     }
/*     */     else
/*     */     {
/* 282 */       int type = getNodeTypeTest(what);
/* 283 */       this.m_extendedTypeID = this.m_cdtm.getExpandedTypeID(namespace, localName, type);
/*     */     }
/*     */   }
/*     */ 
/*     */   public int asNode(XPathContext xctxt)
/*     */     throws TransformerException
/*     */   {
/* 299 */     if (getPredicateCount() > 0) {
/* 300 */       return super.asNode(xctxt);
/*     */     }
/* 302 */     int current = xctxt.getCurrentNode();
/*     */ 
/* 304 */     DTM dtm = xctxt.getDTM(current);
/* 305 */     DTMAxisTraverser traverser = dtm.getAxisTraverser(this.m_axis);
/*     */ 
/* 307 */     String localName = getLocalName();
/* 308 */     String namespace = getNamespace();
/* 309 */     int what = this.m_whatToShow;
/*     */ 
/* 315 */     if ((-1 == what) || (localName == "*") || (namespace == "*"))
/*     */     {
/* 319 */       return traverser.first(current);
/*     */     }
/*     */ 
/* 323 */     int type = getNodeTypeTest(what);
/* 324 */     int extendedType = dtm.getExpandedTypeID(namespace, localName, type);
/* 325 */     return traverser.first(current, extendedType);
/*     */   }
/*     */ 
/*     */   public void detach()
/*     */   {
/* 338 */     if (this.m_allowDetach) {
/* 339 */       this.m_traverser = null;
/* 340 */       this.m_extendedTypeID = 0;
/*     */ 
/* 343 */       super.detach();
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getAxis()
/*     */   {
/* 355 */     return this.m_axis;
/*     */   }
/*     */ 
/*     */   public boolean deepEquals(Expression expr)
/*     */   {
/* 373 */     if (!super.deepEquals(expr)) {
/* 374 */       return false;
/*     */     }
/* 376 */     if (this.m_axis != ((DescendantIterator)expr).m_axis) {
/* 377 */       return false;
/*     */     }
/* 379 */     return true;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.axes.DescendantIterator
 * JD-Core Version:    0.6.2
 */