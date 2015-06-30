/*
  The Initiator class runs on the computer.
  It initalizes a connection to the robot via bluetooth, and feeds it with keycodes after key presses. The arrow keys control the rebot movement, the ESC key closes both applications.
  Compile with: nxjpcc Initiator.java RepeatingReleasedEventsFixer.java
  Run with: nxjpc Initiator
  Note: the robot reciever class needs to be running before this class is ran.
*/
import java.io.DataOutputStream;
import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTInfo;
import lejos.pc.comm.NXTCommFactory;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;


class Initiator{
    public static void main(String[] args) throws Exception{
	// Fixes the repeated multiple keyReleased events on linux.
	new RepeatingReleasedEventsFixer().install();
	NXTComm nxtComm = NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH);
	// Robot needs to be paired beforehand, MAC can be found using:
	// sudo hcitool scan
        String mac = "00:16:53:0B:2D:DC";
	NXTInfo nxtInfo = new NXTInfo(NXTCommFactory.BLUETOOTH, "NXT", mac);
	nxtComm.open(nxtInfo);
	DataOutputStream dos = new DataOutputStream(nxtComm.getOutputStream());
	// The frame is only used to capture key presses
	JFrame jframe = new JFrame();
	jframe.addKeyListener(new MKeyListener(dos, jframe));
	jframe.setSize(400, 350);
	jframe.setVisible(true);	
    }
}


class MKeyListener extends KeyAdapter {
    private DataOutputStream dos;
    private JFrame jframe;
    private int lastkey = 0;

    public MKeyListener(DataOutputStream dos, JFrame jframe){
	this.dos = dos;
	this.jframe = jframe;
    }

    @Override
    public void keyPressed(KeyEvent event) {
	int keycode = event.getKeyCode();
	// Because the keyPressed event is repeated when a key is held, we ignore events with similar keycode as the previous one.
	if (lastkey == keycode){
	    return;
	}else{
	    lastkey = keycode;
	}
	System.out.println("Sending: " + keycode);
	try{
	    this.dos.writeInt(keycode);
	    dos.flush();
	}catch (Exception e){
	    System.out.println("whoops" + e);
	}
	// In case the ESC key is pressed, we close the program.
	if(event.getKeyCode() == 27){
	    jframe.dispatchEvent(new WindowEvent(jframe, WindowEvent.WINDOW_CLOSING));
	    try{
		dos.close();
	    }catch (Exception e){
		System.out.println(e);
	    }
	    System.exit(0);
	}
    }

    @Override
    public void keyReleased(KeyEvent event) {
	// When keys are released, the robot should stop, the unused keycode -1 is used to inform the robot of that.
	System.out.println("Realeased "+ event.getKeyCode());
	try{
	    this.lastkey = -1;
	    this.dos.writeInt(-1);
	    dos.flush();
	}catch (Exception e){
	    System.out.println("whoops" + e);
	}
    }
}

