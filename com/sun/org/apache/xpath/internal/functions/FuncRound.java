/*    */ package com.sun.org.apache.xpath.internal.functions;
/*    */ 
/*    */ import com.sun.org.apache.xpath.internal.Expression;
/*    */ import com.sun.org.apache.xpath.internal.XPathContext;
/*    */ import com.sun.org.apache.xpath.internal.objects.XNumber;
/*    */ import com.sun.org.apache.xpath.internal.objects.XObject;
/*    */ import javax.xml.transform.TransformerException;
/*    */ 
/*    */ public class FuncRound extends FunctionOneArg
/*    */ {
/*    */   static final long serialVersionUID = -7970583902573826611L;
/*    */ 
/*    */   public XObject execute(XPathContext xctxt)
/*    */     throws TransformerException
/*    */   {
/* 47 */     XObject obj = this.m_arg0.execute(xctxt);
/* 48 */     double val = obj.num();
/* 49 */     if ((val >= -0.5D) && (val < 0.0D)) return new XNumber(-0.0D);
/* 50 */     if (val == 0.0D) return new XNumber(val);
/* 51 */     return new XNumber(Math.floor(val + 0.5D));
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.functions.FuncRound
 * JD-Core Version:    0.6.2
 */