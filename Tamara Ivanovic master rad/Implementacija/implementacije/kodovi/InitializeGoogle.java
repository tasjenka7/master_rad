private final SpeechGrpc.SpeechStub mSpeechClient;

private void initializeRecognition() {
  ArrayList<String> languageList = new ArrayList<>();
  languageList.add("sr-RS");
  languageList.add("hr-HR");

  requestObserver = mSpeechClient.streamingRecognize(this);

  RecognitionConfig config =
    RecognitionConfig.newBuilder()
     .setEncoding(RecognitionConfig.AudioEncoding.LINEAR16)
     .setLanguageCode("en-US")
     .addAllAlternativeLanguageCodes(languageList)
     .setSampleRateHertz(mSamplingRate)
     .setModel("command_and_search")
     .build();

 StreamingRecognitionConfig streamingConfig =
    StreamingRecognitionConfig.newBuilder()
     .setConfig(config)
     .setInterimResults(true)
     .setSingleUtterance(true)
     .build();

 StreamingRecognizeRequest initial =
  StreamingRecognizeRequest.newBuilder().setStreamingConfig(streamingConfig).build();
 requestObserver.onNext(initial);
}