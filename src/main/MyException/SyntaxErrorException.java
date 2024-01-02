package main.MyException;

public class SyntaxErrorException extends Exception{
    public SyntaxErrorException(String massage)
    {
        super(massage);
    }
}
