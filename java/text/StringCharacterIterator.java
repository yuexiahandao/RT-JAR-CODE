/*     */ package java.text;
/*     */ 
/*     */ public final class StringCharacterIterator
/*     */   implements CharacterIterator
/*     */ {
/*     */   private String text;
/*     */   private int begin;
/*     */   private int end;
/*     */   private int pos;
/*     */ 
/*     */   public StringCharacterIterator(String paramString)
/*     */   {
/*  65 */     this(paramString, 0);
/*     */   }
/*     */ 
/*     */   public StringCharacterIterator(String paramString, int paramInt)
/*     */   {
/*  76 */     this(paramString, 0, paramString.length(), paramInt);
/*     */   }
/*     */ 
/*     */   public StringCharacterIterator(String paramString, int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/*  89 */     if (paramString == null)
/*  90 */       throw new NullPointerException();
/*  91 */     this.text = paramString;
/*     */ 
/*  93 */     if ((paramInt1 < 0) || (paramInt1 > paramInt2) || (paramInt2 > paramString.length())) {
/*  94 */       throw new IllegalArgumentException("Invalid substring range");
/*     */     }
/*  96 */     if ((paramInt3 < paramInt1) || (paramInt3 > paramInt2)) {
/*  97 */       throw new IllegalArgumentException("Invalid position");
/*     */     }
/*  99 */     this.begin = paramInt1;
/* 100 */     this.end = paramInt2;
/* 101 */     this.pos = paramInt3;
/*     */   }
/*     */ 
/*     */   public void setText(String paramString)
/*     */   {
/* 114 */     if (paramString == null)
/* 115 */       throw new NullPointerException();
/* 116 */     this.text = paramString;
/* 117 */     this.begin = 0;
/* 118 */     this.end = paramString.length();
/* 119 */     this.pos = 0;
/*     */   }
/*     */ 
/*     */   public char first()
/*     */   {
/* 128 */     this.pos = this.begin;
/* 129 */     return current();
/*     */   }
/*     */ 
/*     */   public char last()
/*     */   {
/* 138 */     if (this.end != this.begin)
/* 139 */       this.pos = (this.end - 1);
/*     */     else {
/* 141 */       this.pos = this.end;
/*     */     }
/* 143 */     return current();
/*     */   }
/*     */ 
/*     */   public char setIndex(int paramInt)
/*     */   {
/* 152 */     if ((paramInt < this.begin) || (paramInt > this.end))
/* 153 */       throw new IllegalArgumentException("Invalid index");
/* 154 */     this.pos = paramInt;
/* 155 */     return current();
/*     */   }
/*     */ 
/*     */   public char current()
/*     */   {
/* 164 */     if ((this.pos >= this.begin) && (this.pos < this.end)) {
/* 165 */       return this.text.charAt(this.pos);
/*     */     }
/*     */ 
/* 168 */     return 65535;
/*     */   }
/*     */ 
/*     */   public char next()
/*     */   {
/* 178 */     if (this.pos < this.end - 1) {
/* 179 */       this.pos += 1;
/* 180 */       return this.text.charAt(this.pos);
/*     */     }
/*     */ 
/* 183 */     this.pos = this.end;
/* 184 */     return 65535;
/*     */   }
/*     */ 
/*     */   public char previous()
/*     */   {
/* 194 */     if (this.pos > this.begin) {
/* 195 */       this.pos -= 1;
/* 196 */       return this.text.charAt(this.pos);
/*     */     }
/*     */ 
/* 199 */     return 65535;
/*     */   }
/*     */ 
/*     */   public int getBeginIndex()
/*     */   {
/* 209 */     return this.begin;
/*     */   }
/*     */ 
/*     */   public int getEndIndex()
/*     */   {
/* 218 */     return this.end;
/*     */   }
/*     */ 
/*     */   public int getIndex()
/*     */   {
/* 227 */     return this.pos;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 238 */     if (this == paramObject)
/* 239 */       return true;
/* 240 */     if (!(paramObject instanceof StringCharacterIterator)) {
/* 241 */       return false;
/*     */     }
/* 243 */     StringCharacterIterator localStringCharacterIterator = (StringCharacterIterator)paramObject;
/*     */ 
/* 245 */     if (hashCode() != localStringCharacterIterator.hashCode())
/* 246 */       return false;
/* 247 */     if (!this.text.equals(localStringCharacterIterator.text))
/* 248 */       return false;
/* 249 */     if ((this.pos != localStringCharacterIterator.pos) || (this.begin != localStringCharacterIterator.begin) || (this.end != localStringCharacterIterator.end))
/* 250 */       return false;
/* 251 */     return true;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 260 */     return this.text.hashCode() ^ this.pos ^ this.begin ^ this.end;
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/*     */     try
/*     */     {
/* 270 */       return (StringCharacterIterator)super.clone();
/*     */     }
/*     */     catch (CloneNotSupportedException localCloneNotSupportedException)
/*     */     {
/*     */     }
/* 275 */     throw new InternalError();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.text.StringCharacterIterator
 * JD-Core Version:    0.6.2
 */