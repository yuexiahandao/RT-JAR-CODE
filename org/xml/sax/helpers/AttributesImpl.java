/*     */ package org.xml.sax.helpers;
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
/*     */   public int getIndex(String qName)
/*     */   {
/* 244 */     int max = this.length * 5;
/* 245 */     for (int i = 0; i < max; i += 5) {
/* 246 */       if (this.data[(i + 2)].equals(qName)) {
/* 247 */         return i / 5;
/*     */       }
/*     */     }
/* 250 */     return -1;
/*     */   }
/*     */ 
/*     */   public String getType(String uri, String localName)
/*     */   {
/* 266 */     int max = this.length * 5;
/* 267 */     for (int i = 0; i < max; i += 5) {
/* 268 */       if ((this.data[i].equals(uri)) && (this.data[(i + 1)].equals(localName))) {
/* 269 */         return this.data[(i + 3)];
/*     */       }
/*     */     }
/* 272 */     return null;
/*     */   }
/*     */ 
/*     */   public String getType(String qName)
/*     */   {
/* 286 */     int max = this.length * 5;
/* 287 */     for (int i = 0; i < max; i += 5) {
/* 288 */       if (this.data[(i + 2)].equals(qName)) {
/* 289 */         return this.data[(i + 3)];
/*     */       }
/*     */     }
/* 292 */     return null;
/*     */   }
/*     */ 
/*     */   public String getValue(String uri, String localName)
/*     */   {
/* 308 */     int max = this.length * 5;
/* 309 */     for (int i = 0; i < max; i += 5) {
/* 310 */       if ((this.data[i].equals(uri)) && (this.data[(i + 1)].equals(localName))) {
/* 311 */         return this.data[(i + 4)];
/*     */       }
/*     */     }
/* 314 */     return null;
/*     */   }
/*     */ 
/*     */   public String getValue(String qName)
/*     */   {
/* 328 */     int max = this.length * 5;
/* 329 */     for (int i = 0; i < max; i += 5) {
/* 330 */       if (this.data[(i + 2)].equals(qName)) {
/* 331 */         return this.data[(i + 4)];
/*     */       }
/*     */     }
/* 334 */     return null;
/*     */   }
/*     */ 
/*     */   public void clear()
/*     */   {
/* 353 */     if (this.data != null) {
/* 354 */       for (int i = 0; i < this.length * 5; i++)
/* 355 */         this.data[i] = null;
/*     */     }
/* 357 */     this.length = 0;
/*     */   }
/*     */ 
/*     */   public void setAttributes(Attributes atts)
/*     */   {
/* 371 */     clear();
/* 372 */     this.length = atts.getLength();
/* 373 */     if (this.length > 0) {
/* 374 */       this.data = new String[this.length * 5];
/* 375 */       for (int i = 0; i < this.length; i++) {
/* 376 */         this.data[(i * 5)] = atts.getURI(i);
/* 377 */         this.data[(i * 5 + 1)] = atts.getLocalName(i);
/* 378 */         this.data[(i * 5 + 2)] = atts.getQName(i);
/* 379 */         this.data[(i * 5 + 3)] = atts.getType(i);
/* 380 */         this.data[(i * 5 + 4)] = atts.getValue(i);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void addAttribute(String uri, String localName, String qName, String type, String value)
/*     */   {
/* 406 */     ensureCapacity(this.length + 1);
/* 407 */     this.data[(this.length * 5)] = uri;
/* 408 */     this.data[(this.length * 5 + 1)] = localName;
/* 409 */     this.data[(this.length * 5 + 2)] = qName;
/* 410 */     this.data[(this.length * 5 + 3)] = type;
/* 411 */     this.data[(this.length * 5 + 4)] = value;
/* 412 */     this.length += 1;
/*     */   }
/*     */ 
/*     */   public void setAttribute(int index, String uri, String localName, String qName, String type, String value)
/*     */   {
/* 440 */     if ((index >= 0) && (index < this.length)) {
/* 441 */       this.data[(index * 5)] = uri;
/* 442 */       this.data[(index * 5 + 1)] = localName;
/* 443 */       this.data[(index * 5 + 2)] = qName;
/* 444 */       this.data[(index * 5 + 3)] = type;
/* 445 */       this.data[(index * 5 + 4)] = value;
/*     */     } else {
/* 447 */       badIndex(index);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void removeAttribute(int index)
/*     */   {
/* 462 */     if ((index >= 0) && (index < this.length)) {
/* 463 */       if (index < this.length - 1) {
/* 464 */         System.arraycopy(this.data, (index + 1) * 5, this.data, index * 5, (this.length - index - 1) * 5);
/*     */       }
/*     */ 
/* 467 */       index = (this.length - 1) * 5;
/* 468 */       this.data[(index++)] = null;
/* 469 */       this.data[(index++)] = null;
/* 470 */       this.data[(index++)] = null;
/* 471 */       this.data[(index++)] = null;
/* 472 */       this.data[index] = null;
/* 473 */       this.length -= 1;
/*     */     } else {
/* 475 */       badIndex(index);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setURI(int index, String uri)
/*     */   {
/* 492 */     if ((index >= 0) && (index < this.length))
/* 493 */       this.data[(index * 5)] = uri;
/*     */     else
/* 495 */       badIndex(index);
/*     */   }
/*     */ 
/*     */   public void setLocalName(int index, String localName)
/*     */   {
/* 512 */     if ((index >= 0) && (index < this.length))
/* 513 */       this.data[(index * 5 + 1)] = localName;
/*     */     else
/* 515 */       badIndex(index);
/*     */   }
/*     */ 
/*     */   public void setQName(int index, String qName)
/*     */   {
/* 532 */     if ((index >= 0) && (index < this.length))
/* 533 */       this.data[(index * 5 + 2)] = qName;
/*     */     else
/* 535 */       badIndex(index);
/*     */   }
/*     */ 
/*     */   public void setType(int index, String type)
/*     */   {
/* 551 */     if ((index >= 0) && (index < this.length))
/* 552 */       this.data[(index * 5 + 3)] = type;
/*     */     else
/* 554 */       badIndex(index);
/*     */   }
/*     */ 
/*     */   public void setValue(int index, String value)
/*     */   {
/* 570 */     if ((index >= 0) && (index < this.length))
/* 571 */       this.data[(index * 5 + 4)] = value;
/*     */     else
/* 573 */       badIndex(index);
/*     */   }
/*     */ 
/*     */   private void ensureCapacity(int n)
/*     */   {
/* 591 */     if (n <= 0)
/*     */       return;
/*     */     int max;
/*     */     int max;
/* 595 */     if ((this.data == null) || (this.data.length == 0)) {
/* 596 */       max = 25;
/*     */     } else {
/* 598 */       if (this.data.length >= n * 5) {
/* 599 */         return;
/*     */       }
/*     */ 
/* 602 */       max = this.data.length;
/*     */     }
/* 604 */     while (max < n * 5) {
/* 605 */       max *= 2;
/*     */     }
/*     */ 
/* 608 */     String[] newData = new String[max];
/* 609 */     if (this.length > 0) {
/* 610 */       System.arraycopy(this.data, 0, newData, 0, this.length * 5);
/*     */     }
/* 612 */     this.data = newData;
/*     */   }
/*     */ 
/*     */   private void badIndex(int index)
/*     */     throws ArrayIndexOutOfBoundsException
/*     */   {
/* 625 */     String msg = "Attempt to modify attribute at illegal index: " + index;
/*     */ 
/* 627 */     throw new ArrayIndexOutOfBoundsException(msg);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.xml.sax.helpers.AttributesImpl
 * JD-Core Version:    0.6.2
 */