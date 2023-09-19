package mmehdi.com.android.modbus.ioio;

import android.util.Log;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

import ioio.lib.api.DigitalInput;
import ioio.lib.api.DigitalOutput;
import ioio.lib.api.IOIO;
import ioio.lib.api.Uart;

/**
 * Created by Mujtaba on 06/02/2018.
 */

public class UARTController {
    private String TAG = this.getClass().getSimpleName();
    private int rx, tx, rts, baudFrequency;
    private DigitalOutput rtsPin;


    private Uart relayBoardUart;
    private OutputStream relayBoardOut;
    private InputStream relayBoardIn;

    private IOIO ioio_;

    private boolean connected=false;

    private boolean isOnGoingCall;

    private ModbusUtility modbusUtility;
    public void setup(IOIO ioio_){
        this.ioio_=ioio_;

        /*UART Pins*/
        this.rts=4;
        this.tx=5; //4
        this.rx=3; //5


        this.baudFrequency =9600;

        initUARTPins(this.ioio_);

        isOnGoingCall=false;
        modbusUtility = new ModbusUtility();
    }



    public byte[] writeData(String device_id, String relay_id, String cmd, String delay){
        byte[] bytes=null;
        try {
            String message = "{\n" +
                    "\"" + Constants.KEY_DEVICE_ID + "\":\"" + device_id + "\",\n" +
                    "\"" + Constants.KEY_RELAY_ID + "\":\"" + relay_id + "\",\n" +
                    "\"" + Constants.KEY_DELAY + "\":\"" + delay + "\",\n" +
                    "\"" + Constants.KEY_CMD + "\":\"" + cmd + "\"}";


            bytes = modbusUtility.getCMD(message);

            //while(isOutBusy.get())

            synchronized (relayBoardOut) {
                rtsPin.write(true);

                relayBoardOut.flush();
                relayBoardOut.write(bytes, 0, bytes.length);
                TimeUnit.MILLISECONDS.sleep(200); //thread safe patch
                //relayBoardOut.flush();

            }

        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            return bytes;
        }
    }

    public void writeData(String message){
        try {
            Log.i(TAG,"WRITING:"+message +"\nLENGTH:"+message.length());

            byte[] bytes = modbusUtility.getCMD(message);
            rtsPin.write(true);
            relayBoardOut.write(bytes,0,bytes.length);
            relayBoardOut.flush();
            synchronized (relayBoardOut) {
                //Log.i(TAG, "BYTES LENGTH:" + bytes.length);
                //relayBoardOut.wait(bytes.length*10);
                //relayBoardOut.wait(100);
                //rtsPin.write(false);
                //rtsPin.write(false);
                //rtsPin1.waitForValue(false);
            }


        }
        catch (Exception e){
            e.printStackTrace();
        }
    }





    public void writeModbusHexData(String hexMessage){
        try {
            //Log.i(TAG,"WRITING:"+message +"\nLENGTH:"+message.length());

            byte[] bytes = modbusUtility.getHexCMD(hexMessage);
            //rtsPin.write(true);
            relayBoardOut.write(bytes,0,bytes.length);
            relayBoardOut.flush();

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


    public void initUARTPins(IOIO ioio_){
        try {

            DigitalInput.Spec rxSpec = new DigitalInput.Spec(rx, DigitalInput.Spec.Mode.PULL_UP);
            DigitalOutput.Spec txSpec = new DigitalOutput.Spec(tx, DigitalOutput.Spec.Mode.NORMAL);

            relayBoardUart = ioio_.openUart(rxSpec,txSpec, baudFrequency, Uart.Parity.NONE,Uart.StopBits.ONE);
            rtsPin=ioio_.openDigitalOutput(rts, DigitalOutput.Spec.Mode.NORMAL,true);
            relayBoardOut = relayBoardUart.getOutputStream();

            connected=true;

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public void destroy(){
        try {
            ioio_=null;

            relayBoardUart=null;
            if(relayBoardOut!=null) {
                relayBoardOut.close();
                relayBoardOut=null;
            }

            if(relayBoardIn!=null) {
                relayBoardIn.close();
                relayBoardIn=null;
            }

            modbusUtility=null;
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
