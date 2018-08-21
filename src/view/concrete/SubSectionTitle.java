package view.concrete;

import javafx.geometry.Pos;
import model.semantic.Semantic;
import view.container.TextSection;
import view.helper.Painter;

public class SubSectionTitle extends TextSection {
    public SubSectionTitle(String fontFamily, double size) {
        super(fontFamily, size);
        this.align(Pos.TOP_LEFT);
        this.getStyleClass().addAll(new String[]{"subsection-title", "section"});
        this.currentSemantic = Semantic.STRONG;
        this.fill.bind(Painter.getInstance().sectionColorProperty());
    }

    public String getContentInXML() {
        StringBuilder builder = new StringBuilder("<subsection><title>");
        builder.append(super.toString());
        builder.append("</title>");
        return builder.toString();
    }
}