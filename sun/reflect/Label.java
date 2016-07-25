/*    */ package sun.reflect;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ 
/*    */ class Label
/*    */ {
/* 56 */   private List patches = new ArrayList();
/*    */ 
/*    */   void add(ClassFileAssembler paramClassFileAssembler, short paramShort1, short paramShort2, int paramInt)
/*    */   {
/* 66 */     this.patches.add(new PatchInfo(paramClassFileAssembler, paramShort1, paramShort2, paramInt));
/*    */   }
/*    */ 
/*    */   public void bind() {
/* 70 */     for (Iterator localIterator = this.patches.iterator(); localIterator.hasNext(); ) {
/* 71 */       PatchInfo localPatchInfo = (PatchInfo)localIterator.next();
/* 72 */       int i = localPatchInfo.asm.getLength();
/* 73 */       short s = (short)(i - localPatchInfo.instrBCI);
/* 74 */       localPatchInfo.asm.emitShort(localPatchInfo.patchBCI, s);
/* 75 */       localPatchInfo.asm.setStack(localPatchInfo.stackDepth);
/*    */     }
/*    */   }
/*    */ 
/*    */   static class PatchInfo
/*    */   {
/*    */     ClassFileAssembler asm;
/*    */     short instrBCI;
/*    */     short patchBCI;
/*    */     int stackDepth;
/*    */ 
/*    */     PatchInfo(ClassFileAssembler paramClassFileAssembler, short paramShort1, short paramShort2, int paramInt)
/*    */     {
/* 44 */       this.asm = paramClassFileAssembler;
/* 45 */       this.instrBCI = paramShort1;
/* 46 */       this.patchBCI = paramShort2;
/* 47 */       this.stackDepth = paramInt;
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.reflect.Label
 * JD-Core Version:    0.6.2
 */