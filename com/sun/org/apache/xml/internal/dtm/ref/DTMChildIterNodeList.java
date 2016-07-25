/*     */ package com.sun.org.apache.xml.internal.dtm.ref;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.dtm.DTM;
/*     */ import org.w3c.dom.Node;
/*     */ 
/*     */ public class DTMChildIterNodeList extends DTMNodeListBase
/*     */ {
/*     */   private int m_firstChild;
/*     */   private DTM m_parentDTM;
/*     */ 
/*     */   private DTMChildIterNodeList()
/*     */   {
/*     */   }
/*     */ 
/*     */   public DTMChildIterNodeList(DTM parentDTM, int parentHandle)
/*     */   {
/*  80 */     this.m_parentDTM = parentDTM;
/*  81 */     this.m_firstChild = parentDTM.getFirstChild(parentHandle);
/*     */   }
/*     */ 
/*     */   public Node item(int index)
/*     */   {
/*  98 */     int handle = this.m_firstChild;
/*     */     while (true) { index--; if ((index < 0) || (handle == -1)) break;
/* 100 */       handle = this.m_parentDTM.getNextSibling(handle);
/*     */     }
/* 102 */     if (handle == -1) {
/* 103 */       return null;
/*     */     }
/* 105 */     return this.m_parentDTM.getNode(handle);
/*     */   }
/*     */ 
/*     */   public int getLength()
/*     */   {
/* 113 */     int count = 0;
/* 114 */     for (int handle = this.m_firstChild; 
/* 115 */       handle != -1; 
/* 116 */       handle = this.m_parentDTM.getNextSibling(handle)) {
/* 117 */       count++;
/*     */     }
/* 119 */     return count;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.dtm.ref.DTMChildIterNodeList
 * JD-Core Version:    0.6.2
 */