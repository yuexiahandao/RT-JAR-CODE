/*     */ package com.sun.xml.internal.txw2;
/*     */ 
/*     */ import com.sun.xml.internal.txw2.output.XmlSerializer;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ 
/*     */ public final class Document
/*     */ {
/*     */   private final XmlSerializer out;
/*  48 */   private boolean started = false;
/*     */ 
/*  56 */   private Content current = null;
/*     */ 
/*  58 */   private final Map<Class, DatatypeWriter> datatypeWriters = new HashMap();
/*     */ 
/*  63 */   private int iota = 1;
/*     */ 
/*  68 */   private final NamespaceSupport inscopeNamespace = new NamespaceSupport();
/*     */   private NamespaceDecl activeNamespaces;
/* 161 */   private final ContentVisitor visitor = new ContentVisitor()
/*     */   {
/*     */     public void onStartDocument()
/*     */     {
/* 166 */       throw new IllegalStateException();
/*     */     }
/*     */ 
/*     */     public void onEndDocument() {
/* 170 */       Document.this.out.endDocument();
/*     */     }
/*     */ 
/*     */     public void onEndTag() {
/* 174 */       Document.this.out.endTag();
/* 175 */       Document.this.inscopeNamespace.popContext();
/* 176 */       Document.this.activeNamespaces = null;
/*     */     }
/*     */ 
/*     */     public void onPcdata(StringBuilder buffer) {
/* 180 */       if (Document.this.activeNamespaces != null)
/* 181 */         buffer = Document.this.fixPrefix(buffer);
/* 182 */       Document.this.out.text(buffer);
/*     */     }
/*     */ 
/*     */     public void onCdata(StringBuilder buffer) {
/* 186 */       if (Document.this.activeNamespaces != null)
/* 187 */         buffer = Document.this.fixPrefix(buffer);
/* 188 */       Document.this.out.cdata(buffer);
/*     */     }
/*     */ 
/*     */     public void onComment(StringBuilder buffer) {
/* 192 */       if (Document.this.activeNamespaces != null)
/* 193 */         buffer = Document.this.fixPrefix(buffer);
/* 194 */       Document.this.out.comment(buffer);
/*     */     }
/*     */ 
/*     */     public void onStartTag(String nsUri, String localName, Attribute attributes, NamespaceDecl namespaces) {
/* 198 */       assert (nsUri != null);
/* 199 */       assert (localName != null);
/*     */ 
/* 201 */       Document.this.activeNamespaces = namespaces;
/*     */ 
/* 203 */       if (!Document.this.started) {
/* 204 */         Document.this.started = true;
/* 205 */         Document.this.out.startDocument();
/*     */       }
/*     */ 
/* 208 */       Document.this.inscopeNamespace.pushContext();
/*     */ 
/* 211 */       for (NamespaceDecl ns = namespaces; ns != null; ns = ns.next) {
/* 212 */         ns.declared = false;
/*     */ 
/* 214 */         if (ns.prefix != null) {
/* 215 */           String uri = Document.this.inscopeNamespace.getURI(ns.prefix);
/* 216 */           if ((uri == null) || (!uri.equals(ns.uri)))
/*     */           {
/* 220 */             Document.this.inscopeNamespace.declarePrefix(ns.prefix, ns.uri);
/* 221 */             ns.declared = true;
/*     */           }
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 227 */       for (NamespaceDecl ns = namespaces; ns != null; ns = ns.next) {
/* 228 */         if (ns.prefix == null) {
/* 229 */           if (Document.this.inscopeNamespace.getURI("").equals(ns.uri)) {
/* 230 */             ns.prefix = "";
/*     */           } else {
/* 232 */             String p = Document.this.inscopeNamespace.getPrefix(ns.uri);
/* 233 */             if (p == null)
/*     */             {
/* 235 */               while (Document.this.inscopeNamespace.getURI(p = Document.this.newPrefix()) != null);
/* 237 */               ns.declared = true;
/* 238 */               Document.this.inscopeNamespace.declarePrefix(p, ns.uri);
/*     */             }
/* 240 */             ns.prefix = p;
/*     */           }
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 246 */       assert (namespaces.uri.equals(nsUri));
/* 247 */       assert (namespaces.prefix != null) : "a prefix must have been all allocated";
/* 248 */       Document.this.out.beginStartTag(nsUri, localName, namespaces.prefix);
/*     */ 
/* 251 */       for (NamespaceDecl ns = namespaces; ns != null; ns = ns.next) {
/* 252 */         if (ns.declared) {
/* 253 */           Document.this.out.writeXmlns(ns.prefix, ns.uri);
/*     */         }
/*     */       }
/*     */ 
/* 257 */       for (Attribute a = attributes; a != null; a = a.next)
/*     */       {
/* 259 */         String prefix;
/*     */         String prefix;
/* 259 */         if (a.nsUri.length() == 0) prefix = ""; else
/* 260 */           prefix = Document.this.inscopeNamespace.getPrefix(a.nsUri);
/* 261 */         Document.this.out.writeAttribute(a.nsUri, a.localName, prefix, Document.this.fixPrefix(a.value));
/*     */       }
/*     */ 
/* 264 */       Document.this.out.endStartTag(nsUri, localName, namespaces.prefix);
/*     */     }
/* 161 */   };
/*     */ 
/* 271 */   private final StringBuilder prefixSeed = new StringBuilder("ns");
/*     */ 
/* 273 */   private int prefixIota = 0;
/*     */   static final char MAGIC = '\000';
/*     */ 
/*     */   Document(XmlSerializer out)
/*     */   {
/*  78 */     this.out = out;
/*  79 */     for (DatatypeWriter dw : DatatypeWriter.BUILTIN)
/*  80 */       this.datatypeWriters.put(dw.getType(), dw);
/*     */   }
/*     */ 
/*     */   void flush() {
/*  84 */     this.out.flush();
/*     */   }
/*     */ 
/*     */   void setFirstContent(Content c) {
/*  88 */     assert (this.current == null);
/*  89 */     this.current = new StartDocument();
/*  90 */     this.current.setNext(this, c);
/*     */   }
/*     */ 
/*     */   public void addDatatypeWriter(DatatypeWriter<?> dw)
/*     */   {
/* 105 */     this.datatypeWriters.put(dw.getType(), dw);
/*     */   }
/*     */ 
/*     */   void run()
/*     */   {
/*     */     while (true)
/*     */     {
/* 113 */       Content next = this.current.getNext();
/* 114 */       if ((next == null) || (!next.isReadyToCommit()))
/* 115 */         return;
/* 116 */       next.accept(this.visitor);
/* 117 */       next.written();
/* 118 */       this.current = next;
/*     */     }
/*     */   }
/*     */ 
/*     */   void writeValue(Object obj, NamespaceResolver nsResolver, StringBuilder buf)
/*     */   {
/* 129 */     if (obj == null) {
/* 130 */       throw new IllegalArgumentException("argument contains null");
/*     */     }
/* 132 */     if ((obj instanceof Object[])) {
/* 133 */       for (Object o : (Object[])obj)
/* 134 */         writeValue(o, nsResolver, buf);
/* 135 */       return;
/*     */     }
/* 137 */     if ((obj instanceof Iterable)) {
/* 138 */       for (Iterator i$ = ((Iterable)obj).iterator(); i$.hasNext(); ) { Object o = i$.next();
/* 139 */         writeValue(o, nsResolver, buf); }
/* 140 */       return;
/*     */     }
/*     */ 
/* 143 */     if (buf.length() > 0) {
/* 144 */       buf.append(' ');
/*     */     }
/* 146 */     Class c = obj.getClass();
/* 147 */     while (c != null) {
/* 148 */       DatatypeWriter dw = (DatatypeWriter)this.datatypeWriters.get(c);
/* 149 */       if (dw != null) {
/* 150 */         dw.print(obj, nsResolver, buf);
/* 151 */         return;
/*     */       }
/* 153 */       c = c.getSuperclass();
/*     */     }
/*     */ 
/* 157 */     buf.append(obj);
/*     */   }
/*     */ 
/*     */   private String newPrefix()
/*     */   {
/* 279 */     this.prefixSeed.setLength(2);
/* 280 */     this.prefixSeed.append(++this.prefixIota);
/* 281 */     return this.prefixSeed.toString();
/*     */   }
/*     */ 
/*     */   private StringBuilder fixPrefix(StringBuilder buf)
/*     */   {
/* 292 */     assert (this.activeNamespaces != null);
/*     */ 
/* 295 */     int len = buf.length();
/* 296 */     for (int i = 0; (i < len) && 
/* 297 */       (buf.charAt(i) != 0); i++);
/* 301 */     if (i == len) {
/* 302 */       return buf;
/*     */     }
/*     */ 
/* 324 */     for (; i < len; 
/* 324 */       goto 227)
/*     */     {
/* 305 */       char uriIdx = buf.charAt(i + 1);
/* 306 */       NamespaceDecl ns = this.activeNamespaces;
/* 307 */       while ((ns != null) && (ns.uniqueId != uriIdx))
/* 308 */         ns = ns.next;
/* 309 */       if (ns == null) {
/* 310 */         throw new IllegalStateException("Unexpected use of prefixes " + buf);
/*     */       }
/* 312 */       int length = 2;
/* 313 */       String prefix = ns.prefix;
/* 314 */       if (prefix.length() == 0) {
/* 315 */         if ((buf.length() <= i + 2) || (buf.charAt(i + 2) != ':'))
/* 316 */           throw new IllegalStateException("Unexpected use of prefixes " + buf);
/* 317 */         length = 3;
/*     */       }
/*     */ 
/* 320 */       buf.replace(i, i + length, prefix);
/* 321 */       len += prefix.length() - length;
/*     */ 
/* 323 */       if ((i < len) && (buf.charAt(i) != 0)) {
/* 324 */         i++;
/*     */       }
/*     */     }
/* 327 */     return buf;
/*     */   }
/*     */ 
/*     */   char assignNewId()
/*     */   {
/* 336 */     return (char)this.iota++;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.txw2.Document
 * JD-Core Version:    0.6.2
 */