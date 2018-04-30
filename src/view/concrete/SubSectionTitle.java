package view.concrete;

import javafx.geometry.Pos;
import model.semantic.Semantic;
import view.container.TextSection;
import view.helper.Painter;

public class SubSectionTitle extends TextSection {

    public SubSectionTitle(String fontFamily, double size)
    {
        super(fontFamily, size);
        this.align(Pos.TOP_LEFT);
        getStyleClass().addAll("subsection-title", "section");
        currentSemantic = Semantic.STRONG;
        fill.bind(Painter.getInstance().sectionColorProperty());
    }

    @Override
    public String getContentInXML()
    {
        StringBuilder builder = new StringBuilder("<subsection><title>");
        builder.append(super.toString());
        builder.append("</title>");
//        builder.append("</subsection>");
        return builder.toString();
    }

}
