package a;

import java.util.*;

public class AStar {
    private static int DEFAULT_HV_COST = 10; // Horizontal - Vertical Cost
    private int hvCost;
    private Node[][] searchArea;
    private PriorityQueue<Node> openList;
    private Set<Node> closedSet;
    private Node start;
    private Node end;

    public AStar(int x, int y, Node start, Node end, int hvCost) {
        this.hvCost = hvCost;
        setStart(start);
        setEnd(end);
        this.searchArea = new Node[x][y];
        this.openList = new PriorityQueue<Node>(new Comparator<Node>() {
            @Override
            public int compare(Node node0, Node node1) {
                return Integer.compare(node0.getF(), node1.getF());
            }
        });
        setNodes();
        this.closedSet = new HashSet<>();
    }

    public AStar(int x, int y, Node start, Node end) {
        this(x, y, start, end, DEFAULT_HV_COST);
    }

    private void setNodes() {
        for (int i = 0; i < searchArea.length; i++) {
            for (int j = 0; j < searchArea[0].length; j++) {
                Node node = new Node(i, j);
                node.calculateHeuristic(getEnd());
                this.searchArea[i][j] = node;
            }
        }
    }

    public void setBlocks(Node[] blocksArray) {
        for (int i = 0; i < blocksArray.length; i++) {
            int x = blocksArray[i].getX();
            int y = blocksArray[i].getY();
            setBlock(x, y);
        }
    }

    public List<Node> findPath() {
        openList.add(start);
        while (!isEmpty(openList)) {
            Node currentNode = openList.poll();
            closedSet.add(currentNode);
            if (isEnd(currentNode)) {
                return getPath(currentNode);
            } else {
                addAdjacentNodes(currentNode);
            }
        }
        return new ArrayList<Node>();
    }

    private List<Node> getPath(Node currentNode) {
        List<Node> path = new ArrayList<Node>();
        path.add(currentNode);
        Node parent;
        while ((parent = currentNode.getParent()) != null) {
            path.add(0, parent);
            currentNode = parent;
        }
        return path;
    }

    private void addAdjacentNodes(Node currentNode) {
        addAdjacentUpperx(currentNode);
        addAdjacentMiddlex(currentNode);
        addAdjacentLowerx(currentNode);
    }

    private void addAdjacentLowerx(Node currentNode) {
        int x = currentNode.getX();
        int y = currentNode.getY();
        int lowerx = x + 1;
        if (lowerx < getSearchArea().length) {
            checkNode(currentNode, y, lowerx, getHvCost());
        }
    }

    private void addAdjacentMiddlex(Node currentNode) {
        int x = currentNode.getX();
        int y = currentNode.getY();
        int middlex = x;
        if (y - 1 >= 0) {
            checkNode(currentNode, y - 1, middlex, getHvCost());
        }
        if (y + 1 < getSearchArea()[0].length) {
            checkNode(currentNode, y + 1, middlex, getHvCost());
        }
    }

    private void addAdjacentUpperx(Node currentNode) {
        int x = currentNode.getX();
        int y = currentNode.getY();
        int upperx = x - 1;
        if (upperx >= 0) {
            checkNode(currentNode, y, upperx, getHvCost());
        }
    }

    private void checkNode(Node currentNode, int y, int x, int cost) {
        Node adjacentNode = getSearchArea()[x][y];
        if (!adjacentNode.isBlock() && !getClosedSet().contains(adjacentNode)) {
            if (!getOpenList().contains(adjacentNode)) {
                adjacentNode.setNodeData(currentNode, cost);
                getOpenList().add(adjacentNode);
            } else {
                boolean changed = adjacentNode.checkBetterPath(currentNode, cost);
                if (changed) {
                    getOpenList().remove(adjacentNode);
                    getOpenList().add(adjacentNode);
                }
            }
        }
    }

    private boolean isEnd(Node currentNode) {
        return currentNode.equals(end);
    }
    private boolean isEmpty(PriorityQueue<Node> openList) {
        return openList.size() == 0;
    }

    private void setBlock(int x, int y) {
        this.searchArea[x][y].setBlock(true);
    }
    public void setStart(Node start) {
        this.start = start;
    }
    public void setEnd(Node end) {
        this.end = end;
    }
    public void setSearchArea(Node[][] searchArea) {
        this.searchArea = searchArea;
    }
    public void setOpenList(PriorityQueue<Node> openList) {
        this.openList = openList;
    }
    public void setClosedSet(Set<Node> closedSet) {
        this.closedSet = closedSet;
    }
    public void setHvCost(int hvCost) {
        this.hvCost = hvCost;
    }

    public Node getStart() {
        return start;
    }
    public Node getEnd() {
        return end;
    }
    public Node[][] getSearchArea() {
        return searchArea;
    }
    public PriorityQueue<Node> getOpenList() {
        return openList;
    }
    public Set<Node> getClosedSet() {
        return closedSet;
    }
    public int getHvCost() {
        return hvCost;
    }
}