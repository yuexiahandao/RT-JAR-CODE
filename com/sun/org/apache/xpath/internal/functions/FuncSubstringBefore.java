/*    */ package com.sun.org.apache.xpath.internal.functions;
/*    */ 
/*    */ import com.sun.org.apache.xpath.internal.Expression;
/*    */ import com.sun.org.apache.xpath.internal.XPathContext;
/*    */ import com.sun.org.apache.xpath.internal.objects.XObject;
/*    */ import com.sun.org.apache.xpath.internal.objects.XString;
/*    */ import javax.xml.transform.TransformerException;
/*    */ 
/*    */ public class FuncSubstringBefore extends Function2Args
/*    */ {
/*    */   static final long serialVersionUID = 4110547161672431775L;
/*    */ 
/*    */   public XObject execute(XPathContext xctxt)
/*    */     throws TransformerException
/*    */   {
/* 48 */     String s1 = this.m_arg0.execute(xctxt).str();
/* 49 */     String s2 = this.m_arg1.execute(xctxt).str();
/* 50 */     int index = s1.indexOf(s2);
/*    */ 
/* 52 */     return -1 == index ? XString.EMPTYSTRING : new XString(s1.substring(0, index));
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.functions.FuncSubstringBefore
 * JD-Core Version:    0.6.2
 */