/*     */ package com.sun.jmx.snmp.IPAcl;
/*     */ 
/*     */ import java.util.Stack;
/*     */ 
/*     */ class JJTParserState
/*     */ {
/*     */   private Stack nodes;
/*     */   private Stack marks;
/*     */   private int sp;
/*     */   private int mk;
/*     */   private boolean node_created;
/*     */ 
/*     */   JJTParserState()
/*     */   {
/*  40 */     this.nodes = new Stack();
/*  41 */     this.marks = new Stack();
/*  42 */     this.sp = 0;
/*  43 */     this.mk = 0;
/*     */   }
/*     */ 
/*     */   boolean nodeCreated()
/*     */   {
/*  50 */     return this.node_created;
/*     */   }
/*     */ 
/*     */   void reset()
/*     */   {
/*  56 */     this.nodes.removeAllElements();
/*  57 */     this.marks.removeAllElements();
/*  58 */     this.sp = 0;
/*  59 */     this.mk = 0;
/*     */   }
/*     */ 
/*     */   Node rootNode()
/*     */   {
/*  65 */     return (Node)this.nodes.elementAt(0);
/*     */   }
/*     */ 
/*     */   void pushNode(Node paramNode)
/*     */   {
/*  70 */     this.nodes.push(paramNode);
/*  71 */     this.sp += 1;
/*     */   }
/*     */ 
/*     */   Node popNode()
/*     */   {
/*  77 */     if (--this.sp < this.mk) {
/*  78 */       this.mk = ((Integer)this.marks.pop()).intValue();
/*     */     }
/*  80 */     return (Node)this.nodes.pop();
/*     */   }
/*     */ 
/*     */   Node peekNode()
/*     */   {
/*  85 */     return (Node)this.nodes.peek();
/*     */   }
/*     */ 
/*     */   int nodeArity()
/*     */   {
/*  91 */     return this.sp - this.mk;
/*     */   }
/*     */ 
/*     */   void clearNodeScope(Node paramNode)
/*     */   {
/*  96 */     while (this.sp > this.mk) {
/*  97 */       popNode();
/*     */     }
/*  99 */     this.mk = ((Integer)this.marks.pop()).intValue();
/*     */   }
/*     */ 
/*     */   void openNodeScope(Node paramNode)
/*     */   {
/* 104 */     this.marks.push(new Integer(this.mk));
/* 105 */     this.mk = this.sp;
/* 106 */     paramNode.jjtOpen();
/*     */   }
/*     */ 
/*     */   void closeNodeScope(Node paramNode, int paramInt)
/*     */   {
/* 115 */     this.mk = ((Integer)this.marks.pop()).intValue();
/* 116 */     while (paramInt-- > 0) {
/* 117 */       Node localNode = popNode();
/* 118 */       localNode.jjtSetParent(paramNode);
/* 119 */       paramNode.jjtAddChild(localNode, paramInt);
/*     */     }
/* 121 */     paramNode.jjtClose();
/* 122 */     pushNode(paramNode);
/* 123 */     this.node_created = true;
/*     */   }
/*     */ 
/*     */   void closeNodeScope(Node paramNode, boolean paramBoolean)
/*     */   {
/* 133 */     if (paramBoolean) {
/* 134 */       int i = nodeArity();
/* 135 */       this.mk = ((Integer)this.marks.pop()).intValue();
/* 136 */       while (i-- > 0) {
/* 137 */         Node localNode = popNode();
/* 138 */         localNode.jjtSetParent(paramNode);
/* 139 */         paramNode.jjtAddChild(localNode, i);
/*     */       }
/* 141 */       paramNode.jjtClose();
/* 142 */       pushNode(paramNode);
/* 143 */       this.node_created = true;
/*     */     } else {
/* 145 */       this.mk = ((Integer)this.marks.pop()).intValue();
/* 146 */       this.node_created = false;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.IPAcl.JJTParserState
 * JD-Core Version:    0.6.2
 */