/*      */ package com.sun.xml.internal.messaging.saaj.packaging.mime.internet;
/*      */ 
/*      */ import java.io.EOFException;
/*      */ import java.io.IOException;
/*      */ import java.io.OutputStream;
/*      */ 
/*      */ class AsciiOutputStream extends OutputStream
/*      */ {
/*      */   private boolean breakOnNonAscii;
/* 1434 */   private int ascii = 0; private int non_ascii = 0;
/* 1435 */   private int linelen = 0;
/* 1436 */   private boolean longLine = false;
/* 1437 */   private boolean badEOL = false;
/* 1438 */   private boolean checkEOL = false;
/* 1439 */   private int lastb = 0;
/* 1440 */   private int ret = 0;
/*      */ 
/*      */   public AsciiOutputStream(boolean breakOnNonAscii, boolean encodeEolStrict) {
/* 1443 */     this.breakOnNonAscii = breakOnNonAscii;
/* 1444 */     this.checkEOL = ((encodeEolStrict) && (breakOnNonAscii));
/*      */   }
/*      */ 
/*      */   public void write(int b) throws IOException {
/* 1448 */     check(b);
/*      */   }
/*      */ 
/*      */   public void write(byte[] b) throws IOException {
/* 1452 */     write(b, 0, b.length);
/*      */   }
/*      */ 
/*      */   public void write(byte[] b, int off, int len) throws IOException {
/* 1456 */     len += off;
/* 1457 */     for (int i = off; i < len; i++)
/* 1458 */       check(b[i]);
/*      */   }
/*      */ 
/*      */   private final void check(int b) throws IOException {
/* 1462 */     b &= 255;
/* 1463 */     if ((this.checkEOL) && (((this.lastb == 13) && (b != 10)) || ((this.lastb != 13) && (b == 10))))
/*      */     {
/* 1465 */       this.badEOL = true;
/* 1466 */     }if ((b == 13) || (b == 10)) {
/* 1467 */       this.linelen = 0;
/*      */     } else {
/* 1469 */       this.linelen += 1;
/* 1470 */       if (this.linelen > 998)
/* 1471 */         this.longLine = true;
/*      */     }
/* 1473 */     if (MimeUtility.nonascii(b)) {
/* 1474 */       this.non_ascii += 1;
/* 1475 */       if (this.breakOnNonAscii) {
/* 1476 */         this.ret = 3;
/* 1477 */         throw new EOFException();
/*      */       }
/*      */     } else {
/* 1480 */       this.ascii += 1;
/* 1481 */     }this.lastb = b;
/*      */   }
/*      */ 
/*      */   public int getAscii()
/*      */   {
/* 1488 */     if (this.ret != 0) {
/* 1489 */       return this.ret;
/*      */     }
/*      */ 
/* 1494 */     if (this.badEOL)
/* 1495 */       return 3;
/* 1496 */     if (this.non_ascii == 0)
/*      */     {
/* 1498 */       if (this.longLine) {
/* 1499 */         return 2;
/*      */       }
/* 1501 */       return 1;
/*      */     }
/* 1503 */     if (this.ascii > this.non_ascii)
/* 1504 */       return 2;
/* 1505 */     return 3;
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.messaging.saaj.packaging.mime.internet.AsciiOutputStream
 * JD-Core Version:    0.6.2
 */