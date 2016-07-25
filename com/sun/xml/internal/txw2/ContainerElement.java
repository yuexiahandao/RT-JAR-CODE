/*     */ package com.sun.xml.internal.txw2;
/*     */ 
/*     */ import com.sun.xml.internal.txw2.annotation.XmlAttribute;
/*     */ import com.sun.xml.internal.txw2.annotation.XmlCDATA;
/*     */ import com.sun.xml.internal.txw2.annotation.XmlElement;
/*     */ import com.sun.xml.internal.txw2.annotation.XmlNamespace;
/*     */ import com.sun.xml.internal.txw2.annotation.XmlValue;
/*     */ import java.lang.reflect.InvocationHandler;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Proxy;
/*     */ import javax.xml.namespace.QName;
/*     */ 
/*     */ final class ContainerElement
/*     */   implements InvocationHandler, TypedXmlWriter
/*     */ {
/*     */   final Document document;
/*     */   StartTag startTag;
/*  55 */   final EndTag endTag = new EndTag();
/*     */   private final String nsUri;
/*     */   private Content tail;
/*     */   private ContainerElement prevOpen;
/*     */   private ContainerElement nextOpen;
/*     */   private final ContainerElement parent;
/*     */   private ContainerElement lastOpenChild;
/*     */   private boolean blocked;
/*     */ 
/*     */   public ContainerElement(Document document, ContainerElement parent, String nsUri, String localName)
/*     */   {
/*  85 */     this.parent = parent;
/*  86 */     this.document = document;
/*  87 */     this.nsUri = nsUri;
/*  88 */     this.startTag = new StartTag(this, nsUri, localName);
/*  89 */     this.tail = this.startTag;
/*     */ 
/*  91 */     if (isRoot())
/*  92 */       document.setFirstContent(this.startTag);
/*     */   }
/*     */ 
/*     */   private boolean isRoot() {
/*  96 */     return this.parent == null;
/*     */   }
/*     */ 
/*     */   private boolean isCommitted() {
/* 100 */     return this.tail == null;
/*     */   }
/*     */ 
/*     */   public Document getDocument() {
/* 104 */     return this.document;
/*     */   }
/*     */ 
/*     */   boolean isBlocked() {
/* 108 */     return (this.blocked) && (!isCommitted());
/*     */   }
/*     */ 
/*     */   public void block() {
/* 112 */     this.blocked = true;
/*     */   }
/*     */ 
/*     */   public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
/* 116 */     if ((method.getDeclaringClass() == TypedXmlWriter.class) || (method.getDeclaringClass() == Object.class)) {
/*     */       try
/*     */       {
/* 119 */         return method.invoke(this, args);
/*     */       } catch (InvocationTargetException e) {
/* 121 */         throw e.getTargetException();
/*     */       }
/*     */     }
/*     */ 
/* 125 */     XmlAttribute xa = (XmlAttribute)method.getAnnotation(XmlAttribute.class);
/* 126 */     XmlValue xv = (XmlValue)method.getAnnotation(XmlValue.class);
/* 127 */     XmlElement xe = (XmlElement)method.getAnnotation(XmlElement.class);
/*     */ 
/* 130 */     if (xa != null) {
/* 131 */       if ((xv != null) || (xe != null)) {
/* 132 */         throw new IllegalAnnotationException(method.toString());
/*     */       }
/* 134 */       addAttribute(xa, method, args);
/* 135 */       return proxy;
/*     */     }
/* 137 */     if (xv != null) {
/* 138 */       if (xe != null) {
/* 139 */         throw new IllegalAnnotationException(method.toString());
/*     */       }
/* 141 */       _pcdata(args);
/* 142 */       return proxy;
/*     */     }
/*     */ 
/* 145 */     return addElement(xe, method, args);
/*     */   }
/*     */ 
/*     */   private void addAttribute(XmlAttribute xa, Method method, Object[] args)
/*     */   {
/* 152 */     assert (xa != null);
/*     */ 
/* 154 */     checkStartTag();
/*     */ 
/* 156 */     String localName = xa.value();
/* 157 */     if (xa.value().length() == 0) {
/* 158 */       localName = method.getName();
/*     */     }
/* 160 */     _attribute(xa.ns(), localName, args);
/*     */   }
/*     */ 
/*     */   private void checkStartTag() {
/* 164 */     if (this.startTag == null)
/* 165 */       throw new IllegalStateException("start tag has already been written");
/*     */   }
/*     */ 
/*     */   private Object addElement(XmlElement e, Method method, Object[] args)
/*     */   {
/* 172 */     Class rt = method.getReturnType();
/*     */ 
/* 175 */     String nsUri = "##default";
/* 176 */     String localName = method.getName();
/*     */ 
/* 178 */     if (e != null)
/*     */     {
/* 180 */       if (e.value().length() != 0)
/* 181 */         localName = e.value();
/* 182 */       nsUri = e.ns();
/*     */     }
/*     */ 
/* 185 */     if (nsUri.equals("##default"))
/*     */     {
/* 187 */       Class c = method.getDeclaringClass();
/* 188 */       XmlElement ce = (XmlElement)c.getAnnotation(XmlElement.class);
/* 189 */       if (ce != null) {
/* 190 */         nsUri = ce.ns();
/*     */       }
/*     */ 
/* 193 */       if (nsUri.equals("##default"))
/*     */       {
/* 195 */         nsUri = getNamespace(c.getPackage());
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 200 */     if (rt == Void.TYPE)
/*     */     {
/* 203 */       boolean isCDATA = method.getAnnotation(XmlCDATA.class) != null;
/*     */ 
/* 205 */       StartTag st = new StartTag(this.document, nsUri, localName);
/* 206 */       addChild(st);
/* 207 */       for (Object arg : args)
/*     */       {
/* 209 */         Text text;
/*     */         Text text;
/* 209 */         if (isCDATA) text = new Cdata(this.document, st, arg); else
/* 210 */           text = new Pcdata(this.document, st, arg);
/* 211 */         addChild(text);
/*     */       }
/* 213 */       addChild(new EndTag());
/* 214 */       return null;
/*     */     }
/* 216 */     if (TypedXmlWriter.class.isAssignableFrom(rt))
/*     */     {
/* 218 */       return _element(nsUri, localName, rt);
/*     */     }
/*     */ 
/* 221 */     throw new IllegalSignatureException("Illegal return type: " + rt);
/*     */   }
/*     */ 
/*     */   private String getNamespace(Package pkg)
/*     */   {
/* 228 */     if (pkg == null) return "";
/*     */ 
/* 231 */     XmlNamespace ns = (XmlNamespace)pkg.getAnnotation(XmlNamespace.class);
/*     */     String nsUri;
/*     */     String nsUri;
/* 232 */     if (ns != null)
/* 233 */       nsUri = ns.value();
/*     */     else
/* 235 */       nsUri = "";
/* 236 */     return nsUri;
/*     */   }
/*     */ 
/*     */   private void addChild(Content child)
/*     */   {
/* 243 */     this.tail.setNext(this.document, child);
/* 244 */     this.tail = child;
/*     */   }
/*     */ 
/*     */   public void commit() {
/* 248 */     commit(true);
/*     */   }
/*     */ 
/*     */   public void commit(boolean includingAllPredecessors) {
/* 252 */     _commit(includingAllPredecessors);
/* 253 */     this.document.flush();
/*     */   }
/*     */ 
/*     */   private void _commit(boolean includingAllPredecessors) {
/* 257 */     if (isCommitted()) return;
/*     */ 
/* 259 */     addChild(this.endTag);
/* 260 */     if (isRoot())
/* 261 */       addChild(new EndDocument());
/* 262 */     this.tail = null;
/*     */ 
/* 265 */     if (includingAllPredecessors) {
/* 266 */       for (ContainerElement e = this; e != null; e = e.parent) {
/* 267 */         while (e.prevOpen != null) {
/* 268 */           e.prevOpen._commit(false);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 275 */     while (this.lastOpenChild != null) {
/* 276 */       this.lastOpenChild._commit(false);
/*     */     }
/*     */ 
/* 279 */     if (this.parent != null) {
/* 280 */       if (this.parent.lastOpenChild == this) {
/* 281 */         assert (this.nextOpen == null) : "this must be the last one";
/* 282 */         this.parent.lastOpenChild = this.prevOpen;
/*     */       } else {
/* 284 */         assert (this.nextOpen.prevOpen == this);
/* 285 */         this.nextOpen.prevOpen = this.prevOpen;
/*     */       }
/* 287 */       if (this.prevOpen != null) {
/* 288 */         assert (this.prevOpen.nextOpen == this);
/* 289 */         this.prevOpen.nextOpen = this.nextOpen;
/*     */       }
/*     */     }
/*     */ 
/* 293 */     this.nextOpen = null;
/* 294 */     this.prevOpen = null;
/*     */   }
/*     */ 
/*     */   public void _attribute(String localName, Object value) {
/* 298 */     _attribute("", localName, value);
/*     */   }
/*     */ 
/*     */   public void _attribute(String nsUri, String localName, Object value) {
/* 302 */     checkStartTag();
/* 303 */     this.startTag.addAttribute(nsUri, localName, value);
/*     */   }
/*     */ 
/*     */   public void _attribute(QName attributeName, Object value) {
/* 307 */     _attribute(attributeName.getNamespaceURI(), attributeName.getLocalPart(), value);
/*     */   }
/*     */ 
/*     */   public void _namespace(String uri) {
/* 311 */     _namespace(uri, false);
/*     */   }
/*     */ 
/*     */   public void _namespace(String uri, String prefix) {
/* 315 */     if (prefix == null)
/* 316 */       throw new IllegalArgumentException();
/* 317 */     checkStartTag();
/* 318 */     this.startTag.addNamespaceDecl(uri, prefix, false);
/*     */   }
/*     */ 
/*     */   public void _namespace(String uri, boolean requirePrefix) {
/* 322 */     checkStartTag();
/* 323 */     this.startTag.addNamespaceDecl(uri, null, requirePrefix);
/*     */   }
/*     */ 
/*     */   public void _pcdata(Object value)
/*     */   {
/* 329 */     addChild(new Pcdata(this.document, this.startTag, value));
/*     */   }
/*     */ 
/*     */   public void _cdata(Object value) {
/* 333 */     addChild(new Cdata(this.document, this.startTag, value));
/*     */   }
/*     */ 
/*     */   public void _comment(Object value) throws UnsupportedOperationException {
/* 337 */     addChild(new Comment(this.document, this.startTag, value));
/*     */   }
/*     */ 
/*     */   public <T extends TypedXmlWriter> T _element(String localName, Class<T> contentModel) {
/* 341 */     return _element(this.nsUri, localName, contentModel);
/*     */   }
/*     */ 
/*     */   public <T extends TypedXmlWriter> T _element(QName tagName, Class<T> contentModel) {
/* 345 */     return _element(tagName.getNamespaceURI(), tagName.getLocalPart(), contentModel);
/*     */   }
/*     */ 
/*     */   public <T extends TypedXmlWriter> T _element(Class<T> contentModel) {
/* 349 */     return _element(TXW.getTagName(contentModel), contentModel);
/*     */   }
/*     */ 
/*     */   public <T extends TypedXmlWriter> T _cast(Class<T> facadeType) {
/* 353 */     return (TypedXmlWriter)facadeType.cast(Proxy.newProxyInstance(facadeType.getClassLoader(), new Class[] { facadeType }, this));
/*     */   }
/*     */ 
/*     */   public <T extends TypedXmlWriter> T _element(String nsUri, String localName, Class<T> contentModel) {
/* 357 */     ContainerElement child = new ContainerElement(this.document, this, nsUri, localName);
/* 358 */     addChild(child.startTag);
/* 359 */     this.tail = child.endTag;
/*     */ 
/* 362 */     if (this.lastOpenChild != null) {
/* 363 */       assert (this.lastOpenChild.parent == this);
/*     */ 
/* 365 */       assert (child.prevOpen == null);
/* 366 */       assert (child.nextOpen == null);
/* 367 */       child.prevOpen = this.lastOpenChild;
/* 368 */       assert (this.lastOpenChild.nextOpen == null);
/* 369 */       this.lastOpenChild.nextOpen = child;
/*     */     }
/*     */ 
/* 372 */     this.lastOpenChild = child;
/*     */ 
/* 374 */     return child._cast(contentModel);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.txw2.ContainerElement
 * JD-Core Version:    0.6.2
 */