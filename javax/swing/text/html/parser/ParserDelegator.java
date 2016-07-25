/*     */ package javax.swing.text.html.parser;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.io.Reader;
/*     */ import java.io.Serializable;
/*     */ import javax.swing.text.html.HTMLEditorKit.Parser;
/*     */ import javax.swing.text.html.HTMLEditorKit.ParserCallback;
/*     */ import sun.awt.AppContext;
/*     */ 
/*     */ public class ParserDelegator extends HTMLEditorKit.Parser
/*     */   implements Serializable
/*     */ {
/*  49 */   private static final Object DTD_KEY = new Object();
/*     */ 
/*     */   protected static void setDefaultDTD() {
/*  52 */     getDefaultDTD();
/*     */   }
/*     */ 
/*     */   private static synchronized DTD getDefaultDTD() {
/*  56 */     AppContext localAppContext = AppContext.getAppContext();
/*     */ 
/*  58 */     DTD localDTD1 = (DTD)localAppContext.get(DTD_KEY);
/*     */ 
/*  60 */     if (localDTD1 == null) {
/*  61 */       DTD localDTD2 = null;
/*     */ 
/*  63 */       String str = "html32";
/*     */       try {
/*  65 */         localDTD2 = DTD.getDTD(str);
/*     */       }
/*     */       catch (IOException localIOException) {
/*  68 */         System.out.println("Throw an exception: could not get default dtd: " + str);
/*     */       }
/*  70 */       localDTD1 = createDTD(localDTD2, str);
/*     */ 
/*  72 */       localAppContext.put(DTD_KEY, localDTD1);
/*     */     }
/*     */ 
/*  75 */     return localDTD1;
/*     */   }
/*     */ 
/*     */   protected static DTD createDTD(DTD paramDTD, String paramString)
/*     */   {
/*  80 */     InputStream localInputStream = null;
/*  81 */     int i = 1;
/*     */     try {
/*  83 */       String str = paramString + ".bdtd";
/*  84 */       localInputStream = getResourceAsStream(str);
/*  85 */       if (localInputStream != null) {
/*  86 */         paramDTD.read(new DataInputStream(new BufferedInputStream(localInputStream)));
/*  87 */         DTD.putDTDHash(paramString, paramDTD);
/*     */       }
/*     */     } catch (Exception localException) {
/*  90 */       System.out.println(localException);
/*     */     }
/*  92 */     return paramDTD;
/*     */   }
/*     */ 
/*     */   public ParserDelegator()
/*     */   {
/*  97 */     setDefaultDTD();
/*     */   }
/*     */ 
/*     */   public void parse(Reader paramReader, HTMLEditorKit.ParserCallback paramParserCallback, boolean paramBoolean) throws IOException {
/* 101 */     new DocumentParser(getDefaultDTD()).parse(paramReader, paramParserCallback, paramBoolean);
/*     */   }
/*     */ 
/*     */   static InputStream getResourceAsStream(String paramString)
/*     */   {
/*     */     try
/*     */     {
/* 116 */       return ResourceLoader.getResourceAsStream(paramString);
/*     */     }
/*     */     catch (Throwable localThrowable) {
/*     */     }
/* 120 */     return ParserDelegator.class.getResourceAsStream(paramString);
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws ClassNotFoundException, IOException
/*     */   {
/* 126 */     paramObjectInputStream.defaultReadObject();
/* 127 */     setDefaultDTD();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.html.parser.ParserDelegator
 * JD-Core Version:    0.6.2
 */