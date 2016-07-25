/*    */ package com.sun.media.sound;
/*    */ 
/*    */ import java.util.Comparator;
/*    */ import javax.sound.midi.Instrument;
/*    */ import javax.sound.midi.Patch;
/*    */ 
/*    */ public final class ModelInstrumentComparator
/*    */   implements Comparator<Instrument>
/*    */ {
/*    */   public int compare(Instrument paramInstrument1, Instrument paramInstrument2)
/*    */   {
/* 40 */     Patch localPatch1 = paramInstrument1.getPatch();
/* 41 */     Patch localPatch2 = paramInstrument2.getPatch();
/* 42 */     int i = localPatch1.getBank() * 128 + localPatch1.getProgram();
/* 43 */     int j = localPatch2.getBank() * 128 + localPatch2.getProgram();
/* 44 */     if ((localPatch1 instanceof ModelPatch)) {
/* 45 */       i += (((ModelPatch)localPatch1).isPercussion() ? 2097152 : 0);
/*    */     }
/* 47 */     if ((localPatch2 instanceof ModelPatch)) {
/* 48 */       j += (((ModelPatch)localPatch2).isPercussion() ? 2097152 : 0);
/*    */     }
/* 50 */     return i - j;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.ModelInstrumentComparator
 * JD-Core Version:    0.6.2
 */