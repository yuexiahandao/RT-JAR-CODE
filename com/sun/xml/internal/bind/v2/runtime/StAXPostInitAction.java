/*    */ package com.sun.xml.internal.bind.v2.runtime;
/*    */ 
/*    */ import javax.xml.namespace.NamespaceContext;
/*    */ import javax.xml.stream.XMLEventWriter;
/*    */ import javax.xml.stream.XMLStreamWriter;
/*    */ 
/*    */ final class StAXPostInitAction
/*    */   implements Runnable
/*    */ {
/*    */   private final XMLStreamWriter xsw;
/*    */   private final XMLEventWriter xew;
/*    */   private final NamespaceContext nsc;
/*    */   private final XMLSerializer serializer;
/*    */ 
/*    */   StAXPostInitAction(XMLStreamWriter xsw, XMLSerializer serializer)
/*    */   {
/* 50 */     this.xsw = xsw;
/* 51 */     this.xew = null;
/* 52 */     this.nsc = null;
/* 53 */     this.serializer = serializer;
/*    */   }
/*    */ 
/*    */   StAXPostInitAction(XMLEventWriter xew, XMLSerializer serializer) {
/* 57 */     this.xsw = null;
/* 58 */     this.xew = xew;
/* 59 */     this.nsc = null;
/* 60 */     this.serializer = serializer;
/*    */   }
/*    */ 
/*    */   StAXPostInitAction(NamespaceContext nsc, XMLSerializer serializer) {
/* 64 */     this.xsw = null;
/* 65 */     this.xew = null;
/* 66 */     this.nsc = nsc;
/* 67 */     this.serializer = serializer;
/*    */   }
/*    */ 
/*    */   public void run() {
/* 71 */     NamespaceContext ns = this.nsc;
/* 72 */     if (this.xsw != null) ns = this.xsw.getNamespaceContext();
/* 73 */     if (this.xew != null) ns = this.xew.getNamespaceContext();
/*    */ 
/* 77 */     if (ns == null) {
/* 78 */       return;
/*    */     }
/*    */ 
/* 83 */     for (String nsUri : this.serializer.grammar.nameList.namespaceURIs) {
/* 84 */       String p = ns.getPrefix(nsUri);
/* 85 */       if (p != null)
/* 86 */         this.serializer.addInscopeBinding(nsUri, p);
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.StAXPostInitAction
 * JD-Core Version:    0.6.2
 */