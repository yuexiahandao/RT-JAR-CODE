/*    */ package sun.applet;
/*    */ 
/*    */ import java.awt.Button;
/*    */ import java.awt.Frame;
/*    */ import java.awt.Panel;
/*    */ import java.awt.TextArea;
/*    */ import java.awt.event.ActionEvent;
/*    */ import java.awt.event.ActionListener;
/*    */ import java.awt.event.WindowAdapter;
/*    */ import java.awt.event.WindowEvent;
/*    */ 
/*    */ class TextFrame extends Frame
/*    */ {
/* 88 */   private static AppletMessageHandler amh = new AppletMessageHandler("textframe");
/*    */ 
/*    */   TextFrame(int paramInt1, int paramInt2, String paramString1, String paramString2)
/*    */   {
/* 56 */     setTitle(paramString1);
/* 57 */     TextArea localTextArea = new TextArea(20, 60);
/* 58 */     localTextArea.setText(paramString2);
/* 59 */     localTextArea.setEditable(false);
/*    */ 
/* 61 */     add("Center", localTextArea);
/*    */ 
/* 63 */     Panel localPanel = new Panel();
/* 64 */     add("South", localPanel);
/* 65 */     Button localButton = new Button(amh.getMessage("button.dismiss", "Dismiss"));
/* 66 */     localPanel.add(localButton);
/*    */ 
/* 73 */     localButton.addActionListener(new ActionListener()
/*    */     {
/*    */       public void actionPerformed(ActionEvent paramAnonymousActionEvent)
/*    */       {
/* 70 */         TextFrame.this.dispose();
/*    */       }
/*    */     });
/* 75 */     pack();
/* 76 */     move(paramInt1, paramInt2);
/* 77 */     setVisible(true);
/*    */ 
/* 79 */     WindowAdapter local1 = new WindowAdapter()
/*    */     {
/*    */       public void windowClosing(WindowEvent paramAnonymousWindowEvent) {
/* 82 */         TextFrame.this.dispose();
/*    */       }
/*    */     };
/* 86 */     addWindowListener(local1);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.applet.TextFrame
 * JD-Core Version:    0.6.2
 */