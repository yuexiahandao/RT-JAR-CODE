/*     */ package com.sun.org.apache.xalan.internal.xsltc.dom;
/*     */ 
/*     */ import com.sun.org.apache.xalan.internal.xsltc.DOM;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.DOMEnhancedForDTM;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.StripFilter;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.runtime.Hashtable;
/*     */ import com.sun.org.apache.xml.internal.dtm.DTM;
/*     */ import com.sun.org.apache.xml.internal.dtm.DTMWSFilter;
/*     */ 
/*     */ public class DOMWSFilter
/*     */   implements DTMWSFilter
/*     */ {
/*     */   private AbstractTranslet m_translet;
/*     */   private StripFilter m_filter;
/*     */   private Hashtable m_mappings;
/*     */   private DTM m_currentDTM;
/*     */   private short[] m_currentMapping;
/*     */ 
/*     */   public DOMWSFilter(AbstractTranslet translet)
/*     */   {
/*  61 */     this.m_translet = translet;
/*  62 */     this.m_mappings = new Hashtable();
/*     */ 
/*  64 */     if ((translet instanceof StripFilter))
/*  65 */       this.m_filter = ((StripFilter)translet);
/*     */   }
/*     */ 
/*     */   public short getShouldStripSpace(int node, DTM dtm)
/*     */   {
/*  82 */     if ((this.m_filter != null) && ((dtm instanceof DOM))) {
/*  83 */       DOM dom = (DOM)dtm;
/*  84 */       int type = 0;
/*     */ 
/*  86 */       if ((dtm instanceof DOMEnhancedForDTM)) {
/*  87 */         DOMEnhancedForDTM mappableDOM = (DOMEnhancedForDTM)dtm;
/*     */         short[] mapping;
/*     */         short[] mapping;
/*  90 */         if (dtm == this.m_currentDTM) {
/*  91 */           mapping = this.m_currentMapping;
/*     */         }
/*     */         else {
/*  94 */           mapping = (short[])this.m_mappings.get(dtm);
/*  95 */           if (mapping == null) {
/*  96 */             mapping = mappableDOM.getMapping(this.m_translet.getNamesArray(), this.m_translet.getUrisArray(), this.m_translet.getTypesArray());
/*     */ 
/* 100 */             this.m_mappings.put(dtm, mapping);
/* 101 */             this.m_currentDTM = dtm;
/* 102 */             this.m_currentMapping = mapping;
/*     */           }
/*     */         }
/*     */ 
/* 106 */         int expType = mappableDOM.getExpandedTypeID(node);
/*     */ 
/* 113 */         if ((expType >= 0) && (expType < mapping.length))
/* 114 */           type = mapping[expType];
/*     */         else
/* 116 */           type = -1;
/*     */       }
/*     */       else
/*     */       {
/* 120 */         return 3;
/*     */       }
/*     */ 
/* 123 */       if (this.m_filter.stripSpace(dom, node, type)) {
/* 124 */         return 2;
/*     */       }
/* 126 */       return 1;
/*     */     }
/*     */ 
/* 129 */     return 1;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.dom.DOMWSFilter
 * JD-Core Version:    0.6.2
 */