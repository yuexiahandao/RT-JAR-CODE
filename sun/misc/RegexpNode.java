/*     */ package sun.misc;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ 
/*     */ class RegexpNode
/*     */ {
/*     */   char c;
/*     */   RegexpNode firstchild;
/*     */   RegexpNode nextsibling;
/*     */   int depth;
/*     */   boolean exact;
/*     */   Object result;
/* 268 */   String re = null;
/*     */ 
/*     */   RegexpNode() {
/* 271 */     this.c = '#';
/* 272 */     this.depth = 0;
/*     */   }
/*     */   RegexpNode(char paramChar, int paramInt) {
/* 275 */     this.c = paramChar;
/* 276 */     this.depth = paramInt;
/*     */   }
/*     */   RegexpNode add(char paramChar) {
/* 279 */     RegexpNode localRegexpNode = this.firstchild;
/* 280 */     if (localRegexpNode == null) {
/* 281 */       localRegexpNode = new RegexpNode(paramChar, this.depth + 1);
/*     */     } else {
/* 283 */       while (localRegexpNode != null) {
/* 284 */         if (localRegexpNode.c == paramChar) {
/* 285 */           return localRegexpNode;
/*     */         }
/* 287 */         localRegexpNode = localRegexpNode.nextsibling;
/* 288 */       }localRegexpNode = new RegexpNode(paramChar, this.depth + 1);
/* 289 */       localRegexpNode.nextsibling = this.firstchild;
/*     */     }
/* 291 */     this.firstchild = localRegexpNode;
/* 292 */     return localRegexpNode;
/*     */   }
/*     */   RegexpNode find(char paramChar) {
/* 295 */     for (RegexpNode localRegexpNode = this.firstchild; 
/* 296 */       localRegexpNode != null; 
/* 297 */       localRegexpNode = localRegexpNode.nextsibling)
/* 298 */       if (localRegexpNode.c == paramChar)
/* 299 */         return localRegexpNode;
/* 300 */     return null;
/*     */   }
/*     */   void print(PrintStream paramPrintStream) {
/* 303 */     if (this.nextsibling != null) {
/* 304 */       RegexpNode localRegexpNode = this;
/* 305 */       paramPrintStream.print("(");
/* 306 */       while (localRegexpNode != null) {
/* 307 */         paramPrintStream.write(localRegexpNode.c);
/* 308 */         if (localRegexpNode.firstchild != null)
/* 309 */           localRegexpNode.firstchild.print(paramPrintStream);
/* 310 */         localRegexpNode = localRegexpNode.nextsibling;
/* 311 */         paramPrintStream.write(localRegexpNode != null ? 124 : 41);
/*     */       }
/*     */     } else {
/* 314 */       paramPrintStream.write(this.c);
/* 315 */       if (this.firstchild != null)
/* 316 */         this.firstchild.print(paramPrintStream);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.misc.RegexpNode
 * JD-Core Version:    0.6.2
 */