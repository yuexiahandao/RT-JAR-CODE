/*     */ package com.sun.org.apache.xml.internal.security.utils;
/*     */ 
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.WeakHashMap;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import org.w3c.dom.Attr;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.NamedNodeMap;
/*     */ import org.w3c.dom.Node;
/*     */ 
/*     */ public class IdResolver
/*     */ {
/*  52 */   private static Logger log = Logger.getLogger(IdResolver.class.getName());
/*     */ 
/*  55 */   private static WeakHashMap docMap = new WeakHashMap();
/*     */ 
/* 183 */   private static List names = Arrays.asList(arrayOfString);
/* 184 */   private static int namesLength = names.size();
/*     */ 
/*     */   public static void registerElementById(Element paramElement, String paramString)
/*     */   {
/*  72 */     Document localDocument = paramElement.getOwnerDocument();
/*     */     WeakHashMap localWeakHashMap;
/*  74 */     synchronized (docMap) {
/*  75 */       localWeakHashMap = (WeakHashMap)docMap.get(localDocument);
/*  76 */       if (localWeakHashMap == null) {
/*  77 */         localWeakHashMap = new WeakHashMap();
/*  78 */         docMap.put(localDocument, localWeakHashMap);
/*     */       }
/*     */     }
/*  81 */     localWeakHashMap.put(paramString, new WeakReference(paramElement));
/*     */   }
/*     */ 
/*     */   public static void registerElementById(Element paramElement, Attr paramAttr)
/*     */   {
/*  91 */     registerElementById(paramElement, paramAttr.getNodeValue());
/*     */   }
/*     */ 
/*     */   public static Element getElementById(Document paramDocument, String paramString)
/*     */   {
/* 103 */     Element localElement = getElementByIdType(paramDocument, paramString);
/*     */ 
/* 105 */     if (localElement != null) {
/* 106 */       log.log(Level.FINE, "I could find an Element using the simple getElementByIdType method: " + localElement.getTagName());
/*     */ 
/* 110 */       return localElement;
/*     */     }
/*     */ 
/* 113 */     localElement = getElementByIdUsingDOM(paramDocument, paramString);
/*     */ 
/* 115 */     if (localElement != null) {
/* 116 */       log.log(Level.FINE, "I could find an Element using the simple getElementByIdUsingDOM method: " + localElement.getTagName());
/*     */ 
/* 120 */       return localElement;
/*     */     }
/*     */ 
/* 124 */     localElement = getElementBySearching(paramDocument, paramString);
/*     */ 
/* 126 */     if (localElement != null) {
/* 127 */       registerElementById(localElement, paramString);
/*     */ 
/* 129 */       return localElement;
/*     */     }
/*     */ 
/* 132 */     return null;
/*     */   }
/*     */ 
/*     */   private static Element getElementByIdUsingDOM(Document paramDocument, String paramString)
/*     */   {
/* 144 */     if (log.isLoggable(Level.FINE))
/* 145 */       log.log(Level.FINE, "getElementByIdUsingDOM() Search for ID " + paramString);
/* 146 */     return paramDocument.getElementById(paramString);
/*     */   }
/*     */ 
/*     */   private static Element getElementByIdType(Document paramDocument, String paramString)
/*     */   {
/* 157 */     if (log.isLoggable(Level.FINE))
/* 158 */       log.log(Level.FINE, "getElementByIdType() Search for ID " + paramString);
/*     */     WeakHashMap localWeakHashMap;
/* 160 */     synchronized (docMap) {
/* 161 */       localWeakHashMap = (WeakHashMap)docMap.get(paramDocument);
/*     */     }
/* 163 */     if (localWeakHashMap != null) {
/* 164 */       ??? = (WeakReference)localWeakHashMap.get(paramString);
/* 165 */       if (??? != null) {
/* 166 */         return (Element)((WeakReference)???).get();
/*     */       }
/*     */     }
/* 169 */     return null;
/*     */   }
/*     */ 
/*     */   private static Element getElementBySearching(Node paramNode, String paramString)
/*     */   {
/* 189 */     Element[] arrayOfElement = new Element[namesLength + 1];
/* 190 */     getEl(paramNode, paramString, arrayOfElement);
/* 191 */     for (int i = 0; i < arrayOfElement.length; i++) {
/* 192 */       if (arrayOfElement[i] != null) {
/* 193 */         return arrayOfElement[i];
/*     */       }
/*     */     }
/* 196 */     return null;
/*     */   }
/*     */ 
/*     */   private static int getEl(Node paramNode, String paramString, Element[] paramArrayOfElement) {
/* 200 */     Node localNode = null;
/* 201 */     Object localObject = null;
/*     */     while (true) {
/* 203 */       switch (paramNode.getNodeType()) {
/*     */       case 9:
/*     */       case 11:
/* 206 */         localNode = paramNode.getFirstChild();
/* 207 */         break;
/*     */       case 1:
/* 211 */         Element localElement = (Element)paramNode;
/* 212 */         if (isElement(localElement, paramString, paramArrayOfElement) == 1)
/* 213 */           return 1;
/* 214 */         localNode = paramNode.getFirstChild();
/* 215 */         if (localNode == null) {
/* 216 */           if (localObject != null)
/* 217 */             localNode = paramNode.getNextSibling();
/*     */         }
/*     */         else
/* 220 */           localObject = localElement;
/*     */         break;
/*     */       }
/* 223 */       while ((localNode == null) && (localObject != null)) {
/* 224 */         localNode = ((Node)localObject).getNextSibling();
/* 225 */         localObject = ((Node)localObject).getParentNode();
/* 226 */         if ((localObject != null) && (((Node)localObject).getNodeType() != 1)) {
/* 227 */           localObject = null;
/*     */         }
/*     */       }
/* 230 */       if (localNode == null)
/* 231 */         return 1;
/* 232 */       paramNode = localNode;
/* 233 */       localNode = paramNode.getNextSibling();
/*     */     }
/*     */   }
/*     */ 
/*     */   public static int isElement(Element paramElement, String paramString, Element[] paramArrayOfElement) {
/* 238 */     if (!paramElement.hasAttributes()) {
/* 239 */       return 0;
/*     */     }
/* 241 */     NamedNodeMap localNamedNodeMap = paramElement.getAttributes();
/* 242 */     int i = names.indexOf(paramElement.getNamespaceURI());
/* 243 */     i = i < 0 ? namesLength : i;
/* 244 */     int j = localNamedNodeMap.getLength(); for (int k = 0; k < j; k++) {
/* 245 */       Attr localAttr = (Attr)localNamedNodeMap.item(k);
/* 246 */       String str1 = localAttr.getNamespaceURI();
/*     */ 
/* 248 */       int m = str1 == null ? i : names.indexOf(localAttr.getNamespaceURI());
/* 249 */       m = m < 0 ? namesLength : m;
/* 250 */       String str2 = localAttr.getLocalName();
/* 251 */       if (str2 == null)
/* 252 */         str2 = localAttr.getName();
/* 253 */       if (str2.length() <= 2)
/*     */       {
/* 255 */         String str3 = localAttr.getNodeValue();
/* 256 */         if (str2.charAt(0) == 'I') {
/* 257 */           int n = str2.charAt(1);
/* 258 */           if ((n == 100) && (str3.equals(paramString))) {
/* 259 */             paramArrayOfElement[m] = paramElement;
/* 260 */             if (m == 0)
/* 261 */               return 1;
/*     */           }
/* 263 */           else if ((n == 68) && (str3.endsWith(paramString))) {
/* 264 */             if (m != 3) {
/* 265 */               m = namesLength;
/*     */             }
/* 267 */             paramArrayOfElement[m] = paramElement;
/*     */           }
/* 269 */         } else if (("id".equals(str2)) && (str3.equals(paramString))) {
/* 270 */           if (m != 2) {
/* 271 */             m = namesLength;
/*     */           }
/* 273 */           paramArrayOfElement[m] = paramElement;
/*     */         }
/*     */       }
/*     */     }
/* 277 */     if ((i == 3) && ((paramElement.getAttribute("OriginalRequestID").equals(paramString)) || (paramElement.getAttribute("RequestID").equals(paramString)) || (paramElement.getAttribute("ResponseID").equals(paramString))))
/*     */     {
/* 281 */       paramArrayOfElement[3] = paramElement;
/* 282 */     } else if ((i == 4) && (paramElement.getAttribute("AssertionID").equals(paramString)))
/*     */     {
/* 284 */       paramArrayOfElement[4] = paramElement;
/* 285 */     } else if ((i == 5) && ((paramElement.getAttribute("RequestID").equals(paramString)) || (paramElement.getAttribute("ResponseID").equals(paramString))))
/*     */     {
/* 288 */       paramArrayOfElement[5] = paramElement;
/*     */     }
/* 290 */     return 0;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 175 */     String[] arrayOfString = { "http://www.w3.org/2000/09/xmldsig#", "http://www.w3.org/2001/04/xmlenc#", "http://schemas.xmlsoap.org/soap/security/2000-12", "http://www.w3.org/2002/03/xkms#", "urn:oasis:names:tc:SAML:1.0:assertion", "urn:oasis:names:tc:SAML:1.0:protocol" };
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.utils.IdResolver
 * JD-Core Version:    0.6.2
 */