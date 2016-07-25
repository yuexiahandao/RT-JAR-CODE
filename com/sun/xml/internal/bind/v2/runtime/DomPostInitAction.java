/*    */ package com.sun.xml.internal.bind.v2.runtime;
/*    */ 
/*    */ import java.util.HashSet;
/*    */ import java.util.Set;
/*    */ import org.w3c.dom.Attr;
/*    */ import org.w3c.dom.NamedNodeMap;
/*    */ import org.w3c.dom.Node;
/*    */ 
/*    */ final class DomPostInitAction
/*    */   implements Runnable
/*    */ {
/*    */   private final Node node;
/*    */   private final XMLSerializer serializer;
/*    */ 
/*    */   DomPostInitAction(Node node, XMLSerializer serializer)
/*    */   {
/* 51 */     this.node = node;
/* 52 */     this.serializer = serializer;
/*    */   }
/*    */ 
/*    */   public void run()
/*    */   {
/* 57 */     Set declaredPrefixes = new HashSet();
/* 58 */     for (Node n = this.node; (n != null) && (n.getNodeType() == 1); n = n.getParentNode()) {
/* 59 */       NamedNodeMap atts = n.getAttributes();
/* 60 */       if (atts != null)
/* 61 */         for (int i = 0; i < atts.getLength(); i++) {
/* 62 */           Attr a = (Attr)atts.item(i);
/* 63 */           String nsUri = a.getNamespaceURI();
/* 64 */           if ((nsUri != null) && (nsUri.equals("http://www.w3.org/2000/xmlns/")))
/*    */           {
/* 66 */             String prefix = a.getLocalName();
/* 67 */             if (prefix != null)
/*    */             {
/* 69 */               if (prefix.equals("xmlns")) {
/* 70 */                 prefix = "";
/*    */               }
/* 72 */               String value = a.getValue();
/* 73 */               if (value != null)
/*    */               {
/* 75 */                 if (declaredPrefixes.add(prefix))
/* 76 */                   this.serializer.addInscopeBinding(value, prefix);
/*    */               }
/*    */             }
/*    */           }
/*    */         }
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.DomPostInitAction
 * JD-Core Version:    0.6.2
 */