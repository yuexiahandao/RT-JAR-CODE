/*     */ package javax.swing;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.Comparator;
/*     */ import javax.swing.plaf.ComboBoxUI;
/*     */ import sun.awt.SunToolkit;
/*     */ 
/*     */ public class LayoutFocusTraversalPolicy extends SortingFocusTraversalPolicy
/*     */   implements Serializable
/*     */ {
/*  55 */   private static final SwingDefaultFocusTraversalPolicy fitnessTestPolicy = new SwingDefaultFocusTraversalPolicy();
/*     */ 
/*     */   public LayoutFocusTraversalPolicy()
/*     */   {
/*  62 */     super(new LayoutComparator());
/*     */   }
/*     */ 
/*     */   LayoutFocusTraversalPolicy(Comparator<? super Component> paramComparator)
/*     */   {
/*  70 */     super(paramComparator);
/*     */   }
/*     */ 
/*     */   public Component getComponentAfter(Container paramContainer, Component paramComponent)
/*     */   {
/*  97 */     if ((paramContainer == null) || (paramComponent == null)) {
/*  98 */       throw new IllegalArgumentException("aContainer and aComponent cannot be null");
/*     */     }
/* 100 */     Comparator localComparator = getComparator();
/* 101 */     if ((localComparator instanceof LayoutComparator)) {
/* 102 */       ((LayoutComparator)localComparator).setComponentOrientation(paramContainer.getComponentOrientation());
/*     */     }
/*     */ 
/* 106 */     return super.getComponentAfter(paramContainer, paramComponent);
/*     */   }
/*     */ 
/*     */   public Component getComponentBefore(Container paramContainer, Component paramComponent)
/*     */   {
/* 133 */     if ((paramContainer == null) || (paramComponent == null)) {
/* 134 */       throw new IllegalArgumentException("aContainer and aComponent cannot be null");
/*     */     }
/* 136 */     Comparator localComparator = getComparator();
/* 137 */     if ((localComparator instanceof LayoutComparator)) {
/* 138 */       ((LayoutComparator)localComparator).setComponentOrientation(paramContainer.getComponentOrientation());
/*     */     }
/*     */ 
/* 142 */     return super.getComponentBefore(paramContainer, paramComponent);
/*     */   }
/*     */ 
/*     */   public Component getFirstComponent(Container paramContainer)
/*     */   {
/* 157 */     if (paramContainer == null) {
/* 158 */       throw new IllegalArgumentException("aContainer cannot be null");
/*     */     }
/* 160 */     Comparator localComparator = getComparator();
/* 161 */     if ((localComparator instanceof LayoutComparator)) {
/* 162 */       ((LayoutComparator)localComparator).setComponentOrientation(paramContainer.getComponentOrientation());
/*     */     }
/*     */ 
/* 166 */     return super.getFirstComponent(paramContainer);
/*     */   }
/*     */ 
/*     */   public Component getLastComponent(Container paramContainer)
/*     */   {
/* 181 */     if (paramContainer == null) {
/* 182 */       throw new IllegalArgumentException("aContainer cannot be null");
/*     */     }
/* 184 */     Comparator localComparator = getComparator();
/* 185 */     if ((localComparator instanceof LayoutComparator)) {
/* 186 */       ((LayoutComparator)localComparator).setComponentOrientation(paramContainer.getComponentOrientation());
/*     */     }
/*     */ 
/* 190 */     return super.getLastComponent(paramContainer);
/*     */   }
/*     */ 
/*     */   protected boolean accept(Component paramComponent)
/*     */   {
/* 228 */     if (!super.accept(paramComponent))
/* 229 */       return false;
/* 230 */     if (SunToolkit.isInstanceOf(paramComponent, "javax.swing.JTable"))
/*     */     {
/* 233 */       return true;
/*     */     }
/*     */     Object localObject;
/* 234 */     if (SunToolkit.isInstanceOf(paramComponent, "javax.swing.JComboBox")) {
/* 235 */       localObject = (JComboBox)paramComponent;
/* 236 */       return ((JComboBox)localObject).getUI().isFocusTraversable((JComboBox)localObject);
/* 237 */     }if ((paramComponent instanceof JComponent)) {
/* 238 */       localObject = (JComponent)paramComponent;
/* 239 */       InputMap localInputMap = ((JComponent)localObject).getInputMap(0, false);
/*     */ 
/* 241 */       while ((localInputMap != null) && (localInputMap.size() == 0)) {
/* 242 */         localInputMap = localInputMap.getParent();
/*     */       }
/* 244 */       if (localInputMap != null) {
/* 245 */         return true;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 251 */     return fitnessTestPolicy.accept(paramComponent);
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
/* 255 */     paramObjectOutputStream.writeObject(getComparator());
/* 256 */     paramObjectOutputStream.writeBoolean(getImplicitDownCycleTraversal());
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream) throws IOException, ClassNotFoundException
/*     */   {
/* 261 */     setComparator((Comparator)paramObjectInputStream.readObject());
/* 262 */     setImplicitDownCycleTraversal(paramObjectInputStream.readBoolean());
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.LayoutFocusTraversalPolicy
 * JD-Core Version:    0.6.2
 */