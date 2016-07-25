/*     */ package javax.swing.colorchooser;
/*     */ 
/*     */ import java.awt.event.FocusEvent;
/*     */ import java.awt.event.FocusListener;
/*     */ import java.text.ParseException;
/*     */ import java.util.Locale;
/*     */ import javax.swing.JFormattedTextField;
/*     */ import javax.swing.JFormattedTextField.AbstractFormatter;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.text.AttributeSet;
/*     */ import javax.swing.text.BadLocationException;
/*     */ import javax.swing.text.DefaultFormatterFactory;
/*     */ import javax.swing.text.Document;
/*     */ import javax.swing.text.DocumentFilter;
/*     */ import javax.swing.text.DocumentFilter.FilterBypass;
/*     */ 
/*     */ final class ValueFormatter extends JFormattedTextField.AbstractFormatter
/*     */   implements FocusListener, Runnable
/*     */ {
/*  52 */   private final DocumentFilter filter = new DocumentFilter()
/*     */   {
/*     */     public void remove(DocumentFilter.FilterBypass paramAnonymousFilterBypass, int paramAnonymousInt1, int paramAnonymousInt2) throws BadLocationException {
/*  55 */       if (ValueFormatter.this.isValid(paramAnonymousFilterBypass.getDocument().getLength() - paramAnonymousInt2))
/*  56 */         paramAnonymousFilterBypass.remove(paramAnonymousInt1, paramAnonymousInt2);
/*     */     }
/*     */ 
/*     */     public void replace(DocumentFilter.FilterBypass paramAnonymousFilterBypass, int paramAnonymousInt1, int paramAnonymousInt2, String paramAnonymousString, AttributeSet paramAnonymousAttributeSet)
/*     */       throws BadLocationException
/*     */     {
/*  62 */       if ((ValueFormatter.this.isValid(paramAnonymousFilterBypass.getDocument().getLength() + paramAnonymousString.length() - paramAnonymousInt2)) && (ValueFormatter.this.isValid(paramAnonymousString)))
/*  63 */         paramAnonymousFilterBypass.replace(paramAnonymousInt1, paramAnonymousInt2, paramAnonymousString.toUpperCase(Locale.ENGLISH), paramAnonymousAttributeSet);
/*     */     }
/*     */ 
/*     */     public void insertString(DocumentFilter.FilterBypass paramAnonymousFilterBypass, int paramAnonymousInt, String paramAnonymousString, AttributeSet paramAnonymousAttributeSet)
/*     */       throws BadLocationException
/*     */     {
/*  69 */       if ((ValueFormatter.this.isValid(paramAnonymousFilterBypass.getDocument().getLength() + paramAnonymousString.length())) && (ValueFormatter.this.isValid(paramAnonymousString)))
/*  70 */         paramAnonymousFilterBypass.insertString(paramAnonymousInt, paramAnonymousString.toUpperCase(Locale.ENGLISH), paramAnonymousAttributeSet);
/*     */     }
/*  52 */   };
/*     */   private final int length;
/*     */   private final int radix;
/*     */   private JFormattedTextField text;
/*     */ 
/*     */   static void init(int paramInt, boolean paramBoolean, JFormattedTextField paramJFormattedTextField)
/*     */   {
/*  44 */     ValueFormatter localValueFormatter = new ValueFormatter(paramInt, paramBoolean);
/*  45 */     paramJFormattedTextField.setColumns(paramInt);
/*  46 */     paramJFormattedTextField.setFormatterFactory(new DefaultFormatterFactory(localValueFormatter));
/*  47 */     paramJFormattedTextField.setHorizontalAlignment(4);
/*  48 */     paramJFormattedTextField.setMinimumSize(paramJFormattedTextField.getPreferredSize());
/*  49 */     paramJFormattedTextField.addFocusListener(localValueFormatter);
/*     */   }
/*     */ 
/*     */   ValueFormatter(int paramInt, boolean paramBoolean)
/*     */   {
/*  81 */     this.length = paramInt;
/*  82 */     this.radix = (paramBoolean ? 16 : 10);
/*     */   }
/*     */ 
/*     */   public Object stringToValue(String paramString) throws ParseException
/*     */   {
/*     */     try {
/*  88 */       return Integer.valueOf(paramString, this.radix);
/*     */     }
/*     */     catch (NumberFormatException localNumberFormatException) {
/*  91 */       ParseException localParseException = new ParseException("illegal format", 0);
/*  92 */       localParseException.initCause(localNumberFormatException);
/*  93 */       throw localParseException;
/*     */     }
/*     */   }
/*     */ 
/*     */   public String valueToString(Object paramObject) throws ParseException
/*     */   {
/*  99 */     if ((paramObject instanceof Integer)) {
/* 100 */       if (this.radix == 10) {
/* 101 */         return paramObject.toString();
/*     */       }
/* 103 */       int i = ((Integer)paramObject).intValue();
/* 104 */       int j = this.length;
/* 105 */       char[] arrayOfChar = new char[j];
/* 106 */       while (0 < j--) {
/* 107 */         arrayOfChar[j] = Character.forDigit(i & 0xF, this.radix);
/* 108 */         i >>= 4;
/*     */       }
/* 110 */       return new String(arrayOfChar).toUpperCase(Locale.ENGLISH);
/*     */     }
/* 112 */     throw new ParseException("illegal object", 0);
/*     */   }
/*     */ 
/*     */   protected DocumentFilter getDocumentFilter()
/*     */   {
/* 117 */     return this.filter;
/*     */   }
/*     */ 
/*     */   public void focusGained(FocusEvent paramFocusEvent) {
/* 121 */     Object localObject = paramFocusEvent.getSource();
/* 122 */     if ((localObject instanceof JFormattedTextField)) {
/* 123 */       this.text = ((JFormattedTextField)localObject);
/* 124 */       SwingUtilities.invokeLater(this);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void focusLost(FocusEvent paramFocusEvent) {
/*     */   }
/*     */ 
/*     */   public void run() {
/* 132 */     if (this.text != null)
/* 133 */       this.text.selectAll();
/*     */   }
/*     */ 
/*     */   private boolean isValid(int paramInt)
/*     */   {
/* 138 */     return (0 <= paramInt) && (paramInt <= this.length);
/*     */   }
/*     */ 
/*     */   private boolean isValid(String paramString) {
/* 142 */     int i = paramString.length();
/* 143 */     for (int j = 0; j < i; j++) {
/* 144 */       char c = paramString.charAt(j);
/* 145 */       if (Character.digit(c, this.radix) < 0) {
/* 146 */         return false;
/*     */       }
/*     */     }
/* 149 */     return true;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.colorchooser.ValueFormatter
 * JD-Core Version:    0.6.2
 */