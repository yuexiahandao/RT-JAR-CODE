/*      */ package sun.print;
/*      */ 
/*      */ import java.util.ArrayList;
/*      */ import javax.print.attribute.EnumSyntax;
/*      */ import javax.print.attribute.standard.MediaSize;
/*      */ import javax.print.attribute.standard.MediaSizeName;
/*      */ 
/*      */ class Win32MediaSize extends MediaSizeName
/*      */ {
/* 1723 */   private static ArrayList winStringTable = new ArrayList();
/* 1724 */   private static ArrayList winEnumTable = new ArrayList();
/*      */   private static MediaSize[] predefMedia;
/*      */   private int dmPaperID;
/*      */ 
/*      */   private Win32MediaSize(int paramInt)
/*      */   {
/* 1730 */     super(paramInt);
/*      */   }
/*      */ 
/*      */   private static synchronized int nextValue(String paramString)
/*      */   {
/* 1735 */     winStringTable.add(paramString);
/* 1736 */     return winStringTable.size() - 1;
/*      */   }
/*      */ 
/*      */   public static synchronized Win32MediaSize findMediaName(String paramString) {
/* 1740 */     int i = winStringTable.indexOf(paramString);
/* 1741 */     if (i != -1) {
/* 1742 */       return (Win32MediaSize)winEnumTable.get(i);
/*      */     }
/* 1744 */     return null;
/*      */   }
/*      */ 
/*      */   public static MediaSize[] getPredefMedia() {
/* 1748 */     return predefMedia;
/*      */   }
/*      */ 
/*      */   public Win32MediaSize(String paramString, int paramInt) {
/* 1752 */     super(nextValue(paramString));
/* 1753 */     this.dmPaperID = paramInt;
/* 1754 */     winEnumTable.add(this);
/*      */   }
/*      */ 
/*      */   private MediaSizeName[] getSuperEnumTable() {
/* 1758 */     return (MediaSizeName[])super.getEnumValueTable();
/*      */   }
/*      */ 
/*      */   int getDMPaper()
/*      */   {
/* 1779 */     return this.dmPaperID;
/*      */   }
/*      */ 
/*      */   protected String[] getStringTable() {
/* 1783 */     String[] arrayOfString = new String[winStringTable.size()];
/* 1784 */     return (String[])winStringTable.toArray(arrayOfString);
/*      */   }
/*      */ 
/*      */   protected EnumSyntax[] getEnumValueTable() {
/* 1788 */     MediaSizeName[] arrayOfMediaSizeName = new MediaSizeName[winEnumTable.size()];
/* 1789 */     return (MediaSizeName[])winEnumTable.toArray(arrayOfMediaSizeName);
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/* 1764 */     Win32MediaSize localWin32MediaSize = new Win32MediaSize(-1);
/*      */ 
/* 1767 */     MediaSizeName[] arrayOfMediaSizeName = localWin32MediaSize.getSuperEnumTable();
/* 1768 */     if (arrayOfMediaSizeName != null) {
/* 1769 */       predefMedia = new MediaSize[arrayOfMediaSizeName.length];
/*      */ 
/* 1771 */       for (int i = 0; i < arrayOfMediaSizeName.length; i++)
/* 1772 */         predefMedia[i] = MediaSize.getMediaSizeForName(arrayOfMediaSizeName[i]);
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.print.Win32MediaSize
 * JD-Core Version:    0.6.2
 */