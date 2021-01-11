package map;

import a.*;
import include.ControllerNavigation;
import include.ControllerSession;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.fxml.FXML;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Label;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import java.io.IOException;

public class ControllerMap extends ControllerNavigation {

    // The direction the BoeBot is facing
    public String direction;
    
    @FXML
    public GridPane route;
    public ScrollPane status;
    public VBox statusRoot = new VBox();

    // Define "global" so it's use-able in other functions
    public AStar aStar;
    public ArrayList<ArrayList<Rectangle>> gridArray = new ArrayList<>();
    
    // Load values from the previous page
    // Initiate Grid
    // Initiate Bot
    // Initiate End locations
    // Convert values to A* logic (Define map)
    // Add Obstacles to the A* formula
    // Draw items on the page using multiple different draw functions so it is easily change-able
    public void loadSessionController(ControllerSession sessionOld) {
        int x_grid_value = sessionOld.getX_grid();
        int y_grid_value = sessionOld.getY_grid();
        Node positionBot = new Node(sessionOld.getX_bot(), sessionOld.getY_bot());
        Node positionEnd = new Node(sessionOld.getX_end(), sessionOld.getY_end());

        aStar = new AStar(x_grid_value, y_grid_value, positionBot, positionEnd);

        Node[] blocksArray = sessionOld.getObstacles();
        aStar.setBlocks(blocksArray);

        drawGrid(x_grid_value, y_grid_value);
        drawStart(positionBot.getX(), positionBot.getY());
        drawEnd(positionEnd.getX(), positionEnd.getY());
        drawObstacles(blocksArray);       

        direction = sessionOld.getBotDirection(); // Direction of the nose of the BoeBot
    }

    public void drawGrid(int x_grid_value, int y_grid_value) {
        for (int x_grid = 1; x_grid <= x_grid_value; x_grid++) {                        // For axes X
            ArrayList<Rectangle> RowArray = new ArrayList<>();                          // Make a list for each axes
            for (int y_grid = 1; y_grid <= y_grid_value; y_grid++) {                    // For axes Y
                Rectangle r = new Rectangle(0, 0, 30, 30);                              // Create Items on the page
                r.setFill(Color.WHITE);                                                 // Set Color Background
                r.setStroke(Color.BLACK);                                               // Set Color Border
                RowArray.add(r);                                                        // ADD RECTANGLE to axes list
                route.add(r, x_grid, y_grid);                                           // Add rows and columns to the pane
            }
            gridArray.add(RowArray);                                                    // ADD axes list to grid list
        }
    }

    // Draw Start (in red)
    public void drawStart(int x, int y) {
        setGridItemColor(x, y, Color.RED);
    }

    // Draw Start (in blue)
    public void drawEnd(int x, int y) {
        setGridItemColor(x, y, Color.BLUE);
    }

    // Foreach obstacle draw obstacle (in yellow)
    public void drawObstacles(Node[] blocksArray) {
        for (Node block : blocksArray) {                                               
            setGridItemColor(block.getX(),block.getY(), Color.YELLOW);
        }
    }

    // Foreach used path draw route (in gray)
    public void drawRoute(List<Node> path) {
        for (Node node : path) {
            setGridItemColor(node.getX(),node.getY(), Color.GRAY);
        }
    }

    public void drawStatus() {
        String cssLayout = "-fx-border-color: black;\n" +
                   "-fx-border-width: 1;\n";

        statusRoot.setSpacing(10);
        statusRoot.setStyle(cssLayout);
        status.setContent(statusRoot);
        status.setPannable(true);
    }

    public void drawStatusItemAdd(String action, String progression) {
        statusRoot.getChildren().add(drawStatusItem(action, progression));
    }

    public AnchorPane drawStatusItem(String action, String progression) {
        AnchorPane anchorPane = new AnchorPane();               // Menu item
        anchorPane.setPrefSize(290.0, 90.0);                    // Menu item settings

        ObservableList list = anchorPane.getChildren();         // Menu item inside logic

        Label actionLabel = new Label(action);                  // Define Labels
        Label progressionLabel = new Label(progression);

        actionLabel.setMaxWidth(250.0);                         // Define max length
        progressionLabel.setMaxWidth(250.0);

        actionLabel.setLayoutX(14.0);
        actionLabel.setLayoutY(22.0);
        progressionLabel.setLayoutX(14.0);
        progressionLabel.setLayoutY(45.0);

        list.add(actionLabel);
        list.add(progressionLabel);

        return anchorPane;
    }

    // Change color on certain location
    public void setGridItemColor(int x, int y, Color color) {
        gridArray.get(x).get(y).setFill(color);
    }

    // Return to insert menu
    @FXML
    private void backToInsert(ActionEvent event) throws IOException {
        goToInsert(event);
    }

    // Generate the "perfect" route using findPath();
    // Draw route
    // Show logs
    //// If start position
    //// Else if end position
    //// Else "driving"
    ///// Use the previous location to determine the new direction/location
    ///// Check on which side the new cube is
    @FXML
    private void startBot(ActionEvent event) throws IOException {
        List<Node> path = aStar.findPath();
        drawRoute(path);

        for (int i = 0; i < path.size(); i++) {
            Node start = path.get(0);
            Node current = path.get(i);
            Node end = path.get(path.size() - 1);

            if (current == start) {
                drawStatusItemAdd("Starting Bot", current.toString());
            } else {
                Node previous = path.get(i - 1);
                if (current.getX() > previous.getX()) {
                    directionDetection(direction, "West", current);
                } else if (current.getX() < previous.getX()) {
                    directionDetection(direction, "East", current);
                } else if (current.getY() > previous.getY()) {
                    directionDetection(direction, "South", current);
                } else if (current.getY() < previous.getY()) {
                    directionDetection(direction, "North", current);
                }
            }

            if (current == end) {
                drawStatusItemAdd("End of the route", current.toString());
            }

            // TODO: Insert into log on the page.
            drawStatus();
            // scrollPane.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);         // Set scrollbar when needed
        }
    }   

    // Check which way the BoeBot is facing Generate the corresponding returns.
    // If
    //// Drive Straight
    // Else
    //// Determine which direction the BoeBot needs to go to
    ////// TURN Left
    ////// TURN Right
    ////// BACKWARDS basically means something went wrong
    //// Show turn direction
    //// Update direction the car is facing
    //// Drive straight
    public void directionDetection(String currentDirection, String movingDirection, Node current) {
        if (currentDirection.equals(movingDirection)) {
            drawStatusItemAdd("Driving forward.", current.toString());
        } else {
            String moveText;
            if (
                ((currentDirection.equals("North")) && (movingDirection.equals("West"))) ||
                ((currentDirection.equals("East")) && (currentDirection.equals("North"))) || 
                ((currentDirection.equals("South")) && (movingDirection.equals("East"))) || 
                ((currentDirection.equals("West")) && (movingDirection.equals("South")))) {
                moveText = "Turn right.";
            } else if (
                ((currentDirection.equals("North")) && (movingDirection.equals("East"))) || 
                ((currentDirection.equals("East")) && (movingDirection.equals("South"))) || 
                ((currentDirection.equals("South")) && (movingDirection.equals("West"))) || 
                ((currentDirection.equals("West")) && (currentDirection.equals("North")))) {
                moveText = "Turn left.";
            } else {
                moveText = "180 turn.";
            }
            direction = movingDirection;
            drawStatusItemAdd(moveText, currentDirection + " --> " + movingDirection);
            drawStatusItemAdd("Driving forward.", "New " + current.toString());
        }
    }
}