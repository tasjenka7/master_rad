private class ConnectToStb extends AsyncTask {
 @Override
 protected Object doInBackground(Object[] objects) {
  //Pozicija u listi pronadjenih uredjaja
  int position = (int) objects[0];
  final UdpClient client = new UdpClient();
  final InetAddress inetAddress = items.get(position).getHost();
  client.connect(inetAddress, items.get(position).getPort(), new AsyncReceiver() {
   @Override
   public void onReceive() {
    //Cuvanje instance klijenta sa kojim se povezujemo
    Singleton.getInstance().getCommandsHandler().setClient(client);
    //Ukoliko nemamo sacuvanu konekciju sa datim uredjajem
    if (!isServerInSharedPrefs(inetAddress.getHostAddress())) {
      //Slanje komande za povezivanje
      Singleton.getInstance().getCommandsHandler().getClient()
               .send(PAIR_COMMAND + "@" + Singleton.getInstance().getCommandsHandler()
              .getRandomNumber());
      //Potrebno je otvoriti dijalog za unos i obradu koda
    } else {
      //Ukoliko je sacuvana konekcija sa datim klijentom dovoljno
      //je poslati kod uredjaju da je aplikacija povezana sa njim
      //i pozvati funkciju koja menja trenutnu aktivnost za 
      //aktivnost sa planom daljinskog upravljaca
    }
    //UDP klijent osluskuje poruke koje se salju
    client.startListening();
   }
  // ...
}