package view.container;

import javafx.beans.property.ObjectProperty;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import view.Caret;

public class TextGrid extends Grid {
    protected Text character;
    private Button leftBtn = new Button();
    private Button rightBtn = new Button();
    private HBox hover = new HBox();

    public TextGrid(Text n) {
        this.character = n;
        this.character.getStyleClass().add("character");
        double width = n.getBoundsInLocal().getWidth();
        double height = n.getBoundsInLocal().getHeight();
        this.setUpLayout(width, height);
        this.setButtonHandler();
        this.hover.getChildren().addAll(new Node[]{this.leftBtn, this.rightBtn});
        this.getChildren().addAll(new Node[]{n, this.hover});
    }

    public void setUpLayout(double width, double height) {
        this.getStylesheets().add("/css/javaFX/content-grid.css");
        this.leftBtn.setPrefSize(width / 2.0D, height);
        this.rightBtn.setPrefSize(width / 2.0D, height);
        this.hover.setPrefSize(width, height);
        this.setMinSize(0.0D, 0.0D);
        this.setPrefSize(width, height);
    }

    void setButtonHandler() {
        this.leftBtn.setOnMousePressed(this::leftBtnHandler);
        this.rightBtn.setOnMousePressed(this::rightBtnHandler);
    }

    private void leftBtnHandler(MouseEvent mouseEvent) {
        TextLine parent = (TextLine)this.getParent();
        ((Caret)parent.getChildren().get(parent.getRealPosition(this) - 1)).show();
        parent.requestFocus();
    }

    private void rightBtnHandler(MouseEvent mouseEvent) {
        TextLine parent = (TextLine)this.getParent();
        ((Caret)parent.getChildren().get(parent.getRealPosition(this) + 1)).show();
        parent.requestFocus();
    }

    public Text getCharacter() {
        return this.character;
    }

    public String toString() {
        return this.character instanceof Text ? this.character.getText().toString() : this.character.toString();
    }

    public ObjectProperty<Paint> fillProperty() {
        return this.character.fillProperty();
    }
}
