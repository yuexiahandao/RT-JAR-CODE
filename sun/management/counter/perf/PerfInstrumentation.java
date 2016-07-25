/*     */ package sun.management.counter.perf;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import java.util.SortedMap;
/*     */ import java.util.TreeMap;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import sun.management.counter.Counter;
/*     */ import sun.management.counter.Units;
/*     */ 
/*     */ public class PerfInstrumentation
/*     */ {
/*     */   private ByteBuffer buffer;
/*     */   private Prologue prologue;
/*     */   private long lastModificationTime;
/*     */   private long lastUsed;
/*     */   private int nextEntry;
/*     */   private SortedMap<String, Counter> map;
/*     */ 
/*     */   public PerfInstrumentation(ByteBuffer paramByteBuffer)
/*     */   {
/*  42 */     this.prologue = new Prologue(paramByteBuffer);
/*  43 */     this.buffer = paramByteBuffer;
/*  44 */     this.buffer.order(this.prologue.getByteOrder());
/*     */ 
/*  47 */     int i = getMajorVersion();
/*  48 */     int j = getMinorVersion();
/*     */ 
/*  51 */     if (i < 2) {
/*  52 */       throw new InstrumentationException("Unsupported version: " + i + "." + j);
/*     */     }
/*     */ 
/*  55 */     rewind();
/*     */   }
/*     */ 
/*     */   public int getMajorVersion() {
/*  59 */     return this.prologue.getMajorVersion();
/*     */   }
/*     */ 
/*     */   public int getMinorVersion() {
/*  63 */     return this.prologue.getMinorVersion();
/*     */   }
/*     */ 
/*     */   public long getModificationTimeStamp() {
/*  67 */     return this.prologue.getModificationTimeStamp();
/*     */   }
/*     */ 
/*     */   void rewind()
/*     */   {
/*  72 */     this.buffer.rewind();
/*  73 */     this.buffer.position(this.prologue.getEntryOffset());
/*  74 */     this.nextEntry = this.buffer.position();
/*     */ 
/*  76 */     this.map = new TreeMap();
/*     */   }
/*     */ 
/*     */   boolean hasNext() {
/*  80 */     return this.nextEntry < this.prologue.getUsed();
/*     */   }
/*     */ 
/*     */   Counter getNextCounter() {
/*  84 */     if (!hasNext()) {
/*  85 */       return null;
/*     */     }
/*     */ 
/*  88 */     if (this.nextEntry % 4 != 0)
/*     */     {
/*  90 */       throw new InstrumentationException("Entry index not properly aligned: " + this.nextEntry);
/*     */     }
/*     */ 
/*  94 */     if ((this.nextEntry < 0) || (this.nextEntry > this.buffer.limit()))
/*     */     {
/*  96 */       throw new InstrumentationException("Entry index out of bounds: nextEntry = " + this.nextEntry + ", limit = " + this.buffer.limit());
/*     */     }
/*     */ 
/* 101 */     this.buffer.position(this.nextEntry);
/* 102 */     PerfDataEntry localPerfDataEntry = new PerfDataEntry(this.buffer);
/* 103 */     this.nextEntry += localPerfDataEntry.size();
/*     */ 
/* 105 */     Object localObject = null;
/* 106 */     PerfDataType localPerfDataType = localPerfDataEntry.type();
/* 107 */     if (localPerfDataType == PerfDataType.BYTE) {
/* 108 */       if ((localPerfDataEntry.units() == Units.STRING) && (localPerfDataEntry.vectorLength() > 0)) {
/* 109 */         localObject = new PerfStringCounter(localPerfDataEntry.name(), localPerfDataEntry.variability(), localPerfDataEntry.flags(), localPerfDataEntry.vectorLength(), localPerfDataEntry.byteData());
/*     */       }
/* 114 */       else if (localPerfDataEntry.vectorLength() > 0) {
/* 115 */         localObject = new PerfByteArrayCounter(localPerfDataEntry.name(), localPerfDataEntry.units(), localPerfDataEntry.variability(), localPerfDataEntry.flags(), localPerfDataEntry.vectorLength(), localPerfDataEntry.byteData());
/*     */       }
/* 123 */       else if (!$assertionsDisabled) throw new AssertionError();
/*     */ 
/*     */     }
/* 126 */     else if (localPerfDataType == PerfDataType.LONG) {
/* 127 */       if (localPerfDataEntry.vectorLength() == 0) {
/* 128 */         localObject = new PerfLongCounter(localPerfDataEntry.name(), localPerfDataEntry.units(), localPerfDataEntry.variability(), localPerfDataEntry.flags(), localPerfDataEntry.longData());
/*     */       }
/*     */       else
/*     */       {
/* 134 */         localObject = new PerfLongArrayCounter(localPerfDataEntry.name(), localPerfDataEntry.units(), localPerfDataEntry.variability(), localPerfDataEntry.flags(), localPerfDataEntry.vectorLength(), localPerfDataEntry.longData());
/*     */       }
/*     */ 
/*     */     }
/* 145 */     else if (!$assertionsDisabled) throw new AssertionError();
/*     */ 
/* 147 */     return localObject;
/*     */   }
/*     */ 
/*     */   public synchronized List<Counter> getAllCounters() {
/* 151 */     while (hasNext()) {
/* 152 */       Counter localCounter = getNextCounter();
/* 153 */       if (localCounter != null) {
/* 154 */         this.map.put(localCounter.getName(), localCounter);
/*     */       }
/*     */     }
/* 157 */     return new ArrayList(this.map.values());
/*     */   }
/*     */ 
/*     */   public synchronized List<Counter> findByPattern(String paramString) {
/* 161 */     while (hasNext()) {
/* 162 */       localObject = getNextCounter();
/* 163 */       if (localObject != null) {
/* 164 */         this.map.put(((Counter)localObject).getName(), localObject);
/*     */       }
/*     */     }
/*     */ 
/* 168 */     Object localObject = Pattern.compile(paramString);
/* 169 */     Matcher localMatcher = ((Pattern)localObject).matcher("");
/* 170 */     ArrayList localArrayList = new ArrayList();
/*     */ 
/* 172 */     Iterator localIterator = this.map.entrySet().iterator();
/* 173 */     while (localIterator.hasNext()) {
/* 174 */       Map.Entry localEntry = (Map.Entry)localIterator.next();
/* 175 */       String str = (String)localEntry.getKey();
/*     */ 
/* 178 */       localMatcher.reset(str);
/*     */ 
/* 181 */       if (localMatcher.lookingAt()) {
/* 182 */         localArrayList.add((Counter)localEntry.getValue());
/*     */       }
/*     */     }
/* 185 */     return localArrayList;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.counter.perf.PerfInstrumentation
 * JD-Core Version:    0.6.2
 */