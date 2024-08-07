import com.fazecast.jSerialComm.SerialPort;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class GUI_MainWindow extends JFrame{

    private boolean isRunning=false;

    private GUI_CommPortConfig commPortConfig = GUI_CommPortConfig.getInstance();
    private ConnexionStatus connexionStatus = new ConnexionStatus();
    private RawDataDisplay rawDataDisplay=new RawDataDisplay();
    private PacketListener packetListener= new PacketListener(connexionStatus,rawDataDisplay);
    private DataVisualizer dataVisualizer = new DataVisualizer(packetListener.getCircularBuffer());
    private JButton runButton = new JButton("RUN");
    private JButton dataViewerButton = new JButton("VIEWER");
    private JSlider xAxisSlider = new JSlider ();
    private JLabel xAxisLabel = new JLabel("Xmax : 128");
    private JSlider yAxisSlider = new JSlider ();
    private JLabel yAxisLabel = new JLabel("Ymax : 1024");
    private JCheckBox isTriggered = new JCheckBox();
    private JLabel isTriggeredLable = new JLabel("Trigger");
    private SerialPort currentComPort=null;
    private DataSender dataSender = new DataSender();
    private JButton displayDataSender = new JButton("SENDER");


    public GUI_MainWindow(){
        super("Happy Serial Plotter (c) ENSEA");
        WindowListener l = new WindowAdapter() {
            public void windowClosing(WindowEvent e){
                System.exit(0);
            }
        };
        addWindowListener(l);

        JPanel upperPanel = new JPanel(new BorderLayout());
        JPanel centerPanel = new JPanel(new BorderLayout());
        JPanel lowerPanel = new JPanel (new GridLayout());
        JPanel globalPanel = new JPanel(new BorderLayout());

        upperPanel.add(commPortConfig,BorderLayout.CENTER);
        upperPanel.add(runButton,BorderLayout.EAST);

        centerPanel.add(dataVisualizer,BorderLayout.CENTER);

        xAxisSlider.setMaximum(256);
        xAxisSlider.setMinimum(10);
        xAxisSlider.setValue(100);
        xAxisSlider.addChangeListener((e)-> {
                dataVisualizer.setxAxis(xAxisSlider.getValue());
                xAxisLabel.setText("Xmax : "+xAxisSlider.getValue());        });
        lowerPanel.add(xAxisLabel,0);
        lowerPanel.add(xAxisSlider,1);

        yAxisSlider.setMaximum(2048);
        yAxisSlider.setMinimum(128);
        yAxisSlider.setValue(1024);
        yAxisSlider.addChangeListener((e)->{
            dataVisualizer.setyAxis(yAxisSlider.getValue());
            yAxisLabel.setText("Ymax : "+yAxisSlider.getValue());});

        lowerPanel.add(yAxisLabel,2);
        lowerPanel.add(yAxisSlider,3);

        isTriggered.addActionListener((e)->dataVisualizer.setTriggered(isTriggered.isSelected()));
        lowerPanel.add(isTriggeredLable,4);
        lowerPanel.add(isTriggered,5);

        lowerPanel.add(connexionStatus,6);

        lowerPanel.add(dataViewerButton,7);
        lowerPanel.add(displayDataSender,8);

        globalPanel.add(upperPanel,BorderLayout.NORTH);
        globalPanel.add(centerPanel,BorderLayout.CENTER);
        globalPanel.add(lowerPanel,BorderLayout.SOUTH);

        this.add(globalPanel);
        this.setSize(800,600);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setVisible(true);

        dataViewerButton.addActionListener((e)->this.rawDataDisplay.toggleVisibility());
        displayDataSender.addActionListener((e)->this.dataSender.toggleVisibility());


        runButton.addActionListener((e)->{
            isRunning = ! isRunning;
            if(isRunning){
                currentComPort = GUI_CommPortConfig.getInstance().getCurrentComPort();
                int baudRate = GUI_CommPortConfig.getInstance().getBaudRate();
                System.out.println("Try opening "+currentComPort.getDescriptivePortName() + "@ " + baudRate + " bps");
                currentComPort.openPort(250);
                if (currentComPort.isOpen()){
                    currentComPort.setBaudRate(baudRate);
                    currentComPort.addDataListener(packetListener);
                    System.out.println("Success");
                    connexionStatus.startRunning();
                    this.runButton.setText("STOP"); // Next order
                }
                else{
                    System.out.println("Failure");
                    connexionStatus.stopRunning();
                    isRunning=false;
                }
            }
            else {
                this.runButton.setText("RUN");      // Next order
                currentComPort.closePort();
                connexionStatus.stopRunning();
                System.out.println("Closing "+currentComPort.getDescriptivePortName());
            }

            });

            Timer timer = new Timer(40,(t)->{
                dataVisualizer.repaint();
                connexionStatus.repaint();
            });
            timer.start();
            Thread connexionStatusThread = new Thread(connexionStatus);
            connexionStatusThread.start();
            Thread rawDataDisplayThread = new Thread(rawDataDisplay);
            rawDataDisplayThread.start();

        }

        public static void main(String[] args){

            new GUI_MainWindow();
        }
    }
