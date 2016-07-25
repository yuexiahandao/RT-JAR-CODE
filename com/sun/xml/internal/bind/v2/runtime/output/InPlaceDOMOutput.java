/*    */ package com.sun.xml.internal.bind.v2.runtime.output;
/*    */ 
/*    */ import com.sun.xml.internal.bind.marshaller.SAX2DOMEx;
/*    */ import com.sun.xml.internal.bind.v2.runtime.AssociationMap;
/*    */ import org.w3c.dom.Node;
/*    */ import org.xml.sax.SAXException;
/*    */ 
/*    */ public final class InPlaceDOMOutput extends SAXOutput
/*    */ {
/*    */   private final AssociationMap assoc;
/*    */ 
/*    */   public InPlaceDOMOutput(Node node, AssociationMap assoc)
/*    */   {
/* 42 */     super(new SAX2DOMEx(node));
/* 43 */     this.assoc = assoc;
/* 44 */     assert (assoc != null);
/*    */   }
/*    */ 
/*    */   private SAX2DOMEx getBuilder() {
/* 48 */     return (SAX2DOMEx)this.out;
/*    */   }
/*    */ 
/*    */   public void endStartTag() throws SAXException {
/* 52 */     super.endStartTag();
/*    */ 
/* 54 */     Object op = this.nsContext.getCurrent().getOuterPeer();
/* 55 */     if (op != null) {
/* 56 */       this.assoc.addOuter(getBuilder().getCurrentElement(), op);
/*    */     }
/* 58 */     Object ip = this.nsContext.getCurrent().getInnerPeer();
/* 59 */     if (ip != null)
/* 60 */       this.assoc.addInner(getBuilder().getCurrentElement(), ip);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.output.InPlaceDOMOutput
 * JD-Core Version:    0.6.2
 */