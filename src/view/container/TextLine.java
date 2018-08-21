package view.container;

import java.util.Iterator;
import javafx.beans.property.ObjectProperty;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import view.Caret;
import view.helper.FontMetricsHelper;

public class TextLine extends HBox {
    private TextLine previousLine = null;
    private TextLine nextLine = null;
    private Caret leftEdgeCaret;

    public TextLine(Pos pos, String fontFamily, double fontSize) {
        this.leftEdgeCaret = new Caret(FontMetricsHelper.getFontHeight(fontFamily, fontSize));
        this.getChildren().add(this.leftEdgeCaret);
        this.setUpHandler();
        this.setUpLayout(pos);
    }

    public TextLine(Pos pos, String fontFamily, double fontSize, ObjectProperty<Paint> fill) {
        this.leftEdgeCaret = new Caret(FontMetricsHelper.getFontHeight(fontFamily, fontSize));
        this.getChildren().add(this.leftEdgeCaret);
        this.setUpHandler();
        this.setUpLayout(pos);
    }

    private void setUpHandler() {
        this.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                TextLine.this.moveCaretTo(TextLine.this.getNumberOfCaret() - 1);
                Section section = (Section)TextLine.this.getParent();
            }
        });
    }

    protected void setUpLayout(Pos pos) {
        this.getStyleClass().add("/css/caret-line.css");
        this.setMinSize(0.0D, 0.0D);
        this.getStyleClass().add("text-line");
        this.setAlignment(pos);
    }

    public Caret addAfterCaret(Text n) {
        double width = n.getBoundsInLocal().getWidth();
        double height = n.getBoundsInLocal().getHeight();
        TextGrid gap = new TextGrid(n);
        gap.setPrefSize(width - Caret.getCaretWidth(), height);
        gap.setMinSize(0.0D, 0.0D);
        Caret newCaret = new Caret(height);
        int i = this.insertPosition();
        if (this.getCaretPosition() == this.getNumberOfCaret() - 1) {
            this.getChildren().add(newCaret);
        } else {
            this.getChildren().add(i, newCaret);
        }

        this.getChildren().add(i, gap);
        newCaret.show();
        return newCaret;
    }

    private int insertPosition() {
        return 1 + this.getCaretPosition() * 2;
    }

    public void removeBackwardFromCaret() {
        int index = this.getCaretPosition();
        if (index != 0) {
            this.moveCaretTo(index - 1);
            this.getChildren().removeAll(new Node[]{this.getGapBeforeCaret(index), this.getCaret(index)});
        }

    }

    public Caret getCaret(int position) {
        if (position >= 0 && position <= this.getNumberOfCaret() - 1) {
            int i = 0;
            Iterator var3 = this.getChildren().iterator();

            while(var3.hasNext()) {
                Node n = (Node)var3.next();
                if (n instanceof Caret) {
                    if (i == position) {
                        return (Caret)n;
                    }

                    ++i;
                }
            }

            throw new IllegalStateException("Caret not in this line");
        } else {
            throw new IndexOutOfBoundsException("This line doesn't have caret at " + position);
        }
    }

    public Pane getGapBeforeCaret(int position) {
        if (position >= 0 && position <= this.getNumberOfCaret() - 1) {
            int i = 0;
            Pane pane = null;
            Iterator var4 = this.getChildren().iterator();

            while(var4.hasNext()) {
                Node n = (Node)var4.next();
                if (n instanceof Pane) {
                    pane = (Pane)n;
                }

                if (n instanceof Caret) {
                    if (i == position) {
                        return pane;
                    }

                    ++i;
                }
            }

            throw new IllegalStateException("Caret not in this line");
        } else {
            throw new IndexOutOfBoundsException("This line doesn't have caret at " + position);
        }
    }

    public int getCaretPosition() {
        int position = 0;
        Iterator var2 = this.getChildren().iterator();

        while(var2.hasNext()) {
            Node n = (Node)var2.next();
            if (n instanceof Caret) {
                if (((Caret)n).isOn()) {
                    return position;
                }

                ++position;
            }
        }

        throw new IllegalStateException("Caret not in this line");
    }

    public int getRealPosition(Node node) {
        int position = 0;

        for(Iterator var3 = this.getChildren().iterator(); var3.hasNext(); ++position) {
            Node n = (Node)var3.next();
            if (n == node) {
                return position;
            }
        }

        throw new IllegalStateException("Node " + node + " not in this line: " + this.toString());
    }

    public boolean moveCaretTo(int position) {
        if (position >= 0 && position <= this.getNumberOfCaret() - 1) {
            int i = 0;
            Iterator var3 = this.getChildren().iterator();

            while(var3.hasNext()) {
                Node n = (Node)var3.next();
                if (n instanceof Caret) {
                    if (i == position) {
                        ((Caret)n).show();
                        return true;
                    }

                    ++i;
                }
            }

            throw new IndexOutOfBoundsException("Unknown Error");
        } else {
            throw new IndexOutOfBoundsException("This line doesn't have " + position + " characters");
        }
    }

    public int getNumberOfCaret() {
        return (this.getChildren().size() - 1) / 2 + 1;
    }

    public void moveCaretRight() {
        System.out.println("Move caret right");
        int currentPosition = this.getCaretPosition();
        System.out.println("Current position: " + currentPosition);
        System.out.println("Move on to: " + (currentPosition + 1));
        if (this.getNumberOfCaret() - 1 != currentPosition) {
            this.moveCaretTo(currentPosition + 1);
        }

    }

    public void moveCaretLeft() {
        System.out.println("Move caret left");
        int currentPosition = this.getCaretPosition();
        System.out.println("Current position: " + currentPosition);
        System.out.println("Move on to: " + (currentPosition - 1));
        if (currentPosition != 0) {
            this.moveCaretTo(currentPosition - 1);
        }

    }

    public double getLineWidth() {
        double width = 0.0D;
        Iterator var3 = this.getChildren().iterator();

        while(var3.hasNext()) {
            Node n = (Node)var3.next();
            if (n instanceof TextGrid) {
                width += n.getBoundsInLocal().getWidth();
            }
        }

        return width;
    }

    public boolean hasWhiteSpaceInBetween() {
        boolean ready = false;
        Iterator var2 = this.getChildren().iterator();

        while(var2.hasNext()) {
            Node n = (Node)var2.next();
            if (n instanceof TextGrid) {
                if (((TextGrid)n).getCharacter().getText().equals(" ")) {
                    ready = true;
                } else if (ready) {
                    return true;
                }
            }
        }

        return false;
    }

    public TextLine getPreviousLine() {
        return this.previousLine;
    }

    public void setPreviousLine(TextLine previousLine) {
        this.previousLine = previousLine;
    }

    public TextLine getNextLine() {
        return this.nextLine;
    }

    public void setNextLine(TextLine nextLine) {
        this.nextLine = nextLine;
    }

    public void showLeftEdgeCaret() {
        this.leftEdgeCaret.show();
    }

    Word getWords() {
        Word word = new Word();
        Iterator var2 = this.getChildren().iterator();

        while(var2.hasNext()) {
            Node node = (Node)var2.next();
            if (node instanceof TextGrid) {
                word.add(((TextGrid)node).getCharacter());
            }
        }

        return word;
    }

    Word getWordsAfterCaret() {
        Word word = new Word();

        for(int i = this.getRealPosition(this.getCaret(this.getCaretPosition())); i < this.getChildren().size(); ++i) {
            Node n = (Node)this.getChildren().get(i);
            if (n instanceof TextGrid) {
                word.add(((TextGrid)n).getCharacter());
            }
        }

        return word;
    }

    Word getContentNodes() {
        Word word = new Word();

        for(int i = 1; i < this.getChildren().size(); ++i) {
            word.add(this.getChildren().get(i));
        }

        return word;
    }

    Word getContentNodesAfterCaret() {
        Word word = new Word();

        for(int i = this.getRealPosition(this.getCaret(this.getCaretPosition())) + 1; i < this.getChildren().size(); ++i) {
            word.add(this.getChildren().get(i));
        }

        return word;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        Iterator var2 = this.getChildren().iterator();

        while(var2.hasNext()) {
            Node n = (Node)var2.next();
            if (n instanceof TextGrid) {
                stringBuilder.append(((TextGrid)n).getCharacter().getText());
            }
        }

        return stringBuilder.toString();
    }
}
