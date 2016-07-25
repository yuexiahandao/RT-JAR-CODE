/*     */ package com.sun.org.apache.xml.internal.security.c14n.implementations;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.security.c14n.CanonicalizationException;
/*     */ import com.sun.org.apache.xml.internal.security.c14n.helper.C14nHelper;
/*     */ import com.sun.org.apache.xml.internal.security.signature.XMLSignatureInput;
/*     */ import com.sun.org.apache.xml.internal.security.utils.XMLUtils;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.SortedSet;
/*     */ import java.util.TreeSet;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import org.w3c.dom.Attr;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.NamedNodeMap;
/*     */ import org.w3c.dom.Node;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public abstract class Canonicalizer20010315 extends CanonicalizerBase
/*     */ {
/*  59 */   boolean firstCall = true;
/*  60 */   final SortedSet result = new TreeSet(COMPARE);
/*     */   static final String XMLNS_URI = "http://www.w3.org/2000/xmlns/";
/*     */   static final String XML_LANG_URI = "http://www.w3.org/XML/1998/namespace";
/* 140 */   XmlAttrStack xmlattrStack = new XmlAttrStack();
/*     */ 
/*     */   public Canonicalizer20010315(boolean paramBoolean)
/*     */   {
/* 147 */     super(paramBoolean);
/*     */   }
/*     */ 
/*     */   Iterator handleAttributesSubtree(Element paramElement, NameSpaceSymbTable paramNameSpaceSymbTable)
/*     */     throws CanonicalizationException
/*     */   {
/* 166 */     if ((!paramElement.hasAttributes()) && (!this.firstCall)) {
/* 167 */       return null;
/*     */     }
/*     */ 
/* 170 */     SortedSet localSortedSet = this.result;
/* 171 */     localSortedSet.clear();
/* 172 */     NamedNodeMap localNamedNodeMap = paramElement.getAttributes();
/* 173 */     int i = localNamedNodeMap.getLength();
/*     */ 
/* 175 */     for (int j = 0; j < i; j++) {
/* 176 */       Attr localAttr = (Attr)localNamedNodeMap.item(j);
/* 177 */       String str1 = localAttr.getNamespaceURI();
/*     */ 
/* 179 */       if ("http://www.w3.org/2000/xmlns/" != str1)
/*     */       {
/* 181 */         localSortedSet.add(localAttr);
/*     */       }
/*     */       else
/*     */       {
/* 185 */         String str2 = localAttr.getLocalName();
/* 186 */         String str3 = localAttr.getValue();
/* 187 */         if ((!"xml".equals(str2)) || (!"http://www.w3.org/XML/1998/namespace".equals(str3)))
/*     */         {
/* 193 */           Node localNode = paramNameSpaceSymbTable.addMappingAndRender(str2, str3, localAttr);
/*     */ 
/* 195 */           if (localNode != null)
/*     */           {
/* 197 */             localSortedSet.add(localNode);
/* 198 */             if (C14nHelper.namespaceIsRelative(localAttr)) {
/* 199 */               Object[] arrayOfObject = { paramElement.getTagName(), str2, localAttr.getNodeValue() };
/* 200 */               throw new CanonicalizationException("c14n.Canonicalizer.RelativeNamespace", arrayOfObject);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 206 */     if (this.firstCall)
/*     */     {
/* 209 */       paramNameSpaceSymbTable.getUnrenderedNodes(localSortedSet);
/*     */ 
/* 211 */       this.xmlattrStack.getXmlnsAttr(localSortedSet);
/* 212 */       this.firstCall = false;
/*     */     }
/*     */ 
/* 215 */     return localSortedSet.iterator();
/*     */   }
/*     */ 
/*     */   Iterator handleAttributes(Element paramElement, NameSpaceSymbTable paramNameSpaceSymbTable)
/*     */     throws CanonicalizationException
/*     */   {
/* 232 */     this.xmlattrStack.push(paramNameSpaceSymbTable.getLevel());
/* 233 */     int i = isVisibleDO(paramElement, paramNameSpaceSymbTable.getLevel()) == 1 ? 1 : 0;
/* 234 */     NamedNodeMap localNamedNodeMap = null;
/* 235 */     int j = 0;
/* 236 */     if (paramElement.hasAttributes()) {
/* 237 */       localNamedNodeMap = paramElement.getAttributes();
/* 238 */       j = localNamedNodeMap.getLength();
/*     */     }
/*     */ 
/* 242 */     SortedSet localSortedSet = this.result;
/* 243 */     localSortedSet.clear();
/*     */     Object localObject;
/* 245 */     for (int k = 0; k < j; k++) {
/* 246 */       localObject = (Attr)localNamedNodeMap.item(k);
/* 247 */       String str1 = ((Attr)localObject).getNamespaceURI();
/*     */ 
/* 249 */       if ("http://www.w3.org/2000/xmlns/" != str1)
/*     */       {
/* 251 */         if ("http://www.w3.org/XML/1998/namespace" == str1)
/* 252 */           this.xmlattrStack.addXmlnsAttr((Attr)localObject);
/* 253 */         else if (i != 0)
/*     */         {
/* 255 */           localSortedSet.add(localObject);
/*     */         }
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 261 */         String str2 = ((Attr)localObject).getLocalName();
/* 262 */         String str3 = ((Attr)localObject).getValue();
/* 263 */         if ((!"xml".equals(str2)) || (!"http://www.w3.org/XML/1998/namespace".equals(str3)))
/*     */         {
/* 272 */           if (isVisible((Node)localObject)) {
/* 273 */             if ((i != 0) || (!paramNameSpaceSymbTable.removeMappingIfRender(str2)))
/*     */             {
/* 278 */               Node localNode = paramNameSpaceSymbTable.addMappingAndRender(str2, str3, (Attr)localObject);
/* 279 */               if (localNode != null) {
/* 280 */                 localSortedSet.add(localNode);
/* 281 */                 if (C14nHelper.namespaceIsRelative((Attr)localObject)) {
/* 282 */                   Object[] arrayOfObject = { paramElement.getTagName(), str2, ((Attr)localObject).getNodeValue() };
/* 283 */                   throw new CanonicalizationException("c14n.Canonicalizer.RelativeNamespace", arrayOfObject);
/*     */                 }
/*     */               }
/*     */             }
/*     */           }
/* 288 */           else if ((i != 0) && (str2 != "xmlns"))
/* 289 */             paramNameSpaceSymbTable.removeMapping(str2);
/*     */           else
/* 291 */             paramNameSpaceSymbTable.addMapping(str2, str3, (Attr)localObject);
/*     */         }
/*     */       }
/*     */     }
/* 295 */     if (i != 0)
/*     */     {
/* 297 */       Attr localAttr = paramElement.getAttributeNodeNS("http://www.w3.org/2000/xmlns/", "xmlns");
/* 298 */       localObject = null;
/* 299 */       if (localAttr == null)
/*     */       {
/* 301 */         localObject = paramNameSpaceSymbTable.getMapping("xmlns");
/* 302 */       } else if (!isVisible(localAttr))
/*     */       {
/* 305 */         localObject = paramNameSpaceSymbTable.addMappingAndRender("xmlns", "", getNullNode(localAttr.getOwnerDocument()));
/*     */       }
/*     */ 
/* 309 */       if (localObject != null) {
/* 310 */         localSortedSet.add(localObject);
/*     */       }
/*     */ 
/* 314 */       this.xmlattrStack.getXmlnsAttr(localSortedSet);
/* 315 */       paramNameSpaceSymbTable.getUnrenderedNodes(localSortedSet);
/*     */     }
/*     */ 
/* 319 */     return localSortedSet.iterator();
/*     */   }
/*     */ 
/*     */   public byte[] engineCanonicalizeXPathNodeSet(Set paramSet, String paramString)
/*     */     throws CanonicalizationException
/*     */   {
/* 333 */     throw new CanonicalizationException("c14n.Canonicalizer.UnsupportedOperation");
/*     */   }
/*     */ 
/*     */   public byte[] engineCanonicalizeSubTree(Node paramNode, String paramString)
/*     */     throws CanonicalizationException
/*     */   {
/* 349 */     throw new CanonicalizationException("c14n.Canonicalizer.UnsupportedOperation");
/*     */   }
/*     */ 
/*     */   void circumventBugIfNeeded(XMLSignatureInput paramXMLSignatureInput) throws CanonicalizationException, ParserConfigurationException, IOException, SAXException {
/* 353 */     if (!paramXMLSignatureInput.isNeedsToBeExpanded())
/* 354 */       return;
/* 355 */     Document localDocument = null;
/* 356 */     if (paramXMLSignatureInput.getSubNode() != null)
/* 357 */       localDocument = XMLUtils.getOwnerDocument(paramXMLSignatureInput.getSubNode());
/*     */     else {
/* 359 */       localDocument = XMLUtils.getOwnerDocument(paramXMLSignatureInput.getNodeSet());
/*     */     }
/* 361 */     XMLUtils.circumventBug2650(localDocument);
/*     */   }
/*     */ 
/*     */   void handleParent(Element paramElement, NameSpaceSymbTable paramNameSpaceSymbTable)
/*     */   {
/* 366 */     if (!paramElement.hasAttributes()) {
/* 367 */       return;
/*     */     }
/* 369 */     this.xmlattrStack.push(-1);
/* 370 */     NamedNodeMap localNamedNodeMap = paramElement.getAttributes();
/* 371 */     int i = localNamedNodeMap.getLength();
/* 372 */     for (int j = 0; j < i; j++) {
/* 373 */       Attr localAttr = (Attr)localNamedNodeMap.item(j);
/* 374 */       if ("http://www.w3.org/2000/xmlns/" != localAttr.getNamespaceURI())
/*     */       {
/* 376 */         if ("http://www.w3.org/XML/1998/namespace" == localAttr.getNamespaceURI()) {
/* 377 */           this.xmlattrStack.addXmlnsAttr(localAttr);
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 382 */         String str1 = localAttr.getLocalName();
/* 383 */         String str2 = localAttr.getNodeValue();
/* 384 */         if ((!"xml".equals(str1)) || (!"http://www.w3.org/XML/1998/namespace".equals(str2)))
/*     */         {
/* 388 */           paramNameSpaceSymbTable.addMapping(str1, str2, localAttr);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   static class XmlAttrStack
/*     */   {
/*  64 */     int currentLevel = 0;
/*  65 */     int lastlevel = 0;
/*     */     XmlsStackElement cur;
/*  72 */     List levels = new ArrayList();
/*     */ 
/*  74 */     void push(int paramInt) { this.currentLevel = paramInt;
/*  75 */       if (this.currentLevel == -1)
/*  76 */         return;
/*  77 */       this.cur = null;
/*  78 */       while (this.lastlevel >= this.currentLevel) {
/*  79 */         this.levels.remove(this.levels.size() - 1);
/*  80 */         if (this.levels.size() == 0) {
/*  81 */           this.lastlevel = 0;
/*  82 */           return;
/*     */         }
/*  84 */         this.lastlevel = ((XmlsStackElement)this.levels.get(this.levels.size() - 1)).level;
/*     */       } }
/*     */ 
/*     */     void addXmlnsAttr(Attr paramAttr) {
/*  88 */       if (this.cur == null) {
/*  89 */         this.cur = new XmlsStackElement();
/*  90 */         this.cur.level = this.currentLevel;
/*  91 */         this.levels.add(this.cur);
/*  92 */         this.lastlevel = this.currentLevel;
/*     */       }
/*  94 */       this.cur.nodes.add(paramAttr);
/*     */     }
/*     */     void getXmlnsAttr(Collection paramCollection) {
/*  97 */       int i = this.levels.size() - 1;
/*  98 */       if (this.cur == null) {
/*  99 */         this.cur = new XmlsStackElement();
/* 100 */         this.cur.level = this.currentLevel;
/* 101 */         this.lastlevel = this.currentLevel;
/* 102 */         this.levels.add(this.cur);
/*     */       }
/* 104 */       int j = 0;
/* 105 */       XmlsStackElement localXmlsStackElement = null;
/* 106 */       if (i == -1) {
/* 107 */         j = 1;
/*     */       } else {
/* 109 */         localXmlsStackElement = (XmlsStackElement)this.levels.get(i);
/* 110 */         if ((localXmlsStackElement.rendered) && (localXmlsStackElement.level + 1 == this.currentLevel)) {
/* 111 */           j = 1;
/*     */         }
/*     */       }
/* 114 */       if (j != 0) {
/* 115 */         paramCollection.addAll(this.cur.nodes);
/* 116 */         this.cur.rendered = true;
/* 117 */         return;
/*     */       }
/*     */ 
/* 120 */       HashMap localHashMap = new HashMap();
/* 121 */       for (; i >= 0; i--) {
/* 122 */         localXmlsStackElement = (XmlsStackElement)this.levels.get(i);
/* 123 */         Iterator localIterator = localXmlsStackElement.nodes.iterator();
/* 124 */         while (localIterator.hasNext()) {
/* 125 */           Attr localAttr = (Attr)localIterator.next();
/* 126 */           if (!localHashMap.containsKey(localAttr.getName())) {
/* 127 */             localHashMap.put(localAttr.getName(), localAttr);
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 135 */       this.cur.rendered = true;
/* 136 */       paramCollection.addAll(localHashMap.values());
/*     */     }
/*     */ 
/*     */     static class XmlsStackElement
/*     */     {
/*     */       int level;
/*  69 */       boolean rendered = false;
/*  70 */       List nodes = new ArrayList();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.c14n.implementations.Canonicalizer20010315
 * JD-Core Version:    0.6.2
 */