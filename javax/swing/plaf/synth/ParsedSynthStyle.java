/*      */ package javax.swing.plaf.synth;
/*      */ 
/*      */ import java.awt.Graphics;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import sun.swing.plaf.synth.DefaultSynthStyle;
/*      */ import sun.swing.plaf.synth.DefaultSynthStyle.StateInfo;
/*      */ 
/*      */ class ParsedSynthStyle extends DefaultSynthStyle
/*      */ {
/*   38 */   private static SynthPainter DELEGATING_PAINTER_INSTANCE = new DelegatingPainter(null);
/*      */   private PainterInfo[] _painters;
/*      */ 
/*      */   private static PainterInfo[] mergePainterInfo(PainterInfo[] paramArrayOfPainterInfo1, PainterInfo[] paramArrayOfPainterInfo2)
/*      */   {
/*   44 */     if (paramArrayOfPainterInfo1 == null) {
/*   45 */       return paramArrayOfPainterInfo2;
/*      */     }
/*   47 */     if (paramArrayOfPainterInfo2 == null) {
/*   48 */       return paramArrayOfPainterInfo1;
/*      */     }
/*   50 */     int i = paramArrayOfPainterInfo1.length;
/*   51 */     int j = paramArrayOfPainterInfo2.length;
/*   52 */     int k = 0;
/*   53 */     PainterInfo[] arrayOfPainterInfo1 = new PainterInfo[i + j];
/*   54 */     System.arraycopy(paramArrayOfPainterInfo1, 0, arrayOfPainterInfo1, 0, i);
/*   55 */     for (int m = 0; m < j; m++) {
/*   56 */       int n = 0;
/*   57 */       for (int i1 = 0; i1 < i - k; 
/*   58 */         i1++) {
/*   59 */         if (paramArrayOfPainterInfo2[m].equalsPainter(paramArrayOfPainterInfo1[i1])) {
/*   60 */           arrayOfPainterInfo1[i1] = paramArrayOfPainterInfo2[m];
/*   61 */           k++;
/*   62 */           n = 1;
/*   63 */           break;
/*      */         }
/*      */       }
/*   66 */       if (n == 0) {
/*   67 */         arrayOfPainterInfo1[(i + m - k)] = paramArrayOfPainterInfo2[m];
/*      */       }
/*      */     }
/*   70 */     if (k > 0) {
/*   71 */       PainterInfo[] arrayOfPainterInfo2 = arrayOfPainterInfo1;
/*   72 */       arrayOfPainterInfo1 = new PainterInfo[arrayOfPainterInfo1.length - k];
/*   73 */       System.arraycopy(arrayOfPainterInfo2, 0, arrayOfPainterInfo1, 0, arrayOfPainterInfo1.length);
/*      */     }
/*   75 */     return arrayOfPainterInfo1;
/*      */   }
/*      */ 
/*      */   public ParsedSynthStyle()
/*      */   {
/*      */   }
/*      */ 
/*      */   public ParsedSynthStyle(DefaultSynthStyle paramDefaultSynthStyle) {
/*   83 */     super(paramDefaultSynthStyle);
/*   84 */     if ((paramDefaultSynthStyle instanceof ParsedSynthStyle)) {
/*   85 */       ParsedSynthStyle localParsedSynthStyle = (ParsedSynthStyle)paramDefaultSynthStyle;
/*      */ 
/*   87 */       if (localParsedSynthStyle._painters != null)
/*   88 */         this._painters = localParsedSynthStyle._painters;
/*      */     }
/*      */   }
/*      */ 
/*      */   public SynthPainter getPainter(SynthContext paramSynthContext)
/*      */   {
/*   94 */     return DELEGATING_PAINTER_INSTANCE;
/*      */   }
/*      */ 
/*      */   public void setPainters(PainterInfo[] paramArrayOfPainterInfo) {
/*   98 */     this._painters = paramArrayOfPainterInfo;
/*      */   }
/*      */ 
/*      */   public DefaultSynthStyle addTo(DefaultSynthStyle paramDefaultSynthStyle) {
/*  102 */     if (!(paramDefaultSynthStyle instanceof ParsedSynthStyle)) {
/*  103 */       paramDefaultSynthStyle = new ParsedSynthStyle(paramDefaultSynthStyle);
/*      */     }
/*  105 */     ParsedSynthStyle localParsedSynthStyle = (ParsedSynthStyle)super.addTo(paramDefaultSynthStyle);
/*  106 */     localParsedSynthStyle._painters = mergePainterInfo(localParsedSynthStyle._painters, this._painters);
/*  107 */     return localParsedSynthStyle;
/*      */   }
/*      */ 
/*      */   private SynthPainter getBestPainter(SynthContext paramSynthContext, String paramString, int paramInt)
/*      */   {
/*  113 */     StateInfo localStateInfo = (StateInfo)getStateInfo(paramSynthContext.getComponentState());
/*      */     SynthPainter localSynthPainter;
/*  115 */     if ((localStateInfo != null) && 
/*  116 */       ((localSynthPainter = getBestPainter(localStateInfo.getPainters(), paramString, paramInt)) != null))
/*      */     {
/*  118 */       return localSynthPainter;
/*      */     }
/*      */ 
/*  121 */     if ((localSynthPainter = getBestPainter(this._painters, paramString, paramInt)) != null) {
/*  122 */       return localSynthPainter;
/*      */     }
/*  124 */     return SynthPainter.NULL_PAINTER;
/*      */   }
/*      */ 
/*      */   private SynthPainter getBestPainter(PainterInfo[] paramArrayOfPainterInfo, String paramString, int paramInt)
/*      */   {
/*  129 */     if (paramArrayOfPainterInfo != null)
/*      */     {
/*  131 */       SynthPainter localSynthPainter1 = null;
/*      */ 
/*  133 */       SynthPainter localSynthPainter2 = null;
/*      */ 
/*  135 */       for (int i = paramArrayOfPainterInfo.length - 1; i >= 0; i--) {
/*  136 */         PainterInfo localPainterInfo = paramArrayOfPainterInfo[i];
/*      */ 
/*  138 */         if (localPainterInfo.getMethod() == paramString) {
/*  139 */           if (localPainterInfo.getDirection() == paramInt) {
/*  140 */             return localPainterInfo.getPainter();
/*      */           }
/*  142 */           if ((localSynthPainter2 == null) && (localPainterInfo.getDirection() == -1)) {
/*  143 */             localSynthPainter2 = localPainterInfo.getPainter();
/*      */           }
/*      */         }
/*  146 */         else if ((localSynthPainter1 == null) && (localPainterInfo.getMethod() == null)) {
/*  147 */           localSynthPainter1 = localPainterInfo.getPainter();
/*      */         }
/*      */       }
/*  150 */       if (localSynthPainter2 != null) {
/*  151 */         return localSynthPainter2;
/*      */       }
/*  153 */       return localSynthPainter1;
/*      */     }
/*  155 */     return null;
/*      */   }
/*      */ 
/*      */   public String toString() {
/*  159 */     StringBuffer localStringBuffer = new StringBuffer(super.toString());
/*  160 */     if (this._painters != null) {
/*  161 */       localStringBuffer.append(",painters=[");
/*  162 */       for (int i = 0; i < this._painters.length; i++) {
/*  163 */         localStringBuffer.append(this._painters[i].toString());
/*      */       }
/*  165 */       localStringBuffer.append("]");
/*      */     }
/*  167 */     return localStringBuffer.toString();
/*      */   }
/*      */ 
/*      */   private static class AggregatePainter extends SynthPainter
/*      */   {
/*      */     private List<SynthPainter> painters;
/*      */ 
/*      */     AggregatePainter(SynthPainter paramSynthPainter)
/*      */     {
/*  271 */       this.painters = new LinkedList();
/*  272 */       this.painters.add(paramSynthPainter);
/*      */     }
/*      */ 
/*      */     void addPainter(SynthPainter paramSynthPainter) {
/*  276 */       if (paramSynthPainter != null)
/*  277 */         this.painters.add(paramSynthPainter);
/*      */     }
/*      */ 
/*      */     public void paintArrowButtonBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/*  283 */       for (SynthPainter localSynthPainter : this.painters)
/*  284 */         localSynthPainter.paintArrowButtonBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintArrowButtonBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/*  290 */       for (SynthPainter localSynthPainter : this.painters)
/*  291 */         localSynthPainter.paintArrowButtonBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintArrowButtonForeground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */     {
/*  298 */       for (SynthPainter localSynthPainter : this.painters)
/*  299 */         localSynthPainter.paintArrowButtonForeground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*      */     }
/*      */ 
/*      */     public void paintButtonBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/*  306 */       for (SynthPainter localSynthPainter : this.painters)
/*  307 */         localSynthPainter.paintButtonBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintButtonBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/*  313 */       for (SynthPainter localSynthPainter : this.painters)
/*  314 */         localSynthPainter.paintButtonBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintCheckBoxMenuItemBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/*  321 */       for (SynthPainter localSynthPainter : this.painters)
/*  322 */         localSynthPainter.paintCheckBoxMenuItemBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintCheckBoxMenuItemBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/*  329 */       for (SynthPainter localSynthPainter : this.painters)
/*  330 */         localSynthPainter.paintCheckBoxMenuItemBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintCheckBoxBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/*  336 */       for (SynthPainter localSynthPainter : this.painters)
/*  337 */         localSynthPainter.paintCheckBoxBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintCheckBoxBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/*  343 */       for (SynthPainter localSynthPainter : this.painters)
/*  344 */         localSynthPainter.paintCheckBoxBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintColorChooserBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/*  351 */       for (SynthPainter localSynthPainter : this.painters)
/*  352 */         localSynthPainter.paintColorChooserBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintColorChooserBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/*  359 */       for (SynthPainter localSynthPainter : this.painters)
/*  360 */         localSynthPainter.paintColorChooserBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintComboBoxBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/*  367 */       for (SynthPainter localSynthPainter : this.painters)
/*  368 */         localSynthPainter.paintComboBoxBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintComboBoxBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/*  375 */       for (SynthPainter localSynthPainter : this.painters)
/*  376 */         localSynthPainter.paintComboBoxBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintDesktopIconBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/*  383 */       for (SynthPainter localSynthPainter : this.painters)
/*  384 */         localSynthPainter.paintDesktopIconBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintDesktopIconBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/*  391 */       for (SynthPainter localSynthPainter : this.painters)
/*  392 */         localSynthPainter.paintDesktopIconBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintDesktopPaneBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/*  399 */       for (SynthPainter localSynthPainter : this.painters)
/*  400 */         localSynthPainter.paintDesktopPaneBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintDesktopPaneBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/*  407 */       for (SynthPainter localSynthPainter : this.painters)
/*  408 */         localSynthPainter.paintDesktopPaneBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintEditorPaneBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/*  414 */       for (SynthPainter localSynthPainter : this.painters)
/*  415 */         localSynthPainter.paintEditorPaneBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintEditorPaneBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/*  421 */       for (SynthPainter localSynthPainter : this.painters)
/*  422 */         localSynthPainter.paintEditorPaneBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintFileChooserBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/*  428 */       for (SynthPainter localSynthPainter : this.painters)
/*  429 */         localSynthPainter.paintFileChooserBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintFileChooserBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/*  435 */       for (SynthPainter localSynthPainter : this.painters)
/*  436 */         localSynthPainter.paintFileChooserBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintFormattedTextFieldBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/*  443 */       for (SynthPainter localSynthPainter : this.painters)
/*  444 */         localSynthPainter.paintFormattedTextFieldBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintFormattedTextFieldBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/*  451 */       for (SynthPainter localSynthPainter : this.painters)
/*  452 */         localSynthPainter.paintFormattedTextFieldBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintInternalFrameTitlePaneBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/*  460 */       for (SynthPainter localSynthPainter : this.painters)
/*  461 */         localSynthPainter.paintInternalFrameTitlePaneBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintInternalFrameTitlePaneBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/*  470 */       for (SynthPainter localSynthPainter : this.painters)
/*  471 */         localSynthPainter.paintInternalFrameTitlePaneBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintInternalFrameBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/*  479 */       for (SynthPainter localSynthPainter : this.painters)
/*  480 */         localSynthPainter.paintInternalFrameBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintInternalFrameBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/*  486 */       for (SynthPainter localSynthPainter : this.painters)
/*  487 */         localSynthPainter.paintInternalFrameBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintLabelBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/*  493 */       for (SynthPainter localSynthPainter : this.painters)
/*  494 */         localSynthPainter.paintLabelBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintLabelBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/*  500 */       for (SynthPainter localSynthPainter : this.painters)
/*  501 */         localSynthPainter.paintLabelBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintListBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/*  507 */       for (SynthPainter localSynthPainter : this.painters)
/*  508 */         localSynthPainter.paintListBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintListBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/*  514 */       for (SynthPainter localSynthPainter : this.painters)
/*  515 */         localSynthPainter.paintListBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintMenuBarBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/*  521 */       for (SynthPainter localSynthPainter : this.painters)
/*  522 */         localSynthPainter.paintMenuBarBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintMenuBarBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/*  528 */       for (SynthPainter localSynthPainter : this.painters)
/*  529 */         localSynthPainter.paintMenuBarBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintMenuItemBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/*  535 */       for (SynthPainter localSynthPainter : this.painters)
/*  536 */         localSynthPainter.paintMenuItemBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintMenuItemBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/*  542 */       for (SynthPainter localSynthPainter : this.painters)
/*  543 */         localSynthPainter.paintMenuItemBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintMenuBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/*  549 */       for (SynthPainter localSynthPainter : this.painters)
/*  550 */         localSynthPainter.paintMenuBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintMenuBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/*  556 */       for (SynthPainter localSynthPainter : this.painters)
/*  557 */         localSynthPainter.paintMenuBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintOptionPaneBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/*  563 */       for (SynthPainter localSynthPainter : this.painters)
/*  564 */         localSynthPainter.paintOptionPaneBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintOptionPaneBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/*  570 */       for (SynthPainter localSynthPainter : this.painters)
/*  571 */         localSynthPainter.paintOptionPaneBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintPanelBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/*  577 */       for (SynthPainter localSynthPainter : this.painters)
/*  578 */         localSynthPainter.paintPanelBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintPanelBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/*  584 */       for (SynthPainter localSynthPainter : this.painters)
/*  585 */         localSynthPainter.paintPanelBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintPasswordFieldBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/*  592 */       for (SynthPainter localSynthPainter : this.painters)
/*  593 */         localSynthPainter.paintPasswordFieldBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintPasswordFieldBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/*  599 */       for (SynthPainter localSynthPainter : this.painters)
/*  600 */         localSynthPainter.paintPasswordFieldBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintPopupMenuBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/*  606 */       for (SynthPainter localSynthPainter : this.painters)
/*  607 */         localSynthPainter.paintPopupMenuBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintPopupMenuBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/*  613 */       for (SynthPainter localSynthPainter : this.painters)
/*  614 */         localSynthPainter.paintPopupMenuBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintProgressBarBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/*  620 */       for (SynthPainter localSynthPainter : this.painters)
/*  621 */         localSynthPainter.paintProgressBarBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintProgressBarBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */     {
/*  628 */       for (SynthPainter localSynthPainter : this.painters)
/*  629 */         localSynthPainter.paintProgressBarBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*      */     }
/*      */ 
/*      */     public void paintProgressBarBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/*  636 */       for (SynthPainter localSynthPainter : this.painters)
/*  637 */         localSynthPainter.paintProgressBarBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintProgressBarBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */     {
/*  644 */       for (SynthPainter localSynthPainter : this.painters)
/*  645 */         localSynthPainter.paintProgressBarBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*      */     }
/*      */ 
/*      */     public void paintProgressBarForeground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */     {
/*  653 */       for (SynthPainter localSynthPainter : this.painters)
/*  654 */         localSynthPainter.paintProgressBarForeground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*      */     }
/*      */ 
/*      */     public void paintRadioButtonMenuItemBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/*  663 */       for (SynthPainter localSynthPainter : this.painters)
/*  664 */         localSynthPainter.paintRadioButtonMenuItemBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintRadioButtonMenuItemBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/*  672 */       for (SynthPainter localSynthPainter : this.painters)
/*  673 */         localSynthPainter.paintRadioButtonMenuItemBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintRadioButtonBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/*  679 */       for (SynthPainter localSynthPainter : this.painters)
/*  680 */         localSynthPainter.paintRadioButtonBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintRadioButtonBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/*  686 */       for (SynthPainter localSynthPainter : this.painters)
/*  687 */         localSynthPainter.paintRadioButtonBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintRootPaneBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/*  693 */       for (SynthPainter localSynthPainter : this.painters)
/*  694 */         localSynthPainter.paintRootPaneBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintRootPaneBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/*  700 */       for (SynthPainter localSynthPainter : this.painters)
/*  701 */         localSynthPainter.paintRootPaneBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintScrollBarBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/*  707 */       for (SynthPainter localSynthPainter : this.painters)
/*  708 */         localSynthPainter.paintScrollBarBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintScrollBarBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */     {
/*  715 */       for (SynthPainter localSynthPainter : this.painters)
/*  716 */         localSynthPainter.paintScrollBarBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*      */     }
/*      */ 
/*      */     public void paintScrollBarBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/*  723 */       for (SynthPainter localSynthPainter : this.painters)
/*  724 */         localSynthPainter.paintScrollBarBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintScrollBarBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */     {
/*  731 */       for (SynthPainter localSynthPainter : this.painters)
/*  732 */         localSynthPainter.paintScrollBarBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*      */     }
/*      */ 
/*      */     public void paintScrollBarThumbBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */     {
/*  740 */       for (SynthPainter localSynthPainter : this.painters)
/*  741 */         localSynthPainter.paintScrollBarThumbBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*      */     }
/*      */ 
/*      */     public void paintScrollBarThumbBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */     {
/*  749 */       for (SynthPainter localSynthPainter : this.painters)
/*  750 */         localSynthPainter.paintScrollBarThumbBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*      */     }
/*      */ 
/*      */     public void paintScrollBarTrackBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/*  758 */       for (SynthPainter localSynthPainter : this.painters)
/*  759 */         localSynthPainter.paintScrollBarTrackBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintScrollBarTrackBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */     {
/*  767 */       for (SynthPainter localSynthPainter : this.painters)
/*  768 */         localSynthPainter.paintScrollBarTrackBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*      */     }
/*      */ 
/*      */     public void paintScrollBarTrackBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/*  775 */       for (SynthPainter localSynthPainter : this.painters)
/*  776 */         localSynthPainter.paintScrollBarTrackBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintScrollBarTrackBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */     {
/*  783 */       for (SynthPainter localSynthPainter : this.painters)
/*  784 */         localSynthPainter.paintScrollBarTrackBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*      */     }
/*      */ 
/*      */     public void paintScrollPaneBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/*  791 */       for (SynthPainter localSynthPainter : this.painters)
/*  792 */         localSynthPainter.paintScrollPaneBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintScrollPaneBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/*  798 */       for (SynthPainter localSynthPainter : this.painters)
/*  799 */         localSynthPainter.paintScrollPaneBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintSeparatorBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/*  805 */       for (SynthPainter localSynthPainter : this.painters)
/*  806 */         localSynthPainter.paintSeparatorBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintSeparatorBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */     {
/*  813 */       for (SynthPainter localSynthPainter : this.painters)
/*  814 */         localSynthPainter.paintSeparatorBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*      */     }
/*      */ 
/*      */     public void paintSeparatorBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/*  820 */       for (SynthPainter localSynthPainter : this.painters)
/*  821 */         localSynthPainter.paintSeparatorBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintSeparatorBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */     {
/*  827 */       for (SynthPainter localSynthPainter : this.painters)
/*  828 */         localSynthPainter.paintSeparatorBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*      */     }
/*      */ 
/*      */     public void paintSeparatorForeground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */     {
/*  835 */       for (SynthPainter localSynthPainter : this.painters)
/*  836 */         localSynthPainter.paintSeparatorForeground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*      */     }
/*      */ 
/*      */     public void paintSliderBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/*  843 */       for (SynthPainter localSynthPainter : this.painters)
/*  844 */         localSynthPainter.paintSliderBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintSliderBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */     {
/*  851 */       for (SynthPainter localSynthPainter : this.painters)
/*  852 */         localSynthPainter.paintSliderBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*      */     }
/*      */ 
/*      */     public void paintSliderBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/*  858 */       for (SynthPainter localSynthPainter : this.painters)
/*  859 */         localSynthPainter.paintSliderBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintSliderBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */     {
/*  866 */       for (SynthPainter localSynthPainter : this.painters)
/*  867 */         localSynthPainter.paintSliderBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*      */     }
/*      */ 
/*      */     public void paintSliderThumbBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */     {
/*  874 */       for (SynthPainter localSynthPainter : this.painters)
/*  875 */         localSynthPainter.paintSliderThumbBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*      */     }
/*      */ 
/*      */     public void paintSliderThumbBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */     {
/*  883 */       for (SynthPainter localSynthPainter : this.painters)
/*  884 */         localSynthPainter.paintSliderThumbBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*      */     }
/*      */ 
/*      */     public void paintSliderTrackBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/*  891 */       for (SynthPainter localSynthPainter : this.painters)
/*  892 */         localSynthPainter.paintSliderTrackBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintSliderTrackBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */     {
/*  899 */       for (SynthPainter localSynthPainter : this.painters)
/*  900 */         localSynthPainter.paintSliderTrackBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*      */     }
/*      */ 
/*      */     public void paintSliderTrackBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/*  907 */       for (SynthPainter localSynthPainter : this.painters)
/*  908 */         localSynthPainter.paintSliderTrackBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintSliderTrackBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */     {
/*  915 */       for (SynthPainter localSynthPainter : this.painters)
/*  916 */         localSynthPainter.paintSliderTrackBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*      */     }
/*      */ 
/*      */     public void paintSpinnerBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/*  923 */       for (SynthPainter localSynthPainter : this.painters)
/*  924 */         localSynthPainter.paintSpinnerBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintSpinnerBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/*  930 */       for (SynthPainter localSynthPainter : this.painters)
/*  931 */         localSynthPainter.paintSpinnerBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintSplitPaneDividerBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/*  938 */       for (SynthPainter localSynthPainter : this.painters)
/*  939 */         localSynthPainter.paintSplitPaneDividerBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintSplitPaneDividerBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */     {
/*  947 */       for (SynthPainter localSynthPainter : this.painters)
/*  948 */         localSynthPainter.paintSplitPaneDividerBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*      */     }
/*      */ 
/*      */     public void paintSplitPaneDividerForeground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */     {
/*  957 */       for (SynthPainter localSynthPainter : this.painters)
/*  958 */         localSynthPainter.paintSplitPaneDividerForeground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*      */     }
/*      */ 
/*      */     public void paintSplitPaneDragDivider(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */     {
/*  967 */       for (SynthPainter localSynthPainter : this.painters)
/*  968 */         localSynthPainter.paintSplitPaneDragDivider(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*      */     }
/*      */ 
/*      */     public void paintSplitPaneBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/*  975 */       for (SynthPainter localSynthPainter : this.painters)
/*  976 */         localSynthPainter.paintSplitPaneBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintSplitPaneBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/*  982 */       for (SynthPainter localSynthPainter : this.painters)
/*  983 */         localSynthPainter.paintSplitPaneBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintTabbedPaneBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/*  989 */       for (SynthPainter localSynthPainter : this.painters)
/*  990 */         localSynthPainter.paintTabbedPaneBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintTabbedPaneBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/*  996 */       for (SynthPainter localSynthPainter : this.painters)
/*  997 */         localSynthPainter.paintTabbedPaneBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintTabbedPaneTabAreaBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1004 */       for (SynthPainter localSynthPainter : this.painters)
/* 1005 */         localSynthPainter.paintTabbedPaneTabAreaBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintTabbedPaneTabAreaBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */     {
/* 1013 */       for (SynthPainter localSynthPainter : this.painters)
/* 1014 */         localSynthPainter.paintTabbedPaneTabAreaBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*      */     }
/*      */ 
/*      */     public void paintTabbedPaneTabAreaBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1022 */       for (SynthPainter localSynthPainter : this.painters)
/* 1023 */         localSynthPainter.paintTabbedPaneTabAreaBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintTabbedPaneTabAreaBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */     {
/* 1030 */       for (SynthPainter localSynthPainter : this.painters)
/* 1031 */         localSynthPainter.paintTabbedPaneTabAreaBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*      */     }
/*      */ 
/*      */     public void paintTabbedPaneTabBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */     {
/* 1039 */       for (SynthPainter localSynthPainter : this.painters)
/* 1040 */         localSynthPainter.paintTabbedPaneTabBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*      */     }
/*      */ 
/*      */     public void paintTabbedPaneTabBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
/*      */     {
/* 1049 */       for (SynthPainter localSynthPainter : this.painters)
/* 1050 */         localSynthPainter.paintTabbedPaneTabBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6);
/*      */     }
/*      */ 
/*      */     public void paintTabbedPaneTabBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */     {
/* 1059 */       for (SynthPainter localSynthPainter : this.painters)
/* 1060 */         localSynthPainter.paintTabbedPaneTabBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*      */     }
/*      */ 
/*      */     public void paintTabbedPaneTabBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
/*      */     {
/* 1068 */       for (SynthPainter localSynthPainter : this.painters)
/* 1069 */         localSynthPainter.paintTabbedPaneTabBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6);
/*      */     }
/*      */ 
/*      */     public void paintTabbedPaneContentBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1078 */       for (SynthPainter localSynthPainter : this.painters)
/* 1079 */         localSynthPainter.paintTabbedPaneContentBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintTabbedPaneContentBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1086 */       for (SynthPainter localSynthPainter : this.painters)
/* 1087 */         localSynthPainter.paintTabbedPaneContentBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintTableHeaderBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1093 */       for (SynthPainter localSynthPainter : this.painters)
/* 1094 */         localSynthPainter.paintTableHeaderBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintTableHeaderBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1100 */       for (SynthPainter localSynthPainter : this.painters)
/* 1101 */         localSynthPainter.paintTableHeaderBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintTableBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1107 */       for (SynthPainter localSynthPainter : this.painters)
/* 1108 */         localSynthPainter.paintTableBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintTableBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1114 */       for (SynthPainter localSynthPainter : this.painters)
/* 1115 */         localSynthPainter.paintTableBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintTextAreaBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1121 */       for (SynthPainter localSynthPainter : this.painters)
/* 1122 */         localSynthPainter.paintTextAreaBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintTextAreaBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1128 */       for (SynthPainter localSynthPainter : this.painters)
/* 1129 */         localSynthPainter.paintTextAreaBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintTextPaneBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1135 */       for (SynthPainter localSynthPainter : this.painters)
/* 1136 */         localSynthPainter.paintTextPaneBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintTextPaneBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1142 */       for (SynthPainter localSynthPainter : this.painters)
/* 1143 */         localSynthPainter.paintTextPaneBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintTextFieldBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1149 */       for (SynthPainter localSynthPainter : this.painters)
/* 1150 */         localSynthPainter.paintTextFieldBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintTextFieldBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1156 */       for (SynthPainter localSynthPainter : this.painters)
/* 1157 */         localSynthPainter.paintTextFieldBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintToggleButtonBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1164 */       for (SynthPainter localSynthPainter : this.painters)
/* 1165 */         localSynthPainter.paintToggleButtonBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintToggleButtonBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1172 */       for (SynthPainter localSynthPainter : this.painters)
/* 1173 */         localSynthPainter.paintToggleButtonBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintToolBarBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1179 */       for (SynthPainter localSynthPainter : this.painters)
/* 1180 */         localSynthPainter.paintToolBarBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintToolBarBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */     {
/* 1187 */       for (SynthPainter localSynthPainter : this.painters)
/* 1188 */         localSynthPainter.paintToolBarBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*      */     }
/*      */ 
/*      */     public void paintToolBarBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1195 */       for (SynthPainter localSynthPainter : this.painters)
/* 1196 */         localSynthPainter.paintToolBarBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintToolBarBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */     {
/* 1203 */       for (SynthPainter localSynthPainter : this.painters)
/* 1204 */         localSynthPainter.paintToolBarBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*      */     }
/*      */ 
/*      */     public void paintToolBarContentBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1211 */       for (SynthPainter localSynthPainter : this.painters)
/* 1212 */         localSynthPainter.paintToolBarContentBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintToolBarContentBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */     {
/* 1220 */       for (SynthPainter localSynthPainter : this.painters)
/* 1221 */         localSynthPainter.paintToolBarContentBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*      */     }
/*      */ 
/*      */     public void paintToolBarContentBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1228 */       for (SynthPainter localSynthPainter : this.painters)
/* 1229 */         localSynthPainter.paintToolBarContentBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintToolBarContentBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */     {
/* 1236 */       for (SynthPainter localSynthPainter : this.painters)
/* 1237 */         localSynthPainter.paintToolBarContentBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*      */     }
/*      */ 
/*      */     public void paintToolBarDragWindowBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1245 */       for (SynthPainter localSynthPainter : this.painters)
/* 1246 */         localSynthPainter.paintToolBarDragWindowBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintToolBarDragWindowBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */     {
/* 1254 */       for (SynthPainter localSynthPainter : this.painters)
/* 1255 */         localSynthPainter.paintToolBarDragWindowBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*      */     }
/*      */ 
/*      */     public void paintToolBarDragWindowBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1263 */       for (SynthPainter localSynthPainter : this.painters)
/* 1264 */         localSynthPainter.paintToolBarDragWindowBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintToolBarDragWindowBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */     {
/* 1272 */       for (SynthPainter localSynthPainter : this.painters)
/* 1273 */         localSynthPainter.paintToolBarDragWindowBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*      */     }
/*      */ 
/*      */     public void paintToolTipBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1281 */       for (SynthPainter localSynthPainter : this.painters)
/* 1282 */         localSynthPainter.paintToolTipBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintToolTipBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1288 */       for (SynthPainter localSynthPainter : this.painters)
/* 1289 */         localSynthPainter.paintToolTipBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintTreeBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1295 */       for (SynthPainter localSynthPainter : this.painters)
/* 1296 */         localSynthPainter.paintTreeBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintTreeBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1302 */       for (SynthPainter localSynthPainter : this.painters)
/* 1303 */         localSynthPainter.paintTreeBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintTreeCellBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1309 */       for (SynthPainter localSynthPainter : this.painters)
/* 1310 */         localSynthPainter.paintTreeCellBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintTreeCellBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1316 */       for (SynthPainter localSynthPainter : this.painters)
/* 1317 */         localSynthPainter.paintTreeCellBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintTreeCellFocus(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1323 */       for (SynthPainter localSynthPainter : this.painters)
/* 1324 */         localSynthPainter.paintTreeCellFocus(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintViewportBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1330 */       for (SynthPainter localSynthPainter : this.painters)
/* 1331 */         localSynthPainter.paintViewportBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintViewportBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1337 */       for (SynthPainter localSynthPainter : this.painters)
/* 1338 */         localSynthPainter.paintViewportBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class DelegatingPainter extends SynthPainter
/*      */   {
/*      */     private static SynthPainter getPainter(SynthContext paramSynthContext, String paramString, int paramInt)
/*      */     {
/* 1346 */       return ((ParsedSynthStyle)paramSynthContext.getStyle()).getBestPainter(paramSynthContext, paramString, paramInt);
/*      */     }
/*      */ 
/*      */     public void paintArrowButtonBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1352 */       getPainter(paramSynthContext, "arrowbuttonbackground", -1).paintArrowButtonBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintArrowButtonBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1358 */       getPainter(paramSynthContext, "arrowbuttonborder", -1).paintArrowButtonBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintArrowButtonForeground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */     {
/* 1364 */       getPainter(paramSynthContext, "arrowbuttonforeground", paramInt5).paintArrowButtonForeground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*      */     }
/*      */ 
/*      */     public void paintButtonBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1370 */       getPainter(paramSynthContext, "buttonbackground", -1).paintButtonBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintButtonBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1376 */       getPainter(paramSynthContext, "buttonborder", -1).paintButtonBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintCheckBoxMenuItemBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1382 */       getPainter(paramSynthContext, "checkboxmenuitembackground", -1).paintCheckBoxMenuItemBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintCheckBoxMenuItemBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1388 */       getPainter(paramSynthContext, "checkboxmenuitemborder", -1).paintCheckBoxMenuItemBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintCheckBoxBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1394 */       getPainter(paramSynthContext, "checkboxbackground", -1).paintCheckBoxBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintCheckBoxBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1400 */       getPainter(paramSynthContext, "checkboxborder", -1).paintCheckBoxBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintColorChooserBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1406 */       getPainter(paramSynthContext, "colorchooserbackground", -1).paintColorChooserBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintColorChooserBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1412 */       getPainter(paramSynthContext, "colorchooserborder", -1).paintColorChooserBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintComboBoxBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1418 */       getPainter(paramSynthContext, "comboboxbackground", -1).paintComboBoxBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintComboBoxBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1424 */       getPainter(paramSynthContext, "comboboxborder", -1).paintComboBoxBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintDesktopIconBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1430 */       getPainter(paramSynthContext, "desktopiconbackground", -1).paintDesktopIconBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintDesktopIconBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1436 */       getPainter(paramSynthContext, "desktopiconborder", -1).paintDesktopIconBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintDesktopPaneBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1442 */       getPainter(paramSynthContext, "desktoppanebackground", -1).paintDesktopPaneBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintDesktopPaneBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1448 */       getPainter(paramSynthContext, "desktoppaneborder", -1).paintDesktopPaneBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintEditorPaneBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1454 */       getPainter(paramSynthContext, "editorpanebackground", -1).paintEditorPaneBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintEditorPaneBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1460 */       getPainter(paramSynthContext, "editorpaneborder", -1).paintEditorPaneBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintFileChooserBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1466 */       getPainter(paramSynthContext, "filechooserbackground", -1).paintFileChooserBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintFileChooserBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1472 */       getPainter(paramSynthContext, "filechooserborder", -1).paintFileChooserBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintFormattedTextFieldBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1478 */       getPainter(paramSynthContext, "formattedtextfieldbackground", -1).paintFormattedTextFieldBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintFormattedTextFieldBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1484 */       getPainter(paramSynthContext, "formattedtextfieldborder", -1).paintFormattedTextFieldBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintInternalFrameTitlePaneBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1490 */       getPainter(paramSynthContext, "internalframetitlepanebackground", -1).paintInternalFrameTitlePaneBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintInternalFrameTitlePaneBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1496 */       getPainter(paramSynthContext, "internalframetitlepaneborder", -1).paintInternalFrameTitlePaneBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintInternalFrameBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1502 */       getPainter(paramSynthContext, "internalframebackground", -1).paintInternalFrameBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintInternalFrameBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1508 */       getPainter(paramSynthContext, "internalframeborder", -1).paintInternalFrameBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintLabelBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1514 */       getPainter(paramSynthContext, "labelbackground", -1).paintLabelBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintLabelBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1520 */       getPainter(paramSynthContext, "labelborder", -1).paintLabelBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintListBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1526 */       getPainter(paramSynthContext, "listbackground", -1).paintListBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintListBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1532 */       getPainter(paramSynthContext, "listborder", -1).paintListBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintMenuBarBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1538 */       getPainter(paramSynthContext, "menubarbackground", -1).paintMenuBarBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintMenuBarBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1544 */       getPainter(paramSynthContext, "menubarborder", -1).paintMenuBarBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintMenuItemBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1550 */       getPainter(paramSynthContext, "menuitembackground", -1).paintMenuItemBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintMenuItemBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1556 */       getPainter(paramSynthContext, "menuitemborder", -1).paintMenuItemBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintMenuBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1562 */       getPainter(paramSynthContext, "menubackground", -1).paintMenuBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintMenuBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1568 */       getPainter(paramSynthContext, "menuborder", -1).paintMenuBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintOptionPaneBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1574 */       getPainter(paramSynthContext, "optionpanebackground", -1).paintOptionPaneBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintOptionPaneBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1580 */       getPainter(paramSynthContext, "optionpaneborder", -1).paintOptionPaneBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintPanelBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1586 */       getPainter(paramSynthContext, "panelbackground", -1).paintPanelBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintPanelBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1592 */       getPainter(paramSynthContext, "panelborder", -1).paintPanelBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintPasswordFieldBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1598 */       getPainter(paramSynthContext, "passwordfieldbackground", -1).paintPasswordFieldBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintPasswordFieldBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1604 */       getPainter(paramSynthContext, "passwordfieldborder", -1).paintPasswordFieldBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintPopupMenuBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1610 */       getPainter(paramSynthContext, "popupmenubackground", -1).paintPopupMenuBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintPopupMenuBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1616 */       getPainter(paramSynthContext, "popupmenuborder", -1).paintPopupMenuBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintProgressBarBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1622 */       getPainter(paramSynthContext, "progressbarbackground", -1).paintProgressBarBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintProgressBarBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */     {
/* 1628 */       getPainter(paramSynthContext, "progressbarbackground", paramInt5).paintProgressBarBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*      */     }
/*      */ 
/*      */     public void paintProgressBarBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1634 */       getPainter(paramSynthContext, "progressbarborder", -1).paintProgressBarBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintProgressBarBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */     {
/* 1640 */       getPainter(paramSynthContext, "progressbarborder", paramInt5).paintProgressBarBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*      */     }
/*      */ 
/*      */     public void paintProgressBarForeground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */     {
/* 1646 */       getPainter(paramSynthContext, "progressbarforeground", paramInt5).paintProgressBarForeground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*      */     }
/*      */ 
/*      */     public void paintRadioButtonMenuItemBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1652 */       getPainter(paramSynthContext, "radiobuttonmenuitembackground", -1).paintRadioButtonMenuItemBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintRadioButtonMenuItemBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1658 */       getPainter(paramSynthContext, "radiobuttonmenuitemborder", -1).paintRadioButtonMenuItemBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintRadioButtonBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1664 */       getPainter(paramSynthContext, "radiobuttonbackground", -1).paintRadioButtonBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintRadioButtonBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1670 */       getPainter(paramSynthContext, "radiobuttonborder", -1).paintRadioButtonBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintRootPaneBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1676 */       getPainter(paramSynthContext, "rootpanebackground", -1).paintRootPaneBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintRootPaneBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1682 */       getPainter(paramSynthContext, "rootpaneborder", -1).paintRootPaneBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintScrollBarBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1688 */       getPainter(paramSynthContext, "scrollbarbackground", -1).paintScrollBarBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintScrollBarBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */     {
/* 1694 */       getPainter(paramSynthContext, "scrollbarbackground", paramInt5).paintScrollBarBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*      */     }
/*      */ 
/*      */     public void paintScrollBarBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1701 */       getPainter(paramSynthContext, "scrollbarborder", -1).paintScrollBarBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintScrollBarBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */     {
/* 1708 */       getPainter(paramSynthContext, "scrollbarborder", paramInt5).paintScrollBarBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*      */     }
/*      */ 
/*      */     public void paintScrollBarThumbBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */     {
/* 1714 */       getPainter(paramSynthContext, "scrollbarthumbbackground", paramInt5).paintScrollBarThumbBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*      */     }
/*      */ 
/*      */     public void paintScrollBarThumbBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */     {
/* 1720 */       getPainter(paramSynthContext, "scrollbarthumbborder", paramInt5).paintScrollBarThumbBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*      */     }
/*      */ 
/*      */     public void paintScrollBarTrackBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1726 */       getPainter(paramSynthContext, "scrollbartrackbackground", -1).paintScrollBarTrackBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintScrollBarTrackBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */     {
/* 1732 */       getPainter(paramSynthContext, "scrollbartrackbackground", paramInt5).paintScrollBarTrackBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*      */     }
/*      */ 
/*      */     public void paintScrollBarTrackBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1738 */       getPainter(paramSynthContext, "scrollbartrackborder", -1).paintScrollBarTrackBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintScrollBarTrackBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */     {
/* 1744 */       getPainter(paramSynthContext, "scrollbartrackborder", paramInt5).paintScrollBarTrackBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*      */     }
/*      */ 
/*      */     public void paintScrollPaneBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1750 */       getPainter(paramSynthContext, "scrollpanebackground", -1).paintScrollPaneBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintScrollPaneBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1756 */       getPainter(paramSynthContext, "scrollpaneborder", -1).paintScrollPaneBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintSeparatorBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1762 */       getPainter(paramSynthContext, "separatorbackground", -1).paintSeparatorBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintSeparatorBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */     {
/* 1768 */       getPainter(paramSynthContext, "separatorbackground", paramInt5).paintSeparatorBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*      */     }
/*      */ 
/*      */     public void paintSeparatorBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1774 */       getPainter(paramSynthContext, "separatorborder", -1).paintSeparatorBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintSeparatorBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */     {
/* 1780 */       getPainter(paramSynthContext, "separatorborder", paramInt5).paintSeparatorBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*      */     }
/*      */ 
/*      */     public void paintSeparatorForeground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */     {
/* 1786 */       getPainter(paramSynthContext, "separatorforeground", paramInt5).paintSeparatorForeground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*      */     }
/*      */ 
/*      */     public void paintSliderBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1792 */       getPainter(paramSynthContext, "sliderbackground", -1).paintSliderBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintSliderBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */     {
/* 1798 */       getPainter(paramSynthContext, "sliderbackground", paramInt5).paintSliderBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*      */     }
/*      */ 
/*      */     public void paintSliderBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1804 */       getPainter(paramSynthContext, "sliderborder", -1).paintSliderBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintSliderBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */     {
/* 1810 */       getPainter(paramSynthContext, "sliderborder", paramInt5).paintSliderBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*      */     }
/*      */ 
/*      */     public void paintSliderThumbBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */     {
/* 1816 */       getPainter(paramSynthContext, "sliderthumbbackground", paramInt5).paintSliderThumbBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*      */     }
/*      */ 
/*      */     public void paintSliderThumbBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */     {
/* 1822 */       getPainter(paramSynthContext, "sliderthumbborder", paramInt5).paintSliderThumbBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*      */     }
/*      */ 
/*      */     public void paintSliderTrackBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1828 */       getPainter(paramSynthContext, "slidertrackbackground", -1).paintSliderTrackBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintSliderTrackBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */     {
/* 1834 */       getPainter(paramSynthContext, "slidertrackbackground", paramInt5).paintSliderTrackBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*      */     }
/*      */ 
/*      */     public void paintSliderTrackBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1840 */       getPainter(paramSynthContext, "slidertrackborder", -1).paintSliderTrackBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintSliderTrackBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */     {
/* 1846 */       getPainter(paramSynthContext, "slidertrackborder", paramInt5).paintSliderTrackBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*      */     }
/*      */ 
/*      */     public void paintSpinnerBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1852 */       getPainter(paramSynthContext, "spinnerbackground", -1).paintSpinnerBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintSpinnerBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1858 */       getPainter(paramSynthContext, "spinnerborder", -1).paintSpinnerBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintSplitPaneDividerBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1864 */       getPainter(paramSynthContext, "splitpanedividerbackground", -1).paintSplitPaneDividerBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintSplitPaneDividerBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */     {
/* 1870 */       getPainter(paramSynthContext, "splitpanedividerbackground", paramInt5).paintSplitPaneDividerBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*      */     }
/*      */ 
/*      */     public void paintSplitPaneDividerForeground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */     {
/* 1876 */       getPainter(paramSynthContext, "splitpanedividerforeground", paramInt5).paintSplitPaneDividerForeground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*      */     }
/*      */ 
/*      */     public void paintSplitPaneDragDivider(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */     {
/* 1883 */       getPainter(paramSynthContext, "splitpanedragdivider", paramInt5).paintSplitPaneDragDivider(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*      */     }
/*      */ 
/*      */     public void paintSplitPaneBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1889 */       getPainter(paramSynthContext, "splitpanebackground", -1).paintSplitPaneBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintSplitPaneBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1895 */       getPainter(paramSynthContext, "splitpaneborder", -1).paintSplitPaneBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintTabbedPaneBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1901 */       getPainter(paramSynthContext, "tabbedpanebackground", -1).paintTabbedPaneBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintTabbedPaneBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1907 */       getPainter(paramSynthContext, "tabbedpaneborder", -1).paintTabbedPaneBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintTabbedPaneTabAreaBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1913 */       getPainter(paramSynthContext, "tabbedpanetabareabackground", -1).paintTabbedPaneTabAreaBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintTabbedPaneTabAreaBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */     {
/* 1919 */       getPainter(paramSynthContext, "tabbedpanetabareabackground", paramInt5).paintTabbedPaneTabAreaBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*      */     }
/*      */ 
/*      */     public void paintTabbedPaneTabAreaBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1926 */       getPainter(paramSynthContext, "tabbedpanetabareaborder", -1).paintTabbedPaneTabAreaBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintTabbedPaneTabAreaBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */     {
/* 1932 */       getPainter(paramSynthContext, "tabbedpanetabareaborder", paramInt5).paintTabbedPaneTabAreaBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*      */     }
/*      */ 
/*      */     public void paintTabbedPaneTabBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */     {
/* 1939 */       getPainter(paramSynthContext, "tabbedpanetabbackground", -1).paintTabbedPaneTabBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*      */     }
/*      */ 
/*      */     public void paintTabbedPaneTabBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
/*      */     {
/* 1946 */       getPainter(paramSynthContext, "tabbedpanetabbackground", paramInt6).paintTabbedPaneTabBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6);
/*      */     }
/*      */ 
/*      */     public void paintTabbedPaneTabBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */     {
/* 1953 */       getPainter(paramSynthContext, "tabbedpanetabborder", -1).paintTabbedPaneTabBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*      */     }
/*      */ 
/*      */     public void paintTabbedPaneTabBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
/*      */     {
/* 1960 */       getPainter(paramSynthContext, "tabbedpanetabborder", paramInt6).paintTabbedPaneTabBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6);
/*      */     }
/*      */ 
/*      */     public void paintTabbedPaneContentBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1967 */       getPainter(paramSynthContext, "tabbedpanecontentbackground", -1).paintTabbedPaneContentBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintTabbedPaneContentBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1973 */       getPainter(paramSynthContext, "tabbedpanecontentborder", -1).paintTabbedPaneContentBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintTableHeaderBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1979 */       getPainter(paramSynthContext, "tableheaderbackground", -1).paintTableHeaderBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintTableHeaderBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1985 */       getPainter(paramSynthContext, "tableheaderborder", -1).paintTableHeaderBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintTableBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1991 */       getPainter(paramSynthContext, "tablebackground", -1).paintTableBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintTableBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1997 */       getPainter(paramSynthContext, "tableborder", -1).paintTableBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintTextAreaBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 2003 */       getPainter(paramSynthContext, "textareabackground", -1).paintTextAreaBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintTextAreaBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 2009 */       getPainter(paramSynthContext, "textareaborder", -1).paintTextAreaBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintTextPaneBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 2015 */       getPainter(paramSynthContext, "textpanebackground", -1).paintTextPaneBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintTextPaneBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 2021 */       getPainter(paramSynthContext, "textpaneborder", -1).paintTextPaneBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintTextFieldBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 2027 */       getPainter(paramSynthContext, "textfieldbackground", -1).paintTextFieldBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintTextFieldBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 2033 */       getPainter(paramSynthContext, "textfieldborder", -1).paintTextFieldBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintToggleButtonBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 2039 */       getPainter(paramSynthContext, "togglebuttonbackground", -1).paintToggleButtonBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintToggleButtonBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 2045 */       getPainter(paramSynthContext, "togglebuttonborder", -1).paintToggleButtonBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintToolBarBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 2051 */       getPainter(paramSynthContext, "toolbarbackground", -1).paintToolBarBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintToolBarBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */     {
/* 2057 */       getPainter(paramSynthContext, "toolbarbackground", paramInt5).paintToolBarBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*      */     }
/*      */ 
/*      */     public void paintToolBarBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 2063 */       getPainter(paramSynthContext, "toolbarborder", -1).paintToolBarBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintToolBarBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */     {
/* 2069 */       getPainter(paramSynthContext, "toolbarborder", paramInt5).paintToolBarBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*      */     }
/*      */ 
/*      */     public void paintToolBarContentBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 2075 */       getPainter(paramSynthContext, "toolbarcontentbackground", -1).paintToolBarContentBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintToolBarContentBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */     {
/* 2081 */       getPainter(paramSynthContext, "toolbarcontentbackground", paramInt5).paintToolBarContentBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*      */     }
/*      */ 
/*      */     public void paintToolBarContentBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 2087 */       getPainter(paramSynthContext, "toolbarcontentborder", -1).paintToolBarContentBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintToolBarContentBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */     {
/* 2093 */       getPainter(paramSynthContext, "toolbarcontentborder", paramInt5).paintToolBarContentBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*      */     }
/*      */ 
/*      */     public void paintToolBarDragWindowBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 2099 */       getPainter(paramSynthContext, "toolbardragwindowbackground", -1).paintToolBarDragWindowBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintToolBarDragWindowBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */     {
/* 2105 */       getPainter(paramSynthContext, "toolbardragwindowbackground", paramInt5).paintToolBarDragWindowBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*      */     }
/*      */ 
/*      */     public void paintToolBarDragWindowBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 2111 */       getPainter(paramSynthContext, "toolbardragwindowborder", -1).paintToolBarDragWindowBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintToolBarDragWindowBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */     {
/* 2117 */       getPainter(paramSynthContext, "toolbardragwindowborder", paramInt5).paintToolBarDragWindowBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*      */     }
/*      */ 
/*      */     public void paintToolTipBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 2123 */       getPainter(paramSynthContext, "tooltipbackground", -1).paintToolTipBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintToolTipBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 2129 */       getPainter(paramSynthContext, "tooltipborder", -1).paintToolTipBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintTreeBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 2135 */       getPainter(paramSynthContext, "treebackground", -1).paintTreeBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintTreeBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 2141 */       getPainter(paramSynthContext, "treeborder", -1).paintTreeBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintTreeCellBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 2147 */       getPainter(paramSynthContext, "treecellbackground", -1).paintTreeCellBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintTreeCellBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 2153 */       getPainter(paramSynthContext, "treecellborder", -1).paintTreeCellBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintTreeCellFocus(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 2159 */       getPainter(paramSynthContext, "treecellfocus", -1).paintTreeCellFocus(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintViewportBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 2165 */       getPainter(paramSynthContext, "viewportbackground", -1).paintViewportBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void paintViewportBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 2171 */       getPainter(paramSynthContext, "viewportborder", -1).paintViewportBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */   }
/*      */ 
/*      */   static class PainterInfo
/*      */   {
/*      */     private String _method;
/*      */     private SynthPainter _painter;
/*      */     private int _direction;
/*      */ 
/*      */     PainterInfo(String paramString, SynthPainter paramSynthPainter, int paramInt)
/*      */     {
/*  230 */       if (paramString != null) {
/*  231 */         this._method = paramString.intern();
/*      */       }
/*  233 */       this._painter = paramSynthPainter;
/*  234 */       this._direction = paramInt;
/*      */     }
/*      */ 
/*      */     void addPainter(SynthPainter paramSynthPainter) {
/*  238 */       if (!(this._painter instanceof ParsedSynthStyle.AggregatePainter)) {
/*  239 */         this._painter = new ParsedSynthStyle.AggregatePainter(this._painter);
/*      */       }
/*      */ 
/*  242 */       ((ParsedSynthStyle.AggregatePainter)this._painter).addPainter(paramSynthPainter);
/*      */     }
/*      */ 
/*      */     String getMethod() {
/*  246 */       return this._method;
/*      */     }
/*      */ 
/*      */     SynthPainter getPainter() {
/*  250 */       return this._painter;
/*      */     }
/*      */ 
/*      */     int getDirection() {
/*  254 */       return this._direction;
/*      */     }
/*      */ 
/*      */     boolean equalsPainter(PainterInfo paramPainterInfo) {
/*  258 */       return (this._method == paramPainterInfo._method) && (this._direction == paramPainterInfo._direction);
/*      */     }
/*      */ 
/*      */     public String toString() {
/*  262 */       return "PainterInfo {method=" + this._method + ",direction=" + this._direction + ",painter=" + this._painter + "}";
/*      */     }
/*      */   }
/*      */ 
/*      */   static class StateInfo extends DefaultSynthStyle.StateInfo
/*      */   {
/*      */     private ParsedSynthStyle.PainterInfo[] _painterInfo;
/*      */ 
/*      */     public StateInfo()
/*      */     {
/*      */     }
/*      */ 
/*      */     public StateInfo(DefaultSynthStyle.StateInfo paramStateInfo)
/*      */     {
/*  178 */       super();
/*  179 */       if ((paramStateInfo instanceof StateInfo))
/*  180 */         this._painterInfo = ((StateInfo)paramStateInfo)._painterInfo;
/*      */     }
/*      */ 
/*      */     public void setPainters(ParsedSynthStyle.PainterInfo[] paramArrayOfPainterInfo)
/*      */     {
/*  185 */       this._painterInfo = paramArrayOfPainterInfo;
/*      */     }
/*      */ 
/*      */     public ParsedSynthStyle.PainterInfo[] getPainters() {
/*  189 */       return this._painterInfo;
/*      */     }
/*      */ 
/*      */     public Object clone() {
/*  193 */       return new StateInfo(this);
/*      */     }
/*      */ 
/*      */     public DefaultSynthStyle.StateInfo addTo(DefaultSynthStyle.StateInfo paramStateInfo)
/*      */     {
/*  198 */       if (!(paramStateInfo instanceof StateInfo)) {
/*  199 */         paramStateInfo = new StateInfo(paramStateInfo);
/*      */       }
/*      */       else {
/*  202 */         paramStateInfo = super.addTo(paramStateInfo);
/*  203 */         StateInfo localStateInfo = (StateInfo)paramStateInfo;
/*  204 */         localStateInfo._painterInfo = ParsedSynthStyle.mergePainterInfo(localStateInfo._painterInfo, this._painterInfo);
/*      */       }
/*      */ 
/*  207 */       return paramStateInfo;
/*      */     }
/*      */ 
/*      */     public String toString() {
/*  211 */       StringBuffer localStringBuffer = new StringBuffer(super.toString());
/*  212 */       localStringBuffer.append(",painters=[");
/*  213 */       if (this._painterInfo != null) {
/*  214 */         for (int i = 0; i < this._painterInfo.length; i++) {
/*  215 */           localStringBuffer.append("    ").append(this._painterInfo[i].toString());
/*      */         }
/*      */       }
/*  218 */       localStringBuffer.append("]");
/*  219 */       return localStringBuffer.toString();
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.synth.ParsedSynthStyle
 * JD-Core Version:    0.6.2
 */