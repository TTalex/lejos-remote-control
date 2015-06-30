//package main;
import java.io.*;
import javax.bluetooth.*;
import lejos.nxt.*;
import lejos.pc.comm.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JTextField;
//import main.RepeatingReleasedEventsFixer;


class Initiator{
    public static void main(String[] args) throws Exception{
	new RepeatingReleasedEventsFixer().install();
	NXTComm nxtComm = NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH);
	NXTInfo nxtInfo = new NXTInfo(NXTCommFactory.BLUETOOTH, "NXT", "00:16:53:0B:2D:DC");
	nxtComm.open(nxtInfo);
	//	DataInputStream dis = new DataInputStream(nxtComm.getInputStream());
	DataOutputStream dos = new DataOutputStream(nxtComm.getOutputStream());
	JFrame jframe = new JFrame();
	jframe.addKeyListener(new MKeyListener(dos, jframe));
	jframe.setSize(400, 350);
	jframe.setVisible(true);	
/*
	dis.close();
	dos.close();
	nxtComm.close();*/
    }
}

class MKeyListener extends KeyAdapter {
    DataOutputStream dos;
    JFrame jframe;
    int lastkey = 0;
    public MKeyListener(DataOutputStream dos, JFrame jframe){
	this.dos = dos;
	this.jframe = jframe;
    }
    @Override
	public void keyPressed(KeyEvent event) {
	int keycode = event.getKeyCode();
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

