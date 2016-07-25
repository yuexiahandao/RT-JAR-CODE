/*    */ package com.sun.org.apache.xml.internal.security.utils;
/*    */ 
/*    */ import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
/*    */ import org.w3c.dom.Element;
/*    */ import org.w3c.dom.Node;
/*    */ 
/*    */ public abstract class ElementCheckerImpl
/*    */   implements ElementChecker
/*    */ {
/*    */   public boolean isNamespaceElement(Node paramNode, String paramString1, String paramString2)
/*    */   {
/*  9 */     if ((paramNode == null) || (paramString2 != paramNode.getNamespaceURI()) || (!paramNode.getLocalName().equals(paramString1)))
/*    */     {
/* 11 */       return false;
/*    */     }
/*    */ 
/* 14 */     return true;
/*    */   }
/*    */ 
/*    */   public static class EmptyChecker extends ElementCheckerImpl
/*    */   {
/*    */     public void guaranteeThatElementInCorrectSpace(ElementProxy paramElementProxy, Element paramElement)
/*    */       throws XMLSecurityException
/*    */     {
/*    */     }
/*    */   }
/*    */ 
/*    */   public static class FullChecker extends ElementCheckerImpl
/*    */   {
/*    */     public void guaranteeThatElementInCorrectSpace(ElementProxy paramElementProxy, Element paramElement)
/*    */       throws XMLSecurityException
/*    */     {
/* 40 */       String str1 = paramElementProxy.getBaseLocalName();
/* 41 */       String str2 = paramElementProxy.getBaseNamespace();
/*    */ 
/* 43 */       String str3 = paramElement.getLocalName();
/* 44 */       String str4 = paramElement.getNamespaceURI();
/* 45 */       if ((!str2.equals(str4)) || (!str1.equals(str3)))
/*    */       {
/* 47 */         Object[] arrayOfObject = { str4 + ":" + str3, str2 + ":" + str1 };
/*    */ 
/* 49 */         throw new XMLSecurityException("xml.WrongElement", arrayOfObject);
/*    */       }
/*    */     }
/*    */   }
/*    */ 
/*    */   public static class InternedNsChecker extends ElementCheckerImpl
/*    */   {
/*    */     public void guaranteeThatElementInCorrectSpace(ElementProxy paramElementProxy, Element paramElement)
/*    */       throws XMLSecurityException
/*    */     {
/* 21 */       String str1 = paramElementProxy.getBaseLocalName();
/* 22 */       String str2 = paramElementProxy.getBaseNamespace();
/*    */ 
/* 24 */       String str3 = paramElement.getLocalName();
/* 25 */       String str4 = paramElement.getNamespaceURI();
/* 26 */       if ((str2 != str4) || (!str1.equals(str3)))
/*    */       {
/* 28 */         Object[] arrayOfObject = { str4 + ":" + str3, str2 + ":" + str1 };
/*    */ 
/* 30 */         throw new XMLSecurityException("xml.WrongElement", arrayOfObject);
/*    */       }
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.utils.ElementCheckerImpl
 * JD-Core Version:    0.6.2
 */