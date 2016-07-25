/*      */ package javax.swing;
/*      */ 
/*      */ import java.awt.Component;
/*      */ import java.awt.Component.BaselineResizeBehavior;
/*      */ import java.awt.ComponentOrientation;
/*      */ import java.awt.Container;
/*      */ import java.awt.Dimension;
/*      */ import java.awt.Insets;
/*      */ import java.awt.LayoutManager2;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collections;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ 
/*      */ public class GroupLayout
/*      */   implements LayoutManager2
/*      */ {
/*      */   private static final int MIN_SIZE = 0;
/*      */   private static final int PREF_SIZE = 1;
/*      */   private static final int MAX_SIZE = 2;
/*      */   private static final int SPECIFIC_SIZE = 3;
/*      */   private static final int UNSET = -2147483648;
/*      */   public static final int DEFAULT_SIZE = -1;
/*      */   public static final int PREFERRED_SIZE = -2;
/*      */   private boolean autocreatePadding;
/*      */   private boolean autocreateContainerPadding;
/*      */   private Group horizontalGroup;
/*      */   private Group verticalGroup;
/*      */   private Map<Component, ComponentInfo> componentInfos;
/*      */   private Container host;
/*      */   private Set<Spring> tmpParallelSet;
/*      */   private boolean springsChanged;
/*      */   private boolean isValid;
/*      */   private boolean hasPreferredPaddingSprings;
/*      */   private LayoutStyle layoutStyle;
/*      */   private boolean honorsVisibility;
/*      */ 
/*      */   private static void checkSize(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean)
/*      */   {
/*  339 */     checkResizeType(paramInt1, paramBoolean);
/*  340 */     if ((!paramBoolean) && (paramInt2 < 0))
/*  341 */       throw new IllegalArgumentException("Pref must be >= 0");
/*  342 */     if (paramBoolean) {
/*  343 */       checkResizeType(paramInt2, true);
/*      */     }
/*  345 */     checkResizeType(paramInt3, paramBoolean);
/*  346 */     checkLessThan(paramInt1, paramInt2);
/*  347 */     checkLessThan(paramInt2, paramInt3);
/*      */   }
/*      */ 
/*      */   private static void checkResizeType(int paramInt, boolean paramBoolean) {
/*  351 */     if ((paramInt < 0) && (((paramBoolean) && (paramInt != -1) && (paramInt != -2)) || ((!paramBoolean) && (paramInt != -2))))
/*      */     {
/*  354 */       throw new IllegalArgumentException("Invalid size");
/*      */     }
/*      */   }
/*      */ 
/*      */   private static void checkLessThan(int paramInt1, int paramInt2) {
/*  359 */     if ((paramInt1 >= 0) && (paramInt2 >= 0) && (paramInt1 > paramInt2))
/*  360 */       throw new IllegalArgumentException("Following is not met: min<=pref<=max");
/*      */   }
/*      */ 
/*      */   public GroupLayout(Container paramContainer)
/*      */   {
/*  373 */     if (paramContainer == null) {
/*  374 */       throw new IllegalArgumentException("Container must be non-null");
/*      */     }
/*  376 */     this.honorsVisibility = true;
/*  377 */     this.host = paramContainer;
/*  378 */     setHorizontalGroup(createParallelGroup(Alignment.LEADING, true));
/*  379 */     setVerticalGroup(createParallelGroup(Alignment.LEADING, true));
/*  380 */     this.componentInfos = new HashMap();
/*  381 */     this.tmpParallelSet = new HashSet();
/*      */   }
/*      */ 
/*      */   public void setHonorsVisibility(boolean paramBoolean)
/*      */   {
/*  405 */     if (this.honorsVisibility != paramBoolean) {
/*  406 */       this.honorsVisibility = paramBoolean;
/*  407 */       this.springsChanged = true;
/*  408 */       this.isValid = false;
/*  409 */       invalidateHost();
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean getHonorsVisibility()
/*      */   {
/*  421 */     return this.honorsVisibility;
/*      */   }
/*      */ 
/*      */   public void setHonorsVisibility(Component paramComponent, Boolean paramBoolean)
/*      */   {
/*  446 */     if (paramComponent == null) {
/*  447 */       throw new IllegalArgumentException("Component must be non-null");
/*      */     }
/*  449 */     getComponentInfo(paramComponent).setHonorsVisibility(paramBoolean);
/*  450 */     this.springsChanged = true;
/*  451 */     this.isValid = false;
/*  452 */     invalidateHost();
/*      */   }
/*      */ 
/*      */   public void setAutoCreateGaps(boolean paramBoolean)
/*      */   {
/*  466 */     if (this.autocreatePadding != paramBoolean) {
/*  467 */       this.autocreatePadding = paramBoolean;
/*  468 */       invalidateHost();
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean getAutoCreateGaps()
/*      */   {
/*  480 */     return this.autocreatePadding;
/*      */   }
/*      */ 
/*      */   public void setAutoCreateContainerGaps(boolean paramBoolean)
/*      */   {
/*  493 */     if (this.autocreateContainerPadding != paramBoolean) {
/*  494 */       this.autocreateContainerPadding = paramBoolean;
/*  495 */       this.horizontalGroup = createTopLevelGroup(getHorizontalGroup());
/*  496 */       this.verticalGroup = createTopLevelGroup(getVerticalGroup());
/*  497 */       invalidateHost();
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean getAutoCreateContainerGaps()
/*      */   {
/*  509 */     return this.autocreateContainerPadding;
/*      */   }
/*      */ 
/*      */   public void setHorizontalGroup(Group paramGroup)
/*      */   {
/*  521 */     if (paramGroup == null) {
/*  522 */       throw new IllegalArgumentException("Group must be non-null");
/*      */     }
/*  524 */     this.horizontalGroup = createTopLevelGroup(paramGroup);
/*  525 */     invalidateHost();
/*      */   }
/*      */ 
/*      */   private Group getHorizontalGroup()
/*      */   {
/*  536 */     int i = 0;
/*  537 */     if (this.horizontalGroup.springs.size() > 1) {
/*  538 */       i = 1;
/*      */     }
/*  540 */     return (Group)this.horizontalGroup.springs.get(i);
/*      */   }
/*      */ 
/*      */   public void setVerticalGroup(Group paramGroup)
/*      */   {
/*  552 */     if (paramGroup == null) {
/*  553 */       throw new IllegalArgumentException("Group must be non-null");
/*      */     }
/*  555 */     this.verticalGroup = createTopLevelGroup(paramGroup);
/*  556 */     invalidateHost();
/*      */   }
/*      */ 
/*      */   private Group getVerticalGroup()
/*      */   {
/*  567 */     int i = 0;
/*  568 */     if (this.verticalGroup.springs.size() > 1) {
/*  569 */       i = 1;
/*      */     }
/*  571 */     return (Group)this.verticalGroup.springs.get(i);
/*      */   }
/*      */ 
/*      */   private Group createTopLevelGroup(Group paramGroup)
/*      */   {
/*  580 */     SequentialGroup localSequentialGroup = createSequentialGroup();
/*  581 */     if (getAutoCreateContainerGaps()) {
/*  582 */       localSequentialGroup.addSpring(new ContainerAutoPreferredGapSpring());
/*  583 */       localSequentialGroup.addGroup(paramGroup);
/*  584 */       localSequentialGroup.addSpring(new ContainerAutoPreferredGapSpring());
/*      */     } else {
/*  586 */       localSequentialGroup.addGroup(paramGroup);
/*      */     }
/*  588 */     return localSequentialGroup;
/*      */   }
/*      */ 
/*      */   public SequentialGroup createSequentialGroup()
/*      */   {
/*  597 */     return new SequentialGroup();
/*      */   }
/*      */ 
/*      */   public ParallelGroup createParallelGroup()
/*      */   {
/*  609 */     return createParallelGroup(Alignment.LEADING);
/*      */   }
/*      */ 
/*      */   public ParallelGroup createParallelGroup(Alignment paramAlignment)
/*      */   {
/*  625 */     return createParallelGroup(paramAlignment, true);
/*      */   }
/*      */ 
/*      */   public ParallelGroup createParallelGroup(Alignment paramAlignment, boolean paramBoolean)
/*      */   {
/*  656 */     if (paramAlignment == null) {
/*  657 */       throw new IllegalArgumentException("alignment must be non null");
/*      */     }
/*      */ 
/*  660 */     if (paramAlignment == Alignment.BASELINE) {
/*  661 */       return new BaselineGroup(paramBoolean);
/*      */     }
/*  663 */     return new ParallelGroup(paramAlignment, paramBoolean);
/*      */   }
/*      */ 
/*      */   public ParallelGroup createBaselineGroup(boolean paramBoolean1, boolean paramBoolean2)
/*      */   {
/*  678 */     return new BaselineGroup(paramBoolean1, paramBoolean2);
/*      */   }
/*      */ 
/*      */   public void linkSize(Component[] paramArrayOfComponent)
/*      */   {
/*  699 */     linkSize(0, paramArrayOfComponent);
/*  700 */     linkSize(1, paramArrayOfComponent);
/*      */   }
/*      */ 
/*      */   public void linkSize(int paramInt, Component[] paramArrayOfComponent)
/*      */   {
/*  727 */     if (paramArrayOfComponent == null) {
/*  728 */       throw new IllegalArgumentException("Components must be non-null");
/*      */     }
/*  730 */     for (int i = paramArrayOfComponent.length - 1; i >= 0; i--) {
/*  731 */       localObject = paramArrayOfComponent[i];
/*  732 */       if (paramArrayOfComponent[i] == null) {
/*  733 */         throw new IllegalArgumentException("Components must be non-null");
/*      */       }
/*      */ 
/*  737 */       getComponentInfo((Component)localObject);
/*      */     }
/*      */ 
/*  740 */     if (paramInt == 0)
/*  741 */       i = 0;
/*  742 */     else if (paramInt == 1)
/*  743 */       i = 1;
/*      */     else {
/*  745 */       throw new IllegalArgumentException("Axis must be one of SwingConstants.HORIZONTAL or SwingConstants.VERTICAL");
/*      */     }
/*      */ 
/*  748 */     Object localObject = getComponentInfo(paramArrayOfComponent[(paramArrayOfComponent.length - 1)]).getLinkInfo(i);
/*      */ 
/*  750 */     for (int j = paramArrayOfComponent.length - 2; j >= 0; j--) {
/*  751 */       ((LinkInfo)localObject).add(getComponentInfo(paramArrayOfComponent[j]));
/*      */     }
/*  753 */     invalidateHost();
/*      */   }
/*      */ 
/*      */   public void replace(Component paramComponent1, Component paramComponent2)
/*      */   {
/*  768 */     if ((paramComponent1 == null) || (paramComponent2 == null)) {
/*  769 */       throw new IllegalArgumentException("Components must be non-null");
/*      */     }
/*      */ 
/*  773 */     if (this.springsChanged) {
/*  774 */       registerComponents(this.horizontalGroup, 0);
/*  775 */       registerComponents(this.verticalGroup, 1);
/*      */     }
/*  777 */     ComponentInfo localComponentInfo = (ComponentInfo)this.componentInfos.remove(paramComponent1);
/*  778 */     if (localComponentInfo == null) {
/*  779 */       throw new IllegalArgumentException("Component must already exist");
/*      */     }
/*  781 */     this.host.remove(paramComponent1);
/*  782 */     if (paramComponent2.getParent() != this.host) {
/*  783 */       this.host.add(paramComponent2);
/*      */     }
/*  785 */     localComponentInfo.setComponent(paramComponent2);
/*  786 */     this.componentInfos.put(paramComponent2, localComponentInfo);
/*  787 */     invalidateHost();
/*      */   }
/*      */ 
/*      */   public void setLayoutStyle(LayoutStyle paramLayoutStyle)
/*      */   {
/*  799 */     this.layoutStyle = paramLayoutStyle;
/*  800 */     invalidateHost();
/*      */   }
/*      */ 
/*      */   public LayoutStyle getLayoutStyle()
/*      */   {
/*  812 */     return this.layoutStyle;
/*      */   }
/*      */ 
/*      */   private LayoutStyle getLayoutStyle0() {
/*  816 */     LayoutStyle localLayoutStyle = getLayoutStyle();
/*  817 */     if (localLayoutStyle == null) {
/*  818 */       localLayoutStyle = LayoutStyle.getInstance();
/*      */     }
/*  820 */     return localLayoutStyle;
/*      */   }
/*      */ 
/*      */   private void invalidateHost() {
/*  824 */     if ((this.host instanceof JComponent))
/*  825 */       ((JComponent)this.host).revalidate();
/*      */     else {
/*  827 */       this.host.invalidate();
/*      */     }
/*  829 */     this.host.repaint();
/*      */   }
/*      */ 
/*      */   public void addLayoutComponent(String paramString, Component paramComponent)
/*      */   {
/*      */   }
/*      */ 
/*      */   public void removeLayoutComponent(Component paramComponent)
/*      */   {
/*  857 */     ComponentInfo localComponentInfo = (ComponentInfo)this.componentInfos.remove(paramComponent);
/*  858 */     if (localComponentInfo != null) {
/*  859 */       localComponentInfo.dispose();
/*  860 */       this.springsChanged = true;
/*  861 */       this.isValid = false;
/*      */     }
/*      */   }
/*      */ 
/*      */   public Dimension preferredLayoutSize(Container paramContainer)
/*      */   {
/*  877 */     checkParent(paramContainer);
/*  878 */     prepare(1);
/*  879 */     return adjustSize(this.horizontalGroup.getPreferredSize(0), this.verticalGroup.getPreferredSize(1));
/*      */   }
/*      */ 
/*      */   public Dimension minimumLayoutSize(Container paramContainer)
/*      */   {
/*  895 */     checkParent(paramContainer);
/*  896 */     prepare(0);
/*  897 */     return adjustSize(this.horizontalGroup.getMinimumSize(0), this.verticalGroup.getMinimumSize(1));
/*      */   }
/*      */ 
/*      */   public void layoutContainer(Container paramContainer)
/*      */   {
/*  910 */     prepare(3);
/*  911 */     Insets localInsets = paramContainer.getInsets();
/*  912 */     int i = paramContainer.getWidth() - localInsets.left - localInsets.right;
/*  913 */     int j = paramContainer.getHeight() - localInsets.top - localInsets.bottom;
/*  914 */     boolean bool = isLeftToRight();
/*  915 */     if ((getAutoCreateGaps()) || (getAutoCreateContainerGaps()) || (this.hasPreferredPaddingSprings))
/*      */     {
/*  918 */       calculateAutopadding(this.horizontalGroup, 0, 3, 0, i);
/*      */ 
/*  920 */       calculateAutopadding(this.verticalGroup, 1, 3, 0, j);
/*      */     }
/*      */ 
/*  924 */     this.horizontalGroup.setSize(0, 0, i);
/*  925 */     this.verticalGroup.setSize(1, 0, j);
/*      */ 
/*  927 */     for (ComponentInfo localComponentInfo : this.componentInfos.values())
/*  928 */       localComponentInfo.setBounds(localInsets, i, bool);
/*      */   }
/*      */ 
/*      */   public void addLayoutComponent(Component paramComponent, Object paramObject)
/*      */   {
/*      */   }
/*      */ 
/*      */   public Dimension maximumLayoutSize(Container paramContainer)
/*      */   {
/*  959 */     checkParent(paramContainer);
/*  960 */     prepare(2);
/*  961 */     return adjustSize(this.horizontalGroup.getMaximumSize(0), this.verticalGroup.getMaximumSize(1));
/*      */   }
/*      */ 
/*      */   public float getLayoutAlignmentX(Container paramContainer)
/*      */   {
/*  978 */     checkParent(paramContainer);
/*  979 */     return 0.5F;
/*      */   }
/*      */ 
/*      */   public float getLayoutAlignmentY(Container paramContainer)
/*      */   {
/*  995 */     checkParent(paramContainer);
/*  996 */     return 0.5F;
/*      */   }
/*      */ 
/*      */   public void invalidateLayout(Container paramContainer)
/*      */   {
/* 1008 */     checkParent(paramContainer);
/*      */ 
/* 1013 */     synchronized (paramContainer.getTreeLock()) {
/* 1014 */       this.isValid = false;
/*      */     }
/*      */   }
/*      */ 
/*      */   private void prepare(int paramInt) {
/* 1019 */     int i = 0;
/*      */ 
/* 1021 */     if (!this.isValid) {
/* 1022 */       this.isValid = true;
/* 1023 */       this.horizontalGroup.setSize(0, -2147483648, -2147483648);
/* 1024 */       this.verticalGroup.setSize(1, -2147483648, -2147483648);
/* 1025 */       for (ComponentInfo localComponentInfo : this.componentInfos.values()) {
/* 1026 */         if (localComponentInfo.updateVisibility()) {
/* 1027 */           i = 1;
/*      */         }
/* 1029 */         localComponentInfo.clearCachedSize();
/*      */       }
/*      */     }
/*      */ 
/* 1033 */     if (this.springsChanged) {
/* 1034 */       registerComponents(this.horizontalGroup, 0);
/* 1035 */       registerComponents(this.verticalGroup, 1);
/*      */     }
/*      */ 
/* 1039 */     if ((this.springsChanged) || (i != 0)) {
/* 1040 */       checkComponents();
/* 1041 */       this.horizontalGroup.removeAutopadding();
/* 1042 */       this.verticalGroup.removeAutopadding();
/* 1043 */       if (getAutoCreateGaps())
/* 1044 */         insertAutopadding(true);
/* 1045 */       else if ((this.hasPreferredPaddingSprings) || (getAutoCreateContainerGaps()))
/*      */       {
/* 1047 */         insertAutopadding(false);
/*      */       }
/* 1049 */       this.springsChanged = false;
/*      */     }
/*      */ 
/* 1056 */     if ((paramInt != 3) && ((getAutoCreateGaps()) || (getAutoCreateContainerGaps()) || (this.hasPreferredPaddingSprings)))
/*      */     {
/* 1058 */       calculateAutopadding(this.horizontalGroup, 0, paramInt, 0, 0);
/* 1059 */       calculateAutopadding(this.verticalGroup, 1, paramInt, 0, 0);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void calculateAutopadding(Group paramGroup, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/* 1065 */     paramGroup.unsetAutopadding();
/* 1066 */     switch (paramInt2) {
/*      */     case 0:
/* 1068 */       paramInt4 = paramGroup.getMinimumSize(paramInt1);
/* 1069 */       break;
/*      */     case 1:
/* 1071 */       paramInt4 = paramGroup.getPreferredSize(paramInt1);
/* 1072 */       break;
/*      */     case 2:
/* 1074 */       paramInt4 = paramGroup.getMaximumSize(paramInt1);
/* 1075 */       break;
/*      */     }
/*      */ 
/* 1079 */     paramGroup.setSize(paramInt1, paramInt3, paramInt4);
/* 1080 */     paramGroup.calculateAutopadding(paramInt1);
/*      */   }
/*      */ 
/*      */   private void checkComponents() {
/* 1084 */     for (ComponentInfo localComponentInfo : this.componentInfos.values()) {
/* 1085 */       if (localComponentInfo.horizontalSpring == null) {
/* 1086 */         throw new IllegalStateException(localComponentInfo.component + " is not attached to a horizontal group");
/*      */       }
/*      */ 
/* 1089 */       if (localComponentInfo.verticalSpring == null)
/* 1090 */         throw new IllegalStateException(localComponentInfo.component + " is not attached to a vertical group");
/*      */     }
/*      */   }
/*      */ 
/*      */   private void registerComponents(Group paramGroup, int paramInt)
/*      */   {
/* 1097 */     List localList = paramGroup.springs;
/* 1098 */     for (int i = localList.size() - 1; i >= 0; i--) {
/* 1099 */       Spring localSpring = (Spring)localList.get(i);
/* 1100 */       if ((localSpring instanceof ComponentSpring))
/* 1101 */         ((ComponentSpring)localSpring).installIfNecessary(paramInt);
/* 1102 */       else if ((localSpring instanceof Group))
/* 1103 */         registerComponents((Group)localSpring, paramInt);
/*      */     }
/*      */   }
/*      */ 
/*      */   private Dimension adjustSize(int paramInt1, int paramInt2)
/*      */   {
/* 1109 */     Insets localInsets = this.host.getInsets();
/* 1110 */     return new Dimension(paramInt1 + localInsets.left + localInsets.right, paramInt2 + localInsets.top + localInsets.bottom);
/*      */   }
/*      */ 
/*      */   private void checkParent(Container paramContainer)
/*      */   {
/* 1115 */     if (paramContainer != this.host)
/* 1116 */       throw new IllegalArgumentException("GroupLayout can only be used with one Container at a time");
/*      */   }
/*      */ 
/*      */   private ComponentInfo getComponentInfo(Component paramComponent)
/*      */   {
/* 1126 */     ComponentInfo localComponentInfo = (ComponentInfo)this.componentInfos.get(paramComponent);
/* 1127 */     if (localComponentInfo == null) {
/* 1128 */       localComponentInfo = new ComponentInfo(paramComponent);
/* 1129 */       this.componentInfos.put(paramComponent, localComponentInfo);
/* 1130 */       if (paramComponent.getParent() != this.host) {
/* 1131 */         this.host.add(paramComponent);
/*      */       }
/*      */     }
/* 1134 */     return localComponentInfo;
/*      */   }
/*      */ 
/*      */   private void insertAutopadding(boolean paramBoolean)
/*      */   {
/* 1144 */     this.horizontalGroup.insertAutopadding(0, new ArrayList(1), new ArrayList(1), new ArrayList(1), new ArrayList(1), paramBoolean);
/*      */ 
/* 1149 */     this.verticalGroup.insertAutopadding(1, new ArrayList(1), new ArrayList(1), new ArrayList(1), new ArrayList(1), paramBoolean);
/*      */   }
/*      */ 
/*      */   private boolean areParallelSiblings(Component paramComponent1, Component paramComponent2, int paramInt)
/*      */   {
/* 1162 */     ComponentInfo localComponentInfo1 = getComponentInfo(paramComponent1);
/* 1163 */     ComponentInfo localComponentInfo2 = getComponentInfo(paramComponent2);
/*      */     ComponentSpring localComponentSpring1;
/*      */     ComponentSpring localComponentSpring2;
/* 1166 */     if (paramInt == 0) {
/* 1167 */       localComponentSpring1 = localComponentInfo1.horizontalSpring;
/* 1168 */       localComponentSpring2 = localComponentInfo2.horizontalSpring;
/*      */     } else {
/* 1170 */       localComponentSpring1 = localComponentInfo1.verticalSpring;
/* 1171 */       localComponentSpring2 = localComponentInfo2.verticalSpring;
/*      */     }
/* 1173 */     Set localSet = this.tmpParallelSet;
/* 1174 */     localSet.clear();
/* 1175 */     Spring localSpring = localComponentSpring1.getParent();
/* 1176 */     while (localSpring != null) {
/* 1177 */       localSet.add(localSpring);
/* 1178 */       localSpring = localSpring.getParent();
/*      */     }
/* 1180 */     localSpring = localComponentSpring2.getParent();
/* 1181 */     while (localSpring != null) {
/* 1182 */       if (localSet.contains(localSpring)) {
/* 1183 */         localSet.clear();
/* 1184 */         while (localSpring != null) {
/* 1185 */           if ((localSpring instanceof ParallelGroup)) {
/* 1186 */             return true;
/*      */           }
/* 1188 */           localSpring = localSpring.getParent();
/*      */         }
/* 1190 */         return false;
/*      */       }
/* 1192 */       localSpring = localSpring.getParent();
/*      */     }
/* 1194 */     localSet.clear();
/* 1195 */     return false;
/*      */   }
/*      */ 
/*      */   private boolean isLeftToRight() {
/* 1199 */     return this.host.getComponentOrientation().isLeftToRight();
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/* 1211 */     if (this.springsChanged) {
/* 1212 */       registerComponents(this.horizontalGroup, 0);
/* 1213 */       registerComponents(this.verticalGroup, 1);
/*      */     }
/* 1215 */     StringBuffer localStringBuffer = new StringBuffer();
/* 1216 */     localStringBuffer.append("HORIZONTAL\n");
/* 1217 */     createSpringDescription(localStringBuffer, this.horizontalGroup, "  ", 0);
/* 1218 */     localStringBuffer.append("\nVERTICAL\n");
/* 1219 */     createSpringDescription(localStringBuffer, this.verticalGroup, "  ", 1);
/* 1220 */     return localStringBuffer.toString();
/*      */   }
/*      */ 
/*      */   private void createSpringDescription(StringBuffer paramStringBuffer, Spring paramSpring, String paramString, int paramInt)
/*      */   {
/* 1225 */     String str1 = "";
/* 1226 */     String str2 = "";
/*      */     Object localObject;
/* 1227 */     if ((paramSpring instanceof ComponentSpring)) {
/* 1228 */       localObject = (ComponentSpring)paramSpring;
/* 1229 */       str1 = Integer.toString(((ComponentSpring)localObject).getOrigin()) + " ";
/* 1230 */       String str3 = ((ComponentSpring)localObject).getComponent().getName();
/* 1231 */       if (str3 != null) {
/* 1232 */         str1 = "name=" + str3 + ", ";
/*      */       }
/*      */     }
/* 1235 */     if ((paramSpring instanceof AutoPreferredGapSpring)) {
/* 1236 */       localObject = (AutoPreferredGapSpring)paramSpring;
/*      */ 
/* 1238 */       str2 = ", userCreated=" + ((AutoPreferredGapSpring)localObject).getUserCreated() + ", matches=" + ((AutoPreferredGapSpring)localObject).getMatchDescription();
/*      */     }
/*      */ 
/* 1241 */     paramStringBuffer.append(paramString + paramSpring.getClass().getName() + " " + Integer.toHexString(paramSpring.hashCode()) + " " + str1 + ", size=" + paramSpring.getSize() + ", alignment=" + paramSpring.getAlignment() + " prefs=[" + paramSpring.getMinimumSize(paramInt) + " " + paramSpring.getPreferredSize(paramInt) + " " + paramSpring.getMaximumSize(paramInt) + str2 + "]\n");
/*      */ 
/* 1250 */     if ((paramSpring instanceof Group)) {
/* 1251 */       localObject = ((Group)paramSpring).springs;
/* 1252 */       paramString = paramString + "  ";
/* 1253 */       for (int i = 0; i < ((List)localObject).size(); i++)
/* 1254 */         createSpringDescription(paramStringBuffer, (Spring)((List)localObject).get(i), paramString, paramInt);
/*      */     }
/*      */   }
/*      */ 
/*      */   public static enum Alignment
/*      */   {
/*  306 */     LEADING, 
/*      */ 
/*  316 */     TRAILING, 
/*      */ 
/*  324 */     CENTER, 
/*      */ 
/*  333 */     BASELINE;
/*      */   }
/*      */ 
/*      */   private static final class AutoPreferredGapMatch
/*      */   {
/*      */     public final GroupLayout.ComponentSpring source;
/*      */     public final GroupLayout.ComponentSpring target;
/*      */ 
/*      */     AutoPreferredGapMatch(GroupLayout.ComponentSpring paramComponentSpring1, GroupLayout.ComponentSpring paramComponentSpring2)
/*      */     {
/* 3396 */       this.source = paramComponentSpring1;
/* 3397 */       this.target = paramComponentSpring2;
/*      */     }
/*      */ 
/*      */     private String toString(GroupLayout.ComponentSpring paramComponentSpring) {
/* 3401 */       return paramComponentSpring.getComponent().getName();
/*      */     }
/*      */ 
/*      */     public String toString() {
/* 3405 */       return "[" + toString(this.source) + "-" + toString(this.target) + "]";
/*      */     }
/*      */   }
/*      */ 
/*      */   private class AutoPreferredGapSpring extends GroupLayout.Spring
/*      */   {
/*      */     List<GroupLayout.ComponentSpring> sources;
/*      */     GroupLayout.ComponentSpring source;
/*      */     private List<GroupLayout.AutoPreferredGapMatch> matches;
/*      */     int size;
/*      */     int lastSize;
/*      */     private final int pref;
/*      */     private final int max;
/*      */     private LayoutStyle.ComponentPlacement type;
/*      */     private boolean userCreated;
/*      */ 
/*      */     private AutoPreferredGapSpring()
/*      */     {
/* 3222 */       super();
/* 3223 */       this.pref = -2;
/* 3224 */       this.max = -2;
/* 3225 */       this.type = LayoutStyle.ComponentPlacement.RELATED;
/*      */     }
/*      */     AutoPreferredGapSpring(int paramInt1, int arg3) {
/* 3228 */       super();
/* 3229 */       this.pref = paramInt1;
/*      */       int i;
/* 3230 */       this.max = i;
/*      */     }
/*      */     AutoPreferredGapSpring(LayoutStyle.ComponentPlacement paramInt1, int paramInt2, int arg4) {
/* 3233 */       super();
/* 3234 */       this.type = paramInt1;
/* 3235 */       this.pref = paramInt2;
/*      */       int i;
/* 3236 */       this.max = i;
/* 3237 */       this.userCreated = true;
/*      */     }
/*      */ 
/*      */     public void setSource(GroupLayout.ComponentSpring paramComponentSpring) {
/* 3241 */       this.source = paramComponentSpring;
/*      */     }
/*      */ 
/*      */     public void setSources(List<GroupLayout.ComponentSpring> paramList) {
/* 3245 */       this.sources = new ArrayList(paramList);
/*      */     }
/*      */ 
/*      */     public void setUserCreated(boolean paramBoolean) {
/* 3249 */       this.userCreated = paramBoolean;
/*      */     }
/*      */ 
/*      */     public boolean getUserCreated() {
/* 3253 */       return this.userCreated;
/*      */     }
/*      */ 
/*      */     void unset() {
/* 3257 */       this.lastSize = getSize();
/* 3258 */       super.unset();
/* 3259 */       this.size = 0;
/*      */     }
/*      */ 
/*      */     public void reset() {
/* 3263 */       this.size = 0;
/* 3264 */       this.sources = null;
/* 3265 */       this.source = null;
/* 3266 */       this.matches = null;
/*      */     }
/*      */ 
/*      */     public void calculatePadding(int paramInt) {
/* 3270 */       this.size = -2147483648;
/* 3271 */       int i = -2147483648;
/* 3272 */       if (this.matches != null) {
/* 3273 */         LayoutStyle localLayoutStyle = GroupLayout.this.getLayoutStyle0();
/*      */         int j;
/* 3275 */         if (paramInt == 0) {
/* 3276 */           if (GroupLayout.this.isLeftToRight())
/* 3277 */             j = 3;
/*      */           else
/* 3279 */             j = 7;
/*      */         }
/*      */         else {
/* 3282 */           j = 5;
/*      */         }
/* 3284 */         for (int k = this.matches.size() - 1; k >= 0; k--) {
/* 3285 */           GroupLayout.AutoPreferredGapMatch localAutoPreferredGapMatch = (GroupLayout.AutoPreferredGapMatch)this.matches.get(k);
/* 3286 */           i = Math.max(i, calculatePadding(localLayoutStyle, j, localAutoPreferredGapMatch.source, localAutoPreferredGapMatch.target));
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 3291 */       if (this.size == -2147483648) {
/* 3292 */         this.size = 0;
/*      */       }
/* 3294 */       if (i == -2147483648) {
/* 3295 */         i = 0;
/*      */       }
/* 3297 */       if (this.lastSize != -2147483648)
/* 3298 */         this.size += Math.min(i, this.lastSize);
/*      */     }
/*      */ 
/*      */     private int calculatePadding(LayoutStyle paramLayoutStyle, int paramInt, GroupLayout.ComponentSpring paramComponentSpring1, GroupLayout.ComponentSpring paramComponentSpring2)
/*      */     {
/* 3305 */       int i = paramComponentSpring2.getOrigin() - (paramComponentSpring1.getOrigin() + paramComponentSpring1.getSize());
/*      */ 
/* 3307 */       if (i >= 0)
/*      */       {
/*      */         int j;
/* 3309 */         if (((paramComponentSpring1.getComponent() instanceof JComponent)) && ((paramComponentSpring2.getComponent() instanceof JComponent)))
/*      */         {
/* 3311 */           j = paramLayoutStyle.getPreferredGap((JComponent)paramComponentSpring1.getComponent(), (JComponent)paramComponentSpring2.getComponent(), this.type, paramInt, GroupLayout.this.host);
/*      */         }
/*      */         else
/*      */         {
/* 3316 */           j = 10;
/*      */         }
/* 3318 */         if (j > i) {
/* 3319 */           this.size = Math.max(this.size, j - i);
/*      */         }
/* 3321 */         return j;
/*      */       }
/* 3323 */       return 0;
/*      */     }
/*      */ 
/*      */     public void addTarget(GroupLayout.ComponentSpring paramComponentSpring, int paramInt) {
/* 3327 */       int i = paramInt == 0 ? 1 : 0;
/* 3328 */       if (this.source != null) {
/* 3329 */         if (GroupLayout.this.areParallelSiblings(this.source.getComponent(), paramComponentSpring.getComponent(), i))
/*      */         {
/* 3331 */           addValidTarget(this.source, paramComponentSpring);
/*      */         }
/*      */       } else {
/* 3334 */         Component localComponent = paramComponentSpring.getComponent();
/* 3335 */         for (int j = this.sources.size() - 1; j >= 0; 
/* 3336 */           j--) {
/* 3337 */           GroupLayout.ComponentSpring localComponentSpring = (GroupLayout.ComponentSpring)this.sources.get(j);
/* 3338 */           if (GroupLayout.this.areParallelSiblings(localComponentSpring.getComponent(), localComponent, i))
/*      */           {
/* 3340 */             addValidTarget(localComponentSpring, paramComponentSpring);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     private void addValidTarget(GroupLayout.ComponentSpring paramComponentSpring1, GroupLayout.ComponentSpring paramComponentSpring2)
/*      */     {
/* 3348 */       if (this.matches == null) {
/* 3349 */         this.matches = new ArrayList(1);
/*      */       }
/* 3351 */       this.matches.add(new GroupLayout.AutoPreferredGapMatch(paramComponentSpring1, paramComponentSpring2));
/*      */     }
/*      */ 
/*      */     int calculateMinimumSize(int paramInt) {
/* 3355 */       return this.size;
/*      */     }
/*      */ 
/*      */     int calculatePreferredSize(int paramInt) {
/* 3359 */       if ((this.pref == -2) || (this.pref == -1)) {
/* 3360 */         return this.size;
/*      */       }
/* 3362 */       return Math.max(this.size, this.pref);
/*      */     }
/*      */ 
/*      */     int calculateMaximumSize(int paramInt) {
/* 3366 */       if (this.max >= 0) {
/* 3367 */         return Math.max(getPreferredSize(paramInt), this.max);
/*      */       }
/* 3369 */       return this.size;
/*      */     }
/*      */ 
/*      */     String getMatchDescription() {
/* 3373 */       return this.matches == null ? "" : this.matches.toString();
/*      */     }
/*      */ 
/*      */     public String toString() {
/* 3377 */       return super.toString() + getMatchDescription();
/*      */     }
/*      */ 
/*      */     boolean willHaveZeroSize(boolean paramBoolean)
/*      */     {
/* 3382 */       return paramBoolean;
/*      */     }
/*      */   }
/*      */ 
/*      */   private class BaselineGroup extends GroupLayout.ParallelGroup
/*      */   {
/*      */     private boolean allSpringsHaveBaseline;
/*      */     private int prefAscent;
/*      */     private int prefDescent;
/*      */     private boolean baselineAnchorSet;
/*      */     private boolean baselineAnchoredToTop;
/*      */     private boolean calcedBaseline;
/*      */ 
/*      */     BaselineGroup(boolean arg2)
/*      */     {
/* 2678 */       super(GroupLayout.Alignment.LEADING, bool);
/* 2679 */       this.prefAscent = (this.prefDescent = -1);
/* 2680 */       this.calcedBaseline = false;
/*      */     }
/*      */ 
/*      */     BaselineGroup(boolean paramBoolean1, boolean arg3) {
/* 2684 */       this(paramBoolean1);
/*      */       boolean bool;
/* 2685 */       this.baselineAnchoredToTop = bool;
/* 2686 */       this.baselineAnchorSet = true;
/*      */     }
/*      */ 
/*      */     void unset() {
/* 2690 */       super.unset();
/* 2691 */       this.prefAscent = (this.prefDescent = -1);
/* 2692 */       this.calcedBaseline = false;
/*      */     }
/*      */ 
/*      */     void setValidSize(int paramInt1, int paramInt2, int paramInt3) {
/* 2696 */       checkAxis(paramInt1);
/* 2697 */       if (this.prefAscent == -1) {
/* 2698 */         super.setValidSize(paramInt1, paramInt2, paramInt3);
/*      */       }
/*      */       else
/* 2701 */         baselineLayout(paramInt2, paramInt3);
/*      */     }
/*      */ 
/*      */     int calculateSize(int paramInt1, int paramInt2)
/*      */     {
/* 2706 */       checkAxis(paramInt1);
/* 2707 */       if (!this.calcedBaseline) {
/* 2708 */         calculateBaselineAndResizeBehavior();
/*      */       }
/* 2710 */       if (paramInt2 == 0) {
/* 2711 */         return calculateMinSize();
/*      */       }
/* 2713 */       if (paramInt2 == 2) {
/* 2714 */         return calculateMaxSize();
/*      */       }
/* 2716 */       if (this.allSpringsHaveBaseline) {
/* 2717 */         return this.prefAscent + this.prefDescent;
/*      */       }
/* 2719 */       return Math.max(this.prefAscent + this.prefDescent, super.calculateSize(paramInt1, paramInt2));
/*      */     }
/*      */ 
/*      */     private void calculateBaselineAndResizeBehavior()
/*      */     {
/* 2725 */       this.prefAscent = 0;
/* 2726 */       this.prefDescent = 0;
/* 2727 */       int i = 0;
/* 2728 */       Object localObject = null;
/* 2729 */       for (GroupLayout.Spring localSpring : this.springs) {
/* 2730 */         if ((localSpring.getAlignment() == null) || (localSpring.getAlignment() == GroupLayout.Alignment.BASELINE))
/*      */         {
/* 2732 */           int j = localSpring.getBaseline();
/* 2733 */           if (j >= 0) {
/* 2734 */             if (localSpring.isResizable(1)) {
/* 2735 */               Component.BaselineResizeBehavior localBaselineResizeBehavior = localSpring.getBaselineResizeBehavior();
/*      */ 
/* 2737 */               if (localObject == null)
/* 2738 */                 localObject = localBaselineResizeBehavior;
/* 2739 */               else if (localBaselineResizeBehavior != localObject) {
/* 2740 */                 localObject = Component.BaselineResizeBehavior.CONSTANT_ASCENT;
/*      */               }
/*      */             }
/*      */ 
/* 2744 */             this.prefAscent = Math.max(this.prefAscent, j);
/* 2745 */             this.prefDescent = Math.max(this.prefDescent, localSpring.getPreferredSize(1) - j);
/*      */ 
/* 2747 */             i++;
/*      */           }
/*      */         }
/*      */       }
/* 2751 */       if (!this.baselineAnchorSet) {
/* 2752 */         if (localObject == Component.BaselineResizeBehavior.CONSTANT_DESCENT)
/* 2753 */           this.baselineAnchoredToTop = false;
/*      */         else {
/* 2755 */           this.baselineAnchoredToTop = true;
/*      */         }
/*      */       }
/* 2758 */       this.allSpringsHaveBaseline = (i == this.springs.size());
/* 2759 */       this.calcedBaseline = true;
/*      */     }
/*      */ 
/*      */     private int calculateMaxSize() {
/* 2763 */       int i = this.prefAscent;
/* 2764 */       int j = this.prefDescent;
/* 2765 */       int k = 0;
/* 2766 */       for (GroupLayout.Spring localSpring : this.springs)
/*      */       {
/* 2768 */         int n = localSpring.getMaximumSize(1);
/*      */         int m;
/* 2769 */         if (((localSpring.getAlignment() == null) || (localSpring.getAlignment() == GroupLayout.Alignment.BASELINE)) && ((m = localSpring.getBaseline()) >= 0))
/*      */         {
/* 2772 */           int i1 = localSpring.getPreferredSize(1);
/* 2773 */           if (i1 != n) {
/* 2774 */             switch (GroupLayout.1.$SwitchMap$java$awt$Component$BaselineResizeBehavior[localSpring.getBaselineResizeBehavior().ordinal()]) {
/*      */             case 1:
/* 2776 */               if (this.baselineAnchoredToTop)
/* 2777 */                 j = Math.max(j, n - m); break;
/*      */             case 2:
/* 2782 */               if (!this.baselineAnchoredToTop)
/* 2783 */                 i = Math.max(i, n - i1 + m); break;
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/*      */         }
/*      */         else
/*      */         {
/* 2793 */           k = Math.max(k, n);
/*      */         }
/*      */       }
/* 2796 */       return Math.max(k, i + j);
/*      */     }
/*      */ 
/*      */     private int calculateMinSize() {
/* 2800 */       int i = 0;
/* 2801 */       int j = 0;
/* 2802 */       int k = 0;
/* 2803 */       if (this.baselineAnchoredToTop)
/* 2804 */         i = this.prefAscent;
/*      */       else {
/* 2806 */         j = this.prefDescent;
/*      */       }
/* 2808 */       for (GroupLayout.Spring localSpring : this.springs) {
/* 2809 */         int m = localSpring.getMinimumSize(1);
/*      */         int n;
/* 2811 */         if (((localSpring.getAlignment() == null) || (localSpring.getAlignment() == GroupLayout.Alignment.BASELINE)) && ((n = localSpring.getBaseline()) >= 0))
/*      */         {
/* 2814 */           int i1 = localSpring.getPreferredSize(1);
/* 2815 */           Component.BaselineResizeBehavior localBaselineResizeBehavior = localSpring.getBaselineResizeBehavior();
/*      */ 
/* 2817 */           switch (GroupLayout.1.$SwitchMap$java$awt$Component$BaselineResizeBehavior[localBaselineResizeBehavior.ordinal()]) {
/*      */           case 1:
/* 2819 */             if (this.baselineAnchoredToTop) {
/* 2820 */               j = Math.max(m - n, j);
/*      */             }
/*      */             else {
/* 2823 */               i = Math.max(n, i);
/*      */             }
/* 2825 */             break;
/*      */           case 2:
/* 2827 */             if (!this.baselineAnchoredToTop) {
/* 2828 */               i = Math.max(n - (i1 - m), i);
/*      */             }
/*      */             else
/*      */             {
/* 2832 */               j = Math.max(i1 - n, j);
/*      */             }
/*      */ 
/* 2835 */             break;
/*      */           default:
/* 2839 */             i = Math.max(n, i);
/* 2840 */             j = Math.max(i1 - n, j);
/*      */           }
/*      */ 
/*      */         }
/*      */         else
/*      */         {
/* 2846 */           k = Math.max(k, m);
/*      */         }
/*      */       }
/* 2849 */       return Math.max(k, i + j);
/*      */     }
/*      */ 
/*      */     private void baselineLayout(int paramInt1, int paramInt2)
/*      */     {
/*      */       int i;
/*      */       int j;
/* 2859 */       if (this.baselineAnchoredToTop) {
/* 2860 */         i = this.prefAscent;
/* 2861 */         j = paramInt2 - i;
/*      */       } else {
/* 2863 */         i = paramInt2 - this.prefDescent;
/* 2864 */         j = this.prefDescent;
/*      */       }
/* 2866 */       for (GroupLayout.Spring localSpring : this.springs) {
/* 2867 */         GroupLayout.Alignment localAlignment = localSpring.getAlignment();
/* 2868 */         if ((localAlignment == null) || (localAlignment == GroupLayout.Alignment.BASELINE)) {
/* 2869 */           int k = localSpring.getBaseline();
/* 2870 */           if (k >= 0) {
/* 2871 */             int m = localSpring.getMaximumSize(1);
/* 2872 */             int n = localSpring.getPreferredSize(1);
/* 2873 */             int i1 = n;
/*      */             int i2;
/* 2875 */             switch (GroupLayout.1.$SwitchMap$java$awt$Component$BaselineResizeBehavior[localSpring.getBaselineResizeBehavior().ordinal()]) {
/*      */             case 1:
/* 2877 */               i2 = paramInt1 + i - k;
/* 2878 */               i1 = Math.min(j, m - k) + k;
/*      */ 
/* 2880 */               break;
/*      */             case 2:
/* 2882 */               i1 = Math.min(i, m - n + k) + (n - k);
/*      */ 
/* 2885 */               i2 = paramInt1 + i + (n - k) - i1;
/*      */ 
/* 2887 */               break;
/*      */             default:
/* 2889 */               i2 = paramInt1 + i - k;
/*      */             }
/*      */ 
/* 2892 */             localSpring.setSize(1, i2, i1);
/*      */           } else {
/* 2894 */             setChildSize(localSpring, 1, paramInt1, paramInt2);
/*      */           }
/*      */         } else {
/* 2897 */           setChildSize(localSpring, 1, paramInt1, paramInt2);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     int getBaseline() {
/* 2903 */       if (this.springs.size() > 1)
/*      */       {
/* 2905 */         getPreferredSize(1);
/* 2906 */         return this.prefAscent;
/* 2907 */       }if (this.springs.size() == 1) {
/* 2908 */         return ((GroupLayout.Spring)this.springs.get(0)).getBaseline();
/*      */       }
/* 2910 */       return -1;
/*      */     }
/*      */ 
/*      */     Component.BaselineResizeBehavior getBaselineResizeBehavior() {
/* 2914 */       if (this.springs.size() == 1) {
/* 2915 */         return ((GroupLayout.Spring)this.springs.get(0)).getBaselineResizeBehavior();
/*      */       }
/* 2917 */       if (this.baselineAnchoredToTop) {
/* 2918 */         return Component.BaselineResizeBehavior.CONSTANT_ASCENT;
/*      */       }
/* 2920 */       return Component.BaselineResizeBehavior.CONSTANT_DESCENT;
/*      */     }
/*      */ 
/*      */     private void checkAxis(int paramInt)
/*      */     {
/* 2925 */       if (paramInt == 0)
/* 2926 */         throw new IllegalStateException("Baseline must be used along vertical axis");
/*      */     }
/*      */   }
/*      */ 
/*      */   private class ComponentInfo
/*      */   {
/*      */     private Component component;
/*      */     GroupLayout.ComponentSpring horizontalSpring;
/*      */     GroupLayout.ComponentSpring verticalSpring;
/*      */     private GroupLayout.LinkInfo horizontalMaster;
/*      */     private GroupLayout.LinkInfo verticalMaster;
/*      */     private boolean visible;
/*      */     private Boolean honorsVisibility;
/*      */ 
/*      */     ComponentInfo(Component arg2)
/*      */     {
/*      */       Object localObject;
/* 3602 */       this.component = localObject;
/* 3603 */       updateVisibility();
/*      */     }
/*      */ 
/*      */     public void dispose()
/*      */     {
/* 3608 */       removeSpring(this.horizontalSpring);
/* 3609 */       this.horizontalSpring = null;
/* 3610 */       removeSpring(this.verticalSpring);
/* 3611 */       this.verticalSpring = null;
/*      */ 
/* 3613 */       if (this.horizontalMaster != null) {
/* 3614 */         this.horizontalMaster.remove(this);
/*      */       }
/* 3616 */       if (this.verticalMaster != null)
/* 3617 */         this.verticalMaster.remove(this);
/*      */     }
/*      */ 
/*      */     void setHonorsVisibility(Boolean paramBoolean)
/*      */     {
/* 3622 */       this.honorsVisibility = paramBoolean;
/*      */     }
/*      */ 
/*      */     private void removeSpring(GroupLayout.Spring paramSpring) {
/* 3626 */       if (paramSpring != null)
/* 3627 */         ((GroupLayout.Group)paramSpring.getParent()).springs.remove(paramSpring);
/*      */     }
/*      */ 
/*      */     public boolean isVisible()
/*      */     {
/* 3632 */       return this.visible;
/*      */     }
/*      */ 
/*      */     boolean updateVisibility()
/*      */     {
/*      */       boolean bool1;
/* 3642 */       if (this.honorsVisibility == null)
/* 3643 */         bool1 = GroupLayout.this.getHonorsVisibility();
/*      */       else {
/* 3645 */         bool1 = this.honorsVisibility.booleanValue();
/*      */       }
/* 3647 */       boolean bool2 = bool1 ? this.component.isVisible() : true;
/*      */ 
/* 3649 */       if (this.visible != bool2) {
/* 3650 */         this.visible = bool2;
/* 3651 */         return true;
/*      */       }
/* 3653 */       return false;
/*      */     }
/*      */ 
/*      */     public void setBounds(Insets paramInsets, int paramInt, boolean paramBoolean) {
/* 3657 */       int i = this.horizontalSpring.getOrigin();
/* 3658 */       int j = this.horizontalSpring.getSize();
/* 3659 */       int k = this.verticalSpring.getOrigin();
/* 3660 */       int m = this.verticalSpring.getSize();
/*      */ 
/* 3662 */       if (!paramBoolean) {
/* 3663 */         i = paramInt - i - j;
/*      */       }
/* 3665 */       this.component.setBounds(i + paramInsets.left, k + paramInsets.top, j, m);
/*      */     }
/*      */ 
/*      */     public void setComponent(Component paramComponent) {
/* 3669 */       this.component = paramComponent;
/* 3670 */       if (this.horizontalSpring != null) {
/* 3671 */         this.horizontalSpring.setComponent(paramComponent);
/*      */       }
/* 3673 */       if (this.verticalSpring != null)
/* 3674 */         this.verticalSpring.setComponent(paramComponent);
/*      */     }
/*      */ 
/*      */     public Component getComponent()
/*      */     {
/* 3679 */       return this.component;
/*      */     }
/*      */ 
/*      */     public boolean isLinked(int paramInt)
/*      */     {
/* 3687 */       if (paramInt == 0) {
/* 3688 */         return this.horizontalMaster != null;
/*      */       }
/* 3690 */       assert (paramInt == 1);
/* 3691 */       return this.verticalMaster != null;
/*      */     }
/*      */ 
/*      */     private void setLinkInfo(int paramInt, GroupLayout.LinkInfo paramLinkInfo) {
/* 3695 */       if (paramInt == 0) {
/* 3696 */         this.horizontalMaster = paramLinkInfo;
/*      */       } else {
/* 3698 */         assert (paramInt == 1);
/* 3699 */         this.verticalMaster = paramLinkInfo;
/*      */       }
/*      */     }
/*      */ 
/*      */     public GroupLayout.LinkInfo getLinkInfo(int paramInt) {
/* 3704 */       return getLinkInfo(paramInt, true);
/*      */     }
/*      */ 
/*      */     private GroupLayout.LinkInfo getLinkInfo(int paramInt, boolean paramBoolean) {
/* 3708 */       if (paramInt == 0) {
/* 3709 */         if ((this.horizontalMaster == null) && (paramBoolean))
/*      */         {
/* 3712 */           new GroupLayout.LinkInfo(0).add(this);
/*      */         }
/* 3714 */         return this.horizontalMaster;
/*      */       }
/* 3716 */       assert (paramInt == 1);
/* 3717 */       if ((this.verticalMaster == null) && (paramBoolean))
/*      */       {
/* 3720 */         new GroupLayout.LinkInfo(1).add(this);
/*      */       }
/* 3722 */       return this.verticalMaster;
/*      */     }
/*      */ 
/*      */     public void clearCachedSize()
/*      */     {
/* 3727 */       if (this.horizontalMaster != null) {
/* 3728 */         this.horizontalMaster.clearCachedSize();
/*      */       }
/* 3730 */       if (this.verticalMaster != null)
/* 3731 */         this.verticalMaster.clearCachedSize();
/*      */     }
/*      */ 
/*      */     int getLinkSize(int paramInt1, int paramInt2)
/*      */     {
/* 3736 */       if (paramInt1 == 0) {
/* 3737 */         return this.horizontalMaster.getSize(paramInt1);
/*      */       }
/* 3739 */       assert (paramInt1 == 1);
/* 3740 */       return this.verticalMaster.getSize(paramInt1);
/*      */     }
/*      */   }
/*      */ 
/*      */   private final class ComponentSpring extends GroupLayout.Spring
/*      */   {
/*      */     private Component component;
/*      */     private int origin;
/*      */     private final int min;
/*      */     private final int pref;
/*      */     private final int max;
/* 2944 */     private int baseline = -1;
/*      */     private boolean installed;
/*      */ 
/*      */     private ComponentSpring(Component paramInt1, int paramInt2, int paramInt3, int arg5)
/*      */     {
/* 2950 */       super();
/* 2951 */       this.component = paramInt1;
/* 2952 */       if (paramInt1 == null)
/* 2953 */         throw new IllegalArgumentException("Component must be non-null");
/*      */       int i;
/* 2957 */       GroupLayout.checkSize(paramInt2, paramInt3, i, true);
/*      */ 
/* 2959 */       this.min = paramInt2;
/* 2960 */       this.max = i;
/* 2961 */       this.pref = paramInt3;
/*      */ 
/* 2965 */       GroupLayout.this.getComponentInfo(paramInt1);
/*      */     }
/*      */ 
/*      */     int calculateMinimumSize(int paramInt) {
/* 2969 */       if (isLinked(paramInt)) {
/* 2970 */         return getLinkSize(paramInt, 0);
/*      */       }
/* 2972 */       return calculateNonlinkedMinimumSize(paramInt);
/*      */     }
/*      */ 
/*      */     int calculatePreferredSize(int paramInt) {
/* 2976 */       if (isLinked(paramInt)) {
/* 2977 */         return getLinkSize(paramInt, 1);
/*      */       }
/* 2979 */       int i = getMinimumSize(paramInt);
/* 2980 */       int j = calculateNonlinkedPreferredSize(paramInt);
/* 2981 */       int k = getMaximumSize(paramInt);
/* 2982 */       return Math.min(k, Math.max(i, j));
/*      */     }
/*      */ 
/*      */     int calculateMaximumSize(int paramInt) {
/* 2986 */       if (isLinked(paramInt)) {
/* 2987 */         return getLinkSize(paramInt, 2);
/*      */       }
/* 2989 */       return Math.max(getMinimumSize(paramInt), calculateNonlinkedMaximumSize(paramInt));
/*      */     }
/*      */ 
/*      */     boolean isVisible()
/*      */     {
/* 2994 */       return GroupLayout.this.getComponentInfo(getComponent()).isVisible();
/*      */     }
/*      */ 
/*      */     int calculateNonlinkedMinimumSize(int paramInt) {
/* 2998 */       if (!isVisible()) {
/* 2999 */         return 0;
/*      */       }
/* 3001 */       if (this.min >= 0) {
/* 3002 */         return this.min;
/*      */       }
/* 3004 */       if (this.min == -2) {
/* 3005 */         return calculateNonlinkedPreferredSize(paramInt);
/*      */       }
/* 3007 */       assert (this.min == -1);
/* 3008 */       return getSizeAlongAxis(paramInt, this.component.getMinimumSize());
/*      */     }
/*      */ 
/*      */     int calculateNonlinkedPreferredSize(int paramInt) {
/* 3012 */       if (!isVisible()) {
/* 3013 */         return 0;
/*      */       }
/* 3015 */       if (this.pref >= 0) {
/* 3016 */         return this.pref;
/*      */       }
/* 3018 */       assert ((this.pref == -1) || (this.pref == -2));
/* 3019 */       return getSizeAlongAxis(paramInt, this.component.getPreferredSize());
/*      */     }
/*      */ 
/*      */     int calculateNonlinkedMaximumSize(int paramInt) {
/* 3023 */       if (!isVisible()) {
/* 3024 */         return 0;
/*      */       }
/* 3026 */       if (this.max >= 0) {
/* 3027 */         return this.max;
/*      */       }
/* 3029 */       if (this.max == -2) {
/* 3030 */         return calculateNonlinkedPreferredSize(paramInt);
/*      */       }
/* 3032 */       assert (this.max == -1);
/* 3033 */       return getSizeAlongAxis(paramInt, this.component.getMaximumSize());
/*      */     }
/*      */ 
/*      */     private int getSizeAlongAxis(int paramInt, Dimension paramDimension) {
/* 3037 */       return paramInt == 0 ? paramDimension.width : paramDimension.height;
/*      */     }
/*      */ 
/*      */     private int getLinkSize(int paramInt1, int paramInt2) {
/* 3041 */       if (!isVisible()) {
/* 3042 */         return 0;
/*      */       }
/* 3044 */       GroupLayout.ComponentInfo localComponentInfo = GroupLayout.this.getComponentInfo(this.component);
/* 3045 */       return localComponentInfo.getLinkSize(paramInt1, paramInt2);
/*      */     }
/*      */ 
/*      */     void setSize(int paramInt1, int paramInt2, int paramInt3) {
/* 3049 */       super.setSize(paramInt1, paramInt2, paramInt3);
/* 3050 */       this.origin = paramInt2;
/* 3051 */       if (paramInt3 == -2147483648)
/* 3052 */         this.baseline = -1;
/*      */     }
/*      */ 
/*      */     int getOrigin()
/*      */     {
/* 3057 */       return this.origin;
/*      */     }
/*      */ 
/*      */     void setComponent(Component paramComponent) {
/* 3061 */       this.component = paramComponent;
/*      */     }
/*      */ 
/*      */     Component getComponent() {
/* 3065 */       return this.component;
/*      */     }
/*      */ 
/*      */     int getBaseline() {
/* 3069 */       if (this.baseline == -1) {
/* 3070 */         ComponentSpring localComponentSpring = GroupLayout.this.getComponentInfo(this.component).horizontalSpring;
/*      */ 
/* 3072 */         int i = localComponentSpring.getPreferredSize(0);
/* 3073 */         int j = getPreferredSize(1);
/* 3074 */         if ((i > 0) && (j > 0)) {
/* 3075 */           this.baseline = this.component.getBaseline(i, j);
/*      */         }
/*      */       }
/* 3078 */       return this.baseline;
/*      */     }
/*      */ 
/*      */     Component.BaselineResizeBehavior getBaselineResizeBehavior() {
/* 3082 */       return getComponent().getBaselineResizeBehavior();
/*      */     }
/*      */ 
/*      */     private boolean isLinked(int paramInt) {
/* 3086 */       return GroupLayout.this.getComponentInfo(this.component).isLinked(paramInt);
/*      */     }
/*      */ 
/*      */     void installIfNecessary(int paramInt) {
/* 3090 */       if (!this.installed) {
/* 3091 */         this.installed = true;
/* 3092 */         if (paramInt == 0)
/* 3093 */           GroupLayout.this.getComponentInfo(this.component).horizontalSpring = this;
/*      */         else
/* 3095 */           GroupLayout.this.getComponentInfo(this.component).verticalSpring = this;
/*      */       }
/*      */     }
/*      */ 
/*      */     boolean willHaveZeroSize(boolean paramBoolean)
/*      */     {
/* 3102 */       return !isVisible();
/*      */     }
/*      */   }
/*      */ 
/*      */   private class ContainerAutoPreferredGapSpring extends GroupLayout.AutoPreferredGapSpring
/*      */   {
/*      */     private List<GroupLayout.ComponentSpring> targets;
/*      */ 
/*      */     ContainerAutoPreferredGapSpring()
/*      */     {
/* 3418 */       super(null);
/* 3419 */       setUserCreated(true);
/*      */     }
/*      */ 
/*      */     ContainerAutoPreferredGapSpring(int paramInt1, int arg3) {
/* 3423 */       super(paramInt1, i);
/* 3424 */       setUserCreated(true);
/*      */     }
/*      */ 
/*      */     public void addTarget(GroupLayout.ComponentSpring paramComponentSpring, int paramInt) {
/* 3428 */       if (this.targets == null) {
/* 3429 */         this.targets = new ArrayList(1);
/*      */       }
/* 3431 */       this.targets.add(paramComponentSpring);
/*      */     }
/*      */ 
/*      */     public void calculatePadding(int paramInt) {
/* 3435 */       LayoutStyle localLayoutStyle = GroupLayout.this.getLayoutStyle0();
/* 3436 */       int i = 0;
/*      */ 
/* 3438 */       this.size = 0;
/*      */       int j;
/*      */       int k;
/*      */       GroupLayout.ComponentSpring localComponentSpring;
/* 3439 */       if (this.targets != null)
/*      */       {
/* 3441 */         if (paramInt == 0) {
/* 3442 */           if (GroupLayout.this.isLeftToRight())
/* 3443 */             j = 7;
/*      */           else
/* 3445 */             j = 3;
/*      */         }
/*      */         else {
/* 3448 */           j = 5;
/*      */         }
/* 3450 */         for (k = this.targets.size() - 1; k >= 0; k--) {
/* 3451 */           localComponentSpring = (GroupLayout.ComponentSpring)this.targets.get(k);
/* 3452 */           int m = 10;
/* 3453 */           if ((localComponentSpring.getComponent() instanceof JComponent)) {
/* 3454 */             m = localLayoutStyle.getContainerGap((JComponent)localComponentSpring.getComponent(), j, GroupLayout.this.host);
/*      */ 
/* 3457 */             i = Math.max(m, i);
/* 3458 */             m -= localComponentSpring.getOrigin();
/*      */           } else {
/* 3460 */             i = Math.max(m, i);
/*      */           }
/* 3462 */           this.size = Math.max(this.size, m);
/*      */         }
/*      */       }
/*      */       else {
/* 3466 */         if (paramInt == 0) {
/* 3467 */           if (GroupLayout.this.isLeftToRight())
/* 3468 */             j = 3;
/*      */           else
/* 3470 */             j = 7;
/*      */         }
/*      */         else {
/* 3473 */           j = 5;
/*      */         }
/* 3475 */         if (this.sources != null) {
/* 3476 */           for (k = this.sources.size() - 1; k >= 0; k--) {
/* 3477 */             localComponentSpring = (GroupLayout.ComponentSpring)this.sources.get(k);
/* 3478 */             i = Math.max(i, updateSize(localLayoutStyle, localComponentSpring, j));
/*      */           }
/*      */         }
/* 3481 */         else if (this.source != null) {
/* 3482 */           i = updateSize(localLayoutStyle, this.source, j);
/*      */         }
/*      */       }
/* 3485 */       if (this.lastSize != -2147483648)
/* 3486 */         this.size += Math.min(i, this.lastSize);
/*      */     }
/*      */ 
/*      */     private int updateSize(LayoutStyle paramLayoutStyle, GroupLayout.ComponentSpring paramComponentSpring, int paramInt)
/*      */     {
/* 3492 */       int i = 10;
/* 3493 */       if ((paramComponentSpring.getComponent() instanceof JComponent)) {
/* 3494 */         i = paramLayoutStyle.getContainerGap((JComponent)paramComponentSpring.getComponent(), paramInt, GroupLayout.this.host);
/*      */       }
/*      */ 
/* 3498 */       int j = Math.max(0, getParent().getSize() - paramComponentSpring.getSize() - paramComponentSpring.getOrigin());
/*      */ 
/* 3500 */       this.size = Math.max(this.size, i - j);
/* 3501 */       return i;
/*      */     }
/*      */ 
/*      */     String getMatchDescription() {
/* 3505 */       if (this.targets != null) {
/* 3506 */         return "leading: " + this.targets.toString();
/*      */       }
/* 3508 */       if (this.sources != null) {
/* 3509 */         return "trailing: " + this.sources.toString();
/*      */       }
/* 3511 */       return "--";
/*      */     }
/*      */   }
/*      */ 
/*      */   private class GapSpring extends GroupLayout.Spring
/*      */   {
/*      */     private final int min;
/*      */     private final int pref;
/*      */     private final int max;
/*      */ 
/*      */     GapSpring(int paramInt1, int paramInt2, int arg4)
/*      */     {
/* 3172 */       super();
/*      */       int i;
/* 3173 */       GroupLayout.checkSize(paramInt1, paramInt2, i, false);
/* 3174 */       this.min = paramInt1;
/* 3175 */       this.pref = paramInt2;
/* 3176 */       this.max = i;
/*      */     }
/*      */ 
/*      */     int calculateMinimumSize(int paramInt) {
/* 3180 */       if (this.min == -2) {
/* 3181 */         return getPreferredSize(paramInt);
/*      */       }
/* 3183 */       return this.min;
/*      */     }
/*      */ 
/*      */     int calculatePreferredSize(int paramInt) {
/* 3187 */       return this.pref;
/*      */     }
/*      */ 
/*      */     int calculateMaximumSize(int paramInt) {
/* 3191 */       if (this.max == -2) {
/* 3192 */         return getPreferredSize(paramInt);
/*      */       }
/* 3194 */       return this.max;
/*      */     }
/*      */ 
/*      */     boolean willHaveZeroSize(boolean paramBoolean)
/*      */     {
/* 3199 */       return false;
/*      */     }
/*      */   }
/*      */ 
/*      */   public abstract class Group extends GroupLayout.Spring
/*      */   {
/*      */     List<GroupLayout.Spring> springs;
/*      */ 
/*      */     Group()
/*      */     {
/* 1484 */       super();
/* 1485 */       this.springs = new ArrayList();
/*      */     }
/*      */ 
/*      */     public Group addGroup(Group paramGroup)
/*      */     {
/* 1495 */       return addSpring(paramGroup);
/*      */     }
/*      */ 
/*      */     public Group addComponent(Component paramComponent)
/*      */     {
/* 1505 */       return addComponent(paramComponent, -1, -1, -1);
/*      */     }
/*      */ 
/*      */     public Group addComponent(Component paramComponent, int paramInt1, int paramInt2, int paramInt3)
/*      */     {
/* 1524 */       return addSpring(new GroupLayout.ComponentSpring(GroupLayout.this, paramComponent, paramInt1, paramInt2, paramInt3, null));
/*      */     }
/*      */ 
/*      */     public Group addGap(int paramInt)
/*      */     {
/* 1536 */       return addGap(paramInt, paramInt, paramInt);
/*      */     }
/*      */ 
/*      */     public Group addGap(int paramInt1, int paramInt2, int paramInt3)
/*      */     {
/* 1550 */       return addSpring(new GroupLayout.GapSpring(GroupLayout.this, paramInt1, paramInt2, paramInt3));
/*      */     }
/*      */ 
/*      */     GroupLayout.Spring getSpring(int paramInt) {
/* 1554 */       return (GroupLayout.Spring)this.springs.get(paramInt);
/*      */     }
/*      */ 
/*      */     int indexOf(GroupLayout.Spring paramSpring) {
/* 1558 */       return this.springs.indexOf(paramSpring);
/*      */     }
/*      */ 
/*      */     Group addSpring(GroupLayout.Spring paramSpring)
/*      */     {
/* 1566 */       this.springs.add(paramSpring);
/* 1567 */       paramSpring.setParent(this);
/* 1568 */       if ((!(paramSpring instanceof GroupLayout.AutoPreferredGapSpring)) || (!((GroupLayout.AutoPreferredGapSpring)paramSpring).getUserCreated()))
/*      */       {
/* 1570 */         GroupLayout.this.springsChanged = true;
/*      */       }
/* 1572 */       return this;
/*      */     }
/*      */ 
/*      */     void setSize(int paramInt1, int paramInt2, int paramInt3)
/*      */     {
/* 1580 */       super.setSize(paramInt1, paramInt2, paramInt3);
/* 1581 */       if (paramInt3 == -2147483648) {
/* 1582 */         for (int i = this.springs.size() - 1; i >= 0; 
/* 1583 */           i--)
/* 1584 */           getSpring(i).setSize(paramInt1, paramInt2, paramInt3);
/*      */       }
/*      */       else
/* 1587 */         setValidSize(paramInt1, paramInt2, paramInt3);
/*      */     }
/*      */ 
/*      */     abstract void setValidSize(int paramInt1, int paramInt2, int paramInt3);
/*      */ 
/*      */     int calculateMinimumSize(int paramInt)
/*      */     {
/* 1598 */       return calculateSize(paramInt, 0);
/*      */     }
/*      */ 
/*      */     int calculatePreferredSize(int paramInt) {
/* 1602 */       return calculateSize(paramInt, 1);
/*      */     }
/*      */ 
/*      */     int calculateMaximumSize(int paramInt) {
/* 1606 */       return calculateSize(paramInt, 2);
/*      */     }
/*      */ 
/*      */     int calculateSize(int paramInt1, int paramInt2)
/*      */     {
/* 1617 */       int i = this.springs.size();
/* 1618 */       if (i == 0) {
/* 1619 */         return 0;
/*      */       }
/* 1621 */       if (i == 1) {
/* 1622 */         return getSpringSize(getSpring(0), paramInt1, paramInt2);
/*      */       }
/* 1624 */       int j = constrain(operator(getSpringSize(getSpring(0), paramInt1, paramInt2), getSpringSize(getSpring(1), paramInt1, paramInt2)));
/*      */ 
/* 1626 */       for (int k = 2; k < i; k++) {
/* 1627 */         j = constrain(operator(j, getSpringSize(getSpring(k), paramInt1, paramInt2)));
/*      */       }
/*      */ 
/* 1630 */       return j;
/*      */     }
/*      */ 
/*      */     int getSpringSize(GroupLayout.Spring paramSpring, int paramInt1, int paramInt2) {
/* 1634 */       switch (paramInt2) {
/*      */       case 0:
/* 1636 */         return paramSpring.getMinimumSize(paramInt1);
/*      */       case 1:
/* 1638 */         return paramSpring.getPreferredSize(paramInt1);
/*      */       case 2:
/* 1640 */         return paramSpring.getMaximumSize(paramInt1);
/*      */       }
/* 1642 */       if (!$assertionsDisabled) throw new AssertionError();
/* 1643 */       return 0;
/*      */     }
/*      */ 
/*      */     abstract int operator(int paramInt1, int paramInt2);
/*      */ 
/*      */     abstract void insertAutopadding(int paramInt, List<GroupLayout.AutoPreferredGapSpring> paramList1, List<GroupLayout.AutoPreferredGapSpring> paramList2, List<GroupLayout.ComponentSpring> paramList3, List<GroupLayout.ComponentSpring> paramList4, boolean paramBoolean);
/*      */ 
/*      */     void removeAutopadding()
/*      */     {
/* 1684 */       unset();
/* 1685 */       for (int i = this.springs.size() - 1; i >= 0; i--) {
/* 1686 */         GroupLayout.Spring localSpring = (GroupLayout.Spring)this.springs.get(i);
/* 1687 */         if ((localSpring instanceof GroupLayout.AutoPreferredGapSpring)) {
/* 1688 */           if (((GroupLayout.AutoPreferredGapSpring)localSpring).getUserCreated())
/* 1689 */             ((GroupLayout.AutoPreferredGapSpring)localSpring).reset();
/*      */           else
/* 1691 */             this.springs.remove(i);
/*      */         }
/* 1693 */         else if ((localSpring instanceof Group))
/* 1694 */           ((Group)localSpring).removeAutopadding();
/*      */       }
/*      */     }
/*      */ 
/*      */     void unsetAutopadding()
/*      */     {
/* 1701 */       unset();
/* 1702 */       for (int i = this.springs.size() - 1; i >= 0; i--) {
/* 1703 */         GroupLayout.Spring localSpring = (GroupLayout.Spring)this.springs.get(i);
/* 1704 */         if ((localSpring instanceof GroupLayout.AutoPreferredGapSpring))
/* 1705 */           localSpring.unset();
/* 1706 */         else if ((localSpring instanceof Group))
/* 1707 */           ((Group)localSpring).unsetAutopadding();
/*      */       }
/*      */     }
/*      */ 
/*      */     void calculateAutopadding(int paramInt)
/*      */     {
/* 1713 */       for (int i = this.springs.size() - 1; i >= 0; i--) {
/* 1714 */         GroupLayout.Spring localSpring = (GroupLayout.Spring)this.springs.get(i);
/* 1715 */         if ((localSpring instanceof GroupLayout.AutoPreferredGapSpring))
/*      */         {
/* 1717 */           localSpring.unset();
/* 1718 */           ((GroupLayout.AutoPreferredGapSpring)localSpring).calculatePadding(paramInt);
/* 1719 */         } else if ((localSpring instanceof Group)) {
/* 1720 */           ((Group)localSpring).calculateAutopadding(paramInt);
/*      */         }
/*      */       }
/*      */ 
/* 1724 */       unset();
/*      */     }
/*      */ 
/*      */     boolean willHaveZeroSize(boolean paramBoolean)
/*      */     {
/* 1729 */       for (int i = this.springs.size() - 1; i >= 0; i--) {
/* 1730 */         GroupLayout.Spring localSpring = (GroupLayout.Spring)this.springs.get(i);
/* 1731 */         if (!localSpring.willHaveZeroSize(paramBoolean)) {
/* 1732 */           return false;
/*      */         }
/*      */       }
/* 1735 */       return true;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class LinkInfo
/*      */   {
/*      */     private final int axis;
/*      */     private final List<GroupLayout.ComponentInfo> linked;
/*      */     private int size;
/*      */ 
/*      */     LinkInfo(int paramInt)
/*      */     {
/* 3524 */       this.linked = new ArrayList();
/* 3525 */       this.size = -2147483648;
/* 3526 */       this.axis = paramInt;
/*      */     }
/*      */ 
/*      */     public void add(GroupLayout.ComponentInfo paramComponentInfo) {
/* 3530 */       LinkInfo localLinkInfo = paramComponentInfo.getLinkInfo(this.axis, false);
/* 3531 */       if (localLinkInfo == null) {
/* 3532 */         this.linked.add(paramComponentInfo);
/* 3533 */         paramComponentInfo.setLinkInfo(this.axis, this);
/* 3534 */       } else if (localLinkInfo != this) {
/* 3535 */         this.linked.addAll(localLinkInfo.linked);
/* 3536 */         for (GroupLayout.ComponentInfo localComponentInfo : localLinkInfo.linked) {
/* 3537 */           localComponentInfo.setLinkInfo(this.axis, this);
/*      */         }
/*      */       }
/* 3540 */       clearCachedSize();
/*      */     }
/*      */ 
/*      */     public void remove(GroupLayout.ComponentInfo paramComponentInfo) {
/* 3544 */       this.linked.remove(paramComponentInfo);
/* 3545 */       paramComponentInfo.setLinkInfo(this.axis, null);
/* 3546 */       if (this.linked.size() == 1) {
/* 3547 */         ((GroupLayout.ComponentInfo)this.linked.get(0)).setLinkInfo(this.axis, null);
/*      */       }
/* 3549 */       clearCachedSize();
/*      */     }
/*      */ 
/*      */     public void clearCachedSize() {
/* 3553 */       this.size = -2147483648;
/*      */     }
/*      */ 
/*      */     public int getSize(int paramInt) {
/* 3557 */       if (this.size == -2147483648) {
/* 3558 */         this.size = calculateLinkedSize(paramInt);
/*      */       }
/* 3560 */       return this.size;
/*      */     }
/*      */ 
/*      */     private int calculateLinkedSize(int paramInt) {
/* 3564 */       int i = 0;
/* 3565 */       for (GroupLayout.ComponentInfo localComponentInfo : this.linked)
/*      */       {
/*      */         GroupLayout.ComponentSpring localComponentSpring;
/* 3567 */         if (paramInt == 0) {
/* 3568 */           localComponentSpring = localComponentInfo.horizontalSpring;
/*      */         } else {
/* 3570 */           assert (paramInt == 1);
/* 3571 */           localComponentSpring = localComponentInfo.verticalSpring;
/*      */         }
/* 3573 */         i = Math.max(i, localComponentSpring.calculateNonlinkedPreferredSize(paramInt));
/*      */       }
/*      */ 
/* 3576 */       return i;
/*      */     }
/*      */   }
/*      */ 
/*      */   public class ParallelGroup extends GroupLayout.Group
/*      */   {
/*      */     private final GroupLayout.Alignment childAlignment;
/*      */     private final boolean resizable;
/*      */ 
/*      */     ParallelGroup(GroupLayout.Alignment paramBoolean, boolean arg3)
/*      */     {
/* 2460 */       super();
/* 2461 */       this.childAlignment = paramBoolean;
/*      */       boolean bool;
/* 2462 */       this.resizable = bool;
/*      */     }
/*      */ 
/*      */     public ParallelGroup addGroup(GroupLayout.Group paramGroup)
/*      */     {
/* 2469 */       return (ParallelGroup)super.addGroup(paramGroup);
/*      */     }
/*      */ 
/*      */     public ParallelGroup addComponent(Component paramComponent)
/*      */     {
/* 2476 */       return (ParallelGroup)super.addComponent(paramComponent);
/*      */     }
/*      */ 
/*      */     public ParallelGroup addComponent(Component paramComponent, int paramInt1, int paramInt2, int paramInt3)
/*      */     {
/* 2484 */       return (ParallelGroup)super.addComponent(paramComponent, paramInt1, paramInt2, paramInt3);
/*      */     }
/*      */ 
/*      */     public ParallelGroup addGap(int paramInt)
/*      */     {
/* 2491 */       return (ParallelGroup)super.addGap(paramInt);
/*      */     }
/*      */ 
/*      */     public ParallelGroup addGap(int paramInt1, int paramInt2, int paramInt3)
/*      */     {
/* 2498 */       return (ParallelGroup)super.addGap(paramInt1, paramInt2, paramInt3);
/*      */     }
/*      */ 
/*      */     public ParallelGroup addGroup(GroupLayout.Alignment paramAlignment, GroupLayout.Group paramGroup)
/*      */     {
/* 2514 */       checkChildAlignment(paramAlignment);
/* 2515 */       paramGroup.setAlignment(paramAlignment);
/* 2516 */       return (ParallelGroup)addSpring(paramGroup);
/*      */     }
/*      */ 
/*      */     public ParallelGroup addComponent(Component paramComponent, GroupLayout.Alignment paramAlignment)
/*      */     {
/* 2531 */       return addComponent(paramComponent, paramAlignment, -1, -1, -1);
/*      */     }
/*      */ 
/*      */     public ParallelGroup addComponent(Component paramComponent, GroupLayout.Alignment paramAlignment, int paramInt1, int paramInt2, int paramInt3)
/*      */     {
/* 2550 */       checkChildAlignment(paramAlignment);
/* 2551 */       GroupLayout.ComponentSpring localComponentSpring = new GroupLayout.ComponentSpring(GroupLayout.this, paramComponent, paramInt1, paramInt2, paramInt3, null);
/*      */ 
/* 2553 */       localComponentSpring.setAlignment(paramAlignment);
/* 2554 */       return (ParallelGroup)addSpring(localComponentSpring);
/*      */     }
/*      */ 
/*      */     boolean isResizable() {
/* 2558 */       return this.resizable;
/*      */     }
/*      */ 
/*      */     int operator(int paramInt1, int paramInt2) {
/* 2562 */       return Math.max(paramInt1, paramInt2);
/*      */     }
/*      */ 
/*      */     int calculateMinimumSize(int paramInt) {
/* 2566 */       if (!isResizable()) {
/* 2567 */         return getPreferredSize(paramInt);
/*      */       }
/* 2569 */       return super.calculateMinimumSize(paramInt);
/*      */     }
/*      */ 
/*      */     int calculateMaximumSize(int paramInt) {
/* 2573 */       if (!isResizable()) {
/* 2574 */         return getPreferredSize(paramInt);
/*      */       }
/* 2576 */       return super.calculateMaximumSize(paramInt);
/*      */     }
/*      */ 
/*      */     void setValidSize(int paramInt1, int paramInt2, int paramInt3) {
/* 2580 */       for (GroupLayout.Spring localSpring : this.springs)
/* 2581 */         setChildSize(localSpring, paramInt1, paramInt2, paramInt3);
/*      */     }
/*      */ 
/*      */     void setChildSize(GroupLayout.Spring paramSpring, int paramInt1, int paramInt2, int paramInt3)
/*      */     {
/* 2586 */       GroupLayout.Alignment localAlignment = paramSpring.getAlignment();
/* 2587 */       int i = Math.min(Math.max(paramSpring.getMinimumSize(paramInt1), paramInt3), paramSpring.getMaximumSize(paramInt1));
/*      */ 
/* 2590 */       if (localAlignment == null) {
/* 2591 */         localAlignment = this.childAlignment;
/*      */       }
/* 2593 */       switch (GroupLayout.1.$SwitchMap$javax$swing$GroupLayout$Alignment[localAlignment.ordinal()]) {
/*      */       case 1:
/* 2595 */         paramSpring.setSize(paramInt1, paramInt2 + paramInt3 - i, i);
/*      */ 
/* 2597 */         break;
/*      */       case 2:
/* 2599 */         paramSpring.setSize(paramInt1, paramInt2 + (paramInt3 - i) / 2, i);
/*      */ 
/* 2601 */         break;
/*      */       default:
/* 2603 */         paramSpring.setSize(paramInt1, paramInt2, i);
/*      */       }
/*      */     }
/*      */ 
/*      */     void insertAutopadding(int paramInt, List<GroupLayout.AutoPreferredGapSpring> paramList1, List<GroupLayout.AutoPreferredGapSpring> paramList2, List<GroupLayout.ComponentSpring> paramList3, List<GroupLayout.ComponentSpring> paramList4, boolean paramBoolean)
/*      */     {
/* 2614 */       for (GroupLayout.Spring localSpring : this.springs)
/* 2615 */         if ((localSpring instanceof GroupLayout.ComponentSpring)) {
/* 2616 */           if (((GroupLayout.ComponentSpring)localSpring).isVisible())
/*      */           {
/* 2618 */             for (GroupLayout.AutoPreferredGapSpring localAutoPreferredGapSpring : paramList1) {
/* 2619 */               localAutoPreferredGapSpring.addTarget((GroupLayout.ComponentSpring)localSpring, paramInt);
/*      */             }
/* 2621 */             paramList4.add((GroupLayout.ComponentSpring)localSpring);
/*      */           }
/* 2623 */         } else if ((localSpring instanceof GroupLayout.Group)) {
/* 2624 */           ((GroupLayout.Group)localSpring).insertAutopadding(paramInt, paramList1, paramList2, paramList3, paramList4, paramBoolean);
/*      */         }
/* 2626 */         else if ((localSpring instanceof GroupLayout.AutoPreferredGapSpring)) {
/* 2627 */           ((GroupLayout.AutoPreferredGapSpring)localSpring).setSources(paramList3);
/* 2628 */           paramList2.add((GroupLayout.AutoPreferredGapSpring)localSpring);
/*      */         }
/*      */     }
/*      */ 
/*      */     private void checkChildAlignment(GroupLayout.Alignment paramAlignment)
/*      */     {
/* 2634 */       checkChildAlignment(paramAlignment, this instanceof GroupLayout.BaselineGroup);
/*      */     }
/*      */ 
/*      */     private void checkChildAlignment(GroupLayout.Alignment paramAlignment, boolean paramBoolean)
/*      */     {
/* 2639 */       if (paramAlignment == null) {
/* 2640 */         throw new IllegalArgumentException("Alignment must be non-null");
/*      */       }
/* 2642 */       if ((!paramBoolean) && (paramAlignment == GroupLayout.Alignment.BASELINE))
/* 2643 */         throw new IllegalArgumentException("Alignment must be one of:LEADING, TRAILING or CENTER");
/*      */     }
/*      */   }
/*      */ 
/*      */   private class PreferredGapSpring extends GroupLayout.Spring
/*      */   {
/*      */     private final JComponent source;
/*      */     private final JComponent target;
/*      */     private final LayoutStyle.ComponentPlacement type;
/*      */     private final int pref;
/*      */     private final int max;
/*      */ 
/*      */     PreferredGapSpring(JComponent paramJComponent1, JComponent paramComponentPlacement, LayoutStyle.ComponentPlacement paramInt1, int paramInt2, int arg6)
/*      */     {
/* 3118 */       super();
/* 3119 */       this.source = paramJComponent1;
/* 3120 */       this.target = paramComponentPlacement;
/* 3121 */       this.type = paramInt1;
/* 3122 */       this.pref = paramInt2;
/*      */       int i;
/* 3123 */       this.max = i;
/*      */     }
/*      */ 
/*      */     int calculateMinimumSize(int paramInt) {
/* 3127 */       return getPadding(paramInt);
/*      */     }
/*      */ 
/*      */     int calculatePreferredSize(int paramInt) {
/* 3131 */       if ((this.pref == -1) || (this.pref == -2)) {
/* 3132 */         return getMinimumSize(paramInt);
/*      */       }
/* 3134 */       int i = getMinimumSize(paramInt);
/* 3135 */       int j = getMaximumSize(paramInt);
/* 3136 */       return Math.min(j, Math.max(i, this.pref));
/*      */     }
/*      */ 
/*      */     int calculateMaximumSize(int paramInt) {
/* 3140 */       if ((this.max == -2) || (this.max == -1)) {
/* 3141 */         return getPadding(paramInt);
/*      */       }
/* 3143 */       return Math.max(getMinimumSize(paramInt), this.max);
/*      */     }
/*      */ 
/*      */     private int getPadding(int paramInt)
/*      */     {
/*      */       int i;
/* 3148 */       if (paramInt == 0)
/* 3149 */         i = 3;
/*      */       else {
/* 3151 */         i = 5;
/*      */       }
/* 3153 */       return GroupLayout.this.getLayoutStyle0().getPreferredGap(this.source, this.target, this.type, i, GroupLayout.this.host);
/*      */     }
/*      */ 
/*      */     boolean willHaveZeroSize(boolean paramBoolean)
/*      */     {
/* 3159 */       return false;
/*      */     }
/*      */   }
/*      */ 
/*      */   public class SequentialGroup extends GroupLayout.Group
/*      */   {
/*      */     private GroupLayout.Spring baselineSpring;
/*      */ 
/*      */     SequentialGroup()
/*      */     {
/* 1761 */       super();
/*      */     }
/*      */ 
/*      */     public SequentialGroup addGroup(GroupLayout.Group paramGroup)
/*      */     {
/* 1768 */       return (SequentialGroup)super.addGroup(paramGroup);
/*      */     }
/*      */ 
/*      */     public SequentialGroup addGroup(boolean paramBoolean, GroupLayout.Group paramGroup)
/*      */     {
/* 1780 */       super.addGroup(paramGroup);
/* 1781 */       if (paramBoolean) {
/* 1782 */         this.baselineSpring = paramGroup;
/*      */       }
/* 1784 */       return this;
/*      */     }
/*      */ 
/*      */     public SequentialGroup addComponent(Component paramComponent)
/*      */     {
/* 1791 */       return (SequentialGroup)super.addComponent(paramComponent);
/*      */     }
/*      */ 
/*      */     public SequentialGroup addComponent(boolean paramBoolean, Component paramComponent)
/*      */     {
/* 1804 */       super.addComponent(paramComponent);
/* 1805 */       if (paramBoolean) {
/* 1806 */         this.baselineSpring = ((GroupLayout.Spring)this.springs.get(this.springs.size() - 1));
/*      */       }
/* 1808 */       return this;
/*      */     }
/*      */ 
/*      */     public SequentialGroup addComponent(Component paramComponent, int paramInt1, int paramInt2, int paramInt3)
/*      */     {
/* 1816 */       return (SequentialGroup)super.addComponent(paramComponent, paramInt1, paramInt2, paramInt3);
/*      */     }
/*      */ 
/*      */     public SequentialGroup addComponent(boolean paramBoolean, Component paramComponent, int paramInt1, int paramInt2, int paramInt3)
/*      */     {
/* 1837 */       super.addComponent(paramComponent, paramInt1, paramInt2, paramInt3);
/* 1838 */       if (paramBoolean) {
/* 1839 */         this.baselineSpring = ((GroupLayout.Spring)this.springs.get(this.springs.size() - 1));
/*      */       }
/* 1841 */       return this;
/*      */     }
/*      */ 
/*      */     public SequentialGroup addGap(int paramInt)
/*      */     {
/* 1848 */       return (SequentialGroup)super.addGap(paramInt);
/*      */     }
/*      */ 
/*      */     public SequentialGroup addGap(int paramInt1, int paramInt2, int paramInt3)
/*      */     {
/* 1855 */       return (SequentialGroup)super.addGap(paramInt1, paramInt2, paramInt3);
/*      */     }
/*      */ 
/*      */     public SequentialGroup addPreferredGap(JComponent paramJComponent1, JComponent paramJComponent2, LayoutStyle.ComponentPlacement paramComponentPlacement)
/*      */     {
/* 1874 */       return addPreferredGap(paramJComponent1, paramJComponent2, paramComponentPlacement, -1, -2);
/*      */     }
/*      */ 
/*      */     public SequentialGroup addPreferredGap(JComponent paramJComponent1, JComponent paramJComponent2, LayoutStyle.ComponentPlacement paramComponentPlacement, int paramInt1, int paramInt2)
/*      */     {
/* 1898 */       if (paramComponentPlacement == null) {
/* 1899 */         throw new IllegalArgumentException("Type must be non-null");
/*      */       }
/* 1901 */       if ((paramJComponent1 == null) || (paramJComponent2 == null)) {
/* 1902 */         throw new IllegalArgumentException("Components must be non-null");
/*      */       }
/*      */ 
/* 1905 */       checkPreferredGapValues(paramInt1, paramInt2);
/* 1906 */       return (SequentialGroup)addSpring(new GroupLayout.PreferredGapSpring(GroupLayout.this, paramJComponent1, paramJComponent2, paramComponentPlacement, paramInt1, paramInt2));
/*      */     }
/*      */ 
/*      */     public SequentialGroup addPreferredGap(LayoutStyle.ComponentPlacement paramComponentPlacement)
/*      */     {
/* 1930 */       return addPreferredGap(paramComponentPlacement, -1, -1);
/*      */     }
/*      */ 
/*      */     public SequentialGroup addPreferredGap(LayoutStyle.ComponentPlacement paramComponentPlacement, int paramInt1, int paramInt2)
/*      */     {
/* 1957 */       if ((paramComponentPlacement != LayoutStyle.ComponentPlacement.RELATED) && (paramComponentPlacement != LayoutStyle.ComponentPlacement.UNRELATED))
/*      */       {
/* 1959 */         throw new IllegalArgumentException("Type must be one of LayoutStyle.ComponentPlacement.RELATED or LayoutStyle.ComponentPlacement.UNRELATED");
/*      */       }
/*      */ 
/* 1964 */       checkPreferredGapValues(paramInt1, paramInt2);
/* 1965 */       GroupLayout.this.hasPreferredPaddingSprings = true;
/* 1966 */       return (SequentialGroup)addSpring(new GroupLayout.AutoPreferredGapSpring(GroupLayout.this, paramComponentPlacement, paramInt1, paramInt2));
/*      */     }
/*      */ 
/*      */     public SequentialGroup addContainerGap()
/*      */     {
/* 1982 */       return addContainerGap(-1, -1);
/*      */     }
/*      */ 
/*      */     public SequentialGroup addContainerGap(int paramInt1, int paramInt2)
/*      */     {
/* 2000 */       if (((paramInt1 < 0) && (paramInt1 != -1)) || ((paramInt2 < 0) && (paramInt2 != -1) && (paramInt2 != -2)) || ((paramInt1 >= 0) && (paramInt2 >= 0) && (paramInt1 > paramInt2)))
/*      */       {
/* 2003 */         throw new IllegalArgumentException("Pref and max must be either DEFAULT_VALUE or >= 0 and pref <= max");
/*      */       }
/*      */ 
/* 2007 */       GroupLayout.this.hasPreferredPaddingSprings = true;
/* 2008 */       return (SequentialGroup)addSpring(new GroupLayout.ContainerAutoPreferredGapSpring(GroupLayout.this, paramInt1, paramInt2));
/*      */     }
/*      */ 
/*      */     int operator(int paramInt1, int paramInt2)
/*      */     {
/* 2013 */       return constrain(paramInt1) + constrain(paramInt2);
/*      */     }
/*      */ 
/*      */     void setValidSize(int paramInt1, int paramInt2, int paramInt3) {
/* 2017 */       int i = getPreferredSize(paramInt1);
/*      */       Object localObject;
/* 2018 */       if (paramInt3 == i)
/*      */       {
/* 2020 */         for (localObject = this.springs.iterator(); ((Iterator)localObject).hasNext(); ) { GroupLayout.Spring localSpring = (GroupLayout.Spring)((Iterator)localObject).next();
/* 2021 */           int j = localSpring.getPreferredSize(paramInt1);
/* 2022 */           localSpring.setSize(paramInt1, paramInt2, j);
/* 2023 */           paramInt2 += j; }
/*      */       }
/* 2025 */       else if (this.springs.size() == 1) {
/* 2026 */         localObject = getSpring(0);
/* 2027 */         ((GroupLayout.Spring)localObject).setSize(paramInt1, paramInt2, Math.min(Math.max(paramInt3, ((GroupLayout.Spring)localObject).getMinimumSize(paramInt1)), ((GroupLayout.Spring)localObject).getMaximumSize(paramInt1)));
/*      */       }
/* 2030 */       else if (this.springs.size() > 1)
/*      */       {
/* 2032 */         setValidSizeNotPreferred(paramInt1, paramInt2, paramInt3);
/*      */       }
/*      */     }
/*      */ 
/*      */     private void setValidSizeNotPreferred(int paramInt1, int paramInt2, int paramInt3) {
/* 2037 */       int i = paramInt3 - getPreferredSize(paramInt1);
/* 2038 */       assert (i != 0);
/* 2039 */       boolean bool = i < 0;
/* 2040 */       int j = this.springs.size();
/* 2041 */       if (bool) {
/* 2042 */         i *= -1;
/*      */       }
/*      */ 
/* 2057 */       List localList = buildResizableList(paramInt1, bool);
/* 2058 */       int k = localList.size();
/*      */       int m;
/* 2060 */       if (k > 0)
/*      */       {
/* 2062 */         m = i / k;
/*      */ 
/* 2064 */         int n = i - m * k;
/* 2065 */         int[] arrayOfInt = new int[j];
/* 2066 */         int i2 = bool ? -1 : 1;
/*      */         Object localObject;
/* 2069 */         for (int i3 = 0; i3 < k; i3++) {
/* 2070 */           localObject = (GroupLayout.SpringDelta)localList.get(i3);
/* 2071 */           if (i3 + 1 == k) {
/* 2072 */             m += n;
/*      */           }
/* 2074 */           ((GroupLayout.SpringDelta)localObject).delta = Math.min(m, ((GroupLayout.SpringDelta)localObject).delta);
/* 2075 */           i -= ((GroupLayout.SpringDelta)localObject).delta;
/* 2076 */           if ((((GroupLayout.SpringDelta)localObject).delta != m) && (i3 + 1 < k))
/*      */           {
/* 2080 */             m = i / (k - i3 - 1);
/* 2081 */             n = i - m * (k - i3 - 1);
/*      */           }
/* 2083 */           arrayOfInt[localObject.index] = (i2 * ((GroupLayout.SpringDelta)localObject).delta);
/*      */         }
/*      */ 
/* 2087 */         for (i3 = 0; i3 < j; i3++) {
/* 2088 */           localObject = getSpring(i3);
/* 2089 */           int i4 = ((GroupLayout.Spring)localObject).getPreferredSize(paramInt1) + arrayOfInt[i3];
/* 2090 */           ((GroupLayout.Spring)localObject).setSize(paramInt1, paramInt2, i4);
/* 2091 */           paramInt2 += i4;
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/* 2096 */         for (m = 0; m < j; m++) {
/* 2097 */           GroupLayout.Spring localSpring = getSpring(m);
/*      */           int i1;
/* 2099 */           if (bool)
/* 2100 */             i1 = localSpring.getMinimumSize(paramInt1);
/*      */           else {
/* 2102 */             i1 = localSpring.getMaximumSize(paramInt1);
/*      */           }
/* 2104 */           localSpring.setSize(paramInt1, paramInt2, i1);
/* 2105 */           paramInt2 += i1;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     private List<GroupLayout.SpringDelta> buildResizableList(int paramInt, boolean paramBoolean)
/*      */     {
/* 2118 */       int i = this.springs.size();
/* 2119 */       ArrayList localArrayList = new ArrayList(i);
/* 2120 */       for (int j = 0; j < i; j++) {
/* 2121 */         GroupLayout.Spring localSpring = getSpring(j);
/*      */         int k;
/* 2123 */         if (paramBoolean) {
/* 2124 */           k = localSpring.getPreferredSize(paramInt) - localSpring.getMinimumSize(paramInt);
/*      */         }
/*      */         else {
/* 2127 */           k = localSpring.getMaximumSize(paramInt) - localSpring.getPreferredSize(paramInt);
/*      */         }
/*      */ 
/* 2130 */         if (k > 0) {
/* 2131 */           localArrayList.add(new GroupLayout.SpringDelta(j, k));
/*      */         }
/*      */       }
/* 2134 */       Collections.sort(localArrayList);
/* 2135 */       return localArrayList;
/*      */     }
/*      */ 
/*      */     private int indexOfNextNonZeroSpring(int paramInt, boolean paramBoolean)
/*      */     {
/* 2140 */       while (paramInt < this.springs.size()) {
/* 2141 */         GroupLayout.Spring localSpring = (GroupLayout.Spring)this.springs.get(paramInt);
/* 2142 */         if (!localSpring.willHaveZeroSize(paramBoolean)) {
/* 2143 */           return paramInt;
/*      */         }
/* 2145 */         paramInt++;
/*      */       }
/* 2147 */       return paramInt;
/*      */     }
/*      */ 
/*      */     void insertAutopadding(int paramInt, List<GroupLayout.AutoPreferredGapSpring> paramList1, List<GroupLayout.AutoPreferredGapSpring> paramList2, List<GroupLayout.ComponentSpring> paramList3, List<GroupLayout.ComponentSpring> paramList4, boolean paramBoolean)
/*      */     {
/* 2156 */       ArrayList localArrayList1 = new ArrayList(paramList1);
/*      */ 
/* 2158 */       ArrayList localArrayList2 = new ArrayList(1);
/*      */ 
/* 2160 */       ArrayList localArrayList3 = new ArrayList(paramList3);
/*      */ 
/* 2162 */       ArrayList localArrayList4 = null;
/* 2163 */       int i = 0;
/*      */ 
/* 2166 */       while (i < this.springs.size()) {
/* 2167 */         GroupLayout.Spring localSpring = getSpring(i);
/*      */         Object localObject;
/* 2168 */         if ((localSpring instanceof GroupLayout.AutoPreferredGapSpring)) {
/* 2169 */           if (localArrayList1.size() == 0)
/*      */           {
/* 2172 */             localObject = (GroupLayout.AutoPreferredGapSpring)localSpring;
/*      */ 
/* 2174 */             ((GroupLayout.AutoPreferredGapSpring)localObject).setSources(localArrayList3);
/* 2175 */             localArrayList3.clear();
/* 2176 */             i = indexOfNextNonZeroSpring(i + 1, true);
/* 2177 */             if (i == this.springs.size())
/*      */             {
/* 2180 */               if (!(localObject instanceof GroupLayout.ContainerAutoPreferredGapSpring))
/*      */               {
/* 2182 */                 paramList2.add(localObject);
/*      */               }
/*      */             } else {
/* 2185 */               localArrayList1.clear();
/* 2186 */               localArrayList1.add(localObject);
/*      */             }
/*      */           } else {
/* 2189 */             i = indexOfNextNonZeroSpring(i + 1, true);
/*      */           }
/*      */ 
/*      */         }
/* 2193 */         else if ((localArrayList3.size() > 0) && (paramBoolean))
/*      */         {
/* 2196 */           localObject = new GroupLayout.AutoPreferredGapSpring(GroupLayout.this, null);
/*      */ 
/* 2200 */           this.springs.add(i, localObject);
/*      */         }
/* 2203 */         else if ((localSpring instanceof GroupLayout.ComponentSpring))
/*      */         {
/* 2206 */           localObject = (GroupLayout.ComponentSpring)localSpring;
/* 2207 */           if (!((GroupLayout.ComponentSpring)localObject).isVisible()) {
/* 2208 */             i++;
/*      */           }
/*      */           else {
/* 2211 */             for (GroupLayout.AutoPreferredGapSpring localAutoPreferredGapSpring : localArrayList1) {
/* 2212 */               localAutoPreferredGapSpring.addTarget((GroupLayout.ComponentSpring)localObject, paramInt);
/*      */             }
/* 2214 */             localArrayList3.clear();
/* 2215 */             localArrayList1.clear();
/* 2216 */             i = indexOfNextNonZeroSpring(i + 1, false);
/* 2217 */             if (i == this.springs.size())
/*      */             {
/* 2219 */               paramList4.add(localObject);
/*      */             }
/*      */             else
/* 2222 */               localArrayList3.add(localObject);
/*      */           }
/* 2224 */         } else if ((localSpring instanceof GroupLayout.Group))
/*      */         {
/* 2226 */           if (localArrayList4 == null)
/* 2227 */             localArrayList4 = new ArrayList(1);
/*      */           else {
/* 2229 */             localArrayList4.clear();
/*      */           }
/* 2231 */           localArrayList2.clear();
/* 2232 */           ((GroupLayout.Group)localSpring).insertAutopadding(paramInt, localArrayList1, localArrayList2, localArrayList3, localArrayList4, paramBoolean);
/*      */ 
/* 2235 */           localArrayList3.clear();
/* 2236 */           localArrayList1.clear();
/* 2237 */           i = indexOfNextNonZeroSpring(i + 1, localArrayList4.size() == 0);
/*      */ 
/* 2239 */           if (i == this.springs.size()) {
/* 2240 */             paramList4.addAll(localArrayList4);
/* 2241 */             paramList2.addAll(localArrayList2);
/*      */           } else {
/* 2243 */             localArrayList3.addAll(localArrayList4);
/* 2244 */             localArrayList1.addAll(localArrayList2);
/*      */           }
/*      */         }
/*      */         else {
/* 2248 */           localArrayList1.clear();
/* 2249 */           localArrayList3.clear();
/* 2250 */           i++;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     int getBaseline()
/*      */     {
/*      */       int i;
/*      */       int j;
/* 2257 */       if (this.baselineSpring != null) {
/* 2258 */         i = this.baselineSpring.getBaseline();
/* 2259 */         if (i >= 0) {
/* 2260 */           j = 0;
/* 2261 */           for (GroupLayout.Spring localSpring : this.springs) {
/* 2262 */             if (localSpring == this.baselineSpring) {
/* 2263 */               return j + i;
/*      */             }
/* 2265 */             j += localSpring.getPreferredSize(1);
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/* 2270 */       return -1;
/*      */     }
/*      */ 
/*      */     Component.BaselineResizeBehavior getBaselineResizeBehavior() {
/* 2274 */       if (isResizable(1)) {
/* 2275 */         if (!this.baselineSpring.isResizable(1))
/*      */         {
/* 2279 */           int i = 0;
/* 2280 */           for (GroupLayout.Spring localSpring1 : this.springs) {
/* 2281 */             if (localSpring1 == this.baselineSpring)
/*      */               break;
/* 2283 */             if (localSpring1.isResizable(1)) {
/* 2284 */               i = 1;
/* 2285 */               break;
/*      */             }
/*      */           }
/* 2288 */           int j = 0;
/* 2289 */           for (int m = this.springs.size() - 1; m >= 0; m--) {
/* 2290 */             GroupLayout.Spring localSpring3 = (GroupLayout.Spring)this.springs.get(m);
/* 2291 */             if (localSpring3 == this.baselineSpring) {
/*      */               break;
/*      */             }
/* 2294 */             if (localSpring3.isResizable(1)) {
/* 2295 */               j = 1;
/* 2296 */               break;
/*      */             }
/*      */           }
/* 2299 */           if ((i != 0) && (j == 0))
/* 2300 */             return Component.BaselineResizeBehavior.CONSTANT_DESCENT;
/* 2301 */           if ((i == 0) && (j != 0)) {
/* 2302 */             return Component.BaselineResizeBehavior.CONSTANT_ASCENT;
/*      */           }
/*      */         }
/*      */         else
/*      */         {
/* 2307 */           Component.BaselineResizeBehavior localBaselineResizeBehavior = this.baselineSpring.getBaselineResizeBehavior();
/*      */           Iterator localIterator2;
/*      */           GroupLayout.Spring localSpring2;
/* 2308 */           if (localBaselineResizeBehavior == Component.BaselineResizeBehavior.CONSTANT_ASCENT) {
/* 2309 */             for (localIterator2 = this.springs.iterator(); localIterator2.hasNext(); ) { localSpring2 = (GroupLayout.Spring)localIterator2.next();
/* 2310 */               if (localSpring2 == this.baselineSpring) {
/* 2311 */                 return Component.BaselineResizeBehavior.CONSTANT_ASCENT;
/*      */               }
/* 2313 */               if (localSpring2.isResizable(1))
/* 2314 */                 return Component.BaselineResizeBehavior.OTHER;
/*      */             }
/*      */           }
/* 2317 */           else if (localBaselineResizeBehavior == Component.BaselineResizeBehavior.CONSTANT_DESCENT) {
/* 2318 */             for (int k = this.springs.size() - 1; k >= 0; k--) {
/* 2319 */               localSpring2 = (GroupLayout.Spring)this.springs.get(k);
/* 2320 */               if (localSpring2 == this.baselineSpring) {
/* 2321 */                 return Component.BaselineResizeBehavior.CONSTANT_DESCENT;
/*      */               }
/* 2323 */               if (localSpring2.isResizable(1)) {
/* 2324 */                 return Component.BaselineResizeBehavior.OTHER;
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/* 2329 */         return Component.BaselineResizeBehavior.OTHER;
/*      */       }
/*      */ 
/* 2332 */       return Component.BaselineResizeBehavior.CONSTANT_ASCENT;
/*      */     }
/*      */ 
/*      */     private void checkPreferredGapValues(int paramInt1, int paramInt2) {
/* 2336 */       if (((paramInt1 < 0) && (paramInt1 != -1) && (paramInt1 != -2)) || ((paramInt2 < 0) && (paramInt2 != -1) && (paramInt2 != -2)) || ((paramInt1 >= 0) && (paramInt2 >= 0) && (paramInt1 > paramInt2)))
/*      */       {
/* 2339 */         throw new IllegalArgumentException("Pref and max must be either DEFAULT_SIZE, PREFERRED_SIZE, or >= 0 and pref <= max");
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private abstract class Spring
/*      */   {
/*      */     private int size;
/*      */     private int min;
/*      */     private int max;
/*      */     private int pref;
/*      */     private Spring parent;
/*      */     private GroupLayout.Alignment alignment;
/*      */ 
/*      */     Spring()
/*      */     {
/* 1277 */       this.min = (this.pref = this.max = -2147483648);
/*      */     }
/*      */ 
/*      */     abstract int calculateMinimumSize(int paramInt);
/*      */ 
/*      */     abstract int calculatePreferredSize(int paramInt);
/*      */ 
/*      */     abstract int calculateMaximumSize(int paramInt);
/*      */ 
/*      */     void setParent(Spring paramSpring)
/*      */     {
/* 1308 */       this.parent = paramSpring;
/*      */     }
/*      */ 
/*      */     Spring getParent()
/*      */     {
/* 1315 */       return this.parent;
/*      */     }
/*      */ 
/*      */     void setAlignment(GroupLayout.Alignment paramAlignment)
/*      */     {
/* 1321 */       this.alignment = paramAlignment;
/*      */     }
/*      */ 
/*      */     GroupLayout.Alignment getAlignment()
/*      */     {
/* 1328 */       return this.alignment;
/*      */     }
/*      */ 
/*      */     final int getMinimumSize(int paramInt)
/*      */     {
/* 1335 */       if (this.min == -2147483648) {
/* 1336 */         this.min = constrain(calculateMinimumSize(paramInt));
/*      */       }
/* 1338 */       return this.min;
/*      */     }
/*      */ 
/*      */     final int getPreferredSize(int paramInt)
/*      */     {
/* 1345 */       if (this.pref == -2147483648) {
/* 1346 */         this.pref = constrain(calculatePreferredSize(paramInt));
/*      */       }
/* 1348 */       return this.pref;
/*      */     }
/*      */ 
/*      */     final int getMaximumSize(int paramInt)
/*      */     {
/* 1355 */       if (this.max == -2147483648) {
/* 1356 */         this.max = constrain(calculateMaximumSize(paramInt));
/*      */       }
/* 1358 */       return this.max;
/*      */     }
/*      */ 
/*      */     void setSize(int paramInt1, int paramInt2, int paramInt3)
/*      */     {
/* 1371 */       this.size = paramInt3;
/* 1372 */       if (paramInt3 == -2147483648)
/* 1373 */         unset();
/*      */     }
/*      */ 
/*      */     void unset()
/*      */     {
/* 1381 */       this.size = (this.min = this.pref = this.max = -2147483648);
/*      */     }
/*      */ 
/*      */     int getSize()
/*      */     {
/* 1388 */       return this.size;
/*      */     }
/*      */ 
/*      */     int constrain(int paramInt) {
/* 1392 */       return Math.min(paramInt, 32767);
/*      */     }
/*      */ 
/*      */     int getBaseline() {
/* 1396 */       return -1;
/*      */     }
/*      */ 
/*      */     Component.BaselineResizeBehavior getBaselineResizeBehavior() {
/* 1400 */       return Component.BaselineResizeBehavior.OTHER;
/*      */     }
/*      */ 
/*      */     final boolean isResizable(int paramInt) {
/* 1404 */       int i = getMinimumSize(paramInt);
/* 1405 */       int j = getPreferredSize(paramInt);
/* 1406 */       return (i != j) || (j != getMaximumSize(paramInt));
/*      */     }
/*      */ 
/*      */     abstract boolean willHaveZeroSize(boolean paramBoolean);
/*      */   }
/*      */ 
/*      */   private static final class SpringDelta
/*      */     implements Comparable<SpringDelta>
/*      */   {
/*      */     public final int index;
/*      */     public int delta;
/*      */ 
/*      */     public SpringDelta(int paramInt1, int paramInt2)
/*      */     {
/* 2357 */       this.index = paramInt1;
/* 2358 */       this.delta = paramInt2;
/*      */     }
/*      */ 
/*      */     public int compareTo(SpringDelta paramSpringDelta) {
/* 2362 */       return this.delta - paramSpringDelta.delta;
/*      */     }
/*      */ 
/*      */     public String toString() {
/* 2366 */       return super.toString() + "[index=" + this.index + ", delta=" + this.delta + "]";
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.GroupLayout
 * JD-Core Version:    0.6.2
 */