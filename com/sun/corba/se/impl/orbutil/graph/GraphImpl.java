/*     */ package com.sun.corba.se.impl.orbutil.graph;
/*     */ 
/*     */ import java.util.AbstractSet;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class GraphImpl extends AbstractSet
/*     */   implements Graph
/*     */ {
/*     */   private Map nodeToData;
/*     */ 
/*     */   public GraphImpl()
/*     */   {
/*  42 */     this.nodeToData = new HashMap();
/*     */   }
/*     */ 
/*     */   public GraphImpl(Collection paramCollection)
/*     */   {
/*  47 */     this();
/*  48 */     addAll(paramCollection);
/*     */   }
/*     */ 
/*     */   public boolean add(Object paramObject)
/*     */   {
/*  58 */     if (!(paramObject instanceof Node)) {
/*  59 */       throw new IllegalArgumentException("Graphs must contain only Node instances");
/*     */     }
/*  61 */     Node localNode = (Node)paramObject;
/*  62 */     boolean bool = this.nodeToData.keySet().contains(paramObject);
/*     */ 
/*  64 */     if (!bool) {
/*  65 */       NodeData localNodeData = new NodeData();
/*  66 */       this.nodeToData.put(localNode, localNodeData);
/*     */     }
/*     */ 
/*  69 */     return !bool;
/*     */   }
/*     */ 
/*     */   public Iterator iterator()
/*     */   {
/*  75 */     return this.nodeToData.keySet().iterator();
/*     */   }
/*     */ 
/*     */   public int size()
/*     */   {
/*  81 */     return this.nodeToData.keySet().size();
/*     */   }
/*     */ 
/*     */   public NodeData getNodeData(Node paramNode)
/*     */   {
/*  88 */     return (NodeData)this.nodeToData.get(paramNode);
/*     */   }
/*     */ 
/*     */   private void clearNodeData()
/*     */   {
/*  94 */     Iterator localIterator = this.nodeToData.entrySet().iterator();
/*  95 */     while (localIterator.hasNext()) {
/*  96 */       Map.Entry localEntry = (Map.Entry)localIterator.next();
/*  97 */       NodeData localNodeData = (NodeData)localEntry.getValue();
/*  98 */       localNodeData.clear();
/*     */     }
/*     */   }
/*     */ 
/*     */   void visitAll(NodeVisitor paramNodeVisitor)
/*     */   {
/* 112 */     int i = 0;
/*     */     do
/*     */     {
/* 118 */       i = 1;
/*     */ 
/* 122 */       Map.Entry[] arrayOfEntry = (Map.Entry[])this.nodeToData.entrySet().toArray(new Map.Entry[0]);
/*     */ 
/* 128 */       for (int j = 0; j < arrayOfEntry.length; j++) {
/* 129 */         Map.Entry localEntry = arrayOfEntry[j];
/* 130 */         Node localNode = (Node)localEntry.getKey();
/* 131 */         NodeData localNodeData = (NodeData)localEntry.getValue();
/*     */ 
/* 133 */         if (!localNodeData.isVisited()) {
/* 134 */           localNodeData.visited();
/* 135 */           i = 0;
/*     */ 
/* 137 */           paramNodeVisitor.visit(this, localNode, localNodeData);
/*     */         }
/*     */       }
/*     */     }
/* 140 */     while (i == 0);
/*     */   }
/*     */ 
/*     */   private void markNonRoots()
/*     */   {
/* 145 */     visitAll(new NodeVisitor()
/*     */     {
/*     */       public void visit(Graph paramAnonymousGraph, Node paramAnonymousNode, NodeData paramAnonymousNodeData)
/*     */       {
/* 149 */         Iterator localIterator = paramAnonymousNode.getChildren().iterator();
/* 150 */         while (localIterator.hasNext()) {
/* 151 */           Node localNode = (Node)localIterator.next();
/*     */ 
/* 155 */           paramAnonymousGraph.add(localNode);
/*     */ 
/* 158 */           NodeData localNodeData = paramAnonymousGraph.getNodeData(localNode);
/* 159 */           localNodeData.notRoot();
/*     */         }
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   private Set collectRootSet()
/*     */   {
/* 167 */     HashSet localHashSet = new HashSet();
/*     */ 
/* 169 */     Iterator localIterator = this.nodeToData.entrySet().iterator();
/* 170 */     while (localIterator.hasNext()) {
/* 171 */       Map.Entry localEntry = (Map.Entry)localIterator.next();
/* 172 */       Node localNode = (Node)localEntry.getKey();
/* 173 */       NodeData localNodeData = (NodeData)localEntry.getValue();
/* 174 */       if (localNodeData.isRoot()) {
/* 175 */         localHashSet.add(localNode);
/*     */       }
/*     */     }
/* 178 */     return localHashSet;
/*     */   }
/*     */ 
/*     */   public Set getRoots()
/*     */   {
/* 183 */     clearNodeData();
/* 184 */     markNonRoots();
/* 185 */     return collectRootSet();
/*     */   }
/*     */ 
/*     */   static abstract interface NodeVisitor
/*     */   {
/*     */     public abstract void visit(Graph paramGraph, Node paramNode, NodeData paramNodeData);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.orbutil.graph.GraphImpl
 * JD-Core Version:    0.6.2
 */