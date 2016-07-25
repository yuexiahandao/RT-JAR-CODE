/*    */ package com.sun.org.apache.xpath.internal.operations;
/*    */ 
/*    */ import com.sun.org.apache.xml.internal.dtm.DTM;
/*    */ import com.sun.org.apache.xml.internal.dtm.DTMManager;
/*    */ import com.sun.org.apache.xpath.internal.Expression;
/*    */ import com.sun.org.apache.xpath.internal.XPathContext;
/*    */ import com.sun.org.apache.xpath.internal.objects.XNodeSet;
/*    */ import com.sun.org.apache.xpath.internal.objects.XObject;
/*    */ import javax.xml.transform.TransformerException;
/*    */ 
/*    */ public class VariableSafeAbsRef extends Variable
/*    */ {
/*    */   static final long serialVersionUID = -9174661990819967452L;
/*    */ 
/*    */   public XObject execute(XPathContext xctxt, boolean destructiveOK)
/*    */     throws TransformerException
/*    */   {
/* 63 */     XNodeSet xns = (XNodeSet)super.execute(xctxt, destructiveOK);
/* 64 */     DTMManager dtmMgr = xctxt.getDTMManager();
/* 65 */     int context = xctxt.getContextNode();
/* 66 */     if (dtmMgr.getDTM(xns.getRoot()).getDocument() != dtmMgr.getDTM(context).getDocument())
/*    */     {
/* 69 */       Expression expr = (Expression)xns.getContainedIter();
/* 70 */       xns = (XNodeSet)expr.asIterator(xctxt, context);
/*    */     }
/* 72 */     return xns;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.operations.VariableSafeAbsRef
 * JD-Core Version:    0.6.2
 */