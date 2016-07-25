/*     */ package com.sun.xml.internal.bind.v2.runtime.output;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.istack.internal.Nullable;
/*     */ import com.sun.xml.internal.bind.marshaller.NamespacePrefixMapper;
/*     */ import com.sun.xml.internal.bind.v2.runtime.Name;
/*     */ import com.sun.xml.internal.bind.v2.runtime.NameList;
/*     */ import com.sun.xml.internal.bind.v2.runtime.NamespaceContext2;
/*     */ import com.sun.xml.internal.bind.v2.runtime.XMLSerializer;
/*     */ import java.io.IOException;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public final class NamespaceContextImpl
/*     */   implements NamespaceContext2
/*     */ {
/*     */   private final XMLSerializer owner;
/*  57 */   private String[] prefixes = new String[4];
/*  58 */   private String[] nsUris = new String[4];
/*     */   private int size;
/*     */   private Element current;
/*     */   private final Element top;
/* 103 */   private NamespacePrefixMapper prefixMapper = defaultNamespacePrefixMapper;
/*     */   public boolean collectionMode;
/* 548 */   private static final NamespacePrefixMapper defaultNamespacePrefixMapper = new NamespacePrefixMapper() {
/*     */     public String getPreferredPrefix(String namespaceUri, String suggestion, boolean requirePrefix) {
/* 550 */       if (namespaceUri.equals("http://www.w3.org/2001/XMLSchema-instance"))
/* 551 */         return "xsi";
/* 552 */       if (namespaceUri.equals("http://www.w3.org/2001/XMLSchema"))
/* 553 */         return "xs";
/* 554 */       if (namespaceUri.equals("http://www.w3.org/2005/05/xmlmime"))
/* 555 */         return "xmime";
/* 556 */       return suggestion;
/*     */     }
/* 548 */   };
/*     */ 
/*     */   public NamespaceContextImpl(XMLSerializer owner)
/*     */   {
/* 112 */     this.owner = owner;
/*     */ 
/* 114 */     this.current = (this.top = new Element(this, null, null));
/*     */ 
/* 116 */     put("http://www.w3.org/XML/1998/namespace", "xml");
/*     */   }
/*     */ 
/*     */   public void setPrefixMapper(NamespacePrefixMapper mapper) {
/* 120 */     if (mapper == null)
/* 121 */       mapper = defaultNamespacePrefixMapper;
/* 122 */     this.prefixMapper = mapper;
/*     */   }
/*     */ 
/*     */   public NamespacePrefixMapper getPrefixMapper() {
/* 126 */     return this.prefixMapper;
/*     */   }
/*     */ 
/*     */   public void reset() {
/* 130 */     this.current = this.top;
/* 131 */     this.size = 1;
/* 132 */     this.collectionMode = false;
/*     */   }
/*     */ 
/*     */   public int declareNsUri(String uri, String preferedPrefix, boolean requirePrefix)
/*     */   {
/* 140 */     preferedPrefix = this.prefixMapper.getPreferredPrefix(uri, preferedPrefix, requirePrefix);
/*     */ 
/* 142 */     if (uri.length() == 0) {
/* 143 */       for (int i = this.size - 1; i >= 0; i--) {
/* 144 */         if (this.nsUris[i].length() == 0)
/* 145 */           return i;
/* 146 */         if (this.prefixes[i].length() == 0)
/*     */         {
/* 149 */           assert ((this.current.defaultPrefixIndex == -1) && (this.current.oldDefaultNamespaceUriIndex == -1));
/*     */ 
/* 151 */           String oldUri = this.nsUris[i];
/* 152 */           String[] knownURIs = this.owner.nameList.namespaceURIs;
/*     */ 
/* 154 */           if (this.current.baseIndex <= i)
/*     */           {
/* 157 */             this.nsUris[i] = "";
/*     */ 
/* 159 */             int subst = put(oldUri, null);
/*     */ 
/* 162 */             for (int j = knownURIs.length - 1; j >= 0; j--) {
/* 163 */               if (knownURIs[j].equals(oldUri)) {
/* 164 */                 this.owner.knownUri2prefixIndexMap[j] = subst;
/* 165 */                 break;
/*     */               }
/*     */             }
/* 168 */             if (this.current.elementLocalName != null) {
/* 169 */               this.current.setTagName(subst, this.current.elementLocalName, this.current.getOuterPeer());
/*     */             }
/* 171 */             return i;
/*     */           }
/*     */ 
/* 176 */           for (int j = knownURIs.length - 1; j >= 0; j--) {
/* 177 */             if (knownURIs[j].equals(oldUri)) {
/* 178 */               this.current.defaultPrefixIndex = i;
/* 179 */               this.current.oldDefaultNamespaceUriIndex = j;
/*     */ 
/* 183 */               this.owner.knownUri2prefixIndexMap[j] = this.size;
/* 184 */               break;
/*     */             }
/*     */           }
/* 187 */           if (this.current.elementLocalName != null) {
/* 188 */             this.current.setTagName(this.size, this.current.elementLocalName, this.current.getOuterPeer());
/*     */           }
/*     */ 
/* 191 */           put(this.nsUris[i], null);
/* 192 */           return put("", "");
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 198 */       return put("", "");
/*     */     }
/*     */ 
/* 201 */     for (int i = this.size - 1; i >= 0; i--) {
/* 202 */       String p = this.prefixes[i];
/* 203 */       if ((this.nsUris[i].equals(uri)) && (
/* 204 */         (!requirePrefix) || (p.length() > 0))) {
/* 205 */         return i;
/*     */       }
/*     */ 
/* 208 */       if (p.equals(preferedPrefix))
/*     */       {
/* 210 */         preferedPrefix = null;
/*     */       }
/*     */     }
/*     */ 
/* 214 */     if ((preferedPrefix == null) && (requirePrefix))
/*     */     {
/* 217 */       preferedPrefix = makeUniquePrefix();
/*     */     }
/*     */ 
/* 221 */     return put(uri, preferedPrefix);
/*     */   }
/*     */ 
/*     */   public int force(@NotNull String uri, @NotNull String prefix)
/*     */   {
/* 228 */     for (int i = this.size - 1; i >= 0; i--) {
/* 229 */       if (this.prefixes[i].equals(prefix)) {
/* 230 */         if (!this.nsUris[i].equals(uri)) break;
/* 231 */         return i;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 238 */     return put(uri, prefix);
/*     */   }
/*     */ 
/*     */   public int put(@NotNull String uri, @Nullable String prefix)
/*     */   {
/* 254 */     if (this.size == this.nsUris.length)
/*     */     {
/* 256 */       String[] u = new String[this.nsUris.length * 2];
/* 257 */       String[] p = new String[this.prefixes.length * 2];
/* 258 */       System.arraycopy(this.nsUris, 0, u, 0, this.nsUris.length);
/* 259 */       System.arraycopy(this.prefixes, 0, p, 0, this.prefixes.length);
/* 260 */       this.nsUris = u;
/* 261 */       this.prefixes = p;
/*     */     }
/* 263 */     if (prefix == null) {
/* 264 */       if (this.size == 1) {
/* 265 */         prefix = "";
/*     */       }
/*     */       else {
/* 268 */         prefix = makeUniquePrefix();
/*     */       }
/*     */     }
/* 271 */     this.nsUris[this.size] = uri;
/* 272 */     this.prefixes[this.size] = prefix;
/*     */ 
/* 274 */     return this.size++;
/*     */   }
/*     */ 
/*     */   private String makeUniquePrefix()
/*     */   {
/* 279 */     String prefix = 5 + "ns" + this.size;
/* 280 */     while (getNamespaceURI(prefix) != null) {
/* 281 */       prefix = prefix + '_';
/*     */     }
/* 283 */     return prefix;
/*     */   }
/*     */ 
/*     */   public Element getCurrent()
/*     */   {
/* 288 */     return this.current;
/*     */   }
/*     */ 
/*     */   public int getPrefixIndex(String uri)
/*     */   {
/* 296 */     for (int i = this.size - 1; i >= 0; i--) {
/* 297 */       if (this.nsUris[i].equals(uri))
/* 298 */         return i;
/*     */     }
/* 300 */     throw new IllegalStateException();
/*     */   }
/*     */ 
/*     */   public String getPrefix(int prefixIndex)
/*     */   {
/* 309 */     return this.prefixes[prefixIndex];
/*     */   }
/*     */ 
/*     */   public String getNamespaceURI(int prefixIndex) {
/* 313 */     return this.nsUris[prefixIndex];
/*     */   }
/*     */ 
/*     */   public String getNamespaceURI(String prefix)
/*     */   {
/* 323 */     for (int i = this.size - 1; i >= 0; i--)
/* 324 */       if (this.prefixes[i].equals(prefix))
/* 325 */         return this.nsUris[i];
/* 326 */     return null;
/*     */   }
/*     */ 
/*     */   public String getPrefix(String uri)
/*     */   {
/* 334 */     if (this.collectionMode) {
/* 335 */       return declareNamespace(uri, null, false);
/*     */     }
/* 337 */     for (int i = this.size - 1; i >= 0; i--)
/* 338 */       if (this.nsUris[i].equals(uri))
/* 339 */         return this.prefixes[i];
/* 340 */     return null;
/*     */   }
/*     */ 
/*     */   public Iterator<String> getPrefixes(String uri)
/*     */   {
/* 345 */     String prefix = getPrefix(uri);
/* 346 */     if (prefix == null) {
/* 347 */       return Collections.emptySet().iterator();
/*     */     }
/* 349 */     return Collections.singleton(uri).iterator();
/*     */   }
/*     */ 
/*     */   public String declareNamespace(String namespaceUri, String preferedPrefix, boolean requirePrefix) {
/* 353 */     int idx = declareNsUri(namespaceUri, preferedPrefix, requirePrefix);
/* 354 */     return getPrefix(idx);
/*     */   }
/*     */ 
/*     */   public int count()
/*     */   {
/* 361 */     return this.size;
/*     */   }
/*     */ 
/*     */   public final class Element
/*     */   {
/*     */     public final NamespaceContextImpl context;
/*     */     private final Element prev;
/*     */     private Element next;
/*     */     private int oldDefaultNamespaceUriIndex;
/*     */     private int defaultPrefixIndex;
/*     */     private int baseIndex;
/*     */     private final int depth;
/*     */     private int elementNamePrefix;
/*     */     private String elementLocalName;
/*     */     private Name elementName;
/*     */     private Object outerPeer;
/*     */     private Object innerPeer;
/*     */ 
/*     */     private Element(NamespaceContextImpl context, Element prev)
/*     */     {
/* 426 */       this.context = context;
/* 427 */       this.prev = prev;
/* 428 */       this.depth = (prev == null ? 0 : prev.depth + 1);
/*     */     }
/*     */ 
/*     */     public boolean isRootElement()
/*     */     {
/* 436 */       return this.depth == 1;
/*     */     }
/*     */ 
/*     */     public Element push() {
/* 440 */       if (this.next == null)
/* 441 */         this.next = new Element(NamespaceContextImpl.this, this.context, this);
/* 442 */       this.next.onPushed();
/* 443 */       return this.next;
/*     */     }
/*     */ 
/*     */     public Element pop() {
/* 447 */       if (this.oldDefaultNamespaceUriIndex >= 0)
/*     */       {
/* 449 */         this.context.owner.knownUri2prefixIndexMap[this.oldDefaultNamespaceUriIndex] = this.defaultPrefixIndex;
/*     */       }
/* 451 */       this.context.size = this.baseIndex;
/* 452 */       this.context.current = this.prev;
/*     */ 
/* 454 */       this.outerPeer = (this.innerPeer = null);
/* 455 */       return this.prev;
/*     */     }
/*     */ 
/*     */     private void onPushed() {
/* 459 */       this.oldDefaultNamespaceUriIndex = (this.defaultPrefixIndex = -1);
/* 460 */       this.baseIndex = this.context.size;
/* 461 */       this.context.current = this;
/*     */     }
/*     */ 
/*     */     public void setTagName(int prefix, String localName, Object outerPeer) {
/* 465 */       assert (localName != null);
/* 466 */       this.elementNamePrefix = prefix;
/* 467 */       this.elementLocalName = localName;
/* 468 */       this.elementName = null;
/* 469 */       this.outerPeer = outerPeer;
/*     */     }
/*     */ 
/*     */     public void setTagName(Name tagName, Object outerPeer) {
/* 473 */       assert (tagName != null);
/* 474 */       this.elementName = tagName;
/* 475 */       this.outerPeer = outerPeer;
/*     */     }
/*     */ 
/*     */     public void startElement(XmlOutput out, Object innerPeer) throws IOException, XMLStreamException {
/* 479 */       this.innerPeer = innerPeer;
/* 480 */       if (this.elementName != null)
/* 481 */         out.beginStartTag(this.elementName);
/*     */       else
/* 483 */         out.beginStartTag(this.elementNamePrefix, this.elementLocalName);
/*     */     }
/*     */ 
/*     */     public void endElement(XmlOutput out) throws IOException, SAXException, XMLStreamException
/*     */     {
/* 488 */       if (this.elementName != null) {
/* 489 */         out.endTag(this.elementName);
/* 490 */         this.elementName = null;
/*     */       } else {
/* 492 */         out.endTag(this.elementNamePrefix, this.elementLocalName);
/*     */       }
/*     */     }
/*     */ 
/*     */     public final int count()
/*     */     {
/* 500 */       return this.context.size - this.baseIndex;
/*     */     }
/*     */ 
/*     */     public final String getPrefix(int idx)
/*     */     {
/* 510 */       return this.context.prefixes[(this.baseIndex + idx)];
/*     */     }
/*     */ 
/*     */     public final String getNsUri(int idx)
/*     */     {
/* 520 */       return this.context.nsUris[(this.baseIndex + idx)];
/*     */     }
/*     */ 
/*     */     public int getBase() {
/* 524 */       return this.baseIndex;
/*     */     }
/*     */ 
/*     */     public Object getOuterPeer() {
/* 528 */       return this.outerPeer;
/*     */     }
/*     */ 
/*     */     public Object getInnerPeer() {
/* 532 */       return this.innerPeer;
/*     */     }
/*     */ 
/*     */     public Element getParent()
/*     */     {
/* 539 */       return this.prev;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.output.NamespaceContextImpl
 * JD-Core Version:    0.6.2
 */