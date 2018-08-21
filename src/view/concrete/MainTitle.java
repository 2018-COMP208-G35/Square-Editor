package view.concrete;

import javafx.geometry.Pos;
import model.semantic.Semantic;
import view.container.TextSection;
import view.helper.Painter;

public class MainTitle extends TextSection {
    public MainTitle(String fontFamily, double size) {
        super(fontFamily, size);
        this.align(Pos.CENTER);
        this.getStyleClass().add("main-title");
        this.setCurrentSemantic(Semantic.STRONG);
        this.fill.bind(Painter.getInstance().titleColorProperty());
    }

    public String getContentInXML() {
        StringBuilder builder = new StringBuilder("<title>");
        builder.append(super.toString());
        builder.append("</title>");
        return builder.toString();
    }
}