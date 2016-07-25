/*     */ package com.sun.org.apache.xml.internal.security.transforms.params;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
/*     */ import com.sun.org.apache.xml.internal.security.transforms.TransformParam;
/*     */ import com.sun.org.apache.xml.internal.security.utils.ElementProxy;
/*     */ import com.sun.org.apache.xml.internal.security.utils.XMLUtils;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ 
/*     */ public class XPath2FilterContainer04 extends ElementProxy
/*     */   implements TransformParam
/*     */ {
/*     */   private static final String _ATT_FILTER = "Filter";
/*     */   private static final String _ATT_FILTER_VALUE_INTERSECT = "intersect";
/*     */   private static final String _ATT_FILTER_VALUE_SUBTRACT = "subtract";
/*     */   private static final String _ATT_FILTER_VALUE_UNION = "union";
/*     */   public static final String _TAG_XPATH2 = "XPath";
/*     */   public static final String XPathFilter2NS = "http://www.w3.org/2002/04/xmldsig-filter2";
/*     */ 
/*     */   private XPath2FilterContainer04()
/*     */   {
/*     */   }
/*     */ 
/*     */   private XPath2FilterContainer04(Document paramDocument, String paramString1, String paramString2)
/*     */   {
/*  84 */     super(paramDocument);
/*     */ 
/*  86 */     this._constructionElement.setAttributeNS(null, "Filter", paramString2);
/*     */ 
/*  89 */     if ((paramString1.length() > 2) && (!Character.isWhitespace(paramString1.charAt(0))))
/*     */     {
/*  91 */       XMLUtils.addReturnToElement(this._constructionElement);
/*  92 */       this._constructionElement.appendChild(paramDocument.createTextNode(paramString1));
/*  93 */       XMLUtils.addReturnToElement(this._constructionElement);
/*     */     } else {
/*  95 */       this._constructionElement.appendChild(paramDocument.createTextNode(paramString1));
/*     */     }
/*     */   }
/*     */ 
/*     */   private XPath2FilterContainer04(Element paramElement, String paramString)
/*     */     throws XMLSecurityException
/*     */   {
/* 110 */     super(paramElement, paramString);
/*     */ 
/* 112 */     String str = this._constructionElement.getAttributeNS(null, "Filter");
/*     */ 
/* 116 */     if ((!str.equals("intersect")) && (!str.equals("subtract")) && (!str.equals("union")))
/*     */     {
/* 122 */       Object[] arrayOfObject = { "Filter", str, "intersect, subtract or union" };
/*     */ 
/* 129 */       throw new XMLSecurityException("attributeValueIllegal", arrayOfObject);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static XPath2FilterContainer04 newInstanceIntersect(Document paramDocument, String paramString)
/*     */   {
/* 143 */     return new XPath2FilterContainer04(paramDocument, paramString, "intersect");
/*     */   }
/*     */ 
/*     */   public static XPath2FilterContainer04 newInstanceSubtract(Document paramDocument, String paramString)
/*     */   {
/* 158 */     return new XPath2FilterContainer04(paramDocument, paramString, "subtract");
/*     */   }
/*     */ 
/*     */   public static XPath2FilterContainer04 newInstanceUnion(Document paramDocument, String paramString)
/*     */   {
/* 173 */     return new XPath2FilterContainer04(paramDocument, paramString, "union");
/*     */   }
/*     */ 
/*     */   public static XPath2FilterContainer04 newInstance(Element paramElement, String paramString)
/*     */     throws XMLSecurityException
/*     */   {
/* 189 */     return new XPath2FilterContainer04(paramElement, paramString);
/*     */   }
/*     */ 
/*     */   public boolean isIntersect()
/*     */   {
/* 199 */     return this._constructionElement.getAttributeNS(null, "Filter").equals("intersect");
/*     */   }
/*     */ 
/*     */   public boolean isSubtract()
/*     */   {
/* 211 */     return this._constructionElement.getAttributeNS(null, "Filter").equals("subtract");
/*     */   }
/*     */ 
/*     */   public boolean isUnion()
/*     */   {
/* 223 */     return this._constructionElement.getAttributeNS(null, "Filter").equals("union");
/*     */   }
/*     */ 
/*     */   public String getXPathFilterStr()
/*     */   {
/* 234 */     return getTextFromTextChild();
/*     */   }
/*     */ 
/*     */   public Node getXPathFilterTextNode()
/*     */   {
/* 246 */     NodeList localNodeList = this._constructionElement.getChildNodes();
/* 247 */     int i = localNodeList.getLength();
/*     */ 
/* 249 */     for (int j = 0; j < i; j++) {
/* 250 */       if (localNodeList.item(j).getNodeType() == 3) {
/* 251 */         return localNodeList.item(j);
/*     */       }
/*     */     }
/*     */ 
/* 255 */     return null;
/*     */   }
/*     */ 
/*     */   public final String getBaseLocalName()
/*     */   {
/* 260 */     return "XPath";
/*     */   }
/*     */ 
/*     */   public final String getBaseNamespace()
/*     */   {
/* 265 */     return "http://www.w3.org/2002/04/xmldsig-filter2";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.transforms.params.XPath2FilterContainer04
 * JD-Core Version:    0.6.2
 */