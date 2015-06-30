import java.io.*;
import javax.bluetooth.*;
import lejos.nxt.*;
import lejos.nxt.comm.*;
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
	//DataOutputStream dos = connection.openDataOutputStream();
	//	Motor.B.setSpeed(500);
	//	Motor.C.setSpeed(500);
	while (true){
	    int n = dis.readInt();
	    if (n== 27)
		break;
	    LCD.clear();
	    LCD.drawInt(n,7,0,1);
	    if (n==-1){
		Motor.B.stop(true);
		Motor.C.stop(true);
	    }
	    if (n==39){
		Motor.C.forward();
		Motor.B.backward();
	    }
	    if (n==37){
		Motor.B.forward();
		Motor.C.backward();
	    }
	    if (n==40){
		Motor.B.forward();//rotate(40,true);
		Motor.C.forward();//rotate(40,true);
	    }
	    if (n==38){
		Motor.B.backward();//rotate(-40,true);
		Motor.C.backward();//rotate(-40,true);
	    }
	}
	dis.close();
	//dos.close();
	connection.close();
    }
}