import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class RawDataDisplay extends JFrame implements Runnable {
    private JTextArea jTextArea = new JTextArea();
    private ArrayList<Sample> circularBuffer;

    public RawDataDisplay(ArrayList<Sample> circularBuffer) throws HeadlessException {
        super("Data");
        this.circularBuffer = circularBuffer;
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
            String string = new String();
            for (int i = 0; i < circularBuffer.size(); i++) {
                string = string + "sample (" + Integer.toString(i) + ") = " + Double.toString(circularBuffer.get(i).valueA) + "\n";
            }

            jTextArea.setText(string);
            this.repaint();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
