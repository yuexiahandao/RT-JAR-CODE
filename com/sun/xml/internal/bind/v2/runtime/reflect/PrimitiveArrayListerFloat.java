/*    */ package com.sun.xml.internal.bind.v2.runtime.reflect;
/*    */ 
/*    */ import com.sun.xml.internal.bind.api.AccessorException;
/*    */ import com.sun.xml.internal.bind.v2.runtime.XMLSerializer;
/*    */ import java.util.Map;
/*    */ 
/*    */ final class PrimitiveArrayListerFloat<BeanT> extends Lister<BeanT, float[], Float, FloatArrayPack>
/*    */ {
/*    */   static void register()
/*    */   {
/* 44 */     Lister.primitiveArrayListers.put(Float.TYPE, new PrimitiveArrayListerFloat());
/*    */   }
/*    */ 
/*    */   public ListIterator<Float> iterator(final float[] objects, XMLSerializer context) {
/* 48 */     return new ListIterator() {
/* 49 */       int idx = 0;
/*    */ 
/* 51 */       public boolean hasNext() { return this.idx < objects.length; }
/*    */ 
/*    */       public Float next()
/*    */       {
/* 55 */         return Float.valueOf(objects[(this.idx++)]);
/*    */       }
/*    */     };
/*    */   }
/*    */ 
/*    */   public FloatArrayPack startPacking(BeanT current, Accessor<BeanT, float[]> acc) {
/* 61 */     return new FloatArrayPack();
/*    */   }
/*    */ 
/*    */   public void addToPack(FloatArrayPack objects, Float o) {
/* 65 */     objects.add(o);
/*    */   }
/*    */ 
/*    */   public void endPacking(FloatArrayPack pack, BeanT bean, Accessor<BeanT, float[]> acc) throws AccessorException {
/* 69 */     acc.set(bean, pack.build());
/*    */   }
/*    */ 
/*    */   public void reset(BeanT o, Accessor<BeanT, float[]> acc) throws AccessorException {
/* 73 */     acc.set(o, new float[0]);
/*    */   }
/*    */ 
/* 77 */   static final class FloatArrayPack { float[] buf = new float[16];
/*    */     int size;
/*    */ 
/*    */     void add(Float b) {
/* 81 */       if (this.buf.length == this.size)
/*    */       {
/* 83 */         float[] nb = new float[this.buf.length * 2];
/* 84 */         System.arraycopy(this.buf, 0, nb, 0, this.buf.length);
/* 85 */         this.buf = nb;
/*    */       }
/* 87 */       if (b != null)
/* 88 */         this.buf[(this.size++)] = b.floatValue();
/*    */     }
/*    */ 
/*    */     float[] build() {
/* 92 */       if (this.buf.length == this.size)
/*    */       {
/* 94 */         return this.buf;
/*    */       }
/* 96 */       float[] r = new float[this.size];
/* 97 */       System.arraycopy(this.buf, 0, r, 0, this.size);
/* 98 */       return r;
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.reflect.PrimitiveArrayListerFloat
 * JD-Core Version:    0.6.2
 */