/*     */ package sun.font;
/*     */ 
/*     */ import java.awt.Font;
/*     */ import java.awt.GraphicsEnvironment;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Map;
/*     */ import sun.text.CodePointIterator;
/*     */ 
/*     */ public final class FontResolver
/*     */ {
/*     */   private Font[] allFonts;
/*     */   private Font[] supplementaryFonts;
/*     */   private int[] supplementaryIndices;
/*     */   private static final int DEFAULT_SIZE = 12;
/*  62 */   private Font defaultFont = new Font("Dialog", 0, 12);
/*     */   private static final int SHIFT = 9;
/*     */   private static final int BLOCKSIZE = 128;
/*     */   private static final int MASK = 127;
/*  74 */   private int[][] blocks = new int[512][];
/*     */   private static FontResolver INSTANCE;
/*     */ 
/*     */   private Font[] getAllFonts()
/*     */   {
/*  80 */     if (this.allFonts == null) {
/*  81 */       this.allFonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
/*     */ 
/*  83 */       for (int i = 0; i < this.allFonts.length; i++) {
/*  84 */         this.allFonts[i] = this.allFonts[i].deriveFont(12.0F);
/*     */       }
/*     */     }
/*  87 */     return this.allFonts;
/*     */   }
/*     */ 
/*     */   private int getIndexFor(char paramChar)
/*     */   {
/*  98 */     if (this.defaultFont.canDisplay(paramChar)) {
/*  99 */       return 1;
/*     */     }
/* 101 */     for (int i = 0; i < getAllFonts().length; i++) {
/* 102 */       if (this.allFonts[i].canDisplay(paramChar)) {
/* 103 */         return i + 2;
/*     */       }
/*     */     }
/* 106 */     return 1;
/*     */   }
/*     */ 
/*     */   private Font[] getAllSCFonts()
/*     */   {
/* 111 */     if (this.supplementaryFonts == null) {
/* 112 */       ArrayList localArrayList1 = new ArrayList();
/* 113 */       ArrayList localArrayList2 = new ArrayList();
/*     */ 
/* 115 */       for (int i = 0; i < getAllFonts().length; i++) {
/* 116 */         Font localFont = this.allFonts[i];
/* 117 */         Font2D localFont2D = FontUtilities.getFont2D(localFont);
/* 118 */         if (localFont2D.hasSupplementaryChars()) {
/* 119 */           localArrayList1.add(localFont);
/* 120 */           localArrayList2.add(Integer.valueOf(i));
/*     */         }
/*     */       }
/*     */ 
/* 124 */       i = localArrayList1.size();
/* 125 */       this.supplementaryIndices = new int[i];
/* 126 */       for (int j = 0; j < i; j++) {
/* 127 */         this.supplementaryIndices[j] = ((Integer)localArrayList2.get(j)).intValue();
/*     */       }
/* 129 */       this.supplementaryFonts = ((Font[])localArrayList1.toArray(new Font[i]));
/*     */     }
/* 131 */     return this.supplementaryFonts;
/*     */   }
/*     */ 
/*     */   private int getIndexFor(int paramInt)
/*     */   {
/* 144 */     if (this.defaultFont.canDisplay(paramInt)) {
/* 145 */       return 1;
/*     */     }
/*     */ 
/* 148 */     for (int i = 0; i < getAllSCFonts().length; i++) {
/* 149 */       if (this.supplementaryFonts[i].canDisplay(paramInt)) {
/* 150 */         return this.supplementaryIndices[i] + 2;
/*     */       }
/*     */     }
/* 153 */     return 1;
/*     */   }
/*     */ 
/*     */   public int getFontIndex(char paramChar)
/*     */   {
/* 165 */     int i = paramChar >> '\t';
/* 166 */     int[] arrayOfInt = this.blocks[i];
/* 167 */     if (arrayOfInt == null) {
/* 168 */       arrayOfInt = new int[''];
/* 169 */       this.blocks[i] = arrayOfInt;
/*     */     }
/*     */ 
/* 172 */     int j = paramChar & 0x7F;
/* 173 */     if (arrayOfInt[j] == 0) {
/* 174 */       arrayOfInt[j] = getIndexFor(paramChar);
/*     */     }
/* 176 */     return arrayOfInt[j];
/*     */   }
/*     */ 
/*     */   public int getFontIndex(int paramInt) {
/* 180 */     if (paramInt < 65536) {
/* 181 */       return getFontIndex((char)paramInt);
/*     */     }
/* 183 */     return getIndexFor(paramInt);
/*     */   }
/*     */ 
/*     */   public int nextFontRunIndex(CodePointIterator paramCodePointIterator)
/*     */   {
/* 196 */     int i = paramCodePointIterator.next();
/* 197 */     int j = 1;
/* 198 */     if (i != -1) {
/* 199 */       j = getFontIndex(i);
/*     */ 
/* 201 */       while ((i = paramCodePointIterator.next()) != -1) {
/* 202 */         if (getFontIndex(i) != j) {
/* 203 */           paramCodePointIterator.prev();
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 208 */     return j;
/*     */   }
/*     */ 
/*     */   public Font getFont(int paramInt, Map paramMap)
/*     */   {
/* 226 */     Font localFont = this.defaultFont;
/*     */ 
/* 228 */     if (paramInt >= 2) {
/* 229 */       localFont = this.allFonts[(paramInt - 2)];
/*     */     }
/*     */ 
/* 232 */     return localFont.deriveFont(paramMap);
/*     */   }
/*     */ 
/*     */   public static FontResolver getInstance()
/*     */   {
/* 241 */     if (INSTANCE == null) {
/* 242 */       INSTANCE = new FontResolver();
/*     */     }
/* 244 */     return INSTANCE;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.font.FontResolver
 * JD-Core Version:    0.6.2
 */