/*     */ package javax.swing.text.rtf;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Reader;
/*     */ import java.io.Writer;
/*     */ import javax.swing.text.BadLocationException;
/*     */ import javax.swing.text.Document;
/*     */ import javax.swing.text.StyledDocument;
/*     */ import javax.swing.text.StyledEditorKit;
/*     */ 
/*     */ public class RTFEditorKit extends StyledEditorKit
/*     */ {
/*     */   public String getContentType()
/*     */   {
/*  60 */     return "text/rtf";
/*     */   }
/*     */ 
/*     */   public void read(InputStream paramInputStream, Document paramDocument, int paramInt)
/*     */     throws IOException, BadLocationException
/*     */   {
/*  78 */     if ((paramDocument instanceof StyledDocument))
/*     */     {
/*  81 */       RTFReader localRTFReader = new RTFReader((StyledDocument)paramDocument);
/*  82 */       localRTFReader.readFromStream(paramInputStream);
/*  83 */       localRTFReader.close();
/*     */     }
/*     */     else {
/*  86 */       super.read(paramInputStream, paramDocument, paramInt);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void write(OutputStream paramOutputStream, Document paramDocument, int paramInt1, int paramInt2)
/*     */     throws IOException, BadLocationException
/*     */   {
/* 108 */     RTFGenerator.writeDocument(paramDocument, paramOutputStream);
/*     */   }
/*     */ 
/*     */   public void read(Reader paramReader, Document paramDocument, int paramInt)
/*     */     throws IOException, BadLocationException
/*     */   {
/* 126 */     if ((paramDocument instanceof StyledDocument)) {
/* 127 */       RTFReader localRTFReader = new RTFReader((StyledDocument)paramDocument);
/* 128 */       localRTFReader.readFromReader(paramReader);
/* 129 */       localRTFReader.close();
/*     */     }
/*     */     else {
/* 132 */       super.read(paramReader, paramDocument, paramInt);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void write(Writer paramWriter, Document paramDocument, int paramInt1, int paramInt2)
/*     */     throws IOException, BadLocationException
/*     */   {
/* 152 */     throw new IOException("RTF is an 8-bit format");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.rtf.RTFEditorKit
 * JD-Core Version:    0.6.2
 */