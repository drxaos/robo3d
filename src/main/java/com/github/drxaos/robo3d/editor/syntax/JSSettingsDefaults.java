/*
 * The contents of this file are subject to the terms of the Common Development
 * and Distribution License (the License). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the License at http://www.netbeans.org/cddl.html
 * or http://www.netbeans.org/cddl.txt.
 * 
 * When distributing Covered Code, include this CDDL Header Notice in each file
 * and include the License file at http://www.netbeans.org/cddl.txt.
 * If applicable, add the following below the CDDL Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * The Original Software is NetBeans. The Initial Developer of the Original
 * Software is Sun Microsystems, Inc. Portions Copyright 1997-2006 Sun
 * Microsystems, Inc. All Rights Reserved.
 */

package com.github.drxaos.robo3d.editor.syntax;

import java.awt.Color;
import java.awt.Font;
import org.netbeans.editor.Coloring;
import org.netbeans.editor.Settings;
import org.netbeans.editor.SettingsDefaults;
import org.netbeans.editor.SettingsUtil;
import org.netbeans.editor.TokenCategory;
import org.netbeans.editor.TokenContextPath;
import org.netbeans.editor.ext.ExtSettingsDefaults;
import org.netbeans.modules.javascript.editor.syntax.JSLayerTokenContext;
import org.netbeans.modules.javascript.editor.syntax.JSTokenContext;

/**
 * Initializer for the javascript editor settings.
 * 
 * @author Marek Fukala
  *
 */
public class JSSettingsDefaults extends ExtSettingsDefaults {
    
    static class JSTokenColoringInitializer
    extends SettingsUtil.TokenColoringInitializer {

        Font boldFont = SettingsDefaults.defaultFont.deriveFont(Font.BOLD);
        Font italicFont = SettingsDefaults.defaultFont.deriveFont(Font.ITALIC);
        Settings.Evaluator boldSubst = new SettingsUtil.FontStylePrintColoringEvaluator(Font.BOLD);
        Settings.Evaluator italicSubst = new SettingsUtil.FontStylePrintColoringEvaluator(Font.ITALIC);
        Settings.Evaluator lightGraySubst = new SettingsUtil.ForeColorPrintColoringEvaluator(new Color(120, 120, 120));

        Coloring commentColoring = new Coloring(null, new Color(115, 115, 115), null);

        Coloring numbersColoring = new Coloring(null, new Color(120, 0, 0), null);

        public JSTokenColoringInitializer() {
            super(JSTokenContext.context);
        }

        public Object getTokenColoring(TokenContextPath tokenContextPath,
        TokenCategory tokenIDOrCategory, boolean printingSet) {
            if (!printingSet) {
                switch (tokenIDOrCategory.getNumericID()) {
                    case JSTokenContext.WHITESPACE_ID:
                    case JSTokenContext.IDENTIFIER_ID:
                    case JSTokenContext.OPERATORS_ID:
                        return SettingsDefaults.emptyColoring;

                    case JSTokenContext.ERRORS_ID:
                        return new Coloring(null, Color.white, Color.red);

                    case JSTokenContext.KEYWORDS_ID:
                        return new Coloring(boldFont, Coloring.FONT_MODE_APPLY_STYLE,
                            new Color(0, 0, 153), null);


                    case JSTokenContext.LINE_COMMENT_ID:
                    case JSTokenContext.BLOCK_COMMENT_ID:
                        return commentColoring;

                    case JSTokenContext.CHAR_LITERAL_ID:
                        return new Coloring(null, new Color(0, 111, 0), null);

                    case JSTokenContext.STRING_LITERAL_ID:
                        return new Coloring(null, new Color(153, 0, 107), null);

                    case JSTokenContext.NUMERIC_LITERALS_ID:
                        return numbersColoring;

                    case JSTokenContext.ANNOTATION_ID: // JDK 1.5 annotations
                        return new Coloring(null, new Color(0, 111, 0), null);

                }

            } else { // printing set
                switch (tokenIDOrCategory.getNumericID()) {
                    case JSTokenContext.LINE_COMMENT_ID:
                    case JSTokenContext.BLOCK_COMMENT_ID:
                         return lightGraySubst; // print fore color will be gray

                    default:
                         return SettingsUtil.defaultPrintColoringEvaluator;
                }

            }

            return null;

        }

    }

    static class JSLayerTokenColoringInitializer
    extends SettingsUtil.TokenColoringInitializer {

        Font boldFont = SettingsDefaults.defaultFont.deriveFont(Font.BOLD);
        Settings.Evaluator italicSubst = new SettingsUtil.FontStylePrintColoringEvaluator(Font.ITALIC);

        public JSLayerTokenColoringInitializer() {
            super(JSLayerTokenContext.context);
        }

        public Object getTokenColoring(TokenContextPath tokenContextPath,
        TokenCategory tokenIDOrCategory, boolean printingSet) {
            if (!printingSet) {
                switch (tokenIDOrCategory.getNumericID()) {
                    case JSLayerTokenContext.METHOD_ID:
                        return new Coloring(boldFont, Coloring.FONT_MODE_APPLY_STYLE,
                            null, null);

                }

            } else { // printing set
                switch (tokenIDOrCategory.getNumericID()) {
                    case JSLayerTokenContext.METHOD_ID:
                        return italicSubst;

                    default:
                         return SettingsUtil.defaultPrintColoringEvaluator;
                }

            }

            return null;
        }

    }
}
