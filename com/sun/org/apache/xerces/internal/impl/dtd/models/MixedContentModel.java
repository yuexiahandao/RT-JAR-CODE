/*     */ package com.sun.org.apache.xerces.internal.impl.dtd.models;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.xni.QName;
/*     */ 
/*     */ public class MixedContentModel
/*     */   implements ContentModelValidator
/*     */ {
/*     */   private int fCount;
/*     */   private QName[] fChildren;
/*     */   private int[] fChildrenType;
/*     */   private boolean fOrdered;
/*     */ 
/*     */   public MixedContentModel(QName[] children, int[] type, int offset, int length, boolean ordered)
/*     */   {
/* 124 */     this.fCount = length;
/* 125 */     this.fChildren = new QName[this.fCount];
/* 126 */     this.fChildrenType = new int[this.fCount];
/* 127 */     for (int i = 0; i < this.fCount; i++) {
/* 128 */       this.fChildren[i] = new QName(children[(offset + i)]);
/* 129 */       this.fChildrenType[i] = type[(offset + i)];
/*     */     }
/* 131 */     this.fOrdered = ordered;
/*     */   }
/*     */ 
/*     */   public int validate(QName[] children, int offset, int length)
/*     */   {
/* 166 */     if (this.fOrdered) {
/* 167 */       int inIndex = 0;
/* 168 */       for (int outIndex = 0; outIndex < length; outIndex++)
/*     */       {
/* 171 */         QName curChild = children[(offset + outIndex)];
/* 172 */         if (curChild.localpart != null)
/*     */         {
/* 177 */           int type = this.fChildrenType[inIndex];
/* 178 */           if (type == 0) {
/* 179 */             if (this.fChildren[inIndex].rawname != children[(offset + outIndex)].rawname) {
/* 180 */               return outIndex;
/*     */             }
/*     */           }
/* 183 */           else if (type == 6) {
/* 184 */             String uri = this.fChildren[inIndex].uri;
/* 185 */             if ((uri != null) && (uri != children[outIndex].uri)) {
/* 186 */               return outIndex;
/*     */             }
/*     */           }
/* 189 */           else if (type == 8) {
/* 190 */             if (children[outIndex].uri != null) {
/* 191 */               return outIndex;
/*     */             }
/*     */           }
/* 194 */           else if ((type == 7) && 
/* 195 */             (this.fChildren[inIndex].uri == children[outIndex].uri)) {
/* 196 */             return outIndex;
/*     */           }
/*     */ 
/* 201 */           inIndex++;
/*     */         }
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 207 */       for (int outIndex = 0; outIndex < length; outIndex++)
/*     */       {
/* 210 */         QName curChild = children[(offset + outIndex)];
/*     */ 
/* 213 */         if (curChild.localpart != null)
/*     */         {
/* 217 */           for (int inIndex = 0; 
/* 218 */             inIndex < this.fCount; inIndex++)
/*     */           {
/* 220 */             int type = this.fChildrenType[inIndex];
/* 221 */             if (type == 0) {
/* 222 */               if (curChild.rawname == this.fChildren[inIndex].rawname) {
/* 223 */                 break;
/*     */               }
/*     */             }
/* 226 */             else if (type == 6) {
/* 227 */               String uri = this.fChildren[inIndex].uri;
/* 228 */               if ((uri == null) || (uri == children[outIndex].uri))
/*     */                 break;
/*     */             }
/*     */             else {
/* 232 */               if (type == 8 ? 
/* 233 */                 children[outIndex].uri != null : 
/* 237 */                 (type == 7) && 
/* 238 */                 (this.fChildren[inIndex].uri != children[outIndex].uri))
/*     */               {
/*     */                 break;
/*     */               }
/*     */ 
/*     */             }
/*     */ 
/*     */           }
/*     */ 
/* 249 */           if (inIndex == this.fCount) {
/* 250 */             return outIndex;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 255 */     return -1;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.dtd.models.MixedContentModel
 * JD-Core Version:    0.6.2
 */