package view.container;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
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

public abstract class TextSection extends Section {
    protected Semantic currentSemantic;
    private Pos alignment;
    private double fontSize;
    private String fontFamily;
    protected ObjectProperty<Paint> fill;

    public TextSection(String fontFamily, double fontSize) {
        this.currentSemantic = Semantic.NORMAL;
        this.alignment = Pos.TOP_LEFT;
        this.fill = new SimpleObjectProperty(Color.BLACK);
        this.fontFamily = fontFamily;
        this.fontSize = fontSize;
        this.getChildren().add(new TextLine(this.alignment, fontFamily, fontSize));
        this.setUpHandlers();
        this.setUpLayout();
    }

    public Semantic getCurrentSemantic() {
        return this.currentSemantic;
    }

    public void setCurrentSemantic(Semantic currentSemantic) {
        this.currentSemantic = currentSemantic;
    }

    protected void setUpLayout() {
        this.getStylesheets().add("/css/javaFX/text-section.css");
        this.getStyleClass().add("paragraph");
    }

    protected void payAttention() {
        ((TextLine)this.getChildren().get(0)).getCaret(0).show();
        this.requestFocus();
    }

    protected void payAttentionToEnd() {
        TextLine lastLine = (TextLine)this.getChildren().get(this.getNumberOfLines() - 1);
        lastLine.getCaret(lastLine.getNumberOfCaret() - 1).show();
        this.requestFocus();
    }

    private void setUpHandlers() {
        this.setOnKeyPressed(this::getOnKeyPressed);
        this.setOnMousePressed(this::getOnMousePressed);
    }

    protected void getOnMousePressed(MouseEvent mouseEvent) {
        this.requestFocus();
    }

    protected void getOnKeyPressed(KeyEvent keyEvent) {
        TextLine currentLine = (TextLine)CaretHelper.getInstance().getSelectedCaret().getParent();
        KeyCode keyCode = keyEvent.getCode();
        String text = keyEvent.getText();
        Text inputCharacter = new Text(text);
        inputCharacter.setFont(Font.font(this.fontFamily, this.currentSemantic.getFontWeight(), this.currentSemantic.getFontPosture(), this.fontSize));
        System.out.println("Key: " + keyCode + ", Text: " + text);
        TextLine previousLine;
        if (keyCode.isArrowKey()) {
            if (keyCode.equals(KeyCode.UP)) {
                previousLine = currentLine.getPreviousLine();
                if (currentLine.getPreviousLine() != null) {
                    if (previousLine.getNumberOfCaret() < currentLine.getCaretPosition() + 1) {
                        previousLine.getCaret(previousLine.getNumberOfCaret() - 1).show();
                    } else {
                        previousLine.getCaret(currentLine.getCaretPosition()).show();
                    }
                } else if (this.previousSection != null) {
                    this.previousSection.payAttention();
                }
            } else if (keyCode.equals(KeyCode.DOWN)) {
                previousLine = currentLine.getNextLine();
                if (currentLine.getNextLine() != null) {
                    if (previousLine.getNumberOfCaret() < currentLine.getCaretPosition() + 1) {
                        previousLine.getCaret(previousLine.getNumberOfCaret() - 1).show();
                    } else {
                        previousLine.getCaret(currentLine.getCaretPosition()).show();
                    }
                } else if (this.nextSection != null) {
                    this.nextSection.payAttention();
                }
            } else if (keyCode.equals(KeyCode.LEFT)) {
                if (!keyEvent.isMetaDown() && !keyCode.equals(KeyCode.HOME)) {
                    if (keyEvent.isControlDown()) {
                        this.getCaretBeforeLastWord();
                    } else if (currentLine.getCaretPosition() == 0 && currentLine.getPreviousLine() != null) {
                        previousLine = currentLine.getPreviousLine();
                        previousLine.getCaret(previousLine.getNumberOfCaret() - 1).show();
                    } else {
                        ((TextLine)CaretHelper.getInstance().getSelectedCaret().getParent()).moveCaretLeft();
                    }
                } else {
                    currentLine.getCaret(0).show();
                }
            } else if (keyCode.equals(KeyCode.RIGHT)) {
                if (!keyEvent.isMetaDown() && !keyCode.equals(KeyCode.END)) {
                    if (!keyEvent.isControlDown()) {
                        if (currentLine.getCaretPosition() == currentLine.getNumberOfCaret() - 1 && currentLine.getNextLine() != null) {
                            previousLine = currentLine.getNextLine();
                            previousLine.getCaret(0).show();
                        } else {
                            ((TextLine)CaretHelper.getInstance().getSelectedCaret().getParent()).moveCaretRight();
                        }
                    }
                } else {
                    currentLine.getCaret(currentLine.getNumberOfCaret() - 1).show();
                }
            }
        } else {
            int i;
            if (keyCode.isWhitespaceKey()) {
                if (!keyCode.equals(KeyCode.ENTER) && keyCode.equals(KeyCode.SPACE)) {
                    i = currentLine.getRealPosition(currentLine.getCaret(currentLine.getCaretPosition()));
                    if (i >= 1 && !((TextGrid)currentLine.getChildren().get(i - 1)).getCharacter().getText().equals(" ")) {
                        this.add(inputCharacter);
                    }
                }
            } else if (!keyCode.equals(KeyCode.SHIFT)) {
                if (keyCode.equals(KeyCode.BACK_SPACE)) {
                    if (currentLine.getCaretPosition() == 0) {
                        previousLine = currentLine.getPreviousLine();
                        if (previousLine != null) {
                            Caret lastCaretOnPreviousLine = previousLine.getCaret(previousLine.getNumberOfCaret() - 1);
                            lastCaretOnPreviousLine.show();
                            previousLine.removeBackwardFromCaret();

                            for(int index = this.getLineIndexInThisParagraph(currentLine); index < this.getChildren().size(); ++index) {
                                this.wrapBackward((TextLine)this.getChildren().get(index));
                            }

                            this.removeEmptyLines();
                        }
                    } else {
                        currentLine.removeBackwardFromCaret();

                        for(i = 0; i < this.getChildren().size(); ++i) {
                            this.wrapBackward((TextLine)this.getChildren().get(i));
                        }

                        this.removeEmptyLines();
                        if (currentLine.toString().equals("")) {
                            previousLine = currentLine.getPreviousLine();
                            if (previousLine != null) {
                                previousLine.getCaret(previousLine.getNumberOfCaret() - 1).show();
                            }
                        }
                    }
                } else if (!keyEvent.isControlDown() && !keyEvent.isMetaDown() && !keyCode.isFunctionKey() && !keyCode.isModifierKey() && !keyCode.equals(KeyCode.CAPS)) {
                    this.add(inputCharacter);
                }
            }
        }

    }

    private void getCaretBeforeLastWord() {
        Caret currentCaret = CaretHelper.getInstance().getSelectedCaret();
        TextLine currentLine = (TextLine)currentCaret.getParent();
        boolean beforeIsANonWhitespace = false;

        for(int i = currentLine.getRealPosition(currentCaret); i >= 0; --i) {
            Node n = (Node)currentLine.getChildren().get(i);
            if (n instanceof Caret) {
                int pos = currentLine.getRealPosition(n);
                if (pos > 0 && pos < currentLine.getChildren().size() - 1) {
                    ;
                }
            }
        }

    }

    public int getNumberOfLines() {
        return this.getChildren().size();
    }

    public int getLineIndexInThisParagraph(TextLine line) {
        for(int i = 0; i < this.getChildren().size(); ++i) {
            if (this.getChildren().get(i) == line) {
                return i;
            }
        }

        throw new IllegalStateException("This line is not at this paragraph: " + line);
    }

    public void add(Text text) {
        text.fillProperty().bind(this.fill);
        TextLine textLine = (TextLine)CaretHelper.getInstance().getSelectedCaret().getParent();
        Caret newCaret = textLine.addAfterCaret(text);
        System.out.println("Line width: " + textLine.getLineWidth() + ", limit: " + Page.getLineWidthLimit());

        int i;
        for(i = this.getLineIndexInThisParagraph(textLine); i < this.getChildren().size(); ++i) {
            this.wrapForward((TextLine)this.getChildren().get(i));
        }

        if (text.getText().equals(" ")) {
            for(i = this.getLineIndexInThisParagraph(textLine); i < this.getChildren().size(); ++i) {
                this.wrapBackward((TextLine)this.getChildren().get(i));
            }

            this.removeEmptyLines();
        }

        newCaret.show();
    }

    public void add(Node node) {
        TextLine textLine = (TextLine)CaretHelper.getInstance().getSelectedCaret().getParent();
        textLine.getChildren().add(node);
        System.out.println("Line width: " + textLine.getLineWidth() + ", limit: " + Page.getLineWidthLimit());

        int i;
        for(i = this.getLineIndexInThisParagraph(textLine); i < this.getChildren().size(); ++i) {
            this.wrapForward((TextLine)this.getChildren().get(i));
        }

        for(i = this.getLineIndexInThisParagraph(textLine); i < this.getChildren().size(); ++i) {
            this.wrapBackward((TextLine)this.getChildren().get(i));
        }

        this.removeEmptyLines();
    }

    public void add(Word words) {
        Iterator var2 = words.iterator();

        while(var2.hasNext()) {
            Node node = (Node)var2.next();
            if (node instanceof Text) {
                this.add((Text)node);
            } else {
                this.add(node);
            }
        }

    }

    private void wrapBackward(TextLine textLine) {
        if (textLine.getPreviousLine() != null) {
            double leftSpaceOfPreviousLine = Page.getLineWidthLimit() - textLine.getPreviousLine().getLineWidth();
            Word wordsToWrapBackward = this.getWordsToWrapBackward(textLine, leftSpaceOfPreviousLine);
            StringBuilder wrapbackWord = new StringBuilder();

            Node node;
            for(Iterator var6 = wordsToWrapBackward.iterator(); var6.hasNext(); textLine.getPreviousLine().getChildren().add(node)) {
                node = (Node)var6.next();
                if (node instanceof Text) {
                    wrapbackWord.append(((Text)node).getText());
                }
            }

            System.out.println("Wrap backward word: " + wrapbackWord.toString());
        }

    }

    private void removeEmptyLines() {
        if (this.getChildren().size() != 1) {
            List<Node> linesToBeRemoved = new ArrayList();
            Iterator var2 = this.getChildren().iterator();

            Node n;
            while(var2.hasNext()) {
                n = (Node)var2.next();
                if (!(n instanceof TextLine)) {
                    throw new IllegalStateException("There are something else");
                }

                if (((TextLine)n).getChildren().size() == 1) {
                    TextLine previousLine = ((TextLine)n).getPreviousLine();
                    TextLine nextLine = ((TextLine)n).getNextLine();
                    if (previousLine != null) {
                        if (nextLine != null) {
                            previousLine.setNextLine(nextLine);
                            nextLine.setPreviousLine(previousLine);
                        } else {
                            previousLine.setNextLine((TextLine)null);
                        }
                    } else if (nextLine != null) {
                        nextLine.setPreviousLine((TextLine)null);
                    }

                    linesToBeRemoved.add(n);
                }
            }

            System.out.println("remove size :" + linesToBeRemoved.size());
            var2 = linesToBeRemoved.iterator();

            while(var2.hasNext()) {
                n = (Node)var2.next();
                this.getChildren().remove(n);
            }
        }

    }

    private Word getWordsToWrapBackward(TextLine lineToBeWrapped, double width) {
        Word nodesToWrap = new Word();
        Word temp = new Word();
        boolean hasNoneWhitespaceCharacter = false;
        boolean hasWhitespace = false;

        for(int i = 1; i < lineToBeWrapped.getChildren().size(); ++i) {
            Node n = (Node)lineToBeWrapped.getChildren().get(i);
            if (n instanceof TextGrid) {
                if (((TextGrid)n).getCharacter().getText().equals(" ")) {
                    hasWhitespace = true;
                    temp.add(n);
                } else {
                    hasNoneWhitespaceCharacter = true;
                    temp.add(n);
                }
            } else if (n instanceof Caret) {
                temp.add(n);
                if (hasWhitespace) {
                    if (width < nodesToWrap.getWordWidth() + temp.getWordWidth()) {
                        return nodesToWrap;
                    }

                    if (hasNoneWhitespaceCharacter) {
                        nodesToWrap.addAll(temp);
                        temp.clear();
                    } else {
                        hasWhitespace = false;
                    }
                }
            }
        }

        if (lineToBeWrapped.getNextLine() == null) {
            System.out.println("No words to wrap but line end");
            nodesToWrap.addAll(temp);
            if (width >= nodesToWrap.getWordWidth()) {
                System.out.println("Temp: " + temp);
                System.out.println("Nodes To Wrap: " + nodesToWrap);
                return nodesToWrap;
            } else {
                return new Word();
            }
        } else {
            return new Word();
        }
    }

    private void wrapForward(TextLine textLine) {
        if (textLine.getLineWidth() > Page.getLineWidthLimit()) {
            System.out.println("exceed limit!");
            Word word;
            if (textLine.hasWhiteSpaceInBetween()) {
                word = this.getWordsAndCharactersToWrapForward(textLine);
                System.out.println(word.toString());
            } else {
                word = this.getCharactersToWrapForward(textLine);
            }

            if (textLine.getNextLine() != null) {
                Iterator var3 = word.iterator();

                while(var3.hasNext()) {
                    Node node = (Node)var3.next();
                    textLine.getNextLine().getChildren().add(1, node);
                }
            } else {
                TextLine newLine = new TextLine(this.alignment, this.fontFamily, this.fontSize);
                newLine.setPreviousLine(textLine);
                newLine.setNextLine((TextLine)null);
                textLine.setNextLine(newLine);
                this.getChildren().add(newLine);
                Iterator var7 = word.iterator();

                while(var7.hasNext()) {
                    Node node = (Node)var7.next();
                    newLine.getChildren().add(1, node);
                }
            }
        } else {
            System.out.println("No need to wrap");
        }

    }

    private Word getCharactersToWrapForward(TextLine textLine) {
        Word nodesToWrap = new Word();

        for(int i = textLine.getChildren().size() - 1; i > 0; --i) {
            Node n = (Node)textLine.getChildren().get(i);
            if (n instanceof TextGrid) {
                if (textLine.getLineWidth() - nodesToWrap.getWordWidth() < Page.getLineWidthLimit()) {
                    nodesToWrap.remove(nodesToWrap.getLast());
                    return nodesToWrap;
                }

                nodesToWrap.add(n);
            } else if (n instanceof Caret) {
                nodesToWrap.add(n);
            }
        }

        throw new IllegalArgumentException("No characters to wrap");
    }

    private Word getWordsAndCharactersToWrapForward(TextLine textLine) {
        Word nodesToWrap = new Word();

        for(int i = textLine.getChildren().size() - 1; i > 0; --i) {
            Node n = (Node)textLine.getChildren().get(i);
            if (n instanceof TextGrid) {
                if (((TextGrid)n).getCharacter().getText().equals(" ")) {
                    if (textLine.getLineWidth() - nodesToWrap.getWordWidth() < Page.getLineWidthLimit()) {
                        nodesToWrap.remove(nodesToWrap.getLast());
                        return nodesToWrap;
                    }

                    nodesToWrap.add(n);
                } else {
                    nodesToWrap.add(n);
                }
            } else if (n instanceof Caret) {
                nodesToWrap.add(n);
            }
        }

        return this.getCharactersToWrapForward(textLine);
    }

    public void align(Pos pos) {
        this.alignment = pos;
        Iterator var2 = this.getChildren().iterator();

        while(var2.hasNext()) {
            Node n = (Node)var2.next();
            TextLine line = (TextLine)n;
            line.setAlignment(pos);
        }

    }

    public boolean isEmpty() {
        return ((TextLine)this.getChildren().get(0)).getChildren().size() == 1;
    }

    public Word getAllWords() {
        Word word = new Word();
        Iterator var2 = this.getChildren().iterator();

        while(var2.hasNext()) {
            Node n = (Node)var2.next();
            if (n instanceof TextLine) {
                word.addAll(((TextLine)n).getWords());
            }
        }

        return word;
    }

    public Word getAllWordsAfterCaret() {
        Word word = new Word();
        TextLine currentLine = (TextLine)CaretHelper.getInstance().getSelectedCaret().getParent();
        word.addAll(currentLine.getWordsAfterCaret());

        for(int i = this.getLineIndexInThisParagraph(currentLine) + 1; i < this.getNumberOfLines(); ++i) {
            Node n = (Node)this.getChildren().get(i);
            if (n instanceof TextLine) {
                word.addAll(((TextLine)n).getWords());
            }
        }

        return word;
    }

    public Word getAllContentNodesAfterCaret() {
        Word word = new Word();
        TextLine currentLine = (TextLine)CaretHelper.getInstance().getSelectedCaret().getParent();
        word.addAll(currentLine.getContentNodesAfterCaret());

        for(int i = this.getLineIndexInThisParagraph(currentLine) + 1; i < this.getNumberOfLines(); ++i) {
            Node n = (Node)this.getChildren().get(i);
            if (n instanceof TextLine) {
                word.addAll(((TextLine)n).getContentNodes());
            }
        }

        return word;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        Iterator var2 = this.getAllWords().iterator();

        while(var2.hasNext()) {
            Node n = (Node)var2.next();
            if (n instanceof Text) {
                stringBuilder.append(((Text)n).getText());
            }
        }

        return stringBuilder.toString();
    }

    public String getContentInXML() {
        System.out.println("------------------------------------------------------------");
        StringBuilder piece = new StringBuilder();
        String lastStyle = null;
        Iterator var3 = this.getChildren().iterator();

        while(var3.hasNext()) {
            Node l = (Node)var3.next();
            TextLine line = (TextLine)l;
            Iterator var6 = line.getChildren().iterator();

            while(var6.hasNext()) {
                Node c = (Node)var6.next();
                if (c instanceof TextGrid) {
                    Text character = ((TextGrid)c).getCharacter();
                    String style = character.getFont().getStyle();
                    if (lastStyle == null) {
                        lastStyle = style;
                        if (style.equalsIgnoreCase("bold")) {
                            piece.append("<strong>");
                        } else if (style.equalsIgnoreCase("italic")) {
                            piece.append("<emphasis>");
                        }

                        piece.append(character.getText());
                    } else if (style.equals(lastStyle)) {
                        piece.append(character.getText());
                    } else {
                        if (lastStyle.equalsIgnoreCase("bold")) {
                            piece.append("</strong>");
                        } else if (lastStyle.equalsIgnoreCase("italic")) {
                            piece.append("</emphasis>");
                        }

                        lastStyle = style;
                        if (style.equalsIgnoreCase("bold")) {
                            piece.append("<strong>");
                        } else if (style.equalsIgnoreCase("italic")) {
                            piece.append("<emphasis>");
                        }

                        piece.append(character.getText());
                    }
                }
            }
        }

        if (lastStyle != null) {
            if (lastStyle.equalsIgnoreCase("bold")) {
                piece.append("</strong>");
            } else if (lastStyle.equalsIgnoreCase("italic")) {
                piece.append("</emphasis>");
            }
        }

        System.out.println("------------------------------------------------------------");
        return piece.toString();
    }

    public void loadContentByAdd(String content) {
        char[] var2 = content.toCharArray();
        int var3 = var2.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            char c = var2[var4];
            Text inputCharacter = new Text(String.valueOf(c));
            inputCharacter.setFont(Font.font(this.fontFamily, this.currentSemantic.getFontWeight(), this.currentSemantic.getFontPosture(), this.fontSize));
            this.add(inputCharacter);
        }

    }
}
