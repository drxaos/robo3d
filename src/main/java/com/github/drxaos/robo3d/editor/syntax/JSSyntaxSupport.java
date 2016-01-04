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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import javax.swing.text.BadLocationException;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import javax.swing.text.JTextComponent;
import org.netbeans.editor.SyntaxSupport;
import org.netbeans.editor.BaseDocument;
import org.netbeans.editor.TokenItem;
import org.netbeans.editor.Utilities;
import org.netbeans.editor.TextBatchProcessor;
import org.netbeans.editor.FinderFactory;
import org.netbeans.editor.Syntax;
import org.netbeans.editor.Analyzer;
import org.netbeans.editor.TokenID;
import org.netbeans.editor.TokenContextPath;
import org.netbeans.editor.ext.ExtSyntaxSupport;

/**
 * Support methods for syntax analyzes
 *
 * @author Marek Fukala
 * @version 1.00
 */

public class JSSyntaxSupport extends ExtSyntaxSupport {
    
    private static final char[] COMMAND_SEPARATOR_CHARS = new char[] {
                ';', '{', '}'
            };
    
    public JSSyntaxSupport(BaseDocument doc) {
        super(doc);
    }
    
    /** Return the position of the last command separator before
     * the given position.
     */
    public int getLastCommandSeparator(int pos)
    throws BadLocationException {
        if (pos == 0)
            return 0;
        TextBatchProcessor tbp = new TextBatchProcessor() {
            public int processTextBatch(BaseDocument doc, int startPos, int endPos,
                    boolean lastBatch) {
                try {
                    int[] blks = getCommentBlocks(endPos, startPos);
                    FinderFactory.CharArrayBwdFinder cmdFinder
                            = new FinderFactory.CharArrayBwdFinder(COMMAND_SEPARATOR_CHARS);
                    int lastSeparatorOffset = findOutsideBlocks(cmdFinder, startPos, endPos, blks);
                    if (lastSeparatorOffset<1) return lastSeparatorOffset;
                    TokenID separatorID = getTokenID(lastSeparatorOffset);
                    if (separatorID.getNumericID() == JSTokenContext.RBRACE_ID) {
                        int matchingBrkPos[] = findMatchingBlock(lastSeparatorOffset, true);
                        if (matchingBrkPos != null){
                            int prev = Utilities.getFirstNonWhiteBwd(getDocument(), matchingBrkPos[0]);
                            if (prev > -1 && getTokenID(prev).getNumericID() == JSTokenContext.RBRACKET_ID){
                                return getLastCommandSeparator(prev);
                            }
                        }
                    }
                    if (separatorID.getNumericID() != JSTokenContext.LBRACE_ID &&
                            separatorID.getNumericID() != JSTokenContext.RBRACE_ID &&
                            separatorID.getNumericID() != JSTokenContext.SEMICOLON_ID){
                        lastSeparatorOffset = processTextBatch(doc, lastSeparatorOffset, 0, lastBatch);
                    }
                    return lastSeparatorOffset;
                } catch (BadLocationException e) {
                    e.printStackTrace();
                    return -1;
                }
            }
        };
        int lastPos = getDocument().processText(tbp, pos, 0);
        
        //ensure we return last command separator from last
        //block of java tokens from <startPos;endPos> offset interval
        //AFAIK this is currently needed only for JSP code completion
        TokenItem item = getTokenChain(pos - 1, pos);
        //go back throught the token chain and try to find last java token
        do {
            int tokenOffset = item.getOffset();
            if(lastPos != -1 && tokenOffset < lastPos) break; //stop backtracking if we met the lastPos
            //test token type
            if(!item.getTokenContextPath().contains(JSTokenContext.contextPath)) {
                //return offset of last java token - this token isn't already a java token so return offset of next token
                lastPos = item.getNext() != null ? item.getNext().getOffset() : item.getOffset() + item.getImage().length();
                break;
            }
        } while( (item = item.getPrevious()) != null);
        
        return lastPos;
    }
    
    
}
