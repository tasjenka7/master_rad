public class UdpClient {

  private DatagramSocket mSocket;

  public void disconnect(){
   //Zatvaranje mSocket-a ukoliko postoji
  }

 public void connect(final InetAddress address, final int port, final AsyncReceiver asyncDataReceiver){
   disconnect();

    new Thread(() -> {
     try {
      mSocket = new DatagramSocket(0);
    } catch (SocketException e) {
      e.printStackTrace();
    }
    mSocket.connect(address, port);

    if (mSocket.isConnected()) {
     asyncDataReceiver.onReceive();
    } else {
     asyncDataReceiver.onFailed("Socket not connected");
    }
    }).start();
  }

  //Funkcija koja salje komande na uredjaj	
  public void send(final String message) {
  //...
  }

  public void startListening() {
   stopListening();

   mThread = new Thread(() -> {
    while (!Thread.currentThread().isInterrupted()) {

    byte[] buf = new byte[MAX_UDP_DATAGRAM_LEN];
    final DatagramPacket pack = new DatagramPacket(buf, buf.length);

    if (mSocket != null) {
     if (mSocket.isConnected()) {
       try {
         // Primanje poruke od uredjaja
         mSocket.receive(pack);
        String msg = new String(pack.getData(), pack.getOffset(), pack.getLength());
        msg = msg.trim();
        Singleton.getInstance().getCommandsHandler().onServerCommandReceived(msg);
       } catch (IOException e) {
          e.printStackTrace();
       }
    }
  }
    //...
  });
  mThread.start();
 }
 public void stopListening() {
        //Prekid rada niti
    }
}