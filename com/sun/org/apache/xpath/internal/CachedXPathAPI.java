/*     */ package com.sun.org.apache.xpath.internal;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.utils.PrefixResolver;
/*     */ import com.sun.org.apache.xml.internal.utils.PrefixResolverDefault;
/*     */ import com.sun.org.apache.xpath.internal.objects.XObject;
/*     */ import javax.xml.transform.TransformerException;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ import org.w3c.dom.traversal.NodeIterator;
/*     */ 
/*     */ public class CachedXPathAPI
/*     */ {
/*     */   protected XPathContext xpathSupport;
/*     */ 
/*     */   public CachedXPathAPI()
/*     */   {
/*  77 */     this.xpathSupport = new XPathContext();
/*     */   }
/*     */ 
/*     */   public CachedXPathAPI(CachedXPathAPI priorXPathAPI)
/*     */   {
/*  95 */     this.xpathSupport = priorXPathAPI.xpathSupport;
/*     */   }
/*     */ 
/*     */   public XPathContext getXPathContext()
/*     */   {
/* 106 */     return this.xpathSupport;
/*     */   }
/*     */ 
/*     */   public Node selectSingleNode(Node contextNode, String str)
/*     */     throws TransformerException
/*     */   {
/* 124 */     return selectSingleNode(contextNode, str, contextNode);
/*     */   }
/*     */ 
/*     */   public Node selectSingleNode(Node contextNode, String str, Node namespaceNode)
/*     */     throws TransformerException
/*     */   {
/* 144 */     NodeIterator nl = selectNodeIterator(contextNode, str, namespaceNode);
/*     */ 
/* 147 */     return nl.nextNode();
/*     */   }
/*     */ 
/*     */   public NodeIterator selectNodeIterator(Node contextNode, String str)
/*     */     throws TransformerException
/*     */   {
/* 163 */     return selectNodeIterator(contextNode, str, contextNode);
/*     */   }
/*     */ 
/*     */   public NodeIterator selectNodeIterator(Node contextNode, String str, Node namespaceNode)
/*     */     throws TransformerException
/*     */   {
/* 183 */     XObject list = eval(contextNode, str, namespaceNode);
/*     */ 
/* 186 */     return list.nodeset();
/*     */   }
/*     */ 
/*     */   public NodeList selectNodeList(Node contextNode, String str)
/*     */     throws TransformerException
/*     */   {
/* 202 */     return selectNodeList(contextNode, str, contextNode);
/*     */   }
/*     */ 
/*     */   public NodeList selectNodeList(Node contextNode, String str, Node namespaceNode)
/*     */     throws TransformerException
/*     */   {
/* 222 */     XObject list = eval(contextNode, str, namespaceNode);
/*     */ 
/* 225 */     return list.nodelist();
/*     */   }
/*     */ 
/*     */   public XObject eval(Node contextNode, String str)
/*     */     throws TransformerException
/*     */   {
/* 246 */     return eval(contextNode, str, contextNode);
/*     */   }
/*     */ 
/*     */   public XObject eval(Node contextNode, String str, Node namespaceNode)
/*     */     throws TransformerException
/*     */   {
/* 283 */     PrefixResolverDefault prefixResolver = new PrefixResolverDefault(namespaceNode.getNodeType() == 9 ? ((Document)namespaceNode).getDocumentElement() : namespaceNode);
/*     */ 
/* 288 */     XPath xpath = new XPath(str, null, prefixResolver, 0, null);
/*     */ 
/* 292 */     int ctxtNode = this.xpathSupport.getDTMHandleFromNode(contextNode);
/*     */ 
/* 294 */     return xpath.execute(this.xpathSupport, ctxtNode, prefixResolver);
/*     */   }
/*     */ 
/*     */   public XObject eval(Node contextNode, String str, PrefixResolver prefixResolver)
/*     */     throws TransformerException
/*     */   {
/* 329 */     XPath xpath = new XPath(str, null, prefixResolver, 0, null);
/*     */ 
/* 332 */     XPathContext xpathSupport = new XPathContext();
/* 333 */     int ctxtNode = xpathSupport.getDTMHandleFromNode(contextNode);
/*     */ 
/* 335 */     return xpath.execute(xpathSupport, ctxtNode, prefixResolver);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.CachedXPathAPI
 * JD-Core Version:    0.6.2
 */