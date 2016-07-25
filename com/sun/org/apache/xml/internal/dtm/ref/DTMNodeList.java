/*     */ package com.sun.org.apache.xml.internal.dtm.ref;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.dtm.DTM;
/*     */ import com.sun.org.apache.xml.internal.dtm.DTMIterator;
/*     */ import org.w3c.dom.Node;
/*     */ 
/*     */ public class DTMNodeList extends DTMNodeListBase
/*     */ {
/*     */   private DTMIterator m_iter;
/*     */ 
/*     */   private DTMNodeList()
/*     */   {
/*     */   }
/*     */ 
/*     */   public DTMNodeList(DTMIterator dtmIterator)
/*     */   {
/*  75 */     if (dtmIterator != null) {
/*  76 */       int pos = dtmIterator.getCurrentPos();
/*     */       try {
/*  78 */         this.m_iter = dtmIterator.cloneWithReset();
/*     */       } catch (CloneNotSupportedException cnse) {
/*  80 */         this.m_iter = dtmIterator;
/*     */       }
/*  82 */       this.m_iter.setShouldCacheNodes(true);
/*  83 */       this.m_iter.runTo(-1);
/*  84 */       this.m_iter.setCurrentPos(pos);
/*     */     }
/*     */   }
/*     */ 
/*     */   public DTMIterator getDTMIterator()
/*     */   {
/*  94 */     return this.m_iter;
/*     */   }
/*     */ 
/*     */   public Node item(int index)
/*     */   {
/* 111 */     if (this.m_iter != null) {
/* 112 */       int handle = this.m_iter.item(index);
/* 113 */       if (handle == -1) {
/* 114 */         return null;
/*     */       }
/* 116 */       return this.m_iter.getDTM(handle).getNode(handle);
/*     */     }
/* 118 */     return null;
/*     */   }
/*     */ 
/*     */   public int getLength()
/*     */   {
/* 127 */     return this.m_iter != null ? this.m_iter.getLength() : 0;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.dtm.ref.DTMNodeList
 * JD-Core Version:    0.6.2
 */