/*     */ package javax.xml.xpath;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InvalidClassException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectInputStream.GetField;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.ObjectOutputStream.PutField;
/*     */ import java.io.ObjectStreamField;
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ 
/*     */ public class XPathException extends Exception
/*     */ {
/*  44 */   private static final ObjectStreamField[] serialPersistentFields = { new ObjectStreamField("cause", Throwable.class) };
/*     */   private static final long serialVersionUID = -1837080260374986980L;
/*     */ 
/*     */   public XPathException(String message)
/*     */   {
/*  68 */     super(message);
/*  69 */     if (message == null)
/*  70 */       throw new NullPointerException("message can't be null");
/*     */   }
/*     */ 
/*     */   public XPathException(Throwable cause)
/*     */   {
/*  86 */     super(cause);
/*  87 */     if (cause == null)
/*  88 */       throw new NullPointerException("cause can't be null");
/*     */   }
/*     */ 
/*     */   public Throwable getCause()
/*     */   {
/*  98 */     return super.getCause();
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream out)
/*     */     throws IOException
/*     */   {
/* 112 */     ObjectOutputStream.PutField fields = out.putFields();
/* 113 */     fields.put("cause", super.getCause());
/* 114 */     out.writeFields();
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream in)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 129 */     ObjectInputStream.GetField fields = in.readFields();
/* 130 */     Throwable scause = (Throwable)fields.get("cause", null);
/*     */ 
/* 132 */     if ((super.getCause() == null) && (scause != null))
/*     */       try {
/* 134 */         super.initCause(scause);
/*     */       } catch (IllegalStateException e) {
/* 136 */         throw new InvalidClassException("Inconsistent state: two causes");
/*     */       }
/*     */   }
/*     */ 
/*     */   public void printStackTrace(PrintStream s)
/*     */   {
/* 147 */     if (getCause() != null) {
/* 148 */       getCause().printStackTrace(s);
/* 149 */       s.println("--------------- linked to ------------------");
/*     */     }
/*     */ 
/* 152 */     super.printStackTrace(s);
/*     */   }
/*     */ 
/*     */   public void printStackTrace()
/*     */   {
/* 159 */     printStackTrace(System.err);
/*     */   }
/*     */ 
/*     */   public void printStackTrace(PrintWriter s)
/*     */   {
/* 169 */     if (getCause() != null) {
/* 170 */       getCause().printStackTrace(s);
/* 171 */       s.println("--------------- linked to ------------------");
/*     */     }
/*     */ 
/* 174 */     super.printStackTrace(s);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.xpath.XPathException
 * JD-Core Version:    0.6.2
 */