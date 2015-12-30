package net.antonioshome.nbeditorlib.demo.syntax;

import org.netbeans.editor.Syntax;
import org.netbeans.editor.ext.ExtKit;

import javax.swing.text.Document;


/**
 * SchemeEditorKit is a custom EditorKit for Scheme files.
 *
 * @author $Author: antonio $ Antonio Vieiro (antonio@antonioshome.net)
 * @version $Revision: 1.1.1.1 $
 */
public class SchemeEditorKit
        extends ExtKit {

    public SchemeEditorKit() {
        super();
    }

    public String getContentType() {
        return "text/x-scheme"; // NOI18N
    }

    public Syntax createSyntax(Document document) {
        return new SchemeSyntax();
    }

}
