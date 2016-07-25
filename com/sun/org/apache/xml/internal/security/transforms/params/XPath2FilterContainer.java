/*     */ package com.sun.org.apache.xml.internal.security.transforms.params;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
/*     */ import com.sun.org.apache.xml.internal.security.transforms.TransformParam;
/*     */ import com.sun.org.apache.xml.internal.security.utils.ElementProxy;
/*     */ import com.sun.org.apache.xml.internal.security.utils.HelperNodeList;
/*     */ import com.sun.org.apache.xml.internal.security.utils.XMLUtils;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ 
/*     */ public class XPath2FilterContainer extends ElementProxy
/*     */   implements TransformParam
/*     */ {
/*     */   private static final String _ATT_FILTER = "Filter";
/*     */   private static final String _ATT_FILTER_VALUE_INTERSECT = "intersect";
/*     */   private static final String _ATT_FILTER_VALUE_SUBTRACT = "subtract";
/*     */   private static final String _ATT_FILTER_VALUE_UNION = "union";
/*     */   public static final String INTERSECT = "intersect";
/*     */   public static final String SUBTRACT = "subtract";
/*     */   public static final String UNION = "union";
/*     */   public static final String _TAG_XPATH2 = "XPath";
/*     */   public static final String XPathFilter2NS = "http://www.w3.org/2002/06/xmldsig-filter2";
/*     */ 
/*     */   private XPath2FilterContainer()
/*     */   {
/*     */   }
/*     */ 
/*     */   private XPath2FilterContainer(Document paramDocument, String paramString1, String paramString2)
/*     */   {
/*  97 */     super(paramDocument);
/*     */ 
/*  99 */     this._constructionElement.setAttributeNS(null, "Filter", paramString2);
/*     */ 
/* 101 */     this._constructionElement.appendChild(paramDocument.createTextNode(paramString1));
/*     */   }
/*     */ 
/*     */   private XPath2FilterContainer(Element paramElement, String paramString)
/*     */     throws XMLSecurityException
/*     */   {
/* 114 */     super(paramElement, paramString);
/*     */ 
/* 116 */     String str = this._constructionElement.getAttributeNS(null, "Filter");
/*     */ 
/* 119 */     if ((!str.equals("intersect")) && (!str.equals("subtract")) && (!str.equals("union")))
/*     */     {
/* 125 */       Object[] arrayOfObject = { "Filter", str, "intersect, subtract or union" };
/*     */ 
/* 132 */       throw new XMLSecurityException("attributeValueIllegal", arrayOfObject);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static XPath2FilterContainer newInstanceIntersect(Document paramDocument, String paramString)
/*     */   {
/* 146 */     return new XPath2FilterContainer(paramDocument, paramString, "intersect");
/*     */   }
/*     */ 
/*     */   public static XPath2FilterContainer newInstanceSubtract(Document paramDocument, String paramString)
/*     */   {
/* 161 */     return new XPath2FilterContainer(paramDocument, paramString, "subtract");
/*     */   }
/*     */ 
/*     */   public static XPath2FilterContainer newInstanceUnion(Document paramDocument, String paramString)
/*     */   {
/* 176 */     return new XPath2FilterContainer(paramDocument, paramString, "union");
/*     */   }
/*     */ 
/*     */   public static NodeList newInstances(Document paramDocument, String[][] paramArrayOfString)
/*     */   {
/* 190 */     HelperNodeList localHelperNodeList = new HelperNodeList();
/*     */ 
/* 192 */     XMLUtils.addReturnToElement(paramDocument, localHelperNodeList);
/*     */ 
/* 194 */     for (int i = 0; i < paramArrayOfString.length; i++) {
/* 195 */       String str1 = paramArrayOfString[i][0];
/* 196 */       String str2 = paramArrayOfString[i][1];
/*     */ 
/* 198 */       if ((!str1.equals("intersect")) && (!str1.equals("subtract")) && (!str1.equals("union")))
/*     */       {
/* 204 */         throw new IllegalArgumentException("The type(" + i + ")=\"" + str1 + "\" is illegal");
/*     */       }
/*     */ 
/* 208 */       XPath2FilterContainer localXPath2FilterContainer = new XPath2FilterContainer(paramDocument, str2, str1);
/*     */ 
/* 210 */       localHelperNodeList.appendChild(localXPath2FilterContainer.getElement());
/* 211 */       XMLUtils.addReturnToElement(paramDocument, localHelperNodeList);
/*     */     }
/*     */ 
/* 214 */     return localHelperNodeList;
/*     */   }
/*     */ 
/*     */   public static XPath2FilterContainer newInstance(Element paramElement, String paramString)
/*     */     throws XMLSecurityException
/*     */   {
/* 228 */     return new XPath2FilterContainer(paramElement, paramString);
/*     */   }
/*     */ 
/*     */   public boolean isIntersect()
/*     */   {
/* 238 */     return this._constructionElement.getAttributeNS(null, "Filter").equals("intersect");
/*     */   }
/*     */ 
/*     */   public boolean isSubtract()
/*     */   {
/* 250 */     return this._constructionElement.getAttributeNS(null, "Filter").equals("subtract");
/*     */   }
/*     */ 
/*     */   public boolean isUnion()
/*     */   {
/* 262 */     return this._constructionElement.getAttributeNS(null, "Filter").equals("union");
/*     */   }
/*     */ 
/*     */   public String getXPathFilterStr()
/*     */   {
/* 273 */     return getTextFromTextChild();
/*     */   }
/*     */ 
/*     */   public Node getXPathFilterTextNode()
/*     */   {
/* 286 */     NodeList localNodeList = this._constructionElement.getChildNodes();
/* 287 */     int i = localNodeList.getLength();
/*     */ 
/* 289 */     for (int j = 0; j < i; j++) {
/* 290 */       if (localNodeList.item(j).getNodeType() == 3) {
/* 291 */         return localNodeList.item(j);
/*     */       }
/*     */     }
/*     */ 
/* 295 */     return null;
/*     */   }
/*     */ 
/*     */   public final String getBaseLocalName()
/*     */   {
/* 304 */     return "XPath";
/*     */   }
/*     */ 
/*     */   public final String getBaseNamespace()
/*     */   {
/* 313 */     return "http://www.w3.org/2002/06/xmldsig-filter2";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.transforms.params.XPath2FilterContainer
 * JD-Core Version:    0.6.2
 */