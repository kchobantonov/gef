package org.eclipse.gef4.dot.internal;

import org.eclipse.gef4.dot.internal.DotAttributes;
import org.eclipse.gef4.graph.*;

public class DotTemplate
{
  protected static String nl;
  public static synchronized DotTemplate create(String lineSeparator)
  {
    nl = lineSeparator;
    DotTemplate result = new DotTemplate();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "";
  protected final String TEXT_2 = NL;
  protected final String TEXT_3 = " ";
  protected final String TEXT_4 = "{" + NL + "" + NL + "\t/* Global settings */" + NL + "\tgraph[layout=";
  protected final String TEXT_5 = "]" + NL + "\tnode[shape=box]" + NL + "\trankdir=";
  protected final String TEXT_6 = NL + "\t" + NL + "\t/* Nodes */" + NL + "\t";
  protected final String TEXT_7 = " " + NL + "\t";
  protected final String TEXT_8 = "[label=\"";
  protected final String TEXT_9 = "\"];" + NL + "\t";
  protected final String TEXT_10 = NL + "\t" + NL + "\t/* Edges */" + NL + "\t";
  protected final String TEXT_11 = " " + NL + "\t";
  protected final String TEXT_12 = " " + NL + "\t";
  protected final String TEXT_13 = " ";
  protected final String TEXT_14 = " ";
  protected final String TEXT_15 = "[style=";
  protected final String TEXT_16 = " label=\"";
  protected final String TEXT_17 = "\"];" + NL + "\t";
  protected final String TEXT_18 = NL + "}";

  public String generate(Object argument)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    /*******************************************************************************
 * Copyright (c) 2009, 2010 Fabian Steeg. All rights reserved. This program and 
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * <p/>
 * Contributors: Fabian Steeg - initial API and implementation; see bug 277380
 *******************************************************************************/
     Graph graph = (Graph) argument; 
     String algo = DotAttributes.getLayout(graph); if (algo == null) algo = DotAttributes.LAYOUT__G__DEFAULT;
     boolean digraph = DotAttributes._TYPE__G__DIGRAPH.equals(DotAttributes.getType(graph)); 
     String simpleClassName = graph.getClass().getSimpleName(); 
     /* The exact name 'Graph' is not valid for rendering with Graphviz: */ 
     simpleClassName = simpleClassName.equals("Graph") ? "Dot" + simpleClassName : simpleClassName; 
    stringBuffer.append(TEXT_1);
    stringBuffer.append(TEXT_2);
    stringBuffer.append( digraph ? DotAttributes._TYPE__G__DIGRAPH : DotAttributes._TYPE__G__GRAPH );
    stringBuffer.append(TEXT_3);
    stringBuffer.append(simpleClassName);
    stringBuffer.append(TEXT_4);
    stringBuffer.append(algo);
    stringBuffer.append(TEXT_5);
    stringBuffer.append(DotAttributes.getRankdir(graph) != null ? DotAttributes.getRankdir(graph).toUpperCase() : DotAttributes.RANKDIR__G__DEFAULT.toUpperCase());
    stringBuffer.append(TEXT_6);
     for(Object nodeObject : graph.getNodes()){ Node node = (Node) nodeObject; 
    stringBuffer.append(TEXT_7);
    stringBuffer.append(node.hashCode());
    stringBuffer.append(TEXT_8);
    stringBuffer.append(DotAttributes.getLabel(node));
    stringBuffer.append(TEXT_9);
     }
    stringBuffer.append(TEXT_10);
     for(Object edgeObject : graph.getEdges()){ Edge edge = (Edge) edgeObject; 
    stringBuffer.append(TEXT_11);
    boolean dashed = DotAttributes.STYLE__E__DASHED.equals(DotAttributes.getStyle(edge)); boolean dotted = DotAttributes.STYLE__E__DOTTED.equals(DotAttributes.getStyle(edge));
    stringBuffer.append(TEXT_12);
    stringBuffer.append(edge.getSource().hashCode());
    stringBuffer.append(TEXT_13);
    stringBuffer.append( digraph ? "->" : "--" );
    stringBuffer.append(TEXT_14);
    stringBuffer.append(edge.getTarget().hashCode());
    stringBuffer.append(TEXT_15);
    stringBuffer.append(dashed?"dashed":dotted?"dotted":"solid");
    stringBuffer.append(TEXT_16);
    stringBuffer.append(DotAttributes.getLabel(edge));
    stringBuffer.append(TEXT_17);
     }
    stringBuffer.append(TEXT_18);
    return stringBuffer.toString();
  }
}
