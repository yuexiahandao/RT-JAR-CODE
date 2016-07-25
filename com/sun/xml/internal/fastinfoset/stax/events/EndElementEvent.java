/*     */ package com.sun.xml.internal.fastinfoset.stax.events;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.events.EndElement;
/*     */ import javax.xml.stream.events.Namespace;
/*     */ 
/*     */ public class EndElementEvent extends EventBase
/*     */   implements EndElement
/*     */ {
/*  43 */   List _namespaces = null;
/*     */   QName _qname;
/*     */ 
/*     */   public void reset()
/*     */   {
/*  47 */     if (this._namespaces != null) this._namespaces.clear(); 
/*     */   }
/*     */ 
/*     */   public EndElementEvent()
/*     */   {
/*  51 */     setEventType(2);
/*     */   }
/*     */ 
/*     */   public EndElementEvent(String prefix, String namespaceURI, String localpart) {
/*  55 */     this._qname = getQName(namespaceURI, localpart, prefix);
/*  56 */     setEventType(2);
/*     */   }
/*     */ 
/*     */   public EndElementEvent(QName qname) {
/*  60 */     this._qname = qname;
/*  61 */     setEventType(2);
/*     */   }
/*     */ 
/*     */   public QName getName()
/*     */   {
/*  69 */     return this._qname;
/*     */   }
/*     */ 
/*     */   public void setName(QName qname) {
/*  73 */     this._qname = qname;
/*     */   }
/*     */ 
/*     */   public Iterator getNamespaces()
/*     */   {
/*  84 */     if (this._namespaces != null)
/*  85 */       return this._namespaces.iterator();
/*  86 */     return EmptyIterator.getInstance();
/*     */   }
/*     */ 
/*     */   public void addNamespace(Namespace namespace) {
/*  90 */     if (this._namespaces == null) {
/*  91 */       this._namespaces = new ArrayList();
/*     */     }
/*  93 */     this._namespaces.add(namespace);
/*     */   }
/*     */ 
/*     */   public String toString() {
/*  97 */     StringBuffer sb = new StringBuffer();
/*  98 */     sb.append("</").append(nameAsString());
/*  99 */     Iterator namespaces = getNamespaces();
/* 100 */     while (namespaces.hasNext()) {
/* 101 */       sb.append(" ").append(namespaces.next().toString());
/*     */     }
/* 103 */     sb.append(">");
/* 104 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   private String nameAsString()
/*     */   {
/* 109 */     if ("".equals(this._qname.getNamespaceURI()))
/* 110 */       return this._qname.getLocalPart();
/* 111 */     if (this._qname.getPrefix() != null) {
/* 112 */       return "['" + this._qname.getNamespaceURI() + "']:" + this._qname.getPrefix() + ":" + this._qname.getLocalPart();
/*     */     }
/* 114 */     return "['" + this._qname.getNamespaceURI() + "']:" + this._qname.getLocalPart();
/*     */   }
/*     */   private QName getQName(String uri, String localPart, String prefix) {
/* 117 */     QName qn = null;
/* 118 */     if ((prefix != null) && (uri != null))
/* 119 */       qn = new QName(uri, localPart, prefix);
/* 120 */     else if ((prefix == null) && (uri != null))
/* 121 */       qn = new QName(uri, localPart);
/* 122 */     else if ((prefix == null) && (uri == null))
/* 123 */       qn = new QName(localPart);
/* 124 */     return qn;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.fastinfoset.stax.events.EndElementEvent
 * JD-Core Version:    0.6.2
 */