/*    */ package com.sun.org.apache.xpath.internal.functions;
/*    */ 
/*    */ import com.sun.org.apache.xml.internal.utils.XMLString;
/*    */ import com.sun.org.apache.xpath.internal.Expression;
/*    */ import com.sun.org.apache.xpath.internal.XPathContext;
/*    */ import com.sun.org.apache.xpath.internal.objects.XObject;
/*    */ import com.sun.org.apache.xpath.internal.objects.XString;
/*    */ import javax.xml.transform.TransformerException;
/*    */ 
/*    */ public class FuncSubstringAfter extends Function2Args
/*    */ {
/*    */   static final long serialVersionUID = -8119731889862512194L;
/*    */ 
/*    */   public XObject execute(XPathContext xctxt)
/*    */     throws TransformerException
/*    */   {
/* 49 */     XMLString s1 = this.m_arg0.execute(xctxt).xstr();
/* 50 */     XMLString s2 = this.m_arg1.execute(xctxt).xstr();
/* 51 */     int index = s1.indexOf(s2);
/*    */ 
/* 53 */     return -1 == index ? XString.EMPTYSTRING : (XString)s1.substring(index + s2.length());
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.functions.FuncSubstringAfter
 * JD-Core Version:    0.6.2
 */