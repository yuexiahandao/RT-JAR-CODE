/*      */ package sun.org.mozilla.javascript.internal.regexp;
/*      */ 
/*      */ class REBackTrackData
/*      */ {
/*      */   REBackTrackData previous;
/*      */   int continuation_op;
/*      */   int continuation_pc;
/*      */   int lastParen;
/*      */   long[] parens;
/*      */   int cp;
/*      */   REProgState stateStackTop;
/*      */ 
/*      */   REBackTrackData(REGlobalData paramREGlobalData, int paramInt1, int paramInt2)
/*      */   {
/* 2715 */     this.previous = paramREGlobalData.backTrackStackTop;
/* 2716 */     this.continuation_op = paramInt1;
/* 2717 */     this.continuation_pc = paramInt2;
/* 2718 */     this.lastParen = paramREGlobalData.lastParen;
/* 2719 */     if (paramREGlobalData.parens != null) {
/* 2720 */       this.parens = ((long[])paramREGlobalData.parens.clone());
/*      */     }
/* 2722 */     this.cp = paramREGlobalData.cp;
/* 2723 */     this.stateStackTop = paramREGlobalData.stateStackTop;
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.regexp.REBackTrackData
 * JD-Core Version:    0.6.2
 */