package view.container;

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

/**
 * The type Text line.
 */
public class TextLine extends HBox {

    private TextLine previousLine = null;
    private TextLine nextLine = null;


    /**
     * The Left edge caret.
     */
    private Caret leftEdgeCaret;

    /**
     * Instantiates a new Text line.
     */
    public TextLine(Pos pos, String fontFamily, double fontSize)
    {

        // get width and height of character n
        leftEdgeCaret = new Caret(FontMetricsHelper.getInstance().getFontHeight(fontFamily, fontSize));
        this.getChildren().add(leftEdgeCaret);
        setUpHandler();
        setUpLayout(pos);
    }

    public TextLine(Pos pos, String fontFamily, double fontSize, ObjectProperty<Paint> fill)
    {

        // get width and height of character n
        leftEdgeCaret = new Caret(FontMetricsHelper.getInstance().getFontHeight(fontFamily, fontSize));
        this.getChildren().add(leftEdgeCaret);
        setUpHandler();
        setUpLayout(pos);
    }

    private void setUpHandler()
    {
        setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event)
            {
                // move cursor to last cursor position in this line
                moveCaretTo(getNumberOfCaret() - 1);
                Section section = (Section) getParent();
            }
        });
    }

    protected void setUpLayout(Pos pos)
    {
        getStyleClass().add("/css/caret-line.css"); // todo check css and change name
        this.setMinSize(0, 0);
        getStyleClass().add("text-line");
        this.setAlignment(pos);
    }

    /**
     * Add after caret.
     *
     * @param n the n
     * @return the new created caret
     */
    public Caret addAfterCaret(Text n)
    {
        double width = n.getBoundsInLocal().getWidth();
        double height = n.getBoundsInLocal().getHeight();
        // set width and height of this according to this character
        TextGrid gap = new TextGrid(n);
        gap.setPrefSize(width - Caret.getCaretWidth(), height);
        gap.setMinSize(0, 0);
        Caret newCaret = new Caret(height);
        int i = insertPosition();
        if (getCaretPosition() == getNumberOfCaret() - 1)
        {
            getChildren().add(newCaret);
        }
        else
        {
            getChildren().add(i, newCaret);
        }
        getChildren().add(i, gap);
        newCaret.show();
        return newCaret;
    }


    private int insertPosition()
    {
        return 1 + getCaretPosition() * 2;
    }

    /**
     * Remove backward from caret.
     */
    public void removeBackwardFromCaret()
    {
        int index = getCaretPosition();
        if (index == 0)
        {
            // todo cannot be removed! remove this line!
        }
        else
        {
            moveCaretTo(index - 1);
            this.getChildren().removeAll(getGapBeforeCaret(index), getCaret(index));
        }
    }

    /**
     * Gets caret at the specified position.
     *
     * @param position the specified position
     * @return the caret at the position specified
     */
    public Caret getCaret(int position)
    {
        if (position < 0 || (position > (getNumberOfCaret() - 1))) // e.g. position = 0, number of caret = 1
        {
            throw new IndexOutOfBoundsException("This line doesn't have caret at " + position);
        }
        int i = 0;
        for (Node n : this.getChildren())
        {
            if (n instanceof Caret)
            {
                if (i == position)
                {

                    return (Caret) n;
                }
                else
                {
                    i = i + 1;
                }
            }
        }
        throw new IllegalStateException("Caret not in this line"); // todo should never trigger this
    }

    /**
     * Gets gap before caret.
     *
     * @param position the position
     * @return the gap before caret
     */
    public Pane getGapBeforeCaret(int position)
    {
        if (position < 0 || (position > (getNumberOfCaret() - 1))) // e.g. position = 0, number of caret = 1
        {
            throw new IndexOutOfBoundsException("This line doesn't have caret at " + position);
        }
        int i = 0;
        Pane pane = null;
        for (Node n : this.getChildren())
        {
            if (n instanceof Pane)
            {
                pane = (Pane) n;
            }
            if (n instanceof Caret)
            {
                if (i == position)
                {
                    return pane;
                }
                else
                {
                    i = i + 1;
                }
            }
        }
        throw new IllegalStateException("Caret not in this line"); // todo should never trigger this
    }

    /**
     * Gets caret position.
     *
     * @return the caret position
     */
    public int getCaretPosition()
    {
        int position = 0;
        for (Node n : this.getChildren())
        {
            if (n instanceof Caret)
            {
                if (((Caret) n).isOn())
                {
                    return position;
                }
                else
                {
                    position = position + 1;
                }
            }
        }
        throw new IllegalStateException("Caret not in this line");
    }

    /**
     * Gets real position.
     *
     * @param node the character
     * @return the real position
     */
    public int getRealPosition(Node node)
    {
        int position = 0;
        for (Node n : this.getChildren())
        {
            if (n == node)
            {
                return position;
            }
            position = position + 1;
        }
        throw new IllegalStateException("Node " + node + " not in this line: " + this.toString());
    }

    /**
     * Move caret to boolean.
     *
     * @param position the position
     * @return the boolean
     */
    public boolean moveCaretTo(int position)
    {

        if (position < 0 || (position > (getNumberOfCaret() - 1))) // e.g. position = 0, number of caret = 1
        {
            throw new IndexOutOfBoundsException("This line doesn't have " + position + " characters");
        }
        int i = 0;
        for (Node n : this.getChildren())
        {
            if (n instanceof Caret)
            {
                if (i == position)
                {
                    ((Caret) n).show();
                    return true;
                }
                else
                {
                    i = i + 1;
                }
            }
        }
        throw new IndexOutOfBoundsException("Unknown Error");
//        return false; // todo why would this happen?
    }

    /**
     * Get number of caret position of this line
     *
     * @return the number of caret position in this line
     */
    public int getNumberOfCaret()
    {
        return ((this.getChildren().size() - 1) / 2) + 1;
    }

    public void moveCaretRight()
    {
        System.out.println("Move caret right");
        int currentPosition = getCaretPosition();
        System.out.println("Current position: " + currentPosition);
        System.out.println("Move on to: " + (currentPosition + 1));
        if ((getNumberOfCaret() - 1) == currentPosition)
        {
            // todo move to the next line's line head
        }
        else
        {
            moveCaretTo(currentPosition + 1);
        }
    }

    public void moveCaretLeft()
    {
        System.out.println("Move caret left");
        int currentPosition = getCaretPosition();
        System.out.println("Current position: " + currentPosition);
        System.out.println("Move on to: " + (currentPosition - 1));
        if (currentPosition == 0)
        {
            // todo move to the line before
        }
        else
        {
            moveCaretTo(currentPosition - 1);
        }

    }

    /**
     * Gets line width.
     *
     * @return the line width
     */
    public double getLineWidth()
    {
        double width = 0;
        for (Node n : getChildren())
        {
            if (n instanceof TextGrid)
            {
                width = width + n.getBoundsInLocal().getWidth();
            }
        }
        return width;
    }

    public boolean hasWhiteSpaceInBetween()
    {
        //todo
        boolean ready = false;
        for (Node n : getChildren())
        {
            if (n instanceof TextGrid)
            {
                if (((Text) ((TextGrid) n).getCharacter()).getText().equals(" "))
                {
                    ready = true;
                }
                else
                {
                    if (ready)
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public TextLine getPreviousLine()
    {
        return previousLine;
    }

    public void setPreviousLine(TextLine previousLine)
    {
        this.previousLine = previousLine;
    }

    public TextLine getNextLine()
    {
        return nextLine;
    }

    public void setNextLine(TextLine nextLine)
    {
        this.nextLine = nextLine;
    }

    public void showLeftEdgeCaret()
    {
        leftEdgeCaret.show();
    }

    Word getWords()
    {
        Word word = new Word();
        for (Node node : getChildren())
        {
            if (node instanceof TextGrid)
            {
                word.add(((TextGrid) node).getCharacter());
            }
        }
        return word;
    }


    Word getWordsAfterCaret()
    {
        Word word = new Word();
        for (int i = getRealPosition(getCaret(getCaretPosition())); i < this.getChildren().size(); i++)
        {
            Node n = this.getChildren().get(i);
            if (n instanceof TextGrid)
            {
                word.add(((TextGrid) n).getCharacter());
            }
        }
        return word;
    }

    Word getContentNodes()
    {
        Word word = new Word();
        for (int i = 1; i < getChildren().size(); i++)
        {
            word.add(this.getChildren().get(i));

        }
        return word;
    }

    Word getContentNodesAfterCaret()
    {
        Word word = new Word();
        for (int i = getRealPosition(getCaret(getCaretPosition())) + 1; i < this.getChildren().size(); i++)
        {
            word.add(this.getChildren().get(i));
        }
        return word;
    }


    @Override
    public String toString()
    {
        StringBuilder stringBuilder = new StringBuilder();
        for (Node n : getChildren())
        {
            if (n instanceof TextGrid)
            {
                stringBuilder.append(((Text) ((TextGrid) n).getCharacter()).getText());
            }
        }
        return stringBuilder.toString();
    }


}
