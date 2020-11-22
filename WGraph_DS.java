package ex1.src;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;

public class WGraph_DS implements weighted_graph , Serializable {

	private static final long serialVersionUID = 1L;
    private HashMap<Integer, node_info> graph;
    private HashMap<Integer, HashMap<node_info, Double>> EList;
    private int  E;
    private int T;

    public WGraph_DS() {
    	this.graph = new HashMap<>();
    	this.EList = new HashMap<>();
        this.E = 0;
        this.T = 0;
    }
    
    @Override
    public void addNode(int id) {
        if (!this.graph.containsKey(id)) {
            this.graph.put(id, new Node(id));
            this.EList.put(id,new HashMap<>());
            T++;
        }
    }

    @Override
    public node_info removeNode(int key) {
        if (this.graph.containsKey(key)==true) {
            for (node_info n : graph.values()) {
                    removeEdge(n.getKey(), key);
            }
            return graph.remove(key);
        }
        return null;
    }
    
    @Override
    public void connect(int node1, int node2, double w)
    {
        if (this.graph.containsKey(node1)==true) {
            if (node1 != node2) {
                if (hasEdge(node1, node2)==false) {
                    EList.get(node1).put(getNode(node2), w);
                    EList.get(node2).put(getNode(node1), w);
                    T++;
                    E++;
                }
                else
                {
                    EList.get(node1).replace(getNode(node2), w);
                    EList.get(node2).replace(getNode(node1), w);
                    T++;
                }
            }
        }
    }

    @Override
    public void removeEdge(int node1, int node2)
    {
        if (hasEdge(node1, node2)==true) {
        	EList.get(node1).remove(getNode(node2));
        	EList.get(node2).remove(getNode(node1));
            E--;
            T++;
        }
    }

    @Override
    public node_info getNode(int key) {
        return this.graph.get(key);
    }

    @Override
    public boolean hasEdge(int node1, int node2) {
    	if(EList.get(node1).containsKey(getNode(node2))==true)
    	{
    		return true;
    	}
    	else
    	{
    		return false;
    	}
    }

    @Override
    public double getEdge(int node1, int node2) {
        if (hasEdge(node1,node2))
        {
        	return EList.get(node1).get(getNode(node2));
        }
        else
        {
            return -1;
        }
    }
   
    @Override
    public Collection<node_info> getV() {
        return this.graph.values();
    }

    @Override
    public Collection<node_info> getV(int node_id) {
        return this.EList.get(node_id).keySet();
    }

    @Override
    public int nodeSize() {
        return graph.size();
    }

    @Override
    public int edgeSize() {
        return this.E;
    }

    @Override
    public int getMC() {
        return this.T;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
        {
        	return true;
        }
        if (o == null)
        {
        	return false;
        }
        if(getClass() != o.getClass())
        {
        	return false;
        }
        	
        WGraph_DS ngraph = (WGraph_DS) o;
        if(graph.size() == ngraph.graph.size() && E == ngraph.E)
        {
        	return true;
        }
        else
        {
        	return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(E, T, graph, EList);
    }

    private class Node implements node_info, Serializable {

		private static final long serialVersionUID = 1L;
        private String nod_inf;
        private int key; 
        private double tag;


        public Node(int k) {
        	this.nod_inf = "";
            this.key = k;
            this.tag = 0;
        }

        @Override
        public int getKey() {
            return this.key;
        }

        @Override
        public String getInfo() {
            return this.nod_inf;
        }

        @Override
        public void setInfo(String s) {
            this.nod_inf = s;
        }
        
        @Override
        public void setTag(double t) {
            this.tag = t;
        }
        
        @Override
        public double getTag() {
            return this.tag;
        }
    }
}
