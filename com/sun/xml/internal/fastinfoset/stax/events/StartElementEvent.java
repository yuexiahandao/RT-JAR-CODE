/*     */ package com.sun.xml.internal.fastinfoset.stax.events;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.xml.namespace.NamespaceContext;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.events.Attribute;
/*     */ import javax.xml.stream.events.Namespace;
/*     */ import javax.xml.stream.events.StartElement;
/*     */ 
/*     */ public class StartElementEvent extends EventBase
/*     */   implements StartElement
/*     */ {
/*     */   private Map _attributes;
/*     */   private List _namespaces;
/*  49 */   private NamespaceContext _context = null;
/*     */   private QName _qname;
/*     */ 
/*     */   public void reset()
/*     */   {
/*  53 */     if (this._attributes != null) this._attributes.clear();
/*  54 */     if (this._namespaces != null) this._namespaces.clear();
/*  55 */     if (this._context != null) this._context = null; 
/*     */   }
/*     */ 
/*     */   public StartElementEvent()
/*     */   {
/*  59 */     init();
/*     */   }
/*     */ 
/*     */   public StartElementEvent(String prefix, String uri, String localpart) {
/*  63 */     init();
/*  64 */     if (uri == null) uri = "";
/*  65 */     if (prefix == null) prefix = "";
/*  66 */     this._qname = new QName(uri, localpart, prefix);
/*  67 */     setEventType(1);
/*     */   }
/*     */ 
/*     */   public StartElementEvent(QName qname) {
/*  71 */     init();
/*  72 */     this._qname = qname;
/*     */   }
/*     */ 
/*     */   public StartElementEvent(StartElement startelement) {
/*  76 */     this(startelement.getName());
/*  77 */     addAttributes(startelement.getAttributes());
/*  78 */     addNamespaces(startelement.getNamespaces());
/*     */   }
/*     */ 
/*     */   protected void init() {
/*  82 */     setEventType(1);
/*  83 */     this._attributes = new HashMap();
/*  84 */     this._namespaces = new ArrayList();
/*     */   }
/*     */ 
/*     */   public QName getName()
/*     */   {
/*  93 */     return this._qname;
/*     */   }
/*     */ 
/*     */   public Iterator getAttributes()
/*     */   {
/* 106 */     if (this._attributes != null) {
/* 107 */       Collection coll = this._attributes.values();
/* 108 */       return new ReadIterator(coll.iterator());
/*     */     }
/* 110 */     return EmptyIterator.getInstance();
/*     */   }
/*     */ 
/*     */   public Iterator getNamespaces()
/*     */   {
/* 135 */     if (this._namespaces != null) {
/* 136 */       return new ReadIterator(this._namespaces.iterator());
/*     */     }
/* 138 */     return EmptyIterator.getInstance();
/*     */   }
/*     */ 
/*     */   public Attribute getAttributeByName(QName qname)
/*     */   {
/* 147 */     if (qname == null)
/* 148 */       return null;
/* 149 */     return (Attribute)this._attributes.get(qname);
/*     */   }
/*     */ 
/*     */   public NamespaceContext getNamespaceContext()
/*     */   {
/* 160 */     return this._context;
/*     */   }
/*     */ 
/*     */   public void setName(QName qname)
/*     */   {
/* 165 */     this._qname = qname;
/*     */   }
/*     */ 
/*     */   public String getNamespace()
/*     */   {
/* 170 */     return this._qname.getNamespaceURI();
/*     */   }
/*     */ 
/*     */   public String getNamespaceURI(String prefix)
/*     */   {
/* 182 */     if (getNamespace() != null) return getNamespace();
/*     */ 
/* 184 */     if (this._context != null)
/* 185 */       return this._context.getNamespaceURI(prefix);
/* 186 */     return null;
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 190 */     String s = "<" + nameAsString();
/*     */ 
/* 192 */     if (this._attributes != null) {
/* 193 */       Iterator it = getAttributes();
/* 194 */       Attribute attr = null;
/* 195 */       while (it.hasNext()) {
/* 196 */         attr = (Attribute)it.next();
/* 197 */         s = s + " " + attr.toString();
/*     */       }
/*     */     }
/*     */ 
/* 201 */     if (this._namespaces != null) {
/* 202 */       Iterator it = this._namespaces.iterator();
/* 203 */       Namespace attr = null;
/* 204 */       while (it.hasNext()) {
/* 205 */         attr = (Namespace)it.next();
/* 206 */         s = s + " " + attr.toString();
/*     */       }
/*     */     }
/* 209 */     s = s + ">";
/* 210 */     return s;
/*     */   }
/*     */ 
/*     */   public String nameAsString()
/*     */   {
/* 217 */     if ("".equals(this._qname.getNamespaceURI()))
/* 218 */       return this._qname.getLocalPart();
/* 219 */     if (this._qname.getPrefix() != null) {
/* 220 */       return "['" + this._qname.getNamespaceURI() + "']:" + this._qname.getPrefix() + ":" + this._qname.getLocalPart();
/*     */     }
/* 222 */     return "['" + this._qname.getNamespaceURI() + "']:" + this._qname.getLocalPart();
/*     */   }
/*     */ 
/*     */   public void setNamespaceContext(NamespaceContext context)
/*     */   {
/* 227 */     this._context = context;
/*     */   }
/*     */ 
/*     */   public void addAttribute(Attribute attr) {
/* 231 */     this._attributes.put(attr.getName(), attr);
/*     */   }
/*     */ 
/*     */   public void addAttributes(Iterator attrs) {
/* 235 */     if (attrs != null)
/* 236 */       while (attrs.hasNext()) {
/* 237 */         Attribute attr = (Attribute)attrs.next();
/* 238 */         this._attributes.put(attr.getName(), attr);
/*     */       }
/*     */   }
/*     */ 
/*     */   public void addNamespace(Namespace namespace)
/*     */   {
/* 244 */     if (namespace != null)
/* 245 */       this._namespaces.add(namespace);
/*     */   }
/*     */ 
/*     */   public void addNamespaces(Iterator namespaces)
/*     */   {
/* 250 */     if (namespaces != null)
/* 251 */       while (namespaces.hasNext()) {
/* 252 */         Namespace namespace = (Namespace)namespaces.next();
/* 253 */         this._namespaces.add(namespace);
/*     */       }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.fastinfoset.stax.events.StartElementEvent
 * JD-Core Version:    0.6.2
 */