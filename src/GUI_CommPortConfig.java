import com.fazecast.jSerialComm.SerialPort;

import javax.swing.*;
import java.util.ArrayList;

public class GUI_CommPortConfig extends JPanel {
    private SerialPort[] serialPorts = SerialPort.getCommPorts();
    private ArrayList<String> portList= new ArrayList<>();
    private final String[] possibleBaudRate={"9600","115200","230400","460800","921600","1000000"};
    JComboBox baudRateChooser = new JComboBox(possibleBaudRate);

    private static volatile GUI_CommPortConfig instance = null;
    private JComboBox comPortChooser = new JComboBox(portList.toArray());

    private GUI_CommPortConfig(){
        super();
        enumerate();

        this.add(comPortChooser);
        this.add(baudRateChooser);

        baudRateChooser.setSelectedIndex(1);        //115200 bps par défaut

        JButton reunemumerate =new JButton("Check for new Port");
        this.add(reunemumerate);

        reunemumerate.addActionListener((e)->{
            enumerate();
        });
    }

    private void enumerate(){
        serialPorts=null;
        portList.clear();
        comPortChooser.removeAllItems();
        serialPorts = SerialPort.getCommPorts();
        for (SerialPort serialPort : serialPorts){
            portList.add(serialPort.getDescriptivePortName());
        }
        for (String port : portList) comPortChooser.addItem(port);

    }

    public SerialPort getCurrentComPort() {
        return serialPorts[comPortChooser.getSelectedIndex()];
    }

    public int getBaudRate() {
        return Integer.valueOf(possibleBaudRate[baudRateChooser.getSelectedIndex()]);
    }

    public final static GUI_CommPortConfig getInstance() {
        if (GUI_CommPortConfig.instance == null) {
            synchronized(GUI_CommPortConfig.class) {
                if (GUI_CommPortConfig.instance == null) {
                    GUI_CommPortConfig.instance = new GUI_CommPortConfig();
                }
            }
        }
        return GUI_CommPortConfig.instance;
    }
}
