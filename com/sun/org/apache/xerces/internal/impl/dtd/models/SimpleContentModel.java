/*     */ package com.sun.org.apache.xerces.internal.impl.dtd.models;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.xni.QName;
/*     */ 
/*     */ public class SimpleContentModel
/*     */   implements ContentModelValidator
/*     */ {
/*     */   public static final short CHOICE = -1;
/*     */   public static final short SEQUENCE = -1;
/* 114 */   private QName fFirstChild = new QName();
/*     */ 
/* 121 */   private QName fSecondChild = new QName();
/*     */   private int fOperator;
/*     */ 
/*     */   public SimpleContentModel(short operator, QName firstChild, QName secondChild)
/*     */   {
/* 154 */     this.fFirstChild.setValues(firstChild);
/* 155 */     if (secondChild != null) {
/* 156 */       this.fSecondChild.setValues(secondChild);
/*     */     }
/*     */     else {
/* 159 */       this.fSecondChild.clear();
/*     */     }
/* 161 */     this.fOperator = operator;
/*     */   }
/*     */ 
/*     */   public int validate(QName[] children, int offset, int length)
/*     */   {
/* 197 */     switch (this.fOperator)
/*     */     {
/*     */     case 0:
/* 201 */       if (length == 0) {
/* 202 */         return 0;
/*     */       }
/*     */ 
/* 205 */       if (children[offset].rawname != this.fFirstChild.rawname) {
/* 206 */         return 0;
/*     */       }
/*     */ 
/* 210 */       if (length > 1) {
/* 211 */         return 1;
/*     */       }
/*     */ 
/*     */       break;
/*     */     case 1:
/* 219 */       if ((length == 1) && 
/* 220 */         (children[offset].rawname != this.fFirstChild.rawname)) {
/* 221 */         return 0;
/*     */       }
/*     */ 
/* 229 */       if (length > 1) {
/* 230 */         return 1;
/*     */       }
/*     */ 
/*     */       break;
/*     */     case 2:
/* 240 */       if (length > 0)
/*     */       {
/* 242 */         for (int index = 0; index < length; index++)
/* 243 */           if (children[(offset + index)].rawname != this.fFirstChild.rawname)
/* 244 */             return index;
/*     */       }
/* 242 */       break;
/*     */     case 3:
/* 255 */       if (length == 0) {
/* 256 */         return 0;
/*     */       }
/*     */ 
/* 263 */       for (int index = 0; index < length; index++) {
/* 264 */         if (children[(offset + index)].rawname != this.fFirstChild.rawname) {
/* 265 */           return index;
/*     */         }
/*     */       }
/* 268 */       break;
/*     */     case 4:
/* 275 */       if (length == 0) {
/* 276 */         return 0;
/*     */       }
/*     */ 
/* 279 */       if ((children[offset].rawname != this.fFirstChild.rawname) && (children[offset].rawname != this.fSecondChild.rawname))
/*     */       {
/* 281 */         return 0;
/*     */       }
/*     */ 
/* 285 */       if (length > 1) {
/* 286 */         return 1;
/*     */       }
/*     */ 
/*     */       break;
/*     */     case 5:
/* 294 */       if (length == 2) {
/* 295 */         if (children[offset].rawname != this.fFirstChild.rawname) {
/* 296 */           return 0;
/*     */         }
/* 298 */         if (children[(offset + 1)].rawname != this.fSecondChild.rawname)
/* 299 */           return 1;
/*     */       }
/*     */       else
/*     */       {
/* 303 */         if (length > 2) {
/* 304 */           return 2;
/*     */         }
/*     */ 
/* 307 */         return length;
/*     */       }
/*     */ 
/*     */       break;
/*     */     default:
/* 313 */       throw new RuntimeException("ImplementationMessages.VAL_CST");
/*     */     }
/*     */ 
/* 317 */     return -1;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.dtd.models.SimpleContentModel
 * JD-Core Version:    0.6.2
 */