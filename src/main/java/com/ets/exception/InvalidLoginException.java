package com.ets.exception;

/**
 *
 * @author User
 */
public class InvalidLoginException extends Exception{
    
    public InvalidLoginException(){
     super("##Invalid Username/Password##");
    }
}
