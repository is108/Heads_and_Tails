import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;


class Server extends Casino{
    private static int client_id; //Уникальный id каждого нового клиента
    private ArrayList<ClientThread> clients; // Список всех клиентов
    private SimpleDateFormat current_time;
    private int port; // Порт
    private boolean isStart; //Флаг сообщающий что сервер работает

    Server(int port) {
        this.port = port;
        current_time = new SimpleDateFormat("HH:mm:ss");
        clients = new ArrayList<ClientThread>();
    }


    public void start() {
        isStart = true;
        //Создание сокета сервера и ожидание запросов на подключение игроков
        try
        {
            ServerSocket serverSocket = new ServerSocket(port);

            System.out.println("Server is working! Port to connect: " + port + ".");

            while (isStart)
            {
                Socket socket = serverSocket.accept();

                if (!isStart)
                    break;

                //Создание нового потока для нового игрока
                ClientThread client = new ClientThread(socket);

                clients.add(client);
                client.start();
            }
            // Выключение сервера
            try {
                serverSocket.close();
                for(int i = 0; i < clients.size(); ++i) {
                    //Получение потока игрока
                    ClientThread client_thread = clients.get(i);

                    try {
                        //Закрытие сокета и потоков данных
                        client_thread.sockInput.close();
                        client_thread.sockOutput.close();
                        client_thread.socket.close();
                    }
                    catch(IOException Ex) {
                        System.out.println("Error close socket or data streams: " + Ex);
                    }
                }
            }
            catch(Exception Ex) {
                System.out.println("Exception closing the server or clients: " + Ex);
            }
        }
        catch (IOException Ex) {
            System.out.println(current_time.format(new Date()) + " Exception on new ServerSocket: " + Ex);
        }
    }


    protected void stop() {
        isStart = false;
    }


    //Отправка сообщения клиенту
    private synchronized void broadcast(Message message, int id) {
        String time = current_time.format(new Date());

        System.out.println(time + " Сhoice player " + id + ": " + message.getChoice() + ". Bet player - " + message.getBet());

        //Подсчет того выиграл игрок или проиграл
        if (calcVictory(message.getChoice())) {
            message.setWin(true);
            System.out.println(time + " Player " + id + " win " + message.getBet());
        }
        else {
            message.setWin(false);
            System.out.println(time + " Player " + id + " lose " + message.getBet());
        }

        //Поиск клиента, который отправил запрос и выдача ему результата
        for(int i = 0; i < clients.size(); i++) {
            ClientThread current_client = clients.get(i);
            if (current_client.id == id) {
                current_client.writeMsg(message);
                break;
            }
        }
    }


    // Удаление клиента
    synchronized void remove(int id) {
        for(int i = 0; i < clients.size(); i++) {
            ClientThread client = clients.get(i);
            // Если клиент найден, удалить
            if(client.id == id) {
                clients.remove(i);
                break;
            }
        }
        System.out.println("Player " + id + " delete.");
    }

    //отвечает за поток для каждого клиента
    class ClientThread extends Thread {
        Socket socket;
        ObjectInputStream sockInput;
        ObjectOutputStream sockOutput;
        int id; // Уникальный id клиента
        Message msg; //Сообщение с запросом от клиента
        String date; // Отметка времени


        ClientThread(Socket socket) {
            id = ++client_id;
            this.socket = socket;

            msg = new Message();

            //Создание потоков данных
            try
            {
                sockOutput = new ObjectOutputStream(socket.getOutputStream());
                sockInput  = new ObjectInputStream(socket.getInputStream());

                //Получение информации о создании клиента
                String create = (String) sockInput.readObject();

                //Выдать игроку баланс 10000 монет
                if (create.equalsIgnoreCase("create player")) {
                    try {
                        sockOutput.writeObject(10000.0);
                    }
                    catch (IOException Ex) {
                        System.out.println("Error set balance to player " + id + ". " + Ex);
                    }
                }

                System.out.println("Player " + id + " connected to game.");
            }
            catch (IOException Ex) {
                System.out.println("Exception creating new input/output streams: " + Ex);
                return;
            }
            catch (ClassNotFoundException Ex) {
            }

            date = new Date().toString() + "\n";
        }


        // Цикл для чтения выбора игрока и отправки ему результата
        public void run() {
            boolean isStart = true;
            while(isStart) {

                //Получение запроса от игрока
                try {
                    msg = (Message) sockInput.readObject();
                }
                catch (IOException Ex) {
                    System.out.println("Player " + id + " exception reading streams: " + Ex);
                    break;
                }
                catch (ClassNotFoundException Ex) {
                    break;
                }

                // Отправление сообщения в обработку
                broadcast(msg, id);

            }

            //Если мы выходим из цикла, то удаляем игрока из списка клиентов и закрываем соединение
            remove(id);
            close();
        }

        //Закрытие всех сокетов
        private void close() {

            try {
                if(sockOutput != null)
                    sockOutput.close();
            }
            catch(Exception Ex) {}

            try {
                if(sockInput != null)
                    sockInput.close();
            }
            catch(Exception Ex) {};

            try {
                if(socket != null)
                    socket.close();
            }
            catch (Exception Ex) {}
        }


        // Записать результат в поток вывода клиента
        private boolean writeMsg(Message msg) {
            //Проверяем что сокет подключен
            if(!socket.isConnected()) {
                close();
                return false;
            }

            //Записываем ответ в поток
            try {
                sockOutput.writeObject(msg);
            }
            catch(IOException Ex) {
                System.out.println("Error sending message to " + id + ". " + Ex);
            }

            return true;
        }
    }
}

