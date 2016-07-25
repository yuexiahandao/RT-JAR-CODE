/*     */ package sun.management.counter.perf;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.ByteOrder;
/*     */ 
/*     */ class Prologue
/*     */ {
/*     */   private static final byte PERFDATA_BIG_ENDIAN = 0;
/*     */   private static final byte PERFDATA_LITTLE_ENDIAN = 1;
/*     */   private static final int PERFDATA_MAGIC = -889274176;
/*     */   private ByteBuffer header;
/*     */   private int magic;
/*     */ 
/*     */   Prologue(ByteBuffer paramByteBuffer)
/*     */   {
/*  73 */     this.header = paramByteBuffer.duplicate();
/*     */ 
/*  78 */     this.header.order(ByteOrder.BIG_ENDIAN);
/*  79 */     this.header.position(0);
/*  80 */     this.magic = this.header.getInt();
/*     */ 
/*  83 */     if (this.magic != -889274176) {
/*  84 */       throw new InstrumentationException("Bad Magic: " + Integer.toHexString(getMagic()));
/*     */     }
/*     */ 
/*  91 */     this.header.order(getByteOrder());
/*     */ 
/*  94 */     int i = getMajorVersion();
/*  95 */     int j = getMinorVersion();
/*     */ 
/*  97 */     if (i < 2) {
/*  98 */       throw new InstrumentationException("Unsupported version: " + i + "." + j);
/*     */     }
/*     */ 
/* 103 */     this.header.limit(32);
/*     */   }
/*     */ 
/*     */   public int getMagic() {
/* 107 */     return this.magic;
/*     */   }
/*     */ 
/*     */   public int getMajorVersion() {
/* 111 */     this.header.position(5);
/* 112 */     return this.header.get();
/*     */   }
/*     */ 
/*     */   public int getMinorVersion() {
/* 116 */     this.header.position(6);
/* 117 */     return this.header.get();
/*     */   }
/*     */ 
/*     */   public ByteOrder getByteOrder() {
/* 121 */     this.header.position(4);
/*     */ 
/* 123 */     int i = this.header.get();
/* 124 */     if (i == 0) {
/* 125 */       return ByteOrder.BIG_ENDIAN;
/*     */     }
/*     */ 
/* 128 */     return ByteOrder.LITTLE_ENDIAN;
/*     */   }
/*     */ 
/*     */   public int getEntryOffset()
/*     */   {
/* 133 */     this.header.position(24);
/* 134 */     return this.header.getInt();
/*     */   }
/*     */ 
/*     */   public int getUsed()
/*     */   {
/* 140 */     this.header.position(8);
/* 141 */     return this.header.getInt();
/*     */   }
/*     */ 
/*     */   public int getOverflow() {
/* 145 */     this.header.position(12);
/* 146 */     return this.header.getInt();
/*     */   }
/*     */ 
/*     */   public long getModificationTimeStamp() {
/* 150 */     this.header.position(16);
/* 151 */     return this.header.getLong();
/*     */   }
/*     */ 
/*     */   public int getNumEntries() {
/* 155 */     this.header.position(28);
/* 156 */     return this.header.getInt();
/*     */   }
/*     */ 
/*     */   public boolean isAccessible() {
/* 160 */     this.header.position(7);
/* 161 */     int i = this.header.get();
/* 162 */     return i != 0;
/*     */   }
/*     */ 
/*     */   private class PrologueFieldOffset
/*     */   {
/*     */     private static final int SIZEOF_BYTE = 1;
/*     */     private static final int SIZEOF_INT = 4;
/*     */     private static final int SIZEOF_LONG = 8;
/*     */     private static final int MAGIC_SIZE = 4;
/*     */     private static final int BYTE_ORDER_SIZE = 1;
/*     */     private static final int MAJOR_SIZE = 1;
/*     */     private static final int MINOR_SIZE = 1;
/*     */     private static final int ACCESSIBLE_SIZE = 1;
/*     */     private static final int USED_SIZE = 4;
/*     */     private static final int OVERFLOW_SIZE = 4;
/*     */     private static final int MOD_TIMESTAMP_SIZE = 8;
/*     */     private static final int ENTRY_OFFSET_SIZE = 4;
/*     */     private static final int NUM_ENTRIES_SIZE = 4;
/*     */     static final int MAGIC = 0;
/*     */     static final int BYTE_ORDER = 4;
/*     */     static final int MAJOR_VERSION = 5;
/*     */     static final int MINOR_VERSION = 6;
/*     */     static final int ACCESSIBLE = 7;
/*     */     static final int USED = 8;
/*     */     static final int OVERFLOW = 12;
/*     */     static final int MOD_TIMESTAMP = 16;
/*     */     static final int ENTRY_OFFSET = 24;
/*     */     static final int NUM_ENTRIES = 28;
/*     */     static final int PROLOGUE_2_0_SIZE = 32;
/*     */ 
/*     */     private PrologueFieldOffset()
/*     */     {
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.counter.perf.Prologue
 * JD-Core Version:    0.6.2
 */