package main.Components;

import java.util.HashMap;

import main.Components.Variable.GlobalVariable;
import main.MyException.SyntaxErrorException;

public abstract class Component {
    protected static HashMap<String , GlobalVariable> globalVariables = new HashMap<>();
    protected static HashMap<String , Method> methods = new HashMap<>();
    public abstract void  compile() throws SyntaxErrorException;
}
