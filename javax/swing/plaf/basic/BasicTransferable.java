/*     */ package javax.swing.plaf.basic;
/*     */ 
/*     */ import java.awt.datatransfer.DataFlavor;
/*     */ import java.awt.datatransfer.Transferable;
/*     */ import java.awt.datatransfer.UnsupportedFlavorException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.io.Reader;
/*     */ import java.io.StringBufferInputStream;
/*     */ import java.io.StringReader;
/*     */ import javax.swing.plaf.UIResource;
/*     */ 
/*     */ class BasicTransferable
/*     */   implements Transferable, UIResource
/*     */ {
/*     */   protected String plainData;
/*     */   protected String htmlData;
/*     */   private static DataFlavor[] htmlFlavors;
/*     */   private static DataFlavor[] stringFlavors;
/*     */   private static DataFlavor[] plainFlavors;
/*     */ 
/*     */   public BasicTransferable(String paramString1, String paramString2)
/*     */   {
/*  68 */     this.plainData = paramString1;
/*  69 */     this.htmlData = paramString2;
/*     */   }
/*     */ 
/*     */   public DataFlavor[] getTransferDataFlavors()
/*     */   {
/*  80 */     DataFlavor[] arrayOfDataFlavor1 = getRicherFlavors();
/*  81 */     int i = arrayOfDataFlavor1 != null ? arrayOfDataFlavor1.length : 0;
/*  82 */     int j = isHTMLSupported() ? htmlFlavors.length : 0;
/*  83 */     int k = isPlainSupported() ? plainFlavors.length : 0;
/*  84 */     int m = isPlainSupported() ? stringFlavors.length : 0;
/*  85 */     int n = i + j + k + m;
/*  86 */     DataFlavor[] arrayOfDataFlavor2 = new DataFlavor[n];
/*     */ 
/*  89 */     int i1 = 0;
/*  90 */     if (i > 0) {
/*  91 */       System.arraycopy(arrayOfDataFlavor1, 0, arrayOfDataFlavor2, i1, i);
/*  92 */       i1 += i;
/*     */     }
/*  94 */     if (j > 0) {
/*  95 */       System.arraycopy(htmlFlavors, 0, arrayOfDataFlavor2, i1, j);
/*  96 */       i1 += j;
/*     */     }
/*  98 */     if (k > 0) {
/*  99 */       System.arraycopy(plainFlavors, 0, arrayOfDataFlavor2, i1, k);
/* 100 */       i1 += k;
/*     */     }
/* 102 */     if (m > 0) {
/* 103 */       System.arraycopy(stringFlavors, 0, arrayOfDataFlavor2, i1, m);
/* 104 */       i1 += m;
/*     */     }
/* 106 */     return arrayOfDataFlavor2;
/*     */   }
/*     */ 
/*     */   public boolean isDataFlavorSupported(DataFlavor paramDataFlavor)
/*     */   {
/* 116 */     DataFlavor[] arrayOfDataFlavor = getTransferDataFlavors();
/* 117 */     for (int i = 0; i < arrayOfDataFlavor.length; i++) {
/* 118 */       if (arrayOfDataFlavor[i].equals(paramDataFlavor)) {
/* 119 */         return true;
/*     */       }
/*     */     }
/* 122 */     return false;
/*     */   }
/*     */ 
/*     */   public Object getTransferData(DataFlavor paramDataFlavor)
/*     */     throws UnsupportedFlavorException, IOException
/*     */   {
/* 137 */     DataFlavor[] arrayOfDataFlavor = getRicherFlavors();
/* 138 */     if (isRicherFlavor(paramDataFlavor))
/* 139 */       return getRicherData(paramDataFlavor);
/*     */     String str;
/* 140 */     if (isHTMLFlavor(paramDataFlavor)) {
/* 141 */       str = getHTMLData();
/* 142 */       str = str == null ? "" : str;
/* 143 */       if (String.class.equals(paramDataFlavor.getRepresentationClass()))
/* 144 */         return str;
/* 145 */       if (Reader.class.equals(paramDataFlavor.getRepresentationClass()))
/* 146 */         return new StringReader(str);
/* 147 */       if (InputStream.class.equals(paramDataFlavor.getRepresentationClass())) {
/* 148 */         return new StringBufferInputStream(str);
/*     */       }
/*     */     }
/* 151 */     else if (isPlainFlavor(paramDataFlavor)) {
/* 152 */       str = getPlainData();
/* 153 */       str = str == null ? "" : str;
/* 154 */       if (String.class.equals(paramDataFlavor.getRepresentationClass()))
/* 155 */         return str;
/* 156 */       if (Reader.class.equals(paramDataFlavor.getRepresentationClass()))
/* 157 */         return new StringReader(str);
/* 158 */       if (InputStream.class.equals(paramDataFlavor.getRepresentationClass())) {
/* 159 */         return new StringBufferInputStream(str);
/*     */       }
/*     */ 
/*     */     }
/* 163 */     else if (isStringFlavor(paramDataFlavor)) {
/* 164 */       str = getPlainData();
/* 165 */       str = str == null ? "" : str;
/* 166 */       return str;
/*     */     }
/* 168 */     throw new UnsupportedFlavorException(paramDataFlavor);
/*     */   }
/*     */ 
/*     */   protected boolean isRicherFlavor(DataFlavor paramDataFlavor)
/*     */   {
/* 174 */     DataFlavor[] arrayOfDataFlavor = getRicherFlavors();
/* 175 */     int i = arrayOfDataFlavor != null ? arrayOfDataFlavor.length : 0;
/* 176 */     for (int j = 0; j < i; j++) {
/* 177 */       if (arrayOfDataFlavor[j].equals(paramDataFlavor)) {
/* 178 */         return true;
/*     */       }
/*     */     }
/* 181 */     return false;
/*     */   }
/*     */ 
/*     */   protected DataFlavor[] getRicherFlavors()
/*     */   {
/* 190 */     return null;
/*     */   }
/*     */ 
/*     */   protected Object getRicherData(DataFlavor paramDataFlavor) throws UnsupportedFlavorException {
/* 194 */     return null;
/*     */   }
/*     */ 
/*     */   protected boolean isHTMLFlavor(DataFlavor paramDataFlavor)
/*     */   {
/* 206 */     DataFlavor[] arrayOfDataFlavor = htmlFlavors;
/* 207 */     for (int i = 0; i < arrayOfDataFlavor.length; i++) {
/* 208 */       if (arrayOfDataFlavor[i].equals(paramDataFlavor)) {
/* 209 */         return true;
/*     */       }
/*     */     }
/* 212 */     return false;
/*     */   }
/*     */ 
/*     */   protected boolean isHTMLSupported()
/*     */   {
/* 220 */     return this.htmlData != null;
/*     */   }
/*     */ 
/*     */   protected String getHTMLData()
/*     */   {
/* 227 */     return this.htmlData;
/*     */   }
/*     */ 
/*     */   protected boolean isPlainFlavor(DataFlavor paramDataFlavor)
/*     */   {
/* 239 */     DataFlavor[] arrayOfDataFlavor = plainFlavors;
/* 240 */     for (int i = 0; i < arrayOfDataFlavor.length; i++) {
/* 241 */       if (arrayOfDataFlavor[i].equals(paramDataFlavor)) {
/* 242 */         return true;
/*     */       }
/*     */     }
/* 245 */     return false;
/*     */   }
/*     */ 
/*     */   protected boolean isPlainSupported()
/*     */   {
/* 253 */     return this.plainData != null;
/*     */   }
/*     */ 
/*     */   protected String getPlainData()
/*     */   {
/* 260 */     return this.plainData;
/*     */   }
/*     */ 
/*     */   protected boolean isStringFlavor(DataFlavor paramDataFlavor)
/*     */   {
/* 272 */     DataFlavor[] arrayOfDataFlavor = stringFlavors;
/* 273 */     for (int i = 0; i < arrayOfDataFlavor.length; i++) {
/* 274 */       if (arrayOfDataFlavor[i].equals(paramDataFlavor)) {
/* 275 */         return true;
/*     */       }
/*     */     }
/* 278 */     return false;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*     */     try
/*     */     {
/*  48 */       htmlFlavors = new DataFlavor[3];
/*  49 */       htmlFlavors[0] = new DataFlavor("text/html;class=java.lang.String");
/*  50 */       htmlFlavors[1] = new DataFlavor("text/html;class=java.io.Reader");
/*  51 */       htmlFlavors[2] = new DataFlavor("text/html;charset=unicode;class=java.io.InputStream");
/*     */ 
/*  53 */       plainFlavors = new DataFlavor[3];
/*  54 */       plainFlavors[0] = new DataFlavor("text/plain;class=java.lang.String");
/*  55 */       plainFlavors[1] = new DataFlavor("text/plain;class=java.io.Reader");
/*  56 */       plainFlavors[2] = new DataFlavor("text/plain;charset=unicode;class=java.io.InputStream");
/*     */ 
/*  58 */       stringFlavors = new DataFlavor[2];
/*  59 */       stringFlavors[0] = new DataFlavor("application/x-java-jvm-local-objectref;class=java.lang.String");
/*  60 */       stringFlavors[1] = DataFlavor.stringFlavor;
/*     */     }
/*     */     catch (ClassNotFoundException localClassNotFoundException) {
/*  63 */       System.err.println("error initializing javax.swing.plaf.basic.BasicTranserable");
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.basic.BasicTransferable
 * JD-Core Version:    0.6.2
 */