import com.fazecast.jSerialComm.SerialPort;

public class Main {
    public static void main(String[] args) {
        SerialPort serialPort = SerialPort.getCommPort("COM4");
      //  PacketListener packetListener = new PacketListener();
        serialPort.openPort();
        try {
            while (true)
            {
                while (serialPort.bytesAvailable() == 0)
                    Thread.sleep(20);

                byte[] readBuffer = new byte[serialPort.bytesAvailable()];
                int numRead = serialPort.readBytes(readBuffer, readBuffer.length);
                System.out.println("Read " + numRead + " bytes : " + readBuffer);
            }
        } catch (Exception e) { e.printStackTrace(); }
        serialPort.closePort();
    }
}
