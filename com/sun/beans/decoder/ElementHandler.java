package com.sun.beans.decoder;

public abstract class ElementHandler {
    private DocumentHandler owner;
    private ElementHandler parent;
    private String id;

    public final DocumentHandler getOwner() {
        return this.owner;
    }

    final void setOwner(DocumentHandler paramDocumentHandler) {
        if (paramDocumentHandler == null) {
            throw new IllegalArgumentException("Every element should have owner");
        }
        this.owner = paramDocumentHandler;
    }

    public final ElementHandler getParent() {
        return this.parent;
    }

    final void setParent(ElementHandler paramElementHandler) {
        this.parent = paramElementHandler;
    }

    protected final Object getVariable(String paramString) {
        if (paramString.equals(this.id)) {
            ValueObject localValueObject = getValueObject();
            if (localValueObject.isVoid()) {
                throw new IllegalStateException("The element does not return value");
            }
            return localValueObject.getValue();
        }
        return this.parent != null ? this.parent.getVariable(paramString) : this.owner.getVariable(paramString);
    }

    protected Object getContextBean() {
        if (this.parent != null) {
            localObject = this.parent.getValueObject();
            if (!((ValueObject) localObject).isVoid()) {
                return ((ValueObject) localObject).getValue();
            }
            throw new IllegalStateException("The outer element does not return value");
        }
        Object localObject = this.owner.getOwner();
        if (localObject != null) {
            return localObject;
        }
        throw new IllegalStateException("The topmost element does not have context");
    }

    public void addAttribute(String paramString1, String paramString2) {
        if (paramString1.equals("id"))
            this.id = paramString2;
        else
            throw new IllegalArgumentException("Unsupported attribute: " + paramString1);
    }

    public void startElement() {
    }

    public void endElement() {
        ValueObject localValueObject = getValueObject();
        if (!localValueObject.isVoid()) {
            if (this.id != null) {
                this.owner.setVariable(this.id, localValueObject.getValue());
            }
            if (isArgument())
                if (this.parent != null)
                    this.parent.addArgument(localValueObject.getValue());
                else
                    this.owner.addObject(localValueObject.getValue());
        }
    }

    public void addCharacter(char paramChar) {
        if ((paramChar != ' ') && (paramChar != '\n') && (paramChar != '\t') && (paramChar != '\r'))
            throw new IllegalStateException("Illegal character with code " + paramChar);
    }

    protected void addArgument(Object paramObject) {
        throw new IllegalStateException("Could not add argument to simple element");
    }

    protected boolean isArgument() {
        return this.id == null;
    }

    protected abstract ValueObject getValueObject();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.beans.decoder.ElementHandler
 * JD-Core Version:    0.6.2
 */