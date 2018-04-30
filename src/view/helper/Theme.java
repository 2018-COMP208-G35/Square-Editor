package view.helper;

/**
 * A new theme for the editor.
 */
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

    public Theme(String titleFontFamily, double titleFontSize,
                 String authorFontFamily, double authorFontSize,
                 String sectionFontFamily, double sectionFontSize, double subSectionFontSize, double subSubsectionFontSize,
                 String paragraphFontFamily, double paragraphFontSize, String quoteFontFamily, double quoteFontSize)
    {
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

    public String getTitleFontFamily()
    {
        return titleFontFamily;
    }

    public double getTitleFontSize()
    {
        return titleFontSize;
    }

    public String getAuthorFontFamily()
    {
        return authorFontFamily;
    }

    public double getAuthorFontSize()
    {
        return authorFontSize;
    }

    public String getSectionFontFamily()
    {
        return sectionFontFamily;
    }

    public double getSectionFontSize()
    {
        return sectionFontSize;
    }

    public double getSubSectionFontSize()
    {
        return subSectionFontSize;
    }

    public double getSubSubsectionFontSize()
    {
        return subSubsectionFontSize;
    }

    public String getParagraphFontFamily()
    {
        return paragraphFontFamily;
    }

    public double getParagraphFontSize()
    {
        return paragraphFontSize;
    }

    public String getQuoteFontFamily()
    {
        return quoteFontFamily;
    }

    public double getQuoteFontSize()
    {
        return quoteFontSize;
    }
}
