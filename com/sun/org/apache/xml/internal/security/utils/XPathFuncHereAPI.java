/*     */ package com.sun.org.apache.xml.internal.security.utils;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.security.transforms.implementations.FuncHereContext;
/*     */ import com.sun.org.apache.xml.internal.utils.PrefixResolver;
/*     */ import com.sun.org.apache.xml.internal.utils.PrefixResolverDefault;
/*     */ import com.sun.org.apache.xpath.internal.XPath;
/*     */ import com.sun.org.apache.xpath.internal.objects.XObject;
/*     */ import javax.xml.transform.TransformerException;
/*     */ import org.w3c.dom.Attr;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ import org.w3c.dom.ProcessingInstruction;
/*     */ import org.w3c.dom.Text;
/*     */ import org.w3c.dom.traversal.NodeIterator;
/*     */ 
/*     */ public class XPathFuncHereAPI
/*     */ {
/*     */   public static Node selectSingleNode(Node paramNode1, Node paramNode2)
/*     */     throws TransformerException
/*     */   {
/*  73 */     return selectSingleNode(paramNode1, paramNode2, paramNode1);
/*     */   }
/*     */ 
/*     */   public static Node selectSingleNode(Node paramNode1, Node paramNode2, Node paramNode3)
/*     */     throws TransformerException
/*     */   {
/*  92 */     NodeIterator localNodeIterator = selectNodeIterator(paramNode1, paramNode2, paramNode3);
/*     */ 
/*  96 */     return localNodeIterator.nextNode();
/*     */   }
/*     */ 
/*     */   public static NodeIterator selectNodeIterator(Node paramNode1, Node paramNode2)
/*     */     throws TransformerException
/*     */   {
/* 111 */     return selectNodeIterator(paramNode1, paramNode2, paramNode1);
/*     */   }
/*     */ 
/*     */   public static NodeIterator selectNodeIterator(Node paramNode1, Node paramNode2, Node paramNode3)
/*     */     throws TransformerException
/*     */   {
/* 130 */     XObject localXObject = eval(paramNode1, paramNode2, paramNode3);
/*     */ 
/* 133 */     return localXObject.nodeset();
/*     */   }
/*     */ 
/*     */   public static NodeList selectNodeList(Node paramNode1, Node paramNode2)
/*     */     throws TransformerException
/*     */   {
/* 148 */     return selectNodeList(paramNode1, paramNode2, paramNode1);
/*     */   }
/*     */ 
/*     */   public static NodeList selectNodeList(Node paramNode1, Node paramNode2, Node paramNode3)
/*     */     throws TransformerException
/*     */   {
/* 167 */     XObject localXObject = eval(paramNode1, paramNode2, paramNode3);
/*     */ 
/* 170 */     return localXObject.nodelist();
/*     */   }
/*     */ 
/*     */   public static XObject eval(Node paramNode1, Node paramNode2)
/*     */     throws TransformerException
/*     */   {
/* 190 */     return eval(paramNode1, paramNode2, paramNode1);
/*     */   }
/*     */ 
/*     */   public static XObject eval(Node paramNode1, Node paramNode2, Node paramNode3)
/*     */     throws TransformerException
/*     */   {
/* 222 */     FuncHereContext localFuncHereContext = new FuncHereContext(paramNode2);
/*     */ 
/* 228 */     PrefixResolverDefault localPrefixResolverDefault = new PrefixResolverDefault(paramNode3.getNodeType() == 9 ? ((Document)paramNode3).getDocumentElement() : paramNode3);
/*     */ 
/* 234 */     String str = getStrFromNode(paramNode2);
/*     */ 
/* 237 */     XPath localXPath = new XPath(str, null, localPrefixResolverDefault, 0, null);
/*     */ 
/* 241 */     int i = localFuncHereContext.getDTMHandleFromNode(paramNode1);
/*     */ 
/* 243 */     return localXPath.execute(localFuncHereContext, i, localPrefixResolverDefault);
/*     */   }
/*     */ 
/*     */   public static XObject eval(Node paramNode1, Node paramNode2, PrefixResolver paramPrefixResolver)
/*     */     throws TransformerException
/*     */   {
/* 271 */     String str = getStrFromNode(paramNode2);
/*     */ 
/* 279 */     XPath localXPath = new XPath(str, null, paramPrefixResolver, 0, null);
/*     */ 
/* 282 */     FuncHereContext localFuncHereContext = new FuncHereContext(paramNode2);
/* 283 */     int i = localFuncHereContext.getDTMHandleFromNode(paramNode1);
/*     */ 
/* 285 */     return localXPath.execute(localFuncHereContext, i, paramPrefixResolver);
/*     */   }
/*     */ 
/*     */   private static String getStrFromNode(Node paramNode)
/*     */   {
/* 296 */     if (paramNode.getNodeType() == 3)
/* 297 */       return ((Text)paramNode).getData();
/* 298 */     if (paramNode.getNodeType() == 2)
/* 299 */       return ((Attr)paramNode).getNodeValue();
/* 300 */     if (paramNode.getNodeType() == 7) {
/* 301 */       return ((ProcessingInstruction)paramNode).getNodeValue();
/*     */     }
/*     */ 
/* 304 */     return "";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.utils.XPathFuncHereAPI
 * JD-Core Version:    0.6.2
 */