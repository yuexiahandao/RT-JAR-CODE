/*    */ package com.sun.org.apache.xpath.internal.functions;
/*    */ 
/*    */ import com.sun.org.apache.xml.internal.dtm.DTM;
/*    */ import com.sun.org.apache.xpath.internal.Expression;
/*    */ import com.sun.org.apache.xpath.internal.XPathContext;
/*    */ import com.sun.org.apache.xpath.internal.objects.XBoolean;
/*    */ import com.sun.org.apache.xpath.internal.objects.XObject;
/*    */ import javax.xml.transform.TransformerException;
/*    */ 
/*    */ public class FuncLang extends FunctionOneArg
/*    */ {
/*    */   static final long serialVersionUID = -7868705139354872185L;
/*    */ 
/*    */   public XObject execute(XPathContext xctxt)
/*    */     throws TransformerException
/*    */   {
/* 49 */     String lang = this.m_arg0.execute(xctxt).str();
/* 50 */     int parent = xctxt.getCurrentNode();
/* 51 */     boolean isLang = false;
/* 52 */     DTM dtm = xctxt.getDTM(parent);
/*    */ 
/* 54 */     while (-1 != parent)
/*    */     {
/* 56 */       if (1 == dtm.getNodeType(parent))
/*    */       {
/* 58 */         int langAttr = dtm.getAttributeNode(parent, "http://www.w3.org/XML/1998/namespace", "lang");
/*    */ 
/* 60 */         if (-1 != langAttr)
/*    */         {
/* 62 */           String langVal = dtm.getNodeValue(langAttr);
/*    */ 
/* 64 */           if (!langVal.toLowerCase().startsWith(lang.toLowerCase()))
/*    */             break;
/* 66 */           int valLen = lang.length();
/*    */ 
/* 68 */           if ((langVal.length() == valLen) || (langVal.charAt(valLen) == '-'))
/*    */           {
/* 71 */             isLang = true;
/*    */           }
/* 73 */           break;
/*    */         }
/*    */ 
/*    */       }
/*    */ 
/* 79 */       parent = dtm.getParent(parent);
/*    */     }
/*    */ 
/* 82 */     return isLang ? XBoolean.S_TRUE : XBoolean.S_FALSE;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.functions.FuncLang
 * JD-Core Version:    0.6.2
 */