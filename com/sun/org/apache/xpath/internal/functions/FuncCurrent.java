/*    */ package com.sun.org.apache.xpath.internal.functions;
/*    */ 
/*    */ import com.sun.org.apache.xalan.internal.res.XSLMessages;
/*    */ import com.sun.org.apache.xpath.internal.XPathContext;
/*    */ import com.sun.org.apache.xpath.internal.axes.LocPathIterator;
/*    */ import com.sun.org.apache.xpath.internal.axes.PredicatedNodeTest;
/*    */ import com.sun.org.apache.xpath.internal.axes.SubContextList;
/*    */ import com.sun.org.apache.xpath.internal.objects.XNodeSet;
/*    */ import com.sun.org.apache.xpath.internal.objects.XObject;
/*    */ import com.sun.org.apache.xpath.internal.patterns.StepPattern;
/*    */ import java.util.Vector;
/*    */ import javax.xml.transform.TransformerException;
/*    */ 
/*    */ public class FuncCurrent extends Function
/*    */ {
/*    */   static final long serialVersionUID = 5715316804877715008L;
/*    */ 
/*    */   public XObject execute(XPathContext xctxt)
/*    */     throws TransformerException
/*    */   {
/* 57 */     SubContextList subContextList = xctxt.getCurrentNodeList();
/* 58 */     int currentNode = -1;
/*    */ 
/* 60 */     if (null != subContextList) {
/* 61 */       if ((subContextList instanceof PredicatedNodeTest)) {
/* 62 */         LocPathIterator iter = ((PredicatedNodeTest)subContextList).getLocPathIterator();
/*    */ 
/* 64 */         currentNode = iter.getCurrentContextNode();
/* 65 */       } else if ((subContextList instanceof StepPattern)) {
/* 66 */         throw new RuntimeException(XSLMessages.createMessage("ER_PROCESSOR_ERROR", null));
/*    */       }
/*    */     }
/*    */     else
/*    */     {
/* 71 */       currentNode = xctxt.getContextNode();
/*    */     }
/* 73 */     return new XNodeSet(currentNode, xctxt.getDTMManager());
/*    */   }
/*    */ 
/*    */   public void fixupVariables(Vector vars, int globalsSize)
/*    */   {
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.functions.FuncCurrent
 * JD-Core Version:    0.6.2
 */