import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class RawDataDisplay extends JFrame implements Runnable {
    private JTextArea jTextArea = new JTextArea();
    private String displayString="";
    private final int bufferSize=256;

    public void addToString(String s){
        displayString=displayString+s;
        if(displayString.length()>bufferSize){
            displayString=displayString.substring(displayString.length()-bufferSize);
        }
    }

    public RawDataDisplay() throws HeadlessException {
        super("Data");
        this.add(jTextArea);
        this.setSize(200,600);
        this.setVisible(false);
    }
    int i=0;

    public void toggleVisibility(){
        if (this.isVisible()){
            this.setVisible(false);
        }
        else this.setVisible(true);
    }

    @Override
    public void run(){
        while(true) {
            jTextArea.setText(displayString);
            this.repaint();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
