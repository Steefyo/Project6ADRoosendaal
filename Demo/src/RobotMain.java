import java.util.List;
import TI.*;
import a.*;

public class RobotMain {

    static int sensorPin1 = 0; // R
    static int sensorPin2 = 1; // M
    static int sensorPin3 = 2; // L

    static Servo sl = new Servo(12);
    static Servo sr = new Servo(13);

    static int defaultSpeed = 1500;
    static int driveSpeed = 15;
    static int sensorSensitivity = 400;

    public static AStar aStar;
    public static String direction;
    public static int currentMove = 2;
    static List<Node> path;

    public static void main(String[] args) {
        System.out.println("Boot up...");
        defineAStar();
        System.out.println("Driving...");


        while (true) {
            int sensor1 = BoeBot.analogRead(sensorPin1);
            int sensor2 = BoeBot.analogRead(sensorPin2);
            int sensor3 = BoeBot.analogRead(sensorPin3);
            BoeBot.wait(100);

            if (((sensor1 >= sensorSensitivity) && (sensor2 <= sensorSensitivity) && (sensor3 <= sensorSensitivity))
                    || ((sensor1 >= sensorSensitivity) && (sensor2 >= sensorSensitivity)
                            && (sensor3 <= sensorSensitivity))) {
                //System.out.println("Tweak Right");
                driveTweakRight();
            } else if (((sensor1 <= sensorSensitivity) && (sensor2 <= sensorSensitivity)
                    && (sensor3 >= sensorSensitivity))
                    || ((sensor1 <= sensorSensitivity) && (sensor2 >= sensorSensitivity)
                            && (sensor3 >= sensorSensitivity))) {
                //System.out.println("Tweak Left");
                driveTweakLeft();
            } else if ((sensor1 <= sensorSensitivity) && (sensor2 >= sensorSensitivity)
                    && (sensor3 <= sensorSensitivity)) {
                //System.out.println("Straight");
                driveStraight();
            } else if ((sensor1 >= sensorSensitivity) && (sensor2 >= sensorSensitivity)
                    && (sensor3 >= sensorSensitivity)) {
                //System.out.println("Intersection - check A*");
                getNextActionBot();
            }
            BoeBot.wait(50);
        }
    }

    public static void driveStraight() {
        sl.update(defaultSpeed - driveSpeed);
        sr.update(defaultSpeed + driveSpeed);
    }
    public static void driveTweakLeft() {
        sl.update(defaultSpeed);
        sr.update(defaultSpeed);
        sl.update(defaultSpeed - driveSpeed);
        sr.update(defaultSpeed - driveSpeed);
    }
    public static void driveTweakRight() {
        sl.update(defaultSpeed);
        sr.update(defaultSpeed);
        sl.update(defaultSpeed + driveSpeed);
        sr.update(defaultSpeed + driveSpeed);
    }
    public static void driveLeft() {
        sl.update(defaultSpeed);
        sr.update(defaultSpeed);
        sl.update(defaultSpeed - driveSpeed);
        //sr.update(defaultSpeed - driveSpeed);
        BoeBot.wait(6500);
    }
    public static void driveRight() {
        sl.update(defaultSpeed);
        sr.update(defaultSpeed);
        //sl.update(defaultSpeed + driveSpeed);
        sr.update(defaultSpeed + driveSpeed);
        BoeBot.wait(4500);
    }
    public static void emergencyBreak() {
        sl.update(defaultSpeed);
        sr.update(defaultSpeed);
    }

    public static void defineAStar() {
        Node positionBot = new Node(0, 0);
        Node positionEnd = new Node(4, 9);
        int gridScaleW = 6;
        int gridScaleH = 11;
        aStar = new AStar(gridScaleW, gridScaleH, positionBot, positionEnd);
        Node[] blocksArray = { new Node(0,6), new Node(1,6), new Node(4,1), new Node(5,6), new Node(1,8), new Node(2,8), new Node(3,8), new Node(4,8) };
        aStar.setBlocks(blocksArray);
        path = aStar.findPath();
        direction = "West";

        System.out.println("Initializing A*...");
    }

    public static void getNextActionBot() {
        if (currentMove <= path.size() - 1) {
            Node start = path.get(0);
            Node current = path.get(currentMove);
            Node end = path.get(path.size() - 1);

            if (current == start) {
                // Start
                System.out.println("Starting Bot...");
                System.out.println(current.toString());
            } else {
                Node previous = path.get(currentMove - 1);
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

            if (current != end) {
                System.out.println("---------");
                currentMove++;
                BoeBot.wait(500); // wait 0.5 sec
            } else {
                emergencyBreak();
            }
        }
    }

    public static void directionDetection(String currentDirection, String movingDirection, Node current) {
        if (currentDirection.equals(movingDirection)) {
            driveStraight();
            System.out.println("Driving to the next tile.");
        } else {
            if (
                ((currentDirection.equals("North")) && (movingDirection.equals("West"))) ||
                ((currentDirection.equals("East")) && (movingDirection.equals("North"))) ||
                ((currentDirection.equals("South")) && (movingDirection.equals("East"))) ||
                ((currentDirection.equals("West")) && (movingDirection.equals("South")))) {
                driveRight();
                System.out.println("Turning right...");
            } else if (
                ((currentDirection.equals("North")) && (movingDirection.equals("East"))) ||
                ((currentDirection.equals("East")) && (movingDirection.equals("South"))) ||
                ((currentDirection.equals("South")) && (movingDirection.equals("West"))) ||
                ((currentDirection.equals("West")) && (movingDirection.equals("North")))) {
                driveLeft();
                System.out.println("Turning left...");
            } else {
                driveRight();
                driveRight();
                System.out.println("180 Turn...");
            }
            direction = movingDirection; // update facing direction
            driveStraight();
            System.out.println(currentDirection + " --> " + movingDirection);
            System.out.println("Driving to the next tile...");
        }
    }
}
