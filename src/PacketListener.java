import com.fazecast.jSerialComm.*;

import java.util.ArrayList;

public final class PacketListener implements SerialPortMessageListener
{
    private ConnexionStatus connexionStatus;

    public PacketListener(ConnexionStatus connexionStatus) {
        this.connexionStatus = connexionStatus;
    }

    public void setBufferSize(int bufferSize) {
        if (bufferSize>0) this.bufferSize = bufferSize;
        circularBuffer.clear();
    }

    public ArrayList<Sample> getCircularBuffer() {
        return circularBuffer;
    }

    private ArrayList<Sample> circularBuffer = new ArrayList<>();
    private int bufferSize = 128;

    @Override
    public int getListeningEvents() { return SerialPort.LISTENING_EVENT_DATA_RECEIVED; }


    @Override
    public void serialEvent(SerialPortEvent event)
    {
        byte[] newData = event.getReceivedData();
        String newDataString="";
        for (int i = 0; i < newData.length; ++i) {
            newDataString=newDataString+(char) newData[i];
        }
        String[] newDataArray=newDataString.split((char) 9 +"| ");
        Sample sample=null;
        switch (newDataArray.length){
            case 0 : System.out.println("I really should throw exception here");
                    break;
            case 1 :
                sample = new Sample(Double.valueOf(newDataArray[0]));
                break;
            case 2 :
                sample = new Sample(Integer.valueOf(newDataArray[0]), Double.valueOf(newDataArray[1]));
                break;
            case 3 :
                sample = new Sample(Integer.valueOf(newDataArray[0]), Double.valueOf(newDataArray[1]), Double.valueOf(newDataArray[2]));
                break;
            default :
                System.out.println("I really should throw another exception here");
                break;
        }
        if (sample !=null){
            connexionStatus.startRunning();
            circularBuffer.add(sample);
            if (circularBuffer.size()>bufferSize){
                System.out.println(circularBuffer.get(0).valueA);
                circularBuffer.remove(0);
            }
        }
    }

    @Override
    public byte[] getMessageDelimiter() {
        return new byte[]{(byte) 0x0a};
    }

    @Override
    public boolean delimiterIndicatesEndOfMessage() {
        return true;
    }
}
