/*     */ package sun.util.locale;
/*     */ 
/*     */ public class StringTokenIterator
/*     */ {
/*     */   private String text;
/*     */   private String dlms;
/*     */   private char delimiterChar;
/*     */   private String token;
/*     */   private int start;
/*     */   private int end;
/*     */   private boolean done;
/*     */ 
/*     */   public StringTokenIterator(String paramString1, String paramString2)
/*     */   {
/*  44 */     this.text = paramString1;
/*  45 */     if (paramString2.length() == 1)
/*  46 */       this.delimiterChar = paramString2.charAt(0);
/*     */     else {
/*  48 */       this.dlms = paramString2;
/*     */     }
/*  50 */     setStart(0);
/*     */   }
/*     */ 
/*     */   public String first() {
/*  54 */     setStart(0);
/*  55 */     return this.token;
/*     */   }
/*     */ 
/*     */   public String current() {
/*  59 */     return this.token;
/*     */   }
/*     */ 
/*     */   public int currentStart() {
/*  63 */     return this.start;
/*     */   }
/*     */ 
/*     */   public int currentEnd() {
/*  67 */     return this.end;
/*     */   }
/*     */ 
/*     */   public boolean isDone() {
/*  71 */     return this.done;
/*     */   }
/*     */ 
/*     */   public String next() {
/*  75 */     if (hasNext()) {
/*  76 */       this.start = (this.end + 1);
/*  77 */       this.end = nextDelimiter(this.start);
/*  78 */       this.token = this.text.substring(this.start, this.end);
/*     */     } else {
/*  80 */       this.start = this.end;
/*  81 */       this.token = null;
/*  82 */       this.done = true;
/*     */     }
/*  84 */     return this.token;
/*     */   }
/*     */ 
/*     */   public boolean hasNext() {
/*  88 */     return this.end < this.text.length();
/*     */   }
/*     */ 
/*     */   public StringTokenIterator setStart(int paramInt) {
/*  92 */     if (paramInt > this.text.length()) {
/*  93 */       throw new IndexOutOfBoundsException();
/*     */     }
/*  95 */     this.start = paramInt;
/*  96 */     this.end = nextDelimiter(this.start);
/*  97 */     this.token = this.text.substring(this.start, this.end);
/*  98 */     this.done = false;
/*  99 */     return this;
/*     */   }
/*     */ 
/*     */   public StringTokenIterator setText(String paramString) {
/* 103 */     this.text = paramString;
/* 104 */     setStart(0);
/* 105 */     return this;
/*     */   }
/*     */ 
/*     */   private int nextDelimiter(int paramInt) {
/* 109 */     int i = this.text.length();
/*     */     int j;
/* 110 */     if (this.dlms == null) {
/* 111 */       for (j = paramInt; j < i; j++)
/* 112 */         if (this.text.charAt(j) == this.delimiterChar)
/* 113 */           return j;
/*     */     }
/*     */     else
/*     */     {
/* 117 */       j = this.dlms.length();
/* 118 */       for (int k = paramInt; k < i; k++) {
/* 119 */         int m = this.text.charAt(k);
/* 120 */         for (int n = 0; n < j; n++) {
/* 121 */           if (m == this.dlms.charAt(n)) {
/* 122 */             return k;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 127 */     return i;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.util.locale.StringTokenIterator
 * JD-Core Version:    0.6.2
 */