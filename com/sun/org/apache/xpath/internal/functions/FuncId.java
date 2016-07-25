/*     */ package com.sun.org.apache.xpath.internal.functions;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.dtm.DTM;
/*     */ import com.sun.org.apache.xml.internal.dtm.DTMIterator;
/*     */ import com.sun.org.apache.xml.internal.utils.StringVector;
/*     */ import com.sun.org.apache.xml.internal.utils.XMLString;
/*     */ import com.sun.org.apache.xpath.internal.Expression;
/*     */ import com.sun.org.apache.xpath.internal.NodeSetDTM;
/*     */ import com.sun.org.apache.xpath.internal.XPathContext;
/*     */ import com.sun.org.apache.xpath.internal.objects.XNodeSet;
/*     */ import com.sun.org.apache.xpath.internal.objects.XObject;
/*     */ import java.util.StringTokenizer;
/*     */ import javax.xml.transform.TransformerException;
/*     */ 
/*     */ public class FuncId extends FunctionOneArg
/*     */ {
/*     */   static final long serialVersionUID = 8930573966143567310L;
/*     */ 
/*     */   private StringVector getNodesByID(XPathContext xctxt, int docContext, String refval, StringVector usedrefs, NodeSetDTM nodeSet, boolean mayBeMore)
/*     */   {
/*  62 */     if (null != refval)
/*     */     {
/*  64 */       String ref = null;
/*     */ 
/*  66 */       StringTokenizer tokenizer = new StringTokenizer(refval);
/*  67 */       boolean hasMore = tokenizer.hasMoreTokens();
/*  68 */       DTM dtm = xctxt.getDTM(docContext);
/*     */ 
/*  70 */       while (hasMore)
/*     */       {
/*  72 */         ref = tokenizer.nextToken();
/*  73 */         hasMore = tokenizer.hasMoreTokens();
/*     */ 
/*  75 */         if ((null != usedrefs) && (usedrefs.contains(ref)))
/*     */         {
/*  77 */           ref = null;
/*     */         }
/*     */         else
/*     */         {
/*  82 */           int node = dtm.getElementById(ref);
/*     */ 
/*  84 */           if (-1 != node) {
/*  85 */             nodeSet.addNodeInDocOrder(node, xctxt);
/*     */           }
/*  87 */           if ((null != ref) && ((hasMore) || (mayBeMore)))
/*     */           {
/*  89 */             if (null == usedrefs) {
/*  90 */               usedrefs = new StringVector();
/*     */             }
/*  92 */             usedrefs.addElement(ref);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*  97 */     return usedrefs;
/*     */   }
/*     */ 
/*     */   public XObject execute(XPathContext xctxt)
/*     */     throws TransformerException
/*     */   {
/* 111 */     int context = xctxt.getCurrentNode();
/* 112 */     DTM dtm = xctxt.getDTM(context);
/* 113 */     int docContext = dtm.getDocument();
/*     */ 
/* 115 */     if (-1 == docContext) {
/* 116 */       error(xctxt, "ER_CONTEXT_HAS_NO_OWNERDOC", null);
/*     */     }
/* 118 */     XObject arg = this.m_arg0.execute(xctxt);
/* 119 */     int argType = arg.getType();
/* 120 */     XNodeSet nodes = new XNodeSet(xctxt.getDTMManager());
/* 121 */     NodeSetDTM nodeSet = nodes.mutableNodeset();
/*     */ 
/* 123 */     if (4 == argType)
/*     */     {
/* 125 */       DTMIterator ni = arg.iter();
/* 126 */       StringVector usedrefs = null;
/* 127 */       int pos = ni.nextNode();
/*     */ 
/* 129 */       while (-1 != pos)
/*     */       {
/* 131 */         DTM ndtm = ni.getDTM(pos);
/* 132 */         String refval = ndtm.getStringValue(pos).toString();
/*     */ 
/* 134 */         pos = ni.nextNode();
/* 135 */         usedrefs = getNodesByID(xctxt, docContext, refval, usedrefs, nodeSet, -1 != pos);
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 140 */       if (-1 == argType)
/*     */       {
/* 142 */         return nodes;
/*     */       }
/*     */ 
/* 146 */       String refval = arg.str();
/*     */ 
/* 148 */       getNodesByID(xctxt, docContext, refval, null, nodeSet, false);
/*     */     }
/*     */ 
/* 151 */     return nodes;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.functions.FuncId
 * JD-Core Version:    0.6.2
 */