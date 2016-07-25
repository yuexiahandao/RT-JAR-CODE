/*     */ package com.sun.beans.editors;
/*     */ 
/*     */ import java.awt.Canvas;
/*     */ import java.awt.Choice;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Event;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Panel;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.TextField;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.beans.PropertyChangeSupport;
/*     */ import java.beans.PropertyEditor;
/*     */ 
/*     */ public class ColorEditor extends Panel
/*     */   implements PropertyEditor
/*     */ {
/*     */   private static final long serialVersionUID = 1781257185164716054L;
/* 194 */   private String[] colorNames = { " ", "white", "lightGray", "gray", "darkGray", "black", "red", "pink", "orange", "yellow", "green", "magenta", "cyan", "blue" };
/*     */ 
/* 198 */   private Color[] colors = { null, Color.white, Color.lightGray, Color.gray, Color.darkGray, Color.black, Color.red, Color.pink, Color.orange, Color.yellow, Color.green, Color.magenta, Color.cyan, Color.blue };
/*     */   private Canvas sample;
/* 204 */   private int sampleHeight = 20;
/* 205 */   private int sampleWidth = 40;
/* 206 */   private int hPad = 5;
/*     */   private int ourWidth;
/*     */   private Color color;
/*     */   private TextField text;
/*     */   private Choice choser;
/* 213 */   private PropertyChangeSupport support = new PropertyChangeSupport(this);
/*     */ 
/*     */   public ColorEditor()
/*     */   {
/*  35 */     setLayout(null);
/*     */ 
/*  37 */     this.ourWidth = this.hPad;
/*     */ 
/*  40 */     Panel localPanel = new Panel();
/*  41 */     localPanel.setLayout(null);
/*  42 */     localPanel.setBackground(Color.black);
/*  43 */     this.sample = new Canvas();
/*  44 */     localPanel.add(this.sample);
/*  45 */     this.sample.reshape(2, 2, this.sampleWidth, this.sampleHeight);
/*  46 */     add(localPanel);
/*  47 */     localPanel.reshape(this.ourWidth, 2, this.sampleWidth + 4, this.sampleHeight + 4);
/*  48 */     this.ourWidth += this.sampleWidth + 4 + this.hPad;
/*     */ 
/*  50 */     this.text = new TextField("", 14);
/*  51 */     add(this.text);
/*  52 */     this.text.reshape(this.ourWidth, 0, 100, 30);
/*  53 */     this.ourWidth += 100 + this.hPad;
/*     */ 
/*  55 */     this.choser = new Choice();
/*  56 */     int i = 0;
/*  57 */     for (int j = 0; j < this.colorNames.length; j++) {
/*  58 */       this.choser.addItem(this.colorNames[j]);
/*     */     }
/*  60 */     add(this.choser);
/*  61 */     this.choser.reshape(this.ourWidth, 0, 100, 30);
/*  62 */     this.ourWidth += 100 + this.hPad;
/*     */ 
/*  64 */     resize(this.ourWidth, 40);
/*     */   }
/*     */ 
/*     */   public void setValue(Object paramObject) {
/*  68 */     Color localColor = (Color)paramObject;
/*  69 */     changeColor(localColor);
/*     */   }
/*     */ 
/*     */   public Dimension preferredSize() {
/*  73 */     return new Dimension(this.ourWidth, 40);
/*     */   }
/*     */ 
/*     */   public boolean keyUp(Event paramEvent, int paramInt) {
/*  77 */     if (paramEvent.target == this.text)
/*     */       try {
/*  79 */         setAsText(this.text.getText());
/*     */       }
/*     */       catch (IllegalArgumentException localIllegalArgumentException)
/*     */       {
/*     */       }
/*  84 */     return false;
/*     */   }
/*     */ 
/*     */   public void setAsText(String paramString) throws IllegalArgumentException {
/*  88 */     if (paramString == null) {
/*  89 */       changeColor(null);
/*  90 */       return;
/*     */     }
/*  92 */     int i = paramString.indexOf(',');
/*  93 */     int j = paramString.indexOf(',', i + 1);
/*  94 */     if ((i < 0) || (j < 0))
/*     */     {
/*  96 */       throw new IllegalArgumentException(paramString);
/*     */     }
/*     */     try {
/*  99 */       int k = Integer.parseInt(paramString.substring(0, i));
/* 100 */       int m = Integer.parseInt(paramString.substring(i + 1, j));
/* 101 */       int n = Integer.parseInt(paramString.substring(j + 1));
/* 102 */       Color localColor = new Color(k, m, n);
/* 103 */       changeColor(localColor);
/*     */     } catch (Exception localException) {
/* 105 */       throw new IllegalArgumentException(paramString);
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean action(Event paramEvent, Object paramObject)
/*     */   {
/* 111 */     if (paramEvent.target == this.choser) {
/* 112 */       changeColor(this.colors[this.choser.getSelectedIndex()]);
/*     */     }
/* 114 */     return false;
/*     */   }
/*     */ 
/*     */   public String getJavaInitializationString() {
/* 118 */     return this.color != null ? "new java.awt.Color(" + this.color.getRGB() + ",true)" : "null";
/*     */   }
/*     */ 
/*     */   private void changeColor(Color paramColor)
/*     */   {
/* 126 */     if (paramColor == null) {
/* 127 */       this.color = null;
/* 128 */       this.text.setText("");
/* 129 */       return;
/*     */     }
/*     */ 
/* 132 */     this.color = paramColor;
/*     */ 
/* 134 */     this.text.setText("" + paramColor.getRed() + "," + paramColor.getGreen() + "," + paramColor.getBlue());
/*     */ 
/* 136 */     int i = 0;
/* 137 */     for (int j = 0; j < this.colorNames.length; j++) {
/* 138 */       if (this.color.equals(this.colors[j])) {
/* 139 */         i = j;
/*     */       }
/*     */     }
/* 142 */     this.choser.select(i);
/*     */ 
/* 144 */     this.sample.setBackground(this.color);
/* 145 */     this.sample.repaint();
/*     */ 
/* 147 */     this.support.firePropertyChange("", null, null);
/*     */   }
/*     */ 
/*     */   public Object getValue() {
/* 151 */     return this.color;
/*     */   }
/*     */ 
/*     */   public boolean isPaintable() {
/* 155 */     return true;
/*     */   }
/*     */ 
/*     */   public void paintValue(Graphics paramGraphics, Rectangle paramRectangle) {
/* 159 */     Color localColor = paramGraphics.getColor();
/* 160 */     paramGraphics.setColor(Color.black);
/* 161 */     paramGraphics.drawRect(paramRectangle.x, paramRectangle.y, paramRectangle.width - 3, paramRectangle.height - 3);
/* 162 */     paramGraphics.setColor(this.color);
/* 163 */     paramGraphics.fillRect(paramRectangle.x + 1, paramRectangle.y + 1, paramRectangle.width - 4, paramRectangle.height - 4);
/* 164 */     paramGraphics.setColor(localColor);
/*     */   }
/*     */ 
/*     */   public String getAsText() {
/* 168 */     return this.color != null ? this.color.getRed() + "," + this.color.getGreen() + "," + this.color.getBlue() : null;
/*     */   }
/*     */ 
/*     */   public String[] getTags()
/*     */   {
/* 174 */     return null;
/*     */   }
/*     */ 
/*     */   public Component getCustomEditor() {
/* 178 */     return this;
/*     */   }
/*     */ 
/*     */   public boolean supportsCustomEditor() {
/* 182 */     return true;
/*     */   }
/*     */ 
/*     */   public void addPropertyChangeListener(PropertyChangeListener paramPropertyChangeListener) {
/* 186 */     this.support.addPropertyChangeListener(paramPropertyChangeListener);
/*     */   }
/*     */ 
/*     */   public void removePropertyChangeListener(PropertyChangeListener paramPropertyChangeListener) {
/* 190 */     this.support.removePropertyChangeListener(paramPropertyChangeListener);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.beans.editors.ColorEditor
 * JD-Core Version:    0.6.2
 */