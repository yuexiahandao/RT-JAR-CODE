/*     */ package com.sun.beans.decoder;
/*     */ 
/*     */ public abstract class ElementHandler
/*     */ {
/*     */   private DocumentHandler owner;
/*     */   private ElementHandler parent;
/*     */   private String id;
/*     */ 
/*     */   public final DocumentHandler getOwner()
/*     */   {
/*  48 */     return this.owner;
/*     */   }
/*     */ 
/*     */   final void setOwner(DocumentHandler paramDocumentHandler)
/*     */   {
/*  60 */     if (paramDocumentHandler == null) {
/*  61 */       throw new IllegalArgumentException("Every element should have owner");
/*     */     }
/*  63 */     this.owner = paramDocumentHandler;
/*     */   }
/*     */ 
/*     */   public final ElementHandler getParent()
/*     */   {
/*  72 */     return this.parent;
/*     */   }
/*     */ 
/*     */   final void setParent(ElementHandler paramElementHandler)
/*     */   {
/*  84 */     this.parent = paramElementHandler;
/*     */   }
/*     */ 
/*     */   protected final Object getVariable(String paramString)
/*     */   {
/*  94 */     if (paramString.equals(this.id)) {
/*  95 */       ValueObject localValueObject = getValueObject();
/*  96 */       if (localValueObject.isVoid()) {
/*  97 */         throw new IllegalStateException("The element does not return value");
/*     */       }
/*  99 */       return localValueObject.getValue();
/*     */     }
/* 101 */     return this.parent != null ? this.parent.getVariable(paramString) : this.owner.getVariable(paramString);
/*     */   }
/*     */ 
/*     */   protected Object getContextBean()
/*     */   {
/* 112 */     if (this.parent != null) {
/* 113 */       localObject = this.parent.getValueObject();
/* 114 */       if (!((ValueObject)localObject).isVoid()) {
/* 115 */         return ((ValueObject)localObject).getValue();
/*     */       }
/* 117 */       throw new IllegalStateException("The outer element does not return value");
/*     */     }
/* 119 */     Object localObject = this.owner.getOwner();
/* 120 */     if (localObject != null) {
/* 121 */       return localObject;
/*     */     }
/* 123 */     throw new IllegalStateException("The topmost element does not have context");
/*     */   }
/*     */ 
/*     */   public void addAttribute(String paramString1, String paramString2)
/*     */   {
/* 139 */     if (paramString1.equals("id"))
/* 140 */       this.id = paramString2;
/*     */     else
/* 142 */       throw new IllegalArgumentException("Unsupported attribute: " + paramString1);
/*     */   }
/*     */ 
/*     */   public void startElement()
/*     */   {
/*     */   }
/*     */ 
/*     */   public void endElement()
/*     */   {
/* 169 */     ValueObject localValueObject = getValueObject();
/* 170 */     if (!localValueObject.isVoid()) {
/* 171 */       if (this.id != null) {
/* 172 */         this.owner.setVariable(this.id, localValueObject.getValue());
/*     */       }
/* 174 */       if (isArgument())
/* 175 */         if (this.parent != null)
/* 176 */           this.parent.addArgument(localValueObject.getValue());
/*     */         else
/* 178 */           this.owner.addObject(localValueObject.getValue());
/*     */     }
/*     */   }
/*     */ 
/*     */   public void addCharacter(char paramChar)
/*     */   {
/* 191 */     if ((paramChar != ' ') && (paramChar != '\n') && (paramChar != '\t') && (paramChar != '\r'))
/* 192 */       throw new IllegalStateException("Illegal character with code " + paramChar);
/*     */   }
/*     */ 
/*     */   protected void addArgument(Object paramObject)
/*     */   {
/* 203 */     throw new IllegalStateException("Could not add argument to simple element");
/*     */   }
/*     */ 
/*     */   protected boolean isArgument()
/*     */   {
/* 215 */     return this.id == null;
/*     */   }
/*     */ 
/*     */   protected abstract ValueObject getValueObject();
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.beans.decoder.ElementHandler
 * JD-Core Version:    0.6.2
 */