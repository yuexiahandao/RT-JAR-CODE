/*     */ package com.sun.org.apache.xerces.internal.dom.events;
/*     */ 
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.events.MutationEvent;
/*     */ 
/*     */ public class MutationEventImpl extends EventImpl
/*     */   implements MutationEvent
/*     */ {
/*  35 */   Node relatedNode = null;
/*  36 */   String prevValue = null; String newValue = null; String attrName = null;
/*     */   public short attrChange;
/*     */   public static final String DOM_SUBTREE_MODIFIED = "DOMSubtreeModified";
/*     */   public static final String DOM_NODE_INSERTED = "DOMNodeInserted";
/*     */   public static final String DOM_NODE_REMOVED = "DOMNodeRemoved";
/*     */   public static final String DOM_NODE_REMOVED_FROM_DOCUMENT = "DOMNodeRemovedFromDocument";
/*     */   public static final String DOM_NODE_INSERTED_INTO_DOCUMENT = "DOMNodeInsertedIntoDocument";
/*     */   public static final String DOM_ATTR_MODIFIED = "DOMAttrModified";
/*     */   public static final String DOM_CHARACTER_DATA_MODIFIED = "DOMCharacterDataModified";
/*     */ 
/*     */   public String getAttrName()
/*     */   {
/*  56 */     return this.attrName;
/*     */   }
/*     */ 
/*     */   public short getAttrChange()
/*     */   {
/*  66 */     return this.attrChange;
/*     */   }
/*     */ 
/*     */   public String getNewValue()
/*     */   {
/*  75 */     return this.newValue;
/*     */   }
/*     */ 
/*     */   public String getPrevValue()
/*     */   {
/*  84 */     return this.prevValue;
/*     */   }
/*     */ 
/*     */   public Node getRelatedNode()
/*     */   {
/*  94 */     return this.relatedNode;
/*     */   }
/*     */ 
/*     */   public void initMutationEvent(String typeArg, boolean canBubbleArg, boolean cancelableArg, Node relatedNodeArg, String prevValueArg, String newValueArg, String attrNameArg, short attrChangeArg)
/*     */   {
/* 104 */     this.relatedNode = relatedNodeArg;
/* 105 */     this.prevValue = prevValueArg;
/* 106 */     this.newValue = newValueArg;
/* 107 */     this.attrName = attrNameArg;
/* 108 */     this.attrChange = attrChangeArg;
/* 109 */     super.initEvent(typeArg, canBubbleArg, cancelableArg);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.dom.events.MutationEventImpl
 * JD-Core Version:    0.6.2
 */