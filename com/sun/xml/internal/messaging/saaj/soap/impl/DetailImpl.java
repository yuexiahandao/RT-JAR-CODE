/*     */ package com.sun.xml.internal.messaging.saaj.soap.impl;
/*     */ 
/*     */ import com.sun.xml.internal.messaging.saaj.soap.SOAPDocumentImpl;
/*     */ import com.sun.xml.internal.messaging.saaj.soap.name.NameImpl;
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.soap.Detail;
/*     */ import javax.xml.soap.DetailEntry;
/*     */ import javax.xml.soap.Name;
/*     */ import javax.xml.soap.SOAPElement;
/*     */ import javax.xml.soap.SOAPException;
/*     */ import org.w3c.dom.Element;
/*     */ 
/*     */ public abstract class DetailImpl extends FaultElementImpl
/*     */   implements Detail
/*     */ {
/*     */   public DetailImpl(SOAPDocumentImpl ownerDoc, NameImpl detailName)
/*     */   {
/*  41 */     super(ownerDoc, detailName);
/*     */   }
/*     */   protected abstract DetailEntry createDetailEntry(Name paramName);
/*     */ 
/*     */   protected abstract DetailEntry createDetailEntry(QName paramQName);
/*     */ 
/*     */   public DetailEntry addDetailEntry(Name name) throws SOAPException {
/*  48 */     DetailEntry entry = createDetailEntry(name);
/*  49 */     addNode(entry);
/*  50 */     return (DetailEntry)circumventBug5034339(entry);
/*     */   }
/*     */ 
/*     */   public DetailEntry addDetailEntry(QName qname) throws SOAPException {
/*  54 */     DetailEntry entry = createDetailEntry(qname);
/*  55 */     addNode(entry);
/*  56 */     return (DetailEntry)circumventBug5034339(entry);
/*     */   }
/*     */ 
/*     */   protected SOAPElement addElement(Name name) throws SOAPException {
/*  60 */     return addDetailEntry(name);
/*     */   }
/*     */ 
/*     */   protected SOAPElement addElement(QName name) throws SOAPException {
/*  64 */     return addDetailEntry(name);
/*     */   }
/*     */ 
/*     */   protected SOAPElement convertToSoapElement(Element element) {
/*  68 */     if ((element instanceof DetailEntry)) {
/*  69 */       return (SOAPElement)element;
/*     */     }
/*  71 */     DetailEntry detailEntry = createDetailEntry(NameImpl.copyElementName(element));
/*     */ 
/*  73 */     return replaceElementWithSOAPElement(element, (ElementImpl)detailEntry);
/*     */   }
/*     */ 
/*     */   public Iterator getDetailEntries()
/*     */   {
/*  80 */     return new Iterator() {
/*  81 */       Iterator eachNode = DetailImpl.this.getChildElementNodes();
/*  82 */       SOAPElement next = null;
/*  83 */       SOAPElement last = null;
/*     */ 
/*     */       public boolean hasNext() {
/*  86 */         if (this.next == null) {
/*  87 */           while (this.eachNode.hasNext()) {
/*  88 */             this.next = ((SOAPElement)this.eachNode.next());
/*  89 */             if ((this.next instanceof DetailEntry)) {
/*     */               break;
/*     */             }
/*  92 */             this.next = null;
/*     */           }
/*     */         }
/*  95 */         return this.next != null;
/*     */       }
/*     */ 
/*     */       public Object next() {
/*  99 */         if (!hasNext()) {
/* 100 */           throw new NoSuchElementException();
/*     */         }
/* 102 */         this.last = this.next;
/* 103 */         this.next = null;
/* 104 */         return this.last;
/*     */       }
/*     */ 
/*     */       public void remove() {
/* 108 */         if (this.last == null) {
/* 109 */           throw new IllegalStateException();
/*     */         }
/* 111 */         SOAPElement target = this.last;
/* 112 */         DetailImpl.this.removeChild(target);
/* 113 */         this.last = null;
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   protected boolean isStandardFaultElement() {
/* 119 */     return true;
/*     */   }
/*     */ 
/*     */   protected SOAPElement circumventBug5034339(SOAPElement element)
/*     */   {
/* 128 */     Name elementName = element.getElementName();
/* 129 */     if (!isNamespaceQualified(elementName)) {
/* 130 */       String prefix = elementName.getPrefix();
/* 131 */       String defaultNamespace = getNamespaceURI(prefix);
/* 132 */       if (defaultNamespace != null) {
/* 133 */         Name newElementName = NameImpl.create(elementName.getLocalName(), elementName.getPrefix(), defaultNamespace);
/*     */ 
/* 138 */         SOAPElement newElement = createDetailEntry(newElementName);
/* 139 */         replaceChild(newElement, element);
/* 140 */         return newElement;
/*     */       }
/*     */     }
/* 143 */     return element;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.messaging.saaj.soap.impl.DetailImpl
 * JD-Core Version:    0.6.2
 */