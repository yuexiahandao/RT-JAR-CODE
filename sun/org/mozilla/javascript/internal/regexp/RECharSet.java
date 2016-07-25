/*      */ package sun.org.mozilla.javascript.internal.regexp;
/*      */ 
/*      */ final class RECharSet
/*      */ {
/*      */   static final long serialVersionUID = 7931787979395898394L;
/*      */   int length;
/*      */   int startIndex;
/*      */   int strlength;
/*      */   volatile transient boolean converted;
/*      */   volatile transient boolean sense;
/*      */   volatile transient byte[] bits;
/*      */ 
/*      */   RECharSet(int paramInt1, int paramInt2, int paramInt3)
/*      */   {
/* 2787 */     this.length = paramInt1;
/* 2788 */     this.startIndex = paramInt2;
/* 2789 */     this.strlength = paramInt3;
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.regexp.RECharSet
 * JD-Core Version:    0.6.2
 */