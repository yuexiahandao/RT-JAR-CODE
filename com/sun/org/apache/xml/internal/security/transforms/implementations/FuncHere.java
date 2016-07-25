/*     */ package com.sun.org.apache.xml.internal.security.transforms.implementations;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.dtm.DTM;
/*     */ import com.sun.org.apache.xml.internal.security.utils.I18n;
/*     */ import com.sun.org.apache.xml.internal.security.utils.XMLUtils;
/*     */ import com.sun.org.apache.xpath.internal.NodeSetDTM;
/*     */ import com.sun.org.apache.xpath.internal.XPathContext;
/*     */ import com.sun.org.apache.xpath.internal.functions.Function;
/*     */ import com.sun.org.apache.xpath.internal.objects.XNodeSet;
/*     */ import com.sun.org.apache.xpath.internal.objects.XObject;
/*     */ import java.util.Vector;
/*     */ import javax.xml.transform.TransformerException;
/*     */ import org.w3c.dom.Node;
/*     */ 
/*     */ public class FuncHere extends Function
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */ 
/*     */   public XObject execute(XPathContext paramXPathContext)
/*     */     throws TransformerException
/*     */   {
/*  78 */     Node localNode = (Node)paramXPathContext.getOwnerObject();
/*     */ 
/*  80 */     if (localNode == null) {
/*  81 */       return null;
/*     */     }
/*     */ 
/*  84 */     int i = paramXPathContext.getDTMHandleFromNode(localNode);
/*     */ 
/*  86 */     int j = paramXPathContext.getCurrentNode();
/*  87 */     DTM localDTM = paramXPathContext.getDTM(j);
/*  88 */     int k = localDTM.getDocument();
/*     */ 
/*  90 */     if (-1 == k) {
/*  91 */       error(paramXPathContext, "ER_CONTEXT_HAS_NO_OWNERDOC", null);
/*     */     }
/*     */ 
/*  98 */     Object localObject1 = XMLUtils.getOwnerDocument(localDTM.getNode(j));
/*     */ 
/* 100 */     Object localObject2 = XMLUtils.getOwnerDocument(localNode);
/*     */ 
/* 102 */     if (localObject1 != localObject2) {
/* 103 */       throw new TransformerException(I18n.translate("xpath.funcHere.documentsDiffer"));
/*     */     }
/*     */ 
/* 108 */     localObject1 = new XNodeSet(paramXPathContext.getDTMManager());
/* 109 */     localObject2 = ((XNodeSet)localObject1).mutableNodeset();
/*     */ 
/* 112 */     int m = -1;
/*     */ 
/* 114 */     switch (localDTM.getNodeType(i))
/*     */     {
/*     */     case 2:
/* 118 */       m = i;
/*     */ 
/* 120 */       ((NodeSetDTM)localObject2).addNode(m);
/*     */ 
/* 122 */       break;
/*     */     case 7:
/* 126 */       m = i;
/*     */ 
/* 128 */       ((NodeSetDTM)localObject2).addNode(m);
/*     */ 
/* 130 */       break;
/*     */     case 3:
/* 135 */       m = localDTM.getParent(i);
/*     */ 
/* 137 */       ((NodeSetDTM)localObject2).addNode(m);
/*     */ 
/* 139 */       break;
/*     */     }
/*     */ 
/* 147 */     ((NodeSetDTM)localObject2).detach();
/*     */ 
/* 149 */     return localObject1;
/*     */   }
/*     */ 
/*     */   public void fixupVariables(Vector paramVector, int paramInt)
/*     */   {
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.transforms.implementations.FuncHere
 * JD-Core Version:    0.6.2
 */