package mintwire;

import java.io.IOException;
import org.fife.rsta.ac.LanguageSupport;
import org.fife.rsta.ac.LanguageSupportFactory;
import org.fife.rsta.ac.c.CLanguageSupport;
import org.fife.rsta.ac.css.CssLanguageSupport;
import org.fife.rsta.ac.java.JavaLanguageSupport;
import org.fife.rsta.ac.js.JavaScriptLanguageSupport;
import org.fife.rsta.ac.jsp.JspLanguageSupport;
import org.fife.rsta.ac.perl.PerlLanguageSupport;
import org.fife.rsta.ac.php.PhpLanguageSupport;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;

public class RSTALanguageSupport {

    public static void getLanguageSupport(RSyntaxTextArea textArea) {
        LanguageSupportFactory lsf = LanguageSupportFactory.get();
        lsf.unregister(textArea);
        lsf.register(textArea);
        
        
        LanguageSupport languageSupport;
        switch (textArea.getSyntaxEditingStyle()) {
            case "text/c":
                languageSupport = lsf.getSupportFor(SyntaxConstants.SYNTAX_STYLE_C);
                break;

            case "text/css":
                languageSupport = lsf.getSupportFor(SyntaxConstants.SYNTAX_STYLE_CSS);
                break;


            case "text/perl":
                languageSupport = lsf.getSupportFor(SyntaxConstants.SYNTAX_STYLE_PERL);
                break;
            case "text/php":
                languageSupport = lsf.getSupportFor(SyntaxConstants.SYNTAX_STYLE_PHP);
                break;
            case "text/javascript":
                languageSupport = lsf.getSupportFor(SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT);
               
                JavaScriptLanguageSupport specificSupport = (JavaScriptLanguageSupport) languageSupport;
                try {
                    specificSupport.getJarManager().addClassFileSource(new JDK9ClasspathLibraryInfo());
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
                break;
            case "text/java":
                languageSupport = lsf.getSupportFor(SyntaxConstants.SYNTAX_STYLE_JAVA);
                JavaLanguageSupport specificSupport2 = (JavaLanguageSupport) languageSupport;
                try {
                    specificSupport2.getJarManager().addClassFileSource(new JDK9ClasspathLibraryInfo());
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
                break;
            case "text/jsp":
                languageSupport = lsf.getSupportFor(SyntaxConstants.SYNTAX_STYLE_JSP);
                break;
            case "text/html":
                languageSupport = lsf.getSupportFor(SyntaxConstants.SYNTAX_STYLE_HTML);
                break;
            case "text/xml":
                languageSupport = lsf.getSupportFor(SyntaxConstants.SYNTAX_STYLE_XML);
                break;
            default:
                return;

        }

    }

}
