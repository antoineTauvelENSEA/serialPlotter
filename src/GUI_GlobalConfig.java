import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class GUI_GlobalConfig extends JFrame {
    private boolean hasTimeStamp=false;
    private static volatile GUI_GlobalConfig instance = null;

    private GUI_GlobalConfig(){
        super("Global configuration");

        WindowListener l = new WindowAdapter() {
            public void windowClosing(WindowEvent e){
                setVisible(false);
            }
        };
        addWindowListener(l);

        JPanel jPanel = new JPanel();

        JCheckBox hasTimeStampCheckBox = new JCheckBox("Time Stamp");

        hasTimeStampCheckBox.addActionListener((e)->{
            System.out.println(e);
        });
        jPanel.add(hasTimeStampCheckBox);

        this.setContentPane(jPanel);
        this.setSize(350,100);
        this.setVisible(true);
    }

    public final static GUI_GlobalConfig getInstance() {
        if (GUI_GlobalConfig.instance == null) {
            synchronized(GUI_CommPortConfig.class) {
                if (GUI_GlobalConfig.instance == null) {
                    GUI_GlobalConfig.instance = new GUI_GlobalConfig();
                }
            }
        }
        return GUI_GlobalConfig.instance;
    }

    public static void main(String[] args){
        GUI_GlobalConfig.getInstance();
    }
}
