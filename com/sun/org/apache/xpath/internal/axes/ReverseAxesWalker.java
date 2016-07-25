/*     */ package com.sun.org.apache.xpath.internal.axes;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.dtm.DTM;
/*     */ import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
/*     */ import com.sun.org.apache.xpath.internal.XPathContext;
/*     */ 
/*     */ public class ReverseAxesWalker extends AxesWalker
/*     */ {
/*     */   static final long serialVersionUID = 2847007647832768941L;
/*     */   protected DTMAxisIterator m_iterator;
/*     */ 
/*     */   ReverseAxesWalker(LocPathIterator locPathIterator, int axis)
/*     */   {
/*  44 */     super(locPathIterator, axis);
/*     */   }
/*     */ 
/*     */   public void setRoot(int root)
/*     */   {
/*  55 */     super.setRoot(root);
/*  56 */     this.m_iterator = getDTM(root).getAxisIterator(this.m_axis);
/*  57 */     this.m_iterator.setStartNode(root);
/*     */   }
/*     */ 
/*     */   public void detach()
/*     */   {
/*  67 */     this.m_iterator = null;
/*  68 */     super.detach();
/*     */   }
/*     */ 
/*     */   protected int getNextNode()
/*     */   {
/*  78 */     if (this.m_foundLast) {
/*  79 */       return -1;
/*     */     }
/*  81 */     int next = this.m_iterator.next();
/*     */ 
/*  83 */     if (this.m_isFresh) {
/*  84 */       this.m_isFresh = false;
/*     */     }
/*  86 */     if (-1 == next) {
/*  87 */       this.m_foundLast = true;
/*     */     }
/*  89 */     return next;
/*     */   }
/*     */ 
/*     */   public boolean isReverseAxes()
/*     */   {
/* 100 */     return true;
/*     */   }
/*     */ 
/*     */   protected int getProximityPosition(int predicateIndex)
/*     */   {
/* 129 */     if (predicateIndex < 0) {
/* 130 */       return -1;
/*     */     }
/* 132 */     int count = this.m_proximityPositions[predicateIndex];
/*     */ 
/* 134 */     if (count <= 0)
/*     */     {
/* 136 */       AxesWalker savedWalker = wi().getLastUsedWalker();
/*     */       try
/*     */       {
/* 140 */         ReverseAxesWalker clone = (ReverseAxesWalker)clone();
/*     */ 
/* 142 */         clone.setRoot(getRoot());
/*     */ 
/* 144 */         clone.setPredicateCount(predicateIndex);
/*     */ 
/* 146 */         clone.setPrevWalker(null);
/* 147 */         clone.setNextWalker(null);
/* 148 */         wi().setLastUsedWalker(clone);
/*     */ 
/* 151 */         count++;
/*     */         int next;
/* 154 */         while (-1 != (next = clone.nextNode()))
/*     */         {
/* 156 */           count++;
/*     */         }
/*     */ 
/* 159 */         this.m_proximityPositions[predicateIndex] = count;
/*     */       }
/*     */       catch (CloneNotSupportedException cnse)
/*     */       {
/*     */       }
/*     */       finally
/*     */       {
/* 168 */         wi().setLastUsedWalker(savedWalker);
/*     */       }
/*     */     }
/*     */ 
/* 172 */     return count;
/*     */   }
/*     */ 
/*     */   protected void countProximityPosition(int i)
/*     */   {
/* 182 */     if (i < this.m_proximityPositions.length)
/* 183 */       this.m_proximityPositions[i] -= 1;
/*     */   }
/*     */ 
/*     */   public int getLastPos(XPathContext xctxt)
/*     */   {
/* 198 */     int count = 0;
/* 199 */     AxesWalker savedWalker = wi().getLastUsedWalker();
/*     */     try
/*     */     {
/* 203 */       ReverseAxesWalker clone = (ReverseAxesWalker)clone();
/*     */ 
/* 205 */       clone.setRoot(getRoot());
/*     */ 
/* 207 */       clone.setPredicateCount(getPredicateCount() - 1);
/*     */ 
/* 209 */       clone.setPrevWalker(null);
/* 210 */       clone.setNextWalker(null);
/* 211 */       wi().setLastUsedWalker(clone);
/*     */       int next;
/* 217 */       while (-1 != (next = clone.nextNode()))
/*     */       {
/* 219 */         count++;
/*     */       }
/*     */ 
/*     */     }
/*     */     catch (CloneNotSupportedException cnse)
/*     */     {
/*     */     }
/*     */     finally
/*     */     {
/* 229 */       wi().setLastUsedWalker(savedWalker);
/*     */     }
/*     */ 
/* 232 */     return count;
/*     */   }
/*     */ 
/*     */   public boolean isDocOrdered()
/*     */   {
/* 244 */     return false;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.axes.ReverseAxesWalker
 * JD-Core Version:    0.6.2
 */