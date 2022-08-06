package unsw.graph;

import java.util.*;

public class BreadthFirstGraphIterator<N extends Comparable<N>> implements Iterator<N> {

    private Graph<N> graph;
    Set<N> visited;
    Deque<N> queue;

    public BreadthFirstGraphIterator(Graph<N> graph, N startingNode) {
        this.graph = graph;
        visited = new HashSet<>();
        queue = new LinkedList<>();
        queue.add(startingNode);
    }

    @Override
    public boolean hasNext() {
        return !queue.isEmpty();
    }

    @Override
    public N next() {
        if (!hasNext()) {
            return null;
        }
        N node = queue.pollFirst();
        visited.add(node);
        List<N> adjacentNodes = graph.getAdjacentNodes(node);
        for (N adjacentNode : adjacentNodes) {
            if (!visited.contains(adjacentNode) && !queue.contains(adjacentNode)) {
                queue.add(adjacentNode);
            }
        }
        return node;
    }
}