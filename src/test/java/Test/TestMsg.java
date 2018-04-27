package Test;

import common.java.httpServer.booter;
import common.java.nlogger.nlogger;

public class TestMsg {
    public static void main(String[] args) {
        booter booter = new booter();
        try {
            System.out.println("Message");
            System.setProperty("AppName", "Message");
            booter.start(1002);
        } catch (Exception e) {
            nlogger.logout(e);
        }
    }
}
