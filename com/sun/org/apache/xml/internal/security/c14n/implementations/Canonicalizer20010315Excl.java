/*     */ package com.sun.org.apache.xml.internal.security.c14n.implementations;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.security.c14n.CanonicalizationException;
/*     */ import com.sun.org.apache.xml.internal.security.c14n.helper.C14nHelper;
/*     */ import com.sun.org.apache.xml.internal.security.signature.XMLSignatureInput;
/*     */ import com.sun.org.apache.xml.internal.security.transforms.params.InclusiveNamespaces;
/*     */ import com.sun.org.apache.xml.internal.security.utils.XMLUtils;
/*     */ import java.io.IOException;
/*     */ import java.util.Iterator;
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
/*     */ public abstract class Canonicalizer20010315Excl extends CanonicalizerBase
/*     */ {
/*  64 */   TreeSet _inclusiveNSSet = new TreeSet();
/*     */   static final String XMLNS_URI = "http://www.w3.org/2000/xmlns/";
/*  66 */   final SortedSet result = new TreeSet(COMPARE);
/*     */ 
/*     */   public Canonicalizer20010315Excl(boolean paramBoolean)
/*     */   {
/*  73 */     super(paramBoolean);
/*     */   }
/*     */ 
/*     */   public byte[] engineCanonicalizeSubTree(Node paramNode)
/*     */     throws CanonicalizationException
/*     */   {
/*  85 */     return engineCanonicalizeSubTree(paramNode, "", null);
/*     */   }
/*     */ 
/*     */   public byte[] engineCanonicalizeSubTree(Node paramNode, String paramString)
/*     */     throws CanonicalizationException
/*     */   {
/*  97 */     return engineCanonicalizeSubTree(paramNode, paramString, null);
/*     */   }
/*     */ 
/*     */   public byte[] engineCanonicalizeSubTree(Node paramNode1, String paramString, Node paramNode2)
/*     */     throws CanonicalizationException
/*     */   {
/* 109 */     this._inclusiveNSSet = ((TreeSet)InclusiveNamespaces.prefixStr2Set(paramString));
/*     */ 
/* 111 */     return super.engineCanonicalizeSubTree(paramNode1, paramNode2);
/*     */   }
/*     */ 
/*     */   public byte[] engineCanonicalize(XMLSignatureInput paramXMLSignatureInput, String paramString)
/*     */     throws CanonicalizationException
/*     */   {
/* 122 */     this._inclusiveNSSet = ((TreeSet)InclusiveNamespaces.prefixStr2Set(paramString));
/*     */ 
/* 124 */     return super.engineCanonicalize(paramXMLSignatureInput);
/*     */   }
/*     */ 
/*     */   Iterator handleAttributesSubtree(Element paramElement, NameSpaceSymbTable paramNameSpaceSymbTable)
/*     */     throws CanonicalizationException
/*     */   {
/* 138 */     SortedSet localSortedSet1 = this.result;
/* 139 */     localSortedSet1.clear();
/* 140 */     NamedNodeMap localNamedNodeMap = null;
/*     */ 
/* 142 */     int i = 0;
/* 143 */     if (paramElement.hasAttributes()) {
/* 144 */       localNamedNodeMap = paramElement.getAttributes();
/* 145 */       i = localNamedNodeMap.getLength();
/*     */     }
/*     */ 
/* 148 */     SortedSet localSortedSet2 = (SortedSet)this._inclusiveNSSet.clone();
/*     */     String str2;
/*     */     Object localObject2;
/* 150 */     for (int j = 0; j < i; j++) {
/* 151 */       localObject1 = (Attr)localNamedNodeMap.item(j);
/*     */ 
/* 153 */       if ("http://www.w3.org/2000/xmlns/" != ((Attr)localObject1).getNamespaceURI())
/*     */       {
/* 156 */         str2 = ((Attr)localObject1).getPrefix();
/* 157 */         if ((str2 != null) && (!str2.equals("xml")) && (!str2.equals("xmlns"))) {
/* 158 */           localSortedSet2.add(str2);
/*     */         }
/*     */ 
/* 161 */         localSortedSet1.add(localObject1);
/*     */       }
/*     */       else {
/* 164 */         str2 = ((Attr)localObject1).getLocalName();
/* 165 */         localObject2 = ((Attr)localObject1).getNodeValue();
/*     */ 
/* 167 */         if (paramNameSpaceSymbTable.addMapping(str2, (String)localObject2, (Attr)localObject1))
/*     */         {
/* 169 */           if (C14nHelper.namespaceIsRelative((String)localObject2)) {
/* 170 */             Object[] arrayOfObject = { paramElement.getTagName(), str2, ((Attr)localObject1).getNodeValue() };
/*     */ 
/* 172 */             throw new CanonicalizationException("c14n.Canonicalizer.RelativeNamespace", arrayOfObject);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     String str1;
/* 178 */     if (paramElement.getNamespaceURI() != null) {
/* 179 */       str1 = paramElement.getPrefix();
/* 180 */       if ((str1 == null) || (str1.length() == 0))
/* 181 */         str1 = "xmlns";
/*     */     }
/*     */     else
/*     */     {
/* 185 */       str1 = "xmlns";
/*     */     }
/* 187 */     localSortedSet2.add(str1);
/*     */ 
/* 190 */     Object localObject1 = localSortedSet2.iterator();
/* 191 */     while (((Iterator)localObject1).hasNext()) {
/* 192 */       str2 = (String)((Iterator)localObject1).next();
/* 193 */       localObject2 = paramNameSpaceSymbTable.getMapping(str2);
/* 194 */       if (localObject2 != null)
/*     */       {
/* 197 */         localSortedSet1.add(localObject2);
/*     */       }
/*     */     }
/* 200 */     return localSortedSet1.iterator();
/*     */   }
/*     */ 
/*     */   public byte[] engineCanonicalizeXPathNodeSet(Set paramSet, String paramString)
/*     */     throws CanonicalizationException
/*     */   {
/* 214 */     this._inclusiveNSSet = ((TreeSet)InclusiveNamespaces.prefixStr2Set(paramString));
/*     */ 
/* 216 */     return super.engineCanonicalizeXPathNodeSet(paramSet);
/*     */   }
/*     */ 
/*     */   final Iterator handleAttributes(Element paramElement, NameSpaceSymbTable paramNameSpaceSymbTable)
/*     */     throws CanonicalizationException
/*     */   {
/* 228 */     SortedSet localSortedSet = this.result;
/* 229 */     localSortedSet.clear();
/* 230 */     NamedNodeMap localNamedNodeMap = null;
/* 231 */     int i = 0;
/* 232 */     if (paramElement.hasAttributes()) {
/* 233 */       localNamedNodeMap = paramElement.getAttributes();
/* 234 */       i = localNamedNodeMap.getLength();
/*     */     }
/*     */ 
/* 237 */     Set localSet = null;
/*     */ 
/* 239 */     int j = isVisibleDO(paramElement, paramNameSpaceSymbTable.getLevel()) == 1 ? 1 : 0;
/* 240 */     if (j != 0)
/* 241 */       localSet = (Set)this._inclusiveNSSet.clone();
/*     */     Object localObject1;
/*     */     String str;
/*     */     Object localObject2;
/* 244 */     for (int k = 0; k < i; k++) {
/* 245 */       localObject1 = (Attr)localNamedNodeMap.item(k);
/*     */ 
/* 248 */       if ("http://www.w3.org/2000/xmlns/" != ((Attr)localObject1).getNamespaceURI()) {
/* 249 */         if (isVisible((Node)localObject1))
/*     */         {
/* 254 */           if (j != 0)
/*     */           {
/* 256 */             str = ((Attr)localObject1).getPrefix();
/* 257 */             if ((str != null) && (!str.equals("xml")) && (!str.equals("xmlns"))) {
/* 258 */               localSet.add(str);
/*     */             }
/*     */ 
/* 261 */             localSortedSet.add(localObject1);
/*     */           }
/*     */         }
/*     */       } else {
/* 265 */         str = ((Attr)localObject1).getLocalName();
/* 266 */         if ((j != 0) && (!isVisible((Node)localObject1)) && (str != "xmlns")) {
/* 267 */           paramNameSpaceSymbTable.removeMappingIfNotRender(str);
/*     */         }
/*     */         else {
/* 270 */           localObject2 = ((Attr)localObject1).getNodeValue();
/*     */           Object localObject3;
/* 272 */           if ((j == 0) && (isVisible((Node)localObject1)) && (this._inclusiveNSSet.contains(str)) && (!paramNameSpaceSymbTable.removeMappingIfRender(str))) {
/* 273 */             localObject3 = paramNameSpaceSymbTable.addMappingAndRender(str, (String)localObject2, (Attr)localObject1);
/* 274 */             if (localObject3 != null) {
/* 275 */               localSortedSet.add(localObject3);
/* 276 */               if (C14nHelper.namespaceIsRelative((Attr)localObject1)) {
/* 277 */                 Object[] arrayOfObject = { paramElement.getTagName(), str, ((Attr)localObject1).getNodeValue() };
/* 278 */                 throw new CanonicalizationException("c14n.Canonicalizer.RelativeNamespace", arrayOfObject);
/*     */               }
/*     */ 
/*     */             }
/*     */ 
/*     */           }
/*     */ 
/* 286 */           if (paramNameSpaceSymbTable.addMapping(str, (String)localObject2, (Attr)localObject1))
/*     */           {
/* 288 */             if (C14nHelper.namespaceIsRelative((String)localObject2)) {
/* 289 */               localObject3 = new Object[] { paramElement.getTagName(), str, ((Attr)localObject1).getNodeValue() };
/*     */ 
/* 291 */               throw new CanonicalizationException("c14n.Canonicalizer.RelativeNamespace", (Object[])localObject3);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 297 */     if (j != 0)
/*     */     {
/* 299 */       Attr localAttr = paramElement.getAttributeNodeNS("http://www.w3.org/2000/xmlns/", "xmlns");
/* 300 */       if ((localAttr != null) && (!isVisible(localAttr)))
/*     */       {
/* 303 */         paramNameSpaceSymbTable.addMapping("xmlns", "", getNullNode(localAttr.getOwnerDocument()));
/*     */       }
/*     */ 
/* 306 */       if (paramElement.getNamespaceURI() != null) {
/* 307 */         localObject1 = paramElement.getPrefix();
/* 308 */         if ((localObject1 == null) || (((String)localObject1).length() == 0))
/* 309 */           localSet.add("xmlns");
/*     */         else
/* 311 */           localSet.add(localObject1);
/*     */       }
/*     */       else {
/* 314 */         localSet.add("xmlns");
/*     */       }
/*     */ 
/* 318 */       localObject1 = localSet.iterator();
/* 319 */       while (((Iterator)localObject1).hasNext()) {
/* 320 */         str = (String)((Iterator)localObject1).next();
/* 321 */         localObject2 = paramNameSpaceSymbTable.getMapping(str);
/* 322 */         if (localObject2 != null)
/*     */         {
/* 325 */           localSortedSet.add(localObject2);
/*     */         }
/*     */       }
/*     */     }
/* 329 */     return localSortedSet.iterator();
/*     */   }
/*     */   void circumventBugIfNeeded(XMLSignatureInput paramXMLSignatureInput) throws CanonicalizationException, ParserConfigurationException, IOException, SAXException {
/* 332 */     if ((!paramXMLSignatureInput.isNeedsToBeExpanded()) || (this._inclusiveNSSet.isEmpty()))
/* 333 */       return;
/* 334 */     Document localDocument = null;
/* 335 */     if (paramXMLSignatureInput.getSubNode() != null)
/* 336 */       localDocument = XMLUtils.getOwnerDocument(paramXMLSignatureInput.getSubNode());
/*     */     else {
/* 338 */       localDocument = XMLUtils.getOwnerDocument(paramXMLSignatureInput.getNodeSet());
/*     */     }
/*     */ 
/* 341 */     XMLUtils.circumventBug2650(localDocument);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.c14n.implementations.Canonicalizer20010315Excl
 * JD-Core Version:    0.6.2
 */