package taskmanager;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server implements Runnable {

	private HashMap<Integer, Task> tasks; // רשימת המשימות
	private List<ConnectedClient> clientList = new ArrayList<>(); // רשימת הקליינטים המחוברים
	private ServerSocket serverSocket; // אובייקט סרבר

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Server server = new Server();
	}

	public Server() {
		// TODO Auto-generated constructor stub
		tasks = new HashMap<>();
		tasks.put(1, new Task(1, "להכין עוגה", new Date(), Status.available, null));
		tasks.put(2, new Task(2, "לשטוף כלים", new Date(), Status.available, null));
		tasks.put(3, new Task(3, "לשטוף רצפה", new Date(), Status.available, null));
		tasks.put(4, new Task(4, "לנקות חלונות", new Date(), Status.available, null));
		tasks.put(5, new Task(5, "קניה שבועית", new Date(), Status.available, null));
		tasks.put(6, new Task(6, "לקבוע תור לשיננית", new Date(), Status.available, null));
		tasks.put(7, new Task(7, "לקנות מתנה ליום הולדת של חני", new Date(), Status.available, null));
		tasks.put(8, new Task(8, "לגמור פרויקט", new Date(), Status.available, null));

		try {
			serverSocket = new ServerSocket(5000);
			Thread t = new Thread(this);
			t.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void writeAll(Task data) {
		for (ConnectedClient client : clientList) {
			try {

				client.out.writeObject(data);
				client.out.writeObject(data.getByUser());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			try {
				// כשיש התחברות חדשה יוצרים אובייקט קונקטד כליינט
				ConnectedClient client = new ConnectedClient(serverSocket.accept());
				// הוספה לרשימת הקליינטים
				clientList.add(client);
				// הפעלת סרד לכתיבה וקריאה תמיד של הקליינט הנוכחי
				Thread t = new Thread(client);
				t.start();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	class ConnectedClient implements Runnable {

		ObjectInputStream in;
		ObjectOutputStream out;
		String name;

		public ConnectedClient(Socket socket) {
			// TODO Auto-generated constructor stub
			try {
				in = new ObjectInputStream(socket.getInputStream());
				out = new ObjectOutputStream(socket.getOutputStream());
				name = (String) in.readObject();
				// שליחת מערך המשימות
				out.writeObject(tasks);
				System.out.println("client connected");
			} catch (IOException | ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (true) {
				try {
					// כשמישהו תפס משימה הוא שולח לי את מספר המשימה
					int idTask = Integer.parseInt((String) in.readObject());
					// קטע קריטי
					synchronized (tasks) {
						// קבלת פרטי המשימה
						Task task = tasks.get(idTask);
						if (task.getByUser() == null) {// עדכון המשימה
							task.setByUser(name);
							task.setStatus(Status.occupied);
							tasks.put(idTask, task);
							// שליחה המשימה המעודכנת לכולם
							writeAll(task);
						}
					}
				} catch (IOException | ClassNotFoundException e) {
					// this.socket.close();
					try {
						this.in.close();
						this.out.close();
						clientList.remove(this);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		}
	}
}
