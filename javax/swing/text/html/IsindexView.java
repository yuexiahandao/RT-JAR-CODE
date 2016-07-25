/*     */ package javax.swing.text.html;
/*     */ 
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Component;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.io.IOException;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.net.URLEncoder;
/*     */ import javax.swing.JEditorPane;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JTextField;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.text.AttributeSet;
/*     */ import javax.swing.text.ComponentView;
/*     */ import javax.swing.text.Element;
/*     */ 
/*     */ class IsindexView extends ComponentView
/*     */   implements ActionListener
/*     */ {
/*     */   JTextField textField;
/*     */ 
/*     */   public IsindexView(Element paramElement)
/*     */   {
/*  52 */     super(paramElement);
/*     */   }
/*     */ 
/*     */   public Component createComponent()
/*     */   {
/*  62 */     AttributeSet localAttributeSet = getElement().getAttributes();
/*     */ 
/*  64 */     JPanel localJPanel = new JPanel(new BorderLayout());
/*  65 */     localJPanel.setBackground(null);
/*     */ 
/*  67 */     String str = (String)localAttributeSet.getAttribute(HTML.Attribute.PROMPT);
/*  68 */     if (str == null) {
/*  69 */       str = UIManager.getString("IsindexView.prompt");
/*     */     }
/*  71 */     JLabel localJLabel = new JLabel(str);
/*     */ 
/*  73 */     this.textField = new JTextField();
/*  74 */     this.textField.addActionListener(this);
/*  75 */     localJPanel.add(localJLabel, "West");
/*  76 */     localJPanel.add(this.textField, "Center");
/*  77 */     localJPanel.setAlignmentY(1.0F);
/*  78 */     localJPanel.setOpaque(false);
/*  79 */     return localJPanel;
/*     */   }
/*     */ 
/*     */   public void actionPerformed(ActionEvent paramActionEvent)
/*     */   {
/*  93 */     String str1 = this.textField.getText();
/*  94 */     if (str1 != null) {
/*  95 */       str1 = URLEncoder.encode(str1);
/*     */     }
/*     */ 
/*  99 */     AttributeSet localAttributeSet = getElement().getAttributes();
/* 100 */     HTMLDocument localHTMLDocument = (HTMLDocument)getElement().getDocument();
/*     */ 
/* 102 */     String str2 = (String)localAttributeSet.getAttribute(HTML.Attribute.ACTION);
/* 103 */     if (str2 == null)
/* 104 */       str2 = localHTMLDocument.getBase().toString();
/*     */     try
/*     */     {
/* 107 */       URL localURL = new URL(str2 + "?" + str1);
/* 108 */       JEditorPane localJEditorPane = (JEditorPane)getContainer();
/* 109 */       localJEditorPane.setPage(localURL);
/*     */     }
/*     */     catch (MalformedURLException localMalformedURLException)
/*     */     {
/*     */     }
/*     */     catch (IOException localIOException)
/*     */     {
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.html.IsindexView
 * JD-Core Version:    0.6.2
 */