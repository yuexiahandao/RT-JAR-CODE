/*     */ package com.sun.xml.internal.bind.util;
/*     */ 
/*     */ import org.xml.sax.Attributes;
/*     */ 
/*     */ public class AttributesImpl
/*     */   implements Attributes
/*     */ {
/*     */   int length;
/*     */   String[] data;
/*     */ 
/*     */   public AttributesImpl()
/*     */   {
/*  84 */     this.length = 0;
/*  85 */     this.data = null;
/*     */   }
/*     */ 
/*     */   public AttributesImpl(Attributes atts)
/*     */   {
/*  99 */     setAttributes(atts);
/*     */   }
/*     */ 
/*     */   public int getLength()
/*     */   {
/* 117 */     return this.length;
/*     */   }
/*     */ 
/*     */   public String getURI(int index)
/*     */   {
/* 131 */     if ((index >= 0) && (index < this.length)) {
/* 132 */       return this.data[(index * 5)];
/*     */     }
/* 134 */     return null;
/*     */   }
/*     */ 
/*     */   public String getLocalName(int index)
/*     */   {
/* 149 */     if ((index >= 0) && (index < this.length)) {
/* 150 */       return this.data[(index * 5 + 1)];
/*     */     }
/* 152 */     return null;
/*     */   }
/*     */ 
/*     */   public String getQName(int index)
/*     */   {
/* 167 */     if ((index >= 0) && (index < this.length)) {
/* 168 */       return this.data[(index * 5 + 2)];
/*     */     }
/* 170 */     return null;
/*     */   }
/*     */ 
/*     */   public String getType(int index)
/*     */   {
/* 185 */     if ((index >= 0) && (index < this.length)) {
/* 186 */       return this.data[(index * 5 + 3)];
/*     */     }
/* 188 */     return null;
/*     */   }
/*     */ 
/*     */   public String getValue(int index)
/*     */   {
/* 202 */     if ((index >= 0) && (index < this.length)) {
/* 203 */       return this.data[(index * 5 + 4)];
/*     */     }
/* 205 */     return null;
/*     */   }
/*     */ 
/*     */   public int getIndex(String uri, String localName)
/*     */   {
/* 225 */     int max = this.length * 5;
/* 226 */     for (int i = 0; i < max; i += 5) {
/* 227 */       if ((this.data[i].equals(uri)) && (this.data[(i + 1)].equals(localName))) {
/* 228 */         return i / 5;
/*     */       }
/*     */     }
/* 231 */     return -1;
/*     */   }
/*     */ 
/*     */   public int getIndexFast(String uri, String localName)
/*     */   {
/* 238 */     for (int i = (this.length - 1) * 5; i >= 0; i -= 5)
/*     */     {
/* 240 */       if ((this.data[(i + 1)] == localName) && (this.data[i] == uri)) {
/* 241 */         return i / 5;
/*     */       }
/*     */     }
/* 244 */     return -1;
/*     */   }
/*     */ 
/*     */   public int getIndex(String qName)
/*     */   {
/* 257 */     int max = this.length * 5;
/* 258 */     for (int i = 0; i < max; i += 5) {
/* 259 */       if (this.data[(i + 2)].equals(qName)) {
/* 260 */         return i / 5;
/*     */       }
/*     */     }
/* 263 */     return -1;
/*     */   }
/*     */ 
/*     */   public String getType(String uri, String localName)
/*     */   {
/* 279 */     int max = this.length * 5;
/* 280 */     for (int i = 0; i < max; i += 5) {
/* 281 */       if ((this.data[i].equals(uri)) && (this.data[(i + 1)].equals(localName))) {
/* 282 */         return this.data[(i + 3)];
/*     */       }
/*     */     }
/* 285 */     return null;
/*     */   }
/*     */ 
/*     */   public String getType(String qName)
/*     */   {
/* 299 */     int max = this.length * 5;
/* 300 */     for (int i = 0; i < max; i += 5) {
/* 301 */       if (this.data[(i + 2)].equals(qName)) {
/* 302 */         return this.data[(i + 3)];
/*     */       }
/*     */     }
/* 305 */     return null;
/*     */   }
/*     */ 
/*     */   public String getValue(String uri, String localName)
/*     */   {
/* 321 */     int max = this.length * 5;
/* 322 */     for (int i = 0; i < max; i += 5) {
/* 323 */       if ((this.data[i].equals(uri)) && (this.data[(i + 1)].equals(localName))) {
/* 324 */         return this.data[(i + 4)];
/*     */       }
/*     */     }
/* 327 */     return null;
/*     */   }
/*     */ 
/*     */   public String getValue(String qName)
/*     */   {
/* 341 */     int max = this.length * 5;
/* 342 */     for (int i = 0; i < max; i += 5) {
/* 343 */       if (this.data[(i + 2)].equals(qName)) {
/* 344 */         return this.data[(i + 4)];
/*     */       }
/*     */     }
/* 347 */     return null;
/*     */   }
/*     */ 
/*     */   public void clear()
/*     */   {
/* 366 */     if (this.data != null) {
/* 367 */       for (int i = 0; i < this.length * 5; i++)
/* 368 */         this.data[i] = null;
/*     */     }
/* 370 */     this.length = 0;
/*     */   }
/*     */ 
/*     */   public void setAttributes(Attributes atts)
/*     */   {
/* 384 */     clear();
/* 385 */     this.length = atts.getLength();
/* 386 */     if (this.length > 0) {
/* 387 */       this.data = new String[this.length * 5];
/* 388 */       for (int i = 0; i < this.length; i++) {
/* 389 */         this.data[(i * 5)] = atts.getURI(i);
/* 390 */         this.data[(i * 5 + 1)] = atts.getLocalName(i);
/* 391 */         this.data[(i * 5 + 2)] = atts.getQName(i);
/* 392 */         this.data[(i * 5 + 3)] = atts.getType(i);
/* 393 */         this.data[(i * 5 + 4)] = atts.getValue(i);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void addAttribute(String uri, String localName, String qName, String type, String value)
/*     */   {
/* 419 */     ensureCapacity(this.length + 1);
/* 420 */     this.data[(this.length * 5)] = uri;
/* 421 */     this.data[(this.length * 5 + 1)] = localName;
/* 422 */     this.data[(this.length * 5 + 2)] = qName;
/* 423 */     this.data[(this.length * 5 + 3)] = type;
/* 424 */     this.data[(this.length * 5 + 4)] = value;
/* 425 */     this.length += 1;
/*     */   }
/*     */ 
/*     */   public void setAttribute(int index, String uri, String localName, String qName, String type, String value)
/*     */   {
/* 453 */     if ((index >= 0) && (index < this.length)) {
/* 454 */       this.data[(index * 5)] = uri;
/* 455 */       this.data[(index * 5 + 1)] = localName;
/* 456 */       this.data[(index * 5 + 2)] = qName;
/* 457 */       this.data[(index * 5 + 3)] = type;
/* 458 */       this.data[(index * 5 + 4)] = value;
/*     */     } else {
/* 460 */       badIndex(index);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void removeAttribute(int index)
/*     */   {
/* 475 */     if ((index >= 0) && (index < this.length)) {
/* 476 */       if (index < this.length - 1) {
/* 477 */         System.arraycopy(this.data, (index + 1) * 5, this.data, index * 5, (this.length - index - 1) * 5);
/*     */       }
/*     */ 
/* 480 */       index = (this.length - 1) * 5;
/* 481 */       this.data[(index++)] = null;
/* 482 */       this.data[(index++)] = null;
/* 483 */       this.data[(index++)] = null;
/* 484 */       this.data[(index++)] = null;
/* 485 */       this.data[index] = null;
/* 486 */       this.length -= 1;
/*     */     } else {
/* 488 */       badIndex(index);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setURI(int index, String uri)
/*     */   {
/* 505 */     if ((index >= 0) && (index < this.length))
/* 506 */       this.data[(index * 5)] = uri;
/*     */     else
/* 508 */       badIndex(index);
/*     */   }
/*     */ 
/*     */   public void setLocalName(int index, String localName)
/*     */   {
/* 525 */     if ((index >= 0) && (index < this.length))
/* 526 */       this.data[(index * 5 + 1)] = localName;
/*     */     else
/* 528 */       badIndex(index);
/*     */   }
/*     */ 
/*     */   public void setQName(int index, String qName)
/*     */   {
/* 545 */     if ((index >= 0) && (index < this.length))
/* 546 */       this.data[(index * 5 + 2)] = qName;
/*     */     else
/* 548 */       badIndex(index);
/*     */   }
/*     */ 
/*     */   public void setType(int index, String type)
/*     */   {
/* 564 */     if ((index >= 0) && (index < this.length))
/* 565 */       this.data[(index * 5 + 3)] = type;
/*     */     else
/* 567 */       badIndex(index);
/*     */   }
/*     */ 
/*     */   public void setValue(int index, String value)
/*     */   {
/* 583 */     if ((index >= 0) && (index < this.length))
/* 584 */       this.data[(index * 5 + 4)] = value;
/*     */     else
/* 586 */       badIndex(index);
/*     */   }
/*     */ 
/*     */   private void ensureCapacity(int n)
/*     */   {
/* 604 */     if (n <= 0)
/*     */       return;
/*     */     int max;
/*     */     int max;
/* 608 */     if ((this.data == null) || (this.data.length == 0)) {
/* 609 */       max = 25;
/*     */     } else {
/* 611 */       if (this.data.length >= n * 5) {
/* 612 */         return;
/*     */       }
/*     */ 
/* 615 */       max = this.data.length;
/*     */     }
/* 617 */     while (max < n * 5) {
/* 618 */       max *= 2;
/*     */     }
/*     */ 
/* 621 */     String[] newData = new String[max];
/* 622 */     if (this.length > 0) {
/* 623 */       System.arraycopy(this.data, 0, newData, 0, this.length * 5);
/*     */     }
/* 625 */     this.data = newData;
/*     */   }
/*     */ 
/*     */   private void badIndex(int index)
/*     */     throws ArrayIndexOutOfBoundsException
/*     */   {
/* 638 */     String msg = "Attempt to modify attribute at illegal index: " + index;
/*     */ 
/* 640 */     throw new ArrayIndexOutOfBoundsException(msg);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.util.AttributesImpl
 * JD-Core Version:    0.6.2
 */