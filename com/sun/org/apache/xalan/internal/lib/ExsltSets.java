/*     */ package com.sun.org.apache.xalan.internal.lib;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.utils.DOMHelper;
/*     */ import com.sun.org.apache.xpath.internal.NodeSet;
/*     */ import java.util.Hashtable;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ 
/*     */ public class ExsltSets extends ExsltBase
/*     */ {
/*     */   public static NodeList leading(NodeList nl1, NodeList nl2)
/*     */   {
/*  63 */     if (nl2.getLength() == 0) {
/*  64 */       return nl1;
/*     */     }
/*  66 */     NodeSet ns1 = new NodeSet(nl1);
/*  67 */     NodeSet leadNodes = new NodeSet();
/*  68 */     Node endNode = nl2.item(0);
/*  69 */     if (!ns1.contains(endNode)) {
/*  70 */       return leadNodes;
/*     */     }
/*  72 */     for (int i = 0; i < nl1.getLength(); i++)
/*     */     {
/*  74 */       Node testNode = nl1.item(i);
/*  75 */       if ((DOMHelper.isNodeAfter(testNode, endNode)) && (!DOMHelper.isNodeTheSame(testNode, endNode)))
/*     */       {
/*  77 */         leadNodes.addElement(testNode);
/*     */       }
/*     */     }
/*  79 */     return leadNodes;
/*     */   }
/*     */ 
/*     */   public static NodeList trailing(NodeList nl1, NodeList nl2)
/*     */   {
/*  98 */     if (nl2.getLength() == 0) {
/*  99 */       return nl1;
/*     */     }
/* 101 */     NodeSet ns1 = new NodeSet(nl1);
/* 102 */     NodeSet trailNodes = new NodeSet();
/* 103 */     Node startNode = nl2.item(0);
/* 104 */     if (!ns1.contains(startNode)) {
/* 105 */       return trailNodes;
/*     */     }
/* 107 */     for (int i = 0; i < nl1.getLength(); i++)
/*     */     {
/* 109 */       Node testNode = nl1.item(i);
/* 110 */       if ((DOMHelper.isNodeAfter(startNode, testNode)) && (!DOMHelper.isNodeTheSame(startNode, testNode)))
/*     */       {
/* 112 */         trailNodes.addElement(testNode);
/*     */       }
/*     */     }
/* 114 */     return trailNodes;
/*     */   }
/*     */ 
/*     */   public static NodeList intersection(NodeList nl1, NodeList nl2)
/*     */   {
/* 130 */     NodeSet ns1 = new NodeSet(nl1);
/* 131 */     NodeSet ns2 = new NodeSet(nl2);
/* 132 */     NodeSet inter = new NodeSet();
/*     */ 
/* 134 */     inter.setShouldCacheNodes(true);
/*     */ 
/* 136 */     for (int i = 0; i < ns1.getLength(); i++)
/*     */     {
/* 138 */       Node n = ns1.elementAt(i);
/*     */ 
/* 140 */       if (ns2.contains(n)) {
/* 141 */         inter.addElement(n);
/*     */       }
/*     */     }
/* 144 */     return inter;
/*     */   }
/*     */ 
/*     */   public static NodeList difference(NodeList nl1, NodeList nl2)
/*     */   {
/* 160 */     NodeSet ns1 = new NodeSet(nl1);
/* 161 */     NodeSet ns2 = new NodeSet(nl2);
/*     */ 
/* 163 */     NodeSet diff = new NodeSet();
/*     */ 
/* 165 */     diff.setShouldCacheNodes(true);
/*     */ 
/* 167 */     for (int i = 0; i < ns1.getLength(); i++)
/*     */     {
/* 169 */       Node n = ns1.elementAt(i);
/*     */ 
/* 171 */       if (!ns2.contains(n)) {
/* 172 */         diff.addElement(n);
/*     */       }
/*     */     }
/* 175 */     return diff;
/*     */   }
/*     */ 
/*     */   public static NodeList distinct(NodeList nl)
/*     */   {
/* 192 */     NodeSet dist = new NodeSet();
/* 193 */     dist.setShouldCacheNodes(true);
/*     */ 
/* 195 */     Hashtable stringTable = new Hashtable();
/*     */ 
/* 197 */     for (int i = 0; i < nl.getLength(); i++)
/*     */     {
/* 199 */       Node currNode = nl.item(i);
/* 200 */       String key = toString(currNode);
/*     */ 
/* 202 */       if (key == null) {
/* 203 */         dist.addElement(currNode);
/* 204 */       } else if (!stringTable.containsKey(key))
/*     */       {
/* 206 */         stringTable.put(key, currNode);
/* 207 */         dist.addElement(currNode);
/*     */       }
/*     */     }
/*     */ 
/* 211 */     return dist;
/*     */   }
/*     */ 
/*     */   public static boolean hasSameNode(NodeList nl1, NodeList nl2)
/*     */   {
/* 230 */     NodeSet ns1 = new NodeSet(nl1);
/* 231 */     NodeSet ns2 = new NodeSet(nl2);
/*     */ 
/* 233 */     for (int i = 0; i < ns1.getLength(); i++)
/*     */     {
/* 235 */       if (ns2.contains(ns1.elementAt(i)))
/* 236 */         return true;
/*     */     }
/* 238 */     return false;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.lib.ExsltSets
 * JD-Core Version:    0.6.2
 */