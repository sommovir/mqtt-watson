/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.cnr.istc.mw.mqtt.logic.config;

/**
 * Fare una BUILDER
 *
 * @author sommovir
 */
public class WatsonPropertyKey {

    private String scope; //obbligatorio
    private String account; //obbligatorio
    private WatsonAuthParam param; //obbligatorio
    private boolean defaultParam = false; //se true forza il retrieve di questo param dal file hard-coded
    private boolean verbose = false; //se true avvisa in console se il parametro manca nel file
    private String freeParam = null; //se diverso da null sovrascrive param
    private String subParam = null; //se diverso da null viene aggiunto in coda al param

    public String key() {
        String sub = this.subParam == null ? "" : "."+this.subParam;
        if (freeParam == null) {
            return scope + "." + account + "." + param.getParam()+sub;
        }else{
            return scope + "." + account + "." + freeParam+sub;
        }
    }
    
    
    private WatsonPropertyKey(Builder builder){
        
    this.account=builder.account;
    this.defaultParam=builder.defaultParam;
    this.freeParam=builder.freeParam;
    this.param=builder.param;
    this.scope=builder.scope;
    this.subParam=builder.subParam;
    this.verbose=builder.verbose;
    }
    public static class Builder {
        
        private String scope; //obbligatorio
        private String account; //obbligatorio
        private WatsonAuthParam param; //obbligatorio
        private boolean defaultParam = false; //se true forza il retrieve di questo param dal file hard-coded
        private boolean verbose = false; //se true avvisa in console se il parametro manca nel file
        private String freeParam = null; //se diverso da null sovrascrive param
        private String subParam = null; //se diverso da null viene aggiunto in coda al param

        public Builder(String scope, String account, WatsonAuthParam param) {
            this.scope = scope;
            this.account = account;
            this.param =param;
        }

        public Builder setDefaultParam(boolean defaultParam) {
            this.defaultParam = defaultParam;
            return this;
        }

        public Builder setVerbose(boolean verbose) {
            this.verbose = verbose;
            return this;
        }

        public Builder setFreeParam(String freeParam) {
            this.freeParam = freeParam;
            return this;
        }

        public Builder setSubParam(String subParam) {
            this.subParam = subParam;
            return this;
        }
        public WatsonPropertyKey build(){
            return new WatsonPropertyKey(this);
        }
        
        
        
    }
}
