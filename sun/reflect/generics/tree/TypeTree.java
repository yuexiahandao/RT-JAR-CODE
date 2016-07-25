package sun.reflect.generics.tree;

import sun.reflect.generics.visitor.TypeTreeVisitor;

public abstract interface TypeTree extends Tree
{
  public abstract void accept(TypeTreeVisitor<?> paramTypeTreeVisitor);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.reflect.generics.tree.TypeTree
 * JD-Core Version:    0.6.2
 */