/*     */ package javax.swing.text.html.parser;
/*     */ 
/*     */ class ContentModelState
/*     */ {
/*     */   ContentModel model;
/*     */   long value;
/*     */   ContentModelState next;
/*     */ 
/*     */   public ContentModelState(ContentModel paramContentModel)
/*     */   {
/*  54 */     this(paramContentModel, null, 0L);
/*     */   }
/*     */ 
/*     */   ContentModelState(Object paramObject, ContentModelState paramContentModelState)
/*     */   {
/*  62 */     this(paramObject, paramContentModelState, 0L);
/*     */   }
/*     */ 
/*     */   ContentModelState(Object paramObject, ContentModelState paramContentModelState, long paramLong)
/*     */   {
/*  70 */     this.model = ((ContentModel)paramObject);
/*  71 */     this.next = paramContentModelState;
/*  72 */     this.value = paramLong;
/*     */   }
/*     */ 
/*     */   public ContentModel getModel()
/*     */   {
/*  79 */     ContentModel localContentModel = this.model;
/*  80 */     for (int i = 0; i < this.value; i++) {
/*  81 */       if (localContentModel.next != null)
/*  82 */         localContentModel = localContentModel.next;
/*     */       else {
/*  84 */         return null;
/*     */       }
/*     */     }
/*  87 */     return localContentModel;
/*     */   }
/*     */ 
/*     */   public boolean terminate()
/*     */   {
/*     */     ContentModel localContentModel;
/*     */     int i;
/*  96 */     switch (this.model.type) {
/*     */     case 43:
/*  98 */       if ((this.value == 0L) && (!this.model.empty())) {
/*  99 */         return false;
/*     */       }
/*     */     case 42:
/*     */     case 63:
/* 103 */       return (this.next == null) || (this.next.terminate());
/*     */     case 124:
/* 106 */       for (localContentModel = (ContentModel)this.model.content; localContentModel != null; localContentModel = localContentModel.next) {
/* 107 */         if (localContentModel.empty()) {
/* 108 */           return (this.next == null) || (this.next.terminate());
/*     */         }
/*     */       }
/* 111 */       return false;
/*     */     case 38:
/* 114 */       localContentModel = (ContentModel)this.model.content;
/*     */ 
/* 116 */       for (i = 0; localContentModel != null; localContentModel = localContentModel.next) {
/* 117 */         if (((this.value & 1L << i) == 0L) && 
/* 118 */           (!localContentModel.empty()))
/* 119 */           return false;
/* 116 */         i++;
/*     */       }
/*     */ 
/* 123 */       return (this.next == null) || (this.next.terminate());
/*     */     case 44:
/* 127 */       localContentModel = (ContentModel)this.model.content;
/* 128 */       for (i = 0; i < this.value; localContentModel = localContentModel.next) i++;
/*     */ 
/* 130 */       while ((localContentModel != null) && (localContentModel.empty())) localContentModel = localContentModel.next;
/* 131 */       if (localContentModel != null) {
/* 132 */         return false;
/*     */       }
/* 134 */       return (this.next == null) || (this.next.terminate());
/*     */     }
/*     */ 
/* 138 */     return false;
/*     */   }
/*     */ 
/*     */   public Element first()
/*     */   {
/* 148 */     switch (this.model.type) {
/*     */     case 38:
/*     */     case 42:
/*     */     case 63:
/*     */     case 124:
/* 153 */       return null;
/*     */     case 43:
/* 156 */       return this.model.first();
/*     */     case 44:
/* 159 */       ContentModel localContentModel = (ContentModel)this.model.content;
/* 160 */       for (int i = 0; i < this.value; localContentModel = localContentModel.next) i++;
/* 161 */       return localContentModel.first();
/*     */     }
/*     */ 
/* 165 */     return this.model.first();
/*     */   }
/*     */ 
/*     */   public ContentModelState advance(Object paramObject)
/*     */   {
/*     */     ContentModel localContentModel;
/*     */     int i;
/* 175 */     switch (this.model.type) {
/*     */     case 43:
/* 177 */       if (this.model.first(paramObject)) {
/* 178 */         return new ContentModelState(this.model.content, new ContentModelState(this.model, this.next, this.value + 1L)).advance(paramObject);
/*     */       }
/*     */ 
/* 181 */       if (this.value != 0L) {
/* 182 */         if (this.next != null) {
/* 183 */           return this.next.advance(paramObject);
/*     */         }
/* 185 */         return null;
/*     */       }
/*     */ 
/*     */       break;
/*     */     case 42:
/* 191 */       if (this.model.first(paramObject)) {
/* 192 */         return new ContentModelState(this.model.content, this).advance(paramObject);
/*     */       }
/* 194 */       if (this.next != null) {
/* 195 */         return this.next.advance(paramObject);
/*     */       }
/* 197 */       return null;
/*     */     case 63:
/* 201 */       if (this.model.first(paramObject)) {
/* 202 */         return new ContentModelState(this.model.content, this.next).advance(paramObject);
/*     */       }
/* 204 */       if (this.next != null) {
/* 205 */         return this.next.advance(paramObject);
/*     */       }
/* 207 */       return null;
/*     */     case 124:
/* 211 */       for (localContentModel = (ContentModel)this.model.content; localContentModel != null; localContentModel = localContentModel.next) {
/* 212 */         if (localContentModel.first(paramObject)) {
/* 213 */           return new ContentModelState(localContentModel, this.next).advance(paramObject);
/*     */         }
/*     */       }
/* 216 */       break;
/*     */     case 44:
/* 219 */       localContentModel = (ContentModel)this.model.content;
/* 220 */       for (i = 0; i < this.value; localContentModel = localContentModel.next) i++;
/*     */ 
/* 222 */       if ((localContentModel.first(paramObject)) || (localContentModel.empty())) {
/* 223 */         if (localContentModel.next == null) {
/* 224 */           return new ContentModelState(localContentModel, this.next).advance(paramObject);
/*     */         }
/* 226 */         return new ContentModelState(localContentModel, new ContentModelState(this.model, this.next, this.value + 1L)).advance(paramObject);
/*     */       }
/*     */ 
/*     */       break;
/*     */     case 38:
/* 234 */       localContentModel = (ContentModel)this.model.content;
/* 235 */       i = 1;
/*     */ 
/* 237 */       for (int j = 0; localContentModel != null; localContentModel = localContentModel.next) {
/* 238 */         if ((this.value & 1L << j) == 0L) {
/* 239 */           if (localContentModel.first(paramObject)) {
/* 240 */             return new ContentModelState(localContentModel, new ContentModelState(this.model, this.next, this.value | 1L << j)).advance(paramObject);
/*     */           }
/*     */ 
/* 243 */           if (!localContentModel.empty())
/* 244 */             i = 0;
/*     */         }
/* 237 */         j++;
/*     */       }
/*     */ 
/* 248 */       if (i != 0) {
/* 249 */         if (this.next != null) {
/* 250 */           return this.next.advance(paramObject);
/*     */         }
/* 252 */         return null;
/*     */       }
/*     */ 
/*     */       break;
/*     */     default:
/* 259 */       if (this.model.content == paramObject) {
/* 260 */         if ((this.next == null) && ((paramObject instanceof Element)) && (((Element)paramObject).content != null))
/*     */         {
/* 262 */           return new ContentModelState(((Element)paramObject).content);
/*     */         }
/* 264 */         return this.next;
/*     */       }
/*     */ 
/*     */       break;
/*     */     }
/*     */ 
/* 293 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.html.parser.ContentModelState
 * JD-Core Version:    0.6.2
 */