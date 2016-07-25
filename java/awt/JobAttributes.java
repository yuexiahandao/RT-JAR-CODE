/*      */ package java.awt;
/*      */ 
/*      */ public final class JobAttributes
/*      */   implements Cloneable
/*      */ {
/*      */   private int copies;
/*      */   private DefaultSelectionType defaultSelection;
/*      */   private DestinationType destination;
/*      */   private DialogType dialog;
/*      */   private String fileName;
/*      */   private int fromPage;
/*      */   private int maxPage;
/*      */   private int minPage;
/*      */   private MultipleDocumentHandlingType multipleDocumentHandling;
/*      */   private int[][] pageRanges;
/*      */   private int prFirst;
/*      */   private int prLast;
/*      */   private String printer;
/*      */   private SidesType sides;
/*      */   private int toPage;
/*      */ 
/*      */   public JobAttributes()
/*      */   {
/*  273 */     setCopiesToDefault();
/*  274 */     setDefaultSelection(DefaultSelectionType.ALL);
/*  275 */     setDestination(DestinationType.PRINTER);
/*  276 */     setDialog(DialogType.NATIVE);
/*  277 */     setMaxPage(2147483647);
/*  278 */     setMinPage(1);
/*  279 */     setMultipleDocumentHandlingToDefault();
/*  280 */     setSidesToDefault();
/*      */   }
/*      */ 
/*      */   public JobAttributes(JobAttributes paramJobAttributes)
/*      */   {
/*  290 */     set(paramJobAttributes);
/*      */   }
/*      */ 
/*      */   public JobAttributes(int paramInt1, DefaultSelectionType paramDefaultSelectionType, DestinationType paramDestinationType, DialogType paramDialogType, String paramString1, int paramInt2, int paramInt3, MultipleDocumentHandlingType paramMultipleDocumentHandlingType, int[][] paramArrayOfInt, String paramString2, SidesType paramSidesType)
/*      */   {
/*  340 */     setCopies(paramInt1);
/*  341 */     setDefaultSelection(paramDefaultSelectionType);
/*  342 */     setDestination(paramDestinationType);
/*  343 */     setDialog(paramDialogType);
/*  344 */     setFileName(paramString1);
/*  345 */     setMaxPage(paramInt2);
/*  346 */     setMinPage(paramInt3);
/*  347 */     setMultipleDocumentHandling(paramMultipleDocumentHandlingType);
/*  348 */     setPageRanges(paramArrayOfInt);
/*  349 */     setPrinter(paramString2);
/*  350 */     setSides(paramSidesType);
/*      */   }
/*      */ 
/*      */   public Object clone()
/*      */   {
/*      */     try
/*      */     {
/*  361 */       return super.clone();
/*      */     } catch (CloneNotSupportedException localCloneNotSupportedException) {
/*      */     }
/*  364 */     throw new InternalError();
/*      */   }
/*      */ 
/*      */   public void set(JobAttributes paramJobAttributes)
/*      */   {
/*  375 */     this.copies = paramJobAttributes.copies;
/*  376 */     this.defaultSelection = paramJobAttributes.defaultSelection;
/*  377 */     this.destination = paramJobAttributes.destination;
/*  378 */     this.dialog = paramJobAttributes.dialog;
/*  379 */     this.fileName = paramJobAttributes.fileName;
/*  380 */     this.fromPage = paramJobAttributes.fromPage;
/*  381 */     this.maxPage = paramJobAttributes.maxPage;
/*  382 */     this.minPage = paramJobAttributes.minPage;
/*  383 */     this.multipleDocumentHandling = paramJobAttributes.multipleDocumentHandling;
/*      */ 
/*  385 */     this.pageRanges = paramJobAttributes.pageRanges;
/*  386 */     this.prFirst = paramJobAttributes.prFirst;
/*  387 */     this.prLast = paramJobAttributes.prLast;
/*  388 */     this.printer = paramJobAttributes.printer;
/*  389 */     this.sides = paramJobAttributes.sides;
/*  390 */     this.toPage = paramJobAttributes.toPage;
/*      */   }
/*      */ 
/*      */   public int getCopies()
/*      */   {
/*  401 */     return this.copies;
/*      */   }
/*      */ 
/*      */   public void setCopies(int paramInt)
/*      */   {
/*  414 */     if (paramInt <= 0) {
/*  415 */       throw new IllegalArgumentException("Invalid value for attribute copies");
/*      */     }
/*      */ 
/*  418 */     this.copies = paramInt;
/*      */   }
/*      */ 
/*      */   public void setCopiesToDefault()
/*      */   {
/*  426 */     setCopies(1);
/*      */   }
/*      */ 
/*      */   public DefaultSelectionType getDefaultSelection()
/*      */   {
/*  439 */     return this.defaultSelection;
/*      */   }
/*      */ 
/*      */   public void setDefaultSelection(DefaultSelectionType paramDefaultSelectionType)
/*      */   {
/*  453 */     if (paramDefaultSelectionType == null) {
/*  454 */       throw new IllegalArgumentException("Invalid value for attribute defaultSelection");
/*      */     }
/*      */ 
/*  457 */     this.defaultSelection = paramDefaultSelectionType;
/*      */   }
/*      */ 
/*      */   public DestinationType getDestination()
/*      */   {
/*  468 */     return this.destination;
/*      */   }
/*      */ 
/*      */   public void setDestination(DestinationType paramDestinationType)
/*      */   {
/*  480 */     if (paramDestinationType == null) {
/*  481 */       throw new IllegalArgumentException("Invalid value for attribute destination");
/*      */     }
/*      */ 
/*  484 */     this.destination = paramDestinationType;
/*      */   }
/*      */ 
/*      */   public DialogType getDialog()
/*      */   {
/*  502 */     return this.dialog;
/*      */   }
/*      */ 
/*      */   public void setDialog(DialogType paramDialogType)
/*      */   {
/*  521 */     if (paramDialogType == null) {
/*  522 */       throw new IllegalArgumentException("Invalid value for attribute dialog");
/*      */     }
/*      */ 
/*  525 */     this.dialog = paramDialogType;
/*      */   }
/*      */ 
/*      */   public String getFileName()
/*      */   {
/*  535 */     return this.fileName;
/*      */   }
/*      */ 
/*      */   public void setFileName(String paramString)
/*      */   {
/*  545 */     this.fileName = paramString;
/*      */   }
/*      */ 
/*      */   public int getFromPage()
/*      */   {
/*  562 */     if (this.fromPage != 0)
/*  563 */       return this.fromPage;
/*  564 */     if (this.toPage != 0)
/*  565 */       return getMinPage();
/*  566 */     if (this.pageRanges != null) {
/*  567 */       return this.prFirst;
/*      */     }
/*  569 */     return getMinPage();
/*      */   }
/*      */ 
/*      */   public void setFromPage(int paramInt)
/*      */   {
/*  589 */     if ((paramInt <= 0) || ((this.toPage != 0) && (paramInt > this.toPage)) || (paramInt < this.minPage) || (paramInt > this.maxPage))
/*      */     {
/*  593 */       throw new IllegalArgumentException("Invalid value for attribute fromPage");
/*      */     }
/*      */ 
/*  596 */     this.fromPage = paramInt;
/*      */   }
/*      */ 
/*      */   public int getMaxPage()
/*      */   {
/*  609 */     return this.maxPage;
/*      */   }
/*      */ 
/*      */   public void setMaxPage(int paramInt)
/*      */   {
/*  623 */     if ((paramInt <= 0) || (paramInt < this.minPage)) {
/*  624 */       throw new IllegalArgumentException("Invalid value for attribute maxPage");
/*      */     }
/*      */ 
/*  627 */     this.maxPage = paramInt;
/*      */   }
/*      */ 
/*      */   public int getMinPage()
/*      */   {
/*  640 */     return this.minPage;
/*      */   }
/*      */ 
/*      */   public void setMinPage(int paramInt)
/*      */   {
/*  654 */     if ((paramInt <= 0) || (paramInt > this.maxPage)) {
/*  655 */       throw new IllegalArgumentException("Invalid value for attribute minPage");
/*      */     }
/*      */ 
/*  658 */     this.minPage = paramInt;
/*      */   }
/*      */ 
/*      */   public MultipleDocumentHandlingType getMultipleDocumentHandling()
/*      */   {
/*  671 */     return this.multipleDocumentHandling;
/*      */   }
/*      */ 
/*      */   public void setMultipleDocumentHandling(MultipleDocumentHandlingType paramMultipleDocumentHandlingType)
/*      */   {
/*  687 */     if (paramMultipleDocumentHandlingType == null) {
/*  688 */       throw new IllegalArgumentException("Invalid value for attribute multipleDocumentHandling");
/*      */     }
/*      */ 
/*  691 */     this.multipleDocumentHandling = paramMultipleDocumentHandlingType;
/*      */   }
/*      */ 
/*      */   public void setMultipleDocumentHandlingToDefault()
/*      */   {
/*  700 */     setMultipleDocumentHandling(MultipleDocumentHandlingType.SEPARATE_DOCUMENTS_UNCOLLATED_COPIES);
/*      */   }
/*      */ 
/*      */   public int[][] getPageRanges()
/*      */   {
/*      */     int j;
/*  723 */     if (this.pageRanges != null)
/*      */     {
/*  727 */       int[][] arrayOfInt = new int[this.pageRanges.length][2];
/*  728 */       for (j = 0; j < this.pageRanges.length; j++) {
/*  729 */         arrayOfInt[j][0] = this.pageRanges[j][0];
/*  730 */         arrayOfInt[j][1] = this.pageRanges[j][1];
/*      */       }
/*  732 */       return arrayOfInt;
/*  733 */     }if ((this.fromPage != 0) || (this.toPage != 0)) {
/*  734 */       i = getFromPage();
/*  735 */       j = getToPage();
/*  736 */       return new int[][] { { i, j } };
/*      */     }
/*  738 */     int i = getMinPage();
/*  739 */     return new int[][] { { i, i } };
/*      */   }
/*      */ 
/*      */   public void setPageRanges(int[][] paramArrayOfInt)
/*      */   {
/*  769 */     String str = "Invalid value for attribute pageRanges";
/*  770 */     int i = 0;
/*  771 */     int j = 0;
/*      */ 
/*  773 */     if (paramArrayOfInt == null) {
/*  774 */       throw new IllegalArgumentException(str);
/*      */     }
/*      */ 
/*  777 */     for (int k = 0; k < paramArrayOfInt.length; k++) {
/*  778 */       if ((paramArrayOfInt[k] == null) || (paramArrayOfInt[k].length != 2) || (paramArrayOfInt[k][0] <= j) || (paramArrayOfInt[k][1] < paramArrayOfInt[k][0]))
/*      */       {
/*  782 */         throw new IllegalArgumentException(str);
/*      */       }
/*  784 */       j = paramArrayOfInt[k][1];
/*  785 */       if (i == 0) {
/*  786 */         i = paramArrayOfInt[k][0];
/*      */       }
/*      */     }
/*      */ 
/*  790 */     if ((i < this.minPage) || (j > this.maxPage)) {
/*  791 */       throw new IllegalArgumentException(str);
/*      */     }
/*      */ 
/*  797 */     int[][] arrayOfInt = new int[paramArrayOfInt.length][2];
/*  798 */     for (int m = 0; m < paramArrayOfInt.length; m++) {
/*  799 */       arrayOfInt[m][0] = paramArrayOfInt[m][0];
/*  800 */       arrayOfInt[m][1] = paramArrayOfInt[m][1];
/*      */     }
/*  802 */     this.pageRanges = arrayOfInt;
/*  803 */     this.prFirst = i;
/*  804 */     this.prLast = j;
/*      */   }
/*      */ 
/*      */   public String getPrinter()
/*      */   {
/*  814 */     return this.printer;
/*      */   }
/*      */ 
/*      */   public void setPrinter(String paramString)
/*      */   {
/*  824 */     this.printer = paramString;
/*      */   }
/*      */ 
/*      */   public SidesType getSides()
/*      */   {
/*  847 */     return this.sides;
/*      */   }
/*      */ 
/*      */   public void setSides(SidesType paramSidesType)
/*      */   {
/*  871 */     if (paramSidesType == null) {
/*  872 */       throw new IllegalArgumentException("Invalid value for attribute sides");
/*      */     }
/*      */ 
/*  875 */     this.sides = paramSidesType;
/*      */   }
/*      */ 
/*      */   public void setSidesToDefault()
/*      */   {
/*  884 */     setSides(SidesType.ONE_SIDED);
/*      */   }
/*      */ 
/*      */   public int getToPage()
/*      */   {
/*  901 */     if (this.toPage != 0)
/*  902 */       return this.toPage;
/*  903 */     if (this.fromPage != 0)
/*  904 */       return this.fromPage;
/*  905 */     if (this.pageRanges != null) {
/*  906 */       return this.prLast;
/*      */     }
/*  908 */     return getMinPage();
/*      */   }
/*      */ 
/*      */   public void setToPage(int paramInt)
/*      */   {
/*  928 */     if ((paramInt <= 0) || ((this.fromPage != 0) && (paramInt < this.fromPage)) || (paramInt < this.minPage) || (paramInt > this.maxPage))
/*      */     {
/*  932 */       throw new IllegalArgumentException("Invalid value for attribute toPage");
/*      */     }
/*      */ 
/*  935 */     this.toPage = paramInt;
/*      */   }
/*      */ 
/*      */   public boolean equals(Object paramObject)
/*      */   {
/*  952 */     if (!(paramObject instanceof JobAttributes)) {
/*  953 */       return false;
/*      */     }
/*  955 */     JobAttributes localJobAttributes = (JobAttributes)paramObject;
/*      */ 
/*  957 */     if (this.fileName == null) {
/*  958 */       if (localJobAttributes.fileName != null) {
/*  959 */         return false;
/*      */       }
/*      */     }
/*  962 */     else if (!this.fileName.equals(localJobAttributes.fileName)) {
/*  963 */       return false;
/*      */     }
/*      */ 
/*  967 */     if (this.pageRanges == null) {
/*  968 */       if (localJobAttributes.pageRanges != null)
/*  969 */         return false;
/*      */     }
/*      */     else {
/*  972 */       if ((localJobAttributes.pageRanges == null) || (this.pageRanges.length != localJobAttributes.pageRanges.length))
/*      */       {
/*  974 */         return false;
/*      */       }
/*  976 */       for (int i = 0; i < this.pageRanges.length; i++) {
/*  977 */         if ((this.pageRanges[i][0] != localJobAttributes.pageRanges[i][0]) || (this.pageRanges[i][1] != localJobAttributes.pageRanges[i][1]))
/*      */         {
/*  979 */           return false;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  984 */     if (this.printer == null) {
/*  985 */       if (localJobAttributes.printer != null) {
/*  986 */         return false;
/*      */       }
/*      */     }
/*  989 */     else if (!this.printer.equals(localJobAttributes.printer)) {
/*  990 */       return false;
/*      */     }
/*      */ 
/*  994 */     return (this.copies == localJobAttributes.copies) && (this.defaultSelection == localJobAttributes.defaultSelection) && (this.destination == localJobAttributes.destination) && (this.dialog == localJobAttributes.dialog) && (this.fromPage == localJobAttributes.fromPage) && (this.maxPage == localJobAttributes.maxPage) && (this.minPage == localJobAttributes.minPage) && (this.multipleDocumentHandling == localJobAttributes.multipleDocumentHandling) && (this.prFirst == localJobAttributes.prFirst) && (this.prLast == localJobAttributes.prLast) && (this.sides == localJobAttributes.sides) && (this.toPage == localJobAttributes.toPage);
/*      */   }
/*      */ 
/*      */   public int hashCode()
/*      */   {
/* 1014 */     int i = (this.copies + this.fromPage + this.maxPage + this.minPage + this.prFirst + this.prLast + this.toPage) * 31 << 21;
/*      */ 
/* 1016 */     if (this.pageRanges != null) {
/* 1017 */       int j = 0;
/* 1018 */       for (int k = 0; k < this.pageRanges.length; k++) {
/* 1019 */         j += this.pageRanges[k][0] + this.pageRanges[k][1];
/*      */       }
/* 1021 */       i ^= j * 31 << 11;
/*      */     }
/* 1023 */     if (this.fileName != null) {
/* 1024 */       i ^= this.fileName.hashCode();
/*      */     }
/* 1026 */     if (this.printer != null) {
/* 1027 */       i ^= this.printer.hashCode();
/*      */     }
/* 1029 */     return this.defaultSelection.hashCode() << 6 ^ this.destination.hashCode() << 5 ^ this.dialog.hashCode() << 3 ^ this.multipleDocumentHandling.hashCode() << 2 ^ this.sides.hashCode() ^ i;
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/* 1043 */     int[][] arrayOfInt = getPageRanges();
/* 1044 */     String str = "[";
/* 1045 */     int i = 1;
/* 1046 */     for (int j = 0; j < arrayOfInt.length; j++) {
/* 1047 */       if (i != 0)
/* 1048 */         i = 0;
/*      */       else {
/* 1050 */         str = str + ",";
/*      */       }
/* 1052 */       str = str + arrayOfInt[j][0] + ":" + arrayOfInt[j][1];
/*      */     }
/* 1054 */     str = str + "]";
/*      */ 
/* 1056 */     return "copies=" + getCopies() + ",defaultSelection=" + getDefaultSelection() + ",destination=" + getDestination() + ",dialog=" + getDialog() + ",fileName=" + getFileName() + ",fromPage=" + getFromPage() + ",maxPage=" + getMaxPage() + ",minPage=" + getMinPage() + ",multiple-document-handling=" + getMultipleDocumentHandling() + ",page-ranges=" + str + ",printer=" + getPrinter() + ",sides=" + getSides() + ",toPage=" + getToPage();
/*      */   }
/*      */ 
/*      */   public static final class DefaultSelectionType extends AttributeValue
/*      */   {
/*      */     private static final int I_ALL = 0;
/*      */     private static final int I_RANGE = 1;
/*      */     private static final int I_SELECTION = 2;
/*   70 */     private static final String[] NAMES = { "all", "range", "selection" };
/*      */ 
/*   78 */     public static final DefaultSelectionType ALL = new DefaultSelectionType(0);
/*      */ 
/*   84 */     public static final DefaultSelectionType RANGE = new DefaultSelectionType(1);
/*      */ 
/*   90 */     public static final DefaultSelectionType SELECTION = new DefaultSelectionType(2);
/*      */ 
/*      */     private DefaultSelectionType(int paramInt)
/*      */     {
/*   94 */       super(NAMES);
/*      */     }
/*      */   }
/*      */ 
/*      */   public static final class DestinationType extends AttributeValue
/*      */   {
/*      */     private static final int I_FILE = 0;
/*      */     private static final int I_PRINTER = 1;
/*  106 */     private static final String[] NAMES = { "file", "printer" };
/*      */ 
/*  114 */     public static final DestinationType FILE = new DestinationType(0);
/*      */ 
/*  120 */     public static final DestinationType PRINTER = new DestinationType(1);
/*      */ 
/*      */     private DestinationType(int paramInt)
/*      */     {
/*  124 */       super(NAMES);
/*      */     }
/*      */   }
/*      */ 
/*      */   public static final class DialogType extends AttributeValue
/*      */   {
/*      */     private static final int I_COMMON = 0;
/*      */     private static final int I_NATIVE = 1;
/*      */     private static final int I_NONE = 2;
/*  137 */     private static final String[] NAMES = { "common", "native", "none" };
/*      */ 
/*  145 */     public static final DialogType COMMON = new DialogType(0);
/*      */ 
/*  150 */     public static final DialogType NATIVE = new DialogType(1);
/*      */ 
/*  155 */     public static final DialogType NONE = new DialogType(2);
/*      */ 
/*      */     private DialogType(int paramInt) {
/*  158 */       super(NAMES);
/*      */     }
/*      */   }
/*      */ 
/*      */   public static final class MultipleDocumentHandlingType extends AttributeValue
/*      */   {
/*      */     private static final int I_SEPARATE_DOCUMENTS_COLLATED_COPIES = 0;
/*      */     private static final int I_SEPARATE_DOCUMENTS_UNCOLLATED_COPIES = 1;
/*  173 */     private static final String[] NAMES = { "separate-documents-collated-copies", "separate-documents-uncollated-copies" };
/*      */ 
/*  183 */     public static final MultipleDocumentHandlingType SEPARATE_DOCUMENTS_COLLATED_COPIES = new MultipleDocumentHandlingType(0);
/*      */ 
/*  191 */     public static final MultipleDocumentHandlingType SEPARATE_DOCUMENTS_UNCOLLATED_COPIES = new MultipleDocumentHandlingType(1);
/*      */ 
/*      */     private MultipleDocumentHandlingType(int paramInt)
/*      */     {
/*  196 */       super(NAMES);
/*      */     }
/*      */   }
/*      */ 
/*      */   public static final class SidesType extends AttributeValue
/*      */   {
/*      */     private static final int I_ONE_SIDED = 0;
/*      */     private static final int I_TWO_SIDED_LONG_EDGE = 1;
/*      */     private static final int I_TWO_SIDED_SHORT_EDGE = 2;
/*  210 */     private static final String[] NAMES = { "one-sided", "two-sided-long-edge", "two-sided-short-edge" };
/*      */ 
/*  219 */     public static final SidesType ONE_SIDED = new SidesType(0);
/*      */ 
/*  227 */     public static final SidesType TWO_SIDED_LONG_EDGE = new SidesType(1);
/*      */ 
/*  236 */     public static final SidesType TWO_SIDED_SHORT_EDGE = new SidesType(2);
/*      */ 
/*      */     private SidesType(int paramInt)
/*      */     {
/*  240 */       super(NAMES);
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.JobAttributes
 * JD-Core Version:    0.6.2
 */