/*     */ package com.sun.jndi.dns;
/*     */ 
/*     */ import java.util.Hashtable;
/*     */ 
/*     */ class NameNode
/*     */ {
/*     */   private String label;
/*  55 */   private Hashtable children = null;
/*  56 */   private boolean isZoneCut = false;
/*  57 */   private int depth = 0;
/*     */ 
/*     */   NameNode(String paramString) {
/*  60 */     this.label = paramString;
/*     */   }
/*     */ 
/*     */   protected NameNode newNameNode(String paramString)
/*     */   {
/*  69 */     return new NameNode(paramString);
/*     */   }
/*     */ 
/*     */   String getLabel()
/*     */   {
/*  77 */     return this.label;
/*     */   }
/*     */ 
/*     */   int depth()
/*     */   {
/*  85 */     return this.depth;
/*     */   }
/*     */ 
/*     */   boolean isZoneCut() {
/*  89 */     return this.isZoneCut;
/*     */   }
/*     */ 
/*     */   void setZoneCut(boolean paramBoolean) {
/*  93 */     this.isZoneCut = paramBoolean;
/*     */   }
/*     */ 
/*     */   Hashtable getChildren()
/*     */   {
/* 101 */     return this.children;
/*     */   }
/*     */ 
/*     */   NameNode get(String paramString)
/*     */   {
/* 110 */     return this.children != null ? (NameNode)this.children.get(paramString) : null;
/*     */   }
/*     */ 
/*     */   NameNode get(DnsName paramDnsName, int paramInt)
/*     */   {
/* 122 */     NameNode localNameNode = this;
/* 123 */     for (int i = paramInt; (i < paramDnsName.size()) && (localNameNode != null); i++) {
/* 124 */       localNameNode = localNameNode.get(paramDnsName.getKey(i));
/*     */     }
/* 126 */     return localNameNode;
/*     */   }
/*     */ 
/*     */   NameNode add(DnsName paramDnsName, int paramInt)
/*     */   {
/* 136 */     Object localObject = this;
/* 137 */     for (int i = paramInt; i < paramDnsName.size(); i++) {
/* 138 */       String str1 = paramDnsName.get(i);
/* 139 */       String str2 = paramDnsName.getKey(i);
/*     */ 
/* 141 */       NameNode localNameNode = null;
/* 142 */       if (((NameNode)localObject).children == null)
/* 143 */         ((NameNode)localObject).children = new Hashtable();
/*     */       else {
/* 145 */         localNameNode = (NameNode)((NameNode)localObject).children.get(str2);
/*     */       }
/* 147 */       if (localNameNode == null) {
/* 148 */         localNameNode = newNameNode(str1);
/* 149 */         localNameNode.depth = (((NameNode)localObject).depth + 1);
/* 150 */         ((NameNode)localObject).children.put(str2, localNameNode);
/*     */       }
/* 152 */       localObject = localNameNode;
/*     */     }
/* 154 */     return localObject;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.dns.NameNode
 * JD-Core Version:    0.6.2
 */