package main.Components;
import main.Constants.Regexes;
import main.Components.Variable.LocalVariable;
import main.MyException.SyntaxErrorException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Method extends Component {
    private String type;
    private String name;

    private String[] methodBlock;
    private  List<LocalVariable> parameters;
    private  HashMap<String , LocalVariable> localVariables;

    public Method(String token) throws SyntaxErrorException {
        parameters = new ArrayList<>();
        localVariables = new HashMap<>();
        methodBlock = token.split("\n");
        Pattern pattern = Pattern.compile(Regexes.METHOD_DEFINE);
        Matcher matcher = pattern.matcher(methodBlock[0]);
        if(matcher.find())
        {
            this.type = matcher.group(1);
            this.name = matcher.group(2);
            String[] parametersDefention = matcher.group(3).split(",");
            String regex2 = "\\s*"+ Regexes.VARIABLE_TYPES+"\\s+(.+)\\s*";
            Pattern pattern2 = Pattern.compile(regex2);

            for(String param : parametersDefention){
                Matcher matcher2 = pattern2.matcher(param);
                if(matcher2.matches())
                {
                    String paramType = matcher2.group(1);
                    String paramName = matcher2.group(2);
                    if(localVariables.containsKey(paramName))
                    {
                        System.out.println("Parameter name repeated");
                        //TODO: raise exception
                    }
                    parameters.add(new LocalVariable( paramType +" " + paramName + " ;"));
                    localVariables.put(paramName,new LocalVariable( paramType +" " + paramName + " ;"));
                }
            }
            if(methods.containsKey(this.name))
                throw new SyntaxErrorException("Method name repeated, line:" + token);


            methods.put(this.name, this);
        }
    }

    private boolean checkName(){
        return this.name.matches(Regexes.METHOD_NAME);
    }
    private boolean checkType()
    {
        return this.type.matches(Regexes.METHOD_TYPES);
    }
    private boolean compileHelper(int i , int scope) throws SyntaxErrorException {
        if (scope == 0) {
            return true;
        }
        if (methodBlock[i].matches(Regexes.IF_WHILE)) {
            String condition = methodBlock[i].substring(methodBlock[i].indexOf("(") + 1, methodBlock[i].indexOf(")"));
            if (!isConditionValid(condition)) {
                return false;
            }
            return compileHelper(i + 1, scope + 1);
        }
        if (methodBlock[i].equals("}")) {
            return compileHelper(i + 1, scope - 1);
        }
        if (checkLine(i)) {
            return compileHelper(i + 1, scope);
        }
        return false;

    }

    private boolean checkLine(int i) throws SyntaxErrorException {
        String line = methodBlock[i];
        if (line.matches("\\s*\\w+\\s*\\(\\s*(.*)\\)\\s*;")) {
            return isMethodCallValid(i);
        } else if (line.matches(Regexes.DEFINING_SINGLE_VARIABLE)) {
            String[] split = line.substring(0, line.length()-1).split("=");
            String value = "";
            if(split.length == 2)
            {
                value = "="+split[1];
                if(localVariables.containsKey(split[1].trim())){
                    value = localVariables.get(split[1].trim()).getValue();
                    if(value == null)
                        value = "";
                    else
                        value = "=" + value;
                }
            }
            LocalVariable variable = new LocalVariable(split[0] + value + " ;");
            variable.compile();
            localVariables.put(variable.getName(), variable);
            return true;
        } else if (line.matches(Regexes.CHANGING_VARIABLE_VALUE)) {
            String[] split = line.substring(0, line.length() - 1).split("\\s*=\\s*");
            if (localVariables.containsKey(split[0])) {
                LocalVariable var = localVariables.get(split[0]);
                return var.setValue(split[1]);
            } else return false;
        }
        else if(line.matches(Regexes.RETURN))
        {
            return true;
        }

        return false;

    }

    private boolean isMethodCallValid(int i) {
        String line = methodBlock[i];
        int i1 = line.indexOf("(");
        int i2 = line.indexOf(")");
        String methodName = line.substring(0, i1);
        if (methods.containsKey(methodName)) {
            Method method = methods.get(methodName);
            String newLine = line.substring(i1 + 1, i2);
            if(newLine.equals(""))
            {
                return method.parameters.size() == 0;
            }
            String[] values = newLine.split(",");
            if(values.length != method.parameters.size())
            {
                return false;
            }
            for (int j = 0; j < values.length; j++) {
                if (localVariables.containsKey(values[j].trim()))
                    values[j] = localVariables.get(values[j].trim()).getValue();
                if (globalVariables.containsKey(values[j].trim()))
                    values[j] = globalVariables.get(values[j].trim()).getValue();
                if(!method.parameters.get(j).setValue(values[j].trim()))
                {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private boolean isConditionValid(String condition) {
        String splitRegex = "\\s*(\\|\\||&&)\\s*";
        String constantRegex = "(true|false|(-?\\d++\\.?\\d++)|-?\\d++)";
        String[] conditionConstants = condition.split(splitRegex);
        for (String var : conditionConstants) {
            if (!var.trim().matches(constantRegex)) {
                if (localVariables.containsKey(var.trim())) {
                    if (localVariables.get(var.trim()).getValue() == null ||
                            !localVariables.get(var.trim()).getValue().matches(constantRegex)) {
                        return false;
                    }
                } else if (globalVariables.containsKey(var.trim())) {
                    if (globalVariables.get(var.trim()).getValue() == null ||
                            !globalVariables.get(var.trim()).getValue().matches(constantRegex)) {
                        return false;
                    }
                } else return false;
            }

        }
        return true;

    }

    @Override
    public void compile() throws SyntaxErrorException {
        if(!this.checkName())
            throw new SyntaxErrorException("Illegal method name, name:" + name);
        if(!this.checkType())
            throw new SyntaxErrorException("Illegal method type, type: " + type);
        if(!this.compileHelper(1,1))
            throw new SyntaxErrorException("Syntax error, Method name: " + name);
    }


}
