package main.Components.Variable;

import main.Components.Component;
import main.Constants.Regexes;
import main.MyException.SyntaxErrorException;

public abstract class Variable extends Component {
    final protected String name;
    final protected String type;
    protected String value = null;
    protected String line;
    protected boolean  isFinal = false;
    Variable(String token)
    {
        line = token;
        String[] tokens = token.split("\\s+");
        int i = 0;
        if(tokens[i].equals("final"))
        {
            this.isFinal = true;
            i++;
        }

        this.type = tokens[i++];
        this.name = tokens[i++];
        if(i < tokens.length && tokens[i].equals("="))
        {
            i++;
            this.value = tokens[i];
        }
        if(isFinal && value == null)
        {
            System.out.println("final variable is not initialize");
            //TODO: raise exception
        }

    }
    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public boolean isFinal() {
        return isFinal;
    }

    protected boolean checkName()
    {
        return name.matches(Regexes.VARIABLE_NAME);
    }
    protected boolean checkValue()
    {
        if(this.value == null)
            return true;
        return switch (this.type) {
            case "int" -> value.matches(Regexes.INTEGER_VALUE);
            case "double" -> value.matches(Regexes.DOUBLE_VALUE);
            case "String" -> value.matches(Regexes.STRING_VALUE);
            case "boolean" -> value.matches(Regexes.BOOLEAN_VALUE);
            case "char" -> value.matches(Regexes.CHAR_VALUE);
            default -> false;
        };
    }
    @Override
    public  void compile() throws SyntaxErrorException {
        if(!this.checkName())
        {
            throw new SyntaxErrorException("Invalid variable name, line:" + line);

        }
        if(!this.checkValue())
        {
            throw new SyntaxErrorException("Invalid assignment, line:" + line);
        }

    }


    public boolean setValue(String s) {
        this.value = s;
        return checkValue();
    }
}
