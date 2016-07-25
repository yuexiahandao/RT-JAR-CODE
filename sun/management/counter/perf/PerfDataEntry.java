/*     */ package sun.management.counter.perf;
/*     */ 
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.LongBuffer;
/*     */ import sun.management.counter.Units;
/*     */ import sun.management.counter.Variability;
/*     */ 
/*     */ class PerfDataEntry
/*     */ {
/*     */   private String name;
/*     */   private int entryStart;
/*     */   private int entryLength;
/*     */   private int vectorLength;
/*     */   private PerfDataType dataType;
/*     */   private int flags;
/*     */   private Units unit;
/*     */   private Variability variability;
/*     */   private int dataOffset;
/*     */   private int dataSize;
/*     */   private ByteBuffer data;
/*     */ 
/*     */   PerfDataEntry(ByteBuffer paramByteBuffer)
/*     */   {
/*  70 */     this.entryStart = paramByteBuffer.position();
/*  71 */     this.entryLength = paramByteBuffer.getInt();
/*     */ 
/*  74 */     if ((this.entryLength <= 0) || (this.entryLength > paramByteBuffer.limit())) {
/*  75 */       throw new InstrumentationException("Invalid entry length:  entryLength = " + this.entryLength);
/*     */     }
/*     */ 
/*  79 */     if (this.entryStart + this.entryLength > paramByteBuffer.limit()) {
/*  80 */       throw new InstrumentationException("Entry extends beyond end of buffer:  entryStart = " + this.entryStart + " entryLength = " + this.entryLength + " buffer limit = " + paramByteBuffer.limit());
/*     */     }
/*     */ 
/*  86 */     paramByteBuffer.position(this.entryStart + 4);
/*  87 */     int i = paramByteBuffer.getInt();
/*     */ 
/*  89 */     if (this.entryStart + i > paramByteBuffer.limit()) {
/*  90 */       throw new InstrumentationException("Invalid name offset:  entryStart = " + this.entryStart + " nameOffset = " + i + " buffer limit = " + paramByteBuffer.limit());
/*     */     }
/*     */ 
/*  97 */     paramByteBuffer.position(this.entryStart + 8);
/*  98 */     this.vectorLength = paramByteBuffer.getInt();
/*     */ 
/* 100 */     paramByteBuffer.position(this.entryStart + 12);
/* 101 */     this.dataType = PerfDataType.toPerfDataType(paramByteBuffer.get());
/*     */ 
/* 103 */     paramByteBuffer.position(this.entryStart + 13);
/* 104 */     this.flags = paramByteBuffer.get();
/*     */ 
/* 106 */     paramByteBuffer.position(this.entryStart + 14);
/* 107 */     this.unit = Units.toUnits(paramByteBuffer.get());
/*     */ 
/* 109 */     paramByteBuffer.position(this.entryStart + 15);
/* 110 */     this.variability = Variability.toVariability(paramByteBuffer.get());
/*     */ 
/* 112 */     paramByteBuffer.position(this.entryStart + 16);
/* 113 */     this.dataOffset = paramByteBuffer.getInt();
/*     */ 
/* 117 */     paramByteBuffer.position(this.entryStart + i);
/*     */ 
/* 119 */     int j = 0;
/*     */     int k;
/* 121 */     while ((k = paramByteBuffer.get()) != 0) j++;
/*     */ 
/* 123 */     byte[] arrayOfByte = new byte[j];
/* 124 */     paramByteBuffer.position(this.entryStart + i);
/* 125 */     for (int m = 0; m < j; m++) {
/* 126 */       arrayOfByte[m] = paramByteBuffer.get();
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 131 */       this.name = new String(arrayOfByte, "UTF-8");
/*     */     }
/*     */     catch (UnsupportedEncodingException localUnsupportedEncodingException)
/*     */     {
/* 136 */       throw new InternalError(localUnsupportedEncodingException.getMessage());
/*     */     }
/*     */ 
/* 139 */     if (this.variability == Variability.INVALID) {
/* 140 */       throw new InstrumentationException("Invalid variability attribute: name = " + this.name);
/*     */     }
/*     */ 
/* 143 */     if (this.unit == Units.INVALID) {
/* 144 */       throw new InstrumentationException("Invalid units attribute:  name = " + this.name);
/*     */     }
/*     */ 
/* 148 */     if (this.vectorLength > 0)
/* 149 */       this.dataSize = (this.vectorLength * this.dataType.size());
/*     */     else {
/* 151 */       this.dataSize = this.dataType.size();
/*     */     }
/*     */ 
/* 155 */     if (this.entryStart + this.dataOffset + this.dataSize > paramByteBuffer.limit()) {
/* 156 */       throw new InstrumentationException("Data extends beyond end of buffer:  entryStart = " + this.entryStart + " dataOffset = " + this.dataOffset + " dataSize = " + this.dataSize + " buffer limit = " + paramByteBuffer.limit());
/*     */     }
/*     */ 
/* 163 */     paramByteBuffer.position(this.entryStart + this.dataOffset);
/* 164 */     this.data = paramByteBuffer.slice();
/* 165 */     this.data.order(paramByteBuffer.order());
/* 166 */     this.data.limit(this.dataSize);
/*     */   }
/*     */ 
/*     */   public int size()
/*     */   {
/* 171 */     return this.entryLength;
/*     */   }
/*     */ 
/*     */   public String name() {
/* 175 */     return this.name;
/*     */   }
/*     */ 
/*     */   public PerfDataType type() {
/* 179 */     return this.dataType;
/*     */   }
/*     */ 
/*     */   public Units units() {
/* 183 */     return this.unit;
/*     */   }
/*     */ 
/*     */   public int flags() {
/* 187 */     return this.flags;
/*     */   }
/*     */ 
/*     */   public int vectorLength()
/*     */   {
/* 194 */     return this.vectorLength;
/*     */   }
/*     */ 
/*     */   public Variability variability() {
/* 198 */     return this.variability;
/*     */   }
/*     */ 
/*     */   public ByteBuffer byteData() {
/* 202 */     this.data.position(0);
/* 203 */     assert (this.data.remaining() == vectorLength());
/* 204 */     return this.data.duplicate();
/*     */   }
/*     */ 
/*     */   public LongBuffer longData() {
/* 208 */     LongBuffer localLongBuffer = this.data.asLongBuffer();
/* 209 */     return localLongBuffer;
/*     */   }
/*     */ 
/*     */   private class EntryFieldOffset
/*     */   {
/*     */     private static final int SIZEOF_BYTE = 1;
/*     */     private static final int SIZEOF_INT = 4;
/*     */     private static final int SIZEOF_LONG = 8;
/*     */     private static final int ENTRY_LENGTH_SIZE = 4;
/*     */     private static final int NAME_OFFSET_SIZE = 4;
/*     */     private static final int VECTOR_LENGTH_SIZE = 4;
/*     */     private static final int DATA_TYPE_SIZE = 1;
/*     */     private static final int FLAGS_SIZE = 1;
/*     */     private static final int DATA_UNIT_SIZE = 1;
/*     */     private static final int DATA_VAR_SIZE = 1;
/*     */     private static final int DATA_OFFSET_SIZE = 4;
/*     */     static final int ENTRY_LENGTH = 0;
/*     */     static final int NAME_OFFSET = 4;
/*     */     static final int VECTOR_LENGTH = 8;
/*     */     static final int DATA_TYPE = 12;
/*     */     static final int FLAGS = 13;
/*     */     static final int DATA_UNIT = 14;
/*     */     static final int DATA_VAR = 15;
/*     */     static final int DATA_OFFSET = 16;
/*     */ 
/*     */     private EntryFieldOffset()
/*     */     {
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.counter.perf.PerfDataEntry
 * JD-Core Version:    0.6.2
 */