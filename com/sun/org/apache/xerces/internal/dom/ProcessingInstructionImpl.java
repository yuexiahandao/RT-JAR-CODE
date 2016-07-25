/*     */ package com.sun.org.apache.xerces.internal.dom;
/*     */ 
/*     */ import org.w3c.dom.ProcessingInstruction;
/*     */ 
/*     */ public class ProcessingInstructionImpl extends CharacterDataImpl
/*     */   implements ProcessingInstruction
/*     */ {
/*     */   static final long serialVersionUID = 7554435174099981510L;
/*     */   protected String target;
/*     */ 
/*     */   public ProcessingInstructionImpl(CoreDocumentImpl ownerDoc, String target, String data)
/*     */   {
/*  61 */     super(ownerDoc, data);
/*  62 */     this.target = target;
/*     */   }
/*     */ 
/*     */   public short getNodeType()
/*     */   {
/*  74 */     return 7;
/*     */   }
/*     */ 
/*     */   public String getNodeName()
/*     */   {
/*  81 */     if (needsSyncData()) {
/*  82 */       synchronizeData();
/*     */     }
/*  84 */     return this.target;
/*     */   }
/*     */ 
/*     */   public String getTarget()
/*     */   {
/* 103 */     if (needsSyncData()) {
/* 104 */       synchronizeData();
/*     */     }
/* 106 */     return this.target;
/*     */   }
/*     */ 
/*     */   public String getData()
/*     */   {
/* 123 */     if (needsSyncData()) {
/* 124 */       synchronizeData();
/*     */     }
/* 126 */     return this.data;
/*     */   }
/*     */ 
/*     */   public void setData(String data)
/*     */   {
/* 139 */     setNodeValue(data);
/*     */   }
/*     */ 
/*     */   public String getBaseURI()
/*     */   {
/* 154 */     if (needsSyncData()) {
/* 155 */       synchronizeData();
/*     */     }
/* 157 */     return this.ownerNode.getBaseURI();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.dom.ProcessingInstructionImpl
 * JD-Core Version:    0.6.2
 */