/*
  The Reciever class runs on the robot.
  It waits for a connection to be established via bluetooth, then moves the motor according to the keycodes sent by the initiator.
  Compile with: nxjc Reciever.java
  Run with: nxj -r -o Reciever.nxj Reciever
  Note: the robot reciever class needs to be running first.
*/
import java.io.DataInputStream;
import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.NXTConnection;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.Sound;


class Reciever{
    public static void main(String[] args) throws Exception{
	LCD.drawString("Waiting ..", 0,0);
	NXTConnection connection = Bluetooth.waitForConnection();
	LCD.clear();
	LCD.drawString("Connected", 0,0);
	Sound.setVolume(50);
	Sound.playNote(Sound.PIANO, 440, 100);
	Sound.setVolume(0);
	DataInputStream dis = connection.openDataInputStream();

	while (true){
	    // n contains the keycode for the pressed key
	    int n = dis.readInt();
	    // Escape key, stop the program and close streams
	    if (n== 27)
		break;
	    LCD.clear();
	    LCD.drawInt(n,7,0,1);
	    // Stop action (all keys are released)
	    if (n==-1){
		Motor.B.stop(true);
		Motor.C.stop(true);
	    }
	    // Right key
	    if (n==39){
		Motor.C.forward();
		Motor.B.backward();
	    }
	    // Left key
	    if (n==37){
		Motor.B.forward();
		Motor.C.backward();
	    }
	    // Up key
	    if (n==40){
		Motor.B.forward();
		Motor.C.forward();
	    }
	    // Down key
	    if (n==38){
		Motor.B.backward();
		Motor.C.backward();
	    }
	}
	dis.close();
	connection.close();
    }
}
