package pl.edu.uj.ujrobotics.robotic_client;

/**
 * Created by piotr on 25.11.14.
 */
public enum command {

    LEFT_1("LEFT_1", 'u'),
    LEFT_2("LEFT_2", 'j'),
    LEFT_3("LEFT_3", 'm'),
    FORWARD_1("FORWARD_1", 'i'),
    FORWARD_2("FORWARD_2", 'k'),
    FORWARD_3("FORWARD_3", ','),
    RIGHT_1("RIGHT_1", 'o'),
    RIGHT_2("RIGHT_2", 'l'),
    RIGHT_3("RIGHT_3", '.'),
    STOP("STOP", ' '),
    FASTER("FASTER", 'q'),
    SLOWER("SLOWER", 'z');

    /**
     * Control Your Turtlebot!
     ---------------------------
     Moving around:
     u    i    o
     j    k    l
     m    ,    .

     q/z : increase/decrease max speeds by 10%
     w/x : increase/decrease only linear speed by 10%
     e/c : increase/decrease only angular speed by 10%
    space key, k : force stop
     anything else : stop smoothly

     */
    private String header;
    private char command;

    private command(String header, char command){
        this.header = header;
        this.command = command;
    }

    public String getHeader(){
        return header;
    }

    public char getCommand(){
        return command;
    }
    @Override
    public String toString() {
        return "Header : " + header +  " Command : " + command;
    }
}
