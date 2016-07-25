/*    */ package com.sun.org.apache.xpath.internal.functions;
/*    */ 
/*    */ import com.sun.org.apache.xml.internal.dtm.DTMIterator;
/*    */ import com.sun.org.apache.xpath.internal.XPathContext;
/*    */ import com.sun.org.apache.xpath.internal.axes.SubContextList;
/*    */ import com.sun.org.apache.xpath.internal.compiler.Compiler;
/*    */ import com.sun.org.apache.xpath.internal.objects.XNumber;
/*    */ import com.sun.org.apache.xpath.internal.objects.XObject;
/*    */ import java.util.Vector;
/*    */ import javax.xml.transform.TransformerException;
/*    */ 
/*    */ public class FuncLast extends Function
/*    */ {
/*    */   static final long serialVersionUID = 9205812403085432943L;
/*    */   private boolean m_isTopLevel;
/*    */ 
/*    */   public void postCompileStep(Compiler compiler)
/*    */   {
/* 49 */     this.m_isTopLevel = (compiler.getLocationPathDepth() == -1);
/*    */   }
/*    */ 
/*    */   public int getCountOfContextNodeList(XPathContext xctxt)
/*    */     throws TransformerException
/*    */   {
/* 67 */     SubContextList iter = this.m_isTopLevel ? null : xctxt.getSubContextList();
/*    */ 
/* 70 */     if (null != iter) {
/* 71 */       return iter.getLastPos(xctxt);
/*    */     }
/* 73 */     DTMIterator cnl = xctxt.getContextNodeList();
/*    */     int count;
/*    */     int count;
/* 75 */     if (null != cnl)
/*    */     {
/* 77 */       count = cnl.getLength();
/*    */     }
/*    */     else
/*    */     {
/* 81 */       count = 0;
/* 82 */     }return count;
/*    */   }
/*    */ 
/*    */   public XObject execute(XPathContext xctxt)
/*    */     throws TransformerException
/*    */   {
/* 95 */     XNumber xnum = new XNumber(getCountOfContextNodeList(xctxt));
/*    */ 
/* 97 */     return xnum;
/*    */   }
/*    */ 
/*    */   public void fixupVariables(Vector vars, int globalsSize)
/*    */   {
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.functions.FuncLast
 * JD-Core Version:    0.6.2
 */