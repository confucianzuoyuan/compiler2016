package com.abcdabcd987.compiler2016.AST;

import com.abcdabcd987.compiler2016.Symbol.Type;

/**
 * Created by abcdabcd987 on 2016-03-26.
 */
public abstract class Expr extends Stmt {
    public Type exprType;
    public boolean isLvalue = true;
}
