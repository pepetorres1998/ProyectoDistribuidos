import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class Host extends MainWindow implements Runnable
{
	JTextField txt_ip;
	JButton btn_enviar;

	public boolean run_me = false;
	public int puerto = 5400;

	Host()
	{
		MyLabel l_puerto = new MyLabel("Puerto: " + puerto);
		MyLabel l_ip = new MyLabel("IP Servidor:");
		txt_ip = new JTextField(50);
		txt_ip.requestFocusInWindow();

		MyLabel l_titulo = new MyLabel("¿Enviar datos?");
		btn_enviar = new JButton("Enviar");
		JPanel loginBox = new JPanel();

		btn_enviar.addActionListener(this);

		loginBox.setLayout(new BoxLayout(loginBox, BoxLayout.Y_AXIS));
		loginBox.add(l_puerto);
		loginBox.add(l_ip);
		loginBox.add(txt_ip);
		loginBox.add(l_titulo);
		loginBox.add(btn_enviar);


		int x = 70,y = 70, b = 700,h = 130;
		loginBox.setBounds(x, y, b, h);
		loginBox.setBackground(colores.get(0));
		panelCentro.add(loginBox);
	}

	public void RequestServer () throws IOException
	{
		// TODO Auto-generated method stub
		ObjectOutputStream oos = null;
		ObjectInputStream ois = null;
		Socket s = null;

		try
		{
			s = new Socket(this.txt_ip.getText(), this.puerto);
			oos = new ObjectOutputStream(s.getOutputStream());
			ois = new ObjectInputStream(s.getInputStream());

			Client c = new Client();
			oos.writeObject(String.format("%s,%s,%s,%s,%s", c.cpu, c.ram, c.os, c.version, c.ip));
			//this.btn_enviar.setEnabled(false);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null,"Error: No hubo conexion, ¿Esta encendido el servidor?");
		}
		finally
		{
			if( ois != null ) ois.close();
			if( oos != null ) oos.close();
			if( s != null ) s.close();
		}
	}
	
	public void run () {
		try {
			// TODO Auto-generated method stub
			ObjectInputStream ois = null;
			ObjectOutputStream oos = null;
	
			Socket s = null;
			ServerSocket ss = new ServerSocket(this.puerto);
			while (this.run_me)
			{
				try
				{
					// el ServerSocket me da el Socket
					s = ss.accept();
					// informacion en la consola
					//System.out.println("Se conectaron desde la IP: " +s.getInetAddress());
					// enmascaro la entrada y salida de bytes
					ois = new ObjectInputStream( s.getInputStream() );
					oos = new ObjectOutputStream( s.getOutputStream() );
					// leo el nombre que envia el cliente
					String message = (String)ois.readObject();
                    Client c = new Client();
					String myIP = c.ip;
					if ( myIP.equals(message) ) {
						this.run_me = false;
						this.puerto++;
					} else {
						this.puerto++;
					}
				}
				catch (Exception ex)
				{
					ex.printStackTrace();
				} finally {
					if( oos !=null ) oos.close();
					if( ois !=null ) ois.close();
					if( s != null ) s.close();
				}
			}
			
            if( oos !=null ) oos.close();
            if( ois !=null ) ois.close();
            if( s != null ) s.close();

			Server server = new Server();
			server.finGUI();
			server.puerto++;
			this.dispose();
            try{
                server.run_me = true;
                Thread t1 = new Thread(server);
                t1.start();
            } catch(Exception ex){
                ex.printStackTrace();
            }
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	//JCGE: Este es el metodo que se encarga de tomar las acciones en los botones
	public void actionPerformed(ActionEvent arg0)
	{
		String boton = arg0.getActionCommand();
		if (boton == "Enviar")
		{
			try {
				this.RequestServer();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
