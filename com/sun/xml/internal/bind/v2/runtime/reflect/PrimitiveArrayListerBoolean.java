/*    */ package com.sun.xml.internal.bind.v2.runtime.reflect;
/*    */ 
/*    */ import com.sun.xml.internal.bind.api.AccessorException;
/*    */ import com.sun.xml.internal.bind.v2.runtime.XMLSerializer;
/*    */ import java.util.Map;
/*    */ 
/*    */ final class PrimitiveArrayListerBoolean<BeanT> extends Lister<BeanT, boolean[], Boolean, BooleanArrayPack>
/*    */ {
/*    */   static void register()
/*    */   {
/* 44 */     Lister.primitiveArrayListers.put(Boolean.TYPE, new PrimitiveArrayListerBoolean());
/*    */   }
/*    */ 
/*    */   public ListIterator<Boolean> iterator(final boolean[] objects, XMLSerializer context) {
/* 48 */     return new ListIterator() {
/* 49 */       int idx = 0;
/*    */ 
/* 51 */       public boolean hasNext() { return this.idx < objects.length; }
/*    */ 
/*    */       public Boolean next()
/*    */       {
/* 55 */         return Boolean.valueOf(objects[(this.idx++)]);
/*    */       }
/*    */     };
/*    */   }
/*    */ 
/*    */   public BooleanArrayPack startPacking(BeanT current, Accessor<BeanT, boolean[]> acc) {
/* 61 */     return new BooleanArrayPack();
/*    */   }
/*    */ 
/*    */   public void addToPack(BooleanArrayPack objects, Boolean o) {
/* 65 */     objects.add(o);
/*    */   }
/*    */ 
/*    */   public void endPacking(BooleanArrayPack pack, BeanT bean, Accessor<BeanT, boolean[]> acc) throws AccessorException {
/* 69 */     acc.set(bean, pack.build());
/*    */   }
/*    */ 
/*    */   public void reset(BeanT o, Accessor<BeanT, boolean[]> acc) throws AccessorException {
/* 73 */     acc.set(o, new boolean[0]);
/*    */   }
/*    */ 
/* 77 */   static final class BooleanArrayPack { boolean[] buf = new boolean[16];
/*    */     int size;
/*    */ 
/*    */     void add(Boolean b) {
/* 81 */       if (this.buf.length == this.size)
/*    */       {
/* 83 */         boolean[] nb = new boolean[this.buf.length * 2];
/* 84 */         System.arraycopy(this.buf, 0, nb, 0, this.buf.length);
/* 85 */         this.buf = nb;
/*    */       }
/* 87 */       if (b != null)
/* 88 */         this.buf[(this.size++)] = b.booleanValue();
/*    */     }
/*    */ 
/*    */     boolean[] build() {
/* 92 */       if (this.buf.length == this.size)
/*    */       {
/* 94 */         return this.buf;
/*    */       }
/* 96 */       boolean[] r = new boolean[this.size];
/* 97 */       System.arraycopy(this.buf, 0, r, 0, this.size);
/* 98 */       return r;
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.reflect.PrimitiveArrayListerBoolean
 * JD-Core Version:    0.6.2
 */