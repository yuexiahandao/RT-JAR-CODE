/*    */ package org.jcp.xml.dsig.internal.dom;
/*    */ 
/*    */ import com.sun.org.apache.xml.internal.security.signature.NodeFilter;
/*    */ import com.sun.org.apache.xml.internal.security.signature.XMLSignatureInput;
/*    */ import com.sun.org.apache.xml.internal.security.utils.XMLUtils;
/*    */ import java.util.Collections;
/*    */ import java.util.Iterator;
/*    */ import java.util.LinkedHashSet;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import javax.xml.crypto.NodeSetData;
/*    */ import org.w3c.dom.Node;
/*    */ 
/*    */ public class ApacheNodeSetData
/*    */   implements ApacheData, NodeSetData
/*    */ {
/*    */   private XMLSignatureInput xi;
/*    */ 
/*    */   public ApacheNodeSetData(XMLSignatureInput paramXMLSignatureInput)
/*    */   {
/* 46 */     this.xi = paramXMLSignatureInput;
/*    */   }
/*    */ 
/*    */   public Iterator iterator()
/*    */   {
/* 51 */     if ((this.xi.getNodeFilters() != null) && (!this.xi.getNodeFilters().isEmpty())) {
/* 52 */       return Collections.unmodifiableSet(getNodeSet(this.xi.getNodeFilters())).iterator();
/*    */     }
/*    */     try
/*    */     {
/* 56 */       return Collections.unmodifiableSet(this.xi.getNodeSet()).iterator();
/*    */     }
/*    */     catch (Exception localException) {
/* 59 */       throw new RuntimeException("unrecoverable error retrieving nodeset", localException);
/*    */     }
/*    */   }
/*    */ 
/*    */   public XMLSignatureInput getXMLSignatureInput()
/*    */   {
/* 65 */     return this.xi;
/*    */   }
/*    */ 
/*    */   private Set getNodeSet(List paramList) {
/* 69 */     if (this.xi.isNeedsToBeExpanded()) {
/* 70 */       XMLUtils.circumventBug2650(XMLUtils.getOwnerDocument(this.xi.getSubNode()));
/*    */     }
/*    */ 
/* 74 */     LinkedHashSet localLinkedHashSet1 = new LinkedHashSet();
/* 75 */     XMLUtils.getSet(this.xi.getSubNode(), localLinkedHashSet1, null, !this.xi.isExcludeComments());
/*    */ 
/* 77 */     LinkedHashSet localLinkedHashSet2 = new LinkedHashSet();
/* 78 */     Iterator localIterator1 = localLinkedHashSet1.iterator();
/* 79 */     while (localIterator1.hasNext()) {
/* 80 */       Node localNode = (Node)localIterator1.next();
/* 81 */       Iterator localIterator2 = paramList.iterator();
/* 82 */       int i = 0;
/* 83 */       while ((localIterator2.hasNext()) && (i == 0)) {
/* 84 */         NodeFilter localNodeFilter = (NodeFilter)localIterator2.next();
/* 85 */         if (localNodeFilter.isNodeInclude(localNode) != 1) {
/* 86 */           i = 1;
/*    */         }
/*    */       }
/* 89 */       if (i == 0) {
/* 90 */         localLinkedHashSet2.add(localNode);
/*    */       }
/*    */     }
/* 93 */     return localLinkedHashSet2;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.jcp.xml.dsig.internal.dom.ApacheNodeSetData
 * JD-Core Version:    0.6.2
 */