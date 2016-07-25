/*    */ package com.sun.xml.internal.ws.fault;
/*    */ 
/*    */ import com.sun.istack.internal.NotNull;
/*    */ import com.sun.istack.internal.Nullable;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import javax.xml.bind.annotation.XmlAnyElement;
/*    */ import org.w3c.dom.Element;
/*    */ import org.w3c.dom.Node;
/*    */ 
/*    */ class DetailType
/*    */ {
/*    */ 
/*    */   @XmlAnyElement
/* 56 */   private final List<Element> detailEntry = new ArrayList();
/*    */ 
/*    */   @NotNull
/*    */   List<Element> getDetails()
/*    */   {
/* 61 */     return this.detailEntry;
/*    */   }
/*    */ 
/*    */   @Nullable
/*    */   Node getDetail(int n)
/*    */   {
/* 69 */     if (n < this.detailEntry.size()) {
/* 70 */       return (Node)this.detailEntry.get(n);
/*    */     }
/* 72 */     return null;
/*    */   }
/*    */ 
/*    */   DetailType(Element detailObject) {
/* 76 */     if (detailObject != null)
/* 77 */       this.detailEntry.add(detailObject);
/*    */   }
/*    */ 
/*    */   DetailType()
/*    */   {
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.fault.DetailType
 * JD-Core Version:    0.6.2
 */