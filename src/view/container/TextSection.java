package view.container;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import model.semantic.Semantic;
import view.Caret;
import view.helper.CaretHelper;
import view.helper.Page;

import java.util.ArrayList;
import java.util.List;

public abstract class TextSection extends Section {

    protected Semantic currentSemantic = Semantic.NORMAL;
    private Pos alignment = Pos.TOP_LEFT;
    private double fontSize;
    private String fontFamily;
    protected ObjectProperty<Paint> fill = new SimpleObjectProperty<>(Color.BLACK);

    public TextSection(String fontFamily, double fontSize)
    {
        // initialize the font family and font size for paragraph
        this.fontFamily = fontFamily;
        this.fontSize = fontSize;
        // initialize the paragraph
        this.getChildren().add(new TextLine(alignment, fontFamily, fontSize));
        setUpHandlers();
        setUpLayout();
    }

    public Semantic getCurrentSemantic()
    {
        return currentSemantic;
    }

    public void setCurrentSemantic(Semantic currentSemantic)
    {
        this.currentSemantic = currentSemantic;
    }

    /**
     * Set up the layout of this.
     */
    protected void setUpLayout()
    {
        // todo design the new text-section.css
        getStylesheets().add("/css/javaFX/text-section.css");
        getStyleClass().add("paragraph");
    }

    @Override
    protected void payAttention()
    {
        ((TextLine) this.getChildren().get(0)).getCaret(0).show();
        this.requestFocus();
    }

    @Override
    protected void payAttentionToEnd()
    {
        TextLine lastLine = ((TextLine) this.getChildren().get(getNumberOfLines() - 1));
        lastLine.getCaret(lastLine.getNumberOfCaret() - 1).show();
        this.requestFocus();
    }

    /**
     * Set up handlers for this.
     */
    private void setUpHandlers()
    {
        this.setOnKeyPressed(this::getOnKeyPressed);
        this.setOnMousePressed(this::getOnMousePressed);
    }

    protected void getOnMousePressed(MouseEvent mouseEvent)
    {
        this.requestFocus();
    }


    protected void getOnKeyPressed(KeyEvent keyEvent)
    {
        TextLine currentLine = ((TextLine) CaretHelper.getSelectedCaret().getParent());
        KeyCode keyCode = keyEvent.getCode();
        String text = keyEvent.getText();
        Text inputCharacter = new Text(text);
        inputCharacter.setFont(Font.font(fontFamily,
                                         currentSemantic.getFontWeight(),
                                         currentSemantic.getFontPosture(),
                                         fontSize)); // todo set font
        System.out.println("Key: " + keyCode + ", Text: " + text);

        if (keyCode.isArrowKey())
        {
            // move the caret around
            if (keyCode.equals(KeyCode.UP))
            {
                // todo move caret up
                TextLine previousLine = currentLine.getPreviousLine();
                // if the line where the caret is in has line before in this paragraph, move to the line before
                if (currentLine.getPreviousLine() != null)
                {
                    // place caret at the same index of previous line
                    if (previousLine.getNumberOfCaret() < currentLine.getCaretPosition() + 1)
                    {
                        // put caret on the last character of previous line
                        previousLine.getCaret(previousLine.getNumberOfCaret() - 1).show();
                    }
                    else
                    {
                        previousLine.getCaret(currentLine.getCaretPosition()).show();
                    }
                }
                else// todo move to the next section
                {
                    // do nothing
                    if (previousSection != null)
                    {
                        previousSection.payAttention();
                    }
                }
                // todo if not, move to the previous section
            }
            else if (keyCode.equals(KeyCode.DOWN))
            {

                TextLine nextLine = currentLine.getNextLine();
                // todo move caret down
                if (currentLine.getNextLine() != null)
                {
                    if (nextLine.getNumberOfCaret() < currentLine.getCaretPosition() + 1)
                    {
                        // put caret on the last character of next line
                        nextLine.getCaret(nextLine.getNumberOfCaret() - 1).show();
                    }
                    else
                    {
                        nextLine.getCaret(currentLine.getCaretPosition()).show();
                    }
                }
                else// todo move to the next section
                {
                    // do nothing
                    if (nextSection != null)
                    {
                        nextSection.payAttention();
                    }
                }


            }
            else if (keyCode.equals(KeyCode.LEFT))
            {
                // todo move caret left
                if (keyEvent.isMetaDown() || keyCode.equals(KeyCode.HOME))
                {
                    // move caret to the first caret pos in current line

                    currentLine.getCaret(0).show();
                }
                else if (keyEvent.isControlDown())
                {
                    // todo move to the last word
                    // find closest non-whitespace character who has whitespace after
                    getCaretBeforeLastWord();
                }
                else
                {
                    if ((currentLine.getCaretPosition() == 0) && currentLine.getPreviousLine() != null)
                    {
                        TextLine previousLine = currentLine.getPreviousLine();
                        previousLine.getCaret(previousLine.getNumberOfCaret() - 1).show();
                    }
                    else
                    {
                        ((TextLine) CaretHelper.getSelectedCaret().getParent()).moveCaretLeft();
                    }
                }

            }
            else if (keyCode.equals(KeyCode.RIGHT))
            {
                // todo move caret right
                if (keyEvent.isMetaDown() || keyCode.equals(KeyCode.END))
                {
                    // move caret to the last caret pos in current line
                    currentLine.getCaret(currentLine.getNumberOfCaret() - 1).show();
                }
                else if (keyEvent.isControlDown())
                {
                    // todo move caret to next word
//                    moveCaretBeforeNextWord();
                }
                else
                {
                    if ((currentLine.getCaretPosition() == (currentLine.getNumberOfCaret() - 1)) && currentLine.getNextLine() != null)
                    {
                        TextLine nextLine = currentLine.getNextLine();
                        nextLine.getCaret(0).show();
                    }
                    else
                    {
                        ((TextLine) CaretHelper.getSelectedCaret().getParent()).moveCaretRight();
                    }
                }


            }
        }
        else if (keyCode.isWhitespaceKey())
        {
            if (keyCode.equals(KeyCode.ENTER))
            {
                // create a new line
            }
            else if (keyCode.equals(KeyCode.SPACE))
            {
                add(inputCharacter);
            }
        }
        else if (keyCode.equals(KeyCode.SHIFT))
        {

        }
        else if (keyCode.equals(KeyCode.BACK_SPACE))
        {
            if (currentLine.getCaretPosition() == 0)
            {
                TextLine previousLine = currentLine.getPreviousLine();
                if (previousLine != null)
                {
                    // place caret on the last line's last caret
                    Caret lastCaretOnPreviousLine = previousLine.getCaret(previousLine.getNumberOfCaret() - 1);
                    lastCaretOnPreviousLine.show();
                    previousLine.removeBackwardFromCaret();

                    // todo wrap, remove empty line
                    for (int i = getLineIndexInThisParagraph(currentLine); i < this.getChildren().size(); i++) //
                    {
                        wrapBackward((TextLine) this.getChildren().get(i));
                    }
                    removeEmptyLines();
                }
                else
                {
                    // nothing to delete, don't move
                }
            }
            else
            {
                currentLine.removeBackwardFromCaret();
                // todo wrap all lines, if previous line has space, wrap this line to previous line

                for (int i = 0; i < this.getChildren().size(); i++) //
                {
                    wrapBackward((TextLine) this.getChildren().get(i));
                }
                removeEmptyLines();

                // todo sometimes caret disappear or freeze
            }
        }
        else
        {
            if (!keyEvent.isControlDown() &&
                    !keyEvent.isMetaDown() &&
                    !keyCode.isFunctionKey() &&
                    !keyCode.isModifierKey() &&
                    !keyCode.equals(KeyCode.CAPS))
            {
                add(inputCharacter);
            }
        }
    }

    private void getCaretBeforeLastWord()
    {
        Caret currentCaret = CaretHelper.getSelectedCaret();
        TextLine currentLine = ((TextLine) currentCaret.getParent());
        boolean beforeIsANonWhitespace = false;

        for (int i = currentLine.getRealPosition(currentCaret); i >= 0; i--)
        {
            Node n = currentLine.getChildren().get(i);
            if (n instanceof Caret)
            {
                int pos = currentLine.getRealPosition(n);
                if (pos > 0 && pos < currentLine.getChildren().size() - 1) ;
            }
        }

    }

    public int getNumberOfLines()
    {
        return this.getChildren().size();
    }

    public int getLineIndexInThisParagraph(TextLine line)
    {
        for (int i = 0; i < this.getChildren().size(); i++)
        {
            if (getChildren().get(i) == line)
            {
                return i;
            }
        }
        throw new IllegalStateException("This line is not at this paragraph: " + line);
    }

    /**
     * Add text (supposed to be a character) to the line and wrap text accordingly.
     *
     * @param text the text to be added to this
     */
    public void add(Text text)
    {
        text.fillProperty().bind(fill);
        TextLine textLine = (TextLine) CaretHelper.getSelectedCaret().getParent(); // get the textLine where caret is in
        Caret newCaret = textLine.addAfterCaret(text); // todo if it's a space, check if previous line can hold words in this line
        System.out.println("Line width: " + textLine.getLineWidth() + ", limit: " + Page.getLineWidthLimit());
        for (int i = getLineIndexInThisParagraph(textLine); i < this.getChildren().size(); i++)
        {
            wrapForward((TextLine) this.getChildren().get(i));
        }

        if (text.getText().equals(" "))
        {
            for (int i = getLineIndexInThisParagraph(textLine); i < this.getChildren().size(); i++)
            {
                wrapBackward((TextLine) this.getChildren().get(i));
            }
            removeEmptyLines();
        }
        newCaret.show();
    }

    public void add(Node node)
    {
        TextLine textLine = (TextLine) CaretHelper.getSelectedCaret().getParent(); // get the textLine where caret is in
        textLine.getChildren().add(node); // todo if it's a space, check if previous line can hold words in this line
        System.out.println("Line width: " + textLine.getLineWidth() + ", limit: " + Page.getLineWidthLimit());
        for (int i = getLineIndexInThisParagraph(textLine); i < this.getChildren().size(); i++)
        {
            wrapForward((TextLine) this.getChildren().get(i));
        }

        for (int i = getLineIndexInThisParagraph(textLine); i < this.getChildren().size(); i++)
        {
            wrapBackward((TextLine) this.getChildren().get(i));
        }
        removeEmptyLines();
    }

    /**
     * Add words to the line and wrap text accordingly.
     *
     * @param words the words to be added to this
     */
    public void add(Word words)
    {
        for (Node node : words)
        {
            if (node instanceof Text)
            {
                add((Text) node);
            }
            else
            {
                add(node);
            }

        }
    }

    private void wrapBackward(TextLine textLine)
    {
        if (textLine.getPreviousLine() != null)
        {
            // get left space of previous line
            double leftSpaceOfPreviousLine = Page.getLineWidthLimit() - textLine.getPreviousLine().getLineWidth();
            // todo get words that can be warped backward
            // todo put these words in previous line!
            Word wordsToWrapBackward = getWordsToWrapBackward(textLine, leftSpaceOfPreviousLine);
            for (Node node : wordsToWrapBackward)
            {
                textLine.getPreviousLine().getChildren().add(node);
            }
        }
        else
        {
            // first line, can't wrap
        }
    }

    private void removeEmptyLines()
    {
        if (getChildren().size() != 1)
        {
            List<Node> linesToBeRemoved = new ArrayList<>();

            for (Node n : getChildren())
            {
                if (n instanceof TextLine)
                {
                    if (((TextLine) n).getChildren().size() == 1)
                    { // only left the left edge caret
                        TextLine previousLine = ((TextLine) n).getPreviousLine();
                        TextLine nextLine = ((TextLine) n).getNextLine();
                        if (previousLine != null)
                        {
                            if (nextLine != null)
                            {
                                previousLine.setNextLine(nextLine);
                                nextLine.setPreviousLine(previousLine);
                            }
                            else
                            {
                                previousLine.setNextLine(null);
                            }
                        }
                        else
                        {
                            if (nextLine != null)
                            {
                                nextLine.setPreviousLine(null);
                            }

                        }
                        // remove n
                        linesToBeRemoved.add(n);
                    }
                }
                else
                {
                    throw new IllegalStateException("There are something else");
                }
            }
            System.out.println("remove size :" + linesToBeRemoved.size());
            for (Node n : linesToBeRemoved)
            {
                this.getChildren().remove(n);
            }
        }

    }

    private Word getWordsToWrapBackward(TextLine lineToBeWrapped, double width)
    {
        Word nodesToWrap = new Word();
        Word temp = new Word();
        boolean hasNoneWhitespaceCharacter = false;
        boolean hasWhitespace = false;
        for (int i = 1; i < lineToBeWrapped.getChildren().size(); i++)
        {
            Node n = lineToBeWrapped.getChildren().get(i);
            if (n instanceof TextGrid)
            {
                if (((Text) ((TextGrid) n).getCharacter()).getText().equals(" "))
                {
                    hasWhitespace = true;
                    temp.add(n);
//                    if (width >= nodesToWrap.getWordWidth())
//                    {
//                        if (hasNoneWhitespaceCharacter)
//                        {
//                            return nodesToWrap;
//                        }
//                        else
//                        {
//                            // continue search word
//                        }
//                    }
//                    else
//                    {
//                        return new Word();
//                    }
                }
                else
                {
                    hasNoneWhitespaceCharacter = true;
                    temp.add(n);
                }
            }
            else if (n instanceof Caret)
            {
                temp.add(n);
                if (hasWhitespace)
                {
                    if (width >= nodesToWrap.getWordWidth() + temp.getWordWidth())
                    {
                        if (hasNoneWhitespaceCharacter)
                        {
                            // todo there might be more words can wrap
                            nodesToWrap.addAll(temp);
                            temp.clear();
                            // return nodesToWrap;
                        }
                        else
                        {
                            // continue search word
                            hasWhitespace = false;
                        }
                    }
                    else
                    {
                        return nodesToWrap;
                    }
                }
            }
        }
        if (lineToBeWrapped.getNextLine() == null)
        {

            System.out.println("No words to wrap but line end");

            nodesToWrap.addAll(temp);
            if (width >= nodesToWrap.getWordWidth())
            {
//                nodesToWrap.addAll(temp);
//                nodesToWrap.addAll(temp);
                System.out.println("Temp: " + temp);
                System.out.println("Nodes To Wrap: " + nodesToWrap);
                return nodesToWrap; // todo might be wrong logic
            }
            else
            {
                return new Word();
            }

        }
        else
        {
            return new Word(); // no word to wrap
            // throw new IllegalArgumentException("No words to wrap backwards: " + textLine.toString());
        }
    }

    /**
     * Wrap lines as necessary if the specified line's width larger than the line width limit
     *
     * @param textLine the specified line to be checked (and wrap)
     */
    private void wrapForward(TextLine textLine)
    {
        // if this line needs to wrap
        if (textLine.getLineWidth() > Page.getLineWidthLimit())
        {
            Word word;
            if (textLine.hasWhiteSpaceInBetween())
            {
                // get words until this line < width limit
                // todo get characters in this textLine that needs to wrap,
                word = getWordsToWrapForward(textLine);
                // todo remove from this line
                // create
            }
            else
            {
                // todo get characters can make this line's width < width limit
                word = getCharactersToWrapForward(textLine);

            }

            // todo if next textLine exist, put them in the next textLine, re-typesetting the textLine and
            if (textLine.getNextLine() != null)
            {
                for (Node node : word)
                {
                    textLine.getNextLine().getChildren().add(1, node);
                }
            }
            else // todo if not, create a new textLine, put them in the next textLine
            {

                TextLine newLine = new TextLine(alignment, fontFamily, fontSize);
                newLine.setPreviousLine(textLine);
                newLine.setNextLine(null);
                textLine.setNextLine(newLine);
                this.getChildren().add(newLine);
                for (Node node : word)
                {
                    newLine.getChildren().add(1, node);
                }

            }
        }
        else
        {
            System.out.println("No need to wrap");
        }
    }

    /**
     * Get the characters that needs to wrap (chars that makes this line's length larger than the line width limit).
     * Used when the specified line doesn't has any whitespace in between (in other words, no words in the line).
     *
     * @param textLine the line that has words to wrap
     * @return the wrapped characters
     */
    private Word getCharactersToWrapForward(TextLine textLine)
    {
        Word nodesToWrap = new Word();
        for (int i = textLine.getChildren().size() - 1; i > 0; i--)
        {
            Node n = textLine.getChildren().get(i);
            if (n instanceof TextGrid)
            {
                if (textLine.getLineWidth() - nodesToWrap.getWordWidth() < Page.getLineWidthLimit())
                {
                    nodesToWrap.remove(nodesToWrap.getLast());
                    return nodesToWrap;
                }
                else
                {
                    nodesToWrap.add(n);
                }
            }
            else if (n instanceof Caret)
            {
                nodesToWrap.add(n);
            }
        }
        throw new IllegalArgumentException("No characters to wrap");
    }

    /**
     * Get the words that needs to wrap (words that makes this line's length larger than the line width limit).
     * Used when the specified line has any whitespace in between (in other words, got words in the line).
     *
     * @param textLine the line that has words to wrap
     * @return the wrapped words
     */
    private Word getWordsToWrapForward(TextLine textLine)
    {
        Word nodesToWrap = new Word();
        for (int i = textLine.getChildren().size() - 1; i > 0; i--)
        {
            Node n = textLine.getChildren().get(i);
            if (n instanceof TextGrid)
            {
                if (((Text) ((TextGrid) n).getCharacter()).getText().equals(" "))
                {
                    if (textLine.getLineWidth() - nodesToWrap.getWordWidth() < Page.getLineWidthLimit())
                    {
                        nodesToWrap.remove(nodesToWrap.getLast());
                        return nodesToWrap;
                    }
                    else
                    {
                        nodesToWrap.add(n);
                    }
                }
                else
                {
                    nodesToWrap.add(n);
                }
            }
            else if (n instanceof Caret)
            {
                nodesToWrap.add(n);
            }
        }
        return new Word();
        // throw new IllegalArgumentException("No words to wrap: " + textLine.toString());
    }

    public void align(Pos pos)
    {
        alignment = pos;
        for (Node n : this.getChildren())
        {
            TextLine line = (TextLine) n;
            line.setAlignment(pos);
        }
    }

    @Override
    public boolean isEmpty()
    {
        if (((TextLine) this.getChildren().get(0)).getChildren().size() == 1)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public Word getAllWords()
    {
        Word word = new Word();
        for (Node n : getChildren())
        {
            if (n instanceof TextLine)
            {
                word.addAll(((TextLine) n).getWords());
            }
        }
        return word;
    }


    public Word getAllWordsAfterCaret()
    {
        Word word = new Word();
        TextLine currentLine = ((TextLine) CaretHelper.getSelectedCaret().getParent());

        word.addAll(currentLine.getWordsAfterCaret());
        for (int i = getLineIndexInThisParagraph(currentLine) + 1; i < getNumberOfLines(); i++)
        {
            Node n = this.getChildren().get(i);
            if (n instanceof TextLine)
            {
                word.addAll(((TextLine) n).getWords());
            }
        }
        return word;
    }

    public Word getAllContentNodesAfterCaret()
    {
        Word word = new Word();
        TextLine currentLine = ((TextLine) CaretHelper.getSelectedCaret().getParent());

        word.addAll(currentLine.getContentNodesAfterCaret());
        for (int i = getLineIndexInThisParagraph(currentLine) + 1; i < getNumberOfLines(); i++)
        {
            Node n = this.getChildren().get(i);
            if (n instanceof TextLine)
            {
                word.addAll(((TextLine) n).getContentNodes());
            }
        }
        return word;
    }

    @Override
    public String toString()
    {
        StringBuilder stringBuilder = new StringBuilder();
        for (Node n : getAllWords())
        {
            if (n instanceof Text)
            {
                stringBuilder.append(((Text) n).getText());
            }
        }
        return stringBuilder.toString();
    }

    @Override
    public String getContentInXML()
    {
        System.out.println("------------------------------------------------------------");
        StringBuilder piece = new StringBuilder();
        String lastStyle = null;
        for (Node l : getChildren())
        {
            TextLine line = (TextLine) l;
            for (Node c : line.getChildren())
            {
                if (c instanceof TextGrid)
                {
                    Text character = (Text) ((TextGrid) c).getCharacter();
                    String style = character.getFont().getStyle();
                    if (lastStyle == null)
                    {
                        // initialization
                        lastStyle = style;
                        if (style.equalsIgnoreCase("bold"))
                        {
                            piece.append("<strong>");
                        }
                        else if (style.equalsIgnoreCase("italic"))
                        {
                            piece.append("<emphasis>");
                        }
                        piece.append(character.getText());

                    }
                    else if (style.equals(lastStyle))
                    {
                        piece.append(character.getText());
                    }
                    else // semantic not equal to last semantic
                    {
                        if (lastStyle.equalsIgnoreCase("bold"))
                        {
                            piece.append("</strong>");
                        }
                        else if (lastStyle.equalsIgnoreCase("italic"))
                        {
                            piece.append("</emphasis>");
                        }

                        lastStyle = style;
//                        piece = new StringBuilder();
                        // todo add tag. e.g. <br>
                        if (lastStyle.equalsIgnoreCase("bold"))
                        {
                            piece.append("<strong>");
                        }
                        else if (lastStyle.equalsIgnoreCase("italic"))
                        {
                            piece.append("<emphasis>");
                        }
                        piece.append(character.getText());
                    }
                }
            }
        }

        if (lastStyle != null)
        {
            if (lastStyle.equalsIgnoreCase("bold"))
            {
                piece.append("</strong>");
            }
            else if (lastStyle.equalsIgnoreCase("italic"))
            {
                piece.append("</emphasis>");
            }
        }
        System.out.println("------------------------------------------------------------");
        return piece.toString();
    }
}
