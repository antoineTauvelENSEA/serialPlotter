import javax.swing.*;
import java.awt.*;
import java.nio.charset.StandardCharsets;

public class DataSender extends JFrame {
    private JTextField dataToSend = new JTextField();
    private JButton sendButton = new JButton("Send");
    private JPanel jPanel = new JPanel();

    public DataSender(){

        super("Send DATA");
        GridLayout gridLayout = new GridLayout(2,0);
        jPanel.setLayout(gridLayout);
        this.setResizable(false);

        jPanel.add (dataToSend);
        jPanel.add (sendButton);

        this.add(jPanel);
        this.setSize(250,120);
        this.setVisible(false);

        dataToSend.addActionListener((e)->{
            sendData();
        });

        sendButton.addActionListener((e)->{
            sendData();
        });
    }

    public void toggleVisibility(){
        this.setVisible(!this.isVisible());
    }

    private void sendData(){
        System.out.println("Sending");
        if(GUI_CommPortConfig.getInstance().getCurrentComPort().isOpen()){
            int size=dataToSend.getText().length();
            byte[] dataByte = dataToSend.getText().getBytes(StandardCharsets.UTF_8);
            GUI_CommPortConfig.getInstance().getCurrentComPort().writeBytes(dataByte,size);
        }
        else {
            System.out.println("COM port not open");
        }
    }
}
