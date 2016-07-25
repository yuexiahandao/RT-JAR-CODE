/*     */ package com.sun.jmx.snmp.daemon;
/*     */ 
/*     */ import com.sun.jmx.snmp.SnmpOid;
/*     */ import com.sun.jmx.snmp.agent.SnmpMibAgent;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Vector;
/*     */ 
/*     */ final class SnmpMibTree
/*     */ {
/*     */   private SnmpMibAgent defaultAgent;
/*     */   private TreeNode root;
/*     */ 
/*     */   public SnmpMibTree()
/*     */   {
/*  51 */     this.defaultAgent = null;
/*  52 */     this.root = new TreeNode(-1L, null, null, null);
/*     */   }
/*     */ 
/*     */   public void setDefaultAgent(SnmpMibAgent paramSnmpMibAgent) {
/*  56 */     this.defaultAgent = paramSnmpMibAgent;
/*  57 */     this.root.agent = paramSnmpMibAgent;
/*     */   }
/*     */ 
/*     */   public SnmpMibAgent getDefaultAgent() {
/*  61 */     return this.defaultAgent;
/*     */   }
/*     */ 
/*     */   public void register(SnmpMibAgent paramSnmpMibAgent) {
/*  65 */     this.root.registerNode(paramSnmpMibAgent);
/*     */   }
/*     */ 
/*     */   public void register(SnmpMibAgent paramSnmpMibAgent, long[] paramArrayOfLong) {
/*  69 */     this.root.registerNode(paramArrayOfLong, 0, paramSnmpMibAgent);
/*     */   }
/*     */ 
/*     */   public SnmpMibAgent getAgentMib(SnmpOid paramSnmpOid) {
/*  73 */     TreeNode localTreeNode = this.root.retrieveMatchingBranch(paramSnmpOid.longValue(), 0);
/*  74 */     if (localTreeNode == null) {
/*  75 */       return this.defaultAgent;
/*     */     }
/*  77 */     if (localTreeNode.getAgentMib() == null) {
/*  78 */       return this.defaultAgent;
/*     */     }
/*  80 */     return localTreeNode.getAgentMib();
/*     */   }
/*     */ 
/*     */   public void unregister(SnmpMibAgent paramSnmpMibAgent, SnmpOid[] paramArrayOfSnmpOid) {
/*  84 */     for (int i = 0; i < paramArrayOfSnmpOid.length; i++) {
/*  85 */       long[] arrayOfLong = paramArrayOfSnmpOid[i].longValue();
/*  86 */       TreeNode localTreeNode = this.root.retrieveMatchingBranch(arrayOfLong, 0);
/*  87 */       if (localTreeNode != null)
/*     */       {
/*  89 */         localTreeNode.removeAgent(paramSnmpMibAgent);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void unregister(SnmpMibAgent paramSnmpMibAgent)
/*     */   {
/*  96 */     this.root.removeAgentFully(paramSnmpMibAgent);
/*     */   }
/*     */ 
/*     */   public void printTree()
/*     */   {
/* 109 */     this.root.printTree(">"); } 
/* 262 */   final class TreeNode { private Vector<TreeNode> children = new Vector();
/* 263 */     private Vector<SnmpMibAgent> agents = new Vector();
/*     */     private long nodeValue;
/*     */     private SnmpMibAgent agent;
/*     */     private TreeNode parent;
/*     */ 
/* 120 */     void registerNode(SnmpMibAgent paramSnmpMibAgent) { long[] arrayOfLong = paramSnmpMibAgent.getRootOid();
/* 121 */       registerNode(arrayOfLong, 0, paramSnmpMibAgent); }
/*     */ 
/*     */     TreeNode retrieveMatchingBranch(long[] paramArrayOfLong, int paramInt)
/*     */     {
/* 125 */       TreeNode localTreeNode1 = retrieveChild(paramArrayOfLong, paramInt);
/* 126 */       if (localTreeNode1 == null)
/* 127 */         return this;
/* 128 */       if (this.children.size() == 0)
/*     */       {
/* 131 */         return localTreeNode1;
/*     */       }
/* 133 */       if (paramInt + 1 == paramArrayOfLong.length)
/*     */       {
/* 136 */         return localTreeNode1;
/*     */       }
/*     */ 
/* 139 */       TreeNode localTreeNode2 = localTreeNode1.retrieveMatchingBranch(paramArrayOfLong, paramInt + 1);
/*     */ 
/* 143 */       return localTreeNode2.agent == null ? this : localTreeNode2;
/*     */     }
/*     */ 
/*     */     SnmpMibAgent getAgentMib() {
/* 147 */       return this.agent;
/*     */     }
/*     */ 
/*     */     public void printTree(String paramString)
/*     */     {
/* 152 */       StringBuffer localStringBuffer = new StringBuffer();
/* 153 */       if (this.agents == null) {
/* 154 */         return;
/*     */       }
/*     */ 
/* 157 */       for (Enumeration localEnumeration = this.agents.elements(); localEnumeration.hasMoreElements(); ) {
/* 158 */         localObject = (SnmpMibAgent)localEnumeration.nextElement();
/* 159 */         if (localObject == null)
/* 160 */           localStringBuffer.append("empty ");
/*     */         else
/* 162 */           localStringBuffer.append(((SnmpMibAgent)localObject).getMibName() + " ");
/*     */       }
/*     */       Object localObject;
/* 164 */       paramString = paramString + " ";
/* 165 */       if (this.children == null) {
/* 166 */         return;
/*     */       }
/* 168 */       for (localEnumeration = this.children.elements(); localEnumeration.hasMoreElements(); ) {
/* 169 */         localObject = (TreeNode)localEnumeration.nextElement();
/* 170 */         ((TreeNode)localObject).printTree(paramString);
/*     */       }
/*     */     }
/*     */ 
/*     */     private TreeNode(long arg2, SnmpMibAgent paramTreeNode, TreeNode arg5)
/*     */     {
/* 182 */       this.nodeValue = ???;
/*     */       Object localObject;
/* 183 */       this.parent = localObject;
/* 184 */       this.agents.addElement(paramTreeNode);
/*     */     }
/*     */ 
/*     */     private void removeAgentFully(SnmpMibAgent paramSnmpMibAgent) {
/* 188 */       Vector localVector = new Vector();
/* 189 */       Enumeration localEnumeration = this.children.elements();
/* 190 */       while (localEnumeration.hasMoreElements())
/*     */       {
/* 192 */         TreeNode localTreeNode = (TreeNode)localEnumeration.nextElement();
/* 193 */         localTreeNode.removeAgentFully(paramSnmpMibAgent);
/* 194 */         if (localTreeNode.agents.isEmpty()) {
/* 195 */           localVector.add(localTreeNode);
/*     */         }
/*     */       }
/* 198 */       for (localEnumeration = localVector.elements(); localEnumeration.hasMoreElements(); ) {
/* 199 */         this.children.removeElement(localEnumeration.nextElement());
/*     */       }
/* 201 */       removeAgent(paramSnmpMibAgent);
/*     */     }
/*     */ 
/*     */     private void removeAgent(SnmpMibAgent paramSnmpMibAgent)
/*     */     {
/* 206 */       if (!this.agents.contains(paramSnmpMibAgent))
/* 207 */         return;
/* 208 */       this.agents.removeElement(paramSnmpMibAgent);
/*     */ 
/* 210 */       if (!this.agents.isEmpty())
/* 211 */         this.agent = ((SnmpMibAgent)this.agents.firstElement());
/*     */     }
/*     */ 
/*     */     private void setAgent(SnmpMibAgent paramSnmpMibAgent)
/*     */     {
/* 216 */       this.agent = paramSnmpMibAgent;
/*     */     }
/*     */ 
/*     */     private void registerNode(long[] paramArrayOfLong, int paramInt, SnmpMibAgent paramSnmpMibAgent)
/*     */     {
/* 221 */       if (paramInt >= paramArrayOfLong.length)
/*     */       {
/* 224 */         return;
/* 225 */       }TreeNode localTreeNode = retrieveChild(paramArrayOfLong, paramInt);
/* 226 */       if (localTreeNode == null)
/*     */       {
/* 229 */         long l = paramArrayOfLong[paramInt];
/* 230 */         localTreeNode = new TreeNode(SnmpMibTree.this, l, paramSnmpMibAgent, this);
/* 231 */         this.children.addElement(localTreeNode);
/*     */       }
/* 234 */       else if (!this.agents.contains(paramSnmpMibAgent)) {
/* 235 */         this.agents.addElement(paramSnmpMibAgent);
/*     */       }
/*     */ 
/* 240 */       if (paramInt == paramArrayOfLong.length - 1) {
/* 241 */         localTreeNode.setAgent(paramSnmpMibAgent);
/*     */       }
/*     */       else
/* 244 */         localTreeNode.registerNode(paramArrayOfLong, paramInt + 1, paramSnmpMibAgent);
/*     */     }
/*     */ 
/*     */     private TreeNode retrieveChild(long[] paramArrayOfLong, int paramInt) {
/* 248 */       long l = paramArrayOfLong[paramInt];
/*     */ 
/* 250 */       for (Enumeration localEnumeration = this.children.elements(); localEnumeration.hasMoreElements(); ) {
/* 251 */         TreeNode localTreeNode = (TreeNode)localEnumeration.nextElement();
/* 252 */         if (localTreeNode.match(l))
/* 253 */           return localTreeNode;
/*     */       }
/* 255 */       return null;
/*     */     }
/*     */ 
/*     */     private final boolean match(long paramLong) {
/* 259 */       return this.nodeValue == paramLong;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.daemon.SnmpMibTree
 * JD-Core Version:    0.6.2
 */