/*    */ package com.sun.org.apache.xalan.internal.xsltc.dom;
/*    */ 
/*    */ import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
/*    */ 
/*    */ public final class FilteredStepIterator extends StepIterator
/*    */ {
/*    */   private Filter _filter;
/*    */ 
/*    */   public FilteredStepIterator(DTMAxisIterator source, DTMAxisIterator iterator, Filter filter)
/*    */   {
/* 42 */     super(source, iterator);
/* 43 */     this._filter = filter;
/*    */   }
/*    */ 
/*    */   public int next()
/*    */   {
/*    */     int node;
/* 48 */     while ((node = super.next()) != -1) {
/* 49 */       if (this._filter.test(node)) {
/* 50 */         return returnNode(node);
/*    */       }
/*    */     }
/* 53 */     return node;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.dom.FilteredStepIterator
 * JD-Core Version:    0.6.2
 */