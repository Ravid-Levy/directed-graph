package ex1.src;

import java.io.*;
import java.util.*;

public class WGraph_Algo implements weighted_graph_algorithms , Serializable {

	private static final long serialVersionUID = 1L;
	private weighted_graph walgo;

    @Override
    public void init(weighted_graph g) {
        this.walgo = g;
    }

    @Override
    public weighted_graph getGraph() {
        return this.walgo;
    }

    @Override
    public boolean save(String f) {
        ObjectOutputStream opt;
        try {
            FileOutputStream output = new FileOutputStream(f);
            if(output == null)
            	return false;
            opt = new ObjectOutputStream(output);
            if(opt == null)
            	return false;
            opt.writeObject(this.getGraph());
            return true;
        }
        catch (FileNotFoundException ex) {
            ex.printStackTrace(); 
        }
        catch (IOException e) {
        	e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean load(String f) {
        try {
            FileInputStream fput = new FileInputStream(f);
            if(fput == null)
            	return false;
            ObjectInputStream oput = new ObjectInputStream(fput);
            if(oput == null)
            	return false;
            weighted_graph graph = (WGraph_DS) oput.readObject();
            this.init(graph);
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
    
    @Override
    public weighted_graph copy() {
        if (this.walgo != null) {
            weighted_graph clone = new WGraph_DS();
            for (node_info node : this.walgo.getV()) {
            	clone.addNode(node.getKey());
            	node_info nod = clone.getNode(node.getKey());
            	nod.setInfo(node.getInfo());
            	nod.setTag(node.getTag());
            }
            for (node_info nod : this.walgo.getV()) {
                for (node_info nod2 : this.walgo.getV(nod.getKey())) {
                	clone.connect(nod2.getKey(), nod.getKey(), this.walgo.getEdge(nod2.getKey(), nod.getKey()));
                }
            }
            return clone;
        }
        return null;
    }

    @Override
    public boolean isConnected() {
    	for (node_info node : this.walgo.getV())
        	node.setTag(Double.MAX_EXPONENT);
        if (walgo.getV().size()<=0)
        {
        	return true;
        }
        if (walgo.getV().size() == 1)
        {
        	return true;
        }
        node_info tmp = (node_info)walgo.getV().toArray()[0];
        Queue<node_info> qb = new LinkedList<>();
        qb.add(tmp);
        tmp.setTag(1);
        if (walgo.getV(tmp.getKey()).size() == 0)
        {
        	return false;
        }
        while (!qb.isEmpty()) {
            node_info inf = qb.poll();
            for (node_info nod : walgo.getV(inf.getKey())) {
                if (1 != nod.getTag()) {
                    nod.setTag(1);
                    qb.add(nod);
                }
            }
        }
        for (node_info nod : walgo.getV()) {
            if (nod.getTag() != 1)
            {
            	return false;
            }
        }
        return true;
    }

    @Override
    public double shortestPathDist(int startp, int endp) {
        if (walgo.getNode(startp) == null || walgo.getNode(endp) == null)
        {
        	return -1;
        }
        if (startp == endp)
        {
        	return 0;
        }
        List<node_info> path = shortestPath(startp, endp);
        double ans = path.get(path.size() -1).getTag();
        return ans;
    }

    private List<node_info> RPath(HashMap<Integer, node_info> prev, int startp, int end) {
        List<node_info> path = new ArrayList<>();
        node_info node = walgo.getNode(end);
        path.add(node);
        while (!path.contains(prev.get(startp))) {
        	node = prev.get(node.getKey());
            path.add(node);
            if (node.getKey() == startp) 
            	break;
        }
        Collections.reverse(path);
        return path;
    }
    @Override
    public List<node_info> shortestPath(int startp, int endp) {
    	for (node_info node : this.walgo.getV())
        	node.setTag(Double.MAX_EXPONENT);
        PriorityQueue<node_info> Q = new PriorityQueue<>(new Comparator<node_info>() {
            @Override
            public int compare(node_info obj1, node_info obj2) {
                if(obj1.getTag() > obj2.getTag())
                {
                	return (int) (obj1.getTag() - obj2.getTag());
                }
                else {
                	 return (int) (obj2.getTag() - obj1.getTag());
                	 }
            }
        });
        List<node_info> nodesAL = new ArrayList<>();
        HashMap<Integer, node_info> prev = new HashMap<>();
        if (startp == endp)
        {
        	return nodesAL;
        }
        if (walgo.getNode(startp) == null || walgo.getNode(endp) == null)
        {
        	return null;
        }
        walgo.getNode(startp).setTag(0);
        Q.add(walgo.getNode(startp));
        while (!Q.isEmpty()) {
            node_info node = Q.poll();
            for (node_info near : walgo.getV(node.getKey())) {
                if (!nodesAL.contains(near)) {
                    double edge = walgo.getEdge(node.getKey(), near.getKey());
                    double sum  = edge + node.getTag();
                    if (near.getTag() - sum > 0) {
                    	near.setTag(sum);
                        prev.put(near.getKey(), node);
                        Q.add(near);
                    }
                }
            }
            nodesAL.add(node);
        }
        return RPath(prev, startp, endp);
    }

}