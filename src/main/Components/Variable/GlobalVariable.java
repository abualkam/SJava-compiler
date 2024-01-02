package main.Components.Variable;

import main.Components.Component;
import main.MyException.SyntaxErrorException;

public class GlobalVariable extends Variable {


    public GlobalVariable(String token) throws SyntaxErrorException {
        super(token);

        if(Component.globalVariables.containsKey(name))
            throw new SyntaxErrorException("Repeated global variable name, line:" + token);


        Component.globalVariables.put(name, this);
    }


    protected boolean checkValue()
    {
        if(Component.globalVariables.containsKey(value))
        {
            GlobalVariable other =  Component.globalVariables.get(value);
            if(other == this)
            {
                return false;
            }
            this.value = other.value;
            return this.checkValue();
        }
        return super.checkValue();
    }


}
