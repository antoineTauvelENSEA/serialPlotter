import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class DataVisualizer extends JComponent {
    private ArrayList<Sample> circularBuffer;
    private final int division=8;
    private boolean isTriggered=false;
    private int yAxis=1024;

    public void setTriggered(boolean triggered) {
        isTriggered = triggered;
    }

    public void setyAxis(int yAxis) {
        this.yAxis = yAxis;
    }

    public DataVisualizer(ArrayList<Sample> circularBuffer) {
        this.circularBuffer = circularBuffer;
        this.setSize(800,400);
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        // --- Activate antialiasing flag ---
        //Graphics2D graphics = (Graphics2D) graphicsSimple;
        //graphics.setRenderingHint( RenderingHints.KEY_ANTIALIASING,
        //        RenderingHints.VALUE_ANTIALIAS_ON );

        // --- White background ---
        graphics.setColor( Color.WHITE );
        graphics.fillRect( 0, 0, getWidth(), getHeight() );

        // --- Draw axis ---
        graphics.setColor( Color.LIGHT_GRAY );
        for (int i=0;i<division;i++){
            graphics.drawLine( 0, i*getHeight()/division, getWidth(), i*getHeight()/division );
            graphics.drawLine( i*getWidth()/division, 0, i*getWidth()/division, getHeight() );
        }

        // --- Draw values ---
        graphics.setColor( Color.BLACK );
        graphics.drawString( "0", (int)(getWidth()*0.02), (int)(getHeight()*0.98));
        graphics.drawString( Integer.toString(circularBuffer.size()), (int)(getWidth()*0.98), (int)(getHeight()*0.98));
        graphics.drawString( Integer.toString(-yAxis/2), (int)(getWidth()*0.01), (int)(getHeight()*0.96));
        graphics.drawString( "+"+Integer.toString(yAxis/2), (int)(getWidth()*0.01), (int)(getHeight()*0.04));


        // --- Draw curve ---
        double step = 0.1;
        graphics.setColor( new Color( 255, 0, 255 ) );


        if (circularBuffer.size() !=0)
        {
            int firstValue=0;

            if (isTriggered){
                int i=0;
                while(circularBuffer.get(i).valueA>0){i++;};
                while(circularBuffer.get(i).valueA<0){i++;};
                firstValue=i;
            }

            int oldX= xToPixel(0);
            int oldY= yToPixel(circularBuffer.get(firstValue).valueA);

            for( int i=1;i<circularBuffer.size()-firstValue;i++) {
                int index=(i+firstValue);
                int x = xToPixel( i );
                int y = yToPixel( circularBuffer.get(index).valueA );

                graphics.drawLine( x, y, oldX, oldY );

                oldX = x;
                oldY = y;
            }
                    // Factorisation de code à faire !
            graphics.setColor( new Color( 0, 128, 255 ) );

            if (!Double.isNaN((circularBuffer.get(0).valueB))){
                oldX= xToPixel(0);
                oldY= yToPixel(circularBuffer.get(firstValue).valueB);

                for( int i=1;i<circularBuffer.size()-firstValue;i++) {
                    int index=(i+firstValue);
                    int x = xToPixel( i );
                    int y = yToPixel( circularBuffer.get(index).valueB );

                    graphics.drawLine( x, y, oldX, oldY );

                    oldX = x;
                    oldY = y;
                }
            }
        }
    }

    private int xToPixel( double x ) {
        return (int)( getWidth() * (x/circularBuffer.size()) );
    }

    private int yToPixel( double y ) {
        return (int)( getHeight() * (-y+yAxis/2)/yAxis );
    }
}
