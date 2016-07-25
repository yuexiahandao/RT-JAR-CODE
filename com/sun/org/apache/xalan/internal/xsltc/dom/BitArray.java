/*     */ package com.sun.org.apache.xalan.internal.xsltc.dom;
/*     */ 
/*     */ import java.io.Externalizable;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.ObjectOutput;
/*     */ 
/*     */ public class BitArray
/*     */   implements Externalizable
/*     */ {
/*     */   static final long serialVersionUID = -4876019880708377663L;
/*     */   private int[] _bits;
/*     */   private int _bitSize;
/*     */   private int _intSize;
/*     */   private int _mask;
/*  47 */   private static final int[] _masks = { -2147483648, 1073741824, 536870912, 268435456, 134217728, 67108864, 33554432, 16777216, 8388608, 4194304, 2097152, 1048576, 524288, 262144, 131072, 65536, 32768, 16384, 8192, 4096, 2048, 1024, 512, 256, 128, 64, 32, 16, 8, 4, 2, 1 };
/*     */   private static final boolean DEBUG_ASSERTIONS = false;
/* 140 */   private int _pos = 2147483647;
/* 141 */   private int _node = 0;
/* 142 */   private int _int = 0;
/* 143 */   private int _bit = 0;
/*     */ 
/* 181 */   int _first = 2147483647;
/* 182 */   int _last = -2147483648;
/*     */ 
/*     */   public BitArray()
/*     */   {
/*  63 */     this(32);
/*     */   }
/*     */ 
/*     */   public BitArray(int size) {
/*  67 */     if (size < 32) size = 32;
/*  68 */     this._bitSize = size;
/*  69 */     this._intSize = ((this._bitSize >>> 5) + 1);
/*  70 */     this._bits = new int[this._intSize + 1];
/*     */   }
/*     */ 
/*     */   public BitArray(int size, int[] bits) {
/*  74 */     if (size < 32) size = 32;
/*  75 */     this._bitSize = size;
/*  76 */     this._intSize = ((this._bitSize >>> 5) + 1);
/*  77 */     this._bits = bits;
/*     */   }
/*     */ 
/*     */   public void setMask(int mask)
/*     */   {
/*  85 */     this._mask = mask;
/*     */   }
/*     */ 
/*     */   public int getMask()
/*     */   {
/*  92 */     return this._mask;
/*     */   }
/*     */ 
/*     */   public final int size()
/*     */   {
/*  99 */     return this._bitSize;
/*     */   }
/*     */ 
/*     */   public final boolean getBit(int bit)
/*     */   {
/* 113 */     return (this._bits[(bit >>> 5)] & _masks[(bit % 32)]) != 0;
/*     */   }
/*     */ 
/*     */   public final int getNextBit(int startBit)
/*     */   {
/* 120 */     for (int i = startBit >>> 5; i <= this._intSize; i++) {
/* 121 */       int bits = this._bits[i];
/* 122 */       if (bits != 0) {
/* 123 */         for (int b = startBit % 32; b < 32; b++) {
/* 124 */           if ((bits & _masks[b]) != 0) {
/* 125 */             return (i << 5) + b;
/*     */           }
/*     */         }
/*     */       }
/* 129 */       startBit = 0;
/*     */     }
/* 131 */     return -1;
/*     */   }
/*     */ 
/*     */   public final int getBitNumber(int pos)
/*     */   {
/* 148 */     if (pos == this._pos) return this._node;
/*     */ 
/* 152 */     if (pos < this._pos);
/* 153 */     for (this._int = (this._bit = this._pos = 0); 
/* 157 */       this._int <= this._intSize; this._int += 1) {
/* 158 */       int bits = this._bits[this._int];
/* 159 */       if (bits != 0) {
/* 160 */         for (; this._bit < 32; this._bit += 1) {
/* 161 */           if (((bits & _masks[this._bit]) != 0) && 
/* 162 */             (++this._pos == pos)) {
/* 163 */             this._node = ((this._int << 5) + this._bit - 1);
/* 164 */             return this._node;
/*     */           }
/*     */         }
/*     */ 
/* 168 */         this._bit = 0;
/*     */       }
/*     */     }
/* 171 */     return 0;
/*     */   }
/*     */ 
/*     */   public final int[] data()
/*     */   {
/* 178 */     return this._bits;
/*     */   }
/*     */ 
/*     */   public final void setBit(int bit)
/*     */   {
/* 195 */     if (bit >= this._bitSize) return;
/* 196 */     int i = bit >>> 5;
/* 197 */     if (i < this._first) this._first = i;
/* 198 */     if (i > this._last) this._last = i;
/* 199 */     this._bits[i] |= _masks[(bit % 32)];
/*     */   }
/*     */ 
/*     */   public final BitArray merge(BitArray other)
/*     */   {
/* 208 */     if (this._last == -1) {
/* 209 */       this._bits = other._bits;
/*     */     }
/* 212 */     else if (other._last != -1) {
/* 213 */       int start = this._first < other._first ? this._first : other._first;
/* 214 */       int stop = this._last > other._last ? this._last : other._last;
/*     */ 
/* 217 */       if (other._intSize > this._intSize) {
/* 218 */         if (stop > this._intSize) stop = this._intSize;
/* 219 */         for (int i = start; i <= stop; i++)
/* 220 */           other._bits[i] |= this._bits[i];
/* 221 */         this._bits = other._bits;
/*     */       }
/*     */       else
/*     */       {
/* 225 */         if (stop > other._intSize) stop = other._intSize;
/* 226 */         for (int i = start; i <= stop; i++)
/* 227 */           this._bits[i] |= other._bits[i];
/*     */       }
/*     */     }
/* 230 */     return this;
/*     */   }
/*     */ 
/*     */   public final void resize(int newSize)
/*     */   {
/* 237 */     if (newSize > this._bitSize) {
/* 238 */       this._intSize = ((newSize >>> 5) + 1);
/* 239 */       int[] newBits = new int[this._intSize + 1];
/* 240 */       System.arraycopy(this._bits, 0, newBits, 0, (this._bitSize >>> 5) + 1);
/* 241 */       this._bits = newBits;
/* 242 */       this._bitSize = newSize;
/*     */     }
/*     */   }
/*     */ 
/*     */   public BitArray cloneArray() {
/* 247 */     return new BitArray(this._intSize, this._bits);
/*     */   }
/*     */ 
/*     */   public void writeExternal(ObjectOutput out) throws IOException {
/* 251 */     out.writeInt(this._bitSize);
/* 252 */     out.writeInt(this._mask);
/* 253 */     out.writeObject(this._bits);
/* 254 */     out.flush();
/*     */   }
/*     */ 
/*     */   public void readExternal(ObjectInput in)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 262 */     this._bitSize = in.readInt();
/* 263 */     this._intSize = ((this._bitSize >>> 5) + 1);
/* 264 */     this._mask = in.readInt();
/* 265 */     this._bits = ((int[])in.readObject());
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.dom.BitArray
 * JD-Core Version:    0.6.2
 */