/*     */ package com.sun.org.apache.xml.internal.dtm.ref;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.dtm.DTM;
/*     */ import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
/*     */ import com.sun.org.apache.xml.internal.utils.IntVector;
/*     */ import org.w3c.dom.Node;
/*     */ 
/*     */ public class DTMAxisIterNodeList extends DTMNodeListBase
/*     */ {
/*     */   private DTM m_dtm;
/*     */   private DTMAxisIterator m_iter;
/*     */   private IntVector m_cachedNodes;
/*  64 */   private int m_last = -1;
/*     */ 
/*     */   private DTMAxisIterNodeList()
/*     */   {
/*     */   }
/*     */ 
/*     */   public DTMAxisIterNodeList(DTM dtm, DTMAxisIterator dtmAxisIterator)
/*     */   {
/*  75 */     if (dtmAxisIterator == null) {
/*  76 */       this.m_last = 0;
/*     */     } else {
/*  78 */       this.m_cachedNodes = new IntVector();
/*  79 */       this.m_dtm = dtm;
/*     */     }
/*  81 */     this.m_iter = dtmAxisIterator;
/*     */   }
/*     */ 
/*     */   public DTMAxisIterator getDTMAxisIterator()
/*     */   {
/*  90 */     return this.m_iter;
/*     */   }
/*     */ 
/*     */   public Node item(int index)
/*     */   {
/* 107 */     if (this.m_iter != null) {
/* 108 */       int node = 0;
/* 109 */       int count = this.m_cachedNodes.size();
/*     */ 
/* 111 */       if (count > index) {
/* 112 */         node = this.m_cachedNodes.elementAt(index);
/* 113 */         return this.m_dtm.getNode(node);
/* 114 */       }if (this.m_last == -1)
/*     */       {
/* 116 */         while ((count <= index) && ((node = this.m_iter.next()) != -1)) {
/* 117 */           this.m_cachedNodes.addElement(node);
/* 118 */           count++;
/*     */         }
/* 120 */         if (node == -1)
/* 121 */           this.m_last = count;
/*     */         else {
/* 123 */           return this.m_dtm.getNode(node);
/*     */         }
/*     */       }
/*     */     }
/* 127 */     return null;
/*     */   }
/*     */ 
/*     */   public int getLength()
/*     */   {
/* 135 */     if (this.m_last == -1)
/*     */     {
/*     */       int node;
/* 137 */       while ((node = this.m_iter.next()) != -1) {
/* 138 */         this.m_cachedNodes.addElement(node);
/*     */       }
/* 140 */       this.m_last = this.m_cachedNodes.size();
/*     */     }
/* 142 */     return this.m_last;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.dtm.ref.DTMAxisIterNodeList
 * JD-Core Version:    0.6.2
 */