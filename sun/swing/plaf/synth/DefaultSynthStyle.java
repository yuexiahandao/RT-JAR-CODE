/*     */ package sun.swing.plaf.synth;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Font;
/*     */ import java.awt.Insets;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.UIDefaults.LazyValue;
/*     */ import javax.swing.plaf.UIResource;
/*     */ import javax.swing.plaf.synth.ColorType;
/*     */ import javax.swing.plaf.synth.Region;
/*     */ import javax.swing.plaf.synth.SynthContext;
/*     */ import javax.swing.plaf.synth.SynthGraphicsUtils;
/*     */ import javax.swing.plaf.synth.SynthPainter;
/*     */ import javax.swing.plaf.synth.SynthStyle;
/*     */ 
/*     */ public class DefaultSynthStyle extends SynthStyle
/*     */   implements Cloneable
/*     */ {
/*     */   private static final String PENDING = "Pending";
/*     */   private boolean opaque;
/*     */   private Insets insets;
/*     */   private StateInfo[] states;
/*     */   private Map data;
/*     */   private Font font;
/*     */   private SynthGraphicsUtils synthGraphics;
/*     */   private SynthPainter painter;
/*     */ 
/*     */   public DefaultSynthStyle()
/*     */   {
/*     */   }
/*     */ 
/*     */   public DefaultSynthStyle(DefaultSynthStyle paramDefaultSynthStyle)
/*     */   {
/*  96 */     this.opaque = paramDefaultSynthStyle.opaque;
/*  97 */     if (paramDefaultSynthStyle.insets != null) {
/*  98 */       this.insets = new Insets(paramDefaultSynthStyle.insets.top, paramDefaultSynthStyle.insets.left, paramDefaultSynthStyle.insets.bottom, paramDefaultSynthStyle.insets.right);
/*     */     }
/*     */ 
/* 101 */     if (paramDefaultSynthStyle.states != null) {
/* 102 */       this.states = new StateInfo[paramDefaultSynthStyle.states.length];
/* 103 */       for (int i = paramDefaultSynthStyle.states.length - 1; i >= 0; 
/* 104 */         i--) {
/* 105 */         this.states[i] = ((StateInfo)paramDefaultSynthStyle.states[i].clone());
/*     */       }
/*     */     }
/* 108 */     if (paramDefaultSynthStyle.data != null) {
/* 109 */       this.data = new HashMap();
/* 110 */       this.data.putAll(paramDefaultSynthStyle.data);
/*     */     }
/* 112 */     this.font = paramDefaultSynthStyle.font;
/* 113 */     this.synthGraphics = paramDefaultSynthStyle.synthGraphics;
/* 114 */     this.painter = paramDefaultSynthStyle.painter;
/*     */   }
/*     */ 
/*     */   public DefaultSynthStyle(Insets paramInsets, boolean paramBoolean, StateInfo[] paramArrayOfStateInfo, Map paramMap)
/*     */   {
/* 128 */     this.insets = paramInsets;
/* 129 */     this.opaque = paramBoolean;
/* 130 */     this.states = paramArrayOfStateInfo;
/* 131 */     this.data = paramMap;
/*     */   }
/*     */ 
/*     */   public Color getColor(SynthContext paramSynthContext, ColorType paramColorType) {
/* 135 */     return getColor(paramSynthContext.getComponent(), paramSynthContext.getRegion(), paramSynthContext.getComponentState(), paramColorType);
/*     */   }
/*     */ 
/*     */   public Color getColor(JComponent paramJComponent, Region paramRegion, int paramInt, ColorType paramColorType)
/*     */   {
/* 142 */     if ((!paramRegion.isSubregion()) && (paramInt == 1)) {
/* 143 */       if (paramColorType == ColorType.BACKGROUND) {
/* 144 */         return paramJComponent.getBackground();
/*     */       }
/* 146 */       if (paramColorType == ColorType.FOREGROUND) {
/* 147 */         return paramJComponent.getForeground();
/*     */       }
/* 149 */       if (paramColorType == ColorType.TEXT_FOREGROUND)
/*     */       {
/* 154 */         localColor = paramJComponent.getForeground();
/* 155 */         if (!(localColor instanceof UIResource)) {
/* 156 */           return localColor;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 161 */     Color localColor = getColorForState(paramJComponent, paramRegion, paramInt, paramColorType);
/* 162 */     if (localColor == null)
/*     */     {
/* 164 */       if ((paramColorType == ColorType.BACKGROUND) || (paramColorType == ColorType.TEXT_BACKGROUND))
/*     */       {
/* 166 */         return paramJComponent.getBackground();
/*     */       }
/* 168 */       if ((paramColorType == ColorType.FOREGROUND) || (paramColorType == ColorType.TEXT_FOREGROUND))
/*     */       {
/* 170 */         return paramJComponent.getForeground();
/*     */       }
/*     */     }
/* 173 */     return localColor;
/*     */   }
/*     */ 
/*     */   protected Color getColorForState(SynthContext paramSynthContext, ColorType paramColorType) {
/* 177 */     return getColorForState(paramSynthContext.getComponent(), paramSynthContext.getRegion(), paramSynthContext.getComponentState(), paramColorType);
/*     */   }
/*     */ 
/*     */   protected Color getColorForState(JComponent paramJComponent, Region paramRegion, int paramInt, ColorType paramColorType)
/*     */   {
/* 193 */     StateInfo localStateInfo = getStateInfo(paramInt);
/*     */     Color localColor;
/* 195 */     if ((localStateInfo != null) && ((localColor = localStateInfo.getColor(paramColorType)) != null)) {
/* 196 */       return localColor;
/*     */     }
/* 198 */     if ((localStateInfo == null) || (localStateInfo.getComponentState() != 0)) {
/* 199 */       localStateInfo = getStateInfo(0);
/* 200 */       if (localStateInfo != null) {
/* 201 */         return localStateInfo.getColor(paramColorType);
/*     */       }
/*     */     }
/* 204 */     return null;
/*     */   }
/*     */ 
/*     */   public void setFont(Font paramFont)
/*     */   {
/* 214 */     this.font = paramFont;
/*     */   }
/*     */ 
/*     */   public Font getFont(SynthContext paramSynthContext) {
/* 218 */     return getFont(paramSynthContext.getComponent(), paramSynthContext.getRegion(), paramSynthContext.getComponentState());
/*     */   }
/*     */ 
/*     */   public Font getFont(JComponent paramJComponent, Region paramRegion, int paramInt)
/*     */   {
/* 223 */     if ((!paramRegion.isSubregion()) && (paramInt == 1)) {
/* 224 */       return paramJComponent.getFont();
/*     */     }
/* 226 */     Font localFont = paramJComponent.getFont();
/* 227 */     if ((localFont != null) && (!(localFont instanceof UIResource))) {
/* 228 */       return localFont;
/*     */     }
/* 230 */     return getFontForState(paramJComponent, paramRegion, paramInt);
/*     */   }
/*     */ 
/*     */   protected Font getFontForState(JComponent paramJComponent, Region paramRegion, int paramInt)
/*     */   {
/* 243 */     if (paramJComponent == null) {
/* 244 */       return this.font;
/*     */     }
/*     */ 
/* 247 */     StateInfo localStateInfo = getStateInfo(paramInt);
/*     */     Font localFont;
/* 249 */     if ((localStateInfo != null) && ((localFont = localStateInfo.getFont()) != null)) {
/* 250 */       return localFont;
/*     */     }
/* 252 */     if ((localStateInfo == null) || (localStateInfo.getComponentState() != 0)) {
/* 253 */       localStateInfo = getStateInfo(0);
/* 254 */       if ((localStateInfo != null) && ((localFont = localStateInfo.getFont()) != null)) {
/* 255 */         return localFont;
/*     */       }
/*     */     }
/*     */ 
/* 259 */     return this.font;
/*     */   }
/*     */ 
/*     */   protected Font getFontForState(SynthContext paramSynthContext) {
/* 263 */     return getFontForState(paramSynthContext.getComponent(), paramSynthContext.getRegion(), paramSynthContext.getComponentState());
/*     */   }
/*     */ 
/*     */   public void setGraphicsUtils(SynthGraphicsUtils paramSynthGraphicsUtils)
/*     */   {
/* 273 */     this.synthGraphics = paramSynthGraphicsUtils;
/*     */   }
/*     */ 
/*     */   public SynthGraphicsUtils getGraphicsUtils(SynthContext paramSynthContext)
/*     */   {
/* 283 */     if (this.synthGraphics == null) {
/* 284 */       return super.getGraphicsUtils(paramSynthContext);
/*     */     }
/* 286 */     return this.synthGraphics;
/*     */   }
/*     */ 
/*     */   public void setInsets(Insets paramInsets)
/*     */   {
/* 295 */     this.insets = paramInsets;
/*     */   }
/*     */ 
/*     */   public Insets getInsets(SynthContext paramSynthContext, Insets paramInsets)
/*     */   {
/* 308 */     if (paramInsets == null) {
/* 309 */       paramInsets = new Insets(0, 0, 0, 0);
/*     */     }
/* 311 */     if (this.insets != null) {
/* 312 */       paramInsets.left = this.insets.left;
/* 313 */       paramInsets.right = this.insets.right;
/* 314 */       paramInsets.top = this.insets.top;
/* 315 */       paramInsets.bottom = this.insets.bottom;
/*     */     }
/*     */     else {
/* 318 */       paramInsets.left = (paramInsets.right = paramInsets.top = paramInsets.bottom = 0);
/*     */     }
/* 320 */     return paramInsets;
/*     */   }
/*     */ 
/*     */   public void setPainter(SynthPainter paramSynthPainter)
/*     */   {
/* 329 */     this.painter = paramSynthPainter;
/*     */   }
/*     */ 
/*     */   public SynthPainter getPainter(SynthContext paramSynthContext)
/*     */   {
/* 339 */     return this.painter;
/*     */   }
/*     */ 
/*     */   public void setOpaque(boolean paramBoolean)
/*     */   {
/* 348 */     this.opaque = paramBoolean;
/*     */   }
/*     */ 
/*     */   public boolean isOpaque(SynthContext paramSynthContext)
/*     */   {
/* 360 */     return this.opaque;
/*     */   }
/*     */ 
/*     */   public void setData(Map paramMap)
/*     */   {
/* 370 */     this.data = paramMap;
/*     */   }
/*     */ 
/*     */   public Map getData()
/*     */   {
/* 379 */     return this.data;
/*     */   }
/*     */ 
/*     */   public Object get(SynthContext paramSynthContext, Object paramObject)
/*     */   {
/* 391 */     StateInfo localStateInfo = getStateInfo(paramSynthContext.getComponentState());
/* 392 */     if ((localStateInfo != null) && (localStateInfo.getData() != null) && (getKeyFromData(localStateInfo.getData(), paramObject) != null)) {
/* 393 */       return getKeyFromData(localStateInfo.getData(), paramObject);
/*     */     }
/* 395 */     localStateInfo = getStateInfo(0);
/* 396 */     if ((localStateInfo != null) && (localStateInfo.getData() != null) && (getKeyFromData(localStateInfo.getData(), paramObject) != null)) {
/* 397 */       return getKeyFromData(localStateInfo.getData(), paramObject);
/*     */     }
/* 399 */     if (getKeyFromData(this.data, paramObject) != null)
/* 400 */       return getKeyFromData(this.data, paramObject);
/* 401 */     return getDefaultValue(paramSynthContext, paramObject);
/*     */   }
/*     */ 
/*     */   private Object getKeyFromData(Map paramMap, Object paramObject)
/*     */   {
/* 406 */     Object localObject1 = null;
/* 407 */     if (paramMap != null)
/*     */     {
/* 409 */       synchronized (paramMap) {
/* 410 */         localObject1 = paramMap.get(paramObject);
/*     */       }
/* 412 */       while (localObject1 == "Pending") {
/* 413 */         synchronized (paramMap) {
/*     */           try {
/* 415 */             paramMap.wait(); } catch (InterruptedException localInterruptedException) {
/*     */           }
/* 417 */           localObject1 = paramMap.get(paramObject);
/*     */         }
/*     */       }
/* 420 */       if ((localObject1 instanceof UIDefaults.LazyValue)) {
/* 421 */         synchronized (paramMap) {
/* 422 */           paramMap.put(paramObject, "Pending");
/*     */         }
/* 424 */         localObject1 = ((UIDefaults.LazyValue)localObject1).createValue(null);
/* 425 */         synchronized (paramMap) {
/* 426 */           paramMap.put(paramObject, localObject1);
/* 427 */           paramMap.notifyAll();
/*     */         }
/*     */       }
/*     */     }
/* 431 */     return localObject1;
/*     */   }
/*     */ 
/*     */   public Object getDefaultValue(SynthContext paramSynthContext, Object paramObject)
/*     */   {
/* 443 */     return super.get(paramSynthContext, paramObject);
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/*     */     DefaultSynthStyle localDefaultSynthStyle;
/*     */     try
/*     */     {
/* 454 */       localDefaultSynthStyle = (DefaultSynthStyle)super.clone();
/*     */     } catch (CloneNotSupportedException localCloneNotSupportedException) {
/* 456 */       return null;
/*     */     }
/* 458 */     if (this.states != null) {
/* 459 */       localDefaultSynthStyle.states = new StateInfo[this.states.length];
/* 460 */       for (int i = this.states.length - 1; i >= 0; i--) {
/* 461 */         localDefaultSynthStyle.states[i] = ((StateInfo)this.states[i].clone());
/*     */       }
/*     */     }
/* 464 */     if (this.data != null) {
/* 465 */       localDefaultSynthStyle.data = new HashMap();
/* 466 */       localDefaultSynthStyle.data.putAll(this.data);
/*     */     }
/* 468 */     return localDefaultSynthStyle;
/*     */   }
/*     */ 
/*     */   public DefaultSynthStyle addTo(DefaultSynthStyle paramDefaultSynthStyle)
/*     */   {
/* 483 */     if (this.insets != null) {
/* 484 */       paramDefaultSynthStyle.insets = this.insets;
/*     */     }
/* 486 */     if (this.font != null) {
/* 487 */       paramDefaultSynthStyle.font = this.font;
/*     */     }
/* 489 */     if (this.painter != null) {
/* 490 */       paramDefaultSynthStyle.painter = this.painter;
/*     */     }
/* 492 */     if (this.synthGraphics != null) {
/* 493 */       paramDefaultSynthStyle.synthGraphics = this.synthGraphics;
/*     */     }
/* 495 */     paramDefaultSynthStyle.opaque = this.opaque;
/* 496 */     if (this.states != null)
/*     */     {
/*     */       int i;
/* 497 */       if (paramDefaultSynthStyle.states == null) {
/* 498 */         paramDefaultSynthStyle.states = new StateInfo[this.states.length];
/* 499 */         for (i = this.states.length - 1; i >= 0; i--) {
/* 500 */           if (this.states[i] != null) {
/* 501 */             paramDefaultSynthStyle.states[i] = ((StateInfo)this.states[i].clone());
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 510 */         i = 0;
/*     */ 
/* 512 */         int j = 0;
/* 513 */         int k = paramDefaultSynthStyle.states.length;
/*     */         int n;
/*     */         int i1;
/*     */         int i2;
/* 514 */         for (int m = this.states.length - 1; m >= 0; 
/* 515 */           m--) {
/* 516 */           n = this.states[m].getComponentState();
/* 517 */           i1 = 0;
/*     */ 
/* 519 */           for (i2 = k - 1 - j; 
/* 520 */             i2 >= 0; i2--) {
/* 521 */             if (n == paramDefaultSynthStyle.states[i2].getComponentState())
/*     */             {
/* 523 */               paramDefaultSynthStyle.states[i2] = this.states[m].addTo(paramDefaultSynthStyle.states[i2]);
/*     */ 
/* 526 */               StateInfo localStateInfo = paramDefaultSynthStyle.states[(k - 1 - j)];
/*     */ 
/* 528 */               paramDefaultSynthStyle.states[(k - 1 - j)] = paramDefaultSynthStyle.states[i2];
/*     */ 
/* 530 */               paramDefaultSynthStyle.states[i2] = localStateInfo;
/* 531 */               j++;
/* 532 */               i1 = 1;
/* 533 */               break;
/*     */             }
/*     */           }
/* 536 */           if (i1 == 0) {
/* 537 */             i++;
/*     */           }
/*     */         }
/* 540 */         if (i != 0)
/*     */         {
/* 544 */           StateInfo[] arrayOfStateInfo = new StateInfo[i + k];
/*     */ 
/* 546 */           n = k;
/*     */ 
/* 548 */           System.arraycopy(paramDefaultSynthStyle.states, 0, arrayOfStateInfo, 0, k);
/* 549 */           for (i1 = this.states.length - 1; i1 >= 0; 
/* 550 */             i1--) {
/* 551 */             i2 = this.states[i1].getComponentState();
/* 552 */             int i3 = 0;
/*     */ 
/* 554 */             for (int i4 = k - 1; i4 >= 0; 
/* 555 */               i4--) {
/* 556 */               if (i2 == paramDefaultSynthStyle.states[i4].getComponentState())
/*     */               {
/* 558 */                 i3 = 1;
/* 559 */                 break;
/*     */               }
/*     */             }
/* 562 */             if (i3 == 0) {
/* 563 */               arrayOfStateInfo[(n++)] = ((StateInfo)this.states[i1].clone());
/*     */             }
/*     */           }
/*     */ 
/* 567 */           paramDefaultSynthStyle.states = arrayOfStateInfo;
/*     */         }
/*     */       }
/*     */     }
/* 571 */     if (this.data != null) {
/* 572 */       if (paramDefaultSynthStyle.data == null) {
/* 573 */         paramDefaultSynthStyle.data = new HashMap();
/*     */       }
/* 575 */       paramDefaultSynthStyle.data.putAll(this.data);
/*     */     }
/* 577 */     return paramDefaultSynthStyle;
/*     */   }
/*     */ 
/*     */   public void setStateInfo(StateInfo[] paramArrayOfStateInfo)
/*     */   {
/* 587 */     this.states = paramArrayOfStateInfo;
/*     */   }
/*     */ 
/*     */   public StateInfo[] getStateInfo()
/*     */   {
/* 597 */     return this.states;
/*     */   }
/*     */ 
/*     */   public StateInfo getStateInfo(int paramInt)
/*     */   {
/* 622 */     if (this.states != null) {
/* 623 */       int i = 0;
/* 624 */       int j = -1;
/* 625 */       int k = -1;
/*     */ 
/* 627 */       if (paramInt == 0) {
/* 628 */         for (m = this.states.length - 1; m >= 0; m--) {
/* 629 */           if (this.states[m].getComponentState() == 0) {
/* 630 */             return this.states[m];
/*     */           }
/*     */         }
/* 633 */         return null;
/*     */       }
/* 635 */       for (int m = this.states.length - 1; m >= 0; m--) {
/* 636 */         int n = this.states[m].getComponentState();
/*     */ 
/* 638 */         if (n == 0) {
/* 639 */           if (k == -1) {
/* 640 */             k = m;
/*     */           }
/*     */         }
/* 643 */         else if ((paramInt & n) == n)
/*     */         {
/* 650 */           int i1 = n;
/* 651 */           i1 -= ((0xAAAAAAAA & i1) >>> 1);
/* 652 */           i1 = (i1 & 0x33333333) + (i1 >>> 2 & 0x33333333);
/*     */ 
/* 654 */           i1 = i1 + (i1 >>> 4) & 0xF0F0F0F;
/* 655 */           i1 += (i1 >>> 8);
/* 656 */           i1 += (i1 >>> 16);
/* 657 */           i1 &= 255;
/* 658 */           if (i1 > i) {
/* 659 */             j = m;
/* 660 */             i = i1;
/*     */           }
/*     */         }
/*     */       }
/* 664 */       if (j != -1) {
/* 665 */         return this.states[j];
/*     */       }
/* 667 */       if (k != -1) {
/* 668 */         return this.states[k];
/*     */       }
/*     */     }
/* 671 */     return null;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 676 */     StringBuffer localStringBuffer = new StringBuffer();
/*     */ 
/* 678 */     localStringBuffer.append(super.toString()).append(',');
/*     */ 
/* 680 */     localStringBuffer.append("data=").append(this.data).append(',');
/*     */ 
/* 682 */     localStringBuffer.append("font=").append(this.font).append(',');
/*     */ 
/* 684 */     localStringBuffer.append("insets=").append(this.insets).append(',');
/*     */ 
/* 686 */     localStringBuffer.append("synthGraphics=").append(this.synthGraphics).append(',');
/*     */ 
/* 688 */     localStringBuffer.append("painter=").append(this.painter).append(',');
/*     */ 
/* 690 */     StateInfo[] arrayOfStateInfo1 = getStateInfo();
/* 691 */     if (arrayOfStateInfo1 != null) {
/* 692 */       localStringBuffer.append("states[");
/* 693 */       for (StateInfo localStateInfo : arrayOfStateInfo1) {
/* 694 */         localStringBuffer.append(localStateInfo.toString()).append(',');
/*     */       }
/* 696 */       localStringBuffer.append(']').append(',');
/*     */     }
/*     */ 
/* 700 */     localStringBuffer.deleteCharAt(localStringBuffer.length() - 1);
/*     */ 
/* 702 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   public static class StateInfo
/*     */   {
/*     */     private Map data;
/*     */     private Font font;
/*     */     private Color[] colors;
/*     */     private int state;
/*     */ 
/*     */     public StateInfo()
/*     */     {
/*     */     }
/*     */ 
/*     */     public StateInfo(int paramInt, Font paramFont, Color[] paramArrayOfColor)
/*     */     {
/* 733 */       this.state = paramInt;
/* 734 */       this.font = paramFont;
/* 735 */       this.colors = paramArrayOfColor;
/*     */     }
/*     */ 
/*     */     public StateInfo(StateInfo paramStateInfo)
/*     */     {
/* 745 */       this.state = paramStateInfo.state;
/* 746 */       this.font = paramStateInfo.font;
/* 747 */       if (paramStateInfo.data != null) {
/* 748 */         if (this.data == null) {
/* 749 */           this.data = new HashMap();
/*     */         }
/* 751 */         this.data.putAll(paramStateInfo.data);
/*     */       }
/* 753 */       if (paramStateInfo.colors != null) {
/* 754 */         this.colors = new Color[paramStateInfo.colors.length];
/* 755 */         System.arraycopy(paramStateInfo.colors, 0, this.colors, 0, paramStateInfo.colors.length);
/*     */       }
/*     */     }
/*     */ 
/*     */     public Map getData() {
/* 760 */       return this.data;
/*     */     }
/*     */ 
/*     */     public void setData(Map paramMap) {
/* 764 */       this.data = paramMap;
/*     */     }
/*     */ 
/*     */     public void setFont(Font paramFont)
/*     */     {
/* 773 */       this.font = paramFont;
/*     */     }
/*     */ 
/*     */     public Font getFont()
/*     */     {
/* 782 */       return this.font;
/*     */     }
/*     */ 
/*     */     public void setColors(Color[] paramArrayOfColor)
/*     */     {
/* 792 */       this.colors = paramArrayOfColor;
/*     */     }
/*     */ 
/*     */     public Color[] getColors()
/*     */     {
/* 802 */       return this.colors;
/*     */     }
/*     */ 
/*     */     public Color getColor(ColorType paramColorType)
/*     */     {
/* 811 */       if (this.colors != null) {
/* 812 */         int i = paramColorType.getID();
/*     */ 
/* 814 */         if (i < this.colors.length) {
/* 815 */           return this.colors[i];
/*     */         }
/*     */       }
/* 818 */       return null;
/*     */     }
/*     */ 
/*     */     public StateInfo addTo(StateInfo paramStateInfo)
/*     */     {
/* 834 */       if (this.font != null) {
/* 835 */         paramStateInfo.font = this.font;
/*     */       }
/* 837 */       if (this.data != null) {
/* 838 */         if (paramStateInfo.data == null) {
/* 839 */           paramStateInfo.data = new HashMap();
/*     */         }
/* 841 */         paramStateInfo.data.putAll(this.data);
/*     */       }
/* 843 */       if (this.colors != null) {
/* 844 */         if (paramStateInfo.colors == null) {
/* 845 */           paramStateInfo.colors = new Color[this.colors.length];
/* 846 */           System.arraycopy(this.colors, 0, paramStateInfo.colors, 0, this.colors.length);
/*     */         }
/*     */         else
/*     */         {
/* 850 */           if (paramStateInfo.colors.length < this.colors.length) {
/* 851 */             Color[] arrayOfColor = paramStateInfo.colors;
/*     */ 
/* 853 */             paramStateInfo.colors = new Color[this.colors.length];
/* 854 */             System.arraycopy(arrayOfColor, 0, paramStateInfo.colors, 0, arrayOfColor.length);
/*     */           }
/* 856 */           for (int i = this.colors.length - 1; i >= 0; 
/* 857 */             i--) {
/* 858 */             if (this.colors[i] != null) {
/* 859 */               paramStateInfo.colors[i] = this.colors[i];
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/* 864 */       return paramStateInfo;
/*     */     }
/*     */ 
/*     */     public void setComponentState(int paramInt)
/*     */     {
/* 874 */       this.state = paramInt;
/*     */     }
/*     */ 
/*     */     public int getComponentState()
/*     */     {
/* 884 */       return this.state;
/*     */     }
/*     */ 
/*     */     private int getMatchCount(int paramInt)
/*     */     {
/* 893 */       paramInt &= this.state;
/* 894 */       paramInt -= ((0xAAAAAAAA & paramInt) >>> 1);
/* 895 */       paramInt = (paramInt & 0x33333333) + (paramInt >>> 2 & 0x33333333);
/* 896 */       paramInt = paramInt + (paramInt >>> 4) & 0xF0F0F0F;
/* 897 */       paramInt += (paramInt >>> 8);
/* 898 */       paramInt += (paramInt >>> 16);
/* 899 */       return paramInt & 0xFF;
/*     */     }
/*     */ 
/*     */     public Object clone()
/*     */     {
/* 908 */       return new StateInfo(this);
/*     */     }
/*     */ 
/*     */     public String toString() {
/* 912 */       StringBuffer localStringBuffer = new StringBuffer();
/*     */ 
/* 914 */       localStringBuffer.append(super.toString()).append(',');
/*     */ 
/* 916 */       localStringBuffer.append("state=").append(Integer.toString(this.state)).append(',');
/*     */ 
/* 918 */       localStringBuffer.append("font=").append(this.font).append(',');
/*     */ 
/* 920 */       if (this.colors != null) {
/* 921 */         localStringBuffer.append("colors=").append(Arrays.asList(this.colors)).append(',');
/*     */       }
/*     */ 
/* 924 */       return localStringBuffer.toString();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.swing.plaf.synth.DefaultSynthStyle
 * JD-Core Version:    0.6.2
 */