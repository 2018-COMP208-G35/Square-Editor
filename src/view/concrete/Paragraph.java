package view.concrete;

import javafx.geometry.Pos;
import model.semantic.Semantic;
import view.container.TextSection;
import view.helper.Painter;

public class Paragraph extends TextSection {
    public Paragraph(String fontFamily, double size) {
        super(fontFamily, size);
        this.align(Pos.TOP_LEFT);
        this.getStyleClass().add("paragraph");
        this.setCurrentSemantic(Semantic.NORMAL);
        this.fill.bind(Painter.getInstance().paragraphColorProperty());
    }

    public String getContentInXML() {
        StringBuilder builder = new StringBuilder("<paragraph>");
        builder.append(super.getContentInXML());
        builder.append("</paragraph>");
        return builder.toString();
    }
}