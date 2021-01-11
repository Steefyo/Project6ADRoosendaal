package include;

import a.*;

public final class ControllerSession {
    private int x_grid;
    private int y_grid;
    private int x_bot;
    private int y_bot;
    private int x_end;
    private int y_end;
    private Node[] obstacles;
    private String botDirection;

    public int getX_grid() {
        return x_grid;
    }
    public int getY_grid() {
        return y_grid;
    }
    public int getX_bot() {
        return x_bot;
    }
    public int getY_bot() {
        return y_bot;
    }
    public int getX_end() {
        return x_end;
    }
    public int getY_end() {
        return y_end;
    }
    public Node[] getObstacles() {
        return obstacles;
    }
    public String getBotDirection() {
        return botDirection;
    }

    public void setX_grid(int x_grid) {
        this.x_grid = x_grid;
    }
    public void setY_grid(int y_grid) {
        this.y_grid = y_grid;
    }
    public void setX_bot(int x_bot) {
        this.x_bot = x_bot;
    }
    public void setY_bot(int y_bot) {
        this.y_bot = y_bot;
    }
    public void setX_end(int x_end) {
        this.x_end = x_end;
    }
    public void setY_end(int y_end) {
        this.y_end = y_end;
    }
    public void setObstacles(Node[] obstacles) {
        this.obstacles = obstacles;
    }
    public void setBotDirection(String botDirection) {
        this.botDirection = botDirection;
    }
}
