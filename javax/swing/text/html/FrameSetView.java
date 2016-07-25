/*     */ package javax.swing.text.html;
/*     */ 
/*     */ import java.util.StringTokenizer;
/*     */ import javax.swing.SizeRequirements;
/*     */ import javax.swing.text.AttributeSet;
/*     */ import javax.swing.text.BoxView;
/*     */ import javax.swing.text.Element;
/*     */ import javax.swing.text.View;
/*     */ 
/*     */ class FrameSetView extends BoxView
/*     */ {
/*     */   String[] children;
/*     */   int[] percentChildren;
/*     */   int[] absoluteChildren;
/*     */   int[] relativeChildren;
/*     */   int percentTotals;
/*     */   int absoluteTotals;
/*     */   int relativeTotals;
/*     */ 
/*     */   public FrameSetView(Element paramElement, int paramInt)
/*     */   {
/*  62 */     super(paramElement, paramInt);
/*  63 */     this.children = null;
/*     */   }
/*     */ 
/*     */   private String[] parseRowColSpec(HTML.Attribute paramAttribute)
/*     */   {
/*  74 */     AttributeSet localAttributeSet = getElement().getAttributes();
/*  75 */     String str = "*";
/*  76 */     if ((localAttributeSet != null) && 
/*  77 */       (localAttributeSet.getAttribute(paramAttribute) != null)) {
/*  78 */       str = (String)localAttributeSet.getAttribute(paramAttribute);
/*     */     }
/*     */ 
/*  82 */     StringTokenizer localStringTokenizer = new StringTokenizer(str, ",");
/*  83 */     int i = localStringTokenizer.countTokens();
/*  84 */     int j = getViewCount();
/*  85 */     String[] arrayOfString = new String[Math.max(i, j)];
/*  86 */     for (int k = 0; 
/*  87 */       k < i; k++) {
/*  88 */       arrayOfString[k] = localStringTokenizer.nextToken().trim();
/*     */ 
/*  92 */       if (arrayOfString[k].equals("100%")) {
/*  93 */         arrayOfString[k] = "*";
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/*  98 */     for (; k < arrayOfString.length; k++) {
/*  99 */       arrayOfString[k] = "*";
/*     */     }
/* 101 */     return arrayOfString;
/*     */   }
/*     */ 
/*     */   private void init()
/*     */   {
/* 111 */     if (getAxis() == 1)
/* 112 */       this.children = parseRowColSpec(HTML.Attribute.ROWS);
/*     */     else {
/* 114 */       this.children = parseRowColSpec(HTML.Attribute.COLS);
/*     */     }
/* 116 */     this.percentChildren = new int[this.children.length];
/* 117 */     this.relativeChildren = new int[this.children.length];
/* 118 */     this.absoluteChildren = new int[this.children.length];
/*     */ 
/* 120 */     for (int i = 0; i < this.children.length; i++) {
/* 121 */       this.percentChildren[i] = -1;
/* 122 */       this.relativeChildren[i] = -1;
/* 123 */       this.absoluteChildren[i] = -1;
/*     */ 
/* 125 */       if (this.children[i].endsWith("*")) {
/* 126 */         if (this.children[i].length() > 1) {
/* 127 */           this.relativeChildren[i] = Integer.parseInt(this.children[i].substring(0, this.children[i].length() - 1));
/*     */ 
/* 130 */           this.relativeTotals += this.relativeChildren[i];
/*     */         } else {
/* 132 */           this.relativeChildren[i] = 1;
/* 133 */           this.relativeTotals += 1;
/*     */         }
/* 135 */       } else if (this.children[i].indexOf('%') != -1) {
/* 136 */         this.percentChildren[i] = parseDigits(this.children[i]);
/* 137 */         this.percentTotals += this.percentChildren[i];
/*     */       } else {
/* 139 */         this.absoluteChildren[i] = Integer.parseInt(this.children[i]);
/*     */       }
/*     */     }
/* 142 */     if (this.percentTotals > 100) {
/* 143 */       for (i = 0; i < this.percentChildren.length; i++) {
/* 144 */         if (this.percentChildren[i] > 0) {
/* 145 */           this.percentChildren[i] = (this.percentChildren[i] * 100 / this.percentTotals);
/*     */         }
/*     */       }
/*     */ 
/* 149 */       this.percentTotals = 100;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void layoutMajorAxis(int paramInt1, int paramInt2, int[] paramArrayOfInt1, int[] paramArrayOfInt2)
/*     */   {
/* 172 */     if (this.children == null) {
/* 173 */       init();
/*     */     }
/* 175 */     SizeRequirements.calculateTiledPositions(paramInt1, null, getChildRequests(paramInt1, paramInt2), paramArrayOfInt1, paramArrayOfInt2);
/*     */   }
/*     */ 
/*     */   protected SizeRequirements[] getChildRequests(int paramInt1, int paramInt2)
/*     */   {
/* 183 */     int[] arrayOfInt = new int[this.children.length];
/*     */ 
/* 185 */     spread(paramInt1, arrayOfInt);
/* 186 */     int i = getViewCount();
/* 187 */     SizeRequirements[] arrayOfSizeRequirements = new SizeRequirements[i];
/* 188 */     int j = 0; for (int k = 0; j < i; j++) {
/* 189 */       View localView = getView(j);
/* 190 */       if (((localView instanceof FrameView)) || ((localView instanceof FrameSetView))) {
/* 191 */         arrayOfSizeRequirements[j] = new SizeRequirements((int)localView.getMinimumSpan(paramInt2), arrayOfInt[k], (int)localView.getMaximumSpan(paramInt2), 0.5F);
/*     */ 
/* 195 */         k++;
/*     */       } else {
/* 197 */         int m = (int)localView.getMinimumSpan(paramInt2);
/* 198 */         int n = (int)localView.getPreferredSpan(paramInt2);
/* 199 */         int i1 = (int)localView.getMaximumSpan(paramInt2);
/* 200 */         float f = localView.getAlignment(paramInt2);
/* 201 */         arrayOfSizeRequirements[j] = new SizeRequirements(m, n, i1, f);
/*     */       }
/*     */     }
/* 204 */     return arrayOfSizeRequirements;
/*     */   }
/*     */ 
/*     */   private void spread(int paramInt, int[] paramArrayOfInt)
/*     */   {
/* 216 */     if (paramInt == 0) {
/* 217 */       return;
/*     */     }
/*     */ 
/* 220 */     int i = 0;
/* 221 */     int j = paramInt;
/*     */ 
/* 226 */     for (int k = 0; k < paramArrayOfInt.length; k++) {
/* 227 */       if (this.absoluteChildren[k] > 0) {
/* 228 */         paramArrayOfInt[k] = this.absoluteChildren[k];
/* 229 */         j -= paramArrayOfInt[k];
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 235 */     i = j;
/* 236 */     for (k = 0; k < paramArrayOfInt.length; k++) {
/* 237 */       if ((this.percentChildren[k] > 0) && (i > 0)) {
/* 238 */         paramArrayOfInt[k] = (this.percentChildren[k] * i / 100);
/* 239 */         j -= paramArrayOfInt[k];
/* 240 */       } else if ((this.percentChildren[k] > 0) && (i <= 0)) {
/* 241 */         paramArrayOfInt[k] = (paramInt / paramArrayOfInt.length);
/* 242 */         j -= paramArrayOfInt[k];
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 247 */     if ((j > 0) && (this.relativeTotals > 0)) {
/* 248 */       for (k = 0; k < paramArrayOfInt.length; k++) {
/* 249 */         if (this.relativeChildren[k] > 0) {
/* 250 */           paramArrayOfInt[k] = (j * this.relativeChildren[k] / this.relativeTotals);
/*     */         }
/*     */       }
/*     */     }
/* 254 */     else if (j > 0)
/*     */     {
/* 273 */       float f = paramInt - j;
/* 274 */       float[] arrayOfFloat = new float[paramArrayOfInt.length];
/* 275 */       j = paramInt;
/* 276 */       for (int m = 0; m < paramArrayOfInt.length; m++)
/*     */       {
/* 281 */         arrayOfFloat[m] = (paramArrayOfInt[m] / f * 100.0F);
/* 282 */         paramArrayOfInt[m] = ((int)(paramInt * arrayOfFloat[m] / 100.0F));
/* 283 */         j -= paramArrayOfInt[m];
/*     */       }
/*     */ 
/* 290 */       m = 0;
/* 291 */       while (j != 0) {
/* 292 */         if (j < 0) {
/* 293 */           paramArrayOfInt[(m++)] -= 1;
/* 294 */           j++;
/*     */         }
/*     */         else {
/* 297 */           paramArrayOfInt[(m++)] += 1;
/* 298 */           j--;
/*     */         }
/*     */ 
/* 302 */         if (m == paramArrayOfInt.length) m = 0;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private int parseDigits(String paramString)
/*     */   {
/* 312 */     int i = 0;
/* 313 */     for (int j = 0; j < paramString.length(); j++) {
/* 314 */       char c = paramString.charAt(j);
/* 315 */       if (Character.isDigit(c)) {
/* 316 */         i = i * 10 + Character.digit(c, 10);
/*     */       }
/*     */     }
/* 319 */     return i;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.html.FrameSetView
 * JD-Core Version:    0.6.2
 */