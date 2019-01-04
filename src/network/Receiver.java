package network;



import java.net.Socket;

/**
 *
 * @author pseudo
 */
public abstract class Receiver implements Runnable {

    /**
     *
     */
    public Connection connection;

    /**
     *
     */
    public boolean stop;

    /**
     *
     * @param socket
     */
    public Receiver(Socket socket)
    {
        stop = false;
        connection = new Connection();
        connection.openConnection(socket);
    }
    
    /**
     *
     * @param connection
     */
    public Receiver(Connection connection)
    {
        this.connection = connection;
    }

    /**
     *
     */
    @Override
    public void run() {
        while (true) {
            if (stop){break;}
            Message message = connection.receive();
            if (message == null){break;}
            handleRequest(message);
        }
    }

    /**
     *
     * @param message
     */
    public abstract void handleRequest(Message message);
}
