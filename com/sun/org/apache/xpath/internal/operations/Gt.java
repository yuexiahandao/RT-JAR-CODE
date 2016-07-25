/*    */ package com.sun.org.apache.xpath.internal.operations;
/*    */ 
/*    */ import com.sun.org.apache.xpath.internal.objects.XBoolean;
/*    */ import com.sun.org.apache.xpath.internal.objects.XObject;
/*    */ import javax.xml.transform.TransformerException;
/*    */ 
/*    */ public class Gt extends Operation
/*    */ {
/*    */   static final long serialVersionUID = 8927078751014375950L;
/*    */ 
/*    */   public XObject operate(XObject left, XObject right)
/*    */     throws TransformerException
/*    */   {
/* 49 */     return left.greaterThan(right) ? XBoolean.S_TRUE : XBoolean.S_FALSE;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.operations.Gt
 * JD-Core Version:    0.6.2
 */