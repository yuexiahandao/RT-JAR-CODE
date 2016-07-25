/*     */ package com.sun.jmx.snmp.IPAcl;
/*     */ 
/*     */ import java.net.InetAddress;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Vector;
/*     */ 
/*     */ class SimpleNode
/*     */   implements Node
/*     */ {
/*     */   protected Node parent;
/*     */   protected Node[] children;
/*     */   protected int id;
/*     */   protected Parser parser;
/*     */ 
/*     */   public SimpleNode(int paramInt)
/*     */   {
/*  42 */     this.id = paramInt;
/*     */   }
/*     */ 
/*     */   public SimpleNode(Parser paramParser, int paramInt) {
/*  46 */     this(paramInt);
/*  47 */     this.parser = paramParser;
/*     */   }
/*     */ 
/*     */   public static Node jjtCreate(int paramInt) {
/*  51 */     return new SimpleNode(paramInt);
/*     */   }
/*     */ 
/*     */   public static Node jjtCreate(Parser paramParser, int paramInt) {
/*  55 */     return new SimpleNode(paramParser, paramInt);
/*     */   }
/*     */   public void jjtOpen() {
/*     */   }
/*     */ 
/*     */   public void jjtClose() {
/*     */   }
/*     */ 
/*     */   public void jjtSetParent(Node paramNode) {
/*  64 */     this.parent = paramNode; } 
/*  65 */   public Node jjtGetParent() { return this.parent; }
/*     */ 
/*     */   public void jjtAddChild(Node paramNode, int paramInt) {
/*  68 */     if (this.children == null) {
/*  69 */       this.children = new Node[paramInt + 1];
/*  70 */     } else if (paramInt >= this.children.length) {
/*  71 */       Node[] arrayOfNode = new Node[paramInt + 1];
/*  72 */       System.arraycopy(this.children, 0, arrayOfNode, 0, this.children.length);
/*  73 */       this.children = arrayOfNode;
/*     */     }
/*  75 */     this.children[paramInt] = paramNode;
/*     */   }
/*     */ 
/*     */   public Node jjtGetChild(int paramInt) {
/*  79 */     return this.children[paramInt];
/*     */   }
/*     */ 
/*     */   public int jjtGetNumChildren() {
/*  83 */     return this.children == null ? 0 : this.children.length;
/*     */   }
/*     */ 
/*     */   public void buildTrapEntries(Hashtable<InetAddress, Vector<String>> paramHashtable)
/*     */   {
/*  94 */     if (this.children != null)
/*  95 */       for (int i = 0; i < this.children.length; i++) {
/*  96 */         SimpleNode localSimpleNode = (SimpleNode)this.children[i];
/*  97 */         if (localSimpleNode != null)
/*  98 */           localSimpleNode.buildTrapEntries(paramHashtable);
/*     */       }
/*     */   }
/*     */ 
/*     */   public void buildInformEntries(Hashtable<InetAddress, Vector<String>> paramHashtable)
/*     */   {
/* 107 */     if (this.children != null)
/* 108 */       for (int i = 0; i < this.children.length; i++) {
/* 109 */         SimpleNode localSimpleNode = (SimpleNode)this.children[i];
/* 110 */         if (localSimpleNode != null)
/* 111 */           localSimpleNode.buildInformEntries(paramHashtable);
/*     */       }
/*     */   }
/*     */ 
/*     */   public void buildAclEntries(PrincipalImpl paramPrincipalImpl, AclImpl paramAclImpl)
/*     */   {
/* 121 */     if (this.children != null)
/* 122 */       for (int i = 0; i < this.children.length; i++) {
/* 123 */         SimpleNode localSimpleNode = (SimpleNode)this.children[i];
/* 124 */         if (localSimpleNode != null)
/* 125 */           localSimpleNode.buildAclEntries(paramPrincipalImpl, paramAclImpl);
/*     */       }
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 139 */     return ParserTreeConstants.jjtNodeName[this.id]; } 
/* 140 */   public String toString(String paramString) { return paramString + toString(); }
/*     */ 
/*     */ 
/*     */   public void dump(String paramString)
/*     */   {
/* 146 */     if (this.children != null)
/* 147 */       for (int i = 0; i < this.children.length; i++) {
/* 148 */         SimpleNode localSimpleNode = (SimpleNode)this.children[i];
/* 149 */         if (localSimpleNode != null)
/* 150 */           localSimpleNode.dump(paramString + " ");
/*     */       }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.IPAcl.SimpleNode
 * JD-Core Version:    0.6.2
 */