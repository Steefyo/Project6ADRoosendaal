package input;

import include.ControllerNavigation;
import include.ControllerSession;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.util.regex.Pattern;

import a.*;

import static java.lang.Integer.parseInt;

public class ControllerInsertPage extends ControllerNavigation {

    @FXML
    public Label errorfield;
    public TextField fieldGridX;
    public TextField fieldGridY;
    public TextField fieldBotX;
    public TextField fieldBotY;
    public TextField fieldEndX;
    public TextField fieldEndY;
    public TextField fieldObstacles;
    public ToggleGroup directions;
    public RadioButton directionNorth;
    public RadioButton directionEast;
    public RadioButton directionSouth;
    public RadioButton directionWest;
    
    public ControllerSession session = new ControllerSession();
    public int inputGridX;
    public int inputGridY;
    public int inputBotX;
    public int inputBotY;
    public int inputEndX;
    public int inputEndY;
    public String obstaclesText;
    public Node[] obstacles; // 10 is the limit of obstacles
    public String botDirection;

    public int minGrid = 5;
    public int maxGrid = 15;

    // Fetch value of the button which is selected (Radiobuttons) (Direction of the BoeBot)
    @FXML
    private void directionChange(ActionEvent event) throws IOException {
        RadioButton selected = (RadioButton)directions.getSelectedToggle();
        botDirection = selected.getText();
    }

    @FXML
    private void initiateBot(ActionEvent event) throws IOException {
        directionChange(event); // Fetch the Radio Button
        errorfield.setText(""); // Prefix

        inputGridX = parseInt(fieldGridX.getText());
        inputGridY = parseInt(fieldGridY.getText());
        inputBotX = parseInt(fieldBotX.getText());
        inputBotY = parseInt(fieldBotY.getText());
        inputEndX = parseInt(fieldEndX.getText());
        inputEndY = parseInt(fieldEndY.getText());
        obstaclesText = fieldObstacles.getText();

        checkValues(); // ... Obstacles

        // Only continue if there are no errors left
        if (errorfield.getText() == "") {
            goToMap(event, session);
        }
    }

    // Grid Check
    // Bot Check
    // End position check
    // ...
    public void checkValues() {
        // Grid size
        if ((inputGridX < minGrid) || (inputGridX > maxGrid)) {
            errorfield.setText("Grid width needs to be in between " + minGrid +" and " + maxGrid + ".");
        }
        if ((inputGridY < minGrid) || (inputGridY > maxGrid)) {
            errorfield.setText("Grid height needs to be in between " + minGrid +" and " + maxGrid + ".");
        }

        // Start position
        if ((inputBotX < 0) || (inputBotX > inputGridX)) {
            errorfield.setText("Bot x needs to be in between 0 and " + inputGridX + " to be on the grid.");
        }
        if ((inputBotY < 0) || (inputBotY > inputGridY)) {
            errorfield.setText("Bot y needs to be in between 0 and " + inputGridY + " to be on the grid.");
        }

        // End position
        if ((inputEndX < 0) || (inputEndX > inputGridX)) {
            errorfield.setText("End position x needs to be in between 0 and " + inputGridX + " to be on the grid.");
        }
        if ((inputEndY < 0) || (inputEndY > inputGridY)) {
            errorfield.setText("End position y needs to be in between 0 and " + inputGridY + " to be on the grid.");
        }

        // Obstacles
        String[] obstacleSplit = obstaclesText.replace(" ", "").split(",");
        if (obstacleSplit.length > 10) {
            errorfield.setText("Max amount of obstacles = 10.");
        } else {
            obstacles = new Node[obstacleSplit.length];                                                 // Fetch Lenght of the array (Prefix for filling the Node[array])
            for(int i = 0; i < obstacleSplit.length; i++) {                                             // For each Obstacle in the array
                String obstacleSeparate = obstacleSplit[i];                                             // Separate Obstacle
                String[] obstacleValues = obstacleSeparate.replace(".", ",").split(",");                // IMPOSSIBLE TO SPLIT ON "." APPARENTLY SO SWITCHING IT TO ","

                if ((isNumeric(obstacleValues[0])) && (isNumeric(obstacleValues[1]))) {
                    int posX = parseInt(obstacleValues[0]);                                             // Position X
                    int posY = parseInt(obstacleValues[1]);                                             // Position Y

                    if ((posX < 0) || (posX > maxGrid) || (posY < 0) || (posY > maxGrid)) {
                        errorfield.setText("The obstacle [" + posX + "." + posY + "] is outside of the grid and has no purpose.");
                    }
                    if ((posX == inputBotX) && (posY == inputBotY)) {
                        errorfield.setText("You cannot place an obstacle on top of the bot.");
                    }
                    if ((posX == inputEndX) && (posY == inputEndY)) {
                        errorfield.setText("You cannot place an obstacle on top of the end location.");
                    }

                    // I don't care if you place a obstacle on top of another obstacle.
                    obstacles[i] = new Node(posX, posY);                                                // Add to the Node array on the same position as the split string array
                } else {
                    errorfield.setText("You cannot declare obstacles with something other than numbers.");
                }
            }
        }

        // If there are no errors store them in the session
        if (errorfield.getText() == "") {
            storeValues();
        }
    }

    public void storeValues() {
        session.setX_grid(inputGridX);
        session.setY_grid(inputGridY);
        session.setX_bot(inputBotX);
        session.setY_bot(inputBotY);
        session.setX_end(inputEndX);
        session.setY_end(inputEndY);
        session.setObstacles(obstacles);
        session.setBotDirection(botDirection);
    }

    public boolean isNumeric(String strNum) {
        Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
        if (strNum == null) {
            return false; 
        }
        return pattern.matcher(strNum).matches();
    }
}
