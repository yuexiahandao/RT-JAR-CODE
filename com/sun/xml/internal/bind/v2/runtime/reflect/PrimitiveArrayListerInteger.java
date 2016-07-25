/*    */ package com.sun.xml.internal.bind.v2.runtime.reflect;
/*    */ 
/*    */ import com.sun.xml.internal.bind.api.AccessorException;
/*    */ import com.sun.xml.internal.bind.v2.runtime.XMLSerializer;
/*    */ import java.util.Map;
/*    */ 
/*    */ final class PrimitiveArrayListerInteger<BeanT> extends Lister<BeanT, int[], Integer, IntegerArrayPack>
/*    */ {
/*    */   static void register()
/*    */   {
/* 44 */     Lister.primitiveArrayListers.put(Integer.TYPE, new PrimitiveArrayListerInteger());
/*    */   }
/*    */ 
/*    */   public ListIterator<Integer> iterator(final int[] objects, XMLSerializer context) {
/* 48 */     return new ListIterator() {
/* 49 */       int idx = 0;
/*    */ 
/* 51 */       public boolean hasNext() { return this.idx < objects.length; }
/*    */ 
/*    */       public Integer next()
/*    */       {
/* 55 */         return Integer.valueOf(objects[(this.idx++)]);
/*    */       }
/*    */     };
/*    */   }
/*    */ 
/*    */   public IntegerArrayPack startPacking(BeanT current, Accessor<BeanT, int[]> acc) {
/* 61 */     return new IntegerArrayPack();
/*    */   }
/*    */ 
/*    */   public void addToPack(IntegerArrayPack objects, Integer o) {
/* 65 */     objects.add(o);
/*    */   }
/*    */ 
/*    */   public void endPacking(IntegerArrayPack pack, BeanT bean, Accessor<BeanT, int[]> acc) throws AccessorException {
/* 69 */     acc.set(bean, pack.build());
/*    */   }
/*    */ 
/*    */   public void reset(BeanT o, Accessor<BeanT, int[]> acc) throws AccessorException {
/* 73 */     acc.set(o, new int[0]);
/*    */   }
/*    */ 
/* 77 */   static final class IntegerArrayPack { int[] buf = new int[16];
/*    */     int size;
/*    */ 
/*    */     void add(Integer b) {
/* 81 */       if (this.buf.length == this.size)
/*    */       {
/* 83 */         int[] nb = new int[this.buf.length * 2];
/* 84 */         System.arraycopy(this.buf, 0, nb, 0, this.buf.length);
/* 85 */         this.buf = nb;
/*    */       }
/* 87 */       if (b != null)
/* 88 */         this.buf[(this.size++)] = b.intValue();
/*    */     }
/*    */ 
/*    */     int[] build() {
/* 92 */       if (this.buf.length == this.size)
/*    */       {
/* 94 */         return this.buf;
/*    */       }
/* 96 */       int[] r = new int[this.size];
/* 97 */       System.arraycopy(this.buf, 0, r, 0, this.size);
/* 98 */       return r;
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.reflect.PrimitiveArrayListerInteger
 * JD-Core Version:    0.6.2
 */