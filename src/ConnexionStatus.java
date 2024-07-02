import javax.swing.*;
import java.awt.*;

enum Status{Running, Halt, No_Data};

public class ConnexionStatus extends JComponent implements Runnable {

    private Status status=Status.Halt;
    private long timeStamp;
    private final long timeout=1500;

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.setFont(new Font("Arial",Font.BOLD,12));
        g.setColor(Color.BLACK);
        g.drawString("Status",0,16);
        switch (status){
            case Running :
                g.setColor(Color.GREEN);
                break;
            case Halt :
                g.setColor(Color.RED);
                break;
            case No_Data:
                g.setColor(Color.ORANGE);
                break;
        }
        g.fillOval(45,3,15,15);
    }

    public void startRunning(){
        status=Status.Running;
        timeStamp=System.currentTimeMillis();
    }

    public void stopRunning(){
        status=Status.Halt;
    }

    @Override
    public void run() {
        while(true){
            if((status==Status.Running)&&(System.currentTimeMillis()-timeStamp>timeout)){
                status=Status.No_Data;
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
