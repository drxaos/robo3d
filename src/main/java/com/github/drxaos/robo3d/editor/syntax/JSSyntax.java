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

import org.netbeans.editor.Syntax;
import org.netbeans.editor.TokenID;

/**
 * Syntax analyzer for JavaScript source files.
 * Tokens and internal states are given below.
 *
 * @author Miloslav Metelka, Marek Fukala
 */

public class JSSyntax extends Syntax {
    
    // Internal states
    private static final int ISI_WHITESPACE = 2; // inside white space
    private static final int ISI_LINE_COMMENT = 4; // inside line comment //
    private static final int ISI_BLOCK_COMMENT = 5; // inside block comment /* ... */
    private static final int ISI_STRING = 6; // inside string constant
    private static final int ISI_STRING_A_BSLASH = 7; // inside string constant after backslash
    private static final int ISI_CHAR = 8; // inside char constant
    private static final int ISI_CHAR_A_BSLASH = 9; // inside char constant after backslash
    private static final int ISI_IDENTIFIER = 10; // inside identifier
    private static final int ISA_SLASH = 11; // slash char
    private static final int ISA_EQ = 12; // after '='
    private static final int ISA_GT = 13; // after '>'
    private static final int ISA_GTGT = 14; // after '>>'
    private static final int ISA_GTGTGT = 15; // after '>>>'
    private static final int ISA_LT = 16; // after '<'
    private static final int ISA_LTLT = 17; // after '<<'
    private static final int ISA_PLUS = 18; // after '+'
    private static final int ISA_MINUS = 19; // after '-'
    private static final int ISA_STAR = 20; // after '*'
    private static final int ISA_STAR_I_BLOCK_COMMENT = 21; // after '*'
    private static final int ISA_PIPE = 22; // after '|'
    private static final int ISA_PERCENT = 23; // after '%'
    private static final int ISA_AND = 24; // after '&'
    private static final int ISA_XOR = 25; // after '^'
    private static final int ISA_EXCLAMATION = 26; // after '!'
    private static final int ISA_ZERO = 27; // after '0'
    private static final int ISI_INT = 28; // integer number
    private static final int ISI_OCTAL = 29; // octal number
    private static final int ISI_DOUBLE = 30; // double number
    private static final int ISI_DOUBLE_EXP = 31; // double number
    private static final int ISI_HEX = 32; // hex number
    private static final int ISA_DOT = 33; // after '.'
    
    private boolean isJava15 = true;
    
    //when set to true, the parser divides java block comment by lines (a performance fix of #55628 for JSPs)
    private boolean useInJsp = false;
    
    public JSSyntax() {
        tokenContextPath = JSTokenContext.contextPath;
    }
    
    public JSSyntax(String sourceLevel) {
        this();
        if (sourceLevel != null) {
            try {
                isJava15 = Float.parseFloat(sourceLevel) >= 1.5;
            } catch (NumberFormatException e) {
                // leave the default
            }
        }
    }
    
    public JSSyntax(String sourceLevel, boolean useInJsp) {
        this(sourceLevel);
        this.useInJsp = useInJsp;
    }
    
    protected TokenID parseToken() {
        char actChar;
        
        while(offset < stopOffset) {
            actChar = buffer[offset];
            
            switch (state) {
                case INIT:
                    switch (actChar) {
                        case '"': // NOI18N
                            state = ISI_STRING;
                            break;
                        case '\'':
                            state = ISI_CHAR;
                            break;
                        case '/':
                            state = ISA_SLASH;
                            break;
                        case '=':
                            state = ISA_EQ;
                            break;
                        case '>':
                            state = ISA_GT;
                            break;
                        case '<':
                            state = ISA_LT;
                            break;
                        case '+':
                            state = ISA_PLUS;
                            break;
                        case '-':
                            state = ISA_MINUS;
                            break;
                        case '*':
                            state = ISA_STAR;
                            break;
                        case '|':
                            state = ISA_PIPE;
                            break;
                        case '%':
                            state = ISA_PERCENT;
                            break;
                        case '&':
                            state = ISA_AND;
                            break;
                        case '^':
                            state = ISA_XOR;
                            break;
                        case '~':
                            offset++;
                            return JSTokenContext.NEG;
                        case '!':
                            state = ISA_EXCLAMATION;
                            break;
                        case '0':
                            state = ISA_ZERO;
                            break;
                        case '.':
                            state = ISA_DOT;
                            break;
                        case ',':
                            offset++;
                            return JSTokenContext.COMMA;
                        case ';':
                            offset++;
                            return JSTokenContext.SEMICOLON;
                        case ':':
                            offset++;
                            return JSTokenContext.COLON;
                        case '?':
                            offset++;
                            return JSTokenContext.QUESTION;
                        case '(':
                            offset++;
                            return JSTokenContext.LPAREN;
                        case ')':
                            offset++;
                            return JSTokenContext.RPAREN;
                        case '[':
                            offset++;
                            return JSTokenContext.LBRACKET;
                        case ']':
                            offset++;
                            return JSTokenContext.RBRACKET;
                        case '{':
                            offset++;
                            return JSTokenContext.LBRACE;
                        case '}':
                            offset++;
                            return JSTokenContext.RBRACE;
                        case '@': // 1.5 "@ident" annotation // NOI18N
                            offset++;
                            return JSTokenContext.ANNOTATION;
                            
                        default:
                            // Check for whitespace
                            if (Character.isWhitespace(actChar)) {
                                state = ISI_WHITESPACE;
                                break;
                            }
                            
                            // Check for digit
                            if (Character.isDigit(actChar)) {
                                state = ISI_INT;
                                break;
                            }
                            
                            // Check for identifier
                            if (Character.isJavaIdentifierStart(actChar)) {
                                state = ISI_IDENTIFIER;
                                break;
                            }
                            
                            offset++;
                            return JSTokenContext.INVALID_CHAR;
                    }
                    break;
                    
                case ISI_WHITESPACE: // white space
                    if (!Character.isWhitespace(actChar)) {
                        state = INIT;
                        return JSTokenContext.WHITESPACE;
                    }
                    break;
                    
                case ISI_LINE_COMMENT:
                    switch (actChar) {
                        case '\n':
                            state = INIT;
                            return JSTokenContext.LINE_COMMENT;
                    }
                    break;
                    
                case ISI_BLOCK_COMMENT:
                    switch (actChar) {
                        case '*':
                            state = ISA_STAR_I_BLOCK_COMMENT;
                            break;
                            //create a block comment token for each line of the comment - a performance fix for #55628
                        case '\n':
                            if(useInJsp) {
                                //leave the some state - we are still in the block comment,
                                //we just need to create a token for each line.
                                offset++;
                                return JSTokenContext.BLOCK_COMMENT;
                            }
                    }
                    break;
                    
                case ISI_STRING:
                    switch (actChar) {
                        case '\\':
                            state = ISI_STRING_A_BSLASH;
                            break;
                        case '\n':
                            state = INIT;
                            supposedTokenID = JSTokenContext.STRING_LITERAL;
//!!!                    return JSTokenContext.INCOMPLETE_STRING_LITERAL;
                            return supposedTokenID;
                        case '"': // NOI18N
                            offset++;
                            state = INIT;
                            return JSTokenContext.STRING_LITERAL;
                    }
                    break;
                    
                case ISI_STRING_A_BSLASH:
                    switch (actChar) {
                        case '"': // NOI18N
                        case '\\':
                            break;
                        default:
                            offset--;
                            break;
                    }
                    state = ISI_STRING;
                    break;
                    
                case ISI_CHAR:
                    switch (actChar) {
                        case '\\':
                            state = ISI_CHAR_A_BSLASH;
                            break;
                        case '\n':
                            state = INIT;
                            supposedTokenID = JSTokenContext.CHAR_LITERAL;
// !!!                    return JSTokenContext.INCOMPLETE_CHAR_LITERAL;
                            return supposedTokenID;
                        case '\'':
                            offset++;
                            state = INIT;
                            return JSTokenContext.CHAR_LITERAL;
                    }
                    break;
                    
                case ISI_CHAR_A_BSLASH:
                    switch (actChar) {
                        case '\'':
                        case '\\':
                            break;
                        default:
                            offset--;
                            break;
                    }
                    state = ISI_CHAR;
                    break;
                    
                case ISI_IDENTIFIER:
                    if (!(Character.isJavaIdentifierPart(actChar))) {
                        state = INIT;
                        TokenID tid = matchKeyword(buffer, tokenOffset, offset - tokenOffset);
                        return (tid != null) ? tid : JSTokenContext.IDENTIFIER;
                    }
                    break;
                    
                case ISA_SLASH:
                    switch (actChar) {
                        case '=':
                            offset++;
                            state = INIT;
                            return JSTokenContext.DIV_EQ;
                        case '/':
                            state = ISI_LINE_COMMENT;
                            break;
                        case '*':
                            state = ISI_BLOCK_COMMENT;
                            break;
                        default:
                            state = INIT;
                            return JSTokenContext.DIV;
                    }
                    break;
                    
                case ISA_EQ:
                    switch (actChar) {
                        case '=':
                            offset++;
                            return  JSTokenContext.EQ_EQ;
                        default:
                            state = INIT;
                            return JSTokenContext.EQ;
                    }
                    // break;
                    
                case ISA_GT:
                    switch (actChar) {
                        case '>':
                            state = ISA_GTGT;
                            break;
                        case '=':
                            offset++;
                            return JSTokenContext.GT_EQ;
                        default:
                            state = INIT;
                            return JSTokenContext.GT;
                    }
                    break;
                    
                case ISA_GTGT:
                    switch (actChar) {
                        case '>':
                            state = ISA_GTGTGT;
                            break;
                        case '=':
                            offset++;
                            return JSTokenContext.RSSHIFT_EQ;
                        default:
                            state = INIT;
                            return JSTokenContext.RSSHIFT;
                    }
                    break;
                    
                case ISA_GTGTGT:
                    switch (actChar) {
                        case '=':
                            offset++;
                            return JSTokenContext.RUSHIFT_EQ;
                        default:
                            state = INIT;
                            return JSTokenContext.RUSHIFT;
                    }
                    // break;
                    
                    
                case ISA_LT:
                    switch (actChar) {
                        case '<':
                            state = ISA_LTLT;
                            break;
                        case '=':
                            offset++;
                            return JSTokenContext.LT_EQ;
                        default:
                            state = INIT;
                            return JSTokenContext.LT;
                    }
                    break;
                    
                case ISA_LTLT:
                    switch (actChar) {
                        case '<':
                            state = INIT;
                            offset++;
                            return JSTokenContext.INVALID_OPERATOR;
                        case '=':
                            offset++;
                            return JSTokenContext.LSHIFT_EQ;
                        default:
                            state = INIT;
                            return JSTokenContext.LSHIFT;
                    }
                    
                case ISA_PLUS:
                    switch (actChar) {
                        case '+':
                            offset++;
                            return JSTokenContext.PLUS_PLUS;
                        case '=':
                            offset++;
                            return JSTokenContext.PLUS_EQ;
                        default:
                            state = INIT;
                            return JSTokenContext.PLUS;
                    }
                    
                case ISA_MINUS:
                    switch (actChar) {
                        case '-':
                            offset++;
                            return JSTokenContext.MINUS_MINUS;
                        case '=':
                            offset++;
                            return JSTokenContext.MINUS_EQ;
                        default:
                            state = INIT;
                            return JSTokenContext.MINUS;
                    }
                    
                case ISA_STAR:
                    switch (actChar) {
                        case '=':
                            offset++;
                            return JSTokenContext.MUL_EQ;
                        case '/':
                            offset++;
                            state = INIT;
                            return JSTokenContext.INVALID_COMMENT_END; // '*/' outside comment
                        default:
                            state = INIT;
                            return JSTokenContext.MUL;
                    }
                    
                case ISA_STAR_I_BLOCK_COMMENT:
                    switch (actChar) {
                        case '/':
                            offset++;
                            state = INIT;
                            return JSTokenContext.BLOCK_COMMENT;
                        default:
                            offset--;
                            state = ISI_BLOCK_COMMENT;
                            break;
                    }
                    break;
                    
                case ISA_PIPE:
                    switch (actChar) {
                        case '=':
                            offset++;
                            state = INIT;
                            return JSTokenContext.OR_EQ;
                        case '|':
                            offset++;
                            state = INIT;
                            return JSTokenContext.OR_OR;
                        default:
                            state = INIT;
                            return JSTokenContext.OR;
                    }
                    // break;
                    
                case ISA_PERCENT:
                    switch (actChar) {
                        case '=':
                            offset++;
                            state = INIT;
                            return JSTokenContext.MOD_EQ;
                        default:
                            state = INIT;
                            return JSTokenContext.MOD;
                    }
                    // break;
                    
                case ISA_AND:
                    switch (actChar) {
                        case '=':
                            offset++;
                            state = INIT;
                            return JSTokenContext.AND_EQ;
                        case '&':
                            offset++;
                            state = INIT;
                            return JSTokenContext.AND_AND;
                        default:
                            state = INIT;
                            return JSTokenContext.AND;
                    }
                    // break;
                    
                case ISA_XOR:
                    switch (actChar) {
                        case '=':
                            offset++;
                            state = INIT;
                            return JSTokenContext.XOR_EQ;
                        default:
                            state = INIT;
                            return JSTokenContext.XOR;
                    }
                    // break;
                    
                case ISA_EXCLAMATION:
                    switch (actChar) {
                        case '=':
                            offset++;
                            state = INIT;
                            return JSTokenContext.NOT_EQ;
                        default:
                            state = INIT;
                            return JSTokenContext.NOT;
                    }
                    // break;
                    
                case ISA_ZERO:
                    switch (actChar) {
                        case '.':
                            state = ISI_DOUBLE;
                            break;
                        case 'x':
                        case 'X':
                            state = ISI_HEX;
                            break;
                        case 'l':
                        case 'L':
                            offset++;
                            state = INIT;
                            return JSTokenContext.LONG_LITERAL;
                        case 'f':
                        case 'F':
                            offset++;
                            state = INIT;
                            return JSTokenContext.FLOAT_LITERAL;
                        case 'd':
                        case 'D':
                            offset++;
                            state = INIT;
                            return JSTokenContext.DOUBLE_LITERAL;
                        case '8': // it's error to have '8' and '9' in octal number
                        case '9':
                            state = INIT;
                            offset++;
                            return JSTokenContext.INVALID_OCTAL_LITERAL;
                        case 'e':
                        case 'E':
                            state = ISI_DOUBLE_EXP;
                            break;
                        default:
                            if (Character.isDigit(actChar)) { // '8' and '9' already handled
                                state = ISI_OCTAL;
                                break;
                            }
                            state = INIT;
                            return JSTokenContext.INT_LITERAL;
                    }
                    break;
                    
                case ISI_INT:
                    switch (actChar) {
                        case 'l':
                        case 'L':
                            offset++;
                            state = INIT;
                            return JSTokenContext.LONG_LITERAL;
                        case '.':
                            state = ISI_DOUBLE;
                            break;
                        case 'f':
                        case 'F':
                            offset++;
                            state = INIT;
                            return JSTokenContext.FLOAT_LITERAL;
                        case 'd':
                        case 'D':
                            offset++;
                            state = INIT;
                            return JSTokenContext.DOUBLE_LITERAL;
                        case 'e':
                        case 'E':
                            state = ISI_DOUBLE_EXP;
                            break;
                        default:
                            if (!(actChar >= '0' && actChar <= '9')) {
                                state = INIT;
                                return JSTokenContext.INT_LITERAL;
                            }
                    }
                    break;
                    
                case ISI_OCTAL:
                    if (!(actChar >= '0' && actChar <= '7')) {
                        
                        state = INIT;
                        return JSTokenContext.OCTAL_LITERAL;
                    }
                    break;
                    
                case ISI_DOUBLE:
                    switch (actChar) {
                        case 'f':
                        case 'F':
                            offset++;
                            state = INIT;
                            return JSTokenContext.FLOAT_LITERAL;
                        case 'd':
                        case 'D':
                            offset++;
                            state = INIT;
                            return JSTokenContext.DOUBLE_LITERAL;
                        case 'e':
                        case 'E':
                            state = ISI_DOUBLE_EXP;
                            break;
                        default:
                            if (!((actChar >= '0' && actChar <= '9')
                            || actChar == '.')) {
                                
                                state = INIT;
                                return JSTokenContext.DOUBLE_LITERAL;
                            }
                    }
                    break;
                    
                case ISI_DOUBLE_EXP:
                    switch (actChar) {
                        case 'f':
                        case 'F':
                            offset++;
                            state = INIT;
                            return JSTokenContext.FLOAT_LITERAL;
                        case 'd':
                        case 'D':
                            offset++;
                            state = INIT;
                            return JSTokenContext.DOUBLE_LITERAL;
                        default:
                            if (!(Character.isDigit(actChar)
                            || actChar == '-' || actChar == '+')) {
                                state = INIT;
                                return JSTokenContext.DOUBLE_LITERAL;
                            }
                    }
                    break;
                    
                case ISI_HEX:
                    if (!((actChar >= 'a' && actChar <= 'f')
                    || (actChar >= 'A' && actChar <= 'F')
                    || Character.isDigit(actChar))
                    ) {
                        
                        state = INIT;
                        return JSTokenContext.HEX_LITERAL;
                    }
                    break;
                    
                case ISA_DOT:
                    if (Character.isDigit(actChar)) {
                        state = ISI_DOUBLE;
                    } else if (actChar == '.' && offset + 1 < stopOffset && buffer[offset + 1] == '.') {
                        offset += 2;
                        state = INIT;
                        return JSTokenContext.ELLIPSIS;
                    } else { // only single dot
                        state = INIT;
                        return JSTokenContext.DOT;
                    }
                    break;
                    
            } // end of switch(state)
            
            offset++;
        } // end of while(offset...)
        
        /** At this stage there's no more text in the scanned buffer.
         * Scanner first checks whether this is completely the last
         * available buffer.
         */
        
        if (lastBuffer) {
            switch(state) {
                case ISI_WHITESPACE:
                    state = INIT;
                    return JSTokenContext.WHITESPACE;
                case ISI_IDENTIFIER:
                    state = INIT;
                    TokenID kwd = matchKeyword(buffer, tokenOffset, offset - tokenOffset);
                    return (kwd != null) ? kwd : JSTokenContext.IDENTIFIER;
                case ISI_LINE_COMMENT:
                    return JSTokenContext.LINE_COMMENT; // stay in line-comment state
                case ISI_BLOCK_COMMENT:
                case ISA_STAR_I_BLOCK_COMMENT:
                    return JSTokenContext.BLOCK_COMMENT; // stay in block-comment state
                case ISI_STRING:
                case ISI_STRING_A_BSLASH:
                    return JSTokenContext.STRING_LITERAL; // hold the state
                case ISI_CHAR:
                case ISI_CHAR_A_BSLASH:
                    return JSTokenContext.CHAR_LITERAL; // hold the state
                case ISA_ZERO:
                case ISI_INT:
                    state = INIT;
                    return JSTokenContext.INT_LITERAL;
                case ISI_OCTAL:
                    state = INIT;
                    return JSTokenContext.OCTAL_LITERAL;
                case ISI_DOUBLE:
                case ISI_DOUBLE_EXP:
                    state = INIT;
                    return JSTokenContext.DOUBLE_LITERAL;
                case ISI_HEX:
                    state = INIT;
                    return JSTokenContext.HEX_LITERAL;
                case ISA_DOT:
                    state = INIT;
                    return JSTokenContext.DOT;
                case ISA_SLASH:
                    state = INIT;
                    return JSTokenContext.DIV;
                case ISA_EQ:
                    state = INIT;
                    return JSTokenContext.EQ;
                case ISA_GT:
                    state = INIT;
                    return JSTokenContext.GT;
                case ISA_GTGT:
                    state = INIT;
                    return JSTokenContext.RSSHIFT;
                case ISA_GTGTGT:
                    state = INIT;
                    return JSTokenContext.RUSHIFT;
                case ISA_LT:
                    state = INIT;
                    return JSTokenContext.LT;
                case ISA_LTLT:
                    state = INIT;
                    return JSTokenContext.LSHIFT;
                case ISA_PLUS:
                    state = INIT;
                    return JSTokenContext.PLUS;
                case ISA_MINUS:
                    state = INIT;
                    return JSTokenContext.MINUS;
                case ISA_STAR:
                    state = INIT;
                    return JSTokenContext.MUL;
                case ISA_PIPE:
                    state = INIT;
                    return JSTokenContext.OR;
                case ISA_PERCENT:
                    state = INIT;
                    return JSTokenContext.MOD;
                case ISA_AND:
                    state = INIT;
                    return JSTokenContext.AND;
                case ISA_XOR:
                    state = INIT;
                    return JSTokenContext.XOR;
                case ISA_EXCLAMATION:
                    state = INIT;
                    return JSTokenContext.NOT;
            }
        }
        
        /* At this stage there's no more text in the scanned buffer, but
         * this buffer is not the last so the scan will continue on another buffer.
         * The scanner tries to minimize the amount of characters
         * that will be prescanned in the next buffer by returning the token
         * where possible.
         */
        
        switch (state) {
            case ISI_WHITESPACE:
                return JSTokenContext.WHITESPACE;
        }
        
        return null; // nothing found
    }
    
    public String getStateName(int stateNumber) {
        switch(stateNumber) {
            case ISI_WHITESPACE:
                return "ISI_WHITESPACE"; // NOI18N
            case ISI_LINE_COMMENT:
                return "ISI_LINE_COMMENT"; // NOI18N
            case ISI_BLOCK_COMMENT:
                return "ISI_BLOCK_COMMENT"; // NOI18N
            case ISI_STRING:
                return "ISI_STRING"; // NOI18N
            case ISI_STRING_A_BSLASH:
                return "ISI_STRING_A_BSLASH"; // NOI18N
            case ISI_CHAR:
                return "ISI_CHAR"; // NOI18N
            case ISI_CHAR_A_BSLASH:
                return "ISI_CHAR_A_BSLASH"; // NOI18N
            case ISI_IDENTIFIER:
                return "ISI_IDENTIFIER"; // NOI18N
            case ISA_SLASH:
                return "ISA_SLASH"; // NOI18N
            case ISA_EQ:
                return "ISA_EQ"; // NOI18N
            case ISA_GT:
                return "ISA_GT"; // NOI18N
            case ISA_GTGT:
                return "ISA_GTGT"; // NOI18N
            case ISA_GTGTGT:
                return "ISA_GTGTGT"; // NOI18N
            case ISA_LT:
                return "ISA_LT"; // NOI18N
            case ISA_LTLT:
                return "ISA_LTLT"; // NOI18N
            case ISA_PLUS:
                return "ISA_PLUS"; // NOI18N
            case ISA_MINUS:
                return "ISA_MINUS"; // NOI18N
            case ISA_STAR:
                return "ISA_STAR"; // NOI18N
            case ISA_STAR_I_BLOCK_COMMENT:
                return "ISA_STAR_I_BLOCK_COMMENT"; // NOI18N
            case ISA_PIPE:
                return "ISA_PIPE"; // NOI18N
            case ISA_PERCENT:
                return "ISA_PERCENT"; // NOI18N
            case ISA_AND:
                return "ISA_AND"; // NOI18N
            case ISA_XOR:
                return "ISA_XOR"; // NOI18N
            case ISA_EXCLAMATION:
                return "ISA_EXCLAMATION"; // NOI18N
            case ISA_ZERO:
                return "ISA_ZERO"; // NOI18N
            case ISI_INT:
                return "ISI_INT"; // NOI18N
            case ISI_OCTAL:
                return "ISI_OCTAL"; // NOI18N
            case ISI_DOUBLE:
                return "ISI_DOUBLE"; // NOI18N
            case ISI_DOUBLE_EXP:
                return "ISI_DOUBLE_EXP"; // NOI18N
            case ISI_HEX:
                return "ISI_HEX"; // NOI18N
            case ISA_DOT:
                return "ISA_DOT"; // NOI18N
                
            default:
                return super.getStateName(stateNumber);
        }
    }
    
    public TokenID matchKeyword(char[] buffer, int offset, int len) {
        if (len > 12)
            return null;
        if (len <= 1)
            return null;
        switch (buffer[offset++]) {
            case 'a':
                if (len <= 5)
                    return null;
                switch (buffer[offset++]) {
                    case 'b':
                        return (len == 8
                                && buffer[offset++] == 's'
                                && buffer[offset++] == 't'
                                && buffer[offset++] == 'r'
                                && buffer[offset++] == 'a'
                                && buffer[offset++] == 'c'
                                && buffer[offset++] == 't')
                                ? JSTokenContext.ABSTRACT : null;
                    case 's':
                        return (len == 6
                                && buffer[offset++] == 's'
                                && buffer[offset++] == 'e'
                                && buffer[offset++] == 'r'
                                && buffer[offset++] == 't')
                                ? JSTokenContext.ASSERT : null;
                    default:
                        return null;
                }
            case 'b':
                if (len <= 3)
                    return null;
                switch (buffer[offset++]) {
                    case 'o':
                        return (len == 7
                                && buffer[offset++] == 'o'
                                && buffer[offset++] == 'l'
                                && buffer[offset++] == 'e'
                                && buffer[offset++] == 'a'
                                && buffer[offset++] == 'n')
                                ? JSTokenContext.BOOLEAN : null;
                    case 'r':
                        return (len == 5
                                && buffer[offset++] == 'e'
                                && buffer[offset++] == 'a'
                                && buffer[offset++] == 'k')
                                ? JSTokenContext.BREAK : null;
                    case 'y':
                        return (len == 4
                                && buffer[offset++] == 't'
                                && buffer[offset++] == 'e')
                                ? JSTokenContext.BYTE : null;
                    default:
                        return null;
                }
            case 'c':
                if (len <= 3)
                    return null;
                switch (buffer[offset++]) {
                    case 'a':
                        switch (buffer[offset++]) {
                            case 's':
                                return (len == 4
                                        && buffer[offset++] == 'e')
                                        ? JSTokenContext.CASE : null;
                            case 't':
                                return (len == 5
                                        && buffer[offset++] == 'c'
                                        && buffer[offset++] == 'h')
                                        ? JSTokenContext.CATCH : null;
                            default:
                                return null;
                        }
                    case 'h':
                        return (len == 4
                                && buffer[offset++] == 'a'
                                && buffer[offset++] == 'r')
                                ? JSTokenContext.CHAR : null;
                    case 'l':
                        return (len == 5
                                && buffer[offset++] == 'a'
                                && buffer[offset++] == 's'
                                && buffer[offset++] == 's')
                                ? JSTokenContext.CLASS : null;
                    case 'o':
                        if (len <= 4)
                            return null;
                        if (buffer[offset++] != 'n')
                            return null;
                        switch (buffer[offset++]) {
                            case 's':
                                return (len == 5
                                        && buffer[offset++] == 't')
                                        ? JSTokenContext.CONST : null;
                            case 't':
                                return (len == 8
                                        && buffer[offset++] == 'i'
                                        && buffer[offset++] == 'n'
                                        && buffer[offset++] == 'u'
                                        && buffer[offset++] == 'e')
                                        ? JSTokenContext.CONTINUE : null;
                            default:
                                return null;
                        }
                    default:
                        return null;
                }
            case 'd':
                switch (buffer[offset++]) {
                    case 'e':
                        return (len == 7
                                && buffer[offset++] == 'f'
                                && buffer[offset++] == 'a'
                                && buffer[offset++] == 'u'
                                && buffer[offset++] == 'l'
                                && buffer[offset++] == 't')
                                ? JSTokenContext.DEFAULT : null;
                    case 'o':
                        if (len == 2)
                            return JSTokenContext.DO;
                        switch (buffer[offset++]) {
                            case 'u':
                                return (len == 6
                                        && buffer[offset++] == 'b'
                                        && buffer[offset++] == 'l'
                                        && buffer[offset++] == 'e')
                                        ? JSTokenContext.DOUBLE : null;
                            default:
                                return null;
                        }
                    default:
                        return null;
                }
            case 'e':
                if (len <= 3)
                    return null;
                switch (buffer[offset++]) {
                    case 'l':
                        return (len == 4
                                && buffer[offset++] == 's'
                                && buffer[offset++] == 'e')
                                ? JSTokenContext.ELSE : null;
                    case 'n':
                        return (len == 4
                                && buffer[offset++] == 'u'
                                && buffer[offset++] == 'm')
                                ? isJava15 ? JSTokenContext.ENUM : null : null;
                    case 'x':
                        return (len == 7
                                && buffer[offset++] == 't'
                                && buffer[offset++] == 'e'
                                && buffer[offset++] == 'n'
                                && buffer[offset++] == 'd'
                                && buffer[offset++] == 's')
                                ? JSTokenContext.EXTENDS : null;
                    default:
                        return null;
                }
            case 'f':
                if (len <= 2)
                    return null;
                switch (buffer[offset++]) {
                    case 'a':
                        return (len == 5
                                && buffer[offset++] == 'l'
                                && buffer[offset++] == 's'
                                && buffer[offset++] == 'e')
                                ? JSTokenContext.FALSE : null;
                    case 'i':
                        if (len <= 4)
                            return null;
                        if (buffer[offset++] != 'n'
                                || buffer[offset++] != 'a'
                                || buffer[offset++] != 'l')
                            return null;
                        if (len == 5)
                            return JSTokenContext.FINAL;
                        if (len <= 6)
                            return null;
                        if (buffer[offset++] != 'l'
                                || buffer[offset++] != 'y')
                            return null;
                        if (len == 7)
                            return JSTokenContext.FINALLY;
                        return null;
                    case 'l':
                        return (len == 5
                                && buffer[offset++] == 'o'
                                && buffer[offset++] == 'a'
                                && buffer[offset++] == 't')
                                ? JSTokenContext.FLOAT : null;
                    case 'o':
                        return (len == 3
                                && buffer[offset++] == 'r')
                                ? JSTokenContext.FOR : null;
                    case 'u':
                        return (len == 8
                                && buffer[offset++] == 'n'
                                && buffer[offset++] == 'c'
                                && buffer[offset++] == 't'
                                && buffer[offset++] == 'i'
                                && buffer[offset++] == 'o'
                                && buffer[offset++] == 'n')
                                ? JSTokenContext.FLOAT : null;
                    default:
                        return null;
                }
            case 'g':
                return (len == 4
                        && buffer[offset++] == 'o'
                        && buffer[offset++] == 't'
                        && buffer[offset++] == 'o')
                        ? JSTokenContext.GOTO : null;
            case 'i':
                switch (buffer[offset++]) {
                    case 'f':
                        return (len == 2)
                        ? JSTokenContext.IF : null;
                    case 'm':
                        if (len <= 5)
                            return null;
                        if (buffer[offset++] != 'p')
                            return null;
                        switch (buffer[offset++]) {
                            case 'l':
                                return (len == 10
                                        && buffer[offset++] == 'e'
                                        && buffer[offset++] == 'm'
                                        && buffer[offset++] == 'e'
                                        && buffer[offset++] == 'n'
                                        && buffer[offset++] == 't'
                                        && buffer[offset++] == 's')
                                        ? JSTokenContext.IMPLEMENTS : null;
                            case 'o':
                                return (len == 6
                                        && buffer[offset++] == 'r'
                                        && buffer[offset++] == 't')
                                        ? JSTokenContext.IMPORT : null;
                            default:
                                return null;
                        }
                    case 'n':
                        if (len <= 2)
                            return null;
                        switch (buffer[offset++]) {
                            case 's':
                                return (len == 10
                                        && buffer[offset++] == 't'
                                        && buffer[offset++] == 'a'
                                        && buffer[offset++] == 'n'
                                        && buffer[offset++] == 'c'
                                        && buffer[offset++] == 'e'
                                        && buffer[offset++] == 'o'
                                        && buffer[offset++] == 'f')
                                        ? JSTokenContext.INSTANCEOF : null;
                            case 't':
                                if (len == 3)
                                    return JSTokenContext.INT;
                                switch (buffer[offset++]) {
                                    case 'e':
                                        return (len == 9
                                                && buffer[offset++] == 'r'
                                                && buffer[offset++] == 'f'
                                                && buffer[offset++] == 'a'
                                                && buffer[offset++] == 'c'
                                                && buffer[offset++] == 'e')
                                                ? JSTokenContext.INTERFACE : null;
                                    default:
                                        return null;
                                }
                            default:
                                return null;
                        }
                    default:
                        return null;
                }
            case 'l':
                return (len == 4
                        && buffer[offset++] == 'o'
                        && buffer[offset++] == 'n'
                        && buffer[offset++] == 'g')
                        ? JSTokenContext.LONG : null;
            case 'n':
                if (len <= 2)
                    return null;
                switch (buffer[offset++]) {
                    case 'a':
                        return (len == 6
                                && buffer[offset++] == 't'
                                && buffer[offset++] == 'i'
                                && buffer[offset++] == 'v'
                                && buffer[offset++] == 'e')
                                ? JSTokenContext.NATIVE : null;
                    case 'e':
                        return (len == 3
                                && buffer[offset++] == 'w')
                                ? JSTokenContext.NEW : null;
                    case 'u':
                        return (len == 4
                                && buffer[offset++] == 'l'
                                && buffer[offset++] == 'l')
                                ? JSTokenContext.NULL : null;
                    default:
                        return null;
                }
            case 'p':
                if (len <= 5)
                    return null;
                switch (buffer[offset++]) {
                    case 'a':
                        return (len == 7
                                && buffer[offset++] == 'c'
                                && buffer[offset++] == 'k'
                                && buffer[offset++] == 'a'
                                && buffer[offset++] == 'g'
                                && buffer[offset++] == 'e')
                                ? JSTokenContext.PACKAGE : null;
                    case 'r':
                        if (len <= 6)
                            return null;
                        switch (buffer[offset++]) {
                            case 'i':
                                return (len == 7
                                        && buffer[offset++] == 'v'
                                        && buffer[offset++] == 'a'
                                        && buffer[offset++] == 't'
                                        && buffer[offset++] == 'e')
                                        ? JSTokenContext.PRIVATE : null;
                            case 'o':
                                return (len == 9
                                        && buffer[offset++] == 't'
                                        && buffer[offset++] == 'e'
                                        && buffer[offset++] == 'c'
                                        && buffer[offset++] == 't'
                                        && buffer[offset++] == 'e'
                                        && buffer[offset++] == 'd')
                                        ? JSTokenContext.PROTECTED : null;
                            default:
                                return null;
                        }
                    case 'u':
                        return (len == 6
                                && buffer[offset++] == 'b'
                                && buffer[offset++] == 'l'
                                && buffer[offset++] == 'i'
                                && buffer[offset++] == 'c')
                                ? JSTokenContext.PUBLIC : null;
                    default:
                        return null;
                }
            case 'r':
                return (len == 6
                        && buffer[offset++] == 'e'
                        && buffer[offset++] == 't'
                        && buffer[offset++] == 'u'
                        && buffer[offset++] == 'r'
                        && buffer[offset++] == 'n')
                        ? JSTokenContext.RETURN : null;
            case 's':
                if (len <= 4)
                    return null;
                switch (buffer[offset++]) {
                    case 'h':
                        return (len == 5
                                && buffer[offset++] == 'o'
                                && buffer[offset++] == 'r'
                                && buffer[offset++] == 't')
                                ? JSTokenContext.SHORT : null;
                    case 't':
                        if (len <= 5)
                            return null;
                        switch (buffer[offset++]) {
                            case 'a':
                                return (len == 6
                                        && buffer[offset++] == 't'
                                        && buffer[offset++] == 'i'
                                        && buffer[offset++] == 'c')
                                        ? JSTokenContext.STATIC : null;
                            case 'r':
                                return (len == 8
                                        && buffer[offset++] == 'i'
                                        && buffer[offset++] == 'c'
                                        && buffer[offset++] == 't'
                                        && buffer[offset++] == 'f'
                                        && buffer[offset++] == 'p')
                                        ? JSTokenContext.STRICTFP : null;
                            default:
                                return null;
                        }
                    case 'u':
                        return (len == 5
                                && buffer[offset++] == 'p'
                                && buffer[offset++] == 'e'
                                && buffer[offset++] == 'r')
                                ? JSTokenContext.SUPER : null;
                    case 'w':
                        return (len == 6
                                && buffer[offset++] == 'i'
                                && buffer[offset++] == 't'
                                && buffer[offset++] == 'c'
                                && buffer[offset++] == 'h')
                                ? JSTokenContext.SWITCH : null;
                    case 'y':
                        return (len == 12
                                && buffer[offset++] == 'n'
                                && buffer[offset++] == 'c'
                                && buffer[offset++] == 'h'
                                && buffer[offset++] == 'r'
                                && buffer[offset++] == 'o'
                                && buffer[offset++] == 'n'
                                && buffer[offset++] == 'i'
                                && buffer[offset++] == 'z'
                                && buffer[offset++] == 'e'
                                && buffer[offset++] == 'd')
                                ? JSTokenContext.SYNCHRONIZED : null;
                    default:
                        return null;
                }
            case 't':
                if (len <= 2)
                    return null;
                switch (buffer[offset++]) {
                    case 'h':
                        if (len <= 3)
                            return null;
                        switch (buffer[offset++]) {
                            case 'i':
                                return (len == 4
                                        && buffer[offset++] == 's')
                                        ? JSTokenContext.THIS : null;
                            case 'r':
                                if (len <= 4)
                                    return null;
                                if (buffer[offset++] != 'o'
                                        || buffer[offset++] != 'w')
                                    return null;
                                if (len == 5)
                                    return JSTokenContext.THROW;
                                if (buffer[offset++] != 's')
                                    return null;
                                if (len == 6)
                                    return JSTokenContext.THROWS;
                                return null;
                            default:
                                return null;
                        }
                    case 'r':
                        switch (buffer[offset++]) {
                            case 'a':
                                return (len == 9
                                        && buffer[offset++] == 'n'
                                        && buffer[offset++] == 's'
                                        && buffer[offset++] == 'i'
                                        && buffer[offset++] == 'e'
                                        && buffer[offset++] == 'n'
                                        && buffer[offset++] == 't')
                                        ? JSTokenContext.TRANSIENT : null;
                            case 'u':
                                return (len == 4
                                        && buffer[offset++] == 'e')
                                        ? JSTokenContext.TRUE : null;
                            case 'y':
                                return (len == 3)
                                ? JSTokenContext.TRY : null;
                            default:
                                return null;
                        }
                    default:
                        return null;
                }
            case 'v':
                if (len < 3)
                    return null;
                switch (buffer[offset++]) {
                    case 'o':
                        switch (buffer[offset++]) {
                            case 'i':
                                return (len == 4
                                        && buffer[offset++] == 'd')
                                        ? JSTokenContext.VOID : null;
                            case 'l':
                                return (len == 8
                                        && buffer[offset++] == 'a'
                                        && buffer[offset++] == 't'
                                        && buffer[offset++] == 'i'
                                        && buffer[offset++] == 'l'
                                        && buffer[offset++] == 'e')
                                        ? JSTokenContext.VOLATILE : null;
                            default:
                                return null;
                        }
                    case 'a':
                        return (len == 3
                                && buffer[offset++] == 'r')
                                ? JSTokenContext.VAR : null;
                    default: return null;
                }
            case 'w':
                return (len == 5
                        && buffer[offset++] == 'h'
                        && buffer[offset++] == 'i'
                        && buffer[offset++] == 'l'
                        && buffer[offset++] == 'e')
                        ? JSTokenContext.WHILE : null;
            default:
                return null;
        }
    }
    
}
