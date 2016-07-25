/*     */ package com.sun.beans.editors;
/*     */ 
/*     */ import java.awt.Choice;
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Event;
/*     */ import java.awt.Font;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Label;
/*     */ import java.awt.Panel;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Toolkit;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.beans.PropertyChangeSupport;
/*     */ import java.beans.PropertyEditor;
/*     */ import java.io.PrintStream;
/*     */ 
/*     */ public class FontEditor extends Panel
/*     */   implements PropertyEditor
/*     */ {
/*     */   private static final long serialVersionUID = 6732704486002715933L;
/*     */   private Font font;
/*     */   private Toolkit toolkit;
/* 205 */   private String sampleText = "Abcde...";
/*     */   private Label sample;
/*     */   private Choice familyChoser;
/*     */   private Choice styleChoser;
/*     */   private Choice sizeChoser;
/*     */   private String[] fonts;
/* 213 */   private String[] styleNames = { "plain", "bold", "italic" };
/* 214 */   private int[] styles = { 0, 1, 2 };
/* 215 */   private int[] pointSizes = { 3, 5, 8, 10, 12, 14, 18, 24, 36, 48 };
/*     */ 
/* 217 */   private PropertyChangeSupport support = new PropertyChangeSupport(this);
/*     */ 
/*     */   public FontEditor()
/*     */   {
/*  35 */     setLayout(null);
/*     */ 
/*  37 */     this.toolkit = Toolkit.getDefaultToolkit();
/*  38 */     this.fonts = this.toolkit.getFontList();
/*     */ 
/*  40 */     this.familyChoser = new Choice();
/*  41 */     for (int i = 0; i < this.fonts.length; i++) {
/*  42 */       this.familyChoser.addItem(this.fonts[i]);
/*     */     }
/*  44 */     add(this.familyChoser);
/*  45 */     this.familyChoser.reshape(20, 5, 100, 30);
/*     */ 
/*  47 */     this.styleChoser = new Choice();
/*  48 */     for (i = 0; i < this.styleNames.length; i++) {
/*  49 */       this.styleChoser.addItem(this.styleNames[i]);
/*     */     }
/*  51 */     add(this.styleChoser);
/*  52 */     this.styleChoser.reshape(145, 5, 70, 30);
/*     */ 
/*  54 */     this.sizeChoser = new Choice();
/*  55 */     for (i = 0; i < this.pointSizes.length; i++) {
/*  56 */       this.sizeChoser.addItem("" + this.pointSizes[i]);
/*     */     }
/*  58 */     add(this.sizeChoser);
/*  59 */     this.sizeChoser.reshape(220, 5, 70, 30);
/*     */ 
/*  61 */     resize(300, 40);
/*     */   }
/*     */ 
/*     */   public Dimension preferredSize()
/*     */   {
/*  66 */     return new Dimension(300, 40);
/*     */   }
/*     */ 
/*     */   public void setValue(Object paramObject) {
/*  70 */     this.font = ((Font)paramObject);
/*  71 */     if (this.font == null) {
/*  72 */       return;
/*     */     }
/*  74 */     changeFont(this.font);
/*     */ 
/*  76 */     for (int i = 0; i < this.fonts.length; i++) {
/*  77 */       if (this.fonts[i].equals(this.font.getFamily())) {
/*  78 */         this.familyChoser.select(i);
/*  79 */         break;
/*     */       }
/*     */     }
/*  82 */     for (i = 0; i < this.styleNames.length; i++) {
/*  83 */       if (this.font.getStyle() == this.styles[i]) {
/*  84 */         this.styleChoser.select(i);
/*  85 */         break;
/*     */       }
/*     */     }
/*  88 */     for (i = 0; i < this.pointSizes.length; i++)
/*  89 */       if (this.font.getSize() <= this.pointSizes[i]) {
/*  90 */         this.sizeChoser.select(i);
/*  91 */         break;
/*     */       }
/*     */   }
/*     */ 
/*     */   private void changeFont(Font paramFont)
/*     */   {
/*  97 */     this.font = paramFont;
/*  98 */     if (this.sample != null) {
/*  99 */       remove(this.sample);
/*     */     }
/* 101 */     this.sample = new Label(this.sampleText);
/* 102 */     this.sample.setFont(this.font);
/* 103 */     add(this.sample);
/* 104 */     Container localContainer = getParent();
/* 105 */     if (localContainer != null) {
/* 106 */       localContainer.invalidate();
/* 107 */       localContainer.layout();
/*     */     }
/* 109 */     invalidate();
/* 110 */     layout();
/* 111 */     repaint();
/* 112 */     this.support.firePropertyChange("", null, null);
/*     */   }
/*     */ 
/*     */   public Object getValue() {
/* 116 */     return this.font;
/*     */   }
/*     */ 
/*     */   public String getJavaInitializationString() {
/* 120 */     if (this.font == null) {
/* 121 */       return "null";
/*     */     }
/* 123 */     return "new java.awt.Font(\"" + this.font.getName() + "\", " + this.font.getStyle() + ", " + this.font.getSize() + ")";
/*     */   }
/*     */ 
/*     */   public boolean action(Event paramEvent, Object paramObject)
/*     */   {
/* 128 */     String str = this.familyChoser.getSelectedItem();
/* 129 */     int i = this.styles[this.styleChoser.getSelectedIndex()];
/* 130 */     int j = this.pointSizes[this.sizeChoser.getSelectedIndex()];
/*     */     try {
/* 132 */       Font localFont = new Font(str, i, j);
/* 133 */       changeFont(localFont);
/*     */     } catch (Exception localException) {
/* 135 */       System.err.println("Couldn't create font " + str + "-" + this.styleNames[i] + "-" + j);
/*     */     }
/*     */ 
/* 138 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isPaintable()
/*     */   {
/* 143 */     return true;
/*     */   }
/*     */ 
/*     */   public void paintValue(Graphics paramGraphics, Rectangle paramRectangle)
/*     */   {
/* 148 */     Font localFont = paramGraphics.getFont();
/* 149 */     paramGraphics.setFont(this.font);
/* 150 */     FontMetrics localFontMetrics = paramGraphics.getFontMetrics();
/* 151 */     int i = (paramRectangle.height - localFontMetrics.getAscent()) / 2;
/* 152 */     paramGraphics.drawString(this.sampleText, 0, paramRectangle.height - i);
/* 153 */     paramGraphics.setFont(localFont);
/*     */   }
/*     */ 
/*     */   public String getAsText() {
/* 157 */     if (this.font == null) {
/* 158 */       return null;
/*     */     }
/* 160 */     StringBuilder localStringBuilder = new StringBuilder();
/* 161 */     localStringBuilder.append(this.font.getName());
/* 162 */     localStringBuilder.append(' ');
/*     */ 
/* 164 */     boolean bool1 = this.font.isBold();
/* 165 */     if (bool1) {
/* 166 */       localStringBuilder.append("BOLD");
/*     */     }
/* 168 */     boolean bool2 = this.font.isItalic();
/* 169 */     if (bool2) {
/* 170 */       localStringBuilder.append("ITALIC");
/*     */     }
/* 172 */     if ((bool1) || (bool2)) {
/* 173 */       localStringBuilder.append(' ');
/*     */     }
/* 175 */     localStringBuilder.append(this.font.getSize());
/* 176 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   public void setAsText(String paramString) throws IllegalArgumentException {
/* 180 */     setValue(paramString == null ? null : Font.decode(paramString));
/*     */   }
/*     */ 
/*     */   public String[] getTags() {
/* 184 */     return null;
/*     */   }
/*     */ 
/*     */   public Component getCustomEditor() {
/* 188 */     return this;
/*     */   }
/*     */ 
/*     */   public boolean supportsCustomEditor() {
/* 192 */     return true;
/*     */   }
/*     */ 
/*     */   public void addPropertyChangeListener(PropertyChangeListener paramPropertyChangeListener) {
/* 196 */     this.support.addPropertyChangeListener(paramPropertyChangeListener);
/*     */   }
/*     */ 
/*     */   public void removePropertyChangeListener(PropertyChangeListener paramPropertyChangeListener) {
/* 200 */     this.support.removePropertyChangeListener(paramPropertyChangeListener);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.beans.editors.FontEditor
 * JD-Core Version:    0.6.2
 */