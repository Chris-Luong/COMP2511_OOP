package unsw.graph;

import java.util.*;

public class DepthFirstGraphIterator<N extends Comparable<N>> implements Iterator<N> {
    private Graph<N> graph;
    Set<N> visited;
    Deque<N> queue;

    public DepthFirstGraphIterator(Graph<N> graph, N startingNode) {
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
        N node = queue.pollLast();
        visited.add(node);
        List<N> adjacentNodes = graph.getAdjacentNodes(node);
        adjacentNodes.sort(new Comparator<N>() {
            @Override
            public int compare(N o1, N o2) {
                return -o1.compareTo(o2);
            }
        });
        for (N adjacentNode : adjacentNodes) {
            if (!visited.contains(adjacentNode) && !queue.contains(adjacentNode)) {
                queue.addLast(adjacentNode);
            }
        }
        return node;
    }

    
}