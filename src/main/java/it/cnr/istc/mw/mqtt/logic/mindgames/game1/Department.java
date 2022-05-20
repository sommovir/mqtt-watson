/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.cnr.istc.mw.mqtt.logic.mindgames.game1;

/**
 * name non può essere vuota o nulla in caso contrario name sarà "unknown" 
 * @author Luca
 */
public class Department {
    
    private long id;
    private String name;
    public static final String UNKNOWN = "unknown";

    public Department() {
    }

    public Department(long id, String name) {
        this.id = id;
        setName(name);
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(long id) {

        this.id = id;
    }

    public final void setName(String name) {
       if (name == null || name.isEmpty()) {
            this.name = UNKNOWN;
        } else {
            this.name = name;
        }
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj == null){
            return false;
        }
        if(obj instanceof Department){
            Department p = (Department)obj;
            if(p.getName().equals(this.name)&&p.getId() == this.id){
                return true;
            }else{
                return false;
            }
    }else{
            return false;
        }
    }
    
}
