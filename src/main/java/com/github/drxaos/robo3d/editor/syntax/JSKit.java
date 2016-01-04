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

import org.netbeans.editor.BaseDocument;
import org.netbeans.editor.Formatter;
import org.netbeans.editor.Syntax;
import org.netbeans.editor.SyntaxSupport;
import org.netbeans.editor.ext.ExtKit;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.Document;
import javax.swing.text.TextAction;

/**
 * Editor kit implementation for JavaScript content type
 *
 * @author Miloslav Metelka, Marek Fukala
 * @version 1.00
 */

public class JSKit extends ExtKit {

    static final long serialVersionUID = -1381945567613910297L; //FIXME

    public static final String JAVASCRIPT_MIME_TYPE = "text/x-javascript"; // NOI18N

    public JSKit() {
        super();
    }

    public String getContentType() {
        return JAVASCRIPT_MIME_TYPE;
    }

    protected Action[] createActions() {
        Action[] jsActions = new Action[]{
                new JSDefaultKeyTypedAction()
        };
        return TextAction.augmentList(super.createActions(), jsActions);
    }

    /**
     * Create new instance of syntax coloring scanner
     *
     * @param doc document to operate on. It can be null in the cases the syntax
     *            creation is not related to the particular document
     */
    public Syntax createSyntax(Document doc) {
        return new JSSyntax();
    }

    /**
     * Create syntax support
     */
    public SyntaxSupport createSyntaxSupport(BaseDocument doc) {
        return new JSSyntaxSupport(doc);
    }

    public Formatter createFormatter() {
        return new JSFormatter(this.getClass());
    }

    /**
     * Called after the kit is installed into JEditorPane
     */
    public void install(javax.swing.JEditorPane c) {
        super.install(c);
        //c.setTransferHandler(new HTMLTransferHandler());
    }

    public static class JSDefaultKeyTypedAction extends ExtDefaultKeyTypedAction {

        protected void insertString(BaseDocument doc, int dotPos,
                                    Caret caret, String str,
                                    boolean overwrite) throws BadLocationException {
            super.insertString(doc, dotPos, caret, str, overwrite);
            //HTMLAutoCompletion.charInserted(doc, dotPos, caret, str.charAt(0));
        }

    }


}
 