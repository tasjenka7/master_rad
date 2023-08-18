public void getStbList(final AsyncDataReceiver receiver){
        //Potrebno je obezbediti da pretraga nije u toku 
        nsdDiscover.stopDiscovery();

        nsdDiscover = new NsdDiscover(Singleton.getContext()){
            public void onDiscover(NsdServiceInfo service) {
               //Sva logika vezana za pronadjene uredjaje 
               //se smesta unutar ove funkcije:
               //kreiranja nove instance stb uredjaja,
               //dodavanje uredjaja u listu, 
               //provera da vec nije isti uredjaj u listi...
            }  
        };
        nsdDiscover.startDiscovery();
}