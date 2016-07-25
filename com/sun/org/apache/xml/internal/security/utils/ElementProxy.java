/*     */ package com.sun.org.apache.xml.internal.security.utils;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
/*     */ import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
/*     */ import java.math.BigInteger;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import org.w3c.dom.Attr;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ import org.w3c.dom.Text;
/*     */ 
/*     */ public abstract class ElementProxy
/*     */ {
/*  44 */   protected static final Logger log = Logger.getLogger(ElementProxy.class.getName());
/*     */ 
/*  48 */   protected Element _constructionElement = null;
/*     */ 
/*  51 */   protected String _baseURI = null;
/*     */ 
/*  54 */   protected Document _doc = null;
/*     */ 
/*  57 */   private static Map<String, String> prefixMappings = new ConcurrentHashMap();
/*     */ 
/*     */   public ElementProxy()
/*     */   {
/*     */   }
/*     */ 
/*     */   public ElementProxy(Document paramDocument)
/*     */   {
/*  72 */     if (paramDocument == null) {
/*  73 */       throw new RuntimeException("Document is null");
/*     */     }
/*     */ 
/*  76 */     this._doc = paramDocument;
/*  77 */     this._constructionElement = createElementForFamilyLocal(this._doc, getBaseNamespace(), getBaseLocalName());
/*     */   }
/*     */ 
/*     */   public ElementProxy(Element paramElement, String paramString)
/*     */     throws XMLSecurityException
/*     */   {
/*  89 */     if (paramElement == null) {
/*  90 */       throw new XMLSecurityException("ElementProxy.nullElement");
/*     */     }
/*     */ 
/*  93 */     if (log.isLoggable(Level.FINE)) {
/*  94 */       log.log(Level.FINE, "setElement(\"" + paramElement.getTagName() + "\", \"" + paramString + "\")");
/*     */     }
/*     */ 
/*  97 */     this._doc = paramElement.getOwnerDocument();
/*  98 */     this._constructionElement = paramElement;
/*  99 */     this._baseURI = paramString;
/*     */ 
/* 101 */     guaranteeThatElementInCorrectSpace();
/*     */   }
/*     */ 
/*     */   public abstract String getBaseNamespace();
/*     */ 
/*     */   public abstract String getBaseLocalName();
/*     */ 
/*     */   protected Element createElementForFamilyLocal(Document paramDocument, String paramString1, String paramString2)
/*     */   {
/* 122 */     Element localElement = null;
/* 123 */     if (paramString1 == null) {
/* 124 */       localElement = paramDocument.createElementNS(null, paramString2);
/*     */     } else {
/* 126 */       String str1 = getBaseNamespace();
/* 127 */       String str2 = getDefaultPrefix(str1);
/* 128 */       if ((str2 == null) || (str2.length() == 0)) {
/* 129 */         localElement = paramDocument.createElementNS(paramString1, paramString2);
/* 130 */         localElement.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns", paramString1);
/*     */       } else {
/* 132 */         localElement = paramDocument.createElementNS(paramString1, str2 + ":" + paramString2);
/* 133 */         localElement.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:" + str2, paramString1);
/*     */       }
/*     */     }
/* 136 */     return localElement;
/*     */   }
/*     */ 
/*     */   public static Element createElementForFamily(Document paramDocument, String paramString1, String paramString2)
/*     */   {
/* 153 */     Element localElement = null;
/* 154 */     String str = getDefaultPrefix(paramString1);
/*     */ 
/* 156 */     if (paramString1 == null) {
/* 157 */       localElement = paramDocument.createElementNS(null, paramString2);
/*     */     }
/* 159 */     else if ((str == null) || (str.length() == 0)) {
/* 160 */       localElement = paramDocument.createElementNS(paramString1, paramString2);
/* 161 */       localElement.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns", paramString1);
/*     */     } else {
/* 163 */       localElement = paramDocument.createElementNS(paramString1, str + ":" + paramString2);
/* 164 */       localElement.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:" + str, paramString1);
/*     */     }
/*     */ 
/* 168 */     return localElement;
/*     */   }
/*     */ 
/*     */   public void setElement(Element paramElement, String paramString)
/*     */     throws XMLSecurityException
/*     */   {
/* 179 */     if (paramElement == null) {
/* 180 */       throw new XMLSecurityException("ElementProxy.nullElement");
/*     */     }
/*     */ 
/* 183 */     if (log.isLoggable(Level.FINE)) {
/* 184 */       log.log(Level.FINE, "setElement(" + paramElement.getTagName() + ", \"" + paramString + "\"");
/*     */     }
/*     */ 
/* 187 */     this._doc = paramElement.getOwnerDocument();
/* 188 */     this._constructionElement = paramElement;
/* 189 */     this._baseURI = paramString;
/*     */   }
/*     */ 
/*     */   public final Element getElement()
/*     */   {
/* 199 */     return this._constructionElement;
/*     */   }
/*     */ 
/*     */   public final NodeList getElementPlusReturns()
/*     */   {
/* 209 */     HelperNodeList localHelperNodeList = new HelperNodeList();
/*     */ 
/* 211 */     localHelperNodeList.appendChild(this._doc.createTextNode("\n"));
/* 212 */     localHelperNodeList.appendChild(getElement());
/* 213 */     localHelperNodeList.appendChild(this._doc.createTextNode("\n"));
/*     */ 
/* 215 */     return localHelperNodeList;
/*     */   }
/*     */ 
/*     */   public Document getDocument()
/*     */   {
/* 224 */     return this._doc;
/*     */   }
/*     */ 
/*     */   public String getBaseURI()
/*     */   {
/* 233 */     return this._baseURI;
/*     */   }
/*     */ 
/*     */   void guaranteeThatElementInCorrectSpace()
/*     */     throws XMLSecurityException
/*     */   {
/* 243 */     String str1 = getBaseLocalName();
/* 244 */     String str2 = getBaseNamespace();
/*     */ 
/* 246 */     String str3 = this._constructionElement.getLocalName();
/* 247 */     String str4 = this._constructionElement.getNamespaceURI();
/*     */ 
/* 249 */     if ((!str2.equals(str4)) && (!str1.equals(str3)))
/*     */     {
/* 251 */       Object[] arrayOfObject = { str4 + ":" + str3, str2 + ":" + str1 };
/*     */ 
/* 253 */       throw new XMLSecurityException("xml.WrongElement", arrayOfObject);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void addBigIntegerElement(BigInteger paramBigInteger, String paramString)
/*     */   {
/* 264 */     if (paramBigInteger != null) {
/* 265 */       Element localElement = XMLUtils.createElementInSignatureSpace(this._doc, paramString);
/*     */ 
/* 267 */       Base64.fillElementWithBigInteger(localElement, paramBigInteger);
/* 268 */       this._constructionElement.appendChild(localElement);
/* 269 */       XMLUtils.addReturnToElement(this._constructionElement);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void addBase64Element(byte[] paramArrayOfByte, String paramString)
/*     */   {
/* 280 */     if (paramArrayOfByte != null) {
/* 281 */       Element localElement = Base64.encodeToElement(this._doc, paramString, paramArrayOfByte);
/*     */ 
/* 283 */       this._constructionElement.appendChild(localElement);
/* 284 */       if (!XMLUtils.ignoreLineBreaks())
/* 285 */         this._constructionElement.appendChild(this._doc.createTextNode("\n"));
/*     */     }
/*     */   }
/*     */ 
/*     */   public void addTextElement(String paramString1, String paramString2)
/*     */   {
/* 297 */     Element localElement = XMLUtils.createElementInSignatureSpace(this._doc, paramString2);
/* 298 */     Text localText = this._doc.createTextNode(paramString1);
/*     */ 
/* 300 */     localElement.appendChild(localText);
/* 301 */     this._constructionElement.appendChild(localElement);
/* 302 */     XMLUtils.addReturnToElement(this._constructionElement);
/*     */   }
/*     */ 
/*     */   public void addBase64Text(byte[] paramArrayOfByte)
/*     */   {
/* 311 */     if (paramArrayOfByte != null) {
/* 312 */       Text localText = XMLUtils.ignoreLineBreaks() ? this._doc.createTextNode(Base64.encode(paramArrayOfByte)) : this._doc.createTextNode("\n" + Base64.encode(paramArrayOfByte) + "\n");
/*     */ 
/* 315 */       this._constructionElement.appendChild(localText);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void addText(String paramString)
/*     */   {
/* 325 */     if (paramString != null) {
/* 326 */       Text localText = this._doc.createTextNode(paramString);
/*     */ 
/* 328 */       this._constructionElement.appendChild(localText);
/*     */     }
/*     */   }
/*     */ 
/*     */   public BigInteger getBigIntegerFromChildElement(String paramString1, String paramString2)
/*     */     throws Base64DecodingException
/*     */   {
/* 343 */     return Base64.decodeBigIntegerFromText(XMLUtils.selectNodeText(this._constructionElement.getFirstChild(), paramString2, paramString1, 0));
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public byte[] getBytesFromChildElement(String paramString1, String paramString2)
/*     */     throws XMLSecurityException
/*     */   {
/* 361 */     Element localElement = XMLUtils.selectNode(this._constructionElement.getFirstChild(), paramString2, paramString1, 0);
/*     */ 
/* 366 */     return Base64.decode(localElement);
/*     */   }
/*     */ 
/*     */   public String getTextFromChildElement(String paramString1, String paramString2)
/*     */   {
/* 377 */     return XMLUtils.selectNode(this._constructionElement.getFirstChild(), paramString2, paramString1, 0).getTextContent();
/*     */   }
/*     */ 
/*     */   public byte[] getBytesFromTextChild()
/*     */     throws XMLSecurityException
/*     */   {
/* 391 */     return Base64.decode(XMLUtils.getFullTextChildrenFromElement(this._constructionElement));
/*     */   }
/*     */ 
/*     */   public String getTextFromTextChild()
/*     */   {
/* 401 */     return XMLUtils.getFullTextChildrenFromElement(this._constructionElement);
/*     */   }
/*     */ 
/*     */   public int length(String paramString1, String paramString2)
/*     */   {
/* 412 */     int i = 0;
/* 413 */     Node localNode = this._constructionElement.getFirstChild();
/* 414 */     while (localNode != null) {
/* 415 */       if ((paramString2.equals(localNode.getLocalName())) && (paramString1.equals(localNode.getNamespaceURI())))
/*     */       {
/* 417 */         i++;
/*     */       }
/* 419 */       localNode = localNode.getNextSibling();
/*     */     }
/* 421 */     return i;
/*     */   }
/*     */ 
/*     */   public void setXPathNamespaceContext(String paramString1, String paramString2)
/*     */     throws XMLSecurityException
/*     */   {
/* 441 */     if ((paramString1 == null) || (paramString1.length() == 0))
/* 442 */       throw new XMLSecurityException("defaultNamespaceCannotBeSetHere");
/* 443 */     if (paramString1.equals("xmlns"))
/* 444 */       throw new XMLSecurityException("defaultNamespaceCannotBeSetHere");
/*     */     String str;
/* 445 */     if (paramString1.startsWith("xmlns:"))
/* 446 */       str = paramString1;
/*     */     else {
/* 448 */       str = "xmlns:" + paramString1;
/*     */     }
/*     */ 
/* 451 */     Attr localAttr = this._constructionElement.getAttributeNodeNS("http://www.w3.org/2000/xmlns/", str);
/*     */ 
/* 453 */     if (localAttr != null) {
/* 454 */       if (!localAttr.getNodeValue().equals(paramString2)) {
/* 455 */         Object[] arrayOfObject = { str, this._constructionElement.getAttributeNS(null, str) };
/*     */ 
/* 457 */         throw new XMLSecurityException("namespacePrefixAlreadyUsedByOtherURI", arrayOfObject);
/*     */       }
/* 459 */       return;
/*     */     }
/*     */ 
/* 462 */     this._constructionElement.setAttributeNS("http://www.w3.org/2000/xmlns/", str, paramString2);
/*     */   }
/*     */ 
/*     */   public static void setDefaultPrefix(String paramString1, String paramString2)
/*     */     throws XMLSecurityException
/*     */   {
/* 476 */     JavaUtils.checkRegisterPermission();
/* 477 */     if (prefixMappings.containsValue(paramString2)) {
/* 478 */       String str = (String)prefixMappings.get(paramString1);
/* 479 */       if (!str.equals(paramString2)) {
/* 480 */         Object[] arrayOfObject = { paramString2, paramString1, str };
/*     */ 
/* 482 */         throw new XMLSecurityException("prefix.AlreadyAssigned", arrayOfObject);
/*     */       }
/*     */     }
/*     */ 
/* 486 */     if ("http://www.w3.org/2000/09/xmldsig#".equals(paramString1)) {
/* 487 */       XMLUtils.setDsPrefix(paramString2);
/*     */     }
/* 489 */     if ("http://www.w3.org/2001/04/xmlenc#".equals(paramString1)) {
/* 490 */       XMLUtils.setXencPrefix(paramString2);
/*     */     }
/* 492 */     prefixMappings.put(paramString1, paramString2);
/*     */   }
/*     */ 
/*     */   public static void registerDefaultPrefixes()
/*     */     throws XMLSecurityException
/*     */   {
/* 499 */     setDefaultPrefix("http://www.w3.org/2000/09/xmldsig#", "ds");
/* 500 */     setDefaultPrefix("http://www.w3.org/2001/04/xmlenc#", "xenc");
/* 501 */     setDefaultPrefix("http://www.w3.org/2009/xmlenc11#", "xenc11");
/* 502 */     setDefaultPrefix("http://www.xmlsecurity.org/experimental#", "experimental");
/* 503 */     setDefaultPrefix("http://www.w3.org/2002/04/xmldsig-filter2", "dsig-xpath-old");
/* 504 */     setDefaultPrefix("http://www.w3.org/2002/06/xmldsig-filter2", "dsig-xpath");
/* 505 */     setDefaultPrefix("http://www.w3.org/2001/10/xml-exc-c14n#", "ec");
/* 506 */     setDefaultPrefix("http://www.nue.et-inf.uni-siegen.de/~geuer-pollmann/#xpathFilter", "xx");
/*     */   }
/*     */ 
/*     */   public static String getDefaultPrefix(String paramString)
/*     */   {
/* 518 */     return (String)prefixMappings.get(paramString);
/*     */   }
/*     */ 
/*     */   protected void setLocalIdAttribute(String paramString1, String paramString2)
/*     */   {
/* 523 */     if (paramString2 != null) {
/* 524 */       Attr localAttr = getDocument().createAttributeNS(null, paramString1);
/* 525 */       localAttr.setValue(paramString2);
/* 526 */       getElement().setAttributeNodeNS(localAttr);
/* 527 */       getElement().setIdAttributeNode(localAttr, true);
/*     */     }
/*     */     else {
/* 530 */       getElement().removeAttributeNS(null, paramString1);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.utils.ElementProxy
 * JD-Core Version:    0.6.2
 */