/*      */ package sun.org.mozilla.javascript.internal.regexp;
/*      */ 
/*      */ class REProgState
/*      */ {
/*      */   REProgState previous;
/*      */   int min;
/*      */   int max;
/*      */   int index;
/*      */   int continuation_op;
/*      */   int continuation_pc;
/*      */   REBackTrackData backTrack;
/*      */ 
/*      */   REProgState(REProgState paramREProgState, int paramInt1, int paramInt2, int paramInt3, REBackTrackData paramREBackTrackData, int paramInt4, int paramInt5)
/*      */   {
/* 2692 */     this.previous = paramREProgState;
/* 2693 */     this.min = paramInt1;
/* 2694 */     this.max = paramInt2;
/* 2695 */     this.index = paramInt3;
/* 2696 */     this.continuation_op = paramInt5;
/* 2697 */     this.continuation_pc = paramInt4;
/* 2698 */     this.backTrack = paramREBackTrackData;
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.regexp.REProgState
 * JD-Core Version:    0.6.2
 */