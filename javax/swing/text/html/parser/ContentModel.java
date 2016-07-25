/*     */ package javax.swing.text.html.parser;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public final class ContentModel
/*     */   implements Serializable
/*     */ {
/*     */   public int type;
/*     */   public Object content;
/*     */   public ContentModel next;
/*     */   private boolean[] valSet;
/*     */   private boolean[] val;
/*     */ 
/*     */   public ContentModel()
/*     */   {
/*     */   }
/*     */ 
/*     */   public ContentModel(Element paramElement)
/*     */   {
/*  66 */     this(0, paramElement, null);
/*     */   }
/*     */ 
/*     */   public ContentModel(int paramInt, ContentModel paramContentModel)
/*     */   {
/*  73 */     this(paramInt, paramContentModel, null);
/*     */   }
/*     */ 
/*     */   public ContentModel(int paramInt, Object paramObject, ContentModel paramContentModel)
/*     */   {
/*  80 */     this.type = paramInt;
/*  81 */     this.content = paramObject;
/*  82 */     this.next = paramContentModel;
/*     */   }
/*     */ 
/*     */   public boolean empty()
/*     */   {
/*     */     ContentModel localContentModel;
/*  90 */     switch (this.type) {
/*     */     case 42:
/*     */     case 63:
/*  93 */       return true;
/*     */     case 43:
/*     */     case 124:
/*  97 */       for (localContentModel = (ContentModel)this.content; localContentModel != null; localContentModel = localContentModel.next) {
/*  98 */         if (localContentModel.empty()) {
/*  99 */           return true;
/*     */         }
/*     */       }
/* 102 */       return false;
/*     */     case 38:
/*     */     case 44:
/* 106 */       for (localContentModel = (ContentModel)this.content; localContentModel != null; localContentModel = localContentModel.next) {
/* 107 */         if (!localContentModel.empty()) {
/* 108 */           return false;
/*     */         }
/*     */       }
/* 111 */       return true;
/*     */     }
/*     */ 
/* 114 */     return false;
/*     */   }
/*     */ 
/*     */   public void getElements(Vector<Element> paramVector)
/*     */   {
/* 123 */     switch (this.type) {
/*     */     case 42:
/*     */     case 43:
/*     */     case 63:
/* 127 */       ((ContentModel)this.content).getElements(paramVector);
/* 128 */       break;
/*     */     case 38:
/*     */     case 44:
/*     */     case 124:
/* 132 */       for (ContentModel localContentModel = (ContentModel)this.content; localContentModel != null; localContentModel = localContentModel.next) {
/* 133 */         localContentModel.getElements(paramVector);
/*     */       }
/* 135 */       break;
/*     */     default:
/* 137 */       paramVector.addElement((Element)this.content);
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean first(Object paramObject)
/*     */   {
/*     */     Object localObject;
/* 152 */     switch (this.type) {
/*     */     case 42:
/*     */     case 43:
/*     */     case 63:
/* 156 */       return ((ContentModel)this.content).first(paramObject);
/*     */     case 44:
/* 159 */       for (localObject = (ContentModel)this.content; localObject != null; localObject = ((ContentModel)localObject).next) {
/* 160 */         if (((ContentModel)localObject).first(paramObject)) {
/* 161 */           return true;
/*     */         }
/* 163 */         if (!((ContentModel)localObject).empty()) {
/* 164 */           return false;
/*     */         }
/*     */       }
/* 167 */       return false;
/*     */     case 38:
/*     */     case 124:
/* 171 */       localObject = (Element)paramObject;
/* 172 */       if (this.valSet == null) {
/* 173 */         this.valSet = new boolean[Element.maxIndex + 1];
/* 174 */         this.val = new boolean[Element.maxIndex + 1];
/*     */       }
/*     */ 
/* 177 */       if (this.valSet[localObject.index] != 0) {
/* 178 */         return this.val[localObject.index];
/*     */       }
/* 180 */       for (ContentModel localContentModel = (ContentModel)this.content; localContentModel != null; localContentModel = localContentModel.next) {
/* 181 */         if (localContentModel.first(paramObject)) {
/* 182 */           this.val[localObject.index] = true;
/* 183 */           break;
/*     */         }
/*     */       }
/* 186 */       this.valSet[localObject.index] = true;
/* 187 */       return this.val[localObject.index];
/*     */     }
/*     */ 
/* 191 */     return this.content == paramObject;
/*     */   }
/*     */ 
/*     */   public Element first()
/*     */   {
/* 210 */     switch (this.type) {
/*     */     case 38:
/*     */     case 42:
/*     */     case 63:
/*     */     case 124:
/* 215 */       return null;
/*     */     case 43:
/*     */     case 44:
/* 219 */       return ((ContentModel)this.content).first();
/*     */     }
/*     */ 
/* 222 */     return (Element)this.content;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 230 */     switch (this.type) {
/*     */     case 42:
/* 232 */       return this.content + "*";
/*     */     case 63:
/* 234 */       return this.content + "?";
/*     */     case 43:
/* 236 */       return this.content + "+";
/*     */     case 38:
/*     */     case 44:
/*     */     case 124:
/* 241 */       char[] arrayOfChar = { ' ', (char)this.type, ' ' };
/* 242 */       String str = "";
/* 243 */       for (ContentModel localContentModel = (ContentModel)this.content; localContentModel != null; localContentModel = localContentModel.next) {
/* 244 */         str = str + localContentModel;
/* 245 */         if (localContentModel.next != null) {
/* 246 */           str = str + new String(arrayOfChar);
/*     */         }
/*     */       }
/* 249 */       return "(" + str + ")";
/*     */     }
/*     */ 
/* 252 */     return this.content.toString();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.html.parser.ContentModel
 * JD-Core Version:    0.6.2
 */