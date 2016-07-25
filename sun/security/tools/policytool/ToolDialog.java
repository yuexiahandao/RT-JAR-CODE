/*      */ package sun.security.tools.policytool;
/*      */ 
/*      */ import java.awt.Button;
/*      */ import java.awt.Choice;
/*      */ import java.awt.Component;
/*      */ import java.awt.Container;
/*      */ import java.awt.Dialog;
/*      */ import java.awt.FileDialog;
/*      */ import java.awt.GridBagLayout;
/*      */ import java.awt.Label;
/*      */ import java.awt.List;
/*      */ import java.awt.Panel;
/*      */ import java.awt.Point;
/*      */ import java.awt.TextField;
/*      */ import java.awt.Window;
/*      */ import java.awt.event.WindowAdapter;
/*      */ import java.awt.event.WindowEvent;
/*      */ import java.io.File;
/*      */ import java.io.FileNotFoundException;
/*      */ import java.io.IOException;
/*      */ import java.io.PrintWriter;
/*      */ import java.io.StringWriter;
/*      */ import java.lang.reflect.InvocationTargetException;
/*      */ import java.net.MalformedURLException;
/*      */ import java.security.InvalidParameterException;
/*      */ import java.security.PublicKey;
/*      */ import java.security.cert.CertificateException;
/*      */ import java.text.MessageFormat;
/*      */ import java.util.ArrayList;
/*      */ import java.util.LinkedList;
/*      */ import java.util.ResourceBundle;
/*      */ import java.util.Vector;
/*      */ import javax.accessibility.AccessibleContext;
/*      */ import sun.security.provider.PolicyParser.GrantEntry;
/*      */ import sun.security.provider.PolicyParser.PermissionEntry;
/*      */ import sun.security.provider.PolicyParser.PrincipalEntry;
/*      */ 
/*      */ class ToolDialog extends Dialog
/*      */ {
/*      */   private static final long serialVersionUID = -372244357011301190L;
/*      */   public static final int NOACTION = 0;
/*      */   public static final int QUIT = 1;
/*      */   public static final int NEW = 2;
/*      */   public static final int OPEN = 3;
/*      */   public static final String ALL_PERM_CLASS = "java.security.AllPermission";
/*      */   public static final String FILE_PERM_CLASS = "java.io.FilePermission";
/*      */   public static final String X500_PRIN_CLASS = "javax.security.auth.x500.X500Principal";
/* 1327 */   public static final String PERM = PolicyTool.rb.getString("Permission.");
/*      */ 
/* 1331 */   public static final String PRIN_TYPE = PolicyTool.rb.getString("Principal.Type.");
/*      */ 
/* 1333 */   public static final String PRIN_NAME = PolicyTool.rb.getString("Principal.Name.");
/*      */ 
/* 1337 */   public static final String PERM_NAME = PolicyTool.rb.getString("Target.Name.");
/*      */ 
/* 1342 */   public static final String PERM_ACTIONS = PolicyTool.rb.getString("Actions.");
/*      */   public static final int PE_CODEBASE_LABEL = 0;
/*      */   public static final int PE_CODEBASE_TEXTFIELD = 1;
/*      */   public static final int PE_SIGNEDBY_LABEL = 2;
/*      */   public static final int PE_SIGNEDBY_TEXTFIELD = 3;
/*      */   public static final int PE_PANEL0 = 4;
/*      */   public static final int PE_ADD_PRIN_BUTTON = 0;
/*      */   public static final int PE_EDIT_PRIN_BUTTON = 1;
/*      */   public static final int PE_REMOVE_PRIN_BUTTON = 2;
/*      */   public static final int PE_PRIN_LABEL = 5;
/*      */   public static final int PE_PRIN_LIST = 6;
/*      */   public static final int PE_PANEL1 = 7;
/*      */   public static final int PE_ADD_PERM_BUTTON = 0;
/*      */   public static final int PE_EDIT_PERM_BUTTON = 1;
/*      */   public static final int PE_REMOVE_PERM_BUTTON = 2;
/*      */   public static final int PE_PERM_LIST = 8;
/*      */   public static final int PE_PANEL2 = 9;
/*      */   public static final int PE_CANCEL_BUTTON = 1;
/*      */   public static final int PE_DONE_BUTTON = 0;
/*      */   public static final int PRD_DESC_LABEL = 0;
/*      */   public static final int PRD_PRIN_CHOICE = 1;
/*      */   public static final int PRD_PRIN_TEXTFIELD = 2;
/*      */   public static final int PRD_NAME_LABEL = 3;
/*      */   public static final int PRD_NAME_TEXTFIELD = 4;
/*      */   public static final int PRD_CANCEL_BUTTON = 6;
/*      */   public static final int PRD_OK_BUTTON = 5;
/*      */   public static final int PD_DESC_LABEL = 0;
/*      */   public static final int PD_PERM_CHOICE = 1;
/*      */   public static final int PD_PERM_TEXTFIELD = 2;
/*      */   public static final int PD_NAME_CHOICE = 3;
/*      */   public static final int PD_NAME_TEXTFIELD = 4;
/*      */   public static final int PD_ACTIONS_CHOICE = 5;
/*      */   public static final int PD_ACTIONS_TEXTFIELD = 6;
/*      */   public static final int PD_SIGNEDBY_LABEL = 7;
/*      */   public static final int PD_SIGNEDBY_TEXTFIELD = 8;
/*      */   public static final int PD_CANCEL_BUTTON = 10;
/*      */   public static final int PD_OK_BUTTON = 9;
/*      */   public static final int EDIT_KEYSTORE = 0;
/*      */   public static final int KSD_NAME_LABEL = 0;
/*      */   public static final int KSD_NAME_TEXTFIELD = 1;
/*      */   public static final int KSD_TYPE_LABEL = 2;
/*      */   public static final int KSD_TYPE_TEXTFIELD = 3;
/*      */   public static final int KSD_PROVIDER_LABEL = 4;
/*      */   public static final int KSD_PROVIDER_TEXTFIELD = 5;
/*      */   public static final int KSD_PWD_URL_LABEL = 6;
/*      */   public static final int KSD_PWD_URL_TEXTFIELD = 7;
/*      */   public static final int KSD_CANCEL_BUTTON = 9;
/*      */   public static final int KSD_OK_BUTTON = 8;
/*      */   public static final int USC_LABEL = 0;
/*      */   public static final int USC_PANEL = 1;
/*      */   public static final int USC_YES_BUTTON = 0;
/*      */   public static final int USC_NO_BUTTON = 1;
/*      */   public static final int USC_CANCEL_BUTTON = 2;
/*      */   public static final int CRPE_LABEL1 = 0;
/*      */   public static final int CRPE_LABEL2 = 1;
/*      */   public static final int CRPE_PANEL = 2;
/*      */   public static final int CRPE_PANEL_OK = 0;
/*      */   public static final int CRPE_PANEL_CANCEL = 1;
/*      */   private static final int PERMISSION = 0;
/*      */   private static final int PERMISSION_NAME = 1;
/*      */   private static final int PERMISSION_ACTIONS = 2;
/*      */   private static final int PERMISSION_SIGNEDBY = 3;
/*      */   private static final int PRINCIPAL_TYPE = 4;
/*      */   private static final int PRINCIPAL_NAME = 5;
/* 1439 */   public static ArrayList<Perm> PERM_ARRAY = new ArrayList();
/*      */   public static ArrayList<Prin> PRIN_ARRAY;
/*      */   PolicyTool tool;
/*      */   ToolWindow tw;
/*      */ 
/*      */   ToolDialog(String paramString, PolicyTool paramPolicyTool, ToolWindow paramToolWindow, boolean paramBoolean)
/*      */   {
/* 1473 */     super(paramToolWindow, paramBoolean);
/* 1474 */     setTitle(paramString);
/* 1475 */     this.tool = paramPolicyTool;
/* 1476 */     this.tw = paramToolWindow;
/* 1477 */     addWindowListener(new ChildWindowListener(this));
/*      */   }
/*      */ 
/*      */   static Perm getPerm(String paramString, boolean paramBoolean)
/*      */   {
/* 1485 */     for (int i = 0; i < PERM_ARRAY.size(); i++) {
/* 1486 */       Perm localPerm = (Perm)PERM_ARRAY.get(i);
/* 1487 */       if (paramBoolean) {
/* 1488 */         if (localPerm.FULL_CLASS.equals(paramString)) {
/* 1489 */           return localPerm;
/*      */         }
/*      */       }
/* 1492 */       else if (localPerm.CLASS.equals(paramString)) {
/* 1493 */         return localPerm;
/*      */       }
/*      */     }
/*      */ 
/* 1497 */     return null;
/*      */   }
/*      */ 
/*      */   static Prin getPrin(String paramString, boolean paramBoolean)
/*      */   {
/* 1505 */     for (int i = 0; i < PRIN_ARRAY.size(); i++) {
/* 1506 */       Prin localPrin = (Prin)PRIN_ARRAY.get(i);
/* 1507 */       if (paramBoolean) {
/* 1508 */         if (localPrin.FULL_CLASS.equals(paramString)) {
/* 1509 */           return localPrin;
/*      */         }
/*      */       }
/* 1512 */       else if (localPrin.CLASS.equals(paramString)) {
/* 1513 */         return localPrin;
/*      */       }
/*      */     }
/*      */ 
/* 1517 */     return null;
/*      */   }
/*      */ 
/*      */   void displayPolicyEntryDialog(boolean paramBoolean)
/*      */   {
/* 1534 */     int i = 0;
/* 1535 */     PolicyEntry[] arrayOfPolicyEntry = null;
/* 1536 */     TaggedList localTaggedList1 = new TaggedList(3, false);
/* 1537 */     localTaggedList1.getAccessibleContext().setAccessibleName(PolicyTool.rb.getString("Principal.List"));
/*      */ 
/* 1539 */     localTaggedList1.addActionListener(new EditPrinButtonListener(this.tool, this.tw, this, paramBoolean));
/*      */ 
/* 1541 */     TaggedList localTaggedList2 = new TaggedList(10, false);
/* 1542 */     localTaggedList2.getAccessibleContext().setAccessibleName(PolicyTool.rb.getString("Permission.List"));
/*      */ 
/* 1544 */     localTaggedList2.addActionListener(new EditPermButtonListener(this.tool, this.tw, this, paramBoolean));
/*      */ 
/* 1548 */     Point localPoint = this.tw.getLocationOnScreen();
/* 1549 */     setBounds(localPoint.x + 75, localPoint.y + 200, 650, 500);
/* 1550 */     setLayout(new GridBagLayout());
/* 1551 */     setResizable(true);
/*      */ 
/* 1553 */     if (paramBoolean)
/*      */     {
/* 1555 */       arrayOfPolicyEntry = this.tool.getEntry();
/* 1556 */       localObject1 = (List)this.tw.getComponent(3);
/* 1557 */       i = ((List)localObject1).getSelectedIndex();
/*      */ 
/* 1560 */       localObject2 = arrayOfPolicyEntry[i].getGrantEntry().principals;
/*      */       PolicyParser.PrincipalEntry localPrincipalEntry;
/* 1562 */       for (int j = 0; j < ((LinkedList)localObject2).size(); j++) {
/* 1563 */         Object localObject4 = null;
/* 1564 */         localPrincipalEntry = (PolicyParser.PrincipalEntry)((LinkedList)localObject2).get(j);
/*      */ 
/* 1566 */         localTaggedList1.addTaggedItem(PrincipalEntryToUserFriendlyString(localPrincipalEntry), localPrincipalEntry);
/*      */       }
/*      */ 
/* 1570 */       localObject3 = arrayOfPolicyEntry[i].getGrantEntry().permissionEntries;
/*      */ 
/* 1572 */       for (int k = 0; k < ((Vector)localObject3).size(); k++) {
/* 1573 */         localPrincipalEntry = null;
/* 1574 */         PolicyParser.PermissionEntry localPermissionEntry = (PolicyParser.PermissionEntry)((Vector)localObject3).elementAt(k);
/*      */ 
/* 1576 */         localTaggedList2.addTaggedItem(PermissionEntryToUserFriendlyString(localPermissionEntry), localPermissionEntry);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1581 */     Object localObject1 = new Label(PolicyTool.rb.getString("CodeBase."));
/* 1582 */     this.tw.addNewComponent(this, (Component)localObject1, 0, 0, 0, 1, 1, 0.0D, 0.0D, 1);
/*      */ 
/* 1585 */     Object localObject2 = paramBoolean ? new TextField(arrayOfPolicyEntry[i].getGrantEntry().codeBase, 60) : new TextField(60);
/*      */ 
/* 1588 */     ((TextField)localObject2).getAccessibleContext().setAccessibleName(PolicyTool.rb.getString("Code.Base"));
/*      */ 
/* 1590 */     this.tw.addNewComponent(this, (Component)localObject2, 1, 1, 0, 1, 1, 0.0D, 0.0D, 1);
/*      */ 
/* 1594 */     localObject1 = new Label(PolicyTool.rb.getString("SignedBy."));
/* 1595 */     this.tw.addNewComponent(this, (Component)localObject1, 2, 0, 1, 1, 1, 0.0D, 0.0D, 1);
/*      */ 
/* 1597 */     localObject2 = paramBoolean ? new TextField(arrayOfPolicyEntry[i].getGrantEntry().signedBy, 60) : new TextField(60);
/*      */ 
/* 1600 */     ((TextField)localObject2).getAccessibleContext().setAccessibleName(PolicyTool.rb.getString("Signed.By."));
/*      */ 
/* 1602 */     this.tw.addNewComponent(this, (Component)localObject2, 3, 1, 1, 1, 1, 0.0D, 0.0D, 1);
/*      */ 
/* 1606 */     Object localObject3 = new Panel();
/* 1607 */     ((Panel)localObject3).setLayout(new GridBagLayout());
/*      */ 
/* 1609 */     Button localButton = new Button(PolicyTool.rb.getString("Add.Principal"));
/* 1610 */     localButton.addActionListener(new AddPrinButtonListener(this.tool, this.tw, this, paramBoolean));
/*      */ 
/* 1612 */     this.tw.addNewComponent((Container)localObject3, localButton, 0, 0, 0, 1, 1, 100.0D, 0.0D, 2);
/*      */ 
/* 1615 */     localButton = new Button(PolicyTool.rb.getString("Edit.Principal"));
/* 1616 */     localButton.addActionListener(new EditPrinButtonListener(this.tool, this.tw, this, paramBoolean));
/*      */ 
/* 1618 */     this.tw.addNewComponent((Container)localObject3, localButton, 1, 1, 0, 1, 1, 100.0D, 0.0D, 2);
/*      */ 
/* 1621 */     localButton = new Button(PolicyTool.rb.getString("Remove.Principal"));
/* 1622 */     localButton.addActionListener(new RemovePrinButtonListener(this.tool, this.tw, this, paramBoolean));
/*      */ 
/* 1624 */     this.tw.addNewComponent((Container)localObject3, localButton, 2, 2, 0, 1, 1, 100.0D, 0.0D, 2);
/*      */ 
/* 1627 */     this.tw.addNewComponent(this, (Component)localObject3, 4, 1, 2, 1, 1, 0.0D, 0.0D, 2);
/*      */ 
/* 1631 */     localObject1 = new Label(PolicyTool.rb.getString("Principals."));
/* 1632 */     this.tw.addNewComponent(this, (Component)localObject1, 5, 0, 3, 1, 1, 0.0D, 0.0D, 1, ToolWindow.BOTTOM_PADDING);
/*      */ 
/* 1635 */     this.tw.addNewComponent(this, localTaggedList1, 6, 1, 3, 3, 1, 0.0D, 0.0D, 1, ToolWindow.BOTTOM_PADDING);
/*      */ 
/* 1640 */     localObject3 = new Panel();
/* 1641 */     ((Panel)localObject3).setLayout(new GridBagLayout());
/*      */ 
/* 1643 */     localButton = new Button(PolicyTool.rb.getString(".Add.Permission"));
/* 1644 */     localButton.addActionListener(new AddPermButtonListener(this.tool, this.tw, this, paramBoolean));
/*      */ 
/* 1646 */     this.tw.addNewComponent((Container)localObject3, localButton, 0, 0, 0, 1, 1, 100.0D, 0.0D, 2);
/*      */ 
/* 1649 */     localButton = new Button(PolicyTool.rb.getString(".Edit.Permission"));
/* 1650 */     localButton.addActionListener(new EditPermButtonListener(this.tool, this.tw, this, paramBoolean));
/*      */ 
/* 1652 */     this.tw.addNewComponent((Container)localObject3, localButton, 1, 1, 0, 1, 1, 100.0D, 0.0D, 2);
/*      */ 
/* 1656 */     localButton = new Button(PolicyTool.rb.getString("Remove.Permission"));
/* 1657 */     localButton.addActionListener(new RemovePermButtonListener(this.tool, this.tw, this, paramBoolean));
/*      */ 
/* 1659 */     this.tw.addNewComponent((Container)localObject3, localButton, 2, 2, 0, 1, 1, 100.0D, 0.0D, 2);
/*      */ 
/* 1662 */     this.tw.addNewComponent(this, (Component)localObject3, 7, 0, 4, 2, 1, 0.0D, 0.0D, 2, ToolWindow.LITE_BOTTOM_PADDING);
/*      */ 
/* 1667 */     this.tw.addNewComponent(this, localTaggedList2, 8, 0, 5, 3, 1, 0.0D, 0.0D, 1, ToolWindow.BOTTOM_PADDING);
/*      */ 
/* 1673 */     localObject3 = new Panel();
/* 1674 */     ((Panel)localObject3).setLayout(new GridBagLayout());
/*      */ 
/* 1677 */     localButton = new Button(PolicyTool.rb.getString("Done"));
/* 1678 */     localButton.addActionListener(new AddEntryDoneButtonListener(this.tool, this.tw, this, paramBoolean));
/*      */ 
/* 1680 */     this.tw.addNewComponent((Container)localObject3, localButton, 0, 0, 0, 1, 1, 0.0D, 0.0D, 3, ToolWindow.LR_PADDING);
/*      */ 
/* 1685 */     localButton = new Button(PolicyTool.rb.getString("Cancel"));
/* 1686 */     localButton.addActionListener(new CancelButtonListener(this));
/* 1687 */     this.tw.addNewComponent((Container)localObject3, localButton, 1, 1, 0, 1, 1, 0.0D, 0.0D, 3, ToolWindow.LR_PADDING);
/*      */ 
/* 1692 */     this.tw.addNewComponent(this, (Component)localObject3, 9, 0, 6, 2, 1, 0.0D, 0.0D, 3);
/*      */ 
/* 1695 */     setVisible(true);
/*      */   }
/*      */ 
/*      */   PolicyEntry getPolicyEntryFromDialog()
/*      */     throws InvalidParameterException, MalformedURLException, NoSuchMethodException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, CertificateException, IOException, Exception
/*      */   {
/* 1709 */     TextField localTextField = (TextField)getComponent(1);
/* 1710 */     String str1 = null;
/* 1711 */     if (!localTextField.getText().trim().equals("")) {
/* 1712 */       str1 = new String(localTextField.getText().trim());
/*      */     }
/*      */ 
/* 1715 */     localTextField = (TextField)getComponent(3);
/* 1716 */     String str2 = null;
/* 1717 */     if (!localTextField.getText().trim().equals("")) {
/* 1718 */       str2 = new String(localTextField.getText().trim());
/*      */     }
/*      */ 
/* 1721 */     PolicyParser.GrantEntry localGrantEntry = new PolicyParser.GrantEntry(str2, str1);
/*      */ 
/* 1725 */     LinkedList localLinkedList = new LinkedList();
/* 1726 */     TaggedList localTaggedList1 = (TaggedList)getComponent(6);
/* 1727 */     for (int i = 0; i < localTaggedList1.getItemCount(); i++) {
/* 1728 */       localLinkedList.add((PolicyParser.PrincipalEntry)localTaggedList1.getObject(i));
/*      */     }
/* 1730 */     localGrantEntry.principals = localLinkedList;
/*      */ 
/* 1733 */     Vector localVector = new Vector();
/* 1734 */     TaggedList localTaggedList2 = (TaggedList)getComponent(8);
/* 1735 */     for (int j = 0; j < localTaggedList2.getItemCount(); j++) {
/* 1736 */       localVector.addElement((PolicyParser.PermissionEntry)localTaggedList2.getObject(j));
/*      */     }
/* 1738 */     localGrantEntry.permissionEntries = localVector;
/*      */ 
/* 1741 */     PolicyEntry localPolicyEntry = new PolicyEntry(this.tool, localGrantEntry);
/*      */ 
/* 1743 */     return localPolicyEntry;
/*      */   }
/*      */ 
/*      */   void keyStoreDialog(int paramInt)
/*      */   {
/* 1752 */     Point localPoint = this.tw.getLocationOnScreen();
/* 1753 */     setBounds(localPoint.x + 25, localPoint.y + 100, 500, 300);
/* 1754 */     setLayout(new GridBagLayout());
/*      */ 
/* 1756 */     if (paramInt == 0)
/*      */     {
/* 1759 */       Label localLabel = new Label(PolicyTool.rb.getString("KeyStore.URL."));
/*      */ 
/* 1761 */       this.tw.addNewComponent(this, localLabel, 0, 0, 0, 1, 1, 0.0D, 0.0D, 1, ToolWindow.BOTTOM_PADDING);
/*      */ 
/* 1764 */       TextField localTextField = new TextField(this.tool.getKeyStoreName(), 30);
/*      */ 
/* 1767 */       localTextField.getAccessibleContext().setAccessibleName(PolicyTool.rb.getString("KeyStore.U.R.L."));
/*      */ 
/* 1769 */       this.tw.addNewComponent(this, localTextField, 1, 1, 0, 1, 1, 0.0D, 0.0D, 1, ToolWindow.BOTTOM_PADDING);
/*      */ 
/* 1774 */       localLabel = new Label(PolicyTool.rb.getString("KeyStore.Type."));
/* 1775 */       this.tw.addNewComponent(this, localLabel, 2, 0, 1, 1, 1, 0.0D, 0.0D, 1, ToolWindow.BOTTOM_PADDING);
/*      */ 
/* 1778 */       localTextField = new TextField(this.tool.getKeyStoreType(), 30);
/* 1779 */       localTextField.getAccessibleContext().setAccessibleName(PolicyTool.rb.getString("KeyStore.Type."));
/*      */ 
/* 1781 */       this.tw.addNewComponent(this, localTextField, 3, 1, 1, 1, 1, 0.0D, 0.0D, 1, ToolWindow.BOTTOM_PADDING);
/*      */ 
/* 1786 */       localLabel = new Label(PolicyTool.rb.getString("KeyStore.Provider."));
/*      */ 
/* 1788 */       this.tw.addNewComponent(this, localLabel, 4, 0, 2, 1, 1, 0.0D, 0.0D, 1, ToolWindow.BOTTOM_PADDING);
/*      */ 
/* 1791 */       localTextField = new TextField(this.tool.getKeyStoreProvider(), 30);
/* 1792 */       localTextField.getAccessibleContext().setAccessibleName(PolicyTool.rb.getString("KeyStore.Provider."));
/*      */ 
/* 1794 */       this.tw.addNewComponent(this, localTextField, 5, 1, 2, 1, 1, 0.0D, 0.0D, 1, ToolWindow.BOTTOM_PADDING);
/*      */ 
/* 1799 */       localLabel = new Label(PolicyTool.rb.getString("KeyStore.Password.URL."));
/*      */ 
/* 1801 */       this.tw.addNewComponent(this, localLabel, 6, 0, 3, 1, 1, 0.0D, 0.0D, 1, ToolWindow.BOTTOM_PADDING);
/*      */ 
/* 1804 */       localTextField = new TextField(this.tool.getKeyStorePwdURL(), 30);
/* 1805 */       localTextField.getAccessibleContext().setAccessibleName(PolicyTool.rb.getString("KeyStore.Password.U.R.L."));
/*      */ 
/* 1807 */       this.tw.addNewComponent(this, localTextField, 7, 1, 3, 1, 1, 0.0D, 0.0D, 1, ToolWindow.BOTTOM_PADDING);
/*      */ 
/* 1812 */       Button localButton1 = new Button(PolicyTool.rb.getString("OK"));
/* 1813 */       localButton1.addActionListener(new ChangeKeyStoreOKButtonListener(this.tool, this.tw, this));
/*      */ 
/* 1815 */       this.tw.addNewComponent(this, localButton1, 8, 0, 4, 1, 1, 0.0D, 0.0D, 3);
/*      */ 
/* 1819 */       Button localButton2 = new Button(PolicyTool.rb.getString("Cancel"));
/* 1820 */       localButton2.addActionListener(new CancelButtonListener(this));
/* 1821 */       this.tw.addNewComponent(this, localButton2, 9, 1, 4, 1, 1, 0.0D, 0.0D, 3);
/*      */     }
/*      */ 
/* 1825 */     setVisible(true);
/*      */   }
/*      */ 
/*      */   void displayPrincipalDialog(boolean paramBoolean1, boolean paramBoolean2)
/*      */   {
/* 1839 */     PolicyParser.PrincipalEntry localPrincipalEntry = null;
/*      */ 
/* 1842 */     TaggedList localTaggedList = (TaggedList)getComponent(6);
/* 1843 */     int i = localTaggedList.getSelectedIndex();
/*      */ 
/* 1845 */     if (paramBoolean2) {
/* 1846 */       localPrincipalEntry = (PolicyParser.PrincipalEntry)localTaggedList.getObject(i);
/*      */     }
/*      */ 
/* 1849 */     ToolDialog localToolDialog = new ToolDialog(PolicyTool.rb.getString("Principals"), this.tool, this.tw, true);
/*      */ 
/* 1851 */     localToolDialog.addWindowListener(new ChildWindowListener(localToolDialog));
/*      */ 
/* 1854 */     Point localPoint = getLocationOnScreen();
/* 1855 */     localToolDialog.setBounds(localPoint.x + 50, localPoint.y + 100, 650, 190);
/* 1856 */     localToolDialog.setLayout(new GridBagLayout());
/* 1857 */     localToolDialog.setResizable(true);
/*      */ 
/* 1860 */     Label localLabel = paramBoolean2 ? new Label(PolicyTool.rb.getString(".Edit.Principal.")) : new Label(PolicyTool.rb.getString(".Add.New.Principal."));
/*      */ 
/* 1863 */     this.tw.addNewComponent(localToolDialog, localLabel, 0, 0, 0, 1, 1, 0.0D, 0.0D, 1, ToolWindow.TOP_BOTTOM_PADDING);
/*      */ 
/* 1868 */     Choice localChoice = new Choice();
/* 1869 */     localChoice.add(PRIN_TYPE);
/* 1870 */     localChoice.getAccessibleContext().setAccessibleName(PRIN_TYPE);
/* 1871 */     for (int j = 0; j < PRIN_ARRAY.size(); j++) {
/* 1872 */       localObject2 = (Prin)PRIN_ARRAY.get(j);
/* 1873 */       localChoice.add(((Prin)localObject2).CLASS);
/*      */     }
/*      */ 
/* 1876 */     localChoice.addItemListener(new PrincipalTypeMenuListener(localToolDialog));
/* 1877 */     if (paramBoolean2) {
/* 1878 */       if ("WILDCARD_PRINCIPAL_CLASS".equals(localPrincipalEntry.getPrincipalClass()))
/*      */       {
/* 1880 */         localChoice.select(PRIN_TYPE);
/*      */       } else {
/* 1882 */         localObject1 = getPrin(localPrincipalEntry.getPrincipalClass(), true);
/* 1883 */         if (localObject1 != null) {
/* 1884 */           localChoice.select(((Prin)localObject1).CLASS);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1889 */     this.tw.addNewComponent(localToolDialog, localChoice, 1, 0, 1, 1, 1, 0.0D, 0.0D, 1, ToolWindow.LR_PADDING);
/*      */ 
/* 1895 */     Object localObject1 = paramBoolean2 ? new TextField(localPrincipalEntry.getDisplayClass(), 30) : new TextField(30);
/*      */ 
/* 1898 */     ((TextField)localObject1).getAccessibleContext().setAccessibleName(PRIN_TYPE);
/* 1899 */     this.tw.addNewComponent(localToolDialog, (Component)localObject1, 2, 1, 1, 1, 1, 0.0D, 0.0D, 1, ToolWindow.LR_PADDING);
/*      */ 
/* 1904 */     localLabel = new Label(PRIN_NAME);
/* 1905 */     localObject1 = paramBoolean2 ? new TextField(localPrincipalEntry.getDisplayName(), 40) : new TextField(40);
/*      */ 
/* 1908 */     ((TextField)localObject1).getAccessibleContext().setAccessibleName(PRIN_NAME);
/*      */ 
/* 1910 */     this.tw.addNewComponent(localToolDialog, localLabel, 3, 0, 2, 1, 1, 0.0D, 0.0D, 1, ToolWindow.LR_PADDING);
/*      */ 
/* 1913 */     this.tw.addNewComponent(localToolDialog, (Component)localObject1, 4, 1, 2, 1, 1, 0.0D, 0.0D, 1, ToolWindow.LR_PADDING);
/*      */ 
/* 1918 */     Object localObject2 = new Button(PolicyTool.rb.getString("OK"));
/* 1919 */     ((Button)localObject2).addActionListener(new NewPolicyPrinOKButtonListener(this.tool, this.tw, this, localToolDialog, paramBoolean2));
/*      */ 
/* 1922 */     this.tw.addNewComponent(localToolDialog, (Component)localObject2, 5, 0, 3, 1, 1, 0.0D, 0.0D, 3, ToolWindow.TOP_BOTTOM_PADDING);
/*      */ 
/* 1926 */     Button localButton = new Button(PolicyTool.rb.getString("Cancel"));
/* 1927 */     localButton.addActionListener(new CancelButtonListener(localToolDialog));
/* 1928 */     this.tw.addNewComponent(localToolDialog, localButton, 6, 1, 3, 1, 1, 0.0D, 0.0D, 3, ToolWindow.TOP_BOTTOM_PADDING);
/*      */ 
/* 1932 */     localToolDialog.setVisible(true);
/*      */   }
/*      */ 
/*      */   void displayPermissionDialog(boolean paramBoolean1, boolean paramBoolean2)
/*      */   {
/* 1946 */     PolicyParser.PermissionEntry localPermissionEntry = null;
/*      */ 
/* 1949 */     TaggedList localTaggedList = (TaggedList)getComponent(8);
/* 1950 */     int i = localTaggedList.getSelectedIndex();
/*      */ 
/* 1952 */     if (paramBoolean2) {
/* 1953 */       localPermissionEntry = (PolicyParser.PermissionEntry)localTaggedList.getObject(i);
/*      */     }
/*      */ 
/* 1956 */     ToolDialog localToolDialog = new ToolDialog(PolicyTool.rb.getString("Permissions"), this.tool, this.tw, true);
/*      */ 
/* 1958 */     localToolDialog.addWindowListener(new ChildWindowListener(localToolDialog));
/*      */ 
/* 1961 */     Point localPoint = getLocationOnScreen();
/* 1962 */     localToolDialog.setBounds(localPoint.x + 50, localPoint.y + 100, 700, 250);
/* 1963 */     localToolDialog.setLayout(new GridBagLayout());
/* 1964 */     localToolDialog.setResizable(true);
/*      */ 
/* 1967 */     Label localLabel = paramBoolean2 ? new Label(PolicyTool.rb.getString(".Edit.Permission.")) : new Label(PolicyTool.rb.getString(".Add.New.Permission."));
/*      */ 
/* 1970 */     this.tw.addNewComponent(localToolDialog, localLabel, 0, 0, 0, 1, 1, 0.0D, 0.0D, 1, ToolWindow.TOP_BOTTOM_PADDING);
/*      */ 
/* 1975 */     Choice localChoice = new Choice();
/* 1976 */     localChoice.add(PERM);
/* 1977 */     localChoice.getAccessibleContext().setAccessibleName(PERM);
/* 1978 */     for (int j = 0; j < PERM_ARRAY.size(); j++) {
/* 1979 */       localObject = (Perm)PERM_ARRAY.get(j);
/* 1980 */       localChoice.add(((Perm)localObject).CLASS);
/*      */     }
/* 1982 */     localChoice.addItemListener(new PermissionMenuListener(localToolDialog));
/* 1983 */     this.tw.addNewComponent(localToolDialog, localChoice, 1, 0, 1, 1, 1, 0.0D, 0.0D, 1, ToolWindow.LR_PADDING);
/*      */ 
/* 1989 */     TextField localTextField = paramBoolean2 ? new TextField(localPermissionEntry.permission, 30) : new TextField(30);
/* 1990 */     localTextField.getAccessibleContext().setAccessibleName(PERM);
/* 1991 */     if (paramBoolean2) {
/* 1992 */       localObject = getPerm(localPermissionEntry.permission, true);
/* 1993 */       if (localObject != null) {
/* 1994 */         localChoice.select(((Perm)localObject).CLASS);
/*      */       }
/*      */     }
/* 1997 */     this.tw.addNewComponent(localToolDialog, localTextField, 2, 1, 1, 1, 1, 0.0D, 0.0D, 1, ToolWindow.LR_PADDING);
/*      */ 
/* 2002 */     localChoice = new Choice();
/* 2003 */     localChoice.add(PERM_NAME);
/* 2004 */     localChoice.getAccessibleContext().setAccessibleName(PERM_NAME);
/* 2005 */     localChoice.addItemListener(new PermissionNameMenuListener(localToolDialog));
/* 2006 */     localTextField = paramBoolean2 ? new TextField(localPermissionEntry.name, 40) : new TextField(40);
/* 2007 */     localTextField.getAccessibleContext().setAccessibleName(PERM_NAME);
/* 2008 */     if (paramBoolean2) {
/* 2009 */       setPermissionNames(getPerm(localPermissionEntry.permission, true), localChoice, localTextField);
/*      */     }
/* 2011 */     this.tw.addNewComponent(localToolDialog, localChoice, 3, 0, 2, 1, 1, 0.0D, 0.0D, 1, ToolWindow.LR_PADDING);
/*      */ 
/* 2014 */     this.tw.addNewComponent(localToolDialog, localTextField, 4, 1, 2, 1, 1, 0.0D, 0.0D, 1, ToolWindow.LR_PADDING);
/*      */ 
/* 2019 */     localChoice = new Choice();
/* 2020 */     localChoice.add(PERM_ACTIONS);
/* 2021 */     localChoice.getAccessibleContext().setAccessibleName(PERM_ACTIONS);
/* 2022 */     localChoice.addItemListener(new PermissionActionsMenuListener(localToolDialog));
/* 2023 */     localTextField = paramBoolean2 ? new TextField(localPermissionEntry.action, 40) : new TextField(40);
/* 2024 */     localTextField.getAccessibleContext().setAccessibleName(PERM_ACTIONS);
/* 2025 */     if (paramBoolean2) {
/* 2026 */       setPermissionActions(getPerm(localPermissionEntry.permission, true), localChoice, localTextField);
/*      */     }
/* 2028 */     this.tw.addNewComponent(localToolDialog, localChoice, 5, 0, 3, 1, 1, 0.0D, 0.0D, 1, ToolWindow.LR_PADDING);
/*      */ 
/* 2031 */     this.tw.addNewComponent(localToolDialog, localTextField, 6, 1, 3, 1, 1, 0.0D, 0.0D, 1, ToolWindow.LR_PADDING);
/*      */ 
/* 2036 */     localLabel = new Label(PolicyTool.rb.getString("Signed.By."));
/* 2037 */     this.tw.addNewComponent(localToolDialog, localLabel, 7, 0, 4, 1, 1, 0.0D, 0.0D, 1, ToolWindow.LR_PADDING);
/*      */ 
/* 2040 */     localTextField = paramBoolean2 ? new TextField(localPermissionEntry.signedBy, 40) : new TextField(40);
/* 2041 */     localTextField.getAccessibleContext().setAccessibleName(PolicyTool.rb.getString("Signed.By."));
/*      */ 
/* 2043 */     this.tw.addNewComponent(localToolDialog, localTextField, 8, 1, 4, 1, 1, 0.0D, 0.0D, 1, ToolWindow.LR_PADDING);
/*      */ 
/* 2048 */     Object localObject = new Button(PolicyTool.rb.getString("OK"));
/* 2049 */     ((Button)localObject).addActionListener(new NewPolicyPermOKButtonListener(this.tool, this.tw, this, localToolDialog, paramBoolean2));
/*      */ 
/* 2052 */     this.tw.addNewComponent(localToolDialog, (Component)localObject, 9, 0, 5, 1, 1, 0.0D, 0.0D, 3, ToolWindow.TOP_BOTTOM_PADDING);
/*      */ 
/* 2057 */     Button localButton = new Button(PolicyTool.rb.getString("Cancel"));
/* 2058 */     localButton.addActionListener(new CancelButtonListener(localToolDialog));
/* 2059 */     this.tw.addNewComponent(localToolDialog, localButton, 10, 1, 5, 1, 1, 0.0D, 0.0D, 3, ToolWindow.TOP_BOTTOM_PADDING);
/*      */ 
/* 2063 */     localToolDialog.setVisible(true);
/*      */   }
/*      */ 
/*      */   PolicyParser.PrincipalEntry getPrinFromDialog()
/*      */     throws Exception
/*      */   {
/* 2071 */     TextField localTextField = (TextField)getComponent(2);
/* 2072 */     String str1 = new String(localTextField.getText().trim());
/* 2073 */     localTextField = (TextField)getComponent(4);
/* 2074 */     String str2 = new String(localTextField.getText().trim());
/* 2075 */     if (str1.equals("*")) {
/* 2076 */       str1 = "WILDCARD_PRINCIPAL_CLASS";
/*      */     }
/* 2078 */     if (str2.equals("*")) {
/* 2079 */       str2 = "WILDCARD_PRINCIPAL_NAME";
/*      */     }
/*      */ 
/* 2082 */     Object localObject = null;
/*      */ 
/* 2084 */     if ((str1.equals("WILDCARD_PRINCIPAL_CLASS")) && (!str2.equals("WILDCARD_PRINCIPAL_NAME")))
/*      */     {
/* 2086 */       throw new Exception(PolicyTool.rb.getString("Cannot.Specify.Principal.with.a.Wildcard.Class.without.a.Wildcard.Name"));
/*      */     }
/* 2088 */     if (str2.equals("")) {
/* 2089 */       throw new Exception(PolicyTool.rb.getString("Cannot.Specify.Principal.without.a.Name"));
/*      */     }
/* 2091 */     if (str1.equals(""))
/*      */     {
/* 2094 */       str1 = "PolicyParser.REPLACE_NAME";
/* 2095 */       this.tool.warnings.addElement("Warning: Principal name '" + str2 + "' specified without a Principal class.\n" + "\t'" + str2 + "' will be interpreted " + "as a key store alias.\n" + "\tThe final principal class will be " + "javax.security.auth.x500.X500Principal" + ".\n" + "\tThe final principal name will be " + "determined by the following:\n" + "\n" + "\tIf the key store entry identified by '" + str2 + "'\n" + "\tis a key entry, then the principal name will be\n" + "\tthe subject distinguished name from the first\n" + "\tcertificate in the entry's certificate chain.\n" + "\n" + "\tIf the key store entry identified by '" + str2 + "'\n" + "\tis a trusted certificate entry, then the\n" + "\tprincipal name will be the subject distinguished\n" + "\tname from the trusted public key certificate.");
/*      */ 
/* 2116 */       this.tw.displayStatusDialog(this, "'" + str2 + "' will be interpreted as a key " + "store alias.  View Warning Log for details.");
/*      */     }
/*      */ 
/* 2120 */     return new PolicyParser.PrincipalEntry(str1, str2);
/*      */   }
/*      */ 
/*      */   PolicyParser.PermissionEntry getPermFromDialog()
/*      */   {
/* 2129 */     TextField localTextField = (TextField)getComponent(2);
/* 2130 */     String str1 = new String(localTextField.getText().trim());
/* 2131 */     localTextField = (TextField)getComponent(4);
/* 2132 */     String str2 = null;
/* 2133 */     if (!localTextField.getText().trim().equals(""))
/* 2134 */       str2 = new String(localTextField.getText().trim());
/* 2135 */     if ((str1.equals("")) || ((!str1.equals("java.security.AllPermission")) && (str2 == null)))
/*      */     {
/* 2137 */       throw new InvalidParameterException(PolicyTool.rb.getString("Permission.and.Target.Name.must.have.a.value"));
/*      */     }
/*      */ 
/* 2151 */     if ((str1.equals("java.io.FilePermission")) && (str2.lastIndexOf("\\\\") > 0)) {
/* 2152 */       int i = this.tw.displayYesNoDialog(this, PolicyTool.rb.getString("Warning"), PolicyTool.rb.getString("Warning.File.name.may.include.escaped.backslash.characters.It.is.not.necessary.to.escape.backslash.characters.the.tool.escapes"), PolicyTool.rb.getString("Retain"), PolicyTool.rb.getString("Edit"));
/*      */ 
/* 2159 */       if (i != 89)
/*      */       {
/* 2161 */         throw new NoDisplayException();
/*      */       }
/*      */     }
/*      */ 
/* 2165 */     localTextField = (TextField)getComponent(6);
/* 2166 */     String str3 = null;
/* 2167 */     if (!localTextField.getText().trim().equals("")) {
/* 2168 */       str3 = new String(localTextField.getText().trim());
/*      */     }
/*      */ 
/* 2171 */     localTextField = (TextField)getComponent(8);
/* 2172 */     String str4 = null;
/* 2173 */     if (!localTextField.getText().trim().equals("")) {
/* 2174 */       str4 = new String(localTextField.getText().trim());
/*      */     }
/* 2176 */     PolicyParser.PermissionEntry localPermissionEntry = new PolicyParser.PermissionEntry(str1, str2, str3);
/*      */ 
/* 2178 */     localPermissionEntry.signedBy = str4;
/*      */ 
/* 2181 */     if (str4 != null) {
/* 2182 */       String[] arrayOfString = this.tool.parseSigners(localPermissionEntry.signedBy);
/* 2183 */       for (int j = 0; j < arrayOfString.length; j++) {
/*      */         try {
/* 2185 */           PublicKey localPublicKey = this.tool.getPublicKeyAlias(arrayOfString[j]);
/* 2186 */           if (localPublicKey == null) {
/* 2187 */             MessageFormat localMessageFormat = new MessageFormat(PolicyTool.rb.getString("Warning.A.public.key.for.alias.signers.i.does.not.exist.Make.sure.a.KeyStore.is.properly.configured."));
/*      */ 
/* 2190 */             Object[] arrayOfObject = { arrayOfString[j] };
/* 2191 */             this.tool.warnings.addElement(localMessageFormat.format(arrayOfObject));
/* 2192 */             this.tw.displayStatusDialog(this, localMessageFormat.format(arrayOfObject));
/*      */           }
/*      */         } catch (Exception localException) {
/* 2195 */           this.tw.displayErrorDialog(this, localException);
/*      */         }
/*      */       }
/*      */     }
/* 2199 */     return localPermissionEntry;
/*      */   }
/*      */ 
/*      */   void displayConfirmRemovePolicyEntry()
/*      */   {
/* 2208 */     List localList = (List)this.tw.getComponent(3);
/* 2209 */     int i = localList.getSelectedIndex();
/* 2210 */     PolicyEntry[] arrayOfPolicyEntry = this.tool.getEntry();
/*      */ 
/* 2213 */     Point localPoint = this.tw.getLocationOnScreen();
/* 2214 */     setBounds(localPoint.x + 25, localPoint.y + 100, 600, 400);
/* 2215 */     setLayout(new GridBagLayout());
/*      */ 
/* 2218 */     Label localLabel = new Label(PolicyTool.rb.getString("Remove.this.Policy.Entry."));
/*      */ 
/* 2220 */     this.tw.addNewComponent(this, localLabel, 0, 0, 0, 2, 1, 0.0D, 0.0D, 1, ToolWindow.BOTTOM_PADDING);
/*      */ 
/* 2225 */     localLabel = new Label(arrayOfPolicyEntry[i].codebaseToString());
/* 2226 */     this.tw.addNewComponent(this, localLabel, 1, 0, 1, 2, 1, 0.0D, 0.0D, 1);
/*      */ 
/* 2228 */     localLabel = new Label(arrayOfPolicyEntry[i].principalsToString().trim());
/* 2229 */     this.tw.addNewComponent(this, localLabel, 2, 0, 2, 2, 1, 0.0D, 0.0D, 1);
/*      */ 
/* 2231 */     Vector localVector = arrayOfPolicyEntry[i].getGrantEntry().permissionEntries;
/*      */ 
/* 2233 */     for (int j = 0; j < localVector.size(); j++) {
/* 2234 */       localObject1 = (PolicyParser.PermissionEntry)localVector.elementAt(j);
/* 2235 */       localObject2 = PermissionEntryToUserFriendlyString((PolicyParser.PermissionEntry)localObject1);
/* 2236 */       localLabel = new Label("    " + (String)localObject2);
/* 2237 */       if (j == localVector.size() - 1) {
/* 2238 */         this.tw.addNewComponent(this, localLabel, 3 + j, 1, 3 + j, 1, 1, 0.0D, 0.0D, 1, ToolWindow.BOTTOM_PADDING);
/*      */       }
/*      */       else
/*      */       {
/* 2242 */         this.tw.addNewComponent(this, localLabel, 3 + j, 1, 3 + j, 1, 1, 0.0D, 0.0D, 1);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2250 */     Panel localPanel = new Panel();
/* 2251 */     localPanel.setLayout(new GridBagLayout());
/*      */ 
/* 2254 */     Object localObject1 = new Button(PolicyTool.rb.getString("OK"));
/* 2255 */     ((Button)localObject1).addActionListener(new ConfirmRemovePolicyEntryOKButtonListener(this.tool, this.tw, this));
/*      */ 
/* 2257 */     this.tw.addNewComponent(localPanel, (Component)localObject1, 0, 0, 0, 1, 1, 0.0D, 0.0D, 3, ToolWindow.LR_PADDING);
/*      */ 
/* 2262 */     Object localObject2 = new Button(PolicyTool.rb.getString("Cancel"));
/* 2263 */     ((Button)localObject2).addActionListener(new CancelButtonListener(this));
/* 2264 */     this.tw.addNewComponent(localPanel, (Component)localObject2, 1, 1, 0, 1, 1, 0.0D, 0.0D, 3, ToolWindow.LR_PADDING);
/*      */ 
/* 2268 */     this.tw.addNewComponent(this, localPanel, 3 + localVector.size(), 0, 3 + localVector.size(), 2, 1, 0.0D, 0.0D, 3, ToolWindow.TOP_BOTTOM_PADDING);
/*      */ 
/* 2272 */     pack();
/* 2273 */     setVisible(true);
/*      */   }
/*      */ 
/*      */   void displaySaveAsDialog(int paramInt)
/*      */   {
/* 2282 */     FileDialog localFileDialog = new FileDialog(this.tw, PolicyTool.rb.getString("Save.As"), 1);
/*      */ 
/* 2284 */     localFileDialog.addWindowListener(new WindowAdapter() {
/*      */       public void windowClosing(WindowEvent paramAnonymousWindowEvent) {
/* 2286 */         paramAnonymousWindowEvent.getWindow().setVisible(false);
/*      */       }
/*      */     });
/* 2289 */     localFileDialog.setVisible(true);
/*      */ 
/* 2292 */     if ((localFileDialog.getFile() == null) || (localFileDialog.getFile().equals("")))
/*      */     {
/* 2294 */       return;
/*      */     }
/*      */ 
/* 2297 */     File localFile = new File(localFileDialog.getDirectory(), localFileDialog.getFile());
/* 2298 */     String str = localFile.getPath();
/* 2299 */     localFileDialog.dispose();
/*      */     try
/*      */     {
/* 2303 */       this.tool.savePolicy(str);
/*      */ 
/* 2306 */       MessageFormat localMessageFormat = new MessageFormat(PolicyTool.rb.getString("Policy.successfully.written.to.filename"));
/*      */ 
/* 2308 */       Object[] arrayOfObject = { str };
/* 2309 */       this.tw.displayStatusDialog(null, localMessageFormat.format(arrayOfObject));
/*      */ 
/* 2312 */       TextField localTextField = (TextField)this.tw.getComponent(1);
/*      */ 
/* 2314 */       localTextField.setText(str);
/* 2315 */       this.tw.setVisible(true);
/*      */ 
/* 2319 */       userSaveContinue(this.tool, this.tw, this, paramInt);
/*      */     }
/*      */     catch (FileNotFoundException localFileNotFoundException) {
/* 2322 */       if ((str == null) || (str.equals(""))) {
/* 2323 */         this.tw.displayErrorDialog(null, new FileNotFoundException(PolicyTool.rb.getString("null.filename")));
/*      */       }
/*      */       else
/* 2326 */         this.tw.displayErrorDialog(null, localFileNotFoundException);
/*      */     }
/*      */     catch (Exception localException) {
/* 2329 */       this.tw.displayErrorDialog(null, localException);
/*      */     }
/*      */   }
/*      */ 
/*      */   void displayUserSave(int paramInt)
/*      */   {
/* 2338 */     if (this.tool.modified == true)
/*      */     {
/* 2341 */       Point localPoint = this.tw.getLocationOnScreen();
/* 2342 */       setBounds(localPoint.x + 75, localPoint.y + 100, 400, 150);
/* 2343 */       setLayout(new GridBagLayout());
/*      */ 
/* 2345 */       Label localLabel = new Label(PolicyTool.rb.getString("Save.changes."));
/*      */ 
/* 2347 */       this.tw.addNewComponent(this, localLabel, 0, 0, 0, 3, 1, 0.0D, 0.0D, 1, ToolWindow.L_TOP_BOTTOM_PADDING);
/*      */ 
/* 2351 */       Panel localPanel = new Panel();
/* 2352 */       localPanel.setLayout(new GridBagLayout());
/*      */ 
/* 2354 */       Button localButton1 = new Button(PolicyTool.rb.getString("Yes"));
/* 2355 */       localButton1.addActionListener(new UserSaveYesButtonListener(this, this.tool, this.tw, paramInt));
/*      */ 
/* 2357 */       this.tw.addNewComponent(localPanel, localButton1, 0, 0, 0, 1, 1, 0.0D, 0.0D, 3, ToolWindow.LR_BOTTOM_PADDING);
/*      */ 
/* 2361 */       Button localButton2 = new Button(PolicyTool.rb.getString("No"));
/* 2362 */       localButton2.addActionListener(new UserSaveNoButtonListener(this, this.tool, this.tw, paramInt));
/*      */ 
/* 2364 */       this.tw.addNewComponent(localPanel, localButton2, 1, 1, 0, 1, 1, 0.0D, 0.0D, 3, ToolWindow.LR_BOTTOM_PADDING);
/*      */ 
/* 2368 */       Button localButton3 = new Button(PolicyTool.rb.getString("Cancel"));
/* 2369 */       localButton3.addActionListener(new UserSaveCancelButtonListener(this));
/*      */ 
/* 2371 */       this.tw.addNewComponent(localPanel, localButton3, 2, 2, 0, 1, 1, 0.0D, 0.0D, 3, ToolWindow.LR_BOTTOM_PADDING);
/*      */ 
/* 2376 */       this.tw.addNewComponent(this, localPanel, 1, 0, 1, 1, 1, 0.0D, 0.0D, 1);
/*      */ 
/* 2379 */       pack();
/* 2380 */       setVisible(true);
/*      */     }
/*      */     else {
/* 2383 */       userSaveContinue(this.tool, this.tw, this, paramInt);
/*      */     }
/*      */   }
/*      */ 
/*      */   void userSaveContinue(PolicyTool paramPolicyTool, ToolWindow paramToolWindow, ToolDialog paramToolDialog, int paramInt)
/*      */   {
/*      */     List localList;
/*      */     TextField localTextField;
/* 2398 */     switch (paramInt)
/*      */     {
/*      */     case 1:
/* 2401 */       paramToolWindow.setVisible(false);
/* 2402 */       paramToolWindow.dispose();
/* 2403 */       System.exit(0);
/*      */     case 2:
/*      */       try
/*      */       {
/* 2408 */         paramPolicyTool.openPolicy(null);
/*      */       } catch (Exception localException1) {
/* 2410 */         paramPolicyTool.modified = false;
/* 2411 */         paramToolWindow.displayErrorDialog(null, localException1);
/*      */       }
/*      */ 
/* 2415 */       localList = new List(40, false);
/* 2416 */       localList.addActionListener(new PolicyListListener(paramPolicyTool, paramToolWindow));
/* 2417 */       paramToolWindow.replacePolicyList(localList);
/*      */ 
/* 2420 */       localTextField = (TextField)paramToolWindow.getComponent(1);
/*      */ 
/* 2422 */       localTextField.setText("");
/* 2423 */       paramToolWindow.setVisible(true);
/* 2424 */       break;
/*      */     case 3:
/* 2429 */       FileDialog localFileDialog = new FileDialog(paramToolWindow, PolicyTool.rb.getString("Open"), 0);
/*      */ 
/* 2431 */       localFileDialog.addWindowListener(new WindowAdapter() {
/*      */         public void windowClosing(WindowEvent paramAnonymousWindowEvent) {
/* 2433 */           paramAnonymousWindowEvent.getWindow().setVisible(false);
/*      */         }
/*      */       });
/* 2436 */       localFileDialog.setVisible(true);
/*      */ 
/* 2439 */       if ((localFileDialog.getFile() == null) || (localFileDialog.getFile().equals("")))
/*      */       {
/* 2441 */         return;
/*      */       }
/*      */ 
/* 2444 */       String str = new File(localFileDialog.getDirectory(), localFileDialog.getFile()).getPath();
/*      */       try
/*      */       {
/* 2448 */         paramPolicyTool.openPolicy(str);
/*      */ 
/* 2451 */         localList = new List(40, false);
/* 2452 */         localList.addActionListener(new PolicyListListener(paramPolicyTool, paramToolWindow));
/* 2453 */         PolicyEntry[] arrayOfPolicyEntry = paramPolicyTool.getEntry();
/* 2454 */         if (arrayOfPolicyEntry != null) {
/* 2455 */           for (int i = 0; i < arrayOfPolicyEntry.length; i++)
/* 2456 */             localList.add(arrayOfPolicyEntry[i].headerToString());
/*      */         }
/* 2458 */         paramToolWindow.replacePolicyList(localList);
/* 2459 */         paramPolicyTool.modified = false;
/*      */ 
/* 2462 */         localTextField = (TextField)paramToolWindow.getComponent(1);
/*      */ 
/* 2464 */         localTextField.setText(str);
/* 2465 */         paramToolWindow.setVisible(true);
/*      */ 
/* 2468 */         if (paramPolicyTool.newWarning == true) {
/* 2469 */           paramToolWindow.displayStatusDialog(null, PolicyTool.rb.getString("Errors.have.occurred.while.opening.the.policy.configuration.View.the.Warning.Log.for.more.information."));
/*      */         }
/*      */ 
/*      */       }
/*      */       catch (Exception localException2)
/*      */       {
/* 2475 */         localList = new List(40, false);
/* 2476 */         localList.addActionListener(new PolicyListListener(paramPolicyTool, paramToolWindow));
/* 2477 */         paramToolWindow.replacePolicyList(localList);
/* 2478 */         paramPolicyTool.setPolicyFileName(null);
/* 2479 */         paramPolicyTool.modified = false;
/*      */ 
/* 2482 */         localTextField = (TextField)paramToolWindow.getComponent(1);
/*      */ 
/* 2484 */         localTextField.setText("");
/* 2485 */         paramToolWindow.setVisible(true);
/*      */ 
/* 2488 */         MessageFormat localMessageFormat = new MessageFormat(PolicyTool.rb.getString("Could.not.open.policy.file.policyFile.e.toString."));
/*      */ 
/* 2490 */         Object[] arrayOfObject = { str, localException2.toString() };
/* 2491 */         paramToolWindow.displayErrorDialog(null, localMessageFormat.format(arrayOfObject));
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   void setPermissionNames(Perm paramPerm, Choice paramChoice, TextField paramTextField)
/*      */   {
/* 2509 */     paramChoice.removeAll();
/* 2510 */     paramChoice.add(PERM_NAME);
/*      */ 
/* 2512 */     if (paramPerm == null)
/*      */     {
/* 2514 */       paramTextField.setEditable(true);
/* 2515 */     } else if (paramPerm.TARGETS == null)
/*      */     {
/* 2517 */       paramTextField.setEditable(false);
/*      */     }
/*      */     else {
/* 2520 */       paramTextField.setEditable(true);
/* 2521 */       for (int i = 0; i < paramPerm.TARGETS.length; i++)
/* 2522 */         paramChoice.add(paramPerm.TARGETS[i]);
/*      */     }
/*      */   }
/*      */ 
/*      */   void setPermissionActions(Perm paramPerm, Choice paramChoice, TextField paramTextField)
/*      */   {
/* 2539 */     paramChoice.removeAll();
/* 2540 */     paramChoice.add(PERM_ACTIONS);
/*      */ 
/* 2542 */     if (paramPerm == null)
/*      */     {
/* 2544 */       paramTextField.setEditable(true);
/* 2545 */     } else if (paramPerm.ACTIONS == null)
/*      */     {
/* 2547 */       paramTextField.setEditable(false);
/*      */     }
/*      */     else {
/* 2550 */       paramTextField.setEditable(true);
/* 2551 */       for (int i = 0; i < paramPerm.ACTIONS.length; i++)
/* 2552 */         paramChoice.add(paramPerm.ACTIONS[i]);
/*      */     }
/*      */   }
/*      */ 
/*      */   static String PermissionEntryToUserFriendlyString(PolicyParser.PermissionEntry paramPermissionEntry)
/*      */   {
/* 2558 */     String str = paramPermissionEntry.permission;
/* 2559 */     if (paramPermissionEntry.name != null) {
/* 2560 */       str = str + " " + paramPermissionEntry.name;
/*      */     }
/* 2562 */     if (paramPermissionEntry.action != null) {
/* 2563 */       str = str + ", \"" + paramPermissionEntry.action + "\"";
/*      */     }
/* 2565 */     if (paramPermissionEntry.signedBy != null) {
/* 2566 */       str = str + ", signedBy " + paramPermissionEntry.signedBy;
/*      */     }
/* 2568 */     return str;
/*      */   }
/*      */ 
/*      */   static String PrincipalEntryToUserFriendlyString(PolicyParser.PrincipalEntry paramPrincipalEntry) {
/* 2572 */     StringWriter localStringWriter = new StringWriter();
/* 2573 */     PrintWriter localPrintWriter = new PrintWriter(localStringWriter);
/* 2574 */     paramPrincipalEntry.write(localPrintWriter);
/* 2575 */     return localStringWriter.toString();
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/* 1440 */     PERM_ARRAY.add(new AllPerm());
/* 1441 */     PERM_ARRAY.add(new AudioPerm());
/* 1442 */     PERM_ARRAY.add(new AuthPerm());
/* 1443 */     PERM_ARRAY.add(new AWTPerm());
/* 1444 */     PERM_ARRAY.add(new DelegationPerm());
/* 1445 */     PERM_ARRAY.add(new FilePerm());
/* 1446 */     PERM_ARRAY.add(new InqSecContextPerm());
/* 1447 */     PERM_ARRAY.add(new LogPerm());
/* 1448 */     PERM_ARRAY.add(new MgmtPerm());
/* 1449 */     PERM_ARRAY.add(new MBeanPerm());
/* 1450 */     PERM_ARRAY.add(new MBeanSvrPerm());
/* 1451 */     PERM_ARRAY.add(new MBeanTrustPerm());
/* 1452 */     PERM_ARRAY.add(new NetPerm());
/* 1453 */     PERM_ARRAY.add(new PrivCredPerm());
/* 1454 */     PERM_ARRAY.add(new PropPerm());
/* 1455 */     PERM_ARRAY.add(new ReflectPerm());
/* 1456 */     PERM_ARRAY.add(new RuntimePerm());
/* 1457 */     PERM_ARRAY.add(new SecurityPerm());
/* 1458 */     PERM_ARRAY.add(new SerialPerm());
/* 1459 */     PERM_ARRAY.add(new ServicePerm());
/* 1460 */     PERM_ARRAY.add(new SocketPerm());
/* 1461 */     PERM_ARRAY.add(new SQLPerm());
/* 1462 */     PERM_ARRAY.add(new SSLPerm());
/* 1463 */     PERM_ARRAY.add(new SubjDelegPerm());
/*      */ 
/* 1467 */     PRIN_ARRAY = new ArrayList();
/* 1468 */     PRIN_ARRAY.add(new KrbPrin());
/* 1469 */     PRIN_ARRAY.add(new X500Prin());
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.tools.policytool.ToolDialog
 * JD-Core Version:    0.6.2
 */