/*      */ package com.sun.imageio.plugins.jpeg;
/*      */ 
/*      */ import java.util.Iterator;
/*      */ import java.util.NoSuchElementException;
/*      */ import javax.imageio.ImageTypeSpecifier;
/*      */ 
/*      */ class ImageTypeIterator
/*      */   implements Iterator<ImageTypeSpecifier>
/*      */ {
/*      */   private Iterator<ImageTypeProducer> producers;
/* 1696 */   private ImageTypeSpecifier theNext = null;
/*      */ 
/*      */   public ImageTypeIterator(Iterator<ImageTypeProducer> paramIterator) {
/* 1699 */     this.producers = paramIterator;
/*      */   }
/*      */ 
/*      */   public boolean hasNext() {
/* 1703 */     if (this.theNext != null) {
/* 1704 */       return true;
/*      */     }
/* 1706 */     if (!this.producers.hasNext()) {
/* 1707 */       return false;
/*      */     }
/*      */     do
/* 1710 */       this.theNext = ((ImageTypeProducer)this.producers.next()).getType();
/* 1711 */     while ((this.theNext == null) && (this.producers.hasNext()));
/*      */ 
/* 1713 */     return this.theNext != null;
/*      */   }
/*      */ 
/*      */   public ImageTypeSpecifier next() {
/* 1717 */     if ((this.theNext != null) || (hasNext())) {
/* 1718 */       ImageTypeSpecifier localImageTypeSpecifier = this.theNext;
/* 1719 */       this.theNext = null;
/* 1720 */       return localImageTypeSpecifier;
/*      */     }
/* 1722 */     throw new NoSuchElementException();
/*      */   }
/*      */ 
/*      */   public void remove()
/*      */   {
/* 1727 */     this.producers.remove();
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.imageio.plugins.jpeg.ImageTypeIterator
 * JD-Core Version:    0.6.2
 */