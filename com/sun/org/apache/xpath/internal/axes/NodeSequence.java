/*     */ package com.sun.org.apache.xpath.internal.axes;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.dtm.DTM;
/*     */ import com.sun.org.apache.xml.internal.dtm.DTMIterator;
/*     */ import com.sun.org.apache.xml.internal.dtm.DTMManager;
/*     */ import com.sun.org.apache.xml.internal.utils.NodeVector;
/*     */ import com.sun.org.apache.xpath.internal.NodeSetDTM;
/*     */ import com.sun.org.apache.xpath.internal.XPathContext;
/*     */ import com.sun.org.apache.xpath.internal.objects.XObject;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class NodeSequence extends XObject
/*     */   implements DTMIterator, Cloneable, PathComponent
/*     */ {
/*     */   static final long serialVersionUID = 3866261934726581044L;
/*  45 */   protected int m_last = -1;
/*     */ 
/*  52 */   protected int m_next = 0;
/*     */   private IteratorCache m_cache;
/*     */   protected DTMIterator m_iter;
/*     */   protected DTMManager m_dtmMgr;
/*     */ 
/*     */   protected NodeVector getVector()
/*     */   {
/*  68 */     NodeVector nv = this.m_cache != null ? this.m_cache.getVector() : null;
/*  69 */     return nv;
/*     */   }
/*     */ 
/*     */   private IteratorCache getCache()
/*     */   {
/*  79 */     return this.m_cache;
/*     */   }
/*     */ 
/*     */   protected void SetVector(NodeVector v)
/*     */   {
/*  87 */     setObject(v);
/*     */   }
/*     */ 
/*     */   public boolean hasCache()
/*     */   {
/*  97 */     NodeVector nv = getVector();
/*  98 */     return nv != null;
/*     */   }
/*     */ 
/*     */   private boolean cacheComplete()
/*     */   {
/*     */     boolean complete;
/*     */     boolean complete;
/* 108 */     if (this.m_cache != null)
/* 109 */       complete = this.m_cache.isComplete();
/*     */     else {
/* 111 */       complete = false;
/*     */     }
/* 113 */     return complete;
/*     */   }
/*     */ 
/*     */   private void markCacheComplete()
/*     */   {
/* 121 */     NodeVector nv = getVector();
/* 122 */     if (nv != null)
/* 123 */       this.m_cache.setCacheComplete(true);
/*     */   }
/*     */ 
/*     */   public final void setIter(DTMIterator iter)
/*     */   {
/* 139 */     this.m_iter = iter;
/*     */   }
/*     */ 
/*     */   public final DTMIterator getContainedIter()
/*     */   {
/* 148 */     return this.m_iter;
/*     */   }
/*     */ 
/*     */   private NodeSequence(DTMIterator iter, int context, XPathContext xctxt, boolean shouldCacheNodes)
/*     */   {
/* 169 */     setIter(iter);
/* 170 */     setRoot(context, xctxt);
/* 171 */     setShouldCacheNodes(shouldCacheNodes);
/*     */   }
/*     */ 
/*     */   public NodeSequence(Object nodeVector)
/*     */   {
/* 181 */     super(nodeVector);
/* 182 */     if ((nodeVector instanceof NodeVector)) {
/* 183 */       SetVector((NodeVector)nodeVector);
/*     */     }
/* 185 */     if (null != nodeVector)
/*     */     {
/* 187 */       assertion(nodeVector instanceof NodeVector, "Must have a NodeVector as the object for NodeSequence!");
/*     */ 
/* 189 */       if ((nodeVector instanceof PathComponent))
/*     */       {
/* 191 */         setIter((PathComponent)nodeVector);
/* 192 */         this.m_last = ((PathComponent)nodeVector).getLength();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private NodeSequence(DTMManager dtmMgr)
/*     */   {
/* 204 */     super(new NodeVector());
/* 205 */     this.m_last = 0;
/* 206 */     this.m_dtmMgr = dtmMgr;
/*     */   }
/*     */ 
/*     */   public NodeSequence()
/*     */   {
/*     */   }
/*     */ 
/*     */   public DTM getDTM(int nodeHandle)
/*     */   {
/* 224 */     DTMManager mgr = getDTMManager();
/* 225 */     if (null != mgr) {
/* 226 */       return getDTMManager().getDTM(nodeHandle);
/*     */     }
/*     */ 
/* 229 */     assertion(false, "Can not get a DTM Unless a DTMManager has been set!");
/* 230 */     return null;
/*     */   }
/*     */ 
/*     */   public DTMManager getDTMManager()
/*     */   {
/* 239 */     return this.m_dtmMgr;
/*     */   }
/*     */ 
/*     */   public int getRoot()
/*     */   {
/* 247 */     if (null != this.m_iter) {
/* 248 */       return this.m_iter.getRoot();
/*     */     }
/*     */ 
/* 254 */     return -1;
/*     */   }
/*     */ 
/*     */   public void setRoot(int nodeHandle, Object environment)
/*     */   {
/* 264 */     if (nodeHandle == -1)
/*     */     {
/* 266 */       throw new RuntimeException("Unable to evaluate expression using this context");
/*     */     }
/*     */ 
/* 270 */     if (null != this.m_iter)
/*     */     {
/* 272 */       XPathContext xctxt = (XPathContext)environment;
/* 273 */       this.m_dtmMgr = xctxt.getDTMManager();
/* 274 */       this.m_iter.setRoot(nodeHandle, environment);
/* 275 */       if (!this.m_iter.isDocOrdered())
/*     */       {
/* 277 */         if (!hasCache())
/* 278 */           setShouldCacheNodes(true);
/* 279 */         runTo(-1);
/* 280 */         this.m_next = 0;
/*     */       }
/*     */     }
/*     */     else {
/* 284 */       assertion(false, "Can not setRoot on a non-iterated NodeSequence!");
/*     */     }
/*     */   }
/*     */ 
/*     */   public void reset()
/*     */   {
/* 292 */     this.m_next = 0;
/*     */   }
/*     */ 
/*     */   public int getWhatToShow()
/*     */   {
/* 301 */     return hasCache() ? -17 : this.m_iter.getWhatToShow();
/*     */   }
/*     */ 
/*     */   public boolean getExpandEntityReferences()
/*     */   {
/* 310 */     if (null != this.m_iter) {
/* 311 */       return this.m_iter.getExpandEntityReferences();
/*     */     }
/* 313 */     return true;
/*     */   }
/*     */ 
/*     */   public int nextNode()
/*     */   {
/* 323 */     NodeVector vec = getVector();
/* 324 */     if (null != vec)
/*     */     {
/* 327 */       if (this.m_next < vec.size())
/*     */       {
/* 330 */         int next = vec.elementAt(this.m_next);
/* 331 */         this.m_next += 1;
/* 332 */         return next;
/*     */       }
/* 334 */       if ((cacheComplete()) || (-1 != this.m_last) || (null == this.m_iter))
/*     */       {
/* 336 */         this.m_next += 1;
/* 337 */         return -1;
/*     */       }
/*     */     }
/*     */ 
/* 341 */     if (null == this.m_iter) {
/* 342 */       return -1;
/*     */     }
/* 344 */     int next = this.m_iter.nextNode();
/* 345 */     if (-1 != next)
/*     */     {
/* 347 */       if (hasCache())
/*     */       {
/* 349 */         if (this.m_iter.isDocOrdered())
/*     */         {
/* 351 */           getVector().addElement(next);
/* 352 */           this.m_next += 1;
/*     */         }
/*     */         else
/*     */         {
/* 356 */           int insertIndex = addNodeInDocOrder(next);
/* 357 */           if (insertIndex >= 0)
/* 358 */             this.m_next += 1;
/*     */         }
/*     */       }
/*     */       else {
/* 362 */         this.m_next += 1;
/*     */       }
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 369 */       markCacheComplete();
/*     */ 
/* 371 */       this.m_last = this.m_next;
/* 372 */       this.m_next += 1;
/*     */     }
/*     */ 
/* 375 */     return next;
/*     */   }
/*     */ 
/*     */   public int previousNode()
/*     */   {
/* 383 */     if (hasCache())
/*     */     {
/* 385 */       if (this.m_next <= 0) {
/* 386 */         return -1;
/*     */       }
/*     */ 
/* 389 */       this.m_next -= 1;
/* 390 */       return item(this.m_next);
/*     */     }
/*     */ 
/* 395 */     int n = this.m_iter.previousNode();
/* 396 */     this.m_next = this.m_iter.getCurrentPos();
/* 397 */     return this.m_next;
/*     */   }
/*     */ 
/*     */   public void detach()
/*     */   {
/* 406 */     if (null != this.m_iter)
/* 407 */       this.m_iter.detach();
/* 408 */     super.detach();
/*     */   }
/*     */ 
/*     */   public void allowDetachToRelease(boolean allowRelease)
/*     */   {
/* 418 */     if ((false == allowRelease) && (!hasCache()))
/*     */     {
/* 420 */       setShouldCacheNodes(true);
/*     */     }
/*     */ 
/* 423 */     if (null != this.m_iter)
/* 424 */       this.m_iter.allowDetachToRelease(allowRelease);
/* 425 */     super.allowDetachToRelease(allowRelease);
/*     */   }
/*     */ 
/*     */   public int getCurrentNode()
/*     */   {
/* 433 */     if (hasCache())
/*     */     {
/* 435 */       int currentIndex = this.m_next - 1;
/* 436 */       NodeVector vec = getVector();
/* 437 */       if ((currentIndex >= 0) && (currentIndex < vec.size())) {
/* 438 */         return vec.elementAt(currentIndex);
/*     */       }
/* 440 */       return -1;
/*     */     }
/*     */ 
/* 443 */     if (null != this.m_iter)
/*     */     {
/* 445 */       return this.m_iter.getCurrentNode();
/*     */     }
/*     */ 
/* 448 */     return -1;
/*     */   }
/*     */ 
/*     */   public boolean isFresh()
/*     */   {
/* 456 */     return 0 == this.m_next;
/*     */   }
/*     */ 
/*     */   public void setShouldCacheNodes(boolean b)
/*     */   {
/* 464 */     if (b)
/*     */     {
/* 466 */       if (!hasCache())
/*     */       {
/* 468 */         SetVector(new NodeVector());
/*     */       }
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 474 */       SetVector(null);
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean isMutable()
/*     */   {
/* 482 */     return hasCache();
/*     */   }
/*     */ 
/*     */   public int getCurrentPos()
/*     */   {
/* 490 */     return this.m_next;
/*     */   }
/*     */ 
/*     */   public void runTo(int index)
/*     */   {
/* 500 */     if (-1 == index)
/*     */     {
/* 502 */       int pos = this.m_next;
/*     */       int n;
/* 503 */       while (-1 != (n = nextNode()));
/* 504 */       this.m_next = pos;
/*     */     } else {
/* 506 */       if (this.m_next == index)
/*     */       {
/* 508 */         return;
/*     */       }
/* 510 */       if ((hasCache()) && (index < getVector().size()))
/*     */       {
/* 512 */         this.m_next = index;
/*     */       } else {
/* 514 */         if ((null == getVector()) && (index < this.m_next));
/*     */         int n;
/* 516 */         while ((this.m_next >= index) && (-1 != (n = previousNode()))) { continue;
/*     */           int n;
/* 520 */           while ((this.m_next < index) && (-1 != (n = nextNode())));
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setCurrentPos(int i)
/*     */   {
/* 530 */     runTo(i);
/*     */   }
/*     */ 
/*     */   public int item(int index)
/*     */   {
/* 538 */     setCurrentPos(index);
/* 539 */     int n = nextNode();
/* 540 */     this.m_next = index;
/* 541 */     return n;
/*     */   }
/*     */ 
/*     */   public void setItem(int node, int index)
/*     */   {
/* 549 */     NodeVector vec = getVector();
/* 550 */     if (null != vec)
/*     */     {
/* 552 */       int oldNode = vec.elementAt(index);
/* 553 */       if ((oldNode != node) && (this.m_cache.useCount() > 1))
/*     */       {
/* 561 */         IteratorCache newCache = new IteratorCache();
/*     */         NodeVector nv;
/*     */         try
/*     */         {
/* 564 */           nv = (NodeVector)vec.clone();
/*     */         }
/*     */         catch (CloneNotSupportedException e) {
/* 567 */           e.printStackTrace();
/* 568 */           RuntimeException rte = new RuntimeException(e.getMessage());
/* 569 */           throw rte;
/*     */         }
/* 571 */         newCache.setVector(nv);
/* 572 */         newCache.setCacheComplete(true);
/* 573 */         this.m_cache = newCache;
/* 574 */         vec = nv;
/*     */ 
/* 577 */         super.setObject(nv);
/*     */       }
/*     */ 
/* 586 */       vec.setElementAt(node, index);
/* 587 */       this.m_last = vec.size();
/*     */     }
/*     */     else {
/* 590 */       this.m_iter.setItem(node, index);
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getLength()
/*     */   {
/* 598 */     IteratorCache cache = getCache();
/*     */ 
/* 600 */     if (cache != null)
/*     */     {
/* 603 */       if (cache.isComplete())
/*     */       {
/* 606 */         NodeVector nv = cache.getVector();
/* 607 */         return nv.size();
/*     */       }
/*     */ 
/* 613 */       if ((this.m_iter instanceof NodeSetDTM))
/*     */       {
/* 615 */         return this.m_iter.getLength();
/*     */       }
/*     */ 
/* 618 */       if (-1 == this.m_last)
/*     */       {
/* 620 */         int pos = this.m_next;
/* 621 */         runTo(-1);
/* 622 */         this.m_next = pos;
/*     */       }
/* 624 */       return this.m_last;
/*     */     }
/*     */ 
/* 628 */     return -1 == this.m_last ? (this.m_last = this.m_iter.getLength()) : this.m_last;
/*     */   }
/*     */ 
/*     */   public DTMIterator cloneWithReset()
/*     */     throws CloneNotSupportedException
/*     */   {
/* 638 */     NodeSequence seq = (NodeSequence)super.clone();
/* 639 */     seq.m_next = 0;
/* 640 */     if (this.m_cache != null)
/*     */     {
/* 646 */       this.m_cache.increaseUseCount();
/*     */     }
/*     */ 
/* 649 */     return seq;
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */     throws CloneNotSupportedException
/*     */   {
/* 663 */     NodeSequence clone = (NodeSequence)super.clone();
/* 664 */     if (null != this.m_iter) clone.m_iter = ((PathComponent)this.m_iter.clone());
/* 665 */     if (this.m_cache != null)
/*     */     {
/* 671 */       this.m_cache.increaseUseCount();
/*     */     }
/*     */ 
/* 674 */     return clone;
/*     */   }
/*     */ 
/*     */   public boolean isDocOrdered()
/*     */   {
/* 683 */     if (null != this.m_iter) {
/* 684 */       return this.m_iter.isDocOrdered();
/*     */     }
/* 686 */     return true;
/*     */   }
/*     */ 
/*     */   public int getAxis()
/*     */   {
/* 694 */     if (null != this.m_iter) {
/* 695 */       return this.m_iter.getAxis();
/*     */     }
/*     */ 
/* 698 */     assertion(false, "Can not getAxis from a non-iterated node sequence!");
/* 699 */     return 0;
/*     */   }
/*     */ 
/*     */   public int getAnalysisBits()
/*     */   {
/* 708 */     if ((null != this.m_iter) && ((this.m_iter instanceof PathComponent))) {
/* 709 */       return ((PathComponent)this.m_iter).getAnalysisBits();
/*     */     }
/* 711 */     return 0;
/*     */   }
/*     */ 
/*     */   public void fixupVariables(Vector vars, int globalsSize)
/*     */   {
/* 719 */     super.fixupVariables(vars, globalsSize);
/*     */   }
/*     */ 
/*     */   protected int addNodeInDocOrder(int node)
/*     */   {
/* 732 */     assertion(hasCache(), "addNodeInDocOrder must be done on a mutable sequence!");
/*     */ 
/* 734 */     int insertIndex = -1;
/*     */ 
/* 736 */     NodeVector vec = getVector();
/*     */ 
/* 741 */     int size = vec.size();
/*     */ 
/* 743 */     for (int i = size - 1; i >= 0; i--)
/*     */     {
/* 745 */       int child = vec.elementAt(i);
/*     */ 
/* 747 */       if (child == node)
/*     */       {
/* 749 */         i = -2;
/*     */       }
/*     */       else
/*     */       {
/* 754 */         DTM dtm = this.m_dtmMgr.getDTM(node);
/* 755 */         if (!dtm.isNodeAfter(node, child))
/*     */         {
/*     */           break;
/*     */         }
/*     */       }
/*     */     }
/* 761 */     if (i != -2)
/*     */     {
/* 763 */       insertIndex = i + 1;
/*     */ 
/* 765 */       vec.insertElementAt(node, insertIndex);
/*     */     }
/*     */ 
/* 769 */     return insertIndex;
/*     */   }
/*     */ 
/*     */   protected void setObject(Object obj)
/*     */   {
/* 787 */     if ((obj instanceof NodeVector))
/*     */     {
/* 790 */       super.setObject(obj);
/*     */ 
/* 793 */       NodeVector v = (NodeVector)obj;
/* 794 */       if (this.m_cache != null) {
/* 795 */         this.m_cache.setVector(v);
/* 796 */       } else if (v != null) {
/* 797 */         this.m_cache = new IteratorCache();
/* 798 */         this.m_cache.setVector(v);
/*     */       }
/* 800 */     } else if ((obj instanceof IteratorCache)) {
/* 801 */       IteratorCache cache = (IteratorCache)obj;
/* 802 */       this.m_cache = cache;
/* 803 */       this.m_cache.increaseUseCount();
/*     */ 
/* 806 */       super.setObject(cache.getVector());
/*     */     } else {
/* 808 */       super.setObject(obj);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected IteratorCache getIteratorCache()
/*     */   {
/* 959 */     return this.m_cache;
/*     */   }
/*     */ 
/*     */   private static final class IteratorCache
/*     */   {
/*     */     private NodeVector m_vec2;
/*     */     private boolean m_isComplete2;
/*     */     private int m_useCount2;
/*     */ 
/*     */     IteratorCache()
/*     */     {
/* 889 */       this.m_vec2 = null;
/* 890 */       this.m_isComplete2 = false;
/* 891 */       this.m_useCount2 = 1;
/*     */     }
/*     */ 
/*     */     private int useCount()
/*     */     {
/* 900 */       return this.m_useCount2;
/*     */     }
/*     */ 
/*     */     private void increaseUseCount()
/*     */     {
/* 910 */       if (this.m_vec2 != null)
/* 911 */         this.m_useCount2 += 1;
/*     */     }
/*     */ 
/*     */     private void setVector(NodeVector nv)
/*     */     {
/* 921 */       this.m_vec2 = nv;
/* 922 */       this.m_useCount2 = 1;
/*     */     }
/*     */ 
/*     */     private NodeVector getVector()
/*     */     {
/* 930 */       return this.m_vec2;
/*     */     }
/*     */ 
/*     */     private void setCacheComplete(boolean b)
/*     */     {
/* 939 */       this.m_isComplete2 = b;
/*     */     }
/*     */ 
/*     */     private boolean isComplete()
/*     */     {
/* 948 */       return this.m_isComplete2;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.axes.NodeSequence
 * JD-Core Version:    0.6.2
 */