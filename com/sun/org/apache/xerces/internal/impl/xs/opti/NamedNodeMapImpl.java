/*    */ package com.sun.org.apache.xerces.internal.impl.xs.opti;
/*    */ 
/*    */ import org.w3c.dom.Attr;
/*    */ import org.w3c.dom.DOMException;
/*    */ import org.w3c.dom.NamedNodeMap;
/*    */ import org.w3c.dom.Node;
/*    */ 
/*    */ public class NamedNodeMapImpl
/*    */   implements NamedNodeMap
/*    */ {
/*    */   Attr[] attrs;
/*    */ 
/*    */   public NamedNodeMapImpl(Attr[] attrs)
/*    */   {
/* 41 */     this.attrs = attrs;
/*    */   }
/*    */ 
/*    */   public Node getNamedItem(String name) {
/* 45 */     for (int i = 0; i < this.attrs.length; i++) {
/* 46 */       if (this.attrs[i].getName().equals(name)) {
/* 47 */         return this.attrs[i];
/*    */       }
/*    */     }
/* 50 */     return null;
/*    */   }
/*    */ 
/*    */   public Node item(int index) {
/* 54 */     if ((index < 0) && (index > getLength())) {
/* 55 */       return null;
/*    */     }
/* 57 */     return this.attrs[index];
/*    */   }
/*    */ 
/*    */   public int getLength() {
/* 61 */     return this.attrs.length;
/*    */   }
/*    */ 
/*    */   public Node getNamedItemNS(String namespaceURI, String localName) {
/* 65 */     for (int i = 0; i < this.attrs.length; i++) {
/* 66 */       if ((this.attrs[i].getName().equals(localName)) && (this.attrs[i].getNamespaceURI().equals(namespaceURI))) {
/* 67 */         return this.attrs[i];
/*    */       }
/*    */     }
/* 70 */     return null;
/*    */   }
/*    */ 
/*    */   public Node setNamedItemNS(Node arg) throws DOMException {
/* 74 */     throw new DOMException((short)9, "Method not supported");
/*    */   }
/*    */ 
/*    */   public Node setNamedItem(Node arg) throws DOMException {
/* 78 */     throw new DOMException((short)9, "Method not supported");
/*    */   }
/*    */ 
/*    */   public Node removeNamedItem(String name) throws DOMException {
/* 82 */     throw new DOMException((short)9, "Method not supported");
/*    */   }
/*    */ 
/*    */   public Node removeNamedItemNS(String namespaceURI, String localName) throws DOMException {
/* 86 */     throw new DOMException((short)9, "Method not supported");
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.xs.opti.NamedNodeMapImpl
 * JD-Core Version:    0.6.2
 */