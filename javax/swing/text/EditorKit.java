/*    */ package javax.swing.text;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.OutputStream;
/*    */ import java.io.Reader;
/*    */ import java.io.Serializable;
/*    */ import java.io.Writer;
/*    */ import javax.swing.Action;
/*    */ import javax.swing.JEditorPane;
/*    */ 
/*    */ public abstract class EditorKit
/*    */   implements Cloneable, Serializable
/*    */ {
/*    */   public Object clone()
/*    */   {
/*    */     Object localObject;
/*    */     try
/*    */     {
/* 66 */       localObject = super.clone();
/*    */     } catch (CloneNotSupportedException localCloneNotSupportedException) {
/* 68 */       localObject = null;
/*    */     }
/* 70 */     return localObject;
/*    */   }
/*    */ 
/*    */   public void install(JEditorPane paramJEditorPane)
/*    */   {
/*    */   }
/*    */ 
/*    */   public void deinstall(JEditorPane paramJEditorPane)
/*    */   {
/*    */   }
/*    */ 
/*    */   public abstract String getContentType();
/*    */ 
/*    */   public abstract ViewFactory getViewFactory();
/*    */ 
/*    */   public abstract Action[] getActions();
/*    */ 
/*    */   public abstract Caret createCaret();
/*    */ 
/*    */   public abstract Document createDefaultDocument();
/*    */ 
/*    */   public abstract void read(InputStream paramInputStream, Document paramDocument, int paramInt)
/*    */     throws IOException, BadLocationException;
/*    */ 
/*    */   public abstract void write(OutputStream paramOutputStream, Document paramDocument, int paramInt1, int paramInt2)
/*    */     throws IOException, BadLocationException;
/*    */ 
/*    */   public abstract void read(Reader paramReader, Document paramDocument, int paramInt)
/*    */     throws IOException, BadLocationException;
/*    */ 
/*    */   public abstract void write(Writer paramWriter, Document paramDocument, int paramInt1, int paramInt2)
/*    */     throws IOException, BadLocationException;
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.EditorKit
 * JD-Core Version:    0.6.2
 */