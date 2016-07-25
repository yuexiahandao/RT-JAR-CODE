/*    */ package com.sun.org.apache.xpath.internal.functions;
/*    */ 
/*    */ import com.sun.org.apache.xpath.internal.Expression;
/*    */ import com.sun.org.apache.xpath.internal.XPathContext;
/*    */ import com.sun.org.apache.xpath.internal.objects.XBoolean;
/*    */ import com.sun.org.apache.xpath.internal.objects.XObject;
/*    */ import javax.xml.transform.TransformerException;
/*    */ 
/*    */ public class FuncContains extends Function2Args
/*    */ {
/*    */   static final long serialVersionUID = 5084753781887919723L;
/*    */ 
/*    */   public XObject execute(XPathContext xctxt)
/*    */     throws TransformerException
/*    */   {
/* 48 */     String s1 = this.m_arg0.execute(xctxt).str();
/* 49 */     String s2 = this.m_arg1.execute(xctxt).str();
/*    */ 
/* 52 */     if ((s1.length() == 0) && (s2.length() == 0)) {
/* 53 */       return XBoolean.S_TRUE;
/*    */     }
/* 55 */     int index = s1.indexOf(s2);
/*    */ 
/* 57 */     return index > -1 ? XBoolean.S_TRUE : XBoolean.S_FALSE;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.functions.FuncContains
 * JD-Core Version:    0.6.2
 */