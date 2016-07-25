/*    */ package com.sun.org.apache.xml.internal.security.transforms.params;
/*    */ 
/*    */ import com.sun.org.apache.xml.internal.security.transforms.TransformParam;
/*    */ import com.sun.org.apache.xml.internal.security.utils.SignatureElementProxy;
/*    */ import org.w3c.dom.Document;
/*    */ import org.w3c.dom.Element;
/*    */ import org.w3c.dom.Node;
/*    */ import org.w3c.dom.NodeList;
/*    */ 
/*    */ public class XPathContainer extends SignatureElementProxy
/*    */   implements TransformParam
/*    */ {
/*    */   public XPathContainer(Document paramDocument)
/*    */   {
/* 48 */     super(paramDocument);
/*    */   }
/*    */ 
/*    */   public void setXPath(String paramString)
/*    */   {
/* 58 */     if (this._constructionElement.getChildNodes() != null) {
/* 59 */       localObject = this._constructionElement.getChildNodes();
/*    */ 
/* 61 */       for (int i = 0; i < ((NodeList)localObject).getLength(); i++) {
/* 62 */         this._constructionElement.removeChild(((NodeList)localObject).item(i));
/*    */       }
/*    */     }
/*    */ 
/* 66 */     Object localObject = this._doc.createTextNode(paramString);
/* 67 */     this._constructionElement.appendChild((Node)localObject);
/*    */   }
/*    */ 
/*    */   public String getXPath()
/*    */   {
/* 76 */     return getTextFromTextChild();
/*    */   }
/*    */ 
/*    */   public String getBaseLocalName()
/*    */   {
/* 81 */     return "XPath";
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.transforms.params.XPathContainer
 * JD-Core Version:    0.6.2
 */