/*     */ package javax.swing.border;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Insets;
/*     */ import javax.swing.Icon;
/*     */ 
/*     */ public class MatteBorder extends EmptyBorder
/*     */ {
/*     */   protected Color color;
/*     */   protected Icon tileIcon;
/*     */ 
/*     */   public MatteBorder(int paramInt1, int paramInt2, int paramInt3, int paramInt4, Color paramColor)
/*     */   {
/*  63 */     super(paramInt1, paramInt2, paramInt3, paramInt4);
/*  64 */     this.color = paramColor;
/*     */   }
/*     */ 
/*     */   public MatteBorder(Insets paramInsets, Color paramColor)
/*     */   {
/*  74 */     super(paramInsets);
/*  75 */     this.color = paramColor;
/*     */   }
/*     */ 
/*     */   public MatteBorder(int paramInt1, int paramInt2, int paramInt3, int paramInt4, Icon paramIcon)
/*     */   {
/*  87 */     super(paramInt1, paramInt2, paramInt3, paramInt4);
/*  88 */     this.tileIcon = paramIcon;
/*     */   }
/*     */ 
/*     */   public MatteBorder(Insets paramInsets, Icon paramIcon)
/*     */   {
/*  98 */     super(paramInsets);
/*  99 */     this.tileIcon = paramIcon;
/*     */   }
/*     */ 
/*     */   public MatteBorder(Icon paramIcon)
/*     */   {
/* 111 */     this(-1, -1, -1, -1, paramIcon);
/*     */   }
/*     */ 
/*     */   public void paintBorder(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 118 */     Insets localInsets = getBorderInsets(paramComponent);
/* 119 */     Color localColor = paramGraphics.getColor();
/* 120 */     paramGraphics.translate(paramInt1, paramInt2);
/*     */ 
/* 123 */     if (this.tileIcon != null) {
/* 124 */       this.color = (this.tileIcon.getIconWidth() == -1 ? Color.gray : null);
/*     */     }
/*     */ 
/* 127 */     if (this.color != null) {
/* 128 */       paramGraphics.setColor(this.color);
/* 129 */       paramGraphics.fillRect(0, 0, paramInt3 - localInsets.right, localInsets.top);
/* 130 */       paramGraphics.fillRect(0, localInsets.top, localInsets.left, paramInt4 - localInsets.top);
/* 131 */       paramGraphics.fillRect(localInsets.left, paramInt4 - localInsets.bottom, paramInt3 - localInsets.left, localInsets.bottom);
/* 132 */       paramGraphics.fillRect(paramInt3 - localInsets.right, 0, localInsets.right, paramInt4 - localInsets.bottom);
/*     */     }
/* 134 */     else if (this.tileIcon != null) {
/* 135 */       int i = this.tileIcon.getIconWidth();
/* 136 */       int j = this.tileIcon.getIconHeight();
/* 137 */       paintEdge(paramComponent, paramGraphics, 0, 0, paramInt3 - localInsets.right, localInsets.top, i, j);
/* 138 */       paintEdge(paramComponent, paramGraphics, 0, localInsets.top, localInsets.left, paramInt4 - localInsets.top, i, j);
/* 139 */       paintEdge(paramComponent, paramGraphics, localInsets.left, paramInt4 - localInsets.bottom, paramInt3 - localInsets.left, localInsets.bottom, i, j);
/* 140 */       paintEdge(paramComponent, paramGraphics, paramInt3 - localInsets.right, 0, localInsets.right, paramInt4 - localInsets.bottom, i, j);
/*     */     }
/* 142 */     paramGraphics.translate(-paramInt1, -paramInt2);
/* 143 */     paramGraphics.setColor(localColor);
/*     */   }
/*     */ 
/*     */   private void paintEdge(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
/*     */   {
/* 148 */     paramGraphics = paramGraphics.create(paramInt1, paramInt2, paramInt3, paramInt4);
/* 149 */     int i = -(paramInt2 % paramInt6);
/* 150 */     for (paramInt1 = -(paramInt1 % paramInt5); paramInt1 < paramInt3; paramInt1 += paramInt5) {
/* 151 */       for (paramInt2 = i; paramInt2 < paramInt4; paramInt2 += paramInt6) {
/* 152 */         this.tileIcon.paintIcon(paramComponent, paramGraphics, paramInt1, paramInt2);
/*     */       }
/*     */     }
/* 155 */     paramGraphics.dispose();
/*     */   }
/*     */ 
/*     */   public Insets getBorderInsets(Component paramComponent, Insets paramInsets)
/*     */   {
/* 165 */     return computeInsets(paramInsets);
/*     */   }
/*     */ 
/*     */   public Insets getBorderInsets()
/*     */   {
/* 173 */     return computeInsets(new Insets(0, 0, 0, 0));
/*     */   }
/*     */ 
/*     */   private Insets computeInsets(Insets paramInsets)
/*     */   {
/* 178 */     if ((this.tileIcon != null) && (this.top == -1) && (this.bottom == -1) && (this.left == -1) && (this.right == -1))
/*     */     {
/* 180 */       int i = this.tileIcon.getIconWidth();
/* 181 */       int j = this.tileIcon.getIconHeight();
/* 182 */       paramInsets.top = j;
/* 183 */       paramInsets.right = i;
/* 184 */       paramInsets.bottom = j;
/* 185 */       paramInsets.left = i;
/*     */     } else {
/* 187 */       paramInsets.left = this.left;
/* 188 */       paramInsets.top = this.top;
/* 189 */       paramInsets.right = this.right;
/* 190 */       paramInsets.bottom = this.bottom;
/*     */     }
/* 192 */     return paramInsets;
/*     */   }
/*     */ 
/*     */   public Color getMatteColor()
/*     */   {
/* 201 */     return this.color;
/*     */   }
/*     */ 
/*     */   public Icon getTileIcon()
/*     */   {
/* 210 */     return this.tileIcon;
/*     */   }
/*     */ 
/*     */   public boolean isBorderOpaque()
/*     */   {
/* 218 */     return this.color != null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.border.MatteBorder
 * JD-Core Version:    0.6.2
 */