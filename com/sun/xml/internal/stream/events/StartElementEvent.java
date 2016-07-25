/*     */ package com.sun.xml.internal.stream.events;
/*     */ 
/*     */ import com.sun.xml.internal.stream.util.ReadOnlyIterator;
/*     */ import java.io.IOException;
/*     */ import java.io.Writer;
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
/*     */ public class StartElementEvent extends DummyEvent
/*     */   implements StartElement
/*     */ {
/*     */   private Map fAttributes;
/*     */   private List fNamespaces;
/*  55 */   private NamespaceContext fNamespaceContext = null;
/*     */   private QName fQName;
/*     */ 
/*     */   public StartElementEvent(String prefix, String uri, String localpart)
/*     */   {
/*  59 */     this(new QName(uri, localpart, prefix));
/*     */   }
/*     */ 
/*     */   public StartElementEvent(QName qname) {
/*  63 */     this.fQName = qname;
/*  64 */     init();
/*     */   }
/*     */ 
/*     */   public StartElementEvent(StartElement startelement) {
/*  68 */     this(startelement.getName());
/*  69 */     addAttributes(startelement.getAttributes());
/*  70 */     addNamespaceAttributes(startelement.getNamespaces());
/*     */   }
/*     */ 
/*     */   protected void init() {
/*  74 */     setEventType(1);
/*  75 */     this.fAttributes = new HashMap();
/*  76 */     this.fNamespaces = new ArrayList();
/*     */   }
/*     */ 
/*     */   public QName getName() {
/*  80 */     return this.fQName;
/*     */   }
/*     */ 
/*     */   public void setName(QName qname) {
/*  84 */     this.fQName = qname;
/*     */   }
/*     */ 
/*     */   public Iterator getAttributes() {
/*  88 */     if (this.fAttributes != null) {
/*  89 */       Collection coll = this.fAttributes.values();
/*  90 */       return new ReadOnlyIterator(coll.iterator());
/*     */     }
/*  92 */     return new ReadOnlyIterator();
/*     */   }
/*     */ 
/*     */   public Iterator getNamespaces() {
/*  96 */     if (this.fNamespaces != null) {
/*  97 */       return new ReadOnlyIterator(this.fNamespaces.iterator());
/*     */     }
/*  99 */     return new ReadOnlyIterator();
/*     */   }
/*     */ 
/*     */   public Attribute getAttributeByName(QName qname) {
/* 103 */     if (qname == null)
/* 104 */       return null;
/* 105 */     return (Attribute)this.fAttributes.get(qname);
/*     */   }
/*     */ 
/*     */   public String getNamespace() {
/* 109 */     return this.fQName.getNamespaceURI();
/*     */   }
/*     */ 
/*     */   public String getNamespaceURI(String prefix)
/*     */   {
/* 114 */     if ((getNamespace() != null) && (this.fQName.getPrefix().equals(prefix))) return getNamespace();
/*     */ 
/* 116 */     if (this.fNamespaceContext != null)
/* 117 */       return this.fNamespaceContext.getNamespaceURI(prefix);
/* 118 */     return null;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 130 */     StringBuffer startElement = new StringBuffer();
/*     */ 
/* 133 */     startElement.append("<");
/* 134 */     startElement.append(nameAsString());
/*     */ 
/* 137 */     if (this.fAttributes != null) {
/* 138 */       Iterator it = getAttributes();
/* 139 */       Attribute attr = null;
/* 140 */       while (it.hasNext()) {
/* 141 */         attr = (Attribute)it.next();
/* 142 */         startElement.append(" ");
/* 143 */         startElement.append(attr.toString());
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 148 */     if (this.fNamespaces != null) {
/* 149 */       Iterator it = this.fNamespaces.iterator();
/* 150 */       Namespace attr = null;
/* 151 */       while (it.hasNext()) {
/* 152 */         attr = (Namespace)it.next();
/* 153 */         startElement.append(" ");
/* 154 */         startElement.append(attr.toString());
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 159 */     startElement.append(">");
/*     */ 
/* 162 */     return startElement.toString();
/*     */   }
/*     */ 
/*     */   public String nameAsString()
/*     */   {
/* 169 */     if ("".equals(this.fQName.getNamespaceURI()))
/* 170 */       return this.fQName.getLocalPart();
/* 171 */     if (this.fQName.getPrefix() != null) {
/* 172 */       return "['" + this.fQName.getNamespaceURI() + "']:" + this.fQName.getPrefix() + ":" + this.fQName.getLocalPart();
/*     */     }
/* 174 */     return "['" + this.fQName.getNamespaceURI() + "']:" + this.fQName.getLocalPart();
/*     */   }
/*     */ 
/*     */   public NamespaceContext getNamespaceContext()
/*     */   {
/* 186 */     return this.fNamespaceContext;
/*     */   }
/*     */ 
/*     */   public void setNamespaceContext(NamespaceContext nc) {
/* 190 */     this.fNamespaceContext = nc;
/*     */   }
/*     */ 
/*     */   protected void writeAsEncodedUnicodeEx(Writer writer)
/*     */     throws IOException
/*     */   {
/* 196 */     writer.write(toString());
/*     */   }
/*     */ 
/*     */   void addAttribute(Attribute attr) {
/* 200 */     if (attr.isNamespace())
/* 201 */       this.fNamespaces.add(attr);
/*     */     else
/* 203 */       this.fAttributes.put(attr.getName(), attr);
/*     */   }
/*     */ 
/*     */   void addAttributes(Iterator attrs)
/*     */   {
/* 208 */     if (attrs == null)
/* 209 */       return;
/* 210 */     while (attrs.hasNext()) {
/* 211 */       Attribute attr = (Attribute)attrs.next();
/* 212 */       this.fAttributes.put(attr.getName(), attr);
/*     */     }
/*     */   }
/*     */ 
/*     */   void addNamespaceAttribute(Namespace attr) {
/* 217 */     if (attr == null)
/* 218 */       return;
/* 219 */     this.fNamespaces.add(attr);
/*     */   }
/*     */ 
/*     */   void addNamespaceAttributes(Iterator attrs) {
/* 223 */     if (attrs == null)
/* 224 */       return;
/* 225 */     while (attrs.hasNext()) {
/* 226 */       Namespace attr = (Namespace)attrs.next();
/* 227 */       this.fNamespaces.add(attr);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.stream.events.StartElementEvent
 * JD-Core Version:    0.6.2
 */