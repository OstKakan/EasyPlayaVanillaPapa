
import java.util.*;

public class DirectedGraph<E extends Edge> {
	private Vector<LinkedList<E>> nodeVector;
	private List<E>[] nodes;

	public DirectedGraph(int noOfNodes) {
		System.out.println("Number of nodes: " + noOfNodes);
		nodes = (List<E>[]) new List[noOfNodes];
		for(int i = 0 ; i< noOfNodes ; i++){ 					//O(n)
			nodes[i] = new LinkedList<E>();						//O(1)
		}
	}

	/**
	 * Adds an edge to the graph
	 * @param e The edge to add to the graph
     */
	public void addEdge(E e) {
		System.out.println("Source is: " + e.getSource());
		nodes[e.getSource()].add(e);							//O(1)
	}

	/**
	 * Finds the shortest path between two nodes
	 * @param from The source node
	 * @param to The destination node
     * @return Iterator for the shortest path between the two nodes (with all nodes on the path in order)
     */
	public Iterator<E> shortestPath(int from, int to) {
		// Create array for visited nodes (where all nodes hasn't been visited)
		boolean[] visitedNodes = new boolean[nodes.length];
		Arrays.fill(visitedNodes, false);

		// Create empty priority queue
		Queue<Path<E>> pq = new PriorityQueue<>(new CompDijkstraPath<>());

		pq.add(new Path<E>(from, new ArrayList<E>(), 0));

		while (!pq.isEmpty()) {
			Path<E> path = pq.poll();

			if (!visitedNodes[path.getStartNode()]) {
				if (path.getStartNode() == to) {
					return path.getPath().iterator();
				} else {
					visitedNodes[path.getStartNode()] = true;
					for (E edge : nodes[path.getStartNode()]) {
						List<E> listToAdd = new ArrayList<E>(path.getPath());
						listToAdd.add(edge);
						pq.add(new Path<E>(edge.getDest(), listToAdd, path.getDistance() + edge.getWeight()));
					}
				}
			}
		}

		return null;
	}
		
	public Iterator<E> minimumSpanningTree() {
		LinkedList<E> shorterList, longerList;
		Queue<E> pq = new PriorityQueue<E>(new CompKruskalEdge());
		nodeVector = new Vector<>(); //Vector with list of edges, this will be used for a return;

		for(int i = 0 ; i < nodes.length ; i++){ //Off by one?
			nodeVector.add(new LinkedList<E>());
		}

		for(List<E> list : nodes){
			pq.addAll(list);
		}

		mainloop:
		while(!pq.isEmpty()){
			E e;
			do{
				e = pq.poll();
				if(e == null){
					break mainloop;
				}

			}while(nodeVector.get(e.getSource()) == nodeVector.get(e.getDest()));

			longerList = nodeVector.get(e.getDest());
			shorterList = nodeVector.get(e.getSource());
			if(nodeVector.get(e.getSource()).size() > nodeVector.get(e.getDest()).size()){
				longerList = nodeVector.get(e.getSource());
				shorterList = nodeVector.get(e.getDest());
			}

			longerList.add(e);
			nodeVector.set(e.getDest(), longerList);
			nodeVector.set(e.getSource(), longerList);
			longerList.addAll(shorterList);
			for(E edges : shorterList){
				nodeVector.set(edges.getSource(), longerList);
				nodeVector.set(edges.getDest(), longerList);
			}

		}

		return nodeVector.get(1).iterator();
	}


}

/**
 * A representation of a path between two nodes, holding the number of the start node,
 * a list of edges for the path and the distance (or weight) for the full path
 * @param <E> A subclass of Edge
 */
class Path<E extends Edge> {
	private int startNode;
	private List<E> path;
	private double distance;

	public Path(int startNode, List<E> path, double distance) {
		this.startNode = startNode;
		this.path = path;
		this.distance = distance;
	}

	public int getStartNode() {
		return this.startNode;
	}

	public List<E> getPath() {
		return this.path;
	}

	public double getDistance() {
		return this.distance;
	}
}
