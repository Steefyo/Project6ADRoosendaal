package a;

public class Node {
    private int g;
    private int f;
    private int h;
    private int x;
    private int y;
    private boolean isBlock;
    private Node parent;

    public Node(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void calculateHeuristic(Node finalNode) {
        this.h = Math.abs(finalNode.getX() - getX()) + Math.abs(finalNode.getY() - getY());
    }

    public void setNodeData(Node currentNode, int cost) {
        int gCost = currentNode.getG() + cost;
        setParent(currentNode);
        setG(gCost);
        calculateFinalCost();
    }

    public boolean checkBetterPath(Node currentNode, int cost) {
        int gCost = currentNode.getG() + cost;
        if (gCost < getG()) {
            setNodeData(currentNode, cost);
            return true;
        }
        return false;
    }

    private void calculateFinalCost() {
        int finalCost = getG() + getH();
        setF(finalCost);
    }

    @Override
    public boolean equals(Object arg0) {
        Node other = (Node) arg0;
        return this.getX() == other.getX() && this.getY() == other.getY();
    }

    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }
    public void setH(int h) {
        this.h = h;
    }
    public void setG(int g) {
        this.g = g;
    }
    public void setF(int f) {
        this.f = f;
    }
    public void setParent(Node parent) {
        this.parent = parent;
    }
    public void setBlock(boolean isBlock) {
        this.isBlock = isBlock;
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public int getH() {
        return h;
    }
    public int getG() {
        return g;
    }
    public int getF() {
        return f;
    }
    public Node getParent() {
        return parent;
    }
    public boolean isBlock() {
        return isBlock;
    }    

    @Override
    public String toString() {
        return "position: x = " + x + ", y = " + y;
    }
}
