/*     */ package sun.swing;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Image;
/*     */ import java.awt.Insets;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.io.File;
/*     */ import javax.swing.Box;
/*     */ import javax.swing.ButtonGroup;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.ImageIcon;
/*     */ import javax.swing.JFileChooser;
/*     */ import javax.swing.JToggleButton;
/*     */ import javax.swing.JToolBar;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.border.BevelBorder;
/*     */ import javax.swing.border.EmptyBorder;
/*     */ import javax.swing.filechooser.FileSystemView;
/*     */ import sun.awt.OSInfo;
/*     */ import sun.awt.OSInfo.OSType;
/*     */ import sun.awt.OSInfo.WindowsVersion;
/*     */ import sun.awt.shell.ShellFolder;
/*     */ 
/*     */ public class WindowsPlacesBar extends JToolBar
/*     */   implements ActionListener, PropertyChangeListener
/*     */ {
/*     */   JFileChooser fc;
/*     */   JToggleButton[] buttons;
/*     */   ButtonGroup buttonGroup;
/*     */   File[] files;
/*     */   final Dimension buttonSize;
/*     */ 
/*     */   public WindowsPlacesBar(JFileChooser paramJFileChooser, boolean paramBoolean)
/*     */   {
/*  59 */     super(1);
/*  60 */     this.fc = paramJFileChooser;
/*  61 */     setFloatable(false);
/*  62 */     putClientProperty("JToolBar.isRollover", Boolean.TRUE);
/*     */ 
/*  64 */     int i = (OSInfo.getOSType() == OSInfo.OSType.WINDOWS) && (OSInfo.getWindowsVersion().compareTo(OSInfo.WINDOWS_XP) >= 0) ? 1 : 0;
/*     */ 
/*  67 */     if (paramBoolean) {
/*  68 */       this.buttonSize = new Dimension(83, 69);
/*  69 */       putClientProperty("XPStyle.subAppName", "placesbar");
/*  70 */       setBorder(new EmptyBorder(1, 1, 1, 1));
/*     */     }
/*     */     else {
/*  73 */       this.buttonSize = new Dimension(83, i != 0 ? 65 : 54);
/*  74 */       setBorder(new BevelBorder(1, UIManager.getColor("ToolBar.highlight"), UIManager.getColor("ToolBar.background"), UIManager.getColor("ToolBar.darkShadow"), UIManager.getColor("ToolBar.shadow")));
/*     */     }
/*     */ 
/*  80 */     Color localColor = new Color(UIManager.getColor("ToolBar.shadow").getRGB());
/*  81 */     setBackground(localColor);
/*  82 */     FileSystemView localFileSystemView = paramJFileChooser.getFileSystemView();
/*     */ 
/*  84 */     this.files = ((File[])ShellFolder.get("fileChooserShortcutPanelFolders"));
/*     */ 
/*  86 */     this.buttons = new JToggleButton[this.files.length];
/*  87 */     this.buttonGroup = new ButtonGroup();
/*  88 */     for (int j = 0; j < this.files.length; j++) {
/*  89 */       if (localFileSystemView.isFileSystemRoot(this.files[j]))
/*     */       {
/*  91 */         this.files[j] = localFileSystemView.createFileObject(this.files[j].getAbsolutePath());
/*     */       }
/*     */ 
/*  94 */       String str = localFileSystemView.getSystemDisplayName(this.files[j]);
/*  95 */       int k = str.lastIndexOf(File.separatorChar);
/*  96 */       if ((k >= 0) && (k < str.length() - 1))
/*  97 */         str = str.substring(k + 1);
/*     */       Object localObject2;
/*     */       Object localObject1;
/* 100 */       if ((this.files[j] instanceof ShellFolder))
/*     */       {
/* 102 */         localObject2 = (ShellFolder)this.files[j];
/* 103 */         Image localImage = ((ShellFolder)localObject2).getIcon(true);
/*     */ 
/* 105 */         if (localImage == null)
/*     */         {
/* 107 */           localImage = (Image)ShellFolder.get("shell32LargeIcon 1");
/*     */         }
/*     */ 
/* 110 */         localObject1 = localImage == null ? null : new ImageIcon(localImage, ((ShellFolder)localObject2).getFolderType());
/*     */       } else {
/* 112 */         localObject1 = localFileSystemView.getSystemIcon(this.files[j]);
/*     */       }
/* 114 */       this.buttons[j] = new JToggleButton(str, (Icon)localObject1);
/* 115 */       if (i != 0) {
/* 116 */         this.buttons[j].setText("<html><center>" + str + "</center></html>");
/*     */       }
/* 118 */       if (paramBoolean) {
/* 119 */         this.buttons[j].putClientProperty("XPStyle.subAppName", "placesbar");
/*     */       } else {
/* 121 */         localObject2 = new Color(UIManager.getColor("List.selectionForeground").getRGB());
/* 122 */         this.buttons[j].setContentAreaFilled(false);
/* 123 */         this.buttons[j].setForeground((Color)localObject2);
/*     */       }
/* 125 */       this.buttons[j].setMargin(new Insets(3, 2, 1, 2));
/* 126 */       this.buttons[j].setFocusPainted(false);
/* 127 */       this.buttons[j].setIconTextGap(0);
/* 128 */       this.buttons[j].setHorizontalTextPosition(0);
/* 129 */       this.buttons[j].setVerticalTextPosition(3);
/* 130 */       this.buttons[j].setAlignmentX(0.5F);
/* 131 */       this.buttons[j].setPreferredSize(this.buttonSize);
/* 132 */       this.buttons[j].setMaximumSize(this.buttonSize);
/* 133 */       this.buttons[j].addActionListener(this);
/* 134 */       add(this.buttons[j]);
/* 135 */       if ((j < this.files.length - 1) && (paramBoolean)) {
/* 136 */         add(Box.createRigidArea(new Dimension(1, 1)));
/*     */       }
/* 138 */       this.buttonGroup.add(this.buttons[j]);
/*     */     }
/* 140 */     doDirectoryChanged(paramJFileChooser.getCurrentDirectory());
/*     */   }
/*     */ 
/*     */   protected void doDirectoryChanged(File paramFile) {
/* 144 */     for (int i = 0; i < this.buttons.length; i++) {
/* 145 */       JToggleButton localJToggleButton = this.buttons[i];
/* 146 */       if (this.files[i].equals(paramFile)) {
/* 147 */         localJToggleButton.setSelected(true);
/* 148 */         break;
/* 149 */       }if (localJToggleButton.isSelected())
/*     */       {
/* 152 */         this.buttonGroup.remove(localJToggleButton);
/* 153 */         localJToggleButton.setSelected(false);
/* 154 */         this.buttonGroup.add(localJToggleButton);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent) {
/* 160 */     String str = paramPropertyChangeEvent.getPropertyName();
/* 161 */     if (str == "directoryChanged")
/* 162 */       doDirectoryChanged(this.fc.getCurrentDirectory());
/*     */   }
/*     */ 
/*     */   public void actionPerformed(ActionEvent paramActionEvent)
/*     */   {
/* 167 */     JToggleButton localJToggleButton = (JToggleButton)paramActionEvent.getSource();
/* 168 */     for (int i = 0; i < this.buttons.length; i++)
/* 169 */       if (localJToggleButton == this.buttons[i]) {
/* 170 */         this.fc.setCurrentDirectory(this.files[i]);
/* 171 */         break;
/*     */       }
/*     */   }
/*     */ 
/*     */   public Dimension getPreferredSize()
/*     */   {
/* 177 */     Dimension localDimension1 = super.getMinimumSize();
/* 178 */     Dimension localDimension2 = super.getPreferredSize();
/* 179 */     int i = localDimension1.height;
/* 180 */     if ((this.buttons != null) && (this.buttons.length > 0) && (this.buttons.length < 5)) {
/* 181 */       JToggleButton localJToggleButton = this.buttons[0];
/* 182 */       if (localJToggleButton != null) {
/* 183 */         int j = 5 * (localJToggleButton.getPreferredSize().height + 1);
/* 184 */         if (j > i) {
/* 185 */           i = j;
/*     */         }
/*     */       }
/*     */     }
/* 189 */     if (i > localDimension2.height) {
/* 190 */       localDimension2 = new Dimension(localDimension2.width, i);
/*     */     }
/* 192 */     return localDimension2;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.swing.WindowsPlacesBar
 * JD-Core Version:    0.6.2
 */