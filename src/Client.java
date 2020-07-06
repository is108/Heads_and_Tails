import java.net.*;
import java.io.*;

class Client  {

	private ObjectInputStream sockInput;
	private ObjectOutputStream sockOutput;
	private Socket socket;
	private String server;
	private int port;

	protected Message request_msg; //ответ от сервера
	protected double balance; //баланс, полученный от сервера


	Client(String server, int port) {
		this.server = server;
		this.port = port;
		this.balance = 0;

		request_msg = new Message();
	}
	
	//Запуск клиента
	public boolean start() {
		try {
			socket = new Socket(server, port);
		} 
		catch(Exception Ex) {
			System.out.println("Error connectiong to server:" + Ex);
			return false;
		}
		System.out.println("Successful game connection\n");

		// Создание двух потоков данных
		try
		{
			sockInput  = new ObjectInputStream(socket.getInputStream());
			sockOutput = new ObjectOutputStream(socket.getOutputStream());
		}
		catch (IOException Ex) {
			System.out.println("Exception creating new input/output streams: " + Ex);
			return false;
		}

		// Создание потока, для прослушивания с сервера
		new ListenFromServer().start();

		//Отправка серверу сообщения, что клиент был создан
		try
		{
			sockOutput.writeObject("create player");
		}
		catch (IOException Ex) {
			System.out.println("Exception create player: " + Ex);
			disconnect();
			return false;
		}

		return true;
	}

	//Отправка сообщения на сервер
	void sendMessage(Message msg) {
		try {
			sockOutput.writeObject(msg);
		}
		catch(IOException Ex) {
			System.out.println("Exception writing to server: " + Ex);
		}
	}

	//Закрытие сокетов
	public void disconnect() {
		try { 
			if(sockInput != null)
				sockInput.close();
		}
		catch(Exception Ex) {}

		try {
			if(sockOutput != null)
				sockOutput.close();
		}
		catch(Exception Ex) {}

        try{
			if(socket != null)
				socket.close();
		}
		catch(Exception Ex) {}
			
	}

	//Получение данных от сервера в отдельном потоке
	class ListenFromServer extends Thread {

		public void run() {

			//Получение нашего баланса от сервера
			try {
				balance = (Double) sockInput.readObject();
			}
			catch (IOException Ex) {
				System.out.println("Exception get balance: " + Ex);
			}
			catch (ClassNotFoundException Ex) {}

			//Цикл получения сообщений от сервера
			while(true) {
				try {
					//Получение сообщения от сервера
					request_msg = (Message) sockInput.readObject();
				}
				catch(IOException Ex) {
					System.out.println("Server has closed the connection: " + Ex);
					break;
					//System.exit();
				}
				catch(ClassNotFoundException Ex) {}
			}
		}
	}
}
