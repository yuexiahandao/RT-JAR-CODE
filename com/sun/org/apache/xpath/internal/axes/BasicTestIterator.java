/*     */ package com.sun.org.apache.xpath.internal.axes;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.dtm.DTMIterator;
/*     */ import com.sun.org.apache.xml.internal.utils.PrefixResolver;
/*     */ import com.sun.org.apache.xpath.internal.VariableStack;
/*     */ import com.sun.org.apache.xpath.internal.XPathContext;
/*     */ import com.sun.org.apache.xpath.internal.compiler.Compiler;
/*     */ import com.sun.org.apache.xpath.internal.compiler.OpMap;
/*     */ import javax.xml.transform.TransformerException;
/*     */ 
/*     */ public abstract class BasicTestIterator extends LocPathIterator
/*     */ {
/*     */   static final long serialVersionUID = 3505378079378096623L;
/*     */ 
/*     */   protected BasicTestIterator()
/*     */   {
/*     */   }
/*     */ 
/*     */   protected BasicTestIterator(PrefixResolver nscontext)
/*     */   {
/*  60 */     super(nscontext);
/*     */   }
/*     */ 
/*     */   protected BasicTestIterator(Compiler compiler, int opPos, int analysis)
/*     */     throws TransformerException
/*     */   {
/*  78 */     super(compiler, opPos, analysis, false);
/*     */ 
/*  80 */     int firstStepPos = OpMap.getFirstChildPos(opPos);
/*  81 */     int whatToShow = compiler.getWhatToShow(firstStepPos);
/*     */ 
/*  83 */     if ((0 == (whatToShow & 0x1043)) || (whatToShow == -1))
/*     */     {
/*  89 */       initNodeTest(whatToShow);
/*     */     }
/*     */     else {
/*  92 */       initNodeTest(whatToShow, compiler.getStepNS(firstStepPos), compiler.getStepLocalName(firstStepPos));
/*     */     }
/*     */ 
/*  95 */     initPredicateInfo(compiler, firstStepPos);
/*     */   }
/*     */ 
/*     */   protected BasicTestIterator(Compiler compiler, int opPos, int analysis, boolean shouldLoadWalkers)
/*     */     throws TransformerException
/*     */   {
/* 117 */     super(compiler, opPos, analysis, shouldLoadWalkers);
/*     */   }
/*     */ 
/*     */   protected abstract int getNextNode();
/*     */ 
/*     */   public int nextNode()
/*     */   {
/* 137 */     if (this.m_foundLast)
/*     */     {
/* 139 */       this.m_lastFetched = -1;
/* 140 */       return -1;
/*     */     }
/*     */ 
/* 143 */     if (-1 == this.m_lastFetched)
/*     */     {
/* 145 */       resetProximityPositions();
/*     */     }
/*     */     VariableStack vars;
/*     */     int savedStart;
/* 152 */     if (-1 != this.m_stackFrame)
/*     */     {
/* 154 */       VariableStack vars = this.m_execContext.getVarStack();
/*     */ 
/* 157 */       int savedStart = vars.getStackFrame();
/*     */ 
/* 159 */       vars.setStackFrame(this.m_stackFrame);
/*     */     }
/*     */     else
/*     */     {
/* 164 */       vars = null;
/* 165 */       savedStart = 0;
/*     */     }
/*     */     try
/*     */     {
/*     */       int next;
/*     */       do
/*     */       {
/* 172 */         next = getNextNode();
/*     */       }
/* 174 */       while ((-1 != next) && 
/* 176 */         (1 != acceptNode(next)) && 
/* 184 */         (next != -1));
/*     */       int i;
/* 186 */       if (-1 != next)
/*     */       {
/* 188 */         this.m_pos += 1;
/* 189 */         return next;
/*     */       }
/*     */ 
/* 193 */       this.m_foundLast = true;
/*     */ 
/* 195 */       return -1;
/*     */     }
/*     */     finally
/*     */     {
/* 200 */       if (-1 != this.m_stackFrame)
/*     */       {
/* 203 */         vars.setStackFrame(savedStart);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public DTMIterator cloneWithReset()
/*     */     throws CloneNotSupportedException
/*     */   {
/* 219 */     ChildTestIterator clone = (ChildTestIterator)super.cloneWithReset();
/*     */ 
/* 221 */     clone.resetProximityPositions();
/*     */ 
/* 223 */     return clone;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.axes.BasicTestIterator
 * JD-Core Version:    0.6.2
 */