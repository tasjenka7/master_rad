 NsdManager.DiscoveryListener listener = new NsdManager.DiscoveryListener() {

    // Poziva se cim se zapocne pretraga i dohvataju se informacije o lokalnoj mrezi 
    public void onDiscoveryStarted(String regType) {
        WifiManager wifiMgr = (WifiManager)              mContext.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
        String ipAddress = wifiInfo.toString();
    }

    //Definisu se akcije koje se izvrsavaju kada se pronade zadati servis 
    public void onServiceFound(NsdServiceInfo service) {
      String type = service.getServiceType();
      //Ako se tip servisa podudara sa servisom koji se trazi, kao i sa imenom poziva se metod. Potrebno je obraditi i slucajeve ako se ne poklapaju.
      mNsdManager.resolveService(service, initializeResolveListener());
   }
    // Kada servis na mrezi vise nije dostupan poziva se ova metoda.
    public void onServiceLost(NsdServiceInfo service) {}

    // Kada se zaustavi pretraga poziva se ova metoda.
    public void onDiscoveryStopped(String serviceType) {}

    // Ukoliko nakon nekog vremena nije bilo moguce da se pokrene otkrivanje ovaj metod ce biti pozvan.
    public void onStartDiscoveryFailed(String serviceType, int errorCode) {
      mNsdManager.stopServiceDiscovery(this);
    }

    // Ukoliko nakon nekog vremena nije bilo moguce da se zaustavi otkrivanje ovaj metod ce biti pozvan.
    public void onStopDiscoveryFailed(String serviceType, int errorCode) {
      mNsdManager.stopServiceDiscovery(this);
    }
}

 NsdManager.ResolveListener listener = new NsdManager.ResolveListener() {
   //Ukoliko je razresavanje bilo neuspesno
  public void onResolveFailed(NsdServiceInfo nsdServiceInfo, int errorCode) {}

  //Ukoliko je razresavanje bilo uspesno
  public void onServiceResolved(NsdServiceInfo nsdServiceInfo) {}
}