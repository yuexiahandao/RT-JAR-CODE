/*     */ package sun.net.www;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class MessageHeader
/*     */ {
/*     */   private String[] keys;
/*     */   private String[] values;
/*     */   private int nkeys;
/*     */ 
/*     */   public MessageHeader()
/*     */   {
/*  56 */     grow();
/*     */   }
/*     */ 
/*     */   public MessageHeader(InputStream paramInputStream) throws IOException {
/*  60 */     parseHeader(paramInputStream);
/*     */   }
/*     */ 
/*     */   public synchronized void reset()
/*     */   {
/*  67 */     this.keys = null;
/*  68 */     this.values = null;
/*  69 */     this.nkeys = 0;
/*  70 */     grow();
/*     */   }
/*     */ 
/*     */   public synchronized String findValue(String paramString)
/*     */   {
/*     */     int i;
/*  80 */     if (paramString == null) {
/*  81 */       i = this.nkeys;
/*     */       do { i--; if (i < 0) break; }
/*  82 */       while (this.keys[i] != null);
/*  83 */       return this.values[i];
/*     */     } else {
/*  85 */       i = this.nkeys;
/*     */       do { i--; if (i < 0) break; }
/*  86 */       while (!paramString.equalsIgnoreCase(this.keys[i]));
/*  87 */       return this.values[i];
/*     */     }
/*  89 */     return null;
/*     */   }
/*     */ 
/*     */   public synchronized int getKey(String paramString)
/*     */   {
/*  94 */     int i = this.nkeys;
/*     */     do { i--; if (i < 0) break; }
/*  95 */     while ((this.keys[i] != paramString) && ((paramString == null) || (!paramString.equalsIgnoreCase(this.keys[i]))));
/*     */ 
/*  97 */     return i;
/*  98 */     return -1;
/*     */   }
/*     */ 
/*     */   public synchronized String getKey(int paramInt) {
/* 102 */     if ((paramInt < 0) || (paramInt >= this.nkeys)) return null;
/* 103 */     return this.keys[paramInt];
/*     */   }
/*     */ 
/*     */   public synchronized String getValue(int paramInt) {
/* 107 */     if ((paramInt < 0) || (paramInt >= this.nkeys)) return null;
/* 108 */     return this.values[paramInt];
/*     */   }
/*     */ 
/*     */   public synchronized String findNextValue(String paramString1, String paramString2)
/*     */   {
/* 123 */     int i = 0;
/*     */     int j;
/* 124 */     if (paramString1 == null) {
/* 125 */       j = this.nkeys;
/*     */       while (true) { j--; if (j < 0) break;
/* 126 */         if (this.keys[j] == null) {
/* 127 */           if (i != 0)
/* 128 */             return this.values[j];
/* 129 */           if (this.values[j] == paramString2)
/* 130 */             i = 1; 
/*     */         } } 
/*     */     } else {
/* 132 */       j = this.nkeys;
/*     */       while (true) { j--; if (j < 0) break;
/* 133 */         if (paramString1.equalsIgnoreCase(this.keys[j])) {
/* 134 */           if (i != 0)
/* 135 */             return this.values[j];
/* 136 */           if (this.values[j] == paramString2)
/* 137 */             i = 1;  }  } 
/*     */     }
/* 138 */     return null;
/*     */   }
/*     */ 
/*     */   public boolean filterNTLMResponses(String paramString)
/*     */   {
/* 147 */     int i = 0;
/* 148 */     for (int j = 0; j < this.nkeys; j++) {
/* 149 */       if ((paramString.equalsIgnoreCase(this.keys[j])) && (this.values[j] != null) && (this.values[j].length() > 5) && (this.values[j].regionMatches(true, 0, "NTLM ", 0, 5)))
/*     */       {
/* 152 */         i = 1;
/* 153 */         break;
/*     */       }
/*     */     }
/* 156 */     if (i != 0) {
/* 157 */       j = 0;
/* 158 */       for (int k = 0; k < this.nkeys; k++)
/* 159 */         if ((!paramString.equalsIgnoreCase(this.keys[k])) || ((!"Negotiate".equalsIgnoreCase(this.values[k])) && (!"Kerberos".equalsIgnoreCase(this.values[k]))))
/*     */         {
/* 164 */           if (k != j) {
/* 165 */             this.keys[j] = this.keys[k];
/* 166 */             this.values[j] = this.values[k];
/*     */           }
/* 168 */           j++;
/*     */         }
/* 170 */       if (j != this.nkeys) {
/* 171 */         this.nkeys = j;
/* 172 */         return true;
/*     */       }
/*     */     }
/* 175 */     return false;
/*     */   }
/*     */ 
/*     */   public Iterator<String> multiValueIterator(String paramString)
/*     */   {
/* 228 */     return new HeaderIterator(paramString, this);
/*     */   }
/*     */ 
/*     */   public synchronized Map<String, List<String>> getHeaders() {
/* 232 */     return getHeaders(null);
/*     */   }
/*     */ 
/*     */   public synchronized Map<String, List<String>> getHeaders(String[] paramArrayOfString) {
/* 236 */     return filterAndAddHeaders(paramArrayOfString, null); } 
/* 240 */   public synchronized Map<String, List<String>> filterAndAddHeaders(String[] paramArrayOfString, Map<String, List<String>> paramMap) { int i = 0;
/* 241 */     HashMap localHashMap = new HashMap();
/* 242 */     int j = this.nkeys;
/*     */     Object localObject1;
/*     */     while (true) { j--; if (j < 0) break;
/* 243 */       if (paramArrayOfString != null)
/*     */       {
/* 246 */         for (int k = 0; k < paramArrayOfString.length; k++) {
/* 247 */           if ((paramArrayOfString[k] != null) && (paramArrayOfString[k].equalsIgnoreCase(this.keys[j])))
/*     */           {
/* 249 */             i = 1;
/* 250 */             break;
/*     */           }
/*     */         }
/*     */       }
/* 254 */       if (i == 0) {
/* 255 */         localObject1 = (List)localHashMap.get(this.keys[j]);
/* 256 */         if (localObject1 == null) {
/* 257 */           localObject1 = new ArrayList();
/* 258 */           localHashMap.put(this.keys[j], localObject1);
/*     */         }
/* 260 */         ((List)localObject1).add(this.values[j]);
/*     */       }
/*     */       else {
/* 263 */         i = 0;
/*     */       }
/*     */     }
/*     */ 
/* 267 */     if (paramMap != null) {
/* 268 */       localIterator = paramMap.entrySet().iterator();
/* 269 */       while (localIterator.hasNext()) {
/* 270 */         localObject1 = (Map.Entry)localIterator.next();
/* 271 */         Object localObject2 = (List)localHashMap.get(((Map.Entry)localObject1).getKey());
/* 272 */         if (localObject2 == null) {
/* 273 */           localObject2 = new ArrayList();
/* 274 */           localHashMap.put((String)((Map.Entry)localObject1).getKey(), localObject2);
/*     */         }
/* 276 */         ((List)localObject2).add(((Map.Entry)localObject1).getValue());
/*     */       }
/*     */     }
/*     */ 
/* 280 */     for (Iterator localIterator = localHashMap.keySet().iterator(); localIterator.hasNext(); ) { localObject1 = (String)localIterator.next();
/* 281 */       localHashMap.put(localObject1, Collections.unmodifiableList((List)localHashMap.get(localObject1)));
/*     */     }
/*     */ 
/* 284 */     return Collections.unmodifiableMap(localHashMap);
/*     */   }
/*     */ 
/*     */   public synchronized void print(PrintStream paramPrintStream)
/*     */   {
/* 291 */     for (int i = 0; i < this.nkeys; i++) {
/* 292 */       if (this.keys[i] != null) {
/* 293 */         paramPrintStream.print(this.keys[i] + (this.values[i] != null ? ": " + this.values[i] : "") + "\r\n");
/*     */       }
/*     */     }
/* 296 */     paramPrintStream.print("\r\n");
/* 297 */     paramPrintStream.flush();
/*     */   }
/*     */ 
/*     */   public synchronized void add(String paramString1, String paramString2)
/*     */   {
/* 303 */     grow();
/* 304 */     this.keys[this.nkeys] = paramString1;
/* 305 */     this.values[this.nkeys] = paramString2;
/* 306 */     this.nkeys += 1;
/*     */   }
/*     */ 
/*     */   public synchronized void prepend(String paramString1, String paramString2)
/*     */   {
/* 312 */     grow();
/* 313 */     for (int i = this.nkeys; i > 0; i--) {
/* 314 */       this.keys[i] = this.keys[(i - 1)];
/* 315 */       this.values[i] = this.values[(i - 1)];
/*     */     }
/* 317 */     this.keys[0] = paramString1;
/* 318 */     this.values[0] = paramString2;
/* 319 */     this.nkeys += 1;
/*     */   }
/*     */ 
/*     */   public synchronized void set(int paramInt, String paramString1, String paramString2)
/*     */   {
/* 328 */     grow();
/* 329 */     if (paramInt < 0)
/* 330 */       return;
/* 331 */     if (paramInt >= this.nkeys) {
/* 332 */       add(paramString1, paramString2);
/*     */     } else {
/* 334 */       this.keys[paramInt] = paramString1;
/* 335 */       this.values[paramInt] = paramString2;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void grow()
/*     */   {
/* 343 */     if ((this.keys == null) || (this.nkeys >= this.keys.length)) {
/* 344 */       String[] arrayOfString1 = new String[this.nkeys + 4];
/* 345 */       String[] arrayOfString2 = new String[this.nkeys + 4];
/* 346 */       if (this.keys != null)
/* 347 */         System.arraycopy(this.keys, 0, arrayOfString1, 0, this.nkeys);
/* 348 */       if (this.values != null)
/* 349 */         System.arraycopy(this.values, 0, arrayOfString2, 0, this.nkeys);
/* 350 */       this.keys = arrayOfString1;
/* 351 */       this.values = arrayOfString2;
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized void remove(String paramString)
/*     */   {
/*     */     int i;
/*     */     int j;
/* 363 */     if (paramString == null) {
/* 364 */       for (i = 0; i < this.nkeys; i++)
/* 365 */         while ((this.keys[i] == null) && (i < this.nkeys)) {
/* 366 */           for (j = i; j < this.nkeys - 1; j++) {
/* 367 */             this.keys[j] = this.keys[(j + 1)];
/* 368 */             this.values[j] = this.values[(j + 1)];
/*     */           }
/* 370 */           this.nkeys -= 1;
/*     */         }
/*     */     }
/*     */     else
/* 374 */       for (i = 0; i < this.nkeys; i++)
/* 375 */         while ((paramString.equalsIgnoreCase(this.keys[i])) && (i < this.nkeys)) {
/* 376 */           for (j = i; j < this.nkeys - 1; j++) {
/* 377 */             this.keys[j] = this.keys[(j + 1)];
/* 378 */             this.values[j] = this.values[(j + 1)];
/*     */           }
/* 380 */           this.nkeys -= 1;
/*     */         }
/*     */   }
/*     */ 
/*     */   public synchronized void set(String paramString1, String paramString2)
/*     */   {
/* 391 */     int i = this.nkeys;
/*     */     do { i--; if (i < 0) break; }
/* 392 */     while (!paramString1.equalsIgnoreCase(this.keys[i]));
/* 393 */     this.values[i] = paramString2;
/* 394 */     return;
/*     */ 
/* 396 */     add(paramString1, paramString2);
/*     */   }
/*     */ 
/*     */   public synchronized void setIfNotSet(String paramString1, String paramString2)
/*     */   {
/* 404 */     if (findValue(paramString1) == null)
/* 405 */       add(paramString1, paramString2);
/*     */   }
/*     */ 
/*     */   public static String canonicalID(String paramString)
/*     */   {
/* 412 */     if (paramString == null)
/* 413 */       return "";
/* 414 */     int i = 0;
/* 415 */     int j = paramString.length();
/* 416 */     int k = 0;
/*     */     int m;
/* 418 */     while ((i < j) && (((m = paramString.charAt(i)) == '<') || (m <= 32)))
/*     */     {
/* 420 */       i++;
/* 421 */       k = 1;
/*     */     }
/* 423 */     while ((i < j) && (((m = paramString.charAt(j - 1)) == '>') || (m <= 32)))
/*     */     {
/* 425 */       j--;
/* 426 */       k = 1;
/*     */     }
/* 428 */     return k != 0 ? paramString.substring(i, j) : paramString;
/*     */   }
/*     */ 
/*     */   public void parseHeader(InputStream paramInputStream) throws IOException
/*     */   {
/* 433 */     synchronized (this) {
/* 434 */       this.nkeys = 0;
/*     */     }
/* 436 */     mergeHeader(paramInputStream);
/*     */   }
/*     */ 
/*     */   public void mergeHeader(InputStream paramInputStream) throws IOException
/*     */   {
/* 441 */     if (paramInputStream == null)
/* 442 */       return;
/* 443 */     Object localObject1 = new char[10];
/* 444 */     int i = paramInputStream.read();
/* 445 */     while ((i != 10) && (i != 13) && (i >= 0)) {
/* 446 */       int j = 0;
/* 447 */       int k = -1;
/*     */ 
/* 449 */       int n = i > 32 ? 1 : 0;
/* 450 */       localObject1[(j++)] = ((char)i);
/*     */       int m;
/*     */       Object localObject2;
/* 452 */       while ((m = paramInputStream.read()) >= 0) {
/* 453 */         switch (m) {
/*     */         case 58:
/* 455 */           if ((n != 0) && (j > 0))
/* 456 */             k = j;
/* 457 */           n = 0;
/* 458 */           break;
/*     */         case 9:
/* 460 */           m = 32;
/*     */         case 32:
/* 462 */           n = 0;
/* 463 */           break;
/*     */         case 10:
/*     */         case 13:
/* 466 */           i = paramInputStream.read();
/* 467 */           if ((m == 13) && (i == 10)) {
/* 468 */             i = paramInputStream.read();
/* 469 */             if (i == 13)
/* 470 */               i = paramInputStream.read();
/*     */           }
/* 472 */           if ((i == 10) || (i == 13) || (i > 32)) {
/*     */             break label252;
/*     */           }
/* 475 */           m = 32;
/*     */         }
/*     */ 
/* 478 */         if (j >= localObject1.length) {
/* 479 */           localObject2 = new char[localObject1.length * 2];
/* 480 */           System.arraycopy(localObject1, 0, localObject2, 0, j);
/* 481 */           localObject1 = localObject2;
/*     */         }
/* 483 */         localObject1[(j++)] = ((char)m);
/*     */       }
/* 485 */       i = -1;
/*     */ 
/* 487 */       label252: while ((j > 0) && (localObject1[(j - 1)] <= ' ')) {
/* 488 */         j--;
/*     */       }
/* 490 */       if (k <= 0) {
/* 491 */         localObject2 = null;
/* 492 */         k = 0;
/*     */       } else {
/* 494 */         localObject2 = String.copyValueOf((char[])localObject1, 0, k);
/* 495 */         if ((k < j) && (localObject1[k] == ':'))
/* 496 */           k++;
/* 497 */         while ((k < j) && (localObject1[k] <= ' '))
/* 498 */           k++;
/*     */       }
/*     */       String str;
/* 501 */       if (k >= j)
/* 502 */         str = new String();
/*     */       else
/* 504 */         str = String.copyValueOf((char[])localObject1, k, j - k);
/* 505 */       add((String)localObject2, str);
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized String toString() {
/* 510 */     String str = super.toString() + this.nkeys + " pairs: ";
/* 511 */     for (int i = 0; (i < this.keys.length) && (i < this.nkeys); i++) {
/* 512 */       str = str + "{" + this.keys[i] + ": " + this.values[i] + "}";
/*     */     }
/* 514 */     return str;
/*     */   }
/*     */ 
/*     */   class HeaderIterator
/*     */     implements Iterator<String>
/*     */   {
/* 179 */     int index = 0;
/* 180 */     int next = -1;
/*     */     String key;
/* 182 */     boolean haveNext = false;
/*     */     Object lock;
/*     */ 
/*     */     public HeaderIterator(String paramObject, Object arg3)
/*     */     {
/* 186 */       this.key = paramObject;
/*     */       Object localObject;
/* 187 */       this.lock = localObject;
/*     */     }
/*     */     public boolean hasNext() {
/* 190 */       synchronized (this.lock) {
/* 191 */         if (this.haveNext) {
/* 192 */           return true;
/*     */         }
/* 194 */         while (this.index < MessageHeader.this.nkeys) {
/* 195 */           if (this.key.equalsIgnoreCase(MessageHeader.this.keys[this.index])) {
/* 196 */             this.haveNext = true;
/* 197 */             this.next = (this.index++);
/* 198 */             return true;
/*     */           }
/* 200 */           this.index += 1;
/*     */         }
/* 202 */         return false;
/*     */       }
/*     */     }
/*     */ 
/* 206 */     public String next() { synchronized (this.lock) {
/* 207 */         if (this.haveNext) {
/* 208 */           this.haveNext = false;
/* 209 */           return MessageHeader.this.values[this.next];
/*     */         }
/* 211 */         if (hasNext()) {
/* 212 */           return next();
/*     */         }
/* 214 */         throw new NoSuchElementException("No more elements");
/*     */       } }
/*     */ 
/*     */     public void remove()
/*     */     {
/* 219 */       throw new UnsupportedOperationException("remove not allowed");
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.www.MessageHeader
 * JD-Core Version:    0.6.2
 */