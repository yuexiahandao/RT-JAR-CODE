/*     */ package sun.security.jgss;
/*     */ 
/*     */ import java.util.LinkedList;
/*     */ import org.ietf.jgss.MessageProp;
/*     */ 
/*     */ public class TokenTracker
/*     */ {
/*     */   static final int MAX_INTERVALS = 5;
/*     */   private int initNumber;
/*     */   private int windowStart;
/*     */   private int expectedNumber;
/*  70 */   private int windowStartIndex = 0;
/*     */ 
/*  72 */   private LinkedList<Entry> list = new LinkedList();
/*     */ 
/*     */   public TokenTracker(int paramInt)
/*     */   {
/*  76 */     this.initNumber = paramInt;
/*  77 */     this.windowStart = paramInt;
/*  78 */     this.expectedNumber = paramInt;
/*     */ 
/*  81 */     Entry localEntry = new Entry(paramInt - 1);
/*     */ 
/*  83 */     this.list.add(localEntry);
/*     */   }
/*     */ 
/*     */   private int getIntervalIndex(int paramInt)
/*     */   {
/*  93 */     Entry localEntry = null;
/*     */ 
/*  96 */     for (int i = this.list.size() - 1; i >= 0; i--) {
/*  97 */       localEntry = (Entry)this.list.get(i);
/*  98 */       if (localEntry.compareTo(paramInt) <= 0)
/*     */         break;
/*     */     }
/* 101 */     return i;
/*     */   }
/*     */ 
/*     */   public final synchronized void getProps(int paramInt, MessageProp paramMessageProp)
/*     */   {
/* 152 */     boolean bool1 = false;
/* 153 */     boolean bool2 = false;
/* 154 */     boolean bool3 = false;
/* 155 */     boolean bool4 = false;
/*     */ 
/* 161 */     int i = getIntervalIndex(paramInt);
/* 162 */     Entry localEntry = null;
/* 163 */     if (i != -1) {
/* 164 */       localEntry = (Entry)this.list.get(i);
/*     */     }
/*     */ 
/* 168 */     if (paramInt == this.expectedNumber) {
/* 169 */       this.expectedNumber += 1;
/*     */     }
/* 173 */     else if ((localEntry != null) && (localEntry.contains(paramInt))) {
/* 174 */       bool4 = true;
/*     */     }
/* 177 */     else if (this.expectedNumber >= this.initNumber)
/*     */     {
/* 181 */       if (paramInt > this.expectedNumber)
/* 182 */         bool1 = true;
/* 183 */       else if (paramInt >= this.windowStart)
/* 184 */         bool3 = true;
/* 185 */       else if (paramInt >= this.initNumber)
/* 186 */         bool2 = true;
/*     */       else {
/* 188 */         bool1 = true;
/*     */       }
/*     */ 
/*     */     }
/* 194 */     else if (paramInt > this.expectedNumber) {
/* 195 */       if (paramInt < this.initNumber)
/* 196 */         bool1 = true;
/* 197 */       else if (this.windowStart >= this.initNumber) {
/* 198 */         if (paramInt >= this.windowStart)
/* 199 */           bool3 = true;
/*     */         else
/* 201 */           bool2 = true;
/*     */       }
/* 203 */       else bool2 = true;
/*     */     }
/* 205 */     else if (this.windowStart > this.expectedNumber)
/* 206 */       bool3 = true;
/* 207 */     else if (paramInt < this.windowStart)
/* 208 */       bool2 = true;
/*     */     else {
/* 210 */       bool3 = true;
/*     */     }
/*     */ 
/* 215 */     if ((!bool4) && (!bool2)) {
/* 216 */       add(paramInt, i);
/*     */     }
/* 218 */     if (bool1) {
/* 219 */       this.expectedNumber = (paramInt + 1);
/*     */     }
/* 221 */     paramMessageProp.setSupplementaryStates(bool4, bool2, bool3, bool1, 0, null);
/*     */   }
/*     */ 
/*     */   private void add(int paramInt1, int paramInt2)
/*     */   {
/* 237 */     Entry localEntry2 = null;
/* 238 */     Entry localEntry3 = null;
/*     */ 
/* 240 */     int i = 0;
/* 241 */     int j = 0;
/*     */ 
/* 243 */     if (paramInt2 != -1) {
/* 244 */       localEntry2 = (Entry)this.list.get(paramInt2);
/*     */ 
/* 247 */       if (paramInt1 == localEntry2.getEnd() + 1) {
/* 248 */         localEntry2.setEnd(paramInt1);
/* 249 */         i = 1;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 255 */     int k = paramInt2 + 1;
/* 256 */     if (k < this.list.size()) {
/* 257 */       localEntry3 = (Entry)this.list.get(k);
/*     */ 
/* 260 */       if (paramInt1 == localEntry3.getStart() - 1) {
/* 261 */         if (i == 0) {
/* 262 */           localEntry3.setStart(paramInt1);
/*     */         }
/*     */         else {
/* 265 */           localEntry3.setStart(localEntry2.getStart());
/* 266 */           this.list.remove(paramInt2);
/*     */ 
/* 268 */           if (this.windowStartIndex > paramInt2)
/* 269 */             this.windowStartIndex -= 1;
/*     */         }
/* 271 */         j = 1;
/*     */       }
/*     */     }
/*     */ 
/* 275 */     if ((j != 0) || (i != 0))
/*     */       return;
/*     */     Entry localEntry1;
/* 284 */     if (this.list.size() < 5) {
/* 285 */       localEntry1 = new Entry(paramInt1);
/* 286 */       if (paramInt2 < this.windowStartIndex) {
/* 287 */         this.windowStartIndex += 1;
/*     */       }
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 295 */       int m = this.windowStartIndex;
/* 296 */       if (this.windowStartIndex == this.list.size() - 1) {
/* 297 */         this.windowStartIndex = 0;
/*     */       }
/* 299 */       localEntry1 = (Entry)this.list.remove(m);
/* 300 */       this.windowStart = ((Entry)this.list.get(this.windowStartIndex)).getStart();
/* 301 */       localEntry1.setStart(paramInt1);
/* 302 */       localEntry1.setEnd(paramInt1);
/*     */ 
/* 304 */       if (paramInt2 >= m) {
/* 305 */         paramInt2--;
/*     */       }
/* 320 */       else if (m != this.windowStartIndex)
/*     */       {
/* 322 */         if (paramInt2 == -1)
/*     */         {
/* 324 */           this.windowStart = paramInt1;
/*     */         }
/*     */       }
/* 327 */       else this.windowStartIndex += 1;
/*     */ 
/*     */     }
/*     */ 
/* 335 */     this.list.add(paramInt2 + 1, localEntry1);
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 339 */     StringBuffer localStringBuffer = new StringBuffer("TokenTracker: ");
/* 340 */     localStringBuffer.append(" initNumber=").append(this.initNumber);
/* 341 */     localStringBuffer.append(" windowStart=").append(this.windowStart);
/* 342 */     localStringBuffer.append(" expectedNumber=").append(this.expectedNumber);
/* 343 */     localStringBuffer.append(" windowStartIndex=").append(this.windowStartIndex);
/* 344 */     localStringBuffer.append("\n\tIntervals are: {");
/* 345 */     for (int i = 0; i < this.list.size(); i++) {
/* 346 */       if (i != 0)
/* 347 */         localStringBuffer.append(", ");
/* 348 */       localStringBuffer.append(((Entry)this.list.get(i)).toString());
/*     */     }
/* 350 */     localStringBuffer.append('}');
/* 351 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   class Entry
/*     */   {
/*     */     private int start;
/*     */     private int end;
/*     */ 
/*     */     Entry(int arg2)
/*     */     {
/*     */       int i;
/* 365 */       this.start = i;
/* 366 */       this.end = i;
/*     */     }
/*     */ 
/*     */     final int compareTo(int paramInt)
/*     */     {
/* 375 */       if (this.start > paramInt)
/* 376 */         return 1;
/* 377 */       if (this.end < paramInt) {
/* 378 */         return -1;
/*     */       }
/* 380 */       return 0;
/*     */     }
/*     */ 
/*     */     final boolean contains(int paramInt) {
/* 384 */       return (paramInt >= this.start) && (paramInt <= this.end);
/*     */     }
/*     */ 
/*     */     final void append(int paramInt)
/*     */     {
/* 389 */       if (paramInt == this.end + 1)
/* 390 */         this.end = paramInt;
/*     */     }
/*     */ 
/*     */     final void setInterval(int paramInt1, int paramInt2) {
/* 394 */       this.start = paramInt1;
/* 395 */       this.end = paramInt2;
/*     */     }
/*     */ 
/*     */     final void setEnd(int paramInt) {
/* 399 */       this.end = paramInt;
/*     */     }
/*     */ 
/*     */     final void setStart(int paramInt) {
/* 403 */       this.start = paramInt;
/*     */     }
/*     */ 
/*     */     final int getStart() {
/* 407 */       return this.start;
/*     */     }
/*     */ 
/*     */     final int getEnd() {
/* 411 */       return this.end;
/*     */     }
/*     */ 
/*     */     public String toString() {
/* 415 */       return "[" + this.start + ", " + this.end + "]";
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.jgss.TokenTracker
 * JD-Core Version:    0.6.2
 */