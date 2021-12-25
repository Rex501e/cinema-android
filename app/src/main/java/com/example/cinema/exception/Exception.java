package com.example.cinema.exception;


public class Exception extends java.lang.Exception implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    private String message;
    private String type;

    public Exception() {
    }

    public Exception(String libelle, String type) {
        this.message = libelle;
        this.type = type;
    }

    public Exception(String libelle) {
        this.message = libelle;

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String libelle) {
        this.message = libelle;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}

