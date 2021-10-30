/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.cnr.istc.mw.mqtt.logic.mindgames;

import java.util.logging.Logger;

/**
 *
 * @author Luca
 */
public class Product extends Department{
    
    private long id;
    private String name;
    private Department department = new Department(id, name);

    public Product() {
    }

    public Product(long id, String name) {
        this.id = id;
        this.name = name;
    }
    
    public Product(long id, String name, Department dep) {
        this.id = id;
        this.name = name;
        this.department = dep;
    }
    

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Department getDepartment() {
        return department;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null){
            return false;
        }
        if(obj instanceof Product p){
            if(p.getDepartment().equals(this.department)&&p.getName().equals(this.name)){
                return true;
            }else{
                return false;
            }
    }else{
            return false;
        }
    }
    
    
    
    
    

    
    
    
    
    
    
    
}
