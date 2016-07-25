/*     */ package javax.swing.text.html.parser;
/*     */ 
/*     */ import java.util.BitSet;
/*     */ 
/*     */ final class TagStack
/*     */   implements DTDConstants
/*     */ {
/*     */   TagElement tag;
/*     */   Element elem;
/*     */   ContentModelState state;
/*     */   TagStack next;
/*     */   BitSet inclusions;
/*     */   BitSet exclusions;
/*     */   boolean net;
/*     */   boolean pre;
/*     */ 
/*     */   TagStack(TagElement paramTagElement, TagStack paramTagStack)
/*     */   {
/*  61 */     this.tag = paramTagElement;
/*  62 */     this.elem = paramTagElement.getElement();
/*  63 */     this.next = paramTagStack;
/*     */ 
/*  65 */     Element localElement = paramTagElement.getElement();
/*  66 */     if (localElement.getContent() != null) {
/*  67 */       this.state = new ContentModelState(localElement.getContent());
/*     */     }
/*     */ 
/*  70 */     if (paramTagStack != null) {
/*  71 */       this.inclusions = paramTagStack.inclusions;
/*  72 */       this.exclusions = paramTagStack.exclusions;
/*  73 */       this.pre = paramTagStack.pre;
/*     */     }
/*  75 */     if (paramTagElement.isPreformatted()) {
/*  76 */       this.pre = true;
/*     */     }
/*     */ 
/*  79 */     if (localElement.inclusions != null) {
/*  80 */       if (this.inclusions != null) {
/*  81 */         this.inclusions = ((BitSet)this.inclusions.clone());
/*  82 */         this.inclusions.or(localElement.inclusions);
/*     */       } else {
/*  84 */         this.inclusions = localElement.inclusions;
/*     */       }
/*     */     }
/*  87 */     if (localElement.exclusions != null)
/*  88 */       if (this.exclusions != null) {
/*  89 */         this.exclusions = ((BitSet)this.exclusions.clone());
/*  90 */         this.exclusions.or(localElement.exclusions);
/*     */       } else {
/*  92 */         this.exclusions = localElement.exclusions;
/*     */       }
/*     */   }
/*     */ 
/*     */   public Element first()
/*     */   {
/* 102 */     return this.state != null ? this.state.first() : null;
/*     */   }
/*     */ 
/*     */   public ContentModel contentModel()
/*     */   {
/* 110 */     if (this.state == null) {
/* 111 */       return null;
/*     */     }
/* 113 */     return this.state.getModel();
/*     */   }
/*     */ 
/*     */   boolean excluded(int paramInt)
/*     */   {
/* 124 */     return (this.exclusions != null) && (this.exclusions.get(this.elem.getIndex()));
/*     */   }
/*     */ 
/*     */   boolean advance(Element paramElement)
/*     */   {
/* 134 */     if ((this.exclusions != null) && (this.exclusions.get(paramElement.getIndex()))) {
/* 135 */       return false;
/*     */     }
/* 137 */     if (this.state != null) {
/* 138 */       ContentModelState localContentModelState = this.state.advance(paramElement);
/* 139 */       if (localContentModelState != null) {
/* 140 */         this.state = localContentModelState;
/* 141 */         return true;
/*     */       }
/* 143 */     } else if (this.elem.getType() == 19) {
/* 144 */       return true;
/*     */     }
/* 146 */     return (this.inclusions != null) && (this.inclusions.get(paramElement.getIndex()));
/*     */   }
/*     */ 
/*     */   boolean terminate()
/*     */   {
/* 153 */     return (this.state == null) || (this.state.terminate());
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 160 */     return this.next + " <" + this.tag.getElement().getName() + ">";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.html.parser.TagStack
 * JD-Core Version:    0.6.2
 */