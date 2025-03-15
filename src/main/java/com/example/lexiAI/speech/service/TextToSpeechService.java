package com.example.lexiAI.speech.service;



import com.microsoft.cognitiveservices.speech.*;
import com.microsoft.cognitiveservices.speech.audio.AudioConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class TextToSpeechService {

    @Value("${azure.speech.api-key}")
    private String speechKey;

    @Value("${azure.speech.region}")
    private String region;

    public ByteArrayResource synthesizeSpeech(String text) throws Exception {
        SpeechConfig speechConfig = SpeechConfig.fromSubscription(speechKey, region);
        speechConfig.setSpeechSynthesisVoiceName("en-US-JennyNeural"); // Select a voice

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            // Use an AudioConfig with stream output
            AudioConfig audioConfig = AudioConfig.fromDefaultSpeakerOutput();
            SpeechSynthesizer synthesizer = new SpeechSynthesizer(speechConfig, audioConfig);

            SpeechSynthesisResult result = synthesizer.SpeakText(text);

            if (result.getReason() == ResultReason.SynthesizingAudioCompleted) {
                outputStream.write(result.getAudioData()); // Write synthesized audio data
                return new ByteArrayResource(outputStream.toByteArray()); // Return as ByteArrayResource
            } else {
                throw new RuntimeException("Speech synthesis failed: " + result.getReason());
            }
        }
    }
}
