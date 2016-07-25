/*     */ package com.sun.media.sound;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ public final class ModelPerformer
/*     */ {
/*  38 */   private final List<ModelOscillator> oscillators = new ArrayList();
/*  39 */   private List<ModelConnectionBlock> connectionBlocks = new ArrayList();
/*     */ 
/*  41 */   private int keyFrom = 0;
/*  42 */   private int keyTo = 127;
/*  43 */   private int velFrom = 0;
/*  44 */   private int velTo = 127;
/*  45 */   private int exclusiveClass = 0;
/*  46 */   private boolean releaseTrigger = false;
/*  47 */   private boolean selfNonExclusive = false;
/*  48 */   private Object userObject = null;
/*  49 */   private boolean addDefaultConnections = true;
/*  50 */   private String name = null;
/*     */ 
/*     */   public String getName() {
/*  53 */     return this.name;
/*     */   }
/*     */ 
/*     */   public void setName(String paramString) {
/*  57 */     this.name = paramString;
/*     */   }
/*     */ 
/*     */   public List<ModelConnectionBlock> getConnectionBlocks() {
/*  61 */     return this.connectionBlocks;
/*     */   }
/*     */ 
/*     */   public void setConnectionBlocks(List<ModelConnectionBlock> paramList) {
/*  65 */     this.connectionBlocks = paramList;
/*     */   }
/*     */ 
/*     */   public List<ModelOscillator> getOscillators() {
/*  69 */     return this.oscillators;
/*     */   }
/*     */ 
/*     */   public int getExclusiveClass() {
/*  73 */     return this.exclusiveClass;
/*     */   }
/*     */ 
/*     */   public void setExclusiveClass(int paramInt) {
/*  77 */     this.exclusiveClass = paramInt;
/*     */   }
/*     */ 
/*     */   public boolean isSelfNonExclusive() {
/*  81 */     return this.selfNonExclusive;
/*     */   }
/*     */ 
/*     */   public void setSelfNonExclusive(boolean paramBoolean) {
/*  85 */     this.selfNonExclusive = paramBoolean;
/*     */   }
/*     */ 
/*     */   public int getKeyFrom() {
/*  89 */     return this.keyFrom;
/*     */   }
/*     */ 
/*     */   public void setKeyFrom(int paramInt) {
/*  93 */     this.keyFrom = paramInt;
/*     */   }
/*     */ 
/*     */   public int getKeyTo() {
/*  97 */     return this.keyTo;
/*     */   }
/*     */ 
/*     */   public void setKeyTo(int paramInt) {
/* 101 */     this.keyTo = paramInt;
/*     */   }
/*     */ 
/*     */   public int getVelFrom() {
/* 105 */     return this.velFrom;
/*     */   }
/*     */ 
/*     */   public void setVelFrom(int paramInt) {
/* 109 */     this.velFrom = paramInt;
/*     */   }
/*     */ 
/*     */   public int getVelTo() {
/* 113 */     return this.velTo;
/*     */   }
/*     */ 
/*     */   public void setVelTo(int paramInt) {
/* 117 */     this.velTo = paramInt;
/*     */   }
/*     */ 
/*     */   public boolean isReleaseTriggered() {
/* 121 */     return this.releaseTrigger;
/*     */   }
/*     */ 
/*     */   public void setReleaseTriggered(boolean paramBoolean) {
/* 125 */     this.releaseTrigger = paramBoolean;
/*     */   }
/*     */ 
/*     */   public Object getUserObject() {
/* 129 */     return this.userObject;
/*     */   }
/*     */ 
/*     */   public void setUserObject(Object paramObject) {
/* 133 */     this.userObject = paramObject;
/*     */   }
/*     */ 
/*     */   public boolean isDefaultConnectionsEnabled() {
/* 137 */     return this.addDefaultConnections;
/*     */   }
/*     */ 
/*     */   public void setDefaultConnectionsEnabled(boolean paramBoolean) {
/* 141 */     this.addDefaultConnections = paramBoolean;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.ModelPerformer
 * JD-Core Version:    0.6.2
 */