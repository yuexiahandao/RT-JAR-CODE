/*     */ package sun.applet;
/*     */ 
/*     */ import java.awt.Button;
/*     */ import java.awt.Choice;
/*     */ import java.awt.Event;
/*     */ import java.awt.Frame;
/*     */ import java.awt.GridLayout;
/*     */ import java.awt.Label;
/*     */ import java.awt.Panel;
/*     */ import java.awt.TextField;
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import java.util.Properties;
/*     */ import sun.security.action.GetBooleanAction;
/*     */ import sun.security.action.GetPropertyAction;
/*     */ 
/*     */ class AppletProps extends Frame
/*     */ {
/*     */   TextField proxyHost;
/*     */   TextField proxyPort;
/*     */   Choice accessMode;
/* 191 */   private static AppletMessageHandler amh = new AppletMessageHandler("appletprops");
/*     */ 
/*     */   AppletProps()
/*     */   {
/*  47 */     setTitle(amh.getMessage("title"));
/*  48 */     Panel localPanel = new Panel();
/*  49 */     localPanel.setLayout(new GridLayout(0, 2));
/*     */ 
/*  51 */     localPanel.add(new Label(amh.getMessage("label.http.server", "Http proxy server:")));
/*  52 */     localPanel.add(this.proxyHost = new TextField());
/*     */ 
/*  54 */     localPanel.add(new Label(amh.getMessage("label.http.proxy")));
/*  55 */     localPanel.add(this.proxyPort = new TextField());
/*     */ 
/*  57 */     localPanel.add(new Label(amh.getMessage("label.class")));
/*  58 */     localPanel.add(this.accessMode = new Choice());
/*  59 */     this.accessMode.addItem(amh.getMessage("choice.class.item.restricted"));
/*  60 */     this.accessMode.addItem(amh.getMessage("choice.class.item.unrestricted"));
/*     */ 
/*  62 */     add("Center", localPanel);
/*  63 */     localPanel = new Panel();
/*  64 */     localPanel.add(new Button(amh.getMessage("button.apply")));
/*  65 */     localPanel.add(new Button(amh.getMessage("button.reset")));
/*  66 */     localPanel.add(new Button(amh.getMessage("button.cancel")));
/*  67 */     add("South", localPanel);
/*  68 */     move(200, 150);
/*  69 */     pack();
/*  70 */     reset();
/*     */   }
/*     */ 
/*     */   void reset() {
/*  74 */     AppletSecurity localAppletSecurity = (AppletSecurity)System.getSecurityManager();
/*  75 */     if (localAppletSecurity != null) {
/*  76 */       localAppletSecurity.reset();
/*     */     }
/*  78 */     String str1 = (String)AccessController.doPrivileged(new GetPropertyAction("http.proxyHost"));
/*     */ 
/*  80 */     String str2 = (String)AccessController.doPrivileged(new GetPropertyAction("http.proxyPort"));
/*     */ 
/*  83 */     Boolean localBoolean = (Boolean)AccessController.doPrivileged(new GetBooleanAction("package.restrict.access.sun"));
/*     */ 
/*  86 */     boolean bool = localBoolean.booleanValue();
/*  87 */     if (bool)
/*  88 */       this.accessMode.select(amh.getMessage("choice.class.item.restricted"));
/*     */     else {
/*  90 */       this.accessMode.select(amh.getMessage("choice.class.item.unrestricted"));
/*     */     }
/*     */ 
/*  93 */     if (str1 != null) {
/*  94 */       this.proxyHost.setText(str1);
/*  95 */       this.proxyPort.setText(str2);
/*     */     } else {
/*  97 */       this.proxyHost.setText("");
/*  98 */       this.proxyPort.setText("");
/*     */     }
/*     */   }
/*     */ 
/*     */   void apply() {
/* 103 */     String str1 = this.proxyHost.getText().trim();
/* 104 */     String str2 = this.proxyPort.getText().trim();
/*     */ 
/* 107 */     final Properties localProperties = (Properties)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Object run() {
/* 110 */         return System.getProperties();
/*     */       }
/*     */     });
/* 114 */     if (str1.length() != 0)
/*     */     {
/* 119 */       int i = 0;
/*     */       try {
/* 121 */         i = Integer.parseInt(str2);
/*     */       } catch (NumberFormatException localNumberFormatException) {
/*     */       }
/* 124 */       if (i <= 0) {
/* 125 */         this.proxyPort.selectAll();
/* 126 */         this.proxyPort.requestFocus();
/* 127 */         new AppletPropsErrorDialog(this, amh.getMessage("title.invalidproxy"), amh.getMessage("label.invalidproxy"), amh.getMessage("button.ok")).show();
/*     */ 
/* 131 */         return;
/*     */       }
/*     */ 
/* 135 */       localProperties.put("http.proxyHost", str1);
/* 136 */       localProperties.put("http.proxyPort", str2);
/*     */     } else {
/* 138 */       localProperties.put("http.proxyHost", "");
/*     */     }
/*     */ 
/* 141 */     if (amh.getMessage("choice.class.item.restricted").equals(this.accessMode.getSelectedItem()))
/* 142 */       localProperties.put("package.restrict.access.sun", "true");
/*     */     else {
/* 144 */       localProperties.put("package.restrict.access.sun", "false");
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 149 */       reset();
/* 150 */       AccessController.doPrivileged(new PrivilegedExceptionAction() {
/*     */         public Object run() throws IOException {
/* 152 */           File localFile = Main.theUserPropertiesFile;
/* 153 */           FileOutputStream localFileOutputStream = new FileOutputStream(localFile);
/* 154 */           Properties localProperties = new Properties();
/* 155 */           for (int i = 0; i < Main.avDefaultUserProps.length; i++) {
/* 156 */             String str = Main.avDefaultUserProps[i][0];
/* 157 */             localProperties.setProperty(str, localProperties.getProperty(str));
/*     */           }
/* 159 */           localProperties.store(localFileOutputStream, AppletProps.amh.getMessage("prop.store"));
/* 160 */           localFileOutputStream.close();
/* 161 */           return null;
/*     */         }
/*     */       });
/* 164 */       hide();
/*     */     } catch (PrivilegedActionException localPrivilegedActionException) {
/* 166 */       System.out.println(amh.getMessage("apply.exception", localPrivilegedActionException.getException()));
/*     */ 
/* 169 */       localPrivilegedActionException.printStackTrace();
/* 170 */       reset();
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean action(Event paramEvent, Object paramObject) {
/* 175 */     if (amh.getMessage("button.apply").equals(paramObject)) {
/* 176 */       apply();
/* 177 */       return true;
/*     */     }
/* 179 */     if (amh.getMessage("button.reset").equals(paramObject)) {
/* 180 */       reset();
/* 181 */       return true;
/*     */     }
/* 183 */     if (amh.getMessage("button.cancel").equals(paramObject)) {
/* 184 */       reset();
/* 185 */       hide();
/* 186 */       return true;
/*     */     }
/* 188 */     return false;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.applet.AppletProps
 * JD-Core Version:    0.6.2
 */