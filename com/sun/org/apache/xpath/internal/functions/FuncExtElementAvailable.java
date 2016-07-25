/*    */ package com.sun.org.apache.xpath.internal.functions;
/*    */ 
/*    */ import com.sun.org.apache.xml.internal.utils.PrefixResolver;
/*    */ import com.sun.org.apache.xpath.internal.Expression;
/*    */ import com.sun.org.apache.xpath.internal.ExtensionsProvider;
/*    */ import com.sun.org.apache.xpath.internal.XPathContext;
/*    */ import com.sun.org.apache.xpath.internal.objects.XBoolean;
/*    */ import com.sun.org.apache.xpath.internal.objects.XObject;
/*    */ import javax.xml.transform.TransformerException;
/*    */ 
/*    */ public class FuncExtElementAvailable extends FunctionOneArg
/*    */ {
/*    */   static final long serialVersionUID = -472533699257968546L;
/*    */ 
/*    */   public XObject execute(XPathContext xctxt)
/*    */     throws TransformerException
/*    */   {
/* 59 */     String fullName = this.m_arg0.execute(xctxt).str();
/* 60 */     int indexOfNSSep = fullName.indexOf(':');
/*    */     String methName;
/*    */     String namespace;
/*    */     String methName;
/* 62 */     if (indexOfNSSep < 0)
/*    */     {
/* 64 */       String prefix = "";
/* 65 */       String namespace = "http://www.w3.org/1999/XSL/Transform";
/* 66 */       methName = fullName;
/*    */     }
/*    */     else
/*    */     {
/* 70 */       String prefix = fullName.substring(0, indexOfNSSep);
/* 71 */       namespace = xctxt.getNamespaceContext().getNamespaceForPrefix(prefix);
/* 72 */       if (null == namespace)
/* 73 */         return XBoolean.S_FALSE;
/* 74 */       methName = fullName.substring(indexOfNSSep + 1);
/*    */     }
/*    */ 
/* 77 */     if ((namespace.equals("http://www.w3.org/1999/XSL/Transform")) || (namespace.equals("http://xml.apache.org/xalan")))
/*    */     {
/* 91 */       return XBoolean.S_FALSE;
/*    */     }
/*    */ 
/* 94 */     ExtensionsProvider extProvider = (ExtensionsProvider)xctxt.getOwnerObject();
/* 95 */     return extProvider.elementAvailable(namespace, methName) ? XBoolean.S_TRUE : XBoolean.S_FALSE;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.functions.FuncExtElementAvailable
 * JD-Core Version:    0.6.2
 */