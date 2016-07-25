package com.sun.corba.se.impl.orbutil.graph;

import java.util.Set;

public abstract interface Graph extends Set
{
  public abstract NodeData getNodeData(Node paramNode);

  public abstract Set getRoots();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.orbutil.graph.Graph
 * JD-Core Version:    0.6.2
 */