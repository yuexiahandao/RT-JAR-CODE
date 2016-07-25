/*     */ package com.sun.media.sound;
/*     */ 
/*     */ import java.util.Vector;
/*     */ import javax.sound.sampled.Control;
/*     */ import javax.sound.sampled.Line;
/*     */ import javax.sound.sampled.Line.Info;
/*     */ import javax.sound.sampled.LineUnavailableException;
/*     */ import javax.sound.sampled.Mixer;
/*     */ import javax.sound.sampled.Mixer.Info;
/*     */ 
/*     */ abstract class AbstractMixer extends AbstractLine
/*     */   implements Mixer
/*     */ {
/*     */   protected static final int PCM = 0;
/*     */   protected static final int ULAW = 1;
/*     */   protected static final int ALAW = 2;
/*     */   private final Mixer.Info mixerInfo;
/*     */   protected Line.Info[] sourceLineInfo;
/*     */   protected Line.Info[] targetLineInfo;
/*  70 */   private boolean started = false;
/*     */ 
/*  77 */   private boolean manuallyOpened = false;
/*     */ 
/*  93 */   private final Vector sourceLines = new Vector();
/*     */ 
/*  99 */   private final Vector targetLines = new Vector();
/*     */ 
/*     */   protected AbstractMixer(Mixer.Info paramInfo, Control[] paramArrayOfControl, Line.Info[] paramArrayOfInfo1, Line.Info[] paramArrayOfInfo2)
/*     */   {
/* 113 */     super(new Line.Info(Mixer.class), null, paramArrayOfControl);
/*     */ 
/* 116 */     this.mixer = this;
/* 117 */     if (paramArrayOfControl == null) {
/* 118 */       paramArrayOfControl = new Control[0];
/*     */     }
/*     */ 
/* 122 */     this.mixerInfo = paramInfo;
/* 123 */     this.sourceLineInfo = paramArrayOfInfo1;
/* 124 */     this.targetLineInfo = paramArrayOfInfo2;
/*     */   }
/*     */ 
/*     */   public final Mixer.Info getMixerInfo()
/*     */   {
/* 132 */     return this.mixerInfo;
/*     */   }
/*     */ 
/*     */   public final Line.Info[] getSourceLineInfo()
/*     */   {
/* 137 */     Line.Info[] arrayOfInfo = new Line.Info[this.sourceLineInfo.length];
/* 138 */     System.arraycopy(this.sourceLineInfo, 0, arrayOfInfo, 0, this.sourceLineInfo.length);
/* 139 */     return arrayOfInfo;
/*     */   }
/*     */ 
/*     */   public final Line.Info[] getTargetLineInfo()
/*     */   {
/* 145 */     Line.Info[] arrayOfInfo = new Line.Info[this.targetLineInfo.length];
/* 146 */     System.arraycopy(this.targetLineInfo, 0, arrayOfInfo, 0, this.targetLineInfo.length);
/* 147 */     return arrayOfInfo;
/*     */   }
/*     */ 
/*     */   public final Line.Info[] getSourceLineInfo(Line.Info paramInfo)
/*     */   {
/* 154 */     Vector localVector = new Vector();
/*     */ 
/* 156 */     for (int i = 0; i < this.sourceLineInfo.length; i++)
/*     */     {
/* 158 */       if (paramInfo.matches(this.sourceLineInfo[i])) {
/* 159 */         localVector.addElement(this.sourceLineInfo[i]);
/*     */       }
/*     */     }
/*     */ 
/* 163 */     Line.Info[] arrayOfInfo = new Line.Info[localVector.size()];
/* 164 */     for (i = 0; i < arrayOfInfo.length; i++) {
/* 165 */       arrayOfInfo[i] = ((Line.Info)localVector.elementAt(i));
/*     */     }
/*     */ 
/* 168 */     return arrayOfInfo;
/*     */   }
/*     */ 
/*     */   public final Line.Info[] getTargetLineInfo(Line.Info paramInfo)
/*     */   {
/* 175 */     Vector localVector = new Vector();
/*     */ 
/* 177 */     for (int i = 0; i < this.targetLineInfo.length; i++)
/*     */     {
/* 179 */       if (paramInfo.matches(this.targetLineInfo[i])) {
/* 180 */         localVector.addElement(this.targetLineInfo[i]);
/*     */       }
/*     */     }
/*     */ 
/* 184 */     Line.Info[] arrayOfInfo = new Line.Info[localVector.size()];
/* 185 */     for (i = 0; i < arrayOfInfo.length; i++) {
/* 186 */       arrayOfInfo[i] = ((Line.Info)localVector.elementAt(i));
/*     */     }
/*     */ 
/* 189 */     return arrayOfInfo;
/*     */   }
/*     */ 
/*     */   public final boolean isLineSupported(Line.Info paramInfo)
/*     */   {
/* 197 */     for (int i = 0; i < this.sourceLineInfo.length; i++)
/*     */     {
/* 199 */       if (paramInfo.matches(this.sourceLineInfo[i])) {
/* 200 */         return true;
/*     */       }
/*     */     }
/*     */ 
/* 204 */     for (i = 0; i < this.targetLineInfo.length; i++)
/*     */     {
/* 206 */       if (paramInfo.matches(this.targetLineInfo[i])) {
/* 207 */         return true;
/*     */       }
/*     */     }
/*     */ 
/* 211 */     return false;
/*     */   }
/*     */ 
/*     */   public abstract Line getLine(Line.Info paramInfo)
/*     */     throws LineUnavailableException;
/*     */ 
/*     */   public abstract int getMaxLines(Line.Info paramInfo);
/*     */ 
/*     */   protected abstract void implOpen()
/*     */     throws LineUnavailableException;
/*     */ 
/*     */   protected abstract void implStart();
/*     */ 
/*     */   protected abstract void implStop();
/*     */ 
/*     */   protected abstract void implClose();
/*     */ 
/*     */   public final Line[] getSourceLines()
/*     */   {
/*     */     Line[] arrayOfLine;
/* 229 */     synchronized (this.sourceLines)
/*     */     {
/* 231 */       arrayOfLine = new Line[this.sourceLines.size()];
/*     */ 
/* 233 */       for (int i = 0; i < arrayOfLine.length; i++) {
/* 234 */         arrayOfLine[i] = ((Line)this.sourceLines.elementAt(i));
/*     */       }
/*     */     }
/*     */ 
/* 238 */     return arrayOfLine;
/*     */   }
/*     */ 
/*     */   public final Line[] getTargetLines()
/*     */   {
/*     */     Line[] arrayOfLine;
/* 246 */     synchronized (this.targetLines)
/*     */     {
/* 248 */       arrayOfLine = new Line[this.targetLines.size()];
/*     */ 
/* 250 */       for (int i = 0; i < arrayOfLine.length; i++) {
/* 251 */         arrayOfLine[i] = ((Line)this.targetLines.elementAt(i));
/*     */       }
/*     */     }
/*     */ 
/* 255 */     return arrayOfLine;
/*     */   }
/*     */ 
/*     */   public final void synchronize(Line[] paramArrayOfLine, boolean paramBoolean)
/*     */   {
/* 263 */     throw new IllegalArgumentException("Synchronization not supported by this mixer.");
/*     */   }
/*     */ 
/*     */   public final void unsynchronize(Line[] paramArrayOfLine)
/*     */   {
/* 271 */     throw new IllegalArgumentException("Synchronization not supported by this mixer.");
/*     */   }
/*     */ 
/*     */   public final boolean isSynchronizationSupported(Line[] paramArrayOfLine, boolean paramBoolean)
/*     */   {
/* 280 */     return false;
/*     */   }
/*     */ 
/*     */   public final synchronized void open()
/*     */     throws LineUnavailableException
/*     */   {
/* 290 */     open(true);
/*     */   }
/*     */ 
/*     */   final synchronized void open(boolean paramBoolean)
/*     */     throws LineUnavailableException
/*     */   {
/* 298 */     if (!isOpen()) {
/* 299 */       implOpen();
/*     */ 
/* 301 */       setOpen(true);
/* 302 */       if (paramBoolean)
/* 303 */         this.manuallyOpened = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   final synchronized void open(Line paramLine)
/*     */     throws LineUnavailableException
/*     */   {
/* 326 */     if (equals(paramLine))
/*     */     {
/* 328 */       return;
/*     */     }
/*     */ 
/* 332 */     if (isSourceLine(paramLine.getLineInfo())) {
/* 333 */       if (!this.sourceLines.contains(paramLine))
/*     */       {
/* 336 */         open(false);
/*     */ 
/* 339 */         this.sourceLines.addElement(paramLine);
/*     */       }
/*     */ 
/*     */     }
/* 343 */     else if ((isTargetLine(paramLine.getLineInfo())) && 
/* 344 */       (!this.targetLines.contains(paramLine)))
/*     */     {
/* 347 */       open(false);
/*     */ 
/* 350 */       this.targetLines.addElement(paramLine);
/*     */     }
/*     */   }
/*     */ 
/*     */   final synchronized void close(Line paramLine)
/*     */   {
/* 371 */     if (equals(paramLine))
/*     */     {
/* 373 */       return;
/*     */     }
/*     */ 
/* 376 */     this.sourceLines.removeElement(paramLine);
/* 377 */     this.targetLines.removeElement(paramLine);
/*     */ 
/* 383 */     if ((this.sourceLines.isEmpty()) && (this.targetLines.isEmpty()) && (!this.manuallyOpened))
/*     */     {
/* 385 */       close();
/*     */     }
/*     */   }
/*     */ 
/*     */   public final synchronized void close()
/*     */   {
/* 397 */     if (isOpen())
/*     */     {
/* 399 */       Line[] arrayOfLine = getSourceLines();
/* 400 */       for (int i = 0; i < arrayOfLine.length; i++) {
/* 401 */         arrayOfLine[i].close();
/*     */       }
/*     */ 
/* 405 */       arrayOfLine = getTargetLines();
/* 406 */       for (i = 0; i < arrayOfLine.length; i++) {
/* 407 */         arrayOfLine[i].close();
/*     */       }
/*     */ 
/* 410 */       implClose();
/*     */ 
/* 413 */       setOpen(false);
/*     */     }
/* 415 */     this.manuallyOpened = false;
/*     */   }
/*     */ 
/*     */   final synchronized void start(Line paramLine)
/*     */   {
/* 427 */     if (equals(paramLine))
/*     */     {
/* 429 */       return;
/*     */     }
/*     */ 
/* 433 */     if (!this.started)
/*     */     {
/* 435 */       implStart();
/* 436 */       this.started = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   final synchronized void stop(Line paramLine)
/*     */   {
/* 451 */     if (equals(paramLine))
/*     */     {
/* 453 */       return;
/*     */     }
/*     */ 
/* 456 */     Vector localVector1 = (Vector)this.sourceLines.clone();
/* 457 */     for (int i = 0; i < localVector1.size(); i++)
/*     */     {
/* 462 */       if ((localVector1.elementAt(i) instanceof AbstractDataLine)) {
/* 463 */         AbstractDataLine localAbstractDataLine1 = (AbstractDataLine)localVector1.elementAt(i);
/* 464 */         if ((localAbstractDataLine1.isStartedRunning()) && (!localAbstractDataLine1.equals(paramLine)))
/*     */         {
/* 466 */           return;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 471 */     Vector localVector2 = (Vector)this.targetLines.clone();
/* 472 */     for (int j = 0; j < localVector2.size(); j++)
/*     */     {
/* 476 */       if ((localVector2.elementAt(j) instanceof AbstractDataLine)) {
/* 477 */         AbstractDataLine localAbstractDataLine2 = (AbstractDataLine)localVector2.elementAt(j);
/* 478 */         if ((localAbstractDataLine2.isStartedRunning()) && (!localAbstractDataLine2.equals(paramLine)))
/*     */         {
/* 480 */           return;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 487 */     this.started = false;
/* 488 */     implStop();
/*     */   }
/*     */ 
/*     */   final boolean isSourceLine(Line.Info paramInfo)
/*     */   {
/* 502 */     for (int i = 0; i < this.sourceLineInfo.length; i++) {
/* 503 */       if (paramInfo.matches(this.sourceLineInfo[i])) {
/* 504 */         return true;
/*     */       }
/*     */     }
/*     */ 
/* 508 */     return false;
/*     */   }
/*     */ 
/*     */   final boolean isTargetLine(Line.Info paramInfo)
/*     */   {
/* 519 */     for (int i = 0; i < this.targetLineInfo.length; i++) {
/* 520 */       if (paramInfo.matches(this.targetLineInfo[i])) {
/* 521 */         return true;
/*     */       }
/*     */     }
/*     */ 
/* 525 */     return false;
/*     */   }
/*     */ 
/*     */   final Line.Info getLineInfo(Line.Info paramInfo)
/*     */   {
/* 535 */     if (paramInfo == null) {
/* 536 */       return null;
/*     */     }
/*     */ 
/* 541 */     for (int i = 0; i < this.sourceLineInfo.length; i++) {
/* 542 */       if (paramInfo.matches(this.sourceLineInfo[i])) {
/* 543 */         return this.sourceLineInfo[i];
/*     */       }
/*     */     }
/*     */ 
/* 547 */     for (i = 0; i < this.targetLineInfo.length; i++) {
/* 548 */       if (paramInfo.matches(this.targetLineInfo[i])) {
/* 549 */         return this.targetLineInfo[i];
/*     */       }
/*     */     }
/*     */ 
/* 553 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.AbstractMixer
 * JD-Core Version:    0.6.2
 */