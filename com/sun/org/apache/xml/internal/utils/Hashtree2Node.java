/*    */ package com.sun.org.apache.xml.internal.utils;
/*    */ 
/*    */ import java.util.Enumeration;
/*    */ import java.util.Hashtable;
/*    */ import java.util.Vector;
/*    */ import org.w3c.dom.Document;
/*    */ import org.w3c.dom.Element;
/*    */ import org.w3c.dom.Node;
/*    */ 
/*    */ public abstract class Hashtree2Node
/*    */ {
/*    */   public static void appendHashToNode(Hashtable hash, String name, Node container, Document factory)
/*    */   {
/* 70 */     if ((null == container) || (null == factory) || (null == hash))
/*    */     {
/* 72 */       return;
/*    */     }
/*    */ 
/* 76 */     String elemName = null;
/* 77 */     if ((null == name) || ("".equals(name)))
/* 78 */       elemName = "appendHashToNode";
/*    */     else {
/* 80 */       elemName = name;
/*    */     }
/*    */     try
/*    */     {
/* 84 */       Element hashNode = factory.createElement(elemName);
/* 85 */       container.appendChild(hashNode);
/*    */ 
/* 87 */       Enumeration keys = hash.keys();
/* 88 */       Vector v = new Vector();
/*    */ 
/* 90 */       while (keys.hasMoreElements())
/*    */       {
/* 92 */         Object key = keys.nextElement();
/* 93 */         String keyStr = key.toString();
/* 94 */         Object item = hash.get(key);
/*    */ 
/* 96 */         if ((item instanceof Hashtable))
/*    */         {
/* 101 */           v.addElement(keyStr);
/* 102 */           v.addElement((Hashtable)item);
/*    */         }
/*    */         else
/*    */         {
/*    */           try
/*    */           {
/* 109 */             Element node = factory.createElement("item");
/* 110 */             node.setAttribute("key", keyStr);
/* 111 */             node.appendChild(factory.createTextNode((String)item));
/* 112 */             hashNode.appendChild(node);
/*    */           }
/*    */           catch (Exception e)
/*    */           {
/* 116 */             Element node = factory.createElement("item");
/* 117 */             node.setAttribute("key", keyStr);
/* 118 */             node.appendChild(factory.createTextNode("ERROR: Reading " + key + " threw: " + e.toString()));
/* 119 */             hashNode.appendChild(node);
/*    */           }
/*    */         }
/*    */ 
/*    */       }
/*    */ 
/* 125 */       keys = v.elements();
/* 126 */       while (keys.hasMoreElements())
/*    */       {
/* 129 */         String n = (String)keys.nextElement();
/* 130 */         Hashtable h = (Hashtable)keys.nextElement();
/*    */ 
/* 132 */         appendHashToNode(h, n, hashNode, factory);
/*    */       }
/*    */ 
/*    */     }
/*    */     catch (Exception e2)
/*    */     {
/* 139 */       e2.printStackTrace();
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.utils.Hashtree2Node
 * JD-Core Version:    0.6.2
 */