/*     */ package sun.jdbc.odbc;
/*     */ 
/*     */ public class JdbcOdbcPseudoCol extends JdbcOdbcObject
/*     */ {
/*     */   protected String colLabel;
/*     */   protected int colType;
/*     */   protected int colLength;
/*     */   protected int colDisplaySize;
/*     */ 
/*     */   public JdbcOdbcPseudoCol(String paramString, int paramInt1, int paramInt2)
/*     */   {
/*  49 */     this.colLabel = paramString;
/*  50 */     this.colType = paramInt1;
/*  51 */     this.colLength = paramInt2;
/*     */ 
/*  55 */     this.colDisplaySize = this.colLength;
/*     */ 
/*  57 */     switch (this.colType)
/*     */     {
/*     */     case -4:
/*     */     case -3:
/*     */     case -2:
/*  64 */       this.colDisplaySize *= 2;
/*  65 */       break;
/*     */     case 91:
/*  67 */       this.colDisplaySize = 10;
/*  68 */       break;
/*     */     case 92:
/*  70 */       this.colDisplaySize = 8;
/*  71 */       break;
/*     */     case 93:
/*  73 */       this.colDisplaySize = 29;
/*  74 */       break;
/*     */     case 2:
/*     */     case 3:
/*  77 */       this.colDisplaySize += 2;
/*  78 */       break;
/*     */     case -7:
/*  80 */       this.colDisplaySize = 1;
/*  81 */       break;
/*     */     case -6:
/*  83 */       this.colDisplaySize = 4;
/*  84 */       break;
/*     */     case 5:
/*  86 */       this.colDisplaySize = 6;
/*  87 */       break;
/*     */     case 4:
/*  89 */       this.colDisplaySize = 11;
/*  90 */       break;
/*     */     case -5:
/*  92 */       this.colDisplaySize = 20;
/*  93 */       break;
/*     */     case 7:
/*  95 */       this.colDisplaySize = 13;
/*  96 */       break;
/*     */     case 6:
/*     */     case 8:
/*  99 */       this.colDisplaySize = 22;
/*     */     }
/*     */   }
/*     */ 
/*     */   public String getColumnLabel()
/*     */   {
/* 110 */     return this.colLabel;
/*     */   }
/*     */ 
/*     */   public int getColumnType()
/*     */   {
/* 119 */     return this.colType;
/*     */   }
/*     */ 
/*     */   public int getColumnLength()
/*     */   {
/* 128 */     return this.colLength;
/*     */   }
/*     */ 
/*     */   public int getColumnDisplaySize()
/*     */   {
/* 137 */     return this.colDisplaySize;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.jdbc.odbc.JdbcOdbcPseudoCol
 * JD-Core Version:    0.6.2
 */