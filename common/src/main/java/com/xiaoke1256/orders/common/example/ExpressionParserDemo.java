package com.xiaoke1256.orders.common.example;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * Apache Commons Math 表达式解析示例
 * 演示如何使用MathExpressionParser工具类解析和计算数学表达式
 */
public class ExpressionParserDemo {

    static ScriptEngineManager manager = new ScriptEngineManager();
    static ScriptEngine engine = manager.getEngineByName("nashorn");

    /**
     * 主入口方法 - 演示表达式解析功能
     */
    public static void main(String[] args) throws ScriptException {
        System.out.println("=== Apache Commons Math 表达式解析示例 ===");

        Number result = (Number)engine.eval("(1+1)*2");
        System.out.println("result:"+result);

    }
}

