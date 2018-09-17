package network;



import java.net.Socket;

public abstract class Receiver implements Runnable {
    public Connection connection;
    public boolean stop;

    public Receiver(Socket socket)
    {
        stop = false;
        connection = new Connection();
        connection.openConnection(socket);
    }
    
    public Receiver(Connection connection)
    {
        this.connection = connection;
    }


    @Override
    public void run() {
        while (true) {
            if (stop){break;}
            Message message = connection.receive();
            if (message == null){break;}
            handleRequest(message);
        }
    }

    public abstract void handleRequest(Message message);
}
