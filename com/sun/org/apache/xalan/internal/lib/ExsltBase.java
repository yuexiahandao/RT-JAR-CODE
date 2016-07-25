/*    */ package com.sun.org.apache.xalan.internal.lib;
/*    */ 
/*    */ import com.sun.org.apache.xml.internal.dtm.ref.DTMNodeProxy;
/*    */ import org.w3c.dom.Node;
/*    */ import org.w3c.dom.NodeList;
/*    */ 
/*    */ public abstract class ExsltBase
/*    */ {
/*    */   protected static String toString(Node n)
/*    */   {
/* 44 */     if ((n instanceof DTMNodeProxy)) {
/* 45 */       return ((DTMNodeProxy)n).getStringValue();
/*    */     }
/*    */ 
/* 48 */     String value = n.getNodeValue();
/* 49 */     if (value == null)
/*    */     {
/* 51 */       NodeList nodelist = n.getChildNodes();
/* 52 */       StringBuffer buf = new StringBuffer();
/* 53 */       for (int i = 0; i < nodelist.getLength(); i++)
/*    */       {
/* 55 */         Node childNode = nodelist.item(i);
/* 56 */         buf.append(toString(childNode));
/*    */       }
/* 58 */       return buf.toString();
/*    */     }
/*    */ 
/* 61 */     return value;
/*    */   }
/*    */ 
/*    */   protected static double toNumber(Node n)
/*    */   {
/* 74 */     double d = 0.0D;
/* 75 */     String str = toString(n);
/*    */     try
/*    */     {
/* 78 */       d = Double.valueOf(str).doubleValue();
/*    */     }
/*    */     catch (NumberFormatException e)
/*    */     {
/* 82 */       d = (0.0D / 0.0D);
/*    */     }
/* 84 */     return d;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.lib.ExsltBase
 * JD-Core Version:    0.6.2
 */