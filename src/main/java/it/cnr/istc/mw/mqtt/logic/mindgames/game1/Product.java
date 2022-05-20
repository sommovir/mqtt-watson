/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.cnr.istc.mw.mqtt.logic.mindgames.game1;

import it.cnr.istc.mw.mqtt.exceptions.MindGameException;
import it.cnr.istc.mw.mqtt.logic.mindgames.game1.Department;
import java.util.Date;
import java.util.Random;
import java.util.logging.Logger;

/**
 * name non può essere vuoto o nullo in caso contrario deve essere "unknown"
 *
 * @author Luca
 */
public class Product {

    private long id;
    private String name;//carota:carote:carote:carote
    private Department department = null;
    private String alternatives;
    public final String separatore = ",";
    public final String UNKNOWN = "unknown";

    public Product() {
    }

    public Product(long id, String name) {
        this.id = id;
        setName(name);
    }

    public Product(long id, String name, Department dep, String alternatives) throws MindGameException {
        this.id = id;
        setName(name);
        setDepartment(dep);
        this.alternatives = alternatives;

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

    public String getAlternatives() {
        return alternatives;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setAlternatives(String alternatives) {
        this.alternatives = alternatives;
    }

    public final void setName(String name) {
        if (name == null || name.isEmpty()) {
            this.name = UNKNOWN;
        } else {
            this.name = name;
        }
    }

    public void setDepartment(Department department) throws MindGameException {

        if (department == null) {
            throw new MindGameException() {
                @Override
                public String errorMessage() {
                    return "NULL PRODUCT";
                }
            };
        }
        this.department = department;
    }

    public String get() {
        String[] alternative = this.alternatives.split(separatore);
        Random random = new Random(new Date().getTime());
        int rnd = random.nextInt(alternative.length);
        return alternative[rnd];
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof Product) {
            Product p = (Product) obj;
            if ((p.getDepartment() == null && this.department == null || p.getDepartment().equals(this.department))
                    && p.getName().equals(this.name)
                    && p.getId() == this.getId()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return this.alternatives.split(",")[0] + " " + this.name;
    }

}
