/*     */ package com.sun.media.sound;
/*     */ 
/*     */ import java.util.Vector;
/*     */ import javax.sound.sampled.BooleanControl;
/*     */ import javax.sound.sampled.BooleanControl.Type;
/*     */ import javax.sound.sampled.CompoundControl;
/*     */ import javax.sound.sampled.CompoundControl.Type;
/*     */ import javax.sound.sampled.Control;
/*     */ import javax.sound.sampled.FloatControl;
/*     */ import javax.sound.sampled.FloatControl.Type;
/*     */ import javax.sound.sampled.Line;
/*     */ import javax.sound.sampled.Line.Info;
/*     */ import javax.sound.sampled.LineUnavailableException;
/*     */ import javax.sound.sampled.Port;
/*     */ import javax.sound.sampled.Port.Info;
/*     */ 
/*     */ final class PortMixer extends AbstractMixer
/*     */ {
/*     */   private static final int SRC_UNKNOWN = 1;
/*     */   private static final int SRC_MICROPHONE = 2;
/*     */   private static final int SRC_LINE_IN = 3;
/*     */   private static final int SRC_COMPACT_DISC = 4;
/*     */   private static final int SRC_MASK = 255;
/*     */   private static final int DST_UNKNOWN = 256;
/*     */   private static final int DST_SPEAKER = 512;
/*     */   private static final int DST_HEADPHONE = 768;
/*     */   private static final int DST_LINE_OUT = 1024;
/*     */   private static final int DST_MASK = 65280;
/*     */   private Port.Info[] portInfos;
/*     */   private PortMixerPort[] ports;
/*  65 */   private long id = 0L;
/*     */ 
/*     */   PortMixer(PortMixerProvider.PortMixerInfo paramPortMixerInfo)
/*     */   {
/*  70 */     super(paramPortMixerInfo, null, null, null);
/*     */ 
/*  77 */     int i = 0;
/*  78 */     int j = 0;
/*  79 */     int k = 0;
/*     */     try
/*     */     {
/*     */       try {
/*  83 */         this.id = nOpen(getMixerIndex());
/*  84 */         if (this.id != 0L) {
/*  85 */           i = nGetPortCount(this.id);
/*  86 */           if (i < 0)
/*     */           {
/*  88 */             i = 0;
/*     */           }
/*     */         }
/*     */       } catch (Exception localException) {
/*     */       }
/*  93 */       this.portInfos = new Port.Info[i];
/*     */ 
/*  95 */       for (m = 0; m < i; m++) {
/*  96 */         int n = nGetPortType(this.id, m);
/*  97 */         j += ((n & 0xFF) != 0 ? 1 : 0);
/*  98 */         k += ((n & 0xFF00) != 0 ? 1 : 0);
/*  99 */         this.portInfos[m] = getPortInfo(m, n);
/*     */       }
/*     */     } finally {
/* 102 */       if (this.id != 0L) {
/* 103 */         nClose(this.id);
/*     */       }
/* 105 */       this.id = 0L;
/*     */     }
/*     */ 
/* 109 */     this.sourceLineInfo = new Port.Info[j];
/* 110 */     this.targetLineInfo = new Port.Info[k];
/*     */ 
/* 112 */     j = 0; k = 0;
/* 113 */     for (int m = 0; m < i; m++)
/* 114 */       if (this.portInfos[m].isSource())
/* 115 */         this.sourceLineInfo[(j++)] = this.portInfos[m];
/*     */       else
/* 117 */         this.targetLineInfo[(k++)] = this.portInfos[m];
/*     */   }
/*     */ 
/*     */   public Line getLine(Line.Info paramInfo)
/*     */     throws LineUnavailableException
/*     */   {
/* 128 */     Line.Info localInfo = getLineInfo(paramInfo);
/*     */ 
/* 130 */     if ((localInfo != null) && ((localInfo instanceof Port.Info))) {
/* 131 */       for (int i = 0; i < this.portInfos.length; i++) {
/* 132 */         if (localInfo.equals(this.portInfos[i])) {
/* 133 */           return getPort(i);
/*     */         }
/*     */       }
/*     */     }
/* 137 */     throw new IllegalArgumentException("Line unsupported: " + paramInfo);
/*     */   }
/*     */ 
/*     */   public int getMaxLines(Line.Info paramInfo)
/*     */   {
/* 142 */     Line.Info localInfo = getLineInfo(paramInfo);
/*     */ 
/* 145 */     if (localInfo == null) {
/* 146 */       return 0;
/*     */     }
/*     */ 
/* 149 */     if ((localInfo instanceof Port.Info))
/*     */     {
/* 151 */       return 1;
/*     */     }
/* 153 */     return 0;
/*     */   }
/*     */ 
/*     */   protected void implOpen()
/*     */     throws LineUnavailableException
/*     */   {
/* 161 */     this.id = nOpen(getMixerIndex());
/*     */   }
/*     */ 
/*     */   protected void implClose()
/*     */   {
/* 170 */     long l = this.id;
/* 171 */     this.id = 0L;
/* 172 */     nClose(l);
/* 173 */     if (this.ports != null)
/* 174 */       for (int i = 0; i < this.ports.length; i++)
/* 175 */         if (this.ports[i] != null)
/* 176 */           this.ports[i].disposeControls();
/*     */   }
/*     */ 
/*     */   protected void implStart()
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void implStop()
/*     */   {
/*     */   }
/*     */ 
/*     */   private Port.Info getPortInfo(int paramInt1, int paramInt2)
/*     */   {
/* 190 */     switch (paramInt2) { case 1:
/* 191 */       return new PortInfo(nGetPortName(getID(), paramInt1), true, null);
/*     */     case 2:
/* 192 */       return Port.Info.MICROPHONE;
/*     */     case 3:
/* 193 */       return Port.Info.LINE_IN;
/*     */     case 4:
/* 194 */       return Port.Info.COMPACT_DISC;
/*     */     case 256:
/* 196 */       return new PortInfo(nGetPortName(getID(), paramInt1), false, null);
/*     */     case 512:
/* 197 */       return Port.Info.SPEAKER;
/*     */     case 768:
/* 198 */       return Port.Info.HEADPHONE;
/*     */     case 1024:
/* 199 */       return Port.Info.LINE_OUT;
/*     */     }
/*     */ 
/* 203 */     return null;
/*     */   }
/*     */ 
/*     */   int getMixerIndex() {
/* 207 */     return ((PortMixerProvider.PortMixerInfo)getMixerInfo()).getIndex();
/*     */   }
/*     */ 
/*     */   Port getPort(int paramInt) {
/* 211 */     if (this.ports == null) {
/* 212 */       this.ports = new PortMixerPort[this.portInfos.length];
/*     */     }
/* 214 */     if (this.ports[paramInt] == null) {
/* 215 */       this.ports[paramInt] = new PortMixerPort(this.portInfos[paramInt], this, paramInt, null);
/* 216 */       return this.ports[paramInt];
/*     */     }
/*     */ 
/* 219 */     return this.ports[paramInt];
/*     */   }
/*     */ 
/*     */   long getID() {
/* 223 */     return this.id;
/*     */   }
/*     */ 
/*     */   private static native long nOpen(int paramInt)
/*     */     throws LineUnavailableException;
/*     */ 
/*     */   private static native void nClose(long paramLong);
/*     */ 
/*     */   private static native int nGetPortCount(long paramLong);
/*     */ 
/*     */   private static native int nGetPortType(long paramLong, int paramInt);
/*     */ 
/*     */   private static native String nGetPortName(long paramLong, int paramInt);
/*     */ 
/*     */   private static native void nGetControls(long paramLong, int paramInt, Vector paramVector);
/*     */ 
/*     */   private static native void nControlSetIntValue(long paramLong, int paramInt);
/*     */ 
/*     */   private static native int nControlGetIntValue(long paramLong);
/*     */ 
/*     */   private static native void nControlSetFloatValue(long paramLong, float paramFloat);
/*     */ 
/*     */   private static native float nControlGetFloatValue(long paramLong);
/*     */ 
/*     */   private static final class BoolCtrl extends BooleanControl
/*     */   {
/*     */     private final long controlID;
/* 350 */     private boolean closed = false;
/*     */ 
/*     */     private static BooleanControl.Type createType(String paramString) {
/* 353 */       if (paramString.equals("Mute")) {
/* 354 */         return BooleanControl.Type.MUTE;
/*     */       }
/* 356 */       if (paramString.equals("Select"));
/* 360 */       return new BCT(paramString, null);
/*     */     }
/*     */ 
/*     */     private BoolCtrl(long paramLong, String paramString)
/*     */     {
/* 365 */       this(paramLong, createType(paramString));
/*     */     }
/*     */ 
/*     */     private BoolCtrl(long paramLong, BooleanControl.Type paramType) {
/* 369 */       super(false);
/* 370 */       this.controlID = paramLong;
/*     */     }
/*     */ 
/*     */     public void setValue(boolean paramBoolean) {
/* 374 */       if (!this.closed)
/* 375 */         PortMixer.nControlSetIntValue(this.controlID, paramBoolean ? 1 : 0);
/*     */     }
/*     */ 
/*     */     public boolean getValue()
/*     */     {
/* 380 */       if (!this.closed)
/*     */       {
/* 382 */         return PortMixer.nControlGetIntValue(this.controlID) != 0;
/*     */       }
/*     */ 
/* 385 */       return false;
/*     */     }
/*     */ 
/*     */     private static final class BCT extends BooleanControl.Type
/*     */     {
/*     */       private BCT(String paramString)
/*     */       {
/* 393 */         super();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private static final class CompCtrl extends CompoundControl
/*     */   {
/*     */     private CompCtrl(String paramString, Control[] paramArrayOfControl)
/*     */     {
/* 403 */       super(paramArrayOfControl);
/*     */     }
/*     */ 
/*     */     private static final class CCT extends CompoundControl.Type
/*     */     {
/*     */       private CCT(String paramString)
/*     */       {
/* 411 */         super();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private static final class FloatCtrl extends FloatControl
/*     */   {
/*     */     private final long controlID;
/* 422 */     private boolean closed = false;
/*     */ 
/* 425 */     private static final FloatControl.Type[] FLOAT_CONTROL_TYPES = { null, FloatControl.Type.BALANCE, FloatControl.Type.MASTER_GAIN, FloatControl.Type.PAN, FloatControl.Type.VOLUME };
/*     */ 
/*     */     private FloatCtrl(long paramLong, String paramString1, float paramFloat1, float paramFloat2, float paramFloat3, String paramString2)
/*     */     {
/* 435 */       this(paramLong, new FCT(paramString1, null), paramFloat1, paramFloat2, paramFloat3, paramString2);
/*     */     }
/*     */ 
/*     */     private FloatCtrl(long paramLong, int paramInt, float paramFloat1, float paramFloat2, float paramFloat3, String paramString)
/*     */     {
/* 440 */       this(paramLong, FLOAT_CONTROL_TYPES[paramInt], paramFloat1, paramFloat2, paramFloat3, paramString);
/*     */     }
/*     */ 
/*     */     private FloatCtrl(long paramLong, FloatControl.Type paramType, float paramFloat1, float paramFloat2, float paramFloat3, String paramString)
/*     */     {
/* 445 */       super(paramFloat1, paramFloat2, paramFloat3, 1000, paramFloat1, paramString);
/* 446 */       this.controlID = paramLong;
/*     */     }
/*     */ 
/*     */     public void setValue(float paramFloat) {
/* 450 */       if (!this.closed)
/* 451 */         PortMixer.nControlSetFloatValue(this.controlID, paramFloat);
/*     */     }
/*     */ 
/*     */     public float getValue()
/*     */     {
/* 456 */       if (!this.closed)
/*     */       {
/* 458 */         return PortMixer.nControlGetFloatValue(this.controlID);
/*     */       }
/*     */ 
/* 461 */       return getMinimum();
/*     */     }
/*     */ 
/*     */     private static final class FCT extends FloatControl.Type
/*     */     {
/*     */       private FCT(String paramString)
/*     */       {
/* 469 */         super();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private static final class PortInfo extends Port.Info
/*     */   {
/*     */     private PortInfo(String paramString, boolean paramBoolean)
/*     */     {
/* 479 */       super(paramString, paramBoolean);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static final class PortMixerPort extends AbstractLine
/*     */     implements Port
/*     */   {
/*     */     private final int portIndex;
/*     */     private long id;
/*     */ 
/*     */     private PortMixerPort(Port.Info paramInfo, PortMixer paramPortMixer, int paramInt)
/*     */     {
/* 241 */       super(paramPortMixer, null);
/*     */ 
/* 243 */       this.portIndex = paramInt;
/*     */     }
/*     */ 
/*     */     void implOpen()
/*     */       throws LineUnavailableException
/*     */     {
/* 253 */       long l = ((PortMixer)this.mixer).getID();
/* 254 */       if ((this.id == 0L) || (l != this.id) || (this.controls.length == 0)) {
/* 255 */         this.id = l;
/* 256 */         Vector localVector = new Vector();
/* 257 */         synchronized (localVector) {
/* 258 */           PortMixer.nGetControls(this.id, this.portIndex, localVector);
/* 259 */           this.controls = new Control[localVector.size()];
/* 260 */           for (int i = 0; i < this.controls.length; i++)
/* 261 */             this.controls[i] = ((Control)localVector.elementAt(i));
/*     */         }
/*     */       }
/*     */       else {
/* 265 */         enableControls(this.controls, true);
/*     */       }
/*     */     }
/*     */ 
/*     */     private void enableControls(Control[] paramArrayOfControl, boolean paramBoolean)
/*     */     {
/* 271 */       for (int i = 0; i < paramArrayOfControl.length; i++)
/* 272 */         if ((paramArrayOfControl[i] instanceof PortMixer.BoolCtrl)) {
/* 273 */           ((PortMixer.BoolCtrl)paramArrayOfControl[i]).closed = (!paramBoolean);
/*     */         }
/* 275 */         else if ((paramArrayOfControl[i] instanceof PortMixer.FloatCtrl)) {
/* 276 */           ((PortMixer.FloatCtrl)paramArrayOfControl[i]).closed = (!paramBoolean);
/*     */         }
/* 278 */         else if ((paramArrayOfControl[i] instanceof CompoundControl))
/* 279 */           enableControls(((CompoundControl)paramArrayOfControl[i]).getMemberControls(), paramBoolean);
/*     */     }
/*     */ 
/*     */     private void disposeControls()
/*     */     {
/* 285 */       enableControls(this.controls, false);
/* 286 */       this.controls = new Control[0];
/*     */     }
/*     */ 
/*     */     void implClose()
/*     */     {
/* 293 */       enableControls(this.controls, false);
/*     */     }
/*     */ 
/*     */     public void open()
/*     */       throws LineUnavailableException
/*     */     {
/* 301 */       synchronized (this.mixer)
/*     */       {
/* 303 */         if (!isOpen())
/*     */         {
/* 306 */           this.mixer.open(this);
/*     */           try
/*     */           {
/* 309 */             implOpen();
/*     */ 
/* 312 */             setOpen(true);
/*     */           }
/*     */           catch (LineUnavailableException localLineUnavailableException) {
/* 315 */             this.mixer.close(this);
/* 316 */             throw localLineUnavailableException;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/*     */     public void close()
/*     */     {
/* 325 */       synchronized (this.mixer) {
/* 326 */         if (isOpen())
/*     */         {
/* 330 */           setOpen(false);
/*     */ 
/* 333 */           implClose();
/*     */ 
/* 336 */           this.mixer.close(this);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.PortMixer
 * JD-Core Version:    0.6.2
 */