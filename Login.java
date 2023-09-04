package taskmanager;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Login extends JFrame {
	
	JFrame frame = new  JFrame();
	
	public Login() {
		initFrame();
		showMe();
	}
	
	public static void main(String[] args) {
		Login login = new Login();
	}
	
	public void initFrame()
	{
		 frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 this.setLayout(new BorderLayout());
		JLabel lbl = new JLabel();
		JTextField txtName = new JTextField();
		JButton btn = new JButton();
		this.setLayout(new BorderLayout());
        JPanel panel = new JPanel();
        this.add(panel);
        lbl.setText("name");
        txtName.setPreferredSize(new Dimension(300, 30));
        btn.setText("connect");
        lbl.setFont(new Font("Arial", 2, 15));
        txtName.setFont(new Font("Arial", 2, 15));
        btn.setFont(new Font("Arial", 2, 15));
        panel.add(btn);
        panel.add(txtName);
        panel.add(lbl);
        btn.addActionListener(e->{
			Client client = new Client(txtName.getText());
		});
	}
	
	public void showMe()
	{
		this.setVisible(true);
		this.setSize(500, 500);
	}
}
