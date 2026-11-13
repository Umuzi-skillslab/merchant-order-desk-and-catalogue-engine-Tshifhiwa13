package com.paynestsystem.domain;

public class Customer {

    private final int id;
    private final String name;
    private final String email;

    public Customer(int id, String name, String email) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Please Enter a valid name");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Please Enter a valid email");
        }
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return name + "< " + email + ">" ;
    }
}
