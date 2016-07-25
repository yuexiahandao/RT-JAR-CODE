package com.sun.beans.finder;

import com.sun.beans.WeakCache;
import com.sun.beans.editors.BooleanEditor;
import com.sun.beans.editors.ByteEditor;
import com.sun.beans.editors.DoubleEditor;
import com.sun.beans.editors.EnumEditor;
import com.sun.beans.editors.FloatEditor;
import com.sun.beans.editors.IntegerEditor;
import com.sun.beans.editors.LongEditor;
import com.sun.beans.editors.ShortEditor;

import java.beans.PropertyEditor;

public final class PropertyEditorFinder extends InstanceFinder<PropertyEditor> {
    private static final String DEFAULT = "sun.beans.editors";
    private static final String DEFAULT_NEW = "com.sun.beans.editors";
    private final WeakCache<Class<?>, Class<?>> registry;

    public PropertyEditorFinder() {
        super(PropertyEditor.class, false, "Editor", new String[]{"sun.beans.editors"});

        this.registry = new WeakCache();
        this.registry.put(Byte.TYPE, ByteEditor.class);
        this.registry.put(Short.TYPE, ShortEditor.class);
        this.registry.put(Integer.TYPE, IntegerEditor.class);
        this.registry.put(Long.TYPE, LongEditor.class);
        this.registry.put(Boolean.TYPE, BooleanEditor.class);
        this.registry.put(Float.TYPE, FloatEditor.class);
        this.registry.put(Double.TYPE, DoubleEditor.class);
    }

    public void register(Class<?> paramClass1, Class<?> paramClass2) {
        synchronized (this.registry) {
            this.registry.put(paramClass1, paramClass2);
        }
    }

    public PropertyEditor find(Class<?> paramClass) {
        Class localClass;
        synchronized (this.registry) {
            localClass = (Class) this.registry.get(paramClass);
        }
        ???=(PropertyEditor) instantiate(localClass, null);
        if (???==null){
            ???=(PropertyEditor) super.find(paramClass);
            if (( ???==null)&&(null != paramClass.getEnumConstants())){
                ???=new EnumEditor(paramClass);
            }
        }
        return???;
    }

    protected PropertyEditor instantiate(Class<?> paramClass, String paramString1, String paramString2) {
        return (PropertyEditor) super.instantiate(paramClass, "sun.beans.editors".equals(paramString1) ? "com.sun.beans.editors" : paramString1, paramString2);
    }
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.beans.finder.PropertyEditorFinder
 * JD-Core Version:    0.6.2
 */