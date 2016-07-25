/*      */ package sun.applet;
/*      */ 
/*      */ import java.applet.Applet;
/*      */ import java.applet.AppletContext;
/*      */ import java.applet.AudioClip;
/*      */ import java.awt.Dimension;
/*      */ import java.awt.FileDialog;
/*      */ import java.awt.Frame;
/*      */ import java.awt.Graphics;
/*      */ import java.awt.Graphics2D;
/*      */ import java.awt.Image;
/*      */ import java.awt.Insets;
/*      */ import java.awt.Label;
/*      */ import java.awt.Menu;
/*      */ import java.awt.MenuBar;
/*      */ import java.awt.MenuItem;
/*      */ import java.awt.Point;
/*      */ import java.awt.Toolkit;
/*      */ import java.awt.event.ActionEvent;
/*      */ import java.awt.event.ActionListener;
/*      */ import java.awt.event.WindowAdapter;
/*      */ import java.awt.event.WindowEvent;
/*      */ import java.awt.print.PageFormat;
/*      */ import java.awt.print.Printable;
/*      */ import java.awt.print.PrinterException;
/*      */ import java.awt.print.PrinterJob;
/*      */ import java.io.BufferedOutputStream;
/*      */ import java.io.BufferedReader;
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.io.File;
/*      */ import java.io.FileOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.InputStreamReader;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.PrintStream;
/*      */ import java.io.Reader;
/*      */ import java.net.SocketPermission;
/*      */ import java.net.URL;
/*      */ import java.net.URLConnection;
/*      */ import java.security.AccessController;
/*      */ import java.security.Permission;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.util.Enumeration;
/*      */ import java.util.HashMap;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Iterator;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.Vector;
/*      */ import javax.print.attribute.HashPrintRequestAttributeSet;
/*      */ import sun.awt.AppContext;
/*      */ import sun.awt.SunToolkit;
/*      */ import sun.misc.Ref;
/*      */ 
/*      */ public class AppletViewer extends Frame
/*      */   implements AppletContext, Printable
/*      */ {
/*  125 */   private static String defaultSaveFile = "Applet.ser";
/*      */   AppletViewerPanel panel;
/*      */   Label label;
/*      */   PrintStream statusMsgStream;
/*      */   AppletViewerFactory factory;
/*  368 */   private static Map audioClips = new HashMap();
/*      */ 
/*  384 */   private static Map imageRefs = new HashMap();
/*      */ 
/*  419 */   static Vector appletPanels = new Vector();
/*      */ 
/*  509 */   static Hashtable systemParam = new Hashtable();
/*      */   static AppletProps props;
/*      */   static int c;
/* 1051 */   private static int x = 0;
/* 1052 */   private static int y = 0;
/*      */   private static final int XDELTA = 30;
/*      */   private static final int YDELTA = 30;
/* 1056 */   static String encoding = null;
/*      */ 
/* 1260 */   private static AppletMessageHandler amh = new AppletMessageHandler("appletviewer");
/*      */ 
/*      */   public AppletViewer(int paramInt1, int paramInt2, URL paramURL, Hashtable paramHashtable, PrintStream paramPrintStream, AppletViewerFactory paramAppletViewerFactory)
/*      */   {
/*  160 */     this.factory = paramAppletViewerFactory;
/*  161 */     this.statusMsgStream = paramPrintStream;
/*  162 */     setTitle(amh.getMessage("tool.title", paramHashtable.get("code")));
/*      */ 
/*  164 */     MenuBar localMenuBar = paramAppletViewerFactory.getBaseMenuBar();
/*      */ 
/*  166 */     Menu localMenu = new Menu(amh.getMessage("menu.applet"));
/*      */ 
/*  168 */     addMenuItem(localMenu, "menuitem.restart");
/*  169 */     addMenuItem(localMenu, "menuitem.reload");
/*  170 */     addMenuItem(localMenu, "menuitem.stop");
/*  171 */     addMenuItem(localMenu, "menuitem.save");
/*  172 */     addMenuItem(localMenu, "menuitem.start");
/*  173 */     addMenuItem(localMenu, "menuitem.clone");
/*  174 */     localMenu.add(new MenuItem("-"));
/*  175 */     addMenuItem(localMenu, "menuitem.tag");
/*  176 */     addMenuItem(localMenu, "menuitem.info");
/*  177 */     addMenuItem(localMenu, "menuitem.edit").disable();
/*  178 */     addMenuItem(localMenu, "menuitem.encoding");
/*  179 */     localMenu.add(new MenuItem("-"));
/*  180 */     addMenuItem(localMenu, "menuitem.print");
/*  181 */     localMenu.add(new MenuItem("-"));
/*  182 */     addMenuItem(localMenu, "menuitem.props");
/*  183 */     localMenu.add(new MenuItem("-"));
/*  184 */     addMenuItem(localMenu, "menuitem.close");
/*  185 */     if (paramAppletViewerFactory.isStandalone()) {
/*  186 */       addMenuItem(localMenu, "menuitem.quit");
/*      */     }
/*      */ 
/*  189 */     localMenuBar.add(localMenu);
/*      */ 
/*  191 */     setMenuBar(localMenuBar);
/*      */ 
/*  193 */     add("Center", this.panel = new AppletViewerPanel(paramURL, paramHashtable));
/*  194 */     add("South", this.label = new Label(amh.getMessage("label.hello")));
/*  195 */     this.panel.init();
/*  196 */     appletPanels.addElement(this.panel);
/*      */ 
/*  198 */     pack();
/*  199 */     move(paramInt1, paramInt2);
/*  200 */     setVisible(true);
/*      */ 
/*  202 */     WindowAdapter local1 = new WindowAdapter()
/*      */     {
/*      */       public void windowClosing(WindowEvent paramAnonymousWindowEvent) {
/*  205 */         AppletViewer.this.appletClose();
/*      */       }
/*      */ 
/*      */       public void windowIconified(WindowEvent paramAnonymousWindowEvent) {
/*  209 */         AppletViewer.this.appletStop();
/*      */       }
/*      */ 
/*      */       public void windowDeiconified(WindowEvent paramAnonymousWindowEvent) {
/*  213 */         AppletViewer.this.appletStart();
/*      */       }
/*      */     };
/*  266 */     addWindowListener(local1);
/*  267 */     this.panel.addAppletListener(new AppletListener()
/*      */     {
/*      */       final Frame frame;
/*      */ 
/*      */       public void appletStateChanged(AppletEvent paramAnonymousAppletEvent)
/*      */       {
/*  228 */         AppletPanel localAppletPanel = (AppletPanel)paramAnonymousAppletEvent.getSource();
/*      */ 
/*  230 */         switch (paramAnonymousAppletEvent.getID()) {
/*      */         case 51234:
/*  232 */           if (localAppletPanel != null) {
/*  233 */             AppletViewer.this.resize(AppletViewer.this.preferredSize());
/*  234 */             AppletViewer.this.validate(); } break;
/*      */         case 51236:
/*  239 */           Applet localApplet = localAppletPanel.getApplet();
/*      */ 
/*  255 */           if (localApplet != null)
/*  256 */             AppletPanel.changeFrameAppContext(this.frame, SunToolkit.targetToAppContext(localApplet));
/*      */           else {
/*  258 */             AppletPanel.changeFrameAppContext(this.frame, AppContext.getAppContext());
/*      */           }
/*  260 */           break;
/*      */         }
/*      */       }
/*      */     });
/*  270 */     showStatus(amh.getMessage("status.start"));
/*  271 */     initEventQueue();
/*      */   }
/*      */ 
/*      */   public MenuItem addMenuItem(Menu paramMenu, String paramString)
/*      */   {
/*  276 */     MenuItem localMenuItem = new MenuItem(amh.getMessage(paramString));
/*  277 */     localMenuItem.addActionListener(new UserActionListener(null));
/*  278 */     return paramMenu.add(localMenuItem);
/*      */   }
/*      */ 
/*      */   private void initEventQueue()
/*      */   {
/*  289 */     String str = System.getProperty("appletviewer.send.event");
/*      */ 
/*  291 */     if (str == null)
/*      */     {
/*  293 */       this.panel.sendEvent(1);
/*  294 */       this.panel.sendEvent(2);
/*  295 */       this.panel.sendEvent(3);
/*      */     }
/*      */     else
/*      */     {
/*  303 */       String[] arrayOfString = splitSeparator(",", str);
/*      */ 
/*  305 */       int i = 0; if (i < arrayOfString.length) {
/*  306 */         System.out.println("Adding event to queue: " + arrayOfString[i]);
/*  307 */         if (arrayOfString[i].equals("dispose"))
/*  308 */           this.panel.sendEvent(0);
/*  309 */         else if (arrayOfString[i].equals("load"))
/*  310 */           this.panel.sendEvent(1);
/*  311 */         else if (arrayOfString[i].equals("init"))
/*  312 */           this.panel.sendEvent(2);
/*  313 */         else if (arrayOfString[i].equals("start"))
/*  314 */           this.panel.sendEvent(3);
/*  315 */         else if (arrayOfString[i].equals("stop"))
/*  316 */           this.panel.sendEvent(4);
/*  317 */         else if (arrayOfString[i].equals("destroy"))
/*  318 */           this.panel.sendEvent(5);
/*  319 */         else if (arrayOfString[i].equals("quit"))
/*  320 */           this.panel.sendEvent(6);
/*  321 */         else if (arrayOfString[i].equals("error")) {
/*  322 */           this.panel.sendEvent(7);
/*      */         }
/*      */         else
/*  325 */           System.out.println("Unrecognized event name: " + arrayOfString[i]);
/*  305 */         i++;
/*      */       }
/*      */ 
/*  328 */       while (!this.panel.emptyEventQueue());
/*  329 */       appletSystemExit();
/*      */     }
/*      */   }
/*      */ 
/*      */   private String[] splitSeparator(String paramString1, String paramString2)
/*      */   {
/*  348 */     Vector localVector = new Vector();
/*  349 */     int i = 0;
/*  350 */     int j = 0;
/*      */ 
/*  352 */     while ((j = paramString2.indexOf(paramString1, i)) != -1) {
/*  353 */       localVector.addElement(paramString2.substring(i, j));
/*  354 */       i = j + 1;
/*      */     }
/*      */ 
/*  357 */     localVector.addElement(paramString2.substring(i));
/*      */ 
/*  359 */     String[] arrayOfString = new String[localVector.size()];
/*  360 */     localVector.copyInto(arrayOfString);
/*  361 */     return arrayOfString;
/*      */   }
/*      */ 
/*      */   public AudioClip getAudioClip(URL paramURL)
/*      */   {
/*  374 */     checkConnect(paramURL);
/*  375 */     synchronized (audioClips) {
/*  376 */       Object localObject1 = (AudioClip)audioClips.get(paramURL);
/*  377 */       if (localObject1 == null) {
/*  378 */         audioClips.put(paramURL, localObject1 = new AppletAudioClip(paramURL));
/*      */       }
/*  380 */       return localObject1;
/*      */     }
/*      */   }
/*      */ 
/*      */   public Image getImage(URL paramURL)
/*      */   {
/*  390 */     return getCachedImage(paramURL);
/*      */   }
/*      */ 
/*      */   static Image getCachedImage(URL paramURL)
/*      */   {
/*  395 */     return (Image)getCachedImageRef(paramURL).get();
/*      */   }
/*      */ 
/*      */   static Ref getCachedImageRef(URL paramURL)
/*      */   {
/*  402 */     synchronized (imageRefs) {
/*  403 */       AppletImageRef localAppletImageRef = (AppletImageRef)imageRefs.get(paramURL);
/*  404 */       if (localAppletImageRef == null) {
/*  405 */         localAppletImageRef = new AppletImageRef(paramURL);
/*  406 */         imageRefs.put(paramURL, localAppletImageRef);
/*      */       }
/*  408 */       return localAppletImageRef;
/*      */     }
/*      */   }
/*      */ 
/*      */   static void flushImageCache()
/*      */   {
/*  416 */     imageRefs.clear();
/*      */   }
/*      */ 
/*      */   public Applet getApplet(String paramString)
/*      */   {
/*  425 */     AppletSecurity localAppletSecurity = (AppletSecurity)System.getSecurityManager();
/*  426 */     paramString = paramString.toLowerCase();
/*  427 */     SocketPermission localSocketPermission1 = new SocketPermission(this.panel.getCodeBase().getHost(), "connect");
/*      */ 
/*  429 */     for (Enumeration localEnumeration = appletPanels.elements(); localEnumeration.hasMoreElements(); ) {
/*  430 */       AppletPanel localAppletPanel = (AppletPanel)localEnumeration.nextElement();
/*  431 */       String str = localAppletPanel.getParameter("name");
/*  432 */       if (str != null) {
/*  433 */         str = str.toLowerCase();
/*      */       }
/*  435 */       if ((paramString.equals(str)) && (localAppletPanel.getDocumentBase().equals(this.panel.getDocumentBase())))
/*      */       {
/*  438 */         SocketPermission localSocketPermission2 = new SocketPermission(localAppletPanel.getCodeBase().getHost(), "connect");
/*      */ 
/*  441 */         if (localSocketPermission1.implies(localSocketPermission2)) {
/*  442 */           return localAppletPanel.applet;
/*      */         }
/*      */       }
/*      */     }
/*  446 */     return null;
/*      */   }
/*      */ 
/*      */   public Enumeration getApplets()
/*      */   {
/*  454 */     AppletSecurity localAppletSecurity = (AppletSecurity)System.getSecurityManager();
/*  455 */     Vector localVector = new Vector();
/*  456 */     SocketPermission localSocketPermission1 = new SocketPermission(this.panel.getCodeBase().getHost(), "connect");
/*      */ 
/*  459 */     for (Enumeration localEnumeration = appletPanels.elements(); localEnumeration.hasMoreElements(); ) {
/*  460 */       AppletPanel localAppletPanel = (AppletPanel)localEnumeration.nextElement();
/*  461 */       if (localAppletPanel.getDocumentBase().equals(this.panel.getDocumentBase()))
/*      */       {
/*  463 */         SocketPermission localSocketPermission2 = new SocketPermission(localAppletPanel.getCodeBase().getHost(), "connect");
/*      */ 
/*  465 */         if (localSocketPermission1.implies(localSocketPermission2)) {
/*  466 */           localVector.addElement(localAppletPanel.applet);
/*      */         }
/*      */       }
/*      */     }
/*  470 */     return localVector.elements();
/*      */   }
/*      */ 
/*      */   public void showDocument(URL paramURL)
/*      */   {
/*      */   }
/*      */ 
/*      */   public void showDocument(URL paramURL, String paramString)
/*      */   {
/*      */   }
/*      */ 
/*      */   public void showStatus(String paramString)
/*      */   {
/*  489 */     this.label.setText(paramString);
/*      */   }
/*      */ 
/*      */   public void setStream(String paramString, InputStream paramInputStream) throws IOException
/*      */   {
/*      */   }
/*      */ 
/*      */   public InputStream getStream(String paramString)
/*      */   {
/*  498 */     return null;
/*      */   }
/*      */ 
/*      */   public Iterator getStreamKeys()
/*      */   {
/*  503 */     return null;
/*      */   }
/*      */ 
/*      */   public static void printTag(PrintStream paramPrintStream, Hashtable paramHashtable)
/*      */   {
/*  526 */     paramPrintStream.print("<applet");
/*      */ 
/*  528 */     String str1 = (String)paramHashtable.get("codebase");
/*  529 */     if (str1 != null) {
/*  530 */       paramPrintStream.print(" codebase=\"" + str1 + "\"");
/*      */     }
/*      */ 
/*  533 */     str1 = (String)paramHashtable.get("code");
/*  534 */     if (str1 == null) {
/*  535 */       str1 = "applet.class";
/*      */     }
/*  537 */     paramPrintStream.print(" code=\"" + str1 + "\"");
/*  538 */     str1 = (String)paramHashtable.get("width");
/*  539 */     if (str1 == null) {
/*  540 */       str1 = "150";
/*      */     }
/*  542 */     paramPrintStream.print(" width=" + str1);
/*      */ 
/*  544 */     str1 = (String)paramHashtable.get("height");
/*  545 */     if (str1 == null) {
/*  546 */       str1 = "100";
/*      */     }
/*  548 */     paramPrintStream.print(" height=" + str1);
/*      */ 
/*  550 */     str1 = (String)paramHashtable.get("name");
/*  551 */     if (str1 != null) {
/*  552 */       paramPrintStream.print(" name=\"" + str1 + "\"");
/*      */     }
/*  554 */     paramPrintStream.println(">");
/*      */ 
/*  557 */     int i = paramHashtable.size();
/*  558 */     String[] arrayOfString = new String[i];
/*  559 */     i = 0;
/*  560 */     for (Enumeration localEnumeration = paramHashtable.keys(); localEnumeration.hasMoreElements(); ) {
/*  561 */       str2 = (String)localEnumeration.nextElement();
/*  562 */       int k = 0;
/*  563 */       while ((k < i) && 
/*  564 */         (arrayOfString[k].compareTo(str2) < 0)) {
/*  563 */         k++;
/*      */       }
/*      */ 
/*  568 */       System.arraycopy(arrayOfString, k, arrayOfString, k + 1, i - k);
/*  569 */       arrayOfString[k] = str2;
/*  570 */       i++;
/*      */     }
/*      */     String str2;
/*  573 */     for (int j = 0; j < i; j++) {
/*  574 */       str2 = arrayOfString[j];
/*  575 */       if (systemParam.get(str2) == null) {
/*  576 */         paramPrintStream.println("<param name=" + str2 + " value=\"" + paramHashtable.get(str2) + "\">");
/*      */       }
/*      */     }
/*      */ 
/*  580 */     paramPrintStream.println("</applet>");
/*      */   }
/*      */ 
/*      */   public void updateAtts()
/*      */   {
/*  587 */     Dimension localDimension = this.panel.size();
/*  588 */     Insets localInsets = this.panel.insets();
/*  589 */     this.panel.atts.put("width", Integer.toString(localDimension.width - (localInsets.left + localInsets.right)));
/*      */ 
/*  591 */     this.panel.atts.put("height", Integer.toString(localDimension.height - (localInsets.top + localInsets.bottom)));
/*      */   }
/*      */ 
/*      */   void appletRestart()
/*      */   {
/*  599 */     this.panel.sendEvent(4);
/*  600 */     this.panel.sendEvent(5);
/*  601 */     this.panel.sendEvent(2);
/*  602 */     this.panel.sendEvent(3);
/*      */   }
/*      */ 
/*      */   void appletReload()
/*      */   {
/*  609 */     this.panel.sendEvent(4);
/*  610 */     this.panel.sendEvent(5);
/*  611 */     this.panel.sendEvent(0);
/*      */ 
/*  618 */     AppletPanel.flushClassLoader(this.panel.getClassLoaderCacheKey());
/*      */     try
/*      */     {
/*  625 */       this.panel.joinAppletThread();
/*  626 */       this.panel.release();
/*      */     } catch (InterruptedException localInterruptedException) {
/*  628 */       return;
/*      */     }
/*      */ 
/*  631 */     this.panel.createAppletThread();
/*  632 */     this.panel.sendEvent(1);
/*  633 */     this.panel.sendEvent(2);
/*  634 */     this.panel.sendEvent(3);
/*      */   }
/*      */ 
/*      */   void appletSave()
/*      */   {
/*  641 */     AccessController.doPrivileged(new PrivilegedAction()
/*      */     {
/*      */       public Object run()
/*      */       {
/*  654 */         AppletViewer.this.panel.sendEvent(4);
/*  655 */         FileDialog localFileDialog = new FileDialog(AppletViewer.this, AppletViewer.amh.getMessage("appletsave.filedialogtitle"), 1);
/*      */ 
/*  659 */         localFileDialog.setDirectory(System.getProperty("user.dir"));
/*  660 */         localFileDialog.setFile(AppletViewer.defaultSaveFile);
/*  661 */         localFileDialog.show();
/*  662 */         String str1 = localFileDialog.getFile();
/*  663 */         if (str1 == null)
/*      */         {
/*  665 */           AppletViewer.this.panel.sendEvent(3);
/*  666 */           return null;
/*      */         }
/*  668 */         String str2 = localFileDialog.getDirectory();
/*  669 */         File localFile = new File(str2, str1);
/*      */         try
/*      */         {
/*  672 */           BufferedOutputStream localBufferedOutputStream = new BufferedOutputStream(new FileOutputStream(localFile));
/*  673 */           ObjectOutputStream localObjectOutputStream = new ObjectOutputStream(localBufferedOutputStream);
/*  674 */           AppletViewer.this.showStatus(AppletViewer.amh.getMessage("appletsave.err1", AppletViewer.this.panel.applet.toString(), localFile.toString()));
/*      */ 
/*  676 */           localObjectOutputStream.writeObject(AppletViewer.this.panel.applet);
/*      */         } catch (IOException localIOException) {
/*  678 */           System.err.println(AppletViewer.amh.getMessage("appletsave.err2", localIOException));
/*      */         } finally {
/*  680 */           AppletViewer.this.panel.sendEvent(3);
/*      */         }
/*  682 */         return null;
/*      */       }
/*      */     });
/*      */   }
/*      */ 
/*      */   void appletClone()
/*      */   {
/*  691 */     Point localPoint = location();
/*  692 */     updateAtts();
/*  693 */     this.factory.createAppletViewer(localPoint.x + 30, localPoint.y + 30, this.panel.documentURL, (Hashtable)this.panel.atts.clone());
/*      */   }
/*      */ 
/*      */   void appletTag()
/*      */   {
/*  701 */     ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
/*  702 */     updateAtts();
/*  703 */     printTag(new PrintStream(localByteArrayOutputStream), this.panel.atts);
/*  704 */     showStatus(amh.getMessage("applettag"));
/*      */ 
/*  706 */     Point localPoint = location();
/*  707 */     new TextFrame(localPoint.x + 30, localPoint.y + 30, amh.getMessage("applettag.textframe"), localByteArrayOutputStream.toString());
/*      */   }
/*      */ 
/*      */   void appletInfo()
/*      */   {
/*  714 */     String str = this.panel.applet.getAppletInfo();
/*  715 */     if (str == null) {
/*  716 */       str = amh.getMessage("appletinfo.applet");
/*      */     }
/*  718 */     str = str + "\n\n";
/*      */ 
/*  720 */     String[][] arrayOfString = this.panel.applet.getParameterInfo();
/*  721 */     if (arrayOfString != null) {
/*  722 */       for (int i = 0; i < arrayOfString.length; i++)
/*  723 */         str = str + arrayOfString[i][0] + " -- " + arrayOfString[i][1] + " -- " + arrayOfString[i][2] + "\n";
/*      */     }
/*      */     else {
/*  726 */       str = str + amh.getMessage("appletinfo.param");
/*      */     }
/*      */ 
/*  729 */     Point localPoint = location();
/*  730 */     new TextFrame(localPoint.x + 30, localPoint.y + 30, amh.getMessage("appletinfo.textframe"), str);
/*      */   }
/*      */ 
/*      */   void appletCharacterEncoding()
/*      */   {
/*  738 */     showStatus(amh.getMessage("appletencoding", encoding));
/*      */   }
/*      */ 
/*      */   void appletEdit()
/*      */   {
/*      */   }
/*      */ 
/*      */   void appletPrint()
/*      */   {
/*  751 */     PrinterJob localPrinterJob = PrinterJob.getPrinterJob();
/*      */ 
/*  753 */     if (localPrinterJob != null) {
/*  754 */       HashPrintRequestAttributeSet localHashPrintRequestAttributeSet = new HashPrintRequestAttributeSet();
/*  755 */       if (localPrinterJob.printDialog(localHashPrintRequestAttributeSet)) {
/*  756 */         localPrinterJob.setPrintable(this);
/*      */         try {
/*  758 */           localPrinterJob.print(localHashPrintRequestAttributeSet);
/*  759 */           this.statusMsgStream.println(amh.getMessage("appletprint.finish"));
/*      */         } catch (PrinterException localPrinterException) {
/*  761 */           this.statusMsgStream.println(amh.getMessage("appletprint.fail"));
/*      */         }
/*      */       } else {
/*  764 */         this.statusMsgStream.println(amh.getMessage("appletprint.cancel"));
/*      */       }
/*      */     } else {
/*  767 */       this.statusMsgStream.println(amh.getMessage("appletprint.fail"));
/*      */     }
/*      */   }
/*      */ 
/*      */   public int print(Graphics paramGraphics, PageFormat paramPageFormat, int paramInt) {
/*  772 */     if (paramInt > 0) {
/*  773 */       return 1;
/*      */     }
/*  775 */     Graphics2D localGraphics2D = (Graphics2D)paramGraphics;
/*  776 */     localGraphics2D.translate(paramPageFormat.getImageableX(), paramPageFormat.getImageableY());
/*  777 */     this.panel.applet.printAll(paramGraphics);
/*  778 */     return 0;
/*      */   }
/*      */ 
/*      */   public static synchronized void networkProperties()
/*      */   {
/*  787 */     if (props == null) {
/*  788 */       props = new AppletProps();
/*      */     }
/*  790 */     props.addNotify();
/*  791 */     props.setVisible(true);
/*      */   }
/*      */ 
/*      */   void appletStart()
/*      */   {
/*  798 */     this.panel.sendEvent(3);
/*      */   }
/*      */ 
/*      */   void appletStop()
/*      */   {
/*  805 */     this.panel.sendEvent(4);
/*      */   }
/*      */ 
/*      */   private void appletShutdown(AppletPanel paramAppletPanel)
/*      */   {
/*  813 */     paramAppletPanel.sendEvent(4);
/*  814 */     paramAppletPanel.sendEvent(5);
/*  815 */     paramAppletPanel.sendEvent(0);
/*  816 */     paramAppletPanel.sendEvent(6);
/*      */   }
/*      */ 
/*      */   void appletClose()
/*      */   {
/*  831 */     final AppletViewerPanel localAppletViewerPanel = this.panel;
/*      */ 
/*  833 */     new Thread(new Runnable()
/*      */     {
/*      */       public void run()
/*      */       {
/*  837 */         AppletViewer.this.appletShutdown(localAppletViewerPanel);
/*  838 */         AppletViewer.appletPanels.removeElement(localAppletViewerPanel);
/*  839 */         AppletViewer.this.dispose();
/*      */ 
/*  841 */         if (AppletViewer.countApplets() == 0)
/*  842 */           AppletViewer.this.appletSystemExit();
/*      */       }
/*      */     }).start();
/*      */   }
/*      */ 
/*      */   private void appletSystemExit()
/*      */   {
/*  853 */     if (this.factory.isStandalone())
/*  854 */       System.exit(0);
/*      */   }
/*      */ 
/*      */   protected void appletQuit()
/*      */   {
/*  868 */     new Thread(new Runnable()
/*      */     {
/*      */       public void run()
/*      */       {
/*  872 */         for (Enumeration localEnumeration = AppletViewer.appletPanels.elements(); localEnumeration.hasMoreElements(); ) {
/*  873 */           AppletPanel localAppletPanel = (AppletPanel)localEnumeration.nextElement();
/*  874 */           AppletViewer.this.appletShutdown(localAppletPanel);
/*      */         }
/*  876 */         AppletViewer.this.appletSystemExit();
/*      */       }
/*      */     }).start();
/*      */   }
/*      */ 
/*      */   public void processUserAction(ActionEvent paramActionEvent)
/*      */   {
/*  886 */     String str = ((MenuItem)paramActionEvent.getSource()).getLabel();
/*      */ 
/*  888 */     if (amh.getMessage("menuitem.restart").equals(str)) {
/*  889 */       appletRestart();
/*  890 */       return;
/*      */     }
/*      */ 
/*  893 */     if (amh.getMessage("menuitem.reload").equals(str)) {
/*  894 */       appletReload();
/*  895 */       return;
/*      */     }
/*      */ 
/*  898 */     if (amh.getMessage("menuitem.clone").equals(str)) {
/*  899 */       appletClone();
/*  900 */       return;
/*      */     }
/*      */ 
/*  903 */     if (amh.getMessage("menuitem.stop").equals(str)) {
/*  904 */       appletStop();
/*  905 */       return;
/*      */     }
/*      */ 
/*  908 */     if (amh.getMessage("menuitem.save").equals(str)) {
/*  909 */       appletSave();
/*  910 */       return;
/*      */     }
/*      */ 
/*  913 */     if (amh.getMessage("menuitem.start").equals(str)) {
/*  914 */       appletStart();
/*  915 */       return;
/*      */     }
/*      */ 
/*  918 */     if (amh.getMessage("menuitem.tag").equals(str)) {
/*  919 */       appletTag();
/*  920 */       return;
/*      */     }
/*      */ 
/*  923 */     if (amh.getMessage("menuitem.info").equals(str)) {
/*  924 */       appletInfo();
/*  925 */       return;
/*      */     }
/*      */ 
/*  928 */     if (amh.getMessage("menuitem.encoding").equals(str)) {
/*  929 */       appletCharacterEncoding();
/*  930 */       return;
/*      */     }
/*      */ 
/*  933 */     if (amh.getMessage("menuitem.edit").equals(str)) {
/*  934 */       appletEdit();
/*  935 */       return;
/*      */     }
/*      */ 
/*  938 */     if (amh.getMessage("menuitem.print").equals(str)) {
/*  939 */       appletPrint();
/*  940 */       return;
/*      */     }
/*      */ 
/*  943 */     if (amh.getMessage("menuitem.props").equals(str)) {
/*  944 */       networkProperties();
/*  945 */       return;
/*      */     }
/*      */ 
/*  948 */     if (amh.getMessage("menuitem.close").equals(str)) {
/*  949 */       appletClose();
/*  950 */       return;
/*      */     }
/*      */ 
/*  953 */     if ((this.factory.isStandalone()) && (amh.getMessage("menuitem.quit").equals(str))) {
/*  954 */       appletQuit();
/*  955 */       return;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static int countApplets()
/*      */   {
/*  965 */     return appletPanels.size();
/*      */   }
/*      */ 
/*      */   public static void skipSpace(Reader paramReader)
/*      */     throws IOException
/*      */   {
/*  978 */     while ((c >= 0) && ((c == 32) || (c == 9) || (c == 10) || (c == 13)))
/*      */     {
/*  980 */       c = paramReader.read();
/*      */     }
/*      */   }
/*      */ 
/*      */   public static String scanIdentifier(Reader paramReader)
/*      */     throws IOException
/*      */   {
/*  988 */     StringBuffer localStringBuffer = new StringBuffer();
/*      */ 
/*  990 */     while (((c >= 97) && (c <= 122)) || ((c >= 65) && (c <= 90)) || ((c >= 48) && (c <= 57)) || (c == 95))
/*      */     {
/*  993 */       localStringBuffer.append((char)c);
/*  994 */       c = paramReader.read();
/*      */     }
/*  996 */     return localStringBuffer.toString();
/*      */   }
/*      */ 
/*      */   public static Hashtable scanTag(Reader paramReader)
/*      */     throws IOException
/*      */   {
/* 1005 */     Hashtable localHashtable = new Hashtable();
/* 1006 */     skipSpace(paramReader);
/*      */ 
/* 1043 */     for (; (c >= 0) && (c != 62); 
/* 1043 */       goto 230)
/*      */     {
/* 1008 */       String str1 = scanIdentifier(paramReader);
/* 1009 */       String str2 = "";
/* 1010 */       skipSpace(paramReader);
/* 1011 */       if (c == 61) {
/* 1012 */         int i = -1;
/* 1013 */         c = paramReader.read();
/* 1014 */         skipSpace(paramReader);
/* 1015 */         if ((c == 39) || (c == 34)) {
/* 1016 */           i = c;
/* 1017 */           c = paramReader.read();
/*      */         }
/* 1019 */         StringBuffer localStringBuffer = new StringBuffer();
/* 1020 */         while ((c > 0) && (((i < 0) && (c != 32) && (c != 9) && (c != 10) && (c != 13) && (c != 62)) || ((i >= 0) && (c != i))))
/*      */         {
/* 1024 */           localStringBuffer.append((char)c);
/* 1025 */           c = paramReader.read();
/*      */         }
/* 1027 */         if (c == i) {
/* 1028 */           c = paramReader.read();
/*      */         }
/* 1030 */         skipSpace(paramReader);
/* 1031 */         str2 = localStringBuffer.toString();
/*      */       }
/*      */ 
/* 1034 */       if (!str2.equals("")) {
/* 1035 */         localHashtable.put(str1.toLowerCase(Locale.ENGLISH), str2);
/*      */       }
/*      */ 
/* 1038 */       if ((c != 62) && (c >= 0) && ((c < 97) || (c > 122)) && ((c < 65) || (c > 90)) && ((c < 48) || (c > 57)) && (c != 95))
/*      */       {
/* 1043 */         c = paramReader.read();
/*      */       }
/*      */     }
/*      */ 
/* 1047 */     return localHashtable;
/*      */   }
/*      */ 
/*      */   private static Reader makeReader(InputStream paramInputStream)
/*      */   {
/* 1059 */     if (encoding != null)
/*      */       try {
/* 1061 */         return new BufferedReader(new InputStreamReader(paramInputStream, encoding));
/*      */       } catch (IOException localIOException) {
/*      */       }
/* 1064 */     InputStreamReader localInputStreamReader = new InputStreamReader(paramInputStream);
/* 1065 */     encoding = localInputStreamReader.getEncoding();
/* 1066 */     return new BufferedReader(localInputStreamReader);
/*      */   }
/*      */ 
/*      */   public static void parse(URL paramURL, String paramString)
/*      */     throws IOException
/*      */   {
/* 1073 */     encoding = paramString;
/* 1074 */     parse(paramURL, System.out, new StdAppletViewerFactory());
/*      */   }
/*      */ 
/*      */   public static void parse(URL paramURL) throws IOException {
/* 1078 */     parse(paramURL, System.out, new StdAppletViewerFactory());
/*      */   }
/*      */ 
/*      */   public static void parse(URL paramURL, PrintStream paramPrintStream, AppletViewerFactory paramAppletViewerFactory)
/*      */     throws IOException
/*      */   {
/* 1084 */     int i = 0;
/* 1085 */     int j = 0;
/* 1086 */     int k = 0;
/*      */ 
/* 1089 */     String str1 = amh.getMessage("parse.warning.requiresname");
/* 1090 */     String str2 = amh.getMessage("parse.warning.paramoutside");
/* 1091 */     String str3 = amh.getMessage("parse.warning.applet.requirescode");
/* 1092 */     String str4 = amh.getMessage("parse.warning.applet.requiresheight");
/* 1093 */     String str5 = amh.getMessage("parse.warning.applet.requireswidth");
/* 1094 */     String str6 = amh.getMessage("parse.warning.object.requirescode");
/* 1095 */     String str7 = amh.getMessage("parse.warning.object.requiresheight");
/* 1096 */     String str8 = amh.getMessage("parse.warning.object.requireswidth");
/* 1097 */     String str9 = amh.getMessage("parse.warning.embed.requirescode");
/* 1098 */     String str10 = amh.getMessage("parse.warning.embed.requiresheight");
/* 1099 */     String str11 = amh.getMessage("parse.warning.embed.requireswidth");
/* 1100 */     String str12 = amh.getMessage("parse.warning.appnotLongersupported");
/*      */ 
/* 1102 */     URLConnection localURLConnection = paramURL.openConnection();
/* 1103 */     Reader localReader = makeReader(localURLConnection.getInputStream());
/*      */ 
/* 1107 */     paramURL = localURLConnection.getURL();
/*      */ 
/* 1109 */     int m = 1;
/* 1110 */     Hashtable localHashtable = null;
/*      */     while (true)
/*      */     {
/* 1113 */       c = localReader.read();
/* 1114 */       if (c == -1) {
/*      */         break;
/*      */       }
/* 1117 */       if (c == 60) {
/* 1118 */         c = localReader.read();
/*      */         String str13;
/*      */         Object localObject;
/* 1119 */         if (c == 47) {
/* 1120 */           c = localReader.read();
/* 1121 */           str13 = scanIdentifier(localReader);
/* 1122 */           if ((str13.equalsIgnoreCase("applet")) || (str13.equalsIgnoreCase("object")) || (str13.equalsIgnoreCase("embed")))
/*      */           {
/* 1128 */             if ((j != 0) && 
/* 1129 */               (localHashtable.get("code") == null) && (localHashtable.get("object") == null)) {
/* 1130 */               paramPrintStream.println(str6);
/* 1131 */               localHashtable = null;
/*      */             }
/*      */ 
/* 1135 */             if (localHashtable != null)
/*      */             {
/* 1140 */               paramAppletViewerFactory.createAppletViewer(x, y, paramURL, localHashtable);
/* 1141 */               x += 30;
/* 1142 */               y += 30;
/*      */ 
/* 1144 */               localObject = Toolkit.getDefaultToolkit().getScreenSize();
/* 1145 */               if ((x > ((Dimension)localObject).width - 300) || (y > ((Dimension)localObject).height - 300)) {
/* 1146 */                 x = 0;
/* 1147 */                 y = 2 * m * 30;
/* 1148 */                 m++;
/*      */               }
/*      */             }
/* 1151 */             localHashtable = null;
/* 1152 */             i = 0;
/* 1153 */             j = 0;
/* 1154 */             k = 0;
/*      */           }
/*      */         }
/*      */         else {
/* 1158 */           str13 = scanIdentifier(localReader);
/* 1159 */           if (str13.equalsIgnoreCase("param")) {
/* 1160 */             localObject = scanTag(localReader);
/* 1161 */             String str14 = (String)((Hashtable)localObject).get("name");
/* 1162 */             if (str14 == null) {
/* 1163 */               paramPrintStream.println(str1);
/*      */             } else {
/* 1165 */               String str15 = (String)((Hashtable)localObject).get("value");
/* 1166 */               if (str15 == null)
/* 1167 */                 paramPrintStream.println(str1);
/* 1168 */               else if (localHashtable != null)
/* 1169 */                 localHashtable.put(str14.toLowerCase(), str15);
/*      */               else {
/* 1171 */                 paramPrintStream.println(str2);
/*      */               }
/*      */             }
/*      */           }
/* 1175 */           else if (str13.equalsIgnoreCase("applet")) {
/* 1176 */             i = 1;
/* 1177 */             localHashtable = scanTag(localReader);
/* 1178 */             if ((localHashtable.get("code") == null) && (localHashtable.get("object") == null)) {
/* 1179 */               paramPrintStream.println(str3);
/* 1180 */               localHashtable = null;
/* 1181 */             } else if (localHashtable.get("width") == null) {
/* 1182 */               paramPrintStream.println(str5);
/* 1183 */               localHashtable = null;
/* 1184 */             } else if (localHashtable.get("height") == null) {
/* 1185 */               paramPrintStream.println(str4);
/* 1186 */               localHashtable = null;
/*      */             }
/*      */           }
/* 1189 */           else if (str13.equalsIgnoreCase("object")) {
/* 1190 */             j = 1;
/* 1191 */             localHashtable = scanTag(localReader);
/*      */ 
/* 1194 */             if (localHashtable.get("codebase") != null) {
/* 1195 */               localHashtable.remove("codebase");
/*      */             }
/*      */ 
/* 1198 */             if (localHashtable.get("width") == null) {
/* 1199 */               paramPrintStream.println(str8);
/* 1200 */               localHashtable = null;
/* 1201 */             } else if (localHashtable.get("height") == null) {
/* 1202 */               paramPrintStream.println(str7);
/* 1203 */               localHashtable = null;
/*      */             }
/*      */           }
/* 1206 */           else if (str13.equalsIgnoreCase("embed")) {
/* 1207 */             k = 1;
/* 1208 */             localHashtable = scanTag(localReader);
/*      */ 
/* 1210 */             if ((localHashtable.get("code") == null) && (localHashtable.get("object") == null)) {
/* 1211 */               paramPrintStream.println(str9);
/* 1212 */               localHashtable = null;
/* 1213 */             } else if (localHashtable.get("width") == null) {
/* 1214 */               paramPrintStream.println(str11);
/* 1215 */               localHashtable = null;
/* 1216 */             } else if (localHashtable.get("height") == null) {
/* 1217 */               paramPrintStream.println(str10);
/* 1218 */               localHashtable = null;
/*      */             }
/*      */           }
/* 1221 */           else if (str13.equalsIgnoreCase("app")) {
/* 1222 */             paramPrintStream.println(str12);
/* 1223 */             localObject = scanTag(localReader);
/* 1224 */             str13 = (String)((Hashtable)localObject).get("class");
/* 1225 */             if (str13 != null) {
/* 1226 */               ((Hashtable)localObject).remove("class");
/* 1227 */               ((Hashtable)localObject).put("code", str13 + ".class");
/*      */             }
/* 1229 */             str13 = (String)((Hashtable)localObject).get("src");
/* 1230 */             if (str13 != null) {
/* 1231 */               ((Hashtable)localObject).remove("src");
/* 1232 */               ((Hashtable)localObject).put("codebase", str13);
/*      */             }
/* 1234 */             if (((Hashtable)localObject).get("width") == null) {
/* 1235 */               ((Hashtable)localObject).put("width", "100");
/*      */             }
/* 1237 */             if (((Hashtable)localObject).get("height") == null) {
/* 1238 */               ((Hashtable)localObject).put("height", "100");
/*      */             }
/* 1240 */             printTag(paramPrintStream, (Hashtable)localObject);
/* 1241 */             paramPrintStream.println();
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 1246 */     localReader.close();
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public static void main(String[] paramArrayOfString)
/*      */   {
/* 1257 */     Main.main(paramArrayOfString);
/*      */   }
/*      */ 
/*      */   private static void checkConnect(URL paramURL)
/*      */   {
/* 1264 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 1265 */     if (localSecurityManager != null)
/*      */       try {
/* 1267 */         Permission localPermission = paramURL.openConnection().getPermission();
/*      */ 
/* 1269 */         if (localPermission != null)
/* 1270 */           localSecurityManager.checkPermission(localPermission);
/*      */         else
/* 1272 */           localSecurityManager.checkConnect(paramURL.getHost(), paramURL.getPort());
/*      */       } catch (IOException localIOException) {
/* 1274 */         localSecurityManager.checkConnect(paramURL.getHost(), paramURL.getPort());
/*      */       }
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*  512 */     systemParam.put("codebase", "codebase");
/*  513 */     systemParam.put("code", "code");
/*  514 */     systemParam.put("alt", "alt");
/*  515 */     systemParam.put("width", "width");
/*  516 */     systemParam.put("height", "height");
/*  517 */     systemParam.put("align", "align");
/*  518 */     systemParam.put("vspace", "vspace");
/*  519 */     systemParam.put("hspace", "hspace");
/*      */   }
/*      */ 
/*      */   private final class UserActionListener
/*      */     implements ActionListener
/*      */   {
/*      */     private UserActionListener()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent)
/*      */     {
/*  151 */       AppletViewer.this.processUserAction(paramActionEvent);
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.applet.AppletViewer
 * JD-Core Version:    0.6.2
 */