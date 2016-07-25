/*     */ package com.sun.org.apache.xpath.internal.axes;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.utils.PrefixResolver;
/*     */ import com.sun.org.apache.xpath.internal.Expression;
/*     */ import com.sun.org.apache.xpath.internal.ExpressionOwner;
/*     */ import com.sun.org.apache.xpath.internal.VariableStack;
/*     */ import com.sun.org.apache.xpath.internal.XPathContext;
/*     */ import com.sun.org.apache.xpath.internal.XPathVisitor;
/*     */ import com.sun.org.apache.xpath.internal.compiler.Compiler;
/*     */ import com.sun.org.apache.xpath.internal.compiler.OpMap;
/*     */ import java.util.Vector;
/*     */ import javax.xml.transform.TransformerException;
/*     */ 
/*     */ public class WalkingIterator extends LocPathIterator
/*     */   implements ExpressionOwner
/*     */ {
/*     */   static final long serialVersionUID = 9110225941815665906L;
/*     */   protected AxesWalker m_lastUsedWalker;
/*     */   protected AxesWalker m_firstWalker;
/*     */ 
/*     */   WalkingIterator(Compiler compiler, int opPos, int analysis, boolean shouldLoadWalkers)
/*     */     throws TransformerException
/*     */   {
/*  60 */     super(compiler, opPos, analysis, shouldLoadWalkers);
/*     */ 
/*  62 */     int firstStepPos = OpMap.getFirstChildPos(opPos);
/*     */ 
/*  64 */     if (shouldLoadWalkers)
/*     */     {
/*  66 */       this.m_firstWalker = WalkerFactory.loadWalkers(this, compiler, firstStepPos, 0);
/*  67 */       this.m_lastUsedWalker = this.m_firstWalker;
/*     */     }
/*     */   }
/*     */ 
/*     */   public WalkingIterator(PrefixResolver nscontext)
/*     */   {
/*  80 */     super(nscontext);
/*     */   }
/*     */ 
/*     */   public int getAnalysisBits()
/*     */   {
/*  90 */     int bits = 0;
/*  91 */     if (null != this.m_firstWalker)
/*     */     {
/*  93 */       AxesWalker walker = this.m_firstWalker;
/*     */ 
/*  95 */       while (null != walker)
/*     */       {
/*  97 */         int bit = walker.getAnalysisBits();
/*  98 */         bits |= bit;
/*  99 */         walker = walker.getNextWalker();
/*     */       }
/*     */     }
/* 102 */     return bits;
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */     throws CloneNotSupportedException
/*     */   {
/* 116 */     WalkingIterator clone = (WalkingIterator)super.clone();
/*     */ 
/* 120 */     if (null != this.m_firstWalker)
/*     */     {
/* 122 */       clone.m_firstWalker = this.m_firstWalker.cloneDeep(clone, null);
/*     */     }
/*     */ 
/* 125 */     return clone;
/*     */   }
/*     */ 
/*     */   public void reset()
/*     */   {
/* 134 */     super.reset();
/*     */ 
/* 136 */     if (null != this.m_firstWalker)
/*     */     {
/* 138 */       this.m_lastUsedWalker = this.m_firstWalker;
/*     */ 
/* 140 */       this.m_firstWalker.setRoot(this.m_context);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setRoot(int context, Object environment)
/*     */   {
/* 155 */     super.setRoot(context, environment);
/*     */ 
/* 157 */     if (null != this.m_firstWalker)
/*     */     {
/* 159 */       this.m_firstWalker.setRoot(context);
/* 160 */       this.m_lastUsedWalker = this.m_firstWalker;
/*     */     }
/*     */   }
/*     */ 
/*     */   public int nextNode()
/*     */   {
/* 173 */     if (this.m_foundLast) {
/* 174 */       return -1;
/*     */     }
/*     */ 
/* 184 */     if (-1 == this.m_stackFrame)
/*     */     {
/* 186 */       return returnNextNode(this.m_firstWalker.nextNode());
/*     */     }
/*     */ 
/* 190 */     VariableStack vars = this.m_execContext.getVarStack();
/*     */ 
/* 193 */     int savedStart = vars.getStackFrame();
/*     */ 
/* 195 */     vars.setStackFrame(this.m_stackFrame);
/*     */ 
/* 197 */     int n = returnNextNode(this.m_firstWalker.nextNode());
/*     */ 
/* 200 */     vars.setStackFrame(savedStart);
/*     */ 
/* 202 */     return n;
/*     */   }
/*     */ 
/*     */   public final AxesWalker getFirstWalker()
/*     */   {
/* 216 */     return this.m_firstWalker;
/*     */   }
/*     */ 
/*     */   public final void setFirstWalker(AxesWalker walker)
/*     */   {
/* 227 */     this.m_firstWalker = walker;
/*     */   }
/*     */ 
/*     */   public final void setLastUsedWalker(AxesWalker walker)
/*     */   {
/* 239 */     this.m_lastUsedWalker = walker;
/*     */   }
/*     */ 
/*     */   public final AxesWalker getLastUsedWalker()
/*     */   {
/* 250 */     return this.m_lastUsedWalker;
/*     */   }
/*     */ 
/*     */   public void detach()
/*     */   {
/* 262 */     if (this.m_allowDetach)
/*     */     {
/* 264 */       AxesWalker walker = this.m_firstWalker;
/* 265 */       while (null != walker)
/*     */       {
/* 267 */         walker.detach();
/* 268 */         walker = walker.getNextWalker();
/*     */       }
/*     */ 
/* 271 */       this.m_lastUsedWalker = null;
/*     */ 
/* 274 */       super.detach();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void fixupVariables(Vector vars, int globalsSize)
/*     */   {
/* 290 */     this.m_predicateIndex = -1;
/*     */ 
/* 292 */     AxesWalker walker = this.m_firstWalker;
/*     */ 
/* 294 */     while (null != walker)
/*     */     {
/* 296 */       walker.fixupVariables(vars, globalsSize);
/* 297 */       walker = walker.getNextWalker();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void callVisitors(ExpressionOwner owner, XPathVisitor visitor)
/*     */   {
/* 306 */     if (visitor.visitLocationPath(owner, this))
/*     */     {
/* 308 */       if (null != this.m_firstWalker)
/*     */       {
/* 310 */         this.m_firstWalker.callVisitors(this, visitor);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public Expression getExpression()
/*     */   {
/* 329 */     return this.m_firstWalker;
/*     */   }
/*     */ 
/*     */   public void setExpression(Expression exp)
/*     */   {
/* 337 */     exp.exprSetParent(this);
/* 338 */     this.m_firstWalker = ((AxesWalker)exp);
/*     */   }
/*     */ 
/*     */   public boolean deepEquals(Expression expr)
/*     */   {
/* 346 */     if (!super.deepEquals(expr)) {
/* 347 */       return false;
/*     */     }
/* 349 */     AxesWalker walker1 = this.m_firstWalker;
/* 350 */     AxesWalker walker2 = ((WalkingIterator)expr).m_firstWalker;
/* 351 */     while ((null != walker1) && (null != walker2))
/*     */     {
/* 353 */       if (!walker1.deepEquals(walker2))
/* 354 */         return false;
/* 355 */       walker1 = walker1.getNextWalker();
/* 356 */       walker2 = walker2.getNextWalker();
/*     */     }
/*     */ 
/* 359 */     if ((null != walker1) || (null != walker2)) {
/* 360 */       return false;
/*     */     }
/* 362 */     return true;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.axes.WalkingIterator
 * JD-Core Version:    0.6.2
 */