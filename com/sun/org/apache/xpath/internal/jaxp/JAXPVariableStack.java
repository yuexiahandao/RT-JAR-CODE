/*    */ package com.sun.org.apache.xpath.internal.jaxp;
/*    */ 
/*    */ import com.sun.org.apache.xalan.internal.res.XSLMessages;
/*    */ import com.sun.org.apache.xpath.internal.VariableStack;
/*    */ import com.sun.org.apache.xpath.internal.XPathContext;
/*    */ import com.sun.org.apache.xpath.internal.objects.XObject;
/*    */ import javax.xml.transform.TransformerException;
/*    */ import javax.xml.xpath.XPathVariableResolver;
/*    */ 
/*    */ public class JAXPVariableStack extends VariableStack
/*    */ {
/*    */   private final XPathVariableResolver resolver;
/*    */ 
/*    */   public JAXPVariableStack(XPathVariableResolver resolver)
/*    */   {
/* 47 */     this.resolver = resolver;
/*    */   }
/*    */ 
/*    */   public XObject getVariableOrParam(XPathContext xctxt, com.sun.org.apache.xml.internal.utils.QName qname) throws TransformerException, IllegalArgumentException
/*    */   {
/* 52 */     if (qname == null)
/*    */     {
/* 55 */       String fmsg = XSLMessages.createXPATHMessage("ER_ARG_CANNOT_BE_NULL", new Object[] { "Variable qname" });
/*    */ 
/* 58 */       throw new IllegalArgumentException(fmsg);
/*    */     }
/* 60 */     javax.xml.namespace.QName name = new javax.xml.namespace.QName(qname.getNamespace(), qname.getLocalPart());
/*    */ 
/* 64 */     Object varValue = this.resolver.resolveVariable(name);
/* 65 */     if (varValue == null) {
/* 66 */       String fmsg = XSLMessages.createXPATHMessage("ER_RESOLVE_VARIABLE_RETURNS_NULL", new Object[] { name.toString() });
/*    */ 
/* 69 */       throw new TransformerException(fmsg);
/*    */     }
/* 71 */     return XObject.create(varValue, xctxt);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.jaxp.JAXPVariableStack
 * JD-Core Version:    0.6.2
 */