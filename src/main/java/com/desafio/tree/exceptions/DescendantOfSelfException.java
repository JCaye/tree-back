package com.desafio.tree.exceptions;

public class DescendantOfSelfException extends ApiException {
    @Override
    public String getMessage(){
        return "Provided node descends from self!";
    }
}
