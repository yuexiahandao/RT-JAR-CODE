/*     */ package com.sun.xml.internal.stream.events;
/*     */ 
/*     */ import com.sun.xml.internal.stream.util.ReadOnlyIterator;
/*     */ import java.io.IOException;
/*     */ import java.io.Writer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.events.EndElement;
/*     */ import javax.xml.stream.events.Namespace;
/*     */ 
/*     */ public class EndElementEvent extends DummyEvent
/*     */   implements EndElement
/*     */ {
/*  48 */   List fNamespaces = null;
/*     */   QName fQName;
/*     */ 
/*     */   public EndElementEvent()
/*     */   {
/*  52 */     init();
/*     */   }
/*     */ 
/*     */   protected void init() {
/*  56 */     setEventType(2);
/*  57 */     this.fNamespaces = new ArrayList();
/*     */   }
/*     */ 
/*     */   public EndElementEvent(String prefix, String uri, String localpart)
/*     */   {
/*  62 */     this(new QName(uri, localpart, prefix));
/*     */   }
/*     */ 
/*     */   public EndElementEvent(QName qname) {
/*  66 */     this.fQName = qname;
/*  67 */     init();
/*     */   }
/*     */ 
/*     */   public QName getName() {
/*  71 */     return this.fQName;
/*     */   }
/*     */ 
/*     */   public void setName(QName qname) {
/*  75 */     this.fQName = qname;
/*     */   }
/*     */ 
/*     */   protected void writeAsEncodedUnicodeEx(Writer writer)
/*     */     throws IOException
/*     */   {
/*  81 */     writer.write("</");
/*  82 */     String prefix = this.fQName.getPrefix();
/*  83 */     if ((prefix != null) && (prefix.length() > 0)) {
/*  84 */       writer.write(prefix);
/*  85 */       writer.write(58);
/*     */     }
/*  87 */     writer.write(this.fQName.getLocalPart());
/*  88 */     writer.write(62);
/*     */   }
/*     */ 
/*     */   public Iterator getNamespaces()
/*     */   {
/*  98 */     if (this.fNamespaces != null)
/*  99 */       this.fNamespaces.iterator();
/* 100 */     return new ReadOnlyIterator();
/*     */   }
/*     */ 
/*     */   void addNamespace(Namespace attr) {
/* 104 */     if (attr != null)
/* 105 */       this.fNamespaces.add(attr);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 110 */     String s = "</" + nameAsString();
/* 111 */     s = s + ">";
/* 112 */     return s;
/*     */   }
/*     */ 
/*     */   public String nameAsString() {
/* 116 */     if ("".equals(this.fQName.getNamespaceURI()))
/* 117 */       return this.fQName.getLocalPart();
/* 118 */     if (this.fQName.getPrefix() != null) {
/* 119 */       return "['" + this.fQName.getNamespaceURI() + "']:" + this.fQName.getPrefix() + ":" + this.fQName.getLocalPart();
/*     */     }
/* 121 */     return "['" + this.fQName.getNamespaceURI() + "']:" + this.fQName.getLocalPart();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.stream.events.EndElementEvent
 * JD-Core Version:    0.6.2
 */