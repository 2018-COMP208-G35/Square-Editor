package view.container;

import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public abstract class Section extends VBox
{
    protected Section previousSection;
    protected Section nextSection;

    public Section()
    {
        this.init();
    }

    private void init()
    {
        this.setUpHandlers();
    }

    public Section(Section previousSection, Section nextSection)
    {
        this.previousSection = previousSection;
        this.nextSection = nextSection;
        this.init();
    }

    public Section getPreviousSection()
    {
        return this.previousSection;
    }

    public void setPreviousSection(Section previousSection)
    {
        this.previousSection = previousSection;
    }

    public Section getNextSection()
    {
        return this.nextSection;
    }

    public void setNextSection(Section nextSection)
    {
        this.nextSection = nextSection;
    }

    protected abstract void payAttention();

    protected abstract void payAttentionToEnd();

    private void setUpHandlers()
    {
        this.setOnKeyPressed(this::getOnKeyPressed);
        this.setOnMousePressed(this::getOnMousePressed);
    }

    protected abstract void getOnMousePressed(MouseEvent var1);

    protected abstract void getOnKeyPressed(KeyEvent var1);

    public abstract boolean isEmpty();

    public abstract String getContentInXML();
}
