/*     */ package com.sun.org.apache.xalan.internal.lib;
/*     */ 
/*     */ import com.sun.org.apache.xalan.internal.extensions.ExpressionContext;
/*     */ import com.sun.org.apache.xml.internal.dtm.DTM;
/*     */ import com.sun.org.apache.xml.internal.dtm.ref.DTMNodeProxy;
/*     */ import javax.xml.transform.SourceLocator;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ 
/*     */ public class NodeInfo
/*     */ {
/*     */   public static String systemId(ExpressionContext context)
/*     */   {
/*  52 */     Node contextNode = context.getContextNode();
/*  53 */     int nodeHandler = ((DTMNodeProxy)contextNode).getDTMNodeNumber();
/*  54 */     SourceLocator locator = ((DTMNodeProxy)contextNode).getDTM().getSourceLocatorFor(nodeHandler);
/*     */ 
/*  57 */     if (locator != null) {
/*  58 */       return locator.getSystemId();
/*     */     }
/*  60 */     return null;
/*     */   }
/*     */ 
/*     */   public static String systemId(NodeList nodeList)
/*     */   {
/*  73 */     if ((nodeList == null) || (nodeList.getLength() == 0)) {
/*  74 */       return null;
/*     */     }
/*  76 */     Node node = nodeList.item(0);
/*  77 */     int nodeHandler = ((DTMNodeProxy)node).getDTMNodeNumber();
/*  78 */     SourceLocator locator = ((DTMNodeProxy)node).getDTM().getSourceLocatorFor(nodeHandler);
/*     */ 
/*  81 */     if (locator != null) {
/*  82 */       return locator.getSystemId();
/*     */     }
/*  84 */     return null;
/*     */   }
/*     */ 
/*     */   public static String publicId(ExpressionContext context)
/*     */   {
/*  98 */     Node contextNode = context.getContextNode();
/*  99 */     int nodeHandler = ((DTMNodeProxy)contextNode).getDTMNodeNumber();
/* 100 */     SourceLocator locator = ((DTMNodeProxy)contextNode).getDTM().getSourceLocatorFor(nodeHandler);
/*     */ 
/* 103 */     if (locator != null) {
/* 104 */       return locator.getPublicId();
/*     */     }
/* 106 */     return null;
/*     */   }
/*     */ 
/*     */   public static String publicId(NodeList nodeList)
/*     */   {
/* 121 */     if ((nodeList == null) || (nodeList.getLength() == 0)) {
/* 122 */       return null;
/*     */     }
/* 124 */     Node node = nodeList.item(0);
/* 125 */     int nodeHandler = ((DTMNodeProxy)node).getDTMNodeNumber();
/* 126 */     SourceLocator locator = ((DTMNodeProxy)node).getDTM().getSourceLocatorFor(nodeHandler);
/*     */ 
/* 129 */     if (locator != null) {
/* 130 */       return locator.getPublicId();
/*     */     }
/* 132 */     return null;
/*     */   }
/*     */ 
/*     */   public static int lineNumber(ExpressionContext context)
/*     */   {
/* 151 */     Node contextNode = context.getContextNode();
/* 152 */     int nodeHandler = ((DTMNodeProxy)contextNode).getDTMNodeNumber();
/* 153 */     SourceLocator locator = ((DTMNodeProxy)contextNode).getDTM().getSourceLocatorFor(nodeHandler);
/*     */ 
/* 156 */     if (locator != null) {
/* 157 */       return locator.getLineNumber();
/*     */     }
/* 159 */     return -1;
/*     */   }
/*     */ 
/*     */   public static int lineNumber(NodeList nodeList)
/*     */   {
/* 179 */     if ((nodeList == null) || (nodeList.getLength() == 0)) {
/* 180 */       return -1;
/*     */     }
/* 182 */     Node node = nodeList.item(0);
/* 183 */     int nodeHandler = ((DTMNodeProxy)node).getDTMNodeNumber();
/* 184 */     SourceLocator locator = ((DTMNodeProxy)node).getDTM().getSourceLocatorFor(nodeHandler);
/*     */ 
/* 187 */     if (locator != null) {
/* 188 */       return locator.getLineNumber();
/*     */     }
/* 190 */     return -1;
/*     */   }
/*     */ 
/*     */   public static int columnNumber(ExpressionContext context)
/*     */   {
/* 209 */     Node contextNode = context.getContextNode();
/* 210 */     int nodeHandler = ((DTMNodeProxy)contextNode).getDTMNodeNumber();
/* 211 */     SourceLocator locator = ((DTMNodeProxy)contextNode).getDTM().getSourceLocatorFor(nodeHandler);
/*     */ 
/* 214 */     if (locator != null) {
/* 215 */       return locator.getColumnNumber();
/*     */     }
/* 217 */     return -1;
/*     */   }
/*     */ 
/*     */   public static int columnNumber(NodeList nodeList)
/*     */   {
/* 237 */     if ((nodeList == null) || (nodeList.getLength() == 0)) {
/* 238 */       return -1;
/*     */     }
/* 240 */     Node node = nodeList.item(0);
/* 241 */     int nodeHandler = ((DTMNodeProxy)node).getDTMNodeNumber();
/* 242 */     SourceLocator locator = ((DTMNodeProxy)node).getDTM().getSourceLocatorFor(nodeHandler);
/*     */ 
/* 245 */     if (locator != null) {
/* 246 */       return locator.getColumnNumber();
/*     */     }
/* 248 */     return -1;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.lib.NodeInfo
 * JD-Core Version:    0.6.2
 */