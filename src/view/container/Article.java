package view.container;

import java.util.Iterator;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import model.semantic.Semantic;
import view.Caret;
import view.concrete.Author;
import view.concrete.MainTitle;
import view.concrete.Paragraph;
import view.concrete.Quote;
import view.concrete.SectionTitle;
import view.concrete.SubSectionTitle;
import view.concrete.SubSubSectionTitle;
import view.helper.CaretHelper;
import view.helper.Page;
import view.helper.Painter;
import view.helper.Theme;

public class Article extends VBox
{
    private Theme theme;

    public Article(Theme theme)
    {
        this.theme = theme;
        this.setUpLayout();
        this.setUpKeyListener();
        TextSection textSection = new Paragraph(theme.getParagraphFontFamily(), theme.getParagraphFontSize());
        textSection.payAttention();
        this.getChildren().add(textSection);
    }

    private void setUpLayout()
    {
        this.getStylesheets().add("/css/javaFX/article.css");
        this.getStyleClass().add("paper");
        this.setPadding(new Insets(Page.getPagePadding()));
    }

    private void setUpKeyListener()
    {
        this.addEventFilter(KeyEvent.KEY_PRESSED, this::getOnKeyPressed);
    }

    private void getOnKeyPressed(KeyEvent keyEvent)
    {
        KeyCode keyCode = keyEvent.getCode();
        String text = keyEvent.getText();
        System.out.println("Article: Key: " + keyCode + ", Text: " + text);
        TextLine currentLine = (TextLine) CaretHelper.getInstance().getSelectedCaret().getParent();
        Section currentSection = (Section) currentLine.getParent();
        if (!keyEvent.isMetaDown() && !keyEvent.isControlDown())
        {
            if (!keyCode.isModifierKey())
            {
                if (keyCode.equals(KeyCode.ESCAPE))
                {
                    if (currentSection instanceof Paragraph)
                    {
                        ((TextSection) currentSection).setCurrentSemantic(Semantic.NORMAL);
                        keyEvent.consume();
                    }
                } else if (keyCode.isArrowKey() && !keyCode.equals(KeyCode.UP) && !keyCode.equals(KeyCode.DOWN) && !keyCode.equals(KeyCode.LEFT) && keyCode.equals(KeyCode.RIGHT))
                {
                    ;
                }
            }
        } else if (keyCode.isDigitKey())
        {
            if (keyCode.equals(KeyCode.DIGIT1))
            {
                this.createNewSectionAfterCaret(Article.Structure.SECTION_TITLE);
            } else if (keyCode.equals(KeyCode.DIGIT2))
            {
                this.createNewSectionAfterCaret(Article.Structure.SUBSECTION_TITLE);
            } else if (keyCode.equals(KeyCode.DIGIT3))
            {
                this.createNewSectionAfterCaret(Article.Structure.SUBSUBSECTION_TITLE);
            }
        } else if (keyCode.equals(KeyCode.B))
        {
            if (currentSection instanceof Paragraph)
            {
                if (((TextSection) currentSection).getCurrentSemantic().equals(Semantic.STRONG))
                {
                    ((TextSection) currentSection).setCurrentSemantic(Semantic.NORMAL);
                } else
                {
                    ((TextSection) currentSection).setCurrentSemantic(Semantic.STRONG);
                }
            }
        } else if (keyCode.equals(KeyCode.I))
        {
            if (currentSection instanceof Paragraph)
            {
                if (((TextSection) currentSection).getCurrentSemantic() == Semantic.EMPHASIS)
                {
                    ((TextSection) currentSection).setCurrentSemantic(Semantic.NORMAL);
                } else
                {
                    ((TextSection) currentSection).setCurrentSemantic(Semantic.EMPHASIS);
                }
            }
        } else if (keyCode.equals(KeyCode.K))
        {
            this.createNewSectionAfterCaret(Article.Structure.QUOTE);
        }

        if (keyCode.isWhitespaceKey())
        {
            if (keyCode.equals(KeyCode.ENTER))
            {
                if (currentSection instanceof Paragraph)
                {
                    Word word = ((Paragraph) currentSection).getAllContentNodesAfterCaret();
                    Section newSection = this.createNewSectionAfterCaret(Article.Structure.PARAGRAPH);
                    ((Paragraph) newSection).add(word);
                    newSection.payAttention();
                } else
                {
                    Section var10 = this.createNewSectionAfterCaret(Article.Structure.PARAGRAPH);
                }
            } else if (keyCode.equals(KeyCode.SPACE))
            {
                ;
            }
        } else if (!keyCode.equals(KeyCode.SHIFT))
        {
            if (keyCode.equals(KeyCode.BACK_SPACE))
            {
                if (currentSection.isEmpty())
                {
                    this.removeSection(currentSection);
                    keyEvent.consume();
                } else if (currentSection instanceof Paragraph && currentLine.getCaretPosition() == 0 && ((TextSection) currentSection).getLineIndexInThisParagraph(currentLine) == 0)
                {
                    Paragraph previousSection = (Paragraph) currentSection.getPreviousSection();
                    if (previousSection != null)
                    {
                        Word wordsInCurrentSection = ((Paragraph) currentSection).getAllWords();
                        previousSection.payAttentionToEnd();
                        TextLine lastLineOfPreviousSection = (TextLine) previousSection.getChildren().get(previousSection.getNumberOfLines() - 1);
                        Caret lastCaret = lastLineOfPreviousSection.getCaret(lastLineOfPreviousSection.getNumberOfCaret() - 1);
                        previousSection.add(wordsInCurrentSection);
                        this.removeSection(currentSection);
                        lastCaret.show();
                        keyEvent.consume();
                    }
                }
            } else if (keyCode.isLetterKey())
            {
                ;
            }
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
            } else
            {
                previousSection.setNextSection((Section) null);
            }

            this.getChildren().remove(currentSection);
            previousSection.payAttentionToEnd();
        } else if (nextSection != null)
        {
            nextSection.setPreviousSection((Section) null);
            this.getChildren().remove(currentSection);
            nextSection.payAttention();
        }

    }

    public Section createNewSectionAfterCaret(Article.Structure structure)
    {
        TextLine currentLine = (TextLine) CaretHelper.getInstance().getSelectedCaret().getParent();
        Section currentSection = (Section) currentLine.getParent();
        Object newSection;
        if (structure.equals(Article.Structure.MAIN_TITLE))
        {
            newSection = new MainTitle(this.theme.getTitleFontFamily(), this.theme.getTitleFontSize());
        } else if (structure.equals(Article.Structure.PARAGRAPH))
        {
            newSection = new Paragraph(this.theme.getParagraphFontFamily(), this.theme.getParagraphFontSize());
        } else if (structure.equals(Article.Structure.SECTION_TITLE))
        {
            newSection = new SectionTitle(this.theme.getSectionFontFamily(), this.theme.getSectionFontSize());
        } else if (structure.equals(Article.Structure.SUBSECTION_TITLE))
        {
            newSection = new SubSectionTitle(this.theme.getSectionFontFamily(), this.theme.getSubSectionFontSize());
        } else if (structure.equals(Article.Structure.SUBSUBSECTION_TITLE))
        {
            newSection = new SubSubSectionTitle(this.theme.getSectionFontFamily(), this.theme.getSubSubsectionFontSize());
        } else if (structure.equals(Article.Structure.QUOTE))
        {
            newSection = new Quote(this.theme.getQuoteFontFamily(), this.theme.getQuoteFontSize());
        } else
        {
            if (!structure.equals(Article.Structure.AUTHOR))
            {
                throw new IllegalArgumentException("Not found this semantic!" + structure);
            }

            newSection = new Author(this.theme.getAuthorFontFamily(), this.theme.getAuthorFontSize());
        }

        if (this.hasAuthor() && currentSection instanceof MainTitle)
        {
            currentSection = currentSection.getNextSection();
        }

        Section nextSectionOfCurrentSection = currentSection.getNextSection();
        currentSection.setNextSection((Section) newSection);
        ((Section) newSection).setPreviousSection(currentSection);
        ((Section) newSection).setNextSection(nextSectionOfCurrentSection);
        if (nextSectionOfCurrentSection != null)
        {
            nextSectionOfCurrentSection.setPreviousSection((Section) newSection);
        }

        this.getChildren().add(this.getIndexOfSection(currentSection) + 1, (Node) newSection);
        ((Section) newSection).payAttention();
        return (Section) newSection;
    }

    private int getIndexOfSection(Section section)
    {
        int index = 0;

        for (Iterator var3 = this.getChildren().iterator(); var3.hasNext(); ++index)
        {
            Node n = (Node) var3.next();
            if (!(n instanceof Section))
            {
                throw new IllegalStateException("Someone betrayed us...");
            }

            if (n == section)
            {
                return index;
            }
        }

        throw new IllegalArgumentException("Section " + section + " is not in this article.");
    }

    public boolean hasMainTitle()
    {
        Iterator var1 = this.getChildren().iterator();

        Node n;
        do
        {
            if (!var1.hasNext())
            {
                return false;
            }

            n = (Node) var1.next();
        } while (!(n instanceof MainTitle));

        return true;
    }

    public void createMainTitle()
    {
        if (!this.hasMainTitle())
        {
            Section newSection = new MainTitle(this.theme.getTitleFontFamily(), this.theme.getTitleFontSize());
            Section headSection = (Section) this.getChildren().get(0);
            newSection.setNextSection(headSection);
            newSection.setPreviousSection((Section) null);
            headSection.setPreviousSection(newSection);
            this.getChildren().add(0, newSection);
            newSection.payAttention();
        }

    }

    public boolean hasAuthor()
    {
        Iterator var1 = this.getChildren().iterator();

        Node n;
        do
        {
            if (!var1.hasNext())
            {
                return false;
            }

            n = (Node) var1.next();
        } while (!(n instanceof Author));

        return true;
    }

    public boolean hasSection()
    {
        Iterator var1 = this.getChildren().iterator();

        Node n;
        do
        {
            if (!var1.hasNext())
            {
                return false;
            }

            n = (Node) var1.next();
        } while (!(n instanceof SectionTitle));

        return true;
    }

    public void createAuthor()
    {
        Section newSection = new Author(this.theme.getAuthorFontFamily(), this.theme.getAuthorFontSize());
        if (!this.hasAuthor())
        {
            Section headSection;
            if (this.hasMainTitle())
            {
                headSection = (Section) this.getChildren().get(0);
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
            } else
            {
                headSection = (Section) this.getChildren().get(0);
                newSection.setNextSection(headSection);
                newSection.setPreviousSection((Section) null);
                headSection.setPreviousSection(newSection);
                this.getChildren().add(0, newSection);
                newSection.payAttention();
            }
        }

    }

    public void payAttention()
    {
        TextLine currentLine = (TextLine) CaretHelper.getInstance().getSelectedCaret().getParent();
        currentLine.requestFocus();
    }

    public String toXML()
    {
        StringBuilder xml = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?><article>");
        boolean inSection = false;
        Section lastSection = null;
        Iterator var4 = this.getChildren().iterator();

        while (true)
        {
            while (var4.hasNext())
            {
                Node sec = (Node) var4.next();
                if (!(sec instanceof MainTitle) && !(sec instanceof Author) && !(sec instanceof Paragraph) && !(sec instanceof Quote))
                {
                    if (inSection)
                    {
                        xml.append(this.appendLast(lastSection));
                        inSection = false;
                    }

                    if (sec instanceof SectionTitle)
                    {
                        xml.append(((SectionTitle) sec).getContentInXML());
                        lastSection = (Section) sec;
                        inSection = true;
                    } else if (sec instanceof SubSectionTitle)
                    {
                        xml.append(((SubSectionTitle) sec).getContentInXML());
                        lastSection = (Section) sec;
                        inSection = true;
                    } else if (sec instanceof SubSubSectionTitle)
                    {
                        xml.append(((SubSubSectionTitle) sec).getContentInXML());
                        lastSection = (Section) sec;
                        inSection = true;
                    }
                } else
                {
                    xml.append(((Section) sec).getContentInXML());
                }
            }

            if (inSection)
            {
                xml.append(this.appendLast(lastSection));
            }

            xml.append("</article>");
            System.out.println(xml);
            return xml.toString();
        }
    }

    private String appendLast(Section section)
    {
        if (section instanceof SectionTitle)
        {
            return "</section>";
        } else if (section instanceof SubSectionTitle)
        {
            return "</subsection>";
        } else
        {
            return section instanceof SubSubSectionTitle ? "</subsubsection>" : "";
        }
    }

    public void paint(Article.Structure structure, Color color)
    {
        Painter painter = Painter.getInstance();
        if (structure.equals(Article.Structure.MAIN_TITLE))
        {
            painter.setTitleColor(color);
        } else if (structure.equals(Article.Structure.AUTHOR))
        {
            painter.setAuthorColor(color);
        } else if (!structure.equals(Article.Structure.SECTION_TITLE) && !structure.equals(Article.Structure.SUBSECTION_TITLE) && !structure.equals(Article.Structure.SUBSUBSECTION_TITLE))
        {
            if (structure.equals(Article.Structure.PARAGRAPH))
            {
                painter.setParagraphColor(color);
            } else
            {
                if (!structure.equals(Article.Structure.QUOTE))
                {
                    throw new IllegalArgumentException("Illegal Structure: " + structure);
                }

                painter.setQuoteColor(color);
            }
        } else
        {
            painter.setSectionColor(color);
        }

    }

    public String getTitle()
    {
        Iterator var1 = this.getChildren().iterator();

        Node n;
        do
        {
            if (!var1.hasNext())
            {
                return null;
            }

            n = (Node) var1.next();
        } while (!(n instanceof MainTitle));

        return n.toString();
    }

    public Section getCaretSection()
    {
        Caret caret = CaretHelper.getInstance().getSelectedCaret();
        if (caret != null)
        {
            return (Section) caret.getParent().getParent();
        } else
        {
            throw new IllegalStateException("Caret not found!");
        }
    }

    public static enum Structure
    {
        MAIN_TITLE,
        SECTION_TITLE,
        SUBSECTION_TITLE,
        SUBSUBSECTION_TITLE,
        PARAGRAPH,
        QUOTE,
        AUTHOR;

        private Structure()
        {
        }
    }
}
