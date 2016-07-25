/*      */ package sun.security.tools.policytool;
/*      */ 
/*      */ import java.awt.Button;
/*      */ import java.awt.Component;
/*      */ import java.awt.Container;
/*      */ import java.awt.Frame;
/*      */ import java.awt.GridBagConstraints;
/*      */ import java.awt.GridBagLayout;
/*      */ import java.awt.Insets;
/*      */ import java.awt.Label;
/*      */ import java.awt.List;
/*      */ import java.awt.Menu;
/*      */ import java.awt.MenuBar;
/*      */ import java.awt.Panel;
/*      */ import java.awt.Point;
/*      */ import java.awt.TextArea;
/*      */ import java.awt.TextField;
/*      */ import java.awt.Window;
/*      */ import java.awt.event.ActionEvent;
/*      */ import java.awt.event.ActionListener;
/*      */ import java.io.File;
/*      */ import java.io.FileNotFoundException;
/*      */ import java.security.AccessController;
/*      */ import java.text.MessageFormat;
/*      */ import java.util.ResourceBundle;
/*      */ import java.util.Vector;
/*      */ import javax.accessibility.AccessibleContext;
/*      */ import sun.security.action.GetPropertyAction;
/*      */ 
/*      */ class ToolWindow extends Frame
/*      */ {
/*      */   private static final long serialVersionUID = 5682568601210376777L;
/*  884 */   public static final Insets TOP_PADDING = new Insets(25, 0, 0, 0);
/*  885 */   public static final Insets BOTTOM_PADDING = new Insets(0, 0, 25, 0);
/*  886 */   public static final Insets LITE_BOTTOM_PADDING = new Insets(0, 0, 10, 0);
/*  887 */   public static final Insets LR_PADDING = new Insets(0, 10, 0, 10);
/*  888 */   public static final Insets TOP_BOTTOM_PADDING = new Insets(15, 0, 15, 0);
/*  889 */   public static final Insets L_TOP_BOTTOM_PADDING = new Insets(5, 10, 15, 0);
/*  890 */   public static final Insets LR_BOTTOM_PADDING = new Insets(0, 10, 5, 10);
/*  891 */   public static final Insets L_BOTTOM_PADDING = new Insets(0, 10, 5, 0);
/*  892 */   public static final Insets R_BOTTOM_PADDING = new Insets(0, 0, 5, 10);
/*      */ 
/*  895 */   public static final String NEW_POLICY_FILE = PolicyTool.rb.getString("New");
/*      */ 
/*  897 */   public static final String OPEN_POLICY_FILE = PolicyTool.rb.getString("Open");
/*      */ 
/*  899 */   public static final String SAVE_POLICY_FILE = PolicyTool.rb.getString("Save");
/*      */ 
/*  901 */   public static final String SAVE_AS_POLICY_FILE = PolicyTool.rb.getString("Save.As");
/*      */ 
/*  903 */   public static final String VIEW_WARNINGS = PolicyTool.rb.getString("View.Warning.Log");
/*      */ 
/*  905 */   public static final String QUIT = PolicyTool.rb.getString("Exit");
/*      */ 
/*  907 */   public static final String ADD_POLICY_ENTRY = PolicyTool.rb.getString("Add.Policy.Entry");
/*      */ 
/*  909 */   public static final String EDIT_POLICY_ENTRY = PolicyTool.rb.getString("Edit.Policy.Entry");
/*      */ 
/*  911 */   public static final String REMOVE_POLICY_ENTRY = PolicyTool.rb.getString("Remove.Policy.Entry");
/*      */ 
/*  913 */   public static final String EDIT_KEYSTORE = PolicyTool.rb.getString("Edit");
/*      */ 
/*  915 */   public static final String ADD_PUBKEY_ALIAS = PolicyTool.rb.getString("Add.Public.Key.Alias");
/*      */ 
/*  917 */   public static final String REMOVE_PUBKEY_ALIAS = PolicyTool.rb.getString("Remove.Public.Key.Alias");
/*      */   public static final int MW_FILENAME_LABEL = 0;
/*      */   public static final int MW_FILENAME_TEXTFIELD = 1;
/*      */   public static final int MW_PANEL = 2;
/*      */   public static final int MW_ADD_BUTTON = 0;
/*      */   public static final int MW_EDIT_BUTTON = 1;
/*      */   public static final int MW_REMOVE_BUTTON = 2;
/*      */   public static final int MW_POLICY_LIST = 3;
/*      */   private PolicyTool tool;
/*      */ 
/*      */   ToolWindow(PolicyTool paramPolicyTool)
/*      */   {
/*  935 */     this.tool = paramPolicyTool;
/*      */   }
/*      */ 
/*      */   private void initWindow()
/*      */   {
/*  944 */     MenuBar localMenuBar = new MenuBar();
/*      */ 
/*  947 */     Menu localMenu = new Menu(PolicyTool.rb.getString("File"));
/*  948 */     localMenu.add(NEW_POLICY_FILE);
/*  949 */     localMenu.add(OPEN_POLICY_FILE);
/*  950 */     localMenu.add(SAVE_POLICY_FILE);
/*  951 */     localMenu.add(SAVE_AS_POLICY_FILE);
/*  952 */     localMenu.add(VIEW_WARNINGS);
/*  953 */     localMenu.add(QUIT);
/*  954 */     localMenu.addActionListener(new FileMenuListener(this.tool, this));
/*  955 */     localMenuBar.add(localMenu);
/*  956 */     setMenuBar(localMenuBar);
/*      */ 
/*  959 */     localMenu = new Menu(PolicyTool.rb.getString("KeyStore"));
/*  960 */     localMenu.add(EDIT_KEYSTORE);
/*  961 */     localMenu.addActionListener(new MainWindowListener(this.tool, this));
/*  962 */     localMenuBar.add(localMenu);
/*  963 */     setMenuBar(localMenuBar);
/*      */ 
/*  967 */     Label localLabel = new Label(PolicyTool.rb.getString("Policy.File."));
/*  968 */     addNewComponent(this, localLabel, 0, 0, 0, 1, 1, 0.0D, 0.0D, 1, TOP_BOTTOM_PADDING);
/*      */ 
/*  971 */     TextField localTextField = new TextField(50);
/*  972 */     localTextField.getAccessibleContext().setAccessibleName(PolicyTool.rb.getString("Policy.File."));
/*      */ 
/*  974 */     localTextField.setEditable(false);
/*  975 */     addNewComponent(this, localTextField, 1, 1, 0, 1, 1, 0.0D, 0.0D, 1, TOP_BOTTOM_PADDING);
/*      */ 
/*  981 */     Panel localPanel = new Panel();
/*  982 */     localPanel.setLayout(new GridBagLayout());
/*      */ 
/*  984 */     Button localButton = new Button(ADD_POLICY_ENTRY);
/*  985 */     localButton.addActionListener(new MainWindowListener(this.tool, this));
/*  986 */     addNewComponent(localPanel, localButton, 0, 0, 0, 1, 1, 0.0D, 0.0D, 1, LR_PADDING);
/*      */ 
/*  990 */     localButton = new Button(EDIT_POLICY_ENTRY);
/*  991 */     localButton.addActionListener(new MainWindowListener(this.tool, this));
/*  992 */     addNewComponent(localPanel, localButton, 1, 1, 0, 1, 1, 0.0D, 0.0D, 1, LR_PADDING);
/*      */ 
/*  996 */     localButton = new Button(REMOVE_POLICY_ENTRY);
/*  997 */     localButton.addActionListener(new MainWindowListener(this.tool, this));
/*  998 */     addNewComponent(localPanel, localButton, 2, 2, 0, 1, 1, 0.0D, 0.0D, 1, LR_PADDING);
/*      */ 
/* 1002 */     addNewComponent(this, localPanel, 2, 0, 2, 2, 1, 0.0D, 0.0D, 1, BOTTOM_PADDING);
/*      */ 
/* 1007 */     String str = this.tool.getPolicyFileName();
/*      */     Object localObject1;
/* 1008 */     if (str == null)
/*      */     {
/* 1010 */       localObject1 = (String)AccessController.doPrivileged(new GetPropertyAction("user.home"));
/*      */ 
/* 1012 */       str = (String)localObject1 + File.separatorChar + ".java.policy";
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/* 1017 */       this.tool.openPolicy(str);
/*      */ 
/* 1020 */       localObject1 = new List(40, false);
/* 1021 */       ((List)localObject1).addActionListener(new PolicyListListener(this.tool, this));
/* 1022 */       localObject2 = this.tool.getEntry();
/* 1023 */       if (localObject2 != null) {
/* 1024 */         for (int i = 0; i < localObject2.length; i++)
/* 1025 */           ((List)localObject1).add(localObject2[i].headerToString());
/*      */       }
/* 1027 */       localObject3 = (TextField)getComponent(1);
/*      */ 
/* 1029 */       ((TextField)localObject3).setText(str);
/* 1030 */       initPolicyList((List)localObject1);
/*      */     }
/*      */     catch (FileNotFoundException localFileNotFoundException)
/*      */     {
/* 1034 */       localObject2 = new List(40, false);
/* 1035 */       ((List)localObject2).addActionListener(new PolicyListListener(this.tool, this));
/* 1036 */       initPolicyList((List)localObject2);
/* 1037 */       this.tool.setPolicyFileName(null);
/* 1038 */       this.tool.modified = false;
/* 1039 */       setVisible(true);
/*      */ 
/* 1042 */       this.tool.warnings.addElement(localFileNotFoundException.toString());
/*      */     }
/*      */     catch (Exception localException)
/*      */     {
/* 1046 */       Object localObject2 = new List(40, false);
/* 1047 */       ((List)localObject2).addActionListener(new PolicyListListener(this.tool, this));
/* 1048 */       initPolicyList((List)localObject2);
/* 1049 */       this.tool.setPolicyFileName(null);
/* 1050 */       this.tool.modified = false;
/* 1051 */       setVisible(true);
/*      */ 
/* 1054 */       Object localObject3 = new MessageFormat(PolicyTool.rb.getString("Could.not.open.policy.file.policyFile.e.toString."));
/*      */ 
/* 1056 */       Object[] arrayOfObject = { str, localException.toString() };
/* 1057 */       displayErrorDialog(null, ((MessageFormat)localObject3).format(arrayOfObject));
/*      */     }
/*      */   }
/*      */ 
/*      */   void addNewComponent(Container paramContainer, Component paramComponent, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, double paramDouble1, double paramDouble2, int paramInt6, Insets paramInsets)
/*      */   {
/* 1070 */     paramContainer.add(paramComponent, paramInt1);
/*      */ 
/* 1073 */     GridBagLayout localGridBagLayout = (GridBagLayout)paramContainer.getLayout();
/* 1074 */     GridBagConstraints localGridBagConstraints = new GridBagConstraints();
/* 1075 */     localGridBagConstraints.gridx = paramInt2;
/* 1076 */     localGridBagConstraints.gridy = paramInt3;
/* 1077 */     localGridBagConstraints.gridwidth = paramInt4;
/* 1078 */     localGridBagConstraints.gridheight = paramInt5;
/* 1079 */     localGridBagConstraints.weightx = paramDouble1;
/* 1080 */     localGridBagConstraints.weighty = paramDouble2;
/* 1081 */     localGridBagConstraints.fill = paramInt6;
/* 1082 */     if (paramInsets != null) localGridBagConstraints.insets = paramInsets;
/* 1083 */     localGridBagLayout.setConstraints(paramComponent, localGridBagConstraints);
/*      */   }
/*      */ 
/*      */   void addNewComponent(Container paramContainer, Component paramComponent, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, double paramDouble1, double paramDouble2, int paramInt6)
/*      */   {
/* 1095 */     addNewComponent(paramContainer, paramComponent, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramDouble1, paramDouble2, paramInt6, null);
/*      */   }
/*      */ 
/*      */   void initPolicyList(List paramList)
/*      */   {
/* 1108 */     addNewComponent(this, paramList, 3, 0, 3, 2, 1, 1.0D, 1.0D, 1);
/*      */   }
/*      */ 
/*      */   void replacePolicyList(List paramList)
/*      */   {
/* 1120 */     List localList = (List)getComponent(3);
/* 1121 */     localList.removeAll();
/* 1122 */     String[] arrayOfString = paramList.getItems();
/* 1123 */     for (int i = 0; i < arrayOfString.length; i++)
/* 1124 */       localList.add(arrayOfString[i]);
/*      */   }
/*      */ 
/*      */   void displayToolWindow(String[] paramArrayOfString)
/*      */   {
/* 1132 */     setTitle(PolicyTool.rb.getString("Policy.Tool"));
/* 1133 */     setResizable(true);
/* 1134 */     addWindowListener(new ToolWindowListener(this));
/* 1135 */     setBounds(135, 80, 500, 500);
/* 1136 */     setLayout(new GridBagLayout());
/*      */ 
/* 1138 */     initWindow();
/*      */ 
/* 1141 */     setVisible(true);
/*      */ 
/* 1143 */     if (this.tool.newWarning == true)
/* 1144 */       displayStatusDialog(this, PolicyTool.rb.getString("Errors.have.occurred.while.opening.the.policy.configuration.View.the.Warning.Log.for.more.information."));
/*      */   }
/*      */ 
/*      */   void displayErrorDialog(Window paramWindow, String paramString)
/*      */   {
/* 1153 */     ToolDialog localToolDialog = new ToolDialog(PolicyTool.rb.getString("Error"), this.tool, this, true);
/*      */ 
/* 1157 */     Point localPoint = paramWindow == null ? getLocationOnScreen() : paramWindow.getLocationOnScreen();
/*      */ 
/* 1159 */     localToolDialog.setBounds(localPoint.x + 50, localPoint.y + 50, 600, 100);
/* 1160 */     localToolDialog.setLayout(new GridBagLayout());
/*      */ 
/* 1162 */     Label localLabel = new Label(paramString);
/* 1163 */     addNewComponent(localToolDialog, localLabel, 0, 0, 0, 1, 1, 0.0D, 0.0D, 1);
/*      */ 
/* 1166 */     Button localButton = new Button(PolicyTool.rb.getString("OK"));
/* 1167 */     localButton.addActionListener(new ErrorOKButtonListener(localToolDialog));
/* 1168 */     addNewComponent(localToolDialog, localButton, 1, 0, 1, 1, 1, 0.0D, 0.0D, 3);
/*      */ 
/* 1171 */     localToolDialog.pack();
/* 1172 */     localToolDialog.setVisible(true);
/*      */   }
/*      */ 
/*      */   void displayErrorDialog(Window paramWindow, Throwable paramThrowable)
/*      */   {
/* 1179 */     if ((paramThrowable instanceof NoDisplayException)) {
/* 1180 */       return;
/*      */     }
/* 1182 */     displayErrorDialog(paramWindow, paramThrowable.toString());
/*      */   }
/*      */ 
/*      */   void displayStatusDialog(Window paramWindow, String paramString)
/*      */   {
/* 1189 */     ToolDialog localToolDialog = new ToolDialog(PolicyTool.rb.getString("Status"), this.tool, this, true);
/*      */ 
/* 1193 */     Point localPoint = paramWindow == null ? getLocationOnScreen() : paramWindow.getLocationOnScreen();
/*      */ 
/* 1195 */     localToolDialog.setBounds(localPoint.x + 50, localPoint.y + 50, 500, 100);
/* 1196 */     localToolDialog.setLayout(new GridBagLayout());
/*      */ 
/* 1198 */     Label localLabel = new Label(paramString);
/* 1199 */     addNewComponent(localToolDialog, localLabel, 0, 0, 0, 1, 1, 0.0D, 0.0D, 1);
/*      */ 
/* 1202 */     Button localButton = new Button(PolicyTool.rb.getString("OK"));
/* 1203 */     localButton.addActionListener(new StatusOKButtonListener(localToolDialog));
/* 1204 */     addNewComponent(localToolDialog, localButton, 1, 0, 1, 1, 1, 0.0D, 0.0D, 3);
/*      */ 
/* 1206 */     localToolDialog.pack();
/* 1207 */     localToolDialog.setVisible(true);
/*      */   }
/*      */ 
/*      */   void displayWarningLog(Window paramWindow)
/*      */   {
/* 1215 */     ToolDialog localToolDialog = new ToolDialog(PolicyTool.rb.getString("Warning"), this.tool, this, true);
/*      */ 
/* 1219 */     Point localPoint = paramWindow == null ? getLocationOnScreen() : paramWindow.getLocationOnScreen();
/*      */ 
/* 1221 */     localToolDialog.setBounds(localPoint.x + 50, localPoint.y + 50, 500, 100);
/* 1222 */     localToolDialog.setLayout(new GridBagLayout());
/*      */ 
/* 1224 */     TextArea localTextArea = new TextArea();
/* 1225 */     localTextArea.setEditable(false);
/* 1226 */     for (int i = 0; i < this.tool.warnings.size(); i++) {
/* 1227 */       localTextArea.append((String)this.tool.warnings.elementAt(i));
/* 1228 */       localTextArea.append(PolicyTool.rb.getString("NEWLINE"));
/*      */     }
/* 1230 */     addNewComponent(localToolDialog, localTextArea, 0, 0, 0, 1, 1, 0.0D, 0.0D, 1, BOTTOM_PADDING);
/*      */ 
/* 1233 */     localTextArea.setFocusable(false);
/*      */ 
/* 1235 */     Button localButton = new Button(PolicyTool.rb.getString("OK"));
/* 1236 */     localButton.addActionListener(new CancelButtonListener(localToolDialog));
/* 1237 */     addNewComponent(localToolDialog, localButton, 1, 0, 1, 1, 1, 0.0D, 0.0D, 3, LR_PADDING);
/*      */ 
/* 1241 */     localToolDialog.pack();
/* 1242 */     localToolDialog.setVisible(true);
/*      */   }
/*      */ 
/*      */   char displayYesNoDialog(Window paramWindow, String paramString1, String paramString2, String paramString3, String paramString4)
/*      */   {
/* 1247 */     final ToolDialog localToolDialog = new ToolDialog(paramString1, this.tool, this, true);
/*      */ 
/* 1249 */     Point localPoint = paramWindow == null ? getLocationOnScreen() : paramWindow.getLocationOnScreen();
/*      */ 
/* 1251 */     localToolDialog.setBounds(localPoint.x + 75, localPoint.y + 100, 400, 150);
/* 1252 */     localToolDialog.setLayout(new GridBagLayout());
/*      */ 
/* 1254 */     TextArea localTextArea = new TextArea(paramString2, 10, 50, 1);
/* 1255 */     localTextArea.setEditable(false);
/* 1256 */     addNewComponent(localToolDialog, localTextArea, 0, 0, 0, 1, 1, 0.0D, 0.0D, 1);
/*      */ 
/* 1258 */     localTextArea.setFocusable(false);
/*      */ 
/* 1260 */     Panel localPanel = new Panel();
/* 1261 */     localPanel.setLayout(new GridBagLayout());
/*      */ 
/* 1264 */     final StringBuffer localStringBuffer = new StringBuffer();
/*      */ 
/* 1266 */     Button localButton = new Button(paramString3);
/* 1267 */     localButton.addActionListener(new ActionListener() {
/*      */       public void actionPerformed(ActionEvent paramAnonymousActionEvent) {
/* 1269 */         localStringBuffer.append('Y');
/* 1270 */         localToolDialog.setVisible(false);
/* 1271 */         localToolDialog.dispose();
/*      */       }
/*      */     });
/* 1274 */     addNewComponent(localPanel, localButton, 0, 0, 0, 1, 1, 0.0D, 0.0D, 3, LR_PADDING);
/*      */ 
/* 1278 */     localButton = new Button(paramString4);
/* 1279 */     localButton.addActionListener(new ActionListener() {
/*      */       public void actionPerformed(ActionEvent paramAnonymousActionEvent) {
/* 1281 */         localStringBuffer.append('N');
/* 1282 */         localToolDialog.setVisible(false);
/* 1283 */         localToolDialog.dispose();
/*      */       }
/*      */     });
/* 1286 */     addNewComponent(localPanel, localButton, 1, 1, 0, 1, 1, 0.0D, 0.0D, 3, LR_PADDING);
/*      */ 
/* 1290 */     addNewComponent(localToolDialog, localPanel, 1, 0, 1, 1, 1, 0.0D, 0.0D, 3);
/*      */ 
/* 1293 */     localToolDialog.pack();
/* 1294 */     localToolDialog.setVisible(true);
/* 1295 */     if (localStringBuffer.length() > 0) {
/* 1296 */       return localStringBuffer.charAt(0);
/*      */     }
/*      */ 
/* 1299 */     return 'N';
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.tools.policytool.ToolWindow
 * JD-Core Version:    0.6.2
 */