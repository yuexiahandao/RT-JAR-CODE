/*     */ package org.jcp.xml.dsig.internal.dom;
/*     */ 
/*     */ import java.security.spec.AlgorithmParameterSpec;
/*     */ import java.util.AbstractSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Set;
/*     */ import javax.xml.crypto.XMLCryptoContext;
/*     */ import javax.xml.crypto.XMLStructure;
/*     */ import javax.xml.crypto.dom.DOMStructure;
/*     */ import javax.xml.crypto.dsig.spec.ExcC14NParameterSpec;
/*     */ import javax.xml.crypto.dsig.spec.XPathFilter2ParameterSpec;
/*     */ import javax.xml.crypto.dsig.spec.XPathFilterParameterSpec;
/*     */ import javax.xml.crypto.dsig.spec.XPathType;
/*     */ import javax.xml.crypto.dsig.spec.XSLTTransformParameterSpec;
/*     */ import org.w3c.dom.Attr;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ 
/*     */ public class DOMUtils
/*     */ {
/*     */   public static Document getOwnerDocument(Node paramNode)
/*     */   {
/*  58 */     if (paramNode.getNodeType() == 9) {
/*  59 */       return (Document)paramNode;
/*     */     }
/*  61 */     return paramNode.getOwnerDocument();
/*     */   }
/*     */ 
/*     */   public static Element createElement(Document paramDocument, String paramString1, String paramString2, String paramString3)
/*     */   {
/*  77 */     String str = paramString3 + ":" + paramString1;
/*     */ 
/*  79 */     return paramDocument.createElementNS(paramString2, str);
/*     */   }
/*     */ 
/*     */   public static void setAttribute(Element paramElement, String paramString1, String paramString2)
/*     */   {
/*  91 */     if (paramString2 == null) return;
/*  92 */     paramElement.setAttributeNS(null, paramString1, paramString2);
/*     */   }
/*     */ 
/*     */   public static void setAttributeID(Element paramElement, String paramString1, String paramString2)
/*     */   {
/* 106 */     if (paramString2 == null) return;
/* 107 */     paramElement.setAttributeNS(null, paramString1, paramString2);
/* 108 */     paramElement.setIdAttributeNS(null, paramString1, true);
/*     */   }
/*     */ 
/*     */   public static Element getFirstChildElement(Node paramNode)
/*     */   {
/* 121 */     Node localNode = paramNode.getFirstChild();
/* 122 */     while ((localNode != null) && (localNode.getNodeType() != 1)) {
/* 123 */       localNode = localNode.getNextSibling();
/*     */     }
/* 125 */     return (Element)localNode;
/*     */   }
/*     */ 
/*     */   public static Element getLastChildElement(Node paramNode)
/*     */   {
/* 138 */     Node localNode = paramNode.getLastChild();
/* 139 */     while ((localNode != null) && (localNode.getNodeType() != 1)) {
/* 140 */       localNode = localNode.getPreviousSibling();
/*     */     }
/* 142 */     return (Element)localNode;
/*     */   }
/*     */ 
/*     */   public static Element getNextSiblingElement(Node paramNode)
/*     */   {
/* 155 */     Node localNode = paramNode.getNextSibling();
/* 156 */     while ((localNode != null) && (localNode.getNodeType() != 1)) {
/* 157 */       localNode = localNode.getNextSibling();
/*     */     }
/* 159 */     return (Element)localNode;
/*     */   }
/*     */ 
/*     */   public static String getAttributeValue(Element paramElement, String paramString)
/*     */   {
/* 177 */     Attr localAttr = paramElement.getAttributeNodeNS(null, paramString);
/* 178 */     return localAttr == null ? null : localAttr.getValue();
/*     */   }
/*     */ 
/*     */   public static Set nodeSet(NodeList paramNodeList)
/*     */   {
/* 189 */     return new NodeSet(paramNodeList);
/*     */   }
/*     */ 
/*     */   public static String getNSPrefix(XMLCryptoContext paramXMLCryptoContext, String paramString)
/*     */   {
/* 228 */     if (paramXMLCryptoContext != null) {
/* 229 */       return paramXMLCryptoContext.getNamespacePrefix(paramString, paramXMLCryptoContext.getDefaultNamespacePrefix());
/*     */     }
/*     */ 
/* 232 */     return null;
/*     */   }
/*     */ 
/*     */   public static String getSignaturePrefix(XMLCryptoContext paramXMLCryptoContext)
/*     */   {
/* 244 */     return getNSPrefix(paramXMLCryptoContext, "http://www.w3.org/2000/09/xmldsig#");
/*     */   }
/*     */ 
/*     */   public static void removeAllChildren(Node paramNode)
/*     */   {
/* 253 */     NodeList localNodeList = paramNode.getChildNodes();
/* 254 */     int i = 0; for (int j = localNodeList.getLength(); i < j; i++)
/* 255 */       paramNode.removeChild(localNodeList.item(i));
/*     */   }
/*     */ 
/*     */   public static boolean nodesEqual(Node paramNode1, Node paramNode2)
/*     */   {
/* 263 */     if (paramNode1 == paramNode2) {
/* 264 */       return true;
/*     */     }
/* 266 */     if (paramNode1.getNodeType() != paramNode2.getNodeType()) {
/* 267 */       return false;
/*     */     }
/*     */ 
/* 270 */     return true;
/*     */   }
/*     */ 
/*     */   public static void appendChild(Node paramNode1, Node paramNode2)
/*     */   {
/* 279 */     Document localDocument = getOwnerDocument(paramNode1);
/* 280 */     if (paramNode2.getOwnerDocument() != localDocument)
/* 281 */       paramNode1.appendChild(localDocument.importNode(paramNode2, true));
/*     */     else
/* 283 */       paramNode1.appendChild(paramNode2);
/*     */   }
/*     */ 
/*     */   public static boolean paramsEqual(AlgorithmParameterSpec paramAlgorithmParameterSpec1, AlgorithmParameterSpec paramAlgorithmParameterSpec2)
/*     */   {
/* 289 */     if (paramAlgorithmParameterSpec1 == paramAlgorithmParameterSpec2) {
/* 290 */       return true;
/*     */     }
/* 292 */     if (((paramAlgorithmParameterSpec1 instanceof XPathFilter2ParameterSpec)) && ((paramAlgorithmParameterSpec2 instanceof XPathFilter2ParameterSpec)))
/*     */     {
/* 294 */       return paramsEqual((XPathFilter2ParameterSpec)paramAlgorithmParameterSpec1, (XPathFilter2ParameterSpec)paramAlgorithmParameterSpec2);
/*     */     }
/*     */ 
/* 297 */     if (((paramAlgorithmParameterSpec1 instanceof ExcC14NParameterSpec)) && ((paramAlgorithmParameterSpec2 instanceof ExcC14NParameterSpec)))
/*     */     {
/* 299 */       return paramsEqual((ExcC14NParameterSpec)paramAlgorithmParameterSpec1, (ExcC14NParameterSpec)paramAlgorithmParameterSpec2);
/*     */     }
/*     */ 
/* 302 */     if (((paramAlgorithmParameterSpec1 instanceof XPathFilterParameterSpec)) && ((paramAlgorithmParameterSpec2 instanceof XPathFilterParameterSpec)))
/*     */     {
/* 304 */       return paramsEqual((XPathFilterParameterSpec)paramAlgorithmParameterSpec1, (XPathFilterParameterSpec)paramAlgorithmParameterSpec2);
/*     */     }
/*     */ 
/* 307 */     if (((paramAlgorithmParameterSpec1 instanceof XSLTTransformParameterSpec)) && ((paramAlgorithmParameterSpec2 instanceof XSLTTransformParameterSpec)))
/*     */     {
/* 309 */       return paramsEqual((XSLTTransformParameterSpec)paramAlgorithmParameterSpec1, (XSLTTransformParameterSpec)paramAlgorithmParameterSpec2);
/*     */     }
/*     */ 
/* 312 */     return false;
/*     */   }
/*     */ 
/*     */   private static boolean paramsEqual(XPathFilter2ParameterSpec paramXPathFilter2ParameterSpec1, XPathFilter2ParameterSpec paramXPathFilter2ParameterSpec2)
/*     */   {
/* 318 */     List localList1 = paramXPathFilter2ParameterSpec1.getXPathList();
/* 319 */     List localList2 = paramXPathFilter2ParameterSpec2.getXPathList();
/* 320 */     int i = localList1.size();
/* 321 */     if (i != localList2.size()) {
/* 322 */       return false;
/*     */     }
/* 324 */     for (int j = 0; j < i; j++) {
/* 325 */       XPathType localXPathType1 = (XPathType)localList1.get(j);
/* 326 */       XPathType localXPathType2 = (XPathType)localList2.get(j);
/* 327 */       if ((!localXPathType1.getExpression().equals(localXPathType2.getExpression())) || (!localXPathType1.getNamespaceMap().equals(localXPathType2.getNamespaceMap())) || (localXPathType1.getFilter() != localXPathType2.getFilter()))
/*     */       {
/* 330 */         return false;
/*     */       }
/*     */     }
/* 333 */     return true;
/*     */   }
/*     */ 
/*     */   private static boolean paramsEqual(ExcC14NParameterSpec paramExcC14NParameterSpec1, ExcC14NParameterSpec paramExcC14NParameterSpec2)
/*     */   {
/* 338 */     return paramExcC14NParameterSpec1.getPrefixList().equals(paramExcC14NParameterSpec2.getPrefixList());
/*     */   }
/*     */ 
/*     */   private static boolean paramsEqual(XPathFilterParameterSpec paramXPathFilterParameterSpec1, XPathFilterParameterSpec paramXPathFilterParameterSpec2)
/*     */   {
/* 343 */     return (paramXPathFilterParameterSpec1.getXPath().equals(paramXPathFilterParameterSpec2.getXPath())) && (paramXPathFilterParameterSpec1.getNamespaceMap().equals(paramXPathFilterParameterSpec2.getNamespaceMap()));
/*     */   }
/*     */ 
/*     */   private static boolean paramsEqual(XSLTTransformParameterSpec paramXSLTTransformParameterSpec1, XSLTTransformParameterSpec paramXSLTTransformParameterSpec2)
/*     */   {
/* 350 */     XMLStructure localXMLStructure1 = paramXSLTTransformParameterSpec2.getStylesheet();
/* 351 */     if (!(localXMLStructure1 instanceof DOMStructure)) {
/* 352 */       return false;
/*     */     }
/* 354 */     Node localNode1 = ((DOMStructure)localXMLStructure1).getNode();
/*     */ 
/* 356 */     XMLStructure localXMLStructure2 = paramXSLTTransformParameterSpec1.getStylesheet();
/* 357 */     Node localNode2 = ((DOMStructure)localXMLStructure2).getNode();
/*     */ 
/* 359 */     return nodesEqual(localNode2, localNode1);
/*     */   }
/*     */ 
/*     */   static class NodeSet extends AbstractSet
/*     */   {
/*     */     private NodeList nl;
/*     */ 
/*     */     public NodeSet(NodeList paramNodeList)
/*     */     {
/* 195 */       this.nl = paramNodeList;
/*     */     }
/*     */     public int size() {
/* 198 */       return this.nl.getLength();
/*     */     }
/* 200 */     public Iterator iterator() { return new Iterator() {
/* 201 */         int index = 0;
/*     */ 
/*     */         public void remove() {
/* 204 */           throw new UnsupportedOperationException();
/*     */         }
/*     */         public Object next() {
/* 207 */           if (!hasNext()) {
/* 208 */             throw new NoSuchElementException();
/*     */           }
/* 210 */           return DOMUtils.NodeSet.this.nl.item(this.index++);
/*     */         }
/*     */         public boolean hasNext() {
/* 213 */           return this.index < DOMUtils.NodeSet.this.nl.getLength();
/*     */         }
/*     */       };
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.jcp.xml.dsig.internal.dom.DOMUtils
 * JD-Core Version:    0.6.2
 */