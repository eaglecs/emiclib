package basecode.com.ui.util;


public class TemplateUtil {
    private TemplateUtil() {

    }

    public static Phrase string(CharSequence charSequence) {
        return Phrase.from(charSequence);
    }

    /**
     * create Spannable from text and style span
     *
     * @param text   text need formating
     * @param styles StyleSpan, ForegroundColorSpan, UnderlineSpan, BackgroundColorSpan, StyleSpan, RelativeSizeSpan, ImageSpan, TextAppearanceSpan, AlignmentSpan.Standard, CustomBackgroundSpan, SubscriptSpan, SuperscriptSpan, QuoteSpan, StrikethroughSpan, TypefaceSpan, URLSpan, CustomTypefaceSpan, CustomAlignmentSpan,
     * @return
     */
    public static CharSequence createSpannable(String text, Object... styles) {
        return new Spanny(text, styles);
    }
}
