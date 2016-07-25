/*     */ package com.sun.media.sound;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import javax.sound.midi.Patch;
/*     */ 
/*     */ public class SimpleInstrument extends ModelInstrument
/*     */ {
/*  47 */   protected int preset = 0;
/*  48 */   protected int bank = 0;
/*  49 */   protected boolean percussion = false;
/*  50 */   protected String name = "";
/*  51 */   protected List<SimpleInstrumentPart> parts = new ArrayList();
/*     */ 
/*     */   public SimpleInstrument()
/*     */   {
/*  55 */     super(null, null, null, null);
/*     */   }
/*     */ 
/*     */   public void clear() {
/*  59 */     this.parts.clear();
/*     */   }
/*     */ 
/*     */   public void add(ModelPerformer[] paramArrayOfModelPerformer, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*     */   {
/*  64 */     SimpleInstrumentPart localSimpleInstrumentPart = new SimpleInstrumentPart(null);
/*  65 */     localSimpleInstrumentPart.performers = paramArrayOfModelPerformer;
/*  66 */     localSimpleInstrumentPart.keyFrom = paramInt1;
/*  67 */     localSimpleInstrumentPart.keyTo = paramInt2;
/*  68 */     localSimpleInstrumentPart.velFrom = paramInt3;
/*  69 */     localSimpleInstrumentPart.velTo = paramInt4;
/*  70 */     localSimpleInstrumentPart.exclusiveClass = paramInt5;
/*  71 */     this.parts.add(localSimpleInstrumentPart);
/*     */   }
/*     */ 
/*     */   public void add(ModelPerformer[] paramArrayOfModelPerformer, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/*  76 */     add(paramArrayOfModelPerformer, paramInt1, paramInt2, paramInt3, paramInt4, -1);
/*     */   }
/*     */ 
/*     */   public void add(ModelPerformer[] paramArrayOfModelPerformer, int paramInt1, int paramInt2) {
/*  80 */     add(paramArrayOfModelPerformer, paramInt1, paramInt2, 0, 127, -1);
/*     */   }
/*     */ 
/*     */   public void add(ModelPerformer[] paramArrayOfModelPerformer) {
/*  84 */     add(paramArrayOfModelPerformer, 0, 127, 0, 127, -1);
/*     */   }
/*     */ 
/*     */   public void add(ModelPerformer paramModelPerformer, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*     */   {
/*  89 */     add(new ModelPerformer[] { paramModelPerformer }, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*     */   }
/*     */ 
/*     */   public void add(ModelPerformer paramModelPerformer, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/*  95 */     add(new ModelPerformer[] { paramModelPerformer }, paramInt1, paramInt2, paramInt3, paramInt4);
/*     */   }
/*     */ 
/*     */   public void add(ModelPerformer paramModelPerformer, int paramInt1, int paramInt2) {
/*  99 */     add(new ModelPerformer[] { paramModelPerformer }, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public void add(ModelPerformer paramModelPerformer) {
/* 103 */     add(new ModelPerformer[] { paramModelPerformer });
/*     */   }
/*     */ 
/*     */   public void add(ModelInstrument paramModelInstrument, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*     */   {
/* 108 */     add(paramModelInstrument.getPerformers(), paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*     */   }
/*     */ 
/*     */   public void add(ModelInstrument paramModelInstrument, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 113 */     add(paramModelInstrument.getPerformers(), paramInt1, paramInt2, paramInt3, paramInt4);
/*     */   }
/*     */ 
/*     */   public void add(ModelInstrument paramModelInstrument, int paramInt1, int paramInt2) {
/* 117 */     add(paramModelInstrument.getPerformers(), paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public void add(ModelInstrument paramModelInstrument) {
/* 121 */     add(paramModelInstrument.getPerformers());
/*     */   }
/*     */ 
/*     */   public ModelPerformer[] getPerformers()
/*     */   {
/* 126 */     int i = 0;
/* 127 */     for (Object localObject = this.parts.iterator(); ((Iterator)localObject).hasNext(); ) { SimpleInstrumentPart localSimpleInstrumentPart1 = (SimpleInstrumentPart)((Iterator)localObject).next();
/* 128 */       if (localSimpleInstrumentPart1.performers != null)
/* 129 */         i += localSimpleInstrumentPart1.performers.length;
/*     */     }
/* 131 */     localObject = new ModelPerformer[i];
/* 132 */     int j = 0;
/* 133 */     for (SimpleInstrumentPart localSimpleInstrumentPart2 : this.parts) {
/* 134 */       if (localSimpleInstrumentPart2.performers != null) {
/* 135 */         for (ModelPerformer localModelPerformer1 : localSimpleInstrumentPart2.performers) {
/* 136 */           ModelPerformer localModelPerformer2 = new ModelPerformer();
/* 137 */           localModelPerformer2.setName(getName());
/* 138 */           localObject[(j++)] = localModelPerformer2;
/*     */ 
/* 140 */           localModelPerformer2.setDefaultConnectionsEnabled(localModelPerformer1.isDefaultConnectionsEnabled());
/*     */ 
/* 142 */           localModelPerformer2.setKeyFrom(localModelPerformer1.getKeyFrom());
/* 143 */           localModelPerformer2.setKeyTo(localModelPerformer1.getKeyTo());
/* 144 */           localModelPerformer2.setVelFrom(localModelPerformer1.getVelFrom());
/* 145 */           localModelPerformer2.setVelTo(localModelPerformer1.getVelTo());
/* 146 */           localModelPerformer2.setExclusiveClass(localModelPerformer1.getExclusiveClass());
/* 147 */           localModelPerformer2.setSelfNonExclusive(localModelPerformer1.isSelfNonExclusive());
/* 148 */           localModelPerformer2.setReleaseTriggered(localModelPerformer1.isReleaseTriggered());
/* 149 */           if (localSimpleInstrumentPart2.exclusiveClass != -1)
/* 150 */             localModelPerformer2.setExclusiveClass(localSimpleInstrumentPart2.exclusiveClass);
/* 151 */           if (localSimpleInstrumentPart2.keyFrom > localModelPerformer2.getKeyFrom())
/* 152 */             localModelPerformer2.setKeyFrom(localSimpleInstrumentPart2.keyFrom);
/* 153 */           if (localSimpleInstrumentPart2.keyTo < localModelPerformer2.getKeyTo())
/* 154 */             localModelPerformer2.setKeyTo(localSimpleInstrumentPart2.keyTo);
/* 155 */           if (localSimpleInstrumentPart2.velFrom > localModelPerformer2.getVelFrom())
/* 156 */             localModelPerformer2.setVelFrom(localSimpleInstrumentPart2.velFrom);
/* 157 */           if (localSimpleInstrumentPart2.velTo < localModelPerformer2.getVelTo())
/* 158 */             localModelPerformer2.setVelTo(localSimpleInstrumentPart2.velTo);
/* 159 */           localModelPerformer2.getOscillators().addAll(localModelPerformer1.getOscillators());
/* 160 */           localModelPerformer2.getConnectionBlocks().addAll(localModelPerformer1.getConnectionBlocks());
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 166 */     return localObject;
/*     */   }
/*     */ 
/*     */   public Object getData() {
/* 170 */     return null;
/*     */   }
/*     */ 
/*     */   public String getName() {
/* 174 */     return this.name;
/*     */   }
/*     */ 
/*     */   public void setName(String paramString) {
/* 178 */     this.name = paramString;
/*     */   }
/*     */ 
/*     */   public ModelPatch getPatch() {
/* 182 */     return new ModelPatch(this.bank, this.preset, this.percussion);
/*     */   }
/*     */ 
/*     */   public void setPatch(Patch paramPatch) {
/* 186 */     if (((paramPatch instanceof ModelPatch)) && (((ModelPatch)paramPatch).isPercussion())) {
/* 187 */       this.percussion = true;
/* 188 */       this.bank = paramPatch.getBank();
/* 189 */       this.preset = paramPatch.getProgram();
/*     */     } else {
/* 191 */       this.percussion = false;
/* 192 */       this.bank = paramPatch.getBank();
/* 193 */       this.preset = paramPatch.getProgram();
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class SimpleInstrumentPart
/*     */   {
/*     */     ModelPerformer[] performers;
/*     */     int keyFrom;
/*     */     int keyTo;
/*     */     int velFrom;
/*     */     int velTo;
/*     */     int exclusiveClass;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.SimpleInstrument
 * JD-Core Version:    0.6.2
 */