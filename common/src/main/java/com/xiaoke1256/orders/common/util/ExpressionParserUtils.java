package com.xiaoke1256.orders.common.util;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class ExpressionParserUtils {
    static ScriptEngineManager manager = new ScriptEngineManager();
    static ScriptEngine engine = manager.getEngineByName("nashorn");

    public static long parse(String expression) {
        try {
            Number result = (Number)engine.eval(expression);
            return result.longValue();
        } catch (ScriptException e) {
            throw new RuntimeException(e);
        }
    }
}
