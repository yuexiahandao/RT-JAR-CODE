/*    */ package com.sun.xml.internal.bind.v2.runtime.reflect;
/*    */ 
/*    */ import com.sun.xml.internal.bind.api.AccessorException;
/*    */ import com.sun.xml.internal.bind.v2.runtime.XMLSerializer;
/*    */ import java.util.Map;
/*    */ 
/*    */ final class PrimitiveArrayListerLong<BeanT> extends Lister<BeanT, long[], Long, LongArrayPack>
/*    */ {
/*    */   static void register()
/*    */   {
/* 44 */     Lister.primitiveArrayListers.put(Long.TYPE, new PrimitiveArrayListerLong());
/*    */   }
/*    */ 
/*    */   public ListIterator<Long> iterator(final long[] objects, XMLSerializer context) {
/* 48 */     return new ListIterator() {
/* 49 */       int idx = 0;
/*    */ 
/* 51 */       public boolean hasNext() { return this.idx < objects.length; }
/*    */ 
/*    */       public Long next()
/*    */       {
/* 55 */         return Long.valueOf(objects[(this.idx++)]);
/*    */       }
/*    */     };
/*    */   }
/*    */ 
/*    */   public LongArrayPack startPacking(BeanT current, Accessor<BeanT, long[]> acc) {
/* 61 */     return new LongArrayPack();
/*    */   }
/*    */ 
/*    */   public void addToPack(LongArrayPack objects, Long o) {
/* 65 */     objects.add(o);
/*    */   }
/*    */ 
/*    */   public void endPacking(LongArrayPack pack, BeanT bean, Accessor<BeanT, long[]> acc) throws AccessorException {
/* 69 */     acc.set(bean, pack.build());
/*    */   }
/*    */ 
/*    */   public void reset(BeanT o, Accessor<BeanT, long[]> acc) throws AccessorException {
/* 73 */     acc.set(o, new long[0]);
/*    */   }
/*    */ 
/* 77 */   static final class LongArrayPack { long[] buf = new long[16];
/*    */     int size;
/*    */ 
/*    */     void add(Long b) {
/* 81 */       if (this.buf.length == this.size)
/*    */       {
/* 83 */         long[] nb = new long[this.buf.length * 2];
/* 84 */         System.arraycopy(this.buf, 0, nb, 0, this.buf.length);
/* 85 */         this.buf = nb;
/*    */       }
/* 87 */       if (b != null)
/* 88 */         this.buf[(this.size++)] = b.longValue();
/*    */     }
/*    */ 
/*    */     long[] build() {
/* 92 */       if (this.buf.length == this.size)
/*    */       {
/* 94 */         return this.buf;
/*    */       }
/* 96 */       long[] r = new long[this.size];
/* 97 */       System.arraycopy(this.buf, 0, r, 0, this.size);
/* 98 */       return r;
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.reflect.PrimitiveArrayListerLong
 * JD-Core Version:    0.6.2
 */