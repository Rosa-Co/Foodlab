package progetto.app.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class TypeWritingController {
    private final Label label;
    private final String fullText;
    private final int speedMillis;
    private int currentIndex = 0;
    public TypeWritingController(Label label, String fullText, int speedMillis){
        this.label = label;
        this.fullText = fullText;
        this.speedMillis = speedMillis;
    }

    public void play(){
        label.setText("");
        label.setVisible(true);
        currentIndex = 0;

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(speedMillis),event->{
            if(currentIndex<fullText.length()){
                label.setText(label.getText() + fullText.charAt(currentIndex));
                currentIndex++;
            }
        }));
        timeline.setCycleCount(fullText.length()); //ripete animation per quanti sono i caratteri
        timeline.play();
    }
}
