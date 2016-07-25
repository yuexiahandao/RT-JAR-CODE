package com.sun.beans.editors;

import java.awt.Choice;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Label;
import java.awt.Panel;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyEditor;
import java.io.PrintStream;

public class FontEditor extends Panel
        implements PropertyEditor {
    private static final long serialVersionUID = 6732704486002715933L;
    private Font font;
    private Toolkit toolkit;
    private String sampleText = "Abcde...";
    private Label sample;
    private Choice familyChoser;
    private Choice styleChoser;
    private Choice sizeChoser;
    private String[] fonts;
    private String[] styleNames = {"plain", "bold", "italic"};
    private int[] styles = {0, 1, 2};
    private int[] pointSizes = {3, 5, 8, 10, 12, 14, 18, 24, 36, 48};

    private PropertyChangeSupport support = new PropertyChangeSupport(this);

    public FontEditor() {
        setLayout(null);

        this.toolkit = Toolkit.getDefaultToolkit();
        this.fonts = this.toolkit.getFontList();

        this.familyChoser = new Choice();
        for (int i = 0; i < this.fonts.length; i++) {
            this.familyChoser.addItem(this.fonts[i]);
        }
        add(this.familyChoser);
        this.familyChoser.reshape(20, 5, 100, 30);

        this.styleChoser = new Choice();
        for (i = 0; i < this.styleNames.length; i++) {
            this.styleChoser.addItem(this.styleNames[i]);
        }
        add(this.styleChoser);
        this.styleChoser.reshape(145, 5, 70, 30);

        this.sizeChoser = new Choice();
        for (i = 0; i < this.pointSizes.length; i++) {
            this.sizeChoser.addItem("" + this.pointSizes[i]);
        }
        add(this.sizeChoser);
        this.sizeChoser.reshape(220, 5, 70, 30);

        resize(300, 40);
    }

    public Dimension preferredSize() {
        return new Dimension(300, 40);
    }

    public void setValue(Object paramObject) {
        this.font = ((Font) paramObject);
        if (this.font == null) {
            return;
        }
        changeFont(this.font);

        for (int i = 0; i < this.fonts.length; i++) {
            if (this.fonts[i].equals(this.font.getFamily())) {
                this.familyChoser.select(i);
                break;
            }
        }
        for (i = 0; i < this.styleNames.length; i++) {
            if (this.font.getStyle() == this.styles[i]) {
                this.styleChoser.select(i);
                break;
            }
        }
        for (i = 0; i < this.pointSizes.length; i++)
            if (this.font.getSize() <= this.pointSizes[i]) {
                this.sizeChoser.select(i);
                break;
            }
    }

    private void changeFont(Font paramFont) {
        this.font = paramFont;
        if (this.sample != null) {
            remove(this.sample);
        }
        this.sample = new Label(this.sampleText);
        this.sample.setFont(this.font);
        add(this.sample);
        Container localContainer = getParent();
        if (localContainer != null) {
            localContainer.invalidate();
            localContainer.layout();
        }
        invalidate();
        layout();
        repaint();
        this.support.firePropertyChange("", null, null);
    }

    public Object getValue() {
        return this.font;
    }

    public String getJavaInitializationString() {
        if (this.font == null) {
            return "null";
        }
        return "new java.awt.Font(\"" + this.font.getName() + "\", " + this.font.getStyle() + ", " + this.font.getSize() + ")";
    }

    public boolean action(Event paramEvent, Object paramObject) {
        String str = this.familyChoser.getSelectedItem();
        int i = this.styles[this.styleChoser.getSelectedIndex()];
        int j = this.pointSizes[this.sizeChoser.getSelectedIndex()];
        try {
            Font localFont = new Font(str, i, j);
            changeFont(localFont);
        } catch (Exception localException) {
            System.err.println("Couldn't create font " + str + "-" + this.styleNames[i] + "-" + j);
        }

        return false;
    }

    public boolean isPaintable() {
        return true;
    }

    public void paintValue(Graphics paramGraphics, Rectangle paramRectangle) {
        Font localFont = paramGraphics.getFont();
        paramGraphics.setFont(this.font);
        FontMetrics localFontMetrics = paramGraphics.getFontMetrics();
        int i = (paramRectangle.height - localFontMetrics.getAscent()) / 2;
        paramGraphics.drawString(this.sampleText, 0, paramRectangle.height - i);
        paramGraphics.setFont(localFont);
    }

    public String getAsText() {
        if (this.font == null) {
            return null;
        }
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append(this.font.getName());
        localStringBuilder.append(' ');

        boolean bool1 = this.font.isBold();
        if (bool1) {
            localStringBuilder.append("BOLD");
        }
        boolean bool2 = this.font.isItalic();
        if (bool2) {
            localStringBuilder.append("ITALIC");
        }
        if ((bool1) || (bool2)) {
            localStringBuilder.append(' ');
        }
        localStringBuilder.append(this.font.getSize());
        return localStringBuilder.toString();
    }

    public void setAsText(String paramString) throws IllegalArgumentException {
        setValue(paramString == null ? null : Font.decode(paramString));
    }

    public String[] getTags() {
        return null;
    }

    public Component getCustomEditor() {
        return this;
    }

    public boolean supportsCustomEditor() {
        return true;
    }

    public void addPropertyChangeListener(PropertyChangeListener paramPropertyChangeListener) {
        this.support.addPropertyChangeListener(paramPropertyChangeListener);
    }

    public void removePropertyChangeListener(PropertyChangeListener paramPropertyChangeListener) {
        this.support.removePropertyChangeListener(paramPropertyChangeListener);
    }
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.beans.editors.FontEditor
 * JD-Core Version:    0.6.2
 */