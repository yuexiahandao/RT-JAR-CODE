/*     */ package sun.net.idn;
/*     */ 
/*     */ final class UCharacterDirection
/*     */   implements UCharacterEnums.ECharacterDirection
/*     */ {
/*     */   public static String toString(int paramInt)
/*     */   {
/*  68 */     switch (paramInt)
/*     */     {
/*     */     case 0:
/*  71 */       return "Left-to-Right";
/*     */     case 1:
/*  73 */       return "Right-to-Left";
/*     */     case 2:
/*  75 */       return "European Number";
/*     */     case 3:
/*  77 */       return "European Number Separator";
/*     */     case 4:
/*  79 */       return "European Number Terminator";
/*     */     case 5:
/*  81 */       return "Arabic Number";
/*     */     case 6:
/*  83 */       return "Common Number Separator";
/*     */     case 7:
/*  85 */       return "Paragraph Separator";
/*     */     case 8:
/*  87 */       return "Segment Separator";
/*     */     case 9:
/*  89 */       return "Whitespace";
/*     */     case 10:
/*  91 */       return "Other Neutrals";
/*     */     case 11:
/*  93 */       return "Left-to-Right Embedding";
/*     */     case 12:
/*  95 */       return "Left-to-Right Override";
/*     */     case 13:
/*  97 */       return "Right-to-Left Arabic";
/*     */     case 14:
/*  99 */       return "Right-to-Left Embedding";
/*     */     case 15:
/* 101 */       return "Right-to-Left Override";
/*     */     case 16:
/* 103 */       return "Pop Directional Format";
/*     */     case 17:
/* 105 */       return "Non-Spacing Mark";
/*     */     case 18:
/* 107 */       return "Boundary Neutral";
/*     */     }
/* 109 */     return "Unassigned";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.idn.UCharacterDirection
 * JD-Core Version:    0.6.2
 */