/*      */ package javax.swing;
/*      */ 
/*      */ import java.awt.Component;
/*      */ import java.awt.Container;
/*      */ import java.awt.Dimension;
/*      */ import java.awt.Insets;
/*      */ import java.awt.LayoutManager2;
/*      */ import java.io.PrintStream;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collections;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ 
/*      */ public class SpringLayout
/*      */   implements LayoutManager2
/*      */ {
/*  188 */   private Map<Component, Constraints> componentConstraints = new HashMap();
/*      */ 
/*  190 */   private Spring cyclicReference = Spring.constant(-2147483648);
/*      */   private Set<Spring> cyclicSprings;
/*      */   private Set<Spring> acyclicSprings;
/*      */   public static final String NORTH = "North";
/*      */   public static final String SOUTH = "South";
/*      */   public static final String EAST = "East";
/*      */   public static final String WEST = "West";
/*      */   public static final String HORIZONTAL_CENTER = "HorizontalCenter";
/*      */   public static final String VERTICAL_CENTER = "VerticalCenter";
/*      */   public static final String BASELINE = "Baseline";
/*      */   public static final String WIDTH = "Width";
/*      */   public static final String HEIGHT = "Height";
/*  250 */   private static String[] ALL_HORIZONTAL = { "West", "Width", "East", "HorizontalCenter" };
/*      */ 
/*  252 */   private static String[] ALL_VERTICAL = { "North", "Height", "South", "VerticalCenter", "Baseline" };
/*      */ 
/*      */   private void resetCyclicStatuses()
/*      */   {
/*  885 */     this.cyclicSprings = new HashSet();
/*  886 */     this.acyclicSprings = new HashSet();
/*      */   }
/*      */ 
/*      */   private void setParent(Container paramContainer) {
/*  890 */     resetCyclicStatuses();
/*  891 */     Constraints localConstraints = getConstraints(paramContainer);
/*      */ 
/*  893 */     localConstraints.setX(Spring.constant(0));
/*  894 */     localConstraints.setY(Spring.constant(0));
/*      */ 
/*  904 */     Spring localSpring1 = localConstraints.getWidth();
/*  905 */     if (((localSpring1 instanceof Spring.WidthSpring)) && (((Spring.WidthSpring)localSpring1).c == paramContainer)) {
/*  906 */       localConstraints.setWidth(Spring.constant(0, 0, 2147483647));
/*      */     }
/*  908 */     Spring localSpring2 = localConstraints.getHeight();
/*  909 */     if (((localSpring2 instanceof Spring.HeightSpring)) && (((Spring.HeightSpring)localSpring2).c == paramContainer))
/*  910 */       localConstraints.setHeight(Spring.constant(0, 0, 2147483647));
/*      */   }
/*      */ 
/*      */   boolean isCyclic(Spring paramSpring)
/*      */   {
/*  915 */     if (paramSpring == null) {
/*  916 */       return false;
/*      */     }
/*  918 */     if (this.cyclicSprings.contains(paramSpring)) {
/*  919 */       return true;
/*      */     }
/*  921 */     if (this.acyclicSprings.contains(paramSpring)) {
/*  922 */       return false;
/*      */     }
/*  924 */     this.cyclicSprings.add(paramSpring);
/*  925 */     boolean bool = paramSpring.isCyclic(this);
/*  926 */     if (!bool) {
/*  927 */       this.acyclicSprings.add(paramSpring);
/*  928 */       this.cyclicSprings.remove(paramSpring);
/*      */     }
/*      */     else {
/*  931 */       System.err.println(paramSpring + " is cyclic. ");
/*      */     }
/*  933 */     return bool;
/*      */   }
/*      */ 
/*      */   private Spring abandonCycles(Spring paramSpring) {
/*  937 */     return isCyclic(paramSpring) ? this.cyclicReference : paramSpring;
/*      */   }
/*      */ 
/*      */   public void addLayoutComponent(String paramString, Component paramComponent)
/*      */   {
/*      */   }
/*      */ 
/*      */   public void removeLayoutComponent(Component paramComponent)
/*      */   {
/*  955 */     this.componentConstraints.remove(paramComponent);
/*      */   }
/*      */ 
/*      */   private static Dimension addInsets(int paramInt1, int paramInt2, Container paramContainer) {
/*  959 */     Insets localInsets = paramContainer.getInsets();
/*  960 */     return new Dimension(paramInt1 + localInsets.left + localInsets.right, paramInt2 + localInsets.top + localInsets.bottom);
/*      */   }
/*      */ 
/*      */   public Dimension minimumLayoutSize(Container paramContainer) {
/*  964 */     setParent(paramContainer);
/*  965 */     Constraints localConstraints = getConstraints(paramContainer);
/*  966 */     return addInsets(abandonCycles(localConstraints.getWidth()).getMinimumValue(), abandonCycles(localConstraints.getHeight()).getMinimumValue(), paramContainer);
/*      */   }
/*      */ 
/*      */   public Dimension preferredLayoutSize(Container paramContainer)
/*      */   {
/*  972 */     setParent(paramContainer);
/*  973 */     Constraints localConstraints = getConstraints(paramContainer);
/*  974 */     return addInsets(abandonCycles(localConstraints.getWidth()).getPreferredValue(), abandonCycles(localConstraints.getHeight()).getPreferredValue(), paramContainer);
/*      */   }
/*      */ 
/*      */   public Dimension maximumLayoutSize(Container paramContainer)
/*      */   {
/*  982 */     setParent(paramContainer);
/*  983 */     Constraints localConstraints = getConstraints(paramContainer);
/*  984 */     return addInsets(abandonCycles(localConstraints.getWidth()).getMaximumValue(), abandonCycles(localConstraints.getHeight()).getMaximumValue(), paramContainer);
/*      */   }
/*      */ 
/*      */   public void addLayoutComponent(Component paramComponent, Object paramObject)
/*      */   {
/* 1000 */     if ((paramObject instanceof Constraints))
/* 1001 */       putConstraints(paramComponent, (Constraints)paramObject);
/*      */   }
/*      */ 
/*      */   public float getLayoutAlignmentX(Container paramContainer)
/*      */   {
/* 1009 */     return 0.5F;
/*      */   }
/*      */ 
/*      */   public float getLayoutAlignmentY(Container paramContainer)
/*      */   {
/* 1016 */     return 0.5F;
/*      */   }
/*      */ 
/*      */   public void invalidateLayout(Container paramContainer)
/*      */   {
/*      */   }
/*      */ 
/*      */   public void putConstraint(String paramString1, Component paramComponent1, int paramInt, String paramString2, Component paramComponent2)
/*      */   {
/* 1041 */     putConstraint(paramString1, paramComponent1, Spring.constant(paramInt), paramString2, paramComponent2);
/*      */   }
/*      */ 
/*      */   public void putConstraint(String paramString1, Component paramComponent1, Spring paramSpring, String paramString2, Component paramComponent2)
/*      */   {
/* 1075 */     putConstraint(paramString1, paramComponent1, Spring.sum(paramSpring, getConstraint(paramString2, paramComponent2)));
/*      */   }
/*      */ 
/*      */   private void putConstraint(String paramString, Component paramComponent, Spring paramSpring) {
/* 1079 */     if (paramSpring != null)
/* 1080 */       getConstraints(paramComponent).setConstraint(paramString, paramSpring);
/*      */   }
/*      */ 
/*      */   private Constraints applyDefaults(Component paramComponent, Constraints paramConstraints)
/*      */   {
/* 1085 */     if (paramConstraints == null) {
/* 1086 */       paramConstraints = new Constraints();
/*      */     }
/* 1088 */     if (paramConstraints.c == null) {
/* 1089 */       paramConstraints.c = paramComponent;
/*      */     }
/* 1091 */     if (paramConstraints.horizontalHistory.size() < 2) {
/* 1092 */       applyDefaults(paramConstraints, "West", Spring.constant(0), "Width", Spring.width(paramComponent), paramConstraints.horizontalHistory);
/*      */     }
/*      */ 
/* 1095 */     if (paramConstraints.verticalHistory.size() < 2) {
/* 1096 */       applyDefaults(paramConstraints, "North", Spring.constant(0), "Height", Spring.height(paramComponent), paramConstraints.verticalHistory);
/*      */     }
/*      */ 
/* 1099 */     return paramConstraints;
/*      */   }
/*      */ 
/*      */   private void applyDefaults(Constraints paramConstraints, String paramString1, Spring paramSpring1, String paramString2, Spring paramSpring2, List<String> paramList)
/*      */   {
/* 1105 */     if (paramList.size() == 0) {
/* 1106 */       paramConstraints.setConstraint(paramString1, paramSpring1);
/* 1107 */       paramConstraints.setConstraint(paramString2, paramSpring2);
/*      */     }
/*      */     else
/*      */     {
/* 1111 */       if (paramConstraints.getConstraint(paramString2) == null) {
/* 1112 */         paramConstraints.setConstraint(paramString2, paramSpring2);
/*      */       }
/*      */       else {
/* 1115 */         paramConstraints.setConstraint(paramString1, paramSpring1);
/*      */       }
/*      */ 
/* 1118 */       Collections.rotate(paramList, 1);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void putConstraints(Component paramComponent, Constraints paramConstraints) {
/* 1123 */     this.componentConstraints.put(paramComponent, applyDefaults(paramComponent, paramConstraints));
/*      */   }
/*      */ 
/*      */   public Constraints getConstraints(Component paramComponent)
/*      */   {
/* 1149 */     Constraints localConstraints = (Constraints)this.componentConstraints.get(paramComponent);
/* 1150 */     if (localConstraints == null) {
/* 1151 */       if ((paramComponent instanceof JComponent)) {
/* 1152 */         Object localObject = ((JComponent)paramComponent).getClientProperty(SpringLayout.class);
/* 1153 */         if ((localObject instanceof Constraints)) {
/* 1154 */           return applyDefaults(paramComponent, (Constraints)localObject);
/*      */         }
/*      */       }
/* 1157 */       localConstraints = new Constraints();
/* 1158 */       putConstraints(paramComponent, localConstraints);
/*      */     }
/* 1160 */     return localConstraints;
/*      */   }
/*      */ 
/*      */   public Spring getConstraint(String paramString, Component paramComponent)
/*      */   {
/* 1201 */     paramString = paramString.intern();
/* 1202 */     return new SpringProxy(paramString, paramComponent, this);
/*      */   }
/*      */ 
/*      */   public void layoutContainer(Container paramContainer) {
/* 1206 */     setParent(paramContainer);
/*      */ 
/* 1208 */     int i = paramContainer.getComponentCount();
/* 1209 */     getConstraints(paramContainer).reset();
/* 1210 */     for (int j = 0; j < i; j++) {
/* 1211 */       getConstraints(paramContainer.getComponent(j)).reset();
/*      */     }
/*      */ 
/* 1214 */     Insets localInsets = paramContainer.getInsets();
/* 1215 */     Constraints localConstraints1 = getConstraints(paramContainer);
/* 1216 */     abandonCycles(localConstraints1.getX()).setValue(0);
/* 1217 */     abandonCycles(localConstraints1.getY()).setValue(0);
/* 1218 */     abandonCycles(localConstraints1.getWidth()).setValue(paramContainer.getWidth() - localInsets.left - localInsets.right);
/*      */ 
/* 1220 */     abandonCycles(localConstraints1.getHeight()).setValue(paramContainer.getHeight() - localInsets.top - localInsets.bottom);
/*      */ 
/* 1223 */     for (int k = 0; k < i; k++) {
/* 1224 */       Component localComponent = paramContainer.getComponent(k);
/* 1225 */       Constraints localConstraints2 = getConstraints(localComponent);
/* 1226 */       int m = abandonCycles(localConstraints2.getX()).getValue();
/* 1227 */       int n = abandonCycles(localConstraints2.getY()).getValue();
/* 1228 */       int i1 = abandonCycles(localConstraints2.getWidth()).getValue();
/* 1229 */       int i2 = abandonCycles(localConstraints2.getHeight()).getValue();
/* 1230 */       localComponent.setBounds(localInsets.left + m, localInsets.top + n, i1, i2);
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class Constraints
/*      */   {
/*      */     private Spring x;
/*      */     private Spring y;
/*      */     private Spring width;
/*      */     private Spring height;
/*      */     private Spring east;
/*      */     private Spring south;
/*      */     private Spring horizontalCenter;
/*      */     private Spring verticalCenter;
/*      */     private Spring baseline;
/*  329 */     private List<String> horizontalHistory = new ArrayList(2);
/*  330 */     private List<String> verticalHistory = new ArrayList(2);
/*      */     private Component c;
/*      */ 
/*      */     public Constraints()
/*      */     {
/*      */     }
/*      */ 
/*      */     public Constraints(Spring paramSpring1, Spring paramSpring2)
/*      */     {
/*  352 */       setX(paramSpring1);
/*  353 */       setY(paramSpring2);
/*      */     }
/*      */ 
/*      */     public Constraints(Spring paramSpring1, Spring paramSpring2, Spring paramSpring3, Spring paramSpring4)
/*      */     {
/*  372 */       setX(paramSpring1);
/*  373 */       setY(paramSpring2);
/*  374 */       setWidth(paramSpring3);
/*  375 */       setHeight(paramSpring4);
/*      */     }
/*      */ 
/*      */     public Constraints(Component paramComponent)
/*      */     {
/*  395 */       this.c = paramComponent;
/*  396 */       setX(Spring.constant(paramComponent.getX()));
/*  397 */       setY(Spring.constant(paramComponent.getY()));
/*  398 */       setWidth(Spring.width(paramComponent));
/*  399 */       setHeight(Spring.height(paramComponent));
/*      */     }
/*      */ 
/*      */     private void pushConstraint(String paramString, Spring paramSpring, boolean paramBoolean) {
/*  403 */       int i = 1;
/*  404 */       List localList = paramBoolean ? this.horizontalHistory : this.verticalHistory;
/*      */ 
/*  406 */       if (localList.contains(paramString)) {
/*  407 */         localList.remove(paramString);
/*  408 */         i = 0;
/*  409 */       } else if ((localList.size() == 2) && (paramSpring != null)) {
/*  410 */         localList.remove(0);
/*  411 */         i = 0;
/*      */       }
/*  413 */       if (paramSpring != null) {
/*  414 */         localList.add(paramString);
/*      */       }
/*  416 */       if (i == 0) {
/*  417 */         String[] arrayOfString1 = paramBoolean ? SpringLayout.ALL_HORIZONTAL : SpringLayout.ALL_VERTICAL;
/*  418 */         for (String str : arrayOfString1)
/*  419 */           if (!localList.contains(str))
/*  420 */             setConstraint(str, null);
/*      */       }
/*      */     }
/*      */ 
/*      */     private Spring sum(Spring paramSpring1, Spring paramSpring2)
/*      */     {
/*  427 */       return (paramSpring1 == null) || (paramSpring2 == null) ? null : Spring.sum(paramSpring1, paramSpring2);
/*      */     }
/*      */ 
/*      */     private Spring difference(Spring paramSpring1, Spring paramSpring2) {
/*  431 */       return (paramSpring1 == null) || (paramSpring2 == null) ? null : Spring.difference(paramSpring1, paramSpring2);
/*      */     }
/*      */ 
/*      */     private Spring scale(Spring paramSpring, float paramFloat) {
/*  435 */       return paramSpring == null ? null : Spring.scale(paramSpring, paramFloat);
/*      */     }
/*      */ 
/*      */     private int getBaselineFromHeight(int paramInt) {
/*  439 */       if (paramInt < 0)
/*      */       {
/*  441 */         return -this.c.getBaseline(this.c.getPreferredSize().width, -paramInt);
/*      */       }
/*      */ 
/*  444 */       return this.c.getBaseline(this.c.getPreferredSize().width, paramInt);
/*      */     }
/*      */ 
/*      */     private int getHeightFromBaseLine(int paramInt) {
/*  448 */       Dimension localDimension = this.c.getPreferredSize();
/*  449 */       int i = localDimension.height;
/*  450 */       int j = this.c.getBaseline(localDimension.width, i);
/*  451 */       if (j == paramInt)
/*      */       {
/*  456 */         return i;
/*      */       }
/*      */ 
/*  459 */       switch (SpringLayout.1.$SwitchMap$java$awt$Component$BaselineResizeBehavior[this.c.getBaselineResizeBehavior().ordinal()]) {
/*      */       case 1:
/*  461 */         return i + (paramInt - j);
/*      */       case 2:
/*  463 */         return i + 2 * (paramInt - j);
/*      */       case 3:
/*      */       }
/*      */ 
/*  470 */       return -2147483648;
/*      */     }
/*      */ 
/*      */     private Spring heightToRelativeBaseline(Spring paramSpring) {
/*  474 */       return new Spring.SpringMap(paramSpring) {
/*      */         protected int map(int paramAnonymousInt) {
/*  476 */           return SpringLayout.Constraints.this.getBaselineFromHeight(paramAnonymousInt);
/*      */         }
/*      */ 
/*      */         protected int inv(int paramAnonymousInt) {
/*  480 */           return SpringLayout.Constraints.this.getHeightFromBaseLine(paramAnonymousInt);
/*      */         }
/*      */       };
/*      */     }
/*      */ 
/*      */     private Spring relativeBaselineToHeight(Spring paramSpring) {
/*  486 */       return new Spring.SpringMap(paramSpring) {
/*      */         protected int map(int paramAnonymousInt) {
/*  488 */           return SpringLayout.Constraints.this.getHeightFromBaseLine(paramAnonymousInt);
/*      */         }
/*      */ 
/*      */         protected int inv(int paramAnonymousInt) {
/*  492 */           return SpringLayout.Constraints.this.getBaselineFromHeight(paramAnonymousInt);
/*      */         }
/*      */       };
/*      */     }
/*      */ 
/*      */     private boolean defined(List paramList, String paramString1, String paramString2) {
/*  498 */       return (paramList.contains(paramString1)) && (paramList.contains(paramString2));
/*      */     }
/*      */ 
/*      */     public void setX(Spring paramSpring)
/*      */     {
/*  513 */       this.x = paramSpring;
/*  514 */       pushConstraint("West", paramSpring, true);
/*      */     }
/*      */ 
/*      */     public Spring getX()
/*      */     {
/*  527 */       if (this.x == null) {
/*  528 */         if (defined(this.horizontalHistory, "East", "Width"))
/*  529 */           this.x = difference(this.east, this.width);
/*  530 */         else if (defined(this.horizontalHistory, "HorizontalCenter", "Width"))
/*  531 */           this.x = difference(this.horizontalCenter, scale(this.width, 0.5F));
/*  532 */         else if (defined(this.horizontalHistory, "HorizontalCenter", "East")) {
/*  533 */           this.x = difference(scale(this.horizontalCenter, 2.0F), this.east);
/*      */         }
/*      */       }
/*  536 */       return this.x;
/*      */     }
/*      */ 
/*      */     public void setY(Spring paramSpring)
/*      */     {
/*  551 */       this.y = paramSpring;
/*  552 */       pushConstraint("North", paramSpring, false);
/*      */     }
/*      */ 
/*      */     public Spring getY()
/*      */     {
/*  565 */       if (this.y == null) {
/*  566 */         if (defined(this.verticalHistory, "South", "Height"))
/*  567 */           this.y = difference(this.south, this.height);
/*  568 */         else if (defined(this.verticalHistory, "VerticalCenter", "Height"))
/*  569 */           this.y = difference(this.verticalCenter, scale(this.height, 0.5F));
/*  570 */         else if (defined(this.verticalHistory, "VerticalCenter", "South"))
/*  571 */           this.y = difference(scale(this.verticalCenter, 2.0F), this.south);
/*  572 */         else if (defined(this.verticalHistory, "Baseline", "Height"))
/*  573 */           this.y = difference(this.baseline, heightToRelativeBaseline(this.height));
/*  574 */         else if (defined(this.verticalHistory, "Baseline", "South")) {
/*  575 */           this.y = scale(difference(this.baseline, heightToRelativeBaseline(this.south)), 2.0F);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  582 */       return this.y;
/*      */     }
/*      */ 
/*      */     public void setWidth(Spring paramSpring)
/*      */     {
/*  596 */       this.width = paramSpring;
/*  597 */       pushConstraint("Width", paramSpring, true);
/*      */     }
/*      */ 
/*      */     public Spring getWidth()
/*      */     {
/*  609 */       if (this.width == null) {
/*  610 */         if (this.horizontalHistory.contains("East"))
/*  611 */           this.width = difference(this.east, getX());
/*  612 */         else if (this.horizontalHistory.contains("HorizontalCenter")) {
/*  613 */           this.width = scale(difference(this.horizontalCenter, getX()), 2.0F);
/*      */         }
/*      */       }
/*  616 */       return this.width;
/*      */     }
/*      */ 
/*      */     public void setHeight(Spring paramSpring)
/*      */     {
/*  630 */       this.height = paramSpring;
/*  631 */       pushConstraint("Height", paramSpring, false);
/*      */     }
/*      */ 
/*      */     public Spring getHeight()
/*      */     {
/*  643 */       if (this.height == null) {
/*  644 */         if (this.verticalHistory.contains("South"))
/*  645 */           this.height = difference(this.south, getY());
/*  646 */         else if (this.verticalHistory.contains("VerticalCenter"))
/*  647 */           this.height = scale(difference(this.verticalCenter, getY()), 2.0F);
/*  648 */         else if (this.verticalHistory.contains("Baseline")) {
/*  649 */           this.height = relativeBaselineToHeight(difference(this.baseline, getY()));
/*      */         }
/*      */       }
/*  652 */       return this.height;
/*      */     }
/*      */ 
/*      */     private void setEast(Spring paramSpring) {
/*  656 */       this.east = paramSpring;
/*  657 */       pushConstraint("East", paramSpring, true);
/*      */     }
/*      */ 
/*      */     private Spring getEast() {
/*  661 */       if (this.east == null) {
/*  662 */         this.east = sum(getX(), getWidth());
/*      */       }
/*  664 */       return this.east;
/*      */     }
/*      */ 
/*      */     private void setSouth(Spring paramSpring) {
/*  668 */       this.south = paramSpring;
/*  669 */       pushConstraint("South", paramSpring, false);
/*      */     }
/*      */ 
/*      */     private Spring getSouth() {
/*  673 */       if (this.south == null) {
/*  674 */         this.south = sum(getY(), getHeight());
/*      */       }
/*  676 */       return this.south;
/*      */     }
/*      */ 
/*      */     private Spring getHorizontalCenter() {
/*  680 */       if (this.horizontalCenter == null) {
/*  681 */         this.horizontalCenter = sum(getX(), scale(getWidth(), 0.5F));
/*      */       }
/*  683 */       return this.horizontalCenter;
/*      */     }
/*      */ 
/*      */     private void setHorizontalCenter(Spring paramSpring) {
/*  687 */       this.horizontalCenter = paramSpring;
/*  688 */       pushConstraint("HorizontalCenter", paramSpring, true);
/*      */     }
/*      */ 
/*      */     private Spring getVerticalCenter() {
/*  692 */       if (this.verticalCenter == null) {
/*  693 */         this.verticalCenter = sum(getY(), scale(getHeight(), 0.5F));
/*      */       }
/*  695 */       return this.verticalCenter;
/*      */     }
/*      */ 
/*      */     private void setVerticalCenter(Spring paramSpring) {
/*  699 */       this.verticalCenter = paramSpring;
/*  700 */       pushConstraint("VerticalCenter", paramSpring, false);
/*      */     }
/*      */ 
/*      */     private Spring getBaseline() {
/*  704 */       if (this.baseline == null) {
/*  705 */         this.baseline = sum(getY(), heightToRelativeBaseline(getHeight()));
/*      */       }
/*  707 */       return this.baseline;
/*      */     }
/*      */ 
/*      */     private void setBaseline(Spring paramSpring) {
/*  711 */       this.baseline = paramSpring;
/*  712 */       pushConstraint("Baseline", paramSpring, false);
/*      */     }
/*      */ 
/*      */     public void setConstraint(String paramString, Spring paramSpring)
/*      */     {
/*  752 */       paramString = paramString.intern();
/*  753 */       if (paramString == "West")
/*  754 */         setX(paramSpring);
/*  755 */       else if (paramString == "North")
/*  756 */         setY(paramSpring);
/*  757 */       else if (paramString == "East")
/*  758 */         setEast(paramSpring);
/*  759 */       else if (paramString == "South")
/*  760 */         setSouth(paramSpring);
/*  761 */       else if (paramString == "HorizontalCenter")
/*  762 */         setHorizontalCenter(paramSpring);
/*  763 */       else if (paramString == "Width")
/*  764 */         setWidth(paramSpring);
/*  765 */       else if (paramString == "Height")
/*  766 */         setHeight(paramSpring);
/*  767 */       else if (paramString == "VerticalCenter")
/*  768 */         setVerticalCenter(paramSpring);
/*  769 */       else if (paramString == "Baseline")
/*  770 */         setBaseline(paramSpring);
/*      */     }
/*      */ 
/*      */     public Spring getConstraint(String paramString)
/*      */     {
/*  811 */       paramString = paramString.intern();
/*  812 */       return paramString == "Baseline" ? getBaseline() : paramString == "VerticalCenter" ? getVerticalCenter() : paramString == "HorizontalCenter" ? getHorizontalCenter() : paramString == "Height" ? getHeight() : paramString == "Width" ? getWidth() : paramString == "South" ? getSouth() : paramString == "East" ? getEast() : paramString == "North" ? getY() : paramString == "West" ? getX() : null;
/*      */     }
/*      */ 
/*      */     void reset()
/*      */     {
/*  825 */       Spring[] arrayOfSpring1 = { this.x, this.y, this.width, this.height, this.east, this.south, this.horizontalCenter, this.verticalCenter, this.baseline };
/*      */ 
/*  827 */       for (Spring localSpring : arrayOfSpring1)
/*  828 */         if (localSpring != null)
/*  829 */           localSpring.setValue(-2147483648);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class SpringProxy extends Spring {
/*      */     private String edgeName;
/*      */     private Component c;
/*      */     private SpringLayout l;
/*      */ 
/*      */     public SpringProxy(String paramString, Component paramComponent, SpringLayout paramSpringLayout) {
/*  841 */       this.edgeName = paramString;
/*  842 */       this.c = paramComponent;
/*  843 */       this.l = paramSpringLayout;
/*      */     }
/*      */ 
/*      */     private Spring getConstraint() {
/*  847 */       return this.l.getConstraints(this.c).getConstraint(this.edgeName);
/*      */     }
/*      */ 
/*      */     public int getMinimumValue() {
/*  851 */       return getConstraint().getMinimumValue();
/*      */     }
/*      */ 
/*      */     public int getPreferredValue() {
/*  855 */       return getConstraint().getPreferredValue();
/*      */     }
/*      */ 
/*      */     public int getMaximumValue() {
/*  859 */       return getConstraint().getMaximumValue();
/*      */     }
/*      */ 
/*      */     public int getValue() {
/*  863 */       return getConstraint().getValue();
/*      */     }
/*      */ 
/*      */     public void setValue(int paramInt) {
/*  867 */       getConstraint().setValue(paramInt);
/*      */     }
/*      */ 
/*      */     boolean isCyclic(SpringLayout paramSpringLayout) {
/*  871 */       return paramSpringLayout.isCyclic(getConstraint());
/*      */     }
/*      */ 
/*      */     public String toString() {
/*  875 */       return "SpringProxy for " + this.edgeName + " edge of " + this.c.getName() + ".";
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.SpringLayout
 * JD-Core Version:    0.6.2
 */