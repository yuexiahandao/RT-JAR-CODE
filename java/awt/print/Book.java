/*     */ package java.awt.print;
/*     */ 
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class Book
/*     */   implements Pageable
/*     */ {
/*     */   private Vector mPages;
/*     */ 
/*     */   public Book()
/*     */   {
/*  58 */     this.mPages = new Vector();
/*     */   }
/*     */ 
/*     */   public int getNumberOfPages()
/*     */   {
/*  66 */     return this.mPages.size();
/*     */   }
/*     */ 
/*     */   public PageFormat getPageFormat(int paramInt)
/*     */     throws IndexOutOfBoundsException
/*     */   {
/*  82 */     return getPage(paramInt).getPageFormat();
/*     */   }
/*     */ 
/*     */   public Printable getPrintable(int paramInt)
/*     */     throws IndexOutOfBoundsException
/*     */   {
/*  97 */     return getPage(paramInt).getPrintable();
/*     */   }
/*     */ 
/*     */   public void setPage(int paramInt, Printable paramPrintable, PageFormat paramPageFormat)
/*     */     throws IndexOutOfBoundsException
/*     */   {
/* 116 */     if (paramPrintable == null) {
/* 117 */       throw new NullPointerException("painter is null");
/*     */     }
/*     */ 
/* 120 */     if (paramPageFormat == null) {
/* 121 */       throw new NullPointerException("page is null");
/*     */     }
/*     */ 
/* 124 */     this.mPages.setElementAt(new BookPage(paramPrintable, paramPageFormat), paramInt);
/*     */   }
/*     */ 
/*     */   public void append(Printable paramPrintable, PageFormat paramPageFormat)
/*     */   {
/* 137 */     this.mPages.addElement(new BookPage(paramPrintable, paramPageFormat));
/*     */   }
/*     */ 
/*     */   public void append(Printable paramPrintable, PageFormat paramPageFormat, int paramInt)
/*     */   {
/* 154 */     BookPage localBookPage = new BookPage(paramPrintable, paramPageFormat);
/* 155 */     int i = this.mPages.size();
/* 156 */     int j = i + paramInt;
/*     */ 
/* 158 */     this.mPages.setSize(j);
/* 159 */     for (int k = i; k < j; k++)
/* 160 */       this.mPages.setElementAt(localBookPage, k);
/*     */   }
/*     */ 
/*     */   private BookPage getPage(int paramInt)
/*     */     throws ArrayIndexOutOfBoundsException
/*     */   {
/* 170 */     return (BookPage)this.mPages.elementAt(paramInt);
/*     */   }
/*     */ 
/*     */   private class BookPage
/*     */   {
/*     */     private PageFormat mFormat;
/*     */     private Printable mPainter;
/*     */ 
/*     */     BookPage(Printable paramPageFormat, PageFormat arg3)
/*     */     {
/*     */       Object localObject;
/* 198 */       if ((paramPageFormat == null) || (localObject == null)) {
/* 199 */         throw new NullPointerException();
/*     */       }
/*     */ 
/* 202 */       this.mFormat = localObject;
/* 203 */       this.mPainter = paramPageFormat;
/*     */     }
/*     */ 
/*     */     Printable getPrintable()
/*     */     {
/* 211 */       return this.mPainter;
/*     */     }
/*     */ 
/*     */     PageFormat getPageFormat()
/*     */     {
/* 218 */       return this.mFormat;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.print.Book
 * JD-Core Version:    0.6.2
 */