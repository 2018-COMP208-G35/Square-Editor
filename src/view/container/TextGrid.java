package view.container;

import javafx.beans.property.ObjectProperty;
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

    public TextGrid(Text n)
    {
        character = n;
        character.getStyleClass().add("character");
        // get width and height of character n
        double width = n.getBoundsInLocal().getWidth();
        double height = n.getBoundsInLocal().getHeight();
        // set width and height of this according to this character
        setUpLayout(width, height);
        setButtonHandler();
        hover.getChildren().addAll(leftBtn, rightBtn);
        this.getChildren().addAll(n, hover);
    }

    public void setUpLayout(double width, double height)
    {
        getStylesheets().add("/css/javaFX/content-grid.css");
        // Set layout of buttons
        leftBtn.setPrefSize(width / 2, height);
        rightBtn.setPrefSize(width / 2, height);
        // Set layout of hover
        hover.setPrefSize(width, height);
        this.setMinSize(0, 0);
        // Set layout of this
        this.setPrefSize(width, height);
    }

    void setButtonHandler()
    {
        leftBtn.setOnMousePressed(this::leftBtnHandler);
        rightBtn.setOnMousePressed(this::rightBtnHandler);
    }

    private void leftBtnHandler(MouseEvent mouseEvent)
    {
        TextLine parent = (TextLine) this.getParent();
        ((Caret) parent.getChildren().get(parent.getRealPosition(this) - 1)).show();
        parent.requestFocus();
    }

    private void rightBtnHandler(MouseEvent mouseEvent)
    {
        TextLine parent = (TextLine) this.getParent();
        ((Caret) parent.getChildren().get(parent.getRealPosition(this) + 1)).show();
        parent.requestFocus();
    }

    public Node getCharacter()
    {
        return character;
    }

    @Override
    public String toString()
    {
        if (character instanceof Text)
        {
            return ((Text) character).getText().toString();
        }
        else
        {
            return character.toString();
        }
    }

    public ObjectProperty<Paint> fillProperty()
    {
        return character.fillProperty();
    }

}
