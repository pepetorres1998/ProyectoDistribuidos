import java.awt.Font;

import javax.swing.JLabel;

public class MyLabel extends JLabel
{
	private static final long serialVersionUID = 7966440493165944703L;
	protected Font font = new Font("Ubuntu Mono", Font.BOLD, 20);
	MyLabel(String text)
	{
		this.setText(text);
		this.setFont(font);
	}
}
