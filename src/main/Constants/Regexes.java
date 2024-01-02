package main.Constants;

/*
A class that contains all the regexes we use in our program.
 */

public class Regexes {


    public static final String INTEGER_VALUE = "^-?\\d+$";
    public static final String DOUBLE_VALUE = "^-?\\d+(?:\\.\\d+)?$\n";
    public static final String STRING_VALUE = "^\".*\"$";
    public static final String BOOLEAN_VALUE = "^(true|false|-?\\d+(\\.\\d+)?)$";
    public static final String CHAR_VALUE = "^'.'$";
    public static final String VARIABLE_NAME = "^(?!\\d)\\w+(?: \\w+)*$";
    public static final String METHOD_NAME = "[a-zA-Z]\\w*";
    public static String VARIABLE_TYPES = "(int|boolean|double|String|char)";

    public static final String CHANGING_VARIABLE_VALUE = "\\s*(\\w+)\\s*=\\s*(.+)\\s*;";
    public static final String IF_WHILE = "(if|while)\\s*\\(.*\\)\\s*\\{";
    public static final String FINAL = "(final )";
    public static final String DEFINING_MULTI_VARIABLE = FINAL + "?\\s*" +VARIABLE_TYPES+"\\s+\\w+\\s*(=\\s*[^;\\s]+)?(\\s*,\\s*\\w+\\s*(=\\s*[^;\\s]+)?)*;";
    public static final String DEFINING_SINGLE_VARIABLE = FINAL + "?\\s*" +VARIABLE_TYPES+"\\s+\\w+\\s*(=\\s*[^;\\s]+)?\\s*;";
    public static final String CALLING_METHOD = "\\s*\\w+\\s*\\(\\s*(.*)\\)\\s*;";
    public static final String RETURN = "return\\s*;";
    public static final String METHOD_TYPES = "(void)";
    public static final String METHOD_DEFINE = METHOD_TYPES + "\\s+(\\w+)\\s*\\((\\s*.*)\\)\\s*\\{";

}
