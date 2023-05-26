public void send(final String message) {
  new Thread(() -> {
   byte[] buf = message.getBytes();
   DatagramPacket p = new DatagramPacket(buf, buf.length);

   try {
    if (mSocket.isConnected()) {
       mSocket.send(p);
       Log.d(TAG, "[send] sending data to stb - mSocket is connected");
    } else {
       Log.d(TAG, "[send] sending data to stb - mSOCKET not connected");
    }
   } catch (IOException e) {
       Log.e(TAG, "[send] " + e.getMessage());
       e.printStackTrace();
    }
   }).start();
}