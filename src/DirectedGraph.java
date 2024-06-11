import java.util.*;

class DirectedGraph {
    private Map<Landmark,List<Edge>> graph;

    public DirectedGraph() {
        graph = new HashMap<>();
    }

    public void addNode(Landmark landmark) {
        graph.putIfAbsent(landmark, new LinkedList<Edge>());
    }
    public List<Landmark> getLandmarks() {
        return new ArrayList<>(graph.keySet());
    }


    public void addEdges(Landmark source, List<Edge> edges) {
        addNode(source);
        graph.put(source,edges);
    }

    public Map<Landmark, List<Edge>> getGraph() {
        return graph;
    }

    public void setGraph(Map<Landmark, List<Edge>> graph) {
        this.graph = graph;
    }
    /*void getRouteCalculator( int landmarkNumber){
        List<Edge> edges = getGraph().;


    }*/
}