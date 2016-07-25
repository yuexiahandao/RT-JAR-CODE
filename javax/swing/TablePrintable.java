/*     */ package javax.swing;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.ComponentOrientation;
/*     */ import java.awt.Font;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Shape;
/*     */ import java.awt.geom.AffineTransform;
/*     */ import java.awt.geom.Rectangle2D;
/*     */ import java.awt.print.PageFormat;
/*     */ import java.awt.print.Printable;
/*     */ import java.awt.print.PrinterException;
/*     */ import java.text.MessageFormat;
/*     */ import javax.swing.table.JTableHeader;
/*     */ import javax.swing.table.TableColumn;
/*     */ import javax.swing.table.TableColumnModel;
/*     */ 
/*     */ class TablePrintable
/*     */   implements Printable
/*     */ {
/*     */   private JTable table;
/*     */   private JTableHeader header;
/*     */   private TableColumnModel colModel;
/*     */   private int totalColWidth;
/*     */   private JTable.PrintMode printMode;
/*     */   private MessageFormat headerFormat;
/*     */   private MessageFormat footerFormat;
/* 121 */   private int last = -1;
/*     */ 
/* 124 */   private int row = 0;
/*     */ 
/* 127 */   private int col = 0;
/*     */ 
/* 130 */   private final Rectangle clip = new Rectangle(0, 0, 0, 0);
/*     */ 
/* 133 */   private final Rectangle hclip = new Rectangle(0, 0, 0, 0);
/*     */ 
/* 136 */   private final Rectangle tempRect = new Rectangle(0, 0, 0, 0);
/*     */   private static final int H_F_SPACE = 8;
/*     */   private static final float HEADER_FONT_SIZE = 18.0F;
/*     */   private static final float FOOTER_FONT_SIZE = 12.0F;
/*     */   private Font headerFont;
/*     */   private Font footerFont;
/*     */ 
/*     */   public TablePrintable(JTable paramJTable, JTable.PrintMode paramPrintMode, MessageFormat paramMessageFormat1, MessageFormat paramMessageFormat2)
/*     */   {
/* 172 */     this.table = paramJTable;
/*     */ 
/* 174 */     this.header = paramJTable.getTableHeader();
/* 175 */     this.colModel = paramJTable.getColumnModel();
/* 176 */     this.totalColWidth = this.colModel.getTotalColumnWidth();
/*     */ 
/* 178 */     if (this.header != null)
/*     */     {
/* 180 */       this.hclip.height = this.header.getHeight();
/*     */     }
/*     */ 
/* 183 */     this.printMode = paramPrintMode;
/*     */ 
/* 185 */     this.headerFormat = paramMessageFormat1;
/* 186 */     this.footerFormat = paramMessageFormat2;
/*     */ 
/* 189 */     this.headerFont = paramJTable.getFont().deriveFont(1, 18.0F);
/*     */ 
/* 191 */     this.footerFont = paramJTable.getFont().deriveFont(0, 12.0F);
/*     */   }
/*     */ 
/*     */   public int print(Graphics paramGraphics, PageFormat paramPageFormat, int paramInt)
/*     */     throws PrinterException
/*     */   {
/* 210 */     int i = (int)paramPageFormat.getImageableWidth();
/* 211 */     int j = (int)paramPageFormat.getImageableHeight();
/*     */ 
/* 213 */     if (i <= 0) {
/* 214 */       throw new PrinterException("Width of printable area is too small.");
/*     */     }
/*     */ 
/* 218 */     Object[] arrayOfObject = { Integer.valueOf(paramInt + 1) };
/*     */ 
/* 221 */     String str1 = null;
/* 222 */     if (this.headerFormat != null) {
/* 223 */       str1 = this.headerFormat.format(arrayOfObject);
/*     */     }
/*     */ 
/* 227 */     String str2 = null;
/* 228 */     if (this.footerFormat != null) {
/* 229 */       str2 = this.footerFormat.format(arrayOfObject);
/*     */     }
/*     */ 
/* 233 */     Rectangle2D localRectangle2D1 = null;
/* 234 */     Rectangle2D localRectangle2D2 = null;
/*     */ 
/* 237 */     int k = 0;
/* 238 */     int m = 0;
/*     */ 
/* 241 */     int n = j;
/*     */ 
/* 245 */     if (str1 != null) {
/* 246 */       paramGraphics.setFont(this.headerFont);
/* 247 */       localRectangle2D1 = paramGraphics.getFontMetrics().getStringBounds(str1, paramGraphics);
/*     */ 
/* 250 */       k = (int)Math.ceil(localRectangle2D1.getHeight());
/* 251 */       n -= k + 8;
/*     */     }
/*     */ 
/* 256 */     if (str2 != null) {
/* 257 */       paramGraphics.setFont(this.footerFont);
/* 258 */       localRectangle2D2 = paramGraphics.getFontMetrics().getStringBounds(str2, paramGraphics);
/*     */ 
/* 261 */       m = (int)Math.ceil(localRectangle2D2.getHeight());
/* 262 */       n -= m + 8;
/*     */     }
/*     */ 
/* 265 */     if (n <= 0) {
/* 266 */       throw new PrinterException("Height of printable area is too small.");
/*     */     }
/*     */ 
/* 271 */     double d = 1.0D;
/* 272 */     if ((this.printMode == JTable.PrintMode.FIT_WIDTH) && (this.totalColWidth > i))
/*     */     {
/* 276 */       assert (i > 0);
/*     */ 
/* 279 */       assert (this.totalColWidth > 1);
/*     */ 
/* 281 */       d = i / this.totalColWidth;
/*     */     }
/*     */ 
/* 285 */     assert (d > 0.0D);
/*     */ 
/* 294 */     while (this.last < paramInt)
/*     */     {
/* 296 */       if ((this.row >= this.table.getRowCount()) && (this.col == 0)) {
/* 297 */         return 1;
/*     */       }
/*     */ 
/* 303 */       int i1 = (int)(i / d);
/* 304 */       int i2 = (int)((n - this.hclip.height) / d);
/*     */ 
/* 307 */       findNextClip(i1, i2);
/*     */ 
/* 309 */       this.last += 1;
/*     */     }
/*     */ 
/* 313 */     Graphics2D localGraphics2D = (Graphics2D)paramGraphics.create();
/*     */ 
/* 316 */     localGraphics2D.translate(paramPageFormat.getImageableX(), paramPageFormat.getImageableY());
/*     */ 
/* 322 */     if (str2 != null) {
/* 323 */       localAffineTransform = localGraphics2D.getTransform();
/*     */ 
/* 325 */       localGraphics2D.translate(0, j - m);
/*     */ 
/* 327 */       printText(localGraphics2D, str2, localRectangle2D2, this.footerFont, i);
/*     */ 
/* 329 */       localGraphics2D.setTransform(localAffineTransform);
/*     */     }
/*     */ 
/* 334 */     if (str1 != null) {
/* 335 */       printText(localGraphics2D, str1, localRectangle2D1, this.headerFont, i);
/*     */ 
/* 337 */       localGraphics2D.translate(0, k + 8);
/*     */     }
/*     */ 
/* 341 */     this.tempRect.x = 0;
/* 342 */     this.tempRect.y = 0;
/* 343 */     this.tempRect.width = i;
/* 344 */     this.tempRect.height = n;
/* 345 */     localGraphics2D.clip(this.tempRect);
/*     */ 
/* 349 */     if (d != 1.0D) {
/* 350 */       localGraphics2D.scale(d, d);
/*     */     }
/*     */     else
/*     */     {
/* 355 */       int i3 = (i - this.clip.width) / 2;
/* 356 */       localGraphics2D.translate(i3, 0);
/*     */     }
/*     */ 
/* 360 */     AffineTransform localAffineTransform = localGraphics2D.getTransform();
/* 361 */     Shape localShape = localGraphics2D.getClip();
/*     */ 
/* 365 */     if (this.header != null) {
/* 366 */       this.hclip.x = this.clip.x;
/* 367 */       this.hclip.width = this.clip.width;
/*     */ 
/* 369 */       localGraphics2D.translate(-this.hclip.x, 0);
/* 370 */       localGraphics2D.clip(this.hclip);
/* 371 */       this.header.print(localGraphics2D);
/*     */ 
/* 374 */       localGraphics2D.setTransform(localAffineTransform);
/* 375 */       localGraphics2D.setClip(localShape);
/*     */ 
/* 378 */       localGraphics2D.translate(0, this.hclip.height);
/*     */     }
/*     */ 
/* 382 */     localGraphics2D.translate(-this.clip.x, -this.clip.y);
/* 383 */     localGraphics2D.clip(this.clip);
/* 384 */     this.table.print(localGraphics2D);
/*     */ 
/* 387 */     localGraphics2D.setTransform(localAffineTransform);
/* 388 */     localGraphics2D.setClip(localShape);
/*     */ 
/* 391 */     localGraphics2D.setColor(Color.BLACK);
/* 392 */     localGraphics2D.drawRect(0, 0, this.clip.width, this.hclip.height + this.clip.height);
/*     */ 
/* 395 */     localGraphics2D.dispose();
/*     */ 
/* 397 */     return 0;
/*     */   }
/*     */ 
/*     */   private void printText(Graphics2D paramGraphics2D, String paramString, Rectangle2D paramRectangle2D, Font paramFont, int paramInt)
/*     */   {
/*     */     int i;
/* 420 */     if (paramRectangle2D.getWidth() < paramInt) {
/* 421 */       i = (int)((paramInt - paramRectangle2D.getWidth()) / 2.0D);
/*     */     }
/* 425 */     else if (this.table.getComponentOrientation().isLeftToRight()) {
/* 426 */       i = 0;
/*     */     }
/*     */     else
/*     */     {
/* 430 */       i = -(int)(Math.ceil(paramRectangle2D.getWidth()) - paramInt);
/*     */     }
/*     */ 
/* 433 */     int j = (int)Math.ceil(Math.abs(paramRectangle2D.getY()));
/* 434 */     paramGraphics2D.setColor(Color.BLACK);
/* 435 */     paramGraphics2D.setFont(paramFont);
/* 436 */     paramGraphics2D.drawString(paramString, i, j);
/*     */   }
/*     */ 
/*     */   private void findNextClip(int paramInt1, int paramInt2)
/*     */   {
/* 451 */     boolean bool = this.table.getComponentOrientation().isLeftToRight();
/*     */ 
/* 454 */     if (this.col == 0) {
/* 455 */       if (bool)
/*     */       {
/* 457 */         this.clip.x = 0;
/*     */       }
/*     */       else {
/* 460 */         this.clip.x = this.totalColWidth;
/*     */       }
/*     */ 
/* 464 */       this.clip.y += this.clip.height;
/*     */ 
/* 467 */       this.clip.width = 0;
/* 468 */       this.clip.height = 0;
/*     */ 
/* 471 */       i = this.table.getRowCount();
/* 472 */       j = this.table.getRowHeight(this.row);
/*     */       do {
/* 474 */         this.clip.height += j;
/*     */ 
/* 476 */         if (++this.row >= i)
/*     */         {
/*     */           break;
/*     */         }
/* 480 */         j = this.table.getRowHeight(this.row);
/* 481 */       }while (this.clip.height + j <= paramInt2);
/*     */     }
/*     */ 
/* 486 */     if (this.printMode == JTable.PrintMode.FIT_WIDTH) {
/* 487 */       this.clip.x = 0;
/* 488 */       this.clip.width = this.totalColWidth;
/* 489 */       return;
/*     */     }
/*     */ 
/* 492 */     if (bool)
/*     */     {
/* 494 */       this.clip.x += this.clip.width;
/*     */     }
/*     */ 
/* 498 */     this.clip.width = 0;
/*     */ 
/* 501 */     int i = this.table.getColumnCount();
/* 502 */     int j = this.colModel.getColumn(this.col).getWidth();
/*     */     do {
/* 504 */       this.clip.width += j;
/* 505 */       if (!bool) {
/* 506 */         this.clip.x -= j;
/*     */       }
/*     */ 
/* 509 */       if (++this.col >= i)
/*     */       {
/* 511 */         this.col = 0;
/*     */ 
/* 513 */         break;
/*     */       }
/*     */ 
/* 516 */       j = this.colModel.getColumn(this.col).getWidth();
/* 517 */     }while (this.clip.width + j <= paramInt1);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.TablePrintable
 * JD-Core Version:    0.6.2
 */