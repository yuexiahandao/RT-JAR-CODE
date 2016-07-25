/*     */ package com.sun.org.apache.xml.internal.security.transforms.params;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
/*     */ import com.sun.org.apache.xml.internal.security.transforms.TransformParam;
/*     */ import com.sun.org.apache.xml.internal.security.utils.ElementProxy;
/*     */ import com.sun.org.apache.xml.internal.security.utils.XMLUtils;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ 
/*     */ public class XPathFilterCHGPContainer extends ElementProxy
/*     */   implements TransformParam
/*     */ {
/*     */   private static final String _TAG_INCLUDE_BUT_SEARCH = "IncludeButSearch";
/*     */   private static final String _TAG_EXCLUDE_BUT_SEARCH = "ExcludeButSearch";
/*     */   private static final String _TAG_EXCLUDE = "Exclude";
/*     */   public static final String _TAG_XPATHCHGP = "XPathAlternative";
/*     */   public static final String _ATT_INCLUDESLASH = "IncludeSlashPolicy";
/*     */   public static final boolean IncludeSlash = true;
/*     */   public static final boolean ExcludeSlash = false;
/*     */ 
/*     */   private XPathFilterCHGPContainer()
/*     */   {
/*     */   }
/*     */ 
/*     */   private XPathFilterCHGPContainer(Document paramDocument, boolean paramBoolean, String paramString1, String paramString2, String paramString3)
/*     */   {
/*  87 */     super(paramDocument);
/*     */ 
/*  89 */     if (paramBoolean) {
/*  90 */       this._constructionElement.setAttributeNS(null, "IncludeSlashPolicy", "true");
/*     */     }
/*     */     else
/*  93 */       this._constructionElement.setAttributeNS(null, "IncludeSlashPolicy", "false");
/*     */     Element localElement;
/*  97 */     if ((paramString1 != null) && (paramString1.trim().length() > 0))
/*     */     {
/*  99 */       localElement = ElementProxy.createElementForFamily(paramDocument, getBaseNamespace(), "IncludeButSearch");
/*     */ 
/* 104 */       localElement.appendChild(this._doc.createTextNode(indentXPathText(paramString1)));
/*     */ 
/* 107 */       XMLUtils.addReturnToElement(this._constructionElement);
/* 108 */       this._constructionElement.appendChild(localElement);
/*     */     }
/*     */ 
/* 111 */     if ((paramString2 != null) && (paramString2.trim().length() > 0))
/*     */     {
/* 113 */       localElement = ElementProxy.createElementForFamily(paramDocument, getBaseNamespace(), "ExcludeButSearch");
/*     */ 
/* 118 */       localElement.appendChild(this._doc.createTextNode(indentXPathText(paramString2)));
/*     */ 
/* 121 */       XMLUtils.addReturnToElement(this._constructionElement);
/* 122 */       this._constructionElement.appendChild(localElement);
/*     */     }
/*     */ 
/* 125 */     if ((paramString3 != null) && (paramString3.trim().length() > 0)) {
/* 126 */       localElement = ElementProxy.createElementForFamily(paramDocument, getBaseNamespace(), "Exclude");
/*     */ 
/* 130 */       localElement.appendChild(this._doc.createTextNode(indentXPathText(paramString3)));
/*     */ 
/* 132 */       XMLUtils.addReturnToElement(this._constructionElement);
/* 133 */       this._constructionElement.appendChild(localElement);
/*     */     }
/*     */ 
/* 136 */     XMLUtils.addReturnToElement(this._constructionElement);
/*     */   }
/*     */ 
/*     */   static String indentXPathText(String paramString)
/*     */   {
/* 147 */     if ((paramString.length() > 2) && (!Character.isWhitespace(paramString.charAt(0)))) {
/* 148 */       return "\n" + paramString + "\n";
/*     */     }
/* 150 */     return paramString;
/*     */   }
/*     */ 
/*     */   private XPathFilterCHGPContainer(Element paramElement, String paramString)
/*     */     throws XMLSecurityException
/*     */   {
/* 163 */     super(paramElement, paramString);
/*     */   }
/*     */ 
/*     */   public static XPathFilterCHGPContainer getInstance(Document paramDocument, boolean paramBoolean, String paramString1, String paramString2, String paramString3)
/*     */   {
/* 180 */     return new XPathFilterCHGPContainer(paramDocument, paramBoolean, paramString1, paramString2, paramString3);
/*     */   }
/*     */ 
/*     */   public static XPathFilterCHGPContainer getInstance(Element paramElement, String paramString)
/*     */     throws XMLSecurityException
/*     */   {
/* 196 */     return new XPathFilterCHGPContainer(paramElement, paramString);
/*     */   }
/*     */ 
/*     */   private String getXStr(String paramString)
/*     */   {
/* 207 */     if (length(getBaseNamespace(), paramString) != 1) {
/* 208 */       return "";
/*     */     }
/*     */ 
/* 211 */     Element localElement = XMLUtils.selectNode(this._constructionElement.getFirstChild(), getBaseNamespace(), paramString, 0);
/*     */ 
/* 214 */     return XMLUtils.getFullTextChildrenFromElement(localElement);
/*     */   }
/*     */ 
/*     */   public String getIncludeButSearch()
/*     */   {
/* 223 */     return getXStr("IncludeButSearch");
/*     */   }
/*     */ 
/*     */   public String getExcludeButSearch()
/*     */   {
/* 232 */     return getXStr("ExcludeButSearch");
/*     */   }
/*     */ 
/*     */   public String getExclude()
/*     */   {
/* 241 */     return getXStr("Exclude");
/*     */   }
/*     */ 
/*     */   public boolean getIncludeSlashPolicy()
/*     */   {
/* 251 */     return this._constructionElement.getAttributeNS(null, "IncludeSlashPolicy").equals("true");
/*     */   }
/*     */ 
/*     */   private Node getHereContextNode(String paramString)
/*     */   {
/* 267 */     if (length(getBaseNamespace(), paramString) != 1) {
/* 268 */       return null;
/*     */     }
/*     */ 
/* 271 */     return XMLUtils.selectNodeText(this._constructionElement.getFirstChild(), getBaseNamespace(), paramString, 0);
/*     */   }
/*     */ 
/*     */   public Node getHereContextNodeIncludeButSearch()
/*     */   {
/* 281 */     return getHereContextNode("IncludeButSearch");
/*     */   }
/*     */ 
/*     */   public Node getHereContextNodeExcludeButSearch()
/*     */   {
/* 291 */     return getHereContextNode("ExcludeButSearch");
/*     */   }
/*     */ 
/*     */   public Node getHereContextNodeExclude()
/*     */   {
/* 301 */     return getHereContextNode("Exclude");
/*     */   }
/*     */ 
/*     */   public final String getBaseLocalName()
/*     */   {
/* 310 */     return "XPathAlternative";
/*     */   }
/*     */ 
/*     */   public final String getBaseNamespace()
/*     */   {
/* 319 */     return "http://www.nue.et-inf.uni-siegen.de/~geuer-pollmann/#xpathFilter";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.transforms.params.XPathFilterCHGPContainer
 * JD-Core Version:    0.6.2
 */