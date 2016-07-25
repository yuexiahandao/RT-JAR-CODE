/*     */ package sun.awt.windows;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.util.Arrays;
/*     */ 
/*     */ class HTMLCodec extends InputStream
/*     */ {
/*     */   public static final String ENCODING = "UTF-8";
/*     */   public static final String VERSION = "Version:";
/*     */   public static final String START_HTML = "StartHTML:";
/*     */   public static final String END_HTML = "EndHTML:";
/*     */   public static final String START_FRAGMENT = "StartFragment:";
/*     */   public static final String END_FRAGMENT = "EndFragment:";
/*     */   public static final String START_SELECTION = "StartSelection:";
/*     */   public static final String END_SELECTION = "EndSelection:";
/*     */   public static final String START_FRAGMENT_CMT = "<!--StartFragment-->";
/*     */   public static final String END_FRAGMENT_CMT = "<!--EndFragment-->";
/*     */   public static final String SOURCE_URL = "SourceURL:";
/*     */   public static final String DEF_SOURCE_URL = "about:blank";
/*     */   public static final String EOLN = "\r\n";
/*     */   private static final String VERSION_NUM = "1.0";
/*     */   private static final int PADDED_WIDTH = 10;
/*     */   private final BufferedInputStream bufferedStream;
/* 654 */   private boolean descriptionParsed = false;
/* 655 */   private boolean closed = false;
/*     */   public static final int BYTE_BUFFER_LEN = 8192;
/*     */   public static final int CHAR_BUFFER_LEN = 2730;
/*     */   private static final String FAILURE_MSG = "Unable to parse HTML description: ";
/*     */   private static final String INVALID_MSG = " invalid";
/*     */   private long iHTMLStart;
/*     */   private long iHTMLEnd;
/*     */   private long iFragStart;
/*     */   private long iFragEnd;
/*     */   private long iSelStart;
/*     */   private long iSelEnd;
/*     */   private String stBaseURL;
/*     */   private String stVersion;
/*     */   private long iStartOffset;
/*     */   private long iEndOffset;
/*     */   private long iReadCount;
/*     */   private EHTMLReadMode readMode;
/*     */ 
/*     */   private static String toPaddedString(int paramInt1, int paramInt2)
/*     */   {
/* 522 */     String str = "" + paramInt1;
/* 523 */     int i = str.length();
/* 524 */     if ((paramInt1 >= 0) && (i < paramInt2)) {
/* 525 */       char[] arrayOfChar = new char[paramInt2 - i];
/* 526 */       Arrays.fill(arrayOfChar, '0');
/* 527 */       StringBuffer localStringBuffer = new StringBuffer(paramInt2);
/* 528 */       localStringBuffer.append(arrayOfChar);
/* 529 */       localStringBuffer.append(str);
/* 530 */       str = localStringBuffer.toString();
/*     */     }
/* 532 */     return str;
/*     */   }
/*     */ 
/*     */   public static byte[] convertToHTMLFormat(byte[] paramArrayOfByte)
/*     */   {
/* 564 */     String str1 = "";
/* 565 */     String str2 = "";
/*     */ 
/* 569 */     String str3 = new String(paramArrayOfByte);
/* 570 */     String str4 = str3.toUpperCase();
/* 571 */     if (-1 == str4.indexOf("<HTML")) {
/* 572 */       str1 = "<HTML>";
/* 573 */       str2 = "</HTML>";
/* 574 */       if (-1 == str4.indexOf("<BODY")) {
/* 575 */         str1 = str1 + "<BODY>";
/* 576 */         str2 = "</BODY>" + str2;
/*     */       }
/*     */     }
/* 579 */     str1 = str1 + "<!--StartFragment-->";
/* 580 */     str2 = "<!--EndFragment-->" + str2;
/*     */ 
/* 583 */     str3 = "about:blank";
/* 584 */     int i = "Version:".length() + "1.0".length() + "\r\n".length() + "StartHTML:".length() + 10 + "\r\n".length() + "EndHTML:".length() + 10 + "\r\n".length() + "StartFragment:".length() + 10 + "\r\n".length() + "EndFragment:".length() + 10 + "\r\n".length() + "SourceURL:".length() + str3.length() + "\r\n".length();
/*     */ 
/* 592 */     int j = i + str1.length();
/* 593 */     int k = j + paramArrayOfByte.length - 1;
/* 594 */     int m = k + str2.length();
/*     */ 
/* 596 */     StringBuilder localStringBuilder = new StringBuilder(j + "<!--StartFragment-->".length());
/*     */ 
/* 601 */     localStringBuilder.append("Version:");
/* 602 */     localStringBuilder.append("1.0");
/* 603 */     localStringBuilder.append("\r\n");
/*     */ 
/* 605 */     localStringBuilder.append("StartHTML:");
/* 606 */     localStringBuilder.append(toPaddedString(i, 10));
/* 607 */     localStringBuilder.append("\r\n");
/*     */ 
/* 609 */     localStringBuilder.append("EndHTML:");
/* 610 */     localStringBuilder.append(toPaddedString(m, 10));
/* 611 */     localStringBuilder.append("\r\n");
/*     */ 
/* 613 */     localStringBuilder.append("StartFragment:");
/* 614 */     localStringBuilder.append(toPaddedString(j, 10));
/* 615 */     localStringBuilder.append("\r\n");
/*     */ 
/* 617 */     localStringBuilder.append("EndFragment:");
/* 618 */     localStringBuilder.append(toPaddedString(k, 10));
/* 619 */     localStringBuilder.append("\r\n");
/*     */ 
/* 621 */     localStringBuilder.append("SourceURL:");
/* 622 */     localStringBuilder.append(str3);
/* 623 */     localStringBuilder.append("\r\n");
/*     */ 
/* 626 */     localStringBuilder.append(str1);
/*     */ 
/* 628 */     byte[] arrayOfByte1 = null; byte[] arrayOfByte2 = null;
/*     */     try
/*     */     {
/* 631 */       arrayOfByte1 = localStringBuilder.toString().getBytes("UTF-8");
/* 632 */       arrayOfByte2 = str2.getBytes("UTF-8");
/*     */     }
/*     */     catch (UnsupportedEncodingException localUnsupportedEncodingException) {
/*     */     }
/* 636 */     byte[] arrayOfByte3 = new byte[arrayOfByte1.length + paramArrayOfByte.length + arrayOfByte2.length];
/*     */ 
/* 639 */     System.arraycopy(arrayOfByte1, 0, arrayOfByte3, 0, arrayOfByte1.length);
/* 640 */     System.arraycopy(paramArrayOfByte, 0, arrayOfByte3, arrayOfByte1.length, paramArrayOfByte.length - 1);
/*     */ 
/* 642 */     System.arraycopy(arrayOfByte2, 0, arrayOfByte3, arrayOfByte1.length + paramArrayOfByte.length - 1, arrayOfByte2.length);
/*     */ 
/* 645 */     arrayOfByte3[(arrayOfByte3.length - 1)] = 0;
/*     */ 
/* 647 */     return arrayOfByte3;
/*     */   }
/*     */ 
/*     */   public HTMLCodec(InputStream paramInputStream, EHTMLReadMode paramEHTMLReadMode)
/*     */     throws IOException
/*     */   {
/* 690 */     this.bufferedStream = new BufferedInputStream(paramInputStream, 8192);
/* 691 */     this.readMode = paramEHTMLReadMode;
/*     */   }
/*     */ 
/*     */   public synchronized String getBaseURL() throws IOException
/*     */   {
/* 696 */     if (!this.descriptionParsed) {
/* 697 */       parseDescription();
/*     */     }
/* 699 */     return this.stBaseURL;
/*     */   }
/*     */ 
/*     */   public synchronized String getVersion() throws IOException {
/* 703 */     if (!this.descriptionParsed) {
/* 704 */       parseDescription();
/*     */     }
/* 706 */     return this.stVersion;
/*     */   }
/*     */ 
/*     */   private void parseDescription()
/*     */     throws IOException
/*     */   {
/* 715 */     this.stBaseURL = null;
/* 716 */     this.stVersion = null;
/*     */ 
/* 720 */     this.iHTMLEnd = (this.iHTMLStart = this.iFragEnd = this.iFragStart = this.iSelEnd = this.iSelStart = -1L);
/*     */ 
/* 727 */     this.bufferedStream.mark(8192);
/* 728 */     String[] arrayOfString = { "Version:", "StartHTML:", "EndHTML:", "StartFragment:", "EndFragment:", "StartSelection:", "EndSelection:", "SourceURL:" };
/*     */ 
/* 740 */     BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(this.bufferedStream, "UTF-8"), 2730);
/*     */ 
/* 747 */     long l1 = 0L;
/* 748 */     long l2 = "\r\n".length();
/* 749 */     int i = arrayOfString.length;
/* 750 */     int j = 1;
/*     */ 
/* 752 */     for (int k = 0; k < i; k++) {
/* 753 */       String str1 = localBufferedReader.readLine();
/* 754 */       if (null == str1)
/*     */       {
/*     */         break;
/*     */       }
/* 758 */       for (; k < i; k++) {
/* 759 */         if (str1.startsWith(arrayOfString[k]))
/*     */         {
/* 762 */           l1 += str1.length() + l2;
/* 763 */           String str2 = str1.substring(arrayOfString[k].length()).trim();
/* 764 */           if (null == str2) break;
/*     */           try {
/* 766 */             switch (k) {
/*     */             case 0:
/* 768 */               this.stVersion = str2;
/* 769 */               break;
/*     */             case 1:
/* 771 */               this.iHTMLStart = Integer.parseInt(str2);
/* 772 */               break;
/*     */             case 2:
/* 774 */               this.iHTMLEnd = Integer.parseInt(str2);
/* 775 */               break;
/*     */             case 3:
/* 777 */               this.iFragStart = Integer.parseInt(str2);
/* 778 */               break;
/*     */             case 4:
/* 780 */               this.iFragEnd = Integer.parseInt(str2);
/* 781 */               break;
/*     */             case 5:
/* 783 */               this.iSelStart = Integer.parseInt(str2);
/* 784 */               break;
/*     */             case 6:
/* 786 */               this.iSelEnd = Integer.parseInt(str2);
/* 787 */               break;
/*     */             case 7:
/* 789 */               this.stBaseURL = str2;
/*     */             }
/*     */           }
/*     */           catch (NumberFormatException localNumberFormatException) {
/* 793 */             throw new IOException("Unable to parse HTML description: " + arrayOfString[k] + " value " + localNumberFormatException + " invalid");
/*     */           }
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 801 */     if (-1L == this.iHTMLStart)
/* 802 */       this.iHTMLStart = l1;
/* 803 */     if (-1L == this.iFragStart)
/* 804 */       this.iFragStart = this.iHTMLStart;
/* 805 */     if (-1L == this.iFragEnd)
/* 806 */       this.iFragEnd = this.iHTMLEnd;
/* 807 */     if (-1L == this.iSelStart)
/* 808 */       this.iSelStart = this.iFragStart;
/* 809 */     if (-1L == this.iSelEnd) {
/* 810 */       this.iSelEnd = this.iFragEnd;
/*     */     }
/*     */ 
/* 813 */     switch (1.$SwitchMap$sun$awt$windows$EHTMLReadMode[this.readMode.ordinal()]) {
/*     */     case 1:
/* 815 */       this.iStartOffset = this.iHTMLStart;
/* 816 */       this.iEndOffset = this.iHTMLEnd;
/* 817 */       break;
/*     */     case 2:
/* 819 */       this.iStartOffset = this.iFragStart;
/* 820 */       this.iEndOffset = this.iFragEnd;
/* 821 */       break;
/*     */     case 3:
/*     */     default:
/* 824 */       this.iStartOffset = this.iSelStart;
/* 825 */       this.iEndOffset = this.iSelEnd;
/*     */     }
/*     */ 
/* 829 */     this.bufferedStream.reset();
/* 830 */     if (-1L == this.iStartOffset) {
/* 831 */       throw new IOException("Unable to parse HTML description: invalid HTML format.");
/*     */     }
/*     */ 
/* 834 */     k = 0;
/* 835 */     while (k < this.iStartOffset) {
/* 836 */       k = (int)(k + this.bufferedStream.skip(this.iStartOffset - k));
/*     */     }
/*     */ 
/* 839 */     this.iReadCount = k;
/*     */ 
/* 841 */     if (this.iStartOffset != this.iReadCount) {
/* 842 */       throw new IOException("Unable to parse HTML description: Byte stream ends in description.");
/*     */     }
/* 844 */     this.descriptionParsed = true;
/*     */   }
/*     */ 
/*     */   public synchronized int read() throws IOException {
/* 848 */     if (this.closed) {
/* 849 */       throw new IOException("Stream closed");
/*     */     }
/*     */ 
/* 852 */     if (!this.descriptionParsed) {
/* 853 */       parseDescription();
/*     */     }
/* 855 */     if ((-1L != this.iEndOffset) && (this.iReadCount >= this.iEndOffset)) {
/* 856 */       return -1;
/*     */     }
/*     */ 
/* 859 */     int i = this.bufferedStream.read();
/* 860 */     if (i == -1) {
/* 861 */       return -1;
/*     */     }
/* 863 */     this.iReadCount += 1L;
/* 864 */     return i;
/*     */   }
/*     */ 
/*     */   public synchronized void close() throws IOException {
/* 868 */     if (!this.closed) {
/* 869 */       this.closed = true;
/* 870 */       this.bufferedStream.close();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.windows.HTMLCodec
 * JD-Core Version:    0.6.2
 */