/*     */ package sun.text.normalizer;
/*     */ 
/*     */ import java.text.ParsePosition;
/*     */ 
/*     */ public class RuleCharacterIterator
/*     */ {
/*     */   private String text;
/*     */   private ParsePosition pos;
/*     */   private SymbolTable sym;
/*     */   private char[] buf;
/*     */   private int bufPos;
/*     */   private boolean isEscaped;
/*     */   public static final int DONE = -1;
/*     */   public static final int PARSE_VARIABLES = 1;
/*     */   public static final int PARSE_ESCAPES = 2;
/*     */   public static final int SKIP_WHITESPACE = 4;
/*     */ 
/*     */   public RuleCharacterIterator(String paramString, SymbolTable paramSymbolTable, ParsePosition paramParsePosition)
/*     */   {
/* 137 */     if ((paramString == null) || (paramParsePosition.getIndex() > paramString.length())) {
/* 138 */       throw new IllegalArgumentException();
/*     */     }
/* 140 */     this.text = paramString;
/* 141 */     this.sym = paramSymbolTable;
/* 142 */     this.pos = paramParsePosition;
/* 143 */     this.buf = null;
/*     */   }
/*     */ 
/*     */   public boolean atEnd()
/*     */   {
/* 150 */     return (this.buf == null) && (this.pos.getIndex() == this.text.length());
/*     */   }
/*     */ 
/*     */   public int next(int paramInt)
/*     */   {
/* 162 */     int i = -1;
/* 163 */     this.isEscaped = false;
/*     */     Object localObject;
/*     */     do {
/*     */       while (true)
/*     */       {
/* 166 */         i = _current();
/* 167 */         _advance(UTF16.getCharCount(i));
/*     */ 
/* 169 */         if ((i != 36) || (this.buf != null) || ((paramInt & 0x1) == 0) || (this.sym == null))
/*     */           break;
/* 171 */         localObject = this.sym.parseReference(this.text, this.pos, this.text.length());
/*     */ 
/* 174 */         if (localObject == null) {
/*     */           break label221;
/*     */         }
/* 177 */         this.bufPos = 0;
/* 178 */         this.buf = this.sym.lookup((String)localObject);
/* 179 */         if (this.buf == null) {
/* 180 */           throw new IllegalArgumentException("Undefined variable: " + (String)localObject);
/*     */         }
/*     */ 
/* 184 */         if (this.buf.length == 0) {
/* 185 */           this.buf = null;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 190 */     while (((paramInt & 0x4) != 0) && (UCharacterProperty.isRuleWhiteSpace(i)));
/*     */ 
/* 195 */     if ((i == 92) && ((paramInt & 0x2) != 0)) {
/* 196 */       localObject = new int[] { 0 };
/* 197 */       i = Utility.unescapeAt(lookahead(), (int[])localObject);
/* 198 */       jumpahead(localObject[0]);
/* 199 */       this.isEscaped = true;
/* 200 */       if (i < 0) {
/* 201 */         throw new IllegalArgumentException("Invalid escape");
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 208 */     label221: return i;
/*     */   }
/*     */ 
/*     */   public boolean isEscaped()
/*     */   {
/* 218 */     return this.isEscaped;
/*     */   }
/*     */ 
/*     */   public boolean inVariable()
/*     */   {
/* 225 */     return this.buf != null;
/*     */   }
/*     */ 
/*     */   public Object getPos(Object paramObject)
/*     */   {
/* 248 */     if (paramObject == null) {
/* 249 */       return new Object[] { this.buf, { this.pos.getIndex(), this.bufPos } };
/*     */     }
/* 251 */     Object[] arrayOfObject = (Object[])paramObject;
/* 252 */     arrayOfObject[0] = this.buf;
/* 253 */     int[] arrayOfInt = (int[])arrayOfObject[1];
/* 254 */     arrayOfInt[0] = this.pos.getIndex();
/* 255 */     arrayOfInt[1] = this.bufPos;
/* 256 */     return paramObject;
/*     */   }
/*     */ 
/*     */   public void setPos(Object paramObject)
/*     */   {
/* 265 */     Object[] arrayOfObject = (Object[])paramObject;
/* 266 */     this.buf = ((char[])arrayOfObject[0]);
/* 267 */     int[] arrayOfInt = (int[])arrayOfObject[1];
/* 268 */     this.pos.setIndex(arrayOfInt[0]);
/* 269 */     this.bufPos = arrayOfInt[1];
/*     */   }
/*     */ 
/*     */   public void skipIgnored(int paramInt)
/*     */   {
/* 281 */     if ((paramInt & 0x4) != 0)
/*     */       while (true) {
/* 283 */         int i = _current();
/* 284 */         if (!UCharacterProperty.isRuleWhiteSpace(i)) break;
/* 285 */         _advance(UTF16.getCharCount(i));
/*     */       }
/*     */   }
/*     */ 
/*     */   public String lookahead()
/*     */   {
/* 303 */     if (this.buf != null) {
/* 304 */       return new String(this.buf, this.bufPos, this.buf.length - this.bufPos);
/*     */     }
/* 306 */     return this.text.substring(this.pos.getIndex());
/*     */   }
/*     */ 
/*     */   public void jumpahead(int paramInt)
/*     */   {
/* 316 */     if (paramInt < 0) {
/* 317 */       throw new IllegalArgumentException();
/*     */     }
/* 319 */     if (this.buf != null) {
/* 320 */       this.bufPos += paramInt;
/* 321 */       if (this.bufPos > this.buf.length) {
/* 322 */         throw new IllegalArgumentException();
/*     */       }
/* 324 */       if (this.bufPos == this.buf.length)
/* 325 */         this.buf = null;
/*     */     }
/*     */     else {
/* 328 */       int i = this.pos.getIndex() + paramInt;
/* 329 */       this.pos.setIndex(i);
/* 330 */       if (i > this.text.length())
/* 331 */         throw new IllegalArgumentException();
/*     */     }
/*     */   }
/*     */ 
/*     */   private int _current()
/*     */   {
/* 342 */     if (this.buf != null) {
/* 343 */       return UTF16.charAt(this.buf, 0, this.buf.length, this.bufPos);
/*     */     }
/* 345 */     int i = this.pos.getIndex();
/* 346 */     return i < this.text.length() ? UTF16.charAt(this.text, i) : -1;
/*     */   }
/*     */ 
/*     */   private void _advance(int paramInt)
/*     */   {
/* 355 */     if (this.buf != null) {
/* 356 */       this.bufPos += paramInt;
/* 357 */       if (this.bufPos == this.buf.length)
/* 358 */         this.buf = null;
/*     */     }
/*     */     else {
/* 361 */       this.pos.setIndex(this.pos.getIndex() + paramInt);
/* 362 */       if (this.pos.getIndex() > this.text.length())
/* 363 */         this.pos.setIndex(this.text.length());
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.text.normalizer.RuleCharacterIterator
 * JD-Core Version:    0.6.2
 */