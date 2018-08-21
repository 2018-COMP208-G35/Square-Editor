package view.helper;

public class Theme {
    private String titleFontFamily;
    private double titleFontSize;
    private String authorFontFamily;
    private double authorFontSize;
    private String sectionFontFamily;
    private double sectionFontSize;
    private double subSectionFontSize;
    private double subSubsectionFontSize;
    private String paragraphFontFamily;
    private double paragraphFontSize;
    private String quoteFontFamily;
    private double quoteFontSize;

    public Theme(String titleFontFamily, double titleFontSize, String authorFontFamily, double authorFontSize, String sectionFontFamily, double sectionFontSize, double subSectionFontSize, double subSubsectionFontSize, String paragraphFontFamily, double paragraphFontSize, String quoteFontFamily, double quoteFontSize) {
        this.titleFontFamily = titleFontFamily;
        this.titleFontSize = titleFontSize;
        this.authorFontFamily = authorFontFamily;
        this.authorFontSize = authorFontSize;
        this.sectionFontFamily = sectionFontFamily;
        this.sectionFontSize = sectionFontSize;
        this.subSectionFontSize = subSectionFontSize;
        this.subSubsectionFontSize = subSubsectionFontSize;
        this.paragraphFontFamily = paragraphFontFamily;
        this.paragraphFontSize = paragraphFontSize;
        this.quoteFontFamily = quoteFontFamily;
        this.quoteFontSize = quoteFontSize;
    }

    public String getTitleFontFamily() {
        return this.titleFontFamily;
    }

    public double getTitleFontSize() {
        return this.titleFontSize;
    }

    public String getAuthorFontFamily() {
        return this.authorFontFamily;
    }

    public double getAuthorFontSize() {
        return this.authorFontSize;
    }

    public String getSectionFontFamily() {
        return this.sectionFontFamily;
    }

    public double getSectionFontSize() {
        return this.sectionFontSize;
    }

    public double getSubSectionFontSize() {
        return this.subSectionFontSize;
    }

    public double getSubSubsectionFontSize() {
        return this.subSubsectionFontSize;
    }

    public String getParagraphFontFamily() {
        return this.paragraphFontFamily;
    }

    public double getParagraphFontSize() {
        return this.paragraphFontSize;
    }

    public String getQuoteFontFamily() {
        return this.quoteFontFamily;
    }

    public double getQuoteFontSize() {
        return this.quoteFontSize;
    }
}