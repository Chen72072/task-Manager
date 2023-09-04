package taskmanager;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class Client extends JFrame implements Runnable{

	private Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream in;
    HashMap<Integer, Task> tasks;
    JPanel panel = new JPanel();
    private JFrame frame = new JFrame("TaskManager");

    
	public Client(String name) {
		connect(name);
		initFrame();
		showMe();
		Thread t = new Thread(this);
		t.start();
	}
	
	private void connect(String name) 
	{
		try {
			socket = new Socket("192.168.1.138", 5000);
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
			out.writeObject(name); //שם משתמש
			tasks = (HashMap<Integer, Task>)in.readObject();
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	public void initFrame() { //עיצוב מסך
	    this.setLayout(new BorderLayout());
	    this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				try {
					in.close();
					out.close();
					socket.close();
				} catch (IOException e1) {
					System.exit(1);
				}
				
				System.exit(0);
			}
	    	
	    });
	    panel = new JPanel();
	    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
	    panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

	    Label title = new Label("Task Manager");
	    title.setFont(new Font("Calibri", Font.BOLD, 18));
	    title.setAlignment(Label.CENTER);
	    panel.add(title);

	    JScrollPane scrollPane = new JScrollPane(panel);
	    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	    this.add(scrollPane, BorderLayout.CENTER);

	    for (Task t : tasks.values()) {
	        JPanel taskPanel = new JPanel();
	        taskPanel.setLayout(new BorderLayout());

	        JButton btn= null;
	        if (t.status == Status.available) {
	            btn = new JButton(t.getId() + "-" + t.getDescription());
	            btn.setBackground(Color.GREEN);
	            btn.addActionListener(e->{
					try {
						out.writeObject(e.getActionCommand().substring(0 , e.getActionCommand().indexOf("-")).trim());
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				});
	        } else if (t.status == Status.occupied) {
	            btn = new JButton(t.getId() + "-" + t.getDescription() + "-" + t.getByUser());
	            btn.setBackground(Color.RED);
	            btn.setEnabled(false);
	        } else if (t.status == Status.accomplished) {
	            btn = new JButton(t.getId() + "-" + t.getDescription() + "-" + t.getByUser());
	            btn.setBackground(Color.YELLOW);
	        } 
	        
	        btn.setHorizontalAlignment(SwingConstants.LEFT);
	        panel.add(btn);
	    }
	    //exit
	    this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    socket.close();
                    in.close();
                    out.close();
                } catch (IOException e1) {
                    System.exit(0);
                }
                System.exit(0);
            }
        });
	    
	}
	
	public void showMe() //הצגת מסך
	{
		this.setVisible(true);
		this.setSize(500, 500);
	}
	
	
	public void run() {
		while (true) {
			try {
				Task task = (Task)in.readObject();
				String name = (String)in.readObject();
				System.out.println(name);
				for (Component component : panel.getComponents()) {
					if (component instanceof JButton) {
						JButton button = (JButton) component;
						System.out.println("Button Text: " + button.getText());
						System.out.println("Task ID: " + task.getId());
						if (button.getText().substring(0,button.getText().indexOf("-")).trim().equals(task.getId()+"")) {
							System.out.println(button.getText().substring(0,button.getText().indexOf("-")).trim());
							button.setBackground(Color.RED);
							button.setText(task.getId() + "-" + task.getDescription() + "-" + name);
							button.setEnabled(false);
						}
							
					}
				}

			} catch (IOException | ClassNotFoundException e) {
				System.exit(1);
			}
		}

	}


}
