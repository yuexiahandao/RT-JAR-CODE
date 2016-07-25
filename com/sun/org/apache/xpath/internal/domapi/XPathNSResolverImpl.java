/*    */ package com.sun.org.apache.xpath.internal.domapi;
/*    */ 
/*    */ import com.sun.org.apache.xml.internal.utils.PrefixResolverDefault;
/*    */ import org.w3c.dom.Node;
/*    */ import org.w3c.dom.xpath.XPathNSResolver;
/*    */ 
/*    */ class XPathNSResolverImpl extends PrefixResolverDefault
/*    */   implements XPathNSResolver
/*    */ {
/*    */   public XPathNSResolverImpl(Node xpathExpressionContext)
/*    */   {
/* 53 */     super(xpathExpressionContext);
/*    */   }
/*    */ 
/*    */   public String lookupNamespaceURI(String prefix)
/*    */   {
/* 60 */     return super.getNamespaceForPrefix(prefix);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.domapi.XPathNSResolverImpl
 * JD-Core Version:    0.6.2
 */