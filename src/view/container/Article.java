package view.container;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import model.semantic.Semantic;
import view.Caret;
import view.concrete.*;
import view.helper.CaretHelper;
import view.helper.Page;
import view.helper.Painter;
import view.helper.Theme;

public class Article extends VBox {

    private Theme theme;

    public Article(Theme theme)
    {
        this.theme = theme;
        setUpLayout();
        setUpKeyListener();
        TextSection textSection = new Paragraph(theme.getParagraphFontFamily(), theme.getParagraphFontSize());
        textSection.payAttention();
        getChildren().add(textSection);
    }


    public enum Structure {
        MAIN_TITLE,
        SECTION_TITLE,
        SUBSECTION_TITLE,
        SUBSUBSECTION_TITLE,
        PARAGRAPH,
        QUOTE,
        AUTHOR
    }

    private void setUpLayout()
    {
        getStylesheets().add("/css/javaFX/article.css");
        getStyleClass().add("paper");
        setPadding(new Insets(Page.getPagePadding()));
    }

    private void setUpKeyListener()
    {
        this.addEventFilter(KeyEvent.KEY_PRESSED, this::getOnKeyPressed);
//        this.setOnKeyPressed(this::getOnKeyPressed);
    }

    private void getOnKeyPressed(KeyEvent keyEvent)
    {
        KeyCode keyCode = keyEvent.getCode();
        String text = keyEvent.getText();
        System.out.println("Article: Key: " + keyCode + ", Text: " + text);
        TextLine currentLine = ((TextLine) CaretHelper.getSelectedCaret().getParent());
        Section currentSection = (Section) currentLine.getParent();
        if (keyEvent.isMetaDown() || keyEvent.isControlDown())
        { // Control in windows, Command in MAC
            if (keyCode.isDigitKey())
            {
                if (keyCode.equals(KeyCode.DIGIT1))
                {
                    createNewSectionAfterCaret(Structure.SECTION_TITLE);
                }
                else if (keyCode.equals(KeyCode.DIGIT2))
                {
                    // todo create a new sub section title
                    createNewSectionAfterCaret(Structure.SUBSECTION_TITLE);
                }
                else if (keyCode.equals(KeyCode.DIGIT3))
                {
                    // todo create a new subsub section title
                    createNewSectionAfterCaret(Structure.SUBSUBSECTION_TITLE);
                }

            }
            else if (keyCode.equals(KeyCode.B))
            {
                // todo toggle button
                // set currentEditing state to Bold
                if (currentSection instanceof Paragraph)
                {
                    if (((TextSection) currentSection).getCurrentSemantic().equals(Semantic.STRONG))
                    {
                        ((TextSection) currentSection).setCurrentSemantic(Semantic.NORMAL);
                    }
                    else
                    {
                        ((TextSection) currentSection).setCurrentSemantic(Semantic.STRONG);
                    }

                }
            }
            else if (keyCode.equals(KeyCode.I))
            {
                // todo toggle button
                if (currentSection instanceof Paragraph)
                {
                    if (((TextSection) currentSection).getCurrentSemantic() == Semantic.EMPHASIS)
                    {
                        ((TextSection) currentSection).setCurrentSemantic(Semantic.NORMAL);
                    }
                    else
                    {
                        ((TextSection) currentSection).setCurrentSemantic(Semantic.EMPHASIS);
                    }

                }
            }
            else if (keyCode.equals(KeyCode.K))
            {
                createNewSectionAfterCaret(Structure.QUOTE);
            }
        }
        else if (keyCode.isModifierKey())
        {

        }
        else if (keyCode.equals(KeyCode.ESCAPE)) // set semantic back to normal
        {
            if (currentSection instanceof Paragraph)
            {
                ((TextSection) currentSection).setCurrentSemantic(Semantic.NORMAL);
            }
        }
        else if (keyCode.isArrowKey())
        {
            // move the caret around
            if (keyCode.equals(KeyCode.UP))
            {
                // todo move caret up

            }
            else if (keyCode.equals(KeyCode.DOWN))
            {
                // todo move caret down
            }
            else if (keyCode.equals(KeyCode.LEFT))
            {
                // todo move caret left
//                 moveCaretLeft();
            }
            else if (keyCode.equals(KeyCode.RIGHT))
            {
                // todo move caret right
//                 moveCaretRight();
            }
        }
        if (keyCode.isWhitespaceKey())
        {
            if (keyCode.equals(KeyCode.ENTER))
            {
                // create a new line

                // if current section is also a paragraph, add the words after caret to the new paragraph
                if (currentSection instanceof Paragraph)
                {
                    Word word = ((Paragraph) currentSection).getAllContentNodesAfterCaret();

                    Section newSection = createNewSectionAfterCaret(Structure.PARAGRAPH);
                    ((Paragraph) newSection).add(word);
                    newSection.payAttention();
                }
                else
                {
                    Section newSection = createNewSectionAfterCaret(Structure.PARAGRAPH);
                }


                // todo bring text after the caret to next paragraph
            }
            else if (keyCode.equals(KeyCode.SPACE))
            {
//                 add(new Text(text));
            }
        }
        else if (keyCode.equals(KeyCode.SHIFT))
        {

        }
        else if (keyCode.equals(KeyCode.BACK_SPACE))
        {
            if (currentSection.isEmpty())
            {
                removeSection(currentSection);

                keyEvent.consume();
            }
            else
                // todo when current section has empty lines, remove it
                if (currentSection instanceof Paragraph)
                {

                    // At head of paragraph
                    if (currentLine.getCaretPosition() == 0 &&
                            ((TextSection) currentSection).getLineIndexInThisParagraph(currentLine) == 0)
                    {
                        // todo put text in this section to previous section and remove this section
                        Paragraph previousSection = (Paragraph) currentSection.getPreviousSection();
                        if (previousSection != null)
                        {
                            // todo get text in currentSection
                            // todo add to the end of previous section
                            Word wordsInCurrentSection = ((Paragraph) currentSection).getAllWords();
                            previousSection.payAttentionToEnd();
                            TextLine lastLineOfPreviousSection = ((TextLine) previousSection.getChildren().get(previousSection.getNumberOfLines() - 1));
                            Caret lastCaret = lastLineOfPreviousSection.getCaret(lastLineOfPreviousSection.getNumberOfCaret() - 1);
                            previousSection.add(wordsInCurrentSection);
                            // remove current section
                            removeSection(currentSection);
                            lastCaret.show();
                            keyEvent.consume(); // don't let layer under
                            // re pay attention

//                            previousSection.
                        }
                        else
                        {
                            // do nothing
                        }
                    }

                }
        }
        else if (keyCode.isLetterKey())
        {
//             add(new Text(text));
        }
    }

    private void removeSection(Section currentSection)
    {
        Section previousSection = currentSection.getPreviousSection();
        Section nextSection = currentSection.getNextSection();
        if (previousSection != null)
        {
            if (nextSection != null)
            {
                previousSection.setNextSection(nextSection);
                nextSection.setPreviousSection(previousSection);
            }
            else
            {
                previousSection.setNextSection(null);
            }
            this.getChildren().remove(currentSection);
            previousSection.payAttentionToEnd();
        }
        else
        {
            if (nextSection != null)
            {
                nextSection.setPreviousSection(null);
                this.getChildren().remove(currentSection);
                nextSection.payAttention();
            }
            else
            {
                // do nothing
            }

            // do nothing since previous section is empty
            // todo what about title section?
        }
    }

    public Section createNewSectionAfterCaret(Structure structure)
    {
        TextLine currentLine = ((TextLine) CaretHelper.getSelectedCaret().getParent());
        Section currentSection = (Section) currentLine.getParent();

        // todo create a new Section title after current Section

        Section newSection;
        if (structure.equals(Structure.MAIN_TITLE))
        {
            newSection = new MainTitle(theme.getTitleFontFamily(), theme.getTitleFontSize());
        }
        else if (structure.equals(Structure.PARAGRAPH))
        {
            newSection = new Paragraph(theme.getParagraphFontFamily(), theme.getParagraphFontSize());
        }
        else if (structure.equals(Structure.SECTION_TITLE))
        {
            newSection = new SectionTitle(theme.getSectionFontFamily(), theme.getSectionFontSize());
        }
        else if (structure.equals(Structure.SUBSECTION_TITLE))
        {
            newSection = new SubSectionTitle(theme.getSectionFontFamily(), theme.getSubSectionFontSize());
        }
        else if (structure.equals(Structure.SUBSUBSECTION_TITLE))
        {
            newSection = new SubSubSectionTitle(theme.getSectionFontFamily(), theme.getSubSubsectionFontSize());
        }
        else if (structure.equals(Structure.QUOTE))
        {
            newSection = new Quote(theme.getQuoteFontFamily(), theme.getQuoteFontSize());
        }
        else if (structure.equals(Structure.AUTHOR))
        {
            newSection = new Author(theme.getAuthorFontFamily(), theme.getAuthorFontSize());
        }
        else
        {
            throw new IllegalArgumentException("Not found this semantic!" + structure);
        }

        if (hasAuthor())
        {
            if (currentSection instanceof MainTitle)
            {
                currentSection = currentSection.getNextSection();
            }
        }

        Section nextSectionOfCurrentSection = currentSection.getNextSection();
        currentSection.setNextSection(newSection);
        newSection.setPreviousSection(currentSection);
        newSection.setNextSection(nextSectionOfCurrentSection);
        if (nextSectionOfCurrentSection != null)
        {
            nextSectionOfCurrentSection.setPreviousSection(newSection);
        }
        this.getChildren().add(getIndexOfSection(currentSection) + 1, newSection);
        newSection.payAttention();
        return newSection;
    }

    private int getIndexOfSection(Section section)
    {
        int index = 0;
        for (Node n : getChildren())
        {
            if (n instanceof Section)
            {
                if (n == section)
                {
                    return index;
                }
                else
                {
                    index = index + 1;
                }
            }
            else
            {
                throw new IllegalStateException("Someone betrayed us...");
            }
        }
        throw new IllegalArgumentException("Section " + section + " is not in this article.");
    }

    public boolean hasMainTitle()
    {
        for (Node n : getChildren())
        {
            if (n instanceof MainTitle)
            {
                return true;
            }
        }
        return false;
    }

    public void createMainTitle()
    {
        if (this.hasMainTitle())
        {
            //
        }
        else
        {
            Section newSection = new MainTitle(theme.getTitleFontFamily(), theme.getTitleFontSize());
            Section headSection = (Section) this.getChildren().get(0);
            newSection.setNextSection(headSection);
            newSection.setPreviousSection(null);
            headSection.setPreviousSection(newSection);
            this.getChildren().add(0, newSection);
            newSection.payAttention();
        }
    }

    public boolean hasAuthor()
    {
        for (Node n : getChildren())
        {
            if (n instanceof Author)
            {
                return true;
            }
        }
        return false;
    }

    public boolean hasSection()
    {
        for (Node n : getChildren())
        {
            if (n instanceof SectionTitle)
            {
                return true;
            }
        }
        return false;
    }

    public void createAuthor()
    {
        Section newSection = new Author(theme.getAuthorFontFamily(), theme.getAuthorFontSize());
        if (this.hasAuthor())
        {

        }
        else
        {
            if (this.hasMainTitle())
            {
                Section headSection = (Section) this.getChildren().get(0);
                Section nextSection = headSection.getNextSection();
                newSection.setNextSection(nextSection);
                if (nextSection != null)
                {
                    nextSection.setPreviousSection(newSection);
                }
                newSection.setPreviousSection(headSection);
                headSection.setNextSection(newSection);
                this.getChildren().add(1, newSection);
                newSection.payAttention();
            }
            else
            {
                Section headSection = (Section) this.getChildren().get(0);
                newSection.setNextSection(headSection);
                newSection.setPreviousSection(null);
                headSection.setPreviousSection(newSection);
                this.getChildren().add(0, newSection);
                newSection.payAttention();
            }
        }
    }

    public void payAttention()
    {
        TextLine currentLine = ((TextLine) CaretHelper.getSelectedCaret().getParent());
        currentLine.requestFocus();
    }

    public String toXML()
    {
        StringBuilder xml = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?><article>");
        boolean inSection = false;
        Section lastSection = null;
        for (Node sec : getChildren())
        {
            if (sec instanceof MainTitle || sec instanceof Author || sec instanceof Paragraph || sec instanceof Quote)
            {
                xml.append(((Section) sec).getContentInXML());

            }
            else
            {
                if (inSection)
                {
                    xml.append(appendLast(lastSection));
                    inSection = false;
                }
                if (sec instanceof SectionTitle)
                {
                    xml.append(((SectionTitle) sec).getContentInXML());
                    lastSection = (Section) sec;
                    inSection = true;
                }
                else if (sec instanceof SubSectionTitle)
                {
                    xml.append(((SubSectionTitle) sec).getContentInXML());
                    lastSection = (Section) sec;
                    inSection = true;
                }
                else if (sec instanceof SubSubSectionTitle)
                {
                    xml.append(((SubSubSectionTitle) sec).getContentInXML());
                    lastSection = (Section) sec;
                    inSection = true;
                }
            }
        }
        if (inSection)
        {
            xml.append(appendLast(lastSection));
        }
        xml.append("</article>");
        System.out.println(xml);
        return xml.toString();
    }

    private String appendLast(Section section)
    {
        if (section instanceof SectionTitle)
        {
            return "</section>";
        }
        if (section instanceof SubSectionTitle)
        {
            return "</subsection>";
        }
        if (section instanceof SubSubSectionTitle)
        {
            return "</subsubsection>";
        }
        return "";
    }

    public void paint(Structure structure, Color color)
    {
        Painter painter = Painter.getInstance();
        if (structure.equals(Structure.MAIN_TITLE))
        {
            painter.setTitleColor(color);
        }
        else if (structure.equals(Structure.AUTHOR))
        {
            painter.setAuthorColor(color);
        }
        else if (structure.equals(Structure.SECTION_TITLE)
                || structure.equals(Structure.SUBSECTION_TITLE)
                || structure.equals(Structure.SUBSUBSECTION_TITLE))
        {
            painter.setSectionColor(color);
        }
        else if (structure.equals(Structure.PARAGRAPH))
        {
            painter.setParagraphColor(color);
        }
        else if (structure.equals(Structure.QUOTE))
        {
            painter.setQuoteColor(color);
        }
        else
        {
            throw new IllegalArgumentException("Illgeal Structure: " + structure);
        }

    }

    public String getTitle()
    {
        for (Node n : getChildren())
        {
            if (n instanceof MainTitle)
            {
                return n.toString();
            }
        }
        return null;
    }

}
