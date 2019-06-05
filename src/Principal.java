import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import static java.util.Comparator.comparing;

import java.awt.event.ActionEvent;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class Principal extends MainWindow
{
	ObjectInputStream ois = null;
	ObjectOutputStream oos = null;
	Socket s = null;
	ServerSocket ss = null;

	Principal () {
		MyLabel l_titulo = new MyLabel("¿Mi computadora es?");
		JPanel loginBox = new JPanel();

		JButton btn_host = new JButton("Host");
		JButton btn_server = new JButton("Server");

		btn_host.addActionListener(this);
		btn_server.addActionListener(this);

		loginBox.setLayout(new BoxLayout(loginBox, BoxLayout.Y_AXIS));
		loginBox.add(l_titulo);
		loginBox.add(btn_host);
		loginBox.add(btn_server);

		int x = 70,y = 70, b = 700,h = 300;
		loginBox.setBounds(x, y, b, h+20);
		loginBox.setBackground(colores.get(0));
		panelCentro.add(loginBox);
	}

	public static void main(String[] args) {

		Principal p = new Principal();
		p.finGUI();
	}

	public void actionPerformed(ActionEvent arg0)
	{
		String boton = arg0.getActionCommand();
		if (boton == "Host")
		{
			Host h = new Host();
			h.finGUI();
			this.dispose();
            try{
                h.run_me = true;
                Thread t1 = new Thread(h);
                t1.start();
            } catch(Exception ex){
                ex.printStackTrace();
            }
			return;
		}

		if (boton == "Server")
		{
			Server s = new Server();
			s.finGUI();
			this.dispose();
            try{
                s.run_me = true;
                Thread t1 = new Thread(s);
                t1.start();
            } catch(Exception ex){
                ex.printStackTrace();
            }
			return;
		}
	}
}
