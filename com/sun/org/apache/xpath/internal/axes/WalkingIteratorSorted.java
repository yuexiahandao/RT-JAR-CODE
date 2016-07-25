/*     */ package com.sun.org.apache.xpath.internal.axes;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.utils.PrefixResolver;
/*     */ import com.sun.org.apache.xpath.internal.compiler.Compiler;
/*     */ import java.util.Vector;
/*     */ import javax.xml.transform.TransformerException;
/*     */ 
/*     */ public class WalkingIteratorSorted extends WalkingIterator
/*     */ {
/*     */   static final long serialVersionUID = -4512512007542368213L;
/*  42 */   protected boolean m_inNaturalOrderStatic = false;
/*     */ 
/*     */   public WalkingIteratorSorted(PrefixResolver nscontext)
/*     */   {
/*  52 */     super(nscontext);
/*     */   }
/*     */ 
/*     */   WalkingIteratorSorted(Compiler compiler, int opPos, int analysis, boolean shouldLoadWalkers)
/*     */     throws TransformerException
/*     */   {
/*  74 */     super(compiler, opPos, analysis, shouldLoadWalkers);
/*     */   }
/*     */ 
/*     */   public boolean isDocOrdered()
/*     */   {
/*  85 */     return this.m_inNaturalOrderStatic;
/*     */   }
/*     */ 
/*     */   boolean canBeWalkedInNaturalDocOrderStatic()
/*     */   {
/*  98 */     if (null != this.m_firstWalker)
/*     */     {
/* 100 */       AxesWalker walker = this.m_firstWalker;
/* 101 */       int prevAxis = -1;
/* 102 */       boolean prevIsSimpleDownAxis = true;
/*     */ 
/* 104 */       for (int i = 0; null != walker; i++)
/*     */       {
/* 106 */         int axis = walker.getAxis();
/*     */ 
/* 108 */         if (walker.isDocOrdered())
/*     */         {
/* 110 */           boolean isSimpleDownAxis = (axis == 3) || (axis == 13) || (axis == 19);
/*     */ 
/* 115 */           if ((isSimpleDownAxis) || (axis == -1)) {
/* 116 */             walker = walker.getNextWalker();
/*     */           }
/*     */           else {
/* 119 */             boolean isLastWalker = null == walker.getNextWalker();
/* 120 */             if (isLastWalker)
/*     */             {
/* 122 */               if (((walker.isDocOrdered()) && ((axis == 4) || (axis == 5) || (axis == 17) || (axis == 18))) || (axis == 2))
/*     */               {
/* 125 */                 return true;
/*     */               }
/*     */             }
/* 127 */             return false;
/*     */           }
/*     */         }
/*     */         else {
/* 131 */           return false;
/*     */         }
/*     */       }
/* 133 */       return true;
/*     */     }
/* 135 */     return false;
/*     */   }
/*     */ 
/*     */   public void fixupVariables(Vector vars, int globalsSize)
/*     */   {
/* 200 */     super.fixupVariables(vars, globalsSize);
/*     */ 
/* 202 */     int analysis = getAnalysisBits();
/* 203 */     if (WalkerFactory.isNaturalDocOrder(analysis))
/*     */     {
/* 205 */       this.m_inNaturalOrderStatic = true;
/*     */     }
/*     */     else
/*     */     {
/* 209 */       this.m_inNaturalOrderStatic = false;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.axes.WalkingIteratorSorted
 * JD-Core Version:    0.6.2
 */